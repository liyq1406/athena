package com.athena.module.athena;

import java.util.HashMap;
import java.util.Map;

import com.athena.db.ConstantDbCode;
import com.athena.util.cache.Cache;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


/**
 * 公用类
 * 查询其他表填充到select
 * @author hj
 * @date 2011-11-3
 *
 */
@Component
public class SelectService{
	/**
	 * 下拉框 
	 * @author hj
	 * @date 2011-11-3
	 * @return Map
	 */
	@Inject
	private AbstractIBatisDao baseDao;
	@Inject
	private CacheManager cacheManager;
	
	public  Map selectMap(String sqlMapId){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectMap(sqlMapId);
	}
	public Map selectMap(String cacheName,String sqlMapId,String description) throws ServiceException{
		Map map=new HashMap();
		//判断缓存是否存在
		if(cacheManager.isCache(cacheName)){
			//如果缓存存在，就直接读取缓存
			Cache cache=cacheManager.getCacheInfo(cacheName);
			map=(Map)cache.getCacheValue();
			//LogUtils.add("缓存拿值"+cacheName);
		}else{
			map=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectMap(sqlMapId);
			//如果不存在cacheName缓存，就创建一个缓存
			cacheManager.createCache(cacheName, sqlMapId, description,map);
			//LogUtils.add("新增缓存"+cacheName);
		}
		return map;
	}
}
