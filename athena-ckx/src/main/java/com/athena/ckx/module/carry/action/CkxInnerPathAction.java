package com.athena.ckx.module.carry.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.carry.CkxInnerPathAndModle;
import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.module.carry.service.CkxInnerAndModleService;
import com.athena.ckx.util.GetPostOnly;
import com.athena.ckx.util.xls.ckxinnerpath.XlsHandlerUtilsinnerpath;
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

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxInnerPathAction extends ActionSupport{
	@Inject
	private CkxInnerAndModleService ckxInnerAndModleService;
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
	@SuppressWarnings("rawtypes")
	public String execute(@Param CkxInnerPathAndModle bean) {
        Map map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色		
		String key = GetPostOnly.getPostOnly(map);
		setResult("role", key);//角色
		setResult("loginUser", getLoginUser());
		return  "select";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @return
	 * @update lc 2016.10.31
	 */
	public String query(@Param CkxInnerPathAndModle bean) {
		try{
			//当用户中心查询条件为空时，只能查询自己权限下的用户中心
			if(bean.getUsercenter()==null){
				if(AuthorityUtils.getSecurityUser().getUcList()!=null && AuthorityUtils.getSecurityUser().getUcList().size()>0){
					List<String> uclist=AuthorityUtils.getSecurityUser().getUcList();
					StringBuffer sb=new StringBuffer();
					for (String s : uclist) {
						sb.append("'"+s+"',");
					}
					sb.delete(sb.length()-1, sb.length());
					bean.setUclist(sb.toString());
				}
			}
			setResult("result", ckxInnerAndModleService.select(getParams(), bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "内部物流路径", "数据查询结束");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "内部物流路径", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	/**
	 * 单对象获取
	 * @param bean
	 * @return
	 */
	public String get(@Param CkxInnerPathAndModle bean){
		setResult("result", ckxInnerAndModleService.get(bean));
		return AJAX;
	}
	
	/**
	 * 添加内部物流路径
	 * @param bean
	 * @param list
	 * @return
	 */
	public String addCkxInnerPath(@Param("bean") CkxInnerPathAndModle bean,@Param("ling") ArrayList<CkxLingjxhd> list){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message",ckxInnerAndModleService.addCkxInnerPath(bean,list,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "内部物流路径", "添加内部物流路径结束");
		} catch (Exception e) {
			setResult("success",false );
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "内部物流路径", "添加内部物流路径结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	/**
	 * 增加内部物流路径（手工）
	 * @param bean
	 * @return
	 */
	public String addCkxInnerPath1(@Param CkxInnerPathAndModle bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			List<CkxLingjxhd> list = new ArrayList<CkxLingjxhd>();
			CkxLingjxhd lingjxhd = new CkxLingjxhd();
			lingjxhd.setUsercenter(bean.getUsercenter());
			lingjxhd.setLingjbh(bean.getLingjbh());
			lingjxhd.setFenpqbh(bean.getFenpqh());			
//			lingjxhd.setShengcxbh(bean.getShengcxbh());
			ckxInnerAndModleService.checkLingjXHD(lingjxhd);
			list.add(lingjxhd);
			map.put("message",ckxInnerAndModleService.addCkxInnerPath(bean,list,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "内部物流路径", "添加内部物流路径结束");
		} catch (Exception e) {
			setResult("success",false );
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "内部物流路径", "添加内部物流路径结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	/**
	 * 修改内部物流路径
	 * @param bean
	 * @return
	 */
	public String saveCkxInnerPath(@Param CkxInnerPathAndModle bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxInnerAndModleService.saveCkxInnerPath(bean,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "内部物流路径", "修改内部物流路径结束");
		} catch (RuntimeException e) {
			setResult("success",false );
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "内部物流路径", "修改内部物流路径结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 物理删除
	 * @param bean
	 * @return
	 * @update lc 2016.10.18
	 */
	public String deleteCkxInnerPath(@Param("list") ArrayList<CkxInnerPathAndModle> ckxInnerPathAndModle){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxInnerAndModleService.deleteCkxInnerPath(ckxInnerPathAndModle));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "内部物流路径", "删除内部物流路径结束");
		} catch (RuntimeException e) {
			setResult("success",false );
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "内部物流路径", "删除内部物流路径结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	/**
	 * 检测有多少外部模式更新
	 * @param bean
	 * @return
	 */
	public String checkUpdateInnerPath(@Param CkxInnerPathAndModle bean,@Param("ling") ArrayList<CkxLingjxhd> list,@Param("operate") Integer operate) {
		try{
			setResult("result", ckxInnerAndModleService.checkUpdateInnerPath(bean, list,operate));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "检测有多少外部模式更新", "检测有多少外部模式更新结束");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "检测有多少外部模式更新", "检测有多少外部模式更新", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	
	/**
	 * 内部物流导入模板下载
	 * lc 2016-10-19
	 */
	public String downloadInnerpathMob(@Param CkxInnerPathAndModle bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("neibwlMob.ftl", dataSurce, response, "内部物流-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "内部物流", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "内部物流", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "内部物流", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 内部物流导入
	 * @author lc
	 * @Date 2016-10-21
	 * @Param bean
	 * @return String
	 * */
	@SuppressWarnings("rawtypes")
	public String uploadInnerpath(@Param CkxInnerPathAndModle bean){
		Map<String,String> message = new HashMap<String,String>();
		Map rolemap = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色			
		String key = GetPostOnly.getPostOnly(rolemap);
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();

			Map<String, String> map=new HashMap<String, String>();			
			String mes = "";
			if ("ZBCPOA".equals(key) || "root".equals(key)) {				
				mes = XlsHandlerUtilsinnerpath.analyzeXls(map,request,getLoginUser().getUsername(),baseDao);				
			}else if ("WULGYY".equals(key)) {
				String value = (String) rolemap.get(key);
				Fenpq fenpq = new Fenpq();
				fenpq.setWulgyyz(value);
				//只校验自己权限下的用户中心的分配区
				if(AuthorityUtils.getSecurityUser().getUcList()!=null && AuthorityUtils.getSecurityUser().getUcList().size()>0){
					List<String> uclist=AuthorityUtils.getSecurityUser().getUcList();
					StringBuffer sb=new StringBuffer();
					for (String s : uclist) {
						sb.append("'"+s+"',");
					}
					sb.delete(sb.length()-1, sb.length());
					fenpq.setUclist(sb.toString());
				}
				int listfenpq = ckxInnerAndModleService.checkFenpq(fenpq);
				if(0 == listfenpq){
					mes = "无分配区数据权限，无法导入数据！";
				}else{
					map.put("wulgyyz", value);
					mes = XlsHandlerUtilsinnerpath.analyzeXls(map,request,getLoginUser().getUsername(),baseDao);
				}								
			}else {
				mes = "无导入权限，无法导入数据！";
			}
			
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "内部物流", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "内部物流", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		setResult("loginUser", getLoginUser());
		setResult("role", key);//角色
		return "upload";
     }
}
