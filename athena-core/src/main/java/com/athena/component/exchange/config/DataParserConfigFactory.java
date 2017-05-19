/**
 * 
 */
package com.athena.component.exchange.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;


/**
 * @author Administrator
 *
 */
public class DataParserConfigFactory {
	
	private static final Log logger = LogFactory.getLog(DataParserConfigFactory.class);
	
	private Map<String,DataParserConfig> dataParserConfigs = //解析数据配置
		Collections.synchronizedMap(new HashMap<String,DataParserConfig>());
	
	private static final DataParserConfigFactory dpcf = 
		new DataParserConfigFactory();
	/*
	 * 初始化方法
	 */
	public void init(){
		loadConfig();
	}
	
	private DataParserConfigFactory(){
		init();
	}
	public static DataParserConfigFactory getInstance(){
		return dpcf;
	}
	/**
	 * 加载配置文件
	 */
//	private void loadConfig(){
//		clear();
//		URL rul = DataParserConfigFactory.class.getClassLoader().getResource("config/exchange/exchange-config.xml");
//		
//		InputStream in =null;
//		try {
//			in = rul.openStream();
//		} catch (IOException e) {
//			//记录日志 系统级异常
//		}
//		
//		Document doc =
//			DataParserXmlHelper.readFromXml(in);
//		dataParserConfigs.putAll(DataParserXmlHelper.parseConfigs(doc));
//		logger.info("成功加载数据交换配置文件.");
//	}
	/**
	 * 加载配置文件
	 * 	 将config/exchange中的 以exchange-config开头的文件加载解析
	 */
	private void loadConfig(){
		clear();
//		URL url = DataParserConfigFactory.class.getClassLoader().getResource("config/exchange");
//		File file = new File(url.getPath());
//		File[] files = file.listFiles();
//		if(files.length>0){
//			for (int i = 0; i < files.length; i++) {
//				InputStream in = null;
//				if(files[i].getName().startsWith("exchange-config")){
//					try {
//						in = new FileInputStream(files[i]);
//					} catch (IOException e) {
//						//记录日志 系统级异常
//					}
//					Document doc = DataParserXmlHelper.readFromXml(in);
//					dataParserConfigs.putAll(DataParserXmlHelper.parseConfigs(doc));
//				}				
//			}
//		}
		
		URL rul = DataParserConfigFactory.class.getClassLoader().getResource("config/exchange/exchange-config.xml");
		InputStream in =null;
		try {
			in = rul.openStream();
		} catch (IOException e) {
			//记录日志 系统级异常
		}
		Document doc =
			DataParserXmlHelper.readFromXml(in);
		dataParserConfigs.putAll(DataParserXmlHelper.parseConfigs(doc));
		
		URL rul2 = DataParserConfigFactory.class.getClassLoader().getResource("config/exchange/exchange-config-ddbh.xml");
		InputStream in2 =null;
		try {
			in2 = rul2.openStream();
		} catch (IOException e) {
			//记录日志 系统级异常
		}
		Document doc2 =
			DataParserXmlHelper.readFromXml(in2);
		dataParserConfigs.putAll(DataParserXmlHelper.parseConfigs(doc2));
		
		URL rul3 = DataParserConfigFactory.class.getClassLoader().getResource("config/exchange/exchange-config-zbc.xml");
		InputStream in3 =null;
		try {
			in3 = rul3.openStream();
		} catch (IOException e) {
			//记录日志 系统级异常
		}
		Document doc3 =
			DataParserXmlHelper.readFromXml(in3);
		dataParserConfigs.putAll(DataParserXmlHelper.parseConfigs(doc3));
		
		URL rul4 = DataParserConfigFactory.class.getClassLoader().getResource("config/exchange/exchange-config-zxc.xml");
		InputStream in4 =null;
		try {
			in4 = rul4.openStream();
		} catch (IOException e) {
			//记录日志 系统级异常
		}
		Document doc4 =
			DataParserXmlHelper.readFromXml(in4);
		dataParserConfigs.putAll(DataParserXmlHelper.parseConfigs(doc4));
		
		
		logger.info("成功加载数据交换配置文件.");
	}
	
	public DataParserConfig getDataParserConfig(String configId){
		return dataParserConfigs.get(configId);
	}
	
	public void clear(){
		dataParserConfigs.clear();
	}
}
