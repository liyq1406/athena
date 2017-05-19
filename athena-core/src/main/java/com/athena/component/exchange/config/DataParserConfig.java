/**
 * 
 */
package com.athena.component.exchange.config;

import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.athena.component.exchange.field.DataField;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * @author Administrator
 *
 */
public class DataParserConfig {
	private String insertSql;

    private String updateSql;

	private DataField[] dataFields;//字段
	
	private AbstractIBatisDao baseDao;
	
	private GroupConfig groupConfig;//组配置
	
	private ExchangerConfig readerConfig;//读取配置
	
	private ExchangerConfig writerConfig;//写入配置
	
	private OutputStreamWriter out; //输出流
	
	private String id;
	
	//DB到xml  xss 2015.12.29 
	private String caption;
	
	private Map<String,String>  fileUcMap ; //文件名，用户中心map  hzg 2014.3.22
	
	private List<String> inputFileList ; //输入文件列表 ,{文件名1,文件名2} hzg 2014.10.20
	
	private String usercenter;

    private AtomicInteger insertCount = new AtomicInteger(); //插入条数

    private AtomicInteger updateCount = new AtomicInteger(); //更新条件
    
    private AtomicInteger errorCount = new AtomicInteger(); //错误条数

    public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public Map<String, String> getFileUcMap() {
		return fileUcMap;
	}

	public void setFileUcMap(Map<String, String> fileUcMap) {
		this.fileUcMap = fileUcMap;
	}

	public List<String> getInputFileList() {
		return inputFileList;
	}

	public void setInputFileList(List<String> inputFileList) {
		this.inputFileList = inputFileList;
	}

	public AtomicInteger getInsertCount() {
        return insertCount;
    }

    public AtomicInteger getUpdateCount() {
        return updateCount;
    }
    
    public AtomicInteger getErrorCount() {
    	return errorCount;
    }

    public String getInsertSql() {
        return insertSql;
    }
    
	public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public OutputStreamWriter getOut() {
		return out;
	}

	public void setOut(OutputStreamWriter out) {
		this.out = out;
	}

	//改造 --> 输出：支持输出 生成多文件 
	private ExchangerConfig[] writerConfigs; //写入配置组 
	
	public ExchangerConfig[] getWriterConfigs() {
		return writerConfigs;
	}

	public void setWriterConfigs(ExchangerConfig[] writerConfigs) {
		this.writerConfigs = writerConfigs;
	}

	public AbstractIBatisDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(AbstractIBatisDao baseDao) {
		this.baseDao = baseDao;
	}

	/**
	 * @return
	 */
	public DataField[] getDataFields(){
		return this.dataFields;
	}

	public void setDataFields(DataField[] dataFields) {
		this.dataFields = dataFields;
	}

	public GroupConfig getGroupConfig() {
		return groupConfig;
	}

	public void setGroupConfig(GroupConfig groupConfig) {
		this.groupConfig = groupConfig;
	}

	public ExchangerConfig getReaderConfig() {
		return readerConfig;
	}

	public void setReaderConfig(ExchangerConfig readerConfig) {
		this.readerConfig = readerConfig;
	}

	public ExchangerConfig getWriterConfig() {
		return writerConfig;
	}

	public void setWriterConfig(ExchangerConfig writerConfig) {
		this.writerConfig = writerConfig;
	}
	
	//DB到xml  xss 2015.12.29 
    public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	
}
