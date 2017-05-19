package com.athena.ckx.module.carry;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxYunswld;
import com.athena.ckx.module.carry.service.CkxWuldService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxWuldTest extends AbstractCompomentTests {
	@Inject
	private CkxWuldService service;
	 @Test
	 public void test(){
		 LoginUser user =new LoginUser();
			user.setUsername("KONG");
		List<CkxYunswld> list=new ArrayList<CkxYunswld>();
		CkxYunswld bean1=new CkxYunswld();
		bean1.setWuldbh("111");
		CkxYunswld bean2=new CkxYunswld();
		bean2.setWuldbh("112");
		list.add(bean1);
		list.add(bean2);
		System.out.println(service.saveWuld(list, list, list, user));
		
		
		//测试被占用数据删除
		List<CkxYunswld> list1=new ArrayList<CkxYunswld>();
		bean1.setWuldbh("101");
		list1.add(bean1);
		System.out.println(service.saveWuld(new ArrayList<CkxYunswld>(), new ArrayList<CkxYunswld>(), list1, user));
		
	 }
}
