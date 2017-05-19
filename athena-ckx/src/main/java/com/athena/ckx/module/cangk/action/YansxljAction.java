package com.athena.ckx.module.cangk.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Yansxlj;
import com.athena.ckx.module.cangk.service.YansxljService;
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
 * 零件类型验项
 * @author wangyu
 * @date 2012-2-6
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YansxljAction extends ActionSupport{
	
	
	/**
	 * 用户级操作日志
	 * @author wangyu
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 注入YansblszService
	 * @author wangyu
	 * @date 2012-2-6
	 * @return bean
	 */
	@Inject
	private YansxljService yansxljService;
	
	
	/**
	 * 获取当前用户信息
	 * @author wangyu
	 * @date 2012-2-6
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 跳转到初始页面
	 * @author wangyu
	 * @date 2012-2-6
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @author wangyu
	 * @date 2012-2-6
	 * @return String
	 */
	public String query(@Param Yansxlj bean){
		try {
			setResult("result", yansxljService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件类型验项", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件类型验项", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 保存
	 * @param ArraryList insert
	 * @param ArraryList update
	 * @param ArraryList delete
	 * @author wangyu
	 * @date 2012-2-6
	 * @return String
	 */
	public String save(@Param("operant") Integer operant,@Param Yansxlj bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(yansxljService.save(bean,operant,getLoginUser().getUsername()),"i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件类型验项", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件类型验项", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author wangyu
	 * @date 2012-2-6
	 * @return String
	 */
	public String get(@Param Yansxlj bean){
		try {
			setResult("result", yansxljService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件类型验项", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件类型验项", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author wangyu
	 * @date 2012-2-6
	 * @return String
	 */
	public String list(@Param Yansxlj bean) {
		try {
			setResult("result", yansxljService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件类型验项", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件类型验项", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 删除
	 * @param bean
	 * @author wangyu
	 * @date 2012-2-6
	 * @return String
	 */
	public String remove(@Param Yansxlj bean){
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			setResult("result", yansxljService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件类型验项", "数据删除");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件类型验项", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
}
