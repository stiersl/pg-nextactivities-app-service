package com.pg.plantapps.nextactivitiesappservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pg.plantapps.nextactivitiesappservice.model.NextActivity;
import com.pg.plantapps.nextactivitiesappservice.model.dao.NextActivityDAO;

@RestController
@CrossOrigin
@RequestMapping("/nextActivities")
public class NextActivitiesRestController {

	private NextActivityDAO activityEstimateDAO;

	@Autowired
	public NextActivitiesRestController(NextActivityDAO activityEstimateDAO) {
		this.activityEstimateDAO = activityEstimateDAO;
	}

	@GetMapping("/")
	public String getreply() {
		return "please supply puids";
	}

	@GetMapping("/{puids}")
	public List<NextActivity> getNextActivities(@PathVariable String puids) {
		List<NextActivity> result = activityEstimateDAO.getNextActivities(puids);
		if (result == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		System.out.println("calling rest API:" + puids);
		return result;
	}

}

