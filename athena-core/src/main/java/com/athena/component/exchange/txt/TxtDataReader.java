/**
 * 
 */
package com.athena.component.exchange.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.component.exchange.DataExchange;
import com.athena.component.exchange.DataReader;
import com.athena.component.exchange.DataWriter;
import com.athena.component.exchange.FileLog;
import com.athena.component.exchange.InziDbUtils;
import com.athena.component.exchange.ParserException;
import com.athena.component.exchange.RowParser;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.config.ExchangerConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.component.runner.RunnerService;



/**
 * <p>Title:数据交换平台</p>
 *
 * <p>Description:文本数据reader</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0
 */
public class TxtDataReader implements DataReader {
	private static final Log logger = LogFactory.getLog(TxtDataReader.class);
	
	private InputStreamReader inputStreamReader;
	
	private RowParser<String> rowParser;
	
	private DataParserConfig dataParserConfig;
	
	//支持多文件的读取	
	private List<FileInputStream> fileInputStreams;
	//为文件删除使用
	private File[] files;
	
	//文件运行时间
	private String file_begintime;
	//文件结束时间
	private String file_endtime;
	
	//为了解决exchange的静态变量cid的引用
	private DataExchange de; 
	
	public DataExchange getDe() {
		return de;
	}

	public void setDe(DataExchange de) {
		this.de = de;
	}

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public List<FileInputStream> getFileInputStreams() {
		return fileInputStreams;
	}

	public void setFileInputStreams(List<FileInputStream> fileInputStreams) {
		this.fileInputStreams = fileInputStreams;
	}

	public DataParserConfig getDataParserConfig() {
		return dataParserConfig;
	}

	public void setDataParserConfig(DataParserConfig dataParserConfig) {
		this.dataParserConfig = dataParserConfig;
	}

	public TxtDataReader(DataParserConfig dataParserConfig) {
		this.dataParserConfig = dataParserConfig;
	}

	@Override
	public void open(DataWriter dataWriter,RunnerService runnerService) {
		ExchangerConfig readerConfig = dataParserConfig.getReaderConfig();
		//使用默认的输入文件配置
		if(inputStreamReader==null&&readerConfig.getFilePath()!=null){
			try {
				//做可以读取多文件的 文件的开头名称必须一致  如：A1.txt;A2.txt;会解析此两个文件
				fileInputStreams = getFileInputList(readerConfig.getFilePath());
			} catch (FileNotFoundException e) {
				throw new ParserException("文件【"+readerConfig.getFilePath()+"】未找到！");
			}
		}
		rowParser = 
			new TxtRowParser(dataParserConfig, dataWriter,runnerService);
	}

