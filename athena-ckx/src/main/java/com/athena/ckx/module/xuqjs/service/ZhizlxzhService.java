package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;


import com.athena.ckx.entity.xuqjs.Zhizlxzh;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;

import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

import com.toft.core3.transaction.annotation.Transactional;

/**
 * 制造路线转换Service
 * @author qizhongtao
 * @date 2012-4-17
 */
@Component
public class ZhizlxzhService extends BaseService<Zhizlxzh>{
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
	public String save(ArrayList<Zhizlxzh> insert,ArrayList<Zhizlxzh> edit,ArrayList<Zhizlxzh> delete,String userName)throws ServiceException{
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
	public String inserts(ArrayList<Zhizlxzh> insert,String userName)throws ServiceException{
//		Object obj=cacheManager.getCacheInfo("queryZZLX").getCacheValue();
//		List<CacheValue> l=(ArrayList<CacheValue>)obj;
//		Map<String,String> map=new HashMap<String,String>();
//		for (CacheValue value : l) {
//			map.put(value.getKey(), value.getValue());
//		}
		
		
		for (Zhizlxzh bean : insert) {
			
			
//			if(null != map.get(bean.getZhizlxy()) ){
//				throw new ServiceException("原制造路线不能为"+bean.getZhizlxy());
//			}
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertZhizlxzh", bean);
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
	public String edits(ArrayList<Zhizlxzh> edit,String userName)throws ServiceException{
		for (Zhizlxzh bean : edit) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateZhizlxzh", bean);
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
	public String deletes(ArrayList<Zhizlxzh> delete,String userName)throws ServiceException{
		for (Zhizlxzh bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteZhizlxzh", bean);
		}
		return "";
	}
}
