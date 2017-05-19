package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.Dingdjsyl;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 订单计算依赖关系Service
 * @author qizhongtao
 * @date 2012-4-16
 */
@Component
public class DingdjsylService extends BaseService<Dingdjsyl>{
	/**
	 * 获取命名空间
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 批量数据保存
	 * @author qizhongtao
	 * @date 2012-4-16
	 * @param insert,edit,delete,userName
	 * @return String
	 * */
	@Transactional
	public String save(ArrayList<Dingdjsyl> insert,ArrayList<Dingdjsyl> edit,ArrayList<Dingdjsyl> delete,String userName)throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}else{
			inserts(insert,userName);
			edits(edit,userName);
			deletes(delete,userName);
		}
		return "success";
	}
	/**
	 * 批量insert
	 * @author qizhongtao
	 * @date 2012-4-16
	 * @param insert,userName
	 * @return ""
	 * */
	@SuppressWarnings("unchecked")
	@Transactional
	public String inserts(ArrayList<Dingdjsyl> insert,String userName)throws ServiceException{
		for (Dingdjsyl bean : insert) {
			
			//无主键,判断重复数据
			List<Dingdjsyl> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getDingdjsyl", bean);
			if(0 != list.size()){
				throw new ServiceException(GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()+GetMessageByKey.getMessage("waibughms")+"["+bean.getWaibghms()+"]"+GetMessageByKey.getMessage("dingdanjsgx")+"["+bean.getYilgx()+"]"+GetMessageByKey.getMessage("exist"));
			}
			
			//供应商编号是否存在
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_gongys where gcbh = '"+bean.getGongysbh()+"' and leix in( '1', '2') and biaos = '1'";
//			DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()+GetMessageByKey.getMessage("notexist"));
			Gongcy gongcy = new Gongcy();		
			gongcy.setGcbh(bean.getGongysbh());
			gongcy.setLeix("3");
			gongcy.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountGongys", gongcy, GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()+GetMessageByKey.getMessage("notexist"));
		
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertDingdjsyl", bean);
		}
		return "";
	}
	/**
	 * 批量edit
	 * @author qizhongtao
	 * @date 2012-4-16
	 * @param edit,userName
	 * @return ""
	 * */
	@SuppressWarnings("unchecked")
	@Transactional
	public String edits(ArrayList<Dingdjsyl> edit,String userName)throws ServiceException{
		for (Dingdjsyl bean : edit) {
			//无主键,判断重复数据
			List<Dingdjsyl> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getDingdjsyl", bean);
			if(0 != list.size()){
				throw new ServiceException(GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()+GetMessageByKey.getMessage("waibughms")+"["+bean.getWaibghms()+"]"+GetMessageByKey.getMessage("dingdanjsgx")+"["+bean.getYilgx()+"]"+GetMessageByKey.getMessage("exist"));
			}
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateDingdjsyl", bean);
		}
		return "";
	}
	/**
	 * 批量delete
	 * @author qizhongtao
	 * @date 2012-4-16
	 * @param delete,userName
	 * @return ""
	 * */
	@Transactional
	public String deletes(ArrayList<Dingdjsyl> delete,String userName)throws ServiceException{
		for (Dingdjsyl bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteDingdjsyl", bean);
		}
		return "";
	}
}
