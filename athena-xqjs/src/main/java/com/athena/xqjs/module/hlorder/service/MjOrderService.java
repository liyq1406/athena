/**
 * 参考IlOrderService的Mj计算需要服务
 */
package com.athena.xqjs.module.hlorder.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Nianxb;
import com.athena.xqjs.entity.common.Tidj;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiaohblzjb;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.hlorder.MjMaoxqhzjcTmp;
import com.athena.xqjs.entity.hlorder.VjKuczh;
import com.athena.xqjs.entity.hlorder.VjXianbkc;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.anxorder.service.AnxMaoxqService;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.CalendarCenterService;
import com.athena.xqjs.module.common.service.JidygzqService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.NianxbService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

@Component
public class MjOrderService extends BaseService {

	public final Logger log = Logger.getLogger(MjOrderService.class);

	@Inject
	private CalendarCenterService calendarCenterService;// -用户中心日历service

	@Inject
	private MjMaoxqhzjTmpService mjmaoxqhzjTmpService;// 注入毛需求汇总sJervice

	@Inject
	private MjMaoxqhzjcTmpService mjmaoxqhzjcTmpService;// 注入毛需求汇总JCservice

	@Inject
	private NianxbService nianxbService;// 注入年包service

	@Inject
	private MjMaoxqmxService mjMaoxqmxService;// 毛需求明细service

	@Inject
	private CalendarCenterService calendarcenterservice;

	@Inject
	private MjYugzjbTmpService mjyugzjbTmpService;

	@Inject
	private DingdService dingdservice;

	@Inject
	private YicbjService yicbjservice;

	@Inject
	private JidygzqService jdygzqservice;
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private AnxMaoxqService anxMaoxqService;

	@Inject
	private LingjxhdService lingjxhdservice;

	@Inject
	private MJWulljService mjWulljService;

