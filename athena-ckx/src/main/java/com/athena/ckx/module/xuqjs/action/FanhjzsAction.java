package com.athena.ckx.module.xuqjs.action;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Fanhjzs;
import com.athena.ckx.module.xuqjs.service.FanhjzsService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


/**
 * 返还记账商
 * @author xss
 * 0010495
 * @Date 2015-3-26
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class FanhjzsAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private  FanhjzsService FanhjzsService;
	
	
	/**
	  * 获取用户信息
	  * @author denggq
	  * @Date 2012-3-19
	  * @return LoginUser
	  */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}

	
	public String execute() {
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	public String query(@Param Fanhjzs bean){
		try {
			setResult("result", FanhjzsService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "返还记账商", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "返还记账商", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	public String get(@Param Fanhjzs bean){
		try {
			setResult("result", FanhjzsService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "返还记账商", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "返还记账商", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	public String list(@Param Fanhjzs bean) {
		try {
			setResult("result", FanhjzsService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "返还记账商", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "返还记账商", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}	
	
	public String save(@Param("insert") ArrayList<Fanhjzs> insert,@Param("edit") ArrayList<Fanhjzs> edit , @Param("delete") ArrayList<Fanhjzs> delete){
		Map<String,String> map = new HashMap<String,String>();
		Map<String,String> message = new HashMap<String,String>();
		
		
		/*
		int j = FanhjzsService.ckGongys(insert);//校验返还/记账商是否存在ckx_gongys，是否有效
					
		
		if(j<2){
			setResult("success",false);
			message.put("message", "操作失败！该返还/记账商无效!");
		}
		*/
		try {
			Message m = new Message(FanhjzsService.save(insert, edit, delete,getLoginUser().getUsercenter()),"i18n.ckx.xuqjs.i18n_fanhjzs");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "返还记账商", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "返还记账商", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	
	public String queryfhusercenter(){
		
		try{
			List<Map<String,String>> fhuc =  FanhjzsService.queryfhusercenter();
			setResult("result",fhuc);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "用户中心", "查询用户中心");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "用户中心", "查询用户中心", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
}
