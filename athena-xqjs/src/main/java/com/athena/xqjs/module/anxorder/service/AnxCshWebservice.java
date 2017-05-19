package com.athena.xqjs.module.anxorder.service;

import javax.jws.WebService;

import com.athena.xqjs.entity.anxorder.Chushzyb;


/**
 * 按需初始化执行层webservice调用接口
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2015-7-15
 */
@WebService
public interface AnxCshWebservice {
	/**
	 * 执行层异常出库量和消耗量查询
	 * @author 贺志国
	 * @date 2015-7-15
	 * @return
	 */
	public  Chushzyb queryAnxcshYcxhl(Chushzyb bean);
}
