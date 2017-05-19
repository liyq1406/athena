/**
 * 
 */
package com.athena.authority.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.crypto.hash.Md5Hash;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.entity.User;
import com.athena.authority.service.UserService;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class UserAction extends ActionSupport {
	@Inject
	private UserService userService;
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 用户管理主页面查询
	 * @param bean
	 * @return
	 */
	public String execute(@Param User bean){
		this.setRequestAttribute("postTree", userService.getPostUsercenterTree());
		return "select";
	}

	/**
	 * 数据查询
	 * @param bean
	 * @return
	 */
	public String query(@Param User user) {
		//去除查询条件中的空格
		String loginname = user.getLoginname();
		String name = user.getName();
		String loginnameTemp = "";
		String nameTemp = "";
		if(loginname!=null){
			loginnameTemp = loginname.trim();
		}
		if(name!=null){
			nameTemp = name.trim();
		}
		user.setLoginname(loginnameTemp);
		user.setName(nameTemp);
		setResult("result", userService.select(user));
		
		return AJAX;
	}
	
	/**
	 * 单记录查询用户信息
	 * @param bean
	 * @return
	 */
	public String get(@Param User user) {
		setResult("result", userService.get(user));
		return AJAX;
	}
	
	/**
	 * 保存
	 * @param bean
	 * @return
	 */
	public String save(@Param("operant") String operant,@Param User user) {
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		user.setBiaos("1");
		user.setLoginname(user.getLoginname().trim());
		if(operant.equals("1")){
			String msg = userService.validateOnlyUser(user);
			if(msg.equals("0")){
				try{
					user.setCreator(loginUser.getUsername());
					user.setCreateTime(DateUtil.curDateTime());
					userService.save(user);
					setResult("result", new Message("保存成功!"));
					userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户管理","新增数据"+user.getLoginname()+"操作成功");
				}catch(Exception e){
					userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户管理","新增数据"+user.getLoginname()+"操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
				}
			}else{
				setErrorMessage("保存失败，该用户登录名已存在!");
			}
			
		}else if(operant.equals("2")){
			try{
				user.setMender(loginUser.getUsername());
				user.setModifyTime(DateUtil.curDateTime());
				userService.save(user);
				setResult("result", new Message("保存成功!"));
				userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户管理","修改数据"+user.getLoginname()+"操作成功");
			}catch(Exception e){
				userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户管理","修改数据"+user.getLoginname()+"操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
			}
		}
		return AJAX;
	}
	
	/**
	 * 删除记录
	 * @param bean
	 * @return
	 */
	public String remove(@Param User user) {
		setResult("result", userService.doDelete((user)));
		return AJAX;
	}
	/**
	 * 重置密码
	 * @param userIds
	 * @return
	 */
	public String resetPassword(@Param("userIds") ArrayList<String> userIds){
		try{
			LoginUser loginUser = AuthorityUtils.getSecurityUser();
			for(int i=0;i<userIds.size();i++){
				User user = new User();
				user.setId(userIds.get(i));
				user.setPassword(new Md5Hash("123456").toHex());
				user.setMender(loginUser.getUsername());
				user.setModifyTime(DateUtil.curDateTime());
				userService.resetPassword(user);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户管理","重置密码操作成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户管理","重置密码操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	/**
	 * 保存用户组
	 * @param sysPosts
	 * @param postIds
	 * @param userId
	 * @return
	 */
	public String saveRPostUser(@Param("sysPosts") ArrayList<String> sysPosts,@Param("userId")String userId){
		/**
		 * 保存系统用户组、用户信息
		 */
		try{
			userService.saveRPostUser(sysPosts,userId);
			/**
			 * 设置返回message
			 */
			Map<String,String> message = new HashMap<String,String>();
			message.put("msg","保存成功!");
			setResult("result", message);
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户管理","保存用户组操作成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户管理","保存用户组操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	/**
	 * 获取指定用户下的用户组信息
	 * @param id
	 * @return
	 */
	public String listUserPosts(@Param("id")String id){
		/**
		 * 获取指定用户的用户组
		 */
		setResult("result", userService.listUserPosts(id));
		return AJAX;
	}
	/**
	 * 启/停用户组
	 * @return
	 */
	public String changeUserActive(@Param("userIds") ArrayList<String> userIds,@Param("actives")ArrayList<String> actives){
		User user = new User();
		//设置用户组ID主键
		user.setId(userIds.get(0));
		//设置所选择用户组状态
		String active = actives.get(0);
		String nowActive = "";
		String oper = "";
		if(active!=null&&!"".equals(active)&&active.equals("0")){
			nowActive = "1";
			oper= "启动";
		}
		if(active!=null&&!"".equals(active)&&active.equals("1")){
			nowActive = "0";
			oper= "停用";
		}
		user.setBiaos(nowActive);
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		user.setMender(loginUser.getUsername());
		user.setModifyTime(DateUtil.curDateTime());
		//启/停用户
		try{
			userService.changeUserActive(user);
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"用户管理",oper+"用户组操作成功");
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"用户管理",oper+"用户组操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	/*
	 * 未用
	 */
	public String validateOnlyUser(@Param("loginname") String loginname){
		User user = new User();
		user.setLoginname(loginname);
		setResult("result", userService.validateOnlyUser(user));
		return AJAX;
	}
	
	/**
	 * 用户密码修改
	 * @param username
	 * @param password
	 * @return
	 */
	public String updateUserPassword(@Param("username") String username,@Param("password") String password,@Param("oldpassword") String oldpassword){
		try{
			Map<String,String> map = new HashMap<String,String>();
			map.put("username",username);
			map.put("password", password);
			map.put("oldpassword", oldpassword);
			String pwdmodtime = DateUtil.curDateTime();
			map.put("pwdmodtime", pwdmodtime);
			setResult("result", userService.updateUserPassword(map));
			setResult("flag", "1");
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,"修改密码","修改密码操作成功");
		}catch(Exception e){
			setResult("flag", "0");
			setResult("result", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY,"修改密码","修改密码操作失败",CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
}
