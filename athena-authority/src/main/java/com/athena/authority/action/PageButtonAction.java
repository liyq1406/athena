/**
 * 
 */
package com.athena.authority.action;

import com.athena.authority.entity.PageButton;
import com.athena.authority.service.PageButtonService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PageButtonAction extends ActionSupport {
	@Inject
	private PageButtonService pageButtonService;

	/**
	 * 1：主页面
	 */
	public String execute(@Param PageButton bean, @Param("operant") Integer operant) {
		return "select";
	}
	
	public String list(@Param PageButton bean) {
		setResult("result", pageButtonService.list(bean));
		return AJAX;
	}
}
