package com.athena.component.input;


import org.apache.log4j.Logger;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.component.exchange.utils.ConvertUtils;
import com.toft.core2.DBException;
import com.toft.core2.dao.database.DbUtils;

public class GdtbxqDBDataWriter extends DbDataWriter{
	private String datasourceid;
	protected static Logger logger = Logger.getLogger(GdtbxqDBDataWriter.class);	//定义日志方法
	public GdtbxqDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		datasourceid=dataParserConfig.getReaderConfig().getDatasourceId();
	}
	
	
	@Override
	public void afterRecord(int rowIndex, Record record,Object line){
		String xiaohdbh = queryXiaohdbhOfpeislb(record);
		String danw = queryDanwOfLingj(record);
		record.put("xiaohdbh", "".equals(xiaohdbh)?"":xiaohdbh.substring(0,9));
		record.put("danw", danw);
		record.put("lmp", record.getString("usercenter")+"5"+record.getString("lmp"));
		super.afterRecord(rowIndex, record, line);
	}
	
	/**
	 * 查询配送类别获取消耗点编码
	 * @author 贺志国
	 * @date 2013-2-27
	 * @param record
	 * @return
	 */
	private String queryXiaohdbhOfpeislb(Record record) {
		StringBuilder buffer = new StringBuilder();
		String xiaohdbh = "";
		buffer.append("select p.shangxd from "+SpaceFinal.spacename_ck+".ckx_peislb p where p.peislx= ");
		buffer.append("(select t.peislx from "+SpaceFinal.spacename_ck+".ckx_tongbjplj t where  ");
		buffer.append("t.usercenter='"+record.getString("usercenter")+"' " );
		buffer.append("and t.lingjbh='"+record.getString("product")+"' ");
		buffer.append("and t.shengcxbh='"+record.getString("usercenter")+"5"+record.getString("lmp")+"' ");
		buffer.append("and t.nclass='"+record.getString("oclass")+"')");
		try {
			xiaohdbh = ConvertUtils.strNull(DbUtils.selectValue(buffer.toString(), dataParserConfig.getWriterConfig().getDatasourceId()));
		} catch (DBException e) {
			logger.error(e.getMessage());
		}
		return xiaohdbh;
	}
	
	/**
	 * 查询参考系零件表ckx_lingj取零件单位
	 * @author 贺志国
	 * @date 2013-2-27
	 * @param record
	 * @return
	 */
	private String queryDanwOfLingj(Record record){
		StringBuilder buffer = new StringBuilder();
		String danw = "";
		buffer.append("select p.danw from "+SpaceFinal.spacename_ck+".ckx_lingj p where ");
		buffer.append("p.usercenter='"+record.getString("usercenter")+"' " );
		buffer.append("and p.lingjbh='"+record.getString("product")+"' ");
		try {
			danw = ConvertUtils.strNull(DbUtils.selectValue(buffer.toString(), dataParserConfig.getWriterConfig().getDatasourceId()));
		} catch (DBException e) {
			logger.error(e.getMessage());
		}
		return danw;
	}


	/**
	 * 更新sppv sppv034表JSBS为'T'(已处理状态)
	 */
	@Override
	public void after() {
		try{
		String sql="update sppv034 set JSBS='T' where JSBS='F'";
		DbUtils.execute(sql, datasourceid);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		super.after();
	}
	

}
