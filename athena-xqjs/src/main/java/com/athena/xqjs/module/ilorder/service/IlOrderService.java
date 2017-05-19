package com.athena.xqjs.module.ilorder.service;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.anxorder.UsercenterSet;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.common.CalendarVersion;
import com.athena.xqjs.entity.common.Gongyzq;
import com.athena.xqjs.entity.common.Gongyzx;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Nianxb;
import com.athena.xqjs.entity.common.Tidj;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiaohblzjb;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Maoxqhzj;
import com.athena.xqjs.entity.ilorder.Maoxqhzjc;
import com.athena.xqjs.entity.ilorder.Maoxqhzjfpxh;
import com.athena.xqjs.entity.ilorder.Maoxqhzp;
import com.athena.xqjs.entity.ilorder.Maoxqhzpc;
import com.athena.xqjs.entity.ilorder.Maoxqhzpfpxh;
import com.athena.xqjs.entity.ilorder.Maoxqhzs;
import com.athena.xqjs.entity.ilorder.Maoxqhzsc;
import com.athena.xqjs.entity.ilorder.Maoxqhzsfpxh;
import com.athena.xqjs.entity.ilorder.Yugzjb;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.anxorder.service.AnxMaoxqService;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.CalendarCenterService;
import com.athena.xqjs.module.common.service.CalendarVersionService;
import com.athena.xqjs.module.common.service.JidygzqService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.common.service.WulljService;
import com.athena.xqjs.module.common.service.WzklszService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


@Component
public class IlOrderService extends BaseService {

	
	public  final Logger log = Logger.getLogger(IlOrderService.class);
	
	// 初始化零件flag
	private String flagLingj = "";

	// 初始化仓库flag
	private String flagCangku = "";

	// 初始化零件map
	private Map<String, BigDecimal> zongMap = new TreeMap<String, BigDecimal>();

	// 初始化仓库map
	private Map<String, BigDecimal> cangkMap = new TreeMap<String, BigDecimal>();

	// 定义供应商flag 根据不同供应商生成不同订单
	// private static String FLAGGONGYS = null;

	// 获取订单号，key为用户中心+供应商代码，value为订单号
	private Map<String, String> orderNumberMap = new HashMap<String, String>();

	public Map<String, String> getOrderNumberMap() {
		return orderNumberMap;
	}

	public void setOrderNumberMap(Map<String, String> orderNumberMap) {
		this.orderNumberMap = orderNumberMap;
	}

	@Inject
	private GongyzqService gongyzqService;// 注入工业周期service
	@Inject
	private CalendarCenterService calendarCenterService;// -用户中心日历service
	@Inject
	private GongyzxService gongyzxservice;// 周开始和结束时间service

	@Inject
	private GongyzqService gongyzqservice;// 周期开始和结束日期service

	@Inject
	private MaoxqhzpfpxhService maoxqhzpfpxhservice;// 周期模式注入毛需求汇总到分配循环service

	@Inject
	private MaoxqhzsfpxhService maoxqhzsfpxhservice;// 周模式注入毛需求汇总到分配循环service

	@Inject
	private MaoxqhzjfpxhService maoxqhzjfpxhservice;// 周期日供货模式注入毛需求汇总到分配循环service

	@Inject
	private MaoxqhzpService maoxqhzpservice;// 注入毛需求汇总Pservice

	@Inject
	private MaoxqhzpcService maoxqhzpcService;// 注入毛需求汇总PCservice

	@Inject
	private MaoxqhzjService maoxqhzjservice;// 注入毛需求汇总sJervice

	@Inject
	private MaoxqhzjcService maoxqhzjcService;// 注入毛需求汇总JCservice

	@Inject
	private MaoxqhzsService maoxqhzsservice;// 注入毛需求汇总Sservice

	@Inject
	private MaoxqhzscService maoxqhzscService;// 注入毛需求汇总SCservice

	@Inject
	private NianxbService nianxbService;// 注入年包service

	@Inject
	private MaoxqmxService maoxqmxService;// 毛需求明细service

	@Inject
	private DingdmxService dingdmxService;// 订单明细service

	@Inject
	private DingdljService dingdljservice;

	@Inject
	private DayOrderCountService dayordercountservice;

	@Inject
	private FeneService feneservice;

	@Inject
	private CalendarCenterService calendarcenterservice;

	@Inject
	private YugzjbService yugzjbservice;
	@Inject
	private CalendarVersionService calendarversionservice;

	@Inject
	private DingdService dingdservice;
	@Inject
	private WulljService wulljservice;
	@Inject
	private YicbjService yicbjservice;

	@Inject
	private WzklszService wzklszservice;
	@Inject
	private JidygzqService jdygzqservice;
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private LingjxhdService lingjxhdservice;
	@Inject
	private AnxMaoxqService  anxMaoxqService;
	/**
	 * 获取订单号
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-1
	 * @参数说明：String pattern，需求类型,String usercenter 用户中心,String gongysdm 供应商代码
	 */
	public String getOrderNumber(String pattern, String usercenter, String gongysdm,Map<String,String> orderNumberMap) throws ServiceException {// 根据不同的需求类型分别来获取订单号
		String mos = pattern.substring(0, 1);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中mos="+mos);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中usercenter="+usercenter);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中gongysdm="+gongysdm);
		// 判断在map中是否存在订单号，若存在则直接返回订单号，
		if (null != orderNumberMap.get(usercenter + gongysdm + mos)) {
			// 直接返回订单号，
			CommonFun.mapPrint(orderNumberMap, "获取订单号getOrderNumber方法中orderNumberMap");
			return orderNumberMap.get(usercenter + gongysdm + mos);
		}
		// 若不存在对应的订单号，则先生成部分订单号
		//流水号
		String liushStr = "";
		// 获取到年
		String year = CommonFun.getDate(Const.YEARY);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中year="+year);
		// 获取到月份
		String month0 = CommonFun.getDate(Const.MONTH);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中month0="+month0);
		// 获取到日
		String day = CommonFun.getDate(Const.DAY);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中day="+day);
		// 转换得到16进制的月份
		String month = Integer.toHexString(Integer.valueOf(month0)).toUpperCase();
		CommonFun.logger.debug("获取订单号getOrderNumber方法中month="+month);
		// 实例化年型表
		Nianxb nianx = new Nianxb();
		// 将截取到的年份set到年型的实体中
		nianx.setNianf(year);
		nianx.setUsercenter(usercenter);
		// 调用获取年型的方法，返回一个实体
		nianx = this.nianxbService.getNian(nianx);
		if (nianx == null) {
			String paramStr[] = new String[] { pattern, usercenter, gongysdm };
			yicbjservice.insertError(Const.YICHANG_LX2,
					Const.YICHANG_LX2_str66, "", paramStr,
					usercenter, "", new LoginUser(),
					Const.JISMK_IL_CD);
			throw new ServiceException("获取到年为空值,请检查年型码设置");
		}
		// 通过获取到的年型的实体，得到需要的年型
		year = nianx.getNianx();
		CommonFun.logger.debug("获取订单号getOrderNumber方法中year="+year);
		// 将各种形式放到map中
		Map<String, String> map = CommonFun.getPartten(year, month, day);
		// 得到部分订单号
		String orderNumber = map.get(pattern.toUpperCase());
		CommonFun.logger.debug("获取订单号getOrderNumber方法中orderNumber="+orderNumber);
		// 由部分订单号去订单明细总去匹配查询
		List<Dingd> list = this.dingdservice.queryOrderNumbers(orderNumber);
	//	CommonFun.objListPrint(list, "订单查找返回list");
		// 若查寻的结果集为空，则直接加上流水号0001，存入到map中
		if (list.isEmpty()) {
			// 生成最终的订单号
			orderNumber += Const.LSH_1;
			liushStr = Const.LSH_1;
			CommonFun.logger.debug("获取订单号getOrderNumber方法中订单号list为空时orderNumber="+orderNumber);
			// 若得到的结果不为空，则表示已经存在，则在流水号上加1
		} else {
			// 定义订单号的模板
			DecimalFormat data = new DecimalFormat(Const.LSH_0);
			//数据库中流水号值
			int dataLiush = Integer.valueOf(list.get(0).getDingdh()
					.substring(5, list.get(0).getDingdh().length())) + 1;
			//MAP缓存中流水号值
			int mapLiush = Integer.valueOf(StringUtils.defaultIfEmpty(orderNumberMap.get("liush"), "0")) + 1;
			if(mapLiush >= dataLiush){
				liushStr = data.format(mapLiush);
			}else{
				liushStr = data.format(dataLiush);
			}
			// 若得到的结果不为空，则表示已经存在，则在流水号上加1
			orderNumber = orderNumber + liushStr;
			CommonFun.logger.debug("获取订单号getOrderNumber方法中订单号list不为空时orderNumber="+orderNumber);
		}
		//保存流水号字符串
		orderNumberMap.put("liush",liushStr);
		// 将最终生成的订单号存放到map中
		orderNumberMap.put(usercenter + gongysdm + mos, orderNumber);
		return orderNumber.trim();// 返回订单号
	}

	/**
	 * 获取订单号
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-1
	 * @参数说明：String pattern，需求类型,String usercenter 用户中心,String gongysdm 供应商代码
	 */
	public String getOrderNumber(String pattern, String usercenter, String gongysdm,Map<String,String> orderNumberMap,UsercenterSet uset) throws ServiceException {// 根据不同的需求类型分别来获取订单号
		String mos = pattern.substring(0, 1);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中mos="+mos);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中usercenter="+usercenter);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中gongysdm="+gongysdm);
		// 判断在map中是否存在订单号，若存在则直接返回订单号，
		if (null != orderNumberMap.get(usercenter + gongysdm + mos)) {
			// 直接返回订单号，
			CommonFun.mapPrint(orderNumberMap, "获取订单号getOrderNumber方法中orderNumberMap");
			return orderNumberMap.get(usercenter + gongysdm + mos);
		}
		// 若不存在对应的订单号，则先生成部分订单号
		//流水号
		String liushStr = "";
		// 获取到年
		String year = CommonFun.getDate(Const.YEARY);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中year="+year);
		// 获取到月份
		String month0 = CommonFun.getDate(Const.MONTH);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中month0="+month0);
		// 获取到日
		String day = CommonFun.getDate(Const.DAY);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中day="+day);
		// 转换得到16进制的月份
		String month = Integer.toHexString(Integer.valueOf(month0)).toUpperCase();
		CommonFun.logger.debug("获取订单号getOrderNumber方法中month="+month);
		// 实例化年型表
		Nianxb nianx = new Nianxb();
		// 将截取到的年份set到年型的实体中
		nianx.setNianf(year);
		nianx.setUsercenter(usercenter);
		// 调用获取年型的方法，返回一个实体
		nianx = this.nianxbService.getNian(nianx);
		if (nianx == null) {
			String paramStr[] = new String[] { pattern, usercenter, gongysdm };
			yicbjservice.insertError(Const.YICHANG_LX2,
					Const.YICHANG_LX2_str66, "", paramStr,
					usercenter, "", new LoginUser(),
					Const.JISMK_IL_CD);
			throw new ServiceException("获取到年为空值,请检查年型码设置");
		}
		// 通过获取到的年型的实体，得到需要的年型
		year = nianx.getNianx();
		CommonFun.logger.debug("获取订单号getOrderNumber方法中year="+year);
		// 将各种形式放到map中
		Map<String, String> map = CommonFun.getPartten(year, month, day,uset);
		// 得到部分订单号
		String orderNumber = map.get(pattern.toUpperCase());
		CommonFun.logger.debug("获取订单号getOrderNumber方法中orderNumber="+orderNumber);
		// 由部分订单号去订单明细总去匹配查询
		List<Dingd> list = this.dingdservice.queryOrderNumbers(orderNumber);
	//	CommonFun.objListPrint(list, "订单查找返回list");
		// 若查寻的结果集为空，则直接加上流水号0001，存入到map中
		if (list.isEmpty()) {
			// 生成最终的订单号
			orderNumber += Const.LSH_1;
			liushStr = Const.LSH_1;
			CommonFun.logger.debug("获取订单号getOrderNumber方法中订单号list为空时orderNumber="+orderNumber);
			// 若得到的结果不为空，则表示已经存在，则在流水号上加1
		} else {
			// 定义订单号的模板
			DecimalFormat data = new DecimalFormat(Const.LSH_0);
			//数据库中流水号值
			int dataLiush = Integer.valueOf(list.get(0).getDingdh()
					.substring(5, list.get(0).getDingdh().length())) + 1;
			//MAP缓存中流水号值
			int mapLiush = Integer.valueOf(StringUtils.defaultIfEmpty(orderNumberMap.get("liush"), "0")) + 1;
			if(mapLiush >= dataLiush){
				liushStr = data.format(mapLiush);
			}else{
				liushStr = data.format(dataLiush);
			}
			// 若得到的结果不为空，则表示已经存在，则在流水号上加1
			orderNumber = orderNumber + liushStr;
			CommonFun.logger.debug("获取订单号getOrderNumber方法中订单号list不为空时orderNumber="+orderNumber);
		}
		//保存流水号字符串
		orderNumberMap.put("liush",liushStr);
		// 将最终生成的订单号存放到map中
		orderNumberMap.put(usercenter + gongysdm + mos, orderNumber);
		return orderNumber.trim();// 返回订单号
	}

	/**
	 * 清除中间表的数据
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-1 参数说明：String pattern，需求类型
	 */
	public void clearData(String pattern) {
		CommonFun.logger.info("Il订单计算模式为"+pattern+"的中间表清理开始");
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "清除中间表的数据开始");
		// 清除预告中间表
		this.yugzjbservice.doDeleteAll();
		if (pattern.equalsIgnoreCase(Const.PP)) {// 周期类型的需求
			this.maoxqhzpservice.doAllDelete();// 调用毛需求汇总PP的删除方法
			this.maoxqhzpcService.doAllDelete();// 调用毛需求汇总关联参考系PP的删除方法
		} else if (pattern.equalsIgnoreCase(Const.PS)) {// 周类型的需求
			this.maoxqhzsservice.doAllDelete();// 调用毛需求汇总PS的删除方法
			this.maoxqhzscService.doAllDelete();// 调用毛需求汇总关联参考系PS的删除方法
		} else if (pattern.equalsIgnoreCase(Const.PJ)) {// 日类型的需求
			this.maoxqhzjservice.doAllDelete();// 调用毛需求汇总PJ的删除方法
			this.maoxqhzjcService.doAllDelete();// 调用毛需求汇总关联参考系PJ的删除方法
		}
		CommonFun.logger.info("Il订单计算模式为"+pattern+"的中间表清理结束");
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "清除中间表的数据结束");
	}

	/**
	 * @see 删除计算中的订单
	 * @param user
	 * @param dingdlx
	 * @param active
	 */
	private void deleteDingd(String user, String dingdlx,String active) {
		Map<String,String> param = new HashMap<String, String>();
		param.put("creator", user);
		param.put("dingdlx", dingdlx);
		if(StringUtils.isNotBlank(active))
		{
			param.put("active", active);
		}
		//清除订单内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteDingdLjJsz",param);
		//清除订单明细内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteDingdMxJsz",param);
		//清除订单内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteDingdJsz",param);
		
		//删除没有实际存在订单号的订单零件
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteDingdLjByNoDingdh");
		
		//删除没有实际存在订单号的订单明细
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteDingdMxByNoDingdh");
		
	}

	/**
	 * Title:国产件订单计算 Description:按照选取的毛需求计算既定和预告 参数为订单类型,资源获取日期,用户中心组 计算是否顺利完成
	 * 
	 * @author 陈骏
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws ClassNotFoundException 
	 * @throws ParseException 
	 * @date 2011-12-1
	 */

	public String doCalculate(String zhizlx, String dingdlx, String ziyxqrq, String jiihyz, String[] banc,
			List<Dingd> dingdList,String userString,String [] usercenter,LoginUser loginUser)  {
		CommonFun.logger.debug("国产件订单类型为"+dingdlx+"的计算操开始");
		String message = "";
		String jssj = CommonFun.getJavaTime();
		Integer count = 0;
		 //用于存储maoxqhzpc表的数据
		List ls = new ArrayList();
		 //用于存储dingdlj表的数据
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "取得用户中心开始");
		String caozz = loginUser.getUsername();
		CommonFun.logger.debug("国产件订单计算操作者"+caozz);
		String banch = ""; 
		CommonFun.logger.debug("国产件订单计算用户中心"+usercenter.toString());
		Map<String, String> dingdhMap = new HashMap<String, String>();
		CommonFun.logger.debug("国产件订单计算订货路线为"+zhizlx);
		if (zhizlx.equals(Const.ZHIZAOLUXIAN_GL)) 
		{
			for (Dingd dingd : dingdList) 
			{
				Map<String, String> ddMap = new HashMap<String, String>();
				ddMap.put("dingdh", dingd.getDingdh());
				Dingd dd = this.dingdservice.queryDingdByDingdh(ddMap);
				Map<String,String> zhouqMap = new HashMap<String,String>();
				zhouqMap.put("usercenter", dd.getUsercenter());
				zhouqMap.put("riq", ziyxqrq);
				CalendarCenter calendarcenter = this.calendarcenterservice.queryCalendarCenterObject(zhouqMap);
				if(null != calendarcenter && calendarcenter.getNianzq().equals(dd.getJiszq()))
				{
					dingdhMap.put(dd.getGongysdm()+dd.getUsercenter(), dd.getDingdh());
				}
				else
				{
					return message = "编号为："+dd.getDingdh()+"的订单的计算周期与资源获取日期不在同一周期内";
				}
			}
			CommonFun.mapPrint(dingdhMap,"dingdhMap");
			// 清空计算中的订单信息
			this.deleteDingd(null, "'" + Const.DINGDLX_JL+ "'","");
		}
		else
		{
			// 清空计算中的订单信息
			this.deleteDingd(null,"'" + Const.DINGDLX_ILORDER + "'","");
		}
		Map<String,BigDecimal> resMap = new HashMap<String,BigDecimal>();
		BigDecimal yingyu = BigDecimal.ZERO;
		boolean flag = false;// 用于判断是否有多个既定
	
		this.clearData(dingdlx);// 清理中间表
		this.conversionColRow(banc, dingdlx, ziyxqrq, usercenter, zhizlx, dingdList);// 取出需要计算的毛需求并进行行列转换
		this.checkXiaohbl(dingdlx);
		long startx = System.currentTimeMillis();
		message = this.maoxqhzglckx(ziyxqrq, dingdlx, jiihyz, zhizlx, dingdList,loginUser);// 将毛需求汇总到仓库，并关联参考系参数然后将数据插入到毛需求汇总_参考系表
		if (null!=message) {
			return message;
		}
		long endx = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,单次计算时间"+ (endx - startx)/1000);
		CommonFun.logger.debug("国产件订单类型为"+dingdlx+"的参数校验");
		//error = this.maoxqhzpcService.checkAll(dingdlx);// 校验物流路径，供应商份额以及参考系参数
		count = this.check(loginUser,dingdlx);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "数据准备结束");
		if(null != banc && banc.length > 0)
		{
			for (int x = 0; x < banc.length; x++) {
				if (null != banc[x]) {
					banch +=  banc[x];
				}
				if((x + 1) != banc.length)
				{
					banch += ",";
				}
			}
		}
/////////////////////wuyichao  优化订单明细  订单零件为批量插入//////////////////
		List<Dingd> dingdSaveList = new ArrayList<Dingd>();
		Map<String, List<Dingdlj>> saveLingjMap = new HashMap<String, List<Dingdlj>>();
		Map<String, List<Dingdmx>> saveMingxMap = new HashMap<String, List<Dingdmx>>();
/////////////////////wuyichao  优化订单明细  订单零件为批量插入//////////////////
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算", "计算开始");
		if (dingdlx.equalsIgnoreCase(Const.PP)) {
			long start = System.currentTimeMillis();
			
			ls = this.maoxqhzpcService.selectDingd(Const.PP);// 得到maoxqhzpc表数据
			if (!ls.isEmpty()) {
//				message = "对应的毛需求汇总_参考系表表数据为空"; // 如果得到的数据为空则输出的信息。
//				return message;
//			} else {
				// 如果得到的数据不为空
				//CommonFun.objListPrint(ls, "国产件PP模式订单pc表查询结果");
				for (int i = 0; i < ls.size(); i++) {
					long starti = System.currentTimeMillis();
					BigDecimal ddljzl = BigDecimal.ZERO;
					CommonFun.logger.info("第"+i+"次循环");
					// 初始化订单零件
					Dingdlj dingdlj = new Dingdlj();
					int jidgs = 0;// 用于判断既定个数
					Maoxqhzpc maoxqhzpc = (Maoxqhzpc) ls.get(i);// 取得毛需求汇总_参考系中一条数据
					String dingdnr = maoxqhzpc.getDingdnr();
					CommonFun.logger.debug("国产件PP模式订单订单内容为:"+dingdnr);
					for (int j = 0; j < dingdnr.length(); j++) {
						String neir = maoxqhzpc.getDingdnr().substring(j, j + 1);
						if (neir.equalsIgnoreCase(Const.SHIFOUSHIJIDING)) {
							jidgs = jidgs + 1;
						}
					}
					CommonFun.logger.debug("国产件PP模式订单既定个数为:"+jidgs);
					int pianysj = (maoxqhzpc.getBeihzq().add(maoxqhzpc.getFayzq())).divide(new BigDecimal(30), 0,
							BigDecimal.ROUND_UP).intValue();// 运输周期+备货周期向上取整
					CommonFun.logger.debug("国产件PP模式订单偏移时间为:"+pianysj);
					if(pianysj<=Const.ZPCHANGDU){
					for (int x = 0; x < jidgs; x++) {
						int zuizpysj =  pianysj + x;
						CommonFun.logger.debug("国产件PP模式订单既定计算循环中第:"+x+"次循环的zuizpysj="+zuizpysj);
						String method = Const.GETP + zuizpysj;// 拼接getPi方法
						CommonFun.logger.debug("国产件PP模式订单既定计算循环中第:"+x+"次循环的method="+method);
						BigDecimal jiding = this.backValue(method, maoxqhzpc, zuizpysj,dingdlx);
						CommonFun.logger.debug("国产件PP模式订单既定计算循环中第:"+x+"次循环的jiding="+jiding);
						resMap = this.jiDingCalculate(jiding, maoxqhzpc, flag, yingyu, x, zuizpysj, dingdlj, zhizlx,
								dingdhMap, i,caozz,ddljzl);// 调用既定数量计算方法
						yingyu = resMap.get("yingyu");
						CommonFun.logger.debug("国产件PP模式订单既定计算循环中第:"+x+"次循环的yingyu="+yingyu);
						ddljzl = resMap.get("ddljzl");
						if (x == jidgs - 1) {
							flag = false;
							CommonFun.logger.debug("国产件PP模式订单既定计算循环中第:"+x+"次循环的flag="+flag);
						}
					}
					for (int y = 0; y < Const.DINGDANNEIRONGCHANGDU - jidgs; y++) {
						String method = Const.GETP + (pianysj + jidgs + y);// 拼接getPi方法
						BigDecimal yugao = BigDecimal.ZERO;
						if(pianysj + jidgs + y>Const.ZPCHANGDU){
							yugao = BigDecimal.ZERO;
							yingyu = BigDecimal.ZERO;
						}else{
							CommonFun.logger.debug("国产件PP模式订单预告计算循环中第:"+y+"次循环的method="+method);
							yugao = this.backValue(method, maoxqhzpc, pianysj + jidgs + y,dingdlx);
						}
						CommonFun.logger.debug("国产件PP模式订单预告计算循环中第:"+y+"次循环的yugao="+yugao);
						if (maoxqhzpc.getYugsfqz().equalsIgnoreCase(Const.BUYiLAI)) {
							yingyu = BigDecimal.ZERO;
							CommonFun.logger.debug("国产件PP模式订单预告计算循环中第:"+y+"次循环的预告不取整的yingyu="+yingyu);
						} 
						yingyu = this.yuGaoCalculate(yugao, maoxqhzpc, yingyu, jidgs + y, dingdlj);// 调用预告数量计算方法
						CommonFun.logger.debug("国产件PP模式订单预告计算循环中第:"+y+"次循环的预告取整的yingyu="+yingyu);
						String methodPd = Const.GETP + (jidgs + y) + "sl";
						BigDecimal jissl = BigDecimal.ZERO;
						if(null != dingdlj.getDingdh()){
						jissl = this.backValue(methodPd, dingdlj, 0,dingdlx);
						CommonFun.logger.debug("国产件PP模式订单预告计算循环中第:"+y+"次循环的预告取整的jissl="+jissl);
						}
						if(pianysj + jidgs + y<=Const.ZPCHANGDU){
						this.insertDingdMX(dingdlj, maoxqhzpc, zhizlx, jidgs + y, dingdlx, dingdhMap, jissl, null,caozz,Const.SHIFOUSHIYUGAO,saveMingxMap);
						}
					}
//////////////////////////////wuyichao  
					dayordercountservice.countDayOrder(dingdlj, Const.PP,maoxqhzpc.getLingjmc(),maoxqhzpc.getGongsmc(),maoxqhzpc.getNeibyhzx(),caozz,jiihyz,loginUser,maoxqhzpc.getGcbh(),saveMingxMap );
//////////////////////////////wuyichao  
					dingdlj.setDingdljddlj(ddljzl);
					dingdlj.setJidyhsl(ddljzl);
//////////////////////////////wuyichao  
					//dingdljservice.doInsert(dingdlj);
					List<Dingdlj> flagLingj = saveLingjMap.get(dingdlj.getDingdh());
					if(null == flagLingj)
					{
						flagLingj = new ArrayList<Dingdlj>();
					}
					flagLingj.add(dingdlj);
					saveLingjMap.put(dingdlj.getDingdh(), flagLingj);
//////////////////////////////wuyichao  	
					String dingduser = "";
					for (int z = 0; z < usercenter.length; z++) {
						if (null != usercenter[z]) {
							dingduser = dingduser + usercenter[z] + ",";
						}
					}
					CommonFun.logger.debug("国产件PP模式订单计算订单操作参数dingdlx="+dingdlx);
					CommonFun.logger.debug("国产件PP模式订单计算订单操作参数jssj="+jssj);
					CommonFun.logger.debug("国产件PP模式订单计算订单操作参数banch="+banch);
					CommonFun.logger.debug("国产件PP模式订单计算订单操作参数jiihyz="+jiihyz);
					CommonFun.logger.debug("国产件PP模式订单计算订单操作参数caozz="+caozz);
					this.insertdingd(dingdlj, maoxqhzpc, dingdlx, jssj, banch, jiihyz,caozz);
					long endi = System.currentTimeMillis();
					CommonFun.logger.info("----------------------------IL计算,单次计算时间"+ (endi - starti));
					CommonFun.logger.info("----------------------------IL计算,单次计算时间"+ (endi - starti));
					CommonFun.logger.debug("国产件订单类型为"+endi+"的参数校验");
					}else{
						String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getGongysdm(),maoxqhzpc.getLingjbh()};
						this.yicbjservice.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str9, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginUser, Const.JISMK_IL_CD);
					}
				}
			}
			if (zhizlx.equals(Const.ZHIZAOLUXIAN_IL)) {
				CommonFun.logger.debug("国产件非PP模式订单计算参数dingdlx="+dingdlx);
				CommonFun.logger.debug("国产件非PP模式订单计算参数jssj="+jssj);
				CommonFun.logger.debug("国产件非PP模式订单计算参数yingyu="+yingyu);
				CommonFun.logger.debug("国产件非PP模式订单计算参数jiihyz="+jiihyz);
				CommonFun.logger.debug("国产件非PP模式订单计算参数caozz="+caozz);
				CommonFun.logger.debug("国产件非PP模式订单计算参数banch="+banch);
				yingyu = this.ppyuGaoCalculate(dingdlx,jssj,jiihyz,banch,loginUser,saveLingjMap,saveMingxMap);
				CommonFun.logger.debug("国产件非PP模式订单计算返回yingyu="+yingyu);
			}
			long end = System.currentTimeMillis();
			CommonFun.logger.info("----------------------------IL计算,计算时间"+ (end - start)/1000);
			
		}
		else if (dingdlx.equalsIgnoreCase(Const.PS)) {
			ls = this.maoxqhzscService.selectDingd(dingdlx);// 得到maoxqhzsc表数据
			if (!ls.isEmpty()) {
//				message = "对应的毛需求汇总_参考系表表数据为空"; // 如果得到的数据为空则输出的信息。
//				return message;
//			} else {
				// 如果得到的数据不为空
				//CommonFun.objListPrint(ls, "国产件PS模式订单sc表查询结果");
				for (int i = 0; i < ls.size(); i++) {
					BigDecimal ddljzl = BigDecimal.ZERO;
					// 初始化订单零件
					int jidgs = 0;// 用于判断既定个数
					Dingdlj dingdlj = new Dingdlj();
					Maoxqhzsc maoxqhzsc = (Maoxqhzsc) ls.get(i);// 取得毛需求汇总_参考系中一条数据
					String dingdnr = maoxqhzsc.getDingdnr();
					CommonFun.logger.debug("国产件PS模式订单计算dingdnr="+dingdnr);
					for (int j = 0; j < dingdnr.length(); j++) {
						String neir = maoxqhzsc.getDingdnr().substring(j, j + 1);
						if (neir.equalsIgnoreCase("9")) {
							jidgs = jidgs + 1;
						}
					}
					CommonFun.logger.debug("国产件PS模式订单计算jidgs="+jidgs);
					int pianysj = (maoxqhzsc.getBeihzq().add(maoxqhzsc.getFayzq())).divide(new BigDecimal("7"), 0,
							BigDecimal.ROUND_UP).intValue();// 运输周期+备货周期向上取整
					CommonFun.logger.debug("国产件PS模式订单计算pianysj="+pianysj);
					if(pianysj<=Const.ZSCHANGDU){
					for (int x = 0; x < jidgs; x++) {
						String method = Const.GETS + (pianysj + x);// 拼接getPi方法
						CommonFun.logger.debug("国产件PS模式订单计算既定计算method="+method);
						BigDecimal jiding = this.backValue(method, maoxqhzsc, pianysj + x,dingdlx);
						CommonFun.logger.debug("国产件PS模式订单计算既定计算jiding="+jiding);
						resMap = this.jiDingCalculate(jiding, maoxqhzsc, flag, yingyu, x, pianysj, dingdlj, zhizlx, i,caozz,ddljzl);// 调用既定数量计算方法
						yingyu = resMap.get("yingyu");
						ddljzl = resMap.get("ddljzl");
						CommonFun.logger.debug("国产件PS模式订单计算既定计算yingyu="+yingyu);
						String methodSd = Const.GETP + x + "sl";
						CommonFun.logger.debug("国产件PS模式订单计算既定计算methodSd="+methodSd);
						BigDecimal jissl = this.backValue(methodSd, dingdlj, 0,dingdlx);	
						CommonFun.logger.debug("国产件PS模式订单计算既定计算jissl="+jissl);
						//0004264
						this.insertDingdMX(dingdlj, maoxqhzsc, zhizlx, x, dingdlx, dingdhMap, jissl, null,caozz,Const.SHIFOUSHIJIDING , saveMingxMap);
					}
					for (int y = 0; y < Const.DINGDANNEIRONGCHANGDU - jidgs; y++) {
						String method = Const.GETS + (pianysj + jidgs + y);// 拼接getPi方法
						CommonFun.logger.debug("国产件PS模式订单计算预告计算method="+method);
						BigDecimal yugao = BigDecimal.ZERO;
						if(pianysj + jidgs + y>Const.ZSCHANGDU){
							yugao = BigDecimal.ZERO;
							yingyu = BigDecimal.ZERO;
						}else{
						 yugao = this.backValue(method, maoxqhzsc, pianysj + jidgs + y, dingdlx);
						 CommonFun.logger.debug("国产件PS模式订单计算预告计算yugao="+yugao);
						}
						if (maoxqhzsc.getYugsfqz().equalsIgnoreCase(Const.BUYiLAI)) {
							yingyu = BigDecimal.ZERO;
							CommonFun.logger.debug("国产件PS模式订单计算预告计算在（maoxqhzsc.getYugsfqz().equalsIgnoreCase(Const.BUYiLAI)）时yingyu="+yingyu);
						}
							yingyu = this.yuGaoCalculate(yugao, maoxqhzsc, yingyu, jidgs + y, dingdlj);// 调用预告数量计算方法
							CommonFun.logger.debug("国产件PS模式订单计算预告计算yingyu="+yingyu);
						String methodSd = Const.GETP + (jidgs + y) + "sl";
						CommonFun.logger.debug("国产件PS模式订单计算预告计算methodSd="+methodSd);
						BigDecimal jissl = this.backValue(methodSd, dingdlj,0, dingdlx);
						CommonFun.logger.debug("国产件PS模式订单计算预告计算jissl="+jissl);
						if(pianysj + jidgs + y<=Const.ZSCHANGDU){
						this.insertDingdMX(dingdlj, maoxqhzsc, zhizlx, jidgs + y, dingdlx, dingdhMap, jissl, null,caozz,Const.SHIFOUSHIYUGAO , saveMingxMap);
						}
					}
					CommonFun.objPrint(dingdlj, "国产件PS模式订单计算dingdlj");
//////////////////////////////wuyichao  
					//dingdljservice.doInsert(dingdlj);
					List<Dingdlj> flagLingj = saveLingjMap.get(dingdlj.getDingdh());
					if(null == flagLingj)
					{
						flagLingj = new ArrayList<Dingdlj>();
					}
					flagLingj.add(dingdlj);
					saveLingjMap.put(dingdlj.getDingdh(), flagLingj);
//////////////////////////////wuyichao  	
					for (int x = 0; x < banc.length; x++) {
						String bc = "";
						if (null != banc[x]) {
							bc = bc + banc[x] + ",";
						}
						banch = bc;
					}
					String dingduser = "";
					for (int z = 0; z < usercenter.length; z++) {
						if (null != usercenter[z]) {
							dingduser = dingduser + usercenter[z] + ",";
						}
					}
					dingdlj.setDingdljddlj(ddljzl);
					dingdlj.setJidyhsl(ddljzl);
					this.insertdingd(dingdlj, maoxqhzsc, dingdlx, jssj, banch, jiihyz,caozz);
					}else{
						String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getGongysdm(),maoxqhzsc.getLingjbh()};
						this.yicbjservice.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str9, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginUser, Const.JISMK_IL_CD);
					}
				}
			}
			yingyu = this.ppyuGaoCalculate(dingdlx,jssj,jiihyz,banch,loginUser,saveLingjMap,saveMingxMap);
			banch = "";
		} else if (dingdlx.equalsIgnoreCase(Const.PJ)) {
			
			ls = this.maoxqhzjcService.selectDingd(dingdlx);// 得到maoxqhzjc表数据
			if (!ls.isEmpty()) {
//				message = "对应的毛需求汇总_参考系表表数据为空"; // 如果得到的数据为空则输出的信息。
//				return message;
//			} else {
				//CommonFun.objListPrint(ls, "国产件PJ模式订单jc表查询结果");
				// 如果得到的数据不为空
				for (int i = 0; i < ls.size(); i++) {
					BigDecimal ddljzl = BigDecimal.ZERO;
					int jidgs = 0;// 用于判断既定个数
					// 初始化订单零件
					Dingdlj dingdlj = new Dingdlj();
					Maoxqhzjc maoxqhzjc = (Maoxqhzjc) ls.get(i);// 取得毛需求汇总_参考系中一条数据
					String dingdnr = maoxqhzjc.getDingdnr();
					CommonFun.logger.debug("国产件PJ模式订单计算dingdnr="+dingdnr);
					for (int j = 0; j < dingdnr.length(); j++) {
						String neir = maoxqhzjc.getDingdnr().substring(j, j + 1);
						if (neir.equalsIgnoreCase("9")) {
							jidgs = jidgs + 1;
						}
					}
					CommonFun.logger.debug("国产件PJ模式订单计算jidgs="+jidgs);
					int pianysj = (maoxqhzjc.getBeihzq().add(maoxqhzjc.getFayzq())).divide(BigDecimal.ONE, 0,
							BigDecimal.ROUND_UP).intValue();// 运输周期+备货周期向上取整
					CommonFun.logger.debug("国产件PJ模式订单计算pianysj="+pianysj);
					if(pianysj<=Const.ZJCHANGDU){
					for (int x = 0; x < jidgs; x++) {
						
						String method = Const.GETJ + (pianysj + x);// 拼接getPi方法
						CommonFun.logger.debug("国产件PJ模式订单计算既定计算method="+method);
						String methodJd = Const.GETP + x + "sl";
						CommonFun.logger.debug("国产件PJ模式订单计算既定计算methodJd="+methodJd);
						Class clsdd = dingdlj.getClass();
						Method methdjd = null;
						
						String pxriq = "";
						BigDecimal jiding = BigDecimal.ZERO;
						if (pianysj + x > Const.ZJCHANGDU) {
							jiding = BigDecimal.ZERO;
							CommonFun.logger.debug("国产件PJ模式订单计算既定计算在（pianysj + x > Const.ZJCHANGDU）时jiding="+jiding);
							resMap = this.jiDingCalculate(jiding, maoxqhzjc, flag, yingyu, x, pianysj, dingdlj, zhizlx,
									i,caozz,ddljzl);// 调用既定数量计算方法
							yingyu = resMap.get("yingyu");
							ddljzl = resMap.get("ddljzl");
							CommonFun.logger.debug("国产件PJ模式订单计算既定计算在（pianysj + x > Const.ZJCHANGDU）时yingyu="+yingyu);
						} else {
							try {
								methdjd = clsdd.getMethod(methodJd, new Class[] {});
							} catch (SecurityException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.doCalculate,SecurityException");
							} catch (NoSuchMethodException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.doCalculate,NoSuchMethodException");
							}
							jiding = this.backValue(method, maoxqhzjc, pianysj + x, dingdlx);
							CommonFun.logger.debug("国产件PJ模式订单计算既定计算不在（pianysj + x > Const.ZJCHANGDU）时jiding="+jiding);
							resMap = this.jiDingCalculate(jiding, maoxqhzjc, flag, yingyu, x, pianysj, dingdlj, zhizlx,
									i,caozz,ddljzl);// 调用既定数量计算方法
							yingyu = resMap.get("yingyu");
							ddljzl = resMap.get("ddljzl");
							CommonFun.logger.debug("国产件PJ模式订单计算既定计算不在（pianysj + x > Const.ZJCHANGDU）时yingyu="+yingyu);
							clsdd = dingdlj.getClass();
							if (x == 0) {
								methodJd = "getP" + x + "fyzqxh";
								CommonFun.logger.debug("国产件PJ模式订单计算既定计算在（x == 0）时methodJd="+methodJd);
							} else {
								methodJd = "getP" + x + "rq";
								CommonFun.logger.debug("国产件PJ模式订单计算既定计算不在（x == 0）时methodJd="+methodJd);
							}
							try {
								methdjd = clsdd.getMethod(methodJd, new Class[] {});
							} catch (SecurityException e1) {
								CommonFun.logger.error(e1);
								throw new RuntimeException("IlOrderService.doCalculate,SecurityException");
							} catch (NoSuchMethodException e1) {
								CommonFun.logger.error(e1);
								throw new RuntimeException("IlOrderService.doCalculate,NoSuchMethodException");
							}
							Object rqObj;
							try {
								rqObj = methdjd.invoke(dingdlj, null);
							} catch (IllegalArgumentException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.doCalculate,IllegalArgumentException");
							} catch (IllegalAccessException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.doCalculate,IllegalAccessException");
							} catch (InvocationTargetException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.doCalculate,InvocationTargetException");
							}
							if(null == rqObj){
								pxriq = null;
								CommonFun.logger.debug("国产件PJ模式订单计算既定计算pxriq="+pxriq);
							}else {
								try {
									pxriq = methdjd.invoke(dingdlj, null).toString();
								} catch (IllegalArgumentException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException("IlOrderService.doCalculate,IllegalArgumentException");
								} catch (IllegalAccessException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException("IlOrderService.doCalculate,IllegalAccessException");
								} catch (InvocationTargetException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException("IlOrderService.doCalculate,InvocationTargetException");
								}
								CommonFun.logger.debug("国产件PJ模式订单计算既定计算pxriq="+pxriq);
							}
						}
						methodJd = Const.GETP + x + "sl";
						CommonFun.logger.debug("国产件PJ模式订单计算既定计算methodJd="+methodJd);
						BigDecimal jissl = this.backValue(methodJd, dingdlj, x, dingdlx);
						CommonFun.logger.debug("国产件PJ模式订单计算既定计算jissl="+jissl);
						this.insertDingdMX(dingdlj, maoxqhzjc, zhizlx, x, dingdlx, dingdhMap, jissl, pxriq,caozz,Const.SHIFOUSHIJIDING,saveMingxMap);
					}
					for (int y = 0; y < Const.DINGDNRCHANGDU - jidgs; y++) {
						String method = Const.GETJ + (pianysj + jidgs + y);// 拼接getPi方法
						CommonFun.logger.debug("国产件PJ模式订单计算预告计算method="+method);
						BigDecimal yugao = BigDecimal.ZERO;
//						if(pianysj + jidgs + y>Const.ZJCHANGDU){
//							yugao = BigDecimal.ZERO;
//							yingyu = BigDecimal.ZERO;
//						}
						Class clsdd = dingdlj.getClass();
						String methodJd = Const.GETP + (jidgs + y) + "sl";
						CommonFun.logger.debug("国产件PJ模式订单计算预告计算methodJd="+methodJd);
						Method methdjd;
						try {
							methdjd = clsdd.getMethod(methodJd, new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.doCalculate,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.doCalculate,NoSuchMethodException");
						}
						
						BigDecimal jissl = BigDecimal.ZERO;
						String pxriq = "";
						if (pianysj + jidgs + y > Const.ZJCHANGDU) {
							yugao = BigDecimal.ZERO;
							yingyu = BigDecimal.ZERO;
							CommonFun.logger.debug("国产件PJ模式订单计算预告计算yugao="+yugao);
						} else {
							yugao = this.backValue(method, maoxqhzjc, pianysj + jidgs + y, dingdlx);// 执行得到的方法，取得既定周期的数量
							CommonFun.logger.debug("国产件PJ模式订单计算预告计算yugao="+yugao);
						}
							if (maoxqhzjc.getYugsfqz().equalsIgnoreCase(Const.BUYiLAI)) {
								yingyu = BigDecimal.ZERO;
								CommonFun.logger.debug("国产件PJ模式订单计算预告计算yingyu="+yingyu);
							}
								yingyu = this.yuGaoCalculate(yugao, maoxqhzjc, yingyu, jidgs + y, dingdlj);// 调用预告数量计算方法
								CommonFun.logger.debug("国产件PJ模式订单计算预告计算yingyu="+yingyu);
							if (jidgs + y == 0) {
								methodJd = "getP" + (jidgs + y) + "fyzqxh";
								CommonFun.logger.debug("国产件PJ模式订单计算预告计算在（jidgs + y == 0）时methodJd="+methodJd);
							} else {
								methodJd = "getP" + (jidgs + y) + "rq";
								CommonFun.logger.debug("国产件PJ模式订单计算预告计算不在（jidgs + y == 0）时methodJd="+methodJd);
							}
							try {
								methdjd = clsdd.getMethod(methodJd, new Class[] {});
							} catch (SecurityException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.doCalculate,SecurityException");
							} catch (NoSuchMethodException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.doCalculate,NoSuchMethodException");
							}
							Object riqObj;
							try {
								riqObj = methdjd.invoke(dingdlj, null);
							} catch (IllegalArgumentException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.doCalculate,IllegalArgumentException");
							} catch (IllegalAccessException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.doCalculate,IllegalAccessException");
							} catch (InvocationTargetException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.doCalculate,InvocationTargetException");
							}
							if(null == riqObj){
								
								pxriq = null;
								CommonFun.logger.debug("国产件PJ模式订单计算预告计算在（null == riqObj）时pxriq="+pxriq);
							}else{
								try {
									pxriq = methdjd.invoke(dingdlj, null).toString();
								} catch (IllegalArgumentException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException("IlOrderService.doCalculate,IllegalArgumentException");
								} catch (IllegalAccessException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException("IlOrderService.doCalculate,IllegalAccessException");
								} catch (InvocationTargetException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException("IlOrderService.doCalculate,InvocationTargetException");
								}
								CommonFun.logger.debug("国产件PJ模式订单计算预告计算不在（null == riqObj）时pxriq="+pxriq);
							}
						
						methodJd = Const.GETP + (jidgs + y) + "sl";
						CommonFun.logger.debug("国产件PJ模式订单计算预告计算methodJd="+methodJd);
						jissl =this.backValue(methodJd, dingdlj, jidgs + y, dingdlx);
						CommonFun.logger.debug("国产件PJ模式订单计算预告计算jissl="+jissl);
						if (pianysj + jidgs + y <= Const.ZJCHANGDU) {
						this.insertDingdMX(dingdlj, maoxqhzjc, zhizlx, jidgs + y, dingdlx, dingdhMap, jissl, pxriq,caozz,Const.SHIFOUSHIYUGAO , saveMingxMap);
						}
					}
//////////////////////////////wuyichao  
					//dingdljservice.doInsert(dingdlj);
					List<Dingdlj> flagLingj = saveLingjMap.get(dingdlj.getDingdh());
					if(null == flagLingj)
					{
						flagLingj = new ArrayList<Dingdlj>();
					}
					flagLingj.add(dingdlj);
					saveLingjMap.put(dingdlj.getDingdh(), flagLingj);
//////////////////////////////wuyichao  					
					for (int x = 0; x < banc.length; x++) {
						String bc = "";
						if (null != banc[x]) {
							bc = bc + banc[x] + ",";
						}
						banch = bc;
					}
					String dingduser = "";
					for (int z = 0; z < usercenter.length; z++) {
						if (null != usercenter[z]) {
							dingduser = dingduser + usercenter[z] + ",";
						}
					}
					this.insertdingd(dingdlj, maoxqhzjc, dingdlx, jssj, banch, jiihyz,caozz);
					}else{
						String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getGongysdm(),maoxqhzjc.getLingjbh()};
						this.yicbjservice.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str9, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginUser, Const.JISMK_IL_CD);
					}
				}
			}
			yingyu = this.ppyuGaoCalculate(dingdlx,jssj,jiihyz,banch,loginUser,saveLingjMap,saveMingxMap);
			for(int x = 0;x<banc.length;x++){
				Map<String, String> map = new HashMap<String, String>();
				map.put("xuqbc", banc[x]);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateMaoxq",map);
			}	
			banch = "";
		}
