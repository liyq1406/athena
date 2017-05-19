package com.athena.xqjs.module.ilorder.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.excore.template.export.ExportConstants;
import com.athena.excore.template.export.IProcessData;
import com.athena.excore.template.export.ProcessDataFactory;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.module.ilorder.service.IleditAndEffectService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

@Component
public class IlOrderDownLoadAction extends ActionSupport {

	@Inject
	private IleditAndEffectService iService;

	private static final int PAGE_SIZE = 10000;

	/**
	 * log4j日志打印
	 */
	private final Log log = LogFactory.getLog(IlOrderDownLoadAction.class);

	public String execute() {
		return "success";
	}

	public String queryExpDingdh(@Param Dingd dd){
		 setResult("result",iService.queryExportDd(dd, getParams()));
		 return AJAX;
	}
	
	// 毛需求比较文件下载
	public String downLoadFileIlOrder() throws Exception {

		// 数据源
		Map<String, Object> dataSource = iService.exportOrder(getParams(), new Dingd());
		// 拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		if (dataSource == null) {
			PrintWriter out = response.getWriter();
			out.println("<script>parent.callback('订单下订单零件数据为空，没有可以导出的数据源')</script>");
			out.flush();
			out.close();
			return AJAX;
		}
		moreDataListExport(getParams(), dataSource, response);

		// 返回类型一定要是download类型的
		return "downLoad";

	}

	private void moreDataListExport(Map<String, String> param, Map<String, Object> dataSource, HttpServletResponse response) {
		Dingd bean = new Dingd();
		Integer count = (Integer) dataSource.get("total");
		bean.setTotal(count);
		bean.setPageSize(PAGE_SIZE);
		int pageNos = count/PAGE_SIZE + (count%PAGE_SIZE > 0 ? 1 : 0);
		IProcessData prossData = ProcessDataFactory.getProcessFactoryInstatnces().getProcessDataInstatnces(ExportConstants.FILE_XLS);
		exportFileBefor(response, "国产件订单", ExportConstants.FILE_XLS);
		OutputStream output = null;
		try {
			output = response.getOutputStream(); 
			//模版head
			dataSource.put("count", count+1);
			output.write(prossData.processByteCache("allOrder_head.ftl", dataSource));
			//模版body
			for(int i=0;i<pageNos;i++){
				bean.setPageNo(i + 1);
				dataSource.put("dataSource", iService.exportOrder(getParams(), bean));
				output.write(prossData.processByteCache("allOrder_body.ftl", dataSource));
			} 
			//模版foot
			output.write(prossData.processByteCache("allOrder_foot.ftl", dataSource));
			output.flush();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new ServiceException(ex.getMessage());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					log.error(e.getMessage());
					throw new ServiceException(e.getMessage());
				}
			}
			try {
				response.flushBuffer();
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new ServiceException(e.getMessage());
			}
		}

		
	}

	private void exportFileBefor(HttpServletResponse response, String filePrefix, String fileSuffix) {
		// 文件 名
		String downLoad = "";
		if (filePrefix != null) {
			try {
				downLoad = URLEncoder.encode(filePrefix, ExportConstants.CODE_UTF8);
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e.getMessage());
			}
		}
		// 设置文件名
		StringBuffer buf = new StringBuffer();
		buf.append(ExportConstants.ATTACHMENT);
		buf.append("\"");
		buf.append(downLoad);
		buf.append("_");
		buf.append(DateUtil.getCurrentDate());
		buf.append(fileSuffix);
		buf.append("\"");

		response.setContentType(ExportConstants.CONTEXT_TYPE);
		response.setCharacterEncoding(ExportConstants.CODE_UTF8);
		response.setHeader(ExportConstants.CONTEXT_DISPOSITION, buf.toString());

	}

}
