package com.athena.xqjs.module.ilorder.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.ilorder.Maoxqhzsc;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:毛需求汇总S关联参考系模式类
 * </p>
 * <p>
 * Description:毛需求汇总S关联参考系模式类
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
public class MaoxqhzscService extends BaseService {

	/**
	 * 插入操作，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	public String doInsert(Maoxqhzsc bean) {
		bean.setId(getUUID());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzsc", bean);
		return bean.getId();
	}

	/**
	 * 删除操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	public String doDelete(Maoxqhzsc bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteMaoxqhzsc", bean);
		return bean.getId();
	}

	/**
	 * 删除全部
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 * 
	 */
	public boolean doAllDelete() {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteAllMaoxqhzsc");
		return true;
	}

	/**
	 * 根据ID更新对象
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	public String doUpdate(Maoxqhzsc bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateMaoxqhzsc", bean);
		return bean.getId();
	}

	/**
	 * 查询全部分页
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：分页
	 */
	public Map<String, Object> select(Pageable page) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryMaoxqhzsc", page);
	}

	public List select() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryMaoxqhzsc");
	}

	/**
	 * 毛需求汇总到仓库，并关联参考系表
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-13 参数说明：资源获取日期
	 */
	public List selectInto(String ziyhqrq) {
		Map map = new HashMap();
		map.put("ziyhqrq", ziyhqrq);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryIntoMaoxqhzsc", map);
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
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryWaiBuWuLiuLuJingBysc", map);
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
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryYuGaoBysc", map);
	}

	public BigDecimal selectKuc(String lingjbh, String cangkdm, String ziyhqrq) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lingjbh", lingjbh);
		map.put("cangkdm", cangkdm);
		map.put("ziyhqrq", ziyhqrq);
		return new BigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryKucS", map).toString());
		
	}

	public BigDecimal selectDingKuc(String lingjbh, String cangkdm, String ziyhqrq) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lingjbh", lingjbh);
		map.put("cangkdm", cangkdm);
		map.put("ziyhqrq", ziyhqrq);
		return new BigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDinghKucS", map).toString());
		
	}
	
	
	
	public List<Maoxqhzsc> checkFene(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckFene");
	}
	
	public List<Maoxqhzsc> checkZhizlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckZhizlx");
	}
	
	public List<Maoxqhzsc> checkGongysdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckGongysdm");
	}
	
	public List<Maoxqhzsc> checkDinghcj(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckDinghcj");
	}
	
	public List<Maoxqhzsc> checkBeihzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckBeihzq");
	}
	
	public List<Maoxqhzsc> checkFayzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckFayzq");
	}
	
	public List<Maoxqhzsc> checkZiyhqrq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckZiyhqrq");
	}
	
	public List<Maoxqhzsc> checkLujdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckLujdm");
	}
	
	public List<Maoxqhzsc> checkCangkdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckCangkdm");
	}
	
	public List<Maoxqhzsc> checkUabzlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckUabzlx");
	}
	
	public List<Maoxqhzsc> checkUabzuclx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckUabzuclx");
	}
	
	public List<Maoxqhzsc> checkUabzucsl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckUabzucsl");
	}
	
	public List<Maoxqhzsc> checkUabzucrl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckUabzucrl");
	}
	
	public List<Maoxqhzsc> checkWaibghms(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckWaibghms");
	}
	
	public List<Maoxqhzsc> checkJihyz(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.scheckJihyz");
	}
	
	public void clearErro(){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.sDeleteFene");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.sDeleteOther");
	}
}