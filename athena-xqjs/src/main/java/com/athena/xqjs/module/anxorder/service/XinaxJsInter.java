package com.athena.xqjs.module.anxorder.service;

import com.athena.util.exception.ServiceException;

/**
 * 新按需计算接口服务类
 * @author zbb
 *
 */

public interface XinaxJsInter {

	public void xinaxOrderMethodUW() throws ServiceException;
	
	public void xinaxOrderMethodUL() throws ServiceException;
	
	public void xinaxOrderMethodVD() throws ServiceException;
	
	public void xinaxOrderMethod(String username,String usercenter) throws ServiceException;
	
}
