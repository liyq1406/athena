package com.athena.xqjs.module.ycbj.action;


import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.athena.authority.util.AuthorityUtils;
import com.athena.excore.template.export.ExportConstants;
import com.athena.excore.template.export.IProcessData;
import com.athena.excore.template.export.ProcessDataFactory;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.yaohl.action.YaohlAction;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YicbjAction extends ActionSupport {
	@Inject
	private YicbjService yicbjservice;//异常报警服务类
	
	@Inject
	private UserOperLog userOperLog;
	
	private final Logger log = Logger.getLogger(YaohlAction.class);
	
	/**
	 * getUserInfo获取用户信息方法
	 * @return object userInfo
	 */
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}  
	
	/**
	 * 异常报警查询页面
	 * @return 异常报警查询
	 */
	public String init(){
		setResult("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());
		setResult("jismk", getParam("jismk"));
		String time = CommonFun.getJavaTime("yyyy-MM-dd HH:mm:ss");
		try {
			setResult("create_time", CommonFun.getDayTime(time, -1, "yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			setErrorMessage(e.toString());
		}
		setResult("create_timeTo", time);
		return "ycbj";
	}
	
	/**
	 * 异常报警导出
	 * @param yicbj
	 * @return
	 */
	public String yicbjDownLoad(@Param Yicbj yicbj){
		Map param = getParams();
		Integer count = yicbjservice.countYicbj(param);//
		int pageNos = count / 10000 + (count % 10000 > 0 ? 1 : 0);
		yicbj.setTotal(count);
		yicbj.setPageSize(10000);
		IProcessData prossData = ProcessDataFactory.getProcessFactoryInstatnces().getProcessDataInstatnces(ExportConstants.FILE_XLS);
		HttpServletResponse response = ActionContext.getActionContext().getResponse();	
		exportFileBefor(response, "异常报警", ExportConstants.FILE_XLS);
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			Map<String, Object> dataSource = new HashMap<String, Object>();
			// 模版head
			dataSource.put("count", count + 1);
			output.write(prossData.processByteCache("yicbj_head.ftl", dataSource));
			// 模版body
			for (int i = 0; i < pageNos; i++) {
				yicbj.setPageNo(i + 1);
				dataSource.put("dataSource", (List<Yicbj>)yicbjservice.select(yicbj,param).get("rows"));
				output.write(prossData.processByteCache("yicbj_body.ftl", dataSource));
			}
			// 模版foot
			output.write(prossData.processByteCache("yicbj_foot.ftl", dataSource));
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
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "异常报警导出", "异常报警导出结束");
		return "download";
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
	
	/**
	 * 异常报警查询
	 * @return 查询结果
	 */
	public String searchYicbj(@Param Yicbj bj){
		try {
			setResult("result",yicbjservice.select(bj, getParams()));
		} catch (Exception e) {
			setErrorMessage(e.toString());
		}
		return AJAX;
	}
	
}
