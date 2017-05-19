package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.Xiaohccx;
import com.athena.ckx.module.xuqjs.service.XiaohcService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 小火车-小火车车厢
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class XiaohcTest extends AbstractCompomentTests{
	
	@Inject
	private XiaohcService xiaohcservice;

	//日志
	private static final Logger logger = Logger.getLogger(XiaohcTest.class);
	
	@Test
	public void test(){
		Xiaohc xiaohc1 = new Xiaohc();
		xiaohc1.setUsercenter("QQ");
		xiaohc1.setShengcxbh("00000");
		xiaohc1.setXiaohcbh("00000");
		xiaohc1.setXiaohcmc("小火车1");
		xiaohc1.setBiaos("1");
		
		ArrayList<Xiaohccx> insert = new ArrayList<Xiaohccx>();
			
		ArrayList<Xiaohccx> edit = new ArrayList<Xiaohccx>();
		
		ArrayList<Xiaohccx> delete = new ArrayList<Xiaohccx>();
		
		logger.info("-----------add Xiaohc------------");
		xiaohcservice.save(xiaohc1, 1, edit, edit, edit, "12345");
		logger.info("-----------add Xiaohc------------");
		
		Xiaohc xiaohc2 = new Xiaohc();
		xiaohc2.setUsercenter("QQ");
		xiaohc2.setShengcxbh("00000");
		xiaohc2.setXiaohcbh("00000");
		xiaohc2.setXiaohcmc("小火车2");
		logger.info("-----------edit Xiaohc------------");
		xiaohcservice.save(xiaohc2, 2, edit, edit, edit, "12345");
		logger.info("-----------edit Xiaohc------------");
		
		Xiaohccx xiaohccx1 = new Xiaohccx();
		xiaohccx1.setChexh("1111");
		xiaohccx1.setBiaos("1");
		insert.add(xiaohccx1);	
		logger.info("-----------add Xiaohccx------------");
		xiaohcservice.save(xiaohc2, 2, insert, edit, edit, "12345");
		logger.info("-----------add Xiaohccx------------");
		
		logger.info("-----------edit Xiaohccx------------");
		Xiaohccx xiaohccx2 = new Xiaohccx();
		xiaohccx2.setChexh("1111");
		xiaohccx2.setBiaos("1");
		edit.add(xiaohccx2);
		xiaohcservice.save(xiaohc2, 2, delete, edit, edit, "12345");
		logger.info("-----------edit Xiaohccx------------");
		
		xiaohcservice.doDelete(xiaohc2);
		
		String[] args = new String[2];
		args[0]= "delete from DEV_CKX_TEST.ckx_xiaohc where usercenter = 'QQ' and shengcxbh = '00000' and xiaohcbh = '00000'";
		args[1]= "delete from DEV_CKX_TEST.ckx_xiaohccx where usercenter = 'QQ' and shengcxbh = '00000' and xiaohcbh = '00000' and chexh = '1111'";
		DBUtilRemove.remove(args);
	}
	
}
