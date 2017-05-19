/**
 * 
 */
package com.athena.authority.action;

import com.athena.authority.entity.MenuResource;
import com.athena.authority.service.MenuResourceService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class MenuResourceAction extends ActionSupport {
	@Inject
	private MenuResourceService menuResourceService;

	/**
	 * 1：主页面
	 * 2：新增，更新，删除交易
	 * @param bean
	 * @param operant
	 * @return
	 */
	public String execute(@Param MenuResource bean, @Param("operant") Integer operant) {
		return "select";
	}

	/**
	 * 数据查询
	 * @param bean
	 * @return
	 */
	public String query(@Param MenuResource bean) {
		String menuName = bean.getMenuName();
		String temp = "";
		if(menuName!=null){
			temp = menuName.trim();
		}
		bean.setMenuName(temp);
		setResult("result", menuResourceService.select(bean));
		return AJAX;
	}
	
	public String get(@Param MenuResource bean) {
		setResult("result", menuResourceService.get(bean));
		return AJAX;
	}
	
	public String list(@Param MenuResource bean) {
		setResult("result", menuResourceService.list(bean));
		return AJAX;
	}
	
	public String save(@Param MenuResource bean) {
		setResult("result", menuResourceService.save(bean));
		return AJAX;
	}
	
	public String remove(@Param MenuResource bean) {
		setResult("result", menuResourceService.doDelete(bean));
		return AJAX;
	}
}
