package com.athena.xqjs.module.ilorder.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Yugzjb;
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
public class YugzjbService extends BaseService {
	/**
	 * @插入操作
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 * @参数说明：Yugzjb bean 预告中间表实体
	 */
	public String doInsert(Yugzjb bean) {
		bean.setId(getUUID());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertYugzjb", bean);
		return bean.getId();
	}

	/**
	 * @删除操作
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-01-03
	 */
	 public void doDeleteAll() {
	 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteYugzjb");
	 }

	/**
	 * @更新操作，返回id
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 * @参数说明：Yugzjb bean 预告中间表实体
	 */
	// public String doUpdate(Yugzjb bean) {
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateYugzjb",
	// bean);
	// return bean.getId();
	// }

	/**
	 * @查询全部分页，返回map
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 * @参数说明：page
	 */
	// public Map<String, Object> select(Pageable page) {
	// return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryYugzjb", page);
	// }

	/**
	 * @查询全部，返回list插入到明细
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<Yugzjb> queryAllYugzjb() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryYugzjb");
	}

	/**
	 * @查询全部，返回list，插入到汇总表
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<Yugzjb> queryYugzjbList() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryYugzjbList");
	}
	/**
	 * @行列转换，得到订单零件类集合（周期模式）
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<Dingdlj> colRowYugzjbPP(Map map){
		CommonFun.mapPrint(map, "pp模式预告中间表行列转换参数map");
		CommonFun.logger.debug("pp模式预告中间表行列转换colRowYugzjbPP方法的sql语句为：ilorder.queryAllYugzjbPP");
	return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.queryAllYugzjbPP",map);
	}
	/**
	 * @行列转换，得到订单零件类集合（周模式）
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<Dingdlj> colRowYugzjbPS(Map map){
		CommonFun.mapPrint(map, "ps模式预告中间表行列转换colRowYugzjbPS方法的参数map");
		CommonFun.logger.debug("ps模式预告中间表行列转换colRowYugzjbPS方法的sql语句为：ilorder.queryAllYugzjbPS");
	return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.queryAllYugzjbPS",map);
	}
	public List<Dingdlj> colRowYugzjbPS( ){
		return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.queryAllYugzjbPSNew");
	}
	/**
	 * @行列转换，得到订单零件类集合（日模式）
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<Dingdlj> colRowYugzjbJ(Map map){
		CommonFun.mapPrint(map, "ps模式预告中间表行列转换colRowYugzjbPJ方法的参数map");
		CommonFun.logger.debug("ps模式预告中间表行列转换colRowYugzjbPJ方法的sql语句为：ilorder.queryAllYugzjbJ");
		return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.queryAllYugzjbJ",map);
		}
	
	public List<Dingdlj> colRowYugzjbJ(){
		
		return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.queryAllYugzjbJNew");
		}
	
	/**
	 * @得到预告中间表中的日模式非p预告的所有日期
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-01-03
	 */
	public List<String> selectRq(Map map){
		return	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.queryAllRQ",map);
		}
}