package com.athena.xqjs.ilorder;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.module.ilorder.service.ConversionYugzjbService;
import com.toft.core3.container.annotation.Inject;

@TestData(locations = { "classpath:testData/ilOrder/conversionYugzjbRowLine.xls" })
public class ConversionYugzjbServiceTest extends AbstractCompomentTests {

	@Inject
	private ConversionYugzjbService conversionYugzjbService;

	@Test
	public void testYugzjbConversionLine() {
		
		String[] parrten = { "pp", "ps", "pj" };

		for(String ms:parrten){
			
			this.conversionYugzjbService.conversionYugzjbRowLine(ms);
			
			this.conversionYugzjbService.insertDingdmx(ms);
			
		}

	}

}
