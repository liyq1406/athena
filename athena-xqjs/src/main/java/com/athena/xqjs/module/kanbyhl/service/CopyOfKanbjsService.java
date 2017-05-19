package com.athena.xqjs.module.kanbyhl.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.CalendarGroup;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.entity.kanbyhl.Lingjgys;
import com.athena.xqjs.entity.kanbyhl.Wullj;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.common.service.CalendarGroupService;
import com.athena.xqjs.module.common.service.CalendarVersionService;
import com.athena.xqjs.module.common.service.CommonCalendarService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * 看板规模计算service
 * </p>
 * <p>
 * 
 * @author Niesy
 *         </p>
 *         <p>
 *         Date 2012-01-15
 *         </p>
 * 2015-04-27		gswang			0011283: 看板在做失效和消耗点替换时，准备层发给变更记录表2次
 */
/**@WebService(endpointInterface = "com.athena.xqjs.module.kanbyhl.service.Kanbyhl", serviceName = "/kanbjsService")
@SuppressWarnings("rawtypes")
@Component**/
public class CopyOfKanbjsService extends BaseService implements Kanbyhl {

	@Inject
	private CalendarGroupService calendarGroupService;

	@Inject
	private CalendarVersionService calendarVersionService;

	@Inject
	private YicbjService yicbjService;

	@Inject
	private CommonCalendarService commonCalendarService;

	@Inject
	private AssisterDateService assisterDateService;

	private Map<String, String> cgmap;

	private Map<String, String> date;
	
	public Map<String, String> kanbjsMap = new HashMap<String, String>();

	/**
	 * 周期总工作天数
	 */
	private Map<String, Integer> allWorkDays;
	/**
	 * 日计算开始时间与结束时间
	 */
	private Map<String, String> timeMap;

	@Inject(value = "lingj")
	private Lingj lingj;
	/**
	 * log4j日志打印
	 */
	private final Log log = LogFactory.getLog(CopyOfKanbjsService.class);

	/**
	 * 查询毛需求
	 * 
	 * @param page
	 * @param param
	 * @return Map<String, Object>
	 * @throws ServiceException
	 */
	public Map<String, Object> select(Pageable page) {
		Map<String, String> map = new HashMap<String, String>();
		String xuqly = "and xuqly  in ('DIP','CLV','ZCP','CYJ','BJP','WXP','CYP')";
		map.put("xuqly", xuqly);
		map.put("pageNo", String.valueOf(page.getPageNo()));
		map.put("pageSize", String.valueOf(page.getPageSize()));
		map.put("order", page.getOrder());
		map.put("sort", page.getSort());	
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kanbyhl.maoxq", map, page);
	}

	/**
	 * 查询毛需求
	 * 
	 * @param page
	 * @param param
	 * @return Map<String, Object>
	 * @throws ServiceException
	 */
	public Map<String, Object> selectAll(Maoxq bean) {
		bean.setXuqly(getxuq(bean.getXuqly()));
		bean.setXuqbc(getxuq(bean.getXuqbc()));
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kanbyhl.maoxqkanbjs", bean, bean);
	}
	/**
	 * 查询毛需求明细
	 * 
	 * @param page
	 * @param param
	 * @return Map<String, Object>
	 * @throws ServiceException
	 */
	public Map<String, Object> selectMx(Pageable page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kanbyhl.qrmaoxqmx", param, page);
	}

	/**
	 * 判断选择的毛需求类型是日还是周期
	 */
	public boolean isCycle(String xuqly) {
		// 默认false为需求类型为日
		return !(xuqly.equals(Const.KANB_MXQLX_CLV) || xuqly.equals(Const.MAOXQ_XUQLY_CYJ) || xuqly.equals(Const.MAOXQ_XUQLY_ZCJ));
	}

	/**
	 * 过滤出基础数据，插入中间表
	 * 
	 * @param wullj
	 * @return int
	 * @throws ServiceException
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public void prepareDate(Maoxqmx bean, String mos) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.clear_middle");
		List<Wullj> list = new ArrayList<Wullj>(20000);
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());

		String sql = "kanbyhl.insert_lins";
		if (mos.equalsIgnoreCase(Const.KANB_JS_NEIBMOS_RD) || mos.equalsIgnoreCase(Const.KANB_JS_NEIBMOS_RM)) {
			sql = "kanbyhl.insert_linsRMRD";
			list.addAll(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(sql, bean));
		} else {
			map.put("MOS", "(w.waibms='" + Const.KANB_JS_WAIBMOS_R1 + "' OR w.waibms='" + Const.KANB_JS_WAIBMOS_R2 + "')");
			map.put("mos", "w.waibms");
			list.addAll(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(sql, map));
			map.put("MOS", "(w.wjianglms='" + Const.KANB_JS_WAIBMOS_R1 + "' OR w.wjianglms='" + Const.KANB_JS_WAIBMOS_R2 + "')");
			map.put("mos", "w.wjianglms");
			list.addAll(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(sql, map));
			map.put("MOS", "(w.jianglms='" + Const.KANB_JS_WAIBMOS_R1 + "' OR w.jianglms='" + Const.KANB_JS_WAIBMOS_R2 + "')");
			map.put("mos", "w.jianglms");
			list.addAll(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(sql, map));
			map.put("MOS", "(w.jianglms2='" + Const.KANB_JS_WAIBMOS_R1 + "' OR w.jianglms2='" + Const.KANB_JS_WAIBMOS_R2 + "')");
			map.put("mos", "w.jianglms2");
			list.addAll(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(sql, map));
		}
		log.debug(list.size());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.xxhInsMiddle", list);
	}

	/**
	 * <p>
	 * 新循环的
	 * </p>
	 * 
	 * @param mos
	 *            供货模式
	 * @param usercenter
	 *            用户中心
	 * @param flag
	 *            新循环标识
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public void setXmosData(boolean flag, String mos, String usercenter) {
		if (flag) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("usercenter", usercenter);
			map.put("gonghms", mos);
			String sqlR2RM = "kanbyhl.selectSxR2RM";
			String sqlR1RD = "kanbyhl.selectSxR1RD";
			List<Wullj> list = new ArrayList<Wullj>(20000);
			if (mos.equalsIgnoreCase(Const.KANB_JS_WAIBMOS_R1)) {
				list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(sqlR1RD, map);
				map.put("gonghms", Const.KANB_JS_WAIBMOS_R2);
			} else if (mos.equalsIgnoreCase(Const.KANB_JS_NEIBMOS_RD)) {
				list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(sqlR1RD, map);
				map.put("gonghms", Const.KANB_JS_NEIBMOS_RM);
			}
			list.addAll(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(sqlR2RM, map));
			// 清空中间表
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.clear_middle");
			// 将筛选出来的新循环数据插入中间表
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.xxhInsMiddle", list);
		}

	}

	/**
	 * 需求类型为日 R1/RD模式产线下消耗点比例之和不为百分百，报警
	 */

