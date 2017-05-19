package com.athena.pc.module.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.athena.pc.common.CollectionUtil;
import com.athena.pc.entity.Rigdpcmx;
import com.athena.pc.module.service.DailyRollService;
import com.athena.pc.module.service.RigdService;
import com.athena.pc.module.webInterface.PCLeijjfService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ActionException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;
import com.toft.mvc.interceptor.supports.log.Log;
/**
 * <p>
 * Title:日滚动排产处理类
 * </p>
 * <p>
 * Description:定义日滚动排产处理方法
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
 * @date 2012-3-7
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class RigdAction extends ActionSupport {

	@Inject
	private RigdService rigdService;
	
	@Inject
	private DailyRollService dailyRollService; 
	
	@Inject
	private PCLeijjfService pCLeijjfService;
	
	@Inject
	private DownLoadServices  downloadServices = null;
	/**
	 * 获取用户信息
	 */	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	
	static Logger logger = Logger.getLogger(RigdAction.class.getName());
	
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 页面跳转
	 * @author 贺志国
	 * @date 2012-03-07
	 * @return String 跳转后的页面
	 */
	@Log(description="accessRigd",content="{Toft_SessionKey_UserData.userName}执行了：跳转操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String accessRigd(){
		Map<String,String> param = this.getParams();
		param.put("USERCENTER",loginUser.getUsercenter());
		param.put("jihybh", loginUser.getUsername()); 
		param.put("riq", DateUtil.curDateTime().substring(0, 10));
//		String jihychanxz = loginUser.getJihyz();
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihychanxz = postMap.get("PCJIHY");
		userOperLog.addCorrect(CommonUtil.MODULE_PC, "日滚动排产计划员组", "日滚动排产计划员组"+jihychanxz);
		param.put("JIHYZBH", jihychanxz);
		List<Map<String,String>> chanxList = rigdService.selectChanx(param);
		String shengcx = "''";
		if(chanxList.size()>0){
			shengcx = chanxListToString(chanxList);
			setResult("chanxz", chanxList.get(0).get("CHANXZBH"));
		}
		param.put("shengcx", shengcx);
		String chanxhJson = CollectionUtil.listToJson(chanxList, "SHENGCXBH", "SHENGCXBH");
		List<Map<String,String>> banList = rigdService.selectBanc(param);
		setResult("bancnum",String.valueOf(banList.size()));
		StringBuffer banString = new StringBuffer();
		StringBuffer bancString = new StringBuffer();
		String flag = "";
		for(int i = 0; i<banList.size();i++){
			banString.append(flag).append(banList.get(i).get("BAN"));
			bancString.append(flag).append("BAN").append(i+1);
			flag = ",";
		}
		setResult("banString",banString.toString());
		setResult("bancString",bancString.toString());
		setResult("banList",banList);
		setResult("shengcx", chanxListToStringWithSplit(chanxList));
		setResult("chanxhJson",chanxhJson);
		return "select";
	}
	
	/**
	 * 日滚动排产明细查询
	 * @author gswang
	 * @date 2012-04-12
	 * @param bean 日滚动排产计划明细实体类
	 * @return String AJAX
	 */
	@Log(description="queryRigd",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：查询操作出现异常")
	public String queryRigd(@Param Rigdpcmx bean){
		Map<String,String> param = this.getParams();
		if(param.get("chanxz") == null){
			Map<String,String> msgMap = new HashMap<String,String>();
				msgMap.put("message", "计划员所对应的产线组，生产线有误，请检查");
			setResult("result",msgMap);
			return AJAX;
		}
		if(param.get("chanxh") != null && param.get("chanxh").trim().length()>0){
			param.put("chanxh", param.get("chanxh"));
		}else{
			List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
			param.put("jihybh", loginUser.getUsername()); 
			param.put("chanxhall",CollectionUtil.listToString(chanxlist));
		}
		param.put("USERCENTER",loginUser.getUsercenter());
		if(param.get("banString") != null){
			String []banString = param.get("banString").split(",");
			StringBuffer sql = new StringBuffer();
			StringBuffer sqlName = new StringBuffer();
			if(banString.length>0){
//				for( String bc : banString){
				for(int i = 0;i<banString.length;i++){
					String ban = banString[i];
					sql.append(" sum(decode(a.ban,'").append(ban).append("',a.lingjsl,0)) as BAN").append(i+1).append(",");
//					sql.append(" sum(decode(a.ban,'").append(ban).append("',a.BAN,null)) as BANC").append(i+1).append(",");
					sqlName.append(" bc.BAN").append(i+1).append(",");
//					sqlName.append(" bc.BANC").append(i+1).append(",");
				}
				param.put("sqlBan", sql.toString());
				param.put("sqlName", sqlName.toString());
			}
		}
		setResult("result",rigdService.select(bean,param));
		return AJAX;
	}
	
	/**
	 * 产线集合转成String类型，用于in操作
	 * @author gswang
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
	 * 产线集合转成String类型
	 * @author hzg
	 * @param list 产线集合
	 * @return String 产线L1,L2,L3
	 */
	public String chanxListToStringWithSplit(List<Map<String,String>> list){
		StringBuffer buf = new StringBuffer();
		String flag="";
		for(Map<String,String> chanxMap : list){
			buf.append(flag).append(chanxMap.get("SHENGCXBH"));
			flag=",";
		}
		return buf.toString();
	}
	
	
	/**
	 * 批量保存日滚动信息
	 * @author gswang
	 * @param editList 备储明细批量修改集合
	 * @param banList  班次列表
	 * @param bancList  班次所对应的数据库列明
	 * @return AJAX
	 */
	public String saveRigd(@Param("edit")ArrayList<Rigdpcmx> editList,@Param("banString") String banList,@Param("bancString") String bancList){
	try {
		Map<String,String> param = this.getParams();
		param.put("USERCENTER",loginUser.getUsercenter());
		param.put("jihybh",loginUser.getUsername());
		Message message = new Message(rigdService.saveRigd(editList,banList,bancList,param),"i18n.pc.pc_message");
		Map<String,String> msgMap = new HashMap<String,String>();
		msgMap.put("message", message.getMessage());
		setResult("result",msgMap);
	} catch (Exception e) {
		throw new ActionException(e.getMessage());
	}
		return AJAX;
	}
	
	
	/**
	 * 日滚动排产
	 * @author 贺志国
	 * @date 2012-4-13
	 * @return AJAX
	 */
	public String rigdPaic(){
		List<Map<String, String>> qckcListMap = new ArrayList<Map<String, String>>();
		Map<String,String> param = this.getParams();
		Map<String,String> msgMap = new HashMap<String,String>();
		Map<String, String> message = new HashMap<String,String>();
		List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
		param.put("CHANXH",CollectionUtil.listToString(chanxlist));
		param.put("USERCENTER",loginUser.getUsercenter());
		param.put("SHIJ",DateUtil.curDateTime().substring(0,10));
		String pcCount = rigdService.selectShiFPC(param);
		if(Integer.parseInt(pcCount)>0){
			Message m =  new Message("该生产线排产已生效，不能再次排产","该生产线排产已生效，不能再次排产");
			msgMap.put("message", m.getMessage());
			setResult("result",msgMap);
			return AJAX;
		}
		try{
			param.put("shengcx",CollectionUtil.listToString(chanxlist));
			Date date = new Date(); 
			SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd"); 
	//		param.put("today", "2012-03-05");
			param.put("today", fmat.format(date));
			param.put("GUND", "G");
			param.put("biaos", "R");
			param.put("USERCENTER",loginUser.getUsercenter());
			param.put("jihybh",loginUser.getUsername());
			param.put("kaissj", fmat.format(date));
//			String jihychanxz = loginUser.getJihyz();
			Map<String,String> postMap = loginUser.getPostAndRoleMap();
			String jihychanxz = postMap.get("PCJIHY");
			param.put("JIHYZBH", jihychanxz);
			
			List<Map<String,String>> chanxList = rigdService.selectChanx(param);
			param.put("chanxzbh", chanxList.get(0).get("CHANXZBH"));
			
			Map<String,String> paraeTemp  = new HashMap<String,String>();
			paraeTemp.putAll(param);
			paraeTemp.put("Dingdlx", "PJ");
			paraeTemp.put("DingdlxN", "NJ");
			dailyRollService.updateDingd(paraeTemp);
			
			dailyRollService.calPC(param, qckcListMap);
			userOperLog.addCorrect(CommonUtil.MODULE_PC,"日滚动排产","日滚动排产开始,界面用户点击,调用日滚动排产排产方法排产");
		} catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_PC, "日滚动排产", "日滚动排产出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			String errorMessage = rigdService.selectErrorMessage(param);
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
		msgMap.put("message", m.getMessage());
		setResult("result",msgMap);
		return AJAX;
	}
	
	
	/**
	 * 判断是否已排产
	 * @author 贺志国
	 * @date 2012-4-13
	 * @return AJAX 返回状态主‘Y’的记录数
	 */
	public String queryShiFPC(){
		Map<String,String> param = this.getParams();
		List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
		param.put("CHANXH",CollectionUtil.listToString(chanxlist));
		param.put("USERCENTER",loginUser.getUsercenter());
		param.put("SHIJ",DateUtil.curDateTime().substring(0,10));
		String pcCount = rigdService.selectShiFPC(param);
		setResult("pcCount",Integer.parseInt(pcCount));
		return AJAX;
	}
	
	
	
	/**
	 * 判断是否已生效
	 * @author 贺志国
	 * @date 2012-4-13
	 * @return AJAX 返回状态主‘N’的记录数
	 */
	public String queryShiFSX(){
		Map<String,String> param = this.getParams();
		List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
		param.put("CHANXH",CollectionUtil.listToString(chanxlist));
		param.put("USERCENTER",loginUser.getUsercenter());
		String sxCount = rigdService.selectShiFSX(param);
		setResult("sxCount",Integer.parseInt(sxCount));
		return AJAX;
	}
	
	
	
	/**
	 * 日滚动生效
	 * @author 贺志国
	 * @date 2012-4-13
	 * @return AJAX
	 */
	public String rigdShengx(){
		Map<String,String> param = this.getParams();
		List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
		param.put("CHANXH",CollectionUtil.listToString(chanxlist));
		param.put("USERCENTER",loginUser.getUsercenter());
		
		Map<String, String> message = new HashMap<String,String>();
		param.put("jihybh", loginUser.getUsername()); 
		try{
//			String jihychanxz = loginUser.getJihyz();
			Map<String,String> postMap = loginUser.getPostAndRoleMap();
			String jihychanxz = postMap.get("PCJIHY");
			param.put("JIHYZBH", jihychanxz);
			List<Map<String,String>> chanxList = rigdService.selectChanx(param);
			param.put("chanxzbh", chanxList.get(0).get("CHANXZBH"));
			dailyRollService.calOutPut(param);
		} catch(Exception e){
			Message m =  new Message("youtnup_fail","i18n.pc.pc_message");
			message.put("message", m.getMessage());
			setResult("result",message);
			return AJAX;
		}
		
		int result = rigdService.updateRigdjh(param);
		setResult("count",result);
		return AJAX;
	}
	
	/**
	 * 查询报警信息
	 * @param gongyzq 工业周期
	 * @return AJAX
	 */
	public String queryRiMessage(@Param Rigdpcmx bean,@Param("chanx") String chanx){
		Map<String,String> param = this.getParams();
		List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
		param.put("USERCENTER", loginUser.getUsercenter());
		param.put("biaos", "R");
		if(chanx.trim().length()>0){
			param.put("chanxh", chanx);
		}else{
			param.put("jihybh", loginUser.getUsername()); 
			param.put("chanxhall",CollectionUtil.listToString(chanxlist));
		}
		Map<String,Object> map = rigdService.selectMessage(bean,param);
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 计算日滚动累计交付
	 * @param gongyzq 工业周期
	 * @return AJAX
	 */
	public String rigdLeijjf(@Param Rigdpcmx bean,@Param("chanx") String chanx){
//		Map<String,String> param = this.getParams();
//		String result = pCLeijjfService.callPcSchedule("");
		pCLeijjfService.callPcSchedule("");
		return AJAX;
	}
	
	
	public String rigdDownLoad(@Param("gongyzq") String gongyzq,@Param("chanx") String chanx) throws IOException{
		Map<String,String> param = this.getParams();
		Map<String, Object> dataSource;
		dataSource = new HashMap<String, Object>();
		Map<String, String> message = new HashMap<String,String>();
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
		
			rigdService.getDownload(param,dataSource);
			
			HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
		
			downloadServices.downloadFile("RiGund.ftl", dataSource, response, "日滚动排产", ExportConstants.FILE_XLS, false) ;
		} catch(Exception e){
			logger.info(e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PC, "日滚动排产", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			Message m =  new Message("download_fail","i18n.pc.pc_message");
			message.put("message", m.getMessage());
			setResult("result",message);
		}
		//downloadServices.downloadFile("clearData.ftl", dataSource, response, "月模拟", ExportConstants.FILE_XLS, true) ;
		return "downLoad";
	}
}
