package com.athena.xqjs.module.ilorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Maoxqhzj;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:毛需求汇总J模式类
 * </p>
 * <p>
 * Description:毛需求汇总J模式类
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
 * @date 2011-12-13
 */
@Component
public class MaoxqhzjService extends BaseService {

	/**
	 * 插入操作，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	// @Transactional
	// public boolean doInsert(Maoxqhzj bean) {
	// int count =
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzj",
	// bean);
	// return count>0;
	// }

	/**
	 * 带条件删除全部信息
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	// @Transactional
	// public String doDelete(Maoxqhzj bean) {
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteMaoxqhzj",
	// bean);
	// return bean.getId();
	// }

	/**
	 * 无条件删除全部信息
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	@Transactional
	public boolean doAllDelete() {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteAllMaoxqhzj");
		return true;
	}

	/**
	 * 修改操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	// @Transactional
	// public String doUpdate(Maoxqhzj bean) {
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateMaoxqhzj",
	// bean);
	// return bean.getId();
	// }

	/**
	 * 查询全部信息，返回MAP集合
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	// public Map<String, Object> select(Pageable page) {
	// return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryMaoxqhzj", page);
	// }

	public List select() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryMaoxqhzj");
	}

	/**
	 * 查询全部信息，返回list集合
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 * @param:String riq,String usercenter,String banc,String zhizlx
	 */
	@SuppressWarnings("unchecked")
	public List<Maoxqhzj> queryMaoxqhzj(Map map) {
		CommonFun.logger.debug("Maoxqhzj方法行列转换查询sql语句为：ilorder.queryAllMaoxqhzj");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryAllMaoxqhzj", map);
	}
	///wuyichao  2014-05-17
	public List<Maoxqhzj> queryMaoxqhzjByCenter(Map map) {
		CommonFun.logger.debug("Maoxqhzj方法行列转换查询sql语句为：ilorder.queryAllMaoxqhzj");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryAllMaoxqhzjByCenter", map);
	}
	///wuyichao  2014-05-17
	@Transactional
	public void deleteMaoxqhzjById(String id)
	{
		if(StringUtils.isNotBlank(id))
		{
			Map<String, String> param = new HashMap<String, String>();
			param.put("id", id);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteMaoxqhzjById",param);
		}
	}
	
	
	/**
	 * 插入操作，无返回
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	// @Transactional
	// public void insertMaoxqhzj(Maoxqhzj bean) {
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzj",
	// bean);
	// }

}