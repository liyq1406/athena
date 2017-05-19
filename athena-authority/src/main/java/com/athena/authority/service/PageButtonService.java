/**
 * 代码声明
 */
package com.athena.authority.service;

import com.athena.authority.AuthorityConstants;
import com.athena.authority.entity.PageButton;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;


@Component
public class PageButtonService extends BaseService{
	/**
	 * 获取模块空间
	 */
	@Override
	protected String getNamespace() {
		return AuthorityConstants.MODULE_NAMESPACE;
	}

	public void delete(PageButton delButton) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(getNamespace()+".deletePageButton", delButton);
	}
	
}