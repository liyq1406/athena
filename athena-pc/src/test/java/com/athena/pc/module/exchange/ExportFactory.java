/**
 * 
 */
package com.athena.pc.module.exchange;

import java.io.OutputStream;
import java.util.Map;

import com.toft.core3.container.annotation.Component;

/**
 * @author dsimedd001
 *
 */
@Component
public class ExportFactory {

	public final static String EXPORT_TYPE_XLS="xls"; 
	public final static String EXPORT_TYPE_TXT="txt";
	
	/**
	 * 
	 * 
	 * @param export 导出类型
	 * @param jsonResult  json数据
	 * @param writer writer
	 * @return
	 */
	public void export(String exportType, 
			ExporterHeadInfo headerInfo,
			Map<String, Object> jsonResult, 
			Map<String,String> ecMap) {
		if(exportType==null)return;//快速返回
		Exporter exporter = null;
		exporter = new XlsExporter(headerInfo,jsonResult,ecMap);
		exporter.executeOut();
	}


}
