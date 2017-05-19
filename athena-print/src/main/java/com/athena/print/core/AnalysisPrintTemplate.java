/**
 * 解析模板替换参数类
 */
package com.athena.print.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;

import com.athena.component.runner.Command;
import com.athena.component.utils.JsonUtils;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.Area;
import com.athena.util.cache.Data;
import com.athena.util.cache.PrintConfig;
import com.athena.print.entity.pcenter.PrintQtaskinfo;
import com.athena.print.entity.pcenter.PrintQtasklist;
import com.athena.print.entity.pcenter.PrintQtasksheet;
import com.athena.util.cache.Cache;
import com.athena.util.cache.CacheManager;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * @author ZL
 *
 */
@Component
public class AnalysisPrintTemplate extends Command<Object>{
	//Log4j 
	protected static Logger logger=Logger.getLogger(AnalysisPrintTemplate.class);
	
	
	private AbstractIBatisDao baseDao;
	
	//private int taskHeight;  //任务总高度
	private int perPaperHeight=276; //每页纸的高度
	//private int beginHeight=6; //每页打印开始距纸张高度差
	private int endHeight=6;  //每页打印结束距纸张高度差
	private int totalPage;  //总页数
	private int curretPage = 1;  //每页的页数
	private int w;
	private String rows="";
	private String beginChar ="~CREATE;newlabel;";
	private StringBuffer p ;
	
	private CacheManager cacheManager;
	
	private List<PrintQtaskinfo> prepareTaskList;
	
	public AnalysisPrintTemplate(){
	}
	
	public AnalysisPrintTemplate(List<PrintQtaskinfo> prepareTaskList,AbstractIBatisDao baseDao,CacheManager cacheManager){
		super();
		this.baseDao=baseDao;
		this.prepareTaskList = prepareTaskList;
		this.cacheManager = cacheManager;
	}
	
