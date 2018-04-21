package edu.truman.spicegURLs.proxy;

import java.io.Serializable;
import java.util.*;

public class HttpResponse implements Serializable {
	
	private int code;
	private Map<String, List<String>> headers;
	private String content;
	
	public HttpResponse(int code, Map<String, List<String>> headers, String response) {
		this.code = code;
		this.headers = headers;
		this.content = response;
	}
	
	public int getResponseCode() {
		return code;
	}
	
	public String toString() {
		StringBuffer response = new StringBuffer();
		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			
			// important because first header key is always null
			if (entry.getKey() == null) continue;
			
			response.append(entry.getKey());
			response.append(": ");
			
			//convert list to string
			String val = entry.getValue().toString().trim();
			val = val.substring(1, val.length()-1);
			
			//remove leading and trailing brackets
			response.append(val);
			
			response.append("\r\n");
		}
		response.append("\r\n");
		response.append(content);
		return response.toString();
	}
}
