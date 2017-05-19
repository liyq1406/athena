package com.athena.xqjs.module.hlorder.service;

import java.util.List;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Maoxqhzjfpxh;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class VjMaoxqhzjfpxhService extends BaseService {

	public void doInsert(Maoxqhzjfpxh bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.insertMaoxqhzjfpxh", bean);
	}

	public List<Maoxqhzjfpxh> selectAll() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryInMaoxqhzjfpxh");
	}

	public List<Maoxqhzjfpxh> select() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryMaoxqhzjfpxh");
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
		int count = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.deleteAllJ");
		return count > 0;
	}
}
