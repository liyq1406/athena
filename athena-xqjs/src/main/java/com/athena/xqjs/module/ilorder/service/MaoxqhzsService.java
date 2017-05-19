package com.athena.xqjs.module.ilorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Maoxqhzs;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:毛需求汇总S模式类
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
@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class MaoxqhzsService extends BaseService {

	/**
	 * 插入操作，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	// @Transactional
	// public boolean doInsert(Maoxqhzs bean) {
	// int count =
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzs",
	// bean);
	// return count>0;
	// }

	/**
	 * 有条件删除全部信息
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	// @Transactional
	// public String doDelete(Maoxqhzs bean) {
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteMaoxqhzs",
	// bean) ;
	// return bean.getId();
	// }
	//

	/**
	 * 无条件删除全部信息
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	@Transactional
	public boolean doAllDelete() {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteAllMaoxqhzs");
		return count > 0;
	}

	@Transactional
	public void deleteMaoxqhzsById(String id)
	{
		if(StringUtils.isNotBlank(id))
		{
			Map<String, String> param = new HashMap<String, String>();
			param.put("id", id);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteMaoxqhzsById",param);
		}
	}
	
	
	/**
	 * 更新信息
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	// @Transactional
	// public String doUpdate(Maoxqhzs bean) {
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateMaoxqhzs",
	// bean);
	// return bean.getId();
	// }

	/**
	 * 查询全部分页,返回map
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：分页
	 */
	// public Map<String, Object> select(Pageable page) {
	// return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryMaoxqhzs", page);
	// }

	public List selectAll() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryMaoxqhzs");
	}

	/**
	 * 查询全部,返回list
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：String riq,String nianzx,String usercenter,String
	 *       banc,String zhizlx
	 */

	public List<Maoxqhzs> queryMaoxqhzs(Map<String, String> map) {
		CommonFun.logger.debug("Maoxqhzs方法行列转换查询sql语句为：ilorder.queryAllMaoxqhzs");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryAllMaoxqhzs", map);
	}

	/**
	 * 插入操作，无返回
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	// @Transactional
	// public void insertMaoxqhzs(Maoxqhzs bean) {
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzs",
	// bean);
	// }

}