package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Xuqly;
import com.athena.ckx.module.xuqjs.service.XuqlyService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 需求来源-作用域
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class XuqlyTest extends AbstractCompomentTests{
	
	@Inject
	private XuqlyService service;
	
	@Test
	public void test(){
		ArrayList<Xuqly> insert = new ArrayList<Xuqly>();
		Xuqly bean1 = new Xuqly();
		bean1.setXuqly("000");
		bean1.setZuoyy("000");
		insert.add(bean1);
		
		ArrayList<Xuqly> update = new ArrayList<Xuqly>();
		Xuqly bean2 = new Xuqly();
		bean2.setXuqly("000");
		bean2.setZuoyy("000");
		update.add(bean2);
		
		service.save(insert,update, "test");
	}
}
