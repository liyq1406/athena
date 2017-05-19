package com.athena.truck.util.Impl;

import javax.jws.WebService;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.athena.truck.module.kacPDA.service.KacPDAFangkService;
import com.athena.truck.util.PDATruckUtil;
import com.athena.truck.util.PDATruckWebservice;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * PDA webservice 实现类
 * @author CSY
 *
 */
@WebService(endpointInterface="com.athena.truck.util.PDATruckWebservice", serviceName="/pDATruckWebservice")
@Component
public class PDATruckWebserviceImpl implements PDATruckWebservice {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private KacPDAFangkService kacPDAFangk;
	
	static Logger log = Logger.getLogger(PDATruckWebserviceImpl.class);
	
	private String CODE_GET_QUY = "KC001";	//获取区域
	private String CODE_GET_DAZT = "KC002";	//获取大站台
	private String CODE_GET_CHEW = "KC003";	//获取车位
	private String CODE_DO_FANGK = "KC004";	//车位放空

	/**
	 * 根据交易码调用不同接口
	 */
	@Override
	public String PDATruckService(String params) {
		
		JSONObject json = new JSONObject();	//返回结果
		
		try {
			JSONObject param = JSONObject.fromObject(params);
			
			if (param.get("trscode").equals(CODE_GET_QUY)) {
				log.info("车位放空PDA-获取区域-开始");
				json = kacPDAFangk.getQuy(param);
				log.info("车位放空PDA-获取区域-结束");
			}else if (param.get("trscode").equals(CODE_GET_DAZT)) {
				log.info("车位放空PDA-获取大站台-开始");
				json = kacPDAFangk.getDazt(param);
				log.info("车位放空PDA-获取大站台-结束");
			}else if (param.get("trscode").equals(CODE_GET_CHEW)) {
				log.info("车位放空PDA-获取车位-开始");
				json = kacPDAFangk.getChew(param);
				log.info("车位放空PDA-获取车位-结束");
			}else if (param.get("trscode").equals(CODE_DO_FANGK)) {
				log.info("车位放空PDA-流程操作-开始");
				json = kacPDAFangk.updateYundChew(param);
				log.info("车位放空PDA-流程操作-结束");
			}
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage());
			json = JSONObject.fromObject(params);
			json.put("respdesc", "卡车PDA服务出现异常");
			json.put("response", PDATruckUtil.ERROR_TRANSFER);
			json = PDATruckUtil.gPdaTruck(json);
		}
		
		return json.toString();
		
	}

}
