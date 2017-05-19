package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Manager;
import com.athena.ckx.module.xuqjs.service.ManagerService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 计划员分组
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class ManagerTest extends AbstractCompomentTests{
	
	@Inject
	private ManagerService service;

	@Test
	public void test(){
		
		String[] args = new String[1];
		args[0]= "delete from DEV_CKX_TEST.ckx_manager where usercenter = 'QQ' and zuh = '000'";
		DBUtilRemove.remove(args);
		
		ArrayList<Manager> insert = new ArrayList<Manager>();
		Manager manager1 = new Manager();
		manager1.setUsercenter("QQ");
		manager1.setZuh("000");
		manager1.setZhizlx("97W");
		manager1.setBiaos("1");
		insert.add(manager1);
		
		ArrayList<Manager> update = new ArrayList<Manager>();
		Manager manager2 = new Manager();
		manager2.setUsercenter("QQ");
		manager2.setZuh("000");
		manager2.setZhizlx("97X");
		manager2.setBiaos("1");
		update.add(manager2);
		
		logger.info("--------------test1 Manager----------------");
		logger.info(service.save(insert, update, update, "12345"));
		logger.info("--------------test1 Manager----------------");
		
	}
	
	
	@Test
	public void test2(){
		ArrayList<Manager> insert = new ArrayList<Manager>();
		logger.info("--------------test2 Manager----------------");
		logger.info(service.save(insert, insert, insert, "12345"));
		logger.info("--------------test2 Manager----------------");
	}
	
}
