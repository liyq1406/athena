package com.athena.component.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.athena.util.exception.ServiceException;

/**
 * 单实例类，加载配置信息
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-9-25
 */
public class LoaderProperties {
	private static Logger log = Logger.getLogger(LoaderProperties.class.getName());
	private static LoaderProperties prop;
	private Map<String, String> propMap;
	
	public Map<String, String> getPropMap() {
		return propMap;
	}
	public void setPropMap(Map<String, String> propMap) {
		this.propMap = propMap;
	}
	/**
	 * 构造函数
	 * @param fileName  文件名
	 */
	private LoaderProperties(String fileName){
		try {
			propMap = this.loadPrintProperties(fileName);
		} catch (ServiceException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 读取配置文件信息
	 * @author 贺志国
	 * @date 2012-9-26
	 * @param fileName 文件名
	 * @return map 配置信息
	 * @throws IOException  IO异常
	 * @throws ServiceException 自定义异常
	 */
	public  Map<String,String> loadPrintProperties(String fileName) throws IOException,ServiceException {
		//当前类的classloader
		ClassLoader loader = LoaderProperties.class.getClassLoader();
		//得到指定路径下的资源文件
		ResourceBundle properties = new PropertyResourceBundle(loader.getResourceAsStream(fileName));
		Map<String,String> propMap = new HashMap<String,String>();
		//得到所有的配置信息
		for (Iterator<String> it = properties.keySet().iterator(); it.hasNext();) {
			//路经Key
			String pathKey = it.next().trim();
			//路经Value
			String pathValue = properties.getString(pathKey).trim();
			if(pathValue.length()==0){
				throw new ServiceException(pathKey+" 配置信息不能为空") ;
			}
			propMap.put(pathKey, pathValue);
		}
		return propMap;
	}
	/**
	 *  初始化配置信息
	 * @author 贺志国
	 * @date 2012-9-25
	 * @return Map<String,String> maps 所有属性的值
	 */
	public static Map<String,String> getPropertiesMap(String fileName){  
		if (prop == null) {
			prop = new LoaderProperties(fileName);
		}
		return prop.getPropMap();
	}
}
