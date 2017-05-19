package com.athena.component.output;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.exchange.DataWriter;
import com.athena.component.exchange.InziDbUtils;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.TxtCommand;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.config.ExchangerConfig;
import com.athena.component.exchange.db.DbDataReader;
import com.athena.component.exchange.db.DbRowParser;
import com.athena.component.exchange.field.DataField;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.athena.component.runner.RunnerService;
import com.toft.core2.dao.database.DbUtils;
/**
 * 工作时间模板
 * @author PAN.RUI
 * @vesion 1.0
 * @date 2012-4-18
 * 
 */
public class GongzJSDataReader extends DbDataReader {
	protected static Logger logger = Logger.getLogger(GongzJSDataReader.class);	//定义日志方法
	protected static int i = 0;
	private Connection conn = null;
	private TxtCommand<Object> command = null;
	private final static int PAGESIZE = 10000; //每次固定输入10000条
	private List<OutputStreamWriter> outs = new ArrayList<OutputStreamWriter>();
	//仓库入库明细构造函数
	public GongzJSDataReader(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	@Override
	public void open(DataWriter dataWriter,
			RunnerService runnerService) {
		ExchangerConfig readerConfig = dataParserConfig.getReaderConfig();
		
		String datasourceId = readerConfig.getDatasourceId();
		//获取数据库连接
		if(conn==null){
			this.conn = DbUtils.getConnection(datasourceId);
		}		
	}
	
	@Override
	public int readLine() {
		int totalNum = 0;
		try {
			ExchangerConfig readerConfig = dataParserConfig.getReaderConfig();
			DataField[] dataFields = dataParserConfig.getDataFields();
			
		    String sql = SpaceFinal.replaceSql(readerConfig.getSql()); //得到所配置 的SQL
		    String sqlBuffer = "";
		    
			String readTable = SpaceFinal.replaceSql(readerConfig.getTable()); //read配置文件中配置的table
			
			totalNum = getTotalNum(readTable); //总页数
			int totalPage = 0;
			totalPage = totalNum/PAGESIZE + (totalNum%PAGESIZE==0 ? 0 : 1);
			
			ExchangerConfig[] ecs = dataParserConfig.getWriterConfigs();
			for (ExchangerConfig ec : ecs) {
				outs.add(createOut(ec));
			}
			//10000为分页大小
			for(int j=0;j<totalPage;j++){
				sqlBuffer = " select  USERCENTER,CHANX,GONGZR,XIAOHSJ,JUEDSK "
					+ " from ( " + sql
			        + " and rownum <= "+((j+1)*PAGESIZE)+") t "
			        + " where t.RN >= "+(j*PAGESIZE+1); 
			    List<Map<String,Object>> dataList = GetdoWriterLine(sqlBuffer,dataFields);
			    for(int i=0;i<dataList.size();i++){
			    	for(int m=0;m<outs.size();m++){
					     command = new TxtCommand<Object>(outs.get(m), dataParserConfig, dataList.get(i));
					     command.execute();
			    	}
			    }
			    if(j%10==0){
			    	logger.info(new Date()+"输出数据"+PAGESIZE+"条");
			    }
			}
			InziDbUtils.getInstance().updateIN_zidbCHUCTS(dataParserConfig.getReaderConfig().getDatasourceId(),"1910",totalNum,0);

		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}finally{
			close();
		}
		return totalNum;
	}
	
	private OutputStreamWriter createOut(ExchangerConfig ec) {
		
		String filePath = ec.getFilePath();
		String fileName = ec.getFileName();
		String encoding = ec.getEncoding();

		//默认为GBK
		if(encoding==null){
			encoding = "GBK";
		}
		//System.out.println("filePath:"+filePath+"---"+"fileName:"+fileName+"---"+"encoding:"+encoding);
		//1：如果没有用户输入的路径 就创建此路径
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		//2:生成输出文件路径
		String outName = createRealPath(filePath,fileName);
		
		//3:创建输出流
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(new File(outName)),encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return writer;
	}
	

	/**
	 * 生成输出文件路径
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	private String createRealPath(String filePath, String fileName) {
		StringBuffer sb = new StringBuffer();
		sb.append(filePath);
		sb.append(File.separator);
		sb.append(fileName);
		return sb.toString();
	}
	
	public List<Map<String,Object>> GetdoWriterLine(String sql,DataField[] dataFields) throws SQLException{
		Statement cs = conn.createStatement();
		ResultSet rs = cs.executeQuery(sql);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		while(rs!=null&&rs.next()){
			Map<String,Object> result = new HashMap<String,Object>();
			Object value;
			for(DataField dataField:dataFields){
				//value = rs.getObject(dataField.getReaderColumn());
				//为了得到完成的date类型时间 改造
				value = getValueFromRs(rs,dataField);
				
				if(value!=null&&dataField.getWriterColumn()!=null){
					result.put(dataField.getWriterColumn(), value);
					value = null;
				}
			}
			list.add(result);
		}
		rs.close();
		cs.close();
		return list;
	} 
	
	/**
	 * 得到数据库数据总条数
	 * @param rs
	 * @param dataField
	 * @return
	 * @throws SQLException
	 */
	private int getTotalNum(String tableName){
		StringBuffer strbuf = new StringBuffer();
		int countNum = 0;
		try {
			strbuf.append(" select count(1) countNum from "+ tableName);
			countNum = Integer.valueOf(DbUtils.selectValue(strbuf.toString(),conn).toString());
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return countNum;
	}
	
	private Object getValueFromRs(ResultSet rs, DataField dataField) throws SQLException {
		Object result = null;
		if ("date".equals(dataField.getType())) {
			result = rs.getTimestamp(dataField.getReaderColumn());
		} else {
			result = rs.getObject(dataField.getReaderColumn());
		}
		return result;
	}
	
	public void close() {
		DbUtils.freeConnection(this.conn);
	}
}
