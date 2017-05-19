package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.GongysRouxbl;
import com.athena.ckx.module.xuqjs.service.GongysRouxblService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


/**
 * 供应商-柔性比例Action
 * @author qizhongtao
 * @date 2012-4-09
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class GongysRouxblAction extends ActionSupport{
	
	@Inject
	private GongysRouxblService gongysRouxblService;
	
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
	  * @Date 2012-4-09
	  * @return String
	  */
	public String execute(){
		setResult("usercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @author qizhongtao
	 * @Date 2012-4-09
	 * @return String
	 * */
	public String query(@Param GongysRouxbl bean){
		try{
			setResult("result",gongysRouxblService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商柔性比例", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商柔性比例", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 多记录查询方法
	 * @author denggq
	 * @Date 2012-5-10
	 * @param bean
	 * @return String
	 */
	public String list(@Param GongysRouxbl bean) {
		try{
			setResult("result", gongysRouxblService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商柔性比例", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商柔性比例", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	
	/**
	 * 单记录查询方法
	 * @author denggq
	 * @Date 2012-5-10
	 * @param bean
	 * @return String
	 */
	public String get(@Param GongysRouxbl bean) {
		try{
			setResult("result", gongysRouxblService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商柔性比例", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商柔性比例", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 行编辑保存数据
	 * @author qizhongtao
	 * @Date 2012-4-09
	 * @Param insert,edit,delete
	 * @return String
	 * */
	public String save(@Param("insert") ArrayList<GongysRouxbl> insert,@Param("edit") ArrayList<GongysRouxbl> edit,@Param("delete") ArrayList<GongysRouxbl> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(gongysRouxblService.save(insert, edit, delete, getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商柔性比例", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "供应商柔性比例", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
}
