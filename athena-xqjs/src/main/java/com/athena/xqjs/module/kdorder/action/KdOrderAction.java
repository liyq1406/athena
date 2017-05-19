package com.athena.xqjs.module.kdorder.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Gongyzq;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiaohcyssk;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.common.Yunssk;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.entity.kdorder.Kdmxqhzc;
import com.athena.xqjs.module.anxorder.service.AnxMaoxqService;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.common.service.CalendarCenterService;
import com.athena.xqjs.module.common.service.GongysService;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.common.service.LingjGongysService;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.common.service.WulljService;
import com.athena.xqjs.module.common.service.XiaohcysskService;
import com.athena.xqjs.module.common.service.XqjsLingjckService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.DingdljService;
import com.athena.xqjs.module.ilorder.service.DingdmxService;
import com.athena.xqjs.module.ilorder.service.GongyzqService;
import com.athena.xqjs.module.ilorder.service.IleditAndEffectService;
import com.athena.xqjs.module.ilorder.service.MaoxqService;
import com.athena.xqjs.module.kdorder.service.KdOrderService;
import com.athena.xqjs.module.kdorder.service.KdmxqhzcService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：KDAction
 * <p>
 * 类描述： kd订单
 * </p>
 * 创建人：李明
 * <p>
 * 创建时间：2012-1-12
 * </p>
 * 
 * @version
 * 
 */

@Component
public class KdOrderAction extends BaseWtcAction {
	@Inject
	private KdmxqhzcService kdmxqhzcService;// KD毛需求汇总参考系service
	@Inject
	private DownLoadServices  downloadServices;
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 注入DingdService
	 */
	@Inject
	private DingdService dingdService;

	/**
	 * 注入DingdljService
	 */
	@Inject
	private DingdljService dingdljService;

	/**
	 * 注入DingdmxService
	 */
	@Inject
	private DingdmxService dingdmxService;

	@Inject
	private CalendarCenterService calendarCenterService;
	/**
	 * 注入零件供应商Service
	 */
	@Inject
	private LingjGongysService lingjGongysService;
	@Inject
	private LingjService lingjService;
	@Inject
	private WulljService wulljService;// 柔性比例service
	@Inject
	private GongysService gService;
	/**
	 * kdService
	 */
	@Inject
	private KdOrderService kdOrderService;
	@Inject
	private GongyzqService gongyzqService;
	@Inject
	private DownLoadServices downLoadServices;
	@Inject
	private MaoxqService maoxqservice;

	@Inject
	private LingjxhdService lingjxhdService;

	@Inject
	private XiaohcysskService xService;

	@Inject
	private AnxMaoxqService anxMaoxqService;

	@Inject
	private XqjsLingjckService ljck;
	
	
	@Inject
	private IleditAndEffectService ileditAndEffectService;
	
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;

