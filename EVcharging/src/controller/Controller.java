package controller;

import view.ExcellView;

public class Controller {
	public static void main(String[] args) throws Exception {
		StringBuilder content = new StringBuilder();
		ExcellView ev = new ExcellView();
		content = ev.loadExcel();
		int func = ev.functionSelect();
		switch(func) {
		case 1:;
		case 2:;
		case 3:break;
		}
		//ev.display(path);
	}
	
	

}
