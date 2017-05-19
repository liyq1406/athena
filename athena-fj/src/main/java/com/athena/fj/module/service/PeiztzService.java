package com.athena.fj.module.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.fj.entity.Peizjh;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-3-20
 */
@Component
public class PeiztzService extends BaseService<Peizjh> {
	
	/**
	 * 不分页的配载计划查询
	 * @author 贺志国
	 * @date 2012-3-23
	 * @param param 页面查询参数
	 * @return List<Map<String,String>> 配载计划集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectPeizjhOfTiaoz(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peiztz.queryPeizjhOfTiaoz", param);
	}
	
	
	/**
	 * 根据配载单号查找要货令明细表中的要货令号
	 * @author 贺志国
	 * @date 2012-3-26
	 * @param param:peizdh,usercenter
	 * @return List<Map<String,String>> 内部要货令集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYaohlOfPeizd(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peiztz.queryYaohlOfPeizd",param);
	}
	
	
	/**
	 * 配载计划下的要货令转移
	 * @author 贺志国
	 * @date 2012-3-29
	 * @param param yaohls,peizdh要货令和配载单号
	 */
	@Transactional
	public void moveYaohlToPeizjh(Map<String,String> param) throws ServiceException{
		//修改要货令明细表中的要货令的配载单号
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peiztz.updatePeizdhOfYaohlmx", param);
		
	}
	
	/**
	 * 查询转移后的配载计划下的要货令数量
	 * @author 贺志国
	 * @date 2012-3-29
	 * @param param 配载单号
	 * @return String 要货令数量
	 */
	@Transactional
	public String selectCountYaohlOfPeizjh(String peizdh) throws ServiceException{
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("peiztz.selectCountYaohlOfPeizjh", peizdh);
	}
	
	
	/**
	 * 查询要车明细对应的策略下的包装数量
	 * @author 贺志国
	 * @date 2012-4-16
	 * @param param
	 * @return String 策略下的包装数量
	 */
	public String selectCelBaozsl(Map<String,String> param){
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("peiztz.selectCelBaozsl", param);
	}
	
	
	/**
	 * 修改要车明细表中的是否满载状态为1
	 * @author 贺志国
	 * @date 2012-4-16
	 * @param yaocmxID
	 */
	public void updateYaocmxShifmz(String yaocmxID){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peiztz.updateYaocmxShifmz", yaocmxID);
	}
	
	
	
	/**
	 * 如果配载计划下的要货令全部转移则删除配载计划
	 * @author 贺志国
	 * @date 2012-3-29
	 * @param param
	 */
	@Transactional
	public void deletePeizjhOfTiaoz(Map<String,String> param) throws ServiceException{
		 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peiztz.deletePeizjh", param);
	}
	
	
	/**
	 * 配载计划下的要货令零件汇总
	 * @author 贺志国
	 * @date 2012-3-29
	 * @param param peizdh,usercenter 配载单号和用户中心
	 * @return List<Map<String,String>> 零件汇总集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectPeizjhOfLingj(Map<String, String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peiztz.queryPeizjhOfLingj", param);
	}
	
	/**
	 * 配载计划下的包装组汇总
	 * @author 贺志国
	 * @date 2012-3-29
	 * @param param peizdh 配载单号
	 * @return List<Map<String,String>> 包装组汇总集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectPeizjhOfBaozz(Map<String, String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peiztz.queryPeizjhOfBaozz", param);
	}
	
}
