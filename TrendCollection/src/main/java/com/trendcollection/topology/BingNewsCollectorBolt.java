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
import com.trendcollection.manager.newscollectors.BingNews;

public class BingNewsCollectorBolt extends BaseRichBolt {
	private OutputCollector _collector;
	private BingNews bingNews;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		bingNews = new BingNews();
		
	}

	@Override
	public void execute(Tuple input) {
		final String country = input.getString(0);
			
		final List<NewsObject> bingNewsObjs = bingNews.getBingNews("", "en-"+country);
		
		_collector.ack(input);
		
		for (NewsObject bingNewsObj : bingNewsObjs) {			
			_collector.emit(new Values(bingNewsObj));		    
		}
	}

	//@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		 declarer.declare(new Fields("news"));	
	}

}
