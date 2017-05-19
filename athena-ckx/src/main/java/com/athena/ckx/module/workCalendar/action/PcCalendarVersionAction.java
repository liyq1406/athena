/**
 * 
 */
package com.athena.ckx.module.workCalendar.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.workCalendar.CkxCalendarVersion;

import com.athena.ckx.module.workCalendar.service.PcCalendarVersionService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 日历版次
 * @author kong
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PcCalendarVersionAction extends ActionSupport {
	@Inject
	private PcCalendarVersionService ckxCalendarVersionService;
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
	public String execute(@Param CkxCalendarVersion bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxCalendarVersion bean) {
		if(bean.getBanc()!=null){
			bean.setBanc(bean.getBanc().toUpperCase());
		}
		setResult("result", ckxCalendarVersionService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历版次-pc", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 集合查询
	 * @param bean
	 * @return
	 */
	public String list(@Param CkxCalendarVersion bean) {
		setResult("result", ckxCalendarVersionService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param CkxCalendarVersion bean) {
		setResult("result", ckxCalendarVersionService.get(bean));
		return AJAX;
	}
	/**
	 * 行编辑数据保存
	 * @param bean
	 */
	public String save(@Param("editList") ArrayList<CkxCalendarVersion> editList) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("message", ckxCalendarVersionService.edit(editList,getLoginUser()));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历版次-pc", "数据修改结束");
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 添加版次
	 * @param year
	 * @param center
	 * @param usercode
	 * @return
	 */
	public String addVersion(@Param("year") String year,@Param("center") String center,@Param("usercode") String usercode){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxCalendarVersionService.addVersion(year, center, usercode,getLoginUser()));
		} catch (RuntimeException e) {
			setResult("success",false );
			map.put("message", "数据添加失败");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历版次-pc", "添加版次结束");
		}
		setResult("result",map );
		return AJAX;
	}
	
	
	/**
	 * 复制版次
	 * @param versionCode
	 * @param userCode
	 * @return
	 */
	public String copyVersion(@Param("versionCode") String versionCode,@Param("usercode") String userCode){
		Map<String,String> map = new HashMap<String,String>();
		map.put("message", ckxCalendarVersionService.copyVersion(versionCode, userCode,getLoginUser()));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历版次-pc", "复制版次结束");
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 在版次中添加年份
	 * @param year
	 * @param versionCode
	 * @return
	 */
	public String addDay(@Param("year") String year,@Param("versionCode") String versionCode){
		Map<String,String> map = new HashMap<String,String>();
		map.put("message", ckxCalendarVersionService.addDay(versionCode, year,getLoginUser()));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历版次-pc", "添加日期结束");
		setResult("result",map);
		return AJAX;
	}
	
	/**
	 * 删除版次
	 * @param versionCode
	 * @return
	 */
	public String delVersion(@Param("versionCode") String versionCode){
		Map<String,String> map = new HashMap<String,String>();
		map.put("message",ckxCalendarVersionService.delVersion(versionCode,getLoginUser()));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历版次-pc", "删除版次结束");
		setResult("result",map);
		return AJAX;
	}
	
	/**
	 * 获取版次号
	 * @return
	 */
	public String getVersionNo(@Param CkxCalendarVersion bean){
		setResult("result", ckxCalendarVersionService.getVersionNo(bean));
		return AJAX;
	}
	/**
	 * 修改版次地年周序
	 * @param versionCode
	 * @return
	 */
	public String editCalendarVersionNianzx(@Param CkxCalendarVersion bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxCalendarVersionService.eidtNianzx(bean, getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历版次修改年周序-pc", "修改年周序成功");
		} catch (RuntimeException e) {
			setResult("success",false );
			map.put("message", e.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历版次修改年周序-pc", "修改年周序失败");
		}
		setResult("result",map );
		return AJAX;	
	}
}
