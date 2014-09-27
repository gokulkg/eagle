package com.trendcollection.manager.newscollectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.io.File;
import java.sql.Timestamp;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import com.trendcollection.manager.models.NewsObject;
import com.trendcollection.util.rss.RSSFeedParser;
import com.trendcollection.util.rss.model.Feed;
import com.trendcollection.util.rss.model.FeedMessage;
import com.trendcollection.util.time.TimeHelper;

public class RSSNews {
	private RSSFeedParser parser;
	public static TimeHelper timeHelper = new TimeHelper();

	public Map<String, List<String>> getUrlsAndCountries() {
		Map<String, List<String>> coutriesUrls = new HashMap<String, List<String>>();
		File fXmlFile = new File("properties/rss/countries.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("country");

			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					List<String> urls = new ArrayList<String>();
					for (int j = 0; j < eElement.getElementsByTagName("url")
							.getLength(); ++j) {
						urls.add(eElement.getElementsByTagName("url").item(j)
								.getTextContent());
					}
					if (urls.size() > 0)
						coutriesUrls.put(eElement.getAttribute("id"), urls);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return coutriesUrls;
		}

		return coutriesUrls;
	}

	public List<FeedMessage> getRSSNewsFeeds(String url) {
		List<FeedMessage> newsFeeds = new ArrayList<FeedMessage>();
		try {
			Feed feed;
			try {
				parser = new RSSFeedParser(url);
				feed = parser.readFeed();
				newsFeeds = feed.getMessages();
			} catch (Exception e) {
			}
		} catch (Exception e) {
			System.out.println("some error for fecting news for " + url);
		}
		return newsFeeds;
	}

	public List<NewsObject> getRSSNews(String cat, String country, String url) {
		List<NewsObject> yahooNews = new ArrayList<NewsObject>();
		List<FeedMessage> newsFeeds = getRSSNewsFeeds(url);
		NewsObject newsObject;
		Timestamp pubDate;
		for (FeedMessage feed : newsFeeds) {
			try {
				pubDate = timeHelper.getTimeStampTimeString(feed.getPubDate());
				if (timeHelper.getTimeDifference(pubDate) < 55) {
					newsObject = new NewsObject(feed.getTitle(),feed.getLink(), pubDate, "rss", country);
					yahooNews.add(newsObject);
				}
			} catch (Exception e) {
			}
		}
		return yahooNews;
	}

	public static void main(String[] args) {
		
		RSSNews obj = new RSSNews();
		Map<String, List<String>> countries = obj.getUrlsAndCountries();
		for (Entry<String, List<String>> entry : countries.entrySet()) {

			for (String url : entry.getValue()) {
				System.out.println("country .." + entry.getKey() + "   url "
						+ url);
				List<NewsObject> news = obj.getRSSNews("", entry.getKey(), url);
				for  (NewsObject n : news)
				  System.out.println(n.getTitle());
			}
		}
	}

}
