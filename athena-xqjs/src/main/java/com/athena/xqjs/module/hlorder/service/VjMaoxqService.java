/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：MaoxqService
 * <p>
 * 类描述：毛需求CRUD操作
 * </p>
 * 创建人：
 * <p>
 * 创建时间：2011-12-12
 * </p>
 * @version 
 * 
 */
package com.athena.xqjs.module.hlorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:毛需求类
 * </p>
 * <p>
 * Description:毛需求类
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
public class VjMaoxqService extends BaseService {





	/**
	 * 查询所有版次的毛需求
	 * 
	 * @author Xiahui
	 * @date 2011-12-13
	 * @param page
	 * 分页显示检索结果
	 * @return Map 
	 */
	public Map<String, Object> select(Pageable page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("hlorder.queryMaoxq", param, page);
	}
	
	/**
	 * 按需求类型查询毛需求
	 * 
	 * @author 李智
	 * @date 2012-2-9
	 * @param page
	 *            分页显示
	 * @param param
	 *            查询条件
	 * @return Map 检索结果
	 */
	public Map<String, Object> queryMaoxqByXqlx(Pageable page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("hlorder.queryMaoxqByXqlx", param, page);
	}

	/**
	 * 在资源快照表中查询资源获取日期
	 * 
	 * @author 李智
	 * @date 2012-2-9
	 * @param param
	 *            查询条件
	 * @return List<Ziykzb> 检索结果
	 */
	public List<Ziykzb> queryZiykzb(Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryZiykzb", param);
	}
	

	/**
	 * 查询单挑毛需求
	 * 
	 * @author 李智
	 * @date 2012-4-8
	 * @param param
	 *            查询条件
	 * @return Maoxq 检索结果
	 */
	public Maoxq queryOneMaoxq(Map<String, String> param) {
		return (Maoxq)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("hlorder.queryOneMaoxq", param);
	}
	
	public List queryMaoxqList(String zidlx,String xuqly,String userCenter) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("zidlx", zidlx);
		map.put("xuqly", xuqly);
		map.put("usercenter", userCenter);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryMaoxqList", map);
	}

}