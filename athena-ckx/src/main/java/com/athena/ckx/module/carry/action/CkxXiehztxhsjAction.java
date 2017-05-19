package com.athena.ckx.module.carry.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.carry.CkxXiehztxhsj;
import com.athena.ckx.module.carry.service.CkxXiehztxhsjService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


/**
 * 卸货站台循环时间
 * 2012-02-12
 * @author kong
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxXiehztxhsjAction extends ActionSupport{
	/*业务服务*/
	@Inject
	private CkxXiehztxhsjService ckxXiehztxhsjService;
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
	 * @param bean 实体
	 * @return
	 */
	public String execute(@Param CkxXiehztxhsj bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}
	
	/**
	 * 分页查询
	 * @param bean 实体
	 * @return
	 */
	public String query(@Param CkxXiehztxhsj bean) {
		setResult("result", ckxXiehztxhsjService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台循环时间", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 单对象获取
	 * @param bean
	 * @return
	 */
	public String get(@Param CkxXiehztxhsj bean){
		setResult("result", ckxXiehztxhsjService.get(bean));
		return AJAX;
	}
	
	/**
	 * 添加数据
	 * @param bean
	 * @return
	 */
	public String addCkxXiehztxhsj(@Param CkxXiehztxhsj bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxXiehztxhsjService.addCkxXiehztxhsj(bean,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台循环时间", "数据添加结束");
		} catch (RuntimeException e) {
			setResult("success",false );
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台循环时间", "数据添加结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	/**
	 * 数据保存（修改）
	 * @param bean
	 */
	public String save(@Param CkxXiehztxhsj bean) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxXiehztxhsjService.save(bean,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台循环时间", "数据保存结束");
		} catch (RuntimeException e) {
			setResult("success",false );
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台循环时间", "数据保存结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param CkxXiehztxhsj bean) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			ckxXiehztxhsjService.doDelete(bean);
			map.put("message", "删除成功！");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台循环时间", "数据删除结束");
		} catch (ServiceException e) {
			map.put("message",e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台循环时间", "数据删除结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 逻辑删除
	 * @param bean
	 * @return
	 */
	public String deleteLogic(@Param CkxXiehztxhsj bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxXiehztxhsjService.deleteLogic(bean,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "卸货站台循环时间", "数据删除结束");
		} catch (RuntimeException e) {
			map.put("message",e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "卸货站台循环时间", "数据删除结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
}
