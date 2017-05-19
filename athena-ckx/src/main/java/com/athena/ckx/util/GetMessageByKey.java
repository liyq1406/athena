package com.athena.ckx.util;

import com.athena.component.service.Message;

public class GetMessageByKey {

	private static String URL = "i18n.ckx.@.i18n_message";
	/**
	 * 仅提供service和action调用，其它则用core工程下的类方法
	 * @param key
	 * @return
	 */
	public static String getMessage(String key){
		Message m = new Message(key,getParentPackageByMethod());
		return m.getMessage();
	}
	/**
	 * 仅提供service和action调用，其它则用core工程下的类方法
	 * @param key
	 * @return
	 */
	public static String getMessage(String key,Object[] rValues){
		Message m = new Message(key,getParentPackageByMethod());
		return m.getMessage(rValues);
	}
	/**
	 * 根据调用类找到他向上两级的包名，对应到国际化配置里面
	 * 得到对应的国际化配置路径
	 * i18n.ckx.paicfj.i18n_message
	 * @return
	 */
	private static String getParentPackageByMethod(){
		String packStr = "";
		StackTraceElement[] stack = new Throwable().getStackTrace();
		int stackslen = stack.length;
		if( stackslen > 0){		
			try {	
				//stack[0] 当前方法的对象
				//stack[1] 调用当前方法的对象
				//stack[2] 调用调用当前方法的方法的对象
				packStr = Class.forName(stack[2].getClassName()).getPackage().getName();
			} catch (ClassNotFoundException e) {
//				throw new ServiceException("无法找到该类");
				return URL.replace("@", "ckx"); 
			}
		}
		//得到包路径的到数第二个包的名称
		packStr =packStr.substring(0,packStr.lastIndexOf(".")); 
		String packCenterName = packStr.substring(packStr.lastIndexOf(".")+1);
		return URL.replace("@", packCenterName); 
	}
	
}
