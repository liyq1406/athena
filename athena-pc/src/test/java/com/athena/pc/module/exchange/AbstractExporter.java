/**
 * 
 */
package com.athena.pc.module.exchange;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.athena.component.utils.PropertyUtils;

/**
 * @author dsimedd001
 *
 */
public abstract class AbstractExporter<T> implements Exporter {

	
	protected Map<String, Object> jsonResult;
	
	protected ExporterHeadInfo headInfo;
	
	protected Map<String, String> ec;

	public AbstractExporter(ExporterHeadInfo headInfo,Map<String, Object> jsonResult,Map<String, String> ecMap) {
		this.jsonResult = jsonResult;
		this.headInfo = headInfo;
		this.ec = ecMap;
	}
	
	public abstract void execute();

	@SuppressWarnings("unchecked")
	protected void processJsonResult(T container){
		int num0 = 0;
		int num1 = 1;
		int num2 = 2;
		int num3 = 3;
		
		String type = ec.get("type")==null ?"":ec.get("type");
		if("2".equals(type)){
			num0 = 0;
			num1 = 0;
			num2 = 1;
			num3 = 2;
		}
		
		int col=0;
		//表头处理
		String msg = ec.get("msg")==null ?"":ec.get("msg");
		if(!"0000".equals(msg)){
			addHeadCell(container,num0,0,msg);
		}
		for(String header:headInfo.getHeaders()){
			addHeadCell(container,num1,col++,header);
		}
		col = 0;
		if(headInfo.getPropertiesshow()!= null && headInfo.getPropertiesshow().length>0){
			for(String property:headInfo.getPropertiesshow()){
				addHeadCell(container,num2,col++,property);
			}
		}else{
			for(String property:headInfo.getProperties()){
				addHeadCell(container,num2,col++,property);
			}
		}
		
		//数据处理
		//TODO 数据格式有问题的情况
//		Map<String,Object> result = 
//			(Map<String, Object>) jsonResult.get("result");
		List<Object> rowDatas = (List<Object>) jsonResult.get("rows");
		
		for(int row=0;row<rowDatas.size();row++){
			col = 0;
			for(String property:headInfo.getProperties()){
//				Object value = 
//					PropertyUtils.getPropertyValue(rowDatas.get(row), property);
				Map<String,String> temp = (Map<String,String>)rowDatas.get(row);
				Object value = temp.get(property);
				if(value==null){
					value = "";
				}
				addCell(container,row+num3,col++,value.toString());
			}
		}
	}

	/**
	 * 添加导出表头单元格
	 * @param container
	 * @param row
	 * @param col
	 * @param context
	 */
	abstract void addHeadCell(T container, int row, int col, String context);
	
	/**
	 * 添加数据单元格
	 * @param container
	 * @param row
	 * @param col
	 * @param context
	 */
	abstract void addCell(T container, int row, int col, String context);
	
}
