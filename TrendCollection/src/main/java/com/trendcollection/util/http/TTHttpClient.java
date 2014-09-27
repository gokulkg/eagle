package com.trendcollection.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.util.EntityUtils;

import com.trendcollection.util.http.TTHttpResponse;

import org.apache.log4j.Logger;

/**
 *
 */
public class TTHttpClient {

	private  int coolOffTime;
	private java.util.Date lastCall = new java.util.Date();//null;
	private HttpClient httpClient = null;

	private static final Logger log = Logger.getLogger(TTHttpClient.class);


	public TTHttpClient(int coolOffTime) {
		PoolingClientConnectionManager conMan = new PoolingClientConnectionManager( SchemeRegistryFactory.createDefault() );
        conMan.setMaxTotal(200);
        conMan.setDefaultMaxPerRoute(200);
		
		httpClient = new DefaultHttpClient(conMan);
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
		        CookiePolicy.BROWSER_COMPATIBILITY);
		this.coolOffTime = coolOffTime;
	}
	
	public TTHttpClient() {
		PoolingClientConnectionManager conMan = new PoolingClientConnectionManager( SchemeRegistryFactory.createDefault() );
        conMan.setMaxTotal(200);
        conMan.setDefaultMaxPerRoute(200);
		
		httpClient = new DefaultHttpClient(conMan);
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
		        CookiePolicy.BROWSER_COMPATIBILITY);
	}

	/*
	 * Implementing the logic for retry mechanism. For outer class, this function should be 
	 * called
	 */
	public TTHttpResponse fetchResponse(String url)
	{
		int retry = 0;
		TTHttpResponse response = null;
		while(retry < 3)
		{
			response = fetchResponse(url, retry);
			
			if(response == null || response.getStatusCode() != 200)
			{
				retry++;
				continue;
			}
			else
			{
				return response;
			}
		}
		
		log.error("retry max for url = " + url);
		//returning the status error code and error message.
		return response;
	}

	@SuppressWarnings({ "finally"})
	private TTHttpResponse fetchResponse(String url, int retry)
	{
		TTHttpResponse ttResponse = null;
		HttpResponse response = fetchURL(url);
		
		if(response == null)
		{
			/*null response, no need to process it further*/
			return null;
		}
		String content = "";

		int statusCode = response.getStatusLine().getStatusCode();
		String statusLine = response.getStatusLine().toString();

		if ( statusCode == 500) {
			System.out.println("statusCode == 500 \tinURL: "+url);
			HttpEntity entity = response.getEntity();
			try {
				content = EntityUtils.toString(entity);
				System.out.println(content);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("errorInFetchingURL: "+url);
			}
		}
		if ( statusCode == 200) {
			HttpEntity entity = response.getEntity();

			try {
				content = EntityUtils.toString(entity);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("errorInFetchingURL: "+url);
			}finally{
				ttResponse = new TTHttpResponse(statusCode, statusLine, content);
				return ttResponse;
			}
		}
		else if(response.getStatusLine().getStatusCode() == 400) 
		{
			HttpEntity entity = response.getEntity();
			try {
				content = EntityUtils.toString(entity);
				System.out.println("Error:" + response.getStatusLine());
				System.out.println("Error: " + content);
				if(retry == 2){
					System.out.println("fetchingURL: "+url);
				}
				System.out.println("EXITING!!!!!!!!!!!!!!!!");
				
			} catch (Exception e) {
				e.printStackTrace();
				return new TTHttpResponse(statusCode, statusLine, content);
			} 
		}
		else {
			System.out.println("Error:" + response.getStatusLine());
			System.out.println("fetchingURL: "+url);
			return new TTHttpResponse(statusCode, statusLine, content);
		}

		return new TTHttpResponse(statusCode, statusLine, content);
	}

/*
 * Function to implement retry logic in case where not able to make connection with the host.
 */
	private  HttpResponse fetchURL(String url){

		HttpGet get = null;
		HttpResponse response = null;
		
		try{
		get = new HttpGet(url);
		}
		catch(Exception e)
		{
			log.error("Ill Formed url :" + url);
			log.error("stack:" , e);
			return response;
		}
		int reTry = 0;
		while(reTry <3){
			try{
				coolOff();
				response=	httpClient.execute(get);
				return response;
			}catch(Exception e){
				log.warn("stack:", e);
				reTry++;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					log.warn("stack:", e1);
				}
				//creating the new connection manager
				try
				{
					httpClient.getConnectionManager().shutdown();
				}
				catch(Exception e1)
				{
					log.warn("stack:", e1);
				}
				finally
				{
					httpClient = new DefaultHttpClient();
				}
			}
		}
		return response;
	}

	public void coolOff(){
		if(null == lastCall)
			lastCall = new java.util.Date();
		java.util.Date now = new java.util.Date();

		try {
			if( now.getTime() - lastCall.getTime() < coolOffTime)
			{
				//System.out.println("cooling off!!");
				long t = now.getTime() - lastCall.getTime();
				t = (t > 200) ? t : 1000;//seting minimum time out to 1 sec 
				Thread.sleep(t);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lastCall = new java.util.Date();
	}

}