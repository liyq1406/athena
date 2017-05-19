package com.athena.ckx.module.paicfj.service;

import java.util.List;
import java.util.Date;


import com.athena.ckx.entity.paicfj.Ckx_yongh_chengys;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

import com.toft.core3.transaction.annotation.Transactional;
/**
 * 用户成品库
 * @author hj
 * @date 2012-02-21
 */
@Component
public class Ckx_yongh_chengysService extends BaseService<Ckx_yongh_chengys> {

	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 数据操作
	 * @param insert
	 * @param delete
	 * @param userID
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(List<Ckx_yongh_chengys> insert,
	   		   List<Ckx_yongh_chengys> delete,String userID) throws ServiceException{
		if(0==insert.size()&&0==delete.size()){
			return "null";
		}
		inserts(insert,userID);
		deletes(delete);
		return "success";
	}
	/**
	 * 批量数据录入
	 * @param insert
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String inserts(List<Ckx_yongh_chengys> insert,String userID)throws ServiceException{
		Date date = new Date();
		for(Ckx_yongh_chengys bean:insert){
			bean.setCreator(userID);
			bean.setCreate_time(date);
			bean.setEditor(userID);
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_yongh_chengys", bean);
		}
		return "";
	}
	/**
	 * 批量数据删除
	 * @param delete
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String deletes(List<Ckx_yongh_chengys> delete)throws ServiceException{
		for(Ckx_yongh_chengys bean:delete){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_yongh_chengys",bean);
		}
		return "";
	}
	
}
