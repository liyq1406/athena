package com.athena.ckx.module.xuqjs;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Ziygzlx;
import com.athena.ckx.module.xuqjs.service.ZiygzlxService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 资源跟踪类型对应表
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class ZiygzlxTest extends AbstractCompomentTests{
	
	@Inject
	private ZiygzlxService service;
	
	@Test
	public void test(){
		Ziygzlx bean1 = new Ziygzlx();
		bean1.setJislxbh("000");
		bean1.setJslxmc("456");
		bean1.setXuqly("000");
		bean1.setDinghlx("97W");
		bean1.setGonghms("SY");
		bean1.setJisff("1");
		service.save(bean1,1, "test");
		
		Ziygzlx bean2 = new Ziygzlx();
		bean2.setJislxbh("000");
		bean2.setJslxmc("sdfsdfs");
		bean2.setXuqly("000");
		bean2.setDinghlx("97W");
		bean2.setGonghms("CD");
		bean2.setJisff("2");
		service.save(bean2,2, "test");
		
		service.doDelete(bean2);
		
	}
}
