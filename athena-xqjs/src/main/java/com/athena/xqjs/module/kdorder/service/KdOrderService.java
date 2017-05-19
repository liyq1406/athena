package com.athena.xqjs.module.kdorder.service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.common.CalendarVersion;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiaohblzjb;
import com.athena.xqjs.entity.common.Zuiddhsl;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.entity.kdorder.CopyKdmxqhzc;
import com.athena.xqjs.entity.kdorder.Kdbzfpgz;
import com.athena.xqjs.entity.kdorder.Kdmaoxq;
import com.athena.xqjs.entity.kdorder.Kdmxqhz;
import com.athena.xqjs.entity.kdorder.Kdmxqhzc;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.anxorder.service.AnxMaoxqService;
import com.athena.xqjs.module.anxorder.service.AnxOrderService;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.common.service.CalendarCenterService;
import com.athena.xqjs.module.common.service.CalendarVersionService;
import com.athena.xqjs.module.common.service.GongysService;
import com.athena.xqjs.module.common.service.LingjGongysService;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.common.service.RouxblService;
import com.athena.xqjs.module.common.service.WulljService;
import com.athena.xqjs.module.common.service.XqjsLingjckService;
import com.athena.xqjs.module.common.service.ZuiddhslService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.DingdljService;
import com.athena.xqjs.module.ilorder.service.DingdmxService;
import com.athena.xqjs.module.ilorder.service.FeneService;
import com.athena.xqjs.module.ilorder.service.MaoxqmxService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:KD订单类
 * </p>
 * <p>
 * Description:KD订单类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李明
 * @version v1.0
 * @date 2012-1-18
 */

@Component
public class KdOrderService extends BaseService {
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private CalendarCenterService calendarCenterService;// -用户中心日历service
	@Inject
	private KdmxqhzService kdmxqhzService;// KD毛需求汇总service
	@Inject
	private KdmxqhzcService kdmxqhzcService;// KD毛需求汇总参考系service
	@Inject
	private YicbjService yicbjService;// 异常报警service
	@Inject
	private DingdljService dingdljService;// 订单零件service
	@Inject
	private DingdService dingdService;// 订单service
	@Inject
	private LingjService lingjService;// 参考系零件service
	@Inject
	private DingdmxService dingdmxService;// 订单明细service
	@Inject
	private ZuiddhslService zuiddhslService;// 最大订货数量service
	@Inject
	private RouxblService rouxblService;// 柔性比例service
	@Inject
	private WulljService wulljService;// 柔性比例service
	@Inject
	private GongysService gongysService;// 柔性比例service
	@Inject
	private FeneService feneservice;
	@Inject
	private AnxMaoxqService anxMaoxqService;
	@Inject
	private LingjGongysService lingjGongysService;
	@Inject
	private AnxOrderService anxOrderService;
	@Inject
	private KdmaoxqService kdmaoxqservice;
	@Inject
	private KdrddmxService kdrddmxService;
	@Inject
	private CalendarVersionService calendarversionservice;
	
	@Inject
	private XqjsLingjckService ljck;

	/**
	 * 订单修改及生效：插入订单零件
	 * 
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 * **/
	public boolean insertDingdljService(Dingdlj bean, String jihydm) throws RuntimeException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, ParseException {
		String lingjbh = bean.getLingjbh();
		String jismk = Const.JISMK_KD_CD;
		String info = "";

		// 存放查询参数的map
		Map<String, String> map = new HashMap<String, String>();
		boolean flag = false;
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String caozz = loginUser.getUsername();
		// 存放订单号
		map.put("dingdh", bean.getDingdh());
		// 获取P0发运周期序号，kd就是订单中的发运周期
		Dingd dingd = this.dingdService.queryDingdByDingdh(map);
		map.clear();
		bean.setP0fyzqxh(dingd.getFahzq());
		// 定义既定要货数量
		BigDecimal jidyhsl = BigDecimal.ZERO;
		// 清空参数map
		map.clear();
		// 设置创建时间
		bean.setCreate_time(CommonFun.getJavaTime());
		// 设置创建人为当前计划员
		bean.setCreator(bean.getJihyz());
		// 获取订单内容，得到既定的个数
		int count = this.dingdljService.getDingdnr(bean.getDingdnr());
		// 根据订单内容来获得既定要货数量
		if (count == 0) {
			// 当订单内容中没有9的时候，既定要货数量为p0数量
			bean.setJidyhsl(bean.getP0sl());
		} else {
			// 反射获取到类
			Class<? extends Dingdlj> cls = bean.getClass();
			// 定义动态的方法值
			String sl = null;
			for (int k = 0; k < count; k++) {
				// 动态获取
				sl = "getP" + k + "sl";
				// 获取到get方法
				Method meth = cls.getMethod(sl, new Class[] {});
				// 调用meth方法,将其转换成对应的数据类型
				BigDecimal psl = (BigDecimal) meth.invoke(bean);
				jidyhsl = (psl == null ? BigDecimal.ZERO : psl).add(jidyhsl);
			}
			bean.setJidyhsl(jidyhsl);
		}
		bean.setGongyslx("2");
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("gongysbh", bean.getGongysdm());
		// 从物流路径中得到路径代号和订货仓库以及指定供应商等信息
		List<Wullj> wullj = this.wulljService.queryWulljForKd(map);
		map.clear();
		if ( null == wullj || wullj.size() == 0) {

			String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh(), "订货仓库" };
			this.yicbjService.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str12, jihydm, paramStr, bean.getUsercenter(), bean.getLingjbh(),
					loginUser, jismk);

