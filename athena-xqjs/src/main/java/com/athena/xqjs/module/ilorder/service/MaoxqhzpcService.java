/**
 * 代码声明
 */
package com.athena.xqjs.module.ilorder.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.ilorder.Maoxqhzpc;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:毛需求汇总P关联参考系模式类
 * </p>
 * <p>
 * Description:毛需求汇总P关联参考系模式类
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
public class MaoxqhzpcService extends BaseService {
	@Inject
	private YicbjService yicbjService;

	/**
	 * 插入操作，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	@Transactional
	public boolean doInsert(Maoxqhzpc bean) {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertMaoxqhzpc", bean);
		return count > 0;
	}

	/**
	 * 删除全部，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	@Transactional
	public String doDelete(Maoxqhzpc bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteMaoxqhzpc", bean);
		return bean.getId();
	}

	/**
	 * 全部删除
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13
	 * 
	 */
	@Transactional
	public boolean doAllDelete() {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteAllMaoxqhzpc");
		return count > 0;
	}

	/**
	 * 更新操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：实体
	 */
	@Transactional
	public boolean doUpdate(Maoxqhzpc bean) {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateMaoxqhzpc", bean);
		return count > 0;
	}

	/**
	 * 查询全部
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-13 参数说明：分页
	 */
	public List<Maoxqhzpc> selectAll() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryMaoxqhzpc");
	}

	/**
	 * 查询毛需求汇总消耗点表关联参考系数据 * @author 陈骏
	 * 
	 * @version v1.0
	 */
	public List<Maoxqhzpc> selectInto(String ziyhqrq) {
		Map map = new HashMap();
		map.put("ziyhqrq", ziyhqrq);
		CommonFun.logger.debug("PP模式毛需求-参考系汇总参数ziyhqrq："+ziyhqrq);
		CommonFun.logger.debug("PP模式毛需求-参考系汇总sql语句为：ilorder.queryIntoMaoxqhzpc");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryIntoMaoxqhzpc", map);
	}

	/**
	 * 查询毛需求_汇总_P_参考系表
	 * 
	 * @author 陈骏
	 * 
	 * @date 2011-12-1
	 * 
	 * @return List
	 */
	public List<Maoxqhzpc> select() throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryMaoxqhzpc");
	}

	/**
	 * 物流路径，供应商份额以及参考系参数校验
	 * 
	 * @author 陈骏
	 * 
	 * @date 2011-12-1 参数为订单类型
	 * @return List
	 */
	public Map<String,Object> checkAll(String dingdlx) throws ServiceException {
		long start = System.currentTimeMillis();
		Map<String,Object> map = new HashMap<String,Object>();
		Integer result = 0;
		map.put("dingdlx", dingdlx);
		map.put("result", result);
		 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.checkAll", map);
		 long end = System.currentTimeMillis();
			CommonFun.logger.info("----------------------------IL计算,计算时间"+ (end - start)/1000);
		 return map;
	}

	/**
	 * 根据外部物流模式来筛选pp毛需求
	 * 
	 * @author 陈骏
	 * 
	 * @date 2011-12-1 参数为外部供货模式
	 * @return List
	 */
	public List<Maoxqhzpc> selectDingd(String waibghms) throws ServiceException {
		Map map = new HashMap();
		map.put("waibghms", waibghms);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryWaiBuWuLiuLuJingBypc", map);
	}

	/**
	 * 根据外部物流模式来筛选非pp毛需求
	 * 
	 * @author 陈骏
	 * 
	 * @date 2011-12-1 参数为外部供货模式
	 * @return List
	 */
	public List<Maoxqhzpc> selectYugao(String waibghms) throws ServiceException {
		Map map = new HashMap();
		map.put("waibghms", waibghms);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryYuGaoBypc", map);
	}

	/**
	 * 查询库存数量
	 * */
	public BigDecimal selectKuc(String usercenter, String lingjbh, String cangkdm, String ziyhqrq,String jihyz,LoginUser loginuser) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lingjbh", lingjbh);
		map.put("cangkdm", cangkdm);
		map.put("ziyhqrq", ziyhqrq);
		map.put("usercenter", usercenter);
		List<Maoxqhzpc> all = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryKuc", map);
		BigDecimal bd = BigDecimal.ZERO;
		if (all.size() > 1) {
			String paramStr[] = new String []{usercenter, ziyhqrq,lingjbh,cangkdm};
			this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str8, jihyz, paramStr,usercenter , lingjbh, loginuser, Const.JISMK_IL_CD);
			
			//this.yicbjService.saveYicInfo(Const.JISMK_IL_CD, lingjbh, "200", "XQJS_ZIYKZB存在重复数据");
		} else {
			Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryKuc", map);
			if(null==obj){
				bd = BigDecimal.ZERO;
			}else{
				bd = new BigDecimal(obj.toString());
			}
		}
		return bd;
	}

	public BigDecimal selectDingKuc(String usercenter, String lingjbh, String cangkdm, String ziyhqrq,String jihyz,LoginUser loginuser) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("lingjbh", lingjbh);
		map.put("cangkdm", cangkdm);
		map.put("ziyhqrq", ziyhqrq);
		map.put("usercenter", usercenter);
		List<Maoxqhzpc> all = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDinghKuc", map);
		BigDecimal bd = BigDecimal.ZERO;
		if (all.size() > 1) {
			String paramStr[] = new String []{usercenter, ziyhqrq,lingjbh,cangkdm};
			this.yicbjService.insertError(Const.YICHANG_LX3, Const.YICHANG_LX3_str8, jihyz, paramStr,usercenter , lingjbh, loginuser, Const.JISMK_IL_CD);
			
			//this.yicbjService.saveYicInfo(Const.JISMK_IL_CD, lingjbh, "200", "XQJS_ZIYKZB存在重复数据");
		
		} else {
			Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDinghKuc", map);
			if (null == obj){
				bd = BigDecimal.ZERO;
			}else{
				bd = new BigDecimal(obj.toString());
			}
			
		}
		return bd;
	}
	
	public List<Maoxqhzpc> checkFene(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckFene");
	}
	
	public List<Maoxqhzpc> checkZhizlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckZhizlx");
	}
	
	public List<Maoxqhzpc> checkGongysdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckGongysdm");
	}
	
	public List<Maoxqhzpc> checkDinghcj(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckDinghcj");
	}
	
	public List<Maoxqhzpc> checkBeihzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckBeihzq");
	}
	
	public List<Maoxqhzpc> checkFayzq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckFayzq");
	}
	
	public List<Maoxqhzpc> checkZiyhqrq(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckZiyhqrq");
	}
	
	public List<Maoxqhzpc> checkLujdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckLujdm");
	}
	
	public List<Maoxqhzpc> checkCangkdm(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckCangkdm");
	}
	
	public List<Maoxqhzpc> checkUabzlx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckUabzlx");
	}
	
	public List<Maoxqhzpc> checkUabzuclx(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckUabzuclx");
	}
	
	public List<Maoxqhzpc> checkUabzucsl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckUabzucsl");
	}
	
	public List<Maoxqhzpc> checkUabzucrl(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckUabzucrl");
	}
	
	public List<Maoxqhzpc> checkWaibghms(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckWaibghms");
	}
	
	public List<Maoxqhzpc> checkJihyz(){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.pcheckJihyz");
	}
	
	public void clearErro(){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.pDeleteFene");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.pDeleteOther");
	}
}