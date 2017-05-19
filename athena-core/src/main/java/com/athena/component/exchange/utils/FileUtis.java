package com.athena.component.exchange.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
 

/**
 * 输入平台的文件移动工具类
 * @author chenlei
 * @vesion 1.0
 * @date 2012-5-30
 */
public class FileUtis {
	protected static Logger logger = Logger.getLogger(FileUtis.class);	//定义日志方法
	/**
	 * 
	 * @param filePath 源文件路径
	 * @param newPath  目标路径
	 * @param charSet 文件编码
	 * @param name 文件名称
	 * @param moveToError 移动到哪里   true:移动到错误文件夹中，false:移动到备份文件夹中；此属性是在newPath为null时，生成默认路径时才有用.
	 */
	public static void moveFile(String filePath,String newPath,String charSet,String name,boolean moveToError){
		if(newPath==null){
			/**
			 * 生成默认文件夹
			 * 	moveToError：
			 * 		true：  生成为：filePath+"_ERROR"
			 * 		false: 生成为：filePath+"_BACKUP"
			 */
			if(moveToError){
				newPath=filePath+"_ERROR";
			}else{
				newPath=filePath+"_BACKUP";
			}
		}
		
		/**
		 * 编码为null 则为GBK
		 */
		if(charSet==null){
			charSet = "GBK";
		}
		
		try {
			FileInputStream fis = new FileInputStream(new File(filePath+"/"+name));
			moveFile(fis, newPath, charSet, name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 将文件从oldPath移动到newPath路劲中
	 * @param InputStream in 要移动的文件流
	 * @param newPath 要移动的文件路径
	 */
	public static void moveFile(InputStream in,String newPath,String charSet,String name){
		if(newPath!=null){
			//此路劲不为空，才执行移动
			 FileOutputStream fs=null;
			 InputStreamReader ir = null;
			try {
				//如果要生成的newPath不存在 则要创建此文件夹
				File file = new File(newPath);
				if(!file.exists()){
					file.mkdirs();
				}
				
				//有的话，则生成
                fs = new FileOutputStream(makePath(newPath,name));
                
                try {
					ir = new InputStreamReader(in,charSet);
				} catch (UnsupportedEncodingException e1) {
					//不支持 此编码 不做处理
				} 
				
				BufferedReader bufferedReader = new BufferedReader(ir);
				String line = null;
				while((line = bufferedReader.readLine()) != null){ 
					line = line +"\n";
					fs.write(line.getBytes(charSet));
				}
                              
			} catch (Exception e) { 
				  logger.error("复制单个文件操作出错...");
				  logger.error(e.getMessage());
			}finally{
				if(fs!=null){
					try {
						in.close();
						fs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(in!=null){
					try {
						in.close();
						ir.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}  
			}

		}
	}
	
	/**
	 * 生成一个路径 默认生成当前时间为文件名的txt文件名
	 * @param path
	 * @return
	 */
	public static String makePath(String path,String name){
		name = name.split("\\.")[0];
		StringBuffer result = new StringBuffer();	
		result.append(path);
		result.append("/");
		result.append(name+"_"+new Date().getTime());
		result.append(".txt");		
		return result.toString();
	}
	
	/**
	 * 删除此路径中的所有文件
	 * @param filePath
	 */
	public static void deleteFile(String filePath) {
		
		//为了兼容直接写后缀名的
		File file = new File(filePath);
		if(file.isDirectory()){
			//因为里面都是文件，所以直接删除即可
			for(File f : file.listFiles()){
				if(f.isFile()){
					f.delete();
				}
			}
		}
		
	}
	 
	
	/**
	 *  将errorNameMap中对应的文件名从filePath移到为fileToPath
	 * @param errorNameMap 文件名的集合
	 * @param fileToPath 文件要移动到哪个路劲
	 * @param filePath 文件现在所在的路径
	 */
	@SuppressWarnings("rawtypes")
	public static void moveFile(Map<String,String> errorNameMap,String fileToPath,String filePath){
		if(errorNameMap!=null){
			if(errorNameMap.size()>0){
				//遍历此Map集合
				Iterator keys = errorNameMap.keySet().iterator();				
				while(keys.hasNext()){
					String key = (String) keys.next();//拿到文件名称
					moveFile(filePath,fileToPath,null,key,true);					
				}

			}
		}
	}
	
	/**
	 *  删除此路径下中的对应文件名的文件
	 * @param errorNameMap 文件名集合
	 * @param filePath 文件所在路径
	 */
	@SuppressWarnings("rawtypes")
	public static void deleteFile(Map<String, String> errorNameMap,
			String filePath) {
		if(errorNameMap!=null){
			if(errorNameMap.size()>0){
				//遍历此Map集合
				Iterator keys = errorNameMap.keySet().iterator();				
				while(keys.hasNext()){
					String key = (String) keys.next();//拿到文件名称					
					new File(filePath+"/"+key).delete();
				}

			}
		}
	}
	/**
	 *  将源路径的文件移动到目标路径中去
	 * @param filePath 源路径
	 * @param backUpPath 目标路径
	 */
	public static void moveFile(String filePath, String backUpPath) {
		File file = new File(filePath);
		File[] files  = file.listFiles();
		for(int i=0;i<files.length;i++){
			moveFile(filePath, backUpPath, null, files[i].getName(), true);
		}
		
	}
	
	
	/**
	 * 通过反射方式实例化对象
	 * @author Hezg
	 * @date 2013-6-26
	 * @param cls
	 * @return String Class
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Class getClass(String cls) throws ClassNotFoundException{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return loader.loadClass(cls);
	}
	
}
