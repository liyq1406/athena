package com.athena.xqjs.module.maoxq.action;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.maoxq.JlvXinax;
import com.athena.xqjs.module.maoxq.service.JlvXinaxCompareService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：DdbhClvCompareAction
 * <p>
 * 类描述： ddbh clv 比较
 * </p>
 * 创建人：gswang
 * <p>
 * 创建时间：2014-12-18
 * </p>
 * 
 * @version
 * 
 */

@Component
public class JlvXinaxmpareAction extends ActionSupport {

	@Inject
	private JlvXinaxCompareService jlvxinaxCompareService;
	
	private final Log log = LogFactory.getLog(JlvXinaxmpareAction.class);
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private DownLoadServices downloadServices = null;
	
	// 获取用户信息
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser();
	}

	/**
	 * 页面初始化，执行跳转
	 */
	public String queryjlvxinax() {
		setResult("usercenter",getUserInfo().getUsercenter());
		return "success";
	}
	
	/**
	 * 查询
	 */
	public String jlvxinaxCompare(@Param JlvXinax jlvXinax) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "NUP vs PDS比较", "NUP vs PDS比较查询开始");
		Map<String, String> map = getParams();
		String kString = "";
		jlvxinaxCompareService.getDays(getParam("xuqbc"), 9,map);
		
		setResult("result", jlvxinaxCompareService.selectDdbhclv(getParams(), jlvXinax));
		setResult("kaisrq", kString);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "NUP vs PDS比较", "NUP vs PDS比较查询结束");
		return AJAX;
	}	
	
	/**
	 * 查询
	 */
	public String showXinaxDate(@Param JlvXinax jlvXinax) {
		Map<String, String> map = getParams();
		String kString = "";
		kString = jlvxinaxCompareService.getDays(getParam("xuqbc"), 9,this.getParams()).toString();
		setResult("kaisrq", kString);
		return AJAX;
	}	
	
	// ddbh clv 比较下载
	public String downLoadJlvXinaxCompare(@Param JlvXinax bean) throws IllegalArgumentException, IntrospectionException, IllegalAccessException, InvocationTargetException, NullCalendarCenterException {

		// 数据源
		Map<String, Object> dataSource = null;
		Map<String, String> map = getParams();
		Map<String, String> kString = new HashMap<String, String>();
		kString = jlvxinaxCompareService.getDays(getParam("xuqbc"), 9,map);
		bean.setPageNo(1);
		bean.setPageSize(500000);
		dataSource = jlvxinaxCompareService.selectDdbhclv(getParams(), bean);
		dataSource.put("kaisrq", "J0-J8:"+kString.toString());
		// 拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		// 设置下载
		String fileName ="jlvxinaxCompare.ftl";

		downloadServices.downloadFile(fileName, dataSource, response, "JLV vs 新按需需求比较", ExportConstants.FILE_XLS, false);

		// 返回类型一定要是download类型的
		return "downLoad";

	}
}
