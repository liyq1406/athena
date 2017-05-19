package com.athena.xqjs.module.anxorder.action;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.xqjs.entity.anxorder.Kucjscsb;
import com.athena.xqjs.module.anxorder.service.KucjscsbService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * @see    盘点历史
 * @author wuyichao
 *
 */
@Component
public class PandlsAction extends ActionSupport 
{
	
	@Inject
	private KucjscsbService kucjscsbService;
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(PandlsAction.class);
	
	//获取用户中心
	public LoginUser getUserInfo()
	{
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}
	
	/**
	 * @see init page
	 * @return
	 */
	public String initPandls()
	{
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "success";
	}
	   
	/**
	 * @see   query list
	 * @param kucjscsb
	 * @return
	 */
	public String queryPandls(@Param Kucjscsb kucjscsb)
	{
		Object obj = kucjscsbService.queryPandls(kucjscsb , this.getParams());
		if(null != obj)
		{
			if(obj instanceof String)
			{
				HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
				PrintWriter out = null;
				try
				{
					reqResponse.setContentType("text/html");
					reqResponse.setCharacterEncoding("UTF-8");
					out = reqResponse.getWriter();
				}
				catch (Exception e) 
				{
					logger.error(e);
				}
				finally
				{
					if (out != null) 
					{
						out.print(obj);
						out.flush();
						out.close();
					}
				}
			}
			else if(obj instanceof Map)
			{
				setResult("result",obj );
			}
		}
		return AJAX;
	}
	
}
