package com.trendcollection.util.mysql;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;

import com.mysql.jdbc.Driver;
import com.trendcollection.manager.models.*;
public class MySqlDatacollector {

	static String CONNECTION_URL;
	static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static String DB_USER_NAME;
	static String DB_USER_PASSWORD;
	static String DB_NAME;
	static String NEWS_TABLE = "news";
	
	Connection con = null;

	public MySqlDatacollector() {
		con = getConnection();
	}

	public Connection getConnection() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("properties/mysql/config.properties"));
			DB_USER_NAME = prop.getProperty("dbuser");
			DB_USER_PASSWORD = prop.getProperty("dbpassword");
			CONNECTION_URL = "jdbc:mysql://" + prop.getProperty("dbpath") + "/"
					+ prop.getProperty("database") + "?user=" + DB_USER_NAME
					+ "&password=" + DB_USER_PASSWORD;

			con = new com.mysql.jdbc.Driver().connect(CONNECTION_URL, null);
			return con;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Connection checkConnection() {

		try {
			if (con.isClosed()) {
				con = getConnection();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return con;

	}

	public ResultSet executeQuery(String query) throws IOException,
			SQLException {
		con = checkConnection();
		try {

			Statement statement = con.createStatement();
			ResultSet resultSet = null;

			ResultSet result = statement.executeQuery(query);
			return result;

		} catch (Exception e) {
			throw new RuntimeException("some sql excception!", e);
		} finally {
			// con.close();
		}

	}

	public Long insertQuery(String query) throws SQLException,
			ClassNotFoundException {
		con = checkConnection();
		Long numero = (long) 0;
		Long risultato = (long) -1;
		try {
			Statement stmt = con.createStatement();
			numero = (long) stmt.executeUpdate(query);

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				risultato = rs.getLong(1);
			}
			rs.close();

			stmt.close();
		} catch (Exception e) {
			
			risultato = (long) -1;
		} finally {
			// con.close();
		}
		return risultato;
	}

	public int insertNews(String title, String link, String country,
			String source, Integer catId, Integer subCatId, Timestamp pubDate) {
		PreparedStatement preparedStatement = null;
		con = checkConnection();
		try {
			preparedStatement = con
					.prepareStatement("insert into "
							+ NEWS_TABLE
							+ " (title, link, country, source, category_id, sub_category_id, pub_date) values (?,?,?,?,?,?,?) ");

			preparedStatement.setString(1, title);
			preparedStatement.setString(2, link);
			preparedStatement.setString(3, country);
			preparedStatement.setString(4, source);
			preparedStatement.setInt(5, (catId == null)  ? 0 : catId );
			preparedStatement.setInt(6, (subCatId == null) ? 0 : subCatId);
			preparedStatement.setTimestamp(7, pubDate);
		    preparedStatement.executeUpdate();
			 ResultSet rs = preparedStatement.getGeneratedKeys();
		        if (rs.next()){
		            return(rs.getInt(1));
		        }
		        rs.close();
          return -1;
		} catch (SQLException | NullPointerException e) {
			return -1;
		}

	}

	public void closeConnection() {
		try {
			con.close();
		} catch (Exception e) {

		}

	}

}