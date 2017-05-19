package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.Guodsz;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.Xiaohccx;
import com.athena.ckx.entity.xuqjs.Xiaohcmb;
import com.athena.ckx.entity.xuqjs.XiaohcmbCk;
import com.athena.ckx.entity.xuqjs.Zhengcgd;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 小火车上线指导
 * @author denggq
 * @date 2012-4-10
 */
@Component
public class XiaohcsxzdService extends BaseService<Xiaohc> {
	
	protected static Logger logger = Logger.getLogger(XiaohcsxzdService.class);	//定义日志方法
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-4-10
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 监控一辆小火车
	 * @param bean
	 * @param userName
	 * @return
	 */
	@Transactional
	public String monitorOne(Xiaohc xiaohc,String tiql,String userName,List dataList){
		Map param = new HashMap<String,String>();
		//提前量
		int tqsl = Integer.parseInt(tiql);
		Guodsz guodsz =null;
		Zhengcgd sjEmon = null;
		Zhengcgd sjSmon = null;
		XiaohcmbCk xiaohcmbCk = null;
		XiaohcmbCk xiaohcmbCkYbh = null;//应备货
		XiaohcmbCk xiaohcmbCkSc = null;//小火车模板上一趟次
		List<XiaohcmbCk> yibhXiaohcmbList = null;//已备货列表			
		int max = 0;
		int emon=0;
		int smon =0;
		String guodsj=null;	
		Map resultMap = new HashMap<String,Object>();
		//根据用户中心生产线，查询上线下线物理点
		param.put("usercenter", xiaohc.getUsercenter());
		param.put("shengcxbh", xiaohc.getShengcxbh());
		param.put("xiaohcbh", xiaohc.getXiaohcbh());
		
		//查询已备货/将备货小火车模板10
		yibhXiaohcmbList =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx_sxzd.queryXiaohcmbList", param);
		
		//查询小火车详情		
		xiaohc =  (Xiaohc) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getXiaohc", param);
		
		guodsz = (Guodsz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.queryGuodsz", param);
		
		if(guodsz==null){
			resultMap.put("xiaohc", xiaohc);			
			dataList.add(resultMap);
			return "过点设置为空";
		}
		//实际EMON
		param.put("swuld", guodsz.getShangxd());
		sjEmon = (Zhengcgd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.querySxZhengcgdByWuld", param);
		//实际SMON
		param.put("xwuld", guodsz.getXiaxd());
		sjSmon = (Zhengcgd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.queryXxZhengcgdByWuld", param);
		
		if(sjEmon==null||sjSmon==null){
			resultMap.put("xiaohc", xiaohc);			
			dataList.add(resultMap);
			return "Emon或Smon为空";
		}
		//查询应备货
		xiaohcmbCkYbh = (XiaohcmbCk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.queryYbhXiaohcmb", param);
		
		
		//查询小火车趟次,待发货
		param.put("emonsxlsh", sjEmon.getZhengcxh());
		param.put("smonsxlsh", sjSmon.getZhengcxh());
		xiaohcmbCk = (XiaohcmbCk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.queryXiaohcmbByEmonAndSmon", param);
		
		if(xiaohcmbCk==null){
			resultMap.put("xiaohc", xiaohc);
			dataList.add(resultMap);
			return "小火车模板为空";
		}
		if(sjEmon.getZhengcxh()==null||sjSmon.getZhengcxh()==null||xiaohcmbCk.getEmonsxlsh()==null||xiaohcmbCk.getSmonsxlsh()==null){
			resultMap.put("xiaohc", xiaohc);
			dataList.add(resultMap);
			return "整车序号为空";
		}
		emon = sjEmon.getZhengcxh() - xiaohcmbCk.getEmonsxlsh();
		logger.info("emon="+emon);
		smon = sjSmon.getZhengcxh() - xiaohcmbCk.getSmonsxlsh();
		logger.info("smon="+smon);
		
		if(emon>smon){
			max = emon;
			guodsj = sjEmon.getGuodsj();
		}else{
			max = smon;
			guodsj = sjSmon.getGuodsj();
		}
		logger.info("max="+max);
		param.put("editor", userName);
		//beiz3上线推荐时间
		if(max>=-tqsl&&xiaohcmbCk.getBeiz3()==null){
			//更新上线推荐时间
			xiaohcmbCk.setBeiz3(guodsj);
			xiaohcmbCk.setEditor(userName);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx_sxzd.updateXiaohcmbTjsxsj", xiaohcmbCk);
		}
		
		//查询小火车上一趟次			
		xiaohcmbCkSc = (XiaohcmbCk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.queryXiaohcmbSytc", param);
		
		if(xiaohcmbCkSc!=null&&xiaohcmbCkSc.getShangxsj()==null){
			//更新上线时间为当前时间				
			xiaohcmbCkSc.setEditor(userName);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx_sxzd.updateXiaohcmbSjsxsj", xiaohcmbCkSc);
			
		}		
		
		//封装结果集
		resultMap.put("xiaohc", xiaohc);
		resultMap.put("xiaohcmb", xiaohcmbCk);
		resultMap.put("xiaohcmbYbh", xiaohcmbCkYbh);
		resultMap.put("emon", sjEmon.getZhengcxh());
		resultMap.put("smon", sjSmon.getZhengcxh());
		resultMap.put("max", max);
		resultMap.put("xiaohcmbSc", xiaohcmbCkSc);
		resultMap.put("yibhXiaohcmbList", yibhXiaohcmbList);
		dataList.add(resultMap);
		
		return "success";
	}
	
	/**
	 * 监控多辆小火车
	 * @param bean
	 * @param userName
	 * @return
	 */
	@Transactional
	public String monitorMore(List<Xiaohc> list,String tiql,String userName,List dataList){
		Map param = new HashMap<String,String>();
		//提前量
		int tqsl = Integer.parseInt(tiql);
		Guodsz guodsz =null;
		Zhengcgd sjEmon = null;
		Zhengcgd sjSmon = null;
		XiaohcmbCk xiaohcmbCk = null;
		XiaohcmbCk xiaohcmbCkYbh = null;//应备货
		XiaohcmbCk xiaohcmbCkSc = null;//小火车模板上一趟次
		int max = 0;
		int emon=0;
		int smon =0;
		String guodsj=null;
		//小火车全部信息
		Xiaohc xiaohcMx = null;
		//保存结果集的Map
		Map resultMap = null;
		for(Xiaohc xiaohc:list){
			resultMap = new HashMap<String,Object>();
			//根据用户中心生产线，查询上线下线物理点
			param.put("usercenter", xiaohc.getUsercenter());
			param.put("shengcxbh", xiaohc.getShengcxbh());
			param.put("xiaohcbh", xiaohc.getXiaohcbh());
			
			//查询小火车详情		
			xiaohcMx =  (Xiaohc) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getXiaohc", param);
			
			guodsz = (Guodsz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.queryGuodsz", param);
			
			if(guodsz==null){
				resultMap.put("xiaohc", xiaohcMx);
				dataList.add(resultMap);
				continue;
			}
			//实际EMON
			param.put("swuld", guodsz.getShangxd());
			sjEmon = (Zhengcgd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.querySxZhengcgdByWuld", param);
			//实际SMON
			param.put("xwuld", guodsz.getXiaxd());
			sjSmon = (Zhengcgd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.queryXxZhengcgdByWuld", param);
			
			if(sjEmon==null||sjSmon==null){
				resultMap.put("xiaohc", xiaohcMx);
				dataList.add(resultMap);
				continue;
			}
			//查询应备货
			xiaohcmbCkYbh = (XiaohcmbCk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.queryYbhXiaohcmb", param);
			
			//查询小火车趟次			
			param.put("emonsxlsh", sjEmon.getZhengcxh());
			param.put("smonsxlsh", sjSmon.getZhengcxh());
			xiaohcmbCk = (XiaohcmbCk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.queryXiaohcmbByEmonAndSmon", param);
			
			if(xiaohcmbCk==null){
				resultMap.put("xiaohc", xiaohcMx);
				dataList.add(resultMap);
				continue;
			}
			if(sjEmon.getZhengcxh()==null||sjSmon.getZhengcxh()==null||xiaohcmbCk.getEmonsxlsh()==null||xiaohcmbCk.getSmonsxlsh()==null){
				resultMap.put("xiaohc", xiaohcMx);
				dataList.add(resultMap);
				continue;
			}
			emon = sjEmon.getZhengcxh() - xiaohcmbCk.getEmonsxlsh();
			
			smon = sjSmon.getZhengcxh() - xiaohcmbCk.getSmonsxlsh();
			
			if(emon>smon){
				max = emon;
				guodsj = sjEmon.getGuodsj();
			}else{
				max = smon;
				guodsj = sjSmon.getGuodsj();
			}
			param.put("editor", userName);
			//beiz3上线推荐时间
			if(max>=-tqsl&&xiaohcmbCk.getBeiz3()!=null){
				//更新上线推荐时间
				xiaohcmbCk.setBeiz3(guodsj);
				xiaohcmbCk.setEditor(userName);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx_sxzd.updateXiaohcmbTjsxsj", xiaohcmbCk);
			}
			
			//查询小火车上一趟次			
			xiaohcmbCkSc = (XiaohcmbCk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx_sxzd.queryXiaohcmbSytc", param);
			
			if(xiaohcmbCkSc!=null&&xiaohcmbCkSc.getShangxsj()==null){
				//更新上线时间为当前时间			
				xiaohcmbCkSc.setEditor(userName);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx_sxzd.updateXiaohcmbSjsxsj", xiaohcmbCkSc);
				
			}
			
			
			//封装结果集
			resultMap.put("xiaohc", xiaohcMx);
			resultMap.put("xiaohcmb", xiaohcmbCk);
			resultMap.put("xiaohcmbYbh", xiaohcmbCkYbh);
			resultMap.put("emon", sjEmon.getZhengcxh());
			resultMap.put("smon", sjSmon.getZhengcxh());
			resultMap.put("max", max);
			resultMap.put("xiaohcmbSc", xiaohcmbCkSc);
			dataList.add(resultMap);
		}
	    return "success";
	}
	

}
