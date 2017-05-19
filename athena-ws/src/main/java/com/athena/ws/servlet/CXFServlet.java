/**
 * 
 */
package com.athena.ws.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.toft.core3.io.Resource;
import com.toft.core3.util.ClassUtils;
import com.toft.core3.web.context.WebSdcContext;
import com.toft.core3.web.context.support.ServletContextResourcePatternResolver;
import com.toft.core3.web.context.support.WebSdcContextUtils;


/**
 * <p>Title:SDC webservice组件</p>
 *
 * <p>Description:webService servlet类</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class CXFServlet extends CXFNonSpringServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7734724383772698614L;
	//日记
	private static final Log logger = LogFactory.getLog(CXFServlet.class);
	
	/* (non-Javadoc)
	 * @see org.apache.cxf.transport.servlet.CXFNonSpringServlet#loadBus(javax.servlet.ServletConfig)
	 */
	public void loadBus(ServletConfig servletConfig) throws ServletException {
		super.loadBus(servletConfig);
		this.loadServiceBeans(servletConfig);
	}
	
	/**
	 * 加载webservice配置文件并创建webService服务
	 * @param servletConfig
	 */
	@SuppressWarnings("unchecked")
	private void loadServiceBeans(ServletConfig servletConfig){
		WebSdcContext context =
			WebSdcContextUtils.getWebSdcContext(servletConfig.getServletContext());
		if(context==null){
			logger.error("sdc webcontext has't loaded,please check!");
			return;
		}
		//读取doc对象
		Document doc = readDocument(servletConfig);
		if(doc!=null){
			List<Element> serviceElements = 
				doc.selectNodes("webservices/service");
			//对service配置元素进行解析，创建webservice服务
			for(Element elem:serviceElements){
				this.createServiceBean(context, parseClass(elem.attributeValue("class")),
						elem.attributeValue("address"));
			}
		}
	}
	
	/**
	 * 读取配置文件为DOM4j 的Document对象
	 * @param servletConfig
	 * @return
	 */
	private Document readDocument(ServletConfig servletConfig){
		SAXReader  saxReader = new SAXReader();//创建解析器
		Resource resource = loadResource(servletConfig);//获取资源
		if(resource==null){
			logger.error("webservice config file error!");
			return null;
		}
		Document doc = null;
		try {
			//使用saxReader读取资源文件为Document对象
			doc = saxReader.read(resource.getInputStream());
		} catch (DocumentException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return doc;
	}
	/**
	 * 
	 * @param clazzName
	 * @return
	 */
	private Class<?> parseClass(String clazzName){
		try {
			return ClassUtils.forName(clazzName, this.getClass().getClassLoader());
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
		} catch (LinkageError e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	/**
	 * 加载配置文件
	 * @param servletConfig
	 * @return
	 */
	private Resource loadResource(ServletConfig servletConfig){
		//从servlet配置的参数config中获取配置文件地址
		String config = servletConfig.getInitParameter("config");
		if(config==null||config.equals("")){
			config = "classpath:config/athena-webservice.xml";//默认的文件地址
		}
		//创建web配置文件解析器
		ServletContextResourcePatternResolver 
			resolver = new ServletContextResourcePatternResolver(servletConfig.getServletContext());
		//根据配件文件路径获取资源
		Resource resource = resolver.getResource(config);
		return resource;
	}
	
	/**
	 * 创建serviceBean
	 * @param context
	 * @param beanClazz
	 * @param address
	 */
	private void createServiceBean(WebSdcContext context,
			Class<?> beanClazz,
			String address){
		if(beanClazz==null){
			logger.error("attribute 'class' is not wrong!");
		}
		//创建bena工厂类
		ServerFactoryBean factory = new ServerFactoryBean();
		factory.setBus(bus);//设置bus
		factory.setAddress(address);//设置访问地址
		factory.setServiceBean(context.getComponent(beanClazz));//设置服务实例
		factory.create();//创建
	}
}
