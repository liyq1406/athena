package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.entity.xuqjs.Gongysxhc;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.Xiaohccx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class GongysxhcService extends BaseService<Gongysxhc> {
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-4-10
	 */
	@Override
	protected String getNamespace() {
		return "gongysxhc";
	}
	
	/**
	 * 分页查询供应商小火车页面
	 * @param bean
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> selectGysxhc(Gongysxhc bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ckxgongysxhc.queryGongysxhcByParam",bean,bean);
	}
	
	/**
	 * 保存小火车-车厢
	 * @param bean
	 * @param operant 1-新增  2-修改
	 * @author denggq
	 * @date 2012-4-10
	 * @return bean
	 */
	@Transactional
	public String save(Gongysxhc bean , Integer operant , ArrayList<Gongysxhc> insert , ArrayList<Gongysxhc> edit , ArrayList<Gongysxhc> delete ,String userId) throws ServiceException{
		
		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		String result=checkData(bean);
		if(result!=null){//输入信息无效
			return result;
		}
		if (1 == operant){//增加
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			if(bean.getQistc()==null){
				bean.setQistc(0);
			}
			if(bean.getHebtc()==null){
				bean.setHebtc(0);
			}
			if(bean.getIshunt()==null){
				bean.setIshunt("N");
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxgongysxhc.insertGongysxhc", bean);//增加数据库
		}else if(2 == operant){//修改
			if(bean.getIshunt()==null){
				bean.setIshunt("N");
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxgongysxhc.updateGongysxhc", bean);//修改数据库
		}				
		return "success";
	}
	
	
	@Transactional
	public String doDelete(Gongysxhc bean) throws ServiceException{			
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxgongysxhc.updateBiaosByParam", bean);//失效供应商小火车
		return "shixiaocg";		
	}
	
	private String checkData(Gongysxhc bean){
		String error = null;
		Map<String,String> param = new HashMap<String,String>();
		param.put("gcbh", bean.getGongysbh());
		param.put("usercenter", bean.getUsercenter());
		//供应商是否有效
		List<Gongcy> gongcyList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.checkGongys", param);
		if(gongcyList==null||(gongcyList!=null&&gongcyList.size()<1)){
			error = "gongysbcz";//供应商不存在
		}
		//用户中心，生产线编号，小火车是否有效
		Xiaohc xiaohc = new Xiaohc();
		xiaohc.setUsercenter(bean.getUsercenter());
		xiaohc.setShengcxbh(bean.getShengcxbh());
		xiaohc.setXiaohcbh(bean.getXiaohcbh());
		xiaohc.setBiaos("1");
		List<Xiaohc> xiaohcList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiaohc", xiaohc);
		if(xiaohcList==null || (xiaohcList!=null&&xiaohcList.size()<1)){
			error = "yonghzxscxxhcbcz";//用户中心生产线的小火车不存在
		}
		return error;
	}
}
