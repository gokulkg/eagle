package com.trendcollection.topology;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.trendcollection.manager.MysqlHelper;
import com.trendcollection.manager.models.NewsObject;
import com.trendcollection.util.rss.model.FeedMessage;

public class NewsInsertionBolt extends BaseRichBolt {
	private OutputCollector _collector;
	private MysqlHelper mH;
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
			_collector = collector;
			mH = new MysqlHelper();
	}

	@Override
	public void execute(Tuple input) {
		System.out.println("Inserting news");
		final NewsObject news = (NewsObject) input.getValueByField("news");
		_collector.ack(input);
		final Integer newsId = mH.insertNews(news);
		if (newsId != -1)
			_collector.emit(new Values(news, newsId));
	}

	// @Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("news", "id"));
	}
}