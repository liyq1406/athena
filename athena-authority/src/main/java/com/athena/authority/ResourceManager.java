/**
 * 
 */
package com.athena.authority;

import java.util.Map;

import com.toft.ui.tags.tree.HtmlTreeNode;


/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public interface ResourceManager {

	public Map<String, String> getFilterChainDefinitionMap();
	
	public HtmlTreeNode getMenuTree();

}
