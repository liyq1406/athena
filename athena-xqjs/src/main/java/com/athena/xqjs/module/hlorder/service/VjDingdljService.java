package com.athena.xqjs.module.hlorder.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ilorder.service.IlYaohlService;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * <p>
 * Title:订单零件类
 * </p>
 * <p>
 * Description:订单零件类
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
 * @date 2011-12-09
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class VjDingdljService extends BaseService {
	@Inject
	private IlYaohlService ilYaohlService;

	/**
	 * 插如操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：Dingdlj bean 订单零件实体
	 */
	public boolean doInsert(Dingdlj bean) {
		if (null == bean.getDingdh()) {
			return false;
		}

		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS)
				.execute("hlorder.insertDingdlj", bean);
		return count > 0;

	}

	/**
	 * 删除操作，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：Dingdlj bean 订单零件实体
	 */
	public void doDelete(Dingdlj bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.deleteDingdlj", bean);
	}



	public boolean doUpdateForKd(Object obj) {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS)
				.execute("hlorder.updateDingdljPart", obj);
		return count > 0;
	}

	/**
	 * 查询订单零件实体
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09
	 * @参数说明：map （用户中心，订单号，零件编号，供应商代码,id,用户中心）
	 */
	public Dingdlj queryDingljObject(Map<String, String> map) {
		return (Dingdlj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("hlorder.queryAllDingdlj", map);
	}

	
	/**
	 * 查询全部，返回list
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09
	 * 
	 */
	public List<Dingdlj> queryAllDingdlj() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryAllDingdlj");
	}

	/**
	 * 根据条件查询订单零件
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09
	 * 
	 */
	public List<Dingdlj> queryAllDingdlj(Map<String, String> map) {
		CommonFun.mapPrint(map, "根据条件查询订单零件queryAllDingdlj方法参数map");
		CommonFun.logger
				.debug("根据条件查询订单零件queryAllDingdlj方法的sql语句为：ilorder.queryAllDingdlj");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryAllDingdlj", map);
	}

	/**
	 * 查询全部，返回list
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09
	 * 
	 */
	public List<Dingdlj> queryAllDingdljForList() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryAllDingdljlist");
	}

	/**
	 * 查询全部，返回map -------未测试
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09
	 * 
	 */
	public Map queryAllDingdljForMap(Pageable page, Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("hlorder.queryAllDingdlj", map, page);
	}

	public List queryAllDingdljForBean(Dingdlj bean) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryAllDingdlj", bean);
	}

	/**
	 * 查询全部，返回map -------未测试
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09
	 * 
	 */
	public Map queryAllKDDingdljForMap(Pageable page, Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("hlorder.queryAllKDDingdlj", map, page);
	}

	/**
	 * 002681 查询全部，返回List
	 * 
	 * @author synie
	 * @version v1.0
	 * @date 2012-07-24
	 * 
	 */
	public List<Dingdlj> queryKDDingdljForList(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryAllKDDingdlj", map);
	}

	/**
	 * 查询全部，返回list -------未测试
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-07-04
	 * 
	 */
	public List<Dingdlj> queryAllDingdljList(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryAllDingdlj", map);
	}

	public List<Dingdlj> queryEXPDingdljList(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryEXPDingdlj", map);
	}

	/**
	 * kd件订单导出
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2012-07-04
	 * 
	 */
	public List<Dingdlj> queryEXPKDDingdljList(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryEXPKDDingdlj", map);
	}

	/**
	 * 根据订单号查询实体
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date: 2011-12-09
	 * @参数：String dingdh,String fahzq
	 */
	public Dingd queryDingdByDingdh(Map<String, String> map) {		
		return (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("hlorder.queryDingdByDingdh", map);
	}

	/**
	 * 预告中间表和订单零件表的行列装换，条件查询，返回list集合,p,j,s模式， -------未测试
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09
	 * @参数：p0sl,p1sl,p2sl,p3sl，订单制作时间，p0发运周期
	 */
	private String getParrm(String ids, String parrten) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Const.PP, ids + "queryDingdljConverLineP");
		map.put(Const.PJ, ids + "queryDingdljConverLineJ");
		map.put(Const.PS, ids + "queryDingdljConverLineS");
		return map.get(parrten.toUpperCase());
	}

	public List<Dingdlj> queryDingdljListPJS(BigDecimal p0sl, BigDecimal p1sl,
			BigDecimal p2sl, BigDecimal p3sl, String dingdzzsj,
			String p0fyzqxh, String parrten) {
		String ids = "hlorder.";
		Map map = new HashMap();
		map.put("p0sl", p0sl);
		map.put("p1sl", p1sl);
		map.put("p2sl", p2sl);
		map.put("p3sl", p3sl);
		map.put("dingdzzsj", dingdzzsj);
		map.put("p0fyzqxh", p0fyzqxh);
		String parrm = this.getParrm(ids, parrten);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(parrm, map);
	}

	/**
	 * 获取订单内容中既定的个数
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-1-4
	 */
	public int getDingdnr(String dingdnr) {
		// 定义循环的次数
		int count = 0;
		// 转换为字符数组
		char[] array = dingdnr.toCharArray();
		CommonFun.logger.debug("获取订单内容中既定的个数getDingdnr方法array="+ array.toString());
		// 循环得到既定数量值
		for (int j = 0; j < array.length; j++) {
			if (array[j] == 57) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 取模得到最后的计算结果
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-2-2
	 * @参数：要货数量，模值
	 */
	public Map<Integer, BigDecimal> getModeValue(BigDecimal yaohl, int mode,
			int zhous) {
		Map<Integer, BigDecimal> map = new TreeMap<Integer, BigDecimal>();
		int j = 0;
		if (mode > 0.1) {
			for (int i = mode; i > 0; i--) {
				map.put(j, yaohl.add(BigDecimal.ONE));
				j++;
			}
		} else {
			map.put(j, yaohl);
		}
		if (zhous > mode) {
			for (int i = 0; i < zhous - mode; i++) {
				map.put(j, yaohl);
			}
		}
		return map;
	}

	/**
	 * 根据查询条件查询已生效订单零件
	 * 
	 * @author 李智
	 * @date 2012-2-14
	 * @param page
	 *            分页显示
	 * @param param
	 *            查询条件
	 * @return Map 检索结果
	 */
	public Map<String, Object> queryDingdljByParam(Pageable page,
			Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("hlorder.queryDingdljByParam", param, page);
	}

	/**
	 * 查询某条订单零件可终止的数量
	 * 
	 * @author 李智
	 * @date 2012-2-14
	 * @param param
	 *            订单零件条件
	 * @return double 可终止数量
	 */
	public double queryAllowZzsl(Map<String, String> param) {
		Dingdlj dingdlj = (Dingdlj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(
				"hlorder.queryAllowGzsl", param);
		// 返回可终止数量
		if (dingdlj == null) {
			return 0;
		}
		return dingdlj.getAllowGzsl().doubleValue();
	}

	/**
	 * 终止订单零件
	 * 
	 * @author 李智
	 * @date 2012-2-27
	 * @param Dingdljs
	 *            订单零件
	 * @param loginUser
	 *            登录信息
	 * @return message
	 */
	public List<Yaohl> dingdljZz(List<Dingdlj> all, LoginUser loginUser) {
		List<Yaohl> allowYaohl = new ArrayList<Yaohl>();// 可终止的订单零件
		List<Yaohl> noAllowYaohl = new ArrayList<Yaohl>();// 可终止的数量为0的订单零件
		for (int i = 0; i < all.size(); i++) {
			Dingdlj dingdlj = (Dingdlj) all.get(i);
			Map<String, String> map = new HashMap<String, String>();
			// 终止的订单零件
			map = this.getParamsByBean(map, dingdlj);
			map.put("yaohlzt", Const.YAOHL_BIAOD);
			// 可终止的数量
			double allowGzsl = this.queryAllowZzsl(map);
			// 可终止的数量大于0
			Yaohl yaohl = new Yaohl();
			yaohl.setUsercenter(dingdlj.getUsercenter());// 用户中心
			yaohl.setDingdh(dingdlj.getDingdh());// 订单号
			yaohl.setLingjbh(dingdlj.getLingjbh());// 零件编号
			yaohl.setEditor(loginUser.getUsername());// 更新人
			yaohl.setAllowGzsl(new BigDecimal(allowGzsl));// 可终止量

			if (allowGzsl > 0) {
				allowYaohl.add(yaohl);
			} else {
				noAllowYaohl.add(yaohl);
			}
		}

		// 调“仓库service：终止订单零件”
		// allowYaohl
		// 先自己写方法终止
		for (int i = 0; i < allowYaohl.size(); i++) {
			Yaohl yaohl = allowYaohl.get(i);
			yaohl.setYaohlzt("05");
			ilYaohlService.updateYaohlZt(yaohl);
		}

		allowYaohl.addAll(noAllowYaohl);// 有没有终止量都返回
		return allowYaohl;
	}

	/**
	 * 把对象的值设置到map
	 * 
	 * @author 李智
	 * @param map
	 *            初始化的map
	 * @param dingdlj
	 *            bean
	 * @return 设置值后的map
	 */
	public Map<String, String> getParamsByBean(Map<String, String> map,
			Dingdlj dingdlj) {
		// 订单号
		map.put("dingdh", dingdlj.getDingdh());
		// 供应商代码
		map.put("gongysdm", dingdlj.getGongysdm());
		// 计划员组
		map.put("jihyz", dingdlj.getJihyz());
		// 用户中心
		map.put("usercenter", dingdlj.getUsercenter());
		// 零件号
		map.put("lingjbh", dingdlj.getLingjbh());
		// 交付比例大小等于
		map.put("searchSymbols", dingdlj.getSearchSymbols());
		// 交付比例
		if (dingdlj.getJfbl() != null) {
			map.put("jfbl", dingdlj.getJfbl().toString());
		}
		// 制作时间
		map.put("dingdzzsj_start", dingdlj.getDingdzzsj_start());
		// 制作时间
		map.put("dingdzzsj_end", dingdlj.getDingdzzsj_end());
		return map;
	}

	/**
	 * @方法：获取到pi数量
	 * @author 李明
	 * @version v1.0
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2012-01-03
	 * @参数：实体类，获取个数
	 */
	public static Map<Integer, BigDecimal> getPCount(Dingdlj bean, int index)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		BigDecimal pi = BigDecimal.ZERO;
		CommonFun.logger.debug("获取到pi数量getPCount方法index=" + index);
		Map<Integer, BigDecimal> map = new TreeMap<Integer, BigDecimal>();
		for (int i = 0; i < index; i++) {
			String method = Const.GETP + i + "sl";// 拼接getPi方法
			Class cls = bean.getClass();// 得到实体类
			// method拼接的方法
			Method meth = cls.getMethod(method, new Class[] {});
			pi = new BigDecimal(meth.invoke(bean, null).toString());// 执行得到的方法，取得riq
			CommonFun.logger.debug("获取到pi数量getPCount方法pi=" + pi);
			map.put(i, pi);
		}
		return map;
	}

	/**
	 * @方法：获取到pi数量
	 * @author 袁修瑞
	 * @version v1.0
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2012-12-13
	 * @参数：实体类，获取个数
	 */
	public static Map<String, BigDecimal> getPCounts(Dingdlj bean, int index)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		BigDecimal pi = BigDecimal.ZERO;
		CommonFun.logger.debug("获取到pi数量getPCount方法index=" + index);
		Map<String, BigDecimal> map = new TreeMap<String, BigDecimal>();
		String nianZq = bean.getP0fyzqxh();
		for (int i = 0; i < index; i++) {
			String method = Const.GETP + i + "sl";// 拼接getPi方法
			Class cls = bean.getClass();// 得到实体类
			// method拼接的方法
			Method meth = cls.getMethod(method, new Class[] {});
			pi = new BigDecimal(meth.invoke(bean, null).toString());// 执行得到的方法，取得riq
			CommonFun.logger.debug("获取到pi数量getPCount方法pi=" + pi);
			map.put(nianZq, pi);
			nianZq = CommonFun.addNianzq(nianZq, "12", 1);
		}
		return map;
	}

	public Dingdlj addDingdlj(final Map<String, String> param,
			Anxjscszjb anxcshbxzjb, String loginUser, String mos, String rq,
			String gongysdm) throws ParseException {
		// 查询订单零件
		Dingdlj dingdlj = (Dingdlj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryDingdlj", param);
		// 如果订单零件不为空
		if (dingdlj == null) {
			dingdlj = new Dingdlj();
			// 订单号
			dingdlj.setDingdh(param.get("dingdh"));
			// 删除标识
			dingdlj.setActive(Const.ACTIVE_1);
			// 用户中心
			dingdlj.setUsercenter(anxcshbxzjb.getUsercenter());
			// 零件编号
			dingdlj.setLingjbh(anxcshbxzjb.getLingjbh());
			dingdlj.setZuixqdl(anxcshbxzjb.getZuixqdl());
			// 指定供应商
			dingdlj.setZhidgys(anxcshbxzjb.getZhidgys());
			// 终止零件
			dingdlj.setZhongzlj(anxcshbxzjb.getZhongzzl());
			// 制造路线
			dingdlj.setZhizlx(anxcshbxzjb.getZhizlx());
			dingdlj.setP0fyzqxh(rq);
			// 发货地
			dingdlj.setFahd(anxcshbxzjb.getFahd());
			// 创建时间
			dingdlj.setCreate_time(CommonFun.getJavaTime());
			// 订单制作时间
			dingdlj.setDingdzzsj(CommonFun.getJavaTime().substring(0, 19));
			Date date = new Date();
			date.setTime(CommonFun.sdf.parse(
					CommonFun.getJavaTime().substring(0, 19)).getTime()
					- 24 * 60 * 60 * 1000);
			// 资源获取日期
			dingdlj.setZiyhqrq(CommonFun.sdf.format(date).substring(0, 10));
			// 创建人
			dingdlj.setCreator(loginUser);
			// 仓库
			dingdlj.setCangkdm(anxcshbxzjb.getXianbck().substring(0, 3));
			// 单位
			dingdlj.setDanw(anxcshbxzjb.getDanw());
			// 安全库存数量
			dingdlj.setAnqkc(anxcshbxzjb.getAnqkcs());
			dingdlj.setUabzlx(anxcshbxzjb.getGysuabzlx());
			dingdlj.setUabzuclx(anxcshbxzjb.getGysucbzlx());
			dingdlj.setUabzucrl(anxcshbxzjb.getGysucrl());
			dingdlj.setUabzucsl(anxcshbxzjb.getGysuaucgs());
			dingdlj.setUsbaozlx(anxcshbxzjb.getCkusbzlx());
			dingdlj.setUsbaozrl(anxcshbxzjb.getCkusbzrl());
			// 供应商份额
			dingdlj.setGongysfe(anxcshbxzjb.getGongysfe());
			// 备货周期
			dingdlj.setBeihzq(anxcshbxzjb.getBeihzq());
			// 发货周期
			dingdlj.setFayzq(anxcshbxzjb.getYunszq());
			// 供货模式
			dingdlj.setGonghms(mos);
			// 路径代码
			dingdlj.setLujdm(anxcshbxzjb.getLujbh());
			// 供应商代码
			dingdlj.setGongysdm(gongysdm);
			// 终止零件
			dingdlj.setZhongzlj(anxcshbxzjb.getZhongzzl());
			// 交付零件
			dingdlj.setJiaoflj(anxcshbxzjb.getJiaofzl());
			doInsert(dingdlj);
			return dingdlj;
		} else {
			return null;
		}
	}

	/**
	 * 汇总订单明细到订单零件
	 */
	public List<Dingdmx> queryForInsertDingdlj(Map<String, String> map) {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryForInsertDingdlj", map);
	}

	/**
	 * @方法：循环插入订单零件中 -------未测试
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 * @参数：list集合，实体类
	 */
	public void insertList(List all, Object bean) {
		if (!all.isEmpty()) {
			for (Object obj : all) {
				this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS)
						.execute("hlorder.insertDingdlj", obj);
			}
		}
	}

	public List<Dingdlj> queryAllJihyByDingd(Map<String, String> searchMap) {
		if (null != searchMap && null != searchMap.get("dingdh")) {
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryAllJihyByDingd", searchMap);
		}
		return null;
	}
}