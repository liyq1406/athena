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
public class XlsHandlertbjplj {
	private InputStream in;
	private String jsonConfig;
	private String editor;  //导入人
	private Map<String,Object> maplingj;
	private Map<String,Object> mapscx;
	private Map<String,Object> mappslx;
	private Map<String,String> maptbjplj;
	private Map<String,String> mapljck;
	private AbstractIBatisDao baseDao;//数据源
	

	
	
	public Map<String, String> getMapljck() {
		return mapljck;
	}


	public void setMapljck(Map<String, String> mapljck) {
		this.mapljck = mapljck;
	}


	public Map<String, String> getMaptbjplj() {
		return maptbjplj;
	}


	public void setMaptbjplj(Map<String, String> maptbjplj) {
		this.maptbjplj = maptbjplj;
	}


	public Map<String, Object> getMaplingj() {
		return maplingj;
	}


	public void setMaplingj(Map<String, Object> maplingj) {
		this.maplingj = maplingj;
	}


	public Map<String, Object> getMapscx() {
		return mapscx;
	}


	public void setMapscx(Map<String, Object> mapscx) {
		this.mapscx = mapscx;
	}


	public Map<String, Object> getMappslx() {
		return mappslx;
	}


	public void setMappslx(Map<String, Object> mappslx) {
		this.mappslx = mappslx;
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

	private XlsHandlertbjplj() {
		super();
	}

	public static XlsHandlertbjplj getInstance(Map<String,String> mapljck,Map<String,String> maptbjplj,Map<String,Object> maplingj,Map<String,Object> mapscx,Map<String,Object> mappslx,InputStream in,String jsonConfig,String editor,AbstractIBatisDao baseDao){
		XlsHandlertbjplj handler = new XlsHandlertbjplj();
		handler.setIn(in);
		handler.setJsonConfig(jsonConfig);
		handler.setEditor(editor);
		handler.setBaseDao(baseDao);
		handler.setMaplingj(maplingj);
		handler.setMapscx(mapscx);
		handler.setMappslx(mappslx);
		handler.setMaptbjplj(maptbjplj);
		handler.setMapljck(mapljck);
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
				String error = SheetHandlerFactorytbjplj.getInstance(document,sheet,table,datasourceId,clazz,keys,dateColumns,dateFormats).doSheetHandler(mapljck,maptbjplj,maplingj,mapscx,mappslx,editor,baseDao);
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
