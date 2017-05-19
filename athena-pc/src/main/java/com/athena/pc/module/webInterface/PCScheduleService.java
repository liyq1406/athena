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
import com.athena.pc.module.service.YueMnService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
/**
 * <p>
 * Title:滚动月模拟服务调用
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
		endpointInterface="com.athena.pc.module.webInterface.IPCScheduleService",
		serviceName = "/pcmService")
@Component
public class PCScheduleService extends BaseService implements IPCScheduleService  {
	
	static Logger logger = Logger.getLogger(AbstractMonpcTemplate.class.getName());
	@Inject
	private YueMnService yueMnService;
	
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
		Map<String,String> p = new HashMap<String,String>();
		pcSchedule(bizJson,p);
		return null;
	}
	
	public String pcSchedule(String bizJson,Map<String,String> pa){
		Map<String,String> params = new HashMap<String,String>();
		Date date = new Date(); 
		SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd"); 
		params.put("today", fmat.format(date));
//		params.put("GUND", "G");
//		params.put("biaos", "Y");
		params.put("biaos", "G");
		params.put("jihybh", "4140");
		if(pa.get("PCCXZ")!=null && pa.get("PCCXZ").length()>0){
			params.put("PCCXZ", pa.get("PCCXZ"));
			params.put("PCUC", pa.get("PCUC"));
		}
		Map<String,String> uc = new HashMap<String,String>();
		Map<String, Map<String,String>> chanxz = getChanxzh(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryUserChanxz",params),uc);
		final Iterator<String> ucit = uc.keySet().iterator(); 
		Map<String,String> paramTemp = new  HashMap<String,String>();
		paramTemp.putAll(params);
		while(ucit.hasNext()){
			paramTemp.put("USERCENTER", ucit.next());
			try {
				yueMnService.updateDingd(paramTemp);
			} catch(Exception e) {
				userOperLog.addError(CommonUtil.MODULE_PC, "滚动模拟排产", "滚动模拟更新订单表出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
				logger.error("滚动月模拟计算错误，请修改参数" );
			}	
		}
		final Iterator<String> it = chanxz.keySet().iterator(); 
		while(it.hasNext()){
			Map<String,String> cxh =(HashMap<String,String>)chanxz.get(it.next());
			params.put("chanxzbh", cxh.get("CHANXZBH"));
			List<Map<String, String>> gongyzq  =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("monpc.queryGongyzq",cxh);
			params.put("USERCENTER", cxh.get("USERCENTER"));
			List<Map<String,String>> Qckc = new ArrayList<Map<String,String>>();
			try {
				userOperLog.addCorrect(CommonUtil.MODULE_PC, "滚动模拟排产", "滚动模拟排产开始");
				for(int i = 0 ;i<gongyzq.size();i++){
					Map<String, String> oneGongyzq = gongyzq.get(i);
					if(0 == i){
						params.put("kaissj", fmat.format(date));
					}else{
						params.put("kaissj", oneGongyzq.get("KAISSJ"));
					}
					params.put("jiessj", oneGongyzq.get("JIESSJ"));
					params.put("nextjiessj", (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("monpc.queryNextGyzqJiessj",oneGongyzq.get("GONGYZQ")));
					params.put("period", oneGongyzq.get("GONGYZQ"));
					List<Map<String, String>> chanxzList  =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryPaiccs",params);
					params.put("TIQQ", String.valueOf(chanxzList.get(0).get("TIQQ"))); 
					params.put("MINTIME", String.valueOf(Double.parseDouble(String.valueOf(chanxzList.get(0).get("DAGDW")))/100));
					params.put("ZENGCTS", String.valueOf(chanxzList.get(0).get("ZENGCTS")));
					yueMnService.calPC(params,Qckc);
				}
				userOperLog.addCorrect(CommonUtil.MODULE_PC, "滚动模拟排产", "滚动模拟排产成功");
			} catch(Exception e) {
				userOperLog.addError(CommonUtil.MODULE_PC, "滚动模拟排产", "滚动模拟出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
				logger.error("滚动月模拟计算错误，请修改参数:" +e.getMessage());
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
