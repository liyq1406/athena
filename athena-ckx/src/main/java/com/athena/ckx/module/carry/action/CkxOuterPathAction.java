package com.athena.ckx.module.carry.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.carry.CkxWaibwl;
import com.athena.ckx.module.carry.service.CkxWaibwlService;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.xls.ckxouterpath.XlsHandlerUtilsouterpath;
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
 * 外部物流路径
 * @author kong
 * 2012-02-12
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxOuterPathAction extends ActionSupport{
	
	
	//服务
	@Inject
	private CkxWaibwlService outService;
	@Inject
	private UserOperLog userOperLog;
	// 注入downloadsevices
	@Inject
	private DownLoadServices downloadServices = null;
	@Inject
	private AbstractIBatisDao baseDao;
	/**
	 * 获取当前用户信息
	 * @return
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param CkxWaibwl bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}
	
	
	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String queryOut(@Param CkxWaibwl bean) {
		setResult("result", outService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "外部物流路径", "数据查询结束");
		return AJAX;
	}
	
	
	/**
	 * 获取外部物流路径
	 * @param bean
	 * @return
	 */
	public String getOut(@Param CkxWaibwl bean){
		setResult("result", outService.get(bean));
		return AJAX;
	}
	
	/**
	 * 添加外部物流路径
	 * @param bean
	 * @return
	 */
	public String addCkxOuterPath(@Param CkxWaibwl bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", outService.save(bean,1,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "外部物流路径", "添加外部物流路径结束");
		} catch (RuntimeException e) {
			map.put("message",e.getMessage());
			setResult("success",false );
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 保存外部物流路径
	 * @param bean
	 * @param operant
	 * @return
	 */
	public String saveCkxOuterPath(@Param CkxWaibwl bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message",outService.save(bean,2,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "外部物流路径", "修改外部物流路径结束");
		} catch (RuntimeException e) {
			map.put("message",e.getMessage());
			setResult("success",false );
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 物理删除
	 * @param bean
	 * @return
	 * @update lc 2016.10.17
	 */
	public String removeCkxOuterPath(@Param("list") ArrayList<CkxWaibwl> ckxWaibwl){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(outService.removeCkxOuterPath(ckxWaibwl),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "外部物流路径", "删除外部物流路径结束");
		} catch (ServiceException e) {
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "外部物流路径", "删除外部物流路径结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	public String checkEditCkxOuterPath(@Param CkxWaibwl bean,@Param("operate") Integer operate){
		try{
			setResult("result", outService.checkEditCkxOuterPath(bean,operate));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "检测有多少承运商、卸货站台编组", "检测有多少承运商、卸货站台编组更新");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "检测有多少承运商、卸货站台编组", "检测有多少承运商、卸货站台编组更新", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	
	
	
	/**
	 * 外部物流导入模板下载
	 * xh  716
	 */
	public String downloadOuterpathMob(@Param CkxWaibwl bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("waibwlMob.ftl", dataSurce, response, "外部物流-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "外部物流", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "外部物流", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "外部物流", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	
	/**
	 * 外部物流导入
	 * @author xh
	 * @Date 2015-07-16
	 * @Param bean
	 * @return String
	 * */
	public String uploadOuterpath(@Param CkxWaibwl bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();

			Map<String, String>  map=new HashMap();
			String mes = XlsHandlerUtilsouterpath.analyzeXls(map,request,getLoginUser().getUsername(),baseDao);
			
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "外部物流", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "外部物流", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		setResult("usercenter", getLoginUser().getUsercenter());
		return "upload";
     }
 }
