package com.pg.plantapps.nextactivitiesappservice.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

public class NextActivity {
	
	private String equipment;
	private Integer unitId;
	private String sheetDesc;
	private Long sheetId;
	private Integer sheetType;
	private Integer duration;
	private Integer calendarTime;
	private Integer downTime;
	private Integer upTime;
	private Integer remainingTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Timestamp currentTimeStamp;
	private Long lastActivityID;
	private String lastActivityName;
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
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getSheetDesc() {
		return sheetDesc;
	}
	public void setSheetDesc(String sheetDesc) {
		this.sheetDesc = sheetDesc;
	}
	public Long getSheetId() {
		return sheetId;
	}
	public void setSheetId(Long sheetId) {
		this.sheetId = sheetId;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getCalendarTime() {
		return calendarTime;
	}
	public void setCalendarTime(Integer calendarTime) {
		this.calendarTime = calendarTime;
	}
	public Integer getDownTime() {
		return downTime;
	}
	public void setDownTime(Integer downTime) {
		this.downTime = downTime;
	}
	public Integer getUpTime() {
		return upTime;
	}
	public void setUpTime(Integer upTime) {
		this.upTime = upTime;
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
	
	public Integer getSheetType() {
		return sheetType;
	}
	public void setSheetType(Integer sheetType) {
		this.sheetType = sheetType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastActivityID == null) ? 0 : lastActivityID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NextActivity other = (NextActivity) obj;
		if (lastActivityID == null) {
			if (other.lastActivityID != null)
				return false;
		} else if (!lastActivityID.equals(other.lastActivityID))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "NextActivity [lastActivityID=" + lastActivityID + ", equipment=" + equipment + ", unitId=" + unitId
				+ ", sheetDesc=" + sheetDesc + ", sheetId=" + sheetId + ", sheetType=" + sheetType + ", duration="
				+ duration + ", calendarTime=" + calendarTime + ", downTime=" + downTime + ", upTime=" + upTime
				+ ", remainingTime=" + remainingTime + ", lastActivityName=" + lastActivityName + ", currentTimeStamp="
				+ currentTimeStamp + ", lastActivityStartTime=" + lastActivityStartTime + ", lastActivityEndTime="
				+ lastActivityEndTime + ", lastActivityStatus=" + lastActivityStatus + ", nextActivityEstimate="
				+ nextActivityEstimate + ", nextActivityEstimateCD=" + nextActivityEstimateCD + ", activityCDTracker="
				+ activityCDTracker + ", color=" + color + "]";
	}
	
	
	
	

}
