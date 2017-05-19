package com.athena.xqjs.module.anxorder.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * Title:按需中间表service
 * 
 * @author 李明
 * @version v1.0
 * @date 2012-03-27
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class AnxjscszjbService extends BaseService {

	/**
	 * 插入操作 参数：实体bean
	 * */
	@Transactional
	public boolean doInsert(Anxjscszjb bean) {
		// 返回插入条数
		int count = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.insertAnxjscszjb", bean);
		// 返回是否成功
		return count > 0;
	}

	/**
	 * 清除中间表 参数：实体bean
	 * */
	@Transactional
	public boolean doRemove() {
		// 返回删除条数
		int count = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.deleteAllMessages");
		// 返回是否成功
		return count > 0;
	}

	/**
	 * 查询全部用户中心，零件编号，消耗点信息
	 * */

	public List<Anxjscszjb> queryAllAnxjscszjb(Map map) {
		// List<Anxjscszjb>
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryAnxjscszjbForAnx",map);
	}

	/**
	 * 查询同一零件用户中心消耗点信息
	 * */
	public List<Anxjscszjb> queryAllAnxjscszjbList(Map<String, String> map) {
		// List<Anxjscszjb>
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryListAnxjscszjb", map);
	}

	/**
	 * 查询全部信息(测试用)
	 * */
	public List<Anxjscszjb> queryAnxjscszjbForTest() {
		// List<Anxjscszjb>
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryListAllAnxjscszjb");
	}

}