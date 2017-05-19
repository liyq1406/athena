package com.athena.xqjs.ilorder;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.toft.core3.container.annotation.Inject;

public class DoCalculateTest extends AbstractCompomentTests{
	@Inject
	private IlOrderService ilorderservice;
	@Test
	//@TestData(locations={"classpath:testData/ilorder/DoCalculateTest.xls"})
	public void doCaculateTest() throws Exception{
	  
		 
//		String [] banc = {"94916","94920","94917"} ;
//		ilorderservice.doCalculate("97W","PP", "2012-02-13", "001", banc,null);
//		ilorderservice.doCalculate("97W","PS", "2012-02-13", "001", banc,null);
//		ilorderservice.doCalculate("97W","PJ", "2012-02-13", "001", banc,null);
//		String [] banc = {"17175"} ;
//		List<String> list = new ArrayList<String>();
//		list.add("Juan0002");
//		list.add("Juan0001");
//		ilorderservice.doCalculate("UGB","PP", "2012-06-04", "001", banc,list);
	}

}
