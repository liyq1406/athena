package com.athena.util.cache;

/**
 * 缓存组件类
 * @author WL
 * @date 2011-11-4
 */
public class Comp {
	
	//组件名称
	private String name;
	
	//组件描述
	private String description;
	
	//是否启用组件
	private String isEnabled;
	
	//组件常量名
	private String globName;

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
	 * @return globName
	 */
	public String getGlobName() {
		return globName;
	}

	/**
	 * @param globName 要设置的 globName
	 */
	public void setGlobName(String globName) {
		this.globName = globName;
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
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 要设置的 name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
