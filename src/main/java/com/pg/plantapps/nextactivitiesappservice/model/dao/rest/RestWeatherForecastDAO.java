package com.pg.plantapps.nextactivitiesappservice.model.dao.rest;

import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.pg.plantapps.nextactivitiesappservice.model.WeatherForecast;
import com.pg.plantapps.nextactivitiesappservice.model.dao.WeatherForecastDAO;

@Component
public class RestWeatherForecastDAO implements WeatherForecastDAO {

	

		private static class DarkSkyDataPoint {
			// this sets up the objects that are within the each elment of the Data array
			public String summary;
			public String icon;
			public String precipType;
			public Long time;
			public Double temperatureLow;
							
			@JsonAnySetter
			public Map<String, Double> details = new HashMap<>();
		}

		private static class DarkSkyDataBlock {
			// data is the array object within the daily object
			public List<DarkSkyDataPoint> data;
		}

		private static class DarkSkyForecast {
			//note: daily is the first object
			public DarkSkyDataBlock daily;
		}
		
		private static final String BASE_URL = "https://api.darksky.net/forecast/2fe39f20e170590d1147117710367b6e/";

		private RestTemplate restTemplate = new RestTemplate();
		
		private Timestamp timestamp = new Timestamp(0);
		
		@Override
		public List<WeatherForecast> getWeatherForcast() {
			
			String coordinates = "37.8267,-122.4233";
			DarkSkyForecast forecast = restTemplate.getForObject(BASE_URL + coordinates, DarkSkyForecast.class);
			
			List<WeatherForecast> results = new ArrayList<>();
			for (DarkSkyDataPoint dp : forecast.daily.data) {
				WeatherForecast w = new WeatherForecast();
				w.setHighTemp(dp.details.get("temperatureHigh"));
				w.setLowTemp(dp.temperatureLow);
				w.setSummary(dp.summary);
				w.setTimeStamp(new Timestamp(dp.time * 1000));
				results.add(w);		
			}
			
			return results;
		}

	}
	