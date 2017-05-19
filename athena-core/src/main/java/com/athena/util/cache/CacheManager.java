package com.athena.util.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;

/**
 * 缓存管理类
 * @author WL
 * @date 2011-11-4
 */
@Component(scope = ComponentDefinition.SCOPE_SINGLETON)
public class CacheManager {  
	
	private static final Logger logger = Logger.getLogger(CacheManager.class);
	
	@Inject
	private ComponentManager com;
			
    private static HashMap cacheMap = null;   
    private static CacheManager singleton = new CacheManager();

    /**
     * 构造函数
     */
    private CacheManager() {   
    	cacheMap = new HashMap();    
    }  
    
    /**
     * 单例模式下获取实例
     * @return 实例对象
     */
    public static CacheManager getInstance() {
        return singleton;
    }
    
    /**
     * 获得缓存   
     * @param key 缓存的key
     * @return 返回缓存对象
     */
    private synchronized Cache getCache(String key) {   
        return (Cache) cacheMap.get(key);   
    }   
  
    /**
     * 判断缓存是否存在
     * @param key 缓存key
     * @return true-存在,false-不存在
     */
    private synchronized  boolean hasCache(String key) {   
        return cacheMap.containsKey(key);   
    }   
  
    /**
     * 清除所有缓存
     */
    public synchronized  void clearAll() {   
        cacheMap.clear();   
    }   
  
    // 清除某一类特定缓存,通过遍历HASHMAP下的所有对象，来判断它的KEY与传入的TYPE是否匹配   
    public synchronized  void clearAll(String type) {   
        Iterator i = cacheMap.entrySet().iterator();   
        String key;   
        ArrayList arr = new ArrayList();   
        try {   
            while (i.hasNext()) {   
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();   
                key = (String) entry.getKey();   
                if (key.startsWith(type)) { // 如果匹配则删除掉   
                    arr.add(key);   
                }   
            }   
            for (int k = 0; k < arr.size(); k++) {   
                clearOnly((String)arr.get(k));   
            }   
        } catch (Exception ex) {   
            ex.printStackTrace();   
        }   
    }   
  
    // 清除指定的缓存   
    public synchronized  void clearOnly(String key) {   
        cacheMap.remove(key);   
    }   
  
    // 载入缓存   
    public synchronized void putCache(String key, Cache obj) {   
        cacheMap.put(key, obj);   
    }   
  
    /**  
     * 获取缓存信息.  
     * @param key  
     * @return  
     */  
    public Cache getCacheInfo(String key) {   
        if (hasCache(key)) {   
            Cache cache = getCache(key);   
            if (cacheExpired(cache)) { // 调用判断是否终止方法   
                cache.setExpired(true);   
            }   
            return cache;   
        } else  
            return null;   
    }   
  
    /**  
     * 载入缓存信息.  
     * @param key  
     * @param obj  
     * @param dt  
     * @param expired  
     */  
    public void putCacheInfo(String key, Cache obj, long dt,   
            boolean expired) {   
        Cache cache = new Cache();   
        cache.setCachekey(key);   
        cache.setTimeOut(dt + System.currentTimeMillis()); // 设置多久后更新缓存   
        cache.setCacheValue(obj);   
        cache.setExpired(expired); // 缓存默认载入时，终止状态为FALSE   
        cacheMap.put(key, cache);   
    }   
  
    /**  
     * 重写载入缓存信息方法.  
     * @param key  
     * @param obj  
     * @param dt  
     */  
    public void putCacheInfo(String key, Cache obj, long dt) {   
        Cache cache = new Cache();   
        cache.setCachekey(key);   
        cache.setTimeOut(dt + System.currentTimeMillis());   
        cache.setCacheValue(obj);   
        cache.setExpired(false);   
        cacheMap.put(key, cache);   
    }   
       
    /**   
     * 判断缓存是否终止.  
     * @param cache  
     * @return  
     */  
    public  boolean cacheExpired(Cache cache) {   
        if (null == cache) { // 传入的缓存不存在   
            return false;   
        }   
        long nowDt = System.currentTimeMillis(); // 系统当前的毫秒数   
        long cacheDt = cache.getTimeOut(); // 缓存内的过期毫秒数   
        if (cacheDt <= 0 || cacheDt > nowDt) { // 过期时间小于等于零时,或者过期时间大于当前时间时，则为FALSE   
            return false;   
        } else { // 大于过期时间 即过期   
            return true;   
        }   
    }   
  
    /**  
     * 获取缓存中的大小.  
     * @return  
     */  
    public  int getCacheSize() {   
        return cacheMap.size();   
    }   
  
    /**  
     * 获取指定的类型的大小  
     * @param type  
     * @return  
     */  
    public  int getCacheSize(String type) {   
        int k = 0;   
        Iterator i = cacheMap.entrySet().iterator();   
        String key;   
        try {   
            while (i.hasNext()) {   
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();   
                key = (String) entry.getKey();   
                if (key.indexOf(type) != -1) { // 如果匹配则删除掉   
                    k++;   
                }   
            }   
        } catch (Exception ex) {   
            ex.printStackTrace();   
        }   
        return k;   
    }   
  
    /**  
     * 获取缓存对象中的所有键值名称.  
     * @return  
     */  
    public  ArrayList getCacheAllkey() {   
        ArrayList a = new ArrayList();   
        try {   
            Iterator i = cacheMap.entrySet().iterator();   
            while (i.hasNext()) {   
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();   
                a.add((String) entry.getKey());   
            }   
        } catch (Exception ex) {   
        } finally {   
            return a;   
        }   
    }   
  
