package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.CkxZuiddhsl;
import com.athena.ckx.module.xuqjs.service.CkxZuiddhslService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/***
 * 最大订货数量
 * @author denggq
 * @date 2012-05-03
 */
@Component
public class CkxZuiddhslTest extends AbstractCompomentTests{
	
	@Inject
	private CkxZuiddhslService service;
	
	@Test
	public void test(){
		ArrayList<CkxZuiddhsl> insert = new ArrayList<CkxZuiddhsl>();
		CkxZuiddhsl bean1 = new CkxZuiddhsl();
		bean1.setUsercenter("QQ");
		bean1.setLingjbh("1234562890");
		bean1.setGongysbh("3216549872");
		bean1.setNianzq("111111");
		bean1.setZuiddhsl(20.0);
		insert.add(bean1);
		
		ArrayList<CkxZuiddhsl> update = new ArrayList<CkxZuiddhsl>();
		CkxZuiddhsl bean2 = new CkxZuiddhsl();
		bean2.setUsercenter("QQ");
		bean2.setLingjbh("1234562890");
		bean2.setGongysbh("3216549872");
		bean2.setNianzq("111111");
		bean2.setZuiddhsl(30.0);
		update.add(bean2);
		
		service.save(insert,update, update, "test");
	}
}
