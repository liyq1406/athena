package com.athena.xqjs.module.hlorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.hlorder.MaoxqhzjcTmp;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:毛需求汇总关联参考系J模式类
 * </p>
 * <p>
 * Description:毛需求汇总关联参考系J模式类
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
public class MaoxqhzjcTmpService extends BaseService {

	/**
	 * 插入操作，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	public String doInsert(MaoxqhzjcTmp bean) {
		bean.setId(getUUID());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.insertMaoxqhzjcTmp", bean);
		return bean.getId();
	}

	
	/**
	 * 删除，返回boolean
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	public boolean doAllDelete(String usercenter) {
		Map<String,String>map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.deleteAllMaoxqhzjcTmp",map);
		return true;
	}


	public List select() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryMaoxqhzjcTmp");
	}

	/**
	 * 毛需求汇总到仓库，并关联参考系表
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-13 参数说明：资源获取日期
	 */
	public List proMaoxqhzjc(String ziyhqrq , LoginUser loginuser) {
		Map<String, String> map = new HashMap();
		map.put("ziyhqrq", ziyhqrq); 
		
		//用户中心 - xss-20160422
		map.put("usercenter",loginuser.getUsercenter());
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.selectIntoMaoxqhzjcTmp", map);
	}

	/**
	 * 根据外部物流模式来筛选pp毛需求
	 * 
	 * @author 陈骏
	 * 
	 * @date 2011-12-1 参数为外部供货模式
	 * @return List
	 */
	public List selectDingd(String waibghms , LoginUser loginuser) throws ServiceException {
		Map map = new HashMap();
		map.put("waibghms", waibghms);
		
		//用户中心 - xss-20160422
		map.put("usercenter",loginuser.getUsercenter());
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.queryWaiBuWuLiuLuJingByjc", map);
	}
	
	public List<MaoxqhzjcTmp> checkFene(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckFene");
	}
	
	public List<MaoxqhzjcTmp> checkZhizlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckZhizlx");
	}
	
	public List<MaoxqhzjcTmp> checkGongysdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckGongysdm");
	}
	
	public List<MaoxqhzjcTmp> checkDinghcj(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckDinghcj");
	}
	
	public List<MaoxqhzjcTmp> checkBeihzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckBeihzq");
	}
	
	public List<MaoxqhzjcTmp> checkFayzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckFayzq");
	}
	
	public List<MaoxqhzjcTmp> checkZiyhqrq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckZiyhqrq");
	}
	
	public List<MaoxqhzjcTmp> checkLujdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckLujdm");
	}
	
	public List<MaoxqhzjcTmp> checkCangkdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckCangkdm");
	}
	
	public List<MaoxqhzjcTmp> checkUabzlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckUabzlx");
	}
	
	public List<MaoxqhzjcTmp> checkUabzuclx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckUabzuclx");
	}
	
	public List<MaoxqhzjcTmp> checkUabzucsl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckUabzucsl");
	}
	
	public List<MaoxqhzjcTmp> checkUabzucrl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckUabzucrl");
	}
	
	public List<MaoxqhzjcTmp> checkWaibghms(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckWaibghms");
	}
	
	public List<MaoxqhzjcTmp> checkJihyz(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.jcheckJihyz");
	}
	
	public void clearErro(){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.jDeleteFene");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.jDeleteOther");
	}
}