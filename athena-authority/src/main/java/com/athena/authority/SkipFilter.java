package com.athena.authority;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.db.ConstantDbCode;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.web.context.WebSdcContext;
import com.toft.core3.web.context.support.WebSdcContextUtils;
/**
 * 仓库----参考系的 跳转
 * @author chenlei
 * @vesion 1.0
 * @date 2012-6-28
 */
public class SkipFilter implements Filter {
	private static final String MONITOR_URI_END = "qiehuan.do"; //监听的路径
	private static final String MONITOR_PARAM_NAME="qiehuan_hidden"; //参数的名称
	private static final String MONITOR_PARAM_CK = "cangku";
	private static final String PATH_TO_CK="/index_ck.jsp";//跳转到仓库主页
	private static final String PATH_TO_CKX = "/index.jsp"; //跳转到参考系主页
	
	private static WebSdcContext sdcContext;
	private  AbstractIBatisDao baseDao;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		if(sdcContext==null){
			sdcContext = WebSdcContextUtils.getWebSdcContext(filterConfig.getServletContext());
		}
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
	
		HttpServletRequest hsr = (HttpServletRequest) request;
		String uri = hsr.getRequestURI();
		if(uri!=null && uri.endsWith(MONITOR_URI_END)){
			String skip = hsr.getParameter(MONITOR_PARAM_NAME);			
			
			LoginUser user = AuthorityUtils.getSecurityUser(); //得到当前用户对象
			//有权限跳转
			if(MONITOR_PARAM_CK.equals(skip)){
				//想往仓库跳转
				if(isSkip(".isHasCKSkip",user)){
					//有权限则执行跳转
					SecurityUtils.getSubject().getSession().setAttribute("sysLoginMode", "yck");
					request.getRequestDispatcher(PATH_TO_CK).forward(request, response);
				}else{
					//没有权限 则跳回 参考系主页
					request.getRequestDispatcher(PATH_TO_CKX).forward(request, response);
				}				
			}else{
				//向往参考系跳转
				if(isSkip(".isHasCKXSkip",user)){
					SecurityUtils.getSubject().getSession().setAttribute("sysLoginMode", "nck");
					//有权限则执行跳转
					request.getRequestDispatcher(PATH_TO_CKX).forward(request, response);
				}else{
					//没有权限 则跳回 仓库主页
					request.getRequestDispatcher(PATH_TO_CK).forward(request, response);
				}					
			}
		}		
	}

	public void destroy() {
		
	}
	
	/**
	 * 是否有权限跳转
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isSkip(String sqlxmlId,LoginUser user){
		boolean isSkip = true;
		
		if(user!=null){
			baseDao = sdcContext.getComponent(AbstractIBatisDao.class);
			//查找当前用户对应的用户组，是否有 仓库组 
			//有仓库组  则为真
			//没有  则为假
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(AuthorityConstants.MODULE_NAMESPACE+sqlxmlId,user);
			BigDecimal db = (BigDecimal) ((HashMap)(list.get(0))).get("ID");
			int count = db.intValue();
			if(count<1){
				//如果没有对应的菜单，则认为没有权限
				isSkip = false;
			}
		}else{
			isSkip = false;   //没有当前用户登录，则返回false
		}
		
		return isSkip;
	}

}
