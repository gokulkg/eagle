package com.trendcollection.util.jedis;

import java.io.FileInputStream;
import java.util.Properties;

import redis.clients.jedis.Jedis;

public class JedisHelper {
	private Jedis jedis;
	private String HOST_IP;
	private int HOST_PORT;
	private int TIME_OUT = 1000000;

	public JedisHelper() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("properties/jedis/config.properties"));
			HOST_IP = prop.getProperty("hostip");
			HOST_PORT = Integer.parseInt(prop.getProperty("hostport"));

			jedis = new Jedis(HOST_IP, HOST_PORT, TIME_OUT);
			jedis.connect();
			if (jedis.isConnected()) {
				System.err.println("Connected HOST_IP = " + HOST_IP
						+ "HOST PORT = " + HOST_PORT);
			} else {
				System.err.println("Could not connecte to REDIS");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void checkConnection() {
        try{		
		if (!jedis.isConnected() || !jedis.ping().equals("PONG")){
			jedis = new Jedis(HOST_IP, HOST_PORT, TIME_OUT);
		    jedis.connect();
		}
        }catch (Exception e){
        	jedis = new Jedis(HOST_IP, HOST_PORT, TIME_OUT);
        	connect();
        }
	}

	public String hGet(String key, String field) {
		checkConnection();
		return (jedis.hget(key, field));
	}

	public String Get(String key) {
		checkConnection();
		return (jedis.get(key));
	}

	public Long hSet(String key, String field, String value) {
		checkConnection();
		return (jedis.hset(key, field, value));
	}

	public String set(String key, String value) {
		checkConnection();
		return jedis.set(key, value);
	}

	public void connect() {
		try {
			System.out.println("Jedis Client: Connecting");
			jedis.connect();
		} catch (Exception e) {
			e.printStackTrace();

			while (!jedis.isConnected()) {
				try {
					System.out.println("Jedis Client: Retrying to Connect");
					Thread.sleep(1000 * 2);
					jedis.connect();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		} finally {
			if (null != jedis) {
				System.out.println("Jedis Client: CONNECTED");
			}
		}
	}

	public void disconnect() {
		if (jedis.isConnected()) {
			jedis.disconnect();
		}
	}

	public static void main(String[] args) {

		JedisHelper jed = new JedisHelper();
		jed.set("asda", "asdadsadas");
		String s = jed.Get("aasfsa");
		if (s == null)
			System.out.println(s);
		System.out.println(jed.Get("asda"));

	}

}