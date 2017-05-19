package com.athena.pc.module.service;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.pc.entity.BasicAdjust;
import com.athena.pc.entity.Beic;
import com.athena.pc.entity.OrderAdjust;
import com.athena.pc.entity.WBDDYGAdjust;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-7-30
 * @Time: 下午01:16:49
 * @version 1.0
 * @param <T>
 * @Description :
 */
@Component
public class AdjustService<T>  extends BaseService<Beic>{
	
	/******************查询数据***********************/
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午02:29:42
	 * @version 1.0
	 * @param params 参数  （用中心，日期）
	 * @return List<HashMap<String,String>>
	 * @Description:  得到今天的日期
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String,String>> selectDate(Map<String,String> params){
		return (List<HashMap<String, String>>) this.selectObject("adjust.selectDate", params) ;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午01:24:36
	 * @version 1.0
	 * @param params 参数
	 * @return  Map<String, Object>
	 * @Description:  返回订单集
	 */
	public Map<String, Object> selectOrder(Map<String,String> params,BasicAdjust adjusj){
		return   this.selectObject("adjust.selectOrder", params,adjusj); 
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午01:27:14
	 * @version 1.0
	 * @param params 参数
	 * @return Map<String, Object>
	 * @Description:  返回要货令接口集
	 */
	public Map<String, Object> selectYHL(Map<String,String> params,BasicAdjust adjusj){
		return   this.selectObject("adjust.selectYHL", params,adjusj); 
	}
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午01:28:55
	 * @version 1.0
	 * @param params 参数
	 * @return  Map<String, Object>
	 * @Description:   返回外部订单鱼告集
	 */
	public Map<String, Object> selectWBDDYG(Map<String,String> params,BasicAdjust adjusj){
		return    this.selectObject("adjust.selectWBDDYG", params,adjusj);
	}
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午01:23:27
	 * @version 1.0
	 * @param sqlID SQL id号
	 * @param params  参数
	 * @return  List<Object>
	 * @Description:   返回结果集
	 */
	private Map<String, Object> selectObject(String sqlID,Map<String,String> params,BasicAdjust adjusj){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages(sqlID, params, adjusj);
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午01:23:27
	 * @version 1.0
	 * @param sqlID SQL id号
	 * @param params  参数
	 * @return  List<Object>
	 * @Description:   返回结果集
	 */
	@SuppressWarnings("unchecked")
	private List<T> selectObject(String sqlID,Map<String,String> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select(sqlID, params);
	}
	
	/******************更新数据***********************/
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午01:38:05
	 * @version 1.0
	 * @param adjust PP & PJ 订单
	 * @return int
	 * @Description:  调整 PP & PJ 订单
	 */
	@Transactional
	public int updateOrder(OrderAdjust adjust){
		return this.updateObject("adjust.updateOrder", adjust) ;
	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午01:39:53
	 * @version 1.0
	 * @param adjust 要货令
	 * @return int
	 * @Description:  调整要货令接口
	 */
	@Transactional
	public int updateYHL(WBDDYGAdjust adjust){
		return this.updateObject("adjust.updateYHL", adjust) ;
	}
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午01:41:12
	 * @version 1.0 
	 * @param adjust 外部订单鱼告
	 * @return int
	 * @Description:  调整外部订单鱼告
	 */
	@Transactional
	public int updateWBDDYG(WBDDYGAdjust adjust){
		return this.updateObject("adjust.updateWBDDYG", adjust) ;
	}
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-7-30
	 * @Time: 下午01:35:16
	 * @version 1.0
	 * @param sqlID SQL id号
	 * @param params 参数
	 * @return int
	 * @Description:  更新数据
	 */
	private int updateObject(String sqlID,Object entity){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute(sqlID,entity);
		
	}
	

}
