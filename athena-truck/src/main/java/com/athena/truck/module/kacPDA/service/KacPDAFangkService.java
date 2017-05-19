package com.athena.truck.module.kacPDA.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.util.PDATruckUtil;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.SysLog;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 车位放空PDA SERVICE
 * @author CSY
 * 2016-9-7
 */
@Component
public class KacPDAFangkService extends BaseService{
	
	static Logger log = Logger.getLogger(KacPDAFangkService.class);
	
	private String CODE_GET_CHEW = "PDA-KC003";	//获取车位
	private String CODE_DO_FANGK = "PDA-KC004";	//车位放空
	
	@Inject
	private UserOperLog userOperLog;

	@Override
	public String getNamespace() {
		return "kc_pda";
	}

	
/***********************************************************************************************/

	
	/**
	 * 车位放空PDA，根据区域编号、大站台、用户中心、卡车号		KC004
	 */
	@Transactional
	public JSONObject updateYundChew(JSONObject param) {
		
		JSONObject result = new JSONObject();	//传给PDA的结果
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.putAll(param);
		
		//将parameter中的值放入map
		if (param.containsKey("parameter")) {
			JSONObject pjson = (JSONObject) param.get("parameter");
			if (pjson.containsKey("quybh")) {
				paramMap.put("quybh", pjson.get("quybh"));
			}
			if (pjson.containsKey("daztbh")) {
				paramMap.put("daztbh", pjson.get("daztbh"));
			}
			if (pjson.containsKey("kach")) {
				paramMap.put("kach", pjson.get("kach"));
			}
			paramMap.remove("parameter");
		}
		
		String op_name = "";											//操作名称
		String oper = paramMap.get("oper_no").toString();				//操作人
		String usercenter = paramMap.get("trans_bran_code").toString();	//用户中心
		String kach = paramMap.get("kach").toString();					//卡车号
		String quybh = paramMap.get("quybh").toString();				//区域编号
		String daztbh = paramMap.get("daztbh").toString();				//大站台
		SysLog sysLog = new SysLog();									//PDA日志
		//记录PDA日志
		sysLog.setUsercenter(usercenter);
		sysLog.setOperators(oper);
		sysLog.setTrans_desc("PDA_流程操作");
		sysLog.setModule_name(CommonUtil.MODULE_CKX);
		sysLog.setTrans_url(CODE_DO_FANGK);
		
		log.info("PDA车位流程操作-开始（用户中心:"+usercenter+"，操作人:"+oper+"，区域编号:"+quybh+"，大站台:"+daztbh+"，卡车号:"+kach+"）"); 
		
		List<Map<String, Object>> yundList = new ArrayList<Map<String,Object>>();	//运单
		//根据条件查询出运单，包括排队表中的车位号
		yundList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kc_pda.queryYundChewLiucdyQuanx", paramMap);
		
		if (yundList == null || yundList.size()==0) {
			//该卡车状态不在“准入”到“放空”之间，故无法查出
			paramMap.put("response", PDATruckUtil.ERROR_TRANSFER);
			paramMap.put("respdesc", "该卡车号下无“准入”至“放空”之间的运单");
			paramMap.put("op_name", "操作");
			
			result = PDATruckUtil.gPdaTruck(paramMap);
			log.info("PDA车位流程操作-无可操作运单（用户中心:"+usercenter+"，操作人:"+oper+"，区域编号:"+quybh+"，大站台:"+daztbh+"，卡车号:"+kach+"）");
		
			//记录PDA日志
			sysLog.setTrans_content("PDA_流程操作-无可操作运单");
			userOperLog.addCorrectPDA(sysLog);
		
		}else {
			Map<String, Object> yundMap = yundList.get(0);	//运单
			yundMap.put("EDITOR", paramMap.get("oper_no")); 		//设置修改人姓名
			if (!yundMap.containsKey("QUYQX") || yundMap.get("QUYQX") == null || yundMap.get("QUYQX").equals("") || !yundMap.get("QUYBH").equals(yundMap.get("QUYQX"))) {
				//没有区域权限
				paramMap.put("response", PDATruckUtil.ERROR_TRANSFER);
				paramMap.put("respdesc", "您没有该卡车所在区域的权限");
				paramMap.put("op_name", "操作");
				result = PDATruckUtil.gPdaTruck(paramMap);
				
				//记录PDA日志
				sysLog.setTrans_content("PDA_流程操作-无区域权限（用户中心:"+usercenter+"，操作人:"+oper+"，区域编号:"+quybh+"，大站台:"+daztbh+"，卡车号:"+kach+"）");
				userOperLog.addCorrectPDA(sysLog);
				
				log.info("PDA车位流程操作-无区域权限（用户中心:"+usercenter+"，操作人:"+oper+"，区域编号:"+quybh+"，大站台:"+daztbh+"，卡车号:"+kach+"）");
			}else {
				try {
					
					//根据流程操作记录不同日志
					op_name = yundMap.get("LIUCMC").toString();
					
					yundMap.put("RESULT", "");
					
					log.info("PDA车位流程操作-" + op_name + "-操作开始（用户中心:"+usercenter+"，操作人:"+oper+"，区域编号:"+quybh+"，大站台:"+daztbh+"，卡车号:"+kach+"）");
					
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kc_pda.pro_truck_chewfk",yundMap);
					
					if (yundMap.containsKey("RESULT") && yundMap.get("RESULT").toString().equals("0")) {
						//操作成功
						paramMap.put("response", PDATruckUtil.SUCCESS_TRANSFER);
						paramMap.put("op_name", yundMap.get("NLIUCMC").toString());
						paramMap.put("respdesc", "“"+yundMap.get("LIUCMC").toString()+"”操作成功");
						result = PDATruckUtil.gPdaTruck(paramMap);	
						
						//记录PDA日志
						sysLog.setTrans_content("PDA_流程操作-" + op_name + "-成功（区域编号:"+quybh+"，大站台:"+daztbh+"，卡车号:"+kach+"）");
						userOperLog.addCorrectPDA(sysLog);
						log.info("PDA车位流程操作-" + op_name + "-成功（用户中心:"+usercenter+"，操作人:"+oper+"，区域编号:"+quybh+"，大站台:"+daztbh+"，卡车号:"+kach+"）");
					}else {
						//记录PDA日志
						sysLog.setTrans_content("PDA_流程操作-" + op_name + "-异常（区域编号:"+quybh+"，大站台:"+daztbh+"，卡车号:"+kach+"）");
						userOperLog.addCorrectPDA(sysLog);
						log.info("PDA车位流程操作-" + op_name + "-异常（用户中心:"+usercenter+"，操作人:"+oper+"，区域编号:"+quybh+"，大站台:"+daztbh+"，卡车号:"+kach+"）");
						throw new ServiceException("操作失败");
					}
					
				} catch (Exception e) {
					//记录PDA日志
					sysLog.setTrans_content("PDA_流程操作-失败（区域编号:"+quybh+"，大站台:"+daztbh+"，卡车号:"+kach+"）");
					sysLog.setCclass(CommonUtil.getClassMethod());
					sysLog.setCexception(CommonUtil.replaceBlank(e.getMessage()));
					userOperLog.addErrorPDA(sysLog);
					
					//操作失败
					paramMap.put("response", PDATruckUtil.ERROR_TRANSFER);
					paramMap.put("respdesc", "流程操作失败");
					paramMap.put("op_name", "操作");

					result = PDATruckUtil.gPdaTruck(paramMap);
					log.info("PDA车位流程操作-失败（用户中心:"+usercenter+"，操作人:"+oper+"，区域编号:"+quybh+"，大站台:"+daztbh+"，卡车号:"+kach+"）：" + e.getMessage());
				}
			}
		}
		log.info("PDA车位流程操作-结束");
		return result;
	}
	

/***********************************************************************************************/
	
	
	/**
	 * 获取车位		KC003
	 * 如果当前用户中心-区域-大站台 无自定义流程，则直接将准入状态的运单的车位放空
	 * @param param
	 * @return
	 */
	public JSONObject getChew(JSONObject param){
		
		log.info("PDA车位流程查询-开始");
		
		JSONObject result = new JSONObject();	//传给PDA的结果
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.putAll(param);
		
		//将parameter中的值放入map
		if (param.containsKey("parameter")) {
			JSONObject pjson = (JSONObject) param.get("parameter");
			if (pjson.containsKey("kach")) {
				paramMap.put("kach", pjson.get("kach"));
			}
			paramMap.remove("parameter");
		}

		String oper = paramMap.get("oper_no").toString();				//操作人
		String usercenter = paramMap.get("trans_bran_code").toString();	//用户中心
		String kach = paramMap.get("kach").toString();					//卡车号
		SysLog sysLog = new SysLog();									//PDA日志
		//记录PDA日志
		sysLog.setUsercenter(usercenter);
		sysLog.setOperators(oper);
		sysLog.setTrans_desc("PDA_车位流程查询");
		sysLog.setModule_name(CommonUtil.MODULE_CKX);
		sysLog.setTrans_url(CODE_GET_CHEW);
		
		log.info("PDA车位流程查询-查询可操作运单-开始");
		List<Map<String, Object>> yundList = new ArrayList<Map<String,Object>>();	//运单
		//根据条件查询出运单，包括排队表中的车位号
		yundList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kc_pda.queryYundChewLiucdyQuanx", paramMap);
		
		if (yundList == null || yundList.size()==0) {
			//该卡车状态不在“准入”到“放空”之间，故无法查出
			JSONObject json = new JSONObject();
			json.put("kach", "");
			json.put("chew", "");
			json.put("quybh", "");
			json.put("daztbh", "");
			paramMap.put("parameter", json);
			paramMap.put("response", PDATruckUtil.ERROR_TRANSFER);
			paramMap.put("respdesc", "该卡车号下无“准入”至“放空”之间的运单");
			paramMap.put("op_name", "操作");

			result = PDATruckUtil.gPdaTruck(paramMap);
			log.info("PDA车位流程查询-无可操作运单");
			
			//记录PDA日志
			sysLog.setTrans_content("PDA_车位流程查询-无可操作运单");
			userOperLog.addCorrectPDA(sysLog);
			
		}else {
			Map<String, Object> yundMap = yundList.get(0);	//运单
			if (!yundMap.containsKey("QUYQX") || yundMap.get("QUYQX") == null || yundMap.get("QUYQX").equals("") || !yundMap.get("QUYBH").equals(yundMap.get("QUYQX"))) {
				//没有区域权限
				JSONObject json = new JSONObject();
				json.put("kach", "");
				json.put("chew", "");
				json.put("quybh", "");
				json.put("daztbh", "");
				paramMap.put("parameter", json);
				paramMap.put("response", PDATruckUtil.ERROR_TRANSFER);
				paramMap.put("respdesc", "您没有该卡车所在区域的权限");
				paramMap.put("op_name", "操作");
				
				result = PDATruckUtil.gPdaTruck(paramMap);
				log.info("PDA车位流程查询-无区域权限");
				
				//记录PDA日志
				sysLog.setTrans_content("PDA_车位流程查询-无区域权限");
				userOperLog.addCorrectPDA(sysLog);
				
			}else {
				try {
					paramMap.put("op_name", yundMap.get("LIUCMC").toString());
					paramMap.put("response", PDATruckUtil.SUCCESS_TRANSFER);
					paramMap.put("respdesc", "查询车位成功");
					
					JSONObject json = new JSONObject();
					json.put("kach", yundMap.get("KACH").toString());
					json.put("chew", yundMap.get("CHEWBH").toString());
					json.put("quybh", yundMap.get("QUYBH").toString());
					json.put("daztbh", yundMap.get("DAZTBH").toString());
					paramMap.put("parameter", json);
					
					result = PDATruckUtil.gPdaTruck(paramMap);	
					
					//记录PDA日志
					sysLog.setTrans_content("PDA_车位流程查询-成功（卡车号:"+kach+"）");
					userOperLog.addCorrectPDA(sysLog);
					
					log.info("PDA车位流程查询-成功");
				} catch (Exception e) {
					//记录PDA日志
					sysLog.setTrans_content("PDA_车位流程查询-失败（卡车号:"+kach+"）");
					sysLog.setCclass(CommonUtil.getClassMethod());
					sysLog.setCexception(CommonUtil.replaceBlank(e.getMessage()));
					userOperLog.addErrorPDA(sysLog);
					
					//操作失败
					JSONObject json = new JSONObject();
					json.put("kach", "");
					json.put("chew", "");
					json.put("quybh", "");
					json.put("daztbh", "");
					paramMap.put("parameter", json);
					paramMap.put("response", PDATruckUtil.ERROR_TRANSFER);
					paramMap.put("respdesc", "查询车位失败");
					paramMap.put("op_name", "操作");
					
					result = PDATruckUtil.gPdaTruck(paramMap);
					log.info("PDA车位流程查询-失败");
				}
			}
		}
		log.info("PDA车位流程查询-结束");
		return result;
	}
	

/***********************************************************************************************/
	

