package com.athena.truck.module.yundgz.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.truck.entity.Liucdy;
import com.athena.truck.entity.Yund;
import com.athena.truck.module.kcckx.service.DengdqyService;
import com.athena.truck.module.queue.service.QueueService;
import com.athena.truck.module.yundgz.service.YundgzService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 *  运单跟踪
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YundgzAction extends ActionSupport {

	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private QueueService queue;
	
	@Inject
	private  DengdqyService dengdqy;
	
	@Inject
	private  YundgzService  yundgz;
	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(YundgzAction.class);
	
	//获取用户中心
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}
   //运单跟踪
	public String execute(){
		Map<String,String> map = getParams();
		setResult("quybh",map.get("quybh"));
		setResult("daztbh",map.get("daztbh"));
		setResult("zhuangt",map.get("zhuangt"));
		setResult("usercenter", getUserInfo().getUsercenter());
		return "select";
	}
	
	//等待区域下拉框列表
	public String queryYundgzDengdqy() {
		Map<String,String> params = getParams();
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String quygly = postMap.get("QUYGLY")==null ? "":postMap.get("QUYGLY");
		params.put("post_code", quygly);
		try {
			setResult("result", yundgz.queryYundgzDengdqy(params));
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运单跟踪-等待区域", "运单跟踪-等待区域", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	//大站台下拉查询
	public String queryYundgzDazt() {
		Map<String,String> params = getParams();
		try {
			setResult("result", yundgz.queryYundgzDazt(params));
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运单跟踪-大站台下拉", "运单跟踪-大站台下拉", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	//流程列表
	public String queryYundgzZhuangt() {
		Map<String,String> params = getParams();
		try {
			List<Object> zt = yundgz.queryYundgzZhuangt(params);
			
			if(zt == null || zt.size()==0){
				params.put("quybh", "DEF");
				params.put("daztbh", "DEF");
				zt = yundgz.queryYundgzZhuangt(params);
			}
			Liucdy liud = new Liucdy();
			Liucdy liud2 = new Liucdy();
			liud.setLiucbh("80");
			liud2.setLiucbh("90");
			liud.setLiucmc("80 删除");
			liud2.setLiucmc("90 撤销");
			zt.add(liud);
			zt.add(liud2);
			
			setResult("result", zt);
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运单跟踪-流程列表", "运单跟踪-流程列表", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	//车辆排队分页查询
	public String queryYundgz(@Param Yund bean) {
		Map<String,String> params = getParams();
		try {
			setResult("result", yundgz.queryYundgz(bean, params));
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运单跟踪-分页查询", "运单跟踪-分页查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	   //运单跟踪明细
	public String initYundMx(@Param("usercenter") String usercenter,@Param("yundh") String yundh){
		setResult("usercenter", getParam("usercenter"));
		setResult("yundh", yundh);
		setResult("kach", getParam("kach"));
		return "selectmx";
	}
	
	//车辆排队分页查询
	public String queryYundgzmx(@Param Yund bean) {
		Map<String,String> params = getParams();
		try {
			setResult("result", yundgz.queryYundgzmx(bean, params));
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运单跟踪-分页查询", "运单跟踪-分页查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	//运单提前排队
	public String yunDtiqpd(@Param("yd")ArrayList<Yund> yundList) {
		Map<String,String> params = getParams();
		params.put("username", loginUser.getUsername());
		Map<String,String> message = new HashMap<String,String>();
		try {
			yundgz.yunDtiqpd(yundList,params);
			message.put("message", "操作成功！");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运单跟踪-急件提前排队", "运单跟踪-急件提前排队", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	//运单删除
	public String yundDelete(@Param("yd")ArrayList<Yund> yundList) {
		Map<String,String> params = getParams();
		params.put("username", loginUser.getUsername());
		Map<String,String> message = new HashMap<String,String>();
		try {
			yundgz.yundDelete(yundList,params);
			message.put("message", "删除成功！");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运单跟踪-运单删除", "运单跟踪-运单删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	//运单入厂操作
	public String yundRuc(@Param("yd")ArrayList<Yund> yundList) {
		Map<String,String> params = getParams();
		params.put("username", loginUser.getUsername());
		Map<String,String> message = new HashMap<String,String>();
		try {
			yundgz.yundRuc(yundList,params);
			message.put("message", "入厂操作成功！");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运单跟踪-入厂操作", "运单跟踪-入场操作", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
}
