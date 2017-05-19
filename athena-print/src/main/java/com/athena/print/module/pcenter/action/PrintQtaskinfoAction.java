/**
 * 
 */
package com.athena.print.module.pcenter.action;

import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.module.pcenter.service.PrintQtaskinfoService;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PrintQtaskinfoAction extends ActionSupport {
	@Inject
	private PrintQtaskinfoService printQtaskinfoService;
	
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param PrintQtaskinfo bean) {
		//初始化显示的页面
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param PrintQtaskinfo bean) {
		//子作业的分页查询
			setResult("result", printQtaskinfoService.select(bean));
		return AJAX;
	}
	
}
