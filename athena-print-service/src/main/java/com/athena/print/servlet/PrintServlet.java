package com.athena.print.servlet;

import javax.servlet.ServletConfig;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.apache.log4j.Logger;

import com.athena.print.controller.GetPrintService;
import com.athena.print.controller.PrintServcieImpl;
import com.toft.core3.web.context.WebSdcContext;
import com.toft.core3.web.context.support.WebSdcContextUtils;

public class PrintServlet extends CXFNonSpringServlet{
	
	private static Logger logger=Logger.getLogger(PrintServlet.class);
	
	@Override
	protected void loadBus(ServletConfig sc) {
		super.loadBus(sc);
		
		
		logger.info("sevlet 启动..");
		WebSdcContext context = WebSdcContextUtils.getWebSdcContext(sc.getServletContext());
		PrintServcieImpl ps = context.getComponent(PrintServcieImpl.class);
		
		JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
		factory.setAddress("/GetPrintService");
		factory.setServiceClass(GetPrintService.class);		
		factory.setServiceBean(ps);
		
		factory.create();
		
		logger.info("sevlet 启动结束..");
	}
}
