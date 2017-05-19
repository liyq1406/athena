/**
 * 
 */
package com.athena.ckx.module.workCalendar.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.workCalendar.CkxXiuxr;
import com.athena.ckx.module.workCalendar.service.CkxXiuxrService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ActionException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 休息日 
 * 2021-02-13
 * @author kong
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxXiuxrAction extends ActionSupport {
	/**
	 * 休息日逻辑业务层
	 */
	@Inject
	private CkxXiuxrService ckxXiuxrService;
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
	public String execute(@Param CkxXiuxr bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxXiuxr bean) {
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "休息时间", "数据查询开始");
		setResult("result", ckxXiuxrService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "休息时间", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 集合查询
	 * @param bean
	 * @return
	 */
	public String list(@Param CkxXiuxr bean) {
		setResult("result", ckxXiuxrService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param CkxXiuxr bean) {
		setResult("result", ckxXiuxrService.get(bean));
		return AJAX;
	}
	/**
	 * 行编辑数据保存
	 * @param bean
	 */
	public String save(@Param("addList") ArrayList<CkxXiuxr> addList,@Param("editList") ArrayList<CkxXiuxr> editList,@Param("delList") ArrayList<CkxXiuxr> delList) throws ActionException{
		Map<String,String> map = new HashMap<String,String>();
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "休息时间", "数据编辑开始");
			map.put("message", ckxXiuxrService.save(addList, editList, delList, getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "休息时间", "数据编辑结束");
		} catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "休息时间", "数据编辑结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
}
