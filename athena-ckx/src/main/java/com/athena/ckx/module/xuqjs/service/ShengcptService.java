package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.Shengcpt;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 生产平台
 * @author denggq
 * @date 2012-3-28
 */
@Component
public class ShengcptService extends BaseService<Shengcpt>{

	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-3-28
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-3-28
	 * @param insert,edit,delete,userId
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Shengcpt> insert,
	           ArrayList<Shengcpt> edit,
	   		   ArrayList<Shengcpt> delete,String userId) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userId);
		edits(edit,userId);
		deletes(delete,userId);
		return "success";
	}
	
	
	
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-3-28
	 * @param insert,userId
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Shengcpt> insert,String userId)throws ServiceException{
		for(Shengcpt bean:insert){
			
			//生产线编号是否存在
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_shengcx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+bean.getShengcxbh()+"' and biaos = '1' and flag = '1'";
//			DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("shengchanxbh")+bean.getShengcxbh()+GetMessageByKey.getMessage("notexist"));
			Shengcx shengcx = new Shengcx();
			shengcx.setUsercenter(bean.getUsercenter());
			shengcx.setShengcxbh(bean.getShengcxbh());
			shengcx.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountShengcx", shengcx, GetMessageByKey.getMessage("shengchanxbh")+bean.getShengcxbh()+GetMessageByKey.getMessage("notexist"));
	
			if( null != bean.getShengcjp() && null != bean.getWeilscjp() && bean.getWeilscjp().equals(bean.getShengcjp())){
				throw new ServiceException(GetMessageByKey.getMessage("zcscjpbutwlscjp"));
			}
			
			if(null != bean.getQiehsj() ){
				if(DateTimeUtil.compare(bean.getQiehsj(), DateTimeUtil.getCurrDate())){
					throw new ServiceException(GetMessageByKey.getMessage("bixushiweilai"));
				}
				if(null == bean.getWeilscjp()){
					throw new ServiceException(GetMessageByKey.getMessage("zcwlnotnull"));
				}
			}
			
			if(null == bean.getQiehsj() && null != bean.getWeilscjp()){
				throw new ServiceException(GetMessageByKey.getMessage("zcqhsjnotnull"));
			}
			
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertShengcpt",bean);
		}
		return "";
	}
	
	
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-3-28
	 * @param edit,userId
	 * @return ""
	 */
	@Transactional
	public String edits(List<Shengcpt> edit,String userId) throws ServiceException{
		for(Shengcpt bean:edit){
			
			if( null != bean.getShengcjp() && null != bean.getWeilscjp() && bean.getWeilscjp().equals(bean.getShengcjp())){
				throw new ServiceException(GetMessageByKey.getMessage("zcscjpbutwlscjp"));
			}
			
			if(null != bean.getQiehsj() ){
				if(DateTimeUtil.compare(bean.getQiehsj(), DateTimeUtil.getCurrDate())){
					throw new ServiceException(GetMessageByKey.getMessage("bixushiweilai"));
				}
				if(null == bean.getWeilscjp()){
					throw new ServiceException(GetMessageByKey.getMessage("zcwlnotnull"));
				}
			}
			
			if(null == bean.getQiehsj() && null != bean.getWeilscjp()){// BUG 0002475
				throw new ServiceException(GetMessageByKey.getMessage("zcqhsjnotnull"));
			}
			
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateShengcpt",bean);
		}
		return "";
	}
	
	
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-28
	 * @param delete,userId
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Shengcpt> delete,String userId)throws ServiceException{
		for(Shengcpt bean:delete){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteShengcpt",bean);
		}
		return "";
	}

}
