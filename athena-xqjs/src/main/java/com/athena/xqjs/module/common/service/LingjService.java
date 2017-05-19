/**
 * 代码声明
 */
package com.athena.xqjs.module.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

@SuppressWarnings("rawtypes")
@Component
public class LingjService extends BaseService {
	/**
	 * 插入
	 */
	public String doInsert(Lingj bean) {
		bean.setUsercenter(getUUID());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("CKX.insertLingj", bean);
		return bean.getUsercenter();
	}

	/**
	 * 删除
	 */
	public String doDelete(Lingj bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("CKX.deleteLingj", bean);
		return bean.getUsercenter();
	}

	/**
	 * 更新
	 */
	public String doUpdate(Lingj bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("CKX.updateLingj", bean);
		return bean.getUsercenter();
	}

	/**
	 * 查询
	 */
	public Map<String, Object> select(Pageable page) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("CKX.queryLingj", page);
	}

	/**
	 * 查询 返回集合 author：陈骏
	 */
	public Lingj select(String usercenter, String lingjbh) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lingjbh", lingjbh);
		map.put("usercenter", usercenter);
		map.put("biaos", Const.SHENGXIAO);
		return (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("CKX.queryLingj", map);
	}

	/*
	 * 0013186 
	 * xss
	 * */
	public Lingj select_all(String usercenter, String lingjbh) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lingjbh", lingjbh);
		map.put("usercenter", usercenter);
		//map.put("biaos", Const.SHENGXIAO);
		return (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("CKX.queryLingj", map);
	}
	
	
	/**
	 * 查询对象，返回一个对象 author：李明 参数：零件编号，用户中心
	 */
	public Lingj queryObject(String lingjbh, String usercenter) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lingjbh", lingjbh);
		map.put("usercenter", usercenter);
		map.put("biaos", Const.ACTIVE_1);
		CommonFun.mapPrint(map, "查询零件对象queryObject方法参数map");
		CommonFun.logger.debug("查询零件对象queryObject方法的sql语句为：CKX.queryLingj");
		Lingj bean = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("CKX.queryLingj", map);
		map.clear();
		if (bean == null) {
			return new Lingj();
		}
		return bean;
	}

	/**
	 * @author wuyichao
	 * @see  按条件查询零件
	 * @param paramMap
	 * @return
	 */
	public List<Lingj> queryList(Map<String, String> paramMap)
	{
		paramMap.put("biaos", Const.SHENGXIAO);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("CKX.queryLingj", paramMap);
	}
}