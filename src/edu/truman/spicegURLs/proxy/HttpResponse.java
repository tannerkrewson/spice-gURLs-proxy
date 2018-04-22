package edu.truman.spicegURLs.proxy;

import java.io.Serializable;
import java.util.*;

public class HttpResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String code;
	private Map<String, List<String>> headers;
	private byte[] content;
	
	public HttpResponse(String code, Map<String, List<String>> headers, byte[] response) {
		this.code = code;
		this.headers = headers;
		if (response != null) {
			this.content = response;
		} else {
			this.content = new byte[0];
		}
	}
	
	public String getResponseCode() {
		return code;
	}
	
	public byte[] getRawResponse() {
		// construct the headers as a string
		StringBuffer sb = new StringBuffer();
		
		sb.append("HTTP/1.1 ");
		sb.append(code);
		sb.append("\r\n");
		
		if (headers != null) {
			sb.append("Content-Type: ");
			sb.append(headers.get("Content-Type").get(0));
			sb.append("\r\n");
			
			// any other headers could be appended here in the same format as above
		}
		
		sb.append("\r\n");
		
		byte[] head = String.valueOf(sb).getBytes();

		// combine the head and content byte arrays into one
		byte[] combined = new byte[head.length + content.length];
		System.arraycopy(head, 0, combined, 0, head.length);
		System.arraycopy(content, 0, combined, head.length, content.length);
		
		return combined;
	}
}