	private final Logger log = Logger.getLogger(KdOrderAction.class);
	@Inject
	private UserOperLog userlog;
	/**
	 * getUserInfo获取用户信息方法
	 * 
	 * @return object userInfo
	 */
	public LoginUser getUserInfo() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}

	/**
	 * 获取到用户中心
	 * **/
	public String getUsercenters() {
		String usercenter = "";
		// 获取到该组里面所有的用户中心
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		List<String> allManager = loginUser.getUcList();
		Set<String> all = new HashSet<String>();
		for (String manager : allManager) {
			all.add(manager);
		}
		for (String obj : all) {
			usercenter += obj + ",";
		}
		return usercenter;
	}

	/**
	 * 订单定义页面初始化，执行跳转
	 */
	public String execute() {
		setResult("whr", this.getUserInfo().getUsername());
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "success";
	}

	/**
	 * @：订单定义：查询kd订单
	 * @：李明
	 * @：2012-1-12
	 */
	public String queryListDingd(@Param Dingd bean) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单定义", "查询订单开始");
		Map<String, String> searchMap = getParams();
		searchMap.put("_isJL", "N");
		setResult("result", this.dingdService.kdQueryList(bean, searchMap));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单定义", "查询订单结束");
		return AJAX;
	}

	/**
	 * @：订单定义：kd特殊订单插入操作
	 * @：李明
	 * @：2012-1-12
	 * @param：订单实体
	 */
	public String insertDingdSpecial(@Param Dingd bean) {
		
		Map<String, String> map = new HashMap<String, String>();
		bean.setUsercenter(this.getUsercenters());
		// 首先查找所有的订单，订单号倒序
		
		// 系统生成新的订单号
		String dingdh = this.dingdService.getDingdh(bean.getDingdlx(), bean.getJiszq());
		// 订单号设置到实体类中
		bean.setDingdh(dingdh);
		LingjGongys gys = new LingjGongys();
		gys.setUsercenter(this.getUserInfo().getUsercenter());
		gys.setGongysbh(bean.getGongysdm());
		gys.setDingdnr(bean.getDingdnr());
		// 执行插入操作
		bean.setActive("1");
		bean.setCreate_time(CommonFun.getJavaTime());
		bean.setDingdjssj(CommonFun.getJavaTime());
		bean.setChullx(Const.PP);
		bean.setEditor(this.getUserInfo().getUsername());
		bean.setCreator(this.getUserInfo().getUsername());
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单定义", "插入特殊订单开始");
		boolean flag = this.dingdService.doInsert(bean);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单定义", "插入特殊订单结束");
		if (flag) {
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单定义", "插入特殊订单成功");
			setResult("msg", Const.INSERT_SUCSS);
		} else {
			this.userOperLog.addError(CommonUtil.MODULE_XQJS, "KD订单定义", "插入特殊订单", "KD订单定义：insertDingdSpecial中插入订单操作",
					MessageConst.INSERT_COUNT_0);
			setResult("msg", MessageConst.INSERT_COUNT_0);
		}
		return AJAX;
	}

	/**
	 * @：订单定义：kd普通订单插入操作
	 * @：李明
	 * @：2012-1-12
	 * @param：订单实体
	 */
	public String insertDingdNormal(@Param Dingd bean) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", this.getUserInfo().getUsercenter());
		bean.setUsercenter(this.getUsercenters());
		LingjGongys gys = new LingjGongys();
		gys.setUsercenter(map.get("usercenter"));
		gys.setGongysbh(bean.getGongysdm());
		gys.setDingdnr(bean.getDingdnr());
		bean.setActive(Const.ACTIVE_1);
		bean.setChullx(Const.PP);
		bean.setCreate_time(CommonFun.getJavaTime());
		bean.setEdit_time(CommonFun.getJavaTime());

		bean.setEditor(this.getUserInfo().getUsername());
		bean.setCreator(this.getUserInfo().getUsername());
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单定义", "插入订单开始");
		boolean flag = this.dingdService.doInsert(bean);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单定义", "插入订单结束");
		if (flag) {
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单定义", "插入订单成功");
			setResult("msg", Const.INSERT_SUCSS);
		} else {
			this.userOperLog.addError(CommonUtil.MODULE_XQJS, "KD订单定义", "插入订单", "KD订单定义：insertDingdNormal中插入订单操作",
					MessageConst.INSERT_COUNT_0);
			setResult("msg", MessageConst.INSERT_COUNT_0);
		}
		return AJAX;
	}

	/**
	 * @：订单定义：更新kd订单
	 * @：李明
	 * @：2012-1-12
	 * @参数：list集合
	 */
	public String upDateDingd(@Param("DingdUpdate") ArrayList<Dingd> allDingd) {
		boolean flag = false;
		Map<String, String> maps = new HashMap<String, String>();
		if (!allDingd.isEmpty()) {
			for (Dingd dingd : allDingd) {
				maps.put("dingdh", dingd.getDingdh());
				Dingd bean = this.dingdService.queryDingdByDingdh(maps);
				maps.clear();
				if (bean != null && bean.getDingdzt() != null) {
					if (bean.getDingdzt().equals(new Const().DINGD_STATUS_YDY)) {
						// 订单内容更新到零件供应商
						this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单定义", "更新KD订单开始");
						flag = this.dingdService.doUpdate(dingd);
						this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单定义", "更新KD订单结束");
					}
				}
				if (flag) {
					this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单定义", "更新KD订单");
					setResult("flag", Const.UPDATE_SUCSS);
				} else {
					this.userOperLog.addError(CommonUtil.MODULE_XQJS, "订单定义", "修改订单", "订单定义：修改订单", "修改订单失败");
					setResult("flag", Const.UPDATE_FAIL);
				}
			}
		}
		return AJAX;
	}

	/**
	 * 订单修改及生效页面初始化，执行跳转
	 */
	public String executeUpdate() {
		return "success";
	}

	/**
	 * @：订单生效及修改：查询kd订单
	 * @：李明
	 * @：2012-2-9
	 */
	public String queryListDingdForShengx(@Param Dingd bean) throws Exception {
		Map<String, String> paramMap = getParams();
		String heth = getParam("heth");
		String jislx = getParam("jislx");
		String dingdnr = getParam("dingdnr");
		String beiz = getParam("beiz");
		String result = "";
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单修改及生效：查询", "查询订单开始");
		Map<String, String> message = new HashMap<String, String>();
		if (heth != null && jislx == null) {
			result = "请选择：定单制作时间-结束时间！\n";
		} else if (heth == null && jislx != null) {
			result = "请选择：定单制作时间-起始时间！\n";
		} else if (heth != null && jislx != null && heth.compareTo(jislx) > 0) {
			result = "订单制作时间的起始时间不能大于结束时间！\n";
		} else if (heth != null && jislx != null) {
			String dingdjssj = "dingdjssj between to_date('" + heth + "','yyyy-mm-dd')and to_date('" + jislx
					+ "','yyyy-mm-dd')";
			paramMap.put("dingdjssj", dingdjssj);
		}
		// 生效时间
		if (dingdnr != null && beiz == null) {
			result += "请选择：定单生效时间-结束时间！";
		} else if (dingdnr == null && beiz != null) {
			result += "请选择：定单生效时间-起始时间！";
		} else if (dingdnr != null && beiz != null && dingdnr.compareTo(beiz) > 0) {
			result += "订单定单生效时间的起始时间不能大于结束时间！";
		} else if (dingdnr != null && beiz != null) {
			String dingdsxsj = "dingdsxsj between to_date('" + dingdnr + "','yyyy-mm-dd')and to_date('" + beiz
					+ "','yyyy-mm-dd')";
			paramMap.put("dingdsxsj", dingdsxsj);
		}
		if (result.length() != 0) {
			message.put("message", result);
			setResult("result", message);
		} else {
			setResult("result", this.dingdService.kdQueryListForShengx(bean, paramMap));
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单修改及生效：查询", "查询订单结束");
		return AJAX;
	}

	/**
	 * @：订单生效及修改：查询kd订单
	 * @：李明
	 * @：2012-2-9
	 */
	public String queryListDingdljForMap(@Param Dingdlj bean) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单修改及生效：查询", "查询订单开始");
		setResult("dingdnr", bean.getDingdnr());
		setResult("lingjbh", bean.getLingjbh());
		setResult("dingdh", bean.getDingdh());
		setResult("jihyz", getParam("jihyz") != null ? getParam("jihyz") : getParam("jihyz1"));
		setResult("usercenter", getParam("usercenter") != null ? getParam("usercenter") : getParam("usercenter1"));
		Map map = this.dingdljService.queryAllKDDingdljForMap(bean, getParams());
		setResult("result", map);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单修改及生效：查询", "查询订单结束");
		return AJAX;
	}

	/**
	 * @：订单生效及修改：订单发送
	 * @：李明
	 * @：2012-2-9
	 */
	public String sendOrder(@Param("dingd") ArrayList<Dingd> dingds) {
		boolean flag = false;
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效：订单发送", "订单发送开始");
		if (!dingds.isEmpty()) {
			for (Dingd bean : dingds) {
				bean.setShifyjsyhl(Const.SHIFYJSYHL_Y);
				bean.setEdit_time(CommonFun.getJavaTime());
				flag = this.dingdService.doUpdate(bean);
			}
		}
		if (flag) {
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效：订单发送", "订单发送成功");
			setResult("flag", "订单发送成功");
		} else {
			this.userOperLog.addError(CommonUtil.MODULE_XQJS, "订单修改及生效", "订单发送", "sendOrder", "订单发送失败");
			setResult("flag", "订单发送失败");
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效：订单发送", "订单发送结束");
		return AJAX;
	}

	/**
	 * @：订单生效及修改：跳转
	 * @：李明
	 * @：2012-2-9
	 */
	public String queryDetailOrderLine(@Param Dingdmx bean) {
		String forward = "success";
		setResult("dingdh", getParam("dingdh"));
		setResult("usercenter", getParam("usercenter"));
		setResult("gongysdm", getParam("gongysdm"));
		setResult("lingjbh", getParam("lingjbh"));
		setResult("ids", getParam("ids"));
		setResult("dingdzt", getParam("dingdzt"));
		return forward;
	}

	/**
	 * @：订单生效及修改：查询kd订单行
	 * @：李明
	 * @：2012-2-9
	 */
	public String queryOrderLine(@Param Dingdmx bean) {
		if (getParam("flag") != null && getParam("flag").equals("axbpp")) {
			Map<String, String> maps = getParams();
			String rq = this.axprqfw(getParams());
			if(null!=rq&&!rq.equals("")){
				maps.put("rq", rq);
				Map<String, Object> mapRes = this.dingdmxService.queryDingdmxMap(bean, maps);
				List<Dingdmx> resultList = this.kdOrderService.queryDetailOrderLineService((List<Dingdmx>) mapRes
						.get("rows"));
				mapRes.put("rows", resultList);
				setResult("result", mapRes);
			}else{
				Map<String,String> mesMap = new HashMap<String,String>();
				mesMap.put("message", "工业周期表中没有查询到对应周期的起始和结束日期");
				setResult("result",mesMap);
			}
			
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Dingdmx> all_dingmx = this.dingdmxService.queryDingdmxMap(getParams());
			List<Dingdmx> resultList = this.kdOrderService.queryDetailOrderLineService(all_dingmx);
			map.put("rows", resultList);
			map.put("total", resultList.size());
			setResult("result", map);
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效：查询订单明细", "查询订单行");
		return AJAX;
	}

	/**
	 * 爱信查询明细只能是P0到p2
	 * 
	 * @author NIESY
	 * @param map
	 * @return
	 */
	public String axprqfw(Map<String, String> map) {
		String rq = "";
		List<Dingdlj> ls = this.dingdljService.queryKDDingdljForList(map);
		if (!ls.isEmpty()) {
			Dingdlj lj = ls.get(0);
			int p0zqxh = Integer.parseInt(lj.getP0fyzqxh());
			Gongyzq g0 = gongyzqService.queryGongyzq(String.valueOf(p0zqxh));
			// 实例化日历
			Calendar calendar = new GregorianCalendar();
			// 设置年份
			calendar.set(Calendar.YEAR, Integer.parseInt(lj.getP0fyzqxh().substring(0, 4)));
			// 设置月份
			calendar.set(Calendar.MONTH, Integer.parseInt(lj.getP0fyzqxh().substring(4, 6)) - 1 + 2);
			String p2zqxh = String.valueOf(calendar.get(Calendar.YEAR))
					+ String.format("%02d", (calendar.get(Calendar.MONTH) + 1));
			Gongyzq g2 = gongyzqService.queryGongyzq(p2zqxh);
			if(null!=g2){
				String p0ksrq = g0.getKaissj();
				String p2jsrq = g2.getJiessj();
				rq = "fayrq between to_date('" + p0ksrq + "','yyyy-MM-dd') and to_date('" + p2jsrq + "','yyyy-MM-dd')";
			}
			
		}
		return rq;
	}

	/**
	 * @：订单生效及修改：插入kd订单
	 * @：李明
	 * @：2012-2-9
	 */
	public String insertDingdlj(@Param Dingdlj bean) throws Exception {
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		// 从零件供应商中得到合同号
		LingjGongys gongys = this.lingjGongysService.select(bean.getUsercenter(), getParam("gongysdm"),
				getParam("lingjbh"));
		if (null != gongys&&null!=gongys.getGongyhth()) {
			bean.setHeth(gongys.getGongyhth());
		}
		// =====================插入操作前的数据准备工作开始=====================
		// *******************************关联查询出需要的插入的数据，得到一个全新的实体***********
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单修改及生效：插入订单零件", "插入订单零件开始");
		String jihydm = loginUser.getUsername();
		bean.setDingdh(getParam("dingdh"));
		//bean.setUsercenter(loginUser.getUsercenter());
		bean.setLingjbh(getParam("lingjbh"));
		bean.setGongysdm(getParam("gongysdm"));
		bean.setDingdnr(getParam("dingdnr"));
		bean.setDinghcj(getParam("dinghcj"));
		bean.setFahd(getParam("fahd"));
		bean.setP0sl(new BigDecimal(getParam("p0sl")));
		bean.setP1sl(new BigDecimal(getParam("p1sl")));
		bean.setP2sl(new BigDecimal(getParam("p2sl")));
		if (getParam("p3sl") != null) {
			bean.setP3sl(new BigDecimal(getParam("p3sl")));
			bean.setP4sl(new BigDecimal(getParam("p4sl")));
		}
		bean.setUabzucrl(new BigDecimal(getParam("uabzucrl")));
		bean.setUabzucsl(new BigDecimal(getParam("uabzucsl")));
		bean.setJihyz(getParam("jihyz"));
		bean.setDingdzzsj(getParam("dingdzzsj"));
		bean.setZiyhqrq(getParam("ziyhqrq"));
		bean.setActive("1");
		bean.setBeihzq(new BigDecimal(getParam("beihzq")));
		bean.setFayzq(new BigDecimal(getParam("fayzq")));
		Map<String, String> param = new HashMap<String, String>();
		param.put("usercenter", bean.getUsercenter());
		param.put("gongysdm", bean.getGongysdm());
		param.put("lingjbh", bean.getLingjbh());
		param.put("dingdh", bean.getDingdh());
		List<Dingdmx> mxList = dingdmxService.queryListMx(param);
		if(null != mxList && mxList.size() > 0)
		{
			this.userOperLog.addError(CommonUtil.MODULE_XQJS, "订单修改及生效", "插入订单零件", "insertDingdlj", "插入订单零件失败");
			setResult("msg", "插入失败：插入的零件已存在");
		}
		else
		{
			boolean flag = this.kdOrderService.insertDingdljService(bean, jihydm);
			if (flag) {
				this.dingdService.updateDingdzt(getParam("dingdh"));
				this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效：插入订单零件", "插入订单零件成功");
				setResult("msg", Const.INSERT_SUCSS);
			} else {
				this.userOperLog.addError(CommonUtil.MODULE_XQJS, "订单修改及生效", "插入订单零件", "insertDingdlj", "插入订单零件失败");
				setResult("msg", "插入失败：插入的零件有误");
			}
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单修改及生效：插入订单零件", "插入订单零件结束");
		return AJAX;
	}

	/**
	 * @：删除kd订单
	 * @：李明
	 * @：2012-1-12
	 * @参数：list集合
	 */
	public String deleteDingd(@Param("delete") ArrayList<Dingd> allDingd) {
		Map<String, String> maps = new HashMap<String, String>();
		boolean flag = false;
		if (allDingd.size() > 0.001) {
			for (int i = 0; i < allDingd.size(); i++) {
				Dingd dingd = allDingd.get(i);
				maps.put("dingdh", dingd.getDingdh());
				Dingd bean = this.dingdService.queryDingdByDingdh(maps);
				maps.clear();
				if (bean != null && bean.getDingdzt() != null) {
					if (bean.getDingdzt().equals(Const.DINGD_STATUS_YDY)) {
						// 更新订单零件
						this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单修改及生效：删除订单", "删除订单开始");
						flag = this.dingdService.doRemove(dingd);
						this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单修改及生效：删除订单", "删除订单结束");
					}
				}
				if (flag) {
					this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单修改及生效：删除订单", "删除订单成功");
					setResult("flag", Const.DELETE_SUCSS);
				} else {
					this.userOperLog.addError(CommonUtil.MODULE_XQJS, "KD订单修改及生效", "删除订单", "deleteDingd", "删除订单失败");
					setResult("flag", Const.DELETE_FAIL);
				}
			}
		}
		return AJAX;
	}

	/**
	 * @:kd订单修改及生效：通过零件号获取到容量信息以及发货地和订货车间信息
	 * @throws Exception
	 * @：李明
	 * @：2012-1-12
	 * @参数：list集合
	 */
	public String getRongl(@Param LingjGongys bean) {
		LingjGongys ljgongys = this.lingjGongysService.select(bean.getUsercenter(), bean.getGongysbh(), bean.getLingjbh());
		Lingj lingj = this.lingjService.queryObject(bean.getLingjbh(), bean.getUsercenter());
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("gongysbh", bean.getGongysbh());
		List<Wullj> wullj = null;
		String waibms = getParam("chullx");
		String zhizlx = getParam("zhizlx");
		if(StringUtils.isNotBlank(waibms) && StringUtils.isNotBlank(zhizlx))
		{
			map.put("zhizlx", zhizlx);
			String notwaibms = null ;
			if(zhizlx.equalsIgnoreCase("97W"))
			{
				if(waibms.equalsIgnoreCase("PP"))
				{
					notwaibms = "'PS','PJ'";
				}
				else if(waibms.equalsIgnoreCase("NP"))
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
			}
		}
		wullj = this.wulljService.queryWulljForKd(map);
		List<Wullj> wulljs = wulljService.queryWulljList(map);
		map.clear();
		if (null == wullj || wullj.size() == 0) {
			setResult("flag", "物流路径中没有发货地信息");
			setResult("msg", "wullj");
			return AJAX;
		} 
		else if( StringUtils.isNotBlank(zhizlx) && wullj.size() != 1)
		{
			setResult("flag", "物流路径中没有发货地信息");
			setResult("msg", "wullj");
			return AJAX;
		}
		else {
			setResult("fahd", wullj.get(0).getFahd());
			setResult("xiehzt", wullj.get(0).getXiehztbh());
		}
		if (null == lingj || StringUtils.isBlank(lingj.getAnjmlxhd())) {
			setResult("flag", "零件中没有订货车间信息");
			setResult("msg", "dinghcj");
			
		}else if (lingj.getAnjmlxhd().length() < 3)
		{
			setResult("flag", "零件中的按件目录卸货点字段长度不足3位!");
			setResult("msg", "dinghcj");
		}
		else {
			setResult("dinghcj", lingj.getAnjmlxhd().substring(0, 3));
			if (null == lingj.getJihy()) {
				setResult("flag", "零件中没有计划员信息");
				setResult("msg", "jihy");
			} else {
				setResult("jhy", lingj.getJihy());
				setResult("danw", lingj.getDanw());
			}

		}
		if (null == ljgongys) {
			setResult("flag", "零件供应商中没有该零件信息");
			setResult("msg", "ljgongys");
			return AJAX;
		} else {
			setResult("uabzuclx", ljgongys.getUcbzlx());
			setResult("uabzucrl", ljgongys.getUcrl());
			setResult("uabzucsl", ljgongys.getUaucgs());
			setResult("uabzlx", ljgongys.getUabzlx());
			setResult("beihzq", wullj.get(0).getBeihzq());
			setResult("fayzq", wullj.get(0).getYunszq());
			if (ljgongys.getUcrl() != null && ljgongys.getUaucgs() != null) {
				setResult("uabzrl", ljgongys.getUcrl().multiply(ljgongys.getUaucgs()));
			}
			setResult("heth", ljgongys.getGongyhth());
			setResult("create_time", CommonFun.getJavaTime());
		}
		if(null != wulljs && wulljs.size() > 0)
		{//国产件订单零件添加时，区分供货模式  PP 和其他模式互斥
			String waibums = wulljs.get(0).getWaibms();
			if(StringUtils.isNotBlank(waibums))
			{
				if("PP".equalsIgnoreCase(waibms) || "NP".equalsIgnoreCase(waibms))
				{
					if("PP".equalsIgnoreCase(waibums))
					{
						setResult("waibum", waibums);
					}
					else
					{
						setResult("waibum", "NP");
					}
				}
				else if("PS".equalsIgnoreCase(waibms) || "NS".equalsIgnoreCase(waibms))
				{
					if("PS".equalsIgnoreCase(waibums))
					{
						setResult("waibum", waibums);
					}
					else
					{
						setResult("waibum", "NS");
					}
				}
				else if("PJ".equalsIgnoreCase(waibms) || "NJ".equalsIgnoreCase(waibms) 
						|| "VJ".equalsIgnoreCase(waibms))
				{
					if("PJ".equalsIgnoreCase(waibums))
					{
						setResult("waibum", waibums);
					}
					else if("VJ".equalsIgnoreCase(waibums)){
						setResult("waibum", "VJ");
					}
					else
					{
						setResult("waibum", "NJ");
					}
				}
			}
		}
		return AJAX;
	}

	/**
	 * @:kd订单修改及生效：更新kd订单零件
	 * @throws Exception
	 * @：李明
	 * @：2012-1-12
	 * @参数：list集合
	 */
	public String upDateDingdlj(@Param("DingdUpdate") ArrayList<Dingdlj> allDingdlj) throws Exception {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效:更新订单零件", "更新订单零件开始");
		
		boolean flag = this.kdOrderService.upDateDingdljService(allDingdlj);
		if (flag) {
			for (Dingdlj bean : allDingdlj) {
				this.dingdService.updateDingdzt(bean.getDingdh());
			}
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效:更新订单零件", "更新订单零件成功");
			setResult("flag", Const.UPDATE_SUCSS);
		} else {
			this.userOperLog.addError(CommonUtil.MODULE_XQJS, "订单修改及生效", "更新订单零件", "upDateDingdlj", "更新订单零件失败");
			setResult("flag", Const.UPDATE_FAIL);
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效:更新订单零件", "更新订单零件结束");
		return AJAX;
	}

	/**
	 * @:kd修改及生效：更新kd订单明细
	 * @：李明
	 * @：2012-2-14
	 * @参数：list集合
	 */
	public String upDateDingdmx(@Param("update") ArrayList<Dingdmx> allDingdmx) throws Exception {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单修改及生效:更新订单明细", "更新订单明细开始");
		Map<String,String> map = this.kdOrderService.upDateDingdmxService(allDingdmx);
		setResult("flag", map.get("message"));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单修改及生效:更新订单明细", "更新订单明细结束");
		return AJAX;
	}

	/**
	 * 拼接map中的key
	 * */
	private String getPisl(String fayzq, String p0fyzqxh) {
		// 拼接方法名
		int k = 0;
		if (Integer.parseInt(fayzq.substring(4, 6)) - Integer.parseInt(p0fyzqxh.substring(4, 6)) > 0) {
			k = Integer.parseInt(fayzq.substring(4, 6)) - Integer.parseInt(p0fyzqxh.substring(4, 6));
		} else {
			k = Integer.parseInt(fayzq.substring(4, 6));
		}
		if (k < 0) {
			k = 0;
		}
		return "p" + k + "sl";
	}

	/**
	 * @:kd修改及生效：更新kd订单明细的同时修改订单零件对应的数量
	 * @：李明
	 * @：2012-2-14
	 * @参数：list集合
	 */
	public String doUpdateDingdlj(@Param("update") ArrayList<Dingdlj> allDingdlj) {
		boolean flag = false;
		if (!allDingdlj.isEmpty()) {
			Map<String, String> map = new HashMap<String, String>();
			for (Dingdlj bean : allDingdlj) {
				map.put("id", bean.getId());
				// 得到订单零件
				Dingdlj dingdlj = this.dingdljService.queryDingljObject(map);
				map.clear();
				if (null == dingdlj) {
					continue;
				} else {// 判断是否同一周期
					if (dingdlj.getP0fyzqxh().equals(bean.getFayzq().toString())) {
						map.put("p0sl", bean.getP0sl().toString());
					} else {
						// 拼接方法名
						String psl = this.getPisl(bean.getFayzq().toString(), dingdlj.getP0fyzqxh());
						// 设置map中的key
						map.put(psl, bean.getP0sl().toString());
					}
				}
				map.put("id", bean.getId());
				// 更新订单零件
				flag = this.dingdljService.doUpdateForKd(map);
				map.clear();
			}
		}
		if (flag) {
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效:更新订单零件", "更新订单零件成功");
			setResult("flag", "订单零件修改成功");
		} else {
			this.userOperLog.addError(CommonUtil.MODULE_XQJS, "订单修改及生效", "更新订单零件", "doUpdateDingdlj", "更新订单零件失败");
			setResult("flag", "订单零件修改失败");
		}
		return AJAX;
	}

	/**
	 * @:kd修改及生效：更新kd订单明细的同时修改订单零件对应的数量子方法
	 * @：李明
	 * @：2012-2-14
	 * @参数：list集合
	 */
	public boolean doUpdateDingdljPart(Dingdmx mx) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("dingdh", mx.getDingdh());
		map.put("gongysdm", mx.getGongysdm());
		map.put("usercenter", mx.getUsercenter());
		map.put("lingjbh", mx.getLingjbh());
		// 查询订单零件
		Dingdlj dingdlj = this.dingdljService.queryDingljObject(map);
		if (dingdlj.getP0fyzqxh().equals(mx.getFahzq().toString())) {
			map.put("p0sl", String.valueOf(mx.getShul()));
		} else {
			// 拼接方法名
			String psl = this.getPisl(String.valueOf(mx.getFayrq()), dingdlj.getP0fyzqxh());
			map.put(psl, mx.getShul().toString());
		}
		return this.dingdljService.doUpdateForKd(map);
	}

	/**
	 * <p>
	 * 项目名称：athena-xqjs
	 * </p>
	 * 类名称：KdOrderAction
	 * <p>
	 * 类描述： kd订单目录对比用户导入信息和参考系信息比对并返回结果
	 * </p>
	 * 创建人：陈骏
	 * <p>
	 * 创建时间：2012-1-12
	 * </p>
	 * 
	 * @version
	 * @throws Exception
	 * 
	 */
	public String kdCalculate(@Param("edit") ArrayList<Maoxq> ls) throws Exception {
		
		long start = System.currentTimeMillis();
		String banc[] = new String[ls.size()];
		for (int i = 0; i < ls.size(); i++) {
			banc[i] = ls.get(i).getXuqbc();
		}
		String dingdh = getParam("dingdh");
		String ziyhqrq = getParam("ziyhqrq");
		String zhizlx = getParam("zhizlx");
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String jihyz = loginUser.getJihyz();
		CommonFun.logger.debug("kd订单计算action中jihyz="+jihyz);
		String jihydm = loginUser.getUsername();
		CommonFun.logger.debug("kd订单计算action中jihydm="+jihydm);
		String result = "";

		Map<String, String> map = new HashMap<String, String>();
		map.put("dingdh", dingdh);
		Dingd dingd = this.dingdService.queryDingdByDingdh(map);
		
		//map.put("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());//设置用户中心
		//map.put("username", AuthorityUtils.getSecurityUser().getUsername());//设置用户姓名
		map.put("jiscldm", Const.JISMK_KD_CD);//计算处理代码：跟踪报警（50）
		
		try{
			//判断处理状态,是否有计算进行中
			if(jiscclssz.checkState(map)){
				result = "有计算正在进行,请稍后再计算";
				setResult("result", result);
			}else{
				//更新处理状态为20,计算中
				jiscclssz.updateState(map,Const.JSZT_EXECUTING);
				// 判断订单状态是否是已定义
				CommonFun.logger.debug("kd订单计算action中订单状态Dingdzt="+dingd.getDingdzt());
				if (Const.DINGD_STATUS_YDY.equals(dingd.getDingdzt())) {
					//KD计算
					result = this.kdOrderService.doKDCalculate(ziyhqrq, jihyz, banc, dingdh, zhizlx, jihydm);
				} else {
					result = "选择的订单状态不是“已定义”！";
				}
				//计算完成更新处理状态为90,计算完成
				jiscclssz.updateState(map,Const.JSZT_SURE);
				this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算", "订单计算完成");
				setResult("result", result);
				log.info("KD订单计算结束,耗时---------------------"+(System.currentTimeMillis() - start));
			}
		}catch (Exception e) {
			String loginfo = "kd订单计算异常"+CommonUtil.replaceBlank(e.toString());
			CommonFun.logger.error("kd订单计算异常"+CommonUtil.replaceBlank(e.toString()));
			setResult("result","计算异常" + e.toString());
			userlog.addError(CommonUtil.MODULE_XQJS, "kd订单计算", "kd订单计算异常", CommonUtil.getClassMethod(), loginfo);
			//异常时将处理状态更新为99
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
		}
		return AJAX;
	}

	/**
	 * <p>
	 * 项目名称：athena-xqjs
	 * </p>
	 * 类名称：KdOrderAction
	 * <p>
	 * 类描述：参数校验
	 * </p>
	 * 创建人：陈骏
	 * <p>
	 * 创建时间：2012-1-12
	 * </p>
	 * 
	 * @version
	 * @throws Exception
	 * 
	 */
	public String checkValue(@Param("edit") ArrayList<Maoxq> ls) throws Exception {
		String banc[] = new String[ls.size()];
		for (int i = 0; i < ls.size(); i++) {
			banc[i] = ls.get(i).getXuqbc();
		}
		String dingdh = getParam("dingdh");
		String ziyhqrq = getParam("ziyhqrq");
		String zhizlx = getParam("zhizlx");
		String rilbc = "";
		if(zhizlx.equals(Const.ZHIZAOLUXIAN_KD_PSA)){
			rilbc = Const.KDBANCI;
		}else if(zhizlx.equals(Const.ZHIZAOLUXIAN_KD_AIXIN)){
			rilbc = Const.AXBANCI;
		}
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String jihyz = loginUser.getZuh();
		String jihydm = loginUser.getUsername();

		List<String> useList = loginUser.getUcList();
		String usercenter[] = new String[useList.size()];
		for (int i = 0; i < useList.size(); i++) {
			usercenter[i] = useList.get(i);
		}
		this.kdOrderService.clearData();// 清理中间表
		Map<String, String> Map = new HashMap<String, String>();
		Map.put("dingdh", dingdh);
		Dingd dingd = this.dingdService.queryDingdByDingdh(Map);
		usercenter = CommonFun.getArray(usercenter);
		banc = CommonFun.getArray(banc);
		
		this.kdOrderService.insertKdmxqZ(usercenter, banc,zhizlx);
		// 取出需要计算的毛需求并进行行列转换
		for (int i = 0; i < banc.length; i++) {
			for (int j = 0; j < usercenter.length; j++) {
				// 查询指定用户中心下该版次的毛需求插入中间表
				this.kdOrderService.insertKdmxqC(ziyhqrq, usercenter[j], banc[i], rilbc, dingd, jihyz, loginUser);
			}
		}
		
		int errorCount = this.kdOrderService.checkKeyNumber();// 关键参数校验
		if(errorCount>0){
			setResult("result", "校验完成,有异常");
		}else{
			setResult("result", "校验完成,无异常");
		}
		
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算：参数校验", "参数校验");
		return AJAX;
	}

	/**
	 * <p>
	 * 不匹配装箱规则
	 * </p>
	 * 
	 * @param list
	 * @return
	 */
	public String axunMatch() {
		this.dingdService.updateDingdzt(getParam("dingdh"));
		
		List<Dingdlj> list = dingdljService.queryAllDingdlj(getParams());
		Dingdmx dx = this.dingdmxService.queryCgbh(getParam("dingdh"));
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String caozz = loginUser.getUsername();
		String jihydm = loginUser.getUsername();
		if (list.size() != 0) {
			kdOrderService.clearDdMxByDdh(list.get(0));
		}
		for (int i = 0; i < list.size(); i++) {
			try {
				String gcbh = "";
				if(dx!=null){
					gcbh = dx.getGcbh();
				}
				Map<String,String> ddMap = new HashMap<String,String>();
		    	ddMap.put("dingdh", list.get(i).getDingdh());
		    	Dingd dd = this.dingdService.queryDingdByDingdh(ddMap);
		    	String rilbc = "";
		    	if(dd.getDingdlx().equals(Const.DINGDLX_KD)){
		    		rilbc = Const.KDBANCI;
		    	}else if(dd.getDingdlx().equals(Const.DINGDLX_AIX)){
		    		rilbc = Const.AXBANCI;
		    	}
				Gongys gys = this.gService.queryObject(list.get(i).getGongysdm(), list.get(i).getUsercenter());
				Lingj lingj = this.lingjService.queryObject(list.get(i).getLingjbh(), list.get(i).getUsercenter());
				if(null!=gys&&null!=lingj){
					kdOrderService.newWeekOrderCount(list.get(i), jihydm,lingj.getZhongwmc(),gys.getGongsmc(),gys.getNeibyhzx(),caozz,rilbc,loginUser,gcbh);
				}else{
					kdOrderService.newWeekOrderCount(list.get(i), jihydm,null,null,null,caozz,rilbc,loginUser,gcbh);
				}
				
			} catch (Exception e) {
				this.userOperLog.addError(CommonUtil.MODULE_XQJS, "订单修改及生效：爱信订单", "不匹配装箱规则", "axunMatch", "周要货量错误");
			}

		}

		return AJAX;
	}

	/**
	 * 订单定义 根据用户中心/订单类型查询供应商
	 * 
	 * @return
	 */
	public String getGysList() {
		setResult("result", gService.queryObjectBylx(getParam("dingdlx"), getParam("usercenter")));
		return AJAX;
	}

	public String getGysGroup() {
		setResult("result", gService.queryDingdBylx(getParam("dingdlx"), getParam("usercenter")));
		return AJAX;
	}

	/**
	 * 爱信不匹配装箱规则 订单明细修改，订单零件汇总
	 * 
	 * @author NIESY
	 * @return
	 */
	public String sumOrderParts() {
		Map<String, String> map = getParams();
		List<Dingdmx> resultList = this.kdOrderService.queryDetailOrderLineService(this.dingdmxService
				.queryDingdmxMap(map));
		// 按用户中心、供应商代码、零件号、订单号、发货周期汇总
		for (int i = 0; i < resultList.size(); i++) {
			String userceter = resultList.get(i).getUsercenter();
			String gongysdm = resultList.get(i).getGongysdm();
			String lingjbh = resultList.get(i).getLingjbh();
			String dingdh = resultList.get(i).getDingdh();
			String fahzq = resultList.get(i).getFahzq();
			for (int j = i + 1; j < resultList.size(); j++) {
				if (userceter.equals(resultList.get(j).getUsercenter())
						&& gongysdm.equals(resultList.get(j).getGongysdm())
						&& lingjbh.equals(resultList.get(j).getLingjbh())
						&& dingdh.equals(resultList.get(j).getDingdh()) && fahzq.equals(resultList.get(j).getFahzq())) {
					BigDecimal shulI = resultList.get(i).getShul();
					BigDecimal shulJ = resultList.get(j).getShul();
					BigDecimal psl = shulI.add(shulJ);
					resultList.get(i).setShul(psl);
					resultList.remove(j);
					j--;
				}
			}
		}
		// 修改订单零件
		for (int i = 0; i < resultList.size(); i++) {
			boolean flag = this.doUpdateDingdljPart(resultList.get(i));
			if (flag) {
				this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效： 爱信不匹配装箱规则", "订单明细修改");
				setResult("flag", "订单零件修改成功");
			} else {
				this.userOperLog.addError(CommonUtil.MODULE_XQJS, "订单修改及生效：爱信订单", "不匹配装箱规则", "sumOrderParts",
						"订单明细修改失败");
				setResult("flag", "订单零件修改失败");
				break;
			}
		}
		return AJAX;
	}

	// 不匹配装箱规则页面初始化
	public String initUnMatchPage() {
		setResult("dingdh", getParam("dingdh"));
		setResult("dingdzt", getParam("dingdzt"));
		setResult("usercenter", getParam("usercenter"));
		setResult("gongysdm", getParam("gongysdm"));
		setResult("flag", "axbpp");
		return "success";
	}

	// 不匹配装箱规则订单明细新增
	public String addBppMx(@Param Dingdmx bean) throws ParseException {
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		bean.setActive("1");
		String result = MessageConst.INSERT_COUNT_0;
		bean.setCreator(getUserInfo().getUsername());
		bean.setEditor(getUserInfo().getUsername());
		bean.setCreate_time(CommonFun.getJavaTime());
		bean.setEdit_time(CommonFun.getJavaTime());
		if (!bean.getGonghlx().equals("M1") && !bean.getGonghlx().equals("MD") 
				&& !bean.getGonghlx().equals("MJ") && !bean.getGonghlx().equals("MV")) {
			Gongys gys = gService.queryObject(bean.getGongysdm(), bean.getUsercenter());
			bean.setGongyslx(gys.getLeix());
			bean.setGongsmc(gys.getGongsmc());
			bean.setNeibyhzx(gys.getUsercenter());
			LingjGongys ljgys = lingjGongysService.select(bean.getUsercenter(), bean.getGongysdm(), bean.getLingjbh());
			bean.setGongyfe(ljgys.getGongyfe());
			bean.setUabzlx(ljgys.getUabzlx());
			bean.setUabzucsl(ljgys.getUaucgs());
			bean.setUabzuclx(ljgys.getUcbzlx());
			bean.setUabzucrl(ljgys.getUcrl());
			// xh 10607 临时订单增加明细时判断包装信息是否存在
			if(StringUtils.isBlank(ljgys.getUabzlx())
					|| StringUtils.isBlank(ljgys.getUcbzlx())
					|| null == ljgys.getUaucgs()
					|| null == ljgys.getUcrl()
			)
			{
				setResult("errorMsg", "用户中心" + bean.getUsercenter() + "零件号" + bean.getLingjbh() + "零件包装信息不完整(UA包装类型,UC包装类型,UC容量,UAUC个数)!");
				return AJAX;
			}
		}
		bean.setZhuangt(Const.DINGD_STATUS_ZZZ);
		if (bean.getGonghlx().indexOf("M") != -1 || bean.getGonghlx().indexOf("C") != -1) {
			String flag = this.setAxsj(bean, !StringUtils.isEmpty(bean.getXiaohsj()));
			if (!StringUtils.isEmpty(flag)) {
				setResult("errorMsg", flag);
				return AJAX;
			}
///////////wuyichao 针对按需临时订单进行修改，修改内容为利用WTC服务，将订单明细信息传入执行层，查看该信息是否满足执行层要货令的拆分条件		
			Map<String, Object> wtcMap = new HashMap<String, Object>();
			wtcMap.put("usercneter", bean.getUsercenter());
			wtcMap.put("lingjbh", bean.getLingjbh());
			wtcMap.put("gongysdm", bean.getGongysdm());
			wtcMap.put("gonghms", bean.getGonghlx());
			wtcMap.put("xiaohd", bean.getXiaohd());
			//MD M1  仓库 = bean.getGongysdm  zickbh =  bean.getCangkdm()  CD MD 仓库 = bean.getCangkdm() zickbh =  bean.getCangkdm()
			if(bean.getGonghlx().indexOf("M") != -1)
			{
				wtcMap.put("cangkbh", bean.getGongysdm());
			}
			else
			{
				wtcMap.put("cangkbh", bean.getCangkdm());
			}
			wtcMap.put("zickbh", bean.getCangkdm());
			wtcMap.put("xiehzt", bean.getXiehzt());
			try
			{
				Map<String, String> errorMap = new HashMap<String, String>();
				errorMap.put("C_Y00001", "执行层参考系-零件关系缺失");
				errorMap.put("C_Y00002", "执行层参考系-供应商关系缺失");
				errorMap.put("C_Y00003", "执行层参考系-零件供应商关系缺失");
				errorMap.put("C_Y00004", "执行层参考系-物流路径关系缺失");
				errorMap.put("C_Y00005", "执行层参考系-零件仓库关系缺失");
				errorMap.put("C_Y00006", "执行层参考系-零件仓库关系缺失");
				errorMap.put("C_Y00007", "执行层参考系-卸货站台关系缺失");
				
				WtcResponse wtcResponse = callWtc(bean.getUsercenter(), "Y0201", wtcMap);
				if (!wtcResponse.get("response").equals(Const.WTC_SUSSCESS)) {
					if(errorMap.containsKey(wtcResponse.get("response"))){
						setResult("errorMsg", errorMap.get(wtcResponse.get("response")) + ":零件号" + bean.getLingjbh()  
								+ "未能满足执行层拆分要货令条件,该零件部分信息可能于当天维护,请于明天操作!");
					}else{
						setResult("errorMsg", "未知原因:零件号" + bean.getLingjbh() 
								+ "未能满足执行层拆分要货令条件,该零件部分信息可能于当天维护,请于明天操作!");
					}
					return AJAX;
				}
			}
			catch (Exception e) {
				setResult("errorMsg", "调用WTC服务失败,请联系管理人员!");
				e.printStackTrace();
				log.error("调用WTC服务失败", e);
				return AJAX;
			}
			
///////////wuyichao 针对按需临时订单进行修改，修改内容为利用WTC服务，将订单明细信息传入执行层，查看该信息是否满足执行层要货令的拆分条件	
		}
		// 订货车间
		Lingj lingj = lingjService.select(bean.getUsercenter(), bean.getLingjbh());
		bean.setDinghcj(lingj == null ? null : lingj.getDinghcj());
		bean.setLingjmc(lingj == null ? null : lingj.getZhongwmc());
		bean.setJihyz(lingj == null ? null : lingj.getJihy());
		bean.setJissl(bean.getShul());
		if (bean.getXiaohsj() != null || dingdmxService.queryListMx(getParams()).size() == 0) {
			//判断订单状态
			int zhuangt=dingdService.queryDingdzt(bean.getDingdh());
			if(zhuangt ==-99){
				setResult("errorMsg", "该订单不存在或状态有误，请查证！");
			   return AJAX;
			}else if(zhuangt >3){
				setResult("errorMsg", "只能对已定义、制作中、待生效、拒绝的订单进行操作！请检查订单状态！");
			   return AJAX;
			 }
			this.dingdmxService.doInsert(bean);
			result = "新增成功！";
			//按需订单明细添加时修改订单零件P0数量   wuyichao修改
			if ( !("temp").equalsIgnoreCase(getParam("lx")) && (bean.getGonghlx().indexOf("M") != -1 || bean.getGonghlx().indexOf("C") != -1)) {
				this.sumSlToPn(newEditor, editTime);
			}
			
			this.dingdService.updateDingdzt(bean.getDingdh());
			setResult("msg", result);
			log.debug("========addBppMx 新增成功=========");
		} else {
			log.debug("========addBppMx 新增失败=========");
			setResult("errorMsg", result);
		}
		return AJAX;
	}
	
	//按需订单明细添加时修改订单零件P0数量   wuyichao修改
	public void sumSlToPn(String newEditor, String newEditTime) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", getParam("usercenter"));
		map.put("lingjbh", getParam("lingjbh"));
		map.put("dingdh", getParam("dingdh"));
		map.put("cangkdm", getParam("cangkdm"));
		map.put("gonghlx", getParam("gonghlx"));
		String prq = getParam("prq");
		if(StringUtils.isNotBlank(prq))
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
		} else {
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

	// 临时订单按需，根据消耗时间推算出预计交付时间和预计上线时间
	public String setAxsj(Dingdmx bean, boolean flag) throws ParseException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		String beihsj = null;
		if (bean.getGonghlx().equals("MD") || bean.getGonghlx().equals("CD")) {
			map.put("xiaohdbh", bean.getXiaohd());
			map.put("gongysbh", bean.getGongysdm());
			Lingjxhd lingjxhd = lingjxhdService.queryLingjxhdObject(map);
			// 如果小火车时刻表没有数据,跳过该条记录,不予计算
			if (lingjxhd == null) {
				return "未找到该消耗点";
			}
			if(StringUtils.isEmpty(lingjxhd.getXiaohcbh())){
				return "小火车编号为空";
			}
			if (flag) {
				bean.setFenpxh(bean.getXiaohd().substring(0, 5));
				bean.setXianbyhlx(lingjxhd.getXianbyhlx());
				map.put("xiaohcbh", lingjxhd.getXiaohcbh());
				map.put("shengcxbh", lingjxhd.getShengcxbh());
				map.put("chufsxsj", bean.getXiaohsj().substring(0, 16));
				bean.setXiaohch(lingjxhd.getXiaohcbh());
				bean.setChanx(lingjxhd.getShengcxbh());
				// 取小火车运输时刻
				List<Xiaohcyssk> Listxhcyssk = xService.queryXiaohcysskObjectByShangx(map);
				// 如果小火车时刻表没有数据,跳过该条记录,不予计算
				if (Listxhcyssk == null || Listxhcyssk.isEmpty()) {
					return "未查到相关小火车信息";
				}
				Xiaohcyssk xiaohcyssk = Listxhcyssk.get(1);
				// 备货时间
				beihsj = xiaohcyssk.getKaisbhsj();
				bean.setXiaohcbhsj(beihsj);
				// 上线时间
				String shangxsj = xiaohcyssk.getChufsxsj();
				bean.setXiaohcsxsj(shangxsj.substring(0, 16));
				bean.setTangc(xiaohcyssk.getTangc());//小火车运输时刻趟次
				bean.setXiaohch(lingjxhd.getXiaohcbh());//小火车编号
				bean.setXiaohccxh(lingjxhd.getXiaohccxbh());//小火车车厢编号
				if (beihsj == null) {
					return "小火车运输时刻,小火车编号" + lingjxhd.getXiaohcbh() + "生产线编号" + lingjxhd.getShengcxbh() + "备货时间为空";
				}
			}

		} else {
			beihsj = bean.getXiaohsj();
		}
		// 预计到货时间(yujdhsj)=小火车备货时间-内部物流时间
		Wullj wullj=null;
		String gcbh = null;
		if (bean.getGonghlx().equals("CD")) {
			map.put("fenpqh", bean.getXiaohd().substring(0, 5));
			map.put("waibms", bean.getGonghlx());
			wullj= wulljService.queryWulljObject(map);
			gcbh = wullj == null ? null : wullj.getGcbh();
			bean.setCangkdm(wullj == null ? null : wullj.getMudd());
			bean.setBeihsj2(wullj == null ? null : wullj.getBeihsj());//备货时间
		} else if (bean.getGonghlx().equals("C1") || bean.getGonghlx().equals("CV")) {
			map.put("gongysbh", bean.getGongysdm());
			map.put("waibms", bean.getGonghlx());
			map.put("mudd", bean.getCangkdm());
			wullj = wulljService.queryWulljObjectLsaxc1(map);
			gcbh = wullj == null ? null : wullj.getGcbh();
			if(wullj!=null && wullj.getXianbck()!=null && wullj.getXianbck().length()>0){
				map.put("shengcxbh", wullj.getXianbck());
			}
		}else if(bean.getGonghlx().equals("M1") || bean.getGonghlx().equals("MJ") || bean.getGonghlx().equals("MV")){
			map.put("mos2", bean.getGonghlx());
			map.put("dinghck", bean.getGongysdm());
			map.put("xianbck", bean.getCangkdm());
			map.put("cangkbh", bean.getCangkdm());
			Lingjck ck = ljck.querySingle(map);
			wullj= wulljService.queryWulljObjectLsaxck(map);
			gcbh = bean.getGongysdm();
			bean.setUsbaozlx(ck == null ? null : ck.getUsbzlx());
			bean.setUsbaozrl(ck == null ? null : ck.getUsbzrl());
			if(wullj!=null && wullj.getXianbck()!=null && wullj.getXianbck().length()>0){
				map.put("shengcxbh", wullj.getXianbck());
			}
		}else if(bean.getGonghlx().equals("MD")){
			map.put("mos", "MD");
			map.put("dinghck", bean.getGongysdm());
			map.put("fenpqh", bean.getXiaohd().substring(0, 5));
			wullj = wulljService.queryWulljObjectLsaxck(map);
			bean.setBeihsj2(wullj.getBeihsj());//备货时间
			map.put("cangkbh", wullj == null ? " " : wullj.getXianbck());
			Lingjck ck = ljck.querySingle(map);
			bean.setUabzuclx(ck == null ? null : ck.getUclx());
			bean.setUabzucrl(ck == null ? null : ck.getUcrl());
			bean.setCangkdm(wullj == null ? null : wullj.getXianbck());
			gcbh = bean.getGongysdm();
		}
		if (wullj == null) {
			return "未在物流路径中找到相关记录.";
		}
		bean.setGcbh(gcbh);
		bean.setXiehzt(wullj.getXiehztbh());
		bean.setLujdm(wullj.getLujbh());
		bean.setLeix("9");

		if (flag) {
			BigDecimal neibwlsj = wullj.getBeihsjc();
			// 如果备货时间为空
			if (neibwlsj == null) {
				return "内部物流时间C为空";
			}
			/**
			 * 获取到最晚到货时间
			 * */
			map.put("juedsk", beihsj);
			map.put("shijNum", CommonFun.strNull(neibwlsj.intValue()));//内部物流时间
			//预计到货时间=时间(CDMD为小火车备货时间,C1M1为消耗时间)-内部物流时间
			String zuiwdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));
			map.put("usercenter", bean.getUsercenter());
			map.put("xiehztbh", wullj.getXiehztbh());
			
			map.put("gcbh", gcbh);
			map.put("daohsj", zuiwdhsj);
			/**
			 * 从卸货站台得到提前到货时间
			 */
			Xiehzt xiehzt = anxMaoxqService.queryXiehztObject(map);
			BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
			if (yunxtqdhsj == null) {
				return "卸货站台允许提前到货时间";
			}
			map.put("xiehztbh", wullj.getXiehztbh().substring(0,4));
			List<Yunssk> listYunssk = (List<Yunssk>) anxMaoxqService.queryYunssk(map);
			if (listYunssk != null && !listYunssk.isEmpty()) {
				zuiwdhsj = listYunssk.get(0).getDaohsj().substring(0, 16);
			}
			//预计到货时间=时间(CDMD为小火车备货时间,C1M1为消耗时间)-内部物流时间
			map.put("juedsk", zuiwdhsj);
			map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));//允许提前到货时间
	
			// 最早到货时间
			String zuizdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));
			bean.setZuizdhsj(zuizdhsj);
			bean.setZuiwdhsj(zuiwdhsj);	
		}else{//如果只填写了最晚到货时间,则需推算出最早到货时间
			map.put("usercenter", bean.getUsercenter());
			map.put("xiehztbh", wullj.getXiehztbh());
			map.put("gcbh", gcbh);
			/**
			 * 从卸货站台得到提前到货时间
			 */
			Xiehzt xiehzt = anxMaoxqService.queryXiehztObject(map);
			BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
			if (yunxtqdhsj == null) {
				return "卸货站台允许提前到货时间";
			}
			//预计到货时间=时间(CDMD为小火车备货时间,C1M1为消耗时间)-内部物流时间
			map.put("juedsk", bean.getZuiwdhsj());
			map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));//允许提前到货时间
	
			// 最早到货时间
			String zuizdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));
			bean.setZuizdhsj(zuizdhsj);
		}
		//M1模式保存小火车出发上线时间 = 最晚到货时间
		if(bean.getGonghlx().equals("M1") || bean.getGonghlx().equals("MJ") || bean.getGonghlx().equals("MV")){
			bean.setXiaohcsxsj(bean.getZuiwdhsj());
		}
		/**
		 * M1MD模式计算发运时间
		 */
		if (bean.getGonghlx().equalsIgnoreCase(Const.ANX_MS_M1) || bean.getGonghlx().equalsIgnoreCase(Const.ANX_MS_MD)
				|| bean.getGonghlx().equalsIgnoreCase(Const.MJ) || bean.getGonghlx().equalsIgnoreCase(Const.MV)) {
			Date date = CommonFun.yyyyMMddHHmm.parse(bean.getZuiwdhsj());//设置为最晚到货时间
			//发运时间 = 最晚到货时间 - 仓库送货时间2
			date.setTime(date.getTime() - CommonFun.getBigDecimal(wullj.getCangkshsj2()).multiply(new BigDecimal(24))
					.multiply(new BigDecimal(60)).multiply(new BigDecimal(60)).multiply(new BigDecimal(1000)).intValue());
			bean.setFaysj(CommonFun.yyyyMMddHHmm.format(date));
		}else{//C1CD模式发运时间= 最晚到货时间 - 运输周期
			Date date = CommonFun.yyyyMMddHHmm.parse(bean.getZuiwdhsj());//设置为最晚到货时间
			//发运时间 = 最晚到货时间 - 运输周期
			date.setTime(date.getTime() - CommonFun.getBigDecimal(wullj.getYunszq()).multiply(new BigDecimal(24))
					.multiply(new BigDecimal(60)).multiply(new BigDecimal(60)).multiply(new BigDecimal(1000)).intValue());
			bean.setFaysj(CommonFun.yyyyMMddHHmm.format(date));
		}
		bean.setJiaofrq(bean.getZuiwdhsj());
		return "";
	}

	// 检查添加明细的发运日期是否大于P0发运周期
	public String checkAddmx() {
		Dingdlj lj = this.dingdljService.queryDingljObject(getParams());
		// 有年周期和用户中心查询出有多少周序
		Map<String, String> map = getParams();
		map.put("riq", getParam("fayrq"));
		CalendarCenter calendarCenter = this.calendarCenterService.queryCalendarCenterObject(map);
		if (lj != null) {
			int p0rq = Integer.parseInt(lj.getP0fyzqxh());
			int fayrq = Integer.parseInt(calendarCenter.getNianzq());
			int margin = fayrq - p0rq;
			if (margin < 0 || margin > 2) {
				setResult("flag", "您输入发运日期不在P0-P2周期范围内！");
			}
		} else {
			setResult("ljflag", "您输入的零件号在订单零件里不存在！");
		}
		return AJAX;
	}

	/**
	 * 导出订单xls
	 * 
	 * @throws Exception
	 * */
	public String kdDingdDownLoadFile(@Param("dingdlx") String dingdlx, @Param("dingdh") String dingdh,
			@Param("gongysdm") String gongysdm, @Param("fahzq") String fahzq, @Param("editor") String editor,
			@Param("heth") String heth, @Param("dingdzt") String dingdzt, @Param("dingdnr") String dingdnr,
			@Param("jislx") String jislx, @Param("beiz") String beiz) throws Exception {
		List<Dingdlj> dingdljList = new ArrayList<Dingdlj>();
		if(dingdh!=null&&!dingdh.equals("")){
			String [] ddh = dingdh.split(",");
			for(int i=0;i<ddh.length;i++){
				Map<String, String> param = new HashMap<String, String>();
				param.put("dingdh", ddh[i]);
				List<Dingdlj> ddljList= this.dingdljService.queryEXPKDDingdljList(param);
				dingdljList.addAll(ddljList);
			}
		}else{
			Map<String, String> param = new HashMap<String, String>();
			param.put("dingdlx", dingdlx);
			param.put("dingdh", dingdh);
			param.put("dingdgongysdm", gongysdm);
			param.put("fahzq", fahzq);
			param.put("editor", editor);
			param.put("dingdzt", dingdzt);
			param.put("heth", heth);
			param.put("dingdnr", dingdnr);
			param.put("jislx", jislx);
			param.put("beiz", beiz);
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单导出", "KD订单导出开始");
			dingdljList = this.dingdljService.queryEXPKDDingdljList(param);

		}
		
		Map<String, Object> dataSource = new HashMap<String, Object>();
		dataSource.put("dataSource", dingdljList);
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		this.downLoadServices.downloadFile("dingdlj.ftl", dataSource, response, "订单修改及生效--订单",
				ExportConstants.FILE_XLS, false);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单导出", "KD订单导出结束");
		return "download";
	}

	/**
	 * 导出订单零件xls
	 * 
	 * @throws Exception
	 * */
	public String kdDingdDetailDownload(@Param("usercenter") String usercenter, @Param("dingdh") String dingdh,
			@Param("gongysdm") String gongysdm, @Param("jihyz") String jihyz) throws Exception {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD件导出订单零件", "KD件导出订单零件开始");
		Map<String, String> param = new HashMap<String, String>();
		param.put("usercenter", usercenter);
		param.put("dingdh", dingdh);
		param.put("gongysdm", gongysdm);
		param.put("jihyz", jihyz);
		param.put("active", Const.ACTIVE_1);

		List<Dingdlj> dingdljList = this.dingdljService.queryEXPDingdljList(param);

		Map<String, Object> dataSource = new HashMap<String, Object>();
		dataSource.put("dataSource", dingdljList);
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		this.downLoadServices.downloadFile("dingdlj.ftl", dataSource, response, "订单修改及生效--订单零件",
				ExportConstants.FILE_XLS, false);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD件导出订单零件", "KD件导出订单零件结束");
		return "download";
	}
	public Map<String, String> setUsercenter(Map<String, String> params) {
		params.put("usercenter", this.getUserInfo().getUsercenter());
		return params;
	}
	/**
	 * 初始化获取资源获取日期kd件，只取周期
	 * 
	 * @：陈骏
	 * @：2012-2-10
	 */
	public String executeKDZiykzb() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "获取资源获取日期开始");
		Map<String, String> map = new HashMap<String,String>();
		List<Ziykzb> listZiykzb = this.maoxqservice.queryZiykzb(map);
		if(listZiykzb.size()>0){
			List<Ziykzb> newListZiykzb = new ArrayList<Ziykzb>();
			String kais = listZiykzb.get(listZiykzb.size()-1).getZiyhqrq();
			String jies = listZiykzb.get(0).getZiyhqrq();
			List<String> zhouyi = this.calendarCenterService.queryZhouyi(kais,jies,Const.ZHOUYI);
			for(Ziykzb ziykzb :listZiykzb){
				
				boolean flag = false;
				for(String zhouyirq :zhouyi){
					if(zhouyirq.equals(ziykzb.getZiyhqrq())){
						flag = true;
					}
				}
				if(flag){
					newListZiykzb.add(ziykzb);
				}
			}
			setResult("result", newListZiykzb);
		}else{
			setResult("result", "资源快照表为空");
		}
		
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "获取资源获取日期结束");
		return AJAX;
	}
	
	/**
	 * kd件订单生效
	 * 
	 * @：陈骏
	 * @：2012-2-10
	 */
	public String updateDaStatus(@Param("dd") ArrayList<Dingd> ls) {
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		String flag = getParam("flag");
		String result = MessageConst.UPDATE_COUNT_0;
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单修改及生效：生效、拒绝", "生效、拒绝订单开始");
		try {
			result = this.kdOrderService.updateDaStatus(ls, newEditor, editTime, flag);
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

	public String getLikeGys() {
		setResult("result", lingjGongysService.queryLjgysLike(getParams()));
		return AJAX;
	}
	public String kdhzDownLoadFile() throws IOException{
		
		Map<String ,Object> map = new HashMap<String,Object>();
		
				ArrayList<Kdmxqhzc> list = (ArrayList)this.kdmxqhzcService.selectDingd(null);
				map.put("res", list);
				Map<String, Object> dataSource = map;
				//拿到response对象
				HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
				downloadServices.downloadFile("kdmxqhzc.ftl", dataSource, response, "KD订单计算中间表导出", ExportConstants.FILE_XLS, false) ;	
				return "download";
		}
	
	
	public String changeStats(@Param("change") ArrayList<Dingd> list){
		for (Dingd dd :list){
			Map<String,String> map = new HashMap<String,String>();
			map.put("dingdh", dd.getDingdh());
			Dingd dingd = this.dingdService.queryDingdByDingdh(map);
			dingd.setDingdzt(Const.DINGD_STATUS_YDY);
			dingd.setZiyhqrq(null);
			dingd.setMaoxqbc(null);
			this.dingdService.doUpdate(dingd);
			Dingdlj dingdlj = new Dingdlj();
			dingdlj.setDingdh(dingd.getDingdh());
			this.dingdljService.doDelete(dingdlj);
			Dingdmx dingdmx = new Dingdmx();
			dingdmx.setDingdh(dingd.getDingdh());
			this.dingdmxService.doDelete(dingdmx);
		}
		setResult("result","重置完毕");
		return AJAX;
	}
	
//	String driver,url,user,pwd;
//	public Connection  ConnectOrcl(){
//		driver = "oracle.jdbc.driver.OracleDriver";
//		url = "jdbc:oracle:thin:@10.26.202.45:1521:azbdb";
//		user = "ZBC_TEST";
//		pwd = "ahtenajccs";
//		Connection conn = null;
//		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url,user,pwd);
//		} catch (ClassNotFoundException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//		return conn;
//	}
//	public Connection  ConnectOrcl2(){
//		driver = "oracle.jdbc.driver.OracleDriver";
//		url = "jdbc:oracle:thin:@10.26.171.241:1521:copy45";
//		user = "ZBC_TEST";
//		pwd = "ZBC_TEST";
//		Connection conn = null;
//		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url,user,pwd);
//		} catch (ClassNotFoundException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO 自动生成 catch 块
//			e.printStackTrace();
//		}
//		return conn;
//	}
//	public  static void main(String[] args) throws ParseException{
//		KdOrderAction kat = new KdOrderAction();
//		Connection conn = kat.ConnectOrcl();
//		Connection conn1 = kat.ConnectOrcl2();
//		 try {
//			Statement stmt =conn.createStatement();
//			 String sql="select * from XQJS_MAOXQMX t where t.xuqbc = 'DDB2012101001'";
//	         ResultSet rs=stmt.executeQuery(sql);
//	         while(rs.next())   
//	         {   
//	        	 Maoxqmx mq = new Maoxqmx();
//	        	 //rs.getObject(columnLabel)
//	        	 mq.setId(rs.getString("id"));
//	        	 mq.setXuqbc(rs.getString("xuqbc"));
//	        	 mq.setUsercenter(rs.getString("usercenter"));
//	        	 mq.setShiycj(rs.getString("shiycj"));
//	        	 mq.setChanx(rs.getString("chanx"));
//	        	 mq.setXuqz(rs.getString("xuqz"));
//	        	 mq.setXuqrq(rs.getString("xuqrq"));
//	        	 mq.setLingjbh(rs.getString("lingjbh"));
//	        	 mq.setXuqsl(new BigDecimal(rs.getString("xuqsl")));
//	        	 mq.setDanw(rs.getString("danw"));
//	        	 mq.setZhizlx(rs.getString("zhizlx"));
//	        	 mq.setXuqsszq(rs.getString("xuqsszq"));
//	        	 mq.setXuqksrq(rs.getString("xuqksrq"));
//	        	 mq.setXuqjsrq(rs.getString("xuqjsrq"));
//	        	 mq.setCangk(rs.getString("cangkdm"));
//	        	 String sqlp = "insert into XQJS_MAOXQMX (id, xuqbc, usercenter, shiycj, chanx, xuqz, xuqrq, lingjbh, xuqsl, danw, zhizlx, xuqsszq, xuqksrq, xuqjsrq, cangkdm) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//	        	 
//	        	 //DateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//	        	 //Date dd = format1.parse(rs.getString("xuqrq"));
//	        	 //format1 = new SimpleDateFormat("yyyy-MM-dd");
//	        	 //String newD = format1.format(dd);
//	        	 //dd = format1.parse(newD);
//	        	 DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	        	 Date dd = format1.parse(rs.getString("xuqrq"));
//	        	 java.sql.Date sqlDate = new java.sql.Date(dd.getTime());
//	        	 PreparedStatement pstmt = conn1.prepareStatement(sqlp);
//	        	 pstmt.setString(1, rs.getString("id"));
//	        	 pstmt.setString(2, rs.getString("xuqbc"));
//	        	 pstmt.setString(3, rs.getString("usercenter"));
//	        	 pstmt.setString(4, rs.getString("shiycj"));
//	        	 pstmt.setString(5, rs.getString("chanx"));
//	        	 pstmt.setString(6, rs.getString("xuqz"));
//	        	 pstmt.setDate(7,sqlDate);
//	        	 pstmt.setString(8, rs.getString("lingjbh"));
//	        	 pstmt.setBigDecimal(9, new BigDecimal(rs.getString("xuqsl")));
//	        	 pstmt.setString(10, rs.getString("danw"));
//	        	 pstmt.setString(11, rs.getString("zhizlx"));
//	        	 pstmt.setString(12, rs.getString("xuqsszq"));
//	        	 pstmt.setString(13, rs.getString("xuqksrq"));
//	        	 pstmt.setString(14, rs.getString("xuqjsrq"));
//	        	 pstmt.setString(15, rs.getString("cangkdm"));
//	        	 pstmt.execute();
//	        	 pstmt.close();
//	         }  
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//  }
}
