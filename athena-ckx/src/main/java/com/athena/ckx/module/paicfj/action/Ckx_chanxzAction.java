package com.athena.ckx.module.paicfj.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_chanxz;
import com.athena.ckx.entity.paicfj.Ckx_chanxz_paiccs;
import com.athena.ckx.module.paicfj.service.Ckx_chanxzService;
import com.athena.ckx.module.paicfj.service.Ckx_chanxz_paiccsService;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.util.Assert;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 产线组action
 * @author hj
 * @Date 12/02/28
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class Ckx_chanxzAction extends ActionSupport {
	@Inject 
	private Ckx_chanxzService ckx_chanxz;//注入产线组service
	@Inject
	private Ckx_chanxz_paiccsService ckx_chanxz_paiccs; //注入产线组-排产参数设置service
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 获取用户信息
	 * @author hj
	 * @Date 12/02/28
	 * @return LoginUser
	 */
	public LoginUser  getLoginUser() {
//		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
		return AuthorityUtils.getSecurityUser();
	}
	/**
	 * 跳转页面
	 * @author hj
	 * @Date 12/02/28
	 * @return String
	 */
	public String execute(){
		//获取到用户中心
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return "select";
	}
	/**
	 * 分页查询方法
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return 
	 */
	public String query(@Param Ckx_chanxz bean){		
		try {
			setResult("result", ckx_chanxz.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "产线组", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());//若出现异常，则弹出异常消息
			userOperLog.addError(CommonUtil.MODULE_CKX, "产线组", "查询数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	
	public String list(@Param Ckx_chanxz bean){
		try {
			setResult("result", ckx_chanxz.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "产线组List", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());//若出现异常，则弹出异常消息
			userOperLog.addError(CommonUtil.MODULE_CKX, "产线组List", "查询数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	/**
	 * 分页查询方法
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return 
	 */
	public String query_chanxz_paiccs(@Param Ckx_chanxz_paiccs bean){
		try {
			Assert.notNull(bean.getChanxzbh(),GetMessageByKey.getMessage("addbygsx"));
			setResult("result", ckx_chanxz_paiccs.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "产线组-排产参数设置", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());//若出现异常，则弹出异常消息
			userOperLog.addError(CommonUtil.MODULE_CKX, "产线组-排产参数设置", "查询数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	/**
	 * 保存方法
	 * @author hj
	 * @Date 12/02/28
	 * @param insert
	 * @param edit
	 * @param delete
	 * @return
	 */
	public String save(@Param Ckx_chanxz bean , 
			@Param("operant") Integer operate,
			@Param("f_pc_insert") ArrayList<Ckx_chanxz_paiccs> insert ,
			@Param("f_pc_edit") ArrayList<Ckx_chanxz_paiccs> edit ,
			@Param("f_pc_delete") ArrayList<Ckx_chanxz_paiccs> delete ){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(ckx_chanxz.save(bean,operate, insert, edit, delete, getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "产线组", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());//若出现异常，则弹出异常消息
			userOperLog.addError(CommonUtil.MODULE_CKX, "产线组", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 
	 * @return
	 */
	public String queryPCJHY(){
		try {
			setResult("result", ckx_chanxz.queryPCJIIHY());
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());//若出现异常，则弹出异常消息
			userOperLog.addError(CommonUtil.MODULE_CKX, "排产计划员", "查询数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
}
