package edu.truman.spicegURLs.proxy;

import java.io.Serializable;

public class HttpResponse implements Serializable {
	
	private int code;
	private String content;
	
	public HttpResponse(int code, String response) {
		this.code = code;
		this.content = response;
	}
	
	public int getResponseCode() {
		return code;
	}
	
	public String toString() {
		StringBuffer response = new StringBuffer();
		
		response.append("\r\n");
		response.append(content);
		return response.toString();
	}
}
