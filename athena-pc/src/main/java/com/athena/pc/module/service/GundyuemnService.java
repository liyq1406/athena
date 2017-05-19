package com.athena.pc.module.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.pc.entity.Yuemnjh;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:滚动周期模拟计划逻辑处理类
 * </p>
 * <p>
 * Description:定义滚动周期模拟处理方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 王国首
 * @E-mail gswang@isoftstone.com
 * @version v1.0
 * @date 2012-7-10
 */
@Component
public class GundyuemnService extends BaseService<Yuemnjh> {
	
	/**
	 * 产线查询
	 * @date 2012-2-13
	 * @param param 参数
	 * @return List<Map<String,String>>
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectChanx(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("gundyuemn.queryShengcx",param);
	}
	
	/**
	 * 月模拟计划跟踪查询
	 * @author 王国首
	 * @date 2012-2-9
	 * @param page 分页显示
	 * @param param 查询参数
	 * @return Map<String,Object>
	 */
	public Map<String,Object> select(Pageable page,Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("gundyuemn.queryGundYuemnjh",param,page);
	}
	
	/**
	 * 工业周期查询
	 * @param param 参数
	 * @return List<Map<String,String>> 工业周期集合
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectGongyzq(Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("gundyuemn.queryGongyzq", param);
	}
	
	/**
	 * 工业周期时间范围查询
	 * @param gongyzq 工业周期号
	 * @return List<Map<String,String>> 工业周期时间范围
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectGyzqsjfw(String gongyzq)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("gundyuemn.queryGyzqfw", gongyzq);
	}
	
	/**
	 * 查询某条产线所有的月模拟错误消息
	 * @param param 参数
	 * @return String 错误消息
	 */
	public Map<String,Object> selectMessage(Pageable page,Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("gundyuemn.queryMessage",param,page);
	}
	
	/**
	 * 查询周期模拟否需要覆盖
	 * @author 王国首
	 * @date 2012-7-13
	 * @param param
	 * @return 当天生产线排产状态为Y的个数 pcCount
	 */
	public String queryShiFFG(Map<String,String> param){
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("gundyuemn.queryShiFFG", param);
	}
	
	
	/**
	 * 使用滚动周期模拟的数据覆盖周期模拟的数据
	 * @param param 日滚动计划号
	 * @return int 更新成功个数
	 */
	@Transactional
	public int gundCover(Map<String,String> param)throws ServiceException{
		int result = 1;
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("gundyuemn.gundCover", param);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("gundyuemn.deleteMonrgdljclb",param);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("gundyuemn.deleteLingjrxqb",param);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("gundyuemn.deleteLingjrxqhzb",param);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("gundyuemn.deleteBEICRZHHZ",param);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("gundyuemn.deleteBeicrxh",param);
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("gundyuemn.copyMonrgdljclb",param);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("gundyuemn.copyLingjrxqb",param);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("gundyuemn.copyLingjrxqhzb",param);
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("gundyuemn.copyBeicrzhhz",param);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("gundyuemn.copyBeicrxh",param);
		return result;
		
	}
	
	/**
	 * 返回sqlmap-xxx.xml配置文件中的namespace属性名
	 */
	@Override
	protected String getNamespace(){
		return "gundyuemn";
	}
	
}
