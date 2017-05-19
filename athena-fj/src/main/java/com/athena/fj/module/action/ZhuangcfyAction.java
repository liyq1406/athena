package com.athena.fj.module.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.fj.entity.Zhuangcfy;

import com.athena.fj.module.common.CollectionUtil;
import com.athena.fj.module.service.ZhuangcfyService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ActionException;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;

/**
 * <p>
 * Title:装车发运逻辑类
 * </p>
 * <p>
 * Description:定义装车发运逻辑类
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
 * @date 2012-1-17
 */

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ZhuangcfyAction extends ActionSupport {
	
	
	@Inject
	private ZhuangcfyService zhuangcfyService;
	@Inject
	private UserOperLog userOperLog;
	LoginUser user = AuthorityUtils.getSecurityUser();
	/**
	 * 获取用户信息
	 * @return 用户信息
	 */
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}
	
	/**
	 * 进入装车页面
	 * @return
	 */
	public String accessZhuangcfy(){
		return "select";
	}
	
	/**
	 * 70101
	 * 查询UC信息（根据获取的参数-UC卡号和配载单号）
	 * @param 用户中心，UC卡号，运输路线
	 * @return AJAX
	 */
	public String queryUAInfo(){
		try {
			Map<String,String> params = getParams();
			params.put("usercenter", user.getUsercenter());
			List<Map<String,String>> uaList  = zhuangcfyService.queryUCInfo(params);
			List<String> kehList = zhuangcfyService.getKehOfLxz(params);
			setResult("result", uaList);	
			setResult("kehList",kehList);
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 70102
	 * 查询车牌（根据获取的参数-配载单号）
	 * @return AJAX
	 */
	public String queryChep(){
		Map<String,String> params = this.getParams();
		params.put("usercenter", user.getUsercenter());
		List<Map<String,String>> peizdList = zhuangcfyService.queryPeizdChep(params);
		setResult("result",peizdList);
		return AJAX;
	}
	
	/**
	 * 70103
	 * 查询UC信息（根据获取的参数-UC卡号）
	 * @return AJAX
	 */
	public String queryUAList(){
		Map<String,String> msgMap = new HashMap<String,String>();
		try {
			Map<String,String> uaListMap = new HashMap<String,String>();
			Map<String,String> paramMap = this.getParams();
			paramMap.put("usercenter", user.getUsercenter());
			Zhuangcfy zhuangcfyBean = new Zhuangcfy();
			
			String uch = paramMap.get("uch").toString();
			String []uaArray = uch.split(",");
			StringBuffer strBuf = new StringBuffer();
			String flag = "";
			for(String str : uaArray){//将UC卡号组合成in接收的形式
				strBuf.append(flag).append("'").append(str).append("'");
				flag = ",";
			}
			uaListMap.put("uch", strBuf.toString());
			uaListMap.put("usercenter", user.getUsercenter());
			//根据UCH查询UC记录
			//List<Zhuangcmy> uaList  = zhuangcfyService.queryUCList(uaListMap);
			//查询UC卸货点分组
			List<String> xiehdList = zhuangcfyService.getXiehdOfUCH(uaListMap);
			if(xiehdList.size()==0){
				msgMap.put("message", "卸货点为空");
				setResult("errorMsg",msgMap);
				return AJAX;
			}
			//根据UCH查询YAOHLH集合
			List<String> yaohlhList = zhuangcfyService.getYaohlhOfUCH(uaListMap);
			String yaohlhs = CollectionUtil.listToString(yaohlhList);
			zhuangcfyBean.setYaohlhs(yaohlhs);
			//UC卡号下的供应商，取其一  mantis bug:0006307
			//String gongysbm = zhuangcfyService.queryUCGongysdm(uaArray[0].toString());
			//获得配载单对应的仓库编号
			String cangkbh = zhuangcfyService.queryCangkbhOfPeizd(paramMap);
			
			zhuangcfyBean.setUsercenter(user.getUsercenter());
			zhuangcfyBean.setPeizdh(paramMap.get("peizdh").toString());
			zhuangcfyBean.setYunssbm(paramMap.get("yunssbm").toString());
			zhuangcfyBean.setGongsmc(paramMap.get("gongsmc").toString());
			zhuangcfyBean.setJihcx(paramMap.get("jihcx").toString());
			zhuangcfyBean.setChep(paramMap.get("chep").toString());
			//zhuangcfyBean.setGongysbm(gongysbm);
			zhuangcfyBean.setCreator(user.getUsername());
			zhuangcfyBean.setCreateTime(DateUtil.curDateTime());
			zhuangcfyBean.setEditor(user.getUsername());
			zhuangcfyBean.setEditTime(DateUtil.curDateTime());
			zhuangcfyBean.setCangkbh(cangkbh);
			
			//插入装车发运单并打印
			zhuangcfyService.insertZhuangc(uaListMap,zhuangcfyBean,xiehdList,user.getUsername());
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "打印装车发运单", "打印装车发运单结束");
			msgMap.put("message", "打印成功");
			setResult("result", msgMap);
		} catch (Exception e) {
			msgMap.put("message", "打印出错"+e.getMessage());//若出现异常则抛出错误消息
			setResult("result",msgMap);
			userOperLog.addError(CommonUtil.MODULE_FJ, "装车发运", "装车发运查询UC信息出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			//throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	/**
	 * 客户路线组查询
	 * @author 贺志国
	 * @date 2012-7-9
	 * @param 用户中心
	 * @return List<Map<String,String>>客户路线组集合
	 */
	public Map<String,List<String>> GetKehLxz(Map<String,String> params){
		try{
			params.put("usercenter", user.getUsercenter());
			List<Map<String,String>>  list = zhuangcfyService.selectKehLxz(params);
			return zhuangcfyService.wrapKehLxz(list);
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
	}
	

}
