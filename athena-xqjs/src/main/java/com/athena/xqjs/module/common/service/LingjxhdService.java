package com.athena.xqjs.module.common.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.toft.core3.container.annotation.Component;

@SuppressWarnings("rawtypes")
@Component
public class LingjxhdService extends BaseService {
	
	/**
	 * 更新理论库存
	 * @param bean 库存计算初始表
	 * @return true/成功,false/失败
	 */
	public boolean updateLilkc(Lingjxhd lingjxhd){
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("common.updateLingjxhd", lingjxhd);
		return count > 0;
	}
	
	/**
	 * 更新
	 */
	public boolean doUpdate(Lingjxhd bean) {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("common.updateLingjxhd", bean);
		return count > 0;
	}

	/**
	 * @查询实体
	 * @参数：用户，零件编号，消耗点
	 * **/
	public Lingjxhd queryLingjxhdObject(Map<String, String> map) {
		Lingjxhd bean = new Lingjxhd();
		bean = (Lingjxhd) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryObject", map);
		return bean;
	}

	/**
	 * 根据用户中心和零件编号查询生产线编号
	 * 
	 * @author 李智
	 * @date 2012-3-22
	 * @param param
	 *            查询条件
	 * @return List<Lingjxhd>
	 */
	public List<Lingjxhd> queryShengcxByParam(Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryShengcxByParam", param);
	}

	/**
	 * 根据用户中心、零件编号和产线查询消耗点编号
	 * 
	 * @author 李智
	 * @date 2012-3-22
	 * @param param
	 *            查询条件
	 * @return List<Lingjxhd>
	 */
	public List<Lingjxhd> queryXiaohdByParam(Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryXiaohdByParam", param);
	}

	/**
	 * 根据用户中心、零件编号、产线和消耗点查询继承的未交付量（已发要货令总量-交付总量-终止总量）
	 * 
	 * @author 李智
	 * @date 2012-3-22
	 * @param param
	 *            查询条件
	 * @return List<Lingjxhd>
	 */
	public Lingjxhd queryWeijfzlByParam(Map<String, String> param) {
		return (Lingjxhd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryWeijfzlByParam", param);
	}
}