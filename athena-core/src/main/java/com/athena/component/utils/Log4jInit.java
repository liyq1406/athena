package com.athena.component.utils;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.toft.core3.container.annotation.Component;

/**
 * Log4j初始化类
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2013-10-30
 */
@Component
public class Log4jInit extends HttpServlet {

	/**
	 * 自动生成序列
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(Log4jInit.class);
	/**
	 * 初始化Log4j
	 */
	public void init(){
		String file = getInitParameter("log4j");        
	    logger.info("................log4j start");
        if (file != null) {
        	//当前类的classloader
    		ClassLoader loader = Log4jInit.class.getClassLoader();
            Properties ps=new Properties();
            try {
                ps.load(loader.getResourceAsStream(file));
            } catch (IOException e) {
            	logger.error(e.getMessage());
            }
            PropertyConfigurator.configure(ps);
        }
	}
	
}
