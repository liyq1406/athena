package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Baoz;
import com.athena.ckx.entity.xuqjs.JinjjQuhYuns;
import com.athena.ckx.entity.xuqjs.QuhYuns;
import com.athena.ckx.entity.xuqjs.Tuopbzdygx;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 托盘包装对应关系
 * @author denggq
 * 2012-3-19
 */
@Component
public class TuopbzdygxService extends BaseService<Tuopbzdygx>{

	@Override
	public String getNamespace() {
		return "ts_tuopbzdygx";
	}

	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Tuopbzdygx> insert,
	           ArrayList<Tuopbzdygx> edit,
	   		   ArrayList<Tuopbzdygx> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){//无操作
			return "null";
		}
	
		String msg="";
		 msg=inserts(insert,userID);//增加
		 if(!msg.equalsIgnoreCase("success")){
			 return msg; 
		 }
		 msg=edits(edit,userID);//修改
/*		 if(!msg.equalsIgnoreCase("success")){
			 return msg; 
		 }
		 msg=deletes(delete,userID);//删除
*/		return msg;
	}
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Tuopbzdygx> delete,String userID)throws ServiceException{
		for(Tuopbzdygx bean:delete){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_tuopbzdygx.delTuopbzdygx",bean);//失效数据库
		}
		return "success";
	}
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public String inserts(List<Tuopbzdygx> insert,String userID)throws ServiceException{
		for(Tuopbzdygx bean:insert){
			GetPostOnly.checkqhqx(bean.getUsercenter());
			Baoz baoz=new Baoz();
			baoz.setBiaos("1");
			baoz.setBaozlx(bean.getTuopxh());
			//包装参考系
			Baoz bz1=(Baoz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryBaoz", baoz);
			if(bz1==null){
				 throw new ServiceException("托盘型号："+bean.getTuopxh()+"对应包装参考系不存在");
			}
			baoz.setBaozlx(bean.getBaozxh());
			//包装参考系
			Baoz bz2=(Baoz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryBaoz", baoz);
			if(bz2==null){
				 throw new ServiceException("包装型号："+bean.getBaozxh()+"对应包装参考系不存在");
			}
			Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_tuopbzdygx.countBaozxh",bean);
			if (Integer.valueOf(obj.toString())>0) {
				 throw new ServiceException("一个包装型号只能对应一个托盘型号");
			}
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_tuopbzdygx.insertTuopbzdygx",bean);//增加数据库
	
		}
		return "success";
	}
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(List<Tuopbzdygx> edit,String userID) throws ServiceException{
		for(Tuopbzdygx bean:edit){
			Baoz baoz=new Baoz();
			baoz.setBiaos("1");
			baoz.setBaozlx(bean.getTuopxh());
			//包装参考系
			Baoz bz1=(Baoz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryBaoz", baoz);
			if(bz1==null){
				 throw new ServiceException("托盘型号："+bean.getTuopxh()+"对应包装参考系不存在");
			}
			baoz.setBaozlx(bean.getBaozxh());
			//包装参考系
			Baoz bz2=(Baoz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryBaoz", baoz);
			if(bz2==null){
				 throw new ServiceException("包装型号："+bean.getBaozxh()+"对应包装参考系不存在");
			}
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_tuopbzdygx.updateTuopbzdygx",bean);//修改数据库
			}
		return "success";
	}
	
	
	/**
	 * 获得多个
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(Baoz bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryBaoz",bean);
	}
	
	@SuppressWarnings("unchecked")
	public List<Baoz> listImport(Baoz bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryBaozImport",bean);
	}
}
