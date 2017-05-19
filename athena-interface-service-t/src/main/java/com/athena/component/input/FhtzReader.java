package com.athena.component.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.DataExchange;
import com.athena.component.exchange.FileLog;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.component.exchange.field.DataField;
import com.athena.component.exchange.txt.TxtDataReader;
import com.athena.component.exchange.utils.ConvertUtils;
import com.toft.core2.dao.database.DbUtils;

/**
 * 发货通知数据读取
 * 
 * @author WL
 * @date 2011-10-26
 * 
 */

public class FhtzReader extends TxtDataReader {
	protected static Logger logger = Logger.getLogger(FhtzReader.class);	//定义日志方法
	public String datasourceId=null;	
	PreparedStatement prep=null;
	/**
	 * 数据库连接
	 */
	private Connection conn;

	/**
	 * record放置数据集合
	 */
	private Record record = null;

	/**
	 * 解析过的code
	 */
	private Record isExist = null;

	/**
	 * 代码数组
	 */
	private String[] code = null;

	/**
	 * 开始下标数组
	 */
	private int[] start = null;

	/**
	 * 结束下标数组
	 */
	private int[] end = null;

	/**
	 * B04A001中间变量
	 */
	private Object temp = "";
	private Object tempBz = "";
	private Object tempUasl = "";

	private int fy_xuh = 0; //发运序号

	private int bz_xuh = 0; //包装序号
	
	private int num = 0;//插入信息总条数
	
	private int ztxx_num = 0; //主体信息插入数据条数
	
	private int fymx_num = 0;// 发运明细插入数据条数
	
	private int bzxx_num = 0;//包装信息插入数据条数

	
	private int error_num = 0;//错误数据条数
	
	private String errorMessage=null;//错误信息内容
	
	//private Map map=null;//定义map
	
	private Record lingjRecord = null;
	private Record baozRecord = null;
	private DataParserConfig dataParserConfig=null;
	/**
	 * 构造函数,初始化
	 * 
	 * @param dataParserConfig
	 */
	public FhtzReader(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		datasourceId=dataParserConfig.getWriterConfig().getDatasourceId();
		// 获取数据库连接
		conn = DbUtils.getConnection(datasourceId);
		record = new Record();
		isExist = new Record();
		lingjRecord = new Record();
		baozRecord = new Record();
		// 初始化代码集合,结束下标
		code = new String[] { "AT0100", "AD0300", "AC1800", "AC0500", "AH0200",
				"AC0600", "AD0400", "AA0200","AB0300", "AA0400", "AA0900",
				"AA0100", "AB0400", "AB0500", "AA0700", "AB0200"};
		start = new int[] { 0, 2, 8, 10, 11, 12, 13, 14, 16, 18, 20, 21, 22, 23, 25, 26};
		end = new int[]   { 2, 8, 10, 11, 12, 13, 14, 16, 18, 20, 21, 22, 23, 25, 26, 27};
	}



