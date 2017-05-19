package com.athena.xqjs.module.ilorder.service;

import java.util.List;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Maoxqhzpfpxh;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class MaoxqhzpfpxhService extends BaseService {
	/**
	 * 插入数据到毛需求汇总分配循环
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-15 参数说明：Maoxqhzpfpxh bean，毛需求汇总分配循环实体类
	 */
	public void doInsert(Maoxqhzpfpxh bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzpfpxh", bean);
	}

	/**
	 * 查询出毛需求汇总分配循view
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-15 返回毛需求汇总分配循实体类的list
	 */
	public List selectAll() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryInMaoxqhzpfpxh");
	}

	public List select() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryMaoxqhzpfpxh");
	}

	/**
	 * 删除全部
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-02-29
	 */
	@Transactional
	public boolean removeAll() {
		int count = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteAllP");
		return count > 0;
	}
}
