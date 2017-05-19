package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Danwhs;
import com.athena.ckx.module.xuqjs.service.DanwhsService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/***
 * 单位换算
 * @author denggq
 * @date 2012-05-03
 */
@Component
public class DanwhsTest extends AbstractCompomentTests{
	
	@Inject
	private DanwhsService service;
	
	@Test
	public void test(){
		ArrayList<Danwhs> insert = new ArrayList<Danwhs>();
		Danwhs bean1 = new Danwhs();
		bean1.setUsercenter("QQ");
		bean1.setBeihdw("GR");
		bean1.setMubdw("KG");
		bean1.setHuansbl(1000.000);
		insert.add(bean1);
		
		ArrayList<Danwhs> update = new ArrayList<Danwhs>();
		Danwhs bean2 = new Danwhs();
		bean2.setUsercenter("QQ");
		bean2.setBeihdw("GR");
		bean2.setMubdw("KG");
		bean2.setHuansbl(99.999);
		update.add(bean2);
		
		service.save(insert,update, update, "test");
	}
}