    /**  
     * 获取缓存对象中指定类型的键值名称.  
     * @param type  
     * @return  
     */  
    public  ArrayList getCacheListkey(String type) {   
        ArrayList a = new ArrayList();   
        String key;   
        try {   
            Iterator i = cacheMap.entrySet().iterator();   
            while (i.hasNext()) {   
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();   
                key = (String) entry.getKey();   
                if (key.indexOf(type) != -1) {   
                    a.add(key);   
                }   
            }   
        } catch (Exception ex) {  
        	ex.printStackTrace();
        } finally{   
            return a;   
        } 
               
    } 
    
    
	/**
	 * 获取缓存数据集合
	 * @param sqlName ibatis的sql语句id
	 * @return 缓存数据集合
	 */
	@SuppressWarnings("finally")
	private List<CacheValue> getProperty(String sqlName){	
		
		List<CacheValue> list = new ArrayList<CacheValue>();
		
		try{
			list = com.getProperty(sqlName);
		}catch( Exception ex ){
			logger.error("cacheManager.getProperty() error :"+ex.getMessage());
		}finally{
			return list;
		}

	}
	
	
	/**
	 * 判断缓存是否存在
	 * @param cacheName
	 * @return true/false
	 * @author zhangl 2011/11/09
	 */
	public boolean isCache( String cacheName ){
		
		Cache cache = CacheManager.getInstance().getCacheInfo(cacheName);		
		if( null == cache ){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 重新查询刷新缓存
	 * @param cacheName 要刷新的缓存名
	 * @author zhangl 2011/11/09
	 */
	public void refreshCache(String cacheName){
		
		logger.info("刷新缓存:["+cacheName+"]");
		//或许要刷新的缓存对象
		Cache cache = CacheManager.getInstance().getCacheInfo(cacheName);
		//或许Property对象,根据ibatis的sql语句id刷新缓存内容
		Property pro = (Property) cache.getPropertyObject();
		cache.setCacheValue(getProperty(pro.getSqlName()));
		//放置缓存
		CacheManager.getInstance().putCache(cacheName, cache);
	}
	
	/**
	 * 直接刷新缓存
	 * @param cacheName 要刷新的缓存名
	 * @param object    要刷新的缓存
	 * @author zhangl 2011/11/09
	 */
	public void refreshCache(String cacheName, Object object ){
		
		logger.info("刷新缓存:["+cacheName+"]");
		//要刷新的缓存对象
		Cache cache = CacheManager.getInstance().getCacheInfo(cacheName);
		cache.setCacheValue(object);
		//放置缓存
		CacheManager.getInstance().putCache(cacheName, cache);
	}
	
	/**
	 * 重新查询新增缓存
	 * @param cacheName		缓存键值
	 * @param sqlName       sql语句id
	 * @param description   缓存名称或者描述
	 * @author zhangl 2011/11/09
	 */
	public void createCache( String cacheName, String  sqlName, String description ){

		if( isCache(cacheName) ){
			logger.info("新增缓存:["+cacheName+"]，但是该缓存已经存在，不做新增操作");
			return;
		}else{
			
			logger.info("新增缓存:["+cacheName+"]");
			
			//组件类
			Comp com = new Comp();
			//组件名
			com.setName("SelectMap");
			//描述
			com.setDescription("下拉框缓存组件");
			//启用状态
			com.setIsEnabled("true");
			//属性名
			com.setGlobName("glob_component_select");
			
			//Property对象
			Property pro = new Property();
			//对应的ibatis的sql语句id
			pro.setSql(sqlName);
			//缓存名
			pro.setCacheName(cacheName);
			//缓存描述
			pro.setDescription(description);
			//缓存启用状态
			pro.setIsEnabled("true");
			
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
		}
	}
	
	/**
	 * 直接新增缓存
	 * @param cacheName		缓存键值
	 * @param sqlName       sql语句id
	 * @param description   缓存名称或者描述
	 * @param object        缓存
	 * @author zhangl 2011/11/09
	 */
	public void createCache( String cacheName, String  sqlName, String description, Object object ){

		if( isCache(cacheName) ){
			logger.info("新增缓存:["+cacheName+"]，但是该缓存已经存在，不做新增操作");
			return;
		}else{
			
			logger.info("新增缓存:["+cacheName+"]");
			
			//组件类
			Comp com = new Comp();
			//组件名
			com.setName("SelectMap");
			//描述
			com.setDescription("下拉框缓存组件");
			//启用状态
			com.setIsEnabled("true");
			//属性名
			com.setGlobName("glob_component_select");
			
			//Property对象
			Property pro = new Property();
			//对应的ibatis的sql语句id
			pro.setSql(sqlName);
			//缓存名
			pro.setCacheName(cacheName);
			//缓存描述
			pro.setDescription(description);
			//缓存启用状态
			pro.setIsEnabled("true");
			
			//缓存对象
			Cache cache = new Cache();
			//缓存的key
			cache.setCachekey(pro.getCacheName());
			//缓存的value
			cache.setCacheValue(object);	
			//组件对象
			cache.setComponentObject(com);
			//property对象
			cache.setPropertyObject(pro);
			
			//放置缓存
			CacheManager.getInstance().putCache(pro.getCacheName(), cache);
		}
	}
}  
