/**
 * 
 */
package com.athena.print.core;


import org.junit.Test;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.print.controller.GetQtaskController;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;



/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
@Component
public class PrinterMonitorTests extends AbstractCompomentTests{
	
	
	@Inject
	private GetQtaskController getQtaskController;;
	
	
	@Inject
	protected AbstractIBatisDao baseDao;
	
	@Test
	public void testMonitorStart(){
		getQtaskController.startQtaskController();
	}
}
