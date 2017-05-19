package com.athena.component.exchange.txt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import org.apache.log4j.Logger;

import com.athena.component.exchange.CancellableTask;
import com.athena.component.exchange.FileLog;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.TableDbUtils;
import com.athena.component.exchange.db.TableRecord;
import com.athena.util.exception.ServiceException;
import com.toft.utils.UUIDHexGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: issuser
 * Date: 13-1-15
 * Time: 下午4:53
 *
 */
public class TxtWriterDBTask implements CancellableTask {
    protected final Logger logger=Logger.getLogger(TxtWriterDBTask.class);//定义日志方法
    protected String interfaceId;
    protected String fileName;
    protected List<Record> lineList;
    protected Connection connection;
    protected final DataParserConfig dataParserConfig;
    protected List<String> fieldList = new ArrayList<String>();
    protected List<String> updateFieldList = new ArrayList<String>();
    protected TableDbUtils tdu;
    protected String datasourceId = "";

    //错误信息
    private ArrayList<Map<String,String>> errorMessageList = new ArrayList<Map<String,String>>();
    
    public ArrayList<Map<String, String>> getErrorMessageList() {
		return errorMessageList;
	}
	public void setErrorMessageList(ArrayList<Map<String, String>> errorMessageList) {
		this.errorMessageList = errorMessageList;
	}
    
    private PreparedStatement insertPs;

    private PreparedStatement updatePs;
    /**
     * 构造txt输入db任务
     * @param dataParserConfig 接口配置
     * @param fieldList        字段列表
     * @param updateFieldList  更新字段列表
     * @param fileName         接口文件
     * @param lineList         解析的数据列表
     */
    public TxtWriterDBTask(DataParserConfig dataParserConfig, List<String> fieldList, List<String> updateFieldList, 
    		String fileName, List<Record> lineList){
        this.dataParserConfig = dataParserConfig;
        this.interfaceId = dataParserConfig.getId();
        this.fieldList = fieldList;
        this.updateFieldList = updateFieldList;
        this.fileName = fileName;
        this.lineList = lineList;
        tdu = new TableDbUtils();
		datasourceId = dataParserConfig.getWriterConfig().getDatasourceId();
    }
    /**
     * 当线程中断时，需要调用此方法
     */
    @Override
    public void cancel() {
        close();
    }

    private void close() {
    	//释放List资源 2016.10.9 
    	lineList = null;
        if (this.updatePs != null) {
            try {
                this.updatePs.close();
                //logger.info("线程--接口" + interfaceId + " 关闭update PreparedStatement");
            } catch (SQLException e) {
                logger.error("线程--接口" + interfaceId + " 关闭update PreparedStatement异常" + e.getMessage());
            }
        }
        if (this.insertPs != null) {
            try {
                this.insertPs.close();
                //logger.info("线程--接口" + interfaceId + " 关闭insert PreparedStatement");
            } catch (SQLException e) {
                logger.error("线程--接口" + interfaceId + " 关闭insert PreparedStatement异常" + e.getMessage());
            }
        }
        if (this.connection != null) {
            try {
               this.connection.close();  
               //logger.info("线程--接口" + interfaceId + " 关闭connection");
            } catch (SQLException e) {
                logger.error("线程--接口" + interfaceId + " 关闭connection异常" + e.getMessage());
            }
        }
    }

