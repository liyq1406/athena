package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Shengcpt;
import com.athena.ckx.module.xuqjs.service.ShengcptService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 生产平台
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class ShengcptTest extends AbstractCompomentTests{
	
	@Inject
	private ShengcptService service;
	
	//日志
	private static final Logger logger = Logger.getLogger(ShengcptTest.class);

	@Test
	public void test1(){
		
		ArrayList<Shengcpt> insert = new ArrayList<Shengcpt>();
		Shengcpt shengcpt1 = new Shengcpt();
		shengcpt1.setUsercenter("QQ");
		shengcpt1.setShengcxbh("ZZZZZ");
		shengcpt1.setShengcptbh("ZZZZZZ");
		shengcpt1.setShengcjp("3");
		shengcpt1.setWeilscjp("3");
		shengcpt1.setBiaos("1");
		shengcpt1.setQiehsj(DateTimeUtil.getCurrDate());
		insert.add(shengcpt1);
		
		ArrayList<Shengcpt> update = new ArrayList<Shengcpt>();
		Shengcpt shengcpt2 = new Shengcpt();
		shengcpt2.setUsercenter("QQ");
		shengcpt2.setShengcxbh("ZZZZZ");
		shengcpt2.setShengcptbh("ZZZZZZ");
		shengcpt2.setShengcjp("2");
		shengcpt2.setWeilscjp("2");
		shengcpt2.setQiehsj(DateTimeUtil.getCurrDate());
		update.add(shengcpt2);
		
		logger.info("--------------test1 Shengcpt----------------");
		logger.info(service.save(insert, update, update, "12345"));
		logger.info("--------------test1 Shengcpt----------------");
		
		String[] args = new String[1];
		args[0]= "delete from DEV_CKX_TEST.ckx_shengcpt where usercenter = 'QQ' and shengcxbh = 'ZZZZZ' and shengcptbh= 'ZZZZZZ'";
		DBUtilRemove.remove(args);
	}
	
	
	@Test
	public void test2(){
		ArrayList<Shengcpt> insert = new ArrayList<Shengcpt>();
		logger.info("--------------test2 Shengcpt----------------");
		logger.info(service.save(insert, insert, insert, "12345"));
		logger.info("--------------test2 Shengcpt----------------");
	}
}
