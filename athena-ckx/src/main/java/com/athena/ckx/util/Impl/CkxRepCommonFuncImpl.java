package com.athena.ckx.util.Impl;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.athena.ckx.module.baob.service.BeihlgzService;
import com.athena.ckx.module.baob.service.Faycg100tService;

import com.athena.ckx.module.baob.service.KaixrukjizxService;

import com.athena.ckx.module.baob.service.LingjxhdxhService;
import com.athena.ckx.util.CkxRepCommonFunc;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
@Component
@WebService(endpointInterface="com.athena.ckx.util.CkxRepCommonFunc",serviceName="/CkxRepCommonFunc")
public  class CkxRepCommonFuncImpl implements CkxRepCommonFunc {
	
	
	@Inject
	private LingjxhdxhService lingjxhdxhService;

	@Inject
	private BeihlgzService beihlgzService;
	@Inject
	private KaixrukjizxService kaixrukjizxService;
	@Inject
	private Faycg100tService  faycg100tService;
	
	private  Logger logger =Logger.getLogger(CkxRepCommonFuncImpl.class);
	
	
	public String timingAddLingjxhdxh() {
		try{
			logger.info("调用批量-零件消耗点循环");
			lingjxhdxhService.timingAddLingjxhdxh();
		}catch (Exception e) {
			logger.error("调用批量-零件消耗点循环"+e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		return "success";
	}

	
	@Override
	public String timingDeleteRepBeihlgz() {
		try{
			logger.info("调用批量-备货令跟踪");
			beihlgzService.timingDeleteBeihlgz();
		}catch (Exception e) {
			logger.error("调用批量-备货令跟踪"+e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		return "success";
	}
	
	
	public String timingAddkaixruk() {
		try{
			logger.info("调用批量-开箱入库集装箱");
			kaixrukjizxService.timingAddkaixruk();
		}catch (Exception e) {
			logger.error("调用批量-开箱入库集装箱"+e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		return "success";
	}
	
	public String timingAddfaycg100t() {
		try{
			logger.info("调用批量-开箱入库集装箱");
			faycg100tService.timingAddFaycg100t();
		}catch (Exception e) {
			logger.error("调用批量-开箱入库集装箱"+e.getMessage());
			throw new ServiceException(e.getMessage());
		}
		return "success";
	}
	
}
