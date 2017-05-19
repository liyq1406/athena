package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;

import com.athena.ckx.entity.xuqjs.Xuqly;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 需求来源-作用域Service
 * @author qizhongtao
 * @date 2012-4-17
 */
@Component
public class XuqlyService extends BaseService<Xuqly>{
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
	 * @param insert,delete,userName
	 * @return String
	 * */
	@Transactional
	public String save(ArrayList<Xuqly> insert,ArrayList<Xuqly> delete,String userName)throws ServiceException{
		if(0 == insert.size()&&0 == delete.size()){//无操作
			return "null";
		}else{
			inserts(insert,userName);//增加
			deletes(delete,userName);//删除
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
	public String inserts(ArrayList<Xuqly> insert,String userName)throws ServiceException{
		for (Xuqly bean : insert) {
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertXuqly", bean);//增加数据库
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
	public String deletes(ArrayList<Xuqly> delete,String userName)throws ServiceException{
		for (Xuqly bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXuqly", bean);//删除数据库
		}
		return "";
	}
}
