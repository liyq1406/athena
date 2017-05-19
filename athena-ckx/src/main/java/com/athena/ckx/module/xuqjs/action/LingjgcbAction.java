package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Lingjck;
import com.athena.ckx.entity.xuqjs.Lingjgys;
import com.athena.ckx.module.xuqjs.service.LingjgcbService;
import com.athena.ckx.util.GetPostOnly;
import com.athena.ckx.util.xls.XlsHandlerUtilslingjck;
import com.athena.ckx.util.xls.XlsHandlerUtilslingjgysb;
import com.athena.component.service.Message;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * @description 零件-供应商|仓库-包装
 * @author denggq
 * @date 2012-4-16
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LingjgcbAction extends ActionSupport {

	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	@Inject
	private LingjgcbService lingjgcbService;//零件供应商
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-4-16
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * @description
	 * @param bean
	 * @return denggq
	 * @date 2012-4-16
	 */
	@SuppressWarnings("rawtypes")
	public String execute() {
		//字段权限
		Map map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		
		String  params = 
			  	"edit_usercenter:     root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_lingjbh:     root,ZBCPOA,WULGYY,ZXCPOA;"+
				"edit_gongyswy:     root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_ucbzlx:     root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_ucrl_gys:   root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_uabzlx:     root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_uaucgs:     root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_uarl:     root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_gaib:       root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_neic:       root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_shifczlsbz: root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_usbzlx:     root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_usbzrl:     root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_uclx:       root,ZBCPOA,WULGYY,ZXCPOA;"+
			    "edit_ucrl_ck:    root,ZBCPOA,WULGYY,ZXCPOA;";
		
		for(String s0:params.split(";")){
			String name = s0.split(":")[0].trim();		//字段隐藏属性名
			String roles = s0.split(":")[1].trim();	//所有不隐藏的角色
			if(roles.contains(key)){
				setResult(name, true);
			}else{
				setResult(name, false);
			}
		}
		
		String  param = 
		  	"edit_usercenter:     root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_lingjbh:     root,ZBCPOA,WULGYY,ZXCPOA;"+
			"edit_gongyswy:     root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_usbzlx:     root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_usbzrl:   root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_uclx:     root,ZBCPOA,WULGYY,ZXCPOA;"+
		    "edit_ucrl_ck:     root,ZBCPOA,WULGYY,ZXCPOA;";
	
	for(String s1:param.split(";")){
		String names = s1.split(":")[0].trim();		//字段隐藏属性名
		String role = s1.split(":")[1].trim();	//所有不隐藏的角色
		if(role.contains(key)){
			setResult(names, true);
		}else{
			setResult(names, false);
		}
	}
		
		String  parames = 
	  	"edit_usercenter:     root,ZBCPOA,WULGYY,ZXCPOA;"+
	    "edit_lingjbh:     root,ZBCPOA,WULGYY,ZXCPOA;"+
		"edit_gongyswy:     root,ZBCPOA,WULGYY,ZXCPOA;"+
	    "edit_xuh:     root,ZBCPOA,WULGYY,ZXCPOA;"+
	    "edit_shengxsj:   root,ZBCPOA,WULGYY,ZXCPOA;"+
	    "edit_shixsj:     root,ZBCPOA,WULGYY,ZXCPOA;"+
	    "edit_ucbzlx:     root,ZBCPOA,WULGYY,ZXCPOA;"+
	    "edit_ucrl:     root,ZBCPOA,WULGYY,ZXCPOA;"+
	    "edit_uabzlx:     root,ZBCPOA,WULGYY,ZXCPOA;"+
	    "edit_uaucgs:     root,ZBCPOA,WULGYY,ZXCPOA;"+
	    "edit_gaib:     root,ZBCPOA,WULGYY,ZXCPOA;"+
	    "edit_neic:     root,ZBCPOA,WULGYY,ZXCPOA;";

	for(String s2:parames.split(";")){
		String namese = s2.split(":")[0].trim();		//字段隐藏属性名
		String rolese = s2.split(":")[1].trim();	//所有不隐藏的角色
		if(rolese.contains(key)){
			setResult(namese, true);
		}else{
			setResult(namese, false);
		}
	}
		setResult("lingjbh", StringUtils.defaultIfEmpty(this.getParam("guanjz1"),this.getParam("lingjbh")));
		setResult("usercenter", StringUtils.defaultIfEmpty(getParam("usercenter"), getLoginUser().getUsercenter()));
		return "select";
	}
	
	
	
	/**
	 * @description 设置零件-供应商的包装
	 * @param bean
	 * @return denggq
	 * @date 2012-4-16
	 */
	public String saveLingjgb(@Param("edits") ArrayList<Lingjgys> edit){
		Map<String,String> message = new HashMap<String,String>();
		try {
			lingjgcbService.updateLingjgysBaoz(edit, getLoginUser().getUsername());//修改零件供应商的包装
			message.put("message", "保存成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件-供应商-包装", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件-供应商-包装", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	
	
	/**
	 * @description 设置零件-仓库的包装
	 * @param bean
	 * @return denggq
	 * @date 2012-4-16
	 */
	public String saveLingjcb(@Param("edit") ArrayList<Lingjck> edit){
		Map<String,String> message = new HashMap<String,String>();
		try {
			
			lingjgcbService.updateLingjckBaoz(edit,getLoginUser().getUsername());//修改零件仓库的包装
			message.put("message", "保存成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件-仓库-包装", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件-仓库-包装", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	
	public String copyLingjgcb(@Param Lingjgys bean){
		Map<String,String> message = new HashMap<String,String>();
		try{//同一零件设置的包装可一样
			Message m = new Message(lingjgcbService.copy(bean, getLoginUser().getUsername()), "i18n.ckx.cangk.i18n_fahzt");
			message.put("message",m.getMessage() );
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件-供应商|仓库-包装", "数据复制");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件-供应商|仓库-包装", "数据复制", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 零件供应商-包装导出模板
	 * @param bean
	 * @author wangyu
	 * @date 2014-11-15
	 * @return String
	 */
	public String downloadlingjgcbMob(@Param Lingjgys bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingjgcbMob.ftl", dataSurce, response, "零件-供应商-包装-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件-供应商-包装", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件-供应商-包装", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件-供应商-包装", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 零件供应商-包装导入数据
	 * @author wangyu
	 * @Date 2014-11-18
	 * @Param bean
	 * @return String
	 * */
	public String uploadlingjgysb(@Param Lingjck bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			//查询零件的集合
			Map<String, String>  maplingj = lingjgcbService.getLingjsMap();
			//查询供应商/承运商/运输商的集合
			Map<String, String>  mapgcy = lingjgcbService.getGongcysMap();
			//查询包装型号的集合
			Map<String, String>  mapbaoz = lingjgcbService.getBaozsMap();
			//查询零件供应商的集合
			Map<String, String>  maplingjgys = lingjgcbService.getlingjgysMaps();
			//0007177  将修改人编辑为当前登录人
			String mes = XlsHandlerUtilslingjgysb.analyzeXls(maplingj,mapgcy,mapbaoz,maplingjgys,request,getLoginUser().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件仓库", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件仓库", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setRequestAttribute("uploudmessage",message.get("message"));
		return "upload";
	}
}
