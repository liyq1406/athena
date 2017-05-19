/**
 * 
 */
package com.athena.authority.service;

import com.athena.authority.AuthorityConstants;
import com.athena.authority.entity.Log;
import com.athena.component.service.BaseService;
import com.toft.core3.container.annotation.Component;

/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
@Component
public class LogService  extends BaseService<Log>{
	@Override
	protected String getNamespace() {
		return AuthorityConstants.MODULE_NAMESPACE;
	}
}
