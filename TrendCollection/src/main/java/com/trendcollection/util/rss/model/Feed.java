package com.trendcollection.util.rss.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Stores an RSS feed
 */
public class Feed {

  String title;
  String link;
  String description;
  String language;
  String copyright;
  String pubDate;

  final List<FeedMessage> entries = new ArrayList<FeedMessage>();

  public Feed(String title, String link, String description, String language,
      String copyright, String pubDate) {
    this.title = (title.isEmpty()) ? "-na-" : title;
    this.link = (link.isEmpty()) ? "-na-" :link;
    this.description =(description.isEmpty()) ? "-na-" : description;
    this.language = (language.isEmpty()) ? "-na-" :language;
    this.copyright = (copyright.isEmpty()) ? "-na-" :copyright;
    this.pubDate = (pubDate.isEmpty()) ? "-na-" :pubDate;
  }

  public List<FeedMessage> getMessages() {
    return entries;
  }

  public String getTitle() {
    return title;
  }

  public String getLink() {
    return link;
  }

  public String getDescription() {
    return description;
  }

  public String getLanguage() {
    return language;
  }

  public String getCopyright() {
    return copyright;
  }

  public String getPubDate() {
    return pubDate;
  }

  @Override
  public String toString() {
    return "Feed [copyright=" + copyright + ", description=" + description
        + ", language=" + language + ", link=" + link + ", pubDate="
        + pubDate + ", title=" + title + "]";
  }

} 
