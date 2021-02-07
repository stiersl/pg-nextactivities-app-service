package com.pg.plantapps.nextactivitiesappservice.model;

import java.util.Objects;

public class Sheet {
	private Long sheetId;
	private String sheetDesc;
	private Integer sheetType;
	private Integer unitId;
	private String equipment;
	private Integer eventSubTypeId;
	private Integer ecId;
	private Integer duration;
	
	
	public Long getSheetId() {
		return sheetId;
	}
	public void setSheetId(Long sheetId) {
		this.sheetId = sheetId;
	}
	public String getSheetDesc() {
		return sheetDesc;
	}
	public void setSheetDesc(String sheetDesc) {
		this.sheetDesc = sheetDesc;
	}
	public Integer getSheetType() {
		return sheetType;
	}
	public void setSheetType(Integer sheetType) {
		this.sheetType = sheetType;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public Integer getEventSubTypeId() {
		return eventSubTypeId;
	}
	public void setEventSubTypeId(Integer eventSubTypeId) {
		this.eventSubTypeId = eventSubTypeId;
	}
	public Integer getEcId() {
		return ecId;
	}
	public void setEcId(Integer ecId) {
		this.ecId = ecId;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sheet other = (Sheet) obj;
		if (sheetId == null) {
			if (other.sheetId != null)
				return false;
		} else if (!sheetId.equals(other.sheetId))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(sheetId);
	}
	
	@Override
	public String toString() {
		return "Sheet [sheetId=" + sheetId + ", sheetDesc=" + sheetDesc + ", sheetType=" + sheetType + ", unitId="
				+ unitId + ", equipment=" + equipment + ", eventSubTypeId=" + eventSubTypeId + ", ecId=" + ecId
				+ ", duration=" + duration + "]";
	}

}

