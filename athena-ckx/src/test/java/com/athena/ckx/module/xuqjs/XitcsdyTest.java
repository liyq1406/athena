package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Xitcsdy;
import com.athena.ckx.module.xuqjs.service.XitcsdyService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 系统参数定义
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class XitcsdyTest extends AbstractCompomentTests{
	
	@Inject
	private XitcsdyService service;

	@Test
	public void save(){
		Xitcsdy bean = new Xitcsdy();
		bean.setUsercenter("QQ");
		bean.setZidlx("ZD");
		bean.setZidlxmc("字典类型0");
		
		ArrayList<Xitcsdy> insert = new ArrayList<Xitcsdy>();
		Xitcsdy x1=new Xitcsdy();
		x1.setZidbm("1");
		x1.setZidmc("高度");
		x1.setShifqj("1");
		x1.setQujzxz(0.0);
		x1.setQujzdz(9.9);
		x1.setPaix(1);
		insert.add(x1);
		
		Xitcsdy x2=new Xitcsdy();
		x2.setZidbm("2");
		x2.setZidmc("高度");
		x2.setShifqj("1");
		x2.setQujzxz(10.0);
		x2.setQujzdz(19.9);
		x2.setPaix(2);
		insert.add(x2);
		
		ArrayList<Xitcsdy> update = new ArrayList<Xitcsdy>();
		Xitcsdy x3=new Xitcsdy();
		x3.setZidbm("2");
		x3.setZidmc("高度");
		x3.setShifqj("1");
		x3.setQujzxz(10.0);
		x3.setQujzdz(19.9);
		x3.setPaix(2);
		update.add(x2);
		
		logger.info("--------------test1 Xitcsdy----------------");
		logger.info(service.save(bean,1,insert, update, insert, "12345"));
		logger.info("--------------test1 Xitcsdy----------------");
	}
	
	@Test
	public void delete(){
		Xitcsdy bean = new Xitcsdy();
		bean.setUsercenter("QQ");
		bean.setZidlx("ZD");
		bean.setZidlxmc("字典类型0");
		bean.setZidbm("000");
		
		logger.info("--------------test2 Xitcsdy----------------");
		logger.info(service.doDelete(bean));
		logger.info("--------------test2 Xitcsdy----------------");
	}
	
	@Test
	public void copy(){
		
		String[] args = new String[1];
		args[0]= "delete from DEV_CKX_TEST.ckx_xitcsdy where usercenter = 'UX' and zidlx = 'GD'";
		DBUtilRemove.remove(args);
		
		logger.info("--------------test3 Xitcsdy----------------");
		logger.info(service.copy("UW", "GD", "UX", "12345"));
		logger.info("--------------test3 Xitcsdy----------------");
		
		
	}
	
}
