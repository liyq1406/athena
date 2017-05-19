package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.cangk.Fahzt;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 发货站台
 * @author denggq
 * @date 2012-1-16
 */
@Component
public class FahztService extends BaseService<Fahzt>{

	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-1-16
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Fahzt> insert,
	           ArrayList<Fahzt> edit,
	   		   ArrayList<Fahzt> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		
//		deletes(delete,userID);
		inserts(insert,userID);
		edits(edit,userID);
		
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Fahzt> insert,String userID)throws ServiceException{
		for(Fahzt bean:insert){
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertFahzt",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(List<Fahzt> edit,String userID) throws ServiceException{
		for(Fahzt bean:edit){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateFahzt",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Fahzt> delete,String userID)throws ServiceException{
		for(Fahzt bean:delete){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteFahzt",bean);
		}
		return "";
	}
	
	
	/**
	 * 失效
	 * @author denggq
	 * @date 2012-2-22
	 * @return 主键
	 */
	@Transactional
	public String doDelete(Fahzt bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteFahzt", bean);
		return bean.getFahztbh();
	}
}