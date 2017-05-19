/**
 * 
 */
package com.athena.component.ws;

import javax.servlet.ServletConfig;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import com.athena.component.service.DispatchService;
import com.athena.component.service.InterfaceService;
import com.athena.component.service.imp.InterfaceServiceImp;
import com.toft.core3.web.context.WebSdcContext;
import com.toft.core3.web.context.support.WebSdcContextUtils;




/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class CXFServlet extends CXFNonSpringServlet {

	private static final long serialVersionUID = -4010110681113306837L;
	
	public void loadBus(ServletConfig servletConfig){
		super.loadBus(servletConfig);
		
		//2.52版本
//		JaxWsServerFactoryBean factory1 = new JaxWsServerFactoryBean();	
//		//发布调度程序
//		factory1.setServiceClass(DispatchService.class);
//		factory1.setAddress("/dispatchServiceImpl");	
//		WebSdcContext context = WebSdcContextUtils.getWebSdcContext(servletConfig.getServletContext());
//		DispatchService ds = context.getComponent(DispatchService.class);
//		factory1.setServiceBean(ds);
//		factory1.create();
		
		//发布调度程序
		ServerFactoryBean factory1 = new ServerFactoryBean();
		factory1.setAddress("/dispatchServiceImpl");
		factory1.setServiceClass(DispatchService.class);
		WebSdcContext context = WebSdcContextUtils.getWebSdcContext(servletConfig.getServletContext());
		DispatchService ds = context.getComponent(DispatchService.class);
		factory1.setServiceBean(ds); 	
		factory1.create();
		
		//发布接口调用 测试weblogic传参 hzg 2013-5-14
	/*	ServerFactoryBean factory3 = new ServerFactoryBean();
		factory3.setAddress("/testHelloImpl");
		factory3.setServiceClass(AnxJsInter.class);
		factory3.setServiceBean(new TestHelloImpl()); 	
		factory3.create();*/
		
		
		//发布接口调用
		ServerFactoryBean factory2 = new ServerFactoryBean();
		factory2.setAddress("/InterfaceServiceImp");
		factory2.setServiceClass(InterfaceService.class);
		factory2.setServiceBean(new InterfaceServiceImp()); 	
		//factory2.getInInterceptors().add(new MyIntercetpr(Phase.RECEIVE));
		factory2.create();
		
		// 2.52 发布接口调用
//		JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
//		factory.setServiceClass(InterfaceService.class);
//		factory.setAddress("/InterfaceServiceImp");
//		factory.setServiceBean(new InterfaceServiceImp());
//		//factory.getInInterceptors().add(new MyIntercetpr(Phase.RECEIVE));
//		factory.create();
	}
}