    /**
     * @return 一个 RunnableFuture，在运行的时候，它将调用底层可调用任务，
     *         作为 Future 任务，它将生成可调用的结果作为其结果，并为底层任务提供取消操作。
     */
    @SuppressWarnings({"unchecked","finally" })
	@Override
    public RunnableFuture<?> newTask() {
        return new FutureTask<Boolean>(this) {
			public boolean cancel(boolean mayInterruptIfRunning) {
                try {
                    TxtWriterDBTask.this.cancel();
                } finally {
                    return super.cancel(true);
                }
            }
        };
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Boolean call() throws Exception {
    	//int i= 0;
        Boolean result = new Boolean(false);
        TableRecord tableReocrd = new TableRecord();
        tableReocrd.setTableName(dataParserConfig.getWriterConfig().getTable());
        tableReocrd.setFields(fieldList.toArray(new String[fieldList.size()]));
        tableReocrd.setUpdateFields(updateFieldList.toArray(new String[updateFieldList.size()]));
        tableReocrd.setIdKeys(dataParserConfig.getWriterConfig().getIdKeys().split(","));
        before();
        logger.info("线程--接口" + interfaceId + "文件" + fileName +"开始解析数据");
        initConnection();
        for(Record record : lineList) {
        	//logger.info("线程--接口" + interfaceId + "文件" + fileName + "解析第" + record.getLineNum() + "行数据");
        	//logger.info("**************************************************"+i++);
        	try {
        		if(!beforeRecord(record)){
        			//logger.info("测试是否进入：***********************");
        			continue;
        		}
        		exec(tableReocrd, record); 
        	} catch (SQLException sqlEx) {
        		logger.error("线程--接口" + interfaceId + "解析第" + record.getLineNum() + "行数据"
        				+ " 执行sql异常" + sqlEx.getMessage());
        		//判断是否需要抛出异常通知autosys
        		runSqlException(sqlEx,record);

        	} catch (WarmBusinessException warm) {
        		this.dataParserConfig.getErrorCount().incrementAndGet();
        		logger.error(warm.getMessage(), warm);
        	}
        }
        result = true;
        close();
        return result;
    }
    
    public void exec(TableRecord tableReocrd, Record record) throws SQLException {
		// TODO Auto-generated method stub
    	tableReocrd.setRecord(record.getValue());
		int updateCount = tdu.saveTable(tableReocrd,insertPs,updatePs);
		if (updateCount == 0) {
			this.dataParserConfig.getInsertCount().incrementAndGet();
			//logger.info("线程--接口" + interfaceId + "文件" + fileName + "解析第" + record.getLineNum() + "行数据插入结束");
			//logger.info("测试是否进入：***********************插入");
		} else {
			this.dataParserConfig.getUpdateCount().incrementAndGet();
			//logger.info("线程--接口" + interfaceId + "文件" + fileName + "解析第" + record.getLineNum() + "行数据更新结束");
			//logger.info("测试是否进入：***********************更新");
		}
	}
	/**
     * 判断是否需要抛出异常通知autosys
     * @author Hezg
     * @date 2013-2-19
     */
    public void runSqlException(SQLException sqlEx,Record record){
    	String message = sqlEx.getMessage();
        String [] oraStr = message.split(":");
        if("ORA-00957".equals(oraStr[0])||"ORA-00904".equals(oraStr[0])
        		||"ORA-00942".equals(oraStr[0])||"ORA-12899".equals(oraStr[0])){//可判断的SQL异常，报1，写日志数据库
        	 //错误日志记录
            in_errorfile_info(record,sqlEx.getMessage());
        	throw new ServiceException("线程--接口" + interfaceId + "解析第" + record.getLineNum() + "行数据"
                    + " 执行sql异常" + sqlEx.getMessage(), sqlEx);
        }else if("ORA-00001".equals(oraStr[0])||"ORA-01400".equals(oraStr[0])
        		||"ORA-01861".equals(oraStr[0])||"ORA-01847".equals(oraStr[0])){//可忽略SQL异常，写错误日志文件，潘瑞说也要写表，加写表日志 hzg 2013-3-22
        	logger.error("线程--接口" + interfaceId + "解析第" + record.getLineNum() + "行数据"
                    + " 执行sql异常" + sqlEx.getMessage(), sqlEx);
        	 in_errorfile_info(record,sqlEx.getMessage());
        }else{ //其他不可判断的SQL异常也报1，写日志数据库
        	 //错误日志记录
            in_errorfile_info(record,sqlEx.getMessage());
        	throw new ServiceException("线程--接口" + interfaceId + "解析第" + record.getLineNum() + "行数据"
                    + " 执行sql异常" + sqlEx.getMessage(), sqlEx);
        }
    }
    
    /**
     * 钩子方法
     */
    public boolean beforeRecord(Record record) { 
		return true;
	}
    
    /**
     * 钩子方法
     * @author Hezg
     * @date 2013-1-30
     * @return
     */
    public void before(){
    	
    }
    
    /**
     * 钩子方法
     * @author hzg
     * @date 2013-3-4
     * @return
     */
    public void after(){
    	
    }
    
    /**
	 * 向 接口文件错误记录信息 表中 记录日志
	 * @date 2013-2-18
	 * @param errorNameMap  错误文件名
	 * @param errorMeList 错误信息集合
	 */
    private void in_errorfile_info(Record record,String error_date) {
    	Map<String,String> params = new HashMap<String,String>();
    	params.put("EID", UUIDHexGenerator.getInstance().generate());
    	params.put("SID", "");
    	params.put("INBH", interfaceId);
    	params.put("file_errorinfo",ObjStr(record.getValue()));
    	params.put("error_date", ObjStr(error_date));
    	if(!"".equals(datasourceId)&&Integer.parseInt(datasourceId)>=4){
    		FileLog.getInstance(dataParserConfig.getReaderConfig().getDatasourceId()).
    		insert_file_ErrorInfo(params,dataParserConfig.getBaseDao());
    	}else{
    		FileLog.getInstance(datasourceId).insert_file_ErrorInfo(params,dataParserConfig.getBaseDao());
    	}
    		
	}
    
    
	private void initConnection() throws SQLException {
        this.connection = dataParserConfig.getBaseDao().getSdcDataSource(datasourceId).getConnection();
        logger.info("当前连接为："+this.connection);
        try {
            this.connection.setAutoCommit(true);
            logger.debug("线程--接口" + interfaceId + "开始执行Insert SQL，" + dataParserConfig.getInsertSql());
            insertPs = this.connection.prepareStatement(dataParserConfig.getInsertSql());

            //2012-08-30 仓库一接口不做更新
            if("false".equals(dataParserConfig.getWriterConfig().getIsUpdate())){
                updatePs= null;
            }else{
                logger.debug("线程--接口" + interfaceId + "开始执行Update SQL，" + dataParserConfig.getUpdateSql());
                updatePs = this.connection.prepareStatement(dataParserConfig.getUpdateSql());
            }

        } catch (SQLException e) {
            logger.error("线程--接口" + interfaceId + " 执行sql异常" + e.getMessage());
            throw new RuntimeException("线程--接口" + interfaceId + " 执行sql异常" + e.getMessage());
        }
    }
	
	/**
	 * 对象为null时为空
	 * @param obj
	 * @return
	 */
	public String ObjStr(Object obj){
		return obj==null?"":obj.toString();
		
	}
	
	/**
	 * 生成UUID
	 * @return String
	 */
	public static String getUUID(){
		return UUIDHexGenerator.getInstance().generate();
	}
	
}
