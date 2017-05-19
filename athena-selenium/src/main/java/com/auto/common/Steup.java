package com.auto.common;

import java.util.HashMap;

/**
 * All attribute into this class
 * 
 * @author yq
 * 
 */
public class Steup {
	// 运行使用的浏览器
	public static String browser; 
	// 统一密码
	public static String password;
	// 切换多个浏览器
	public static HashMap<String, String> url; 
	// 切换多个用户登录
	public static HashMap<String, String> username; 
	// 切换用户中心
	public static HashMap<String, String> usercenter; 
	// 设置页面超时时间
	public static String timeout; 
	//批处理地址
	public static String batchHost;
	//批处理密码
	public static String batchPassword;
	//批处理用户
	public static String batchName;
	//准备层接口SFTPhost
	public static String zbcInterfaceSFTPHost;
	//准备层接口SFTP用户名
	public static String zbcInterfaceSFTPName;
	//准备层接口SFTP密码
	public static String zbcInterfaceSFTPpwd;
	//准备层接口SFTP端口
	public static int zbcInterfaceSFTPport;
	//准备层接口SFTP下载上传路径
	public static String zbcInterfaceSFTPdirectory;
	
	//执行层接口SFTPhost
	public static String zxcInterfaceSFTPHost;
	//执行层接口SFTP用户名
	public static String zxcInterfaceSFTPName;
	//执行层接口SFTP密码
	public static String zxcInterfaceSFTPpwd;
	//执行层接口SFTP端口
	public static int zxcInterfaceSFTPport;
	//执行层接口SFTP下载上传路径
	public static String zxcInterfaceSFTPdirectory;
	
	//DDBH接口SFTPhost
	public static String ddbhInterfaceSFTPHost;
	//DDBH接口SFTP用户名
	public static String ddbhInterfaceSFTPName;
	//DDBH接口SFTP密码
	public static String ddbhInterfaceSFTPpwd;
	//DDBH接口SFTP端口
	public static int ddbhInterfaceSFTPport;
	//DDBH接口SFTP下载上传路径
	public static String ddbhInterfaceSFTPdirectory;
	
	//配置文件的路径
	public static String xmlFilePath = System.getProperty("user.dir")
	+ "\\config.xml";
	public static String reportPath = System.getProperty("user.dir")
			+ "\\Report\\";
}
