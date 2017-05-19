package com.athena.ckx.module.transTime.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.transTime.CkxGongysChengysXiehzt;
import com.athena.ckx.module.transTime.service.CkxFrequencyService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 卸货站台送货频次
 * @author kong
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxFrequencyAction extends ActionSupport {
	@Inject
	private CkxFrequencyService ckxFrequencyService;
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
	public String execute(@Param CkxGongysChengysXiehzt bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}
	
	/**
	 * 查询
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxGongysChengysXiehzt bean){
		//0008693 加导出
		setResult("result", ckxFrequencyService.select(bean,getParam("exportXls")));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "计算送货频次", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 计算送货频次
	 * @param list
	 * @return
	 */
	public String compute(@Param("usercenter") String usercenter){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxFrequencyService.compute(getLoginUser(),usercenter));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "计算送货频次", "计算结束");
		} catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "计算送货频次", "计算结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 行编辑保存
	 * @param bean
	 */
	public String save(@Param("editList") ArrayList<CkxGongysChengysXiehzt> editList) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxFrequencyService.edit(editList,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "计算送货频次", "数据修改结束");
		} catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "计算送货频次", "数据修改结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	/**
	 * 单行增加
	 * @param bean
	 */
	public String add(@Param CkxGongysChengysXiehzt bean) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxFrequencyService.insert(bean,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "增加送货频次", "数据增加结束");
		} catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "计算送货频次", "数据增加失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	/**
	 * 单行删除
	 * @param bean
	 */
	public String delete(@Param CkxGongysChengysXiehzt bean) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxFrequencyService.delete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "删除送货频次", "数据删除结束");
		} catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "删除送货频次", "数据删除失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	/**
	 * 归集卸货站台对应的承运商||归集承运商
	 * hj
	 * @return
	 */
	public String listGcbhORXiehzt(@Param CkxGongysChengysXiehzt bean){
		if("root".equals(bean.getEditor())){
			bean.setUsercenter(getLoginUser().getUsercenter());
		}		
		setResult("result",ckxFrequencyService.listGcbhORXiehzt(bean));
		return AJAX;
	}
}
