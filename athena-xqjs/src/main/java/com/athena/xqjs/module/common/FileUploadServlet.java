
package com.athena.xqjs.module.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.xqjs.module.kanbyhl.service.DaorzdyhlService;
import com.athena.xqjs.module.kdorder.service.KdbdmlService;
import com.athena.xqjs.module.maoxq.service.MaoxqCompareService;
import com.toft.core3.web.context.support.WebSdcContextUtils;

/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class FileUploadServlet extends HttpServlet{


	private static final long serialVersionUID = 1L;
	
	private org.apache.commons.logging.Log  log = LogFactory.getLog(getClass());
	// 当前登录人信息
	private LoginUser loginUser = AuthorityUtils.getSecurityUser();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");       
		req.setCharacterEncoding("UTF-8");      
		resp.setContentType("application/force-download");
		resp.setContentType("application/x-download");
		String fname=req.getParameter("fileName");
	   
		resp.setContentLength(4000000);
		if (fname != null) {       
		    OutputStream os = null;       
		    FileInputStream fis = null;       
		    try {       
		         String file = fname;       
		         if (!(new File(file)).exists()) {       
		              return;       
		          }          
		         os = resp.getOutputStream();       
		         resp.setHeader("content-disposition", "attachment;filename=" + new String(fname.getBytes("GBK"), "ISO-8859-1"));         
		         resp.setContentType("application/octet-stream");//八进制流 与文件类型无关     
		         byte temp[] = new byte[1024];       
		         fis = new FileInputStream(file);       
		         int n = 0;       
		         while ((n = fis.read(temp)) != -1) {       
		            os.write(temp, 0, n);       
		          }       
		       } catch (Exception e) {       
		             
		       } finally {       
		           if (os != null){
		        	   
		        	   os.close();       
		           }       
		           if (fis != null){
		        	   
		        	   fis.close();       
		           }       
		        }   
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//保存临时文件
		//String dispatcher = req.getParameter("dispatcher");
		//DaorzdyhlService zdyhlService = WebSdcContextUtils.getWebSdcContext(getServletContext()).getComponent(DaorzdyhlService.class);
		req.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		resp.setCharacterEncoding("UTF-8");
		//String fileName = "";
		String  fullFilePath = "";
		String xuqly = "";
		String  beiz = "";
		String  mkm  = "";
		RequestContext requestContext = new ServletRequestContext(req);
		String saveLj = System.getProperty("java.io.tmpdir");
		if (FileUpload.isMultipartContent(requestContext)) {
			try {
//				DiskFileItemFactory factory = new DiskFileItemFactory();
//
//				factory.setRepository(new File(saveLj));
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				upload.setSizeMax(4000000);
				List<FileItem> fileItems;

				fileItems = upload.parseRequest(req);
				
				for(FileItem item:fileItems){
					if (item.isFormField()) {
						String value = item.getString();
						if("xuqly".equals(item.getFieldName())){
							xuqly = item.getString();
							log.info(xuqly);
						}else if ("beiz".equals(item.getFieldName())){
							beiz = item.getString();
						}else if("mkm".equals(item.getFieldName())){
							mkm = item.getString();
						}
						//System.out.println(item.getFieldName()+":"+value);
						log.info(item.getFieldName()+":"+value);
					} else {
						String fileName = item.getName();
						if (fileName != null) {
							// UploadUtil
							// File fileOnServer = new File(UploadUtil)
							//File fullFile = new File(item.getName());
							if( fileName.lastIndexOf('\\') != -1){
								fileName = fileName.substring(
			                            fileName.lastIndexOf('\\') + 1, fileName.length());
							}
							 fullFilePath = saveLj+fileName;
							File fullFile = new File(saveLj);
							if(!fullFile.exists()){
								fullFile.mkdirs();
							}
							File fileOnServer = new File(fullFilePath);
				
						    item.write(fileOnServer);
								
//							ByteOutputStream byteOut = new ByteOutputStream();
//							byteOut.write(item.getInputStream());
//							FileWriter fileWriter = new FileWriter("D:/temp/"+item.getName());
//							fileWriter.write(item.getString());
							//input.close();
						
						}
					}
				}
			} catch (FileUploadException e) {
				log.error(e.getMessage());
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		try {
			resp.setContentType("text/html");

			if(mkm.equals(Const.FILE_UP_MAOXQ)){
				MaoxqCompareService  mc = WebSdcContextUtils.getWebSdcContext(getServletContext()).getComponent(MaoxqCompareService.class);
				req.setAttribute("impResult",
						mc.insExcelData(fullFilePath, beiz,
								loginUser.getUsername()));
			}else if(mkm.equals(Const.FILE_UP_DRZDYHL)){
				DaorzdyhlService zdyhlService = WebSdcContextUtils.getWebSdcContext(getServletContext()).getComponent(DaorzdyhlService.class);
			    zdyhlService.readMulu(fullFilePath);
			}else if(mkm.equals(Const.FILE_UP_KDBD)){
				KdbdmlService  kdml =  WebSdcContextUtils.getWebSdcContext(getServletContext()).getComponent(KdbdmlService.class);
				kdml.readMulu(fullFilePath);
			}
		
			//zdyhlService.updateKanb(fullFilePath);
		} catch (BiffException e) {
			log.error(e.getMessage());
		} catch (ParseException e) {
			log.error(e.getMessage());
		} finally {
			if (out != null) {
				out.close();
			}
			out.close();
		}
		resp.getOutputStream().println("{}");
	}
//		req.setAttribute("formHtmls", "表单内容");
//		req.setAttribute("action", dispatcher);//form提交地址
//		req.getRequestDispatcher("upload.jsp").forward(req, resp);
		
		
   }


