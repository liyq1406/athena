package com.athena.util.tag;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.athena.util.tag.xls.XlsHandler;


/**
 * xls文件导入 的解析工具
 * @author chenlei
 * @vesion 1.0
 * @date 2012-5-17
 */
public class XlsHandlerUtils {
	/**
	 * /对请求对象中得到文件上传的内容进行解析
	 * @param request
	 * @return 返回错误信息 
	 * 			没有错误信息则返回 正整数1
	 * 			有错误信息 则返回错误信息
	 */
	@SuppressWarnings("unchecked")
	public static String analyzeXls(HttpServletRequest request){
		String error = null;
		
		//得到一个FileIterm工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//得到解析器对象
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		try {
			List<FileItem> list = upload.parseRequest(request);
			String jsonConfig = null; //解析xls文件的配置
			InputStream in = null;
			//判断上传的是不是文件上传的方式 只处理单文件上传
			for(FileItem item : list)
			{
				//判断是不是普通字段
				if(item.isFormField())
				{
					jsonConfig = item.getString("UTF-8");
				}
				else
				{
					in = item.getInputStream();
				}
				
			}
			
			//通过解析xls工厂生成xls解析器 对xls文件进行解析
			String errorMessage = XlsHandler.getInstance(in,jsonConfig).doXlsHandler();
			if(errorMessage==null || "".equals(errorMessage)){
				error ="1";
			}else{
				error = errorMessage;
			}
			
		}catch (FileUploadException e) {
			throw new RuntimeException(e);
		}catch(IOException io){
			throw new RuntimeException(io);
		}
		if(error !=null){
			  error = error.replace("\n", "@").replace("\"", "'");
		}
//		if(error!=null && error.endsWith("\n")){
//			error = error.substring(0, error.length()-1);
//		}
		return error;
	}
}
