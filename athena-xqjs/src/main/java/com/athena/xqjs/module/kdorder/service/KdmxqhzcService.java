package com.athena.xqjs.module.kdorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.kdorder.CopyKdmxqhzc;
import com.athena.xqjs.entity.kdorder.Kdmxqhzc;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:KD毛需求汇总参考系类
 * </p>
 * <p>
 * Description:KD毛需求汇总参考系类
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
 * @date 2012-1-18
 */

@SuppressWarnings("rawtypes")
@Component
public class KdmxqhzcService extends BaseService {

	/**
	 * 查询全部，返回List
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date: 2012-1-18
	 */
	public List<Kdmxqhzc> queryListKdmxqhzc() {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryKdmxqhzc");
	}

	/**
	 * 无条件删除全部
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-1-31
	 */
	@Transactional
	public boolean doAllDelete() {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.deleteAllKdmxqhzc");
		return count > 0;
	}

	/**
	 * 根据外部物流模式来筛选pp毛需求
	 * 
	 * @author 陈骏
	 * 
	 * @date 2011-12-1 参数为外部供货模式
	 * @return List
	 */
	public List<Kdmxqhzc> selectDingd(String waibghms) throws ServiceException {
		Map map = new HashMap();
		map.put("waibghms", waibghms);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryRealKdmxqhzc", map);
	}

	/**
	 * 查询全部，返回List
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date: 2012-1-18
	 */
	public List<Kdmxqhzc> queryListKdmxqhzcForConvert(Map map) {
		CommonFun.mapPrint(map, "kd件毛需求-参考系汇总queryListKdmxqhzcForConvert方法参数map");
		CommonFun.logger.debug("kd件毛需求-参考系汇总queryListKdmxqhzcForConvert方法的sql语句为：kdorder.queryAllMxqhzcByDinghck");
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryAllMxqhzcByDinghck", map);
	}

	/**
	 * 查询全部，返回ListCopy
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date: 2012-1-18
	 */
	public List<CopyKdmxqhzc> queryListCopyKdmxqhzc() {
		CommonFun.logger.debug("查询全部，返回ListCopy，queryListCopyKdmxqhzc方法的sql语句为：kdorder.queryKdmxqhzc");
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryKdmxqhzc");
	}

	/**
	 * 统计份额
	 * **/
	public CopyKdmxqhzc countFene(Map<String, String> map) {
		return (CopyKdmxqhzc) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kdorder.CountGongysFene", map);
	}

	// public List<Kdmxqhzc> queryAllMxqhzc(Map map) {
	// return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryAllMxqhzc", map);
	// }

	/**
	 * 删除操作，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-1-30 参数说明：CopyKdmxqhzc bean 订单明细实体deleteKdmxqhzc
	 */
	public String doDeleteKdmxqhzc(CopyKdmxqhzc bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.deleteKdmxqhzc", bean);
		return bean.getId();
	}

	/**
	 * 循环插入操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date: 2012-1-18
	 * @参数：list集合，实体Kdmxqhzc
	 */
	@Transactional
	public void listInsertMxqhzc(List all, Object bean) {
		for (Object obj : all) {
			this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.insertKdmxqhzc", obj);
		}
	}

	/**
	 * 插入操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date: 2012-1-18
	 * @参数：实体Kdmxqhzc
	 */
	public void listInsertMxqhzcByObject(Kdmxqhzc bean) {
		CommonFun.objPrint(bean, "插入操作listInsertMxqhzcByObject方法参数bean");
		CommonFun.logger.debug("插入操作listInsertMxqhzcByObject方法的sql语句为：kdorder.insertKdmxqhzc");
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.insertKdmxqhzc", bean);
	}
	public Integer checkFene(Map<String,String> map){
		return (Integer) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kdorder.queryFene", map);
	}
	
	
	
	public List<Kdmxqhzc> checkFene(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckFene");
	}
	
	public List<Kdmxqhzc> checkZhizlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckZhizlx");
	}
	
	public List<Kdmxqhzc> checkGongysdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckGongysdm");
	}
	
	public List<Kdmxqhzc> checkDinghcj(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckDinghcj");
	}
	
	public List<Kdmxqhzc> checkBeihzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckBeihzq");
	}
	
	public List<Kdmxqhzc> checkFayzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckFayzq");
	}
	
	public List<Kdmxqhzc> checkZiyhqrq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckZiyhqrq");
	}
	
	public List<Kdmxqhzc> checkLujdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckLujdm");
	}
	
	public List<Kdmxqhzc> checkCangkdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckCangkdm");
	}
	
	public List<Kdmxqhzc> checkUabzlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckUabzlx");
	}
	
	public List<Kdmxqhzc> checkUabzuclx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckUabzuclx");
	}
	
	public List<Kdmxqhzc> checkUabzucsl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckUabzucsl");
	}
	
	public List<Kdmxqhzc> checkUabzucrl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckUabzucrl");
	}
	
	public List<Kdmxqhzc> checkWaibghms(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckWaibghms");
	}
	
	public List<Kdmxqhzc> checkJihyz(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckJihyz");
	}
	
	public void clearErro(){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.pDeleteOther");
	}
	
	////////////wuyichao  2014-05-27///////////////
	public void clearData(String gongysdm){
		Map<String, String> map = new HashMap<String, String>();
		map.put("gongysdm", gongysdm);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.pDeleteData",map);
	}
	////////////wuyichao  2014-05-27///////////////
	
	public List<Kdmxqhzc> checkWullj() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kdorder.pcheckWullj");
	}
	
	/**
	 * 查询全部，返回List
	 * 
	 * @author 袁修瑞
	 * @version v1.0
	 * @date: 2012-12-11
	 */
	public List<Kdmxqhzc> queryNewListKdmxqhzcForConvert(Map map) {
		CommonFun.mapPrint(map, "kd件毛需求-参考系汇总queryNewListKdmxqhzcForConvert方法参数map");
		CommonFun.logger.debug("kd件毛需求-参考系汇总queryNewListKdmxqhzcForConvert方法的sql语句为：kdorder.queryAllMxqhzcByDinghck");
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdorder.queryNewAllMxqhzcByDinghck", map);
	}

	
}