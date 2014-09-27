package com.trendcollection.topology;

import java.util.List;
import java.util.Map;

import com.trendcollection.manager.newscollectors.RSSNews;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class RSSNewsSpout extends BaseRichSpout {
	SpoutOutputCollector _collector;
	RSSNews rssNews;

	public void open(Map confMap, TopologyContext context,
			SpoutOutputCollector collector) {
		rssNews = new RSSNews();
		_collector = collector;

	}

	public void nextTuple() {
		Map<String, List<String>> countryUrls = rssNews.getUrlsAndCountries();
		
	    for (Map.Entry<String, List<String>> entry : countryUrls.entrySet()) {
	           for(String url : entry.getValue())    	
				_collector.emit(new Values("", entry.getKey(), url));
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