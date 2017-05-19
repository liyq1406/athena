package com.athena.util.cache;

/**
 * Property属性类
 * @author WL
 * @date 
 */
public class Property {
	
	//sql编号
	private String sqlName;
	
	//缓存编号
	private String cacheName;
	
	//缓存描述
	private String description;
	
	//是否启用
	private String isEnabled;
	

	/**
	 * @return cacheName
	 */
	public String getCacheName() {
		return cacheName;
	}

	/**
	 * @param cacheName 要设置的 cacheName
	 */
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description 要设置的 description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return isEnabled
	 */
	public String getIsEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled 要设置的 isEnabled
	 */
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * @return sqlName
	 */
	public String getSqlName() {
		return sqlName;
	}

	/**
	 * @param sqlName 要设置的 sqlName
	 */
	public void setSql(String sqlName) {
		this.sqlName = sqlName;
	}
	
	
}
