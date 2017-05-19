
package com.athena.ckx.module.cangk.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.module.cangk.service.ZickService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.util.Assert;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 子仓库
 * @author denggq
 * @date 2012-1-12
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ZickAction extends ActionSupport{
	
	
	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 注入ZickService
	 * @author denggq
	 * @date 2012-1-12
	 * @return bean
	 */
	@Inject
	private ZickService zickService;
	
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
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-1-12
	 * @return String
	 */
	public String query(@Param Zick bean){
		Assert.notNull(bean.getCangkbh(),"增加时不能刷新！");
		try {
			setResult("result", zickService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "子仓库", "分页查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "子仓库", "分页查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-12
	 * @return String
	 */
	public String get(@Param Zick bean){
		try {
			setResult("result", zickService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "子仓库", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "子仓库", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-12
	 * @return String
	 */
	public String list(@Param Zick bean) {
		try {
			setResult("result", zickService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "子仓库", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "子仓库", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 删除
	 * @param bean
	 * @author denggq
	 * @date 2012-1-12
	 * @return String
	 */
	public String remove(@Param Zick bean){
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			setResult("result", zickService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "子仓库", "数据失效");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "子仓库", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
}
