package com.athena.xqjs.module.ppl.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.ppl.Niandyg;
import com.athena.xqjs.entity.ppl.Niandygmx;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.ilorder.service.MaoxqmxService;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：NiandygService
 * <p>
 * 类描述：毛需求CRUD操作
 * </p>
 * 创建人：Xiahui
 * <p>
 * 创建时间：2011-12-12
 * </p>
 * 
 * @version
 * 
 */
@SuppressWarnings("rawtypes")
@Component
public class NiandygService extends BaseService {
	@Inject
	private MaoxqmxService maoxqmxService;

	@Inject
	private NiandygmxService niandygmxService;

	/**
	 * log4j日志打印
	 */
	private final Log log = LogFactory.getLog(NiandygService.class);


	@Inject
	private CommonFun cf;
	/**
	 * 
	 * 查询年度预告
	 * 
	 * @param
	 * @author Xiahui
	 * @return list
	 */
	public Map<String, Object> select(Pageable page, Map<String, String> params) {

		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ppl.queryNiandyg", params, page);
	}

	/**
	 * 修改一条年度预告
	 * 
	 * @param bean
	 * @author Xiahui
	 * @return pplbc
	 * @date
	 */
	public String doUpdate(Niandyg bean) {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.updateNiandyg", bean);
		if (count == 0) {
			throw new ServiceException(MessageConst.UPDATE_COUNT_0);
		} else {
			return bean.getPplbc();
		}
	}

