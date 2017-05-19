package com.athena.component.service;

import javax.jws.WebService;

/**
 * 按需计算WebService接口服务类
 * @author WL
 *
 */
@WebService
public interface AnxJsInter {

	public void anxOrderMethod(String username,String usercenter) throws Exception;
	
	public void anxOrderMethodUL()throws Exception ;
	public void anxOrderMethodUW()throws Exception;
	
}
