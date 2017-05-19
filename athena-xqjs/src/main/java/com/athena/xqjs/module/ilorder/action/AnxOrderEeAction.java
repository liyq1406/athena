package com.athena.xqjs.module.ilorder.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.DateCell;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.cxf.frontend.ClientProxyFactoryBean;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.utils.LoaderProperties;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiaohcyssk;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.entity.userCenter.UserCenter;
import com.athena.xqjs.module.anxorder.service.AnxMaoxqService;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.ilorder.service.AnxlsOrderWebEeService;
import com.athena.xqjs.module.ilorder.service.AxOrderService;
import com.athena.xqjs.util.AXlsddWebservice;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：AnxOrderEeAction
 * <p>
 * 类描述： 按需订单
 * </p>
 * 创建人：CSY
 * <p>
 * 创建时间：2016-11-09
 * </p>
 * 
 * @version 4.0
 * 
 */
@Component
public class AnxOrderEeAction extends BaseWtcAction {

	// 日志
	private final Log log = org.apache.commons.logging.LogFactory.getLog(AnxOrderEeAction.class);

	@Inject
	private AxOrderService anxOrderService;			//按需临时订单
	@Inject
	private AnxMaoxqService anxMaoxqService;
	@Inject
	private AnxlsOrderWebEeService anxlsOrderService;//临时订单
	
	//配置文件路径
	private final String fileName="urlPath.properties"; 
	
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;
	
