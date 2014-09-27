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
import com.trendcollection.util.cassandra.CassandraWriter;

public class CassandraNewsInsertionBolt extends BaseRichBolt {
	private OutputCollector _collector;
	private CassandraWriter cW;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		cW = new CassandraWriter();
	}

	@Override
	public void execute(Tuple input) {
		final NewsObject news = (NewsObject) input.getValueByField("news");
		final Integer newsId = input.getInteger(1);
		_collector.ack(input);
		
		if (newsId != -1){
			cW.isertNews(news, newsId);
		}
	}
	// @Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

}