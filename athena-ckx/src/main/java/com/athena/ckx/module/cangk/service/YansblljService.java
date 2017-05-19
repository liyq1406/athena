package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.cangk.Xingzysts;
import com.athena.ckx.entity.cangk.Yansbllj;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.entity.xuqjs.Lingjgys;
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
 * 零件验收比例设置
 * @author denggq
 * @date 2012-2-6
 */
@Component
public class YansblljService extends BaseService<Yansbllj>{

	/**
	 * 分页查询方法
	 * @author denggq
	 * @date 2012-2-18
	 * @param bean
	 * @return Map 分页的结果
	 */
	@Transactional
	public Map<String, Object> select(Yansbllj bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryYansbllj",bean,bean);
	}
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-2-6
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Yansbllj> insert,
	           ArrayList<Yansbllj> edit,
	   		   ArrayList<Yansbllj> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userID);
		edits(edit,userID);
		deletes(delete);
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
	public String inserts(List<Yansbllj> insert,String userID)throws ServiceException{
		for(Yansbllj bean:insert){
			//零件编号是否存在
			CkxLingj lj = new CkxLingj();
			lj.setUsercenter(bean.getUsercenter());
			lj.setLingjbh(bean.getLingjbh());
			lj.setBiaos("1");
			String mes =GetMessageByKey.getMessage("lingjianbh")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountLingj", lj)){
				throw new ServiceException(mes);
			}
//			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter = '"+bean.getUsercenter()+"' and lingjbh = '"+bean.getLingjbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql1,GetMessageByKey.getMessage("lingjianbh")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
			
			//供应商编号是否存在
			Gongcy gcy = new Gongcy();
			gcy.setUsercenter(bean.getUsercenter());
			gcy.setGcbh(bean.getGongysbh());
			gcy.setLeix("3");
			gcy.setBiaos("1");
			String mse =GetMessageByKey.getMessage("gongyingsbh")+bean.getGongysbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountGongys", gcy)){
				throw new ServiceException(mse);
			}
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_gongys where usercenter = '"+bean.getUsercenter()+"' and gcbh = '"+bean.getGongysbh()+"' and leix in( '1', '2') and biaos = '1'";
//			DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("gongyingsbh")+bean.getGongysbh()+GetMessageByKey.getMessage("notexist"));
			
			//验收项编号是否存在
			Xingzysts xingzysts = new Xingzysts();
			xingzysts.setYansxbh(bean.getYansxbh());
			xingzysts.setBiaos("1");
			String mse1 = GetMessageByKey.getMessage("yanshouxbh")+bean.getYansxbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountXingzysts", xingzysts)){
				throw new ServiceException(mse1);
			}
//			String sql3 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xingzysts where yansxbh = '"+bean.getYansxbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql3, GetMessageByKey.getMessage("yanshouxbh")+bean.getYansxbh()+GetMessageByKey.getMessage("notexist"));
			
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertYansbllj",bean);
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
	public String edits(List<Yansbllj> edit,String userID) throws ServiceException{
		for(Yansbllj bean:edit){
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateYansbllj",bean);
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
	public String deletes(List<Yansbllj> delete)throws ServiceException{
		for(Yansbllj bean:delete){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteYansbllj",bean);
		}
		return "";
	}
	
	
	/**
	 * 生效提交按钮
	 * @author denggq
	 * @date 2012-5-8
	 * @return ""
	 */
	@Transactional
	public String commit(String usercenter,String userId) throws ServiceException{
		Yansbllj bean = new Yansbllj();//
		bean.setUsercenter(usercenter);
		bean.setCreator(userId);
		bean.setCreate_time(DateTimeUtil.getAllCurrTime());
		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteByMob",bean);//删除模板数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertByMob", bean);//根据模板生成数据
		return "shujucgsc";
	}
	
	/**
	 * 增加一个供应商的零件的验收项
	 * @author denggq
	 * @date 2012-8-2
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String addYansblljByGongys(Yansbllj bean, String userId) throws ServiceException{
		
		//List<Yansbllj> yansblljList = new ArrayList<Yansbllj>();
		//供应商编号是否存在
//		String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_gongys where usercenter = '"+bean.getUsercenter()+"' and gcbh = '"+bean.getGongysbh()+"' and leix in( '1', '2') and biaos = '1'";
//		DBUtilRemove.checkBH(sql2,  GetMessageByKey.getMessage("gongyingsbh")+bean.getGongysbh()+GetMessageByKey.getMessage("notexist"));
		
		//验收项编号是否存在
//		String sql3 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xingzysts where yansxbh = '"+bean.getYansxbh()+"' and biaos = '1'";
//		DBUtilRemove.checkBH(sql3,  GetMessageByKey.getMessage("yanshouxbh")+bean.getYansxbh()+GetMessageByKey.getMessage("notexist"));
		Xingzysts xzysts = new Xingzysts();
		xzysts.setYansxbh(bean.getYansxbh());
		xzysts.setBiaos("1");
		Xingzysts xingzysts = (Xingzysts)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getXingzysts", xzysts);
		if(null==xingzysts){
			throw new ServiceException(GetMessageByKey.getMessage("yanshouxbh")+bean.getYansxbh()+GetMessageByKey.getMessage("notexist"));
		}
		
		//零件编号是否存在
//		String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_lingj where usercenter ='"+bean.getUsercenter()+"' and lingjbh = '"+bean.getLingjbh()+"' and biaos = '1'";
//		DBUtilRemove.checkBH(sql1,  GetMessageByKey.getMessage("零件编号")+bean.getLingjbh()+GetMessageByKey.getMessage("notexist"));
		Lingjgys lingjgys = new Lingjgys();
		lingjgys.setUsercenter(bean.getUsercenter());
		lingjgys.setGongysbh(bean.getGongysbh());
		lingjgys.setLingjbh(bean.getLingjbh());
		lingjgys.setBiaos("1");
		List<Lingjgys> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryLingjgys",lingjgys);
		if(0 == list.size()){
			throw new ServiceException( GetMessageByKey.getMessage("yonghuzx")+"["+bean.getUsercenter()+"]"+GetMessageByKey.getMessage("xiagongys")+"["+bean.getGongysbh()+"]"+GetMessageByKey.getMessage("meiyoulj"));
		}
		for (Lingjgys l:list) {
			bean.setLingjbh(l.getLingjbh());
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			bean.setYansxsm(xingzysts.getYansxsm());
			//yansblljList.add(bean);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertYansbllj", bean);
			
		}
		//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_ckx.insertYansbllj", yansblljList);
		return "success";
	}
	
}