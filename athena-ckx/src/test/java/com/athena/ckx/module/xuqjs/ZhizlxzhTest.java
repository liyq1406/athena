package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Zhizlxzh;
import com.athena.ckx.module.xuqjs.service.ZhizlxzhService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 制造路线转换
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class ZhizlxzhTest extends AbstractCompomentTests{
	
	@Inject
	private ZhizlxzhService service;
	
	@Test
	public void test(){
		ArrayList<Zhizlxzh> insert = new ArrayList<Zhizlxzh>();
		Zhizlxzh bean1 = new Zhizlxzh();
		bean1.setUsercenter("QQ");
		bean1.setZhizlxy("000");
		bean1.setZhizlxx("97D");
		insert.add(bean1);
		
		ArrayList<Zhizlxzh> update = new ArrayList<Zhizlxzh>();
		Zhizlxzh bean2 = new Zhizlxzh();
		bean2.setUsercenter("QQ");
		bean2.setZhizlxy("000");
		bean2.setZhizlxx("97X");
		update.add(bean2);
		
		service.save(insert,update, update, "test");
	}
}
