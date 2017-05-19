package com.athena.ckx.module.cangk.action;
//11976

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Anxyhlbbcssz;
import com.athena.ckx.entity.cangk.Anxzdyhl;
import com.athena.ckx.module.baob.service.ShisywyhlConfigService;
import com.athena.ckx.module.cangk.service.AnxzdyhlService;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class AnxzdyhlAction extends ActionSupport {
	
	@Inject
	private ShisywyhlConfigService yhlService;
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private AnxzdyhlService anxzdyhlService;
	
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	public String execute(){
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	public String query()
	{
		try{
			Map<String,String> params = this.getParams();

			Anxzdyhl bean = new Anxzdyhl();
			bean.setUsercenter(params.get("usercenter"));
			if(null != params.get("lingjbh"))
			{
				bean.setLingjbh(params.get("lingjbh"));
			}
			if(null != params.get("ckxhd"))
			{
				bean.setCkxhd(params.get("ckxhd"));
			}
			bean.setCurrentPage(Integer.parseInt(params.get("pageNo")));
			bean.setPageSize(Integer.parseInt(params.get("pageSize")));
			setResult("result", anxzdyhlService.queryZdyhl(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "按需最大要货量", "数据查询");
		}catch (Exception e){
			Map<String,String> map = new HashMap<String,String>();
			System.out.println("######################"+e.getCause()+e.getMessage());
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "按需最大要货量", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
		
	}
	
	public String save(@Param("insert") ArrayList<Anxzdyhl> insert,@Param("edit") ArrayList<Anxzdyhl> edit , @Param("delete") ArrayList<Anxzdyhl> delete){
		Map<String,String> map = new HashMap<String,String>();		
		try {
			Message m = new Message(anxzdyhlService.save(insert, edit, delete),"i18n.ckx.cangk.i18n_anxzdyhl");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "按需最大要货量", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "按需最大要货量", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	//huxy_V4_041
	public String anxyhltjbbcszs()
	{
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		//List<HashMap<String, String>> factoryList = yhlService.queryFactoryByUsercebter(getLoginUser().getUsercenter());
		//setResult("fac",factoryList);
		return "select";
	}
	//huxy_V4_030
	public String queryAnxtjbbsel(@Param Anxyhlbbcssz bean)
	{
		Map<String, String> map = new HashMap<String,String>();
		map.put("usercenter", getLoginUser().getUsercenter());
		map.put("gongc", this.getParam("gongc"));
		map.put("chanx", this.getParam("chanx"));
		this.setResult("result", anxzdyhlService.queryParam(bean,map));
		return AJAX;
	}
	
	public String queryAnxyhllx()
	{
		setResult("result", anxzdyhlService.queryAnxyhllx());
		return AJAX;
	}
	public String queryFactoryByUsercenter()
	{
		setResult("result", anxzdyhlService.queryFactoryByUsercenter(getLoginUser().getUsercenter()));
		return AJAX;
	}
	
	public String eidtAnxyhlbbSz(@Param Anxyhlbbcssz bean){
		/*
		bean.setGongc(this.getParam("gongc"));
		bean.setChanx(this.getParam("chanx"));
		bean.setUsercenter(this.getParam("usercenter"));
		bean.setJihsl(Integer.parseInt(this.getParam("jihsl")));
		bean.setYaohllx(this.getParam("yaohllx"));*/
		Map<String,String> map = new HashMap<String,String>();
		try{
			Message m = new Message(anxzdyhlService.editSz(bean),"i18n.ckx.cangk.i18n_yhlbb");
			map.put("success", "编辑成功");
			map.put("message", "编辑成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "按需要货令统计报表参数设置", "数据保存");
		}catch(Exception e){
			map.put("success", "false");
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "按需要货令统计报表参数设置", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	
	public String deleteAnxyhlbbSz(){
		Map<String,String> map = new HashMap<String,String>();
		Map<String,String> param = this.getParams();
		Anxyhlbbcssz bean = new Anxyhlbbcssz();
		bean.setChanx(param.get("chanx"));
		bean.setUsercenter(param.get("usercenter"));
		bean.setGongc(param.get("gongc"));
		
		try{
			Message m = new Message(anxzdyhlService.deleteSz(bean),"i18n.ckx.cangk.i18n_yhlbb");
			map.put("success", "删除成功");
			map.put("message", "删除成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "按需要货令统计报表参数设置", "数据保存");
		}catch(Exception e){
			map.put("success", "false");
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "按需要货令统计报表参数设置", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
	//huxy_13164
	public String addAnxyhlbbSz(@Param Anxyhlbbcssz bean){
		
		Map<String,String> map = new HashMap<String,String>();
		try{
			Message m = new Message(anxzdyhlService.insertSz(bean),"i18n.ckx.cangk.i18n_yhlbb");
			map.put("success", "新增成功");
			map.put("message", "新增成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "按需要货令统计报表参数设置", "数据保存");
		}catch(Exception e){
			map.put("success", "false");
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "按需要货令统计报表参数设置", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}
}
