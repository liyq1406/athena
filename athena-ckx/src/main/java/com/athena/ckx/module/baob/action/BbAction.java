package com.athena.ckx.module.baob.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.Anxbb;
import com.athena.ckx.module.baob.service.ShisywyhlConfigService;
import com.athena.ckx.module.baob.service.anxbbService;
import com.athena.ckx.module.cangk.service.KuwService;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

//V4_041
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class BbAction extends ActionSupport {
	@Inject
	private anxbbService anxService;
	@Inject
	private UserOperLog userOperLog;
	private static Logger logger =Logger.getLogger(BbAction.class);
	/**
	 * 获取当前用户信息
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	public String execute()
	{
		String usercenter = getLoginUser().getUsercenter();	
		setResult("usercenter", usercenter);	//登录人所在的用户中心
		return "select";
	}
	
	public String queryGongc(){
		String usercenter = getLoginUser().getUsercenter();
		try{
			setResult("result", anxService.queryGongc(usercenter));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "查询工厂");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "查询工厂", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String query(@Param Anxbb bean)
	{
		try{
			setResult("result", anxService.query(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表","按需要货令统计报表查询");
		}catch (Exception e){
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表","按需要货令统计报表查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	

	public String downloadbb() throws UnsupportedEncodingException
	{
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
        response.setContentType("application/vnd.ms-excel");    
        response.setHeader("Content-disposition", "attachment;filename=按需要货令报表.xls"); 
		ArrayList<String> data = new ArrayList<String>(3);
		data.add(this.getParam("img"));
		data.add(this.getParam("img1"));
		data.add(this.getParam("img2"));
		String s = this.getParam("factory");
		//byte[] fullByte1 = new String(this.getParam("factory").getBytes("ISO-8859-1"), "UTF-8").getBytes("GBK");  
		//String s = new String(fullByte1, "GBK");
		logger.info("factory ="+s);

		Map<String,String> message = new HashMap<String,String>();

		int maxcolumnKeys = 10;//要货令类型最多10中
		int maxDate = 15;//最多显示15天
		//报表个数最大值 
		int maxbbNum = 3;
		int currentNum = 0;
		logger.info("要货令类型最多"+maxcolumnKeys+"种"+"日期最多统计"+maxDate+"天");
		Anxbb bean = new Anxbb();
		String sheetName = null;
		bean.setFactory(s);
		List<String> yaohllx_list = new ArrayList<String>();;
		List<String> date_list = new ArrayList<String>();
		List<String> date_list1 = new ArrayList<String>();
		String title;
		
		HSSFWorkbook wb = null;
		String path = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
		String filename = generateFileName();
		String fileType = "xls";
        String excelPath = path+File.separator+filename+"."+fileType;
        File file = new File(excelPath);
        if (!file.exists()) 
        {
        	 wb = new HSSFWorkbook();
        }
        logger.info("生成excel路径"+excelPath);
        int [][] shul = null;
        //第三个报表数据不同
		for(currentNum = 0;currentNum < maxbbNum -1 ;currentNum++)
		{
			yaohllx_list.clear();
			date_list.clear();
			date_list1.clear();
			bean.setFlag(Integer.toString(currentNum));
			logger.info("bean.factory="+bean.getFactory()+"bean.flag="+bean.getFlag());
			yaohllx_list = anxService.queryYaohllx(bean);
			if(!yaohllx_list.isEmpty())
			{
				date_list = anxService.querytongjrqyear(bean);
				date_list1 = anxService.querytongjrq(bean);
			}else{
				continue;
			}
			logger.info("date.size = "+date_list.size());
			shul = new int[maxcolumnKeys][maxDate];
			int i=0;
			int j=0;

			for(i=0;i<yaohllx_list.size();i++)
			{
				for(j=0;j<date_list.size();j++)
				{
					bean.setYaohllx(yaohllx_list.get(i));
					bean.setTongjrq(date_list.get(j).substring(0,10));
					logger.info("bean.factory="+bean.getFactory()+"bean.flag="+bean.getFlag()+"bean.yaohllx="+bean.getYaohllx()+"bean.tongjrq="+bean.getTongjrq());
					shul[i][j] = (int) anxService.queryNum(bean).get(0).getShul();
					logger.info("bean.factory="+bean.getFactory()+"bean.flag="+bean.getFlag()+"bean.yaohllx="+bean.getYaohllx()+"bean.tongjrq="+bean.getTongjrq()+"shul[i][j]="+i+" "+j+" "+shul[i][j]);
				}
			}
			
			if(0 == currentNum)
			{
				sheetName = "外部要货令统计";
			}else if( 1 == currentNum){
				sheetName = "紧急外部要货令统计";
			}else{
				sheetName = "过点数量统计";
			}
			title =  s + sheetName;
			logger.info("title = "+title+"currentNum="+currentNum);
			writer(data.get(currentNum).replaceAll(" ", "+"),wb,sheetName,title,shul,date_list1,yaohllx_list);
		}
		//生成第三个报表
		logger.info("下载第三个报表开始--------------------------");
		bean.setFlag("2");
		bean.setFactory(s);
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("实际进总装数量",0);
		map.put("计划进总装数量",1);
		ArrayList<String> list = new ArrayList<String>();
		list.add("实际进总装数量");
		list.add("计划进总装数量");
		yaohllx_list.clear();
		date_list.clear();
		date_list1.clear();
		String tmp = null;
		int a=0;
		int b=0;
		int i=0;
		int j=0;
		List<String> shengcx = new ArrayList<String>();
		logger.info("bean.factory="+bean.getFactory()+"bean.flag="+bean.getFlag());
		shengcx = anxService.queryYaohllx(bean);
		logger.info("shengcx.size="+shengcx.size());
		if(!shengcx.isEmpty())
		{
			date_list = anxService.querytongjrqyear(bean);
			date_list1 = anxService.querytongjrq(bean);
			
			//这里的yaohllx_list格式如下：L1实际进总装数量 , L1计划进总装数量 ,L2实际进总装数量  ,L2计划进总装数量 
			for(a=0;a<shengcx.size();a++)
			{
				for(b=0;b<list.size();b++)
				{
					tmp = new String();
					tmp = shengcx.get(a) + list.get(b);
					yaohllx_list.add(tmp);
				}
			}
			
			shul = new int[maxcolumnKeys][maxDate];
			for(i=0;i<yaohllx_list.size();i++)
			{
				for(j=0;j < date_list.size();j++)
				{
					bean.setYaohllx(yaohllx_list.get(i).substring(0,5));
					bean.setFlag("2");
					bean.setTongjrq(date_list.get(j).substring(0,10));
					bean.setBiaos(map.get(yaohllx_list.get(i).substring(5)));
					logger.info("bean.factory="+bean.getFactory()+"bean.flag="+bean.getFlag()+"bean.yaohllx="+bean.getYaohllx()+"bean.tongjrq="+bean.getTongjrq()+"bean.biaos="+bean.getBiaos());
					shul[i][j] = (int) anxService.queryJihsl(bean).get(0).getShul();
					logger.info("bean.factory="+bean.getFactory()+"bean.flag="+bean.getFlag()+"bean.yaohllx="+bean.getYaohllx()+"bean.tongjrq="+bean.getTongjrq()+"shul[i][j]="+i+" "+j+" "+shul[i][j]);
				}
			}
			sheetName = "过点数量统计";
			title =  s + sheetName;
			logger.info("title = "+title+"currentNum="+currentNum);
			writer(data.get(2).replaceAll(" ", "+"),wb,sheetName,title,shul,date_list1,yaohllx_list);
			
		}else{
	        //创建文件流   
	        OutputStream stream;
	        
			try {
				stream = response.getOutputStream();
	            //OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				//InputStream fis = new BufferedInputStream(new FileInputStream(excelPath));
				//byte[] buffer = new byte[fis.available()];
				//fis.read(buffer);
				//fis.close();
		        wb.write(stream);
		        //stream.write(buffer);
		        stream.flush();
		        stream.close();
		        //response.setContentType("application/octet-stream");
	            // toClient.write(buffer);
	            //toClient.flush();
	            //toClient.close(); 
		        wb.close();
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
				message.put("result", e.getMessage());
			} catch (IOException e) {
				message.put("result", e.getMessage());
				logger.error(e.getMessage());
			} 
			message.put("result", "下载成功");
			this.setRequestAttribute("result", message);
			return "download";
		}
		bean = null;
		data = null;
		shul = null;
		date_list1 = null;
		yaohllx_list = null;
        //创建文件流   
        OutputStream stream;
		try {
			stream = response.getOutputStream();
	        wb.write(stream);  
	        stream.flush();
	        stream.close();  
	        wb.close();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			message.put("result", e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
			message.put("result", e.getMessage());
		} 
		wb = null;
		message.put("result", "下载成功");
		this.setRequestAttribute("result", message);
		return "download";
	}
	
	private String generateFileName() {
		// 获得当前时间
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		// 转换为字符串
		String formatDate = format.format(new Date());
		// 随机生成文件编号
		int random = new Random().nextInt(10000);
		return new StringBuffer().append(formatDate).append(random).toString();
	}
	
	private void writer(String imageURL,HSSFWorkbook wb,String sheetName,String title,int[][] shul, List<String> date_list1, List<String> yaohllx_list) 
	{
		HSSFSheet sheet = null;
        sheet = wb.createSheet(sheetName);

        short Imagecolumn = 16;
        int line = 20;
        if(!imageURL.isEmpty())
        {
    	    String[] url = imageURL.split(",");
    	    String u = url[1];
    	    // Base64解码
    	    byte[] b;
    		try {
    			b = new sun.misc.BASE64Decoder().decodeBuffer(u);
    		    for(int i=0;i<b.length;++i)  
    		    {  
    		            if(b[i]<0)  
    		            {
    		               b[i]+=256;  
    		            }
    		    }
    	        //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
    	        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();     
    	        //anchor主要用于设置图片的属性  
    	        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,(short) 0, 0, Imagecolumn, line);     
    	        anchor.setAnchorType(3);     
    	        //插入图片    
    	        patriarch.createPicture(anchor, wb.addPicture(b, HSSFWorkbook.PICTURE_TYPE_PNG)); 
    		} catch (IOException e) {
    			//13161
    			logger.error(e.getMessage());
				throw new ServiceException(e.getMessage());
    		}
        }
        //当前表格行数
        Row row = null; 
        Cell cell = null;
        //row.setHeight((short) 540); 
        CellStyle style = wb.createCellStyle(); // 样式对象      
        
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index); 
        style.setWrapText(true);
        Font font = wb.createFont();  
        font.setFontName("微软雅黑");  
        font.setFontHeight((short) 180);  
        style.setFont(font);  
        line++;
        row = sheet.createRow(line);    //创建第一行   
        cell = row.createCell(0);  
        cell.setCellValue("类型");  
        cell.setCellStyle(style); // 样式，居中
        //设置第一行日期显示
        int i=0;
        int j=0;
        for(i = 1;i <= date_list1.size();i++){  
            cell = row.createCell(i);  
            cell.setCellValue(date_list1.get(i-1));  
            cell.setCellStyle(style); // 样式，居中
            sheet.setColumnWidth(i, 20 * 256); 
        }
        row.setHeight((short) 540); 
        line++;
        //设置数据显示
        i=0;
        j=0;
        for(i=0;i<yaohllx_list.size();i++)
        {
            row = sheet.createRow(line+i);    //创建第二三四行   
            cell = row.createCell(0);  
            cell.setCellValue(yaohllx_list.get(i));  
            cell.setCellStyle(style); // 样式，居中
            for(j=0;j<date_list1.size();j++)
            {
                cell = row.createCell(j+1);  
                cell.setCellValue(shul[i][j]);  
                cell.setCellStyle(style); // 样式，居中
            }
        }
        sheet.autoSizeColumn((short)0);
        sheet.autoSizeColumn((short)1);
        sheet.autoSizeColumn((short)2);
        sheet.autoSizeColumn((short)3);
        sheet.autoSizeColumn((short)4);
        sheet.autoSizeColumn((short)5);
        sheet.autoSizeColumn((short)6);
        sheet.autoSizeColumn((short)7);
        sheet.autoSizeColumn((short)8);
        sheet.autoSizeColumn((short)9);
        sheet.autoSizeColumn((short)10);
        sheet.autoSizeColumn((short)11);
        sheet.autoSizeColumn((short)12);
        sheet.autoSizeColumn((short)13);
        sheet.autoSizeColumn((short)14);
        sheet.autoSizeColumn((short)15);
        sheet.autoSizeColumn((short)16);
        
	}
	public String show() throws UnsupportedEncodingException
	{
		int i=0;
		int j=0;
		int a=0;//3个报表
		
		int maxcolumnKeys = 10;//要货令类型最多10中
		int maxDate = 15;//最多显示15天
		Anxbb bean = new Anxbb();
		
		//byte[] fullByte1 = new String(this.getParam("gongc").getBytes("ISO-8859-1"), "UTF-8").getBytes("GBK");  
		//String factory = new String(fullByte1, "GBK");
		//logger.info("factory ="+factory);
		
		//String factory = this.getParam("gongc");
		String factory= URLDecoder.decode(this.getParam("gongc"));
		//String factory = URLEncoder.encode(new String(this.getParam("gongc")),"utf-8"); 
		this.setResult("factory",  factory);
		logger.info("生成"+factory+"工厂的报表开始");
		logger.info("报表显示最长日期"+maxDate+"天"+"要货令或者产线最多"+maxcolumnKeys+"个");
		Map<String,String> lx = new HashMap<String,String>();
		lx.put("C1", "AX");
		lx.put("CD", "AX");
		lx.put("CV", "AX");
		lx.put("PP", "AX");
		lx.put("PS", "AX");
		lx.put("PJ", "AX");
		lx.put("VJ", "AX");

		bean.setFactory(factory);
		List<String> yaohllx_list;
		List<List<String>> yaohllx = new ArrayList<List<String>>();
		List<String> date_list;
		List<String> date_list1;
		List<List<String>> date = new ArrayList<List<String>>();
		List<List<String>> date1 = new ArrayList<List<String>>();
		for(a=0;a<2;a++)
		{
			yaohllx_list = new ArrayList<String>();
			bean.setFlag(Integer.toString(a));
			yaohllx_list = anxService.queryYaohllx(bean);
			logger.info("bean.flag = "+a+"factory="+factory);
			logger.info("第"+a+"个报表"+"要货令类型数量为"+yaohllx_list.size());
			if(!yaohllx_list.isEmpty())
			{
				yaohllx.add(yaohllx_list);
				date_list = new ArrayList<String>();
				date_list1 = new ArrayList<String>();
				bean.setFlag(Integer.toString(a));
				date_list = anxService.querytongjrqyear(bean);
				date_list1 = anxService.querytongjrq(bean);
				date.add(date_list);
				date1.add(date_list1);
			}else{
				logger.info("第"+a+"个报表"+"要货令类型数量为"+yaohllx_list.size()+"取下一个报表数据");
				continue;
			}
		}
		JSONArray obj = JSONArray.fromObject(yaohllx);
		setRequestAttribute("yaohllx", obj);

		obj = JSONArray.fromObject(date1);
		setRequestAttribute("xAis", obj);
		
		int [][][] shul = new int[2][maxcolumnKeys][maxDate];
		int [][] sum = new int[2][maxDate];

		for(int m=0;m < yaohllx.size();m++)
		{
			bean.setFlag(Integer.toString(m));
			for(i=0;i<yaohllx.get(m).size();i++)
			{
				for(j=0;j<date.get(m).size();j++)
				{
					bean.setYaohllx(yaohllx.get(m).get(i));
					bean.setTongjrq(date.get(m).get(j).substring(0,10));
					logger.info("值列表 m="+m+"i="+i+"j="+j+"shul[m][i][j]="+shul[m][i][j]);
					shul[m][i][j] = (int) anxService.queryNum(bean).get(0).getShul();
					logger.info("值列表 m="+m+"i="+i+"j="+j+"shul[m][i][j]="+shul[m][i][j]);
					sum[m][j] += shul[m][i][j]; 
					logger.info("值列表  sum[i] = "+sum[m][i]);
				}
			}	
		}
		logger.info("值列表 "+"i="+i+"j="+j+"yaohllx.size() = "+yaohllx.size());
		Map<String,Object> series ;
		List<Map<String,Object>> serie = new ArrayList<Map<String,Object>>();
		List<List<Map<String,Object>>> data = new ArrayList<List<Map<String,Object>>>();
		String stack = new String();
		for(int c=0;c<yaohllx.size();c++)
		{
			serie = new ArrayList<Map<String,Object>>();
			for(int k=0;k<yaohllx.get(c).size();k++)
			{
				series = new HashMap<String,Object>();
				//label = new HashMap<String,Object>();
				//ietmStyle = new HashMap<String,Object>();
				stack = yaohllx.get(c).get(k);
				series.put("name",stack);
				series.put("type", "bar");
				series.put("stack", lx.get(stack));
				series.put("data", shul[c][k]);
				series.put("barMaxWidth","20%");
				/*label.put("show", true);
				label.put("position", "insideTop");
				label.put("formatter", "{c}");
				ietmStyle.put("normal", label);
				series.put("label", ietmStyle);*/
				serie.add(series);
				logger.info("当前data="+series.get("data")+"stack="+series.get("stack"));
			}
			//增加一条折线图
			series = new HashMap<String,Object>();
			series.put("name","合计");
			series.put("type", "line");
			series.put("data", sum[c]);
			serie.add(series);
			data.add(serie);
			logger.info("所有data.size="+data.size());
		}
		obj = JSONArray.fromObject(data);
		setRequestAttribute("series", obj);
		logger.info("前2个报表生成完毕，-------------------开始生成第三个报表");
		if(!GetZZGC(factory))
		{
			setRequestAttribute("result", "生成报表失败");
		}
		return "select";
	}
	
	public boolean GetZZGC(String s)
	{
		Anxbb bean = new Anxbb();
		bean.setFactory(s);
		bean.setFlag("2");
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("实际进总装数量",0);
		map.put("计划进总装数量",1);
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("实际进总装数量");
		list.add("计划进总装数量");
		logger.info("bean.factory="+bean.getFactory()+"bean.flag="+bean.getFlag());
		List<String> chanx = new ArrayList<String>();
		List<String> date_list;
		List<String> date_list1=null;
		chanx = anxService.queryYaohllx(bean);
		String tmp ;
		List<String> yaohllx_list = new ArrayList<String>();
		
		Map<String,Object> series ;
		List<Map<String,Object>> serie = new ArrayList<Map<String,Object>>();
		
		//产线不空，有数据
		int a=0;
		int b=0;
		JSONArray obj = null;
		logger.info("chanx.size = "+chanx.size());
		if(!chanx.isEmpty())
		{
			date_list = new ArrayList<String>();
			date_list = anxService.querytongjrqyear(bean);
			date_list1 = anxService.querytongjrq(bean);
			
			obj = JSONArray.fromObject(date_list1);
			setRequestAttribute("aXis", obj);
			//这里的yaohllx_list格式如下：L1实际进总装数量 , L1计划进总装数量 ,L2实际进总装数量  ,L2计划进总装数量 
			for(a=0;a<chanx.size();a++)
			{
				for(b=0;b<list.size();b++)
				{
					tmp = new String();
					tmp = chanx.get(a) + list.get(b);
					yaohllx_list.add(tmp);
				}
			}
		}else{
			obj = JSONArray.fromObject(date_list1);
			setRequestAttribute("aXis", obj);
			obj = JSONArray.fromObject(yaohllx_list);
			setRequestAttribute("chanx", obj);
			obj = JSONArray.fromObject(serie);
			setRequestAttribute("gcsl", obj);
			logger.info("chanx.size = "+chanx.size()+"无数据，跳出");
			return true;
		}
		obj = JSONArray.fromObject(yaohllx_list);
		setRequestAttribute("chanx", obj);
		int chanxNum = chanx.size();
		int shul[][] = new int[chanxNum*2][date_list.size()];
		int i=0;
		int j=0;
		String shengcx = new String();
		String lx = new String();
		//这里的yaohllx_list格式如下：L1实际进总装数量 , L1计划进总装数量 ,L2实际进总装数量  ,L2计划进总装数量 
		for(i=0;i < yaohllx_list.size() ;i++)
		{
			for(j=0;j<date_list.size();j++)
			{
				shengcx = yaohllx_list.get(i).substring(0,5);
				lx = yaohllx_list.get(i).substring(5);
				bean.setYaohllx(shengcx);
				bean.setFlag("2");
				bean.setTongjrq(date_list.get(j).substring(0,10));
				bean.setBiaos(map.get(lx));
				logger.info("chanx="+bean.getYaohllx()+"flag="+bean.getFlag()+"tongjrq="+bean.getTongjrq()+"biaos="+bean.getBiaos());
				shul[i][j] = (int) anxService.queryJihsl(bean).get(0).getShul();
				logger.info("值列表 "+"i="+i+"j="+j+"shul[m][i][j]="+shul[i][j]);
			}
		}
		

		String stack = new String();

		serie = new ArrayList<Map<String,Object>>();
		for(int k=0;k<yaohllx_list.size();k++)
		{
			series = new HashMap<String,Object>();
			stack = yaohllx_list.get(k);
			series.put("name",stack);
			series.put("type", "bar");
			series.put("stack", stack.substring(0,5));
			series.put("data", shul[k]);
			series.put("barMaxWidth","46");
			serie.add(series);		
			logger.info("当前data="+series.get("data")+"stack="+series.get("stack"));
		}

		obj = JSONArray.fromObject(serie);
		setRequestAttribute("gcsl", obj);
		
		return true;
	}
	
}



