package com.athena.util.cache;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 缓存组件管理类
 * @author WL
 * @date 2011-11-4
 */
@Component(scope = ComponentDefinition.SCOPE_SINGLETON)//单例元件
public class ComponentManager {

	private static final Logger logger = Logger.getLogger(ComponentManager.class);
	
	 @Inject 
	 protected AbstractIBatisDao baseDao;
	
	/**
	 * 缓存属性名
	 */
	public static final String codeListComponent = "glob_component_select"; 
	public static final String printModuleComponent = "glob_component_print";
	
	/**
	 * @Description 启动初始化函数
	 * @return 启动标识 1-成功,0-失败
	 * @author WL
	 * @date 2011-11-4
	 */
	public void init() throws Exception{
		//解析XML文件
		SAXBuilder sax = new SAXBuilder();
		Document doc = sax.build(CacheListener.class.getResourceAsStream("/config/cacheConfig.xml"));
		//获取rootelement
		Element element = doc.getRootElement();
		//获取component节点
		List list = element.getChildren("component");
		//初始化component节点
		initComponent(list);
	}
	
	/**
	 * 初始化component节点
	 * @param list component节点element
	 * @author WL
	 * @date 2011-11-4
	 */
	private void initComponent(List list){
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element) list.get(i);
			//组件类
			Comp com = new Comp();
			//组件名
			com.setName(e.getAttributeValue("name").trim());
			//描述
			com.setDescription(e.getAttributeValue("description").trim());
			//启用状态
			com.setIsEnabled(e.getAttributeValue("isEnabled").trim());
			//属性名
			com.setGlobName(e.getAttributeValue("globName").trim());
			
			//加载下拉框组件,解析启用状态为true,属性名为属性名的component节点
			if(null != com.getIsEnabled() && "true".equals(com.getIsEnabled()) && codeListComponent.equals(com.getGlobName())){
				//获取property节点
				List component = e.getChildren("property");
				//解析property节点
				initProperty(component,com);
			}
			