			// info = "没有订货仓库";
			// this.yicbjService.saveYicInfo(jismk, lingjbh, Const.CUOWLX_200,
			// info);
			return false;
		}
		else if(StringUtils.isBlank(wullj.get(0).getDinghck()) && StringUtils.isBlank(wullj.get(0).getXianbck()))
		{
			String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh(), "订货仓库" };
			this.yicbjService.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str12, jihydm, paramStr, bean.getUsercenter(), bean.getLingjbh(),
					loginUser, jismk);
			return false;
		}
		bean.setXiehzt(wullj.get(0).getXiehztbh());
		bean.setLujdm(wullj.get(0).getLujbh());
		bean.setCangkdm(wullj.get(0).getDinghck()!=null?wullj.get(0).getDinghck():wullj.get(0).getXianbck());
		bean.setZhidgys(wullj.get(0).getZhidgys());
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("gongysbh", bean.getGongysdm());
		map.put("ziyhqrq", bean.getZiyhqrq());
		map.put("cangkdm", bean.getCangkdm());
		map.put("cangkbh", bean.getCangkdm());
		//查询零件仓库
		//Lingjck lingjck = ljck.querySingle(map);
		//if(null != lingjck)
		//{
		//	bean.setXiehzt(lingjck.getXiehztbh());//设置卸货站台
		//}
		// 查询资源快照表
		Ziykzb ziykzb = this.anxMaoxqService.queryZiykz(map);
		map.clear();
		if (null != ziykzb) {
			if (ziykzb.getXttzz() == null) {
				bean.setXittzz(BigDecimal.ZERO);
			} else {
				bean.setXittzz(ziykzb.getXttzz());
			}
			if (ziykzb.getKucsl() == null) {
				bean.setKuc(BigDecimal.ZERO);
			} else {
				bean.setKuc(ziykzb.getKucsl());
			}
			if (ziykzb.getAnqkc() == null) {
				bean.setAnqkc(BigDecimal.ZERO);
			} else {
				bean.setAnqkc(ziykzb.getAnqkc());
			}
			if (ziykzb.getJstzsz() == null) {
				bean.setTiaozjsz(BigDecimal.ZERO);
			} else {
				bean.setTiaozjsz(new BigDecimal(ziykzb.getJstzsz()));
			}

			if (ziykzb.getJiaoflj() == null) {
				bean.setJiaoflj(BigDecimal.ZERO);
			} else {
				bean.setJiaoflj(ziykzb.getJiaoflj());
			}
			if (ziykzb.getDingdlj() == null) {
				bean.setDingdlj(BigDecimal.ZERO);
			} else {
				bean.setDingdlj(ziykzb.getDingdlj());
			}
		} else {
			bean.setXittzz(BigDecimal.ZERO);
			bean.setKuc(BigDecimal.ZERO);
			bean.setAnqkc(BigDecimal.ZERO);
			bean.setTiaozjsz(BigDecimal.ZERO);
			bean.setJiaoflj(BigDecimal.ZERO);
			bean.setDingdlj(BigDecimal.ZERO);
		}
		// 查询零件供应商得到包装信息
		LingjGongys lingjGongys = this.lingjGongysService.select(bean.getUsercenter(), bean.getGongysdm(), bean.getLingjbh());
		if (null != lingjGongys) {
			bean.setUabzlx(lingjGongys.getUabzlx());
			bean.setUabzuclx(lingjGongys.getUcbzlx());
			bean.setUabzucsl(lingjGongys.getUaucgs());
			bean.setGongysfe(lingjGongys.getGongyfe());
			bean.setFahd(lingjGongys.getFayd());
		}
		// 查询零件得到单位信息
		Lingj lingj = this.lingjService.queryObject(bean.getLingjbh(), bean.getUsercenter());
		if (null != lingj) {
			bean.setDanw(lingj.getDanw());
		}
		flag = this.dingdljService.doInsert(bean);
		Gongys gys = gongysService.queryObject(bean.getGongysdm(), bean.getUsercenter());
		Map<String, String> ddMap = new HashMap<String, String>();
		ddMap.put("dingdh", bean.getDingdh());
		Dingd dd = this.dingdService.queryDingdByDingdh(ddMap);
		String rilbc = "";
		if (dd.getDingdlx().equals(Const.DINGDLX_KD) || dd.getDingdlx().equals(Const.DINGDLX_TS)) {
			rilbc = Const.KDBANCI;
		} else if (dd.getDingdlx().equals(Const.DINGDLX_AIX)) {
			rilbc = Const.AXBANCI;
		}
		if (flag) {
			// 周要货计算，插入到订单明细
			if (null != lingj && null != gys) {

				this.newWeekOrderCount(bean, jihydm, lingj.getZhongwmc(), gys.getGongsmc(), gys.getNeibyhzx(), caozz, rilbc, loginUser, wullj.get(0)
						.getGcbh());

			} else {
				this.newWeekOrderCount(bean, jihydm, null, null, null, caozz, rilbc, loginUser, wullj.get(0).getGcbh());
			}

		}
		return flag;
	}

	/**
	 * 订单修改及生效：查询订单行
	 * **/
	public List<Dingdmx> queryDetailOrderLineService(List<Dingdmx> list) {
		Map<String, String> map = new HashMap<String, String>();
		List<Dingdmx> all = new ArrayList<Dingdmx>();
		String rilbc = "";
		if (list.size() > 0) {
			map.put("dingdh", list.get(0).getDingdh());
			Dingd dd = this.dingdService.queryDingdByDingdh(map);
			if (dd != null && (dd.getDingdlx().equals("1") || dd.getDingdlx().equals("4"))) {
				rilbc = Const.KDBANCI;
			} else if (dd != null && dd.getDingdlx().equals("2")) {
				rilbc = Const.AXBANCI;
			}
		}
		for (Dingdmx mx : list) {
			// 有年周期和用户中心查询出有多少周序
			CalendarVersion calendarversion = this.calendarversionservice.queryCalendarVersionNianzq(mx.getFayrq().substring(0, 10), Const.KDRILI,
					rilbc);
			Dingdmx dmx = new Dingdmx();
			dmx.setDingdh(mx.getDingdh());
			dmx.setUsercenter(mx.getUsercenter());
			dmx.setId(mx.getId());
			dmx.setEditor(mx.getEditor());
			dmx.setEdit_time(mx.getEdit_time());
			dmx.setLingjbh(mx.getLingjbh());
			if(calendarversion!=null){
				dmx.setFahzq(calendarversion.getNianzq());
			}
			dmx.setShul(mx.getShul());
			dmx.setUabzucrl(mx.getUabzucrl());
			dmx.setUabzucsl(mx.getUabzucsl());
			dmx.setFaykssj(mx.getYaohqsrq() == null ? null : mx.getYaohqsrq().substring(0, 10));
			dmx.setFayjssj(mx.getYaohjsrq() == null ? null : mx.getYaohjsrq().substring(0, 10));
			dmx.setDanw(mx.getDanw());
			dmx.setUabzuclx(mx.getUabzuclx());
			dmx.setGongysdm(mx.getGongysdm());
			dmx.setFayrq(mx.getFayrq() == null ? null :mx.getFayrq().substring(0, 10));
			// 从零件中得到零件名称
			Lingj lj = lingjService.select(mx.getUsercenter(), mx.getLingjbh());
			dmx.setLingjmc(lj == null ? null : lj.getZhongwmc());
			all.add(dmx);
		}
		return all;
	}

	/**
	 * 订单修改及生效：更新订单行
	 * **/
	public Map<String, String> upDateDingdmxService(ArrayList<Dingdmx> allDingdmx) {
		// 定义返回标记
		String message = null;
		boolean flag = false;
		Map<String, String> map = new HashMap<String, String>();
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		// 判断传递进来的集合不为空
		if (!allDingdmx.isEmpty()) {
			Dingd dingd = null;
			boolean flagDingd = true;
			// 实例化订单行
			Dingdmx mx = new Dingdmx();
			// 开始循环结果集
			for (Dingdmx bean : allDingdmx) {
				if(flagDingd)
				{
					flagDingd = false;
					if(StringUtils.isNotBlank(bean.getDingdh()))
					{
						map.put("dingdh", bean.getDingdh());
						dingd = dingdService.queryDingdByDingdh(map);
					}
				}
				Map<String, String> mxMap = new HashMap<String, String>();
				mxMap.put("id", bean.getId());
				Dingdmx upmx = this.dingdmxService.queryDingdmxObject(mxMap);
				LingjGongys lingjgys = this.lingjGongysService.select(upmx.getUsercenter(), upmx.getGongysdm(), upmx.getLingjbh());
				if (bean.getShul().longValue() % (lingjgys.getUaucgs().multiply(lingjgys.getUcrl()).longValue()) != 0) {
					message = "编号为" + upmx.getLingjbh() + "的零件数量不为包装的整数倍。修改失败！";
					map.put("flag", "false");
					map.put("message", message);
					return map;
				} else {
					// 设置ID
					mx.setId(bean.getId());
					// 设置数量
					mx.setShul(bean.getShul());
					if(null != dingd && StringUtils.isNotBlank(dingd.getDingdlx()) && Const.DINGDLX_TS.equals(dingd.getDingdlx()))
					{
						if(bean.getFayjssj().compareTo(bean.getFaykssj())<0){
							//开始时间不能大于或等于结束时间
							message = "开始时间不能大于结束时间," + bean.getLingjbh() + "更新失败！";
							map.put("flag", "false");
							map.put("message", message);
							return map;
						}
						mx.setFaykssj(bean.getFaykssj());
						mx.setFayjssj(bean.getFayjssj());
					}
					// 更新订单明细(只修改---修改数量，保持原来的计算数量)
					flag = this.dingdmxService.doUpdateMx(mx);
					message = "更新完成";
					if (!flag) {
						message = message + "," + bean.getLingjbh() + "更新失败！";
						map.put("flag", "false");
						map.put("message", message);
					} else {

						Map<String, String> zhouqiMap = new HashMap<String, String>();
						zhouqiMap.put("id", mx.getId());
						Dingdmx dingdmx = this.dingdmxService.queryDingdmxObject(zhouqiMap);
						zhouqiMap.clear();
						zhouqiMap.put("usercenter", dingdmx.getUsercenter());
						zhouqiMap.put("riq", dingdmx.getYaohqsrq().substring(0, 10));
						CalendarCenter calendarcenter = this.calendarCenterService.queryCalendarCenterObject(zhouqiMap);
						zhouqiMap.clear();
						zhouqiMap.put("usercenter", dingdmx.getUsercenter());
						zhouqiMap.put("nianzq", calendarcenter.getNianzq());
						List<CalendarCenter> cList = this.calendarCenterService.getStartandEndDay(zhouqiMap);
						BigDecimal sumShul = this.dingdmxService.sumShul(dingdmx.getDingdh(), dingdmx.getLingjbh(), dingdmx.getUsercenter(),
								dingdmx.getGongysdm(), cList.get(0).getRiq().substring(0, 10), cList.get(cList.size() - 1).getRiq().substring(0, 10));
						zhouqiMap.clear();
						zhouqiMap.put("usercenter", dingdmx.getUsercenter());
						zhouqiMap.put("dingdh", dingdmx.getDingdh());
						zhouqiMap.put("lingjbh", dingdmx.getLingjbh());
						zhouqiMap.put("gongysdm", dingdmx.getGongysdm());
						zhouqiMap.put("cangkdm", dingdmx.getCangkdm());
						Dingdlj dingdlj = this.dingdljService.queryDingljObject(zhouqiMap);
						Integer index = CommonFun.subValue(dingdlj.getP0fyzqxh(), calendarcenter.getNianzq(), Const.MAXZQ);
						String method = "";
						Method methdj;
						Class cls = dingdlj.getClass();// 得到Dingdlj类
						method = "setP" + index + "sl";// 拼接getPi方法
						try {
							methdj = cls.getMethod(method, BigDecimal.class);
						} catch (SecurityException e) {
							throw new RuntimeException(e);
						} catch (NoSuchMethodException e) {
							throw new RuntimeException(e);
						}// 得到Dingdlj类的method拼接的方法
						try {
							methdj.invoke(dingdlj, sumShul);
						} catch (IllegalArgumentException e) {
							throw new RuntimeException(e);
						} catch (IllegalAccessException e) {
							throw new RuntimeException(e);
						} catch (InvocationTargetException e) {
							throw new RuntimeException(e);
						}// 执行得到的方法，setPx
						dingdlj.setEditor(loginUser.getUsername());
						dingdlj.setEdit_time(CommonFun.getJavaTime());
						this.dingdljService.doUpdateForKd(dingdlj);
						this.dingdService.updateDingdzt(bean.getDingdh());
					}

				}

			}
		}
		map.put("flag", "true");
		map.put("message", message);
		return map;
	}

	/**
	 * 订单修改及生效：更新订单零件
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ParseException
	 * **/
	public boolean upDateDingdljService(ArrayList<Dingdlj> allDingdlj) throws SecurityException, IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, ParseException {
		Map<String, String> map = new HashMap<String, String>();
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String caozz = loginUser.getUsername();
		String gcbh = "";
		boolean flag = false;
		for (Dingdlj bean : allDingdlj) {
			// 根据订单内容得到求既定的方法
			int count = this.dingdljService.getDingdnr(bean.getDingdnr());

			bean.setGongysdm(bean.getGongysdm());
			bean.setEditor(caozz);
			bean.setEdit_time(CommonFun.getJavaTime());
			// 更新订单零件
			flag = this.dingdljService.doUpdateForKd(bean);
			if (count >= 1) {
				map.put("id", bean.getId());
				map.put("lingjbh", bean.getLingjbh());
				map.put("usercenter", bean.getUsercenter());
				map.put("gongysdm",bean.getGongysdm());
				bean = this.dingdljService.queryDingljObject(map);
				map.clear();
				// 计算周要货量，修改订单明细
				Map<String, String> ddMap = new HashMap<String, String>();
				ddMap.put("dingdh", bean.getDingdh());
				Dingd dd = this.dingdService.queryDingdByDingdh(ddMap);
				String rilbc = "";

				if ((dd.getDingdlx().equals(Const.DINGDLX_KD)) || (dd.getDingdlx().equals(Const.DINGDLX_TS))) {

					rilbc = Const.KDBANCI;
				} else if (dd.getDingdlx().equals(Const.DINGDLX_AIX)) {
					rilbc = Const.AXBANCI;
				}
				Gongys gys = gongysService.queryObject(bean.getGongysdm(), bean.getUsercenter());
				Lingj lingj = this.lingjService.queryObject(bean.getLingjbh(), bean.getUsercenter());
				map.clear();
				map.put("dingdh", bean.getDingdh());
				map.put("lingjbh", bean.getLingjbh());
				map.put("usercenter", bean.getUsercenter());
				map.put("gongysdm", bean.getGongysdm());
				List<Dingdmx> list = this.dingdmxService.queryDingdmxMap(map);
				if (list.size() > 0) {
					gcbh = list.get(0).getGcbh();
				}
				this.dingdmxService.deleteList(bean.getUsercenter(), bean.getDingdh(), bean.getGongysdm(), bean.getLingjbh());
				this.newWeekOrderCount(bean, bean.getJihyz(), lingj.getZhongwmc(), gys.getGongsmc(), gys.getNeibyhzx(), caozz, rilbc, loginUser, gcbh);
			}
		}
		return flag;
	}

	/**
	 * kd行列转换：汇总数据到kdmxqhz表中
	 * 
	 * @参数说明：CalendarCenter centerObject, String usercenter, String banc, String
	 *                      zhizlx, String riq, String jismk, String cuowlx
	 */
	private void collectDataKdmxqhz(CalendarVersion centervObject, String usercenter, String banc, String zhizlx, String riq, String jismk,
			String cuowlx, String rilbc, String jihyz, LoginUser loginuser) {
		if (null != centervObject) {
			// 参数map
			Map<String, String> map = new HashMap<String, String>();
			map.put("usercenter", Const.KDRILI);
			map.put("nianzq", centervObject.getNianzx().substring(0, 4));
			map.put("banc", rilbc);
			// 取得最大年周序
			CommonFun.mapPrint(map, "汇总数据到kdmxqhz表中collectDataKdmxqhz方法参数map");
			CalendarVersion centerMax = this.calendarversionservice.maxTime(map);
			CommonFun.objPrint(centerMax, "汇总数据到kdmxqhz表中collectDataKdmxqhz方法centerMax");
			map.clear();
			Map<String, String> maps = MaoxqmxService.getZhoux(centervObject.getNianzx(), 40, centerMax.getNianzx());
			maps.put("usercenter", usercenter);
			maps.put("xuqbc", banc);
			maps.put("zhizlx", zhizlx);
			maps.put("riq", riq);
			maps.put("s0sszxh", centervObject.getNianzx());
			List<Kdmxqhz> allKdmxqhz = this.kdmxqhzService.queryListKdmxqhzForConvert(maps);
			CommonFun.objListPrint(allKdmxqhz, "汇总数据到kdmxqhz表中collectDataKdmxqhz方法allKdmxqhz");
			maps.clear();
			if (!allKdmxqhz.isEmpty()) {
				// 插入到表中
				this.kdmxqhzService.listInsert(allKdmxqhz, new Kdmxqhz());
			}
		} else {
			String paramStr[] = new String[] { Const.KDRILI, riq };
			this.yicbjService.insertError(cuowlx, Const.YICHANG_LX3_str4, jihyz, paramStr, Const.KDRILI, null, loginuser, jismk);
			// String cuowxxxx = "在中心日历中没有用户中心为"+usercenter+"和日期为" + riq +
			// "对应的周序";
			// this.yicbjService.saveYicInfo(jismk, "日历错误", cuowlx, cuowxxxx);
		}
	}

	/**
	 * kd行列转换：过滤出关联后消耗比例为空或者为0的数据，插入到异常报警表中
	 * 
	 * @参数说明：List<Kdmaoxq> list;String jismk,String cuowlx
	 */
	private void checkXhblNullAndZreo(List<Kdmaoxq> list, String jismk, String cuowlx) {
		if (!list.isEmpty()) {
			CommonFun.objListPrint(list, "kd件产线消耗点比例校验结果list");
			for (Kdmaoxq kdmaoxq : list) {
				String cuowxxxx = "零件所在编号为" + kdmaoxq.getChanx() + "产线上得消耗点的消耗比例为空、为0或者产线的消耗比例之和不为1";
				this.yicbjService.saveYicInfo(jismk, kdmaoxq.getLingjbh(), cuowlx, cuowxxxx);
				// 参数map
				Map<String, String> map = new HashMap<String, String>();
				map.put("usercenter", kdmaoxq.getUsercenter());
				map.put("lingjbh", kdmaoxq.getLingjbh());
				map.put("chanx", kdmaoxq.getChanx());
				this.kdmaoxqservice.doDeleteOne(map);
			}
		}
	}

	/**
	 * kd行列转换：计算周序
	 * 
	 * @throws Exception
	 * @参数说明：Kdmxqhzc bean, String jismk, String cuowlx, CalendarCenter center,
	 *                String jiszq
	 */
	private String countZhoux(Kdmxqhzc bean, String jismk, String cuowlx, CalendarVersion centerv, String jiszq, String rilbc, String jiyhz,
			LoginUser loginuser) {
		String zhouqi = "";
		String zhoux = "";
		String zhouxs = "";
		if (null != centerv) {
			// s0年周期
			String s0 = jiszq;
			CommonFun.logger.debug("kd行列转换：计算周序s0=" + s0);
			CommonFun.logger.debug("kd行列转换：计算周序Beihzq=" + bean.getBeihzq());
			if (null != bean.getBeihzq()) {
				// 备货周期取整
				int index = bean.getBeihzq().divide(new BigDecimal(Const.MONTH_30), 0, BigDecimal.ROUND_UP).intValue();
				CommonFun.logger.debug("kd行列转换：计算周序Beihzq=" + bean.getBeihzq());
				// 得到取整之后的map，从而得到取整后的周期数
				Map<String, String> zhouq = MaoxqmxService.getZhoux(s0, index, s0.substring(0, 4) + Const.MAXZQ);
				CommonFun.mapPrint(zhouq, "得到取整之后的map，从而得到取整后的周期数zhouq");
				// 在取整后的周期数上加1
				zhouqi = CommonFun.addNianzq(zhouq.get("s" + (zhouq.size() - 1)), Const.MAXZQ, 1);
				CommonFun.logger.debug("kd行列转换：计算周序zhouqi=" + zhouqi);
			} else {
				String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh(), "备货周期" };
				this.yicbjService.insertError(cuowlx, Const.YICHANG_LX2_str12, jiyhz, paramStr, bean.getUsercenter(), bean.getLingjbh(), loginuser,
						jismk);
				// String cuowxxxx = "备货周期为空";
				// this.yicbjService.saveYicInfo(jismk, bean.getLingjbh(),
				// cuowlx, cuowxxxx);
			}
			// 参数map
			Map<String, String> map = new HashMap<String, String>();
			map.put("usercenter", bean.getUsercenter());
			map.put("nianzq", zhouqi);
			// 按照日期倒序排列，取得全部周序,用于得到p0周期的起始周序
			List<CalendarVersion> all = this.calendarversionservice.queryCalendarVersionByNianzq(Const.KDRILI, rilbc, zhouqi);
			CommonFun.objListPrint(all, "按照日期倒序排列，取得全部周序,用于得到p0周期的起始周序all");
			map.clear();
			map.put("usercenter", Const.KDRILI);
			map.put("nianzq", zhouqi.substring(0, 4));
			map.put("banc", rilbc);
			// 取得最大年周序
			CommonFun.mapPrint(map, "汇总数据到kdmxqhz表中collectDataKdmxqhz方法参数map");
			CalendarVersion centerMax = this.calendarversionservice.maxTime(map);
			// map.put("usercenter", bean.getUsercenter());
			// map.put("nianzq", zhouqi.substring(0, 4));
			// // 获取到本年内的最大年周序
			// CalendarCenter center2 = this.calendarCenterService.maxTime(map);
			map.clear();
			if (!all.isEmpty()) {
				// 得到最小的周序
				zhoux = all.get(0).getNianzx();
				CommonFun.logger.debug("kd行列转换：计算周序zhoux=" + zhoux);
				zhouxs = zhoux;
				CommonFun.logger.debug("kd行列转换：计算周序zhouxs=" + zhouxs);
				if (null != bean.getFayzq()) {
					// 发货周期/7取整
					int value = bean.getFayzq().divide(new BigDecimal(Const.WEEK_7), 0, BigDecimal.ROUND_UP).intValue();
					CommonFun.logger.debug("kd行列转换：计算周序发货周期/7取整value=" + value);
					// 得到取整之后的map，从而得到平移value周数后的周序
					Map<String, String> zhous = MaoxqmxService.getZhoux(zhoux, value, centerMax.getNianzx());
					CommonFun.mapPrint(zhous, "得到取整之后的map，从而得到平移value周数后的周序zhous");
					// 得到平移value周数后的周序
					zhoux = zhous.get("s" + (zhous.size() - 1));
					CommonFun.logger.debug("kd行列转换：计算周序得到平移value周数后的周序zhoux=" + zhoux);
					zhouxs = zhoux;
					CommonFun.logger.debug("kd行列转换：计算周序zhouxs=" + zhouxs);
				} else {
					String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh(), "发运周期" };
					this.yicbjService.insertError(cuowlx, Const.YICHANG_LX2_str12, jiyhz, paramStr, bean.getUsercenter(), bean.getLingjbh(),
							loginuser, jismk);
					// String cuowxxxx = "发运周期为空";
					// this.yicbjService.saveYicInfo(jismk, bean.getLingjbh(),
					// cuowlx, cuowxxxx);
				}
			}
		}
		return zhouxs;
	}

	/**
	 * kd行列转换：计算库存
	 * 
	 * @throws Exception
	 * 
	 * @参数说明：Kdmxqhzc bean;String jismk,String cuowlx
	 */
	private BigDecimal countKuc(Kdmxqhzc bean, String jismk, String cuowlx, String riq) {
		BigDecimal kuc = BigDecimal.ZERO;
		if (bean.getCangklx().equalsIgnoreCase(Const.CANGKLX)) {
			// 参数map
			Map<String, String> map = new HashMap<String, String>();
			map.put("usercenter", bean.getUsercenter());
			map.put("lingjbh", bean.getLingjbh());
			map.put("dinghck", bean.getDinghck());
			map.put("ziyhqrq", riq);
			CommonFun.mapPrint(map, "kd行列转换：计算库存查询资源快照表参数map");
			// 得到线边库存数量
			Ziykzb ziykzb = this.kdmxqhzService.queryAllZiykzb(map);
			CommonFun.objPrint(ziykzb, "kd行列转换：计算库存查询资源快照结果类ziykzb");
			map.clear();
			// 全部库存=线边+订货
			if (null == ziykzb) {
				kuc = bean.getKuc();
				CommonFun.logger.debug("kd行列转换：计算库存在（null == ziykzb）时kuc=" + kuc);
			} else {
				if (bean.getKuc() == null) {
					kuc = BigDecimal.ZERO.add(ziykzb.getKucsl());
				} else {
					kuc = bean.getKuc().add(ziykzb.getKucsl());
				}

				CommonFun.logger.debug("kd行列转换：计算库存不在（null == ziykzb）时kuc=" + kuc);
			}
		} else {
			kuc = bean.getKuc();
			CommonFun.logger.debug("kd行列转换：计算库存不为订货仓库时kuc=" + kuc);
		}
		return kuc;
	}

	/**
	 * kd行列转换：计算待消耗
	 * 
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * 
	 * @throws Exception
	 * 
	 * @参数说明：Kdmxqhzc bean list;String jismk,String cuowlx
	 */
	private BigDecimal countDaixh(Kdmxqhzc bean, String s0zx, String zhoux, String jismk, String cuowlx, CalendarVersion centervs, String jihyz,
			LoginUser loginuser) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// 二者之间的周序的差距为：
		int sn = 0;
		// 得到差距
		sn = CommonFun.subValue(s0zx, zhoux, centervs.getNianzx().substring(4, centervs.getNianzx().length()));
		CommonFun.logger.debug("kd行列转换：计算待消耗得到差距sn=" + sn);
		// 判断sn不大于40或者为负数
		if (sn - 40 > 0) {
			sn = 41;
			CommonFun.logger.debug("kd行列转换：计算待消耗得到差距当(sn不大于40或者为负数)sn=" + sn);
		} else if (sn < 0) {
			sn = 0;
			String paramStr[] = new String[] { Const.KDRILI, zhoux };
			this.yicbjService.insertError(cuowlx, Const.YICHANG_LX3_str4, jihyz, paramStr, Const.KDRILI, bean.getLingjbh(), loginuser, jismk);
			// String cuowxxxx = "中心日历对应周序错误";
			// this.yicbjService.saveYicInfo(jismk, bean.getLingjbh(), cuowlx,
			// cuowxxxx);
		}
		// 反射获取到类
		Class<? extends Kdmxqhzc> cls = bean.getClass();
		// 定义动态的方法值
		String sl = null;
		BigDecimal daixh = BigDecimal.ZERO;
		for (int k = 0; k < sn; k++) {
			// 动态获取
			sl = Const.GETS + k;
			CommonFun.logger.debug("kd行列转换：计算待消耗动态获取sl=" + sl);
			// 获取到方法
			Method meth = cls.getMethod(sl, new Class[] {});
			Object object = meth.invoke(bean, null);
			if (null == object) {
				String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh(), "待消耗" };
				this.yicbjService.insertError(cuowlx, Const.YICHANG_LX2_str12, jihyz, paramStr, bean.getUsercenter(), bean.getLingjbh(), loginuser,
						jismk);
			} else {
				// 调用meth方法,将其转换成对应的数据类型
				daixh = new BigDecimal(meth.invoke(bean, null).toString()).add(daixh);
				CommonFun.logger.debug("kd行列转换：计算待消耗动态获取daixh=" + daixh);
			}
		}
		return daixh;
	}

	public void kdLineconvertRow(String[] usercenters, String riq, String[] bancs, String zhizlx, String jiszq, String jihyz, String jihydm,
			String dingdnr, String rilbc, LoginUser loginuser) throws SecurityException, IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算", "行列转换开始");
		// 参数map

		Map<String, String> map = new HashMap<String, String>();
		// 去掉为null,"",重复的值
		String[] usercenter = CommonFun.getArray(usercenters);
		CommonFun.logger.debug("kd件行列转换usercenter=" + usercenter.toString());
		// 去掉为null,"",重复的值
		String[] banc = CommonFun.getArray(bancs);
		CommonFun.logger.debug("kd件行列转换banc=" + banc.toString());
		String jismk = "32";
		String cuowlx = Const.CUOWLX_200;
		for (int i = 0; i < banc.length; i++) {
			for (int j = 0; j < usercenter.length; j++) {
				// 查询指定用户中心下该版次的毛需求插入中间表
				CommonFun.logger.debug("kd件行列转换第" + j + "个usercenter=" + usercenter[j].toString());
				CommonFun.logger.debug("kd件行列转换第" + i + "个banc=" + banc[i].toString());
				this.kdmaoxqservice.selectXuq(usercenter[j], banc[i]);
				/**
				 * 第一步：过滤出关联后消耗比例为空或者为0的数据，插入到异常报警表中
				 * **/
				// map.put("usercenter", usercenter[j]);
				// map.put("xuqbc", banc[i]);
				// map.put("zhizlx", zhizlx);
				// List<Kdmaoxq> list =
				// this.kdmxqhzService.queryNullinsert(map);
				// map.clear();
				// this.checkXhblNullAndZreo(list, jismk, cuowlx);
				/**
				 * 第二步：得到s0周序
				 * **/
				map.put("usercenter", usercenter[j]);
				map.put("riq", riq);

				CalendarVersion calendarVersionobj = this.calendarversionservice.queryCalendarVersionNianzq(riq, Const.KDRILI, rilbc);
				CommonFun.objPrint(calendarVersionobj, "kd件行列转换calendarVersionobj");
				map.clear();
				/**
				 * 第三步：汇总到kdmxqhz表中
				 * **/
				this.collectDataKdmxqhz(calendarVersionobj, usercenter[j], banc[i], zhizlx, riq, jismk, cuowlx, rilbc, jihyz, loginuser);
			}
		}
		// 查询毛需求汇总表数据,得到所有的用户中心和零件编号
		map.put("ziyhqrq", riq);
		List<Kdmxqhzc> list = this.kdmxqhzcService.queryListKdmxqhzcForConvert(map);
		CommonFun.objListPrint(list, "kd件毛需求-参考系汇总queryListKdmxqhzcForConvert方法结果list");
		map.clear();
		if (!list.isEmpty()) {
			for (Kdmxqhzc bean : list) {
				bean.setDingdnr(dingdnr);
				// 判断订货仓库是否为空
				CommonFun.logger.debug("kd件毛需求-参考系汇总Dingdnr=" + dingdnr);
				if (null == bean.getDinghck()) {
					bean.setDaixh(BigDecimal.ZERO);
					bean.setKuc(BigDecimal.ZERO);
				} else {
					// 计算库存
					BigDecimal kuc = this.countKuc(bean, jismk, cuowlx, riq);
					CommonFun.logger.debug("kd件毛需求-参考系汇总kuc=" + kuc);
					bean.setKuc(kuc);
					/**
					 * 求待消耗数量 第一步：得到s0所在年周期
					 * **/
					CalendarVersion calendarVersionobj = this.calendarversionservice.queryCalendarVersionNianzq(riq, Const.KDRILI, rilbc);
					map.clear();
					map.put("usercenter", Const.KDRILI);
					map.put("nianzq", calendarVersionobj.getNianzx().substring(0, 4));
					map.put("banc", rilbc);
					// 取得最大年周序
					CommonFun.mapPrint(map, "汇总数据到kdmxqhz表中collectDataKdmxqhz方法参数map");
					CalendarVersion centerMax = this.calendarversionservice.maxTime(map);
					map.clear();
					// 资源获取日期所在年周序
					String s0zx = calendarVersionobj.getNianzx();
					CommonFun.logger.debug("kd件毛需求-参考系汇总s0zx=" + s0zx);
					/**
					 * 第二步：得到s0所在年周期+备货周期/30取整周的年周期，从而得到周序
					 * **/
					// 计算得到周序数
					String zhouxs = this.countZhoux(bean, jismk, cuowlx, centerMax, jiszq, rilbc, jihyz, loginuser);
					CommonFun.logger.debug("kd件毛需求-参考系汇总zhouxs=" + zhouxs);
					// 设置待消耗
					bean.setDaixh(this.countDaixh(bean, s0zx, zhouxs, jismk, cuowlx, centerMax, jihyz, loginuser));
					CommonFun.logger.debug("kd件毛需求-参考系汇总Daixh=" + bean.getDaixh());
				}
				// 设置计划员组

				this.checkFields(bean);
				this.kdmxqhzcService.listInsertMxqhzcByObject(bean);
			}
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算", "行列转换结束");
	}

	/**
	 * 检验数指是否为空，为空将其置为0
	 */
	private void checkFields(Kdmxqhzc bean) {
		BigDecimal value = BigDecimal.ZERO;
		CommonFun.logger.debug("检验数指是否为空，为空将其置为0,checkFields方法Anqkc=" + bean.getAnqkc());
		if (null == bean.getAnqkc()) {
			bean.setAnqkc(value);
		}
		CommonFun.logger.debug("检验数指是否为空，为空将其置为0,checkFields方法Kuc=" + bean.getKuc());
		if (null == bean.getKuc()) {
			bean.setKuc(value);
		}
		CommonFun.logger.debug("检验数指是否为空，为空将其置为0,checkFields方法Dinghaqkc=" + bean.getDinghaqkc());
		if (null == bean.getDinghaqkc()) {
			bean.setDinghaqkc(value);
		}
		CommonFun.logger.debug("检验数指是否为空，为空将其置为0,checkFields方法Dingdlj=" + bean.getDingdlj());
		if (null == bean.getDingdlj()) {
			bean.setDingdlj(value);
		}
		CommonFun.logger.debug("检验数指是否为空，为空将其置为0,checkFields方法Jiaoflj=" + bean.getJiaoflj());
		if (null == bean.getJiaoflj()) {
			bean.setJiaoflj(value);
		}
	}

	/**
	 * 检验实体中设置的关键字段是否为空
	 * 
	 * @author 李明
	 * @version v1.0
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2012-1-30
	 */
	private String checkNull(Field[] fields, Object bean) throws IllegalArgumentException, IllegalAccessException {
		String message = "true";
		// 开始循环实体字段
		for (int i = 0; i < fields.length - 3; i++) {
			// 得到字段
			Field f = fields[i];
			CommonFun.logger.debug("检验实体中设置的关键字段是否为空checkNull方法fields[i]=" + f);
			// 修改对象的accessible标志，是的对象可以访问
			f.setAccessible(true);
			CommonFun.logger.debug("检验实体中设置的关键字段是否为空checkNull方法f.get(bean)=" + f.get(bean));
			// 判断对象是都为空
			if (f.get(bean) == null) {
				// 若第一个字段出错则报毛需求错误，否则就报参考系参数为空
				if (i == 1) {
					message = "100" + Const.MXQ_ERROR;
				} else {
					message = "200" + "参考系" + f.getName() + Const.CONDITIONS_NULL;
				}
				break;
			}
		}
		return message;
	}

	/**
	 * kd检验关键字段
	 * 
	 * @author 李明
	 * @version v1.0
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2012-1-30
	 */
	public int checkKeyNumber() throws IllegalArgumentException, IllegalAccessException {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算", "检验关键字段开始");
		String message = null;
		String jismk = Const.JISMK_KD_CD;
		int count = 0;
		// 获取到所有信息
		List<CopyKdmxqhzc> listKdmxqhzc = this.kdmxqhzcService.queryListCopyKdmxqhzc();
		CommonFun.objListPrint(listKdmxqhzc, "kd检验关键字段checkKeyNumber方法listKdmxqhzc");
		// 判断得到的结果集不为空
		if (!listKdmxqhzc.isEmpty()) {
			for (CopyKdmxqhzc obj : listKdmxqhzc) {
				// 获取到所有的字段集合
				Field[] fields = CopyKdmxqhzc.class.getDeclaredFields();
				CommonFun.logger.debug("kd检验关键字段checkKeyNumber方法fields=" + fields.toString());
				// 检验参数是否为空
				message = this.checkNull(fields, obj);
				// 设置异常信息
				String lingjbh = obj.getLingjbh();
				if (!message.equals("true")) {
					// 将异常信息插入到异常报警表中
					this.yicbjService.saveYicInfo(jismk, lingjbh, message.substring(0, 3), obj.getLingjbh() + message.substring(3, message.length()));
					// 删除此记录
					this.kdmxqhzcService.doDeleteKdmxqhzc(obj);
					count = count + 1;
				} else {

					if (obj.getGongysfe().intValue() != 1) {
						// 设置错误信息
						message = "400" + obj.getLingjbh() + Const.FENE_ERROR;
						// 将异常信息插入到异常报警表中
						this.yicbjService.saveYicInfo(jismk, lingjbh, message.substring(0, 3), message.substring(3, message.length()));
						// 删除此记录
						this.kdmxqhzcService.doDeleteKdmxqhzc(obj);
						count = count + 1;
					}
				}
			}
		}

		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算", "检验关键字段结束");
		return count;
	}

	/**
	 * kd周期订单清空中间表
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-1-30
	 * @参数说明：String[] usercenter 用户中心数组;String riq，资源获取日期;String[] banc
	 *                版次数组;String zhizlx 制造路线
	 */
	public void clearData() {// 根据需求类型，清空对应的中间表
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算", "清理中间表开始");
		this.kdmaoxqservice.doAllDelete();
		this.kdmxqhzcService.doAllDelete();// 调用毛需求汇总关联参考系的删除方法
		this.kdmxqhzService.doAllDelete();// 调用毛需求汇总PP的删除方法
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算", "清理中间表结束");
	}

	/**
	 * kd周期订单计算
	 * 
	 * @author 袁修瑞
	 * @version v1.0
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ParseException
	 * @throws IntrospectionException
	 * @throws DataAccessException
	 * @throws Exception
	 * @参数说明：String[] usercenter 用户中心数组;String riq，资源获取日期;String[] banc
	 *                版次数组;String zhizlx 制造路线;String jihyz,计划员组
	 */

	public String doKDCalculate(String ziyxqrq, String jihyz, String[] banc, String dingdh, String zhizlx, String jihydm) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ParseException, DataAccessException,
			IntrospectionException {
		/**
		 * 得到计划员所属的所有用户中心
		 * **/
		String rilbc = "";
		String message = "";
		if (zhizlx.equals(Const.ZHIZAOLUXIAN_KD_PSA)) {
			rilbc = Const.KDBANCI;
		} else if (zhizlx.equals(Const.ZHIZAOLUXIAN_KD_AIXIN)) {
			rilbc = Const.AXBANCI;
		}
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		List<String> useList = loginUser.getUcList();
		// CommonFun.objListPrint(useList, "kd件订单计算doKDCalculate方法useList");
		String caozz = loginUser.getUsername();
		CommonFun.logger.debug("kd件订单计算doKDCalculate方法caozz=" + caozz);
		String usercenter[] = new String[useList.size()];
		for (int i = 0; i < useList.size(); i++) {
			usercenter[i] = useList.get(i);
		}
		/**
		 * 用于存储maoxqhzpc表的数据
		 */
		Map<String, String> ddMap = new HashMap<String, String>();
		ddMap.put("dingdh", dingdh);
		Dingd dingd = this.dingdService.queryDingdByDingdh(ddMap);
		String gongysdm = dingd.getGongysdm();
		if(StringUtils.isBlank(gongysdm))
		{
			return "所选订单的供应商代码为空,不能进行KD订单计算!";
		}
		// 清理中间表
		this.clearData();
		this.insertKdmxqZ(usercenter, banc, zhizlx);
		// 取出需要计算的毛需求并进行行列转换
		for (int i = 0; i < banc.length; i++) {
			// // 查询指定用户中心下该版次的毛需求插入中间表
			this.insertKdmxqC(ziyxqrq, null, banc[i], rilbc, dingd, jihyz, loginUser);
		}

		// 关键参数校验
		Integer errorCount = this.kdCheck(loginUser,gongysdm);
		// 得到毛需求汇总参考系表数据
		List<Kdmxqhzc> list = this.kdmxqhzcService.selectDingd(Const.PP);
	//	CommonFun.objListPrint(list, "kd件订单计算doKDCalculate方法list");
		
		if (list.isEmpty()) {
			message = "对应的毛需求汇总_参考系表表数据为空"; // 如果得到的数据为空则输出的信息。
		} else {
			//
			this.jisjid(list, dingd, rilbc, loginUser, banc, zhizlx, jihyz, jihydm, usercenter);
			Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
			this.feneservice.setCangkMap(map);
			this.feneservice.setFlagCangku("");
			this.feneservice.setFlagLingj("");
			this.feneservice.setZhouqixuhao(0);
			this.feneservice.setZongMap(map);
			if (errorCount > 0) {
				message = "计算完成，但校验数据有异常，请查看异常报警";
			} else {
				message = "计算完成！";
			}

		}// 如果计算完成得到的信息。
		return message;
	}

	/**
	 * <p>
	 * Title:KD件既定要货量计算
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
	 * @throws Exception
	 * @date 2011-12-1
	 **/
	public Map<String, Object> jiDingCalculate(BigDecimal jiding, Kdmxqhzc kdmxqhzc, boolean flag, Map<String, Object> map) {
		BigDecimal kuc = BigDecimal.ZERO;// 库存

		BigDecimal daijf = BigDecimal.ZERO;// 待交付
		BigDecimal daixh = BigDecimal.ZERO;// 待消耗
		BigDecimal yaohl = BigDecimal.ZERO;// 要货量
		BigDecimal quzhyhl = BigDecimal.ZERO;// 要货量取整后的值
		if (!flag) {
			// 获取替代件数
			BigDecimal tidjsl = this.anxOrderService.queryTidjsl(kdmxqhzc.getDinghck(), kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(),
					kdmxqhzc.getZiyhqrq());
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法tidjsl=" + tidjsl);
			// 将替代件数量加入到库存中
			kuc = kdmxqhzc.getKuc().add(tidjsl);
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法kuc=" + kuc);
			// 重新设置到库存中
			kdmxqhzc.setKuc(kuc);
			daijf = kdmxqhzc.getDingdlj().subtract(kdmxqhzc.getJiaoflj());
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法daijf=" + daijf);
			daixh = kdmxqhzc.getDaixh();
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法daixh=" + daixh);
			BigDecimal xuqiu = jiding.add(kdmxqhzc.getDinghaqkc());// 计算需求
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法Dinghaqkc=" + kdmxqhzc.getDinghaqkc());
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法xuqiu=" + xuqiu);
			BigDecimal ziyuan = kuc.add(daijf).subtract(daixh)
			// 计算资源
					.add(kdmxqhzc.getXittzz() == null ? BigDecimal.ZERO : kdmxqhzc.getXittzz());
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法Xittzz=" + kdmxqhzc.getXittzz());
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法ziyuan=" + ziyuan);
			yaohl = xuqiu.subtract(ziyuan);
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法yaohl=" + yaohl);
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				map.put("yingyu", yaohl.negate());
				yaohl = BigDecimal.ZERO;
				quzhyhl = yaohl.divide(kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()), 0, BigDecimal.ROUND_UP).multiply(
						kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
				CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法yaohl=" + yaohl);
			} else {
				quzhyhl = yaohl.divide(kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()), 0, BigDecimal.ROUND_UP).multiply(
						kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
				CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法Uabzucrl=" + kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
				CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法quzhyhl=" + quzhyhl);
				map.put("yingyu", quzhyhl.subtract(yaohl));
			}
			map.put("yaohl", quzhyhl);

		} else {
			BigDecimal xuqiu = jiding;// 计算需求
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法xuqiu=" + xuqiu);
			yaohl = xuqiu.subtract(new BigDecimal(map.get("yingyu").toString()));
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法yingyu=" + map.get("yingyu").toString());
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法yaohl=" + yaohl);
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				map.put("yingyu", yaohl.negate());
				yaohl = BigDecimal.ZERO;
				quzhyhl = yaohl.divide(kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()), 0, BigDecimal.ROUND_UP).multiply(
						kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
				CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法yaohl=" + yaohl);
			} else {
				quzhyhl = yaohl.divide(kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()), 0, BigDecimal.ROUND_UP).multiply(
						kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
				CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法Uabzucrl=" + kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
				CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法quzhyhl=" + quzhyhl);
				map.put("yingyu", quzhyhl.subtract(yaohl));
			}
			BigDecimal sum = new BigDecimal(map.get("yaohl").toString()).add(quzhyhl);
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法sum=" + sum);
			map.put("yaohl", sum);
		}
		return map;
	}

	/**
	 * P0的要货 Company: 软通动力
	 * 
	 * @author 袁修瑞
	 * @version v1.0
	 * @date 2012-12-13
	 **/
	public Map<String, Object> jiDingCalculateP0(BigDecimal jiding, Kdmxqhzc kdmxqhzc, Map<String, Object> map) {

		BigDecimal kuc = BigDecimal.ZERO;// 库存

		BigDecimal daijf = BigDecimal.ZERO;// 待交付
		BigDecimal daixh = BigDecimal.ZERO;// 待消耗
		BigDecimal yaohl = BigDecimal.ZERO;// 要货量
		BigDecimal quzhyhl = BigDecimal.ZERO;// 要货量取整后的值
		// 获取替代件数
////wuyichao修改  2014-05-16     替代件为全中心的零件数量
		BigDecimal tidjsl = this.anxOrderService.queryTidjsls( kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(),
				kdmxqhzc.getZiyhqrq());
		tidjsl = tidjsl == null ? BigDecimal.ZERO : tidjsl;
		//BigDecimal tidjsl = BigDecimal.ZERO;
////wuyichao修改  2014-05-16     替代件为全中心的零件数量
		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法tidjsl=" + tidjsl);

		// 将替代件数量加入到库存中
		kuc = kdmxqhzc.getKuc().add(tidjsl);
		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法kuc=" + kuc);
		// 重新设置到库存中
		kdmxqhzc.setKuc(kuc);
		daijf = kdmxqhzc.getDingdlj().subtract(kdmxqhzc.getJiaoflj()).subtract(kdmxqhzc.getDingdzzlj()==null?BigDecimal.ZERO:kdmxqhzc.getDingdzzlj());
		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法daijf=" + daijf);
		daixh = kdmxqhzc.getDaixh();
		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法daixh=" + daixh);
		//0006910: KD订单计算 计算调整值参与2次计算
		BigDecimal xuqiu = jiding;// 计算需求
		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法Dinghaqkc=" + kdmxqhzc.getDinghaqkc());
		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法xuqiu=" + xuqiu);
		BigDecimal ziyuan = kuc.add(daijf).subtract(daixh).add(kdmxqhzc.getXittzz() == null ? BigDecimal.ZERO : kdmxqhzc.getXittzz());
		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法Xittzz=" + kdmxqhzc.getXittzz());
		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法ziyuan=" + ziyuan);
		yaohl = xuqiu.subtract(ziyuan).add(kdmxqhzc.getJstzsz() == null ? BigDecimal.ZERO : kdmxqhzc.getJstzsz());
		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法yaohl=" + yaohl);
		if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
			map.put("yingyu", yaohl.negate());
			yaohl = BigDecimal.ZERO;
			quzhyhl = yaohl.divide(kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()), 0, BigDecimal.ROUND_UP).multiply(
					kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法yaohl=" + yaohl);
		} else {
			quzhyhl = yaohl.divide(kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()), 0, BigDecimal.ROUND_UP).multiply(
					kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法Uabzucrl=" + kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculateP0方法quzhyhl=" + quzhyhl);
			map.put("yingyu", quzhyhl.subtract(yaohl));
		}
		map.put("yaohl", yaohl);

		return map;
	}

	/**
	 * P0的要货 Company: 软通动力
	 * 
	 * @author 袁修瑞
	 * @version v1.0
	 * @date 2012-12-13
	 **/
	public Map<String, Object> jiDingCalculatePn(BigDecimal jiding, Kdmxqhzc kdmxqhzc, Map<String, Object> map) {

		BigDecimal kuc = BigDecimal.ZERO;// 库存

		BigDecimal daijf = BigDecimal.ZERO;// 待交付
		BigDecimal daixh = BigDecimal.ZERO;// 待消耗
		BigDecimal yaohl = BigDecimal.ZERO;// 要货量
		BigDecimal quzhyhl = BigDecimal.ZERO;// 要货量取整后的值
		BigDecimal xuqiu = jiding;// 计算需求

		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法xuqiu=" + xuqiu);
		
		if ( map.get("yingyu")!=null ){
			yaohl = xuqiu.multiply(kdmxqhzc.getGongysfe()).subtract(new BigDecimal(map.get("yingyu").toString()));
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法yingyu=" + map.get("yingyu").toString());
		}else{ 
			yaohl = xuqiu.multiply(kdmxqhzc.getGongysfe());
		}
				
		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法yaohl=" + yaohl);
		if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
			map.put("yingyu", yaohl.negate());
			yaohl = BigDecimal.ZERO;
			quzhyhl = yaohl.divide(kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()), 0, BigDecimal.ROUND_UP).multiply(
					kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法yaohl=" + yaohl);
		} else {
			quzhyhl = yaohl.divide(kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()), 0, BigDecimal.ROUND_UP).multiply(
					kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法Uabzucrl=" + kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
			CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法quzhyhl=" + quzhyhl);
			map.put("yingyu", quzhyhl.subtract(yaohl));
		}
		BigDecimal sum = new BigDecimal(map.get("yaohl").toString()).add(quzhyhl);
		CommonFun.logger.debug("KD件既定要货量计算jiDingCalculate方法sum=" + sum);
		map.put("yaohl", sum);

		return map;
	}

	/**
	 * <p>
	 * Title:KD件预告要货量计算
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
	 * @throws Exception
	 * @date 2011-12-1
	 **/
	public Map<String, Object> yuGaoCalculate(BigDecimal yugao, Kdmxqhzc kdmxqhzc, Map<String, Object> map) {
		BigDecimal yaohl = BigDecimal.ZERO;// 要货量
		BigDecimal quzhyhl = BigDecimal.ZERO;// 要货量取整后的值
		BigDecimal xuqiu = yugao;// 计算需求
		CommonFun.logger.debug("KD件预告要货量计算yuGaoCalculate方法xuqiu=" + xuqiu);
		yaohl = xuqiu.subtract(new BigDecimal(map.get("yingyu").toString()));
		CommonFun.logger.debug("KD件预告要货量计算yuGaoCalculate方法yingyu=" + map.get("yingyu").toString());
		CommonFun.logger.debug("KD件预告要货量计算yuGaoCalculate方法yaohl=" + yaohl);
		if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
			map.put("yingyu", yaohl.negate());
			yaohl = BigDecimal.ZERO;
			quzhyhl = yaohl.divide(kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()), 0, BigDecimal.ROUND_UP).multiply(
					kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
			CommonFun.logger.debug("KD件预告要货量计算yuGaoCalculate方法yaohl=" + yaohl);
		} else {
			quzhyhl = yaohl.divide(kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()), 0, BigDecimal.ROUND_UP).multiply(
					kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
			CommonFun.logger.debug("KD件预告要货量计算yuGaoCalculate方法Uabzucrl=" + kdmxqhzc.getUabzucrl().multiply(kdmxqhzc.getUabzucsl()));
			CommonFun.logger.debug("KD件预告要货量计算yuGaoCalculate方法quzhyhl=" + quzhyhl);
			map.put("yingyu", quzhyhl.subtract(yaohl));
			CommonFun.logger.debug("KD件预告要货量计算yuGaoCalculate方法yingyu=" + map.get("yingyu").toString());
		}
		BigDecimal sum = new BigDecimal(map.get("yaohl").toString()).add(quzhyhl);
		CommonFun.logger.debug("KD件预告要货量计算yuGaoCalculate方法sum=" + sum);
		map.put("yaohl", sum);
		return map;
	}

	/**
	 * kd订单周要货量计算
	 * 
	 * @author 李明
	 * @version v1.0
	 * @throws ParseException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2012-2-2
	 * @参数说明：订单零件实体
	 */
	@Transactional
	public void weekOrderCount(Dingdlj bean, String jihydm, String lingjmc, String gongsmc, String neibyhzx, String caozz, String rilbc,
			LoginUser loginuser, String gcbh) throws ParseException, SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单计算", "周要货量计算开始");

		// 异常报警计算模块
		String jismk = Const.JISMK_KD_CD;
		// 异常报警错误类型
		String cuowlx = Const.CUOWLX_200;
		// 异常报警信息
		String info = "";
		CommonFun.objPrint(bean, " kd订单周要货量计算weekOrderCount方法Dingdlj");
		if (null == bean) {
			// info = "订单零件信息为空";
			// this.yicbjService.saveYicInfo(jismk, null, cuowlx, info);
			// this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单计算",
			// "周要货量计算结束");
			return;
		}

		// 实例化订单明细
		Dingdmx mx = new Dingdmx();
		// 周要货量
		BigDecimal zhouyhl = BigDecimal.ZERO;
		// 取模
		int mode = 0;
		// 反射获取到类
		Class<? extends Dingdlj> cls = bean.getClass();
		// 定义动态的方法值
		String methodName = null;
		// 定义P的数量
		BigDecimal pisl = BigDecimal.ZERO;
		// 参数存放Map
		Map<String, String> parmMap = new HashMap<String, String>();
		// 拆分周期的周期信息（包含日、周序、周期序信息）
		CalendarVersion piCalendarVersion = null;
		// 周期类周的集合
		List<CalendarVersion> zhouList = new ArrayList<CalendarVersion>();

		List<CalendarVersion> beforeZhouList = new ArrayList<CalendarVersion>();
		// 获取到需要拆分既定的个数
		int count = this.dingdljService.getDingdnr(bean.getDingdnr());
		CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法count=" + count);
		// 得到零件编号
		String lingjbh = bean.getLingjbh();
		CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法lingjbh=" + lingjbh);
		// 获取到零件，从而得到第一起运时间
		Lingj lingj = this.lingjService.queryObject(bean.getLingjbh(), bean.getUsercenter());
		CommonFun.objPrint(lingj, "kd订单周要货量计算weekOrderCount方法lingj");
		// 判断是否取得第一起运时间
		if (null == lingj) {
			String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh() };
			this.yicbjService
					.insertError(cuowlx, Const.YICHANG_LX0_str1, jihydm, paramStr, bean.getUsercenter(), bean.getLingjbh(), loginuser, jismk);

			//
			// info = "在零件表中不存在该零件";
			// this.yicbjService.saveYicInfo(jismk, lingjbh, cuowlx, info);
			// this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单计算",
			// "周要货量计算结束");
			return;
		}
		for (int pi = 0; pi < Const.KDDINGDANNEIRONGCHANGDU; pi++) {
			// 周期内周数
			int zhoushu = 0;

			// //第一次启运时间所在周期的周期信息（包含日、周序、周期序信息）
			// CalendarCenter diyiciqiyunObject = null;
			// 周内工作天数集合
			List<CalendarVersion> list_riq = new ArrayList<CalendarVersion>();
			parmMap.put("usercenter", Const.KDRILI);
			parmMap.put("nianzq", bean.getP0fyzqxh());
			parmMap.put("banc", rilbc);
			String maxZhoux = this.calendarversionservice.getMaxNianzx(parmMap);
			if (pi == 0) {
				// 如果第一次启运时间为空
				CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0）时Diycqysj=" + lingj.getDiycqysj());
				if (null == lingj.getDiycqysj()) {
					parmMap.put("usercenter", Const.KDRILI);
					parmMap.put("nianzq", bean.getP0fyzqxh());
					parmMap.put("banc", rilbc);
					// 取得周期内周的集合
					zhouList = this.calendarversionservice.getWorkZhoushus(parmMap);
					CommonFun.objListPrint(zhouList, "kd订单周要货量计算weekOrderCount方法在（pi==0且null == lingj.getDiycqysj()）时zhouList");
					// 取得p0周期内的周数
					zhoushu = zhouList.size();
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null == lingj.getDiycqysj()）时zhoushu=" + zhoushu);
					// 取得p0周期内的第一个工作日
					String p0qisrq = this.calendarversionservice.getMinRi(parmMap);
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null == lingj.getDiycqysj()）时p0qisrq=" + p0qisrq);

					// 取得p0周期的周期序号及第一周周序
					parmMap.clear();
					parmMap.put("usercenter", Const.KDRILI);
					parmMap.put("riq", p0qisrq);
					parmMap.put("banc", rilbc);
					piCalendarVersion = this.calendarversionservice.queryCalendarVersionObject(parmMap);
				} else {
					java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
					// 转换第一次启运时间为date类型
					Date diycqyDate = df.parse(lingj.getDiycqysj());
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()）时diycqyDate=" + diycqyDate);
					java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyyMMdd");
					// 将第一次启运时间转为yyyyMMdd格式后截取年月
					String diycqyzhouqx = df2.format(diycqyDate).substring(0, 6);
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()）时diycqyzhouqx=" + diycqyzhouqx);
					if (diycqyzhouqx.compareTo(bean.getP0fyzqxh()) == 0) {
						parmMap.clear();
						parmMap.put("usercenter", Const.KDRILI);
						parmMap.put("nianzq", diycqyzhouqx);
						parmMap.put("riq", lingj.getDiycqysj().substring(0, 10));
						parmMap.put("banc", rilbc);
						beforeZhouList = this.calendarversionservice.getBefWorkZhoushus(parmMap);
						// 取得周期内周的集合
						zhouList = this.calendarversionservice.getWorkZhoushus(parmMap);
						CommonFun
								.objListPrint(zhouList,
										"kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()且diycqyzhouqx.compareTo(bean.getP0fyzqxh())==0）时zhouList");
						// 获取到周期内的周数
						zhoushu = zhouList.size();
						CommonFun.logger
								.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()且diycqyzhouqx.compareTo(bean.getP0fyzqxh())==0）时zhoushu="
										+ zhoushu);
						// 得到第一次启运时间所在周序
						piCalendarVersion = this.calendarversionservice.queryCalendarVersionObject(parmMap);
					} else if (diycqyzhouqx.compareTo(bean.getP0fyzqxh()) < 0) {
						parmMap.clear();
						parmMap.put("usercenter", Const.KDRILI);
						parmMap.put("nianzq", bean.getP0fyzqxh());
						parmMap.put("banc", rilbc);
						// 取得周期内周的集合
						zhouList = this.calendarversionservice.getWorkZhoushus(parmMap);
						CommonFun
								.objListPrint(zhouList,
										"kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()且diycqyzhouqx.compareTo(bean.getP0fyzqxh())<0）时zhouList");
						// 获取到周期内的周数
						zhoushu = zhouList.size();
						CommonFun.logger
								.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()且diycqyzhouqx.compareTo(bean.getP0fyzqxh())<0）时zhoushu="
										+ zhoushu);
						String p0qisrq = this.calendarversionservice.getMinRi(parmMap);
						parmMap.put("riq", p0qisrq);
						parmMap.put("banc", rilbc);
						// 获取到p0周期内第一周周序
						piCalendarVersion = this.calendarversionservice.queryCalendarVersionObject(parmMap);
					} else {// 如果第一次启运时间所在周期大于p0周期，则不进行拆分，并写入异常报警表

						String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh(), lingj.getDiycqysj(), "第一启运时间" };
						this.yicbjService.insertError(cuowlx, Const.YICHANG_LX2_str60, jihydm, paramStr, bean.getUsercenter(), bean.getLingjbh(),
								loginuser, jismk);

						// info = "第一启运时间" + lingj.getDiycqysj().substring(0,
						// 10) + "在p0周期序号之后";
						// CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法info="+info);
						// this.yicbjService.saveYicInfo(jismk, lingjbh, cuowlx,
						// info);
						this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单计算", "周要货量计算结束");
						return;
					}
				}
			} else {
				parmMap.clear();
				parmMap.put("usercenter", Const.KDRILI);
				parmMap.put("nianzq", CommonFun.addNianzq(piCalendarVersion.getNianzq(), Const.MAXZQ, pi));
				parmMap.put("banc", rilbc);
				// 取得周期内周的集合
				zhouList = this.calendarversionservice.getWorkZhoushus(parmMap);
				CommonFun.objListPrint(zhouList, "kd订单周要货量计算weekOrderCount方法在（pi!=0）时zhouList");
				// 获取到周期内的周数
				zhoushu = zhouList.size();
				CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi!=0）时zhoushu=" + zhoushu);
			}
			// 动态获取
			methodName = Const.GETP + pi + "sl";
			CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法methodName=" + methodName);
			// 获取到方法
			Method meth = cls.getMethod(methodName, new Class[] {});
			// 调用meth方法,将其转换成对应的数据类型
			pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
			CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);
			// 零件数量按包装取整
			BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()), 0, BigDecimal.ROUND_UP);
			CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法Uabzucrl=" + bean.getUabzucrl().multiply(bean.getUabzucsl()));
			CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法baozgs=" + baozgs);
			// 初步取得周要货包装个数
			zhouyhl = baozgs.divide(new BigDecimal(zhoushu), 0, BigDecimal.ROUND_DOWN);
			CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法zhouyhl=" + zhouyhl);
			if (baozgs.intValue() >= zhoushu) {
				// 获取到模值
				mode = baozgs.intValue() % zhoushu;
				CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法baozgs=" + baozgs);
				CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法zhoushu=" + zhoushu);
				CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法mode=" + mode);
			} else {
				// 获取到模值
				mode = baozgs.intValue();
				CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法mode=" + mode);
			}
			for (int i = 0; i < beforeZhouList.size(); i++) {
				parmMap.clear();
				parmMap.put("nianzx", beforeZhouList.get(i).getNianzx());
				parmMap.put("usercenter", Const.KDRILI);
				parmMap.put("banc", rilbc);
				// 取得周内所有日期
				list_riq = this.calendarversionservice.getStartandEndDay(parmMap);
				// 设置要货开始时间
				mx.setYaohqsrq(list_riq.get(0).getRiq());
				// 设置要货结束时间
				mx.setYaohjsrq(list_riq.get(list_riq.size() - 1).getRiq());
				// 设置交付时间
				mx.setJiaofrq(list_riq.get(0).getRiq());
				// 直接设置数量为0
				mx.setShul(BigDecimal.ZERO);
				mx.setJissl(BigDecimal.ZERO);
				// 设置订单明细bean
				mx.setFayrq(mx.getYaohqsrq());
				mx.setZhidgys(bean.getZhidgys());
				mx.setZuixqdl(bean.getZuixqdl());
				mx.setGonghlx(Const.PP);
				mx.setActive(Const.ACTIVE_1);
				mx.setDanw(bean.getDanw());
				mx.setUabzlx(bean.getUabzlx());
				mx.setUabzuclx(bean.getUabzuclx());
				mx.setUabzucrl(bean.getUabzucrl());
				mx.setUabzucsl(bean.getUabzucsl());
				mx.setYugsfqz(bean.getYugsfqz());
				mx.setLujdm(bean.getLujdm());
				mx.setJihyz(bean.getJihyz());
				mx.setYijfl(bean.getYijfl());
				mx.setDingdh(bean.getDingdh());
				mx.setUsercenter(bean.getUsercenter());
				mx.setLingjbh(bean.getLingjbh());
				mx.setGongysdm(bean.getGongysdm());
				mx.setGonghlx(bean.getGongyslx());
				mx.setCangkdm(bean.getCangkdm());
				mx.setFahd(bean.getFahd());
				mx.setDinghcj(bean.getDinghcj());
				mx.setCreate_time(CommonFun.getJavaTime());
				mx.setCreator(caozz);
				mx.setEdit_time(CommonFun.getJavaTime());
				mx.setEditor(caozz);
				mx.setZhuangt(Const.DINGD_STATUS_DSX);
				mx.setFayrq(mx.getYaohqsrq());
				mx.setZhidgys(bean.getZhidgys());
				mx.setZuixqdl(bean.getZuixqdl());
				mx.setGonghlx(Const.PP);
				mx.setActive(Const.ACTIVE_1);
				mx.setLingjmc(lingjmc);
				mx.setGongsmc(gongsmc);
				mx.setNeibyhzx(neibyhzx);
				mx.setFahd(bean.getFahd());
				mx.setXiehzt(bean.getXiehzt());
				mx.setGcbh(gcbh);
				if (count > 0) {
					mx.setLeix(Const.SHIFOUSHIJIDING);
				} else {
					mx.setLeix(Const.SHIFOUSHIYUGAO);
				}
				CommonFun.objPrint(mx, "kd订单周要货量计算weekOrderCount方法mx");

				this.dingdmxService.doInsert(mx);

			}
			beforeZhouList.clear();
			for (int i = 0; i < zhoushu; i++) {
				parmMap.clear();
				parmMap.put("nianzx", zhouList.get(i).getNianzx());
				parmMap.put("usercenter", Const.KDRILI);
				parmMap.put("banc", rilbc);
				// 取得周内所有日期
				list_riq = this.calendarversionservice.getStartandEndDay(parmMap);
				// 设置要货开始时间
				mx.setYaohqsrq(list_riq.get(0).getRiq());
				// 设置要货结束时间
				mx.setYaohjsrq(list_riq.get(list_riq.size() - 1).getRiq());
				// 设置交付时间
				mx.setJiaofrq(list_riq.get(0).getRiq());

				if (mode == 0) {
					// 如果模为0则直接设置数量
					mx.setShul(zhouyhl.multiply(bean.getUabzucrl().multiply(bean.getUabzucsl())));
					mx.setJissl(zhouyhl.multiply(bean.getUabzucrl().multiply(bean.getUabzucsl())));
				} else {
					// 如果模不为0 则包装个数加一，并且模减一
					mx.setShul((zhouyhl.add(BigDecimal.ONE)).multiply(bean.getUabzucrl().multiply(bean.getUabzucsl())));
					mx.setJissl((zhouyhl.add(BigDecimal.ONE)).multiply(bean.getUabzucrl().multiply(bean.getUabzucsl())));
					mode = mode - 1;
				}

				// 设置订单明细bean
				mx.setFayrq(mx.getYaohqsrq());
				mx.setZhidgys(bean.getZhidgys());
				mx.setZuixqdl(bean.getZuixqdl());
				mx.setGonghlx(Const.PP);
				mx.setActive(Const.ACTIVE_1);
				mx.setDanw(bean.getDanw());
				mx.setUabzlx(bean.getUabzlx());
				mx.setUabzuclx(bean.getUabzuclx());
				mx.setUabzucrl(bean.getUabzucrl());
				mx.setUabzucsl(bean.getUabzucsl());
				mx.setYugsfqz(bean.getYugsfqz());
				mx.setLujdm(bean.getLujdm());
				mx.setJihyz(bean.getJihyz());
				mx.setYijfl(bean.getYijfl());
				mx.setDingdh(bean.getDingdh());
				mx.setUsercenter(bean.getUsercenter());
				mx.setLingjbh(bean.getLingjbh());
				mx.setGongysdm(bean.getGongysdm());
				mx.setGonghlx(bean.getGongyslx());
				mx.setCangkdm(bean.getCangkdm());
				mx.setFahd(bean.getFahd());
				mx.setDinghcj(bean.getDinghcj());
				mx.setCreate_time(CommonFun.getJavaTime());
				mx.setCreator(caozz);
				mx.setEdit_time(CommonFun.getJavaTime());
				mx.setEditor(caozz);
				mx.setZhuangt(Const.DINGD_STATUS_DSX);
				mx.setFayrq(mx.getYaohqsrq());
				mx.setZhidgys(bean.getZhidgys());
				mx.setZuixqdl(bean.getZuixqdl());
				mx.setGonghlx(Const.PP);
				mx.setActive(Const.ACTIVE_1);
				mx.setLingjmc(lingjmc);
				mx.setGongsmc(gongsmc);
				mx.setNeibyhzx(neibyhzx);
				mx.setFahd(bean.getFahd());
				mx.setXiehzt(bean.getXiehzt());
				mx.setGcbh(gcbh);
				if (count > 0) {
					mx.setLeix(Const.SHIFOUSHIJIDING);
				} else {
					mx.setLeix(Const.SHIFOUSHIYUGAO);
				}
				CommonFun.objPrint(mx, "kd订单周要货量计算weekOrderCount方法mx");
				boolean flag = false;
				int index = 0;
				if (rilbc.equals(Const.KDBANCI)) {
					if (zhouList.get(i).getNianzx().equals(maxZhoux) || zhouList.get(i).getNianzx().substring(4).equals("01")) {
						for (int x = 0; x < list_riq.size(); x++) {
							CalendarVersion cev = list_riq.get(x);
							if (cev.getRiq().substring(5).equals("01-01")) {
								flag = true;
								index = x;
							}
						}
					}
					if (flag) {
						if (Integer.parseInt(list_riq.get(index).getXingq()) >= 5) {
							mx.setYaohjsrq(list_riq.get(index - 1).getRiq());
							this.dingdmxService.doInsert(mx);
							mx.setYaohqsrq(list_riq.get(index).getRiq());
							mx.setYaohjsrq(list_riq.get(list_riq.size() - 1).getRiq());
							mx.setFayrq(mx.getYaohqsrq());
							mx.setShul(BigDecimal.ZERO);
							mx.setJissl(BigDecimal.ZERO);
						} else if (Integer.parseInt(list_riq.get(index).getXingq()) > 1 && Integer.parseInt(list_riq.get(index).getXingq()) < 5) {
							int zMode = 0;
							BigDecimal zyh = BigDecimal.ZERO;
							BigDecimal tianyh = mx.getShul().divide(bean.getUabzucrl().multiply(bean.getUabzucsl()), 0, BigDecimal.ROUND_UP)
									.divide(new BigDecimal(4), 0, BigDecimal.ROUND_DOWN);
							if (mx.getShul().divide(bean.getUabzucrl().multiply(bean.getUabzucsl()), 0, BigDecimal.ROUND_UP).intValue() >= 4) {
								// 获取到模值
								zMode = mx.getShul().divide(bean.getUabzucrl().multiply(bean.getUabzucsl()), 0, BigDecimal.ROUND_UP).intValue() % 4;
								CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法mode=" + zMode);
							} else {
								// 获取到模值
								zMode = mx.getShul().divide(bean.getUabzucrl().multiply(bean.getUabzucsl()), 0, BigDecimal.ROUND_UP).intValue();
								CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法mode=" + mode);
							}
							if (zMode == 0) {
								zyh = tianyh.multiply(new BigDecimal(index));
							} else {
								if (zMode > index) {
									zyh = tianyh.multiply(new BigDecimal(index)).add(new BigDecimal(index));
								} else {
									zyh = tianyh.multiply(new BigDecimal(index)).add(new BigDecimal(zMode));
								}
							}

							mx.setYaohjsrq(list_riq.get(index - 1).getRiq());
							mx.setShul(zyh.multiply(bean.getUabzucrl().multiply(bean.getUabzucsl())));
							mx.setJissl(zyh.multiply(bean.getUabzucrl().multiply(bean.getUabzucsl())));
							this.dingdmxService.doInsert(mx);
							if (zMode == 0) {
								zyh = tianyh.multiply(new BigDecimal(4 - index));
							} else {
								if (zMode > index) {
									zyh = tianyh.multiply(new BigDecimal(4 - index)).add(new BigDecimal(zMode - index));
								} else {
									zyh = tianyh.multiply(new BigDecimal(4 - index));
								}
							}
							mx.setYaohqsrq(list_riq.get(index).getRiq());
							mx.setYaohjsrq(list_riq.get(list_riq.size() - 1).getRiq());
							mx.setFayrq(mx.getYaohqsrq());
							mx.setShul(zyh.multiply(bean.getUabzucrl().multiply(bean.getUabzucsl())));
							mx.setJissl(zyh.multiply(bean.getUabzucrl().multiply(bean.getUabzucsl())));
							this.dingdmxService.doInsert(mx);
						}
					} else {
						this.dingdmxService.doInsert(mx);
					}
				} else {
					this.dingdmxService.doInsert(mx);
				}
			}
			count = count - 1;
			CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法count=" + count);
		}

		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单计算", "周要货量计算结束");
	}

	public void dayOrderCount(Dingdmx mx, String caozz) throws ParseException {
			BigDecimal riyhl = BigDecimal.ZERO;
			int modeT = 0;
			String jiaofrq = mx.getJiaofrq();
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(jiaofrq!=null&&!"".equals(jiaofrq)){
				calendar.setTime(sdf.parse(jiaofrq));
			}
			Map<String, String> ddMap = new HashMap<String, String>();
			ddMap.put("dingdh", mx.getDingdh());
			Dingd dd = this.dingdService.queryDingdByDingdh(ddMap);
			String rilbc = "";
			if (dd.getDingdlx().equals(Const.DINGDLX_KD)) {
				rilbc = Const.KDBANCI;
			} else if (dd.getDingdlx().equals(Const.DINGDLX_AIX)) {
				rilbc = Const.AXBANCI;
			}
			if (mx.getLeix().endsWith("9")) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("kais", mx.getYaohqsrq().substring(0, 10));
				map.put("jies", mx.getYaohjsrq().substring(0, 10));
				map.put("riq", mx.getYaohjsrq().substring(0, 10));
				map.put("usercenter", Const.KDRILI);
				map.put("shifgzr", Const.GZR_Y);
				map.put("banc", rilbc);
				List<CalendarVersion> tianshu = this.calendarversionservice.getTianshu(map);
				if (tianshu.size() > 0) {
					riyhl = mx.getShul().divide(mx.getUabzucrl().multiply(mx.getUabzucsl()))
							.divide(new BigDecimal(tianshu.size()), 0, BigDecimal.ROUND_DOWN);
					// 得到日要货量的模
					modeT = mx.getShul().divide(mx.getUabzucrl().multiply(mx.getUabzucsl())).intValue() % tianshu.size();
					for (int z = 0; z < tianshu.size(); z++) {
						if (modeT == 0) {
							mx.setShul(riyhl.multiply(mx.getUabzucrl().multiply(mx.getUabzucsl())));
							mx.setJissl(riyhl.multiply(mx.getUabzucrl().multiply(mx.getUabzucsl())));
						} else {
							/////////////////////xh 11504 start
							//KD均分 如果周工作日为5天时 则使用新的分配规则否则使用现有分配规则从前往后分配
							if(tianshu.size()==5){
							    Kdbzfpgz bean=queryKdbzfpgz().get(modeT);
								try {
									int n=z+1;
									Method method = bean.getClass().getMethod("getXingq" + n);
									String str =  (String)method.invoke(bean);
									if("1".equals(str)){
										mx.setShul((riyhl.add(BigDecimal.ONE)).multiply(mx.getUabzucrl().multiply(mx.getUabzucsl())));
										mx.setJissl((riyhl.add(BigDecimal.ONE)).multiply(mx.getUabzucrl().multiply(mx.getUabzucsl())));	
									}else{
										mx.setShul(riyhl.multiply(mx.getUabzucrl().multiply(mx.getUabzucsl())));
										mx.setJissl(riyhl.multiply(mx.getUabzucrl().multiply(mx.getUabzucsl())));
									}
								} catch (SecurityException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
							   mx.setShul((riyhl.add(BigDecimal.ONE)).multiply(mx.getUabzucrl().multiply(mx.getUabzucsl())));
							   mx.setJissl((riyhl.add(BigDecimal.ONE)).multiply(mx.getUabzucrl().multiply(mx.getUabzucsl())));
							   modeT = modeT - 1;
							}
							///////////////xh  11504  end
						}
						mx.setYaohqsrq(tianshu.get(z).getRiq());
						mx.setYaohjsrq(tianshu.get(z).getRiq());
						calendar.add(Calendar.DAY_OF_YEAR, z);//平移发货日期;
						if(jiaofrq!=null&&!"".equals(jiaofrq)){
							mx.setJiaofrq(sdf.format(calendar.getTime()));
							calendar.setTime(sdf.parse(jiaofrq));
						}
						mx.setFayrq(tianshu.get(z).getRiq());
						mx.setZhuangt(Const.DINGD_STATUS_DSX);
						mx.setEditor(caozz);
						mx.setEdit_time(CommonFun.getJavaTime());

						this.kdrddmxService.doInsert(mx);
					}
				}
			}

		// Map<String, String> nianMap = new HashMap<String, String>();
		// Map<String, String> map = new HashMap<String, String>();
		// nianMap.put("usercenter", bean.getUsercenter());
		// nianMap.put("nianzx", calendarCenterObject.getNianzq().substring(0,
		// 4));
		// map.put("usercenter", bean.getUsercenter());
		// map.put("nianzq", calendarCenterObject.getNianzq());
		// Map zhouxMap =
		// MaoxqmxService.getZhoux(calendarCenterObject.getZhoux(), i,
		// this.calendarCenterService.getMaxNianzx(nianMap));
		// map.put("zhoux", zhouxMap.get("s" + (zhouxMap.size() -
		// 1)).toString());
		// map.put("shifgzr", "1");
		// // 获取到ben周内的天数
		// List<CalendarCenter> tianshu =
		// this.calendarCenterService.getTianshu(map);
		// map.clear();
		// if (tianshu.isEmpty()) {
		// info = "用户中心为"+bean.getUsercenter()+"根据周序为"+zhouxMap.get("s" +
		// (zhouxMap.size() - 1))+"查找不到对应的天数信息";
		// this.yicbjService.saveYicInfo(jismk, lingjbh, cuowlx, info);
		// } else {
		// // 当为第一个周期的时候，周其内包含1月1日，则日要货量=周要货量
		// if (i == 0) {
		// // 当P0周内包含1月1日的时候，日要货量=周要货量
		//
		// // 初步得到日要货量
		// riyhl = mx
		// .getShul()
		// .divide(bean.getUabzucrl().multiply(bean.getUabzucsl()))
		// .divide(new BigDecimal(tianshu.size()), 0,
		// BigDecimal.ROUND_DOWN);
		// // 得到日要货量的模
		// modeT = mx.getShul()
		// .divide(bean.getUabzucrl().multiply(bean.getUabzucsl()))
		// .intValue()
		// % tianshu.size();
		//
		// } else {
		// // 不为第一个周期，在sql中按照日期排序，取其第一个数据中的日期来比较
		// if (tianshu.get(0).getRiq().equals(Const.RIQI_01_01)) {
		// riyhl = mx.getShul().divide(
		// bean.getUabzucrl().multiply(bean.getUabzucsl()));
		// } else {
		// // 初步得到日要货量
		// riyhl = mx
		// .getShul()
		// .divide(bean.getUabzucrl().multiply(bean.getUabzucsl()))
		// .divide(new BigDecimal(tianshu.size()), 0,
		// BigDecimal.ROUND_DOWN);
		// // 得到日要货量的模
		// modeT = mx.getShul()
		// .divide(bean.getUabzucrl().multiply(bean.getUabzucsl()))
		// .intValue()
		// % tianshu.size();
		// }
		// }
		// for (int z = 0; z < tianshu.size(); z++) {
		// if (modeT == 0) {
		// mx.setShul(riyhl.multiply(bean.getUabzucrl().multiply(
		// bean.getUabzucsl())));
		// mx.setJissl(riyhl.multiply(bean.getUabzucrl().multiply(
		// bean.getUabzucsl())));
		// } else {
		// mx.setShul((riyhl.add(BigDecimal.ONE)).multiply(bean.getUabzucrl()
		// .multiply(bean.getUabzucsl())));
		// mx.setJissl((riyhl.add(BigDecimal.ONE)).multiply(bean.getUabzucrl()
		// .multiply(bean.getUabzucsl())));
		// modeT = modeT - 1;
		// }
		// mx.setYaohqsrq(tianshu.get(z).getRiq());
		// mx.setYaohjsrq(tianshu.get(z).getRiq());
		// mx.setJiaofrq(tianshu.get(z).getRiq());
		// this.dingdmxService.doInsert(mx);
		// }
		// }

	}

	// 跟据PO发运周期推算第I周期
	public String getDiZ(String p0fyzqxh, int i) {
		// 实例化日历
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, Integer.parseInt(p0fyzqxh.substring(0, 4)));
		calendar.set(Calendar.MONTH, Integer.parseInt(p0fyzqxh.substring(4, 6)) + i - 1);
		return "" + calendar.get(Calendar.YEAR) + String.format("%02d", (calendar.get(Calendar.MONTH) + 1));
	}

	// public Map<Integer, Boolean> weekOrderCountForUpdateMx(Dingdlj bean)
	// throws SecurityException, NoSuchMethodException,
	// IllegalArgumentException, IllegalAccessException,
	// InvocationTargetException {
	// Map<Integer, Boolean> maps = new HashMap<Integer, Boolean>();
	// List<Dingdmx> list = new ArrayList<Dingdmx>();
	// String lingjbh = bean.getLingjbh();
	// // 实例化订单明细
	// Dingdmx mx = new Dingdmx();
	// // 周要货量
	// BigDecimal zhouyhl = BigDecimal.ZERO;
	// //日要货量
	// BigDecimal riyhl = BigDecimal.ZERO;
	// // 取模
	// int mode = 0;
	// // 反射获取到类
	// Class<? extends Dingdlj> cls = bean.getClass();
	// // 定义动态的方法值
	// String sl = null;
	// // 定义P的数量
	// BigDecimal pi = BigDecimal.ZERO;
	// // 参数存放Map
	// Map<String, String> map = new HashMap<String, String>();
	// // 定义周期内周数
	// int pzhous = 0;
	// if (null != bean) {
	// // 查询到明细
	// map.put("dingdh", bean.getDingdh());
	// list = this.dingdmxService.queryListMx(map);
	// map.clear();
	// // 获取到需要拆分既定的个数
	// int count = this.dingdljService.getDingdnr(bean.getDingdnr());
	// // 既定数量至少有一个
	// if (count >= 1) {
	// // 获取到零件，从而得到第一起运时间
	// Lingj lingj = this.lingjService.queryObject(bean.getLingjbh(),
	// bean.getUsercenter());
	// // 判断是否取得第一起运时间
	// if (null == lingj || null == lingj.getDiycqysj()) {
	// this.yicbjService.saveYicInfo(Const.JISMK_KD_CD, lingjbh,
	// Const.CUOWLX_200, "该用户中心下不存在对应的零件或者第一起运时间错误");
	// } else {
	// map.put("usercenter", bean.getUsercenter());
	// map.put("riq", lingj.getDiycqysj().substring(0, 10));
	// // 用户中心对象
	// CalendarCenter calendarCenterObject =
	// this.calendarCenterService.queryCalendarCenterObject(map);
	// map.clear();
	// // 判断取得的用户中心对象不为空
	// if (calendarCenterObject == null) {
	// this.yicbjService.saveYicInfo(Const.JISMK_KD_CD, lingjbh,
	// Const.CUOWLX_200, "根据第一启运时间查找不到对应的日历信息");
	// } else {
	// // 判断当前获取的年周期和p0周期序号是否一致
	// if (calendarCenterObject.getNianzq().equals(bean.getP0fyzqxh())) {
	// map.put("usercenter", bean.getUsercenter());
	// map.put("nianzq", calendarCenterObject.getNianzq());
	// // 获取到周期内的周数
	// int zhoushu = this.calendarCenterService.getZhoushu(map).size();
	// map.clear();
	// // 获取到周序
	// map.put("nianzq", calendarCenterObject.getNianzq());
	// map.put("usercenter", bean.getUsercenter());
	// map.put("riq", lingj.getDiycqysj().substring(0, 10));
	// CalendarCenter calendarCenter =
	// this.calendarCenterService.getZhouxu(map);
	// map.clear();
	// map.put("nianzq", calendarCenterObject.getNianzq());
	// map.put("zhoux", calendarCenterObject.getZhoux());
	// // 查找起始日期
	// List<CalendarCenter> list_riq =
	// this.calendarCenterService.getStartandEndDay(map);
	// map.clear();
	// // 取最后一条记录，可得到结束时间
	// mx.setYaohjsrq(list_riq.get(list_riq.size() - 1).getRiq());
	//
	// mx.setJiaofrq(list_riq.get(0).getRiq());
	// // 循环周期
	// for (int i = 0; i < count; i++) {
	// // 当为P0周期时，需要计算p0内实际的周数
	// if (i == 0) {
	// // 得到P0周期内的周数
	// pzhous = zhoushu - Integer.parseInt(calendarCenter.getZhoux()) + 1;
	// map.put("riq", lingj.getDiycqysj().substring(0, 10));
	// // 当在第一周期中的时候，开始时间为第一次起运时间
	// mx.setYaohqsrq(map.get("riq").toString());
	// } else {
	// // 其他周期内的周数
	// pzhous = zhoushu;
	// // 若不是第一个周期
	// mx.setYaohjsrq(list_riq.get(list_riq.size() - 1).getRiq());
	// mx.setJiaofrq(list_riq.get(0).getRiq());
	// }
	// // 动态获取
	// sl = Const.GETP + i + "sl";
	// // 获取到方法
	// Method meth = cls.getMethod(sl, new Class[] {});
	// // 调用meth方法,将其转换成对应的数据类型
	// pi = (BigDecimal) meth.invoke(bean);
	// // 初步得到周要货量
	// zhouyhl = bean.getP0sl().divide(new BigDecimal(pzhous), 0,
	// BigDecimal.ROUND_UP);
	// // 获取到模值
	// mode = pi.intValue() % pzhous;
	// // 最终得到的周要货量集合
	// Map<Integer, BigDecimal> mapValue =
	// this.dingdljService.getModeValue(zhouyhl, mode, pzhous);
	// for (int j = 0; j < mapValue.size(); j++) {
	// // 最终得到的周要货量
	// zhouyhl = new BigDecimal(mapValue.get(j).toString());
	// if (zhouyhl.intValue() < 0) {
	// zhouyhl = BigDecimal.ZERO;
	// }
	// mx = list.get(j);
	// mx.setShul(zhouyhl);
	// // 修改订单明细表
	// boolean flag = this.dingdmxService.doUpdateMx(mx);
	// maps.put(j, flag);
	// }
	// map.put("usercenter", bean.getUsercenter());
	// map.put("nianzq", calendarCenterObject.getNianzq());
	// map.put("zhoux", calendarCenterObject.getZhoux());
	// // 获取到ben周内的天数
	// List<CalendarCenter> tianshu =
	// this.calendarCenterService.getTianshu(map);
	// map.clear();
	// // 当为第一个周期的时候，周其内包含1月1日，则日要货量=周要货量
	// if (i == 0) {
	// // 当P0周内包含1月1日的时候，日要货量=周要货量
	// if (lingj.getDiycqysj().substring(5, 10).equals(Const.RIQI_01_01)) {
	// riyhl = zhouyhl;
	// } else {
	// // 初步得到日要货量
	// riyhl = zhouyhl.divide(new BigDecimal(tianshu.size()),
	// BigDecimal.ROUND_HALF_UP);
	// // 得到日要货量的模
	// mode = zhouyhl.intValue() % tianshu.size();
	// }
	// } else {
	// // 不为第一个周期，在sql中按照日期排序，取其第一个数据中的日期来比较
	// if (tianshu.get(0).getRiq().equals(Const.RIQI_01_01)) {
	// riyhl = zhouyhl;
	// } else {
	// // 初步得到日要货量
	// riyhl = zhouyhl.divide(new BigDecimal(tianshu.size()));
	// // 得到日要货量的模
	// mode = zhouyhl.intValue() % tianshu.size();
	// }
	// }
	// }
	// } else {
	// // 插入错误日志；
	// this.yicbjService.saveYicInfo(Const.JISMK_KD_CD, lingjbh,
	// Const.CUOWLX_300, Const.FA_YUN_ZHOU);
	// }
	// }
	// }
	// } else {
	// this.yicbjService.saveYicInfo(Const.JISMK_KD_CD, lingjbh,
	// Const.CUOWLX_200, "该订单零件中至少包含一个既定要货");
	// }
	// }
	// return maps;
	// }

	/**
	 * @方法： 柔性比例检查
	 * @author 李明
	 * @version v1.0
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws Exception
	 * @date 2012-3-6
	 * @参数说明：订单零件
	 */
	public void rouxblCheck(Dingdlj bean, String jihyz, LoginUser loginuser) throws SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		// 放置查询参数的map
		Map<String, String> map = new HashMap<String, String>();
		// 存放上个周期的p0----p4数量map
		Map<Integer, BigDecimal> mapCount = new TreeMap<Integer, BigDecimal>();
		// 得到零件编号
		String lingjbh = bean.getLingjbh();
		CommonFun.logger.debug("柔性比例检查rouxblCheck方法lingjbh=" + lingjbh);
		// 设置计算模块
		String jismk = Const.JISMK_KD_CD;
		// 定义错误详细信息
		String info = "";
		if (null != bean) {
			Map<Integer, BigDecimal> map_p = DingdljService.getPCount(bean, 5);
			Map<String, String> maps = MaoxqmxService.getZhoux(bean.getP0fyzqxh(), 4, bean.getP0fyzqxh().substring(0, 4) + Const.MAXZQ);
			for (int i = 0; i < maps.size(); i++) {
				Zuiddhsl object = this.zuiddhslService
						.queryObject(maps.get(Const.S + i), bean.getGongysdm(), bean.getUsercenter(), bean.getLingjbh());
				CommonFun.objPrint(object, "柔性比例检查rouxblCheck方法object");
				if (null != object) {
					if (map_p.get(i).intValue() > object.getZuiddhsl().intValue()) {
						String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh() };
						this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str5, jihyz, paramStr, bean.getUsercenter(),
								bean.getLingjbh(), loginuser, jismk);
					}
				}
			}
			Dingd obj = getDingdObject(bean);
			if (null == obj) {
				String paramStr[] = new String[] { bean.getDingdh() };
				this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str6, jihyz, paramStr, bean.getUsercenter(), bean.getLingjbh(),
						loginuser, jismk);

				// info = "订单编号为" + bean.getDingdh() + "的订单找不到上一周期订单";
				// // 插入错误日志
				// this.yicbjService.saveYicInfo(jismk, lingjbh,
				// Const.YICHANG_LX3, info);
			} else {
				// 根据订单号，得到订单零件
				map.put("dingdh", obj.getDingdh());
				List<Dingdlj> dingljList = this.dingdljService.queryAllDingdlj(map);
				CommonFun.objListPrint(dingljList, "柔性比例检查rouxblCheck方法dingljList");
				map.clear();
				if (null != dingljList) {
					for (Dingdlj dingljObject : dingljList) {

						// 得到p0----p4数量map集合(上一个周期的数量)
						mapCount = DingdljService.getPCount(dingljObject, 5);
						CommonFun.mapPrint(mapCount, "柔性比例检查rouxblCheck方法mapCount");
						// 查询到零件实体，获取到关键零件标志
						Lingj lingj = this.lingjService.queryObject(bean.getLingjbh(), bean.getUsercenter());
						CommonFun.objPrint(lingj, "柔性比例检查rouxblCheck方法lingj");
						// 得到p0----p4数量map集合

						CommonFun.mapPrint(map_p, "柔性比例检查rouxblCheck方法map_p");
						// 得到p0----p4连续的周期

						CommonFun.mapPrint(maps, "柔性比例检查rouxblCheck方法maps");
						// 查询到零件不为空
						if (null != lingj) {
							// 循环
							for (int i = 0; i < maps.size(); i++) {
								// 检查最大订货量(p0发运周期序号)

								// 判断最大要货对象不为空

								// 当p0---p4的数量小于最大要货量，则继续判断是否超过柔性比例

								Double bl = BigDecimal.ZERO.doubleValue();
								if (i < 4) {
									if (mapCount.get(i + 1).intValue() == 0) {
										bl = 1.0;
									} else {
										bl = map_p.get(i).subtract(mapCount.get(i + 1)).divide(mapCount.get(i + 1), 3, BigDecimal.ROUND_DOWN)
												.doubleValue();
										CommonFun.logger.debug("柔性比例检查rouxblCheck方法bl=" + bl);
									}
								}
								// 在柔性比例表中查询出具体的比例来
								BigDecimal rxbl = this.rouxblService.getRxbl(bean.getUsercenter(), bean.getGuanjljjb(), "P" + i, bean.getGongysdm());
								CommonFun.logger.debug("柔性比例检查rouxblCheck方法rxbl=" + rxbl);
								// 将得到的比例做边界处理
								Map<String, Double> rxblMap = this.rouxblService.checkRxbl(rxbl, 1.0, bean, jihyz, loginuser);
								CommonFun.mapPrint(rxblMap, "柔性比例检查rouxblCheck方法rxblMap");
								// 不能大于最大，也不能小于最小
								if (rxblMap.size() > 0) {
									if (bl > rxblMap.get("max") || -bl < rxblMap.get("min")) {
										String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh() };
										this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str1, jihyz, paramStr,
												bean.getUsercenter(), bean.getLingjbh(), loginuser, jismk);

									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 根据已知订单号推算另外一个订单号
	 * */
	private Dingd getDingdObject(Dingdlj bean) {
		String fahzq = null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("dingdh", bean.getDingdh());
		// 根据订单号查询实体(获取到发货周期)
		Dingd dingd = this.dingdService.queryDingdByDingdh(map);
		map.clear();
		fahzq = dingd.getFahzq();
		CommonFun.logger.debug("根据已知订单号推算另外一个订单号getDingdObject方法fahzq=" + fahzq);
		for (int i = 0; i < 3; i++) {
			// 得到发货周期
			fahzq = DingdService.getXuhao(fahzq);
			CommonFun.logger.debug("根据已知订单号推算另外一个订单号getDingdObject方法fahzq=" + fahzq);
			map.put("fahzq", fahzq);
			map.put("dingdzt", Const.DINGD_STATUS_YSX);
			map.put("dingdlx", dingd.getDingdlx());
			// 根据发货周期查询订单。最终得到订单号
			List<Dingd> ddList = this.dingdService.queryDdListByDingdh(map);
			map.clear();
			if (ddList.size() > 0) {
				return ddList.get(0);
			}
		}
		return null;
	}

	/**
	 * 根据已知订单号推算最近的订单
	 * */
	private List<Dingd> getLastDingdObject(Dingd bean) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("dingdh", bean.getDingdh());
		return this.dingdService.queryLastDingdByDingdh(map);
	}
//////////////wuyichao////////////////
	/**
	 * 根据已知订单号推算最近的订单
	 * */
	private Dingd getLastDingd(Dingd bean) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("dingdh", bean.getDingdh());
		map.put("dingdlx", bean.getDingdlx());
		map.put("jiszq", bean.getJiszq());
		return this.dingdService.queryLastDingdByDingdhNew(map);
	}
//////////////wuyichao////////////////
	/**
	 * @方法：订单状态修改
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-3-6
	 * @参数说明：订单实体
	 */
	public void updateDingd(String dingdh, Dingdlj dingdlj, String[] bancs, String jihyz, String[] usercenter, String caozz,String dingdzt) {
		Map<String, String> map = new HashMap<String, String>();
		String banch = "";
		String user = "";
		String[] banc = CommonFun.getArray(bancs);

		for (int i = 0; i < banc.length; i++) {
			banch = banch + banc[i] + ";";
		}
		for (int i = 0; i < usercenter.length; i++) {
			user = user + usercenter[i] + ";";
		}
		CommonFun.logger.debug("订单状态修改updateDingd方法banch=" + banch.toString());
		CommonFun.logger.debug("订单状态修改updateDingd方法user=" + user.toString());
		map.put("dingdh", dingdh);
		Dingd bean = new Dingd();
		bean = this.dingdService.queryDingdByDingdh(map);
		bean.setChullx(Const.PP);
		bean.setGongyslx(Const.WAIBUGONGYINGSHANG);
		bean.setDingdzt(dingdzt);
		bean.setDingdjssj(CommonFun.getJavaTime());
		bean.setZiyhqrq(dingdlj.getZiyhqrq());
		bean.setFahzq(dingdlj.getP0fyzqxh());
		bean.setMaoxqbc(banch);
		bean.setJislx(jihyz);
		bean.setUsercenter(user);
		bean.setEditor(caozz);
		bean.setEdit_time(CommonFun.getJavaTime());
		CommonFun.objPrint(bean, "订单状态修改updateDingd方法bean");
		this.dingdService.doUpdate(bean);
	}

	/**
	 * 不匹配装箱规则
	 * 
	 * @param bean
	 */
	public void clearDdMxByDdh(Dingdlj bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteDingdmxByDingdh", bean);
	}

	@SuppressWarnings("unchecked")
	public void checkXiaohbl(String jihyz) {
		List<Xiaohblzjb> xiaohblList = (ArrayList<Xiaohblzjb>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.checkXiaohblKDPP");
		for (Xiaohblzjb xiaohblzjb : xiaohblList) {
			Yicbj yc = new Yicbj();
			String cuowxxxx = xiaohblzjb.getUsercenter() + "下的编号为" + xiaohblzjb.getLingjbh() + "的零件在编号为" + xiaohblzjb.getShengcxbh()
					+ "的产线上消耗比例和不为100%";
			yc.setUsercenter(xiaohblzjb.getUsercenter());
			yc.setLingjbh(xiaohblzjb.getLingjbh());
			yc.setJismk(Const.JISMK_KD_CD);
			yc.setCuowlx(Const.YICHANG_LX6);
			yc.setCuowxxxx(cuowxxxx);
			yc.setJihyz(jihyz);
			Map<String, String> ppMap = new HashMap<String, String>();
			ppMap.put("usercenter", xiaohblzjb.getUsercenter());
			ppMap.put("lingjbh", xiaohblzjb.getLingjbh());
			ppMap.put("chanx", xiaohblzjb.getShengcxbh());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.deleteOneOfKDMaoxq", ppMap);
		}

	}

	/**
	 * kd件订单生效
	 * 
	 * @param bean
	 */
	public String updateDaStatus(List<Dingd> ls, String newEditor, String editTime, String flag) {
		String result = "修改成功!";
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String caozz = loginUser.getUsername();
		for (int i = 0, len = ls.size(); i < len; i++) {
			Dingd dd = ls.get(i);
			dd.setNewEditor(newEditor);
			dd.setNewEditTime(editTime);
			String dingdzt = dd.getDingdzt();
			String zhuangt = "";
			int j = 0;
			// 0待生效1生效2拒绝
			if (flag.equalsIgnoreCase(Const.DINGD_STATUS_YDY)) {
				if (dingdzt.equalsIgnoreCase(Const.DINGD_STATUS_YDY) || dingdzt.equalsIgnoreCase(Const.DINGD_STATUS_ZZZ)) {
					// 待生效
					dd.setDingdzt(Const.DINGD_STATUS_DSX);
					j = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateUnEe", dd);
					zhuangt = Const.DINGD_STATUS_DSX;
					Map<String, Object> map = new HashMap<String, Object>();
					// 修改人
					map.put("editor", dd.getEditor());
					// 修改时间
					map.put("edit_time", dd.getEdit_time());
					map.put("zhuangt", zhuangt);
					// 当前更新人
					map.put("newEditor", newEditor);
					// 当前更新时间
					map.put("newEditTime", editTime);
					// 订单号
					map.put("dingdh", dd.getDingdh());
					// 用户中心
					map.put("usercenter", dd.getUsercenter());
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.editDdmx", map);
				} else {
					throw new ServiceException("只能待生效制作中的订单！");
				}
			} else if (flag.equalsIgnoreCase(Const.DINGD_STATUS_ZZZ)) {
				if (dingdzt.equalsIgnoreCase(Const.DINGD_STATUS_DSX)) {
					// 生效
					Map<String, String> map = new HashMap<String, String>();
					// 修改人
					map.put("dingdh", dd.getDingdh());
		///////////////wuyichao   0010339 生效订单的时候查询订单明细下的用户中心，与订单的用户中心进行匹配/////////////
					List<String> mxUsercenter =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryUsercenterByDingdmx", map);
					if(null != mxUsercenter && mxUsercenter.size() > 0)
					{
						String mxU = "";
						for (String u : mxUsercenter) 
						{
							mxU += u + ",";
						}
						dd.setnUsercenter(mxU);
					}
		///////////////wuyichao   0010339 生效订单的时候查询订单明细下的用户中心，与订单的用户中心进行匹配/////////////
					Dingd dingd = this.dingdService.queryDingdByDingdh(map);
					List<Dingdmx> list = new ArrayList<Dingdmx>();
					if (null != dingd) {
						if (dingd.getDingdlx().equals(Const.DINGDLX_KD)) {
							list = this.dingdmxService.queryListMx(map);
							for (Dingdmx mx : list) {
									try {
										this.dayOrderCount(mx, caozz);
									} catch (ParseException e) {
										e.printStackTrace();
									}
							}
						} else {
							map.put("leix", Const.SHIFOUSHIJIDING);
							list = this.dingdmxService.queryListMx(map);
							if(null != list && list.size() > 0)
							{
								baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kdorder.insertKdrddmx", list);
							}
							//for (Dingdmx mx : list) {
							//	this.kdrddmxService.doInsert(mx);
							//}
						}

						if (dingd.getMaoxqbc() != null && !dingd.getMaoxqbc().equals("")) {
							String[] banc = dingd.getMaoxqbc().split(";");
							for (int x = 0; x < banc.length; x++) {

								map.clear();
								map.put("xuqbc", banc[x]);
								baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateMaoxq", map);
							}
						}
		
					}
				
					dd.setDingdzt(Const.DINGD_STATUS_YSX);
					dd.setDingdsxsj(editTime);//xh 0706 11496订单生效时间精确到年月日时分秒
					j = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateUnEe", dd);
					this.dingdmxService.doUpdateMxZt(dd.getDingdh(), Const.DINGD_STATUS_YFS);
					this.kdrddmxService.doUpdateMxZt(dd.getDingdh(), Const.DINGD_STATUS_YSX);
					zhuangt = Const.DINGD_STATUS_YSX;
				} else {
					throw new ServiceException("只能生效状态为待生效的订单！");
				}
			} else if (flag.equalsIgnoreCase(Const.DINGD_STATUS_DSX)) {
				if (dingdzt.equalsIgnoreCase(Const.DINGD_STATUS_DSX)) {
					// 拒绝
					dd.setDingdzt(Const.DINGD_STATUS_JUJ);
					j = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateUnEe", dd);
					zhuangt = Const.DINGD_STATUS_JUJ;
					Map<String, Object> map = new HashMap<String, Object>();
					// 修改人
					map.put("editor", dd.getEditor());
					// 修改时间
					map.put("edit_time", dd.getEdit_time());
					map.put("zhuangt", zhuangt);
					// 当前更新人
					map.put("newEditor", newEditor);
					// 当前更新时间
					map.put("newEditTime", editTime);
					// 订单号
					map.put("dingdh", dd.getDingdh());
					// 用户中心
					map.put("usercenter", dd.getUsercenter());
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.editDdmx", map);
				} else {
					throw new ServiceException("只能拒绝状态为待生效的订单！");
				}
			}

			// 如果更新行数为，则抛出异常
			if (j == 0) {
				throw new ServiceException(MessageConst.UPDATE_COUNT_0);
			}
		}
		return result;
	}

	public Integer kdCheck(LoginUser loginuser , String gongysdm) {
		Integer count = 0;
		List<Kdmxqhzc> erroList = new ArrayList();
		List<Yicbj> yicbjList = new ArrayList();
//////wuyichao  2014-05-27 删除非指定供应商的汇总数据////////////
		this.kdmxqhzcService.clearData(gongysdm);
//////wuyichao  2014-05-27////////////		
		
		erroList = this.kdmxqhzcService.checkFene();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh() };
				CommonFun.insertError(Const.YICHANG_LX4, Const.YICHANG_LX4_str1, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
						kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
			}

		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkZhizlx();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "制造路线" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str15, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
						kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkGongysdm();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "供应商代码" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str5, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
						kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkDinghcj();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "订货车间" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str50, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
						kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkBeihzq();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "备货周期" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str4, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
						kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkFayzq();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "发运周期" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str12, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
						kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkZiyhqrq();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "资源获取日期" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
						kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkLujdm();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				if (kdmxqhzc.getDinghck() == null || kdmxqhzc.getGongysdm() == null) {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "路径代码" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				} else {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), kdmxqhzc.getGongysdm(),
							kdmxqhzc.getDinghck(), "路径代码" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str13, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				}
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkCangkdm();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "仓库编号" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
						kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkUabzlx();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				if (kdmxqhzc.getGongysdm() != null) {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), kdmxqhzc.getGongysdm(), "UA包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				} else {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "UA包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				}
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkUabzuclx();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				if (kdmxqhzc.getGongysdm() != null) {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), kdmxqhzc.getGongysdm(), "UA中UC包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				} else {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "UA中UC包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				}

			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkUabzucsl();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				if (kdmxqhzc.getGongysdm() != null) {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), kdmxqhzc.getGongysdm(), "UA中UC个数" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				} else {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "UA中UC个数" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				}
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkUabzucrl();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				if (kdmxqhzc.getGongysdm() != null) {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), kdmxqhzc.getGongysdm(), "UA中UC容量" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str59, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				} else {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "UA中UC容量" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				}
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkWaibghms();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				if (kdmxqhzc.getGongysdm() == null || kdmxqhzc.getDinghck() == null) {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "外部供货模式" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				} else {
					String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), kdmxqhzc.getGongysdm(),
							kdmxqhzc.getDinghck(), "外部供货模式" };
					CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str13, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
							kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
				}
			}
		}

		erroList.clear();
		erroList = this.kdmxqhzcService.checkJihyz();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "计划员代码" };
				CommonFun.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str49, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
						kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
			}
		}
		
		erroList.clear();
		erroList = this.kdmxqhzcService.checkWullj();
		if (erroList.size() > 0) {
			String str = "用户中心%1下零件%2的没有物流路径或该物流路径的备货周期与发运周期为空!";
			count = erroList.size();
			for (Kdmxqhzc kdmxqhzc : erroList) {
				String[] paramStr = new String[] { kdmxqhzc.getUsercenter(), kdmxqhzc.getLingjbh(), "计划员代码" };
				CommonFun.insertError(Const.YICHANG_LX2, str, yicbjList, Const.POAcode, paramStr, kdmxqhzc.getUsercenter(),
						kdmxqhzc.getLingjbh(), loginuser, Const.JISMK_KD_CD);
			}
		}
		
		if (yicbjList.size() > 0) {
			this.yicbjService.insertAll(yicbjList);
		}
		this.kdmxqhzcService.clearErro();
		return count;
	}

	public void insertKdmxqZ(String[] usercenter, String banc[], String zhizlx) {
		for (int i = 0; i < banc.length; i++) {
			for (int j = 0; j < usercenter.length; j++) {
				// 查询指定用户中心下该版次的毛需求插入中间表
				this.kdmaoxqservice.selectNewXuq(usercenter[j], banc[i], zhizlx);
			}
		}
	}

	public void insertKdmxqC(String riq, String usercenter, String banc, String rilbc, Dingd dingd, String jihyz, LoginUser loginuser)
			throws IntrospectionException, ParseException, DataAccessException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, SecurityException, NoSuchMethodException {

		Map<String, Object> rilmap = new HashMap<String, Object>();
		// map.put("usercenter", "UW");

		rilmap.put("xuqbc", banc);
		//取出备货与发运组合的集合
		List<Kdmxqhz> kdmxqhzNew = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.countByBeihAndfay", rilmap);
		String jismk = "32";
		String cuowlx = Const.CUOWLX_200;

		rilmap.put("ziyhqrq", riq);

		rilmap.put("banc", rilbc);
		Map<String, BigDecimal> kdmxqhzcNewMap = new HashMap<String, BigDecimal>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xuqbc", banc);
		map.put("ziyhqrq", riq);
		map.put("banc", rilbc);
		Class cls = null;
		String method = "";
		if (!kdmxqhzNew.isEmpty()) {
			for (Kdmxqhz kdmxqhz : kdmxqhzNew) {
				List<Kdmxqhzc> mxqhzcNewlist = new ArrayList<Kdmxqhzc>();
				// 计算得到周数（S0......Sn）以及周期周序
				List<Map> zhouxsList = this.queryNianZqAndZhoux(kdmxqhz, dingd, rilmap);
				rilmap.put("beihzq", kdmxqhz.getBeihzq()==null?0:kdmxqhz.getBeihzq());
				rilmap.put("fayzq", kdmxqhz.getFayzq()==null?0:kdmxqhz.getFayzq());
				//取出同一组备货与发运零件及相关信息的集合
				List<Kdmxqhzc> list = this.kdmxqhzcService.queryNewListKdmxqhzcForConvert(rilmap);
				
				//零件存在
				if((list!=null && list.size()>0)){
					//无备货周期发运周期
					if((zhouxsList!=null&&zhouxsList.size()>0)){
						int j = 0;
						for (Map nianzqmap : zhouxsList) {
							Class cl = Kdmxqhzc.class;
							map.put("nianzq", nianzqmap.get("NIANZQ"));
							map.put("zhoux", nianzqmap.get("ZHOUX"));
							String starttime = this.getAfterLineStartWeekDate(map, kdmxqhz.getFayzq());
							map.put("starttime", starttime);
							map.put("endtime", this.getAfterLineEndWeekDate(map, kdmxqhz.getFayzq()));
							kdmxqhzcNewMap = this.getXuqslMap(map);
							for (Kdmxqhzc kdmxqhzcNew : list) {
								StringBuffer key = new StringBuffer();
								key.append(kdmxqhzcNew.getLingjbh());
								key.append(kdmxqhzcNew.getGongysdm());
								key.append(kdmxqhzcNew.getUsercenter());
								kdmxqhzcNew.setS0sszxh(zhouxsList.get(0).get("NIANZQ").toString());

								// 汇总求和
								BigDecimal xuqsl = BigDecimal.ZERO;
								if (!kdmxqhzcNewMap.isEmpty() && kdmxqhzcNewMap.get(key.toString().trim()) != null) {
									xuqsl = kdmxqhzcNewMap.get(key.toString().trim());
								}

								BigDecimal daixh = BigDecimal.ZERO;
								if (j == 0) {
									PropertyDescriptor pd = new PropertyDescriptor("s" + 0, cl);
									pd.getWriteMethod().invoke(kdmxqhzcNew, xuqsl);
									daixh = this.newCountDaixh(kdmxqhzcNew,rilbc,banc, jismk, cuowlx, jihyz, riq, starttime, loginuser);
									daixh = daixh == null ? BigDecimal.ZERO : daixh;
									kdmxqhzcNew.setDaixh(daixh);
									kdmxqhzcNew.setDingdnr(dingd.getDingdnr());

									kdmxqhzcNew.setKuc(kdmxqhzcNew.getKuc() == null ? BigDecimal.ZERO : kdmxqhzcNew.getKuc());
									kdmxqhzcNew.setDinghaqkc(kdmxqhzcNew.getDinghaqkc() == null ? BigDecimal.ZERO : kdmxqhzcNew.getDinghaqkc());
									kdmxqhzcNew.setDingdlj(kdmxqhzcNew.getDingdlj() == null ? BigDecimal.ZERO : kdmxqhzcNew.getDingdlj());
									kdmxqhzcNew.setDingdzzlj(kdmxqhzcNew.getDingdzzlj() == null ? BigDecimal.ZERO : kdmxqhzcNew.getDingdzzlj());
									kdmxqhzcNew.setJiaoflj(kdmxqhzcNew.getJiaoflj() == null ? BigDecimal.ZERO : kdmxqhzcNew.getJiaoflj());
									kdmxqhzcNew.setXittzz(kdmxqhzcNew.getXittzz() == null ? BigDecimal.ZERO : kdmxqhzcNew.getXittzz());
									kdmxqhzcNew.setJstzsz(kdmxqhzcNew.getJstzsz() == null ? BigDecimal.ZERO : kdmxqhzcNew.getJstzsz());
									mxqhzcNewlist.add(kdmxqhzcNew);
								
								} else {
									int i = 0;
									for (Kdmxqhzc kdzc : mxqhzcNewlist) {
										cls = kdzc.getClass();//
										StringBuffer key1 = new StringBuffer();
										key1.append(kdzc.getLingjbh());
										key1.append(kdzc.getGongysdm());
										key1.append(kdzc.getUsercenter());
										if (!kdmxqhzcNewMap.isEmpty()) {
											xuqsl = kdmxqhzcNewMap.get(key1.toString().trim());
										}
										method = "setS" + j;//
										Method methdj = cls.getMethod(method, BigDecimal.class);
										methdj.invoke(kdzc, xuqsl);// 执行得到的方法，setS
										mxqhzcNewlist.set(i, kdzc);
										i++;

									}
								}

							}
							j++;
							kdmxqhzcNewMap.clear();
						} 
					} else {
						//插入基本信息（未算S0....Sn）
						this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kdorder.insertKdmxqhzc", list);
					}

					this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kdorder.insertKdmxqhzc", mxqhzcNewlist);
				}
				
			}
		}

	}

	private Map<String, BigDecimal> getXuqslMap(Map<String, Object> map) {
		List<Kdmxqhz> list = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.sumNewXuqsl", map);
		Map<String, BigDecimal> kdMap = new HashMap<String, BigDecimal>();
		for (Kdmxqhz kd : list) {
			StringBuffer key = new StringBuffer();
			key.append(kd.getLingjbh());
			key.append(kd.getGongysbh());
			key.append(kd.getUsercenter());
			kdMap.put(key.toString().trim(), kd.getXuqsl());
		}
		return kdMap;
	}

	private BigDecimal newCountDaixh(Kdmxqhzc bean,String rilbc,String xuqbanc, String jismk, String cuowlx, String jihyz, String ziyhqrq, String Snstarttime,
			LoginUser loginuser) throws DataAccessException, ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ziyhqrq", ziyhqrq);
		map.put("banc", rilbc);
		CalendarVersion c = (CalendarVersion) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryNianzqAndzhoux", map);
		map.put("nianzq", c.getNianzq());
		map.put("zhoux", c.getZhoux());
		// 计算出资源获取日期所在的开始日期
		String starttime = this.getAfterLineStartWeekDate(map, BigDecimal.ZERO);
		
		map.put("banc", xuqbanc);
		map.put("lingjbh", bean.getLingjbh());
		map.put("gongysbh", bean.getGongysdm());
		map.put("usercenter", bean.getUsercenter());
		map.put("starttime", starttime);
		map.put("endtime", Snstarttime);
		return (BigDecimal) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kdorder.sumDaixh", map);
	}

	/*
	 * // 计算周数 private int countZhouxs(Kdmxqhz bean, Dingd dingd) { String jiszq
	 * = dingd.getJiszq(); Calendar calendar = Calendar.getInstance(); int year
	 * = Integer.parseInt(jiszq.substring(0, 4)); int month =
	 * Integer.parseInt(jiszq.substring(4, 6)) - 1; // int day =
	 * Integer.parseInt(jiszq.substring(8, 10)); calendar.set(year, month, 1);
	 * int zhouxs = 0; if (null != bean.getBeihzq()) { // 备货周期取整 int bhindex =
	 * bean.getBeihzq().divide(new BigDecimal(Const.MONTH_30), 0,
	 * BigDecimal.ROUND_UP).intValue();
	 * CommonFun.logger.debug("kd行列转换：计算周序Beihzq=" + bean.getBeihzq());
	 * calendar.add(Calendar.MONTH, bhindex);// 加上备货周期 Date beihDate =
	 * calendar.getTime(); // 得到备货月 int moveMonth = dingd.getDingdnr().length();
	 * calendar.add(calendar.MONTH, moveMonth); Date moveDate =
	 * calendar.getTime(); Calendar cal_start = Calendar.getInstance();
	 * cal_start.setTime(beihDate); Calendar cal_end = Calendar.getInstance();
	 * cal_end.setTime(moveDate); zhouxs = this.getDaysBetween(cal_start,
	 * cal_end) / 7; } return zhouxs; }
	 */

	// 计算周序
	private List<Map> queryNianZqAndZhoux(Kdmxqhz bean, Dingd dingd, Map<String, Object> paramMap) {
		String jiszq = dingd.getJiszq();
		Calendar calendar = Calendar.getInstance();
		int year = Integer.parseInt(jiszq.substring(0, 4));
		int month = Integer.parseInt(jiszq.substring(4, 6)) - 1;
		calendar.set(year, month, 1);
		if (null != bean.getBeihzq()) {
			// 备货周期取整
			int bhindex = bean.getBeihzq().divide(new BigDecimal(Const.MONTH_30), 0, BigDecimal.ROUND_UP).intValue();
			CommonFun.logger.debug("kd行列转换：计算周序Beihzq=" + bean.getBeihzq());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			calendar.add(Calendar.MONTH, bhindex + 1);// 加上备货周期
			String startNianZq = sdf.format(calendar.getTime());
			int moveMonth = dingd.getDingdnr().length();
			calendar.add(calendar.MONTH, moveMonth);
			String endNianZq = sdf.format(calendar.getTime());
			paramMap.put("startNianZq", startNianZq);
			paramMap.put("endNianZq", endNianZq);
			return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.countWeeks", paramMap);
		}
		return null;
	}

	// 根据日历班次，用户中心，年周期查询上线周的开始时间
	private String getAfterLineStartWeekDate(Map<String, Object> paramMap, BigDecimal fayzq) throws ParseException {
		    String minRiq = CommonFun.strNull(this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryFirstWeekMinRiq", paramMap));
		    if (minRiq != null && !"".equals(minRiq)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(minRiq));
				int fyindex = fayzq.divide(new BigDecimal(Const.WEEK_7), 0, BigDecimal.ROUND_UP).intValue();
				calendar.add(Calendar.DAY_OF_YEAR, fyindex * Integer.parseInt(Const.WEEK_7));
				return sdf.format(calendar.getTime());
		    }
		    return null;
	}

	// 根据日历班次，用户中心，年周期查询上线周的结束时间
	private String getAfterLineEndWeekDate(Map<String, Object> paramMap, BigDecimal fayzq) throws ParseException {
		String maxRiq = CommonFun.strNull(this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryFirstWeekMaxRiq", paramMap));
		if (maxRiq != null && !"".equals(maxRiq)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(maxRiq));
			int fyindex = fayzq.divide(new BigDecimal(Const.WEEK_7), 0, BigDecimal.ROUND_UP).intValue();
			calendar.add(Calendar.DAY_OF_YEAR, fyindex * Integer.parseInt(Const.WEEK_7));
			return sdf.format(calendar.getTime());
		}
		return null;
	}

	/*
	 * private String byWeekMove(String nowDate) throws ParseException {
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); Calendar
	 * calendar = Calendar.getInstance(); calendar.setTime(sdf.parse(nowDate));
	 * calendar.add(calendar.DAY_OF_YEAR, 7); return
	 * sdf.format(calendar.getTime()); }
	 * 
	 * // 计算2个日期之间相隔的天数 private int getDaysBetween(Calendar cal_start, Calendar
	 * cal_end) { if (cal_start.after(cal_end)) { java.util.Calendar swap =
	 * cal_start; cal_start = cal_end; cal_end = swap; } int days =
	 * cal_end.get(java.util.Calendar.DAY_OF_YEAR) -
	 * cal_start.get(java.util.Calendar.DAY_OF_YEAR); int y2 =
	 * cal_end.get(java.util.Calendar.YEAR); if
	 * (cal_start.get(java.util.Calendar.YEAR) != y2) { cal_start =
	 * (java.util.Calendar) cal_start.clone(); do { days +=
	 * cal_start.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
	 * cal_start.add(java.util.Calendar.YEAR, 1); } while
	 * (cal_start.get(java.util.Calendar.YEAR) != y2); } return days; }
	 */

	// 计算出当前时间的周开始时间
	private Date beginWeekDate(Date nowDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate);
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
		cal.add(Calendar.DATE, -day_of_week);
		return cal.getTime();
	}

	// 计算当前日期的周结束时间
	private Date endWeekDate(Date nowDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowDate);
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
		cal.add(Calendar.DATE, -day_of_week);
		cal.add(Calendar.DATE, 6);
		return cal.getTime();
	}

	private void jisjid(List<Kdmxqhzc> list, Dingd dingd, String rilbc, LoginUser loginUser, String[] banc, String zhizlx, String jihyz,
			String jihydm, String[] usercenter) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, ParseException {
		String caozz = loginUser.getUsername();
		// 得到既定的个数
		int jidgs = this.dingdljService.getDingdnr(dingd.getDingdnr());
		
		
		Map<String,String> map = new HashMap<String,String>();
//		map.put("dingdh", dingd.getDingdh());
		map.put("dingdzt", Const.DINGD_STATUS_JSZ);
		map.put("dingdlxin", "'1','2','4'");
		List<Dingd> dingList = this.dingdService.queryLastDingdByDingdh(map);
		Dingdlj dlj = new Dingdlj();
		//删除订单状态为计算中的(-1)相关信息
		if(dingList != null && dingList.size()>0){
			for (Dingd dd : dingList) {
				dd.setDingdzt(Const.DINGD_STATUS_YDY);
				dd.setZiyhqrq(null);
				dd.setMaoxqbc(null);
				this.dingdService.doUpdate(dd);
				Dingdlj deldlj = new Dingdlj();
				deldlj.setDingdh(dd.getDingdh());
				this.dingdljService.doDelete(deldlj);
				Dingdmx dingdmx = new Dingdmx();
				dingdmx.setDingdh(dd.getDingdh());
				this.dingdmxService.doDelete(dingdmx);
			}
		}
		String nianzq = "";
////////////////////////////////wuyichao修改////////////////////		
		//计算前将订单状态修改为计算中
		Dingd jszDindd = new Dingd();
		jszDindd.setDingdh(dingd.getDingdh());
		jszDindd.setDingdzt(Const.DINGD_STATUS_JSZ);
		this.dingdService.doUpdate(jszDindd);
		
		
		//缓存零件信息
		List<Dingdlj> dingdLjList = new ArrayList<Dingdlj>();
		//缓存周数
		Map<String , Integer > zhousCacheMap = new HashMap<String, Integer>(); 
		//缓存工作日  （按周算）
		Map<String , Integer > gzrByZhouCacheMap = new HashMap<String, Integer>();
		//缓存工作日  (按周期算)
		Map<String , Integer > gzrByZhouqCacheMap = new HashMap<String, Integer>();
		//缓存周开始日期
		Map<String , String > startDateByZhouCacheMap = new HashMap<String, String>();
		//缓存周结束日期
		Map<String , String > endDateByZhouCacheMap = new HashMap<String, String>();
		//缓存零件信息
		map.put("biaos", Const.ACTIVE_1);
		List<Lingj> lingjCacheList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("CKX.queryLingj", map);
		Map<String , Lingj> lingjCacheMap = new HashMap<String, Lingj>();
		if(null != lingjCacheList)
		{
			for (Lingj lingj : lingjCacheList) 
			{
				lingjCacheMap.put(lingj.getUsercenter() + lingj.getLingjbh(), lingj);
			}
		}
////////////////////////////////wuyichao修改////////////////////		
		
		
		for (Kdmxqhzc kdmxqhzc : list) {
			// 记录当前S几
			int sn = 0;
			int nextSn = 0;
			BigDecimal ddljzl = BigDecimal.ZERO;
			boolean flag = true;// P0只算一次
			// 初始化订单零件
			Dingdlj dingdlj = new Dingdlj();
			Map<String, Object> zhouqi = new HashMap<String, Object>();// 用于汇总周期要货

			CommonFun.logger.debug("kd件订单计算doKDCalculate方法jidgs=" + jidgs);
			BigDecimal P0_jiding = BigDecimal.ZERO; //xss_双既定时汇总P0需求数量_v4_013 
			String diycqyzhouqx = "";
			
			Lingj lingj = this.lingjService.queryObject(kdmxqhzc.getLingjbh(), kdmxqhzc.getUsercenter());			
			java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			
			if( lingj.getDiycqysj()!= null  ){
				Date diycqyDate = df.parse(lingj.getDiycqysj());
				CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()）时diycqyDate=" + diycqyDate);
				java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyyMMdd");
				// 将第一次启运时间转为yyyyMMdd格式后截取年月
				diycqyzhouqx = df2.format(diycqyDate).substring(0, 6);
				CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()）时diycqyzhouqx=" + diycqyzhouqx);
	
			}			
			
			//P1周期
			String nianzq_P1 = CommonFun.addNianzq(kdmxqhzc.getS0sszxh(), Const.MAXZQ, 1);

			//双既定时如果大于P0、P1则全部按照预告算法计算
			if (diycqyzhouqx.compareTo(kdmxqhzc.getS0sszxh()) > 0 && jidgs == 2 && diycqyzhouqx.compareTo(nianzq_P1) > 0) { 
				jidgs = 0;
			}
			
			
			/**
			 * 计算既定部分
			 * **/
			if (jidgs > 0 && flag) {
				flag = false;
				nianzq = kdmxqhzc.getS0sszxh();
				// 得到周期内周数
//////////////////wuyichao///////////////
			//	int zhoushu = this.calendarversionservice.getZhouShu(Const.KDRILI, nianzq, rilbc, null);
				int zhoushu = getZhouShu(Const.KDRILI, nianzq, rilbc, zhousCacheMap);
//////////////////wuyichao///////////////
				CommonFun.logger.debug("kd件订单计算doKDCalculate方法nianzq=" + nianzq);
				CommonFun.logger.debug("kd件订单计算doKDCalculate方法zhoushu=" + zhoushu);
				zhouqi.put("nianzq", nianzq);
				zhouqi.put("yaohl", BigDecimal.ZERO);
				BigDecimal jiding = BigDecimal.ZERO;
				String method = "";
				Class cls = null;
				for (int z = 0; z < zhoushu; z++) {
					method = Const.GETS + sn;// 拼接getPi方法
					sn++;
					cls = kdmxqhzc.getClass();// 得到Maoxqhzpc类
					Method meth = cls.getMethod(method, new Class[] {});// 得到Maoxqhzpc类的method拼接的方法
//					BigDecimal s = BigDecimal.ZERO;
//					if (meth.invoke(kdmxqhzc) != null) {
//						s = new BigDecimal(meth.invoke(kdmxqhzc).toString());
//					}
					jiding = jiding.add((BigDecimal) (meth.invoke(kdmxqhzc)==null?BigDecimal.ZERO:meth.invoke(kdmxqhzc)));// 执行得到的方法，取得既定周期的数量

				}
				
				P0_jiding = jiding ;
				
				// 如果该零件的第一次起运时间大于S0周期时，并且是双既定时，需求变为零
				if( lingj.getDiycqysj()!= null  ){
					if (diycqyzhouqx.compareTo(kdmxqhzc.getS0sszxh()) > 0 && jidgs == 2 ) {
							jiding = BigDecimal.ZERO;
					}
				}
				
				
				zhouqi = this.jiDingCalculateP0(jiding, kdmxqhzc, zhouqi);// 如果是第一个周期调用既定数量计算方法
				//CommonFun.mapPrint(zhouqi, "kd件订单计算doKDCalculate方法在（x == 0）时zhouqi");
				nextSn = sn;

				method = "setP" + 0 + "sl";// 拼接getPi方法
				CommonFun.logger.debug("kd件订单计算doKDCalculate方法method=" + method);

				dingdlj.setDingdh(dingd.getDingdh());// 订单号
				dingdlj.setLingjbh(kdmxqhzc.getLingjbh());// 零件号
				dingdlj.setGongysdm(kdmxqhzc.getGongysdm());// 供应商代码
				dingdlj.setGongyslx(kdmxqhzc.getGongyslx());// 供应商类型
				dingdlj.setUsercenter(kdmxqhzc.getUsercenter());// 用户中心
				dingdlj.setDinghcj(kdmxqhzc.getDinghcj());// 订货车间
				dingdlj.setCangkdm(kdmxqhzc.getDinghck());// 仓库代码
				dingdlj.setP0fyzqxh(zhouqi.get("nianzq").toString());// p0周期序号
				dingdlj.setUabzlx(kdmxqhzc.getUabzlx());// ua包装类型
				dingdlj.setUabzuclx(kdmxqhzc.getUabzuclx());// ua中uc包装类型
				dingdlj.setUabzucrl(kdmxqhzc.getUabzucrl());// ua中uc包装容量
				dingdlj.setUabzucsl(kdmxqhzc.getUabzucsl());// ua中uc包装数量
				dingdlj.setDanw(kdmxqhzc.getDanw());// 单位
				dingdlj.setGongysfe(kdmxqhzc.getGongysfe());// 供应商份额
				dingdlj.setBeihzq(kdmxqhzc.getBeihzq());// 备货周期
				dingdlj.setFayzq(kdmxqhzc.getFayzq());// 发运周期
				dingdlj.setKuc(kdmxqhzc.getKuc());// 库存
				dingdlj.setAnqkc(kdmxqhzc.getAnqkc());// 安全库存
				dingdlj.setTiaozjsz(kdmxqhzc.getDinghaqkc());// 调整计算值
				dingdlj.setXittzz(kdmxqhzc.getXittzz());// 系统调整值
				dingdlj.setDingdlj(kdmxqhzc.getDingdlj());// 订单累计
				dingdlj.setJiaoflj(kdmxqhzc.getJiaoflj());// 交付累计
				dingdlj.setDaixh(kdmxqhzc.getDaixh());// 待消耗
				dingdlj.setZiyhqrq(kdmxqhzc.getZiyhqrq().substring(0, 10));// 资源获取日期
				dingdlj.setJihyz(kdmxqhzc.getJihyz());// 计划员组
				dingdlj.setLujdm(kdmxqhzc.getLujdm());// 路径代码
				dingdlj.setDingdnr(kdmxqhzc.getDingdnr());// 订单内容
				dingdlj.setZhidgys(kdmxqhzc.getZhidgys());// 指定供应商
				dingdlj.setDingdzzsj(CommonFun.getJavaTime().substring(0, 19));// 订单制作时间String
				dingdlj.setGonghms(Const.PP);
				dingdlj.setActive(Const.ACTIVE_1);
				dingdlj.setCreate_time(CommonFun.getJavaTime());
				dingdlj.setCreator(caozz);
				dingdlj.setEdit_time(CommonFun.getJavaTime());
				dingdlj.setEditor(caozz);
				dingdlj.setFahd(kdmxqhzc.getFahd());
				dingdlj.setXiehzt(kdmxqhzc.getXiehzt());
				dingdlj.setGuanjljjb(kdmxqhzc.getGuanjljjb());
				
				
				if( lingj.getDiycqysj()!= null  ){
					// 如果该零件的第一次起运时间大于S0周期时，并且是双既定时，需求变为零
					if (diycqyzhouqx.compareTo(kdmxqhzc.getS0sszxh()) > 0 && jidgs == 2 ) {  
							zhouqi.put("yaohl", BigDecimal.ZERO);
					}
				}
				

				// 份额计算
				Map<String, BigDecimal> velueMap = this.feneservice.gongysFeneJs(dingdlj, new BigDecimal(zhouqi.get("yaohl").toString()), zhizlx, 0);
				cls = dingdlj.getClass();// 得到Dingdlj类
				Method methdj = cls.getMethod(method, BigDecimal.class);// 得到Dingdlj类的method拼接的方法
				methdj.invoke(dingdlj, new BigDecimal(velueMap.get("yaohl").toString()));// 执行得到的方法，setPx
				ddljzl = ddljzl.add(velueMap.get("yaohl")); 
			}
			
			
			/**
			 * 计算预告部分
			 * **/
			for (int y = 0; y < dingd.getDingdnr().length() - 1; y++) {
				if (nianzq == null  || nianzq.equals("") ) {
					nianzq = kdmxqhzc.getS0sszxh();
				}
				
				if (jidgs > 0 ) { 
					nianzq = CommonFun.addNianzq(nianzq, Const.MAXZQ, 1);
				}else{ 
					zhouqi.put("nianzq", nianzq);
					dingdlj.setDingdh(dingd.getDingdh());// 订单号
					dingdlj.setLingjbh(kdmxqhzc.getLingjbh());// 零件号
					dingdlj.setGongysdm(kdmxqhzc.getGongysdm());// 供应商代码
					dingdlj.setGongyslx(kdmxqhzc.getGongyslx());// 供应商类型
					dingdlj.setUsercenter(kdmxqhzc.getUsercenter());// 用户中心
					dingdlj.setDinghcj(kdmxqhzc.getDinghcj());// 订货车间
					dingdlj.setCangkdm(kdmxqhzc.getDinghck());// 仓库代码
					dingdlj.setP0fyzqxh(zhouqi.get("nianzq").toString());// p0周期序号
					dingdlj.setUabzlx(kdmxqhzc.getUabzlx());// ua包装类型
					dingdlj.setUabzuclx(kdmxqhzc.getUabzuclx());// ua中uc包装类型
					dingdlj.setUabzucrl(kdmxqhzc.getUabzucrl());// ua中uc包装容量
					dingdlj.setUabzucsl(kdmxqhzc.getUabzucsl());// ua中uc包装数量
					dingdlj.setDanw(kdmxqhzc.getDanw());// 单位
					dingdlj.setGongysfe(kdmxqhzc.getGongysfe());// 供应商份额
					dingdlj.setBeihzq(kdmxqhzc.getBeihzq());// 备货周期
					dingdlj.setFayzq(kdmxqhzc.getFayzq());// 发运周期
					dingdlj.setKuc(kdmxqhzc.getKuc());// 库存
					dingdlj.setAnqkc(kdmxqhzc.getAnqkc());// 安全库存
					dingdlj.setTiaozjsz(kdmxqhzc.getDinghaqkc());// 调整计算值
					dingdlj.setXittzz(kdmxqhzc.getXittzz());// 系统调整值
					dingdlj.setDingdlj(kdmxqhzc.getDingdlj());// 订单累计
					dingdlj.setJiaoflj(kdmxqhzc.getJiaoflj());// 交付累计
					dingdlj.setDaixh(kdmxqhzc.getDaixh());// 待消耗
					dingdlj.setZiyhqrq(kdmxqhzc.getZiyhqrq().substring(0, 10));// 资源获取日期
					dingdlj.setJihyz(kdmxqhzc.getJihyz());// 计划员组
					dingdlj.setLujdm(kdmxqhzc.getLujdm());// 路径代码
					dingdlj.setDingdnr(kdmxqhzc.getDingdnr());// 订单内容
					dingdlj.setZhidgys(kdmxqhzc.getZhidgys());// 指定供应商
					dingdlj.setDingdzzsj(CommonFun.getJavaTime().substring(0, 19));// 订单制作时间String
					dingdlj.setGonghms(Const.PP);
					dingdlj.setActive(Const.ACTIVE_1);
					dingdlj.setCreate_time(CommonFun.getJavaTime());
					dingdlj.setCreator(caozz);
					dingdlj.setEdit_time(CommonFun.getJavaTime());
					dingdlj.setEditor(caozz);
					dingdlj.setFahd(kdmxqhzc.getFahd());
					dingdlj.setXiehzt(kdmxqhzc.getXiehzt());
					dingdlj.setGuanjljjb(kdmxqhzc.getGuanjljjb());
					 
				} 
								
				
				Map<String, String> maxMap = new HashMap<String, String>();
				maxMap.put("usercenter", Const.KDRILI);
				maxMap.put("nianzq", nianzq.substring(0, 4));
				maxMap.put("banc", rilbc);
//////////////////wuyichao/////////////////////
				//int zhoushu = this.calendarversionservice.getZhouShu(Const.KDRILI, nianzq, rilbc, null);
				int zhoushu = getZhouShu(Const.KDRILI, nianzq, rilbc, zhousCacheMap);
//////////////////wuyichao/////////////////////
				zhouqi.put("nianzq", nianzq);
				zhouqi.put("yaohl", BigDecimal.ZERO);
				BigDecimal yugao = BigDecimal.ZERO;
				Class cls = null;
				String method = "";
				for (int z = 0; z < zhoushu; z++) {
					method = Const.GETS + nextSn;// 拼接getPi方法
					CommonFun.logger.debug("kd件订单计算doKDCalculate方法预告method=" + method);
					cls = kdmxqhzc.getClass();// 得到Maoxqhzpc类
					Method meth = cls.getMethod(method, new Class[] {});// 得到Maoxqhzpc类的method拼接的方法
//					BigDecimal s = BigDecimal.ZERO;
//					if (meth.invoke(kdmxqhzc) != null) {
//						s = new BigDecimal(meth.invoke(kdmxqhzc).toString());
//					}
					yugao = yugao.add((BigDecimal) (meth.invoke(kdmxqhzc)==null?BigDecimal.ZERO:meth.invoke(kdmxqhzc)));// 执行得到的方法，取得预告周期的数量
					CommonFun.logger.debug("kd件订单计算doKDCalculate方法预告yugao=" + yugao);
					nextSn++;
				}
				
				//xss_V4_013 
				if( lingj.getDiycqysj()!= null  ){					
					// 如果该零件的第一次起运时间大于S0周期时，并且是双既定，将P0的需求汇总到P1
					if (diycqyzhouqx.compareTo(kdmxqhzc.getS0sszxh()) > 0 && jidgs == 2  && y ==0) {
						yugao = yugao.add(P0_jiding);
					}
				}
				
				
				zhouqi = this.jiDingCalculatePn(yugao, kdmxqhzc, zhouqi);// 调用预告数量计算方法
				//CommonFun.mapPrint(zhouqi, "kd件订单计算doKDCalculate方法预告zhouqi");
				
				int PX = 0;
				
				if (jidgs > 0 ) { 
					PX = y + 1;
				}else{
					PX = y ;
				}
				
				
				CommonFun.logger.debug("kd件订单计算doKDCalculate方法预告PX=" + PX);
				method = "setP" + PX + "sl";// 拼接getPi方法
				CommonFun.logger.debug("kd件订单计算doKDCalculate方法预告method=" + method);
				cls = dingdlj.getClass();// 得到Dingdlj类
				Method methdj = cls.getMethod(method, BigDecimal.class);// 得到Dingdlj类的method拼接的方法
				methdj.invoke(dingdlj, new BigDecimal(zhouqi.get("yaohl").toString()));// 执行得到的方法，setPx

				if (jidgs - 1 - y != 0) {
					// 份额计算
					Map<String, BigDecimal> velueMap = this.feneservice.gongysFeneJs(dingdlj, new BigDecimal(zhouqi.get("yaohl").toString()), zhizlx,
							0);
					ddljzl = ddljzl.add(velueMap.get("yaohl"));
				} else {
					dingdlj.setDingdljddlj(ddljzl);
				}
			}
//////////////////////////////////wuyichao///////////////////////
			dingdLjList.add(dingdlj);
			this.newWeekOrderCount(dingdlj, jihydm, kdmxqhzc.getLingjmc(), kdmxqhzc.getGongsmc(), kdmxqhzc.getNeibyhzx(), caozz, rilbc, loginUser,
					kdmxqhzc.getGcbh(),lingjCacheMap,zhousCacheMap,gzrByZhouCacheMap, gzrByZhouqCacheMap,startDateByZhouCacheMap,endDateByZhouCacheMap);
			//this.newRouxblCheck(dingdlj, jihyz, loginUser, rilbc, dingd);
			//this.dingdljService.doInsert(dingdlj);
			//this.newWeekOrderCount(dingdlj, jihydm, kdmxqhzc.getLingjmc(), kdmxqhzc.getGongsmc(), kdmxqhzc.getNeibyhzx(), caozz, rilbc, loginUser,
			//		kdmxqhzc.getGcbh());
			//this.updateDingd(dingd.getDingdh(), dingdlj, banc, jihyz, usercenter, caozz,Const.DINGD_STATUS_JSZ);
//////////////////////////////////wuyichao///////////////////////
			dlj=dingdlj;
		}
///////////////////////wuyichao////////////////////////
		if(0 < dingdLjList.size())
		{
			//柔性比例检查
			this.newRouxblCheck(dingdLjList, jihyz, loginUser, rilbc, dingd);
			//批量插入订单零件
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ilorder.insertDingdlj", dingdLjList);
		}
///////////////////////wuyichao////////////////////////
		this.updateDingd(dingd.getDingdh(), dlj, banc, jihyz, usercenter, caozz,Const.KDDINGDANZHIZUOZHONG);
	}

	/**
	 * @方法： 柔性比例检查
	 * @author 袁修瑞
	 * @version v1.0
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws Exception
	 * @date 2012-12-13
	 * @参数说明：订单零件
	 */
	public void newRouxblCheck(Dingdlj bean, String jihyz, LoginUser loginuser, String rilbc, Dingd dingd) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		// 放置查询参数的map
		Map<String, String> map = new HashMap<String, String>();
		// 存放上个周期的p0----p4数量map
		Map<String, BigDecimal> mapCount = new TreeMap<String, BigDecimal>();
		// 得到零件编号
		String lingjbh = bean.getLingjbh();
		CommonFun.logger.debug("柔性比例检查rouxblCheck方法lingjbh=" + lingjbh);
		// 设置计算模块
		String jismk = Const.JISMK_KD_CD;
        int index=0;
        String nianzq=bean.getP0fyzqxh();
		if (null != bean) {
			Map<String, BigDecimal> map_p = DingdljService.getPCounts(bean, dingd.getDingdnr().length());
			for (Map.Entry<String, BigDecimal> entry : map_p.entrySet()) {
				nianzq = CommonFun.addNianzq(bean.getP0fyzqxh(), Const.MAXZQ, index);
				Zuiddhsl object = this.zuiddhslService.queryObject(nianzq, bean.getGongysdm(), bean.getUsercenter(), bean.getLingjbh());
				index++;
				//CommonFun.objPrint(object, "柔性比例检查rouxblCheck方法object");
				if (null != object) {
					if (map_p.get(entry.getKey()).compareTo(object.getZuiddhsl()) == 1) {
						String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh(),nianzq};
						this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str10, jihyz, paramStr, bean.getUsercenter(),
								bean.getLingjbh(), loginuser, jismk);
					}
				}
			}

			List<Dingd> list = getLastDingdObject(dingd);
			if (null == list) {
				String paramStr[] = new String[] { bean.getDingdh() };
				this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str6, jihyz, paramStr, bean.getUsercenter(), bean.getLingjbh(),
						loginuser, jismk);
			} else {
				Dingd obj = list.get(0);
				// 根据订单号，得到订单零件
				map.put("dingdh", obj.getDingdh());
				map.put("lingjbh", bean.getLingjbh());
				map.put("usercenter", bean.getUsercenter());
				map.put("gongysdm", bean.getGongysdm());
				map.put("cangkdm", bean.getCangkdm());
				Dingdlj lastDinglj = this.dingdljService.queryDingljObject(map);
				map.clear();
				if (null != lastDinglj) {
					// 得到p0----p4数量map集合(上一个周期的数量)
					mapCount = DingdljService.getPCounts(lastDinglj, dingd.getDingdnr().length());
					//CommonFun.mapPrint(mapCount, "柔性比例检查rouxblCheck方法mapCount");
					// 查询到零件实体，获取到关键零件标志
					Lingj lingj = this.lingjService.queryObject(bean.getLingjbh(), bean.getUsercenter());
					//CommonFun.objPrint(lingj, "柔性比例检查rouxblCheck方法lingj");
					// 得到p0----p4数量map集合
					//CommonFun.mapPrint(map_p, "柔性比例检查rouxblCheck方法map_p");
					// 得到p0----p4连续的周期
					// 查询到零件不为空
					if (null != lingj) {
						int i = 0;
						for (Map.Entry<String, BigDecimal> entry : map_p.entrySet()) {
							Double bl = BigDecimal.ZERO.doubleValue();
							if (mapCount.get(entry.getKey()) != null) {
								if (mapCount.get(entry.getKey()).compareTo(BigDecimal.ZERO) == 0) {
									String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh() };
									this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str1, jihyz, paramStr, bean.getUsercenter(),
											bean.getLingjbh(), loginuser, jismk);
								} else {
									bl = map_p.get(entry.getKey()).subtract(mapCount.get(entry.getKey()))
											.divide(mapCount.get(entry.getKey()), 3, BigDecimal.ROUND_DOWN).doubleValue();
									CommonFun.logger.debug("柔性比例检查rouxblCheck方法bl=" + bl);
								}

							}
							// 在柔性比例表中查询出具体的比例来
							BigDecimal rxbl = this.rouxblService.getRxbl(bean.getUsercenter(), bean.getGuanjljjb(), "P" + i, bean.getGongysdm());
							CommonFun.logger.debug("柔性比例检查rouxblCheck方法rxbl=" + rxbl);

							if (rxbl != null) {
								// 将得到的比例做边界处理
								Map<String, Double> rxblMap = this.rouxblService.checkRxbl(rxbl, 1.0, bean, jihyz, loginuser);
								//CommonFun.mapPrint(rxblMap, "柔性比例检查rouxblCheck方法rxblMap");
								// 不能大于最大，也不能小于最小
								if (rxblMap.size() > 0) {
									if (bl > rxblMap.get("max") || -bl < rxblMap.get("min")) {
										String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh() };
										this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str1, jihyz, paramStr,
												bean.getUsercenter(), bean.getLingjbh(), loginuser, jismk);

									}
								}
							}
							i++;
						}
					}
				} else {
					String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh() };
					this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str1 + "A订单号=" + dingd.getDingdh() + "B订单号=" + obj.getDingdh(), jihyz, paramStr, bean.getUsercenter(),
							bean.getLingjbh(), loginuser, jismk);
				}
			}
		}
	}
	
