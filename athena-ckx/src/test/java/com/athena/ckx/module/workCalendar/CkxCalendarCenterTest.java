package com.athena.ckx.module.workCalendar;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.module.workCalendar.service.CkxCalendarCenterService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxCalendarCenterTest extends AbstractCompomentTests {
	
	@Inject
	private CkxCalendarCenterService service;
	/**
	 * 测试方法
	 */
	@Test
	public void test(){
		LoginUser user =new LoginUser();
		user.setUsername("KONG");
		try {
			System.out.println(service.addYear("UL", "2012", user));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println(service.delYear("2012", "UL"));;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
