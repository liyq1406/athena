package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.module.xuqjs.service.CkxGongyxhdService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 工艺消耗点
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class CkxGongyxhdTest extends AbstractCompomentTests{
	
	@Inject
	private CkxGongyxhdService service;
	
	//日志
	private static final Logger logger = Logger.getLogger(CkxGongyxhdTest.class);

	@Test
	public void test(){
		ArrayList<CkxGongyxhd> insert = new ArrayList<CkxGongyxhd>();
		CkxGongyxhd gongyxhd1 = new CkxGongyxhd();
		gongyxhd1.setGongyxhd("000000000");
		gongyxhd1.setLiush("0000");
		gongyxhd1.setGongybs("1");
		gongyxhd1.setChessl(123);
		gongyxhd1.setPianysj(20);
		gongyxhd1.setBiaos("1");
		insert.add(gongyxhd1);
		
		ArrayList<CkxGongyxhd> update = new ArrayList<CkxGongyxhd>();
		CkxGongyxhd gongyxhd2 = new CkxGongyxhd();
		gongyxhd2.setGongyxhd("000000000");
		gongyxhd2.setLiush("0001");
		gongyxhd2.setGongybs("11");
		gongyxhd2.setChessl(121);
		gongyxhd2.setPianysj(21);
		gongyxhd1.setGongybs("0");
		update.add(gongyxhd2);
		
		logger.info("--------------test1 Gongyxhd----------------");
		//logger.info(service.save(insert, update, update, "12345","12345"));
		logger.info("--------------test1 Gongyxhd----------------");
		
		String[] args = new String[1];
		args[0]= "delete from DEV_CKX_TEST.ckx_gongyxhd where gongyxhd = '000000000'";
		DBUtilRemove.remove(args);
	}
	
	
	@Test
	public void test2(){
		ArrayList<CkxGongyxhd> insert = new ArrayList<CkxGongyxhd>();
		logger.info("--------------test2 Gongyxhd----------------");
	//	logger.info(service.save(insert, insert, insert, "12345","12345"));
		logger.info("--------------test2 Gongyxhd----------------");
	}
	
	@Test
	public void test3(){
		
		CkxGongyxhd gongyxhd = new CkxGongyxhd();
		gongyxhd.setGongyxhd("000000000");
		gongyxhd.setEditor("123");
		gongyxhd.setEdit_time(DateTimeUtil.getCurrDateTime());
		
		logger.info("--------------test3 Gongyxhd----------------");
		service.doDelete(gongyxhd);
		logger.info("--------------test3 Gongyxhd----------------");
	}
}
