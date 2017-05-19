package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Baoz;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.entity.xuqjs.JinjjQuhYuns;
import com.athena.ckx.entity.xuqjs.QuhYuns;
import com.athena.ckx.entity.xuqjs.Tuopbzdygx;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 紧急件参考系
 * @author denggq
 * 2012-3-19
 */
@Component
public class JinjjQuhYunsService extends BaseService<JinjjQuhYuns>{

	@Override
	public String getNamespace() {
		return "ts_jinjjquhyuns";
	}

	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<JinjjQuhYuns> insert,
	           ArrayList<JinjjQuhYuns> edit,
	   		   ArrayList<JinjjQuhYuns> delete,String userID) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){//无操作
			return "null";
		}

		String msg="";
		 msg=inserts(insert,userID);//增加
		 if(!msg.equalsIgnoreCase("success")){
			 return msg; 
		 }
		 msg=edits(edit,userID);//修改
	/*	 if(!msg.equalsIgnoreCase("success")){
			 return msg; 
		 }
		 msg=deletes(delete,userID);//删除
*/		return msg;
	}
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<JinjjQuhYuns> delete,String userID)throws ServiceException{
		for(JinjjQuhYuns bean:delete){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_jinjjquhyuns.delJinjjQuhYuns",bean);//失效数据库
		}
		return "success";
	}
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public String inserts(List<JinjjQuhYuns> insert,String userID)throws ServiceException{
		for(JinjjQuhYuns bean:insert){
			GetPostOnly.checkqhqx(bean.getUsercenter());
			if(bean.getShengxsj().compareTo(bean.getShixsj())>0){
				 throw new ServiceException("生效时间必须小于失效时间");
			}
			 Gongcy gongys=new Gongcy();
			 gongys.setUsercenter(bean.getUsercenter());
			 gongys.setBiaos("1");
			 gongys.setGcbh(bean.getChengysdm());
			 Object obj2=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountGongys", gongys);
			 if (Integer.valueOf(obj2.toString())==0) {
					 throw new ServiceException("承运商编码:"+bean.getChengysdm()+"在参考系供应商不存在！");
			 }
			 gongys.setLeix("3");//类型不等于承运商
			 gongys.setGcbh(bean.getGongysdm());
			Object obj1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountGongys", gongys);
			if (Integer.valueOf(obj1.toString())==0) {
				 throw new ServiceException("供应商编码:"+bean.getGongysdm()+"在参考系供应商不存在！");
			}
			JinjjQuhYuns jinjjQuhYuns=new JinjjQuhYuns();
			jinjjQuhYuns.setUsercenter(bean.getUsercenter());
			jinjjQuhYuns.setGongysdm(bean.getGongysdm());
			jinjjQuhYuns.setChengysdm(bean.getChengysdm());
			List<JinjjQuhYuns> oldbeans=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_jinjjquhyuns.getJinjjQuhYuns",jinjjQuhYuns);
			for (JinjjQuhYuns jinjjQuhYuns2 : oldbeans) {
				if(jinjjQuhYuns2!=null ){
					if((jinjjQuhYuns2.getShengxsj().compareTo(bean.getShengxsj())<=0 && jinjjQuhYuns2.getShixsj().compareTo(bean.getShengxsj())>=0 ) 
					|| (jinjjQuhYuns2.getShengxsj().compareTo(bean.getShixsj())<=0   && jinjjQuhYuns2.getShixsj().compareTo(bean.getShixsj())>=0 )
					|| (jinjjQuhYuns2.getShengxsj().compareTo(bean.getShengxsj())<=0 && jinjjQuhYuns2.getShixsj().compareTo(bean.getShixsj())>=0 )
					|| (jinjjQuhYuns2.getShengxsj().compareTo(bean.getShengxsj())>=0 && jinjjQuhYuns2.getShixsj().compareTo(bean.getShixsj())<=0) 
					){
						 throw new ServiceException("生效时间重叠");	
					}
				}
			}
			
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_jinjjquhyuns.insertJinjjQuhYuns",bean);//增加数据库
	
		}
		return "success";
	}
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	public String edits(List<JinjjQuhYuns> edit,String userID) throws ServiceException{
		for(JinjjQuhYuns bean:edit){
			if(bean.getShengxsj().compareTo(bean.getShixsj())>0){
				 throw new ServiceException("生效时间必须小于失效时间");
			}
			if(bean.getGongysdm()!=null){
				bean.setGongysdm(bean.getGongysdm().replace(" ", " "));	
			}
			if(bean.getChengysdm()!=null){
				bean.setChengysdm(bean.getChengysdm().replace(" ", " "));
			}
			 Gongcy gongys=new Gongcy();
			 gongys.setUsercenter(bean.getUsercenter());
			 gongys.setBiaos("1");
			 gongys.setGcbh(bean.getChengysdm());
			 Object obj2=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountGongys", gongys);
			 if (Integer.valueOf(obj2.toString())==0) {
					 throw new ServiceException("承运商编码"+bean.getChengysdm()+"在参考系供应商不存在！");
			 }
			 gongys.setLeix("3");//类型不等于承运商
			 gongys.setGcbh(bean.getGongysdm());
			Object obj1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountGongys", gongys);
			if (Integer.valueOf(obj1.toString())==0) {
				 throw new ServiceException("供应商编码"+bean.getGongysdm()+"在参考系供应商不存在！");
			}
			JinjjQuhYuns jinjjQuhYuns=new JinjjQuhYuns();
			jinjjQuhYuns.setJinjjid(bean.getJinjjid());
			jinjjQuhYuns.setUsercenter(bean.getUsercenter());
			if(bean.getGongysdm()!=null){
				bean.setGongysdm(bean.getGongysdm().replace(" ", " "));	
			}
			if(bean.getChengysdm()!=null){
				bean.setChengysdm(bean.getChengysdm().replace(" ", " "));
			}
			jinjjQuhYuns.setGongysdm(bean.getGongysdm());
			jinjjQuhYuns.setChengysdm(bean.getChengysdm());
			List<JinjjQuhYuns> oldbeans=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_jinjjquhyuns.getJinjjQuhYuns",jinjjQuhYuns);
			for (JinjjQuhYuns jinjjQuhYuns2 : oldbeans) {
				if(jinjjQuhYuns2!=null ){
					if((jinjjQuhYuns2.getShengxsj().compareTo(bean.getShengxsj())<=0 && jinjjQuhYuns2.getShixsj().compareTo(bean.getShengxsj())>=0 ) 
					|| (jinjjQuhYuns2.getShengxsj().compareTo(bean.getShixsj())<=0   && jinjjQuhYuns2.getShixsj().compareTo(bean.getShixsj())>=0 )
					|| (jinjjQuhYuns2.getShengxsj().compareTo(bean.getShengxsj())<=0 && jinjjQuhYuns2.getShixsj().compareTo(bean.getShixsj())>=0 )
					|| (jinjjQuhYuns2.getShengxsj().compareTo(bean.getShengxsj())>=0 && jinjjQuhYuns2.getShixsj().compareTo(bean.getShixsj())<=0) 
					){
						 throw new ServiceException("生效时间重叠");
					}
				}
			}		
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_jinjjquhyuns.updateJinjjQuhYuns",bean);//修改数据库
			}
		return "success";
	}
	

}
