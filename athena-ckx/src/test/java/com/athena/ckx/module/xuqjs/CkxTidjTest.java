package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.CkxTidj;
import com.athena.ckx.module.xuqjs.service.CkxTidjService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 替代件
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class CkxTidjTest extends AbstractCompomentTests{
	
	@Inject
	private CkxTidjService service;
	
	@Test
	public void test(){
		ArrayList<CkxTidj> insert = new ArrayList<CkxTidj>();
		CkxTidj bean1 = new CkxTidj();
		bean1.setUsercenter("UX");
		bean1.setLingjbh("3322118655");
		bean1.setTidljh("2255889966");
		insert.add(bean1);
		
		service.save(insert,insert,insert, "EMINEM");
	}
}
