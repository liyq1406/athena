package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.Lingjgyslsbz;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 零件供应商临时包装
 * @author denggq
 * @date 2012-4-17
 */
@Component
public class LingjgyslsbzService extends BaseService<Lingjgyslsbz>{

	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-4-17
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-4-17
	 * @param insert,edit,delete,userId
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Lingjgyslsbz> insert,
	           ArrayList<Lingjgyslsbz> edit,
	   		   ArrayList<Lingjgyslsbz> delete,String userId) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userId);
		edits(edit,userId);
		deletes(delete,userId);
		return "success";
	}
	
	
	
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-4-17
	 * @param insert,userId
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Lingjgyslsbz> insert,String userId)throws ServiceException{
		for(Lingjgyslsbz bean:insert){
			if(bean.getXuh()==null||"".equals(bean.getXuh())){
				throw new ServiceException("序号不能为空");
			}
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertLingjgyslsbz",bean);
		}
		return "";
	}
	
	
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-4-17
	 * @param edit,userId
	 * @return ""
	 */
	@Transactional
	public String edits(List<Lingjgyslsbz> edit,String userId) throws ServiceException{
		for(Lingjgyslsbz bean:edit){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjgyslsbz",bean);
		}
		return "";
	}
	
	
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-4-17
	 * @param delete,userId
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Lingjgyslsbz> delete,String userId)throws ServiceException{
		for(Lingjgyslsbz bean:delete){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteLingjgyslsbz",bean);
		}
		return "";
	}

}
