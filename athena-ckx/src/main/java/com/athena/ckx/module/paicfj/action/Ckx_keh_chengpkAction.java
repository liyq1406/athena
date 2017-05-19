package com.athena.ckx.module.paicfj.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_keh_chengpk;
import com.athena.ckx.module.paicfj.service.Ckx_keh_chengpkService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 客户成品库action
 * @author hj
 * @Date 12/02/28
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class Ckx_keh_chengpkAction extends ActionSupport {
     
	@Inject
	private Ckx_keh_chengpkService keh_chengpkService;//客户成品库service
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
	public String query(@Param Ckx_keh_chengpk bean){
		try {
			setResult("result", keh_chengpkService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "客户成品库关系", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message=new HashMap<String, String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "客户成品库关系", "查询数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result", message);
		}
		return AJAX;
	}
	/**
	 * 行编辑保存方法
	 * @author hj
	 * @Date 12/02/28
	 * @param insert
	 * @param edit
	 * @param delete
	 * @return
	 */
	public String saves(@Param("insert") ArrayList<Ckx_keh_chengpk> insert,
			@Param("edit") ArrayList<Ckx_keh_chengpk> edit,
			@Param("delete") ArrayList<Ckx_keh_chengpk> delete){
		Map<String,String> message=new HashMap<String, String>();
		try {
			Message m=new Message(keh_chengpkService.saves(insert, edit, delete, getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "客户成品库关系", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "客户成品库关系", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	/**
	 * 单数据保存方法
	 * @author hj
	 * @Date 12/02/28
	 * @param insert
	 * @param edit
	 * @param delete
	 * @return
	 */
	public String save(@Param Ckx_keh_chengpk bean,@Param("operant") Integer operant) {
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(keh_chengpkService.save(bean,operant,getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "客户成品库关系", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "客户成品库关系", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 删除
	 * @author hj
	 * @Date 12/02/28
	 * @param bean
	 * @return 
	 */
	public String remove(@Param Ckx_keh_chengpk bean){
		try {
			setResult("result", keh_chengpkService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "客户成品库关系", "删除数据结束");
		} catch (Exception e) {
			Map<String,String> message=new HashMap<String, String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "客户成品库关系", "删除数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result", message);
		}
		return AJAX;
	}
}
