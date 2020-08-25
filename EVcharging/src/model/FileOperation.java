package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileOperation {
	
	Vehicle vehicle;
	Station station;
	ArrayList<Vehicle> vehicleList;
	Map<Integer, List<Station>> stationMap;
	
	/**
     * read excel data
     *
     * @param args  ignored.
     */
    @SuppressWarnings("deprecation")
	public StringBuilder readExcel(String fileName) throws Exception {
    	//file path
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("E:/workspace/EVCS/"+fileName)));
        XSSFSheet sheet = null;
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {// get each sheet
            sheet = workbook.getSheetAt(i);
            for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {// get last row number
                XSSFRow row = sheet.getRow(j);
                if (row != null) {
                    for (int k = 0; k < row.getLastCellNum(); k++) {// get last cell number
                    	String result = null;
                        if (row.getCell(k) != null) { // get cell value
                        	switch(row.getCell(k).getCellType())
                        	{
                        		case XSSFCell.CELL_TYPE_NUMERIC:
                        			short format = row.getCell(k).getCellStyle().getDataFormat();
                        			SimpleDateFormat sdf = null;
                        			if(format == 20 || format == 164 || format == 176)
                        			{
                        				sdf = new SimpleDateFormat("HH:mm");
                        				double value = row.getCell(k).getNumericCellValue();
                        				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                        				result = sdf.format(date);
                        				//System.out.print(result+" ");
                        				content.append("       " + result + "       ");
                        			}
                        			else{
	                        			Double d = row.getCell(k).getNumericCellValue();
	                        			DecimalFormat df = new DecimalFormat("#");
	                        			result = df.format(d);
	                        			//System.out.print(result+ " ");
	                        			content.append("        " + result + "        ");
                        			}
                        			break;
                        		case XSSFCell.CELL_TYPE_STRING:
                        			result = row.getCell(k).toString();
                        			//System.out.print(result+ " ");
                        			content.append("  "+result+"  ");
                        			break;
                        		case XSSFCell.CELL_TYPE_BLANK:  
                                    break;
                                default:
                                	break;
                        	}
                        } else {
                            //System.out.print(" ");
                        	content.append(" ");
                        }
                    }
                }
                //System.out.println(""); // read last line
                content.append("\r\n");
            }
            //System.out.println("read sheet" + fileName + " " + workbook.getSheetName(i) + " finished");
            content.append("read sheet " + fileName + " " + workbook.getSheetName(i) + " finished\r\n");
        }
        return content;
    }
    
    /**
     * read vehicle data and compress in object
     *
     * @param args  ignored.
     */
    @SuppressWarnings("deprecation")
	public ArrayList<Vehicle> readVehicle(String fileName) throws Exception {
    	//file path
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("E:/workspace/EVCS/"+fileName)));
        XSSFSheet sheet = null;
        sheet = workbook.getSheetAt(0);
        vehicleList = new ArrayList<Vehicle>();
        for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
            XSSFRow row = sheet.getRow(j);
            vehicle = new Vehicle();
            if (row != null) {
                for (int k = 0; k < row.getLastCellNum(); k++) {
                	String result = null;
                    if (row.getCell(k) != null) { 
                    	switch(row.getCell(k).getCellType())
                    	{
                    		case XSSFCell.CELL_TYPE_NUMERIC:
                    			short format = row.getCell(k).getCellStyle().getDataFormat();
                    			SimpleDateFormat sdf = null;
                    			//find Date format and translate in string
                    			if(format == 20 || format == 164 || format == 176)
                    			{
                    				sdf = new SimpleDateFormat("HH:mm");
                    				double value = row.getCell(k).getNumericCellValue();
                    				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    				result = sdf.format(date);
                    			}
                    			else{
                        			Double d = row.getCell(k).getNumericCellValue();
                        			DecimalFormat df = new DecimalFormat("#");
                        			result = df.format(d);
                    			}
                    			break;
                    		case XSSFCell.CELL_TYPE_STRING:
                    			result = row.getCell(k).toString();
                    			break;
                    		case XSSFCell.CELL_TYPE_BLANK:  
                                break;
                            default:
                            	break;
                    	}
                    } else {
                    	
                    }
                    
                	if(j != 0)
                	{
                    	switch(k)
                    	{
                    		case 0:
                    			vehicle.setVehicleId(Integer.parseInt(result));
                    			break;
                    		case 1:
                    			DateFormat df = new SimpleDateFormat("HH:mm");
                    			vehicle.setStartTime(df.parse(result));
                    			break;
                    		case 2:
                    			DateFormat df2 = new SimpleDateFormat("HH:mm");
                    			vehicle.setEndTime(df2.parse(result));
                    			break;
                    		case 3:
                    			vehicle.setMiles(Integer.parseInt(result));
                    			break;
                    		case 4:
                    			vehicle.setType(Integer.parseInt(result));
                    			break;
                    	}
                	}
                                         
                }
            }
            if(j!=0)
            {
            	vehicleList.add(vehicle);
            }
        }  
        return vehicleList;
    }
    
    /**
     * read charging point data and compress in object
     *
     * @param args  ignored.
     */
    @SuppressWarnings("deprecation")
	public Map<Integer, List<Station>> readStation(String fileName) throws Exception {
    	//file path
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("E:/workspace/EVCS/"+fileName)));
        XSSFSheet sheet = null;
        sheet = workbook.getSheetAt(1);
        stationMap = new HashMap<Integer, List<Station>>();
        for(int i=1;i<5;i++){
			List<Station> hashList = new ArrayList<Station>();
			stationMap.put(i, hashList);
		}
        for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
            XSSFRow row = sheet.getRow(j);
            station = new Station();
            List<Station> stationList = new ArrayList<Station>();
            if (row != null) {
                for (int k = 0; k < row.getLastCellNum(); k++) {
                	String result = null;
                    if (row.getCell(k) != null) { 
                    	switch(row.getCell(k).getCellType())
                    	{
                    		case XSSFCell.CELL_TYPE_NUMERIC:
                    			short format = row.getCell(k).getCellStyle().getDataFormat();
                    			SimpleDateFormat sdf = null;
                    			if(format == 20)
                    			{
                    				sdf = new SimpleDateFormat("HH:mm");
                    				double value = row.getCell(k).getNumericCellValue();
                    				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    				result = sdf.format(date);
                    			}
                    			else{
                        			Double d = row.getCell(k).getNumericCellValue();
                        			DecimalFormat df = new DecimalFormat("#");
                        			result = df.format(d);
                    			}
                    			break;
                    		case XSSFCell.CELL_TYPE_STRING:
                    			result = row.getCell(k).toString();
                    			break;
                    		case XSSFCell.CELL_TYPE_BLANK:  
                                break;
                            default:
                            	break;
                    	}
                    } else {
                    	
                    }
                   
                	if( j!= 0)
                	{
                    	switch(k)
                    	{
                    		case 0:
                    			station.setStationId(Integer.parseInt(result));
                    			break;
                    		case 1:
                    			station.setType(Integer.parseInt(result));
                    			break;
                    	}
                	}
                                
                }
            }
            if(j!=0)
            {
	            stationList = stationMap.get(station.getType());
	            stationList.add(station);
	            stationMap.put(station.getType(), stationList);
            }
        }  
        return stationMap;
    }
    
    /**
     * wirte excel in output file
     *
     * @param args  ignored.
     */
    public void writeExcel(ArrayList<Gantt> ganttList, int EVNum, String fileName) throws IOException
    {
    	//get exist excel and create workbook
    	FileInputStream fileInput = new FileInputStream(new File("E:/workspace/EVCS/"+fileName));//file path
    	XSSFWorkbook workbook = new XSSFWorkbook(fileInput);
    	workbook.createSheet("Output");
    	XSSFSheet sheet = workbook.getSheetAt(2);
    	//set cell style for Date
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    	XSSFCellStyle dateCellStyle=workbook.createCellStyle(); 
    	short df=workbook.createDataFormat().getFormat("HH:mm"); 
    	dateCellStyle.setDataFormat(df);
    	//create title for each gantt object
    	XSSFRow	title = sheet.createRow(0);
    	title.createCell(0).setCellValue("Customer ID");
		title.createCell(1).setCellValue("EV Type");
		title.createCell(2).setCellValue("Start Charging Time");
		title.createCell(3).setCellValue("Charging Duration");
		title.createCell(4).setCellValue("Assigned Charging Point");
    	//create row for each gantt object
    	for(int i = 1; i < ganttList.size()+2; i++)
    	{
    		XSSFRow	row = sheet.createRow(i);
    		row.createCell(0).setCellValue("");
    		row.createCell(1).setCellValue("");
    		row.createCell(2).setCellValue("");
    		row.createCell(3).setCellValue("");
    		row.createCell(4).setCellValue("");
    		if(i==ganttList.size()+1)
    		{
    			row.createCell(0).setCellValue("The number of EVs:"+EVNum);
    		}
    	}
    	//set value to each cell
    	for (int i = 1; i < ganttList.size()+1; i++) {
    		XSSFRow row = sheet.getRow(i);
    		for (int j = 0; j < 5; j++)
    		{
    			switch(j)
    			{
    				case 0:
    		
    					row.getCell(0).setCellValue(ganttList.get(i-1).getCustomerID());
    					break;
    				case 1:
    					row.getCell(1).setCellValue(ganttList.get(i-1).getEVType());
    					break;
    				case 2:
    					row.getCell(2).setCellStyle(dateCellStyle);
    					row.getCell(2).setCellValue(ganttList.get(i-1).getStartTime());
    					break;
    				case 3:
    					row.getCell(3).setCellValue(ganttList.get(i-1).getDuration()+"Min");
    					break;
    				case 4:
    					row.getCell(4).setCellValue(ganttList.get(i-1).getChargingPoint());
    					break;
    			}
    		}
          }
    	//output file stream
    	FileOutputStream os = new FileOutputStream(new File("E:/workspace/EVCS/"+fileName));//file path
    	os.flush();
        workbook.write(os);
        workbook.close();
        fileInput.close();
        os.close();
    }
	
}
