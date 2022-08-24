package de.re.easymodbus.adapter;
/**
*Copyright(c) 2021 Verein SmartGridready Switzerland
* @generated NOT
* 
This Open Source Software is BSD 3 clause licensed:
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in 
   the documentation and/or other materials provided with the distribution.
3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from 
   this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
OF THE POSSIBILITY OF SUCH DAMAGE.

It includes completely manually generated code. It is the Interface of the Modbus RTU inside the CommHandler and its purpose is to enable 3rd party programmers to
use their own Modbus RTU drivers
 */


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestSocketServer {
	
	private static final Logger LOG = LogManager.getLogger(TestSocketServer.class);

	private ServerSocket serverSocket;
	private boolean closeFlag = false;
	private boolean stopFlag = false;

	public void start() throws Exception {
		stopFlag = false;
		Thread server = new ServerHandler();
		server.start();
	}
	
	public void disconnect() throws Exception {
		stopFlag = true;
	}
	
	public void setCloseFlag(boolean closeFlag) {
		this.closeFlag = closeFlag;
	}
	
	class ServerHandler extends Thread {
		
		public void run() {							
			try {
				serverSocket = new ServerSocket(9099);
				
				while(!stopFlag) {
					Socket socket = serverSocket.accept();				
					Thread handler = new ClientHandler(socket);
					handler.start();
				}
				serverSocket.close();
			} catch (Exception e) {
				LOG.info("Server socket closed.");
			}
		}		
	}
		

	class ClientHandler extends Thread {
		
		final InputStream is;
		final OutputStream os;
		final Socket s;

		// Constructor
		public ClientHandler(Socket s) throws Exception {
			this.s = s;
			this.is = s.getInputStream();
			this.os = s.getOutputStream();
		}				

		@Override
	    public void run() 
	    {
			final int buflen = 1024;
			ByteBuffer bbuf = ByteBuffer.wrap(new byte[buflen+1]);
			try {
				byte[] buf = new byte[buflen];				
				int count;			
				while ((count = is.read(buf)) >= 0 && !closeFlag && !stopFlag) {
					bbuf.put(new byte[] {0});
					bbuf.put(buf, 0, count);
					os.write(bbuf.array(), 0, count+1);
				}				
				if(closeFlag) {
					LOG.info("Client socket forcefully closed.");
				}
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}	
}

