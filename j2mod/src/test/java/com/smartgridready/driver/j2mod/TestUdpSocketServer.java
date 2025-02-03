package com.smartgridready.driver.j2mod;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestUdpSocketServer {
	
	private static final Logger LOG = LogManager.getLogger(TestUdpSocketServer.class);

	private DatagramSocket serverSocket;
	private boolean stopFlag = false;

	public void start() throws Exception {
		stopFlag = false;
		Thread server = new ServerHandler();
		server.start();
	}
	
	public void disconnect() throws Exception {
		stopFlag = true;
	}
	
	class ServerHandler extends Thread {
		
		@Override
		public void run() {							
			try {
				serverSocket = new DatagramSocket(9099);
				
				while(!stopFlag) {
					handleMessage();
				}
				serverSocket.close();
			} catch (Exception e) {
				LOG.info("Server socket closed.");
			}
		}

		private void handleMessage() {
			final int buflen = 1024;
			ByteBuffer bbuf = ByteBuffer.wrap(new byte[buflen+1]);
			byte[] buf = new byte[buflen];
			
			try {
				DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
				serverSocket.receive(inPacket);
				
				InetAddress srcAddress = inPacket.getAddress();
				int srcPort = inPacket.getPort();
				int srcLen = inPacket.getLength();

				bbuf.put(new byte[] {0});
				bbuf.put(inPacket.getData(), 0, srcLen);

				DatagramPacket outPacket = new DatagramPacket(bbuf.array(), srcLen+1, srcAddress, srcPort);
				serverSocket.send(outPacket);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

