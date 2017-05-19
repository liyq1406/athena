package com.athena.truck.module.kcckx.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.truck.entity.Chew;
import com.athena.truck.entity.ChewChengys;
import com.athena.truck.module.kcckx.service.ChewChengysService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 车位承运商
 * @author wangliang
 * @date 0215-01-24
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ChewChengysAction extends ActionSupport{

	@Inject
	private UserOperLog userOperLog;

	@Inject
	private ChewChengysService chewChengysService;
	
	@Inject
	private ChewAction chewAction;
	
	
	/**
	 * 登录人信息
	 * @author wangliang
	 * @date 0215-01-24
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author wangliang
	 * @date 0215-01-24
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "init";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-24
	 * @return String
	 */
	public String query(@Param ChewChengys bean){
		try{
			if("2".equals(bean.getChewsx()) || null==bean.getChewsx() ){
				setResult("result", chewChengysService.select(bean));
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位承运商", "数据查询");
			}else{
				return AJAX;
			}
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位承运商", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-24
	 * @return String
	 */
	public String get(@Param ChewChengys bean){
		try {
			setResult("result", chewChengysService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位承运商", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "实际大站台", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-24
	 * @return String
	 */
	public String list(@Param ChewChengys bean) {
		try {
			setResult("result", chewChengysService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位承运商", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位承运商", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	

	/**
	 * 删除
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-24
	 * @return String
	 */
	public String remove(@Param ChewChengys bean,@Param Chew chew){
		try {
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateUtil.curDateTime());
			setResult("result", chewChengysService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "车位承运商", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "车位承运商", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		chewAction.remove(chew);
		return AJAX;
	}
	/**
	 * 分页查询
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-24
	 * @return String
	 */
	public String querygys(@Param ChewChengys bean){
		try{
			bean.setChengysbh(bean.getChengysbh().replace(" ", " "));
			setResult("result", chewChengysService.getx(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "承运商", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "承运商", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
}
