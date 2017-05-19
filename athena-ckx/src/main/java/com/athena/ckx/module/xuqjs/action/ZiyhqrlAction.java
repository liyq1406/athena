package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Ziyhqrl;
import com.athena.ckx.module.xuqjs.service.ZiyhqrlService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


/**
 * 资源获取日历Action
 * @author qizhongtao
 * @date 2012-4-16
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ZiyhqrlAction extends ActionSupport{
	
	@Inject
	private ZiyhqrlService ziyhqrlService;
	
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
	  * @Date 2012-4-16
	  * @return String
	  */
	public String execute(){
		setResult("usercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @author qizhongtao
	 * @Date 2012-4-16
	 * @return String
	 * */
	public String query(@Param Ziyhqrl bean){
		try{
			setResult("result",ziyhqrlService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "资源获取日历", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "资源获取日历", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 单数据查询
	 * @author denggq
	 * @Date 2012-5-10
	 * @return String
	 * */
	public String get(@Param Ziyhqrl bean){
		try{
			setResult("result",ziyhqrlService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "资源获取日历", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "资源获取日历", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 多数据查询
	 * @author denggq
	 * @Date 2012-5-10
	 * @return String
	 * */
	public String list(@Param Ziyhqrl bean){
		try{
			setResult("result",ziyhqrlService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "资源获取日历", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "资源获取日历", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 行编辑保存数据
	 * @author qizhongtao
	 * @Date 2012-4-16
	 * @Param insert,delete
	 * @return String
	 * */
	public String save(@Param("insert") ArrayList<Ziyhqrl> insert,@Param("edit") ArrayList<Ziyhqrl> edit,@Param("delete") ArrayList<Ziyhqrl> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(ziyhqrlService.save(insert,delete, getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "资源获取日历", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "资源获取日历", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
}
