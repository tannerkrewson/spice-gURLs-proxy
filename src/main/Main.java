package main;

import java.io.*;

import edu.truman.spicegURLs.proxy.Proxy;

/**
 * Serves to get the system started.
 * @author Brandon Crane
 * @author Brandon Heisserer
 * @author Tanner Krewson
 * @author Carl Yarwood
 * @version 22 April 2018
 */
public class Main {
	
	static final int PORT = 8080;

	public static void main(String[] args) throws IOException {
		Thread t = new Thread(new Proxy(PORT));
		t.start();
		
        BufferedReader reader = 
        		new BufferedReader(new InputStreamReader(System.in));
		do {
			System.out.println("Enter \"exit\" to terminate the proxy.");
		} while (!reader.readLine().trim().equals("exit"));
		System.exit(0);
	}

}
