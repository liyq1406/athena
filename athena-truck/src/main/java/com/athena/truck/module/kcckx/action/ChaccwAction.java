package com.athena.truck.module.kcckx.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.truck.entity.Chew;
import com.athena.truck.module.kcckx.service.ChaccwService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


/**
 * 叉车-车位
 * @author liushaung
 * @date 2015-1-20
 */

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ChaccwAction extends ActionSupport{

	/**
	 * 用户级操作日志
	 * @author liushaung
	 * @date 2015-1-20
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;

	/**
	 * 注入chacService
	 * @author liushaung
	 * @date 2015-1-20
	 * @return bean
	 */
	@Inject
	private ChaccwService chewService;
	
	
	/**
	 * 获取当前用户信息
	 * @author liushaung
	 * @date 2015-1-20
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	

	/**
	 * 分页查询叉车车位
	 * @param bean
     * @author liushaung
	 * @date 2015-1-20
	 * @return String
	 */
	public String queryChew(@Param Chew bean){
		try {
			setResult("result", chewService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 查询增加页面的叉车车位
	 * @param bean
     * @author liushaung
	 * @date 2015-1-20
	 * @return String
	 */
	public String queryChaccw(@Param Chew bean){
		try {
			setResult("result", chewService.listchaccw(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 删除
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-29
	 * @return String
	 */
	public String remove(@Param Chew bean){
		
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateUtil.curDateTime());
			setResult("result", chewService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author chenpeng
	 * @date 2015-2-3
	 * @return String
	 */
	public String list(@Param Chew bean) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("total", chewService.list(bean).size());
			map.put("rows", chewService.list(bean));
			//setResult("result", chewService.list(bean));
			setResult("result", map);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
}