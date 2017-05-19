package com.athena.pc.module.action;


import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.pc.entity.Qickc;
import com.athena.pc.entity.Yuemn;
import com.athena.pc.module.service.RigdService;
import com.athena.pc.module.service.YueMnService;
import com.athena.pc.module.service.YuemnQueryService;
import com.athena.pc.module.webInterface.PCDailyProduceService;
import com.athena.pc.module.webInterface.PCScheduleService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;
import com.toft.mvc.interceptor.supports.log.Log;
import com.toft.mvc.utils.StringUtils;

/**
 * <p>
 * Title:月模拟业务处理类
 * </p>
 * <p>
 * Description:定义业务处理方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-2-13
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YuemnAction extends ActionSupport {
	//注入service
	@Inject
	private YuemnQueryService yuemnQueryService;
	@Inject
	private YueMnService yueMnService;
	
	@Inject
	private RigdService rigdService;
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private PCScheduleService pCScheduleService;
	
	@Inject
	PCDailyProduceService pCDailyProduceService;
	static Logger logger = Logger.getLogger(YuemnAction.class.getName());
	@Inject
	private DownLoadServices  downloadServices = null;
	/**
	 * 获取用户信息
	 * @return 用户信息
	 */
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	/**
	 * 页面跳转到设定期初库存页面
	 * @return String 跳转页面路径
	 */
	public String accessCansmn(@Param("gongyzq") String gongyzq){
		setResult("gongyzq",gongyzq);
		return "success";
	}
	
	/**
	 * 执行跳转页面方法
	 * @author 贺志国
	 * @date 2012-2-9
	 * @return String 返回跳转
	 */
	@Log(description="accessYuemn",content="{Toft_SessionKey_UserData.userName}执行了：跳转操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String accessYuemn(){
		logger.info("模拟排产开始");
		Map<String,String> param = this.getParams();
		String gongyzq = param.get("gongyzq");
		String chanxbh =  param.get("chanx");
		param.put("USERCENTER",loginUser.getUsercenter());
		param.put("jihybh", loginUser.getUsername()); 
		param.put("riq", DateUtil.curDateTime().substring(0, 10));
		//工业周期查询
		List<Map<String,String>>  gyzqList  = yuemnQueryService.selectGongyzq(param); 
		if (gyzqList.size() > 0) {
			setGongyzqParam(gyzqList,param,gongyzq);
			//查询计划员所在用户中心下的所有产线
			Map<String,String> postMap = loginUser.getPostAndRoleMap();
			String jihychanxz = postMap.get("PCJIHY");
			param.put("JIHYZBH", jihychanxz);
			List<Map<String,String>> chanxList = rigdService.selectChanx(param);
			selectChanxYuemn(chanxList,gyzqList,param,chanxbh);
		}  
		setResult("gongyzq",param.get("gongyzq"));
		setResult("chanxbh",chanxbh);
		setResult("param",param);
		return "select";
	}
	
	/**
	 * 设置工业周期参数
	 * @param gyzqList
	 * @param param
	 * @param gongyzq
	 */
	public void setGongyzqParam(List<Map<String,String>>  gyzqList,Map<String,String> param,String gongyzq){
		if(StringUtils.isEmpty(gongyzq)){//初始进入页面工业周期为NULL
			param.put("kaissj", gyzqList.get(0).get("KAISSJ"));
			param.put("jiessj", gyzqList.get(0).get("JIESSJ"));
			setResult("kaissj", gyzqList.get(0).get("KAISSJ"));
			setResult("jiessj", gyzqList.get(0).get("JIESSJ"));
			setResult("rili", param.get("kaissj"));
		}else{//根据工业周期号查询该工业周期的开始时间和结束时间
			List<Map<String,String>> gongyzqstart = null;
			if(param.get("start")!= null && param.get("start").length()>0){
				gongyzqstart = yuemnQueryService.selectGongyzqstart(param);
				if(gongyzqstart.size()>0){
					gongyzq = gongyzqstart.get(0).get("GONGYZQ");
					param.put("gongyzq", gongyzq);
				}
			}
			List<Map<String,String>> gyzqsjfw = yuemnQueryService.selectGyzqsjfw(gongyzq);
			param.put("kaissj", gyzqsjfw.get(0).get("KAISSJ"));
			param.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
			setResult("kaissj",gyzqsjfw.get(0).get("KAISSJ"));
			setResult("jiessj",gyzqsjfw.get(0).get("JIESSJ"));
			String rili = gongyzq.substring(0,4)+"-"+gongyzq.substring(4)+"-15";
			setResult("rili", rili);
			
			if(param.get("start")!= null && param.get("start").length()>0){
				param.put("start",param.get("kaissj"));
				param.put("end",param.get("jiessj"));
			}
		}
	} 
	
	/**
	 * 根据产线查询获得月模拟集合
	 * @param chanxList
	 * @param gyzqList
	 * @param param
	 * @param chanxbh
	 */
	public void selectChanxYuemn(List<Map<String,String>> chanxList,List<Map<String,String>>  gyzqList,Map<String,String> param,String chanxbh){
		if (chanxList.size() > 0) {
			if(StringUtils.isEmpty(chanxbh)){
				param.put("oppobj", chanxListToString(chanxList));
			}else{
				param.put("oppobj", "'"+chanxbh+"'");
			}
			//当前工业周期下的月模拟计划
			List<Yuemn> yuemnList =yuemnQueryService.selectYuemn(param);
			setResult("chanxList",chanxList);
			setResult("gyzqList",gyzqList);
			setResult("yuemnList",yuemnList);
		}
	}
	
	/**
	 * 月模拟计划确认
	 * @param gongyzq 工业周期 
	 * @return AJAX 确认是否成功  result>0：确认成功，否则事物回滚
	 */
	public String mnPaicSure(@Param("gongyzq") String gongyzq){
		
		Map<String,String> yuemnjhhParam = new HashMap<String,String>();
		Map<String, String> message = new HashMap<String,String>();
		yuemnjhhParam.put("USERCENTER",loginUser.getUsercenter());
		yuemnjhhParam.put("jihybh", loginUser.getUsername()); 
		yuemnjhhParam.put("yuemnjhh", yuemnjhhToString(gongyzq));
		try{
//			String jihychanxz = loginUser.getJihyz();
			Map<String,String> postMap = loginUser.getPostAndRoleMap();
			String jihychanxz = postMap.get("PCJIHY");
			yuemnjhhParam.put("JIHYZBH", jihychanxz);
			List<Map<String,String>> chanxList = rigdService.selectChanx(yuemnjhhParam);
			yuemnjhhParam.put("period", gongyzq);
			yuemnjhhParam.put("chanxzbh", chanxList.get(0).get("CHANXZBH"));
			yueMnService.calOutPut(yuemnjhhParam);
		} catch(Exception e){
			Message m =  new Message("youtnup_fail","i18n.pc.pc_message");
			message.put("message", m.getMessage());
			setResult("result",message);
			return AJAX;
		}
		int result = yuemnQueryService.updateYuedmnjhb(yuemnjhhParam);
		setResult("count",result);
		return AJAX;
	}
	
	/**
	 * 查询月模拟计划表，判断是否确认过
	 * @return String ajax
	 */
	public String queryShifqr(@Param("gongyzq") String gongyzq){
		Map<String,String> yuemnjhhParam = new HashMap<String,String>();
		yuemnjhhParam.put("yuemnjhh", yuemnjhhToString(gongyzq));
		List<Map<String,String>> shifqrList = yuemnQueryService.selectShifqrOfYuedmnjh(yuemnjhhParam);
		if(shifqrList.size() == 0){
			setResult("quernull","0");
		}else{
			logger.info("排产确认qr："+shifqrList.get(0).get("SHIFQR"));
			setResult("quernull","1");
			setResult("querzt",shifqrList.get(0).get("SHIFQR"));
		}
		return AJAX;
	}
	
	/**
	 * 将月模拟计划号拼成in能够接收的类型字符串
	 * @param gongyzq
	 * @param param 用户中心，计划员编号
	 * @return String 以','拼成的月模拟计划号
	 */
	public String yuemnjhhToString(String gongyzq){
		Map<String,String> param = new HashMap<String,String>();
		param.put("USERCENTER",loginUser.getUsercenter());
		param.put("jihybh", loginUser.getUsername()); 
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihychanxz = postMap.get("PCJIHY");
//		String jihychanxz = loginUser.getJihyz();
		param.put("JIHYZBH", jihychanxz);
		List<Map<String,String>> chanxList = rigdService.selectChanx(param);
		StringBuffer buf = new StringBuffer();
		String flag="";
		for( Map<String, String> shengcxMap : chanxList ){
			buf.append(flag).append("'").append(param.get("USERCENTER")+shengcxMap.get("SHENGCXBH")+gongyzq).append("'");
			flag=","; 
		}
		return buf.toString();
	}
	
	/**
	 * 产线集合转成String类型，用于in操作
	 * @param list 产线集合
	 * @return String 产线'L1','L2','L3'
	 */
	public String chanxListToString(List<Map<String,String>> list){
		StringBuffer buf = new StringBuffer();
		String flag="";
		for(Map<String,String> chanxMap : list){
			buf.append(flag).append("'").append(chanxMap.get("SHENGCXBH")).append("'");
			flag=",";
		}
		return buf.toString();
	}
	
	
	/**
	 * 得到月模拟排产参数并设置到Map中
	 * @param gongyzq 工业周期
	 * @return Map<String,String> 月模拟参数
	 */
	protected Map<String,String> getQickcParams(String gongyzq){
		Map<String,String> params = new HashMap<String,String>();
		try{
			String nextGongyzq = yuemnQueryService.selectNextGyzq(gongyzq);
			params.put("USERCENTER",loginUser.getUsercenter());
			params.put("jihybh", loginUser.getUsername()); 
	//		String jihychanxz = loginUser.getJihyz();
			Map<String,String> postMap = loginUser.getPostAndRoleMap();
			String jihychanxz = postMap.get("PCJIHY");
			params.put("JIHYZBH", jihychanxz);
			List<Map<String,String>> chanxList = rigdService.selectChanx(params);
			List<Map<String,String>> gyzqsjfw = yuemnQueryService.selectGyzqsjfw(gongyzq);
			List<Map<String,String>> nextGyzqsjfw = yuemnQueryService.selectGyzqsjfw(nextGongyzq);
			String kaissj = gyzqsjfw.get(0).get("KAISSJ");
			String currDate =  DateUtil.curDateTime().substring(0,10);
			params.put("kaissj", gyzqsjfw.get(0).get("KAISSJ"));
			if(kaissj.compareTo(currDate)<0){
				params.put("kaissj", currDate);
			}
			params.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
			params.put("nextjiessj", nextGyzqsjfw.get(0).get("JIESSJ"));
			params.put("shengcx", chanxListToString(chanxList));
			params.put("biaos", "Y");
			params.put("today", currDate);
			//当前计划员所在产线组
			params.put("chanxzbh", chanxList.get(0).get("CHANXZBH"));
			List<Map<String,String>> paiccsList = yuemnQueryService.selectPaiccs(params);
			if(paiccsList == null || paiccsList.size() == 0){
				Message m =  new Message("paicparams_err","i18n.pc.pc_message");
				params.put("errMessage", m.getMessage());
				return params;
			}
			BigDecimal b = new BigDecimal(String.valueOf(paiccsList.get(0).get("DAGDW")));
	//		double dagdw = b.doubleValue()/100;
			double dagdw = b.doubleValue();
			params.put("TIQQ", String.valueOf(paiccsList.get(0).get("TIQQ"))); 
			params.put("MINTIME", String.valueOf(dagdw));
			params.put("ZENGCTS", String.valueOf(paiccsList.get(0).get("ZENGCTS")));
			//当前工业周期
			params.put("period", gongyzq);
		} catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_PC, "模拟排产", "模拟参数出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			params = null;
		}	
		return params;
	}
	
	
	/**
	 * 查询期初库存
	 * @param gongyzq 工业周期
	 * @return AJAX
	 */
	public String queryQickc(@Param("gongyzq") String gongyzq){
		userOperLog.addCorrect(CommonUtil.MODULE_PC,"模拟排产","模拟排产开始,界面用户点击,查询期初库存");
		List<Map<String,String>> qckc = null;
		Map<String, String> message = new HashMap<String,String>();
		Map<String,String> para = this.getQickcParams(gongyzq);
		if(para == null ){
			Message m =  new Message("params_err","i18n.pc.pc_message");
			message.put("message", m.getMessage());
			setResult("result",message);
			return AJAX;
		}else if(para.get("errMessage") != null){
			message.put("message", para.get("errMessage"));
			setResult("result",message);
			return AJAX;
		}
		List<Map<String, String>> list = yueMnService.qiChuKuCun(para,qckc);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", list.size());
		map.put("rows", list);
		setResult("result", map);
		return AJAX;
	}
	
	
	/**
	 * 模拟排产
	 * @param gongyzq 工业周期
	 * @param qckclList 期初库存集合
	 * @return AJAX
	 */
	public String monPaic(@Param("gongyzq") String gongyzq,@Param("qckclList")ArrayList<Qickc> qckclList){
		userOperLog.addCorrect(CommonUtil.MODULE_PC,"模拟排产","模拟排产开始,界面用户点击,得到期初库存");
		List<Map<String, String>> qckcListMap = new ArrayList<Map<String, String>>();
		Map<String, String> message = new HashMap<String,String>();
		for(Qickc qckc : qckclList){
			Map<String, String> map = new HashMap<String,String>();
			map.put("SHIJ", qckc.getShij());
			map.put("LINGJBH", qckc.getLingjbh());
			map.put("LINGJSL", String.valueOf(qckc.getLingjsl()));
			map.put("ANQKC", String.valueOf(qckc.getAnqkc()));
			map.put("KCSL", String.valueOf(qckc.getKcsl()));
			map.put("MAOXQ", String.valueOf(qckc.getMaoxq()));
			map.put("QCKC", String.valueOf(qckc.getQickc()));
			qckcListMap.add(map);
		}
		Map<String,String> para = this.getQickcParams(gongyzq);
		if(para == null ){
			Message m =  new Message("params_err","i18n.pc.pc_message");
			message.put("message", m.getMessage());
			setResult("result",message);
			return AJAX;
		}else if(para.get("errMessage") != null){
			message.put("message", para.get("errMessage"));
			setResult("result",message);
			return AJAX;
		}
		try{
			Map<String,String> paraeTemp  = new HashMap<String,String>();
			paraeTemp.putAll(para);
			paraeTemp.put("Dingdlx", "PP");
			yueMnService.updateDingd(paraeTemp);
			userOperLog.addCorrect(CommonUtil.MODULE_PC,"模拟排产","模拟排产开始,界面用户点击,调用月模拟排产方法排产");
			yueMnService.calPC(para, qckcListMap);
			userOperLog.addCorrect(CommonUtil.MODULE_PC,"模拟排产","模拟排产开始,界面用户点击,调用月模拟排产方法排产结束");
		} catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_PC, "模拟排产", "模拟出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			String errorMessage = yuemnQueryService.selectErrorMessage(this.getQickcParams(gongyzq));
			String ee =e.getMessage();
			if(e.getMessage() != null){
				logger.info(ee);	
			}
			if((errorMessage==null||"".equals(errorMessage)) && ee == null){
				Message m =  new Message("paic_fail","i18n.pc.pc_message");
				message.put("message", m.getMessage());
				setResult("result",message);
			}else{
				String reMessage = (errorMessage == null || "".equals(errorMessage))? ee : errorMessage;
				setResult("result",new Message(reMessage));
			}
			return AJAX;
		}
		Message m =  new Message("paic_success","i18n.pc.pc_message");
		message.put("message",m.getMessage());
		setResult("result",message);
		return AJAX;
	}
	
	
	/**
	 * 查看工作日历详细
	 * @param chanx 产线
	 * @param gongzbh 工作编号
	 * @return String 跳转页面
	 */
	public String workCalendarDetail(@Param("chanx") String chanx,@Param("gongzbh") String gongzbh,@Param("gongyzq") String gongyzq){
		String riq =  "";
		try{
			riq = DateUtil.StringFormatWithLine(gongzbh.substring(gongzbh.length()-8));
		}catch(ParseException e){
			logger.error(e.getMessage());
		}
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("usercenter", loginUser.getUsercenter());
		paramMap.put("chanx", chanx);
		paramMap.put("riq", riq);
		//获得某条产线信息集合
		List<Map<String,String>> listMap = yuemnQueryService.selectChanxDetail(paramMap);
		//取得产线开始时间
		String kaissj = listMap.get(0).get("KAISSJ");
		//取得集合中最后一条记录的调整数
		String tiaozsj = listMap.get(listMap.size()-1).get("TIAOZSJ");
		//取得集合中最后一条记录的结束时间
		String jiezsj = listMap.get(listMap.size()-1).get("JIEZSJ");
		String jiessj = "";
		if(tiaozsj==null||"".equals(tiaozsj)||"0".equals(tiaozsj)){//调整时间为空或0直接得到当天工作的结束时间
			jiessj = riq+" "+jiezsj;
		}else{ ////判断调整时间大于1，加调整时间天数
			jiessj = DateUtil.dateAddDays(riq,Integer.parseInt(tiaozsj))+" "+jiezsj;
		}
		setResult("hour",listMap.get(0).get("HOUR"));
		setResult("kaissj",kaissj.substring(0,16));
		setResult("jiessj",jiessj.substring(0,16));
		setResult("chanx",chanx); //产线设置
		setResult("gongzbh",gongzbh);//工作编号设置
		setResult("gongyzq",gongyzq);//工业周期
		return "success";
	}
	
	
	/**
	 * 月模拟计划日产线零件查询
	 * @param gongzbh 工作编号
	 * @return AJAX 产线零件集合
	 */
	public String queryChanxLj(@Param("gongzbh") String gongzbh){
		List<Map<String,String>> list =  yuemnQueryService.selectChanxLj(gongzbh);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", list.size());
		map.put("rows", list);
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 错误消息
	 * @param gongyzq
	 * @return
	 */
	public String queryErrorMessage(@Param("chanx") String chanx,@Param("gongyzq") String gongyzq,@Param("gongzbh") String gongzbh){
		Map<String,String> param = new HashMap<String,String>();
		String riq = "";
		try{
			riq = DateUtil.StringFormatWithLine(gongzbh.substring(gongzbh.length()-8));
		}catch(ParseException e){
			logger.error(e.getMessage());
		}
//		List<Map<String,String>> gyzqsjfw = yuemnQueryService.selectGyzqsjfw(gongyzq);
		param.put("usercenter", loginUser.getUsercenter());
//		param.put("kaissj", gyzqsjfw.get(0).get("KAISSJ"));
//		param.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
		param.put("chanx", chanx);
		param.put("riq", riq);
		param.put("biaos", "Y");
		List<Map<String,String>> list = yuemnQueryService.selectAllErrorMessage(param);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", list.size());
		map.put("rows", list);
		setResult("result", map);
		return AJAX;
	}
	
	public String accessMessage(@Param("gongyzq") String gongyzq,@Param("chanx") String chanx){
		setResult("gongyzq",gongyzq);
		setResult("chanx",chanx);
		return "success";
	}
	
	/**
	 * 查询报警信息
	 * @param gongyzq 工业周期
	 * @return AJAX
	 */
	public String queryMessage(@Param Yuemn bean,@Param("gongyzq") String gongyzq,@Param("chanx") String chanx){
		Map<String,String> param = new HashMap<String,String>();
		List<Map<String,String>> gyzqsjfw = yuemnQueryService.selectGyzqsjfw(gongyzq);
		param.put("USERCENTER", loginUser.getUsercenter());
		param.put("kaissj", gyzqsjfw.get(0).get("KAISSJ"));
		param.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
		param.put("biaos", "Y");
		if(chanx.trim().length()>0){
			param.put("chanxh", chanx);
		}else{
			param.put("jihybh", loginUser.getUsername()); 
//			String jihychanxz = loginUser.getJihyz();
			Map<String,String> postMap = loginUser.getPostAndRoleMap();
			String jihychanxz = postMap.get("PCJIHY");
			param.put("JIHYZBH", jihychanxz);
			List<Map<String,String>> chanxList = rigdService.selectChanx(param);
			param.put("chanxhall", chanxListToString(chanxList));
		}
		Map<String,Object> map = yuemnQueryService.selectMessage(bean,param);
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("total", list.size());
//		map.put("rows", list);
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 查询报警信息
	 * @param gongyzq 工业周期
	 * @return AJAX
	 */
	public String monService(@Param Yuemn bean,@Param("gongyzq") String gongyzq,@Param("chanx") String chanx){
		Map<String,String> yuemnjhhParam = new HashMap<String,String>();
		yuemnjhhParam.put("USERCENTER",loginUser.getUsercenter());
		yuemnjhhParam.put("jihybh", loginUser.getUsername()); 
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihychanxz = postMap.get("PCJIHY");
		yuemnjhhParam.put("JIHYZBH", jihychanxz);
		List<Map<String,String>> chanxList = rigdService.selectChanx(yuemnjhhParam);
		
		String cxz ="";
		if(chanxList!=null && chanxList.size()>0 && chanxList.get(0).get("CHANXZBH")!=null ){
			String zz = chanxList.get(0).get("CHANXZBH");
			cxz = zz.length()>0 ? zz:"";
		}
		Map<String,String>  param = new HashMap<String,String>();
		param.put("PCCXZ", cxz);
		param.put("PCUC", loginUser.getUsercenter());
		pCScheduleService.pcSchedule("",param);
		return AJAX;
	}
	
	/**
	 * 查询报警信息
	 * @param gongyzq 工业周期
	 * @return AJAX
	 */
	public String rukmxService(@Param Yuemn bean,@Param("gongyzq") String gongyzq,@Param("chanx") String chanx){
		pCDailyProduceService.callPcDailyProduce("");
		return AJAX;
	}
	
	public String yuemnDownLoadFile(@Param("gongyzq") String gongyzq,@Param("chanx") String chanx) throws IOException{
				Map<String,String> param = this.getParams();
		Map<String, Object> dataSource;
		dataSource = new HashMap<String, Object>();
		HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
		try{
			param.put("USERCENTER",loginUser.getUsercenter());
			param.put("jihybh", loginUser.getUsername()); 
//			String jihychanxz = loginUser.getJihyz();
			Map<String,String> postMap = loginUser.getPostAndRoleMap();
			String jihychanxz = postMap.get("PCJIHY");
			param.put("JIHYZBH", jihychanxz);
			List<Map<String,String>> chanxList = rigdService.selectChanx(param);
			
			if(chanxList.size()==0){
				param.put("chanxhall", "''");
				param.put("chanxzbh", jihychanxz);
			}else{
				param.put("chanxhall", chanxListToString(chanxList));
				param.put("chanxzbh", chanxList.get(0).get("CHANXZBH"));
			}

			param.put("chanxFlag", "Y");
			dataSource.put("usercenter", param.get("USERCENTER"));
			dataSource.put("MAXCOLS", "16384");
			dataSource.put("MAXROWS", "1048576");
			dataSource.put("A1", "12");

			yuemnQueryService.getDownload(param,dataSource);
			if(dataSource.get("lingjcx") != null && dataSource.get("lingjnotcx") != null ){
				userOperLog.addCorrect(CommonUtil.MODULE_PC,"模拟排产","导出模拟排产数据正确");
			}
			downloadServices.downloadFile("MonthChanx.ftl", dataSource, response, "月模拟", ExportConstants.FILE_XLS, false) ;
		} catch(Exception e){
			logger.info(e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PC, "模拟排产", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
//			Message m =  new Message("download_fail","i18n.pc.pc_message");
			PrintWriter out = response.getWriter();
//			out.println("<script>parent.callback('"+"1111"+"')</script>");
			out.println("<script>alert('"+"导出失败"+"');</script>");
			out.flush();
			out.close();
			return "yuemndown";
		}
//		downloadServices.downloadFile("clearData.ftl", dataSource, response, "月模拟", ExportConstants.FILE_XLS, true) ;
		return "downLoad";
	}
}