	/**
	 * 读取数据方法
	 * 
	 * @author GJ
	 * @date 2011-10-26
	 */
	@SuppressWarnings("finally")
	@Override
	public int readLine() {
		// 读取文件
		FileInputStream fs=null;
		InputStreamReader is=null;
		BufferedReader br=null;
		File f=null;
		String f_name=null;//文件名称
		String SID=DbDataWriter.getUUID();//唯一标示
		String EID=DbDataWriter.getUUID();//唯一标示
		String FileBeginTime=DataExchange.RunStartTime;//接口文件开始运行时间
	    String FileEndTime=null;//文件运行结束时间
		try {
			// 获取配置
			  dataParserConfig = getDataParserConfig();
			  f=new File(dataParserConfig.getReaderConfig().getFilePath());
			  String filename=f.getName();
			  String[] fname=filename.split(",");
			  for (int i = 0; i < fname.length; i++) {
				    f=new File("/users/ath00/tmp/"+fname[i]+".txt");
				    f_name=f.getName();
				    if(f.exists()){
				    	fs = new FileInputStream(f.getPath());
				    	is = new InputStreamReader(fs, dataParserConfig.getReaderConfig().getEncoding());
				    	br = new BufferedReader(is);
				    	String line = "";
				    	
			// 解析文本
			while (null != (line = br.readLine())) {
				i++;
				complete(i, dataParserConfig, line);
			}// 解析完文本保存文本数据
			this.writeTableAndLog();
			
			num=ztxx_num+fymx_num+bzxx_num;//插入的总记录行数
			FileEndTime=DateTimeUtil.getAllCurrTime();//文件运行结束时间
			FileLog.getInstance(datasourceId).File_info(SID, "2050", f_name, FileBeginTime, FileEndTime, num, 0, error_num, "1");//记录文件信息日志
			
				    
				    }
		}
		}catch (Exception e) {
			FileEndTime=DateTimeUtil.getAllCurrTime();//文件运行结束时间
			FileLog.getInstance(datasourceId).File_info(SID, "2050", f_name, FileBeginTime, FileEndTime, num, 0, error_num, "-1");//记录文件信息日志
			FileLog.getInstance(datasourceId).File_ErrorInfo(EID, "2050", SID, errorMessage, "");//记录错误信息日志
//			throw new RuntimeException(e.getMessage());
			logger.error(e.getMessage());
		} finally {// 关闭流
			try {
				if (null != br) {
					br.close();
					br=null;
				}
				if (null != is) {
					is.close();
					is=null;
				}
				if (null != fs) {
					fs.close();
					fs = null;
				}
				
				close();
				
				if(null!=conn){
					conn.close();
				}
				if(null!=prep){
					prep.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			return 0;
		}
	}

	
	public void close() {
		for (int i = 0; i < this.getFileInputStreams().size(); i++) {
			if (this.getFileInputStreams().get(i) != null) {
				try {
					this.getFileInputStreams().get(i).close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}

	}
	
	
	/**
	 * 解析文本数据
	 * 
	 * @param rowIndex
	 *            行标
	 * @param dataPareserConfig
	 *            字段配置
	 * @param line
	 *            行数�?
	 * @author GJ
	 * @throws SQLException
	 * @date 2011-10-26
	 */
	private void complete(int rowIndex, DataParserConfig dataPareserConfig,
			String line) throws SQLException {
		// 文本头代码
		String id = line.toString().substring(0, 6).trim();
		if(null!=id&&!"".equals(id)){
		for (int j = 0; j < code.length; j++) {
			if (id.equals(code[j])) {// 判断代码,转换数据
				parse(id, dataPareserConfig, line, start[j], end[j]);
			}
		}
	}
		// 如果不是第一行且开头是SERV表示数据解析完毕,保存该数据
		if (rowIndex > 1 && id.equals("SERV")) {
			this.writeTableAndLog();
		}
	}
	
	/**
	 * 查询in_edi_fhtz_fyxx表交付单号
	 * @author 贺志国
	 * @date 2013-1-18
	 * @param jiaofdh 交付单号
	 * @return String 交付单号
	 * @throws SQLException 异常
	 */
	private String  queryJfdhOfFyxx(String jiaofdh) throws SQLException{
		StringBuilder sbuf = new StringBuilder();
		String strJfd = "";
		sbuf.append("select T01A001 from "+SpaceFinal.spacename_ck+".in_edi_fhtz_fyxx ");
		sbuf.append("where T01A001='" + strNull(jiaofdh) + "'");
		prep=conn.prepareStatement(sbuf.toString());
		ResultSet rs = prep.executeQuery();
		while(rs.next()){
			strJfd = rs.getString("T01A001");
		}
		prep.close();
		rs.close();
		return strJfd;
}
	
	
	/**
	 * 写LOG日志
	 * @author 贺志国
	 * @date 2013-1-18
	 */
	private void writeLog(){
		
		StringBuilder strbuf = new StringBuilder();
		strbuf.append("交付单号已存在，交付单号="+strNull(record.getString("T01A001")));
		strbuf.append(",usercenter='"+ record.getString("H02AB01").substring(0, 2)+"',");
		strbuf.append("T01B001='"+strNull(DateTimeUtil.DateFormat_Fhtz(record.getString("T01B001")))+"',");
		strbuf.append("D03B001='"+strNull(record.getString("D03B001"))+"',");
		strbuf.append("D03D002='"+strNull(record.getString("D03D002"))+"',");
		strbuf.append("D03FA01='"+record.get("D03FA01")+"',");
		strbuf.append("D03FA02='"+strNull(record.getString("D03FA02"))+"',");
		strbuf.append("D03FA05='"+strNull(record.getString("D03FA05"))+"',");
		strbuf.append("D03FD03='"+strNull(DateTimeUtil.DateFormat_Fhtz(record.getString("D03FD03")))+"',");
		strbuf.append("C18B005='"+strNull(record.getString("C18B005"))+"',");
		strbuf.append("C05AB01='"+strNull(record.getString("C05AB01"))+"',");
		strbuf.append("D04A001='"+strNull(record.getString("D04A001"))+"',");
		logger.info(strbuf.toString());
	
		//记录解析完毕,清空缓存
		isExist = new Record();
		lingjRecord = new Record();
		baozRecord = new Record();
		temp = "";
		tempBz = "";
		fy_xuh = 0;
		bz_xuh = 0;
	}
	
	
	/**
	 * 提取方法，如果交付单号相同则写日志，否则写DB
	 * @author 贺志国
	 * @date 2013-1-18
	 * @throws SQLException
	 */
	private void writeTableAndLog()throws SQLException{
		//查询主表in_edi_fhtz_fyxx交付单号，看是否存在相同的交付单号
		String jiaofdh = queryJfdhOfFyxx(record.get("T01A001").toString());
		if(strNull(record.getString("T01A001")).equals(jiaofdh)){//文本中的交付单号和数据库中的交付单号相同
			this.writeLog(); //重复数据，写Log日志
		}else{
			insert();//执行插入数据
			update();//更新包装对应信息表
		}
	}

	/**
	 * 数据转换方法
	 * 
	 * @param dataPareserConfig
	 *            数据配置
	 * @param record
	 *            数据集合
	 * @param line
	 *            行数据
	 * @param start
	 *            开始下标
	 * @param end
	 *            结束下标
	 * @author GJ
	 * @date 2011-10-26
	 */
	private void parse(String id, DataParserConfig dataPareserConfig,
			String line, int start, int end) {
		Record info = new Record();
		try{
		if (id.equals("AA0200")) {
			fy_xuh++;
		}
		if (id.equals("AB0500")) {
			bz_xuh++;
		}
		// 获取配置字段集合
		DataField[] fields = dataPareserConfig.getDataFields();
		// 循环遍历配置字段集合
		for (; start < end; start++) {
			DataField field = fields[start];// 字段信息
			int s = field.getStart();// 开始下标
			int e = s + field.getLength();// 结束下标
			String value = "";
			if (s < line.length()) {// 截取数据
				value = line.substring(s, Math.min(e, line.length()));
			}
			// 如果是可重复出现的子数据,保存数据信息
			if (id.equals("AA0200") ||id.equals("AB0200")|| id.equals("AB0400")|| id.equals("AB0500")||id.equals("AB0300")) {
				info.put(field.getWriterColumn(),ConvertUtils.convertValue(field, value.trim()));
				if (id.equals("AB0400")) {
					temp = ConvertUtils.convertValue(field, value.trim());// 缓存B04A001信息
				} else if (id.equals("AB0500")) {
					info.put("B04A001", temp);
				}
				//hzg 2012-11-20 add method field 修改原因 B05AB02值 重复存map，导致所有值相同
				if (id.equals("AB0200")) {
					tempUasl = ConvertUtils.convertValue(field, value.trim());// 缓存B05AB02信息
				} else if (id.equals("AB0500")) {
					info.put("B05AB02", tempUasl);
				}
				//判断是否为包装类型（UA）AB0300->B03AB01，B03AB02；缓存第一个值B03AB01到AB0500中
				if (id.equals("AB0300")) {
					baozRecord.put("B03AB01"+fy_xuh, line.substring(6, 11).trim()); //包装类型（UA）
					tempBz = ConvertUtils.convertValue(field, line.substring(6, 11).trim());// 缓存B03AB01信息
				} else if (id.equals("AB0500")) {
					info.put("B03AB01", tempBz);
				}
				info.put("fy_xuh", fy_xuh);
				info.put("bz_xuh", bz_xuh);
			} else {
				record.put(field.getWriterColumn(),ConvertUtils.convertValue(field, value.trim()));
			}
			//将AA0400->A04A002，A04B003；的第一个值A04A002放入lingjRecord中，（第二个值默认放入到record中）
			if(id.equals("AA0400")){//零件号
				String lingjh = line.substring(6, 16).trim();  //零件
				lingjRecord.put("A04A002"+fy_xuh, lingjh);
			}

			//将AB0300->B03AB01，B03AB02；的第二个值放入record中
			if(id.equals("AB0300")){
				String B03AB02 = line.substring(23, 26).trim();//编码责任代理行
				record.put("B03AB02", B03AB02);
			}
		}
		// 计算子数据数量
		int count = count(isExist.get(id));
		isExist.put(id, count);
		record.put(id + count, info);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
	}

	/**
	 * 保存数据方法
	 * 
	 * @author GJ
	 * @throws SQLException
	 * @date 2011-10-26
	 */
	private void insert() throws SQLException {
		try {
			conn.setAutoCommit(false);
			String H02AB01 = record.getString("H02AB01");
			String usercenter=null;
			String cangkbh="";
			if(!"".equals(H02AB01)){
				usercenter = H02AB01.substring(0, 2);
				cangkbh = H02AB01.substring(H02AB01.length()-3, H02AB01.length());
			}
			String C18AB01=strNull(record.getString("C06AB01"));   //经测试liuyc与于鹏讨论,C18AB01销售商代码与C06AB01发送者代码值取相同值，取C06AB01的值  hzg 2012-11-22。
			//将获取的发运者代码C06AB01的值，即“ 邓白氏码”转换成运输商代码
			String gongysbh = strNull(queryGongys(usercenter,C18AB01));
			//根据用户中心和仓库编号查询参考系ckx_xiehzt取卸货站台编码
			String xiehztbh =  queryXiehztbh(usercenter,cangkbh);
			
			// 保存发运主体信息
			StringBuffer strbuf=new StringBuffer();
			strbuf.append("insert into "+SpaceFinal.spacename_ck+".in_edi_fhtz_fyxx(USERCENTER,T01A001,T01B001,D03B001,D03D002,");
			strbuf.append("D03FA01,D03FA02,D03FA05,D03FD03,C18AB01,C18B005,C05AB01,H02AB01,C06AB01,");
			strbuf.append("D04A001,cj_date,clzt)");
			strbuf.append("values(");
			strbuf.append("'"+usercenter+"',");
			strbuf.append("'"+strNull(record.getString("T01A001"))+"',");
			strbuf.append("'"+strNull(DateTimeUtil.DateFormat_Fhtz(record.getString("T01B001")))+"',");
			strbuf.append("'"+strNull(record.getString("D03B001"))+"',");
			strbuf.append("'"+strNull(record.getString("D03D002"))+"',");
			strbuf.append("'"+record.get("D03FA01")+"',");
			strbuf.append("'"+strNull(record.getString("D03FA02"))+"',");
			strbuf.append("'"+strNull(record.getString("D03FA05"))+"',");
			strbuf.append("'"+strNull(DateTimeUtil.DateFormat_Fhtz(record.getString("D03FD03")))+"',");
			strbuf.append("'"+gongysbh+"',");
			strbuf.append("'"+strNull(record.getString("C18B005"))+"',");
			strbuf.append("'"+strNull(record.getString("C05AB01"))+"',");
			strbuf.append("'"+strNull(xiehztbh)+"',");
			strbuf.append("'"+gongysbh+"',");
			strbuf.append("'"+strNull(record.getString("D04A001"))+"',");
			strbuf.append("to_date('"+DateTimeUtil.getAllCurrTime()+"','yyyy-MM-dd hh24:mi:ss'),");
			strbuf.append("0)");			
			prep=conn.prepareStatement(strbuf.toString());
			prep.execute();
			prep.close();
			ztxx_num++;
			
			// 保存发运明细信息,AA0200代表发运明细信息
			int count = (Integer) isExist.get("AA0200");
			for (int i = 1; i <= count; i++) {
				Record AA0200 = (Record) record.get("AA0200" + i);
				StringBuffer strbuf1=new StringBuffer();
				strbuf1.append("insert into "+SpaceFinal.spacename_ck+".in_edi_fhtz_fymx(xuh,T01A001,A02D001,A02D002,");
				strbuf1.append("B03AB01,B03AB02,A04A002,A04B003,A09AC01,A01C009)");
				strbuf1.append("values(");
				strbuf1.append("'"+strNull(AA0200.getString("fy_xuh"))+"',");
				strbuf1.append("'"+strNull(record.getString("T01A001"))+"',");
				strbuf1.append("'"+strNull(AA0200.getString("A02D001"))+"',");
				strbuf1.append("'"+strNull(AA0200.getString("A02D002"))+"',");
				//strbuf1.append("'"+B03AB01+"',");
				strbuf1.append("'"+strNull(baozRecord.getString("B03AB01"+i))+"',"); //包装类型 hzg 2012-11-4 
				strbuf1.append("'"+strNull(record.getString("B03AB02"))+"',");
				//strbuf1.append("'"+A04A002+"',");
				strbuf1.append("'"+strNull(lingjRecord.getString("A04A002"+i))+"',"); //零件号 hzg 2012-11-4 bug 0005263
				strbuf1.append("'"+strNull(record.getString("A04B003"))+"',");
				strbuf1.append("'"+strNull(record.getString("A09AC01"))+"',");
				strbuf1.append("'"+strNull(record.getString("A01C009"))+"')");
                prep=conn.prepareStatement(strbuf1.toString());
                prep.execute();
                prep.close();
				fymx_num++;	
				
			}

			// 保存包装对应信息,AB0500对应包装对应信息
			int num = (Integer) isExist.get("AB0500");
			for (int i = 1; i <= num; i++) {	
				Record AB0500 = (Record) record.get("AB0500" + i);		
				String B05AD01 = AB0500.getString("B05AD01");
				/*String lingjh = lingjRecord.getString("AA0400"+i); //零件号
				Map<String,String> map = this.queryUCOfLingjgys(usercenter, C18AB01, lingjh);
				String ucbzlx = (String) map.get("ucbzlx");
				String uaucgs = (String) map.get("uaucgs");
				String ucrl = (String) map.get("ucrl");*/
				
				// 录入信息到包装对应信息表
				StringBuffer strbuf3=new StringBuffer();
				strbuf3.append("insert into "+SpaceFinal.spacename_ck+".in_edi_fhtz_bzxx(xuh,T01A001,B04A001,B05AB01,B05AB03," +
						//"ucbzlx,uaucgs,ucrl," +
						"B05AB02,B05AD01,A07A001)");
				strbuf3.append("values(");
//				strbuf3.append("'"+strNull(AB0500.getString("bz_xuh"))+"',");
				/****last modify 2012-09-17   bug号:0004312   ****/
				strbuf3.append("'"+strNull(AB0500.getString("fy_xuh"))+"',");
				
				strbuf3.append("'"+strNull(record.getString("T01A001"))+"',");
				strbuf3.append("'"+strNull(AB0500.getString("B04A001"))+"',");
				strbuf3.append("'"+strNull(AB0500.getString("B05AB01"))+"',");
				//strbuf3.append("'"+B03AB01+"',");
				strbuf3.append("'"+strNull(AB0500.getString("B03AB01"))+"',");//包装类型 hzg 2012-11-4 
				/*strbuf3.append("'"+strNull(ucbzlx)+"',");
				strbuf3.append("'"+strNull(uaucgs)+"',");
				strbuf3.append("'"+strNull(ucrl)+"',");*/
				//strbuf3.append("'"+strNull(record.getString("B05AB02"))+"',");
				strbuf3.append("'"+strNull(AB0500.getString("B05AB02"))+"',"); //UA实际发运数量 hzg 2012-11-20
				// 如果接收到要货令数据第一位字符为'W',并且最后2位为'03',则将'W'改为'L'，'03'改为'00'
				if (!"".equals(B05AD01)) {
					strbuf3.append("'"+B05AD01+"',");
					//add by pan.rui 需求号为：2013-01-CK001 需要取消此规则
//					StringBuilder strbud = new StringBuilder(B05AD01);
//					if("W".equals(strbud.substring(0, 1))&&"03".equals(strbud.substring(7,9))){
//						strbud.replace(0, 1, "L");
//						strbud.replace(7, 9, "00");			
//						strbuf3.append("'"+strbud.toString()+"',");
//					}else{
//						strbuf3.append("'"+B05AD01+"',");
//					}					
				}else{
					strbuf3.append("'',");
				}
				
				strbuf3.append("'"+strNull(record.getString("A07A001"))+"')");
                prep=conn.prepareStatement(strbuf3.toString());
                prep.execute();
                prep.close();
				bzxx_num++;
				}
			conn.commit();// 提交事物
			}catch (SQLException e){
			conn.rollback();// 异常,回滚事物
			errorMessage=e.getMessage();
			error_num++;
            throw new SQLException (e.getMessage());
			} finally {
			//记录解析完毕,清空缓存
			/*isExist = new Record();
			lingjRecord = new Record();
			baozRecord = new Record();
			temp = "";
			tempBz = "";
			fy_xuh = 0;
			bz_xuh = 0;*/
			//conn.close();
			prep.close();
		}
	}

	
	/**
	 * 查询零件供应商参考系表：供应商UC的包装类型，供应商UA里UC的个数，供应商UC的容量字段
	 * @author 贺志国
	 * @date 2012-11-4
	 * @param usercenter 用户中心
	 * @param C18AB01 销售商代码
	 * @param A04A002 零件号
	 * @return Map<String,String> map
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> queryUCOfLingjgys(String usercenter,String gongysbh,String lingjbh){
		// 去零件供应商参考系表：供应商UC的包装类型，供应商UA里UC的个数，供应商UC的容量字段
		StringBuffer strbuf2=new StringBuffer();
		strbuf2.append("select ucbzlx, to_char(uaucgs) as uaucgs, to_char(ucrl) as ucrl ");
		strbuf2.append("from "+SpaceFinal.spacename_ck+".ckx_lingjgys ");
		strbuf2.append("where usercenter='"+usercenter+"' and ");
		strbuf2.append("gongysbh='"+gongysbh+"' and ");
		strbuf2.append("lingjbh='"+lingjbh+"'");
		Map<String,String> map = DbUtils.selectOne(strbuf2.toString(), datasourceId);
		return map;
	}
	
	
	/**
	 * 根据邓白氏码查询供应商表中的供承运商编号
	 * @author 贺志国
	 * @date 2012-11-22
	 * @param usercenter 用户中心
	 * @param dengbsm 邓白氏码
	 * @return String gongysbh 供供承运商编号
	 */
	public String queryGongys(String usercenter,String dengbsm){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("select gcbh from "+SpaceFinal.spacename_ck+".ckx_gongys ");
		strbuf.append("where usercenter='"+usercenter+"' and ");
		strbuf.append("chuanz='"+dengbsm+"'");
		String gongysbh = (String) DbUtils.selectValue(strbuf.toString(), datasourceId);
		return gongysbh;
	}
	
	/**
	 * 根据用户中心和仓库编号查询参考系卸货站台表
	 * @author 贺志国
	 * @date 2013-1-9
	 * @param usercenter 用户中心
	 * @param cangkbh 仓库编号
	 * @return String Xiehztbh 卸货站台编号
	 * @update hzg 如果有多条说明他们数据维护错误，只需一条记录 2013-5-29 mantis:0007070
	 */
	public String queryXiehztbh(String usercenter,String cangkbh){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("select xiehztbh from "+SpaceFinal.spacename_ck+".ckx_xiehzt ");
		strbuf.append("where usercenter='"+usercenter+"' and ");
		strbuf.append("cangkbh='"+cangkbh+"' and rownum=1");
		String xiehzt = (String) DbUtils.selectValue(strbuf.toString(), datasourceId);
		return xiehzt;
	}
	
	/**
	 * 更新包装对应信息表（in_edi_fhtz_bzxx）中的UC类型，UA容量，UC容量字段
	 * 条件：序号，交付单号
	 * @author 贺志国
	 * @date 2012-11-22
	 */
	private void update() throws SQLException{
		try{
			conn.setAutoCommit(false);
			
			String H02AB01 = record.getString("H02AB01");
			String usercenter=null;
			if(!"".equals(H02AB01)){
			usercenter = H02AB01.substring(0, 2);
			}
			String C18AB01=strNull(record.getString("C06AB01"));   //经测试liuyc与于鹏讨论,C18AB01销售商代码与C06AB01发送者代码值取相同值，取C06AB01的值  hzg 2012-11-22。
			//将获取的发运者代码C06AB01的值，即“ 邓白氏码”转换成运输商代码
			String gongysbh = strNull(queryGongys(usercenter,C18AB01));
			
			//发运明细信息个数
			int count = (Integer) isExist.get("AA0200");
			for(int i=1;i<=count;i++){
				StringBuffer strbuf = new StringBuffer();
				Map<String,String> ucmap = this.queryUCOfLingjgys(usercenter, gongysbh, lingjRecord.getString("A04A002"+i));
				strbuf.append("update "+SpaceFinal.spacename_ck+".in_edi_fhtz_bzxx set ucbzlx='"+strNull(ucmap.get("ucbzlx")));
				strbuf.append("' ,uaucgs='"+strNull(ucmap.get("uaucgs")));
				strbuf.append("' ,ucrl='"+strNull(ucmap.get("ucrl"))+"' ");
				strbuf.append("where xuh='"+i+"' and  t01a001='"+strNull(record.getString("T01A001"))+"'");
				prep = conn.prepareStatement(strbuf.toString());
				prep.execute();
				prep.close();
			}
			conn.commit();
		}catch (SQLException e){
			conn.rollback();// 异常,回滚事物
			errorMessage=e.getMessage();
			error_num++;
            throw new SQLException (e.getMessage());
		} finally {
			//记录解析完毕,清空缓存
			isExist = new Record();
			lingjRecord = new Record();
			baozRecord = new Record();
			temp = "";
			tempBz = "";
			fy_xuh = 0;
			bz_xuh = 0;
			prep.close();
		}
		
		
	}
	
	/**
	 * 就算次数
	 * 
	 * @param obj
	 *            之前的次数对�?
	 * @return 次数
	 * @author GJ
	 * @date 2011-10-26
	 */
	private int count(Object obj) {
		if (null == obj){// 如果次数对象为空,则是第一条
			return 1;
		} else {
			int i = (Integer) obj;// 不为空次数++
			i++;
			return i;
		}
	}

	/**
	 * 空串处理
	 * 
	 * @param obj
	 *            对象
	 * @return 处理后字符串
	 * @author GJ
	 * @date 2011-10-26
	 */
	private String strNull(Object obj) {// 对象为空返回空串,不为空toString
		return obj == null ? "" : obj.toString();
	}
    
}
