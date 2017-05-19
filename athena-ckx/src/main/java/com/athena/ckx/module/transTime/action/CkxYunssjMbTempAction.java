package com.athena.ckx.module.transTime.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.transTime.CkxYunssjMBSx;
import com.athena.ckx.entity.transTime.CkxYunssjMb;
import com.athena.ckx.entity.workCalendar.CkxCalendarGroup;
import com.athena.ckx.entity.workCalendar.CkxCalendarTeam;
import com.athena.ckx.entity.workCalendar.CkxCalendarVersion;
import com.athena.ckx.entity.xuqjs.Ticxxsj;
import com.athena.ckx.module.transTime.service.CkxYunssjMbService;
import com.athena.ckx.module.workCalendar.service.CkxCalendarTeamService;
import com.athena.ckx.module.workCalendar.service.CkxCalendarVersionService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetPostOnly;
import com.athena.ckx.util.xls.yunssjmb.XlsHandlerUtilsYssjmb;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;


/**
 * 运输时间模板
 * @author hj
 * @date 2012-03-19
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxYunssjMbTempAction extends ActionSupport{
	@Inject
	private CkxYunssjMbService yssjmbService;
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private DownLoadServices  downloadServices;
	@Inject
	private AbstractIBatisDao baseDao;
	@Inject
	private CkxCalendarTeamService ckxCalendarTeamService;
	@Inject
	private CkxCalendarVersionService ckxCalendarVersionService;
	/**
	 * 获取当前用户信息
	 * @author hj
	 * @Date 2012-03-19
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 跳转页面（运输时间（计算））
	 * @param bean
	 * @return
	 */
	public String execute_temp() {
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return  "select_temp";
	}
	/**
	 * 跳转页面（运输时间（实际））
	 * @return
	 */
	public String execute() {
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return  "select";
	}
	/**
	 * 分页查询（运输时间（计算））
	 * @param bean
	 * @return
	 */
	public String query_temp(@Param CkxYunssjMb bean){
		bean.setUsercenter(getLoginUser().getUsercenter());		
		setResult("result", yssjmbService.select_Temp(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间计算模板", "数据查询结束");
		return AJAX;
	}
	//进入手工计算页面
	public String tosgjsPage(@Param CkxYunssjMb b){
	
	
		CkxCalendarGroup bean=new CkxCalendarGroup();

		bean.setUsercenter(getLoginUser().getUsercenter());	
		bean.setLxLength("4");
		bean.setBiaos("1");
//		if(AuthorityUtils.getSecurityUser().getUcList()!=null && AuthorityUtils.getSecurityUser().getUcList().size()>0){
//			List<String> uclist=AuthorityUtils.getSecurityUser().getUcList();
//			StringBuffer sb=new StringBuffer();
//			for (String s : uclist) {
//			sb.append("'"+s.substring(1, 2)+"',");
//			}
//			sb.delete(sb.length()-1, sb.length());
//			bean.setUclist(sb.toString());
//		}
		//当前用户对应权限的卸货站台编组
		setResult("result", yssjmbService.query_Temp(bean));
		
		CkxCalendarVersion version=new CkxCalendarVersion();
		version.setUsercenter(bean.getUsercenter());
	    CkxCalendarTeam team=new CkxCalendarTeam();
		team.setBiaos("1");
//		if(AuthorityUtils.getSecurityUser().getUcList()!=null && AuthorityUtils.getSecurityUser().getUcList().size()>0){
//			List<String> uclist=AuthorityUtils.getSecurityUser().getUcList();
//			StringBuffer sb=new StringBuffer();
//			StringBuffer sb2=new StringBuffer();
//			for (String s : uclist) {
//			sb.append("'"+s.substring(1, 2)+"',");
//			sb2.append("'"+s+"',");
//			}
//			sb.delete(sb.length()-1, sb.length());
//			sb2.delete(sb2.length()-1, sb2.length());
//			team.setUclist(sb.toString());
//			version.setUclist(sb2.toString());
//		}
		team.setUclist("'"+getLoginUser().getUsercenter().substring(1, 2)+"'");
		version.setUclist("'"+getLoginUser().getUsercenter()+"'");
		//获取工作日历
	    setResult("gongzrl", ckxCalendarVersionService.getVersionNo(version));
	    //获取工作
		setResult("gongzsjbz", ckxCalendarTeamService.getSelectTeamCode(team));
	
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间进入手工计算页面", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 分页查询（运输时间（实际））
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxYunssjMb bean){
		bean.setUsercenter(getLoginUser().getUsercenter());	
		setResult("result", yssjmbService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间实际模板", "数据查询结束");
		return AJAX;
	}
	
	
	/**
	 * 数据修改(运输时间（计算）)
	 * @param edit
	 * @return
	 */
	public String save_temp(@Param("edit") ArrayList<CkxYunssjMb> edit){
		Map<String,String> message = new HashMap<String,String>();
		try {			
			Message m=new Message(yssjmbService.edit_Temp(edit, getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间计算模板", "数据编辑结束");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输时间计算模板", "数据编辑结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 数据操作 运输时间（实际）
	 * @param insert
	 * @param edit
	 * @param delete
	 * @return
	 */
	public String save(@Param("insert") ArrayList<CkxYunssjMb> insert,
			@Param("edit") ArrayList<CkxYunssjMb> edit,
			@Param("delete") ArrayList<CkxYunssjMb> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {			
			Message m=new Message(yssjmbService.save(insert,edit,delete, getLoginUser().getUsername()),"i18n.ckx.paicfj.i18n_shengcx");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间实际模板", "数据编辑结束");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输时间实际模板", "数据编辑结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	/**
	 * 数据生效
	 * @return
	 */
	public String effect(){
		Map<String,String> message = new HashMap<String,String>();
		try {			
			message.put("message", yssjmbService.effect(getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间实际模板", "数据生效结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输时间实际模板", "数据生效结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	/** xss-0011489
	 * 手工计算
	 * @return
	 */
/*	public String sgjs(){
		Map<String,String> message = new HashMap<String,String>();
		try {			
			message.put("message", yssjmbService.sgjs(getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间手工计算", "结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输时间手工计算", "结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}*/
	
	/** xss-0011489
	 * 手工计算
	 * @return
	 */
	public String sgjs(@Param("list") ArrayList<CkxYunssjMb> bean){
		Map<String,String> message = new HashMap<String,String>();
		try {	
			if(bean!=null  && bean.size()>0){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(
			"workCalendar.deleteCKX_CALENDAR_GROUP_temp");// 清空交付日历表数据
			for (CkxYunssjMb ckxYunssjMb : bean) {
			 if(StringUtils.isNotBlank(ckxYunssjMb.getGongzsjbz()) && StringUtils.isNotBlank(ckxYunssjMb.getGongzrl())){
				CkxCalendarGroup group=new CkxCalendarGroup();
				group.setUsercenter(getLoginUser().getUsercenter());
				group.setAppobj(ckxYunssjMb.getXiehztbh());
				group.setBianzh(ckxYunssjMb.getGongzsjbz());
				group.setRilbc(ckxYunssjMb.getGongzrl());
				group.setBiaos("1");
				group.setCreator(getLoginUser().getUsername());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(
						"workCalendar.insertCkxCalendarGroupTemp", group);
				}
			}
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(
				"ts_ckx.deleteTicxxsjTemp");// 清空交付日历表数据
				Ticxxsj t = new Ticxxsj();// 剔除休息时间实体类
				t.setRiq(DateTimeUtil.getCurrDate());// 前两天 未来18天
				t.setCreator(AuthorityUtils.getSecurityUser().getUsername());// 增加人
				t.setEditor(AuthorityUtils.getSecurityUser().getUsername());// 修改人
				// 按照编组号生成未来几日剔除休息时间
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(
						"ts_ckx.insertTicxxsjTempByBianzh", t);// 根据工作时间分组，日历版次和工作时间生成剔除休息日的工作时间
				
				message.put("message", yssjmbService.sgjs(getLoginUser()));
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间手工计算", "结束");
			}
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输时间手工计算", "结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 数据导出
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String derived(@Param CkxYunssjMb bean,@Param("type") String type){
//		HttpServletRequest request = ActionContext.getActionContext().getRequest();
//		HttpServletResponse response = ActionContext.getActionContext().getResponse();
//		Map<String,String> message = new HashMap<String,String>();
//		String fileName = getParam("fileName");
//		try {
//			DerivedFile  derivedFiles = new DerivedFile();
//			List<?> list = yssjmbService.list(new CkxYunssjMb());
//			derivedFiles.derivedXls(request, response, fileName,list);
//		} catch (Exception e) {
//			message.put("message", e.getMessage());
//			setResult("result",message);
//		}
//		return AJAX;
		bean.setUsercenter(getLoginUser().getUsercenter());
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			//按照数据权限导出
			String creator = yssjmbService.getXiehztbzhs(bean.getUsercenter());
			bean.setCreator(creator);
			List<CkxYunssjMb> rows = (List<CkxYunssjMb>) yssjmbService.getClass().getMethod("list"+type,CkxYunssjMb.class).invoke(yssjmbService,bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("yunssjmb.ftl", dataSurce, response, "yunsjMb"+type, ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间模板（计算）", "数据导出");
		
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输时间模板（计算）", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	/**
	 * 数据导入
	 * @return
	 */
	public String importExcel(){
		Map<String,String> message = new HashMap<String,String>();
		try {
			// 0007099  按照用户中心清除数据
			CkxYunssjMb bean = new CkxYunssjMb();
			bean.setUsercenter(getLoginUser().getUsercenter());
			//按照用户中心清除数据
//			yssjmbService.removeTemp(bean);
			String xiehztbzs = yssjmbService.getXiehztbzhsUpdate(bean.getUsercenter());
			HttpServletRequest request = ActionContext.getActionContext().getRequest();
			
			Map<String, String>  map= new HashMap<String, String>();
			map.put("xiehztbzs", xiehztbzs);
			map.put("usercenter", getLoginUser().getUsercenter());
			// 将创建人,修改人 编辑 为当前登录人
			String mes = XlsHandlerUtilsYssjmb.analyzeXls(map,request,getLoginUser().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}

			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间模板（计算）", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输时间模板（计算）", "数据导入失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		//获取到用户中心
		setResult("loginUsercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询（定时生效）
	 * @param bean
	 * @return
	 */
	public String queryDingssx(@Param CkxYunssjMBSx bean){
		bean.setUsercenter(getLoginUser().getUsercenter());	
		setResult("result", yssjmbService.queryDingssx(bean,getLoginUser()));
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间实际模板", "数据查询结束");
		return AJAX;
	}
	
	/**
	 * 定时生效 保存
	 * @param insert
	 * @param edit
	 * @param delete
	 * @return
	 */
	public String saveDingssx(@Param("dssx_insert") ArrayList<CkxYunssjMBSx> insert,
			@Param("dssx_edit") ArrayList<CkxYunssjMBSx> edit,
			@Param("dssx_delete") ArrayList<CkxYunssjMBSx> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {			
			Message m=new Message(yssjmbService.saveDingssx(insert,edit,delete, getLoginUser().getUsername(),getLoginUser()));
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "运输时间实际模板", "数据编辑结束");
		} catch (Exception e) {
			setResult("success", false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "运输时间实际模板", "数据编辑结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
}
