package com.athena.pc.module.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.pc.common.CollectionUtil;
import com.athena.pc.entity.Yuemnjh;
import com.athena.pc.module.service.RigdService;
import com.athena.pc.module.service.YuemnjhgzService;
import com.athena.util.exception.ActionException;
import com.athena.util.exception.ServiceException;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;
import com.toft.mvc.interceptor.supports.log.Log;

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YuemnjhgzAction extends ActionSupport {
	//注入service
	@Inject
	private YuemnjhgzService yuemnjhgzService;
	
	@Inject
	private RigdService rigdService;
	@Inject
	private DownLoadServices downLoadServices = null;
	
	/**
	 * 获取用户信息
	 */	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	/**
	 * 获取用户信息
	 * @return 用户信息
	 */
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}	
	
	/**
	 * 执行跳转页面方法
	 * @author 贺志国
	 * @date 2012-2-9
	 * @return String 返回跳转
	 */
	@Log(description="accessYuemnjhgz",content="{Toft_SessionKey_UserData.userName}执行了：跳转操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String accessYuemnjhgz(){
		Map<String,String> param = this.getParams();
		param.put("USERCENTER",loginUser.getUsercenter());
//		String jihychanxz = loginUser.getJihyz();
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihychanxz = postMap.get("PCJIHY");
		param.put("JIHYZBH", jihychanxz);
		List<Map<String,String>> chanxList = rigdService.selectChanx(param);
		String shengcx = "''";
		if(chanxList.size()>0){
			shengcx = chanxListToString(chanxList);
			setResult("chanxz", chanxList.get(0).get("CHANXZBH"));
		}
		param.put("shengcx", shengcx);
		String chanxhJson = CollectionUtil.listToJson(chanxList, "SHENGCXBH", "SHENGCXBH");
		setResult("usercenter",loginUser.getUsercenter());
		setResult("shengcx", chanxListToStringWithSplit(chanxList));
		setResult("chanxhJson",chanxhJson);
		return "select";
	}
	
	/**
	 * 产线集合转成String类型，用于in操作
	 * @author gswang
	 * @param list 产线集合
	 * @return String 产线'L1','L2','L3'
	 */
	public String chanxListToString(List<Map<String,String>> list){
		StringBuffer buf = new StringBuffer();
		String flag="";
		for(Map<String,String> chanxMap : list){
			buf.append(flag).append("'").append(chanxMap.get("SHENGCXBH")).append("'");
			flag=",";
		}
		return buf.toString();
	}
	
	/**
	 * 月模拟计划查询
	 * @author 贺志国
	 * @date 2012-2-10
	 * @param bean
	 * @return AJAX
	 */
	public String queryYuemnjh(@Param Yuemnjh bean){
		Map<String,String> param = this.getParams();
		try {
			if(param.get("chanxh") != null && param.get("chanxh").trim().length()>0){
				param.put("chanxh", param.get("chanxh"));
			}else{
				List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
				param.put("jihybh", loginUser.getUsername()); 
				param.put("chanxhall",CollectionUtil.listToString(chanxlist));
			}
			setResult("result", yuemnjhgzService.select(bean,param));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-8-2
	 * @Time: 下午02:14:40
	 * @version 1.0
	 * @return
	 * @Description:   导出
	 */
	public String downLoad(){
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("kaissj" , this.getParam("kaissj")) ;
		map.put("jiessj" , this.getParam("jiessj")) ;
		map.put("chanxh" , this.getParam("chanxh")) ;
		map.put("lingjbh" , this.getParam("lingjbh")) ;
		map.put("usercenter" ,loginUser.getUsercenter()) ; 
		map.put("chanxhall" ,this.getParam("chanxhall")) ;
		
		
		List<Yuemnjh> ymn = yuemnjhgzService.select(map);
		
		Map<String, Object> dataSource;
		dataSource = new HashMap<String, Object>();
		dataSource.put("ymn", ymn);
		HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
		try{
		downLoadServices.downloadFile("ymngz.ftl", dataSource, response, "周期计划跟踪", ExportConstants.FILE_XLS, false);
		}
		catch(ServiceException e){
			HttpServletRequest request = ActionContext.getActionContext().getRequest();
			request.setAttribute("message",e);
			return "error";
		}  
		
		return "downLoad" ;
	}
	
	/**
	 * 产线集合转成String类型
	 * @author hzg
	 * @param list 产线集合
	 * @return String 产线L1,L2,L3
	 */
	public String chanxListToStringWithSplit(List<Map<String,String>> list){
		StringBuffer buf = new StringBuffer();
		String flag="";
		for(Map<String,String> chanxMap : list){
			buf.append(flag).append(chanxMap.get("SHENGCXBH"));
			flag=",";
		}
		return buf.toString();
	}
	
	
}
