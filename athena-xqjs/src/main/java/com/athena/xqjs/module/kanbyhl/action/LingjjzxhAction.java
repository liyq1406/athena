package com.athena.xqjs.module.kanbyhl.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.excore.template.export.IProcessData;
import com.athena.excore.template.export.ProcessDataFactory;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.module.kanbyhl.service.LingjjzxhService;
import com.athena.xqjs.module.utils.xls.kanbyhl.XlsHandlerUtilslingjjzxh;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * 零件截止循环
 * @author hanwu
 * @date 2015-6-16
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LingjjzxhAction extends ActionSupport{

	@Inject
	private UserOperLog userOperLog;				//用户日志
	
	@Inject
	private DownLoadServices downloadServices = null;
	
	@Inject
	private LingjjzxhService lingjjzxhService;
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	private Log log = LogFactory.getLog(LingjjzxhAction.class);
	
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
		setResult("zbczxc", getzbcZxc());
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	/**
	 * 查看截止零件数据
	 * @return
	 */
	public String queryLingjjzxh(@Param Kanbxhgm kbxh){
		String gmbh = getParam("gmbh");
		String bh = getParam("bh");
		if (("02".equalsIgnoreCase(gmbh) || "03".equalsIgnoreCase(gmbh)) && bh == null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("message", "规模变化百分比不能为空");
			setResult("result", map);
			return AJAX;
		}
		setResult("result", lingjjzxhService.select(kbxh, getParams()));
		return AJAX;
	}
	
	/**
	 * 判断是准备层还是执行层，准备层返回true，执行层返回false
	 * @return
	 */
	private boolean getzbcZxc(){
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		HttpSession session = request.getSession();
		String zbczxc = (String) session.getAttribute("zbcZxc");
		boolean flag = false;
		if("ZBC".equals(zbczxc)){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 批量零件截止循环导出模板
	 * @param bean
	 * @author hanwu
	 * @date 2015-07-09
	 * @return String
	 */
	public String downloadLingjjzxhMob(@Param Kanbxhgm bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingjjzxhMob.ftl", dataSurce, response, "批量零件截止循环-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "批量零件截止循环", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_XQJS, "批量零件截止循环", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_XQJS, "批量零件截止循环", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 批量零件截止循环导入数据
	 * @author hanwu
	 * @Date 2015-07-09
	 * @Param bean
	 * @return String
	 * */
	public String uploadlingjjzxh(){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			//获取所有的看板循环规模，并加以封装
			Map<String,List<Kanbxhgm>> map=lingjjzxhService.getKanbxhMap();
			//验证导入数据，并插入到零件截止循环表中
			String mes = XlsHandlerUtilslingjjzxh.analyzeXls(map,request,getLoginUser().getUsername(),baseDao);
			
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "批量零件截止循环", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "批量零件截止循环", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		setResult("usercenter", getLoginUser().getUsercenter());
		setResult("zbczxc", getzbcZxc());
		return "upload";
	}
	
	/**
	 * 跳转到查看已交付页面
	 * @return
	 */
	public String initYjf(){
		String xunhbm = getParam("xunhbm");
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		String s = request.getParameter("xunhbm");
		
		String usercenter = "";
		if(xunhbm != null){
			usercenter = getParam("usercenter");
		}else{
			usercenter = getLoginUser().getUsercenter();
		}
		setResult("xunhbm", xunhbm);
		setResult("usercenter" , usercenter);
		return "initYjf";
	}
	/**
	 * 查看已交付页面数据
	 * @return
	 */
	public String queryYjf(@Param Kanbxhgm kbxh){
		setResult("result", lingjjzxhService.selectYjfzl(kbxh, getParams()));
		return AJAX;
	}
	
	/**
	 * 看板循环规模查询
	 * 
	 * @return 查询结果
	 */
	public String expKanbxhgm(@Param Kanbxhgm kbxh, @Param("xunhbms") ArrayList<String> xunhbms) {

		// 选择的循环规模
		String choosexunhbm = "";
		if (xunhbms != null && xunhbms.size() > 0) {
			for (String value : xunhbms) {
				if (StringUtils.isEmpty(choosexunhbm)) {
					choosexunhbm = "'" + value + "'";
				} else {
					choosexunhbm = choosexunhbm + ",'" + value + "'";
				}
			}
			choosexunhbm = " xunhbm in (" + choosexunhbm + ")";
		}

		// 合计条数
		kbxh.setPageNo(1);
		kbxh.setPageSize(1);
		Map<String, String> paramcount = new HashMap<String, String>();
		paramcount.putAll(getParams());
		paramcount.put("choosexunhbm", choosexunhbm);
		Map<String, Object> countDate = lingjjzxhService.select(kbxh, paramcount);// 数据条数查询
		Object o = countDate.get("total") == null ? "" : countDate.get("total");
		Integer count = NumberUtils.toInt(o.toString(), 0);

		// 分页导出，减少数据内存消耗
		int PAGE_SIZE = 10000;
		kbxh.setTotal(count);
		kbxh.setPageSize(PAGE_SIZE);
		int pageNos = count / PAGE_SIZE + (count % PAGE_SIZE > 0 ? 1 : 0);
		IProcessData prossData = ProcessDataFactory.getProcessFactoryInstatnces().getProcessDataInstatnces(
				ExportConstants.FILE_XLS);
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		exportFileBefor(response, "结束批量零件循环截止", ExportConstants.FILE_XLS);
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			Map<String, Object> dataSource = new HashMap<String, Object>();
			dataSource.put("count", count);
			// 模版head
			output.write(prossData.processByteCache("kbxhgm_head.ftl", dataSource));

			// 模版body
			Map<String, String> param = new HashMap<String, String>();
			param.putAll(getParams());
			param.put("choosexunhbm", choosexunhbm);
			for (int i = 0; i < pageNos; i++) {
				kbxh.setPageNo(i + 1);
				dataSource.put("list", lingjjzxhService.select(kbxh, param).get("rows"));// 数据查询
				output.write(prossData.processByteCache("kbxhgm_body.ftl", dataSource));
			}

			// 模版foot
			output.write(prossData.processByteCache("kbxhgm_foot.ftl", dataSource));
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

		// 返回类型一定要是download类型的
		return "downLoad";
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
