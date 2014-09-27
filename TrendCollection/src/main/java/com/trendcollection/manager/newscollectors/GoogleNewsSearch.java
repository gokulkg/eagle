package com.trendcollection.manager.newscollectors;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trendcollection.manager.models.NewsObject;
import com.trendcollection.util.rss.RSSFeedParser;
import com.trendcollection.util.rss.model.Feed;
import com.trendcollection.util.rss.model.FeedMessage;
import com.trendcollection.util.time.TimeHelper;

public class GoogleNewsSearch {
	private static String BASE_URL = "https://news.google.co.in/news/feeds?output=rss";
	private static String CF = "&cf=all";
	private static String NED = "&ned";
	private static String NUM = "&num=100";

	private RSSFeedParser parser;
	private static TimeHelper timeHelper = new TimeHelper();

	public Feed getGoogleNewsFeeds(String url) {
		Feed feed = null;
		try {
			parser = new RSSFeedParser(url);
			feed = parser.readFeed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feed;
	}

	public List<NewsObject> getNewsforTopicAndKeyword(String topic, String key) {
		List<NewsObject> news = new ArrayList<NewsObject>();
		String url = BASE_URL + CF + NUM + "&t="+topic+"&q="+key;
		Feed feed = getGoogleNewsFeeds(url);
		NewsObject newsObj;
		Timestamp time;
		String link;
		if (feed != null)
			for (FeedMessage feedmessage : feed.getMessages()) {
				time = timeHelper.getTimeStampTimeString(feedmessage
						.getPubDate());
				link = extractUrl(feedmessage.getLink());
				newsObj = new NewsObject(removeNewsSource(feedmessage.getTitle()), link, time, "google", "-na-");
				news.add(newsObj);
			}

		return news;
	}
	
	public List<NewsObject> getTopStories() {
		List<NewsObject> news = new ArrayList<NewsObject>();
		String url = BASE_URL + CF + NUM;
		Feed feed = getGoogleNewsFeeds(url);
		NewsObject newsObj;
		Timestamp time;
		String link;
		if (feed != null)
			for (FeedMessage feedmessage : feed.getMessages()) {
				time = timeHelper.getTimeStampTimeString(feedmessage
						.getPubDate());
				link = extractUrl(feedmessage.getLink());
				newsObj = new NewsObject(removeNewsSource(feedmessage.getTitle()), link, time, "top_stories", "-na-");
				news.add(newsObj);
			}

		return news;
	}

	public String extractUrl(String s) {
		String clusterUrlPattern = "\\b(url=https|url=http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*";

		String url = (getLastMatch(s, clusterUrlPattern)).replace("url=", "");
		return (url);
	}

	public static String getLastMatch(String s, String p) {
		String match = "";
		Matcher m = Pattern.compile(p).matcher(s);
		while (m.find()) {
			match = m.group(0);
		}
		return match;
	}
	
	public String removeNewsSource(String text) {
		try {
			String[] parts = text.split(" - ");
			return parts[0];
		} catch (Exception e) {
			e.printStackTrace();
			return text;
		}
	}
	public static void main(String[] args) {
		List<NewsObject> news = new GoogleNewsSearch().getTopStories();
		for(NewsObject n : news){
			System.out.println(n.getTitle());
		}

	}

}
