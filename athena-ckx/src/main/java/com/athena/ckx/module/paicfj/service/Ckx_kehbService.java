package com.athena.ckx.module.paicfj.service;

import java.util.Date;

import com.athena.ckx.entity.paicfj.Ckx_kehb;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

import com.toft.core3.transaction.annotation.Transactional;
/**
 * 客户表
 * @author hj
 * @Date 2012-02-21
 */
@Component
public class Ckx_kehbService extends BaseService<Ckx_kehb> {

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}
	@Transactional
	public String save(Ckx_kehb bean,Integer operate,String userID)throws ServiceException{
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
	 * @author hj
	 * @Date 12/02/21
	 * @param bean
	 * @param userID
	 * @return success 
	 * @throws ServiceException
	 */
	
	private String inserts(Ckx_kehb bean,String userID)throws ServiceException{
			bean.setCreator(userID);
			bean.setCreate_time(new Date());
			bean.setEditor(userID);
			bean.setEdit_time(bean.getCreate_time());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_kehb",bean);
		
		return "success";
	}
	/**
	 * 数据编辑
	 * @author hj
	 * @Date 12/02/21
	 * @param bean
	 * @param userID
	 * @return success 
	 * @throws ServiceException
	 */
	private String edits(Ckx_kehb bean,String userID)throws ServiceException{
			bean.setEditor(userID);
			bean.setEdit_time(new Date());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_kehb",bean);
		
		return "success";
	}
	/**
	 * 数据删除（逻辑删除）
	 * @author hj
	 * @Date 12/02/21
	 * @param bean
	 * @param userID
	 * @return success
	 * @throws ServiceException
	 */
	@Transactional
	public String remove(Ckx_kehb bean,String userID)throws ServiceException{
			bean.setEditor(userID);
			bean.setEdit_time(new Date());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_kehb",bean);
		
		return "success";
	}
}
