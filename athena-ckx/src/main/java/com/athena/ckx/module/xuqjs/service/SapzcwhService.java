package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.Anjmlxhd;
import com.athena.ckx.entity.xuqjs.Sapzcwh;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;



@Component
public class SapzcwhService extends BaseService{
	
	static Logger log = Logger.getLogger(SapzcwhService.class);
	
	@Inject
	private CacheManager cacheManager;//缓存

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	public Map querySapzcwh(Sapzcwh sapzcwh){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectPages("sapzcwh.querySapzcwh",sapzcwh,sapzcwh);
	}

	public Sapzcwh selectSapzcwh(Sapzcwh sapzcwh){
		return (Sapzcwh) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("sapzcwh.querySapzcwh",sapzcwh,sapzcwh);
			}
	
	
	public int deleteSapzcwh(Sapzcwh sapzcwh)throws ServiceException {
		 int  i= 0 ;
		 try{  
			 this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			 
			 i =  deleteSapzcwh_new(sapzcwh);
			 
			 //切换到执行层
			 this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
			
		 }catch(Exception e){  
			//切换到执行层
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
			throw new ServiceException(e.getMessage());
		 }
		 return i;
	}
	
	@Transactional
	public int deleteSapzcwh_new(Sapzcwh sapzcwh){ 
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("sapzcwh.deleteSapzcwh",sapzcwh);

	}
	
	
	public int saveSapzcwh(Sapzcwh sapzcwh)throws ServiceException {		
		 int  i= 0 ;
		 try{  
			 this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			 
			 i =  saveSapzcwh_new(sapzcwh);
			 
			 //切换到执行层
			 this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
			
		 }catch(Exception e){  
			//切换到执行层
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
			throw new ServiceException(e.getMessage());
		 }
		 return i;
	}
	
	@Transactional
	public int saveSapzcwh_new(Sapzcwh sapzcwh){	 
			 return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("sapzcwh.saveSapzcwh", sapzcwh);		 
	}
	
	
	public int updateSapzcwh(Sapzcwh sapzcwh)throws ServiceException {		
		 int  i= 0 ;
		 try{  
			 this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			 
			 i = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("sapzcwh.updateSapzcwh", sapzcwh);
			 
			 //切换到执行层
			 this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
			
		 }catch(Exception e){  
			//切换到执行层
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
			throw new ServiceException(e.getMessage());
		} 
		 return i;
	}
	
	@Transactional
	public int updateSapzcwh_new(Sapzcwh sapzcwh){	 
			 return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("sapzcwh.updateSapzcwh", sapzcwh);
	}

	
}