	/**
	 * 获取区域，根据用户中心、用户账号有权限的所有区域		KC001
	 */
	public JSONObject getQuy(JSONObject param) {

		JSONObject result = new JSONObject();	//传给PDA的结果
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.putAll(param);
		
		//根据传入参数查询区域编号集合
		List<Map<String, Object>> listQuy = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kc_pda.getQuyByParam", paramMap);
		
		//PDATruck pdaTruck = new PDATruck();		//组装返回参数
		
		//根据条件设置不同的返回结果
		if (null != listQuy && listQuy.size()>0) {
			paramMap.put("response", PDATruckUtil.SUCCESS_TRANSFER);
			JSONObject json = new JSONObject();
			JSONArray jsonList = JSONArray.fromObject(listQuy);
			json.put("qylist", jsonList);
			paramMap.put("parameter", json);
			paramMap.put("respdesc", "区域查询成功");
		}else {
			JSONObject json = new JSONObject();
			JSONArray jsonList = new JSONArray();
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("quybh", "");
			jsonList.add(jsonParam);
			json.put("qylist", jsonList);
			paramMap.put("parameter", json);
			paramMap.put("response", PDATruckUtil.ERROR_TRANSFER);
			paramMap.put("respdesc", "区域查询为空");
		}
		
		result = PDATruckUtil.gPdaTruck(paramMap);
		
		return result;
	}

