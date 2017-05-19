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
import com.athena.xqjs.entity.maoxq.Ddbhclv;
import com.athena.xqjs.module.maoxq.service.DdbhClvCompareService;
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
public class DdbhClvCompareAction extends ActionSupport {

	@Inject
	private DdbhClvCompareService ddbhClvCompareService;
	
	private final Log log = LogFactory.getLog(DdbhClvCompareAction.class);
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
	public String queryDdbhclv() {
		setResult("usercenter",getUserInfo().getUsercenter());
		return "success";
	}
	
	/**
	 * 查询
	 */
	public String ddbhclvCompare(@Param Ddbhclv ddbhclv) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "NUP vs PDS比较", "NUP vs PDS比较查询开始");
		Map<String, String> map = getParams();
		String kString = "";
		ddbhClvCompareService.getDays(getParam("xuqbc"), 9,map);
		
		setResult("result", ddbhClvCompareService.selectDdbhclv(getParams(), ddbhclv));
		setResult("kaisrq", kString);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "NUP vs PDS比较", "NUP vs PDS比较查询结束");
		return AJAX;
	}	
	
	/**
	 * 查询
	 */
	public String showDdbhDate(@Param Ddbhclv ddbhclv) {
		Map<String, String> map = getParams();
		String kString = "";
		kString = ddbhClvCompareService.getDays(getParam("xuqbc"), 9,this.getParams()).toString();
		setResult("kaisrq", kString);
		return AJAX;
	}	
	
	// ddbh clv 比较下载
	public String downLoadDdbhclvCompare(@Param Ddbhclv bean) throws IllegalArgumentException, IntrospectionException, IllegalAccessException, InvocationTargetException, NullCalendarCenterException {

		// 数据源
		Map<String, Object> dataSource = null;
		Map<String, String> map = getParams();
		Map<String, String> kString = new HashMap<String, String>();
		kString = ddbhClvCompareService.getDays(getParam("xuqbc"), 9,map);
		bean.setPageNo(1);
		bean.setPageSize(500000);
		dataSource = ddbhClvCompareService.selectDdbhclv(getParams(), bean);
		dataSource.put("kaisrq", "J0-J8:"+kString.toString());
		// 拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		// 设置下载
		String fileName ="ddbhclvCompare.ftl";

		downloadServices.downloadFile(fileName, dataSource, response, "NUP vs PDS需求比较", ExportConstants.FILE_XLS, false);

		// 返回类型一定要是download类型的
		return "downLoad";

	}
}
