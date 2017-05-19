package com.athena.pc.module.webInterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.pc.module.service.AbstractMonpcTemplate;
import com.athena.pc.module.service.DailyRollService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
/**
 * <p>
 * Title:定时调用更新入库明细到时间生产数量
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
 * @date 2012-05-16
 */
@WebService(
		endpointInterface="com.athena.pc.module.webInterface.IPCDailyProduceService",
		serviceName = "/pcpService")
@Component
public class PCDailyProduceService extends BaseService implements IPCDailyProduceService  {
	
	static Logger logger = Logger.getLogger(AbstractMonpcTemplate.class.getName());
	@Inject
	private DailyRollService dailyRollService;
	
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 定时调用更新入库明细到时间生产数量
	 * @date 2012-05-16
	 * @param bizJson 客户端业务请求json串
	 * @return 返回客户端需求json字符串
	 * @throws ServiceException
	 */
	public String callPcDailyProduce(String bizJson) throws ServiceException{

		Map<String,String> params = new HashMap<String,String>();
		params.put("today", DateUtil.getCurrentDate());
		params.put("todayhms", DateUtil.curDateTime());
		params.put("jiessjhms", DateUtil.curDateTime());
		params.put("kaissj", DateUtil.dateAddDays(params.get("today"),-1));
//		params.put("today", "2012-03-05");
		params.put("jihybh", "4150");
		Map<String, Map<String,String>> chanxz = getChanxzh(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryProduceUserChanxz",params));
		final Iterator<String> it = chanxz.keySet().iterator(); 
		while(it.hasNext()){
			Map<String,String> cxh =(HashMap<String,String>)chanxz.get(it.next());
			params.put("chanxzbh", cxh.get("CHANXZBH"));
			params.put("USERCENTER", cxh.get("USERCENTER"));
			try {
				dailyRollService.calDailyProduce(params);
				userOperLog.addCorrect(CommonUtil.MODULE_PC, "更新入库明细计算", "更新入库明细成功");
			} catch(Exception e) {
				userOperLog.addError(CommonUtil.MODULE_PC, "更新入库明细计算错误", "更新入库明细计算错误", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
				logger.error("更新入库明细计算错误，请修改参数:" +e.getMessage());
			}
		}
		return null;
	}
	
	/**
	 * @description 根据时间范围及产线查询各个产线的工作日历
	 * @author 王国首
	 * @date 2011-12-26
	 * @param params    包含产线和排产时间范围
	 * @param dateRange 查询的时间范围
	 * @return          各个产线的工作日历
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String,String>> getChanxzh(final List<Map<String, String>> chanxz){
		final Map<String, Map<String,String>> result = new HashMap<String, Map<String,String>>();
		for (Map<String, String> cxz : chanxz){
			Map<String, String> temp = null;
			if (result.containsKey(cxz.get("CHANXZBH"))){
				temp = result.get(cxz.get("CHANXZBH"));
			} else {
				temp = new HashMap<String, String>();
			}
			temp.put("USERCENTER", cxz.get("USERCENTER"));
			temp.put("CHANXZBH", cxz.get("CHANXZBH"));
			result.put(cxz.get("CHANXZBH"), temp);
		} 
		return result;
	}
}