////////////////////////////////////wuyichao			
		Set<String> flagDingdhSet = saveLingjMap.keySet(); 
		for (String flagDingdh : flagDingdhSet) 
		{
			executeBatch(saveLingjMap.get(flagDingdh),"ilorder.insertDingdlj");
			executeBatch(saveMingxMap.get(flagDingdh),"ilorder.insertDingdmx");
			/////////////////////////////////////////////xiahui
			//零件流三期  PJ日订单均分 0011486
			//判断当前的的订单类型 ，如果当前订单时PJ时则将订单明细数据同时插入到PJ均分订单明细表中
			if(dingdlx.equalsIgnoreCase(Const.PJ)){
				PJdingdjf(flagDingdh);
			}
            /////////////////////////////////////////////xiahui end
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertDingdlj",saveLingjMap.get(flagDingdh));
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertDingdmx", saveMingxMap.get(flagDingdh));
		}
///////////////////////////////////wuyichao
		
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		this.orderNumberMap = new HashMap<String, String>();
		this.feneservice.setCangkMap(map);
		this.feneservice.setFlagCangku("");
		this.feneservice.setFlagLingj("");
		this.feneservice.setZhouqixuhao(0);
		this.feneservice.setZongMap(map);
		try
		{
			//如果是日订单直接生效
			//xss_0013089
			if(dingdlx.equalsIgnoreCase(Const.PJ))
			{
				this.updateDingd(dingdlx, null, Const.DINGD_STATUS_YSX, caozz,"'" +Const.DINGDLX_ILORDER + "','" + Const.DINGDLX_JL+ "'" );
			}else if(dingdlx.equalsIgnoreCase(Const.PP))
			{
				this.updateDingd_PP(dingdlx, null, Const.DINGD_STATUS_ZZZ, caozz,"'" +Const.DINGDLX_ILORDER + "','" + Const.DINGDLX_JL+ "'" );
			}else{
				this.updateDingd(dingdlx, null, Const.DINGD_STATUS_ZZZ, caozz,"'" +Const.DINGDLX_ILORDER + "','" + Const.DINGDLX_JL+ "'" );
			}
		}
		catch (Exception e) {
			CommonFun.logger.error(e.toString());
			log.error("Il订单计算出错");
			log.error(e.toString());
		}
		if(count!=0){
			message = "计算完成,但是校验时某些零件参数有误,请查看异常报警信息!";
		}else{
			message = "计算完成";
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算", "计算结束");
		CommonFun.logger.info("Il订单计算正常结束");
		log.info("Il订单计算正常结束");
		return message;
	}
	
	/**
	 * 更新PP / NP订单状态
	 * @param dingdjssj 订单计算时间
	 * @param state 订单状态
	 * @param user 操作人
	 */
	private void updateDingd_PP(String chullx, String dingdjssj,String state,String user,String dingdlx){ 
		Map<String,String> param = new HashMap<String, String>();
		param.put("state", state);
		param.put("dingdjssj", dingdjssj);
		param.put("creator", user);
		param.put("dingdlx", dingdlx);  
		
		//更新订单状态
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.updateDingdJsz_PP",param);
	}

	/**
	 * 更新订单状态
	 * @param dingdjssj 订单计算时间
	 * @param state 订单状态
	 * @param user 操作人
	 */
	private void updateDingd(String chullx, String dingdjssj,String state,String user,String dingdlx){
		//xss_0013089
		Map<String,String> param = new HashMap<String, String>();
		param.put("state", state);
		param.put("dingdjssj", dingdjssj);
		param.put("creator", user);
		param.put("dingdlx", dingdlx);
		
		param.put("chullx", chullx);
		
		
		
		//更新订单状态
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.updateDingdJsz",param);
	}

	/**
	 * <p>
	 * Title:pp国产件既定要货量计算
	 * </p>
	 * <p>
	 * Description:按照选取的毛需求计算既定
	 * </p>
	 * 参数为既定数量，以及毛需求类，以及多既定FLAG
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception
	 * @date 2011-12-1
	 **/
	public Map<String,BigDecimal> jiDingCalculate(BigDecimal jiding, Maoxqhzpc maoxqhzpc, boolean flag, BigDecimal yingyu,
			int jiszq, int pianysj, Dingdlj dingdlj, String zhizlx, Map<String, String> dingdhMap, int zhouqxh,String caozz,BigDecimal ddljzl) 
			 {
		Map<String,BigDecimal> resMap = new HashMap<String,BigDecimal>();
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算", "既定计算开始");
		BigDecimal kuc = BigDecimal.ZERO;// 库存
		BigDecimal anqkc = BigDecimal.ZERO;// 安全库存
		BigDecimal daijf = BigDecimal.ZERO;// 待交付
		BigDecimal daixh = BigDecimal.ZERO;// 待消耗
		BigDecimal yaohl = BigDecimal.ZERO;// 要货量
		BigDecimal quzhyhl = BigDecimal.ZERO;// 要货量取整后的值
		if (!flag) {
			if (zhizlx.equals(Const.ZHOUQIGONGHUOMOSHI_IL_JUANLIAO)) {
				String dingdh = dingdhMap.get(maoxqhzpc.getGongysdm()+maoxqhzpc.getUsercenter());
				CommonFun.logger.debug("PP模式既定计算卷料订单订单号dingdh="+dingdh);
				if(null == dingdh){
					resMap.put("yingyu", BigDecimal.ZERO);
					resMap.put("ddljzl", BigDecimal.ZERO);
			        return resMap;
				}
				else{
					dingdlj.setDingdh(dingdh);
				}
			} else {
				dingdlj.setDingdh(this.getOrderNumber(Const.PP, maoxqhzpc.getUsercenter(), maoxqhzpc.getGongysdm(),this.orderNumberMap));// 订单号
				CommonFun.logger.debug("PP模式既定计算国产订单订单号dingdh="+dingdlj.getDingdh());
			}
			CommonFun.logger.debug("PP模式既定计算是否依赖库存"+maoxqhzpc.getShifylkc());
			if (maoxqhzpc.getShifylkc().equalsIgnoreCase("1")) {// 查看订单计算是否依赖库存
				kuc = maoxqhzpc.getKuc();
				CommonFun.logger.debug("PP模式既定计算库存"+kuc);
			}
			CommonFun.logger.debug("PP模式既定计算是否依赖安全库存"+maoxqhzpc.getShifylaqkc());
			if (maoxqhzpc.getShifylaqkc().equalsIgnoreCase("1")) {// 查看订单计算是否依赖安全库存
				anqkc = maoxqhzpc.getAnqkc();
				CommonFun.logger.debug("PP模式既定计算安全库存"+anqkc);
			}
			CommonFun.logger.debug("PP模式既定计算是否依赖待交付"+maoxqhzpc.getShifyldjf());
			if (maoxqhzpc.getShifyldjf().equalsIgnoreCase("1")) {// 查看订单计算是否依赖待交付
				daijf = maoxqhzpc.getDingdlj().subtract(maoxqhzpc.getJiaoflj()).subtract(maoxqhzpc.getDingdzzlj());
				CommonFun.logger.debug("PP模式既定计算待交付"+daijf);
			}
			CommonFun.logger.debug("PP模式既定计算是否依赖待消耗"+maoxqhzpc.getShifyldxh());
			if (maoxqhzpc.getShifyldxh().equalsIgnoreCase("1")) {// 查看订单计算是否依赖待消耗
				daixh = maoxqhzpc.getDaixh();
				CommonFun.logger.debug("PP模式既定计算待消耗"+daixh);
			}
			
			BigDecimal tidjKuc = this
					.tidjKuc(maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), maoxqhzpc.getZiyhqrq());
			CommonFun.logger.debug("PP模式既定计算替代件库存"+tidjKuc);
			DecimalFormat data = new DecimalFormat("00");
			BigDecimal xuqiu = jiding.add(anqkc);// 计算需求
			CommonFun.logger.debug("PP模式既定计算需求"+xuqiu);
			BigDecimal ziyuan = kuc.add(daijf).subtract(daixh)// 计算资源
					.add(maoxqhzpc.getXittzz()).add(tidjKuc);
			CommonFun.logger.debug("PP模式既定计算资源"+ziyuan);
			yaohl = xuqiu.subtract(ziyuan).add( // 计算要货量
					maoxqhzpc.getTiaozjsz());
			
			
			CommonFun.logger.debug("PP模式既定计算要货量"+yaohl);

			dingdlj.setLingjbh(maoxqhzpc.getLingjbh());// 零件号
			dingdlj.setGongysdm(maoxqhzpc.getGongysdm());// 供应商代码
			dingdlj.setGongyslx(maoxqhzpc.getGongyslx());// 供应商类型
			dingdlj.setUsercenter(maoxqhzpc.getUsercenter());// 用户中心
			dingdlj.setDinghcj(maoxqhzpc.getDinghcj());// 订货车间
			dingdlj.setCangkdm(maoxqhzpc.getCangkdm());// 仓库代码
			if (Integer.parseInt(maoxqhzpc.getP0zqxh().substring(4)) + jiszq + pianysj > 12) {
				
				String nian = (Integer.parseInt(maoxqhzpc.getP0zqxh().substring(0, 4)) + 1) + "";
				CommonFun.logger.debug("PP模式既定计算nian"+nian);
				String xuhao = data.format(Integer.parseInt(maoxqhzpc.getP0zqxh().substring(4)) + jiszq + pianysj - 12);
				CommonFun.logger.debug("PP模式既定计算xuhao"+xuhao);
				dingdlj.setP0fyzqxh(nian + xuhao);// p0周期序号
				CommonFun.logger.debug("PP模式既定计算p0周期序号"+(nian + xuhao));
			} else {
				String nian = (Integer.parseInt(maoxqhzpc.getP0zqxh().substring(0, 4))) + "";
				CommonFun.logger.debug("PP模式既定计算nian"+nian);
				String xuhao = data.format(Integer.parseInt(maoxqhzpc.getP0zqxh().substring(4)) + jiszq + pianysj);
				CommonFun.logger.debug("PP模式既定计算xuhao"+xuhao);
				dingdlj.setP0fyzqxh(nian + xuhao);// p0周期序号
				CommonFun.logger.debug("PP模式既定计算p0周期序号"+(nian + xuhao));
			}

			// dingdlj.setP0fyzqxh(maoxqhzpc.getP0zqxh());
			dingdlj.setUabzlx(maoxqhzpc.getUabzlx());// ua包装类型
			dingdlj.setUabzuclx(maoxqhzpc.getUabzuclx());// ua中uc包装类型
			dingdlj.setUabzucrl(maoxqhzpc.getUabzucrl());// ua中uc包装容量
			dingdlj.setUabzucsl(maoxqhzpc.getUabzucsl());// ua中uc包装数量
			dingdlj.setDanw(maoxqhzpc.getDanw());// 单位
			dingdlj.setGongysfe(maoxqhzpc.getGongysfe());// 供应商份额
			dingdlj.setBeihzq(maoxqhzpc.getBeihzq());// 备货周期
			dingdlj.setFayzq(maoxqhzpc.getFayzq());// 发运周期
			dingdlj.setKuc(maoxqhzpc.getKuc());// 库存
			dingdlj.setAnqkc(maoxqhzpc.getAnqkc());// 安全库存
			dingdlj.setTiaozjsz(maoxqhzpc.getTiaozjsz());// 调整计算值
			dingdlj.setXittzz(maoxqhzpc.getXittzz());// 系统调整值
			
			dingdlj.setJiaoflj(maoxqhzpc.getJiaoflj());// 交付累计
			dingdlj.setDaixh(maoxqhzpc.getDaixh());// 待消耗
			dingdlj.setZiyhqrq(maoxqhzpc.getZiyhqrq());// 资源获取日期
			dingdlj.setJihyz(maoxqhzpc.getJihydz());// 计划员组
			dingdlj.setLujdm(maoxqhzpc.getLujdm());// 路径代码
			dingdlj.setYugsfqz(maoxqhzpc.getYugsfqz());// 预告是否取整
			dingdlj.setShifylaqkc(maoxqhzpc.getShifylaqkc());// 是否依赖安全库存
			dingdlj.setShifyldjf(maoxqhzpc.getShifyldjf());// 是否依赖待交付
			dingdlj.setShifyldxh(maoxqhzpc.getShifyldxh());// 是否依赖待消耗
			dingdlj.setShifylkc(maoxqhzpc.getShifylkc());// 是否依赖库存
			dingdlj.setDingdnr(maoxqhzpc.getDingdnr());// 订单内容
			dingdlj.setZhidgys(maoxqhzpc.getZhidgys());// 指定供应商
			dingdlj.setZhongzlj(maoxqhzpc.getDingdzzlj());// 订单终止
			dingdlj.setDingdzzsj(CommonFun.getJavaTime().toString());// 订单制作时间
			dingdlj.setLingjsx(maoxqhzpc.getLingjsx());
			dingdlj.setZuixqdl(maoxqhzpc.getZuixqdl());
			dingdlj.setJiaofm(maoxqhzpc.getJiaofm());
			dingdlj.setGonghms(maoxqhzpc.getWaibghms());
			dingdlj.setCreator(caozz);
			dingdlj.setCreate_time(CommonFun.getJavaTime());
			dingdlj.setEdit_time(CommonFun.getJavaTime());
			dingdlj.setEditor(caozz);
			dingdlj.setXiehzt(maoxqhzpc.getXiehztbh());
			dingdlj.setActive(Const.ACTIVE_1);
			// 调用份额分配方法
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yingyu = yaohl.negate().multiply(maoxqhzpc.getGongysfe());
				yaohl = BigDecimal.ZERO;
				Map<String, BigDecimal> map = feneservice.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh);
				quzhyhl = BigDecimal.ZERO;
			}else{
			Map<String, BigDecimal> map = feneservice.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh);
			quzhyhl = map.get("yaohl");
			yingyu = map.get("yingyu");
			}
			String method = "setP" + jiszq + "sl";// 拼接getPi方法
			CommonFun.logger.debug("PP模式既定计算份额分配后要货量"+quzhyhl);
			dingdlj.setJidyhsl(quzhyhl);
			dingdlj.setDingdlj(maoxqhzpc.getDingdlj());// 订单累计
			//dingdlj.setDingdlj(quzhyhl);// 订单累计
			Class cls = dingdlj.getClass();// 得到Dingdlj类
			Method meth;
			try {
				meth = cls.getMethod(method, BigDecimal.class);
			} catch (SecurityException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,NoSuchMethodException");
			}// 得到Dingdlj类的method拼接的方法
			try {
				meth.invoke(dingdlj, quzhyhl);
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,InvocationTargetException");
			}// 执行得到的方法，setPx

		}
		if (flag) {
			if(null == dingdlj.getDingdh()){
				resMap.put("yingyu", BigDecimal.ZERO);
				resMap.put("ddljzl", BigDecimal.ZERO);
		        return resMap;
			}
			CommonFun.logger.debug("PP模式既定计算jiding"+jiding);
			CommonFun.logger.debug("PP模式既定计算yingyu"+yingyu);
			yaohl = jiding.subtract(yingyu);
			CommonFun.logger.debug("PP模式既定计算yaohl"+yaohl);
			// 调用份额分配方法
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yingyu = yaohl.negate();
				yaohl = BigDecimal.ZERO;
				Map<String, BigDecimal> map = feneservice.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh);
				quzhyhl = BigDecimal.ZERO;
			}else{	
			Map<String, BigDecimal> map = feneservice.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh);
			quzhyhl = map.get("yaohl");
			yingyu = map.get("yingyu");
			}
			String method = "setP" + jiszq + "sl";// 拼接getPi方法
			CommonFun.logger.debug("PP模式既定计算份额分配后要货量"+quzhyhl);
			dingdlj.setJidyhsl(quzhyhl);

			Class cls = dingdlj.getClass();// 得到Dingdlj类
			Method meth;
			try {
				meth = cls.getMethod(method, BigDecimal.class);
			} catch (SecurityException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,NoSuchMethodException");
			}// 得到Dingdlj类的method拼接的方法
			try {
				meth.invoke(dingdlj, quzhyhl);
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,InvocationTargetException");
			}// 执行得到的方法，setPx

		}
		ddljzl = ddljzl.add(quzhyhl);
		resMap.put("yingyu", yingyu);
		resMap.put("ddljzl", ddljzl);
		flag = true;
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算", "既定计算结束");
		CommonFun.logger.debug("PP模式既定计算yingyu"+yingyu);
		return resMap;
	}

	/**
	 * <p>
	 * Title:ps国产件既定要货量计算
	 * </p>
	 * <p>
	 * Description:按照选取的毛需求计算既定
	 * </p>
	 * 参数为既定数量，以及毛需求类，以及多既定FLAG
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception
	 * @date 2011-12-1
	 **/
	public Map<String,BigDecimal> jiDingCalculate(BigDecimal jiding, Maoxqhzsc maoxqhzsc, boolean flag, BigDecimal yingyu,
			int jiszq, int pianysj, Dingdlj dingdlj, String zhizlx, int zhouqxh,String caozz,BigDecimal ddljzl)  {
		Map<String,BigDecimal> resMap = new HashMap<String,BigDecimal>();
		BigDecimal kuc = BigDecimal.ZERO;// 库存
		BigDecimal anqkc = BigDecimal.ZERO;// 安全库存
		BigDecimal daijf = BigDecimal.ZERO;// 待交付
		BigDecimal daixh = BigDecimal.ZERO;// 待消耗
		BigDecimal yaohl = BigDecimal.ZERO;// 要货量
		BigDecimal quzhyhl = BigDecimal.ZERO;// 要货量取整后的值
		if (!flag) {
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法是否依赖库存"+maoxqhzsc.getShifylkc());
			if (maoxqhzsc.getShifylkc().equalsIgnoreCase(Const.YiLAI)) {// 查看订单计算是否依赖库存
				kuc = maoxqhzsc.getKuc();
			}
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法是否依赖安全库存"+maoxqhzsc.getShifylaqkc());
			if (maoxqhzsc.getShifylaqkc().equalsIgnoreCase(Const.YiLAI)) {// 查看订单计算是否依赖安全库存
				anqkc = maoxqhzsc.getAnqkc();
			}
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法是否依赖待交付"+maoxqhzsc.getShifyldjf());
			if (maoxqhzsc.getShifyldjf().equalsIgnoreCase(Const.YiLAI)) {// 查看订单计算是否依赖待交付
				daijf = maoxqhzsc.getDingdlj().subtract(maoxqhzsc.getJiaoflj()).subtract(maoxqhzsc.getDingdzzlj());
			}
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法是否依赖待消耗"+maoxqhzsc.getShifyldxh());
			if (maoxqhzsc.getShifyldxh().equalsIgnoreCase(Const.YiLAI)) {// 查看订单计算是否依赖待消耗
				daixh = maoxqhzsc.getDaixh();
			}
			BigDecimal tidjKuc = this.tidjKuc(maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), maoxqhzsc.getZiyhqrq());
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法tidjKuc="+tidjKuc);
			DecimalFormat data = new DecimalFormat("00");
			BigDecimal xuqiu = jiding.add(anqkc);// 计算需求
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法jiding="+jiding);
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法anqkc="+anqkc);
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法xuqiu="+xuqiu);
			BigDecimal ziyuan = kuc.add(daijf).subtract(daixh)// 计算资源
					.add(maoxqhzsc.getXittzz()).add(tidjKuc);
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法kuc="+kuc);
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法daijf="+daijf);
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法daixh="+daixh);
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法Xittzz="+maoxqhzsc.getXittzz());
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法tidjKuc="+tidjKuc);
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法ziyuan="+ziyuan);
			yaohl = xuqiu.subtract(ziyuan).add( // 计算要货量
					maoxqhzsc.getTiaozjsz());
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法Tiaozjsz="+maoxqhzsc.getTiaozjsz());
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法yaohl="+yaohl);
			dingdlj.setDingdh(this.getOrderNumber(Const.PS, maoxqhzsc.getUsercenter(), maoxqhzsc.getGongysdm(),this.orderNumberMap));// 订单号

			dingdlj.setLingjbh(maoxqhzsc.getLingjbh());// 零件号
			dingdlj.setGongysdm(maoxqhzsc.getGongysdm());// 供应商代码
			dingdlj.setGongyslx(maoxqhzsc.getGongyslx());// 供应商类型
			dingdlj.setUsercenter(maoxqhzsc.getUsercenter());// 用户中心
			dingdlj.setDinghcj(maoxqhzsc.getDinghcj());// 订货车间
			dingdlj.setCangkdm(maoxqhzsc.getCangkdm());// 仓库代码

			Map<String, String> mapn = new HashMap<String, String>();
			mapn.put("usercenter", maoxqhzsc.getUsercenter());
			mapn.put("nianzx", maoxqhzsc.getS0zxh().substring(0, 4));
			String nianzx = calendarcenterservice.getMaxNianzx(mapn);
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法nianzx="+nianzx);
			if ((Integer.parseInt(maoxqhzsc.getS0zxh().substring(4)) + pianysj + jiszq) > Integer.parseInt(nianzx
					.substring(4))) {
				String nian = Integer.parseInt(maoxqhzsc.getS0zxh().substring(0, 4)) + 1 + "";
				CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法在（(Integer.parseInt(maoxqhzsc.getS0zxh().substring(4)) + pianysj + jiszq) > Integer.parseInt(nianzx.substring(4))）时nian="+nian);
				String zhoux = Integer.parseInt(maoxqhzsc.getS0zxh().substring(4)) + pianysj + jiszq
						- Integer.parseInt(nianzx.substring(5)) + "";
				CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法在（(Integer.parseInt(maoxqhzsc.getS0zxh().substring(4)) + pianysj + jiszq) > Integer.parseInt(nianzx.substring(4))）时zhoux="+zhoux);
				dingdlj.setP0fyzqxh(nian + zhoux);
			} else {
				String nian = (Integer.parseInt(maoxqhzsc.getS0zxh().substring(0, 4))) + "";
				CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法不在（(Integer.parseInt(maoxqhzsc.getS0zxh().substring(4)) + pianysj + jiszq) > Integer.parseInt(nianzx.substring(4))）时nian="+nian);
				String zhoux = data.format(Integer.parseInt(maoxqhzsc.getS0zxh().substring(4)) + jiszq + pianysj);
				CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法不在（(Integer.parseInt(maoxqhzsc.getS0zxh().substring(4)) + pianysj + jiszq) > Integer.parseInt(nianzx.substring(4))）时nian="+nian);
				dingdlj.setP0fyzqxh(nian + zhoux);
			}

			//dingdlj.setP0fyzqxh(maoxqhzsc.getS0zxh());// p0周期序号
			dingdlj.setUabzlx(maoxqhzsc.getUabzlx());// ua包装类型
			dingdlj.setUabzuclx(maoxqhzsc.getUabzuclx());// ua中uc包装类型
			dingdlj.setUabzucrl(maoxqhzsc.getUabzucrl());// ua中uc包装容量
			dingdlj.setUabzucsl(maoxqhzsc.getUabzucsl());// ua中uc包装数量
			dingdlj.setDanw(maoxqhzsc.getDanw());// 单位
			dingdlj.setGongysfe(maoxqhzsc.getGongysfe());// 供应商份额
			dingdlj.setBeihzq(maoxqhzsc.getBeihzq());// 备货周期
			dingdlj.setFayzq(maoxqhzsc.getFayzq());// 发运周期
			dingdlj.setKuc(maoxqhzsc.getKuc());// 库存
			dingdlj.setAnqkc(maoxqhzsc.getAnqkc());// 安全库存
			dingdlj.setTiaozjsz(maoxqhzsc.getTiaozjsz());// 调整计算值
			dingdlj.setXittzz(maoxqhzsc.getXittzz());// 系统调整值
			dingdlj.setDingdlj(maoxqhzsc.getDingdlj());// 订单累计
			dingdlj.setJiaoflj(maoxqhzsc.getJiaoflj());// 交付累计
			dingdlj.setDaixh(maoxqhzsc.getDaixh());// 待消耗
			dingdlj.setZiyhqrq(maoxqhzsc.getZiyhqrq());// 资源获取日期
			dingdlj.setJihyz(maoxqhzsc.getJihyz());// 计划员组
			dingdlj.setLujdm(maoxqhzsc.getLujdm());// 路径代码
			dingdlj.setYugsfqz(maoxqhzsc.getYugsfqz());// 预告是否取整
			dingdlj.setShifylaqkc(maoxqhzsc.getShifylaqkc());// 是否依赖安全库存
			dingdlj.setShifyldjf(maoxqhzsc.getShifyldjf());// 是否依赖待交付
			dingdlj.setShifyldxh(maoxqhzsc.getShifyldxh());// 是否依赖待消耗
			dingdlj.setShifylkc(maoxqhzsc.getShifylkc());// 是否依赖库存
			dingdlj.setDingdnr(maoxqhzsc.getDingdnr());// 订单内容
			dingdlj.setZhidgys(maoxqhzsc.getZhidgys());// 指定供应商
			dingdlj.setDingdzzsj(CommonFun.getJavaTime().toString());// 订单制作时间
			dingdlj.setLingjsx(maoxqhzsc.getLingjsx());
			dingdlj.setZuixqdl(maoxqhzsc.getZuixqdl());
			dingdlj.setGonghms(maoxqhzsc.getWaibghms());
			dingdlj.setCreator(caozz);
			dingdlj.setCreate_time(CommonFun.getJavaTime());
			dingdlj.setEditor(caozz);
			dingdlj.setEdit_time(CommonFun.getJavaTime());
			dingdlj.setXiehzt(maoxqhzsc.getXiehztbh());
			dingdlj.setActive(Const.ACTIVE_1);
			// 零件是否为卷料判断
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yingyu = yaohl.negate();
				yaohl = BigDecimal.ZERO;
				Map<String, BigDecimal> map = feneservice.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh);
				quzhyhl = BigDecimal.ZERO;
			}else{	
			Map<String, BigDecimal> map = feneservice.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh);
			
			quzhyhl = map.get("yaohl");
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法quzhyhl="+quzhyhl);
			yingyu = map.get("yingyu");
			}
			String method = "setP" + jiszq + "sl";// 拼接getPi方法
			Class cls = dingdlj.getClass();// 得到Dingdlj类
			Method meth;
			try {
				meth = cls.getMethod(method, BigDecimal.class);
			} catch (SecurityException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,NoSuchMethodException");
			}// 得到Dingdlj类的method拼接的方法
			try {
				meth.invoke(dingdlj, quzhyhl);
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,InvocationTargetException");
			}// 执行得到的方法，setPx

		}
		if (flag) {
			yaohl = jiding.subtract(yingyu);
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yingyu = yaohl.negate();
				yaohl = BigDecimal.ZERO;
				Map<String, BigDecimal> map = feneservice.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh);
				quzhyhl = BigDecimal.ZERO;
			}else{	
			Map<String, BigDecimal> map = feneservice.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh);
			quzhyhl = map.get("yaohl");
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法quzhyhl="+quzhyhl);
			yingyu = map.get("yingyu");
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法yingyu="+yingyu);
			}
			String method = "setP" + jiszq + "sl";// 拼接getPi方法
			CommonFun.logger.debug("国产件订单计算PS模式既定计算jiDingCalculate方法method="+method);
			Class cls = dingdlj.getClass();// 得到Dingdlj类
			Method meth;
			try {
				meth = cls.getMethod(method, BigDecimal.class);
			} catch (SecurityException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,NoSuchMethodException");
			}// 得到Dingdlj类的method拼接的方法
			try {
				meth.invoke(dingdlj, quzhyhl);
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,InvocationTargetException");
			}// 执行得到的方法，setPx
		}
		ddljzl = ddljzl.add(quzhyhl);
		flag = true;
		resMap.put("yingyu", yingyu);
		resMap.put("ddljzl", ddljzl);
        return resMap;
	}

	/**
	 * <p>
	 * Title:pj国产件既定要货量计算
	 * </p>
	 * <p>
	 * Description:按照选取的毛需求计算既定
	 * </p>
	 * 参数为既定数量，以及毛需求类，以及多既定FLAG
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception
	 * @date 2011-12-1
	 **/
	public Map<String,BigDecimal> jiDingCalculate(BigDecimal jiding, Maoxqhzjc maoxqhzjc, boolean flag, BigDecimal yingyu,
			int jiszq, int pianysj, Dingdlj dingdlj, String zhizlx, int zhouqxh,String caozz,BigDecimal ddljzl) {
		Map<String,BigDecimal> resMap = new HashMap<String,BigDecimal>();
		BigDecimal kuc = BigDecimal.ZERO;// 库存
		BigDecimal anqkc = BigDecimal.ZERO;// 安全库存
		BigDecimal daijf = BigDecimal.ZERO;// 待交付
		BigDecimal daixh = BigDecimal.ZERO;// 待消耗
		BigDecimal yaohl = BigDecimal.ZERO;// 要货量
		BigDecimal quzhyhl = BigDecimal.ZERO;// 要货量取整后的值
		if (!flag) {
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法是否依赖库存"+maoxqhzjc.getShifylkc());
			if (maoxqhzjc.getShifylkc().equalsIgnoreCase("1")) {// 查看订单计算是否依赖库存
				kuc = maoxqhzjc.getKuc();
			}
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法是否依赖安全库存"+maoxqhzjc.getShifylaqkc());
			if (maoxqhzjc.getShifylaqkc().equalsIgnoreCase("1")) {// 查看订单计算是否依赖安全库存
				anqkc = maoxqhzjc.getAnqkc();
			}
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法是否依赖待交付"+maoxqhzjc.getShifyldjf());
			if (maoxqhzjc.getShifyldjf().equalsIgnoreCase("1")) {// 查看订单计算是否依赖待交付
				daijf = maoxqhzjc.getDingdlj().subtract(maoxqhzjc.getJiaoflj()).subtract(maoxqhzjc.getDingdzzlj());
			}
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法是否依赖待消耗"+maoxqhzjc.getShifyldxh());
			if (maoxqhzjc.getShifyldxh().equalsIgnoreCase("1")) {// 查看订单计算是否依赖待消耗
				daixh = maoxqhzjc.getDaixh();
			}
			BigDecimal tidjKuc = this
					.tidjKuc(maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), maoxqhzjc.getZiyhqrq());// 替代件数量
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法tidjKuc="+tidjKuc);
			DecimalFormat data = new DecimalFormat("00");
			BigDecimal xuqiu = jiding.add(anqkc);// 计算需求
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法jiding="+jiding);
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法anqkc="+anqkc);
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法xuqiu="+xuqiu);
			BigDecimal ziyuan = kuc.add(daijf).subtract(daixh)// 计算资源
					.add(maoxqhzjc.getXittzz()).add(tidjKuc);
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法kuc="+kuc);
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法daijf="+daijf);
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法daixh="+daixh);
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法Xittzz="+maoxqhzjc.getXittzz());
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法tidjKuc="+tidjKuc);
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法ziyuan="+ziyuan);
			yaohl = xuqiu.subtract(ziyuan).add( // 计算要货量
					maoxqhzjc.getTiaozjsz());
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yaohl = BigDecimal.ZERO;
			}
			
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法Tiaozjsz()="+maoxqhzjc.getTiaozjsz());
			dingdlj.setDingdh(this.getOrderNumber(Const.PJ, maoxqhzjc.getUsercenter(), maoxqhzjc.getGongysdm(),this.orderNumberMap));// 订单号

			dingdlj.setLingjbh(maoxqhzjc.getLingjbh());// 零件号
			dingdlj.setGongysdm(maoxqhzjc.getGongysdm());// 供应商代码
			dingdlj.setGongyslx(maoxqhzjc.getGongyslx());// 供应商类型
			dingdlj.setUsercenter(maoxqhzjc.getUsercenter());// 用户中心
			dingdlj.setDinghcj(maoxqhzjc.getDinghcj());// 订货车间
			dingdlj.setCangkdm(maoxqhzjc.getCangkdm());// 仓库代码
			int px = maoxqhzjc.getBeihzq().add(maoxqhzjc.getFayzq()).divide(BigDecimal.ONE, 0, BigDecimal.ROUND_UP)
					.intValue();
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法px="+jiszq);
			String method = "";// 拼接getPi方法
			if (px == 0) {
				method = "getJ" + px + "rq";// 拼接getPi方法
				CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法在（）时method="+method);
			} else {
				method = "getJ" + px + "riq";
				CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法不在（）时method="+method);
				;// 拼接getPi方法
			}

			String pxriq = null;
			Class cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
			Method meth;
			try {
				meth = cls.getMethod(method, new Class[] {});
			} catch (SecurityException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,NoSuchMethodException");
			}// 得到Maoxqhzpc类的method拼接的方法
			Object obj;
			try {
				obj = meth.invoke(maoxqhzjc, null);
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,InvocationTargetException");
			}
			
			if(null == obj){
				dingdlj.setP0fyzqxh(null);
				CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法P0fyzqxh="+null);
			}else {
				try {
					pxriq = meth.invoke(maoxqhzjc, null).toString();
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,InvocationTargetException");
				}// 执行得到的方法，取得预告周期的数量
				CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法pxriq="+pxriq);
				dingdlj.setP0fyzqxh(pxriq.substring(0, 10));// p0周期序号
				CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法P0fyzqxh="+pxriq.substring(0, 10));
			}	
			dingdlj.setUabzlx(maoxqhzjc.getUabzlx());// ua包装类型
			dingdlj.setUabzuclx(maoxqhzjc.getUabzuclx());// ua中uc包装类型
			dingdlj.setUabzucrl(maoxqhzjc.getUabzucrl());// ua中uc包装容量
			dingdlj.setUabzucsl(maoxqhzjc.getUabzucsl());// ua中uc包装数量
			dingdlj.setDanw(maoxqhzjc.getDanw());// 单位
			dingdlj.setGongysfe(maoxqhzjc.getGongysfe());// 供应商份额
			dingdlj.setBeihzq(maoxqhzjc.getBeihzq());// 备货周期
			dingdlj.setFayzq(maoxqhzjc.getFayzq());// 发运周期
			dingdlj.setKuc(maoxqhzjc.getKuc());// 库存
			dingdlj.setAnqkc(maoxqhzjc.getAnqkc());// 安全库存
			dingdlj.setTiaozjsz(maoxqhzjc.getTiaozjsz());// 调整计算值
			dingdlj.setXittzz(maoxqhzjc.getXittzz());// 系统调整值
			dingdlj.setDingdlj(maoxqhzjc.getDingdlj());// 订单累计
			dingdlj.setJiaoflj(maoxqhzjc.getJiaoflj());// 交付累计
			dingdlj.setDaixh(maoxqhzjc.getDaixh());// 待消耗
			dingdlj.setZiyhqrq(maoxqhzjc.getZiyhqrq());// 资源获取日期
			dingdlj.setJihyz(maoxqhzjc.getJihyz());// 计划员组
			dingdlj.setLujdm(maoxqhzjc.getLujdm());// 路径代码
			dingdlj.setYugsfqz(maoxqhzjc.getYugsfqz());// 预告是否取整
			dingdlj.setShifylaqkc(maoxqhzjc.getShifylaqkc());// 是否依赖安全库存
			dingdlj.setShifyldjf(maoxqhzjc.getShifyldjf());// 是否依赖待交付
			dingdlj.setShifyldxh(maoxqhzjc.getShifyldxh());// 是否依赖待消耗
			dingdlj.setShifylkc(maoxqhzjc.getShifylkc());// 是否依赖库存
			dingdlj.setDingdnr(maoxqhzjc.getDingdnr());// 订单内容
			dingdlj.setZhidgys(maoxqhzjc.getZhidgys());// 指定供应商
			dingdlj.setDingdzzsj(CommonFun.getJavaTime().toString());// 订单制作时间
			dingdlj.setLingjsx(maoxqhzjc.getLingjsx());
			dingdlj.setZuixqdl(maoxqhzjc.getZuixqdl());
			dingdlj.setGonghms(maoxqhzjc.getWaibghms());
			dingdlj.setCreator(caozz);
			dingdlj.setCreate_time(CommonFun.getJavaTime());
			dingdlj.setEditor(caozz);
			dingdlj.setXiehzt(maoxqhzjc.getXiehztbh());
			dingdlj.setEdit_time(CommonFun.getJavaTime());
			dingdlj.setActive(Const.ACTIVE_1);
			for (int z = 1; z < 10; z++) {
				//xss-20160422
				if( (px + z) > 15 ){
					break;
				}
				
				method = "getJ" + (px + z) + "riq";
				CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法method="+method);
				;// 拼接getPi方法
				cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
				try {
					meth = cls.getMethod(method, new Class[] {});
				} catch (SecurityException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,SecurityException");
				} catch (NoSuchMethodException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,NoSuchMethodException");
				}// 得到Maoxqhzpc类的method拼接的方法
				Object riqObj;
				try {
					riqObj = meth.invoke(maoxqhzjc, null);
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,InvocationTargetException");
				}
				if(null == riqObj){
					pxriq = null;
					CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法pxriq="+pxriq);
				}else {
					try {
						pxriq = meth.invoke(maoxqhzjc, null).toString();
					} catch (IllegalArgumentException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalArgumentException");
					} catch (IllegalAccessException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalAccessException");
					} catch (InvocationTargetException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.jiDingCalculate,InvocationTargetException");
					}// 执行得到的方法，取得预告周期的数量
					CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法pxriq="+pxriq);
				}
				
				method = "setP" + (z) + "rq";
				;// 拼接getPi方法
				cls = dingdlj.getClass();// 得到Maoxqhzpc类
				try {
					meth = cls.getMethod(method, String.class);
				} catch (SecurityException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,SecurityException");
				} catch (NoSuchMethodException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,NoSuchMethodException");
				}// 得到Maoxqhzpc类的method拼接的方法
				try {
					meth.invoke(dingdlj, pxriq);
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.jiDingCalculate,InvocationTargetException");
				}// 执行得到的方法，setPx
			}

			// 调用份额分配方法

			// 零件是否为卷料判断
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yingyu = yaohl.negate();
				yaohl = BigDecimal.ZERO;
				Map<String, BigDecimal> map = feneservice.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh);
				quzhyhl = BigDecimal.ZERO;
			}else{	
			Map<String, BigDecimal> map = feneservice.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh);
			quzhyhl = map.get("yaohl");
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法quzhyhl="+quzhyhl);
			yingyu = map.get("yingyu");
			}
			method = "setP" + jiszq + "sl";// 拼接getPi方法
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法method="+method);
			cls = dingdlj.getClass();// 得到Dingdlj类
			try {
				meth = cls.getMethod(method, BigDecimal.class);
			} catch (SecurityException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,NoSuchMethodException");
			}// 得到Dingdlj类的method拼接的方法
			try {
				meth.invoke(dingdlj, quzhyhl);
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,InvocationTargetException");
			}// 执行得到的方法，setPx

		}
		if (flag) {
			yaohl = jiding.subtract(yingyu);
			// 零件是否为卷料判断
			Map<String, BigDecimal> map = feneservice.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh);
			quzhyhl = map.get("yaohl");
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法quzhyhl="+quzhyhl);
			yingyu = map.get("yingyu");
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法yingyu="+yingyu);
			String method = "setP" + jiszq + "sl";// 拼接getPi方法
			CommonFun.logger.debug("国产件订单计算PJ模式既定计算jiDingCalculate方法method="+method);
			Class cls = dingdlj.getClass();// 得到Dingdlj类
			Method meth;
			try {
				meth = cls.getMethod(method, BigDecimal.class);
			} catch (SecurityException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,NoSuchMethodException");
			}// 得到Dingdlj类的method拼接的方法
			try {
				meth.invoke(dingdlj, quzhyhl);
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,InvocationTargetException");
			}// 执行得到的方法，setPx
		}
		ddljzl = ddljzl.add(quzhyhl);
		flag = true;
		resMap.put("yingyu", yingyu);
		resMap.put("ddljzl", ddljzl);
        return resMap;
	}

	/**
	 * <p>
	 * Title:pp国产件pp模式预告要货量计算
	 * </p>
	 * <p>
	 * Description:按照选取的毛需求计算预告
	 * </p>
	 * 参数为预告数量，毛需求类汇总参考系类，盈余量，计算周期，订单零件类
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception
	 * @date 2011-12-1
	 **/
	public BigDecimal yuGaoCalculate(BigDecimal yugao, Maoxqhzpc maoxqhzpc, BigDecimal yingyu, int jiszq,
			Dingdlj dingdlj)  {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il周期订单计算", "周期预告计算开始");
		CommonFun.logger.debug("国产件PP模式订单预告计算订单号为："+dingdlj.getDingdh());
		if(null == dingdlj.getDingdh()){
			return yingyu = BigDecimal.ZERO;
		}
		BigDecimal yaohl = BigDecimal.ZERO;// 要货量
		if (maoxqhzpc.getYugsfqz().equalsIgnoreCase(Const.YiLAI)) {
			CommonFun.logger.debug("国产件PP模式订单预告计算预告取整情况下：yugao="+yugao);
			CommonFun.logger.debug("国产件PP模式订单预告计算预告取整情况下：yingyu="+yingyu);
			CommonFun.logger.debug("国产件PP模式订单预告计算预告取整情况下：Gongysfe="+maoxqhzpc.getGongysfe());
			CommonFun.logger.debug("国产件PP模式订单预告计算预告取整情况下：getBaozrl="+maoxqhzpc.getBaozrl());
			yaohl = yugao.multiply(maoxqhzpc.getGongysfe()).subtract(yingyu);
					
			CommonFun.logger.debug("国产件PP模式订单预告计算预告取整情况下：yaohl="+yaohl);
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yingyu = yaohl.negate();
				yaohl = BigDecimal.ZERO;
				CommonFun.logger.debug("国产件PP模式订单预告计算计算得到的要货量小于0的情况下：yaohl="+yaohl);
			}else{
				yaohl = yaohl.divide(maoxqhzpc.getBaozrl(), 0, BigDecimal.ROUND_UP).multiply(maoxqhzpc.getBaozrl());// 毛需求乘以供应商份额然后按包装容量取整
				yingyu = yaohl.subtract(yugao.subtract(yingyu).multiply(maoxqhzpc.getGongysfe()));
			}
			CommonFun.logger.debug("国产件PP模式订单预告计算预告取整情况下：yingyu="+yingyu);

		} else {
			CommonFun.logger.debug("国产件PP模式订单预告计算预告不取整情况下：yugao="+yugao);
			CommonFun.logger.debug("国产件PP模式订单预告计算预告不取整情况下：Gongysfe="+maoxqhzpc.getGongysfe());
			yaohl = yugao.multiply(maoxqhzpc.getGongysfe());
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yaohl = BigDecimal.ZERO;
				CommonFun.logger.debug("国产件PP模式订单预告计算计算得到的要货量小于0的情况下：yaohl="+yaohl);
			}
			CommonFun.logger.debug("国产件PP模式订单预告计算预告取整情况下：yingyu="+yingyu);
			CommonFun.logger.debug("国产件PP模式订单预告计算预告不取整情况下：yaohl="+yaohl);
			yingyu = BigDecimal.ZERO;
			CommonFun.logger.debug("国产件PP模式订单预告计算预告不取整情况下：yingyu="+yingyu);
		}
		
		
		String method = "setP" + jiszq + "sl";// 拼接getPi方法
		Class cls = dingdlj.getClass();// 得到Dingdlj类
		Method meth;
		try {
			meth = cls.getMethod(method, BigDecimal.class);
		} catch (SecurityException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,SecurityException");
		} catch (NoSuchMethodException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,NoSuchMethodException");
		}// 得到Dingdlj类的method拼接的方法
		try {
			meth.invoke(dingdlj, yaohl);
		} catch (IllegalArgumentException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalArgumentException");
		} catch (IllegalAccessException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalAccessException");
		} catch (InvocationTargetException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,InvocationTargetException");
		}// 执行得到的方法，setPx
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il周期订单计算", "周期预告计算结束");
		CommonFun.logger.debug("国产件PP模式订单预告计算：yingyu="+yingyu);
		return yingyu;
		
	}

	/**
	 * <p>
	 * Title:pp国产件ps模式预告要货量计算
	 * </p>
	 * <p>
	 * Description:按照选取的毛需求计算预告
	 * </p>
	 * 参数为预告数量，毛需求类汇总参考系类，盈余量，计算周期，订单零件类
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception
	 * @date 2011-12-1
	 **/
	public BigDecimal yuGaoCalculate(BigDecimal yugao, Maoxqhzsc maoxqhzsc, BigDecimal yingyu, int jiszq,
			Dingdlj dingdlj)   {
		BigDecimal yaohl = BigDecimal.ZERO;// 要货量
		
		if (maoxqhzsc.getYugsfqz().equalsIgnoreCase(Const.YiLAI)) {
			CommonFun.logger.debug("国产件PS模式订单预告计算预告取整情况下：yugao="+yugao);
			CommonFun.logger.debug("国产件PS模式订单预告计算预告取整情况下：yingyu="+yingyu);
			CommonFun.logger.debug("国产件PS模式订单预告计算预告取整情况下：Gongysfe="+maoxqhzsc.getGongysfe());
			CommonFun.logger.debug("国产件PS模式订单预告计算预告取整情况下：getBaozrl="+maoxqhzsc.getBaozrl());

			yaohl = yugao.multiply(maoxqhzsc.getGongysfe()).subtract(yingyu);
					
			CommonFun.logger.debug("国产件PS模式订单预告计算预告取整情况下：yaohl="+yaohl);
			
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yingyu = yaohl.negate();
				yaohl = BigDecimal.ZERO;
				CommonFun.logger.debug("国产件PP模式订单预告计算计算得到的要货量小于0的情况下：yaohl="+yaohl);
			}else{
				yaohl = yaohl.divide(maoxqhzsc.getBaozrl(), 0, BigDecimal.ROUND_UP).multiply(maoxqhzsc.getBaozrl());// 毛需求乘以供应商份额然后按包装容量取整
			yingyu = yaohl.subtract(yugao.subtract(yingyu).multiply(maoxqhzsc.getGongysfe()));
			}
			CommonFun.logger.debug("国产件PS模式订单预告计算预告取整情况下：yingyu="+yingyu);

		} else {
			CommonFun.logger.debug("国产件PS模式订单预告计算预告不取整情况下：yugao="+yugao);
			CommonFun.logger.debug("国产件PS模式订单预告计算预告不取整情况下：Gongysfe="+maoxqhzsc.getGongysfe());
			yaohl = yugao.multiply(maoxqhzsc.getGongysfe());
			CommonFun.logger.debug("国产件PS模式订单预告计算预告不取整情况下：yaohl="+yaohl);
			yingyu = BigDecimal.ZERO;
			CommonFun.logger.debug("国产件PS模式订单预告计算预告不取整情况下：yingyu="+yingyu);
		}
		if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
			yaohl = BigDecimal.ZERO;
			CommonFun.logger.debug("国产件PS模式订单预告计算计算得到的要货量小于0的情况下：yaohl="+yaohl);
		}
		String method = "setP" + jiszq + "sl";// 拼接getPi方法
		Class cls = dingdlj.getClass();// 得到Dingdlj类
		Method meth;
		try {
			meth = cls.getMethod(method, BigDecimal.class);
		} catch (SecurityException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,SecurityException");
		} catch (NoSuchMethodException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,NoSuchMethodException");
		}// 得到Dingdlj类的method拼接的方法
		try {
			meth.invoke(dingdlj, yaohl);
		} catch (IllegalArgumentException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalArgumentException");
		} catch (IllegalAccessException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalAccessException");
		} catch (InvocationTargetException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,InvocationTargetException");
		}// 执行得到的方法，setPx
		CommonFun.logger.debug("国产件PS模式订单预告计算：yingyu="+yingyu);
		return yingyu;
	}

	/**
	 * <p>
	 * Title:pp国产件pj模式预告要货量计算
	 * </p>
	 * <p>
	 * Description:按照选取的毛需求计算预告
	 * </p>
	 * 参数为预告数量，毛需求类汇总参考系类，盈余量，计算周期，订单零件类
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception
	 * @date 2011-12-1
	 **/
	public BigDecimal yuGaoCalculate(BigDecimal yugao, Maoxqhzjc maoxqhzjc, BigDecimal yingyu, int jiszq,
			Dingdlj dingdlj)  {

		BigDecimal yaohl = BigDecimal.ZERO;// 要货量

		BigDecimal yingyl = BigDecimal.ZERO;// 盈余量

		if (maoxqhzjc.getYugsfqz().equalsIgnoreCase(Const.YiLAI)) {
			CommonFun.logger.debug("国产件PJ模式订单预告计算预告取整情况下：yugao="+yugao);
			CommonFun.logger.debug("国产件PJ模式订单预告计算预告取整情况下：yingyu="+yingyu);
			CommonFun.logger.debug("国产件PJ模式订单预告计算预告取整情况下：Gongysfe="+maoxqhzjc.getGongysfe());
			CommonFun.logger.debug("国产件PJ模式订单预告计算预告取整情况下：getBaozrl="+maoxqhzjc.getBaozrl());

			yaohl = yugao.multiply(maoxqhzjc.getGongysfe()).subtract(yingyu);
					
			
			CommonFun.logger.debug("国产件PJ模式订单预告计算预告取整情况下：yaohl="+yaohl);
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yingyl = yaohl.negate();
				yaohl = BigDecimal.ZERO;
				CommonFun.logger.debug("国产件PP模式订单预告计算计算得到的要货量小于0的情况下：yaohl="+yaohl);
			}else{
				yaohl = yaohl.divide(maoxqhzjc.getBaozrl(), 0, BigDecimal.ROUND_UP).multiply(maoxqhzjc.getBaozrl());// 毛需求乘以供应商份额然后按包装容量取整
			yingyl = yaohl.subtract(yugao.subtract(yingyu).multiply(maoxqhzjc.getGongysfe()));
			}
			CommonFun.logger.debug("国产件PJ模式订单预告计算预告取整情况下：yingyl="+yingyl);

		} else {
			CommonFun.logger.debug("国产件PJ模式订单预告计算预告不取整情况下：yugao="+yugao);
			CommonFun.logger.debug("国产件PJ模式订单预告计算预告不取整情况下：Gongysfe="+maoxqhzjc.getGongysfe());
			yaohl = yugao.multiply(maoxqhzjc.getGongysfe());
			CommonFun.logger.debug("国产件PJ模式订单预告计算预告不取整情况下：yaohl="+yaohl);
			yingyl = BigDecimal.ZERO;
			CommonFun.logger.debug("国产件PJ模式订单预告计算预告不取整情况下：yingyl="+yingyl);
		}
		if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
			yaohl = BigDecimal.ZERO;
			CommonFun.logger.debug("国产件PJ模式订单预告计算计算得到的要货量小于0的情况下：yaohl="+yaohl);
		}
		String method = "setP" + jiszq + "sl";// 拼接getPi方法
		Class cls = dingdlj.getClass();// 得到Dingdlj类
		Method meth;
		try {
			meth = cls.getMethod(method, BigDecimal.class);
		} catch (SecurityException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,SecurityException");
		} catch (NoSuchMethodException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,NoSuchMethodException");
		}// 得到Dingdlj类的method拼接的方法
		try {
			meth.invoke(dingdlj, yaohl);
		} catch (IllegalArgumentException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalArgumentException");
		} catch (IllegalAccessException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalAccessException");
		} catch (InvocationTargetException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException("IlOrderService.yuGaoCalculate,InvocationTargetException");
		}// 执行得到的方法，setPx
		CommonFun.logger.debug("国产件PJ模式订单预告计算：yingyu="+yingyu);
		return yingyl;
	}

	/**
	 * @Title:pp国产件非pp模式预告要货量计算
	 * @author 陈骏
	 * @version v1.0
	 * @throws ParseException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @date 2011-12-1
	 **/
	public BigDecimal ppyuGaoCalculate(String dingdlx,String jssj,String jihyz,String banc,LoginUser loginUser,Map<String, List<Dingdlj>> saveLingjMap, Map<String, List<Dingdmx>> saveMingxMap)  {

		List yugList = new ArrayList();
		BigDecimal yingyl = BigDecimal.ZERO;// 盈余量
		
		if (dingdlx.equalsIgnoreCase(Const.PP)) {
///////////////////////////wuyichao
			List<Yugzjb> rsList = new ArrayList<Yugzjb>();
			Map<String,Maoxqhzpc> maoxqhzpcMap = new HashMap<String, Maoxqhzpc>();
			String p0zhouqxh = "";
			Map<String , String> maxNianzqMap = new HashMap<String, String>();
			Map<String,String> usercenterMap = new HashMap<String, String>();
			//this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il周期订单计算", "非周期模式计算开始");
			CommonFun.logger.debug("非周期模式计算开始");
			yugList = this.maoxqhzpcService.selectYugao(dingdlx);
			for(int i=0 ;i< yugList.size() ; i ++)
			{
				Maoxqhzpc maoxqhzpc = (Maoxqhzpc) yugList.get(i);
				if( i == 0 ) 
				{
					p0zhouqxh = maoxqhzpc.getP0zqxh();
				}
				if(null != maoxqhzpc.getUsercenter())
					usercenterMap.put(maoxqhzpc.getUsercenter(), maoxqhzpc.getUsercenter());
			}
			Set<String> usercenterSet = usercenterMap.keySet();
			if(StringUtils.isNotBlank(p0zhouqxh))
			{
				for (String string : usercenterSet) {
					getMaxniqnzq(p0zhouqxh,string,maxNianzqMap);
				}
			}
			//CommonFun.objListPrint(yugList, "非PP模式预告计算中间pc表查询结果list");
			BigDecimal yaohl = BigDecimal.ZERO;// 要货量
			for (int i = 0; i < yugList.size(); i++) {
				Maoxqhzpc maoxqhzpc = (Maoxqhzpc) yugList.get(i);
				
				String dingdnr = maoxqhzpc.getDingdnr();
				CommonFun.logger.debug("非PP模式预告计算订单内容：dingdnr="+dingdnr);
				CommonFun.logger.debug("非PP模式预告计算备货周期：maoxqhzpc.getBeihzq()="+maoxqhzpc.getBeihzq());
				CommonFun.logger.debug("非PP模式预告计算发运周期：maoxqhzpc.getFayzq()="+maoxqhzpc.getFayzq());
				int pianysj = (maoxqhzpc.getBeihzq().add(maoxqhzpc.getFayzq())).divide(new BigDecimal(30), 0,
						BigDecimal.ROUND_UP).intValue();// 运输周期+备货周期向上取整
				CommonFun.logger.debug("非PP模式预告计算偏移时间：pianysj="+pianysj);
				if(pianysj<=Const.ZPCHANGDU){
				maoxqhzpcMap.put(maoxqhzpc.getUsercenter() + maoxqhzpc.getLingjbh() + maoxqhzpc.getGongysdm(), maoxqhzpc);
				
				for (int x = 0; x < dingdnr.length(); x++) {
					
					Yugzjb yugzjb = new Yugzjb();
					yugzjb.setUsercenter(maoxqhzpc.getUsercenter());// 用户中心
					yugzjb.setGongysdm(maoxqhzpc.getGongysdm());// 供应商代码
					yugzjb.setGongyslx(maoxqhzpc.getGongyslx());// 供应商类型
					yugzjb.setLingjbh(maoxqhzpc.getLingjbh());// 零件编号
					yugzjb.setCangkdm(maoxqhzpc.getCangkdm());// 仓库代码
					yugzjb.setGonghlx(maoxqhzpc.getWaibghms());// 供货模式
					yugzjb.setDanw(maoxqhzpc.getDanw());// 单位
					yugzjb.setUabzlx(maoxqhzpc.getUabzlx());// ua包装类型
					yugzjb.setUabzuclx(maoxqhzpc.getUabzuclx());// ua中uc包装类型
					yugzjb.setUabzucrl(maoxqhzpc.getUabzucrl());// ua中uc包装的容量
					yugzjb.setUabzucsl(maoxqhzpc.getUabzucsl());// ua中uc的包装数量
					String zhouqi = "";
					String fayzq = "";
					//fayzq = this.calendarCenterService.nianzqGroup(maoxqhzpc.getP0zqxh(),pianysj,
					//		maoxqhzpc.getUsercenter()).get(pianysj);
					fayzq = this.calendarCenterService.nianzqGroup(p0zhouqxh, maxNianzqMap.get(maoxqhzpc.getUsercenter()), pianysj).get(pianysj);
					CommonFun.logger.debug("非PP模式预告计算发运周期：fayzq="+fayzq);
					//zhouqi = this.calendarCenterService.nianzqGroup(maoxqhzpc.getP0zqxh(),pianysj+ x,
					//		maoxqhzpc.getUsercenter()).get(x);
					zhouqi = this.calendarCenterService.nianzqGroup(p0zhouqxh, maxNianzqMap.get(maoxqhzpc.getUsercenter()), pianysj + x).get(x);
					CommonFun.logger.debug("非PP模式预告计算周期：zhouqi="+zhouqi);
					Gongyzq gongyzq = this.gongyzqservice.queryGongyzq(zhouqi);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					yugzjb.setP0fyzqxh(fayzq);
					yugzjb.setZqxh(zhouqi);
					try {
						yugzjb.setYaohqsrq(sdf.parse(gongyzq.getKaissj()));
					} catch (ParseException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,ParseException");
					}// 要货起始日期
					CommonFun.logger.debug("非PP模式预告计算要货起始日期：gongyzq.getKaissj()="+gongyzq.getKaissj());
					try {
						yugzjb.setYaohjsrq(sdf.parse(gongyzq.getJiessj()));
					} catch (ParseException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,ParseException");
					}// 要货结束日期
					CommonFun.logger.debug("非PP模式预告计算要货结束日期：gongyzq.getKaissj()="+gongyzq.getJiessj());
					CommonFun.logger.debug("非PP模式预告计算预告是否取整：maoxqhzpc.getYugsfqz()="+maoxqhzpc.getYugsfqz());
//					if (!maoxqhzpc.getYugsfqz().equalsIgnoreCase(Const.YiLAI)) {
//						yingyl = BigDecimal.ZERO;
//						CommonFun.logger.debug("非PP模式预告计算预告不取整：yingyl="+yingyl);
//					}
					String method = Const.GETP + (pianysj + x);// 拼接getPi方法
					BigDecimal yugao;
					if(pianysj+ x<=Const.ZPCHANGDU){
					CommonFun.logger.debug("非PP模式预告计算反射pc表取值方法：method="+method);
					Class cls = maoxqhzpc.getClass();// 得到Maoxqhzpc类
					Method meth;
					try {
						meth = cls.getMethod(method, new Class[] {});
					} catch (SecurityException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,SecurityException");
					} catch (NoSuchMethodException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,SecurityException");
					}// 得到Maoxqhzpc类的method拼接的方法
					
					try {
						yugao = new BigDecimal(meth.invoke(maoxqhzpc, null).toString());
					} catch (IllegalArgumentException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalArgumentException");
					} catch (IllegalAccessException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalAccessException");
					} catch (InvocationTargetException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,InvocationTargetException");
					}// 执行得到的方法，取得当前周期的数量
					CommonFun.logger.debug("非PP模式预告计算反射pc表取值方法得到值：yugao="+yugao);
					CommonFun.logger.debug("非PP模式预告计算：maoxqhzpc.getGongysfe()="+maoxqhzpc.getGongysfe());
					CommonFun.logger.debug("非PP模式预告计算：maoxqhzpc.getBaozrl()="+maoxqhzpc.getBaozrl());
					}else{
						yugao = BigDecimal.ZERO;
					}
					yaohl = yugao.multiply(maoxqhzpc.getGongysfe());// 毛需求乘以供应商份额
					CommonFun.logger.debug("非PP模式预告计算：yaohl="+yaohl);
					if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
						yaohl = BigDecimal.ZERO;
						CommonFun.logger.debug("非PP模式预告计算要货量小于0时：yaohl="+yaohl);
					}
					yugzjb.setShul(yaohl);
					yugzjb.setDinghcj(maoxqhzpc.getDinghcj());
					yugzjb.setLingjsx(maoxqhzpc.getLingjsx());
					yugzjb.setId(getUUID());
					rsList.add(yugzjb);
//					yugzjbservice.doInsert(yugzjb);
				}
///////////////////wuyichao 修改、、、、、、、、、
		//		this.yugInDingdljPS(maoxqhzpc.getP0zqxh(), maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(),
		//				maoxqhzpc.getCangkdm(), maoxqhzpc.getGongysdm(), dingdlx, maoxqhzpc.getBeihzq(),
		//				maoxqhzpc.getFayzq(), maoxqhzpc.getGongysfe(), maoxqhzpc.getZiyhqrq(), maoxqhzpc.getJihydz(),
		//				maoxqhzpc.getDingdnr(),maoxqhzpc.getGongyhth(),jssj,jihyz,banc,loginUser.getUsername(),maoxqhzpc.getGcbh(),saveLingjMap,saveMingxMap);
///////////////////wuyichao 修改、、、、
				}else{
					String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getGongysdm(),maoxqhzpc.getLingjbh()};
					this.yicbjservice.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str9, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginUser, Const.JISMK_IL_CD);
				}
			}