///////////////////////wuyichao//////////////////////////////////////
	public void newRouxblCheck(List<Dingdlj> dingdLjList, String jihyz, LoginUser loginuser, String rilbc, Dingd dingd) throws SecurityException,
	IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		// 放置查询参数的map
		Map<String, String> map = new HashMap<String, String>();
		// 存放上个周期的p0----p4数量map
		Map<String, BigDecimal> mapCount = new TreeMap<String, BigDecimal>();
		// 缓存零件的柔性比例  Key 为 usercenter+guanjljjb()+ "Px"+ gongysdm;
		Map<String ,BigDecimal > rxblCacheMap = new HashMap<String, BigDecimal>();
		// 设置计算模块
		String jismk = Const.JISMK_KD_CD;
		List<Zuiddhsl> zuiddhslList = zuiddhslService.queryList();
		Map<String,Zuiddhsl> zuiddhslMap = new HashMap<String, Zuiddhsl>();
		if(null != zuiddhslList)
		{
			for (Zuiddhsl zuiddhsl : zuiddhslList) {
				zuiddhslMap.put(zuiddhsl.getUsercenter() + zuiddhsl.getLingjbh() + zuiddhsl.getGongysbh() + zuiddhsl.getNianzq(), zuiddhsl);
			}
		}
		/////得到上次已计算并且 计算周期小于当前订单，同类型的订单
		List<Dingdlj> lastDingdljList = new ArrayList<Dingdlj>();
		Map<String,Dingdlj> lastDingdljMap = new HashMap<String, Dingdlj>();
		Dingd lastDingd = getLastDingd(dingd);
		if(null == lastDingd)
		{
		}
		else
		{
			map.put("dingdh", lastDingd.getDingdh());
			lastDingdljList = dingdljService.queryAllDingdlj(map);
			map.clear();
		}
		if(0 < lastDingdljList.size())
		{
			for (Dingdlj dingdlj : lastDingdljList)
			{
				lastDingdljMap.put(dingdlj.getUsercenter() + dingdlj.getLingjbh() + dingdlj.getGongysdm() + dingdlj.getCangkdm(), dingdlj);
			}
		}
		
		if(null != dingdLjList && 0 < dingdLjList.size())
		{
			for (Dingdlj bean : dingdLjList) 
			{
				int index = 0;
				Map<String, BigDecimal> map_p = DingdljService.getPCounts(bean, dingd.getDingdnr().length());
				for (Map.Entry<String, BigDecimal> entry : map_p.entrySet()) {
					String nianzq = CommonFun.addNianzq(bean.getP0fyzqxh(), Const.MAXZQ, index);
					Zuiddhsl object = zuiddhslMap.get(bean.getUsercenter()+ bean.getLingjbh() + bean.getGongysdm() + nianzq);
					index++;
					//CommonFun.objPrint(object, "柔性比例检查rouxblCheck方法object");
					if (null != object) {
						if (map_p.get(entry.getKey()).compareTo(object.getZuiddhsl()) == 1) {
							String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh(),nianzq};
							this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str10, jihyz, paramStr, bean.getUsercenter(),
									bean.getLingjbh(), loginuser, jismk);
						}
					}
				}
				//得到与当前计算所对应的上一次计算的订单零件信息
				Dingdlj lastDinglj = lastDingdljMap.get(bean.getUsercenter() + bean.getLingjbh() + bean.getGongysdm() + bean.getCangkdm());
					
				if (null != lastDinglj) {
					// 得到p0----p4数量map集合(上一个周期的数量)
					mapCount = DingdljService.getPCounts(lastDinglj, dingd.getDingdnr().length());
					//CommonFun.mapPrint(mapCount, "柔性比例检查rouxblCheck方法mapCount");
					//CommonFun.mapPrint(map_p, "柔性比例检查rouxblCheck方法map_p");
					// 得到p0----p4连续的周期
					// 查询到零件不为空
					int i = 0;
					for (Map.Entry<String, BigDecimal> entry : map_p.entrySet()) {
						Double bl = BigDecimal.ZERO.doubleValue();
						///本周的P0与上周的P0 + 1对比
						if (mapCount.get(CommonFun.addNianzq(entry.getKey(), "12", 1)) != null) {
							if (mapCount.get(entry.getKey()).compareTo(BigDecimal.ZERO) == 0) {
								String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh() };
								this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str1, jihyz, paramStr, bean.getUsercenter(),
										bean.getLingjbh(), loginuser, jismk);
							} else {
								bl = map_p.get(entry.getKey()).subtract(mapCount.get(entry.getKey()))
								.divide(mapCount.get(entry.getKey()), 3, BigDecimal.ROUND_DOWN).doubleValue();
								CommonFun.logger.debug("柔性比例检查rouxblCheck方法bl=" + bl);
							}
							
						}
						// 在柔性比例表中查询出具体的比例来
						BigDecimal rxbl = getRxbl(bean.getUsercenter(), bean.getGuanjljjb(), "P" + i, bean.getGongysdm(),rxblCacheMap);
						CommonFun.logger.debug("柔性比例检查rouxblCheck方法rxbl=" + rxbl);
						
						if (rxbl != null) {
							// 将得到的比例做边界处理
							Map<String, Double> rxblMap = this.rouxblService.checkRxbl(rxbl, 1.0, bean, jihyz, loginuser);
							//CommonFun.mapPrint(rxblMap, "柔性比例检查rouxblCheck方法rxblMap");
							// 不能大于最大，也不能小于最小
							if (rxblMap.size() > 0) {
								if (bl > rxblMap.get("max") || -bl < rxblMap.get("min")) {
									String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh() };
									this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str1, jihyz, paramStr,
											bean.getUsercenter(), bean.getLingjbh(), loginuser, jismk);
									
								}
							}
						}
						i++;
					}
				} 
			    else 
			    {
						String paramStr[] = new String[] { dingd.getDingdh() };
						this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str6, jihyz, paramStr,  bean.getUsercenter(),  bean.getLingjbh(),
								loginuser, jismk);
				}
			}
			
		}
	}
	
	/**
	 * @see   冲缓存map内读取柔性比例，如无则查出其柔性比例存放于缓存内
	 * @param usercenter
	 * @param guanjljjb
	 * @param px
	 * @param gongysdm
	 * @param rxblCacheMap
	 * @return
	 */
	private BigDecimal getRxbl(String usercenter, String guanjljjb, String px,
			String gongysdm , Map<String , BigDecimal> rxblCacheMap) {
		
		BigDecimal rxbl = null;
		if(null != rxblCacheMap)
		{
			 rxbl = rxblCacheMap.get(usercenter + guanjljjb + px + gongysdm);
			 if(null == rxbl)
			 {
				 rxbl =  this.rouxblService.getRxbl(usercenter, guanjljjb, px, gongysdm);
				 rxblCacheMap.put(usercenter + guanjljjb + px + gongysdm, rxbl);
			 }
		}
		return rxbl;
	}

