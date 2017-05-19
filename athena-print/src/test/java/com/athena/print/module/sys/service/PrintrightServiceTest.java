package com.athena.print.module.sys.service;

import java.util.ArrayList;

import org.junit.Test;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.print.entity.sys.Printright;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Inject;
public class PrintrightServiceTest extends AbstractCompomentTests{

	@Inject
	private PrintrightService printrightService;
	
	
	
	@Test
	public void testAddPrintright(){
		
		Printright printright = new Printright();
		Printright printright1 = new Printright();
		
		//主动在权限表中操作
		//插入表  
		//第一条数据
		printright.setUsercenter("UW");
		printright.setUserscode("444444");
		printright.setScodes("BBB");
		printright.setStoragescode("C004");
		printright.setSpcodes("1B1B1B");
		printright.setCreator("李四");
		//printright.setCreate_time(DateUtil.curDateTime());
		//第二条数据
		printright1.setUsercenter("UW");
		printright1.setUserscode("555555");
		printright1.setScodes("DDD");
		printright1.setStoragescode("C005");
		printright1.setSpcodes("1C1C1C");
		printright1.setCreator("张三");
		//printright1.setCreate_time(DateUtil.curDateTime());
		
		ArrayList<Printright> inserts = new ArrayList<Printright>();
		inserts.add(printright);
		inserts.add(printright1);
		ArrayList<Printright> insertss = new ArrayList<Printright>();
		printrightService.saves(printright,inserts, insertss, insertss,"李四");
		
		//更新  表
		ArrayList<Printright> updates = new ArrayList<Printright>();
		ArrayList<Printright> updatess = new ArrayList<Printright>();
		printright.setUserscode(printright.getUserscode());
		printright.setScodes(printright.getScodes());
		printright.setStoragescode(printright.getStoragescode());
		printright.setSpcodes("2222");
	
		printright1.setUserscode(printright1.getUserscode());
		printright1.setScodes(printright1.getScodes());
		printright1.setStoragescode(printright1.getStoragescode());
		printright.setSpcodes("111111");
		
		updates.add(printright);
		updates.add(printright1);
		printrightService.saves(printright,updatess, updates, updatess,"张三");
		
		//删除表  要3个主键 才能确定删除一行数据
		
		ArrayList<Printright> deletes = new ArrayList<Printright>();
		ArrayList<Printright> deletess = new ArrayList<Printright>();
		printright.setUserscode(printright.getUserscode());
		printright.setScodes(printright.getScodes());
		printright.setStoragescode(printright.getStoragescode());
		deletes.add(printright);
		printright1.setUserscode(printright1.getUserscode());
		printright1.setScodes(printright1.getScodes());
		printright1.setStoragescode(printright1.getStoragescode());
		deletes.add(printright1);
		printrightService.saves(printright,deletess, deletess, deletes,"张三");
		
		
		//触发进行操作 即已知 用户组编号  来给该用户组 设置对应的  单据类型组、仓库编号、打印机组
		String userscode ="333333";
		Printright printright2 = new Printright();
		//插入表  
		//插入一条数据
		printright2.setUsercenter("UW");
		printright2.setScodes("EEE");
		printright2.setStoragescode("C006");
		printright2.setSpcodes("1G1G1G");
		printright2.setCreator("赵六");
		printright2.setCreate_time(DateUtil.curDateTime());
		ArrayList<Printright> insert = new ArrayList<Printright>();
		insert.add(printright2);
		printrightService.save(printright2.getUsercenter(),userscode, insert, null, null,"张三");
		
		//更新一条数据
		ArrayList<Printright> update = new ArrayList<Printright>();
		printright2.setSpcodes("1H1H1H");
		printright2.setEdit_time(DateUtil.curDateTime());
		update.add(printright2);
		printrightService.save(printright2.getUsercenter(),userscode, null, update, null,"张三");
		
		//删除一条数据
		ArrayList<Printright>delete = new ArrayList<Printright>();
		printright2.setUsercenter(printright2.getUsercenter());
		printright2.setUserscode(userscode);
		printright2.setScodes(printright2.getScodes());
		printright2.setStoragescode(printright2.getStoragescode());
		delete.add(printright2);
		printrightService.save(printright2.getUsercenter(),userscode, null, null, delete,"张三");
	}
}
