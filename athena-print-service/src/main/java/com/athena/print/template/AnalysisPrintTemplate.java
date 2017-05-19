/**
 * 解析模板替换参数类
 */
package com.athena.print.template;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import net.sf.json.JSONArray;
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
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * @author ZL
 *
 */
@Component
public class AnalysisPrintTemplate{
	//Log4j 
	static Logger logger=Logger.getLogger(AnalysisPrintTemplate.class);
	
	private AbstractIBatisDao baseDao;
	private int taskHeight=288; //纸总高度
	private int perPaperHeight=276; //每页纸的高度
	private int otherperPaperHeight=384; //另一种纸的高度(针对p28 1个模板设定的高度)
	private int othertaskHeight=403;
	private int endHeight=12;  //每页打印结束距纸张高度差
	private int totalPage;  //总页数
	private int curretPage = 1;  //每页的页数
	private int w;
	private String rows="";
	private String beginChar ="~CREATE;newlabel;";
	private StringBuffer p ;
	private String qId;
	private CacheManager cacheManager;
	
	private List<PrintQtaskinfo> prepareTaskList;

	public AnalysisPrintTemplate() {

    }
	public AnalysisPrintTemplate(String qId, List<PrintQtaskinfo> prepareTaskList, AbstractIBatisDao baseDao,CacheManager cacheManager){
        super();
        this.qId = qId;
		this.baseDao=baseDao;
		this.prepareTaskList = prepareTaskList;
		this.cacheManager = cacheManager;
	}
	
