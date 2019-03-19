package view;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import model.utilities.StringAnalyzer;

public class ExcellView {
	public String loadExcel() {
		
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
		
		if(jf.showOpenDialog(null) == jf.APPROVE_OPTION) {
			if(jf.getName().contains("xlsx" )) 
			{
				mapName = path.getName().substring(0, path.getName().length() - 4);
				return path.getAbsolutePath();
			}
			File path = jf.getSelectedFile();
			}
		else  
			return loadExcel();
		
		return null;
	}
	public void display() {
		
	}
}
