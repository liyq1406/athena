package com.athena.fj.module.service;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.fj.module.interfaces.IFJScheduleService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
/**
 * <p>
 * Title:发交定时更新调用
 * </p>
 * <p>
 * Description:此类用来发布服务后供接口调用
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 王国首
 * @E-mail gswang@isoftstone.com
 * @version v1.0
 * @date 2012-02-17
 */
@WebService(
		endpointInterface="com.athena.fj.module.interfaces.IFJScheduleService",
		serviceName = "/fjService")
@Component
public class FJScheduleService extends BaseService implements IFJScheduleService  {
	
	static Logger logger = Logger.getLogger(FJScheduleService.class.getName());
	
	@Inject
	private UserOperLog userOperLog;
	
//	@Inject
//	private BackgroundRunLog backgroundRunLog;
	
	/**
	 * 定时调用更新仓库子仓库和配载状态。
	 * @date 2012-02-16
	 * @param bizJson 客户端业务请求json串
	 * @return 返回客户端需求json字符串
	 * @throws ServiceException
	 */
	public String scheduleRun(String bizJson) throws ServiceException{
		try {
			//此方法转移到接口中去执行。      gswang 2013-12-05
//			Map<String, String> params = new HashMap<String, String>();
//			int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("fjschedule.updateJiaofj",params);
//			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载发交", "配载发交更新交付时间成功，更新条数:"+num);
//			logger.info("配载发交更新交付时间成功，更新条数:"+num);
//			num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("fjschedule.updateCangkh",params);
//			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载发交", "配载发交更新仓库子仓库成功，更新条数:"+num);
//			logger.info("配载发交更新仓库子仓库成功，更新条数:"+num);
//			num =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("yaocjh.updateYHLPZ");
//			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载发交", "配载发交更新配载状态成功，更新条数:"+num);
//			logger.info("配载发交更新配载状态成功，更新条数:"+num);
		} catch(Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "配载发交", "配载发交更新仓库子仓库出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			logger.error("配载发交更新仓库子仓库错误"+e.getMessage() );
		}
		return null;
	}
}
