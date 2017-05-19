package com.athena.xqjs.module.diaobl.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.xqjs.entity.anxorder.Kucjscsb;
import com.athena.xqjs.entity.diaobl.Diaobmx;
import com.athena.xqjs.entity.diaobl.Diaobsq;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.diaobl.service.DiaobshService;
import com.athena.xqjs.module.diaobl.service.DiaobsqOperationService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：DiaoblShAction
 * <p>
 * 类描述：调拨令审核action
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2011-12-19
 * </p>
 * 
 * @version 1.0
 * 
 */   
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DiaoblShAction extends BaseWtcAction {

	@Inject
	private DiaobshService sh;

	@Inject
	private DiaobsqOperationService diaobsqOperationService;
	
	/*
	 * @Inject private GetWtcServeCode xWtcDy;
	 */

	private final Log log = LogFactory.getLog(getClass()); 
	/**
	 * getUserInfo获取用户信息方法
	 * @return object userInfo
	 */
	public LoginUser getUserInfo() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}  
	
	/**
	 * 调拨令审核页面初始化
	 * @return 定位页面
	 */
	public String init(){
		setResult("usercenter", getUserInfo().getUsercenter());
		return "initSh";
	}

	/**
	 * 调拨申请查询
	 * 
	 * @param diaobd
	 *            查询参数
	 * @return 查询结果
	 * 
	 */
	public String searchDiaobSq(@Param Diaobsq diaosq){
		Map<String, String> map = getParams();
		if (!this.getUserInfo().hasRole(Const.QUANX_POA)) {
		map.put("jihy", getUserInfo().getZuh());
		}
		map.put("sh", "60");
		setResult("result", sh.select_sh(diaosq, map));
		return AJAX;
	}
	
	/**
	 * 跳转到调拨单申请明细页面
	 * @param diaobsqdh 调拨单申请单号
	 * @param zhuangt 调拨单状态()
	 * @param diaobsqsj 调拨申请时间
	 * @return 调拨单申请明细页面
	 */
	public String initDiaobSqMx(@Param("diaobsqdh") String diaobsqdh,@Param("zhuangt") String zhuangt,
			@Param("diaobsqsj") String diaobsqsj){
		String foward = "";
		Diaobsq dbsq = new Diaobsq();
		dbsq.setDiaobsqdh(diaobsqdh);
		setResult("usercenter", getParam("usercenter"));
		setResult("diaobsqdh", diaobsqdh);
		setResult("diaobsqsj", diaobsqsj);
		setResult("removeId", getParam("removeId"));
		//状态小于等于已审核，跳转到编辑页面
		if(Integer.parseInt(zhuangt)-Integer.parseInt(Const.DIAOBL_ZT_PASSED) <= 0){
			foward = "dbsqmx";
		}else {//状态大于已审核跳转到终止页面
			foward = "dbsqzz";
		}
		return foward;
	}
	
	/**
	 * 查询调拨申请明细
	 * @param sqmx 查询条件
	 * @return 查询结果
	 */
	public String searchDiaobSqMx(@Param Diaobsq dbsq,@Param("diaobsqdh") String sqdh){
		dbsq.setDiaobsqdh(sqdh);
		if (!this.getUserInfo().hasRole(Const.QUANX_POA)) {
			// dbsq.setJihy(postService.getQueryPostCode("LINGJIAN"));// 暂时未实现
			dbsq.setJihy(getUserInfo().getZuh());
		}
		setResult("result", sh.sumShipsl(dbsq));
		return AJAX;
	}
	
	/**
	 * 查询调拨单明细
	 * @param diaobsqmx 调拨申请明细
	 * @return 查询结果
	 */
	public String searchDiaobdMx(@Param Diaobsqmx dbsqmx){
		setResult("result", sh.selectDiaobmx(dbsqmx));
		return AJAX;
	}
	
	/**
	 * 查询终止调拨申请明细
	 * @param dbsq 调拨申请信息
	 * @param sqdh 申请单号
	 * @return 查询结果
	 */
	public String searchZzDiaobSqMx(@Param Diaobsq dbsq,@Param("diaobsqdh") String sqdh){
		dbsq.setDiaobsqdh(dbsq.getDiaobsqdh());
		dbsq.setLingjbh(dbsq.getLingjbh());
		dbsq.setZhuangt(dbsq.getZhuangt());
		if (!this.getUserInfo().hasRole(Const.QUANX_POA)) {
			// dbsq.setJihy(postService.getQueryPostCode("LINGJIAN"));// 零件权限
			dbsq.setJihy(getUserInfo().getZuh());
		}
		setResult("result", sh.selectZhongzi(dbsq));
		return AJAX;
	}
	
	/**
	 * 查询终止调拨单明细
	 * @param dbsqmx 调拨申请明细信息
	 * @return 查询结果
	 */
	public String searchZzDiaobdMx(@Param Diaobsqmx dbsqmx) {
		setResult("result", sh.selectZhongzimx(dbsqmx, getParams()));
		return AJAX;
	}
	
	/**
	 * 调拨令审核确认
	 * @param dbsqmx 调拨申请明细
	 * @param diaobd 调拨单信息
	 * @return 确认结果
	 */
	public String diaoblQr(@Param("dbsqmx") Diaobsqmx dbsqmx, @Param("dbmx") ArrayList<Diaobmx> dbmx) {
		String result = "调拨令审核确认失败";
		 Diaobsqmx sqmx = diaobsqOperationService.queryDiaobsqmx(dbsqmx);
		 if(sqmx==null){
				result = "调拨申请不存在，请重新查询后再审批！";
				 setResult("result", result);
				 return  AJAX;
		 } 
		 
		 //v4_018
		 if(sqmx.getZhuangt().equals(Const.DIAOBL_ZT_EFFECT)){
			result = "不能对已生效的调拨申请进行再操作！";
			 setResult("result", result);
			 return  AJAX;
		 }

		if(!sqmx.getShenbsl().equals(dbsqmx.getShenbsl())){
			result = "调拨申请单零件申报数量已发生变化，请重新查询后再审批！";
			setResult("result", result);
			return  AJAX;
		}
			 
		try{
			//LoginUser user = getUserInfo();//用户信息
			//gird更新时间格式不对,进行格式化处理
			//String edit_time = dbsqmx.getEdit_time();
			//dbsqmx.setEdit_time(edit_time.substring(0, edit_time.lastIndexOf(".")));
			//dbsqmx.setEditor("001");//用户ID
			//获取系统时间
			String sysDate = com.athena.xqjs.module.common.CommonFun.getJavaTime();
			String shifdcjf = getParam("shifdcjf");//是否多次交付
			//为调拨明细设置更新事件，更信人
			
			BigDecimal hzsl = BigDecimal.ZERO;

			//xss-v4_018
			for (int i = 0; i < dbmx.size(); i++) {
				Diaobmx mx = dbmx.get(i);
				mx.setEdit_time(sysDate);
				if(mx.getShengxsj()==null || mx.getShengxsj().compareTo(sysDate.substring(0,10))<0){
	        		setResult("result","生效时间不能为空或小于当前时间！");
	    			return AJAX;
	        	}
				
				//xss-v4_018
				if(mx.getShipsl().compareTo( dbsqmx.getShenbsl())>0  ){
	        		setResult("result","实批数量不能大于申报数量！");
	    			return AJAX;
	        	} 
				hzsl = mx.getShipsl().add(hzsl);  
				
				//根据调拨单号、仓库，零件查询  非该仓库的 所有已审核调拨数量 
				mx.setDiaobsqdh(dbsqmx.getDiaobsqdh());//调拨申请单号
				BigDecimal hzsl_dbmx  = sh.sumShipsl_new(mx); 
				
				//汇总选中仓库的数据, 和已审核的其他仓库数量 ，再校验	
				BigDecimal hzsl_new = hzsl.add(hzsl_dbmx);
				
				//v4_018
				//已审核的状态下 需要汇总 所有调拨明细单 进行校验
				 if(sqmx.getZhuangt().equals(Const.DIAOBL_ZT_PASSED)){ 
						if(hzsl_new.compareTo( dbsqmx.getShenbsl())>0  ){
			        		setResult("result","该调拨申请明细已审核过，此零件所有仓库实批数量总和不能大于申报数量！");
			    			return AJAX;
			        	}   
				 }					
				
				mx.setEditor(getUserInfo().getUsername());
				mx.setShifdcjf(shifdcjf);
				mx.setDiaobsqdh(dbsqmx.getDiaobsqdh());//调拨申请单号
				mx.setLux(dbsqmx.getLux());//调拨申请单号
				mx.setCreator(getUserInfo().getUsername());
				mx.setCreate_time(sysDate);
				mx.setShenbsl(dbsqmx.getShenbsl());//申报数量
			}
			dbsqmx.setNewEditor(getUserInfo().getUsername());
			dbsqmx.setZhuangt(Const.DIAOBL_ZT_PASSED);
			dbsqmx.setNewEdit_time(sysDate);
			String infomtion = sh.verify(dbsqmx, dbmx);// 审核确认，获取返回值，传递给页面
			if (infomtion.equals("true")) {
				result = "调拨令审核确认成功";
			}
		}catch (Exception e) {
			 log.info(e.toString());
		}
		setResult("result", result);
		return AJAX;
	}
	
	/**
	 * 调拨令拒绝
	 * @param 调拨申请信息
	 * @return 拒绝结果
	 */
	public String diaoblJj(@Param("edit") ArrayList<Diaobsqmx> dbsqmx){
		String result = "调拨令拒绝失败";
		try {
			String sysTime = com.athena.xqjs.module.common.CommonFun.getJavaTime();
			//数据加工
			for (int i = 0; i < dbsqmx.size(); i++) {
				Diaobsqmx mx = dbsqmx.get(i);
				Diaobsqmx sqmx = diaobsqOperationService.queryDiaobsqmx(mx);
				if (!sqmx.getZhuangt().equals(Const.DIAOBL_ZT_APPLYING)) {
					result = "不能对已审核的调拨申请进行再操作！";
					setResult("result", result);
					return AJAX;
				}

				//String updateTime = mx.getEdit_time();
				//mx.setEdit_time(updateTime.substring(0, updateTime.lastIndexOf(".")));
				mx.setZhuangt(Const.DIAOBL_ZT_REJECTED);
				mx.setNewEditor(getUserInfo().getUsername());
				mx.setNewEdit_time(sysTime);
			}
			String info = sh.refused(dbsqmx);
			if(info.equals("true")){
				
				result = "调拨令拒绝成功";
			}
		} catch (Exception e) {
			 log.info(e.toString());
		}
		setResult("result",result);
		return AJAX;
	}
	
	/**
	 * 生效
	 * @param dbsq 调拨申请信息
	 * @return 生效结果
	 */
	public String diaoblSx(@Param("dbsq") Diaobmx  bean){
		String result = "生效失败";
		String flag = null;
		Diaobsq  sq = new Diaobsq();
		sq.setUsercenter(bean.getUsercenter());
		sq.setDiaobsqdh(bean.getDiaobsqdh());
		try {
			String sysDate = com.athena.xqjs.module.common.CommonFun.getJavaTime();
			bean.setNewEdit_time(sysDate);
			bean.setNewEditor(getUserInfo().getUsername());// 开发前期，默认用户001
			/*
			 * if (!this.getUserInfo().hasRole(Const.QUANX_POA_JIHS)) {
			 * sq.setJihy(postService.getQueryPostCode("ckx_lingj"));//暂时未实现 }
			 */
			if (sh.queryState(sq).getZhuangt().equals(Const.DIAOBL_ZT_EFFECT)) {
				setResult("result", "该调拨申请单已生效.");
				setResult("flag", Const.STRING_THREE);
				setResult("dbmx", bean);
				return AJAX;
			}
		        flag = sh.effect(bean,sq);
			if (flag.equalsIgnoreCase(Const.STRING_ONE)) {
			    	
			    	result = "生效成功,等待其它计划员审批完成后才能打印！";
			} else if (flag.equalsIgnoreCase(Const.STRING_TWO)) {
			    	
			    	result = "请全部审批完成后，进行生效操作！";
			} else if (flag.equalsIgnoreCase(Const.STRING_THREE)) {
			    	result = "生效成功！";
			    }
		} catch (Exception e) {
			 log.info(e.toString());
		}
		setResult("result", result);
		setResult("flag", flag);
		setResult("dbmx",bean);
		return AJAX;
	}
	
	/**
	 * 生效以后打印
	 * @param dbsq 调拨申请信息
	 * @return 
	 */
	public  String  cangkprint(@Param  Diaobsqmx  bean){
		String json = sh.printDiaobmx(bean);
		log.info(json);
		setResult("json", json);
		return  AJAX;
	}
	
	/**
	 * 差异打印
	 * @param dbsq 调拨差异信息
	 * @return 
	 */
	public  String  chayiprint(@Param  Diaobsq  bean){
		String json = sh.printDiaobCymx(bean);
		log.info(json);
		setResult("json", json);
		return  AJAX;
	}
	
	/***
	 * 按零件终止
	 * @param dbsqmx 调拨申请明细
	 * @return 终止结果
	 */

	@SuppressWarnings("unchecked")
	public String ljzz(@Param("edit") ArrayList<Diaobsqmx> dbsqmx){
		/*
		 * try { String sysTime =
		 * com.athena.xqjs.module.common.CommonFun.getJavaTime(); //数据加工 for
		 * (int i = 0; i < dbsqmx.size(); i++) { Diaobsqmx mx = dbsqmx.get(i);
		 * mx.setNewEditor(getUserInfo().getUsername());
		 * mx.setNewEdit_time(sysTime);
		 * 
		 * } String str = sh.lingjStop(dbsqmx); if(str.equals("true")){ result =
		 * "终止成功"; } } catch (Exception e) { log.info(e.toString()); }
		 * setResult("result", result);
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		List<Diaobmx> all = new ArrayList<Diaobmx>();
		for (Diaobsqmx sqmx : dbsqmx) 
		{
			List<Diaobmx> dbls = (List<Diaobmx>) sh.selectZhongzimx(sqmx);
			for (Diaobmx diaobmx : dbls) 
			{
				//将未被终止执行的调拨明细进行终止
				if(StringUtils.isBlank(diaobmx.getZhuangt()) || (!diaobmx.getZhuangt().equalsIgnoreCase(Const.DIAOBL_ZT_EXCUTED) && !diaobmx.getZhuangt().equalsIgnoreCase(Const.DIAOBL_ZT_STOPPED)))
				{
					all.add(diaobmx);
				}
			}
		}
		int max = all.size();
		int count = 0;
		if(max > 0)
		{
			Map<String, List<Diaobmx>> csmap = threeCenterList(all);
			// 分批次调用WTC
			Set<Entry<String, List<Diaobmx>>> setDd = csmap.entrySet();
			for (Entry<String, List<Diaobmx>> entry : setDd) 
			{
				List<Diaobmx> wtcList = entry.getValue();
				if(null != wtcList && wtcList.size() > 0)
				{
					map.put("list", entry.getValue());
					WtcResponse wtcResponse = callWtc(entry.getKey(), "Q0101", map);
					// String response = wtcResponse.get("response").toString();
					if (wtcResponse.get("response").equals(Const.WTC_SUSSCESS))
					{
						Map<String, Object> wtcParameter = (Map<String, Object>) wtcResponse.get("parameter");
						wtcParameter.put("editor", getUserInfo().getUsername());
						count += this.getStopCount(wtcParameter);
					}
				}
			}
			
		}	
		//如果有终止去更新调拨申请状态
		if(count > 0)
		{
			try 
			{
				sh.updateStoppedStateByLevel2(all,CommonFun.getJavaTime(),getUserInfo().getUsername());
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		String result = "成功终止了" + count + "个调拨单.";
		setResult("result", result);
		return AJAX;
	}

	private Map<String, List<Diaobmx>> threeCenterList(List<Diaobmx> all) {
		List<Diaobmx> uwls = new ArrayList<Diaobmx>();
		List<Diaobmx> ulls = new ArrayList<Diaobmx>();
		List<Diaobmx> uxls = new ArrayList<Diaobmx>();
		// 拆分成三个用户中心
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i).getUsercenter().equalsIgnoreCase(Const.WTC_CENTER_UW)) {
				uwls.add(all.get(i));
			} else if (all.get(i).getUsercenter().equalsIgnoreCase(Const.WTC_CENTER_UL)) {
				ulls.add(all.get(i));
			} else {
				uxls.add(all.get(i));
			}
		}

		Map<String, List<Diaobmx>> csmap = new LinkedHashMap<String, List<Diaobmx>>();
		csmap.put(Const.WTC_CENTER_UW, uwls);
		csmap.put(Const.WTC_CENTER_UL, ulls);
		csmap.put(Const.WTC_CENTER_UX, uxls);
		return csmap;
	}
	
	/***
	 * 按零件仓库终止
	 * @param dbmx 调拨单明细
	 * @return 终止结果
	 */
	@SuppressWarnings("unchecked")
	public String ljckzz(@Param("edit") ArrayList<Diaobmx> dbmx){
		/*
		 * try { String sysTime =
		 * com.athena.xqjs.module.common.CommonFun.getJavaTime(); //数据加工 for
		 * (int i = 0; i < dbmx.size(); i++) { Diaobmx mx = dbmx.get(i);
		 * mx.setEditor(getUserInfo().getUsername()); mx.setEdit_time(sysTime);
		 * } String str = sh.cangkStop(dbmx); if(str.equals("true")){ result =
		 * "终止成功"; } } catch (Exception e) { log.info(e.toString()); }
		 */
		int count = 0;
		List<Diaobmx> all = new ArrayList<Diaobmx>();
		if(null != dbmx && dbmx.size() > 0)
		{
			for (Diaobmx diaobmx : dbmx) {
				//将未被终止执行的调拨明细进行终止
				if(StringUtils.isBlank(diaobmx.getZhuangt()) || (!diaobmx.getZhuangt().equalsIgnoreCase(Const.DIAOBL_ZT_EXCUTED) && !diaobmx.getZhuangt().equalsIgnoreCase(Const.DIAOBL_ZT_STOPPED) ))
				{
					all.add(diaobmx);
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, List<Diaobmx>> csmap = threeCenterList(all);
		// 分批次调用WTC
		Set<Entry<String, List<Diaobmx>>> setDd = csmap.entrySet();
		for (Entry<String, List<Diaobmx>> entry : setDd) 
		{
			List<Diaobmx> wtcList = entry.getValue();
			if(null != wtcList && wtcList.size() > 0)
			{
				map.put("list", wtcList);
				WtcResponse wtcResponse = callWtc(entry.getKey(), "Q0101", map);
				if (wtcResponse.get("response").equals(Const.WTC_SUSSCESS)) 
				{
					Map<String, Object> wtcParameter = (Map<String, Object>) wtcResponse.get("parameter");
					wtcParameter.put("editor", getUserInfo().getUsername());
					count += this.getStopCount(wtcParameter);
				}
			}
		}
		if(count > 0)
		{
			try 
			{
				sh.updateStoppedStateByLevel2(dbmx,CommonFun.getJavaTime(),getUserInfo().getUsername());
			} 
			catch(Exception e) 
			{
				e.printStackTrace();
			}
		}
		String result = "成功终止了" + count + "个调拨单.";
		setResult("result", result);
		return AJAX;
	}

	/**
	 * <p>
	 * 解析WTC调用返回的结果
	 * </p>
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getStopCount(Map<String, Object> map) {
		int count = 0;
		List<Map<String, Object>> ls = (List<Map<String, Object>>) map.get("list");
		for (Map<String, Object> map2 : ls) {
			Object shul = map2.get("shul");
			if (!shul.equals(-1)) 
			{
				//吴易超进行修改,对已终止的调拨明细进行状态修改
				log.info("diaobdh:"+(String) map2.get("diaobdh") + "    diaobsqdh:"+(String) map2.get("diaobsqdh"));
				Diaobmx diaobmx = new Diaobmx();
				diaobmx.setZhuangt(Const.DIAOBL_ZT_STOPPED);
				diaobmx.setCangkbh((String) map2.get("cangkbh"));
				diaobmx.setUsercenter((String) map2.get("usercenter"));
				diaobmx.setDiaobdh((String) map2.get("diaobdh"));
				diaobmx.setLingjbh((String) map2.get("lingjbh"));
				diaobmx.setLux((String) map2.get("lux"));
				String dbqd = (String) map2.get("diaobdh");
				diaobmx.setDiaobsqdh(dbqd.substring(0,8));
				diaobmx.setEditor((String) map.get("editor"));
				diaobmx.setEdit_time(com.athena.xqjs.module.common.CommonFun.getJavaTime());
				sh.updateZhongziDiaobmxAndYaohl(diaobmx);
				
				//终止时 将实批数量 记录异常消耗 0012554
					Kucjscsb mx2 = new Kucjscsb();
					BigDecimal shul_t = BigDecimal.ZERO;
					
					BigDecimal shul_t2 = new BigDecimal ( map2.get("shul").toString() ) ;
					
					mx2.setUsercenter(  diaobmx.getUsercenter() );
					mx2.setLingjbh( diaobmx.getLingjbh() ); 
					
					mx2.setYicxhl( shul_t.subtract(shul_t2) );
					mx2.setXiaohd( diaobmx.getCangkbh() );
					mx2.setCreator( diaobmx.getEditor() );
					sh.insert_kucjscsb(mx2);
					
					log.info("插入准备层 调拨的  异常消耗成功!"); 
				
				count++;
			}
		}
		return count;
	}

	/**
	 * @see 调拨扫描审核  
	 * @return
	 */
	public String dodiaobsm()
	{
		String result = null;
		try
		{
			sh.dodiaobsm();
			result = "扫描审核成功!";
		}
		catch (Exception e) 
		{
			result = "扫描审核失败!";
			log.info(e.toString());
		}
		finally
		{
			setResult("result", result);
		}
		return AJAX;
	}
}
