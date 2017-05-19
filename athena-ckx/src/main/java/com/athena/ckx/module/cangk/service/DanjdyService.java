package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Danjdy;
import com.athena.ckx.entity.cangk.Kehczm;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 单据打印
 * @author denggq
 * @date 2012-2-18
 */
@Component
public class DanjdyService extends BaseService<Danjdy>{

	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-2-18
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 分页查询方法
	 * @author denggq
	 * @date 2012-2-18
	 * @param bean
	 * @return Map 分页的结果
	 */
	@Transactional
	public Map<String, Object> select(Danjdy bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryDanjdy",bean,bean);
	}
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Danjdy> insert,
	           ArrayList<Danjdy> edit,
	   		   ArrayList<Danjdy> delete,String userID,Kehczm kehbhczm) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userID,kehbhczm);
		edits(edit,userID,kehbhczm);
		deletes(delete,userID,kehbhczm);
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-1-30
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Danjdy> insert,String userID,Kehczm kehbhczm)throws ServiceException{
		for(Danjdy bean:insert){
			
			//仓库子仓库编号是否存在
			String cangkbh = bean.getCangkbh().substring(0,3);//仓库编号
			String zickbh = bean.getCangkbh().substring(3);//子仓库编号
			
			Cangk ck = new Cangk();
			ck.setUsercenter(kehbhczm.getUsercenter());
			ck.setCangkbh(cangkbh);
			ck.setBiaos("1");
			String mes = GetMessageByKey.getMessage("cangkubh")+cangkbh+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountCangk", ck)){
				throw new ServiceException(mes); 
			}
//			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_cangk where usercenter = '"+kehbhczm.getUsercenter()+"' and cangkbh = '"+cangkbh+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("cangkubh")+cangkbh+GetMessageByKey.getMessage("notexist"));
			Zick zck = new Zick();
			zck.setUsercenter(kehbhczm.getUsercenter());
			zck.setCangkbh(cangkbh);
			zck.setZickbh(zickbh);
			zck.setBiaos("1");
			String mse = GetMessageByKey.getMessage("zicangkubh")+zickbh+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountZick", zck)){
				throw new ServiceException(mse); 
			}
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_zick where usercenter = '"+kehbhczm.getUsercenter()+"' and cangkbh = '"+cangkbh+"' and zickbh ='"+zickbh+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("zicangkubh")+zickbh+GetMessageByKey.getMessage("notexist"));
			
			bean.setUsercenter(kehbhczm.getUsercenter());
			bean.setKehbh(kehbhczm.getKehbh());
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertDanjdy",bean);
		}
		return "";
	}
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-1-30
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(List<Danjdy> edit,String userID,Kehczm kehbhczm) throws ServiceException{
		for(Danjdy bean:edit){
			bean.setUsercenter(kehbhczm.getUsercenter());
			bean.setKehbh(kehbhczm.getKehbh());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateDanjdy",bean);
		}
		return "";
	}
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-1-17
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Danjdy> delete,String userID,Kehczm kehbhczm)throws ServiceException{
		for(Danjdy bean:delete){
			bean.setUsercenter(kehbhczm.getUsercenter());
			bean.setKehbh(kehbhczm.getKehbh());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteDanjdy",bean);
		}
		return "";
	}
	
}
