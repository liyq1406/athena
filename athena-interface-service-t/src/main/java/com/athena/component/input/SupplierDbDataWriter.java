package com.athena.component.input;

import java.util.Date;

import org.apache.log4j.Logger;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;


/**
 * 供应商参考系接口输入类
 * @author GJ
 * @update hzg 2012-10-23 去掉in_supplier中间表，直接写ckx_gongys表。
 */
public class SupplierDbDataWriter extends DbDataWriter{
	protected static Logger logger = Logger.getLogger(SupplierDbDataWriter.class);	//定义日志方法
	public SupplierDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		
	}

	/**
	 *解析数据之前清空供应商参考系数据
	 */
	@Override
	public boolean before(){
		/*try{
			String sql="delete from "+SpaceFinal.spacename_ckx+".in_supplier";
			super.execute(sql);
		}catch(RuntimeException ex)
		{
			logger.error(ex.getMessage());
		}*/
		
		return super.before();
	}

	/**
	 * 行解析之后处理方法
	 * @param rowIndex 行标
	 * @param record 行数据集合
	 * @author GJ
	 */
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{
			//插入创建时间和处理状态初始数据
			record.put("leix", "2");
			record.put("fayd", "GYS");
			record.put("biaos", "1");
//			record.put("creator", "interface") ;
//			record.put("editor", "interface") ;
			record.put("creator", super.dataExchange.getCID()) ;
			record.put("editor", super.dataExchange.getCID()) ;
			record.put("create_time", new Date()) ;
			record.put("edit_time", new Date()) ;
			super.afterRecord(rowIndex, record, line);
		}catch(Exception e){
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
}
