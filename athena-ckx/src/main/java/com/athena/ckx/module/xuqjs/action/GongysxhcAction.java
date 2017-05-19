package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Gongysxhc;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.Xiaohccx;
import com.athena.ckx.module.xuqjs.service.GongysxhcService;
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
 * 供应商小火车
 * @author zbb
 * @date 2015-11-20
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class GongysxhcAction extends ActionSupport {
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private GongysxhcService gongysxhcService;
	
	
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
	 * 根据条件查询供应商小火车列表
	 * @param gongysxhc 供应商小火车对象
	 * @author zbb
	 * @return String
	 */
	public String query(@Param Gongysxhc gongysxhc){
		try {
			Map<String, Object> resultMap = gongysxhcService.selectGysxhc(gongysxhc);			
			setResult("result", resultMap);		
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商小火车", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商小火车", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}		
		return AJAX;
	}
	
	/**
	 * 根据供应商小火车对象更新数据
	 * @param gongysxhc 供应商小火车对象
	 * @author zbb
	 * @return String
	 */
	public String update(@Param Gongysxhc gongysxhc){
		
		return AJAX;
	}
	
	/**
	 * 失效
	 * @param bean
	 * @author zbb
	 * 
	 * @return String
	 */
	public String remove(@Param Gongysxhc bean){
		Map<String,String> message = new HashMap<String,String>();
		
		bean.setEditor(getLoginUser().getUsername());			//修改人
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());	//修改时间
		try {			
			Message m = new Message(gongysxhcService.doDelete(bean), "i18n.ckx.xuqjs.i18n_gongysxhc");
			message.put("message",m.getMessage());//逻辑删除
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车", "数据失效");			
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	/**
	 * @description 供应商小火车
	 * @param bean 小火车
	 * @param Gongysxhc
	 * @author zbb
	 * @date 2015-11-23
	 * @return String
	 */
	public String saves(@Param Gongysxhc bean,@Param("operant") Integer operant,@Param("list_insert") ArrayList<Gongysxhc> insert,@Param("list_edit") ArrayList<Gongysxhc> edit , @Param("list_delete") ArrayList<Gongysxhc> delete) {
		
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(gongysxhcService.save(bean,operant,insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_gongysxhc");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商小火车", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商小火车", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", message);
		return AJAX;
	}
}
