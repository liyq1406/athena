/**
 * 代码声明
 */
package com.athena.authority.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.AuthorityConstants;
import com.athena.authority.entity.PostDataItem;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.Cache;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.toft.core3.container.annotation.Component;
import com.toft.utils.JSONUtils;
import com.toft.utils.json.JSONException;

@Component
public class PostDataItemService extends BaseService<PostDataItem>{
	// log4j日志初始化
	private final Log log = LogFactory.getLog(PostDataItemService.class);
	@Override
	protected String getNamespace() {
		return AuthorityConstants.MODULE_NAMESPACE;
	}
	/**
	 * 权限dataId获取用户组数据权限项
	 * @param dataId
	 * @return
	 */
	public Map listPostDataItemByDataId(String dataId,String postCode) {
		//初始化PostDataItem
		PostDataItem pDataItem = new PostDataItem();
		//设置dataId
		pDataItem.setDataId(dataId);
		pDataItem.setPostCode(postCode);
		//返回集合,查询所有分配数据项用户组数据权限项
		List<PostDataItem> dataItems = 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(getNamespace()+".listPostDataItemByDataId", pDataItem);
		CacheManager cacheManager = CacheManager.getInstance();
		Cache cache = cacheManager.getCacheInfo("queryUserCenterMap");
		List<CacheValue> cacheValue = (List<CacheValue>) cache.getCacheValue();
		Map<String,String> cacheMap = new HashMap<String,String>();
		for(CacheValue cvalue:cacheValue){
			cacheMap.put(cvalue.getKey(),cvalue.getValue());
		}
		List newList = new ArrayList();
		for(PostDataItem dataitem:dataItems){
			dataitem.setUsercenter(cacheMap.get(dataitem.getUsercenter()));
			String resultJson = "";
			try {
				resultJson = JSONUtils.toJSON(dataitem);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				log.error("对象转化为JSON失败");
			}
			newList.add(resultJson);
		}
		int size = dataItems.size();
		Map map =  new HashMap();
		map.put("total", size);
		map.put("rows", newList);
		return map;
	}
	
}