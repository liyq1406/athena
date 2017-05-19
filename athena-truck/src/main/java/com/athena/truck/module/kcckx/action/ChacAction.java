package com.athena.truck.module.kcckx.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.truck.entity.Chac;
import com.athena.truck.entity.Chew;
import com.athena.truck.module.kcckx.service.ChacService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
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
public class ChacAction extends ActionSupport{

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
	private ChacService chacService;
	
	
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
	 * 跳转到初始页面
	 * @author liushaung
	 * @date 2015-1-20
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询叉车
	 * @param bean
     * @author liushaung
	 * @date 2015-1-20
	 * @return String
	 */
	
	public String queryChac(@Param Chac bean){
		try {
			setResult("result", chacService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "叉车", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "叉车", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	/**
	 * 查询叉车组
	 * @param bean
	 * @author liushuang
	 * @date 2015-2-5
	 * @return String
	 */
	public String queryChacz() {
		try {
			Map<String,String> params = getParams();
			setResult("result",chacService.queryChacz(params));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "叉车", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "叉车", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 增加查询叉车组
	 * @param bean
	 * @author chenpeng
	 * @date 2015-2-13
	 * @return String
	 */
	public String queryYXChacz() {
		try {
			Map<String,String> params = getParams();
			setResult("result",chacService.queryYXChacz(params));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "叉车", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "叉车", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * @description 保存叉车和车位关系
	 * @param bean
     * @author liushuang
	 * @date 2015-1-20
	 * @return String
	 * @date 2015-1-7
	 */
	public String save(@Param Chac bean,@Param("operant") Integer operant,@Param("chew_insert") ArrayList<Chew> insert ) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Map maps = getParams();
			bean.setUsercenter((String)maps.get("usercenter"));
			bean.setQuybh((String)maps.get("quybh"));
			bean.setDaztbh((String)maps.get("daztbh"));
			bean.setChacbh((String)maps.get("chacbh"));
			if(null!=(String)maps.get("chacmc")){
				bean.setChacmc((String)maps.get("chacmc"));
				
			}
			if(null!=(String)maps.get("chacz")){
				bean.setChacz((String)maps.get("chacz"));
			}
			Message m = new Message(chacService.save(bean,operant,insert,getLoginUser().getUsername()),"系统参数定义");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "等待区域", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "等待区域", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	
	/**
	 * 删除方法
	 * @param bean
     * @author liushaung
	 * @date 2015-1-20
	 * @return String
	 */
	public String remove(@Param Chac bean) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Map maps = getParams();
			bean.setUsercenter((String)maps.get("usercenter"));
			bean.setChacbh((String)maps.get("chacbh"));
			bean.setDaztbh((String)maps.get("daztbh"));
			Message m = new Message(chacService.Delete(bean), "叉车");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "叉车", "数据删除");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "叉车", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	
}
