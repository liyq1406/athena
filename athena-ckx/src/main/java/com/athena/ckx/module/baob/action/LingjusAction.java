package com.athena.ckx.module.baob.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.Lingjus;
import com.athena.ckx.module.baob.service.LingjusService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;



/**
 * 2.15.6.2 零件US/KD冻结零件
 * @author xryuan
 * @date 2013-3-2
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LingjusAction extends ActionSupport{
	@Inject
	private LingjusService lingjusService;
	/**
	 * 用户级操作日志
	 * @author xryuan
	 * @date 2013-3-22
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 获取当前用户信息
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @return String
	 */
	public String execute(){
		//grid表格不显示权限字段	
		setResult("USERCENTER", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author xryuan
	 * @date 2013-3-25
	 * @return String
	 */
	public String query(@Param Lingjus bean){
		try {
			setResult("result", lingjusService.query(bean,getParams()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"));
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表", StringUtils.defaultIfEmpty(getParam("exportXls"), "查询"), CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
	/**
	 * 查询零件状态
	 */
	public String queryztsx(){
		setResult("result", lingjusService.queryztsx());
		return AJAX;
	}
	
	/**
	 * <p>
	 * 导出零件US/KD件
	 * </p>
	 * 
	 * @return
	 */
	public String expLingus(@Param Lingjus bean,@Param("usercenter") String usercenter,@Param("lingjbh") String lingjbh,@Param("elh") String elh,@Param("zhuangtsx") String zhuangtsx,@Param("cangkbh") String cangkbh,@Param("zhizlx") String zhizlx,@Param("startrukrq") String startrukrq,@Param("endrukrq") String endrukrq) {
		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		List<Map<String, String>> list = lingjusService.select(getParams());
		Map<String, String> message = new HashMap<String, String>();
		boolean flag = lingjusService.exportExcel(reqResponse, list);
		if (!flag) {
			String result = "导出文件出错！";
			message.put("message", result);
			setResult("result", message);
		}
		return AJAX;
	}
	
}
