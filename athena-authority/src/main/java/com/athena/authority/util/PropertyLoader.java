/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 穆伟
 * @version v1.0
 * @date 
 */
package com.athena.authority.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.service.DicService;

public class PropertyLoader {
	// log4j日志初始化
	private final Log log = LogFactory.getLog(PropertyLoader.class);
	public String getPropertyInfo(String paramInfo){
		InputStream inputStream = null;
		inputStream = this.getClass().getClassLoader().getResourceAsStream("com/athena/ckx/config/sqlmap.properties");
		Properties p = new Properties();
		 try {
			p.load(inputStream);
		} catch (IOException e) {
			log.error("加载属性文件失败");
		}
		String newParam = "";
		String tempDBSchemal0 = (String)p.getProperty("dbSchemal0");
		String tempDBSchemal1 = (String)p.getProperty("dbSchemal1");
		String tempDBSchemal2 = (String)p.getProperty("dbSchemal2");
		String tempDBSchemal3 = (String)p.getProperty("dbSchemal3");
		String tempDBSchemal4 = (String)p.getProperty("dbSchemal4");
		if(paramInfo.indexOf("[dbSchemal0]")>=0){
			newParam = paramInfo.replaceFirst("\\[dbSchemal0\\]", tempDBSchemal0);
		}else if(paramInfo.indexOf("[dbSchemal1]")>=0){
			newParam = paramInfo.replaceAll("\\[dbSchemal1\\]", tempDBSchemal1);
		}else if(paramInfo.indexOf("[dbSchemal2]")>=0){
			newParam = paramInfo.replaceAll("\\[dbSchemal2\\]", tempDBSchemal2);
		}else if(paramInfo.indexOf("[dbSchemal3]")>=0){
			newParam = paramInfo.replaceAll("\\[dbSchemal3\\]", tempDBSchemal3);
		}else if(paramInfo.indexOf("[dbSchemal4]")>=0){
			newParam = paramInfo.replaceAll("\\[dbSchemal4\\]", tempDBSchemal4);
		}
		return newParam;
	}
	
	/**
	 * 仓库数据权限过滤掉此组已经选择的数据
	 * @author 王国首
	 * @param paramInfo
	 * @param postCode
	 * @return
	 */
	public String getNewDataSql(String paramInfo,String postCode){
		//gswang 2012-09-27 mantis 0004307
		String newParam = paramInfo;
		if(paramInfo.indexOf("[AND]")>=0){
			if(postCode != null && postCode.length()>0 ){
				newParam = paramInfo.replaceFirst("\\[AND\\]", " AND POST_ID = '"+postCode+"' ");
			}else{
				newParam = paramInfo.replaceFirst("\\[AND\\]", "");
			}
		}
		return newParam;
	}
}
