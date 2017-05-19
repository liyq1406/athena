package com.athena.ckx.module.carry;

import java.util.List;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxWaibwlxx;
import com.athena.ckx.module.carry.service.CkxWaibwlxxService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxWaibwlxxTest extends AbstractCompomentTests {
	@Inject
	private CkxWaibwlxxService service;

	@Test
	public void test() {
		LoginUser user =new LoginUser();
		user.setUsername("KONG");
		user.setUsercenter("UW");
		
		CkxWaibwlxx bean =new CkxWaibwlxx();
		bean.setUsercenter(user.getUsercenter());
		String bianzu="10010";
		
		//添加新编组
		System.out.println(service.save(bean, bianzu, 1, user));
		
		CkxWaibwlxx bean1 =new CkxWaibwlxx();
		bean1.setLujbh(bianzu);
		bean1.setUsercenter(user.getUsercenter());
		//在新编组上添加路径
		System.out.println(service.save(bean1, "", 1, user));
		
		
		bean.setGcbh("gc02");
		bean.setXuh("1");
		//修改数据
		System.out.println(service.save(bean, "", 2, user));
		
		
		
		//物理删除(添加了两条，所以删除两条)
		for (int i = 0; i < 2; i++) {
			System.out.println(service.doDelete(bean));
		}
		
		
		

		List<CkxWaibwlxx> list= service.getSelectLujbhCode(user);
		System.out.println(list.size());

	}
}