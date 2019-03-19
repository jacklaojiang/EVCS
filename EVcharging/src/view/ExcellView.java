package view;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcellView {
	public StringBuilder loadExcel() throws Exception {
		
		System.out.println("Select the map file (.map)...");
		JFileChooser jf = new JFileChooser() 
		{
			
			protected JDialog createDialog(Component parent) throws HeadlessException 
			{
				JDialog jDialog = super.createDialog(parent);
				jDialog.setAlwaysOnTop(true);
				return jDialog;		
			}
		};
		
		
		/*To open in specific directory*/
		File workingDirectory = new File("E:\\work\\project");
		jf.setCurrentDirectory(workingDirectory);
		String path="";
		String fileName="";
		if(jf.showOpenDialog(null) == jf.APPROVE_OPTION) {
			File file = jf.getSelectedFile();
			if(file.getName().contains(".xlsx" )) 
			{
				fileName = file.getName();
				path = file.getAbsolutePath();
			}
			else  loadExcel();
			
		
		
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(path)));
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
                        				content.append("          " + result + "          ");
                        			}
                        			else{
	                        			Double d = row.getCell(k).getNumericCellValue();
	                        			DecimalFormat df = new DecimalFormat("#");
	                        			result = df.format(d);
	                        			//System.out.print(result+ " ");
	                        			content.append("          " + result + "          ");
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
        
		else return null;
		
	}
	
	public int functionSelect() {
		System.out.println("please select one of the following function: "
				+ "\n1. show excel info"+"\n2.show gantt chart"+"\n3.exit");
		Scanner kb = new Scanner(System.in);
		int func = kb.nextInt();
		if(func<1 || func>3) return functionSelect();
		return func;
	}
	public void display(String path) {
		System.out.print(path);
	}
}
