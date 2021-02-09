package com.pg.plantapps.nextactivitiesappservice.model.dao.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.pg.plantapps.nextactivitiesappservice.model.Unit;
import com.pg.plantapps.nextactivitiesappservice.model.dao.UnitDAO;


@Component
public class RestUnitDAO implements UnitDAO{

	private static class MyMachine {
		public String name;
		public Long assetId;
		public String type;
		public Long lineId;
		public String lineName;
	}

	private static class MyMachines {
		// data is the array object within the daily object
		public List<MyMachine> data;
	}
	
	private static final String BASE_URL = "https://pugophub:8444/mymachines-service-impl-0.6.5/myMachines/v1/units/preferred-units?previousPreferences=0&retrieveChildren=false";

	private RestTemplate restTemplate = new RestTemplate();
	
	
	
	@Override
	public List<Unit> getPreferredUnits(String token) {
	
		System.out.println("token:" + token);
		
		// create Headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		//HttpEntity request = new HttpEntity(headers);
		
		
		ResponseEntity<MyMachine[]> unitResponseEntity = restTemplate.exchange(BASE_URL, HttpMethod.GET, request, MyMachine[].class);
		
		if(unitResponseEntity == null || unitResponseEntity.getBody() == null) {
			return null;
		}
		
		ResponseEntity<MyMachine[]> myMachines = unitResponseEntity;
		
		List<Unit> results = new ArrayList<>();
		for (MyMachine mm : myMachines.getBody()) {
			Unit unit = new Unit();
			unit.setUnitId(mm.assetId);	
			unit.setEquipment(mm.name);
			unit.setType(mm.type);
			unit.setLineId(mm.lineId);
			unit.setLine(mm.lineName);
			results.add(unit);		
		}
		
		return results;
	}

}

