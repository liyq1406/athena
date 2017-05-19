package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.cangk.Xingzysts;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 行政验收提示验收项
 * @author denggq
 * @date 2012-2-6
 */
@Component
public class XingzystsService extends BaseService<Xingzysts>{

	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-2-6
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 分页查询方法
	 * @author denggq
	 * @date 2012-2-17
	 * @param bean
	 * @return Map 分页的结果
	 */
	@Transactional
	public Map<String, Object> select(Xingzysts bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryXingzysts",bean,bean);
	}
	

	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-2-6
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Xingzysts> insert,
	           ArrayList<Xingzysts> edit,
	   		   ArrayList<Xingzysts> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		
		if(0 != delete.size()){
			return "noDelete";
		}
		inserts(insert,userID);
		edits(edit,userID);
//		deletes(delete,userID);
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-2-6
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Xingzysts> insert,String userID)throws ServiceException{
		for(Xingzysts bean:insert){
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertXingzysts",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-2-6
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(List<Xingzysts> edit,String userID) throws ServiceException{
		for(Xingzysts bean:edit){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateXingzysts",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-2-6
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Xingzysts> delete,String userID)throws ServiceException{
		for(Xingzysts bean:delete){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.removeXingzysts",bean);
		}
		return "";
	}
	
	/**
	 * 卸货站台失效
	 * @author denggq
	 * @param bean
	 * @date 2012-2-22
	 * @return String
	 */
	@Transactional
	public String doDelete(Xingzysts bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXingzysts", bean);
		return bean.getYansxbh();
	}
}