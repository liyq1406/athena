package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.CkxXiaohcyssk;
import com.athena.ckx.module.xuqjs.service.CkxXiaohcysskService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;


/**
 * @description 小火车运输时刻表
 * @author denggq
 * @date 2012-4-20
 */

@Component
public class CkxXiaohcysskTest extends AbstractCompomentTests{
	
	@Inject
	private CkxXiaohcysskService service;


	@Test
	public void test(){
		service.calculateXiaohcYssk("test");
		
		ArrayList<CkxXiaohcyssk> insert = new ArrayList<CkxXiaohcyssk>();
		service.save(insert, insert, insert, "test");
		
		CkxXiaohcyssk bean1 = new CkxXiaohcyssk();
		bean1.setUsercenter("QQ");
		bean1.setShengcxbh("00000");
		bean1.setXiaohcbh("00000");
		bean1.setRiq("2012-05-03");
		bean1.setTangc(0);
		bean1.setKaisbhsj("2012-05-03 15:00:00");
		bean1.setChufsxsj("2012-05-03 15:30:00");
		insert.add(bean1);
		
		ArrayList<CkxXiaohcyssk> update = new ArrayList<CkxXiaohcyssk>();
		
		CkxXiaohcyssk bean2 = new CkxXiaohcyssk();
		bean2.setUsercenter("QQ");
		bean2.setShengcxbh("00000");
		bean2.setXiaohcbh("00000");
		bean2.setRiq("2012-05-03");
		bean2.setTangc(0);
		bean2.setKaisbhsj("2012-05-03 14:00:00");
		bean2.setChufsxsj("2012-05-03 14:30:00");
		update.add(bean2);
		
		service.save(insert, update, update, "test");
	}
	
}
