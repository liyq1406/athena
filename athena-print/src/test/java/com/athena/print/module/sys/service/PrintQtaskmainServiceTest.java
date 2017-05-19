package com.athena.print.module.sys.service;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.print.DBUtilRemove;
import com.athena.print.core.AddTaskTool;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.entity.pcenter.PrintQtaskmain;
import com.athena.print.entity.sys.PrintDevice;
import com.athena.print.entity.sys.PrintDevicegroup;
import com.athena.print.entity.sys.PrintDevicestatus;
import com.athena.print.module.pcenter.service.PrintQtaskinfoService;
import com.athena.print.module.pcenter.service.PrintQtaskmainService;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


public class PrintQtaskmainServiceTest extends AbstractCompomentTests{

	@Inject
	private PrintQtaskmainService printQtaskmainService;
	
	@Inject
	private PrintQtaskinfoService printQtaskinfoService;
	
	@Inject
	private PrintDeviceService printDeviceService;
	
	@Inject
	private PrintDevicegroupService printDevicegroupService;
	
	@Inject 
	protected AbstractIBatisDao baseDao;
	
	@Inject
	private AddTaskTool addTaskTool;
	
	@Test
	public void testAddPrintQtaskmain()throws Exception{
		
		//打印队列主表
		PrintQtaskmain bean = new PrintQtaskmain();
		bean.setUsercenter("UW");
		bean.setQid("cf3351d4-370c-4fd9-92f7-9ead100s");
		bean.setSaccount("user05");
		bean.setPgid("1C1C1C");
		bean.setScode("s01");
		bean.setStoragecode("001");
		bean.setSdevicecode("AAABBB");
		bean.setCreatetime(DateUtil.curDateTime());
		bean.setStatus("1");
		
		//打印队列子表
		PrintQtaskinfo printQtaskinfo = new PrintQtaskinfo();
		printQtaskinfo.setUsercenter("UW");
		printQtaskinfo.setQid("cf3351d4-370c-4fd9-92f7-9ead100s");
		
		//创建一个TXT路径
		String paths;
		String filepath = "C:";
		String path = "/123456.txt";
		if(System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
			paths=filepath+path;
		}else{
			paths=path;
		}
		File filename = new File(paths);
	       if (!filename.exists()) {
	          filename.createNewFile();
	       }
		printQtaskinfo.setSfilename(paths);
		printQtaskinfo.setSeq(1+"");
		printQtaskinfo.setStatus(0);
		
		
		// 插入主表
		printQtaskmainService.doInsert(bean);
		
		// 插入子表
		printQtaskinfoService.doInsert(printQtaskinfo);
		
		//插入一条打印设备的数据 （打印设备组以及打印设备）
		PrintDevicegroup printDevicegroup = new PrintDevicegroup();
		printDevicegroup.setUsercenter("UW");
		printDevicegroup.setSpcodes("123456");
		printDevicegroupService.doInsert(printDevicegroup,"张三");
		List<PrintDevice> insert = new ArrayList<PrintDevice>();
		PrintDevice printDevice = new PrintDevice();
		printDevice.setUsercenter("UW");
		printDevice.setSpcode("AAABBB");
		printDevice.setSip("10.26.171.86");
		printDevice.setSname("仓库入口1号打印机");
		insert.add(printDevice);
		printDeviceService.doInsert(printDevicegroup, insert,"李四");
		
		//打印中断的 作业 取消打印
		//printQtaskmainService.doDelete(bean);
		
		// 根据主作业QID 来更新 打印机编号
		bean.setSdevicecode("AAABBB");
		printQtaskmainService.doUpdate(bean);
		
		//根据打印机编号来更新 打印机状态
		printQtaskmainService.updateByspcode(bean.getSdevicecode());
		
		
		//根据主作业QID 查询    下面的子作业
		List<PrintQtaskinfo>  pf=printQtaskmainService.selectQtaskinfo(bean.getQid());
		logger.info("*********************"+pf.size());
		
		for (int i = 0; i < pf.size(); i++) {
			//根据打印机编号  更新子任务作业状态为 已执行
			printQtaskmainService.updateQtaskinfo(pf.get(0));
		}
		
		//根据打印机编号      更新 辅状态为  繁忙
		printQtaskmainService.updateStatus(bean.getSdevicecode());
		
		//打印机状态表更新主作业任务号、用户编号
		PrintDevicestatus devicestatus = new PrintDevicestatus();
		devicestatus.setSpcode(bean.getSdevicecode());
		devicestatus.setLastqid(bean.getQid());
		devicestatus.setUsercode(bean.getSaccount());
		printQtaskmainService.updateMain(devicestatus);
		
		//根据主作业QID   更新主作业状态为 打印中
		printQtaskmainService.updateQtaskmain(bean.getQid());
		
		//主作业 下未获取到子作业 则释放打印机
		printQtaskmainService.updateQtaskmain1(bean.getSdevicecode());
		
		//修改主作业的状态为 打印中断 
		printQtaskmainService.updateQtaskmain2(bean.getQid());
		
		//根据打印机编号 查询 打印机实体
		PrintDevice pd = printQtaskmainService.selectDevice(bean.getSdevicecode());
		logger.info("*********************"+pd);
		logger.info("*********************"+pd.getSname());
		
		//根据主作业QID 查询    作业实体
		PrintQtaskmain printQtaskmain = printQtaskmainService.selectQtask(bean.getQid(),"UW");
		logger.info("*********************"+printQtaskmain);
		logger.info("*********************"+printQtaskmain.getStoragecode());
		
		
		
		//先删除子表    在删除主表 物理删除
		String[] args = new String[2];
		args[0] = "delete from ckx_print_qtask_info where  qid= 'cf3351d4-370c-4fd9-92f7-9ead100s' and seq=1 and usercenter='UW'" ;
		args[1] = "delete from ckx_print_qtask_main where  qid= 'cf3351d4-370c-4fd9-92f7-9ead100s' and usercenter='UW'" ;
		DBUtilRemove.remove(args);
		
		//先删除设备子表    在删除设备组主表 物理删除
		String[] args1 = new String[2];
		args1[0] = "delete from ckx_print_device where  spcode= 'AAABBB' and usercenter='UW'" ;
		args1[1] = "delete from ckx_print_devicegroup where  spcodes= '123456' and usercenter='UW'" ;
		DBUtilRemove.remove(args1);
		
		
		
		//异常情况
		try{
			addTaskTool.getUserscode("EEEEEE");
		}catch(Exception e){
			logger.info("-------------------------"+e.getMessage());
		}
		
		//设置用户组
		String userscode ="444444";
		//设置单据组
		String scodes ="EEE";
		//设置仓库编号
		String storagescode ="C010";
		
		//异常情况
		try{
			addTaskTool.getSpcodes(userscode, scodes, storagescode);
		}catch(Exception e){
			logger.info("-------------------------"+e.getMessage());
		}

		
	}
}
