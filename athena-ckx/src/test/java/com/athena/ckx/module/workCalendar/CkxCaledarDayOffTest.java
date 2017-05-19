package com.athena.ckx.module.workCalendar;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.workCalendar.CkxXiuxr;
import com.athena.ckx.module.workCalendar.service.CkxXiuxrService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxCaledarDayOffTest extends AbstractCompomentTests {
	@Inject
	private CkxXiuxrService service;
	
	
	/**
	 * 测试方法
	 */
	@Test
	public void test(){
		LoginUser user =new LoginUser();
		user.setUsername("KONG");
		
		List<CkxXiuxr> list=new ArrayList<CkxXiuxr>();
		
		
		//实体1
		CkxXiuxr off1=new CkxXiuxr();
		off1.setRiq("2021-04-03");
		off1.setBeiz("测试备注");
		off1.setCreateTime(DateTimeUtil.getAllCurrTime());
		off1.setCreator(user.getUsername());
		off1.setEditor(user.getUsername());
		off1.setEditTime(DateTimeUtil.getAllCurrTime());
		
		
		//实体2
		CkxXiuxr off2=new CkxXiuxr();
		off2.setRiq("2021-04-04");
		off2.setBeiz("测试备注");
		off2.setCreateTime(DateTimeUtil.getAllCurrTime());
		off2.setCreator(user.getUsername());
		off2.setEditor(user.getUsername());
		off2.setEditTime(DateTimeUtil.getAllCurrTime());
		
		list.add(off1);
		list.add(off2);
		
		System.out.println(service.save(list, list, list, user));
	}
}
