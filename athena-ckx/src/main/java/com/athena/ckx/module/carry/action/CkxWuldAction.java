package com.athena.ckx.module.carry.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.carry.CkxWuldlx;
import com.athena.ckx.entity.carry.CkxYunswld;
import com.athena.ckx.module.carry.service.CkxWuldService;
import com.athena.ckx.module.carry.service.CkxWuldlxService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 物理点
 * @author kong
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxWuldAction extends ActionSupport{
	@Inject
	private CkxWuldService ckxWuldService;
	@Inject
	private CkxWuldlxService ckxWuldlxService;
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
	 * 物理点主页面
	 * @param bean
	 * @return
	 */
	public String executeWuld(@Param CkxYunswld bean) {
		setResult("loginUser", getLoginUser());
		return  "selectWuld";
	}
	
	/**
	 * 查询物理点集合
	 * @param bean
	 * @return
	 */
	public String queryWuld(@Param CkxYunswld bean){
		setResult("result",ckxWuldService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输物理点", "数据查询结束");
		return AJAX;
	}
	
	
	/**
	 * 根据条件获取物理点集合
	 * @param bean
	 * @return
	 */
	public String selectWuld(@Param CkxYunswld bean){
		bean.setBiaos("1");//只查询有效数据
		setResult("result",ckxWuldService.list(bean));
		return AJAX;
	}
	
	

	/**
	 * 根据物理点编号获取物理点对象,用于页面ajax联动
	 * @param lujbh  物理点编号
	 * @return
	 */
	public String getPathNameByPathCode(@Param("lujbh") String lujbh){
		CkxYunswld bean =new CkxYunswld();
		bean.setWuldbh(lujbh);
		bean.setBiaos("1");
		bean= ckxWuldService.get(bean);
		setResult("result", bean);
		return AJAX;
	}
	
	/**
	 * 物理点保存操作
	 * @param addList
	 * @param editList
	 * @param delList
	 * @return
	 */
	public String save(@Param("addList") ArrayList<CkxYunswld> addList,@Param("editList") ArrayList<CkxYunswld> editList,@Param("delList") ArrayList<CkxYunswld> delList){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxWuldService.saveWuld(addList, editList, delList, getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输物理点", "数据保存结束");
		} catch (Exception e) {
			setResult("success",false );
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输物理点", "数据保存结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	
	/*----- 物理点类型 -------*/
	/**
	 * 物理点类型主页面
	 * @param bean
	 * @return
	 */
	public String executeWuldlx(@Param CkxWuldlx bean) {
		return  "selectWuldlx";
	}
	
	/**
	 * 查询物理点类型集合
	 * @param bean
	 * @return
	 */
	public String queryWuldlx(@Param CkxWuldlx bean){
		setResult("result", ckxWuldlxService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "物理点类型", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 查询物理点类型集合(无分页)
	 * @param bean
	 * @return
	 */
	public String selectWuldlx(@Param CkxWuldlx bean){
		bean.setBiaos("1");//有效数据
		setResult("result", ckxWuldlxService.list(bean));
		return AJAX;
	}
	
	
	/**
	 * 保存物理点类型集合
	 * @param addList
	 * @param editList
	 * @param delList
	 * @return
	 */
	public String saveType(@Param("addList") ArrayList<CkxWuldlx> addList,@Param("editList") ArrayList<CkxWuldlx> editList,@Param("delList") ArrayList<CkxWuldlx> delList){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxWuldlxService.saveType(addList, editList, delList, getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "物理点类型", "数据保存结束");
		} catch (Exception e) {
			setResult("success",false );
			map.put("message",e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "物理点类型", "数据保存结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
}