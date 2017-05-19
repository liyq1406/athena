package com.athena.ckx.module.transTime;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.module.transTime.service.CkxXiehztxqgjService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxXiehztxqgjTest extends AbstractCompomentTests{

	@Inject
	private CkxXiehztxqgjService service;
	 @Test
	 public void test(){
		LoginUser user =new LoginUser();
		user.setUsername("TEST");
		user.setUsercenter("UW");
		user.setCaption(DateTimeUtil.getAllCurrTime());
		service.addCkxXiehztxqgj(user);
	 }
}
