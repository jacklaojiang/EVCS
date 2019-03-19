package model;

import java.util.Date;

public class Vehicle {
	private int vehicleId;
	private int type;
	private Date startTime;
	private Date endTime;
	private int miles;
	private int eDuration;
	private int lDuration;
	
	public void setVehicleId(int id) {
		this.vehicleId = id;
	}
	public int getVehicleId() {
		return vehicleId;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}

	public void SetstartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void SetEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setMiles(int miles) {
		this.miles = miles;
	}
	public int getMiles() {
		return miles;
	}
	
	public void setEDuration(int eduration) {
		this.eDuration = eduration;
	}
	public int getEDuration() {
		return eDuration;
	}
	
	public void setLDuration(int lDuration) {
		this.lDuration = lDuration;
	}
	public int getLDuration() {
		return lDuration;
	}
}
