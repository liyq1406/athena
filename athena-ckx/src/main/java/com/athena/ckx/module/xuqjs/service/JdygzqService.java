package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;

import com.athena.ckx.entity.xuqjs.Jdygzq;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 既定-预告-周期Service
 * @author qizhongtao
 * @date 2012-4-16
 */
@Component
public class JdygzqService extends BaseService<Jdygzq>{
	/**
	 * 获取命名空间
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 批量数据保存
	 * @author qizhongtao
	 * @date 2012-4-16
	 * @param insert,edit,delete,userName
	 * @return String
	 * */
	@Transactional
	public String save(ArrayList<Jdygzq> insert,ArrayList<Jdygzq> edit,ArrayList<Jdygzq> delete,String userName)throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}else{
			inserts(insert,userName);
			edits(edit,userName);
			deletes(delete,userName);
		}
		return "success";
	}
	/**
	 * 批量insert
	 * @author qizhongtao
	 * @date 2012-4-16
	 * @param insert,userName
	 * @return ""
	 * */
	@Transactional
	public String inserts(ArrayList<Jdygzq> insert,String userName)throws ServiceException{
		for (Jdygzq bean : insert) {
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertJdygzq", bean);
		}
		return "";
	}
	/**
	 * 批量edit
	 * @author qizhongtao
	 * @date 2012-4-16
	 * @param edit,userName
	 * @return ""
	 * */
	@Transactional
	public String edits(ArrayList<Jdygzq> edit,String userName)throws ServiceException{
		for (Jdygzq bean : edit) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateJdygzq", bean);
		}
		return "";
	}
	/**
	 * 批量delete
	 * @author qizhongtao
	 * @date 2012-4-16
	 * @param delete,userName
	 * @return ""
	 * */
	@Transactional
	public String deletes(ArrayList<Jdygzq> delete,String userName)throws ServiceException{
		for (Jdygzq bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteJdygzq", bean);
		}
		return "";
	}
}
