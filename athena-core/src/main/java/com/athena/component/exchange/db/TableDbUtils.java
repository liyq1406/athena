/**
 * 
 */
package com.athena.component.exchange.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
public class TableDbUtils {
	
	
	public synchronized  int saveTable(TableRecord tableRecord,
			PreparedStatement insertPs,PreparedStatement updatePs) throws SQLException{
		int updateCount = 0;

		if(updatePs!=null){//更新数据
			updateCount = execute(updatePs,generateUpdateValues(tableRecord));
		}
		if(updateCount==0){//插入数据
			execute(insertPs,generateInsertValues(tableRecord));
		}
		return updateCount;
	}
	
	private  int execute(PreparedStatement ps,List<Object> values) throws SQLException{
		int i = 0;
		for(Object value:values){
			ps.setObject(++i, valueConvert(value));
		}
		return ps.executeUpdate();
	}
	
	public  List<Object> generateInsertValues(TableRecord tableRecord){
		List<Object> values = new ArrayList<Object>();
		for(String field:tableRecord.getFields()){
			values.add(tableRecord.get(field));
		}
		return values;
	}
	
	
	public  List<Object> generateUpdateValues(TableRecord tableRecord){
		List<Object> values= new ArrayList<Object>();
		for(String field:tableRecord.getUpdateFields()){
			values.add(tableRecord.getValue().get(field));
		}
		for(String idKey:tableRecord.getIdKeys()){
			values.add(tableRecord.getValue().get(idKey)==null?"":tableRecord.getValue().get(idKey));
		}
		return values;
	}
	
	public static Object valueConvert(Object value){
		if(value==null)return "";
		if(value instanceof java.util.Date){
			return new java.sql.Timestamp(((java.util.Date)value).getTime());
		}
		return value;
	}
}

class ExecuteTable{
	private String sql;
	
	private List<Object> values;
	
	
	public ExecuteTable(String sql, List<Object> values) {
		this.sql = sql;
		this.values = values;
	}


	public String getSql() {
		return sql;
	}


	public void setSql(String sql) {
		this.sql = sql;
	}


	public List<Object> getValues() {
		return values;
	}


	public void setValues(List<Object> values) {
		this.values = values;
	}
	
}
