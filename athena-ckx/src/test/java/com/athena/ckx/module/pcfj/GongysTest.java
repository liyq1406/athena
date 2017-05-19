package com.athena.ckx.module.pcfj;



import org.junit.Test;


import com.athena.ckx.entity.paicfj.Ckx_gongys;

import com.athena.ckx.module.paicfj.service.Ckx_gongysService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;

import com.toft.core3.container.annotation.Inject;

public class GongysTest extends AbstractCompomentTests{

	@Inject
	private Ckx_gongysService gongysService;
	@Test
	public void save(){
		String[] args = new String[1];
		args[0]= "delete from ckx_gongys where usercenter = 'UL' and gcbh = 'gys0000009' ";
		DBUtilRemove.remove(args);
		
		Ckx_gongys gongys=new Ckx_gongys();
		gongys.setUsercenter("UL");
		gongys.setGcbh("gys0000009");
		gongys.setGonghlx("AAA");
		gongysService.save(gongys, 1, "张三");
		gongysService.save(gongys, 2, "张三");
		gongysService.removes(gongys, "张三");
		
	}
}
