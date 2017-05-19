package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Chanxhb;
import com.athena.ckx.module.xuqjs.service.ChanxhbService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


/**
 * 产线合并Action
 * @author qizhongtao
 * @date 2012-4-17
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ChanxhbAction extends ActionSupport{
	
	@Inject
	private ChanxhbService chanxhbService;
	
	/**
	 * 注入UserOperLog
	 * @author qizhongtao
	 * @date 2012-3-28
	 * */
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
	  * @Date 2012-4-17
	  * @return String
	  */
	public String execute(){
		setResult("usercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @author denggq
	 * @Date 2012-5-9
	 * @return String
	 * */
	public String query(@Param Chanxhb bean){
		try{
			setResult("result",chanxhbService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "产线合并", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "产线合并", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 单数据查询
	 * @author denggq
	 * @Date 2012-5-9
	 * @return String
	 * */
	public String get(@Param Chanxhb bean){
		try{
			setResult("result",chanxhbService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "产线合并", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "产线合并", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 多数据查询
	 * @author denggq
	 * @Date 2012-5-9
	 * @return String
	 * */
	public String list(@Param Chanxhb bean){
		try{
			setResult("result",chanxhbService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "产线合并", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "产线合并", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 行编辑保存数据
	 * @author qizhongtao
	 * @Date 2012-4-17
	 * @Param insert,edit,delete
	 * @return String
	 * */
	public String save(@Param("insert") ArrayList<Chanxhb> insert,@Param("edit") ArrayList<Chanxhb> edit,@Param("delete") ArrayList<Chanxhb> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(chanxhbService.save(insert, edit, delete, getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "产线合并", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "产线合并", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
}
