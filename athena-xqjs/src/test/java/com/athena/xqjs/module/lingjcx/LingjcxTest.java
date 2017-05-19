/**
 * 
 */
package com.athena.xqjs.module.lingjcx;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.module.lingjcx.service.LingjcxService;
import com.toft.core3.container.annotation.Inject;

/**
 * @author dsimedd001
 * 
 */
@TestData(locations = {"classpath:testData/lingjcx/lingjcx.xls"})
public class LingjcxTest extends AbstractCompomentTests {
	@Inject
	private LingjcxService lingjcxService;


}
