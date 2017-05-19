package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;

import com.athena.ckx.entity.xuqjs.Danwhs;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 单位换算Service
 * @author qizhongtao
 * @date 2012-4-17
 */
@Component
public class DanwhsService extends BaseService<Danwhs>{
	/**
	 * 获取命名空间
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 批量数据保存
	 * @author qizhongtao
	 * @date 2012-4-17
	 * @param insert,edit,delete,userName
	 * @return String
	 * */
	@Transactional
	public String save(ArrayList<Danwhs> insert,ArrayList<Danwhs> edit,ArrayList<Danwhs> delete,String userName)throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}else{
			inserts(insert,userName);
			edits(edit,userName);
			deletes(delete,userName);
		}
		return "success";
	}
	/**
	 * 批量insert
	 * @author qizhongtao
	 * @date 2012-4-17
	 * @param insert,userName
	 * @return ""
	 * */
	@Transactional
	public String inserts(ArrayList<Danwhs> insert,String userName)throws ServiceException{
		for (Danwhs bean : insert) {
			
			if(bean.getBeihdw().equals(bean.getMubdw())){
				throw new ServiceException(GetMessageByKey.getMessage("diffenert"));
			}
			
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertDanwhs", bean);
		}
		return "";
	}
	/**
	 * 批量edit
	 * @author qizhongtao
	 * @date 2012-4-17
	 * @param edit,userName
	 * @return ""
	 * */
	@Transactional
	public String edits(ArrayList<Danwhs> edit,String userName)throws ServiceException{
		for (Danwhs bean : edit) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateDanwhs", bean);
		}
		return "";
	}
	/**
	 * 批量delete
	 * @author qizhongtao
	 * @date 2012-4-17
	 * @param delete,userName
	 * @return ""
	 * */
	@Transactional
	public String deletes(ArrayList<Danwhs> delete,String userName)throws ServiceException{
		for (Danwhs bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteDanwhs", bean);
		}
		return "";
	}
}
