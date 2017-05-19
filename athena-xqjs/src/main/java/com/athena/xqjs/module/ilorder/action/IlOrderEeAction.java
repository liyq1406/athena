package com.athena.xqjs.module.ilorder.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.DateCell;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Gongyzq;
import com.athena.xqjs.entity.common.Gongyzx;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.module.anxorder.service.AnxMaoxqService;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.common.service.GongysService;
import com.athena.xqjs.module.common.service.JiaofrlService;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.common.service.LingjGongysService;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.common.service.WulljService;
import com.athena.xqjs.module.common.service.XqjsLingjckService;
import com.athena.xqjs.module.diaobl.service.DiaobsqService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.DingdljService;
import com.athena.xqjs.module.ilorder.service.DingdmxService;
import com.athena.xqjs.module.ilorder.service.ExpEffectOrderService;
import com.athena.xqjs.module.ilorder.service.GongyzqService;
import com.athena.xqjs.module.ilorder.service.GongyzxService;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.athena.xqjs.module.ilorder.service.IleditAndEffectService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：IlOrderEeAction
 * <p>
 * 类描述： Il订单
 * </p>
 * 创建人：NIESY
 * <p>
 * 创建时间：2012-03-26
 * </p>
 * 
 * @version 1.0
 * 
 */
@Component
public class IlOrderEeAction extends BaseWtcAction {

	@Inject
	private IleditAndEffectService ileditAndEffectService;
	// 日志
	private final Log log = org.apache.commons.logging.LogFactory.getLog(IlOrderEeAction.class);
	
	@Inject
	private GongyzqService gongyzqService;

	@Inject
	private GongyzxService gongyzxService;

	@Inject
	private XqjsLingjckService xService;

	@Inject
	private DingdljService dingdljService;
	
	@Inject
	private DingdService dingdService;

	@Inject
	private DingdmxService dingdmxService;

	@Inject
	private ExpEffectOrderService expEffectOrderService;

	@Inject
	private GongysService gongysService;
	@Inject
	private LingjGongysService lingjGongysService;
	@Inject
	private DiaobsqService dService;
	@Inject
	private LingjService ljService;

	@Inject
	private LingjxhdService lingjxhdService;
	@Inject
	private WulljService wulljService;

	@Inject
	private com.athena.xqjs.module.common.service.NianxmService nianxmService;

	@Inject
	private IlOrderService ilOrderService;
	@Inject
	private UserOperLog userOperLog;
	
	
	@Inject
	private JiaofrlService jiaofrlService;// 注入交付日历service
	
	/////////wuyichao2014-10-31 导入国产件临时订单////////////
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;
	/////////wuyichao2014-10-31 导入国产件临时订单////////////
	@Inject
	private AnxMaoxqService  anxMaoxqService;
	
	/**
	 * 导出servives hzg 2015.7.14 
	 */
	@Inject
	private DownLoadServices  downLoadServices;
	
	//获取用户中心
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}

	/**
	 * 页面初始化，执行跳转
	 */
	public String initEditE() {
		setResult("jihyz",getUserInfo().getZuh());
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "success";
	}

	/**
	 * 发货周期转换
	 * 
	 * @return
	 */
	public String convertFhzq(String chullx, String fahzq) {
		String convertzq = null;
		if (chullx.equalsIgnoreCase("PP")) {
			convertzq = gongyzqService.queryGongyzqbyRq(fahzq).getGongyzq();
		} else if (chullx.equalsIgnoreCase("PS")) {
			convertzq = gongyzxService.queryGongyzxByRq(fahzq).getGongyzx();
		} else {// 按需/PJ
			convertzq = fahzq;
		}
		return convertzq;
	}

	/**
	 * 订单查询
	 */
	public String queryDd(@Param Dingd bean) {
		/*
		 * Map<String, String> paramMap = getParams(); log.info(getParams()); //
		 * 订单制作时间：起始时间 String dingdjssjFrom = getParam("dingdjssjFrom"); //
		 * 订单制作时间：结束时间 String dingdjssjTo = getParam("dingdjssjTo"); //
		 * 订单生效时间：起始时间 String dingdsxsjFrom = getParam("dingdsxsjFrom"); //
		 * 订单生效时间：结束时间 String dingdsxsjTo = getParam("dingdsxsjTo"); Map<String,
		 * String> message = new HashMap<String, String>(); if (dingdjssjFrom !=
		 * null && dingdjssjTo == null) { result = "请选择：订单制作时间-结束时间！\n"; } else
		 * if (dingdjssjFrom == null && dingdjssjTo != null) { result =
		 * "请选择：订单制作时间-起始时间！\n"; } else if (dingdjssjFrom != null && dingdjssjTo
		 * != null && dingdjssjFrom.compareTo(dingdjssjTo) > 0) { result =
		 * "订单制作时间的起始时间不能大于结束时间！\n"; } else if (dingdjssjFrom != null &&
		 * dingdjssjTo != null) { String dingdjssj =
		 * "to_char(dingdjssj,'yyyy-mm-dd HH24:mi:ss') between '" +
		 * dingdjssjFrom + "'and '" + dingdjssjTo + "'"; log.info(dingdjssj);
		 * paramMap.put("dingdjssj", dingdjssj); } // 生效时间 if (dingdsxsjFrom !=
		 * null && dingdsxsjTo == null) { result += "请选择：订单生效时间-结束时间！"; } else
		 * if (dingdsxsjFrom == null && dingdsxsjTo != null) { result +=
		 * "请选择：订单生效时间-起始时间！"; } else if (dingdsxsjFrom != null && dingdsxsjTo
		 * != null && dingdsxsjFrom.compareTo(dingdsxsjTo) > 0) { result +=
		 * "订单生效时间的起始时间不能大于结束时间！"; } else if (dingdsxsjFrom != null &&
		 * dingdsxsjTo != null) { String dingdsxsj =
		 * "dingdsxsj between to_date('" + dingdsxsjFrom +
		 * "','yyyy-mm-dd')and to_date('" + dingdsxsjTo + "','yyyy-mm-dd')";
		 * log.info(dingdsxsj); paramMap.put("dingdsxsj", dingdsxsj); } if
		 * (result.length() != 0) { message.put("message", result);
		 * setResult("result", message); } else { setResult("result",
		 * ileditAndEffectService.selectDd(paramMap, bean)); }
		 */
		setResult("result", ileditAndEffectService.selectDdByPage(getParams(), bean));
		return AJAX;
	}

	public String queryDdNopages() {
		setResult("result", ileditAndEffectService.selectDdLs(getParams()));
		return AJAX;
	}
	

/*********************************  hzg 添加方法 2015/07/14 start ******************************/		
	/**
	 * 按需订单数量查询
	 * @author 贺志国
	 * @date 2015-7-14
	 * @return
	 */
	public String queryAnxDdCount() {
		setResult("result", ileditAndEffectService.selectAnxDdCount(getParams()));
		return AJAX;
	}
	
	/**
	 *  按需订单导出  mantis:0011506
	 * @author 贺志国
	 * @date 2015-7-14
	 * @return
	 * @throws Exception
	 */
	public String anxDingdmxDownload() throws Exception {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需(C1CD,M1MD,CVMV)待生效订单导出", "按需待生效订单导出开始");
		Map<String, Object> dataSource = new HashMap<String, Object>();
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		Map<String, String> param = getParams();
		List<Dingdmx> dingdmxList = this.ileditAndEffectService.selectAnxDdLs(param);
/*		if(dingdmxList==null){
			PrintWriter out = response.getWriter();
			out.println("<script>parent.callback('按需订单下订单零件数据为空，没有可以导出的数据源')</script>");
			out.flush();
			out.close();
			return AJAX;
		}*/
		dataSource.put("dataSource", dingdmxList);
		this.downLoadServices.downloadFile("anxDingd.ftl", dataSource, response, "订单修改及生效--按需订单导出",
				ExportConstants.FILE_XLS, false);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需(C1CD,M1MD,CVMV)待生效订单导出", "按需待生效订单导出结束");
		// 返回类型一定要是download类型的
		return "download";
	}
	
