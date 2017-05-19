package com.athena.xqjs.module.ilorder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:订单明细类
 * </p>
 * <p>
 * Description:订单明细类
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
 * @date 2011-12-26
 */
@Component
public class DingdmxService extends BaseService {
	@Inject
	private IlYaohlService ilYaohlService;

	/**
	 * 获取上次计算时间
	 * @param dingdjssj 当前订单计算时间
	 * @return 上次计算时间
	 */
	public String getShangcjssj(){
		return  CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryAxScjssj"));
	}
	
	/**
	 * 插如操作，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：Dingdmx bean 订单明细实体
	 */
	public boolean doInsert(Dingdmx bean) {
		int count = 0;
		if(bean.getDingdh()!=null){
			CommonFun.logger.debug("Dingdmx插如操作doInsert方法的sql语句为：ilorder.insertDingdmx");
			count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertDingdmx", bean);
		}
		return count > 0;
	}

	/**
	 * 插如操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：Dingdmx bean 订单明细实体
	 */
	@Transactional
	public void doInsert(List<Dingdmx> list) {
		for (int i = 0; i < list.size(); i++) {
			Dingdmx bean = list.get(i);
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertDingdmx", bean);
			if (count == 0) {
				throw new ServiceException();
			}
		}
	}

	/**
	 * 删除操作，返回id
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：Dingdmx bean 订单明细实体
	 */
	public void doDelete(Dingdmx bean) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteDingdmx", bean);
	}

	/**
	 * 更新操作
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：Dingdmx bean 订单明细实体
	 */
	@Transactional
	public boolean doUpdate(Dingdmx bean) {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateDingdmx", bean);
		return count > 0;
	}

	@Transactional
	public boolean doUpdateMx(Dingdmx bean) {
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS)
				.execute("ilorder.updateDingdmxForKd", bean);
		return count > 0;
	}
	/**
	 * 更新订单明细状态
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-09 参数说明：订单号，订单状态
	 */
	@Transactional
	public boolean doUpdateMxZt(String dingdh,String dingdzt) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("dingdh", dingdh);
		map.put("zhuangt", dingdzt);
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateDingdmxZt", map);
		return count > 0;
	}
	/**
	 * 查询全部分页，返回map
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：page
	 */
	public Map<String, Object> select(Pageable page) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryDingdmx", page);
	}

	/**
	 * 查询全部分页，返回map
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：page,map
	 */
	public Map<String, Object> queryAllDingdmx(Pageable page, Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryDingdmx", map, page);
	}
	/**
	 * 关联零件表查询全部分页，返回map
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：page,map(订单号，零件编号)
	 */
	public List<Dingdmx> queryDingdmxMap(Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingdmxByLingjbh", map) ;
	}

	public Map<String, Object> queryDingdmxMap(Pageable bean, Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryDingdmxByLingjbh", map, bean);
	}

	/**
	 * 查询全部，返回List
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2012-12-4
	 * @参数：模式
	 */
	private String getParrm(String parrten,String id){
		Map<String, String> map = new HashMap<String, String>();
		map.put(Const.PP, id+"queryDingdmxByPC") ;
		map.put(Const.PJ, id+"queryDingdmxByJC") ;
		map.put(Const.PS, id+"queryDingdmxBySC") ;
		return map.get(parrten.toUpperCase())==null?null:map.get(parrten.toUpperCase());
	
	}
	public List queryAllByParrten(String parrten) {
		String id = "ilorder.";
		String parrm = this.getParrm(parrten, id) ;
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(parrm);
	}

	/**
	 * 查询全部分页，返回map
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：page
	 */
	public List<Dingd> queryOrderNumber(String usercenter, String dingdh) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		map.put("dingdh", dingdh.substring(0, 4));
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryOrderNums", map);
	}

	/**
	 * 查询全部分页，返回list
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：page
	 */
	public List<Dingdmx> queryListMx(Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingdmx", map);
	}

	/**
	 * 查询实体
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09 参数说明：page
	 */
	public Dingdmx queryDingdmxObject(Map<String, String> map) {
		return (Dingdmx) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDingdmx", map);
	}

	/**
	 * 统计一个周期内的要货数量
	 * 
	 * */
	public BigDecimal queryDingdmxShul(Map<String, String> map) {
		return new BigDecimal(this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryShul", map).toString());
	}

	/**
	 * @方法：循环插入订单明细中
	 * @author 李明
	 * @version v1.0
	 * @date 2012-01-03
	 * @参数：list集合，实体类
	 */
	public void insertListMx(List all, Object bean) {
		if (!all.isEmpty()) {
			for (Object obj : all) {
				this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertDingdmx", obj);
			}
		}
	}

	/**
	 * 根据查询条件查询已生效订单明细
	 * 
	 * @author 李智
	 * @date 2012-2-28
	 * @param page
	 *            分页显示
	 * @param param
	 *            查询条件
	 * @return Map 检索结果
	 */
	public Map queryDingdmxByParam(Pageable page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryDingdmxByParam", param, page);
	}

	/**
	 * 查询某条订单明细可终止的数量
	 * 
	 * @author 李智
	 * @date 2012-2-28
	 * @param param
	 *            订单明细条件
	 * @return double 可终止数量
	 */
	public double queryMxAllowZzsl(Map<String, String> param) {
		Dingdmx dingdmx = (Dingdmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryMxAllowGzsl", param);
		// 返回可终止数量
		if (dingdmx == null) {
			return 0;
		}
		return dingdmx.getAllowGzsl().doubleValue();
	}

	 

	/**
	 * 终止订单行
	 * 
	 * @author 李智
	 * @date 2012-2-28
	 * @param Dingdmxs
	 *            订单明细
	 * @param loginUser
	 *            登录信息
	 * @return message
	 */
	public List<Yaohl> dingdmxZz(List<Dingdmx> all, LoginUser loginUser) {
		List<Yaohl> allowYaohl = new ArrayList<Yaohl>();// 可终止的订单零件
		List<Yaohl> noAllowYaohl = new ArrayList<Yaohl>();// 可终止的数量为0的订单零件

		for (int i = 0; i < all.size(); i++) {
			Dingdmx dingdmx = (Dingdmx) all.get(i);
			Map<String, String> map = new HashMap<String, String>();
			// 终止的订单行
			map = this.getParamsByBean(map, dingdmx);
			map.put("yaohlzt", Const.YAOHL_BIAOD);
			// 可终止的数量
			double allowGzsl = this.queryMxAllowZzsl(map);
			// 可终止的数量大于0
			Yaohl yaohl = new Yaohl();
			yaohl.setUsercenter(dingdmx.getUsercenter()); // 用户中心
			yaohl.setDingdh(dingdmx.getDingdh()); // 订单号
			yaohl.setLingjbh(dingdmx.getLingjbh()); // 零件编号
			yaohl.setDingdmxid(dingdmx.getId()); // 明细编号
			yaohl.setEditor(loginUser.getUsername()); // 更新人
			yaohl.setAllowGzsl(new BigDecimal(allowGzsl)); // 可终止量

			if (allowGzsl > 0) {
				allowYaohl.add(yaohl);
			} else {
				noAllowYaohl.add(yaohl);
			}
		}

		// 调“仓库service：终止订单零件”
		// allowYaohl
		// 先自己写方法终止
		for (int i = 0; i < allowYaohl.size(); i++) {
			Yaohl yaohl = allowYaohl.get(i);
			yaohl.setYaohlzt("05");
			ilYaohlService.updateYaohlZt(yaohl);
		}

		allowYaohl.addAll(noAllowYaohl);// 有没有终止量都返回
		return allowYaohl;
	}

	/**
	 * 把对象的值设置到map
	 * 
	 * @author 李智
	 * @param map
	 *            初始化的map
	 * @param dingdmx
	 *            bean
	 * @return 设置值后的map
	 */
	public Map<String, String> getParamsByBean(Map<String, String> map, Dingdmx dingdmx) {
		// 订单号
		map.put("dingdh", dingdmx.getDingdh());
		// 供应商代码
		map.put("gongysdm", dingdmx.getGongysdm());
		// 计划员组
		map.put("jihyz", dingdmx.getJihyz());
		// 用户中心
		map.put("usercenter", dingdmx.getUsercenter());
		// 零件号
		map.put("lingjbh", dingdmx.getLingjbh());
		// 明细ID
		map.put("dingdmxid", dingdmx.getId());
		// 交付比例大小等于
		map.put("searchSymbols", dingdmx.getSearchSymbols());
		// 交付比例
		if (dingdmx.getJfbl() != null) {
			map.put("jfbl", dingdmx.getJfbl().toString());
		}
		// 交付时间
		map.put("dingdzzsj_start", dingdmx.getJiaofrqStart());
		// 交付时间
		map.put("dingdzzsj_end", dingdmx.getJiaofrqEnd());
		return map;
	}
	public void deleteList (String usercenter,String dingdh,String gongysdm,String lingjbh ){
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("dingdh", dingdh);
		map.put("gongysdm", gongysdm);
		map.put("lingjbh", lingjbh);
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteDingdmxList", map);
    }
	public BigDecimal sumShul(String dingdh,String lingjbh,String usercenter,String gongysdm,String qisrq,String jiesrq){
		BigDecimal sum = BigDecimal.ZERO;
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("dingdh", dingdh);
		map.put("gongysdm", gongysdm);
		map.put("lingjbh", lingjbh);
		map.put("qisrq", qisrq);
		map.put("jiesrq", jiesrq);
		Object obj = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ilorder.sumShul",map);
		if(obj!=null){
			sum = new BigDecimal(obj.toString());
		}
		return sum;
		
	}
	public Dingdmx queryCgbh(String dingdh){
		Map<String,String> map =new HashMap<String,String>();
		map.put("dingdh", dingdh);
		return (Dingdmx) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ilorder.queryCgbh", map);
	}
	
	
	
	///////////////////////wuyichao 2014-10-29 临时订单导入///////////////////
	
	public Map<String,String> getUsercenterMap()
	{
		Map<String,String> map = new HashMap<String, String>();
		List<String> usercenters = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryUsercenters");
		for (String string : usercenters) 
		{
			map.put(string, string);
		}
		return map;
	}

 	
	///////////////////////wuyichao 2014-10-29 临时订单导入///////////////////
}