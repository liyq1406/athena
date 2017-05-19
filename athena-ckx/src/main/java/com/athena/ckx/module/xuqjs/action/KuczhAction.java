package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Kuczh;
import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.module.xuqjs.service.KuczhService;
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
 * 库存置换
 * @author zbb
 * @date 2015-11-20
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KuczhAction extends ActionSupport {
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private KuczhService kuczhService;
	
	
	/**
	 * 获得登录人信息
	 * @author denggq
	 * @date 2012-4-10
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 跳转到初始页面
	 * @author zbb
	 * @date 2015-11-20
	 * @return String
	 */
	public String execute(){		
		return "select";
	}
	
	/**
	 * 根据条件查询kuczh列表
	 * @param kuczh kuczh对象
	 * @author zbb
	 * @return String
	 */
	public String query(@Param Kuczh kuczh){
		try {
			Map<String, Object> resultMap = kuczhService.selectKuczh(kuczh);			
			setResult("result", resultMap);		
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库存置换", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库存置换", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}		
		return AJAX;
	}
		
	/**
	 * @description 库存置换保存，修改，删除
	 * @param bean kuczh对象 
	 * @author zbb
	 * @date 2015-11-23
	 * @return String
	 */
	public String saves(@Param("insert") ArrayList<Kuczh> insert,@Param("edit") ArrayList<Kuczh> edit , @Param("delete") ArrayList<Kuczh> delete) {
		
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(kuczhService.save(insert,edit,delete,getLoginUser().getUsername()),"i18n.i18n_common");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库存置换", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库存置换", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", message);
		return AJAX;
	}
}
