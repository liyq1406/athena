package com.athena.util.cache;

import org.apache.log4j.Logger;

import com.toft.core3.context.SdcContextEvent;
import com.toft.core3.context.SdcContextListener;

/***
 * 缓存监听类
 * @author WL
 * @date 2011-11-4
 */
public class CacheListener implements SdcContextListener{
	protected static Logger logger = Logger.getLogger(CacheListener.class);	//定义日志方法
	/**
	 * 监听组件名
	 */
	private String componentName = "cacheListner";
	
	/**
	 * 监听销毁方法
	 * @author WL
     * @date 2011-11-4
	 */
	public void contextDestroyed(SdcContextEvent arg0) {
		//销毁指定单例的元件
		arg0.getSdcContext().destroySingleton(componentName);
	}

	/**
	 * 监听初始化方法
	 * @author WL
     * @date 2011-11-4
	 */
	public void contextInitialized(SdcContextEvent arg0) {
		//获取缓存管理类
		ComponentManager com = arg0.getSdcContext().getComponent(ComponentManager.class);
		try {
			//初始化缓存
			com.init();
		} catch (Exception e) {
			logger.error("监听初始化失败"+e.toString());
		}
	}

	
	
}