//////////////////wuyichao		
			if(null != rsList && rsList.size() > 0)
			{
				this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertYugzjb", rsList);
				this.yugInDingdljPP(p0zhouqxh,dingdlx,jssj,jihyz,banc,loginUser.getUsername(),maoxqhzpcMap,saveLingjMap,saveMingxMap);
			}
//////////////////wuyichao				
			//this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il周期订单计算", "非周期模式计算结束");
			CommonFun.logger.debug("非周期模式计算结束!");
		} else if (dingdlx.equalsIgnoreCase(Const.PS)) {
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il周订单计算", "非周期模式计算开始");
			String s0zhouqxh = null;
			Map<String,Maoxqhzsc> maoxqhzscMap = new HashMap<String, Maoxqhzsc>();
			List<Yugzjb> rsList = new ArrayList<Yugzjb>();
			Map<String , String> maxNianzxMap = new HashMap<String, String>();
			Map<String,String> usercenterMap = new HashMap<String, String>();
			Map<String,BigDecimal> maoxqhzscSlMap = new HashMap<String, BigDecimal>();
			Map<String ,CalendarCenter> maoxqhzscCalendarCenterMap = new HashMap<String, CalendarCenter>();
			yugList = this.maoxqhzscService.selectYugao(dingdlx);
			
			
			for(int i=0 ;i< yugList.size() ; i ++)
			{
				Maoxqhzsc maoxqhzsc = (Maoxqhzsc) yugList.get(i);
				if( i == 0 ) 
				{
					s0zhouqxh = maoxqhzsc.getS0zxh();
				}
				if(null != maoxqhzsc.getUsercenter())
					usercenterMap.put(maoxqhzsc.getUsercenter(), maoxqhzsc.getUsercenter());
			}
			Set<String> usercenterSet = usercenterMap.keySet();
			if(StringUtils.isNotBlank(s0zhouqxh))
			{
				for (String string : usercenterSet) {
					getMaxniqnzx(s0zhouqxh,string,maxNianzxMap);
					Map<String,String> zhouxMap = new HashMap<String,String>();
					zhouxMap.put("usercenter", string);
					zhouxMap.put("nianzq", s0zhouqxh.substring(0, 4));
					// 获取到本年内的最大年周序
					CalendarCenter center2 = this.calendarCenterService.maxTime(zhouxMap);
					maoxqhzscCalendarCenterMap.put(string, center2);
				}
			}
			
			
			//CommonFun.objListPrint(yugList, "非PS模式预告计算中间表sc结果list");
			BigDecimal yaohl = BigDecimal.ZERO;// 要货量
			for (int i = 0; i < yugList.size(); i++) {
				Maoxqhzsc maoxqhzsc = (Maoxqhzsc) yugList.get(i);
				String key = maoxqhzsc.getUsercenter() + maoxqhzsc.getLingjbh() + maoxqhzsc.getGongysdm() ;
				maoxqhzscMap.put(key, maoxqhzsc);
				int pianysj = (maoxqhzsc.getBeihzq().add(maoxqhzsc.getFayzq())).divide(new BigDecimal(7), 0,
						BigDecimal.ROUND_UP).intValue();// 运输周期+备货周期向上取整
				if(pianysj<=Const.ZSCHANGDU){
				CommonFun.logger.debug("非PS模式预告计算：pianysj="+pianysj);
				String dingdnr = maoxqhzsc.getDingdnr();
				CommonFun.logger.debug("非PS模式预告计算：dingdnr="+dingdnr);
				Gongyzx gongyzx = this.gongyzxservice.queryGongyzx(maoxqhzsc.getS0zxh());
				for (int x = 0; x < dingdnr.length(); x++) {
					Yugzjb yugzjb = new Yugzjb();
					yugzjb.setUsercenter(maoxqhzsc.getUsercenter());// 用户中心
					yugzjb.setGongysdm(maoxqhzsc.getGongysdm());// 供应商代码
					yugzjb.setGongyslx(maoxqhzsc.getGongyslx());// 供应商类型
					yugzjb.setLingjbh(maoxqhzsc.getLingjbh());// 零件编号
					yugzjb.setCangkdm(maoxqhzsc.getCangkdm());// 仓库代码
					yugzjb.setGonghlx(maoxqhzsc.getWaibghms());// 供货模式
					yugzjb.setDanw(maoxqhzsc.getDanw());// 单位
					yugzjb.setUabzlx(maoxqhzsc.getUabzlx());// ua包装类型
					yugzjb.setUabzuclx(maoxqhzsc.getUabzuclx());// ua中uc包装类型
					yugzjb.setUabzucrl(maoxqhzsc.getUabzucrl());// ua中uc包装的容量
					yugzjb.setUabzucsl(maoxqhzsc.getUabzucsl());// ua中uc的包装数量
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String zhouxuhao = "";
					String fayzq = "";
				//	fayzq = this.calendarCenterService.nianzxGroup(maoxqhzsc.getS0zxh(),pianysj,
				//			maoxqhzsc.getUsercenter()).get(pianysj);
					fayzq = this.calendarCenterService.nianzqGroup(s0zhouqxh, maxNianzxMap.get(maoxqhzsc.getUsercenter()), pianysj).get(pianysj);
					CommonFun.logger.debug("非PS模式预告计算：fayzq="+fayzq);
				//	zhouxuhao = this.calendarCenterService.nianzxGroup(maoxqhzsc.getS0zxh(),pianysj+ x,
				//			maoxqhzsc.getUsercenter()).get(x);
					zhouxuhao = this.calendarCenterService.nianzqGroup(s0zhouqxh, maxNianzxMap.get(maoxqhzsc.getUsercenter()), pianysj + x).get(x);
					CommonFun.logger.debug("非PS模式预告计算：zhouxuhao="+zhouxuhao);
					yugzjb.setP0fyzqxh(fayzq);
					yugzjb.setZqxh(zhouxuhao);
					try {
						yugzjb.setYaohqsrq(sdf.parse(gongyzx.getKaissj()));
					} catch (ParseException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,ParseException");
					}
					CommonFun.logger.debug("非PS模式预告计算：gongyzx.getKaissj()="+gongyzx.getKaissj());
					try {
						yugzjb.setYaohjsrq(sdf.parse(gongyzx.getJiessj()));
					} catch (ParseException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,ParseException");
					}
					CommonFun.logger.debug("非PS模式预告计算：gongyzx.getJiessj()="+gongyzx.getJiessj());
					if (!maoxqhzsc.getYugsfqz().equalsIgnoreCase(Const.YiLAI)) {
						yingyl = BigDecimal.ZERO;
						CommonFun.logger.debug("非PS模式预告计算预告不取整：yingyl="+yingyl);
					}
					
					String method = Const.GETS + (pianysj + x);// 拼接getPi方法
					BigDecimal yugao;
					if(pianysj+ x<=Const.ZSCHANGDU){
					CommonFun.logger.debug("非PS模式预告计算反射sc取值方法：method="+method);
					Class cls = maoxqhzsc.getClass();// 得到Maoxqhzpc类
					Method meth;
					try {
						meth = cls.getMethod(method, new Class[] {});
					} catch (SecurityException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,SecurityException");
					} catch (NoSuchMethodException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,NoSuchMethodException");
					}// 得到Maoxqhzpc类的method拼接的方法
					
					try {
						yugao = new BigDecimal(meth.invoke(maoxqhzsc, null).toString());
					} catch (IllegalArgumentException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalArgumentException");
					} catch (IllegalAccessException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalAccessException");
					} catch (InvocationTargetException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,InvocationTargetException");
					}// 执行得到的方法，取得当前周期的数量
					CommonFun.logger.debug("非PS模式预告计算：yugao="+yugao);
					CommonFun.logger.debug("非PS模式预告计算：maoxqhzsc.getGongysfe()="+maoxqhzsc.getGongysfe());
					CommonFun.logger.debug("非PS模式预告计算：maoxqhzsc.getBaozrl()="+maoxqhzsc.getBaozrl());
					}else{
						yugao = BigDecimal.ZERO;
					}
					yaohl = yugao.multiply(maoxqhzsc.getGongysfe())	;// 毛需求乘以供应商份额
					CommonFun.logger.debug("非PS模式预告计算：yaohl="+yaohl);
					if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
						yaohl = BigDecimal.ZERO;
						CommonFun.logger.debug("非PS模式预告计算要货量小于0时：yaohl="+yaohl);
					}
					yugzjb.setShul(yaohl);
					yugzjb.setDinghcj(maoxqhzsc.getDinghcj());
					yugzjb.setLingjsx(maoxqhzsc.getLingjsx());
					yugzjb.setId(getUUID());
					CommonFun.objPrint(yugzjb, "预告中间表bean");
					rsList.add(yugzjb);
					maoxqhzscSlMap.put(key + "Sl" + x, yugzjb.getShul());
//					yugzjbservice.doInsert(yugzjb);
				}
			//	this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertYugzjb", rsList);
			//	this.yugInDingdljPS(maoxqhzsc.getS0zxh(), maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(),
			//			maoxqhzsc.getCangkdm(), maoxqhzsc.getGongysdm(), dingdlx, maoxqhzsc.getBeihzq(),
			//			maoxqhzsc.getFayzq(), maoxqhzsc.getGongysfe(), maoxqhzsc.getZiyhqrq(), maoxqhzsc.getJihyz(),
			//			maoxqhzsc.getDingdnr(),maoxqhzsc.getGongyhth(),jssj,jihyz,banc,loginUser.getUsername(),maoxqhzsc.getGcbh(),saveLingjMap,saveMingxMap);
				}else{
					String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getGongysdm(),maoxqhzsc.getLingjbh()};
					this.yicbjservice.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str9, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginUser, Const.JISMK_IL_CD);
				}
			}
			if(rsList.size() > 0)
			{
				this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertYugzjb", rsList);
				this.yugInDingdljPS(s0zhouqxh,dingdlx,jssj,jihyz,banc,loginUser.getUsername(),maoxqhzscMap,maoxqhzscCalendarCenterMap,maoxqhzscSlMap,saveLingjMap,saveMingxMap);
			}
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il周订单计算", "非周期计算模式结束");
		} else if (dingdlx.equalsIgnoreCase(Const.PJ)) {
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il日订单计算", "非周期计算模式开始");
			yugList = this.maoxqhzjcService.selectYugao(dingdlx);
			//CommonFun.objListPrint(yugList, "非PJ模式预告计算从中间表jc中查询得到的结果list");
			BigDecimal yaohl = BigDecimal.ZERO;// 要货量
/////////////////////wuyichao////////////////////////////
			List<Yugzjb> rsList = new ArrayList<Yugzjb>();
			Map<String,String> maoxqhzjcRqMap = new HashMap<String, String>();
			Map<String,BigDecimal> maoxqhzjcSlMap = new HashMap<String, BigDecimal>();
			Map<String,Maoxqhzjc> maoxqhzjcMap = new HashMap<String, Maoxqhzjc>();
/////////////////////wuyichao////////////////////////////
			
			for (int i = 0; i < yugList.size(); i++) {
				Maoxqhzjc maoxqhzjc = (Maoxqhzjc) yugList.get(i);
				
				// digDecimal(30)).doubleValue());// 运输周期+备货周期向上取整
				int pianysj = (int) Math.ceil((maoxqhzjc.getBeihzq().add(maoxqhzjc.getFayzq())).divide(BigDecimal.ONE,
						0, BigDecimal.ROUND_UP).doubleValue());
				if(pianysj<=Const.ZJCHANGDU){
///////////////////////////wuyichao////////////
					String key = maoxqhzjc.getUsercenter() + maoxqhzjc.getLingjbh() + maoxqhzjc.getGongysdm() ;
					maoxqhzjcMap.put(key, maoxqhzjc);
///////////////////////////wuyichao////////////
				CommonFun.logger.debug("非PJ模式预告计算：pianysj="+pianysj);
				String dingdnr = maoxqhzjc.getDingdnr();
				CommonFun.logger.debug("非PJ模式预告计算：dingdnr="+dingdnr);
				for (int x = 0; x < Const.DINGDNRCHANGDU; x++) {
					
					Class cls = null;
					Method meth = null;
					String method = Const.GETJ + (pianysj + x);// 拼接getPi方法
					BigDecimal yugao = BigDecimal.ZERO;
					
					CommonFun.logger.debug("非PJ模式预告计算从jc中反射取值方法：method="+method);
					Yugzjb yugzjb = new Yugzjb();
					yugzjb.setUsercenter(maoxqhzjc.getUsercenter());// 用户中心
					yugzjb.setGongysdm(maoxqhzjc.getGongysdm());// 供应商代码
					yugzjb.setGongyslx(maoxqhzjc.getGongyslx());// 供应商类型
					yugzjb.setLingjbh(maoxqhzjc.getLingjbh());// 零件编号
					yugzjb.setCangkdm(maoxqhzjc.getCangkdm());// 仓库代码
					yugzjb.setGonghlx(maoxqhzjc.getWaibghms());// 供货模式
					yugzjb.setDanw(maoxqhzjc.getDanw());// 单位
					yugzjb.setUabzlx(maoxqhzjc.getUabzlx());// ua包装类型
					yugzjb.setUabzuclx(maoxqhzjc.getUabzuclx());// ua中uc包装类型
					yugzjb.setUabzucrl(maoxqhzjc.getUabzucrl());// ua中uc包装的容量
					yugzjb.setUabzucsl(maoxqhzjc.getUabzucsl());// ua中uc的包装数量
					// Gongyzx gongyzx =
					// this.gongyzxservice.queryGongyzx(maoxqhzjc.getJ0rq());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String methodRQ = "";
					String pxrq = "";
					String yaohrq = "";
					if(pianysj+x<=Const.ZJCHANGDU){
					if (pianysj == 0) {
						methodRQ = Const.GETJ + (pianysj + x) + "rq";
						CommonFun.logger.debug("非PJ模式预告计算从jc中反射取值方法：methodRQ="+methodRQ);
					} else {
						methodRQ = Const.GETJ + (pianysj + x) + "riq";
						CommonFun.logger.debug("非PJ模式预告计算从jc中反射取值方法：methodRQ="+methodRQ);
					}

					
					if (pianysj > 15) {
						cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
						try {
							meth = cls.getMethod("getJ15riq", new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,NoSuchMethodException");
						}// 得到Maoxqhzpc类的method拼接的方法
						String riqi;
						try {
							riqi = meth.invoke(maoxqhzjc, null).toString();
						} catch (IllegalArgumentException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalArgumentException");
						} catch (IllegalAccessException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalAccessException");
						} catch (InvocationTargetException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,InvocationTargetException");
						}// 执行得到的方法，取得当前周期的数量

						pxrq = this.calendarcenterservice.workDayGroup(riqi, pianysj - 15, maoxqhzjc.getUsercenter())
								.get(pianysj - 15);
					} else {
						cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
						try {
							meth = cls.getMethod(methodRQ, new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,NoSuchMethodException");
						}// 得到Maoxqhzpc类的method拼接的方法
						try {
							pxrq = meth.invoke(maoxqhzjc, null).toString();
						} catch (IllegalArgumentException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalArgumentException");
						} catch (IllegalAccessException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalAccessException");
						} catch (InvocationTargetException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,InvocationTargetException");
						}// 执行得到的方法，取得当前周期的数量
					}
					CommonFun.logger.debug("非PJ模式预告计算：pxrq="+pxrq);
					yugzjb.setP0fyzqxh(pxrq.substring(0, 10));
					try {
						yaohrq = pxrq.substring(0, 10);
						yugzjb.setYaohqsrq(sdf.parse(yaohrq));
					} catch (ParseException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,ParseException");
					}
					CommonFun.logger.debug("非PJ模式预告计算：pxrq.substring(0, 10)="+pxrq.substring(0, 10));
					try {
						yugzjb.setYaohjsrq(sdf.parse(pxrq.substring(0, 10)));
					} catch (ParseException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("IlOrderService.yuGaoCalculate,ParseException");
					}

					if (!maoxqhzjc.getYugsfqz().equalsIgnoreCase(Const.YiLAI)) {
						yingyl = BigDecimal.ZERO;
					}

					
					if (pianysj > 15) {
						yugao = BigDecimal.ZERO;
					} else {
						cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
						try {
							meth = cls.getMethod(method, new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,NoSuchMethodException");
						}// 得到Maoxqhzpc类的method拼接的方法
						try {
							yugao = new BigDecimal(meth.invoke(maoxqhzjc, null).toString());
						} catch (IllegalArgumentException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalArgumentException");
						} catch (IllegalAccessException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,IllegalAccessException");
						} catch (InvocationTargetException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yuGaoCalculate,InvocationTargetException");
						}// 执行得到的方法，取得当前周期的数量
						
					}
					}
					//如果大于15则放弃
					else
					{
						continue;
					}
					CommonFun.logger.debug("非PJ模式预告计算：yugao="+yugao);
					CommonFun.logger.debug("非PJ模式预告计算：maoxqhzjc.getGongysfe()="+maoxqhzjc.getGongysfe());
					CommonFun.logger.debug("非PJ模式预告计算：maoxqhzjc.getBaozrl()="+maoxqhzjc.getBaozrl());
					yaohl = yugao.multiply(maoxqhzjc.getGongysfe());// 毛需求乘以供应商份额
					CommonFun.logger.debug("非PJ模式预告计算：yaohl="+yaohl);
					if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
						yaohl = BigDecimal.ZERO;
					}
					yugzjb.setShul(yaohl);
					yugzjb.setDinghcj(maoxqhzjc.getDinghcj());
					yugzjb.setLingjsx(maoxqhzjc.getLingjsx());
					CommonFun.objPrint(yugzjb, "预告中间表bean");
////////////////////////////////wuyichao//////////////////
					maoxqhzjcRqMap.put(key + "Rq" + x, yaohrq);
					maoxqhzjcSlMap.put(key + "Sl" + x, yugzjb.getShul());
					yugzjb.setId(getUUID());
					rsList.add(yugzjb);
////////////////////////////////wuyichao//////////////////
//					yugzjbservice.doInsert(yugzjb);
				}
//				Map<String, String> dingdljMap = new HashMap<String, String>();
//				dingdljMap.put("usercenter", maoxqhzjc.getUsercenter());
//				dingdljMap.put("lingjbh", maoxqhzjc.getLingjbh());
//				dingdljMap.put("cangkdm", maoxqhzjc.getCangkdm());
//				dingdljMap.put("gongysdm", maoxqhzjc.getGongysdm());
//				List<String> rqList = this.yugzjbservice.selectRq(dingdljMap);
//				for (int k = 0; k < rqList.size(); k++) {
//					dingdljMap.put("j" + k + "rq", rqList.get(k).substring(0, 10));
//				}
//				this.yugInDingdljPJ(dingdljMap, maoxqhzjc.getBeihzq(), maoxqhzjc.getFayzq(), maoxqhzjc.getGongysfe(),
//						maoxqhzjc.getZiyhqrq(), maoxqhzjc.getJihyz(), maoxqhzjc.getDingdnr(),maoxqhzjc.getGongyhth(),jssj,jihyz,banc,loginUser.getUsername(),maoxqhzjc.getGcbh(),saveLingjMap,saveMingxMap);
				}else{
					String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getGongysdm(),maoxqhzjc.getLingjbh()};
					this.yicbjservice.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str9, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginUser, Const.JISMK_IL_CD);
				}
				}
				
			if(rsList.size() > 0)
			{
				this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertYugzjb", rsList);
				this.yugInDingdljPJ(maoxqhzjcMap ,maoxqhzjcRqMap,maoxqhzjcSlMap ,jssj,jihyz,banc,loginUser.getUsername(),saveLingjMap,saveMingxMap);
			}
			
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il日订单计算", "非周期模式计算结束");
		}
		return yingyl;
	}

	private void yugInDingdljPS(String s0zhouqxh, String dingdlx, String jssj,
			String jihyz, String banc, String username,
			Map<String, Maoxqhzsc> maoxqhzscMap,
			Map<String ,CalendarCenter> maoxqhzscCalendarCenterMap,
			Map<String,BigDecimal> maoxqhzscSlMap,
			Map<String, List<Dingdlj>> saveLingjMap,
			Map<String, List<Dingdmx>> saveMingxMap) {
		List<Dingdlj> ls = this.yugzjbservice.colRowYugzjbPS();
		if (null != ls) {
			List<Dingdmx> dmRsList = new ArrayList<Dingdmx>();
			for (Dingdlj dingdlj : ls) {
				String key = dingdlj.getUsercenter() + dingdlj.getLingjbh() + dingdlj.getGongysdm();
				Maoxqhzsc maoxqhzsc = maoxqhzscMap.get(key);
				if(null == maoxqhzsc)
					continue;
				CalendarCenter center = maoxqhzscCalendarCenterMap.get(dingdlj.getUsercenter());
				int pianysj = (maoxqhzsc.getBeihzq().add(maoxqhzsc.getFayzq())).divide(new BigDecimal(7), 0,
						BigDecimal.ROUND_UP).intValue();
				String flags0zhouqxh = this.calendarCenterService.addNianzqOrNianzx(s0zhouqxh, center.getNianzx().substring(4, center.getNianzx().length()), pianysj);
				dingdlj.setDingdh(this.getOrderNumber(Const.PS, dingdlj.getUsercenter(), dingdlj.getGongysdm(),this.orderNumberMap));// 订单号
				dingdlj.setP0fyzqxh(flags0zhouqxh);
				dingdlj.setBeihzq(maoxqhzsc.getBeihzq());
				dingdlj.setFayzq(maoxqhzsc.getFayzq());
				dingdlj.setGongysfe(maoxqhzsc.getGongysfe());
				dingdlj.setZiyhqrq(maoxqhzsc.getZiyhqrq());
				dingdlj.setJihyz(maoxqhzsc.getJihyz());
				dingdlj.setDingdnr(maoxqhzsc.getDingdnr());
				dingdlj.setActive(Const.ACTIVE_1);
				dingdlj.setCreator(username);
				dingdlj.setDingdzzsj(CommonFun.getJavaTime());
				dingdlj.setCreate_time(CommonFun.getJavaTime());
				dingdlj.setEditor(username);
				dingdlj.setEdit_time(CommonFun.getJavaTime());
				dingdlj.setGonghms(Const.FEIZHOUQIPS);
			   
				for (int x = 0; x < dingdlj.getDingdnr().length(); x++) {
						Dingdmx dingdmx = new Dingdmx();
						
						dingdmx.setUabzlx(dingdlj.getUabzlx());
						dingdmx.setUabzuclx(dingdlj.getUabzuclx());
						dingdmx.setUabzucsl(dingdlj.getUabzucsl());
						dingdmx.setUabzucrl(dingdlj.getUabzucrl());
						dingdmx.setDingdh(dingdlj.getDingdh());
						dingdmx.setUsercenter(dingdlj.getUsercenter());
						dingdmx.setLingjbh(dingdlj.getLingjbh());
						dingdmx.setGongysdm(dingdlj.getGongysdm());
						dingdmx.setGongyslx(dingdlj.getGongyslx());
						dingdmx.setCangkdm(dingdlj.getCangkdm());
						dingdmx.setFahd(dingdlj.getFahd());
						dingdmx.setDinghcj(dingdlj.getDinghcj());
						dingdmx.setDanw(dingdlj.getDanw());
						dingdmx.setLujdm(dingdlj.getLujdm());
						dingdmx.setJihyz(dingdlj.getJihyz());
						dingdmx.setYijfl(dingdlj.getYijfl());
						dingdmx.setZhuangt(Const.DINGD_STATUS_DSX);
						dingdmx.setLeix(Const.SHIFOUSHIYUGAO);
						dingdmx.setGonghlx(Const.FEIZHOUQIPS);
						dingdmx.setActive(Const.ACTIVE_1);
						dingdmx.setCreator(username);
						dingdmx.setGcbh(maoxqhzsc.getGcbh());
						dingdmx.setEditor(username);
						dingdmx.setEdit_time(CommonFun.getJavaTime());
						dingdmx.setCreate_time(CommonFun.getJavaTime());
						Gongyzx gongyzx = this.gongyzqService.queryGongyzx(CommonFun.addNianzq(dingdlj.getP0fyzqxh(),center.getNianzx(), x));
						if (null != gongyzx) {
							// 设置要货开始时间
							dingdmx.setYaohqsrq(gongyzx.getKaissj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式Yaohqsrq="+gongyzx.getKaissj());
							// 设置要货结束时间
							dingdmx.setYaohjsrq(gongyzx.getJiessj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式Yaohjsrq="+gongyzx.getJiessj());
							dingdmx.setJiaofrq(gongyzx.getJiessj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式Jiaofrq="+gongyzx.getJiessj());
							dingdmx.setFayrq(gongyzx.getKaissj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式Fayrq="+gongyzx.getKaissj());
						}
						BigDecimal jissl = maoxqhzscSlMap.get(key + "Sl" + x) == null ? BigDecimal.ZERO : maoxqhzscSlMap.get(key + "Sl" + x);
						switch (x) {
						case 0:
							dingdlj.setP0sl(jissl);
							break;
						case 1:
							dingdlj.setP1sl(jissl);
							break;
						case 2:
							dingdlj.setP2sl(jissl);
							break;
						case 3:
							dingdlj.setP3sl(jissl);
							break;
						}
						dingdmx.setShul(jissl);
						dingdmx.setJissl(jissl);
						CommonFun.objPrint(dingdmx, "行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式dingdmx");
						String flagDingdh = dingdmx.getDingdh();
						List<Dingdmx> flagmx = saveMingxMap.get(flagDingdh);
						if(null == flagmx)
						{
							flagmx = new ArrayList<Dingdmx>();
						}
						flagmx.add(dingdmx);
						saveMingxMap.put(flagDingdh, flagmx);
					}
					String flagDingdh = dingdlj.getDingdh();
					List<Dingdlj> flaglj = saveLingjMap.get(flagDingdh);
					if(null == flaglj)
					{
						flaglj = new ArrayList<Dingdlj>();
					}
					flaglj.add(dingdlj);
					saveLingjMap.put(flagDingdh, flaglj);
			
				Dingd existDd = new Dingd();
				Map<String, String> ddhMap = new HashMap<String, String>();
				ddhMap.put("dingdh", dingdlj.getDingdh());
				existDd = this.dingdservice.queryDingdByDingdh(ddhMap);
				CommonFun.objPrint(existDd, "行列转换，得到订单零件类集合yugInDingdljPS方法中existDd");
				if (null == existDd) {
					Map<String,String> riqMap = new HashMap<String,String>();
					riqMap.put("usercenter", dingdlj.getUsercenter());
					riqMap.put("riq", dingdlj.getZiyhqrq().substring(0,10));
					CalendarCenter centerObject = this.calendarCenterService.queryCalendarCenterObject(riqMap);
					Dingd dd = new Dingd();
					dd.setDingdh(dingdlj.getDingdh());// 订单号
					dd.setHeth(maoxqhzsc.getGongyhth());// 合同号
					dd.setDingdlx(Const.DINGDLX_ILORDER);// 国产订单类型
					dd.setGongysdm(dingdlj.getGongysdm());// 供应商编号
					dd.setChullx(dingdlj.getGonghms());
					dd.setDingdzt(Const.DINGD_STATUS_JSZ);
					dd.setDingdjssj(jssj);
					dd.setDingdnr(dingdlj.getDingdnr());
					dd.setZiyhqrq(dingdlj.getZiyhqrq());
					dd.setFahzq(dingdlj.getP0fyzqxh());
					dd.setMaoxqbc(banc);
					dd.setJislx(jihyz);
					dd.setShifyjsyhl(Const.SHIFYJSYHL_Y);
					dd.setShifzfsyhl(Const.SHIFZFSYHL_YES);
					dd.setCreator(username);
					dd.setCreate_time(CommonFun.getJavaTime());
					dd.setActive(Const.ACTIVE_0);
					dd.setUsercenter(dingdlj.getUsercenter());
					dd.setJiszq(centerObject.getNianzq());
					CommonFun.objPrint(existDd, "行列转换，得到订单零件类集合yugInDingdljPS方法中dd");
					this.dingdservice.doInsert(dd);
				}
			}
		}
	}

	private void yugInDingdljPJ(Map<String, Maoxqhzjc> maoxqhzjcMap,
			Map<String, String> maoxqhzjcRqMap,
			Map<String, BigDecimal> maoxqhzjcSlMap, String jssj, String jihyz,
			String banc, String username,
			Map<String, List<Dingdlj>> saveLingjMap,
			Map<String, List<Dingdmx>> saveMingxMap) {
		List<Dingdlj> ls = this.yugzjbservice.colRowYugzjbJ();
		//CommonFun.objListPrint(ls, "行列转换，得到订单零件类集合yugInDingdljPJ方法中ls");
		if (null != ls) {
			for (Dingdlj dingdlj : ls) {
				String key = dingdlj.getUsercenter() + dingdlj.getLingjbh() + dingdlj.getGongysdm();
				Maoxqhzjc maoxqhzjc = maoxqhzjcMap.get(key);
				if(null == maoxqhzjc)
					continue;
				
				dingdlj.setDingdh(this.getOrderNumber(Const.PJ, dingdlj.getUsercenter(), dingdlj.getGongysdm(),this.orderNumberMap));// 订单号
				dingdlj.setGonghms(Const.FEIZHOUQIPJ);
				dingdlj.setBeihzq(maoxqhzjc.getBeihzq());
				dingdlj.setFayzq(maoxqhzjc.getFayzq());
				dingdlj.setGongysfe(maoxqhzjc.getGongysfe());
				dingdlj.setZiyhqrq(maoxqhzjc.getZiyhqrq());
				dingdlj.setJihyz(maoxqhzjc.getJihyz());
				dingdlj.setDingdnr(maoxqhzjc.getDingdnr());
				dingdlj.setActive(Const.ACTIVE_1);
				dingdlj.setCreator(username);
				dingdlj.setP0fyzqxh(maoxqhzjcRqMap.get(key + "Rq0"));
				dingdlj.setCreate_time(CommonFun.getJavaTime());
				dingdlj.setEditor(username);
				dingdlj.setEdit_time(CommonFun.getJavaTime());
				CommonFun.objPrint(dingdlj, "行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式dingdlj");
				//this.dingdljservice.doInsert(dingdlj);
				for (int x = 0; x < Const.DINGDNRCHANGDU; x++) {
					Dingdmx dingdmx = new Dingdmx();
					
					String pxriq = maoxqhzjcRqMap.get(key + "Rq" + x);
					BigDecimal jissl = maoxqhzjcSlMap.get(key + "Sl" + x) == null ? BigDecimal.ZERO : maoxqhzjcSlMap.get(key + "Sl" + x);
					if(StringUtils.isBlank(pxriq))
						continue;
					switch (x) {
					case 0:
						dingdlj.setP0fyzqxh(pxriq);
						dingdlj.setP0sl(jissl);
						break;
					case 1:
						dingdlj.setP1rq(pxriq);
						dingdlj.setP1sl(jissl);
						break;
					case 2:
						dingdlj.setP2rq(pxriq);
						dingdlj.setP2sl(jissl);
						break;
					case 3:
						dingdlj.setP3rq(pxriq);
						dingdlj.setP3sl(jissl);
						break;
					case 4:
						dingdlj.setP4rq(pxriq);
						dingdlj.setP4sl(jissl);
						break;
					case 5:
						dingdlj.setP5rq(pxriq);
						dingdlj.setP5sl(jissl);
						break;
					case 6:
						dingdlj.setP6rq(pxriq);
						dingdlj.setP6sl(jissl);
						break;
					case 7:
						dingdlj.setP7rq(pxriq);
						dingdlj.setP7sl(jissl);
						break;
					case 8:
						dingdlj.setP8rq(pxriq);
						dingdlj.setP8sl(jissl);
						break;
					case 9:
						dingdlj.setP9rq(pxriq);
						dingdlj.setP9sl(jissl);
						break;
					}
					dingdmx.setUabzlx(dingdlj.getUabzlx());
					dingdmx.setUabzuclx(dingdlj.getUabzuclx());
					dingdmx.setUabzucsl(dingdlj.getUabzucsl());
					dingdmx.setUabzucrl(dingdlj.getUabzucrl());
					dingdmx.setDingdh(dingdlj.getDingdh());
					dingdmx.setUsercenter(dingdlj.getUsercenter());
					dingdmx.setLingjbh(dingdlj.getLingjbh());
					dingdmx.setGongysdm(dingdlj.getGongysdm());
					dingdmx.setGongyslx(dingdlj.getGongyslx());
					dingdmx.setCangkdm(dingdlj.getCangkdm());
					dingdmx.setFahd(dingdlj.getFahd());
					dingdmx.setDinghcj(dingdlj.getDinghcj());
					dingdmx.setDanw(dingdlj.getDanw());
					dingdmx.setLujdm(dingdlj.getLujdm());
					dingdmx.setJihyz(dingdlj.getJihyz());
					dingdmx.setYijfl(dingdlj.getYijfl());
					/////dingdmx.setZhuangt(Const.DINGD_STATUS_DSX);
					////订单明细修改为已生效
					dingdmx.setZhuangt(Const.DINGD_STATUS_YSX);
					dingdmx.setLeix(Const.SHIFOUSHIYUGAO);
					dingdmx.setGonghlx(Const.FEIZHOUQIPJ);
					dingdmx.setActive(Const.ACTIVE_1);
					// 设置要货开始时间
					dingdmx.setYaohqsrq(pxriq);
					// 设置要货结束时间
					dingdmx.setYaohjsrq(pxriq);
					dingdmx.setJiaofrq(pxriq);

					dingdmx.setFayrq(pxriq);
					dingdmx.setShul(jissl);
					dingdmx.setJissl(jissl);
					dingdmx.setCreator(username);
					dingdmx.setGcbh(maoxqhzjc.getGcbh());
					dingdmx.setEdit_time(CommonFun.getJavaTime());
					dingdmx.setEditor(username);
					dingdmx.setCreate_time(CommonFun.getJavaTime());
					//CommonFun.objPrint(dingdmx, "行列转换，得到订单零件类集合yugInDingdljPJ方法中dingdmx");
/////////////////////////////////wuyichao 					
					List<Dingdmx> flagMx = saveMingxMap.get(dingdmx.getDingdh());
					if(null == flagMx)
					{
						flagMx = new ArrayList<Dingdmx>();
					}
					flagMx.add(dingdmx);
					saveMingxMap.put(dingdmx.getDingdh(), flagMx);
////////////////////////////wuyichao
				}
////////////////////////////wuyichao
				List<Dingdlj> flagLj = saveLingjMap.get(dingdlj.getDingdh());
				if(null == flagLj)
				{
					flagLj = new ArrayList<Dingdlj>();
				}
				flagLj.add(dingdlj);
				saveLingjMap.put(dingdlj.getDingdh(), flagLj);
////////////////////////////wuyichao
				Dingd existDd = new Dingd();
				Map<String, String> ddhMap = new HashMap<String, String>();
				ddhMap.put("dingdh", dingdlj.getDingdh());
				existDd = this.dingdservice.queryDingdByDingdh(ddhMap);
				CommonFun.objPrint(existDd, "行列转换，得到订单零件类集合yugInDingdljPJ方法中existDd");
				if (null == existDd) {
					
					Map<String,String> riqMap = new HashMap<String,String>();
					riqMap.put("usercenter", dingdlj.getUsercenter());
					riqMap.put("riq", dingdlj.getZiyhqrq().substring(0,10));
					CalendarCenter centerObject = this.calendarCenterService.queryCalendarCenterObject(riqMap);
					Dingd dd = new Dingd();
					dd.setDingdh(dingdlj.getDingdh());// 订单号
					dd.setHeth(maoxqhzjc.getGongyhth());// 合同号
					dd.setDingdlx(Const.DINGDLX_ILORDER);// 国产订单类型
					dd.setGongysdm(dingdlj.getGongysdm());// 供应商编号
					dd.setChullx(dingdlj.getGonghms());
					dd.setDingdzt(Const.DINGD_STATUS_JSZ);
					dd.setDingdjssj(jssj);
					dd.setDingdnr(dingdlj.getDingdnr());
					dd.setZiyhqrq(dingdlj.getZiyhqrq());
					dd.setFahzq(dingdlj.getP0fyzqxh());
					dd.setMaoxqbc(banc);
					dd.setJislx(jihyz);
					dd.setShifyjsyhl(Const.SHIFYJSYHL_Y);
					dd.setShifzfsyhl(Const.SHIFZFSYHL_YES);
					dd.setCreator(username);
					dd.setCreate_time(CommonFun.getJavaTime());
					dd.setActive(Const.ACTIVE_0);
					dd.setUsercenter(dingdlj.getUsercenter());
					dd.setJiszq(centerObject.getNianzq());
					CommonFun.objPrint(dd, "行列转换，得到订单零件类集合yugInDingdljPJ方法中dd");
					this.dingdservice.doInsert(dd);
				}
			}
		}
	}

	/**
	 * @see   批量根据预告信息，生成订单
	 * @param jssj
	 * @param jihyz
	 * @param banc
	 * @param username
	 * @param maoxqhzpcMap
	 * @param saveLingjMap
	 * @param saveMingxMap
	 */
	private void yugInDingdljPP(String p0zhouqxh,String dingdlx,String jssj, String jihyz, String banc,
			String username, Map<String, Maoxqhzpc> maoxqhzpcMap,
			Map<String, List<Dingdlj>> saveLingjMap,
			Map<String, List<Dingdmx>> saveMingxMap) {
		String gongyshth = ""; 
		String gcbh = ""; 
		Map<String, String> map = new HashMap<String, String>();
		List<Dingdlj> ls = new ArrayList<Dingdlj>();
		if(dingdlx.equals(Const.PP)){
			map.put("nianzq", p0zhouqxh);
			ls = this.yugzjbservice.colRowYugzjbPP(map);
		}
		
		if (null != ls) {
//			List<Dingd> ddRsList = new ArrayList<Dingd>();
//			List<Dingdlj> djRsList = new ArrayList<Dingdlj>();
			List<Dingdmx> dmRsList = new ArrayList<Dingdmx>();
			for (Dingdlj dingdlj : ls) {
				if(dingdlx.equals(Const.PP)){
					dingdlj.setDingdh(this.getOrderNumber(Const.PP, dingdlj.getUsercenter(), dingdlj.getGongysdm(),this.orderNumberMap));// 订单号
					CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中PP模式dingdlj.setDingdh="+dingdlj.getDingdh());
				}
				Maoxqhzpc maoxqhzpc = maoxqhzpcMap.get(dingdlj.getUsercenter() + dingdlj.getLingjbh() +  dingdlj.getGongysdm());
				if(null != maoxqhzpc)
				{
					dingdlj.setBeihzq(maoxqhzpc.getBeihzq());
					dingdlj.setFayzq(maoxqhzpc.getFayzq());
					dingdlj.setGongysfe(maoxqhzpc.getGongysfe());
					dingdlj.setZiyhqrq( maoxqhzpc.getZiyhqrq());
					dingdlj.setJihyz( maoxqhzpc.getJihydz());
					dingdlj.setDingdnr(maoxqhzpc.getDingdnr());
					dingdlj.setCreator(username);
					dingdlj.setEditor(username);
					gcbh = maoxqhzpc.getGcbh();
					gongyshth = maoxqhzpc.getGongyhth();
					
				}
				dingdlj.setActive(Const.ACTIVE_1);
				dingdlj.setDingdzzsj(CommonFun.getJavaTime());
				dingdlj.setCreate_time(CommonFun.getJavaTime());
				dingdlj.setEdit_time(CommonFun.getJavaTime());
				if (dingdlx.equals(Const.PP)) {
					dingdlj.setGonghms(Const.FEIZHOUQIPP);
					for (int x = 0; x < dingdlj.getDingdnr().length(); x++) {
						Dingdmx dingdmx = new Dingdmx();
						String methodPd = Const.GETP + (x) + "sl";
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式methodPd="+methodPd);
						Class clsdd = dingdlj.getClass();
						Method methdPd;
						try {
							methdPd = clsdd.getMethod(methodPd, new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,NoSuchMethodException");
						}
						BigDecimal jissl;
						try {
							jissl = new BigDecimal(methdPd.invoke(dingdlj, null).toString());
						} catch (IllegalArgumentException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,IllegalArgumentException");
						} catch (IllegalAccessException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,IllegalAccessException");
						} catch (InvocationTargetException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,InvocationTargetException");
						}
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式jissl="+jissl);
						dingdmx.setUabzlx(dingdlj.getUabzlx());
						dingdmx.setUabzuclx(dingdlj.getUabzuclx());
						dingdmx.setUabzucsl(dingdlj.getUabzucsl());
						dingdmx.setUabzucrl(dingdlj.getUabzucrl());
						dingdmx.setDingdh(dingdlj.getDingdh());
						dingdmx.setUsercenter(dingdlj.getUsercenter());
						dingdmx.setLingjbh(dingdlj.getLingjbh());
						dingdmx.setGongysdm(dingdlj.getGongysdm());
						dingdmx.setGongyslx(dingdlj.getGongyslx());
						dingdmx.setCangkdm(dingdlj.getCangkdm());
						dingdmx.setFahd(dingdlj.getFahd());
						dingdmx.setDinghcj(dingdlj.getDinghcj());
						dingdmx.setDanw(dingdlj.getDanw());
						dingdmx.setLujdm(dingdlj.getLujdm());
						dingdmx.setJihyz(dingdlj.getJihyz());
						dingdmx.setYijfl(dingdlj.getYijfl());
						dingdmx.setZhuangt(Const.DINGD_STATUS_DSX);
						dingdmx.setLeix(Const.SHIFOUSHIYUGAO);
						dingdmx.setGonghlx(Const.FEIZHOUQIPP);
						dingdmx.setActive(Const.ACTIVE_1);
						dingdmx.setCreator(username);
						dingdmx.setGcbh(gcbh);
						dingdmx.setEditor(username);
						dingdmx.setEdit_time(CommonFun.getJavaTime());
						dingdmx.setCreate_time(CommonFun.getJavaTime());
						Gongyzq gongyzq = this.gongyzqService.queryGongyzq(CommonFun.addNianzq(dingdlj.getP0fyzqxh(),Const.MAXZQ, x));
						if (null != gongyzq) {
							// 设置要货开始时间
							dingdmx.setYaohqsrq(gongyzq.getKaissj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式Yaohqsrq="+gongyzq.getKaissj());
							// 设置要货结束时间
							dingdmx.setYaohjsrq(gongyzq.getJiessj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式Yaohjsrq="+gongyzq.getJiessj());
							dingdmx.setJiaofrq(gongyzq.getJiessj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式Jiaofrq="+gongyzq.getJiessj());
							dingdmx.setFayrq(gongyzq.getKaissj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式Fayrq="+gongyzq.getKaissj());
						}

						dingdmx.setShul(jissl);
						dingdmx.setJissl(jissl);
						//CommonFun.objPrint(dingdmx, "行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式dingdmx");
						String key = dingdmx.getDingdh();
						List<Dingdmx> flagmx = saveMingxMap.get(key);
						if(null == flagmx)
						{
							flagmx = new ArrayList<Dingdmx>();
						}
						flagmx.add(dingdmx);
						saveMingxMap.put(key, flagmx);
					}
				} 
				String key = dingdlj.getDingdh();
				List<Dingdlj> flaglj = saveLingjMap.get(key);
				if(null == flaglj)
				{
					flaglj = new ArrayList<Dingdlj>();
				}
				flaglj.add(dingdlj);
				saveLingjMap.put(key, flaglj);
				Dingd existDd = new Dingd();
				Map<String, String> ddhMap = new HashMap<String, String>();
				ddhMap.put("dingdh", dingdlj.getDingdh());
				existDd = this.dingdservice.queryDingdByDingdh(ddhMap);
				CommonFun.objPrint(existDd, "行列转换，得到订单零件类集合yugInDingdljPS方法中existDd");
				if (null == existDd) {
					Map<String,String> riqMap = new HashMap<String,String>();
					riqMap.put("usercenter", dingdlj.getUsercenter());
					riqMap.put("riq", dingdlj.getZiyhqrq().substring(0,10));
					CalendarCenter centerObject = this.calendarCenterService.queryCalendarCenterObject(riqMap);
					Dingd dd = new Dingd();
					dd.setDingdh(dingdlj.getDingdh());// 订单号
					dd.setHeth(gongyshth);// 合同号
					dd.setDingdlx(Const.DINGDLX_ILORDER);// 国产订单类型
					dd.setGongysdm(dingdlj.getGongysdm());// 供应商编号
					dd.setChullx(dingdlj.getGonghms());
					dd.setDingdzt(Const.DINGD_STATUS_JSZ);
					dd.setDingdjssj(jssj);
					dd.setDingdnr(dingdlj.getDingdnr());
					dd.setZiyhqrq(dingdlj.getZiyhqrq());
					dd.setFahzq(dingdlj.getP0fyzqxh());
					dd.setMaoxqbc(banc);
					dd.setJislx(jihyz);
					dd.setShifyjsyhl(Const.SHIFYJSYHL_Y);
					dd.setShifzfsyhl(Const.SHIFZFSYHL_YES);
					dd.setCreator(username);
					dd.setCreate_time(CommonFun.getJavaTime());
					dd.setActive(Const.ACTIVE_0);
					dd.setUsercenter(dingdlj.getUsercenter());
					dd.setJiszq(centerObject.getNianzq());
					CommonFun.objPrint(existDd, "行列转换，得到订单零件类集合yugInDingdljPS方法中dd");
					this.dingdservice.doInsert(dd);
				}
			}
		}
	}

	/**
	 * 行列转换
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-1
	 * @参数说明：String pattern，需求类型；String riq，资源获取日期;String[] usercenter
	 *              用户中心数组,String[] banc 需求版次，String zhizlx 制造路线
	 */
	public void conversionColRow(String[] bancs, String parrten, String riq, String[] usercenters, String zhizlx,
			List<Dingd> dingdList)  {
		CommonFun.logger.info("Il订单计算模式为"+parrten+"的行列转换开始");
		long start = System.currentTimeMillis();
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "conversionColRow----行列转换开始");
		// 存放查询参数的map
		Map<String, String> map = new HashMap<String, String>();
		// 取出数组内的空值和重复的值
		String[] banc = CommonFun.getArray(bancs);
		String[] usercenter = CommonFun.getArray(usercenters);
		// 循环版次，一个版次对应多个用户中心
		for (int i = 0; i < banc.length; i++) {
			// 迭代用户中心
			for (String user : usercenter) {
				map.put("usercenter", user);
				map.put("riq", riq);
				CommonFun.logger.debug("Il订单计算模式为"+parrten+"的行列循环中，第"+i+"版次的版次号为："+banc[i]+"用户中心为："+user+"资源获取日期为："+riq);
				CalendarCenter calendarcenter = this.calendarCenterService.queryCalendarCenterObject(map);
				CommonFun.objPrint(calendarcenter, "返回的中心日历实体calendarcenter");
				map.clear();
				if (null != calendarcenter) {
					// 获取到年周期
					String nianzq = "";
					CommonFun.logger.debug("Il订单计算模式为"+parrten+"的行列循环中订货路线为"+zhizlx);
					if (zhizlx.equals(Const.ZHIZAOLUXIAN_IL)) {
						nianzq = calendarcenter.getNianzq();
						CommonFun.logger.debug("Il订单计算模式为"+parrten+"的行列循环中国产件年周期为："+nianzq);
					} else if (zhizlx.equals(Const.ZHIZAOLUXIAN_GL)) {
						map.put("dingdh", dingdList.get(0).getDingdh());
						Dingd dd = this.dingdservice.queryDingdByDingdh(map);
						map.clear();
						nianzq = dd.getJiszq();
						CommonFun.logger.debug("Il订单计算模式为"+parrten+"的行列循环中卷料年周期为："+nianzq);
					}
					// 获取到年周序
					String nianzx = calendarcenter.getNianzx();
					CommonFun.logger.debug("Il订单计算模式为"+parrten+"的行列循环中年周序为："+nianzx);
					Object obj = null;
					
					if (parrten.equalsIgnoreCase(Const.PP)) {
						List allList = new ArrayList();
						// 调用maoxqhzpservice，根据年周期查找，返回一个list集合
						allList = this.maoxqhzpservice.queryAllMaoxqhzp(nianzq, user, banc[i], zhizlx);
						CommonFun.listPrint(allList, "Il订单计算模式为"+parrten+"的行列循环中周期模式汇总后的：allList");
						obj = new Maoxqhzp();
						if (!allList.isEmpty()) {
							this.maoxqmxService.listInsertForIL(allList, obj, parrten.toUpperCase());
						}
					} else if (parrten.equalsIgnoreCase(Const.PS)) {
						List allList = new ArrayList();
						map.put("usercenter", user);
						map.put("riq", riq);
						CalendarCenter centerObject = this.calendarCenterService.queryCalendarCenterObject(map);
						map.clear();
						if (null != centerObject) {
							map.put("usercenter", user);
							map.put("nianzq", centerObject.getNianzx().substring(0, 4));
							// 取得最大年周序
							CalendarCenter centerMax = this.calendarCenterService.maxTime(map);
							
							map.clear();
							CommonFun.logger.debug("Il订单计算模式为"+parrten+"的行列循环中MaoxqmxService.getZhoux方法参数为："+centerObject.getNianzx()+"以及"+centerMax.getNianzx());
							Map<String, String> maps = MaoxqmxService.getZhoux(centerObject.getNianzx(), 10,
									centerMax.getNianzx());
							
							maps.put("usercenter", user);
							maps.put("xuqbc", banc[i]);
							maps.put("zhizlx", zhizlx);
							maps.put("nianzx", nianzx);
							maps.put("riq", riq);
							CommonFun.mapPrint(maps, "Il订单计算模式为"+parrten+"的行列循环中周模式汇总的参数map：maps");
							allList = this.maoxqhzsservice.queryMaoxqhzs(maps);
							CommonFun.listPrint(allList, "Il订单计算模式为"+parrten+"的行列循环中周模式汇总后的：allList");
							map.clear();
							obj = new Maoxqhzs();
							if (!allList.isEmpty()) {
								this.maoxqmxService.listInsertForIL(allList, obj, parrten.toUpperCase());
							}
						}
						// 传入的参数是j模式
					} else if (parrten.equalsIgnoreCase(Const.PJ)) {
						List allList = new ArrayList();
						List<String> chanxList = this.maoxqmxService.getChanx(user, banc[i],zhizlx);
						for(int x = 0;x<chanxList.size();x++){
							Map<String, String> inMap = this.maoxqmxService.getriq(chanxList.get(x),user, riq);
							if (null != inMap && !inMap.isEmpty()) {
								inMap.put("j0riq", riq);
								inMap.put("zhizlx", zhizlx);
								inMap.put("usercenter", user);
								inMap.put("xuqbc", banc[i]);
								inMap.put("chanx", chanxList.get(x));
								CommonFun.mapPrint(inMap, "Il订单计算模式为"+parrten+"的行列循环中周模式汇总的参数map：inMap");
								allList = this.maoxqhzjservice.queryMaoxqhzj(inMap);
								if (!allList.isEmpty()) {
									this.maoxqmxService.listInsertForIL(allList, obj, parrten.toUpperCase());
								}
							}
						}
						//NJ订单需求汇总为按中心聚合需求
						Map<String, String> inNjMap = this.maoxqmxService.getriqByCenter(user, riq);
						if (null != inNjMap && !inNjMap.isEmpty()) 
						{
							inNjMap.put("j0riq", riq);
							inNjMap.put("zhizlx", zhizlx);
							inNjMap.put("usercenter", user);
							inNjMap.put("xuqbc", banc[i]);
							allList = this.maoxqhzjservice.queryMaoxqhzjByCenter(inNjMap);
							if (!allList.isEmpty()) {
								this.maoxqmxService.listInsertForIL(allList, obj, parrten.toUpperCase());
							}
						}
						//NJ订单需求汇总为按中心聚合需求
						// 调用maoxqhzjservice，根据日期查找，返回一个list集合
						obj = new Maoxqhzj();
					}
					
				}
			}
		}
		long end = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,行列转换时间"+ (end - start)/1000);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "conversionColRow----行列转换结束");
	}

	public void maoxqhzck() {

	}

	/*
	 * 将毛需求汇总到分配循环并把数据插入到毛需求分配循环表中
	 * 
	 * @author 陈骏
	 * 
	 * @date 2011-12-13
	 */
	public void maoxqhzfpxh(String dingdlx) {
		if (dingdlx.equalsIgnoreCase(Const.PP)) {
			List<Maoxqhzpfpxh> list = this.maoxqhzpfpxhservice.selectAll();// 把毛需求汇总周期表的需求汇总到分配循环
			for (Maoxqhzpfpxh maoxqhzpfpxh : list) {
				this.maoxqhzpfpxhservice.doInsert(maoxqhzpfpxh);// 把汇总得到的数据插入到周期毛需求分配循环表中
			}
		} else if (dingdlx.equalsIgnoreCase(Const.PS)) {
			List<Maoxqhzsfpxh> list = this.maoxqhzsfpxhservice.selectAll();// 把毛需求汇总周表的需求汇总到分配循环
			for (Maoxqhzsfpxh maoxqhzsfpxh : list) {
				this.maoxqhzsfpxhservice.doInsert(maoxqhzsfpxh);// 把汇总得到的数据插入到周毛需求分配循环表中
			}
		} else if (dingdlx.equalsIgnoreCase(Const.PJ)) {
			List<Maoxqhzjfpxh> list = this.maoxqhzjfpxhservice.selectAll();// 把毛需求汇总日表的需求汇总到分配循环
			for (Maoxqhzjfpxh maoxqhzjfpxh : list) {
				this.maoxqhzjfpxhservice.doInsert(maoxqhzjfpxh);// 把汇总得到的数据插入到日毛需求分配循环表中
			}
		}
	}

	/*
	 * 将毛需求分配循环表中的毛需求汇总到仓库并关联相关参考系然后插入到毛需求汇总参考系表中
	 * 
	 * @author 陈骏 参数为资源获取日期
	 * 
	 * @date 2011-12-14
	 */
	public String maoxqhzglckx(String ziyhqrq, String dingdlx, String jihyz, String zhizlx, List<Dingd> dingdList,LoginUser loginuser) 
			 {
		long start = System.currentTimeMillis();
		String message = null;
		
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il周期订单计算", "毛需求关联参考系开始");
		if (dingdlx.equalsIgnoreCase(Const.PP)) {
			this.ppCheckWullj(loginuser);
			message = this.ppMaoxqHz(message, ziyhqrq, zhizlx, dingdList,jihyz,loginuser);
		} else if (dingdlx.equalsIgnoreCase(Const.PS)) {
			this.psCheckWullj(loginuser);
			message = this.psMaoxqHz(message, ziyhqrq, zhizlx, dingdList,jihyz,loginuser);
		} else if (dingdlx.equalsIgnoreCase(Const.PJ)) {
			this.pjCheckWullj(loginuser);
			message= this.pjMaoxqHz(message, ziyhqrq, zhizlx, dingdList,jihyz,loginuser);
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il日订单计算", "毛需求关联参考系结束");
		long end = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,计算时间"+ (end - start)/1000);
		return message;
	}

	private void ppCheckWullj(LoginUser loginuser) 
	{
		try
		{
			List<Yicbj> yicbjList = new ArrayList();
			List<Maoxqhzpc> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.ppCheckMaoxqhzpcWullj");
			if(null != list && list.size() > 0)
			{
				for (Maoxqhzpc maoxqhzpc : list) 
				{
					String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),maoxqhzpc.getZhizlx()};
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str65, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
					maoxqhzpservice.deleteMaoxqhzpById(maoxqhzpc.getId());
				}
			}
			if(yicbjList.size()>0)
			{
				this.yicbjservice.insertAll(yicbjList);
			}
		}
		catch (Exception e) 
		{
			CommonFun.logger.error(e);
		}
	}

	private void psCheckWullj(LoginUser loginuser) 
	{
		try
		{
			List<Yicbj> yicbjList = new ArrayList();
			List<Maoxqhzsc> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.psCheckMaoxqhzscWullj");
			if(null != list && list.size() > 0)
			{
				for (Maoxqhzsc maoxqhzsc : list) 
				{
					String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),maoxqhzsc.getZhizlx()};
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str65, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
					maoxqhzsservice.deleteMaoxqhzsById(maoxqhzsc.getId());
				}
			}
			if(yicbjList.size()>0)
			{
				this.yicbjservice.insertAll(yicbjList);
			}
		}
		catch (Exception e) 
		{
			CommonFun.logger.error(e);
		}
	}

	private void pjCheckWullj(LoginUser loginuser) 
	{
		try
		{
			List<Yicbj> yicbjList = new ArrayList();
			List<Maoxqhzjc> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.pjCheckMaoxqhzjcWullj");
			if(null != list && list.size() > 0)
			{
				for (Maoxqhzjc maoxqhzjc : list) 
				{
					String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),maoxqhzjc.getZhizlx()};
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str65, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
					maoxqhzjservice.deleteMaoxqhzjById(maoxqhzjc.getId());
				}
			}
			if(yicbjList.size()>0)
			{
				this.yicbjservice.insertAll(yicbjList);
			}
		}
		catch (Exception e) 
		{
			CommonFun.logger.error(e);
		}
	}

	/**
	 * 份额计算
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09
	 * @参数说明：Dingdlj bean 订单零件实体，BigDecimal zongyhl 总要货量
	 */
	public Map<String, BigDecimal> gongysFeneJs(Dingdlj bean, BigDecimal zongyhl) throws ServiceException {
		//this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "gongysFeneJs----份额计算开始");
		CommonFun.logger.debug("Il订单计算,份额计算开始");
		// 存放结果map
		Map<String, BigDecimal> valueMap = new HashMap<String, BigDecimal>();
		if (null != bean) {
			// 获取到对象零件flag
			String ljFlag = bean.getUsercenter() + bean.getLingjbh();
			// 获取到对象仓库flag
			String ckFlag = bean.getCangkdm();
			// 份额
			BigDecimal fene = bean.getGongysfe();
			// 供应商已要货数量
			BigDecimal gongysyyhl = BigDecimal.ZERO;
			// 定义总要货量
			BigDecimal zyhl = BigDecimal.ZERO;
			// 要货量
			BigDecimal p0 = BigDecimal.ZERO;
			// 周期要货量
			BigDecimal zhouqyhl = zongyhl;
			// 最后要货量
			BigDecimal yaohl = BigDecimal.ZERO;
			//
			BigDecimal zero = BigDecimal.ZERO;
			// 判断是否是第一次计算，当flag都为空的时候，为第一次计算
			if (flagCangku == null || flagCangku.equals("") || flagLingj == null || flagLingj.equals("")) {
				cangkMap = this.getValue(bean, fene, zyhl, zhouqyhl, gongysyyhl);
				yaohl = cangkMap.get(bean.getGongysdm());
				// 将得到的结果存放到map中(分配后的要货量和盈余量)
				valueMap = this.quzheng(yaohl, bean.getZhizlx(), bean.getUabzucrl().multiply(bean.getUabzucsl()),
						bean.getZuixqdl());
				// 赋给零件标志新值
				flagLingj = bean.getUsercenter() + bean.getLingjbh();
				// 赋给仓库标志新值
				flagCangku = bean.getCangkdm();

			} else if (flagCangku != null && flagLingj != null) {// 当flag不为空的时候
				// 当标记没有发生变化的时候
				if (flagLingj.equals(ljFlag) && flagCangku.equals(ckFlag)) {// 标志没有发生变化
					// 没有指定供应商
					if (null == bean.getZhidgys()) {
						// 从公式得到要货量
						p0 = (zyhl.add(zhouqyhl)).multiply(fene).subtract(gongysyyhl);
						// 指定供应商的情况
					} else {
						// 指定供应商一致的情况，将份额是为100%
						if (bean.getGongysdm().equalsIgnoreCase(bean.getZhidgys())) {
							p0 = (zyhl.add(zhouqyhl)).subtract(gongysyyhl);
							// 指定供应商不一致的情况
						} else {
							p0 = zero;
						}
					}
					// 得带的结果为负数的时间，将要货量置为0
					if (p0.intValue() < 0) {
						p0 = zero;
						// 总map中不能大于周期要货量，当大于周期要货量的时候，需要对其减去多余的部分；
					} else if (p0.subtract(zhouqyhl).intValue() > 0) {
						p0 = zhouqyhl;
					}
					if (null == cangkMap.get(bean.getGongysdm())) {
						cangkMap = this.getValue(bean, fene, zyhl, zhouqyhl, gongysyyhl);
					}
					yaohl = cangkMap.get(bean.getGongysdm());

					// 将得到的结果存放到map中(分配后的要货量和盈余量)
					valueMap = this.quzheng(yaohl, bean.getZhizlx(), bean.getUabzucrl().multiply(bean.getUabzucsl()),
							bean.getZuixqdl());
					// 赋给零件标志新值
					flagLingj = bean.getUsercenter() + bean.getLingjbh();
					// 赋给仓库标志新值
					flagCangku = bean.getCangkdm();
				} else if (!flagLingj.equals(ljFlag)) {// 零件标志发生变化的时候，需要清空两个map
					// 清空仓库map
					cangkMap.clear();
					// 清空总map
					zongMap.clear();
					// 从公式得到要货量
					p0 = (zyhl.add(zhouqyhl)).multiply(fene).subtract(gongysyyhl);
					// 将要货量存放到仓库map
					cangkMap.put(bean.getGongysdm(), this.checkZero(p0, zhouqyhl));
					// 将要货量设置到实体p0数量中去
					yaohl = cangkMap.get(bean.getGongysdm());
					// 将得到的结果存放到map中(分配后的要货量和盈余量)
					valueMap = this.quzheng(yaohl, bean.getZhizlx(), bean.getUabzucrl().multiply(bean.getUabzucsl()),
							bean.getZuixqdl());
					// 赋给零件标志新值
					flagLingj = bean.getUsercenter() + bean.getLingjbh();
					// 赋给仓库标志新值
					flagCangku = bean.getCangkdm();
				} else if (flagLingj.equals(ljFlag) && !flagCangku.equals(ckFlag)) {// 仓库发生变化的时候
					// 将仓库map中的值汇总到总map中
					zongMap = CommonFun.sumMapValue(cangkMap, zongMap);
					// 清空仓库map
					cangkMap.clear();
					// 判断map不为空
					if (!zongMap.isEmpty()) {
						// 遍历map得到总要货量
						zyhl = CommonFun.sumValue(zongMap);
						// 从总map中获取到供应商已要货量
						gongysyyhl = zongMap.get(bean.getGongysdm());
					}
					// 从公式得到要货量p0
					p0 = (zyhl.add(zhouqyhl)).multiply(fene).subtract(gongysyyhl);
					// 不为负数
					if (p0.intValue() < 0) {
						p0 = zero;
						// 要货数量不大于周期要货数量
					} else if (p0.subtract(zhouqyhl).intValue() > 0) {
						p0 = zhouqyhl;
					}
					// 判断仓库map是否为空
					if (null == cangkMap.get(bean.getGongysdm())) {
						cangkMap = this.getValue(bean, fene, zyhl, zhouqyhl, gongysyyhl);
					}
					// 将要货量设置到实体p0数量中去
					yaohl = cangkMap.get(bean.getGongysdm());
					// 将得到的结果存放到map中(分配后的要货量和盈余量)
					valueMap = this.quzheng(yaohl, bean.getZhizlx(), bean.getUabzucrl().multiply(bean.getUabzucsl()),
							bean.getZuixqdl());
					// 赋给零件标志新值
					flagLingj = bean.getUsercenter() + bean.getLingjbh();
					// 赋给仓库标志新值
					flagCangku = bean.getCangkdm();
				}
			}
		}
		CommonFun.logger.debug("Il订单计算,份额计算结束");
		//this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "gongysFeneJs----份额计算结束");
		// 返回实体
		return valueMap;
	}

	/**
	 * 份额计算：初始化要货数量
	 * **/
	public Map getValue(Dingdlj bean, BigDecimal fene, BigDecimal zyhl, BigDecimal zhouqyhl, BigDecimal gongysyyhl) {
		// 要货量
		BigDecimal psl = BigDecimal.ZERO;
		//
		BigDecimal zero = BigDecimal.ZERO;
		// 没有指定供应商
		if (null == bean.getZhidgys()) {
			// 从公式得到要货量
			psl = (zyhl.add(zhouqyhl)).multiply(fene).subtract(gongysyyhl);
			// 指定供应商的情况
		} else {
			// 指定供应商一致的情况，将份额是为100%
			if (bean.getGongysdm().equalsIgnoreCase(bean.getZhidgys())) {
				psl = (zyhl.add(zhouqyhl)).subtract(gongysyyhl);
				// 指定供应商不一致的情况
			} else {
				psl = zero;
			}
		}
		// 将得到的要货量存放到仓库map中
		cangkMap.put(bean.getGongysdm(), this.checkZero(psl, zhouqyhl));
		return cangkMap;
	}

	// 份额计算：判断传入的参数是否大于最大值和是否小于0
	private BigDecimal checkZero(BigDecimal value, BigDecimal maxValue) {
		BigDecimal lastValue = BigDecimal.ZERO;
		if (value.intValue() < 0) {
			lastValue = BigDecimal.ZERO;
		} else if (value.subtract(maxValue).intValue() > 0) {
			lastValue = maxValue;
		}
		return lastValue;
	}

	/**
	 * <p>
	 * Title:要货量取整方法
	 * </p>
	 * <p>
	 * Description:根据已有的要货量进行向上取整
	 * </p>
	 * 参数为
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-1
	 **/
	public Map<String, BigDecimal> quzheng(BigDecimal yaohls, String zhizlx, BigDecimal baozrl, BigDecimal zuixqdl) {
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		BigDecimal quzhyhl = BigDecimal.ZERO;
		BigDecimal yingyus = BigDecimal.ZERO;
		if (zhizlx != null && zhizlx.equalsIgnoreCase(// 如果是普通零件则按包装取整
				Const.ZHOUQIGONGHUOMOSHI_IL_JUANLIAO)) {
			
			CommonFun.logger.debug("卷料包装取整包装容量："+baozrl);
			CommonFun.logger.debug("卷料包装取整要货数量："+yaohls);
			if (yaohls.compareTo(BigDecimal.ZERO) == -1) {
				yingyus = yaohls.negate();
				yaohls = BigDecimal.ZERO;
				quzhyhl = yaohls.divide(baozrl, 0, BigDecimal.ROUND_UP).multiply(baozrl);
			}else{
			quzhyhl = yaohls.divide(baozrl, 0, BigDecimal.ROUND_UP).multiply(baozrl);
			CommonFun.logger.debug("卷料包装取整取整后要货量："+quzhyhl);
			
			/* xss - 0012844 按照包装要货 ， dinglj和 dingdmx保持一致
			if(null!= zuixqdl){
				CommonFun.logger.debug("卷料包装取整最小起订量："+zuixqdl);
				if(quzhyhl.compareTo(zuixqdl)==-1){
					quzhyhl = yaohls.divide(zuixqdl, 0, BigDecimal.ROUND_UP).multiply(zuixqdl);
					CommonFun.logger.debug("卷料包装取整按最小起订量取整后："+quzhyhl);
				}
			}
			*/
			}
			
			yingyus = quzhyhl.subtract(yaohls);
			CommonFun.logger.debug("卷料包装取整盈余数量："+yingyus);
		} else {
			// 按照零件供应商表中的最小起订量取整
			CommonFun.logger.debug("普通零件包装取整包装容量："+baozrl);
			CommonFun.logger.debug("普通零件包装取整要货数量："+yaohls);
			if (yaohls.compareTo(BigDecimal.ZERO) == -1) {
				yingyus = yaohls.negate();
				yaohls = BigDecimal.ZERO;
				quzhyhl = yaohls.divide(baozrl, 0, BigDecimal.ROUND_UP).multiply(baozrl);
			}else{
					quzhyhl = yaohls.divide(baozrl, 0, BigDecimal.ROUND_UP).multiply(baozrl);
					CommonFun.logger.debug("普通零件包装取整取整后要货量："+quzhyhl);
					yingyus = quzhyhl.subtract(yaohls);
					CommonFun.logger.debug("普通零件包装取整盈余数量："+yingyus);
				}	
		}
		map.put("yaohl", quzhyhl);
		map.put("yingyu", yingyus);
		return map;
	}

	public BigDecimal tidjKuc(String usercenter, String lingjbh, String ziyhqrq)   {
		
		Map<String, Object> tidjMap = new HashMap<String, Object>();
		tidjMap.put("usercenter", usercenter);
		tidjMap.put("lingjbh", lingjbh);
		CommonFun.mapPrint(tidjMap, "替代件库存查询tidjKuc方法查找替代件仓库参数map");
		CommonFun.logger.debug("替代件库存查询tidjKuc方法查找替代件仓库的sql语句为：");
		List<Tidj> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTidj", tidjMap);
		//CommonFun.objListPrint(list, "替代件库存查询tidjKuc方法查找替代件仓库的结果list");
		BigDecimal tidjKcSum = BigDecimal.ZERO;
		if (!list.isEmpty()) {
			for (Tidj tidj : list) {
				
				Map<String, Object> tidjKcMap = new HashMap();
				tidjKcMap.put("usercenter", tidj.getUsercenter());
				tidjKcMap.put("lingjbh", tidj.getLingjbh());
				tidjKcMap.put("ziyhqrq", ziyhqrq);
				CommonFun.mapPrint(tidjMap, "替代件库存查询tidjKuc方法查找替代件仓库库存和参数map");
				Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryTidjkc", tidjKcMap);
				if(obj== null){
					tidjKcSum = tidjKcSum.add(BigDecimal.ZERO);
				}else{
					tidjKcSum = tidjKcSum.add(new BigDecimal(obj.toString()));
				}
				
			}
		}
		return tidjKcSum;
	}

	/**
	 *@方法： 检查消耗比例：检查消耗比例中间表
	 * @参数：其中可以分别为：firstSQL和secondSQL
	 * */
	public void countByPartten(String firstSQL, String secondSQL,String jihyz,LoginUser loginUser) {
		List<Xiaohblzjb> xiaohblList = (ArrayList<Xiaohblzjb>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(firstSQL);
		for (Xiaohblzjb xiaohblzjb : xiaohblList) {
			
			String paramStr[] = new String []{xiaohblzjb.getUsercenter(), xiaohblzjb.getLingjbh(),xiaohblzjb.getShengcxbh()};
			this.yicbjservice.insertError(Const.YICHANG_LX6, Const.YICHANG_LX6_str1, jihyz, paramStr,xiaohblzjb.getUsercenter(), xiaohblzjb.getLingjbh(), loginUser, Const.JISMK_IL_CD);
			
//			String info = "产线上消耗比例和不为100%";
//			this.yicbjservice.saveYicInfo(Const.JISMK_IL_CD, xiaohblzjb.getLingjbh(), Const.YICHANG_LX6, info);
			Map<String, String> ppMap = new HashMap<String, String>();
			ppMap.put("usercenter", xiaohblzjb.getUsercenter());
			ppMap.put("lingjbh", xiaohblzjb.getLingjbh());
			ppMap.put("chanx", xiaohblzjb.getShengcxbh());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(secondSQL, ppMap);
		}
	}

	/**
	 *@方法：检查消耗比例： 根据订单类型获取执行的sql语句
	 * @参数：其中key分别为：firstSQL和secondSQL
	 * */
	private Map<String,String> getSQL(String dingdlx){
		Map<String,String> map_ps = new HashMap<String,String>() ;
		Map<String,String> map_pj = new HashMap<String,String>() ;
		Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>() ;
		map_ps.put("firstSQL", "ilorder.checkXiaohblPS") ;
		map_ps.put("secondSQL", "ilorder.deleteOneOfMaoxqhzs") ;
		map_pj.put("firstSQL", "ilorder.checkXiaohblPJ") ;
		map_pj.put("secondSQL", "ilorder.deleteOneOfMaoxqhzj") ;
		map.put(Const.PS, map_ps) ;
		map.put(Const.PJ, map_pj);	
		return map.get(dingdlx.toUpperCase()) ;
	}
	
	/**
	 *@方法： 检查消耗比例：根据订单类型获取执行的sql语句
	 * @参数：订单类型和计划员组
	 * */
	public int checkXiaohbl(String dingdlx) {
		long start = System.currentTimeMillis();
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算", "产线消耗比例校验开始");
		this.wulljservice.checkXhbl(dingdlx);
		ArrayList<Xiaohblzjb> errorList = new ArrayList<Xiaohblzjb>();
		if (dingdlx.equals(Const.PP)) {
			String info = "";
			CommonFun.logger.debug("PP模式产线消耗比例校验语句为：ilorder.checkXiaohblPP");
			errorList = (ArrayList<Xiaohblzjb>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.checkXiaohblPP");
			//CommonFun.objListPrint(errorList, "PP模式产线消耗比例校验结果errorList");
			if(errorList.size()>0){
				for(Xiaohblzjb xiaohblzjb:errorList){
					//info ="用户中心为"+xiaohblzjb.getUsercenter()+ "编号为"+xiaohblzjb.getLingjbh()+"的零件在编号为"+xiaohblzjb.getShengcxbh()+"的产线上消耗比例和不为100%";
					LoginUser loginUser = AuthorityUtils.getSecurityUser();
					//this.yicbjservice.insertManyYic(Const.JISMK_IL_CD, Const.YICHANG_LX6, info, jihyz, loginUser.getUsername(),xiaohblzjb.getUsercenter(),xiaohblzjb.getLingjbh());
					Map<String,String> param = new HashMap<String,String>();
					param.put("usercenter", xiaohblzjb.getUsercenter());
					param.put("lingjbh", xiaohblzjb.getLingjbh());
					param.put("shengcxbh", xiaohblzjb.getShengcxbh());
					List<Lingjxhd> xhdList = this.lingjxhdservice.queryShengcxByParam(param);
					if(null != xhdList && xhdList.size() > 0)
					{
						for(Lingjxhd lingjxhd :xhdList){
							param.clear();
							param.put("usercenter", lingjxhd.getUsercenter());
							param.put("lingjbh", lingjxhd.getLingjbh());
							param.put("shengcxbh", lingjxhd.getShengcxbh());
							param.put("fenpqh", lingjxhd.getFenpqbh());
							String wulgyyz = this.wulljservice.queryWulgyy(param);
							String paramStr[] = new String []{lingjxhd.getUsercenter(),lingjxhd.getLingjbh(),lingjxhd.getShengcxbh(),lingjxhd.getFenpqbh()};
							try
							{
								this.yicbjservice.insertError(Const.YICHANG_LX6, Const.YICHANG_LX6_str2, wulgyyz, paramStr,lingjxhd.getUsercenter(), lingjxhd.getLingjbh(), loginUser, Const.JISMK_IL_CD);
							}
							catch (Exception e) {
								CommonFun.logger.error(e);
							}
						}
					}
					else
					{
						try
						{
							this.yicbjservice.saveYicbj(xiaohblzjb.getLingjbh(),  xiaohblzjb.getUsercenter(),"用户中心:" + xiaohblzjb.getUsercenter() +"下零件编号为:"+xiaohblzjb.getLingjbh()  +"在产线:"+xiaohblzjb.getShengcxbh()+"上没有消耗点.", loginUser.getUsername(), Const.JISMK_IL_CD, Const.YICHANG_LX6, loginUser.getUsername());
						}
						catch (Exception e) {
							CommonFun.logger.error(e);
						}
					}
					Map<String,String> paramMap = new HashMap<String,String>();
					paramMap.put("usercenter", xiaohblzjb.getUsercenter());
					paramMap.put("lingjbh", xiaohblzjb.getLingjbh());
					paramMap.put("chanx", xiaohblzjb.getShengcxbh());
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteOneOfMaoxqhzp", paramMap);
				}
				
			}
		}else if(dingdlx.equals(Const.PS)){
			String info = "";
			CommonFun.logger.debug("PS模式产线消耗比例校验语句为：ilorder.checkXiaohblPS");
			errorList = (ArrayList<Xiaohblzjb>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.checkXiaohblPS");
			//CommonFun.objListPrint(errorList, "PP模式产线消耗比例校验结果errorList");
			if(errorList.size()>0){
				for(Xiaohblzjb xiaohblzjb:errorList){
					//info ="用户中心为"+xiaohblzjb.getUsercenter()+ "编号为"+xiaohblzjb.getLingjbh()+"的零件在编号为"+xiaohblzjb.getShengcxbh()+"的产线上消耗比例和不为100%";
					LoginUser loginUser = AuthorityUtils.getSecurityUser();
					
					Map<String,String> param = new HashMap<String,String>();
					param.put("usercenter", xiaohblzjb.getUsercenter());
					param.put("lingjbh", xiaohblzjb.getLingjbh());
					param.put("shengcxbh", xiaohblzjb.getShengcxbh());
					List<Lingjxhd> xhdList = this.lingjxhdservice.queryShengcxByParam(param);
					if(null != xhdList && xhdList.size() > 0)
					{
						for(Lingjxhd lingjxhd :xhdList){
							param.clear();
							param.put("usercenter", lingjxhd.getUsercenter());
							param.put("lingjbh", lingjxhd.getLingjbh());
							param.put("shengcxbh", lingjxhd.getShengcxbh());
							param.put("fenpqh", lingjxhd.getFenpqbh());
							Wullj wj = this.wulljservice.queryWulljObject(param);
							String paramStr[] = new String []{lingjxhd.getUsercenter(),lingjxhd.getLingjbh(),lingjxhd.getShengcxbh(),lingjxhd.getFenpqbh()};
							try
							{
								this.yicbjservice.insertError(Const.YICHANG_LX6, Const.YICHANG_LX6_str2, wj.getWulgyyz(), paramStr,lingjxhd.getUsercenter(), lingjxhd.getLingjbh(), loginUser, Const.JISMK_IL_CD);
							}
							catch (Exception e) {
								CommonFun.logger.error(e);
							}
						}
					}
					else
					{
						try
						{
							this.yicbjservice.saveYicbj(xiaohblzjb.getLingjbh(),  xiaohblzjb.getUsercenter(),"用户中心:" + xiaohblzjb.getUsercenter() +"下零件编号为:"+xiaohblzjb.getLingjbh()  +"在产线:"+xiaohblzjb.getShengcxbh()+"上没有消耗点.", loginUser.getUsername(), Const.JISMK_IL_CD, Const.YICHANG_LX6, loginUser.getUsername());
						}
						catch (Exception e) {
							CommonFun.logger.error(e);
						}
					}
					//this.yicbjservice.insertManyYic(Const.JISMK_IL_CD, Const.YICHANG_LX6, info, jihyz, loginUser.getUsername(),xiaohblzjb.getUsercenter(),xiaohblzjb.getLingjbh());
//					String paramStr[] = new String []{xiaohblzjb.getUsercenter(), xiaohblzjb.getLingjbh(),xiaohblzjb.getShengcxbh()};
//					this.yicbjservice.insertError(Const.YICHANG_LX6, Const.YICHANG_LX6_str1, jihyz, paramStr,xiaohblzjb.getUsercenter(), xiaohblzjb.getLingjbh(), loginUser, Const.JISMK_IL_CD);
//					
					Map<String,String> paramMap = new HashMap<String,String>();
					paramMap.put("usercenter", xiaohblzjb.getUsercenter());
					paramMap.put("lingjbh", xiaohblzjb.getLingjbh());
					paramMap.put("chanx", xiaohblzjb.getShengcxbh());
					//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteAllMaoxqhzs", paramMap);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteOneOfMaoxqhzs", paramMap);
				}
			}
		}else if(dingdlx.equals(Const.PJ)){
			String info = "";
			CommonFun.logger.debug("PJ模式产线消耗比例校验语句为：ilorder.checkXiaohblPJ");
			errorList = (ArrayList<Xiaohblzjb>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.checkXiaohblPJ");
			//CommonFun.objListPrint(errorList, "PP模式产线消耗比例校验结果errorList");
			if(errorList.size()>0){
				for(Xiaohblzjb xiaohblzjb:errorList){
					//info ="用户中心为"+xiaohblzjb.getUsercenter()+ "编号为"+xiaohblzjb.getLingjbh()+"的零件在编号为"+xiaohblzjb.getShengcxbh()+"的产线上消耗比例和不为100%";
					LoginUser loginUser = AuthorityUtils.getSecurityUser();
					//this.yicbjservice.insertManyYic(Const.JISMK_IL_CD, Const.YICHANG_LX6, info, jihyz, loginUser.getUsername(),xiaohblzjb.getUsercenter(),xiaohblzjb.getLingjbh());
					Map<String,String> param = new HashMap<String,String>();
					param.put("usercenter", xiaohblzjb.getUsercenter());
					param.put("lingjbh", xiaohblzjb.getLingjbh());
					param.put("shengcxbh", xiaohblzjb.getShengcxbh());
					List<Lingjxhd> xhdList = this.lingjxhdservice.queryShengcxByParam(param);
					if(null != xhdList && xhdList.size() > 0)
					{
						for(Lingjxhd lingjxhd :xhdList){
							param.clear();
							param.put("usercenter", lingjxhd.getUsercenter());
							param.put("lingjbh", lingjxhd.getLingjbh());
							param.put("shengcxbh", lingjxhd.getShengcxbh());
							param.put("fenpqh", lingjxhd.getFenpqbh());
							Wullj wj = this.wulljservice.queryWulljObject(param);
							String paramStr[] = new String []{lingjxhd.getUsercenter(),lingjxhd.getLingjbh(),lingjxhd.getShengcxbh(),lingjxhd.getFenpqbh()};
							try
							{
								this.yicbjservice.insertError(Const.YICHANG_LX6, Const.YICHANG_LX6_str2, wj.getWulgyyz(), paramStr,lingjxhd.getUsercenter(), lingjxhd.getLingjbh(), loginUser, Const.JISMK_IL_CD);
							}
							catch (Exception e) {
								CommonFun.logger.error(e);
							}
						}
					}
					else
					{
						try
						{
							this.yicbjservice.saveYicbj(xiaohblzjb.getLingjbh(),  xiaohblzjb.getUsercenter(),"用户中心:" + xiaohblzjb.getUsercenter() +"下零件编号为:"+xiaohblzjb.getLingjbh()  +"在产线:"+xiaohblzjb.getShengcxbh()+"上没有消耗点.", loginUser.getUsername(), Const.JISMK_IL_CD, Const.YICHANG_LX6, loginUser.getUsername());
						}
						catch (Exception e) {
							CommonFun.logger.error(e);
						}
					}
					//					String paramStr[] = new String []{xiaohblzjb.getUsercenter(), xiaohblzjb.getLingjbh(),xiaohblzjb.getShengcxbh()};
//					this.yicbjservice.insertError(Const.YICHANG_LX6, Const.YICHANG_LX6_str1, jihyz, paramStr,xiaohblzjb.getUsercenter(), xiaohblzjb.getLingjbh(), loginUser, Const.JISMK_IL_CD);
					Map<String,String> paramMap = new HashMap<String,String>();
					paramMap.put("usercenter", xiaohblzjb.getUsercenter());
					paramMap.put("lingjbh", xiaohblzjb.getLingjbh());
					paramMap.put("chanx", xiaohblzjb.getShengcxbh());
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteOneOfMaoxqhzj", paramMap);
				}
			}
		}
		long end = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,消耗比例校验时间"+ (end - start)/1000);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算", "产线消耗比例校验结束");
		return errorList.size();
	}

	/**
	 * @行列转换，得到订单零件类集合（周和周期模式）,然后插入订单零件表
	 * @author 陈骏
	 * @version v1.0
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @date 2012-01-03
	 */
	public void yugInDingdljPS(String p0zhouqxh, String usercenter, String lingjbh, String cangkdm, String gongysdm,
			String dingdlx, BigDecimal beihzq, BigDecimal fayzq, BigDecimal gongysfe, String ziyhqrq, String jihydz,
			String dingdnr,String gongyshth,String jssj,String jihyz,String banc,String caozz,String gcbh,Map<String,List<Dingdlj>> saveLingjMap,Map<String, List<Dingdmx>> saveMingxMap) {
		Map<String, String> map = new HashMap<String, String>();
		List<Dingdlj> ls = new ArrayList<Dingdlj>();
		if(dingdlx.equals(Const.PP)){
			map.put("nianzq", p0zhouqxh);
			map.put("usercenter", usercenter);
			map.put("lingjbh", lingjbh);
			map.put("cangkdm", cangkdm);
			map.put("gongysdm", gongysdm);
			ls = this.yugzjbservice.colRowYugzjbPP(map);
		}else if(dingdlx.equals(Const.PS)){
			map.put("usercente", usercenter);
			map.put("nianzx", p0zhouqxh);
			map.put("lingjbh", lingjbh);
			map.put("cangkdm", cangkdm);
			map.put("gongysdm", gongysdm);
			String maxZhoux = this.calendarcenterservice.getMaxNianzx(map);
			CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中maxZhoux="+maxZhoux);
			if(null!=maxZhoux){
				for(int i = 1;i<4;i++){
					CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中p0zhouqxh="+p0zhouqxh);
					if(Integer.parseInt(p0zhouqxh)+i>Integer.parseInt(maxZhoux)){
						int nian = Integer.parseInt(p0zhouqxh.substring(1,4))+1;
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中nian="+nian);
						int zhou = Integer.parseInt(p0zhouqxh.substring(4,6))+(Integer.parseInt(p0zhouqxh)+i-Integer.parseInt(maxZhoux));
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中zhou="+zhou);
						String value = nian+""+zhou;
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中value="+value);
							map.put("nianzx"+i, value);
					}else{
						map.put("nianzx"+i, (Integer.parseInt(p0zhouqxh)+i)+"");
					}
				}
			}
			ls = this.yugzjbservice.colRowYugzjbPS(map);
			//CommonFun.objListPrint(ls, "行列转换，得到订单零件类集合yugInDingdljPS方法中ls");
		}
		
		if (null != ls) {
//			List<Dingd> ddRsList = new ArrayList<Dingd>();
//			List<Dingdlj> djRsList = new ArrayList<Dingdlj>();
			List<Dingdmx> dmRsList = new ArrayList<Dingdmx>();
			for (Dingdlj dingdlj : ls) {
				if(dingdlx.equals(Const.PP)){
					dingdlj.setDingdh(this.getOrderNumber(Const.PP, usercenter, gongysdm,this.orderNumberMap));// 订单号
					CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中PP模式dingdlj.setDingdh="+dingdlj.getDingdh());
				}else if(dingdlx.equals(Const.PS)){
					dingdlj.setDingdh(this.getOrderNumber(Const.PS, usercenter, gongysdm,this.orderNumberMap));// 订单号
					CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中Ps模式dingdlj.setDingdh="+dingdlj.getDingdh());
				}
				dingdlj.setBeihzq(beihzq);
				dingdlj.setFayzq(fayzq);
				dingdlj.setGongysfe(gongysfe);
				dingdlj.setZiyhqrq(ziyhqrq);
				dingdlj.setJihyz(jihydz);
				dingdlj.setDingdnr(dingdnr);
				dingdlj.setActive(Const.ACTIVE_1);
				dingdlj.setCreator(caozz);
				dingdlj.setDingdzzsj(CommonFun.getJavaTime());
				dingdlj.setCreate_time(CommonFun.getJavaTime());
				dingdlj.setEditor(caozz);
				dingdlj.setEdit_time(CommonFun.getJavaTime());
				if (dingdlx.equals(Const.PP)) {
					dingdlj.setGonghms(Const.FEIZHOUQIPP);
					for (int x = 0; x < dingdlj.getDingdnr().length(); x++) {
						Dingdmx dingdmx = new Dingdmx();
						String methodPd = Const.GETP + (x) + "sl";
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式methodPd="+methodPd);
						Class clsdd = dingdlj.getClass();
						Method methdPd;
						try {
							methdPd = clsdd.getMethod(methodPd, new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,NoSuchMethodException");
						}
						BigDecimal jissl;
						try {
							jissl = new BigDecimal(methdPd.invoke(dingdlj, null).toString());
						} catch (IllegalArgumentException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,IllegalArgumentException");
						} catch (IllegalAccessException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,IllegalAccessException");
						} catch (InvocationTargetException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,InvocationTargetException");
						}
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式jissl="+jissl);
						dingdmx.setUabzlx(dingdlj.getUabzlx());
						dingdmx.setUabzuclx(dingdlj.getUabzuclx());
						dingdmx.setUabzucsl(dingdlj.getUabzucsl());
						dingdmx.setUabzucrl(dingdlj.getUabzucrl());
						dingdmx.setDingdh(dingdlj.getDingdh());
						dingdmx.setUsercenter(dingdlj.getUsercenter());
						dingdmx.setLingjbh(dingdlj.getLingjbh());
						dingdmx.setGongysdm(dingdlj.getGongysdm());
						dingdmx.setGongyslx(dingdlj.getGongyslx());
						dingdmx.setCangkdm(dingdlj.getCangkdm());
						dingdmx.setFahd(dingdlj.getFahd());
						dingdmx.setDinghcj(dingdlj.getDinghcj());
						dingdmx.setDanw(dingdlj.getDanw());
						dingdmx.setLujdm(dingdlj.getLujdm());
						dingdmx.setJihyz(dingdlj.getJihyz());
						dingdmx.setYijfl(dingdlj.getYijfl());
						dingdmx.setZhuangt(Const.DINGD_STATUS_DSX);
						dingdmx.setLeix(Const.SHIFOUSHIYUGAO);
						dingdmx.setGonghlx(Const.FEIZHOUQIPP);
						dingdmx.setActive(Const.ACTIVE_1);
						dingdmx.setCreator(caozz);
						dingdmx.setGcbh(gcbh);
						dingdmx.setCreate_time(CommonFun.getJavaTime());
						Gongyzq gongyzq = this.gongyzqService.queryGongyzq(CommonFun.addNianzq(dingdlj.getP0fyzqxh(),Const.MAXZQ, x));
						if (null != gongyzq) {
							// 设置要货开始时间
							dingdmx.setYaohqsrq(gongyzq.getKaissj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式Yaohqsrq="+gongyzq.getKaissj());
							// 设置要货结束时间
							dingdmx.setYaohjsrq(gongyzq.getJiessj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式Yaohjsrq="+gongyzq.getJiessj());
							dingdmx.setJiaofrq(gongyzq.getJiessj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式Jiaofrq="+gongyzq.getJiessj());
							dingdmx.setFayrq(gongyzq.getKaissj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式Fayrq="+gongyzq.getKaissj());
						}

						dingdmx.setShul(jissl);
						dingdmx.setJissl(jissl);
						CommonFun.objPrint(dingdmx, "行列转换，得到订单零件类集合yugInDingdljPS方法中pp模式dingdmx");
//wuyichao/////////////////////////////////////////////////////
						//dmRsList.add(dingdmx);
						String key = dingdmx.getDingdh();
						List<Dingdmx> flagmx = saveMingxMap.get(key);
						if(null == flagmx)
						{
							flagmx = new ArrayList<Dingdmx>();
						}
						flagmx.add(dingdmx);
						saveMingxMap.put(key, flagmx);
//////////////////////////////////////////////////
//						this.dingdmxService.doInsert(dingdmx);
					}
				} else if (dingdlx.equals(Const.PS)) {
					dingdlj.setGonghms(Const.FEIZHOUQIPS);
					for (int x = 0; x < dingdlj.getDingdnr().length(); x++) {
						Dingdmx dingdmx = new Dingdmx();
						String methodSd = Const.GETP + x + "sl";
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式methodSd="+methodSd);
						Class clsdd = dingdlj.getClass();
						Method methdSd;
						try {
							methdSd = clsdd.getMethod(methodSd, new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,NoSuchMethodException");
						}
						BigDecimal jissl;
						try {
							jissl = new BigDecimal(methdSd.invoke(dingdlj, null).toString());
						} catch (IllegalArgumentException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,IllegalArgumentException");
						} catch (IllegalAccessException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,IllegalAccessException");
						} catch (InvocationTargetException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPS,InvocationTargetException");
						}
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式jissl="+jissl);
						dingdmx.setUabzlx(dingdlj.getUabzlx());
						dingdmx.setUabzuclx(dingdlj.getUabzuclx());
						dingdmx.setUabzucsl(dingdlj.getUabzucsl());
						dingdmx.setUabzucrl(dingdlj.getUabzucrl());
						dingdmx.setDingdh(dingdlj.getDingdh());
						dingdmx.setUsercenter(dingdlj.getUsercenter());
						dingdmx.setLingjbh(dingdlj.getLingjbh());
						dingdmx.setGongysdm(dingdlj.getGongysdm());
						dingdmx.setGongyslx(dingdlj.getGongyslx());
						dingdmx.setCangkdm(dingdlj.getCangkdm());
						dingdmx.setFahd(dingdlj.getFahd());
						dingdmx.setDinghcj(dingdlj.getDinghcj());
						dingdmx.setDanw(dingdlj.getDanw());
						dingdmx.setLujdm(dingdlj.getLujdm());
						dingdmx.setJihyz(dingdlj.getJihyz());
						dingdmx.setYijfl(dingdlj.getYijfl());
						dingdmx.setZhuangt(Const.DINGD_STATUS_DSX);
						dingdmx.setLeix(Const.SHIFOUSHIYUGAO);
						dingdmx.setGonghlx(Const.FEIZHOUQIPS);
						dingdmx.setActive(Const.ACTIVE_1);
						dingdmx.setCreator(caozz);
						dingdmx.setGcbh(gcbh);
						dingdmx.setCreate_time(CommonFun.getJavaTime());
						Map<String,String> zhouxMap = new HashMap<String,String>();
						map.put("usercenter", dingdmx.getUsercenter());
						map.put("nianzq", dingdlj.getP0fyzqxh().substring(0, 4));
						// 获取到本年内的最大年周序
						CalendarCenter center2 = this.calendarCenterService.maxTime(zhouxMap);
						Gongyzx gongyzx = this.gongyzqService.queryGongyzx(CommonFun.addNianzq(dingdlj.getP0fyzqxh(),center2.getNianzx(), x));
						if (null != gongyzx) {
							// 设置要货开始时间
							dingdmx.setYaohqsrq(gongyzx.getKaissj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式Yaohqsrq="+gongyzx.getKaissj());
							// 设置要货结束时间
							dingdmx.setYaohjsrq(gongyzx.getJiessj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式Yaohjsrq="+gongyzx.getJiessj());
							dingdmx.setJiaofrq(gongyzx.getJiessj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式Jiaofrq="+gongyzx.getJiessj());
							dingdmx.setFayrq(gongyzx.getKaissj());
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式Fayrq="+gongyzx.getKaissj());
						}

						dingdmx.setShul(jissl);
						dingdmx.setJissl(jissl);
						CommonFun.objPrint(dingdmx, "行列转换，得到订单零件类集合yugInDingdljPS方法中ps模式dingdmx");
//						this.dingdmxService.doInsert(dingdmx);
//wuyichao/////////////////////////////////////////////////////
						//dmRsList.add(dingdmx);
						String key = dingdmx.getDingdh();
						List<Dingdmx> flagmx = saveMingxMap.get(key);
						if(null == flagmx)
						{
							flagmx = new ArrayList<Dingdmx>();
						}
						flagmx.add(dingdmx);
						saveMingxMap.put(key, flagmx);
//////////////////////////////////////////////////
					}
				}
////////////////////////////wuyichao////////////////////
				String key = dingdlj.getDingdh();
				List<Dingdlj> flaglj = saveLingjMap.get(key);
				if(null == flaglj)
				{
					flaglj = new ArrayList<Dingdlj>();
				}
				flaglj.add(dingdlj);
				saveLingjMap.put(key, flaglj);
			//	this.dingdljservice.doInsert(dingdlj);
////////////////////////////wuyichao////////////////////
				//djRsList.add(dingdlj);
				Dingd existDd = new Dingd();
				Map<String, String> ddhMap = new HashMap<String, String>();
				ddhMap.put("dingdh", dingdlj.getDingdh());
				existDd = this.dingdservice.queryDingdByDingdh(ddhMap);
				CommonFun.objPrint(existDd, "行列转换，得到订单零件类集合yugInDingdljPS方法中existDd");
				if (null == existDd) {
					Map<String,String> riqMap = new HashMap<String,String>();
					riqMap.put("usercenter", dingdlj.getUsercenter());
					riqMap.put("riq", dingdlj.getZiyhqrq().substring(0,10));
					CalendarCenter centerObject = this.calendarCenterService.queryCalendarCenterObject(riqMap);
					Dingd dd = new Dingd();
					dd.setDingdh(dingdlj.getDingdh());// 订单号
					dd.setHeth(gongyshth);// 合同号
					dd.setDingdlx(Const.DINGDLX_ILORDER);// 国产订单类型
					dd.setGongysdm(dingdlj.getGongysdm());// 供应商编号
					dd.setChullx(dingdlj.getGonghms());
					dd.setDingdzt(Const.DINGD_STATUS_JSZ);
					dd.setDingdjssj(jssj);
					dd.setDingdnr(dingdlj.getDingdnr());
					dd.setZiyhqrq(dingdlj.getZiyhqrq());
					dd.setFahzq(dingdlj.getP0fyzqxh());
					dd.setMaoxqbc(banc);
					dd.setJislx(jihyz);
					dd.setShifyjsyhl(Const.SHIFYJSYHL_Y);
					dd.setShifzfsyhl(Const.SHIFZFSYHL_YES);
					dd.setCreator(caozz);
					dd.setCreate_time(CommonFun.getJavaTime());
					dd.setActive(Const.ACTIVE_0);
					dd.setUsercenter(dingdlj.getUsercenter());
					dd.setJiszq(centerObject.getNianzq());
					CommonFun.objPrint(existDd, "行列转换，得到订单零件类集合yugInDingdljPS方法中dd");
					this.dingdservice.doInsert(dd);
				}
			}
			//this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("", djRsList);
///////////////////wuyichao/////////////////
			//this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertDingdmx", dmRsList);
/////////////////wuyichao/////////////////
		}
	}

	/**
	 * @行列转换，得到订单零件类集合（日模式）,然后插入订单零件表
	 * @author 陈骏
	 * @version v1.0
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2012-01-03
	 */
	public void yugInDingdljPJ(Map<String, String> map, BigDecimal beihzq, BigDecimal fayzq, BigDecimal gongysfe,
			String ziyhqrq, String jihydz, String dingdnr,String gongyshth,String jssj,String jihyz,String banc,String caozz,String gcbh , Map<String,List<Dingdlj>> saveLingjMap, Map<String,List<Dingdmx>> saveMingxMap) {
		List<Dingdlj> ls = this.yugzjbservice.colRowYugzjbJ(map);
		//CommonFun.objListPrint(ls, "行列转换，得到订单零件类集合yugInDingdljPJ方法中ls");
		if (null != ls) {
			for (Dingdlj dingdlj : ls) {
				dingdlj.setDingdh(this.getOrderNumber(Const.PJ, map.get("usercenter"), map.get("gongysdm"),this.orderNumberMap));// 订单号
				dingdlj.setGonghms(Const.FEIZHOUQIPJ);
				dingdlj.setBeihzq(beihzq);
				dingdlj.setFayzq(fayzq);
				dingdlj.setGongysfe(gongysfe);
				dingdlj.setZiyhqrq(ziyhqrq);
				dingdlj.setJihyz(jihydz);
				dingdlj.setDingdnr(dingdnr);
				dingdlj.setActive(Const.ACTIVE_1);
				dingdlj.setCreator(caozz);
				dingdlj.setP0fyzqxh(map.get("j0rq"));
				dingdlj.setCreate_time(CommonFun.getJavaTime());
				dingdlj.setEditor(caozz);
				dingdlj.setEdit_time(CommonFun.getJavaTime());
				CommonFun.objPrint(dingdlj, "行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式dingdlj");
				//this.dingdljservice.doInsert(dingdlj);
				for (int x = 0; x < Const.DINGDNRCHANGDU; x++) {
					Dingdmx dingdmx = new Dingdmx();
					String methodJd = Const.GETP + x + "sl";
					CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式methodJd="+methodJd);
					Class clsdd = dingdlj.getClass();
					Method methdjd;
					try {
						methdjd = clsdd.getMethod(methodJd, new Class[] {});
					} catch (SecurityException e1) {
						CommonFun.logger.error(e1);
						throw new RuntimeException("IlOrderService.yugInDingdljPJ,SecurityException");
					} catch (NoSuchMethodException e1) {
						CommonFun.logger.error(e1);
						throw new RuntimeException("IlOrderService.yugInDingdljPJ,NoSuchMethodException");
					}
					String pxriq = "";
					Method meth = null;

					BigDecimal jissl = BigDecimal.ZERO;
					if (x > Const.ZJCHANGDU) {
						jissl = BigDecimal.ZERO;
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式在（x > Const.ZJCHANGDU）时jissl="+jissl);
					} else {
						
						clsdd = dingdlj.getClass();
						if (x == 0) {
							methodJd = "getP" + x + "fyzqxh";
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式不在（x > Const.ZJCHANGDU）并且在（x == 0）时methodJd="+methodJd);
						} else {
							methodJd = "getP" + x + "rq";
							CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式不在（x > Const.ZJCHANGDU）并且不在在（x == 0）时methodJd="+methodJd);
						}

						try {
							methdjd = clsdd.getMethod(methodJd, new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPJ,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPJ,NoSuchMethodException");
						}
						try {
							pxriq = methdjd.invoke(dingdlj, null).toString();
						} catch (IllegalArgumentException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPJ,IllegalArgumentException");
						} catch (IllegalAccessException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPJ,IllegalAccessException");
						} catch (InvocationTargetException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPJ,InvocationTargetException");
						}
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式pxriq="+pxriq);
						methodJd = Const.GETP + x + "sl";
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式methodJd="+methodJd);
						try {
							methdjd = clsdd.getMethod(methodJd, new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPJ,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPJ,NoSuchMethodException");
						}
						try {
							jissl = new BigDecimal(methdjd.invoke(dingdlj, null).toString());
						} catch (IllegalArgumentException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPJ,IllegalArgumentException");
						} catch (IllegalAccessException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPJ,IllegalAccessException");
						} catch (InvocationTargetException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.yugInDingdljPJ,InvocationTargetException");
						}
						CommonFun.logger.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式jissl="+jissl);
					}
					dingdmx.setUabzlx(dingdlj.getUabzlx());
					dingdmx.setUabzuclx(dingdlj.getUabzuclx());
					dingdmx.setUabzucsl(dingdlj.getUabzucsl());
					dingdmx.setUabzucrl(dingdlj.getUabzucrl());
					dingdmx.setDingdh(dingdlj.getDingdh());
					dingdmx.setUsercenter(dingdlj.getUsercenter());
					dingdmx.setLingjbh(dingdlj.getLingjbh());
					dingdmx.setGongysdm(dingdlj.getGongysdm());
					dingdmx.setGongyslx(dingdlj.getGongyslx());
					dingdmx.setCangkdm(dingdlj.getCangkdm());
					dingdmx.setFahd(dingdlj.getFahd());
					dingdmx.setDinghcj(dingdlj.getDinghcj());
					dingdmx.setDanw(dingdlj.getDanw());
					dingdmx.setLujdm(dingdlj.getLujdm());
					dingdmx.setJihyz(dingdlj.getJihyz());
					dingdmx.setYijfl(dingdlj.getYijfl());
					/////dingdmx.setZhuangt(Const.DINGD_STATUS_DSX);
					////订单明细修改为已生效
					dingdmx.setZhuangt(Const.DINGD_STATUS_YSX);
					dingdmx.setLeix(Const.SHIFOUSHIYUGAO);
					dingdmx.setGonghlx(Const.FEIZHOUQIPJ);
					dingdmx.setActive(Const.ACTIVE_1);
					// 设置要货开始时间
					dingdmx.setYaohqsrq(pxriq);
					// 设置要货结束时间
					dingdmx.setYaohjsrq(pxriq);
					dingdmx.setJiaofrq(pxriq);

					dingdmx.setFayrq(pxriq);
					dingdmx.setShul(jissl);
					dingdmx.setJissl(jissl);
					dingdmx.setCreator(caozz);
					dingdmx.setGcbh(gcbh);
					dingdmx.setCreate_time(CommonFun.getJavaTime());
					CommonFun.objPrint(dingdmx, "行列转换，得到订单零件类集合yugInDingdljPJ方法中dingdmx");
/////////////////////////////////wuyichao 					
					List<Dingdmx> flagMx = saveMingxMap.get(dingdmx.getDingdh());
					if(null == flagMx)
					{
						flagMx = new ArrayList<Dingdmx>();
					}
					flagMx.add(dingdmx);
					saveMingxMap.put(dingdmx.getDingdh(), flagMx);
					//this.dingdmxService.doInsert(dingdmx);
////////////////////////////wuyichao
				}
////////////////////////////wuyichao
				List<Dingdlj> flagLj = saveLingjMap.get(dingdlj.getDingdh());
				if(null == flagLj)
				{
					flagLj = new ArrayList<Dingdlj>();
				}
				flagLj.add(dingdlj);
				saveLingjMap.put(dingdlj.getDingdh(), flagLj);
				//this.dingdljservice.doInsert(dingdlj);
////////////////////////////wuyichao
				Dingd existDd = new Dingd();
				Map<String, String> ddhMap = new HashMap<String, String>();
				ddhMap.put("dingdh", dingdlj.getDingdh());
				existDd = this.dingdservice.queryDingdByDingdh(ddhMap);
				CommonFun.objPrint(existDd, "行列转换，得到订单零件类集合yugInDingdljPJ方法中existDd");
				if (null == existDd) {
					
					Map<String,String> riqMap = new HashMap<String,String>();
					riqMap.put("usercenter", dingdlj.getUsercenter());
					riqMap.put("riq", dingdlj.getZiyhqrq().substring(0,10));
					CalendarCenter centerObject = this.calendarCenterService.queryCalendarCenterObject(riqMap);
					Dingd dd = new Dingd();
					dd.setDingdh(dingdlj.getDingdh());// 订单号
					dd.setHeth(gongyshth);// 合同号
					dd.setDingdlx(Const.DINGDLX_ILORDER);// 国产订单类型
					dd.setGongysdm(dingdlj.getGongysdm());// 供应商编号
					dd.setChullx(dingdlj.getGonghms());
					dd.setDingdzt(Const.DINGD_STATUS_JSZ);
					dd.setDingdjssj(jssj);
					dd.setDingdnr(dingdlj.getDingdnr());
					dd.setZiyhqrq(dingdlj.getZiyhqrq());
					dd.setFahzq(dingdlj.getP0fyzqxh());
					dd.setMaoxqbc(banc);
					dd.setJislx(jihyz);
					dd.setShifyjsyhl(Const.SHIFYJSYHL_Y);
					dd.setShifzfsyhl(Const.SHIFZFSYHL_YES);
					dd.setCreator(caozz);
					dd.setCreate_time(CommonFun.getJavaTime());
					dd.setActive(Const.ACTIVE_0);
					dd.setUsercenter(dingdlj.getUsercenter());
					dd.setJiszq(centerObject.getNianzq());
					CommonFun.objPrint(dd, "行列转换，得到订单零件类集合yugInDingdljPJ方法中dd");
					this.dingdservice.doInsert(dd);
				}
			}
		}
	}
	public BigDecimal backValue(String method,Object obj,int pianysj,String dingdlx){
		CommonFun.logger.debug("反射取值backValue方法参数method="+method);
		CommonFun.logger.debug("反射取值backValue方法参数pianysj="+pianysj);
		CommonFun.logger.debug("反射取值backValue方法参数dingdlx="+dingdlx);
		CommonFun.objPrint(obj, "反射取值backValue方法参数obj");
		BigDecimal value = BigDecimal.ZERO;
		if(null!=obj){
		Class cls = obj.getClass();// 得到Maoxqhzpc类
		Method meth = null;
		int changdu = 0;
		if(dingdlx.equalsIgnoreCase(Const.PP)){
			changdu = Const.ZPCHANGDU;
		}else if(dingdlx.equalsIgnoreCase(Const.PS)){
			changdu = Const.ZSCHANGDU;
		}else if(dingdlx.equalsIgnoreCase(Const.PJ)){
			changdu = Const.ZJCHANGDU;
		}
		if (pianysj > changdu) {
			value = BigDecimal.ZERO;
		} else {

			try {
				meth = cls.getMethod(method, new Class[] {});
			}  catch (NoSuchMethodException e1) {
				CommonFun.logger.error(e1);
				// TODO Auto-generated catch block
				throw new RuntimeException("IlOrderService.backValue,NoSuchMethodException");
				
			}catch (SecurityException e1) {
				CommonFun.logger.error(e1);
				// TODO Auto-generated catch block
				throw new RuntimeException("IlOrderService.backValue,SecurityException");
			}
			try {
				value = new BigDecimal(meth.invoke(obj, null).toString());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.backValue,IllegalArgumentException");
				
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				// TODO Auto-generated catch block
				throw new RuntimeException("IlOrderService.backValue,IllegalAccessException");
				
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				// TODO Auto-generated catch block
				throw new RuntimeException("IlOrderService.backValue,InvocationTargetException");
			
			}
		}
		}
		CommonFun.logger.debug("反射取值backValue方法结果value="+value);
		return value;
	}
	
	
	public void insertDingdMX(Dingdlj dingdlj,Object obj,String zhizlx,int pianysj,String dingdlx,Map<String, String> dingdhMap,BigDecimal jissl,String pxriq,String caozz,String zhouqilx,Map<String ,List<Dingdmx>> saveMingxMap){
		if(null == dingdlj.getDingdh()){
			return ;
		}
		
		Dingdmx dingdmx = new Dingdmx();
		dingdmx.setUabzlx(dingdlj.getUabzlx());
		dingdmx.setUabzuclx(dingdlj.getUabzuclx());
		dingdmx.setUabzucsl(dingdlj.getUabzucsl());
		dingdmx.setUabzucrl(dingdlj.getUabzucrl());
		dingdmx.setDingdh(dingdlj.getDingdh());
		dingdmx.setUsercenter(dingdlj.getUsercenter());
		dingdmx.setLingjbh(dingdlj.getLingjbh());
		dingdmx.setGongysdm(dingdlj.getGongysdm());
		dingdmx.setGongyslx(dingdlj.getGongyslx());
		dingdmx.setCangkdm(dingdlj.getCangkdm());
		dingdmx.setFahd(dingdlj.getFahd());
		dingdmx.setDinghcj(dingdlj.getDinghcj());
		dingdmx.setDanw(dingdlj.getDanw());
		dingdmx.setLujdm(dingdlj.getLujdm());
		dingdmx.setJihyz(dingdlj.getJihyz());
		dingdmx.setYijfl(dingdlj.getYijfl());
		dingdmx.setCreator(caozz);
		dingdmx.setCreate_time(CommonFun.getJavaTime());
		dingdmx.setEditor(caozz);
		dingdmx.setEdit_time(CommonFun.getJavaTime());
		dingdmx.setXiehzt(dingdlj.getXiehzt());
		dingdmx.setZhuangt(Const.DINGD_STATUS_DSX);
		
		if(zhouqilx.equals(Const.SHIFOUSHIJIDING)){
			dingdmx.setLeix(Const.SHIFOUSHIJIDING);
		}else if(zhouqilx.equals(Const.SHIFOUSHIYUGAO)){
			dingdmx.setLeix(Const.SHIFOUSHIYUGAO);
		}
		dingdmx.setActive(Const.ACTIVE_1);
		dingdmx.setShul(jissl);
		dingdmx.setJissl(jissl);
		dingdmx.setCreator(caozz);
		dingdmx.setCreate_time(CommonFun.getJavaTime());
		if(dingdlx.equalsIgnoreCase(Const.PP)){
			Maoxqhzpc maoxqhzpc = (Maoxqhzpc)obj;	
			dingdmx.setGcbh(maoxqhzpc.getGcbh());
			if (zhizlx.equals(Const.ZHOUQIGONGHUOMOSHI_IL_JUANLIAO)) {
				String dingdh = dingdhMap.get(maoxqhzpc.getGongysdm()+maoxqhzpc.getUsercenter());
				dingdmx.setDingdh(dingdh);
			} else {
				dingdmx.setDingdh(dingdlj.getDingdh());// 订单号
			}
			dingdmx.setLingjmc(maoxqhzpc.getLingjmc());
			dingdmx.setGongsmc(maoxqhzpc.getGongsmc());
			dingdmx.setNeibyhzx(maoxqhzpc.getNeibyhzx());
			dingdmx.setGonghlx(Const.PM);
			String nianzq = "";
			if (pianysj == 0) {
				nianzq = dingdlj.getP0fyzqxh();
			} else {
				nianzq = this.calendarCenterService.nianzqGroup(dingdlj.getP0fyzqxh(), pianysj,
						dingdlj.getUsercenter()).get(pianysj);
			}
			Gongyzq gongyzq = this.gongyzqService.queryGongyzq(nianzq);
			if (null != gongyzq) {
				// 设置要货开始时间
				dingdmx.setYaohqsrq(gongyzq.getKaissj());
				// 设置要货结束时间
				dingdmx.setYaohjsrq(gongyzq.getJiessj());
				dingdmx.setJiaofrq(gongyzq.getJiessj());
				dingdmx.setFayrq(gongyzq.getKaissj());
			}
			
		}else if(dingdlx.equalsIgnoreCase(Const.PS)){
			Maoxqhzsc maoxqhzsc = (Maoxqhzsc)obj;
			dingdmx.setGcbh(maoxqhzsc.getGcbh());
			dingdmx.setLingjmc(maoxqhzsc.getLingjmc());
			dingdmx.setGongsmc(maoxqhzsc.getGongsmc());
			dingdmx.setNeibyhzx(maoxqhzsc.getNeibyhzx());
			dingdmx.setGonghlx(Const.SM);
			String nianzx = "";
			if (pianysj == 0) {
				nianzx = dingdlj.getP0fyzqxh();
			} else {

				nianzx = this.calendarCenterService.nianzxGroup(dingdlj.getP0fyzqxh(), pianysj,
						dingdlj.getUsercenter()).get(pianysj);
			}
			Gongyzx gongyzx = this.gongyzqService.queryGongyzx(nianzx);
			if (null != gongyzx) {
				// 设置要货开始时间
				dingdmx.setYaohqsrq(gongyzx.getKaissj());
				// 设置要货结束时间
				dingdmx.setYaohjsrq(gongyzx.getJiessj());
				dingdmx.setJiaofrq(gongyzx.getJiessj());
				dingdmx.setFayrq(gongyzx.getKaissj());
			}
		}else if(dingdlx.equalsIgnoreCase(Const.PJ)){
			Maoxqhzjc maoxqhzjc = (Maoxqhzjc)obj;
			dingdmx.setGcbh(maoxqhzjc.getGcbh());
			dingdmx.setGonghlx(Const.JM);
			dingdmx.setLingjmc(maoxqhzjc.getLingjmc());
			dingdmx.setGongsmc(maoxqhzjc.getGongsmc());
			dingdmx.setNeibyhzx(maoxqhzjc.getNeibyhzx());
			// 设置要货开始时间
			dingdmx.setYaohqsrq(pxriq);
			// 设置要货结束时间
			dingdmx.setYaohjsrq(pxriq);
			dingdmx.setJiaofrq(pxriq);
			dingdmx.setFayrq(pxriq);
			//订单明细状态变成已发送
			dingdmx.setZhuangt(Const.DINGD_STATUS_YSX);
		}
///////////////////////wuyichao///////////////
			String dingdh = dingdmx.getDingdh();
			List<Dingdmx> flagDingdmx = saveMingxMap.get(dingdh);
			if(null == flagDingdmx )
			{
				flagDingdmx = new ArrayList<Dingdmx>();
			}
			flagDingdmx.add(dingdmx);
			saveMingxMap.put(dingdh, flagDingdmx);
///////////////////////wuyichao///////////////
	}
	
	public void insertdingd (Dingdlj dingdlj,Object obj,String dingdlx,String jssj,String banch,String jihyz,String caozz){
		if(null == dingdlj.getDingdh()){
			return ;
		}
		Map<String,String> riqMap = new HashMap<String,String>();
		riqMap.put("usercenter", dingdlj.getUsercenter());
		riqMap.put("riq", dingdlj.getZiyhqrq().substring(0,10));
		CalendarCenter centerObject = this.calendarCenterService.queryCalendarCenterObject(riqMap);
		Dingd dd = new Dingd();
		dd.setDingdh(dingdlj.getDingdh());// 订单号	
		dd.setDingdlx(Const.DINGDLX_ILORDER);// 国产订单类型
		dd.setGongysdm(dingdlj.getGongysdm());// 供应商编号
		dd.setDingdzt(Const.DINGD_STATUS_JSZ);
		dd.setDingdjssj(jssj);
		dd.setDingdnr(dingdlj.getDingdnr());
		dd.setZiyhqrq(dingdlj.getZiyhqrq());
		dd.setFahzq(dingdlj.getP0fyzqxh());
		dd.setMaoxqbc(banch);
		dd.setJislx(jihyz);
		dd.setShifyjsyhl(Const.SHIFYJSYHL_N);
		dd.setShifzfsyhl(Const.SHIFZFSYHL_YES);
		dd.setCreator(caozz);
		dd.setCreate_time(CommonFun.getJavaTime());
		dd.setActive(Const.ACTIVE_0);
		
		if(dingdlx.equalsIgnoreCase(Const.PP)){
			Maoxqhzpc maoxqhzpc = (Maoxqhzpc)obj;
			if (maoxqhzpc.getZhizlx().equals(Const.ZHIZAOLUXIAN_IL)) {
				dd.setHeth(maoxqhzpc.getGongyhth());// 合同号
				dd.setUsercenter(maoxqhzpc.getUsercenter());
				dd.setChullx(Const.PP);
				dd.setJiszq(centerObject.getNianzq());	
			} else if (maoxqhzpc.getZhizlx().equals(Const.ZHIZAOLUXIAN_GL)) {
				if (null != dingdlj.getDingdh()) {
					//dingdljservice.doInsert(dingdlj);
					Dingd gjdd = new Dingd();
					gjdd.setDingdzt(Const.DINGD_STATUS_JSZ);
					gjdd.setDingdjssj(jssj);
					gjdd.setFahzq(dingdlj.getP0fyzqxh());
					gjdd.setMaoxqbc(banch);
					gjdd.setJislx(jihyz);
					gjdd.setShifyjsyhl(Const.SHIFYJSYHL_N);
					gjdd.setShifzfsyhl(Const.SHIFZFSYHL_YES);
					gjdd.setCreator(jihyz);
					gjdd.setCreate_time(CommonFun.getJavaTime());
					gjdd.setActive(Const.ACTIVE_1);
					gjdd.setDingdh(dingdlj.getDingdh());
					CommonFun.objPrint(gjdd, "国产订单插入时的gjdd");
					this.dingdservice.doUpdate(gjdd);
				}
		  }
		}else if(dingdlx.equalsIgnoreCase(Const.PS)){
			Maoxqhzsc maoxqhzsc = (Maoxqhzsc)obj;
			dd.setHeth(maoxqhzsc.getGongyhth());// 合同号
			dd.setChullx(Const.PS);
			dd.setUsercenter(maoxqhzsc.getUsercenter());
			dd.setJiszq(centerObject.getNianzx());
		}else if(dingdlx.equalsIgnoreCase(Const.PJ)){
			Maoxqhzjc maoxqhzjc = (Maoxqhzjc)obj;
			dd.setHeth(maoxqhzjc.getGongyhth());// 合同号
			dd.setChullx(Const.PJ);
			dd.setUsercenter(maoxqhzjc.getUsercenter());
			dd.setJiszq(dingdlj.getZiyhqrq().substring(0,10));
		}
			Dingd existDd = new Dingd();
			Map<String, String> ddhMap = new HashMap<String, String>();
			ddhMap.put("dingdh", dingdlj.getDingdh());
			existDd = this.dingdservice.queryDingdByDingdh(ddhMap);
			CommonFun.objPrint(dd, "国产订单插入时的dd");
			if (null == existDd) {
				this.dingdservice.doInsert(dd);
			}
			
	}
	public String ppMaoxqHz(String message,String ziyhqrq,String zhizlx,List<Dingd> dingdList,String jihyz,LoginUser loginuser){
		String dingdnr = "";
		List list = this.maoxqhzpcService.selectInto(ziyhqrq);// 把毛需求汇总周期表的需求汇总到仓库并关联参考系表
		//CommonFun.objListPrint(list, "PP模式毛需求-参考系汇总结果list");
		if (list.size() > 0) {
			if (zhizlx.endsWith(Const.ZHIZAOLUXIAN_IL)) {
				Map<String, String> map = new HashMap<String, String>();
				Maoxqhzpc mzpc = (Maoxqhzpc) list.get(0);
				map.put("usercenter", mzpc.getUsercenter());
				map.put("riq", ziyhqrq);
				CommonFun.mapPrint(map, "PP模式国产件毛需求-参考系汇总资源获取日期所在周期查询参数map");
				CalendarCenter calendarCenterObject = this.calendarCenterService.queryCalendarCenterObject(map);
				if(null == calendarCenterObject){
					message ="PP模式国产件毛需求-参考系汇总资源获取日期所在周期查询参数为空。";
					return message;
				}
				String suoszq = calendarCenterObject.getNianzq().substring(4);
				CommonFun.logger.debug("PP模式毛国产件需求-参考系汇总资源获取日期所在周期suoszq："+suoszq);
				
				dingdnr = this.jdygzqservice.queryDingdnr(zhizlx, suoszq);
				CommonFun.logger.debug("PP模式国产件毛需求-参考系汇总订单内容为："+dingdnr);
				if (null == dingdnr) {
					message =zhizlx+"订货路线下"+suoszq+ "周期内的既定-预告-周期表订单内容字段为空。";
					return message;
				}
			} else if (zhizlx.endsWith(Const.ZHIZAOLUXIAN_GL)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("dingdh", dingdList.get(0).getDingdh());
				CommonFun.logger.debug("PP模式卷料毛需求-参考系汇总根据订单号查询订单信息参数dingdh为："+dingdList.get(0).getDingdh());
				Dingd dd = this.dingdservice.queryDingdByDingdh(map);
				CommonFun.objPrint(dd, "PP模式卷料毛需求-参考系汇总根据订单号查询订单信息dd");
				dingdnr = this.jdygzqservice.queryDingdnr(zhizlx, dd.getJiszq().substring(4));
				CommonFun.logger.debug("PP模式卷料毛需求-参考系汇总订单内容为："+dingdnr);
				if (null == dingdnr) {
					message = zhizlx+"订货路线下"+dd.getJiszq()+ "周期内的既定-预告-周期表订单内容字段为空。";
					return message;
				}
			}

		}else{
			message = "对应的毛需求汇总_参考系表表数据为空.";
			return message;
		}
//////////////wuyichao 2014/02/20 将资源快照表内的数据存入内存 //////////////////////////////
		Map<String, BigDecimal> xianbKcMap = new HashMap<String, BigDecimal>();
		Map<String, Integer> xianbCountMap = new HashMap<String, Integer>();
		Map<String, BigDecimal> dinghKcMap = new HashMap<String, BigDecimal>();
		Map<String, Integer> dinghCountMap = new HashMap<String, Integer>();
		//初始化map
		initKucMapAndCountMap(queryAllKucByXianbck(ziyhqrq), xianbKcMap, xianbCountMap);
		initKucMapAndCountMap(queryAllKucByDinghck(ziyhqrq), dinghKcMap, dinghCountMap);
//////////////wuyichao 2014/02/20 将资源快照表内的数据存入内存 //////////////////////////////	
		List<Maoxqhzpc> rsList = new ArrayList<Maoxqhzpc>();
		for (int i = 0; i < list.size(); i++) {
			
			Maoxqhzpc maoxqhzpc = (Maoxqhzpc) list.get(i);// 把汇总的数据插入到毛需求汇总参考系表中
			maoxqhzpc.setDingdnr(dingdnr);
			if (zhizlx.endsWith(Const.ZHIZAOLUXIAN_IL)) {
				BigDecimal daixh = BigDecimal.ZERO;
				if(maoxqhzpc.getBeihzq()!=null&&maoxqhzpc.getFayzq()!=null){
				int fengbiqi = (maoxqhzpc.getBeihzq().add(maoxqhzpc.getFayzq())).divide(new BigDecimal(30), 0,
						BigDecimal.ROUND_UP).intValue();
				Integer workDay =  this.calendarversionservice.countDay(maoxqhzpc.getUsercenter(),
						maoxqhzpc.getCangkdm(), maoxqhzpc.getP0zqxh());
				CommonFun.logger.debug("PP模式国产件毛需求-参考系汇总计算周期内工作天数为："+workDay);
				Integer lastDay = this.calendarversionservice.countLastDay(maoxqhzpc.getUsercenter(),
						maoxqhzpc.getCangkdm(), maoxqhzpc.getP0zqxh(), maoxqhzpc.getZiyhqrq());
				CommonFun.logger.debug("PP模式毛国产件需求-参考系汇总计算周期内剩下的工作天数为："+lastDay);
				if (workDay > 0)
				{
					if(null != maoxqhzpc.getP0())
					{
						daixh = maoxqhzpc.getP0().divide(new BigDecimal(workDay), 3, BigDecimal.ROUND_UP).multiply(new BigDecimal(lastDay));
					}
				}
				CommonFun.logger.debug("PP模式毛国产件需求-参考系汇总计算周期内剩下的daixh="+daixh);
				BigDecimal sumXuq = BigDecimal.ZERO;
				for(int x = 1; x < fengbiqi; x++){
					String method = Const.GETP + x;// 拼接getPi方法
					Class cls = maoxqhzpc.getClass();// 得到Maoxqhzpc类
					Method meth = null;
					BigDecimal xuq = BigDecimal.ZERO;
					if (x > Const.ZPCHANGDU) {
						xuq = BigDecimal.ZERO;
					} else {

						try {
							meth = cls.getMethod(method, new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.ppMaoxqHz,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.ppMaoxqHz,NoSuchMethodException");
						}// 得到Maoxqhzpc类的method拼接的方法
						try {
							xuq = new BigDecimal(meth.invoke(maoxqhzpc, null).toString());
							CommonFun.logger.debug("PP模式卷料毛需求-参考系汇需求为："+xuq);
						} catch (IllegalArgumentException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.ppMaoxqHz,IllegalArgumentException");
						} catch (IllegalAccessException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.ppMaoxqHz,IllegalAccessException");
						} catch (InvocationTargetException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException("IlOrderService.ppMaoxqHz,InvocationTargetException");
						}// 执行得到的方法，取得既定周期的数量
					}
					sumXuq = sumXuq.add(xuq);
					CommonFun.logger.debug("PP模式普通零件毛需求-参考系汇需求汇总为："+sumXuq);		
				}
				    daixh = daixh.add(sumXuq);
				}
					maoxqhzpc.setDaixh(daixh);

			} else if (zhizlx.endsWith(Const.ZHIZAOLUXIAN_GL)) {
				Map<String, String> map = new HashMap<String, String>();

				map.put("usercenter", maoxqhzpc.getUsercenter());
				map.put("riq", ziyhqrq);
				CalendarCenter calendarCenterObject = this.calendarCenterService.queryCalendarCenterObject(map);
				map.clear();
				map.put("usercenter", maoxqhzpc.getUsercenter());
				map.put("nianzq", calendarCenterObject.getNianzq());
				List<CalendarCenter> list_zhouqiriq = this.calendarcenterservice.getStartandEndDay(map);
				String kais = list_zhouqiriq.get(0).getRiq();
				CommonFun.logger.debug("PP模式卷料毛需求-参考系汇总开始时间为："+kais);
				String jies = ziyhqrq;
				CommonFun.logger.debug("PP模式卷料毛需求-参考系汇总结束时间为："+jies);
				BigDecimal chuk = this.wzklszservice.sumChuk(Const.WZKCZM, maoxqhzpc.getLingjbh(), kais, jies,maoxqhzpc.getCangkdm(),maoxqhzpc.getUsercenter());
				CommonFun.logger.debug("PP模式卷料毛需求-参考系汇总出库汇总为："+chuk);
				BigDecimal daixh = BigDecimal.ZERO;
				if(null != maoxqhzpc.getBeihzq() && null != maoxqhzpc.getFayzq())
				{
					int fengbiqi = (maoxqhzpc.getBeihzq().add(maoxqhzpc.getFayzq())).divide(new BigDecimal(30), 0,
							BigDecimal.ROUND_UP).intValue();
					CommonFun.logger.debug("PP模式卷料毛需求-参考系汇总封闭期为："+fengbiqi);
					BigDecimal sumXuq = BigDecimal.ZERO;
					for (int x = 0; x < fengbiqi; x++) {
						
						String method = Const.GETP + x;// 拼接getPi方法
						Class cls = maoxqhzpc.getClass();// 得到Maoxqhzpc类
						Method meth = null;
						BigDecimal xuq = BigDecimal.ZERO;
						if (x > Const.ZPCHANGDU) {
							xuq = BigDecimal.ZERO;
						} else {
							
							try {
								meth = cls.getMethod(method, new Class[] {});
							} catch (SecurityException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.ppMaoxqHz,SecurityException");
							} catch (NoSuchMethodException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.ppMaoxqHz,NoSuchMethodException");
							}// 得到Maoxqhzpc类的method拼接的方法
							try {
								xuq = new BigDecimal(meth.invoke(maoxqhzpc, null).toString());
								CommonFun.logger.debug("PP模式卷料毛需求-参考系汇需求为："+xuq);
							} catch (IllegalArgumentException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.ppMaoxqHz,IllegalArgumentException");
							} catch (IllegalAccessException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.ppMaoxqHz,IllegalAccessException");
							} catch (InvocationTargetException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException("IlOrderService.ppMaoxqHz,InvocationTargetException");
							}// 执行得到的方法，取得既定周期的数量
						}
						sumXuq = sumXuq.add(xuq);
						CommonFun.logger.debug("PP模式卷料毛需求-参考系汇需求汇总为："+sumXuq);
					}
					CommonFun.logger.debug("PP模式卷料毛需求-参考系汇需求汇总减去出库汇总为："+sumXuq+"-"+chuk);
					daixh = sumXuq.subtract(chuk);
					CommonFun.logger.debug("PP模式卷料毛需求-参考系汇总待消耗为："+daixh);
				}
				maoxqhzpc.setDaixh(daixh);
				map.clear();
			}
			CommonFun.logger.debug("PP模式毛需求-参考系汇总仓库类型为："+maoxqhzpc.getCangklx());
		/*	if (maoxqhzpc.getCangklx().equals("0")) {
				BigDecimal dbkc = BigDecimal.ZERO;
				 dbkc = this.maoxqhzpcService.selectKuc(maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(),
						maoxqhzpc.getCangkdm(), ziyhqrq,jihyz,loginuser);
				 CommonFun.logger.debug("PP模式毛需求-参考系汇总库存为："+dbkc);
				maoxqhzpc.setKuc(dbkc);
			} else if (maoxqhzpc.getCangklx().equals("1")) {
				BigDecimal dbkc = BigDecimal.ZERO;
				dbkc =  this.maoxqhzpcService.selectDingKuc(maoxqhzpc.getUsercenter(),
						maoxqhzpc.getLingjbh(), maoxqhzpc.getCangkdm(), ziyhqrq,jihyz,loginuser);
				 CommonFun.logger.debug("PP模式毛需求-参考系汇总库存为："+dbkc);
				maoxqhzpc.setKuc(dbkc);
			}
			*/
			
			/* 如果既有订货库 又有线边库 ， 2个仓库代码也不同 ， 还是按照 订货仓库 :1  为默认值   20170116*/
			//xss_0013126
			
			String key = maoxqhzpc.getUsercenter() + maoxqhzpc.getLingjbh() +	maoxqhzpc.getCangkdm();
			if (maoxqhzpc.getCangklx().equals("0")) {
				Integer count = xianbCountMap.get(key);
				if(null == count || count <= 1)
				{
					maoxqhzpc.setKuc(xianbKcMap.get(key) == null ? BigDecimal.ZERO : xianbKcMap.get(key));
				}
				else
				{
					String paramStr[] = new String []{maoxqhzpc.getUsercenter(), ziyhqrq,maoxqhzpc.getLingjbh(),maoxqhzpc.getCangkdm()};
					yicbjservice.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str8, jihyz, paramStr,maoxqhzpc.getUsercenter() , maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
					continue;
				}
			} else if (maoxqhzpc.getCangklx().equals("1")) { 
				//Integer count = dinghCountMap.get(key);
				//xss_0013126 按照 仓库代码 取资源
				Integer count = xianbCountMap.get(key);
				if(null == count || count <= 1)
				{
					maoxqhzpc.setKuc(xianbKcMap.get(key) == null ? BigDecimal.ZERO : xianbKcMap.get(key)); 
					//maoxqhzpc.setKuc(dinghKcMap.get(key) == null ? BigDecimal.ZERO : dinghKcMap.get(key));
				}
				else
				{
					String paramStr[] = new String []{maoxqhzpc.getUsercenter(), ziyhqrq,maoxqhzpc.getLingjbh(),maoxqhzpc.getCangkdm()};
					yicbjservice.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str8, jihyz, paramStr,maoxqhzpc.getUsercenter() , maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
					continue;
				}
				
			}
			maoxqhzpc.setAnqkc(null == maoxqhzpc.getAnqkc() ? BigDecimal.ZERO : maoxqhzpc.getAnqkc());
			
			if (null == maoxqhzpc.getAnqkc()) {
				maoxqhzpc.setAnqkc(BigDecimal.ZERO);
			}
			if (null == maoxqhzpc.getKuc()) {
				maoxqhzpc.setKuc(BigDecimal.ZERO);
			}
			if (null == maoxqhzpc.getTiaozjsz()) {
				maoxqhzpc.setTiaozjsz(BigDecimal.ZERO);
			}
			
			if (null == maoxqhzpc.getDingdlj()) {
				maoxqhzpc.setDingdlj(BigDecimal.ZERO);
			}
			if (null == maoxqhzpc.getJiaoflj()) {
				maoxqhzpc.setJiaoflj(BigDecimal.ZERO);
			}
			if (null == maoxqhzpc.getXittzz()) {
				maoxqhzpc.setXittzz(BigDecimal.ZERO);
			}
			if (null == maoxqhzpc.getDingdzzlj()) {
				maoxqhzpc.setDingdzzlj(BigDecimal.ZERO);
			}
			CommonFun.objPrint(maoxqhzpc, "PP模式毛需求-参考系汇总maoxqhzpc类");
			rsList.add(maoxqhzpc);
		//this.maoxqhzpcService.doInsert(maoxqhzpc);
		}
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertMaoxqhzpc", rsList);
		return message;
	}
	public String psMaoxqHz(String message,String ziyhqrq,String zhizlx,List<Dingd> dingdList,String jihyz,LoginUser loginuser){
		String dingdnr = "";
		List list = this.maoxqhzscService.selectInto(ziyhqrq);// 把毛需求汇总周表的需求汇总到仓库并关联参考系表
		//CommonFun.objListPrint(list, "PS模式毛需求-参考系汇总结果LIST");
		
		
		if(list.size()>0){
			Map<String, String> map = new HashMap<String, String>();
			Maoxqhzsc mzsc = (Maoxqhzsc) list.get(0);
			map.put("usercenter", mzsc.getUsercenter());
			map.put("riq", ziyhqrq);
			CalendarCenter calendarCenterObject = this.calendarCenterService.queryCalendarCenterObject(map);
			if(null == calendarCenterObject)
			{
				message = "PS模式国产件毛需求-参考系汇总所属周期为空。";
				return message;
			}
			String suoszq = calendarCenterObject.getNianzq().substring(4);
			CommonFun.logger.debug("PS模式国产件毛需求-参考系汇总所属周期为："+suoszq);
			dingdnr = this.jdygzqservice.queryDingdnr(zhizlx, suoszq);
			CommonFun.logger.debug("PS模式国产件毛需求-参考系汇总订单内容为："+dingdnr);
			if (null == dingdnr) {

				message = zhizlx+"订货路线下"+suoszq+ "既定-预告-周期表订单内容字段为空。";
				return message;
			}
		}else{
			message = "对应的毛需求汇总_参考系表表数据为空.";
			return message;
		}
		///////Key 为banc  (usercenter + cangkdm + nianzx)
		List<CalendarVersion> workDayList = this.calendarversionservice.pscountWeekDay();
		List<CalendarVersion> lastDayList = this.calendarversionservice.pscountLastWeekDay(ziyhqrq);
		Map<String,BigDecimal> workDayMap = getPsCalendarVersionMap(workDayList);
		Map<String,BigDecimal> lastDayMap = getPsCalendarVersionMap(lastDayList);
		List<Maoxqhzsc> maoxqhzscs = new ArrayList<Maoxqhzsc>();
		
		for (int i = 0; i < list.size(); i++) {
			Maoxqhzsc maoxqhzsc = (Maoxqhzsc) list.get(i);// 把汇总的数据插入到毛需求汇总参考系表中
			Integer workDay = 0;
			Integer lastDay = 0;
			//Integer workDay = this.calendarversionservice.countWeekDay(maoxqhzsc.getUsercenter(),
			//		maoxqhzsc.getCangkdm(), maoxqhzsc.getS0zxh());
			BigDecimal flagWorkDay = workDayMap.get(maoxqhzsc.getUsercenter() + maoxqhzsc.getCangkdm() + maoxqhzsc.getS0zxh());
			if(null != flagWorkDay)
			{
				workDay = flagWorkDay.intValue();
			}
			CommonFun.logger.debug("PS模式国产件毛需求-参考系汇总计算周期内工作天数为："+workDay);
			//Integer lastDay = this.calendarversionservice.countLastWeekDay(maoxqhzsc.getUsercenter(),
			//		maoxqhzsc.getCangkdm(), maoxqhzsc.getS0zxh(), maoxqhzsc.getZiyhqrq());
			BigDecimal flagLastDay = lastDayMap.get(maoxqhzsc.getUsercenter() + maoxqhzsc.getCangkdm() + maoxqhzsc.getS0zxh());
			if(null != flagLastDay)
			{
				lastDay = flagLastDay.intValue();
			}
			CommonFun.logger.debug("PS模式国产件毛需求-参考系汇总计算周期内所剩工作天数为："+lastDay);
			if (workDay == 0) {
				// maoxqhzsc.setAnqkc(BigDecimal.ZERO);
				maoxqhzsc.setDaixh(BigDecimal.ZERO);
				CommonFun.logger.debug("PS模式国产件毛需求-参考系汇总待消耗为："+BigDecimal.ZERO);
			} else {
				BigDecimal daixh = maoxqhzsc.getS0().divide(new BigDecimal(workDay), 3, BigDecimal.ROUND_UP)
						.multiply(new BigDecimal(lastDay));
				CommonFun.logger.debug("PS模式国产件毛需求-参考系汇总待消耗为："+daixh);
				maoxqhzsc.setDaixh(daixh);
				// BigDecimal anqkc = maoxqhzsc.getS0().divide(new
				// BigDecimal(workDay)).multiply(maoxqhzsc.getAnqkc());
				// maoxqhzsc.setAnqkc(anqkc);
			}
			CommonFun.logger.debug("PS模式国产件毛需求-参考系汇总仓库类型为："+maoxqhzsc.getCangklx());
			if (maoxqhzsc.getCangklx().equals("0")) {
				maoxqhzsc.setKuc(this.maoxqhzpcService.selectKuc(maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(),
						maoxqhzsc.getCangkdm(), ziyhqrq,jihyz,loginuser));
				CommonFun.logger.debug("PS模式国产件毛需求-参考系汇总库存为："+maoxqhzsc.getKuc());
			} else if (maoxqhzsc.getCangklx().equals("1")) {
				maoxqhzsc.setKuc(this.maoxqhzpcService.selectDingKuc(maoxqhzsc.getUsercenter(),
						maoxqhzsc.getLingjbh(), maoxqhzsc.getCangkdm(), ziyhqrq,jihyz,loginuser));
				CommonFun.logger.debug("PS模式国产件毛需求-参考系汇总库存为："+maoxqhzsc.getKuc());
			}
			if (null == maoxqhzsc.getAnqkc()) {
				maoxqhzsc.setAnqkc(BigDecimal.ZERO);
			}
			if (null == maoxqhzsc.getKuc()) {
				maoxqhzsc.setKuc(BigDecimal.ZERO);
			}
			if (null == maoxqhzsc.getTiaozjsz()) {
				maoxqhzsc.setTiaozjsz(BigDecimal.ZERO);
			}
			if (null == maoxqhzsc.getDingdlj()) {
				maoxqhzsc.setDingdlj(BigDecimal.ZERO);
			}
			if (null == maoxqhzsc.getJiaoflj()) {
				maoxqhzsc.setJiaoflj(BigDecimal.ZERO);
			}
			if (null == maoxqhzsc.getXittzz()) {
				maoxqhzsc.setXittzz(BigDecimal.ZERO);
			}
			if (null == maoxqhzsc.getDingdzzlj()) {
				maoxqhzsc.setDingdzzlj(BigDecimal.ZERO);
			}
			maoxqhzsc.setDingdnr(dingdnr);
			maoxqhzsc.setId(getUUID());
			//CommonFun.objPrint(maoxqhzsc, "PP模式毛需求-参考系汇总maoxqhzsc类");
			//this.maoxqhzscService.doInsert(maoxqhzsc);
			maoxqhzscs.add(maoxqhzsc);
		}
		if(maoxqhzscs.size() > 0)
		{
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertMaoxqhzsc", maoxqhzscs);
		}
		return message;
	}
	
	
	
	
	public String pjMaoxqHz(String message,String ziyhqrq,String zhizlx,List<Dingd> dingdList,String jihyz,LoginUser loginuser){
		String dingdnr = "";
		List list = this.maoxqhzjcService.proMaoxqhzjc(ziyhqrq);// 把毛需求汇总日表的需求汇总到仓库并关联参考系表，并将数据插入到数据库中
		//CommonFun.objListPrint(list, "PJ模式毛需求-参考系汇总结果LIST");
		if(list.size()>0){
			Map<String, String> map = new HashMap<String, String>();
			Maoxqhzjc mzjc = (Maoxqhzjc) list.get(0);
			map.put("usercenter", mzjc.getUsercenter());
			map.put("riq", ziyhqrq);
			CalendarCenter calendarCenterObject = this.calendarCenterService.queryCalendarCenterObject(map);
			String suoszq = calendarCenterObject.getNianzq().substring(4);
			CommonFun.logger.debug("PJ模式毛需求-参考系汇总所属周期为："+suoszq);
			dingdnr = this.jdygzqservice.queryDingdnr(zhizlx, suoszq);
			CommonFun.logger.debug("PJ模式毛需求-参考系汇总订单内容为："+dingdnr);
			if (null == dingdnr) {
				message = zhizlx+"订货路线下"+suoszq+ "既定-预告-周期表订单内容字段为空。";
				return message;
			}
		}else{
			message = "对应的毛需求汇总_参考系表表数据为空.";
			return message;
		}
//////////////wuyichao 2014/02/20 将资源快照表内的数据存入内存 //////////////////////////////
		Map<String, BigDecimal> xianbKcMap = new HashMap<String, BigDecimal>();
		Map<String, Integer> xianbCountMap = new HashMap<String, Integer>();
		Map<String, BigDecimal> dinghKcMap = new HashMap<String, BigDecimal>();
		Map<String, Integer> dinghCountMap = new HashMap<String, Integer>();
		//初始化map
		initKucMapAndCountMap(queryAllKucByXianbck(ziyhqrq), xianbKcMap, xianbCountMap);
		initKucMapAndCountMap(queryAllKucByDinghck(ziyhqrq), dinghKcMap, dinghCountMap);
//////////////wuyichao 2014/02/20 将资源快照表内的数据存入内存 //////////////////////////////	
		
		List<Maoxqhzjc> rsList = new ArrayList<Maoxqhzjc>();
		for (int i = 0; i < list.size(); i++) {
			Maoxqhzjc maoxqhzjc = (Maoxqhzjc) list.get(i);// 把汇总的数据插入到毛需求汇总参考系表中
			
			int pianysj = maoxqhzjc.getBeihzq().add(maoxqhzjc.getFayzq())
					.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_UP).intValue();
			CommonFun.logger.debug("PJ模式毛需求-参考系汇总偏移时间为："+pianysj);
			BigDecimal daixh = BigDecimal.ZERO;
			for (int x = 0; x < pianysj; x++) {
				Method meth = null;
				try {
				if(x<=15){
				String method = Const.GETJ + x;// 拼接getPi方法
				Class cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
				
				meth = cls.getMethod(method, new Class[] {});
				}
				} catch (SecurityException e1) {
					CommonFun.logger.error(e1);
					throw new RuntimeException("IlOrderService.pjMaoxqHz,SecurityException");
				} catch (NoSuchMethodException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.pjMaoxqHz,NoSuchMethodException");
				}// 得到Maoxqhzpc类的method拼接的方法
				try {
					if(x>15){
						daixh = daixh.add(BigDecimal.ZERO);
					}else{
					daixh =daixh.add( new BigDecimal(meth.invoke(maoxqhzjc, null).toString()));
					}
					
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.pjMaoxqHz,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.pjMaoxqHz,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException("IlOrderService.pjMaoxqHz,InvocationTargetException");
				}// 执行得到的方法，取得既定周期的数量
			}
			CommonFun.logger.debug("PJ模式毛需求-参考系汇总待消耗为："+daixh);
			maoxqhzjc.setDaixh(daixh);
			String key = maoxqhzjc.getUsercenter() + maoxqhzjc.getLingjbh() +	maoxqhzjc.getCangkdm();
			if (maoxqhzjc.getCangklx().equals("0")) {
				//maoxqhzjc.setKuc(this.maoxqhzpcService.selectKuc(maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
				//		maoxqhzjc.getCangkdm(), ziyhqrq,jihyz,loginuser));
				Integer count = xianbCountMap.get(key);
				if(null == count || count <= 1)
				{
					maoxqhzjc.setKuc(xianbKcMap.get(key) == null ? BigDecimal.ZERO : xianbKcMap.get(key));
				}
				else
				{
					String paramStr[] = new String []{maoxqhzjc.getUsercenter(), ziyhqrq,maoxqhzjc.getLingjbh(),maoxqhzjc.getCangkdm()};
					yicbjservice.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str8, jihyz, paramStr,maoxqhzjc.getUsercenter() , maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
					continue;
				}
			} else if (maoxqhzjc.getCangklx().equals("1")) {
				//maoxqhzjc.setKuc(this.maoxqhzpcService.selectDingKuc(maoxqhzjc.getUsercenter(),
				//		maoxqhzjc.getLingjbh(), maoxqhzjc.getCangkdm(), ziyhqrq,jihyz,loginuser));
				Integer count = dinghCountMap.get(key);
				if(null == count || count <= 1)
				{
					maoxqhzjc.setKuc(dinghKcMap.get(key) == null ? BigDecimal.ZERO : dinghKcMap.get(key));
				}
				else
				{
					String paramStr[] = new String []{maoxqhzjc.getUsercenter(), ziyhqrq,maoxqhzjc.getLingjbh(),maoxqhzjc.getCangkdm()};
					yicbjservice.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str8, jihyz, paramStr,maoxqhzjc.getUsercenter() , maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
					continue;
				}
				
			}
			if (null == maoxqhzjc.getAnqkc()) {
				maoxqhzjc.setAnqkc(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getKuc()) {
				maoxqhzjc.setKuc(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getTiaozjsz()) {
				maoxqhzjc.setTiaozjsz(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getDingdlj()) {
				maoxqhzjc.setDingdlj(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getJiaoflj()) {
				maoxqhzjc.setJiaoflj(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getXittzz()) {
				maoxqhzjc.setXittzz(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getDingdzzlj()) {
				maoxqhzjc.setDingdzzlj(BigDecimal.ZERO);
			}
			maoxqhzjc.setDingdnr(dingdnr);
			CommonFun.objPrint(maoxqhzjc, "PP模式毛需求-参考系汇总maoxqhzjc类");
			//this.maoxqhzjcService.doInsert(maoxqhzjc);
			rsList.add(maoxqhzjc);
		}
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertMaoxqhzjc", rsList);
		return message;
	}
	
	public Integer check (LoginUser loginuser,String dingdlx){
		Integer count = 0 ;
		if(dingdlx.equalsIgnoreCase(Const.PP)){
			count = this.ppCheck(loginuser);
		}
		if(dingdlx.equalsIgnoreCase(Const.PS)){
			count = this.psCheck(loginuser);
		}
		if(dingdlx.equalsIgnoreCase(Const.PJ)){
			count = this.pjCheck(loginuser);
		}
		return count;
	}
	
	public Integer ppCheck(LoginUser loginuser){
		Integer count = 0;
		List<Maoxqhzpc> erroList = new ArrayList();
		List<Yicbj> yicbjList = new ArrayList();
		
		
		
		
		
		erroList = this.maoxqhzpcService.checkFene();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh()};
				CommonFun.insertError(Const.YICHANG_LX4, Const.YICHANG_LX4_str1, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
			
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkZhizlx();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),"制造路线"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str15, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkGongysdm();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),"供应商代码"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str5, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkDinghcj();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),"订货车间"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str50, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkBeihzq();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),"备货周期"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str4, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkFayzq();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),"发运周期"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str12, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkZiyhqrq();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),"资源获取日期"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkLujdm();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				if(maoxqhzpc.getCangkdm()==null||maoxqhzpc.getGongysdm()==null){
				String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),"路径代码"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
					String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),maoxqhzpc.getGongysdm(),maoxqhzpc.getCangkdm(),"路径代码"};
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str13, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}
			}	
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkCangkdm();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),"仓库编号"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkUabzlx();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				if(maoxqhzpc.getGongysdm()!=null&&maoxqhzpc.getCangkdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzpc.getUsercenter(), maoxqhzpc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(),maoxqhzpc.getGongysdm(), "UA包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
				String[] paramStr = new String[] { maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), "UA包装类型" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
						paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkUabzuclx();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				if(maoxqhzpc.getGongysdm()!=null&&maoxqhzpc.getCangkdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzpc.getUsercenter(), maoxqhzpc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(),maoxqhzpc.getGongysdm(), "UA中UC包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
					String[] paramStr = new String[] { maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), "UA中UC包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
							paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}
				
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkUabzucsl();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				if(maoxqhzpc.getGongysdm()!=null&&maoxqhzpc.getCangkdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzpc.getUsercenter(), maoxqhzpc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(),maoxqhzpc.getGongysdm(), "UA中UC个数" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
				String[] paramStr = new String[] { maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), "UA中UC个数" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
						paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkUabzucrl();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				if(maoxqhzpc.getGongysdm()!=null&&maoxqhzpc.getCangkdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzpc.getUsercenter(), maoxqhzpc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(),maoxqhzpc.getGongysdm(), "UA中UC容量" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList,wlgyy,
							paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
				String[] paramStr = new String[] { maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), "UA中UC容量" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList,Const.POAcode,
						paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkWaibghms();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				if(maoxqhzpc.getGongysdm()==null||maoxqhzpc.getCangkdm()==null){
				String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),"外部供货模式"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
					String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),maoxqhzpc.getGongysdm(),maoxqhzpc.getCangkdm(),"外部供货模式"};
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str13, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzpcService.checkJihyz();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzpc maoxqhzpc : erroList){
				String [] paramStr = new String []{maoxqhzpc.getUsercenter(),maoxqhzpc.getLingjbh(),"计划员代码"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzpc.getJihydz(), paramStr, maoxqhzpc.getUsercenter(), maoxqhzpc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		try
		{
			if(yicbjList.size()>0){
				this.yicbjservice.insertAll(yicbjList);
			}
			this.maoxqhzpcService.clearErro();	
		}
		catch (Exception e) {
			CommonFun.logger.error(e);
		}
		return count;
	}
	
	
	public Integer psCheck(LoginUser loginuser){
		Integer count = 0;
		List<Maoxqhzsc> erroList = new ArrayList();
		List<Yicbj> yicbjList = new ArrayList();
		
		erroList = this.maoxqhzscService.checkFene();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh()};
				CommonFun.insertError(Const.YICHANG_LX4, Const.YICHANG_LX4_str1, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
			
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkZhizlx();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),"制造路线"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str15, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkGongysdm();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),"供应商代码"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str5, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkDinghcj();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),"订货车间"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str50, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkBeihzq();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),"备货周期"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str4, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkFayzq();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),"发运周期"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str12, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkZiyhqrq();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),"资源获取日期"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkLujdm();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				if(maoxqhzsc.getCangkdm()==null||maoxqhzsc.getGongysdm()==null){
				String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),"路径代码"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
					String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),maoxqhzsc.getGongysdm(),maoxqhzsc.getCangkdm(),"路径代码"};
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str13, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}
			}	
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkCangkdm();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),"仓库编号"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkUabzlx();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				if(maoxqhzsc.getGongysdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzsc.getUsercenter(), maoxqhzsc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(),maoxqhzsc.getGongysdm(), "UA包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
				String[] paramStr = new String[] { maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), "UA包装类型" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList,Const.POAcode,
						paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkUabzuclx();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				if(maoxqhzsc.getGongysdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzsc.getUsercenter(), maoxqhzsc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(),maoxqhzsc.getGongysdm(), "UA中UC包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
					String[] paramStr = new String[] { maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), "UA中UC包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
							paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}
				
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkUabzucsl();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				if(maoxqhzsc.getGongysdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzsc.getUsercenter(), maoxqhzsc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(),maoxqhzsc.getGongysdm(), "UA中UC个数" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
				String[] paramStr = new String[] { maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), "UA中UC个数" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
						paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkUabzucrl();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				if(maoxqhzsc.getGongysdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzsc.getUsercenter(), maoxqhzsc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(),maoxqhzsc.getGongysdm(), "UA中UC容量" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList,wlgyy,
							paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
				String[] paramStr = new String[] { maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), "UA中UC容量" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
						paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkWaibghms();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				if(maoxqhzsc.getGongysdm()==null||maoxqhzsc.getCangkdm()==null){
				String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),"外部供货模式"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
					String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),maoxqhzsc.getGongysdm(),maoxqhzsc.getCangkdm(),"外部供货模式"};
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str13, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzscService.checkJihyz();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzsc maoxqhzsc : erroList){
				String [] paramStr = new String []{maoxqhzsc.getUsercenter(),maoxqhzsc.getLingjbh(),"计划员代码"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzsc.getJihyz(), paramStr, maoxqhzsc.getUsercenter(), maoxqhzsc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		try{
			if(yicbjList.size()>0){
				this.yicbjservice.insertAll(yicbjList);
			}
			this.maoxqhzscService.clearErro();	
		}
		catch (Exception e) {
			CommonFun.logger.error(e);
		}
		return count;
	}
	
	public Integer pjCheck(LoginUser loginuser){
		Integer count = 0;
		List<Maoxqhzjc> erroList = new ArrayList();
		List<Yicbj> yicbjList = new ArrayList();
		
		erroList = this.maoxqhzjcService.checkFene();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh()};
				CommonFun.insertError(Const.YICHANG_LX4, Const.YICHANG_LX4_str1, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
			
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkZhizlx();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),"制造路线"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str15, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkGongysdm();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),"供应商代码"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str5, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkDinghcj();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),"订货车间"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str50, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkBeihzq();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),"备货周期"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str4, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkFayzq();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),"发运周期"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str12, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkZiyhqrq();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),"资源获取日期"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkLujdm();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				if(maoxqhzjc.getCangkdm()==null||maoxqhzjc.getGongysdm()==null){
				String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),"路径代码"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
					String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),maoxqhzjc.getGongysdm(),maoxqhzjc.getCangkdm(),"路径代码"};
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str13, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}
			}	
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkCangkdm();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),"仓库编号"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkUabzlx();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				if(maoxqhzjc.getGongysdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzjc.getUsercenter(), maoxqhzjc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),maoxqhzjc.getGongysdm(), "UA包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), "UA包装类型" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
						paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkUabzuclx();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				if(maoxqhzjc.getGongysdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzjc.getUsercenter(), maoxqhzjc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),maoxqhzjc.getGongysdm(), "UA中UC包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
					String[] paramStr = new String[] { maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), "UA中UC包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList,Const.POAcode,
							paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}
				
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkUabzucsl();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				if(maoxqhzjc.getGongysdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzjc.getUsercenter(), maoxqhzjc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),maoxqhzjc.getGongysdm(), "UA中UC个数" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), "UA中UC个数" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
						paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkUabzucrl();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				if(maoxqhzjc.getGongysdm()!=null){
					String wlgyy = this.wulljservice.queryWlgyy(maoxqhzjc.getUsercenter(), maoxqhzjc.getCangkdm());
					String[] paramStr = new String[] { maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),maoxqhzjc.getGongysdm(), "UA中UC容量" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), "UA中UC容量" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
						paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkWaibghms();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				if(maoxqhzjc.getGongysdm()==null||maoxqhzjc.getCangkdm()==null){
				String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),"外部供货模式"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}else{
					String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),maoxqhzjc.getGongysdm(),maoxqhzjc.getCangkdm(),"外部供货模式"};
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str13, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
				}
			}
		}
		
		erroList.clear();
		erroList = this.maoxqhzjcService.checkJihyz();
		if(erroList.size()>0){
			count = erroList.size();
			for(Maoxqhzjc maoxqhzjc : erroList){
				String [] paramStr = new String []{maoxqhzjc.getUsercenter(),maoxqhzjc.getLingjbh(),"计划员代码"};
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, maoxqhzjc.getJihyz(), paramStr, maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}
		try
		{
			if(yicbjList.size()>0){
				this.yicbjservice.insertAll(yicbjList);
			}
			this.maoxqhzjcService.clearErro();	
		}
		catch (Exception e) {
			CommonFun.logger.error(e);
		}
		return count;
	}
	
