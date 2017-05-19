package com.athena.xqjs.module.maoxq.action;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.xqjs.entity.maoxq.CompareCyc;
import com.athena.xqjs.module.maoxq.service.MaoxqCompareService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class MxqDownLoadExcAction extends ActionSupport {
	// 注入downloadsevices
	@Inject
	private DownLoadServices downloadServices = null;

	@Inject
	private MaoxqCompareService maoxqCompareService;

	// 毛需求比较文件下载
	public String downLoadFileCompare(@Param CompareCyc bean) throws IllegalArgumentException, IntrospectionException, IllegalAccessException, InvocationTargetException, NullCalendarCenterException {

		// 数据源
		Map<String, Object> dataSource = null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("xuqbc", bean.getXuqbc());
		map.put("xuqbc1", getParams().get("xuqbc1"));
		map.put("jihyz", bean.getJihyz());
		map.put("xsfs", bean.getXsfs());
		map.put("xuqlx", bean.getXuqlx());
		map.put("mode", getParams().get("mode"));
		if (!getParams().get("mode").isEmpty()) {
			dataSource = maoxqCompareService.comparePpOrPs(map, bean, true);
		}
		// 拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		// 设置下载
		String fileName = null;
		if (bean.getXuqlx().equals("Cyc")) {
			fileName = "mxqCompareCyc.ftl";
		} else {
			fileName = "mxqCompareWeek.ftl";
		}
		downloadServices.downloadFile(fileName, dataSource, response, "毛需求比较", ExportConstants.FILE_XLS, false);

		// 返回类型一定要是download类型的
		return "downLoad";

	}

	// 毛需求明细按用户中心与按产线文件下载
	public String downLoadFileDetail(@Param CompareCyc bean, @Param("mode") String mode) throws NullCalendarCenterException {
		Map<String, Object> dataSource = null;
		// 查询毛需求明细
		if (!mode.isEmpty()) {
			dataSource = maoxqCompareService.query(mode, bean, true);
		}
		// 拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		// 设置下载
		String fileName = null;
		if (bean.getXuqlx().equals("Cyc")) {
			fileName = "mxqCyc.ftl";
		} else if (bean.getXuqlx().equals("Week")) {
			fileName = "mxqWeek.ftl";
		} else if (bean.getXuqlx().equals("Days")) {
			fileName = "mxqDays.ftl";
		}
		downloadServices.downloadFile(fileName, dataSource, response, "毛需求明细", ExportConstants.FILE_XLS, false);

		// 返回类型一定要是download类型的
		return "downLoad";

	}
}
