package com.athena.print.module.sys.service;



import org.junit.Test;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.print.entity.sys.PrintDevicestatus;
import com.toft.core3.container.annotation.Inject;


public class PrintdevicestatusServiceTest extends AbstractCompomentTests{

	@Inject
	private PrintDevicestatusService printDevicestatusService;
	
	@Test
	public void testAddPrintdevicestatus(){
		
	PrintDevicestatus bean = new PrintDevicestatus();
	printDevicestatusService.selectStatus(bean);
	
	}
}
