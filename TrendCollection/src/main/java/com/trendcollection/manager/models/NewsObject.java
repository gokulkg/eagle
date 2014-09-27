package com.trendcollection.manager.models;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class NewsObject {

	private SignalObject signal;
	private String title;
	private Timestamp pubDate;
	private String country;
	private String link;
	private String content;
	private String source;
	private int score;

	public NewsObject(String title, String link,
			Timestamp pubDate, String source,String country) {
		this.title = title;
		this.pubDate = pubDate;
		this.link = link;
		this.score = 0;
		this.source = source;
		this.country = country;
		this.content = "-na-";
	}

	public SignalObject getSignal() {
		return signal;
	}

	public int getScore() {
		return score;
	}

	public String getTitle() {
		return title;
	}

	public Timestamp getPubDate() {
		return pubDate;
	}

	public String getLink() {
		return link;
	}

	
	public String getCountry() {
		return country;
	}
	
	public void setSignal(SignalObject s){
		this.signal = s;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getSource() {
		return source;
	}

}