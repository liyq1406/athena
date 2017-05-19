/**
 * 代码声明
 */
package com.athena.xqjs.module.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

@SuppressWarnings("rawtypes")
@Component
public class GongysService extends BaseService {
	/**
	 * 插入
	 */
	public String doInsert(Gongys bean) {
		bean.setUsercenter(getUUID());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("CKX.insertGongys", bean);
		return bean.getUsercenter();
	}

	/**
	 * 删除
	 */
	public String doDelete(Gongys bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("CKX.deleteGongys", bean);
		return bean.getUsercenter();
	}

	/**
	 * 更新
	 */
	public String doUpdate(Gongys bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("CKX.updateGongys", bean);
		return bean.getUsercenter();
	}
	
	/**
	 * 查询实体集合
	 * 
	 * */
	public boolean queryAllGys(String gongysdm, String usercenter) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("gcbh", gongysdm);
		map.put("usercenter", usercenter);
		map.put("leix", Const.GYSLX);
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("CKX.queryAllGongys", map).size()>0 ;
	}
	
	/**
	 * 查询实体
	 * 
	 * */
	public Gongys queryObject(String gongysdm, String usercenter) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("gcbh", gongysdm);
		map.put("usercenter", usercenter);
		Gongys bean =  (Gongys) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("CKX.queryGongys", map);
		map.clear();
		if (bean == null) {
			return new Gongys();
		}
		return bean;
	}
	
	/**
	 * 查询实体
	 * 
	 * */
	public Gongys queryGongys(Map map) {
		return (Gongys) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("CKX.queryGongys", map);
	}

	/**
	 * 根据订单类型查询实体
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Gongys> queryObjectBylx(String dingdlx, String usercenter) {
		Map<String, String> map = new HashMap<String, String>();
		String gonghlx = "";
		if (dingdlx.equalsIgnoreCase("1")) {
			gonghlx = "gonghlx='97X'";
		} else if (dingdlx.equalsIgnoreCase("2")) {
			gonghlx = "gonghlx='97D'";
		} else {
			gonghlx = "(gonghlx='97D' or gonghlx='97X')";
		}
		map.put("gonghlx", gonghlx);
		map.put("usercenter", usercenter);
		
		return (List<Gongys>) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("CKX.queryGongysByLx", map);
	}
	/**
	 * 根据订单类型查询实体
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Gongys> queryDingdBylx(String dingdlx, String usercenter) {
		Map<String, String> map = new HashMap<String, String>();
		String gonghlx = "";
		if (dingdlx.equalsIgnoreCase("1")) {
			gonghlx = "gonghlx='97X'";
		} else if (dingdlx.equalsIgnoreCase("2")) {
			gonghlx = "gonghlx='97D'";
		} else {
			gonghlx = "(gonghlx='97D' or gonghlx='97X')";
		}
		map.put("gonghlx", gonghlx);
		map.put("usercenter", usercenter);
		map.put("leix", Const.GYSLX) ;
		return (List<Gongys>) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("CKX.queryGongysByLxAndWaibu", map);
	}

	/**
	 * 根据订单类型查询实体
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Gongys> queryIlObjectBylx(String usercenter) {
		Map<String, String> map = new HashMap<String, String>();
		String gonghlx = "97W";
		map.put("gonghlx", gonghlx);
		map.put("usercenter", usercenter);
		return (List<Gongys>) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("CKX.queryGongys", map);
	}

	/**
	 * 按需：查询实体，获取备货周期和发货周期
	 * 
	 * */
	public Gongys queryAnxObject(String lingjbh, String usercenter) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lingjbh", lingjbh);
		map.put("usercenter", usercenter);
		Gongys bean = (Gongys) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("CKX.queryAnxGongys", map);
		map.clear();
		return bean;
	}

	/**
	 * 查询
	 */
	public Map<String, Object> select(Pageable page) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("CKX.queryGongys", page);
	}
}