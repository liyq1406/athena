/**
 * 
 */
package com.athena.print.module.sys.action;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.print.entity.sys.Printuser;
import com.athena.print.entity.sys.Printuserinfo;
import com.athena.print.module.sys.service.PrintuserService;
import com.athena.print.module.sys.service.PrintuserinfoService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PrintuserAction extends ActionSupport {
	@Inject
	private PrintuserService printuserService;
	@Inject
	private PrintuserinfoService printuserinfoService;
	@Inject
	private UserOperLog userOperLog;
	
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}
	
	/**
	 * 获取用户信息
	 * @Date 12/02/28
	 * @return LoginUser
	 */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param Printuser bean) {
		//设置用户中心
		setResult("loginUsercenter",getLoginUser().getUsercenter());
		return  "select";
	}
	
	/**
	 * 修改
	 * @param bean,insert,edit,delete
	 * @return
	 */
	public String save(@Param Printuser bean , 			 
			@Param("ckx_postinfo_insert") ArrayList<Printuserinfo> insert ,
			@Param("ckx_postinfo_edit") ArrayList<Printuserinfo> edit ,
			@Param("ckx_postinfo_delete") ArrayList<Printuserinfo> delete ){
		Map<String,String> message = new HashMap<String,String>();
		try {
			//执行 修改按钮操作
			Message m=new Message(printuserService.doSave(bean, insert, edit, delete,getLoginUser().getUsername()),"i18n.print.i18n_print");
			//将提示信息放在Message中
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "用户组操作", "保存数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "用户组操作", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//放在result 在前台页面给出提示
		setResult("result",message);
		return AJAX;
	}
	
	
	/**
	 * 增加
	 * @param bean,insert,edit,delete
	 * @return
	 */
	public String addPrintPost(@Param Printuser bean ,
			@Param("ckx_postinfo_insert") ArrayList<Printuserinfo> insert ,
			@Param("ckx_postinfo_edit") ArrayList<Printuserinfo> edit ,
			@Param("ckx_postinfo_delete") ArrayList<Printuserinfo> delete ){
		Map<String,String> message = new HashMap<String,String>();
		try {
			//执行 新增按钮操作
			Message m=new Message(printuserService.doAdd(bean, insert,getLoginUser().getUsername()),"i18n.print.i18n_print");
			//将提示信息放在Message中
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "用户组操作", "新增数据结束");
		} catch (Exception e) {
			//将提示信息放在Message中
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "用户组操作", "新增数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//放在result 在前台页面给出提示
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param Printuser bean,@Param  Printuserinfo printuserinfo) {
		//用户组页面查询时 用户编号不为空时的查询
		if(null != printuserinfo.getUsercode()&&!"".equals(printuserinfo.getUsercode())){
			//根据用户编号  得到对应的用户组编号
			Printuserinfo print=printuserinfoService.get(printuserinfo);
			if(null == print){
				return AJAX;
			}else{
				//用户组编号 设置到 用户组实体中
				bean.setUserscode(print.getUserscode());
				//在查询用户组表
				setResult("result", printuserService.selectU(bean));
				userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "用户编号来查询用户组", "查询数据结束");
				return AJAX;
			}
		}
		//在查询用户组表
		setResult("result", printuserService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "用户组操作", "查询数据结束");
		return AJAX;
	}
	
	/**
	 * 失效按钮
	 * @param bean
	 * @return
	 */
	public String disable(@Param Printuser bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			String userscode = this.getParam("userscode");
			String active = this.getParam("active");
			bean.setUsercenter(this.getParam("usercenter"));
			//根据用户组编号来查询下面是否有子记录
			List<Printuserinfo> plist = printuserinfoService.list(userscode);
			if(0!=plist.size()){
				//将提示信息放在Message中
				message.put("message", "有子数据，无法失效!");
			}else if("0".equals(active)){
				//将提示信息放在Message中
				message.put("message", "已是无效状态");
			}
			else{
				Message m=new Message(printuserService.doDelete(bean),"i18n.print.i18n_print");
				//将提示信息放在Message中
				message.put("message", m.getMessage());
				userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "用户组操作", "失效数据结束");
			}
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "用户组操作", "失效数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//放在result 在前台页面给出提示
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 有效按钮
	 * @param bean
	 * @return
	 */
	public String nodisable(@Param Printuser bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			String active = this.getParam("active");
			String userscode = this.getParam("userscode");
			bean.setUsercenter(this.getParam("usercenter"));
			if("0".equals(active)){
				//设置用户组编号
				bean.setUserscode(userscode);
				//进行有效的更新操作
				Message m=new Message(printuserService.doUpdate1(bean),"i18n.print.i18n_print");
				//将提示信息放在Message中
				message.put("message", m.getMessage());
				userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "用户组操作", "有效数据结束");
			}else{
				//将提示信息放在Message中
				message.put("message", "已是有效状态");
			}
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "打印机组操作", "有效数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//放在result 在前台页面给出提示
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 通用查询
	 * @param bean
	 */
	public String list(@Param Printuser bean) {
		//用户组的通用查询
		setResult("result", printuserService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param Printuser bean) {
		//用户组的单记录查询
		setResult("result", printuserService.get(bean));
		return AJAX;
	}
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param Printuser bean) {
		//用户组数据的保存
		setResult("result", printuserService.save(bean));
		return AJAX;
	}
	
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param Printuser bean) {
		//用户组数据的删除
		setResult("result", printuserService.doDelete(bean));
		return AJAX;
	}
	
}
