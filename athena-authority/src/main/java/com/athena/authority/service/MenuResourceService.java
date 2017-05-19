/**
 * 代码声明
 */
package com.athena.authority.service;

import com.athena.authority.AuthorityConstants;
import com.athena.authority.entity.MenuResource;
import com.athena.component.service.BaseService;
import com.toft.core3.container.annotation.Component;


@Component
public class MenuResourceService extends BaseService<MenuResource>{
	/**
	 * 获取模块空间
	 */
	@Override
	protected String getNamespace() {
		return AuthorityConstants.MODULE_NAMESPACE;
	}
}