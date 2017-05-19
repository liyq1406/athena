package com.athena.component.input;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

/**
 * 执行层零件仓库输入
 * @author dsimedd001
 *
 */
public class LingjckDbDataWriter extends DbDataWriter{
	
	protected static Logger logger = Logger.getLogger(LingjckDbDataWriter.class);	//定义日志方法
	
	private Date date= new Date();
	
	public LingjckDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	/**
	 * 行记录解析之后   给行记录增加创建人、创建时间、修改人、修改时间,并将时间类型数据格式转化成【yyyy-MM-dd HH:mm:ss】形式.
	 * */
	public void afterRecord(int rowIndex, Record record, Object line){
		try {
			//暂时将接口传入的数据创建者取名叫temp
			//record.put("CREATOR", "interface");
			record.put("CREATOR", super.dataExchange.getCID());
			record.put("CREATE_TIME", date);
			record.put("EDITOR", "temp");
			record.put("EDIT_TIME", date);
			record.put("shengxsj",DateTimeUtil.StringYMDToDate(record.getString("shengxsj")));
			super.afterRecord(rowIndex, record, line);
		} catch (Exception e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
		
	}
	/**
	 * mantis 0004753
	 */
	public void after() {
		//删除本次接口传递以外的数据
		String sql1 = "delete from  "+SpaceFinal.spacename_ck+".ckx_lingjck t  where t.editor != 'temp' or t.editor is null";
		updateTable(sql1);
		//将剩余数据的修改人改为interface
		String sql = "update "+SpaceFinal.spacename_ck+".ckx_lingjck d set d.editor = '2160' where d.editor = 'temp'";
		updateTable(sql);
		super.after();
	}
	
	
	/**
	 * 更新表信息
	 * @param rowObject
	 */

	private void updateTable(String sql) {
		try{
			
			
			
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.executeUpdate();
			conn.commit();
		}catch(SQLException e){
	         logger.error(e.getMessage());
		}	
		
	}
}
