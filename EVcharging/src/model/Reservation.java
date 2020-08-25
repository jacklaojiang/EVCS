package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





public class Reservation {
	ArrayList<Vehicle> vehicle;
	Map<Integer, List<Station>> station;
	int vehicleNum;
	
	/**
	 * prioritize the vehicle 
	 */
	public ArrayList<Vehicle> vehicleSort(ArrayList<Vehicle> vehicleList) throws ParseException{
		Date startDate[] = new Date[vehicleList.size()];
		Date endDate[] = new Date[vehicleList.size()];
		int order[] = new int[vehicleList.size()];
		int id=0;
		Date min = new Date();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		min = df.parse("24:00");
		for(int i =0;i<vehicleList.size()-1;i++)
		{
			for(int j=i+1;j<vehicleList.size();j++)
			{
				if(vehicleList.get(i).getEndTime().getTime()>vehicleList.get(j).getEndTime().getTime()) 
				{
					Vehicle tempI = vehicleList.get(i);
					Vehicle tempJ = vehicleList.get(j);
					vehicleList.set(i, tempJ);
					vehicleList.set(j, tempI);
				}
				if(vehicleList.get(i).getEndTime().getTime()==vehicleList.get(j).getEndTime().getTime() &&
						vehicleList.get(i).getStartTime().getTime()>vehicleList.get(j).getStartTime().getTime())
				{
					Vehicle tempI = vehicleList.get(i);
					Vehicle tempJ = vehicleList.get(j);
					vehicleList.set(i, tempJ);
					vehicleList.set(j, tempI);
				}
				if(vehicleList.get(i).getStartTime().getTime()==vehicleList.get(j).getStartTime().getTime() &&
						vehicleList.get(i).getEndTime().getTime()==vehicleList.get(j).getEndTime().getTime()&&
						vehicleList.get(i).getMiles()<vehicleList.get(j).getMiles())
				{
					Vehicle tempI = vehicleList.get(i);
					Vehicle tempJ = vehicleList.get(j);
					vehicleList.set(i, tempJ);
					vehicleList.set(j, tempI);
				}
				if(vehicleList.get(i).getStartTime().getTime()==vehicleList.get(j).getStartTime().getTime() &&
						vehicleList.get(i).getEndTime().getTime()==vehicleList.get(j).getEndTime().getTime()&&
						vehicleList.get(i).getMiles()==vehicleList.get(j).getMiles()&&
						vehicleList.get(i).getType()>vehicleList.get(j).getType())
				{
					Vehicle tempI = vehicleList.get(i);
					Vehicle tempJ = vehicleList.get(j);
					vehicleList.set(i, tempJ);
					vehicleList.set(j, tempI);
				}
			}
		}
		vehicle = vehicleList;
		//test
//		for(Vehicle buf : vehicleInfo)
//		{
//			int a = buf.getVehicleId();
//			System.out.println("ID:"+a);
//		}
		//test
		
		//get the start time of first charge
		for(Vehicle vehicle: vehicleList)
		{
			if(vehicle.getStartTime().getTime()<=min.getTime())
			{
				min = vehicle.getStartTime();
			}
		}
		
		//set the start time of first charge to each point
		for(int key=1;key<5;key++)
		{
			for(int i=0;i<station.get(key).size();i++)
			{
				station.get(key).get(i).setNextAvaTime(min);
			}
		}
		return vehicle;
	}
	
	public void parse(){
		for(Vehicle vehicle : vehicle)
		{
			int miles = 0;
			int eDuration = 0;
			int lDuration = 0;
			switch(vehicle.getType())
			{
				case 1:
					miles = vehicle.getMiles();
					eDuration = (int) (miles/1.2);
					lDuration = (int) (miles/0.37);
					break;
				case 2:
					miles = vehicle.getMiles();
					eDuration = (int) (miles/2.2);
					lDuration = (int) (miles/0.4);
					break;
				case 3:
					miles = vehicle.getMiles();
					eDuration = (int) (miles/2.67);
					lDuration = (int) (miles/1.42);
					break;
			}
			vehicle.setEDuration(eDuration);
			vehicle.setLDuration(lDuration);
		}
	}
	
