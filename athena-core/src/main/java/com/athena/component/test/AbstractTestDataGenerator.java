/**
 * 
 */
package com.athena.component.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.toft.core3.util.ResourceUtils;

/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public abstract class AbstractTestDataGenerator implements TestDataGenerator {
	protected final Log logger = LogFactory.getLog(getClass());
	
	protected String[] locations;
	
	protected List<InsertParameter> insertParameters;
	
	public AbstractTestDataGenerator(String[] locations) {
		this.locations = locations;
		
		List<InsertParameter> insertParameters 
			= new ArrayList<InsertParameter>();
		for(String location:locations){
			insertParameters.addAll(parseSource(location));
		}
		
		this.insertParameters = insertParameters;
	}
	
	/* 
	 * 清除
	 * (non-Javadoc)
	 * @see com.athena.component.test.TestDataGenerator#getClearParams()
	 */
	public List<String> getClearTables(){
		List<String> tableNames = new ArrayList<String>();
		for(InsertParameter insertParameter:insertParameters ){
			tableNames.add(insertParameter.getTableName());
		}
		return tableNames;
	}
	
	public void visitTable(RowCallBack callBack){
		for(InsertParameter insertParameter:insertParameters ){
			try {
				callBack.doRow(insertParameter);
			} catch (Exception e) {
				logger.error(""+e.getLocalizedMessage());
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	
	public Object getInsertParams(){
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("inserts", insertParameters);
		return params;
	}
	
	/**
	 * 资源解析
	 * @param location
	 */
	private List<InsertParameter> parseSource(String location) {
		File resource = null;
		try {
			resource = ResourceUtils.getFile(location);
		} catch (FileNotFoundException e) {
			logger.error("file not found,["+location+"]");
		}
		
		List<InsertParameter> insertParameters = null;
		if(resource!=null){
			insertParameters = generateInsertParameter(resource);
		}
		return insertParameters==null?new ArrayList<InsertParameter>():insertParameters;
	}

	/**
	 * 数据类型转换
	 * @param content
	 * @param dataType
	 * @return
	 */
	protected String convertValue(String content, String dataType) {
		String result;
		if(content==null){
			result = "";
		}else if("string".equals(dataType)){
			result = "'"+content.trim()+"'";
		}else if("date".equals(dataType)){
			String insertValue = content.trim();
			if(insertValue.length()==10){
				insertValue = insertValue+" 00:00:00";
			}
			result = "to_date('"+insertValue+"','yyyy-mm-dd hh24:mi:ss')";
		}else if("timestamp".equals(dataType)){
			result =  "to_timestamp('"+content.trim()+"','yyyy-mm-dd hh24:mi:ss')";
		}else{
			result = content;
		}
		return result;
	}
	/**
	 * @param file
	 * @return
	 */
	public abstract List<InsertParameter> generateInsertParameter(File file);

	public String[] getLocations() {
		return locations;
	}

	public void setLocations(String[] locations) {
		this.locations = locations;
	}
	
}
