package com.athena.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 未分类通用工具类
 * @author zhangl
 *
 */
public class CommonUtil {
	
	/**
	 * 系统级
	 */
	public final static String MODULE_ATHENA = "athena";
	/**
	 * 核心模块
	 */
	public final static String MODULE_CORE = "core";
	/**
	 * 权限模块
	 */
	public final static String MODULE_AUTHORITY = "authority";	
	/**
	 * 需求计算模块
	 */
	public final static String MODULE_XQJS = "xqjs";
	/**
	 * 仓库模块
	 */
	public final static String MODULE_CANGK = "cangk";
	/**
	 * 参考系模块
	 */
	public final static String MODULE_CKX = "ckx";
	/**
	 * 排产模块
	 */
	public final static String MODULE_PC = "pc";
	/**
	 * 发交模块
	 */
	public final static String MODULE_FJ = "fj";
	/**
	 * 打印模块
	 */
	public final static String MODULE_PRINT = "print";	
	/**
	 * 系统调度
	 */
	public final static String MODULE_DIAOD = "diaod";
	/**
	 * 接口模块
	 */
	public final static String MODULE_INTERFACE = "interface";
	
	/**
	 * 去除字符串中 换行符\r,制表符\t,回车\n
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {  
		String dest = "";  
		if ( null != str ) {  
		    Pattern p = Pattern.compile("\t|\r|\n");  
		    Matcher m = p.matcher(str);  
		    dest = m.replaceAll("");  
		    str = dest;
		}  
		return str;  
	} 
	
	/**
	 * 获取当前运行类和其方法
	 * @return 包.类.方法()
	 */
	public static String getClassMethod(){
		String str="";
		
		StackTraceElement[] stack = new Throwable().getStackTrace();
		int stackslen = stack.length;
		if( stackslen > 0){
			str = stack[1].getClassName()+"."+stack[1].getMethodName()+"()"; //stack[1].getLineNumber();
		}
		
		return str;
	}
	
	/**
	 * 对象为空返回空串,不为空toString
	 * @return 
	 */
	public static String strNull(Object obj) {
		return obj == null ? "" : obj.toString();
	}
	
	/**
	 * 根据参数第一位拼接消耗点
	 * 若参数第一位是"L"、"X"、"W"，则使用"U"拼接
	 * 若参数第一位其他，则使用"V"拼接
	 * 以后根据新加厂名可能会做出更改
	 * @param str
	 * @return str
	 * 当传入参数为null时，返回null
	 * 当传入参数为""时，返回""
	 */
	public static String getUsercenter(String str){
		if (null!=str && !"".equals(str)) {
			str = str.substring(0, 1).toUpperCase();
			if(str.equals("L")||str.equals("X")||str.equals("W")){
				str = "U" + str;
			}else{
				str = "V" + str;
			}
		}
		return str;
	}
	
}
