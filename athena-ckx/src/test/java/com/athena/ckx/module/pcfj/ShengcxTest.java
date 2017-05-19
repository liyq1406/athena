package com.athena.ckx.module.pcfj;



import org.junit.Test;

import com.athena.ckx.entity.paicfj.Ckx_shengcx;
import com.athena.ckx.module.paicfj.service.Ckx_shengcxService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class ShengcxTest extends AbstractCompomentTests {

	@Inject
	private Ckx_shengcxService shengcxService;
	@Test
	public void save(){
		String[] args = new String[1];
		args[0]= "delete from ckx_shengcx where usercenter = 'UL' and shengcxbh = 'UXFL9' ";
		DBUtilRemove.remove(args);
		Ckx_shengcx shengcx=new Ckx_shengcx();
		shengcx.setShengcxbh("UXFL9");
		shengcx.setUsercenter("UL");
//		shengcx.setTessjxq1("1");
		shengcxService.save(shengcx, 1, "张三");
		shengcxService.save(shengcx, 2, "张三");
		
	}
	
	@Test
	public void save1(){
		String[] args = new String[1];
		args[0]= "delete from ckx_shengcx where usercenter = 'UL' and shengcxbh = 'UXFL9' ";
		DBUtilRemove.remove(args);
		Ckx_shengcx shengcx=new Ckx_shengcx();
		shengcx.setShengcxbh("UXFL9");
		shengcx.setUsercenter("UL");
//		shengcx.setTessjxq1("1");
		shengcxService.save(shengcx, 1,  "张三");
		shengcxService.remove(shengcx, "张三");
		
	}
}
