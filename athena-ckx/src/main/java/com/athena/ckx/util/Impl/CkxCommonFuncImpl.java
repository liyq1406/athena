package com.athena.ckx.util.Impl;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.athena.ckx.module.cangk.service.KuwService;
import com.athena.ckx.module.carry.service.CkxWulljService;
import com.athena.ckx.module.paicfj.service.UtilControlService;
import com.athena.ckx.module.transTime.service.CkxTempMonService;
import com.athena.ckx.module.transTime.service.CkxYunsskService;
import com.athena.ckx.module.workCalendar.service.CkxCalendarVersionService;
import com.athena.ckx.module.workCalendar.service.PcCalendarVersionService;
import com.athena.ckx.module.xuqjs.service.CkxXiaohcysskService;
import com.athena.ckx.module.xuqjs.service.CmjService;
import com.athena.ckx.module.xuqjs.service.TicxxsjService;
import com.athena.ckx.module.xuqjs.service.XXiaohcmbCkService;
import com.athena.ckx.module.xuqjs.service.XiaohcmbCkService;
import com.athena.ckx.util.CkxCommonFunc;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
@Component
@WebService(endpointInterface="com.athena.ckx.util.CkxCommonFunc",serviceName="/CkxCommonFunc")
public class CkxCommonFuncImpl implements CkxCommonFunc {
	@Inject
	private CkxTempMonService ckxTempMonService;
	@Inject
	private CkxYunsskService  ckxYunsskService;
	@Inject
	private CkxWulljService ckxWulljService;
	@Inject
	private CkxCalendarVersionService ckxCalendarVersionService;
	@Inject
	private TicxxsjService ticxxsjService;
	@Inject
	private CmjService cmjService;
	@Inject
	private CkxXiaohcysskService ckxXiaohcysskService;
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private  KuwService kuwService;
	@Inject
	private UtilControlService utilControlService;
	@Inject
	private PcCalendarVersionService pcCalendarVersionService;

	@Inject
	private XiaohcmbCkService xiaohcmbCkService;
	
	@Inject
	private XXiaohcmbCkService xxiaohcmbCkService;
	private  Logger logger =Logger.getLogger(CkxCommonFuncImpl.class);
	public String jisYunssk() {
		try {
			logger.info("调用批量-运输时刻模板定时生效--4210--开始");
			ckxTempMonService.dingsjisuan("4210");
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量运输时刻计算", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-运输时刻模板定时生效--4210--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量运输时刻计算", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}
		logger.info("调用批量-运输时刻模板 --4210 -- 结束");
		return "success";
	}	
	public String insertTimeOut() {
		try {
			logger.info("调用批量-运输时刻--4190--开始");
			ckxYunsskService.insertTimeOut("4190");
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量运输时刻", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-运输时刻--4190--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量运输时刻", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}	
		logger.info("调用批量-运输时刻 --4190 -- 结束");
		return "success";
	}
	 
	public String addWullj() {		
		try {
			logger.info("调用批量-物流路径--4030--开始");
			ckxWulljService.addWullj("4030");
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量物流路径", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-物流路径--4030--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量物流路径", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}	
		logger.info("调用批量-物流路径 --4030 -- 结束");
		return "success";
	}
	
	public String timingTask() {		
		try {
			logger.info("调用批量-日历版次(准备层)---- 4220 ---开始");
			ckxCalendarVersionService.timingTask();	
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量日历版次", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-日历版次(准备层)--4220--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量日历版次", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}	
		logger.info("调用批量-日历版次(准备层) --4220 -- 结束");
		return "success";
	}

	public String calculate() {		
		try {
			logger.info("调用批量-剔除休息时间--4020--开始");
			ticxxsjService.calculate("4020");
			logger.info("调用批量-剔除休息时间 --4020 -- 结束");
			logger.info("调用批量-时间合并-工作时间模板--4020--开始");
			ckxCalendarVersionService.mergeTime("4020");
			ticxxsjService.calculateTicxxsjTemp("4020");
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量剔出休息时间", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-剔除休息时间--4020--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量4020", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}	
		logger.info("调用批量-时间合并-工作时间模板--4020--结束");
		return "success";
	}

	public String calculateCmj() {		
		try {
			logger.info("调用批量-cmj--4180--开始");
			cmjService.calculateCmj("4180");
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量cmj", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-cmj--4180--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量cmj", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}
		logger.info("调用批量-cmj--4180--结束");
		return "success";
	}

	public String calculateXiaohcYssk() {		
		try {
			logger.info("调用批量-小火车运输时刻---4040---开始");
			ckxXiaohcysskService.calculateXiaohcYssk("4040");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量小火车运输时刻", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-小火车运输时刻--4040--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量小火车运输时刻", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}
		logger.info("调用批量-小火车运输时刻--4040--结束");
		return "success";
	}
	
