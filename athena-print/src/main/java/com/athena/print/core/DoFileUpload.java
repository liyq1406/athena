package com.athena.print.core;

import java.io.File;
import java.util.Calendar;
import org.apache.log4j.Logger;
public class DoFileUpload{
	
	private static Logger logger=Logger.getLogger(DoFileUpload.class);
	
	//备份文档到服务器磁盘 (要判断操作系统  然后确定返回的路径)    目录规则  一级:print 二级:日期    三级：文件名
	public  String doFileUpload(String filecode) 
	{
		String picpath = "";
		String filepath = "";
		try {
			Calendar ca = Calendar.getInstance();
			//获得保存路径信息
				filepath = "F:";
		
			//保存路径 一级根目录-print
				picpath = "/" + "print"; 
				File path = new File(filepath+picpath);
				if(!path.exists()){
					path.mkdir();
				}
			//保存路径 二级目录-日期
		   			picpath = picpath + "/" + ca.get(Calendar.YEAR)+"-"+(ca.get(Calendar.MONTH)+1)+"-"+ca.get(Calendar.DATE);
		   			path = new File(filepath+picpath);
		   			if(!path.exists()){
		   			path.mkdir();
		   			}
		   			//保存路径  三级目录-文件名 
		   			picpath = picpath + "/" + filecode;
		   			path = new File(filepath+picpath);
		   			if(!path.exists()){
		   				path.mkdir();
		   			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}	
		if(System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
				return filepath+picpath;
		}else{
			return picpath;
		}
	}
	
	@SuppressWarnings("unused")
	public void Delete(File dir){
		//判断删除的该文件是否存在
		if(dir.isFile()){
			//该路径下是否是一个标准的文件
			dir.delete();
		}else{
			logger.info("文件不存在");
		}
		
	}
	
	public static void main(String[] args) {
		File dir = new File("F:/print/2012-3-28/cf3351d4-370c-4fd9-92f7-9ead200f/cf3351d4-370c-4fd9-92f7-9ead200f-1.txt");
		 new DoFileUpload().Delete(dir);
	}
}

