package com.athena.xqjs.module.utils.xls.kanbyhl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 解析器接口
 * @author chenlei
 * @vesion 1.0
 * @date 2012-5-17
 */
public class XlsHandlerlingjjzxh {
	private InputStream in;
	private String jsonConfig;
	private String editor;  //导入人
	private Map<String,List<Kanbxhgm>> map;
	private Map<String,Object> pmap;
	private AbstractIBatisDao baseDao;//数据源
	
	public Map<String,List<Kanbxhgm>> getMap() {
		return map;
	}

	public void setMap(Map<String,List<Kanbxhgm>> map) {
		this.map = map;
	}
	
	
	public Map<String, Object> getPmap() {
		return pmap;
	}

	public void setPmap(Map<String, Object> pmap) {
		this.pmap = pmap;
	}

	public String getEditor() {
		return editor;
	}
	
	
	public AbstractIBatisDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(AbstractIBatisDao baseDao) {
		this.baseDao = baseDao;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	
	private XlsHandlerlingjjzxh() {
		super();
	}

	public static XlsHandlerlingjjzxh getInstance(InputStream in,String jsonConfig,String editor,Map<String,List<Kanbxhgm>> map,AbstractIBatisDao baseDao){
		XlsHandlerlingjjzxh handler = new XlsHandlerlingjjzxh();
		handler.setIn(in);
		handler.setJsonConfig(jsonConfig);
		handler.setEditor(editor);
		handler.setMap(map);//map
		handler.setBaseDao(baseDao);
		return handler;
	}
	
	/**
	 * 执行解析xls文件
	 */
	public String doXlsHandler(){
		
		StringBuffer errorMessage = new StringBuffer(); //错误信息
		try {
			
			//Workbook workbook = Workbook.getWorkbook(in);
			Document document = getDocument();
			
			//解析jsonConfig 遍历
			JSONArray jsonArry = JSONArray.fromObject(jsonConfig);
			for(int i=0;i<jsonArry.size();i++){
				String sheet = jsonArry.getJSONObject(i).getString("sheet");
				String table= jsonArry.getJSONObject(i).getString("table");
				String datasourceId = jsonArry.getJSONObject(i).getString("datasourceId");
				String clazz  = jsonArry.getJSONObject(i).getString("clazz");
				String keys = jsonArry.getJSONObject(i).getString("keys");
				String dateColumns = jsonArry.getJSONObject(i).getString("dateColumns");
				String dateFormats = jsonArry.getJSONObject(i).getString("dateFormats");
				//对每个sheet进行解析入库
				//String error = SheetHandlerFactory.getInstance(workbook,sheet,table,datasourceId,clazz,keys,dateColumns,dateFormats).doSheetHandler();
				String error = SheetHandlerFactorylingjjzxh.getInstance(document,sheet,table,datasourceId,clazz,keys,dateColumns,dateFormats).doSheetHandler(editor,map,baseDao);
				//填充错误信息
				if(error==null || "".equals(error)){
				}else{
					errorMessage.append(error);
				}
			}
			
		} catch (DocumentException e) {
			errorMessage.append("导入文件格式有误,解析失败.");
		} catch (Exception e) {
			errorMessage.append("导入数据有误.");
		}
		//返回错误信息
		return errorMessage.toString();	
	}
	
	/**
	 * 做为xml文件来加载文件流
	 * @return
	 * @throws DocumentException
	 */

	private Document getDocument() throws DocumentException{
		Document document = null;
			//做为xml处理
		SAXReader reader = new SAXReader();			
		Map<String,String> map = new HashMap<String,String>();
		map.put("xmlns", "urn:schemas-microsoft-com:office:spreadsheet");
		reader.getDocumentFactory().setXPathNamespaceURIs(map);
		document =  reader.read(in);
		return document;
	}
	
	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}

	public String getJsonConfig() {
		return jsonConfig;
	}

	public void setJsonConfig(String jsonConfig) {
		this.jsonConfig = jsonConfig;
	}

}
