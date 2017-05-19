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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.date.DateUtil;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.laxkaix.Kaixjh;
import com.athena.xqjs.entity.laxkaix.Kaixjhmx;
import com.athena.xqjs.entity.laxkaix.LaxjhBean;
import com.athena.xqjs.entity.laxkaix.LinsKeytc;
import com.athena.xqjs.entity.laxkaix.LinsKeytclj;
import com.athena.xqjs.entity.laxkaix.LinsTclj;
import com.athena.xqjs.entity.laxkaix.LinsXuq;
import com.athena.xqjs.entity.laxkaix.TC;
import com.athena.xqjs.entity.laxkaix.Weimzlj;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.utils.json.JSONException;

/**
 * @author dsimedd001
 * 
 */
@Component
public class KaixjhService extends BaseService {

	@Inject
	private LaxjhService laxjhService;
	
	@Inject
	private YicbjService yicbjService;
	
	// log4j日志初始化
	private final Log log = LogFactory.getLog(KaixjhService.class);

	/**
	 * 计算零件短缺
	 * 
	 * @param xuqbc
	 * @param usercenter
	 * @return
	 */
	@Transactional
	public String jsLingjDuanq(Maoxqmx xqmx, Kaixjh kaixjh, LoginUser loginUser) {
		// 开箱界限
		BigDecimal kaixjx = kaixjh.getKaixjx();
		// 安全库存天数
		BigDecimal anqkc = BigDecimal.ZERO;
		// 安全库存天数为NULL，则为零
		if (kaixjh.getAnqkc() != null) {
			anqkc = kaixjh.getAnqkc();
		}
		// 获取用户名
		String username = loginUser.getUsername();
		// 用户中心
		String usercenter = xqmx.getUsercenter();
		// 设置零件号与计划员代码的对应
		//Lingj lingj = new Lingj();
		// 设置用户中心
		//lingj.setUsercenter(usercenter);
		// 删除临时表 tc零件表中的数据
		LinsTclj linsTclj = new LinsTclj();
		// 设置用户中心
		linsTclj.setUsercenter(usercenter);
		// 删除临时表_临时需求表
		LinsXuq tempxuq = new LinsXuq();
		tempxuq.setUsercenter(usercenter);
		laxjhService.deleteLinsxuq(tempxuq);
		log.info(LaxkaixConst.deleteLinsXuqMsg);
		laxjhService.deleteLinstclj(linsTclj);
		log.info(LaxkaixConst.deleteLinstcljMsg);
		/**
		 * 初始化临时表 TC零件表
		 */
		// 初始化零件集合
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String,String> tjMap = new HashMap<String,String>();
		tjMap.put("usercenter", usercenter);
		tjMap.put("xuqbc", xqmx.getXuqbc());
		List<String> maoxqLingjbhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.getMaoxqLingjbh", tjMap);
		log.info(LaxkaixConst.selectMaoxuqLingjbh);
		boolean flag = true;
		StringBuffer sb = new StringBuffer();
		int size = maoxqLingjbhList.size();
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
//		for(int i=0;i<size;i++){
//			String lingjbh = maoxqLingjbhList.get(i);
//			sb.append("'"+lingjbh+"'");
//			if(i<size-1&&flag==true){
//				sb.append(",");
//			}else{
//				flag = false;
//			}
//		}
		sb.append(")");
		tjMap.put("lingjbhs", sb.toString());
		// 查询各集装箱中各零件信息
		list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kaixjh.getTCLingjList", tjMap);
		log.info(LaxkaixConst.selectLinstcljMsg);
		// 遍历集合并将TC零件信息插入LINS_TCLJ中
		String msg = "1";
		try {
			laxjhService.bianliList(list, usercenter,  username);
			log.info(LaxkaixConst.insertLinstcljMsg);
		} catch (Exception e) {
			msg = "2";
			log.info(LaxkaixConst.nullJihydmMsg);
		}
		// 获取开箱界限日期
		String now = DateUtil.getCurrentDate();
		String kaixzdddsj = DateUtil.dateAddDays(now, kaixjx.intValue()-1);
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = laxjhService.jszqrq(now, kaixzdddsj);
			log.info(LaxkaixConst.jiszqrqMsg);
		} catch (Exception e) {
			msg = "3";
			log.info(e.toString());
		}
		List<LinsXuq> xuqList = new ArrayList<LinsXuq>();
		Map laxjxGongzrtsMap = new HashMap();
		// 获取临时需求
		try {
			LaxjhBean bean = laxjhService.getLingjLinsxuq(map, xqmx, username,Const.JISMK_KAIX_CD);
			xuqList = bean.getLinsxuqList();
			laxjxGongzrtsMap = bean.getLaxjxGongzrtsMap();
			log.info(LaxkaixConst.getLinsxuqMsg);
		} catch (Exception e) {
			msg = "4";
		}
		try {
			this.insertLinsXuq(xuqList);
		} catch (Exception e) {
			msg = "5";
		}
		// 计算KD零件重箱区库存
		List<Map<String, Object>> zxqkcList = this.getZxqkcByUc(usercenter);
		// 重箱区库存集合
		Map zxqkc = this.getZxqkc(zxqkcList);
		TC tc = new TC();
		tc.setKaixzdsj(kaixzdddsj);
		List<Map<String, Object>> yddhljslList = this.getYujdhsl(tc);
		// 预定到货零件数量Map集合
		Map yddhljslmap = new HashMap();
		yddhljslmap = this.getljslMap(yddhljslList);
		yddhljslList.clear();
		// 查出现有库存中的所有零件的库存
		List<Map<String, Object>> allLjkcList = new ArrayList();// 初始化零件库存集合
		// 初始化临时变量
		allLjkcList = this.getAllLjkc(kaixjh);
		Map ljkcMap = new HashMap();// 初始化零件库存对应map集合
		ljkcMap = this.getljslMap(allLjkcList);
		allLjkcList.clear();
		List<Map<String, Object>> allTdjList = new ArrayList();// 初始化替代件集合
		allTdjList = this.getAlllTdjList(kaixjh);
		Map tdjMap = new HashMap();// 初始化替代件map集合
		tdjMap = this.getTdjMap(allTdjList);
		allTdjList.clear();
		List<LinsXuq> updateList = new ArrayList<LinsXuq>();
		List<LinsXuq> deleteList = new ArrayList<LinsXuq>();
		for (LinsXuq xuq : xuqList) {// 获取插入的临时需求表
			String lingjh = xuq.getLingjh();// 获取零件号
			BigDecimal sumValues = BigDecimal.ZERO;
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
			// 开箱界限*cmj
			BigDecimal laxjxs = BigDecimal.ZERO;
			if(laxjxGongzrtsMap!=null&&laxjxGongzrtsMap.get(lingjh)!=null){
				laxjxs = new BigDecimal(cmjs)
				.multiply(new BigDecimal((Integer)laxjxGongzrtsMap.get(lingjh)));
			}
			
			// 安全库存*cmj与*开箱界限*cmj之和
			BigDecimal addCmj = new BigDecimal(anqkcs.toString())
					.add(new BigDecimal(laxjxs.toString()));
			// 零件重箱区库存
			BigDecimal linjgzxqkc = BigDecimal.ZERO;
			linjgzxqkc = this.getZxqkcljsl(zxqkc, lingjh);
			// 开箱界限天数内开箱计划到达数量
			BigDecimal yddhljsl = BigDecimal.ZERO;
			if (yddhljslmap.get(lingjh) != null) {
				yddhljsl = (BigDecimal) yddhljslmap.get(lingjh);
			}
			// 零件仓库库存+开箱界限天数内零件重箱区库存之和
			BigDecimal allkc = new BigDecimal(yddhljsl.toString())
					.add(new BigDecimal(sumValues.toString()));
			// 设置需求数量；即是短缺数量；短缺数量：安全库存*cmj与*开箱界限*cmj之和-零件仓库库存+开箱界限天数内零件重箱区库存之和
			BigDecimal xuqsl = BigDecimal.ZERO;
			xuqsl = new BigDecimal(addCmj.toString()).subtract(new BigDecimal(
					allkc.toString()));
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
			// 断点时间=当前计算日期+(零件仓库库存+拉箱界限天数内零件重箱区库存之和)/CMJ
			String duandsj = laxjhService.getDuandsj(days, currentDate);
			// 设置修改员
			xuq.setEditor(username);
			// 设置修改时间
			String currentDay = DateUtil.getCurrentDate();
			xuq.setEditTime(currentDay);
			xuq.setDuandsj(duandsj);
			if (xuqsl.compareTo(BigDecimal.ZERO) == 1) {
				updateList.add(xuq);
			} else if (xuqsl.compareTo(BigDecimal.ZERO) == -1) {
				deleteList.add(xuq);
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("linsxuq.updateLinsxuq", updateList);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("linsxuq.deleteLinsxuqInfo", deleteList);
		return msg;
	}

	/**
	 * 获取零件重箱区库存零件数量
	 * 
	 * @param linjgzxqkc
	 * @param zxqkc
	 * @param lingjh
	 * @return
	 */
	public BigDecimal getZxqkcljsl(Map zxqkc, String lingjh) {
		BigDecimal tempLinjgzxqkc = BigDecimal.ZERO;
		if (zxqkc.get(lingjh) != null) {
			tempLinjgzxqkc = (BigDecimal) zxqkc.get(lingjh);
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
	public BigDecimal getLjsls(Map ljkcMap, Map tdjMap, String lingjh) {
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
		List tdjListBylingjh = new ArrayList();
		// 判空
		if (tdjMap.get(lingjh) != null) {
			tdjListBylingjh = (List) tdjMap.get(lingjh);
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
	 * 
	 * @param tempTdjMap
	 * @param allTdjList
	 * @return
	 */
	public Map<String, List<String>> getTdjMap(
			List<Map<String, Object>> allTdjList) {
		Map<String, List<String>> tempTdjMap = new HashMap<String, List<String>>();
		for (Map<String, Object> map : allTdjList) {// 遍历循环集合
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
	 * 获取所有替代件信息
	 * 
	 * @param tempAllTdjList
	 * @param laxjh
	 * @return
	 */
	public List<Map<String, Object>> getAlllTdjList(Kaixjh kaixjh) {
		List<Map<String, Object>> tempAllTdjList = new ArrayList<Map<String, Object>>();
		tempAllTdjList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kaixjh.getAllTdj", kaixjh);// 查询所有替代件
		return tempAllTdjList;
	}

	/**
	 * 获取所有零件库存
	 * 
	 * @param laxjh
	 * @return
	 */
	public List<Map<String, Object>> getAllLjkc(Kaixjh kaixjh) {
		List<Map<String, Object>> ljkcList = new ArrayList<Map<String, Object>>();
		ljkcList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kaixjh.getAllLjkc", kaixjh);// 查询所有库存信息
		return ljkcList;
	}

	/**
	 * 获取零件数量Map
	 * 
	 * @param tempYddhljslmap
	 * @param yddhljslList
	 * @return
	 */
	public Map getljslMap(List<Map<String, Object>> yddhljslList) {
		Map<String, BigDecimal> tempYddhljslmap = new HashMap<String, BigDecimal>();
		// 遍历预定到货计划数量集合
		for (Map<String, Object> map : yddhljslList) {
			// 零件编号
			String lingjh = (String) map.get("LINGJBH");
			// 零件数量集合
			BigDecimal lingjsl = (BigDecimal) map.get("SUMLINGJSL");
			tempYddhljslmap.put(lingjh, lingjsl);
		}
		return tempYddhljslmap;
	}

	/**
	 * 获取预计到货数量
	 * 
	 * @param tc
	 * @return
	 */
	public List<Map<String, Object>> getYujdhsl(TC tc) {
		List<Map<String, Object>> yddhljslList = new ArrayList<Map<String, Object>>();
		yddhljslList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kaixjh.getYddhljsl", tc);
		return yddhljslList;
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
	 * @param jihydmMap
	 *            计划员代码map集合
	 * @param username
	 *            用户名
	 */
	/*public void bianliList(List<Map<String, Object>> list, String usercenter, String username) {
		List linstcljList = new ArrayList();
		for (Map<String, Object> map : list) {
			// TC号
			String tcNo = (String) map.get("TCNO");
			// 启运时间
			Date qiysj = (Date) map.get("QIYSJ");
			// 物理点
			String mudd = (String) map.get("MUDD");
			// 最新物理点
			String zuiswld = (String) map.get("ZUISWLD");
			// 最新预计到达时间
			Date zuixyjddsj = (Date) map.get("ZUIXYJDDSJ");
			// 拉箱指定到达时间
			Date laxzdddsj = (Date) map.get("LAXZDDDSJ");
			// 零件编号
			String lingjbh = (String) map.get("LINGJBH");
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
				String yjddsjs = DateUtil.dateFromat(zuixyjddsj, "yyyy-MM-dd");
				tclj.setYujddsj(yjddsjs);
			}
			// 设置拉箱指定到达时间
			if (laxzdddsj != null && !"null".equals(laxzdddsj)) {
				String laxzdddsjs = DateUtil
						.dateFromat(laxzdddsj, "yyyy-MM-dd");
				tclj.setLaxzdddsj(laxzdddsjs);
			}
			tclj.setCreator(username);
			String createTime = DateUtil.curDateTime();
			tclj.setCreateTime(createTime);
			linstcljList.add(tclj);
		}
		this.insertLinsTclj(linstcljList);
	}
*/
	/**
	 * 插入临时需求
	 * 
	 * @param list
	 */
	public void insertLinsXuq(List<LinsXuq> list) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS)
		.executeBatch("linsxuq.insertLinsxuq", list);
	}

	/**
	 * 获取重箱区库存
	 * 
	 * @param zxqkcMap
	 * @param zxqkcList
	 * @return
	 */
	public Map getZxqkc(List<Map<String, Object>> zxqkcList) {
		Map zxqkcMap = new HashMap();
		for (Map map : zxqkcList) {// 遍历重箱区库存
			String lingjh = (String) map.get("LINGJH");// 零件号
			BigDecimal sumlingjsl = (BigDecimal) map.get("SUMLINGJSL");// 零件数量
			zxqkcMap.put(lingjh, sumlingjsl);// 设置重箱区零件号与零件数量对应
		}
		return zxqkcMap;
	}

	/**
	 * 插入临时TC零件数据
	 * 
	 * @param linsTcljList
	 */
	/*public void insertLinsTclj(List<LinsTclj> linsTcljList) {
		for (LinsTclj tclj : linsTcljList) {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"linstclj.insertLinsTclj", tclj);
		}
	}*/

	/**
	 * 根据用户中心查出重箱区库存
	 * 
	 * @param usercenter
	 * @return
	 */
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
		}else if (usercenter.equals("UX")) {
			zuiswld = "15A";
		}
		// 设置其物理点
		tc.setZuiswld(zuiswld);
		// 查询零件数量之和
		sumzxqkc = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("tc.getSumzxqkc", tc);
		return sumzxqkc;
	}

	/**
	 * 计算拉箱计划 计算步骤： 1、根据用户中心，清空临时表 可用TC，可以TC零件明细
	 * 2、取得《拉箱计算参数设置》画面上设置的各计算参数:用户中心、安全库存、拉箱范围、计算策略
	 * 3、将"报警零件列表"画面修改结果，更新到"临时表 临时需求"，如果未修改，则不需要进行保存
	 * 4、取得"临时表临时需求",如果该表中没有记录，则表示未来没有需要拉箱计算的短缺的KD零件， 后续计算处理终止，有记录则计算继续
	 * 5、取得可用TC号信息，并更新到"临时表 可用TC"、"临时表 可用TC零件明细"
	 * 6、计算每个集装箱对短缺零件的"满意度"；并更新"临时表_可用TC明细"的满意度
	 * 7、根据"临时表_可用TC明细"，计算集装箱的满意度，并将结果更新到"临时表_可用TC"
	 * 8、根据计划员指定的计算策略(品种满足优先/滞箱时间优先), 从"临时表 可用TC"中依次选取最能满足计算策略的集装箱，生成拉箱计划
	 * 9、生成"拉箱/开箱计划未满足零件列表"
	 * 
	 * @param xqmx
	 * @param bean
	 * @return
	 * @throws ParseException 
	 */
	@Transactional
	public String jsKaixjh(LinsXuq linsXuq, Kaixjh bean, LoginUser loginUser,
			String wuldgk) throws ParseException {
		// 根据用户中心清空临时表 可有TC,可用TC零件明细
		// 删除条件值
		String tjValue = bean.getUsercenter();
		LinsKeytc keytc = new LinsKeytc();
		keytc.setUsercenter(tjValue);
		// 删除XQJS_LINS_KEYTC
		laxjhService.deleteLinsKeyTc(keytc);
		// 删除XQJS_LINS_KEYTCLJ
		LinsKeytclj keytclj = new LinsKeytclj();
		keytclj.setUsercenter(tjValue);
		laxjhService.deleteLinsKeyTclj(keytclj);
		// 获取"临时表临时需求"
		// 开箱界限天数
		BigDecimal kaixjx = bean.getKaixjx();
		String currentDay = DateUtil.getCurrentDate();
		String zuixyjddsj = DateUtil.dateAddDays(currentDay, kaixjx.intValue()-1);// 开箱界限时间
		List<LinsXuq> list = new ArrayList<LinsXuq>();
		String now = DateUtil.getCurrentDate();
		String nowAdd1 = DateUtil.dateAddDays(now, 1);
		linsXuq.setDuandsj(laxjhService.getDuandsj(new BigDecimal("1"), nowAdd1));
		linsXuq.setLaxjxDuandsj(zuixyjddsj);
		list = this.getLingjbj(linsXuq);
		int size = list.size();
		String msg = "1";
		// 用户中心
		String usercenter = bean.getUsercenter();
		
		Map<String,String> tjmap = new HashMap<String,String>();
		tjmap.put("zuixyjddsj", zuixyjddsj);// 设置预计到达时间时间
		String zuiswld = "";
		if (usercenter.equals("UW")) {
			zuiswld = "107";
		} else if (usercenter.equals("UL")) {
			zuiswld = "108";
		}else if (usercenter.equals("UX")) {
			zuiswld = "15A";
		}
		tjmap.put("wuld", zuiswld);
		if (size == 0) {
			msg = "0";
		} else if (size > 0) {
			// 获取"临时表 TC零件"
			//List<LinsTclj> tcljList = this.getLinsTclj(tclj);
			// 初始化零件TCMAP信息
			//Map lingjTcMap = new HashMap();
			// 将所有集装箱中的零件分类
			//lingjTcMap = this.getLingjtcMap(tcljList);
			LaxjhBean laxjhBean = this.getBeans(list, tjValue,tjmap);
			// 初始化可用TC零件list
			List<LinsTclj> keyTcljList = new ArrayList<LinsTclj>();
			// 获取可用TC零件List
			keyTcljList = laxjhBean.getKeyTcljList();
			Map duanqljsjMap = laxjhBean.getDuanqljsjMap();
			if (keyTcljList.size() > 0) {
				// 获取短缺零件map
				Map duanqlingjMap = laxjhBean.getDuanqlingjMap();
				// 初始化TC零件集合
				List<Map<String, Object>> lingjslList = new ArrayList();
				// 初始化零件实体BEAN
				LinsTclj linsTclj = new LinsTclj();
				// 设置用户中心
				linsTclj.setUsercenter(usercenter);
				linsTclj.setZuixyjddsj(zuixyjddsj);
				
				
				// 获取TCLJ零件集合
				lingjslList = laxjhService.getSumLingjslByTc(linsTclj);
				// 初始化满意度MAP
				Map manydMap = new HashMap();
				// 获取TC零件MAP
				Map tcLingjMap = new HashMap();
				List<String> tcNoList = new ArrayList();
				// 获取所有零件数量List
				LaxjhBean lBean = this.getBeansBysumLingjslList(lingjslList,
						duanqlingjMap,duanqljsjMap);
				// 获取满意度map
				manydMap = lBean.getManydMap();
				tcLingjMap = lBean.getTcLingjMap();
				tcNoList = lBean.getTcNoList();
				Map minduandsjmap = lBean.getMinduandsjmap();
				List duandljList = lBean.getDuanqljList();
				// 新增到可用TC,可用TC明细
				String username = loginUser.getUsername();
				// 初始化可用TC集合
				Map newTcMap = new HashMap();
				// 获取滞箱天数
				List<Map<String, Object>> zxtsList = new ArrayList();
				zxtsList = this.getZxts(zuiswld);
				Map zxtsMap = new HashMap();
				zxtsMap = laxjhService.getZxtsMap(zxtsList);
				// 插入可用TC零件和明细
				try {
					laxjhService.insertKaixKeytcAndlj(keyTcljList, username, newTcMap,
							zxtsMap);
				} catch (Exception e) {
					msg = "2";
				}
				if (msg.equals("1")) {
					// 更新零件满意度
					this.updateKeytcAndlj(tcNoList, tcLingjMap, manydMap,
							username);
					// 根据计算策略，生成拉箱计划和"拉箱未满足零件列表"
					Kaixjh kaixjh = new Kaixjh();
					// 用户中心
					String jhUc = bean.getUsercenter();
					// 毛需求版次
					String jhmaoxbc = linsXuq.getMaoxqbc();
					// 计算算法策略
					String jhSuanfcl = bean.getSuanfcl();
					// 开箱界限
					BigDecimal jhKaixjx = bean.getKaixjx();
					// 安全库存
					BigDecimal jhAnqkc = BigDecimal.ZERO;
					if (bean.getAnqkc() != null) {
						jhAnqkc = bean.getAnqkc();
					}
					String creator = username;
					String createTime = DateUtil.curDateTime();
					Calendar calendar = new GregorianCalendar();
					/**
					 * 开箱计划号生成逻辑: 1、开箱计划号是10位 2、前6位是用户中心(2位)+当前年份,后4位是流水号
					 * 3、流水号是从数据库中XQJS_KAIXJH中查出最大的流水号，再加一
					 * 4、如果流水号为一年的第二位，则将0000去掉，重新赋为0，再加1
					 */
					int year = calendar.get(Calendar.YEAR);
					String tempjhh = usercenter + "K" + year;
					String kaixjhh = "";
					String kaixjhhs = "";
					String tempkaixjhh = this.getKaixjhh(tempjhh);
					if (tempkaixjhh == null || "".equals(tempkaixjhh)) {
						kaixjhhs = "000";
					} else {
						// 获取其长度
						int length = tempkaixjhh.length();
						// 获取其后4位
						String temp = tempkaixjhh.substring(length - 3, length);
						String newString = "";
						if (temp.equals("000")) {
							newString = "0";
						} else {
							newString = LaxjhService.getNewString(temp);
						}
						// 变成整型+1;
						Integer kaixjhhI = Integer.valueOf(newString) + 1;
						// 然后将其变成3位字符串，不够三位补零
						kaixjhhs = String.format("%03d", kaixjhhI);
					}
					// 开箱计划号
					kaixjhh = tempjhh + kaixjhhs;
					kaixjh.setKaixjhh(kaixjhh);
					// 用户中心
					kaixjh.setUsercenter(jhUc);
					// 毛需求版次
					kaixjh.setBanc(jhmaoxbc);
					// 算法策略
					kaixjh.setSuanfcl(jhSuanfcl);
					// 开箱界限
					kaixjh.setKaixjx(jhKaixjx);
					// 安全库存
					kaixjh.setAnqkc(jhAnqkc);
					// 创建者
					kaixjh.setCreator(creator);
					// 创建时间
					kaixjh.setCreateTime(createTime);
					// 生效状态
					kaixjh.setShengxzt("0");
					kaixjh.setJihy(loginUser.getJihyz());
					// 插入开箱计划
					this.insertKaixjh(kaixjh);
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
					Map tcljManzslMap = new HashMap();
					List kaixjhmxTcList = new ArrayList();
					// 根据上一步取得的可用集装箱的"TC"号,从"临时表 临时需求"和"临时表 可用TC"中取得集装箱相关信息插入到"拉箱计划明细"
					LaxjhBean newLaxjhBean = this.getTcInfo(keyList,
							duanqlingjMap, tcNoKeytcljMap, duandljList);
					tcljManzslMap = newLaxjhBean.getTcljManzslMap();
					kaixjhmxTcList = newLaxjhBean.getLaxjhmxTcList();
					List<Map<String, Object>> newKeyTcList = new ArrayList();
					Map suanfclMap = new HashMap();
					suanfclMap.put("suanfcl", jhSuanfcl);
					newKeyTcList = this.getSelectKaixjhKeytc(suanfclMap);
					// 插入开箱计划明细
					this.insertKaixjhmx(newKeyTcList, kaixjhmxTcList, kaixjh,minduandsjmap);
					// 更新"临时表_可用TC"中的"TC状态"
					this.updateKeyTcbyzt(newTcKeyList, username);
					// 更新"临时表 临时需求"的满意数量:"满足数量"="满足数量"+"临时表  可用TC零件明细"
					// TC中的"零件数量"
					this.updateLinsxuq(keyList, tcljManzslMap, linsXuq,
							username);
					// 将未满足数量插入到拉箱未满足零件列表
					List weimzlist = this.getWeimzljInfo(usercenter);
					laxjhService.insertWeimzlj(weimzlist, kaixjhh, username);
				}
			} else {
				msg = "2";
			}
		}
		return msg;
	}

	/**
	 * @param bean
	 * @return
	 */
	private List<Map<String, Object>> getKeytcBySuanfcl(Kaixjh bean) {
		List tcManzlist = new ArrayList();
		tcManzlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxjh.getKeytcBySuanfcl", bean);
		return tcManzlist;
	}
	/**
	 * 未满足零件信息
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
	public String getKaixjhh(String tempjhh) {
		Map jhhMap = new HashMap();
		jhhMap.put("kaixjhh", tempjhh);
		Map map = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kaixjh.getKaixjhh", jhhMap);
		return (String) map.get("KAIXJHH");
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
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("linskeytc.updateKeytcByZt", list);
	}

	/**
	 * 插入开箱明细
	 * 
	 * @param newKeyTcList
	 * @param kaixjhmxTcList
	 * @param kaixjh
	 */
	public void insertKaixjhmx(List<Map<String, Object>> newKeyTcList,
			List kaixjhmxTcList, Kaixjh kaixjh,Map minduandsjmap) {
		List<Map<String,String>> tcIdList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("tc.getTcIdList");
		Map<String,String> tcIdmap = new HashMap<String,String>();
		for(int i=0;i<tcIdList.size();i++){
			Map<String,String> map = tcIdList.get(i);
			String id = map.get("ID");
			String tcNo = map.get("TCNO");
			tcIdmap.put(tcNo, id);
		}
		List list = new ArrayList();
		Date keyTcLaxzdsj =(Date)minduandsjmap.get("tcNoDuandsj");
		String keyTcLaxzdsjS = "";
		if(keyTcLaxzdsj!=null){
			keyTcLaxzdsjS = DateUtil.dateAddDays(DateUtil.dateToStringYMD(keyTcLaxzdsj), -1);
		}
		String kaixzdsj = this.getGongzrsj(keyTcLaxzdsjS);
		List tempList = new ArrayList();
		for (Map map : newKeyTcList) {
			// 用户中心
			String keyTcuc = (String) map.get("USERCENTER");
			// TC号
			String keyTcno = (String) map.get("TCNO");
			// 启运时间
			Date keyTcQiysj = (Date) map.get("QIYSJ");
			
			// 物理点
			String wuld = (String) map.get("WULD");
			String id = (String)tcIdmap.get(keyTcno);
			String tempInfo = keyTcuc + ":" + keyTcno;
			if (kaixjhmxTcList.contains(tempInfo)) {
				// 初始化开箱计划明细
				Kaixjhmx kaixjhmx = new Kaixjhmx();
				// 开箱计划号
				kaixjhmx.setKaixjhNo(kaixjh.getKaixjhh());
				// 用户中心
				kaixjhmx.setUsercenter(keyTcuc);
				// 可用TC号
				kaixjhmx.setId(id);
				// 物理点
				kaixjhmx.setTcNo(keyTcno);
				// 启运时间
				String qiysj = "";
				if(keyTcQiysj!=null&&!"".equals(keyTcQiysj.toString())){
					 qiysj = DateUtil.dateFromat(keyTcQiysj, "yyyy-MM-dd");
				}
				kaixjhmx.setQiysj(qiysj);
				kaixjhmx.setWuld(wuld);
				kaixjhmx.setKaixzdsj(kaixzdsj);
				// TC状态为2，为拉箱预定
				kaixjhmx.setZhuangt("2");
				kaixjhmx.setCreator(kaixjh.getCreator());
				kaixjhmx.setCreateTime(kaixjh.getCreateTime());
				//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kaixjhmx.insertKaixjhmx", kaixjhmx);
				String key = keyTcuc +":"+id+":"+keyTcno;
				if(!tempList.contains(key)){
					list.add(kaixjhmx);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kaixjhmx.insertKaixjhmx", kaixjhmx);
				}
				tempList.add(key);
			}
		}
		//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kaixjhmx.insertKaixjhmx", list);
	}
	public String getGongzrsj(String keyTcLaxzdsjS) {
		String gongzr = "";
		//String dinhck = (String) wulljMap.get(usercenter + ":" + lingjh);
		//String rilbc = cgmap.get(xqmx.getUsercenter() + ":" + dinhck);
		String rilbc = Const.LAXBANCI;
		Map <String,String> rilbcMap = new HashMap<String,String>();
		rilbcMap.put("rilbc", rilbc);
		rilbcMap.put("currentDate", keyTcLaxzdsjS);
		Map result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kaixjh.getMaxgzrsj", rilbcMap, 1, 1);
		List resultList = (List)result.get("rows");
		if(resultList!=null&&resultList.size()>0){
			Map gzrsjMap = (Map)resultList.get(0);
			gongzr = (String)gzrsjMap.get("MAXGONGZR");
		}
		return gongzr;
	}
	/**
	 * 获取拉箱计划可用TC 查询字段:
	 * 1、USERCENTER;2、TCNO；3、QIYSJ;4、ID；5、LAXZDDDSJ(需求中最小断点时间-1) 查询表:
	 * XQJS_LINS_KEYTCLJ;XQJS_TC;XQJS_LINS_XUQ 根据计算算法策略进行排序：
	 * 1、算法策略为1,则按照TCMYD进行排序 2、算法策略为2，则按照ZHIXSJ进行排序
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getSelectKaixjhKeytc(Map suanfclMap) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kaixjh.selectKaixjhKeytc", suanfclMap);
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
			Map tcNoKeytcljMap, List duandljList) {
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
					if (duandljList != null && !duandljList.contains(tempTcKeyLingj)) {
						slValue = tempTclj.getLingjsl();
					}
					sumLingjsls = sumLingjsls.add(new BigDecimal(slValue
							.toString()));
					tcljManzslMap.put(tempKey, sumLingjsls);
					map.put(tempKey, slValue);
				} else if (tcljManzslMap.get(tempKey) != null) {
					BigDecimal slValue = BigDecimal.ZERO;
					if (duandljList != null && !duandljList.contains(tempTcKeyLingj)) {
						slValue = tempTclj.getLingjsl();
					}
					sumLingjsls = sumLingjsls.add(new BigDecimal(slValue
							.toString()));
					tcljManzslMap.put(tempKey, sumLingjsls);
					map.put(tempKey, slValue);
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
	 * @param kaixjh
	 */
	public void insertKaixjh(Kaixjh kaixjh) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"kaixjh.insertKaixjh", kaixjh);
	}

	/**
	 * 获取用户中心下可用零件明细的集合
	 * 
	 * @param bean
	 * @return
	 */
	public List<LinsKeytclj> getNewKeyTcljList(Kaixjh bean) {
		List newKeyTcljList = new ArrayList();
		Map tjMap = new HashMap();
		tjMap.put("usercenter", bean.getUsercenter());
		tjMap.put("suanfcl", bean.getSuanfcl());
		newKeyTcljList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linskeytclj.selectKeyTclj", tjMap);
		return newKeyTcljList;
	}

	/**
	 * 将LinsKeytclj信息拆分， 以UC、计划员代码、TC号、零件号为key 并且将key与LinsKeytclj放入map中
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
			LinsKeytc keytc = new LinsKeytc();
			keytc.setUsercenter(tempUc);
			keytc.setJihydm(tempJihydm);
			keytc.setTcNo(tempTcNo);
			keytc.setTcmyd(tcManyd);
			keytc.setEditor(username);
			keytc.setEditTime(DateUtil.curDateTime());
			keytc.setTczt("1");
			batchkeyTcList.add(keytc);
		}
		laxjhService.batchUpdateKeytclj(batchKeytcljList);
		laxjhService.batchUpdateKeytc(batchkeyTcList);
	}
	/**
	 * 
	 * @param sumLingjslList
	 * @param duanqlingjMap
	 * @return
	 * @throws ParseException 
	 */
	public LaxjhBean getBeansBysumLingjslList(
			List<Map<String, Object>> sumLingjslList, Map duanqlingjMap,Map duanqljsjMap) throws ParseException {
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
		String currentDate = DateUtil.getCurrentDate();
		String tempDate = DateUtil.dateAddDays(currentDate, 1);
		Date tempDateD = DateUtil.stringToDateYMD(tempDate);
		List duanqljList = new ArrayList();
		String minDuandsj = "";
		Map minduandsjmap =new HashMap();
		for (Map<String, Object> map : sumLingjslList) {
			String tcUc = (String) map.get("USERCENTER");
			String tcjihydm = (String) map.get("JIHYDM");
			String tcNo = (String) map.get("TCNO");
			String lingjh = (String) map.get("LINGJH");
			String tempKey = tcUc + ":" + tcjihydm + ":" + tcNo;
			String duandsj = "";
			if(duanqljsjMap.get(lingjh)!=null){
				duandsj = (String)duanqljsjMap.get(lingjh);
			}
			Date duandsjD = DateUtil.stringToDateYMD(duandsj);
			if(null!=duandsjD&&!"".equals(duandsjD.toString())){
				if(tempDateD.compareTo(duandsjD)<0){
					if (!tcNoList.contains(tempKey)) {
						tcNoList.add(tempKey);
					}
					if(minduandsjmap.get("tcNoDuandsj")==null){
						minduandsjmap.put("tcNoDuandsj", duandsjD);
					}else if(minduandsjmap.get("tcNoDuandsj")!=null){
						Date temp = (Date)minduandsjmap.get("tcNoDuandsj");
						if(duandsjD.compareTo(temp)<0){
							minduandsjmap.put("tcNoDuandsj", duandsjD);
						}
					}
					
				}else {
					duanqljList.add(tcUc + ":" + tcjihydm +":"+tcNo+ ":"
							+ lingjh);
				}
			}
			
			
			String lingjkey = tempKey + ":" + lingjh;
			BigDecimal lingjsl = (BigDecimal) map.get("LINGJSL");
			// 计算每个集装箱对短缺零件的"满意度"
			BigDecimal duanqsl = BigDecimal.ZERO;
			if (duanqlingjMap.get(lingjh) != null) {
				if(duanqlingjMap.get(lingjh)!=null){
					duanqsl = (BigDecimal) duanqlingjMap.get(lingjh);
				}
				if(lingjsl==null){
					lingjsl = BigDecimal.ZERO;
				}
				BigDecimal tempManyd = lingjsl.divide(duanqsl, 4,
						BigDecimal.ROUND_HALF_EVEN);
				if (tempManyd.compareTo(BigDecimal.ONE) == 1) {
					manydMap.put(lingjkey, BigDecimal.ONE);
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
		LaxjhBean bean = new LaxjhBean();
		bean.setManydMap(manydMap);
		bean.setTcLingjMap(tcLingjMap);
		bean.setTcNoList(tcNoList);
		bean.setDuanqljList(duanqljList);
		bean.setMinduandsjmap(minduandsjmap);
		return bean;
	}

	/**
	 * 
	 * @param list
	 *            临时需求
	 * @param lingjTcMap
	 *            集装箱中的零件分类
	 * @return
	 */
	public LaxjhBean getBeans(List<LinsXuq> list,  String usercenter,Map tjmap) {
		// 用于存可用TC的list
		LaxjhBean bean = new LaxjhBean();
		List<LinsTclj> tcljlist = new ArrayList<LinsTclj>();
		tcljlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linstclj.getLinsTclj");
		List<Map<String,String>> tclist = new ArrayList<Map<String,String>>();
		tclist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linstclj.getKaiTc",tjmap);
		Map duanqljsjMap = new HashMap();
		// 用于存可用TC的list
		List<LinsTclj> keyTcljList = new ArrayList();
		Map duanqlingjMap = new HashMap();
		List<LinsTclj> newtcljList = new ArrayList<LinsTclj>();
		Map<String,LinsTclj> tcljMap = new HashMap();
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
				if(tcljMap.get(tcNo + ":" + lingjbh)!=null){
					keyTcljList.add(tcljMap.get(tcNo + ":" + lingjbh));
				}
			}
		}
		bean.setDuanqlingjMap(duanqlingjMap);
		bean.setKeyTcljList(keyTcljList);
		bean.setDuanqljsjMap(duanqljsjMap);
		tclist.clear();
		list.clear();	
		//Date date1 = new Date();
		return bean;
	}

	/**
	 * 
	 * @param tcljList
	 * @return
	 */
	public Map getLingjtcMap(List<LinsTclj> tcljList) {
		/**
		 * 判断逻辑：一个零件可能存在于多个集装箱，所以将各个集装箱中的零件取出
		 * 通过map进行对应，如果零件号在MAP中没有对应值，则新建一个LIST，将零件数据放入List中，再用map对应
		 * 如果零件号对应在MAP中有值，则将其所对应的取出，将零件数据放入List中，再用map对应值
		 */
		Map lingjhMap = new HashMap();
		for (LinsTclj linsTclj : tcljList) {
			String lingjh = linsTclj.getLingjh();
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
		return lingjhMap;
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

	/**
	 * 拉箱计划可利用TC信息的条件: 1、根据表"LINSTC中的TC号.最新预计到达神龙时间"<系统现在时间+"拉箱范围" 2、开箱指定时间 is
	 * null 3、拉装箱中的零件必须包含"KD资源短缺清单"中任何一种
	 * 
	 * @param xqmx
	 * @return
	 */
	public List getLinsTclj(LinsTclj tclj) {
		List<LinsTclj> list = new ArrayList();
		list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("linstclj.getKaixLinsTclj", tclj);
		return list;
	}

	/**
	 * 获取开箱报警数据
	 * 
	 * @param xuqbc
	 * @return
	 * @throws JSONException
	 */
	public Map queryLingjbj(LinsXuq bean, Pageable page, String anqkc,
			String kaixjx) throws JSONException {
		Map<String, Object> map = new HashMap();
		Map tjmap = new HashMap();
		tjmap.put("anqkc", anqkc);
		tjmap.put("kaixjx", kaixjx);
		tjmap.put("usercenter", bean.getUsercenter());
		tjmap.put("maoxqbc", bean.getMaoxqbc());
		tjmap.put("lingjh", bean.getLingjh());
		tjmap.put("jihydm", bean.getJihydm());
		map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("linsxuq.queryLingjbjKaix", tjmap, page);
		return map;
	}


	/**
	 * @param kaixjh
	 * @param kaixjh2
	 * @return
	 */
	public Map queryKaixjh(Kaixjh kaixjh, Pageable page) {
		Map<String, Object> map = new HashMap();
		map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kaixjh.queryKaixjh", kaixjh, page);
		return map;
	}

	/**
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
	 * @param linsXuq
	 * @param loginUser
	 */
	public void saveLinsxuq(LinsXuq linsXuq, LoginUser loginUser) {
		laxjhService.setLinsxuqSingle(linsXuq, loginUser,"2");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"linsxuq.insertLinsxuqInfo", linsXuq);
	}

	/**
	 * @param laxjhh
	 * @return
	 */
	public Map queryKaixjhmx(Kaixjhmx mx, Map tjMap) {
		Map<String, Object> resultMap = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages(
				"kaixjhmx.queryKaixjhmxInfo", tjMap, mx);
		return resultMap;
	}

	/**
	 * @param laxjh
	 * @return
	 */
	public Map queryKaixjhWeimzlj(Weimzlj weimzlj) {
		Map tjMap = new HashMap();
		tjMap.put("jihNo", weimzlj.getJihNo());
		Map<String, Object> resultMap = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages(
				"weimzlj.queryLaxjhWeimzlj",tjMap, weimzlj);

		return resultMap;
	}

	/**
	 * 
	 */
	public void editKaixjhmx(Kaixjhmx kaixjhmx) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"kaixjhmx.deleteKaixjhmx", kaixjhmx);
	}

	/**
	 * @param laxjhmx
	 */
	public void saveKaixjhmx(Kaixjhmx kaixjhmx, LoginUser loginUser) {
		// 死数据
		kaixjhmx.setZhuangt("2");
		kaixjhmx.setCreator(loginUser.getUsername());
		kaixjhmx.setCreateTime(DateUtil.getCurrentDate());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"kaixjhmx.insertKaixjhmx", kaixjhmx);
	}

	/**
	 * @param kaixjh
	 * @param loginUser
	 */
	@Transactional
	public void sxKaixjh(Kaixjh kaixjh, LoginUser loginUser) {
		kaixjh.setShengxzt("1");
		String username = loginUser.getUsername();
		String currentDate = DateUtil.getCurrentDate();
		kaixjh.setEditor(username);
		kaixjh.setEditTime(currentDate);
		Kaixjhmx mx = new Kaixjhmx();
		String kaixjhNo = kaixjh.getKaixjhh();
		String usercenter = kaixjh.getUsercenter();
		mx.setKaixjhNo(kaixjhNo);
		mx.setUsercenter(usercenter);
		List<Map<String, Object>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
				"kaixjhmx.queryKaixjhmx", mx);
		for (Map<String, Object> map : list) {
			String tcNo = (String) map.get("TCNO");
			String kaixzdsj = (String) map.get("KAIXZDSJ");
			Map mxMap = new HashMap();
			mxMap.put("kaixjhNo", kaixjhNo);
			mxMap.put("tcNo", tcNo);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"kaixjhmx.deleteKaixjhmxOther", mxMap);
			String id = (String) map.get("ID");
			Map tcMap = new HashMap();
			tcMap.put("kaixzdsj", kaixzdsj);
			tcMap.put("editor", username);
			tcMap.put("editTime", currentDate);
			tcMap.put("tcNo", tcNo);
			tcMap.put("id", id);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"tc.updateKaixzdsj", tcMap);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"kaixjh.changeZtKaixjh", kaixjh);
	}

	/**
	 * @param kaixjh
	 * @param loginUser
	 */
	@Transactional
	public void qxKaixjh(Kaixjh kaixjh, LoginUser loginUser) {
		kaixjh.setShengxzt("2");
		String username = loginUser.getUsername();
		String currentDate = DateUtil.getCurrentDate();
		kaixjh.setEditor(username);
		kaixjh.setEditTime(currentDate);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"kaixjh.changeZtKaixjh", kaixjh);
		Kaixjhmx mx = new Kaixjhmx();
		String kaixjhNo = kaixjh.getKaixjhh();
		String usercenter = kaixjh.getUsercenter();
		mx.setKaixjhNo(kaixjhNo);
		mx.setUsercenter(usercenter);
		List<Map<String, Object>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
				"kaixjhmx.queryKaixjhmx", mx);
		for (Map<String, Object> map : list) {
			Map mxMap = new HashMap();
			mxMap.put("shengxzt", "2");
			mxMap.put("editor", username);
			mxMap.put("editTime", currentDate);
			mxMap.put("kaixjhNo", kaixjhNo);
			mxMap.put("usercenter", usercenter);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"kaixjhmx.deleteKaixjhmxbyUc", mxMap);
		}
	}

	/**
	 * @param tjMap
	 * @return
	 */
	public String validateKaixjhmxTcNo(Map<String, String> tjMap) {
		Integer count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(
				"kaixjhmx.validateKaixjhmxTcNo", tjMap);
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
	 * @throws ParseException 
	 */
	public Map<String, Object> kaixjhDownLoadFile(Map<String, String> tjMap) throws ParseException {
		Map<String, Object> dataSource = new HashMap<String, Object>();
		//String kaixjhh = (String) tjMap.get("kaixjhNo");
		List<Kaixjh> laxjhlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kaixjh.queryKaixjh");
		Kaixjh kaixjh = laxjhlist.get(0);
		BigDecimal kaixjx = kaixjh.getKaixjx();
		BigDecimal anqkc = BigDecimal.ZERO;
		if (kaixjh.getAnqkc() != null) {
			anqkc = kaixjh.getAnqkc();
		}
		dataSource.put("kaixjx", kaixjx);
		dataSource.put("anqkc", anqkc);
		String jskaisriq = kaixjh.getCreateTime();
		jskaisriq = jskaisriq.substring(0, 10);
		String jsjiesriq = DateUtil.dateAddDays(jskaisriq, kaixjx.intValue());
		Date jskaisriqD = DateUtil.stringToDateYMD(jskaisriq);
		String jskaisriqS = DateUtil.dateFromat(jskaisriqD, "yyyy-MM-dd");
		dataSource.put("jskaisriq", jskaisriqS);
		dataSource.put("jsjiesriq", jsjiesriq);
		List<Map<String, Object>> mxList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
				"kaixjhmx.queryKaixjhmxDownload", tjMap);
		Map<String, Object> tcMap = new HashMap<String, Object>();
		for (Map<String, Object> map : mxList) {
			// 用户中心
			String usercenter = (String) map.get("USERCENTER");
			// 拉箱计划号
			String kaixjhNo = (String) map.get("KAIXJHNO");
			// ID号
			String id = (String) map.get("ID");
			// TC号
			String tcNo = (String) map.get("TCNO");
			// 拉箱指定时间
			String kaixzdsj = (String) map.get("KAIXZDSJ");
			// 启运时间
			String qiysj = (String) map.get("QIYSJ");
			// 物理点
			String wuld = (String) map.get("WULD");
			// 到货车间
			String dinghcj = (String) map.get("DINGHCJ");
			Kaixjhmx kaixjhmx = new Kaixjhmx();
			kaixjhmx.setUsercenter(usercenter);
			kaixjhmx.setKaixjhNo(kaixjhNo);
			kaixjhmx.setId(id);
			kaixjhmx.setTcNo(tcNo);
			kaixjhmx.setQiysj(qiysj);
			kaixjhmx.setKaixzdsj(kaixzdsj);
			kaixjhmx.setWuld(wuld);
			kaixjhmx.setDinghcj(dinghcj);
			String key = kaixzdsj;
			if (tcMap.get(key) == null) {
				List tcList = new ArrayList();
				tcList.add(tcNo + "(" + wuld + ")");
				tcMap.put(key, tcList);
			} else if (tcMap.get(key) != null) {
				List tcList = (List) tcMap.get(key);
				tcList.add(tcNo + "(" + wuld + ")");
				tcMap.put(key, tcList);
			}
		}
		//int allRows = 0;
		//Map rowsKeyMap = new HashMap();
		Map otherMap = new HashMap();
		Map countMap = new HashMap();
		for (Map.Entry<String, Object> entry : tcMap.entrySet()) {
			List tcNoList = (List) entry.getValue();
			String key = entry.getKey();
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
					list = (List) otherMap.get(key);
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
			}
		}
		dataSource.put("rowsKeyMap", otherMap);
		dataSource.put("countMap", countMap);
		return dataSource;
	}
}
