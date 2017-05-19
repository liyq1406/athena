package com.athena.xqjs.module.ilorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.ilorder.Maoxqhzjc;
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
public class MaoxqhzjcService extends BaseService {

	/**
	 * 插入操作，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	public String doInsert(Maoxqhzjc bean) {
		bean.setId(getUUID());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzjc", bean);
		return bean.getId();
	}

	/**
	 * 删除，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	// public String doDelete(Maoxqhzjc bean) {
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteMaoxqhzjc",
	// bean);
	// return bean.getId();
	// }
	//
	/**
	 * 删除，返回boolean
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	public boolean doAllDelete() {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteAllMaoxqhzjc");
		return true;
	}

	/**
	 * 更新操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	// public String doUpdate(Maoxqhzjc bean) {
	// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateMaoxqhzjc",
	// bean);
	// return bean.getId();
	// }

	/**
	 * 查询全部分页
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	// public Map<String, Object> select(Pageable page) {
	// return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryMaoxqhzjc", page);
	// }
	public List select() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryMaoxqhzjc");
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
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.selectIntoMaoxqhzjc", map);
	}

	/**
	 * 根据外部物流模式来筛选pp毛需求
	 * 
	 * @author 陈骏
	 * 
	 * @date 2011-12-1 参数为外部供货模式
	 * @return List
	 */
	public List selectDingd(String waibghms) throws ServiceException {
		Map map = new HashMap();
		map.put("waibghms", waibghms);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryWaiBuWuLiuLuJingByjc", map);
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
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryYuGaoByjc", map);
	}
	
	public List<Maoxqhzjc> checkFene(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckFene");
	}
	
	public List<Maoxqhzjc> checkZhizlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckZhizlx");
	}
	
	public List<Maoxqhzjc> checkGongysdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckGongysdm");
	}
	
	public List<Maoxqhzjc> checkDinghcj(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckDinghcj");
	}
	
	public List<Maoxqhzjc> checkBeihzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckBeihzq");
	}
	
	public List<Maoxqhzjc> checkFayzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckFayzq");
	}
	
	public List<Maoxqhzjc> checkZiyhqrq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckZiyhqrq");
	}
	
	public List<Maoxqhzjc> checkLujdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckLujdm");
	}
	
	public List<Maoxqhzjc> checkCangkdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckCangkdm");
	}
	
	public List<Maoxqhzjc> checkUabzlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckUabzlx");
	}
	
	public List<Maoxqhzjc> checkUabzuclx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckUabzuclx");
	}
	
	public List<Maoxqhzjc> checkUabzucsl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckUabzucsl");
	}
	
	public List<Maoxqhzjc> checkUabzucrl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckUabzucrl");
	}
	
	public List<Maoxqhzjc> checkWaibghms(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckWaibghms");
	}
	
	public List<Maoxqhzjc> checkJihyz(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.jcheckJihyz");
	}
	
	public void clearErro(){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.jDeleteFene");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.jDeleteOther");
	}
}