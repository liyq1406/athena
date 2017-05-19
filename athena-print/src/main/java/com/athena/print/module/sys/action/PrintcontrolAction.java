package com.athena.print.module.sys.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.print.entity.sys.Cangk;
import com.athena.print.entity.sys.Ckbatctrl;
import com.athena.print.entity.sys.Plttrscodej;
import com.athena.print.entity.sys.PrintControl;
import com.athena.print.entity.sys.PrintStrogdict;
import com.athena.print.module.sys.service.PrintcontrolService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 用户中心
 * @author denggq
 * @Date 2012-3-19
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class PrintcontrolAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private PrintcontrolService printcontrolService;
	
	@Inject
	private AbstractIBatisDao baseDao;
	
	/**
	  * 获取用户信息
	  * @author denggq
	  * @Date 2012-3-19
	  * @return LoginUser
	  */
	public LoginUser  getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转页面
	 * @author denggq
	 * @Date 2012-3-19
	 * @return String
	 */
	
	
	public String execute() {
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	
	/**
	 * 分页查询方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String query(@Param PrintControl bean) {
		try{
			setResult("result", printcontrolService.select(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "打印单据控制", "数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "打印单据控制", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	
	/**
	 * 多记录查询方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String list(@Param PrintControl bean) {
		try{
			setResult("result", printcontrolService.list(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "打印单据控制", "多数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "打印单据控制", "多数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}

	
	/**
	 * 单记录查询方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String get(@Param PrintControl bean) {
		try{
			setResult("result", printcontrolService.get(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "打印单据控制", "单数据查询");
		}catch (Exception e) {
			Map<String,String> message = new HashMap<String,String>();
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "打印单据控制", "单数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * 行编辑保存方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String save(@Param("insert") ArrayList<PrintControl> insert,@Param("edit") ArrayList<PrintControl> edit,@Param("delete") ArrayList<PrintControl> delete) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			Message m = new Message(printcontrolService.save(insert, edit, delete,getLoginUser().getUsername()),"i18n.ckx.xuqjs.i18n_xitcsdy");
			map.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "打印单据控制", "数据保存");
		}catch (Exception e) {
			setResult("success", false);
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "打印单据控制", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", map);
		return AJAX;
	}

	
	/**
	 * 失效方法
	 * @author denggq
	 * @Date 2012-3-19
	 * @param bean
	 * @return String
	 */
	public String remove(@Param PrintControl bean) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			Message m = new Message(printcontrolService.doDelete(bean),"i18n.ckx.xuqjs.i18n_xitcsdy");
			message.put("message", m.getMessage());
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "打印单据控制", "数据失效");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "打印单据控制", "数据失效", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return AJAX;
	}
	
	/**
	 * 校验交易码编号
	 * @param bean
	 */
//	public String jyuserscode(){
//		boolean flag = false;
//		String users = this.getParam("userscode");
//		String usercenter = getLoginUser().getUsercenter();
//		List<Printuser> uList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintuser1",usercenter);
//		if(null!=users){
//			for (Printuser printuser : uList) {
//				if(users.equals(printuser.getUserscode())){
//					flag = true;
//					break;
//				}
//			}
//		}
//		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印权限校验用户编号操作", "校验用户编号结束");
//		setResult("result",flag);
//		return AJAX;
//	}
	
	/**
	 * 校验单据编号
	 * @param bean
	 */
	public String jydanjbh(){
		boolean flag1 = false;
		String danjbh = this.getParam("danjbh");
		String usercenter = getLoginUser().getUsercenter();
		List<PrintStrogdict> dList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryPrintStrogdicts",usercenter);
		if(null!=danjbh){
			for (PrintStrogdict printStrogdict : dList) {
				if(danjbh.equals(printStrogdict.getZidbm())){
					flag1 = true;
					break;
				}
			}
		}
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印单据控制校验单据编号操作", "校验单据编号结束");
		setResult("result",flag1);
		return AJAX;
	}
	
	/**
	 * 校验用仓库编号
	 * @param bean
	 */
	public String jycangkbh(){
		boolean flag2 = false;
		String cangkbh = this.getParam("cangkbh");
		String usercenter = getLoginUser().getUsercenter();
		List<Cangk> cList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).select("sys.queryCangks",usercenter);
		if(null!=cangkbh){
			for (Cangk cangk : cList) {
				if(cangkbh.equals(cangk.getCangkbh())){
					flag2 = true;
					break;
				}
			}
		}
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印单据控制校验仓库编号操作", "校验仓库编号结束");
		setResult("result",flag2);
		return AJAX;
	}
	
	/**
	 * 校验交易码
	 * @param bean
	 */
	public String jyjiaoym(){
		boolean flag = false;
		String jiaoym = this.getParam("jiaoym");
		String usercenter = getLoginUser().getUsercenter();
		if(!"".equals(jiaoym)&&null!=jiaoym){
			if(jiaoym.substring(0, 1).equals("0")){
				Ckbatctrl beans = new Ckbatctrl();
				beans.setBatcode(jiaoym);
				Ckbatctrl ckbatctrl =(Ckbatctrl)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryCk_batctrl",beans);
				if(ckbatctrl!=null){
					flag = true;
				}
			}else{
				//Plttrscodej bean = new Plttrscodej();
				//bean.setTrscode(jiaoym);
				//bean.setUsercenter(usercenter);
				Map map = new HashMap();
				map.put("usercenter", usercenter);
				map.put("trscode", jiaoym);
				Plttrscodej plttrscodej = (Plttrscodej) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryPlt_trscode_j",map);
				if(plttrscodej!=null){
					flag = true;
				}
			}
		}
		userOperLog.addCorrect(CommonUtil.MODULE_PRINT, "打印单据控制校验交易码操作", "校验交易码结束");
		setResult("result",flag);
		return AJAX;
	}
	
}
