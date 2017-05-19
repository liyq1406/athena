package com.athena.pc.module.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.pc.common.CollectionUtil;
import com.athena.pc.entity.LineTimeUpdate;
import com.athena.pc.entity.Qickc;
import com.athena.pc.module.service.SgEquilibriaSCXService;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ActionException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
/**
 * <p>
 * Title:产线工时调整处理类
 * </p>
 * <p>
 * Description:定义产线工时调整处理方法
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
 * @date 2012-6-20
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class SgEquilibriaSCXAction extends ActionSupport  {
	/**
	 * 获取用户信息
	 */	
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	@Inject
	private SgEquilibriaSCXService sgEquilibriaSCXService;
	
	/**
	 * 跳转到工时调整页面
	 * @author 贺志国
	 * @date 2012-6-20
	 * @return String selectGstz 跳转名称
	 */
	public String toPageGongstz(){
		setResult("gongyzq",this.getParam("gongyzq"));
		setResult("biaos",this.getParam("biaos"));
		setResult("chanxzbh",this.getChanxzbh());
		return "selectGstz";
	}
	
	/**
	 * 查询工业周期内的产线所在周的工时
	 * @author 贺志国
	 * @date 2012-6-20
	 * @param bean 实体集
	 * @return String AJAX 查询结果集
	 */
	public String queryGongstz(@Param LineTimeUpdate bean){
		Map<String,String> param = this.getParams(); 
		param.put("usercenter",loginUser.getUsercenter());
		//查询计划员所在用户中心下的所有产线
		List<Map<String,String>> chanxList = this.getChanxList();
		param.put("oppobj", CollectionUtil.chanxListToString(chanxList,"SHENGCXBH"));
		
		//根据工业周期查询该工业周期的开始和结束时间
		List<Map<String,String>> gyzqsjfw = sgEquilibriaSCXService.selectGyzqsjfw(param.get("gongyzq"));
		param.put("kaissj", gyzqsjfw.get(0).get("KAISSJ"));
		param.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
		param.put("biaos", param.get("biaos")); //Y--周期模拟，G--滚动周期模拟
		
		List<Map<String,LineTimeUpdate>> cxgsList = sgEquilibriaSCXService.selectChanxGongs(param);
		//检查该产线所在周是否有排产
		checkPaicmxMaxGS(cxgsList,param.get("biaos"));
		Map<String,Object> cxgsMap = new HashMap<String,Object>();
		cxgsMap.put("total", cxgsList.size());
		cxgsMap.put("rows", cxgsList);
		setResult("result", cxgsMap);
		return AJAX;
	}
	
	
	/**
	 * 查询工作日历所在周是否排产，如果排产取出所在周的最大工时
	 * @author 贺志国
	 * @date 2012-8-1
	 * @param cxgsList 产线工时集合
	 */
	public void checkPaicmxMaxGS(List<Map<String,LineTimeUpdate>> cxgsList,String biaos){
		for(int i=0;i<cxgsList.size();i++){
			LineTimeUpdate lineTime = (LineTimeUpdate) cxgsList.get(i);
			BigDecimal paicGS = lineTime.getWorkTime();
			Map<String,String> params = new HashMap<String,String>();
			String kaissj = lineTime.getStartTime();
			String jiessj = DateUtil.dateAddDays(kaissj, Integer.parseInt(lineTime.getXingq())-1);
			params.put("kaissj", kaissj);
			params.put("jiessj", jiessj);
			params.put("usercenter", loginUser.getUsercenter());
			params.put("chanxh", lineTime.getLineNum());
			params.put("biaos", biaos); 
			
			if(paicGS==null){
				//查询排产明细表
				List<Map<String,String>> paicmx = this.queryPaicmx(params);
				if(paicmx.size()>0){
					lineTime.setWorkTime(new BigDecimal(paicmx.get(0).get("WORKTIME")));
				}
			}
		}
	}
	/**
	 * 查询排产明细表所在周的最大工时
	 * @author 贺志国
	 * @date 2012-8-1
	 * @param params
	 * @return
	 */
	public List<Map<String,String>> queryPaicmx(Map<String,String> params){
		return sgEquilibriaSCXService.selectPaicmx(params);
	}
	
	
	/**
	 * 产线工时调整
	 * @author 贺志国
	 * @date 2012-6-20
	 * @Param("edit") List<LineTimeUpdate> editList
	 * @return String AJAX 调整后的结果
	 */
	public String changxGongstz(@Param("gongyzq") String gongyzq,@Param("biaos") String biaos,@Param("edit")ArrayList<LineTimeUpdate> gstzList){
		try {
			Map<String,String> params = this.wrapParams(gongyzq, biaos);
			//手动均衡开始
			String isTrue = sgEquilibriaSCXService.stickOfequilibria(params,gstzList);
			if("success".equals(isTrue)){
				setResult("success",isTrue);
			}else{
				setResult("success",isTrue);
			}
			
		} catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
		return AJAX;
	}
	
	
	/**
	 * 封装基本参数
	 * @author 贺志国
	 * @date 2012-7-6
	 * @param gongyzq 工业周期
	 * @param biaos 标识
	 * @return Map<String,String> 基本参数
	 */
	public Map<String,String> wrapParams(String gongyzq,String biaos){
		Map<String,String> params = new HashMap<String,String>(); //包含biaos类型，Y=月模拟，G=滚动周期模拟，从页面传过来
		params.put("biaos", biaos);
		//查询计划员所在用户中心下的所有产线
		List<Map<String,String>> chanxList = this.getChanxList();
		params.put("chanxh", CollectionUtil.chanxListToString(chanxList,"SHENGCXBH"));
		//根据工业周期查询该工业周期的开始和结束时间
		List<Map<String,String>> gyzqsjfw = sgEquilibriaSCXService.selectGyzqsjfw(gongyzq);
		params.put("kaissj", gyzqsjfw.get(0).get("KAISSJ"));
		params.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
		params.put("usercenter", loginUser.getUsercenter());
		//根据'用户中心'、'产线号'、'时间'范围查询出月度模拟计划明细表B中的工作编号和计划号
		List<Map<String,String>> gongzbhBList = sgEquilibriaSCXService.selectYuedmnjhmxOfGongzbh(params);
		String gongzbh = CollectionUtil.chanxListToString(gongzbhBList, "GONGZBH");
		params.put("gongzbh", gongzbh); //工作编号
		List<Map<String,String>> jihhBList =  sgEquilibriaSCXService.selectYuedmnjhmxOfJihh(params);
		String jihh = CollectionUtil.chanxListToString(jihhBList, "YUEMNJHH");
		params.put("yuemnjhh", jihh); //计划号
		//根据计划员查询产线-零件表获得计划员所管零件
		List<Map<String,String>> lingjbhList = sgEquilibriaSCXService.selectCKX_SHENGCX_LINGJOFLingjbh(params);
		String lingjbh = CollectionUtil.chanxListToString(lingjbhList, "LINGJBH");
		params.put("lingjbh", lingjbh); //零件编号
		params.put("chanxzbh", this.getChanxzbh());
		return params;
	}
	
	
	/**
	 * 查询计划员所在用户中心下的所有产线
	 * @author 贺志国
	 * @date 2012-6-21
	 * @param param
	 * @return 产线集合
	 */
	public List<Map<String,String>> getChanxList(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("USERCENTER",loginUser.getUsercenter());
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihyzbh = postMap.get("PCJIHY");
		param.put("JIHYZBH", jihyzbh); 
		//查询计划员所在用户中心下的所有产线
//		List<Map<String,String>> chanxList = sgEquilibriaSCXService.selectChanx(param);
		return sgEquilibriaSCXService.selectChanx(param);
	}
	
	/**
	 * 查询计划员所在用户中心下的产线组编号
	 * @author 贺志国
	 * @date 2012-6-25
	 * @return String 产线组编号
	 */
	public String getChanxzbh(){
		Map<String,String> param = new HashMap<String,String>();
		param.put("USERCENTER",loginUser.getUsercenter());
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihyzbh = postMap.get("PCJIHY");
		param.put("JIHYZBH", jihyzbh); 
		return sgEquilibriaSCXService.selectChanxzbh(param);
	}
	
	/**
	 * 跳转到手工调整后的期初库存查询页面
	 * @author 贺志国
	 * @date 2012-7-1
	 * @return String 
	 */
	public String toPageSGQickc(){
		setResult("gongyzq",this.getParam("gongyzq"));
		setResult("biaos",this.getParam("biaos"));
		return "selectGSqckc";
	}
	
	
	/**
	 * 根据权限查询计划员所以产线组下的产线期初存查询
	 * @author 贺志国
	 * @date 2012-6-18
	 * @param bean 实体类
	 * @return String AJAX 结果集
	 */
	public String querySGQickcAll(@Param Qickc bean){
		//查询计划员所在用户中心下的所有产线
//		String jihyzbh =loginUser.getJihyz();
		Map<String,String> postMap = loginUser.getPostAndRoleMap();
		String jihyzbh = postMap.get("PCJIHY");
		Map<String,String> param = this.getParams();
		//根据工业周期查询该工业周期的开始和结束时间
		List<Map<String,String>> gyzqsjfw = sgEquilibriaSCXService.selectGyzqsjfw(param.get("gongyzq"));
		param.put("kaissj", gyzqsjfw.get(0).get("KAISSJ"));
		param.put("jiessj", gyzqsjfw.get(0).get("JIESSJ"));
		param.put("jihyzbh", jihyzbh);
		param.put("usercenter", loginUser.getUsercenter());
		Map<String,Object> qickcMap = sgEquilibriaSCXService.selectSGQickcAll(bean,param);
		setResult("result",qickcMap);
		return AJAX;
	}
	
	
	/**
	 * 调整确认
	 * @author 贺志国
	 * @date 2012-7-6
	 * @return String 
	 */
	public String confirmTiaoz(){
		Map<String,String> params = this.getParams();
		params = this.wrapParams(params.get("gongyzq"), params.get("biaos"));
		sgEquilibriaSCXService.sureUpdate(params);
		return AJAX;
	}
	
	
	
}
