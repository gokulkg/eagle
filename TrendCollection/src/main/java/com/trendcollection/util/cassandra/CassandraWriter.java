package com.trendcollection.util.cassandra;

import java.sql.Timestamp;
import java.util.Date;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.trendcollection.manager.models.NewsObject;

public class CassandraWriter {

	private static String KEY_SPACE = "trends";
	private static String NEWS_TABLE_NAME = "news";
	private static String FB_POST_TABLE_NAME = "fb_posts";
	private ConnectionManager con;

	public CassandraWriter() {
		con = new ConnectionManager();
	}

	public void isertNews(NewsObject news, Integer newsId) {
		try {
			Session session = con.getSession();
			session.execute(getInsertQueryString(news, session, newsId));
			session.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("some error happened in writing news in cassandra for news _id: "
							+ newsId);
		}
	}

	public BoundStatement getInsertQueryString(NewsObject news,
			Session session, Integer newsId) {
		String query = "INSERT INTO "
				+ KEY_SPACE
				+ "."
				+ NEWS_TABLE_NAME
				+ " ( news_id, category, source, country, title, link, content, pub_date) VALUES(?,?,?,?,?,?,?,?)";

		PreparedStatement prepStatement = session.prepare(query);
		prepStatement.setConsistencyLevel(ConsistencyLevel.ONE);

		BoundStatement cql = prepStatement.bind( newsId,
				news.getSignal().getSubCategory(), news.getSource(), news.getCountry(),
				news.getTitle(), news.getLink(), news.getContent(),
				(Date) news.getPubDate());

		return cql;
	}
}