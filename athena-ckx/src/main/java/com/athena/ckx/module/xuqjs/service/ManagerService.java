package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.Manager;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 计划员分组
 * @author denggq
 * 2012-3-20
 */
@Component
public class ManagerService extends BaseService<Manager>{
	

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	
	/**
	 * 保存
	 * @param list
	 * @param username
	 * @return String
	 * @author denggq
	 * @time 2012-3-20
	 */
	@Transactional
	public String save(ArrayList<Manager> insert,ArrayList<Manager> edit, ArrayList<Manager> delete,String username) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		
		inserts(insert,username);
		edits(edit,username);
		deletes(delete,username);
		
		return "success";
	}
	
	
	/**
	 * 私有批量增加方法
	 * @author denggq
	 * @date 2012-3-20
	 * @param list,username
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Manager> insert,String username)throws ServiceException{
		for(Manager bean:insert){
			bean.setCreator(username);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertManager",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量修改方法
	 * @author denggq
	 * @date 2012-3-20
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String edits(List<Manager> edit,String username) throws ServiceException{
		for(Manager bean:edit){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateManager",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-20
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Manager> delete,String username)throws ServiceException{
		for(Manager bean:delete){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteManager",bean);
		}
		return "";
	}
	
}
