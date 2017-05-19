package com.athena.ckx.module.paicfj.service;


import java.util.Date;
import java.util.List;

import com.athena.ckx.entity.paicfj.Ckx_chengpk;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 成品库Service
 * @author hj
 * @Date 12/02/17
 */
@Component
public class Ckx_chengpkService extends BaseService<Ckx_chengpk> {


	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 数据操作
	 * @author hj
	 * @Date 12/02/21
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(List<Ckx_chengpk> insert,
	           List<Ckx_chengpk> edit,
	           List<Ckx_chengpk> delete,String userID) throws ServiceException {
		if (0 == insert.size() && 0 == edit.size() && 0 == delete.size()) {
			return "null";
		}
		inserts(insert, userID);
		edits(edit, userID);
		removes(delete);
		return "success";

	}
	/**
	 * 私有批量数据录入方法
	 * @author hj
	 * @Date 12/02/21
	 * @param insert
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String inserts(List<Ckx_chengpk> insert,String userID) {
		Date date = new Date();
		for (Ckx_chengpk bean : insert) {
			bean.setCreator(userID);
			bean.setCreate_time(date);
			bean.setEditor(userID);
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_chengpk", bean);
		}
		return "";
	}
	/**
	 * 私有批量数据编辑方法
	 * @author hj
	 * @Date 12/02/21 
	 * @param edit
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String edits(List<Ckx_chengpk> edit,String userID) {
		Date date = new Date();
		for (Ckx_chengpk bean : edit) {
			bean.setEditor(userID);
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_chengpk", bean);
		}
		return "";
	}
	/**
	 * 私有批量删除方法（物理删除）
	 * @author hj
	 * @Date 12/02/21
	 * @param delete
	 * @param userID
	 * @return
	 * @throws ServiceException
	 */
	private String removes(List<Ckx_chengpk> delete) throws ServiceException{
		for (Ckx_chengpk bean : delete) {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_chengpk", bean);
		}
		return "";
	}
}