///////////////////////wuyichao//////////////////////////////////////	
	
	
	
	
	/**
	 * kd订单周要货量计算
	 * 
	 * @author 袁修瑞
	 * @version v1.0
	 * @throws ParseException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2012-12-13
	 * @参数说明：订单零件实体
	 */
	@Transactional
	public void newWeekOrderCount(Dingdlj bean, String jihydm, String lingjmc, String gongsmc, String neibyhzx, String caozz, String rilbc,
			LoginUser loginuser, String gcbh) throws ParseException, SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单计算", "周要货量计算开始");

		// 异常报警计算模块
		String jismk = Const.JISMK_KD_CD;
		// 异常报警错误类型
		String cuowlx = Const.CUOWLX_200;
		// 异常报警信息
		String info = "";
		//CommonFun.objPrint(bean, " kd订单周要货量计算weekOrderCount方法Dingdlj");
		if (null == bean) {
			// info = "订单零件信息为空";
			// this.yicbjService.saveYicInfo(jismk, null, cuowlx, info);
			// this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单计算",
			// "周要货量计算结束");
			return;
		}

		// 反射获取到类
		Class<? extends Dingdlj> cls = bean.getClass();
		// 定义动态的方法值
		String methodName = null;
		// 定义P的数量
		BigDecimal pisl = BigDecimal.ZERO;

		// 获取到需要拆分既定的个数
		int count = this.dingdljService.getDingdnr(bean.getDingdnr());
		CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法count=" + count);
		// 得到零件编号
		String lingjbh = bean.getLingjbh();
		CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法lingjbh=" + lingjbh);
		// 获取到零件，从而得到第一起运时间
		Lingj lingj = this.lingjService.queryObject(bean.getLingjbh(), bean.getUsercenter());
		//CommonFun.objPrint(lingj, "kd订单周要货量计算weekOrderCount方法lingj");
		// 判断是否取得第一起运时间
		if (null == lingj) {
			String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh() };
			this.yicbjService
					.insertError(cuowlx, Const.YICHANG_LX0_str1, jihydm, paramStr, bean.getUsercenter(), bean.getLingjbh(), loginuser, jismk);
			return;
		}

		String nianzq = "";
		// 参数存放Map
		Map<String, Object> paramMap = new HashMap<String, Object>();

		List<Dingdmx> mxList = new ArrayList<Dingdmx>();
		int szhous = 0;// 记录当前算到第几周
		for (int pi = 0; pi < bean.getDingdnr().length(); pi++) {
			// List<Dingdmx> tempmxList = new ArrayList<Dingdmx>();
			// 周期内周数
			nianzq = CommonFun.addNianzq(bean.getP0fyzqxh(), Const.MAXZQ, pi);
			// 得到周期内周数
			int zhoushu = this.calendarversionservice.getZhouShu(Const.KDRILI, nianzq, rilbc, null);
			int gongZRsZhouqi = this.calendarversionservice.getGongZRsByZhouqi(Const.KDRILI, nianzq, rilbc, null);
			// 第一次启运时间所在周期的周期信息（包含日、周序、周期序信息）
			CalendarCenter diyiciqiyunObject = null;

			if (pi == 0) {
				// 如果第一次启运时间为空
				CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0）时Diycqysj=" + lingj.getDiycqysj());
				if (null == lingj.getDiycqysj()) {
					// 动态获取
					methodName = Const.GETP + pi + "sl";
					// 获取到方法
					Method meth = cls.getMethod(methodName, new Class[] {});
					pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);
					// 零件数量按包装取整
					BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
					BigDecimal zhoubzgs = BigDecimal.ZERO;
					BigDecimal zhouyaoh = BigDecimal.ZERO;
					Map<String, Object> resultMap = new HashMap<String, Object>();
					String shifgzr = "";
					int sign = 0;
					// 周均分包装
					for (int i = 0; i < zhoushu; i++) {
						// 实例化订单明细
						Dingdmx mx = new Dingdmx();

						mx.setZhidgys(bean.getZhidgys());
						mx.setZuixqdl(bean.getZuixqdl());
						mx.setGonghlx(Const.PP);
						mx.setActive(Const.ACTIVE_1);
						mx.setDanw(bean.getDanw());
						mx.setUabzlx(bean.getUabzlx());
						mx.setUabzuclx(bean.getUabzuclx());
						mx.setUabzucrl(bean.getUabzucrl());
						mx.setUabzucsl(bean.getUabzucsl());
						mx.setYugsfqz(bean.getYugsfqz());
						mx.setLujdm(bean.getLujdm());
						mx.setJihyz(bean.getJihyz());
						mx.setYijfl(bean.getYijfl());
						mx.setDingdh(bean.getDingdh());
						mx.setUsercenter(bean.getUsercenter());
						mx.setLingjbh(bean.getLingjbh());
						mx.setGongysdm(bean.getGongysdm());
//						mx.setGonghlx(bean.getGongyslx());
						mx.setCangkdm(bean.getCangkdm());
						mx.setFahd(bean.getFahd());
						mx.setDinghcj(bean.getDinghcj());
						mx.setCreate_time(CommonFun.getJavaTime());
						mx.setCreator(caozz);
						mx.setEdit_time(CommonFun.getJavaTime());
						mx.setEditor(caozz);
						mx.setZhuangt(Const.DINGD_STATUS_DSX);
//						mx.setZhidgys(bean.getZhidgys());
						mx.setZuixqdl(bean.getZuixqdl());
//						mx.setGonghlx(Const.PP);
//						mx.setActive(Const.ACTIVE_1);
						mx.setLingjmc(lingjmc);
						mx.setGongsmc(gongsmc);
						mx.setNeibyhzx(neibyhzx);
						mx.setXiehzt(bean.getXiehzt());
						mx.setGcbh(gcbh);

						if (count > 0) {
							mx.setLeix(Const.SHIFOUSHIJIDING);
						} else {
							mx.setLeix(Const.SHIFOUSHIYUGAO);
						}

						paramMap.put("usercenter", Const.KDRILI);
						paramMap.put("nianzq", nianzq);
						paramMap.put("banc", rilbc);
						paramMap.put("zhoux", "0" + (i + 1));
						paramMap.put("shifgzr", "1");
						paramMap.put("riq", lingj.getDiycqysj());
						int gongZrZhou = this.calendarversionservice
								.getGongZRsByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), lingj.getDiycqysj());
						shifgzr=gongZrZhou>0?"1":"0";
						sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
						paramMap.put("shifgzr", shifgzr);
						String starttime = this.getAfterLineStartWeekDate(paramMap, BigDecimal.ZERO);
						shifgzr =null;//显示都是已整个工作周为
						paramMap.put("shifgzr", shifgzr);
						String endttime = this.getAfterLineEndWeekDate(paramMap, BigDecimal.ZERO);
						zhouyaoh =new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsZhouqi), 5, BigDecimal.ROUND_HALF_DOWN).multiply(baozgs)
								.intValue()) ;
						zhoubzgs = zhoubzgs.add(zhouyaoh);
						mx.setYaohqsrq(this.getAfterLineStartWeekDate(paramMap, BigDecimal.ZERO));
						
						Calendar calendar = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						calendar.setTime(sdf.parse(starttime));
						calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
						mx.setJiaofrq(sdf.format(calendar.getTime()));
						mx.setFayrq(mx.getYaohqsrq());
						
						mx.setYaohjsrq(endttime);
						mx.setShul(zhouyaoh);
						mx.setJissl(zhouyaoh);
						mx.setSign(sign);
						paramMap.clear();
						mxList.add(mx);

					}

					int yushu = baozgs.subtract(zhoubzgs).intValue();
					for (int x = szhous; x < (szhous + zhoushu); x++) {
						if (mxList.get(x).getSign() == 1 && yushu > 0) {
							BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
							mxList.get(x).setShul(shul);
							yushu--;
						}
					}
					