	/**
	 * 启动线程后自动执行的方法
	 */
	@Override
	public void execute(){
		try{
			logger.info("********************自动执行 开始***************************");
			getFile(prepareTaskList,baseDao);
			logger.info("********************自动执行END***************************");
			
		}catch(Exception e){
			//backgroundRunLog.addExceptionError("生成打印文档", "UW", CommonUtil.MODULE_PRINT, "生成打印文档", "生成打印文档", CommonUtil.getClassMethod(),CommonUtil.replaceBlank(e.getMessage()) );
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	/**
	 * 处理准备任务总方法
	 * @param prepareTaskList 准备任务列表信息
	 * @throws Exception
	 */
	public void getFile( List<PrintQtaskinfo> prepareTaskList,AbstractIBatisDao baseDao ) throws RuntimeException{
		logger.info("********************准备任务列表信息 开始***************************");
		if( null == prepareTaskList || prepareTaskList.size() == 0 ) {
			return;
		}
		
		for( int i=0; i<prepareTaskList.size(); i++ ){
			w = 0;
			curretPage = 1;
			p=new StringBuffer();
			rows="";
			PrintQtaskinfo printQtaskinfo = (PrintQtaskinfo)prepareTaskList.get(i);
			dealPrepareTask(printQtaskinfo,baseDao);
		}
		logger.info("********************准备任务列表信息 END***************************");
	}

	/**
	 * 私有方法
	 */
	
	/**
	 * 获取缓存中指定的报表模板
	 * @param printModel 模板编号
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private PrintConfig getPrintModule( String printModel ) throws RuntimeException{		
		Cache cache = cacheManager.getCacheInfo(printModel);
		return (PrintConfig) cache.getCacheValue(); 
	}
	
	/**
	 * 处理单个准备任务信息总方法
	 * @param printQtasks 任务对象
	 * @param basePage    分页基数，常量
	 * @throws Exception
	 */
	private void dealPrepareTask(PrintQtaskinfo printQtaskinfo,AbstractIBatisDao baseDao) throws RuntimeException {
		logger.info("********************处理单个准备任务信息 开始***************************");
		//backgroundRunLog.addCorrect("处理单个准备任务的信息", "UW", CommonUtil.MODULE_PRINT, "处理单个准备任务", "处理单个准备任务开始");
		//从缓存中获取到组装的最外面一层的对象
		PrintConfig printConfig = getPrintModule(printQtaskinfo.getModelnumber());
		List list = printConfig.getAreaList();
		JSONArray jsonArray;
		List listjson;
		p.append("~CREATE;newlabel;\n");
		p.append("SCALE;DOT\n");
		int whith = Integer.valueOf(printConfig.getBeginHeight());
		//获取打印内容的时候需要先判断是否有多区域    1单区域     2多区域
		if("1".equals(printQtaskinfo.getQuyzs())){
			//根据作业编号和子任务编号去查询对应的区域里的打印数据
				for (int i = 0; i < list.size(); i++) {
					Area e = (Area)list.get(i);
					PrintQtasksheet ps =(PrintQtasksheet)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("pcenter.queryPrintsheet",printQtaskinfo);
					if(null==ps){
						continue;
					}
					//获取打印的数据
					jsonArray = JSONArray.fromObject(ps.getSpars());
					//json数据放入转化成List
					listjson = (List)JsonUtils.jsonToList(jsonArray);
					
					//调用解析的方法
					test(e,whith,listjson);
			}
		}else{
			//如果是多区域 根据对应的区域ID 与模板中的ID匹配后  依次获取打印的数据
			for (int i = 0; i < list.size(); i++) {
				Area e = (Area)list.get(i);
				String id = e.getId();
				String type = e.getType();
				PrintQtasksheet ps1 = new PrintQtasksheet();
				ps1.setQid(printQtaskinfo.getQid());
				ps1.setSeq(printQtaskinfo.getSeq());
				ps1.setArea(Integer.valueOf(id));
				//如果是sheet  根据id查询表单明细表
				if("sheet".equals(type)){
					PrintQtasksheet ps = (PrintQtasksheet)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("pcenter.querySheet",ps1);
					if(null==ps){
						continue;
					}
					//获取打印的数据
					jsonArray = JSONArray.fromObject(ps.getSpars());
					//json数据放入转化成List
					listjson = (List)JsonUtils.jsonToList(jsonArray);
					//调用解析的方法
					test(e,whith,listjson);
				}else{   //如果是grid   根据id查询清单明细表
					List<PrintQtasklist> pList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryGrid",ps1);
					if(0==pList.size()){
						continue;
					}
					List totaljson = new ArrayList();
					for (int j = 0; j < pList.size(); j++) {
						PrintQtasklist pt = pList.get(j);
						jsonArray = JSONArray.fromObject(pt.getSpars());
						//json数据放入转化成List
						listjson = (List)JsonUtils.jsonToList(jsonArray);
						Map m = null;
						if(0 != listjson.size() ){
							m = (Map)listjson.get(0) ;
						}
						totaljson.add(m);
					}
					//调用解析的方法
					test(e,whith,totaljson);
				}
			}
		}
		//获取总页数
		totalPage = curretPage;
			if(totalPage>1){
				for (int i = 0; i<totalPage; i++) {
					//第一次页码的开始横向
					int fPage = 12;
					int sr = fPage+(i*288); 
					p.append("TWOBYTE");
					p.append("\n");
					p.append("F16;P8;"+sr+";"+"485;0;0;"+"\""+"页码:"+"\"");
					p.append("\n");
					p.append("F16;P8;"+sr+";"+"506;0;0;"+"\""+(i+1)+"/"+totalPage+"\"");
					p.append("\n");
					p.append("STOP");
					p.append("\n");
				}	
				p.append("END\n");
				p.append("~EXECUTE;newlabel;1\n");
				p.append("~NORMAL\n");
				p.append("~DELETE FORM;newlabel\n");
				p.append("\n");
				p.replace(beginChar.length(), beginChar.length(), ""+(perPaperHeight*curretPage+(totalPage-1)*12));
			}else{
				p.append("END\n");
				p.append("~EXECUTE;newlabel;1\n");
				p.append("~NORMAL\n");
				p.append("~DELETE FORM;newlabel\n");
				p.append("\n");
				p.replace(beginChar.length(), beginChar.length(), ""+perPaperHeight*curretPage);
			}
			
			//System.out.println(p);
			// 生成TXT  并取得路径
			String path = doFileUpload(printQtaskinfo.getQid());
			domParseUtil(p,path+"/"+printQtaskinfo.getQid()+"-"+printQtaskinfo.getSeq()+".txt");
			//更新主列表中的作业的状态为  新作业
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("pcenter.updatePrintQtaskmain2",printQtaskinfo.getQid());
			// 更新子列表中的文件路径
			PrintQtaskinfo bean = new PrintQtaskinfo();
			bean.setQid(printQtaskinfo.getQid());
			bean.setSeq(printQtaskinfo.getSeq());
			bean.setSfilename(path+"/"+printQtaskinfo.getQid()+"-"+printQtaskinfo.getSeq()+".txt");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskinfo2",bean);
			
			logger.info("********************处理单个准备任务信息 END***************************");
			//backgroundRunLog.addCorrect("处理单个准备任务的信息", "UW", CommonUtil.MODULE_PRINT, "处理单个准备任务", "处理单个准备任务结束");
	}
	
	/*
	 * 生成  TXT文本
	 */
	private void domParseUtil(StringBuffer sb,String path) throws RuntimeException{
		logger.info("********************生成  TXT文本 开始***************************");
		logger.info("********************"+path+"***************************");
		
		try {
			
			logger.info("********************文件到服务器开始***************************");
			
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
			out.write(sb.toString().getBytes("GBK"));
		
			logger.info("********************文件到服务器END***************************");
			out.close(); 
		} catch (Exception e) {
			logger.error("文件写到服务器失败");
			throw new RuntimeException(e);
		}
		logger.info("********************生成  TXT文本 END***************************");
	}
	
	//备份文档到服务器磁盘 (要判断操作系统  然后确定返回的路径)    目录规则  一级:print 二级:日期    三级：文件名
	public  String doFileUpload(String filecode) throws RuntimeException{
		logger.info("********************备份文档到服务器磁盘 开始**************************");
		String picpath = "";
		String filepath = "";
		try {
			Calendar ca = Calendar.getInstance();
			//获得保存路径信息
				filepath = "C:";
		
			//保存路径 一级根目录-print
				
				picpath = File.separator + "Oracle" + File.separator + "print"; 
				
				if(System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
					File path = new File(filepath+picpath);
					if(!path.exists()){
						logger.info("********************保存路径 一级根目录-print开始***************************");
						path.mkdir();
						logger.info("********************保存路径 一级根目录-print结束***************************");
					}
					
				//保存路径 二级目录-日期
		   			picpath = picpath + File.separator + ca.get(Calendar.YEAR)+"-"+(ca.get(Calendar.MONTH)+1)+"-"+ca.get(Calendar.DATE);
		   			path = new File(filepath+picpath);
		   			if(!path.exists()){
		   				logger.info("********************保存路径 二级目录-日期 开始***************************");
		   				path.mkdir();
		   				logger.info("********************保存路径 二级目录-日期 结束***************************");
		   			}
		   			
		   			//保存路径  三级目录-文件名 
		   			picpath = picpath + File.separator + filecode;
		   			path = new File(filepath+picpath);
		   			if(!path.exists()){
		   				logger.info("********************保存路径  三级目录-文件名  开始***************************");
		   				path.mkdir();
		   				logger.info("********************保存路径  三级目录-文件名  结束***************************");
		   			}
		   			
		   			logger.info("********************备份文档到windows磁盘 END***************************");
					logger.info("********************保存路径  ***************************" + filepath+picpath);
					return filepath+picpath;
				}else{
					File path = new File(picpath);
					if(!path.exists()){
						logger.info("********************保存路径 一级根目录-print开始***************************");
						path.mkdir();
						logger.info("********************保存路径 一级根目录-print结束***************************");
					}
					
					//保存路径 二级目录-日期
		   			picpath = picpath + File.separator + ca.get(Calendar.YEAR)+"-"+(ca.get(Calendar.MONTH)+1)+"-"+ca.get(Calendar.DATE);
		   			path = new File(picpath);
		   			if(!path.exists()){
		   				logger.info("********************保存路径 二级目录-日期 开始***************************");
		   				path.mkdir();
		   				logger.info("********************保存路径 二级目录-日期 结束***************************");
		   			}
		   			
		   			//保存路径  三级目录-文件名 
		   			picpath = picpath + File.separator + filecode;
		   			path = new File(picpath);
		   			if(!path.exists()){
		   				logger.info("********************保存路径  三级目录-文件名  开始***************************");
		   				path.mkdir();
		   				logger.info("********************保存路径  三级目录-文件名  结束***************************");
		   			}
		   			logger.info("********************备份文档到Linux磁盘 END***************************");
					logger.info("********************保存路径  ***************************" + picpath);
					return picpath;
				}
	   			
		} catch (Exception e) {
			logger.error("获得保存路径信息报错。");
			logger.error(e.getMessage());
			throw new RuntimeException("获得保存路径信息报错。");
		}	
//		if(System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
//			logger.info("********************备份文档到windows磁盘 END***************************");
//			logger.info("********************保存路径  ***************************" + filepath+picpath);
//			return filepath+picpath;
//		}else{
//			logger.info("********************备份文档到Linux磁盘END***************************");
//			logger.info("********************保存路径  ***************************" + picpath);
//			return picpath;
//		}
	}
	
	private  void test(Area e, int whith,List listjson ){
		int autorSrc = Integer.valueOf(e.getAutoSr());
		List listData = e.getDataList();
		String sr = "";
		String value = "";
			int n =0 ;
			while(true){
				Map mapArg = null;
				if(0 < listjson.size()){
					mapArg = (Map)listjson.get(n);
				}
				for( int i = 0; i<listData.size(); i++ ){
					Data data = (Data)listData.get(i);
					sr = data.getSr();
					if(null!=data){
						p.append(data.getDataType());
						p.append("\n");
						//标准的 汉字 打印
						if("TWOBYTE".equals(data.getDataType())){   
							if(null!=data.getSr()&&"#sr".equals(sr)){
								if("".equals(rows)){
									w = whith+autorSrc;
								}else if(Integer.valueOf(rows)!=Integer.valueOf(data.getRow())+n){
									w +=autorSrc;
								}
							}else{
								if("".equals(rows)){
								     w = Integer.valueOf(data.getSr());
								}else if(Integer.valueOf(rows)!=Integer.valueOf(data.getRow())+n){
									w += Integer.valueOf(data.getSr());
								}
							}
							
							//横坐标累加后高度超过一张纸的标准 高度  则换页
							if(w>perPaperHeight*curretPage-endHeight){
								w = 288*curretPage+whith+14;
								curretPage++;
							}            
							
							p.append(data.getFont()+w+";"+data.getSc()+data.getVe()+data.getHe());
							//System.out.println(p);
							//替换需要填充的value值
							if(0!=listjson.size()){
								if(null!=data.getValue()&& data.getValue().contains("#replace")){
									value = data.getValue();
									if(null!=mapArg.get(data.getKey())){
										value = value.replace("#replace","\""+mapArg.get(data.getKey())+"\"");
									}else{
										value = value.replace("#replace","\""+"\"");
									}
									p.append(value);
									p.append("\n");
								}else if(null!=data.getValue()&& !data.getValue().contains("#replace")){
									value = "\""+data.getValue()+"\"";	
									p.append(value);
									p.append("\n");
								}else{
									p.append("\n");
								}	
							}else{
								if(null!=data.getValue()&& !data.getValue().contains("#replace")){
									value = "\""+data.getValue()+"\"";
									p.append(value);
									p.append("\n");
								}else{
									p.append("\n");
								}	
							}
						}
						
						//标准的标签打印
						if("BARCODE".equals(data.getDataType())){   
							p.append(data.getFont()+data.getSr()+data.getSc());
							p.append("\n");
							//替换需要填充的value值
							if(null!=data.getValue()&& data.getValue().contains("#replace")){
								value = data.getValue();
								value = value.replace("#replace","\""+mapArg.get(data.getKey())+"\"");
								p.append(value);
								p.append("\n");
							}else if(null!=data.getValue()&& !data.getValue().contains("#replace")){
								value = "\""+data.getValue()+"\"";	
								p.append(value);
								p.append("\n");
							}else{
								p.append("\n");
							}		
						}
						
						p.append("STOP");
						p.append("\n");
					}
					int k = Integer.valueOf(data.getRow())+ n ;
					rows = k+"";
				}
				n++;
				if(n >= listjson.size()){
					break;
				}
			}
	}
	
	@Override
	public Object result() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
