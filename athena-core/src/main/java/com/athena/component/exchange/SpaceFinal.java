package com.athena.component.exchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.athena.util.exception.ServiceException;

public class SpaceFinal {
	
	private static Logger logger = Logger.getLogger(SpaceFinal.class); 
	private static Map<String, String> propMap = new HashMap<String,String>(); 
	
	/**读取athena-interface-service下的sqlmap.properties配置文件  add method hzg 2012-11-14 **/
	static {
		//当前类的classloader
		ClassLoader loader = SpaceFinal.class.getClassLoader();
		//得到classpath下的资源文件
		try {
			ResourceBundle properties = new PropertyResourceBundle(loader.getResourceAsStream("sqlmap.properties"));
			//得到所有的配置信息
			for (Iterator<String> it = properties.keySet().iterator(); it.hasNext();) {
				//路经Key
				String pathKey = it.next().trim();
				//路经Value
				String pathValue = properties.getString(pathKey).trim();
				if(pathValue.length()==0){
					throw new ServiceException(pathKey+" 配置信息不能为空") ;
				}
				propMap.put(pathKey, pathValue);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	public static final String spacename_ckx = propMap.get("spacename_ckx");// 参考系空间名 都作为准备层 ZBC_TEST
	public static final String spacename_ck = propMap.get("spacename_ck");// 仓库空间名  ZXC_TEST
	public static final String spacename_xqjs = propMap.get("spacename_xqjs");// 需求计算空间名  ZBC_TEST
	public static final String spacename_pcfj = propMap.get("spacename_pcfj");// 排产发交空间名  ZBC_TEST
	
	//为了和前面的已经开发完的外部接口衔接,则定义了此静态变量
//	private static final String dbSchemal1 = "dbSchemal1"; //对应SpaceFinal类的spacename_xqjs  需求计算空间名
//	private static final String dbSchemal2 = "dbSchemal2"; //对应SpaceFinal类的spacename_pcfj  排产发交空间名
//	private static final String dbSchemal3 = "dbSchemal3"; //对应SpaceFinal类的spacename_ckx  参考系空间名
//	private static final String dbSchemal4 = "dbSchemal4"; //对应SpaceFinal类的spacename_ck  仓库空间名
//	private static final String dbSchemal5 = "dbSchemal5"; //对应SpaceFinal类的spacename_ddbh  DDBH空间名
	
	
	//为了使得维护方便，为内部接口 则定义此命名空间
	public static final String spacename_zbc = propMap.get("spacename_zbc"); //准备层
	public static final String spacename_zxc = propMap.get("spacename_zxc"); //执行层
	public static final String  spacename_ddbh = propMap.get("spacename_ddbh"); //DDBH空间名  DDBH_TEST
	
	/**
	 * 替换表空间名 ${dbSchemal3}
	 * 
	 * @param sql
	 * @return
	 */
	public static  String replaceSql(String tableSpace) {

		String result = tableSpace;

		if (tableSpace.contains("${")) {
			String regex = "\\$\\{\\w+\\}";
			List<String> li = new ArrayList<String>();
			final Pattern pa = Pattern.compile(regex);
			final Matcher ma = pa.matcher(tableSpace);
			while (ma.find()) {
				li.add(ma.group());
			}

			// 将占位符替换成真命名空间名
			for (String key : li) {
				String realValue = getValueByKey(key);
				tableSpace = tableSpace.replace(key, realValue);
			}

			result = tableSpace;
		}
		return result;
	}

	/**
	 * 对Key 最处理后 再依据处理后的key到 properties文件中拿值 key:${xxxx} 处理后 xxx
	 * 
	 * @param key
	 * @return
	 */
	private static String getValueByKey(String key) {
		String result = null;
		if(key==null){
			return "";
		}
		String realKey = key.substring(2, key.length() - 1);
		if(realKey.equals("spacename_zbc")){
			result = SpaceFinal.spacename_zbc;
		}else if(realKey.equals("spacename_zxc")){
			result = SpaceFinal.spacename_zxc;
		}else if(realKey.equals("spacename_ddbh")){
			result = SpaceFinal.spacename_ddbh;
		}else{
			return "";
		}
		return result + ".";
	}
}
