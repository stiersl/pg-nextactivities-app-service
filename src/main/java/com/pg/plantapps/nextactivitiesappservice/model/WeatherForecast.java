package com.pg.plantapps.nextactivitiesappservice.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class WeatherForecast {
	
	private String summary;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date timeStamp;
	private double highTemp;
	private double lowTemp;
	
	public double getHighTemp() {
		return highTemp;
	}
	public void setHighTemp(double highTemp) {
		this.highTemp = highTemp;
	}
	public double getLowTemp() {
		return lowTemp;
	}
	public void setLowTemp(double lowTemp) {
		this.lowTemp = lowTemp;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
	
}
