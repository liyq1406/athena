package com.athena.ckx.module.cangk.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Xingzysts;
import com.athena.ckx.module.cangk.service.XingzystsService;
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
 * 行政验收提示验收项
 * @author denggq
 * @date 2012-1-16
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class XingzystsAction extends ActionSupport{

	
	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;
	
	
	/**
	 * 注入XingzystsService
	 * @author denggq
	 * @date 2012-1-16
	 * @return bean
	 */
	@Inject
	private XingzystsService xingzystsService;
	
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
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String execute(){
		return "select";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String query(@Param Xingzysts bean){
		try {
			setResult("result", xingzystsService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "行政验收提示验收项", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "行政验收提示验收项", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 多行保存
	 * @param ArraryList insert
	 * @param ArraryList update
	 * @param ArraryList delete
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String save(@Param("insert") ArrayList<Xingzysts> insert,@Param("edit") ArrayList<Xingzysts> edit , @Param("delete") ArrayList<Xingzysts> delete){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(xingzystsService.save(insert, edit, delete,getLoginUser().getUsername()),"i18n.ckx.cangk.i18n_xingzysts");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "行政验收提示验收项", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "行政验收提示验收项", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String get(@Param Xingzysts bean){
		try {
			setResult("result", xingzystsService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "行政验收提示验收项", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "行政验收提示验收项", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String list(@Param Xingzysts bean) {
		try {
			setResult("result", xingzystsService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "行政验收提示验收项", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "行政验收提示验收项", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 删除
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String remove(@Param Xingzysts bean){
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			setResult("result", xingzystsService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "行政验收提示验收项", "数据失效");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "行政验收提示验收项", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
}
