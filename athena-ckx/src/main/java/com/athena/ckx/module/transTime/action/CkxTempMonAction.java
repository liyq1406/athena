package com.athena.ckx.module.transTime.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.transTime.CkxTempMon;

import com.athena.ckx.module.transTime.service.CkxTempMonService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 模拟次数
 * @author dsimedd001
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxTempMonAction extends ActionSupport {
	@Inject
	private CkxTempMonService tempMonService;//模拟计算service
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 获取当前用户信息
	 * @author hj
	 * @Date 2012-03-22
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	/**
	 * 分页查询方法
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxTempMon bean) {
		if("2".equals(bean.getDingszt())){
			return AJAX;
		}
		bean.setUsercenter(getLoginUser().getUsercenter());
		setResult("result", tempMonService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "模拟次数", "数据查询结束");
		return  AJAX;
	}
	/**
	 * 数据操作
	 * 计算运输时间模板方法入口 
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param jisfs
	 * @return
	 */
	public String save(@Param("mon_insert") ArrayList<CkxTempMon> insert,			
			@Param("mon_delete") ArrayList<CkxTempMon> delete,
			@Param("jisfs") String jisfs){
		Map<String,String> message = new HashMap<String,String>();
		Map<String,String> map = new HashMap<String, String>();
		try {   // jisfs 计算方式（是定时计算还是即时计算）
			Message m=new Message(tempMonService.save(insert,delete,jisfs, getLoginUser(),map),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "计算时间模板", "计算结束");
		} catch (Exception e) {
			if(map!=null && map.get("xiehztbhs")!= null && !"".equals(map.get("xiehztbhs"))){
				tempMonService.deletedsjspzJS(map);
			}
			setResult("success",false);
			message.put("message", e.getMessage()!=null?e.getMessage():"计算失败");
			userOperLog.addError(CommonUtil.MODULE_CKX, "计算时间模板", "计算结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
}
