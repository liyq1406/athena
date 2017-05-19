package com.athena.xqjs.module.ppl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.ppl.Niandyg;
import com.athena.xqjs.entity.ppl.Niandygmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.MessageConst;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：NiandygmxService
 * <p>
 * 类描述：年度预告明细CRUD操作
 * </p>
 * 创建人：Xiahui
 * <p>
 * 创建时间：2011-12-12
 * </p>
 * 
 * @version
 * 
 */
@SuppressWarnings("rawtypes")
@Component
public class NiandygmxService extends BaseService {

	/**
	 * 查询年度预告明细
	 * 
	 * @param bean
	 * @author Xiahui
	 * @return map
	 * @date
	 */
	public Map<String, Object> select(Pageable page, Map<String, String> params) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ppl.queryNiandygmx", params, page);
	}
	
	
	public List<Object> selectAll(Map<String, String> params) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ppl.queryNiandygmx", params);
	}

	/**
	 * Down 不分页根据条件全查询
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> select(Map<String, String> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ppl.queryNiandygmx", params));
		return map;
	}

	/**
	 * 修改年度预告明细
	 * 
	 * @param bean
	 * @author xiahui
	 * @return pplbc
	 * @date
	 */
	public String doUpdate(Niandygmx bean) {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.updteNiandygmx", bean);
		if (count == 0) {
			throw new ServiceException(MessageConst.UPDATE_COUNT_0);
		} else {
			return bean.getPplbc();
		}
	}

	/**
	 * 查询两个年度预告明细之间的异同
	 * 
	 * @param map
	 * @author xiahui
	 * @return list
	 * @date
	 */
	public Map<String, Object> compare(Map bc, Niandyg bean) {
		if("exportXls".equals(bc.get("exportXls"))){ 
			return CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ppl.queryBijiaobc",bc));
		}else if("exportEfi".equals(bc.get("exportEfi"))){
			return CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ppl.queryBijiaobc",bc));
		}else{
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ppl.queryBijiaobc",bc,bean); 
		}
		
		
	}

	/**
	 * 
	 * 插入IL件年度预告明细的信息
	 * 
	 * @param bean
	 * @author Xiahui
	 * @return pplbc
	 * @date
	 */
	public int doInsertIL(Map bean) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.insertIL", bean);
	}

	/**
	 * 
	 * 插入KD件年度预告明细的信息
	 * 
	 * @param bean
	 * @author Xiahui
	 * @return pplbc
	 * @date
	 */
	public String doInsertKD(Map bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.insertKDNiandygmx", bean);
		return (String) bean.get("pplbc");
	}

	/**
	 * 
	 * 删除某一版次的ppl预告明细信息
	 * 
	 * @param bean
	 * @author Xiahui
	 * @return pplbc
	 * @date
	 */
	public int doDelete(Map map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.deleteNiandygmx", map);
	}

	/**
	 * 添加一条年度预告明细信息
	 * 
	 * @param bean
	 * @author Xiahui
	 * @return Boolean
	 */
	public boolean insertNiandygmx(Niandygmx bean) {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ppl.insertNiandygmx", bean);
		return count > 0;
	}

	/**
	 * 保存年度预告明细方法
	 * 
	 * @param insert
	 *            保存数据
	 * @param update
	 *            更新数据
	 * @param delete
	 *            删除数据(只做逻辑删除,不做物理删除)
	 * @param user
	 *            创建用户
	 * @param pplbc
	 *            PPL版次号
	 * @param p0xqzq
	 *            p0需求周期
	 * @return 保存结果
	 */
	@Transactional
	public boolean saveMx(List<Niandygmx> insert, List<Niandygmx> update, List<Niandygmx> delete, String user,
 String pplbc, String p0xqzq) {
		boolean bl = false;
		String sysTime = com.athena.xqjs.module.common.CommonFun.getJavaTime();// 获取系统时间
		for (int i = 0; i < insert.size(); i++) {// 新增数据
			Niandygmx mx = insert.get(i);
			mx.setPplbc(pplbc);
			mx.setZhuangt("0");// 状态为未发送
			mx.setCreate_time(sysTime);// 设置系统时间
			mx.setCreator(user);// 设置操作用户
			mx.setActive("1");// 设置数据有效性
			mx.setPplbc(pplbc);// 设置PPL版次
			mx.setP0xqzq(p0xqzq);// p0需求周期
			mx.setEditor(user);// 编辑人
			mx.setEdit_time(sysTime);// 编辑时间
			Lingj lj = new Lingj();
			lj.setLingjbh(mx.getLingjbh()); // 获取零件编号
			lj.setUsercenter(mx.getUsercenter()); // 获取用户中心
			lj = this.selectLingj(lj);
			mx.setJihydm(lj.getJihy());// 计划员代码
			mx.setDinghcj(lj.getDinghcj());// 订货车间
			Gongys gys = new Gongys();
			gys.setGcbh(mx.getGongysdm()); // 获取供应商名称
			gys.setUsercenter(mx.getUsercenter());// 获取用户中心
			gys = this.selectGongys(gys); // 根据供应商代码和用户中心获取供应商
			mx.setGongysmc(gys.getGongsmc());// 供应商名称
			insertNiandygmx(mx);
		}
		for (int i = 0; i < update.size(); i++) {// 编辑数据

			Niandygmx mx = update.get(i);
			mx.setEdittime(sysTime);// 新编辑时间
			mx.setNeweditor(user);
			doUpdate(mx);
		}
		for (int i = 0; i < delete.size(); i++) {// 删除数据
			Niandygmx mx = delete.get(i);
			mx.setActive("0");
			mx.setEdittime(sysTime);// 原编辑时间
			mx.setNeweditor(user);
			doUpdate(mx);
		}
		bl = true;
		return bl;
	}

	// 查询零件表获取零件名称与订货车间
	public Lingj selectLingj(Lingj lj) {
		return (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ppl.lingjinfo", lj);
	}

	// 查询供应商表获取供应商名称
	public Gongys selectGongys(Gongys gys) {
		return (Gongys) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ppl.gysinfo", gys);
	}

}
