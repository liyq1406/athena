package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.CkxTidj;
import com.athena.ckx.module.xuqjs.service.CkxTidjService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 替代件
 * @author qizhongtao
 * @date 2012-3-28
 * */
@Component ( scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxTidjAction extends ActionSupport{
	
	@Inject
	private CkxTidjService tidjService;
	
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2018-4-18
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 主页面
	 * @param bean
	 * @author qizhongtao
	 * @date 2012-3-28 
	 * */
	public String execute(){
		setResult("usercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @author qizhongtao
	 * @date 2012-3-28 
	 * */
	public String query(@Param CkxTidj bean){
		try{
			setResult("result", tidjService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "替代件", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "替代件", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 单数据查询
	 * @param bean
	 * @author denggq
	 * @date 2012-5-9 
	 * */
	public String get(@Param CkxTidj bean){
		try{
			setResult("result", tidjService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "替代件", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "替代件", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 多数据查询
	 * @param bean
	 * @author denggq
	 * @date 2012-5-9 
	 * */
	public String list(@Param CkxTidj bean){
		try{
			setResult("result", tidjService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "替代件", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "替代件", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	/**
	 * 行编辑保存
	 * @param bean
	 * @author qizhongtao
	 * @date 2012-3-28
	 * */
	public String save(@Param("insert") ArrayList<CkxTidj> insert,@Param("edit") ArrayList<CkxTidj> edit,@Param("delete") ArrayList<CkxTidj> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(tidjService.save(insert,edit,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "替代件", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "替代件", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
}
