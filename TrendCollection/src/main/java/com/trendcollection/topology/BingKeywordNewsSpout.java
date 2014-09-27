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

public class BingKeywordNewsSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private Signal signal;
	SpoutOutputCollector _collector;

	public void open(Map confMap, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
		signal = new Signal();
	}

	public void nextTuple() {
		List<SignalObject> signals = signal.getSignal();
		for (SignalObject s : signals) {
			_collector.emit(new Values(s));
			try {
				Thread.sleep(1200);// sleep for 1.2 sec
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		try {
			Thread.sleep(1000 * 60 * 60 * 24);// sleep for 12 hours
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("signal"));
	}

}