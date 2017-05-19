package com.athena.ckx.module.cangk.service;


import java.util.ArrayList;
import java.util.List;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.cangk.Fahzt;
import com.athena.ckx.entity.cangk.Kuckzzysz;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 王宇
 * @author wangyu
 * @date 2013-07-30
 */
@Component
public class KuckzzyszService extends BaseService<Kuckzzysz> {
	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 批量保存方法
	 * @author wangyu
	 * @date 2012-4-17
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save( ArrayList<Kuckzzysz> insert,ArrayList<Kuckzzysz> edit,ArrayList<Kuckzzysz> delete) throws ServiceException{
		
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		edits(edit);
		deletes(delete);
		inserts(insert);
		return "success";
	}
	
	
	/**
	 * 私有批量insert方法  
	 * @author wangyu
	 * @date 2012-07-30
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Kuckzzysz> insert)throws ServiceException{
		for(Kuckzzysz bean:insert){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertKuckzzysz",bean);
		}
			return "success";
	}		
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-4-17
	 * @param user
	 * @return ""
	 */
	@SuppressWarnings("unused")
	@Transactional
	public String edits(List<Kuckzzysz> edit) throws ServiceException{
		for(Kuckzzysz bean:edit){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuckzzysz",bean);
		}
			return "success";
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public String deletes(List<Kuckzzysz> delete) {
		//执行物理删除操 作   删除前先判断是否为 有效
		for(Kuckzzysz bean:delete){
//			if("1".equals(bean.getShengxbs())){
//				throw new ServiceException("生效状态下 不能删除");
//			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("ts_ckx.deleteKuckzzysz", bean);
		}
		return "success";
	}
}
