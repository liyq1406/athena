package com.athena.util.cache;

/**
 * @author zhangl
 */
/**
 * @description 缓存类
 *
 */
public class Cache {
	
	/**
	 * @description 缓存的key
	 */
	private String cachekey;
	
	/**
	 * @description 缓存的值
	 */
	private Object cacheValue;
	
	/**
	 * @description 缓存更新时间
	 */
	private long timeOut;
	
	/**
	 * @description 是否终止
	 */
	private boolean expired;  
	
	/**
	 * @description 组件对象
	 */
	private Object componentObject;
	
	/**
	 * @description 部件对象
	 */
	private Object propertyObject;

	/**
	 * @return cachekey 缓存的key
	 */
	public String getCachekey() {
		return cachekey;
	}

	/**
	 * @param cachekey 要设置的 缓存的key
	 */
	public void setCachekey(String cachekey) {
		this.cachekey = cachekey;
	}

	/**
	 * @return cacheValue 缓存的值
	 */
	public Object getCacheValue() {
		return cacheValue;
	}

	/**
	 * @param cacheValue 要设置的 缓存的值
	 */
	public void setCacheValue(Object cacheValue) {
		this.cacheValue = cacheValue;
	}

	/**
	 * @return componentObject 组件对象
	 */
	public Object getComponentObject() {
		return componentObject;
	}

	/**
	 * @param componentObject 要设置的 组件对象
	 */
	public void setComponentObject(Object componentObject) {
		this.componentObject = componentObject;
	}

	/**
	 * @return expired 是否终止
	 */
	public boolean isExpired() {
		return expired;
	}

	/**
	 * @param expired 要设置的 是否终止
	 */
	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	/**
	 * @return propertyObject 部件对象
	 */
	public Object getPropertyObject() {
		return propertyObject;
	}

	/**
	 * @param propertyObject 要设置的 部件对象
	 */
	public void setPropertyObject(Object propertyObject) {
		this.propertyObject = propertyObject;
	}

	/**
	 * @return timeOut 缓存更新时间
	 */
	public long getTimeOut() {
		return timeOut;
	}

	/**
	 * @param timeOut 要设置的 缓存更新时间
	 */
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
	
	

}

