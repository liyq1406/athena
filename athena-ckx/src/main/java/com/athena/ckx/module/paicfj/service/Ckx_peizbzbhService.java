package com.athena.ckx.module.paicfj.service;


import java.util.Date;
import java.util.List;

import com.athena.ckx.entity.paicfj.Ckx_peizbz;
import com.athena.ckx.entity.paicfj.Ckx_peizbzbh;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

/**
 * 配载包装包含
 * @author hj
 * @Date 2012-02-21
 */
@Component
public class Ckx_peizbzbhService extends BaseService<Ckx_peizbzbh> {

	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 批量数据录入
	 * @author hj
	 * @Date 2012-02-21
	 * @param insert
	 * @param peizbz
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	public String inserts(List<Ckx_peizbzbh> insert,Ckx_peizbz peizbz,String userID)  throws ServiceException{
		Date date = new Date();
		for (Ckx_peizbzbh bean : insert) {
				bean.setBaozzbh(peizbz.getBaozzbh());
				bean.setCreator(userID);
				bean.setCreate_time(date);
				bean.setEditor(userID);
				bean.setEdit_time(date);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_peizbzbh",bean);
			
		}
		return "";
	}
	/**
	 * 批量数据删除（物理删除）
	 * @author hj
	 * @Date 2012-02-21
	 * @param delete
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	public String removes(List<Ckx_peizbzbh> delete,String userID) throws ServiceException{
		for (Ckx_peizbzbh bean : delete) {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_peizbzbh", bean);
		}
		return "success";
	}
	
}
