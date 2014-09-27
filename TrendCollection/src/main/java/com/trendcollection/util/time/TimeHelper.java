package com.trendcollection.util.time;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeHelper {
	public Timestamp getTimeStamp(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date dt = null;
		try {
			dt = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Timestamp(new Date().getTime());
		}
		return new Timestamp(dt.getTime());
	}
	
	public Timestamp getTimeStampTimeString(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"EEE',' dd MMM yyyy HH:mm:ss Z");
		Date dt = null;
		try {
			dt = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Timestamp(new Date().getTime());
		}
		return new Timestamp(dt.getTime());
	}

	public long getTimeDifference(Timestamp Time) {
		try {
			long oldTime = Time.getTime();
			long currentTime = System.currentTimeMillis();
			long diff = currentTime - oldTime;
			long diffHours = diff / (60 * 60 * 1000);
			return diffHours;
		} catch (Exception e) {
			return -1;
		}
	}
	public static void main(String[] args){
	
	}
	
}
