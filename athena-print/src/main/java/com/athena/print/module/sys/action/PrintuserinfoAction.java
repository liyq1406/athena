/**
 * 
 */
package com.athena.print.module.sys.action;

import com.athena.print.entity.sys.Printuserinfo;
import com.athena.print.module.sys.service.PrintuserinfoService;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PrintuserinfoAction extends ActionSupport {
	@Inject
	private PrintuserinfoService printuserinfoService;
	
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param Printuserinfo bean) {
		//初始化进入的页面
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param Printuserinfo bean) {
		//用户明细的分页查询
		setResult("result", printuserinfoService.select(bean));
		return AJAX;
	}
	
	/**
	 * 通用查询
	 * @param bean
	 */
	public String list(@Param Printuserinfo bean) {
		//用户明细的通用查询
		setResult("result", printuserinfoService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param Printuserinfo bean) {
		//用户明细的单记录查询
		setResult("result", printuserinfoService.get(bean));
		return AJAX;
	}
	/**
	 * 数据保存
	 * @param bean
	 */
	public String save(@Param Printuserinfo bean) {
		//用户明细的保存
		setResult("result", printuserinfoService.save(bean));
		return AJAX;
	}
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param Printuserinfo bean) {
		//用户明细的物理删除
		setResult("result", printuserinfoService.doDelete(bean));
		return AJAX;
	}
	
}