	//执行生成文档方法
	public void execute(){
        logger.info("作业"+prepareTaskList.get(0).getQid()+"执行 开始");
        getFile(prepareTaskList);
        logger.info("作业"+prepareTaskList.get(0).getQid()+"执行END");
		
	}
	
	
	/**
	 * 处理准备任务总方法
	 * @param prepareTaskList 准备任务列表信息
	 * @throws Exception
	 */
	public void getFile( List<PrintQtaskinfo> prepareTaskList) throws RuntimeException{
			logger.info("作业"+prepareTaskList.get(0).getQid()+"准备任务列表信息 开始");
			if( null == prepareTaskList || prepareTaskList.size() == 0 ){
				return;
			}
			
			for( int i=0; i<prepareTaskList.size(); i++ ){
				w = 0;
				curretPage = 1;
				p=new StringBuffer();
				rows="";
				PrintQtaskinfo printQtaskinfo = prepareTaskList.get(i);
				if(printQtaskinfo.getPrintnumber()>1){
					delmanyTask(printQtaskinfo);
				}else{
					dealPrepareTask(printQtaskinfo);
				}
			}
			logger.info("作业"+prepareTaskList.get(0).getQid()+"准备任务列表信息 END");
	}

	
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
     * @param printQtaskinfo
     * @throws RuntimeException
     */
	private void dealPrepareTask(PrintQtaskinfo printQtaskinfo) throws RuntimeException {
		logger.info("作业"+printQtaskinfo.getQid()+"处理单个准备任务信息 开始");
		//从缓存中获取到组装的最外面一层的对象
		PrintConfig printConfig = getPrintModule(printQtaskinfo.getModelnumber());
		List list = printConfig.getAreaList(); 
		p.append(beginChar+"\n");
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
					JSONArray jsonArray = JSONArray.fromObject(ps.getSpars());
					//json数据放入转化成List
					List listjson = (List)JsonUtils.jsonToList(jsonArray);
					//调用解析的方法
					if("p28".equals(printQtaskinfo.getModelnumber())){
						createDocument(e,whith,listjson,otherperPaperHeight,othertaskHeight);
					}else{
						createDocument(e,whith,listjson,taskHeight,taskHeight);
					}
					
			}
		}else{
			 ParseManyArea(printQtaskinfo, whith,list);
		}
		//获取总页数
		totalPage = curretPage;
			if(totalPage>1){
				totalPage(totalPage,printQtaskinfo);
			}else{
				singlePage(printQtaskinfo);
			}
		logger.info("作业"+printQtaskinfo.getQid()+"处理单个准备任务信息 END");	
	}
	
	
	/*
	 * 生成  TXT文本
	 */
	private void domParseUtil(StringBuffer sb,String path) throws ServiceException{
		logger.info("生成  TXT文本 开始");
		try {
			logger.info("作业" + qId + "文件路径" + path);
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
			out.write(sb.toString().getBytes("GBK"));
			out.close(); 
		} catch (Exception e) {
			logger.error("文件写到服务器失败" + e.getMessage());
			throw new ServiceException(e);
		}
		logger.info("生成  TXT文本 END");
	}
	
	//备份文档到服务器磁盘 (要判断操作系统  然后确定返回的路径)    目录规则  一级:print 二级:日期    三级：文件名
	public  String doFileUpload(String filecode) throws RuntimeException{
		logger.info("备份文档到服务器磁盘 开始");
		String picpath = "";
		String filepath = "C:";
		try {
				Calendar ca = Calendar.getInstance();
				//获得保存路径信息
				picpath = File.separator + "Oracle" + File.separator + "print"; 
				if(System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1){
					return StockWindows(filepath,picpath,ca,filecode);
				}else{
					return StockLinux(picpath,ca,filecode);
				}
		} catch (Exception e) {
			logger.error("获得保存路径信息报错。");
			logger.error(e.getMessage());
			throw new RuntimeException("获得保存路径信息报错");
		}
	}
	
	/*
	 * 解析sheet表单的数据到模板中
	 */
	private void ParseManyArea(PrintQtaskinfo printQtaskinfo,int whith,List list){
		//如果是多区域 根据对应的区域ID 与模板中的ID匹配后  依次获取打印的数据
		List totaljson = new ArrayList();
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
				totaljson = new ArrayList();
				//获取打印的数据
				JSONArray jsonArray = JSONArray.fromObject(ps.getSpars());
				//json数据放入转化成List
				totaljson = (List)JsonUtils.jsonToList(jsonArray);
				//调用解析的方法 
				
			}else{   //如果是grid   根据id查询清单明细表

                logger.info("作业" + qId + "读出表格清单条件:" + ps1.getArea() + "-" + ps1.getQid() + "-" + ps1.getSeq());
				List<PrintQtasklist> pList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("pcenter.queryGrid",ps1);
                logger.info("作业" + qId + "读出表格清单记录条数:" + pList.size());
				if(0==pList.size()){
					continue;
				}
				totaljson = new ArrayList();
				for (int j = 0; j < pList.size(); j++) {
					PrintQtasklist pt = pList.get(j);
                    JSONArray temp = JSONArray.fromObject(pt.getSpars());
					//json数据放入转化成List
					List listjson = (List)JsonUtils.jsonToList(temp);
					Map m = null;
					if(0 != listjson.size() ){
						m = (Map)listjson.get(0) ;
					}
					totaljson.add(m);
				}

				
			}
			//调用解析的方法
			if("p28".equals(printQtaskinfo.getModelnumber())){
				createDocument(e,whith,totaljson,otherperPaperHeight,othertaskHeight);
			}else{
				 createDocument(e,whith,totaljson,taskHeight,taskHeight);
			}
		}
	}
	
	/*
	 * 对多页的数据进行分页
	 */
	private void totalPage(int totalPage,PrintQtaskinfo printQtaskinfo){
        logger.info("作业" + this.qId + "开始对多页的数据进行分页");
		for (int i = 0; i<totalPage; i++) {
			if("p28".equals(printQtaskinfo.getModelnumber())){
				//第一次页码的开始横向
				int fPage = 12;
				int sr = fPage+(i*403); 
				p.append("TWOBYTE");
				p.append("\n");
				p.append("F16;P8;"+sr+";"+"698;0;0;"+"\""+"页码:"+"\"");
				p.append("\n");
				p.append("F16;P8;"+sr+";"+"719;0;0;"+"\""+(i+1)+"/"+totalPage+"\"");
				p.append("\n");
				p.append("STOP");
				p.append("\n");
			}else{
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
		}	
		p.append("END\n");
		p.append("~EXECUTE;newlabel;1\n");
		p.append("~NORMAL\n");
		p.append("~DELETE FORM;newlabel\n");
		p.append("\n");
		if(!(printQtaskinfo.getPrintnumber()>1)){
			if("p28".equals(printQtaskinfo.getModelnumber())){
				p.replace(beginChar.length(), beginChar.length(), ""+(otherperPaperHeight*curretPage+(totalPage-1)*12));
			}else{
				p.replace(beginChar.length(), beginChar.length(), ""+(perPaperHeight*curretPage+(totalPage-1)*12));
			}
			// 生成TXT  并取得路径
			String path = doFileUpload(printQtaskinfo.getQid());
			domParseUtil(p,path+File.separator+printQtaskinfo.getQid()+"-"+printQtaskinfo.getSeq()+".txt");
			// 更新子列表中的文件路径
			PrintQtaskinfo bean = new PrintQtaskinfo();
			bean.setQid(printQtaskinfo.getQid());
			bean.setSeq(printQtaskinfo.getSeq());
			bean.setSfilename(path+File.separator+printQtaskinfo.getQid()+"-"+printQtaskinfo.getSeq()+".txt");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskinfo2",bean);
		}
        logger.info("作业" + this.qId + "结束对多页的数据进行分页");
	}
	
	/*
	 * 对单页的数据进行处理
	 */
	private void singlePage(PrintQtaskinfo printQtaskinfo){
        logger.info("作业" + this.qId + "开始对单页的数据进行处理");
		p.append("END\n");
		p.append("~EXECUTE;newlabel;1\n");
		p.append("~NORMAL\n");
		p.append("~DELETE FORM;newlabel\n");
		p.append("\n");
		if(!(printQtaskinfo.getPrintnumber()>1)){
			if("p28".equals(printQtaskinfo.getModelnumber())){
				p.replace(beginChar.length(), beginChar.length(), ""+(otherperPaperHeight*curretPage+(totalPage-1)*12));
			}else{
				p.replace(beginChar.length(), beginChar.length(), ""+(perPaperHeight*curretPage+(totalPage-1)*12));
			}
			//System.out.println(p);
			// 生成TXT  并取得路径
			String path = doFileUpload(printQtaskinfo.getQid());
			domParseUtil(p,path+File.separator+printQtaskinfo.getQid()+"-"+printQtaskinfo.getSeq()+".txt");
			//更新主列表中的作业的状态为  新作业
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("pcenter.updatePrintQtaskmain2",printQtaskinfo.getQid());
			// 更新子列表中的文件路径
			PrintQtaskinfo bean = new PrintQtaskinfo();
			bean.setQid(printQtaskinfo.getQid());
			bean.setSeq(printQtaskinfo.getSeq());
			bean.setSfilename(path+File.separator+printQtaskinfo.getQid()+"-"+printQtaskinfo.getSeq()+".txt");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskinfo2",bean);
		}
        logger.info("作业" + this.qId + "结束对单页的数据进行处理");
	}
	
	/*
	 * 根据纸张高度不同来创建 文档
	 */
	private  void createDocument(Area e, int whith,List listjson,int highTask,int otherhighTask){
        logger.info("作业" + this.qId + "生成开始文本字符");
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
							if(w>otherhighTask*curretPage-endHeight){
								w = highTask*curretPage+whith+14;
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
						
						//标准的 汉字 打印
						if("ALPHA".equals(data.getDataType())){   
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
							if(w>otherhighTask*curretPage-endHeight){
								w = highTask*curretPage+whith+14;
								curretPage++;
							}            
							
							p.append(data.getFont()+"DARK;"+w+";"+data.getSc()+data.getVe()+data.getHe());
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
        logger.info("作业" + this.qId + "生成结束文本字符");
	}
	
	
	/*
	 * 处理多个单据任务的方法
	 */
	private void delmanyTask(PrintQtaskinfo printQtaskinfo){
		for (int j = 0; j < printQtaskinfo.getPrintnumber(); j++) {
			dealPrepareTask(printQtaskinfo);
			w = 0;
			curretPage = 1;
			rows="";
		}
		curretPage = totalPage;
		p = new StringBuffer(p.toString().replace(beginChar, beginChar+(perPaperHeight*curretPage+(totalPage-1)*12)));
		// 生成TXT  并取得路径
		String path = doFileUpload(printQtaskinfo.getQid());
		domParseUtil(p,path+File.separator+printQtaskinfo.getQid()+"-"+printQtaskinfo.getSeq()+".txt");
		// 更新子列表中的文件路径
		PrintQtaskinfo bean = new PrintQtaskinfo();
		bean.setQid(printQtaskinfo.getQid());
		bean.setSeq(printQtaskinfo.getSeq());
		bean.setSfilename(path+File.separator+printQtaskinfo.getQid()+"-"+printQtaskinfo.getSeq()+".txt");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("pcenter.updatePrintQtaskinfo2",bean);
	}
	
	/*
	 * 存储在WINDOWS系统下
	 */
	private String StockWindows(String filepath,String picpath,Calendar ca,String filecode){
			//保存路径 一级根目录-print
			File path = new File(filepath+picpath);
			if(!path.exists()){
				path.mkdir();
			}
			//保存路径 二级目录-日期
			picpath = picpath + File.separator + ca.get(Calendar.YEAR)+"-"+(ca.get(Calendar.MONTH)+1)+"-"+ca.get(Calendar.DATE);
			path = new File(filepath+picpath);
			if(!path.exists()){
				path.mkdir();
			}
			
			//保存路径  三级目录-文件名 
			picpath = picpath + File.separator + filecode;
			path = new File(filepath+picpath);
			if(!path.exists()){
				path.mkdir();
			}
			logger.info("备份文档到服务器磁盘 结束");
			return filepath+picpath;
	}
	
	/*
	 * 存储在LINUX系统下
	 */
	private String StockLinux(String picpath,Calendar ca,String filecode){
			//保存路径 一级根目录-print
			File path = new File(picpath);
			if(!path.exists()){
				path.mkdir();
			}
			//保存路径 二级目录-日期
			picpath = picpath + File.separator + ca.get(Calendar.YEAR)+"-"+(ca.get(Calendar.MONTH)+1)+"-"+ca.get(Calendar.DATE);
			path = new File(picpath);
			if(!path.exists()){
				path.mkdir();
			}
			//保存路径  三级目录-文件名 
			picpath = picpath + File.separator + filecode;
			path = new File(picpath);
			if(!path.exists()){
				path.mkdir();
			}
			logger.info("备份文档到服务器磁盘 结束");
			return picpath;
	}
}
