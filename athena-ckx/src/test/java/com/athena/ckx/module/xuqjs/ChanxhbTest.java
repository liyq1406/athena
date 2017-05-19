package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Chanxhb;
import com.athena.ckx.module.xuqjs.service.ChanxhbService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class ChanxhbTest extends AbstractCompomentTests{
	
	@Inject
	private ChanxhbService service;
	
	@Test
	public void test(){
		ArrayList<Chanxhb> insert = new ArrayList<Chanxhb>();
		Chanxhb bean1 = new Chanxhb();
		bean1.setUsercenter("QQ");
		bean1.setYuancx("0000");
		bean1.setMubcx("23dd");
		insert.add(bean1);
		
		ArrayList<Chanxhb> update = new ArrayList<Chanxhb>();
		Chanxhb bean2 = new Chanxhb();
		bean2.setUsercenter("QQ");
		bean2.setYuancx("0000");
		bean2.setMubcx("dddd");
		update.add(bean2);
		
		service.save(insert,update, update, "EMINEM");
	}
}
