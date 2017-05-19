/**
 * 
 */
package com.athena.print.module.sys.action;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.print.entity.sys.PrintDevice;
import com.athena.print.entity.sys.PrintDevicegroup;
import com.athena.print.module.sys.service.PrintDeviceService;
import com.athena.print.module.sys.service.PrintDevicegroupService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PrintDevicegroupAction extends ActionSupport {
	@Inject
	private PrintDevicegroupService printDevicegroupService;
	
	@Inject
	private PrintDeviceService printDeviceService;
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	@Inject
	private UserOperLog userOperLog;
	
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
	 * 修改
	 * @param bean,insert,edit,delete
	 * @return
	 */
	public String save(@Param PrintDevicegroup bean , 			 
			@Param("ckx_device_insert") ArrayList<PrintDevice> insert ,
			@Param("ckx_device_edit") ArrayList<PrintDevice> edit ,
			@Param("ckx_device_delete") ArrayList<PrintDevice> delete ){
		Map<String,String> message = new HashMap<String,String>();
		try {
			//执行 修改按钮操作
			Message m=new Message(printDevicegroupService.doSave(bean, insert, edit, delete,getLoginUser().getUsername()),"i18n.print.i18n_print");
			//将提示信息放在Message中
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印机组操作", "保存数据结束");
		} catch (Exception e) {
			message.put("message", e.getMessage());//若出现异常，则弹出异常消息
			userOperLog.addError(CommonUtil.MODULE_PRINT, "打印机组操作", "保存数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//放在result 在前台页面给出提示
		setResult("result",message);
		return AJAX;
	}
	
	
	/**
	 * 增加
	 * @param bean,insert,edit,delete
	 * @return
	 */
	public String addPrintDevicegroup(@Param PrintDevicegroup bean ,
			@Param("ckx_device_insert") ArrayList<PrintDevice> insert){
			Map<String,String> message = new HashMap<String,String>();
			try {
				Message m=new Message(printDevicegroupService.doAdd(bean, insert,getLoginUser().getUsername()),"i18n.print.i18n_print");
				//将提示信息放在Message中
				message.put("message", m.getMessage());
				userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印机组操作", "新增数据结束");
			} catch (Exception e) {
				message.put("message", e.getMessage());
				userOperLog.addError(CommonUtil.MODULE_PRINT, "打印机组操作", "新增数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			}
			//放在result 在前台页面给出提示
			setResult("result",message);
			return AJAX;
	}
	
	/**
	 * 失效按钮
	 * @param bean
	 * @return
	 */
	public String disable(@Param PrintDevicegroup bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			String spcodes = this.getParam("spcodes");
			String active = this.getParam("active");
			bean.setUsercenter(this.getParam("usercenter"));
			List<PrintDevice> plist = printDeviceService.list(spcodes);
			if(0!=plist.size()){
				//将提示信息放在Message中
				message.put("message", "有子数据，无法失效!");
			}else if("0".equals(active)){
				//将提示信息放在Message中
				message.put("message", "已是无效状态");
			}
			else{
				Message m=new Message(printDevicegroupService.doDelete(bean),"i18n.print.i18n_print");
				//将提示信息放在Message中
				message.put("message", m.getMessage());
				userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印机组操作", "失效数据结束");
			}
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_PRINT, "打印机组操作", "失效数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//放在result 在前台页面给出提示
		setResult("result",message);
		return AJAX;
	}
	
	/**
	 * 有效按钮
	 * @param bean
	 * @return
	 */
	public String nodisable(@Param PrintDevicegroup bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			String active = this.getParam("active");
			String spcodes = this.getParam("spcodes");
			bean.setUsercenter(this.getParam("usercenter"));
			if("0".equals(active)){
				bean.setSpcodes(spcodes);
				Message m=new Message(printDevicegroupService.doUpdate1(bean),"i18n.print.i18n_print");
				//将提示信息放在Message中
				message.put("message", m.getMessage());
				userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印机组操作", "有效数据结束");
			}else{
				//将提示信息放在Message中
				message.put("message", "已是有效状态");
			}
		}catch (Exception e) {
			message.put("message",e.getMessage() );
			userOperLog.addError(CommonUtil.MODULE_PRINT, "打印机组操作", "有效数据结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		//放在result 在前台页面给出提示
		setResult("result",message);
		return AJAX;
	}
	
	
	
	
	
	/**
	 * 主页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param PrintDevicegroup bean) {
		//获取到用户中心
		setResult("loginUsercenter",getLoginUser().getUsercenter());
		return  "select";
	}

	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param PrintDevicegroup bean,@Param PrintDevice printdevice) {
		if(null != printdevice.getSpcode()&&!"".equals(printdevice.getSpcode())){
			PrintDevice pd=printDeviceService.get(printdevice);
			if(null == pd){
				return AJAX;
			}else{
				bean.setSpcodes(pd.getSpcodes());
				setResult("result", printDevicegroupService.selectS(bean));
				userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印机编号来查询打印机组", "查询数据结束");
				return AJAX;
			}
		}
		setResult("result", printDevicegroupService.select(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印机组操作", "查询数据结束");
		return AJAX;
	}
	
	/**
	 * 校验打印机编号
	 * @param bean
	 */
	public String jyreplacespcode(){
		boolean flag = false;
		String replacespcode = this.getParam("replacespcode");
		String usercenter = getLoginUser().getUsercenter();
		List<PrintDevice> rList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPD",usercenter);
		if(null!=replacespcode){
			for (PrintDevice printDevice : rList) {
				if(replacespcode.equals(printDevice.getSpcode())){
					flag = true;
					break;
				}
			}
		}
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限校验打印机编号操作", "校验打印机编号结束");
		setResult("result",flag);
		return AJAX;
	}
	
	
	/**
	 * 通用查询
	 * @param bean
	 */
	public String list(@Param PrintDevicegroup bean) {
		setResult("result", printDevicegroupService.list(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印机组操作", "查询数据结束");
		return AJAX;
	}
	
	/**
	 * 单记录查询
	 * @param bean
	 */
	public String get(@Param PrintDevicegroup bean) {
		setResult("result", printDevicegroupService.get(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印机组操作", "查询其中一条打印机组记录结束");
		return AJAX;
	}
	
	/**
	 * 数据删除
	 * @return
	 */
	public String remove(@Param PrintDevicegroup bean) {
		setResult("result", printDevicegroupService.doDelete(bean));
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印机组操作", "删除数据结束");
		return AJAX;
	}
	
}
