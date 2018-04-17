package edu.truman.spicegURLs.proxy;
import java.net.*;
public class ProxySession implements Runnable {

	private Socket client;
	public ProxySession(Socket client){
		this.client = client;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			String message = "spice gURLs reporting for duty";
	        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + message;
	        client.getOutputStream().write(httpResponse.getBytes("UTF-8"));
	        System.out.println(client.getInetAddress());
	        client.close();
		}catch(Exception e){
			
		}
	}

}
