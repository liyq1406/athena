package com.athena.ckx.module.carry.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.carry.CkxCangkxhsj;
import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.module.carry.service.CkxCangkxhsjService;
import com.athena.ckx.util.xls.XlsHandlerUtilsckxhsj;
import com.athena.ckx.util.xls.XlsHandlerUtilsgyxhd;
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
 * 仓库循环时间
 * @author kong
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class CkxCangkxhsjAction extends ActionSupport{
	
	@Inject
	private CkxCangkxhsjService ckxCangkxhsjService;
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
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
	public String execute(@Param CkxCangkxhsj bean) {
		setResult("loginUser", getLoginUser());
		return  "select";
	}
	
	/**
	 * 分页查询
	 * @param bean
	 * @return
	 */
	public String query(@Param CkxCangkxhsj bean) {
		try{
			setResult("result", ckxCangkxhsjService.select(bean,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库循环时间", "数据查询结束 ");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库循环时间", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		return AJAX;
	}
	
	/**
	 * 单对象获取
	 * @param bean
	 * @return
	 */
	public String get(@Param CkxCangkxhsj bean){
		setResult("result", ckxCangkxhsjService.get(bean));
		return AJAX;
	}
	
	/**
	 * 添加数据
	 * @param bean
	 * @return
	 */
	public String addCkxCangkxhsj(@Param CkxCangkxhsj bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message",ckxCangkxhsjService.addCkxCangkxhsj(bean,getLoginUser()) );
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库循环时间", "数据添加结束");
		} catch (Exception e) {
			setResult("success",false );
			map.put("message",e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX,  "仓库循环时间", "数据添加结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 数据保存（修改）
	 * @param bean
	 */
	public String save(@Param CkxCangkxhsj bean, @Param("operant") Integer operant) {
		Map<String,String> map = new HashMap<String,String>();
		bean.setCangkbh(bean.getCangkbh());
		bean.setFenpqhck(bean.getFenpqhck());
		try {
			map.put("message",ckxCangkxhsjService.save(bean,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库循环时间", "数据修改结束");
		} catch (Exception e) {
			setResult("success",false );
			map.put("message",e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX,  "仓库循环时间", "数据修改结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 物理删除
	 * @return
	 */
	public String remove(@Param CkxCangkxhsj bean) {
		Map<String,String> map = new HashMap<String,String>();
		try {
			ckxCangkxhsjService.doDelete(bean);
			map.put("message","删除成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库循环时间", "数据删除结束");
		} catch (ServiceException e) {
			map.put("message",e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库循环时间", "数据删除结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	/**
	 * 逻辑删除
	 * @param bean
	 * @return
	 */
	public String deleteLogic(@Param CkxCangkxhsj bean){
		Map<String,String> map = new HashMap<String,String>();
		try {
			map.put("message", ckxCangkxhsjService.deleteLogic(bean,getLoginUser()));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库循环时间", "数据删除结束");
		} catch (RuntimeException e) {
			map.put("message",e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库循环时间", "数据删除结束", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",map );
		return AJAX;
	}
	
	
	/**
	 * 查询带子仓库的仓库编号和分配区编号,用户添加仓库循环时间的时候下拉框展示
	 * 如果仓库不带子仓库，则排除
	 * @return 6位仓库编码（带子仓库编号）和分配区编号
	 */
	public String listCangkXunh(@Param CkxCangkxhsj bean){
		setResult("result", ckxCangkxhsjService.listCangkXunh(bean));
		return AJAX;
	}
	/**
	 * 查询带子仓库的仓库编号
	 * @param bean
	 * @return 6位仓库编码（带子仓库编号）
	 */
	public String listCangk(@Param CkxCangkxhsj bean){
		setResult("result", ckxCangkxhsjService.listCangk(bean));
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
			downloadServices.downloadFile("cangkxhsjMob.ftl", dataSurce, response, "仓库循环时间模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库循环时间", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库循环时间", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库循环时间", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
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
	public String upload(@Param CkxGongyxhd bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			//查询出仓库的集合
			Map<String,String> mapcangk = ckxCangkxhsjService.getCangkMap();
			//查询出仓库子仓库的集合
			Map<String,String> mapcangkzick = ckxCangkxhsjService.getCkzickMap();
			//查询出分配区的集合
			Map<String,String> mapfenpq = ckxCangkxhsjService.getfenpqMap();
			// 将创建人,修改人 编辑 为当前登录人
			String mes = XlsHandlerUtilsckxhsj.analyzeXls(mapcangk,mapcangkzick,mapfenpq,request,getLoginUser().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "仓库循环时间", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "仓库循环时间", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		
		setRequestAttribute("uploudmessage",message.get("message"));
		return "upload";
	}
	
}