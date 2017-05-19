package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Jiaofmsd;
import com.athena.ckx.module.xuqjs.service.JiaofmsdService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 交付码设定
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class JiaofmsdTest extends AbstractCompomentTests{
	
	@Inject
	private JiaofmsdService service;

	@Test
	public void test(){
		ArrayList<Jiaofmsd> insert = new ArrayList<Jiaofmsd>();
		Jiaofmsd jiaofmsd1 = new Jiaofmsd();
		jiaofmsd1.setUsercenter("QQ");
		jiaofmsd1.setJiaofm("000");
		jiaofmsd1.setZhouxh("7");
		jiaofmsd1.setXingqxh("7");
		insert.add(jiaofmsd1);
		
		ArrayList<Jiaofmsd> update = new ArrayList<Jiaofmsd>();
		
		logger.info("--------------test1 Jiaofmsd----------------");
		logger.info(service.save(insert, update, insert, "12345"));
		logger.info("--------------test1 Jiaofmsd----------------");
		
	}
	
	
	@Test
	public void test2(){
		ArrayList<Jiaofmsd> insert = new ArrayList<Jiaofmsd>();
		logger.info("--------------test2 Jiaofmsd----------------");
		logger.info(service.save(insert, insert, insert, "12345"));
		logger.info("--------------test2 Jiaofmsd----------------");
	}
	
}
