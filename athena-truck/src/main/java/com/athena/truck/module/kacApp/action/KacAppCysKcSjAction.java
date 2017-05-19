package com.athena.truck.module.kacApp.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.truck.entity.Chengyskc;
import com.athena.truck.entity.Kacsj;
import com.athena.truck.module.kacApp.service.KacAppCysKcSjService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 承运商-卡车-司机
 * @author CSY
 * @Date 2016-9-8
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KacAppCysKcSjAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private KacAppCysKcSjService kacAppCysKcSjService;
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(KacAppCysKcSjAction.class);
	
	/**
	  * 获取用户信息
	  * @author CSY
	  * @Date 2016-9-8
	  * @return LoginUser
	  */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转页面
	 * @author CSY
	 * @Date 2016-9-8
	 * @return String
	 */
	public String execute() {
		return "select";
	}
	
	/**
	 * 分页查询承运商卡车
	 * @author CSY
	 * @Date 2016-9-9
	 * @param bean
	 * @return String
	 */
	public String query(@Param Chengyskc bean) {
		try{
			setResult("result", kacAppCysKcSjService.query(bean));
			userOperLog.addCorrect("kcApp", "卡车-司机", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError("kcApp", "卡车-司机", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 分页查询卡车司机
	 * @author CSY
	 * @Date 2016-9-9
	 * @param bean
	 * @return String
	 */
	public String queryKacsj(@Param Kacsj bean){
		try{
			setResult("result", kacAppCysKcSjService.queryKacsj(bean));
			userOperLog.addCorrect("kcApp", "卡车-司机", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError("kcApp", "卡车-司机", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 失效
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String remove(@Param Chengyskc bean){
		Map<String,String> params = getParams();
		try {
			bean.setEditor(getLoginUser().getUsername());
			setResult("result", kacAppCysKcSjService.doDelete(bean, params));
			userOperLog.addCorrect("kcApp", "承运商-卡车", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			setResult("result",message);
			userOperLog.addError("kcApp", "承运商-卡车", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * @description 保存承运商-卡车和卡车-司机
	 * @param bean
	 * @param Shijdzts
	 * @return
	 * @author chenpeng
	 * @date 2015-1-7
	 */
	public String saveKacsjxx(@Param Chengyskc bean,@Param("operant") Integer operant,@Param("kacsj_insert") ArrayList<Kacsj> insert,@Param("kacsj_edit") ArrayList<Kacsj> edit , @Param("kacsj_delete") ArrayList<Kacsj> delete ) {
		
		Map<String,String> map = new HashMap<String,String>();
		Map<String,String> params = getParams();
		try {
			Message m = new Message(kacAppCysKcSjService.save(bean,operant,insert,edit,delete,getLoginUser().getUsername(),params),"系统参数定义");
			map.put("message", m.getMessage());
			userOperLog.addCorrect("kcApp", "卡车司机信息", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError("kcApp", "卡车司机信息", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		
		return AJAX;
	}
	
}
