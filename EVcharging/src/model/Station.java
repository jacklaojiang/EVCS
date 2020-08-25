package model;

import java.util.Date;

public class Station {
	private int stationId;
	private int type;
	private Date nextAvaTime;
	
	public void setStationId(int id) {
		this.stationId = id;
	}
	public int getStationId() {
		return stationId;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	
	public void setNextAvaTime(Date nextAvaTime) {
		this.nextAvaTime = nextAvaTime;
	}
	public Date getNextAvaTime() {
		return nextAvaTime;
	}

}
