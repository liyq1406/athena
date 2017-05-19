package com.auto.init;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.auto.common.Steup;
import com.auto.common.Util;

public class ReadConfig {

	private Document doc = null;
	private Steup steup = null;

	public ReadConfig() {
		steup = new Steup();
	}

	public void init() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(new File(Steup.xmlFilePath));
	}

	public void viewXML() {
		try {
			this.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Element element = doc.getDocumentElement();

		// 设置浏览器
		NodeList nodeList = doc.getElementsByTagName("browser");
		steup.browser = nodeList.item(0).getTextContent();

		// 设置密码
		nodeList = doc.getElementsByTagName("password");
		steup.password = nodeList.item(0).getTextContent();

		// 设置超时时间
		nodeList = doc.getElementsByTagName("timeout");
		steup.timeout = nodeList.item(0).getTextContent();
		
		//批处理地址
		nodeList = doc.getElementsByTagName("batchHost");
		steup.batchHost = nodeList.item(0).getTextContent();
		
		//批处理用户名
		nodeList = doc.getElementsByTagName("batchName");
		steup.batchName = nodeList.item(0).getTextContent();
		
		//批处理密码
		nodeList = doc.getElementsByTagName("batchPassword");
		steup.batchPassword = nodeList.item(0).getTextContent();
		
		//准备层接口SFTPhost
		nodeList = doc.getElementsByTagName("zbcInterfaceSFTPHost");
		steup.zbcInterfaceSFTPHost = nodeList.item(0).getTextContent();
		
		//准备层接口SFTP用户名
		nodeList = doc.getElementsByTagName("zbcInterfaceSFTPName");
		steup.zbcInterfaceSFTPName = nodeList.item(0).getTextContent();
		
		//准备层接口SFTP密码
		nodeList = doc.getElementsByTagName("zbcInterfaceSFTPpwd");
		steup.zbcInterfaceSFTPpwd = nodeList.item(0).getTextContent();
		
		//准备层接口SFTP端口
		nodeList = doc.getElementsByTagName("zbcInterfaceSFTPport");
		steup.zbcInterfaceSFTPport = Integer.valueOf(nodeList.item(0).getTextContent());
		
		//准备层接口SFTP下载上传路径
		nodeList = doc.getElementsByTagName("zbcInterfaceSFTPdirectory");
		steup.zbcInterfaceSFTPdirectory = nodeList.item(0).getTextContent();
		
		//执行层接口SFTPhost
		nodeList = doc.getElementsByTagName("zxcInterfaceSFTPHost");
		steup.zxcInterfaceSFTPHost = nodeList.item(0).getTextContent();
		
		//执行层接口SFTP用户名
		nodeList = doc.getElementsByTagName("zxcInterfaceSFTPName");
		steup.zxcInterfaceSFTPName = nodeList.item(0).getTextContent();
		
		//执行层接口SFTP密码
		nodeList = doc.getElementsByTagName("zxcInterfaceSFTPpwd");
		steup.zxcInterfaceSFTPpwd = nodeList.item(0).getTextContent();
		
		//执行层接口SFTP端口
		nodeList = doc.getElementsByTagName("zxcInterfaceSFTPport");
		steup.zxcInterfaceSFTPport = Integer.valueOf(nodeList.item(0).getTextContent());
		
		//执行层接口SFTP下载上传路径
		nodeList = doc.getElementsByTagName("zxcInterfaceSFTPdirectory");
		steup.zxcInterfaceSFTPdirectory = nodeList.item(0).getTextContent();
		
		//DDBH接口SFTPhost
		nodeList = doc.getElementsByTagName("ddbhInterfaceSFTPHost");
		steup.ddbhInterfaceSFTPHost = nodeList.item(0).getTextContent();
		
		//DDBH接口SFTP用户名
		nodeList = doc.getElementsByTagName("ddbhInterfaceSFTPName");
		steup.ddbhInterfaceSFTPName = nodeList.item(0).getTextContent();
		
		//DDBH接口SFTP密码
		nodeList = doc.getElementsByTagName("ddbhInterfaceSFTPpwd");
		steup.ddbhInterfaceSFTPpwd = nodeList.item(0).getTextContent();
		
		//DDBH接口SFTP端口
		nodeList = doc.getElementsByTagName("ddbhInterfaceSFTPport");
		steup.ddbhInterfaceSFTPport = Integer.valueOf(nodeList.item(0).getTextContent());
		
		//DDBH接口SFTP下载上传路径
		nodeList = doc.getElementsByTagName("ddbhInterfaceSFTPdirectory");
		steup.ddbhInterfaceSFTPdirectory = nodeList.item(0).getTextContent();
		
		
		// 设置系统访问地址
		steup.url = Util.getElement("url.key");

		// 设置系统用户
		steup.username = Util.getElement("username.role");

		// 设置用户中心
		steup.usercenter = Util.getElement("username.role.usercenter");
		
	}
}
