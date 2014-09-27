package com.trendcollection.manager;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.trendcollection.manager.models.SignalObject;
import com.trendcollection.util.http.HttpPostClient;
import com.trendcollection.util.mysql.MySqlDatacollector;

public class Signal {
	public HttpPostClient http;
	public MySqlDatacollector mDC;

	public Signal() {
		mDC = new MySqlDatacollector();
	}

	public List<SignalObject> getSignal() {
		List<SignalObject> signals = new ArrayList<SignalObject>();
        SignalObject signal;
		String query = "SELECT sc.*, c.name as cname FROM `sub_category` sc  left join category c on sc.category_id = c.id WHERE 1 ";
		try {
			
			ResultSet res = mDC.executeQuery(query);
			while (res.next()) {
				try {
					signal = new SignalObject(res.getString("cname"), res.getInt("category_id"), res.getString("name"), res.getInt("id"));			
				    signals.add(signal);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("no signals  found");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return signals;
	}

	public static void main(String[] args) {
		Signal obj = new Signal();

		 List<SignalObject> signals = obj.getSignal();
		 for(SignalObject s : signals){
			 System.out.println(s.getSubCategoryId());
		 }
	}

}
