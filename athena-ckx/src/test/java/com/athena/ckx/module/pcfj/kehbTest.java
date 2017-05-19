package com.athena.ckx.module.pcfj;



import org.junit.Test;

import com.athena.ckx.entity.paicfj.Ckx_kehb;
import com.athena.ckx.module.paicfj.service.Ckx_kehbService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;

import com.toft.core3.container.annotation.Inject;

public class kehbTest extends AbstractCompomentTests {

	@Inject
	private Ckx_kehbService kehbService;
	@Test
	public void insert(){
		String[] args = new String[1];
		args[0]= "delete from ckx_kehb where kehbh = 'kh000011' ";
		DBUtilRemove.remove(args);
		
		Ckx_kehb kehb = new Ckx_kehb();
		kehb.setKehbh("kh000011");
		kehb.setChuanz("123");
		kehbService.save(kehb,1,"张三");
		kehbService.save(kehb,2,"张三");
		kehbService.remove(kehb, "张三");
		
		
	}
}
