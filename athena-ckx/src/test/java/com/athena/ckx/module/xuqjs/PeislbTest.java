package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Peislb;
import com.athena.ckx.module.xuqjs.service.PeislbService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 配送类别
 * @author denggq
 * @date 2012-4-27
 */
@Component
public class PeislbTest extends AbstractCompomentTests{
	
	@Inject
	private PeislbService service;
	
	@Test
	public void test(){
		ArrayList<Peislb> insert = new ArrayList<Peislb>();
		Peislb bean1 = new Peislb();
		bean1.setPeislx("0000");
		bean1.setPeislxmc("dfg");
		bean1.setBaozlx("21ffg");
		bean1.setBaozsl(123);
		bean1.setZuicddcws(100);
		bean1.setTongbjpbs("2");
		bean1.setShangxd("sdf2123123212");
		bean1.setPeitsxbs("1");
		bean1.setBeihtqq(5.0);
		bean1.setXiaohccxc("12");
		bean1.setShifgj("0");
		bean1.setBeiz("beiz");
		bean1.setShifbhd("1");
		bean1.setBiaos("1");
		insert.add(bean1);
		
		ArrayList<Peislb> update = new ArrayList<Peislb>();
		Peislb bean2 = new Peislb();
		bean2.setPeislx("0000");
		bean2.setPeislxmc("ddd");
		bean2.setBaozlx("21drg");
		bean2.setBaozsl(456);
		bean2.setZuicddcws(431);
		bean2.setTongbjpbs("1");
		bean2.setShangxd("sdf212321");
		bean2.setPeitsxbs("g");
		bean2.setBeihtqq(13.0);
		bean2.setXiaohccxc("21");
		bean2.setShifgj("0");
		bean2.setBeiz("beiz2");
		bean2.setShifbhd("1");
		update.add(bean2);
		
		service.save(insert,update, update, "test");
		
		String[] args = new String[1];
		args[0]= "delete from DEV_CKX_TEST.ckx_peislb where peislx = '0000'";
		DBUtilRemove.remove(args);
	}
}
