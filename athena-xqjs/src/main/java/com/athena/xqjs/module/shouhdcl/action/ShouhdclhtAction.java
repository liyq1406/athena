package com.athena.xqjs.module.shouhdcl.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.shouhdcl.Jusgzd;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.shouhdcl.service.ShouhdclhtService;
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
public class ShouhdclhtAction extends BaseWtcAction {

	@Inject
	private ShouhdclhtService shouhdclht;
	
	@Inject
	private UserOperLog log;
	private final Log logger = LogFactory.getLog(ShouhdclhtAction.class);
	/**
	 * 收货待处理页面初始化
	 * @return 
	 */
	public String initShouhdclht(){
		if(null!=getParam("usercenter")&&!"".equals(getParam("usercenter"))){
			setResult("usercenter", getParam("usercenter"));
		}else{
			setResult("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());
		}
		return "shouhdclht";
	}
	
	/**
	 * 查询收货待处理'78001HÂ Â P2'
	 * @return 
	 */
	public String queryShouhdclht(@Param Jusgzd jusgzd){
		String loginfo = "";
		String userloginfo = "";
		String response = "";
		try {
			
			//返回结果
			Map<String,Object> result = new HashMap<String, Object>();
			//返回结果
			Map<String,String> param = getParams();
			String currentPage = param.get("pageNo");
			String usercenter = param.get("usercenter");
			param.put("currentPage", currentPage);
			logger.info("Q0309调用");
			//调用WTC查询拒收跟踪单
			WtcResponse wtcResponse = callWtc(usercenter,"Q0309", param);
			logger.info("Q0309调用完成success");
			//获取WTC
			response = CommonFun.strNull(wtcResponse.get("response"));
/*			response = "0000";
			WtcResponse wtcResponse=new WtcResponse(result);
			Map wtcParameter = new HashMap();*/
			if(response.equals(Const.WTC_SUSSCESS)){
				logger.info("Q0309调用正常");
				Map wtcParameter = (Map) wtcResponse.get("parameter");
				logger.info("Q0309调用正常total_page");
				//总页数信息重置
				logger.info("Q0309调用正常:"+CommonFun.strNull(wtcParameter.get("list")));
				if(wtcParameter.get("list")!=null){
					result.put("total", CommonFun.strNull(wtcResponse.get("total_page")));
					result.put("rows", wtcParameter.get("list"));
				}
				setResult("result", result);
			}else{
				logger.info("Q0309调用异常:"+response);
				Message m=new Message(response,"i18n.xqjs.shouhdcl.shouhdcl");
				Message mck=null;
				userloginfo = m.getMessage();
				if(userloginfo!=null && response.equals(userloginfo)){
					mck=new Message(response,"i18n.ckJs_zh_CN");
					userloginfo = mck.getMessage();
				}
				if(userloginfo==null || userloginfo.length() == 0 || response.equals(userloginfo)){
					userloginfo= "收货待处理回退调用WTC异常";
				}
				logger.info("Q0309调用异常userloginfo:"+userloginfo);
				setResult("result", userloginfo);
				throw new RuntimeException(userloginfo);
			}
//			setResult("result",shouhdclht.queryShouhdclht(jusgzd, param));
			log.addCorrect(CommonUtil.MODULE_XQJS, "调用WTC查询拒收跟踪单回退数据", "调用WTC查询拒收跟踪单回退数据完成");
		} catch (Exception e) {
			loginfo = "调用WTC查询拒收跟踪单异常"+CommonUtil.replaceBlank(e.toString()) + response;
			CommonFun.logger.info(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "调用WTC查询拒收跟踪单", "调用WTC查询拒收跟踪单", CommonUtil.getClassMethod(), loginfo);
			throw new ServiceException("".equals(userloginfo)?loginfo:userloginfo);
		}
		return AJAX;
	}
	
	/**
	 * 更新拒收跟踪单
	 * @return
	 */
	public String shouhdclhtUpdate(@Param("list") ArrayList<Jusgzd> jusgzds){
		String loginfo = "";
		String response = "";
		try {
			String usercenter = jusgzds.get(0).getUsercenter();
			Map param = new HashMap();
			param.put("list", jusgzds);
			//调用WTC查询拒收跟踪单
			WtcResponse wtcResponse = callWtc(usercenter,"Q0310", param);
			//获取WTC
			response = CommonFun.strNull(wtcResponse.get("response"));
			if(response.equals(Const.WTC_SUSSCESS)){
				if(jusgzds != null && jusgzds.size()>1){
					Map wtcParameter = (Map) wtcResponse.get("parameter");
					//总页数信息重置
					String susscessNum = CommonFun.strNull(wtcParameter.get("susscessNum"));
					String failNum = CommonFun.strNull(wtcParameter.get("failNum"));
					
					//   0006915   返回未指定要货令个数=  选中数据-成功指定数据
					int su = Integer.valueOf(StringUtils.defaultIfEmpty(susscessNum, "0"));
					int errorNum=jusgzds.size()-su;
					logger.info("Q0310调用:jusgzds.size"+String.valueOf(jusgzds.size())+"	susscessNum:"+susscessNum+"	su:"+String.valueOf(su));
					if(String.valueOf(jusgzds.size()).equals(String.valueOf(su))){
						loginfo = "处理完成！";
					}else{
						loginfo = "已经回退" + susscessNum + "个拒收跟踪单,还有" +  errorNum + "个" + "拒收跟踪单未回退";
					}
				}else{
					loginfo = "处理完成！";
				}
			}else{
				Message m=new Message(response,"i18n.xqjs.shouhdcl.shouhdcl");
				Message mck=null;
				loginfo = m.getMessage();
				if(loginfo!=null && response.equals(loginfo)){
					mck=new Message(response,"i18n.ckJs_zh_CN");
					loginfo = mck.getMessage();
				}
				if(loginfo==null || loginfo.length() == 0 || response.equals(loginfo)){
					loginfo= "收货待处理回退调用WTC异常";
				}
			}
			CommonFun.logger.info(loginfo);
			log.addCorrect(CommonUtil.MODULE_XQJS, "调用WTC批量收货待处理回退", loginfo);
		} catch (Exception e) {
			loginfo = "调用WTC批量收货待处理回退"+CommonUtil.replaceBlank(e.toString()) + response;
			CommonFun.logger.error(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "调用WTC批量收货待处理回退", "调用WTC批量收货待处理回退", CommonUtil.getClassMethod(), loginfo);
		}
		setResult("result", loginfo);
		return AJAX;
	}
	
}
