/****************************************************************************
 * 
 * 系统名     :神龙车辆入厂系统
 * 模块名  :            
 *
 * $Author: herohoy $
 * $Date: 2014-6-26 下午5:56:40 $
 * $RCSfile: ParamUtils.java,v $
 * $Revision: 1.0 $
 * 
 *  功能描述写在这里。
 *
 * Ver.    Revision   设计/BUGNo    变更日期        变更者              描述
 * ------- ---------- ------------- -------------  -----------------  -----------------------------
 * 1.0.0   1.1        -             2014-6-26                           新创建
 ***************************************************************************/

package com.athena.truck.module.churcsb.util;

import java.net.URLDecoder;
//import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.commons.lang.StringUtils;

import com.athena.util.exception.ServiceException;

public class ParamUtils {
	// 单次上传总文件最大50M
	public static final long MAX_SIZE = 50 * 1024 * 1024;
	// 单次上传单个文件最大10M
	public static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
	
	@SuppressWarnings("unused")
	private static String errMessage = null;
	
	@SuppressWarnings("unchecked")
	public static void uploadNoFile(HttpServletRequest request,Map<String,String> params){
		 // 创建磁盘工厂，利用构造器实现内存数据储存量和临时储存路径
       DiskFileItemFactory factory = new DiskFileItemFactory();
       // 产生一新的文件上传处理程式
       ServletFileUpload upload = new ServletFileUpload(factory);
       // 设置路径、文件名的字符集
       upload.setHeaderEncoding("UTF-8");
       // 设置允许用户上传文件大小,单位:字节
       upload.setSizeMax(MAX_SIZE);
       // 单个上传文件的最大尺寸
       upload.setFileSizeMax(MAX_FILE_SIZE);
       // 得到所有的表单域，它们目前都被当作FileItem
       try {
			List<FileItem> fileItems = upload.parseRequest(request);
			// 依次处理请求
           Iterator<FileItem> iter = fileItems.iterator();
           while (iter.hasNext()) {
               FileItem item = (FileItem) iter.next();
               params.put(item.getFieldName(), URLDecoder.decode(item.getString(),"UTF-8"));
           }
		} catch (FileUploadException e) {
			e.printStackTrace();
			errMessage = "转换参数时的文件上传过程发生异常";
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
			errMessage = "转换参数时发生异常";
		} finally{
			
		}
	}
	
	/**
	 * 
	 * 用于为数据库的批量操作准备字符串参数
	 *
	 * @param origin(sample:1,2,3,4)
	 * @return result(like:'1','2','3','4')
	 */
	public static String multiIdModify(String origin){
		StringBuffer result = new StringBuffer();
		try{
			String[] arr = origin.split(",");
			if(arr.length>0){
				result.append("'");
				result.append(arr[0]);
				result.append("'");
				if (arr.length>1) {
					for (int i = 1; i < arr.length; i++) {
						result.append(",'");
						result.append(arr[i]);
						result.append("'");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result.toString();
	}
	
	public static String getAscii(String str) {  
		String tmp;  
		StringBuffer sb = new StringBuffer(1000);  
		char c;  
		int i, j;  
		sb.setLength(0);  
		for (i = 0; i < str.length(); i++) {  
			c = str.charAt(i);  
			if (c > 255) {  
				sb.append("\\u");  
				j = (c >>> 8);  
				tmp = Integer.toHexString(j);  
				if (tmp.length() == 1)  
					sb.append("0");  
				sb.append(tmp);  
				j = (c & 0xFF);  
				tmp = Integer.toHexString(j);  
				if (tmp.length() == 1)  
					sb.append("0");  
				sb.append(tmp);  
			} else {  
				sb.append(c);  
			}  

		}  
		return (new String(sb));  
	}  

	
	public static void main(String[] args){
//		System.out.println(StringUtils.countMatches("1,,2,", ","));
//		System.out.println(StringUtils.isEmpty(" "));
		System.out.println(getAscii("请注意那些无法勾选的单据，它们的出门区域不是您所在的区域"));
		System.out.println(getAscii("所有相关单据的对应出门区域都不是您所在的区域，无法进行出厂申报操作"));
		System.out.println("GA43575".substring(1));
//		ArrayList list = new ArrayList();
//		System.out.println(list.get(0));
	}
}
