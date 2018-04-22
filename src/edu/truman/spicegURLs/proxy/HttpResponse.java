package edu.truman.spicegURLs.proxy;

import java.io.Serializable;
import java.util.*;

/**
 * Stores the status code, headers, and data of an HTTP response.
 * @author tanne
 */
public class HttpResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String code;
	private Map<String, List<String>> headers;
	private byte[] content;
	
	/**
	 * Constructs an empty HTTP response with only the status code.
	 * @param code HTTP status code
	 */
	public HttpResponse(String code) {
		this(code, null, new byte[0]);
	}
	
	/**
	 * Constructs an HTTP response with a status code, headers, and the data payload.
	 * @param code HTTP status code
	 * @param headers HTTP response headers
	 * @param response byte[] of any kind of data
	 */
	public HttpResponse(String code, Map<String, List<String>> headers, byte[] response) {
		this.code = code;
		this.headers = headers;
		this.content = response;
	}
	
	/**
	 * Return the HTTP response status in the following format:
	 * 200 OK
	 * 404 Not Found
	 * @return string of response status
	 */
	public String getResponseCode() {
		return code;
	}
	
	/**
	 * Returns a byte array of the full HTTP response to
	 * send back.
	 * @return byte[] of HTTP response
	 */
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
		
		// convert the string of headers into a byte array
		byte[] head = String.valueOf(sb).getBytes();

		// combine the head and content byte arrays into one
		byte[] combined = new byte[head.length + content.length];
		System.arraycopy(head, 0, combined, 0, head.length);
		System.arraycopy(content, 0, combined, head.length, content.length);
		
		return combined;
	}
}
