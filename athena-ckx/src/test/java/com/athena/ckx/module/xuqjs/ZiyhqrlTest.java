package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Ziyhqrl;
import com.athena.ckx.module.xuqjs.service.ZiyhqrlService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 资源获取日历
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class ZiyhqrlTest extends AbstractCompomentTests{
	
	@Inject
	private ZiyhqrlService service;
	
	@Test
	public void test(){
		ArrayList<Ziyhqrl> insert = new ArrayList<Ziyhqrl>();
		Ziyhqrl bean1 = new Ziyhqrl();
		bean1.setZiyhqrq("2012-05-03");
		insert.add(bean1);
		
		ArrayList<Ziyhqrl> delete = new ArrayList<Ziyhqrl>();
		Ziyhqrl bean2 = new Ziyhqrl();
		bean2.setZiyhqrq("2012-05-03");
		delete.add(bean2);
		
		service.save(insert,delete, "test");
	}
}
