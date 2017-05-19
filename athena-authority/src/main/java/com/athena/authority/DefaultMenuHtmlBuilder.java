/**
 * 
 */
package com.athena.authority;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.athena.authority.service.MenuDirectoryService;
import com.athena.authority.util.AuthorityUtils;
import com.athena.authority.util.MenuUtils;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.web.context.WebSdcContext;
import com.toft.core3.web.context.support.WebSdcContextUtils;
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
@Component("defaultMenuHtmlBuilder")
public class DefaultMenuHtmlBuilder implements MenuHtmlBuilder {

	@Inject
	private MenuDirectoryService menuDirectoryService;
	
	/* 
	 * 系统布局不使用frame，所有的内容都来自于一个page内，
	 * 需要每次都生成html以记录当前选择的菜单；
	 * 默认的菜单分横向和纵向两部分；
	 * 系统使用后台重新生成菜单的展开样式（适合后台并发少的系统，比如准备层）；
	 * (non-Javadoc)
	 * @see com.athena.authority.MenuHtmlBuilder#createMenuHtml()
	 */
	public void createMenuHtml(HttpServletRequest request,AthenaAccount account) {
		//获取session
		HttpSession session = request.getSession();
		if(account==null){//没有登录信息时清除历史缓存
			session.removeAttribute(AuthorityConstants.SESSION_SYS_LOGIN_H_MENUHTML);
			session.removeAttribute(AuthorityConstants.SESSION_SYS_LOGIN_MENUHTML);
			return;
		}
		//从request中获取sdc平台上下文
		WebSdcContext context =
			WebSdcContextUtils.getWebSdcContext(request.getSession().getServletContext());
		
		//获取web上下文路径
		String contextPath = request.getContextPath();
		//如果缓存中存在则不重新生成
		HtmlTreeNode sysMenuTree = null;
		if(menuDirectoryService!=null){
			//获取系统菜单树，有缓存,如果SESSION中存在所有的菜单,则不重新生成菜单信息,取SESSION中的菜单信息
			if(session.getAttribute(AuthorityConstants.SESSION_SYS_MENU)==null){
				sysMenuTree = menuDirectoryService.getSysMenuTree();
				session.setAttribute(AuthorityConstants.SESSION_SYS_MENU, sysMenuTree);
			}else if(session.getAttribute(AuthorityConstants.SESSION_SYS_MENU)!=null){
				sysMenuTree =  (HtmlTreeNode)session.getAttribute(AuthorityConstants.SESSION_SYS_MENU);
			}
			//创建用户树的跟节点，使用不带子节点的克隆方法
			HtmlTreeNode userMenuTree = sysMenuTree.cloneExcludeChildren();
			List<HtmlTreeNode> list = sysMenuTree.getChildren();
			//帐号过滤，当为开发用户时使用全权限
			//实际运行时需要关闭AthenaAuthority.properties配置文件中的user.developer项
			if(!AuthorityUtils.isDeveloper(context,account.getToftUser())){//非开发用户过滤权限
				MenuUtils.filterTree(list,account.getMenuAndButtonsIds(),userMenuTree);
			}else{//开发用户不过滤权限
				userMenuTree = sysMenuTree;
			}
			int size = userMenuTree.getChildren().size();
			if(size>0){
				//当前定位的菜单ID
				String selectedMenuId = getSelectedMenuId(request,session);
				//当前定位的module
				int index =  getMenuModuleIndex(request, session, userMenuTree, selectedMenuId);
		        //左边栏菜单树
				if(userMenuTree!=null&&index>size){
					index = userMenuTree.getChildren().size();
				}
				//获取当前定位的横向菜单
		        HtmlTreeNode vTree = userMenuTree.getChildren().get(index);
		        //横向菜单
		        session.setAttribute(AuthorityConstants.SESSION_SYS_LOGIN_H_MENUHTML,
		        this.buildHMenuHtml(userMenuTree, index, contextPath));
		        //纵向菜单
		        session.setAttribute(AuthorityConstants.SESSION_SYS_LOGIN_MENUHTML,
		        MenuUtils.buildVerMenuHtml(vTree,selectedMenuId==null?null:selectedMenuId,contextPath));
			}
			
		}
	}

	/**
	 * 创建横向菜单的HTML
	 * @param userMenuTree
	 * @param index
	 * @param contextPath
	 * @return
	 */
	private String buildHMenuHtml(HtmlTreeNode userMenuTree,int index,String contextPath){
		//横向菜单
        StringBuffer hMenuHtmls = new StringBuffer();
        int i = 0;
        for(HtmlTreeNode hTreeNode:userMenuTree.getChildren()){
        	hMenuHtmls.append("<span class=\"v-menu-bar "+(i==index?" selected":"")+"\"><a href=\""+contextPath+"/authority/sys_login_module.do?moduleIndex="+(i++)+"\">");
        	hMenuHtmls.append(hTreeNode.getText());
        	hMenuHtmls.append("</a></span>");
        }
        return hMenuHtmls.toString();
	}
	/**
	 * 获取当前访问的菜单
	 * 1.首先从request的参数【menu.selected】中查找
	 * 2.未找到则从session属性【menu.selected】中查找
	 * 3.找到则设置到session中
	 * @param request
	 * @param session
	 * @return
	 */
	private String getSelectedMenuId(HttpServletRequest request,HttpSession session){
		//当前选择的菜单
		Object selectedMenuId = //从request参数中取
			request.getParameter(MenuUtils.MENU_PARAMS_SELECTED);
		if(selectedMenuId==null){//从session中取
			selectedMenuId = session.getAttribute(MenuUtils.MENU_PARAMS_SELECTED);
		}
		if(selectedMenuId!=null){//设置到session中
			session.setAttribute(MenuUtils.MENU_PARAMS_SELECTED, selectedMenuId.toString());
		}
		return selectedMenuId == null?null:selectedMenuId.toString();
	}
	/**
	 * 获取当前访问的模块定位
	 * 1.首先从request的参数【moduleIndex】中查找
	 * 2.未找到则从session属性【moduleIndex】中查找
	 * 3.根据以选择的菜单从菜单树中查找
	 * 4.找到则设置到session中
	 * @param request
	 * @param session
	 * @return
	 */
	private int getMenuModuleIndex(HttpServletRequest request
			,HttpSession session,
			HtmlTreeNode userMenuTree,
			String selectedMenuId){
		int index = 0;
		Object moduleIndex =  request.getParameter("moduleIndex");
        if(moduleIndex==null||moduleIndex.equals("")){
        	moduleIndex =  session.getAttribute("moduleIndex");
        }
        
    	if(moduleIndex==null&&selectedMenuId!=null){//从菜单树中查找
           List<HtmlTreeNode> traces = 
           		MenuUtils.findMenuNodeTrace(userMenuTree, selectedMenuId, null);
           if(traces.size()>1){
        	   for(HtmlTreeNode hTreeNode:userMenuTree.getChildren()){
        		   if(hTreeNode.getId().equals(traces.get(1).getId())){
        			   index++;
        			   moduleIndex = index+"";
        			   break;
        		   }
        	   }
           }
    	}
        //设置到session
        session.setAttribute("moduleIndex",moduleIndex);
        
        if(moduleIndex!=null){
        	try {
				index = Integer.parseInt(moduleIndex.toString());
			} catch (NumberFormatException e) {
				//
			}
        }
        return index;
	}
}