//					mxList = getOrdeyDetailListWeekCount(bean, lingjmc, gongsmc, neibyhzx, caozz, rilbc, loginuser, gcbh, zhoushu, szhous, gongZRsZhouqi, count, lingj, nianzq, paramMap, pisl, mxList);

				} else {
					java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
					// // 转换第一次启运时间为date类型
					Date diycqyDate = df.parse(lingj.getDiycqysj());
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()）时diycqyDate=" + diycqyDate);
					java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyyMMdd");
					// 将第一次启运时间转为yyyyMMdd格式后截取年月
					String diycqyzhouqx = df2.format(diycqyDate).substring(0, 6);
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()）时diycqyzhouqx=" + diycqyzhouqx);

					// 第一次起运时间在周期内
					if (diycqyzhouqx.compareTo(bean.getP0fyzqxh()) == 0) {
						// 动态获取
						methodName = Const.GETP + pi + "sl";
						// 获取到方法
						Method meth = cls.getMethod(methodName, new Class[] {});
						pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
						CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);

						// 从起运日期开始的周数
						int qiyzhou = this.calendarversionservice.getZhouShu(Const.KDRILI, nianzq, rilbc, lingj.getDiycqysj());
						// 从起运日期开始的工作日天数
						int gongZRsByZhouqiAndQiy = this.calendarversionservice.getGongZRsByZhouqi(Const.KDRILI, nianzq, rilbc, lingj.getDiycqysj());

						// 零件数量按包装取整
						BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
						BigDecimal zhoubzgs = BigDecimal.ZERO;
						BigDecimal zhouyaoh = BigDecimal.ZERO;
						Map<String, Object> resultMap = new HashMap<String, Object>();
						String shifgzr = "";
						int  sign =  0 ;
						// 周均分包装
						for (int i = zhoushu - qiyzhou; i < zhoushu; i++) {
							// 实例化订单明细
							Dingdmx mx = new Dingdmx();
                            
							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
							mx.setGonghlx(Const.PP);
							mx.setActive(Const.ACTIVE_1);
							mx.setDanw(bean.getDanw());
							mx.setUabzlx(bean.getUabzlx());
							mx.setUabzuclx(bean.getUabzuclx());
							mx.setUabzucrl(bean.getUabzucrl());
							mx.setUabzucsl(bean.getUabzucsl());
							mx.setYugsfqz(bean.getYugsfqz());
							mx.setLujdm(bean.getLujdm());
							mx.setJihyz(bean.getJihyz());
							mx.setYijfl(bean.getYijfl());
							mx.setDingdh(bean.getDingdh());
							mx.setUsercenter(bean.getUsercenter());
							mx.setLingjbh(bean.getLingjbh());
							mx.setGongysdm(bean.getGongysdm());
//							mx.setGonghlx(bean.getGongyslx());
							mx.setCangkdm(bean.getCangkdm());
							mx.setFahd(bean.getFahd());
							mx.setDinghcj(bean.getDinghcj());
							mx.setCreate_time(CommonFun.getJavaTime());
							mx.setCreator(caozz);
							mx.setEdit_time(CommonFun.getJavaTime());
							mx.setEditor(caozz);
							mx.setZhuangt(Const.DINGD_STATUS_DSX);
//							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
//							mx.setGonghlx(Const.PP);
//							mx.setActive(Const.ACTIVE_1);
							mx.setLingjmc(lingjmc);
							mx.setGongsmc(gongsmc);
							mx.setNeibyhzx(neibyhzx);
							mx.setXiehzt(bean.getXiehzt());
							mx.setGcbh(gcbh);

							if (count > 0) {
								mx.setLeix(Const.SHIFOUSHIJIDING);
							} else {
								mx.setLeix(Const.SHIFOUSHIYUGAO);
							}

							paramMap.put("usercenter", Const.KDRILI);
							paramMap.put("nianzq", nianzq);
							paramMap.put("banc", rilbc);
							paramMap.put("zhoux", "0" + (i + 1));
							paramMap.put("shifgzr", "1");
							paramMap.put("riq", lingj.getDiycqysj());
							int gongZrZhou = this.calendarversionservice.getGongZRsByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1),
									lingj.getDiycqysj());
							shifgzr=gongZrZhou>0?"1":"0";
							sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
							
							paramMap.put("shifgzr", shifgzr);
							String starttime = this.getAfterLineStartWeekDate(paramMap, BigDecimal.ZERO);
							shifgzr =null;//显示都是已整个工作周为
							paramMap.put("shifgzr", shifgzr);
							String endttime = this.getAfterLineEndWeekDate(paramMap, BigDecimal.ZERO);
							zhouyaoh = new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsByZhouqiAndQiy), 5, BigDecimal.ROUND_HALF_DOWN)
									.multiply(baozgs).intValue());
							zhoubzgs = zhoubzgs.add(zhouyaoh);
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							calendar.setTime(sdf.parse(starttime));
							calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
							mx.setJiaofrq(sdf.format(calendar.getTime()));
							mx.setYaohqsrq(this.getAfterLineStartWeekDate(paramMap, BigDecimal.ZERO));
							mx.setFayrq(starttime);
							mx.setYaohjsrq(endttime);
							mx.setShul(zhouyaoh);
							mx.setJissl(zhouyaoh);
							mx.setSign(sign);
							mxList.add(mx);
							paramMap.clear();
						}

						int yushu = baozgs.subtract(zhoubzgs).intValue();

						for (int x = szhous; x < (szhous + qiyzhou); x++) {
							if (mxList.get(x).getSign() == 1 && yushu > 0) {
								BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
								mxList.get(x).setShul(shul);
								yushu--;
							}
						}
   
