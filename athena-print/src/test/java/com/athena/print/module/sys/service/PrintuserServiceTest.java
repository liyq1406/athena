package com.athena.print.module.sys.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.print.DBUtilRemove;
import com.athena.print.entity.sys.Printuser;
import com.athena.print.entity.sys.Printuserinfo;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Inject;


public class PrintuserServiceTest extends AbstractCompomentTests{

	@Inject
	private PrintuserService printuserService;
	
	@Inject
	private PrintuserinfoService printuserinfoService;
	
	
	@Test
	public void testAddPrintuser(){
		
		Printuser printuser = new Printuser();
		
		//插入主表   和   子表
		printuser.setUsercenter("UW");
		printuser.setUserscode("666666");
		printuser.setUsers("E组");
		printuser.setBiaos("1");
		printuser.setCreator("");
		printuser.setCreate_time(DateUtil.curDateTime());
		
		ArrayList<Printuserinfo> insert = new ArrayList<Printuserinfo>();
		Printuserinfo printuserinfo = new Printuserinfo();
		printuserinfo.setUsercenter("UW");
		printuserinfo.setUsercode("FFFFFF");
		printuserinfo.setUserscode(printuser.getUserscode());
		printuserinfo.setSname("韩寒");
		printuserinfo.setUsertype("1");
		printuserinfo.setCreator("张三");
		printuserinfo.setCreate_time(DateUtil.curDateTime());
		insert.add(printuserinfo);
		
		printuserService.doAdd(printuser, insert,"张三");
		
		//更新主   子表
		ArrayList<Printuserinfo> update = new ArrayList<Printuserinfo>();
		printuser.setUsers("E1E组");
		printuserinfo.setUserscode(printuser.getUserscode());
		printuserinfo.setSname("寒韩");
		printuserinfo.setUsertype("0");
		printuserinfo.setEdit_time(DateUtil.curDateTime());
		update.add(printuserinfo);
		printuserService.doSave(printuser, null, update, null,"张三");
		
		
		//根据打印机组编号来查询 打印机集合
		List<Printuserinfo> pList = printuserinfoService.list(printuserinfo.getUsercode());
		logger.info("*********************"+pList.size());
		
		//查询用户明细
		Printuserinfo pf = printuserinfoService.selectUsersByuser(printuserinfo.getUsercode());
		logger.info("*********************"+pf);
		logger.info("*********************"+pf.getSname());
		
		
		//删除子表一条数据 
		ArrayList<Printuserinfo> delete = new ArrayList<Printuserinfo>();
		printuserinfo.setUserscode(printuser.getUserscode());
		delete.add(printuserinfo);
		printuserService.doSave(printuser, null, null, delete,"张三");
		
		
		//逻辑删除主表 更改为无效
		printuserService.doDelete(printuser);
		
		//在把主表 更改为有效
		printuserService.doUpdate1(printuser);
		
		
		//删除主表
		String[] args = new String[1];
		args[0] = "delete from ckx_print_users  where  userscode= '666666' and usercenter='UW'";
		DBUtilRemove.remove(args);
		
	}
}
