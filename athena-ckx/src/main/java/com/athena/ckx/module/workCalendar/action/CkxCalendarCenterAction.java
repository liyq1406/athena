/**
 * 
 */
package com.athena.ckx.module.workCalendar.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.workCalendar.CkxCalendarCenter;
import com.athena.ckx.module.workCalendar.service.CkxCalendarCenterService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 中心日历
 * 
 * 
 * @author kong
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxCalendarCenterAction extends ActionSupport {
	
	//中心日历逻辑业务类
	@Inject
	private CkxCalendarCenterService ckxCalendarCenterService;
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}

	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param CkxCalendarCenter bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxCalendarCenter bean) {
		if ("0".equals(bean.getXingq())) {
			bean.setXingq(null);
		}
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "中心日历", "数据查询开始");
		setResult("result", ckxCalendarCenterService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "中心日历", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 集合查询
	 * @param bean
	 * @return
	 */
	public String list(@Param CkxCalendarCenter bean) {
		setResult("result", ckxCalendarCenterService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param CkxCalendarCenter bean) {
		setResult("result", ckxCalendarCenterService.get(bean));
		return AJAX;
	}
	/**
	 * 行编辑保存
	 * @param bean
	 */
	public String save(@Param("editList") ArrayList<CkxCalendarCenter> editList) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "中心日历", "数据编辑开始");
			map.put("message", ckxCalendarCenterService.edit(editList,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "中心日历", "数据编辑结束");
		} catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "中心日历", "数据编辑结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	/**
	 * 
	 * @param year 添加的年份
	 * @param center 用户中心
	 * @return
	 */
	public String addYear(@Param("year") String year,@Param("center") String center){
		Map<String,String> map = new HashMap<String,String>();
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "中心日历", "数据添加开始");
			map.put("message",ckxCalendarCenterService.addYear(center, year,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "中心日历", "数据添加结束");
		} catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "中心日历", "数据添加结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 根据年份和用户中心删除中心日历
	 * @param year
	 * @param center
	 * @return
	 */
	public String delYear(@Param("year") String year,@Param("center") String center){
		Map<String,String> map = new HashMap<String,String>();
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "中心日历", "数据删除开始");
		map.put("message", ckxCalendarCenterService.delYear(year, center));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "中心日历", "数据删除结束");
		setResult("result",map );
		return AJAX;
	}
}
