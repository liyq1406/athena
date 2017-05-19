package com.athena.ckx.module.paicfj.service;

import java.util.Date;

import com.athena.ckx.entity.paicfj.Ckx_gongys;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 供应商|承运商|运输商Service
 * 
 * @author hj
 * @Date 12/02/21
 * 
 */
@Component
public class Ckx_gongysService extends BaseService<Ckx_gongys> {

	protected String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 数据操作
	 * 
	 * @author hj
	 * @Date 12/02/21
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @param leix
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(Ckx_gongys bean, Integer operate, String userID)
			throws ServiceException {
		// 如果operate为1，则产线组进行录入操作，若为2，则产线组进行编辑操作
		if (1 == operate) {
			inserts(bean, userID);
		} else {
			edits(bean, userID);
		}
		return "success";
	}

	/**
	 * 数据录入
	 * 
	 * @author hj
	 * @Date 12/02/21
	 * @param insert
	 * @param userID
	 * @param leix
	 * @return ""
	 * @throws ServiceException
	 */
	private String inserts(Ckx_gongys bean, String userID)throws ServiceException {
		Date date = new Date();
		bean.setCreator(userID);
		bean.setCreate_time(date);
		bean.setEditor(userID);
		bean.setEdit_time(date);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_gongys", bean);
		return "";
	}

	/**
	 * 数据编辑 
	 * @author hj
	 * @Date 12/02/21
	 * @param edit
	 * @param userID
	 * @param leix
	 * @return ""
	 * @throws ServiceException
	 */
	private String edits(Ckx_gongys bean, String userID) throws ServiceException{
		bean.setEditor(userID);
		bean.setEdit_time(new Date());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_gongys", bean);
		return "";
	}

	/**
	 * 数据删除（逻辑删除）
	 * 
	 * @author hj
	 * @Date 12/02/21
	 * @param delete
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	@Transactional
	public String removes(Ckx_gongys bean, String userID)throws ServiceException {
		bean.setEditor(userID);
		bean.setEdit_time(new Date());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_gongys", bean);
		return "success";
	}
}
