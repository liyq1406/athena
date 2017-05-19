package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Ziygzlx;
import com.athena.ckx.module.xuqjs.service.ZiygzlxService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


/**
 * 资源跟踪类型对应表Action
 * @author qizhongtao
 * @date 2012-4-17
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ZiygzlxAction extends ActionSupport{
	
	@Inject
	private ZiygzlxService ziygzlxService;
	
	@Inject
	private UserOperLog userOperLog;
	
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-4-6
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
	 * @author qizhongtao
	 * @Date 2012-4-17
	 * @return String
	 * */
	public String query(@Param Ziygzlx bean){
		try{
			setResult("result",ziygzlxService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "资源跟踪类型对应表", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "资源跟踪类型对应表", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 单数据查询
	 * @author denggq
	 * @Date 2012-5-10
	 * @return String
	 * */
	public String get(@Param Ziygzlx bean){
		try{
			setResult("result",ziygzlxService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "资源跟踪类型对应表", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "资源跟踪类型对应表", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 多数据查询
	 * @author denggq
	 * @Date 2012-5-10
	 * @return String
	 * */
	public String list(@Param Ziygzlx bean){
		try{
			setResult("result",ziygzlxService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "资源跟踪类型对应表", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "资源跟踪类型对应表", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String save(@Param Ziygzlx bean,@Param("operant") Integer operant ,@Param("gonghms") ArrayList<String> gonghms){
		Map<String,String> message = new HashMap<String,String>();
		try {
			String ghms = gonghms.toString().replaceAll(" ", "");//List装换为String并去掉空格
			ghms = ghms.substring(1,ghms.length()-1);//去掉前后[]
			bean.setGonghms(ghms);
			Message m = new Message(ziygzlxService.save(bean, operant, getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "资源跟踪类型对应表", "数据保存");
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "资源跟踪类型对应表", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	public String remove(@Param Ziygzlx bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(ziygzlxService.doDelete(bean),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "资源跟踪类型对应表", "数据删除");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "资源跟踪类型对应表", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
}
