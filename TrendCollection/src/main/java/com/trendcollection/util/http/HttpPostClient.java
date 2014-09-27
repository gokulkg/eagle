package com.trendcollection.util.http;

import org.json.simple.*;

import java.util.Properties;
import java.util.regex.*;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class HttpPostClient {

	public HttpPostClient(){
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("properties/http/config.properties"));
		

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static String convertStreamToString(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public Integer postRequest(JSONObject json, String url) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(json.toString());
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream res = entity.getContent();

				String result = convertStreamToString(res);

				return parseId(result);
			}

			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	private TTHttpResponse fetchBingResponse(String url, int retry, String key) {
		TTHttpResponse ttResponse = null;
		HttpResponse response = fetchBingNewsURL(url, key);

		if (response == null) {
			/* null response, no need to process it further */
			return null;
		}
		String content = "";

		int statusCode = response.getStatusLine().getStatusCode();
		String statusLine = response.getStatusLine().toString();

		if (statusCode == 500) {
			System.out.println("statusCode == 500 \tinURL: " + url);
			HttpEntity entity = response.getEntity();
			try {
				content = EntityUtils.toString(entity);
				System.out.println(content);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("errorInFetchingURL: " + url);
			}
		}
		if (statusCode == 200) {
			HttpEntity entity = response.getEntity();

			try {
				content = EntityUtils.toString(entity);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("errorInFetchingURL: " + url);
			} finally {
				ttResponse = new TTHttpResponse(statusCode, statusLine, content);
				return ttResponse;
			}
		} else if (response.getStatusLine().getStatusCode() == 400) {
			HttpEntity entity = response.getEntity();
			try {
				content = EntityUtils.toString(entity);
				System.out.println("Error:" + response.getStatusLine());
				System.out.println("Error: " + content);
				if (retry == 2) {
					System.out.println("fetchingURL: " + url);
				}
				System.out.println("EXITING!!!!!!!!!!!!!!!!");

			} catch (Exception e) {
				e.printStackTrace();
				return new TTHttpResponse(statusCode, statusLine, content);
			}
		} else {
			System.out.println("Error:" + response.getStatusLine());
			System.out.println("fetchingURL: " + url);
			return new TTHttpResponse(statusCode, statusLine, content);
		}

		return new TTHttpResponse(statusCode, statusLine, content);
	}

	/*
	 * Function to implement retry logic in case where not able to make
	 * connection with the host.
	 */

	private HttpResponse fetchBingNewsURL(String url, String key) {

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet get = null;
		HttpResponse response = null;

		try {
			get = new HttpGet(url);
			get.addHeader("Authorization", key);
		} catch (Exception e) {
			return response;
		}
		try {
			response = httpClient.execute(get);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	public Integer parseId(String s) {
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(s);
		while (m.find()) {
			return Integer.parseInt(m.group());
		}
		return -1;
	}

	public JsonNode getBingNews(String url, String key) {
		JsonNode node = null;

		try {
			TTHttpResponse response = fetchBingResponse(url, 3, key);

			if (response.getStatusCode() == 500) {
				System.out.println("500 error in fetching url: " + url);
				url = null;

			}

			if (response.getStatusCode() == 200) {

				String content = response.getBody();

				ObjectMapper m = new ObjectMapper();
				node = m.readTree(content);

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return node;
	}

	public static void main(String[] args) {
	
	}



}