	public String updateUtilControlBiaos() {
		String error = "";
		try {
			logger.info("调用批量-批量更新--4050---开始");
			error = utilControlService.updateUtilControlBiaos("4050");
			if(!"批量更新状态成功".equals(error)){
				throw new ServiceException("批量更新出错:"+error);				
			}
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量更新状态", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-批量更新--4050--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量更新状态", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}
		
		if("".equals(error)){
			error = "success";
		}
		logger.info("调用批量-批量更新--4050--结束");
		return error;
	}
	public String clearVersion(){
		try {
			logger.info("调用批量-清除日历版次(执行层)----4230---开始");		
			pcCalendarVersionService.clearVersion();
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量排产日历版次", "清除成功");
		} catch (Exception e) {
			logger.error("调用批量-清除日历版次(执行层)--4230--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量排产日历版次", "清除失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}
		logger.info("调用批量-清除日历版次(执行层)----4230-----结束");		
		return "success";
	}
	public String updateWeilbzhTobianzh(){
		String error = "";
		try {
			logger.info("调用批量-批量更新(执行层)--4240 --开始");
			Message m = new Message("pilgxztcg","i18n.ckx.paicfj.i18n_message");
			error = utilControlService.updatePCCalendarBianzh("4240");
			if(!m.getMessage().equals(error)){
				throw new ServiceException("批量更新出错:"+error);
			}
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量更新状态", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-批量更新(执行层)--4240--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量更新状态（执行层）", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}		
		if("".equals(error)){
			error = "success";
		}
		logger.info("调用批量-批量更新(执行层)--4240--结束");
		return error;
	}
	public String autoUpdateXiaohc(){
		String error = "";
		try {
			logger.info("调用批量-自动根据将来参数生效日赋值将来参数--4260 --开始");
			error = xiaohcmbCkService.autoUpdateXiaohc("4260");
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量更新状态", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-自动根据将来参数生效日赋值将来参数--4260 --开始"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "自动根据将来参数生效日赋值将来参数--4260", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}		
		if("".equals(error)){
			error = "success";
		}
		logger.info("调用批量-小火车模板计算(执行层)--4260 --结束");
		return error;
	}
	public String calculateXiaohcmbCk(){
		String error = "";
		try {
			logger.info("调用批量-小火车模板计算(执行层)--4270 --开始");
			error = xiaohcmbCkService.calculate("4270");
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量更新状态", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-小火车模板计算(执行层)--4270 --开始"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车模板计算(执行层)--4270", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}		
		if("".equals(error)){
			error = "success";
		}
		logger.info("调用批量-小火车模板计算(执行层)--4270 --结束");
		return error;
	}
	
	//新的计算小火车模板，添加小火车日期字段 add by zbb 2016-1-11
	public String calculateXXiaohcmbCk(){
		String error = "";
		try {
			logger.info("调用批量-小火车模板计算(执行层)--4271 --开始");
			error = xxiaohcmbCkService.calculate("4271");
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "批量更新状态", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-小火车模板计算(执行层)--4271 --开始"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车模板计算(执行层)--4271", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}		
		if("".equals(error)){
			error = "success";
		}
		logger.info("调用批量-小火车模板计算(执行层)--4271 --结束");
		return error;
	}
//	
//	public String addTempLingjpztj(){
//		try{
//			logger.info("调用批量-零件品种统计");
//			lingjpztjService.addTempLingjpztj();
//		}catch (Exception e) {
//			logger.error("调用批量-零件品种统计"+e.toString());
//			throw new ServiceException(e.toString());
//		}
//		return "success";
//	}
//	
//	
//	public String timingAddLingjxhdxh() {
//		try{
//			logger.info("调用批量-零件消耗点循环");
//			lingjxhdxhService.timingAddLingjxhdxh();
//		}catch (Exception e) {
//			logger.error("调用批量-零件消耗点循环"+e.toString());
//			throw new ServiceException(e.toString());
//		}
//		return "success";
//	}
//
//	public String timingAddRepJicsjhz() {
//		try{
//			logger.info("调用批量-基础数据汇总");
//			jicsjhzService.addRepJicsjhz();
//		}catch (Exception e) {
//			logger.error("调用批量-基础数据汇总"+e.toString());
//			throw new ServiceException(e.toString());
//		}
//		return "success";
//	}
	
	
	@Override
	public String calulateHebTime() {
		try {
			logger.info("调用批量-时间合并-工作时间模板---4280---开始");
			ckxCalendarVersionService.mergeTime("4280");
			ticxxsjService.calculateTicxxsjTemp("4280");
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "时间合并-工作时间模板", "计算成功");
		} catch (Exception e) {
			logger.error("调用批量-时间合并-工作时间模板--4280--错误"+e.toString());
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "时间合并-工作时间模板", "计算失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}
		logger.info("调用批量-时间合并-工作时间模板--4280--结束");
		return "success";
	}
	
	public String updatekuwbzxx() {		
		try {
			logger.info("调用批量-更新库位包装信息--4290--开始");
			kuwService.updatekuwbzxx();
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "更新库位包装信息", "更新成功");
		} catch (Exception e) {
			logger.error("调用批量-更新库位包装信息--4290--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "更新库位包装信息", "更新失败,"+(e.toString().length()>1980?e.toString().substring(0,1980):e.toString()));
			throw new ServiceException(e.toString());
		}	
		logger.info("调用批量-更新库位包装信息--4290-- 结束");
		return "success";
	}
}
