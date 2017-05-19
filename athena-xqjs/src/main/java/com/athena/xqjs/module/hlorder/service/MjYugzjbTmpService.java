package com.athena.xqjs.module.hlorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.hlorder.YugzjbTmp;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:预告中间表类
 * </p>
 * <p>
 * Description:预告中间表类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李明
 * @version v1.0
 * @date 2012-01-03
 */
@Component
public class MjYugzjbTmpService extends BaseService {
	/**
	 * @插入操作
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 * @参数说明：Yugzjb bean 预告中间表实体
	 */
	public String doInsert(YugzjbTmp bean) {
		bean.setId(getUUID());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.insertYugzjbTmp", bean);
		return bean.getId();
	}

	/**
	 * @删除操作
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-01-03
	 */
	 public void doDeleteAll(String usercenter) {
		 Map<String,String>map = new HashMap<String,String>();
		 map.put("usercenter", usercenter);
		 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.mjDeleteYugzjbTmp",map);
	 }


	/**
	 * @查询全部，返回list插入到明细
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<YugzjbTmp> queryAllYugzjb() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryYugzjbTmp");
	}

	/**
	 * @查询全部，返回list，插入到汇总表
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<YugzjbTmp> queryYugzjbList() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryYugzjbTmpList");
	}
	/**
	 * @行列转换，得到订单零件类集合（周期模式）
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<Dingdlj> colRowYugzjbPP(Map map){
		CommonFun.mapPrint(map, "pp模式预告中间表行列转换参数map");
		CommonFun.logger.debug("pp模式预告中间表行列转换colRowYugzjbPP方法的sql语句为：hlorder.queryAllYugzjbTmpPP");
	return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.queryAllYugzjbTmpPP",map);
	}
	/**
	 * @行列转换，得到订单零件类集合（周模式）
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<Dingdlj> colRowYugzjbPS(Map map){
		CommonFun.mapPrint(map, "ps模式预告中间表行列转换colRowYugzjbPS方法的参数map");
		CommonFun.logger.debug("ps模式预告中间表行列转换colRowYugzjbPS方法的sql语句为：hlorder.queryAllYugzjbTmpPS");
	return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.queryAllYugzjbTmpPS",map);
	}
	public List<Dingdlj> colRowYugzjbPS( ){
		return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.queryAllYugzjbTmpPSNew");
	}
	/**
	 * @行列转换，得到订单零件类集合（日模式）
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<Dingdlj> colRowYugzjbJ(Map map){
		CommonFun.mapPrint(map, "ps模式预告中间表行列转换colRowYugzjbPJ方法的参数map");
		CommonFun.logger.debug("ps模式预告中间表行列转换colRowYugzjbPJ方法的sql语句为：hlorder.queryAllYugzjbTmpJ");
		return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.queryAllYugzjbTmpJ",map);
		}
	
	public List<Dingdlj> colRowYugzjbJ(){
		
		return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.queryAllYugzjbTmpJNew");
		}
	
	/**
	 * @得到预告中间表中的日模式非p预告的所有日期
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<String> selectRq(Map map){
		return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.queryAllRQ",map);
		}
}