package com.athena.ckx.module.cangk.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.module.cangk.service.CangkService;
import com.athena.ckx.util.DateTimeUtil;
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
 * 仓库
 * @author denggq
 * @date 2012-1-16
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CangkAction extends ActionSupport{
	

	/**
	 * 用户级操作日志
	 * @author denggq
	 * @date 2012-5-9
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 注入CangkService
	 * @author denggq
	 * @date 2012-1-16
	 * @return bean
	 */
	@Inject
	private CangkService cangkService;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	/**
	 * 获取当前用户信息
	 * @author denggq
	 * @date 2012-2-6
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes"})
	public String execute(){
		//grid表格不显示权限字段
		setResult("hidden_WULGYY", true);
		
		Map map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		
		String key = GetPostOnly.getPostOnly(map);
		String value = (String) map.get(key);
		
		//仓库
		if("ZBCPOA".equals(key) || "root".equals(key)){ //准备层POA仓库暴露权限字段
			setResult("hidden_WULGYY", false);
		}else if("WULGYY".equals(key)){//物流工艺员组
			setResult("WULGYY",value);
		}
		if("ZXCPOA".equals(key) || "root".equals(key)){ //准备层POA仓库暴露权限字段
			setResult("hidden_chukms", false);
		}else if("ZXCPOA".equals(key)){
			setResult("hidden_chukms", true);
		}
		//各角色子仓库需维护字段设置
		String  params = 
		    "edit_zickbh:   	root,ZXCPOA,ZBCPOA,WULGYY;"+
		    "edit_baohd:   		root,ZXCPOA;"+
		    "edit_shifelgl:     root,ZXCPOA;"+
		    "edit_guanlyzbh: 	root,ZXCPOA;"+
		    "edit_zhantbh:      root,ZXCPOA,ZBCPOA,WULGYY;"+
		    "edit_biaos:        root,ZXCPOA,ZBCPOA,WULGYY;";
	
		for(String s0:params.split(";")){
			String name = s0.split(":")[0].trim();		//字段隐藏属性名
			String roles = s0.split(":")[1].trim();	//所有不隐藏的角色
			if(roles.contains(key)){
				setResult(name, true);
			}else{
				setResult(name, false);
			}
		}
		
		setResult("role", key);//角色（更新保存时用）
		setResult("usercenter", getLoginUser().getUsercenter());	//登录人所在的用户中心
		return "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String query(@Param Cangk bean){
		
		try {
			setResult("result", cangkService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 获得单条记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String get(@Param Cangk bean ){
		
		try {
			if("".equals(bean.getCangkbh())){
				bean.setCangkbh("  ");
			}
			setResult("result", cangkService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	/**
	 * 获得多行记录
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String list(@Param Cangk bean) {
		
		try {
			setResult("result", cangkService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
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
	public String listByUserCenter(@Param Cangk bean) {

		try {
			if(null == bean.getUsercenter()){
			     bean.setUsercenter(getLoginUser().getUsercenter());
			}
			setResult("result", cangkService.list(bean));
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result",map);
		}
		
		return AJAX;
	}
	
	/**
	 * 删除
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String remove(@Param Cangk bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			bean.setEditor(getLoginUser().getUsername());			//修改人
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());	//修改时间
			//逻辑删除
			map.put("message", cangkService.doDelete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库", "数据失效");
		}catch (Exception e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map);
		return AJAX;
	}
	
	/**
	 * @description 保存子仓库
	 * @param bean 仓库
	 * @param zicks
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String saveZicks(@Param Cangk bean,@Param("operant") Integer operant,@Param("zicks_insert") ArrayList<Zick> insert,
			@Param("zicks_edit") ArrayList<Zick> edit , @Param("zicks_delete") ArrayList<Zick> delete,@Param("role") String role) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(cangkService.save(bean,operant,insert,edit,delete,getLoginUser()),"i18n.ckx.cangk.i18n_fahzt");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", map);
		return AJAX;
	}
	
	
	/**
	 * 仓库-子仓库导出
	 * @param bean
	 * @author wangyu
	 * @date 2012-11-20
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String download(@Param Cangk bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			//仓库
			List<Cangk> cangkData = cangkService.listForExport(bean);
			dataSurce.put("cangkData", cangkData);
			//子仓库
			List<Zick> zickData = cangkService.listZick(bean);
			dataSurce.put("zickData", zickData);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("cangk.ftl", dataSurce, response, "仓库-子仓库", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库-子仓库", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库-子仓库", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 查询用户中心对应的工厂
	 * @param bean
	 * @author hanwu
	 * @date 2015-07-20
	 * @return String
	 */
	public String queryGongc(@Param("usercenter") String usercenter) {

		try {
			if(usercenter==null||"".equals(usercenter)){
				usercenter=getLoginUser().getUsercenter();
			}
			setResult("result", cangkService.queryGongc(usercenter));
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result",map);
		}
		
		return AJAX;
	}
	
}
