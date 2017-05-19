package com.athena.xqjs.module.ilorder.service;

import java.util.List;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Maoxqhzsfpxh;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class MaoxqhzsfpxhService extends BaseService {

	/*
	 * 将已有的Maoxqhzpfpxh类插入到数据库中 作者：陈骏
	 */
	public void doInsert(Maoxqhzsfpxh bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzsfpxh", bean);
	}

	/*
	 * 将毛需求汇总到分配循环并关联物流路径表 作者：陈骏
	 */
	public List selectAll() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryInMaoxqhzsfpxh");
	}

	public List select() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryMaoxqhzsfpxh");
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
		int count = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteAllS");
		return count > 0;
	}
}