			//加载报表模板组件,解析启用状态为true,属性名为属性名的component节点
			if(null != com.getIsEnabled() && "true".equals(com.getIsEnabled()) && printModuleComponent.equals(com.getGlobName())){
				//获取property节点
				List component = e.getChildren("propertyModule");
				//解析property节点
				initPrintModuleProperty(component,com);
			}
		}
		
		//test
		//testPrint();
	}
	
	/**
	 * 解析打印模板节点
	 * @param list
	 * @param com
	 * zhangl 2012-01-31
	 */
	private void initPrintModuleProperty(List list,Comp com) {
		
		for (int i = 0; i < list.size(); i++) {
			
			Element e = (Element) list.get(i);
			
			PropertyModule module = new PropertyModule();
			//模板地址
			module.setPath(e.getAttributeValue("path").trim());
			//缓存名
			module.setCacheName(e.getAttributeValue("cacheName").trim());
			//缓存描述
			module.setDescription(e.getAttributeValue("description").trim());
			//缓存启用状态
			module.setIsEnabled(e.getAttributeValue("isEnabled").trim());
			
			//解析启用状态为true,path不为空的缓存
			if( null != module.getIsEnabled() && "true".equals(module.getIsEnabled()) 
					&& null != module.getPath() && !"".equals(module.getPath())){
				
				//List<PrintModule> printList = new LinkedList<PrintModule>();
				PrintConfig printConfig = new PrintConfig();
				logger.info("加载打印模板["+module.getCacheName()+"|"+module.getDescription()+"]缓存开始，模板路径为["+module.getPath()+"]");

				try {
					//解析XML文件
					SAXBuilder sax = new SAXBuilder();
					Document doc = sax.build(CacheListener.class.getResourceAsStream(module.getPath()));
					//获取rootelement
					Element element	 = doc.getRootElement();
					//获取area 的节点个数
					List listArea = element.getChildren("area");
					List<Area> areaList = new LinkedList<Area>();
					
					String areaId="";
					String dataPId="";
					
					
					//第一个根节点 PrintConfig set 属性
					printConfig.setPerPaperHeight(element.getAttributeValue("perPaperHeight"));
					printConfig.setBeginHeight(element.getAttributeValue("beginHeight"));
					printConfig.setEndHeight(element.getAttributeValue("endHeight"));
					printConfig.setPageInfo(element.getAttributeValue("pageInfo"));
					printConfig.setTaskHeight(element.getAttributeValue("taskHeight"));

					for (int n = 0; n < listArea.size(); n++) {
						
						List<Data> dataList = new LinkedList<Data>();		
						
						Element eArea = (Element)listArea.get(n);
						//获取data 的节点个数
						List listData = eArea.getChildren("data");
						areaId = eArea.getAttributeValue("id");
						
						Area area = new Area();
						//第二个节点 area    set 属性
						area.setId(eArea.getAttributeValue("id"));
					    area.setType(eArea.getAttributeValue("type"));
					    area.setAutoSr(eArea.getAttributeValue("autoSr"));
					    
					    for( int k=0; k<listData.size(); k++ ){
					    	Element eData = (Element)listData.get(k);
					    	dataPId = eData.getAttributeValue("parentid");
					    	if( dataPId.equals(areaId)){
						    	Data data = new Data();
						    	//第三个节点 data    set 属性
						    	data.setParentid(eData.getAttributeValue("parentid"));
								data.setKey(eData.getAttributeValue("key"));
								data.setDataType(eData.getAttributeValue("dataType"));
								data.setFont(eData.getAttributeValue("font"));
								data.setSr(eData.getAttributeValue("sr"));
								data.setSc(eData.getAttributeValue("sc"));
								data.setVe(eData.getAttributeValue("ve"));
								data.setHe(eData.getAttributeValue("he"));
								data.setValue(eData.getAttributeValue("value"));
								data.setRow(eData.getAttributeValue("row"));
								dataList.add(data);
					    	}	    	
					    }
					    area.setDataList(dataList);		
					    areaList.add(area);
					}
					printConfig.setAreaList(areaList);
					
					//解析XML文件
//					SAXBuilder sax = new SAXBuilder();
//					Document doc = sax.build(CacheListener.class.getResourceAsStream(module.getPath()));
					
					//获取rootelement
					//Element element = doc.getRootElement();
					//获取param节点
//					List modluelist = element.getChildren("param");
//					
//					for( int j=0; j<modluelist.size(); j++ ){
//						
//						Element param = (Element) modluelist.get(j);
//
//						PrintModule printModule = new PrintModule();
//
//						printModule.setKey(param.getChild("key").getText());
//						printModule.setPrintBegin(param.getChild("printBegin").getText());
//						printModule.setPrintSr(param.getChild("printSr").getText());
//						printModule.setPrintSc(param.getChild("printSc").getText());
//						printModule.setPrintVe(param.getChild("printVe").getText());
//						printModule.setPrintHe(param.getChild("printHe").getText());
//						printModule.setPrintValue(param.getChild("printValue").getText());
//						printModule.setPrintEnd(param.getChild("printEnd").getText());
//						
//						printList.add(printModule);
//					}

				} catch (JDOMException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch ( Exception e1 ){
					e1.printStackTrace();
				} 
					
				//缓存对象
				Cache cache = new Cache();
				//缓存的key
				cache.setCachekey(module.getCacheName());
				//缓存的value
				cache.setCacheValue(printConfig);	
				//组件对象
				cache.setComponentObject(com);
				//property对象
				cache.setPropertyObject(module);
				
				//放置缓存
				CacheManager.getInstance().putCache(module.getCacheName(), cache);
				logger.info("加载["+module.getCacheName()+"|"+module.getDescription()+"]缓存完毕!");
			}
		}
	}
	
	/**
	 * 解析property节点
	 * @param list property节点element
	 * @param com 组件类对象
	 */
	private void initProperty(List list,Comp com){
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element) list.get(i);
			
			//Property对象
			Property pro = new Property();
			//对应的ibatis的sql语句id
			pro.setSql(e.getAttributeValue("sqlName").trim());
			//缓存名
			pro.setCacheName(e.getAttributeValue("cacheName").trim());
			//缓存描述
			pro.setDescription(e.getAttributeValue("description").trim());
			//缓存启用状态
			pro.setIsEnabled(e.getAttributeValue("isEnabled").trim());
			
			//解析启用状态为true,ibatis的sql语句id不为空的缓存
			if(null != pro.getIsEnabled() && "true".equals(pro.getIsEnabled()) 
					&& null != pro.getSqlName() && !"".equals(pro.getSqlName())  ){
				
				logger.info("加载下拉框["+pro.getCacheName()+"|"+pro.getDescription()+"]缓存开始，sql语句句柄为["+pro.getSqlName()+"]");
				
				try{
					
					//缓存对象
					Cache cache = new Cache();
					//缓存的key
					cache.setCachekey(pro.getCacheName());
					//缓存的value
					cache.setCacheValue(getProperty(pro.getSqlName()));	
					//组件对象
					cache.setComponentObject(com);
					//property对象
					cache.setPropertyObject(pro);
					
					//放置缓存
					CacheManager.getInstance().putCache(pro.getCacheName(), cache);
					
					logger.info("加载["+pro.getCacheName()+"|"+pro.getDescription()+"]缓存成功!");
				
				}catch( Exception ex){
					logger.error("加载["+pro.getCacheName()+"|"+pro.getDescription()+"]缓存失败!");
					logger.error(ex.getMessage());
				}finally{
					logger.info("加载["+pro.getCacheName()+"|"+pro.getDescription()+"]缓存完毕!");
				}
				
				
			}
		}
		
		
	}
	
	/**
	 * 获取缓存数据集合
	 * @param sqlName ibatis的sql语句id
	 * @return 缓存数据集合
	 */
	public List<CacheValue> getProperty(String sqlName) throws Exception {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(sqlName, CacheValue.class); 
	}
	
	/**
	 * ZL test 方法
	 */
	
	@Inject
	private CacheManager cacheManager;   

}
