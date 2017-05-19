package com.athena.xqjs.module.kdorder.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.entity.kdorder.Kdmaoxq;
import com.athena.xqjs.entity.kdorder.Kdmxqhz;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:KD毛需求汇总类
 * </p>
 * <p>
 * Description:KD毛需求汇总类
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
 * @date 2012-1-18
 */

@Component
public class KdmxqhzService extends BaseService {

	/**
	 * 查询全部，返回List
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date: 2012-1-18
	 * @param：用户中心，制造路线，需求班次，日期，s0周序
	 */
	public List<Kdmxqhz> queryListKdmxqhzForConvert(Map map) {
		CommonFun.mapPrint(map, "kd件行列转换queryListKdmxqhzForConvert方法参数map");
		CommonFun.logger.debug("kd件行列转换queryListKdmxqhzForConvert方法的sql语句为：kdorder.kdmxhzLineConvertRow");
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.kdmxhzLineConvertRow", map);
	}

	/**
	 * 查询全部，返回List,针对行列转换
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date: 2012-1-18
	 */
	public List<Kdmxqhz> queryListKdmxqhzFor() {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryAllKdmxqhz");
	}

	/**
	 * 循环插入操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date: 2012-1-18
	 * @参数：list集合，实体Kdmxqhz
	 */
	@Transactional
	public void listInsert(List all, Object bean) {
		for (Object obj : all) {
			this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.insertKdmxqhz", obj);
		}
	}

	/**
	 * 无条件删除全部
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-1-31
	 */
	@Transactional
	public boolean doAllDelete() {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.deleteAllKdmxqhz");
		return count > 0;
	}

	/**
	 * 查询资源快照表，统计线边库存
	 **/
	public Ziykzb queryAllZiykzb(Map<String, String> map) {
		Ziykzb bean = new Ziykzb();
		bean = (Ziykzb) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kdorder.queryAllZiykzb", map);
		return bean;
	}

	/**
	 * @方法：查询毛需求和零件消耗点关联后消耗比例为空的数据，插入到异常报警表中
	 * @参数：用户中心，制造路线，需求班次
	 **/
	public List<Kdmaoxq> queryNullinsert(Map<String, String> map) {
		CommonFun.mapPrint(map, "kd件产线消耗点校验参数map");
		CommonFun.logger.debug("kd件产线消耗点校验的sql语句为：kdorder.insertYcbj");
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.insertYcbj", map);
	}

}