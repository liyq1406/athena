/**
 * 
 */
package com.athena.ckx.module.paicfj.action;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.util.Assert;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.paicfj.Yunslx;
import com.athena.ckx.entity.paicfj.YunslxJiaof;
import com.athena.ckx.module.paicfj.service.YunslxJiaofService;
import com.athena.ckx.module.paicfj.service.YunslxService;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
/**
 * 运输路线
 * @author hj
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YunslxAction extends ActionSupport {
	@Inject
	private YunslxService yunslxService;//运输路线
	@Inject
	private YunslxJiaofService YunslxJiaofService;//运输路线交付时刻
	@Inject
	private UserOperLog userOperLog;
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}

	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param Yunslx bean) {
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param Yunslx bean) {
		try {
			setResult("result", yunslxService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输路线", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输路线", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query_jiaofsk(@Param YunslxJiaof bean) {
		try {
			Assert.notNull(bean.getYunslxbh(),GetMessageByKey.getMessage("addbygsx"));
			setResult("result", YunslxJiaofService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输路线-交付时刻", "查询数据结束");
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输路线-交付时刻", "查询数据结束",CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",message);
		}
		return AJAX;
	}
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param Yunslx bean,@Param("operant") Integer operant,
			@Param("jfsk_insert") ArrayList<YunslxJiaof> insert,			
			@Param("jfsk_delete") ArrayList<YunslxJiaof> delete) {
		Map<String,String> message = new HashMap<String,String>();
		try {
			bean.setCreator(getLoginUser().getUsername());
			bean.setEditor(getLoginUser().getUsername());
			Message m=new Message(yunslxService.save(bean,operant,insert,delete),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输路线", "保存数据结束");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输路线", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param Yunslx bean) {
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m=new Message(yunslxService.remove(bean),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输路线", "删除数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输路线", "删除数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
}
