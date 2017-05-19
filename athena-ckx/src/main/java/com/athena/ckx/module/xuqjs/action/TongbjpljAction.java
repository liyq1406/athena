package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.entity.xuqjs.Lingjgys;
import com.athena.ckx.entity.xuqjs.Tongbjplj;
import com.athena.ckx.module.xuqjs.service.CkxGongyxhdService;
import com.athena.ckx.module.xuqjs.service.TongbjpljService;
import com.athena.ckx.util.xls.XlsHandlerUtilsckxhsj;
import com.athena.ckx.util.xls.XlsHandlerUtilstbjplj;
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
 * 同步集配零件分类Action
 * @author denggq
 * @date 2012-4-11
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class TongbjpljAction extends ActionSupport{
	
	@Inject
	private TongbjpljService tongbjpljService;
	
	@Inject
	private UserOperLog userOperLog;
	
	
	@Inject
	private CkxGongyxhdService ckxGongyxhdService;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	@Inject
	private AbstractIBatisDao baseDao;
	
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
	  * @author denggq
	  * @Date 2012-4-11
	  * @return String
	  */
	public String execute(){
		setResult("usercenter",getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询
	 * @author denggq
	 * @Date 2012-4-11
	 * @return String
	 * */
	public String query(@Param Tongbjplj bean){
		
		try{
			setResult("result",tongbjpljService.query(bean,getParam("exportXls")));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "同步集配零件分类", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "同步集配零件分类", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	
	/**
	 * 单数据查询
	 * @author denggq
	 * @Date 2012-5-10
	 * @return String
	 * */
	public String get(@Param Tongbjplj bean){
		
		try{
			setResult("result",tongbjpljService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "同步集配零件分类", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "同步集配零件分类", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	
	
	/**
	 * 多数据查询
	 * @author denggq
	 * @Date 2012-5-10
	 * @return String
	 * */
	public String list(@Param Tongbjplj bean){
		
		try{
			setResult("result",tongbjpljService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "同步集配零件分类", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "同步集配零件分类", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	
	/**
	 * 行编辑保存数据
	 * @author denggq
	 * @Date 2012-4-11
	 * @Param insert,edit,delete
	 * @return String
	 * */
	public String save(@Param("insert") ArrayList<Tongbjplj> insert,@Param("edit") ArrayList<Tongbjplj> edit,@Param("delete") ArrayList<Tongbjplj> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(tongbjpljService.save(insert, edit, delete, getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "同步集配零件分类", "数据保存");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "同步集配零件分类", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
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
			//dataSurce.put("count", 0);
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			downloadServices.downloadFile("tongbjpljMob.ftl", dataSurce, response, "同步集配零件分类模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "同步集配零件分类", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "同步集配零件分类", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "同步集配零件分类", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 导入数据
	 * @author WANGYU
	 * @Date 2014-9-16
	 * @Param bean
	 * @return String
	 * */
	public String upload(@Param Tongbjplj bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			//查询出 零件的集合
			Map<String,Object> maplingj = tongbjpljService.getLingjMap();
			//获取生产线的集合
			Map<String,Object> mapscx = ckxGongyxhdService.getChanxMap();
			//获取配送类型的集合
			Map<String,Object> mappslx= tongbjpljService.getPeislxMap();
			//查询同步集配零件分类的集合
			Map<String,String> maptbjplj =tongbjpljService.getTbjpljMap();
			//查询零件仓库的集合
			Map<String,String> mapljck =tongbjpljService.getLingjckMap();
			// 将创建人,修改人 编辑 为当前登录人
			String mes = XlsHandlerUtilstbjplj.analyzeXls(mapljck,maptbjplj,maplingj,mapscx,mappslx,request,getLoginUser().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "同步集配零件分类", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "同步集配零件分类", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setRequestAttribute("uploudmessage",message.get("message"));
		return "upload";
	}
	
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author denggq
	 * @date 2012-5-22
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String download(@Param Tongbjplj bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<Lingjgys> rows = tongbjpljService.list(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("tongbjplj.ftl", dataSurce, response, "同步集配零件", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "同步集配零件", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "同步集配零件", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	/**
	 * 获得零件名称
	 * @param bean
	 * @return lingjmc
	 */
    public  String  getLingjmc(){
		Map<String, String> map = getParams();
		String lingjmc="";
    	try {
    		 lingjmc = tongbjpljService.selectLingjmc(map);
		} catch (Exception e) {
			 userOperLog.addError(CommonUtil.MODULE_CKX, "同步集配零件", "零件名称查询失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("lingjmc", lingjmc);
    	   return  AJAX;
    }
}
