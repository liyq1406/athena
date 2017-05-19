package com.athena.ckx.module.paicfj.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Ckx_chanxz_jhy;
import com.athena.ckx.module.paicfj.service.Ckx_chanxz_jhyService;
import com.athena.component.service.Message;

import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 产线组计划员action
 * @author hj
 * @Date 12/02/28
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class Ckx_chanxz_jhyAction extends ActionSupport {

	@Inject
	private Ckx_chanxz_jhyService ckx_chanxz_jhy;//注入产线组计划员service
	/**
	 * 获取用户信息
	 * @author hj
	 * @Date 12/02/16
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
	public String execute() {
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
	public String query(@Param Ckx_chanxz_jhy bean) {
		try {
			setResult("result", ckx_chanxz_jhy.select(bean));
		} catch (Exception e) {
			
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
	public String save(@Param("insert") ArrayList<Ckx_chanxz_jhy> insert,
			@Param("edit") ArrayList<Ckx_chanxz_jhy> edit,
			@Param("delete") ArrayList<Ckx_chanxz_jhy> delete) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(ckx_chanxz_jhy.save(insert, edit, delete,
					getLoginUser().getUsername()), "i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
		} catch (Exception e) {
			message.put("message", e.getMessage());
		}
		setResult("result", message);
		return AJAX;
	}
}
