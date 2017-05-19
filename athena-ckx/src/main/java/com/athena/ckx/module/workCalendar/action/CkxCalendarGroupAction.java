/**
 * 
 */
package com.athena.ckx.module.workCalendar.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.workCalendar.CkxCalendarGroup;
import com.athena.ckx.module.workCalendar.service.CkxCalendarGroupService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * 工作时间关联
 * 0
 * @author kong
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxCalendarGroupAction extends ActionSupport {
	/**
	 * 工作时间关联逻辑业务层
	 */
	@Inject
	private CkxCalendarGroupService ckxCalendarGroupService;
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
	public String execute(@Param CkxCalendarGroup bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxCalendarGroup bean) {
		//手动填写的编码统一转换为大写
//		if(bean.getBianzh()!=null){
//			bean.setBianzh(bean.getBianzh().toUpperCase());
//		}
		//手动填写的编码统一转换为大写
//		if(bean.getRilbc()!=null){
//			bean.setRilbc(bean.getRilbc().toUpperCase());
//		}
		//手动填写的编码统一转换为大写
//		if(bean.getAppobj()!=null){
//			bean.setAppobj(bean.getAppobj().toUpperCase());
//		}
		//没选择用户中心时，查询到有权限的工作时间
		if(AuthorityUtils.getSecurityUser().getUcList()!=null && AuthorityUtils.getSecurityUser().getUcList().size()>0){
			List<String> uclist=AuthorityUtils.getSecurityUser().getUcList();
			StringBuffer sb=new StringBuffer();
			for (String s : uclist) {
			sb.append("'"+s.substring(1, 2)+"',");
			}
			sb.delete(sb.length()-1, sb.length());
			bean.setUclist(sb.toString());
		}
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历关联", "数据查询开始");
		setResult("result", ckxCalendarGroupService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历关联", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 集合查询
	 * @param bean
	 * @return
	 */
	public String list(@Param CkxCalendarGroup bean) {
		setResult("result", ckxCalendarGroupService.list(bean));
		return AJAX;
	}

	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param CkxCalendarGroup bean) {
		setResult("result", ckxCalendarGroupService.get(bean));
		return AJAX;
	}

	/**
	 * 添加分组
	 * @param bean
	 * @return
	 */
	public String addGroup(@Param CkxCalendarGroup bean){
		Map<String,String> map = new HashMap<String,String>();
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历关联", "数据添加开始");
		String s=ckxCalendarGroupService.addGroup(bean,getLoginUser());
		map.put("message", s);
		if(!"增加成功".equals(s)){
			setResult("success",false );
		}
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历关联", "数据添加结束");
		setResult("result",map);
		return AJAX;
	}

	/**
	 * 数据保存（修改）
	 * @param bean
	 */
	public String save(@Param CkxCalendarGroup bean) {
		Map<String,String> map = new HashMap<String,String>();
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历关联", "数据修改开始");
		map.put("message", ckxCalendarGroupService.save(bean,getLoginUser()));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历关联", "数据修改结束");
		setResult("result",map);
		return AJAX;
	}

	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param CkxCalendarGroup bean) {
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历关联", "数据删除开始");
		setResult("result", ckxCalendarGroupService.doDelete(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "日历关联", "数据删除结束");
		return AJAX;
	}
}
