package com.trendcollection.topology;

import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.trendcollection.manager.models.NewsObject;
import com.trendcollection.manager.newscollectors.RSSNews;

public class RSSNewsCollectorBolt extends BaseRichBolt {
	private OutputCollector _collector;
	private RSSNews rssNews;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		rssNews = new RSSNews();		
	}

	@Override
	public void execute(Tuple input) {
		final String cat = input.getString(0);
		final String country = input.getString(1);
		final String url = input.getString(2);
			
		final List<NewsObject> newsObjs = rssNews.getRSSNews(cat, country, url);		
		_collector.ack(input);
		for (NewsObject newsObj : newsObjs) {			
			_collector.emit(new Values(newsObj));		    
		}
	}

	//@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		 declarer.declare(new Fields("news"));	
	}

}
