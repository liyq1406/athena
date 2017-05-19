package com.athena.xqjs.module.anxorder.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.anxorder.AnxMaoxq;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.common.Tidj;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.common.Xiehztxhsj;
import com.athena.xqjs.entity.common.Xitcsdy;
import com.athena.xqjs.entity.common.Yunssk;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

/**
 * Title:按需毛需求service
 * 
 * @author 李明
 * @version v1.0
 * @date 2012-03-19
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class AnxMaoxqService extends BaseService {

	/**
	 * 关联物流路径， 零件消耗点 查询全部信息， 返回list集合
	 * */
	public List<Anxjscszjb> queryForAnxjscszjb() {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.anxMaoxqhz");
	}

	/**
	 * 查询全部信息， 返回list集合 参数：当前系统时间，用户中心，零件编号，消耗点
	 * */
	// public List<AnxMaoxq> queryAllAnxMaoxq(Map<String,String> map){
	// return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryAllObject",map) ;
	// }

	/**
	 * 查询毛需求信息 参数：用户中心，零件编号，消耗时间段（拼字符串）
	 * */
	// public AnxMaoxq queryCountAnxMaoxq(Map<String,String> map){
	// AnxMaoxq bean = new AnxMaoxq() ;
	// bean = (AnxMaoxq)
	// this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryAllObjectCount",map) ;
	// map.clear() ;
	// return bean ;
	// }
	/**
	 * 查询毛需求信息 参数：用户中心，流水号 返回实体AnxMaoxq
	 * */
	public AnxMaoxq queryObjectMaoxq(Map<String, String> map) {
		AnxMaoxq bean = new AnxMaoxq();
		// 单条毛需求
		bean = (AnxMaoxq) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryObjectMaoxq", map);
		map.clear();
		return bean;
	}

	/**
	 * 查询订单信息 参数：用户中心，零件编号，订单号，消耗点
	 * */
	public Dingdmx queryCountAnxDingdmx(Map<String, String> map) {
		Dingdmx bean = new Dingdmx();
		// 单条订单明细
		bean = (Dingdmx) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryDingdmxCount", map);
		return bean;
	}
	
	/**
	 * 查询订单信息 参数：用户中心，零件编号，订单号，消耗点
	 * */
	public Dingdmx queryCountAnxDingddh(Map<String, String> map) {
		Dingdmx bean = new Dingdmx();
		// 单条订单明细
		bean = (Dingdmx) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryDingdmxDh", map);
		return bean;
	}

	/*
	 * 关联查询出完整路径，返回list集合（按需计算---数据准备）
	 */
	// public List<Wullj> queryLujForAnx(){
	// return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryWulljForAnx") ;
	// }
	//
	/**
	 * 按需：查询资源快照实体 返回实体 参数：当前系统时间，用户中心，零件编号，仓库代码
	 * */
	public Ziykzb queryZykzObject(Map<String, String> map) {
		CommonFun.mapPrint(map, "查询资源快照实体queryZykzObject方法参数map");
		CommonFun.logger.debug("查询资源快照实体queryZykzObject方法的sql语句为：anx.queryZiykzbObject");
		Ziykzb ziykzb = new Ziykzb();
		Object obj = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryZiykzbObject", map);
		if(obj!= null){
			ziykzb = (Ziykzb)obj;
			return ziykzb;
		}else{
			return null;
		}

	}
	
	////////////wuyichao 2014-05-16////////////////
	public Ziykzb queryZykzObjects(Map<String, String> map) {
		CommonFun.mapPrint(map, "查询资源快照实体queryZykzObject方法参数map");
		CommonFun.logger.debug("查询资源快照实体queryZykzObject方法的sql语句为：anx.queryZiykzbObject");
		Ziykzb ziykzb = new Ziykzb();
		Object obj = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryZiykzbObjects", map);
		if(obj!= null){
			ziykzb = (Ziykzb)obj;
			return ziykzb;
		}else{
			return null;
		}

	}
	////////////wuyichao 2014-05-16////////////////
	
	public Ziykzb queryZiykz(Map<String, String> map) {
		CommonFun.mapPrint(map, "查询资源快照实体queryZykzObject方法参数map");
		CommonFun.logger.debug("查询资源快照实体queryZykzObject方法的sql语句为：anx.queryZiykzbObject");
		Ziykzb ziykzb = new Ziykzb();
		List objList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryZiykzbObject", map);
		if(objList.size()>0&&objList.size()<=1){
			ziykzb = (Ziykzb)objList.get(0);
			return ziykzb;
		}else{
			return ziykzb;
		}

	}

	/**
	 * 按需：查询系统参数，获取按需时间间隔 参数：用户中心,字段名称 返回实体
	 * */
	public Xitcsdy queryAnxShijjg(Map<String, String> map) {
		Xitcsdy bean = new Xitcsdy();
		bean = (Xitcsdy) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryShijjg", map);
		return bean;
	}
	
	/**
	 * 查询工作时间模板向前
	 * @param map 参数
	 * @return 时间
	 */
	public String queryGongzsjmbQ(Map<String,String> map){
		return CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryGongzsjmbQ",map));
	}
	
	/**
	 * @方法：按需：查询卸货站台，获取允许提前到货时间
	 * @参数：用户中心,卸货站台编号
	 * @返回：实体
	 * */
	public Xiehzt queryXiehztObject(Map<String, String> map) {
		Xiehzt bean = (Xiehzt) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryXiehztObject", map);
		if(bean == null){
			bean = new Xiehzt();
		}
		return bean;
	}

	/**
	 * @按需：查询运输时刻，获取最晚到货时间
	 * @参数：用户中心,卸货站台编号，供应商编号
	 * @返回：实体
	 * */
	public Yunssk queryYunsskObject(Map<String, String> map) {
		Yunssk bean = new Yunssk();
		bean = (Yunssk) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryYunsskObject", map);
		map.clear();
		return bean;
	}

	public List<Yunssk> queryYunssk(Map<String, String> map) {
		return (List<Yunssk>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryYunsskObject", map);
	}

	/**
	 * @按需：查询卸货站循环时间台，获取允许内部物流时间
	 * @参数：用户中心,卸货站台编号，仓库编号，模式
	 * @返回：实体
	 * */
	public Xiehztxhsj queryXiehztxhsjObject(Map<String, String> map) {
		Xiehztxhsj bean = (Xiehztxhsj) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryXiehztxhsjObject", map);
		if(bean == null){
			return new Xiehztxhsj();
		}
		return bean;
	}

	/**
	 * 按需：查询实体，获取备货周期和发货周期
	 * */
	// public Gongys queryAnxObject(String lingjbh,String usercenter){
	// Gongys bean = new Gongys() ;
	// Map<String,String> map = new HashMap<String,String>() ;
	// map.put("lingjbh", lingjbh) ;
	// map.put("usercenter", usercenter) ;
	// bean = (Gongys) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("CKX.queryAnxGongys",map) ;
	// map.clear() ;
	// return bean ;
	// }

	/**
	 * 按需：查询实体 参数：消耗时间，用户中心，零件编号 返回实体
	 * */
	public AnxMaoxq queryAnxMinTimeObject(Map<String, String> map) {
		AnxMaoxq bean = new AnxMaoxq();
		bean = (AnxMaoxq) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryAnxmaoxqObject", map);
		map.clear();
		return bean;
	}

	/**
	 * 按需：查询实体，按用中心，零件，消耗点，时间排序 返回list集合
	 * */
	public List<AnxMaoxq> queryAnxList() {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryAnxmaoxqObjectOrderby");
	}

	/**
	 * 清除中间表 无参数
	 * **/
	public boolean removeAnxMaoxqzjb() {
		int count = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.deleteAnxMaoxqzjb");
		return count > 0;
	}

	/**
	 * 汇总成时间段 参数：banci班次 返回list集合
	 * **/
	public List<AnxMaoxq> sumAllMaoxq(Map<String, String> map) {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryAnxMaoxqDistinct", map);
	}

	/**
	 * @：查找工作日
	 * @：参数：用户中心，日期，是否工作日标志
	 * @：返回实体
	 * **/
	public CalendarCenter queryWorkDay(Map<String, String> map) {
		CalendarCenter bean = new CalendarCenter();
		List<CalendarCenter> all = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryWorkDay", map);
		map.clear();
		if (all.size() > 0.1) {
			bean = all.get(0);
		}
		return bean;
	}

	/**
	 * 查询替代件的零件号码
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-2-3
	 * @参数说明：String usercenter, 零件编号
	 */
	public List<Tidj> queryTidjAll(String usercenter, String lingjbh) {
		Map<String, String> map = new HashMap<String, String>();
		// 设置条件
		map.put("usercenter", usercenter);
		map.put("lingjbh", lingjbh);
		CommonFun.mapPrint(map, "kd件查询替代件的零件号码queryTidjAll方法参数map");
		CommonFun.logger.debug("kd件查询替代件的零件号码queryTidjAll方法的sql语句为：common.queryTidj");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTidj", map);
	}

}