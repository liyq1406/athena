package com.athena.truck.module.kcckx.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.truck.entity.Dengdqy;
import com.athena.truck.entity.Shijdzt;
import com.athena.truck.module.kcckx.service.DengdqyService;
import com.athena.truck.module.kcckx.service.ShijdztService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 等待区域
 * @author chenpeng
 * @date 2015-1-7
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DengdqyAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private DengdqyService dengdqyService;
	@Inject
	private ShijdztService shijdztService;
	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	
	
	/**
	 * 登录人信息
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 跳转到初始页面
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String query(@Param Dengdqy bean,@Param Shijdzt shijdzt){
		try {
			if(((null!=shijdzt.getDaztbh())&&(null==bean.getQuybh()))&&(!"".equals(shijdzt.getDaztbh())&&null==bean.getQuybh())){
				Shijdzt sd = shijdztService.get(shijdzt);
				if(null == sd){
					return AJAX;
				}else{
					bean.setQuybh(sd.getQuybh());
					bean.setUsercenter(sd.getUsercenter());
					setResult("result", dengdqyService.selectS(bean));
					userOperLog.addCorrect(CommonUtil.MODULE_CKX, "等待区域信息", "数据查询");
					return AJAX;
				}
			}else{
				setResult("result", dengdqyService.select(bean));
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "等待区域", "数据查询");
			}
			
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "等待区域", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String get(@Param Dengdqy bean){
		try {
			setResult("result", dengdqyService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "等待区域", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "等待区域", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String list(@Param Dengdqy bean) {
		try {
			setResult("result", dengdqyService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "等待区域", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "等待区域", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 查询等待区
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String listQuybhqc(@Param Dengdqy bean) {
		try {
			setResult("result", dengdqyService.listquybhqc(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "等待区域", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "等待区域", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	
	/**
	 * 删除
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 * @return String
	 */
	public String remove(@Param Dengdqy bean){
		Map<String,String> params = getParams();
		try {
			Map<String,String> postMap = loginUser.getPostAndRoleMap();
			String quygly = postMap.get("QUYGLY")==null ? "":postMap.get("QUYGLY");
			params.put("post_code", quygly);
			
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateUtil.curDateTime());
			setResult("result", dengdqyService.doDelete(bean, params));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "等待区域", "数据失效");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			setResult("result",message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "等待区域", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * @description 保存区域定义和实际大站台
	 * @param bean
	 * @param Shijdzts
	 * @return
	 * @author chenpeng
	 * @date 2015-1-7
	 */
	public String saveShijdzts(@Param Dengdqy bean,@Param("operant") Integer operant,@Param("shijdzts_insert") ArrayList<Shijdzt> insert,@Param("shijdzts_edit") ArrayList<Shijdzt> edit , @Param("shijdzts_delete") ArrayList<Shijdzt> delete ) {
		
		Map<String,String> map = new HashMap<String,String>();
		Map<String,String> params = getParams();
		try {
			Map<String,String> postMap = loginUser.getPostAndRoleMap();
			String quygly = postMap.get("QUYGLY")==null ? "":postMap.get("QUYGLY");
			params.put("post_code", quygly);
			Message m = new Message(dengdqyService.save(bean,operant,insert,edit,delete,getLoginUser().getUsername(),params),"系统参数定义");
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
	
}
