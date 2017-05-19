/**
 * 
 */
package com.athena.component.exchange;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.config.DataParserConfigFactory;
import com.athena.component.exchange.config.DataParserXmlHelper;
import com.athena.component.exchange.config.ExchangerConfig;
import com.athena.component.exchange.config.GroupConfig;
import com.athena.component.exchange.db.DBOutputTxtSerivce;
import com.athena.component.exchange.db.DBOutputXmlSerivce;
import com.athena.component.exchange.txt.TxtInputDBSerivce;
import com.athena.component.exchange.utils.FileUtis;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.util.ComponentUtils;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.util.Assert;
import com.toft.utils.UUIDHexGenerator;


/**
 * <p>Title:数据交换平台</p>
 *
 * <p>Description:数据交换服务了</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
@Component
public class DataExchange{
	public final static String IN_INTERFACE = "in";
	public final static String OUT_INTERFACE = "out";

    protected final Logger logger=Logger.getLogger(DataExchange.class);//定义日志方法
	
	@Inject 
	protected AbstractIBatisDao baseDao;//注入baseDao

    /**
     *
     * @param configId   接口ID
     * @param flag       标识：in表示输入, out表示输出
     */
	public boolean doExchange(String configId, String flag) throws ServiceException {
		logger.info("接口"+ configId + "开始解析....");
        boolean result = false;
        DataParserConfig dataParserConfig = initDataParserConfig(configId);
		if (IN_INTERFACE.equals(flag)) {
            InputDataService inputDataService;
            try {
                inputDataService = createInputDataService(dataParserConfig);
            } catch (Exception e) {
                logger.error("接口"+ configId + "创建输入对象类出错", e);
                throw new ServiceException("接口"+ configId + "创建输入对象类出错",e);
            }
            result = inputDataService.read(dataParserConfig);
        } else if (OUT_INTERFACE.equals(flag)) {
            OutputDataService outputDataService;
            try {
                outputDataService = createOutputDataService(dataParserConfig);
            } catch (Exception e) {
                logger.error("接口"+ configId + "创建输出对象类出错", e);
                throw new ServiceException("接口"+ configId + "创建输出对象类出错",e);
            }
            result = outputDataService.write(dataParserConfig);
        }
        return result;
	}

    /**
     * 初始化配置
     * @param configId  接口ID
     * @return
     */
    private DataParserConfig initDataParserConfig(String configId) {
        DataParserConfig dataParserConfig =
                DataParserConfigFactory.getInstance().getDataParserConfig(configId);
        dataParserConfig.setId(configId);
        dataParserConfig.setBaseDao(baseDao); //设置dao类
        
        if("txt".equals(dataParserConfig.getGroupConfig().getWriter())){
            //为了直接从接口总表拿出用户中心，及所生成的文件
            List<String> list = InziDbUtils.getInstance().selectById(dataParserConfig.getReaderConfig().getDatasourceId(), configId,"toTxt");
            if(list!=null){
                //创建多个ExchangerConfig
                List<ExchangerConfig> wclist = new ArrayList<ExchangerConfig>();
                for(String usercenter : list){
                    wclist.add(DataParserXmlHelper.parseWriterExchangerConfig(dataParserConfig.getWriterConfigs()[0], usercenter));
                }
                dataParserConfig.setWriterConfigs(wclist.toArray(new ExchangerConfig[wclist.size()]));
            }
        }
        //TXT到DB  hzg 2014.3.21
        if("db".equals(dataParserConfig.getGroupConfig().getWriter())&&"txt".equals(dataParserConfig.getGroupConfig().getReader())){
        	//为了直接从接口总表拿输入的用户中心，及输入文件名,  list{UL,UX:ath3iath104,UW:ath3iath204}
            List<String> list = InziDbUtils.getInstance().selectById(dataParserConfig.getWriterConfig().getDatasourceId(), configId,"toDb");
            if(list!=null){
            	
            	Map<String,String>  fileUcMap = new HashMap<String,String>();
                for(String usercenter : list){ //将list值解析后放到map中  hzg 2014.3.22
                	String[] strUC = usercenter.split(":");
                	fileUcMap.put(strUC[1]+ ".txt", getUsercenter(strUC[0]));
                }
                dataParserConfig.setFileUcMap(fileUcMap);
                dataParserConfig.setInputFileList(list);
               
                logger.error("接口"+ dataParserConfig.getId() + "list size："+list.size());    
                for(int i=0;i<list.size();i++){
            logger.error("接口"+ dataParserConfig.getId() + "list："+list.get(i));
                }
                
            }
        }
        //XML到DB  hanwu 20151225 代码同TXT到DB，只是添加后缀名改为.xml
        if("db".equals(dataParserConfig.getGroupConfig().getWriter())&&"xml".equals(dataParserConfig.getGroupConfig().getReader())){
        	//为了直接从接口总表拿输入的用户中心，及输入文件名,  list{UL,UX:ath3iath104,UW:ath3iath204}
            List<String> list = InziDbUtils.getInstance().selectById(dataParserConfig.getWriterConfig().getDatasourceId(), configId,"toDb");
            if(list!=null){
            	
            	Map<String,String>  fileUcMap = new HashMap<String,String>();
                for(String usercenter : list){ //将list值解析后放到map中  hzg 2014.3.22
                	String[] strUC = usercenter.split(":");
                	fileUcMap.put(strUC[1]+ ".xml", getUsercenter(strUC[0]));
                }
                dataParserConfig.setFileUcMap(fileUcMap);
                dataParserConfig.setInputFileList(list);
               
                logger.info("接口"+ dataParserConfig.getId() + "list size："+list.size());    
                for(int i=0;i<list.size();i++){
                	logger.info("接口"+ dataParserConfig.getId() + "list："+list.get(i));
                }
                
            }
        }
        //DB到xml  xss 2015.12.29 
        if("xml".equals(dataParserConfig.getGroupConfig().getWriter())){
            //为了直接从接口总表拿出用户中心，及所生成的文件
            List<String> list = InziDbUtils.getInstance().selectById(dataParserConfig.getReaderConfig().getDatasourceId(), configId,"toXml");
            if(list!=null){
                //创建多个ExchangerConfig
                List<ExchangerConfig> wclist = new ArrayList<ExchangerConfig>();
                for(String usercenter : list){
                    wclist.add(DataParserXmlHelper.parseWriterExchangerConfig(dataParserConfig.getWriterConfigs()[0], usercenter));
                }
                dataParserConfig.setWriterConfigs(wclist.toArray(new ExchangerConfig[wclist.size()]));
            }
        }
        
        return dataParserConfig;
    }
	
    /**
     *  hzg 
     * 生成所需查询的用户中心
     * @date 2014-3-22
     * @param user 用户中心 UL,UX  或UW
     * @return 加引号的用户中心 'UL,UX' 或'UW'
     */
	private String getUsercenter(String user){
		String[] str = user.split(",");
		String userCenter = "";
		for(int i=0;i<str.length;i++){
			if(i==str.length-1){
				userCenter +="'"+str[i]+"'";
			}else{
				userCenter +="'"+str[i]+"',";
			}
		}
		return userCenter;
    }
    
	/**
	 * 创建输出对象
	 * @param dataParserConfig
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public OutputDataService createOutputDataService(DataParserConfig dataParserConfig) throws Exception {
		String readerClass = 
			dataParserConfig.getWriterConfigs()[0].getExchangerClass();
		String groupWriter = dataParserConfig.getGroupConfig().getWriter();
		Class<? extends OutputDataService> dataReaderClazz = null;
		if(StringUtils.isNotEmpty(readerClass)){
			try { 
				dataReaderClazz = FileUtis.getClass(readerClass);
			} catch (ClassNotFoundException e) {
				logger.error("接口"+ dataParserConfig.getId() + "数据输出类未找到："+readerClass+",使用组默认的输出类。");
			}
		}
		if(dataReaderClazz==null){
			if(GroupConfig.EXCHANGE_DB.equals(groupWriter)){
//				dataReaderClazz = DbDataWriter.class;
			}else if(GroupConfig.EXCHANGE_TXT.equals(groupWriter)){
				dataReaderClazz = DBOutputTxtSerivce.class;
			}else if (GroupConfig.EXCHANGE_XML.equals(groupWriter)){ //DB到xml  xss 2015.12.29 
				dataReaderClazz = DBOutputXmlSerivce.class;
			}
		}

//		//实例化
		OutputDataService dataReader;
		if(readerClass == null || "".equals(readerClass)){
			if(GroupConfig.EXCHANGE_XML.equals(groupWriter)){//DB到xml  xss 2015.12.29 
				dataReader = (OutputDataService)ConstructorUtils.invokeConstructor(DBOutputXmlSerivce.class, null);
			}else{
				dataReader = (OutputDataService)ConstructorUtils.invokeConstructor(DBOutputTxtSerivce.class, null);
			} 
		}else{
			//构造函数
			Constructor<OutputDataService> constructor = 
				ConstructorUtils.getAccessibleConstructor(dataReaderClazz, new Class[]{DataParserConfig.class});
			//参数
			Object[] args =  new Object[]{dataParserConfig};
			dataReader =ComponentUtils.instantiateClass(constructor, args);
		}
		

        logger.info("接口" + dataParserConfig.getId() + "数据输出类：" + dataReader.getClass());
		return dataReader;
	}

    /**
     * 创建输出接口
     * @param dataParserConfig
     * @return
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	private InputDataService createInputDataService(DataParserConfig dataParserConfig) throws Exception {
		GroupConfig groupConfig = dataParserConfig.getGroupConfig();
		
		String groupWriter = groupConfig.getWriter();
		String readerClass = dataParserConfig.getReaderConfig().getExchangerClass();
		
		Class<? extends InputDataService> dataReaderClass = null;
		if(StringUtils.isNotEmpty(readerClass)){
			try {
				dataReaderClass = FileUtis.getClass(readerClass);
			} catch (ClassNotFoundException e) {
				logger.error("接口"+ dataParserConfig.getId() +"数据输入类未找到："+dataReaderClass+",使用组默认的输入类。");
			}
		}
		if(dataReaderClass==null){
			if(GroupConfig.EXCHANGE_DB.equals(groupWriter)){
				dataReaderClass = TxtInputDBSerivce.class;
			}else if(GroupConfig.EXCHANGE_TXT.equals(groupWriter)){
				dataReaderClass = TxtInputDBSerivce.class;
			}
		}
//      InputDataService dataWriter = (InputDataService)ConstructorUtils.invokeConstructor(dataWriterClazz, null);
//		logger.info("接口"+ dataParserConfig.getId() + "数据输入类："+dataWriter.getClass());
		//hzg add 2013-1-25
		//构造函数
		Constructor<InputDataService> constructor = 
			ConstructorUtils.getAccessibleConstructor(dataReaderClass, new Class[]{DataParserConfig.class});
		//参数 
		Object[] args = new Object[]{dataParserConfig};
		//实例化
		InputDataService inputDataWriter = ComponentUtils.instantiateClass(constructor, args);
		Assert.notNull(inputDataWriter,"数据输入对象不能为空！");
		logger.debug("接口"+ dataParserConfig.getId() + "数据输入类："+inputDataWriter.getClass());
		return inputDataWriter;
	}
	
	
	
	// gswang 2012-01-23 test
	//主日志ID
	private String CID;
	
	//db-->txt做完增量抽取后的当前时间，设置给总接口做完成时间
	public String nowTime = null;
	/**
	 * 生成UUID
	 * @return String
	 */
	public static String getUUID(){
		return UUIDHexGenerator.getInstance().generate();
	}
	
	public String getCID() {
		return CID;
	}

	public void setCID(String cID) {
		CID = cID;
	}
	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}
	public static String configId=null;//定义configId
	public static String RunStartTime=null;//定义运行开始时间
	
	//分发标志
	private String isGoon;
	public String getIsGoon() {
		return isGoon;
	}

	public void setIsGoon(String isGoon) {
		this.isGoon = isGoon;
	}
	
	public int total=0; //处理总记录
}
