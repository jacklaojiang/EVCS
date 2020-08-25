package view;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import model.FileOperation;
import model.Reservation;

public class Layout extends JFrame{
	
	static JTextArea text_logging = new JTextArea();
	StringBuilder content = new StringBuilder();
	
	/**
     * constructor of frame
     *
     * @param args  ignored.
     */
	public Layout(){
			JFrame frame = new JFrame("EV Charging System");
			frame.setBounds(0, 0, 40, 60);
			frame.setSize(400,300);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(null);
			frame.getLayeredPane().setLayout(null); 
			
			JLabel lbl_chooseFile = new JLabel("Choose File");
			lbl_chooseFile.setBounds(30, 30, 100, 30);
			frame.getContentPane().add(lbl_chooseFile);
			
			final JComboBox cbox_loadExcel = new JComboBox();
			cbox_loadExcel.setBounds(130, 30, 150, 30);
			frame.add(cbox_loadExcel);
			cbox_loadExcel.setEditable(false);
			//file path
			String [] fileName = getFileName("E:/workspace/EVCS/");
			for (String name:fileName) {
				cbox_loadExcel.addItem(name);
			}
			
			JButton button_start = new JButton("Start");
			button_start.setBounds(new Rectangle(100,100,100,30));
			frame.add(button_start);
			button_start.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					String excelName = cbox_loadExcel.getSelectedItem().toString();
					System.out.print(excelName);
					Reservation schedule = new Reservation();
					try {
						schedule.scheduleProcess(excelName);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace(); 
					}
				}
			});
			
			
			
			
			JButton button_show = new JButton("Show Excel");
			button_show.setBounds(new Rectangle(200,100,100,30));
			frame.add(button_show);
			button_show.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					String excelName = cbox_loadExcel.getSelectedItem().toString();
					FileOperation excel = new FileOperation();
					try {
						content = excel.readExcel(excelName);
						System.out.print(content);
//						
//						text_logging.setText(content+"\r\n");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
			//setvisible
			frame.setVisible(true);
	}
	
	/**
     * get file name by path
     *
     * @param args  ignored.
     */
	public static String [] getFileName(String path)
    {
        File file = new File(path);
        String [] fileName = file.list();
        return fileName;
    }
}