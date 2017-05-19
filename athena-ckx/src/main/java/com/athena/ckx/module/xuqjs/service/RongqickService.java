package com.athena.ckx.module.xuqjs.service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.Anjmlxhd;
import com.athena.ckx.entity.xuqjs.Rongqick;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;


/**
 * 容器区-仓库
 * @author xss
 * 2015-4-9
 * 0010495
 */
@Component
public class RongqickService extends BaseService{
	
	static Logger log = Logger.getLogger(RongqickService.class);
	
	@Inject
	private CacheManager cacheManager;//缓存
	
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}

	@Override
	public String getNamespace() {
		return "rongqick";
	}
	

//	@Transactional
//	public Map<String, Object> select(Rongqick bean) throws ServiceException{
//		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("rongqick.queryRongqick",bean,bean);
//	}
//	
//	@Transactional
//	public String deletes(List<Rongqick> delete,String userID)throws ServiceException{
//		for(Rongqick bean:delete){
//			bean.setEditor(userID);
//			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("rongqick.deleteRongqick",bean);
//		}
//		return "success";
//	}
//	
//	
//	@Transactional
//	public String save(ArrayList<Rongqick> insert,
//	           ArrayList<Rongqick> edit,
//	   		   ArrayList<Rongqick> delete,String userID) throws ServiceException{
//		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
//			return "null";
//		}
//		inserts(insert,userID);
//		edits(edit,userID);
//		deletes(delete,userID);
//		return "success";
//	}
//	
//	@Transactional
//	public String inserts(List<Rongqick> insert,String userID)throws ServiceException{
//		String message = "";
//		for(Rongqick bean:insert){			
//			bean.setCreator(getLoginUser().getUsername());
//			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
//			bean.setEditor(getLoginUser().getUsername());
//			bean.setEdit_time(DateTimeUtil.getAllCurrTime());	
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("rongqick.insertRongqick",bean);
//		}
//		return "success";
//	}
//	
//	@Transactional
//	public String edits(List<Rongqick> edit,String userID) throws ServiceException{
//		String message = "";
//		for(Rongqick bean:edit){					
//			bean.setCreator(getLoginUser().getUsername());
//			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
//			bean.setEditor(getLoginUser().getUsername());
//			bean.setEdit_time(DateTimeUtil.getAllCurrTime());	
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("rongqick.updateRongqick",bean);
//		}
//		return "success";
//	}
//	
//	
	

	public Map queryRongqick(Rongqick rongqick){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("rongqick.queryRongqick",rongqick,rongqick);
	}

	public Rongqick selectRongqick(Rongqick rongqick){
		return (Rongqick) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("rongqick.queryRongqick",rongqick,rongqick);
	}
	
	
	public int deleteRongqick(Rongqick rongqick){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("rongqick.deleteRongqick",rongqick);
	}

	public int saveRongqick(Rongqick rongqick){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("rongqick.saveRongqick", rongqick);
	}
	
	public int updateRongqick(Rongqick rongqick){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("rongqick.updateRongqick", rongqick);
	}
	
	
	public List queryRongqqbh(Map<String,String> map) {//查询容器区编号
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("rongqick.queryRongqcbh",map);
	}
	
	public List queryCangkbh(Map<String,String> map) {//查询仓库编号
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("rongqick.queryCangkbh",map);
	}
	
	
}
