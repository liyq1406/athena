package com.athena.component;

import java.util.Date;
import java.util.Timer;

import com.athena.print.controller.GetQtaskTimeController;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
@Component
public class TotalTimeStart {

	//@Inject
	//private PrintQtaskTimeController printQtaskTimeController;
	
	//@Inject
	//private PrintMonitorTimeController printMonitorTimeController;
	
	@Inject
	private GetQtaskTimeController getQtaskTimeController;
	
	public void execute(){
		Timer timer = new Timer();
		
		Date date = new Date();
		
		
		//timer.schedule(printQtaskTimeController, date, 10000);
		
		
		//timer.schedule(printMonitorTimeController, date, 2000);
		
		//120000
		//timer.schedule(getQtaskTimeController, date, 10000);
	}
}