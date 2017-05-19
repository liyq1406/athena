package com.athena.pc.module.webInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
/**
 * <p>
 * Title:日滚动模拟服务调用
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
 * @date 2012-04-05
 */
@WebService(
		endpointInterface="com.athena.pc.module.webInterface.IPCDailyRollService",
		serviceName = "/pcdService")
@Component
public class PCDailyRollService extends BaseService implements IPCDailyRollService  {
	
	static Logger logger = Logger.getLogger(AbstractMonpcTemplate.class.getName());
	@Inject
	private DailyRollService dailyRollService;
	
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 定时调用日滚动模拟排产
	 * @date 2012-02-16
	 * @param bizJson 客户端业务请求json串
	 * @return 返回客户端需求json字符串
	 * @throws ServiceException
	 */
	public String callPcDailyRoll(String bizJson) throws ServiceException{

		Map<String,String> params = new HashMap<String,String>();
		Date date = new Date(); 
		SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd"); 
		params.put("today", fmat.format(date));
//		params.put("today", "2012-03-05");
//		params.put("GUND", "G");
		params.put("biaos", "R");
		params.put("jihybh", "4130");
		Map<String,String> uc = new HashMap<String,String>();
		try {
			Map<String, Map<String,String>> chanxz = getChanxzh(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryUserChanxz",params),uc);
			final Iterator<String> ucit = uc.keySet().iterator(); 
			Map<String,String> paramTemp = new  HashMap<String,String>();
			paramTemp.putAll(params);
			paramTemp.put("Dingdlx", "PJ");
			while(ucit.hasNext()){
				paramTemp.put("USERCENTER", ucit.next());
				dailyRollService.updateDingd(paramTemp);
			}
			final Iterator<String> it = chanxz.keySet().iterator(); 
			while(it.hasNext()){
				Map<String,String> cxh =(HashMap<String,String>)chanxz.get(it.next());
				params.put("chanxzbh", cxh.get("CHANXZBH"));
				params.put("USERCENTER", cxh.get("USERCENTER"));
				params.put("kaissj", params.get("today"));
				dailyRollService.calPC(params,new ArrayList<Map<String,String>>());
				userOperLog.addCorrect(CommonUtil.MODULE_PC, "日滚动模拟排产", "日滚动模拟排产成功");
			}
		} catch(Exception e) {
			userOperLog.addError(CommonUtil.MODULE_PC, "日滚动模拟计算错误", "日滚动模拟计算错误", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			logger.error("日滚动模拟计算错误，请修改参数:"+e.getMessage() );
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
	public Map<String, Map<String,String>> getChanxzh(final List<Map<String, String>> chanxz,Map<String,String> uc){
		final Map<String, Map<String,String>> result = new HashMap<String, Map<String,String>>();
		for (Map<String, String> cxz : chanxz){
			Map<String, String> temp = null;
			if (result.containsKey(cxz.get("CHANXZBH"))){
				temp = result.get(cxz.get("CHANXZBH"));
			} else {
				temp = new HashMap<String, String>();
			}
			if(temp.get("kaissj") == null ||(temp.get("kaissj") != null && temp.get("kaissj").compareTo(cxz.get("KAISSJ"))>0)){
				temp.put("kaissj",cxz.get("KAISSJ"));
			}
			if(temp.get("jiessj") == null ||(temp.get("jiessj") != null && temp.get("jiessj").compareTo(cxz.get("JIESSJ"))<0)){
				temp.put("jiessj",cxz.get("JIESSJ"));
			}
			uc.put(cxz.get("USERCENTER"), cxz.get("USERCENTER"));
			temp.put("USERCENTER", cxz.get("USERCENTER"));
			temp.put("CHANXZBH", cxz.get("CHANXZBH"));
			result.put(cxz.get("CHANXZBH"), temp);
		} 
		return result;
	}
}
