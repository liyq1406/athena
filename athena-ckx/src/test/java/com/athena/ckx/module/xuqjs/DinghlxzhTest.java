package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Dinghlxzh;
import com.athena.ckx.module.xuqjs.service.DinghlxzhService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/***
 * 订货路线转换
 * @author denggq
 * @date 2012-05-03
 */
@Component
public class DinghlxzhTest extends AbstractCompomentTests{
	
	@Inject
	private DinghlxzhService service;
	
	@Test
	public void test(){
		ArrayList<Dinghlxzh> insert = new ArrayList<Dinghlxzh>();
		Dinghlxzh bean1 = new Dinghlxzh();
		bean1.setUsercenter("QQ");
		bean1.setLingjbh("1234567890");
		bean1.setZhizlx("97X");
		bean1.setDinghlx("97D");
		insert.add(bean1);
		
		ArrayList<Dinghlxzh> update = new ArrayList<Dinghlxzh>();
		Dinghlxzh bean2 = new Dinghlxzh();
		bean2.setUsercenter("QQ");
		bean2.setLingjbh("1234567890");
		bean2.setZhizlx("97X");
		bean2.setDinghlx("97X");
		update.add(bean2);
		
		service.save(insert,update,update,"test");
	}
}
