package com.athena.ckx.module.xuqjs.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.DFPVLingjqh;
import com.athena.ckx.entity.xuqjs.DFPVLingjqhLCDV;
import com.athena.ckx.module.xuqjs.service.DFPVLingjqhService;
import com.athena.ckx.util.GetPostOnly;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;


/**
 * 零件切换Action
 * @author CSY
 * @date 2016-05-04
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DfpvlingjqhAction extends ActionSupport{
	
	@Inject
	private DFPVLingjqhService DFPVlingjqhService;
	
	@Inject
	private UserOperLog userOperLog;
	
	protected static Logger logger = Logger.getLogger(DfpvlingjqhAction.class);
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
	 * 分页查询
	 * @author CSY
	 * @Date 2016-05-04
	 * @return String
	 * */
	public String queryLingjqh(@Param DFPVLingjqh bean){
		try {
			setResult("result", DFPVlingjqhService.queryLingjqh(bean));
			logger.info("零件切换-查询成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件切换", "数据查询-成功");
		} catch (Exception e) {
			setResult("success",false );
			logger.error("零件切换-查询异常：" + e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件切换", "数据查询-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 分页查询
	 * @author CSY
	 * @Date 2016-05-04
	 * @return String
	 * */
	public String queryLingjqhLCDV(@Param DFPVLingjqhLCDV bean){
		try {
			setResult("result", DFPVlingjqhService.queryLingjqhLCDV(bean));
			logger.info("零件切换-查询LCDV成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件切换", "LCDV数据查询-成功");
		} catch (Exception e) {
			setResult("success",false );
			logger.error("零件切换-查询LCDV异常：" + e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件切换", "LCDV数据查询-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 查询最近十次版本号
	 * @author CSY
	 * @Date 2016-05-04
	 * @return String
	 * */
	public String queryLingjqhBBH(@Param DFPVLingjqh bean){
		try {
			setResult("result", DFPVlingjqhService.queryLingjqhBBH(bean));
			logger.info("零件切换-最近十次版本号查询成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件切换", "查询最近十次版本号-成功");
		} catch (Exception e) {
			setResult("success",false );
			logger.error("零件切换-最近十次版本号查询异常：" + e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件切换", "查询最近十次版本号-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	public String execute() {
		Map<String,String> map = getLoginUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map) ;
		setResult("role", key);//角色（更新保存时用）
		return "select";
	}
	
	/**
	 * 失效方法
	 * @author CSY
	 * @Date 2016-05-11
	 * @param bean
	 * @return String
	 */
	public String shixiao(@Param("list") ArrayList<DFPVLingjqh> updateDFPVLingjqh) {
		try {
			DFPVlingjqhService.shixiao(updateDFPVLingjqh,getLoginUser().getUsername());
			logger.info("零件切换-失效成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件切换", "数据失效-成功");
		} catch (Exception e) {
			setResult("success",false );
			logger.error("零件切换-失效异常：" + e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件切换", "数据失效-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 生效方法
	 * @author CSY
	 * @Date 2016-05-12
	 * @param bean
	 * @return String
	 */
	public String shengx(@Param("list") ArrayList<DFPVLingjqh> updateDFPVLingjqh) {
		try {
			DFPVlingjqhService.shengx(updateDFPVLingjqh,getLoginUser().getUsername());
			logger.info("零件切换-生效成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件切换", "数据生效-成功");
		} catch (Exception e) {
			setResult("success",false );
			logger.error("零件切换-生效异常：" + e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件切换", "数据生效-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 模板导出（按车型）
	 * 
	 */
	public String downloadMob() {

		Map<String,String> message = new HashMap<String,String>();
		try{
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			String fileName = "lingjqh_lcdv.xls";
			this.downLoadFile(response, fileName, "xls");
			logger.info("零件切换-车型模板导出成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件切换", "模板（按车型）-导出成功");
		}catch (Exception e) {
			message.put("message", e.getMessage());
			setResult("success",false );
			logger.error("零件切换-车型模板导出异常：" + e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件切换", "模板（按车型）-导出异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return "downLoad";
	}
	
	/**
	 * 模板导出（按流水号）
	 * 
	 */
	public String downloadLSMob() {

		Map<String,String> message = new HashMap<String,String>();
		try{
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			String fileName = "lingjqh_liush.xls";
			this.downLoadFile(response, fileName, "xls");
			logger.info("零件切换-流水号模板导出成功");
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件切换", "模板（按流水号）-导出成功");
		}catch (Exception e) {
			message.put("message", e.getMessage());
			setResult("success",false );
			logger.error("零件切换-流水号模板导出异常：" + e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件切换", "模板（按流水号）-导出异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return "downLoad";
	}
	
	/**
	 * 导入数据
	 * @author denggq
	 * @Date 2018-4-18
	 * @Param bean
	 * @return String
	 * */
	public String upload(){
		synchronized (DfpvlingjqhAction.class){
			HttpServletRequest request = ActionContext.getActionContext().getRequest();
			String msg = "";
			try {
				msg = DFPVlingjqhService.upload(request,getLoginUser().getUsername());
				logger.info("零件切换-导入成功");
				userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件切换", "导入-成功");
			} catch (Exception e) {
				msg = "导入有误";
				setResult("success",false );
				logger.error("零件切换-导入异常：" + e.getMessage());
				userOperLog.addError(CommonUtil.MODULE_CKX, "零件切换", "导入-异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			} 
			request.setAttribute("mess", msg);
		}
		return "upload";
	}
	
	//导出模板
	public void downLoadFile(HttpServletResponse response,
			String fileName, String fileType) throws Exception {
		
		//String filePath = this.getClass().getClassLoader().getResource("").getPath().substring(1);

		/*filePath += "template/excel/" + fileName;
		logger.info("读取文件的路径为：" + filePath);*/
		
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/excel/" + fileName);
		
		//File file = new File(filePath); // 根据文件路径获得File文件
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		// 文件名
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ new String(fileName.getBytes(), "ISO-8859-1") + "\"");
		//response.setContentLength((int) file.length());
		byte[] buffer = new byte[4096];// 缓冲区
		BufferedOutputStream output = null;
		BufferedInputStream input = null;
		try {
			output = new BufferedOutputStream(response.getOutputStream());
			//input = new BufferedInputStream(new FileInputStream(file));
			input = new BufferedInputStream(in);
			//input = new BufferedInputStream(in);
			int n = -1;
			// 遍历，开始下载
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
			output.flush();
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if (input != null)
				input.close();
			if (output != null)
				output.close();
			if (in != null) {
				in.close();
			}
		}
	}
	
}
