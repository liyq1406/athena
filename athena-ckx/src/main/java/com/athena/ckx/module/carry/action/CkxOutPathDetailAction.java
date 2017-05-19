package com.athena.ckx.module.carry.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.carry.CkxWaibwl;
import com.athena.ckx.entity.carry.CkxWaibwlxx;
import com.athena.ckx.module.carry.service.CkxWaibwlxxService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxOutPathDetailAction extends ActionSupport{
	@Inject
	private CkxWaibwlxxService outdetailService;
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
	public String execute(@Param CkxWaibwl bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxWaibwlxx bean) {
		setResult("result", outdetailService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "外部物流详细路径", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 获取对象
	 * @param bean
	 * @return
	 */
	public String get(@Param CkxWaibwlxx bean){
		setResult("result", outdetailService.get(bean));
		return AJAX;
	}
	
	
	/**
	 * 获取路径编号集合
	 * @return
	 */
	public String getSelectLujbhCode(){
		setResult("result", outdetailService.getSelectLujbhCode(getLoginUser()));
		return AJAX;
	}
	
	/**
	 * 保存用户
	 * @param bean
	 * @param newlujbh
	 * @param operant
	 * @return
	 */
	public String save(@Param CkxWaibwlxx bean,@Param("newlujbh") String newlujbh,@Param("operant") Integer operant){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message",  outdetailService.save(bean,newlujbh,operant,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "外部物流详细路径", "数据保存结束");
		} catch (RuntimeException e) {
			setResult("success",false );
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "外部物流路径", "数据保存结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	
	/**
	 * 物理删除
	 * @param bean
	 * @return
	 */
	public String remove(@Param CkxWaibwlxx bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", outdetailService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "外部物流详细路径", "数据保存结束");
		} catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "外部物流详细路径", "数据保存结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	/**
	 * 获取对象
	 * @param bean
	 * @return
	 */
	public String getWaibwlxxMaxXuh(@Param CkxWaibwlxx bean){
		setResult("result", outdetailService.getWaibwlxxMaxXuh(bean));
		return AJAX;
	}
//	/**
//	 * 根据路径编号获得第一个物理点对象 ,用于外部物流路径添加页面ajax联动
//	 * @param lujbh  物理点编号
//	 * @return
//	 */
//	public String getFastWuldByLujbh(@Param("lujbh") String lujbh){
//		CkxWaibwlxx bean =new CkxWaibwlxx();
//		bean.setLujbh(lujbh);
//		bean.setUsercenter(getLoginUser().getUsercenter());
//		//根据路径编号获取第一个物理点对象
//		setResult("result", outdetailService.getFastWuldByLujbh(bean));
//		return AJAX;
//	}
}