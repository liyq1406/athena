package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Zhucgys;
import com.athena.ckx.module.xuqjs.service.ZhucgysService;
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
 * 主从供应商
 * @author xss
 * @Date 2015-2-4
 * 0010495
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ZhucgysAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private  ZhucgysService zhucgysService;
	
	
	/**
	  * 获取用户信息
	  * @author denggq
	  * @Date 2012-3-19
	  * @return LoginUser
	  */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}

	
	public String execute() {
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	public String query(@Param Zhucgys bean){
		
		try {
			setResult("result", zhucgysService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "主从供应商", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "主从供应商", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	public String get(@Param Zhucgys bean){
		try {
			setResult("result", zhucgysService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "主从供应商", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "主从供应商", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	public String list(@Param Zhucgys bean) {
		try {
			setResult("result", zhucgysService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "主从供应商", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "主从供应商", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	public String save(@Param("insert") ArrayList<Zhucgys> insert,@Param("edit") ArrayList<Zhucgys> edit , @Param("delete") ArrayList<Zhucgys> delete){
		Map<String,String> map = new HashMap<String,String>();
		Map<String,String> message = new HashMap<String,String>();
/*
		int i = zhucgysService.checkSlavefanhs(insert);//该从承运商是否存在
		int j = zhucgysService.ckGongys(insert);//校验主从承运商的是否存在，是否有效 
		
	
		if(i>0){
			setResult("success",false);
			message.put("message", "该从承运商已经存在");
		}
		if(j<2){
			setResult("success",false);
			message.put("message", "该从承运商无效!");
		}
			
		*/
		
		try {
			Message m = new Message(zhucgysService.save(insert, edit, delete,getLoginUser().getUsercenter()),"i18n.ckx.xuqjs.i18n_zhucgys");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "主从供应商", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "主从供应商", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	
}
