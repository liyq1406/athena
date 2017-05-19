package com.athena.xqjs.module.maoxq.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.code.EncodeURIComponent;
import com.athena.util.exception.ActionException;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.anxorder.AnxMaoxq;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.maoxq.CompareCyc;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.maoxq.service.MaoxqCompareSendService;
import com.athena.xqjs.module.maoxq.service.MaoxqCompareService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：MaoxqAction
 * <p>
 * 类描述： 毛需求
 * </p>
 * 创建人：Niesy
 * <p>
 * 创建时间：2012-02-07
 * </p>
 * 
 * @version
 * 
 */

@Component
public class MaoxqAction extends ActionSupport {

	@Inject
	private MaoxqCompareService maoxqCompareService;
	@Inject
	private DownLoadServices downloadServices = null; 
	
	@Inject
	private JisclcsszService jiscclssz;
	
	//xss-v4
	@Inject
	private MaoxqCompareSendService maoxqCompareSendService;
	
	private final Log log = LogFactory.getLog(MaoxqAction.class);
	@Inject
	private UserOperLog userOperLog;

	// 获取用户信息
	public LoginUser getUserInfo() {

		return com.athena.authority.util.AuthorityUtils.getSecurityUser();
	}

	/**
	 * 页面初始化，执行跳转
	 */
	public String execute() {
		return "success";
	}
	
	/**
	 * 页面初始化，执行跳转 
	 * 0007182: 增加按需毛需求查询界面  按需 毛需求主页面初始化 
	 */
	public String executeAn_x() {
		setResult("usercenter",getUserInfo().getUsercenter());
		return "success";
	}

