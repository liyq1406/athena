package com.athena.xqjs.module.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiaohblzjb;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.container.annotation.Component;

/**
 * <p>
 * Title:物流路径service类
 * </p>
 * <p>
 * Description::物流路径service类
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
 * @date 2012-1-4
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class WulljService extends BaseService {

	/**
	 * 查询CalendarCenter返回实体
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-1-4 参数说明：String usercenter, 用户中心，String lingjbh 零件编号
	 */
	public List<Wullj> queryWulljForKd(Map map) {
		return (List<Wullj>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryWulljForupdate", map);
	}

	/**
	 * 查询CalendarCenter返回实体
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-1-4 参数说明：String usercenter, 用户中心，String lingjbh 零件编号
	 */
	public List<Wullj> queryWullj(Map map) {
		CommonFun.mapPrint(map, "查询Wullj返回list的queryWullj方法参数map");
		CommonFun.logger.debug("查询Wullj返回list的queryWullj方法的sql语句为：common.queryWulljForDinghck");
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryWulljForDinghck", map);
	}

	/**
	 * 查询物流路径实体
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-3-22 参数说明：String usercenter, 用户中心，String lingjbh
	 *       零件编号，供应商编号，循环编号
	 */
	public Wullj queryWulljObject(Map<String, String> map) {
		Wullj bean = new Wullj();
		bean = (Wullj) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryWullj", map);
		return bean;
	}

	public List<Wullj> queryWulljList(Map<String, String> map) {
		return (List<Wullj>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryWullj", map);
	}

	/**
	 * 查询临时按需M1/MD
	 * 
	 * @param map
	 * @return
	 */
	public Wullj queryWulljObjectLsaxck(Map<String, String> map) {
		return (Wullj) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryWulljForlsck", map);
	}

	/**
	 * 查询临时按需C1
	 * 
	 * @param map
	 * @return
	 */
	public Wullj queryWulljObjectLsaxc1(Map<String, String> map) {
		return (Wullj) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryWulljForlsc1", map);
	}

	/**
	 * @方法：查询关联得到消耗比例信息 李明 2012-5-17
	 * **/
	public List<Xiaohblzjb> queryXhbl() {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryXhbl");
	}

	/**
	 * @方法：插入到消耗比例中间表 李明 2012-5-17
	 * **/
	public boolean saveXiaohblzjb(Xiaohblzjb bean) {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("common.insertXhbl", bean);
		return count > 0;
	}

	/**
	 * @方法： 清理中间表 李明 2012-5-17
	 * **/
	public boolean deleteAllZjb() {
		int count = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("common.clearZjb");
		return count > 0;
	}

	/**
	 * @方法：检验消耗比例 李明 2012-5-17
	 * **/
	public void checkXhbl(String dingdlx) {
		// 清理中间表
		this.deleteAllZjb();
		// 物流路径关联零件消耗点
		List<Xiaohblzjb> allXhbl = null;
		if(dingdlx.equals(Const.PP))
		{
			allXhbl = this.queryXhbl();
		}
		else if(dingdlx.equals(Const.PS))
		{
			allXhbl = this.queryXhblPS();
		}
		else if(dingdlx.equals(Const.PJ))
		{
			allXhbl = this.queryXhblPJ();
		}
			
		// 判断结果集不为空
		if (!allXhbl.isEmpty()) {
			// 迭代循环
			for (Xiaohblzjb bean : allXhbl) {
				// 将消耗比例不为1 的数据插入到中间表中
				if(null != bean.getXiaohbl()){
				if (1 != bean.getXiaohbl().intValue()) {
					this.saveXiaohblzjb(bean);
				}
				}else{
					this.saveXiaohblzjb(bean);
				}
			}
		}
	}
	private List<Xiaohblzjb> queryXhblPJ() {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryXhblPJ");
	}

	private List<Xiaohblzjb> queryXhblPS() {
		return this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryXhblPS");
	}

	public String queryWlgyy(String usercenter,String cangkbh){
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("cangkbh", cangkbh);
		Object obj= this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("common.queryWlgyy",map);
		if(obj!=null){
			return obj.toString();
		}else{
			return null;
		}
	}
	public String queryWulgyy(Map<String, String> map){
		Object obj= this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("common.queryWulgyy",map);
		if(obj!=null){
			return obj.toString();
		}else{
			return null;
		}
	}
}