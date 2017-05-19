/**
 * 
 */
package com.athena.component.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;

import com.toft.core3.container.ComponentsException;
import com.toft.core3.context.SdcContext;
import com.toft.core3.context.SdcContextAware;
/**
 * @author Administrator
 *
 */
@RunWith(SdcJUnit4ClassRunner.class)
public class AbstractCompomentTests implements SdcContextAware{
	protected final Log logger = LogFactory.getLog(getClass());
	
	protected SdcContext sdcContext;
	
	@Override
	public final void setSdcContextAware(final SdcContext sdcContext) throws ComponentsException {
		this.sdcContext = sdcContext;
	}

}
