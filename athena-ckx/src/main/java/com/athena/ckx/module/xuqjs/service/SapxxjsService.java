package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.Anjmlxhd;
import com.athena.ckx.entity.xuqjs.Sapxxjs;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 用户中心|容器记账
 * @author xss
 * 2015-2-3
 * 0010495
 */
@Component
public class SapxxjsService extends BaseService{
	
	static Logger log = Logger.getLogger(SapxxjsService.class);
	
	@Inject
	private CacheManager cacheManager;//缓存

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	public Map querySapxxjs(Sapxxjs sapxxjs){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectPages("sapxxjs.querySapxxjs",sapxxjs,sapxxjs);		
	}

	public Sapxxjs selectSapxxjs(Sapxxjs sapxxjs){
		return (Sapxxjs) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("sapxxjs.querySapxxjs",sapxxjs,sapxxjs);		
	}
	 
	public int deleteSapxxjs(Sapxxjs sapxxjs)throws ServiceException {
		 int  i= 0 ;
		 try{ 
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
				
			 i = deleteSapxxjs_new(sapxxjs);
			
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
	public int deleteSapxxjs_new(Sapxxjs sapxxjs)throws ServiceException { 
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("sapxxjs.deleteSapxxjs",sapxxjs);			 					
	}

	
	
	public int saveSapxxjs(Sapxxjs sapxxjs)throws ServiceException {	
		 int  i = 0 ;
		 try{  
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2); 
			 
			i = saveSapxxjs_new(sapxxjs);
			
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
	public int saveSapxxjs_new(Sapxxjs sapxxjs)throws ServiceException {
		//0013490:插入数据重复，页面给出提示
		Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("sapxxjs.querySapxxjs", sapxxjs);
		if(obj != null){
			throw new ServiceException("对不起，该数据已存在！");
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("sapxxjs.saveSapxxjs", sapxxjs);
	}
	
	
	
	public int updateSapxxjs(Sapxxjs sapxxjs)throws ServiceException {	
		int i = 0 ;
		 try{   
			 this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2); 
			 
			 i = updateSapxxjs_new(sapxxjs);
			 
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
	public int updateSapxxjs_new(Sapxxjs sapxxjs)throws ServiceException {	 
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("sapxxjs.updateSapxxjs", sapxxjs);		
	}

	
}
