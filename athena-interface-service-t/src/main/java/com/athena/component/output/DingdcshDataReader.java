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
import java.text.SimpleDateFormat;
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
import com.athena.component.exchange.field.DataField;
import com.athena.component.runner.RunnerService;
import com.toft.core2.dao.database.DbUtils;
/**
 * 订单初始化 1760,1770,不区分用户中心，不使用分布输出
 * @author HZG
 * @vesion 1.0
 * @date 2013-1-10
 * 
 */
public class DingdcshDataReader extends DbDataReader {
	protected static Logger logger = Logger.getLogger(GongzJSDataReader.class);	//定义日志方法
	protected static int i = 0;
	private Connection conn = null;
	private TxtCommand<Object> command = null;
	private List<OutputStreamWriter> outs = new ArrayList<OutputStreamWriter>(); 
	
	//订单初始化构造函数
	public DingdcshDataReader(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 获取数据库连接
	 */
	@Override
	public void open(DataWriter dataWriter,RunnerService runnerService) {
		ExchangerConfig readerConfig = dataParserConfig.getReaderConfig();
		String datasourceId = readerConfig.getDatasourceId();
		//获取数据库连接
		if(conn==null){
			this.conn = DbUtils.getConnection(datasourceId);
		}		
	}
	
	/**
	 * 读取数据行
	 */
	@Override
	public int readLine() {
		int totalNum = 0;
		try {
			ExchangerConfig readerConfig = dataParserConfig.getReaderConfig();
			DataField[] dataFields = dataParserConfig.getDataFields();
			
		    String sql = SpaceFinal.replaceSql(readerConfig.getSql()); //得到所配置 的SQL
		    //String interfaceCode = SpaceFinal.replaceSql(readerConfig.getTable()); //read配置文件中配置的table值
		    String interfaceCode = super.getDe().getCID();
			 /**
             * 为了解决生成有增量的文件
             * 	根据 reader中isAllSet属性
             * 	如果此字段为空或true,则认为直接执行sql
             *  如果此字段为false,则将sql拼接上 增量条件   editTime 在 '接口完成时间'和 '当前时间'之间.
             */
		    String flagStr = readerConfig.getIsAllSet(); //判断是否为全量
		    
			ExchangerConfig[] ecs = dataParserConfig.getWriterConfigs();
			for (ExchangerConfig ec : ecs) {
				outs.add(createOut(ec));
			}
			totalNum = outPutTxt(sql,dataFields,flagStr,interfaceCode);
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}finally{
			close();
		}
		return totalNum;
	}
	
	/**
     * 输出数据
     * @param sql SQL字符串
     * @param dataFields 输出字段数组
     * @param flagStr 增量或全量的判断字符
     */
    private int outPutTxt(String sql,DataField[] dataFields,String flagStr,String interfaceCode) throws SQLException{
    	int totalNum = 0;
    	String flagSQL = "false".equals(flagStr)? makeIncrementSql():"";
        //拼接增量的字符串
    	StringBuilder sqlBuffer = new  StringBuilder();
    	sqlBuffer.append(sql);
    	sqlBuffer.append(flagSQL);
    	totalNum = getTotalNum(sqlBuffer.toString()); //总记录数
        List<Map<String,Object>> dataList = GetdoWriterLine(sqlBuffer.toString(),dataFields);
        //直接输出为txt
        for(int i=0;i<dataList.size();i++){
	    	for(int m=0;m<outs.size();m++){
			     command = new TxtCommand<Object>(outs.get(m), dataParserConfig, dataList.get(i));
			     command.execute();
	    	}
	    }
        //logger.info(new Date()+"共输出数据"+totalNum+"条记录");
        //写日志表
		InziDbUtils.getInstance().updateIN_zidbCHUCTS(dataParserConfig.getReaderConfig().getDatasourceId(),interfaceCode,totalNum,0);
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
	 * 得到数据库数据总条数
	 * @param rs
	 * @param dataField
	 * @return
	 * @throws SQLException
	 */
	private int getTotalNum(String sql){
		StringBuffer strbuf = new StringBuffer();
		int countNum = 0;
		try {
			strbuf.append(" select count(1) countNum from ("+ sql+")");
			countNum = Integer.valueOf(DbUtils.selectValue(strbuf.toString(),conn).toString());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return countNum;
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
	
	/**
	 * 得到并解析文件行
	 * @author 贺志国
	 * @date 2013-1-10
	 * @param sql 增量执行的SQL
	 * @param dataFields 配置文件需输出的字段集
	 * @return 查询到的结果集
	 * @throws SQLException
	 */
	public List<Map<String,Object>> GetdoWriterLine(String sql,DataField[] dataFields) throws SQLException{
		Statement cs = conn.createStatement();
		ResultSet rs = cs.executeQuery(sql);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		while(rs!=null&&rs.next()){
			Map<String,Object> result = new HashMap<String,Object>();
			Object value;
			for(DataField dataField:dataFields){
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
     * 生成带有增量条件的sql
     * 	 and to_char(edit_time, 'yyyymmddHH24MIss') between
     (SELECT to_char(i.lastcpltime, 'yyyymmddHH24MIss')
     FROM IN_ZIDB i
     WHERE i.inbh = 'id1') and to_char(sysdate, 'yyyymmddHH24MIss')
     * @return
     */
    protected String makeIncrementSql() {
        //1：查找接口总表数据库,拿出此接口的 完成时间
        //2: 以此完成时间，和当前时间做条件 生成串
        //3: 更新此接口总表 此接口的完成时间，上上次完成时间
        StringBuffer sb = new StringBuffer();
        String nowTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        sb.append(" AND TO_CHAR(EDIT_TIME, 'yyyymmddHH24MIss') BETWEEN ");
        sb.append(" (SELECT TO_CHAR(i.LASTCPLTIME, 'yyyymmddHH24MIss') ");
        sb.append(" FROM IN_ZIDB i ");
        sb.append(" WHERE i.INBH = '"+dataParserConfig.getId()+"') and '"+nowTime+"' ");

        return sb.toString();
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