	/**
	 * 0007182: 增加按需毛需求查询界面  按需 毛需求查询
	 */
	public String queryMaoxqAnx(@Param AnxMaoxq bean) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需毛需求查询", "按需毛需求查询开始");
		try {
			setResult("result", maoxqCompareService.selectAnxMxq(getParams(), bean));
		} catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			setResult("success", false);
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需毛需求查询", "按需毛需求查询结束");
		return AJAX;
	}
	/**
	 * 毛需求主页面查询
	 */
	public String queryMaoxq(@Param Maoxq bean) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "毛需求查询与比较", "毛需求查询开始");
		setResult("result", maoxqCompareService.selectMxq(getParams(), bean));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "毛需求查询与比较", "毛需求查询结束");
		return AJAX;
	}

	/**
	 * 毛需求明细 新增
	 * 
	 * @param bean
	 * @return
	 */
	public String initAdd(@Param Maoxq bean) {
		Map<String, String> map = getParams();
		map.put("xuqly", bean.getXuqbc().substring(0, 3));
		setResult("xuqbc", bean.getXuqbc());
		setResult("mode", getParam("mode"));
		if (null != getParam("usercenter") && !"".equals(getParam("usercenter"))) {
			setResult("usercenter", getParam("usercenter"));
		} else {
			//setResult("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());
		}
		setResult("lingjbh", getParam("lingjbh"));
		setResult("zhizlx", getParam("zhizlx"));
		setResult("shiycj", getParam("shiycj"));
		setResult("chanx", getParam("chanx"));
		setResult("danw", getParam("danw"));
		setResult("lingjmc", getParam("lingjmc"));
		setResult("jihyz", getParam("jihyz"));
		setResult("removeId", getParam("removeId"));
		return "add";
	}

	/**
	 * 毛需求主页面修改备注
	 */
	public String updateBeiz(@Param("edit") ArrayList<Maoxq> ls) {
		String result = "";
		boolean flag = false;
		try {
			flag = maoxqCompareService.updateComment(ls, this.getUserInfo().getUsername(), CommonFun.getJavaTime());

		} catch (Exception e) {
			// System.out.println(e.toString());
			log.info(e.getCause().getCause().getMessage());
		}
		if (flag) {
			result = "修改成功！";
			setResult("result", result);
		} else {
			result = "修改失败！";
			setResult("result", result);
		}
		return AJAX;
	}

	/**
	 * 毛需求主页面删除
	 */
	public String deleteMaoxq(@Param("edit") ArrayList<CompareCyc> ls) {
		String result = "";
		boolean flag = false;
		try {
			flag = maoxqCompareService.delete(ls, this.getUserInfo().getUsername(), CommonFun.getJavaTime());

		} catch (Exception e) {
			// System.out.println(e.toString());
			log.info(e.getMessage());
		}
		if (flag) {
			result = "删除成功！";
		} else {
			result = MessageConst.DELETE_COUNT_0;
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "毛需求查询", "毛需求主页面删除");
		setResult("result", result);
		return AJAX;
	}

	public String showDate() {
		String kString = "";
		if ("Days".equals(getParam("xuqlx"))) {
			kString = maoxqCompareService.getDays(getParam("xuqbc"), 9,this.getParams()).toString();
		} else {
			String minYear = maoxqCompareService.startYearRq(getParam("xuqbc"));
			kString = minYear != null ? minYear.substring(0, 4) + "年" + minYear.substring(5, 7) + "月"
					+ minYear.substring(8, 10) + "日" : "";

		}
		setResult("result", kString);
		return AJAX;
	}

	/**
	 * 毛需求明细 查询 页面初始化
	 */
	public String initCwD(@Param CompareCyc bean) {
		String mode = maoxqCompareService.storageMode(bean);
		String kString;
		// 取的改版次的开始年份
		if (bean.getXuqlx().equalsIgnoreCase("Days")) {
			// 日滚动
			kString = maoxqCompareService.getDays(bean.getXuqbc(), 9,this.getParams()).toString();
		} else {
			String minYear = maoxqCompareService.startYearRq(bean.getXuqbc());
			kString = minYear != null ? minYear.substring(0, 4) + "年" + minYear.substring(5, 7) + "月"
					+ minYear.substring(8, 10) + "日" : "";
		}

		// 显示方式
		String xsfs = bean.getXsfs();
		if (xsfs == null) {
			xsfs = "1";
		}
		// 按用户中心显示或产线显示
		/*
		 * if (mode.equalsIgnoreCase("usercenter")) {
		 * 
		 * forward = "usercenter" + bean.getXuqlx();
		 * 
		 * } else if (mode.equalsIgnoreCase("chanx") &&
		 * xsfs.equalsIgnoreCase("1")) {
		 * 
		 * forward = "chanx" + bean.getXuqlx();
		 * 
		 * } else if (mode.equalsIgnoreCase("chanx") &&
		 * xsfs.equalsIgnoreCase("0")) {
		 * 
		 * forward = "usercenter" + bean.getXuqlx(); } else if (mode.isEmpty())
		 * { forward = "usercenter" + bean.getXuqlx(); }
		 */
		setResult("removeId", getParam("removeId"));
		setResult("mode", mode);
		setResult("xuqbc", bean.getXuqbc());
		setResult("xsfs", xsfs);
		setResult("usercenter", bean.getUsercenter());
		setResult("lingjbh", bean.getLingjbh());
		setResult("zhizlx", bean.getXuqbc().indexOf("DKS") != -1 ? Const.ZHIZAOLUXIAN_KD_AIXIN : bean.getZhizlx());
		setResult("xuqlx", bean.getXuqlx());
		setResult("kaisrq", kString);
		setResult("shengxbz", getParam("shengxbz"));
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("usercenter", loginUser.getUsercenter());
		return "chanx" + bean.getXuqlx();
	}

	/**
	 * <p>
	 * 周期、周、日滚动毛需求查询
	 * </p>
	 * <p>
	 * 按产线汇总或按用户中心汇总
	 * </p>
	 * 
	 * @author NIESY
	 * @date 2012-03-07
	 * @param bean
	 * @param mode
	 * @return
	 */
	public String queryMxP(@Param CompareCyc bean, @Param("mode") String mode) {
		// 查询毛需求明细
		if (!mode.isEmpty()) {
			try {
				Map<String, Object> map = maoxqCompareService.query(mode, bean, false);
				setResult("result", map);
			} catch (NullCalendarCenterException e) {
				setErrorMessage(e.getMessage());
			}

		}else{
			throw new ServiceException("查询范围内需求为空！");
		}
		return AJAX;
	}

	/**
	 * <p>
	 * 毛需求明细 新增
	 * </p>
	 * 
	 * @author NIESY
	 * 
	 * @param ls
	 * @return
	 */
	public String addMx(@Param("insert") ArrayList<CompareCyc> ls) {
		String creator = getUserInfo().getUsername();
		Maoxq bean = new Maoxq();
		bean.setXuqbc(ls.get(0).getXuqbc());
		if (maoxqCompareService.shifSx(bean)) {
			setResult("result", "不能对生效的毛需求进行新增操作.");
			return AJAX;
		}
		setResult("result", maoxqCompareService.insMxqmx(ls, creator, CommonFun.getJavaTime()));
		return AJAX;
	}

	/**
	 * <p>
	 * 毛需求明细 修改 初始化
	 * </p>
	 * 
	 * @author NIESY
	 * 
	 * @param bean
	 * @return
	 */
	public String editPage(@Param CompareCyc bean) {
		setResult("xuqbc", bean.getXuqbc());
		setResult("usercenter", bean.getUsercenter());
		setResult("lingjbh", bean.getLingjbh());
		setResult("zhizlx", bean.getZhizlx());
		setResult("chanx", bean.getChanx());
		setResult("shiycj", bean.getShiycj());
		setResult("removeId", getParam("removeId"));
		return "editPage";
	}

	/**
	 * <p>
	 * 毛需求明细 修改 查询
	 * </p>
	 * 
	 * @author NIESY
	 * 
	 * @param ls
	 * @return
	 */
	public String editQr(@Param CompareCyc bean) {
		setResult("result", maoxqCompareService.editSlQuery(bean));
		return AJAX;
	}

	/**
	 * <p>
	 * 毛需求明细 修改
	 * </p>
	 * 
	 * @author NIESY
	 * 
	 * @param ls
	 * @return
	 */
	public String editMx(@Param("mxsl") ArrayList<CompareCyc> ls) {
		String result = "修改失败！";
		Maoxq bean = new Maoxq();
		bean.setXuqbc(ls.get(0).getXuqbc());
		if (maoxqCompareService.shifSx(bean)) {
			setResult("result", "不能对生效的毛需求进行修改操作.");
			return AJAX;
		}
		boolean flag = false;
		try {
			flag = maoxqCompareService.updateSl(ls, getUserInfo().getUsername(), CommonFun.getJavaTime());
		} catch (Exception e) {
		}
		if (flag) {
			result = "修改成功！";
		}
		setResult("result", result);
		return AJAX;
	}

	/**
	 * <p>
	 * 毛需求明细 删除
	 * </p>
	 * 
	 * @author NIESY
	 * 
	 * @param ls
	 * @return
	 */
	public String delectMxP(@Param("delete") ArrayList<CompareCyc> ls) {
		String result = "删除失败！";
		String newEditor = getUserInfo().getUsername();
		String newEdittime = CommonFun.getJavaTime();
		Maoxq bean = new Maoxq();
		bean.setXuqbc(ls.get(0).getXuqbc());
		if (maoxqCompareService.shifSx(bean)) {
			setResult("result", "不能对生效的毛需求进行删除操作.");
			return AJAX;
		}
		boolean flag = false;
		try {
			flag = maoxqCompareService.delMx(ls, newEditor, newEdittime);
		} catch (Exception e) {
		}
		if (flag) {
			result = "删除成功！";
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "毛需求查询", "毛需求明细删除");
		setResult("result", result);
		return AJAX;
	}

	/**
	 * <p>
	 * 毛需求PP/PS比较
	 * </p>
	 * <p>
	 * 按用户中心或产线
	 * </p>
	 * 
	 * @author NIESY
	 * 
	 * @param bean
	 * @return
	 */
	public String comparePage(@Param CompareCyc bean) {
		String forward = "";
		String mode = maoxqCompareService.storageMode(bean);
		if (mode.isEmpty()) {
			CompareCyc mc = new CompareCyc();
			mc.setXuqbc(getParams().get("xuqbc1"));
			mode = maoxqCompareService.storageMode(mc);
		}
		// 取的改版次的开始年份
		String minYear1 = maoxqCompareService.startYearRq(bean.getXuqbc());
		String minYear2 = maoxqCompareService.startYearRq(getParams().get("xuqbc1"));
		String rq = null;
		// 如果两个明细都为空，直接返回
		if (minYear1 == null && minYear2 == null) {
			rq = "";
		} else if (minYear1 == null) {
			rq = minYear2;
		} else if (minYear2 == null) {
			rq = minYear1;
		} else {
			int k = minYear1.compareTo(minYear2);
			rq = k <= 0 ? minYear1 : minYear2;
		}
		String kString = !rq.equalsIgnoreCase("") ? rq.substring(0, 4) + "年" + rq.substring(5, 7) + "月"
				+ rq.substring(8, 10) + "日" : "";
		// 显示方式
		String xsfs = bean.getXsfs();
		if (xsfs == null) {
			xsfs = "1";
		}
		// 按用户中心显示或产线显示
		if (mode.equalsIgnoreCase("usercenter")) {
			xsfs = "0";
			forward = "cmpUsercenter" + bean.getXuqlx();

		} else if (mode.equalsIgnoreCase("chanx") && xsfs.equalsIgnoreCase("1")) {
			xsfs = "1";
			forward = "cmpChanx" + bean.getXuqlx();

		} else if (mode.equalsIgnoreCase("chanx") && xsfs.equalsIgnoreCase("0")) {
			xsfs = "0";
			forward = "cmpUsercenter" + bean.getXuqlx();
		} else if (mode.isEmpty()) {
			forward = "cmpUsercenter" + bean.getXuqlx();
		}
		setResult("removeId", getParam("removeId"));
		setResult("mode", mode);
		setResult("xuqbc", bean.getXuqbc());
		setResult("xuqbc1", getParams().get("xuqbc1"));
		setResult("xsfs", xsfs);
		setResult("usercenter", bean.getUsercenter());
		setResult("lingjbh", bean.getLingjbh());
		setResult("zhizlx", bean.getXuqbc().indexOf("DKS") != -1 ? Const.ZHIZAOLUXIAN_KD_AIXIN : bean.getZhizlx());
		setResult("xuqlx", bean.getXuqlx());
		setResult("kaisrq", kString);

		return forward;
	}

	/**
	 * <p>
	 * 毛需求明细 修改 查询
	 * </p>
	 * 
	 * @author NIESY
	 * 
	 * @param ls
	 * @return
	 */
	public String compareQr(@Param CompareCyc bean) {
		Map<String, String> map_Jis = new HashMap<String, String>();
		map_Jis.put("usercenter", getUserInfo().getUsercenter());// 设置用户中心 
		map_Jis.put("jsrq", CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD));// 计算日期
		map_Jis.put("username", getUserInfo().getUsername());// 设置用户姓名
		
		
		boolean flag = false; 
		flag = maoxqCompareService.maoxqSfbj();
		String result = "";
		if(flag){ 
			result = "当前有其他用户正在比较，请稍等！";
			setResult("result", result); 
			return AJAX;
		} 
		
		if(getUserInfo().getUsercenter()==null && getUserInfo().getUsercenter().equals("") ){
			map_Jis.put("jiscldm", "");  
		}else if (getUserInfo().getUsercenter()!=null && getUserInfo().getUsercenter().equals("UW") ){
			map_Jis.put("jiscldm", Const.MXQBJ_UW);  
		}else if(getUserInfo().getUsercenter()!=null && getUserInfo().getUsercenter().equals("UL") ){
			map_Jis.put("jiscldm", Const.MXQBJ_UL);  
		}else if(getUserInfo().getUsercenter()!=null && getUserInfo().getUsercenter().equals("UX") ){
			map_Jis.put("jiscldm", Const.MXQBJ_UX);  
		}else if (getUserInfo().getUsercenter()!=null && getUserInfo().getUsercenter().equals("VD") ){
			map_Jis.put("jiscldm", Const.MXQBJ_VD);  
		}
		
		//加锁
		map_Jis.put("chulzt", "20"); 
		maoxqCompareService.updateJisState(map_Jis);
		
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("xuqbc", bean.getXuqbc());
		map.put("xuqbc1", getParams().get("xuqbc1"));
		map.put("jihyz", bean.getJihyz());
		map.put("xsfs", bean.getXsfs());
		map.put("xuqlx", bean.getXuqlx());
		map.put("zhizlx", bean.getZhizlx());
		map.put("mode", getParams().get("mode"));
		try {
			if (!getParams().get("mode").isEmpty()) {
				Map<String, Object> maprs = maoxqCompareService.comparePpOrPs(map, bean, false);
				setResult("result", maprs);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
						
		
		//比较完 更新处理状态为0,比较完成，解锁状态
		map_Jis.put("chulzt", "90"); 
		maoxqCompareService.updateJisState(map_Jis);
		 
		
		return AJAX;
	}

	/**
	 * <p>
	 * 导入文件
	 * </p>
	 * 
	 * @author NIESY
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String impExc() {
		log.debug("==============毛需求导入文件开始===============");
		HttpServletRequest req = ActionContext.getActionContext().getRequest();
		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		String fullFilePath = "";
		String beiz = null;
		PrintWriter out = null;
		//取用户的根目录
		String saveLj = System.getProperty("user.home")+File.separator+"tmp"; 
		RequestContext requestContext = new ServletRequestContext(req); 
		if (FileUpload.isMultipartContent(requestContext)) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax(40000000);
				List<FileItem> fileItems;

				fileItems = upload.parseRequest(req);

				for (FileItem item : fileItems) {
					if (item.isFormField()) {
						String value = item.getString();
						if ("beiz".equals(item.getFieldName())) {
							beiz = EncodeURIComponent.decodeURIComponent(value);
						}
						log.info(item.getFieldName() + ":" + beiz);
					} else {    
						File fullFile = new File(saveLj);
						if (!fullFile.exists()) {
							fullFile.mkdirs();
						}
						// 0007258  路径问题 直接改为自定义路径
						fullFilePath = saveLj + File.separator+String.valueOf(new Date().getTime())+".xls";
						log.info("上传文件路径："+fullFilePath);
						File fileOnServer = new File(fullFilePath); 
						item.write(fileOnServer); 
						log.info("上传文件成功,路径："+fullFilePath);
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
			String impResult = maoxqCompareService.insExcelData(fullFilePath, beiz, getUserInfo().getUsername());
			String msg = "<script>parent.callback(\"" + impResult + "\")</script>";
			log.info(msg);
			out.print(msg);
			out.flush();
		} catch (Exception e) {
			log.error("毛需求导入出错==================", e);
			out.println("<script>parent.callback('导入文件出错！ ')</script>");
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}

		return AJAX;
	}

	/**
	 * <p>
	 * 是否指定CMJ及是否指定运输时刻
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	public String ifAppoint(@Param("zd") ArrayList<CompareCyc> ls, @Param("zd") ArrayList<Maoxq> mls) {
		Map<String, String> map = getParams();
		map.put("newEdittime", CommonFun.getJavaTime());
		map.put("newEditor", getUserInfo().getUsername());
		String shifzdyssk = map.get("shifzdyssk");
		String shifjscmj = map.get("shifjscmj");
		// 指定运输时刻标志
		boolean skFlag = shifzdyssk.equalsIgnoreCase("1");
		// 指定CMJ版本标志
		boolean cmjFlag = shifjscmj.equalsIgnoreCase("1");
		// 验证时间
		if ((skFlag && cmjFlag) || skFlag) {
			String from = map.get("zdgyzqfrom");
			String to = map.get("zdgyzqto");
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
			String nowTime = sf.format(date);
			if (nowTime.compareTo(from) > 0 && nowTime.compareTo(to) > 0) {
				setResult("result", "不能小于当前时间！");
				return AJAX;
			} else if (from.compareTo(to) > 0) {
				setResult("result", "起始时间不能大于结束时间！");
				return AJAX;
			}
		}
		String appoint = "";
		if (skFlag && cmjFlag) {
			appoint = "and chanx is null and zhizlx = '" + Const.ZHIZAOLUXIAN_IL + "'";
		} else if (skFlag) {
			appoint = "and chanx is null and zhizlx = '" + Const.ZHIZAOLUXIAN_IL + "'";
		} else if (cmjFlag) {
			appoint = "and chanx is null ";
		}
		map.put("appoint", appoint);
		boolean info = false;
		try {
			info = maoxqCompareService.appointTc(mls, ls, map);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		String result = MessageConst.UPDATE_COUNT_0;
		if (info) {
			result = "指定成功！";
		} else {
			result = "含有产线为空的数据，不能被指定！";
		}
		setResult("result", result);
		return AJAX;
	}

	/**
	 * <p>
	 * 毛需求另存为
	 * </p>
	 * 
	 * @author NIESY
	 * 
	 * @param ls
	 * @return
	 */
	public String saveAs(@Param CompareCyc bean) {
		String result = "另存为失败！";
		String time = CommonFun.getJavaTime();
		bean.setCreate_time(time);
		bean.setCreator(getUserInfo().getUsername());
		bean.setEdit_time(time);
		bean.setEditor(getUserInfo().getUsername());
		boolean flag = false;
		try {
			flag = maoxqCompareService.saveAs(bean, getParams().get("beiz"));

		} catch (ActionException e) {
			log.error(e.getMessage());
		}
		if (flag) {
			result = "另存为成功！";
		}
		setResult("result", result);
		return AJAX;
	}

	public String validateMxqLj() {
		Diaobsqmx dbsqmx = maoxqCompareService.getMaoxqLjlx(getParams());
		if (dbsqmx == null) {
			setResult("flag", "您输入的用户中心或零件号有误，请重新输入！");
		} else {
			setResult("lingj", dbsqmx);
		}
		return AJAX;
	}
	
	// 毛需求明细按用户中心与按产线文件下载
	public String downLoadFileDetailRi(@Param CompareCyc bean, @Param("mode") String mode) throws NullCalendarCenterException, IOException {
		Map<String, Object> dataSource = null;
		String message = null;
		// 查询毛需求明细
		try {
			if (!mode.isEmpty()) {
				dataSource = maoxqCompareService.query(mode, bean, true);
			}
		} catch (Exception e) {
			HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
			OutputStream output = null;
			reqResponse.setContentType("text/html");
			reqResponse.setCharacterEncoding("utf-8");
			output = reqResponse.getOutputStream() ;
			Map<String,String> map = new HashMap<String,String>();
			message = "<script>parent.callback('"+e.getMessage()+"')</script>";
			map.put("message", e.getMessage());
			setResult("result", map);
			if (output != null) 
			{
				output.write(message.getBytes());
				output.flush();
				output.close();
			}
			return AJAX;
//			return mode+bean.getXuqlx();
		}
		
		// 拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		// 设置下载
		String fileName = null;
		if (bean.getXuqlx().equals("Cyc")) {
			fileName = "mxqCyc.ftl";
		} else if (bean.getXuqlx().equals("Week")) {
			fileName = "mxqWeek.ftl";
		} else if (bean.getXuqlx().equals("Days")) {
			fileName = "mxqDays.ftl";
		}
		downloadServices.downloadFile(fileName, dataSource, response, "毛需求明细", ExportConstants.FILE_XLS, false);

		// 返回类型一定要是download类型的
		return "downLoad";

	}
	
	

	//相同的版次、基准版次对比结果 是否已经发送给外部系统
	public String maoxqSffs(@Param CompareCyc bean) { 
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("xuqbc", getParams().get("xuqbc"));
		map.put("xuqbc1", getParams().get("xuqbc1")); 
		
		map.put("xsfs", getParams().get("xsfs")); //比较方式 0012943
		 
		boolean flag = false; 
		
		flag = maoxqCompareSendService.maoxqSffs(map);
				
		
		if(flag){
			setResult("success", false); 
			setResult("flag", "已经发送过相同比较方式的需求版次对比数据，不能再次发送！"); 
			return AJAX;
		}
		
		flag = maoxqCompareService.maoxqSfbj();
		
		if(flag){
			setResult("success", false); 
			setResult("flag", "当前有其他用户正在比较，请稍后再试！"); 
			return AJAX;
		}
		 
		return AJAX;
	}
	
	
	//是否有用户正在比较周期
	public String maoxqSfbj() {  
		Map<String, String> cmap = new HashMap<String, String>();
		boolean flag = false; 
		
		flag = maoxqCompareService.maoxqSfbj();
		
		if(flag){ 
			setResult("flag", "当前有其他用户正在比较，请稍后再试！"); 
			return AJAX;
		}
		 
		return AJAX;
	}
	
	
	
	
	//xss 20161010 v4_008
	public String SendComparePage(@Param CompareCyc bean) { 
		Map<String, String> map = new HashMap<String, String>();
		//map.put("usercenter", bean.getUsercenter());
		//map.put("lingjbh", bean.getLingjbh());
		map.put("xuqbc", bean.getXuqbc());
		map.put("xuqbc1", getParams().get("xuqbc1"));
		//map.put("jihyz", bean.getJihyz());
		map.put("xsfs", bean.getXsfs());
		map.put("xuqlx", bean.getXuqlx());
		map.put("zhizlx", bean.getZhizlx());
		map.put("mode", getParams().get("mode"));
		map.put("chanx", bean.getChanx()); 
		
		String  biaos= getParams().get("biaos");
		
		String  result = "失败！";
		
		boolean flag = false; //插入数据成功

		boolean flag_delete = false;//删除标识；	
		 
		
		
		Map<String, String> map_Jis = new HashMap<String, String>();
		map_Jis.put("usercenter", getUserInfo().getUsercenter());// 设置用户中心 
		map_Jis.put("jsrq", CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD));// 计算日期
		map_Jis.put("username", getUserInfo().getUsername());// 设置用户姓名
		
		if(getUserInfo().getUsercenter()==null && getUserInfo().getUsercenter().equals("") ){
			map_Jis.put("jiscldm", "");  
		}else if (getUserInfo().getUsercenter()!=null && getUserInfo().getUsercenter().equals("UW") ){
			map_Jis.put("jiscldm", Const.MXQBJ_UW);  
		}else if(getUserInfo().getUsercenter()!=null && getUserInfo().getUsercenter().equals("UL") ){
			map_Jis.put("jiscldm", Const.MXQBJ_UL);  
		}else if(getUserInfo().getUsercenter()!=null && getUserInfo().getUsercenter().equals("UX") ){
			map_Jis.put("jiscldm", Const.MXQBJ_UX);  
		}else if (getUserInfo().getUsercenter()!=null && getUserInfo().getUsercenter().equals("VD") ){
			map_Jis.put("jiscldm", Const.MXQBJ_VD);  
		}
		
		//加锁
		map_Jis.put("chulzt", "20"); 
		maoxqCompareService.updateJisState(map_Jis);
		
		
		try {
			 
			if (!getParams().get("mode").isEmpty()) {
				
				Map<String, Object> maprs = maoxqCompareService.comparePpOrPs(map, bean, true);
				
				if(biaos.equals("1")){
					//插入数据库表
					List<CompareCyc>  comparers = (List<CompareCyc>)(maprs.get("list")); 
					
					//List<CompareCyc>  comparers = (List<CompareCyc>)(maprs.get("rows")); 
					
					//如果存在相同的版次1、版次2、基准版次， 就删除原来的数据 
					boolean flag_exists = false;
						
				    flag_exists = maoxqCompareSendService.queryXuqbcJizExists(map);
				    
					if(flag_exists){//删除原来存在的相同数据
						flag_delete =  maoxqCompareSendService.doDeleteXuqbcJizExists(map); 
					}
					 
				 flag = maoxqCompareSendService.insertCompareQr(comparers, map , getUserInfo().getUsername(), CommonFun.getJavaTime());					
		
				} 
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
			
		//比较完 更新处理状态为0,比较完成，解锁状态
		map_Jis.put("chulzt", "90"); 
		maoxqCompareService.updateJisState(map_Jis);
		 			
		if (flag) {
			result = "导出数据成功！";
		}
		setResult("result", result);
		return "success";
	}
	
	
}