/******************************** end ******************************/		

	/**
	 * 订单修改待生效、生效、拒绝
	 */
	public String updateDaStatus(@Param("dd") ArrayList<Dingd> ls) {
		if (ls.size() == 0) {
			ls = (ArrayList<Dingd>) ileditAndEffectService.selectDdLs(getParams());
		}
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		String flag = getParam("flag");
		String result = MessageConst.UPDATE_COUNT_0;
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效：生效、拒绝", "生效、拒绝订单开始");
		try {
			result = ileditAndEffectService.updateDaStatus(ls, newEditor, editTime, flag);
			setResult("editor", newEditor);
			setResult("edit_time", "editTime");  //临时订单修改待生效及生效
		} catch (Exception e) {
			if (e.getMessage().endsWith("只能待生效制作中的订单！")) {
				result = "只能待生效制作中的订单！";
			} else if (e.getMessage().endsWith("只能生效状态为待生效的订单！")) {
				result = "只能生效状态为待生效的订单！";
			} else if (e.getMessage().endsWith("只能拒绝状态为待生效的订单！")) {
				result = "只能拒绝状态为待生效的订单！";
			}
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效：生效、拒绝", "生效、拒绝订单结束");
		setResult("result", result);
		return AJAX;
	}

	/**
	 * 临时订单修改待生效、生效
	 */
	public String updateLsStatus(@Param("bean") Dingd bean) {
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		String flag = getParam("flag");
		Map<String, String> map=new HashMap();
		String result = MessageConst.UPDATE_COUNT_0;
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效：生效、拒绝", "生效、拒绝订单开始");
		try {
			map.put("zhuangt", ileditAndEffectService.updateLsStatus( bean, newEditor, editTime, flag));
			result="操作成功！";
		} catch (Exception e) {
			if (e.getMessage().endsWith("只能待生效制作中的订单！")) {
				result = "只能待生效制作中的订单！";
			} else if (e.getMessage().endsWith("只能生效状态为待生效的订单！")) {
				result = "只能生效状态为待生效的订单！";
			} 
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效：生效、拒绝", "生效、拒绝订单结束");
		map.put("editor", newEditor);
		map.put("edit_time", editTime);
		map.put("result", result);
		setResult("result", map);
		return AJAX;
	}
	/**
	 * 修改订单零件数量
	 */
	public String updateDdLj(@Param("dd") ArrayList<Dingdlj> ls) {
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		String result = MessageConst.UPDATE_COUNT_0;
		try {
			result = ileditAndEffectService.updateDdljSl(ls, newEditor, editTime);
		} catch (Exception e) {
		}
		setResult("result", result);
		if(result.equals("修改成功！")){
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效", "修改订单零件数量");
			for (Dingdlj bean : ls) {
				this.dingdService.updateDingdzt(bean.getDingdh());
			}
		}
		return AJAX;
	}

	/**
	 * 修改订单明细数量
	 */
	public String updateMxSl(@Param("ddmx") ArrayList<Dingdmx> ls) {
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		String result = MessageConst.UPDATE_COUNT_0;
		try {
			//判断订单状态   xh 10607修改订单明细时先判断订单状态
			int zhuangt=dingdService.queryDingdzt(getParam("dingdh"));
			if(zhuangt ==-99){
				setResult("result", "该订单不存在或状态有误，请查证！");
			   return AJAX;
			}else if(zhuangt >3){
				setResult("result", "只能对已定义、制作中、待生效、拒绝的订单进行操作！请检查订单状态！");
			   return AJAX;
			 }
			result = ileditAndEffectService.updateDdmxSl(ls, newEditor, editTime);
			if (!("temp").equalsIgnoreCase(getParam("lx"))) {
			this.sumSlToPn(newEditor, editTime);
			} else {
				// 是临时
			}
		} catch (Exception e) {
		}
		if(result.equals("修改成功！")){
			this.dingdService.updateDingdzt(getParam("dingdh"));

		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效", "修改订单明细数量");
		setResult("result", result);
		return AJAX;
	}

	/**
	 * 生效订单导出
	 */
	@SuppressWarnings("unchecked")
	public String expilOrder(@Param Dingd bean) {
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		List<Map<String, Object>> ls = ileditAndEffectService.selectEeDdmx(getParams());
		Map<String, Object> dd = ileditAndEffectService.selectDd(bean, getParams());
		List<Map<String, Object>> dingd = (List<Map<String, Object>>) dd.get("rows");
		for (int i = 0; i < dingd.size(); i++) {
			Map<String, Object> map = dingd.get(i);
			map.put("DINGDLX", "临时订单");
			map.put("SHIFFSGYS", map.get("SHIFFSGYS").equals("0") ? "否" : "是");
		}
		boolean flag = expEffectOrderService.exportExcel(response, ls, dingd);
		if (!flag) {
			String result = "导出文件出错！";
			setResult("result", new Message(result));
		}
		return AJAX;
	}

	/**
	 * <p>
	 * 判断该交付日期是否在该工业周期内
	 * </p>
	 * 
	 * @author NIESY
	 * @return
	 */
	public String getThisGyzq() {
		Gongyzq bean = gongyzqService.queryGongyzqbyRq(getParam("jiaofrq"));
		setResult("result", bean == null ? "" : bean.getGongyzq());
		return AJAX;
	}

	/**
	 * <p>
	 * 取某一个日期的下一个工业周期的第一天
	 * </p>
	 * 
	 * @author NIESY
	 * @return
	 */
	public String getNextZqFirstDay() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("jiessj", getParam("jiaofrq"));
		List<Gongyzq> ls = (List<Gongyzq>) gongyzqService.queryGongyzq(map);
		setResult("gongyzq", ls.size() > 1 ? ls.get(1) : "");
		return AJAX;
	}

	/**
	 * <p>
	 * 判断该交付日期是否在该工业周序内
	 * </p>
	 * 
	 * @author NIESY
	 * @return
	 */
	public String judgeZx() {
		Gongyzx bean = gongyzxService.queryGongyzx(getParam("gongyzx"));
		// 该工业周期开始日期
		String ksrq = bean.getKaissj();
		// 该工业周期结束日期
		String jsrq = bean.getJiessj();
		// 所选交付日期
		String jfrq = getParam("jiaofrq").trim();
		if (jfrq.compareTo(ksrq) < 0 || jfrq.compareTo(jsrq) > 0) {
			setResult("result", new Message("交付日期未在周序" + ksrq + "与" + jsrq + "之间"));
		}
		return AJAX;
	}

	/**
	 * <p>
	 * 根据类型查询供应商
	 * </p>
	 * 
	 * @author NIESY
	 * @return
	 */
	public String ifExistGys() {
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("result", gongysService.queryIlObjectBylx(loginUser.getUsercenter()));
		return AJAX;
	}

	public String validateExistGys() {
		Map<String, String> map = getParams();
		map.put("gongysbh", map.get("gongysdm"));
		String waibms = map.get("chullx");
		String notwaibms = null;
		List<com.athena.xqjs.entity.common.Wullj> wls = null;
		if(StringUtils.isNotBlank(waibms))
		{
			if(waibms.equalsIgnoreCase("PP"))
			{
				notwaibms = "'PS','PJ'";
			}
			else if(waibms.equalsIgnoreCase("PS"))
			{
				notwaibms = "'PP','PJ'";
			}
			else if(waibms.equalsIgnoreCase("PJ"))
			{
				notwaibms = "'PS','PP'";
			}
			if(StringUtils.isNotBlank(notwaibms))
			{
				//map.put("notwaibms", notwaibms);
			}
			wls = wulljService.queryWulljList(map);
		}
		if (null == wls  || wls.size() == 0) {
			setResult("msg", "该订单类别在物流路径中未找到您输入的供应商代码");
		}
		return AJAX;
	}
	
	public String validateGys() {
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "供应商校验", "查询供应商开始");
		boolean flag = this.gongysService.queryAllGys(getParam("gongysdm"), loginUser.getUsercenter()) ;
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "供应商校验", "查询供应商结束");
		if (!flag) {
			setResult("msg", "供应商代码不存在");
		}
		return AJAX;
	}

	/**
	 * 订单零件页面初始化，执行跳转
	 */
	@SuppressWarnings("unchecked")
	public String initDdlj(@Param Dingd bean) {
		setResult("dingdh", getParam("DINGDH"));
		setResult("dingdnr", getParam("DINGDNR"));
		setResult("usercenter", getParam("USERCENTER"));
		setResult("gongysdm", getParam("GONGYSDM"));
		setResult("chullx", getParam("CHULLX"));
		setResult("dingdzt", getParam("DINGDZT"));
		setResult("fahzq", getParam("FAHZQ"));
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("dingdh", getParam("DINGDH"));
		Map<String, Object> map = dingdljService.queryAllDingdljForMap(bean, maps);
		List<Dingdlj> all = (List<Dingdlj>) map.get("rows");
		if (!all.isEmpty()) {
			// setResult("ziyhqrq", all.get(0).getZiyhqrq().substring(0, 10));
			String p0fyzqxh = getParam("FAHZQ");
			String chullx = getParam("CHULLX");
			String result = "";
			map.put("usercenter", getParam("USERCENTER").substring(0, 2));
			// 订单零件供货模式
			if (chullx.equalsIgnoreCase("PP")||chullx.equalsIgnoreCase("NP")) {
				result = p0fyzqxh.substring(0, 4) + "年" + p0fyzqxh.substring(4, 6) + "月";
			} else if (chullx.equalsIgnoreCase("PS")||chullx.equalsIgnoreCase("NS")) {
				//p0zq = calendarCenterService.getNianzq(getParam("USERCENTER").substring(0, 2), p0fyzqxh);
				result =  p0fyzqxh.substring(0, 4) + "年" + p0fyzqxh.substring(4, 6) + "周";
			} else {
				// map.put("riq", p0fyzqxh);
				// p0zq =
				// calendarCenterService.queryCalendarCenterObject(map).getNianzq();
				result = p0fyzqxh.substring(0, 4) + "年" + p0fyzqxh.substring(5, 7) + "月" + p0fyzqxh.substring(8, 10);
                
			}
			setResult("p0fyzqxh", result);
			// setResult("gonghms", getGonghms(chullx.substring(1)));
		}
		return "success";
	}

	public String getGonghms() {
		ArrayList<Map<String, String>> ls = new ArrayList<Map<String, String>>();
		Map<String, String> msp = new HashMap<String, String>();
		msp.put("gonghms", "P" + getParam("chullx").substring(1));
		msp.put("names", "P" + getParam("chullx").substring(1));
		ls.add(msp);
		Map<String, String> msnp = new HashMap<String, String>();
		msnp.put("gonghms", "N" + getParam("chullx").substring(1));
		msnp.put("names", "非周期");
		ls.add(msnp);
		setResult("result", ls);
		return AJAX;
	}

	/**
	 * 订单明细页面初始化，执行跳转
	 */
	public String initDdmx() {
		String ljid =  this.getParam("id");
		String dingdh =  this.getParam("dingdh");
		String usercenter =  this.getParam("usercenter");
		String gongysdm =  this.getParam("gongysdm");
		String lingjh =  this.getParam("lingjh");
		String cangkdm =  this.getParam("cangkdm");
		String dingdzt =  this.getParam("dingdzt");
		String gonghms =  this.getParam("gonghms");
		String p0fyzqxh =  this.getParam("p0fyzqxh");
		setResult("ljid",ljid);
		setResult("dingdh", dingdh);
		setResult("usercenter", usercenter);
		setResult("gongysdm", gongysdm);
		setResult("lingjh", lingjh);
		setResult("cangkdm", cangkdm);
		setResult("dingdzt", dingdzt);
		setResult("gonghms", gonghms);
		setResult("pn", getParam("pn"));
		Dingdlj ddlj = new Dingdlj();
		ddlj.setId(ljid);
		ddlj.setDingdh(dingdh);
		ddlj.setUsercenter(usercenter);
		ddlj.setGongysdm(gongysdm);
		ddlj.setLingjbh(lingjh);
		ddlj.setCangkdm(cangkdm);
		ddlj.setDingdzt(dingdzt);
		ddlj.setGonghms(gonghms);
		ddlj.setP0fyzqxh(p0fyzqxh);
		if (getParam("pn") != null) {
			Map<String, String> map = ileditAndEffectService.getPartsDate(ddlj);
			setResult("prqfw", map.get(getParam("pn") + "fw"));
			// setSessionAttribute("prq", map.get(getParam("pn")));
		}
		return "success";
	}

	
	/**
	 * 订单明细页面初始化，执行跳转
	 */
	public String initAnxDingd() {
		setResult("dingdh", getParam("DINGDH"));
		setResult("dingdnr", getParam("DINGDNR"));
		setResult("usercenter", getParam("USERCENTER"));
		setResult("gongysdm", getParam("GONGYSDM"));
		setResult("chullx", getParam("CHULLX"));
		setResult("dingdzt", getParam("DINGDZT"));
		setResult("p0fyzqxh", getParam("FAHZQ"));
		if (getParam("CHULLX").indexOf("M") != -1) {
			return "morderAx";
		}
		return "success";
	}
	
	/**
	 * 订单明细页面初始化，执行跳转
	 */
	public String initAnxMxDingd() {
		String ljid =  this.getParam("id");
		String dingdh =  this.getParam("dingdh");
		String usercenter =  this.getParam("usercenter");
		String gongysdm =  this.getParam("gongysdm");
		String lingjh =  this.getParam("lingjh");
		String cangkdm =  this.getParam("cangkdm");
		String dingdzt =  this.getParam("dingdzt");
		String gonghms =  this.getParam("gonghms");
		String p0fyzqxh =  this.getParam("p0fyzqxh");
		setResult("ljid", ljid);
		setResult("dingdh", dingdh);
		setResult("usercenter",usercenter);
		setResult("gongysdm", gongysdm);
		setResult("lingjh",lingjh);
		setResult("cangkdm",cangkdm);
		setResult("dingdzt",dingdzt);
		setResult("gonghms", gonghms);
		setResult("pn", getParam("pn"));
		Dingdlj ddlj = new Dingdlj();
		ddlj.setId(ljid);
		ddlj.setDingdh(dingdh);
		ddlj.setUsercenter(usercenter);
		ddlj.setGongysdm(gongysdm);
		ddlj.setLingjbh(lingjh);
		ddlj.setCangkdm(cangkdm);
		ddlj.setDingdzt(dingdzt);
		ddlj.setGonghms(gonghms);
		ddlj.setP0fyzqxh(p0fyzqxh);
		if (getParam("pn") != null) {
			Map<String, String> map = ileditAndEffectService.getPartsDate(ddlj);
			setResult("prqfw", map.get(getParam("pn") + "fw"));
			// setSessionAttribute("prq", map.get(getParam("pn")));
		}
		if (gonghms.indexOf("M") != -1) {
			return "mAxmxOrder";
		}
		return "success";
	}
	
	
	
	
	
	
	

	/**
	 * <p>
	 * 查询订单零件
	 * </p>
	 * 
	 * @author NIESY
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryDdLj(@Param Dingd bean) {
		Map<String,Object> map = dingdljService.queryAllDingdljForMap(bean, getParams());
		List<Dingdlj> ddljlist = (List<Dingdlj>) map.get("rows");
		for (int i = 0; i < ddljlist.size(); i++) {
			if(getParam("chullx").equals("M1") || getParam("chullx").equals("MJ") || getParam("chullx").equals("MV")){
				ddljlist.get(i).setBzlx(ddljlist.get(i).getUsbaozlx());
				ddljlist.get(i).setBzrl(ddljlist.get(i).getUsbaozrl());
			}else if(getParam("chullx").equals("MD")){
				ddljlist.get(i).setBzlx(ddljlist.get(i).getUabzuclx());
				ddljlist.get(i).setBzrl(ddljlist.get(i).getUabzucrl());
			}
		}
		map.put("rows", ddljlist);
		setResult("result", map);
		return AJAX;
	}

	/**
	 * <p>
	 * 查询订单明细
	 * </p>
	 * 
	 * @author NIESY
	 * @param bean
	 * @return
	 */
	public String queryDdmx(@Param Dingdmx bean) {
		Map<String, String> map = getParams();
		map.put("lingjbh", getParam("lingjbh") == null ? getParam("lingjh") : getParam("lingjbh"));
		// pn
		String pn = getParam("pn");
		// pn日期范围
		String prqfw = getParam("prqfw");
		String prqfwK = "";
		String prqfwJ = "";
		if (getParam("pn") != null && prqfw != null && prqfw.indexOf("_") != -1) {
			int index = prqfw.indexOf("_");
			prqfwK = prqfw.substring(0, index);
			prqfwJ = prqfw.substring(index + 1);
			
			String rq = "to_char(jiaofrq,'yyyy-mm-dd') between '" + prqfwK + "' and  '" + prqfwJ + "'";
			//String rq = "to_char(jiaofrq,'yyyy-mm-dd') < '"  + prqfwJ + "'";
			
			map.put("rq", rq);
		} else if (prqfw != null) {
			
			//prqfwK = prqfw;
			//prqfwJ = prqfw;
			//String rq = "to_char(jiaofrq,'yyyy-MM-dd')='" + prqfw + "'";
			//map.put("rq", rq);
			map.put("leix", "9");
		}
		String result = "";
		Map<String, String> message = new HashMap<String, String>();
		// 交付日期
		String jiaofrqF = getParam("jiaofrqF");
		String jiaofrqT = getParam("jiaofrqT");
		//原PJ订单明细查询P0日期校验有误 现修改针对PJ模式
		if(getParam("gonghms") != null && (getParam("gonghms").equals("PJ") || getParam("gonghms").equals("VJ")) ){
			if (jiaofrqF != null && jiaofrqT == null) {
				result = "请选择：交付日期-结束时间！";
			} else if (jiaofrqF == null && jiaofrqT != null) {
				result = "请选择：交付日期-开始时间！";
			} else if (jiaofrqF != null && jiaofrqT != null && jiaofrqF.compareTo(jiaofrqT) > 0) {
				result = "交付日期的开始时间不能大于结束时间！";
			}  else if (jiaofrqF != null && jiaofrqT != null && jiaofrqF.compareTo(jiaofrqT) <= 0) {
				String rq = "jiaofrq between to_date('" + jiaofrqF + "','yyyy-mm-dd') and to_date('" + jiaofrqT + "','yyyy-mm-dd')";
				map.put("rq", rq);
			}
		}else{
			if (jiaofrqF != null && jiaofrqT == null) {
				result = "请选择：交付日期-结束时间！";
			} else if (jiaofrqF == null && jiaofrqT != null) {
				result = "请选择：交付日期-开始时间！";
			} else if (jiaofrqF != null && jiaofrqT != null && jiaofrqF.compareTo(jiaofrqT) > 0) {
				result = "交付日期的开始时间不能大于结束时间！";
			} else if (jiaofrqF != null && jiaofrqT != null && (prqfwK.compareTo(jiaofrqF) > 0 || prqfwJ.compareTo(jiaofrqF) < 0)) {
				result = "交付日期的开始时间不在" + pn + "开始日期" + prqfwK + "与" + "结束日期" + prqfwJ + "之间";
			} else if (jiaofrqF != null && jiaofrqT != null && (prqfwJ.compareTo(jiaofrqT) < 0 || prqfwK.compareTo(jiaofrqT) > 0)) {
				result = "交付日期的结束时间不能大于" + pn + "开始日期" + prqfwK + "与" + "结束日期" + prqfwJ + "之间";
			} else if (jiaofrqF != null && jiaofrqT != null && jiaofrqF.compareTo(jiaofrqT) <= 0) {
				String rq = "jiaofrq between to_date('" + jiaofrqF + "','yyyy-mm-dd') and to_date('" + jiaofrqT + "','yyyy-mm-dd')";
				map.put("rq", rq);
			}
		}
		
		// 操作有误提示
		if (result.length() != 0) {
			message.put("message", result);
			setResult("result", message);
		} else {
			map.put("gonghlx", bean.getGonghlx() != null ? bean.getGonghlx() : "");
			setResult("result", dingdmxService.queryAllDingdmx(bean, map));
		}
		return AJAX;
	}

	/**
	 * 临时订单明细查询(周期、周、日)
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryTempDdmx(@Param Dingdmx bean) {
		Map<String, String> querymap = getParams();
		querymap.remove("gonghlx");
		Map<String, Object> map = dingdmxService.queryAllDingdmx(bean, querymap);
		List<Dingdmx> ls = (List<Dingdmx>) map.get("rows");
		for (int i = 0; i < ls.size(); i++) {
			Dingdmx dmx = ls.get(i);
			dmx.setGonghlx(dmx.getGonghlx().indexOf("N") != -1 ? "非周期" : dmx.getGonghlx());
			if (dmx.getJiaofrq() != null && !dmx.getJiaofrq().isEmpty()) {
				Gongyzq gongyzq = gongyzqService.queryGongyzqbyRq(dmx.getJiaofrq());
				dmx.setGongyzq(gongyzq == null ? null : gongyzq.getGongyzq());
			}
		}
		map.put("rows", ls);
		setResult("result", map);
		return AJAX;
	}

	/**
	 * 临时订单明细查询(按需)
	 * 
	 * @return
	 */
	public String queryTempAnx(@Param Dingdmx bean) {
		setResult("result", dingdmxService.queryAllDingdmx(bean, getParams()));
		return AJAX;
	}

	/**
	 * 临时订单创建查询(看板)
	 * 
	 * @return
	 */
	public String queryTempKanb(@Param Dingdmx bean) {
		setResult("result", dingdmxService.queryAllDingdmx(bean, getParams()));
		return AJAX;
	}

	/**
	 * <p>
	 * 删除订单明细
	 * </p>
	 * 
	 * @author NIESY
	 * @param bean
	 * @return
	 */
	public String deleteDdMx(@Param("ddmx") ArrayList<Dingdmx> ls) {
		String result = MessageConst.DELETE_COUNT_0;
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		try {
			// xh 10607 删除订单明细时先判断订单状态
			int zhuangt=dingdService.queryDingdzt(getParam("dingdh"));
			if(zhuangt ==-99){
				setResult("result", "该订单不存在或状态有误，请查证！");
			   return AJAX;
			}else if(zhuangt >3){
				setResult("result", "只能对已定义、制作中、待生效、拒绝的订单进行操作！请检查订单状态！");
			   return AJAX;
			 }
			boolean flag = ileditAndEffectService.deleteDdMx(ls);
			if (flag && !("temp").equalsIgnoreCase(getParam("lx"))) {
				this.sumSlToPn(newEditor, editTime);
			}
			if (flag) {
				this.dingdService.updateDingdzt(getParam("dingdh"));
				result = "删除成功!";
				setResult("res", result);
			}
		} catch (Exception e) {
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效", "删除订单明细");
		setResult("result", result);
		return AJAX;
	}
	
	
	/***
	 * @author wuyichao
	 * @param bean
	 * @return
	 * @see 添加订单明细
	 */
	public String addDdMx(@Param Dingdmx bean)  {

		//判断订单信息是否完整
		if(null == bean)
		{
			setResult("result", "订单信息有误");
		}
		else
		{
			String result = MessageConst.INSERT_COUNT_0;
			String newEditor = getUserInfo().getUsername();
			String editTime = CommonFun.getJavaTime();
			if(StringUtils.isBlank(bean.getLingjbh()) ||
			 StringUtils.isBlank(bean.getDingdh()) ||
			StringUtils.isBlank(bean.getUsercenter())||
			StringUtils.isBlank(bean.getCangkdm())||
			StringUtils.isBlank(bean.getJiaofrq()))
			{
				setResult("result", "订单信息有误");
			}
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("lingjbh", bean.getLingjbh());
			paramMap.put("usercenter", bean.getUsercenter());
			paramMap.put("cangkbh", bean.getCangkdm());
			paramMap.put("cangkdm", bean.getCangkdm());
			//判断仓库编号是否存在
			List<Lingjck> ls = xService.queryObject(paramMap);
			if(null == ls || ls.size() <= 0)
			{
				//仓库编号不正确  错误提示
				setResult("result", "仓库编号不正确");
				return AJAX;
			}
			//判断交付日期是否为工作日
			paramMap.put("dingdh", bean.getDingdh());
			Dingdlj dingdlj = null;
			try
			{
				dingdlj = dingdljService.queryDingljObject(paramMap);
			}
			catch(Exception e)
			{
				dingdlj = null;
			}
			finally
			{
				if(null == dingdlj)
				{
					//订单信息有误 错误提示
					setResult("result", "订单信息有误");
					return AJAX;
				}
			}
			if(!bean.getCangkdm().equalsIgnoreCase(dingdlj.getCangkdm()))
			{
				//订单信息有误 错误提示
				setResult("result", "订单明细仓库信息与订单零件仓库信息不符");
				return AJAX;
			}
			boolean errorFlag = true;
			if(StringUtils.isNotBlank(bean.getGonghms()) && !bean.getGonghms().equalsIgnoreCase("PJ") && !bean.getGonghms().equalsIgnoreCase("NP") && !bean.getGonghms().equalsIgnoreCase("VJ"))
			{
				try
				{
					Map<String, String> map = ileditAndEffectService.getPartsDate(dingdlj);
					if(null != map && null != map.get("P0fw"))
					{
						String flag = map.get("P0fw");
						if(StringUtils.isBlank(flag))
						{
							//没有日期信息  提示错误
							setResult("result", "日期有误");
							return AJAX;
						}
						this.getParams().put("prq", flag);
						String[] nowFlag = bean.getJiaofrq().split("-");
						Date nowDate  = new Date(Integer.parseInt(nowFlag[0]), Integer.parseInt(nowFlag[1]), Integer.parseInt(nowFlag[2]));
						if(flag.contains("_"))
						{
							String[] timeFlag = flag.split("_");
							String begin = timeFlag[0];
							String end = timeFlag[1];
							String[] beginFlag = begin.split("-");
							String[] endFlag = end.split("-");
							Date beginDate  = new Date(Integer.parseInt(beginFlag[0]), Integer.parseInt(beginFlag[1]), Integer.parseInt(beginFlag[2]));
							Date endDate  = new Date(Integer.parseInt(endFlag[0]), Integer.parseInt(endFlag[1]), Integer.parseInt(endFlag[2]));
							if(beginDate.getTime() <= nowDate.getTime() && nowDate.getTime()<= endDate.getTime())
							{
								errorFlag = false;
								
							}
						}
						//日
						else
						{
							String[] beginFlag = flag.split("-");
							Date beginDate  = new Date(Integer.parseInt(beginFlag[0]), Integer.parseInt(beginFlag[1]), Integer.parseInt(beginFlag[2]));
							if(beginDate.getTime() == nowDate.getTime())
							{
								errorFlag = false;
							}
						}
						String zhouqi = CommonFun.addNianzq(dingdlj.getP0fyzqxh(), Const.MAXZQ, 0);
						//查看是否为交付日
						//List<Jiaofrl> jiaofrlList = jiaofrlService.queryAllJiaofm(dingdlj.getUsercenter(), dingdlj.getJiaofm(),zhouqi,bean.getJiaofrq());
						//if(null == jiaofrlList || jiaofrlList.size() <= 0)
						//{
						//	errorFlag = true;
						//}
						//查看是否有重复交付日
						map.put("jiaofrq", bean.getJiaofrq());
						List<Dingdmx> dingdmxs =  dingdmxService.queryListMx(map);
						if(null == dingdmxs || dingdmxs.size() <= 0)
						{
							errorFlag = false;
						}
						else
						{
							errorFlag = true;
						}
					}
				}
				catch(Exception e)
				{
					errorFlag = true;
				}
				finally
				{
					if(errorFlag)
					{
						setResult("result", "日期出错,请检查日期是否有重复!");
						return AJAX;
					}
				}
			}
			try {
				bean.setCreator(getUserInfo().getUsername());
				bean.setEditor(getUserInfo().getUsername());
				boolean flag = ileditAndEffectService.addDdMx(bean,dingdlj);
			
				if (flag) {
					if ( !("temp").equalsIgnoreCase(getParam("lx"))) {
						this.sumSlToPn(newEditor, editTime);
					}
					this.dingdService.updateDingdzt(getParam("dingdh"));
					result = "添加成功!";
					setResult("res", result);
				}
				else
				{
					setResult("result", "订单信息添加失败!");
				}
			} catch (Exception e) {
				setResult("result", "订单信息添加失败!");
			}
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效", "添加订单明细");
		}
		return AJAX;
	}
	
	
	
	public String effectDdmx() {
		String result = "生效成功";
		String dingdh = getParam("dingdh") ;
		Map<String,String> map = new HashMap<String,String>();
		map.put("dingdh", dingdh) ;
		Dingd bean = this.dingdService.queryDingdByDingdh(map) ;
		if (bean.getDingdzt().equalsIgnoreCase(Const.DINGD_STATUS_DSX)) {
			Dingd dingd = new Dingd() ;
			dingd.setDingdh(dingdh) ;
			dingd.setDingdzt(Const.DINGD_STATUS_YSX) ;
			boolean flag = this.dingdService.doUpdate(dingd);
			if(!flag){
				result = "生效失败" ;
			}
		}else{
			result = "生效失败：只能对待生效的订单进行生效";
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效", "生效订单明细");
		setResult("result", result);
		return AJAX;
	}

	/**
	 * <p>
	 * 删除订单零件
	 * </p>
	 * 
	 * @author NIESY
	 * @param bean
	 * @return
	 */
	public String deleteDdlj(@Param("dd") ArrayList<Dingdlj> ls) {
		String result = MessageConst.DELETE_COUNT_0;
		try {
			boolean flag = ileditAndEffectService.deleteDdLj(ls);
			if (flag) {
				result = "删除成功!";
			}
		} catch (Exception e) {
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效", "删除订单零件");
		setResult("result", result);
		return AJAX;
	}

	/**
	 * <p>
	 * 删除订单
	 * </p>
	 * 
	 * @author NIESY
	 * @param bean
	 * @return
	 */
	public String deleteDd(@Param("dd") ArrayList<Dingd> ls) {
		String result = MessageConst.DELETE_COUNT_0;
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		try {
			boolean flag = ileditAndEffectService.deleteDd(ls, newEditor, editTime);
			if (flag) {
				result = "删除成功!";
			}
		} catch (Exception e) {
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效", "删除订单");
		setResult("result", result);
		return AJAX;
	}

	/**
	 * 根据零件号、用户中心查询零件仓库
	 * 
	 * @author NIESY
	 * @return
	 */
	public String queryLingjck(){
		List<Lingjck> ls = xService.queryObject(getParams());
		setResult("result", ls);
		setResult("total", ls.size());
		 return AJAX;	
	}

	// TODO
	public String queryWulljMudd() {
		List<com.athena.xqjs.entity.common.Wullj> wls = wulljService.queryWulljList(getParams());
		setResult("result", wls);
		setResult("total", wls.size());
		return AJAX;
	}
	/**
	 * <p>
	 * 添加订单
	 * </p>
	 * 
	 * @author NIESY
	 * @param bean
	 * @return
	 */
	public String insertIlDd(@Param("dd") ArrayList<Dingd> ls) {
		String result = MessageConst.INSERT_COUNT_0;
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		String dingdh = this.ilOrderService.getOrderNumber(getParam("pattern"), getParam("usercenter"), getParam("gongysdm"), new HashMap<String, String>());
		result = ileditAndEffectService.insDingdIl(ls, newEditor, editTime, dingdh);
		setResult("result", result);
		setResult("dingdh", dingdh);
		return AJAX;
	}
	
	/**
	 * <p>
	 * 添加订单零件
	 * </p>
	 * 
	 * @author NIESY
	 * @param bean
	 * @return
	 */
	public String insertIlDdlj(@Param("dd") ArrayList<Dingdlj> ls) {
		String result = MessageConst.INSERT_COUNT_0;
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		Dingdlj lj = ls.get(0);
		Gongys gys = gongysService.queryObject(lj.getGongysdm(), lj.getUsercenter());
		lj.setGongyslx(gys.getLeix());
		if (lj.getGonghms().indexOf("M") == -1) {
			LingjGongys ljgys = lingjGongysService.select(lj.getUsercenter(), lj.getGongysdm(), lj.getLingjbh());
			lj.setGongysfe(ljgys.getGongyfe());
			lj.setUabzlx(ljgys.getUabzlx());
			lj.setUabzucsl(ljgys.getUaucgs());
			lj.setGongsmc(gys.getGongsmc());
		}
		// 订货车间
		Lingj lingj = ljService.select(lj.getUsercenter(), lj.getLingjbh());
		lj.setDinghcj(lingj.getDinghcj());
		lj.setGuanjljjb(lingj.getGuanjljjb());
		lj.setDanw(lingj.getDanw());
		String jihyz = lj.getJihyz();
		lj.setJihyz(jihyz);
		List<?> vals = dingdljService.queryAllDingdljForBean(lj);
		if (vals.size() != 0) {
			setResult("result", result);
			return AJAX;
		}
		try {
			result = ileditAndEffectService.insDingdljIl(lj, newEditor, editTime);

			//dayOrderCountService.countDayOrder(ls.get(0), getParam("chullx"),lingj.getZhongwmc(),gys.getGongsmc(),gys.getNeibyhzx(),caozz);
			if(!"anx".equals(getParam("anx"))){
				// 新增订单零件时将预告插入到订单明细
				ileditAndEffectService.insertYugMx(lj);
			}

		} catch (Exception e) {
		}
		if(result.equals("新增成功!")){
			for (Dingdlj bean : ls) {
				this.dingdService.updateDingdzt(bean.getDingdh());
			}
		}
		setResult("result", result);
		return AJAX;
	}

	/**
	 * <p>
	 * 添加订单零件
	 * </p>
	 * 
	 * @author NIESY
	 * @param bean
	 * @return
	 */
	public String insertIlDdmx(@Param("dd") ArrayList<Dingdmx> ls) {
		String result = MessageConst.INSERT_COUNT_0;
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		try {
			result = ileditAndEffectService.insDingdmxIl(ls, newEditor, editTime);
			this.sumSlToPn(newEditor, editTime);
		} catch (Exception e) {
		}
		setResult("result", result);
		return AJAX;
	}

	/**
	 * 添加订单时，检查零件是否是国产件 验证制造路线是否为97W
	 */
	public String checkIlParts() {
		String zhizlx = "";
		try {
			Diaobsqmx mx = dService.selectLingjmc(getParams());
			zhizlx = mx.getLux();
		} catch (Exception e) {
			setResult("result", "您输入的零件号有误，请确认！");
		}
		if (Const.ZHIZAOLUXIAN_IL.equalsIgnoreCase(zhizlx)) {
			setResult("result", "请输入制造路线为97W的零件号！");
		}
		return AJAX;
	}

	/**
	 * 临时订单创建页面初始化
	 */
	public String initTempDd() {
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "success";
	}

	/**
	 * 临时订单修改及生效页面初始化
	 */
	public String initTempDdEe() {
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "success";
	}

	/**
	 * 临时订单修改及生效页面初始化 订单明细-周期、周、日
	 */
	public String initTempDdmx() {
		setResult("usercenter", this.getUserInfo().getUsercenter());
		setResult("dingdh", getParam("DINGDH"));
		setResult("dingdzt", getParam("DINGDZT"));
		setResult("dingdnr", getParam("DINGDNR"));
		setResult("usercenter", getParam("USERCENTER"));
		setResult("gongysdm", getParam("GONGYSDM"));
		setResult("gonghlx", getParam("CHULLX"));
		setResult("editor", getParam("EDITOR"));
		setResult("edit_time", getParam("EDIT_TIME"));
		return "success";
	}

	/**
	 * 临时订单修改及生效页面初始化
	 * <p>
	 * 订单明细-按需
	 * </p>
	 */
	public String initTempAnxDdmx() {
		setResult("usercenter", this.getUserInfo().getUsercenter());
		setResult("dingdh", getParam("DINGDH"));
		setResult("dingdnr", getParam("DINGDNR"));
		setResult("usercenter", getParam("USERCENTER"));
		setResult("gongysdm", getParam("GONGYSDM"));
		setResult("gonghlx", getParam("CHULLX"));
		setResult("dingdzt", getParam("DINGDZT"));
		setResult("editor", getParam("EDITOR"));
		setResult("edit_time", getParam("EDIT_TIME"));
		if (getParam("CHULLX").indexOf("M") != -1) {
			return "mTempOrderAx";
		}
		return "success";
	}

	/**
	 * 查询订单零件
	 */
	public String queryDdLj(@Param Dingdlj bean) {
		setResult("result", ileditAndEffectService.selectDalj(bean, getParams()));
		return AJAX;
	}

	public void sumSlToPn(String newEditor, String newEditTime) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", getParam("usercenter"));
		map.put("lingjbh", getParam("lingjbh"));
		map.put("dingdh", getParam("dingdh"));
		map.put("cangkdm", getParam("cangkdm"));
		map.put("gonghlx", getParam("gonghlx"));
		String prq = getParam("prq");
		if(StringUtils.isNotBlank(prq) && prq.indexOf("_") != -1)
		{
			int index = prq.indexOf("_");
			String prqfwK = prq.substring(0, index);
			String prqfwJ = prq.substring(index + 1);
			String rq = "to_char(jiaofrq,'yyyy-mm-dd') between '" + prqfwK + "' and  '" + prqfwJ + "'";
			map.put("RQ", rq);
		}
		Map<String, Object> sumLj = ileditAndEffectService.sumDdmxToLj(map);
		BigDecimal shul = BigDecimal.ZERO;
		if (sumLj == null) {
			sumLj = new HashMap<String, Object>();
		} else if(sumLj.get("SHUL") != null){
			shul = (BigDecimal) sumLj.get("SHUL");
		}
		sumLj.put((getParam("pn") + "sl").toLowerCase(), shul);
		sumLj.put("usercenter", getParam("usercenter"));
		sumLj.put("lingjbh", getParam("lingjbh"));
		sumLj.put("dingdh", getParam("dingdh"));
		sumLj.put("gongysdm", getParam("gongysdm"));
		sumLj.put("cangkdm", getParam("cangkdm"));
		sumLj.put("gonghms", getParam("gonghms"));
		ileditAndEffectService.updateDdljSl(sumLj, newEditor, newEditTime);
	}

	/**
	 * 查询零件供应商
	 */
	public String queryLjgys() {
		setResult("result", lingjGongysService.queryAll(getParams()));
		setResult("lingj", ljService.queryObject(getParam("lingjbh"), getParam("usercenter")));
		return AJAX;
	}

	/**
	 * 查询零件供应商
	 */
	public String validateXhd() {
		Lingjxhd lingjxhd = lingjxhdService.queryLingjxhdObject(getParams());
		if (lingjxhd == null) {

			setResult("result", "在零件消耗点未查到该消耗点");
		}
		return AJAX;
	}

	/**
	 * 临时订单创建（看板）根据供应商类型显示供货类型
	 */
	public String getKeh() {
		String gonghlx = getParam("gonghlx");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (gonghlx.equalsIgnoreCase("R1")) {
			// R1到消耗点
			List<Lingjxhd> ls = lingjxhdService.queryXiaohdByParam(getParams());
			for (Lingjxhd lingjxhd : ls) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("keh", lingjxhd.getXiaohdbh());
				list.add(map);
			}
		} else {
			// R2到仓库
			List<Lingjck> ckls = xService.queryObject(getParams());
			for (Lingjck lingjck : ckls) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("keh", lingjck.getCangkbh());
				list.add(map);
			}
		}
		setResult("result", list);
		return AJAX;
	}

	/**
	 * 临时订单查询（看板）
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String querykanbls(@Param Dingdmx bean) {
		Map<String, Object> map = dingdmxService.queryAllDingdmx(bean, getParams());
		List<Dingdmx> ls = (List<Dingdmx>) map.get("rows");
		if (!ls.isEmpty()) {
			for (int i = 0; i < ls.size(); i++) {
				Dingdmx dmx = ls.get(i);
				dmx.setKeh(dmx.getCangkdm() == null ? dmx.getXiaohd() : dmx.getCangkdm());
				ls.set(i, dmx);
			}
			map.put("rows", ls);
		}
		setResult("result", map);
		return AJAX;
	}

	public String getKblsDdh(String usercenter) {
		// 取得年型
		String year = nianxmService.getYear(usercenter);
		// 实力化Calendar类对象
		Calendar calendar = new GregorianCalendar();
		// 获取当前月
		int month = calendar.get(Calendar.MONTH) + 1;
		String mon = null;
		// 月份转换为16进制
		mon = Integer.toHexString(month).toUpperCase();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String daystr = String.format("%02d", day);
		// 前5的调拨申请单号
		String dh = "KL" + year + mon + daystr;
		String maxdh = ileditAndEffectService.getMaxKlddh(dh);
		if (maxdh == null) {
			dh += "001";
		} else {
			dh += String.format("%03d", Integer.parseInt(maxdh.substring(6)) + 1);
		}
		return dh;
	}

	/**
	 * <p>
	 * 临时看板插入
	 * </p>
	 * 
	 * @param dd
	 * @param mx
	 * @return
	 */
	public String insertLsKb(@Param Dingd dd, @Param Dingdmx mx) {
		String result = MessageConst.INSERT_COUNT_0;
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		String dingdh = ilOrderService.getOrderNumber(getParam("pattern"), dd.getUsercenter(), dd.getGongysdm(), new HashMap<String, String>());
		dd.setDingdh(dingdh);
		dd.setDingdlx(Const.DINGDLX_LINS);
		dd.setChullx(getParam("gonghlx"));
		try {
			result = ileditAndEffectService.insDingdIl(dd, newEditor, editTime, dingdh);
			// 订单零件
			Dingdlj lj = new Dingdlj();
			lj.setDingdh(dingdh);
			lj.setLingjbh(mx.getLingjbh());
			lj.setUsercenter(dd.getUsercenter());
			lj.setGongysdm(dd.getGongysdm());
			lj.setP0sl(mx.getShul());
			Gongys gys = gongysService.queryObject(lj.getGongysdm(), lj.getUsercenter());
			lj.setGongyslx(gys.getLeix());
			LingjGongys ljgys = lingjGongysService.select(lj.getUsercenter(), lj.getGongysdm(), lj.getLingjbh());
			lj.setGongysfe(ljgys.getGongyfe());
			lj.setUabzlx(ljgys.getUabzlx());
			lj.setUabzucsl(ljgys.getUaucgs());
			lj.setUabzuclx(ljgys.getUcbzlx());
			lj.setUabzucrl(ljgys.getUcrl());
			// 订货车间
			Lingj lingj = ljService.select(lj.getUsercenter(), lj.getLingjbh());
			lj.setDinghcj(lingj.getDinghcj());
			ileditAndEffectService.insDingdljIl(lj, newEditor, editTime);
			if (getParam("gonghlx").equalsIgnoreCase("R1")) {
				mx.setXiaohd(mx.getKeh());
			} else {
				mx.setCangkdm(mx.getKeh());
			}
			mx.setFahd(gys.getFayd());
			mx.setGonghlx(getParam("gonghlx"));
			mx.setLingjbh(mx.getLingjbh());
			mx.setDingdh(dingdh);
			mx.setDinghcj(lingj.getDinghcj());
			mx.setDanw(lingj.getDanw());
			mx.setJihyz(lingj.getJihy());
			mx.setUsercenter(dd.getUsercenter());
			mx.setGongysdm(dd.getGongysdm());
			mx.setShul(mx.getShul());
			mx.setUabzlx(ljgys.getUabzlx());
			mx.setUabzucsl(ljgys.getUaucgs());
			mx.setUabzuclx(ljgys.getUcbzlx());
			mx.setUabzucrl(ljgys.getUcrl());
			ileditAndEffectService.insDingdmxIl(mx, newEditor, editTime);
		} catch (Exception e) {

		}
		setResult("dingdh", dingdh);
		setResult("result", result);
		return AJAX;
	}

	// 临时订单看板修改
	public String updateLsKbSl(@Param("ddmx") ArrayList<Dingdmx> ls) {
		String result = MessageConst.UPDATE_COUNT_0;
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		try{
		result = ileditAndEffectService.updateDdmxSl(ls, newEditor, editTime);
		Map<String,Object> map = ileditAndEffectService.sumDdmxToLj(getParams());
		map.put("p0sl", map.get("SHUL"));
		map.put("dingdh", map.get("DINGDH"));
		map.put("lingjbh", map.get("LINGJBH"));
		map.put("usercenter", map.get("USERCENTER"));
		map.put("newEditor", newEditor);
		map.put("newEdittime", editTime);
		ileditAndEffectService.updateDdljSl(map);
		}catch(Exception e){
			
		}
		setResult("result", result);
		return AJAX;
	}

	// 临时看板订单明细的删除
	public String deleteKbDdmx(@Param("ddmx") ArrayList<Dingdmx> ls) {
		String result = MessageConst.DELETE_COUNT_0;
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		try {
			boolean flag = ileditAndEffectService.deleteDdMx(ls);
			Map<String, Object> map = ileditAndEffectService.sumDdmxToLj(getParams());
			if (flag) {
				result = "删除成功!";
			}
			if (map != null && !map.isEmpty()) {
				map.put("p0sl", map.get("SHUL"));
				map.put("dingdh", map.get("DINGDH"));
				map.put("lingjbh", map.get("LINGJBH"));
				map.put("usercenter", map.get("USERCENTER"));
				map.put("newEditor", newEditor);
				map.put("newEdittime", editTime);
				ileditAndEffectService.updateDdljSl(map);
			}
		} catch (Exception e) {
		}
		setResult("result", result);
		return AJAX;
	}

	/**
	 * 修改临时订单零件的数量
	 */
	public void updateTempDdljsl(List<Dingdmx> ls) {

	}

	/**
	 * 筛选看板循环编码
	 */
	public String getKanbxhbm() {
		Map<String, String> map = getParams();
		map.put("gonghms", map.get("gonghlx"));
		if (getParam("gonghlx").equalsIgnoreCase("R1")) {
			map.put("xiaohd", map.get("keh"));
		} else if (getParam("gonghlx").equalsIgnoreCase("R2")) {
			map.put("cangkdm", map.get("keh"));
		}
		setResult("result", ileditAndEffectService.getKanbxhbm(map));
		return AJAX;
	}


	// 临时订单周期 增加
	public String addLsZqMx(@Param("insert") ArrayList<Dingdmx> list) throws ParseException {
		String result = MessageConst.INSERT_COUNT_0;
		String time = CommonFun.getJavaTime();
		for (int i = 0; i < list.size(); i++) {
			Dingdmx bean = list.get(i);
			bean.setActive("1");
			bean.setCreator(getUserInfo().getUsername());
			bean.setEditor(getUserInfo().getUsername());
			bean.setCreate_time(time);
			bean.setEdit_time(time);
			bean.setJissl(bean.getShul());
			Gongys gys = gongysService.queryObject(bean.getGongysdm(), bean.getUsercenter());
			bean.setGongyslx(gys.getLeix());
			bean.setGongsmc(gys.getGongsmc());
			LingjGongys ljgys = lingjGongysService.select(bean.getUsercenter(), bean.getGongysdm(), bean.getLingjbh());
			bean.setGongyfe(ljgys == null ? null : ljgys.getGongyfe());
			bean.setUabzlx(ljgys == null ? null : ljgys.getUabzlx());
			bean.setUabzucsl(ljgys == null ? null : ljgys.getUaucgs());
			bean.setUabzuclx(ljgys == null ? null : ljgys.getUcbzlx());
			bean.setUabzucrl(ljgys == null ? null : ljgys.getUcrl());
			//xh 10607添加订单明细时判断订包装信息
			if(StringUtils.isBlank(ljgys.getUabzlx())
					|| StringUtils.isBlank(ljgys.getUcbzlx())
					|| null == ljgys.getUaucgs()
					|| null == ljgys.getUcrl()
			)
			{
				setResult("errorMsg", "用户中心" + bean.getUsercenter() + "零件号" + bean.getLingjbh() + "零件包装信息不完整(UA包装类型,UC包装类型,UC容量,UAUC个数)!");
				return AJAX;
			}
			bean.setZhuangt(Const.DINGD_STATUS_YDY);
			// 订货车间
			Lingj lingj = ljService.select(bean.getUsercenter(), bean.getLingjbh());
			if (lingj == null) {
				// yService.saveYicInfo(Const.JISMK_IL_CD, bean.getLingjbh(),
				// Const.CUOWLX_200, "零件未生效");
				setResult("errorMsg", "用户中心" + bean.getUsercenter() + "零件号" + bean.getLingjbh() + "零件不存在或未生效");
				return AJAX;
			}
////////////////////wuyichao  2014-09-15 KD订单取按键目录卸货点前3位 ///////////
		//	if(StringUtils.isNotBlank(gys.getGonghlx()) &&( gys.getGonghlx().equalsIgnoreCase("97X") ||  gys.getGonghlx().equalsIgnoreCase("97D")))
		//	{
		//		if (StringUtils.isBlank(lingj.getAnjmlxhd()) || lingj.getAnjmlxhd().length() < 3) {
		//			setResult("errorMsg", "用户中心" + bean.getUsercenter() + "零件号" + bean.getLingjbh() + "的按件目录卸货点字段为空或长度不足3位");
		//			return AJAX;
		//			
		//		}
		//		bean.setDinghcj(lingj.getAnjmlxhd().substring(0, 3));
		//	}
		//	else
		//	{
				bean.setDinghcj(lingj.getDinghcj());
		//	}
////////////////////wuyichao  2014-09-15 KD订单取按键目录卸货点前3位 ///////////
			bean.setJihyz(lingj.getJihy());
			bean.setLingjmc(lingj.getZhongwmc());  //xh 2015-9-14
			Map<String, String> map = getParams();
		//wuyichao   2014/02/19 发现以下代码查询逻辑有误，添加订单号查询条件
			map.put("dingdh", bean.getDingdh());
		//wuyichao   2014/02/19 发现以下代码查询逻辑有误，添加订单号查询条件
			map.put("gonghlx", bean.getGonghlx());
			map.put("usercneter", bean.getUsercenter());
			map.put("lingjbh", bean.getLingjbh());
		//wuyichao  2014/02/13 发现gongys字段取的是bean.getCangkdm() 修改为bean.getGongysdm()
			map.put("gongysdm", bean.getGongysdm());
		//wuyichao  2014/02/13 发现gongys字段取的是bean.getCangkdm() 修改为bean.getGongysdm()
			map.put("cangkdm", bean.getCangkdm());
			map.put("jiaofrq", bean.getJiaofrq());
			if (dingdmxService.queryListMx(map).size() != 0) {
				setResult("errorMsg", result + ":零件号" + bean.getLingjbh() + "交付日期" + bean.getJiaofrq());
				return AJAX;
			}
			
///////////wuyichao 针对周期临时订单进行修改，修改内容为利用WTC服务，将订单明细信息传入执行层，查看该信息是否满足执行层要货令的拆分条件
			/*Map<String, Object> wtcMap = new HashMap<String, Object>();
			wtcMap.put("usercneter", bean.getUsercenter());
			wtcMap.put("lingjbh", bean.getLingjbh());
			wtcMap.put("gongysdm", bean.getGongysdm());
			wtcMap.put("gonghms", bean.getGonghlx());
			wtcMap.put("xiaohd", bean.getXiaohd());
			wtcMap.put("cangkbh", bean.getCangkdm());
			wtcMap.put("zickbh", "");
			wtcMap.put("xiehzt", bean.getXiehzt());
			try
			{
				WtcResponse wtcResponse = callWtc(bean.getUsercenter(), "Y0201", wtcMap);
				if (!wtcResponse.get("response").equals(Const.WTC_SUSSCESS)) {
						setResult("errorMsg", ":零件号" + bean.getLingjbh() + "未能满足执行层拆分要货令条件,该零件部分信息可能于当天维护,请于明天操作!");
						return AJAX;
				}
			}
			catch (Exception e) {
				setResult("errorMsg", "调用WTC服务失败,请联系管理人员!");
				e.printStackTrace();
				log.error("调用WTC服务失败", e);
				return AJAX;
			}*/
///////////wuyichao 针对周期临时订单进行修改，修改内容为利用WTC服务，将订单明细信息传入执行层，查看该信息是否满足执行层要货令的拆分条件
		}

		try {
			//xh 10607新增订单明细时先判断订单状态
			int zhuangt=dingdService.queryDingdzt(list.get(0).getDingdh());
			if(zhuangt ==-99){
				setResult("errorMsg", "该订单不存在或状态有误，请查证！");
			   return AJAX;
			}else if(zhuangt >3){
				setResult("errorMsg", "只能对已定义、制作中、待生效、拒绝的订单进行操作！请检查订单状态！");
			   return AJAX;
			 }
			//PJ订单新增明细   计算最早 最晚到货时间 交付时间  发运日期   xh  0715
			 List mxlist=new ArrayList();
			 for(Dingdmx mx:list){
				if((mx.getGonghlx().equals("PJ")||mx.getGonghlx().equals("VJ"))&&mx.getLeix().equals("9")){
					Map map=new HashMap();
					map.put("usercenter", mx.getUsercenter());
					map.put("xiehztbh",mx.getXiehzt());
					map.put("shengcxbh", mx.getXiehzt().substring(0, 4));
					//查询运输时刻的发运周期
					map.put("lingjbh", mx.getLingjbh());
					map.put("gongysbh", mx.getGongysdm());
					
					Xiehzt xiehzt = anxMaoxqService.queryXiehztObject(map);
					if (xiehzt == null) {
						
					}else{
						BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
					    map.put("juedsk", mx.getJiaofrq());
						map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));//允许提前到货时间
						String zuizdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));
						if(StringUtils.isBlank(zuizdhsj)){
							zuizdhsj=mx.getJiaofrq();
						}
						mx.setZuizdhsj(zuizdhsj);
						mx.setZuiwdhsj(mx.getJiaofrq());
					}
					map.remove("shengcxbh");
					map.put("waibms", mx.getGonghlx());
					map.put("xiehzt",mx.getXiehzt());
					
					if (mx.getFayrq() == null || mx.getFayrq().equals("")) {
						List<Wullj> wulljxx =ilOrderService.queryWulljxx(map);
						if(wulljxx.size()>0){
							 Wullj wullj= wulljxx.get(0);
							 BigDecimal yunszq = (wullj.getYunszq()).multiply(BigDecimal.valueOf(24*60));
							 String faysj=DateUtil.DateSubtractMinutes(mx.getJiaofrq(),yunszq.intValue());
							 mx.setFayrq(faysj);
						}	
					}else {
						mx.setFaysj(mx.getFayrq());
					}
			    }
				if (mx.getFayrq() == null || mx.getFayrq().equals("")) {
				}else {
					mx.setFaysj(mx.getFayrq());
				}
				 mxlist.add(mx);
			 }
			this.dingdmxService.doInsert(mxlist);
			this.dingdService.updateDingdzt(list.get(0).getDingdh());
			result = "新增成功！";
			setResult("msg", result);
		} catch (Exception e) {
			log.error("临时周期订单明细新增失败", e);
			setResult("errorMsg", "临时订单明细新增失败");
		}
		return AJAX;
	}

	/**
	 * 判断输入的订货仓库是否存在
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String validateDindhck() {
		Map<String, String> map = getParams();
		map.put("dinghck", map.get("gongysdm"));
		List ls = wulljService.queryWullj(map);
		if (ls.size() == 0) {
			setResult("msg", "未在物流路径中找到订货仓库或线边仓库");
		}
		return AJAX;
	}

	public String getRonglvalidate(@Param LingjGongys bean) {
		List<LingjGongys> ljgongys = lingjGongysService.query(bean);
		Lingj lingj = ljService.queryObject(bean.getLingjbh(), bean.getUsercenter());
		if (null != lingj) {
			setResult("jhy", lingj.getJihy());
			setResult("danw", lingj.getDanw());
		}
		if (ljgongys.size() == 0) {
			setResult("flag", "零件供应商中没有该零件信息");
		} else {
			setResult("uabzuclx", ljgongys.get(0).getUcbzlx());
			setResult("uabzucrl", ljgongys.get(0).getUcrl());
			setResult("uabzucsl", ljgongys.get(0).getUaucgs());
			setResult("uabzlx", ljgongys.get(0).getUabzlx());
			if (ljgongys.get(0).getUcrl() != null && ljgongys.get(0).getUaucgs() != null) {
				setResult("uabzrl", ljgongys.get(0).getUcrl().multiply(ljgongys.get(0).getUaucgs()));
			}
		}
		return AJAX;
	}
	
	
	
	
	
	////////////////////wuyichao 2014-10-27 IL临时订单导入//////////////////////
	/**
	 * 导出模板
	 */
	public String expMuban() {

		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		Map<String, String> message = new HashMap<String, String>();
		boolean flag = exp(reqResponse);
		if (!flag) {
			String result = "导出文件出错！";
			message.put("message", result);
			setResult("result", message);
		}
		return AJAX;
	}
	
	
	private boolean exp(HttpServletResponse response)
	{
		final String USERCENTER = "USERCENTER";
		final String GONGYSBH = "GONGYSBH";
		final String LINGJBH = "LINGJBH";
		final String GONGHLX = "GONGHLX";
		final String CANGKBH = "CANGKBH";
		final String LINGJSL = "LINGJSL";
		final String YAOHSJ = "YAOHSJ";
		final String SHIFJD = "SHIFJD";
		final String SHIFFSGHS = "SHIFFSGHS";
		final String FAYSJ = "FAYSJ";
		WritableFont fontRed = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.RED);  
        WritableCellFormat cellFormatRed = new WritableCellFormat(fontRed); 
      
		try {
			cellFormatRed.setWrap(true);  
		    cellFormatRed.setAlignment(Alignment.CENTRE);  
	        cellFormatRed.setVerticalAlignment(VerticalAlignment.CENTRE);  
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			//mantis:0013187 by CSY 20170215
			String fileName = "周期临时订单导入模板";  
			//fileName = new String(fileName.getBytes(),"iso-8859-1"); 
			response.setHeader("Content-Disposition", new String(("attachment; filename=" + fileName).getBytes("gb2312"), "ISO-8859-1")+".xls");//
			//response.setHeader("Content-disposition", "attachment; filename=周期临时订单导入模板.xls");// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型

			WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
			String tmptitle = "Il国产件临时订单导入"; // 标题
			WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称
			wsheet.getSettings().setDefaultColumnWidth(20);
			// 设置excel标题
			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			WritableCellFormat wcfFC = new WritableCellFormat(wfont);
			// wcfFC.setBackground(Colour.AQUA);
			wsheet.addCell(new Label(4, 0, tmptitle, wcfFC));
			wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			wcfFC = new WritableCellFormat(wfont);
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put(USERCENTER, "用户中心\n(不分大小写)");
			map.put(GONGYSBH, "供应商编号\n(不分大小写)");
			map.put(LINGJBH, "零件编号\n(不分大小写)");
			map.put(GONGHLX, "供货类型\n(PP,PS,PJ,VJ,不分大小写)");
			map.put(CANGKBH, "仓库编号\n(不分大小写)");
			map.put(LINGJSL, "零件数量\n(整数最大10位,小数最大3位)");
			map.put(YAOHSJ, "要货时间 \n(年-月-日)");
			map.put(SHIFJD, "是否既定 \n(否,是)");
			map.put(SHIFFSGHS, "是否发送供应商\n(否,是)");
			map.put(FAYSJ, "发运时间\n(年-月-日)");
			
			Map<String, String> data = new LinkedHashMap<String, String>();
			data.put(USERCENTER, "UL");
			data.put(GONGYSBH, "XXXXXXXXXX");
			data.put(LINGJBH, "XXXXXXXXXX");
			data.put(GONGHLX, "PP");
			data.put(CANGKBH, "XXX");
			data.put(LINGJSL, "0");
			data.put(YAOHSJ, "1991-1-1");
			data.put(SHIFJD, "是");
			data.put(SHIFFSGHS, "是");
			data.put(FAYSJ, "1991-1-1");
			
			
			jxl.write.DateFormat df=new jxl.write.DateFormat("yyyy-mm-dd");  
	        jxl.write.WritableCellFormat wcfDF=new jxl.write.WritableCellFormat(df);  
			CellView cellView = new CellView();
			cellView.setAutosize(true);
			cellView.setFormat(wcfDF);
			wsheet.setColumnView(6, cellView);
			
			jxl.write.DateFormat dfFY=new jxl.write.DateFormat("yyyy-MM-dd HH:mm");  
	        jxl.write.WritableCellFormat wcfFY=new jxl.write.WritableCellFormat(dfFY);  
			CellView cellViewFY = new CellView();
			cellViewFY.setAutosize(true);
			cellViewFY.setFormat(wcfFY);
			wsheet.setColumnView(9, cellViewFY);
			
			// 开始生成主体内容
			Set<Map.Entry<String, String>> set = map.entrySet();
			int temp = 0;
			for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				wsheet.addCell(new Label(temp++, 1, entry.getValue(),cellFormatRed));
			}
			temp = 0;// 向excel单元格里写数据
			int i = 0;
			 
			for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				Object obj = data.get(entry.getKey());
				String content = obj == null ? "" : String.valueOf(obj);
				if (temp != 6 && temp != 9) {
					wsheet.addCell(new Label(temp++, i + 2, content));
				}else {
					DateTime dateCell = new DateTime(temp++, i + 2, new Date(), wcfFY );
					wsheet.addCell(dateCell);
				}
			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 导入临时订单
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public String impIlorderTemp() throws IOException {
		HttpServletRequest req = ActionContext.getActionContext().getRequest();
		HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
		PrintWriter out = null;
		out = reqResponse.getWriter();
		reqResponse.setContentType("text/html");
		reqResponse.setCharacterEncoding("UTF-8");
		StringBuffer sbResult =  new StringBuffer();
		sbResult.append("<script>parent.callback(\"");
		Map<String, String> map = getParams();
		map.put("jsrq", CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD));// 计算日期
		map.put("username", AuthorityUtils.getSecurityUser().getUsername());// 设置用户姓名
		map.put("jiscldm", Const.JISMK_IL_TEMP_CD);// 计算处理代码：国产件临时订单（30）
		if (jiscclssz.checkState(map)) {
			sbResult.append("有导入正在进行,请稍后再计算!");
			sbResult.append("\")</script>");
			log.info(sbResult.toString());
			if (out != null)
			{
				out.print(sbResult.toString());
				out.flush();
				out.close();
			}
		}
		else
		{
			jiscclssz.updateState(map, Const.JSZT_EXECUTING);
			String fullFilePath = "";
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
					jiscclssz.updateState(map, Const.JSZT_SURE);
				}
			}
			try {
				log.info(fullFilePath);
				Object result = imp(fullFilePath);
				if(null == result || result instanceof java.lang.String)
				{
					sbResult.append(result);
				}
				else
				{
					boolean flag = true;
					List<Dingdmx> dingdmxs = (List<Dingdmx>) result;
					if(null != dingdmxs && dingdmxs.size() > 0)
					{
						flag = ilOrderService.impTempDingd(dingdmxs);
						if(flag)
						{
							sbResult.append("导入文件成功!");
						}
						else
						{
							sbResult.append("导入文件失败!");
						}
					}
				}
				sbResult.append("\")</script>");
				log.info(sbResult.toString());
				out.print(sbResult.toString());
				out.flush();
			} catch (Exception e) {
				log.error(e.getMessage());
				out.println("<script>parent.callback('导入文件出错！ ')</script>");
				out.flush();
			} finally {
				jiscclssz.updateState(map, Const.JSZT_SURE);
				if (out != null)
				{
					out.close();
				}
			}
		}
		return AJAX;
	}

	private Object imp(String fullFilePath) throws BiffException, IOException
	{
		final String ERROR_ROW_MAX = "导入文件的数据大于500条!";
		final String ERROR_ROW_NULL = "该文件为空文件,无导入数据!";
		final int ROW_MAX = 503;
		// 读入文件流
		InputStream is = new FileInputStream(new File(fullFilePath));
		// 取得工作薄
		jxl.Workbook wb = Workbook.getWorkbook(is);
		// 取得工作表
		jxl.Sheet sheet = wb.getSheet(0);
		// 行数、列数
		int rows = sheet.getRows();
		if(rows > ROW_MAX)
		{
			return ERROR_ROW_MAX;
		}
		int columns = sheet.getColumns();
		List<Dingdmx> dingdmxs = new ArrayList<Dingdmx>();
		Map<String,Dingdmx> dingdmxMap = new HashMap<String, Dingdmx>();
		Map<String,String> wtcCacheMap = new HashMap<String, String>();
		Map<String,String> usercenterMap = dingdmxService.getUsercenterMap();
		Map<String,Lingj> lingjCacheMap = new HashMap<String, Lingj>();
		Map<String,Gongys> gongysCacheMap = new HashMap<String, Gongys>();
		Map<String,LingjGongys> lingjGongysCacheMap = new HashMap<String, LingjGongys>();
		Map<String,Wullj> wulljCacheMap = new HashMap<String, Wullj>();
		Map<String,String> nianzqCacheMap = new HashMap<String, String>();
		Dingdmx data = null;
		Dingdmx flagData = null;
		String username = getUserInfo().getUsername();
		String time = CommonFun.getJavaTime();
		String error = null;
		TimeZone zone = TimeZone.getTimeZone("GMT");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setTimeZone(zone);
		SimpleDateFormat formatter = null;
		//导入数据,效验
		for (int i = 2; i < rows; i++)
		{
			data = new Dingdmx();
			data.setCreator(username);
			data.setEditor(username);
			data.setCreate_time(time);
			data.setEdit_time(time);
			data.setZhuangt(Const.DINGD_STATUS_ZZZ);
			data.setActive(Const.ACTIVE_1);
			for (int j = 0; j < columns; j++)
			{
				switch (j) 
				{
					case 0:
						// 读取用户中心
						data.setUsercenter(sheet.getCell(j, i).getContents().trim().toUpperCase());
						break;
					case 1:
						//供应商编号
						data.setGongysdm(sheet.getCell(j, i).getContents().trim().toUpperCase());
						break;
					case 2:
						//零件编号
						data.setLingjbh(sheet.getCell(j, i).getContents().trim().toUpperCase());
						break;
					case 3:
						//供货模式
						data.setGonghlx(sheet.getCell(j, i).getContents().trim().toUpperCase());
						break;
					case 4:
						//仓库代码
						data.setCangkdm(sheet.getCell(j, i).getContents().trim().toUpperCase());
						break;
					case 5:
						//零件数量
						data.setShulStr(sheet.getCell(j, i).getContents().trim());
						break;
					case 6:
						//要货日期
						String jiaofrq = "";
						formatter = null;
						try {
							Cell jiaofCell = sheet.getCell(j, i);
							if (jiaofCell.getContents() == null || jiaofCell.getContents().equals("")) {
								jiaofrq = "error";
							}else {
								if(jiaofCell.getType()==CellType.DATE){ 
									DateCell dc = (DateCell)jiaofCell;         
									jiaofrq = sdf.format(dc.getDate()); 
								}else {
									jiaofrq = sheet.getCell(j, i).getContents().trim();
									
									if(StringUtils.isNotBlank(jiaofrq))
									{
										if(jiaofrq.length() >= 16){   
											if(jiaofrq.contains("-")){
												formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
												jiaofrq = formatter.format(formatter.parse(jiaofrq));
											}else {
												formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm");
												jiaofrq = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(formatter.parse(jiaofrq));
											}
										}else{
											formatter = new SimpleDateFormat("yyyy-MM-dd");
											jiaofrq = CommonFun.getJavaTime(formatter.parse(jiaofrq));
										}
									} 
								}
							}
						} catch (Exception e) {
							jiaofrq = "error";
						}
						
						data.setJiaofrq(jiaofrq);
						break;
					case 7:
						//是否既定
						data.setLeixStr(sheet.getCell(j, i).getContents().trim());
						break;
					case 8:
						//是否既定
						data.setFasgysStr(sheet.getCell(j, i).getContents().trim());
						break;
					case 9:
						//发运日期 0013092: 临时订单上传增加发运时间字段 by csy 20170105
						String fayrq = "";
						formatter = null;
						try {
							Cell fayrqCell = sheet.getCell(j, i);
							if (fayrqCell.getContents() == null || fayrqCell.getContents().equals("")) {
								fayrq = "error";
							}else {
								if(fayrqCell.getType()==CellType.DATE){ 
									DateCell dc = (DateCell)fayrqCell;         
									fayrq = sdf.format(dc.getDate()); 
								}else {
									fayrq = sheet.getCell(j, i).getContents().trim();
									
									if(StringUtils.isNotBlank(fayrq))
									{
										if(fayrq.length() >= 16){   
											if(fayrq.contains("-")){
												formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
												fayrq = formatter.format(formatter.parse(fayrq));
											}else {
												formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm");
												fayrq = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(formatter.parse(fayrq));
											}
										}else{
											formatter = new SimpleDateFormat("yyyy-MM-dd");
											fayrq = CommonFun.getJavaTime(formatter.parse(fayrq));
										}
									} 
								}
							}
						} catch (Exception e) {
							fayrq = "error";
						}
						data.setFayrq(fayrq);
						break;
				}
			}
			///////////////////////导入数据效验///////////
			error = check(data,i+1,usercenterMap,lingjCacheMap,gongysCacheMap,lingjGongysCacheMap,wulljCacheMap,nianzqCacheMap,wtcCacheMap);
			///////////////////////导入数据效验///////////
			if(StringUtils.isNotBlank(error))
			{
				return error;
			}
			
			flagData = dingdmxMap.get(data.getUsercenter()+data.getGongysdm()+data.getLingjbh()+data.getGonghlx()+data.getCangkdm() + data.getJiaofrq() + data.getLeixStr() + data.getFasgysStr());
			if(null != flagData)
			{
				data.setShul(data.getShul().add(flagData.getShul()));
			}
			dingdmxMap.put(data.getUsercenter()+data.getGongysdm()+data.getLingjbh()+data.getGonghlx()+data.getCangkdm() + data.getJiaofrq() + data.getLeixStr() + data.getFasgysStr(), data);
		}
		dingdmxs.addAll(dingdmxMap.values());
		return dingdmxs.size() == 0 ? ERROR_ROW_NULL : dingdmxs;
	}

	private String check(Dingdmx data,int i,Map<String,String> usercenterMap,Map<String,Lingj> lingjCacheMap,Map<String,Gongys> gongysCacheMap,Map<String,LingjGongys> lingjGongysCacheMap,Map<String,Wullj> wulljCacheMap,Map<String,String> nianzqCacheMap,Map<String,String> wtcCacheMap)
	{
		StringBuffer sb = new StringBuffer("第");
		sb.append(i).append("行:");
		if(StringUtils.isBlank(data.getUsercenter()) 
				|| StringUtils.isBlank(data.getGongysdm()) 
				|| StringUtils.isBlank(data.getCangkdm()) 
				|| StringUtils.isBlank(data.getLingjbh()) 
				|| StringUtils.isBlank(data.getGonghlx()) 
				|| StringUtils.isBlank(data.getShulStr()) 
				|| StringUtils.isBlank(data.getJiaofrq()) 
				|| StringUtils.isBlank(data.getLeixStr()) 
				|| StringUtils.isBlank(data.getFasgysStr()) 
		)
		{
			sb.append("用户中心,供应商编号,零件编号,供货类型,仓库代码,零件数量,要货时间 ,是否既定 ,是否发送供应商为必填项!");
			return sb.toString();
		}
		if(StringUtils.isBlank(usercenterMap.get(data.getUsercenter())))
		{
			sb.append("用户中心不存在或已失效!");
			return sb.toString();
		}
		if(!(data.getGonghlx().equalsIgnoreCase("PP") || data.getGonghlx().equalsIgnoreCase("PS") || data.getGonghlx().equalsIgnoreCase("PJ")|| data.getGonghlx().equalsIgnoreCase("VJ")))
		{
			sb.append("供货类型为(PP,PS,PJ,VJ)!");
			return sb.toString();
		}
		try
		{
			double shul = Double.valueOf(data.getShulStr());
			if(!data.getShulStr().matches("^[0-9]{1,10}(?:\\.[0-9]{1,3})?$|^0\\.[0-9]{1,3}$"))
			{
				sb.append("零件数量的取值范围为(0  ~~ 9999999999.999)!");
				return sb.toString();
			}
			data.setShul(new BigDecimal(shul));
			data.setJissl(data.getShul());
		}
		catch (Exception e) 
		{
			sb.append("零件数量的取值范围为(0  ~~ 9999999999.999)!");
			return sb.toString();
		}
		//0013092: 临时订单上传增加发运时间字段 by csy 20170105
		if (data.getFayrq().equals("error"))
		{
			sb.append("发运时间格式不正确(yyyy-MM-dd)!");
			return sb.toString();
		}
		//交付时间
		if (data.getJiaofrq().equals("error"))
		{
			sb.append("要货时间格式不正确(yyyy-MM-dd)!");
			return sb.toString();
		}
		if(data.getGonghlx().equals("PJ") || data.getGonghlx().equals("VJ")){
			if((data.getJiaofrq()).length()!=16){
				sb.append("PJ、VJ模式的要货时间格式不正确(yyyy-MM-dd hh:mm)!");
				return sb.toString();
			}
		}
		if (data.getJiaofrq().substring(0, 10).compareTo(data.getCreate_time().substring(0, 10)) < 0) 
		{
			sb.append("要货时间不能小于当前时间!");
			return sb.toString();
		}
		else 
		{
			String gongyzq = nianzqCacheMap.get(data.getJiaofrq());
			if(StringUtils.isBlank(gongyzq))
			{
				Gongyzq bean = gongyzqService.queryGongyzqbyRq(data.getJiaofrq());
				if(null == bean || StringUtils.isBlank(bean.getGongyzq()))
				{
					sb.append("要货时间没有找到对应的工业周期!");
					return sb.toString();
				}
				gongyzq = bean.getGongyzq();
				nianzqCacheMap.put(data.getJiaofrq(), gongyzq);
			}
			data.setGongyzq(gongyzq);
		}
		if(data.getLeixStr().equalsIgnoreCase("是"))
		{
			data.setLeix(Const.SHIFOUSHIJIDING);
		}
		else if(data.getLeixStr().equalsIgnoreCase("否"))
		{
			data.setLeix(Const.SHIFOUSHIYUGAO);
		}
		else
		{
			sb.append("是否既定的赋值规则为(否,是)!");
			return sb.toString();
		}
		if(data.getFasgysStr().equalsIgnoreCase("是"))
		{
			data.setFasgysStr(Const.ACTIVE_1);
		}
		else if(data.getFasgysStr().equalsIgnoreCase("否"))
		{
			data.setFasgysStr(Const.ACTIVE_0);
		}
		else
		{
			sb.append("是否发送供应商的赋值规则为(否,是)!");
			return sb.toString();
		}
		
		Lingj lingj = getLingj(data, lingjCacheMap);
		if(null == lingj)
		{
			sb.append("零件编号不存在或已失效!");
			return sb.toString();
		}
		else
		{
			data.setDanw(lingj.getDanw());
			data.setJihyz(lingj.getJihy());
			data.setDinghcj(lingj.getDinghcj());
		}
		Gongys gongys = getGongys(data,gongysCacheMap);
		if(null == gongys)
		{
			sb.append("供应商编号不存在或已失效!");
			return sb.toString();
		}
		else
		{
			data.setGongyslx(gongys.getLeix());
			data.setGongsmc(gongys.getGongsmc());
		}
		LingjGongys lingjGongys = getLingjgys(data,lingjGongysCacheMap);
		if(null == lingjGongys)
		{
			sb.append("没有找到零件与供应商的关联关系!");
			return sb.toString();
		}
		else
		{
			data.setGongyfe(lingjGongys.getGongyfe());
			if(StringUtils.isBlank(lingjGongys.getUabzlx())
					|| StringUtils.isBlank(lingjGongys.getUcbzlx())
					|| null == lingjGongys.getUaucgs()
					|| null == lingjGongys.getUcrl()
			)
			{
				sb.append("导入数据对应的包装信息不完整(UA包装类型,UC包装类型,UC容量,UAUC个数)!");
				return sb.toString();
			}
			data.setUabzlx(lingjGongys.getUabzlx());
			data.setUabzucsl(lingjGongys.getUaucgs());
			data.setUabzuclx(lingjGongys.getUcbzlx());
			data.setUabzucrl(lingjGongys.getUcrl());
			data.setUabzrl(lingjGongys.getUcrl().multiply(lingjGongys.getUaucgs()));
		}
		Wullj wullj = getWullj(data,wulljCacheMap);
		if(null == wullj)
		{
			sb.append("没有找到与仓库对应的物流路径!");
			return sb.toString();
		}
		else if(StringUtils.isBlank(wullj.getXiehztbh()))
		{
			sb.append("与仓库对应的物流路径对应的卸货站台为空!");
			return sb.toString();
		}
		else
		{
			data.setXiehzt(wullj.getXiehztbh());
			data.setGcbh(wullj.getGcbh());
			data.setLujdm(wullj.getLujbh());
		}
		/*if(StringUtils.isBlank(wtcCacheMap.get(data.getUsercenter() + data.getLingjbh() +  data.getGongysdm() + data.getCangkdm() + data.getGonghlx() )))
		{
			Map<String, Object> wtcMap = new HashMap<String, Object>();
			wtcMap.put("usercneter", data.getUsercenter());
			wtcMap.put("lingjbh", data.getLingjbh());
			wtcMap.put("gongysdm", data.getGongysdm());
			wtcMap.put("gonghms", data.getGonghlx());
			wtcMap.put("xiaohd", data.getXiaohd());
			wtcMap.put("cangkbh", data.getCangkdm());
			wtcMap.put("zickbh", "");
			wtcMap.put("xiehzt", data.getXiehzt());
			try
			{
				WtcResponse wtcResponse = callWtc(data.getUsercenter(), "Y0201", wtcMap);
				if (!wtcResponse.get("response").equals(Const.WTC_SUSSCESS)) {
						sb.append("未能满足执行层拆分要货令条件,该零件部分信息可能于当天维护,请于明天操作!");
						return sb.toString();
				}
				wtcCacheMap.put(data.getUsercenter() + data.getLingjbh() +  data.getGongysdm() + data.getCangkdm() + data.getGonghlx() , "ok");
			}
			catch (Exception e) {
				sb.append("调用WTC服务失败,请联系管理人员!");
				e.printStackTrace();
				log.error("调用WTC服务失败", e);
				return sb.toString();
			}
		}*/
		return null;
	}
	
	private Wullj getWullj(Dingdmx data, Map<String, Wullj> wulljCacheMap) 
	{
		Wullj wullj = null;
		Map<String,String> params = new HashMap<String, String>();
		wullj = wulljCacheMap.get(data.getUsercenter() + data.getLingjbh() + data.getGongysdm() + data.getCangkdm() + data.getGonghlx());
		if(null == wullj)
		{
			params.put("usercenter", data.getUsercenter());
			params.put("lingjbh", data.getLingjbh());
			params.put("gongysbh", data.getGongysdm());
			params.put("mudd", data.getCangkdm());
			params.put("waibms", data.getGonghlx());
			List<Wullj> wulljs = wulljService.queryWulljList(params);
			if(null != wulljs && wulljs.size() > 0)
			{
				wullj = wulljs.get(0);
				wulljCacheMap.put(data.getUsercenter() + data.getLingjbh() + data.getGongysdm() + data.getCangkdm() + data.getGonghlx(), wullj);
			}
		}
		return wullj;
	}

	private LingjGongys getLingjgys(Dingdmx data,Map<String, LingjGongys> lingjGongysCacheMap)
	{
		LingjGongys lingjGongys = null;
		lingjGongys = lingjGongysCacheMap.get(data.getUsercenter() + data.getLingjbh() + data.getGongysdm());
		if(null == lingjGongys)
		{
			lingjGongys = lingjGongysService.select(data.getUsercenter(), data.getGongysdm(), data.getLingjbh());
			if(null != lingjGongys)
			{
				lingjGongysCacheMap.put(data.getUsercenter() + data.getLingjbh() + data.getGongysdm(), lingjGongys);
			}
		}
		return lingjGongys;
	}

	private Gongys getGongys(Dingdmx data, Map<String, Gongys> gongysCacheMap)
	{
		Gongys gongys = null;
		gongys = gongysCacheMap.get(data.getUsercenter() + data.getGongysdm());
		if(null == gongys)
		{
			Map<String,String> param = new HashMap<String, String>();
			param.put("gcbh", data.getGongysdm());
			param.put("usercenter", data.getUsercenter());
			gongys =  gongysService.queryGongys(param);
			if(null != gongys)
			{
				gongysCacheMap.put(data.getUsercenter() + data.getGongysdm(), gongys);
			}
		}
		return gongys;
	}

	private Lingj getLingj(Dingdmx data,Map<String,Lingj> lingjCacheMap)
	{
		Lingj lingj = null;
		lingj = lingjCacheMap.get(data.getUsercenter() + data.getLingjbh());
		if(null == lingj)
		{
			lingj = ljService.select(data.getUsercenter(), data.getLingjbh());
			if(null != lingj)
			{
				lingjCacheMap.put(data.getUsercenter()+ data.getLingjbh(), lingj);
			}
		}
		return lingj;
	}
	////////////////////wuyichao 2014-10-27 IL临时订单导入//////////////////////
}
