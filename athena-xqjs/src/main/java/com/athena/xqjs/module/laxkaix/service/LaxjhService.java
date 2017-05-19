/**
 * 
 */
package com.athena.xqjs.module.laxkaix.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.service.PostService;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.date.DateUtil;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.laxkaix.CkYaohlBean;
import com.athena.xqjs.entity.laxkaix.CkxwulljBean;
import com.athena.xqjs.entity.laxkaix.Laxjh;
import com.athena.xqjs.entity.laxkaix.LaxjhBean;
import com.athena.xqjs.entity.laxkaix.Laxjhmx;
import com.athena.xqjs.entity.laxkaix.LinsKeytc;
import com.athena.xqjs.entity.laxkaix.LinsKeytclj;
import com.athena.xqjs.entity.laxkaix.LinsTclj;
import com.athena.xqjs.entity.laxkaix.LinsXuq;
import com.athena.xqjs.entity.laxkaix.TC;
import com.athena.xqjs.entity.laxkaix.Weimzlj;
import com.athena.xqjs.entity.lingjcx.YaohlBean;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.CommonCalendarService;
import com.athena.xqjs.module.ilorder.service.ZiykzbService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.utils.JSONUtils;
import com.toft.utils.json.JSONException;

/**
 * @author dsimedd001
 * 
 */
@Component
public class LaxjhService extends BaseService {

	@Inject
	private YicbjService yicbjService;

	@Inject
	private CommonCalendarService commonCalendarService;
	
	@Inject
	private ZiykzbService ziykzbService ;
	private final Log log = LogFactory.getLog(PostService.class);

	/**
	 * 计算零件短缺
	 * 
	 * @param xuqbc
	 * @param usercenter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String jsLingjDuanq(Maoxqmx xqmx, Laxjh laxjh, LoginUser loginUser){
		// 拉箱界限
		BigDecimal laxjx = laxjh.getLaxjx();
		// 安全库存天数
		BigDecimal anqkc = BigDecimal.ZERO;
		// 安全库存天数为空，则为零
		if (laxjh.getAnqkc() != null) {
			anqkc = laxjh.getAnqkc();
		}
		// 资源天数
		BigDecimal ziyts = BigDecimal.ZERO;
		// 当资源天数!=空时，取资源天数的值；当资源天数==空时，资源天数取拉箱界限的值；
		if (laxjh.getZiyts() != null) {
			ziyts = laxjh.getZiyts();
		} else {
			ziyts = laxjx;
		}
		// 获取用户名
		String username = loginUser.getUsername();
		// 用户中心
		String usercenter = xqmx.getUsercenter();
		// 删除临时表 tc零件表中的数据
		LinsTclj linsTclj = new LinsTclj();
		// 设置用户中心
		linsTclj.setUsercenter(usercenter);
		// 删除临时表_临时需求表
		LinsXuq tempxuq = new LinsXuq();
		tempxuq.setUsercenter(usercenter);//用户中心
		this.deleteLinsXuq(tempxuq);//清空临时需求表
		log.info(LaxkaixConst.deleteLinsXuqMsg);
		// 删除临时表_零件表
		this.deleteLinstclj(linsTclj);
		log.info(LaxkaixConst.deleteLinstcljMsg);
		/**
		 * 初始化临时表 TC零件表
		 */
		// 初始化零件集合
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 设置零件号与计划员代码的对应
		// Lingj lingj = new Lingj();
		// 设置用户中心
		// lingj.setUsercenter(usercenter);
		// 查询各集装箱中各零件信息
		// 获取拉箱界限日期
		String now = DateUtil.getCurrentDate();
		//查询TC零件信息
		log.info(LaxkaixConst.selectLinstcljMsg);
		String msg = "1";
		Map<String,String> tjMap = new HashMap<String,String>();
		//用户中心
		tjMap.put("usercenter", usercenter);
		//需求版次
		tjMap.put("xuqbc", xqmx.getXuqbc());
		//获取所选择的毛需求中的零件编号信息
		List<String> maoxqLingjbhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.getMaoxqLingjbh",tjMap);
		boolean flag = true;
		StringBuffer sb = new StringBuffer();
		int size = maoxqLingjbhList.size();
		//拼接所有毛需求中的零件的需件编号
		sb.append("(");
	    for (int i = 0; i < size; i++) {  
	    	String lingjbh = maoxqLingjbhList.get(i);//用户编号
	    	sb.append("'"+lingjbh+"'");//零件编号
            if (i == (size - 1)) {  
            	sb.append("'"+lingjbh+"'"); //SQL拼装，最后一条不加“,”。  
            }else if((i%999)==0 && i>0){  
            	sb.append(") or UABQ_.lingjbh in ("); //解决ORA-01795问题  
            }else{  
            	sb.append(",");  
            }  
        }   
		//sb.append("(");
//		for(int i=0;i<size;i++){
//			String lingjbh = maoxqLingjbhList.get(i);//用户编号
//			sb.append("'"+lingjbh+"'");//零件编号
//			if(i<size-1&&flag==true){
//				sb.append(",");
//			}else{
//				flag = false;
//			}
//		}
		sb.append(")");
		tjMap.put("lingjbhs", sb.toString());
		//查询
		list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.getTCLingjList", tjMap);
		log.info(LaxkaixConst.selectMaoxuqLingjbh);
		try {
			// 遍历集合并将TC零件信息插入LINS_TCLJ中
			this.bianliList(list, usercenter, username);
			log.info(LaxkaixConst.insertLinstcljMsg);
		} catch (Exception e) {
			msg = "2";
			log.info(LaxkaixConst.nullJihydmMsg);
		}
		//拉箱结束时间为当前时间+滞箱天数-1
		String laxzdddsj = DateUtil.dateAddDays(now, ziyts.intValue() - 1);
		Map<String, String> zqmap = new HashMap<String, String>();
		try {
			//判断当前时间与结束时间是否跨周期
			zqmap = this.jszqrq(now, laxzdddsj);
			log.info(LaxkaixConst.jiszqrqMsg);
		} catch (Exception e) {
			msg = "3";
		}
		List<LinsXuq> xuqList = new ArrayList<LinsXuq>();
		Map<String,Integer> laxjxGongzrtsMap = new HashMap<String,Integer>();
		// 获取临时需求
		try {
			LaxjhBean bean = this.getLingjLinsxuq(zqmap, xqmx, username,
					Const.JISMK_LAX_CD);
			xuqList = bean.getLinsxuqList();
			laxjxGongzrtsMap = bean.getLaxjxGongzrtsMap();
			//获取临时需求
			log.info(LaxkaixConst.getLinsxuqMsg);
		} catch (Exception e) {
			log.error("请检查零件与用户中心是否对应订货仓库");
		}
		try {
			this.insertLinsXuq(xuqList);
		} catch (Exception e) {
			msg = "5";
		}
		// 计算KD零件重箱区库存
		List<Map<String, Object>> zxqkcList = this.getZxqkcByUc(usercenter);
		// 重箱区库存集合
		Map<String,BigDecimal> zxqkc = this.getZxqkc(zxqkcList);
		//初始化Bean TC
		TC tc = new TC();
		//用户中心
		tc.setUsercenter(usercenter);
		//拉箱指定到达时间
		tc.setLaxzdddsj(laxzdddsj);
		if (usercenter.equals(LaxkaixConst.ucUw)) {
			tc.setZuiswld(LaxkaixConst.uwZxq);//用户中心为UW时，最新物理点设置为107的重箱区
		} else if (usercenter.equals("UL")) {
			tc.setZuiswld(LaxkaixConst.uxZxq);//用户中心为UL时，最新物理点设置为108的重箱区
		}else if(usercenter.equals("UX")){
			tc.setZuiswld("15A");//用户中心为UL时，最新物理点设置为108的重箱区
		}
		tc.setCurrentDate(now);//设置当前日期
		//获取预计到货零件数量
		List<Map<String, Object>> yddhljslList = this.getYujdhsl(tc);
		// 预定到货零件数量Map集合
		Map<String,BigDecimal> yddhljslmap = new HashMap<String,BigDecimal>();
		yddhljslmap = this.getljslMap(yddhljslList);
		yddhljslList.clear();
		// 查出现有库存中的所有零件的库存
		List<Map<String, Object>> allLjkcList = new ArrayList<Map<String, Object>>();// 初始化零件库存集合
		// 初始化临时变量(计算库存)
		allLjkcList = this.getAllLjkc(laxjh);
		Map<String,BigDecimal> ljkcMap = new HashMap<String,BigDecimal>();// 初始化零件库存对应map集合
		ljkcMap = this.getljslMap(allLjkcList);
		allLjkcList.clear();
		List<Map<String, Object>> allTdjList = new ArrayList<Map<String, Object>>();// 初始化替代件集合
		allTdjList = this.getAlllTdjList(laxjh);
		Map<String, List<String>> tdjMap = new HashMap<String, List<String>>();// 初始化替代件map集合
		tdjMap = this.getTdjMap(allTdjList);

