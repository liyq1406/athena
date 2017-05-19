package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.GongysRouxbl;
import com.athena.ckx.module.xuqjs.service.GongysRouxblService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/***
 * 供应商-柔性比例
 * @author denggq
 * @date 2012-05-03
 */
@Component
public class GongysRouxblTest extends AbstractCompomentTests{
	
	@Inject
	private GongysRouxblService service;
	
	@Test
	public void test(){
		ArrayList<GongysRouxbl> insert = new ArrayList<GongysRouxbl>();
		GongysRouxbl bean1 = new GongysRouxbl();
		bean1.setUsercenter("QQ");
		bean1.setGongysbh("0000000000");
		bean1.setGuanjljjb("2");
		bean1.setDinghzq("P1");
		bean1.setRouxbl(90.0);
		insert.add(bean1);
		
		ArrayList<GongysRouxbl> update = new ArrayList<GongysRouxbl>();
		GongysRouxbl bean2 = new GongysRouxbl();
		bean2.setUsercenter("QQ");
		bean2.setGongysbh("0000000000");
		bean2.setGuanjljjb("2");
		bean2.setDinghzq("P1");
		bean2.setRouxbl(80.0);
		update.add(bean2);
		
		service.save(insert,update, update, "test");
	}
}
