package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Kuwdj;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 库位等级
 * @author denggq
 * @date 2012-1-17
 */
@Component
public class KuwdjService extends BaseService<Kuwdj>{
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-1-17
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
	public Map<String, Object> select(Kuwdj bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryKuwdj",bean,bean);
	}
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Kuwdj> insert,
	           ArrayList<Kuwdj> edit,
	   		   ArrayList<Kuwdj> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userID);
		edits(edit,userID);
//		deletes(delete,userID);
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Kuwdj> insert,String userID)throws ServiceException{
		for(Kuwdj bean:insert){
			//仓库编号是否存在
			Cangk ck = new Cangk();
			ck.setUsercenter(bean.getUsercenter());
			ck.setCangkbh(bean.getCangkbh());
			ck.setBiaos("1");
			String mes = GetMessageByKey.getMessage("cangkubh")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountCangk", ck)){
				throw new ServiceException(mes); 
			}
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_cangk where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("cangkubh")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist"));
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertKuwdj",bean);
		}
		return "";
	}
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(List<Kuwdj> edit,String userID) throws ServiceException{
		for(Kuwdj bean:edit){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwdj",bean);
		}
		return "";
	}
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Kuwdj> delete,String userID)throws ServiceException{
		for(Kuwdj bean:delete){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteKuwdj",bean);
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
	public String doDelete(Kuwdj bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteKuwdj", bean);
		return bean.getKuwdjbh();
	}
}
