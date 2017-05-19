/**
 * 
 */
package com.athena.ckx.module.transTime.action;

import java.util.ArrayList;
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
import com.toft.mvc.annotaions.Param;
import com.athena.ckx.entity.transTime.CkxCschush;
import com.athena.ckx.entity.transTime.CkxCsChushlk;
import com.athena.ckx.entity.xuqjs.Gongysxhc;
import com.athena.ckx.entity.xuqjs.XiaohcmbCk;
import com.athena.ckx.module.xuqjs.service.GongysxhcService;
import com.athena.ckx.module.xuqjs.service.XiaohcmbCkService;
import com.athena.db.ConstantDbCode;
import com.athena.util.CommonUtil;

/**
 * @author xss
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxCsChushlkAction  extends BaseWtcAction {
	
	@Inject
	private XiaohcmbCkService xiaohcmbCkService;
	
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
	 * 跳转页面
	 * @param bean
	 * @return
	 */
	public String xinExecute() {
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return  "selectXin";
	}
	
	
	/**
	 *  CS初始化拉空
	 * 
	 * @return
	 */
	public String lakong(@Param("edit") ArrayList<CkxCsChushlk> edit){
		String loginfo = "";
		String response = "";
		try {
			Map<String,String> param1 = this.getParams();
			String usercenter = param1.get("usercenter");
			String chanx = param1.get("chanx");
			
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("chanx", chanx);
			param.put("usercenter", usercenter);
			param.put("list",edit);
			
			
			Map<String,Object> result = new HashMap<String, Object>();
			//调用WTC查询
			WtcResponse wtcResponse = callWtc(usercenter,"CS122", param);
			//获取WTC
			response = strNull(wtcResponse.get("response"));

			if(response.equals("0000")){
				Map wtcParameter = (Map) wtcResponse.get("parameter");
				//总页数信息重置
				result.put("total", strNull(wtcResponse.get("total_page")));
				result.put("rows", wtcParameter.get("list"));
				result.put("response", response);
				setResult("result", result);
			}else{
				Message message = new Message(response, "i18n.ckx.transTime.i18n_cschushlk");
				setResult("result", message);		
			}
		} catch (Exception e) {
			loginfo = "调用WTC查询异常"+replaceBlank(e.toString())+response;
			}
		return AJAX;
	}
	
	
	
	

	/**
	 *  CS初始化拉空查询
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
			WtcResponse wtcResponse = callWtc(usercenter,"CS121", param);
			//获取WTC
			response = strNull(wtcResponse.get("response"));
			Message message = new Message(response, "i18n.ckx.transTime.i18n_cschushlk");
			setResult("result", message);	
			
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
	
	/**
	 *  新CS初始化拉空查询
	 * 
	 * @return
	 */
	public String xinQuery(@Param CkxCsChushlk ckxCsChushlk){
		try {
			Map<String, Object> resultMap = xiaohcmbCkService.selectXiaohcmb(ckxCsChushlk);
			setResult("result", resultMap);		
			//userOperLog.addCorrect(CommonUtil.MODULE_CKX, "供应商小火车", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			//userOperLog.addError(CommonUtil.MODULE_CKX, "供应商小火车", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}		
		return AJAX;
	}
	
	/**
	 *  新CS初始化拉空
	 * 
	 * @return
	 */
	public String xinLakong(@Param("edit") ArrayList<CkxCsChushlk> edit){
		try {
			for(CkxCsChushlk item:edit){				
				item.setEditor(getLoginUser().getUsername());
			}
			String result = xiaohcmbCkService.xiaohcmbLk(edit);
			if(!"success".equals(result)){
				Map<String,String> message = new HashMap<String,String>();
				message.put("message", result);
				setResult("success",false);
				setResult("result", message);
			}
			setResult("result", result);
		} catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			setResult("success",false);
			setResult("result", message);
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
