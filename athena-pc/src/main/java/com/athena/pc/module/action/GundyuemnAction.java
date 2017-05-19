package com.athena.pc.module.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.pc.common.CollectionUtil;
import com.athena.pc.entity.Yuemnjh;
import com.athena.pc.module.service.GundyuemnService;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ActionException;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.interceptor.supports.log.Log;

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class GundyuemnAction extends ActionSupport {
	//注入service
//	@Inject
//	private YuemnjhgzService yuemnjhgzService;
	
	@Inject
	private GundyuemnService gundyuemnService;
	
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
	@Log(description="accessGundyuemn",content="{Toft_SessionKey_UserData.userName}执行了：跳转操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String accessGundyuemn(){
		Map<String,String> param = this.getParams();
		param.put("USERCENTER",loginUser.getUsercenter());
//		String jihychanxz = loginUser.getJihyz();
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihychanxz = postMap.get("PCJIHY");
		param.put("JIHYZBH", jihychanxz);
		List<Map<String,String>> chanxList = gundyuemnService.selectChanx(param);
		param.put("riq", DateUtil.curDateTime().substring(0, 10));
		String shengcx = "''";
		List<Map<String,String>>  gyzqList  = gundyuemnService.selectGongyzq(param);
		if(chanxList.size()>0){
			shengcx = chanxListToString(chanxList);
			param.put("chanxall", param.get("USERCENTER")+chanxList.get(0).get("CHANXZBH"));
			setResult("chanxall", param.get("chanxall"));
			setResult("chanxz", chanxList.get(0).get("CHANXZBH"));
		}else{
			return "select";
		}
		if(gyzqList.size()>0){
			setResult("gyzq", gyzqList.get(0).get("GONGYZQ"));
		}
		setResult("shengcx", chanxListToStringWithSplit(chanxList)); 
		param.put("shengcx", shengcx);
		Map<String,String> shengcxall = new HashMap<String,String>();
		shengcxall.put("SHENGCXBHNAME",  param.get("chanxall"));
		shengcxall.put("SHENGCXBH", "全部产线");
		chanxList.add(0, shengcxall);
		String chanxhJson = CollectionUtil.listToJson(chanxList, "SHENGCXBHNAME", "SHENGCXBH");
		String gongyzqJson = CollectionUtil.listToJson(gyzqList, "GONGYZQ", "GONGYZQ");		
		setResult("usercenter",loginUser.getUsercenter());
		setResult("chanxhJson",chanxhJson);
		setResult("gongyzqJson",gongyzqJson);
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
	public String queryGundyuemn(@Param Yuemnjh bean){
		Map<String,String> param = this.getParams();
		try {
			param.put("USERCENTER",loginUser.getUsercenter());
			if(param.get("chanxh") != null && param.get("chanxh").length()>0 && !param.get("chanxh").equals(param.get("chanxall"))){
				param.put("paramchanxh", param.get("chanxh"));
			}else{
				List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
				param.put("jihybh", loginUser.getUsername()); 
				param.put("paramchanxhall",CollectionUtil.listToString(chanxlist));
			}
			List<Map<String,String>> gyzqsjfw = gundyuemnService.selectGyzqsjfw(param.get("gongyzq"));
			param.put("kaissj", gyzqsjfw.get(0).get("KAISSJ"));
			param.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
			setResult("result", gundyuemnService.select(bean,param));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
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
	
	/**
	 * 查询报警信息
	 * @param gongyzq 工业周期
	 * @return AJAX
	 */
	public String queryGunMessage(@Param Yuemnjh bean){
		Map<String,String> param = this.getParams();
		List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
		param.put("USERCENTER", loginUser.getUsercenter());
		param.put("biaos", "G");
		if(param.get("chanxh") != null && param.get("chanxh").length()>0 && !param.get("chanxh").equals(param.get("chanxall"))){
			param.put("paramchanxh", param.get("chanxh"));
		}else{
			param.put("jihybh", loginUser.getUsername()); 
			param.put("paramchanxhall",CollectionUtil.listToString(chanxlist));
		}
		List<Map<String,String>> gyzqsjfw = gundyuemnService.selectGyzqsjfw(param.get("gongyzq"));
		param.put("kaissj", gyzqsjfw.get(0).get("KAISSJ"));
		param.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
		Map<String,Object> map = gundyuemnService.selectMessage(bean,param);
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 判断是否需要覆盖周期模拟
	 * @author 王国首
	 * @date 2012-7-13
	 * @return AJAX 返回状态主‘Y’的记录数
	 */
	public String queryShiFFG(){
		Map<String,String> param = this.getParams();
		List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
		param.put("CHANXH",CollectionUtil.listToString(chanxlist));
		param.put("USERCENTER",loginUser.getUsercenter());
		List<Map<String,String>> gyzqsjfw = gundyuemnService.selectGyzqsjfw(param.get("gongyzq"));
		param.put("kaissj", DateUtil.getCurrentDate());
		param.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
		String pcCount = gundyuemnService.queryShiFFG(param);
		setResult("pcCount",Integer.parseInt(pcCount));
		return AJAX;
	}
	
	/**
	 * 使用滚动周期模拟数据覆盖周期模拟数据
	 * @author 王国首
	 * @date 2012-7-13
	 * @return AJAX
	 */
	public String gundCover(){
		Map<String,String> param = this.getParams();
		List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
		param.put("CHANXH",CollectionUtil.listToString(chanxlist));
		param.put("USERCENTER",loginUser.getUsercenter());
		
		Map<String, String> message = new HashMap<String,String>();
		param.put("jihybh", loginUser.getUsername()); 
		param.put("EDIT_TIME", DateUtil.curDateTime());
		int result = 0;
		try{
			List<Map<String,String>> gyzqsjfw = gundyuemnService.selectGyzqsjfw(param.get("gongyzq"));
			param.put("kaissj", DateUtil.getCurrentDate());
			param.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
			result = gundyuemnService.gundCover(param);
		} catch(Exception e){
			Message m =  new Message("youtnup_fail","i18n.pc.pc_message");
			message.put("message", m.getMessage());
			setResult("result",message);
			return AJAX;
		}
		setResult("count",result);
		return AJAX;
	}
}
