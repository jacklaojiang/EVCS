package model;

import java.util.Date;

public class Gantt{
		int vehicleId;
		Date startTime;
		int duration;
		int chargingPoint;
		int EVType;
		void setCustomerID(int ID)
		{
			this.vehicleId = ID;
		}
		int getCustomerID()
		{
			return this.vehicleId ;
		}
		void setStartTime(Date start)
		{
			this.startTime = start;
		}
		Date getStartTime()
		{
			return this.startTime;
		}
		void setDuration(int duration)
		{
			this.duration = duration;
		}
		int getDuration()
		{
			return this.duration;
		}
		void setChargingPoint(int chargingPoint)
		{
			this.chargingPoint = chargingPoint;
		}
		int getChargingPoint()
		{
			return this.chargingPoint;
		}
		void setEVType(int EVType)
		{
			this.EVType = EVType;
		}
		int getEVType()
		{
			return this.EVType;
		}
	}