//////////////////////////////////wuyichao 添加方法 2014/02/20/////////////
	/**
	 * @see 查询所有库存以线边仓库为基准
	 * @return
	 */
	public List<Ziykzb> queryAllKucByXianbck(String ziyhqrq)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("ziyhqrq", ziyhqrq);
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryAllKucByXianbck",map);
	}
	
	/**
	 * @see 查询所有库存以订货仓库为基准
	 * @return
	 */
	public List<Ziykzb> queryAllKucByDinghck(String ziyhqrq)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("ziyhqrq", ziyhqrq);
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryAllKucByDinghck",map);
	}
	
	/**
	 * @see   初始化库存map 和 计数map  Key 以 usercenter,lingjbh,cangkdm value 为kucsl 和计数值
	 *        两个参数MAP必须为实际的map 不能为null
	 * @param kucMap
	 * @param countMap
	 * @param ziykzbs
	 */
	public void initKucMapAndCountMap(List<Ziykzb> ziykzbs,Map<String,BigDecimal> kucMap,Map<String,Integer> countMap)
	{
		if(null != ziykzbs && ziykzbs.size() > 0)
		{
			for (Ziykzb ziykzb : ziykzbs) 
			{
				String key = ziykzb.getUsercenter() + ziykzb.getLingjbh() + ziykzb.getCangkdm();
				if(kucMap.containsKey(key))
				{
					countMap.put(key, countMap.get(key) + 1);
				}
				else
				{
					kucMap.put(key, ziykzb.getKucsl());
					countMap.put(key, 1);
				}
			}
		}
	}
	
	
	private void executeBatch(List list,String sqlId)
	{
		if(null != list && StringUtils.isNotBlank(sqlId))
		{
			if(list.size() / 10000 > 1)
			{
				int i = 1;
				List flag = new ArrayList();
				for (Object object : list) {
					if(i > 10000)
					{
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(sqlId, flag);
						flag = new ArrayList();
						i = 0;
					}
					flag.add(object);
					i++;
				}
				if(flag.size() > 0)
				{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(sqlId, flag);
				}
			}
			else
			{
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(sqlId, list);
			}
		}
	}
	
	private void getMaxniqnzq(String nianzq,String usercenter ,Map<String,String> map)
	{
		Map<String,String> param= new HashMap<String, String>() ;
		param.put("usercenter", usercenter) ;
		param.put("nianzq", nianzq.substring(0, 4)) ;
		CalendarCenter calendarCenter = calendarCenterService.maxTime(param);
		if(null != calendarCenter && StringUtils.isNotBlank(calendarCenter.getNianzq()))
			map.put(usercenter, calendarCenter.getNianzq().substring(4));
	}
	

	private void getMaxniqnzx(String zhouqxh, String usercenter,Map<String, String> map) {
		Map<String,String> param= new HashMap<String, String>() ;
		map.put("usercenter", usercenter) ;
		map.put("nianzx", zhouqxh.substring(0, 4)) ;
		String maxNianzx = calendarCenterService.getMaxNianzx(param) ;
		if(StringUtils.isNotBlank(maxNianzx))
			map.put(usercenter, maxNianzx);
	}

	
	private Map<String,BigDecimal> getPsCalendarVersionMap(List<CalendarVersion> calendarVersions)
	{
		Map<String , BigDecimal> resultMap = new HashMap<String, BigDecimal>();
		if(null != calendarVersions)
		{
			for (CalendarVersion calendarVersion : calendarVersions) {
				resultMap.put(calendarVersion.getBanc(), calendarVersion.getXis());
			}
		}
		
		return resultMap;
	}
	
	
