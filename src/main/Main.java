package main;

import edu.truman.spicegURLs.proxy.Proxy;

public class Main {
	
	static final int PORT = 8080;

	public static void main(String[] args) {
		Thread t = new Thread(new Proxy(PORT));
		t.start();
	}

}
