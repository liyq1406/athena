package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Chexpt;
import com.athena.ckx.module.xuqjs.service.ChexptService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 车型平台关系
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class ChexptTest extends AbstractCompomentTests{
	
	@Inject
	private ChexptService service;
	
	//日志
	private static final Logger logger = Logger.getLogger(ChexptTest.class);

	@Test
	public void test(){
		
		ArrayList<Chexpt> insert = new ArrayList<Chexpt>();
		Chexpt chexpt1 = new Chexpt();
		chexpt1.setUsercenter("QQ");
		chexpt1.setShengcxbhzz("L1");
		chexpt1.setShengcxbhhz("L2");
		chexpt1.setChejbhzz("UU1");
		chexpt1.setChejbhhz("UU2");
		chexpt1.setShengcptbh("123456");
		chexpt1.setLcdv("123");
		insert.add(chexpt1);
		
		ArrayList<Chexpt> update = new ArrayList<Chexpt>();
		Chexpt chexpt2 = new Chexpt();
		chexpt2.setUsercenter("QQ");
		chexpt2.setShengcxbhzz("L1");
		chexpt2.setShengcxbhhz("L2");
		chexpt2.setChejbhzz("UU1");
		chexpt2.setChejbhhz("UU2");
		chexpt2.setShengcptbh("123456");
		chexpt2.setLcdv("234");
		update.add(chexpt2);
		logger.info("--------------test1 Chexpt----------------");
		logger.info(service.save(insert, update, update, "root"));
		logger.info("--------------test1 Chexpt----------------");
	}
	
	@Test
	public void test2(){
		ArrayList<Chexpt> insert = new ArrayList<Chexpt>();
		logger.info("--------------test2 Chexpt----------------");
		logger.info(service.save(insert, insert, insert, "12345"));
		logger.info("--------------test2 Chexpt----------------");
	}
	
}
