package com.athena.xqjs.module.hlorder.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.ppl.Kdxqhzc;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.CalendarCenterService;
import com.athena.xqjs.module.common.service.CalendarVersionService;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * <p>
 * Title:毛需求明细类
 * </p>
 * <p>
 * Description:毛需求明细类
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
 * @date 2011-12-13
 */

@Component
public class MjMaoxqmxService extends BaseService {
	@Inject
	private CalendarCenterService calendarCenterService;// -用户中心日历service
	@Inject
	private CalendarVersionService calendarversionservice;

	/**
	 * 查询全部信息，返回list集合
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：String usercenter 用户中心,String banc 版次,String zhizlx
	 *       制造路线
	 */
	public List<Maoxqmx> queryAllMaoxqmxObject(String usercenter, String banc,
			String zhizlx) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		map.put("xuqbc", banc);
		map.put("zhizlx", zhizlx);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryMaoxqmxObject", map);
	}

	/**
	 * 
	 * 查询某一版次下的毛需求明细信息 -----ppl中使用
	 * 
	 * @param bean
	 * @return map 检索结果
	 * @author Xiahui
	 * @date
	 */
	public Map<String, Object> select(Pageable page, Map<String, String> params) {
		String lx = params.get(Const.PARAMS_PPL_LINGJLEIXING); // 从页面上获取输入的零件的类型（KD/IL）
		if (Const.LINGJIAN_LX_KD.equals(lx)) {
			// KD件的制造路线分为：爱信/PSA
			params.put("zhizlx", " and (l.zhizlx = '"
					+ Const.ZHIZAOLUXIAN_KD_PSA + "' or l.zhizlx = '"
					+ Const.ZHIZAOLUXIAN_KD_AIXIN + "' )");
		} else if (Const.LINGJIAN_LX_IL.equals(lx)) {
			// IL件的制造路线
			params.put("zhizlx", " and ( zhizlx = '" + Const.ZHIZAOLUXIAN_IL
					+ "'or zhizlx = '" + Const.ZHIZAOLUXIAN_GL + "' )");
		} else {
			params.put("zhizlx", "");
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.selectMaoxqmx", params, page);
	}

	/**
	 * 
	 * 将KD件毛需求明细表中的数据插入到kd毛需求汇总表中 -----ppl中使用
	 * 
	 * @param bean
	 * @author Xiahui
	 * @date
	 */
	public void insertKdxqhzc(Map map) {
		// 首先删除kdxqhzc表中所有数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"ilorder.deleteKdxqhzc");
		List<Kdxqhzc> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.selectKdxqhzc", map);
		for (int i = 0; i < list.size(); i++) {
			Kdxqhzc kdxqhzc = list.get(i);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"ilorder.insertKdxqhzc", kdxqhzc);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"ilorder.updateKdxqhzcax", map);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"ilorder.updateKdxqhzckd", map);
	}

	/**
	 * 将kd件毛需求汇总表中数据平移到kdxqhz表中 -----ppl中使用
	 * 
	 * @param kdxqhzc
	 * @author Xiahui
	 * @date
	 */
	public void insertKdxqhz(Map map) {
		// 首先删除kdxqhz表中的所有数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"ilorder.deleteKdxqhz");
		// 将kdxqhzc表中数据平移到 kdxqhz表中
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"ilorder.insertkdxqhz", map);

	}

	/**
	 * KD获取周序集合
	 * 
	 * @author lm
	 * @date 2012-01-19
	 * @参数：基准周序号，需要的周序个数,最大的年周序 资源获取日期所在周序 后 index 个周序的集合，如果大于当年的最大周序就跨年
	 */
	public static Map<String, String> getZhoux(String zhouqi, int index,
			String maxZhoux) {
		Map<String, String> map = new TreeMap<String, String>();
		if (StringUtils.isBlank(zhouqi) || StringUtils.isBlank(maxZhoux)
				|| (Integer.parseInt(zhouqi) > Integer.parseInt(maxZhoux))) {
			return map;
		}
		String zhouq = zhouqi;
		int max = Integer.parseInt(maxZhoux.substring(4, maxZhoux.length()));
		CommonFun.logger.debug("KD获取周序集合中变量max值为：" + max);
		String front = zhouq.substring(0, zhouq.length() - 2);
		CommonFun.logger.debug("KD获取周序集合中变量front值为：" + front);
		String back = zhouq.substring(zhouq.length() - 2, zhouq.length());
		CommonFun.logger.debug("KD获取周序集合中变量back值为：" + back);
		int length = (index - max + Integer.parseInt(back));
		CommonFun.logger.debug("KD获取周序集合中变量length值为：" + length);
		int key = max - Integer.parseInt(back) + 1;
		CommonFun.logger.debug("KD获取周序集合中变量key值为：" + key);
		DecimalFormat df = new DecimalFormat("00");
		for (int i = 0; i < index + 1; i++) {
			zhouq = front + df.format((Integer.parseInt(back) + i));
			CommonFun.logger.debug("KD获取周序集合中变量zhouq值为：" + zhouq);
			if (Integer.parseInt(zhouq) > (Integer.parseInt(front + max))) {
				front = Integer.parseInt(front) + 1 + "";
				CommonFun.logger.debug("KD获取周序集合中变量front值为：" + front);
				back = "01";
				CommonFun.logger.debug("KD获取周序集合中变量back值为：" + back);
				break;
			}
			map.put(Const.S + i, zhouq);
		}
		for (int i = 0; i < length; i++) {
			zhouq = front + df.format((Integer.parseInt(back) + i));
			map.put(Const.S + (key + i), zhouq);
		}
		return map;
	}

	/**
	 * 循环插入操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date: 2012-1-18
	 * @参数：list集合，实体bean,模式
	 */
	public String getParrten(String nameSpace, String parrten) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Const.MJ, nameSpace + "mjInsertMaoxqhzjTmp");
		return map.get(parrten.toUpperCase());
	}

	public void listInsertForIL(List all, String parrten) {
		String nameSpace = "hlorder.";
		String parrm = this.getParrten(nameSpace, parrten);
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(parrm, all);

	}

	public Map<String, String> getriq(String chanx, String usercenter,
			String ziyhqrq) {
		List<String> list = this.calendarversionservice.getZhuanHRq(usercenter,
				ziyhqrq, chanx);
		Map<String, String> outMap = new HashMap<String, String>();
		if (!list.isEmpty()) {
			for (int x = 0; x < list.size(); x++) {
				outMap.put("j" + x + "riq", list.get(x));
			}
		}
		
		return outMap;
	}

	public Map<String, String> getriqByCenter(String usercenter, String ziyhqrq) {
		List<String> list = this.calendarCenterService.getZhuanHRq(usercenter,
				ziyhqrq);
		Map<String, String> outMap = new HashMap<String, String>();
		if (!list.isEmpty()) {
			for (int x = 0; x < list.size(); x++) {
				outMap.put("j" + x + "riq", list.get(x));
			}
		}
		return outMap;
	}

	public List<String> getChanx(String user, String banc) {
		Map<String, Object> inMap = new HashMap<String, Object>();
		inMap.put("usercenter", user);
		inMap.put("xuqbc", banc);
		List<String> list = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryXuqcx", inMap);
		return list;
	}

}