package com.athena.ckx.module.workCalendar;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.workCalendar.CkxCalendarVersion;
import com.athena.ckx.module.workCalendar.service.CkxCalendarVersionService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxCalendarVersionTest extends AbstractCompomentTests {
	@Inject
	private CkxCalendarVersionService service;
	
//	@Test
	public void test(){
		LoginUser user =new LoginUser();
		user.setUsername("KONG");
		
		//service.edit(editList, user);
		
		try {
			System.out.println(service.addVersion("2012", "UW", "TEST", user));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		try {
			System.out.println(service.copyVersion("N201TEST", "TEST", user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println(service.addDay("N201TEST", "2013", user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println(service.delVersion("N201TEST", user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CkxCalendarVersion bean=new CkxCalendarVersion();
		bean.setUsercenter("UW");
		service.getVersionNo(bean);

		service.timingTask();
		
	}
	@Test
	public void editNianzx(){
		CkxCalendarVersion bean=new CkxCalendarVersion();
		bean.setUsercenter("UW");
		bean.setBanc("NA10KD01");
		
//		bean.setNianzx("201247");
//		bean.setYnianzx("201248");
//		bean.setRiq("2012-11-25");             //2012-11-19   || 2012-11-25
//		bean.setZhoux("03");
		
//		bean.setNianzx("201248");
//		bean.setYnianzx("201247");
//		bean.setRiq("2012-11-19");             //2012-11-19   || 2012-11-25
//		bean.setZhoux("02");
		
		
		//跨年
//		bean.setNianzx("201254");
//		bean.setYnianzx("201301");
//		bean.setRiq("2013-01-06");             //2013-01-06   || 2013-01-01
//		bean.setZhoux("01");
		
		bean.setNianzx("201301");
		bean.setYnianzx("201254");
		bean.setRiq("2013-01-01");             //2013-01-06   || 2013-01-01
		bean.setZhoux("05");
		
		
		
		
		LoginUser login = new LoginUser();
		login.setUsername("root");
		service.eidtNianzx(bean,login);
	}
}
