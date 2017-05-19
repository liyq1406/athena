package com.athena.xqjs.module.hlorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.hlorder.MaoxqhzjTmp;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:毛需求汇总J模式类
 * </p>
 * <p>
 * Description:毛需求汇总J模式类
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
public class MaoxqhzjTmpService extends BaseService {

	

	/**
	 * 无条件删除全部信息
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 */
	@Transactional
	public boolean doAllDelete(String usercenter) {
		Map<String,String>map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.deleteAllMaoxqhzjTmp",map);
		return true;
	}

	

	public List select() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryMaoxqhzjTmp");
	}

	/**
	 * 查询全部信息，返回list集合
	 * 
	 * @author wyg
	 * @version v1.0
	 * @date 2011-12-13
	 * @param:String riq,String usercenter,String banc,String zhizlx
	 */
	@SuppressWarnings("unchecked")
	public List<MaoxqhzjTmp> queryMaoxqhzj(Map map) {
		CommonFun.logger.debug("Maoxqhzj方法行列转换查询sql语句为：hlorder.queryAllMaoxqhzjTmp");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryAllMaoxqhzjTmp", map);
	}
	///wuyichao  2014-05-17
	public List<MaoxqhzjTmp> queryMaoxqhzjByCenter(Map map) {
		CommonFun.logger.debug("Maoxqhzj方法行列转换查询sql语句为：hlorder.queryAllMaoxqhzjTmpByCenter");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryAllMaoxqhzjTmpByCenter", map);
	}
	///wuyichao  2014-05-17
	@Transactional
	public void deleteMaoxqhzjById(String id)
	{
		if(StringUtils.isNotBlank(id))
		{
			Map<String, String> param = new HashMap<String, String>();
			param.put("id", id);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.deleteMaoxqhzjTmpById",param);
		}
	}
	
	//xss 20161010 v4_015
	//没有需求版次时的需求
	@SuppressWarnings("unchecked")
	public List<MaoxqhzjTmp> queryMaoxqhzj2(Map map) {
		CommonFun.logger.debug("Maoxqhzj方法行列转换查询sql语句为：hlorder.queryAllMaoxqhzjTmp");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryAllMaoxqhzjTmp2", map);
	}

}