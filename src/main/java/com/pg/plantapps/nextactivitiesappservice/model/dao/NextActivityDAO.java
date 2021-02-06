package com.pg.plantapps.nextactivitiesappservice.model.dao;

import java.util.List;

import com.pg.plantapps.nextactivitiesappservice.model.NextActivity;

public interface NextActivityDAO {
	// when passed a string of puids return a list of Activity Estimates
	public List<NextActivity> getNextActivities(String puids);
	
}
