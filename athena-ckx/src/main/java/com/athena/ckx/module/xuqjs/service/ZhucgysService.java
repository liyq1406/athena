package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.athena.ckx.entity.xuqjs.Anjmlxhd;
import com.athena.ckx.entity.xuqjs.Zhucgys;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 主从供应商
 * @author xss
 * 2015-2-4
 * 0010495
 */
@Component
public class ZhucgysService extends BaseService<Zhucgys>{
	
	static Logger log = Logger.getLogger(ZhucgysService.class);
	
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	/*
	@Inject
	private CacheManager cacheManager;//缓存
*/
	@Override
	public String getNamespace() {
		return "zhucgys";
	}

	
	@Transactional
	public Map<String, Object> select(Zhucgys bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zhucgys.queryZhucgys",bean,bean);
	}	
	
	
	@Transactional
	public String inserts(List<Zhucgys> insert,String userID)throws ServiceException{
		String message = "";
		for(Zhucgys bean:insert){
			int i = checkSlavefanhs(bean);//该从承运商是否存在
			int j = ckGongys(bean);//校验主从承运商的是否存在，是否有效 			
		
			if(i>0){
				message = "该从承运商已经存在!";
				throw new ServiceException(message);
			}
			if(j<2){
				message = "该主/从承运商无效!";
				throw new ServiceException(message);
			}			
			
			bean.setCreator(getLoginUser().getUsername());
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());	
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("zhucgys.insertZhucgys",bean);
		}
		return "success";
	}
	
	
	@Transactional
	public String deletes(List<Zhucgys> delete,String userID)throws ServiceException{
		for(Zhucgys bean:delete){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("zhucgys.deleteZhucgys",bean);
		}
		return "success";
	}
	
	
	@Transactional
	public String edits(List<Zhucgys> edit,String userID) throws ServiceException{
		String message = "";
		for(Zhucgys bean:edit){
			int i = checkSlavefanhs(bean);//该从承运商是否存在
			int j = ckGongys(bean);//校验主从承运商的是否存在，是否有效 			
		
			/*
			if(i>0){
				message = "该从承运商已经存在!";
				throw new ServiceException(message);
			}
			*/
			if(j<2){
				message = "该主/从承运商无效!";
				throw new ServiceException(message);
			}
			
			bean.setCreator(getLoginUser().getUsername());
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());	
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("zhucgys.updateZhucgys",bean);
		}
		return "success";
	}
	
	
	@Transactional
	public String save(ArrayList<Zhucgys> insert,
	           ArrayList<Zhucgys> edit,
	   		   ArrayList<Zhucgys> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userID);
		edits(edit,userID);
		deletes(delete,userID);
		return "success";
	}
	
	public int checkSlavefanhs(Zhucgys zhucgys){
		String str = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zhucgys.checkSlavefanhs",zhucgys);
		return Integer.valueOf(StringUtils.defaultString(str, "0"));
	}
	
	public int ckGongys(Zhucgys zhucgys){
		String str = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zhucgys.ckGongys",zhucgys);
		return Integer.valueOf(StringUtils.defaultString(str, "0"));
	}
	
	
	
	/*
	public Map queryZhucgys(Zhucgys zhucgys){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zhucgys.queryZhucgys",zhucgys,zhucgys);
	}

	public Zhucgys selectZhucgys(Zhucgys zhucgys){
		return (Zhucgys) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zhucgys.queryZhucgys",zhucgys,zhucgys);
	}
	
	public int deleteZhucgys(Zhucgys zhucgys){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("zhucgys.deleteZhucgys",zhucgys);
	}

	public int saveZhucgys(Zhucgys zhucgys){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("zhucgys.saveZhucgys", zhucgys);
	}
		*/
		


	
}
