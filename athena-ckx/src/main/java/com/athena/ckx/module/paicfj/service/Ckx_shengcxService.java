package com.athena.ckx.module.paicfj.service;


import java.util.Date;

import com.athena.ckx.entity.paicfj.Ckx_shengcx;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

import com.toft.core3.transaction.annotation.Transactional;

/**
 * 生产线
 * 
 * @author hj
 * @Date 2012-02-21
 */
@Component
public class Ckx_shengcxService extends BaseService<Ckx_shengcx> {
	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 数据操作
	 * 
	 * @author hj
	 * @Date 2012-02-21
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(Ckx_shengcx bean, Integer operate, String userID)
			throws ServiceException {
		if (1 == operate) {
			inserts(bean, userID);
		} else {
			edits(bean, userID);
		}
		return "success";
	}

	/**
	 * 私有insert方法
	 * 
	 * @author hj
	 * @date 2011-12-30
	 * @param insert
	 * @param userID
	 * @return ""
	 */
	private String inserts(Ckx_shengcx bean, String userID)	throws ServiceException {
		bean.setCreator(userID);
		bean.setCreate_time(new Date());
		bean.setEditor(userID);
		bean.setEdit_time(bean.getCreate_time());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_shengcx", bean);
		return "";
	}

	/**
	 * 私有update方法
	 * 
	 * @author hj
	 * @date 2011-12-30
	 * @param edit
	 * @param userID
	 * @return ""
	 */
	private String edits(Ckx_shengcx bean, String userID)throws ServiceException {
		bean.setEditor(userID);
		bean.setEdit_time(new Date());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_shengcx", bean);
		return "";
	}

	/**
	 * 私有删除方法（逻辑删除）
	 * 
	 * @author hj
	 * @date 2011-12-30
	 * @param delete
	 * @param userID
	 * @return ""
	 */
	@Transactional
	public String remove(Ckx_shengcx bean, String userID)throws ServiceException {
		bean.setEditor(userID);
		bean.setEdit_time(new Date());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_shengcx", bean);
		return "success";
	}

}
