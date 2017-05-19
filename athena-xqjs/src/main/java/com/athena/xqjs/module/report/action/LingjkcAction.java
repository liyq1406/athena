package com.athena.xqjs.module.report.action;

import com.athena.authority.util.AuthorityUtils;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.module.report.service.LingjkcService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 零件库存2.15.6.1action
 * @author WL
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LingjkcAction extends ActionSupport {

	@Inject
	private LingjkcService lingjkcService;
	
	/**
	 * 初始化零件库存报表信息
	 * @return
	 */
	public String initLingjkc(){
		setResult("usercenter",AuthorityUtils.getSecurityUser().getUsercenter());
		return "lingjkc";
	}
	
	/**
	 * 查询零件库存报表信息
	 * @return
	 */
	public String queryLingjkc(@Param Ziykzb ziykzb){
		setResult("result", lingjkcService.queryLingjkc(ziykzb, getParams()));
		return AJAX;
	}
}
