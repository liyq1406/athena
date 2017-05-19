package com.athena.xqjs.module.kdorder.action;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.kdorder.service.ExpAqkcService;
import com.athena.xqjs.module.kdorder.service.ImpAqkcService;
import com.athena.xqjs.module.maoxq.action.MaoxqAction;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ImpAqkcAction
 * <p>
 * 类描述：订单修改及生效service
 * </p>
 * 创建人：NIESY
 * <p>
 * 创建时间：2012-03-21
 * </p>
 * 
 * @version 1.0
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ImpAqkcAction extends ActionSupport {
	
	@Inject
	private ImpAqkcService impAqkcService;

	@Inject
	private ExpAqkcService expAqkcService;

	// @Inject
	// private PostService postService;

	private final Log log = LogFactory.getLog(MaoxqAction.class);
	// 获取用户信息
	public LoginUser getUserInfo() {

		return com.athena.authority.util.AuthorityUtils.getSecurityUser();
	}

	/**
	 * 初始化导入安全库存
	 * 
	 * @return
	 */
	public String impKdAqkcInit() {
		setResult("whr", this.getUserInfo().getUsername());
		setResult("usercenter", this.getUserInfo().getUsercenter());
		//0006908: KD导入订货安全库存 POA查询所有数据。 
		if(this.getUserInfo().getJihyz()!=null&&!this.getUserInfo().getJihyz().equals("POA")){
			setResult("jihyz", this.getUserInfo().getJihyz());
		}
		return "success";
	}

	/**
	 * <p>
	 * 导入安全库存查询
	 * </p>
	 * 
	 * @param bean
	 * @return
	 */
	public String queryKdAqkc(@Param Lingjck bean) {
		setResult("result", impAqkcService.select(bean, getParams()));
		return AJAX;
	}

	/**
	 * <p>
	 * 修改安全库存
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	public String updateAqkc(@Param("edit") ArrayList<Lingjck> ls) {
		String newEditor = getUserInfo().getUsername();
		String time = CommonFun.getJavaTime();
		String result = "零件仓库" + MessageConst.UPDATE_COUNT_0;
		boolean flag = false;
		try {
			flag = impAqkcService.updateKc(ls, newEditor, time);
		} catch (Exception e) {
			log.debug(result, e);
		}
		if (flag) {
			result = "修改成功！";
		}
		setResult("result", result);
		return AJAX;
	}

	/**
	 * <p>
	 * 导入订货安全库存
	 * </p>
	 * <p>
	 * 修改安全库存
	 * </p>
	 * 
	 * @return
	 */
	public String impUpdateKc() {
		HttpServletRequest req = ActionContext.getActionContext().getRequest();
		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		String fullFilePath = "";
		PrintWriter out = null;
		String saveLj = System.getProperty("java.io.tmpdir");
		RequestContext requestContext = new ServletRequestContext(req);
		if (FileUpload.isMultipartContent(requestContext)) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax(4000000);
				List<FileItem> fileItems;

				fileItems = upload.parseRequest(req);

				for (FileItem item : fileItems) {
					if (item.isFormField()) {
						String value = item.getString();
						log.info(item.getFieldName() + ":" + value);
					} else {
						String fileName = item.getName();
						if (fileName != null) {
							if (fileName.lastIndexOf(File.separator) != -1) {
								fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1, fileName.length());
							}
							fullFilePath = saveLj + File.separator + fileName;
							File fullFile = new File(saveLj);
							if (!fullFile.exists()) {
								fullFile.mkdirs();
							}
							File fileOnServer = new File(fullFilePath);

							item.write(fileOnServer);

						}
					}
				}
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}
		try {
			log.info(fullFilePath);
			out = reqResponse.getWriter();
			reqResponse.setContentType("text/html");
			reqResponse.setCharacterEncoding("UTF-8");
			List<Lingjck> ls = impAqkcService.importExc(fullFilePath);
			String result = checkData(ls);
			if(StringUtils.isBlank(result))
			{
				boolean  impResult = impAqkcService.updateKc(ls, getUserInfo().getUsername(), CommonFun.getJavaTime());
				if (impResult) {
					result = "导入文件成功！(相关错误信息将写入异常报警表)";
				}
			}
			String msg = "<script>parent.callback(\"" + result + "\")</script>";
			log.info(msg);
			out.print(msg);
			out.flush();
		} catch (Exception e) {
			log.error(e.getMessage());
			out.println("<script>parent.callback('导入文件出错！ ')</script>");
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}

		return AJAX;
	}

	private String checkData(List<Lingjck> ls) 
	{
		StringBuffer sb = null;
		boolean error = false;
		int i = 3;
		for (Lingjck lingjck : ls) 
		{
			sb = new StringBuffer();
			sb.append("第" + i + "行");
			if(StringUtils.isBlank(lingjck.getUsercenter()) 
					|| StringUtils.isBlank(lingjck.getLingjbh())
					|| StringUtils.isBlank(lingjck.getCangkbh())
					|| StringUtils.isBlank(lingjck.getZiyhqrq())
			)
			{
					sb.append("用户中心,零件号,订货仓库,资源的截止日期不能为空!");
					error = true;
			}
			else
			{
					if(StringUtils.isBlank(lingjck.getJistzzStr()))
					{
						lingjck.setJistzz(BigDecimal.ZERO);
					}
					else
					{
						try{
							double jistzz = Double.valueOf(lingjck.getJistzzStr());
							if(!lingjck.getJistzzStr().matches("^-?[0-9]{1,7}(?:\\.[0-9]{1,3})?$|^0\\.[0-9]{1,3}$"))
							{
								error = true;
								sb.append("计算调整值格式有误,该值的取值范围为(-9999999.999  ~~ 9999999.999)!");
							}
							else
								lingjck.setJistzz(new BigDecimal(jistzz));
						}
						catch (Exception e) {
							error = true;
							sb.append("计算调整值格式有误,该值的取值范围为(-9999999.999  ~~ 9999999.999)!");
						}
					}
					if(StringUtils.isBlank(lingjck.getDingdljStr()))
					{
						lingjck.setDingdlj(BigDecimal.ZERO);
					}
					else
					{
						try{
							double dingdlj = Double.valueOf(lingjck.getDingdljStr());
							if( !lingjck.getDingdljStr().matches("^[0-9]{1,10}(?:\\.[0-9]{1,3})?$|^0\\.[0-9]{1,3}$"))
							{
								error = true;
								sb.append("订单累计格式有误,该值的取值范围为(0  ~~ 9999999999.999)!");
							}
							else
								lingjck.setDingdlj(new BigDecimal(dingdlj));
						}
						catch (Exception e) {
							error = true;
							sb.append("订单累计格式有误,该值的取值范围为(0  ~~ 9999999999.999)!");
						}
					}
					if(StringUtils.isBlank(lingjck.getJiaofljStr()))
					{
						lingjck.setJiaoflj(BigDecimal.ZERO);
					}
					else
					{
						try{
							double jiaoflj = Double.valueOf(lingjck.getJiaofljStr());
							if(!lingjck.getJiaofljStr().matches("^[0-9]{1,10}(?:\\.[0-9]{1,3})?$|^0\\.[0-9]{1,3}$"))
							{
								error = true;
								sb.append("交付累计格式有误,该值的取值范围为(0  ~~ 9999999999.999)!");
							}
							else
								lingjck.setJiaoflj(new BigDecimal(jiaoflj));
						}
						catch (Exception e) {
							error = true;
							sb.append("交付累计格式有误,该值的取值范围为(0  ~~ 9999999999.999)!");
						}
					}
					if(StringUtils.isBlank(lingjck.getZhongzljStr()))
					{
						lingjck.setZhongzlj(BigDecimal.ZERO);
					}
					else
					{
						try{
							double zhongzlj = Double.valueOf(lingjck.getZhongzljStr());
							if( !lingjck.getZhongzljStr().matches("^[0-9]{1,10}(?:\\.[0-9]{1,3})?$|^0\\.[0-9]{1,3}$"))
							{
								error = true;
								sb.append("终止累计格式有误,该值的取值范围为(0  ~~ 9999999999.999)!");
							}
							else
								lingjck.setZhongzlj(new BigDecimal(zhongzlj));
						}
						catch (Exception e) {
							error = true;
							sb.append("终止累计格式有误,该值的取值范围为(0  ~~ 9999999999.999)!");
						}
					}
					if(lingjck.getZiyhqrq().equalsIgnoreCase("error"))
					{
						error = true;
						sb.append("资源的截止日期格式有误,格式规范为yyyy/mm/dd!");
					}
			}
			i ++;
			if(error) break;
		}
		
		return error ? sb.toString() :  "";
	}

	/**
	 * <p>
	 * 导出订货安全库存
	 * </p>
	 * 
	 * @return
	 */
	public String readyExpExc(@Param Lingjck bean) {
		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		List<Map<String, String>> list = impAqkcService.select(getParams());
		Map<String, String> message = new HashMap<String, String>();
		String result = "0";
		String info = "";
		if ( (list==null) || (list.size()==0) ) {
			result="1";
			info="资源快照表无"+getParams().get("ziyhqrq")+"数据!";
		} 
		
		message.put("status", result);
		message.put("message", info);
		setResult("result", message);
		return AJAX;
	}
	
	/**
	 * <p>
	 * 导出订货安全库存
	 * </p>
	 * 
	 * @return
	 */
	public String expExc(@Param Lingjck bean) {
		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		List<Map<String, String>> list = impAqkcService.select(getParams());
		Map<String, String> message = new HashMap<String, String>();
		boolean flag = expAqkcService.exportExcel(reqResponse, list);
		if (!flag) {
			String result = "导出文件出错！";
			message.put("message", result);
			setResult("result", message);
		}
		return AJAX;
	}
	
	
}
