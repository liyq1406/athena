/**
 * 
 */
package com.athena.util.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.toft.mvc.ActionSupport;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>Title:SDC工具类</p>
 *
 * <p>Description:文件上传辅助工具类</p>
 *
 * <p>Copyright:Copyright (c) 2012.1</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class UploadFileUtils {

	/**
	 * 从request中获取上传的文件集合
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,DiskFileItem> getDiskFiles(HttpServletRequest request){
		//使用common-fileupload 组件实现文件上传功能
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<DiskFileItem> items = new ArrayList<DiskFileItem>();
		Map<String,DiskFileItem> files = new HashMap<String,DiskFileItem>();
		
		try {
			//解析request，获取上传的文件列表
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			throw new RuntimeException("文件上传异常："+e.getMessage());
		}
		//判断form提交是否含有上传文件
		if(ServletFileUpload.isMultipartContent(request)){
			for(DiskFileItem diskFileItem:items){
				if(!diskFileItem.isFormField()&&null!=diskFileItem.getName()){//
					files.put(diskFileItem.getFieldName(), diskFileItem);
				}
			}
		}
		return files;
	}
	
	/**
	 * 获取指定参数名称的上传文件输出流
	 * @param request
	 * @param parameter
	 * @return
	 * @throws IOException 
	 */
	public static OutputStream getFileOutput(HttpServletRequest request,String parameter) throws IOException{
		Map<String,DiskFileItem> items
			= UploadFileUtils.getDiskFiles(request);
		
		DiskFileItem diskFileItem = items.get(parameter);
		return  diskFileItem!=null?diskFileItem.getOutputStream():null;
	}
	
	/**
	 * 获取指定参数名称的上传文件输入流
	 * @param request
	 * @param parameter
	 * @return
	 */
	public static InputStream getFileInput(HttpServletRequest request,String parameter) throws IOException{
		Map<String,DiskFileItem> items
			= UploadFileUtils.getDiskFiles(request);
		
		DiskFileItem diskFileItem = items.get(parameter);
		return  diskFileItem!=null?diskFileItem.getInputStream():null;
	}
	
	/**
	 * SDC action 中获取文件输出流
	 * 限定在action中调用
	 * @param as
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	public static OutputStream getFileOutput(ActionSupport as,String parameter) throws IOException{
		/*
		 * 使用SDC的ActionContext获取request对象，
		 * action 不能为空对象
		 */
		if(as==null){
			return null;
		}
		return UploadFileUtils.getFileOutput(ActionContext.getActionContext().getRequest(), parameter);
	}
	
	/**
	 * SDC action 中获取文件输入流
	 * @param as
	 * @param parameter
	 * @return
	 * @throws IOException
	 */
	public static InputStream getFileInput(ActionSupport as,String parameter) throws IOException{
		/*
		 * 使用SDC的ActionContext获取request对象，
		 * action 不能为空对象
		 */
		if(as==null){
			return null;
		}
		return UploadFileUtils.getFileInput(ActionContext.getActionContext().getRequest(), parameter);
	}
}
