/**
 * 
 */
package com.athena.authority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.IniShiroFilter;

import com.toft.core3.container.ComponentsException;
import com.toft.core3.web.context.WebSdcContext;
import com.toft.core3.web.context.support.WebSdcContextUtils;

/**
 * <p>Title:SDC 权限模块</p>
 *
 * <p>Description:权限控制过滤器</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class AthenaIniShiroFilter extends IniShiroFilter {
	//log4j
	private static final Log logger = LogFactory.getLog(AthenaIniShiroFilter.class);
	/**
	 * SDC 上下文
	 */
	private static WebSdcContext sdcContext;

	/**
	 * shiro和sdc平台集成初始化
	 */
	private void initContainer() {
		if (sdcContext == null) {
			//从request中获取sdc平台上下文
			sdcContext = WebSdcContextUtils.getWebSdcContext(getServletContext());
			//获取shiro工厂类
			ShiroFilterFactoryBean shiroFilterFactoryBean
			 	= sdcContext.getComponent(ShiroFilterFactoryBean.class);
			//初始化处理shiro工厂类
			if(shiroFilterFactoryBean!=null){
				PathMatchingFilterChainResolver filterChainResolver = new PathMatchingFilterChainResolver();
		        filterChainResolver.setFilterChainManager(shiroFilterFactoryBean.createFilterChainManager(filterConfig));
		        SecurityManager securityManager = shiroFilterFactoryBean.getSecurityManager();
		        setSecurityManager((WebSecurityManager)securityManager);
				setFilterChainResolver(filterChainResolver);
			}
		}
		if (sdcContext == null){//未找到sdc配置，抛出运行期异常
			throw new RuntimeException("Can not find container in this application");
		}
			
	}
	
	/* 
	 * 
	 * (non-Javadoc)
	 * @see org.apache.shiro.web.servlet.AbstractShiroFilter#doFilterInternal(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws ServletException, IOException {
		initContainer();
		HttpServletRequest request =  (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		//从request中获取session
        HttpSession session = request.getSession();
		//登录帐号信息获取
		Object sessionAccount = session.getAttribute(AuthorityConstants.SESSION_ACCOUNT_USER);
		AthenaAccount account = null;
		//登录帐号非空判断和类型转换
		if(sessionAccount instanceof AthenaAccount){
			account = (AthenaAccount)sessionAccount;
		}

		String ajax = request.getHeader("x-Requested-with");
		if((ajax != null && ajax.equals("XMLHttpRequest")) && sessionAccount == null){ 
			response.setHeader("sessionstatus", "timeout");   
			return; 
		}
		
		if(request.getParameter("pageusername") != null && !"".equals(request.getParameter("pageusername"))){
			String pageusername = request.getParameter("pageusername");
			String Username= account==null?"":account.getToftUser().getUsername();
			if(!pageusername.equals(Username)){
				response.setHeader("sessionstatus", "userchange");   
				return; 
			}
		}
		//获取配置的menuBuilder
		String menuBuilderComp = 
			sdcContext.getMessage(AuthorityConstants.KEY_MENUHTML_BUILDER);
		MenuHtmlBuilder menuBuilder;
		try {
			//根据配置文件初始化菜单创建器
			menuBuilder = sdcContext.getComponent(menuBuilderComp, MenuHtmlBuilder.class);
			//创建用户菜单
			menuBuilder.createMenuHtml(request, account);
			
			menuBuilder = sdcContext.getComponent("defaultMenuHtmlBuilder", MenuHtmlBuilder.class);
			menuBuilder.createMenuHtml(request, account);
			//根据配置文件初始化菜单创建器
			//menuBuilder = sdcContext.getComponent(menuBuilderComp, MenuHtmlBuilder.class);
			//创建用户菜单
			//menuBuilder.createMenuHtml(request, account);
			if(account!=null){
				Set<String> set = account.getMenuAndButtonsIds();
				Map<String,String> buttonMap = (Map)session.getAttribute("buttonMap");
				Map<String,String> pageButtonMap = (Map<String,String>)session.getAttribute("pageButtonMap");
				Map<String,List<String>> menuButtonMap = (Map<String,List<String>>)session.getAttribute("menuButtonMap");
				Map map = new HashMap();
				for(String func_id:set){
					String buttonName = "";
					String menuId  = "";
					if(func_id.startsWith("button_")){
						func_id = func_id.substring(7,func_id.length());
						buttonName = buttonMap.get(func_id);
						menuId = pageButtonMap.get(func_id);
					}else{
						menuId= func_id;
					}
					if(map.get(menuId)==null){
						List<String> list = new ArrayList<String>();
						if(buttonName!=null&&!"".equals(buttonName)){
							list.add(buttonName);
						}
						map.put(menuId, list);
					}else if(map.get(menuId)!=null){
						List<String> list = (List<String>)map.get(menuId);
						if(buttonName!=null&&!"".equals(buttonName)){
							list.add(buttonName);
							map.put(menuId, list);
						}
					}
					if(menuButtonMap.get(menuId) != null && buttonName.length()>0){
						List<String> buttonTemp = menuButtonMap.get(menuId);
						for(int i = 0;i<buttonTemp.size();i++){
							if(buttonName.equals(buttonTemp.get(i))){
								buttonTemp.remove(i);
							}
						}
					}
				}
				if(menuBuilderComp != null && "defaultMenuHtmlBuilder".equals(menuBuilderComp)){
					session.setAttribute("zbcZxc", "ZBC");
				}else{
					session.setAttribute("zbcZxc", "ZXC");
				}
				session.setAttribute("pageMap", map);
				session.setAttribute("menuButtonMap", menuButtonMap);
				//buttonStr = buttonStr.substring(0,buttonStr.length()-1);
			}
		
		} catch (ComponentsException e) {
			//输出错误信息到日记
			logger.error(e.getMessage());
		}
		//调用父类的方法
		super.doFilterInternal(servletRequest, servletResponse, chain);
	}
	
}
