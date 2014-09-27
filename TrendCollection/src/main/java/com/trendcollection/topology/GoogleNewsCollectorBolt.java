package com.trendcollection.topology;
import java.util.List;
import java.util.Map;

import com.trendcollection.manager.models.NewsObject;
import com.trendcollection.manager.models.SignalObject;
import com.trendcollection.manager.newscollectors.GoogleNewsSearch;
import com.trendcollection.util.rss.model.FeedMessage;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class GoogleNewsCollectorBolt extends BaseRichBolt {
	private OutputCollector _collector;
    private GoogleNewsSearch gNS;
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		_collector = collector;
		gNS = new GoogleNewsSearch();

	}

	@Override
	public void execute(Tuple input) {
		final SignalObject signal = (SignalObject) input.getValueByField("signal");
			
		final List<NewsObject> newsObjs = gNS.getNewsforTopicAndKeyword(signal.getCategory(), signal.getSubCategory());
		
		_collector.ack(input);
		
		for (NewsObject news : newsObjs) {
			news.setSignal(signal);
			_collector.emit(new Values(news));		    
		}
	}

	//@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		 declarer.declare(new Fields("news"));	
	}

}
