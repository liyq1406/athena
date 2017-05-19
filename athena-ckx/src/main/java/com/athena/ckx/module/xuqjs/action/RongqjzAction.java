package com.athena.ckx.module.xuqjs.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Anjmlxhd;
import com.athena.ckx.entity.xuqjs.Rongqjz;
import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.module.xuqjs.service.RongqjzService;
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
 * 用户中心|容器记账
 * @author xss
 * @Date 2015-2-3
 * 0010495
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class RongqjzAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private RongqjzService rongqjzService;
	
	
	/**
	  * 获取用户信息
	  * @author denggq
	  * @Date 2012-3-19
	  * @return LoginUser
	  */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}

	
	public String execute() {
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	public String queryRongqjz(@Param Rongqjz rongqjz){
		setResult("result", rongqjzService.queryRongqjz(rongqjz) );
		return AJAX;
	}

	public String selectRongqjz(@Param Rongqjz rongqjz){
		Map<String,String> message = new HashMap<String,String>();
		try {
			setResult("result", rongqjzService.selectRongqjz(rongqjz));
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "用户中心|容器记账", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String deleteRongqjz(@Param Rongqjz rongqjz){
		Map<String,String> message = new HashMap<String,String>();
		try {
			setResult("result", rongqjzService.deleteRongqjz(rongqjz));
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "用户中心|容器记账", "删除数据", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	public String saveRongqjz(@Param Rongqjz rongqjz,@Param("operant") Integer operant){
		Map<String,String> message = new HashMap<String,String>();
		try {
				rongqjz.setCreator(getLoginUser().getUsername());
				rongqjz.setCreate_time(DateTimeUtil.getAllCurrTime());
				rongqjz.setEditor(getLoginUser().getUsername());
				rongqjz.setEdit_time(DateTimeUtil.getAllCurrTime());			
			
				//操作符为1为插入操作
				if(1 == operant){
					rongqjzService.saveRongqjz(rongqjz);
					message.put("message", "保存成功!");
				//更新操作
				}else{
					rongqjzService.updateRongqjz(rongqjz);
					message.put("message", "更新成功!");
				}
		
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "用户中心|容器记账", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	
}
