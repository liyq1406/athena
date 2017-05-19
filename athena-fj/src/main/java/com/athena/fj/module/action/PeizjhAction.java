package com.athena.fj.module.action;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.fj.entity.Peizdmx;
import com.athena.fj.entity.Peizjh;
import com.athena.fj.entity.Wulgz;
import com.athena.fj.entity.YaoCJhMx;
import com.athena.fj.module.common.CollectionUtil;
import com.athena.fj.module.service.PeizjhService;
import com.athena.fj.module.service.ShougpzService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ActionException;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.interceptor.supports.log.Log;
import com.toft.mvc.interceptor.supports.log.LogUtils;

/**
 * <p>
 * Title:配载计划逻辑类
 * </p>
 * <p>
 * Description:定义配载计划逻辑类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2011-12-26
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PeizjhAction extends BaseWtcAction  {
	//依赖注入
	@Inject
	private PeizjhService peizjhService;
	@Inject
	private ShougpzService shougpzService;
//	@Inject
//	private BillNumUtil billUtil;
	@Inject
	private UserOperLog userOperLog;
	
	LoginUser user = AuthorityUtils.getSecurityUser();
	Logger log = Logger.getLogger(getClass());
	
	/**
	 * 执行页面菜单跳转方法
	 * @author 贺志国
	 * @date 2011-12-26
	 * @param bean 配载计划实体集
	 * @param operate 操作类型
	 * @return String 返回跳转
	 */
	@Log(description="accessPeizjh",content="{Toft_SessionKey_UserData.userName}执行了：跳转操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：跳转操作出现异常")
	public String accessPeizjh(){
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划", "配载计划执行了跳转开始");
			List<Map<String,String>> chexList = peizjhService.selectMap("queryChexMap",user.getUsercenter());
			List<Map<String,String>> yunssList = peizjhService.selectMap("queryYunssMap",user.getUsercenter());
			
			String strChex = CollectionUtil.listToJson(chexList, "CHEXBH", "CHEXBH");
			String strYunss = CollectionUtil.listToJson(yunssList, "GCBH", "GCBH");
			setResult("chexJson",strChex);
			setResult("yunssJson",strYunss);
			setResult("usercenter",user.getUsercenter());
			setResult("username",user.getUsername());
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return "select";
	}
	
	/**
	 * 配载计划查询
	 * @author 贺志国
	 * @date 2011-12-26
	 * @param bean 配载计划实体集
	 * @return String 返回ajax
	 */
	public String queryPeizjh(@Param Peizjh bean){ 
		try {
			//String ckJhy = user.getJihyz(); //仓库权限
			Map<String,String> postMap = user.getPostAndRoleMap();
			bean.setJihyzbh(postMap.get("PZJIHY"));
			bean.setUsercenter(user.getUsercenter());
			setResult("result", peizjhService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划查询", "配载计划查询结束");
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "配载计划查询", "配载计划查询出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	/**
	 * 根据配载计划号查询配载单下的要货令列表，不分页
	 * @author 贺志国
	 * @date 2011-12-26
	 * @return String AJAX
	 */
	public String queryPeizjhYaohl(){ 
		try {
			Map<String,String> message = new HashMap<String,String>();
			Map<String,String> param = this.getParams();
			param.put("usercenter", user.getUsercenter());
			List<Map<String,Object>> list  =  peizjhService.select(param);
			if(list.size()==0){
				message.put("message", "配载计划对应的要货令明细表没有数据");
				setResult("result", message);
			}else{
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("total", list.size());
				map.put("rows", list);
				setResult("result", map);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			//throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 执行修改打印配载计划
	 * @author 贺志国
	 * @createDate 2011-12-22
	 * @param bean 配载计划bean
	 * @return String
	 */
	public String updatePeizjh(@Param Peizjh bean, @Param("yaohlList")ArrayList<String> yaohlList){  
		userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划打印", "配载计划更新操作开始");
		bean.setYaohlList(yaohlList);
		bean.setUsercenter(user.getUsercenter());
		Map<String,String> peizdhMap = new HashMap<String,String>();   
		peizdhMap.put("peizdh", bean.getPeizdh());
		peizdhMap.put("usercenter", user.getUsercenter());
		peizdhMap.put("editor", user.getUsername());
		peizdhMap.put("jihzt", Peizjh.STATUE_PRINT2);
		peizjhService.updatejihzt(peizdhMap);
		StringBuffer buffer = new StringBuffer();
		String flag = "";
		for(String yaohl : bean.getYaohlList()){//要货令列表
			buffer.append(flag).append("'").append(yaohl).append("'");
			flag=",";
		}
		String yaohlh = buffer.toString();
		Map<String,String> map = new HashMap<String,String>();
		map.put("peizdh", bean.getPeizdh());  
		map.put("cheph", bean.getChep());
		map.put("yaohlList", yaohlh);
		userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划打印", "配载计划向仓库WTC接口发消息开始");
		//发送验证仓库资源消息
		log.info("调用仓库WTC备货出库交易接口服务开始");
		WtcResponse wtcCheck  = callWtc(Peizjh.WTC_CHECK_STORE, map);
		log.info("调用仓库WTC备货出库交易接口服务结束");
		userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划打印", "配载计划向仓库WTC接口发消息结束");
		/*Map<String, String> result = new HashMap<String,String>();
		result.put("peizdh", "PW0001");
		result.put("cheph", "鄂A1001");
		result.put("success", "true");
		result.put("yaohlList", "20015,20018,20017,20016");
		result.put("bhdList", "W00000071");*/
		Map<String, String> result = null;
		try{
			result = peizjhService.parseCheckWtcResponse(wtcCheck);
		} catch(Exception e){	
			userOperLog.addError(CommonUtil.MODULE_FJ, "配载计划打印", "配载计划仓库交货失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			log.error(e.getMessage());
			setResult("result",new Message("cangkujiaohu_error","i18n.fj.fj_message"));
			return AJAX;
		}
		if (peizjhService.checkStoreResponse(result)) {//接收验证仓库资源成功
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划打印", "配载计划验证仓库资源成功");
			bean.setBhlList(Arrays.asList(result.get("bhdList").split(","))); 
			StringBuffer buf = new StringBuffer();
			flag = "";
			for(String beihd : bean.getBhlList()){
				buf.append(flag).append("'").append(beihd).append("'");
				flag = ",";
			}
			List<Peizdmx> listPeizdmx = peizjhService.getBeihdList(buf.toString());
			if(bean.getYaocmxh()==null||"".equals(bean.getYaocmxh())){//手工增加的配载计划，没有要车明细号
				peizjhService.batchPeizSG(bean,listPeizdmx,user.getUsername());
			}else{//存在配载计划，走配载计划确认流程
				peizjhService.batchUpdate(bean,listPeizdmx,user.getUsername());
			}
			setResult("result",result);
			
		} else { //接收验证仓库资源不足
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划打印", "配载计划仓库资源不足");
			setResult("result",result);
			if(result.get("yaohlList").isEmpty()){
				//将配载计划状态修改为0未打印
				peizdhMap.put("jihzt", Peizjh.STATUE_PRINT0);
				peizjhService.updatejihzt(peizdhMap);
				setResult("noYaohl","0");
				log.info("noYaohl:0");
				return AJAX;
			}
			peizdhMap.put("peizdh", bean.getPeizdh());
			bean.setBhlList(Arrays.asList(result.get("bhdList").split(",")));
			bean.setYaohlList(Arrays.asList(result.get("yaohlList").split(","))); 
			
			//手工配载时，获取没有匹配上的要货令集合
			List<Map<String,Object>> notMatchListSG = this.notBeiHOfYaohlSGList(bean.getYaohlList(),yaohlList);
			setResult("notMatchSG",notMatchListSG);
			log.info("没有匹配的要货令notMatchSG:"+notMatchListSG.toString());
			//自动配载时查询没有匹配上的要货令集合
			List<Map<String,Object>>  notMatchList= peizjhService.queryNotBeiHOfYaohl(bean); 
			setResult("notMatch",notMatchList);
			log.info("没有匹配的要货令notMatch:"+notMatchList.toString());
		} 
		userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划打印", "配载计划打印结束");
		return AJAX;
	}
	
	
	/**
	 * 	继续配载
	 * @return
	 */
	public String surePeiz(@Param Peizjh bean, @Param("noMatchYaohlList")ArrayList<String> noMatchyaohlList
			,@Param("bhdList") String bhdList,@Param("yaohlList") String yaohlList){
		bean.setYaohlList(noMatchyaohlList);
		log.info("要货令BeanYaohlList:"+bean.getYaohlList());
		log.info("要货令yaohlList:"+yaohlList);
		bean.setUsercenter(user.getUsercenter());
//		if(bean.getYaocmxh()==null||"".equals(bean.getYaocmxh())){//手工配载要车明细号为空
//			bean.setYaohlList(Arrays.asList(yaohlList.split(","))); 
//		}

		Map<String,String> peizdhMap =  new HashMap<String,String>();
		peizdhMap.put("peizdh", bean.getPeizdh());
		userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划继续配载", "配载计划打印向仓库WTC接口发消息开始");
		//发消息给仓库确认备货
		log.info("调用仓库WTC备货出库交易接口服务开始");
		WtcResponse wtcSure  = callWtc(Peizjh.WTC_STORE_SURE, peizdhMap); 
		userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划继续配载", "配载计划打印向仓库WTC接口发消息结束");
		Map<String, String> result = null;
		try{
			//返回解析后的结果
			result = peizjhService.sureStoreResponse(wtcSure);
			setResult("result",result);
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_FJ, "配载计划继续配载", "配载计划继续配载仓库交货出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			LogUtils.add(e.getMessage());
			log.error(e.getMessage());
			setResult("result",new Message("cangkujiaohu_error","i18n.fj.fj_message"));
			return AJAX;
		}
		bean.setBhlList(Arrays.asList(bhdList.split(",")));
		LoginUser user = AuthorityUtils.getSecurityUser();
		StringBuffer buf = new StringBuffer();
		String flag = "";
		for(String beihd : bean.getBhlList()){
			buf.append(flag).append("'").append(beihd).append("'");
			flag = ",";
		}
		List<Peizdmx> listPeizdmx = peizjhService.getBeihdList(buf.toString());
		
		//记物流故障
		Wulgz wulgzBean = new Wulgz();
		wulgzBean.setPeizdh(bean.getPeizdh());
		wulgzBean.setYaocmxh(bean.getYaocmxh());
		wulgzBean.setJihcx(bean.getJihcx());
		wulgzBean.setShijcp(bean.getChep());
		wulgzBean.setGuzyy(new Message("noEnoughYaohl_continue","i18n.fj.fj_message").getMessage());
		wulgzBean.setCreator(user.getUsername());
		wulgzBean.setCreateTime(DateUtil.curDateTime());
		wulgzBean.setEditor(user.getUsername());
		wulgzBean.setEditTime(DateUtil.curDateTime());
		if(bean.getYaocmxh()==null||"".equals(bean.getYaocmxh())){//没有配载计划，直接走手工配载流程
			peizjhService.surePeizSG(bean,listPeizdmx,user.getUsername());
		}else{
			//确认配载，记物流故障，插入配载单和配载单明细
			peizjhService.surePeiz(bean,wulgzBean,listPeizdmx,user.getUsername());	
		}
		userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划继续配载", "配载计划继续配载打印结束");
		//打印配载单
		return AJAX;
	}
	
	
	/**
	 * 取消息配载
	 * @return
	 */
	public String cancelPeiz(){
		Map<String,String> peizdhMap =  new HashMap<String,String>();
		peizdhMap.put("peizdh", this.getParam("peizdh"));
		//发消息给仓库删除备货单
		WtcResponse wtcDel  = callWtc(Peizjh.WTC_STORE_DEL, peizdhMap);
		try{
			boolean result = peizjhService.delStoreResponse(wtcDel);  
			peizdhMap.put("usercenter", user.getUsercenter());
			peizdhMap.put("editor", user.getUsername());
			peizdhMap.put("jihzt", Peizjh.STATUE_PRINT0);
			peizjhService.updatejihzt(peizdhMap);
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划取消配载", "配载计划取消配载结束");
			setResult("result",result);
		}catch(Exception e){
			userOperLog.addError(CommonUtil.MODULE_FJ, "配载计划取消配载", "配载计划取消配载出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			LogUtils.add(e.getMessage());	
			log.error(e.getMessage());
			setResult("result",new Message("cangkujiaohu_error","i18n.fj.fj_message"));
			return AJAX;
		}
		return AJAX;
	}
	
	
	/**
	 * 根据备上货的要货令反查没有备上货的要货令集合
	 * @author hzg
	 * @date 2012-3-1
	 * @param yaohlList 匹配上的要货令集合
	 * @return 没有匹配上的要货令集合
	 */
	public List<Map<String,Object>> notBeiHOfYaohlSGList(List<String> beihYaohlList,ArrayList<String> needBeihYaohlList){
		//如果是手工配载的，则用来保存所有选中的要货令
		List <String> selYalhlList=new ArrayList<String>(needBeihYaohlList);
		//仅保留指定子对象集合
		selYalhlList.retainAll(beihYaohlList);
		//yaohlList中去掉两者共同有的数据
		needBeihYaohlList.removeAll(selYalhlList);
		//重新将没有匹配上的要货令放到新的集合中
		List <String> noMatchYhlList=new ArrayList<String>();
		noMatchYhlList.addAll(needBeihYaohlList);
		String yaohls = CollectionUtil.listToString(noMatchYhlList);
		Map<String,String> param  = new HashMap<String,String>();
		param.put("yaohls", yaohls);
		param.put("usercenter", user.getUsercenter());
		//手工配载时查询没有匹配上的要货令集合
		return peizjhService.queryNotBeiHOfYaohlSG(param);
	}
	
	/**
	 * 删除配载计划，并记物流故障
	 * @param bean
	 * @return
	 */
	public String deletePeizjh(@Param Wulgz bean){	
		try {
			if(bean.getYaocmxh()==null||"undefined".equals(bean.getYaocmxh())){
				bean.setYaocmxh("");
			}
			Message message = new Message("del_success","i18n.fj.fj_message");
			Map<String,String> msgMap = new HashMap<String,String>();
			bean.setUsercenter(user.getUsercenter());
			String printStatus = peizjhService.queryPrintStatus(bean.getPeizdh());
			if(printStatus!=null && "2".equals(printStatus)){
				List<Map> yaohlmx = peizjhService.queryPeizjhmx(bean.getPeizdh());
				if(yaohlmx == null || yaohlmx.size()==0){
					setResult("num","0");
				}else{
					msgMap.put("message", "处理中配载单无法删除！");
					setResult("result",msgMap);
					return AJAX;
				}
			}else if(!(printStatus!=null && "0".equals(printStatus))){
				msgMap.put("message", "无法删除配载单，请检查配载单状态！");
				setResult("result",msgMap);
				return AJAX;
			}
			peizjhService.delPeizjh(bean,user.getUsername());
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "删除配载计划", "删除配载计划结束");
			msgMap.put("message", message.getMessage());
			setResult("result",msgMap);
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "删除配载计划", "删除配载计划出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 跳转到增加配载计划页面
	 * @author 贺志国
	 * @date 2012-3-20
	 * @return String
	 */
	public String toPageAddPeizjh(){
		return "pageAddPeizjh";
	}
	
	/**
	 * 根据查询区域条件查询未配载要货令集合，分页查询
	 * @author 贺志国
	 * @date 2012-3-22
	 * @return AJAX
	 */
	public String queryNoPeizYaohl(@Param Peizjh bean){
		Map<String,String> param = this.getParams();
		if(!" ".equals(param.get("yaohl_in"))){
			List<String> yaohlList =  Arrays.asList(param.get("yaohl_in").split(","));
			StringBuffer buffer = new StringBuffer();
			String flag = "";
			for(String yaohl_in : yaohlList){
				buffer.append(flag).append("'").append(yaohl_in).append("'");
				flag=",";
			}
			param.put("yaohlhs", buffer.toString());
		}
		param.put("usercenter", user.getUsercenter());
		if(param.get("keh") == null ){
			param.put("keh", " ");
		}
		try {
			setResult("result",peizjhService.selectNoPeizYaohl(bean, param));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	

	/**
	 * 新增配载计划
	 * @author 贺志国
	 * @date 2012-3-23
	 * @param bean 配载计划实体数据集
	 * @return String AJAX 保存提示
	 */
	public String insertPeizjh(@Param Peizjh bean,@Param("yaohls")ArrayList<String> yaohl_in){
	try {
		String yaohls = CollectionUtil.listToString(yaohl_in);
		bean.setUsercenter(user.getUsercenter());
		Map<String,String> msgMap = new HashMap<String,String>();
		Map<String,String> param =  new HashMap<String,String>();
		param.put("usercenter", user.getUsercenter());
		param.put("creator", user.getUsername());
		param.put("yaohls", yaohls);
		param.put("yunssbm", bean.getYunssbm());
		//根据用户中心和要货令查询最早发运时间、客户和仓库号
		List<Map<String,String>> faysjOfYaohl = peizjhService.queryFaysjOfYaohl(param);
		String cangkbh = faysjOfYaohl.get(0).get("CANGKBH");
		bean.setCangkbh(cangkbh);
		bean.setCreator(user.getUsername());
		bean.setCreateTime(DateUtil.curDateTime());
		peizjhService.savePeizjh(bean, param);
		userOperLog.addCorrect(CommonUtil.MODULE_FJ, "新增配载计划", "新增配载计划结束");
		Message message = new Message("save_success","i18n.fj.fj_message");
		msgMap.put("message", message.getMessage());
		setResult("result",msgMap);
	} catch (Exception e) {
		userOperLog.addError(CommonUtil.MODULE_FJ, "新增配载计划", "新增配载计划出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		throw new ActionException(e.getMessage());
	}
		return AJAX;
	}
	
	/**
	 * 跳转到修改配载计划页面
	 * @author 贺志国
	 * @date 2012-3-21
	 * @return pageEditPeizjh 跳转后页面
	 */
	public String toPageEditPeizjh(@Param Peizjh bean){
		setResult("selRowData",bean);
		return "pageEditPeizjh";
	}
	
	/**
	 * 删除要货令明细，将要货令表中的锁定状态改为0
	 * @author 贺志国
	 * @date 2012-3-27
	 * @param bean 实体参数
	 * @return String AJAX 删除提示
	 */
	public String deleteYaohlmx(@Param Peizjh bean){
		try {
			Map<String,String> msgMap = new HashMap<String,String>();
			bean.setSuodpz(Peizjh.SUODPZ_STATE_0);
			bean.setEditor(user.getUsername());
			bean.setEditTime(DateUtil.curDateTime());
			String printStatus = peizjhService.queryPrintStatus(bean.getPeizdh());
			if(!(printStatus!=null && "0".equals(printStatus))){
				msgMap.put("message", "无法删除要货令，请检查配载单状态");
				setResult("result",msgMap);
				return AJAX;
			}
			peizjhService.deleteYaohlmx(bean);
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划", "删除要货令明细，修改要货令锁定状态为0结束");
			Message message = new Message("del_success","i18n.fj.fj_message");
			msgMap.put("message", message.getMessage());
			setResult("result",msgMap);
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "配载计划", "删除要货令明细，修改要货令锁定状态为0时出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage());
		}
		return AJAX;
	}
	
	/**
	 * 增加要货令明细，将要货令表中的锁定状态改为1
	 * @author 贺志国
	 * @date 2012-3-28
	 * @param bean 实体参数
	 * @return  String AJAX 增加提示
	 */
	public String addYaohlmx(@Param Peizjh bean){
		try {
			Map<String,String> param = this.getParams();
			Map<String,String> msgMap = new HashMap<String,String>();
			param.put("usercenter", user.getUsercenter());
			param.put("creator", user.getUsername());
			bean.setYaohls(param.get("yaohls"));
			bean.setSuodpz(Peizjh.SUODPZ_STATE_1);
			bean.setEditor(user.getUsername());
			bean.setEditTime(DateUtil.curDateTime());
			String printStatus = peizjhService.queryPrintStatus(bean.getPeizdh());
			if(!(printStatus!=null && "0".equals(printStatus))){
				msgMap.put("message", "无法增加要货令，请检查配载单状态");
				setResult("result",msgMap);
				return AJAX;
			}
			peizjhService.addYaohlmx(bean,param);
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划", "增加要货令明细，修改要货令锁定状态为1结束");
			Message message = new Message("add_success","i18n.fj.fj_message");
			msgMap.put("message", message.getMessage());
			setResult("result",msgMap);
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "配载计划", "增加要货令明细，修改要货令锁定状态为1时出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage());
		}	
		return AJAX;
	}
	
	/**
	 * 修改配载计划运输商
	 * @author 贺志国
	 * @date 2012-3-28
	 * @return String AJAX 修改成功提示
	 */
	public String updatePeizjhYunss(){
		try{
			Map<String,String> param = this.getParams();
			Map<String,String> msgMap = new HashMap<String,String>();
			param.put("usercenter", user.getUsercenter());
			peizjhService.updatePeizjhYunss(param);
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划", "修改配载计划运输商结束");
			Message message = new Message("save_success","i18n.fj.fj_message");
			msgMap.put("message", message.getMessage());
			setResult("result",msgMap);
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "配载计划", "修改配载计划运输商出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage());
		}	
		return AJAX;
	}
	
	
	/**
	 * 配载计划下的要货令零件汇总
	 * @author 贺志国
	 * @date 2012-3-29
	 * @return String AJAX
	 */
	public String queryPeizjhYaohlOfLingj(){
		Map<String,String> param = this.getParams();
		try{
			param.put("usercenter", user.getUsercenter());
			List<Map<String,String>> listLj = peizjhService.selectPeizjhYaohlOfLingj(param);
			Map<String,Object> mapObj = new HashMap<String,Object>();
			mapObj.put("total",listLj.size());
			mapObj.put("rows",listLj);
			setResult("result",mapObj);
		} catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
		return AJAX;
	}
	
	/**
	 * 配载计划下的要货令包装汇总
	 * @author 贺志国
	 * @date 2012-3-29
	 * @return String AJAX
	 */
	public String queryPeizjhYaohlOfBaoz(){
		Map<String,String> param = this.getParams();
		try{
			param.put("usercenter", user.getUsercenter());
			List<Map<String,String>> listBz = peizjhService.selectPeizjhYaohlOfBaoz(param);
			Map<String,Object> mapObj = new HashMap<String,Object>();
			mapObj.put("total",listBz.size());
			mapObj.put("rows",listBz);
			setResult("result",mapObj);
		} catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
		return AJAX;
	}

	
	/**
	 * 系统推荐配载策略查询
	 * @author 贺志国
	 * @date 2012-3-30
	 * @return String AJAX
	 */
	public String queryPeizcl(){
		try {
			Map<String, String> params = this.getParams();
			Map<String,String> msgMap = new HashMap<String,String>();
			
			params.put("UC", user.getUsercenter());
			List<Map<String,String>> listClz = shougpzService.selectPeizcl(params);
			if(listClz.size()==0){
				Message message = new Message("没有找到配载策略");
				msgMap.put("message", message.getMessage());
				setResult("result",msgMap);
				return AJAX;
			}
			Map<String,Object> mapObj = new HashMap<String,Object>();
			mapObj.put("total",listClz.size());
			mapObj.put("rows",listClz);
			setResult("result",mapObj);
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	/**
	 * 系统推荐配载
	 * @author 贺志国
	 * @date 2012-3-30
	 * @return String
	 * @description   系统推荐配载
	 */
	public String tuiJanPeiZ(){
		try {
			Map<String, String> params = this.getParams();
			params.put("UC", user.getUsercenter());
			List<YaoCJhMx>  getYaoCjhMxOfFull  =new ArrayList<YaoCJhMx>(); 
	    	getYaoCjhMxOfFull = shougpzService.tuiJYaoCJHmx(params) ;
	    	if(getYaoCjhMxOfFull == null || getYaoCjhMxOfFull.size()==0){
	    		throw new ServiceException("推荐配载的要货令已配载或不满一辆车!");
	    	}
	    	userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划", "系统推荐配载结束");
			setResult("result",getYaoCjhMxOfFull.get(0));
			
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "配载计划", "系统推荐配载出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	
	/**
	 * 根据要货令号查询配载要货令集合
	 * @author 贺志国
	 * @date 2012-3-31
	 */
	public String queryYaohlOfTuij(){
		Map<String,String> param = this.getParams();
		String yaohlhs = CollectionUtil.listToString(Arrays.asList(param.get("yaohlhs").split(",")));
		param.put("yaohlhs", yaohlhs);
		param.put("usercenter", user.getUsercenter());
		try {
			List<Map<String,String>> yaolist = peizjhService.selectYaohlOfTuij(param);
			Map<String,Object> mapObj = new HashMap<String,Object>();
			mapObj.put("total",yaolist.size());
			mapObj.put("rows",yaolist);
			setResult("result",mapObj);
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	/**
	 * 直接打印配载单
	 * @author 贺志国
	 * @date 2012-6-5
	 * @return String AJAX
	 */
	public String printPeizjh(@Param Peizjh bean){
		try {	
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划", "打印配载计划，插入打印数据到打印表开始");
			//获得用户中心
			bean.setUsercenter(user.getUsercenter());
			bean.setZuiwsj(peizjhService.queryZuiwsj(bean.getPeizdh()));
			//根据配载单号查询客户编码
			String kehbm = peizjhService.queryKehbmOfYaohlmx(bean.getPeizdh());
			//根据配载单号查询配载单明细
			List<Peizdmx> pzdmx = peizjhService.selectQueryPeizdmx(bean.getPeizdh());
			//设置客户编码
			bean.setKehbm(kehbm);
			//调用批量插入打印表方法
			peizjhService.batchInsertPrintBusinessTable(bean,pzdmx,user.getUsername());
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "配载计划", "打印配载计划，插入打印数据到打印表结束");
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "配载计划", "打印配载计划，插入打印数据到打印表出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	
	/**
	 * 运输路线下的运输商查询
	 * @author 贺志国
	 * @date 2012-8-3
	 * @param yunslx 运输路线
	 * @return String AJAX
	 */
	public String queryYunssOfLXZ(@Param("yunslx") String yunslx){
		try {
			Map<String,String> param  = new HashMap<String,String>();
			param.put("yunslx", yunslx);
			param.put("usercenter",user.getUsercenter());
			setResult("result",peizjhService.selectYunssOfLXZ(param));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
		
	}
	
	/**
	 * 检查配载计划是否可以删除
	 * @author 王国首
	 * @date 2014-12-05
	 * @param bean 实体参数
	 * @return String AJAX 删除提示
	 */
	public String deleteCheck(@Param Peizjh bean){
		Map<String,String> param = this.getParams();
		String peizdh = param.get("peizdh");
		String zhuangt = peizjhService.queryPrintStatus(peizdh);
		if(zhuangt!= null && "2".equals(zhuangt)){
			List<Map> yaohlmx = peizjhService.queryPeizjhmx(peizdh);
			if(yaohlmx == null || yaohlmx.size()==0){
				setResult("num","0");
			}else{
				throw new ActionException("配载计划处理中，不可以删除"); 
			}
		}else{
			throw new ActionException("配载状态为："+zhuangt+",配载计划不可以删除"); 
		}
		return AJAX;
	}
	
}
