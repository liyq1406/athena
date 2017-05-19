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
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
/**
 * <p>
 * Title:累计交付差额接口服务
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
		endpointInterface="com.athena.pc.module.webInterface.IPCLeijjfService",
		serviceName = "/pclService")
@Component
public class PCLeijjfService extends BaseService implements IPCLeijjfService  {
	
	static Logger logger = Logger.getLogger(AbstractMonpcTemplate.class.getName());
//	@Inject
//	private YueMnService yueMnService;
	
	@Inject
	private UserOperLog userOperLog;
	
//	@Inject
//	private BackgroundRunLog backgroundRunLog;
	/**
	 * 定时调用滚动月模拟排产
	 * @date 2012-02-16
	 * @param bizJson 客户端业务请求json串
	 * @return 返回客户端需求json字符串
	 * @throws ServiceException
	 */
	public String callPcSchedule(String bizJson) throws ServiceException{

		Map<String,String> params = new HashMap<String,String>();

		params.put("today", DateUtil.getCurrentDate());
		params.put("todayhms", DateUtil.curDateTime());
		params.put("jiessjhms", DateUtil.curDateTime());
		
//		params.put("today", "2012-07-03");
//		params.put("todayhms", "2012-07-03 03:03:00");
//		params.put("jiessjhms", "2012-07-03 03:03:00");
		
		params.put("kaissjhms", DateUtil.DateSubtractHours(params.get("jiessjhms"),24));
		params.put("kaissj", params.get("kaissjhms").substring(0, 10));
		

		params.put("jihybh", "4120");
		Map<String, Map<String,String>> chanxz = getChanxzh(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("leijce.queryProduceUserChanxz",params));
		final Iterator<String> it = chanxz.keySet().iterator(); 
		while(it.hasNext()){
			Map<String,String> cxh =(HashMap<String,String>)chanxz.get(it.next());
			params.put("chanxzbh", cxh.get("CHANXZBH"));
			params.put("USERCENTER", cxh.get("USERCENTER"));
			try {
				List<Map<String, String>> shengxList = getChangxList(params);
				String flag = "";
				final StringBuffer lStrChanx = new StringBuffer();
				for (Map<String, String> changx : shengxList) {
					lStrChanx.append(flag).append('\'').append(changx.get("SHENGCXBH").toString()).append('\'');
					flag = ",";
				}
				params.put("shengcx", lStrChanx.toString());
				calLeijjf(params);
				userOperLog.addCorrect(CommonUtil.MODULE_PC, "累计交付差额计算", "累计交付差额计算成功");
			} catch(Exception e) {
				userOperLog.addError(CommonUtil.MODULE_PC, "累计交付差额计算", "累计交付差额计算", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
				logger.error("累计交付计算错误，请修改参数:"+e.getMessage() );
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
	
	/**
	 * @description 计算累计交付差额
	 * @author 王国首
	 * @date 2011-12-26
	 * @param params    包含产线和排产时间范围
	 * @param dateRange 查询的时间范围
	 * @return          各个产线的工作日历
	 */
	@SuppressWarnings("unchecked")
	public void calLeijjf(final Map<String,String> params){
//		final Map<String, Map<String,String>> result = new HashMap<String, Map<String,String>>();
		params.put("lastDate", DateUtil.dateAddDays(params.get("kaissj"), -1));
		List<Map<String,String>> lastDate = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("leijce.queryLastDate",params);
		if(lastDate.size()>0){
			params.put("lastDate", lastDate.get(0).get("SHIJ"));
			if(params.get("lastDate").compareTo(params.get("kaissj"))>=0){
				return;
			}
		}
		List<Map<String,String>> leijce = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("leijce.queryLeijfceb",params);
		List<Map<String,String>> sjjf = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("leijce.querySjjf",params);
		for(Map<String,String> sjjfMap : sjjf){
			boolean flag = false;
			for(Map<String,String> leijceMap : leijce){
				if(leijceMap.get("LINGJBH").equals(sjjfMap.get("LINGJBH"))){
					double chae = Double.parseDouble(String.valueOf(leijceMap.get("LEIJJFCE")))-Double.parseDouble(String.valueOf(sjjfMap.get("SJJF")));
					leijceMap.put("LEIJJFCE", String.valueOf(chae));
					leijceMap.put("SJJF", String.valueOf(sjjfMap.get("SJJF")));
					flag = true;
					break;
				}
			}
			if(!flag){
				Map<String,String> leijceMapTemp = new HashMap<String,String>();
				leijceMapTemp.putAll(sjjfMap);
				leijceMapTemp.put("LEIJJFCE", String.valueOf(0-Double.parseDouble(String.valueOf(leijceMapTemp.get("SJJF")))));
				leijce.add(leijceMapTemp);
			}
		}
		List<Map<String,String>> maoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("leijce.queryMaoxq",params);
		for(Map<String,String> maoxqMap : maoxq){
			boolean flag = false;
			for(Map<String,String> leijceMap : leijce){
				if(leijceMap.get("LINGJBH").equals(maoxqMap.get("LINGJBH"))){
					double chae = Double.parseDouble(String.valueOf(leijceMap.get("LEIJJFCE")))+Double.parseDouble(String.valueOf(maoxqMap.get("MAOXQ")));
					leijceMap.put("LEIJJFCE", String.valueOf(chae));
					leijceMap.put("MAOXQ", String.valueOf(maoxqMap.get("MAOXQ")));
					flag = true;
					break;
				}
			}
			if(!flag){
				Map<String,String> leijceMapTemp = new HashMap<String,String>();
				leijceMapTemp.putAll(maoxqMap);
				leijceMapTemp.put("LEIJJFCE", String.valueOf(leijceMapTemp.get("MAOXQ")));
				leijce.add(leijceMapTemp);
			}
		}
		for(Map<String,String> leijceTemp : leijce){
			if(Double.parseDouble(String.valueOf(leijceTemp.get("LEIJJFCE"))) > 0 || Double.parseDouble(String.valueOf(leijceTemp.get("LEIJJFCE"))) < 0){
				leijceTemp.put("todayhms", params.get("todayhms"));
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("leijce.insertLeijce",leijceTemp);
			}

		}
	}
	
	/**
	 * @description 得到产线组编号
	 * @author 王国首
	 * @date 2011-12-26
	 * @param params    包含产线和排产时间范围
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getChangxList(final Map<String,String> params) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("leijce.queryChanx",params); 
	} 
}
