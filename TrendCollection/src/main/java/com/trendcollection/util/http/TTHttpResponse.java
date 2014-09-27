package com.trendcollection.util.http;

public class TTHttpResponse {

	int statusCode;
	String statusLine;
	String body;

	public TTHttpResponse(int statusCode, String statusLine, String body) {
		this.statusCode = statusCode;
		this.statusLine = statusLine;
		this.body = body;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusLine() {
		return statusLine;
	}
	public void setStatusLine(String statusLine) {
		this.statusLine = statusLine;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

}