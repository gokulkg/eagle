package com.trendcollection.manager;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cassandra.thrift.Cassandra.system_add_column_family_args;

import com.trendcollection.manager.models.NewsObject;
import com.trendcollection.util.mysql.MySqlDatacollector;
import com.trendcollection.util.rss.model.FeedMessage;

public class MysqlHelper {
	private MySqlDatacollector mDC;

	public MysqlHelper() {
		mDC = new MySqlDatacollector();
	}

	public Integer insertNews(NewsObject news) {
		
		Integer id = mDC.insertNews(news.getTitle(), news.getLink(),news.getCountry(),news.getSource(), news.getSignal().getCategoryId(), news.getSignal().getSubCategoryId(), news.getPubDate());
		return id;
	}

}
