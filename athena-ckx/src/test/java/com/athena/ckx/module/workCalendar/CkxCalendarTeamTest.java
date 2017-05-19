package com.athena.ckx.module.workCalendar;

import java.util.List;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.workCalendar.CkxCalendarTeam;
import com.athena.ckx.module.workCalendar.service.CkxCalendarTeamService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Inject;

public class CkxCalendarTeamTest extends AbstractCompomentTests {
	@Inject
	private CkxCalendarTeamService service;
	
	@SuppressWarnings("unchecked")
	@Test
	public void test(){
		LoginUser user =new LoginUser();
		user.setUsername("KONG");
		CkxCalendarTeam bean=new CkxCalendarTeam();
		bean.setKaissj("09:30:00");
		bean.setJiezsj("10:00:00");
		bean.setBan("A");
		bean.setXingqxh("1");
		
		//增加一条
		try {
			System.out.println(service.addTeam(bean, "TEST", user));
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bean.setBianzh("N201TEST");
		//增加一条
		try {
			System.out.println(service.addTeam(bean, "", user));
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		List<CkxCalendarTeam> editList=service.list(bean);
		try {
			System.out.println(service.edit(editList, user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println(service.copyTeam("TEST", "N201TEST", user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println(service.delTeam( "N202TEST", user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println(service.delTeam( "N201TEST", user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<CkxCalendarTeam> list = service.getSelectTeamCode(new CkxCalendarTeam());
		System.out.println(list.size());
		
			String[] args = new String[2];
			args[0]= "delete from ckx_calendar_team where bianzh = 'N201TEST' ";
			args[1]= "delete from ckx_calendar_team where bianzh = 'N202TEST' ";
			DBUtilRemove.remove(args);
	}
}
