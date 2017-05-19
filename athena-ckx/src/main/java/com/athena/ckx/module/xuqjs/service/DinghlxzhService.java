package com.athena.ckx.module.xuqjs.service;


import java.util.ArrayList;

import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.Dinghlxzh;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 订货路线转换
 * @author qizhongtao
 * @date 2012-4-06
 */
@Component
public class DinghlxzhService extends BaseService<Dinghlxzh>{
	
	/**
	 * 获取命名空间
	 * @author qizhongtao
	 * @date 2012-4-06
	 * @return String
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 批量数据保存
	 * @author qizhongtao
	 * @date 2012-4-06
	 * @param insert,edit,delete,userName
	 * @return String
	 * */
	@Transactional
	public String save(ArrayList<Dinghlxzh> insert,ArrayList<Dinghlxzh> edit,ArrayList<Dinghlxzh> delete,String userName) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()) {
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
	 * @date 2012-4-06
	 * @param insert,userName
	 * @return ""
	 * */
	@Transactional
	public String inserts(ArrayList<Dinghlxzh> insert,String userName) throws ServiceException{
		for (Dinghlxzh bean : insert) {
			
			//零件编号是否存在
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter = '"+bean.getUsercenter()+"' and lingjbh = '"+bean.getLingjbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			CkxLingj lingj = new CkxLingj();
			lingj.setUsercenter(bean.getUsercenter());
			lingj.setLingjbh(bean.getLingjbh());
			lingj.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountLingj", lingj, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertDinghlxzh",bean);
		}
		return "";
	}
	
	/**
	 * 批量edit
	 * @author qizhongtao
	 * @date 2012-4-06
	 * @param edit,userName
	 * @return ""
	 * */
	@Transactional
	public String edits(ArrayList<Dinghlxzh> edit,String userName) throws ServiceException{
		for (Dinghlxzh bean : edit) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateDinghlxzh",bean);
		}
		return "";
	}
	
	/**
	 * 批量delete
	 * @author qizhongtao
	 * @date 2012-4-06
	 * @param delete,userName
	 * @return ""
	 * */
	@Transactional
	public String deletes(ArrayList<Dinghlxzh> delete,String userName) throws ServiceException{
		for (Dinghlxzh bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteDinghlxzh",bean);
		}
		return "";
	}
}




