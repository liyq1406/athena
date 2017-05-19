package com.athena.ckx.module.paicfj.service;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.paicfj.Ckx_peizcl;
import com.athena.ckx.entity.paicfj.Ckx_peizclzb;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;


/**
 * 配载策略子表
 * @author hj
 * @Date 2012-02-21
 */
@Component
public class Ckx_peizclzbService extends BaseService<Ckx_peizclzb> {

	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 批量数据录入
	 * @author hj
	 * @Date 2012-02-21
	 * @param insert
	 * @param peizcl
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	public String inserts(List<Ckx_peizclzb> insert,Ckx_peizcl peizcl,String userID)throws ServiceException{
		Date date = new Date();
		for (Ckx_peizclzb bean : insert) {
			bean.setUsercenter(peizcl.getUsercenter());
			bean.setCelbh(peizcl.getCelbh());
			bean.setCreator(userID);
			bean.setCreate_time(date);
			bean.setEditor(userID);
			bean.setEdit_time(date);
			
			Map<String,String> map1 = new HashMap<String,String>();
			map1.put("tableName", "ckx_peizbz");
			map1.put("baozzbh", bean.getBaozzbh());
			//"配载包装定义表里不存在 配载包装号为"+bean.getBaozzbh()+"的数据";
			String mes1 = GetMessageByKey.getMessage("pzbzbcz")+bean.getBaozzbh()+GetMessageByKey.getMessage("sj");
			DBUtilRemove.checkYN(map1, mes1);
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_peizclzb",bean);
		}
		return "";
	}
	/**
	 * 数据编辑
	 * @author hj
	 * @Date 2012-02-21
	 * @param edit
	 * @param peizcl
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	public String edits(List<Ckx_peizclzb> edit,Ckx_peizcl peizcl,String userID)throws ServiceException{
		Date date = new Date();
		for (Ckx_peizclzb bean : edit) {
				bean.setUsercenter(peizcl.getUsercenter());
				bean.setCelbh(peizcl.getCelbh());
				bean.setEditor(userID);
				bean.setEdit_time(date);
				
				Map<String,String> map = new HashMap<String,String>();
				map.put("tableName", "ckx_peizbz");
				map.put("baozzbh", bean.getBaozzbh());
				//"配在包装定义表里不存在 配在包装号为"+bean.getBaozzbh()+"的数据"
				String mes =  GetMessageByKey.getMessage("pzbzbcz")+bean.getBaozzbh()+GetMessageByKey.getMessage("sj");
				DBUtilRemove.checkYN(map, mes);
				
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_peizclzb",bean);
		}
		return "";
	}
	/**
	 * 数据删除
	 * @author hj
	 * @Date 2012-02-21
	 * @param delete
	 * @param peizcl
	 * @return ""
	 * @throws ServiceException
	 */
	public String removes(List<Ckx_peizclzb> delete,Ckx_peizcl peizcl)throws ServiceException{
		for (Ckx_peizclzb bean : delete) {			
				bean.setUsercenter(peizcl.getUsercenter());
				bean.setCelbh(peizcl.getCelbh());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_peizclzb",bean);			
		}
		return "";
	}
	
}
