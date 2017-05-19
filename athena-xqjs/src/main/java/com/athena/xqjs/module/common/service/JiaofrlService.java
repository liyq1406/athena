/**
 * 代码声明
 */
package com.athena.xqjs.module.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Jiaofrl;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
/**
 * <p>
 * Title:交付日历类
 * </p>
 * <p>
 * Description:交付日历类
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
 * @date 2011-12-20
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class JiaofrlService extends BaseService{
	
	/**
	 *查询全部，返回list集合
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 * 参数说明：map
	 */
	public List<Jiaofrl> queryAllJiaofm(String usercenter,String jiaofm,String nianzq) {
		Map<String,String> map = new HashMap<String,String>() ;
		map.put("usercenter", usercenter) ;
		map.put("jiaofm", jiaofm) ;
		map.put("nianzq", nianzq) ;
		CommonFun.mapPrint(map, "交付码查询queryAllJiaofm方法参数map");
		CommonFun.logger.debug("交付码查询queryAllJiaofm方法的sql语句为：common.queryJiaofrl");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryJiaofrl", map);
	}

	/**
	 * @author wuyichao
	 * @see 根据交付码 用户中心 交付日期 得到交付日历
	 * @param usercenter
	 * @param jiaofm
	 * @param jiaofrq
	 * @return
	 */
	public List<Jiaofrl> queryAllJiaofm(String usercenter, String jiaofm,String nianzq,
			String jiaofrq) {
		Map<String,String> map = new HashMap<String,String>() ;
		map.put("usercenter", usercenter) ;
		map.put("jiaofm", jiaofm) ;
		map.put("jiaofrq", jiaofrq) ;
		CommonFun.mapPrint(map, "交付码查询queryAllJiaofm方法参数map");
		CommonFun.logger.debug("交付码查询queryAllJiaofm方法的sql语句为：common.queryJiaofrl");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryJiaofrl", map);
	}
	
}