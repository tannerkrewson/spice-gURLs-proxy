package edu.truman.spicegURLs.proxy;

import java.net.*;
/**
 * An object which acts as a server and hosts ProxySessions.
 * @author Brandon Crane
 * @author Brandon Heisserer
 * @author Tanner Krewson
 * @author Carl Yarwood
 * @version 22 April 2018
 */
public class Proxy implements Runnable {
	
	private ServerSocket server;
	private Cache cache;
	private int port;
	
	/**
	 * Constructs a new proxy with a port
	 * @param port the port number to run this server on
	 */
	public Proxy (int port) {
		this.port = port;
	}
	
	/**
	 * Starts up the proxy server on the port
	 * @see java.lang.Runnable#run()
	 */
	public void run () {
        System.out.println("Listening for connection on port " + 
        		this.port + " ....");
        
        cache = new Cache();
        
        try {
        	this.server = new ServerSocket(this.port);
			while (true) {
            	Thread t = 
            		new Thread(new ProxySession(server.accept(), cache));
            	t.start();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
