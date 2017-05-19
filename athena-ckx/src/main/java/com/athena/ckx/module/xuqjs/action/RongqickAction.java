package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Rongqick;
import com.athena.ckx.module.xuqjs.service.RongqickService;
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
 * 容器区-仓库
 * @author xss
 * @Date 2015-4-9
 * 0010495
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class RongqickAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private  RongqickService rongqickService;
	
	
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
	

	public String queryRongqick(@Param Rongqick rongqick){
		setResult("result", rongqickService.queryRongqick(rongqick) );
		return AJAX;
	}

	public String selectRongqick(@Param Rongqick rongqick){
		Map<String,String> message = new HashMap<String,String>();
		try {
			setResult("result", rongqickService.selectRongqick(rongqick));
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器区-仓库", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String deleteRongqick(@Param Rongqick rongqick){
		Map<String,String> message = new HashMap<String,String>();
		try {
			setResult("result", rongqickService.deleteRongqick(rongqick));
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器区-仓库", "删除数据", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	public String saveRongqick(@Param Rongqick rongqick,@Param("operant") Integer operant){
		Map<String,String> message = new HashMap<String,String>();
		try {
			rongqick.setCreator(getLoginUser().getUsername());
			rongqick.setCreate_time(DateTimeUtil.getAllCurrTime());
			rongqick.setEditor(getLoginUser().getUsername());
			rongqick.setEdit_time(DateTimeUtil.getAllCurrTime());			
			
				//操作符为1为插入操作
				if(1 == operant){
					rongqickService.saveRongqick(rongqick);
					message.put("message", "保存成功!");
				//更新操作
				}else{
					rongqickService.updateRongqick(rongqick);
					message.put("message", "更新成功!");
				}
		
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "容器区-仓库", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	
	
	
//	
//	
//	public String query(@Param Rongqick bean){
//		try {
//			setResult("result", rongqickService.select(bean));
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器区-仓库", "数据查询");
//		}catch (Exception e) {
//			Map<String,String> map = new HashMap<String,String>();
//			map.put("message", e.getMessage());
//			userOperLog.addError(CommonUtil.MODULE_CKX, "容器区-仓库", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
//			setResult("result",map);
//		}
//		return AJAX;
//	}
//	
//	public String get(@Param Rongqick bean){
//		try {
//			setResult("result", rongqickService.get(bean));
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器区-仓库", "单数据查询");
//		}catch (Exception e) {
//			Map<String,String> map = new HashMap<String,String>();
//			map.put("message", e.getMessage());
//			userOperLog.addError(CommonUtil.MODULE_CKX, "容器区-仓库", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
//			setResult("result",map);
//		}
//		return AJAX;
//	}
//	
//	public String list(@Param Rongqick bean) {
//		try {
//			setResult("result", rongqickService.list(bean));
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器区-仓库", "多数据查询");
//		}catch (Exception e) {
//			Map<String,String> map = new HashMap<String,String>();
//			map.put("message", e.getMessage());
//			userOperLog.addError(CommonUtil.MODULE_CKX, "容器区-仓库", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
//			setResult("result",map);
//		}
//		return AJAX;
//	}
//	
//
//	public String save(@Param("insert") ArrayList<Rongqick> insert,@Param("edit") ArrayList<Rongqick> edit , @Param("delete") ArrayList<Rongqick> delete){		
//		Map<String,String> map = new HashMap<String,String>();
//		try {
//			
//			Message m = new Message(rongqickService.save(insert, edit, delete,getLoginUser().getUsercenter()),"i18n.ckx.xuqjs.i18n_rongqick");
//			map.put("message", m.getMessage());
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "容器区-仓库", "数据保存");
//		}catch (Exception e) {
//			setResult("success", false);
//			map.put("message", e.getMessage());
//			userOperLog.addError(CommonUtil.MODULE_CKX, "容器区-仓库", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
//		}
//		setResult("result", map);
//		return AJAX;
//	}
	
	public String queryRongqqbh(){//查询容器区编号
		LoginUser currentUser = AuthorityUtils.getSecurityUser();
		String usercenter = currentUser.getUsercenter();
		Map<String,String> postMap = currentUser.getPostAndRoleMap();
		String postId = postMap.get("CK_003");		
		
		Map<String,String> map = new HashMap<String,String>();
		
		map.put("usercenter", usercenter);
		map.put("postId", postId);
		
		setResult("result", rongqickService.queryRongqqbh(map));
		return AJAX;
	}
	
	public String queryCangkbh(){//查询仓库
		LoginUser currentUser = AuthorityUtils.getSecurityUser();
		String usercenter = currentUser.getUsercenter();
		Map<String,String> postMap = currentUser.getPostAndRoleMap();
		String postId = postMap.get("CK_001");		
		
		Map<String,String> map = new HashMap<String,String>();
		
		map.put("usercenter", usercenter);
		map.put("postId", postId);
		
		setResult("result", rongqickService.queryCangkbh(map) );
		return AJAX;
	}	
	
}