	/**
	 * 获取大站台，根据用户中心和区域编号		KC002
	 */
	public JSONObject getDazt(JSONObject param) {

		JSONObject result = new JSONObject();	//传给PDA的结果
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.putAll(param);
		
		//将parameter中的值放入map
		if (param.containsKey("parameter")) {
			JSONObject pjson = (JSONObject) param.get("parameter");
			if (pjson.containsKey("quybh")) {
				paramMap.put("quybh", pjson.get("quybh"));
			}
			paramMap.remove("parameter");
		}
		
		//根据传入参数查询大站台编号集合
		List<Map<String, Object>> listDazt = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kc_pda.getDaztByParam", paramMap);
		
		//PDATruck pdaTruck = new PDATruck();		//组装返回参数
		
		//根据条件设置不同的返回结果
		if (null != listDazt && listDazt.size()>0) {
			paramMap.put("response", PDATruckUtil.SUCCESS_TRANSFER);
			JSONObject json = new JSONObject();
			JSONArray jsonList = JSONArray.fromObject(listDazt);
			json.put("dztlist", jsonList);
			paramMap.put("parameter", json);
			paramMap.put("respdesc", "大站台查询成功");
		}else {
			JSONObject json = new JSONObject();
			JSONArray jsonList = new JSONArray();
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("daztbh", "");
			jsonList.add(jsonParam);
			json.put("dztlist", jsonList);
			paramMap.put("parameter", json);
			paramMap.put("response", PDATruckUtil.ERROR_TRANSFER);
			paramMap.put("respdesc", "大站台查询为空");
		}

		result = PDATruckUtil.gPdaTruck(paramMap);
		
		return result;
	}
	
}
