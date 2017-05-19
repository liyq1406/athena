package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Tongbjplj;
import com.athena.ckx.module.xuqjs.service.TongbjpljService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 同步集配零件分类
 * @author denggq
 * @date 2012-4-12
 */
@Component
public class TongbjpljTest extends AbstractCompomentTests{
	
	@Inject
	private TongbjpljService service;
	
	@Test
	public void test(){
		ArrayList<Tongbjplj> insert = new ArrayList<Tongbjplj>();
		Tongbjplj bean1 = new Tongbjplj();
		bean1.setUsercenter("QQ");
		bean1.setLingjbh("0000000000");
		bean1.setShengcxbh("00000");
		bean1.setNclass("222");
		bean1.setNclass("222");
		bean1.setLingjmc("零件1");
		bean1.setPeislx("0000");
		bean1.setQianhcbs("1");
		bean1.setJipbzwz("1");
		insert.add(bean1);
		
		ArrayList<Tongbjplj> update = new ArrayList<Tongbjplj>();
		Tongbjplj bean2 = new Tongbjplj();
		bean2.setUsercenter("QQ");
		bean2.setLingjbh("0000000000");
		bean2.setShengcxbh("00000");
		bean2.setNclass("222");
		bean2.setNvalue("888");
		bean2.setLingjmc("零件2");
		bean2.setPeislx("0000");
		bean2.setQianhcbs("0");
		bean2.setJipbzwz("0");
		update.add(bean2);
		
		service.save(insert,update, update, "test");
	}
}
