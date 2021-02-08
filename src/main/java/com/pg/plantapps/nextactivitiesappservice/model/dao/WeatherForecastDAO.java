package com.pg.plantapps.nextactivitiesappservice.model.dao;

import java.util.List;
import com.pg.plantapps.nextactivitiesappservice.model.WeatherForecast;

public interface WeatherForecastDAO {

	public List<WeatherForecast> getWeatherForcast();
}
