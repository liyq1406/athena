package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.text.Format;
import java.text.SimpleDateFormat;
import com.athena.authority.entity.LoginUser;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.DateTimeUtil;
import com.toft.core3.jdbc.support.MultiDataSource;
import com.toft.core2.dao.database.DbUtils;
import com.athena.authority.util.AuthorityUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.entity.xuqjs.Fanhjzs;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.athena.ckx.util.GetMessageByKey;


/**
 * 返还记账商
 * @author xss
 * 2015-3-26
 * 0010495
 */
@Component
public class FanhjzsService extends BaseService<Fanhjzs>{
	
	static Logger log = Logger.getLogger(FanhjzsService.class);
	
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	@Inject
	private CacheManager cacheManager;//缓存

	@Override
	public String getNamespace() {
		return "fanhjzs";
	}
	
	@Transactional
	public Map<String, Object> select(Fanhjzs bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("fanhjzs.queryFanhjzs",bean,bean);
	}
	
	@Transactional
	public String deletes(List<Fanhjzs> delete,String userID)throws ServiceException{
		for(Fanhjzs bean:delete){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fanhjzs.deleteFanhjzs",bean);
		}
		return "success";
	}
	
	
	@Transactional
	public String save(ArrayList<Fanhjzs> insert,
	           ArrayList<Fanhjzs> edit,
	   		   ArrayList<Fanhjzs> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userID);
		edits(edit,userID);
		deletes(delete,userID);
		return "success";
	}
	
/*
	public int saveFanhjzs(Fanhjzs fanhjzs){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fanhjzs.saveFanhjzs", fanhjzs);
	}
	*/
	
	/*
	@Transactional
	public String edits(List<Fanhjzs> edit,String userID) throws ServiceException{
		for(Fanhjzs bean:edit){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKuwdj",bean);
		}
		return "";
	}
	*/
	
	@Transactional
	public String inserts(List<Fanhjzs> insert,String userID)throws ServiceException{
		String message = "";
		for(Fanhjzs bean:insert){

			int j = ckGongys(bean);//校验返还/记账商是否存在ckx_gongys，是否有效
			if(j<2){
				message = "操作失败！该返还/记账商无效!";
				throw new ServiceException(message);
			}
			//huxy_11348
			String s = isChengys(bean);
			if(s.equals("0"))
			{
				message = "只有记账方式为供应商的用户中心才能配置返还-记账商";
				throw new ServiceException(message);
			}			
			bean.setCreator(getLoginUser().getUsername());
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());	
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fanhjzs.insertFanhjzs",bean);
		}
		return "success";
	}
	
	@Transactional
	public String edits(List<Fanhjzs> edit,String userID) throws ServiceException{
		String message = "";
		for(Fanhjzs bean:edit){			
			int j = ckGongys(bean);//校验返还/记账商是否存在ckx_gongys，是否有效
			if(j<2){
				message = "操作失败！该返还/记账商无效!";
				throw new ServiceException(message);
			}
			
			bean.setCreator(getLoginUser().getUsername());
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());	
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("fanhjzs.updateFanhjzs",bean);
		}
		return "success";
	}
		
		
	public List<Map<String,String>> queryfhusercenter(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("fanhjzs.queryfhusercenter");		
	}
	
	
	public int checkfanhs(Fanhjzs fanhjzs){
		String str = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("fanhjzs.checkfanhs",fanhjzs);
		return Integer.valueOf(StringUtils.defaultString(str, "0"));
	}
	
	public int ckGongys(Fanhjzs fanhjzs){
		String str = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("fanhjzs.ckGongys",fanhjzs);
		return Integer.valueOf(StringUtils.defaultString(str, "0"));
	}
	
	public String isChengys(Fanhjzs fanhjzs){
		String str = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("fanhjzs.isChengys",fanhjzs);
		return str;
	}
	
}
