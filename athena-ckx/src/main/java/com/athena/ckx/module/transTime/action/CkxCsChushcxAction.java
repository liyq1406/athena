/**
 * 
 */
package com.athena.ckx.module.transTime.action;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;

/**
 * @author xss
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxCsChushcxAction  extends BaseWtcAction {
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}

	/* log4j日志初始化
	private final Log log = LogFactory.getLog(CkxCsChushAction.class);
	*/

	
	@Inject
	LoginUser loginUser = AuthorityUtils.getSecurityUser();
    //String cklist = (String) loginUser.getParams().get("cklist");
	
	/**
	 * 获取当前用户信息
	 * @author xss
	 * @Date 2014-9-09
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}

    
	/**
	 * 跳转页面
	 * @param bean
	 * @return
	 */
	public String execute() {
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return  "select";
	}
    

	/**
	 *  CS初始化历史查询
	 * 
	 * @return
	 */
	public String query(){
		String loginfo = "";
		String response = "";
		try {
			//返回结果
			Map<String,Object> result = new HashMap<String, Object>();
			//返回结果
			Map<String,String> param = getParams();
			String currentPage = param.get("pageNo");
			String usercenter = param.get("usercenter");
			param.put("currentPage", currentPage);
			//调用WTC查询
			WtcResponse wtcResponse = callWtc(usercenter,"CS141", param);
			//获取WTC
			response = strNull(wtcResponse.get("response"));
			Message message = new Message(response, "i18n.ckx.transTime.i18n_cschushcx");
			result.put("response", message);
			
			if(response.equals("0000")){
				Map wtcParameter = (Map) wtcResponse.get("parameter");
				//总页数信息重置
				result.put("total", strNull(wtcResponse.get("total_page")));
				result.put("rows", wtcParameter.get("list"));			
				setResult("result", result);
			}
		} catch (Exception e) {
			loginfo = "调用WTC查询异常"+replaceBlank(e.toString())+response;
			}
		return AJAX;
	}
	
	public static String strNull(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}
	
	public static String replaceBlank(String str) {  
		String dest = "";  
		if ( null != str ) {  
		    Pattern p = Pattern.compile("\t|\r|\n");  
		    Matcher m = p.matcher(str);  
		    dest = m.replaceAll("");  
		    str = dest;
		}  
		return str;  
	} 
	
	
	
	
}
