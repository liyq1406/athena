package com.athena.ckx.module.carry;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxWuldlx;
import com.athena.ckx.module.carry.service.CkxWuldlxService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxWuldlxTest extends AbstractCompomentTests {
	@Inject
	private CkxWuldlxService service;
	 @Test
	 public void test(){
		 LoginUser user =new LoginUser();
			user.setUsername("KONG");
			List<CkxWuldlx> list=new ArrayList<CkxWuldlx>();
			CkxWuldlx bean1=new CkxWuldlx();
			bean1.setWuldlxbh("0001");
			CkxWuldlx bean2=new CkxWuldlx();
			bean2.setWuldlxbh("0002");
			list.add(bean1);
			list.add(bean2);
			System.out.println(service.saveType(list, list, list, user));
			
			
			
			//测试被占用数据删除
			List<CkxWuldlx> list1=new ArrayList<CkxWuldlx>();
			bean1.setWuldlxbh("1001");
			list1.add(bean1);
			System.out.println(service.saveType(new ArrayList<CkxWuldlx>(), new ArrayList<CkxWuldlx>(), list1, user));
			
	 }
}
