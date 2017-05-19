package com.athena.ckx.module.workCalendar;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.workCalendar.CkxCalendarGroup;
import com.athena.ckx.module.workCalendar.service.CkxCalendarGroupService;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Inject;

public class CkxCalendarGroupTest extends AbstractCompomentTests {
	@Inject
	private CkxCalendarGroupService service;
	
	
	@Test
	public void test(){
		LoginUser user =new LoginUser();
		user.setUsername("KONG");
		user.setUsercenter("UW");
		CkxCalendarGroup bean=new CkxCalendarGroup();
		bean.setBianzh("N102TEST");
		bean.setRilbc("N101TEST");
		bean.setUsercenter("UL");
		bean.setAppobj("C03004");		
		
		try {
			service.addGroup(bean, user);
			
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			service.save(bean, user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			service.doDelete(bean);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		 service.getTotalTime(user,"CX002");
		
		
		
	}
}
