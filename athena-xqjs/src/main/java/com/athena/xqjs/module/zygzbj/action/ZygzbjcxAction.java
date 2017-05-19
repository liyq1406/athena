package com.athena.xqjs.module.zygzbj.action;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.xqjs.entity.zygzbj.ZiygzbjHz;
import com.athena.xqjs.entity.zygzbj.Ziygzbjmx;
import com.athena.xqjs.module.zygzbj.service.ZygzbjcxService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ZygzbjcxAction
 * <p>
 * 类描述：资源跟踪报警查询action
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-02-13
 * </p>
 * 
 * @version 1.0
 * 
 */  
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ZygzbjcxAction extends ActionSupport {

	@Inject
	private ZygzbjcxService zygzbjcx;
	
	/**
	 * 资源跟踪报警计算页面初始化
	 * @param type 查询类型
	 * @return 定位页面
	 */
	public String initCx(@Param("type") String type){
		String foward = "";
		if(type.equals("zq")){
			foward = "zq";
		}else if(type.equals("z")){
			foward = "z";
		}else if(type.equals("r")){
			foward = "r";
		}else if(type.equals("sd")){
			foward = "sd";
		}
		setResult("type", type);
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("usercenter",loginUser.getUsercenter());
		return foward; 
	}
	
	/**
	 * 查询资源跟踪报警汇总 
	 * @param zygzbjhz 查询对象
	 * @return 查询结果
	 */
	public String queryZygzbj(@Param ZiygzbjHz zygzbjhz){
		setResult("result", zygzbjcx.selZygzbjhz(zygzbjhz, getParams()));	
		return AJAX;
	}
	
	/**
	 * 资源跟踪报警明细页面查询初始化
	 * @return 明细页面
	 */
	public String initMx(){
		setResult("removeId", getParam("removeId"));
		setResult("id", getParam("id"));//主键ID
		setResult("xuh", getParam("xuh"));//序号
		setResult("anqkc", getParam("anqkc"));//安全库存
		setResult("cangkdm", getParam("cangkdm"));//安全库存
		Ziygzbjmx mx = zygzbjcx.selZygzbjmx(getParams());
		if(mx != null){
			setResult("lingjbh", mx.getLingjbh());//零件编号
			setResult("startTime", mx.getStarttime());//开始日期
			setResult("endTime", mx.getEndtime());//结束日期
			setResult("baojlx", mx.getBaojlx());//获取报警类型
		}
		return "zygzmx";
	}
	
	/**
	 * 查询资源跟踪报警明细信息
	 * @param zygzbjmx 查询对象
	 * @return 查询结果
	 */
	public String queryZygzbjmx(@Param Ziygzbjmx param){
		setResult("result", zygzbjcx.selZygzbjmxs(param, getParams()));
		return AJAX;
	}
}
