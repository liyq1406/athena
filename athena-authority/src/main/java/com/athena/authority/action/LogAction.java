/**
 * 
 */
package com.athena.authority.action;

import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

import com.athena.authority.entity.Log;
import com.athena.authority.service.LogService;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LogAction extends ActionSupport {
	@Inject
	private LogService busLogService;

	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param Log bean) {
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param Log bean) {
		setResult("result", busLogService.select(bean));
		return AJAX;
	}
	
	/**
	 * 集合查询
	 * @param bean
	 * @return
	 */
	public String list(@Param Log bean) {
		setResult("result", busLogService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param Log bean) {
		setResult("result", busLogService.get(bean));
		return AJAX;
	}
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param Log bean) {
		setResult("result", busLogService.doDelete(bean));
		return AJAX;
	}
}
