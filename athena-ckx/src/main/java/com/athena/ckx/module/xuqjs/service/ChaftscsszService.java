package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.xuqjs.Xitcsdy;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 系统参数定义
 * @author denggq
 * 2012-3-22
 */
@Component
public class ChaftscsszService extends BaseService<Xitcsdy>{
	
	
	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 拆分天数参数设置查询
	 */
	public Map<String,Object> selectchaf(Xitcsdy bean){
		Map<String,Object> map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectPages("ts_ckx.queryChaftscssz", bean, bean);
		
		//切换到执行层
		this.changeSourceId(ConstantDbCode.DATASOURCE_CKX); 
		return map;
	}
	
	/**
	 * 保存系统参数定义
	 * @param xitcsdy
	 * @param userId
	 * @return 主键
	 * @author denggq
	 * @time 2012-3-6
	 */
	public String save(ArrayList<Xitcsdy> insert,ArrayList<Xitcsdy> edit,ArrayList<Xitcsdy> delete,String userId) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		//切换到DDBH
		this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
		 try{   
			 edits(edit,userId); 
			 this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);			
		 }catch(Exception e){  
			 	//切换到执行层
				this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
				throw new ServiceException(e.getMessage());
		 }
		return "success";
	}
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(List<Xitcsdy> edit,String userId) throws ServiceException{		
		for(Xitcsdy chaftscssz:edit){
				chaftscssz.setZidlx("CFTS");
				chaftscssz.setQujzdz(chaftscssz.getQujzxz());
				chaftscssz.setEditor(userId);
				chaftscssz.setEdit_time(DateTimeUtil.getAllCurrTime());
				
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("ts_ckx.updateChaftscssz",chaftscssz);
		}
		return "";		
	}
	
}
