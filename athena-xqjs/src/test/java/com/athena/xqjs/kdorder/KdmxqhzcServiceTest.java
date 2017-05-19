package com.athena.xqjs.kdorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.xqjs.entity.kdorder.CopyKdmxqhzc;
import com.athena.xqjs.entity.kdorder.Kdmxqhzc;
import com.athena.xqjs.module.kdorder.service.KdmxqhzcService;
import com.toft.core3.container.annotation.Inject;
public class KdmxqhzcServiceTest extends AbstractCompomentTests{
	@Inject
	private KdmxqhzcService kdmxqhzcService;
	
	@Test
	public void testKdmxqhzcService(){
		Map map = new HashMap();
		List all = new ArrayList() ;
		this.kdmxqhzcService.countFene(map);
		this.kdmxqhzcService.doAllDelete() ;
		this.kdmxqhzcService.queryListKdmxqhzc() ;
		this.kdmxqhzcService.doDeleteKdmxqhzc(new CopyKdmxqhzc());
		this.kdmxqhzcService.listInsertMxqhzc(all, new Kdmxqhzc()) ;
	}
}
