package com.pg.plantapps.nextactivitiesappservice.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pg.plantapps.nextactivitiesappservice.model.WeatherForecast;
import com.pg.plantapps.nextactivitiesappservice.model.dao.WeatherForecastDAO;

@RestController
@CrossOrigin
@RequestMapping("/weather")
public class WeatherForecastController {

	private WeatherForecastDAO weatherForecastDAO;

	@Autowired
	public WeatherForecastController(WeatherForecastDAO weatherForecastDAO) {
		this.weatherForecastDAO = weatherForecastDAO;
	}

	@GetMapping("/")
	public List<WeatherForecast> getWeatherForcasts() {
		List<WeatherForecast> result = weatherForecastDAO.getWeatherForcast();
		if (result == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		System.out.println("calling get Weather Forcast:");
		return result;
	}

}