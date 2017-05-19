package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.CkxXiaohcyssk;
import com.athena.ckx.module.xuqjs.service.CkxXiaohcysskService;
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
 * 小火车运输时刻表
 * @author denggq
 * @date 2012-4-12
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxXiaohcysskAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * @description 注入XiaohcysskService
	 * @author denggq
	 * @date 2012-4-12
	 * @return bean
	 */
	@Inject
	private CkxXiaohcysskService xiaohcysskService;
	
	
	/**
	 * @description 获得登录人信息
	 * @author denggq
	 * @date 2012-4-12
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * @description 跳转到初始页面
	 * @author denggq
	 * @date 2012-4-12
	 * @return String
	 */
	public String execute(){
		//字段权限
		@SuppressWarnings("rawtypes")
		Map map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		
		String  params = 
			    "edit_kaisbhsj:     root,ZXCPOA;"+
			    "edit_chufsxsj:     root,ZXCPOA;";
		
		for(String s0:params.split(";")){
			String name = s0.split(":")[0].trim();		//字段隐藏属性名
			String roles = s0.split(":")[1].trim();	//所有不隐藏的角色
			if(roles.contains(key)){
				setResult(name, true);
			}else{
				setResult(name, false);
			}
		}
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	
	/**
	 * @description 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-4-12
	 * @return String
	 */
	public String query(@Param CkxXiaohcyssk bean){
		try {
			setResult("result", xiaohcysskService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻表", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻表", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * @description 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-4-12
	 * @return String
	 */
	public String get(@Param CkxXiaohcyssk bean ){
		try {
			setResult("result", xiaohcysskService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻表", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻表", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * @description 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-4-12
	 * @return String
	 */
	public String list(@Param CkxXiaohcyssk bean) {
		try {
			setResult("result", xiaohcysskService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻表", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻表", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * @description 失效
	 * @param bean
	 * @author denggq
	 * @date 2012-4-12
	 * @return String
	 */
	public String remove(@Param CkxXiaohcyssk bean){
		
		bean.setEditor(getLoginUser().getUsername());			//修改人
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());	//修改时间
		
		try {
			setResult("result", xiaohcysskService.doDelete(bean));	//逻辑删除
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻表", "数据删除");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻表", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			
		}
		return AJAX;
	}
	
	/**
	 * @description 保存
	 * @author denggq
	 * @date 2012-4-12
	 * @return String
	 */
	public String save(@Param("insert") ArrayList<CkxXiaohcyssk> insert,@Param("edit") ArrayList<CkxXiaohcyssk> edit , @Param("delete") ArrayList<CkxXiaohcyssk> delete) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(xiaohcysskService.save(insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻表", "数据保存");
		}catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻表", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", map);
		return AJAX;
	}
	
	
	/**
	 * @description 计算小火车运输时刻模板
	 * @author denggq
	 * @date 2012-4-20
	 * @return String
	 */
	public String calculate() {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(xiaohcysskService.calculateXiaohcYssk(getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "小火车运输时刻表", "计算小火车运输时刻表");
		}catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "小火车运输时刻表", "计算小火车运输时刻表", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", map);
		return AJAX;
		
	}
}