	private Map<String, UserCenter> usercenters = new HashMap<String, UserCenter>();//用户中心
	private Map<String, Lingj> lingjs = new HashMap<String, Lingj>();				//零件
	private Map<String, Gongys> gongyss = new HashMap<String, Gongys>();			//供应商
	private Map<String, LingjGongys> lingjgyss = new HashMap<String, LingjGongys>();//零件供应商
	private Map<String, Map<String, Wullj>> wulljs = new HashMap<String, Map<String, Wullj>>();	//物流路径
	private Map<String, Lingjck> lingjcks = new HashMap<String, Lingjck>();			//零件仓库
	private Map<String, Lingjxhd> lingjxhds = new HashMap<String, Lingjxhd>();		//零件消耗点
	private Map<String, Xiehzt> xiehzts = new HashMap<String, Xiehzt>();			//卸货站台
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}
	
	/**
	 * 模板下载
	 */
	public String expMubanAX() {

		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		Map<String, String> message = new HashMap<String, String>();
		boolean flag = exp(reqResponse);
		if (!flag) {
			String result = "导出文件出错！";
			message.put("message", result);
			setResult("result", message);
		}
		return AJAX;
	}
	
	/**
	 * 模板生成
	 * @param response
	 * @return
	 */
	private boolean exp(HttpServletResponse response){
		final String USERCENTER = "USERCENTER";
		final String GONGYSBH = "GONGYSBH";
		final String LINGJBH = "LINGJBH";
		final String GONGHLX = "GONGHLX";
		final String CANGKBH = "KEH";
		final String LINGJSL = "LINGJSL";
		final String YAOHSJ = "YAOHSJ";
		final String SHIFJD = "SHIFJD";
		final String SHIFFSGHS = "SHIFFSGHS";
		final String FAYZQ = "FAYZQ";
		WritableFont fontRed = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.RED);  
        WritableCellFormat cellFormatRed = new WritableCellFormat(fontRed); 
      
		try {
			cellFormatRed.setWrap(true);  
		    cellFormatRed.setAlignment(Alignment.CENTRE);  
	        cellFormatRed.setVerticalAlignment(VerticalAlignment.CENTRE);  
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			//mantis:0013187 by CSY 20170215
			String fileName = "按需临时订单导入模板";  
			//fileName = new String(fileName.getBytes(),"iso-8859-1"); 
			response.setHeader("Content-Disposition", new String(("attachment; filename=" + fileName).getBytes("gb2312"), "ISO-8859-1")+".xls");// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型

			WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
			String tmptitle = "按需临时订单导入"; // 标题
			WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称
			wsheet.getSettings().setDefaultColumnWidth(20);
			// 设置excel标题
			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			WritableCellFormat wcfFC = new WritableCellFormat(wfont);
			// wcfFC.setBackground(Colour.AQUA);
			wsheet.addCell(new Label(4, 0, tmptitle, wcfFC));
			wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			wcfFC = new WritableCellFormat(wfont);
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put(USERCENTER, "用户中心\n(不分大小写)");
			map.put(GONGYSBH, "供应商/订货仓库\n(不分大小写)");
			map.put(LINGJBH, "零件编号\n(不分大小写)");
			map.put(GONGHLX, "供货类型\n(C1,CV,CD,M1,MV,MD,不分大小写)");
			map.put(CANGKBH, "客户/仓库\n(不分大小写)");
			map.put(LINGJSL, "零件数量\n(整数最大10位,小数最大3位)");
			map.put(YAOHSJ, "要货时间 \n(年-月-日 时:分)");
			map.put(SHIFJD, "是否既定 \n(否,是)");
			map.put(SHIFFSGHS, "是否发送供应商\n(否,是)");
			map.put(FAYZQ, "发运区间");
			
			Map<String, String> data = new LinkedHashMap<String, String>();
			data.put(USERCENTER, "UL");
			data.put(GONGYSBH, "XXXXXXXXXX");
			data.put(LINGJBH, "XXXXXXXXXX");
			data.put(GONGHLX, "C1");
			data.put(CANGKBH, "XXX");
			data.put(LINGJSL, "0");
			data.put(YAOHSJ, "2017-01-01 12:18");
			data.put(SHIFJD, "是");
			data.put(SHIFFSGHS, "是");
			data.put(FAYZQ, "201611");
			
			jxl.write.DateFormat df=new jxl.write.DateFormat("yyyy-MM-dd HH:mm");  
	        jxl.write.WritableCellFormat wcfDF=new jxl.write.WritableCellFormat(df);  
			CellView cellView = new CellView();
			cellView.setAutosize(true);
			cellView.setFormat(wcfDF);
			wsheet.setColumnView(6, cellView);
			
			// 开始生成主体内容
			Set<Map.Entry<String, String>> set = map.entrySet();
			int temp = 0;
			for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				wsheet.addCell(new Label(temp++, 1, entry.getValue(),cellFormatRed));
			}
			temp = 0;// 向excel单元格里写数据
			int i = 0;
			 
			for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				Object obj = data.get(entry.getKey());
				String content = obj == null ? "" : String.valueOf(obj);
				if (temp != 6) {
					wsheet.addCell(new Label(temp++, i + 2, content));
				}else{
					DateTime dateCell = new DateTime(temp++, i + 2, new Date(), wcfDF );
					wsheet.addCell(dateCell);
				}
			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 导入临时订单
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public String impAXorderTemp() throws IOException {
		HttpServletRequest req = ActionContext.getActionContext().getRequest();
		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		PrintWriter out = null;
		out = reqResponse.getWriter();
		reqResponse.setContentType("text/html");
		reqResponse.setCharacterEncoding("UTF-8");
		StringBuffer sbResult =  new StringBuffer();
		sbResult.append("<script>parent.callback(\"");
		Map<String, String> map = getParams();
		map.put("jsrq", CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD));// 计算日期
		map.put("username", AuthorityUtils.getSecurityUser().getUsername());// 设置用户姓名
		map.put("jiscldm", Const.JISMK_IL_TEMP_CD);// 计算处理代码：国产件临时订单（30）
		if (jiscclssz.checkState(map)) {
			sbResult.append("有导入正在进行,请稍后再计算!");
			sbResult.append("\")</script>");
			log.info(sbResult.toString());
			if (out != null)
			{
				out.print(sbResult.toString());
				out.flush();
				out.close();
			}
		}else{
			jiscclssz.updateState(map, Const.JSZT_EXECUTING);
			String fullFilePath = "";
			String saveLj = System.getProperty("java.io.tmpdir");
			RequestContext requestContext = new ServletRequestContext(req);
			if (FileUpload.isMultipartContent(requestContext)) {
				try {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					upload.setSizeMax(4000000);
					List<FileItem> fileItems;
					
					fileItems = upload.parseRequest(req);
					
					for (FileItem item : fileItems) {
						if (item.isFormField()) {
							String value = item.getString();
							log.info(item.getFieldName() + ":" + value);
						} else {
							String fileName = item.getName();
							if (fileName != null) {
								if (fileName.lastIndexOf(File.separator) != -1) {
									fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1, fileName.length());
								}
								fullFilePath = saveLj + File.separator + fileName;
								File fullFile = new File(saveLj);
								if (!fullFile.exists()) {
									fullFile.mkdirs();
								}
								File fileOnServer = new File(fullFilePath);
								
								item.write(fileOnServer);
								
							}
						}
					}
				} catch (Exception e) {
					log.info(e.getMessage());
					jiscclssz.updateState(map, Const.JSZT_SURE);
				}
			}try {
				log.info(fullFilePath);
				//模板文件的获取和验证
				Object result = imp(fullFilePath);
				if(null == result || result instanceof java.lang.String){
					sbResult.append(result);
				}else{
					boolean flag = true;
					//模板数据转换为订单明细集合
					List<Dingdmx> dingdmxs = (List<Dingdmx>) result;
					if(null != dingdmxs && dingdmxs.size() > 0){
						//订单明细集合插入数据库
						flag = anxOrderService.impTempDingd(dingdmxs);
						if(flag){
							sbResult.append("导入文件成功!");
						}else{
							sbResult.append("导入文件失败!");
						}
					}
				}
				sbResult.append("\")</script>");
				log.info(sbResult.toString());
				out.print(sbResult.toString());
				out.flush();
			} catch (Exception e) {
				log.error(e.getMessage());
				out.println("<script>parent.callback('导入文件出错！ ')</script>");
				out.flush();
			} finally {
				jiscclssz.updateState(map, Const.JSZT_SURE);
				if (out != null){
					out.close();
				}
			}
		}
		return AJAX;
	}

	/**
	 * 读取导入文件及验证
	 * @param fullFilePath
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 * @throws ParseException 
	 */
	private Object imp(String fullFilePath) throws BiffException, IOException, ParseException{
		final String ERROR_ROW_MAX = "导入文件的数据大于500条!";
		final String ERROR_ROW_NULL = "该文件为空文件,无导入数据!";
		final int ROW_MAX = 503;
		// 读入文件流
		InputStream is = new FileInputStream(new File(fullFilePath));
		// 取得工作薄
		jxl.Workbook wb = Workbook.getWorkbook(is);
		// 取得工作表
		jxl.Sheet sheet = wb.getSheet(0);
		// 行数、列数
		int rows = sheet.getRows();
		if(rows > ROW_MAX){
			return ERROR_ROW_MAX;
		}
		int columns = sheet.getColumns();
		List<Dingdmx> ddmxsAll = new ArrayList<Dingdmx>();	//存放所有订单明细数据，还未检验
		Dingdmx data = null;
		String username = getUserInfo().getUsername();
		String time = CommonFun.getJavaTime();
		String error = null;
		StringBuffer lingjParam = new StringBuffer("");	//组装零件集合
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT")); 
		//导入数据,校验
		for (int i = 2; i < rows; i++){
			data = new Dingdmx();
			data.setCreator(username);
			data.setEditor(username);
			data.setCreate_time(time);
			data.setEdit_time(time);
			data.setZhuangt(Const.DINGD_STATUS_ZZZ);
			data.setActive(Const.ACTIVE_1);
			for (int j = 0; j < columns; j++){
				switch (j) {
					case 0:
						// 读取用户中心
						data.setUsercenter(sheet.getCell(j, i).getContents().trim().toUpperCase());
						break;
					case 1:
						//C为供应商编号，M为仓库编号
						data.setGongysdm(sheet.getCell(j, i).getContents().trim().toUpperCase());
						break;
					case 2:
						//零件编号
						data.setLingjbh(sheet.getCell(j, i).getContents().trim().toUpperCase());
						break;
					case 3:
						//供货模式
						data.setGonghlx(sheet.getCell(j, i).getContents().trim().toUpperCase());
						break;
					case 4:
						//客户，D为消耗点，1和V为线边仓库
						data.setKeh(sheet.getCell(j, i).getContents().trim().toUpperCase());
						break;
					case 5:
						//零件数量
						data.setShulStr(sheet.getCell(j, i).getContents().trim());
						break;
					case 6:
						//要货日期
						String jiaofrq = "";
						try {
							Cell jiaofrqCell = sheet.getCell(j, i);
							if (jiaofrqCell.getContents() == null || jiaofrqCell.getContents().equals("")) {
								jiaofrq = "error";
								
							}else {
								if(jiaofrqCell.getType()==CellType.DATE){ 
									DateCell dc = (DateCell)jiaofrqCell;         
									jiaofrq = sdf.format(dc.getDate()); 
								}else {
									jiaofrq = "error";
								}
							}
						} catch (Exception e) {
							jiaofrq = "error";
						}
						data.setJiaofrq(jiaofrq);
						break;
					case 7:
						//是否既定
						data.setLeixStr(sheet.getCell(j, i).getContents().trim());
						break;
					case 8:
						//是否发货供应商
						data.setFasgysStr(sheet.getCell(j, i).getContents().trim());
						break;
					case 9:
						//发运周期
						data.setFahzq(sheet.getCell(j, i).getContents().trim());
						break;
				}
			}
			if (data.getLingjbh() != null && !data.getLingjbh().equals("")) {
				if (i != 2 && i < rows) {
					lingjParam.append(",");
				}
				lingjParam.append("'").append(data.getLingjbh()).append("'");
			}
			ddmxsAll.add(data);
			
		}
		usercenters = anxOrderService.getUsercenters();					//用户中心
		lingjs = anxOrderService.getLingj(lingjParam.toString());		//零件
		gongyss = anxOrderService.getGongys();							//供应商
		lingjgyss = anxOrderService.getLingjgys(lingjParam.toString());	//零件供应商
		wulljs = anxOrderService.getWullj(lingjParam.toString());		//物流路径
		lingjcks = anxOrderService.getLingjck(lingjParam.toString());	//零件仓库
		lingjxhds = anxOrderService.getLingjxhd(lingjParam.toString());	//零件消耗点
		xiehzts = anxOrderService.getXiehzt();							//卸货站台
		
		///////////////////////导入数据效验///////////
		error = check(ddmxsAll, usercenters, lingjs, gongyss, lingjgyss, wulljs, lingjcks, lingjxhds, xiehzts);
		///////////////////////导入数据效验///////////
		
		if(StringUtils.isNotBlank(error)){
			return error;
		}
		
		//根据用户中心分离集合数据
		List<Dingdmx> listUL = new ArrayList<Dingdmx>();	//UL
		List<Dingdmx> listUW = new ArrayList<Dingdmx>();	//UW
		List<Dingdmx> listVD = new ArrayList<Dingdmx>();	//VD
		
		for (int k = 0; k < ddmxsAll.size(); k++) {
			if (ddmxsAll.get(k).getUsercenter().equals("UL") || ddmxsAll.get(k).getUsercenter().equals("UX")) {
				listUL.add(ddmxsAll.get(k));
			}else if (ddmxsAll.get(k).getUsercenter().equals("UW")) {
				listUW.add(ddmxsAll.get(k));
			}else if (ddmxsAll.get(k).getUsercenter().equals("VD")) {
				listVD.add(ddmxsAll.get(k));
			}
		}
		
		//UL/UX执行层验证
		if (listUL != null && listUL.size() > 0) {
			String urlPath = LoaderProperties.getPropertiesMap(fileName).get("urlPathUL");
			ClientProxyFactoryBean factoryvd = new ClientProxyFactoryBean();
			factoryvd.setServiceClass(AXlsddWebservice.class);
			factoryvd.setAddress(urlPath+"axLSDDWebservice");
			AXlsddWebservice client = (AXlsddWebservice) factoryvd.create();
			String str = client.checkAXlldd(listUL);
			if (!str.equals("success")) {
				if (str.equals("fail")) {
					return "执行层验证未通过";
				}
				return "用户中心UL 零件号："+str+"未能满足执行层拆分要货令条件,该零件部分信息可能于当天维护,请于明天操作!";
			}
		}
		
		//UW执行层验证
		if (listUW != null && listUW.size() > 0) {
			String urlPath = LoaderProperties.getPropertiesMap(fileName).get("urlPathUW");
			ClientProxyFactoryBean factoryvd = new ClientProxyFactoryBean();
			factoryvd.setServiceClass(AXlsddWebservice.class);
			factoryvd.setAddress(urlPath+"axLSDDWebservice");
			AXlsddWebservice client = (AXlsddWebservice) factoryvd.create();
			String str = client.checkAXlldd(listUW);
			if (!str.equals("success")) {
				if (str.equals("fail")) {
					return "执行层验证未通过";
				}
				return "用户中心UW 零件号："+str+"未能满足执行层拆分要货令条件,该零件部分信息可能于当天维护,请于明天操作!";
			}
		}
		
		//VD执行层验证
		if (listVD != null && listVD.size() > 0) {
			String urlPath = LoaderProperties.getPropertiesMap(fileName).get("urlPathVD");
			ClientProxyFactoryBean factoryvd = new ClientProxyFactoryBean();
			factoryvd.setServiceClass(AXlsddWebservice.class);
			factoryvd.setAddress(urlPath+"axLSDDWebservice");
			AXlsddWebservice client = (AXlsddWebservice) factoryvd.create();
			String str = client.checkAXlldd(listVD);
			if (!str.equals("success")) {
				if (str.equals("fail")) {
					return "执行层验证未通过";
				}
				return "用户中心VD 零件号："+str+"未能满足执行层拆分要货令条件,该零件部分信息可能于当天维护,请于明天操作!";
			}
		}
		
		return ddmxsAll.size() == 0 ? ERROR_ROW_NULL : ddmxsAll;
	}

	/**
	 * 验证
	 * @param dingdmxs
	 * @param usercenterMap
	 * @param lingjMap
	 * @param gongysMap
	 * @param lingjGongysMap
	 * @param wulljMap
	 * @param lingjckMap
	 * @param lingjxhdMap
	 * @param xiehztMap
	 * @return
	 * @throws ParseException 
	 */
	private String check(List<Dingdmx> dingdmxs,Map<String,UserCenter> usercenterMap,Map<String,Lingj> lingjMap,Map<String,Gongys> gongysMap,Map<String,LingjGongys> lingjGongysMap,Map<String,Map<String, Wullj>> wulljMap, Map<String, Lingjck> lingjckMap, Map<String, Lingjxhd> lingjxhdMap, Map<String, Xiehzt> xiehztMap) throws ParseException{
		StringBuffer sb = new StringBuffer("第");
		for (int j = 0; j < dingdmxs.size(); j++) {
			sb.append(j + 3).append("行:");
			Dingdmx data = dingdmxs.get(j);
			if(StringUtils.isBlank(data.getUsercenter()) 
					|| StringUtils.isBlank(data.getGongysdm()) 
					|| StringUtils.isBlank(data.getKeh()) 
					|| StringUtils.isBlank(data.getLingjbh()) 
					|| StringUtils.isBlank(data.getGonghlx()) 
					|| StringUtils.isBlank(data.getShulStr()) 
					|| StringUtils.isBlank(data.getJiaofrq()) 
					|| StringUtils.isBlank(data.getLeixStr()) 
					|| StringUtils.isBlank(data.getFasgysStr()) 
			){
				sb.append("用户中心,供应商/仓库编号,零件编号,供货类型,客户/仓库,零件数量,要货时间 ,是否既定 ,是否发送供应商为必填项!");
				return sb.toString();
			}
			if(!usercenterMap.containsKey(data.getUsercenter())){
				sb.append("用户中心不存在或已失效!");
				return sb.toString();
			}
			if(!(data.getGonghlx().equalsIgnoreCase("C1") || data.getGonghlx().equalsIgnoreCase("CV") || data.getGonghlx().equalsIgnoreCase("CD")|| data.getGonghlx().equalsIgnoreCase("M1")|| data.getGonghlx().equalsIgnoreCase("MV")|| data.getGonghlx().equalsIgnoreCase("MD"))){
				sb.append("供货类型应为(C1,CV,CD,M1,MV,MD)!");
				return sb.toString();
			}
			try{
				double shul = Double.valueOf(data.getShulStr());
				if(!data.getShulStr().matches("^[0-9]{1,10}(?:\\.[0-9]{1,3})?$|^0\\.[0-9]{1,3}$")){
					sb.append("零件数量的取值范围为(0  ~~ 9999999999.999)!");
					return sb.toString();
				}
				data.setShul(new BigDecimal(shul));
				data.setJissl(data.getShul());
			}catch (Exception e){
				sb.append("零件数量的取值范围为(0  ~~ 9999999999.999)!");
				return sb.toString();
			}
			if (data.getJiaofrq().equals("error")){
				sb.append("要货时间格式不正确(yyyy-MM-dd HH:mm)!");
				return sb.toString();
			}else {
				if((data.getJiaofrq()).length()!=16){
					sb.append("要货时间格式不正确(yyyy-MM-dd HH:mm)!");
					return sb.toString();
				}
			}
			if (data.getJiaofrq().substring(0, 10).compareTo(data.getCreate_time().substring(0, 10)) < 0) {
				sb.append("要货时间不能小于当前时间!");
				return sb.toString();
			}
			if (data.getFahzq() == null || data.getFahzq().equals("")) {
				sb.append("发运区间不能为空!");
				return sb.toString();
			}
			if(data.getLeixStr().equalsIgnoreCase("是")){
				data.setLeix(Const.SHIFOUSHIJIDING);
			}else if(data.getLeixStr().equalsIgnoreCase("否"))
			{
				data.setLeix(Const.SHIFOUSHIYUGAO);
			}else{
				sb.append("是否既定的赋值规则为(否,是)!");
				return sb.toString();
			}
			if(data.getFasgysStr().equalsIgnoreCase("是")){
				data.setFasgysStr(Const.ACTIVE_1);
			}else if(data.getFasgysStr().equalsIgnoreCase("否")){
				data.setFasgysStr(Const.ACTIVE_0);
			}else{
				sb.append("是否发送供应商的赋值规则为(否,是)!");
				return sb.toString();
			}
			if(!lingjMap.containsKey(data.getUsercenter() + data.getLingjbh())){
				sb.append("零件编号不存在或已失效!");
				return sb.toString();
			}else{
				Lingj lingj = lingjMap.get(data.getUsercenter() + data.getLingjbh());
				data.setDanw(lingj.getDanw());
				data.setJihyz(lingj.getJihy());
				data.setDinghcj(lingj.getDinghcj());
				data.setLingjmc(lingj.getZhongwmc());
			}
			//如果是C，需要验证供应商和零件供应商
			if (data.getGonghlx()!= null && !data.getGonghlx().equals("") && data.getGonghlx().startsWith("C")) {
				if(!gongysMap.containsKey(data.getUsercenter() + data.getGongysdm())){
					sb.append("供应商编号不存在或已失效!");
					return sb.toString();
				}else{
					Gongys gongys = gongysMap.get(data.getUsercenter() + data.getGongysdm());
					data.setGongyslx(gongys.getLeix());
					data.setGongsmc(gongys.getGongsmc());
				}
				if(!lingjGongysMap.containsKey(data.getUsercenter() + data.getGongysdm() + data.getLingjbh())){
					sb.append("没有找到零件与供应商的关联关系!");
					return sb.toString();
				}else{
					LingjGongys lingjGongys = lingjGongysMap.get(data.getUsercenter() + data.getGongysdm() + data.getLingjbh());
					data.setGongyfe(lingjGongys.getGongyfe());
					if(StringUtils.isBlank(lingjGongys.getUabzlx())
							|| StringUtils.isBlank(lingjGongys.getUcbzlx())
							|| null == lingjGongys.getUaucgs()
							|| null == lingjGongys.getUcrl()
					){
						sb.append("导入数据对应的包装信息不完整(UA包装类型,UC包装类型,UC容量,UAUC个数)!");
						return sb.toString();
					}
					data.setUabzlx(lingjGongys.getUabzlx());
					data.setUabzucsl(lingjGongys.getUaucgs());
					data.setUabzuclx(lingjGongys.getUcbzlx());
					data.setUabzucrl(lingjGongys.getUcrl());
					data.setUabzrl(lingjGongys.getUcrl().multiply(lingjGongys.getUaucgs()));
				}
			}else if (data.getGonghlx()!= null && !data.getGonghlx().equals("") && data.getGonghlx().startsWith("M")) {
				//如果是M，查询零件仓库
				if (!lingjckMap.containsKey(data.getUsercenter() + data.getLingjbh() + data.getGongysdm())) {
					sb.append("订货仓库不存在或已失效!");
					return sb.toString();
				}else{
					Lingjck ck = lingjckMap.get(data.getUsercenter() + data.getLingjbh() + data.getGongysdm());
					if (data.getGonghlx().equals("M1") || data.getGonghlx().equals("MV")) {
						if (!lingjckMap.containsKey(data.getUsercenter() + data.getLingjbh() + data.getKeh())) {
							sb.append("线边仓库不存在或已失效!");
							return sb.toString();
						}
						Lingjck xbck = lingjckMap.get(data.getUsercenter() + data.getLingjbh() + data.getKeh());
						data.setGongysdm(data.getGongysdm());
						//data.setCangkdm(ck.getCangkbh());
						data.setCangkdm(data.getKeh());
						data.setUsbaozlx(xbck.getUsbzlx());
						data.setUsbaozrl(xbck.getUsbzrl());
						/*data.setUabzuclx(ck.getUclx());
						data.setUabzucrl(ck.getUcrl());*/
					}else if (data.getGonghlx().equals("MD")) {
						String MDMos = "MD";
						String MDKey = data.getUsercenter() + data.getLingjbh() + data.getGongysdm() + data.getKeh().substring(0, 5) + data.getGonghlx();
						Map<String, Wullj> wulljCheck = wulljMap.get(MDMos);
						
						if (!wulljCheck.containsKey(MDKey)) {
							sb.append("没有找到对应的物流路径!");
							return sb.toString();
						}else if(StringUtils.isBlank(wulljCheck.get(MDKey).getXiehztbh())){
							sb.append("对应的物流路径对应的卸货站台为空!");
							return sb.toString();
						}else if (StringUtils.isBlank(wulljCheck.get(MDKey).getGcbh())) {
							sb.append("对应的物流路径对应的承运商编码为空!");
							return sb.toString();
						}else if (StringUtils.isBlank(MDKey)) {
							sb.append("对应的物流路径对应的路径编号为空!");
							return sb.toString();
						}else{
							Wullj wullj = wulljCheck.get(MDKey);
							if (!lingjckMap.containsKey(data.getUsercenter() + data.getLingjbh() + wullj.getXianbck())) {
								sb.append("线边仓库不存在或已失效!");
								return sb.toString();
							}
							Lingjck xbck = lingjckMap.get(data.getUsercenter() + data.getLingjbh() + wullj.getXianbck());
							data.setGongysdm(data.getGongysdm());
							data.setCangkdm(xbck.getCangkbh());
							data.setUabzuclx(xbck.getUclx());
							data.setUabzucrl(xbck.getUcrl());
							data.setUsbaozlx(ck.getUclx());
							data.setUsbaozrl(ck.getUcrl());
						}
					}
					
				}
			}
			//如果是D，查询零件消耗点
			Map<String, String> map = new HashMap<String, String>();
			String beihsj = null;
			//获取本次时间推算所需参数
			Lingjxhd xhd = null;
			Wullj wullj = null;
			Xiehzt xiehzt = null;
			if (data.getGonghlx()!= null && !data.getGonghlx().equals("") && data.getGonghlx().endsWith("D")) {
				if (!lingjxhdMap.containsKey(data.getUsercenter() + data.getLingjbh() + data.getKeh())) {
					sb.append("消耗点不存在或已失效!");
					return sb.toString();
				}else {
					xhd = lingjxhdMap.get(data.getUsercenter() + data.getLingjbh() + data.getKeh());
					if (xhd.getXiaohcbh() == null || xhd.getXiaohcbh().equals("")) {
						sb.append("小火车编号为空!");
						return sb.toString();
					}
					data.setXiaohd(xhd.getXiaohdbh());
					data.setChanx(xhd.getShengcxbh());
					data.setXianbyhlx(xhd.getXianbyhlx());
					
				}
			}
			//String fenpq = data.getXiaohd().substring(0, 5); 分配区？
			String key = "";
			String mos = "";
			if (data.getGonghlx().equals("CD")) {
				//key=用户中心+零件编号+供应商代码+生产线编号+分配区号+外部模式
				key = data.getUsercenter() + data.getLingjbh() + data.getGongysdm() + data.getChanx() + data.getKeh().substring(0, 5) + data.getGonghlx();
				mos = "CD";
			}else if (data.getGonghlx().equals("C1") || data.getGonghlx().equals("CV")) {
				//key=用户中心+零件编号+目的地+外部模式+供应商代码
				key = data.getUsercenter() + data.getLingjbh() + data.getKeh() + data.getGonghlx() + data.getGongysdm();
				mos = "CV";
			}else if (data.getGonghlx().equals("MD")) {
				//key=用户中心+零件编号+订货仓库+分配区+模式
				key = data.getUsercenter() + data.getLingjbh() + data.getGongysdm() + data.getKeh().substring(0, 5) + data.getGonghlx();
				mos = "MD";
			}else if (data.getGonghlx().equals("M1") || data.getGonghlx().equals("MV")) {
				//key=用户中心+零件编号+订货仓库+线边仓库+模式2
				key = data.getUsercenter() + data.getLingjbh() + data.getGongysdm() + data.getKeh() + data.getGonghlx();
				mos = "MV";
			}
			if(!wulljMap.containsKey(mos)){
				sb.append("物流路径中没有对应模式!");
				return sb.toString();
			}else {
				Map<String, Wullj> wulljCheck = wulljMap.get(mos);
				if (!wulljCheck.containsKey(key)) {
					sb.append("没有找到对应的物流路径!");
					return sb.toString();
				}else if(StringUtils.isBlank(wulljCheck.get(key).getXiehztbh())){
					sb.append("对应的物流路径对应的卸货站台为空!");
					return sb.toString();
				}else if (StringUtils.isBlank(wulljCheck.get(key).getGcbh())) {
					sb.append("对应的物流路径对应的承运商编码为空!");
					return sb.toString();
				}else if (StringUtils.isBlank(wulljCheck.get(key).getLujbh())) {
					sb.append("对应的物流路径对应的路径编号为空!");
					return sb.toString();
				}else{
					wullj = wulljCheck.get(key);
					data.setXiehzt(wullj.getXiehztbh());
					data.setLujdm(wullj.getLujbh());
					data.setFahd(wullj.getFahd());
					if (mos.equals("CD")) {
						data.setCangkdm(wullj.getMudd());
						data.setBeihsj2(wullj.getBeihsj());
						data.setGcbh(wullj.getGcbh());
					}else if (mos.equals("CV")) {
						data.setCangkdm(wullj.getMudd());
						map.put("shengcxbh", wullj.getXianbck());
						data.setGcbh(wullj.getGcbh());
					}else if (mos.equals("MD")) {
						data.setCangkdm(wullj.getXianbck());
						data.setBeihsj2(wullj.getBeihsj());
						data.setGcbh(data.getGongysdm());
						map.put("cangkbh", wullj.getXianbck());
					}else if (mos.equals("MV")) {
						data.setGcbh(data.getGongysdm());
						map.put("shengcxbh", wullj.getXianbck());
					}
					if (data.getGonghlx().equals("MD") || data.getGonghlx().equals("CD")) {
						//模板时间为最晚到货时间
						data.setZuiwdhsj(data.getJiaofrq());	
						BigDecimal neibwlsj = wullj.getBeihsjc();
						//内部物流时间C为空
						if (neibwlsj == null) {
							sb.append("内部物流时间C为空");
							return sb.toString();
						}
						//从卸货站台得到提前到货时间
						xiehzt = xiehztMap.get(data.getUsercenter() + data.getXiehzt());
						BigDecimal yunxtqdhsj = new BigDecimal(0);
						if (xiehzt!=null) {
							yunxtqdhsj = xiehzt.getYunxtqdhsj();
							if (yunxtqdhsj == null) {
								sb.append("卸货站台允许提前到货时间为空!");
								return sb.toString();
							}
						}else {
							sb.append("卸货站台不存在!");
							return sb.toString();
						}
						//参考系类获取结束
						String zuiwdhsj = data.getZuiwdhsj();
						//1. 根据提前到货时间和最晚到货时间向前推工作时间模板，获取最早到货时间
						map.put("xiaohcbh", xhd.getXiaohcbh());
						map.put("usercenter", data.getUsercenter());
						map.put("lingjbh", data.getLingjbh());
						map.put("xiaohdbh", data.getXiaohd());
						map.put("gongysbh", data.getGongysdm());
						map.put("shengcxbh", data.getChanx());
						map.put("juedsk", zuiwdhsj);
						map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));//允许提前到货时间
						// 最早到货时间
						String zuizdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));//向前 推工作时间模板
						if (zuizdhsj.length()<16) {
							sb.append("找不到有效的工作时间模板");
							return sb.toString();
						}
						data.setZuizdhsj(zuizdhsj);	
						
						//2. 从物流路径获取内部物流时间，再根据最晚到货时间和内部物流时间从工作时间模板向后推算出备货时间
						map.put("juedsk", zuiwdhsj);
						map.put("shijNum", CommonFun.strNull(neibwlsj.intValue()));//内部物流时间
						//推算备货时间（用户中心、生产线、最晚到货时间）
						beihsj = CommonFun.strNull(anxlsOrderService.queryGongzsjmbH(map));//向后推工作时间模板
						if (beihsj.length()<16) {
							sb.append("找不到有效的工作时间模板");
							return sb.toString();
						}
						map.put("gcbh", data.getGcbh());
						map.put("daohsj", zuiwdhsj);
						map.put("xiehztbh", wullj.getXiehztbh().substring(0,4));
						
						//消耗时间和备货时间现在应该是最后推算的
						map.put("kaisbhsj", beihsj.substring(0, 16));
						// 取小火车运输时刻
						List<Xiaohcyssk> Listxhcyssk = anxlsOrderService.queryXiaohcysskObjectByShangxH(map);
						// 如果小火车时刻表没有数据,跳过该条记录,不予计算
						if (Listxhcyssk == null || Listxhcyssk.isEmpty()) {
							sb.append("未查到相关小火车信息");
							return sb.toString();
						}else if (Listxhcyssk.size()<2) {
							sb.append("相关小火车信息数量不足");
							return sb.toString();
						}
						Xiaohcyssk xiaohcyssk = Listxhcyssk.get(1);
						// 上线时间
						String shangxsj = xiaohcyssk.getChufsxsj();
						//备货时间
						beihsj = xiaohcyssk.getKaisbhsj();
						
						if (beihsj == null || beihsj.equals("")) {
							return "小火车运输时刻,小火车编号" + xhd.getXiaohcbh() + "生产线编号" + xhd.getShengcxbh() + "备货为空";
						}
						if (shangxsj == null || shangxsj.equals("")) {
							return "小火车运输时刻,小火车编号" + xhd.getXiaohcbh() + "生产线编号" + xhd.getShengcxbh() + "上线时间为空";
						}
						data.setXiaohcsxsj(shangxsj.substring(0, 16));
						data.setXiaohcbhsj(beihsj);
						data.setTangc(xiaohcyssk.getTangc());//小火车运输时刻趟次
						data.setXiaohch(xhd.getXiaohcbh());//小火车编号
						data.setXiaohccxh(xhd.getXiaohccxbh());//小火车车厢编号
						
						data.setXiaohsj(shangxsj);
					}else {
						map.put("usercenter", data.getUsercenter());
						map.put("xiehztbh", wullj.getXiehztbh());
						map.put("gcbh", data.getGcbh());
						//从卸货站台得到提前到货时间
						xiehzt = xiehztMap.get(data.getUsercenter() + data.getXiehzt());
						BigDecimal yunxtqdhsj = new BigDecimal(0);
						if (xiehzt!=null) {
							yunxtqdhsj = xiehzt.getYunxtqdhsj();
							if (yunxtqdhsj == null) {
								sb.append("卸货站台允许提前到货时间为空!");
								return sb.toString();
							}
						}else {
							sb.append("卸货站台不存在!");
							return sb.toString();
						}
						data.setZuiwdhsj(data.getJiaofrq());//模板时间为最晚到货时间
						//预计到货时间=时间(CDMD为小火车备货时间,C1M1为消耗时间)-内部物流时间
						map.put("juedsk", data.getZuiwdhsj());
						map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));//允许提前到货时间
				
						// 最早到货时间
						String zuizdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));
						if (zuizdhsj.length()<16) {
							sb.append("找不到有效的工作时间模板");
							return sb.toString();
						}
						data.setZuizdhsj(zuizdhsj);
					}
					//M1模式保存小火车出发上线时间 = 最晚到货时间
					if(data.getGonghlx().equals("M1") || data.getGonghlx().equals("MV")){
						data.setXiaohcsxsj(data.getZuiwdhsj());
					}
					/**
					 * M1MD模式计算发运时间
					 */
					if (data.getGonghlx().equalsIgnoreCase(Const.ANX_MS_M1) || data.getGonghlx().equalsIgnoreCase(Const.ANX_MS_MD)
							|| data.getGonghlx().equalsIgnoreCase(Const.MV)) {
						Date date = CommonFun.yyyyMMddHHmm.parse(data.getZuiwdhsj());//设置为最晚到货时间
						//发运时间 = 最晚到货时间 - 仓库送货时间2
						date.setTime(date.getTime() - CommonFun.getBigDecimal(wullj.getCangkshsj2()).multiply(new BigDecimal(24))
								.multiply(new BigDecimal(60)).multiply(new BigDecimal(60)).multiply(new BigDecimal(1000)).intValue());
						data.setFaysj(CommonFun.yyyyMMddHHmm.format(date));
					}else{//C1CD模式发运时间= 最晚到货时间 - 运输周期
						Date date = CommonFun.yyyyMMddHHmm.parse(data.getZuiwdhsj());//设置为最晚到货时间
						//发运时间 = 最晚到货时间 - 运输周期
						date.setTime(date.getTime() - CommonFun.getBigDecimal(wullj.getYunszq()).multiply(new BigDecimal(24))
								.multiply(new BigDecimal(60)).multiply(new BigDecimal(60)).multiply(new BigDecimal(1000)).intValue());
						data.setFaysj(CommonFun.yyyyMMddHHmm.format(date));
					}
					data.setJiaofrq(data.getZuiwdhsj());
				}
			}
			if (sb.toString().endsWith("行:")) {
				sb = new StringBuffer("第");
			}
		}
		return null;
	}
	
}
