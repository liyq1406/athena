package com.athena.xqjs.module.dfpvdiaobl.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.xqjs.entity.diaobl.Diaobmx;
import com.athena.xqjs.entity.diaobl.Diaobsq;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.entity.diaobl.Yicsbcz;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.dfpvdiaobl.service.DfpvDiaobshService;
import com.athena.xqjs.module.dfpvdiaobl.service.DfpvDiaobsqOperationService;
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
 * 类描述：DFPV调拨令审核action
 * </p>
 * 创建人：xss
 * <p>
 * 创建时间：2016-1-4
 * </p>
 * 
 * @version 1.0
 * 
 */   
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DfpvDiaoblShAction extends BaseWtcAction {

	@Inject
	private DfpvDiaobshService sh;

	@Inject
	private DfpvDiaobsqOperationService dfpvdiaobsqOperationService;
	
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
		map.put("sh", "70");
		setResult("result", sh.select(diaosq, map));
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
		 Diaobsqmx sqmx = dfpvdiaobsqOperationService.queryDiaobsqmx(dbsqmx);
		 if(sqmx==null){
				result = "调拨申请不存在，请重新查询后再审批！";
				 setResult("result", result);
				 return  AJAX;
		 }
		 
		 if(!sqmx.getZhuangt().equals(Const.DIAOBL_ZT_APPLYING)){
			result = "不能对已审核的调拨申请进行再操作！";
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
			for (int i = 0; i < dbmx.size(); i++) {
				Diaobmx mx = dbmx.get(i);
				mx.setEdit_time(sysDate);
				if(mx.getShengxsj()==null || mx.getShengxsj().compareTo(sysDate.substring(0,10))<0){
	        		setResult("result","生效时间不能为空或小于当前时间！");
	    			return AJAX;
	        	}
				if ( mx.getZickbh() ==null ||"".equals( mx.getZickbh().toString() ) ){
	        		setResult("result","子仓库不能为空！");
	    			return AJAX; 
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
				Diaobsqmx sqmx = dfpvdiaobsqOperationService.queryDiaobsqmx(mx);
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
		Map<String, Object> map = new HashMap<String, Object>();
		List<Diaobmx> all = new ArrayList<Diaobmx>();
		int count = 0;
		int i = 0 ;
		
		for (Diaobsqmx sqmx : dbsqmx) 
		{			
			List<Diaobmx> dbls = (List<Diaobmx>) sh.selectZhongzimx(sqmx);
			
			//如果调拨明细为空的话 - 实批数量为零
			if(null == dbls || dbls.size() == 0){
				try { 
						//如果未发送过，进行终止操作时，将beiz1改为1，不发送终止申请。
						String beiz1 =(String) sh.selectDiaobsqBeiz1( sqmx );
						if( beiz1.equals("30") ){ 
							sh.update_Diaobsqmx_State_1(sqmx);
						}
						sh.updateStoppedStateByLevel2(sqmx,CommonFun.getJavaTime(),getUserInfo().getUsername());	 
				
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				i++;
			}else{
				for (Diaobmx diaobmx : dbls) 
				{
					//将未被终止执行的调拨明细进行终止
					if(StringUtils.isBlank(diaobmx.getZhuangt()) || (!diaobmx.getZhuangt().equalsIgnoreCase(Const.DIAOBL_ZT_EXCUTED) && !diaobmx.getZhuangt().equalsIgnoreCase(Const.DIAOBL_ZT_STOPPED)))
					{
						//如果已经执行，则不能终止
						//如果没有执行或者没有流水，则可以终止。
						
						count =  dfpvdiaobsqOperationService.checkZhixLiuS(diaobmx);
						log.info(diaobmx.getDiaobsqdh()+"发现:"+count+"条调拨流水数据!");
						
						//如果没有调拨流水时终止，去更新调拨申请状态
						if(count <= 0)
						{
							try 
							{
								diaobmx.setZhuangt(Const.DIAOBL_ZT_STOPPED);
								diaobmx.setEditor(getUserInfo().getUsername());
								diaobmx.setEdit_time(com.athena.xqjs.module.common.CommonFun.getJavaTime());
								//diaobsqmx.setDiaobsqdh(diaobmx.getDiaobsqdh());
								
								sh.updateZhongziDiaobmxAndYaohl(diaobmx);//终止调拨明细并更新要货令状态
								sh.updateStoppedStateByLevel2(sqmx,CommonFun.getJavaTime(),getUserInfo().getUsername());
							} 
							catch (Exception e)
							{
								e.printStackTrace();
							}
							i++;
						}
					}
					
					
					//终止时 将实批数量 记录异常消耗 0012554
					Yicsbcz mx2 =  new Yicsbcz();
					//BigDecimal shul = BigDecimal.ZERO;
					
					mx2.setUsercenter(diaobmx.getUsercenter());
					mx2.setLiush( diaobmx.getDiaobdh() );
					mx2.setLingjbh( diaobmx.getLingjbh());
					mx2.setCaozsl( BigDecimal.ZERO.subtract( diaobmx.getShipsl() ) );
					mx2.setShengbd(  diaobmx.getCangkbh()  );
					mx2.setFlag("1");
					mx2.setCaozlx("1");
					
					sh.insert_yicsbcz(mx2);
					
					log.info("插入执行层 调拨的  异常消耗成功!");
					
				}
			}
		}
		if(count>0){//如果有调拨的流水
			String result = "该调拨单已执行无法终止";
			setResult("result", result);
		}else{
			String result = "成功终止了" + i + "个调拨单";
			setResult("result", result);
		}

		
		return AJAX;
	}
	
	/***
	 * 按零件仓库终止
	 * @param dbmx 调拨单明细
	 * @return 终止结果
	 */
	@SuppressWarnings("unchecked")
	public String ljckzz(@Param("edit") ArrayList<Diaobmx> dbmx){ 
		int count = 0;
		int i = 0;
		List<Diaobmx> all = new ArrayList<Diaobmx>();
		if(null != dbmx && dbmx.size() > 0)
		{
			for (Diaobmx diaobmx : dbmx) { 

				//将未被终止执行的调拨明细进行终止
				if(StringUtils.isBlank(diaobmx.getZhuangt()) || (!diaobmx.getZhuangt().equalsIgnoreCase(Const.DIAOBL_ZT_EXCUTED) && !diaobmx.getZhuangt().equalsIgnoreCase(Const.DIAOBL_ZT_STOPPED) ))
				{
					//如果没有执行或者没有流水，则可以终止。 
					count =  dfpvdiaobsqOperationService.checkZhixLiuS(diaobmx);
					
					log.info(diaobmx.getDiaobsqdh()+"发现"+count+"条调拨流水数据!");
					
					if(count <= 0)
					{
						try 
						{
							diaobmx.setZhuangt(Const.DIAOBL_ZT_STOPPED);
							diaobmx.setEditor(getUserInfo().getUsername());
							diaobmx.setEdit_time(com.athena.xqjs.module.common.CommonFun.getJavaTime());
							sh.updateZhongziDiaobmxAndYaohl(diaobmx);//终止调拨明细并更新要货令状态
							sh.updateStoppedStateByLevel2(dbmx,CommonFun.getJavaTime(),getUserInfo().getUsername());//更新调拨申请明细
						} 
						catch(Exception e) 
						{
							e.printStackTrace();
						}
						i++;
					}
					
				}
			}
		}
	
		if(count>0){//已经有调拨流水
			String result = "该调拨单已执行无法终止";
			setResult("result", result);
		}else{
			String result = "成功终止了" + i + "个调拨单";
			setResult("result", result);
		}

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
