package com.athena.ckx.module.xuqjs.action;

//import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Chexpt;
import com.athena.ckx.module.xuqjs.service.ChexptService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 车型平台关系
 * @author denggq
 * @date 2012-4-18
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ChexptAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;

	/**
	 * 注入ChexptService
	 * @author denggq
	 * @date 2012-4-18
	 * @return bean
	 */
	@Inject
	private ChexptService chexptService;
	
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-4-18
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-4-18
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-4-18
	 * @return String
	 */
	public String query(@Param Chexpt bean){
		try{
			setResult("result", chexptService.selectChexpt(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车型平台关系", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "车型平台关系", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 行编辑保存方法
	 * @author denggq
	 * @Date 2012-4-18
	 * @param bean
	 * @return String
	 */
	public String save(@Param("insert") ArrayList<Chexpt> insert,@Param("edit") ArrayList<Chexpt> edit,@Param("delete") ArrayList<Chexpt> delete) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(chexptService.save(insert, edit, delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车型平台关系", "数据保存");
		}catch (Exception e) {
			//e.printStackTrace();
			//ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			//e.printStackTrace(new PrintStream(bOut));
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车型平台关系", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
}
