package com.smartgridready.driver.j2mod;

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
		
		@Override
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

