package com.athena.ckx.module.transTime.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.transTime.CkxXiehztxqgj;
import com.athena.ckx.module.transTime.service.CkxXiehztxqgjService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 卸货站台需求归集
 * @author kong
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxXiehztxqgjAction extends ActionSupport {
	@Inject
	private CkxXiehztxqgjService ckxXiehztxqgjService;
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param CkxXiehztxqgj bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}
	
	/**
	 * 获取卸货站台需求归集
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxXiehztxqgj bean){
		try {
			ckxXiehztxqgjService.addCkxXiehztxqgj(getLoginUser());
			setResult("result", ckxXiehztxqgjService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台需求归集", "数据查询结束");
		} catch (ServiceException e) {//如果添加异常则显示异常信息
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台需求归集", "数据查询结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
//	/**
//	 * 添加卸货站台需求归集
//	 * @return
//	 */
//	public String addCkxXiehztxqgj(){
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("message",ckxXiehztxqgjService.addCkxXiehztxqgj(getLoginUser()));
//		setResult("result",map );
//		return AJAX;
//	}
}
