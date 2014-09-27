package com.trendcollection.topology;

import com.trendcollection.topology.*;

import backtype.storm.StormSubmitter;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class TrendTopology {

	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {
		TopologyBuilder builder = new TopologyBuilder();

		Config conf = new Config();
		
		builder.setSpout("bingKeywordNews", new BingKeywordNewsSpout(), 1);
		builder.setBolt("bingKeywordNewsCollector",
				new BingKeywordNewsCollectorBolt(), 1).shuffleGrouping(
				"bingKeywordNews");	
	
		builder.setBolt("googleNewsCollector", new GoogleNewsCollectorBolt(), 3)
		.shuffleGrouping("bingKeywordNews");
		
		builder.setSpout("googleTopStories", new NewsSpout(), 1);
		builder.setBolt("googleTopStoryCollector", new GoogleTopStoryCollectorBolt(), 3)
		.shuffleGrouping("googleTopStories");
			
		builder.setBolt("newsInsertion", new NewsInsertionBolt(), 3)
				 .shuffleGrouping("googleTopStoryCollector")
				 .shuffleGrouping("googleNewsCollector")
				 .shuffleGrouping("bingKeywordNewsCollector");
	
		builder.setBolt("newsContentsCollection",
				new NewsContentCollectionBolt(), 3)
				.shuffleGrouping("newsInsertion");
		
		builder.setBolt("cassandraNewsInsertion",
				new CassandraNewsInsertionBolt(), 3).shuffleGrouping(
				"newsContentsCollection");


		if (args.length > 1) {
			conf.setNumWorkers(3);
			conf.setDebug(true);
			StormSubmitter.submitTopology(args[0], conf,
					builder.createTopology());
		} else {
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("trends_collection", conf,
					builder.createTopology());
		}
	}
}
