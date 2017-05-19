package com.athena.truck.module.dapxs.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.truck.entity.Dengdqy;
import com.athena.truck.module.churcsb.service.YundService;
import com.athena.truck.module.dapxs.service.DapxsService;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DapxsAction extends ActionSupport{
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private YundService yundService;
	
	@Inject
	private DapxsService dapxs;
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(DapxsAction.class);
	
	public String execute(){
		userOperLog.addCorrect("truck", "大屏显示", "大屏显示页面");
		LoginUser currentUser = AuthorityUtils.getSecurityUser();
		String usercenter = currentUser.getUsercenter();
		//区域组id，通过区域组id和用户中心在SYS_QY_GROUP表中查到所有区域
		Map<String,String> postMap = currentUser.getPostAndRoleMap();
		String postId = postMap.get("QUYGLY");
		Map<String,Object> quyParam = new HashMap<String,Object>();
		quyParam.put("usercenter", usercenter);
		quyParam.put("postId", postId);
		List<Map<String,Object>> quyResults = yundService.queryCurUserQuy(quyParam);
		if(quyResults.size()>0){
			quyParam.put("quybh", quyResults.get(0).get("QUYBH"));
			setRequestAttribute("quybh",quyResults.get(0).get("QUYBH"));
			setRequestAttribute("usercenter",usercenter);
			setRequestAttribute("username",AuthorityUtils.getSecurityUser().getUsername());
			Dengdqy bean =dapxs.queryQuymc(quyParam);	
			setRequestAttribute("quymc",bean.getQuymc());
			return "select";
		}else{
			return "error";
		}
	}
	
	
	
	//查询车辆入厂信息
	public String queryRucxx(){
		//添加日志-执行开始
		logger.info("开始-大屏-准入查询（DapxsAction.queryRucxx）");
		Map params = getParams();
		LoginUser currentUser = AuthorityUtils.getSecurityUser();
		params.put("usercenter", currentUser.getUsercenter());
        List list=dapxs.queryRucxx(params);
	    setResult("result", list);
	  //添加日志-执行开始
		logger.info("结束-大屏-准入查询（DapxsAction.queryRucxx）");
		return AJAX;
	}
	
	public String queryShenbpdxx(){
		//添加日志-执行开始
		logger.info("开始-大屏-申报排队查询（DapxsAction.queryShenbpdxx）");
		Map params = getParams();
		LoginUser currentUser = AuthorityUtils.getSecurityUser();
		params.put("usercenter", currentUser.getUsercenter());
        List list=dapxs.queryShenbpdxx(params);
	    setResult("result", list);
	  //添加日志-执行开始
		logger.info("结束-大屏-申报排队查询（DapxsAction.queryShenbpdxx）");
		return AJAX;
	}
	
	
}
