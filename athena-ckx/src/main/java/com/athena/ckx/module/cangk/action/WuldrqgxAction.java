package com.athena.ckx.module.cangk.action;


import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Wuldrqgx;
import com.athena.ckx.module.cangk.service.WuldrqgxService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class WuldrqgxAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 注入RongqcxhdService
	 * @author wangyu
	 * @date 2012-12-10
	 */
	@Inject 
	private WuldrqgxService wuldrqgxService;
	
	
	/**
	 * 登录人信息
	 * @author wangyu
	 * @date 2012-12-10
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 * @author wangyu
	 * @date 20121210
	 */
	public String execute(@Param Wuldrqgx bean) {
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return  "select";
	}

	
	/**
	 * 分页查询
	 * @param bean
	 * @author wangyu
	 * @date 20121210
	 * @return String
	 */
	public String query(@Param Wuldrqgx bean) {
		try{
			setResult("result", wuldrqgxService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "物理点容器", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "物理点容器", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 单记录查询
	 * @param bean
	 * @author wangyu
	 * @date 20121210
	 */
	public String get(@Param Wuldrqgx bean) {
		try{
			setResult("result", wuldrqgxService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "物理点容器", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "物理点容器", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 多记录查询
	 * @param bean
	 * @author wangyu
	 * @date 20121210
	 */
	public String list(@Param Wuldrqgx bean) {
		try{
			setResult("result", wuldrqgxService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "物理点容器", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "物理点容器", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	
	/**
	 * 数据修改（多条数据操作）
	 * @param bean
	 * @author wangyu
	 * @date 2012-12-10
	 */
	public String save(@Param Wuldrqgx bean) {
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(wuldrqgxService.save(bean,getLoginUser()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "物理点容器", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "物理点容器", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 增加
	 * @param bean,insert,edit,delete
	 * @return
	 */
	public String addWuldrqgx(@Param Wuldrqgx bean ){
			Map<String,String> message = new HashMap<String,String>();
			try {
				Message m=new Message(wuldrqgxService.inserts(bean,getLoginUser()),"i18n.print.i18n_print");
				message.put("message", m.getMessage());
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "物理点容器", "数据保存");
			} catch (Exception e) {
				setResult("success", false);
				message.put("message", e.getMessage());
				userOperLog.addError(CommonUtil.MODULE_CKX, "物理点容器", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			}
			setResult("result",message);
			return AJAX;
	}
	
	/**
	 * 删除
	 * @param bean,insert,edit,delete
	 * @return
	 */
	public String remove(@Param Wuldrqgx bean ){
			Map<String,String> message = new HashMap<String,String>();
			try {
				Message m=new Message(wuldrqgxService.doDelete(bean),"i18n.print.i18n_print");
				message.put("message", m.getMessage());
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "物理点容器", "数据保存");
			} catch (Exception e) {
				setResult("success", false);
				message.put("message", e.getMessage());
				userOperLog.addError(CommonUtil.MODULE_CKX, "物理点容器", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			}
			setResult("result",message);
			return AJAX;
	}
	
}