//						mxList = getOrdeyDetailListWeekCount(bean, lingjmc, gongsmc, neibyhzx, caozz, rilbc, loginuser, gcbh, zhoushu, szhous, gongZRsZhouqi, count, lingj, nianzq, paramMap, pisl, mxList);
						
						// 起运周期之前的周不参与均分
						for (int i = 0; i < zhoushu - qiyzhou; i++) {
							// 实例化订单明细
							Dingdmx mx = new Dingdmx();

							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
							mx.setGonghlx(Const.PP);
							mx.setActive(Const.ACTIVE_1);
							mx.setDanw(bean.getDanw());
							mx.setUabzlx(bean.getUabzlx());
							mx.setUabzuclx(bean.getUabzuclx());
							mx.setUabzucrl(bean.getUabzucrl());
							mx.setUabzucsl(bean.getUabzucsl());
							mx.setYugsfqz(bean.getYugsfqz());
							mx.setLujdm(bean.getLujdm());
							mx.setJihyz(bean.getJihyz());
							mx.setYijfl(bean.getYijfl());
							mx.setDingdh(bean.getDingdh());
							mx.setUsercenter(bean.getUsercenter());
							mx.setLingjbh(bean.getLingjbh());
							mx.setGongysdm(bean.getGongysdm());
//							mx.setGonghlx(bean.getGongyslx());
							mx.setCangkdm(bean.getCangkdm());
							mx.setFahd(bean.getFahd());
							mx.setDinghcj(bean.getDinghcj());
							mx.setCreate_time(CommonFun.getJavaTime());
							mx.setCreator(caozz);
							mx.setEdit_time(CommonFun.getJavaTime());
							mx.setEditor(caozz);
							mx.setZhuangt(Const.DINGD_STATUS_DSX);
//							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
//							mx.setGonghlx(Const.PP);
//							mx.setActive(Const.ACTIVE_1);
							mx.setLingjmc(lingjmc);
							mx.setGongsmc(gongsmc);
							mx.setNeibyhzx(neibyhzx);
							mx.setXiehzt(bean.getXiehzt());
							mx.setGcbh(gcbh);

							if (count > 0) {
								mx.setLeix(Const.SHIFOUSHIJIDING);
							} else {
								mx.setLeix(Const.SHIFOUSHIYUGAO);
							}

							paramMap.put("usercenter", Const.KDRILI);
							paramMap.put("nianzq", nianzq);
							paramMap.put("banc", rilbc);
							paramMap.put("zhoux", "0" + (i + 1));
							String starttime = this.getAfterLineStartWeekDate(paramMap, BigDecimal.ZERO);
							String endttime = this.getAfterLineEndWeekDate(paramMap, BigDecimal.ZERO);
							mx.setYaohqsrq(starttime);
							mx.setYaohjsrq(endttime);
							mx.setShul(BigDecimal.ZERO);
							mx.setJissl(BigDecimal.ZERO);
							mx.setJiaofrq(starttime);
							mx.setFayrq(mx.getYaohqsrq());
							mx.setSign(0);
							mxList.add(mx);
							paramMap.clear();
						}
					} else if (diycqyzhouqx.compareTo(bean.getP0fyzqxh()) < 0) {
						// 动态获取
						methodName = Const.GETP + pi + "sl";
						// 获取到方法
						Method meth = cls.getMethod(methodName, new Class[] {});
						pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
						CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);
						// 零件数量按包装取整
						BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
						BigDecimal zhoubzgs = BigDecimal.ZERO;
						BigDecimal zhouyaoh = BigDecimal.ZERO;
						String shifgzr = "";
						int  sign = 0;
						// 周均分包装
						for (int i = 0; i < zhoushu; i++) {
							// 实例化订单明细
							Dingdmx mx = new Dingdmx();

							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
							mx.setGonghlx(Const.PP);
							mx.setActive(Const.ACTIVE_1);
							mx.setDanw(bean.getDanw());
							mx.setUabzlx(bean.getUabzlx());
							mx.setUabzuclx(bean.getUabzuclx());
							mx.setUabzucrl(bean.getUabzucrl());
							mx.setUabzucsl(bean.getUabzucsl());
							mx.setYugsfqz(bean.getYugsfqz());
							mx.setLujdm(bean.getLujdm());
							mx.setJihyz(bean.getJihyz());
							mx.setYijfl(bean.getYijfl());
							mx.setDingdh(bean.getDingdh());
							mx.setUsercenter(bean.getUsercenter());
							mx.setLingjbh(bean.getLingjbh());
							mx.setGongysdm(bean.getGongysdm());
//							mx.setGonghlx(bean.getGongyslx());
							mx.setCangkdm(bean.getCangkdm());
							mx.setFahd(bean.getFahd());
							mx.setDinghcj(bean.getDinghcj());
							mx.setCreate_time(CommonFun.getJavaTime());
							mx.setCreator(caozz);
							mx.setEdit_time(CommonFun.getJavaTime());
							mx.setEditor(caozz);
							mx.setZhuangt(Const.DINGD_STATUS_DSX);
//							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
//							mx.setGonghlx(Const.PP);
//							mx.setActive(Const.ACTIVE_1);
							mx.setLingjmc(lingjmc);
							mx.setGongsmc(gongsmc);
							mx.setNeibyhzx(neibyhzx);
							mx.setXiehzt(bean.getXiehzt());
							mx.setGcbh(gcbh);

							if (count > 0) {
								mx.setLeix(Const.SHIFOUSHIJIDING);
							} else {
								mx.setLeix(Const.SHIFOUSHIYUGAO);
							}

							paramMap.put("usercenter", Const.KDRILI);
							paramMap.put("nianzq", nianzq);
							paramMap.put("banc", rilbc);
							paramMap.put("zhoux", "0" + (i + 1));
							paramMap.put("shifgzr", "1");
							paramMap.put("riq", lingj.getDiycqysj());
							int gongZrZhou = this.calendarversionservice.getGongZRsByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1),
									lingj.getDiycqysj());
							shifgzr=gongZrZhou>0?"1":"0";
							sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
							paramMap.put("shifgzr", shifgzr);
							String starttime = this.getAfterLineStartWeekDate(paramMap, BigDecimal.ZERO);
							shifgzr =null;//显示都是已整个工作周为
							paramMap.put("shifgzr", shifgzr);
							String endttime = this.getAfterLineEndWeekDate(paramMap, BigDecimal.ZERO);

							zhouyaoh = new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsZhouqi), 5, BigDecimal.ROUND_HALF_DOWN)
									.multiply(baozgs).intValue());
							zhoubzgs = zhoubzgs.add(zhouyaoh);
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							calendar.setTime(sdf.parse(starttime));
							calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
							mx.setJiaofrq(sdf.format(calendar.getTime()));
							mx.setYaohqsrq(this.getAfterLineStartWeekDate(paramMap, BigDecimal.ZERO));
							mx.setYaohjsrq(endttime);
							mx.setFayrq(mx.getYaohqsrq());
							mx.setShul(zhouyaoh);
							mx.setJissl(zhouyaoh);
							mx.setSign(sign);
							mxList.add(mx);
							paramMap.clear();
						}

						int yushu = baozgs.subtract(zhoubzgs).intValue();

						for (int x = szhous; x < (szhous + zhoushu); x++) {
							if (mxList.get(x).getSign() == 1 && yushu > 0) {
								BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
								mxList.get(x).setShul(shul);
								yushu--;
							}
						}
						
//						mxList = getOrdeyDetailListWeekCount(bean, lingjmc, gongsmc, neibyhzx, caozz, rilbc, loginuser, gcbh, zhoushu, szhous, gongZRsZhouqi, count, lingj, nianzq, paramMap, pisl, mxList);

					} else {
						// 如果第一次启运时间所在周期大于p0周期，则不进行拆分，并写入异常报警表
						String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh(), lingj.getDiycqysj(), "第一启运时间" };
						this.yicbjService.insertError(cuowlx, Const.YICHANG_LX2_str60, jihydm, paramStr, bean.getUsercenter(), bean.getLingjbh(),
								loginuser, jismk);
						this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单计算", "周要货量计算结束");
						return;
					}
				}
				szhous += zhoushu;
			} else {

				// 动态获取
				methodName = Const.GETP + pi + "sl";
				// 获取到方法
				Method meth = cls.getMethod(methodName, new Class[] {});
				pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
				CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);
//				// 零件数量按包装取整
				BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
				BigDecimal zhoubzgs = BigDecimal.ZERO;
				BigDecimal zhouyaoh = BigDecimal.ZERO;
				String shifgzr = "";
				int sign = 0;
				// 周均分包装
				for (int i = 0; i < zhoushu; i++) {
					// 实例化订单明细
					Dingdmx mx = new Dingdmx();

					mx.setZhidgys(bean.getZhidgys());
					mx.setZuixqdl(bean.getZuixqdl());
					mx.setGonghlx(Const.PP);
					mx.setActive(Const.ACTIVE_1);
					mx.setDanw(bean.getDanw());
					mx.setUabzlx(bean.getUabzlx());
					mx.setUabzuclx(bean.getUabzuclx());
					mx.setUabzucrl(bean.getUabzucrl());
					mx.setUabzucsl(bean.getUabzucsl());
					mx.setYugsfqz(bean.getYugsfqz());
					mx.setLujdm(bean.getLujdm());
					mx.setJihyz(bean.getJihyz());
					mx.setYijfl(bean.getYijfl());
					mx.setDingdh(bean.getDingdh());
					mx.setUsercenter(bean.getUsercenter());
					mx.setLingjbh(bean.getLingjbh());
					mx.setGongysdm(bean.getGongysdm());
//					mx.setGonghlx(bean.getGongyslx());
					mx.setCangkdm(bean.getCangkdm());
					mx.setFahd(bean.getFahd());
					mx.setDinghcj(bean.getDinghcj());
					mx.setCreate_time(CommonFun.getJavaTime());
					mx.setCreator(caozz);
					mx.setEdit_time(CommonFun.getJavaTime());
					mx.setEditor(caozz);
					mx.setZhuangt(Const.DINGD_STATUS_DSX);
//					mx.setZhidgys(bean.getZhidgys());
					mx.setZuixqdl(bean.getZuixqdl());
//					mx.setGonghlx(Const.PP);
//					mx.setActive(Const.ACTIVE_1);
					mx.setLingjmc(lingjmc);
					mx.setGongsmc(gongsmc);
					mx.setNeibyhzx(neibyhzx);
					mx.setXiehzt(bean.getXiehzt());
					mx.setGcbh(gcbh);

					if (count > 0) {
						mx.setLeix(Const.SHIFOUSHIJIDING);
					} else {
						mx.setLeix(Const.SHIFOUSHIYUGAO);
					}

					paramMap.put("usercenter", Const.KDRILI);
					paramMap.put("nianzq", nianzq);
					paramMap.put("banc", rilbc);
					paramMap.put("zhoux", "0" + (i + 1));
					paramMap.put("shifgzr", "1");
					paramMap.put("riq", lingj.getDiycqysj());
					int gongZrZhou = this.calendarversionservice.getGongZRsByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), lingj.getDiycqysj());
					shifgzr =gongZrZhou>0?"1":"0";//显示都是已整个工作周为
					sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
					paramMap.put("shifgzr", shifgzr);
					String starttime = this.getAfterLineStartWeekDate(paramMap, BigDecimal.ZERO);
					shifgzr =null;//显示都是已整个工作周为
					paramMap.put("shifgzr", shifgzr);
					String endttime = this.getAfterLineEndWeekDate(paramMap, BigDecimal.ZERO);
					zhouyaoh =new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsZhouqi), 5, BigDecimal.ROUND_HALF_DOWN).multiply(baozgs)
							.intValue());
					zhoubzgs = zhoubzgs.add(zhouyaoh);
					mx.setYaohqsrq(this.getAfterLineStartWeekDate(paramMap, BigDecimal.ZERO));
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					calendar.setTime(sdf.parse(starttime));
					calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
					mx.setJiaofrq(sdf.format(calendar.getTime()));
					mx.setYaohjsrq(endttime);
					mx.setFayrq(mx.getYaohqsrq());
					mx.setShul(zhouyaoh);
					mx.setJissl(zhouyaoh);
					mx.setSign(sign);
					mxList.add(mx);
					paramMap.clear();
				}

				int yushu = baozgs.subtract(zhoubzgs).intValue();

				for (int x = szhous; x < (szhous + zhoushu); x++) {
					if (mxList.get(x).getSign() == 1 && yushu > 0) {
						BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
						mxList.get(x).setShul(shul);
						yushu--;
					}
				}
//				mxList = getOrdeyDetailListWeekCount(bean, lingjmc, gongsmc, neibyhzx, caozz, rilbc, loginuser, gcbh, zhoushu, szhous, gongZRsZhouqi, count, lingj, nianzq, paramMap, pisl, mxList);
				szhous += zhoushu;
			}

			count = count - 1;
			CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法count=" + count);
		}

		List<Dingdmx> newMxList = new ArrayList<Dingdmx>();
		for (Dingdmx dingdmx : mxList) {
			BigDecimal shul = BigDecimal.ZERO;
			if (!dingdmx.getShul().equals(null) || !dingdmx.getShul().equals(0)) {
				shul = dingdmx.getShul().multiply(bean.getUabzucrl().multiply(bean.getUabzucsl()));
			}
			dingdmx.setShul(shul);
			dingdmx.setJissl(shul);
			newMxList.add(dingdmx);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ilorder.insertDingdmx", newMxList);

		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单计算", "周要货量计算结束");
	}
	
	