	public ArrayList<Gantt> greedy() throws ParseException{
		ArrayList<Gantt> ganttList = new ArrayList<Gantt>();
		Gantt gantt;
		for (Vehicle vehicle : vehicle) {
			int miles = 0;
			int eDuration = 0;
			int lDuration = 0;
			int alreadyAssign = 0;
			gantt = new Gantt();
			Date next = new Date();
			int stationPos = 0;
			SimpleDateFormat df= new SimpleDateFormat("HH:mm");
			Date Min = df.parse("24:00");
			switch(vehicle.getType())
			{
				case 1:
					miles = vehicle.getMiles();
					eDuration = (int) (miles/1.22);
					lDuration = (int) (miles/0.37);
					//fast
					if( alreadyAssign == 0)
					{
						Min = df.parse("24:00");
						stationPos = 0;
						for(int i=0;i<station.get(2).size();i++)
						{
							Station checkStation = station.get(2).get(i);
							Date buf = checkStation.getNextAvaTime();
							if(buf.getTime()<Min.getTime())
							{
								Min = buf;
								stationPos = i;
							}
						}
						next = Min;
						if(next.getTime()<=vehicle.getStartTime().getTime())  
						{
							if((vehicle.getStartTime().getTime()+eDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(vehicle.getStartTime());
								gantt.setDuration(eDuration);
								gantt.setChargingPoint(station.get(2).get(stationPos).getStationId());
								Date nextNew = new Date(vehicle.getStartTime().getTime()+eDuration*60*1000);
								station.get(2).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
						if(next.getTime()>vehicle.getStartTime().getTime() && 
								next.getTime()<vehicle.getEndTime().getTime())
						{
							if((next.getTime()+eDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(next); //diff
								gantt.setDuration(eDuration);
								gantt.setChargingPoint(station.get(2).get(stationPos).getStationId());
								Date nextNew = new Date(next.getTime()+eDuration*60*1000); //diff
								station.get(2).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
					}
					//last
					if(alreadyAssign == 0)
					{
						Min = df.parse("24:00");
						stationPos = 0;
						for(int i=0;i<station.get(1).size();i++)
						{
							Station checkStation = station.get(1).get(i);
							Date buf = checkStation.getNextAvaTime();
							if(buf.getTime()<Min.getTime())
							{
								Min = buf;
								stationPos = i;
							}
							
						}
						next = Min;
						if(next.getTime()<=vehicle.getStartTime().getTime())
						{
							if((vehicle.getStartTime().getTime()+lDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(vehicle.getStartTime());
								gantt.setDuration(lDuration);
								gantt.setChargingPoint(station.get(1).get(stationPos).getStationId());
								Date nextNew = new Date(vehicle.getStartTime().getTime()+lDuration*60*1000);
								station.get(1).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
						if(next.getTime()>vehicle.getStartTime().getTime() && 
								next.getTime()<vehicle.getEndTime().getTime())
						{
							if((next.getTime()+lDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(next); //diff
								gantt.setDuration(lDuration);
								gantt.setChargingPoint(station.get(1).get(stationPos).getStationId());
								Date nextNew = new Date(next.getTime()+lDuration*60*1000); //diff
								station.get(1).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
					}
					break;
				case 2:
					miles = vehicle.getMiles();
					eDuration = (int) (miles/2.2);
					lDuration = (int) (miles/0.4);
					//fast
					if(alreadyAssign == 0)
					{
						Min = df.parse("24:00");
						stationPos = 0;
						for(int i=0;i<station.get(3).size();i++)
						{
							Station checkStation = station.get(3).get(i);
							Date buf = checkStation.getNextAvaTime();
							if(buf.getTime()<Min.getTime())
							{
								Min = buf;
								stationPos = i;
							}
							
						}
						next = Min;
						if(next.getTime()<=vehicle.getStartTime().getTime())
						{
							if((vehicle.getStartTime().getTime()+eDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(vehicle.getStartTime());
								gantt.setDuration(eDuration);
								gantt.setChargingPoint(station.get(3).get(stationPos).getStationId());
								Date nextNew = new Date(vehicle.getStartTime().getTime()+eDuration*60*1000);
								station.get(3).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
						if(next.getTime()>vehicle.getStartTime().getTime() && 
								next.getTime()<vehicle.getEndTime().getTime())
						{
							if((next.getTime()+eDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(next); //diff
								gantt.setDuration(eDuration);
								gantt.setChargingPoint(station.get(3).get(stationPos).getStationId());
								Date nextNew = new Date(next.getTime()+eDuration*60*1000); //diff
								station.get(3).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
					}
					//last
					if(alreadyAssign == 0)
					{
						Min = df.parse("24:00");
						stationPos = 0;
						for(int i=0;i<station.get(1).size();i++)
						{
							Station checkStation = station.get(1).get(i);
							Date buf = checkStation.getNextAvaTime();
							if(buf.getTime()<Min.getTime())
							{
								Min = buf;
								stationPos = i;
							}
							
						}
						next = Min;
						if(next.getTime()<=vehicle.getStartTime().getTime())
						{
							if((vehicle.getStartTime().getTime()+lDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(vehicle.getStartTime());
								gantt.setDuration(lDuration);
								gantt.setChargingPoint(station.get(1).get(stationPos).getStationId());
								Date nextNew = new Date(vehicle.getStartTime().getTime()+lDuration*60*1000);
								station.get(1).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
						if(next.getTime()>vehicle.getStartTime().getTime() && 
								next.getTime()<vehicle.getEndTime().getTime())
						{
							if((next.getTime()+lDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(next); //diff
								gantt.setDuration(lDuration);
								gantt.setChargingPoint(station.get(1).get(stationPos).getStationId());
								Date nextNew = new Date(next.getTime()+lDuration*60*1000); //diff
								station.get(1).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
					}
					break;
				case 3:
					miles = vehicle.getMiles();
					eDuration = (int) (miles/2.67);
					int mDuration = (int) (miles/1.42);
					lDuration = (int) (miles/0.42);
					//fast
					if(alreadyAssign == 0)
					{
						Min = df.parse("24:00");
						stationPos = 0;
						for(int i=0;i<station.get(4).size();i++)
						{
							Station checkStation = station.get(4).get(i);
							Date buf = checkStation.getNextAvaTime();
							if(buf.getTime()<Min.getTime())
							{
								Min = buf;
								stationPos = i;
							}
							
						}
						next = Min;
						if(next.getTime()<=vehicle.getStartTime().getTime())
						{
							if((vehicle.getStartTime().getTime()+eDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(vehicle.getStartTime());
								gantt.setDuration(eDuration);
								gantt.setChargingPoint(station.get(4).get(stationPos).getStationId());
								Date nextNew = new Date(vehicle.getStartTime().getTime()+eDuration*60*1000);
								station.get(4).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
						if(next.getTime()>vehicle.getStartTime().getTime() && 
								next.getTime()<vehicle.getEndTime().getTime())
						{
							if((next.getTime()+eDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(next); //diff
								gantt.setDuration(eDuration);
								gantt.setChargingPoint(station.get(4).get(stationPos).getStationId());
								Date nextNew = new Date(next.getTime()+eDuration*60*1000); //diff
								station.get(4).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
					}
					//mid
					if(alreadyAssign == 0)
					{
						Min = df.parse("24:00");
						stationPos = 0;
						for(int i=0;i<station.get(2).size();i++)
						{
							Station checkStation = station.get(2).get(i);
							Date buf = checkStation.getNextAvaTime();
							if(buf.getTime()<Min.getTime())
							{
								Min = buf;
								stationPos = i;
							}
							
						}
						next = Min;
						if(next.getTime()<=vehicle.getStartTime().getTime())
						{
							if((vehicle.getStartTime().getTime()+mDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(vehicle.getStartTime());
								gantt.setDuration(mDuration);
								gantt.setChargingPoint(station.get(2).get(stationPos).getStationId());
								Date nextNew = new Date(vehicle.getStartTime().getTime()+mDuration*60*1000);
								station.get(2).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
						if(next.getTime()>vehicle.getStartTime().getTime() && 
								next.getTime()<vehicle.getEndTime().getTime())
						{
							if((next.getTime()+mDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(next); //diff
								gantt.setDuration(mDuration);
								gantt.setChargingPoint(station.get(2).get(stationPos).getStationId());
								Date nextNew = new Date(next.getTime()+mDuration*60*1000); //diff
								station.get(2).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
					}
					//last
					if(alreadyAssign == 0)
					{
						Min = df.parse("24:00");
						stationPos = 0;
						for(int i=0;i<station.get(1).size();i++)
						{
							Station checkStation = station.get(1).get(i);
							Date buf = checkStation.getNextAvaTime();
							if(buf.getTime()<Min.getTime())
							{
								Min = buf;
								stationPos = i;
							}
							
						}
						next = Min;
						if(next.getTime()<=vehicle.getStartTime().getTime())
						{
							if((vehicle.getStartTime().getTime()+lDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(vehicle.getStartTime());
								gantt.setDuration(lDuration);
								gantt.setChargingPoint(station.get(1).get(stationPos).getStationId());
								Date nextNew = new Date(vehicle.getStartTime().getTime()+lDuration*60*1000);
								station.get(1).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
						if(next.getTime()>vehicle.getStartTime().getTime() && 
								next.getTime()<vehicle.getEndTime().getTime())
						{
							if((next.getTime()+lDuration*60*1000)<=vehicle.getEndTime().getTime())
							{
								gantt.setEVType(vehicle.getType());
								gantt.setCustomerID(vehicle.getVehicleId());
								gantt.setStartTime(next); //diff
								gantt.setDuration(lDuration);
								gantt.setChargingPoint(station.get(1).get(stationPos).getStationId());
								Date nextNew = new Date(next.getTime()+lDuration*60*1000); //diff
								station.get(1).get(stationPos).setNextAvaTime(nextNew);
								ganttList.add(gantt);
								vehicleNum++;
								alreadyAssign = 1;
								break;
							}
						}
					}
					break;
			}
		}
		return ganttList;
	}
	
	public void scheduleProcess(String fileName) throws Exception{
		vehicle = new ArrayList<Vehicle>();
		station = new HashMap<Integer, List<Station>>();
		vehicleNum = 0;
		ArrayList<Gantt> output = new ArrayList<Gantt>();
		FileOperation excel = new FileOperation();
		ArrayList<Vehicle> vehicleList = excel.readVehicle(fileName);
		station = excel.readStation(fileName);
		vehicle = vehicleSort(vehicleList);
		output = greedy();
		GanttChart.createGantt(output,vehicleNum);
		excel.writeExcel(output,vehicleNum,fileName);
	}
}
