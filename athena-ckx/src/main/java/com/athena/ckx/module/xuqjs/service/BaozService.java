package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.Baoz;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 包装
 * @author denggq
 * 2012-3-19
 */
@Component
public class BaozService extends BaseService<Baoz>{

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Baoz> insert,
	           ArrayList<Baoz> edit,
	   		   ArrayList<Baoz> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){//无操作
			return "null";
		}
		inserts(insert,userID);//增加
		edits(edit,userID);//修改
//		deletes(delete,userID);//失效
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
	public String inserts(List<Baoz> insert,String userID)throws ServiceException{
		for(Baoz bean:insert){
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertBaoz",bean);//增加数据库
		}
		return "";
	}
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(List<Baoz> edit,String userID) throws ServiceException{
		for(Baoz bean:edit){
			if(null==bean.getShifhs()||"".equals(bean.getShifhs())){
				throw  new ServiceException("是否回收不能为空");
			}
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			bean.setBaozmc(bean.getBaozmc().replaceAll(" ", " "));  //把乱码的空格替换掉
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateBaoz",bean);//修改数据库
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Baoz> delete,String userID)throws ServiceException{
		for(Baoz bean:delete){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteBaoz",bean);//失效数据库
		}
		return "";
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
