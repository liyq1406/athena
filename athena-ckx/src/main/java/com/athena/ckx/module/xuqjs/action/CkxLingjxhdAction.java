package com.athena.ckx.module.xuqjs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.CkxLingjxhds;
import com.athena.ckx.module.xuqjs.service.CkxGongyxhdService;
import com.athena.ckx.module.xuqjs.service.CkxLingjxhdService;
import com.athena.ckx.util.GetPostOnly;
import com.athena.ckx.util.xls.XlsHandlerUtils;
import com.athena.ckx.util.xls.XlsHandlerUtilsbg;
import com.athena.ckx.util.xls.XlsHandlerUtilsjipxx;
import com.athena.ckx.util.xls.XlsHandlerUtilslingjxhd;
import com.athena.ckx.util.xls.XlsHandlerUtilstongbxx;
import com.athena.component.service.Message;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.db.ConstantDbCode;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;


/**
 * 消耗点-零件Action
 * @author denggq
 * @date 2018-4-18
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxLingjxhdAction  extends BaseWtcAction{
	
	@Inject
	private UserOperLog userOperLog;				//用户日志
	
	@Inject
	private CkxLingjxhdService lingjxhdService;
	@Inject
	private AbstractIBatisDao baseDao;
	
	@Inject 
	private CkxGongyxhdService gongyxhdsService;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	protected static Logger logger = Logger.getLogger(CkxLingjxhdAction.class);
	/**
	 * 登录人信息
	 * @author denggq
	 * @date 2018-4-18
	 * @return bean
	 */
	public LoginUser getLoginUser() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
	
	
	/**
	  * 主页面
	  * @author denggq
	  * @Date 2018-4-18
	  * @return String
	  */
	public String execute(){
		Map<String,String> map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map) ;
		
		setResult("role", key);//角色（更新保存时用）
		setResult("usercenter", StringUtils.defaultIfEmpty(getParam("usercenter"), getLoginUser().getUsercenter()));
		setResult("lingjbh",getParam("guanjz1"));
		setResult("fenpqbh",getParam("guanjz2")!= null?getParam("guanjz2").substring(0,5):null);
		setResult("biaos", StringUtils.defaultIfEmpty(getParam("biaos"), "1"));
		return "select";
	}
	
	
	/**
	  * 集配信息主页面
	  * @author denggq
	  * @Date 2018-4-18
	  * @return String
	  */
	public String executejipxx(){
		setResult("usercenter", StringUtils.defaultIfEmpty(getParam("usercenter"), getLoginUser().getUsercenter()));
		return "select";
	}
	
	
	
	/**
	  * 同步信息主页面
	  * @author CSY
	  * @Date 2016-03-18
	  * @return String
	  */
	public String executetongbxx(){
		setResult("usercenter", StringUtils.defaultIfEmpty(getParam("usercenter"), getLoginUser().getUsercenter()));
		return "select";
	}
	
	
	
	/**
	 * 分页查询
	 * @author denggq
	 * @Date 2018-4-18
	 * @return String
	 * */
	public String queryjipxx(@Param CkxLingjxhd bean){
		try{
			setResult("result",lingjxhdService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "集配信息", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "集配信息", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 分页查询
	 * @author CSY
	 * @Date 2016-03-18
	 * @return String
	 * */
	public String querytongbxx(@Param CkxLingjxhd bean){
		try{
			setResult("result",lingjxhdService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "同步信息", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "同步信息", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	
	/**
	 * 分页查询
	 * @author denggq
	 * @Date 2018-4-18
	 * @return String
	 * */
	public String query(@Param CkxLingjxhd bean){
		try{
			setResult("result",lingjxhdService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 单数据查询
	 * @author denggq
	 * @Date 2018-4-18
	 * @return String
	 * */
	public String get(@Param CkxLingjxhd bean){
		try{
			setResult("result",lingjxhdService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件", "单数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 单数据查询
	 * @author denggq
	 * @Date 2018-4-18
	 * @return String
	 * */
	public String list(@Param CkxLingjxhd bean){
		try{
			setResult("result",lingjxhdService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件", "多数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 单数据保存
	 * @author denggq
	 * @Date 2018-4-18
	 * @Param bean operant
	 * @return String
	 * */
	public String save(@Param CkxLingjxhd bean,@Param("operant") Integer operant){
		Map<String,String> message = new HashMap<String,String>();
		try {
			WtcResponse wtc = null;
			if(null != bean.getPeislxbh()){
				Map<String,String> map = new HashMap<String,String>();
				map.put("peislx", bean.getPeislxbh());
				//配送类型增加用户中心 mantis 5006
				map.put("usercenter", bean.getUsercenter());
				logger.info("调用仓库WTC配送类别交易接口服务开始");
				wtc = this.callWtc(CkxLingjxhd.WTC_CHECK_PSLX, map);
			}
			String saveMsg = lingjxhdService.save(bean, getLoginUser().getUsername(), operant,wtc);
			Message m = new Message(saveMsg,"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件", "单数据保存");
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件", "单数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * @description 行编辑保存
	 * @author denggq
	 * @date 2012-4-25
	 * @return String
	 */
	public String saves(@Param("insert") ArrayList<CkxLingjxhd> insert,@Param("edit") ArrayList<CkxLingjxhd> edit , @Param("delete") ArrayList<CkxLingjxhd> delete) {
		
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(lingjxhdService.saves(insert,edit,delete,getLoginUser().getUsercenter(),getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件", "多数据保存");
		}catch (Exception e) {
			setResult("success",false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件", "多数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setResult("result", map);
		return AJAX;
	}
	
	
	/**
	 * 单数据保存
	 * @author denggq
	 * @Date 2018-4-18
	 * @Param bean operant
	 * @return String
	 * */
	public String savejipxx(@Param CkxLingjxhd bean,@Param("operant") Integer operant){
		Map<String,String> message = new HashMap<String,String>();
		try {
			WtcResponse wtc = null;
			if(null != bean.getPeislxbh()){
				Map<String,String> map = new HashMap<String,String>();
				map.put("peislx", bean.getPeislxbh());
				//配送类型增加用户中心 mantis 5006
				map.put("usercenter", bean.getUsercenter());
				logger.info("调用仓库WTC配送类别交易接口服务开始");
				wtc = this.callWtc(CkxLingjxhd.WTC_CHECK_PSLX, map);
			}
			Message m = new Message(lingjxhdService.savejipxx(bean, getLoginUser().getUsername(), operant,wtc),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件", "单数据保存");
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件", "单数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	
	
	/**
	 * 单数据保存
	 * @author CSY
	 * @Date 2016-03-18
	 * @Param bean operant
	 * @return String
	 * */
	public String savetongbxx(@Param CkxLingjxhd bean,@Param("operant") Integer operant){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Integer pslbCount = 1;
			if(null != bean.getPeislxbh()){
				logger.info("调用仓库配送类别交易接口服务开始");
				pslbCount = lingjxhdService.getTongbxxPeiz(bean);
			}
			Message m = new Message(lingjxhdService.savetongbxx(bean, getLoginUser().getUsername(), operant, pslbCount),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件", "单数据保存");
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件", "单数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
	
	
	
	/**
	 * 删除数据
	 * @author denggq
	 * @Date 2018-4-18
	 * @Param bean
	 * @return String
	 * */
	public String remove(@Param CkxLingjxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			Message m = new Message(lingjxhdService.doDelete(bean,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件", "数据删除");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 查询零件消耗点,且在内部物流路径中不存在
	 * <br/>物流路径专用
	 * @param bean
	 * @return
	 * @author kong
	 */
	public String queryLingjxhdByCarry(@Param CkxLingjxhd bean){
		try{
			setResult("result",lingjxhdService.queryLingjxhdByCarry(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件", "数据查询(内部物流路径中不存在)");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件", "数据查询(内部物流路径中不存在)", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 生效方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String junf(@Param("list") ArrayList<CkxLingjxhd> updateCkxLingjxhd) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(lingjxhdService.junfen(updateCkxLingjxhd,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件均分比例", "数据失效");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件均分比例", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	
	
	/**
	 * 按照查询条件导出
	 * @param bean
	 * @author denggq
	 * @date 2012-5-22
	 * @return String
	 */
	public String download(@Param CkxLingjxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			List<CkxLingjxhd> rows = lingjxhdService.listImport(bean);
			dataSurce.put("rows", rows);
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingjxhd.ftl", dataSurce, response, "消耗点-零件", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "CMJ", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "CMJ", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 导出模板
	 * @param bean
	 * @author denggq
	 * @date 2012-8-31
	 * @return String
	 */
	public String downloadMob(@Param CkxLingjxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingjxhdMob.ftl", dataSurce, response, "零件-消耗点模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件-消耗点", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件-消耗点", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件-消耗点", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 集配信息导出模板
	 * @param bean
	 * @author denggq
	 * @date 2012-8-31
	 * @return String
	 */
	public String downloadJipxxMob(@Param CkxLingjxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("jipxxMob.ftl", dataSurce, response, "集配信息-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "集配信息", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "集配信息", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "集配信息", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	/**
	 * 新增及更新导出模板
	 * @param bean
	 * @author denggq
	 * @date 2012-8-31
	 * @return String
	 */
	public String downloadljxhdMob(@Param CkxLingjxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downloadServices.downloadFile("lingjxhdMobXZ.ftl", dataSurce, response, "零件-消耗点新增及更新模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件-消耗点新增及更新", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件-消耗点新增及更新", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件-消耗点新增及更新", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String upload(@Param CkxLingjxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			//List<CkxLingjxhds> listInsertCkxLingjxhds = new ArrayList<CkxLingjxhds>();
			Map<String, String>  map=lingjxhdService.getFenpqToChanxMap();
			//工艺消耗点MAP
			Map<String, String>  gyxhdmap=lingjxhdService.getGyxhdMap();
			//消耗点的MAP
			Map<String, String>  xhdmap=lingjxhdService.getXhdMap();
			//小火车MAP
			Map<String, String>  xiaohcmap=lingjxhdService.getXiaohMap();
			//小火车编号MAP
			Map<String, String>  xiaohcbhmap=lingjxhdService.getXiaohcbhMap();
			//零件编号MAP
			Map<String, String>  lingjmap=lingjxhdService.getLingjMap();
			//0007177  将修改人编辑为当前登录人
			String mes = XlsHandlerUtilsbg.analyzeXls(lingjmap,xiaohcmap,xiaohcbhmap,gyxhdmap,xhdmap,map,request,getLoginUser().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setRequestAttribute("uploudmessage",message.get("message"));
		return "upload";
	}
	
	
	/**
	 * 集配信息导入数据
	 * @author denggq
	 * @Date 2018-4-18
	 * @Param bean
	 * @return String
	 * */
	public String uploadjipxx(@Param CkxLingjxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			//List<CkxLingjxhds> listInsertCkxLingjxhds = new ArrayList<CkxLingjxhds>();
			Map<String, String>  map=lingjxhdService.getCkxlingjxhToJipxx();
			//通过WebService调取2个执行层的配送类型的集合
			Map<String, Object>  peislbmap=lingjxhdService.getPeislbToWebs();
			//0007177  将修改人编辑为当前登录人
			String mes = XlsHandlerUtilsjipxx.analyzeXls(peislbmap,map,request,getLoginUser().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "集配信息", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "集配信息", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setRequestAttribute("uploudmessage",message.get("message"));
		return "upload";
	}
	
	
	/**
	 * 同步信息导入数据
	 * @author CSY
	 * @Date 2016-03-22
	 * @Param bean
	 * @return String
	 * */
	public String uploadtongbxx(@Param CkxLingjxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			//List<CkxLingjxhds> listInsertCkxLingjxhds = new ArrayList<CkxLingjxhds>();
			Map<String, String>  map=lingjxhdService.getCkxlingjxhToTongbxx();
			//通过WebService调取2个执行层的配送类型的集合
			Map<String, Object>  peislbmap=lingjxhdService.getPeislbToWebs();
			//0007177  将修改人编辑为当前登录人
			String mes = XlsHandlerUtilstongbxx.analyzeXls(peislbmap,map,request,getLoginUser().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "集配信息", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "集配信息", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setRequestAttribute("uploudmessage",message.get("message"));
		return "upload";
	}
	
	
	/**
	 * 新增及更新零件消耗点导入数据
	 * @author denggq
	 * @Date 2018-4-18
	 * @Param bean
	 * @return String
	 * */
	public String uploadlingjxhd(@Param CkxLingjxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			//得到分配区产线
			Map<String, String>  mapfenpq=gongyxhdsService.getFenpqToChanxMap();
			//验证生产线
			//Map<String,Object>  mapshengcx=gongyxhdsService.getChanxMap();
			//查询通用零件中的零件是否存在且有效
			Map<String, String>  maplingj = lingjxhdService.getLingjMap();
			//工艺消耗点MAP
			Map<String, String>  gyxhdmap=lingjxhdService.getGyxhdMap();
			//小火车MAP
			Map<String, String>  xiaohcmap=lingjxhdService.getXiaohMap();
			//小火车编号MAP
			Map<String, String>  xiaohcbhmap=lingjxhdService.getXiaohcbhMap();
			//供应商编号存在有效性
			Map<String, String>  gongysmap=lingjxhdService.getGongysMap();
			//0007177  将修改人编辑为当前登录人
			String mes = XlsHandlerUtilslingjxhd.analyzeXls(mapfenpq,gongysmap,maplingj,gyxhdmap,xiaohcmap,xiaohcbhmap,request,getLoginUser().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "集配信息", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "集配信息", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setRequestAttribute("uploudmessage",message.get("message"));
		return "upload";
	}
	
	/**
	 * 失效方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String shixiao(@Param("list") ArrayList<CkxLingjxhd> updateCkxLingjxhd) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(lingjxhdService.shixiao(updateCkxLingjxhd,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件消耗点", "数据失效");
		} catch (Exception e) {
			setResult("success",false );
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件消耗点", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	
	/**
	 * 查询PDS生效/失效时间
	 * @param bean
	 * @return
	 * @author xiah
	 */
	public String pdssj(){
	  Map m=this.getParams();
		try{
			setResult("result",lingjxhdService.queryPdssj(m));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "消耗点-零件", "数据查询(PDS生效/失效时间)");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "消耗点-零件", "数据查询(PDS生效/失效时间)失败", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
}
