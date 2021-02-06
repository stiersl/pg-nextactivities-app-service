package com.pg.plantapps.nextactivitiesappservice.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public class NextActivity {
	private Long lastActivityID;
	private String equipment;
	private String sheet_Desc;
	private Integer ude_RunTimeTarget;
	private Integer totalCT;
	private Integer totalDT;
	private Integer totalUT;
	private Integer remainingTime;
	private String lastActivityName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Timestamp currentTimeStamp;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ssZ")
	private Timestamp lastActivityStartTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ssZ")
	private Timestamp lastActivityEndTime;
	private String lastActivityStatus;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ssZ")
	private Timestamp nextActivityEstimate;
	private String nextActivityEstimateCD;
	private String activityCDTracker;
	private Integer color;
	public Long getLastActivityID() {
		return lastActivityID;
	}
	public void setLastActivityID(Long lastActivityID) {
		this.lastActivityID = lastActivityID;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public String getSheet_Desc() {
		return sheet_Desc;
	}
	public void setSheet_Desc(String sheet_Desc) {
		this.sheet_Desc = sheet_Desc;
	}
	public Integer getUde_RunTimeTarget() {
		return ude_RunTimeTarget;
	}
	public void setUde_RunTimeTarget(Integer ude_RunTimeTarget) {
		this.ude_RunTimeTarget = ude_RunTimeTarget;
	}
	public Integer getTotalCT() {
		return totalCT;
	}
	public void setTotalCT(Integer totalCT) {
		this.totalCT = totalCT;
	}
	public Integer getTotalDT() {
		return totalDT;
	}
	public void setTotalDT(Integer totalDT) {
		this.totalDT = totalDT;
	}
	public Integer getTotalUT() {
		return totalUT;
	}
	public void setTotalUT(Integer totalUT) {
		this.totalUT = totalUT;
	}
	public Integer getRemainingTime() {
		return remainingTime;
	}
	public void setRemainingTime(Integer remainingTime) {
		this.remainingTime = remainingTime;
	}
	public String getLastActivityName() {
		return lastActivityName;
	}
	public void setLastActivityName(String lastActivityName) {
		this.lastActivityName = lastActivityName;
	}
	public Timestamp getCurrentTimeStamp() {
		return currentTimeStamp;
	}
	public void setCurrentTimeStamp(Timestamp currentTimeStamp) {
		this.currentTimeStamp = currentTimeStamp;
	}
	public Timestamp getLastActivityStartTime() {
		return lastActivityStartTime;
	}
	public void setLastActivityStartTime(Timestamp lastActivityStartTime) {
		this.lastActivityStartTime = lastActivityStartTime;
	}
	public Timestamp getLastActivityEndTime() {
		return lastActivityEndTime;
	}
	public void setLastActivityEndTime(Timestamp lastActivityEndTime) {
		this.lastActivityEndTime = lastActivityEndTime;
	}
	public String getLastActivityStatus() {
		return lastActivityStatus;
	}
	public void setLastActivityStatus(String lastActivityStatus) {
		this.lastActivityStatus = lastActivityStatus;
	}
	public Timestamp getNextActivityEstimate() {
		return nextActivityEstimate;
	}
	public void setNextActivityEstimate(Timestamp nextActivityEstimate) {
		this.nextActivityEstimate = nextActivityEstimate;
	}
	public String getNextActivityEstimateCD() {
		return nextActivityEstimateCD;
	}
	public void setNextActivityEstimateCD(String nextActivityEstimateCD) {
		this.nextActivityEstimateCD = nextActivityEstimateCD;
	}
	public String getActivityCDTracker() {
		return activityCDTracker;
	}
	public void setActivityCDTracker(String activityCDTracker) {
		this.activityCDTracker = activityCDTracker;
	}
	public Integer getColor() {
		return color;
	}
	public void setColor(Integer color) {
		this.color = color;
	}

}
