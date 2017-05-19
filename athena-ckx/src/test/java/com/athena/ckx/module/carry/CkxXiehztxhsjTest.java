package com.athena.ckx.module.carry;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxXiehztxhsj;
import com.athena.ckx.module.carry.service.CkxXiehztxhsjService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxXiehztxhsjTest extends AbstractCompomentTests {
	@Inject
	private CkxXiehztxhsjService service;
	 @Test
	 public void test(){
		 LoginUser user =new LoginUser();
			user.setUsername("KONG");
		CkxXiehztxhsj bean= new CkxXiehztxhsj();
		bean.setUsercenter("UL");
		bean.setCangkbh("C01001");
		bean.setXiehztbh("XH0002");
		bean.setMos("R2");
		
		System.out.println(service.addCkxXiehztxhsj(bean, user));
		System.out.println(service.save(bean, user));
		System.out.println(service.deleteLogic(bean, user));
		String[] args = new String[1];
		args[0]= "delete from ckx_xiehztxhsj where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and xiehztbh='"+bean.getXiehztbh()+"'  and mos='"+bean.getMos()+"'";
		DBUtilRemove.remove(args);
	 }
}
