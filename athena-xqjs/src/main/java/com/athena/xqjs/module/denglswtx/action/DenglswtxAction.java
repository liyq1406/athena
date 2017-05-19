package com.athena.xqjs.module.denglswtx.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.AuthorityConst;
import com.athena.authority.util.AuthorityUtils;
import com.athena.xqjs.entity.denglswtx.Denglswtx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.denglswtx.service.DenglswtxService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 登录事务提醒
 * @author WL
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DenglswtxAction extends ActionSupport{

	@Inject
	private DenglswtxService denglswtxService;
	
	/**
	 * 初始化欢迎页面
	 * @return 欢迎页面
	 */
	public String initWelcome(){
		setResult("anxNum", denglswtxService.queryYicbj(Const.JISMK_ANX_CD,AuthorityUtils.getSecurityUser().getUsercenter()));//最近一次按需计算产生的异常报警数量
		setResult("kanBNum", denglswtxService.queryYicbj(Const.JISMK_kANB_CD,AuthorityUtils.getSecurityUser().getUsercenter()));//最近一次看板产生的异常报警数量
		setResult("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());//用户中心
		//denglswtxService.insertCkxShiwtx();
		return "welcome";
	}
	
	/**
	 * 解决参考系登录事物提醒
	 * @return
	 */
	public String jiejCkxShiwtx(@Param("edit") ArrayList<Denglswtx> edit){
		try {
			denglswtxService.jiejCkxShiwtx(edit);
			setResult("result", "解决成功");
		} catch (Exception e) {
			setErrorMessage("解决事物提醒异常"+e.getMessage());
		}
		return AJAX;
	}
	
	/**
	 * 查询参考系登录事务提醒
	 * @param denglswtx 登录事务提醒
	 * @return 登录事务提醒信息集合
	 */
	public String queryCkxShiwtx(@Param Denglswtx denglswtx){
		Map<String,String> param = getParams();
		//如果不是POA,只查询自己用户组下的相关信息
		if(!Const.POAcode.equals(AuthorityUtils.getSecurityUser().getPostAndRoleMap().get(Const.QUANX_POA))){
			String jihyz = CommonFun.strNull(AuthorityUtils.getSecurityUser().getPostAndRoleMap().get(AuthorityConst.ZU_JIHUAY));//计划员组
			//如果为计划员,则查询计划员下的提醒
			if(!StringUtils.isEmpty(jihyz)){
				param.put("jihyz", jihyz);
			}
			String wulgyyz = CommonFun.strNull(AuthorityUtils.getSecurityUser().getPostAndRoleMap().get(AuthorityConst.ZU_WULGYY));//物流工艺员组
			if(!StringUtils.isEmpty(wulgyyz)){
				param.put("wulgyyz", wulgyyz);
			}
		}
		setResult("result", denglswtxService.queryCkxShiwtx(denglswtx, param));
		return AJAX;
	}
}

