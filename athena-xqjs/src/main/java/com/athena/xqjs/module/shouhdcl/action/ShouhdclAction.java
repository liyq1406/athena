package com.athena.xqjs.module.shouhdcl.action;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.util.AuthorityUtils;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.shouhdcl.Jusgzd;
import com.athena.xqjs.entity.shouhdcl.minJusgzd;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.shouhdcl.service.ShouhdclService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ShouhdclAction
 * <p>
 * 类描述：收货待处理action
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-04-05
 * </p>
 * 
 * @version 1.0
 * 
 */  
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ShouhdclAction extends BaseWtcAction {

	@Inject
	private ShouhdclService shouhdcl;
	
	@Inject
	private UserOperLog log;
	
	/**
	 * 收货待处理页面初始化
	 * @return 
	 */
	public String initShouhdcl(){
		if(null!=getParam("usercenter")&&!"".equals(getParam("usercenter"))){
			setResult("usercenter", getParam("usercenter"));
		}else{
			setResult("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());
		}
		setResult("lingjbh", getParam("lingjbh"));//零件编号
		setResult("gongysdm", getParam("gongysdm"));//供应商代码
		setResult("jihydm", getParam("jihydm"));//计划员
		setResult("gonghms", getParam("gonghms"));//供货模式
		setResult("cangkbh", getParam("cangkbh"));//仓库编号
		setResult("zhuangt1", getParam("zhuangt1"));//状态
		return "shouhdcl";
	}
	
	/**
	 * 查询收货待处理
	 * @return 
	 */
	public String queryShouhdcl(@Param Jusgzd jusgzd){
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
			//如果状态1没选择,则视为-1(应刘栋要求)
			if(CommonFun.strNull(param.get("zhuangt1")).equals("")){
				param.put("zhuangt1", "-1");
			}
			//调用WTC查询拒收跟踪单
			WtcResponse wtcResponse = callWtc(usercenter,"Q0301", param);
			//获取WTC
			response = CommonFun.strNull(wtcResponse.get("response"));
			if(response.equals(Const.WTC_SUSSCESS)){
				Map wtcParameter = (Map) wtcResponse.get("parameter");
				//总页数信息重置
				result.put("total", CommonFun.strNull(wtcResponse.get("total_page")));
				result.put("rows", wtcParameter.get("list"));
				setResult("result", result);
			}
		} catch (Exception e) {
			loginfo = "调用WTC查询拒收跟踪单异常"+CommonUtil.replaceBlank(e.toString()) + response;
			CommonFun.logger.info(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "调用WTC查询拒收跟踪单", "调用WTC查询拒收跟踪单", CommonUtil.getClassMethod(), loginfo);
		}
		return AJAX;
	}
	
	/**
	 * 更新拒收跟踪单
	 * @return
	 */
	public String updateJusgzd(){
		String loginfo = "";//日志信息
		String response = "零件{0},供应商{1},仓库{2},供货模式{3},未找到对应的物流路径,无法得到外部模式";//返回结果
		String gonghms = "";//供货模式
		try {
			//返回结果
			Map<String,String> param = getParams();
			//由于承运商存在空格无法识别 修改空格格式
			String gongys =param.get("gongysdm").replace(" ", " ");
			param.remove("gongysdm");
			param.put("gongysdm", gongys);
			String usercenter = param.get("usercenter");//用户中心
			Wullj wul = shouhdcl.quertWullj(param);//查询物流路径
			//如果物流路径不为空,更新相关信息
			if(wul != null){
				gonghms = wul.getWaibms();
				param.put("xiehd", wul.getXiehztbh());//卸货站台编号
				param.put("gonghms", gonghms);//外部模式
				//调用WTC查询拒收跟踪单
				WtcResponse wtcResponse = callWtc(usercenter,"Q0308", param);
				//获取WTC
				response = CommonFun.strNull(wtcResponse.get("response"));
			}
			//WTC返回成功
			if(response.equals(Const.WTC_SUSSCESS)){
				setResult("gonghms", gonghms);
				setResult("result", "success");
			}else{
				setResult("result", MessageFormat.format(response, param.get("lingjbh"), param.get("gongysdm"), param.get("cangkbh"), param.get("gonghms")));
			}
		} catch (Exception e) {
			loginfo = "调用WTC更新拒收跟踪单异常"+CommonUtil.replaceBlank(e.toString())+response;
			CommonFun.logger.error(loginfo);
			setResult("result", loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "调用WTC更新拒收跟踪单", "调用WTC更新拒收跟踪单", CommonUtil.getClassMethod(), loginfo);
		}
		return AJAX;
	}
	
	/**
	 * 收货待处理审核列表页面初始化
	 * @return
	 */
	public String initShouhdclshlb(){
		SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
	 	setResult("lingjbh", getParam("lingjbh"));// 零件编号
		setResult("danw", getParam("danw"));//单位
		setResult("usercenter", getParam("usercenter"));//用户中心
		setResult("gongysdm", (getParam("gongysdm")).replace(" ", " "));//供应商代码
		setResult("gongysmc", getParam("gongysmc"));//供应商名称
		setResult("gonghms", getParam("gonghms"));//供货模式
		setResult("yansy", getParam("yansy"));//计划员
		setResult("cangkbh", getParam("cangkbh"));//仓库编号
		setResult("zhuangt", getParam("zhuangt"));//状态
		setResult("fahzq",yyyyMM.format(new Date())); 
		//上个页面查询条件保留
		setResult("usercenter1", getParam("usercenter1"));//用户中心
		setResult("lingjbh1", getParam("lingjbh1"));//零件编号
		setResult("gongysdm1", (getParam("gongysdm1")).replace(" ", " "));//供应商代码
		setResult("jihydm1", getParam("jihydm1"));//计划员
		setResult("gonghms1", getParam("gonghms1"));//供货模式
		setResult("cangkbh1", getParam("cangkbh1"));//仓库编号
		setResult("zhuangt1", getParam("zhuangt1"));//状态
		String param=getParam("param"); //xh 141216 10600 收货待处理拒绝无法操作
		return param;
	}

	/**
	 * 查询收货待处理审核列表
	 * @return 
	 */
	public String queryShouhdclshlb(@Param Jusgzd jusgzd){
		String loginfo = "";
		String response = "";
		try {
			//返回结果
			Map<String,Object> result = new HashMap<String, Object>();
			//返回结果
			Map<String,String> param = getParams();
			//由于承运商存在空格无法识别 修改空格格式
			String gongys =param.get("gongysdm").replace(" ", " ");
			param.remove("gongysdm");
			param.put("gongysdm", gongys);
			String currentPage = param.get("pageNo");
			String usercenter = param.get("usercenter");
			param.put("currentPage", currentPage);
			//调用WTC查询拒收跟踪单
			WtcResponse wtcResponse = callWtc(usercenter,"Q0302", param);
			//获取WTC
			response = CommonFun.strNull(wtcResponse.get("response"));
			if(response.equals(Const.WTC_SUSSCESS)){
				Map wtcParameter = (Map) wtcResponse.get("parameter");
				//总页数信息重置
				result.put("total", CommonFun.strNull(wtcResponse.get("total_page")));
				result.put("rows", wtcParameter.get("list"));
				setResult("result", result);
			}
		} catch (Exception e) {
			loginfo = "调用WTC查询拒收跟踪单列表异常"+CommonUtil.replaceBlank(e.toString())+response;
			CommonFun.logger.error(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "调用WTC查询拒收跟踪单列表", "调用WTC查询拒收跟踪单列表", CommonUtil.getClassMethod(), loginfo);
		}
		return AJAX;
	}
	
	/**
	 * 初始化指定要货令
	 * @return 指定要货令初始化页面
	 */
	public String initZhidyhl(){
		setResult("lingjbh", getParam("lingjbh"));//零件编号
		setResult("usercenter", getParam("usercenter"));//用户中心
		setResult("jusgzdh", getParam("jusgzdh"));//拒收跟踪单号
		setResult("xiehd", getParam("xiehd"));//零件编号
		setResult("zhuangt",getParam("zhuangt"));
		setResult("jusljs",getParam("jusljs"));
		return "zhidyhl";
	}
	
	/**
	 * 审核
	 * @param edit 拒收跟踪单列表
	 * @return 审核结果
	 */
	public String shenH(@Param("list") ArrayList<Jusgzd> jusgzds){
		String loginfo = "";
		//获取WTC
		String response = "";
		try {
			String usercenter = jusgzds.get(0).getUsercenter();
			Map param = new HashMap();
			param.put("list", jusgzds);
			//调用WTC拒绝拒收跟踪单
			WtcResponse wtcResponse = callWtc(usercenter,"Q0306", param);
			Map wtcParameter = (Map) wtcResponse.get("parameter");
			response = CommonFun.strNull(wtcResponse.get("response"));
			if(response.equals(Const.WTC_SUSSCESS)){
				List list = (List) wtcParameter.get("list");
				if(list.size() == 0){
					loginfo = "拒绝拒收跟踪单成功";
				}else{
					loginfo = list.size()+"条拒绝拒收跟踪单失败,已处理的将不能再处理 ";
				}            
			}else{
				loginfo = "调用WTC拒绝拒收跟踪单异常" + response;
			}
			CommonFun.logger.info(loginfo);
			log.addCorrect(CommonUtil.MODULE_XQJS, "调用WTC拒绝拒收跟踪单", loginfo);
		} catch (Exception e) {
			loginfo = "调用WTC拒绝拒收跟踪单异常"+CommonUtil.replaceBlank(e.toString()) + response;
			CommonFun.logger.error(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "调用WTC拒绝拒收跟踪单", "调用WTC拒绝拒收跟踪单", CommonUtil.getClassMethod(), loginfo);
		}
		setResult("result", loginfo);
		return AJAX; 
	}
	
	/**
	 * 查询要货令
	 * @param yaohl 要货令
	 * @return 要货令列表
	 */
	public String queryYaohl(@Param Yaohl yaohl){
		String loginfo = "";
		String response = "";
		try {
			//返回结果
			Map<String,Object> result = new HashMap<String, Object>();
			//返回结果
			Map<String,String> param = getParams();
			String usercenter = param.get("usercenter");
			String currentPage = param.get("pageNo");
			param.put("currentPage", currentPage);
			//调用WTC查询拒收跟踪单
			WtcResponse wtcResponse = callWtc(usercenter,"Q0303", param);
			//获取WTC
			response = CommonFun.strNull(wtcResponse.get("response"));
			if(response.equals(Const.WTC_SUSSCESS)){
				Map wtcParameter = (Map) wtcResponse.get("parameter");
				//总页数信息重置
				result.put("total", CommonFun.strNull(wtcResponse.get("total_page")));
				result.put("rows", wtcParameter.get("list"));
				setResult("result", result);
			}
		} catch (Exception e) {
			loginfo = "调用WTC拒绝拒收跟踪单异常"+CommonUtil.replaceBlank(e.toString())+response;
			CommonFun.logger.error(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "调用WTC拒绝拒收跟踪单", "调用WTC拒绝拒收跟踪单", CommonUtil.getClassMethod(), loginfo);
		}
		return AJAX; 
	}
	
	/**
	 * 批量指定要货令
	 * @param jusgzds 拒收跟踪单列表
	 * @return
	 */
	public String piLzdyhl(@Param("list") ArrayList<minJusgzd> jusgzds,@Param("gongysdm") String gongysdm,@Param("chuljg") String chuljg,@Param("blh") String blh,@Param("daohsj") String daohsj,@Param("cangkbh") String cangkbh){
		String loginfo = "";
		String response = "";
		try {
			String usercenter = jusgzds.get(0).getUsercenter();
			Map param = new HashMap();
			param.put("gongysdm", gongysdm.replace(" ", " "));
			param.put("chuljg", chuljg);
			param.put("blh", blh);
			param.put("daohsj", daohsj);
			param.put("cangkbh", cangkbh);
			param.put("list", jusgzds);
			//调用WTC查询拒收跟踪单
			WtcResponse wtcResponse = callWtc(usercenter,"Q0305", param);
			//获取WTC
			response = CommonFun.strNull(wtcResponse.get("response"));
			if(response.equals(Const.WTC_SUSSCESS)){
				Map wtcParameter = (Map) wtcResponse.get("parameter");
				//总页数信息重置
				String susscessNum = CommonFun.strNull(wtcParameter.get("susscessNum"));
				String failNum = CommonFun.strNull(wtcParameter.get("failNum"));
				
				
				//   0006915   返回未指定要货令个数=  选中数据-成功指定数据
				int errorNum=jusgzds.size()-Integer.valueOf(StringUtils.defaultIfEmpty(susscessNum, "0"));
				loginfo = "已经指定" + susscessNum + "个要货令,还有" +  errorNum + "个" + "要货令未指定";
			}else{
				loginfo = "批量指定要货令调用WTC异常"+response;
			}
			CommonFun.logger.info(loginfo);
			log.addCorrect(CommonUtil.MODULE_XQJS, "调用WTC批量指定要货令", loginfo);
		} catch (Exception e) {
			loginfo = "调用WTC批量指定要货令"+CommonUtil.replaceBlank(e.toString()) + response;
			CommonFun.logger.error(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "调用WTC批量指定要货令", "调用WTC批量指定要货令", CommonUtil.getClassMethod(), loginfo);
		}
		setResult("result", loginfo);
		return AJAX;
	}
	
	/**
	 * 校验要货令是否被指定
	 * @param yaohl 要货令信息
	 * @return 校验结果
	 */
	public String checkYhl(@Param Yaohl yaohl){
		setResult("result", shouhdcl.checkYhl(yaohl));
		return AJAX;
	}
	
	/**
	 * 指定要货令
	 * @return
	 */
	public String zhiDyhl(@Param Jusgzd jusgzd){
		String loginfo = "";
		String response = "";
		try {
			//调用WTC查询查询要货令
			WtcResponse wtcResponse = callWtc(getParam("usercenter"),"Q0304", getParams());
			response = (String) wtcResponse.get("response");
			if(response.equals(Const.WTC_SUSSCESS)){
				loginfo = "指定要货令成功";
			}else{
				loginfo = "调用WTC指定要货令异常"+response;
			}
			CommonFun.logger.info(loginfo);
			log.addCorrect(CommonUtil.MODULE_XQJS, "指定要货令成功", loginfo);
		} catch (Exception e) {
			loginfo = "调用WTC指定要货令异常"+CommonUtil.replaceBlank(e.toString())+response;
			CommonFun.logger.error(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "调用WTC指定要货令异常", "调用WTC指定要货令异常", CommonUtil.getClassMethod(), loginfo);
		}
		setResult("result", loginfo);
		/*setResult("lingjbh", getParam("lingjbh"));
		setResult("usercenter", getParam("usercenter"));
		setResult("fahzq", CommonFun.yyyyMM.format(new Date()));//用户中心
*/		return AJAX;
	}
	
	/**
	 * 创建临时订单
	 * @param dingd 订单
	 * @return
	 */
	public String saveLinsdd(@Param("dingd") Dingd dingd,@Param("edit") ArrayList<Jusgzd> jusgzds){
		String loginfo = "";
		String response = "";
		try {
			dingd.setActive("1");
			loginfo = shouhdcl.saveLinsdd(getParam("jiaofrq"),dingd, jusgzds, AuthorityUtils.getSecurityUser().getUsername());
			if(loginfo.equals("false")){
				loginfo = "创建临时订单失败,订单号已经存在";
				setErrorMessage(loginfo);
			}else if(loginfo.equals("2")){
				loginfo = "创建临时订单失败,物流路径为空!";
				setErrorMessage(loginfo);
			}else{
				Map param = getParams();
				param.put("dingdh",loginfo);
				param.put("list", jusgzds);
				//调用WTC
				WtcResponse wtcResponse = callWtc(dingd.getUsercenter(),"Q0307", param);
				response = (String) wtcResponse.get("response");
				if(response.equals(Const.WTC_SUSSCESS)){
					loginfo = "创建临时订单成功";
				}else if( response.equals("Q0307_02") ){//xss_0013082 
					//调用失败后删除之前生成的订单和明细_xss_0013083
					shouhdcl.deleteLinsddmx(loginfo) ;
					
					loginfo = "指定订单号，拒收跟踪单中无此数据";
					setErrorMessage(loginfo);
				}else if( response.equals("Q0307_01") ){//xss_0013083 
					//调用失败后删除之前生成的订单和明细_xss_0013083
					shouhdcl.deleteLinsddmx(loginfo) ;
					
					loginfo = "指定订单号SQL语句错误";
					setErrorMessage(loginfo);
				}
				/* xss-0013623- 网络问题导致 Tuxedo 没有返回成功
				else{ 
					//调用失败后删除之前生成的订单和明细 _xss_0013083
					shouhdcl.deleteLinsddmx(loginfo) ;
					
					loginfo = "创建临时订单调用WTC异常"+response;
					setErrorMessage(loginfo);
				}
				*/
				CommonFun.logger.info(loginfo);
				log.addCorrect(CommonUtil.MODULE_XQJS, "创建临时订单成功", loginfo);
			}
		} catch (Exception e) {
			loginfo = "创建临时订单异常"+CommonUtil.replaceBlank(e.getMessage())+response;
			setErrorMessage(loginfo);
			CommonFun.logger.error(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "创建临时订单异常", "创建临时订单异常", CommonUtil.getClassMethod(), loginfo);
		}
		setResult("result", loginfo);
		return AJAX;
	}
	
	/**
	 * 校验拒收跟踪单和消耗点的仓库编号 0011223
	 * @param dingd 订单
	 * @return
	 */
	public String checkLinsdd(@Param("dingd") Dingd dingd,@Param("edit") ArrayList<Jusgzd> jusgzds){
		String loginfo = "";
		String response = "";
		try {
			loginfo = shouhdcl.checkLinsdd(getParam("jiaofrq"),dingd, jusgzds, AuthorityUtils.getSecurityUser().getUsername());
		} catch (Exception e) {
			loginfo = "创建临时订单异常"+CommonUtil.replaceBlank(e.getMessage())+response;
			setErrorMessage(loginfo);
			CommonFun.logger.error(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "创建临时订单异常", "创建临时订单异常", CommonUtil.getClassMethod(), loginfo);
		}
		setResult("result", loginfo);
		return AJAX;
	}
}
