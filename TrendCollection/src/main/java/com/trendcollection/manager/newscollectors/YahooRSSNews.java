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

public class YahooRSSNews {
	private RSSFeedParser parser;
	public static TimeHelper timeHelper = new TimeHelper();

	public Map<String, String> getUrlsAndCountries() {
		Map<String, String> coutriesUrls = new HashMap<String, String>();
		File fXmlFile = new File("properties/countries/countries.xml");
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
					coutriesUrls.put(eElement.getAttribute("id"), eElement
							.getElementsByTagName("url").item(0).getTextContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return coutriesUrls;
		}
		return coutriesUrls;
	}

	public List<FeedMessage> getYahooRSSNewsFeeds(String cat, String url) {
		List<FeedMessage> newsFeeds = new ArrayList<FeedMessage>();
		try {
			Feed feed;
			try {
				parser = new RSSFeedParser(url + "/" + cat);
				feed = parser.readFeed();
				newsFeeds = feed.getMessages();
			} catch (Exception e) {
			}
		} catch (Exception e) {
			System.out.println("some error for fecting news for " + cat);
		}
		return newsFeeds;
	}

	public List<NewsObject> getYaahooNews(String cat, String country, String url) {
		List<NewsObject> yahooNews = new ArrayList<NewsObject>();
		List<FeedMessage> newsFeeds = getYahooRSSNewsFeeds(cat, url);
		NewsObject newsObject;
		Timestamp pubDate;
		for (FeedMessage feed : newsFeeds) {
			try {
				pubDate = timeHelper.getTimeStampTimeString(feed.getPubDate());
				if (timeHelper.getTimeDifference(pubDate) < 55) {
					newsObject = new NewsObject(feed.getTitle(),
							feed.getLink(), pubDate, "yahoo", country);
					yahooNews.add(newsObject);
				}
			} catch (Exception e) {
			}
		}
		return yahooNews;
	}

	public static void main(String[] args) {
		YahooRSSNews obj = new YahooRSSNews();
		Map<String, String> countries = obj.getUrlsAndCountries();
		for(Entry<String, String> entry : countries.entrySet()){
		    System.out.println("country .."+entry.getKey()+ "   url "+entry.getValue());       
			//	List<NewsObject> news = obj.getYaahooNews("", entry.getKey(), entry.getValue());
		//	for (NewsObject n : news)
		//		System.out.println(n.getTitle());
		}
		
		
	}

}
