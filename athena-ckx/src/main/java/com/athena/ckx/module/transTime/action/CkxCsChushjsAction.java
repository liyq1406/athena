/**
 * 
 */
package com.athena.ckx.module.transTime.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.module.transTime.service.CkxYunsskService;
import com.athena.component.service.Message;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.athena.ckx.module.transTime.service.CkxChushjsService;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author xss
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxCsChushjsAction  extends BaseWtcAction {
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}

	/* log4j日志初始化
	private final Log log = LogFactory.getLog(CkxCsChushAction.class);
	*/

	@Inject
	private CkxChushjsService CkxChushjsService;
	
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
	
	
		/***
	 * 查询产线
	
	public String selectChanx(@Param Kanbxhgm kbxh) {
		setResult("result", kanbxhkService.selectChanx(getParams()));
		return AJAX;

	}
	 */
	
	/***
	 * 验证用户名和密码
	 
	 */
	public String authUser() {
		Map<String,String> param = getParams();
		String username = param.get("username");
		String password1 = param.get("Password");
		
		param.put("password", new Md5Hash(password1).toHex() );

			
		Map<String,Object> result = new HashMap<String, Object>();
	
		List message= CkxChushjsService.selectUser(param);
		
		if(message.size()!=0){//成功
			setResult("flag", 1);
        }else{//失败
        	setResult("flag", 0);
        }
		setResult("result", result);
		return AJAX;

	}
	
	
	private Object Md5Hash(String password1) {
			// TODO Auto-generated method stub
			return null;
		}


	/*
		public String flagJudge624() {
		// 获取session数据
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String rongqcbh = this.getParam("rongqqbh");
		Map  map = new HashMap();
		map.put("usercenter",loginUser.getUsercenter());
		map.put("rongqcbh",rongqcbh);
		List<Rongqc> rqcList =  commonService.flagJudge624(map);
		// 1 表示通过查询条件可以获取数据 0 表示没有获取数据 
		ArrayList list = new ArrayList();
		if(rqcList.size()==0){
			list.add("{name:\"judge\",value:\"0\"}");
		}else{
			list.add("{name:\"judge\",value:\"1\"}");
		}

		// 拼Json串
		String sJson = JSONArray.fromObject(list).toString();
		CommonEntity commonEntity = new CommonEntity();
		commonEntity.setParameter(sJson);
		JSONObject obj = JSONObject.fromObject(commonEntity);
		setResult("result", obj);
		return AJAX;
	}
	*/
    
	/**
	 *  CS初始化计算
	 * 
	 * @return
	 */
	public String jisuan(){
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
			WtcResponse wtcResponse = callWtc(usercenter,"CS132", param);
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
	 *  CS初始化计算查询数量
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
			//String currentPage = param.get("pageNo");
			String usercenter = param.get("usercenter");
			//param.put("currentPage", currentPage);
			//调用WTC查询
			WtcResponse wtcResponse = callWtc(usercenter,"CS131", param);
			//获取WTC
			response = strNull(wtcResponse.get("response"));
			if(response.equals("0000")){
				Map wtcParameter = (Map) wtcResponse.get("parameter");
				//总页数信息重置
				result.put("total", strNull(wtcResponse.get("total_page")));
				result.put("rows", wtcParameter.get("list"));
				result.put("l1", wtcParameter.get("l1"));
				result.put("l5", wtcParameter.get("l5"));
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
