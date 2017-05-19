/**
 * 
 */
package com.athena.component.exchange.config;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
public class ExchangerConfig {
	private DbConfig dbConfig;//数据库配置
	
	private String filePath;//输入文件路径
	
	private String exchangerClass;//交换数据读取类
	
	private String split;//文本分割符
	
	private String quote;//文本引用符
	
	private String sql;//数据查询sql
	
	private String table;//数据插入数据表
	
	private String idKeys;//主键字段，使用，号分隔多个字段
	
	private String datasourceId;//系统配置的数据库
	
	private int runnerPoolSize;//线程池数目
	
	private String encoding;//编码
	
	//改造 --> 输出：支持输出 生成多文件  
	private String fileName;// 文件名称
	
	//为了实现  文件读取的错误 保存文件
	private String errorFilePath; //错误文件的保存路径
	//对处理完的文件 进行备份
	private String backupFilePath; //备份文件保存路径
	
	private String isGoOn; //文件处理表示  文件解析失败  或者有错误的地方 是否继续执行下面的文件，true：继续执行下面的文件解析，false：不执行下面文件的解析
	
	
	private String isTask;//是否为子类任务
	
	private String athfilePath;//移动目的地文件路径
	
	public String getIsTask() {
		return isTask;
	}
	public void setIsTask(String isTask) {
		this.isTask = isTask;
	}
	
	
	//支持输出 按照用户中心生成文件
	private String usercenter;
	//支持输出 是否增量查询的标识
	private String isAllSet;

	//2012-08-24 剔除空格  true/false
	private String isTrim;
	
	//2012-08-30 是否做更新
	private String isUpdate;

	private String isGoOnOut; //继续输出，不新建文件，true：继续输出，不新建文件，false：删除原有文件，新建文件输出
	
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
	public String getIsTrim() {
		return isTrim;
	}
	public void setIsTrim(String isTrim) {
		this.isTrim = isTrim;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getIsAllSet() {
		return isAllSet;
	}
	public void setIsAllSet(String isAllSet) {
		this.isAllSet = isAllSet;
	}
	public String getIsGoOn() {
		return isGoOn;
	}
	public void setIsGoOn(String isGoOn) {
		this.isGoOn = isGoOn;
	}
	public String getErrorFilePath() {
		return errorFilePath;
	}
	public void setErrorFilePath(String errorFilePath) {
		this.errorFilePath = errorFilePath;
	}
	public String getBackupFilePath() {
		return backupFilePath;
	}
	public void setBackupFilePath(String backupFilePath) {
		this.backupFilePath = backupFilePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getRunnerPoolSize() {
		return runnerPoolSize;
	}
	public void setRunnerPoolSize(int runnerPoolSize) {
		this.runnerPoolSize = runnerPoolSize;
	}
	public int getRunnerGroup() {
		return runnerGroup;
	}
	public void setRunnerGroup(int runnerGroup) {
		this.runnerGroup = runnerGroup;
	}
	private int runnerGroup;//每组线程个数
	public String getIdKeys() {
		return idKeys;
	}
	public void setIdKeys(String idKeys) {
		this.idKeys = idKeys;
	}
	public DbConfig getDbConfig() {
		return dbConfig;
	}
	public void setDbConfig(DbConfig dbConfig) {
		this.dbConfig = dbConfig;
	}
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getExchangerClass() {
		return exchangerClass;
	}

	public void setExchangerClass(String exchangerClass) {
		this.exchangerClass = exchangerClass;
	}

	public String getSplit() {
		return split;
	}

	public void setSplit(String split) {
		this.split = split;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getIsGoOnOut() {
		return isGoOnOut;
	}
	public void setIsGoOnOut(String isGoOnOut) {
		this.isGoOnOut = isGoOnOut;
	}
	public String getAthfilePath() {
		return athfilePath;
	}
	public void setAthfilePath(String athfilePath) {
		this.athfilePath = athfilePath;
	}

	
}
