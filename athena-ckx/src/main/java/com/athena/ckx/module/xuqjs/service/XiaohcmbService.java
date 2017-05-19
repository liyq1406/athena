package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.Xiaohcmb;
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
 * @description小火车运输时刻模板
 * @author denggq
 * @date 2012-4-11
 */
@Component
public class XiaohcmbService extends BaseService<Xiaohcmb> {
	
	/**
	 * @description获得命名空间
	 * @author denggq
	 * @date 2012-4-11
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}

	@Transactional
	public String save(Xiaohcmb bean , Integer operant , ArrayList<Xiaohcmb> insert ,String userId) throws ServiceException{
		
		//生产线编号是否存在
		String sql1= "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_shengcx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+bean.getShengcxbh()+"' and biaos = '1' and flag = '1'";
		DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("shengchanxbh")+bean.getShengcxbh()+GetMessageByKey.getMessage("notexist"));
		
		//小火车编号是否存在
		String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xiaohc where usercenter = '"+bean.getUsercenter()+"' and xiaohcbh = '"+bean.getXiaohcbh()+"' and shengcxbh = '"+bean.getShengcxbh()+"' and biaos = '1'";
		DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("xiaohcbh")+bean.getXiaohcbh()+GetMessageByKey.getMessage("notexist"));
		
		for(Xiaohcmb x:insert){
			x.setUsercenter(bean.getUsercenter());
			x.setShengcxbh(bean.getShengcxbh());
			x.setXiaohcbh(bean.getXiaohcbh());
			//获取同一编组同一天的最大序号的值
			Xiaohcmb o = (Xiaohcmb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getMaxTangc",x);
			Integer tangc = 1;
			if(o!=null){//本组有上一条数据				
				tangc=o.getTangc()+1;//获取趟次
			}			
			x.setTangc(tangc);			
			//获取当前趟的前一趟 
			Xiaohcmb before = (Xiaohcmb)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getHuocmbBefore",x);
			
			//当前趟上线时间大于上一趟次上线时间
			if(null != before){
				if(x.getShangxpysj()<=before.getShangxpysj()){
					throw new ServiceException("同用户中心"+x.getUsercenter()+"+生产线"+x.getShengcxbh()+"+小火车"+x.getXiaohcbh()+"下,第"+x.getTangc()+"趟的上线偏移时间应大于上一趟的上线偏移时间");
				}
			}
			x.setTangc(tangc);
			x.setCreator(userId);
			x.setCreate_time(DateTimeUtil.getAllCurrTime());
			x.setEditor(userId);
			x.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertXiaohcmb",x);
		}
		return "success";
	}
	
	/**
	 * @description 多行保存
	 * @param bean
	 * @author denggq
	 * @date 2012-4-11
	 * @return bean
	 */
	@Transactional
	public String save(ArrayList<Xiaohcmb> insert , ArrayList<Xiaohcmb> edit , ArrayList<Xiaohcmb> delete ,String userId) throws ServiceException{
		
//		inserts(insert,userId);
		edits(edit,userId);
//		deletes(delete,userId);
		
		return "success";
		
	}
	
