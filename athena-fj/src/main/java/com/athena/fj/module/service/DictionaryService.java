package com.athena.fj.module.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

/**
 * 数据字典service
 * @author 贺志国
 *
 */
@SuppressWarnings("rawtypes")
@Component
public class DictionaryService extends BaseService {
	
	/**
	 * 参考系客户查询
	 * @return List<Map<String,String>>客户集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectKeh() throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("dictionary.queryKeh");
	}
	
	/**
	 * 参考系运输商查询
	 * @return List<Map<String,String>>运输商集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYunss(String usercenter) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("dictionary.queryYunss",usercenter);
	}
	
	/**
	 * 参考系车型查询
	 * @return List<Map<String,String>>车型集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectChex() throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("dictionary.queryChex");
	}
	
	/**
	 * 参考系运输路线查询
	 * @return List<Map<String,String>>运输路线集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYunslx(String usercenter) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("dictionary.queryYunslx",usercenter);
	}
	
	
	/**
	 * 参考系客户成品库查询，根据运输路线查询客户
	 * @return List<Map<String,String>>客户集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectKehCpk(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("dictionary.queryKehCpk",param);
	}
	
	/**
	 * 
	 * 参考系客户成品库查询，根据运输路线查询运输商
	 * @author 贺志国
	 * @date 2012-3-30
	 * @param param 
	 * @return List<Map<String,String>>运输商集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYunssCpk(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("dictionary.queryYunssCpk",param);
	}
	
	
	/**
	 * 
	 * 参考系客户成品库查询，根据运输商查询车型
	 * @author 贺志国
	 * @date 2012-3-30
	 * @param param 
	 * @return List<Map<String,String>>车型集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectChexYunss(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("dictionary.queryChexYunss",param);
	}
}
