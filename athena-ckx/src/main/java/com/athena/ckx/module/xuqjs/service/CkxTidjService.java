package com.athena.ckx.module.xuqjs.service;


import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.CkxTidj;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 替代件
 * @author qizhongtao
 * @date 2012-3-28
 * */
@Component
public class CkxTidjService extends BaseService<CkxTidj>{
	/**
	 * 获得命名空间
	 * @author 
	 * @date 2012-3-28
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 批量数据保存
	 * @author qizhongtao
	 * @date 2012-3-28
	 * @param insert,delete,userName
	 * @return ""
	 * */
	@Transactional
	public String save(ArrayList<CkxTidj> insert,ArrayList<CkxTidj> edit,ArrayList<CkxTidj> delete,String userName)throws ServiceException{
		if(0 == insert.size() && 0 == delete.size() && 0 == edit.size()){
			return "null";
		}
		insert(insert,userName);
		edit(edit,userName);
		delete(delete,userName);
		return "success";
	}
	/**
	 * 批量insert方法
	 * @author qizhongtao
	 * @date 2012-3-28
	 * @param insert,userName
	 * @return ""
	 * */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String insert(ArrayList<CkxTidj> insert,String userName) throws ServiceException{
		for(CkxTidj bean : insert){
			
			if(bean.getTidljh().equals(bean.getLingjbh())){
				throw new ServiceException( GetMessageByKey.getMessage("ljbhytdbt"));
			}
			
			//零件编号是否存在
//			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter = '"+bean.getUsercenter()+"' and lingjbh = '"+bean.getLingjbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql1,  GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			CkxLingj lingj = new CkxLingj();
			lingj.setUsercenter(bean.getUsercenter());
			lingj.setLingjbh(bean.getLingjbh());
			lingj.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountLingj", lingj, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			//零件编号是否被用
			CkxTidj c1 = new CkxTidj();
			c1.setUsercenter(bean.getUsercenter());
			c1.setTidljh(bean.getLingjbh());
			List l1 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxTidj",c1);
			if(0 != l1.size() ){
				throw new ServiceException(  GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+ GetMessageByKey.getMessage("tdljycz"));
			}
			
			//替代零件编号是否存在
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter = '"+bean.getUsercenter()+"' and lingjbh = '"+bean.getTidljh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("tidailjbh")+bean.getTidljh()+GetMessageByKey.getMessage("notexist"));
			lingj.setLingjbh(bean.getTidljh());
			DBUtil.checkCount(baseDao, "ts_ckx.getCountLingj", lingj, GetMessageByKey.getMessage("tidailjbh")+bean.getTidljh()+GetMessageByKey.getMessage("notexist"));
			//替代零件编号是否被用
			CkxTidj c2 = new CkxTidj();
			c2.setUsercenter(bean.getUsercenter());
			c2.setLingjbh(bean.getTidljh());
			List l2 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxTidj",c2);
			if(0 != l2.size() ){
				throw new ServiceException( GetMessageByKey.getMessage("tidailjbh")+bean.getTidljh()+GetMessageByKey.getMessage("ljbhycz"));
			}
			
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxTidj",bean);
		}
		return "";
	}
	/**
	 * 批量insert方法
	 * @author qizhongtao
	 * @date 2012-3-28
	 * @param insert,userName
	 * @return ""
	 * */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String edit(ArrayList<CkxTidj> edit,String userName) throws ServiceException{
		for(CkxTidj bean : edit){
			
			if(bean.getTidljh().equals(bean.getLingjbh())){
				throw new ServiceException( GetMessageByKey.getMessage("ljbhytdbt"));
			}
			
			//零件编号是否存在
//			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter = '"+bean.getUsercenter()+"' and lingjbh = '"+bean.getLingjbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			CkxLingj lingj = new CkxLingj();
			lingj.setUsercenter(bean.getUsercenter());
			lingj.setLingjbh(bean.getLingjbh());
			lingj.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountLingj", lingj, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			//零件编号是否被用
			CkxTidj c1 = new CkxTidj();
			c1.setUsercenter(bean.getUsercenter());
			c1.setTidljh(bean.getLingjbh());
			List l1 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxTidj",c1);
			if(0 != l1.size() ){
				throw new ServiceException(  GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("tdljycz"));
			}
			
			//替代零件编号是否存在
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter = '"+bean.getUsercenter()+"' and lingjbh = '"+bean.getTidljh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("tidailjbh")+bean.getTidljh()+GetMessageByKey.getMessage("notexist"));
			lingj.setLingjbh(bean.getTidljh());
			DBUtil.checkCount(baseDao, "ts_ckx.getCountLingj", lingj, GetMessageByKey.getMessage("tidailjbh")+bean.getTidljh()+GetMessageByKey.getMessage("notexist"));
			//替代零件编号是否被用
			CkxTidj c2 = new CkxTidj();
			c2.setUsercenter(bean.getUsercenter());
			c2.setLingjbh(bean.getTidljh());
			List l2 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxTidj",c2);
			if(0 != l2.size() ){
				throw new ServiceException( GetMessageByKey.getMessage("tidailjbh")+bean.getTidljh()+GetMessageByKey.getMessage("ljbhycz"));
			}
			
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxTidj",bean);
		}
		return "";
	}
	/**
	 * 批量delete方法
	 * @author qizhongtao
	 * @date 2012-3-30
	 * @param delete,userName
	 * @return ""
	 * */
	@Transactional
	public String delete(ArrayList<CkxTidj> delete,String userName) throws ServiceException{
		for (CkxTidj bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkxTidj",bean);
		}
		return "";
	}
}
