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
package com.athena.xqjs.module.ilorder.service;

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
public class MaoxqService extends BaseService {
	/**
	 * 向毛需求汇总P表中插入一条信息
	 * 
	 * @author
	 * @date 2011-12-13
	 * @param bean
	 *            插入的信息
	 * @return ID 返回删除信息的ID
	 */
//	public String doInsert(Maoxqhzp bean) {
//		bean.setId(getUUID());
//		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzp", bean);
//		return bean.getId();
//	}

	/**
	 * 根据条件删除毛需求汇总P表中相关的记录
	 * 
	 * @author
	 * @date 2011-12-13
	 * @param bean
	 *            删除的条件
	 * @return ID 返回删除信息的ID
	 */
//	public String doDelete(Maoxqhzp bean) {
//		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteMaoxqhzp", bean);
//		return bean.getId();
//	}

	/**
	 * 删除毛需求汇总P表中的记录
	 * 
	 * @author
	 * @date 2011-12-13
	 * @return boolean 返回删除成功与否
	 */
//	public boolean doAllDelete() {
//		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteAllMaoxqhzp");
//		return true;
//	}

	/**
	 * 修改毛需求汇总P表的信息
	 * 
	 * @author
	 * @date 2011-12-13
	 * @param bean
	 *            修改的信息
	 * @return ID 返回修改信息的ID
	 */
//	public String doUpdate(Maoxqhzp bean) {
//		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateMaoxqhzp", bean);
//		return bean.getId();
//	}

	/**
	 * 查询毛需求汇总P表
	 * 
	 * @author
	 * @date 2011-12-13
	 * @param page
	 *            分页显示
	 * @return Map 检索结果
	 */
//	public Map<String, Object> select(Pageable page) {
//		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryMaoxqhzp", page);
//	}

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
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryMaoxq", param, page);
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
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryMaoxqByXqlx", param, page);
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
	 * 查询需求来源
	 * 
	 * @author 李智
	 * @date 2012-4-8
	 * @param param
	 *            查询条件
	 * @return List<Maoxq> 检索结果
	 */
//	public List<Xitcsdy> queryMaoxqXqly(Map<String, String> param) {
//		param.put("zidmc", Const.MAOXQ_XUQLY_DKS_NAME);
//		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryZid", param);
//	}
	
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
		return (Maoxq)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryOneMaoxq", param);
	}
	
	public List queryMaoxqList(String zidlx,String xuqly) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("zidlx", zidlx);
		map.put("xuqly", xuqly);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryMaoxqList", map);
	}
	
//	/**
//	 * 在系统参数定义表中获取计算类型
//	 * 
//	 * @author 李智
//	 * @date 2012-2-9
//	 * @param param
//	 *            查询条件
//	 * @return Map 检索结果
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Xitcsdy> queryjislx(Map<String, String> param) {
//		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryZid", param);
//	}
}