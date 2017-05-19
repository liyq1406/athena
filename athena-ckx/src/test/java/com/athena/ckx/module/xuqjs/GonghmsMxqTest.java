package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.GonghmsMxq;
import com.athena.ckx.module.xuqjs.service.GonghmsMxqService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/***
 * 供货模式-毛需求
 * @author denggq
 * @date 2012-05-03
 */
@Component
public class GonghmsMxqTest extends AbstractCompomentTests{
	
	@Inject
	private GonghmsMxqService service;
	
	@Test
	public void test(){
		ArrayList<GonghmsMxq> insert = new ArrayList<GonghmsMxq>();
		GonghmsMxq bean1 = new GonghmsMxq();
		bean1.setGonghms("QQ");
		bean1.setXuqly("DIP");
		bean1.setDinghlx("97X");
		insert.add(bean1);
		
		ArrayList<GonghmsMxq> update = new ArrayList<GonghmsMxq>();
		GonghmsMxq bean2 = new GonghmsMxq();
		bean2.setGonghms("QQ");
		bean2.setXuqly("ZIP");
		bean2.setDinghlx("97D");
		update.add(bean2);
		
		service.save(insert,update,update, "test");
	}
}
