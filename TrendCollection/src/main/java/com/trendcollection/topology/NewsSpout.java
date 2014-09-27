package com.trendcollection.topology;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.trendcollection.manager.Signal;
import com.trendcollection.manager.models.SignalObject;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class NewsSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private Signal signal;
	SpoutOutputCollector _collector;

	public void open(Map confMap, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
		signal = new Signal();
	}

	public void nextTuple() {
		//passing empty signal
		SignalObject s = new SignalObject("", null, "", null);

		  _collector.emit(new Values(s));

		try {
			Thread.sleep(1000 * 60 * 60 * 2);// sleep for 2 hours
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("signal"));
	}

}