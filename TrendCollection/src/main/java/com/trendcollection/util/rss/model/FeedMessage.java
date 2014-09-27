package com.trendcollection.util.rss.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedMessage {

	  String title;
	  String description;
	  String link;
	  String author;
	  String guid;
	  String pubDate;

	  public String getTitle() {
	    return title;
	  }
	  
	  public void setTitle(String title) {
		    this.title = (title.isEmpty()) ? "-na-" :title;
		  }

	  public String getPubDate() {
		    return pubDate;
		  }
		  
	  public void setPubDate(String pubDate) {
	    this.pubDate = (pubDate.isEmpty()) ? "-na-" : pubDate;
	  }

	  public String getDescription() {
	    return description;
	  }

	  public void setDescription(String description) {
	    this.description = (description.isEmpty()) ? "-na-" :description;
	  }

	  public String getLink() {
	    return link;
	  }

	  public void setLink(String link) {
	    this.link = (link.isEmpty()) ? "-na-" :link;
	  }

	  public String getAuthor() {
	    return author;
	  }

	  public void setAuthor(String author) {
	    this.author = (author.isEmpty()) ? "-na-" :author;
	  }

	  public String getGuid() {
	    return guid;
	  }

	  public void setGuid(String guid) {
	    this.guid = (guid.isEmpty()) ? "-na-" :guid;
	  }
	
	} 
