package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Jiaofmzd;
import com.athena.ckx.module.xuqjs.service.JiaofmzdService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 交付码字典
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class JiaofmzdTest extends AbstractCompomentTests{
	
	@Inject
	private JiaofmzdService service;

	@Test
	public void test(){
		ArrayList<Jiaofmzd> insert = new ArrayList<Jiaofmzd>();
		Jiaofmzd jiaofmzd1 = new Jiaofmzd();
		jiaofmzd1.setUsercenter("QQ");
		jiaofmzd1.setJiaofm("000");
		jiaofmzd1.setShuom("123");
		jiaofmzd1.setBeiz("676");
		jiaofmzd1.setBiaos("1");
		insert.add(jiaofmzd1);
		
		ArrayList<Jiaofmzd> update = new ArrayList<Jiaofmzd>();
		Jiaofmzd jiaofmzd2 = new Jiaofmzd();
		jiaofmzd2.setUsercenter("QQ");
		jiaofmzd2.setJiaofm("000");
		jiaofmzd2.setShuom("1234");
		jiaofmzd2.setBeiz("6764");
		update.add(jiaofmzd2);
		
		logger.info("--------------test1 Jiaofmzd----------------");
		logger.info(service.save(insert, update, update, "12345"));
		logger.info("--------------test1 Jiaofmzd----------------");
		
		String[] args = new String[1];
		args[0]= "delete from DEV_CKX_TEST.ckx_jiaofmzd where usercenter = 'QQ' and jiaofm = '000'";
		DBUtilRemove.remove(args);
	}
	
	
	@Test
	public void test2(){
		ArrayList<Jiaofmzd> insert = new ArrayList<Jiaofmzd>();
		logger.info("--------------test2 Jiaofmzd----------------");
		logger.info(service.save(insert, insert, insert, "12345"));
		logger.info("--------------test2 Jiaofmzd----------------");
	}
	
}
