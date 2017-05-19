package com.athena.xqjs.module.kanbyhl.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.excore.template.export.IProcessData;
import com.athena.excore.template.export.ProcessDataFactory;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.entity.kanbyhl.KanbxhgmWTC;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.kanbyhl.service.DaorzdyhlService;
import com.athena.xqjs.module.kanbyhl.service.KanbxhgmService;
import com.athena.xqjs.module.utils.xls.kanbyhl.XlsHandlerUtilstiaozxhgm;
import com.athena.xqjs.module.utils.xls.kanbyhl.XlsHandlerUtilszuidyhl;
import com.athena.xqjs.module.yaohl.service.YaohlService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * 看板循环规模Action
 * 
 * @author Xiahui
 * @date 2012-1-31
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KanbxhgmAction extends BaseWtcAction {

	@Inject
	private UserOperLog userOperLog;				//用户日志
	// 注入downloadsevices
	@Inject
	private DownLoadServices downloadServices = null;
    
	@Inject
	private AbstractIBatisDao baseDao;
	
	@Inject
	private KanbxhgmService kanbxhgmService;

	@Inject
	private YaohlService yaohlService;

	@Inject
	private DaorzdyhlService daorzdyhlService;

	private Log log = LogFactory.getLog(KanbxhgmAction.class);

	private static final String KB_UNSHIX = "2";

	/*
	 * @Inject private DaorzdyhlService daoryhl;
	 */

	/**
	 * getUserInfo获取用户信息方法
	 * 
	 * @return object userInfo
	 */
	public LoginUser getUserInfo() {
		return (LoginUser) AuthorityUtils.getSecurityUser();

	}

	/**
	 * 看板循环管理（外部）
	 */
	public String init() {
		setRequestAttribute("usercenter", getUserInfo().getUsercenter());
		
		setResult("zbczxc", getzbcZxc());
		return "kanbxhw";
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
			usercenter = getUserInfo().getUsercenter();
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
		setResult("result", kanbxhgmService.selectYjfzl(kbxh, getParams()));
		return AJAX;
	}
	
	/**
	 * 看板循环管理（内部）
	 */
	public String kanbxhn() {
		setResult("usercenter", getUserInfo().getUsercenter());
		return "kanbxhn";
	}

	public static void main(String[] args) {
		System.out.println("==" + "\ufeff" + "||");
	}

	/**
	 * 看板循环规模查询
	 * 
	 * @return 查询结果
	 */
	public String searchKanbxhgm(@Param Kanbxhgm kbxh) {
		String gmbh = getParam("gmbh");
		String bh = getParam("bh");
		if (("02".equalsIgnoreCase(gmbh) || "03".equalsIgnoreCase(gmbh)) && bh == null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("message", "规模变化百分比不能为空");
			setResult("result", map);
			return AJAX;
		}
		setResult("result", kanbxhgmService.select(kbxh, getParams()));
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
		Map<String, Object> countDate = kanbxhgmService.select(kbxh, paramcount);// 数据条数查询
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
		exportFileBefor(response, "看板循环规模", ExportConstants.FILE_XLS);
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
				dataSource.put("list", kanbxhgmService.select(kbxh, param).get("rows"));// 数据查询
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

	/**
	 * 生效看板循环规模
	 */
	public String shengXKanbxhgm(@Param("kb") ArrayList<Kanbxhgm> ls) {
		// 修改操作
		String result = MessageConst.UPDATE_COUNT_0;
		String time = CommonFun.getJavaTime();
		String info;
		try {
			for (int i = 0; i < ls.size(); i++) {
				Kanbxhgm kb = (Kanbxhgm) ls.get(i);
				if (kb.getDongjjdzt().equals(Const.KANBXH_YIDONGJ)) {
					setResult("flag", "不能对已冻结状态的做修改操作，请确认！");
					return AJAX;
				}
				kb.setShengxzt(Const.KANBXH_SHENGX);
				// 当前修改时间
				kb.setEdit_time(time);
				// 当前修改人
				kb.setEditor(getUserInfo().getUsername());
				if(!getzbcZxc()){
					kanbxhgmService.doUpdate(kb);
				}
			}
			info = kanbxhgmService.doUpdate(ls);
		} catch (Exception e) {
			setResult("flag", result);
			return AJAX;
		}
		if (info.equals("false")) {
			setResult("flag", result);
			return AJAX;
		}
		result = "修改成功！";
		setResult("result", result);
		return AJAX;
	}

	/**
	 * 失效看板循环规模
	 */
	public String unEffectKanbxhgm(@Param("kb") ArrayList<Kanbxhgm> ls) {
		// 修改操作
		String result = MessageConst.UPDATE_COUNT_0;
		String time = CommonFun.getJavaTime();
		String info;
		try {
			for (int i = 0; i < ls.size(); i++) {
				Kanbxhgm kb = (Kanbxhgm) ls.get(i);
				kb.setShengxzt(KB_UNSHIX);
				// 当前修改时间
				kb.setEdit_time(time);
				// 当前修改人
				kb.setEditor(getUserInfo().getUsername());
			}
			info = kanbxhgmService.doUpdate(ls);
		} catch (Exception e) {
			setResult("flag", result);
			return AJAX;
		}
		if (info.equals("false")) {
			setResult("flag", result);
			return AJAX;
		}
		result = "修改成功！";
		setResult("result", result);
		return AJAX;
	}

	/**
	 * 
	 * $.sdcui.editorFactory.closeEditor(); 冻结循环规模
	 */
	public String dongJKanbxhgm(@Param("kb") ArrayList<Kanbxhgm> ls) {
		// 修改操作
		String result = MessageConst.UPDATE_COUNT_0;
		String time = CommonFun.getJavaTime();
		String info;

		try {
			for (int i = 0; i < ls.size(); i++) {
				Kanbxhgm kb = (Kanbxhgm) ls.get(i);
				if (kb.getDongjjdzt().equals(Const.KANBXH_YIDONGJ)) {
					setResult("flag", "不能对已冻结状态的做冻结操作，请确认！");
					return AJAX;
				}
				// 当前修改时间
				kb.setEdit_time(time);
				// 当前修改人
				kb.setEditor(getUserInfo().getUsername());
				kb.setDongjjdzt(Const.KANBXH_YIDONGJ);
				kb.setShengxzt(Const.KANBXH_SHENGX);
				//当且仅当值为YN时，才执行  sql  decode(xiafxhgm,null,2,xiafxhgm)
				kb.setShifgxxfgm("YN");
			}
			info = kanbxhgmService.doUpdate(ls);
		} catch (Exception e) {
			setResult("flag", result);
			return AJAX;
		}
		if (info.equals("false")) {
			setResult("flag", result);
			return AJAX;
		}
		result = "修改成功！";
		setResult("result", result);
		return AJAX;
	}

	/**
	 * 解冻循环规模
	 * 
	 */
	public String jieDKanbxhgm(@Param("kb") ArrayList<KanbxhgmWTC> ls, @Param("kb") ArrayList<Kanbxhgm> gmls) {
		// 修改操作
		String result = MessageConst.UPDATE_COUNT_0;
		try {
			for (int i = 0; i < ls.size(); i++) {
				KanbxhgmWTC kb = ls.get(i);
				if (kb.getZhuangt().equals(Const.KANBXH_WEIDONGJ)) {
					setResult("flag", "不能对未冻结状态的做解冻操作，请确认！");
//					return AJAX;
				}
				kb.setZhuangt(Const.KANBXH_WEIDONGJ);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", ls);
			if(getzbcZxc()){
				kanbxhgmService.doUpdate(gmls, CommonFun.getJavaTime(), getUserInfo());
				result = "操作成功！";
			}else{
				WtcResponse wtcResponse = callWtc(getParam("usercenter"), "Q0355", map);
				// String response = wtcResponse.get("response").toString();
				setResult("response", wtcResponse.get("response"));
				if (wtcResponse.get("response").equals(Const.WTC_SUSSCESS)) {
					kanbxhgmService.doUpdate(gmls, CommonFun.getJavaTime(), getUserInfo());
					result = "操作成功！";
				}
			}
		} catch (Exception e) {
			setResult("flag", result);
			return AJAX;
		}
		setResult("result", result);
		return AJAX;
	}

	/***
	 * 查询零件是否存在
	 * 
	 */
	public String selectLingj(@Param Kanbxhgm kbxh) {
		setResult("result", yaohlService.selectLingj(getParams()));
		return AJAX;

	}

	/***
	 * 查询产线
	 */
	public String selectChanx(@Param Kanbxhgm kbxh) {
		setResult("result", kanbxhgmService.selectChanx(getParams()));
		return AJAX;

	}

	/**
	 * 手工设置最大要货量
	 */
	public String setZuidyhl(@Param Kanbxhgm kbxh) {
		setResult("xunhbm", getParam("xunhbm"));
		setResult("lingjbh", getParam("lingjbh"));
		return "zuidyhl";
	}

	/**
	 * 看板循环保存最大要货量 (方法废弃)
	 */
	public String zuidyhl(@Param Kanbxhgm kbxh) {
		kbxh.setXunhbm(getParam("xunhbm"));
		kbxh.setLingjbh(getParam("lingjbh"));
		kbxh.setZuidyhl(new BigDecimal(getParam("zuidyhl")));
		try {
			int count = kanbxhgmService.updateZuidyhl(kbxh);
			if (count != 0) {
				setResult("result", "最大要货量设置成功！");
			} else {
				setResult("result", "最大要货量设置失败，请检查数据！");
			}
		} catch (Exception e) {
			setResult("result", "最大要货量设置失败，请检查数据！");
			// System.out.println(e.toString());
			log.info(e.getCause().getMessage());
		}

		return AJAX;
	}

	/**
	 * 导入最大要货量
	 */
	public String daoryhl() {
		return "execl";
	}

	/**
	 * <p>
	 * 导入文件
	 * </p>
	 * 
	 * @author NIESY
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String impZdyhl() {
		HttpServletRequest req = ActionContext.getActionContext().getRequest();
		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		String fullFilePath = "";
		PrintWriter out = null;
		String saveLj = System.getProperty("java.io.tmpdir");
		RequestContext requestContext = new ServletRequestContext(req);
		if (FileUpload.isMultipartContent(requestContext)) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax(4000000);
				List<FileItem> fileItems;

				fileItems = upload.parseRequest(req);

				for (FileItem item : fileItems) {
					if (item.isFormField()) {
						String value = item.getString();
						log.info(item.getFieldName() + ":" + value);
					} else {
						String fileName = item.getName();
						if (fileName != null) {
							if (fileName.lastIndexOf(File.separator) != -1) {
								fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1,
										fileName.length());
							}
							fullFilePath = saveLj + fileName;
							File fullFile = new File(saveLj);
							if (!fullFile.exists()) {
								fullFile.mkdirs();
							}
							File fileOnServer = new File(fullFilePath);

							item.write(fileOnServer);

						}
					}
				}
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}
		try {
			log.info(fullFilePath);
			out = reqResponse.getWriter();
			reqResponse.setContentType("text/html");
			reqResponse.setCharacterEncoding("UTF-8");
			String impResult = daorzdyhlService.updateKanb(fullFilePath);
			String msg = "<script>parent.callback(\"" + impResult + "\")</script>";
			log.info(msg);
			out.print(msg);
			out.flush();
		} catch (Exception e) {
			log.error(e.getMessage());
			out.println("<script>parent.callback('导入文件出错！ ')</script>");
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}

		return AJAX;
	}
	/**
	 * 判断是准备层还是执行层，准备层返回true，执行层返回false
	 * @return
	 */
	public boolean getzbcZxc(){
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
	 * 最大要货量信息导出模板
	 * @param bean
	 * @author denggq
	 * @date 2012-8-31
	 * @return String
	 */
	public String downloadZuidyhlMob(@Param Kanbxhgm bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("zuidyhlMob.ftl", dataSurce, response, "最大要货量-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "最大要货量", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "最大要货量", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "最大要货量", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 最大要货量信息导入数据
	 * @author denggq
	 * @Date 2018-4-18
	 * @Param bean
	 * @return String
	 * */
	public String uploadzuidyhl(@Param Kanbxhgm bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();

			Map<String, String>  map=kanbxhgmService.getXiaohdckMap();
			//修改看板循环规模最大要货量及供应商交付总量设置为0
			String mes = XlsHandlerUtilszuidyhl.analyzeXls(map,request,getUserInfo().getUsername(),baseDao);
			
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "最大要货量", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "最大要货量", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		setResult("usercenter", getUserInfo().getUsercenter());
		//hanwu 20150711 0011502 由批量零件截止循环调用此方法时，应返回批量零件截止循环页面
		if(getParam("isLingjjzxh")!= null && getParam("isLingjjzxh").equals("1")){
			return "upload_lingjjzxh";
		}else{
			return "upload";
		}
	}
	
	/**
	 * 调整循环规模导入模板
	 * @param bean
	 * @author CSY
	 * @date 2016-10-24
	 * @return String
	 */
	public String downloadKanbxhgmMob(@Param Kanbxhgm bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("tiaozxhgm.ftl", dataSurce, response, "调整循环规模-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "调整循环规模", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "调整循环规模", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "调整循环规模", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 调整循环规模导入
	 * @author CSY
	 * @Date 2016-10-24
	 * @Param bean
	 * @return String
	 * */
	public String uploadxhgmdr(@Param Kanbxhgm bean,@Param("nwb")String nwb){
		Map<String,String> message = new HashMap<String,String>();
		boolean zbcZxc = getzbcZxc();
		
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();

			String mes = XlsHandlerUtilstiaozxhgm.analyzeXls(request,getUserInfo().getUsername(),baseDao,nwb,zbcZxc);
			
			message.put("message",mes);
			
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "调整循环规模", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "调整循环规模", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		setResult("usercenter", getUserInfo().getUsercenter());
		setResult("checkZBCZXC", zbcZxc);
		setResult("zbczxc", zbcZxc);
		
		if (nwb.equals("n")) {
			return "upload";
		}else {
			return "uploadw";
		}
	}
	
}
