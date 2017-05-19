package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.GonghmsMxq;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 供货模式-毛需求Service
 * @author qizhongtao
 * @date 2012-4-09
 */
@Component
public class GonghmsMxqService extends BaseService<GonghmsMxq>{
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
	 * @date 2012-4-09
	 * @param insert,edit,delete,userName
	 * @return String
	 * */
	@Transactional
	public String save(ArrayList<GonghmsMxq> insert,ArrayList<GonghmsMxq> update,ArrayList<GonghmsMxq> delete,String userName)throws ServiceException{
		if(0 == insert.size()&&0 == update.size()&&0 == delete.size()){
			return "null";
		}else{
			inserts(insert,userName);
			updates(update,userName);
			deletes(delete,userName);
		}
		return "success";
	}
	/**
	 * 批量insert
	 * @author qizhongtao
	 * @date 2012-4-09
	 * @param insert,userName
	 * @return ""
	 * */
	@SuppressWarnings("unchecked")
	@Transactional
	public String inserts(ArrayList<GonghmsMxq> insert,String userName)throws ServiceException{
		for (GonghmsMxq bean : insert) {
			
			//无主键,判断重复数据
			List<GonghmsMxq> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getGonghmsMxq", bean);
			if(0 != list.size()){
				throw new ServiceException(GetMessageByKey.getMessage("gonghuomoshi")+bean.getGonghms()+GetMessageByKey.getMessage("xuqiulaiyuan")+bean.getXuqly()+GetMessageByKey.getMessage("dinghuoluxian")+bean.getDinghlx()+GetMessageByKey.getMessage("exist"));
			}
			
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertGonghmsMxq", bean);
		}
		return "";
	}
	/**
	 * 批量update
	 * @author qizhongtao
	 * @date 2012-4-09
	 * @param update,userName
	 * @return ""
	 * */
	@SuppressWarnings("unchecked")
	@Transactional
	public String updates(ArrayList<GonghmsMxq> update,String userName)throws ServiceException{
		for (GonghmsMxq bean : update) {
			
			//无主键,判断重复数据
			List<GonghmsMxq> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getGonghmsMxq", bean);
			if(0 != list.size()){
				throw new ServiceException(GetMessageByKey.getMessage("gonghuomoshi")+bean.getGonghms()+GetMessageByKey.getMessage("xuqiulaiyuan")+bean.getXuqly()+GetMessageByKey.getMessage("dinghuoluxian")+bean.getDinghlx()+GetMessageByKey.getMessage("exist"));
			}
			
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteGonghmsMxq", bean);
		}
		return "";
	}
	/**
	 * 批量delete
	 * @author qizhongtao
	 * @date 2012-4-09
	 * @param delete,userName
	 * @return ""
	 * */
	@Transactional
	public String deletes(ArrayList<GonghmsMxq> delete,String userName)throws ServiceException{
		for (GonghmsMxq bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteGonghmsMxq", bean);
		}
		return "";
	}
}
