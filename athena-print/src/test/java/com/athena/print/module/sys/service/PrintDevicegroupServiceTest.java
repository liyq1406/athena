/**
 * 
 */
package com.athena.print.module.sys.service;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.print.DBUtilRemove;
import com.athena.print.entity.sys.PrintDevice;
import com.athena.print.entity.sys.PrintDevicegroup;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Inject;
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
public class PrintDevicegroupServiceTest  extends AbstractCompomentTests {
	
	static Logger logger=Logger.getLogger(PrintDevicegroupServiceTest.class);
	
	@Inject
	private PrintDevicegroupService printDevicegroupService;
	
	@Inject
	private PrintDeviceService printDeviceService;
	
	
	@Test
	public void testAddPrintDevicegroup() throws Exception{
		
		PrintDevicegroup printDevicegroup = new PrintDevicegroup();
		
		//插入主表   和   子表
		printDevicegroup.setUsercenter("UW");
		printDevicegroup.setSpcodes("AAAAAA");
		printDevicegroup.setSname("仓库1组");
		printDevicegroup.setSdesc("仓库1组打印机组");
		printDevicegroup.setNflag("0");
		printDevicegroup.setBiaos("1");
		printDevicegroup.setCreator("");
		printDevicegroup.setCreate_time(DateUtil.curDateTime());
		
		ArrayList<PrintDevice> insert = new ArrayList<PrintDevice>();
		PrintDevice printDevice = new PrintDevice();
		printDevice.setUsercenter("UW");
		printDevice.setSpcode("CK1111");
		printDevice.setSpcodes(printDevicegroup.getSpcodes());
		printDevice.setSname("1a1a1a");
		printDevice.setSip("10.26.171.86");
		printDevice.setSport("9001");
		printDevice.setSdesc("仓库1组下的一台打印机");
		//printDevice.setCreator("张三");
		printDevice.setCreate_time(DateUtil.curDateTime());
		insert.add(printDevice);
		printDevicegroupService.doAdd(printDevicegroup, insert,"张三");
		
		
		//更新主   子表
		ArrayList<PrintDevice> update = new ArrayList<PrintDevice>();
		printDevicegroup.setSname("仓库壹组");
		printDevicegroup.setSdesc("仓库壹组打印机组");
		printDevice.setUsercenter("UW");
		printDevice.setSpcodes(printDevicegroup.getSpcodes());
		printDevice.setSname("1b1b1b");
		printDevice.setSport("9100");
		printDevice.setEdit_time(DateUtil.curDateTime());
		update.add(printDevice);
		printDevicegroupService.doSave(printDevicegroup, null, update, null,"李四");
		
		//根据打印机组编号来查询 打印机集合
		List<PrintDevice> pdList = printDeviceService.list(printDevicegroup.getSpcodes());
		logger.info("*********************"+pdList.size());
		
		
		//删除子表一条数据 
		ArrayList<PrintDevice> delete = new ArrayList<PrintDevice>();
		printDevice.setSpcodes(printDevicegroup.getSpcodes());
		delete.add(printDevice);
		printDevicegroupService.doSave(printDevicegroup, null, null, delete,"李四");
		
		//逻辑删除主表 更改为无效
		printDevicegroupService.doDelete(printDevicegroup);
		
		//在把主表 更改为有效
		printDevicegroupService.doUpdate1(printDevicegroup);
		
		
		//删除主表
		String[] args = new String[1];
		args[0] = "delete from ckx_print_devicegroup where  spcodes= 'AAAAAA' and usercenter='UW'";
		DBUtilRemove.remove(args);
		
		//删除的异常
		String[] yc= new String[1];
		yc[0] = "";
		DBUtilRemove.remove(yc);
		

	}
	
	
}