	/**
	 * 获取订单号
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-1
	 * @参数说明：String pattern，需求类型,String usercenter 用户中心,String gongysdm 供应商代码
	 */
	public String getOrderNumber(String pattern, String usercenter,
			String gongysdm, Map<String, String> orderNumberMap,MjMaoxqhzjcTmp maoxqhzjc,LoginUser loginUse)
			throws ServiceException {// 根据不同的需求类型分别来获取订单号
		String mos = pattern.substring(0, 1);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中mos=" + mos);
		CommonFun.logger
				.debug("获取订单号getOrderNumber方法中usercenter=" + usercenter);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中gongysdm=" + gongysdm);
		// 判断在map中是否存在订单号，若存在则直接返回订单号，
		if (null != orderNumberMap.get(usercenter + gongysdm + mos)) {
			// 直接返回订单号，
			CommonFun.mapPrint(orderNumberMap,
					"获取订单号getOrderNumber方法中orderNumberMap");
			return orderNumberMap.get(usercenter + gongysdm + mos);
		}
		// 若不存在对应的订单号，则先生成部分订单号
		// 流水号
		String liushStr = "";
		// 获取到年
		String year = CommonFun.getDate(Const.YEARY);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中year=" + year);
		// 获取到月份
		String month0 = CommonFun.getDate(Const.MONTH);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中month0=" + month0);
		// 获取到日
		String day = CommonFun.getDate(Const.DAY);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中day=" + day);
		// 转换得到16进制的月份
		String month = Integer.toHexString(Integer.valueOf(month0))
				.toUpperCase();
		CommonFun.logger.debug("获取订单号getOrderNumber方法中month=" + month);
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
					Const.YICHANG_LX2_str66, maoxqhzjc.getJihyz(), paramStr, usercenter,  maoxqhzjc.getLingjbh(),
					loginUse, Const.MJJS_UL);
			throw new ServiceException("获取到年为空值,请检查年型码设置");
		}
		// 通过获取到的年型的实体，得到需要的年型
		year = nianx.getNianx();
		CommonFun.logger.debug("获取订单号getOrderNumber方法中year=" + year);
		// 将各种形式放到map中
		Map<String, String> map = CommonFun.getPartten(year, month, day);
		// 得到部分订单号
		String orderNumber = map.get(pattern.toUpperCase());
		CommonFun.logger.debug("获取订单号getOrderNumber方法中orderNumber="
				+ orderNumber);
		// 由部分订单号去订单明细总去匹配查询
		List<Dingd> list = this.dingdservice.queryOrderNumbers(orderNumber);
		// CommonFun.objListPrint(list, "订单查找返回list");
		// 若查寻的结果集为空，则直接加上流水号0001，存入到map中
		if (list.isEmpty()) {
			// 生成最终的订单号
			orderNumber += Const.LSH_1;
			liushStr = Const.LSH_1;
			CommonFun.logger
					.debug("获取订单号getOrderNumber方法中订单号list为空时orderNumber="
							+ orderNumber);
			// 若得到的结果不为空，则表示已经存在，则在流水号上加1
		} else {
			// 定义订单号的模板
			DecimalFormat data = new DecimalFormat(Const.LSH_0);
			// 数据库中流水号值
			int dataLiush = Integer.valueOf(list.get(0).getDingdh()
					.substring(5, list.get(0).getDingdh().length())) + 1;
			// MAP缓存中流水号值
			int mapLiush = Integer.valueOf(StringUtils.defaultIfEmpty(
					orderNumberMap.get("liush"), "0")) + 1;
			if (mapLiush >= dataLiush) {
				liushStr = data.format(mapLiush);
			} else {
				liushStr = data.format(dataLiush);
			}
			// 若得到的结果不为空，则表示已经存在，则在流水号上加1
			orderNumber = orderNumber + liushStr;
			CommonFun.logger
					.debug("获取订单号getOrderNumber方法中订单号list不为空时orderNumber="
							+ orderNumber);
		}
		// 保存流水号字符串
		orderNumberMap.put("liush", liushStr);
		// 将最终生成的订单号存放到map中
		orderNumberMap.put(usercenter + gongysdm + mos, orderNumber);
		return orderNumber.trim();// 返回订单号
	}

	/**
	 * 清除中间表的数据
	 * 
	 * @author wyg
	 * @version v1.0
	 * @date 2011-12-1 参数说明：String pattern，需求类型
	 */
	public void clearData(String pattern, String usercenter) {
		CommonFun.logger.info("Il订单计算模式为" + pattern + "的中间表清理开始");
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算",
				"清除中间表的数据开始");
		// 清除预告中间表
		this.mjmaoxqhzjTmpService.doAllDelete(usercenter);// 调用毛需求汇总MJ的删除方法
		this.mjmaoxqhzjcTmpService.doAllDelete(usercenter);// 调用毛需求汇总关联参考系MJ的删除方法
		CommonFun.logger.info("Il订单计算模式为" + pattern + "的中间表清理结束");
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算",
				"清除中间表的数据结束");
	}

	/**
	 * @see 删除计算中的订单
	 * @param user
	 * @param dingdlx
	 * @param active
	 */
	private void deleteDingd(String user, String dingdlx, String active,
			String usercenter) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("creator", user);
		param.put("dingdlx", dingdlx);
		param.put("usercenter", usercenter);
		if (StringUtils.isNotBlank(active)) {
			param.put("active", active);
		}
		// 清除订单内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"anxjis.deleteDingdLjJsz", param);
		// 清除订单明细内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"anxjis.deleteDingdMxJsz", param);
		// 清除订单内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"anxjis.deleteDingdJsz", param);

		// 使用DingdljTmp.xml
		// 删除没有实际存在订单号的订单零件
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.deleteDingdLjByNoDingdh");

		// 使用DingdmxTmp.xml
		// 删除没有实际存在订单号的订单明细
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.mjDeleteDingdMxByNoDingdh");

	}

	/**
	 * Title:根据MJ计算修改成的MJ计算 国产件订单计算 Description:按照选取的毛需求计算既定和预告
	 * 参数为订单类型,资源获取日期,用户中心组 计算是否顺利完成
	 * 
	 * @param zhizlx
	 *            制造路线 "97W"
	 * @param dingdlx
	 *            订单类型 "MJ"
	 * @param ziyxqrq
	 *            资源获取日期
	 * @param jiihyz
	 *            ""
	 * @param banc
	 *            需求版次
	 * @param userString
	 *            用户字符 "UL"
	 * @param usercenter
	 *            用户中心 "UL","UW"
	 * @param loginUser
	 *            登录用户
	 * @author wuyg
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws ParseException
	 * @date 2011-12-1
	 */

	public String doCalculate(String dingdlx, String ziyxqrq,
			String jiihyz, String banc, String userString, LoginUser loginUser,
			List<Dingd> dingdList, String jkCode) {
		Map<String, String> orderNumberMap = new HashMap<String, String>();

		CommonFun.logger.debug("国产件订单类型为" + dingdlx + "的计算操开始");
		String message = "";
		String jssj = CommonFun.getJavaTime();
		Integer count = 0;
		// 用于存储maoxqhzpc表的数据
		List ls = new ArrayList();
		// 用于存储dingdlj表的数据

		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算",
				"取得用户中心开始");
		String caozz = loginUser.getUsername();
		CommonFun.logger.debug("国产件订单计算操作者" + caozz);
		CommonFun.logger.debug("国产件订单计算用户中心" + userString);
		Map<String, String> dingdhMap = new HashMap<String, String>();
		
		// 清空计算中的订单信息
		this.deleteDingd(null, "'" + Const.DINGDLX_ILORDER + "'", "",
				userString);

		Map<String, BigDecimal> resMap = new HashMap<String, BigDecimal>();
		BigDecimal yingyu = BigDecimal.ZERO; // 盈余在循环中查询，此处设置为0
		boolean flag = false;// 用于判断是否有多个既定

		this.clearData(dingdlx, userString);// 清理中间表
		this.conversionColRow(banc, dingdlx, ziyxqrq, userString);// 取出需要计算的毛需求并进行行列转换
																			// 按中心，零件，产线，使用车间汇总毛需求

		this.checkXiaohbl(dingdlx, userString);
		long startx = System.currentTimeMillis();
		// 汇总并插入中间表
		message = this
				.maoxqhzglckx(ziyxqrq, dingdlx, jiihyz, loginUser);// 将毛需求汇总到仓库，并关联参考系参数然后将数据插入到毛需求汇总_参考系表
		if (null != message) {
			return message;
		}
		long endx = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,单次计算时间"
				+ (endx - startx) / 1000);
		CommonFun.logger.debug("国产件订单类型为" + dingdlx + "的参数校验");

		// 效验中间表数据
		count = this.check(loginUser, dingdlx);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "数据准备结束");

		// ///////////////////wuyichao 优化订单明细 订单零件为批量插入//////////////////
		Map<String, List<Dingdlj>> saveLingjMap = new HashMap<String, List<Dingdlj>>();
		Map<String, List<Dingdmx>> saveMingxMap = new HashMap<String, List<Dingdmx>>();
		// ///////////////////wuyichao 优化订单明细 订单零件为批量插入//////////////////
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算",
				"计算开始");

		// 初始化订单计算
		// 初始化零件map
		Map<String, BigDecimal> zongMap = new TreeMap<String, BigDecimal>();
		// 初始化仓库map
		Map<String, BigDecimal> cangkMap = new TreeMap<String, BigDecimal>();
		Map<String, Object> flagMap = new HashMap<String, Object>();

		flagMap.put("flagCangku", "");
		flagMap.put("flagLingj", "");
		flagMap.put("zhouqixuhao", 0);

		ls = this.mjmaoxqhzjcTmpService.selectDingd(dingdlx);// 得到maoxqhzjcmjTmp表数据
		int maxPx=0;
		if (!ls.isEmpty()) {
			// 如果得到的数据不为空
			// 查询全部库存转换记录 ,为减少查询次数，一个用户中心查询一次

			// 根据用户中心汇总全部库存置换集合
			Map<String, Map<String, VjKuczh>> kuczhUsercenterMap = new HashMap<String, Map<String, VjKuczh>>();
			for (int i = 0; i < ls.size(); i++) {
				MjMaoxqhzjcTmp maoxqhzjc = (MjMaoxqhzjcTmp) ls.get(i);// 取得毛需求汇总_参考系中一条数据

				//用户中心 - xss-20160422 
				maxPx = this.getGongysMaxPx( maoxqhzjc.getLingjbh() , maoxqhzjc.getUsercenter() );
				
				// 按用户中心得到该用户中心的全部 库存置换集合
				kuczhUsercenterMap = findKuczh(maoxqhzjc.getUsercenter(),
						kuczhUsercenterMap);

				BigDecimal ddljzl = BigDecimal.ZERO;
				int jidgs = 0;// 用于判断既定个数
				// 初始化订单零件
				Dingdlj dingdlj = new Dingdlj();

				String dingdnr = maoxqhzjc.getDingdnr();
				CommonFun.logger.debug("国产件MJ模式订单计算dingdnr=" + dingdnr);
				for (int j = 0; j < dingdnr.length(); j++) {
					String neir = maoxqhzjc.getDingdnr().substring(j, j + 1);
					if (neir.equalsIgnoreCase("9")) {
						jidgs = jidgs + 1;
					}
				}
				CommonFun.logger.debug("国产件MJ模式订单计算jidgs=" + jidgs);
				// 偏移时间为：ckx_wullj表里 beihsj2 + CANGKSHSJ2 + CANGKFHSJ2
				// begin 封闭期
				int pianysj = 0;
				pianysj = this.getPianysj(maoxqhzjc,maxPx);
				if(pianysj==-1){
					continue;
				}
				// end 封闭期
				//当maxPx>pianysj时，需要补算既定天数到封闭期日期
				//偏移差异
				int pianycy = 0;
				if(maxPx>pianysj)
				{
					pianycy= maxPx - pianysj;
				}
				jidgs = jidgs+pianycy;
				CommonFun.logger.info("偏移差异：" + pianycy);
				CommonFun.logger.info("既定个数：" + jidgs);
//				int pianysj = (maoxqhzjc.getBeihsj2().add(
//						maoxqhzjc.getCangkshsj2()).add(maoxqhzjc
//						.getCangkfhsj2())).divide(BigDecimal.ONE, 0,
//						BigDecimal.ROUND_UP).intValue();
				CommonFun.logger.debug("国产件MJ模式订单计算pianysj=" + pianysj);
				if (pianysj <= Const.ZJCHANGDU) {
					for (int x = 0; x < jidgs; x++) {
						String method = Const.GETJ + (pianysj + x);// 拼接getPi方法
						CommonFun.logger.debug("国产件MJ模式订单计算既定计算method="
								+ method);
						String methodJd = Const.GETP + x + "sl";
						CommonFun.logger.debug("国产件MJ模式订单计算既定计算methodJd="
								+ methodJd);
						Class clsdd = dingdlj.getClass();
						Method methdjd = null;
						String pxriq = "";
						BigDecimal jiding = BigDecimal.ZERO;
						if (pianysj + x > Const.ZJCHANGDU) {
							jiding = BigDecimal.ZERO;
							CommonFun.logger
									.debug("国产件MJ模式订单计算既定计算在（pianysj + x > Const.ZJCHANGDU）时jiding="
											+ jiding);
							resMap = this.jiDingCalculate(jiding, maoxqhzjc,
									flag, x, pianysj, dingdlj,
									i, caozz, ddljzl,
									sfayujs(kuczhUsercenterMap, maoxqhzjc),
									ziyxqrq, orderNumberMap, cangkMap, zongMap,
									flagMap,loginUser,jkCode);// 调用既定数量计算方法
							yingyu = resMap.get("yingyu");
							ddljzl = resMap.get("ddljzl");

							CommonFun.logger.debug("国产件MJ模式订单计算既定计算在（pianysj + x > Const.ZJCHANGDU）时yingyu="+ yingyu);
						} else {
								try {
									methdjd = clsdd.getMethod(methodJd,new Class[] {});
								} catch (SecurityException e) {
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									e.printStackTrace();
								}
							jiding = this.backValue(method, maoxqhzjc, pianysj
									+ x, dingdlx);
							CommonFun.logger.debug("国产件MJ模式订单计算既定计算不在（pianysj + x > Const.ZJCHANGDU）时jiding="+ jiding);
							resMap = this.jiDingCalculate(jiding, maoxqhzjc,
									flag, x, pianysj, dingdlj,
									i, caozz, ddljzl,
									sfayujs(kuczhUsercenterMap, maoxqhzjc),
									ziyxqrq, orderNumberMap, cangkMap, zongMap,
									flagMap,loginUser,jkCode);// 调用既定数量计算方法
							yingyu = resMap.get("yingyu");
							ddljzl = resMap.get("ddljzl");
							CommonFun.logger
									.debug("国产件MJ模式订单计算既定计算不在（pianysj + x > Const.ZJCHANGDU）时yingyu="
											+ yingyu);
							clsdd = dingdlj.getClass();
							if (x == 0) {
								methodJd = "getP" + x + "fyzqxh";
								CommonFun.logger
										.debug("国产件MJ模式订单计算既定计算在（x == 0）时methodJd="
												+ methodJd);
							} else {
								methodJd = "getP" + x + "rq";
								CommonFun.logger
										.debug("国产件MJ模式订单计算既定计算不在（x == 0）时methodJd="
												+ methodJd);
							}
							try {
								methdjd = clsdd.getMethod(methodJd,new Class[] {});
							} catch (SecurityException e1) {
								CommonFun.logger.error(e1);
								throw new RuntimeException(
										"IlOrderService.doCalculate,SecurityException");
							} catch (NoSuchMethodException e1) {
								CommonFun.logger.error(e1);
								throw new RuntimeException(
										"IlOrderService.doCalculate,NoSuchMethodException");
							}
							Object rqObj;
							try {
								rqObj = methdjd.invoke(dingdlj, null);
							} catch (IllegalArgumentException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException(
										"IlOrderService.doCalculate,IllegalArgumentException");
							} catch (IllegalAccessException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException(
										"IlOrderService.doCalculate,IllegalAccessException");
							} catch (InvocationTargetException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException(
										"IlOrderService.doCalculate,InvocationTargetException");
							}
							if (null == rqObj) {
								pxriq = null;
								CommonFun.logger.debug("国产件MJ模式订单计算既定计算pxriq="
										+ pxriq);
							} else {
								try {
									pxriq = methdjd.invoke(dingdlj, null)
											.toString();
								} catch (IllegalArgumentException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException(
											"IlOrderService.doCalculate,IllegalArgumentException");
								} catch (IllegalAccessException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException(
											"IlOrderService.doCalculate,IllegalAccessException");
								} catch (InvocationTargetException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException(
											"IlOrderService.doCalculate,InvocationTargetException");
								}
								CommonFun.logger.debug("国产件MJ模式订单计算既定计算pxriq="
										+ pxriq);
							}
						}
						methodJd = Const.GETP + x + "sl";
						CommonFun.logger.debug("国产件MJ模式订单计算既定计算methodJd="
								+ methodJd);
						BigDecimal jissl = this.backValue(methodJd, dingdlj, x,
								dingdlx);
						CommonFun.logger.debug("国产件MJ模式订单计算既定计算jissl=" + jissl);
						this.insertDingdMX(dingdlj, maoxqhzjc, x,
								dingdlx, dingdhMap, jissl, pxriq, caozz,
								Const.SHIFOUSHIJIDING, saveMingxMap);
					}
					// vjDingdljService.doInsert(dingdlj);
					List<Dingdlj> flagLingj = saveLingjMap.get(dingdlj
							.getDingdh());
					if (null == flagLingj) {
						flagLingj = new ArrayList<Dingdlj>();
					}
					flagLingj.add(dingdlj);
					saveLingjMap.put(dingdlj.getDingdh(), flagLingj);
					this.insertdingd(dingdlj, maoxqhzjc, dingdlx, jssj, banc,
							jiihyz, caozz);

				} else {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getGongysdm(),
							maoxqhzjc.getLingjbh() };
					this.yicbjservice.insertError(Const.YICHANG_LX3,
							Const.YICHANG_LX3_str9, maoxqhzjc.getJihyz(),
							paramStr, maoxqhzjc.getUsercenter(),
							maoxqhzjc.getLingjbh(), loginUser,
							Const.JISMK_IL_CD);
				}
			}

		}
		//xss - 0012726
		Map<String, String> map = new HashMap<String, String>();
		map.put("xuqbc", banc);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.updateMaoxq", map);
		
		Set<String> flagDingdhSet = saveLingjMap.keySet();
		for (String flagDingdh : flagDingdhSet) {
			executeBatch(saveLingjMap.get(flagDingdh), "hlorder.insertDingdlj");
			executeBatch(saveMingxMap.get(flagDingdh),
					"hlorder.mjInsertDingdmx");
			// 零件流三期 MJ日订单均分 0011486
			// 判断当前的的订单类型 ，如果当前订单时MJ时则将订单明细数据同时插入到MJ均分订单明细表中
			if (dingdlx.equalsIgnoreCase(Const.MJ)) {
				mJdingdjf(flagDingdh, userString);
			}
		}

		flagMap.put("flagCangku", "");
		flagMap.put("flagLingj", "");
		flagMap.put("zhouqixuhao", 0);
		try {
			// 如果是日订单直接生效
			//xss_0013089
			if (dingdlx.equalsIgnoreCase(Const.MJ)) {
				this.updateDingd(dingdlx, null, Const.DINGD_STATUS_YSX, caozz, "'"+ Const.DINGDLX_ILORDER + "','" + Const.DINGDLX_JL+ "'");
			}
			// 其他订单需审核
			else {
				this.updateDingd(dingdlx, null, Const.DINGD_STATUS_ZZZ, caozz, "'"+ Const.DINGDLX_ILORDER + "','" + Const.DINGDLX_JL+ "'");
			}
		} catch (Exception e) {
			CommonFun.logger.error(e.toString());
			log.error("Il订单计算出错");
			log.error(e.toString());
		}
		if (count != 0) {
			message = "计算完成,但是校验时某些零件参数有误,请查看异常报警信息!";
		} else {
			message = "计算完成";
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算",
				"计算结束");
		CommonFun.logger.info("Il订单计算正常结束");
		log.info("Il订单计算正常结束");
		return message;
	}

	/**
	 * 更新订单状态
	 * 
	 * @param dingdjssj
	 *            订单计算时间
	 * @param state
	 *            订单状态
	 * @param user
	 *            操作人
	 */
	private void updateDingd(String chullx, String dingdjssj, String state, String user,
			String dingdlx) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("state", state);
		param.put("dingdjssj", dingdjssj);
		param.put("creator", user);
		param.put("dingdlx", dingdlx);
		//xss_0013089
		param.put("chullx", chullx);
		// 更新订单状态
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"anxjis.updateDingdJsz", param);
	}

	/**
	 * <p>
	 * Title:MJ国产件既定要货量计算
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
	 * @param jiding
	 * @param maoxqhzjc
	 *            毛需求汇总参考系记录
	 * @param flag
	 *            用于判断是否有多个既定
	 * @param yingyu
	 *            盈余
	 * @param jiszq
	 *            既定个数循环
	 * @param pianysj
	 *            偏移时间
	 * @param dingdlj
	 *            订单零件
	 * @param zhizlx
	 *            制造路线 "97W"
	 * @param zhouqxh
	 *            周期序号
	 * @param caozz
	 *            操作者
	 * @param ddljzl
	 * @param sfayyjs
	 *            是否按盈余计算
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
	public Map<String, BigDecimal> jiDingCalculate(BigDecimal jiding,
			MjMaoxqhzjcTmp maoxqhzjc, boolean flag, 
			int jiszq, int pianysj, Dingdlj dingdlj,
			int zhouqxh, String caozz, BigDecimal ddljzl, boolean sfayyjs,
			String ziyxqrq, Map<String, String> orderNumberMap,
			Map<String, BigDecimal> cangkMap, Map<String, BigDecimal> zongMap,
			Map<String, Object> flagMap,LoginUser loginUse, String jkCode) {
		Map<String, BigDecimal> resMap = new HashMap<String, BigDecimal>();
		BigDecimal yingyu = BigDecimal.ZERO;// 盈余
		BigDecimal daixh = BigDecimal.ZERO;// 待消耗
		BigDecimal yaohl = BigDecimal.ZERO;// 要货量
		BigDecimal quzhyhl = BigDecimal.ZERO;// 要货量取整后的值
		BigDecimal ziy = BigDecimal.ZERO;// 净资源
		if (!flag) {
			BigDecimal aqkc = maoxqhzjc.getAnqkc();// 安全库存
			if (aqkc == null) {
				aqkc = BigDecimal.ZERO;
			}
			if (sfayyjs) {// 按盈余
				// 查询盈余 需要时实查询，应为在计算中会修改盈余。
				// 获取线边库存
				VjXianbkc xianbkc = queryXianbkc(maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), null, maoxqhzjc.getCangkdm(),
						"1", maoxqhzjc.getDanw());
				if (xianbkc.getYingy() == null) {
					xianbkc.setYingy(BigDecimal.ZERO);// 置零
				}
				yingyu = xianbkc.getYingy();
				
				// 异常消耗
				BigDecimal yicxh = BigDecimal.ZERO;
				
				if (jiszq == 0)
				{
					// 盈余计算时要减去安全库存减去异常消耗，保存时要增加安全库存 。目的是为保障供应时有一定的安全库存
					Date date = new Date();
					DateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String time = format.format(date);
					Map<String, String> param = new HashMap<String, String>();
					param.put("lingjbh", maoxqhzjc.getLingjbh());
					param.put("usercenter", maoxqhzjc.getUsercenter());
					param.put("gonghlx", maoxqhzjc.getMos2());
					param.put("xianbck", maoxqhzjc.getCangkdm());
					param.put("jilrq", time);
					yicxh = this.getYcxh(param);
					if ( null == yicxh)
					{
						yicxh = BigDecimal.ZERO;
					}
				}
				ziy = yingyu.subtract(aqkc).subtract(yicxh);// 盈余-安全库存-异常消耗
				yaohl = jiding.subtract(ziy);
				// 判断要货量是否大于盈余
				if (yaohl.compareTo(BigDecimal.ZERO) < 0) {// 净资源 大于要货量
					yingyu = yaohl.abs(); // 盈余没有减完，还有多的
					yaohl = BigDecimal.ZERO;// 盈余能满足需求
				} else {
					yingyu = BigDecimal.ZERO;// 盈余减完
				}
			} else {// 按库存
				// 查询库存(资源快照)
				Ziykzb ziykzb = queryZiykzb(maoxqhzjc.getUsercenter(),
				maoxqhzjc.getLingjbh(), maoxqhzjc.getCangkdm(), ziyxqrq);
				// MJ模式:待交付=已发要货总量 – 交付总量 – 终止总量(零件仓库)
				BigDecimal weijfzl = this
						.getBigDecimalAx(maoxqhzjc.getDingdlj())
						.subtract(this.getBigDecimalAx(maoxqhzjc.getJiaoflj()))
						.subtract(
								this.getBigDecimalAx(maoxqhzjc.getDingdzzlj()));
				// 待消耗
				daixh = maoxqhzjc.getDaixh();
				
				//动态获取待消耗 add by zbb
				//daixh = this.getDaixh(maoxqhzjc, jiszq+pianysj);
				//CommonFun.logger.debug("动态待消耗="	+ daixh);

				// MJ模式汇总替代件数量+替代件待交付
				BigDecimal tidjKuc = this.tidjKuc(maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), maoxqhzjc.getZiyhqrq(),
						maoxqhzjc.getCangkdm());// 替代件数量

				// 净资源=库存数量 + 待交付 + 替代件数量 +替代件待交付+ 仓库系统调整值(年末)XITTZZ -
				// 安全库存(零件仓库) - 待消耗
				ziy = ziykzb.getKucsl().add(weijfzl).add(tidjKuc)
						.add(this.getBigDecimalAx(maoxqhzjc.getXittzz()))
						.subtract(this.getBigDecimalAx(maoxqhzjc.getAnqkc()))
						.subtract(this.getBigDecimalAx(daixh));

				yaohl = jiding.subtract(ziy);
				if (yaohl.compareTo(BigDecimal.ZERO) < 0) { // 净资源 大于要货量
					yaohl = BigDecimal.ZERO;
				}
			}
			dingdlj.setDingdh(this.getOrderNumber(Const.MJ,
					maoxqhzjc.getUsercenter(), maoxqhzjc.getGongysdm(),
					orderNumberMap,maoxqhzjc,loginUse));// 订单号

			dingdlj.setLingjbh(maoxqhzjc.getLingjbh());// 零件号
			dingdlj.setGongysdm(maoxqhzjc.getGongysdm());// 订货仓库
			dingdlj.setGongyslx(maoxqhzjc.getGongyslx());// 订货仓库类型
			dingdlj.setUsercenter(maoxqhzjc.getUsercenter());// 用户中心
			dingdlj.setDinghcj(maoxqhzjc.getDinghcj());// 订货车间
			dingdlj.setCangkdm(maoxqhzjc.getCangkdm());// 仓库代码

			int px = pianysj;
			CommonFun.logger.debug("国产件订单计算MJ模式既定计算jiDingCalculate方法px="+ jiszq);
			String method = "";// 拼接getPi方法
			if (px == 0) {
				method = "getJ" + px + "rq";// 拼接getPi方法
				CommonFun.logger.debug("国产件订单计算MJ模式既定计算jiDingCalculate方法在（）时method="+ method);
			} else {
				// 拼接getPi方法
				method = "getJ" + px + "riq";
				CommonFun.logger.debug("国产件订单计算MJ模式既定计算jiDingCalculate方法不在（）时method="+ method);
			}

			String pxriq = null;
			Class cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
			Object obj;
			Method meth;
			
			try {
				meth = cls.getMethod(method, new Class[] {});
				obj = meth.invoke(maoxqhzjc, null);
			} catch (Exception e) {
				CommonFun.logger.error(e);
				throw new RuntimeException("IlOrderService.jiDingCalculate,Exception");
			}
			
			if (null == obj) {
				dingdlj.setP0fyzqxh(null);
				CommonFun.logger
						.debug("国产件订单计算MJ模式既定计算jiDingCalculate方法P0fyzqxh="
								+ null);
			} else {
				try {
					pxriq = meth.invoke(maoxqhzjc, null).toString();
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,InvocationTargetException");
				}// 执行得到的方法，取得预告周期的数量
				CommonFun.logger.debug("国产件订单计算MJ模式既定计算jiDingCalculate方法pxriq="
						+ pxriq);
				dingdlj.setP0fyzqxh(pxriq.substring(0, 10));// p0周期序号
				CommonFun.logger
						.debug("国产件订单计算MJ模式既定计算jiDingCalculate方法P0fyzqxh="
								+ pxriq.substring(0, 10));
			}
			dingdlj.setUsbaozlx(maoxqhzjc.getUsbzlx());// US包装类型
			dingdlj.setUsbaozrl(maoxqhzjc.getUsbzrl());// US包装容量
			dingdlj.setDanw(maoxqhzjc.getDanw());// 单位
			dingdlj.setKuc(maoxqhzjc.getKuc());// 库存
			dingdlj.setAnqkc(maoxqhzjc.getAnqkc());// 安全库存
			dingdlj.setTiaozjsz(maoxqhzjc.getTiaozjsz());// 调整计算值
			dingdlj.setXittzz(maoxqhzjc.getXittzz());// 系统调整值
			dingdlj.setDingdlj(maoxqhzjc.getDingdlj());// 订单累计
			dingdlj.setJiaoflj(maoxqhzjc.getJiaoflj());// 交付累计
			dingdlj.setZhongzljddlj(maoxqhzjc.getDingdzzlj());// //订单终止累计

			dingdlj.setDaixh(maoxqhzjc.getDaixh());// 待消耗
			dingdlj.setZiyhqrq(maoxqhzjc.getZiyhqrq());// 资源获取日期
			dingdlj.setShifylaqkc(maoxqhzjc.getShifylaqkc());// 是否依赖安全库存
			dingdlj.setShifyldjf(maoxqhzjc.getShifyldjf());// 是否依赖待交付
			dingdlj.setShifyldxh(maoxqhzjc.getShifyldxh());// 是否依赖待消耗
			dingdlj.setShifylkc(maoxqhzjc.getShifylkc());// 是否依赖库存
			dingdlj.setDingdnr(maoxqhzjc.getDingdnr());// 订单内容
			dingdlj.setDingdzzsj(CommonFun.getJavaTime().toString());// 订单制作时间
			dingdlj.setLingjsx(maoxqhzjc.getLingjsx());
			dingdlj.setZuixqdl(maoxqhzjc.getZuixqdl());
			dingdlj.setGonghms(maoxqhzjc.getMos2());
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
				
				// 拼接getPi方法
				method = "getJ" + (px + z) + "riq";
				CommonFun.logger.debug("国产件订单计算MJ模式既定计算jiDingCalculate方法method="+ method);
				cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
				try {
					meth = cls.getMethod(method, new Class[] {});
				} catch (SecurityException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,SecurityException");
				} catch (NoSuchMethodException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,NoSuchMethodException");
				}// 得到Maoxqhzpc类的method拼接的方法
				Object riqObj;
				try {
					riqObj = meth.invoke(maoxqhzjc, null);
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,InvocationTargetException");
				}
				if (null == riqObj) {
					pxriq = null;
					CommonFun.logger.debug("国产件订单计算MJ模式既定计算jiDingCalculate方法pxriq="+ pxriq);
				} else {
					try {
						pxriq = meth.invoke(maoxqhzjc, null).toString();
					} catch (IllegalArgumentException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException(
								"IlOrderService.jiDingCalculate,IllegalArgumentException");
					} catch (IllegalAccessException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException(
								"IlOrderService.jiDingCalculate,IllegalAccessException");
					} catch (InvocationTargetException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException(
								"IlOrderService.jiDingCalculate,InvocationTargetException");
					}// 执行得到的方法，取得预告周期的数量
					CommonFun.logger.debug("国产件订单计算MJ模式既定计算jiDingCalculate方法pxriq="+ pxriq);
				}

				method = "setP" + (z) + "rq";
				
				cls = dingdlj.getClass();// 得到Maoxqhzpc类
				try {
					meth = cls.getMethod(method, String.class);
				} catch (SecurityException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,SecurityException");
				} catch (NoSuchMethodException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,NoSuchMethodException");
				}// 得到Maoxqhzpc类的method拼接的方法
				try {
					meth.invoke(dingdlj, pxriq);
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,InvocationTargetException");
				}// 执行得到的方法，setPx
			}
			// 要货量取整
			Map<String, Object> valueMap = quzheng(yaohl, dingdlj.getUsbaozrl(), dingdlj.getZuixqdl());
			quzhyhl = (BigDecimal) valueMap.get("yaohl");// 取整后的要货量
			yingyu = yingyu.add((BigDecimal) valueMap.get("yingyu"));// 盈余

			// 更新盈余
			if (sfayyjs) {// 按 盈余计算，更新盈余
				// 盈余 = 现在盈余+安全库存
				yingyu = yingyu.add(aqkc);
				updateYingy(maoxqhzjc, yingyu, jkCode);
			}
			method = "setP" + jiszq + "sl";// 拼接getPi方法
			CommonFun.logger.debug("国产件订单计算MJ模式既定计算jiDingCalculate方法method="+ method);
			cls = dingdlj.getClass();// 得到Dingdlj类
			try {
				meth = cls.getMethod(method, BigDecimal.class);
			} catch (SecurityException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,NoSuchMethodException");
			}// 得到Dingdlj类的method拼接的方法
			try {
				meth.invoke(dingdlj, quzhyhl);
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,InvocationTargetException");
			}// 执行得到的方法，setPx
		}

		ddljzl = ddljzl.add(quzhyhl);
		resMap.put("yingyu", yingyu);
		resMap.put("ddljzl", ddljzl);
		return resMap;
	}

	/**
	 * 行列转换
	 * 
	 * @param bancs
	 *            需求版次数组
	 * @param parrten
	 *            订单类型 "MJ"
	 * @param riq
	 *            资源获取日期
	 * @param usercenter
	 *            用户中心 "UL","UW"
	 * @param zhizlx
	 *            制造路线 "97W"
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-1
	 * @参数说明：String pattern，需求类型；String riq，资源获取日期;String[] usercenter
	 *              用户中心数组,String[] banc 需求版次，String zhizlx 制造路线
	 */
	public void conversionColRow(String banc, String parrten, String riq,
			String usercenter) {
		CommonFun.logger.info("Il订单计算模式为" + parrten + "的行列转换开始");
		long start = System.currentTimeMillis();
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算",
				"conversionColRow----行列转换开始");
		// 存放查询参数的map
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		map.put("riq", riq);
		CommonFun.logger.debug("Il订单计算模式为" + parrten + "的行列循环中，版次号为：" + banc
				+ "用户中心为：" + usercenter + "资源获取日期为：" + riq);
		CalendarCenter calendarcenter = this.calendarCenterService
				.queryCalendarCenterObject(map);
		CommonFun.objPrint(calendarcenter, "返回的中心日历实体calendarcenter");
		map.clear();
		if (null != calendarcenter) {
			// 获取到年周期
			String nianzq = "";
			nianzq = calendarcenter.getNianzq();
			CommonFun.logger.debug("Il订单计算模式为" + parrten + "的行列循环中国产件年周期为："
					+ nianzq);

			// 获取到年周序
			String nianzx = calendarcenter.getNianzx();
			CommonFun.logger.debug("Il订单计算模式为" + parrten + "的行列循环中年周序为："+ nianzx);

			List allList = new ArrayList();
			List<String> chanxList = this.mjMaoxqmxService.getChanx(usercenter,
					banc); // 根据用户中心 需求版次 制造路线 在毛需求明细中查询产线
			for (int x = 0; x < chanxList.size(); x++) {
				Map<String, String> inMap = this.mjMaoxqmxService.getriq(
						chanxList.get(x), usercenter, riq);
				if (null != inMap && !inMap.isEmpty()) {
					//xss_20161122_非工作日时，向后推算J0日期
					//inMap2.put("j0riq", riq);
					 
					inMap.put("usercenter", usercenter);
					inMap.put("xuqbc", banc);
					inMap.put("chanx", chanxList.get(x));
					CommonFun.mapPrint(inMap, "Il订单计算模式为" + parrten
							+ "的行列循环中周模式汇总的参数map：inMap");
					allList = this.mjmaoxqhzjTmpService.queryMaoxqhzj(inMap);
					if (!allList.isEmpty()) {
						this.mjMaoxqmxService.listInsertForIL(allList,
								parrten.toUpperCase());
					}
				}
			}

		}

		long end = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,行列转换时间"
				+ (end - start) / 1000);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算",
				"conversionColRow----行列转换结束");
	}

	/**
	 * @param ziyxqrq
	 *            资源获取日期
	 * @param dingdlx
	 *            订单类型 "MJ"
	 * @param jiihyz
	 *            ""
	 * @param zhizlx
	 *            制造路线 "97W"
	 * @param loginuser
	 *            登录用户
	 * 
	 *            将毛需求分配循环表中的毛需求汇总到仓库并关联相关参考系然后插入到毛需求汇总参考系表中
	 * 
	 * @author 陈骏 参数为资源获取日期
	 * 
	 * @date 2011-12-14
	 */
	public String maoxqhzglckx(String ziyhqrq, String dingdlx, String jihyz, LoginUser loginuser) {
		long start = System.currentTimeMillis();
		String message = null;

		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il周期订单计算",
				"毛需求关联参考系开始");
		this.mjCheckWullj(loginuser);
		// 毛需求汇总 ,并插入中间表
		message = this.mjMaoxqHz(message, ziyhqrq, jihyz, loginuser);

		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il日订单计算",
				"毛需求关联参考系结束");
		long end = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,计算时间"
				+ (end - start) / 1000);
		return message;
	}

	private void mjCheckWullj(LoginUser loginuser) {
		try {
			List<Yicbj> yicbjList = new ArrayList();
			List<MjMaoxqhzjcTmp> list = baseDao
					.select("hlorder.mjPjCheckMaoxqhzjcWullj");
			if (null != list && list.size() > 0) {
				for (MjMaoxqhzjcTmp maoxqhzjc : list) {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							maoxqhzjc.getZhizlx() };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str65, yicbjList,
							maoxqhzjc.getJihyz(), paramStr,
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							loginuser, Const.JISMK_IL_CD);
					mjmaoxqhzjTmpService.deleteMaoxqhzjById(maoxqhzjc.getId());
				}
			}
			if (yicbjList.size() > 0) {
				this.yicbjservice.insertAll(yicbjList);
			}
		} catch (Exception e) {
			CommonFun.logger.error(e);
		}
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
	public Map<String, Object> quzheng(BigDecimal yaohls,
			BigDecimal baozrl, BigDecimal zuixqdl) {
		Map<String, Object> map = new HashMap<String, Object>();
		BigDecimal quzhyhl = BigDecimal.ZERO;
		BigDecimal yingyus = BigDecimal.ZERO;
		
		// 按照零件供应商表中的最小起订量取整
		CommonFun.logger.debug("普通零件包装取整包装容量：" + baozrl);
		CommonFun.logger.debug("普通零件包装取整要货数量：" + yaohls);
		if (yaohls.compareTo(BigDecimal.ZERO) == -1) {
			yingyus = yaohls.negate();
			yaohls = BigDecimal.ZERO;
			quzhyhl = yaohls.divide(baozrl, 0, BigDecimal.ROUND_UP)
					.multiply(baozrl);
		} else {
			quzhyhl = yaohls.divide(baozrl, 0, BigDecimal.ROUND_UP)
					.multiply(baozrl);
			CommonFun.logger.debug("普通零件包装取整取整后要货量：" + quzhyhl);
			yingyus = quzhyhl.subtract(yaohls);
			CommonFun.logger.debug("普通零件包装取整盈余数量：" + yingyus);
		}
		map.put("yaohl", quzhyhl);
		map.put("yingyu", yingyus);
		return map;
	}

	/*
	 * 替代库存
	 */
	private BigDecimal tidjKuc(String usercenter, String lingjbh,
			String ziyhqrq, String cangkbh) {
		Map<String, Object> tidjMap = new HashMap<String, Object>();
		tidjMap.put("usercenter", usercenter);
		tidjMap.put("lingjbh", lingjbh);
		CommonFun.mapPrint(tidjMap, "替代件库存查询tidjKuc方法查找替代件仓库参数map");
		CommonFun.logger.debug("替代件库存查询tidjKuc方法查找替代件仓库的sql语句为：");
		List<Tidj> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTidj", tidjMap);
		BigDecimal tidjKcSum = BigDecimal.ZERO;
		BigDecimal tidjdjf = BigDecimal.ZERO;

		if (!list.isEmpty()) {
			for (Tidj tidj : list) {

				Map<String, Object> tidjKcMap = new HashMap();
				tidjKcMap.put("usercenter", tidj.getUsercenter());
				tidjKcMap.put("lingjbh", tidj.getTidljh());
				tidjKcMap.put("ziyhqrq", ziyhqrq);
				tidjKcMap.put("cangkdm", cangkbh);

				CommonFun.mapPrint(tidjMap, "替代件库存查询tidjKuc方法查找替代件仓库库存和参数map");
				Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryTidjkc",
						tidjKcMap);

				Object djf = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxjis.queryTidjdjfM1",
						tidjKcMap);
				tidjdjf = new BigDecimal(djf != null ? djf.toString() : "0");
				if (obj == null) {
					tidjKcSum = tidjKcSum.add(BigDecimal.ZERO);
				} else {
					tidjKcSum = tidjKcSum.add(new BigDecimal(obj.toString()))
							.add(tidjdjf);
				}

			}
		}
		return tidjKcSum;
	}

	public BigDecimal backValue(String method, Object obj, int pianysj,
			String dingdlx) {
		CommonFun.logger.debug("反射取值backValue方法参数method=" + method);
		CommonFun.logger.debug("反射取值backValue方法参数pianysj=" + pianysj);
		CommonFun.logger.debug("反射取值backValue方法参数dingdlx=" + dingdlx);
		CommonFun.objPrint(obj, "反射取值backValue方法参数obj");
		BigDecimal value = BigDecimal.ZERO;
		if (null != obj) {
			Class cls = obj.getClass();// 得到Maoxqhzpc类
			Method meth = null;
			int changdu = 0;
			changdu = Const.ZJCHANGDU;
			if (pianysj > changdu) {
				value = BigDecimal.ZERO;
			} else {

				try {
					meth = cls.getMethod(method, new Class[] {});
				} catch (NoSuchMethodException e1) {
					CommonFun.logger.error(e1);
					// TODO Auto-generated catch block
					throw new RuntimeException(
							"IlOrderService.backValue,NoSuchMethodException");

				} catch (SecurityException e1) {
					CommonFun.logger.error(e1);
					// TODO Auto-generated catch block
					throw new RuntimeException(
							"IlOrderService.backValue,SecurityException");
				}
				try {
					value = new BigDecimal(meth.invoke(obj, null).toString());
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.backValue,IllegalArgumentException");

				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					// TODO Auto-generated catch block
					throw new RuntimeException(
							"IlOrderService.backValue,IllegalAccessException");

				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					// TODO Auto-generated catch block
					throw new RuntimeException(
							"IlOrderService.backValue,InvocationTargetException");

				}
			}
		}
		CommonFun.logger.debug("反射取值backValue方法结果value=" + value);
		return value;
	}

	public void insertDingdMX(Dingdlj dingdlj, Object obj,
			int pianysj, String dingdlx, Map<String, String> dingdhMap,
			BigDecimal jissl, String pxriq, String caozz, String zhouqilx,
			Map<String, List<Dingdmx>> saveMingxMap) {
		if (null == dingdlj.getDingdh()) {
			return;
		}

		Dingdmx dingdmx = new Dingdmx();
		dingdmx.setUsbaozlx(dingdlj.getUsbaozlx());
		dingdmx.setUsbaozrl(dingdlj.getUsbaozrl());
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

		if (zhouqilx.equals(Const.SHIFOUSHIJIDING)) {
			dingdmx.setLeix(Const.SHIFOUSHIJIDING);
		} else if (zhouqilx.equals(Const.SHIFOUSHIYUGAO)) {
			dingdmx.setLeix(Const.SHIFOUSHIYUGAO);
		}
		dingdmx.setActive(Const.ACTIVE_1);
		dingdmx.setShul(jissl);
		dingdmx.setJissl(jissl);
		dingdmx.setCreator(caozz);
		dingdmx.setCreate_time(CommonFun.getJavaTime());

		MjMaoxqhzjcTmp maoxqhzjc = (MjMaoxqhzjcTmp) obj;
		dingdmx.setGcbh(maoxqhzjc.getGcbh()); // 设置为订货仓库代码
		dingdmx.setGonghlx(Const.MJ);
		dingdmx.setLingjmc(maoxqhzjc.getLingjmc());
		dingdmx.setGongsmc(maoxqhzjc.getGongsmc());
		dingdmx.setNeibyhzx(maoxqhzjc.getNeibyhzx());
		// 设置要货开始时间
		dingdmx.setYaohqsrq(pxriq);
		// 设置要货结束时间
		dingdmx.setYaohjsrq(pxriq);
		dingdmx.setJiaofrq(pxriq);
		dingdmx.setFayrq(pxriq);
		// 订单明细状态变成已发送
		dingdmx.setZhuangt(Const.DINGD_STATUS_YSX);

		// /////////////////////wuyichao///////////////
		String dingdh = dingdmx.getDingdh();
		List<Dingdmx> flagDingdmx = saveMingxMap.get(dingdh);
		if (null == flagDingdmx) {
			flagDingdmx = new ArrayList<Dingdmx>();
		}
		flagDingdmx.add(dingdmx);
		saveMingxMap.put(dingdh, flagDingdmx);
		// /////////////////////wuyichao///////////////
	}

	public void insertdingd(Dingdlj dingdlj, Object obj, String dingdlx,
			String jssj, String banch, String jihyz, String caozz) {
		if (null == dingdlj.getDingdh()) {
			return;
		}
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

		MjMaoxqhzjcTmp maoxqhzjc = (MjMaoxqhzjcTmp) obj;
		dd.setChullx(Const.MJ);
		dd.setUsercenter(maoxqhzjc.getUsercenter());
		dd.setJiszq(dingdlj.getZiyhqrq().substring(0, 10));

		Map<String, String> ddhMap = new HashMap<String, String>();
		ddhMap.put("dingdh", dingdlj.getDingdh());
		Dingd existDd = this.dingdservice.queryDingdByDingdh(ddhMap);
		CommonFun.objPrint(dd, "国产订单插入时的dd");
		if (null == existDd) {
			this.dingdservice.doInsert(dd);
		}

	}

	/**
	 * 
	 * @param message
	 * @param ziyxqrq
	 *            资源获取日期
	 * @param zhizlx
	 *            制造路线 "97W"
	 * @param jihyz
	 *            ""
	 * @param loginuser
	 *            登录用户
	 * @return
	 */
	public String mjMaoxqHz(String message, String ziyhqrq, String jihyz, LoginUser loginuser) {
		String dingdnr = "";
		List list = this.mjmaoxqhzjcTmpService.proMaoxqhzjc(ziyhqrq);// 把毛需求汇总日表的需求汇总到仓库并关联参考系表，并将数据插入到数据库中
		if (list.size() > 0) {
			Map<String, String> map = new HashMap<String, String>();
			MjMaoxqhzjcTmp mzjc = (MjMaoxqhzjcTmp) list.get(0);
			map.put("usercenter", mzjc.getUsercenter());
			map.put("riq", ziyhqrq);
			CalendarCenter calendarCenterObject = this.calendarCenterService
					.queryCalendarCenterObject(map);
			String suoszq = calendarCenterObject.getNianzq().substring(4);
			CommonFun.logger.debug("MJ模式毛需求-参考系汇总所属周期为：" + suoszq);
			//dingdnr = this.jdygzqservice.queryDingdnr("", suoszq);
			// 不查询订单内容，直接赋值“9888”
			dingdnr = "9888";
			CommonFun.logger.debug("MJ模式毛需求-参考系汇总订单内容为：" + dingdnr);
			if (null == dingdnr) {
				message =  "订货路线下" + suoszq + "既定-预告-周期表订单内容字段为空。";
				return message;
			}
		} else {
			message = "对应的毛需求汇总_参考系表表数据为空.";
			return message;
		}
		// ////////////wuyichao 2014/02/20 将资源快照表内的数据存入内存
		// //////////////////////////////
		Map<String, BigDecimal> xianbKcMap = new HashMap<String, BigDecimal>();
		Map<String, Integer> xianbCountMap = new HashMap<String, Integer>();
		Map<String, BigDecimal> dinghKcMap = new HashMap<String, BigDecimal>();
		Map<String, Integer> dinghCountMap = new HashMap<String, Integer>();
		HashMap<String, Integer> fengbqMap = new HashMap<String, Integer>();
		// 初始化map
		initKucMapAndCountMap(queryAllKucByXianbck(ziyhqrq), xianbKcMap,
				xianbCountMap);
		initKucMapAndCountMap(queryAllKucByDinghck(ziyhqrq), dinghKcMap,
				dinghCountMap);
		// ////////////wuyichao 2014/02/20 将资源快照表内的数据存入内存
		// //////////////////////////////

		List<MjMaoxqhzjcTmp> rsList = new ArrayList<MjMaoxqhzjcTmp>();
		
		int maxPx = 0;
		int pianysj = 0;
		int fengbqMax = 0;
		// 遍历最大封闭期
		for (int i = 0; i < list.size(); i++) {
			MjMaoxqhzjcTmp maoxqhzjc = (MjMaoxqhzjcTmp) list.get(i);// 把汇总的数据插入到毛需求汇总参考系表中
			// 偏移时间为：ckx_wullj表里 CANGKSHPC2 + CANGKSHSJ2 + CANGKFHSJ2
			if (maoxqhzjc.getBeihsj2() == null) {
				maoxqhzjc.setBeihsj2(BigDecimal.ZERO);
			}
			if (maoxqhzjc.getCangkshsj2() == null) {
				maoxqhzjc.setCangkshsj2(BigDecimal.ZERO);
			}
			if (maoxqhzjc.getCangkfhsj2() == null) {
				maoxqhzjc.setCangkfhsj2(BigDecimal.ZERO);
			}
			pianysj = (maoxqhzjc.getBeihsj2().add(
					maoxqhzjc.getCangkshsj2()).add(maoxqhzjc.getCangkfhsj2()))
					.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_UP).intValue();
			Integer fengbq = fengbqMap.get(maoxqhzjc.getLingjbh());
			if (null == fengbq || fengbq <= 1)
			{
				fengbqMap.put(maoxqhzjc.getLingjbh(),pianysj);
			}
			else
			{
				if ( pianysj > fengbq)
				{
					maxPx = pianysj;
					fengbqMap.put(maoxqhzjc.getLingjbh(),maxPx);
				}
			}
		}
		
		
		for (int i = 0; i < list.size(); i++) {
			MjMaoxqhzjcTmp maoxqhzjc = (MjMaoxqhzjcTmp) list.get(i);// 把汇总的数据插入到毛需求汇总参考系表中
			fengbqMax = fengbqMap.get(maoxqhzjc.getLingjbh());
			//begin 封闭期的计算
			pianysj = this.getPianysj(maoxqhzjc,fengbqMax);
			CommonFun.logger.info("实际封闭期："+pianysj);
			if(pianysj==-1){
				continue;
			}
			//end 封闭期的计算
			
			CommonFun.logger.debug("MJ模式毛需求-参考系汇总偏移时间为：" + pianysj);
			BigDecimal daixh = BigDecimal.ZERO;
			for (int x = 0; x < pianysj; x++) {
				Method meth = null;
				try {
					if (x <= 15) {
						String method = Const.GETJ + x;// 拼接getPi方法
						Class cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类

						meth = cls.getMethod(method, new Class[] {});
					}
				} catch (SecurityException e1) {
					CommonFun.logger.error(e1);
					throw new RuntimeException(
							"IlOrderService.pjMaoxqHz,SecurityException");
				} catch (NoSuchMethodException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.pjMaoxqHz,NoSuchMethodException");
				}// 得到Maoxqhzpc类的method拼接的方法
				try {
					if (x > 15) {
						daixh = daixh.add(BigDecimal.ZERO);
					} else {
						daixh = daixh.add(new BigDecimal(meth.invoke(maoxqhzjc,
								null).toString()));
					}

				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.pjMaoxqHz,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.pjMaoxqHz,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.pjMaoxqHz,InvocationTargetException");
				}// 执行得到的方法，取得既定周期的数量
			}
			CommonFun.logger.debug("MJ模式毛需求-参考系汇总待消耗为：" + daixh);
			maoxqhzjc.setDaixh(daixh);
			String key = maoxqhzjc.getUsercenter() + maoxqhzjc.getLingjbh()
					+ maoxqhzjc.getCangkdm();

			// 线边库
			Integer count = xianbCountMap.get(key);
			if (null == count || count <= 1) {
				maoxqhzjc.setKuc(xianbKcMap.get(key) == null ? BigDecimal.ZERO
						: xianbKcMap.get(key));
			} else {
				String paramStr[] = new String[] { maoxqhzjc.getUsercenter(),
						ziyhqrq, maoxqhzjc.getLingjbh(), maoxqhzjc.getCangkdm() };
				yicbjservice.insertError(Const.YICHANG_LX3,
						Const.YICHANG_LX3_str8, jihyz, paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
				continue;
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
			CommonFun.objPrint(maoxqhzjc, "MJ模式毛需求-参考系汇总maoxqhzjc类");
			rsList.add(maoxqhzjc);
		}

		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("hlorder.mjInsertMaoxqhzjcTmp", rsList);
		return message;
	}

	public Integer check(LoginUser loginuser, String dingdlx) {
		Integer count = 0;
		count = this.vjCheck(loginuser);
		return count;
	}

	/**
	 * 效验中间表
	 * 
	 * @param loginuser
	 * @return
	 */
	public Integer vjCheck(LoginUser loginuser) {
		Integer count = 0;
		List<MjMaoxqhzjcTmp> erroList = new ArrayList();
		List<Yicbj> yicbjList = new ArrayList();

		erroList.clear();
		erroList = this.mjmaoxqhzjcTmpService.checkZhizlx();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MjMaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "制造路线" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str15, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.mjmaoxqhzjcTmpService.checkDinghcj();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MjMaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "订货车间" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str50, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.mjmaoxqhzjcTmpService.checkBEIHSJ2();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MjMaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "备货时间2" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str4, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.mjmaoxqhzjcTmpService.checkCangkshsj2();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MjMaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "仓库送货时间2" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str4, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.mjmaoxqhzjcTmpService.checkCangkfhsj2();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MjMaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "仓库返回时间2" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str4, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.mjmaoxqhzjcTmpService.checkZiyhqrq();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MjMaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "资源获取日期" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str49, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		// 订货仓库
		erroList.clear();
		erroList = this.mjmaoxqhzjcTmpService.checkGcbh();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MjMaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "订货仓库编号" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
						paramStr, maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.mjmaoxqhzjcTmpService.checkCangkdm();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MjMaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "线边仓库编号" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
						paramStr, maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.mjmaoxqhzjcTmpService.checkUsbaozlx();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MjMaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "US包装类型" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str4, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.mjmaoxqhzjcTmpService.checkUsbaozrl();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MjMaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "US包装容量" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str49, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.mjmaoxqhzjcTmpService.checkMos2();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MjMaoxqhzjcTmp maoxqhzjc : erroList) {
				if (maoxqhzjc.getGongysdm() == null
						|| maoxqhzjc.getCangkdm() == null) {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							"内部供货模式" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str49, yicbjList,
							maoxqhzjc.getJihyz(), paramStr,
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							loginuser, Const.JISMK_IL_CD);
				} else {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							maoxqhzjc.getGongysdm(), maoxqhzjc.getCangkdm(),
							"内部供货模式" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str13, yicbjList,
							maoxqhzjc.getJihyz(), paramStr,
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							loginuser, Const.JISMK_IL_CD);
				}
			}
		}

		try {
			if (yicbjList.size() > 0) {
				this.yicbjservice.insertAll(yicbjList);
			}
			this.mjmaoxqhzjcTmpService.clearErro();
		} catch (Exception e) {
			CommonFun.logger.error(e);
		}

		return count;
	}

	// ////////////////////////////////wuyichao 添加方法 2014/02/20/////////////
	/**
	 * @see 查询所有库存以线边仓库为基准
	 * @return
	 */
	public List<Ziykzb> queryAllKucByXianbck(String ziyhqrq) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ziyhqrq", ziyhqrq);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryAllKucByXianbck", map);
	}

	/**
	 * @see 查询所有库存以订货仓库为基准
	 * @return
	 */
	public List<Ziykzb> queryAllKucByDinghck(String ziyhqrq) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ziyhqrq", ziyhqrq);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryAllKucByDinghck", map);
	}

	/**
	 * @see 初始化库存map 和 计数map Key 以 usercenter,lingjbh,cangkdm value 为kucsl 和计数值
	 *      两个参数MAP必须为实际的map 不能为null
	 * @param kucMap
	 * @param countMap
	 * @param ziykzbs
	 */
	public void initKucMapAndCountMap(List<Ziykzb> ziykzbs,
			Map<String, BigDecimal> kucMap, Map<String, Integer> countMap) {
		if (null != ziykzbs && ziykzbs.size() > 0) {
			for (Ziykzb ziykzb : ziykzbs) {
				String key = ziykzb.getUsercenter() + ziykzb.getLingjbh()
						+ ziykzb.getCangkdm();
				if (kucMap.containsKey(key)) {
					countMap.put(key, countMap.get(key) + 1);
				} else {
					kucMap.put(key, ziykzb.getKucsl());
					countMap.put(key, 1);
				}
			}
		}
	}

	private void executeBatch(List list, String sqlId) {
		if (null != list && StringUtils.isNotBlank(sqlId)) {
			if (list.size() / 10000 > 1) {
				int i = 1;
				List flag = new ArrayList();
				for (Object object : list) {
					if (i > 10000) {
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(sqlId, flag);
						flag = new ArrayList();
						i = 0;
					}
					flag.add(object);
					i++;
				}
				if (flag.size() > 0) {
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(sqlId, flag);
				}
			} else {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(sqlId, list);
			}
		}
	}

	// MJ订单拆分
	public void mJdingdjf(String dingdh, String usercenter) {
		Map<String, String> mapvjjf = new HashMap<String, String>();
		mapvjjf.put("usercenter", usercenter);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.mjDeleteVJJF", mapvjjf);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.mjInsertDingdmxvjjf", dingdh);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.mjDeleteVJjfmx", dingdh);
		List<Dingdmx> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjQueryVJjf",dingdh);
		for (Dingdmx bean : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("usercenter", bean.getUsercenter());
			map.put("xiehztbh", bean.getXiehzt());
			map.put("shengcxbh", bean.getXiehzt().substring(0, 4)); // 取卸货站台前4位
			// 查询运输时刻的发运周期
			map.put("lingjbh", bean.getLingjbh());
			map.put("gongysbh", bean.getGongysdm());

			Xiehzt xiehzt = anxMaoxqService.queryXiehztObject(map);
			if (xiehzt == null) {

			} else {
				BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
				// String
				// juedsk=DateUtil.DateSubtractMinutes(bean.getJiaofrq(),yunxtqdhsj.intValue());
				map.put("juedsk", bean.getJiaofrq());
				map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));// 允许提前到货时间
				String zuizdhsj = CommonFun.strNull(anxMaoxqService
						.queryGongzsjmbQ(map));
				if (StringUtils.isBlank(zuizdhsj)) {
					zuizdhsj = bean.getJiaofrq();
				}
				bean.setZuizdhsj(zuizdhsj);
				bean.setZuiwdhsj(bean.getJiaofrq());
			}
			map.remove("shengcxbh");
			map.put("mos2", "MJ");
			map.put("mudd", bean.getCangkdm());
			map.put("xiehzt", bean.getXiehzt());
			map.put("gongysbh", bean.getGongysdm());

			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"hlorder.mjUpdatePjjf", bean);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.mjInsertDingdmxfromvjjf", dingdh);
	}

	/*
	 * 是否按盈余计算 true 是 false 否
	 */
	private boolean sfayujs(
			Map<String, Map<String, VjKuczh>> kuczhUsercenterMap,
			MjMaoxqhzjcTmp maoxqhzjc) {
		Map<String, VjKuczh> kuczhmap = kuczhUsercenterMap.get(maoxqhzjc
				.getUsercenter());// 用户中心的全部库存置换参考记录
		String key = kuczhCreateKey(maoxqhzjc.getUsercenter(),
				maoxqhzjc.getCangkdm());
		VjKuczh vjkuczh = kuczhmap.get(key);
		if (vjkuczh != null) {
			if("1".equals(vjkuczh.getIskaolckjs())){
				return false;
			}else {
				return true;
			}
		}
		return true;
	}

	/**
	 * 更新盈余
	 * 
	 */
	private void updateYingy(MjMaoxqhzjcTmp maoxqhzjc, BigDecimal yingy,String jkCode) {
		Map<String, Object> map = new HashMap<String, Object>();// 参数map
		map.put("usercenter", maoxqhzjc.getUsercenter());
		map.put("lingjbh", maoxqhzjc.getLingjbh());
		map.put("mudd",	maoxqhzjc.getCangkdm()); 	
		map.put("leix", "1");// 仓库
		map.put("yingy", yingy);
		map.put("editor", jkCode);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.updateXianbkcByParam", map);
	}

	/*
	 * 获取线边库存
	 * 
	 * @param userCenter 用户中心
	 * 
	 * @param lingjbh 零件编号
	 * 
	 * @param shengcxbh 生产线编号
	 * 
	 * @param mudd 仓库编号
	 * 
	 * @param leix 类型1仓库 2消耗点 固定为1
	 * 
	 * @return danw 零件的单位
	 */
	private VjXianbkc queryXianbkc(String userCenter, String lingjbh,
			String shengcxbh, String mudd, String leix, String danw) {
		Map<String, String> map = new HashMap<String, String>();// 参数map
		map.put("usercenter", userCenter);
		map.put("lingjbh", lingjbh);
		map.put("mudd", mudd);
		map.put("leix", leix);
		// logger.info("线边仓库："+userCenter+"-"+lingjbh+"-"+shengcxbh+"-"+mudd);
		VjXianbkc xianbkc = (VjXianbkc) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(
				"hlorder.queryXianbkcByParam", map);
		if (xianbkc == null) {
			xianbkc = new VjXianbkc();
			xianbkc.setUsercenter(userCenter);
			xianbkc.setChanx(shengcxbh);
			xianbkc.setLingjbh(lingjbh);
			xianbkc.setMudd(mudd);// 仓库编码
			xianbkc.setLeix(leix); // 1仓库 2消耗点
			xianbkc.setDanw(danw); // 零件的单位
			xianbkc.setYingy(BigDecimal.ZERO);
			xianbkc.setPandcy(BigDecimal.ZERO);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"hlorder.insertXianbkc", xianbkc);
		}

		return xianbkc;
	}

	/*
	 * kuczhUsercenterMap 格式为： key = 用户中心 值kuczhmap kuczhmap 格式为key 用户中心+库存号
	 * 值库存置换记录 针对用户中心，每一个用户中心都需要查询，不能查询结果是否有，都要存入一个map，以免多次查询
	 */
	private Map<String, Map<String, VjKuczh>> findKuczh(String usercenter,
			Map<String, Map<String, VjKuczh>> kuczhUsercenterMap) {
		Map<String, VjKuczh> kuczhmap = kuczhUsercenterMap.get(usercenter);
		if (kuczhmap == null) {// 没有查询过为null 如果查询过且没有值为空map
			// 库存置换集合
			Map<String, String> map = new HashMap<String, String>();
			map.put("usercenter", usercenter);// 用户中心
			List<VjKuczh> kuczhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
					"hlorder.queryKuczhByParam", map);
			if (kuczhList != null) {
				kuczhmap = new HashMap<String, VjKuczh>();
				for (int i = 0; i < kuczhList.size(); i++) {
					VjKuczh kuczh = kuczhList.get(i);
					kuczhmap.put(
							kuczhCreateKey(kuczh.getUsercenter(),
									kuczh.getCangk()), kuczh);
				}
			} else {
				kuczhmap = new HashMap<String, VjKuczh>(0);
			}
			kuczhUsercenterMap.put(usercenter, kuczhmap);

		}
		return kuczhUsercenterMap;
	}

	/*
	 * 按用户中心+库存号产生mapkey
	 */
	private String kuczhCreateKey(String usercenter, String cangk) {
		return usercenter + cangk;
	}

	/**
	 * 查询资源快照库存
	 * 
	 * @param bean
	 *            按需计算中间表对象
	 * @param ziyhqrq
	 *            资源获取日期
	 * @return 资源快照(没有则数量都为0)
	 */
	public Ziykzb queryZiykzb(String usercenter, String lingjbh,
			String cangkdm, String ziyhqrq) {
		Map<String, String> map = new HashMap<String, String>();// 参数map
		map.put("usercenter", usercenter);
		map.put("lingjbh", lingjbh);
		map.put("cangkdm", cangkdm);
		map.put("ziyhqrq", ziyhqrq);
		// 查询资源快照表
		Ziykzb ziykzb = (Ziykzb) baseDao
				.selectObject("anxjis.queryZiykzb", map);
		// 查不到库存,把库存当做0计算
		if (ziykzb == null) {
			ziykzb = new Ziykzb();
			ziykzb.setAnqkc(BigDecimal.ZERO);
			ziykzb.setKucsl(BigDecimal.ZERO);
			ziykzb.setDingdlj(BigDecimal.ZERO);
			ziykzb.setJiaoflj(BigDecimal.ZERO);
			ziykzb.setXttzz(BigDecimal.ZERO);
			ziykzb.setDingdzzlj(BigDecimal.ZERO);
		}
		return ziykzb;
	}

	private BigDecimal getBigDecimalAx(Object obj) {
		if (obj != null) {
			return new BigDecimal(this.strNullAx(obj));
		} else {
			return BigDecimal.ZERO;
		}
	}

	private String strNullAx(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	/**
	 * @方法： 检查消耗比例：根据订单类型获取执行的sql语句
	 * @参数：订单类型和计划员组
	 * */
	public int checkXiaohbl(String dingdlx, String usercenter) {
		long start = System.currentTimeMillis();
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算",
				"产线消耗比例校验开始");
		this.mjWulljService.checkMJXhbl(dingdlx, usercenter);
		ArrayList<Xiaohblzjb> errorList = new ArrayList<Xiaohblzjb>();
		CommonFun.logger.debug("MJ模式产线消耗比例校验语句为：hlorder.mjCheckXiaohblPJ");
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		errorList = (ArrayList<Xiaohblzjb>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.mjCheckXiaohblPJ", map);
		if (errorList.size() > 0) {
			for (Xiaohblzjb xiaohblzjb : errorList) {
				LoginUser loginUser = AuthorityUtils.getSecurityUser();
				Map<String, String> param = new HashMap<String, String>();
				param.put("usercenter", xiaohblzjb.getUsercenter());
				param.put("lingjbh", xiaohblzjb.getLingjbh());
				param.put("shengcxbh", xiaohblzjb.getShengcxbh());
				List<Lingjxhd> xhdList = this.lingjxhdservice
						.queryShengcxByParam(param);
				try {
					if (null != xhdList && xhdList.size() > 0) {
						for (Lingjxhd lingjxhd : xhdList) {
							param.clear();
							param.put("usercenter", lingjxhd.getUsercenter());
							param.put("lingjbh", lingjxhd.getLingjbh());
							param.put("shengcxbh", lingjxhd.getShengcxbh());
							param.put("fenpqh", lingjxhd.getFenpqbh());
							param.put("gongysbh", lingjxhd.getGongysbh());
							Wullj wj = this.mjWulljService
									.queryWulljObject(param);
							String paramStr[] = new String[] {
									lingjxhd.getUsercenter(),
									lingjxhd.getLingjbh(),
									lingjxhd.getShengcxbh(),
									lingjxhd.getFenpqbh() };

							this.yicbjservice.insertError(Const.YICHANG_LX6,
									Const.YICHANG_LX6_str2, wj.getWulgyyz(),
									paramStr, lingjxhd.getUsercenter(),
									lingjxhd.getLingjbh(), loginUser,
									Const.JISMK_IL_CD);
						}
					} else {
						this.yicbjservice.saveYicbj(xiaohblzjb.getLingjbh(),
								xiaohblzjb.getUsercenter(), "用户中心:"
										+ xiaohblzjb.getUsercenter()
										+ "下零件编号为:" + xiaohblzjb.getLingjbh()
										+ "在产线:" + xiaohblzjb.getShengcxbh()
										+ "上没有消耗点.", loginUser.getUsername(),
								Const.JISMK_IL_CD, Const.YICHANG_LX6,
								loginUser.getUsername());
					}
				} catch (Exception e) {
					CommonFun.logger.error(e);
				}
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("usercenter", xiaohblzjb.getUsercenter());
				paramMap.put("lingjbh", xiaohblzjb.getLingjbh());
				paramMap.put("chanx", xiaohblzjb.getShengcxbh());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS)
						.execute("hlorder.mjDeleteOneOfMaoxqhzjTmp", paramMap);
			}
		}
		long end = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,消耗比例校验时间"
				+ (end - start) / 1000);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算",
				"产线消耗比例校验结束");
		return errorList.size();
	}
	//根据偏移量获取封闭期日期
	private String getFengbq(int px,MjMaoxqhzjcTmp maoxqhzjc){
			String method = "";// 拼接getPi方法
			if (px == 0) {
				method = "getJ" + px + "rq";
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法在（）时method="
								+ method);
			} else {
				method = "getJ" + px + "riq";
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法不在（）时method="
								+ method);
			}
	
			String pxriq = null;
			Class cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
			Method meth;
			try {
				meth = cls.getMethod(method, new Class[] {});
			} catch (SecurityException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,NoSuchMethodException");
			}// 得到Maoxqhzpc类的method拼接的方法
			Object obj;
			try {
				obj = meth.invoke(maoxqhzjc, null);
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,InvocationTargetException");
			}
	
			if (null == obj) {
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法P0fyzqxh="
								+ null);
			} else {
				try {
					pxriq = meth.invoke(maoxqhzjc, null).toString();
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,InvocationTargetException");
				}			
		}
		return pxriq;
	}
	//获取供应商最大偏移量
	private int getGongysMaxPx(String lingjbh , String usercenter){
		int maxPx = 0;
		Map<String, String> param = new HashMap<String, String>();
		// 从xqjs_dingd表里取出对应零件的最大fayrq
		param.put("lingjbh",lingjbh);
		
		//用户中心 - xss-20160422
		param.put("usercenter",usercenter);
		
		Object obj = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_XQJS).selectObject(
						"hlorder.getMaxPxMj", param);
		 if ( null!=obj && !"".equals(obj))
		 {
			 maxPx = ((BigDecimal)obj).divide(BigDecimal.ONE, 0, BigDecimal.ROUND_UP).intValue();;
		 }
		return maxPx;
	}
	
	//获取封闭期的偏移时间	
	public int getPianysj(MjMaoxqhzjcTmp maoxqhzjc,int maxPx){
		int pianysj=0;	
		//查询map
		Map<String,String> param = new HashMap<String,String>();
		// 从xqjs_dingd表里取出对应零件的最大fayrq
		param.put("usercenter", maoxqhzjc.getUsercenter());
		param.put("lingjbh", maoxqhzjc.getLingjbh());
		param.put("gonghlx", maoxqhzjc.getMos2());
		String fayrq=(String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("hlorder.getMaxYaohqsrq4Mj", param);
		//发运日期为空
		if(fayrq==null||"".equals(fayrq)){			
			return maxPx;
		}
		Date yaohqsrqDate = null;
		try {
			yaohqsrqDate = CommonFun.sdfAxparse(fayrq);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Date J0Date = null;
		try {
			J0Date = CommonFun.sdfAxparse(maoxqhzjc.getJ0rq());
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//xss-12799 如果最大要货起始日期小于资源获取日期(J0日期)，则更新要货起始日期
		if(yaohqsrqDate.before(J0Date)){
			yaohqsrqDate = J0Date;
		}
		
		//按正常流程
		if(yaohqsrqDate.before(J0Date)){		
			pianysj = maxPx;
		}else{			
			String fengbq = this.getFengbq(maxPx, maoxqhzjc);
			Date fengbqDate = null;
			try {
				fengbqDate = CommonFun.sdfAxparse(fengbq);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//封闭期大于fahzqDate,fahzqDate下一天作为封闭期。
			if(yaohqsrqDate.before(fengbqDate)){
				for(int k=0;k<Const.ZJCHANGDU;k++){
					try {
						fengbqDate = CommonFun.sdfAxparse(this.getFengbq(k, maoxqhzjc));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 找到对应的日期
					if (fengbqDate.getTime() == yaohqsrqDate.getTime()) {
						pianysj = k + 1;
						break;
					}
					if (fengbqDate.getTime() > yaohqsrqDate.getTime()) {
						pianysj = k;
						break;
					}
				}
			}else{//退出不做计算
				return -1;
			}
		}
		return pianysj;
	}
	
	/**
	 * 获取零件 的异常消耗
	 * @param param
	 * @return
	 */
	public BigDecimal getYcxh(Map<String, String> param) {
		BigDecimal yicxh = (BigDecimal) baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_XQJS).selectObject(
				"hlorder.queryMjYicxhl", param);
		return yicxh;
	}
	
	/**
	 * 获取待消耗
	 * @param param
	 * @return
	 */
	public BigDecimal getDaixh(MjMaoxqhzjcTmp maoxqhzjc,int pianysj){
		BigDecimal daixh = BigDecimal.ZERO;
		for (int x = 0; x < pianysj; x++) {
			Method meth = null;
			try {
				if (x <= 15) {
					String method = Const.GETJ + x;// 拼接getPi方法
					Class cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类

					meth = cls.getMethod(method, new Class[] {});
				}
			} catch (SecurityException e1) {
				CommonFun.logger.error(e1);
				throw new RuntimeException(
						"IlOrderService.pjMaoxqHz,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.pjMaoxqHz,NoSuchMethodException");
			}// 得到Maoxqhzpc类的method拼接的方法
			try {
				if (x > 15) {
					daixh = daixh.add(BigDecimal.ZERO);
				} else {
					daixh = daixh.add(new BigDecimal(meth.invoke(maoxqhzjc,
							null).toString()));
					CommonFun.logger.info("待消耗量："+daixh);
				}

			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.pjMaoxqHz,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.pjMaoxqHz,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.pjMaoxqHz,InvocationTargetException");
			}// 执行得到的方法，取得既定周期的数量
		}
		CommonFun.logger.debug("VJ模式毛需求-参考系汇总待消耗为：" + daixh);
		return daixh;
	}
}
