package com.athena.ckx.module.cangk.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Xiehzt;
import com.athena.ckx.entity.cangk.Xiehztbz;
import com.athena.ckx.module.cangk.service.XiehztService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


/**
 * 卸货站台
 * @author denggq
 * @date 2012-1-16
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class XiehztAction extends ActionSupport{
	

	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;

	/**
	 * 注入XiehztService
	 * @author denggq
	 * @date 2012-1-16
	 * @return bean
	 */
	@Inject
	private XiehztService xiehztService;
	
	
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
	@SuppressWarnings("rawtypes")
	public String execute(){
		Map map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		setResult("role", key);//角色（更新保存时用）
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String query(@Param Xiehzt bean){
		try {
			setResult("result", xiehztService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	/**
	 * 分页查询
	 * @param bean
	 * @author hj
	 * @date 2012-8-21
	 * @return String
	 */
	public String queryZsd(@Param Xiehztbz bean){
		try {
			setResult("result", xiehztService.selectBZ(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台真实点", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台真实点", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String save(
			@Param Xiehztbz bean,
			@Param("operant") Integer operate,
			@Param("xiehzt_insert") ArrayList<Xiehzt> insert,
			@Param("xiehzt_edit") ArrayList<Xiehzt> edit , 
			@Param("xiehzt_delete") ArrayList<Xiehzt> delete){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(xiehztService.save(bean,operate,insert, edit, delete,getLoginUser().getUsername()),"i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件验收比例设置", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String get(@Param Xiehzt bean){
		try {
			setResult("result", xiehztService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String list(@Param Xiehzt bean) {
		try {
			setResult("result", xiehztService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 删除 卸货站台编组
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String remove(@Param Xiehztbz bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			Message m = new Message(xiehztService.doDelete(bean),"i18n.ckx.cangk.i18n_xiehzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台编组", "数据失效");
		}catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台编组", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map);
		return AJAX;
	}
	
	
	/**
	 * 获得大站台编号,如果用户中心为空，则默认为当前用户中心
	 * @param usercenter
	 * @author hezg
	 * @date 2015-1-27
	 * @return String usercenter
	 */
	public String getDaztbhCount(@Param("daztbh") String daztbh,@Param("usercenter") String usercenter){
		try{
			if(usercenter==null||"".equals(usercenter)){
				usercenter=getLoginUser().getUsercenter();
			}
			/*Map<String,String> params = new HashMap<String,String>();
			params.put("daztbh", daztbh);
			params.put("usercenter", usercenter);*/
			Xiehzt bean = new  Xiehzt();
			bean.setUsercenter(usercenter);
			bean.setDaztbh(daztbh);
			setResult("result", xiehztService.getDaztbhCount(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台", "查询大站台编号");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台", "查询大站台编号", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	

}
