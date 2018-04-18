package main;

import edu.truman.spicegURLs.proxy.Proxy;

/**
 * Serves to get the system started.
 * @author Brandon Crane
 * @author Brandon Heisserer
 * @author Tanner Krewson
 * @author Carl Yarwood
 * @version 17 April 2018
 */
public class Main {
	
	static final int PORT = 8080;

	public static void main(String[] args) {
		Thread t = new Thread(new Proxy(PORT));
		t.start();
	}

}
