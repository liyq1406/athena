package com.athena.ckx.module.cangk.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Danjdy;
import com.athena.ckx.module.cangk.service.DanjdyService;
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
public class DanjdyAction extends ActionSupport{
	

	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 注入DanjdyService
	 * @author denggq
	 * @date 2012-1-12
	 * @return bean
	 */
	@Inject
	private DanjdyService danjdyService;
	
	
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
	public String query(@Param Danjdy bean){
		Assert.notNull(bean.getKehbh(),"增加时不能刷新！");
		try {
			setResult("result", danjdyService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "单据打印", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "单据打印", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String get(@Param Danjdy bean){
		try {
			setResult("result", danjdyService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "单据打印", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "单据打印", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String list(@Param Danjdy bean) {
		try {
			setResult("result", danjdyService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "单据打印", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "单据打印", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String remove(@Param Danjdy bean){
		try {
			setResult("result", danjdyService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "单据打印", "数据删除");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "单据打印", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
}
