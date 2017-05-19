package com.athena.xqjs.module.anxorder.service;

import javax.jws.WebService;

import com.athena.util.exception.ServiceException;

/**
 * 按需计算WebService接口服务类
 * @author WL
 *
 */
@WebService
public interface AnxJsInter {

	public void anxOrderMethodUW() throws ServiceException;
	
	public void anxOrderMethodUL() throws ServiceException;
	
	public void anxOrderMethod(String username,String usercenter) throws ServiceException;
	
}
