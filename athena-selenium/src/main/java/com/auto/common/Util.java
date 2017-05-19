package com.auto.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 工具类
 * @author lizhi
 * @date 2012-8-9
 */
public class Util {
	private static Document doc;
	/**
	 * 获得Properties对象
	 * @param url Properties文件路径
	 * @return Properties
	 * @throws IOException
	 * 
	 * @author lizhi
	 */
//	public static Properties getProperties(String url) throws IOException {
//		InputStream in = new BufferedInputStream(new FileInputStream(url)); 
//	    Properties p = new Properties (); 
//	    p.load(in);
//	    return p;
//	}
	/**
	 * 获得元素和属性的值
	 * 
	 * @param element_attribute(格式element.attribute)
	 * @return HashMap
	 * 
	 * @author lizhi
	 * @date 2012-8-9
	 */
	public static HashMap<String, String> getElement(String element_attribute) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(new File(Steup.xmlFilePath));
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		HashMap<String, String> hashMap = new HashMap<String, String>();
		String[] elements = element_attribute.split("\\.");
		if(elements.length > 0) {
			NodeList nodeList = doc.getElementsByTagName(elements[0]);
			if(elements.length == 2) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element e = (Element) nodeList.item(i);
					String key = e.getAttribute(elements[1]);
					String value = e.getTextContent();
					hashMap.put(key, value);
				}
			}
			else if(elements.length == 3) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element e = (Element) nodeList.item(i);
					String key = e.getAttribute(elements[1]);
					String value = e.getAttribute(elements[2]);
					hashMap.put(key, value);
				}
			}
		}
		return hashMap;
	}
	
	/**
	 * 得到properties的Url
	 * @param key config中的key
	 * @return String
	 */
	public static Properties propertiesUrl(String key) {
		String u = "/src/main/resources/i18n/";
		String url = System.getProperty("user.dir") + u +getElement("propertiesurl.key").get(key);
		InputStream in = null;
		Properties p = null;
		try {
			in = new BufferedInputStream(new FileInputStream(url));
			p = new Properties (); 
			p.load(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    return p;
	}
}
