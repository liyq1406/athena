package com.athena.pc.module.service;

import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.pc.entity.Yaonbhl;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

import com.athena.db.ConstantDbCode;

/**
 * <p>
 * Title:未处理需求业务类
 * </p>
 * <p>
 * Description:定义未处理需求业务方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-6-19
 */
@Component
public class WeiclxqService extends BaseService<Yaonbhl> {

	/**
	 * 未处理需求查询
	 * @author 贺志国
	 * @date 2012-6-19
	 * @param page 分页查询
	 * @param param 页面查询参数
	 * @return Map<String,Object> 查询结果集
	 */
	public Map<String,Object> selectWeiclxq(Pageable page,Map<String,String> param){
		//20170220-xss
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("weiclxq.queryWeiclxq", param, page);
	}
	
}
