package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Dinghlxzh;
import com.athena.ckx.module.xuqjs.service.DinghlxzhService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 订货路线转换Action
 * @author qizhongtao
 * @date 2012-4-06
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DinghlxzhAction extends ActionSupport{
	
	@Inject
	private DinghlxzhService dinghlxzhService;
	
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2018-4-18
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 主页面
	 * @author qizhongtao
	 * @date 2012-4-06
	 * @return String
	 * */
	public String execute(){
		setResult("usercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @author qizhongtao
	 * @date 2012-4-06
	 * @Param dinghlxzh
	 * @return String
	 * */
	public String query(@Param Dinghlxzh bean) {
		try{
			setResult("result", dinghlxzhService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "订货路线转换", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "订货路线转换", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 单数据查询
	 * @author qizhongtao
	 * @date 2012-4-06
	 * @Param dinghlxzh
	 * @return String
	 * */
	public String get(@Param Dinghlxzh bean) {
		try{
			setResult("result", dinghlxzhService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "订货路线转换", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "订货路线转换", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 多数据查询
	 * @author qizhongtao
	 * @date 2012-4-06
	 * @Param dinghlxzh
	 * @return String
	 * */
	public String list(@Param Dinghlxzh bean) {
		try{
			setResult("result", dinghlxzhService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "订货路线转换", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "订货路线转换", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 行编辑保存
	 * @author qizhongtao
	 * @date 2012-4-06
	 * @Param insert,edit,delete
	 * @return String
	 * */
	public String save(@Param("insert") ArrayList<Dinghlxzh> insert,@Param("edit") ArrayList<Dinghlxzh> edit,@Param("delete") ArrayList<Dinghlxzh> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(dinghlxzhService.save(insert, edit, delete, getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "订货路线转换", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "订货路线转换", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
}