/////////////////////////////////////wuyichao//////////////////////////////////////////////
	public void newWeekOrderCount(Dingdlj bean, String jihydm, String lingjmc, String gongsmc, String neibyhzx, String caozz, String rilbc,
			LoginUser loginuser, String gcbh ,Map<String , Lingj> lingjCacheMap, Map<String , Integer > zhousCacheMap ,Map<String , Integer > gzrByZhouCacheMap,Map<String , Integer > gzrByZhouqCacheMap ,Map<String , String > startDateByZhouCacheMap,Map<String , String > endDateByZhouCacheMap) throws ParseException, SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单计算", "周要货量计算开始");

		// 异常报警计算模块
		String jismk = Const.JISMK_KD_CD;
		// 异常报警错误类型
		String cuowlx = Const.CUOWLX_200;
		// 异常报警信息
		String info = "";
		//CommonFun.objPrint(bean, " kd订单周要货量计算weekOrderCount方法Dingdlj");
		if (null == bean) {
			return;
		}

		// 反射获取到类
		Class<? extends Dingdlj> cls = bean.getClass();
		// 定义动态的方法值
		String methodName = null;
		// 定义P的数量
		BigDecimal pisl = BigDecimal.ZERO;

		// 获取到需要拆分既定的个数
		int count = this.dingdljService.getDingdnr(bean.getDingdnr());
		CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法count=" + count);
		// 得到零件编号
		String lingjbh = bean.getLingjbh();
		CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法lingjbh=" + lingjbh);
		// 获取到零件，从而得到第一起运时间
		Lingj lingj = lingjCacheMap.get(bean.getUsercenter() + bean.getLingjbh());
		//CommonFun.objPrint(lingj, "kd订单周要货量计算weekOrderCount方法lingj");
		// 判断是否取得第一起运时间
		if (null == lingj) {
			String paramStr[] = new String[] { bean.getUsercenter(), bean.getLingjbh() };
			this.yicbjService
					.insertError(cuowlx, Const.YICHANG_LX0_str1, jihydm, paramStr, bean.getUsercenter(), bean.getLingjbh(), loginuser, jismk);
			return;
		}

		String nianzq = "";
		// 参数存放Map
		Map<String, Object> paramMap = new HashMap<String, Object>();

		List<Dingdmx> mxList = new ArrayList<Dingdmx>();
		int szhous = 0;// 记录当前算到第几周
		
		//P1周期
		String nianzq_P1 = CommonFun.addNianzq(bean.getP0fyzqxh(), Const.MAXZQ, 1);
		
		for (int pi = 0; pi < bean.getDingdnr().length(); pi++) {
			// List<Dingdmx> tempmxList = new ArrayList<Dingdmx>();
			// 周期内周数
			nianzq = CommonFun.addNianzq(bean.getP0fyzqxh(), Const.MAXZQ, pi);
			// 得到周期内周数
			int zhoushu = getZhouShu(Const.KDRILI, nianzq, rilbc, zhousCacheMap);
			int gongZRsZhouqi = getGzrByZhouq(Const.KDRILI, nianzq, rilbc,gzrByZhouqCacheMap);
				
			// 第一次启运时间所在周期的周期信息（包含日、周序、周期序信息）
			CalendarCenter diyiciqiyunObject = null;

			if (pi == 0) {
				// 如果第一次启运时间为空
				CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0）时Diycqysj=" + lingj.getDiycqysj());
				if (null == lingj.getDiycqysj()) {
					// 动态获取
					methodName = Const.GETP + pi + "sl";
					// 获取到方法
					Method meth = cls.getMethod(methodName, new Class[] {});
					pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);
					// 零件数量按包装取整
					BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
					BigDecimal zhoubzgs = BigDecimal.ZERO;
					BigDecimal zhouyaoh = BigDecimal.ZERO;
					Map<String, Object> resultMap = new HashMap<String, Object>();
					String shifgzr = "";
					int sign = 0;
					// 周均分包装
					for (int i = 0; i < zhoushu; i++) {
						// 实例化订单明细
						Dingdmx mx = new Dingdmx();

						mx.setZhidgys(bean.getZhidgys());
						mx.setZuixqdl(bean.getZuixqdl());
						mx.setGonghlx(Const.PP);
						mx.setActive(Const.ACTIVE_1);
						mx.setDanw(bean.getDanw());
						mx.setUabzlx(bean.getUabzlx());
						mx.setUabzuclx(bean.getUabzuclx());
						mx.setUabzucrl(bean.getUabzucrl());
						mx.setUabzucsl(bean.getUabzucsl());
						mx.setYugsfqz(bean.getYugsfqz());
						mx.setLujdm(bean.getLujdm());
						mx.setJihyz(bean.getJihyz());
						mx.setYijfl(bean.getYijfl());
						mx.setDingdh(bean.getDingdh());
						mx.setUsercenter(bean.getUsercenter());
						mx.setLingjbh(bean.getLingjbh());
						mx.setGongysdm(bean.getGongysdm());
//						mx.setGonghlx(bean.getGongyslx());
						mx.setCangkdm(bean.getCangkdm());
						mx.setFahd(bean.getFahd());
						mx.setDinghcj(bean.getDinghcj());
						mx.setCreate_time(CommonFun.getJavaTime());
						mx.setCreator(caozz);
						mx.setEdit_time(CommonFun.getJavaTime());
						mx.setEditor(caozz);
						mx.setZhuangt(Const.DINGD_STATUS_DSX);
//						mx.setZhidgys(bean.getZhidgys());
						mx.setZuixqdl(bean.getZuixqdl());
//						mx.setGonghlx(Const.PP);
//						mx.setActive(Const.ACTIVE_1);
						mx.setLingjmc(lingjmc);
						mx.setGongsmc(gongsmc);
						mx.setNeibyhzx(neibyhzx);
						mx.setXiehzt(bean.getXiehzt());
						mx.setGcbh(gcbh);

						if (count > 0) {
							mx.setLeix(Const.SHIFOUSHIJIDING);
						} else {
							mx.setLeix(Const.SHIFOUSHIYUGAO);
						}

						paramMap.put("usercenter", Const.KDRILI);
						paramMap.put("nianzq", nianzq);
						paramMap.put("banc", rilbc);
						paramMap.put("zhoux", "0" + (i + 1));
						paramMap.put("shifgzr", "1");
						paramMap.put("riq", lingj.getDiycqysj());
						int gongZrZhou = getGzrByZhou(Const.KDRILI, nianzq, rilbc , "0" + (i + 1) , lingj.getDiycqysj(), gzrByZhouCacheMap);
						shifgzr=gongZrZhou>0?"1":"0";
						sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
						paramMap.put("shifgzr", shifgzr);
						String starttime = getStartWeekDate(Const.KDRILI,nianzq,rilbc, "0" + (i + 1),shifgzr,lingj.getDiycqysj(),BigDecimal.ZERO,startDateByZhouCacheMap);
							
							
						shifgzr =null;//显示都是已整个工作周为
						paramMap.put("shifgzr", shifgzr);
						String endttime = getEndWeekDate(Const.KDRILI,nianzq,rilbc, "0" + (i + 1),shifgzr,lingj.getDiycqysj(),BigDecimal.ZERO,endDateByZhouCacheMap);
							
						zhouyaoh =new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsZhouqi), 5, BigDecimal.ROUND_HALF_DOWN).multiply(baozgs)
								.intValue()) ;
						zhoubzgs = zhoubzgs.add(zhouyaoh);
						mx.setYaohqsrq(getStartWeekDate(Const.KDRILI,nianzq,rilbc, "0" + (i + 1),shifgzr,lingj.getDiycqysj(),BigDecimal.ZERO,startDateByZhouCacheMap));
						
						Calendar calendar = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						calendar.setTime(sdf.parse(starttime));
						calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
						mx.setJiaofrq(sdf.format(calendar.getTime()));
						mx.setFayrq(mx.getYaohqsrq());
						
						mx.setYaohjsrq(endttime);
						mx.setShul(zhouyaoh);
						mx.setJissl(zhouyaoh);
						mx.setSign(sign);
						paramMap.clear();
						mxList.add(mx);

					}

					int yushu = baozgs.subtract(zhoubzgs).intValue();
					for (int x = szhous; x < (szhous + zhoushu); x++) {
						if (mxList.get(x).getSign() == 1 && yushu > 0) {
							BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
							mxList.get(x).setShul(shul);
							yushu--;
						}
					}
					
//					mxList = getOrdeyDetailListWeekCount(bean, lingjmc, gongsmc, neibyhzx, caozz, rilbc, loginuser, gcbh, zhoushu, szhous, gongZRsZhouqi, count, lingj, nianzq, paramMap, pisl, mxList);

				} else {
					java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
					// // 转换第一次启运时间为date类型
					Date diycqyDate = df.parse(lingj.getDiycqysj());
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()）时diycqyDate=" + diycqyDate);
					java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyyMMdd");
					// 将第一次启运时间转为yyyyMMdd格式后截取年月
					String diycqyzhouqx = df2.format(diycqyDate).substring(0, 6);
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()）时diycqyzhouqx=" + diycqyzhouqx);

					// 第一次起运时间在周期内
					if (diycqyzhouqx.compareTo(bean.getP0fyzqxh()) == 0) {
						// 动态获取
						methodName = Const.GETP + pi + "sl";
						// 获取到方法
						Method meth = cls.getMethod(methodName, new Class[] {});
						pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
						CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);

						// 从起运日期开始的周数
						int qiyzhou = getZhouShu(Const.KDRILI, nianzq, rilbc,  lingj.getDiycqysj(), zhousCacheMap);
						// 从起运日期开始的工作日天数
						int gongZRsByZhouqiAndQiy = getGzrByZhouq(Const.KDRILI, nianzq, rilbc, lingj.getDiycqysj(), gzrByZhouqCacheMap);

						// 零件数量按包装取整
						BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
						BigDecimal zhoubzgs = BigDecimal.ZERO;
						BigDecimal zhouyaoh = BigDecimal.ZERO;
						Map<String, Object> resultMap = new HashMap<String, Object>();
						String shifgzr = "";
						int  sign =  0 ;
						// 周均分包装
						for (int i = zhoushu - qiyzhou; i < zhoushu; i++) {
							// 实例化订单明细
							Dingdmx mx = new Dingdmx();
                            
							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
							mx.setGonghlx(Const.PP);
							mx.setActive(Const.ACTIVE_1);
							mx.setDanw(bean.getDanw());
							mx.setUabzlx(bean.getUabzlx());
							mx.setUabzuclx(bean.getUabzuclx());
							mx.setUabzucrl(bean.getUabzucrl());
							mx.setUabzucsl(bean.getUabzucsl());
							mx.setYugsfqz(bean.getYugsfqz());
							mx.setLujdm(bean.getLujdm());
							mx.setJihyz(bean.getJihyz());
							mx.setYijfl(bean.getYijfl());
							mx.setDingdh(bean.getDingdh());
							mx.setUsercenter(bean.getUsercenter());
							mx.setLingjbh(bean.getLingjbh());
							mx.setGongysdm(bean.getGongysdm());
//							mx.setGonghlx(bean.getGongyslx());
							mx.setCangkdm(bean.getCangkdm());
							mx.setFahd(bean.getFahd());
							mx.setDinghcj(bean.getDinghcj());
							mx.setCreate_time(CommonFun.getJavaTime());
							mx.setCreator(caozz);
							mx.setEdit_time(CommonFun.getJavaTime());
							mx.setEditor(caozz);
							mx.setZhuangt(Const.DINGD_STATUS_DSX);
//							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
//							mx.setGonghlx(Const.PP);
//							mx.setActive(Const.ACTIVE_1);
							mx.setLingjmc(lingjmc);
							mx.setGongsmc(gongsmc);
							mx.setNeibyhzx(neibyhzx);
							mx.setXiehzt(bean.getXiehzt());
							mx.setGcbh(gcbh);

							if (count > 0) {
								mx.setLeix(Const.SHIFOUSHIJIDING);
							} else {
								mx.setLeix(Const.SHIFOUSHIYUGAO);
							}

							paramMap.put("usercenter", Const.KDRILI);
							paramMap.put("nianzq", nianzq);
							paramMap.put("banc", rilbc);
							paramMap.put("zhoux", "0" + (i + 1));
							paramMap.put("shifgzr", "1");
							paramMap.put("riq", lingj.getDiycqysj());
							int gongZrZhou = getGzrByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), lingj.getDiycqysj(), gzrByZhouCacheMap);
							shifgzr=gongZrZhou>0?"1":"0";
							sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
							
							paramMap.put("shifgzr", shifgzr);
							String starttime = getStartWeekDate(Const.KDRILI,nianzq,rilbc, "0" + (i + 1),shifgzr,lingj.getDiycqysj(),BigDecimal.ZERO,startDateByZhouCacheMap);
								
							shifgzr =null;//显示都是已整个工作周为
							paramMap.put("shifgzr", shifgzr);
							String endttime = getEndWeekDate(Const.KDRILI, nianzq, rilbc,  "0" + (i + 1), shifgzr, lingj.getDiycqysj(), BigDecimal.ZERO, endDateByZhouCacheMap);
							zhouyaoh = new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsByZhouqiAndQiy), 5, BigDecimal.ROUND_HALF_DOWN)
									.multiply(baozgs).intValue());
							zhoubzgs = zhoubzgs.add(zhouyaoh);
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							calendar.setTime(sdf.parse(starttime));
							calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
							mx.setJiaofrq(sdf.format(calendar.getTime()));
							mx.setYaohqsrq(getStartWeekDate(Const.KDRILI,nianzq,rilbc, "0" + (i + 1),shifgzr,lingj.getDiycqysj(),BigDecimal.ZERO,startDateByZhouCacheMap));
							mx.setFayrq(starttime);
							mx.setYaohjsrq(endttime);
							mx.setShul(zhouyaoh);
							mx.setJissl(zhouyaoh);
							mx.setSign(sign);
							mxList.add(mx);
							paramMap.clear();
						}

						int yushu = baozgs.subtract(zhoubzgs).intValue();

						for (int x = szhous; x < (szhous + qiyzhou); x++) {
							if (mxList.get(x).getSign() == 1 && yushu > 0) {
								BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
								mxList.get(x).setShul(shul);
								yushu--;
							}
						}
   
//						mxList = getOrdeyDetailListWeekCount(bean, lingjmc, gongsmc, neibyhzx, caozz, rilbc, loginuser, gcbh, zhoushu, szhous, gongZRsZhouqi, count, lingj, nianzq, paramMap, pisl, mxList);
						
						// 起运周期之前的周不参与均分
						for (int i = 0; i < zhoushu - qiyzhou; i++) {
							// 实例化订单明细
							Dingdmx mx = new Dingdmx();

							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
							mx.setGonghlx(Const.PP);
							mx.setActive(Const.ACTIVE_1);
							mx.setDanw(bean.getDanw());
							mx.setUabzlx(bean.getUabzlx());
							mx.setUabzuclx(bean.getUabzuclx());
							mx.setUabzucrl(bean.getUabzucrl());
							mx.setUabzucsl(bean.getUabzucsl());
							mx.setYugsfqz(bean.getYugsfqz());
							mx.setLujdm(bean.getLujdm());
							mx.setJihyz(bean.getJihyz());
							mx.setYijfl(bean.getYijfl());
							mx.setDingdh(bean.getDingdh());
							mx.setUsercenter(bean.getUsercenter());
							mx.setLingjbh(bean.getLingjbh());
							mx.setGongysdm(bean.getGongysdm());
//							mx.setGonghlx(bean.getGongyslx());
							mx.setCangkdm(bean.getCangkdm());
							mx.setFahd(bean.getFahd());
							mx.setDinghcj(bean.getDinghcj());
							mx.setCreate_time(CommonFun.getJavaTime());
							mx.setCreator(caozz);
							mx.setEdit_time(CommonFun.getJavaTime());
							mx.setEditor(caozz);
							mx.setZhuangt(Const.DINGD_STATUS_DSX);
//							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
//							mx.setGonghlx(Const.PP);
//							mx.setActive(Const.ACTIVE_1);
							mx.setLingjmc(lingjmc);
							mx.setGongsmc(gongsmc);
							mx.setNeibyhzx(neibyhzx);
							mx.setXiehzt(bean.getXiehzt());
							mx.setGcbh(gcbh);

							if (count > 0) {
								mx.setLeix(Const.SHIFOUSHIJIDING);
							} else {
								mx.setLeix(Const.SHIFOUSHIYUGAO);
							}

							paramMap.put("usercenter", Const.KDRILI);
							paramMap.put("nianzq", nianzq);
							paramMap.put("banc", rilbc);
							paramMap.put("zhoux", "0" + (i + 1));
							String starttime = getStartWeekDate( Const.KDRILI, nianzq, rilbc, "0" + (i + 1), BigDecimal.ZERO, startDateByZhouCacheMap);
							
							String endttime = getEndWeekDate( Const.KDRILI, nianzq, rilbc, "0" + (i + 1), BigDecimal.ZERO, endDateByZhouCacheMap);
							mx.setYaohqsrq(starttime);
							mx.setYaohjsrq(endttime);
							mx.setShul(BigDecimal.ZERO);
							mx.setJissl(BigDecimal.ZERO);
							mx.setFayrq(mx.getYaohqsrq());
							mx.setJiaofrq(starttime);
							mx.setSign(0);
							mxList.add(mx);
							paramMap.clear();
						}
					} else if (diycqyzhouqx.compareTo(bean.getP0fyzqxh()) < 0) {
						// 动态获取
						methodName = Const.GETP + pi + "sl";
						// 获取到方法
						Method meth = cls.getMethod(methodName, new Class[] {});
						pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
						CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);
						// 零件数量按包装取整
						BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
						BigDecimal zhoubzgs = BigDecimal.ZERO;
						BigDecimal zhouyaoh = BigDecimal.ZERO;
						String shifgzr = "";
						int  sign = 0;
						// 周均分包装
						for (int i = 0; i < zhoushu; i++) {
							// 实例化订单明细
							Dingdmx mx = new Dingdmx();

							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
							mx.setGonghlx(Const.PP);
							mx.setActive(Const.ACTIVE_1);
							mx.setDanw(bean.getDanw());
							mx.setUabzlx(bean.getUabzlx());
							mx.setUabzuclx(bean.getUabzuclx());
							mx.setUabzucrl(bean.getUabzucrl());
							mx.setUabzucsl(bean.getUabzucsl());
							mx.setYugsfqz(bean.getYugsfqz());
							mx.setLujdm(bean.getLujdm());
							mx.setJihyz(bean.getJihyz());
							mx.setYijfl(bean.getYijfl());
							mx.setDingdh(bean.getDingdh());
							mx.setUsercenter(bean.getUsercenter());
							mx.setLingjbh(bean.getLingjbh());
							mx.setGongysdm(bean.getGongysdm());
//							mx.setGonghlx(bean.getGongyslx());
							mx.setCangkdm(bean.getCangkdm());
							mx.setFahd(bean.getFahd());
							mx.setDinghcj(bean.getDinghcj());
							mx.setCreate_time(CommonFun.getJavaTime());
							mx.setCreator(caozz);
							mx.setEdit_time(CommonFun.getJavaTime());
							mx.setEditor(caozz);
							mx.setZhuangt(Const.DINGD_STATUS_DSX);
//							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
//							mx.setGonghlx(Const.PP);
//							mx.setActive(Const.ACTIVE_1);
							mx.setLingjmc(lingjmc);
							mx.setGongsmc(gongsmc);
							mx.setNeibyhzx(neibyhzx);
							mx.setXiehzt(bean.getXiehzt());
							mx.setGcbh(gcbh);

							if (count > 0) {
								mx.setLeix(Const.SHIFOUSHIJIDING);
							} else {
								mx.setLeix(Const.SHIFOUSHIYUGAO);
							}

							paramMap.put("usercenter", Const.KDRILI);
							paramMap.put("nianzq", nianzq);
							paramMap.put("banc", rilbc);
							paramMap.put("zhoux", "0" + (i + 1));
							paramMap.put("shifgzr", "1");
							paramMap.put("riq", lingj.getDiycqysj());
							int gongZrZhou = getGzrByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), lingj.getDiycqysj(), gzrByZhouCacheMap);
							shifgzr=gongZrZhou>0?"1":"0";
							sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
							paramMap.put("shifgzr", shifgzr);
							String starttime = getStartWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, lingj.getDiycqysj(), BigDecimal.ZERO, startDateByZhouCacheMap);
							shifgzr =null;//显示都是已整个工作周为
							paramMap.put("shifgzr", shifgzr);
							String endttime = getEndWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, lingj.getDiycqysj(), BigDecimal.ZERO, endDateByZhouCacheMap);

							zhouyaoh = new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsZhouqi), 5, BigDecimal.ROUND_HALF_DOWN)
									.multiply(baozgs).intValue());
							zhoubzgs = zhoubzgs.add(zhouyaoh);
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							calendar.setTime(sdf.parse(starttime));
							calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
							mx.setJiaofrq(sdf.format(calendar.getTime()));
							mx.setYaohqsrq(getStartWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, lingj.getDiycqysj(), BigDecimal.ZERO, startDateByZhouCacheMap));
							mx.setYaohjsrq(endttime);
							mx.setFayrq(mx.getYaohqsrq());
							mx.setShul(zhouyaoh);
							mx.setJissl(zhouyaoh);
							mx.setSign(sign);
							mxList.add(mx);
							paramMap.clear();
						}

						int yushu = baozgs.subtract(zhoubzgs).intValue();

						for (int x = szhous; x < (szhous + zhoushu); x++) {
							if (mxList.get(x).getSign() == 1 && yushu > 0) {
								BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
								mxList.get(x).setShul(shul);
								yushu--;
							}
						}
						
//						mxList = getOrdeyDetailListWeekCount(bean, lingjmc, gongsmc, neibyhzx, caozz, rilbc, loginuser, gcbh, zhoushu, szhous, gongZRsZhouqi, count, lingj, nianzq, paramMap, pisl, mxList);

					} else { //xss_启运日期大于P0日期_第一个既定
						// 动态获取
						methodName = Const.GETP + pi + "sl";
						// 获取到方法
						Method meth = cls.getMethod(methodName, new Class[] {});
						pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
						CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);
						// 零件数量按包装取整
						BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
						BigDecimal zhoubzgs = BigDecimal.ZERO;
						BigDecimal zhouyaoh = BigDecimal.ZERO;
						String shifgzr = "";
						int  sign = 0;
						// 周均分包装
						for (int i = 0; i < zhoushu; i++) {
							// 实例化订单明细
							Dingdmx mx = new Dingdmx();

							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
							mx.setGonghlx(Const.PP);
							mx.setActive(Const.ACTIVE_1);
							mx.setDanw(bean.getDanw());
							mx.setUabzlx(bean.getUabzlx());
							mx.setUabzuclx(bean.getUabzuclx());
							mx.setUabzucrl(bean.getUabzucrl());
							mx.setUabzucsl(bean.getUabzucsl());
							mx.setYugsfqz(bean.getYugsfqz());
							mx.setLujdm(bean.getLujdm());
							mx.setJihyz(bean.getJihyz());
							mx.setYijfl(bean.getYijfl());
							mx.setDingdh(bean.getDingdh());
							mx.setUsercenter(bean.getUsercenter());
							mx.setLingjbh(bean.getLingjbh());
							mx.setGongysdm(bean.getGongysdm());
//							mx.setGonghlx(bean.getGongyslx());
							mx.setCangkdm(bean.getCangkdm());
							mx.setFahd(bean.getFahd());
							mx.setDinghcj(bean.getDinghcj());
							mx.setCreate_time(CommonFun.getJavaTime());
							mx.setCreator(caozz);
							mx.setEdit_time(CommonFun.getJavaTime());
							mx.setEditor(caozz);
							mx.setZhuangt(Const.DINGD_STATUS_DSX);
//							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
//							mx.setGonghlx(Const.PP);
//							mx.setActive(Const.ACTIVE_1);
							mx.setLingjmc(lingjmc);
							mx.setGongsmc(gongsmc);
							mx.setNeibyhzx(neibyhzx);
							mx.setXiehzt(bean.getXiehzt());
							mx.setGcbh(gcbh);

							if (count == 1){//如果 只有一个既定，则第一个既定变为预告
								mx.setLeix(Const.SHIFOUSHIYUGAO); 
							}else{//双既定时，大于P0，P1则全部变为预告
								if ( diycqyzhouqx.compareTo(nianzq_P1) > 0 ) { 
									mx.setLeix(Const.SHIFOUSHIYUGAO); 
								}else{ 
									mx.setLeix(Const.SHIFOUSHIJIDING);
								}
							}
							 							

							paramMap.put("usercenter", Const.KDRILI);
							paramMap.put("nianzq", nianzq);
							paramMap.put("banc", rilbc);
							paramMap.put("zhoux", "0" + (i + 1));
							paramMap.put("shifgzr", "1");
							paramMap.put("riq", lingj.getDiycqysj());
							int gongZrZhou = getGzrByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), null, gzrByZhouCacheMap);
							shifgzr=gongZrZhou>0?"1":"0";
							sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
							paramMap.put("shifgzr", shifgzr);
							String starttime = getStartWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, null, BigDecimal.ZERO, startDateByZhouCacheMap);
							shifgzr =null;//显示都是已整个工作周为
							paramMap.put("shifgzr", shifgzr);
							String endttime = getEndWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, null, BigDecimal.ZERO, endDateByZhouCacheMap);

							if(starttime == null || endttime==null ){
								 continue;
							}
							
							zhouyaoh = new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsZhouqi), 5, BigDecimal.ROUND_HALF_DOWN)
									.multiply(baozgs).intValue());
							zhoubzgs = zhoubzgs.add(zhouyaoh);
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							calendar.setTime(sdf.parse(starttime));
							calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
							mx.setJiaofrq(sdf.format(calendar.getTime()));
							mx.setYaohqsrq(getStartWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, null, BigDecimal.ZERO, startDateByZhouCacheMap));
							mx.setYaohjsrq(endttime);
							mx.setFayrq(mx.getYaohqsrq());
							mx.setShul(zhouyaoh);
							mx.setJissl(zhouyaoh);
							mx.setSign(sign);
							mxList.add(mx);
							paramMap.clear();
						}

						int yushu = baozgs.subtract(zhoubzgs).intValue();

						for (int x = szhous; x < mxList.size(); x++) {
							if (mxList.get(x).getSign() == 1 && yushu > 0) {
								BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
								mxList.get(x).setShul(shul);
								yushu--;
							}
						}
					}
				}
				szhous += zhoushu;
			} else { 
				
				// 转换第一次启运时间为date类型
				if( lingj.getDiycqysj()!=null ){ 
					java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
					Date diycqyDate = df.parse(lingj.getDiycqysj());
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()）时diycqyDate=" + diycqyDate);
					java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyyMMdd");
					// 将第一次启运时间转为yyyyMMdd格式后截取年月
					String diycqyzhouqx = df2.format(diycqyDate).substring(0, 6);
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法在（pi==0且null != lingj.getDiycqysj()）时diycqyzhouqx=" + diycqyzhouqx);
				
					// 双既定时，第一次起运时间在第二个既定周期内，
					//启运日期大于P0周期，并且启运日期等于当前要货日期	
					if (diycqyzhouqx.compareTo(bean.getP0fyzqxh()) > 0  && diycqyzhouqx.compareTo(nianzq) == 0) {
						// 动态获取
						methodName = Const.GETP + pi + "sl";
						// 获取到方法
						Method meth = cls.getMethod(methodName, new Class[] {});
						pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
						CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);

						// 从起运日期开始的周数
						int qiyzhou = getZhouShu(Const.KDRILI, nianzq, rilbc,  lingj.getDiycqysj(), zhousCacheMap);
						// 从起运日期开始的工作日天数
						int gongZRsByZhouqiAndQiy = getGzrByZhouq(Const.KDRILI, nianzq, rilbc, lingj.getDiycqysj(), gzrByZhouqCacheMap);

						// 零件数量按包装取整
						BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
						BigDecimal zhoubzgs = BigDecimal.ZERO;
						BigDecimal zhouyaoh = BigDecimal.ZERO;
						Map<String, Object> resultMap = new HashMap<String, Object>();
						String shifgzr = "";
						int  sign =  0 ;
						// 周均分包装
						for (int i = zhoushu - qiyzhou; i < zhoushu; i++) {
							// 实例化订单明细
							Dingdmx mx = new Dingdmx();
	                        
							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
							mx.setGonghlx(Const.PP);
							mx.setActive(Const.ACTIVE_1);
							mx.setDanw(bean.getDanw());
							mx.setUabzlx(bean.getUabzlx());
							mx.setUabzuclx(bean.getUabzuclx());
							mx.setUabzucrl(bean.getUabzucrl());
							mx.setUabzucsl(bean.getUabzucsl());
							mx.setYugsfqz(bean.getYugsfqz());
							mx.setLujdm(bean.getLujdm());
							mx.setJihyz(bean.getJihyz());
							mx.setYijfl(bean.getYijfl());
							mx.setDingdh(bean.getDingdh());
							mx.setUsercenter(bean.getUsercenter());
							mx.setLingjbh(bean.getLingjbh());
							mx.setGongysdm(bean.getGongysdm());
//							mx.setGonghlx(bean.getGongyslx());
							mx.setCangkdm(bean.getCangkdm());
							mx.setFahd(bean.getFahd());
							mx.setDinghcj(bean.getDinghcj());
							mx.setCreate_time(CommonFun.getJavaTime());
							mx.setCreator(caozz);
							mx.setEdit_time(CommonFun.getJavaTime());
							mx.setEditor(caozz);
							mx.setZhuangt(Const.DINGD_STATUS_DSX);
//							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
//							mx.setGonghlx(Const.PP);
//							mx.setActive(Const.ACTIVE_1);
							mx.setLingjmc(lingjmc);
							mx.setGongsmc(gongsmc);
							mx.setNeibyhzx(neibyhzx);
							mx.setXiehzt(bean.getXiehzt());
							mx.setGcbh(gcbh);
							
							if(count == 1){
								mx.setLeix(Const.SHIFOUSHIJIDING); 				
							}else{
								mx.setLeix(Const.SHIFOUSHIYUGAO);	
							}
															 

							paramMap.put("usercenter", Const.KDRILI);
							paramMap.put("nianzq", nianzq);
							paramMap.put("banc", rilbc);
							paramMap.put("zhoux", "0" + (i + 1));
							paramMap.put("shifgzr", "1");
							paramMap.put("riq", lingj.getDiycqysj());
							int gongZrZhou = getGzrByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), lingj.getDiycqysj(), gzrByZhouCacheMap);
							shifgzr=gongZrZhou>0?"1":"0";
							sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
							
							paramMap.put("shifgzr", shifgzr);
							String starttime = getStartWeekDate(Const.KDRILI,nianzq,rilbc, "0" + (i + 1),shifgzr,lingj.getDiycqysj(),BigDecimal.ZERO,startDateByZhouCacheMap);
								
							shifgzr =null;//显示都是已整个工作周为
							paramMap.put("shifgzr", shifgzr);
							String endttime = getEndWeekDate(Const.KDRILI, nianzq, rilbc,  "0" + (i + 1), shifgzr, lingj.getDiycqysj(), BigDecimal.ZERO, endDateByZhouCacheMap);
							
							if(starttime == null || endttime==null ){
								 continue;
							}
							
							zhouyaoh = new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsByZhouqiAndQiy), 5, BigDecimal.ROUND_HALF_DOWN)
									.multiply(baozgs).intValue());
							zhoubzgs = zhoubzgs.add(zhouyaoh);
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							calendar.setTime(sdf.parse(starttime));
							calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
							mx.setJiaofrq(sdf.format(calendar.getTime()));
							mx.setYaohqsrq(getStartWeekDate(Const.KDRILI,nianzq,rilbc, "0" + (i + 1),shifgzr,lingj.getDiycqysj(),BigDecimal.ZERO,startDateByZhouCacheMap));
							mx.setFayrq(starttime);
							mx.setYaohjsrq(endttime);
							mx.setShul(zhouyaoh);
							mx.setJissl(zhouyaoh);
							mx.setSign(sign);
							mxList.add(mx);
							paramMap.clear();
						}

						int yushu = baozgs.subtract(zhoubzgs).intValue();

						for (int x = szhous; x < mxList.size(); x++) {
							if (mxList.get(x).getSign() == 1 && yushu > 0) {
								BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
								mxList.get(x).setShul(shul);
								yushu--;
							}
						}

