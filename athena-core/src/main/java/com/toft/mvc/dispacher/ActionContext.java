package com.toft.mvc.dispacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.toft.mvc.proxy.ActionProxy;

public class ActionContext
{
  private static ThreadLocal<ActionContext> actionContextHolder = new ThreadLocal();
  private HttpServletRequest request;
  private HttpServletResponse response;
  private String acionPath;
  private Map<String, String> params;
  private List<String> errorMessages;
  private ActionProxy actionProxy;
  private Map<String, Object> result;

  public ActionContext(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, Map<String, String> paramMap)
  {
    this.request = paramHttpServletRequest;
    this.response = paramHttpServletResponse;
    this.params = paramMap;
    this.result = new HashMap();
    this.errorMessages = new ArrayList();
  }

  public static void setActionContext(ActionContext paramActionContext)
  {
    actionContextHolder.set(paramActionContext);
  }

  /**
   * modify by zhangl 20120425
   * @return
   */
  public static ActionContext getActionContext(){
	  
	ActionContext action  = null;
	try{
		action = (ActionContext)actionContextHolder.get();
	}catch(Exception e){
		return null;
	}
	return action;	
  }

  public HttpServletRequest getRequest()
  {
    return this.request;
  }

  public HttpServletResponse getResponse()
  {
    return this.response;
  }

  public String getAcionPath()
  {
    return this.acionPath;
  }

  public void setAcionPath(String paramString)
  {
    this.acionPath = paramString;
  }

  public List<String> getErrorMessages()
  {
    return this.errorMessages;
  }

  public void setErrorMessage(String paramString)
  {
    this.errorMessages.add(paramString);
  }

  public Map<String, String> getParams()
  {
    return this.params;
  }

  public String getParam(String paramString)
  {
    return (String)this.params.get(paramString);
  }

  public ActionProxy getActionProxy()
  {
    return this.actionProxy;
  }

  public void setActionProxy(ActionProxy paramActionProxy)
  {
    this.actionProxy = paramActionProxy;
  }

  public Map<String, Object> getResult()
  {
    return this.result;
  }

  public void setResult(String paramString, Object paramObject)
  {
    this.result.put(paramString, paramObject);
  }
}