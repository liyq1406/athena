package com.athena.xqjs.module.ilorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Maoxqhzp;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:毛需求汇总P模式类
 * </p>
 * <p>
 * Description:毛需求汇总P模式类
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
public class MaoxqhzpService extends BaseService {

	/**
	 * 插入操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	// @Transactional
	// public boolean doInsert(Maoxqhzp bean) {
	// int count =
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzp",
	// bean);
	// return count>0;
	// }

	/**
	 * 根据条件删除
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	// @Transactional
	// public boolean doDelete(Maoxqhzp bean) {
	// int count =
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteMaoxqhzp",
	// bean);
	// return count>0 ;
	// }

	/**
	 * 无条件删除全部
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	@Transactional
	public boolean doAllDelete() {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteAllMaoxqhzp");
		return count > 0;
	}

	@Transactional
	public void deleteMaoxqhzpById(String id)
	{
		if(StringUtils.isNotBlank(id))
		{
			Map<String, String> param = new HashMap<String, String>();
			param.put("id", id);											
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteMaoxqhzpById",param);
		}
	}
	
	
	/**
	 * 更新操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	// @Transactional
	// public String doUpdate(Maoxqhzp bean) {
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateMaoxqhzp",
	// bean);
	// return bean.getId();
	// }

	/**
	 * 查询全部
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	public List<Maoxqhzp> select() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryMaoxqhzp");
	}

	/**
	 * 行列转换查询，返回list
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	public List<Maoxqhzp> queryAllMaoxqhzp(String nianzq, String usercenter, String banc, String zhizlx) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("nianzq", nianzq);
		map.put("usercenter", usercenter);
		map.put("xuqbc", banc);
		map.put("zhizlx", zhizlx);
		CommonFun.mapPrint(map, "行列转换查询参数map");
		CommonFun.logger.debug("Maoxqhzp方法行列转换查询sql语句为：ilorder.queryAllMaoxqhzp");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryAllMaoxqhzp", map);
	}

	/**
	 * 行列转换查询，返回list，将list插入到表中
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	// @Transactional
	// public void insertMaoxq(Maoxqhzp bean){
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzp",
	// bean);
	// }

}