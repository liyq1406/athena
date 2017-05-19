package com.athena.util.cache;

/**
 * 打印报表模板属性类
 * @author ZL
 * @date 
 */
public class PropertyModule {
	
	//模板路径
	private String path;
	
	//缓存编号
	private String cacheName;
	
	//缓存描述
	private String description;
	
	//是否启用
	private String isEnabled;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	

}
