package com.trendcollection.util.cassandra;

import java.io.FileInputStream;
import java.util.Properties;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class ConnectionManager {

	private static Cluster cluster;
	private static String HOST_IP;
	private static Integer PORT;
	private static Integer HIGHER_TIMEOUT = 600000;

	public ConnectionManager() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(
					"properties/cassandra/config.properties"));
			HOST_IP = prop.getProperty("hostip");
			PORT = Integer.parseInt(prop.getProperty("port"));
		
			cluster = getCluster();
			
			System.out.println("connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Session setSession() {
		try {
			 Session session = cluster.connect();
			return session;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(1000 * 2);
				return null;
			} catch (InterruptedException e1) {
				return null;
			}

		}
	}
	
	public Session getSession(){
		Session session = null;
		while (session == null)
			session = setSession();		
	    return session;		
	}
	
	public Cluster setCluster() {
		try {
			try{
			cluster.shutdown();
			}catch(Exception e){
			}
			
			cluster = Cluster.builder().addContactPoint(HOST_IP).withPort(PORT).build();
			cluster.getConfiguration().getSocketOptions().setReadTimeoutMillis(HIGHER_TIMEOUT);
			return cluster;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(1000);
				return null;
			} catch (InterruptedException e1) {
				return null;
			}

		}
	}
	
	public Cluster getCluster(){
		cluster = null;
		while (cluster == null)
			cluster = setCluster();
		
	    return cluster;		
	}
	public void shutdownCluster(){
		cluster.shutdown();
	}
	
	public static void main(String[] args){
		ConnectionManager obj = new ConnectionManager();
	}

}