	/* (non-Javadoc)
	 * @see com.athena.component.exchange.DataReader#readLine()
	 */
	@Override
	public int readLine() {
		int i=0;
		String encoding = dataParserConfig.getReaderConfig().getEncoding();
		//String tableName = dataParserConfig.getWriterConfig().getTable();
		String tableName = dataParserConfig.getWriterConfig().getTable();
		if(tableName.contains("${")){
			String[] strs = SpaceFinal.replaceSql(tableName).split("\\.");
			if(strs.length>1){
				tableName = strs[1];
			}
		}
		
		if(fileInputStreams!=null){
			for(int k=0;k<fileInputStreams.size();k++){
				file_begintime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				
				//设置文件名称  文件路径, 	设置dbwriter对应文件的错误数量 ，更新数量，插入数量
				if(((TxtRowParser)rowParser).getDataWriter() instanceof DbDataWriter){
					
					//根据操作系统 将win操作系统的\替换为/ 入库
					String filePath = files[k].getPath();
					String sysName = System.getProperty("os.name");
					if(sysName.startsWith("win")||sysName.startsWith("Win")){
						filePath = filePath.replace("\\", "/");
					}
					((DbDataWriter)((TxtRowParser)rowParser).getDataWriter()).setWENJLJ(filePath);
					((DbDataWriter)((TxtRowParser)rowParser).getDataWriter()).setWENJMC(files[k].getName());
					((DbDataWriter)((TxtRowParser)rowParser).getDataWriter()).setFILE_ERROR_COUNT(0);
					((DbDataWriter)((TxtRowParser)rowParser).getDataWriter()).setFILE_INSERT_COUNT(0);
					((DbDataWriter)((TxtRowParser)rowParser).getDataWriter()).setFILE_UPDATE_COUNT(0);
				}

				
				int j = 0;
				if(encoding==null)encoding="GBK";
				
				try {
					inputStreamReader = new InputStreamReader(fileInputStreams.get(k),encoding);
				} catch (UnsupportedEncodingException e1) {
					//不支持 此编码 不做处理
				} 
				
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				long startTime = System.currentTimeMillis();			
				String line = null; 	
				
				try {
					((TxtRowParser)(rowParser)).setFileName(files[k].getName()); //设置解析的文件名
					while((line = bufferedReader.readLine()) != null){
						//剔除空行  2012-08-24
						if(!"".equals(line)){
							rowParser.parse(++j,line);
						}
					}
					logger.info("解析文本" + j + "行");
					rowParser.complete();//结束解析
					//设置文件解析结束时间
					file_endtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					
					logger.debug("文件解析完成，共解析记录"+j+"条,耗时"+(System.currentTimeMillis()-startTime)+"毫秒.");
					logger.debug("文件解析完成，共解析记录"+j+"条,耗时"+(System.currentTimeMillis()-startTime)+"毫秒.");
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.error("TxtDataReader  出错： "+e.getMessage());				
				}finally{
					close();
					
					//2：生成日志表 接口文件记录信息日志 记录;同时将 datawriter中的ErrorFileName对应的值改为表中的日志主键，为了后面生成 接口文件错误记录信息 使用
					Map<String,String> errorNameMap = ((DbDataWriter)((TxtRowParser)rowParser).getDataWriter()).getErrorFileName();
					if(errorNameMap.get(files[k].getName())!=null){
						String SID = file_info(k); 
						
						//将errorNameMap中的对应主建值改成日志表id
						errorNameMap.remove(files[k].getName());
						errorNameMap.put(files[k].getName(), SID);
						
						//更新主日志 in_zidb 的错误文件数量
						//FileLog.getInstance(dataParserConfig.getWriterConfig().getDatasourceId()).UpdateInterface_info(de.getCID());
						InziDbUtils.getInstance().update_cuowjsl(dataParserConfig.getWriterConfig().getDatasourceId(), de.getCID());
					}else{
						//此文件时成功文件---解析没有错  如果是txt-->业务表  要记录文件到日志表中
						if(!tableName.toLowerCase().startsWith("in")){
							//txt-->业务表
							file_info(files[k].getName(),0,"1");
						}
					}
					
				}	
			}
			
			/**
			 * 对文件，日志的处理
			 */
			
			////2012-8-13 文件移动 由aotu做 我们不用对文件处理
//			//1：根据dbwriter记录的错误文件，将文件移到错误文件夹中，并将源文件夹中此文件删除
//			FileUtis.moveFile(errorNameMap, dataParserConfig.getReaderConfig().getErrorFilePath(),dataParserConfig.getReaderConfig().getFilePath()); //将文件从filePath移到到errorPath路径中
//			FileUtis.deleteFile(errorNameMap,dataParserConfig.getReaderConfig().getFilePath()); //删除此文件夹中的errorNameMap的文件
			
			//2：为了记录3表的日志
			Map<String,String> errorNameMap = ((DbDataWriter)((TxtRowParser)rowParser).getDataWriter()).getErrorFileName();
			ArrayList<Map<String, String>> errorMeList = ((DbDataWriter)((TxtRowParser)rowParser).getDataWriter()).getErrorMessageList();
			//日志记录
			in_errorfile_info(errorNameMap, errorMeList);
			
			
			//判断 是否为 txt--->中间表；   判断的依据：writer的表明 是否已in开头，中间表都是已in开头的，业务表不是
			/**
			 * txt-->中间表
			 * 	 isgoon为false:
			 * 		则为中止分发
			 * 		所以 有错误信息时，应做 1：更新日志的主表  将 状态改为 0（为接口执行错误） 
			 */
			if(tableName.toLowerCase().startsWith("in")){
				//txt-->中间表
				if("false".equals(de.getIsGoon()) && errorMeList.size()>0){
					//不分发  且有有错误信息
					if(errorMeList.size()>0){
						/**
						 * 有错误记录 则直接抛出异常  在 服务器DispatchServiceImpl类中 抓取此异常，并将接口总表的  接口状态设置为  出错；并维护 结束时间
						 */
						throw new RuntimeException("有错误数据，接口中止..");
					}
				}
			}else{
				//不为txt-->中间表  则为txt-->业务表  
				/**
				 * txt-->业务表  
				 * 	有错
				 * 		1：在每个文件解析完  判断 是的话 将没有错误的 写入到日志2表
				 * 		2：将错误文件  和  成功过的文件移动
				 *      3：更新 日志1表的状态和 结束时间  
				 */
				if(errorMeList.size()<=0){
					//2012-8-13 文件移动 由aotu做 我们不用对文件处理
//					//没有错误信息  说明执行成功   1:将文件移动到成功文件夹中 
//					FileUtis.moveFile(dataParserConfig.getReaderConfig().getFilePath(), dataParserConfig.getReaderConfig().getBackupFilePath());
//					FileUtis.deleteFile(dataParserConfig.getReaderConfig().getFilePath());
				}else{
					//有错误信息  则 认为此接口运行出错
					throw new RuntimeException("有错误数据，接口中止..");
				}
			}
		}
		//返回总记录数
		return i;
	}
	
	
	/**
	 * 记录文件日志
	 * @param wenjmc
	 */
	private String file_info(String wenjmc,int error_num,String file_satus) {
		DbDataWriter ddw = ((DbDataWriter)((TxtRowParser)rowParser).getDataWriter());
		String SID = DbDataWriter.getUUID();
		int insert_num = 0;
		int update_num = 0;
		if(!file_satus.equals("-1")){
			insert_num = ddw.getFILE_INSERT_COUNT();
			update_num = ddw.getFILE_UPDATE_COUNT();
		}
		if(error_num==-1){
			error_num = ddw.getFILE_ERROR_COUNT();
		}
		//文件做日志记录
		FileLog.getInstance(dataParserConfig.getWriterConfig().getDatasourceId()).File_info(SID,de.getCID(),wenjmc,file_begintime,file_endtime,insert_num,update_num,error_num,file_satus);
		return SID;
	}
	
