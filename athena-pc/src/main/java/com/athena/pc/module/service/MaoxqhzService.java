package com.athena.pc.module.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.pc.entity.Yuemnjh;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;

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
public class MaoxqhzService extends BaseService<Yuemnjh> {
	
	/**
	 * 产线查询
	 * @return List产线列表
	 * @author 贺志国
	 * @date 2012-2-13
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectGongyzqs(Map<String,String> param)  throws ServiceException{
        try{
		    return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("maoxqhz.queryGongyzqs");
         }catch(DataAccessException e){
              throw new ServiceException(e.getMessage());
         }
	}
	
	/**
	 * 毛需求查询
	 * @author 王国首
	 * @date 2012-4-25
	 * @param page 分页显示
	 * @param param 查询参数
	 * @return Map<String,Object>
	 */
	public Map<String,Object> select(Pageable page,Map<String,String> param) throws ServiceException{
		Map<String,Object> result = new HashMap<String,Object>();
		if(param.get("chullx").equals("PP")){
			result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("maoxqhz.queryMaoxqPP",param,page);
		}else if(param.get("chullx").equals("PJ")){
			result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("maoxqhz.queryMaoxqPJ",param,page);
		}
		return result;
	}
	
	/**
	 * 工业周期时间范围查询
	 * @param gongyzq 工业周期号
	 * @return List<Map<String,String>> 工业周期时间范围
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectGyzqsjfw(String gongyzq)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("maoxqhz.queryGyzqfw", gongyzq);
	}
}
