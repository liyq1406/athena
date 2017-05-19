package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Kuw;
import com.athena.ckx.entity.cangk.Kuwdj;
import com.athena.ckx.entity.cangk.KuwdjBaoz;
import com.athena.ckx.entity.xuqjs.Baoz;
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
 * 库位等级与包装
 * @author denggq
 * @date 2012-1-17
 */
@Component
public class KuwdjBaozService extends BaseService<KuwdjBaoz>{
	
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
	public Map<String, Object> select(KuwdjBaoz bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryKuwdjBaoz",bean,bean);
	}
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<KuwdjBaoz> insert,
	           ArrayList<KuwdjBaoz> edit,
	   		   ArrayList<KuwdjBaoz> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userID);
		edits(edit,userID);
		deletes(delete,userID);
		return "success";
	}
	
	/**
	 * 被动调用批量保存方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String saves(Kuwdj kuwdj,ArrayList<KuwdjBaoz> insert,
	           ArrayList<KuwdjBaoz> edit,
	   		   ArrayList<KuwdjBaoz> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		insert(insert,userID,kuwdj);
		edit(edit,userID,kuwdj);
		delete(delete,userID,kuwdj);
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
	public String inserts(List<KuwdjBaoz> insert,String userID)throws ServiceException{
		for(KuwdjBaoz bean:insert){
			//仓库编号是否存在
			Cangk ck = new Cangk();
			ck.setUsercenter(bean.getUsercenter());
			ck.setCangkbh(bean.getCangkbh());
			ck.setBiaos("1");
			String mes = GetMessageByKey.getMessage("cangkubh")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountCangk", ck)){
				throw new ServiceException(mes); 
			}
//			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_cangk where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("cangkubh")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist"));
			
			//库位等级编号是否存在
			Kuwdj kuw = new Kuwdj();
			kuw.setUsercenter(bean.getUsercenter());
			kuw.setCangkbh(bean.getCangkbh());
			kuw.setKuwdjbh(bean.getKuwdjbh());
			kuw.setBiaos("1");
			String mse = GetMessageByKey.getMessage("kuweidjbh")+bean.getKuwdjbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountKuwdj", kuw)){
				throw new ServiceException(mse); 
			}
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_kuwdj where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getCangkbh()+"' and kuwdjbh = '"+bean.getKuwdjbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("kuweidjbh")+bean.getKuwdjbh()+GetMessageByKey.getMessage("notexist"));
			
			//包装类型是否存在
			Baoz baoz = new Baoz();
			baoz.setBaozlx(bean.getBaozlx());
			baoz.setBiaos("1");
			String mse1 = GetMessageByKey.getMessage("baozhuanglx")+bean.getBaozlx()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountBaoz", baoz)){
				throw new ServiceException(mse1); 
			}
//			String sql3 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where baozlx = '"+bean.getBaozlx()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql3, GetMessageByKey.getMessage("baozhuanglx")+bean.getBaozlx()+GetMessageByKey.getMessage("notexist"));
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertKuwdjBaoz",bean);
		}
		return "";
	}
	
	/**
	 * 被动调用私有批量insert方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String insert(List<KuwdjBaoz> insert,String userID,Kuwdj kuwdj)throws ServiceException{
		for(KuwdjBaoz bean:insert){
			//仓库编号是否存在
			Cangk ck = new Cangk();
			ck.setUsercenter(bean.getUsercenter());
			ck.setCangkbh(bean.getCangkbh());
			ck.setBiaos("1");
			String mes = GetMessageByKey.getMessage("cangkubh")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountCangk", ck)){
				throw new ServiceException(mes); 
			}
//			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_cangk where usercenter = '"+kuwdj.getUsercenter()+"' and cangkbh = '"+kuwdj.getCangkbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("cangkubh")+kuwdj.getCangkbh()+GetMessageByKey.getMessage("notexist"));
			
			//库位等级编号是否存在
			Kuwdj kuw = new Kuwdj();
			kuw.setUsercenter(bean.getUsercenter());
			kuw.setCangkbh(bean.getCangkbh());
			kuw.setKuwdjbh(bean.getKuwdjbh());
			kuw.setBiaos("1");
			String mse = GetMessageByKey.getMessage("kuweidjbh")+bean.getKuwdjbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountKuwdj", kuw)){
				throw new ServiceException(mse); 
			}
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_kuwdj where usercenter = '"+kuwdj.getUsercenter()+"' and cangkbh = '"+kuwdj.getCangkbh()+"' and kuwdjbh = '"+kuwdj.getKuwdjbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("kuweidjbh")+kuwdj.getKuwdjbh()+GetMessageByKey.getMessage("notexist"));
			
			//包装类型是否存在
			Baoz baoz = new Baoz();
			baoz.setBaozlx(bean.getBaozlx());
			baoz.setBiaos("1");
			String mse1 = GetMessageByKey.getMessage("baozhuanglx")+bean.getBaozlx()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountBaoz", baoz)){
				throw new ServiceException(mse1); 
			}
//			String sql3 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where baozlx = '"+bean.getBaozlx()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql3, GetMessageByKey.getMessage("baozhuanglx")+bean.getBaozlx()+GetMessageByKey.getMessage("notexist"));
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			bean.setCangkbh(kuwdj.getCangkbh());
			bean.setKuwdjbh(kuwdj.getKuwdjbh());
			bean.setUsercenter(kuwdj.getUsercenter());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertKuwdjBaoz",bean);
		}
		return "";
	}
	
	/**
	 * 被动调用私有批量update方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edit(List<KuwdjBaoz> edit,String userID,Kuwdj kuwdj) throws ServiceException{
		for(KuwdjBaoz bean:edit){
			bean.setUsercenter(kuwdj.getUsercenter());
			bean.setCangkbh(kuwdj.getCangkbh());
			bean.setKuwdjbh(kuwdj.getKuwdjbh());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwdjBaoz",bean);
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
	public String edits(List<KuwdjBaoz> edit,String userID) throws ServiceException{
		for(KuwdjBaoz bean:edit){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwdjBaoz",bean);
		}
		return "";
	}
	
	/**
	 * 被动调用私有批量删除方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String delete(List<KuwdjBaoz> delete,String userID,Kuwdj kuwdj)throws ServiceException{
		for(KuwdjBaoz bean:delete){
			bean.setUsercenter(kuwdj.getUsercenter());
			bean.setCangkbh(kuwdj.getCangkbh());
			bean.setKuwdjbh(kuwdj.getKuwdjbh());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteKuwdjBaoz",bean);
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
	public String deletes(List<KuwdjBaoz> delete,String userID)throws ServiceException{
		for(KuwdjBaoz bean:delete){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteKuwdjBaoz",bean);
		}
		return "";
	}
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(KuwdjBaoz bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryKuwdjBaoz",bean);
	}
	
}
