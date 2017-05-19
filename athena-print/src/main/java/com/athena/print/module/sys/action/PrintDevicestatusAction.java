/**
 * 
 */
package com.athena.print.module.sys.action;

import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.print.entity.sys.PrintDevicestatus;
import com.athena.print.module.sys.service.PrintDevicestatusService;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PrintDevicestatusAction extends ActionSupport {
	@Inject
	private PrintDevicestatusService printDevicestatusService;
	/**
	 * 获取用户信息
	 * @Date 12/02/28
	 * @return LoginUser
	 */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param PrintDevicestatus bean) {
		//页面初始化显示的页面 并获取对应的用户中心
		setResult("loginUsercenter",getLoginUser().getUsercenter());
		return  "select";
	}
	
	/**
	 * 数据查询
	 * @param bean
	 * @return
	 */
	public String queryDevice(@Param PrintDevicestatus bean) {
		//按打印机编号来查询 集合
		setResult("result", printDevicestatusService.select(bean));
		return AJAX;
	}
	
	
	/**
	 * 数据查询
	 * @param bean
	 * @return
	 */
	public String query(@Param PrintDevicestatus bean) {
		//设置打印机编号
		bean.setSpcode(this.getParam("sdevicecode"));
		//按打印机编号来查询 集合
		setResult("result", printDevicestatusService.select(bean));
		return AJAX;
	}
	
	/**
	 * 查询出空闲的打印机
	 * @param bean
	 * @return
	 */
	public String queryStatus(@Param PrintDevicestatus bean) {
		//查询空闲的打印机
		setResult("result", printDevicestatusService.selectStatus(bean));
		return AJAX;
	}
}
