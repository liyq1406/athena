package com.athena.xqjs.module.utils.xls.kanbyhl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.toft.core3.ibatis.support.AbstractIBatisDao;



/**
 * xls文件导入 的解析工具(最大要货量导入专用)
 * @author chenlei
 * @vesion 1.0
 * @date 2013-2-18
 */
public class XlsHandlerUtilszuidyhl {
	/**
	 * /对请求对象中得到文件上传的内容进行解析
	 * @param request
	 * @return 返回错误信息 
	 * 			没有错误信息则返回 success
	 * 			有错误信息 则返回错误信息
	 */
	@SuppressWarnings("unchecked")
	public static String analyzeXls(Map<String,String> map,HttpServletRequest request,String username,AbstractIBatisDao baseDao){
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
				if(item.isFormField()){
					jsonConfig = item.getString("UTF-8");
				}
				else{
					in = item.getInputStream();
				}				
			}
			// 将修改人编辑为当前登录人  编辑时间写入数据库
			//通过解析xls工厂生成xls解析器 对xls文件进行解析
			String errorMessage = XlsHandlerzuidyhl.getInstance(in,jsonConfig,username,map,baseDao).doXlsHandler();
			if(errorMessage==null || "".equals(errorMessage)){
				error = "success";
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

