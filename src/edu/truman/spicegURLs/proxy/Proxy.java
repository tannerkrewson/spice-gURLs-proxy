package edu.truman.spicegURLs.proxy;

import java.net.*;

public class Proxy implements Runnable {
	
	ServerSocket server;
	int port;
	
	public Proxy (int port) {
		this.port = port;
	}
	
	public void run () {
        System.out.println("Listening for connection on port " + this.port + " ....");
        try {
        	this.server = new ServerSocket(this.port);
			while (true) {
            	Socket socket = server.accept();
            	String message = "spice gURLs reporting for duty";
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + message;
                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                socket.close();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
