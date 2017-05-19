package com.athena.xqjs.module.kdorder.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.read.biff.BiffException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.logging.LogFactory;

import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.kdorder.KdbdmulResult;
import com.athena.xqjs.module.kdorder.service.KdbdmlService;
import com.athena.xqjs.module.kdorder.service.KdbdmulResultService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：KdbdmlAction
 * <p>
 * 类描述： kd订单
 * </p>
 * 创建人：陈骏
 * <p>
 * 创建时间：2012-1-12
 * </p>
 * 
 * @version
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class MulubiduiAction extends ActionSupport {
	@Inject
	public KdbdmlService kdbdmlservice;
	@Inject
	private KdbdmulResultService kdbdmulresultservice;
	@Inject
	private DownLoadServices  downloadServices;
	
	private org.apache.commons.logging.Log log = LogFactory.getLog(getClass());
	
	@Inject
	private UserOperLog userOperLog;

	// 当前登录人信息
	// private LoginUser loginUser = AuthorityUtils.getSecurityUser();
	/**
	 * <p>
	 * 项目名称：athena-xqjs
	 * </p>
	 * 类名称：KdbdmlAction
	 * <p>
	 * 类描述： kd订单目录对比页面初始化
	 * </p>
	 * 创建人：陈骏
	 * <p>
	 * 创建时间：2012-1-12
	 * </p>
	 * 
	 * @version
	 * 
	 */
	public String execute() {
		// String forward = "success";
		return "success";
	}

	/**
	 * <p>
	 * 项目名称：athena-xqjs
	 * </p>
	 * 类名称：KdbdmlAction
	 * <p>
	 * 类描述： kd订单目录对比导入用户表格
	 * </p>
	 * 创建人：陈骏
	 * <p>
	 * 创建时间：2012-1-12
	 * </p>
	 * 
	 * @version
	 * @throws Exception
	 * 
	 */
	public String insertMulu() throws Exception {
		HttpServletRequest req = ActionContext.getActionContext().getRequest();
		HttpServletResponse reqResponse = ActionContext.getActionContext()
				.getResponse();

		PrintWriter out = null;
		out = reqResponse.getWriter();

		String fullFilePath = "";
		String xuqly = "";
		String beiz = "";
		RequestContext requestContext = new ServletRequestContext(req);
		String saveLj = System.getProperty("java.io.tmpdir");
		if (FileUpload.isMultipartContent(requestContext)) {

			// DiskFileItemFactory factory = new DiskFileItemFactory();
			//
			// factory.setRepository(new File(saveLj));
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			upload.setSizeMax(4000000);
			List<FileItem> fileItems;

			try {
				fileItems = upload.parseRequest(req);

				for (FileItem item : fileItems) {
					if (item.isFormField()) {
						String value = item.getString();
						if ("xuqly".equals(item.getFieldName())) {
							xuqly = item.getString();
						} else if ("beiz".equals(item.getFieldName())) {
							beiz = item.getString();
						}
						// System.out.println(item.getFieldName()+":"+value);
						log.info(item.getFieldName() + ":" + value);
					} else {
						String fileName = item.getName();
						if (fileName != null) {
							if (fileName.lastIndexOf('\\') != -1) {
								fileName = fileName.substring(
										fileName.lastIndexOf('\\') + 1,
										fileName.length());
							}
							fullFilePath = saveLj + File.separator + fileName;
							File fullFile = new File(saveLj);
							if (!fullFile.exists()) {
								fullFile.mkdirs();
							}
							File fileOnServer = new File(fullFilePath);

							try {
								item.write(fileOnServer);

							} catch (Exception e) {
								// TODO Auto-generated catch block
								log.info(e.getMessage());
								out.println("<script>parent.callback('导入文件出错！ ')</script>");
							}

						}
					}
				}
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				log.info(e.getMessage());
				out.println("<script>parent.callback('导入文件出错！ ')</script>");
			}

		}

		reqResponse.setContentType("text/html");
		reqResponse.setCharacterEncoding("UTF-8");
 
		try {
			this.kdbdmlservice.deleteKdbdml();
			this.kdbdmlservice.readMulu(fullFilePath);
			String impResult = "导入成功！";
			String str = "<script>parent.callback(\"" + impResult
					+ "\")</script>";
			log.info(str);
			out.print(str);
			out.flush();
		} catch (BiffException e) {
			log.info(e.getMessage());
			out.print("<script>parent.callback('导入文件出错！ ')</script>");
		} catch (IOException e) {
			log.info(e.getMessage());
			out.print("<script>parent.callback('导入文件出错！ ')</script>");
		} catch (ParseException e) {
			log.info(e.getMessage());
			out.print("<script>parent.callback('导入文件出错！ ')</script>");
		} catch (Exception e) {
			log.error(e.getMessage());
			
			out.print("<script>parent.callback(\"" + e.getMessage()
					+ "\")</script>");
			
			return e.getMessage();
		} 

		return AJAX;
	}

	/**
	 * <p>
	 * 项目名称：athena-xqjs
	 * </p>
	 * 类名称：KdbdmlAction
	 * <p>
	 * 类描述： kd订单目录对比用户导入信息和参考系信息比对并返回结果
	 * </p>
	 * 创建人：陈骏
	 * <p>
	 * 创建时间：2012-1-12
	 * </p>
	 * 
	 * @version
	 * 
	 */
	public String MuluCompared() {
		String res = this.kdbdmlservice.MuluCompared();
		 
//		Map<String, Object> map = new HashMap<String, Object>();
		// map = this.kdbdmulresultservice.bidResult(new KdbdmulResult());
		setResult("result", res);

		return AJAX;
	}

	/**
	 * <p>
	 * 项目名称：athena-xqjs
	 * </p>
	 * 类名称：KdbdmlAction
	 * <p>
	 * 类描述： kd订单目录对比查询结果
	 * </p>
	 * 创建人：陈骏
	 * <p>
	 * 创建时间：2012-1-12
	 * </p>
	 * 
	 * @version
	 * 
	 */
	public String queryMuluRes(@Param KdbdmulResult bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = this.kdbdmulresultservice.bidResult(bean, getParams());
		setResult("result", map);
		return AJAX;
	}
	public String muluDownLoadFile() throws IOException{
		Map<String ,Object> map = new HashMap<String,Object>();
		ArrayList<KdbdmulResult> list = (ArrayList)this.kdbdmulresultservice.selectResult();
		map.put("res", list);
		Map<String, Object> dataSource = map;
		//拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
		downloadServices.downloadFile("mulubd.ftl", dataSource, response, "目录比对结果", ExportConstants.FILE_XLS, false) ;
		return "download";
	}
	

	/**
	 * 导入模板下载
	 * v4_014
	 *
	public String downloadKdorderMob(@Param Kdbdml bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("mulubdMob.ftl", dataSurce, response, "KD目录比对-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "KD目录比对", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "KD目录比对", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "KD目录比对", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	*/
	
	public String downloadKdorderMob() {

		Map<String,String> message = new HashMap<String,String>();
		try{
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			String fileName = "CATALOGUE DU MOIS A VENIR.xls";
			this.downLoadFile(response, fileName, "xls");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "KD目录比对", "模板下载");
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "KD目录比对", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "KD目录比对", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return "downLoad";
	}
	
	
	//导出模板
	public void downLoadFile(HttpServletResponse response,
			String fileName, String fileType) throws Exception {
		
		//String filePath = this.getClass().getClassLoader().getResource("").getPath().substring(1);

		/*filePath += "template/excel/" + fileName;
		logger.info("读取文件的路径为：" + filePath);*/
		
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/excel/" + fileName);
		
		//File file = new File(filePath); // 根据文件路径获得File文件
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		// 文件名
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ new String(fileName.getBytes(), "ISO-8859-1") + "\"");
		//response.setContentLength((int) file.length());
		byte[] buffer = new byte[4096];// 缓冲区
		BufferedOutputStream output = null;
		BufferedInputStream input = null;
		try {
			output = new BufferedOutputStream(response.getOutputStream());
			//input = new BufferedInputStream(new FileInputStream(file));
			input = new BufferedInputStream(in);
			//input = new BufferedInputStream(in);
			int n = -1;
			// 遍历，开始下载
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
			output.flush();
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if (input != null)
				input.close();
			if (output != null)
				output.close();
			if (in != null) {
				in.close();
			}
		}
	}
	
	
	
}
