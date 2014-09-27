package com.trendcollection.topology;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trendcollection.manager.newscollectors.YahooRSSNews;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class YahooNewsSpout extends BaseRichSpout {
	private static String DOMAINS = "entertainment,sports,science,fashion,business,travel,tech,world,us,politics,health,bollywood,hollywood";
	private static final long serialVersionUID = 1L;
	SpoutOutputCollector _collector;
	YahooRSSNews yahooRSS;

	public void open(Map confMap, TopologyContext context,
			SpoutOutputCollector collector) {
		yahooRSS = new YahooRSSNews();
		_collector = collector;

	}

	public void nextTuple() {
		Map<String, String> countryUrls = yahooRSS.getUrlsAndCountries();
		List<String> items = Arrays.asList(DOMAINS.split("\\s*,\\s*"));
		for (String cat : items) {
			for (Map.Entry<String, String> entry : countryUrls.entrySet()) {
				_collector.emit(new Values(cat, entry.getKey(), entry.getValue()));
			}
		}
		try {
			Thread.sleep(1000 * 60 * 60 * 4);// sleep for 4 hours
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("category", "country", "url"));
	}

}