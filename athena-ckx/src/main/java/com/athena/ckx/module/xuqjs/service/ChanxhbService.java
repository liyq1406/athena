package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;

import com.athena.ckx.entity.xuqjs.Chanxhb;
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
 * 产线合并Service
 * @author qizhongtao
 * @date 2012-4-17
 */
@Component
public class ChanxhbService extends BaseService<Chanxhb>{
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
	 * @date 2012-4-17
	 * @param insert,edit,delete,userName
	 * @return String
	 * */
	@Transactional
	public String save(ArrayList<Chanxhb> insert,ArrayList<Chanxhb> edit,ArrayList<Chanxhb> delete,String userName)throws ServiceException{
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
	 * @date 2012-4-17
	 * @param insert,userName
	 * @return ""
	 * */
	@Transactional
	public String inserts(ArrayList<Chanxhb> insert,String userName)throws ServiceException{
		for (Chanxhb bean : insert) {
			if(null != bean.getMubcx()){
				//目标产线是否存在
//				String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_shengcx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+bean.getMubcx()+"' and biaos = '1' and flag = '1'";
//				DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("mubiaocx")+bean.getMubcx()+GetMessageByKey.getMessage("notexist"));
				Shengcx shengcx = new Shengcx();
				shengcx.setUsercenter(bean.getUsercenter());
				shengcx.setShengcxbh(bean.getMubcx());
				shengcx.setBiaos("1");
				DBUtil.checkCount(baseDao, "ts_ckx.getCountShengcx", shengcx, GetMessageByKey.getMessage("mubiaocx")+bean.getMubcx()+GetMessageByKey.getMessage("notexist"));
			}
			
			if(null !=bean.getYuancx() && null != bean.getMubcx() && bean.getYuancx().equals(bean.getMubcx())){
				throw new ServiceException(GetMessageByKey.getMessage("yuancxyumubiaocxbt"));
			}
			
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertChanxhb", bean);
		}
		return "";
	}
	/**
	 * 批量edit
	 * @author qizhongtao
	 * @date 2012-4-17
	 * @param edit,userName
	 * @return ""
	 * */
	@Transactional
	public String edits(ArrayList<Chanxhb> edit,String userName)throws ServiceException{
		for (Chanxhb bean : edit) {
			
			if(null != bean.getMubcx()){
				//目标产线是否存在
//				String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_shengcx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+bean.getMubcx()+"' and biaos = '1' and flag = '1'";
//				DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("mubiaocx")+bean.getMubcx()+GetMessageByKey.getMessage("notexist"));
				Shengcx shengcx = new Shengcx();
				shengcx.setUsercenter(bean.getUsercenter());
				shengcx.setShengcxbh(bean.getMubcx());
				DBUtil.checkCount(baseDao, "ts_ckx.getCountShengcx", shengcx, GetMessageByKey.getMessage("mubiaocx")+bean.getMubcx()+GetMessageByKey.getMessage("notexist"));
			}
			
			if(null !=bean.getYuancx() && null != bean.getMubcx() && bean.getYuancx().equals(bean.getMubcx())){
				throw new ServiceException(GetMessageByKey.getMessage("yuancxyumubiaocxbt"));
			}
			
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateChanxhb", bean);
		}
		return "";
	}
	/**
	 * 批量delete
	 * @author qizhongtao
	 * @date 2012-4-17
	 * @param delete,userName
	 * @return ""
	 * */
	@Transactional
	public String deletes(ArrayList<Chanxhb> delete,String userName)throws ServiceException{
		for (Chanxhb bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteChanxhb", bean);
		}
		return "";
	}
}
