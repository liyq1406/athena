package com.athena.ckx.module.cangk.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Fahzt;
import com.athena.ckx.entity.cangk.Kuckzzysz;
import com.athena.ckx.entity.cangk.Wuldrqgx;
import com.athena.ckx.module.cangk.service.KuckzzyszService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 王宇
 * @author wangyu
 * @date 2013-07-30
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KuckzzyszAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 注入KuckzzyszService
	 * @author wangyu
	 * @date 2012-12-10
	 */
	@Inject 
	private KuckzzyszService kuckzzyszService;
	
	
	/**
	 * 登录人信息
	 * @author wangyu
	 * @date 2012-12-10
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 * @author wangyu
	 * @date 20121210
	 */
	public String execute(@Param Kuckzzysz bean) {
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return  "select";
	}

	
	/**
	 * 分页查询
	 * @param bean
	 * @author wangyu
	 * @date 20121210
	 * @return String
	 */
	public String query(@Param Kuckzzysz bean) {
		try{
			setResult("result", kuckzzyszService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库存快照资源设置", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "库存快照资源设置", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 单记录查询
	 * @param bean
	 * @author wangyu
	 * @date 20121210
	 */
	public String get(@Param Kuckzzysz bean) {
		try{
			setResult("result", kuckzzyszService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库存快照资源设置", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "库存快照资源设置", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 多记录查询
	 * @param bean
	 * @author wangyu
	 * @date 20121210
	 */
	public String list(@Param Kuckzzysz bean) {
		try{
			setResult("result", kuckzzyszService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库存快照资源设置", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "库存快照资源设置", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 数据修改（多条数据操作）
	 * @param bean
	 * @author wangyu
	 * @date 2012-12-10
	 */
	public String save(@Param("insert") ArrayList<Kuckzzysz> insert,@Param("edit") ArrayList<Kuckzzysz> edit,@Param("delete") ArrayList<Kuckzzysz> delete) {
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(kuckzzyszService.save(insert,edit,delete),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "库存快照资源设置", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "库存快照资源设置", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
}