//////////////////////////////////wuyichao 添加方法 2014/02/20/////////////
	
	
//////////////////wuyichao 2014-10-30 il国产件临时订单导入/////////////////////////
	@Transactional
	public boolean impTempDingd(List<Dingdmx> dingdmxs) 
	{
		boolean falg = true;
		Map<String,Dingd> dingdMap = new HashMap<String, Dingd>();
		Map<String,List<Dingdmx>> dingdmxMap = new HashMap<String, List<Dingdmx>>();
		Dingd dingd = null;
		List<Dingdmx> dingdmxList = null;
		String key = null;
		for (Dingdmx dingdmx : dingdmxs) 
		{
			key = dingdmx.getUsercenter() + dingdmx.getGongysdm() + dingdmx.getGonghlx() + dingdmx.getFasgysStr();
			dingd = dingdMap.get(key);
			if(null == dingd)
			{
				dingd = new Dingd();
				dingd.setUsercenter( dingdmx.getUsercenter());
				dingd.setGongysdm(dingdmx.getGongysdm());
				dingd.setBeiz("导入数据");
				dingd.setActive(Const.ACTIVE_1);
				dingd.setChullx(dingdmx.getGonghlx());
				dingd.setDingdzt(Const.DINGD_STATUS_ZZZ);
				dingd.setShiffsgys(dingdmx.getFasgysStr());
				dingd.setDingdlx(Const.DINGDLX_LINS);
				dingd.setDingdjssj(dingdmx.getCreate_time());
				dingd.setCreate_time(dingdmx.getCreate_time());
				dingd.setCreator(dingdmx.getCreator());
				dingd.setEdit_time(dingdmx.getEdit_time());
				dingd.setEditor(dingdmx.getEditor());
				dingd.setDingdh(getOrderNumber("T", dingd.getDingdh(), dingd.getGongysdm(), new HashMap<String, String>()));
				if(!dingdservice.doInsert(dingd))
				{
					throw new RuntimeException("导入国产件临时订单时插入订单有误!");
				}
				dingdMap.put(key, dingd);
			}
			dingdmxList = dingdmxMap.get(key);
			if(null == dingdmxList)
			{
				dingdmxList = new ArrayList<Dingdmx>();
			}
			dingdmx.setDingdh(dingd.getDingdh());
			dingdmxList.add(dingdmx);
			dingdmxMap.put(key, dingdmxList);
		}
		for (List<Dingdmx> insert : dingdmxMap.values()) {
			List<Dingdmx> ddmxlist=new ArrayList();
			for(Dingdmx bean:insert){
				if((bean.getGonghlx().equals("PJ")||bean.getGonghlx().equals("VJ"))&&bean.getLeix().equals("9")){
					Map map=new HashMap();
					map.put("usercenter", bean.getUsercenter());
					map.put("xiehztbh",bean.getXiehzt());
					map.put("shengcxbh", bean.getXiehzt().substring(0, 4)); //取卸货站台前4位
					//查询运输时刻的发运周期
					map.put("lingjbh", bean.getLingjbh());
					map.put("gongysbh", bean.getGongysdm());
					Xiehzt xiehzt = anxMaoxqService.queryXiehztObject(map);
					if (xiehzt == null) {
						
					}else{
						BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
						map.put("juedsk", bean.getJiaofrq());
						map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));//允许提前到货时间
						String zuizdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));
						if(StringUtils.isBlank(zuizdhsj)){
							zuizdhsj=bean.getJiaofrq();
						}
						bean.setZuizdhsj(zuizdhsj);
						bean.setZuiwdhsj(bean.getJiaofrq());
					}
					map.remove("shengcxbh");
					map.put("waibms", bean.getGonghlx());
					map.put("mudd", bean.getCangkdm());
					map.put("xiehzt", bean.getXiehzt());
					map.put("gongysbh", bean.getGongysdm());
			        List<Wullj> wulljxx =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.queryWullj",map);
			      //0013092: 临时订单上传增加发运时间字段 by csy 20170105
			        if (bean.getFayrq() == null || bean.getFayrq().equals("")) {
						if(wulljxx.size()>0){
							 Wullj wullj= wulljxx.get(0);
							 BigDecimal yunszq = (wullj.getYunszq()).multiply(BigDecimal.valueOf(24*60));
							 String faysj=DateUtil.DateSubtractMinutes(bean.getJiaofrq(),yunszq.intValue());
							 bean.setFayrq(faysj);
							 bean.setFaysj(faysj);
						}
					}else {
						bean.setFaysj(bean.getFayrq());
					}
			        		
				}
				if (bean.getFayrq() == null || bean.getFayrq().equals("")) {
				}else {
					bean.setFaysj(bean.getFayrq());
				}
				ddmxlist.add(bean);
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertDingdmx", ddmxlist);
		}
		return falg;
	}
	//////////////////wuyichao 2014-10-30 il国产件临时订单导入/////////////////////////
	
	
	//PJ订单拆分
	public void PJdingdjf(String dingdh){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deletePJJF");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertDingdmxpjjf",dingdh);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deletePJjfmx",dingdh);
		List<Dingdmx> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.queryPJjf",dingdh);
		List<Dingdmx> ddmx=new ArrayList<Dingdmx>();
		for(Dingdmx  bean:list){
			Map map=new HashMap();
			map.put("usercenter", bean.getUsercenter());
			map.put("xiehztbh",bean.getXiehzt());
			map.put("shengcxbh", bean.getXiehzt().substring(0, 4)); //取卸货站台前4位
			//查询运输时刻的发运周期
			map.put("lingjbh", bean.getLingjbh());
			map.put("gongysbh", bean.getGongysdm());
			
			Xiehzt xiehzt = anxMaoxqService.queryXiehztObject(map);
			if (xiehzt == null) {
				
			}else{
				BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
			    //String juedsk=DateUtil.DateSubtractMinutes(bean.getJiaofrq(),yunxtqdhsj.intValue());
				map.put("juedsk", bean.getJiaofrq());
				map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));//允许提前到货时间
				String zuizdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));
				if(StringUtils.isBlank(zuizdhsj)){
					zuizdhsj=bean.getJiaofrq();
				}else{
					zuizdhsj=zuizdhsj;
				}
				bean.setZuizdhsj(zuizdhsj);
				bean.setZuiwdhsj(bean.getJiaofrq());
			}
			map.remove("shengcxbh");
			map.put("waibms", "PJ");
			map.put("mudd", bean.getCangkdm());
			map.put("xiehzt", bean.getXiehzt());
			map.put("gongysbh", bean.getGongysdm());
	        List<Wullj> wulljxx =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.queryWullj",map);
			if(wulljxx.size()>0){
				 Wullj wullj= wulljxx.get(0);
				 BigDecimal yunszq = (wullj.getYunszq()).multiply(BigDecimal.valueOf(24*60));
				 String faysj=DateUtil.DateSubtractMinutes(bean.getJiaofrq(),yunszq.intValue());
				 bean.setFayrq(faysj);
			}	
         baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updatePjjf",bean);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertDingdmxfrompjjf",dingdh);
	}
	
	//查询物流路径中 运输周期
	public List queryWulljxx(Map map){
		List<Wullj> wulljxx =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.queryWulljxx",map);
		return wulljxx;
	}
}


