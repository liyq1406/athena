package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Jdygzq;
import com.athena.ckx.module.xuqjs.service.JdygzqService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 既定预告周期
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class JdygzqTest extends AbstractCompomentTests{
	
	@Inject
	private JdygzqService service;
	
	@Test
	public void test(){
		ArrayList<Jdygzq> insert = new ArrayList<Jdygzq>();
		Jdygzq bean1 = new Jdygzq();
		bean1.setDinghlx("QQQ");
		bean1.setSuozgyzq("00");
		bean1.setJidzqs(5);
		bean1.setYugzqs(6);
		insert.add(bean1);
		
		ArrayList<Jdygzq> update = new ArrayList<Jdygzq>();
		Jdygzq bean2 = new Jdygzq();
		bean2.setDinghlx("QQQ");
		bean2.setSuozgyzq("00");
		bean2.setJidzqs(4);
		bean2.setYugzqs(4);
		update.add(bean2);
		
		service.save(insert,update, update, "test");
	}
}
