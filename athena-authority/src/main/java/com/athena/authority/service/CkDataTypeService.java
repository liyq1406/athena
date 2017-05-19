/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 穆伟
 * @version v1.0
 * @date 
 */
package com.athena.authority.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.CkDataType;
import com.athena.authority.util.PropertyLoader;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.Cache;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.toft.core3.container.annotation.Component;
import com.toft.utils.JSONUtils;
import com.toft.utils.json.JSONException;

@Component
public class CkDataTypeService extends BaseService{
	// log4j日志初始化
	private final Log log = LogFactory.getLog(CkDataTypeService.class);
	/**
	 * 获取模块空间
	 */
	@Override
	protected String getNamespace() {
		return "authority";
	}

	public Map<String,Object> getCkDataInfo(CkDataType bean,String postCode) {
		CkDataType dt = (CkDataType)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".getCkDataType", bean);
		String  cuncTableName = dt.getCuncTableName();
		String  dataSQLParam = dt.getDataSQLParam();
		PropertyLoader pl = new PropertyLoader();
		//gswang 2012-09-27 mantis 0004307
		String newDaataSQLParam = pl.getPropertyInfo(dataSQLParam);
		newDaataSQLParam = pl.getNewDataSql(newDaataSQLParam,postCode);
		List<Map<String,Object>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".getDataSQLParamInfo",newDaataSQLParam);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("cuncTableName", cuncTableName);
		List<String> newlist = new ArrayList<String>();
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCacheInfo("queryUserCenterMap");
		List<CacheValue> cacheValue = (List<CacheValue>) cache.getCacheValue();
		Map<String,String> cacheMap = new HashMap<String,String>();
		for(CacheValue cvalue:cacheValue){
			cacheMap.put(cvalue.getKey(),cvalue.getValue());
		}
		for(Map<String,Object> map:list){
			String usercenter = (String)map.get("USERCENTER");
			String text =(String)map.get("TEXT");
			String value = (String)map.get("VALUE");
			Map<String,String> newMap = new HashMap<String,String>();
			newMap.put("usercenter", usercenter);
			newMap.put("ucname", (String)cacheMap.get(usercenter));
			newMap.put("text", text);
			newMap.put("value", value);
			String resultJson = "";
			if(newMap.get("ucname")!=null && !"".equals(newMap.get("ucname")) && newMap.get("ucname").length()>0){
				try {
					resultJson = JSONUtils.toJSON(newMap);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					log.error("对象转化为JSON失败");
				}	
				newlist.add(resultJson);
			}
		}
		resultMap.put("rows", newlist);
		return resultMap;
	}

	/*
	 * 根据DicCode查询数据权限组项
	 * @param bean
	 * @return
	 */
	public Object listDicCode(CkDataType bean) {
		/**
		 * 执行查询
		 */
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".getCkDataTypeDicCode",bean);
	}
	
}
