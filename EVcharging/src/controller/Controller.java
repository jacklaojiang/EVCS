package controller;

import view.ExcellView;

public class Controller {
	public static void main(String[] args) throws Exception {
		StringBuilder content = new StringBuilder();
		ExcellView ev = new ExcellView();
		try {
			content = ev.loadExcel();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		int func = ev.functionSelect();
		switch(func) {
		case 1:ev.displayExcel(content);
		case 2:;
		case 3:break;
		}
		//ev.display(path);
	}

	
	

}
