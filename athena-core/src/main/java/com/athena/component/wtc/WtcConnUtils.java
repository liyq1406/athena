/**
 * 
 */
package com.athena.component.wtc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weblogic.wtc.gwt.TuxedoConnection;
import weblogic.wtc.gwt.TuxedoConnectionFactory;
import weblogic.wtc.jatmi.TPException;

/**
 * 
 * WTC 工具类
 * @author Administrator
 * 
 */
public class WtcConnUtils {
	private static final Log log = LogFactory.getLog(WtcConnUtils.class);
	
	private final static String TUXEDO_SERVICES_CONNECTION = 
		"tuxedo.services.TuxedoConnection";
	
	private static TuxedoConnectionFactory tuxedoFactory = null;
	
	//初始化tuxedoFactory
	static{
		tuxedoFactory = createTuxedoConnectionFactory();
	}
	
	/**
	 * 
	 * @return
	 */
	private static TuxedoConnectionFactory createTuxedoConnectionFactory(){
		 TuxedoConnectionFactory tuxedoFactory = null;
		Context ctx = null;
		try {
			ctx = new InitialContext();
		} catch (NamingException e) {
			throw new WtcException(""+e.getMessage());
		}
		
		try {
			tuxedoFactory = (TuxedoConnectionFactory) 
						ctx.lookup(TUXEDO_SERVICES_CONNECTION);
		} catch (NamingException e) {
			throw new WtcException("没有发现可用的WTC服务："+e.getMessage());
		}
		return tuxedoFactory;
	}
	/**
	 * 获取Tuxedo连接
	 * @return
	 * @throws NamingException
	 * @throws TPException
	 */
	public static TuxedoConnection getConn() {
		TuxedoConnection conn = null;
		
		if(tuxedoFactory==null){
			log.error("没有有效的WTC服务连接！");
			return null;
		}
		
		try {
			conn = tuxedoFactory.getTuxedoConnection();
		} catch (TPException e) {
			log.error("tuxedo连接获取失败："+e.getMessage());
		}
		return conn;
	}
}