	/**
	 * 向 接口文件错误记录信息 表中 记录日志
	 * @param errorNameMap  错误文件名
	 * @param errorMeList 错误信息集合
	 */
	private void in_errorfile_info(Map<String, String> errorNameMap,
			ArrayList<Map<String, String>> errorMeList) {
		if(errorMeList.size()>0){
			for(Map<String,String> map : errorMeList){
				String EID = DbDataWriter.getUUID();
				String SID = errorNameMap.get(map.get("WENJMC"));
				String file_errorinfo = map.get("ERRORMESSAGE");
				String error_date = map.get("ERROREXCEPTION");
				FileLog.getInstance(dataParserConfig.getWriterConfig().getDatasourceId()).File_ErrorInfo(EID,de.getCID(),SID,file_errorinfo,error_date);
			}
		}
	}
	
	/**
	 * 向 接口文件记录信息日志 记录日志  并将其主键返回
	 * @param k
	 * @return
	 */
	private String file_info(int k) {
		DbDataWriter ddw = ((DbDataWriter)((TxtRowParser)rowParser).getDataWriter());
		String SID = DbDataWriter.getUUID();
		int insert_num = 0;
		int update_num = 0;
		int error_num = ddw.getFILE_ERROR_COUNT();		
		//说明 此文件问错误文件  则将生成日志记录
		FileLog.getInstance(dataParserConfig.getWriterConfig().getDatasourceId()).File_info(SID,de.getCID(),files[k].getName(),file_begintime,file_endtime,insert_num,update_num,error_num,"-1");
		return SID;
	}

	/* (non-Javadoc)
	 * @see com.athena.component.exchange.DataReader#close()
	 */
	@Override
	public void close() {
		if(inputStreamReader==null)return;
		try {
			inputStreamReader.close();
		} catch (IOException e) {
			//关闭文件
		}
	}
	
	/**
	 * 如果readerConfigPath没有后缀名，则认为是要解析多个文件
	 *	
	 * @param readerConfigPath 路径
	 * @return 返回文件流集合
	 * @throws FileNotFoundException 
	 */
	private List<FileInputStream> getFileInputList(String readerConfigPath) throws FileNotFoundException{
		List<FileInputStream> fileInputStreams = new ArrayList<FileInputStream>();
		
			//对readerConfigPath 格式： /users/ath00/temp/abcdfd,sdss 做处理 
			// /users/ath00/temp/:路径
			// abcdfd.txt:要解析的文件名称 ,只要此路径下 文件名包含abcdfd则认为是要被解析的
			int top = readerConfigPath.lastIndexOf("/");
			String filePath = readerConfigPath.substring(0, top);
			String fileName = readerConfigPath.substring(top+1,readerConfigPath.length());
			File file = new File(filePath);
			File[] in_files = file.listFiles();
			List<File> fileList = new ArrayList<File>();
			if(in_files.length>0){
				//此文件夹中的 文件名称 含有此fileName字段 则认为此文件要被此接口解析的
				for(File f : in_files){
					if(isContainsFile(f.getName(),fileName)){
						fileInputStreams.add(new FileInputStream(f));
						fileList.add(f);
					}
				}
				files = fileList.toArray(new File[fileList.size()]);
			}
		
		return fileInputStreams;
	}
	
	/**
	 *  只要file包含abcdfd或者sdss 就返回真
	 * @param file 文件名称
	 * @param fileName 包含文件名 格式：abcdfd,sdss
	 * @return
	 */
	private  boolean isContainsFile(String file,String fileName){
		boolean result = false;
		String[] filestrs = fileName.split(",");
		for(String filestr : filestrs){
			if(file.contains(filestr)){
				result = true;
				break;
			}
		}
		return result;
	}
}
