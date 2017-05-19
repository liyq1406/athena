package com.athena.xqjs.module.zygzbj.service;

import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ZygzService
 * <p>
 * 类描述：资源跟踪service
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-03-08
 * </p>
 * 
 * @version 1.0
 * 
 */
@SuppressWarnings("rawtypes")
@Component
public class ZygzService extends BaseService {

	/**
	 * 查询资源跟踪信息
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryZygz(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.queryZygz", param, page);
	}
	
	/**
	 * 查询资源跟踪信息
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryZygzMx(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.queryZygzMx", param, page);
	}

	/**
	 * 查询资源跟踪零件信息
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryJizxlb(PageableSupport page, Map<String, String> param) {
		if(param.get("usercenter")==null||param.get("usercenter").equals("")||param.get("usercenter").equals("null")){
			param.put("usercenter", "");
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.queryJizxlb", param, page);
	}

	/**
	 * 查询资源跟踪零件信息
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryWeifylj(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.queryWeifylj", param, page);
	}

	/**
	 * 查询资源跟踪零件信息
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryZygzjzx(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.queryWeizxyhllb", param, page);
	}

	/**
	 * 查询未装箱要货令列表
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryWeizxyhllb(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.queryWeizxyhllb", param, page);
	}
	
	/**
	 * 查询要货令信息
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryYhl(PageableSupport page, Map<String, String> param) {
		//String duandsj = param.get("duandsj");
		//Date duandsjD = DateUtil.
		String zuiwsj = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.queryYhlNext", param);
		if(null!=zuiwsj&&!"".equals(zuiwsj)){
			String newzuiwsj = DateUtil.dateAddMinutes(zuiwsj, 24*60);
			param.put("nextsj", newzuiwsj);
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.queryYhl", param, page);
	}

	/**
	 * 更新交付事件
	 * 
	 * @param param
	 *            参数
	 * @return 更新结果
	 */
	public int updateJiaofsj(Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.updateJiaofsj", param);
	}

	/**
	 * @param map
	 * @return
	 */
	public String queryTcSheetId(Map map) {
		String kdysSheetIdS = "";
		Object kdysSheetId = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("zygzbj.queryTcSheetId", map);
		if(kdysSheetId!=null&&!"".equals(kdysSheetId)){
			kdysSheetIdS = (String)kdysSheetId;
		}
		return kdysSheetIdS;
	}
}
