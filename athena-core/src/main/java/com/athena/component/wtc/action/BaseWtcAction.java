/**
 * 
 */
package com.athena.component.wtc.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.entity.PltTrscode;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.wtc.WtcRequest;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.json.JsonWtcManager;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.cache.CacheManager;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;

/**
 * @author Administrator
 * 
 */
public class BaseWtcAction extends ActionSupport {

	protected final String SELECT = "select";

	protected JsonWtcManager wtcManager = new JsonWtcManager();

	@Inject
	private UserOperLog userOperLog;
	@Inject
	private CacheManager cacheManager;
	
	/**
	 * @param transCode
	 * @param bean
	 * @return
	 */
	protected String doInsert(String transCode, Object bean) {
		WtcResponse wtcResponse = doWtcRquest(new WtcRequest(transCode));
		return wtcResponse.toString();
	}

	/**
	 * @param transCode
	 * @param bean
	 * @return
	 */
	protected WtcResponse callWtc(String transCode, Object bean) {
		WtcRequest wtcRequest = new WtcRequest(transCode);
		wtcRequest.put("parameter", bean);
		WtcResponse wtcResponse = doWtcRquest(wtcRequest);
		return wtcResponse;
	}

	/**
	 * WTC服务调用（需带用户中心）
	 * 
	 * @param userCenter　用户中心
	 * @param transCode
	 * @param bean
	 * @return
	 */
	protected WtcResponse callWtc(String userCenter, String transCode, Object bean) {
		WtcRequest wtcRequest = new WtcRequest(transCode);
		wtcRequest.put("parameter", bean);
		// 设定用户中心
		wtcRequest.put("trans_bran_code", userCenter);
		WtcResponse wtcResponse = doWtcRquest(wtcRequest);
		return wtcResponse;
	}
	
	/**
	 * @param transCode
	 * @param bean
	 * @return
	 */
	protected String doDelete(String transCode, Object bean) {
		WtcResponse wtcResponse = doWtcRquest(new WtcRequest(transCode));
		return wtcResponse.toString();
	}

	/**
	 * @param wtcRequest
	 * @return
	 */
	private WtcResponse doWtcRquest(WtcRequest wtcRequest) {
		wtcRequest.put("trscode", wtcRequest.getTransCode());
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		// 用户中心
		if (!wtcRequest.containsKey("trans_bran_code")) {
			wtcRequest.put("trans_bran_code", loginUser.getUsercenter());
		}
		// 操作员
		wtcRequest.put("oper_no", loginUser.getUsername()); // 不能超过9位
		// 仓库编号
		if (wtcRequest.containsKey("parameter")) {
			Map parameter = (Map) wtcRequest.get("parameter");
			// 添加每页显示的行数
			if (!parameter.containsKey("pageSize")) {
				parameter.put("pageSize", 10);
			}
			// 移除对象中存在list[
			for (Iterator it = parameter.keySet().iterator(); it.hasNext();) {
				Object key = (Object) it.next();
				if (key.toString().startsWith("list[")) {
					it.remove();
				}
			}
		}
		//获取平台参数
		PltTrscode pltTrscode = new PltTrscode();
		pltTrscode.setTrstype("0");
		pltTrscode.setUsercenter(wtcRequest.get("trans_bran_code").toString());
		pltTrscode.setTrscode(wtcRequest.getTransCode());
		pltTrscode.setService("PASPLT");
		pltTrscode.setTrsname("默认代理服务");
		//获取对象
		List<PltTrscode> cachelist = (List<PltTrscode>) cacheManager.getCacheInfo("queryPltTrscode").getCacheValue();
		for(PltTrscode trs : cachelist){
			if (pltTrscode.getUsercenter().equals(trs.getUsercenter()) && pltTrscode.getTrscode().equals(trs.getTrscode())){
				pltTrscode.setService(trs.getService());
				pltTrscode.setTrsname(trs.getTrsname());
				pltTrscode.setTrstype(trs.getTrstype());
				break;
			}
		}
		//2013-1-22   ====与手持请求冲突======BEGIN
		//wtcRequest.put("trans_type", pltTrscode.getTrstype());
		//2013-1-22   ==========END
		//调用weblogic服务
		WtcResponse wtcResponse = wtcManager.request(wtcRequest, pltTrscode);
		//写数据库日志
		String sResponse = wtcResponse.get("response").toString();
		if (sResponse.equals("0000") || sResponse.equals("C_1403")) {
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, pltTrscode.getTrsname(), pltTrscode.getTrsname());
		} else {
			userOperLog.addError(CommonUtil.MODULE_CANGK, pltTrscode.getTrsname(), pltTrscode.getTrsname(), pltTrscode.getService(), sResponse);
		}
		
		/*
		 * CacheManager cm = CacheManager.getInstance(); List cachelist =
		 * (List<Trscode>) cm.getCacheInfo("queryTrscode").getCacheValue();
		 */
		/*List cachelist = commonService.selectAllTrscode();
		int i;
		Trscode trscode = new Trscode();
		trscode.setTrancode(wtcRequest.getTransCode());
		if ((i = cachelist.indexOf(trscode)) >= 0) {
			trscode = (Trscode) cachelist.get(i);
			String sResponse = wtcResponse.get("response").toString();
			if (sResponse.equals("0000") || sResponse.equals("C_1403")) {
				userOperLog.addCorrect("CK", trscode.getTrsname(),
						trscode.getTrsname());
			} else {
				userOperLog.addError("CK", trscode.getTrsname(),
						trscode.getTrsname(), trscode.getService(), sResponse);
			}
		}*/
		return wtcResponse;
	}
}
