/**
 * 
 */
package com.athena.print.core;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.print.controller.PrintQtaskTimeController;
import com.athena.print.controller.PrintQtaskController;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
public class PrintTaskExecutorTests  extends AbstractCompomentTests {
	
	@Inject
	PrintQtaskController totalController;
	
	@Inject
	PrintQtaskTimeController timeController;
	
	@Inject
	protected AbstractIBatisDao baseDao;
	
	@Test
	public void testTaskStart(){
		//PrinterTaskExecutor printerTaskExecutor  = new PrinterTaskExecutor();
//		printTaskExecutor.setPrintDeviceService(printDeviceService);
//		printTaskExecutor.setPrintQtaskService(printQtaskService);
//		printTaskExecutor.setPrintPostuserService(printPostuserService);
//		printTaskExecutor.setPrintPostdeviceService(printPostdeviceService);
		//printTaskExecutor.start();//启动打印
//		printerTaskExecutor.getDeviceGroup();
//		printerTaskExecutor.getUsers();
//		printerTaskExecutor.getPrintTask();
		//totalController.startController(baseDao);
		timeController.run();
		
	}
}
