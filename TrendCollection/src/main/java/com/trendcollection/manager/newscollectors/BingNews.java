package com.trendcollection.manager.newscollectors;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.trendcollection.manager.models.NewsObject;
import com.trendcollection.util.http.HttpPostClient;
import com.trendcollection.util.time.TimeHelper;

public class BingNews {
	public static String API_KEY = "";
	public static String SOURCE = "bing";	
	public static String KEY_WORD_SORCE = "bing_keyword";
	public static String URL = "https://api.datamarket.azure.com/Bing/Search/News";
	public HttpPostClient client = new HttpPostClient();
	public static TimeHelper timeHelper = new TimeHelper();
	
	public BingNews(){
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("properties/bing/config.properties"));
			API_KEY = prop.getProperty("key");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getBas64Key() {
		byte[] encodedBytes = Base64.encodeBase64((API_KEY + ":" + API_KEY)
				.getBytes());
		String key = new String(encodedBytes);

		return ("Basic " + key);
	}

	public List<NewsObject> getBingNews(String cat, String country) {
		try {
			try {
				cat = URLEncoder.encode(cat, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String url = URL + "?$format=json&Query='" + cat + "'&$top=100";

			if (!country.isEmpty() && !country.equals(""))
				url += "&Market='" + country + "'";
			
			JsonNode node = client.getBingNews(url, getBas64Key());

			List<NewsObject> bingNews = getBingNewsObjects(
					node.get("d").get("results"), cat);

			return bingNews;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (new ArrayList<NewsObject>());
		}
	}

	public List<NewsObject> getBingNewsObjects(JsonNode node, String cat) {
		String source = SOURCE;
		if (!cat.isEmpty() && !cat.equals(""))
			source = KEY_WORD_SORCE;
		List<NewsObject> bingNews = new ArrayList<NewsObject>();

		NewsObject bingNewsObj;
		String title = "";
		String text = "";
		String link = "";
		Timestamp pubDate;

		for (JsonNode obj : node) {
			try {
				pubDate = timeHelper.getTimeStamp((obj.get("Date"))
						.getTextValue());
				if (timeHelper.getTimeDifference(pubDate) < 55) {
					title = (obj.get("Title")).getTextValue();
					text = (obj.get("Description")).getTextValue();
					link = (obj.get("Url")).getTextValue();
					bingNewsObj = new NewsObject(title, link,
							pubDate, source, "-na-");
					bingNews.add(bingNewsObj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return bingNews;
	}

	public static void main(String[] args) {
		List<NewsObject> news = new BingNews().getBingNews("cricket", "en-US");
		System.out.println(news.size());
		for (NewsObject n : news) {
			System.out.println(n.getTitle());

		}
	}

}
