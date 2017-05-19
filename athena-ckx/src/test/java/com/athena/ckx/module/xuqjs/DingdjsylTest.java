package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Dingdjsyl;
import com.athena.ckx.module.xuqjs.service.DingdjsylService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/***
 * 订单计算依赖关系
 * @author denggq
 * @date 2012-05-03
 */
@Component
public class DingdjsylTest extends AbstractCompomentTests{
	
	@Inject
	private DingdjsylService service;
	
	@Test
	public void test(){
		ArrayList<Dingdjsyl> insert = new ArrayList<Dingdjsyl>();
		Dingdjsyl bean1 = new Dingdjsyl();
		bean1.setGongysbh("1234567895");
		bean1.setWaibghms("PP");
		bean1.setYilgx("YKC");
		insert.add(bean1);
		
		ArrayList<Dingdjsyl> update = new ArrayList<Dingdjsyl>();
		Dingdjsyl bean2 = new Dingdjsyl();
		bean2.setGongysbh("1234567895");
		bean2.setWaibghms("PS");
		bean2.setYilgx("YJF");
		update.add(bean2);
		
		service.save(insert,update, update, "test");
	}
}
