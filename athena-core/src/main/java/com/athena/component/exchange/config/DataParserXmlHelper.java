/**
 * 
 */
package com.athena.component.exchange.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.athena.component.exchange.ParserException;
import com.athena.component.exchange.field.DataField;

/**
 * <p>Title:</p>
 *
 * <p>Description:xml配置文件解析工具栏</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0
 */
public class DataParserXmlHelper {
	
	private static final Log logger = LogFactory.getLog(DataParserXmlHelper.class);
	
	public static final String ATTR_ID="id";//属性:id
	
	public static final String ATTR_CAPTION="caption";//属性:文本描述
	
	public static final String ATTR_DEFAULT_INPUT="defaultInput";//属性:默认输入
	
	private static final String ATTR_SQL = "sql";//sql 字段

	private static final String ATTR_ID_KEYS = "idKeys";//属性:idKeys

	private static final String ATTR_TABLE = "table";//属性:table

	private static final String ATTR_SPLIT = "split";//分隔符

	private static final String ATTR_QUOTE = "quote";//引号
	
	private static final String ATTR_FILE_PATH = "filePath";//文件路径
	
	private static final String ATTR_EXCHANGER_CLASS = "class";//交换类名称
	
	private static final String ATTR_FILE_ENCODING = "encoding";
	
	private static final String ATTR_ERROR_PATH = "errorFilePath"; //错误文件路径
	
	private static final String ATTR_BACKUP_PATH = "backupFilePath"; //备份文件路径
	
	private static final String ATTR_IS_GOON = "isGoOn"; //错误后是否继续处理
	
	private static final String ATTR_IS_GOONOUT = "isGoOnOut"; //是否继续输出不新建文件
	
	private static final String ATTR_ATH_FILE_PATH = "athfilePath";//文件路径
	
	private static String writer_class; //输出组的class属性
	
	private static final String ATTR_ISTASK	 = "isTask";//属性:isTask,是否有子类任务
		
	public static String getWriter_class() {
		return writer_class;
	}

	public  static void setWriter_class(String writer_class) {
		DataParserXmlHelper.writer_class = writer_class;
	}

