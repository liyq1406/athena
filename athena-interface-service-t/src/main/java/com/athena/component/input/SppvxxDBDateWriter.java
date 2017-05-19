package com.athena.component.input;


import org.apache.log4j.Logger;

import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.toft.core2.dao.database.DbUtils;

public class SppvxxDBDateWriter extends DbDataWriter{
    private String datasourceid;
	protected static Logger logger = Logger.getLogger(SppvxxDBDateWriter.class);	//定义日志方法
	public SppvxxDBDateWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		datasourceid=dataParserConfig.getReaderConfig().getDatasourceId();
		
	}
	
	
	
	
	/**
	 * 接口处理完成后更新SPPV ATHENA001表的状态为'T'(已处理状态)
	 */
	@Override
	public void after() {
        StringBuffer strbuf=new StringBuffer();
        try{
        strbuf.append("update ATHENA001 set STATE='T' where STATE='F'");
		DbUtils.execute(strbuf.toString(),datasourceid);
		String sql6 = "update "+SpaceFinal.spacename_ddbh+".ddbh_sppvxx set shengcx=usercenter||'5L'||substr(zongzlsh,1,1) where wuld='6000' and length(shengcx) <> 5 and flag is null";
		super.execute(sql6);
		String sql2 = "update "+SpaceFinal.spacename_ddbh+".ddbh_sppvxx set shengcx=usercenter||'2L'||substr(zongzlsh,1,1) where wuld='2000' and length(shengcx) <> 5 and flag is null";
		super.execute(sql2);
        }catch(Exception e){
        	logger.error(e.getMessage());
        }
		super.after();
	}

}
