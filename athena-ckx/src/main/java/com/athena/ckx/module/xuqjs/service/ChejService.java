package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.Chej;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 用户中心
 * @author denggq
 * 2012-7-5
 */
@Component
public class ChejService extends BaseService<Chej>{
	
	@Inject
	private CacheManager cacheManager;//缓存

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	
	/**
	 * 保存车间
	 * @param list
	 * @param username
	 * @return String
	 * @author denggq
	 * @time 2012-3-6
	 */
	@Transactional
	public String save(ArrayList<Chej> insert,ArrayList<Chej> edit, ArrayList<Chej> delete,String username) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		deletes(delete,username);
		inserts(insert,username);
		edits(edit,username);
		cacheManager.refreshCache("queryChej");//刷新缓存车间（零件表）
		cacheManager.refreshCache("queryZuoyy");//刷新缓存作用域
		return "success";
	}
	
	
	/**
	 * 私有批量增加方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param list,username
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Chej> insert,String username)throws ServiceException{
		for(Chej bean:insert){
			bean.setCreator(username);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertChej",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量修改方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String edits(List<Chej> edit,String username) throws ServiceException{
		for(Chej bean:edit){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateChej",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-7-5
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Chej> delete,String username)throws ServiceException{
		for(Chej bean:delete){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteChej",bean);
		}
		return "";
	}
	
	
	/**
	 * 删除
	 * @param bean
	 * @return 主键
	 * @author denggq
	 * @time 2012-7-5
	 */
	public String doDelete(Chej bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteChej", bean);
		return "success";
	}
	
}
