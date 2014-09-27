package com.trendcollection.util.goose;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.gravity.goose.Article;
import com.gravity.goose.Configuration;
import com.gravity.goose.Goose;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.trendcollection.util.http.HttpPostClient;

public class GooseTextExtractor {
	private static Goose goose = null;
	private HttpPostClient http;
	private Configuration configuration;

	public GooseTextExtractor() {
		try {
			configuration = new Configuration();
			http = new HttpPostClient();
			configuration.setEnableImageFetching(false);
			goose = new Goose(configuration);
		} catch (Exception e) {
			System.out.println("Goose is not connected");
		}
	}

	public List<String> getContent(String url) {
		String rawHTML = scrape(url);
		List<String> content = new ArrayList<String>();
		try {

			Article article = goose.extractContent("http://tookitaki.com",
					rawHTML);
			content.add(article.title());
			content.add(article.cleanedArticleText());

			return content;
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("not found" + url);
			content.add("");
			content.add("");
			return content;
		}
	}

	public String getContentString(String url) {
		String rawHTML = scrape(url);
		String content = "";
		if (!rawHTML.equals("")) {
			try {
				Article article = goose.extractContent("http://tookitaki.com",
						rawHTML);
				content += article.title();
				content += article.cleanedArticleText();
				return content;
			} catch (Exception e) {
				System.out.println("goose can not extract from this url :"
						+ url);
				return content;
			}
		}
		return content;
	}

	public String scrape(String urlString) {
		String line = null, data = "";
		try {
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				data += line + "\n";
			}
		} catch (Exception e) {

		}
		return data;
	}

	public static void main(String[] args) throws Exception {
		GooseTextExtractor gE = new GooseTextExtractor();
		System.out
				.println(gE
						.getContentString("http://timesofindia.indiatimes.com/india/LTC-scam-Livid-TMC-accuses-Modi-govt-of-vendetta/articleshow/36511954.cms"));
	}

}