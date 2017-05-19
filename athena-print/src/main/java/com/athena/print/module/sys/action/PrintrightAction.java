/**
 * 
 */
package com.athena.print.module.sys.action;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.print.entity.sys.Zick;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.print.entity.sys.Cangk;
import com.athena.print.entity.sys.PrintDevicegroup;
import com.athena.print.entity.sys.PrintStrogdict;
import com.athena.print.entity.sys.Printright;
import com.athena.print.entity.sys.Printuser;
import com.athena.print.module.sys.service.PrintrightService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PrintrightAction extends ActionSupport {
	@Inject
	private PrintrightService printrightService;
	
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private AbstractIBatisDao baseDao;
	
	public UserInfo getUserInfo() {
		return (UserInfo) getSessionAttribute(ToftConst.Toft_SessionKey_UserData);
	}
	
	/**
	 * 获取用户信息
	 * @Date 12/02/28
	 * @return LoginUser
	 */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param Printright bean) {
		//设置用户中心
		setResult("loginUsercenter",getLoginUser().getUsercenter());
		return  "select";
	}
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String executegly(@Param Printright bean) {
		//设置用户中心
		setResult("loginUsercenter",getLoginUser().getUsercenter());
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param Printright bean) {
		setResult("result",printrightService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限操作", "查询数据结束");
		return AJAX;
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String querys(@Param Printright bean) {
		setResult("result",printrightService.selectS(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限操作", "查询数据结束");
		return AJAX;
	}
	
	/**
	 * 分页查询 管理员组编号
	 * @param bean
	 * @return
	 */
	public String queryGly(@Param Printright bean) {
		setResult("result",printrightService.selectGly(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限操作", "查询数据结束");
		return AJAX;
	}
	
	
	/**
	 * 被动调用后数据保存
	 * @param bean
	 */
	public String save(@Param Printright bean,
			@Param("ckx_printright_insert") ArrayList<Printright> insert ,
			@Param("ckx_printright_edit") ArrayList<Printright> edit ,
			@Param("ckx_printright_delete") ArrayList<Printright> delete ) {
		Map<String,String> message = new HashMap<String,String>();
		try {
			//被动调用后数据的插入操作
			Message m=new Message(printrightService.save(bean.getUsercenter(),bean.getUserscode(), insert, edit, delete,getLoginUser().getUsername()),"i18n.print.i18n_print");
			//将提示信息放在Message中
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限操作", "保存数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "打印机组操作", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//放在result 在前台页面给出提示
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 直接数据保存
	 * @param bean
	 */
	public String saves(@Param Printright bean,@Param("insert") ArrayList<Printright> insert,
	           @Param("edit") ArrayList<Printright> edit,
	   		   @Param("delete") ArrayList<Printright> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			//权限的批量处理总方法
			Message m=new Message(printrightService.saves(bean,insert, edit, delete,getLoginUser().getUsername()),"i18n.print.i18n_print");
			//将提示信息放在Message中
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限操作", "保存数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "打印机组操作", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//放在result 在前台页面给出提示
			setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 直接数据保存
	 * @param bean
	 */
	public String savesgly(@Param Printright bean,@Param("insert") ArrayList<Printright> insert,
	           @Param("edit") ArrayList<Printright> edit,
	   		   @Param("delete") ArrayList<Printright> delete){
		Map<String,String> message = new HashMap<String,String>();
		try {
			//权限的批量处理总方法
			Message m=new Message(printrightService.savesgly(bean,insert, edit, delete,getLoginUser().getUsername()),"i18n.print.i18n_print");
			//将提示信息放在Message中
			message.put("message", m.getMessage());
			
			userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限操作", "保存数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "打印机组操作", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//放在result 在前台页面给出提示
		setResult("result",message);
		
		return AJAX;
	}
	
	
	
	/**
	 * 校验用户组编号
	 * @param bean
	 */
	public String jyuserscode(){
		boolean flag = false;
		String users = this.getParam("userscode");
		String usercenter = getLoginUser().getUsercenter();
		List<Printuser> uList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintuser1",usercenter);
		if(null!=users){
			for (Printuser printuser : uList) {
				if(users.equals(printuser.getUserscode())){
					flag = true;
					break;
				}
			}
		}
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限校验用户编号操作", "校验用户编号结束");
		setResult("result",flag);
		return AJAX;
	}
	
	/**
	 * 校验单据组编号
	 * @param bean
	 */
	public String jyscodes(){
		boolean flag1 = false;
		String scodes = this.getParam("scodes");
		String usercenter = getLoginUser().getUsercenter();
		List<PrintStrogdict> dList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintStrogdicts",usercenter);
		if(null!=scodes){
			for (PrintStrogdict printStrogdict : dList) {
				if(scodes.equals(printStrogdict.getDanjzbh())){
					flag1 = true;
					break;
				}
			}
		}
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限校验单据组编号操作", "校验单据组编号结束");
		setResult("result",flag1);
		return AJAX;
	}
	
	/**
	 * 校验用仓库编号
	 * @param bean
	 */
	public String jystoragescode(){
		boolean flag2 = false;
		String storagescode = this.getParam("storagescode");
		String usercenter = getLoginUser().getUsercenter();
		List<Cangk> cList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryCangkr",usercenter);
		if(null!=storagescode){
			for (Cangk cangk : cList) {
				if(storagescode.equals(cangk.getCangkbh())){
					flag2 = true;
					break;
				}
			}
		}
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限校验仓库编号操作", "校验仓库编号结束");
		setResult("result",flag2);
		return AJAX;
	}
	
	/**
	 * 校验管理员组编号
	 * @param bean
	 */
	public String jystoragescodegly(){
		boolean flag2 = false;
		String storagescodegly = this.getParam("storagescode");
		String usercenter = getLoginUser().getUsercenter();
		Zick zck = new Zick();
		zck.setUsercenter(usercenter);
		zck.setGuanlyzbh(storagescodegly);
		List<Zick> cList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryGuanlybh",zck);
		if(null!=storagescodegly){
			if(0!=cList.size()){	
				flag2 = true;
			}
		}
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限校验管理员组编号操作", "校验仓库编号结束");
		setResult("result",flag2);
		return AJAX;
	}
	
	/**
	 * 校验打印机组编号
	 * @param bean
	 */
	public String jyspcodes(){
		boolean flag3 = false;
		String spcodes = this.getParam("spcodes");
		String usercenter = getLoginUser().getUsercenter();
		List<PrintDevicegroup> sList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintDevicegroup2",usercenter);
		if(null!=spcodes){
			for (PrintDevicegroup printDevicegroup : sList) {
				if(spcodes.equals(printDevicegroup.getSpcodes())){
					flag3 = true;
					break;
				}
			}
		}
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限校验打印机组编号操作", "校验打印机组编号结束");
		setResult("result",flag3);
		return AJAX;
	}
	
	/**
	 * 通用查询
	 * @param bean
	 */
	public String list(@Param Printright bean) {
		//权限的通用查询
		setResult("result", printrightService.list(bean));
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param Printright bean) {
		//权限的单记录查询
		setResult("result", printrightService.get(bean));
		return AJAX;
	}
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param Printright bean) {
		//权限的物理删除
		setResult("result", printrightService.doDelete(bean));
		return AJAX;
	}
	
}
