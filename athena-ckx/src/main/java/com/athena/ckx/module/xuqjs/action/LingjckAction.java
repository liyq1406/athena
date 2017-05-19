package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Kuw;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.entity.xuqjs.Lingjck;
import com.athena.ckx.module.xuqjs.service.LingjckService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetPostOnly;
import com.athena.ckx.util.xls.XlsHandlerUtilsDingzkw;
import com.athena.ckx.util.xls.XlsHandlerUtilslingjck;
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
 * 零件仓库设置Action
 * @author denggq
 * 2012-4-13
 */
@Component ( scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class LingjckAction extends ActionSupport{
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	@Inject
	private LingjckService lingjckService;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2012-4-13
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 主页面
	 * @return String
	 * @author denggq
	 * @time 2012-4-13
	 */
	@SuppressWarnings("rawtypes")
	public String execute(){
		
		Map map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		setResult("role", key);//角色
		setResult("zbczxc", getzbcZxc());
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	/**
	 * 分页查询 零件仓库页面
	 * @param bean
	 * @return String
	 * @author denggq
	 * @time 2012-4-13
	 */
	public String query(@Param Lingjck bean ){
		Map<String,String> message = new HashMap<String,String>();
		try{
			setResult("result", lingjckService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件仓库设置", "数据查询");
		}catch (Exception e) {
			message.put("message", e.getMessage());
			setResult("result",message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件仓库设置", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 分页查询 零件仓库页面
	 * @param bean
	 * @return String
	 * @author denggq
	 * @time 2012-4-13
	 */
	public String queryByBaoz(@Param Lingjck bean ){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//bean.setBiaos("1");//只查询有效数据
			setResult("result", lingjckService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件仓库设置", "数据查询");
		}catch (Exception e) {
			message.put("message", e.getMessage());
			setResult("result",message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件仓库设置", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 获取单条数据
	 * @param bena
	 * @return String
	 * @author denggq
	 * @time 2012-4-13
	 */
	public String get(@Param Lingjck bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			setResult("result", lingjckService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件仓库设置", "单数据查询");
		}catch (Exception e) {
			message.put("message", e.getMessage());
			setResult("result",message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件仓库设置", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 获取多条数据
	 * @param bena
	 * @return String
	 * @author denggq
	 * @time 2012-4-13
	 */
	public String list(@Param Lingjck bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			setResult("result", lingjckService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件仓库设置", "多数据查询");
		}catch (Exception e) {
			message.put("message", e.getMessage());
			setResult("result",message);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件仓库设置", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 保存
	 * @param bean,operant
	 * @return String
	 * @author denggq
	 * @time 2012-4-13
	 */
	public String save(@Param Lingjck bean,@Param("operant") Integer operant){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(lingjckService.save(bean,operant,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件仓库设置", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件仓库设置", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 仓库替换
	 * @param bean
	 * @return String
	 * @author hj
	 * @time 
	 */
	public String replace(@Param Lingjck bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			String mes = lingjckService.replace(bean,getLoginUser().getUsername());
			message.put("message", mes);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件仓库设置", "仓库替换");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件仓库设置", "仓库替换", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 删除数据
	 * @param bean
	 * @return String
	 * @author denggq
	 * @time 2012-4-13
	 * @update lc 2016.10.24
	 */
	public String delete(@Param("list") ArrayList<Lingjck> lingjck){
		
		String editor = getLoginUser().getUsername();		//修改人
		String edit_time = DateTimeUtil.getAllCurrTime();	//修改时间
		
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(lingjckService.doDelete(lingjck, editor, edit_time),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件仓库设置", "数据删除");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件仓库设置", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
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
	public String download(@Param Lingjck bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Lingjck> rows = lingjckService.list(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingjck.ftl", dataSurce, response, "零件仓库设置", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件仓库设置", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件仓库设置", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 零件仓库导出模板
	 * @param bean
	 * @author wangyu
	 * @date 2014-11-15
	 * @return String
	 */
	public String downloadlingjckMob(@Param Lingjck bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingjckMob.ftl", dataSurce, response, "零件仓库-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件仓库", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件仓库", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件仓库", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 零件仓库导入数据
	 * @author wangyu
	 * @Date 2014-11-18
	 * @Param bean
	 * @return String
	 * */
	public String uploadlingjck(@Param Lingjck bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			//查询零件的集合
			Map<String, String>  maplingj = lingjckService.getLingjsMap();
			//查询仓库的集合
			Map<String, String>  mapcangk = lingjckService.getCangksMap();
			//查询仓库子仓库的集合
			Map<String, String>  mapzick = lingjckService.getZickMap();
			//查询包装型号的集合
			Map<String, String>  mapbaoz = lingjckService.getBaozMap();
			//查询仓库子仓库对应的卸货站台  有效的
			Map<String, String>  mapxiehzt = lingjckService.getXiehztMap();
			//0007177  将修改人编辑为当前登录人
			String mes = XlsHandlerUtilslingjck.analyzeXls(maplingj,mapcangk,mapzick,mapbaoz,mapxiehzt,request,getLoginUser().getUsername(),baseDao);
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
		setResult("zbczxc", getzbcZxc());
		return "upload";
	}
	
	/**
	 * 定置库位导入模板下载
	 * @param bean
	 * @author CSY
	 * @date 2016-11-07
	 * @return String
	 */
	public String downloadDingzkwMob(@Param Lingjck bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("dingzkwdr.ftl", dataSurce, response, "定置库位-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "定置库位-模板", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "定置库位-模板", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "定置库位-模板", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 定置库位导入
	 * @author CSY
	 * @Date 2016-11-07
	 * @Param bean
	 * @return String
	 * */
	public String uploadDingzkw(@Param Lingjck bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Map map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
			String key = GetPostOnly.getPostOnly(map);
			String mes = "";
			if ("ZXCPOA".equals(key)) {
				HttpServletRequest request =ActionContext.getActionContext().getRequest();
				//查询用户中心，用以验证导入的用户中心是否正确
				Map<String, String>  mapUsercenter = lingjckService.getUsercenterMap();
				//查询零件的集合，用以验证导入的零件是否正确
				Map<String, String>  mapLingj = lingjckService.getLingjsMap();
				//查询库位集合，用以验证导入的仓库、子仓库、库位是否正确，适用于 非 B和D结尾
				Map<String, Kuw>  mapKuw = lingjckService.getKuwMap();
				//查询仓库子仓库，用以验证导入的仓库、子仓库是否正确，适用于D结尾
				Map<String, Zick>  mapDim = lingjckService.getDimMap();
				//查询零件仓库表，用以判断是否存在，模板中存在而数据库中不存在的不执行任何操作
				Map<String, Lingjck>  mapLingjck = lingjckService.getLingjckMap();
				//0007177  将修改人编辑为当前登录人
				if (request!= null && mapUsercenter!=null && mapLingj != null && mapKuw != null && mapDim != null && mapLingjck != null) {
					mes = XlsHandlerUtilsDingzkw.analyzeXls(mapUsercenter,mapLingj,mapKuw,mapDim,mapLingjck,request,getLoginUser().getUsername(),baseDao);
				}else {
					if (mapUsercenter == null) {
						mes += "参考系用户中心无数据\n";
					}
					if (mapLingj == null) {
						mes += "参考系零件无数据\n";
					}
					if (mapKuw == null) {
						mes += "仓库子仓库库位无数据\n";
					}
					if (mapDim == null) {
						mes += "地面库无数据\n";
					}
					if (mapLingjck == null) {
						mes += "参考系零件仓库\n";
					}
				}
			}else {
				mes = "权限不足";
			}	
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "定置库位", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "定置库位", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setRequestAttribute("uploudmessage",message.get("message"));
		setResult("zbczxc", getzbcZxc());
		return "upload";
	}
	
	/**
	 * 判断是准备层还是执行层，准备层返回true，执行层返回false
	 * @return
	 */
	public boolean getzbcZxc(){
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		HttpSession session = request.getSession();
		String zbczxc = (String) session.getAttribute("zbcZxc");
		boolean flag = false;
		if("ZBC".equals(zbczxc)){
			flag = true;
		}
		return flag;
	}
	
}
