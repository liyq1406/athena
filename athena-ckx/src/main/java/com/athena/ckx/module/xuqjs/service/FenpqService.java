package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Fenzx;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 分配区
 * @author denggq
 * @date 2012-3-21
 */
@Component
public class FenpqService extends BaseService<Fenpq>{

	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-3-21
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Fenpq> insert,
	           ArrayList<Fenpq> edit,
	   		   ArrayList<Fenpq> delete,String userID,Shengcx shengcx) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){//无操作
			return "null";
		}
		inserts(insert,userID,shengcx);//增加
		edits(edit,userID,shengcx);//修改
//		deletes(delete,userID,shengcx);//失效
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param insert,userID
	 * @return  ""
	 */
	@SuppressWarnings({"unchecked" })
	@Transactional
	public String inserts(List<Fenpq> insert,String userID,Shengcx shengcx)throws ServiceException{
		for(Fenpq bean:insert){
			
//			Fenpq f =  new Fenpq();
//			f.setUsercenter(shengcx.getUsercenter());
//			f.setFenpqh(bean.getFenpqh());
//			List<Fenpq> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getFenpq",f);//增加数据库
//			if(0 != list.size()){
//				if("0".equals(list.get(0).getBiaos())){
//					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateFenpq",bean);//更新数据库
//				}else{
//					throw new ServiceException(GetMessageByKey.getMessage("fpbh")+bean.getFenpqh()+GetMessageByKey.getMessage("scxexist")+list.get(0).getShengcxbh()+GetMessageByKey.getMessage("bucunzai"));
//				}
//			}
			bean.setUsercenter(shengcx.getUsercenter());//用户中心
			bean.setShengcxbh(shengcx.getShengcxbh());//生产线编号
			//如果产线为分装线，则生产线编号为大线线号		hanwu 20151120
			Fenzx fzx = FenzxService.getFenzx(baseDao, bean.getUsercenter(), bean.getShengcxbh());
			if(fzx != null){
				bean.setFenzxh(fzx.getFenzxh());
				bean.setShengcxbh(fzx.getDaxxh());
			}
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertFenpq",bean);//增加数据库
			try {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.AorEbYFenpq",bean);//增加|修改数据库
				//hanwu date:20150302  mantis:0012493
				//已存在失效的分配区 ，会进行update而不是insert，此时要更新生产线编号以及分装线号 
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjxhdByFenpq",bean);
			} catch (DataAccessException e) {
				throw new ServiceException("已存在用户中心："+bean.getUsercenter()+",分配区："+bean.getFenpqh()+"的有效数据");
			}
		}
		return "";
	}
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(List<Fenpq> edit,String userID,Shengcx shengcx) throws ServiceException{
		for(Fenpq bean:edit){
			bean.setUsercenter(shengcx.getUsercenter());//用户中心
			bean.setShengcxbh(shengcx.getShengcxbh());//生产线编号
			//如果产线为分装线，则生产线编号为大线线号		hanwu 20160301 0012491
			Fenzx fzx = FenzxService.getFenzx(baseDao, bean.getUsercenter(), bean.getShengcxbh());
			if(fzx != null){
				bean.setFenzxh(fzx.getFenzxh());
				bean.setShengcxbh(fzx.getDaxxh());
			}
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateFenpq",bean);//更新数据库
		}
		return "";
	}
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Fenpq> delete,String userID,Shengcx shengcx)throws ServiceException{
		for(Fenpq bean:delete){
			bean.setUsercenter(shengcx.getUsercenter());//用户中心
			bean.setShengcxbh(shengcx.getShengcxbh());//生产线编号
			//如果产线为分装线，则生产线编号为大线线号		hanwu 20160301 0012491
			Fenzx fzx = FenzxService.getFenzx(baseDao, bean.getUsercenter(), bean.getShengcxbh());
			if(fzx != null){
				bean.setFenzxh(fzx.getFenzxh());
				bean.setShengcxbh(fzx.getDaxxh());
			}
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteFenpq",bean);//失效数据库
		}
		return "";
	}
	
}
