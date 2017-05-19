package com.athena.ckx.module.carry;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxCangkxhsj;
import com.athena.ckx.module.carry.service.CkxCangkxhsjService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxCangkxhsjTest extends AbstractCompomentTests {
	@Inject
	private CkxCangkxhsjService service;
	 @Test
	 public void test(){
		 LoginUser user =new LoginUser();
			user.setUsername("KONG");
		CkxCangkxhsj bean =new CkxCangkxhsj();
		bean.setUsercenter("UC");
		bean.setCangkbh("C01001");
		bean.setFenpqhck("F0001");
		bean.setMos("R1");
		
		System.out.println(service.addCkxCangkxhsj(bean, user));
		System.out.println(service.save(bean, user));
		System.out.println(service.deleteLogic(bean, user));
		String[] args = new String[1];
		args[0]= "delete from ckx_cangkxhsj where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and fenpqhck='"+bean.getFenpqhck()+"'  and mos='"+bean.getMos()+"'";
		DBUtilRemove.remove(args);
	 }
		 
}
