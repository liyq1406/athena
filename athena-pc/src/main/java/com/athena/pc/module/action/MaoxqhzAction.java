package com.athena.pc.module.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.pc.common.CollectionUtil;
import com.athena.pc.entity.Rigdpcmx;
import com.athena.pc.module.service.MaoxqhzService;
import com.athena.pc.module.service.RigdService;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.interceptor.supports.log.Log;
/**
 * <p>
 * Title:日滚动排产处理类
 * </p>
 * <p>
 * Description:定义日滚动排产处理方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-3-7
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class MaoxqhzAction extends ActionSupport {

	@Inject
	private RigdService rigdService;
	
//	@Inject
//	private DailyRollService dailyRollService; 
	
	/**
	 * 获取用户信息
	 */	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	
	@Inject
	private MaoxqhzService maoxqhzService;
	
	/**
	 * 页面跳转
	 * @author gswang
	 * @date 2012-03-07
	 * @return String 跳转后的页面
	 */
	@Log(description="accessRigd",content="{Toft_SessionKey_UserData.userName}执行了：跳转操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String accessMaoxqhz(){
		Map<String,String> param = this.getParams();
		param.put("USERCENTER",loginUser.getUsercenter());
		param.put("jihybh", loginUser.getUsername()); 
		param.put("riq", DateUtil.curDateTime().substring(0, 10));
//		String jihychanxz = loginUser.getJihyz();
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihychanxz = postMap.get("PCJIHY");
		param.put("JIHYZBH", jihychanxz);
		List<Map<String,String>> chanxList = rigdService.selectChanx(param);
		List<Map<String,String>> gongyzqList = maoxqhzService.selectGongyzqs(param);
		String gongyzqYear = chanxListToStringWithSplit(gongyzqList,"GONGYZQ");
		String shengcx = "''";
		if(chanxList.size()>0){
			shengcx = chanxListToString(chanxList);
			setResult("chanxz", chanxList.get(0).get("CHANXZBH"));
		}
		param.put("shengcx", shengcx);
		String chanxhJson = CollectionUtil.listToJson(chanxList, "SHENGCXBH", "SHENGCXBH");
		setResult("gongyzqyear",gongyzqYear);
		setResult("shengcx", chanxListToStringWithSplit(chanxList,"SHENGCXBH"));
		setResult("chanxhJson",chanxhJson);
		return "select";
	}
	
	/**
	 * 日滚动排产明细查询
	 * @author gswang
	 * @date 2012-04-12
	 * @param bean 日滚动排产计划明细实体类
	 * @return String AJAX
	 */
	@Log(description="queryRigd",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：查询操作出现异常")
	public String queryMaoxqhz(@Param Rigdpcmx bean){
		Map<String,String> param = this.getParams();
		Map<String,String> msgMap = new HashMap<String,String>();
		if(param.get("chanxz") == null){
			msgMap.put("message", "计划员所对应的产线组，生产线有误，请检查");
			setResult("result",msgMap);
			return AJAX;
		}
		String gongyzq = param.get("gongyzq");
		List<Map<String,String>> gyzqsjfw = maoxqhzService.selectGyzqsjfw(gongyzq);
		if(gyzqsjfw.size()<1){
			msgMap.put("message", "工业周期不能为空，请设置工业周期参考系");
			setResult("result",msgMap);
			return AJAX;
		}
		param.put("kaissj", gyzqsjfw.get(0).get("KAISSJ"));
		param.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
		List<String> chanxlist = Arrays.asList(param.get("shengcx").split(","));
		param.put("jihybh", loginUser.getUsername()); 
		param.put("chanxhall",CollectionUtil.listToString(chanxlist));
		param.put("USERCENTER",loginUser.getUsercenter());
		setResult("result",maoxqhzService.select(bean,param));
		return AJAX;
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
	 * 产线集合转成String类型
	 * @author gswang
	 * @param list 产线集合
	 * @return String 产线L1,L2,L3
	 */
	public String chanxListToStringWithSplit(List<Map<String,String>> list,String name){
		StringBuffer buf = new StringBuffer();
		String flag="";
		for(Map<String,String> chanxMap : list){
			buf.append(flag).append(chanxMap.get(name));
			flag=",";
		}
		return buf.toString();
	}
}