	/**
	 * 
	 * 添加一条年度预告
	 * 
	 * @param bean
	 * @author Xiahui
	 * @return pplbc
	 * @throws ServerException
	 * @date
	 */
	public String doInsert(Niandyg bean) throws ServerException {
		// 获取系统的时间
		// Date date = new Date(System.currentTimeMillis());
		// // 将系统时间格式化为"yyyyMMddHH24mm"形式
		// Format df = new SimpleDateFormat("yyyyMMddHHmm");
		String time = CommonFun.getJavaTime(Const.TIME_FORMAT_yyyyMMddHHmm);
		// 设置ppl版次为
		bean.setPplbc("PPL" + time);
		Map map = new HashMap();
		map.put("pplbc", "PPL" + time);
		Niandyg count = (Niandyg) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ppl.queryNiandyg", map);
		if (count != null) {
			throw new ServerException(MessageConst.EXCEPTION_CHONGFU);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.insertNiandyg", bean);
		bean.getJisnf();
		return bean.getPplbc();
	}

	/**
	 * 
	 * 删除某一版次的ppl预告信息
	 * 
	 * @param bean
	 * @author Xiahui
	 * @return pplbc
	 * @date
	 */
	public int doDelete(Map<String, Object> map) {
		// 删除已经失效的ppl版次预告信息（逻辑删除 修改ppl版次的状态）
		// 先删除ppl预告明细的相关信息（即修改年度预告明细的Active 为-1）
		niandygmxService.doDelete(map);
		// 再删除ppl预告明细的相关信息（即修改年度预告的Active状态为-1）
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.deleteNiandyg", map);

	}

	/**
	 * 检查参数异常报警 在计算年度预告前对毛需求明细中的零件进行参数校验 并将毛需求明细中相关联的参数信息存放在xqjs_xqmx表中 将参数齐全的零件
	 * 的标示符active 改为1 将缺少参数的零件零件信息存放到xqjs_yicbj表中
	 * 
	 * @param map
	 * @author Xiahui
	 */
	@SuppressWarnings("unchecked")
	public void checkvalue(Map<String, String> params) {
		// 检查参数前清空xqmx表中数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.deleteXqmx");
		// 设置异常报警的类型
		params.put("cuowlx", Const.YICHANG_LX1);
		// 检查参数如果参数齐全则修改xqjs_xqmx表的Active的标识为'1'
		if (Const.LINGJIAN_LX_KD.equals(params.get(Const.PARAMS_PPL_LINGJLEIXING))) {
			// 查询某个版次的毛需求明细信息集相关联的参数 存放到xqjs_xqmx表中
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.insertXqmxKd", params);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.updateNogysFeKd");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.updateNogysFeAx");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.updateXqmxKd");
		} else {
			// 查询某个版次的毛需求明细信息集相关联的参数 存放到xqjs_xqmx表中
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.insertXqmx", params);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.updateNogysFe");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.updateXqmxIl");
		}
		// 如果参数参数不齐全则将缺失参数的相关信息插入到xqjs_yicbj表中
		/*
		 * params.put("jihydm", params.get("creator"));
		 * params.put("exceptionMsg1", MessageConst.EXCEPTION_MSG1);
		 * params.put("exceptionMsg2", MessageConst.EXCEPTION_MSG2);
		 * params.put("exceptionMsg3", MessageConst.EXCEPTION_MSG3);
		 * params.put("exceptionMsg4", MessageConst.EXCEPTION_MSG4);
		 * params.put("exceptionMsg5", MessageConst.EXCEPTION_MSG5);
		 * params.put("exceptionMsg6", MessageConst.EXCEPTION_MSG6); //
		 * params.put("exceptionMsg7", MessageConst.EXCEPTION_MSG7);
		 * params.put("exceptionMsg8", MessageConst.EXCEPTION_MSG8);
		 * params.put("exceptionMsg9", MessageConst.EXCEPTION_MSG9);
		 * params.put("exceptionMsg10", MessageConst.EXCEPTION_MSG10);
		 * params.put("exceptionMsg11", MessageConst.EXCEPTION_MSG11);
		 * params.put("exceptionMsg12", MessageConst.EXCEPTION_MSG12);
		 * params.put("jismk", Const.JSMK_PPL);
		 * baseDao.getSdcDataSource(ConstantDbCode
		 * .DATASOURCE_XQJS).execute("ppl.insertYicbj", params);
		 */
		/*ArrayList<String> paramStr = new ArrayList<String>();
		LoginUser loginuser = AuthorityUtils.getSecurityUser();
		this.gJcsYcbj(paramStr, loginuser, Const.LINGJIAN_LX_KD.equals(params.get(Const.PARAMS_PPL_LINGJLEIXING)));
		String lx2 = params.get("lx2");
		params.remove("lx2");
		List<Map<String, String>> lxls1 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ppl.queryZhizlxBpp", params);
		params.remove("lx1");
		params.put("lx2", lx2);
		List<Map<String, String>> lxls2 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ppl.queryZhizlxBpp", params);
		lxls1.addAll(lxls2);
		for (int i = 0; i < lxls1.size(); i++) {
			Map<String, String> map = lxls1.get(i);
			paramStr.clear();
			paramStr.add(map.get("USERCENTER"));
			paramStr.add(map.get("LINGJBH"));
			paramStr.add(map.get("ZHIZLX"));
			paramStr.add(map.get("GONGHLX"));
			String cuowxxxx = Const.YICHANG_LX2_str29;
			cf.insertError(Const.YICHANG_LX3, cuowxxxx, loginuser.getJihyz(), paramStr, map.get("USERCENTER"), map.get("LINGJBH"), loginuser, Const.JSMK_PPL);
		}

		// 检查供应商份额是否为100%
		List<?> listlingj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ppl.selectfe");
		for (int i = 0; i < listlingj.size(); i++) {
			paramStr.clear();
			Lingj lingj = (Lingj) listlingj.get(i);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.deletefene", lingj);
			String cuowxxxx = Const.YICHANG_LX4_str1;
			paramStr.add(lingj.getUsercenter());
			paramStr.add(lingj.getLingjbh());
			cf.insertError(Const.YICHANG_LX4, cuowxxxx, loginuser.getJihyz(), paramStr, lingj.getUsercenter(), lingj.getLingjbh(), loginuser, Const.JSMK_PPL);

		}*/

	}

	/**
	 * 关键参数异常报警
	 * 
	 * @param paramStr
	 * @param loginuser
	 */
	@SuppressWarnings("unchecked")
	private void gJcsYcbj(ArrayList<String> paramStr, LoginUser loginuser, boolean flag) {
		log.debug("==============================gJcsYcbj  START=======================================");
		List<Map<String, String>> gjcsycbj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ppl.queryallYicbj");
		List<Yicbj> ycls = new ArrayList<Yicbj>();
		for (int i = 0; i < gjcsycbj.size(); i++) {
			Map<String, String> map = gjcsycbj.get(i);
			log.debug(i + "==============================gJcsYcbj=======================================" + map);
			paramStr.clear();
			String cuowxxxx;
			if (StringUtils.isEmpty(map.get("DINGHCJ"))) {
				paramStr.add(map.get("USERCENTER"));
				paramStr.add(map.get("LINGJBH"));
				cuowxxxx = Const.YICHANG_LX2_str47;
				ycls = CommonFun.insertError(Const.YICHANG_LX2, cuowxxxx, ycls, loginuser.getJihyz(), paramStr, map.get("USERCENTER"), map.get("LINGJBH"), loginuser, Const.JSMK_PPL);
			} else if (flag && StringUtils.isEmpty(map.get("GONGYSDM"))) {
				paramStr.add(map.get("USERCENTER"));
				paramStr.add(map.get("LINGJBH"));
				cuowxxxx = Const.YICHANG_LX2_str42;
				ycls = CommonFun.insertError(Const.YICHANG_LX2, cuowxxxx, ycls, loginuser.getJihyz(), paramStr, map.get("USERCENTER"), map.get("LINGJBH"), loginuser,
						Const.JSMK_PPL);
			} else if (StringUtils.isEmpty(map.get("ZHIZLX"))) {
				paramStr.add(map.get("USERCENTER"));
				paramStr.add(map.get("GONGYSDM"));
				cuowxxxx = Const.YICHANG_LX2_str40;
				ycls = CommonFun.insertError(Const.YICHANG_LX2, cuowxxxx, ycls, loginuser.getJihyz(), paramStr, map.get("USERCENTER"), map.get("LINGJBH"), loginuser,
						Const.JSMK_PPL);
			} else if (map.get("GONGYFE") == null) {
				paramStr.add(map.get("USERCENTER"));
				paramStr.add(map.get("LINGJBH"));
				paramStr.add(map.get("GONGYSDM"));
				cuowxxxx = Const.YICHANG_LX2_str43;
				cf.insertError(Const.YICHANG_LX2, cuowxxxx, loginuser.getJihyz(), paramStr, map.get("USERCENTER"), map.get("LINGJBH"), loginuser, Const.JSMK_PPL);
			} else if (flag && map.get("FAYZQ") == null) {
				paramStr.add(map.get("USERCENTER"));
				paramStr.add(map.get("GONGYSDM"));
				cuowxxxx = Const.YICHANG_LX2_str40;
				ycls = CommonFun.insertError(Const.YICHANG_LX2, cuowxxxx, ycls, loginuser.getJihyz(), paramStr, map.get("USERCENTER"), map.get("LINGJBH"), loginuser,
						Const.JSMK_PPL);
			} else if (flag && map.get("BEIHZQ") == null) {
				paramStr.add(map.get("USERCENTER"));
				paramStr.add(map.get("GONGYSDM"));
				cuowxxxx = Const.YICHANG_LX2_str39;
				ycls = CommonFun.insertError(Const.YICHANG_LX2, cuowxxxx, ycls, loginuser.getJihyz(), paramStr, map.get("USERCENTER"), map.get("LINGJBH"), loginuser,
						Const.JSMK_PPL);
			}

		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbj", ycls);
		log.debug("==============================gJcsYcbj  END=======================================");
	}

	/**
	 * 当点击确定按钮时会产生一条年度预告信息及多条年度预告明细信息
	 * 
	 * @param 选择的零件类型
	 * @throws ServerException
	 */
	@Transactional
	public void niandygCollect(Map<String, Object> params) throws ServerException {
		// 首先获得零件类型
		String lx = (String) params.get(Const.PARAMS_PPL_LINGJLEIXING);
		// 获取毛需求的版次
		String bc = (String) params.get(Const.MAOXQ_BC);
		// 获取计算年份
		String jisnf = (String) params.get(Const.NAINDYG_JISNF);

		// 获取创建人
		String creator = (String) params.get("creator");
		String create_time = (String) params.get("create_time");
		String editor = (String) params.get("editor");
		String edit_time = (String) params.get("edit_time");
		Niandyg bean = new Niandyg();
		bean.setJisnf(jisnf);
		bean.setJissj(create_time.substring(0, 19));
		bean.setMaoxqbc(bc);
		bean.setCreator(creator);
		bean.setCreate_time(create_time);
		bean.setEditor(editor);
		bean.setEdit_time(edit_time);
		bean.setJihydm((String) params.get("jihydm"));
		bean.setXuqcfsj((String) params.get("xuqcfsj"));
		Niandygmx niandygmx = new Niandygmx();

		params.put("jisnf", jisnf);
		params.put("xuqbc", bc);
		params.put("P0", jisnf + "01");
		params.put("P1", jisnf + "02");
		params.put("P2", jisnf + "03");
		params.put("P3", jisnf + "04");
		params.put("P4", jisnf + "05");
		params.put("P5", jisnf + "06");
		params.put("P6", jisnf + "07");
		params.put("P7", jisnf + "08");
		params.put("P8", jisnf + "09");
		params.put("P9", jisnf + "10");
		params.put("P10", jisnf + "11");
		params.put("P11", jisnf + "12");

		if (Const.LINGJIAN_LX_KD.equals(lx)) {
			// 设置年度预告PPL类型
			bean.setPpllx("KD");
			params.put("lx1", Const.ZHIZAOLUXIAN_KD_PSA);
			params.put("lx2", Const.ZHIZAOLUXIAN_KD_AIXIN);
			params.put("zhizlx", " and (zhizlx = '" + Const.ZHIZAOLUXIAN_KD_PSA + "' or zhizlx = '"
					+ Const.ZHIZAOLUXIAN_KD_AIXIN + "' )");
			this.kdJS(params, bean, niandygmx);
		} else if (Const.LINGJIAN_LX_IL.equals(lx)) {
			// 设置年度预告PPL类型
			bean.setPpllx("IL");
			params.put("lx1", Const.ZHIZAOLUXIAN_IL);
			params.put("lx2", Const.ZHIZAOLUXIAN_GL);
			// 获取制造路线
			params.put("zhizlx", " and ( zhizlx = '" + Const.ZHIZAOLUXIAN_IL + "'or zhizlx = '"
					+ Const.ZHIZAOLUXIAN_GL+ "' )");
			this.ilJS(params, bean, niandygmx);
		}
		// 计算完成后制作路线转换
		//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.replacePath",params);
	}

	/**
	 * 零件数量长度格式化 获取零件数量，如果零件数量不为空则显示为10为数字不够在前面补零 如果为空则补全为10位0
	 * 
	 * @param 零件数量
	 * @author Xiahui
	 * @date 2012-1-9
	 */
	public String formatsl(BigDecimal psl) {
		String str = "";
		if (psl == null) {
			str = "000000000";
		} else {
			str = String.format("%1$09d", psl.intValue());
		}
		return str;
	}

	/**
	 * 将年度预告明细的记录导出到txt文本
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * 
	 * @throws IOException
	 */
	public String writeTxt(Pageable page, Map<String, String> params) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			IOException {
		StringBuffer str = new StringBuffer();
		// 获取选中的年度预告明细信息
		List<?> list = this.niandygmxService.selectAll(params);
		// 遍历获取零件的信息
		for (int i = 0; i < list.size(); i++) {
			Niandygmx mx = (com.athena.xqjs.entity.ppl.Niandygmx) list.get(i);
			// List nzqlist = new ArrayList();
			// 获取每个周期的第一天
			for (int n = 0; n < 12; n++) {
				CalendarCenter cc = new CalendarCenter();
				cc.setNianzq(String.valueOf(Integer.parseInt(mx.getP0xqzq()) + n));
				cc.setUsercenter(mx.getUsercenter());
				// 查询参考系中心日历表 获得每个周期的第一天
				cc = (CalendarCenter) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ppl.queryCC", cc);
				Method method = mx.getClass().getMethod("getP" + n + "sl");
				BigDecimal psl = (BigDecimal) method.invoke(mx);
				if (cc != null) {
					str.append(Const.SPACE_9).append(mx.getLingjbh()).append(StringUtils.isEmpty(mx.getDinghcj()) ? Const.SPACE_3 : mx.getDinghcj()).append(Const.SPACE_15);
					str.append(mx.getZhizlx()).append(Const.SPACE_12).append(formatsl(psl)).append(Const.SPACE_2);
					// str.append(Integer.parseInt(mx.getP0xqzq())+n);
					String rq = cc.getRiq();
					str.append(rq.replaceAll("-", ""));
					str.append(Const.SPACE_4);
					str.append("P");
					str.append(Const.SPACE_4).append("\r\n");

				}
				// 存放到nsqlist集合中
				// nzqlist.add(cc.getRiq());
			}
			// 获取每个用户中心下每个周期零件的数量及每个周期的第一天
			/*
			 * str += (Const.SPACE_9 + mx.getLingjbh() + mx.getDinghcj() +
			 * Const.SPACE_15 + mx.getZhizlx() + Const.SPACE_12 +
			 * formatsl(mx.getP0sl()) + Const.SPACE_2 +
			 * Integer.parseInt(mx.getP0xqzq()) + nzqlist.get(0) + Const.SPACE_4
			 * + "P" + "\n"); str += (Const.SPACE_9 + mx.getLingjbh() +
			 * mx.getDinghcj() + Const.SPACE_15 + mx.getZhizlx() +
			 * Const.SPACE_12 + formatsl(mx.getP1sl()) + Const.SPACE_2
			 * +(Integer.parseInt(mx.getP0xqzq()) + 1) + nzqlist.get(1) +
			 * Const.SPACE_4 + "P" + "\n"); str += (Const.SPACE_9 +
			 * mx.getLingjbh() + mx.getDinghcj() + Const.SPACE_15 +
			 * mx.getZhizlx() + Const.SPACE_12 + formatsl(mx.getP2sl()) +
			 * Const.SPACE_2 + (Integer.parseInt(mx.getP0xqzq()) + 2) +
			 * nzqlist.get(2) + Const.SPACE_4 + "P" + "\n"); str +=
			 * (Const.SPACE_9 + mx.getLingjbh() + mx.getDinghcj() +
			 * Const.SPACE_15 + mx.getZhizlx() + Const.SPACE_12 +
			 * formatsl(mx.getP3sl()) + Const.SPACE_2 +
			 * (Integer.parseInt(mx.getP0xqzq()) + 3) + nzqlist.get(3) +
			 * Const.SPACE_4 + "P" + "\n"); str += (Const.SPACE_9 +
			 * mx.getLingjbh() + mx.getDinghcj() + Const.SPACE_15 +
			 * mx.getZhizlx() + Const.SPACE_12 + formatsl(mx.getP4sl()) +
			 * Const.SPACE_2 + (Integer.parseInt(mx.getP0xqzq()) + 4) +
			 * nzqlist.get(4) + Const.SPACE_4 + "P" + "\n"); str +=
			 * (Const.SPACE_9 + mx.getLingjbh() + mx.getDinghcj() +
			 * Const.SPACE_15 + mx.getZhizlx() + Const.SPACE_12 +
			 * formatsl(mx.getP5sl()) + Const.SPACE_2 +
			 * (Integer.parseInt(mx.getP0xqzq()) + 5) + nzqlist.get(5) +
			 * Const.SPACE_4 + "P" + "\n"); str += (Const.SPACE_9 +
			 * mx.getLingjbh() + mx.getDinghcj() + Const.SPACE_15 +
			 * mx.getZhizlx() + Const.SPACE_12 + formatsl(mx.getP6sl()) +
			 * Const.SPACE_2 + (Integer.parseInt(mx.getP0xqzq()) + 6) +
			 * nzqlist.get(6) + Const.SPACE_4 + "P" + "\n"); str +=
			 * (Const.SPACE_9 + mx.getLingjbh() + mx.getDinghcj() +
			 * Const.SPACE_15 + mx.getZhizlx() + Const.SPACE_12 +
			 * formatsl(mx.getP7sl()) + Const.SPACE_2 +
			 * (Integer.parseInt(mx.getP0xqzq()) + 7) + nzqlist.get(7) +
			 * Const.SPACE_4 + "P" + "\n"); str += (Const.SPACE_9 +
			 * mx.getLingjbh() + mx.getDinghcj() + Const.SPACE_15 +
			 * mx.getZhizlx() + Const.SPACE_12 + formatsl(mx.getP8sl()) +
			 * Const.SPACE_2 + (Integer.parseInt(mx.getP0xqzq()) + 8) +
			 * nzqlist.get(8) + Const.SPACE_4 + "P" + "\n"); str +=
			 * (Const.SPACE_9 + mx.getLingjbh() + mx.getDinghcj() +
			 * Const.SPACE_15 + mx.getZhizlx() + Const.SPACE_12 +
			 * formatsl(mx.getP9sl()) + Const.SPACE_2 +
			 * (Integer.parseInt(mx.getP0xqzq()) + 9) + nzqlist.get(9) +
			 * Const.SPACE_4 + "P" + "\n"); str += (Const.SPACE_9 +
			 * mx.getLingjbh() + mx.getDinghcj() + Const.SPACE_15 +
			 * mx.getZhizlx() + Const.SPACE_12 + formatsl(mx.getP10sl()) +
			 * Const.SPACE_2 + (Integer.parseInt(mx.getP0xqzq()) + 10) +
			 * nzqlist.get(10) + Const.SPACE_4 + "P" + "\n"); str +=
			 * (Const.SPACE_9 + mx.getLingjbh() + mx.getDinghcj() +
			 * Const.SPACE_15 + mx.getZhizlx() + Const.SPACE_12 +
			 * formatsl(mx.getP11sl()) + Const.SPACE_2 +
			 * (Integer.parseInt(mx.getP0xqzq()) + 11) + nzqlist.get(11) +
			 * Const.SPACE_4 + "P" + "\n");
			 */
		}
		// 写入到文件的文本路径及文件名

		String flagUrl = System.getProperty("java.io.tmpdir");
		if(StringUtils.isNotBlank(flagUrl))
		{
			if(!flagUrl.endsWith(File.separator))
			{
				flagUrl = flagUrl + File.separator;
			}
		}
		String fileurl = flagUrl + "ppl年度预告明细.txt";
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileurl)));
		out.write(str.toString());
		out.close();
		return fileurl;
	}

	/**
	 * 获取计划员组的信息
	 * 
	 * @author Xiahui
	 * @date 2012-1-11
	 */
	public List selectjihyz(Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ppl.selectJihyz", map);
	}

	/**
	 * 获取需求版次的信息
	 */
	public List selectxuqbc() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ppl.selectXuqbc");

	}

	/**
	 * kd件年度预告计算
	 * 
	 * @throws ServerException
	 * 
	 */
	public void kdJS(Map mxmap, Niandyg bean, Niandygmx niandygmx) throws ServerException {

		// 先添加一条年度预告信息
		String pplbc = doInsert(bean);
		// 检查参数
		this.checkvalue(mxmap);
		// 从中间表汇总到周需求，将kd件毛需求插入到kdxqhzc表中
		// maoxqmxService.insertKdxqhzc(mxmap);
		// 将kdxqhzc表中数据平移到kdxqhz表中,周平移
		maoxqmxService.insertKdxqhz(mxmap);
		// 设置年度预告明细的pplbc
		// niandygmx.setPplbc(pplbc);
		mxmap.put("pplbc", pplbc);
		// 设置kdxqhz表的年周期
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateNianzqKD", mxmap);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateNianzqAX", mxmap);
		// 将kdxqhz表中数据插入到niandygmx表中
		niandygmxService.doInsertKD(mxmap);
	}

	/**
	 * IL件年度预告明细
	 * 
	 * @throws ServerException
	 */
	public void ilJS(Map mxmap, Niandyg bean, Niandygmx niandygmx) throws ServerException {
		// 添加一条年度预告信息
		String pplbc = doInsert(bean);
		this.calendarBj(mxmap.get("JISNF").toString());
		// 检查参数
		this.checkvalue(mxmap);
		mxmap.put("pplbc", pplbc);
		// 向niandygmx表中添加数据
		niandygmxService.doInsertIL(mxmap);
	}

	/**
	 * 查询计算参数设置表 判断当前是否有用户在进行ppl计算
	 * 
	 * @author Xiahui
	 * @date 2012-2-13
	 */
	public boolean selectJisclcssz() {
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ppl.searchJisclcssz");
		return list.size() == 0;
	}

	/**
	 * 在进行ppl计算时向计算参数设置表中插入一条数据
	 * 
	 * @author Xiahui
	 * @date 2012-2-13
	 */
	public void insertJisclcssz(Map map) {
		map.put("updatetime", CommonFun.getJavaTime().substring(0, 19));
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.insertJisclcssz", map);
	}

	/**
	 * 当ppl计算结束时将计算参数设置表中的处理状态修改为已完成
	 * 
	 * @author Xiahui
	 * @date 2012-2-13
	 */
	public void updateJisclcssz(Map map) {

		map.put("updatetime", CommonFun.getJavaTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.updateJisclcssz", map);
	}

	/**
	 * 中心日历数据不足计算时报警
	 */
	public void calendarBj(String jsnf) {
		Map<String, String> map = new HashMap<String, String>();
		List<String> ucls = new ArrayList<String>();
		ucls.add(Const.WTC_CENTER_UW);
		ucls.add(Const.WTC_CENTER_UL);
		ucls.add(Const.WTC_CENTER_UX);
		map.put("from", jsnf + "01");
		int nf = Integer.parseInt(jsnf);
		map.put("to", (nf + 1) + "03");
		Yicbj yicbj = new Yicbj();
		yicbj.setJismk(Const.JSMK_PPL);
		yicbj.setCuowlx(Const.CUOWLX_200);
		for (int i = 0; i < ucls.size(); i++) {
			map.remove("usercenter");
			map.put("usercenter", ucls.get(i));
			List<?> ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ppl.queCalendar", map);
			if (ls.size() == 0) {
				String cuowxxxx = ucls.get(i) + "用户中心下的" + jsnf + "年的中心日历数据为空";
				yicbj.setCuowxxxx(cuowxxxx);
				yicbj.setUsercenter(ucls.get(i));
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.yicfe", yicbj);
			}

		}
	}

}