//						mxList = getOrdeyDetailListWeekCount(bean, lingjmc, gongsmc, neibyhzx, caozz, rilbc, loginuser, gcbh, zhoushu, szhous, gongZRsZhouqi, count, lingj, nianzq, paramMap, pisl, mxList);
						
						// 起运周期之前的周不参与均分
						for (int i = 0; i < zhoushu - qiyzhou; i++) {
							// 实例化订单明细
							Dingdmx mx = new Dingdmx();

							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
							mx.setGonghlx(Const.PP);
							mx.setActive(Const.ACTIVE_1);
							mx.setDanw(bean.getDanw());
							mx.setUabzlx(bean.getUabzlx());
							mx.setUabzuclx(bean.getUabzuclx());
							mx.setUabzucrl(bean.getUabzucrl());
							mx.setUabzucsl(bean.getUabzucsl());
							mx.setYugsfqz(bean.getYugsfqz());
							mx.setLujdm(bean.getLujdm());
							mx.setJihyz(bean.getJihyz());
							mx.setYijfl(bean.getYijfl());
							mx.setDingdh(bean.getDingdh());
							mx.setUsercenter(bean.getUsercenter());
							mx.setLingjbh(bean.getLingjbh());
							mx.setGongysdm(bean.getGongysdm());
//							mx.setGonghlx(bean.getGongyslx());
							mx.setCangkdm(bean.getCangkdm());
							mx.setFahd(bean.getFahd());
							mx.setDinghcj(bean.getDinghcj());
							mx.setCreate_time(CommonFun.getJavaTime());
							mx.setCreator(caozz);
							mx.setEdit_time(CommonFun.getJavaTime());
							mx.setEditor(caozz);
							mx.setZhuangt(Const.DINGD_STATUS_DSX);
//							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
//							mx.setGonghlx(Const.PP);
//							mx.setActive(Const.ACTIVE_1);
							mx.setLingjmc(lingjmc);
							mx.setGongsmc(gongsmc);
							mx.setNeibyhzx(neibyhzx);
							mx.setXiehzt(bean.getXiehzt());
							mx.setGcbh(gcbh);
							
							if(count == 1){
								mx.setLeix(Const.SHIFOUSHIJIDING); 				
							}else{
								mx.setLeix(Const.SHIFOUSHIYUGAO);	
							}

							paramMap.put("usercenter", Const.KDRILI);
							paramMap.put("nianzq", nianzq);
							paramMap.put("banc", rilbc);
							paramMap.put("zhoux", "0" + (i + 1));
							String starttime = getStartWeekDate( Const.KDRILI, nianzq, rilbc, "0" + (i + 1), BigDecimal.ZERO, startDateByZhouCacheMap);
							
							String endttime = getEndWeekDate( Const.KDRILI, nianzq, rilbc, "0" + (i + 1), BigDecimal.ZERO, endDateByZhouCacheMap);
							
							if(starttime == null || endttime==null ){
								 continue;
							}
							
							mx.setYaohqsrq(starttime);
							mx.setYaohjsrq(endttime);
							mx.setShul(BigDecimal.ZERO);
							mx.setJissl(BigDecimal.ZERO);
							mx.setFayrq(mx.getYaohqsrq());
							mx.setJiaofrq(starttime);
							mx.setSign(0);
							mxList.add(mx);
							paramMap.clear();
						}
					}else if (diycqyzhouqx.compareTo(bean.getP0fyzqxh()) > 0  && diycqyzhouqx.compareTo(nianzq) > 0) {
					//启运日期大于P0周期，并且启运日期大于P1要货日期	
						// 动态获取
						methodName = Const.GETP + pi + "sl";
						// 获取到方法
						Method meth = cls.getMethod(methodName, new Class[] {});
						pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
						CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);
//						// 零件数量按包装取整
						BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
						BigDecimal zhoubzgs = BigDecimal.ZERO;
						BigDecimal zhouyaoh = BigDecimal.ZERO;
						String shifgzr = "";
						int sign = 0;
						// 周均分包装
						for (int i = 0; i < zhoushu; i++) {
							// 实例化订单明细
							Dingdmx mx = new Dingdmx();

							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
							mx.setGonghlx(Const.PP);
							mx.setActive(Const.ACTIVE_1);
							mx.setDanw(bean.getDanw());
							mx.setUabzlx(bean.getUabzlx());
							mx.setUabzuclx(bean.getUabzuclx());
							mx.setUabzucrl(bean.getUabzucrl());
							mx.setUabzucsl(bean.getUabzucsl());
							mx.setYugsfqz(bean.getYugsfqz());
							mx.setLujdm(bean.getLujdm());
							mx.setJihyz(bean.getJihyz());
							mx.setYijfl(bean.getYijfl());
							mx.setDingdh(bean.getDingdh());
							mx.setUsercenter(bean.getUsercenter());
							mx.setLingjbh(bean.getLingjbh());
							mx.setGongysdm(bean.getGongysdm());
//							mx.setGonghlx(bean.getGongyslx());
							mx.setCangkdm(bean.getCangkdm());
							mx.setFahd(bean.getFahd());
							mx.setDinghcj(bean.getDinghcj());
							mx.setCreate_time(CommonFun.getJavaTime());
							mx.setCreator(caozz);
							mx.setEdit_time(CommonFun.getJavaTime());
							mx.setEditor(caozz);
							mx.setZhuangt(Const.DINGD_STATUS_DSX);
//							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
//							mx.setGonghlx(Const.PP);
//							mx.setActive(Const.ACTIVE_1);
							mx.setLingjmc(lingjmc);
							mx.setGongsmc(gongsmc);
							mx.setNeibyhzx(neibyhzx);
							mx.setXiehzt(bean.getXiehzt());
							mx.setGcbh(gcbh);
							
							//xss_0013028_启运日期大于P1周期，则全部变为预告
							mx.setLeix(Const.SHIFOUSHIYUGAO);	
							

							paramMap.put("usercenter", Const.KDRILI);
							paramMap.put("nianzq", nianzq);
							paramMap.put("banc", rilbc);
							paramMap.put("zhoux", "0" + (i + 1));
							paramMap.put("shifgzr", "1");
							paramMap.put("riq", lingj.getDiycqysj());
							int gongZrZhou = getGzrByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), null, gzrByZhouCacheMap);
							shifgzr =gongZrZhou>0?"1":"0";//显示都是已整个工作周为
							sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
							paramMap.put("shifgzr", shifgzr);
							String starttime = getStartWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, null, BigDecimal.ZERO, startDateByZhouCacheMap);
							shifgzr =null;//显示都是已整个工作周为
							paramMap.put("shifgzr", shifgzr);
							String endttime = getEndWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, null, BigDecimal.ZERO, endDateByZhouCacheMap);
							
							if(starttime == null || endttime==null ){
								 continue;
							}
							
							zhouyaoh =new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsZhouqi), 5, BigDecimal.ROUND_HALF_DOWN).multiply(baozgs)
									.intValue());
							zhoubzgs = zhoubzgs.add(zhouyaoh);
							mx.setYaohqsrq(getStartWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, null, BigDecimal.ZERO, startDateByZhouCacheMap));
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							calendar.setTime(sdf.parse(starttime));
							calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
							mx.setJiaofrq(sdf.format(calendar.getTime()));
							mx.setYaohjsrq(endttime);
							mx.setFayrq(mx.getYaohqsrq());
							mx.setShul(zhouyaoh);
							mx.setJissl(zhouyaoh);
							mx.setSign(sign);
							mxList.add(mx);
							paramMap.clear();
						}

						int yushu = baozgs.subtract(zhoubzgs).intValue();

						for (int x = szhous; x < mxList.size(); x++) {
							if (mxList.get(x).getSign() == 1 && yushu > 0) {
								BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
								mxList.get(x).setShul(shul);
								yushu--;
							}
						}
						szhous += zhoushu; 
					}else{
						// 动态获取
						methodName = Const.GETP + pi + "sl";
						// 获取到方法
						Method meth = cls.getMethod(methodName, new Class[] {});
						pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
						CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);
//						// 零件数量按包装取整
						BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
						BigDecimal zhoubzgs = BigDecimal.ZERO;
						BigDecimal zhouyaoh = BigDecimal.ZERO;
						String shifgzr = "";
						int sign = 0;
						// 周均分包装
						for (int i = 0; i < zhoushu; i++) {
							// 实例化订单明细
							Dingdmx mx = new Dingdmx();

							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
							mx.setGonghlx(Const.PP);
							mx.setActive(Const.ACTIVE_1);
							mx.setDanw(bean.getDanw());
							mx.setUabzlx(bean.getUabzlx());
							mx.setUabzuclx(bean.getUabzuclx());
							mx.setUabzucrl(bean.getUabzucrl());
							mx.setUabzucsl(bean.getUabzucsl());
							mx.setYugsfqz(bean.getYugsfqz());
							mx.setLujdm(bean.getLujdm());
							mx.setJihyz(bean.getJihyz());
							mx.setYijfl(bean.getYijfl());
							mx.setDingdh(bean.getDingdh());
							mx.setUsercenter(bean.getUsercenter());
							mx.setLingjbh(bean.getLingjbh());
							mx.setGongysdm(bean.getGongysdm());
//							mx.setGonghlx(bean.getGongyslx());
							mx.setCangkdm(bean.getCangkdm());
							mx.setFahd(bean.getFahd());
							mx.setDinghcj(bean.getDinghcj());
							mx.setCreate_time(CommonFun.getJavaTime());
							mx.setCreator(caozz);
							mx.setEdit_time(CommonFun.getJavaTime());
							mx.setEditor(caozz);
							mx.setZhuangt(Const.DINGD_STATUS_DSX);
//							mx.setZhidgys(bean.getZhidgys());
							mx.setZuixqdl(bean.getZuixqdl());
//							mx.setGonghlx(Const.PP);
//							mx.setActive(Const.ACTIVE_1);
							mx.setLingjmc(lingjmc);
							mx.setGongsmc(gongsmc);
							mx.setNeibyhzx(neibyhzx);
							mx.setXiehzt(bean.getXiehzt());
							mx.setGcbh(gcbh);

							if (count > 0) {
								mx.setLeix(Const.SHIFOUSHIJIDING);
							} else {
								mx.setLeix(Const.SHIFOUSHIYUGAO);
							}

							paramMap.put("usercenter", Const.KDRILI);
							paramMap.put("nianzq", nianzq);
							paramMap.put("banc", rilbc);
							paramMap.put("zhoux", "0" + (i + 1));
							paramMap.put("shifgzr", "1");
							paramMap.put("riq", lingj.getDiycqysj());
							int gongZrZhou = getGzrByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), lingj.getDiycqysj(), gzrByZhouCacheMap);
							shifgzr =gongZrZhou>0?"1":"0";//显示都是已整个工作周为
							sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
							paramMap.put("shifgzr", shifgzr);
							String starttime = getStartWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, lingj.getDiycqysj(), BigDecimal.ZERO, startDateByZhouCacheMap);
							shifgzr =null;//显示都是已整个工作周为
							paramMap.put("shifgzr", shifgzr);
							String endttime = getEndWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, lingj.getDiycqysj(), BigDecimal.ZERO, endDateByZhouCacheMap);
							zhouyaoh =new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsZhouqi), 5, BigDecimal.ROUND_HALF_DOWN).multiply(baozgs)
									.intValue());
							zhoubzgs = zhoubzgs.add(zhouyaoh);
							mx.setYaohqsrq(getStartWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, lingj.getDiycqysj(), BigDecimal.ZERO, startDateByZhouCacheMap));
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							calendar.setTime(sdf.parse(starttime));
							calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
							mx.setJiaofrq(sdf.format(calendar.getTime()));
							mx.setYaohjsrq(endttime);
							mx.setFayrq(mx.getYaohqsrq());
							mx.setShul(zhouyaoh);
							mx.setJissl(zhouyaoh);
							mx.setSign(sign);
							mxList.add(mx);
							paramMap.clear();
						}

						int yushu = baozgs.subtract(zhoubzgs).intValue();

						for (int x = szhous; x < (szhous + zhoushu); x++) {
							if (mxList.get(x).getSign() == 1 && yushu > 0) {
								BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
								mxList.get(x).setShul(shul);
								yushu--;
							}
						}
//						mxList = getOrdeyDetailListWeekCount(bean, lingjmc, gongsmc, neibyhzx, caozz, rilbc, loginuser, gcbh, zhoushu, szhous, gongZRsZhouqi, count, lingj, nianzq, paramMap, pisl, mxList);
						szhous += zhoushu;
						
					}  
				}else{

					// 动态获取
					methodName = Const.GETP + pi + "sl";
					// 获取到方法
					Method meth = cls.getMethod(methodName, new Class[] {});
					pisl = (BigDecimal) meth.invoke(bean) == null ? BigDecimal.ZERO : (BigDecimal) meth.invoke(bean);
					CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法pisl=" + pisl);
//					// 零件数量按包装取整
					BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
					BigDecimal zhoubzgs = BigDecimal.ZERO;
					BigDecimal zhouyaoh = BigDecimal.ZERO;
					String shifgzr = "";
					int sign = 0;
					// 周均分包装
					for (int i = 0; i < zhoushu; i++) {
						// 实例化订单明细
						Dingdmx mx = new Dingdmx();

						mx.setZhidgys(bean.getZhidgys());
						mx.setZuixqdl(bean.getZuixqdl());
						mx.setGonghlx(Const.PP);
						mx.setActive(Const.ACTIVE_1);
						mx.setDanw(bean.getDanw());
						mx.setUabzlx(bean.getUabzlx());
						mx.setUabzuclx(bean.getUabzuclx());
						mx.setUabzucrl(bean.getUabzucrl());
						mx.setUabzucsl(bean.getUabzucsl());
						mx.setYugsfqz(bean.getYugsfqz());
						mx.setLujdm(bean.getLujdm());
						mx.setJihyz(bean.getJihyz());
						mx.setYijfl(bean.getYijfl());
						mx.setDingdh(bean.getDingdh());
						mx.setUsercenter(bean.getUsercenter());
						mx.setLingjbh(bean.getLingjbh());
						mx.setGongysdm(bean.getGongysdm());
//						mx.setGonghlx(bean.getGongyslx());
						mx.setCangkdm(bean.getCangkdm());
						mx.setFahd(bean.getFahd());
						mx.setDinghcj(bean.getDinghcj());
						mx.setCreate_time(CommonFun.getJavaTime());
						mx.setCreator(caozz);
						mx.setEdit_time(CommonFun.getJavaTime());
						mx.setEditor(caozz);
						mx.setZhuangt(Const.DINGD_STATUS_DSX);
//						mx.setZhidgys(bean.getZhidgys());
						mx.setZuixqdl(bean.getZuixqdl());
//						mx.setGonghlx(Const.PP);
//						mx.setActive(Const.ACTIVE_1);
						mx.setLingjmc(lingjmc);
						mx.setGongsmc(gongsmc);
						mx.setNeibyhzx(neibyhzx);
						mx.setXiehzt(bean.getXiehzt());
						mx.setGcbh(gcbh);

						if (count > 0) {
							mx.setLeix(Const.SHIFOUSHIJIDING);
						} else {
							mx.setLeix(Const.SHIFOUSHIYUGAO);
						}

						paramMap.put("usercenter", Const.KDRILI);
						paramMap.put("nianzq", nianzq);
						paramMap.put("banc", rilbc);
						paramMap.put("zhoux", "0" + (i + 1));
						paramMap.put("shifgzr", "1");
						paramMap.put("riq", lingj.getDiycqysj());
						int gongZrZhou = getGzrByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), lingj.getDiycqysj(), gzrByZhouCacheMap);
						shifgzr =gongZrZhou>0?"1":"0";//显示都是已整个工作周为
						sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
						paramMap.put("shifgzr", shifgzr);
						String starttime = getStartWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, lingj.getDiycqysj(), BigDecimal.ZERO, startDateByZhouCacheMap);
						shifgzr =null;//显示都是已整个工作周为
						paramMap.put("shifgzr", shifgzr);
						String endttime = getEndWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, lingj.getDiycqysj(), BigDecimal.ZERO, endDateByZhouCacheMap);
						zhouyaoh =new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsZhouqi), 5, BigDecimal.ROUND_HALF_DOWN).multiply(baozgs)
								.intValue());
						zhoubzgs = zhoubzgs.add(zhouyaoh);
						mx.setYaohqsrq(getStartWeekDate(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), shifgzr, lingj.getDiycqysj(), BigDecimal.ZERO, startDateByZhouCacheMap));
						Calendar calendar = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						calendar.setTime(sdf.parse(starttime));
						calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
						mx.setJiaofrq(sdf.format(calendar.getTime()));
						mx.setYaohjsrq(endttime);
						mx.setFayrq(mx.getYaohqsrq());
						mx.setShul(zhouyaoh);
						mx.setJissl(zhouyaoh);
						mx.setSign(sign);
						mxList.add(mx);
						paramMap.clear();
					}

					int yushu = baozgs.subtract(zhoubzgs).intValue();

					for (int x = szhous; x < (szhous + zhoushu); x++) {
						if (mxList.get(x).getSign() == 1 && yushu > 0) {
							BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
							mxList.get(x).setShul(shul);
							yushu--;
						}
					}
//					mxList = getOrdeyDetailListWeekCount(bean, lingjmc, gongsmc, neibyhzx, caozz, rilbc, loginuser, gcbh, zhoushu, szhous, gongZRsZhouqi, count, lingj, nianzq, paramMap, pisl, mxList);
					szhous += zhoushu;
					
				}
			}

			count = count - 1;
			CommonFun.logger.debug("kd订单周要货量计算weekOrderCount方法count=" + count);
		}

		List<Dingdmx> newMxList = new ArrayList<Dingdmx>();
		for (Dingdmx dingdmx : mxList) {
			BigDecimal shul = BigDecimal.ZERO;
			if (!dingdmx.getShul().equals(null) || !dingdmx.getShul().equals(0)) {
				shul = dingdmx.getShul().multiply(bean.getUabzucrl().multiply(bean.getUabzucsl()));
			}
			dingdmx.setShul(shul);
			dingdmx.setJissl(shul);
			newMxList.add(dingdmx);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ilorder.insertDingdmx", newMxList);

		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "订单计算", "周要货量计算结束");
	}
	
///////////////////////////////////wuyichao//////////////////////////////////////////////	
	
	
	
	
	


	private List<Dingdmx> getOrdeyDetailListWeekCount(Dingdlj bean,String lingjmc, String gongsmc, String neibyhzx, String caozz, String rilbc,
			LoginUser loginuser, String gcbh,int zhoushu,int szhous,int gongZRsZhouqi,int count,Lingj lingj,String nianzq,Map<String,Object> paramMap,BigDecimal pisl,List<Dingdmx> mxList) throws ParseException{
		
		// 零件数量按包装取整
		BigDecimal baozgs = pisl.divide(bean.getUabzucrl().multiply(bean.getUabzucsl()));
		BigDecimal zhoubzgs = BigDecimal.ZERO;
		BigDecimal zhouyaoh = BigDecimal.ZERO;
		String shifgzr = "";
		int sign = 0;
		// 周均分包装
		for (int i = 0; i < zhoushu; i++) {
			// 实例化订单明细
			Dingdmx mx = new Dingdmx();

			mx.setZhidgys(bean.getZhidgys());
			mx.setZuixqdl(bean.getZuixqdl());
			mx.setGonghlx(Const.PP);
			mx.setActive(Const.ACTIVE_1);
			mx.setDanw(bean.getDanw());
			mx.setUabzlx(bean.getUabzlx());
			mx.setUabzuclx(bean.getUabzuclx());
			mx.setUabzucrl(bean.getUabzucrl());
			mx.setUabzucsl(bean.getUabzucsl());
			mx.setYugsfqz(bean.getYugsfqz());
			mx.setLujdm(bean.getLujdm());
			mx.setJihyz(bean.getJihyz());
			mx.setYijfl(bean.getYijfl());
			mx.setDingdh(bean.getDingdh());
			mx.setUsercenter(bean.getUsercenter());
			mx.setLingjbh(bean.getLingjbh());
			mx.setGongysdm(bean.getGongysdm());
//			mx.setGonghlx(bean.getGongyslx());
			mx.setCangkdm(bean.getCangkdm());
			mx.setFahd(bean.getFahd());
			mx.setDinghcj(bean.getDinghcj());
			mx.setCreate_time(CommonFun.getJavaTime());
			mx.setCreator(caozz);
			mx.setEdit_time(CommonFun.getJavaTime());
			mx.setEditor(caozz);
			mx.setZhuangt(Const.DINGD_STATUS_DSX);
//			mx.setZhidgys(bean.getZhidgys());
			mx.setZuixqdl(bean.getZuixqdl());
//			mx.setGonghlx(Const.PP);
//			mx.setActive(Const.ACTIVE_1);
			mx.setLingjmc(lingjmc);
			mx.setGongsmc(gongsmc);
			mx.setNeibyhzx(neibyhzx);
			mx.setXiehzt(bean.getXiehzt());
			mx.setGcbh(gcbh);

			if (count > 0) {
				mx.setLeix(Const.SHIFOUSHIJIDING);
			} else {
				mx.setLeix(Const.SHIFOUSHIYUGAO);
			}

			paramMap.put("usercenter", Const.KDRILI);
			paramMap.put("nianzq", nianzq);
			paramMap.put("banc", rilbc);
			paramMap.put("zhoux", "0" + (i + 1));
			paramMap.put("shifgzr", "1");
			paramMap.put("riq", lingj.getDiycqysj());
			int gongZrZhou = this.calendarversionservice.getGongZRsByZhou(Const.KDRILI, nianzq, rilbc, "0" + (i + 1), lingj.getDiycqysj());
			shifgzr =gongZrZhou>0?"1":"0";//显示都是已整个工作周为
			sign =gongZrZhou>0?1:0;//标识是否参与均分  1参与均分 0不参与均分
			paramMap.put("shifgzr", shifgzr);
			String starttime = this.getAfterLineStartWeekDate(paramMap, BigDecimal.ZERO);
			shifgzr =null;//显示都是已整个工作周为
			paramMap.put("shifgzr", shifgzr);
			String endttime = this.getAfterLineEndWeekDate(paramMap, BigDecimal.ZERO);
			zhouyaoh =new BigDecimal(new BigDecimal(gongZrZhou).divide(new BigDecimal(gongZRsZhouqi), 5, BigDecimal.ROUND_HALF_DOWN).multiply(baozgs)
					.intValue());
			zhoubzgs = zhoubzgs.add(zhouyaoh);
			mx.setYaohqsrq(this.getAfterLineStartWeekDate(paramMap, BigDecimal.ZERO));
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			calendar.setTime(sdf.parse(starttime));
			calendar.add(Calendar.DAY_OF_YEAR,bean.getFayzq().intValue());
			mx.setJiaofrq(sdf.format(calendar.getTime()));
			mx.setYaohjsrq(endttime);
			mx.setFayrq(mx.getYaohqsrq());
			mx.setShul(zhouyaoh);
			mx.setJissl(zhouyaoh);
			mx.setSign(sign);
			mxList.add(mx);
			paramMap.clear();
		}

		int yushu = baozgs.subtract(zhoubzgs).intValue();

		for (int x = szhous; x < (szhous + zhoushu); x++) {
			if (mxList.get(x).getSign() == 1 && yushu > 0) {
				BigDecimal shul = mxList.get(x).getShul().add(new BigDecimal(1));
				mxList.get(x).setShul(shul);
				yushu--;
			}
		}
		
		return mxList;
	
	}
	
	
	
	
	
////////////////////////////////wuyichao ///////////////////////
	/**
	 * @see   缓存查询出来的周数   根据查询条件为key
	 */
	public int getZhouShu(String usercenter, String nianzq,String rilbc,Map<String,Integer> zhouShuMap)
	{
		String flagKey = usercenter + nianzq + rilbc;
		Integer zhouShu = zhouShuMap.get(flagKey);
		if(null == zhouShu)
		{
			zhouShu = this.calendarversionservice.getZhouShu(usercenter, nianzq, rilbc, null);
			if(null != zhouShu)
			{
				zhouShuMap.put(flagKey, zhouShu);
			}
		}
		return zhouShu;
	}
	
	public int getZhouShu(String usercenter, String nianzq,String rilbc, String riq,Map<String,Integer> zhouShuMap)
	{
		String flagKey = usercenter + nianzq + rilbc + riq;
		Integer zhouShu = zhouShuMap.get(flagKey);
		if(null == zhouShu)
		{
			zhouShu = this.calendarversionservice.getZhouShu(usercenter, nianzq, rilbc, riq);
			if(null != zhouShu)
			{
				zhouShuMap.put(flagKey, zhouShu);
			}
		}
		return zhouShu;
	}

	private int getGzrByZhouq(String usercenter, String nianzq, String rilbc,Map<String, Integer> gzrByZhouqCacheMap) {
		String flagKey = usercenter + nianzq + rilbc;
		Integer zhouShu = gzrByZhouqCacheMap.get(flagKey);
		if(null == zhouShu)
		{
			zhouShu = this.calendarversionservice.getGongZRsByZhouqi(usercenter, nianzq, rilbc, null);
			if(null != zhouShu)
			{
				gzrByZhouqCacheMap.put(flagKey, zhouShu);
			}
		}
		return zhouShu;
		
	}
	
	
	private int getGzrByZhouq(String usercenter, String nianzq, String rilbc, String riq,Map<String, Integer> gzrByZhouqCacheMap) {
		String flagKey = usercenter + nianzq + rilbc + riq;
		Integer zhouShu = gzrByZhouqCacheMap.get(flagKey);
		if(null == zhouShu)
		{
			zhouShu = this.calendarversionservice.getGongZRsByZhouqi(usercenter, nianzq, rilbc, riq);
			if(null != zhouShu)
			{
				gzrByZhouqCacheMap.put(flagKey, zhouShu);
			}
		}
		return zhouShu;
		
	}
	
	private int getGzrByZhou(String usercenter, String nianzq, String rilbc , String zhoux , String riq ,Map<String, Integer> gzrByZhouCacheMap) {
		String flagKey = usercenter + nianzq + rilbc + zhoux + riq;
		Integer zhouShu = gzrByZhouCacheMap.get(flagKey);
		if(null == zhouShu)
		{
			zhouShu = this.calendarversionservice.getGongZRsByZhou(usercenter, nianzq, rilbc, zhoux, riq);
			if(null != zhouShu)
			{
				gzrByZhouCacheMap.put(flagKey, zhouShu);
			}
		}
		return zhouShu;
		
	}
	
	

	private String getStartWeekDate(String usercenter, String nianzq, String rilbc,
			String zhoux, String shifgzr, String riq, BigDecimal fayzq,
			Map<String, String> startDateByZhouCacheMap) throws ParseException {
		Map<String ,Object> paramMap = new HashMap<String, Object>();
		
		String flagKey = usercenter + nianzq + rilbc + zhoux + riq;
		if(StringUtils.isNotBlank(shifgzr))
		{
			flagKey += shifgzr;
		}
		String result = startDateByZhouCacheMap.get(flagKey);
		if(StringUtils.isBlank(result))
		{
			paramMap.put("usercenter", usercenter);
			paramMap.put("nianzq", nianzq);
			paramMap.put("banc", rilbc);
			paramMap.put("zhoux", zhoux);
			paramMap.put("shifgzr", shifgzr);
			paramMap.put("riq", riq);
			result = this.getAfterLineStartWeekDate(paramMap,fayzq);
			if(StringUtils.isNotBlank(result))
			{
				startDateByZhouCacheMap.put(flagKey, result);
			}
		}
		return result;
	}
	
	

	private String getStartWeekDate(String usercenter, String nianzq, String rilbc,
			String zhoux,BigDecimal fayzq,Map<String, String> startDateByZhouCacheMap) throws ParseException {
		Map<String ,Object> paramMap = new HashMap<String, Object>();
		
		String flagKey = usercenter + nianzq + rilbc + zhoux ;
		
		String result = startDateByZhouCacheMap.get(flagKey);
		if(StringUtils.isBlank(result))
		{
			paramMap.put("usercenter", usercenter);
			paramMap.put("nianzq", nianzq);
			paramMap.put("banc", rilbc);
			paramMap.put("zhoux", zhoux);
			result = this.getAfterLineStartWeekDate(paramMap,fayzq);
			if(StringUtils.isNotBlank(result))
			{
				startDateByZhouCacheMap.put(flagKey, result);
			}
		}
		return result;
	}
	
	private String getEndWeekDate(String usercenter, String nianzq, String rilbc,
			String zhoux, String shifgzr, String riq, BigDecimal fayzq,
			Map<String, String> endDateByZhouCacheMap) throws ParseException {
		Map<String ,Object> paramMap = new HashMap<String, Object>();
		
		String flagKey = usercenter + nianzq + rilbc + zhoux + riq;
		if(StringUtils.isNotBlank(shifgzr))
		{
			flagKey += shifgzr;
		}
		String result = endDateByZhouCacheMap.get(flagKey);
		if(StringUtils.isBlank(result))
		{
			paramMap.put("usercenter", usercenter);
			paramMap.put("nianzq", nianzq);
			paramMap.put("banc", rilbc);
			paramMap.put("zhoux", zhoux);
			paramMap.put("shifgzr", shifgzr);
			paramMap.put("riq", riq);
			result = this.getAfterLineEndWeekDate(paramMap,fayzq);
			if(StringUtils.isNotBlank(result))
			{
				endDateByZhouCacheMap.put(flagKey, result);
			}
		}
		return result;
	}
	
	
	private String getEndWeekDate(String usercenter, String nianzq, String rilbc,
			String zhoux, BigDecimal fayzq,Map<String, String> endDateByZhouCacheMap) throws ParseException {
		Map<String ,Object> paramMap = new HashMap<String, Object>();
		
		String flagKey = usercenter + nianzq + rilbc + zhoux ;
		
		String result = endDateByZhouCacheMap.get(flagKey);
		if(StringUtils.isBlank(result))
		{
			paramMap.put("usercenter", usercenter);
			paramMap.put("nianzq", nianzq);
			paramMap.put("banc", rilbc);
			paramMap.put("zhoux", zhoux);
			result = this.getAfterLineEndWeekDate(paramMap,fayzq);
			if(StringUtils.isNotBlank(result))
			{
				endDateByZhouCacheMap.put(flagKey, result);
			}
		}
		return result;
	}
	////////////////////////////////wuyichao ///////////////////////
	
	
	
	//KD包装分配规则表
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<Integer,Kdbzfpgz> queryKdbzfpgz(){
	    Map map =new HashMap();
		List<Kdbzfpgz> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.queryKdbzfpgz");
		for(Kdbzfpgz bean:list){
			map.put(bean.getShengybzgs(), bean);
		}
		return map;
	}
}