package com.athena.xqjs.module.hlorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.hlorder.MjMaoxqhzjcTmp;
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
public class MjMaoxqhzjcTmpService extends BaseService {

	/**
	 * 插入操作，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	public String doInsert(MjMaoxqhzjcTmp bean) {
		bean.setId(getUUID());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.mjInsertMaoxqhzjcTmp", bean);
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
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.mjDeleteAllMaoxqhzjcTmp",map);
		return true;
	}


	public List select() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.mjQueryMaoxqhzjcTmp");
	}

	/**
	 * 毛需求汇总到仓库，并关联参考系表
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-13 参数说明：资源获取日期
	 */
	public List proMaoxqhzjc(String ziyhqrq) {
		Map<String, String> map = new HashMap();
		map.put("ziyhqrq", ziyhqrq);
	    return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.mjSelectIntoMaoxqhzjcTmp", map);
	}

	/**
	 * 根据外部物流模式来筛选pp毛需求
	 * 
	 * @author 陈骏
	 * 
	 * @date 2011-12-1 参数为外部供货模式
	 * @return List
	 */
	public List selectDingd(String mos2) throws ServiceException {
		Map map = new HashMap();
		map.put("mos2", mos2);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.mjQueryWaiBuWuLiuLuJingByjc", map);
	}

	/**
	 * 根据外部物流模式来筛选非pp毛需求
	 * 
	 * @author 陈骏
	 * 
	 * @date 2011-12-1 参数为外部供货模式
	 * @return List
	 */
	public List selectYugao(String waibghms) throws ServiceException {
		Map map = new HashMap();
		map.put("waibghms", waibghms);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("hlorder.mjQueryYuGaoByjc", map);
	}
	
	public List<MjMaoxqhzjcTmp> checkFene(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckFene");
	}
	
	public List<MjMaoxqhzjcTmp> checkZhizlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjCheckZhizlx");
	}
	
	public List<MjMaoxqhzjcTmp> checkGcbh(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckGcbh");
	}
	
	public List<MjMaoxqhzjcTmp> checkDinghcj(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckDinghcj");
	}
	
	public List<MjMaoxqhzjcTmp> checkBeihzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjCheckBeihzq");
	}
	
	public List<MjMaoxqhzjcTmp> checkFayzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckFayzq");
	}
	
	public List<MjMaoxqhzjcTmp> checkZiyhqrq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckZiyhqrq");
	}
	
	public List<MjMaoxqhzjcTmp> checkLujdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckLujdm");
	}
	
	public List<MjMaoxqhzjcTmp> checkCangkdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckCangkdm");
	}
	
	
	
	public List<MjMaoxqhzjcTmp> checkUabzlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckUabzlx");
	}
	
	public List<MjMaoxqhzjcTmp> checkUabzuclx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckUabzuclx");
	}
	
	public List<MjMaoxqhzjcTmp> checkUabzucsl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckUabzucsl");
	}
	
	public List<MjMaoxqhzjcTmp> checkUabzucrl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckUabzucrl");
	}
	
	public List<MjMaoxqhzjcTmp> checkMos2(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckMos2");
	}
	
	public List<MjMaoxqhzjcTmp> checkJihyz(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckJihyz");
	}
	
	public List<MjMaoxqhzjcTmp> checkBEIHSJ2(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckBEIHSJ2");
	}
	
	public List<MjMaoxqhzjcTmp> checkCangkshsj2(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckCANGKSHSJ2");
	}
	
	public List<MjMaoxqhzjcTmp> checkCangkfhsj2(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckCANGKFHSJ2");
	}
	
	public List<MjMaoxqhzjcTmp> checkUsbaozlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckUSBAOZLX");
	}
	
	public List<MjMaoxqhzjcTmp> checkUsbaozrl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.mjJcheckUSBAOZRL");
	}
	
	public void clearErro(){
		//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.mjJDeleteFene");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.mjJDeleteOther");
	}
}