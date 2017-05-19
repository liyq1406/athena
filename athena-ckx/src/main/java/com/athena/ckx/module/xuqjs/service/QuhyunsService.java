package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Baoz;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.entity.xuqjs.QuhYuns;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 取货运输参考系
 * @author denggq
 * 2012-3-19
 */
@Component
public class QuhyunsService extends BaseService<QuhYuns>{

	@Override
	public String getNamespace() {
		return "ts_quhys";
	}

	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-3-21
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<QuhYuns> insert,
	           ArrayList<QuhYuns> edit,
	   		   ArrayList<QuhYuns> delete,String userID) throws ServiceException{
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
	public String deletes(List<QuhYuns> delete,String userID)throws ServiceException{
		for(QuhYuns bean:delete){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_quhys.delQuhYuns",bean);//失效数据库
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
	public String inserts(List<QuhYuns> insert,String userID)throws ServiceException{
		for(QuhYuns bean:insert){
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
		//	int lifmdj=Integer.parseInt(bean.getZhixqhdj())+Integer.parseInt(bean.getQuyjpdj())+Integer.parseInt(bean.getGanxysdj())+Integer.parseInt(bean.getCangcpsdj());
			double lifmdj=bean.getZhixqhdj()+bean.getQuyjpdj()+bean.getGanxysdj()+bean.getCangcpsdj();
			
			if(lifmdj>0){
			QuhYuns b=new QuhYuns();
			b.setUsercenter(bean.getUsercenter());
			b.setGongysdm(bean.getGongysdm());
	//		b.setChengysdm(bean.getChengysdm());  一个用户中心+供应商+生效时间只能有一个承运商。
			List<QuhYuns> oldbeans=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_quhys.getQuhYuns",b);
			for (QuhYuns oldbean : oldbeans) {
				if(oldbean!=null ){
					if((oldbean.getShengxsj().compareTo(bean.getShengxsj())<=0 && oldbean.getShixsj().compareTo(bean.getShengxsj())>=0 ) 
					|| (oldbean.getShengxsj().compareTo(bean.getShixsj())<=0   && oldbean.getShixsj().compareTo(bean.getShixsj())>=0 )
					|| (oldbean.getShengxsj().compareTo(bean.getShengxsj())<=0 && oldbean.getShixsj().compareTo(bean.getShixsj())>=0 )
					|| (oldbean.getShengxsj().compareTo(bean.getShengxsj())>=0 && oldbean.getShixsj().compareTo(bean.getShixsj())<=0) 
					){
						 throw new ServiceException("生效时间重叠");
					}
				}
			}		
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_quhys.insertQuhYuns",bean);//增加数据库
		}else{
			 throw new ServiceException("支线取货、区域集配、干线运输、仓储配送之和必须大于0");
		}
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
	public String edits(List<QuhYuns> edit,String userID) throws ServiceException{
		for(QuhYuns bean:edit){
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
					 throw new ServiceException("承运商编码:"+bean.getChengysdm()+"在参考系供应商不存在！");
			 }
				gongys.setLeix("3");//类型不等于承运商
			 gongys.setGcbh(bean.getGongysdm());
			Object obj1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountGongys", gongys);
			if (Integer.valueOf(obj1.toString())==0) {
				 throw new ServiceException("供应商编码:"+bean.getGongysdm()+"在参考系供应商不存在！");
			}
			
			double lifmdj=bean.getZhixqhdj()+bean.getQuyjpdj()+bean.getGanxysdj()+bean.getCangcpsdj();
			if(lifmdj>0){
			QuhYuns b=new QuhYuns();
			b.setUsercenter(bean.getUsercenter());
			b.setQuhid(bean.getQuhid());
			if(bean.getGongysdm()!=null){
				b.setGongysdm(bean.getGongysdm().replace(" ", " "));	
			}
	//		if(bean.getChengysdm()!=null){   一个用户中心+供应商+生效时间只能有一个承运商
	//			b.setChengysdm(bean.getChengysdm().replace(" ", " "));
	//		}
		
			List<QuhYuns> oldbeans=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_quhys.getQuhYuns",b);
			for (QuhYuns oldbean : oldbeans) {
				if(oldbean!=null ){
					if((oldbean.getShengxsj().compareTo(bean.getShengxsj())<=0 && oldbean.getShixsj().compareTo(bean.getShengxsj())>=0 ) 
					|| (oldbean.getShengxsj().compareTo(bean.getShixsj())<=0   && oldbean.getShixsj().compareTo(bean.getShixsj())>=0 )
					|| (oldbean.getShengxsj().compareTo(bean.getShengxsj())<=0 && oldbean.getShixsj().compareTo(bean.getShixsj())>=0 )
					|| (oldbean.getShengxsj().compareTo(bean.getShengxsj())>=0 && oldbean.getShixsj().compareTo(bean.getShixsj())<=0) 
					){
						 throw new ServiceException("生效时间重叠");	
					}
				}
			}
			if(bean.getGongysdm()!=null){
				bean.setGongysdm(bean.getGongysdm().replace(" ", " "));
			}
			if(bean.getChengysdm()!=null){
				bean.setChengysdm(bean.getChengysdm().replace(" ", " "));
			}
	
			
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_quhys.updateQuhYuns",bean);//修改数据库
			}else{
				 throw new ServiceException("支线取货、区域集配、干线运输、仓储配送之和必须大于0");
			}
			}
		return "success";
	}
	

	
	/**
	 * 获得多个
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(Baoz bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryBaoz",bean);
	}
	
	@SuppressWarnings("unchecked")
	public List<Baoz> listImport(Baoz bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryBaozImport",bean);
	}
}
