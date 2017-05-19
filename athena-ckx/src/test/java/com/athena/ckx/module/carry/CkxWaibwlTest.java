package com.athena.ckx.module.carry;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxWaibwl;
import com.athena.ckx.module.carry.service.CkxWaibwlService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxWaibwlTest extends AbstractCompomentTests {
	@Inject
	private CkxWaibwlService service;
	 @Test
	 public void test(){
		 
		 LoginUser user =new LoginUser();
			user.setUsername("KONG");
		 
		 CkxWaibwl bean=new CkxWaibwl();
		 bean.setUsercenter("UL");
		 bean.setGongysbh("00002");
		 bean.setFahd("01");
		 bean.setMudd("01");
		 bean.setLujbh("10001");
		 //添加
		 System.out.println(service.save(bean, 1, user));
		 
		 
		 //修改
		 bean.setLujbh("10002");
		 System.out.println(service.save(bean, 2, user));
		 
		 String[] args = new String[1];
		 args[0]= "delete from ckx_waibwl where usercenter = '"+bean.getUsercenter()+"' and gongysbh = '"+bean.getGongysbh()+"' and fahd='"+bean.getFahd()+"'  and mudd='"+bean.getMudd()+"'";
		 DBUtilRemove.remove(args);
	 }
}
