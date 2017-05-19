package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;

import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.CkxZuiddhsl;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.entity.xuqjs.Lingjgys;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 最大订货数量Service
 * @author qizhongtao
 * @date 2012-4-07
 */
@Component
public class CkxZuiddhslService extends BaseService<CkxZuiddhsl>{
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
	 * @date 2012-4-07
	 * @param insert,edit,delete,userName
	 * @return String
	 * */
	@Transactional
	public String save(ArrayList<CkxZuiddhsl> insert,ArrayList<CkxZuiddhsl> edit,ArrayList<CkxZuiddhsl> delete,String userName)throws ServiceException{
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
	 * @date 2012-4-07
	 * @param insert,userName
	 * @return ""
	 * */
	@Transactional
	public String inserts(ArrayList<CkxZuiddhsl> insert,String userName)throws ServiceException{
		for (CkxZuiddhsl bean : insert) {
			
			//零件编号是否存在
//			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter = '"+bean.getUsercenter()+"' and lingjbh = '"+bean.getLingjbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			CkxLingj lingj = new CkxLingj();
			lingj.setUsercenter(bean.getUsercenter());
			lingj.setLingjbh(bean.getLingjbh());
			lingj.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountLingj", lingj, GetMessageByKey.getMessage("lingjianbianhao")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			//供应商编号是否存在
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_gongys where usercenter = '"+bean.getUsercenter()+"' and gcbh = '"+bean.getGongysbh()+"' and leix in( '1', '2') and biaos = '1'";
//			DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()+GetMessageByKey.getMessage("notexist"));
			Gongcy gongcy = new Gongcy();
			gongcy.setUsercenter(bean.getUsercenter());
			gongcy.setGcbh(bean.getGongysbh());
			gongcy.setLeix("3");
			gongcy.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountGongys", gongcy, GetMessageByKey.getMessage("gongyisbh")+bean.getGongysbh()+GetMessageByKey.getMessage("notexist"));
			//零件供应商关系是否存在
			Lingjgys l = new Lingjgys();
			l.setUsercenter(bean.getUsercenter());
			l.setLingjbh(bean.getLingjbh());
			l.setGongysbh(bean.getGongysbh());
			Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLingjgys", l);
			if(null == obj){
				throw new ServiceException(GetMessageByKey.getMessage("lj")+"["+bean.getLingjbh()+"]"+GetMessageByKey.getMessage("gys")+"["+bean.getGongysbh()+"]"+GetMessageByKey.getMessage("guanxbcz"));
			}
			bean.setCreator(userName);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxZuiddhsl", bean);
		}
		return "";
	}
	/**
	 * 批量edit
	 * @author qizhongtao
	 * @date 2012-4-07
	 * @param edit,userName
	 * @return ""
	 * */
	@Transactional
	public String edits(ArrayList<CkxZuiddhsl> edit,String userName)throws ServiceException{
		for (CkxZuiddhsl bean : edit) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxZuiddhsl", bean);
		}
		return "";
	}
	/**
	 * 批量delete
	 * @author qizhongtao
	 * @date 2012-4-07
	 * @param delete,userName
	 * @return ""
	 * */
	@Transactional
	public String deletes(ArrayList<CkxZuiddhsl> delete,String userName)throws ServiceException{
		for (CkxZuiddhsl bean : delete) {
			bean.setEditor(userName);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkxZuiddhsl", bean);
		}
		return "";
	}
}
