package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Xiaohcmb;
import com.athena.ckx.module.xuqjs.service.XiaohcmbService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 小火车运输时刻模板
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class XiaohcmbTest extends AbstractCompomentTests{
	
	@Inject
	private XiaohcmbService service;

	@Test
	public void test1(){
		ArrayList<Xiaohcmb> insert = new ArrayList<Xiaohcmb>();
		Xiaohcmb xiaohcmb1 = new Xiaohcmb();
		xiaohcmb1.setUsercenter("QQ");
		xiaohcmb1.setShengcxbh("00000");
		xiaohcmb1.setXiaohcbh("00000");
		xiaohcmb1.setTangc(1);
		xiaohcmb1.setBeihpysj(20);
		xiaohcmb1.setShangxpysj(20);
		insert.add(xiaohcmb1);
		
		Xiaohcmb xiaohcmb2 = new Xiaohcmb();
		xiaohcmb2.setUsercenter("QQ");
		xiaohcmb2.setShengcxbh("00000");
		xiaohcmb2.setXiaohcbh("00000");
		xiaohcmb2.setTangc(2);
		xiaohcmb2.setBeihpysj(50);
		xiaohcmb2.setShangxpysj(20);
		insert.add(xiaohcmb2);
		
		ArrayList<Xiaohcmb> update = new ArrayList<Xiaohcmb>();
		Xiaohcmb xiaohcmb3 = new Xiaohcmb();
		xiaohcmb3.setUsercenter("QQ");
		xiaohcmb3.setShengcxbh("00000");
		xiaohcmb3.setXiaohcbh("00000");
		xiaohcmb3.setTangc(2);
		xiaohcmb3.setBeihpysj(45);
		xiaohcmb3.setShangxpysj(20);
		update.add(xiaohcmb3);
		
		try{
			logger.info("--------------test1 Xiaohcmb----------------");
			logger.info(service.save(insert, update, insert, "12345"));
			logger.info("--------------test1 Xiaohcmb----------------");
		}catch(Exception e){
			logger.error("-------------test1 error-------------"+e.getMessage());
		}
	}
	
	@Test
	public void test2(){
		ArrayList<Xiaohcmb> insert = new ArrayList<Xiaohcmb>();
		Xiaohcmb xiaohcmb1 = new Xiaohcmb();
		xiaohcmb1.setUsercenter("QQ");
		xiaohcmb1.setShengcxbh("00000");
		xiaohcmb1.setXiaohcbh("00000");
		xiaohcmb1.setTangc(1);
		xiaohcmb1.setBeihpysj(20);
		xiaohcmb1.setShangxpysj(20);
		insert.add(xiaohcmb1);
		
		Xiaohcmb xiaohcmb2 = new Xiaohcmb();
		xiaohcmb2.setUsercenter("QQ");
		xiaohcmb2.setShengcxbh("00000");
		xiaohcmb2.setXiaohcbh("00000");
		xiaohcmb2.setTangc(2);
		xiaohcmb2.setBeihpysj(20);
		xiaohcmb2.setShangxpysj(20);
		insert.add(xiaohcmb2);
		
		ArrayList<Xiaohcmb> update = new ArrayList<Xiaohcmb>();
		Xiaohcmb xiaohcmb3 = new Xiaohcmb();
		xiaohcmb3.setUsercenter("QQ");
		xiaohcmb3.setShengcxbh("00000");
		xiaohcmb3.setXiaohcbh("00000");
		xiaohcmb3.setTangc(2);
		xiaohcmb3.setBeihpysj(50);
		xiaohcmb3.setShangxpysj(20);
		update.add(xiaohcmb3);
		
		try{
			logger.info("--------------test2 Xiaohcmb----------------");
			logger.info(service.save(insert, update, insert, "12345"));
			logger.info("--------------test2 Xiaohcmb----------------");
		}catch(Exception e){
			logger.error("-------------test2 error-------------"+e.getMessage());
		}
		
	}
	
	
	
	@Test
	public void test3(){
		ArrayList<Xiaohcmb> insert = new ArrayList<Xiaohcmb>();
		Xiaohcmb xiaohcmb1 = new Xiaohcmb();
		xiaohcmb1.setUsercenter("QQ");
		xiaohcmb1.setShengcxbh("00000");
		xiaohcmb1.setXiaohcbh("00000");
		xiaohcmb1.setTangc(1);
		xiaohcmb1.setBeihpysj(20);
		xiaohcmb1.setShangxpysj(20);
		insert.add(xiaohcmb1);
		
		Xiaohcmb xiaohcmb2 = new Xiaohcmb();
		xiaohcmb2.setUsercenter("QQ");
		xiaohcmb2.setShengcxbh("00000");
		xiaohcmb2.setXiaohcbh("00000");
		xiaohcmb2.setTangc(2);
		xiaohcmb2.setBeihpysj(50);
		xiaohcmb2.setShangxpysj(20);
		insert.add(xiaohcmb2);
		
		ArrayList<Xiaohcmb> update = new ArrayList<Xiaohcmb>();
		Xiaohcmb xiaohcmb3 = new Xiaohcmb();
		xiaohcmb3.setUsercenter("QQ");
		xiaohcmb3.setShengcxbh("00000");
		xiaohcmb3.setXiaohcbh("00000");
		xiaohcmb3.setTangc(2);
		xiaohcmb3.setBeihpysj(15);
		xiaohcmb3.setShangxpysj(20);
		update.add(xiaohcmb3);
		
		try{
			logger.info("--------------test3 Xiaohcmb----------------");
			logger.info(service.save(insert, update, insert, "12345"));
			logger.info("--------------test3 Xiaohcmb----------------");
		}catch(Exception e){
			logger.error("-------------test3 error-------------"+e.getMessage());
		}
	}
	
	@Test
	public void test4(){
		ArrayList<Xiaohcmb> insert = new ArrayList<Xiaohcmb>();
		Xiaohcmb xiaohcmb1 = new Xiaohcmb();
		xiaohcmb1.setUsercenter("QQ");
		xiaohcmb1.setShengcxbh("00000");
		xiaohcmb1.setXiaohcbh("00000");
		xiaohcmb1.setTangc(1);
		xiaohcmb1.setBeihpysj(20);
		xiaohcmb1.setShangxpysj(20);
		insert.add(xiaohcmb1);
		
		Xiaohcmb xiaohcmb2 = new Xiaohcmb();
		xiaohcmb2.setUsercenter("QQ");
		xiaohcmb2.setShengcxbh("00000");
		xiaohcmb2.setXiaohcbh("00000");
		xiaohcmb2.setTangc(2);
		xiaohcmb2.setBeihpysj(10);
		xiaohcmb2.setShangxpysj(20);
		insert.add(xiaohcmb2);
		
		ArrayList<Xiaohcmb> update = new ArrayList<Xiaohcmb>();
		Xiaohcmb xiaohcmb3 = new Xiaohcmb();
		xiaohcmb3.setUsercenter("QQ");
		xiaohcmb3.setShengcxbh("00000");
		xiaohcmb3.setXiaohcbh("00000");
		xiaohcmb3.setTangc(2);
		xiaohcmb3.setBeihpysj(15);
		xiaohcmb3.setShangxpysj(20);
		update.add(xiaohcmb3);
		
		try{
			logger.info("--------------test4 Xiaohcmb----------------");
			logger.info(service.save(insert, update, insert, "12345"));
			logger.info("--------------test4 Xiaohcmb----------------");
		}catch(Exception e){
			logger.error("-------------test4 error-------------"+e.getMessage());
		}
	}
	
}
