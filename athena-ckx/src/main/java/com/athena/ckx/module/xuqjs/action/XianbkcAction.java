package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Xianbkc;
import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.module.xuqjs.service.XianbkcService;
import com.athena.ckx.module.xuqjs.service.XianbkcService;
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
 *  盈余修正
 * @author zbb
 * @date 2015-11-20
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class XianbkcAction extends ActionSupport {
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private XianbkcService xianbkcService;
	
	
	/**
	 * 获得登录人信息
	 * @author denggq
	 * @date 2012-4-10
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 跳转到初始页面
	 * @author zbb
	 * @date 2015-11-20
	 * @return String
	 */
	public String execute(){		
		return "select";
	}
	
	/**
	 * 根据条件查询Xianbkc列表
	 * @param Xianbkc Xianbkc对象
	 * @author zbb
	 * @return String
	 */
	public String query(@Param Xianbkc xianbkc){
		try {
			Map<String, Object> resultMap = xianbkcService.selectXianbkc(xianbkc);			
			setResult("result", resultMap);		
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "线边库存", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "线边库存", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}		
		return AJAX;
	}
		
	/**
	 * @description 线边库存保存，修改，删除
	 * @param bean Xianbkc对象 
	 * @author zbb
	 * @date 2015-11-23
	 * @return String
	 */
	public String saves(@Param("insert") ArrayList<Xianbkc> insert,@Param("edit") ArrayList<Xianbkc> edit , @Param("delete") ArrayList<Xianbkc> delete) {
		
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(xianbkcService.save(insert,edit,delete,getLoginUser().getUsername()),"i18n.i18n_common");
			message.put("message", m.getMessage());			
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "线边库存", "数据保存");
		}catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "线边库存", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", message);
		return AJAX;
	}
}
