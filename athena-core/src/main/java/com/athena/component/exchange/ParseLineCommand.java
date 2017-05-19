/**
 * 
 */
package com.athena.component.exchange;

import com.athena.component.exchange.field.DataField;
import com.athena.component.exchange.utils.ConvertUtils;
import com.athena.component.runner.Command;

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
public abstract class ParseLineCommand<T>  extends Command<Record>{
	
	protected DataWriter dataWriter;
	
	protected int rowIndex;
	
	protected T rowObject;
	
	protected Record record;

	public ParseLineCommand(DataWriter dataWriter,
			int rowIndex, T rowObject) {
		this.dataWriter = dataWriter;
		this.rowIndex = rowIndex;
		this.rowObject = rowObject;
	}

	@Override
	public void execute() {
		//行解析前
		if(!dataWriter.beforeRecord(rowIndex,rowObject))return;
		record = buildRecord();
		//行解析后
		dataWriter.afterRecord(rowIndex,record,rowObject);
		
	}
	
	public abstract Record buildRecord();
	
	public Record result(){
		return record;
	}
	
	/**
	 * 值转换
	 * @param dataField
	 * @param strValue
	 * @return
	 */
	protected Object convertValue(DataField dataField, String strValue) throws ParserException {
		return ConvertUtils.convertValue(dataField,strValue);
	}
}