	public static Document readFromXml(String configPath){
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(configPath);
		} catch (DocumentException e) {
			throw new ParserException("配置文件读取异常："+e.getMessage());
		}
		return doc;
	}
	
	/**
	 * 读取xml配置
	 * @param input
	 * @return
	 */
	public static Document readFromXml(InputStream input){
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(input);
		} catch (DocumentException e) {
			throw new ParserException("配置文件读取异常："+e.getMessage());
		}
		return doc;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<? extends String, ? extends DataParserConfig> parseConfigs(
			Document doc) {
		Map<String,DataParserConfig> configs = 
			new HashMap<String,DataParserConfig>();
		
		if(doc!=null){
			List<Element> groupElements = 
				doc.selectNodes("exchange-configs/exchange-config-group");
			for(Element groupElement:groupElements){
				configs.putAll(parseGroupConfigs(groupElement,parseGroupConfig(groupElement)));
			}
		}
		logger.info("读取数据交换配置完成！");
		return configs;
	}
	
	/**
	 * 解析分组
	 * @param element
	 * @return
	 */
	private static GroupConfig parseGroupConfig(Element element){
		GroupConfig groupConfig = new GroupConfig();
		groupConfig.setReader(getAttrValue(element, "reader"));
		groupConfig.setWriter(getAttrValue(element, "writer"));
		return groupConfig;
	}

	/**
	 * 
	 * 解析组下面的配置项
	 * @param groupElement
	 * @param groupConfig
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<? extends String, ? extends DataParserConfig> parseGroupConfigs(Element groupElement
			,GroupConfig groupConfig) {
		Map<String,DataParserConfig> configs = 
			new HashMap<String,DataParserConfig>();
		
		List<Element> configElements = 
			groupElement.selectNodes("exchange-config");
		String configId = null;
		for(Element configElement:configElements){
			configId = configElement.attributeValue(ATTR_ID);
			if(configId!=null){
				configs.put(configId,parseConfig(configElement,groupConfig,configId));
			}
		}
		return configs;
	}
	
	/**
	 * exchange-config元素解析成DataParserConfig对象
	 * @param configElement 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static DataParserConfig parseConfig(Element configElement,GroupConfig groupConfig,String configId){
		DataParserConfig dpc = new DataParserConfig();
		
		String caption = null;
		caption = configElement.attributeValue(ATTR_CAPTION);
		dpc.setCaption(caption);
		//DB到xml  xss 2015.12.29 
		
		dpc.setId(configId);
		List<DataField> dataFields = new ArrayList<DataField>();
		
		List<Element> fieldElements = configElement.selectNodes("field");
		
		
		int start = 0;
		String split = configElement.attributeValue(ATTR_SPLIT);
		String quote = configElement.attributeValue(ATTR_QUOTE);
		
		split = split==null?"":split;
		quote = quote==null?"":quote;
		
		DataField dataField;
		for(Element fieldElement:fieldElements){
			dataField = parseDataField(fieldElement);
			if(dataField!=null){
				if(dataField.getStart()==0){
					dataField.setStart(start);
				}
				dataFields.add(dataField);
				start+=(dataField.getLength()+split.length());
				dataField = null;
			}
		}
		
		dpc.setGroupConfig(groupConfig);
		dpc.setDataFields(dataFields.toArray(new DataField[dataFields.size()]));
		dpc.setReaderConfig(parseReadExchangerConfig(configElement.selectSingleNode("reader")));
		
		//生成输出项 原
		//dpc.setWriterConfig(parseExchangerConfig(configElement.selectSingleNode("writer")));
		
		//改造 --> 输出：支持输出 生成多文件 
		if("txt".equals(groupConfig.getWriter())){
			//改造 --> 输出：支持输出 生成多文件 
			List<Element> writers = configElement.selectNodes("writers");
			//设置class属性
			writer_class =  getAttrValue(writers.get(0), ATTR_EXCHANGER_CLASS); 
			dpc.setWriterConfigs(parseExchengerConfigs(configElement.selectSingleNode("writers")));
		}else if("xml".equals(groupConfig.getWriter())){//DB到xml  xss 2015.12.29 
			//改造 --> 输出：支持输出 生成多文件 
			List<Element> writers = configElement.selectNodes("writers");
			//设置class属性
			writer_class =  getAttrValue(writers.get(0), ATTR_EXCHANGER_CLASS); 
			dpc.setWriterConfigs(parseExchengerConfigs(configElement.selectSingleNode("writers")));
		}else{
			writer_class = null;
			dpc.setWriterConfig(parseWriterExchangerConfig(configElement.selectSingleNode("writer")));
		}
		
		return dpc;
	}
	
//	/**
//	 * 创建数据交换回调类
//	 * @param callBackClassName
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	private static DataWriter createDataParserCallBack(
//			String callBackClassName) {
//		DataWriter dataParserCallBack = null;
//		Class<DataWriter> callbackClass = null;
//		if(callBackClassName!=null){
//			try {
//				callbackClass = 
//					(Class<DataWriter>) ClassUtils.forName(callBackClassName,DataParserXmlHelper.class.getClassLoader());
//			} catch (ClassNotFoundException e) {
//				logger.warn("配置的数据交换回调类【"+callBackClassName+"】没有找到!");
//			} catch (LinkageError e) {
//				logger.warn("配置的数据交换回调类【"+callBackClassName+"】:"+e.getMessage());
//			}catch(Exception e){
//				logger.warn("配置的数据交换回调类【"+callBackClassName+"】："+e.getMessage());
//			}
//		}
//		if(callbackClass!=null){
//			try {
//				dataParserCallBack = callbackClass.newInstance();
//			} catch (InstantiationException e) {
//				logger.warn("配置的数据交换回调类【"+callBackClassName+"】实例化时异常："+e.getMessage());
//			} catch (IllegalAccessException e) {
//				logger.warn("配置的数据交换回调类【"+callBackClassName+"】实例化时异常："+e.getMessage());
//			}
//		}
//		
//		return dataParserCallBack;
//	}
	
	/**
	 * 改造 --> 输出：支持输出 生成多文件---生成输出组对象
	 */
	private static ExchangerConfig[] parseExchengerConfigs(Node node) {
		List<ExchangerConfig> exchangerConfigs = new ArrayList<ExchangerConfig>();
		
		@SuppressWarnings("unchecked")
		List<Element> configElements = node.selectNodes("writer");
		
		for(Element element : configElements){
			exchangerConfigs.add(parseWriterExchangerConfig(element));
		}
		
		return exchangerConfigs.toArray(new ExchangerConfig[exchangerConfigs.size()]);
	}

	/**
	 * @param node
	 * @return
	 */
	private static ExchangerConfig parseReadExchangerConfig(Node node){
	
		ExchangerConfig exchangerConfig = new ExchangerConfig();
		if(node==null)return exchangerConfig;
		
		Element element = (Element)node;
		
		exchangerConfig.setExchangerClass(getAttrValue(element,ATTR_EXCHANGER_CLASS));

		exchangerConfig.setFilePath(getAttrValue(element,ATTR_FILE_PATH));
		exchangerConfig.setEncoding(getAttrValue(element,ATTR_FILE_ENCODING));
		
		exchangerConfig.setQuote(getAttrValue(element,ATTR_QUOTE));
		exchangerConfig.setSplit(getAttrValue(element,ATTR_SPLIT));
		
		exchangerConfig.setSql(getAttrValue(element,ATTR_SQL));
		exchangerConfig.setTable(element.attributeValue(ATTR_TABLE));
		exchangerConfig.setIdKeys(element.attributeValue(ATTR_ID_KEYS));
		exchangerConfig.setDatasourceId(element.attributeValue("datasourceId"));
		
		exchangerConfig.setIsTask(element.attributeValue(ATTR_ISTASK));
		
		//设置文件的 错误路径  和 备份路径 
		exchangerConfig.setErrorFilePath(element.attributeValue(ATTR_ERROR_PATH)); //设置 错误文件路径
		exchangerConfig.setBackupFilePath(element.attributeValue(ATTR_BACKUP_PATH));//设置 备份文件路径
		
		//设置发生错误 是否移动 标志 暂时没有用
		exchangerConfig.setIsGoOn(element.attributeValue(ATTR_IS_GOON)); //是否继续解析下面文件
		//支持增量条件的标识
		exchangerConfig.setIsAllSet(element.attributeValue("isAllSet"));//是否生成增量的标识

		Node databaseNode = element.selectSingleNode("database");
		if(databaseNode!=null){
			exchangerConfig.setDbConfig(parseDbConfig((Element)databaseNode));
		}
				
		return exchangerConfig;
	}
	
