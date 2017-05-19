package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.module.xuqjs.service.CkxLingjService;
import com.athena.ckx.module.xuqjs.service.TongbjpljService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetPostOnly;
import com.athena.ckx.util.xls.XlsHandlerUtilsgyxhd;
import com.athena.ckx.util.xls.XlsHandlerUtilslingj;
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
 * 零件
 * @author denggq
 * @date 2012-3-22
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxLingjAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private TongbjpljService tongbjpljService;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	
	@Inject
	private CkxLingjService lingjService;
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-3-22
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-3-22
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes" })
	public String execute(){
		Map map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		
		//grid表格默认不显示权限字段
		//setResult("hidden_WULGYY", true);//物流工艺员
		setResult("hidden_JIHUAY", true);//计划员
		//setResult("hidden_WULIUY", true);//物流员
		setResult("hidden_ZHIJIA", true);//质检员
		
		
		String value = (String) map.get(key);
		
		if("ZBCPOA".equals(key)){			//准备层POA
			setResult("hidden_JIHUAY", false);
			//setResult("hidden_WULGYY", false);
		}else if("ZXCPOA".equals(key)){		//执行层POA
			//setResult("hidden_WULIUY", false);
			setResult("hidden_ZHIJIA", false);
		}
		
		setResult(key,value);//各角色组代码（查询和保存数据时使用）
	
		setResult("role", key);//角色（更新保存时用）
		setResult("usercenter", StringUtils.defaultIfEmpty(getParam("usercenter"), getLoginUser().getUsercenter()));
		setResult("lingjbh", getParam("guanjz1"));
		setResult("biaos", StringUtils.defaultIfEmpty(getParam("biaos"), "1"));
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-3-22
	 * @return String
	 */
	public String query(@Param CkxLingj bean){
		try{
			setResult("result", lingjService.selectPages(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-3-22
	 * @return String
	 */
	public String get(@Param CkxLingj bean){
		try{
			setResult("result", lingjService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-3-22
	 * @return String
	 */
	public String list(@Param CkxLingj bean) {
		try{
			setResult("result", lingjService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	/**
	 * 获得多行记录
	 * @param bean
	 * @author hj
	 * @date 2012-5-7
	 * @return String
	 */
	public String listByUserCenter(@Param CkxLingj bean) {
		try{
			if(null == bean.getUsercenter()){
			     bean.setUsercenter(getLoginUser().getUsercenter());
			}
			setResult("result", lingjService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "带用户中心数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "带用户中心数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 保存记录
	 * @param bean
	 * @author denggq
	 * @date 2012-3-22
	 * @return String
	 */
	public String save(@Param CkxLingj bean,@Param("operant") Integer operant,@Param("role") String role) {
		
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(lingjService.save(bean,operant,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "数据保存");
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 失效
	 * @param bean
	 * @author denggq
	 * @date 2012-3-22
	 * @return String
	 */
	public String remove(@Param CkxLingj bean){
		
		bean.setEditor(getLoginUser().getUsername());
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		
		Map<String,String> message = new HashMap<String,String>();
		
		try {
			Message m=new Message(lingjService.doDelete(bean),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "数据失效");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result",message);
		return AJAX;
	}
	
	
	/**
	 * 获得权限组代码,如果角色为空，则默认当前角色 
	 * @param bean
	 * @author denggq
	 * @date 2012-7-14
	 * @return String
	 */
	public String getZudmByAthority(@Param("role") String role,@Param("usercenter") String usercenter){
		try{
			if(role==null||"".equals(role)){
				Map<String,String> map = getLoginUser().getPostAndRoleMap();
				role = GetPostOnly.getPostOnly(map);
			}
			if(usercenter==null||"".equals(usercenter)){
				usercenter=getLoginUser().getUsercenter();
			}
			setResult("result", lingjService.getZudmByAthority(role,usercenter));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "查询权限组代码");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "查询权限组代码", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 获得权限组代码,如果有ZXCPOA的权限获取所有，有质检组无POA获取质检组,无POA无质检组获取所有但不能修改
	 * @param bean
	 * @author chenpeng
	 * @date 2015-3-4
	 * @return String
	 */
	public String getZJYQX(@Param("role") String role,@Param("usercenter") String usercenter){
		try{
			Map<String,String> map = getLoginUser().getPostAndRoleMap();
			if(role==null||"".equals(role)){
				role = GetPostOnly.getPostOnly(map);
			}
			if(usercenter==null||"".equals(usercenter)){
				usercenter=getLoginUser().getUsercenter();
			}
			String zhijy = map.get(role);
			setResult("result", lingjService.getZJYQX(role,usercenter,zhijy));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "查询权限组代码");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "查询权限组代码", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String edit(@Param CkxLingj beans){
			try{
				setResult("result", lingjService.edit(beans));
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "单数据查询");
			}catch (Exception e) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("message", e.getMessage());
				setResult("result", map);
				userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			}
			return AJAX;
	}
	
	/**
	 * 获得权限组代码,如果角色为空，则默认当前角色 
	 * @param bean
	 * @author denggq
	 * @date 2012-7-14
	 * @return String
	 */
	public String queryJihyByLingj(@Param("usercenter") String usercenter){
		try{
			if(usercenter==null||"".equals(usercenter)){
				usercenter=getLoginUser().getUsercenter();
			}
			setResult("result", lingjService.queryJihyByLingj(usercenter));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "查询计划员代码");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "查询计划员代码", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author denggq
	 * @date 2012-8-3
	 * @return String
	 */
	public String download(@Param CkxLingj bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<CkxLingj> rows = lingjService.listImport(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingj.ftl", dataSurce, response, "零件", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	/**
	 * 模板导出
	 * 
	 */
	public String downloadMob() {

		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingjMob.ftl", dataSurce, response, "通用零件模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "通用零件", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "通用零件", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "通用零件", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	
	/**
	 * 导入数据
	 * @author denggq
	 * @Date 2018-4-18
	 * @Param bean
	 * @return String
	 * */
	public String upload(@Param CkxGongyxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			//计划员组的集合
			Map<String, String>  map=lingjService.getJihyMap(getLoginUser().getUsercenter());
			//用户中心+零件 的集合
			Map<String, Object>  mapusercenter=tongbjpljService.getLingjMaps(getLoginUser().getUsercenter());
			// 将创建人,修改人 编辑 为当前登录人
			String mes = XlsHandlerUtilslingj.analyzeXls(mapusercenter,map,request,getLoginUser().getUsername(),baseDao,getLoginUser().getUsercenter());
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "通用零件", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "通用零件", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setRequestAttribute("uploudmessage",message.get("message"));
		return "upload";
	}
	
}