	/**
	 * @description私有批量insert方法
	 * @author denggq
	 * @date 2012-4-11
	 * @param insert,userId
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Xiaohcmb> insert,String userId)throws ServiceException{
		for(Xiaohcmb bean:insert){
			
			//生产线编号是否存在
//			String sql1= "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_shengcx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+bean.getShengcxbh()+"' and biaos = '1' and flag = '1'";
//			DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("shengchanxbh")+bean.getShengcxbh()+GetMessageByKey.getMessage("notexist"));
			Shengcx shengcx = new Shengcx();
			shengcx.setUsercenter(bean.getUsercenter());
			shengcx.setShengcxbh(bean.getShengcxbh());
			shengcx.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountShengcx", shengcx, GetMessageByKey.getMessage("shengchanxbh")+bean.getShengcxbh()+GetMessageByKey.getMessage("notexist"));
		
			//小火车编号是否存在
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xiaohc where usercenter = '"+bean.getUsercenter()+"' and xiaohcbh = '"+bean.getXiaohcbh()+"' and shengcxbh = '"+bean.getShengcxbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql2,GetMessageByKey.getMessage("xiaohcbh")+bean.getXiaohcbh()+GetMessageByKey.getMessage("notexist"));
			Xiaohc xiaohc = new Xiaohc();
			xiaohc.setUsercenter(bean.getUsercenter());
			xiaohc.setShengcxbh(bean.getShengcxbh());
			xiaohc.setXiaohcbh(bean.getXiaohcbh());
			xiaohc.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountXiaohc", xiaohc, GetMessageByKey.getMessage("xiaohcbh")+bean.getXiaohcbh()+GetMessageByKey.getMessage("notexist"));
	
			//下一趟次上线时间大于上一趟次上线时间
			compare(bean);
			
			//获取同一编组同一天的最大序号的值
			Object o = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getMaxTangc",bean);
			Integer tangc = 1;
			Xiaohcmb t=null;
			if(o!=null){//本组有上一条数据
				t=(Xiaohcmb) o;
				tangc=t.getTangc()+1;//获取趟次
			}
			
			bean.setTangc(tangc);
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertXiaohcmb",bean);
		}
		return "";
	}
	
	
	/**
	 * @description私有批量update方法
	 * @author denggq
	 * @date 2012-4-11
	 * @param edit,userId
	 * @return ""
	 */
	@Transactional
	public String edits(List<Xiaohcmb> edit,String userId) throws ServiceException{
		int i = 0;
		for(Xiaohcmb bean:edit){
			i++;
			//获取当前趟的后一趟 
			Xiaohcmb after = (Xiaohcmb)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getHuocmbAfter",bean);
			//获取当前趟的前一趟 
			Xiaohcmb before = (Xiaohcmb)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getHuocmbBefore",bean);
			
			//当前趟次上线时间必须大于上一趟次上线时间
			if(null != before){
				if(bean.getShangxpysj()<=before.getShangxpysj()){
					throw new ServiceException("同用户中心"+bean.getUsercenter()+"+生产线"+bean.getShengcxbh()+"+小火车"+bean.getXiaohcbh()+"下,第"+bean.getTangc()+"趟的上线偏移时间应大于上一趟的上线偏移时间");
				}
			}
			//获取下一条数据，如果是最后一条，则 为空
			Xiaohcmb afterCruuent = edit.size() > i ? edit.get(i): null;
			//下一趟次上线时间大于上一趟次上线时间
			if(null != after){
				//获取下一趟次的准确的上线偏移时间
				if(getShangxpysj(afterCruuent,after)<=bean.getShangxpysj()){
					throw new ServiceException("同用户中心"+bean.getUsercenter()+"+生产线"+bean.getShengcxbh()+"+小火车"+bean.getXiaohcbh()+"下,第"+bean.getTangc()+"趟的上线偏移时间应小于下一趟的上线偏移时间");
				}
			}
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateXiaohcmb",bean);
		}
		return "success";
	}
	 /**
	  * 0007569
	  * 获取下一趟次的准确的上线偏移时间
	  * @param bean list的
	  * @param after 查询数据库的
	  * @return
	  */
	private Integer getShangxpysj(Xiaohcmb bean,Xiaohcmb after){
		if(bean !=null
				&&bean.getUsercenter().equals(after.getUsercenter())
				&& bean.getShengcxbh().equals(after.getShengcxbh())
				&& bean.getXiaohcbh().equals(after.getXiaohcbh())
				&& bean.getTangc().equals(after.getTangc())				
				){
			//0007569 如果下一趟次的上线偏移时间被修改过，则取修改过得上线偏移时间
			return bean.getShangxpysj();
		}
		return after.getShangxpysj();
	} 
	
	/**
	 * @description私有批量删除方法
	 * @author denggq
	 * @date 2012-4-11
	 * @param delete,userId
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Xiaohcmb> delete,String userId)throws ServiceException{
		for(Xiaohcmb bean:delete){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXiaohcmb",bean);
		}
		return "";
	}
	
	
	/**
	 * @description 单条数据删除
	 * @author denggq
	 * @date 2012-7-9
	 * @param 
	 * @return ""
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String doDelete(Xiaohcmb bean) throws ServiceException {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXiaohcmb",bean);
		List<Xiaohcmb> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getAllHuocmbAfter",bean);
		for(Xiaohcmb x:list){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateAllTangc",x);
		}
		
		return "deletesuccess";
	}
	

	/**
	 * @description 比较
	 * @author denggq
	 * @param bean
	 * @date 2012-4-13
	 * @return String
	 */
	@Transactional
	public String compare(Xiaohcmb bean) throws ServiceException {
		if(1 != bean.getTangc()){
			Xiaohcmb temp = new Xiaohcmb();
			temp.setUsercenter(bean.getUsercenter());
			temp.setShengcxbh(bean.getShengcxbh());
			temp.setXiaohcbh(bean.getXiaohcbh());
			temp=(Xiaohcmb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getHuocmbAfter", temp);
			if(bean.getShangxpysj()<=temp.getShangxpysj()){
				throw new ServiceException("同用户中心"+bean.getUsercenter()+"+生产线"+bean.getShengcxbh()+"+小火车"+bean.getXiaohcbh()+"下,第"+bean.getTangc()+"趟的上线偏移时间应大于上一趟的上线偏移时间");
			}
		}
		return "";
	}
	
}
