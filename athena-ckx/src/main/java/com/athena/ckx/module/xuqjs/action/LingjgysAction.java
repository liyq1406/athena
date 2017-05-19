package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Lingjgys;
import com.athena.ckx.module.xuqjs.service.LingjgysService;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.Message;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * 零件供应商Action
 * @author denggq
 * 2012-3-29
 */
@Component ( scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LingjgysAction extends ActionSupport{
	
	@Inject
	private LingjgysService lingjgysService;
	
	@Inject
	private UserOperLog userOperLog;
	
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-4-6
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 主页面
	 * @return String
	 * @author denggq
	 * @time 2012-3-29
	 */
	@SuppressWarnings("rawtypes")
	public String execute(){
		Map map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map) ;
		String  params = 
		    "edit_usercenter:   root,ZBCPOA,JIHUAY;"+
		    "edit_lingjbh:   	root,ZBCPOA,JIHUAY;"+
		    "edit_gongysbh:     root,ZBCPOA,JIHUAY;"+
		    "edit_gongyhth:     root,ZBCPOA,JIHUAY,ZHIJIA;"+
		    "edit_gongyfe:      root,ZBCPOA,JIHUAY,ZHIJIA,ZXCPOA;"+
		    "edit_youxq:        root,ZBCPOA,JIHUAY,ZHIJIA,ZXCPOA;"+
		    "edit_fayd:         root,ZBCPOA,JIHUAY,ZHIJIA,ZXCPOA;"+
		    "edit_shengxsj:     root,ZBCPOA,JIHUAY,ZHIJIA,ZXCPOA;"+
		    "edit_shixsj:       root,ZBCPOA,JIHUAY,ZHIJIA,ZXCPOA;"+
		    "edit_zuixqdl:      root,ZBCPOA,JIHUAY,ZHIJIA;"+
		    "edit_cankfz:       root,JIHUAY;"+
		    "edit_zhijcjbl: 	root,ZHIJIA,ZXCPOA;"+
		    "edit_shifyzpch:    root,ZHIJIA,ZXCPOA;"+
		    "edit_shifsxgl:     root,ZHIJIA,ZXCPOA;"+
		    "edit_biaos:    	root,ZBCPOA,JIHUAY;";
	
		for(String s0:params.split(";")){
			String name = s0.split(":")[0].trim();		//字段隐藏属性名
			String roles = s0.split(":")[1].trim();	//所有不隐藏的角色
			if(roles.contains(key)){
				setResult(name, true);
			}else{
				setResult(name, false);
			}
		}
		
		setResult("usercenter", StringUtils.defaultIfEmpty(getParam("usercenter"), getLoginUser().getUsercenter()));
		setResult("lingjbh",getParam("guanjz1"));
		setResult("gongysbh",getParam("guanjz2"));
		return "select";
	}
	
	/**
	 * 分页查询 零件供应商页面
	 * @param lingjgys
	 * @return String
	 * @author denggq
	 * @time 2012-3-29
	 */
	public String query(@Param Lingjgys bean ){
		try{
			setResult("result", lingjgysService.queryLinjgysDHLX(bean,getParam("exportXls")));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件供应商", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件供应商", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 分页查询 零件供应商包装页面
	 * @param lingjgys
	 * @return String
	 * @author denggq
	 * @time 2012-3-29
	 */
	public String queryByBaoz(@Param Lingjgys bean ){
		try{
			//hanwu 20150630 0011518 零件-供应商 包装信息维护不限制状态
			//bean.setBiaos("1");//只查询有效数据
			setResult("result", lingjgysService.queryLinjgysDHLX(bean,getParam("exportXls")));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件供应商", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件供应商", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 保存
	 * @param lingjgys，operant
	 * @return String
	 * @author denggq
	 * @time 2012-3-29
	 */
	public String save(@Param("insert") ArrayList<Lingjgys> insert,@Param("edit") ArrayList<Lingjgys> edit,@Param("delete") ArrayList<Lingjgys> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(lingjgysService.save(insert, edit,delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件供应商", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件供应商", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 多数据查询
	 * @param lingjgys
	 * @return String
	 * @author hj
	 * @time 2012-5-2
	 */
	public String list(@Param Lingjgys bean ){	
		try{
			setResult("result", lingjgysService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件供应商", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件供应商", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 单数据查询
	 * @param bean、
	 * @return String
	 * @author 邓贵琼
	 * @time 2012-5-10
	 */
	public String get(@Param Lingjgys bean ){	
		try{
			setResult("result", lingjgysService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件供应商", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件供应商", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author denggq
	 * @date 2012-5-22
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String download(@Param Lingjgys bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Lingjgys> rows = lingjgysService.list(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingjgys.ftl", dataSurce, response, "零件供应商", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件供应商", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件供应商", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
}
