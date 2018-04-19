package edu.truman.spicegURLs.proxy;

import java.net.*;
/**
 * An object which acts as a server and hosts ProxySessions.
 * @author Brandon Crane
 * @author Brandon Heisserer
 * @author Tanner Krewson
 * @author Carl Yarwood
 * @version 17 April 2018
 */
public class Proxy implements Runnable {
	
	ServerSocket server;
	Cache cache;
	int port;
	
	public Proxy (int port) {
		this.port = port;
	}
	
	public void run () {
        System.out.println("Listening for connection on port " + this.port + " ....");
        
        cache = new Cache();
        
        try {
        	this.server = new ServerSocket(this.port);
			while (true) {
            	Thread t = new Thread(new ProxySession(server.accept(), cache));
            	t.start();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
