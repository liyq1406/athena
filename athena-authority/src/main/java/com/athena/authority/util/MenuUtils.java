/**
 * 
 */
package com.athena.authority.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.athena.authority.entity.MenuDirectory;
import com.toft.ui.tags.tree.HtmlTreeNode;


/**
 * <p>Title:SDC 权限控制组件</p>
 *
 * <p>Description:菜单工具类</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public abstract class MenuUtils {

	public final static String MENU_PARAMS_SELECTED = "menu.selected";//当前选择菜单参数
	/**
	 * 使用树的子节点创建垂直菜单（左边栏）
	 * 
	 * @param treeNode
	 *            树节点
	 * @return
	 */
	public static String buildVerMenuHtml(HtmlTreeNode treeNode,String selected,String contextPath) {
		//
		StringBuffer htmls = new StringBuffer();
		// List<MenuDirectory> menus
		List<HtmlTreeNode> menus = treeNode.getChildren();

		for (HtmlTreeNode menu : menus) {
			htmls.append("<div class=\"menu-bar-title selected\">");
			htmls.append("	<span class=\"menu-span\">" + menu.getText()
					+ "</span>");
			htmls.append("</div>");

			//菜单
			htmls.append("<div class=\"menu-bar-content-up\"></div>");
			htmls.append("<div class=\"menu-bar-content\">");
			htmls.append(	buildChildMenuItems(menu,selected,contextPath,true));
			htmls.append("</div>");
			htmls.append("<div class=\"menu-bar-content-down\"></div>");
		}

		return htmls.toString();
	}

	private static String buildChildMenuItems(HtmlTreeNode menu,String selected,String contextPath,boolean isRoot) {
		StringBuffer htmls = new StringBuffer();

		List<HtmlTreeNode> children = menu.getChildren();
		boolean hasChild = children.size()>0;
		
		Object bean = menu.getBean();
		
		String src = null;
		if(bean instanceof MenuDirectory){
			if(((MenuDirectory)bean).getDirPath()!=null){
				src = ((MenuDirectory)bean).getDirPath();
			}
		}
		
		if (src == null || src.trim().equals("")) {
			src = "###";
		}else{
			if(contextPath!=null&&src.startsWith("/")){
				src= contextPath+src;
			}
			
			src = src+(src.indexOf("?")==-1?"?":"&")+MENU_PARAMS_SELECTED+"="+menu.getId();
		}
		String selectedStyle = menu.getId().equals(selected)?" selected":"";
		if(!isRoot){
			htmls.append("<li id=\""+menu.getId()+"\" class=\"menu-item "+selectedStyle);
			htmls.append("\"><span class=\"menu-span");
			if(hasChild){
				htmls.append(" expandable expanded");
			}
		htmls.append("\"><a class=\"menu-a "+selectedStyle+"\" href='" + src+ "'>"+menu.getText()+"</a></span>");
		}
		if (hasChild) {
			htmls.append("<ul>");
			for (HtmlTreeNode child : children) {
				htmls.append(buildChildMenuItems(child,selected,contextPath,false));
			}
			htmls.append("</ul>");
		}
		if(!isRoot){
			htmls.append("</li>");
		}
		return htmls.toString();
	}
	
	/**
	 * @param menuTree
	 * @return
	 */
	public static List<HtmlTreeNode> findMenuNodeTrace(HtmlTreeNode menuTree,String menuId,
			List<HtmlTreeNode> parentTraces){
		
		List<HtmlTreeNode> list = null;
		if(parentTraces==null){
			list = new ArrayList<HtmlTreeNode>();
		}else{
			list = parentTraces;
		}
		
		//
		List<HtmlTreeNode> traces = new ArrayList<HtmlTreeNode>();
		List<HtmlTreeNode> children = menuTree.getChildren();
		
		List<HtmlTreeNode> childTraces;
		
		for(HtmlTreeNode child:children){
			if(child.getId().equals(menuId)){
				traces.addAll(list);
				traces.add(child);
				return traces;
			}
			childTraces = findMenuNodeTrace(child,menuId,parentTraces);
			if(childTraces!=null&&childTraces.size()>0){//找到
				return childTraces;
			}
			childTraces = null;
		}
		return traces;
	}
	
	/**
	 * 树过滤
	 * @param children 需要过滤的第一层子节点
	 * @param menuAndButtons 过滤集合
	 * @param newParentNode 存储过滤后的树
	 * @return
	 */
	public static HtmlTreeNode filterTree(List<HtmlTreeNode> children,
			Set<String> menuAndButtons,
			HtmlTreeNode newParentNode){
		for(HtmlTreeNode child:children){
			if(menuAndButtons.contains(child.getId())){//找到子节点
				HtmlTreeNode newNode = child.cloneExcludeChildren();
				newParentNode.addChild(newNode);
				filterTree(child.getChildren(),menuAndButtons,newNode);
			}
		}
		return newParentNode;
	}
}
