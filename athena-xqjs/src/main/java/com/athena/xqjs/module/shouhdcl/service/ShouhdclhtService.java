package com.athena.xqjs.module.shouhdcl.service;

import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ShouhdclhtService
 * <p>
 * 类描述：收货待处理service
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-04-06
 * </p>
 * 
 * @version 1.0
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class ShouhdclhtService extends BaseService {

	public static final Logger logger = Logger.getLogger(ShouhdclService.class);

	/**
	 * 查询拒收跟踪单
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryShouhdclht(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("shouhdcl.queryShouhdclht", param, page);
	}


}
