package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.Jiaofmsd;
import com.athena.ckx.entity.xuqjs.Jiaofmzd;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 交互码字典
 * @author denggq
 * @date 2012-4-6
 */
@Component
public class JiaofmsdService extends BaseService<Jiaofmsd>{

	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-4-6
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-4-6
	 * @param insert,edit,delete,userId
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Jiaofmsd> insert,
	           ArrayList<Jiaofmsd> edit,
	   		   ArrayList<Jiaofmsd> delete,String userId) throws ServiceException{
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
	 * @date 2012-4-6
	 * @param insert,userId
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Jiaofmsd> insert,String userId)throws ServiceException{
		for(Jiaofmsd bean:insert){
			
			//交付码是否存在
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_jiaofmzd where usercenter = '"+bean.getUsercenter()+"' and jiaofm = '"+bean.getJiaofm()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("jiaofuma")+bean.getJiaofm()+GetMessageByKey.getMessage("notexist"));
			Jiaofmzd jiaofmzd = new Jiaofmzd();
			jiaofmzd.setUsercenter(bean.getUsercenter());
			jiaofmzd.setJiaofm(bean.getJiaofm());
			jiaofmzd.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountJiaofmzd", jiaofmzd, GetMessageByKey.getMessage("jiaofuma")+bean.getJiaofm()+GetMessageByKey.getMessage("notexist"));
			
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertJiaofmsd",bean);
		}
		return "";
	}
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-4-6
	 * @param edit,userId
	 * @return ""
	 */
	@Transactional
	public String edits(List<Jiaofmsd> edit,String userId) throws ServiceException{
		for(Jiaofmsd bean:edit){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateJiaofmsd",bean);
		}
		return "";
	}
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-4-6
	 * @param delete,userId
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Jiaofmsd> delete,String userId)throws ServiceException{
		for(Jiaofmsd bean:delete){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteJiaofmsd",bean);
		}
		return "";
	}
	
}
