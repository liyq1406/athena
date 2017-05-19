package com.athena.component.input;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.toft.core2.dao.database.DbUtils;

public class SyhofDBDateWriter extends DbDataWriter{
	private String datasourceid=null;
	protected static Logger logger = Logger.getLogger(SyhofDBDateWriter.class);	//定义日志方法

	public SyhofDBDateWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		datasourceid=dataParserConfig.getReaderConfig().getDatasourceId();
	}
	
	
	
	
	/**
	 * 接口处理完成后更新SPPV ATHENA002表的状态为'T'(已处理状态)
	 * @throws SQLException 
	 */
	@Override
	public void after() {
		StringBuffer strbuf=new StringBuffer();
        try{
        	strbuf.append("update ATHENA002 set JSBS='T' where JSBS='F'");
			DbUtils.execute(strbuf.toString(),datasourceid);
			//进行了in_lcdv_clddxx数据删除
			String sqlDel = "delete "+SpaceFinal.spacename_ddbh+".in_lcdv_clddxx t where t.whof in (select whof from "+SpaceFinal.spacename_ddbh+".in_syof)";
			super.execute(sqlDel);
        }catch(Exception e){
        	logger.error(e.getMessage());
        }
		
		super.after();
	}

}