private static ExchangerConfig parseWriterExchangerConfig(Node node){
		
		ExchangerConfig exchangerConfig = new ExchangerConfig();
		if(node==null)return exchangerConfig;
		
		Element element = (Element)node;
		
		//改造 --> 输出：支持输出 生成多文件--- 给每个writer属性设置class属性
		if(writer_class!=null){
			exchangerConfig.setExchangerClass(writer_class);
		}else{
			exchangerConfig.setExchangerClass(getAttrValue(element,ATTR_EXCHANGER_CLASS));
		}
		
		
		exchangerConfig.setFilePath(getAttrValue(element,ATTR_FILE_PATH));
		exchangerConfig.setEncoding(getAttrValue(element,ATTR_FILE_ENCODING));
		
		exchangerConfig.setQuote(getAttrValue(element,ATTR_QUOTE));
		exchangerConfig.setSplit(getAttrValue(element,ATTR_SPLIT));
		
		exchangerConfig.setSql(getAttrValue(element,ATTR_SQL));
		exchangerConfig.setTable(element.attributeValue(ATTR_TABLE));
		exchangerConfig.setIdKeys(element.attributeValue(ATTR_ID_KEYS));
		exchangerConfig.setDatasourceId(element.attributeValue("datasourceId"));
		
		exchangerConfig.setIsTask(element.attributeValue(ATTR_ISTASK));
				
		//设置是否继续输出，不新建文件，用来多表输出到一个文件
		exchangerConfig.setIsGoOnOut(element.attributeValue(ATTR_IS_GOONOUT)); //是否继续解析下面文件
		exchangerConfig.setAthfilePath(element.attributeValue(ATTR_ATH_FILE_PATH)); //是否继续解析下面文件
		Node databaseNode = element.selectSingleNode("database");
		if(databaseNode!=null){
			exchangerConfig.setDbConfig(parseDbConfig((Element)databaseNode));
		}
		
		//改造 --> 输出：支持输出 生成多文件 
		exchangerConfig.setFilePath(element.attributeValue("filePath"));
		exchangerConfig.setFileName(element.attributeValue("fileName"));
		
		exchangerConfig.setUsercenter(element.attributeValue("usercenter"));
		
		//2012-08-24
		exchangerConfig.setIsTrim(element.attributeValue("istrim"));
		
		exchangerConfig.setIsUpdate(element.attributeValue("isUpdate"));
		return exchangerConfig;
	}
	
	/**
	 * 根据oldConfig配置生成一个新的exchangerConfig
	 *  设置文件名称     用户中心
	 * @param oldConfig
	 * @param usercenter 格式  ALL:名称  
	 * @return
	 */
	public static ExchangerConfig parseWriterExchangerConfig(ExchangerConfig oldConfig,String usercenter){
		//输出 创建WriterExchangerConfig对象
		String[] usercenters = usercenter.split(":");
		
		ExchangerConfig exchangerConfig = new ExchangerConfig();
		if(oldConfig==null)return exchangerConfig;
		
		//改造 --> 输出：支持输出 生成多文件--- 给每个writer属性设置class属性
		exchangerConfig.setExchangerClass(oldConfig.getExchangerClass());
		
		
		exchangerConfig.setFilePath(oldConfig.getFilePath());
		exchangerConfig.setEncoding(oldConfig.getEncoding());
		
		exchangerConfig.setQuote(oldConfig.getQuote());
		exchangerConfig.setSplit(oldConfig.getSplit());
		
		exchangerConfig.setSql(oldConfig.getSql());
		exchangerConfig.setTable(oldConfig.getTable());
		exchangerConfig.setIdKeys(oldConfig.getIdKeys());
		exchangerConfig.setDatasourceId(oldConfig.getDatasourceId());
		exchangerConfig.setIsGoOnOut(oldConfig.getIsGoOnOut());
		exchangerConfig.setFilePath(oldConfig.getFilePath());
		exchangerConfig.setAthfilePath(oldConfig.getAthfilePath());
		//exchangerConfig.setFileName(usercenter+"_"+oldConfig.getFileName());
		exchangerConfig.setFileName(usercenters[1]+ ".txt"); // 设置文件名称
		
		exchangerConfig.setUsercenter(usercenters[0]);
				
		return exchangerConfig;
	}
	
	private static DbConfig parseDbConfig(Element dbElement){
		DbConfig dbConfig = new DbConfig();
		dbConfig.setUrl(getAttrValue(dbElement,"url"));
		dbConfig.setUsername(getAttrValue(dbElement,"username"));
		dbConfig.setDriver(getAttrValue(dbElement,"driver"));
		dbConfig.setPassword(getAttrValue(dbElement,"password"));
		return dbConfig;
	}
	
	private static String getAttrValue(Element element,String attrName){
		return element.attributeValue(attrName);
	}

	/**
	 * 配置字段解析
	 * @param fieldElement
	 * @return
	 */
	private static DataField parseDataField(Element fieldElement){
		DataField dataField = new DataField();
		
		dataField.setCaption(fieldElement.attributeValue("caption"));
		dataField.setFormat(fieldElement.attributeValue("format"));
		dataField.setStart(convertAttrInt(fieldElement.attributeValue("start")));
		dataField.setLength(convertAttrInt(fieldElement.attributeValue("length")));
		
		dataField.setWriterColumn(fieldElement.attributeValue("writerColumn"));
		dataField.setReaderColumn(fieldElement.attributeValue("readerColumn"));
		dataField.setUpdate(!"false".equals(fieldElement.attributeValue("isUpdate")));
		
		/**
		 * <filed没有配置属性isParma的字段为null，判断属性值是否为"true"，不为true就返回boolean=true。
		 * 属性值为"true"就返回false
		 */
		dataField.setIsParam(!"true".equals(fieldElement.attributeValue("isParam")));
		
		dataField.setType(fieldElement.attributeValue("type"));
		
		//改造 --> 输出：支持输出 生成多文件
		dataField.setSeparate(fieldElement.attributeValue("separate"));
		dataField.setSeparate_size(fieldElement.attributeValue("separate_size"));
		
		return dataField;
	}
	
	private static int convertAttrInt(String attributeValue) {
		try {
			return Integer.parseInt(attributeValue);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
