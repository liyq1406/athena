package com.athena.xqjs.module.zygzbj.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

/**
 * 资源跟踪报警计算参数
 * @author WL
 *
 */
@Component
public class ZygzbjjsParam extends BaseService {
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(ZygzbjjsParam.class);
	
	/**
	 * MAF库缓存MAP
	 */
	public static Map<String, BigDecimal> inMafMap;
	
	/**
	 * 零件消耗点MAP
	 */
	public static Map<String, Lingjxhd> lingjxhdMap;
	
	/**
	 * 零件是否多用户中心统计
	 */
	public static Map<String,Integer> countUsercenterMap;
	
	/**
	 * 日历版次MAP
	 */
	public static Map<String,String> rilbcMap;
	
	/**
	 * 加载资源跟踪报警参数
	 */
	public void initParam(Map map){
		logger.info("初始化资源跟踪报警参数开始");
		long start = System.currentTimeMillis();
		
		inMafMap = new HashMap<String, BigDecimal>();
		lingjxhdMap = new HashMap<String, Lingjxhd>();
		countUsercenterMap = new HashMap<String, Integer>();
		rilbcMap = new HashMap<String, String>();
		
		/**
		 * 汇总日历版次
		 */
		List<Map<String,String>> allRilbcList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selAllRilbc",map);
		for (int i = 0; i < allRilbcList.size(); i++) {
			Map<String,String> rilbcMap = allRilbcList.get(i);
			String key = rilbcMap.get("usercenter") + rilbcMap.get("appobj");
			rilbcMap.put(key, rilbcMap.get("rilbc"));
		}
		allRilbcList = null;
		
		/**
		 * 统计零件用户中心数,判断零件是否多用户中心
		 */
		List<Map> countList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.countAllLingjUc",map);
		for (int i = 0; i < countList.size(); i++) {
			Map countUsercenter = countList.get(i);
			//零件用户中心数
			Integer count = CommonFun.getBigDecimal(countUsercenter.get("COUNTLINGJBH")).intValue();
			//用户中心数量大于1,则为多用户中心,设置为1
			if (count > 1) {
				countUsercenterMap.put(CommonFun.strNull(countUsercenter.get("LINGJBH")), 1);
			//否则,为单用户中心使用,设置为0
			}else {
				countUsercenterMap.put(CommonFun.strNull(countUsercenter.get("LINGJBH")), 0);
			}
		}
		countList = null;
		
		/**
		 * 加载MAF库信息
		 */
		List<Map> listMaf = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selAllMaf",map);
		for (int i = 0; i < listMaf.size(); i++) {
			Map mafkcMap = listMaf.get(i);
			String usercenter = CommonFun.strNull(mafkcMap.get("C_USER_CENTER"));
			String lingjbh = CommonFun.strNull(mafkcMap.get("C_COMPONENT_CODE"));
			BigDecimal mafkc = CommonFun.getBigDecimal(mafkcMap.get("MAFNUM"));
			String key = usercenter + lingjbh;
			inMafMap.put(key, mafkc);
		}
		listMaf = null;
		
		/**
		 * 零件消耗点信息
		 */
		List<Lingjxhd> listLingjxhd = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryObject",map);
		for (int i = 0; i < listLingjxhd.size(); i++) {
			Lingjxhd lingjxhd = listLingjxhd.get(i);
			String key = lingjxhd.getUsercenter() + lingjxhd.getLingjbh() + lingjxhd.getXiaohdbh();
			lingjxhdMap.put(key, lingjxhd);
		}
		listLingjxhd = null;
		
		logger.info("加载资源跟踪报警参数结束,耗时---------------------"+(System.currentTimeMillis() - start));
	}
	
	/**
	 * 销毁
	 */
	public void destory(){
		inMafMap = null;
		lingjxhdMap = null;
		countUsercenterMap = null;
		rilbcMap = null;
		logger.info("资源跟踪报警参数销毁");
	}

}