	@SuppressWarnings("unchecked")
	public List<Wullj> xiaohblBJ() {
		String mos = null;
		Map<String, String> map = new HashMap<String, String>();
		mos = "and (w.waibms = '" + Const.KANB_JS_WAIBMOS_R1 + "'";
		mos += "or w.wjianglms = '" + Const.KANB_JS_WAIBMOS_R1 + "'";
		mos += "or w.wjianglms = '" + Const.KANB_JS_NEIBMOS_RD + "'";
		mos += "or w.jianglms = '" + Const.KANB_JS_WAIBMOS_R1 + "'";
		mos += "or w.jianglms = '" + Const.KANB_JS_NEIBMOS_RD + "'";
		mos += "or w.jianglms2 = '" + Const.KANB_JS_WAIBMOS_R1 + "'";
		mos += "or w.jianglms2 = '" + Const.KANB_JS_NEIBMOS_RD + "'";
		mos += "or w.mos = '" + Const.KANB_JS_NEIBMOS_RD + "')";
		map.put("MOS", mos);
		return (List<Wullj>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.xiaohbl_bj", map);
	}

	/**
	 * 通过零件表 查询计划员组
	 */
	public String getJihyz(Lingj wj) {
		// 查询零件表，得到计划员组
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhl.query_jihy", wj);
	}

	/**
	 * 看板计算异常报警
	 */
	public void kanbBJ(List<Wullj> ls, String cuowlx) {
		String cuowxxxx;
		boolean flag = cuowlx.equals(Const.KANB_LINGJGONGYS_ERROR);
		if (flag) {
			cuowxxxx = Const.YICHANG_LX4_str1;
		} else {
			cuowxxxx = Const.YICHANG_LX6_str1;
		}
		List<Yicbj> ycls = new ArrayList<Yicbj>(10000);
		ArrayList<String> paramStr = new ArrayList<String>();
		for (int i = 0; i < ls.size(); i++) {
			paramStr.clear();
			Wullj wj = ls.get(i);
			paramStr.add(wj.getUsercenter());
			paramStr.add(wj.getLingjbh());
			String jihyz = "";
			if (flag) {
				lingj.setUsercenter(wj.getUsercenter());
				lingj.setLingjbh(wj.getLingjbh());
				jihyz = this.getJihyz(lingj);
			} else {
				paramStr.add(wj.getChanx());
				jihyz = wj.getWulgyyz();
			}
			// 插入异常报警
			ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, jihyz, paramStr, wj.getUsercenter(), wj.getLingjbh(), AuthorityUtils.getSecurityUser(), Const.JISMK_kANB_CD);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
	}

	/**
	 * 需求类型为日 供应商比例之和不为百分百，报警
	 */
	@SuppressWarnings("unchecked")
	public List<Wullj> gonyfeBJ() {
		String mos = null;
		Map<String, String> map = new HashMap<String, String>();
		// 模式为r1或r2下供应商份额不为百分百
		mos = "and (w.waibms = '" + Const.KANB_JS_WAIBMOS_R2 + "'";
		mos += "or w.wjianglms = '" + Const.KANB_JS_WAIBMOS_R1 + "'";
		mos += "or w.wjianglms = '" + Const.KANB_JS_WAIBMOS_R2 + "'";
		mos += "or w.jianglms = '" + Const.KANB_JS_WAIBMOS_R2 + "'";
		mos += "or w.jianglms = '" + Const.KANB_JS_WAIBMOS_R1 + "'";
		mos += "or w.jianglms2 = '" + Const.KANB_JS_WAIBMOS_R2 + "'";
		mos += "or w.jianglms2 = '" + Const.KANB_JS_WAIBMOS_R1 + "'";
		mos += "or w.waibms = '" + Const.KANB_JS_WAIBMOS_R1 + "')";
		map.put("MOS", mos);
		return (List<Wullj>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.gongyfe_bj", map);
	}

	/**
	 * 日期 指定截取年月日
	 */
	public String getJsDate(String dateStr) {
		return dateStr.substring(0, 4) + dateStr.substring(5, 7);
	}

	/**
	 * 查询装车系数
	 */
	public BigDecimal getZhuangcxs(Wullj bean) {
		// 装车系数K
		Lingj cx = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhl.zhuangcxsK", bean);
		return cx == null ? null : cx.getZhuangcxs();
	}

	/**
	 * 获取指定版次日需求类型的需求起始时间与结束时间
	 * 
	 * @param bean
	 * @return Map<String,String>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getIntervalTime(Maoxqmx bean) {
		return (Map<String, String>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhl.maoxqsj", bean);
	}

	/**
	 * 查询开始时间到结束时间的所有版次的工作日
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public Map<String, Integer> getWorkDaysMap(String startDate, String endDate) {
		List<Map<String, Object>> calendarList = commonCalendarService.getAllBancGongZts(startDate, endDate);
		Map<String, Integer> cdmap = new HashMap<String, Integer>();
		for (Map<String, Object> calendarMap : calendarList) {
			Integer gongrts = ((BigDecimal) calendarMap.get("GONGZRTS")).intValue();
			String calendaruc = (String) calendarMap.get("USERCENTER");
			String banc = (String) calendarMap.get("BANC");
			cdmap.put(calendaruc + ":" + banc, gongrts);
		}
		return cdmap;
	}

	/**
	 * 取得对应用户中心、线边仓库或产线的工作天数
	 * 
	 * @param wullj
	 * @param bean
	 * @return wullj
	 */
	public String getRilbc(String usercenter, String appobj, String lingjbh, List<String> paramStr, List<Yicbj> ycls) {
		String jihyz = AuthorityUtils.getSecurityUser() == null ? "SYSTEM" : AuthorityUtils.getSecurityUser().getJihyz();
		// 查询对应的仓库或产线的日历版次
		String rilbc = cgmap.get(usercenter + ":" + appobj);
		String cuowlx = Const.CUOWLX_200;
		paramStr.clear();
		String cuowxxxx = "";
		if (appobj == null) {
			paramStr.add(usercenter);
			paramStr.add(lingjbh);
			cuowxxxx = Const.YICHANG_LX2_str46;
			CommonFun.insertError(cuowlx, cuowxxxx, ycls, jihyz, paramStr, usercenter, lingjbh, AuthorityUtils.getSecurityUser(), Const.JISMK_kANB_CD);
		} else if (rilbc == null) {
			paramStr.add(usercenter);
			paramStr.add(appobj);
			cuowxxxx = Const.YICHANG_LX2_str21;
			CommonFun.insertError(cuowlx, cuowxxxx, ycls, jihyz, paramStr, usercenter, lingjbh, AuthorityUtils.getSecurityUser(), Const.JISMK_kANB_CD);
		}
		return rilbc;
	}

	public int getGongZts(String usercenter, String appobj, String startTime, String endTime, String lingjbh) {
		// 查询对应的仓库或产线的日历版次
		CalendarGroup group = calendarGroupService.queryCalendarGroupObject(usercenter, appobj);
		log.debug("查询对应的仓库或产线的日历版次" + group);
		String cuowlx = Const.KANB_CALENDAR_ERROR;
		String cuowxxxx = "CKX_CALENDAR_GROUP表用户中心" + usercenter + "产线/仓库" + appobj + "数据有误";
		if (group == null || group.getRilbc() == null) {
			this.yicbjService.saveYicInfo("61", lingjbh, cuowlx, cuowxxxx);
		}
		// 查询对应产线的工作天数
		List<?> rils = calendarVersionService.queryCalendarVersionListGzr(startTime, endTime, usercenter, group.getRilbc(), "1");
		return rils.size();
	}

	/**
	 * 查询该用户中心所有零件信息
	 * 
	 * @param usercenter
	 *            用户中心
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, Object>> getLjMap(String usercenter) {
		// 将查询出来的LIST零件信息转成MAP
		List<Map<String, Object>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.queryAllLj", usercenter);
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			map.put((String) list.get(i).get("USERCENTER") + list.get(i).get("LINGJBH"), list.get(i));
		}
		list = null;
		return map;
	}

	/**
	 * 查询该用户中心所有零件消耗点信息
	 * 
	 * @param usercenter
	 *            用户中心
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, String>> getxhdMap(Wullj wl) {
		// 将查询出来的LIST零件信息转成MAP
		List<Map<String, String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.xianbck_yaohlx", wl);
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			map.put((String) list.get(i).get("USERCENTER") + list.get(i).get("LINGJBH") + list.get(i).get("XIAOHDBH"), list.get(i));
		}
		list = null;
		return map;
	}

	/**
	 * 查询该用户中心所有零件仓库信息
	 * 
	 * @param usercenter
	 *            用户中心
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Lingjck> getAllLjckMap(String usercenter) {
		// 将查询出来的LIST零件信息转成MAP
		List<Lingjck> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.xianbckAllRl", usercenter);
		Map<String, Lingjck> map = new HashMap<String, Lingjck>();
		for (int i = 0; i < list.size(); i++) {
			map.put((String) list.get(i).getUsercenter() + list.get(i).getLingjbh() + list.get(i).getCangkbh(), list.get(i));
		}
		list = null;
		return map;
	}

	/**
	 * 查询该用户中心所有零件供应商信息
	 * 
	 * @param usercenter
	 *            用户中心
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Lingjgys> getAllLjgysMap(Wullj wullj) {
		// 将查询出来的LIST零件信息转成MAP
		List<Lingjgys> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.gongysfe", wullj);
		Map<String, Lingjgys> map = new HashMap<String, Lingjgys>();
		for (int i = 0; i < list.size(); i++) {
			map.put((String) list.get(i).getUsercenter() + list.get(i).getLingjbh() + list.get(i).getGongysbh(), list.get(i));
		}
		list = null;
		return map;
	}

	/**
	 * RD异常报警检查
	 * 
	 * @param ls
	 * @return
	 */
	public List<Wullj> rDJsBj(List<Wullj> ls) {
		log.info("==========================rDJsBj STRART=============================");
		String jismk = Const.JISMK_kANB_CD;
		String cuowlx = Const.CUOWLX_200;
		String cuowxxxx = "";
		LoginUser loginuser = AuthorityUtils.getSecurityUser();
		String kaissj = timeMap == null ? date.get(Const.JSDATE) : timeMap.get("STARTTIME");
		String jiessj = timeMap == null ? date.get(Const.JSEND) : timeMap.get("ENDTIME");
		// 工作分组-每天工作小时数
		Map<String, BigDecimal> worktimeMap = commonCalendarService.getWorkTimeMap();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		ArrayList<String> paramStr = new ArrayList<String>();
		// 筛选出的结果list
		List<Wullj> resultls = new ArrayList<Wullj>(1000);
		// 加载所有的零件信息
		Map<String, Map<String, Object>> ljmap = null;
		Map<String, Map<String, String>> xhdMapAll = null;
		Map<String, Lingjck> lingjckAllMap = null;
		if (ls.size() > 0) {
			ljmap = getLjMap(ls.get(0).getUsercenter());
			// 线边库要货类型为R/K类型
			xhdMapAll = getxhdMap(ls.get(0));
			// 查询零件-仓库的包装容量
			lingjckAllMap = getAllLjckMap(ls.get(0).getUsercenter());
		}
		// 循环时间
		BigDecimal tr = null;
		for (int i = 0; i < ls.size(); i++) {
			paramStr.clear();
			Wullj wullj = ls.get(i);
			BigDecimal k = ljmap.get(wullj.getUsercenter() + wullj.getLingjbh()) == null ? null : (BigDecimal) ljmap.get(wullj.getUsercenter() + wullj.getLingjbh()).get("ZHUANGCXS");
			// 线边库要货类型为R/K类型
			Map<String, String> xhdMap = xhdMapAll.get(wullj.getUsercenter() + wullj.getLingjbh() + wullj.getXiaohdbh());
			String xianbyhlx = xhdMap == null ? null : xhdMap.get("XIANBYHLX");
			Lingjck lingjck = wullj.getXianbck() == null ? null : lingjckAllMap.get(wullj.getUsercenter() + wullj.getLingjbh() + wullj.getXianbck());
			BigDecimal bzrl = null;
			String bzlx = null;
			if ("K".equals(xianbyhlx) && lingjck != null) {
				bzlx = lingjck.getUclx();
				bzrl = lingjck.getUcrl();
			} else if ("R".equals(xianbyhlx) && lingjck != null) {
				bzlx = lingjck.getUsbzlx();
				bzrl = lingjck.getUsbzrl();
			}
			String yancsxbz = xhdMap == null ? null : xhdMap.get("YANCSXBZ");
			String key=wullj.getUsercenter()+":"+wullj.getXianbck();
			// 0006969 RD看板计算时验证线边仓库与工作时间的关联
			if(!cgmap.containsKey(key)){
				cuowxxxx = Const.YICHANG_LX2_str64;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getXiaohdbh());
				paramStr.add(wullj.getXianbck());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			}else if (wullj.getXiaohbl() == null) {
				cuowxxxx = Const.YICHANG_LX2_str33;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getXiaohdbh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			} else if (wullj.getRilbc() != null && (wullj.getGongzr() == null || wullj.getGongzr() == 0)) {
				cuowxxxx = Const.YICHANG_LX2_str34;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getRilbc());
				paramStr.add(kaissj);
				paramStr.add(jiessj);
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, loginuser == null ? "SYSTEM" : loginuser.getJihyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			} else if (k == null) {// 装车系数K
				cuowxxxx = Const.YICHANG_LX2_str2;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, loginuser == null ? "SYSTEM" : loginuser.getJihyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			} else if (xianbyhlx == null) {
				cuowxxxx = Const.KANB_JS_NEIBMOS_RD + Const.YICHANG_LX2_str8;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getXiaohdbh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			} else if (wullj.getXianbck() == null) {// 线边仓库为空
				cuowxxxx = Const.YICHANG_LX2_str5;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add("RD线边仓库为空");
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz1(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			} else if (lingjck == null) {
				cuowxxxx = Const.YICHANG_LX2_str19;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getXianbck());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz1(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			} else if (("K").equals(xianbyhlx) && (bzrl == null || bzrl.equals(BigDecimal.ZERO))) {
				cuowxxxx = Const.YICHANG_LX2_str35;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getXianbck());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz1(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			} else if (("R").equals(xianbyhlx) && (bzrl == null || bzrl.equals(BigDecimal.ZERO))) {
				cuowxxxx = Const.YICHANG_LX2_str9;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getXianbck());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz1(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			} else if (yancsxbz == null) {// 响应时间TG
				cuowxxxx = Const.YICHANG_LX2_str10;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getXiaohdbh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz1(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			}else if(wullj.getCangkshsj()==null){
				cuowxxxx = Const.YICHANG_LX2_str51;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getFenpqh());
				paramStr.add(wullj.getXianbck());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz1(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			}else if(wullj.getCangkfhsj()==null){
				cuowxxxx = Const.YICHANG_LX2_str52;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getFenpqh());
				paramStr.add(wullj.getXianbck());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz1(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			}else if (yancsxbz.equalsIgnoreCase("I") && wullj.getIbeihsj() == null) {
				cuowxxxx = Const.YICHANG_LX2_str36;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getFenpqh());
				paramStr.add(wullj.getXianbck());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz1(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			} else if (yancsxbz.equalsIgnoreCase("P") && wullj.getPbeihsj() == null) {
				cuowxxxx = Const.YICHANG_LX2_str37;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getFenpqh());
				paramStr.add(wullj.getXianbck());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz1(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			} else {
				if (yancsxbz.equalsIgnoreCase("I") && wullj.getIbeihsj() != null) {
					tr = wullj.getIbeihsj().add(wullj.getCangkshsj()).add(wullj.getCangkfhsj());
				} else if (yancsxbz.equalsIgnoreCase("P") && wullj.getPbeihsj() != null) {
					tr = wullj.getPbeihsj().add(wullj.getCangkshsj()).add(wullj.getCangkfhsj());
				}
				wullj.setZhuangcxs(k);
				wullj.setBzlx(bzlx);
				wullj.setBzrl(bzrl);
				wullj.setTr(tr.multiply(BigDecimal.valueOf(24)).divide(worktimeMap.get(wullj.getUsercenter() + ":" + wullj.getChanx()), 6, BigDecimal.ROUND_UP));
				wullj.setXianbyhlx(xianbyhlx);
				wullj.setYancsxbz(yancsxbz);
				wullj.setJihyz((String) ljmap.get(wullj.getUsercenter() + wullj.getLingjbh()).get("JIHY"));
				resultls.add(wullj);
			}
		}
		// 批量写异常报警
		lingjckAllMap = null;
		ljmap = null;
		xhdMapAll = null;
		ls = null;
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		log.info("==========================rDJsBj END=============================");
		return resultls;
	}

	/**
	 * R1异常报警检查
	 * 
	 * @param ls
	 * @return
	 */
	public List<Wullj> r1R2JsBj(List<Wullj> ls, String mos) {
		log.info("==========================r1R2JsBj STRART=============================");
		String jismk = Const.JISMK_kANB_CD;
		String cuowlx = Const.CUOWLX_200;
		String cuowxxxx = "";
		LoginUser loginuser = AuthorityUtils.getSecurityUser();
		String kaissj = timeMap == null ? date.get(Const.JSDATE) : timeMap.get("STARTTIME");
		String jiessj = timeMap == null ? date.get(Const.JSEND) : timeMap.get("ENDTIME");
		// 工作分组-每天工作小时数
		Map<String, BigDecimal> worktimeMap = commonCalendarService.getWorkTimeMap();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		ArrayList<String> paramStr = new ArrayList<String>();
		// 筛选出的结果list
		List<Wullj> resultls = new ArrayList<Wullj>(1000);
		// 加载所有的零件信息
		Map<String, Map<String, Object>> ljmap = null;
		Map<String, Lingjgys> lingjGYSAllMap = null;
		if (ls.size() > 0) {
			ljmap = getLjMap(ls.get(0).getUsercenter());
			// 查询零件-仓库的包装容量
			lingjGYSAllMap = getAllLjgysMap(ls.get(0));
		}
		// R1/R2模式判断
		boolean msflag = mos.equalsIgnoreCase(Const.KANB_JS_WAIBMOS_R1);
		for (int i = 0; i < ls.size(); i++) {
			log.debug(i + "==========================r1R2JsBj=============================");
			Wullj wullj = ls.get(i);
			paramStr.clear();
			// 装车系数K
			BigDecimal k = ljmap.get(wullj.getUsercenter() + wullj.getLingjbh()) == null ? null : (BigDecimal) ljmap.get(wullj.getUsercenter() + wullj.getLingjbh()).get("ZHUANGCXS");
			// 查询零件供应商
			Lingjgys lgs = lingjGYSAllMap.get(wullj.getUsercenter() + wullj.getLingjbh() + wullj.getGongysbh());
			if (wullj.getXiaohbl() == null && msflag) {
				log.debug(i + "==========================r1R2JsBj  消耗比例=============================");
				cuowxxxx = Const.YICHANG_LX2_str33;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getXiaohdbh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else if (wullj.getRilbc() != null && (wullj.getGongzr() == null || wullj.getGongzr() == 0)) {
				log.debug(i + "==========================r1R2JsBj  工作天数为零=============================");
				cuowxxxx = Const.YICHANG_LX2_str34;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getRilbc());
				paramStr.add(kaissj);
				paramStr.add(jiessj);
				log.debug(i + "===========================r1R2JsBj  工作天数为零============================paramStr"+paramStr);
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, loginuser == null ? "SYSTEM" : loginuser.getJihyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else if (k == null) {// 装车系数K
				log.debug(i + "==========================r1R2JsBj  装车系数K=============================");
				cuowxxxx = Const.YICHANG_LX2_str2;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, loginuser == null ? "SYSTEM" : loginuser.getJihyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else if (lgs.getUaucgs() == null || lgs.getUcrl() == null || lgs.getUcrl().equals(BigDecimal.ZERO) || lgs.getUaucgs().equals(BigDecimal.ZERO)) {
				log.debug(i + "==========================r1R2JsBj  包装容量=============================");
				cuowxxxx = Const.YICHANG_LX2_str28;
				paramStr.add(mos);
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getGongysbh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else if (wullj.getBeihzq() == null || wullj.getYunszq() == null) {// 循环时间TR
				log.debug(i + "==========================r1R2JsBj  循环时间TR=============================");
				cuowxxxx = Const.YICHANG_LX2_str14;
				paramStr.add(mos);
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getGongysbh());
				paramStr.add(wullj.getFenpqh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else if (wullj.getSonghpc() == null || wullj.getSonghpc().equals(0)) {
				log.debug(i + "==========================r1R2JsBj  送货频次=============================");
				cuowxxxx = Const.YICHANG_LX2_str38;
				paramStr.add(mos);
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getGongysbh());
				paramStr.add(wullj.getFenpqh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else {
				wullj.setUabzlx(lgs.getUabzlx());
				wullj.setUaucgs(lgs.getUaucgs());
				wullj.setUcbzlx(lgs.getUcbzlx());
				wullj.setUcrl(lgs.getUcrl());
				BigDecimal tr = wullj.getBeihzq().add(wullj.getYunszq());
				// 产线-仓库
				String appobj = msflag ? wullj.getChanx() : wullj.getMudd();
				wullj.setTr(tr.multiply(BigDecimal.valueOf(24)).divide(worktimeMap.get(wullj.getUsercenter() + ":" + appobj), 6, BigDecimal.ROUND_UP));
				wullj.setZhuangcxs(k);
				wullj.setJihyz((String) ljmap.get(wullj.getUsercenter() + wullj.getLingjbh()).get("JIHY"));
				resultls.add(wullj);
			}
		}
		// 批量写异常报警
		ljmap = null;
		lingjGYSAllMap = null;
		ls = null;
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		log.debug("==========================r1R2JsBj END=============================");
		return resultls;
	}

	/**
	 * RM异常报警检查
	 * 
	 * @param ls
	 * @return
	 */
	public List<Wullj> rMJsBj(List<Wullj> ls) {
		log.info("==========================rMJsBj STRART=============================");
		String jismk = Const.JISMK_kANB_CD;
		String cuowlx = Const.CUOWLX_200;
		String cuowxxxx = "";
		LoginUser loginuser = AuthorityUtils.getSecurityUser();
		String kaissj = timeMap == null ? date.get(Const.JSDATE) : timeMap.get("STARTTIME");
		String jiessj = timeMap == null ? date.get(Const.JSEND) : timeMap.get("ENDTIME");
		// 工作分组-每天工作小时数
		Map<String, BigDecimal> worktimeMap = commonCalendarService.getWorkTimeMap();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		ArrayList<String> paramStr = new ArrayList<String>();
		// 筛选出的结果list
		List<Wullj> resultls = new ArrayList<Wullj>(1000);
		// 加载所有的零件信息
		Map<String, Map<String, Object>> ljmap = null;
		Map<String, Lingjck> lingjckAllMap = null;
		if (ls.size() > 0) {
			ljmap = getLjMap(ls.get(0).getUsercenter());
			// 查询零件-仓库的包装容量
			lingjckAllMap = getAllLjckMap(ls.get(0).getUsercenter());
		}
		for (int i = 0; i < ls.size(); i++) {
			Wullj wullj = ls.get(i);
			paramStr.clear();
			// 装车系数K
			BigDecimal k = ljmap.get(wullj.getUsercenter() + wullj.getLingjbh()) == null ? null : (BigDecimal) ljmap.get(wullj.getUsercenter() + wullj.getLingjbh()).get("ZHUANGCXS");
			// 查询零件-仓库的包装容量
			Lingjck lingjck = wullj.getXianbck() == null ? null : lingjckAllMap.get(wullj.getUsercenter() + wullj.getLingjbh() + wullj.getXianbck());
			
			String key=wullj.getUsercenter()+":"+wullj.getDinghck();
			// 0006969 RM看板计算时验证订货仓库与工作时间的关联
			if(!cgmap.containsKey(key)){
				cuowxxxx = Const.YICHANG_LX2_str63;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getXianbck());
				paramStr.add(wullj.getDinghck());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			}else if (wullj.getRilbc() != null && (wullj.getGongzr() == null || wullj.getGongzr() == 0)) {
				cuowxxxx = Const.YICHANG_LX2_str34;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getRilbc());
				paramStr.add(kaissj);
				paramStr.add(jiessj);
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, loginuser == null ? "SYSTEM" : loginuser.getJihyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else if (k == null) {// 装车系数K
				cuowxxxx = Const.YICHANG_LX2_str2;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, loginuser == null ? "SYSTEM" : loginuser.getJihyz(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else if (wullj.getDinghck() == null) {// 订货仓库为空
				cuowxxxx = Const.YICHANG_LX2_str5;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add("RM订货仓库为空");
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz2(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(), loginuser, jismk);
			} else if (wullj.getXianbck() == null) {// 订货仓库为空
				cuowxxxx = Const.YICHANG_LX2_str5;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add("RM线边仓库为空");
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz2(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else if (lingjck == null) {// 零件仓库异常报警
				cuowxxxx = Const.YICHANG_LX2_str19;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getXianbck());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz2(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else if (wullj.getCangkshsj2() == null || wullj.getCangkfhsj2() == null || wullj.getBeihsj2() == null) {
				// RM循环时间异常报警
				cuowxxxx = Const.YICHANG_LX2_str61;
				paramStr.add(Const.KANB_JS_NEIBMOS_RM);
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getDinghck());
				paramStr.add(wullj.getFenpqh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz2(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else if (wullj.getCangkshpc2() == null) {
				// RM送货频次异常报警
				cuowxxxx = Const.YICHANG_LX2_str62;
				paramStr.add(Const.KANB_JS_NEIBMOS_RM);
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getDinghck());
				paramStr.add(wullj.getFenpqh());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz2(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else if (lingjck.getUsbzrl() == null || lingjck.getUsbzrl().equals(BigDecimal.ZERO)) {
				// RM包装容量异常报警
				cuowxxxx = Const.YICHANG_LX2_str9;
				paramStr.add(wullj.getUsercenter());
				paramStr.add(wullj.getLingjbh());
				paramStr.add(wullj.getXianbck());
				ycls = CommonFun.insertError(cuowlx, cuowxxxx, ycls, wullj.getWulgyyz2(), paramStr, wullj.getUsercenter(), wullj.getLingjbh(),
						loginuser, jismk);
			} else {
				wullj.setZhuangcxs(k);
				wullj.setBzlx(lingjck.getUsbzlx());
				wullj.setBzrl(lingjck.getUsbzrl());
				// 循环时间TR
				BigDecimal tr = wullj.getCangkshsj2().add(wullj.getCangkfhsj2()).add(wullj.getBeihsj2());
				wullj.setTr(tr.multiply(BigDecimal.valueOf(24)).divide(worktimeMap.get(wullj.getUsercenter() + ":" + wullj.getXianbck()), 6, BigDecimal.ROUND_UP));
				wullj.setJihyz((String) ljmap.get(wullj.getUsercenter() + wullj.getLingjbh()).get("JIHY"));
				// 加入计算list
				resultls.add(wullj);
			}
		}
		// 批量写异常报警
		ljmap = null;
		lingjckAllMap = null;
		ls = null;
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		log.info("==========================rMJsBj END=============================");
		return resultls;
	}

	/**
	 * 供货模式RD计算
	 */
	public Kanbxhgm computeRD(Wullj wullj) {
		log.info("==========================computeRD STRART=============================");
		// cmj
		BigDecimal cmj = (wullj.getXuqsl() == null ? BigDecimal.ZERO : wullj.getXuqsl()).multiply(wullj.getXiaohbl()).divide(BigDecimal.valueOf(wullj.getGongzr()), 6, BigDecimal.ROUND_UP);
		// a=cmj^0.6
		BigDecimal a = BigDecimal.valueOf(Math.pow(cmj.doubleValue(), 0.6));
		// b=k^0.4
		BigDecimal b = BigDecimal.valueOf(Math.pow(wullj.getZhuangcxs().doubleValue(), 0.4));
		// cmjmax = cmj+2.91*cmj^0.6*k^0.4
		BigDecimal cmjmax = cmj.add(BigDecimal.valueOf(2.91).multiply(a).multiply(b)).setScale(6, BigDecimal.ROUND_UP);
		// 求循环规模KB
		BigDecimal kbRD = cmjmax.multiply(wullj.getTr()).divide(wullj.getBzrl(), 0, BigDecimal.ROUND_UP);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wullj", wullj);
		map.put("lingjgys", null);
		map.put("cmj", cmj);
		map.put("cmjmax", cmjmax);
		map.put("kbk", kbRD);
		log.info("==========================computeRD END=============================");
		return setKbean(Const.KANB_JS_NEIBMOS_RD, map);
	}

	/**
	 * 供货模式R1计算
	 */
	public Kanbxhgm computeR1(Wullj wullj) {
		log.info("==========================computeR1 STRART=============================");
		// 供应商份额
		BigDecimal gongysfe = wullj.getGyfe() == null ? BigDecimal.ZERO : wullj.getGyfe();
		log.debug("=====================================R1供应份额" + gongysfe);
		// cmj
		BigDecimal cmj = (wullj.getXuqsl() == null ? BigDecimal.ZERO : wullj.getXuqsl()).multiply(wullj.getXiaohbl() == null ? BigDecimal.ZERO : wullj.getXiaohbl().multiply(gongysfe)).divide(
				BigDecimal.valueOf(wullj.getGongzr()), 6, BigDecimal.ROUND_UP);
		// a=cmj^0.6
		BigDecimal a = BigDecimal.valueOf(Math.pow(cmj.doubleValue(), 0.6));
		// b=k^04
		BigDecimal b = BigDecimal.valueOf(Math.pow(wullj.getZhuangcxs().doubleValue(), 0.4));
		// cmjmax = cmj+2.91*cmj^06*k^04
		BigDecimal cmjmax = cmj.add(BigDecimal.valueOf(2.91).multiply(a).multiply(b)).setScale(6, BigDecimal.ROUND_UP);
		BigDecimal bzrl = wullj.getUaucgs().multiply(wullj.getUcrl());
		BigDecimal tr = wullj.getTr();
		// 响应时间TG
		BigDecimal tg = BigDecimal.ONE.divide(wullj.getSonghpc(), 6, BigDecimal.ROUND_UP);
		// 求循环规模KB
		BigDecimal kbR1 = cmjmax.multiply(tr.add(tg)).divide(bzrl, 0, BigDecimal.ROUND_UP);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wullj", wullj);
		map.put("cmj", cmj);
		map.put("cmjmax", cmjmax);
		map.put("kbk", kbR1);
		log.info("==========================computeR1 END=============================");
		return setKbean(Const.KANB_JS_WAIBMOS_R1, map);
	}

	private Kanbxhgm setKbean(String mos, Map<String, Object> map) {
		log.info("==========================setKbean STRART=============================");
		BigDecimal kbk = (BigDecimal) map.get("kbk");
		BigDecimal cmj = (BigDecimal) map.get("cmj");
		BigDecimal cmjmax = (BigDecimal) map.get("cmjmax");
		Wullj wullj = (Wullj) map.get("wullj");
		// 放入看板循环规模数据
		Kanbxhgm kanBean = new Kanbxhgm();
		// 用户中心
		kanBean.setUsercenter(wullj.getUsercenter());
		// 零件号
		kanBean.setLingjbh(wullj.getLingjbh());
		// 供货模式
		kanBean.setGonghms(mos);
		// 路径代码
		kanBean.setLujdm(wullj.getLujbh());
		if (mos.equalsIgnoreCase(Const.KANB_JS_WAIBMOS_R1) || mos.equalsIgnoreCase(Const.KANB_JS_NEIBMOS_RD)) {
			// 线边要货类型
			kanBean.setXianbyhlx(wullj.getXianbyhlx());
			// 是否延迟上线
			kanBean.setYancsxbz(wullj.getYancsxbz());
		}
		if (mos.equalsIgnoreCase(Const.KANB_JS_WAIBMOS_R2) || mos.equalsIgnoreCase(Const.KANB_JS_WAIBMOS_R1)) {
			// uc类型
			kanBean.setUclx(wullj.getUcbzlx());
			// uc容量
			kanBean.setUcrl(wullj.getUcrl());
			// ua类型
			kanBean.setUmlx(wullj.getUabzlx());
			// ua容量
			kanBean.setUmzucgs(wullj.getUaucgs());
			// ua零件数量
			kanBean.setUmljsl(wullj.getUaucgs().multiply(wullj.getUcrl()));
		} else if (mos.equalsIgnoreCase(Const.KANB_JS_NEIBMOS_RD) || mos.equalsIgnoreCase(Const.KANB_JS_NEIBMOS_RM)) {
			// uc类型
			kanBean.setUclx(wullj.getBzlx());
			// uc容量
			kanBean.setUcrl(wullj.getBzrl());

		}
		if (mos.equalsIgnoreCase(Const.KANB_JS_WAIBMOS_R1) || mos.equalsIgnoreCase(Const.KANB_JS_NEIBMOS_RD)) {
			// 消耗点
			kanBean.setXiaohd(wullj.getXiaohdbh());
			// 客户
			kanBean.setKehd(wullj.getXiaohdbh());
		} else if (mos.equalsIgnoreCase(Const.KANB_JS_WAIBMOS_R2)) {
			// 线边仓库
			kanBean.setCangkdm(wullj.getMudd());
			// 客户
			kanBean.setKehd(wullj.getMudd());
			// 订货仓库
			kanBean.setDinghck(wullj.getMudd());
		} else if (mos.equalsIgnoreCase(Const.KANB_JS_NEIBMOS_RM)) {
			// 线边仓库
			kanBean.setCangkdm(wullj.getXianbck());
			// 客户
			kanBean.setKehd(wullj.getXianbck());
			// 订货仓库
			kanBean.setDinghck(wullj.getDinghck());
		}

		// 计划员组
		kanBean.setJihydm(wullj.getJihyz());

		// 产线
		kanBean.setChanx(wullj.getChanx());
		// CMJ
		kanBean.setCmj(cmj);
		// CMJmax
		kanBean.setCjmax(cmjmax);
		// 计算循环规模
		kanBean.setJisxhgm(kbk);
		// 是否是将来模式
		kanBean.setJianglms(Const.STRING_ONE.equalsIgnoreCase(wullj.getShifjlms()) ? Const.STRING_ONE : Const.STRING_ZERO);
		log.info("==========================setKbean END=============================");
		return kanBean;
	}

	/**
	 * 供货模式R2计算
	 */
	public Kanbxhgm computeR2(Wullj wullj) {
		log.info("==========================computeR2 STRART=============================");
		// 供应商份额
		BigDecimal gongysfe = wullj.getGyfe() == null ? BigDecimal.ZERO : wullj.getGyfe();
		log.debug("=====================================R2供应份额" + gongysfe);
		// 线边仓库cmj
		BigDecimal cmj = (wullj.getXuqsl() == null ? BigDecimal.ZERO : wullj.getXuqsl()).multiply(gongysfe).divide(BigDecimal.valueOf(wullj.getGongzr()), 6, BigDecimal.ROUND_UP);
		// a=cmj^06
		BigDecimal a = BigDecimal.valueOf(Math.pow(cmj.doubleValue(), 0.6));
		// b=k^04
		BigDecimal b = BigDecimal.valueOf(Math.pow(wullj.getZhuangcxs().doubleValue(), 0.4));
		// cmjmax = cmj+2.91*cmj^06*k^04
		BigDecimal cmjmax = cmj.add(BigDecimal.valueOf(2.91).multiply(a).multiply(b)).setScale(6, BigDecimal.ROUND_UP);
		BigDecimal tr = wullj.getTr();
		// 响应时间TG
		BigDecimal tg = BigDecimal.ONE.divide(wullj.getSonghpc(), 6, BigDecimal.ROUND_UP);
		// 求循环规模KB
		BigDecimal kbR2 = cmjmax.multiply(tr.add(tg)).divide(wullj.getUaucgs().multiply(wullj.getUcrl()), 0, BigDecimal.ROUND_UP);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wullj", wullj);
		map.put("cmj", cmj);
		map.put("cmjmax", cmjmax);
		map.put("kbk", kbR2);
		log.info("==========================computeR2 END=============================");
		return setKbean(Const.KANB_JS_WAIBMOS_R2, map);
	}

	/**
	 * 供货模式RM计算
	 */
	public Kanbxhgm computeRM(Wullj wullj) {
		log.info("==========================computeRM STRART=============================");
		// 线边仓库cmj
		BigDecimal cmj = wullj.getXuqsl() == null ? BigDecimal.ZERO : wullj.getXuqsl().divide(BigDecimal.valueOf(wullj.getGongzr()), 6, BigDecimal.ROUND_UP);
		// a=cmj^06
		BigDecimal a = BigDecimal.valueOf(Math.pow(cmj.doubleValue(), 0.6));
		// b=k^04
		BigDecimal b = BigDecimal.valueOf(Math.pow(wullj.getZhuangcxs().doubleValue(), 0.4));
		// cmjmax = cmj+2.91*cmj^06*k^04
		BigDecimal cmjmax = cmj.add(BigDecimal.valueOf(2.91).multiply(a).multiply(b)).setScale(6, BigDecimal.ROUND_UP);
		// 循环时间
		BigDecimal tr = wullj.getTr();
		// 响应时间TG
		BigDecimal tg = BigDecimal.ONE.divide(wullj.getCangkshpc2(), 6, BigDecimal.ROUND_UP);
		// 求循环规模KB
		BigDecimal kbRM = cmjmax.multiply(tr.add(tg)).divide(wullj.getBzrl(), 0, BigDecimal.ROUND_UP);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wullj", wullj);
		map.put("cmj", cmj);
		map.put("cmjmax", cmjmax);
		map.put("kbk", kbRM);
		log.info("==========================computeRM END=============================");
		return setKbean(Const.KANB_JS_NEIBMOS_RM, map);
	}

	/**
	 * 存在指定供应商时,该供应商份额为100% 剔除一个用户中心，零件下对应的其它供应商
	 */
	public List<Wullj> filterSuppliers(List<Wullj> wls) {
		log.info("==========================filterSuppliers START=============================");
		Map<String, String> flagMap = new HashMap<String, String>();
		// 找出有指定供应商的用户中心下的零件
		for (int i = 0; i < wls.size(); i++) {
			Wullj wullj = wls.get(i);
			if (wullj.getZhidgys() != null) {
				flagMap.put(wullj.getUsercenter() + "." + wullj.getLingjbh() + "." + wullj.getFenpqh(), wullj.getZhidgys());
			}
		}
		// 剔除有指定供应商的用户中心下的零件的其它供应商
		for (int i = 0; i < wls.size(); i++) {
			Wullj wullj = wls.get(i);
			String key = wullj.getUsercenter() + "." + wullj.getLingjbh() + "." + wullj.getFenpqh();
			if (flagMap.containsKey(key)) {
				if (!wullj.getGongysbh().equalsIgnoreCase(flagMap.get(key))) {
					wls.remove(i);
					i--;
				} else {
					// 供应份额设为100%
					wullj.setGongysbh(wullj.getZhidgys());
					wullj.setGyfe(BigDecimal.ONE);
				}
			}
		}
		log.info("==========================filterSuppliers END=============================");
		return wls;
	}

	/**
	 * 查询指定模式关联毛需求过滤数据
	 * 
	 * @param bean
	 * @param constMos
	 * @param selMos
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Wullj> getWullj(Maoxqmx bean, String constMos, String selMos) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("xuqbc", bean.getXuqbc());
		map.put("usercenter", bean.getUsercenter());
		map.put("mos", constMos);
		List<Wullj> ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(selMos, map);
		// 将来模式
		map.remove("mos");
		String jlms = "";
		if (constMos.equals(Const.KANB_JS_WAIBMOS_R1) || constMos.equals(Const.KANB_JS_WAIBMOS_R2)) {
			jlms = "wjianglms";
		} else if (constMos.equals(Const.KANB_JS_NEIBMOS_RM)) {
			jlms = "jianglms2";
		} else if (constMos.equals(Const.KANB_JS_NEIBMOS_RD)) {
			jlms = "jianglms";
		}
		map.put(jlms, constMos);
		List futureMode = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(selMos, map);
		map.remove(jlms);
		ls.addAll(filterIfFutureMode(futureMode, constMos, bean.getUsercenter()));
		return ls;
	}

	/**
	 * 需求类型为日，内部模式为RD的CMJ和CMJMAX
	 */
	public List<Kanbxhgm> jsRRD(Maoxqmx bean) {
		/*
		 * Map<String, String> map = new HashMap<String, String>();
		 * map.put("xuqbc", bean.getXuqbc()); map.put("mos",
		 * Const.KANB_JS_NEIBMOS_RD); List<Wullj> ls =
		 * baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.jsRRD", map);
		 */
		List<Wullj> ls = getWullj(bean, Const.KANB_JS_NEIBMOS_RD, "kanbyhl.jsRRD");
		List<Kanbxhgm> kanb = new ArrayList<Kanbxhgm>(10000);
		List<String> parameter = new ArrayList<String>();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		for (int i = 0; i < ls.size(); i++) {
			Wullj wullj = ls.get(i);
			String rilbc = this.getRilbc(wullj.getUsercenter(), wullj.getChanx(), wullj.getLingjbh(), parameter, ycls);
			if(StringUtils.isEmpty(rilbc)){
				ls.remove(i);
				i--;continue;
			}
			Integer gzDays = allWorkDays.get(wullj.getUsercenter() + ":" + rilbc);
			wullj.setGongzr(gzDays);
			wullj.setRilbc(rilbc);
		}
		// 释放内存，插入异常报警
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		parameter = null;
		ycls = null;
		List<Wullj> yiCheckLs = this.rDJsBj(ls);
		for (int i = 0; i < yiCheckLs.size(); i++) {
			kanb.add(this.computeRD(yiCheckLs.get(i)));
		}
		return kanb;
	}

	/**
	 * 需求类型为日, 计算外部模式为R1的CMJ和CMJMAX
	 */
	public List<Kanbxhgm> jsRR1(Maoxqmx bean) {
		/*
		 * Map<String, String> map = new HashMap<String, String>();
		 * map.put("xuqbc", bean.getXuqbc()); map.put("mos",
		 * Const.KANB_JS_WAIBMOS_R1); List<Wullj> ls =
		 * baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.jsRR1", map);
		 */
		List<Wullj> ls = this.filterSuppliers(getWullj(bean, Const.KANB_JS_WAIBMOS_R1, "kanbyhl.jsRR1"));
		List<Kanbxhgm> kanb = new ArrayList<Kanbxhgm>(10000);
		// 异常报警参数
		List<String> parameter = new ArrayList<String>();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		for (int i = 0; i < ls.size(); i++) {
			Wullj wullj = ls.get(i);
			String rilbc = this.getRilbc(wullj.getUsercenter(), wullj.getChanx(), wullj.getLingjbh(), parameter, ycls);
			if(StringUtils.isEmpty(rilbc)){
				ls.remove(i);
				i--;continue;
			}
			Integer gzDays = allWorkDays.get(wullj.getUsercenter() + ":" + rilbc);
			wullj.setGongzr(gzDays);
			wullj.setRilbc(rilbc);
		}
		// 释放内存，插入异常报警
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		parameter = null;
		ycls = null;
		List<Wullj> yiCheckLs = this.r1R2JsBj(ls, Const.KANB_JS_WAIBMOS_R1);
		for (int i = 0; i < yiCheckLs.size(); i++) {
			kanb.add(this.computeR1(yiCheckLs.get(i)));
		}
		return kanb;
	}

	/**
	 * 需求类型为日, 计算外部模式为RM的CMJ和CMJMAX
	 */
	public List<Kanbxhgm> jsRRM(Maoxqmx bean) {
		/*
		 * Map<String, String> map = new HashMap<String, String>();
		 * map.put("xuqbc", bean.getXuqbc()); map.put("mos",
		 * Const.KANB_JS_NEIBMOS_RM); List<Wullj> ls =
		 * baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.jsRRM", map);
		 */
		List<Wullj> ls = this.sumAumontToRmCk(getWullj(bean, Const.KANB_JS_NEIBMOS_RM, "kanbyhl.jsRRM"));
		List<Kanbxhgm> kanb = new ArrayList<Kanbxhgm>(10000);
		// 异常报警参数
		List<String> parameter = new ArrayList<String>();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		for (int i = 0; i < ls.size(); i++) {
			Wullj wullj = ls.get(i);
			String rilbc = this.getRilbc(wullj.getUsercenter(), wullj.getXianbck(), wullj.getLingjbh(), parameter, ycls);
			if(StringUtils.isEmpty(rilbc)){
				ls.remove(i);
				i--;continue;
			}
			Integer gzDays = allWorkDays.get(wullj.getUsercenter() + ":" + rilbc);
			wullj.setGongzr(gzDays);
			wullj.setRilbc(rilbc);
		}
		// 释放内存，插入异常报警
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		parameter = null;
		ycls = null;
		List<Wullj> yiCheckLs = this.rMJsBj(ls);
		for (int i = 0; i < yiCheckLs.size(); i++) {
			kanb.add(this.computeRM(yiCheckLs.get(i)));
		}
		return kanb;
	}

	/**
	 * 需求类型为日, 计算外部模式为R2的CMJ和CMJMAX
	 */
	public List<Kanbxhgm> jsRR2(Maoxqmx bean) {
		List<Wullj> gysls = this.filterSuppliers(getWullj(bean, Const.KANB_JS_WAIBMOS_R2, "kanbyhl.jsRR2"));
		List<Wullj> ls = this.sumAumontToCk(gysls);
		List<Kanbxhgm> kanb = new ArrayList<Kanbxhgm>(10000);
		// 异常报警参数
		List<String> parameter = new ArrayList<String>();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		for (int i = 0; i < ls.size(); i++) {
			Wullj wullj = ls.get(i);
			String rilbc = this.getRilbc(wullj.getUsercenter(), wullj.getMudd(), wullj.getLingjbh(), parameter, ycls);
			if(StringUtils.isEmpty(rilbc)){
				ls.remove(i);
				i--;continue;
			}
			Integer gzDays = allWorkDays.get(wullj.getUsercenter() + ":" + rilbc);
			wullj.setGongzr(gzDays);
			wullj.setRilbc(rilbc);
		}
		// 释放内存，插入异常报警
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		parameter = null;
		ycls = null;
		List<Wullj> yiCheckLs = this.r1R2JsBj(ls, Const.KANB_JS_WAIBMOS_R2);
		for (int i = 0; i < yiCheckLs.size(); i++) {
			kanb.add(this.computeR2(yiCheckLs.get(i)));
		}
		return kanb;
	}

	/**
	 * 求某一年里某月的开始日期
	 * 
	 */
	public String monthOfStart(String date) {
		// 设置日期格式
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String ret = "";
		try {
			sf.format(sf.parse(date));
			Calendar tempcl = sf.getCalendar();
			// 设置制定月份的开始日
			tempcl.set(Calendar.DATE, 1);
			// 年月日重新组合
			ret = tempcl.get(Calendar.YEAR) + "- " + (tempcl.get(Calendar.MONTH) + 1) + "- " + tempcl.get(Calendar.DATE);
			// 对重新组合的年月日进行yyyy-MM-dd格式化
			ret = sf.format(sf.parseObject(ret));
		} catch (ParseException e) {
			log.info(e.getMessage());
		}
		return ret;
	}

	/**
	 * 求某一年里某月的结束日期
	 * 
	 */
	public String monthOfEnd(String date) {
		// 设置日期格式
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String ret = "";
		try {
			sf.format(sf.parse(date));
			Calendar tempcl = sf.getCalendar();
			// 获得制定月份的最大天数
			int maxDayOfMonthOfYear = tempcl.getActualMaximum(Calendar.DATE);
			// 年月日重新组合
			ret = tempcl.get(Calendar.YEAR) + "- " + (tempcl.get(Calendar.MONTH) + 1) + "- " + maxDayOfMonthOfYear;
			// 对重新组合的年月日进行yyyy-MM-dd格式化
			ret = sf.format(sf.parseObject(ret));
		} catch (ParseException e) {
			log.info(e.getMessage());
		}
		return ret;
	}

	/**
	 * 查询毛需求明细 以用户中心、零件号、产线汇总 汇总到产线
	 * 
	 * @param map
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHzChanx(Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.chanx_xqsl", map);
	}

	/**
	 * 计算需求类型为周期，模式为RD的看板规模计算
	 */
	public List<Kanbxhgm> jsZRD(Maoxqmx bean) {
		String xuqbc = bean.getXuqbc();
		String mos = Const.KANB_JS_NEIBMOS_RD;
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("xuqbc", xuqbc);
		map.put("mos", mos);
		// 看板循环规模计算，存数据
		List<Kanbxhgm> kanb = new ArrayList<Kanbxhgm>(10000);
		List<Wullj> ls = new ArrayList<Wullj>(10000);
		if (null == date.get("retStart")) {
			// 未跨月
			log.info("==========================RMSTRART NOT ACROSS MONTH START=============================");
			ls = notAcrossMonth(mos, map, bean.getUsercenter());
			log.info("==========================RMSTRART NOT ACROSS MONTH END=============================");
		} else {
			// 跨月
			// 数据过滤
			List<Wullj> all = this.arossMonthDateFilter(mos, this.kuaZqXq(map), bean.getUsercenter());
			ls = all;

		}
		// 计算cmj
		// 异常报警参数
		List<String> parameter = new ArrayList<String>();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		for (int i = 0; i < ls.size(); i++) {
			Wullj wullj = ls.get(i);
			String rilbc = this.getRilbc(wullj.getUsercenter(), wullj.getChanx(), wullj.getLingjbh(), parameter, ycls);
			if(StringUtils.isEmpty(rilbc)){
				ls.remove(i);
				i--;continue;
			}
			Integer gzDays = allWorkDays.get(wullj.getUsercenter() + ":" + rilbc);
			wullj.setGongzr(gzDays);
			wullj.setRilbc(rilbc);
		}
		// 释放内存，插入异常报警
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		parameter = null;
		ycls = null;
		List<Wullj> yiCheckLs = this.rDJsBj(ls);
		for (int i = 0; i < yiCheckLs.size(); i++) {
			kanb.add(this.computeRD(yiCheckLs.get(i)));
		}
		return kanb;
	}

	/**
	 * 周期计算，R1模式
	 */
	public List<Kanbxhgm> jsZR1(Maoxqmx bean) {
		log.info("==========================jsZR1  R1START=============================");
		String xuqbc = bean.getXuqbc();
		String mos = Const.KANB_JS_WAIBMOS_R1;
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("xuqbc", xuqbc);
		map.put("mos", mos);
		// 看板循环规模计算，存数据
		List<Kanbxhgm> kanb = new ArrayList<Kanbxhgm>(10000);
		List<Wullj> ls = new ArrayList<Wullj>(10000);
		if (null == date.get("retStart")) {
			// 未跨月
			log.info("==========================R1STRART NOT ACROSS MONTH START=============================");
			ls = notAcrossMonth(mos, map, bean.getUsercenter());
			log.info("==========================R1STRART NOT ACROSS MONTH END=============================");
		} else {
			// 跨月
			log.info("==========================R1STRART ACROSS MONTH START=============================");
			// 数据过滤
			ls = arossMonthDateFilter(mos, this.kuaZqXq(map), bean.getUsercenter());
			log.info("==========================R1STRART ACROSS MONTH END=============================");
		}
		List<Wullj> list = this.filterSuppliers(ls);
		// 计算CMJ/CMJMAX/循环规模
		// 异常报警参数
		List<String> parameter = new ArrayList<String>();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		for (int i = 0; i < list.size(); i++) {
			Wullj wullj = ls.get(i);
			String rilbc = this.getRilbc(wullj.getUsercenter(), wullj.getChanx(), wullj.getLingjbh(), parameter, ycls);
			if(StringUtils.isEmpty(rilbc)){
				ls.remove(i);
				i--;continue;
			}
			Integer gzDays = allWorkDays.get(wullj.getUsercenter() + ":" + rilbc);
			wullj.setGongzr(gzDays);
			wullj.setRilbc(rilbc);
		}
		// 释放内存，插入异常报警
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		parameter = null;
		ycls = null;
		List<Wullj> yiCheckLs = this.r1R2JsBj(ls, Const.KANB_JS_WAIBMOS_R1);
		for (int i = 0; i < yiCheckLs.size(); i++) {
			kanb.add(this.computeR1(yiCheckLs.get(i)));
		}
		log.info("==========================jsZR1  R1END=============================");
		return kanb;
	}

	/**
	 * 跨月周期需求物流路径数据筛选
	 * 
	 * @param mos
	 *            看板模式
	 * @param map
	 * @return
	 */
	// TODO
	@SuppressWarnings("unchecked")
	private List<Wullj> arossMonthDateFilter(String mos, Map<String, String> list, String usercenter) {
		log.debug("arossMonthDateFilter start");
		Map<String, String> map = new HashMap<String, String>();
		// map.put("usercenter", mx.get("USERCENTER"));
		// map.put("lingjbh", mx.getLingjbh());
		// map.put("xuqsl", mx.getXuqsl().toString());
		// map.put("chanx", mx.getChanx());
		map.put("mos", mos);
		List<Wullj> wlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.jsZ" + mos + "_kuay", map);
		// 将来模式为看板的
		map.remove("mos");
		map.put("jianglmos", mos);
		List<Wullj> future = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.jsZ" + mos + "_kuay", map);
		map.remove("jianglmos");
		// 过滤将来模式
		wlist.addAll(filterIfFutureMode(future, mos, usercenter));
		boolean flag = mos.equals(Const.KANB_JS_WAIBMOS_R2) || mos.equals(Const.KANB_JS_NEIBMOS_RM);
		for (int i = 0; i < wlist.size(); i++) {
			log.debug(i + "==================================================" + mos);
			Wullj wl = wlist.get(i);
			String key = wl.getUsercenter() + wl.getLingjbh() + wl.getChanx();
			String xqsl = list.get(key) == null ? "0" : list.get(key);
			if (flag) {
				wl.setXuqsl(wl.getXiaohbl().multiply(new BigDecimal(xqsl)));
			} else {
				wl.setXuqsl(new BigDecimal(xqsl));
			}
		}
		log.debug("arossMonthDateFilter end");
		return wlist;
	}

	/**
	 * 未跨月计算数据筛选
	 * 
	 * @param mos
	 *            看板模式
	 * @param map
	 *            参数map 需求所属周期、用户中心、需求版次
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Wullj> notAcrossMonth(String mos, Map<String, String> map, String usercenter) {
		List<Wullj> ls;
		map.put(Const.XUQSSZQ, date.get("xuqsszq"));
		ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.jsZ" + mos, map);
		// 将来模式为看板的
		map.remove("mos");
		map.put("jianglmos", mos);
		List futureMode = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.jsZ" + mos, map);
		map.remove("jianglmos");
		ls.addAll(filterIfFutureMode(futureMode, mos, usercenter));
		return ls;
	}

	/**
	 * 跨月汇总周期需求数量到产线 上一月 下一月
	 * 
	 * @param mx
	 *            毛需求
	 * @return mx
	 */
	public List<Map<String, Object>> sumChanx(List<Map<String, Object>> mx, String first) {
		// 跨周期 第一周期工作日
		Map<String, Integer> firstAll = this.getWorkDaysMap(date.get("monthStartofDate"), date.get("retEnd"));
		// 第一周期计算部分工作日
		Map<String, Integer> firstParts = this.getWorkDaysMap(date.get(Const.JSDATE), date.get("retEnd"));
		// 第二周期工作日
		Map<String, Integer> secondAll = this.getWorkDaysMap(date.get("retStart"), date.get("monthEndofDate"));
		// 第二周期计算部分工作日
		Map<String, Integer> secondParts = this.getWorkDaysMap(date.get("retStart"), date.get(Const.JSEND));
		// 异常报警参数
		List<String> parameter = new ArrayList<String>();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		// 筛选出的结果list
		List<Map<String, Object>> resultls = new ArrayList<Map<String, Object>>(1000);
		for (int i = 0; i < mx.size(); i++) {
			Map<String, Object> mxBean = mx.get(i);
			// 查询对应的产线的日历版次
			// 整月的工作日
			Integer monthofGZR = null;
			// 实际计算工作日
			Integer inBetweenofGZR = null;
			String rilbc = this.getRilbc((String) mxBean.get("USERCENTER"), (String) mxBean.get("CHANX"), (String) mxBean.get("LINGJBH"), parameter, ycls);
			if (!StringUtils.isEmpty(rilbc)) {
				if (first.equalsIgnoreCase("first")) {
					monthofGZR = firstAll.get(mxBean.get("USERCENTER") + ":" + rilbc);
					inBetweenofGZR = firstParts.get(mxBean.get("USERCENTER") + ":" + rilbc);
				} else {
					monthofGZR = secondAll.get(mxBean.get("USERCENTER") + ":" + rilbc);
					inBetweenofGZR = secondParts.get(mxBean.get("USERCENTER") + ":" + rilbc);
				}
				BigDecimal sl = BigDecimal.ZERO;
				if (monthofGZR != null && monthofGZR != 0) {
					// 需求数量
					BigDecimal xuqsl = (BigDecimal) mxBean.get("XUQSL");
					if (xuqsl == null) {
						xuqsl = BigDecimal.ZERO;
					}
					sl = xuqsl.divide(new BigDecimal(monthofGZR), 6, BigDecimal.ROUND_UP).multiply(inBetweenofGZR == null ? BigDecimal.ZERO : new BigDecimal(inBetweenofGZR));
					// 重新设置需求数量
				}
				mxBean.put("XUQSL", sl);
				resultls.add(mxBean);
			}
		}
		firstAll = null;
		firstParts = null;
		secondAll = null;
		secondParts = null;
		// 释放内存，插入异常报警
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		parameter = null;
		ycls = null;
		return mx;
	}

	/**
	 * 汇总两个周期的需求：合并两个list并以用户中心+产线+零件汇总需求数量 汇总集合中相同项
	 */
	public Map<String, String> mergeList(List<Map<String, Object>> mx1, List<Map<String, Object>> mx2) {
		mx1.addAll(mx2);
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < mx1.size(); i++) {
			Map<String, Object> maoxqmx = mx1.get(i);
			String key = (String) maoxqmx.get("USERCENTER") + maoxqmx.get("LINGJBH") + maoxqmx.get("CHANX");
			if (!map.isEmpty() && map.containsKey(key)) {
				// 需求数量
				BigDecimal xuqsl = (BigDecimal) maoxqmx.get("XUQSL");
				String xuqsl2 = map.get(key);
				BigDecimal sl = xuqsl.add(new BigDecimal(xuqsl2));
				map.put(key, String.valueOf(sl));
				mx1.remove(i);
				i--;
			} else {
				map.put(key, String.valueOf(maoxqmx.get("XUQSL")));
			}
		}
		return map;
	}

	/**
	 * 计算需求类型为周期，模式为R2的看板规模计算
	 */
	public List<Kanbxhgm> jsZR2(Maoxqmx bean) {
		String xuqbc = bean.getXuqbc();
		String mos = Const.KANB_JS_WAIBMOS_R2;
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("xuqbc", xuqbc);
		map.put("mos", mos);
		// 看板循环规模计算，存数据
		List<Kanbxhgm> kanb = new ArrayList<Kanbxhgm>(10000);
		List<Wullj> ls = new ArrayList<Wullj>(10000);
		if (null == date.get("retStart")) {
			// 未跨月
			ls = this.notAcrossMonth(mos, map, bean.getUsercenter());
		} else {
			// 跨月
			ls = this.arossMonthDateFilter(mos, this.kuaZqXq(map), bean.getUsercenter());

		}
		List<Wullj> lsMudd = this.sumAumontToCk(this.filterSuppliers(ls));
		// 计算CMJ/CMJMAX/循环规模
		// 异常报警参数
		List<String> parameter = new ArrayList<String>();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		for (int i = 0; i < lsMudd.size(); i++) {
			Wullj wullj = lsMudd.get(i);
			String rilbc = this.getRilbc(wullj.getUsercenter(), wullj.getMudd(), wullj.getLingjbh(), parameter, ycls);
			if(StringUtils.isEmpty(rilbc)){
				ls.remove(i);
				i--;continue;
			}
			Integer gzDays = allWorkDays.get(wullj.getUsercenter() + ":" + rilbc);
			wullj.setGongzr(gzDays);
			wullj.setRilbc(rilbc);
		}
		// 释放内存，插入异常报警
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		parameter = null;
		ycls = null;
		List<Wullj> yiCheckLs = this.r1R2JsBj(ls, Const.KANB_JS_WAIBMOS_R2);
		for (int i = 0; i < yiCheckLs.size(); i++) {
			kanb.add(this.computeR2(yiCheckLs.get(i)));
		}
		return kanb;
	}

	/**
	 * 计算需求类型为周期，模式为RM的看板规模计算
	 */
	public List<Kanbxhgm> jsZRM(Maoxqmx bean) {
		String xuqbc = bean.getXuqbc();
		String mos = Const.KANB_JS_NEIBMOS_RM;
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("xuqbc", xuqbc);
		map.put("mos", mos);
		// 看板循环规模计算，存数据
		List<Kanbxhgm> kanb = new ArrayList<Kanbxhgm>(10000);
		List<Wullj> ls = new ArrayList<Wullj>(10000);
		if (null == date.get("retStart")) {
			// 未跨月
			log.info("==========================RMSTRART NOT ACROSS MONTH START=============================");
			ls = notAcrossMonth(mos, map, bean.getUsercenter());
			log.info("==========================RMSTRART NOT ACROSS MONTH END=============================");
		} else {
			// 以用户中心+零件号+产线、模式过滤出用户中心+零件号+线边仓库
			// 跨月
			List<Wullj> all = this.arossMonthDateFilter(mos, this.kuaZqXq(map), bean.getUsercenter());
			ls = all;
		}
		List<Wullj> lsMudd = this.sumAumontToRmCk(ls);
		// 计算CMJ/CMJMAX/循环规模
		// 异常报警参数
		List<String> parameter = new ArrayList<String>();
		List<Yicbj> ycls = new ArrayList<Yicbj>(1000);
		for (int i = 0; i < lsMudd.size(); i++) {
			Wullj wullj = lsMudd.get(i);
			String rilbc = this.getRilbc(wullj.getUsercenter(), wullj.getXianbck(), wullj.getLingjbh(), parameter, ycls);
			if(StringUtils.isEmpty(rilbc)){
				ls.remove(i);
				i--;continue;
			}
			Integer gzDays = allWorkDays.get(wullj.getUsercenter() + ":" + rilbc);
			wullj.setGongzr(gzDays);
			wullj.setRilbc(rilbc);
		}
		// 释放内存，插入异常报警
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		parameter = null;
		ycls = null;
		List<Wullj> yiCheckLs = this.rMJsBj(lsMudd);
		for (int i = 0; i < yiCheckLs.size(); i++) {
			kanb.add(this.computeRM(yiCheckLs.get(i)));
		}
		return kanb;
	}

	/**
	 * <P>
	 * 跨月周期需求计算
	 * </P>
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, String> kuaZqXq(Map<String, String> map) {
		// 需求所属周期1
		map.put(Const.XUQSSZQ, date.get("xuqsszq"));
		// 对周期1的需求进行汇总到产线
		List<Map<String, Object>> mx1 = this.queryHzChanx(map);
		List<Map<String, Object>> mxs1 = sumChanx(mx1, "first");
		map.remove(Const.XUQSSZQ);
		// 需求所属周期2
		map.put(Const.XUQSSZQ, date.get("xuqsszq2"));
		// 对周期2的需求进行汇总到产线
		List<Map<String, Object>> mx2 = this.queryHzChanx(map);
		List<Map<String, Object>> mxs2 = sumChanx(mx2, "second");
		// 汇总两个周期的需求：合并两个list并以用户中心+产线+零件汇总需求数量
		return this.mergeList(mxs1, mxs2);

	}

	/**
	 * <p>
	 * 汇总R1的循环卡数
	 * </p>
	 * 
	 * @author Niesy
	 * 
	 * @date 2012-02-25
	 * 
	 * @return List<Wullj>
	 */
	public List<Kanbxhgm> sumKbXhd(List<Kanbxhgm> ls) {
		/**
		 * 以用户中心 零件编号 消耗点汇总循环卡数
		 */
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < ls.size(); i++) {
			Kanbxhgm kb = ls.get(i);
			String key = kb.getUsercenter() + kb.getLingjbh() + kb.getXiaohd();
			if (!map.isEmpty() && map.containsKey(key)) {
				BigDecimal jsxhgmI = ls.get(map.get(key)).getJisxhgm();
				BigDecimal jsxhgmJ = ls.get(i).getJisxhgm();
				BigDecimal jsxhgm = jsxhgmI.add(jsxhgmJ);
				ls.get(map.get(key)).setJisxhgm(jsxhgm);
				// CMJ
				BigDecimal cmjI = ls.get(map.get(key)).getCmj();
				BigDecimal cmjJ = ls.get(i).getCmj();
				BigDecimal cmj = cmjI.add(cmjJ);
				ls.get(map.get(key)).setCmj(cmj);
				// CMJMX
				BigDecimal cmjMaxI = ls.get(map.get(key)).getCjmax();
				BigDecimal cmjMaxJ = ls.get(i).getCjmax();
				BigDecimal cmjMax = cmjMaxI.add(cmjMaxJ);
				ls.get(map.get(key)).setCjmax(cmjMax);
				ls.remove(i);
				i--;
			} else {
				map.put(key, i);
			}
		}
		return ls;
	}

	/**
	 * <ul>
	 * <il> 汇总R2的循环卡数 </il> <il>
	 * 
	 * @author Niesy </il> <il>
	 * @date 2012-02-25 </il>
	 * @return List<Wullj>
	 *         </ul>
	 */
	public List<Kanbxhgm> sumKbCk(List<Kanbxhgm> ls) {
		/**
		 * 以用户中心 零件编号 线边仓库汇总循环卡数
		 */
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < ls.size(); i++) {
			Kanbxhgm kb = ls.get(i);
			String key = kb.getUsercenter() + kb.getLingjbh() + kb.getCangkdm();
			kb.setChanx(null);
			if (!map.isEmpty() && map.containsKey(key)) {
				BigDecimal jsxhgmI = ls.get(map.get(key)).getJisxhgm();
				BigDecimal jsxhgmJ = ls.get(i).getJisxhgm();
				BigDecimal jsxhgm = jsxhgmI.add(jsxhgmJ);
				ls.get(map.get(key)).setJisxhgm(jsxhgm);
				// CMJ
				BigDecimal cmjI = ls.get(map.get(key)).getCmj();
				BigDecimal cmjJ = ls.get(i).getCmj();
				BigDecimal cmj = cmjI.add(cmjJ);
				ls.get(map.get(key)).setCmj(cmj);
				// CMJMX
				BigDecimal cmjMaxI = ls.get(map.get(key)).getCjmax();
				BigDecimal cmjMaxJ = ls.get(i).getCjmax();
				BigDecimal cmjMax = cmjMaxI.add(cmjMaxJ);
				ls.get(map.get(key)).setCjmax(cmjMax);
				ls.remove(i);
				i--;
			} else {
				map.put(key, i);
			}
		}
		return ls;
	}

	/**
	 * <ul>
	 * <il> 汇总R2的需求数量到仓库 </il> <il>
	 * 
	 * @author Niesy </il> <il>
	 * @date 2012-02-25 </il>
	 * @return List<Wullj>
	 *         </ul>
	 */
	// TODO
	public List<Wullj> sumAumontToCk(List<Wullj> ls) {
		/**
		 * 汇总R2的需求数量到仓库
		 */
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < ls.size(); i++) {
			Wullj wl = ls.get(i);
			String key = wl.getUsercenter() + wl.getLingjbh() + wl.getGongysbh() + wl.getMudd();
			if (!map.isEmpty() && map.containsKey(key)) {
				BigDecimal slA = ls.get(map.get(key)).getXuqsl() == null ? BigDecimal.ZERO : ls.get(map.get(key)).getXuqsl();
				BigDecimal slB = ls.get(i).getXuqsl() == null ? BigDecimal.ZERO : ls.get(i).getXuqsl();
				BigDecimal sl = slA.add(slB);
				ls.get(map.get(key)).setXuqsl(sl);
				ls.remove(i);
				i--;
			} else {
				map.put(key, i);
			}
		}
		return ls;
	}

	/**
	 * <ul>
	 * <il> 汇总Rm的需求数量到仓库 </il> <il>
	 * 
	 * @author Niesy </il> <il>
	 * @date 2012-02-25 </il>
	 * @return List<Wullj>
	 *         </ul>
	 */
	public List<Wullj> sumAumontToRmCk(List<Wullj> ls) {
		/**
		 * 汇总RM的需求数量到仓库
		 */
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < ls.size(); i++) {
			Wullj wl = ls.get(i);
			wl.setChanx(null);
			String key = wl.getUsercenter() + wl.getLingjbh() + wl.getXianbck();
			if (!map.isEmpty() && map.containsKey(key)) {
				BigDecimal slA = ls.get(map.get(key)).getXuqsl() == null ? BigDecimal.ZERO : ls.get(map.get(key)).getXuqsl();
				BigDecimal slB = ls.get(i).getXuqsl() == null ? BigDecimal.ZERO : ls.get(i).getXuqsl();
				BigDecimal sl = slA.add(slB);
				ls.get(map.get(key)).setXuqsl(sl);
				ls.remove(i);
				i--;
			} else {
				map.put(key, i);
			}
		}
		return ls;
	}

	/**
	 * 点击确认后，更新计算参数处理设置
	 */
	@Transactional
	public int updateJssz(Map<String, String> map) {
		// 更新计算参数处理设置
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.ins_jisclcssz", map);
	}

	/**
	 * 点击确认后，更新计算参数处理设置
	 */
	@Transactional
	public int updateJsszAfterJs(Map<String, String> map) {
		// 更新计算参数处理设置
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.update_jszt", map);
	}

	/**
	 * 循环编码的生成
	 */
	public String xhbmGeneration(Kanbxhgm bean) {
		// 用户中心
		String usercenter = bean.getUsercenter();
		// 产线
		String chanx = bean.getChanx();
		// 产线
		String cangkbh = bean.getCangkdm();
		// 编码后五位最大流水号
		String bmlsh = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhl.xhbm");
		// 循环编码
		String xunhbm = "";
		if (null == bmlsh) {
			bmlsh = "00000";
		}
		if (chanx != null) {
			xunhbm = usercenter.substring(1, 2) + chanx.substring(2, 3) + chanx.substring(4, 5) + String.format("%05d", Integer.parseInt(bmlsh) + 1);
		} else {
			xunhbm = cangkbh + String.format("%05d", Integer.parseInt(bmlsh) + 1);
		}
		return xunhbm;
	}

	/**
	 * 循环编码的生成
	 */
	public String xhbmGenerationJs(Kanbxhgm bean, String lsh, int i) {
		// 用户中心
		String usercenter = bean.getUsercenter();
		// 产线
		String chanx = bean.getChanx();
		// 产线
		String cangkbh = bean.getCangkdm();
		// 循环编码
		String xunhbm = "";
		if (chanx != null) {
			xunhbm = usercenter.substring(1, 2) + chanx.substring(2, 3) + chanx.substring(4, 5) + String.format("%05d", Integer.parseInt(lsh) + i);
		} else {
			xunhbm = cangkbh + String.format("%05d", Integer.parseInt(lsh) + i);
		}
		return xunhbm;
	}

	/**
	 * 查询该用户中心所有零件消耗点发货标示信息
	 * 
	 * @param usercenter
	 *            用户中心
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getxhdFhbsMap(String usercenter) {
		// 将查询出来的LIST零件信息转成MAP
		List<Map<String, String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.xhdfhbz", usercenter);
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i).get("USERCENTER") + list.get(i).get("LINGJBH") + list.get(i).get("XIAOHDBH"), list.get(i).get("ZIDFHBZ"));
		}
		list = null;
		return map;
	}

	/**
	 * 查询该用户中心所有零件消耗点发货标示信息
	 * 
	 * @param usercenter
	 *            用户中心
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getckFhbsMap(String usercenter) {
		// 将查询出来的LIST零件信息转成MAP
		List<Map<String, String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.ckfhbz", usercenter);
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i).get("USERCENTER") + list.get(i).get("LINGJBH") + list.get(i).get("CANGKBH"), list.get(i).get("ZIDFHBZ"));
		}
		list = null;
		return map;
	}

	/**
	 * 查询该用户中心所有零件消耗点发货标示信息
	 * 
	 * @param usercenter
	 *            用户中心
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getallKbgmMap(String usercenter) {
		// 将查询出来的LIST零件信息转成MAP
		List<Map<String, String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.queryAllKbgm", usercenter);
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i).get("USERCENTER") + list.get(i).get("LINGJBH") + list.get(i).get("GONGHMS") + list.get(i).get("CANGKDM") + list.get(i).get("XIAOHD"), list.get(i).get("XUNHBM"));
		}
		return map;
	}

	/**
	 * 根据计算范围去更新看板循环计算表 范围：所有循环或新循环
	 */
	@Transactional
	public void updateKB(String xhfw, String creator, String createTime, int shifzdfs, List<Kanbxhgm> ls) {
		log.info("==========================updateKB STRART=============================");
		// 编码后五位最大流水号
		String bmlsh = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhl.xhbm");
		if (null == bmlsh) {
			bmlsh = "00000";
		}
		List<Kanbxhgm> isList = new ArrayList<Kanbxhgm>(10000);
		List<Kanbxhgm> upList = new ArrayList<Kanbxhgm>(10000);
		Map<String, String> xhdbsMap = null;
		Map<String, String> ckfhbsMap = null;
		Map<String, String> kbgmMap = null;
		if (ls.size() > 0) {
			xhdbsMap = getxhdFhbsMap(ls.get(0).getUsercenter());
			ckfhbsMap = getckFhbsMap(ls.get(0).getUsercenter());
			kbgmMap = this.getallKbgmMap(ls.get(0).getUsercenter());
		}
		for (int i = 0; i < ls.size(); i++) {
			// 如果循环规模小于2,将循环规模置为2
			String time = CommonFun.getJavaTime();
			Kanbxhgm bean = ls.get(i);
			if (bean.getJisxhgm().compareTo(BigDecimal.valueOf(2)) < 0) {
				bean.setJisxhgm(BigDecimal.valueOf(2));
			}
			bean.setWeihr(creator);
			bean.setWeihsj(time);
			bean.setEditor(creator);
			bean.setEdit_time(time);
			bean.setJissj(CommonFun.getJavaTime().substring(0, 10));
			// 是否自动下传
			bean.setShifzdfs(shifzdfs == 0 ? Integer.parseInt(shifzdsx(bean, xhdbsMap, ckfhbsMap)) : shifzdfs);
			// 生效状态
			bean.setShengxzt(Const.KANBXH_WEISX);
			// 冻结状态
			bean.setDongjjdzt("1");
			String key = bean.getUsercenter() + bean.getLingjbh() + bean.getGonghms() + bean.getCangkdm() + bean.getXiaohd();
			// int count =
			// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.update_kanb",
			// ls.get(i));
			if (StringUtils.isEmpty(kbgmMap.get(key))) {
				// count为0表示不存在这条记录就做插入操作
				bean.setDangqxhgm(BigDecimal.ZERO);
				String xunhbm = xhbmGenerationJs(bean, bmlsh, i + 1);
				// 新规模1调整规模2
				bean.setLeix("1");
				bean.setJianglms(Const.STRING_ONE.equals(bean.getJianglms()) ? Const.STRING_ONE : Const.STRING_ZERO);
				bean.setXunhbm(xunhbm);
				// 存在就更新，不存在就插入
				isList.add(bean);
			} else {
				bean.setXunhbm(kbgmMap.get(key));
				bean.setJianglms(Const.STRING_ONE.equals(bean.getJianglms()) ? Const.STRING_ONE : Const.STRING_ZERO);
				upList.add(bean);
			}
		}
		xhdbsMap = null;
		ckfhbsMap = null;
		ls = null;
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.ins_kanb", isList);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.update_kanb", upList);
		log.info("==========================updateKB END=============================");

	}

	/**
	 * 查询计算处理参数设置 return map (处理状态，参数)
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> selJscssz(Map<String, String> map) {
		return (Map<String, String>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhl.sel_jszt", map);
	}

	// 关键参数缺失校验，插入异常报警表
	public void checkToYcBj(String usercenter, String lingjbh, String jihydm, String cuowlx, String cuowxxxx, String jihyz) {
		Yicbj bean = new Yicbj();
		bean.setUsercenter(usercenter);
		bean.setLingjbh(lingjbh);
		// 看板循环规模
		bean.setJihyz(jihyz);
		// 计划员代码
		bean.setJihydm(jihydm);
		// 错误类型
		bean.setCuowlx(cuowlx);
		// 错误类型详细信息
		bean.setCuowxxxx(cuowxxxx);
		// 计算模块
		bean.setCreate_time(com.athena.xqjs.module.common.CommonFun.getJavaTime());
		bean.setEdit_time(com.athena.xqjs.module.common.CommonFun.getJavaTime());
		bean.setJismk(Const.JISMK_kANB_CD);
		yicbjService.insert(bean);
	}

	/**
	 * <p>
	 * 查询零件消耗点
	 * </p>
	 * 
	 * @author NIESY
	 * @param map
	 * @return
	 */
	public Lingjxhd queryXhd(String usercenter, String lingjbh, String xiaodbh) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		map.put("lingjbh", lingjbh);
		map.put("xiaohdbh", xiaodbh);
		return (Lingjxhd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryObject", map);
	}

	/**
	 * <p>
	 * 查询零件仓库
	 * </p>
	 * 
	 * @author NIESY
	 * @param map
	 * @return
	 */
	public Lingjck queryPartsWareHouse(String usercenter, String lingjbh, String cangkbh) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		map.put("lingjbh", lingjbh);
		map.put("xianbck", cangkbh);
		return (Lingjck) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhl.xianbck_rl", map);
	}

	/**
	 * <p>
	 * 是否自动下传
	 * </p>
	 * 
	 * @author NIESY
	 * @param bean
	 */
	public String shifzdsx(Kanbxhgm bean, Map<String, String> xhdbsMap, Map<String, String> ckfhbsMap) {
		String shifzdfs = "0";
		// 是否自动下传
		if (bean.getGonghms().equals(Const.KANB_JS_WAIBMOS_R1) || bean.getGonghms().equals(Const.KANB_JS_NEIBMOS_RD)) {
			// 消耗点的自动发货标志
			shifzdfs = "Y".equalsIgnoreCase(xhdbsMap.get(bean.getUsercenter() + bean.getLingjbh() + bean.getXiaohd())) ? Const.KANBXH_SHENGX : Const.KANBXH_WEISX;
		} else {
			// 仓库自动发货标志
			shifzdfs = Const.KANBXH_SHENGX.equalsIgnoreCase(ckfhbsMap.get(bean.getUsercenter() + bean.getLingjbh() + bean.getCangkdm())) ? Const.KANBXH_SHENGX : Const.KANBXH_WEISX;
		}
		return shifzdfs;
	}

	/**
	 * <p>
	 * 判断是否是将来模式
	 * </p>
	 * 
	 * @author NIESY
	 * @param bean
	 * @return
	 */
	public List<Wullj> filterIfFutureMode(List<Wullj> ls, String mos, String usercenter) {
		Map<String, Map<String, Object>> cangkxhsjmap = new HashMap<String, Map<String, Object>>();
		Map<String, Lingjck> ljckmap = new HashMap<String, Lingjck>();
		if (mos.equals(Const.KANB_JS_NEIBMOS_RM) || mos.equals(Const.KANB_JS_NEIBMOS_RD)) {
			cangkxhsjmap = assisterDateService.getCangkxhsj(usercenter, mos);
			ljckmap = getAllLjckMap(usercenter);
		}
		for (int i = 0; i < ls.size(); i++) {
			Wullj bean = ls.get(i);
			bean.setShifjlms(Const.STRING_ONE);
			// 将来模式RD段
			String jianglms = bean.getJianglms();
			// 将来模式2（RM）段
			String jianglms2 = bean.getJianglms2();
			// 外部将来模式段
			String wjianglms = bean.getWjianglms();
			// 设置日期格式
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			// 实力化Calendar类对象
			Calendar calendar = new GregorianCalendar();
			// 获取当前时间
			String nowDate = sf.format(calendar.getTime());
			// 往后推4天的日期
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 4);
			String afterFourDays = sf.format(calendar.getTime());
			// 对应的将来生效时间
			String jlsxsj = "";
			if (mos.equals(wjianglms)) {
				// 外部将来生效时间
				jlsxsj = bean.getWshengxsj();
			} else if (mos.equals(jianglms)) {
				// 将来生效时间
				jlsxsj = bean.getShengxsj();
			} else if (mos.equals(jianglms2)) {
				// 将来生效时间2
				jlsxsj = bean.getShengxsj2();
			}
			if (jlsxsj == null || jlsxsj.compareTo(nowDate) < 0 || jlsxsj.compareTo(afterFourDays) > 0) {
				// RD
				ls.remove(i);
				i--;
			} else if (mos.equals(Const.KANB_JS_NEIBMOS_RM)) {
				// 仓库+子仓库
				Lingjck ljck = ljckmap.get(ls.get(i).getUsercenter() + ls.get(i).getLingjbh() + ls.get(i).getDinghck());
				String dhzick = ljck == null ? "" : ljck.getZickbh();
				String cangkbh = "" + ls.get(i).getDinghck() + dhzick;
				String xbzick = ljckmap.get(ls.get(i).getUsercenter() + ls.get(i).getLingjbh() + ls.get(i).getXianbck()) == null ? "" : ljckmap.get(
						ls.get(i).getUsercenter() + ls.get(i).getLingjbh() + ls.get(i).getXianbck()).getZickbh();
				String xbckbh = "" + ls.get(i).getXianbck() + xbzick;
				String key = ls.get(i).getUsercenter() + cangkbh + xbckbh + mos;
				// 取得RM的仓库循环时间
				Map<String, Object> cksjMap = cangkxhsjmap.get(key) == null ? new HashMap<String, Object>() : cangkxhsjmap.get(key);
				ls.get(i).setCangkfhsj2(cksjMap.get("CANGKFHSJ") == null ? null : (BigDecimal) cksjMap.get("CANGKFHSJ"));
				ls.get(i).setCangkshsj2(cksjMap.get("CANGKSHSJ") == null ? null : (BigDecimal) cksjMap.get("CANGKSHSJ"));
				ls.get(i).setBeihsj2(cksjMap.get("BEIHSJ") == null ? null : (BigDecimal) cksjMap.get("BEIHSJ"));
				ls.get(i).setCangkshpc2(cksjMap.get("CANGKSHPCF") == null ? null : (BigDecimal) cksjMap.get("CANGKSHPCF"));
			} else if (mos.equals(Const.KANB_JS_NEIBMOS_RD)) {
				// 取得RD的仓库循环时间
				// 仓库+子仓库
				Lingjck ljck = ljckmap.get(ls.get(i).getUsercenter() + ls.get(i).getLingjbh() + ls.get(i).getXianbck());
				String zick = ljck == null ? "" : ljck.getZickbh();
				String cangkbh = "" + ls.get(i).getXianbck() + zick;
				String key = ls.get(i).getUsercenter() + cangkbh + ls.get(i).getFenpqh() + mos;
				// 取得RM的仓库循环时间
				Map<String, Object> cksjMap = cangkxhsjmap.get(key) == null ? new HashMap<String, Object>() : cangkxhsjmap.get(key);
				ls.get(i).setCangkfhsj(cksjMap.get("CANGKFHSJ") == null ? null : (BigDecimal) cksjMap.get("CANGKFHSJ"));
				ls.get(i).setCangkshsj(cksjMap.get("CANGKSHSJ") == null ? null : (BigDecimal) cksjMap.get("CANGKSHSJ"));
				ls.get(i).setIbeihsj(cksjMap.get("IBEIHSJ") == null ? null : (BigDecimal) cksjMap.get("IBEIHSJ"));
				ls.get(i).setPbeihsj(cksjMap.get("PBEIHSJ") == null ? null : (BigDecimal) cksjMap.get("PBEIHSJ"));
				ls.get(i).setCangkshpc(cksjMap.get("CANGKSHPCF") == null ? null : (BigDecimal) cksjMap.get("CANGKSHPCF"));
			}
		}

		return ls;
	}

	/**
	 * <p>
	 * 自动下放循环规模
	 * </p>
	 * 
	 * @author NIESY
	 * @date 2012-04-20
	 */
	public void updateXiafgm() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", Const.WTC_CENTER_UW);
		map.put("jiscldm", Const.KANB_MKDM);
		Map<String, String> hMap = this.selJscssz(map);
		if (CommonFun.getJavaTime("yyyy-MM-dd").equals(hMap.get("PARAM7")))
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateXiafgm");
	}

	// 周期计算初始化
	public void kuaZqInit() {
		cgmap = commonCalendarService.getCalendarGroupMap();
		date = assisterDateService.cycleJudge();
		// 跨周期计算从计算开始日期到计算结束日期所有版次工作日
		allWorkDays = this.getWorkDaysMap(date.get(Const.JSDATE), date.get(Const.JSEND));
	}

	// 日计算初始化
	public void dayComputerInit(Maoxqmx bean) {
		cgmap = commonCalendarService.getCalendarGroupMap();
		// 获得开始日期与结束日期
		timeMap = getIntervalTime(bean);
		allWorkDays = this.getWorkDaysMap(timeMap.get("STARTTIME"), timeMap.get("ENDTIME"));
	}

	// 销毁
	public void destroy() {
		cgmap = null;
		date = null;
		allWorkDays = null;

	}

	/**
	 * 看板计算
	 * 
	 * @param usercenter
	 * 
	 */
	public String numeration(String usercenter) throws ServiceException {
		String message = "";
		String num = usercenter;
		String us = usercenter;
		if(usercenter.length()==3){
			num = num.substring(2);
			usercenter = us.substring(0, 2);
		}
        
		try {
			Map<String, String> map = new HashMap<String, String>();
			// map.put("usercenter", Const.WTC_CENTER_UW);
			map.put("jiscldm", Const.KANB_MKDM);
			// 查询该用户中心下，看板计算模块的处理状态
			Map<String, String> hMap = this.selJscssz(map);
			// 处理状态
			if (hMap == null) {
				message = usercenter + MessageConst.JSCSSZ_KANB;
			} else if (hMap.get("CHULZT").equals(Const.JSZT_EXECUTING)) {
				message = MessageConst.COMPARE_KANB;
			} else {
				// 计算完成，修改计算参数设置表的处理状态
				map.put("chulzt", Const.JSZT_EXECUTING);
				this.updateJsszAfterJs(map);
				// PARAM1需求版次PARAM2需求来源PARAM3计算范围PARAM4创建人PARAM5创建时间PARAM6是否自动PARAM7生效时间
				Maoxqmx bean = new Maoxqmx();
				bean.setUsercenter(usercenter);
				log.info("看板计算-计算中========获取毛需求版次="+hMap.get("PARAM1")  );
				bean.setXuqbc(hMap.get("PARAM1"));
				bean.setXuqly(hMap.get("PARAM2"));
				map.put("xuqbc", hMap.get("PARAM1"));
				List<Map<String, String>> bjshix = new ArrayList<Map<String, String>>();
				// 消耗、仓库切换失效看板循环
				assisterDateService.disabledKbxh(usercenter,hMap.get("PARAM4"),bjshix);
				// 判断是周期还是日
				List<Kanbxhgm> kanbxhgms = new ArrayList<Kanbxhgm>(10000);
				try {
					// 数据准备
					this.prepareDate(bean, Const.KANB_JS_WAIBMOS_R1);
					// R1、RD消耗比例不为百分百报警
					this.kanbBJ(this.xiaohblBJ(), Const.KANB_XIAOHDBL_ERROR);
					// R1、R2供应商不为百分百报警
					this.kanbBJ(this.gonyfeBJ(), Const.KANB_LINGJGONGYS_ERROR);
					// 为新循环的数据筛选
					this.setXmosData(hMap.get("PARAM3").equals("new"), Const.KANB_JS_WAIBMOS_R1, usercenter);
					if (isCycle(hMap.get("PARAM2"))) {
						this.kuaZqInit();
						if (!StringUtils.isEmpty(date.get("message"))) {
							message = date.get("message");
							// 计算完成，修改计算参数设置表的处理状态
							map.put("chulzt", Const.JSZT_EXECPTION);
							this.updateJsszAfterJs(map);
							return message;
						}
						// 周期
						kanbxhgms.addAll(sumKbXhd(this.jsZR1(bean)));
						kanbxhgms.addAll(sumKbCk(this.jsZR2(bean)));
						// RM.RD数据准备
						this.prepareDate(bean, Const.KANB_JS_NEIBMOS_RD);
						// R1、RD消耗比例不为百分百报警
						this.kanbBJ(this.xiaohblBJ(), Const.KANB_XIAOHDBL_ERROR);
						// 为新循环的数据筛选
						this.setXmosData(hMap.get("PARAM3").equals("new"), Const.KANB_JS_NEIBMOS_RD, usercenter);
						kanbxhgms.addAll(this.jsZRM(bean));
						kanbxhgms.addAll(this.jsZRD(bean));
					} else {
						this.dayComputerInit(bean);
						// 日
						kanbxhgms.addAll(sumKbXhd(this.jsRR1(bean)));
						kanbxhgms.addAll(sumKbCk(this.jsRR2(bean)));
						// RM.RD数据准备
						this.prepareDate(bean, Const.KANB_JS_NEIBMOS_RD);
						// R1、RD消耗比例不为百分百报警
						this.kanbBJ(this.xiaohblBJ(), Const.KANB_XIAOHDBL_ERROR);
						// 为新循环的数据筛选
						this.setXmosData(hMap.get("PARAM3").equals("new"), Const.KANB_JS_NEIBMOS_RD, usercenter);
						kanbxhgms.addAll(this.jsRRM(bean));
						kanbxhgms.addAll(this.jsRRD(bean));
					}
					this.destroy();
				} catch (Exception e) {
					log.error("看板计算异常结束===============COMPUTER", e);
					// 计算异常结束
					message = "用户中心" + usercenter + "看板计算异常结束";
					// 计算完成，修改计算参数设置表的处理状态
					map.put("chulzt", Const.JSZT_EXECPTION);
					this.updateJsszAfterJs(map);
					return message;
				}
				this.updateKB(hMap.get("PARAM3"), hMap.get("PARAM4"), hMap.get("PARAM5"), Integer.parseInt(hMap.get("PARAM6")), kanbxhgms);
				
				// 模式切换
				if (num.equals(Const.STRING_TWO)) {
					assisterDateService.changeMode(usercenter,hMap.get("PARAM4"));
				}
				// 计算完成，修改计算参数设置表的处理状态
				map.put("chulzt", Const.JSZT_SURE);
				this.updateJsszAfterJs(map);
				
				// 消耗、仓库切换生效新看板循环，更新下发循环规模
				assisterDateService.enabledKbxh(usercenter,hMap.get("PARAM4"),bjshix);
				
				
				message = "计算成功";
			}
		} catch (Exception e) {
			log.error("看板计算异常结束=========UPDATE", e);
			// 更新异常结束
			message = "用户中心" + usercenter + "看板计算更新异常结束";
		}
		log.info("看板计算结束-message========" + message);
		return message;

	}


	public String numerationAllUsercenter() throws ServiceException {
		log.info("看板计算-开始========"  );
		//获取最新的日滚动的版次（clv,all）。   0007087
		String xucfsjMax = "and xuqly = 'CLV'";
		Map<String,String> mapBean = new HashMap<String, String>();
		mapBean.put("xuqly", xucfsjMax);
		try {//添加日志记录 2013-06-07  kong
			Maoxq maoxq =  (Maoxq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kanbyhl.maoxqByCLV", mapBean);
			mapBean.clear();
			mapBean.put("usercenter", Const.WTC_CENTER_UW);
			mapBean.put("jiscldm", Const.KANB_MKDM);
			mapBean.put("param1", maoxq.getXuqbc());
			mapBean.put("param2", "CLV");
			mapBean.put("param3", "all");
			mapBean.put("param4", Const.WTC_CENTER_UW);
			mapBean.put("param5", CommonFun.getJavaTime());	
			
			Map<String, String> map = new HashMap<String, String>();
			 map.put("usercenter", Const.WTC_CENTER_UW);
			map.put("jiscldm", Const.KANB_MKDM);
			// 查询该用户中心下，看板计算模块的处理状态
			Map<String, String> hMap = this.selJscssz(map);
			String param6 =  hMap ==null ?"0":StringUtils.defaultIfEmpty(hMap.get("param6"),"0");
			mapBean.put("param6", param6);	
			mapBean.put("param7", CommonFun.getJavaTime());	
			this.updateJssz(mapBean);
		} catch (DataAccessException e) {
			log.error("4060看板计算---》获取clv最新毛需求出错"+e.toString());
		}
	
		String message = "";
		message = this.numeration(Const.WTC_CENTER_UW);
		mapBean.put("param4", Const.WTC_CENTER_UL);
		this.updateJssz(mapBean);
		message = this.numeration(Const.WTC_CENTER_UL);
		mapBean.put("param4", Const.WTC_CENTER_UX);
		this.updateJssz(mapBean);
		message = this.numeration(Const.WTC_CENTER_UX);
		
		log.info("看板计算-结束========"+message );
		return message;
	}

	public static void main2(String[] args) {
		Connection con = null;
		Statement st = null;
		String sql = "update ck_yaohl t set t.yaohlzt ='05'";
		try {
			String ds = "jdbc:oracle:thin:@10.26.218.90:1521:athdb";
			String user = "ZBC_TEST";
			String login = "ZBC_TEST";
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			con = DriverManager.getConnection(ds, user, login);
			con.setAutoCommit(false);
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			System.out.println("开始时间" + System.currentTimeMillis());
			st = con.createStatement();
			/*
			 * for (int i = 0; i < 30; i++) { if (i == 0) {
			 */
			st.executeUpdate(sql);
			/*
			 * } else if (i == 1) { throw new SQLException("sssss"); } }
			 */
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				st.executeUpdate(sql);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		System.out.println("结束时间" + System.currentTimeMillis());
	}

	
	
	
	
	/**
	 * 将需求来源或版次号拼成（'1','2'）模式
	 * @param xuq
	 * @return
	 */
	public String getxuq(String xuq){
		if(xuq != null ){
			String xuqs = "(";
			String param[] = xuq.split(",");
			for (String val : param) {
				xuqs += "'"+val+"',";
			}
			xuqs = xuqs.substring(0,xuqs.length()-1);
			xuqs += ")"; 
			return xuqs;
		}
		return null;
		
		
	}
}
