package com.athena.xqjs.module.kanbyhl.action;

import com.athena.xqjs.module.common.service.XqjsAuthority;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;

@Component
public class WulzAuthorityAction extends ActionSupport {
	@Inject
	private XqjsAuthority xAuthority;

	/**
	 * <P>
	 * 获得内部物流组对应的仓库或者消耗点权限
	 * </P>
	 * 
	 * @return
	 */
	public String getWulzKehd() {
		setResult("result", xAuthority.getWulgyyz(getParam("gonghms"), ""));
		return AJAX;
	}


}