		List<Map<String, Object>> getZcyujddslList = new ArrayList<Map<String, Object>>();
		getZcyujddslList = this.getZcyujddslList(tc);
		// 预计快到达零件数量Map集合
		Map<String, BigDecimal> yjddLingjmap = new HashMap<String, BigDecimal>();
		yjddLingjmap = this.getljslMap(getZcyujddslList);
		getZcyujddslList.clear();
		allTdjList.clear();
		List<LinsXuq> updateList = new ArrayList<LinsXuq>();//初始化临时需求中需要更新的List
		List<LinsXuq> deleteList = new ArrayList<LinsXuq>();//初始化临时需求中需要删除的List
		// 获取所有日历版次信息
		for (LinsXuq xuq : xuqList) {// 获取插入的临时需求表
			String lingjh = xuq.getLingjh();// 获取零件号
			BigDecimal sumValues = BigDecimal.ZERO;
			//获取仓库零件(仓库和替代件)
			sumValues = this.getLjsls(ljkcMap, tdjMap, lingjh);
			/**
			 * 短缺零件判断
			 */
			BigDecimal cmj = BigDecimal.ZERO;
			String cmjs = "0";
			if (xuq.getCmj() != null) {
				cmj = xuq.getCmj();
				cmjs = cmj.toString();
			}
			// 安全库存天数*cmj
			BigDecimal anqkcs = anqkc.multiply(new BigDecimal(cmjs));
			// 拉箱界限*cmj
			BigDecimal laxjxs = BigDecimal.ZERO;
			if(laxjxGongzrtsMap.get(lingjh)!=null&&!"".equals(laxjxGongzrtsMap.get(lingjh))){
				laxjxs = new BigDecimal(cmjs).multiply(new BigDecimal(
						(Integer) laxjxGongzrtsMap.get(lingjh)));
			}
			// 安全库存*cmj与拉箱界限*cmj之和
			BigDecimal addCmj = (new BigDecimal(anqkcs.toString()))
					.add(new BigDecimal(laxjxs.toString()));
			// 零件重箱区库存
			BigDecimal linjgzxqkc = BigDecimal.ZERO;
			linjgzxqkc = this.getZxqkcljsl(zxqkc, lingjh);
			// 零件仓库库存与零件重箱区库存之和
			BigDecimal tempkc = BigDecimal.ZERO;
			tempkc = sumValues.add(new BigDecimal(linjgzxqkc.toString()));
			// 拉箱界限天数内拉箱计划到达数量
			BigDecimal yddhljsl = BigDecimal.ZERO;
			if (yddhljslmap.get(lingjh) != null) {
				yddhljsl = (BigDecimal) yddhljslmap.get(lingjh);
			}
			// 拉箱标识为卡运计划时，正常预计能到达的箱子
			BigDecimal yjddljsl = BigDecimal.ZERO;
			if (yjddLingjmap.get(lingjh) != null) {
				yjddljsl = (BigDecimal) yjddLingjmap.get(lingjh);
			}
			// 零件仓库库存+零件重箱区库存+拉箱界限天数内零件重箱区库存之和
			BigDecimal laxjxLjsl = BigDecimal.ZERO;
			if (laxjh.getLaxbs().equals("2")) {
				laxjxLjsl = yjddljsl.add(tempkc);
				tempkc = laxjxLjsl;
			}
			BigDecimal allkc = (new BigDecimal(yddhljsl.toString()))
					.add(new BigDecimal(tempkc.toString()));
			// 设置需求数量；即是短缺数量，短缺数量：安全库存*cmj与拉箱界限*cmj之和-零件仓库库存+零件重箱区库存+拉箱界限天数内零件重箱区库存之和
			BigDecimal xuqsl = BigDecimal.ZERO;
			xuqsl = (new BigDecimal(addCmj.toString()))
					.subtract(new BigDecimal(allkc.toString()));
			xuq.setXuqsl(xuqsl);
			// 设置零件库存
			xuq.setCangkkc(sumValues);
			// 设置重箱区库存
			xuq.setZhongxqkc(linjgzxqkc);
			// 断点时间计算
			BigDecimal days = BigDecimal.ZERO;
			if (cmj.compareTo(BigDecimal.ZERO) == 1) {
				days = allkc.divide(cmj, 0, BigDecimal.ROUND_UP);
			}
			String currentDate = DateUtil.getCurrentDate();
			String duandsj = getDuandsj(days, currentDate);
			// 设置修改员
			xuq.setEditor(username);
			// 设置修改时间
			String currentDay = DateUtil.getCurrentDate();
			xuq.setEditTime(currentDay);
			xuq.setDuandsj(duandsj);
			/**
			 * 判断短缺零件 当需求数量大于零时，则表示有短缺零件，则更新临时需求
			 */
			if (xuqsl.compareTo(BigDecimal.ZERO) == 1&&(days.compareTo(ziyts)<=0)) {
				updateList.add(xuq);
			} else if (xuqsl.compareTo(BigDecimal.ZERO) == -1&&(days.compareTo(ziyts)>0)) {
				deleteList.add(xuq);
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"linsxuq.updateLinsxuq", updateList);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"linsxuq.deleteLinsxuqInfo", deleteList);
		return msg;
	}

	@SuppressWarnings("unchecked")
	public String getDuandsj(BigDecimal days, String currentDate) {
		String duandsj = "";
		String rilbc = Const.LAXBANCI;
		Map<String,String> rilbcMap = new HashMap<String,String>();
		rilbcMap.put("rilbc", rilbc);
		rilbcMap.put("currentDate", currentDate);
		//rilbcMap.put("endRiq", DateUtil.dateAddDays(currentDate, days.intValue()));
		if(days.intValue()>0){
			//获取传入days天内的工作日的日期
			Map<String, Object> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("laxjh.getDuandsj", rilbcMap, 1, days.intValue());
			List<Map<String,String>> resultList = (List<Map<String,String>>)result.get("rows");
			Map<String,String> duandsjMap = new HashMap<String,String>();
			if(resultList.size()>0){
				duandsjMap = resultList.get(resultList.size()-1);
				//其最小的日期为断点时间
				duandsj = (String)duandsjMap.get("DUANDSJ");
			}
		}else if(days.intValue()==0){
			//获取下一个工作日
			Map<String, Object> result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("laxjh.getGongzrsj", rilbcMap, 1, 1);
			List<Map<String,String>> resultList = (List<Map<String,String>>)result.get("rows");
			if(resultList.size()>0){
				//如果获取日期是工作日 ，则当前日期为断点时间，如果不是工作日 ，则获取下一个工作日
				Map<String,String> duandsjMap = resultList.get(resultList.size()-1);
				String sfgzr = duandsjMap.get("SHIFGZR");
				if(sfgzr.equals("1")){
					duandsj = currentDate;
				}else if(sfgzr.equals("0")){
					Map<String, Object> result1 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("laxjh.getDuandsj", rilbcMap, 1, 1);
					List<Map<String,String>> resultList1 = (List<Map<String,String>>)result1.get("rows");
					if(resultList1.size()>0){
						Map<String,String> duandsjMap1 = (Map<String,String>)resultList1.get(0);
						duandsj = duandsjMap1.get("DUANDSJ");
					}
				}
			}
		}
		return duandsj;
	}

	/**
	 * @param tc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getZcyujddslList(TC tc) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.getZcyujddsl", tc);
	}

	/**
	 * 获取临时需求 判断逻辑： 未跨周期: 拉箱开始时间到拉箱结束时间之内的毛需求明细的零件数量之和/拉箱界限之间工作天数 跨周期:
	 * ((拉箱开始时间当月的毛需求明细之和除以当月工作天数*拉箱开始时间到当月最后一天的工作日历) +
	 * (拉箱界限时间当月的毛需求明细之和除以当月工作天数*拉箱结束时间第一天到拉箱界限时间的工作日历)) 拉箱开始时间到拉箱界限结束时间之内的工作日历
	 * 
	 * @param map
	 * @return
	 */
	public LaxjhBean getLingjLinsxuq(Map<String, String> sjMap, Maoxqmx xqmx,
			String username, String jismk) {
		// 获取拉箱开始时间
		String now = sjMap.get("now");
		// 从当前日期的下一天开始
		// now = DateUtil.dateAddDays(now, 1);
		// 获取拉箱界限终上时间
		String laxjxsj = sjMap.get("laxjxsj");
		// 获取拉箱开始时间当月第一天
		String nowStartofDate = sjMap.get("nowStartofDate");
		// 获取拉箱开始时间当月最后一天
		String nowEndofDate = sjMap.get("nowEndofDate");
		// 获取拉箱界限终止时间当月第一天
		String laxjxStartofDate = sjMap.get("laxjxStartofDate");
		// 获取拉箱界限终止时间当月最后一天
		String laxjxEndofDate = sjMap.get("laxjxEndofDate");
		// 未跨月的毛需求明细零件数量之和List
		List<Map<String, Object>> lingjslList = new ArrayList<Map<String, Object>>();
		// 返回的临时需求List
		List<LinsXuq> linsXuqlist = new ArrayList<LinsXuq>();
		// 用户中心
		String usercenter = xqmx.getUsercenter();
		// 毛需求版次
		String maoxqbc = xqmx.getXuqbc();
		// 跨月后的开始月的毛需求明细零件数量之和List
		List<Map<String, Object>> nowlingjslList = new ArrayList<Map<String, Object>>();
		// 跨月后的结束月的毛需求明细零件数量之和List
		List<Map<String, Object>> laxjxlingjslList = new ArrayList<Map<String, Object>>();
		Map<String, Object> lingjcmjMap = new HashMap<String, Object>();
		// 获取所有日历版次信息
		Map<String, String> cgmap = commonCalendarService.getCalendarGroupMap();
		Map<String,Integer> laxjxGongzrtsMap = new HashMap<String,Integer>();
		if (laxjxStartofDate == null) {
			// 未跨周期
			String xuqsszq = sjMap.get("xuqsszq");
			lingjslList = getSumLingsl(xqmx, xuqsszq);
			log.info("根据需求所属周期，获取该月的毛需求信息");
			Map<String, BigDecimal> nowCdmap = getAllGongzrts(nowStartofDate,
					nowEndofDate);
			Map<String,String> wulljMap = getWuljMap(usercenter);
			Map<String, BigDecimal> nowGongzrmap = getAllGongzrts(now, laxjxsj);
			for (Map<String, Object> map : lingjslList) {
				// 零件编号
				String lingjbh = (String) map.get("LINGJBH");
				// KD需求总量
				BigDecimal sumXuqsl = (BigDecimal) map.get("SUMXUQSL");
				log.info("根据零件编号与用户中心获取订货仓库");
				
				String dinhck = wulljMap.get(usercenter + ":"
						+ lingjbh);
				log.info("获取订货仓库" + dinhck);
				if (StringUtils.isEmpty(dinhck)) {
					List<String> msgList = new ArrayList<String>();
					msgList.add(usercenter);
					msgList.add(lingjbh);
					LoginUser loginuser = new LoginUser();
					loginuser.setUsername(username);
					yicbjService.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str30, "", msgList, usercenter,
							lingjbh, loginuser, jismk);
				}else{
					String rilbc = Const.LAXBANCI;
					// KD件订货仓库的生产日历-工作天数
					BigDecimal gongzts = BigDecimal.ZERO;
					Integer gongztsI = 0;
					if(nowCdmap!=null&&!nowCdmap.equals("")&&nowCdmap.get(xqmx.getUsercenter() + ":" + rilbc)!=null&&!"".equals(nowCdmap.get(xqmx.getUsercenter() + ":" + rilbc))){
							gongzts = nowCdmap.get(xqmx.getUsercenter() + ":" + rilbc);
							// 工作天数转换为Integer
							gongztsI = Integer.valueOf(gongzts.intValue());
					} else{
						List<String> msgList = new ArrayList<String>();
						msgList.add(usercenter);
						msgList.add(rilbc);
						msgList.add(xuqsszq);
						msgList.add(nowEndofDate);
						LoginUser loginuser = new LoginUser();
						loginuser.setUsername(username);
						yicbjService.insertError(Const.YICHANG_LX2,
								Const.YICHANG_LX2_str31, "", msgList, usercenter,
								lingjbh, loginuser, jismk);

					}
					
					// 单个零件号的CMJ值
					BigDecimal singleCMJ = BigDecimal.ZERO;
					// 工作天数
					if (gongzts.intValue() != 0) {
						// 单个零件CMJ(零件总数量/工作天数)
						singleCMJ = (new BigDecimal(sumXuqsl.toString())).divide(
								new BigDecimal(gongztsI), 3,
								BigDecimal.ROUND_HALF_EVEN);
					}
					String singleCMJS = singleCMJ + "";
					// 初始化临时表 临时需求表
					LinsXuq lxuq = new LinsXuq();
					// 设置用户中心
					lxuq.setUsercenter(usercenter);
					// 设置毛需求版次
					lxuq.setMaoxqbc(maoxqbc);
					// 获取计划员信息(以usercenter+":"+lingjbh为key)
					String jihydm = (String) map.get("JIHY");
					// 设置计划员代码
					lxuq.setJihydm(jihydm);
					// 设置零件编号
					lxuq.setLingjh(lingjbh);
					lxuq.setManzsl(BigDecimal.ZERO);
					// 设置零件CMJ
					String cmjKey = usercenter + ":" + lingjbh;
					/**
					 * 设置lingj的cmj信息 Map设置零件的CMJ信息,以usercenter+":"+lingjbh为key,
					 * 如果map中不存在key值，则设置该零件的cmj,如果存在，则获取其原来的CMJ值，与新获取的cmj值相加
					 */
					if (lingjcmjMap.get(cmjKey) == null) {
						lingjcmjMap.put(cmjKey, new BigDecimal(singleCMJS));
						lxuq.setCmj(new BigDecimal(singleCMJS));
					} else if (lingjcmjMap.get(cmjKey) != null) {
						BigDecimal tempcmjs = (BigDecimal) lingjcmjMap.get(cmjKey);
						tempcmjs = tempcmjs.add(new BigDecimal(singleCMJS));
						lingjcmjMap.put(cmjKey, tempcmjs);
						lxuq.setCmj(tempcmjs);
					}
					// 设置创建者
					lxuq.setCreator(username);
					// 设置创建时间
					lxuq.setCreateTime(DateUtil.curDateTime());
					linsXuqlist.add(lxuq);
					if(nowGongzrmap!=null&&!nowGongzrmap.equals("")&&nowGongzrmap.get(usercenter + ":" + rilbc)!=null&&!"".equals(nowGongzrmap.get(usercenter + ":" + rilbc))){
							laxjxGongzrtsMap.put(lingjbh,nowGongzrmap.get(usercenter + ":" + rilbc).intValue());
					}else{
						List<String> msgList = new ArrayList<String>();
						msgList.add(usercenter);
						msgList.add(rilbc);
						msgList.add(xuqsszq);
						msgList.add(nowEndofDate);
						LoginUser loginuser = new LoginUser();
						loginuser.setUsername(username);
						yicbjService.insertError(Const.YICHANG_LX2,
								Const.YICHANG_LX2_str31, "", msgList, usercenter,
								lingjbh, loginuser, jismk);
					}
				}
			}
		} else {
			// 跨周期开始所属周期
			String startXuqsszq = sjMap.get("startXuqsszq");
			nowlingjslList = getSumLingsl(xqmx, startXuqsszq);
			log.info("根据跨周期开始所属周期查询零件数量");
			// 开始月的需求数量Map，以零件编号为key,需求数量之和为value
			Map<String, BigDecimal> nowXuqslMap = new HashMap<String, BigDecimal>();
			// 开始月的工作天数Map,以零件编号为key,开始时间到当月的结束日期的工作天数
			Map<String, Integer> nowgongztsMap = new HashMap<String, Integer>();
			// 零件List集合
			List<String> lingjList = new ArrayList<String>();
			Map<String, String> jihydmMap = new HashMap<String, String>();
			Map<String, BigDecimal> nowCdmap = getAllGongzrts(nowStartofDate,
					nowEndofDate);
			Map<String, BigDecimal> nowEndcdmap = getAllGongzrts(now,
					nowEndofDate);
			Map<String, BigDecimal> laxjxCdmap = getAllGongzrts(
					laxjxStartofDate, laxjxEndofDate);
			Map<String, BigDecimal> laxjxEndCdmap = getAllGongzrts(
					laxjxStartofDate, laxjxsj);
			Map<String,String> wulljMap = getWuljMap(usercenter);
			for (int i = 0; i < nowlingjslList.size(); i++) {
				Map<String, Object> map = nowlingjslList.get(i);
				// 零件编号
				String lingjbh = (String) map.get("LINGJBH");
				// KD需求总量
				BigDecimal sumXuqsl = (BigDecimal) map.get("SUMXUQSL");
				String jihy = (String) map.get("JIHY");
				jihydmMap.put(xqmx.getUsercenter() + ":" + lingjbh, jihy);
				// 查物流路径表
				String dinhck = wulljMap.get(usercenter + ":"
						+ lingjbh);
				if (StringUtils.isEmpty(dinhck)) {
					List<String> msgList = new ArrayList<String>();
					msgList.add(usercenter);
					msgList.add(lingjbh);
					LoginUser loginuser = new LoginUser();
					loginuser.setUsername(username);
					// CommonFun cf = new CommonFun();
					yicbjService.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str30, "", msgList, usercenter,
							lingjbh, loginuser, jismk);
				} else {
					/**
					 * 当前所属周期的一整周期的工作天数 KD件订货仓库的生产日历-工作天数 参数：
					 * 用户中心，产线，需求开始周期的开始日期、需求结束周期的结束日期
					 */
					String rilbc = Const.LAXBANCI;;
					//	rilbc = cgmap.get(xqmx.getUsercenter() + ":" + dinhck);

					BigDecimal nowgongzts = BigDecimal.ZERO;
					Integer gongztsI = 0;
					if(nowCdmap!=null&&!nowCdmap.equals("")&&nowCdmap.get(xqmx.getUsercenter() + ":" + rilbc)!=null&&!"".equals(nowCdmap.get(xqmx.getUsercenter() + ":" + rilbc))){
							nowgongzts = nowCdmap.get(xqmx.getUsercenter() + ":" + rilbc);
							gongztsI = Integer.valueOf(nowgongzts.intValue());
					}else{
						List<String> msgList = new ArrayList<String>();
						msgList.add(usercenter);
						msgList.add(rilbc);
						msgList.add(startXuqsszq);
						msgList.add(nowStartofDate);
						msgList.add(nowEndofDate);
						LoginUser loginuser = new LoginUser();
						loginuser.setUsername(username);
						// CommonFun cf = new CommonFun();
						yicbjService.insertError(Const.YICHANG_LX2,
								Const.YICHANG_LX2_str32, "", msgList,
								usercenter, lingjbh, loginuser, jismk);

					}

					// 单个零件号的CMJ值
					BigDecimal singleCMJ = BigDecimal.ZERO;
					if (nowgongzts.intValue() != 0) {
						// 需求数量/工作天数
						singleCMJ = (new BigDecimal(sumXuqsl.toString()))
								.divide(new BigDecimal(gongztsI), 3,
										BigDecimal.ROUND_HALF_EVEN);
					}
					// 当前时间-该周期结束日期的工作天数
					BigDecimal gongzts = BigDecimal.ZERO;
					if (nowEndcdmap.get(xqmx.getUsercenter() + ":" + rilbc) != null&&!"".equals(nowEndcdmap.get(xqmx.getUsercenter() + ":" + rilbc))) {
						gongzts = nowEndcdmap.get(xqmx.getUsercenter() + ":"
								+ rilbc);
					} else {
						List<String> msgList = new ArrayList<String>();
						msgList.add(usercenter);
						msgList.add(rilbc);
						msgList.add(startXuqsszq);
						msgList.add(nowEndofDate);
						LoginUser loginuser = new LoginUser();
						loginuser.setUsername(username);
						yicbjService.insertError(Const.YICHANG_LX2,
								Const.YICHANG_LX2_str31, "", msgList,
								usercenter, lingjbh, loginuser, jismk);

					}
					// 需求量
					BigDecimal xuql = BigDecimal.ZERO;
					// 单个零件的cmj*(当前时间-该周期结束日期的工作天数)
					xuql = singleCMJ.multiply(gongzts);
					// 设置产线的零件key
					String key = lingjbh + ":" + dinhck;
					// 设置需求数量的map
					nowXuqslMap.put(key, xuql);
					// 设置零件的工作天数
					nowgongztsMap.put(key, gongzts.intValue());
					lingjList.add(key);
				}
			}
			// 释放内存
			nowCdmap.clear();
			nowEndcdmap.clear();
			nowlingjslList.clear();
			// 第二周期的结束日期
			String endXuqsszq = sjMap.get("endXuqsszq");
			laxjxlingjslList = getSumLingsl(xqmx, endXuqsszq);
			// 结束月的需求数量Map，以零件编号为key,需求数量之和为value
			Map<String, BigDecimal> laxjxXuqslMap = new HashMap<String, BigDecimal>();
			// 结束月的工作天数Map,以零件编号为key,结束时间第一天到拉箱界限的工作天数
			Map<String, Integer> laxjxgongztsMap = new HashMap<String, Integer>();
			for (int i = 0; i < laxjxlingjslList.size(); i++) {
				Map<String, Object> map = laxjxlingjslList.get(i);
				// 零件编号
				String lingjbh = (String) map.get("LINGJBH");
				// KD需求总量
				BigDecimal sumXuqsl = (BigDecimal) map.get("SUMXUQSL");
				String jihy = (String) map.get("JIHY");
				String tempKey = xqmx.getUsercenter() + ":" + lingjbh;
				if (!jihydmMap.containsKey(tempKey)) {
					jihydmMap.put(tempKey, jihy);
				}

				String dinhck = (String) wulljMap.get(usercenter + ":"
						+ lingjbh);
				if (StringUtils.isEmpty(dinhck)) {
					List<String> msgList = new ArrayList<String>();
					msgList.add(usercenter);
					msgList.add(lingjbh);
					LoginUser loginuser = new LoginUser();
					loginuser.setUsername(username);
					yicbjService.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str30, "", msgList, usercenter,
							lingjbh, loginuser, jismk);
				} else {
					/**
					 * 跨周期的第二周期的工作天数 KD件订货仓库的生产日历-工作天数 参数：
					 * 用户中心，产线，需求结束周期的开始日期、需求结束周期的结束日期
					 */
					String rilbc = Const.LAXBANCI;
					BigDecimal laxjxgongzts = BigDecimal.ZERO;
					if(laxjxCdmap!=null&&!laxjxCdmap.equals("")&&laxjxCdmap.get(xqmx.getUsercenter()+ ":" + rilbc)!=null&&!"".equals(laxjxCdmap.get(xqmx.getUsercenter()+ ":" + rilbc))){
							laxjxgongzts = laxjxCdmap.get(xqmx.getUsercenter() + ":" + rilbc);
					} else {
						List<String> msgList = new ArrayList<String>();
						msgList.add(usercenter);
						msgList.add(rilbc);
						msgList.add(endXuqsszq);
						msgList.add(laxjxStartofDate);
						msgList.add(laxjxEndofDate);
						LoginUser loginuser = new LoginUser();
						loginuser.setUsername(username);
						yicbjService.insertError(Const.YICHANG_LX2,
								Const.YICHANG_LX2_str32, "", msgList,
								usercenter, lingjbh, loginuser, jismk);
					}
					Integer gongztsI = Integer.valueOf(laxjxgongzts.intValue());
					// 单个零件号的CMJ值
					BigDecimal singleCMJ = BigDecimal.ZERO;
					if (laxjxgongzts.intValue() != 0) {
						// 需求数量/工作天数
						singleCMJ = (new BigDecimal(sumXuqsl.toString()))
								.divide(new BigDecimal(gongztsI), 3,
										BigDecimal.ROUND_HALF_EVEN);
					}
					// 跨周期的结束周期的开始日期-拉限界限结束日期的工作天数
					BigDecimal gongzts = BigDecimal.ZERO;
					if(laxjxEndCdmap!=null&&!laxjxEndCdmap.equals("")&&laxjxEndCdmap.get(xqmx.getUsercenter() + ":" + rilbc)!=null&&!"".equals(laxjxEndCdmap.get(xqmx.getUsercenter() + ":" + rilbc))){
						gongzts = laxjxEndCdmap.get(xqmx.getUsercenter() + ":" + rilbc);
					}else{
						List<String> msgList = new ArrayList<String>();
						msgList.add(usercenter);
						msgList.add(rilbc);
						msgList.add(endXuqsszq);
						msgList.add(laxjxStartofDate);
						msgList.add(laxjxsj);
						LoginUser loginuser = new LoginUser();
						loginuser.setUsername(username);
						yicbjService.insertError(Const.YICHANG_LX2,
								Const.YICHANG_LX2_str32, "", msgList,
								usercenter, lingjbh, loginuser, jismk);
					}
					// 所需需求数量
					BigDecimal xuql = BigDecimal.ZERO;
					xuql = singleCMJ.multiply(gongzts);
					// 零件编号+":"+产线为key
					String key = lingjbh + ":" + dinhck;
					// 设置零件需求量
					laxjxXuqslMap.put(key, xuql);
					// 设置零件工作天数
					laxjxgongztsMap.put(key, gongzts.intValue());
					// 把两个周期之间的零件信息相加，综合为一个list
					if (!lingjList.contains(key)) {
						lingjList.add(key);
					}
				}
			}
			// 释放cgmap
			wulljMap.clear();
			laxjxlingjslList.clear();
			cgmap.clear();
			laxjxCdmap.clear();
			laxjxEndCdmap.clear();
			// 循环所有的零件信息,key值:零件编号+":"+产线
			for (String key : lingjList) {
				// 开始周期的需求量
				BigDecimal xuql = nowXuqslMap.get(key);
				// 结束周期的需求量
				BigDecimal laxjxXuql = laxjxXuqslMap.get(key);
				// 两个周期的需求量之和
				BigDecimal allXuql = BigDecimal.ZERO;
				if (laxjxXuql != null) {
					allXuql = xuql.add(laxjxXuql);
				}
				// 开始周期的工作天数
				Integer nowgongzts = nowgongztsMap.get(key);
				// 结束周期的工作天数
				Integer laxjxgongzts = 0;
				if (laxjxgongztsMap.get(key) != null) {
					laxjxgongzts = laxjxgongztsMap.get(key);
				}
				// 总的工作天数
				Integer gongzts = nowgongzts + laxjxgongzts;
				// 零件cmj值
				BigDecimal cmj = BigDecimal.ZERO;
				if (!gongzts.toString().equals("0")) {
					cmj = new BigDecimal(allXuql.toString()).divide(
							new BigDecimal(gongzts.toString()), 3,
							BigDecimal.ROUND_HALF_EVEN);
				}
				String[] keys = key.split(":");
				String lingjbh = keys[0];
				// 初始化临时表 临时需求表
				LinsXuq lxuq = new LinsXuq();
				// 设置用户中心
				lxuq.setUsercenter(usercenter);
				// 设置毛需求版次
				lxuq.setMaoxqbc(maoxqbc);
				String jihydm = (String) jihydmMap.get(usercenter + ":"
						+ lingjbh);
				// 设置计划员代码
				lxuq.setJihydm(jihydm);
				// 设置零件编号
				lxuq.setLingjh(lingjbh);
				String cmjKey = usercenter + ":" + lingjbh;
				if (lingjcmjMap.get(cmjKey) == null) {
					lingjcmjMap.put(cmjKey, cmj);
					lxuq.setCmj(cmj);
				} else {
					BigDecimal tempcmjs = (BigDecimal) lingjcmjMap.get(cmjKey);
					tempcmjs = tempcmjs.add(cmj);
					lingjcmjMap.put(cmjKey, tempcmjs);
					lxuq.setCmj(tempcmjs);
				}
				lxuq.setManzsl(BigDecimal.ZERO);
				// 设置创建者
				lxuq.setCreator(username);
				// 设置创建时间
				lxuq.setCreateTime(DateUtil.curDateTime());
				linsXuqlist.add(lxuq);
				laxjxGongzrtsMap.put(lingjbh, gongzts);
			}
			lingjList.clear();
		}
		List<LinsXuq> newXuqlist = new ArrayList<LinsXuq>();
		Map<String, Object> keyMap = new HashMap<String, Object>();
		for (LinsXuq xuq : linsXuqlist) {
			String newuc = xuq.getUsercenter();
			String jihy = xuq.getJihydm();
			String lingjbh = xuq.getLingjh();
			String key = newuc + ":" + jihy + ":" + lingjbh;
			keyMap.put(key, xuq);
		}
		linsXuqlist.clear();
		// 获取临时需求List
		for (Map.Entry<String, Object> entry : keyMap.entrySet()) {
			LinsXuq xuq = (LinsXuq) entry.getValue();
			newXuqlist.add(xuq);
		}
		keyMap.clear();
		LaxjhBean bean = new LaxjhBean();
		bean.setLinsxuqList(newXuqlist);
		if(laxjxGongzrtsMap!=null){
			bean.setLaxjxGongzrtsMap(laxjxGongzrtsMap);
		}

		return bean;
	}
	@SuppressWarnings("unchecked")
	public Map<String,String> getWuljMap(String usercenter) {
		Map<String, String> wulMap = new HashMap<String, String>();
		wulMap.put("usercenter", usercenter);
		List<CkxwulljBean> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.getDinghck", wulMap);
		Map<String,String> wulljMap = new HashMap<String,String>();
		for (int i = 0; i < list.size(); i++) {
			CkxwulljBean bean = list.get(i);
			if (bean.getMUDD() != null) {
				wulljMap.put(bean.getUsercenter() + ":" + bean.getLingjbh(),bean.getMUDD());
			}
		}
		return wulljMap;
	}

	/**
	 * 获取需求所属周期零件信息
	 * 
	 * @param xqmx
	 * @param xuqsszq
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSumLingsl(Maoxqmx xqmx, String xuqsszq) {
		xqmx.setXuqsszq(xuqsszq);
		// 获取年-月，查询该月的毛需求
		List<Map<String, Object>> lingjslList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
				"laxjh.getSumlingjsl", xqmx);
		return lingjslList;
	}

	private Map<String, BigDecimal> getAllGongzrts(String nowStartofDate,
			String nowEndofDate) {
		List<Map<String, Object>> calendarList = commonCalendarService
				.getAllGongZts(nowStartofDate, nowEndofDate);
		Map<String, BigDecimal> nowCdmap = new HashMap<String, BigDecimal>();
		for (Map<String, Object> calendarMap : calendarList) {
			Object gongrts = (Object) calendarMap.get("GONGZRTS");
			String calendaruc = (String) calendarMap.get("USERCENTER");
			String banc = (String) calendarMap.get("BANC");
			nowCdmap.put(calendaruc + ":" + banc,
					CommonFun.getBigDecimal(gongrts));
		}
		return nowCdmap;
	}

	/**
	 * 需求类型为周期, 计算周期开始时间和结束时间
	 */
	public Map<String, String> jszqrq(String now, String laxzdddsj) {
		Map<String, String> dateMap = new HashMap<String, String>();
		// 周期起始时间与结束时间
		// 判断计算日期是跨周期
		String rilbc = Const.LAXBANCI;
		Map<String,String> nowtjMap = new HashMap<String,String>();
		nowtjMap.put("rilbc", rilbc);
		nowtjMap.put("riq", now);
		String startGongyzq = this.getNianzq(nowtjMap);
		Map<String,String> endtjMap = new HashMap<String,String>();
		endtjMap.put("rilbc", rilbc);
		endtjMap.put("riq", laxzdddsj);
		String endGongyzq = this.getNianzq(endtjMap);
		if (startGongyzq.compareTo(endGongyzq) != 0) {
			// 将计算开始日期所在工业周期的开始日期存进Map
			Map<String,String> riqtjMap = new HashMap<String,String>();
			riqtjMap.put("rilbc", rilbc);
			riqtjMap.put("nianzq", startGongyzq);
			Map<String,String> gongyzqMap = this.monthOfGongyzq(riqtjMap);
			String monthStartofDate = gongyzqMap.get("STARTRIQ");
			String nowEndofDate =  gongyzqMap.get("ENDRIQ");;
			// 计算开始日期所在工业周期的结束日期
			dateMap.put("nowStartofDate", monthStartofDate);
			// 将计算开始日期所在月的结束日期存进Map
			dateMap.put("nowEndofDate", nowEndofDate);
			Map<String,String> riqEndtjMap = new HashMap<String,String>();
			riqEndtjMap.put("rilbc", rilbc);
			riqEndtjMap.put("nianzq", endGongyzq);
			Map<String,String> gongyzqEndMap = this.monthOfGongyzq(riqEndtjMap);
			String laxjxStartofDate = gongyzqEndMap.get("STARTRIQ");
			String laxjxEndofDate =  gongyzqEndMap.get("ENDRIQ");;
			// 将计算结束日期所在工业周期的开始日期存进Map
			dateMap.put("laxjxStartofDate", laxjxStartofDate);
			// 将计算结束日期所在工业周期的结束日期存进Map
			dateMap.put("laxjxEndofDate", laxjxEndofDate);
			dateMap.put("startXuqsszq", startGongyzq);
			dateMap.put("endXuqsszq", endGongyzq);
		} else {
			// 将计算开始日期所在月的开始日期存进Map
			Map<String,String> riqtjMap = new HashMap<String,String>();
			riqtjMap.put("rilbc", rilbc);
			riqtjMap.put("nianzq", startGongyzq);
			Map<String,String> gongyzqMap = this.monthOfGongyzq(riqtjMap);
			String monthStartofDate = gongyzqMap.get("STARTRIQ");
			String retEnd =  gongyzqMap.get("ENDRIQ");;
			dateMap.put("nowStartofDate", monthStartofDate);
			// 将计算开始日期所在月的结束日期存进Map
			dateMap.put("nowEndofDate", retEnd);
			dateMap.put("xuqsszq", startGongyzq);
		}
		// 将计算开始日期存进Map
		dateMap.put("now", now);
		// 将计算结束日期存进Map
		dateMap.put("laxjxsj", laxzdddsj);
		return dateMap;
	}

	/**
	 * @param nowtjMap
	 * @return
	 */
	private String getNianzq(Map<String,String> nowtjMap) {
		String nianzq = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("laxjh.getCalendarVersionNianzq",nowtjMap);
		return nianzq;
	}

	/**
	 * 求工业周期开始日期
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> monthOfGongyzq(Map<String,String> tjmap) {
		// 设置日期格式
		Map<String,String> map = (Map<String,String>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("laxjh.getStartAndEndriq",tjmap);
		return map;
	}

	/**
	 * 在每次计算拉箱计划之前 删除临时需求
	 * 
	 * @param xuq
	 */
	private void deleteLinsXuq(LinsXuq xuq) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"linsxuq.deleteLinsxuq", xuq);
	}

	/**
	 * 获取零件重箱区库存
	 * 
	 * @param linjgzxqkc
	 * @param zxqkc
	 * @param lingjh
	 * @return
	 */
	public BigDecimal getZxqkcljsl(Map<String,BigDecimal> zxqkc, String lingjh) {
		BigDecimal tempLinjgzxqkc = BigDecimal.ZERO;
		// 根据零件号，获取重箱区库存
		if (zxqkc.get(lingjh) != null) {
			tempLinjgzxqkc = zxqkc.get(lingjh);
		}
		return tempLinjgzxqkc;
	}

	/**
	 * 获取零件库存
	 * 
	 * @param tempSumValues
	 * @param ljkcMap
	 * @param tdjMap
	 * @param lingjh
	 * @return
	 */
	public BigDecimal getLjsls(Map<String,BigDecimal> ljkcMap, Map<String,List<String>> tdjMap, String lingjh) {
		// 零件仓库库存
		// 初始化sumLingjh
		BigDecimal sumLingjh = BigDecimal.ZERO;
		String sumLingjhs = "0";
		if (ljkcMap.get(lingjh) != null) {
			sumLingjh = (BigDecimal) ljkcMap.get(lingjh);// 获取零件数量
			sumLingjhs = sumLingjh.toString();
		}
		BigDecimal tempSumValues = BigDecimal.ZERO;
		tempSumValues = tempSumValues.add(new BigDecimal(sumLingjhs));
		// 获取替代件编号集合
		List<String> tdjListBylingjh = new ArrayList<String>();
		// 判空
		if (tdjMap.get(lingjh) != null) {
			tdjListBylingjh = tdjMap.get(lingjh);
		}
		/**
		 * 遍历替代件零件号，将其替代件库存量获取出来
		 */
		for (int i = 0; i < tdjListBylingjh.size(); i++) {
			String tidljh = (String) tdjListBylingjh.get(i);
			BigDecimal tidljhValue = BigDecimal.ZERO;
			String tidljhValues = "0";
			if (ljkcMap.get(tidljh) != null) {
				tidljhValue = (BigDecimal) ljkcMap.get(tidljh);
				tidljhValues = tidljhValue.toString();
			}
			tempSumValues = tempSumValues.add(new BigDecimal(tidljhValues));
		}
		return tempSumValues;
	}

	/**
	 * @param tempTdjMap
	 * @param allTdjList
	 * @return
	 */
	public Map<String, List<String>> getTdjMap(
			List<Map<String, Object>> allTdjList) {
		Map<String, List<String>> tempTdjMap = new HashMap<String, List<String>>();
		for (int i = 0; i < allTdjList.size(); i++) {
			Map<String, Object> map = allTdjList.get(i);
			String lingjbh = (String) map.get("LINGJBH");// 获取零件编号
			String tldljh = (String) map.get("TIDLJH");// 获取替代件编号
			/**
			 * 判断逻辑：如果tdjMap中的根据零件编号获取的值为空， 则新创建List集合 并将List集合中加入替代件的值
			 * 如果tdjMap中的根据零编号获取的值不为空， 则根据零件编号获取其value值-list集合， 再将新的替代件加入List集合中
			 */
			if (tempTdjMap.get(lingjbh) == null) {
				List<String> values = new ArrayList<String>();
				values.add(tldljh);
				tempTdjMap.put(lingjbh, values);
			} else if (tempTdjMap.get(lingjbh) != null) {
				List<String> values = (List<String>) tempTdjMap.get(lingjbh);
				values.add(tldljh);
				tempTdjMap.put(lingjbh, values);
			}
		}
		return tempTdjMap;
	}

	/**
	 * @param tempAllTdjList
	 * @param laxjh
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAlllTdjList(Laxjh laxjh) {
		// 查询所有替代件
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.getAllTdj", laxjh);
	}

	/**
	 * @param laxjh
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAllLjkc(Laxjh laxjh) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.getAllLjkc", laxjh);
	}

	/**
	 * @param tempYddhljslmap
	 * @param yddhljslList
	 * @return
	 */
	public Map<String, BigDecimal> getljslMap(List<Map<String, Object>> yddhljslList) {
		Map<String, BigDecimal> tempYddhljslmap = new HashMap<String, BigDecimal>();
		// 遍历预定到货计划数量集合
		for (int i = 0; i < yddhljslList.size(); i++) {
			Map<String, Object> map = yddhljslList.get(i);
			String lingjh = (String) map.get("LINGJBH");
			// 零件数量集合
			BigDecimal lingjsl = (BigDecimal) map.get("SUMLINGJSL");
			tempYddhljslmap.put(lingjh, lingjsl);
		}
		yddhljslList.clear();
		return tempYddhljslmap;
	}

	/**
	 * 获取预计到货零件数量List
	 * 
	 * @param tc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getYujdhsl(TC tc) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.getYddhljsl", tc);
	}

	/**
	 * 删除临时表_零件数据
	 * 
	 * @param linsTclj
	 */
	public void deleteLinstclj(LinsTclj linsTclj) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"linstclj.deleteLinstclj", linsTclj);
	}

	/**
	 * 遍历各集装箱中各零件信息
	 * 
	 * @param list
	 *            零件List
	 * @param usercenter
	 *            用户中心
	 * @param username
	 *            用户名
	 */
	public void bianliList(List<Map<String, Object>> list, String usercenter,
			String username) {
		List<LinsTclj> linstcljList = new ArrayList<LinsTclj>();
		Map<String,LinsTclj> tcljmap = new HashMap<String,LinsTclj>();
		for (Map<String, Object> map : list) {
			// 零件编号
			String lingjbh = (String) map.get("LINGJBH");
			//if (maoxqLingjbhList.contains(lingjbh)) {
				// TC号
				String tcNo = (String) map.get("TCNO");
				// 启运时间
				Date qiysj = (Date) map.get("QIYSJ");
				// 目的点
				String mudd = (String) map.get("MUDD");
				// 最新物理点
				String zuiswld = (String) map.get("ZUISWLD");
				// 最新预计到达时间
				Date zuixyjddsj = null;
				if (map.get("ZUIXYJDDSJ") != null) {
					zuixyjddsj = (Date) map.get("ZUIXYJDDSJ");
				}
				// 拉箱指定到达时间
				Date laxzdddsj = null;
				if (map.get("LAXZDDDSJ") != null) {
					laxzdddsj = (Date) map.get("LAXZDDDSJ");
				}

				// 零件数量
				BigDecimal lingjsl = (BigDecimal) map.get("SUMLINGJSL");
				// 初始化临时表 tc零件
				LinsTclj tclj = new LinsTclj();
				// 设置用户中心
				tclj.setUsercenter(usercenter);
				// 设置零件编号
				String jihydm = (String) map.get("JIHY");
				// 设置计划员代码
				tclj.setJihydm(jihydm);
				// 设置TC NO
				tclj.setTcNo(tcNo);
				// 设置启运点时间
				if (qiysj != null) {
					String qiysjs = DateUtil.dateFromat(qiysj, "yyyy-MM-dd");
					tclj.setQiysj(qiysjs);
				}
				// 设置目的地
				tclj.setMudd(mudd);
				// 设置零件号
				tclj.setLingjh(lingjbh);
				// 设置零件数量
				tclj.setLingjsl(lingjsl);
				// 设置物理点
				tclj.setWuld(zuiswld);
				// 设置预计到达时间
				if (zuixyjddsj != null && !"null".equals(zuixyjddsj)) {
					String yjddsjs = DateUtil.dateFromat(zuixyjddsj,
							"yyyy-MM-dd");
					tclj.setYujddsj(yjddsjs);
				}
				// 设置拉箱指定到达时间
				if (laxzdddsj != null && !"null".equals(laxzdddsj)) {
					String laxzdddsjs = DateUtil.dateFromat(laxzdddsj,
							"yyyy-MM-dd");
					tclj.setLaxzdddsj(laxzdddsjs);
				}
				//创建者
				tclj.setCreator(username);
				//创建时间
				String createTime = DateUtil.curDateTime();
				tclj.setCreateTime(createTime);
				//判断计划员代码是是否为空，不为空，则设置零件信息;为空则将数据插入异常报警表
				if (tclj.getJihydm() != null && !"".equals(tclj.getJihydm())) {
					String key = tclj.getUsercenter()+":"+tclj.getJihydm()+":"+tclj.getTcNo()+":"+tclj.getLingjh();
					if(tcljmap.get(key)==null){
						tcljmap.put(key, tclj);
					}else{
						LinsTclj _tclj = (LinsTclj)tcljmap.get(key);
						lingjsl = lingjsl.add(_tclj.getLingjsl());
						_tclj.setLingjsl(lingjsl);
						tcljmap.put(key, _tclj);
					}
				} else {
					this.yicbjService.saveYicInfo("61", tclj.getLingjh(),
							"200", tclj.getLingjh() + "零件计划员代码为空");
				}
			}

		//}
		for(Entry<String,LinsTclj> entry : tcljmap.entrySet()){
			linstcljList.add(entry.getValue());
		}
		// 调用插入临时零件方法;
		if (linstcljList.size() < 500) {
			this.insertLinsTclj(linstcljList);
		} else {
			//分段批量插入，以500为一次插入，循环进行
			List<LinsTclj> templist = new ArrayList<LinsTclj>();
			for (int i = 0; i < linstcljList.size(); i++) {
				if (i % 500 == 0) {
					if (templist.size() > 0) {
						this.insertLinsTclj(templist);
					}
					templist = new ArrayList<LinsTclj>();
				}
				templist.add(linstcljList.get(i));
			}
			//批量插入余下的数据
			if (templist.size() > 0) {
				this.insertLinsTclj(templist);
			}
		}

	}

	/**
	 * 插入临时需求
	 * 
	 * @param list
	 */
	public void insertLinsXuq(List<LinsXuq> list) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"linsxuq.insertLinsxuq", list);
	}

	/**
	 * 获取重箱区库存
	 * 
	 * @param zxqkcMap
	 * @param zxqkcList
	 * @return
	 */
	public Map<String, BigDecimal> getZxqkc(List<Map<String, Object>> zxqkcList) {
		Map<String, BigDecimal> zxqkcMap = new HashMap<String, BigDecimal>();
		// 遍历重箱区库存
		for (Map<String, Object> map : zxqkcList) {
			// 零件号
			String lingjh = (String) map.get("LINGJH");
			// 零件数量
			BigDecimal sumlingjsl = (BigDecimal) map.get("SUMLINGJSL");
			// 设置重箱区零件号与零件数量对应
			zxqkcMap.put(lingjh, sumlingjsl);
		}
		return zxqkcMap;
	}

	/**
	 * 插入临时TC零件数据
	 * 
	 * @param linsTcljList
	 */
	public void insertLinsTclj(List<LinsTclj> linsTcljList) {
		// 循环遍历插入临时零件
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"linstclj.insertLinsTclj", linsTcljList);
	}

	/**
	 * 根据用户中心查出重箱区库存
	 * 
	 * @param usercenter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getZxqkcByUc(String usercenter) {
		List<Map<String, Object>> sumzxqkc = new ArrayList<Map<String, Object>>();
		// 初始化集装箱
		TC tc = new TC();
		// 定义物理点
		String zuiswld = "";
		// 根据用户中心设置其重箱区
		if (usercenter.equals("UW")) {
			zuiswld = "107";
		} else if (usercenter.equals("UL")) {
			zuiswld = "108";
		}else if(usercenter.equals("UX")){
			zuiswld = "15A";
		}
		// 设置其物理点
		tc.setZuiswld(zuiswld);
		// 查询零件数量之和
		try {
			sumzxqkc = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("tc.getSumzxqkc", tc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sumzxqkc;
	}

	/**
	 * 计算拉箱计划 计算步骤： 1、根据用户中心，清空临时表 可用TC，可以TC零件明细
	 * 2、取得《拉箱计算参数设置》画面上设置的各计算参数:用户中心、安全库存、拉箱范围、计算策略
	 * 3、将"报警零件列表"画面修改结果，更新到"临时表 临时需求"，如果未修改，则不需要进行保存
	 * 4、取得"临时表临时需求",如果该表中没有记录，则表示未来没有需要拉箱计算的短缺的KD零件，后续计算处理终止，有记录则计算继续
	 * 5、取得可用TC号信息，并更新到"临时表 可用TC"、"临时表 可用TC零件明细"
	 * 6、计算每个集装箱对短缺零件的"满意度"；并更新"临时表_可用TC明细"的满意度
	 * 7、根据"临时表_可用TC明细"，计算集装箱的满意度，并将结果更新到"临时表_可用TC"
	 * 8、根据计划员指定的计算策略(品种满足优先/滞箱时间优先),从"临时表 可用TC"中依次选取最能满足计算策略的集装箱，生成拉箱计划
	 * 9、生成"拉箱/开箱计划未满足零件列表"
	 * 
	 * @param xqmx
	 * @param bean
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String jsLaxjh(LinsXuq linsXuq, Laxjh bean, LoginUser loginUser,
			String wuldgk) throws ParseException {
		// 根据用户中心清空临时表 可有TC,可用TC零件明细
		// 删除条件值
		String tjValue = bean.getUsercenter();
		// 初始化可用临时零件
		LinsKeytc keytc = new LinsKeytc();
		// 设置用户中心
		keytc.setUsercenter(tjValue);
		// 删除XQJS_LINS_KEYTC
		this.deleteLinsKeyTc(keytc);
		// 删除XQJS_LINS_KEYTCLJ
		LinsKeytclj keytclj = new LinsKeytclj();
		keytclj.setUsercenter(tjValue);
		this.deleteLinsKeyTclj(keytclj);
		// 获取"临时表临时需求"
		List<LinsXuq> list = new ArrayList<LinsXuq>();
		// 获取零件报警数据
		String now = DateUtil.getCurrentDate();
		String nowAdd1 = DateUtil.dateAddDays(now, 1);
		linsXuq.setDuandsj(this.getDuandsj(new BigDecimal("1"), nowAdd1));
		list = this.getLingjbj(linsXuq);
		int size = list.size();
		String msg = "1";
		// 用户中心
		String usercenter = bean.getUsercenter();
		// 拉箱界限天数
		BigDecimal laxjx = bean.getLaxjx();
		String currentDay = DateUtil.getCurrentDate();
		String zuixyjddsj = DateUtil.dateAddDays(currentDay,
				laxjx.intValue() - 1);// 拉箱界限时间
		Map<String, String> tjmap = new HashMap<String, String>();
		tjmap.put("suanfcl", bean.getSuanfcl());
		tjmap.put("zuixyjddsj", zuixyjddsj);
		tjmap.put("wuld", wuldgk);
		// 资源天数
		BigDecimal ziyts = BigDecimal.ZERO;
		// 当资源天数!=空时，取资源天数的值；当资源天数==空时，资源天数取拉箱界限的值；
		if (bean.getZiyts() != null) {
			ziyts = bean.getZiyts();
		} else {
			ziyts = laxjx;
		}
		String ziysj = "";
		if(bean.getLaxbs().equals("2")){
			ziysj = DateUtil.dateAddDays(now, ziyts.intValue() - 1);
			tjmap.put("ziysj", ziysj);
		}
		if (size == 0) {
			msg = "0";
		} else if (size > 0) {
			// 获取"临时表 TC零件"
			LaxjhBean laxjhBean = this.getBeans(list, tjValue, tjmap);
			List<LinsTclj> keyTcljList = new ArrayList<LinsTclj>();
			// 获取可用TC零件List
			keyTcljList = laxjhBean.getKeyTcljList();
			Map<String,String> duanqljsjMap = laxjhBean.getDuanqljsjMap();
			if (keyTcljList.size() > 0) {
				// 获取短缺零件Map
				Map<String,String> duanqlingjMap = laxjhBean.getDuanqlingjMap();
				// 初始化TC零件集合
				List<Map<String, Object>> lingjslList = new ArrayList();
				// 初始化零件零件
				LinsTclj linsTclj = new LinsTclj();
				// 设置用户中心
				linsTclj.setUsercenter(usercenter);
				linsTclj.setZuixyjddsj(zuixyjddsj);
				linsTclj.setZiysj(ziysj);
				// 获取TCLJ零件集合
				lingjslList = this.getSumLingjslByTc(linsTclj);
				// 初始化满意度MAP
				Map<String,BigDecimal> manydMap = new HashMap<String,BigDecimal>();
				// 初始化TC零件MAP
				Map<String,List<String>> tcLingjMap = new HashMap<String,List<String>>();
				List<String> tcNoList = new ArrayList<String>();
				List<String> duanqljList = new ArrayList<String>();
				String username = loginUser.getUsername();
				LaxjhBean lBean = this.getBeansBysumLingjslList(lingjslList,
							duanqlingjMap, bean, wuldgk, username,msg,duanqljsjMap);
				if(lBean.getMsg().equals("")){
					manydMap = lBean.getManydMap();
					tcLingjMap = lBean.getTcLingjMap();
					tcNoList = lBean.getTcNoList();
					duanqljList = lBean.getDuanqljList();
					Map minduandsjmap = lBean.getMinduandsjmap();
					// 初始化可用TC集合
					Map newTcMap = new HashMap();
					// 获取滞箱天数
					List<Map<String, Object>> zxtsList = new ArrayList();
					// 获取滞箱天数List
					zxtsList = this.getZxts(wuldgk);
					// 初始化滞箱天数Map，以TC号为key,以滞箱天数为map
					Map zxtsMap = new HashMap();
					zxtsMap = this.getZxtsMap(zxtsList);
					// 插入临时TC和临时零件
					this.insertKeytcAndlj(tcNoList,keyTcljList, username, newTcMap, zxtsMap);
					// 更新零件满意度
					this.updateKeytcAndlj(tcNoList, tcLingjMap, manydMap, username);
					// 根据计算策略，生成拉箱计划和"拉箱未满足零件列表"
					Laxjh laxjh = new Laxjh();
					// 获取用户中心
					String jhUc = bean.getUsercenter();
					// 获取毛需求版次
					String jhmaoxbc = linsXuq.getMaoxqbc();
					// 获取计算算法策略
					String jhSuanfcl = bean.getSuanfcl();
					// 获取拉箱界限
					BigDecimal jhLaxjx = bean.getLaxjx();
					// 获取计划安全天数
					BigDecimal jhAnqkc = BigDecimal.ZERO;
					if (bean.getAnqkc() != null) {
						jhAnqkc = bean.getAnqkc();
					}
					String creator = username;
					String createTime = DateUtil.curDateTime();
					// 获取拉箱计划号
					Calendar calendar = new GregorianCalendar();
					int year = calendar.get(Calendar.YEAR);
					String tempjhh = usercenter + "L" + year;
					// 获取数据库中最大的拉箱计划号
					// 获取拉箱计划号
					String laxjhh = "";
					String laxjhhs = "";
					String templaxjhh = this.getLaxjhh(tempjhh);
					if (templaxjhh == null || "".equals(templaxjhh)) {
						laxjhhs = "000";
					} else {
						// 获取其长度
						int length = templaxjhh.length();
						// 获取其后4位
						String temp = templaxjhh.substring(length - 3, length);
						// 去掉4位中的前面的零
						String newString = "";
						if (temp.equals("000")) {
							newString = "0";
						} else {
							newString = LaxjhService.getNewString(temp);
						}
						// 变成整型+1;
						Integer laxjhhI = Integer.valueOf(newString) + 1;
						// 然后将其变成3位字符串，不够三位补零
						laxjhhs = String.format("%03d", laxjhhI);
					}
					// 获取拉箱计划号
					laxjhh = tempjhh + laxjhhs;
					// 设置拉箱计划号
					laxjh.setLaxjhh(laxjhh);
					// 设置用户中心
					laxjh.setUsercenter(jhUc);
					// 设置版次
					laxjh.setBanc(jhmaoxbc);
					// 设置算法策略
					laxjh.setSuanfcl(jhSuanfcl);
					// 设置拉箱界限
					laxjh.setLaxjx(jhLaxjx);
					// 设置安全库存
					laxjh.setAnqkc(jhAnqkc);
					laxjh.setCreator(creator);
					laxjh.setCreateTime(createTime);
					laxjh.setJihy(loginUser.getJihyz());
					laxjh.setLaxbs(bean.getLaxbs());
					laxjh.setZiyts(bean.getZiyts());
					// 设置生效状态
					laxjh.setShengxzt("0");
					// 插入拉箱计划
					this.insertLaxjh(laxjh);
					laxjh.setKyljdm(bean.getKyljdm());
					List<Map<String, Object>> tcManzlist = new ArrayList();
					// 根据用户条件设置，进行可用KEYTC的集装箱排序
					tcManzlist = this.getKeytcBySuanfcl(bean);
					List<String> newTcKeyList = new ArrayList();
					// 生成可用TC的新key，用于更新可用集装箱keyTc的循环更新
					newTcKeyList = this.getNewTcKeyList(tcManzlist);
					// 获取该用户中心下的可用零件明细
					List<LinsKeytclj> newKeyTcljList = new ArrayList();
					newKeyTcljList = this.getNewKeyTcljList(bean);
					List<String> keyList = new ArrayList();
					Map tcNoKeytcljMap = new HashMap();
					LaxjhBean ljhBean = this.getTcljInfo(newKeyTcljList);
					keyList = ljhBean.getKeyList();
					tcNoKeytcljMap = ljhBean.getTcNoKeytcljMap();
					// 该MAP用来存key与满足数量的满意度
					Map tcljManzslMap = new HashMap();
					List laxjhmxTcList = new ArrayList();
					// 根据上一步取得的可用集装箱的"TC"号,从"临时表 临时需求"和"临时表 可用TC"中取得集装箱相关信息插入到"拉箱计划明细"
					LaxjhBean newLaxjhBean = this.getTcInfo(keyList, duanqlingjMap,
							tcNoKeytcljMap, duanqljList);
					tcljManzslMap = newLaxjhBean.getTcljManzslMap();
					laxjhmxTcList = newLaxjhBean.getLaxjhmxTcList();
					List<Map<String, Object>> newKeyTcList = new ArrayList();
					Map suanfclMap = new HashMap();
					suanfclMap.put("suanfcl", jhSuanfcl);
					newKeyTcList = this.getSelectLaxjhKeytc(suanfclMap);
					// 插入拉箱计划明细
					this.insertLaxjhmx(newKeyTcList, laxjhmxTcList, laxjh,minduandsjmap);
					// 更新"临时表_可用TC"中的"TC状态"
					this.updateKeyTcbyzt(newTcKeyList, username);
					// 更新"临时表 临时需求"的满意数量:"满足数量"="满足数量"+"临时表  可用TC零件明细" TC中的"零件数量"
					this.updateLinsxuq(keyList, tcljManzslMap, linsXuq, username);
					// 将未满足数量插入到拉箱未满足零件列表
					List weimzlist = this.getWeimzljInfo(usercenter);
					// 插入未满足数量
					this.insertWeimzlj(weimzlist, laxjhh, username);
				}else{
					msg = lBean.getMsg();
				}
			} else {
				msg = "2";
			}
		}
		return msg;
	}

	/**
	 * 插入未满足数量 判断逻辑： 将"临时表 临时需求"中"需求数量-满足数量>0"的数据插入到"拉箱未满足零件表"中
	 * 
	 * @param weimzlist
	 */
	public void insertWeimzlj(List<Weimzlj> weimzlist, String jihNo,
			String username) {
		List<Weimzlj> list = new ArrayList<Weimzlj>();
		for (Weimzlj weimzlj : weimzlist) {
			weimzlj.setJihNo(jihNo);
			weimzlj.setCreateTime(DateUtil.curDateTime());
			list.add(weimzlj);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"weimzlj.insertWeimzlj", list);
	}

	/**
	 * 获取未满足零件信息
	 * 
	 * @param usercenter
	 */
	public List getWeimzljInfo(String usercenter) {
		// 初始化临时需求
		LinsXuq xuq = new LinsXuq();
		// 设置用户中心
		xuq.setUsercenter(usercenter);
		// 获取临时需求表中未满足需求的零件信息
		List<Map<String, Object>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
				"linsxuq.getWeimzljInfo", xuq);
		// 初始化未满足零件List
		List weimzlist = new ArrayList();
		/**
		 * List中所存为map，要求将HashMap中的值转为化Object对象
		 */
		for (Map<String, Object> map : list) {
			// 用户中心
			String uc = (String) map.get("USERCENTER");
			// 零件号
			String lingjh = (String) map.get("LINGJH");
			// 目的地
			String mudd = (String) map.get("MUDD");
			// 断点时间
			String duandsj = (String) map.get("DUANDSJ");
			// 仓库库存
			BigDecimal cangkkc = (BigDecimal) map.get("CANGKKC");
			// 重箱区库存
			BigDecimal zhongxqkc = (BigDecimal) map.get("ZHONGXQKC");
			// 未满足数量
			BigDecimal weimzsl = (BigDecimal) map.get("WEIMZSL");
			// 初始化未满足零件实体Bean
			Weimzlj weimzlj = new Weimzlj();
			// 设置用户中心
			weimzlj.setUsercenter(uc);
			// 设置零件号
			weimzlj.setLingjh(lingjh);
			// 设置目的地
			weimzlj.setMudd(mudd);
			// 设置断点时间
			weimzlj.setDuandsj(duandsj);
			// 仓库库存
			weimzlj.setCangkkc(cangkkc);
			// 重箱区库存
			weimzlj.setZhongxqkc(zhongxqkc);
			// 未满足数量
			weimzlj.setWeimzsl(weimzsl);
			weimzlist.add(weimzlj);
		}
		return weimzlist;
	}

	/**
	 * 获取目前数据库中最大拉箱计划号
	 * 
	 * @param tempjhh
	 * @return
	 */
	public String getLaxjhh(String tempjhh) {
		// 初始化Map
		Map jhhMap = new HashMap();
		jhhMap.put("laxjhh", tempjhh);
		Map map = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("laxjh.getLaxjhh", jhhMap);
		return (String) map.get("LAXJHH");
	}

	/**
	 * 更新临时需求表
	 * 
	 * @param keyList
	 * @param linsXuq
	 * @param username
	 */
	public void updateLinsxuq(List<String> keyList, Map tcljManzslMap,
			LinsXuq linsXuq, String username) {
		// 更新临时需求表中的满足数量
		List list = new ArrayList();
		for (String key : keyList) {
			String[] keys = key.split(":");
			// 用户中心
			String tempUc = keys[0];
			// 计划员代码
			String tempJihydm = keys[1];
			// 零件号
			String tempLingjh = keys[3];
			String tempKey = tempUc + ":" + tempJihydm + ":" + tempLingjh;
			// 实始化满足数量
			BigDecimal manzsl = BigDecimal.ZERO;
			// 临时需求实体BEAN初始化
			LinsXuq linsxuq = new LinsXuq();
			// 用户中心
			linsxuq.setUsercenter(tempUc);
			// 计划员代码
			linsxuq.setJihydm(tempJihydm);
			// 毛需求版次
			//linsxuq.setMaoxqbc(linsXuq.getMaoxqbc());
			// 零件号
			linsxuq.setLingjh(tempLingjh);
			// 获取满足数量
			if (tcljManzslMap.get(tempKey) != null) {
				manzsl = (BigDecimal) tcljManzslMap.get(tempKey);
			}
			// 设置满足数量
			linsxuq.setManzsl(manzsl);
			linsxuq.setEditor(username);
			linsxuq.setEditTime(DateUtil.curDateTime());
			list.add(linsxuq);
		}
		batchUpdatelinsxuqBymzsl(list);
	}

	/**
	 * 批量更新临时需求中的满足数量
	 * 
	 * @param list
	 */
	public void batchUpdatelinsxuqBymzsl(List list) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"linsxuq.updateLinsxuqByManzsl", list);
	}

	/**
	 * 更新临时 可用TC中的TC记录的状态为2，拉箱预定
	 * 
	 * @param newTcKeyList
	 * @param tcManydMap
	 */
	public void updateKeyTcbyzt(List<String> newTcKeyList, String username) {
		List list = new ArrayList();
		for (String key : newTcKeyList) {
			String[] keys = key.split(":");
			String tempUc = keys[0];
			String tempJihydm = keys[1];
			String tempTcNo = keys[2];
			// 初始化临时可用TC
			LinsKeytc tc = new LinsKeytc();
			tc.setUsercenter(tempUc);
			tc.setJihydm(tempJihydm);
			tc.setTcNo(tempTcNo);
			// TC状态为2，为拉箱预定
			tc.setTczt("2");
			tc.setEditor(username);
			tc.setEditTime(DateUtil.curDateTime());
			list.add(tc);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"linskeytc.updateKeytcByZt", list);
	}

	/**
	 * 插入拉箱计划明细
	 * @throws ParseException 
	 */
	@Transactional
	public void insertLaxjhmx(List<Map<String, Object>> newKeyTcList,
			List laxjhmxTcList, Laxjh laxjh,Map minduandsjmap) throws ParseException {
		List<Map<String, String>> tcIdList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("tc.getTcIdList");
		Map<String, String> tcIdmap = new HashMap<String, String>();
		for (int i = 0; i < tcIdList.size(); i++) {
			Map<String, String> map = tcIdList.get(i);
			String id = map.get("ID");
			String tcNo = map.get("TCNO");
			tcIdmap.put(tcNo, id);
		}
		List list = new ArrayList();
		List tempList = new ArrayList();
		for (Map map : newKeyTcList) {
			// 用户中心
			String keyTcuc = (String) map.get("USERCENTER");
			// TC号
			String keyTcno = (String) map.get("TCNO");
			// 启运时间
			Date keyTcQiysj = (Date) map.get("QIYSJ");
			// TC拉箱指定到哒时间
			//Date keyTcLaxzdsj = DateUtil.stringToDateYMD(DateUtil.dateAddDays(duandsj, -1));
			Date keyTcLaxzdsj = DateUtil.stringToDateYMD((String)minduandsjmap.get(keyTcno));
			// 物理点
			String wuld = (String) map.get("WULD");
			// ID
			String id = (String) tcIdmap.get(keyTcno);
			String tempInfo = keyTcuc + ":" + keyTcno;
			if (laxjhmxTcList.contains(tempInfo)) {
				// 初始化拉箱计划明细
				Laxjhmx laxjhmx = new Laxjhmx();
				// 拉箱计划号
				laxjhmx.setLaxjhNo(laxjh.getLaxjhh());
				// 用户中心
				laxjhmx.setUsercenter(keyTcuc);
				laxjhmx.setId(id);
				// 可用TC号
				laxjhmx.setTcNo(keyTcno);
				// 物理点
				laxjhmx.setWuld(wuld);
				// 启运时间
				if(keyTcQiysj!=null){
					laxjhmx.setQiysj(DateUtil.dateFromat(keyTcQiysj, "yyyy-MM-dd"));
				}
				if(keyTcLaxzdsj!=null){
					laxjhmx.setLaxzdsj(DateUtil.dateFromat(keyTcLaxzdsj,"yyyy-MM-dd"));
				}
				// TC状态为2，为拉箱预定
				laxjhmx.setZhuangt("2");
				laxjhmx.setCreator(laxjh.getCreator());
				laxjhmx.setCreateTime(laxjh.getCreateTime());
				laxjhmx.setKyljdm(laxjh.getKyljdm());
				String key = keyTcuc +":"+id+":"+keyTcno;
				if(!tempList.contains(key)){
					list.add(laxjhmx);
				}
				tempList.add(key);
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("laxjhmx.insertLaxjhmx", list);
	}

	/**
	 * 获取拉箱计划可用TC 查询字段:
	 * 1、USERCENTER;2、TCNO；3、QIYSJ;4、ID；5、LAXZDDDSJ(需求中最小断点时间-1) 查询表:
	 * XQJS_LINS_KEYTCLJ;XQJS_TC;XQJS_LINS_XUQ 根据计算算法策略进行排序：
	 * 1、算法策略为1,则按照TCMYD进行排序 2、算法策略为2，则按照ZHIXSJ进行排序
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getSelectLaxjhKeytc(Map suanfclMap) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.selectLaxjhKeytc", suanfclMap);
	}

	/**
	 * 获取可用TC信息 该功能为过滤箱子的主要算法 逻辑: 1、零件短缺数量大于满足数量 2、箱子中该零件的数量不为零
	 * 
	 * @param keyList
	 * @param duanqlingjMap
	 * @param tcNoKeytcljMap
	 * @return
	 */
	public LaxjhBean getTcInfo(List<String> keyList, Map duanqlingjMap,
			Map tcNoKeytcljMap, List duanqljList) {
		Map tcljManzslMap = new HashMap();
		List laxjhmxTcList = new ArrayList();
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();

		for (String key : keyList) {
			/**
			 * key：以usercenter+":"+jihy+":"+"tcNo"+"lingjbh"
			 */
			String[] keys = key.split(":");
			String tempUsercenter = keys[0];
			String tempJihy = keys[1];
			String tempLingjh = keys[3];
			/**
			 * 以usercenter+":"+jihy+":"+lingjbh为key，获取零件的短缺数量
			 */
			String tempKey = tempUsercenter + ":" + tempJihy + ":" + tempLingjh;
			BigDecimal duanqsl = BigDecimal.ZERO;
			if (duanqlingjMap.get(tempLingjh) != null) {
				duanqsl = (BigDecimal) duanqlingjMap.get(tempLingjh);
			}
			LinsKeytclj tempTclj = new LinsKeytclj();
			if (tcNoKeytcljMap.get(key) != null) {
				tempTclj = (LinsKeytclj) tcNoKeytcljMap.get(key);
			}
			String tempTcKey = tempTclj.getUsercenter() + ":"
					+ tempTclj.getTcNo();
			String tempTcKeyLingj = tempUsercenter + ":" + tempJihy +":"+tempTclj.getTcNo()+ ":" + tempLingjh;
			// 初始化TC满意度
			BigDecimal sumLingjsls = BigDecimal.ZERO;
			if (tcljManzslMap.get(tempKey) != null) {
				sumLingjsls = (BigDecimal) tcljManzslMap.get(tempKey);
			}
			/**
			 * 判断逻辑: 如果缺短数量大于满足数量， 则将其可用TC零件信息key加入laxjhmxTcList中， 表示该集装箱为可用
			 */
			if (duanqsl.compareTo(sumLingjsls) == 1) {
				if (tcljManzslMap.get(tempKey) == null) {
					BigDecimal slValue = BigDecimal.ZERO;
					if (duanqljList != null && !duanqljList.contains(tempTcKeyLingj)) {
						slValue = tempTclj.getLingjsl();
						if(slValue==null){
							slValue = BigDecimal.ZERO;
						}
					}
					sumLingjsls = sumLingjsls.add(new BigDecimal(slValue
							.toString()));
					tcljManzslMap.put(tempKey, sumLingjsls);
					map.put(tempKey, slValue);
				} else if (tcljManzslMap.get(tempKey) != null) {
					BigDecimal slValue = BigDecimal.ZERO;
					if (duanqljList != null && !duanqljList.contains(tempTcKeyLingj)) {
						slValue = tempTclj.getLingjsl();
					}
					map.put(tempKey, slValue);
					sumLingjsls = sumLingjsls.add(new BigDecimal(slValue
							.toString()));
					tcljManzslMap.put(tempKey, sumLingjsls);
				}
				BigDecimal lingjsl = BigDecimal.ZERO;
				if (map.get(tempKey) != null) {
					lingjsl = (BigDecimal) map.get(tempKey);
				}
				if (lingjsl.compareTo(BigDecimal.ZERO) == 1) {
					laxjhmxTcList.add(tempTcKey);
				}
			}
		}
		LaxjhBean bean = new LaxjhBean();
		bean.setTcljManzslMap(tcljManzslMap);
		bean.setLaxjhmxTcList(laxjhmxTcList);
		return bean;
	}

	/**
	 * 插入拉箱计划
	 * 
	 * @param laxjh
	 */
	public void insertLaxjh(Laxjh laxjh) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"laxjh.insertLaxjh", laxjh);
	}

	/**
	 * 获取用户中心下可用零件明细的集合
	 * 
	 * @param bean
	 * @return
	 */
	public List<LinsKeytclj> getNewKeyTcljList(Laxjh bean) {
		List newKeyTcljList = new ArrayList();
		Map tjMap = new HashMap();
		tjMap.put("usercenter", bean.getUsercenter());
		tjMap.put("suanfcl", bean.getSuanfcl());
		newKeyTcljList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linskeytclj.selectKeyTclj", tjMap);
		return newKeyTcljList;
	}

	/**
	 * 将LinsKeytclj信息拆分，以UC、计划员代码、 TC号、零件号为key 并且将key与LinsKeytclj放入map中
	 * 
	 * @param newKeyTcljList
	 * @return
	 */
	public LaxjhBean getTcljInfo(List<LinsKeytclj> newKeyTcljList) {
		List keyList = new ArrayList();
		Map tcNoKeytcljMap = new HashMap();
		for (LinsKeytclj keytclj : newKeyTcljList) {
			String uc = keytclj.getUsercenter();
			String jihydm = keytclj.getJihydm();
			String tcNo = keytclj.getTcNo();
			String lingjh = keytclj.getLingjh();
			String key = uc + ":" + jihydm + ":" + tcNo + ":" + lingjh;
			keyList.add(key);
			tcNoKeytcljMap.put(key, keytclj);
		}
		LaxjhBean bean = new LaxjhBean();
		bean.setKeyList(keyList);
		bean.setTcNoKeytcljMap(tcNoKeytcljMap);
		return bean;
	}

	/**
	 * 可用KEYTC集合
	 * 
	 * @param tcManzlist
	 * @return
	 */
	public List getNewTcKeyList(List<Map<String, Object>> tcManzlist) {
		List newTcKeyList = new ArrayList();
		for (Map map : tcManzlist) {
			String tempUsercenter = (String) map.get("USERCENTER");
			String tempJihydm = (String) map.get("JIHYDM");
			String tcNo = (String) map.get("TCNO");
			String key = tempUsercenter + ":" + tempJihydm + ":" + tcNo;
			newTcKeyList.add(key);
		}
		return newTcKeyList;
	}

	/**
	 * 根据算法策略获取可用TC信息
	 * 
	 * @param para
	 * @return
	 */
	public List getKeytcBySuanfcl(Laxjh laxjh) {
		List tcManzlist = new ArrayList();
		tcManzlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.getKeytcBySuanfcl", laxjh);
		return tcManzlist;
	}

	/**
	 * 插入可用TC和可用TC明细
	 * 
	 * @param keyTcljList
	 * @param username
	 * @param newTcMap
	 * @param zxtsMap
	 */
	public void insertKeytcAndlj(List tcNoList,List<LinsTclj> keyTcljList, String username,
			Map newTcMap, Map zxtsMap) {
		// keyTcljList为零件可用集合
		List batchTcList = new ArrayList();
		List batchTcljList = new ArrayList();
		for (LinsTclj newlinsTclj : keyTcljList) {
			LinsKeytclj keyTclj = new LinsKeytclj();
			// 用户中心
			String keyTcljuc = newlinsTclj.getUsercenter();
			// 计划员代码
			String keyTcljjidm = newlinsTclj.getJihydm();
			// TC号
			String keyTcljtcNo = newlinsTclj.getTcNo();
			// 启运时间
			String keyTcljqiysj = newlinsTclj.getQiysj();
			// 零件号
			String keyTcljlingjh = newlinsTclj.getLingjh();
			// 零件数量
			BigDecimal keyTcljlingjsl = newlinsTclj.getLingjsl();
			// 物理点
			String keyTcljwuld = newlinsTclj.getWuld();
			// 预计到达时间
			String keyTcljyjddsj = newlinsTclj.getYujddsj();
			// 创建者
			String creator = username;
			// 创建时间
			String createTime = DateUtil.curDateTime();
			keyTclj.setUsercenter(keyTcljuc);
			keyTclj.setJihydm(keyTcljjidm);
			keyTclj.setTcNo(keyTcljtcNo);
			String qiysj = "";
			if(keyTcljqiysj!=null&&!"".equals(keyTcljqiysj)){
				qiysj = keyTcljqiysj.substring(0,keyTcljqiysj.length() - 2);
			}
			keyTclj.setQiysj(qiysj);
			keyTclj.setLingjh(keyTcljlingjh);
			keyTclj.setLingjsl(keyTcljlingjsl);
			keyTclj.setWuld(keyTcljwuld);
			if(keyTcljyjddsj!=null&&!"".equals(keyTcljyjddsj)){
				keyTclj.setYujddsl(keyTcljyjddsj.substring(0,keyTcljyjddsj.length() - 2));
			}
			keyTclj.setCreator(creator);
			keyTclj.setCreateTime(createTime);
			String key = keyTcljuc + ":" + keyTcljjidm + ":" + keyTcljtcNo;
			/**
			 * 新增可用TC 判断：将keytclj放入map中，以用户中心+":"+计划员代码+":"+TC号为key，如果map中存在该key
			 * ，则表示该TC号已经添加进数据库 不存在，则为新tc
			 */
			if (!newTcMap.containsKey(key)&&tcNoList.contains(key)) {
				LinsKeytc linsKeyTc = new LinsKeytc();
				linsKeyTc.setUsercenter(keyTcljuc);
				linsKeyTc.setJihydm(keyTcljjidm);
				linsKeyTc.setTcNo(keyTcljtcNo);
				linsKeyTc.setQiysj(qiysj);
				BigDecimal zyts = BigDecimal.ZERO;
				if (zxtsMap.get(keyTcljtcNo) != null) {
					zyts = (BigDecimal) zxtsMap.get(keyTcljtcNo);
				}
				linsKeyTc.setZhixsj(new BigDecimal(zyts.intValue()));
				linsKeyTc.setCreator(creator);
				linsKeyTc.setCreateTime(createTime);
				batchTcList.add(linsKeyTc);
				// this.insertKeyTC(linsKeyTc);
				newTcMap.put(key, newlinsTclj);
			}
			batchTcljList.add(keyTclj);
			// this.insertKeyTclj(keyTclj);
		}
		this.insertKeyTC(batchTcList);
		this.insertKeyTclj(batchTcljList);
	}

	/**
	 * 插入可用TC和可用TC明细
	 * 
	 * @param keyTcljList
	 * @param username
	 * @param newTcMap
	 * @param zxtsMap
	 */
	public void insertKaixKeytcAndlj(List<LinsTclj> keyTcljList, String username,
			Map newTcMap, Map zxtsMap) {
		// keyTcljList为零件可用集合
		List batchTcList = new ArrayList();
		List batchTcljList = new ArrayList();
		for (LinsTclj newlinsTclj : keyTcljList) {
			LinsKeytclj keyTclj = new LinsKeytclj();
			// 用户中心
			String keyTcljuc = newlinsTclj.getUsercenter();
			// 计划员代码
			String keyTcljjidm = newlinsTclj.getJihydm();
			// TC号
			String keyTcljtcNo = newlinsTclj.getTcNo();
			// 启运时间
			String keyTcljqiysj = newlinsTclj.getQiysj();
			// 零件号
			String keyTcljlingjh = newlinsTclj.getLingjh();
			// 零件数量
			BigDecimal keyTcljlingjsl = newlinsTclj.getLingjsl();
			// 物理点
			String keyTcljwuld = newlinsTclj.getWuld();
			// 预计到达时间
			String keyTcljyjddsj = newlinsTclj.getYujddsj();
			// 创建者
			String creator = username;
			// 创建时间
			String createTime = DateUtil.curDateTime();
			keyTclj.setUsercenter(keyTcljuc);
			keyTclj.setJihydm(keyTcljjidm);
			keyTclj.setTcNo(keyTcljtcNo);
			String qiysj = "";
			if(keyTcljqiysj!=null&&!"".equals(keyTcljqiysj)){
				qiysj =keyTcljqiysj.substring(0,keyTcljqiysj.length() - 2);
			}
			keyTclj.setQiysj(qiysj);
			keyTclj.setLingjh(keyTcljlingjh);
			keyTclj.setLingjsl(keyTcljlingjsl);
			keyTclj.setWuld(keyTcljwuld);
			keyTclj.setYujddsl(keyTcljyjddsj.substring(0,
					keyTcljyjddsj.length() - 2));
			keyTclj.setCreator(creator);
			keyTclj.setCreateTime(createTime);
			String key = keyTcljuc + ":" + keyTcljjidm + ":" + keyTcljtcNo;
			/**
			 * 新增可用TC 判断：将keytclj放入map中，以用户中心+":"+计划员代码+":"+TC号为key，如果map中存在该key
			 * ，则表示该TC号已经添加进数据库 不存在，则为新tc
			 */
			if (!newTcMap.containsKey(key)) {
				LinsKeytc linsKeyTc = new LinsKeytc();
				linsKeyTc.setUsercenter(keyTcljuc);
				linsKeyTc.setJihydm(keyTcljjidm);
				linsKeyTc.setTcNo(keyTcljtcNo);
				linsKeyTc.setQiysj(qiysj);
				BigDecimal zyts = BigDecimal.ZERO;
				if (zxtsMap.get(keyTcljtcNo) != null) {
					zyts = (BigDecimal) zxtsMap.get(keyTcljtcNo);
				}
				linsKeyTc.setZhixsj(new BigDecimal(zyts.intValue()));
				linsKeyTc.setCreator(creator);
				linsKeyTc.setCreateTime(createTime);
				batchTcList.add(linsKeyTc);
				// this.insertKeyTC(linsKeyTc);
				newTcMap.put(key, newlinsTclj);
				batchTcljList.add(keyTclj);
			}
			// this.insertKeyTclj(keyTclj);
		}
		this.insertKeyTC(batchTcList);
		this.insertKeyTclj(batchTcljList);
	}

	/**
	 * 插入可用TC
	 * 
	 * @param linsKeytc
	 */
	public void insertKeyTC(List list) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"linskeytc.insertKeytc", list);
	}

	/**
	 * 插入可用TC零件
	 * 
	 * @param keyTclj
	 */
	public void insertKeyTclj(List list) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"linskeytclj.insertKeytclj", list);
	}

	/**
	 * 更新零件满意度
	 * 
	 * @param tcNoList
	 * @param tcLingjMap
	 * @param manydMap
	 * @param username
	 */
	public void updateKeytcAndlj(List<String> tcNoList, Map tcLingjMap,
			Map manydMap, String username) {
		List batchkeyTcList = new ArrayList();
		List batchKeytcljList = new ArrayList();
		for (String key : tcNoList) {
			List<String> tcLingjList = new ArrayList();
			String[] keys = key.split(":");
			String tempUc = keys[0];
			String tempJihydm = keys[1];
			String tempTcNo = keys[2];
			if (tcLingjMap.get(key) != null) {
				tcLingjList = (List) tcLingjMap.get(key);
			}
			BigDecimal tcManyd = BigDecimal.ZERO;
			for (String lingjh : tcLingjList) {
				BigDecimal lingjManyd = BigDecimal.ZERO;
				String lingjkey = tempUc + ":" + tempJihydm + ":" + tempTcNo
						+ ":" + lingjh;
				// 更新零件满意度
				if (manydMap.get(lingjkey) != null) {
					lingjManyd = (BigDecimal) manydMap.get(lingjkey);
					// 初始化可用TC零件
					LinsKeytclj keyTclj = new LinsKeytclj();
					// 零件满意度
					keyTclj.setLingjmyd(lingjManyd);
					// 用户中心
					keyTclj.setUsercenter(tempUc);
					// 计划员代码
					keyTclj.setJihydm(tempJihydm);
					// TC号
					keyTclj.setTcNo(tempTcNo);
					// 零件号
					keyTclj.setLingjh(lingjh);
					keyTclj.setEditor(username);
					keyTclj.setEditTime(DateUtil.curDateTime());
					batchKeytcljList.add(keyTclj);
					tcManyd = tcManyd.add(lingjManyd);
				}
			}
			// 更新TC满意度
			LinsKeytc keytc1 = new LinsKeytc();
			keytc1.setUsercenter(tempUc);
			keytc1.setJihydm(tempJihydm);
			keytc1.setTcNo(tempTcNo);
			keytc1.setTcmyd(tcManyd);
			keytc1.setEditor(username);
			keytc1.setEditTime(DateUtil.curDateTime());
			keytc1.setTczt("1");
			batchkeyTcList.add(keytc1);
		}
		batchUpdateKeytclj(batchKeytcljList);
		batchUpdateKeytc(batchkeyTcList);
	}

	/**
	 * 批量更新可用TC
	 * 
	 * @param batchkeyTcList
	 */
	public void batchUpdateKeytc(List batchkeyTcList) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"linskeytc.updateKeytc", batchkeyTcList);
	}

	/**
	 * 批量更新可用TC零件
	 * 
	 * @param batchKeytcljList
	 */
	public void batchUpdateKeytclj(List batchKeytcljList) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"linskeytclj.updateKeytclj", batchKeytcljList);
	}

	/**
	 * 判断逻辑: 1、遍历TC零件集合sumLingjslList,以用户中心+":"+计划员代码+":"+TC号为key，
	 * 判断tcNoList中是否有该key 如果没有，则表示该集装箱为新的可用集装箱
	 * 将其加入到集合tcNoList中，有则表示该零件所装集装箱已经加为可用
	 * 2、满意度mandyMap用来存零件号与满意度的集合,零件满意度为零件数量除以短缺数量 ,并保留四位小数乘以100%
	 * 3、tcLingjMap是以用户中心+":"+计划员代码+":"+TC号为key，
	 * 剔除不合适的零件以及TC集装箱的方法 
	 * @param sumLingjslList
	 * @param duanqlingjMap
	 * @return
	 * @throws ParseException 
	 */
	public LaxjhBean getBeansBysumLingjslList(
			List<Map<String, Object>> sumLingjslList, Map duanqlingjMap,
			Laxjh laxjh, String wuldgk, String username,String msg,Map duanqljsjMap) throws ParseException {
		Map manydMap = new HashMap();
		Map tcLingjMap = new HashMap();
		// 获取各TC中的零件数量
		List<String> tcNoList = new ArrayList();
		/**
		 * 判断逻辑: 1、遍历TC零件集合sumLingjslList,以用户中心+":"+计划员代码+":"+TC号为key，
		 * 判断tcNoList中是否有该key 如果没有，则表示该集装箱为新的可用集装箱
		 * 将其加入到集合tcNoList中，有则表示该零件所装集装箱已经加为可用
		 * 2、满意度mandyMap用来存零件号与满意度的集合,零件满意度为零件数量除以短缺数量 ,并保留四位小数乘以100%
		 * 3、tcLingjMap是以用户中心+":"+计划员代码+":"+TC号为key，
		 */
		String laxbs = laxjh.getLaxbs();

		// 获取物理点信息
		Map wsMap = getAllwuld();
		// 获取TC箱子路径代码信息
		Map tclujdmMap = getTclujdm();
		// 获取外部物流路径信息
		Map jihydhsjMap = getWaibwulxx(wsMap);
		// 获取最紧缺零件的断点时间
		LaxjhBean bean = this.getMinduandsj();
		String currentDate = DateUtil.getCurrentDate();
		String minDuandsj = bean.getDuandsj();
		List duanqljList = new ArrayList();
		Map minduandsjmap =new HashMap();
		//设置零件是否满足的MAP,如果满足，则不计算其他集装箱，如果不满足，则继续计算其他集装箱
		Map ljmyd = new HashMap();
		for (Map<String, Object> map : sumLingjslList) {
			String tcUc = (String) map.get("USERCENTER");
			String tcjihydm = (String) map.get("JIHYDM");
			String tcNo = (String) map.get("TCNO");

			String wuld = (String) map.get("WULD");
			String yujddsj = (String) map.get("YUJDDSJ");
			String lingjh = (String) map.get("LINGJH");

			String key = tcUc + ":" + tcjihydm + ":" +lingjh;
			String tempKey = tcUc + ":" + tcjihydm + ":" + tcNo;
			String lingjkey = tempKey + ":" + lingjh;
			BigDecimal lingjsl = BigDecimal.ZERO;
			if(map.get("LINGJSL")!=null){
				lingjsl = (BigDecimal)map.get("LINGJSL");
			}
			Date _yujddsj = DateUtil.stringToDateYMD(yujddsj);
			String wuldSxh = (String) wsMap.get(wuld);
			Integer wuldSxhI = Integer.parseInt(wuldSxh);
			String lujdm = "";
			if (tclujdmMap.get(tcNo) != null
					&& !"".equals(tclujdmMap.get(tcNo))) {
				lujdm = (String) tclujdmMap.get(tcNo);
			}else{
				List<String> msgList = new ArrayList<String>();
				msgList.add(tcUc);
				msgList.add(tcNo);
				LoginUser loginuser = new LoginUser();
				loginuser.setUsername(username);
				yicbjService.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str48, "", msgList, tcUc, lingjh,
						loginuser, Const.JISMK_LAX_CD);
				continue;
			}
			BigDecimal tempsj = BigDecimal.ZERO;
			try {
				tempsj = laxjh.getLaxjx().subtract(
						(BigDecimal) jihydhsjMap.get(lujdm + ":" + wuldgk));
			} catch (Exception e) {
				List<String> msgList = new ArrayList<String>();
				msgList.add(tcUc);
				msgList.add(tcNo);
				LoginUser loginuser = new LoginUser();
				loginuser.setUsername(username);
				yicbjService.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str48, "", msgList, tcUc, lingjh,
						loginuser, Const.JISMK_LAX_CD);
			}
			
			Date date = DateUtil.stringToDateYMD(DateUtil.dateAddDays(
					currentDate, tempsj.intValue()));
			String lingjduandsj = (String)duanqljsjMap.get(lingjh);
			if(lingjduandsj!=null&&!lingjduandsj.equals("")&&ljmyd.get(key)==null){
				Date tempYjddsj = DateUtil.stringToDateYMD(yujddsj);
				Date tempLingjduandsj = DateUtil.stringToDateYMD(DateUtil.dateAddDays(lingjduandsj, -1));
				Date tempCurrentDate = DateUtil.stringToDateYMD(currentDate);
					//算法策略为零件品种优先
					if (laxjh.getSuanfcl().equals("1")) {
						//拉箱标识为拉箱计划
						if (laxjh.getLaxbs().equals("1")) {
							// 拉箱界限-武汉到达神龙的时间
							if (tempYjddsj.compareTo(tempLingjduandsj) <= 0&&tempYjddsj.compareTo(tempCurrentDate)>0) {
								//物理点顺序号在武汉杨泗港或者是在杨泗港与上海港顺序号之间
								if (wuldSxhI.equals(Const.YSGSHUNXH)
										|| (wuldSxhI>Const.YSGSHUNXH && wuldSxhI <Const.SHGSHUNXH)) {
									if (!tcNoList.contains(tempKey)) {
										tcNoList.add(tempKey);
									}
								}
								minduandsjmap.put(tcNo, yujddsj);
							} else {
								//剔除的零件集合
								duanqljList.add(tcUc + ":" + tcjihydm +":"+tcNo+ ":"
										+ lingjh);
							}

						} else if (laxjh.getLaxbs().equals("2")) {//拉箱标识为卡运计划
							// 拉箱界限-武汉到达神龙的时间
							// 计算卡运节约时间
							BigDecimal oldJihdhsysj = (BigDecimal) jihydhsjMap
									.get(lujdm + ":" + wuldgk);
							BigDecimal newJihdhsysj= BigDecimal.ZERO;
							if(jihydhsjMap
									.get(laxjh.getKyljdm() + ":" + wuldgk)!=null){
								 newJihdhsysj = (BigDecimal) jihydhsjMap
								.get(laxjh.getKyljdm() + ":" + wuldgk);
							}
							BigDecimal kyjysj = newJihdhsysj
									.subtract(oldJihdhsysj);
							String temp = DateUtil.dateAddDays(yujddsj,kyjysj.intValue());
							if (DateUtil.stringToDateYMD(temp).compareTo(tempLingjduandsj) <= 0&&tempYjddsj.compareTo(tempCurrentDate)>0) {
								//物理点顺序号在上海港或者是在上海港与起始港之间
								if (wuldSxhI.equals(Const.SHGSHUNXH)
										|| (wuldSxhI > Const.QIYSHUNXH && wuldSxhI < Const.SHGSHUNXH)) {
									if (!tcNoList.contains(tempKey)) {
										tcNoList.add(tempKey);
									}
								}
								//TC的最新预计到达时间减去节约天数
								minduandsjmap.put(tcNo, temp);
							} else {
								duanqljList.add(tcUc + ":" + tcjihydm + ":"+tcNo+":"
										+ lingjh);
							}
						}
					} else if (laxjh.getSuanfcl().equals("2")) {//算法策略为滞箱天数优先
						if (laxjh.getLaxbs().equals("1")) {//拉箱标识为拉箱计划
							if (tempYjddsj.compareTo(tempLingjduandsj) <= 0) {
								if (wuldSxhI.equals(Const.YSGSHUNXH)){//物理点在杨泗港
									if (!tcNoList.contains(tempKey)) {
										tcNoList.add(tempKey);
									}
								}
							} else {
								duanqljList.add(tcUc + ":" + tcjihydm +":"+tcNo+ ":"
										+ lingjh);
							}
						} else if (laxjh.getLaxbs().equals("2")) {//拉箱标识为卡运计划
							if (tempYjddsj.compareTo(tempLingjduandsj) <= 0) {
								if (!tcNoList.contains(tempKey)) {
									tcNoList.add(tempKey);
								}
							} else {
								duanqljList.add(tcUc + ":" + tcjihydm +":"+tcNo + ":"
										+ lingjh);
							}
						}
						//如果为滞箱状态，拉箱指定到达时间为计算日加滞箱地到达神龙的时间
						BigDecimal oldJihdhsysj = (BigDecimal) jihydhsjMap.get(lujdm + ":" + wuldgk);
						String temp = DateUtil.dateAddDays(currentDate,oldJihdhsysj.intValue());
						minduandsjmap.put(tcNo, temp);
					}
			}
				
			// 计算每个集装箱对短缺零件的"满意度"
			BigDecimal duanqsl = BigDecimal.ZERO;
			if (duanqlingjMap.get(lingjh) != null) {
				duanqsl = (BigDecimal) duanqlingjMap.get(lingjh);
				BigDecimal tempManyd = lingjsl.divide(duanqsl, 4,BigDecimal.ROUND_HALF_EVEN);
				if (tempManyd.compareTo(BigDecimal.ONE) >= 0) {
					manydMap.put(lingjkey, BigDecimal.ONE);
					if(tcNoList.contains(tempKey)){
						ljmyd.put(key, BigDecimal.ONE);
					}
					
				} else {
					manydMap.put(lingjkey, tempManyd);
				}
			}
			// 将同一集装箱的零件放入map中
			if (tcLingjMap.get(tempKey) == null) {
				List tcList = new ArrayList();
				tcList.add(lingjh);
				tcLingjMap.put(tempKey, tcList);
			} else if (tcLingjMap.get(tempKey) != null) {
				List tcList = (List) tcLingjMap.get(tempKey);
				tcList.add(lingjh);
				tcLingjMap.put(tempKey, tcList);
			}
		}
		
		bean.setManydMap(manydMap);
		bean.setTcLingjMap(tcLingjMap);
		bean.setTcNoList(tcNoList);
		bean.setDuandsj(minDuandsj);
		bean.setDuanqljList(duanqljList);
		bean.setMinduandsjmap(minduandsjmap);
		return bean;
	}
	public LaxjhBean getMinduandsj(){
		LaxjhBean bean= new LaxjhBean();
		Map tjMap = new HashMap();
		String currentDate = DateUtil.getCurrentDate();
		tjMap.put("currentDate", currentDate);
		Map minDuandsjMap = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("linsxuq.getMinDuandsj",tjMap);
		String minDuandsj = "";
		if(minDuandsjMap.get("DUANDSJ")!=null){
			minDuandsj = (String) minDuandsjMap.get("DUANDSJ");
			bean.setDuandsj(minDuandsj);
		}else{
			bean.setMsg("3");
			
		}
		return bean;
	}
	private Map getTclujdm() {
		List tclujdmList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("tc.getTcLujdm");
		Map tclujdmMap = new HashMap();
		for (int i = 0; i < tclujdmList.size(); i++) {
			Map map = (Map) tclujdmList.get(i);
			String tcNo = (String) map.get("TCNO");
			String lujdm = (String) map.get("LUJDM");
			tclujdmMap.put(tcNo, lujdm);
		}
		return tclujdmMap;
	}

	public Map getWaibwulxx(Map wsMap) {
		List waibwulxxList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linsxuq.getAllWaibwulxx");
		Map jihydhsjMap = new HashMap();
		for (int i = 0; i < waibwulxxList.size(); i++) {
			Map map = (Map) waibwulxxList.get(i);
			// 路径编号
			String lujbh = (String) map.get("LUJBH");
			// 物理点编号
			String wuldbh = (String) map.get("WULDBH");

			String shunxh = (String) wsMap.get(wuldbh);

			// 计划到货剩余时间
			BigDecimal jihdhsysj = (BigDecimal) map.get("JIHDHSYSJ");
			// 运输模式
			String yunsms = (String) map.get("YUNSMS");
			String key = lujbh + ":" + wuldbh;
			jihydhsjMap.put(key, jihdhsysj);
		}
		return jihydhsjMap;
	}

	public Map getAllwuld() {
		List wuldList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linsxuq.getAllWuldlx");
		Map wsMap = new HashMap();
		for (int i = 0; i < wuldList.size(); i++) {
			Map map = (Map) wuldList.get(i);
			String wuldbh = (String) map.get("WULDBH");
			String wuldmc = (String) map.get("WULDMC");
			String shunxh = (String) map.get("SHUNXH");
			wsMap.put(wuldbh, shunxh);
		}
		return wsMap;
	}

	/**
	 * 拉箱计划可利用TC信息的条件: 1、根据表TC中的TC号.最新预计到达神龙时间"<系统现在时间+"拉箱范围" 2、拉箱指定到达时间 is null
	 * 3、拉装箱中的零件必须包含"KD资源短缺清单"中任何一种
	 * 
	 * @param xqmx
	 * @return
	 */
	public List<LinsTclj> getLinsTclj(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linstclj.getLinsTclj", map);
	}

	/**
	 * 判断逻辑：一个零件可能存在于多个集装箱，所以将各个集装箱中的零件取出
	 * 通过map进行对应，如果零件号在MAP中没有对应值，则新建一个LIST，将零件数据放入List中，再用map对应
	 * 如果零件号对应在MAP中有值，则将其所对应的取出，将零件数据放入List中，再用map对应值
	 * 
	 * @param tcljList
	 * @return
	 */
	public Map getLingjtcMap(List<LinsTclj> tcljList, String usercenter) {
		Map lingjhMap = new HashMap();
		String zxqwuld = "";
		if (usercenter.equals("UW") || usercenter.equals("UX")) {
			zxqwuld = "107";
		} else {
			zxqwuld = "108";
		}
		for (LinsTclj linsTclj : tcljList) {
			String lingjh = linsTclj.getLingjh();
			String wuld = linsTclj.getWuld();
			if (!zxqwuld.equals(wuld)) {
				if (lingjhMap.get(lingjh) == null) {
					List<LinsTclj> listTemp = new ArrayList();
					listTemp.add(linsTclj);
					lingjhMap.put(lingjh, listTemp);
				} else if (lingjhMap.get(lingjh) != null) {
					List<LinsTclj> listTemp = (List) lingjhMap.get(lingjh);
					listTemp.add(linsTclj);
					lingjhMap.put(lingjh, listTemp);
				}
			}
		}
		return lingjhMap;
	}

	/**
	 * 判断逻辑:lingjTcMap为所有可用零件的集合 如果临时需求中的零件在lingjTcMap中， 则表示该TcNo中拥有所缺零件
	 * 再将该零件集合加入可用TC集体，即keyTcList中
	 * 
	 * @param list
	 *            临时需求
	 * @param lingjTcMap
	 *            集装箱中的零件分类
	 * @return
	 */
	public LaxjhBean getBeans(List<LinsXuq> list, String usercenter, Map tjmap) {
		LaxjhBean bean = new LaxjhBean();
		String zxqwuld = "";
		if (usercenter.equals("UW") || usercenter.equals("UX")) {
			zxqwuld = "107";
		} else {
			zxqwuld = "108";
		}
		List<LinsTclj> tcljlist = new ArrayList<LinsTclj>();
		
		String currentDate = DateUtil.getCurrentDate();
		tcljlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linstclj.getLinsTclj");
		List<Map<String, String>> tclist = new ArrayList<Map<String, String>>();
		tclist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linstclj.getTc", tjmap);
		// 用于存可用TC的list
		List<LinsTclj> keyTcljList = new ArrayList();
		Map duanqlingjMap = new HashMap();
		List<LinsTclj> newtcljList = new ArrayList<LinsTclj>();
		Map<String, LinsTclj> tcljMap = new HashMap();
		Map<String,String> duanqljsjMap = new HashMap<String,String>();
		for (Map<String, String> tcmap : tclist) {
			String tcNo = tcmap.get("TCNO");
			for (LinsTclj tclj : tcljlist) {
				String lingjh = tclj.getLingjh();
				String wuld = tclj.getWuld();
				if (tcNo.equals(tclj.getTcNo())) {
					tcljMap.put(tcNo + ":" + lingjh, tclj);
				}
			}
		}
		for (Map<String, String> tcmap : tclist) {
			String tcNo = tcmap.get("TCNO");
			for (LinsXuq xuq : list) {
				String lingjbh = xuq.getLingjh();
				// 需求数量即为短缺数量
				BigDecimal duanqsl = xuq.getXuqsl();
				String ljduanqsj = xuq.getDuandsj();
				duanqljsjMap.put(lingjbh, ljduanqsj);
				duanqlingjMap.put(lingjbh, duanqsl);
				if (tcljMap.get(tcNo + ":" + lingjbh) != null) {
					keyTcljList.add(tcljMap.get(tcNo + ":" + lingjbh));
				}
			}
		}
		bean.setDuanqlingjMap(duanqlingjMap);
		bean.setKeyTcljList(keyTcljList);
		bean.setDuanqljsjMap(duanqljsjMap);
		tclist.clear();
		list.clear();
		return bean;
	}

	/**
	 * 获取需求数量为null的零件信息 条件:用户中心、毛需求版次
	 * 
	 * @param linsXuq
	 * @return
	 */
	public List<LinsXuq> getLingjbj(LinsXuq linsXuq) {
		List xuqList = new ArrayList();
		xuqList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linsxuq.queryLingjbj", linsXuq);
		return xuqList;
	}

	/**
	 * 获取滞箱天数
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getZxts(String wuldgk) {
		List zxtsList = new ArrayList();
		Map map = new HashMap();
		map.put("wuldgk", wuldgk);
		zxtsList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linstclj.getLinsTcljzxts", map);
		return zxtsList;
	}

	public Map<String, Object> getZxtsMap(List<Map<String, Object>> zxtsList) {
		// zxtsList为滞箱天数集合
		Map zxtsMap = new HashMap();
		for (int i = 0; i < zxtsList.size(); i++) {
			Map<String, Object> map = zxtsList.get(i);
			String tcNo = (String) map.get("TCNO");
			Object obj = map.get("ZXTS");
			zxtsMap.put(tcNo, obj);
		}
		return zxtsMap;
	}

	/**
	 * 获取临时TC零件中的各TC中的零件数量
	 * 
	 * @param linsTclj
	 * @return
	 */
	public List<Map<String, Object>> getSumLingjslByTc(LinsTclj linsTclj) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linstclj.getLinsTcljsl", linsTclj);
	}

	/**
	 * 删除可用TC
	 * 
	 * @param table
	 * @param tjNameMap
	 */
	public void deleteLinsKeyTc(LinsKeytc linsKeytc) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"linskeytc.deleteLinsKeytc", linsKeytc);
	}

	/**
	 * 删除可用TC零件明细
	 * 
	 * @param linsKeytclj
	 */
	public void deleteLinsKeyTclj(LinsKeytclj linsKeytclj) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"linskeytclj.deleteLinsKeytclj", linsKeytclj);
	}

	/**
	 * 获取拉箱零件报警数据
	 * 
	 * @param xuqbc
	 * @return
	 * @throws Exception
	 */
	public Map queryLingjbj(LinsXuq bean, Pageable page, String anqkc,
			String laxjx, String laxbs, String ziyts, String kyljdm) {
		Map<String, Object> map = new HashMap();
		Map tjmap = new HashMap();
		tjmap.put("anqkc", anqkc);
		tjmap.put("laxjx", laxjx);
		tjmap.put("usercenter", bean.getUsercenter());
		tjmap.put("maoxqbc", bean.getMaoxqbc());
		tjmap.put("lingjh", bean.getLingjh());
		tjmap.put("jihydm", bean.getJihydm());
		tjmap.put("laxbs", laxbs);
		tjmap.put("ziyts", ziyts);
		tjmap.put("kyljdm", kyljdm);

		map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("linsxuq.queryLingjbjLax", tjmap, page);
		return map;
	}

	/**
	 * 查询拉箱计划
	 * 
	 * @param laxjh
	 * @param page
	 * @return
	 */
	public Map queryLaxjh(Laxjh laxjh, Pageable page) {
		Map<String, Object> map = new HashMap();
		map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("laxjh.queryLaxjh", laxjh, page);
		return map;
	}

	/**
	 * 页面更新临时需求
	 * 
	 * @param linsXuq
	 * @param loginUser
	 */
	public void editLinsxuq(LinsXuq linsXuq, LoginUser loginUser) {
		String username = loginUser.getUsername();
		String createTime = DateUtil.curDateTime();
		linsXuq.setEditor(username);
		linsXuq.setEditTime(createTime);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"linsxuq.updateLinsxuqByXuqsl", linsXuq);
	}

	/**
	 * 页面添加临时需求
	 * 
	 * @param linsXuq
	 */
	public void saveLinsXuq(LinsXuq linsXuq, LoginUser loginUser) {
		//设置临时需求断点时间
		setLinsxuqSingle(linsXuq, loginUser,"1");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"linsxuq.insertLinsxuqInfo", linsXuq);
	}
	public void setLinsxuqSingle(LinsXuq linsXuq, LoginUser loginUser,String method) {
		BigDecimal cmj = linsXuq.getCmj();
		BigDecimal allkc =BigDecimal.ZERO;
		if(method.equals("1")){
			 allkc = linsXuq.getCangkkc().add(linsXuq.getZhongxqkc());
		}else if(method.equals("2")){
			allkc = linsXuq.getCangkkc();
		}
		BigDecimal days = BigDecimal.ZERO;
		if (cmj.compareTo(BigDecimal.ZERO) == 1) {
			days = allkc.divide(cmj, 0, BigDecimal.ROUND_UP);
		}
		String currentDate = DateUtil.getCurrentDate();
		String duandsj = getDuandsj(days, currentDate);
		linsXuq.setDuandsj(duandsj);
		BigDecimal daohl = linsXuq.getDaohl();
		BigDecimal manzsl = allkc.add(daohl);
		if(manzsl==null||manzsl.equals("")){
			manzsl = BigDecimal.ZERO;
		}
		linsXuq.setManzsl(manzsl);
		String username = loginUser.getUsername();
		String createTime = DateUtil.curDateTime();
		linsXuq.setCreator(username);
		linsXuq.setCreateTime(createTime);
	}

	/**
	 * 获取零件计划员代码信息
	 * 
	 * @param usercenter
	 * @param lingjh
	 * @return
	 */
	public Map getLingjh(String usercenter, String lingjh,String ziyts,String laxbs,String method) {
		Map map = new HashMap();
		map.put("usercenter", usercenter);
		map.put("lingjbh", lingjh);
		Map jihydmMap = new HashMap();
		if (usercenter != null && !"".equals(usercenter) && lingjh != null
				&& !"".equals(lingjh)) {
			List<Map<String, Object>> lingjList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
					"linsxuq.getLingjhJijhydm", map);
			for (Map<String, Object> lingjhmap : lingjList) {
				String jihydm = (String) lingjhmap.get("JIHY");
				jihydmMap.put("jihydm", jihydm);
			}
			// 资源天数
			BigDecimal ziytsB = new BigDecimal(ziyts);
			// 当资源天数!=空时，取资源天数的值；当资源天数==空时，资源天数取拉箱界限的值；
			String now = DateUtil.getCurrentDate();
			String laxzdddsj = DateUtil.dateAddDays(now, ziytsB.intValue() - 1);
			TC tc = new TC();
			tc.setUsercenter(usercenter);
			tc.setLaxzdddsj(laxzdddsj);
			if (usercenter.equals(LaxkaixConst.ucUw)) {
				tc.setZuiswld(LaxkaixConst.uwZxq);
			} else if (usercenter.equals("UL")) {
				tc.setZuiswld(LaxkaixConst.uxZxq);
			}else if (usercenter.equals(LaxkaixConst.ucUx)) {
				tc.setZuiswld("15A");
			}
			tc.setCurrentDate(now);
			List<Map<String, Object>> yddhljslList = this.getYujdhsl(tc);
			// 预定到货零件数量Map集合
			Map<String,BigDecimal> yddhljslmap = new HashMap<String,BigDecimal>();
			yddhljslmap = this.getljslMap(yddhljslList);
			//获取资源获取日期
			String ziyhqrq =  this.ziykzbService.getFirstDay();
			//获取零件库存
			// 查出现有库存中的所有零件的库存
			List<Map<String, Object>> allLjkcList = new ArrayList<Map<String, Object>>();// 初始化零件库存集合
			// 初始化临时变量(计算库存)
			Laxjh laxjh = new Laxjh();
			laxjh.setUsercenter(usercenter);
			laxjh.setZiyhqrq(ziyhqrq);
			allLjkcList = this.getAllLjkc(laxjh);
			Map<String,BigDecimal> ljkcMap = new HashMap<String,BigDecimal>();// 初始化零件库存对应map集合
			ljkcMap = this.getljslMap(allLjkcList);
			allLjkcList.clear();
			List<Map<String, Object>> allTdjList = new ArrayList<Map<String, Object>>();// 初始化替代件集合
			allTdjList = this.getAlllTdjList(laxjh);
			Map<String, List<String>> tdjMap = new HashMap<String, List<String>>();// 初始化替代件map集合
			tdjMap = this.getTdjMap(allTdjList);
			BigDecimal sumValues = BigDecimal.ZERO;
			sumValues = this.getLjsls(ljkcMap, tdjMap, lingjh);
			// 计算KD零件重箱区库存
			List<Map<String, Object>> zxqkcList = this.getZxqkcByUc(usercenter);
			// 重箱区库存集合
			Map<String,BigDecimal> zxqkc = this.getZxqkc(zxqkcList);
			// 零件重箱区库存
			BigDecimal linjgzxqkc = BigDecimal.ZERO;
			linjgzxqkc = this.getZxqkcljsl(zxqkc, lingjh);
			BigDecimal allkc = BigDecimal.ZERO;
			if(method.equals("1")){
				List<Map<String, Object>> getZcyujddslList = new ArrayList<Map<String, Object>>();
				getZcyujddslList = this.getZcyujddslList(tc);
				// 预计快到达零件数量Map集合
				Map<String, BigDecimal> yjddLingjmap = new HashMap<String, BigDecimal>();
				yjddLingjmap = this.getljslMap(getZcyujddslList);
				// 拉箱界限天数内拉箱计划到达数量
				BigDecimal yddhljsl = BigDecimal.ZERO;
				if (yddhljslmap.get(lingjh) != null) {
					yddhljsl = (BigDecimal) yddhljslmap.get(lingjh);
				}
				// 拉箱标识为卡运计划时，正常预计能到达的箱子
				BigDecimal yjddljsl = BigDecimal.ZERO;
				if (yjddLingjmap.get(lingjh) != null) {
					yjddljsl = (BigDecimal) yjddLingjmap.get(lingjh);
				}
				BigDecimal tempkc = BigDecimal.ZERO;
				// 零件仓库库存+零件重箱区库存+拉箱界限天数内零件重箱区库存之和
				BigDecimal laxjxLjsl = BigDecimal.ZERO;
				if (laxbs!=null&&!laxbs.equals("")&&laxbs.equals("2")) {
					allkc = yjddljsl.add(yddhljsl);
				}else{
					allkc = yddhljsl;
				}
			}
			
			if(linjgzxqkc==null){
				linjgzxqkc = BigDecimal.ZERO;
			}
			if(sumValues==null){
				sumValues = BigDecimal.ZERO;
			}
			if(allkc==null){
				allkc = BigDecimal.ZERO;
			}
			jihydmMap.put("zhongxqkc", linjgzxqkc);
			jihydmMap.put("cangkkc", sumValues);
			jihydmMap.put("daohl", allkc);
		}
		return jihydmMap;
	}

	/**
	 * 删除临时需求
	 * 
	 * @param linsXuq
	 */
	public void deleteLinsxuq(LinsXuq linsXuq) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"linsxuq.deleteLinsxuqBytj", linsXuq);
	}

	/**
	 * 查询拉箱计划明细
	 * 
	 * @param laxjhh
	 * @return
	 */
	public Map queryLaxjhmx(Laxjhmx mx, Map tjMap) {
		// 分页查询拉箱计划明细
		Map<String, Object> resultMap = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages(
				"laxjhmx.queryLaxjhmxInfo", tjMap, mx);
		return resultMap;
	}

	/**
	 * 查询未满足零件数量
	 * 
	 * @param laxjh
	 * @return
	 */
	public Map<String, Object> queryLaxjhWeimzlj(Weimzlj weimzlj,Map<String, String> params) {
		Map tjMap = new HashMap();
		Map<String, Object> map = new HashMap<String, Object>();
		tjMap.put("jihNo", weimzlj.getJihNo());
		if("exportXls".equals(params.get("exportXls"))){
			List<Object> list = baseDao.getSdcDataSource(
					ConstantDbCode.DATASOURCE_XQJS).select("weimzlj.queryLaxjhWeimzlj", tjMap);
			map.put("total", list.size());
			map.put("rows", list);
		}else{
			map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("weimzlj.queryLaxjhWeimzlj", tjMap,weimzlj);
		}
		return map;
	}

	/**
	 * 编辑拉箱计划明细
	 * 
	 * @param laxjhmx
	 */
	public void editLaxjhmx(Laxjhmx laxjhmx) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"laxjhmx.deleteLaxjhmx", laxjhmx);
	}

	/**
	 * 更新拉箱计划明细状态为2
	 * 
	 * @param laxjhmx
	 */
	public void saveLaxjhmx(Laxjhmx laxjhmx, LoginUser loginUser) {
		// 死数据
		laxjhmx.setZhuangt("2");
		laxjhmx.setCreator(loginUser.getUsername());
		laxjhmx.setCreateTime(DateUtil.getCurrentDate());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"laxjhmx.insertLaxjhmx", laxjhmx);
	}

	/**
	 * 生效拉箱计划 逻辑: 1、 系统会将包含这版拉箱计划的任一TC号的其他的拉箱计划都删除， 保证一个TC号只会在一版已生效的拉箱计划中 2、
	 * 更新"TC"表信息："最新预计到达时间”和"拉箱指定到达时间"更新为拉箱计划指定的"拉箱计划指定到达时间"；
	 * 
	 * @param laxjh
	 */
	@Transactional
	public void  sxLaxjh(Laxjh laxjh, LoginUser loginUser) {
		laxjh.setShengxzt("1");
		String username = loginUser.getUsername();
		String currentDate = DateUtil.getCurrentDate();
		laxjh.setEditor(username);
		laxjh.setEditTime(currentDate);
		Laxjhmx mx = new Laxjhmx();
		String laxjhNo = laxjh.getLaxjhh();
		String usercenter = laxjh.getUsercenter();
		mx.setLaxjhNo(laxjhNo);
		mx.setUsercenter(usercenter);
		// 查询拉箱计划明细信息
		List<Map<String, Object>> list = getLaxjhmx(mx);
		
		for (Map<String, Object> map : list) {
			String _usercenter = (String)map.get("USERCENTER");
			String tcNo = (String) map.get("TCNO");
			String laxzdsj = (String) map.get("LAXZDSJ");
			String kyljdm = "";
			if(map.get("KYLJDM")!=null&&!"".equals(map.get("KYLJDM"))){
				kyljdm = (String) map.get("KYLJDM");
			}
			String kdysSheetId ="";
			if(map.get("KDYS_SHEET_ID")!=null&&!"".equals(map.get("KDYS_SHEET_ID"))){
				kdysSheetId	= (String)map.get("KDYS_SHEET_ID");
			}
			
			Map mxMap = new HashMap();
			mxMap.put("laxjhNo", laxjhNo);
			mxMap.put("tcNo", tcNo);
			// 删除包含这版拉箱计划的任一TC号的其他的拉箱计划
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"laxjhmx.deleteLaxjhmxOther", mxMap);
			String id = (String) map.get("ID");
			Map tcMap = new HashMap();
			tcMap.put("laxzdsj", laxzdsj);
			tcMap.put("editor", username);
			tcMap.put("editTime", currentDate);
			tcMap.put("tcNo", tcNo);
			tcMap.put("id", id);
			tcMap.put("lujdm", kyljdm);
			// 更新TC拉箱计划时间
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"tc.updateLaxzdsj", tcMap);
		}
		// 更新拉箱计划状态
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"laxjh.changeZtLaxjh", laxjh);
		/**
		 * 更新要货令信息
		 */
		Map map = this.getTcByYaohl();
		this.updateYaohlBytcList(list, map);
	}

	public List<Map<String, Object>> getLaxjhmx(Laxjhmx mx) {
		List<Map<String, Object>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjhmx.queryLaxjhmx",mx);
		return list;
	}

	/**
	 * 根据TC号找到要货令信息
	 * 
	 * @param usercenter
	 * @return
	 */
	public Map getTcByYaohl() {
		List yaohlList = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_XQJS).select("laxjh.selectYaohl");
		Map map = new HashMap();
		for (int i = 0; i < yaohlList.size(); i++) {
			YaohlBean bean = (YaohlBean) yaohlList.get(i);
			String _usercenter = bean.getUsercenter();
			String yaohlh = bean.getYaohlh();
			String tcNo = bean.getTcNo();
			String key = _usercenter+":"+yaohlh;
			if(map.get(tcNo)==null){
				List list = new ArrayList();
				list.add(key);
				map.put(tcNo, list);
			}else{
				List list = (List)map.get(tcNo);
				list.add(key);
				map.put(tcNo, list);
			}
		}
		return map;
	}

	/**
	 * 更新要货令信息
	 * 
	 * @param usercenter
	 * @param list
	 * @param map
	 */
	public void updateYaohlBytcList(List<Map<String, Object>> list, Map map) {
		List sjList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> laxjhmxmap = (Map<String, Object>) list.get(i);
			String tcNo = (String) laxjhmxmap.get("TCNO");
			String laxzdsj = (String) laxjhmxmap.get("LAXZDSJ");
			List ucYaohlList = (List) map.get(tcNo);
			if (ucYaohlList!=null&&ucYaohlList.size()>0) {
				for(int j=0;j<ucYaohlList.size();j++){
					String key = (String)ucYaohlList.get(j);
					CkYaohlBean ckyaohlBean = new CkYaohlBean();
					String[] values = key.split(":");
					String _usercenter = values[0];
					String yaohlh = values[1];
					ckyaohlBean.setUsercenter(_usercenter);
					ckyaohlBean.setYaohlh(yaohlh);
					ckyaohlBean.setTcNo(tcNo);
					ckyaohlBean.setLaxzdsj(laxzdsj);
					sjList.add(ckyaohlBean);
				}
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"laxjh.updateCkyaohl", sjList);
	}

	/**
	 * 根据TC号单个更新要货令信息
	 * 
	 * @param tcNo
	 * @param laxzdsj
	 * @param map
	 */
	public void updateYaohlBytc(String tcNo, String laxzdsj, Map map) {
		List keyList = (List) map.get(tcNo);
		CkYaohlBean ckyaohlBean = new CkYaohlBean();
		List list = new ArrayList();
		if (keyList != null) {
			for(int i=0;i<keyList.size();i++){
				String value = (String)keyList.get(i);
				String[] values = value.split(":");
				String _usercenter = values[0];
				String yaohlh = values[1];
				ckyaohlBean.setUsercenter(_usercenter);
				ckyaohlBean.setYaohlh(yaohlh);
				ckyaohlBean.setTcNo(tcNo);
				ckyaohlBean.setLaxzdsj(laxzdsj);
				list.add(ckyaohlBean);
			}
			
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch(
				"laxjh.updateCkyaohlByZiybj", list);
	}

	/**
	 * 取消拉箱计划 逻辑： 1、更改拉箱计划.状态="删除",拉箱计划明细.状态="删除" 2、更新条件:画面指定的用户中心、拉箱计划号
	 * 3、选中拉箱计划的状态="已生效"
	 * 
	 * @param laxjh
	 * @param loginUser
	 */
	@Transactional
	public void qxLaxjh(Laxjh laxjh, LoginUser loginUser) {
		laxjh.setShengxzt("2");
		String username = loginUser.getUsername();
		String currentDate = DateUtil.getCurrentDate();
		laxjh.setEditor(username);
		laxjh.setEditTime(currentDate);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"laxjh.changeZtLaxjh", laxjh);
		Laxjhmx mx = new Laxjhmx();
		String laxjhNo = laxjh.getLaxjhh();
		String usercenter = laxjh.getUsercenter();
		mx.setLaxjhNo(laxjhNo);
		mx.setUsercenter(usercenter);
		List<Map<String, Object>> list = getLaxjhmx(mx);
		for (Map<String, Object> map : list) {
			Map mxMap = new HashMap();
			mxMap.put("shengxzt", "2");
			mxMap.put("editor", username);
			mxMap.put("editTime", currentDate);
			mxMap.put("laxjhNo", laxjhNo);
			mxMap.put("usercenter", usercenter);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"laxjhmx.deleteLaxjhmxbyUc", mxMap);
		}
	}

	/**
	 * @param tcNo
	 * @return
	 */
	public Map getTcId(String tcNo) {
		Map resultMap = new HashMap();
		Map tcNoMap = new HashMap();
		tcNoMap.put("tcNo", tcNo);
		List<Map<String, Object>> tcList = baseDao
				.select("tc.getTcId", tcNoMap);
		for (Map<String, Object> idMap : tcList) {
			resultMap.put("id", (String) idMap.get("ID"));
			resultMap.put("qiysj", (String) idMap.get("QIYSJ"));
			resultMap.put("mudd", (String) idMap.get("MUDD"));
			resultMap.put("zuiswld", (String) idMap.get("ZUISWLD"));
		}
		return resultMap;
	}

	/**
	 * @param lingjh
	 * @return
	 */
	public String validateLingjh(String lingjh) {
		Map map = new HashMap();
		map.put("lingjh", lingjh);
		Integer count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(
				"linsxuq.validateLingjh", map);
		String msg = "";
		if (count == 0) {
			msg = "1";
		} else if (count > 0) {
			msg = "0";
		}
		return msg;
	}

	/**
	 * @param tjMap
	 * @return
	 */
	public String validateLinsxuqLingjh(Map<String, String> tjMap) {
		Integer count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(
				"linsxuq.validateLinsxuqLingjh", tjMap);
		String msg = "";
		if (count == 0) {
			msg = "1";
		} else if (count > 0) {
			msg = "0";
		}
		return msg;
	}

	/**
	 * @param tjMap
	 * @return
	 */
	public String validateLaxjhmxTcNo(Map<String, String> tjMap) {
		Integer count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(
				"laxjhmx.validateLaxjhmxTcNo", tjMap);
		String msg = "";
		if (count == 0) {
			msg = "1";
		} else if (count > 0) {
			msg = "0";
		}
		return msg;
	}

	/**
	 * @param laxjh
	 * @param tcNo
	 * @param dinghcj
	 * @param qiysj
	 * @param wuld
	 * @return
	 * @throws ParseException 
	 */
	public Map<String, Object> laxjhDownLoadFile(Map tjMap) throws ParseException {
		Map<String, Object> dataSource = new HashMap<String, Object>();
		// String laxjhh = (String) tjMap.get("laxjhNo");
		List<Laxjh> laxjhlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.queryLaxjh");
		Laxjh laxjh = laxjhlist.get(0);
		BigDecimal laxjx = laxjh.getLaxjx();
		BigDecimal anqkc = BigDecimal.ZERO;
		if (laxjh.getAnqkc() != null) {
			anqkc = laxjh.getAnqkc();
		}
		dataSource.put("laxjx", laxjx);
		dataSource.put("anqkc", anqkc);
		String jskaisriq = laxjh.getCreateTime();
		jskaisriq = jskaisriq.substring(0, 10);
		String jsjiesriq = DateUtil
				.dateAddDays(jskaisriq, laxjx.intValue() - 1);
		Date jskaisriqD = DateUtil.stringToDateYMD(jskaisriq);
		String jskaisriqS = DateUtil.dateFromat(jskaisriqD, "yyyy-MM-dd");
		dataSource.put("jskaisriq", jskaisriqS);
		dataSource.put("jsjiesriq", jsjiesriq);
		List<Map<String, Object>> mxList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
				"laxjhmx.queryLaxjhmxDownload", tjMap);
		Map<String, Object> tcMap = new HashMap<String, Object>();
		Map wuldMap = new HashMap();
		for (Map<String, Object> map : mxList) {
			// 用户中心
			String usercenter = (String) map.get("USERCENTER");
			// 拉箱计划号
			String laxjhNo = (String) map.get("LAXJHNO");
			// ID号
			String id = (String) map.get("ID");
			// TC号
			String tcNo = (String) map.get("TCNO");
			// 拉箱指定时间
			String laxzdsj = (String) map.get("LAXZDSJ");
			// 启运时间
			String qiysj = (String) map.get("QIYSJ");
			// 物理点
			String wuld = "";
			if (map.get("WULD") != null) {
				wuld = (String) map.get("WULD");
			}
			// 到货车间
			String dinghcj = (String) map.get("DINGHCJ");
			Laxjhmx laxjhmx = new Laxjhmx();
			laxjhmx.setUsercenter(usercenter);
			laxjhmx.setLaxjhNo(laxjhNo);
			laxjhmx.setId(id);
			laxjhmx.setTcNo(tcNo);
			laxjhmx.setQiysj(qiysj);
			laxjhmx.setLaxzdsj(laxzdsj);
			laxjhmx.setWuld(wuld);
			laxjhmx.setDinghcj(dinghcj);
			String key = qiysj + ":" + laxzdsj;
			if (tcMap.get(key) == null) {
				List tcList = new ArrayList();
				tcList.add(tcNo);
				tcMap.put(key, tcList);
			} else if (tcMap.get(key) != null) {
				List tcList = (List) tcMap.get(key);
				tcList.add(tcNo);
				tcMap.put(key, tcList);
			}
			if (wuldMap.get(key) == null) {
				wuldMap.put(key, wuld);
			}
		}
		// int allRows = 0;
		// Map rowsKeyMap = new HashMap();
		Map otherMap = new HashMap();
		Map newWuldMap = new HashMap();
		Map countMap = new HashMap();
		for (Map.Entry<String, Object> entry : tcMap.entrySet()) {
			List tcNoList = (List) entry.getValue();
			String key = entry.getKey();
			String wuld = (String) wuldMap.get(key);
			int size = tcNoList.size();
			int tcRows = size / 3 + 1;
			for (int i = 0; i < tcRows; i++) {
				int m = i + 1;
				String tempKey = m + ":" + key;
				List list = null;
				Integer count = null;
				if (otherMap.get(tempKey) == null) {
					list = new ArrayList();
				} else if (otherMap.get(tempKey) != null) {
					list = (List) otherMap.get(tempKey);
				}
				if (countMap.get(tempKey) == null) {
					count = 0;
				} else if (countMap.get(tempKey) != null) {
					count = (Integer) countMap.get(tempKey);
				}
				if ((m * 3 - 3) < size) {
					list.add(tcNoList.get(m * 3 - 3));
					count = count + 1;
				} else {
					list.add(" ");
				}
				if ((m * 3 - 2) < size) {
					list.add(tcNoList.get(m * 3 - 2));
					count = count + 1;
				} else {
					list.add(" ");
				}
				if ((m * 3 - 1) < size) {
					list.add(tcNoList.get(m * 3 - 1));
					count = count + 1;
				} else {
					list.add(" ");
				}
				otherMap.put(tempKey, list);
				countMap.put(tempKey, count);
				newWuldMap.put(tempKey, wuld);
			}
		}
		dataSource.put("rowsKeyMap", otherMap);
		dataSource.put("wuldMap", newWuldMap);
		dataSource.put("countMap", countMap);
		return dataSource;
	}

	public static String getNewString(String str) {
		return str.replaceFirst("^0*", "");
	}

	/**
	 * @return
	 */
	public List listKayunLujdm() {
		List list = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_AUTHORITY).select(
				"laxjh.listWaibwlxx");
		List lujbhlist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			try {
				String result = JSONUtils.toJSON(map);
				lujbhlist.add(result);
			} catch (JSONException e) {
				log.info(e.toString());
			}
		}
		return lujbhlist;
	}
}


