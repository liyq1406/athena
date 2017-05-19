package com.athena.ckx.module.paicfj.action;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_gongys;
import com.athena.ckx.module.paicfj.service.Ckx_gongysService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 运输商action
 * @author hj
 * @Date 12/02/28
 */
@Component(scope=ComponentDefinition.SCOPE_PROTOTYPE)
public class Ckx_gongysAction extends ActionSupport {
	@Inject
	private Ckx_gongysService ckx_gongysService;//运输商service
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 获取用户信息
	 * @author hj
	 * @Date 12/02/28
	 * @return LoginUser
	 */
	public LoginUser  getLoginUser() {
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
	public String query(@Param Ckx_gongys bean){
		
		try {
			setResult("result", ckx_gongysService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输商", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message=new HashMap<String, String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输商", "查询数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result", message);
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
	public String save(@Param Ckx_gongys bean,
			   @Param("operant") Integer operate){
		Map<String,String> message=new HashMap<String, String>();
		try {
			Message m=new Message(ckx_gongysService.save(bean, operate, getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输商", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输商", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	/**
	 * 删除方法（逻辑删除）
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return
	 */
	public String remove(@Param Ckx_gongys bean){
		Map<String,String> message=new HashMap<String, String>();
		try {
			Message m = new Message(ckx_gongysService.removes(bean, getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输商", "删除数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输商", "删除数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	public String list(@Param Ckx_gongys bean){
		try {
			setResult("result", ckx_gongysService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输商list", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message=new HashMap<String, String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输商list", "查询数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result", message);
		}
		return AJAX;
	}
}
