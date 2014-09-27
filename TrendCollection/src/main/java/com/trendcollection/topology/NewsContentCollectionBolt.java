package com.trendcollection.topology;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.trendcollection.manager.models.NewsObject;
import com.trendcollection.util.goose.GooseTextExtractor;

public class NewsContentCollectionBolt extends BaseRichBolt {
	private OutputCollector _collector;
	private GooseTextExtractor goose;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		goose = new GooseTextExtractor();
	}

	@Override
	public void execute(Tuple input) {
		final NewsObject news = (NewsObject) input.getValueByField("news");
		final Integer newsId = input.getInteger(1);
		_collector.ack(input);
		if (newsId != -1) {
			news.setContent(goose.getContentString(news.getLink()));
			_collector.emit(new Values(news, newsId));
		}
	}

	// @Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("news", "id"));
	}

}