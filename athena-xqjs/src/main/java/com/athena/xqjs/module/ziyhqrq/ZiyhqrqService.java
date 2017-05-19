/**
 * 
 */
package com.athena.xqjs.module.ziyhqrq;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Gongyzq;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.CommonZiykzDays;
import com.athena.xqjs.module.ilorder.service.GongyzqService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * @author dsimedd001
 * 
 */
@WebService(endpointInterface = "com.athena.xqjs.module.ziyhqrq.Ziykz", serviceName = "/ziykz")
@Component
public class ZiyhqrqService extends BaseService implements Ziykz {
	@Inject
	private GongyzqService gongyzqService;
	
	/**
	 * log4j日志打印
	 */
	private final Log log = LogFactory.getLog(ZiyhqrqService.class);

	@Transactional
	public void ziyhqrqManage() {
		log.info("===========4080批量过滤过滤资源获取日期开始："+CommonFun.getJavaTime());
		int days = CommonZiykzDays.getWeekDay();
		String currentDate = CommonZiykzDays.getDate(0);// 当天
		String yesDate = CommonZiykzDays.getDate(1);// 昨天
		String mondayDate = CommonZiykzDays.getDate(days - 1);// 本周一
		String lastMondayDate = CommonZiykzDays.getDate(days + 6);// 上周一

		Map<String, String> map = new HashMap<String, String>();
		map.put("currentDate", currentDate);
		Gongyzq gongyzq = gongyzqService.queryAllGongyzq(map);//
		String zq = gongyzq.getGongyzq();// 第几个工业周期

		// 上个工业周期最后一天
		Integer zqInt = Integer.parseInt(zq);
		Integer lastZqInt = zqInt ;
		Map<String, String> lasttjMap = new HashMap<String, String>();
		lasttjMap.put("gongyzq", lastZqInt.toString());
		Gongyzq lastgongyzq = gongyzqService.queryLastGongyzq(lasttjMap);
		String currentKaissj = lastgongyzq != null ? lastgongyzq.getJiessj() : "";

		// 上上个工业周期最后一天
		lastZqInt = lastZqInt - 1;
		lasttjMap = new HashMap<String, String>();
		lasttjMap.put("gongyzq", lastZqInt.toString());
		lastgongyzq = gongyzqService.queryLastGongyzq(lasttjMap);
		String lastKaissj = lastgongyzq != null ? lastgongyzq.getJiessj() : "";

		Map<String, String> deleteMap = new HashMap<String, String>();
		deleteMap.put("currentDate", currentDate);// 当天
		deleteMap.put("yesDate", yesDate);// 昨天
		deleteMap.put("mondayDate", mondayDate);// 本周一
		deleteMap.put("lastMondayDate", lastMondayDate);// 上周一
		deleteMap.put("currentKaissj", currentKaissj);// 上个工业周期最后一天
		deleteMap.put("lastKaissj", lastKaissj);// 上上个工业周期最后一天
		
		
		//添加打印批量日志 kong  2013-06-04 
		StringBuffer bu=new StringBuffer();
		for (String key : deleteMap.keySet()) {
			bu.append(key);
			bu.append("=");
			bu.append(deleteMap.get(key));
			bu.append("|");
		}
		int n;
		try {
			log.info("===========需要保留的日期："+bu.toString());
			n = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("common.deleteZiykzb", deleteMap);
			log.info("===========4080批量过滤过滤资源获取日期结束："+CommonFun.getJavaTime()+"  删除数据"+n+"条");
		} catch (DataAccessException e) {
			log.error("===========4080批量过滤过滤资源获取日期出错"+e.toString());
		}
	}

}
