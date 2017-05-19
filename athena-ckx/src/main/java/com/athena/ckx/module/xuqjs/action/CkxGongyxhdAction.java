package com.athena.ckx.module.xuqjs.action;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.entity.xuqjs.CkxLingjxhd;

import com.athena.ckx.module.xuqjs.service.CkxGongyxhdService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.xls.XlsHandlerUtils;
import com.athena.ckx.util.xls.XlsHandlerUtilsgyxhd;
import com.athena.component.service.Message;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.excore.template.export.IProcessData;
import com.athena.excore.template.export.ProcessDataFactory;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxGongyxhdAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	/**
	 * 注入GongyxhdService
	 * @author denggq
	 * @date 2012-3-6
	 */
	@Inject 
	private CkxGongyxhdService gongyxhdsService;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	//log4j日志初始化
	private final Log log = LogFactory.getLog(CkxGongyxhdAction.class); 
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-4-17
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 * @author denggq
	 * @date 20111229
	 */
	public String execute(@Param CkxGongyxhd bean) {
		setResult("gongyxhd", this.getParam("guanjz1"));
		setResult("biaos", StringUtils.defaultIfEmpty(getParam("biaos"), "1"));
		return  "select";
	}

	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 20111229
	 * @return String
	 */
	public String query(@Param CkxGongyxhd bean) {
		try{
			setResult("result", gongyxhdsService.queryGongyxhd(bean,getParam("exportXls")));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工艺消耗点", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "工艺消耗点", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 单记录查询
	 * @param bean
	 * @author denggq
	 * @date 20111229
	 */
	public String get(@Param CkxGongyxhd bean) {
		try{
			setResult("result", gongyxhdsService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工艺消耗点", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "工艺消耗点", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 多记录查询
	 * @param bean
	 * @author denggq
	 * @date 20120224
	 */
	public String list(@Param CkxGongyxhd bean) {
		try{
			setResult("result", gongyxhdsService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工艺消耗点", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "工艺消耗点", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	
	/**
	 * 数据保存（多条数据操作）
	 * @param bean
	 * @author denggq
	 * @date 2012-4-17
	 */
	public String save(@Param("insert") ArrayList<CkxGongyxhd> insert,@Param("edit") ArrayList<CkxGongyxhd> edit,@Param("delete") ArrayList<CkxGongyxhd> delete) {
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(gongyxhdsService.save(insert, edit ,delete,getLoginUser()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工艺消耗点", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "工艺消耗点", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	
	
	/**
	 * 数据失效
	 * @author denggq
	 * @date 2012-4-17
	 */
	public String remove(@Param CkxGongyxhd bean) {
		
		bean.setEditor(getLoginUser().getUsername());
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(gongyxhdsService.doDelete(bean),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工艺消耗点", "数据失效");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "工艺消耗点", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	
	
	/**
	 * 按照查询条件导出
	 * @param bean
	 */
	@SuppressWarnings("unchecked")
	public String download(@Param CkxGongyxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<CkxGongyxhd> rows = gongyxhdsService.list(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("gongyxhd.ftl", dataSurce, response, "工艺消耗点", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工艺消耗点", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "工艺消耗点", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	/**
	 * 模板导出
	 * 
	 */
//	public String downloadMob() {
//
//		IProcessData prossData = ProcessDataFactory.getProcessFactoryInstatnces().getProcessDataInstatnces(
//				ExportConstants.FILE_XLS);
//		HttpServletResponse response = ActionContext.getActionContext().getResponse();
//		exportFileBefor(response, "GONGYXHDMUBAN", ExportConstants.FILE_XLS);
//		OutputStream output = null;
//		try {
//			output = response.getOutputStream();
//			Map<String, Object> dataSource = new HashMap<String, Object>();
//
//			dataSource.put("count", 0);
//			// 模版head
//			output.write(prossData.processByteCache("gongyxhdmuban_head.ftl", dataSource));
//			output.flush();
//			// 模版body
//			output.write(prossData.processByteCache("gongyxhdmuban_body.ftl", dataSource));
//			// 模版foot
//			output.write(prossData.processByteCache("gongyxhdmuban_foot.ftl", dataSource));
//		} catch (Exception ex) {
//			log.error(ex.getMessage());
//			throw new ServiceException(ex.getMessage());
//		} finally {
//			if (output != null) {
//				try {
//					output.close();
//				} catch (Exception e) {
//					log.error(e.getMessage());
//					throw new ServiceException(e.getMessage());
//				}
//			}
//			try {
//				response.flushBuffer();
//			} catch (IOException e) {
//				log.error(e.getMessage());
//				throw new ServiceException(e.getMessage());
//			}
//		}
//		return "downLoad";
//	}
	
	/**
	 * 模板导出
	 * 
	 */
	public String downloadMob() {

		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			//dataSurce.put("mob", "工艺消耗点模板");
			dataSurce.put("count", 0);
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			downloadServices.downloadFile("gongyxhdMob.ftl", dataSurce, response, "工艺消耗点模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工艺消消耗点", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "工艺消耗点", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "工艺消耗点", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
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
	
	public String errdownLoad() {
		Map<String, String> map = getParams();
		IProcessData prossData = ProcessDataFactory.getProcessFactoryInstatnces().getProcessDataInstatnces(
				ExportConstants.FILE_XLS);
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		exportFileBefor(response, "GONGYXHDMUBAN"+map.get("name"), ExportConstants.FILE_XLS);
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			//存储文件根目录
			String saveLj = System.getProperty("user.home")+File.separator+"tmp"; 
			String fullFilePath = saveLj +File.separator+ map.get("name")+".xls";
			InputStream fis  = new FileInputStream(new File(fullFilePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            
            output = new BufferedOutputStream(response.getOutputStream());
            output.write(buffer);
            output.flush();
            output.close();
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
		return "downLoad";
	}	
	
	
	
	/**
	 * 导入数据
	 * @author denggq
	 * @Date 2018-4-18
	 * @Param bean
	 * @return String
	 * */
	public String upload(@Param CkxGongyxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			//List<CkxLingjxhds> listInsertCkxLingjxhds = new ArrayList<CkxLingjxhds>();
			//得到分配区产线
			Map<String, String>  map=gongyxhdsService.getFenpqToChanxMap();
			//验证生产线
			Map<String,Object>  mapshengcx=gongyxhdsService.getChanxMap();
			// 将创建人,修改人 编辑 为当前登录人
			String mes = XlsHandlerUtilsgyxhd.analyzeXls(mapshengcx,map,request,getLoginUser().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工艺消耗点", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "工艺消耗点", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setRequestAttribute("uploudmessage",message.get("message"));
		return "upload";
	}
	
	
	
	/**
	 * <p>
	 * 导入文件
	 * </p>
	 * 
	 * @author wuyichao
	 * @return
	 * @throws IOException
	 
	@SuppressWarnings("unchecked")
	public String upload() throws IOException 
	{
		
		//其他导入数据
		Map<String , String> paramMap = new HashMap<String, String>();
		HttpServletRequest req = ActionContext.getActionContext().getRequest();
		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		String message = null;
		
		OutputStream output = null;
		reqResponse.setContentType("text/html");
		reqResponse.setCharacterEncoding("utf-8");
		output = reqResponse.getOutputStream() ;
		LoginUser loginUser = getLoginUser();
		String username = "";
		String time = DateTimeUtil.getAllCurrTime();
		paramMap.put("time", time);
		if(null != loginUser)
		{
			username = loginUser.getUsername();
			paramMap.put("username", username);
			//paramMap.put("usercenter", loginUser.getUsercenter());
		}
//		if(StringUtils.isNotBlank(flagBeiz))
//		{
//			int length = flagBeiz.length();
//			if(length > 50)
//			{
//				message = "<script>parent.callback('备注信息字符数量超过50,请调整!')</script>";
//				if (output != null) 
//				{
//					output.write(message.getBytes());
//					output.flush();
//					output.close();
//				}
//				return AJAX;
//			}
//		}
//		else
//		{
//			StringBuffer flagSb = new StringBuffer();
//			flagSb.append("在时间为:").append(time).append("由").append(username).append("导入调拨单信息") ;//,文件路径为:").append(fullFilePath);
//			paramMap.put("beiz", flagSb.toString());
//		}
		
	
		//存储文件名称
		String fullFilePath = "";
		//备注
		String result = null;
		
		
		//存储文件根目录
		String saveLj = System.getProperty("user.home")+File.separator+"tmp"; 
		
		RequestContext requestContext = new ServletRequestContext(req); 
		//是否有上传文件
		if (FileUpload.isMultipartContent(requestContext)) 
		{
			try 
			{
				FileItemFactory factory = new DiskFileItemFactory();
				//上传主件
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax(40000000);
				List<FileItem> fileItems;

				fileItems = upload.parseRequest(req);
				
				for (FileItem item : fileItems) 
				{
					//如果不是文本项，则进行文件传输
					if (!item.isFormField()) {
						File fullFile = new File(saveLj);
						if (!fullFile.exists()) 
						{
							fullFile.mkdirs();
						}
						// 文件名称
						fullFilePath = saveLj + File.separator+String.valueOf(new Date().getTime())+".xls";
						log.info("上传文件路径："+fullFilePath);
						File fileOnServer = new File(fullFilePath); 
						item.write(fileOnServer); 
						log.info("上传文件成功,路径："+fullFilePath);
					}
				}
				
			} 
			catch (Exception e) 
			{
				log.info(e.getMessage());
			}
		}
		
		ArrayList<CkxGongyxhd> datasretu=new ArrayList<CkxGongyxhd>();
		String filename = "";
		try {
			IProcessData prossData = ProcessDataFactory.getProcessFactoryInstatnces().getProcessDataInstatnces(
					ExportConstants.FILE_XLS);
			log.info(fullFilePath);

			 datasretu = gongyxhdsService.importData(fullFilePath,paramMap);
			//如果返回集合是空，说明提交成功。如果有值，说明有错误信息，需要返回。
			if(datasretu==null || datasretu.size()<=0){
				message = "<script>parent.callback('导入成功!')</script>";
			}else{
						Map<String, Object> dataSource = new HashMap<String, Object>();
						dataSource.put("count", datasretu.size());
						 filename = String.valueOf(new Date().getTime());
						fullFilePath = saveLj +File.separator+ filename+".xls";

						OutputStream out = new FileOutputStream(fullFilePath);
						
						// 模版head
						out.write(prossData.processByteCache("gongyxhdmuban_head.ftl", dataSource));
						
						// 模版body					
							dataSource.put("list", datasretu);// 数据查询
						out.write(prossData.processByteCache("gongyxhdsc_body.ftl", dataSource));
						
						// 模版foot
						out.write(prossData.processByteCache("gongyxhdmuban_foot.ftl", dataSource));
						
						out.flush();
						out.close();
						
						message = "<script>parent.callbackdow('导入数据有问题，请修改!','"+filename+"')</script>";
				}
			} catch (Exception ex) {
				log.error(ex.getMessage());
				throw new ServiceException(ex.getMessage());
			} 
			if (output != null) 
			{
				output.write(message.getBytes());
				output.flush();
				output.close();
			}
			return "downLoad";
	}
	//当数据从 新增 或无效 变成有效时 寻找对应的生产线编码
	
//	public String yanzscxbh(@Param CkxGongyxhd bean){		
//		try {
//			//Message m=new Message(gongyxhdsService.yzscxbh(bean),"i18n.ckx.xuqjs.i18n_xitcsdy");
//			//message.put("message", m.getMessage());
//			setResult("result",gongyxhdsService.yzscxbh(bean,getLoginUser()));
//			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "工艺消耗点", "验证生产线编码");
//		} catch (Exception e) {		
//			
//			setResult("messages",e.getMessage());
//			userOperLog.addError(CommonUtil.MODULE_CKX, "工艺消耗点", "验证生产线编码", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
//		}
//		return AJAX;
//	}*/
	
}
