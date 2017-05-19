package com.athena.pc.module.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.pc.entity.Yuemnjh;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:月模拟计划跟踪逻辑处理类
 * </p>
 * <p>
 * Description:定义月模拟计划跟踪处理方法
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
 * @date 2012-2-13
 */
@Component
public class YuemnjhgzService extends BaseService<Yuemnjh> {
	
	/**
	 * 产线查询
	 * @return List产线列表
	 * @author 贺志国
	 * @date 2012-2-13
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectChanx(String usercenter){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemnjhgz.queryShengcx",usercenter);
	}
	
	/**
	 * 月模拟计划跟踪查询
	 * @author 贺志国
	 * @date 2012-2-9
	 * @param page 分页显示
	 * @param param 查询参数
	 * @return Map<String,Object>
	 */
	public Map<String,Object> select(Pageable page,Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("yuemnjhgz.queryYuemnjh",param,page);
	}
	
	
	/**
	 * 月模拟计划跟踪查询
	 * @author 贺志国
	 * @date 2012-2-9
	 * @param page 分页显示
	 * @param param 查询参数
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public List<Yuemnjh> select(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemnjhgz.queryYuemnjh",param);
	}
	
	/**
	 * 返回sqlmap-xxx.xml配置文件中的namespace属性名
	 */
	@Override
	protected String getNamespace(){
		return "yuemnjhgz";
	}
	
}
