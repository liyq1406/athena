package com.athena.ckx.module.cangk.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Danjdy;
import com.athena.ckx.entity.cangk.Kehczm;
import com.athena.ckx.module.cangk.service.KehczmService;
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
 * 客户操作码
 * @author denggq
 * @date 2012-1-30
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KehczmAction extends ActionSupport{
	

	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;

	/**
	 * 注入KehczmService
	 * @author denggq
	 * @date 2012-1-30
	 * @return bean
	 */
	@Inject
	private KehczmService kehczmService;
	
	
	/**
	 * 获取当前用户信息
	 * @author denggq
	 * @date 2012-2-6
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-1-30
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-1-30
	 * @return String
	 */
	public String query(@Param Kehczm bean){
		try {
			setResult("result", kehczmService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "客户操作码", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "客户操作码", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-30
	 * @return String
	 */
	public String get(@Param Kehczm bean){
		try {
			setResult("result", kehczmService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "客户操作码", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "客户操作码", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-30
	 * @return String
	 */
	public String list(@Param Kehczm bean) {
		try {
			setResult("result", kehczmService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "客户操作码", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "客户操作码", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 删除
	 * @param bean
	 * @author denggq
	 * @date 2012-1-30
	 * @return String
	 */
	public String remove(@Param Kehczm bean){
		
		bean.setEditor(getLoginUser().getUsername());
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		
		try {
			setResult("result", kehczmService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "客户操作码", "数据失效");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "客户操作码", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * @description 保存单据打印
	 * @param bean
	 * @param danjdys
	 * @return
	 * @author denggq
	 * @date 2012-1-30
	 */
	public String saveDanjdys(@Param Kehczm bean,@Param("operant") Integer operant,@Param("danjdys_insert") ArrayList<Danjdy> insert,@Param("danjdys_edit") ArrayList<Danjdy> edit , @Param("danjdys_delete") ArrayList<Danjdy> delete ) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(kehczmService.save(bean,operant,insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "客户操作码", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "客户操作码", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		
		return AJAX;
	}
}
