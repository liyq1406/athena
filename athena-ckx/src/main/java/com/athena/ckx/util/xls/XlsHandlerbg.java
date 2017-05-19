package com.athena.ckx.util.xls;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.toft.core3.ibatis.support.AbstractIBatisDao;

import net.sf.json.JSONArray;

/**
 * 解析器接口
 * @author chenlei
 * @vesion 1.0
 * @date 2012-5-17
 */
public class XlsHandlerbg {
	private InputStream in;
	private String jsonConfig;
	private String editor;  //导入人
	private Map<String,String> map;
	private Map<String,String> gyxhdmap;
	private Map<String,String> xhdmap;
	private Map<String,String> xiaohcmap;
	private Map<String,String> xiaohcbhmap;
	private Map<String,String> lingjmap;
	private AbstractIBatisDao baseDao;//数据源
	
	
	
	
	
	public Map<String, String> getLingjmap() {
		return lingjmap;
	}

	public void setLingjmap(Map<String, String> lingjmap) {
		this.lingjmap = lingjmap;
	}

	public Map<String, String> getXiaohcmap() {
		return xiaohcmap;
	}

	public void setXiaohcmap(Map<String, String> xiaohcmap) {
		this.xiaohcmap = xiaohcmap;
	}

	public Map<String, String> getXiaohcbhmap() {
		return xiaohcbhmap;
	}

	public void setXiaohcbhmap(Map<String, String> xiaohcbhmap) {
		this.xiaohcbhmap = xiaohcbhmap;
	}

	public Map<String, String> getGyxhdmap() {
		return gyxhdmap;
	}

	public void setGyxhdmap(Map<String, String> gyxhdmap) {
		this.gyxhdmap = gyxhdmap;
	}

	public Map<String, String> getXhdmap() {
		return xhdmap;
	}

	public void setXhdmap(Map<String, String> xhdmap) {
		this.xhdmap = xhdmap;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
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

	private XlsHandlerbg() {
		super();
	}

	public static XlsHandlerbg getInstance(InputStream in,String jsonConfig,String editor,Map<String,String> map,Map<String,String> gyxhdmap,Map<String,String> xhdmap,Map<String,String> xiaohcmap,Map<String,String> xiaohcbhmap,Map<String, String>  lingjmap,AbstractIBatisDao baseDao){
		XlsHandlerbg handler = new XlsHandlerbg();
		handler.setIn(in);
		handler.setJsonConfig(jsonConfig);
		handler.setEditor(editor);
		handler.setMap(map);//产线map
		handler.setGyxhdmap(gyxhdmap);
		handler.setXhdmap(xhdmap);
		handler.setXiaohcmap(xiaohcmap);
		handler.setXiaohcbhmap(xiaohcbhmap);
		handler.setLingjmap(lingjmap);
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
				//0007177  将修改人编辑为当前登录人  编辑时间写入数据库
				String error = SheetHandlerFactorybg.getInstance(document,sheet,table,datasourceId,clazz,keys,dateColumns,dateFormats).doSheetHandler(editor,map,gyxhdmap,xhdmap,xiaohcmap,xiaohcbhmap,lingjmap,baseDao);
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
