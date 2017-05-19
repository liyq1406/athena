package com.athena.xqjs.module.ppl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.xqjs.entity.ppl.Niandygmx;
import com.athena.xqjs.module.ppl.service.NiandygmxService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.dispacher.ActionContext;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PplDownXlscAction extends ActionSupport {
	// 注入downloadsevices
	@Inject
	private DownLoadServices downloadServices = null;

	@Inject
	private NiandygmxService niandygmxService;

	// 毛需求比较文件下载
	@SuppressWarnings("unchecked")
	public String downLoadFilePpl() {

		// 数据源
		Map<String, Object> dataSource = niandygmxService.select(getParams());
		List<Niandygmx> list = (List<Niandygmx>) dataSource.get("list");
		for (Niandygmx bean : list) {
			String zhuangt = bean.getZhuangt().equalsIgnoreCase("0") ? "未生效" : "已发送";
			bean.setZhuangt(zhuangt);
		}
		dataSource.put("list", list);
		// 拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		// 设置下载
		String fileName = "pplDown.ftl";
		downloadServices.downloadFile(fileName, dataSource, response, "PPL年度预告", ExportConstants.FILE_XLS, false);

		// 返回类型一定要是download类型的
		return "downLoad";

	}


}
