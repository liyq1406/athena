package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Yansblsz;
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
 * 零件类型验收比例设置
 * @author denggq
 * @date 2012-2-6
 */
@Component
public class YansblszService extends BaseService<Yansblsz>{

	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-2-6
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
	public Map<String, Object> select(Yansblsz bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryYansblsz",bean,bean);
	}
	
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-2-6
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Yansblsz> insert,
	           ArrayList<Yansblsz> edit,
	   		   ArrayList<Yansblsz> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userID);
		edits(edit,userID);
		deletes(delete,userID);
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-2-6
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Yansblsz> insert,String userID)throws ServiceException{
		for(Yansblsz bean:insert){
			//验收项编号是否存在
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xingzysts where yansxbh = '"+bean.getYansxbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql,GetMessageByKey.getMessage("yanshouxbh")+bean.getYansxbh()+GetMessageByKey.getMessage("notexist"));
			//仓库编号是否存在
			Cangk ck = new Cangk();
			ck.setUsercenter(bean.getUsercenter());
			ck.setCangkbh(bean.getCangkbh());
			ck.setBiaos("1");
			String mes = GetMessageByKey.getMessage("仓库编号")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountCangk", ck)){
				throw new ServiceException(mes); 
			}
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_cangk where cangkbh = '"+bean.getCangkbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql,GetMessageByKey.getMessage("仓库编号")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist"));
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//同一零件类型验收比例要一致
//			int count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.yanzYansblsz",bean);
//			if(count>0){
//				//先查询出存在的零件类型 对应的 验收比例
//				Yansblsz yzbl = (Yansblsz)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getYzYansblsz",bean);
//				if(bean.getYansbl().equals(yzbl.getYansbl())){
//					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertYansblsz",bean);
//				}else{
//					throw new ServiceException (GetMessageByKey.getMessage("tongyiljyz"));
//				}
//				
//			}else{
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertYansblsz",bean);
//			}
		}
		return "";
	}
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-2-6
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(List<Yansblsz> edit,String userID) throws ServiceException{
		for(Yansblsz bean:edit){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//同一零件类型验收比例要一致
//			Yansblsz yzbl = (Yansblsz)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getYZYansblsz",bean);
//				if(bean.getYansbl().equals(yzbl.getYansbl())){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateYansblsz",bean);
//				}else{
//					throw new ServiceException (GetMessageByKey.getMessage("tongyiljyz"));
//				}
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-2-6
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Yansblsz> delete,String userID)throws ServiceException{
		for(Yansblsz bean:delete){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteYansblsz",bean);
		}
		return "";
	}
	
	
	/**
	 * 失效
	 * @author denggq
	 * @date 2012-2-22
	 * @return 主键
	 */
	@Transactional
	public String doDelete(Yansblsz bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteYansblsz", bean);
		return bean.getYansxbh();
	}
}