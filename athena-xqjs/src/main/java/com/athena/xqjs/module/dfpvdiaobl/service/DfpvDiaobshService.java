package com.athena.xqjs.module.dfpvdiaobl.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.diaobl.Diaobmx;
import com.athena.xqjs.entity.diaobl.Diaobsq;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.entity.diaobl.Yicsbcz;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：DFPVDiaobshService
 * <p>
 * 类描述：DFPV调拨审核service
 * </p>
 * 创建人：xss
 * <p>
 * 创建时间：2016-1-4
 * </p>
 * 
 * @version
 * 
 */
@Component
public class DfpvDiaobshService extends BaseService {
	// LOG4J日志打印信息
	private final Log log = LogFactory.getLog(getClass());
	@Inject
	private DfpvDiaobsqService dfpvdiaobsqService;
	
	@Inject
	private DfpvDiaobsqOperationService dfpvdiaobsqOperationService;
	
	/**
	 * 调拨审核主页面 查询分页方法
	 * 
	 * @param pageable
	 *            map
	 * 
	 * @return map
	 */
	public Map<String, Object> select(Pageable page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("dfpvdiaobl.queryDiaobsq", param, page);
	}

	/**
	 * 全部审核的，点击生效按钮,更改主表状态为已生效 某个计划员只能生效改计划员对应的零件
	 * 
	 * @param Diaobsq
	 * @return list
	 */
	public String modifyState(Diaobsq bean) {
		// 1代表调拨申请单未生效，计划员对应的零件审批完
		// 2代表计划员对应的零件未审批完
		// 3代表调拨申请单已生效
		String flag = "1";
		// 检查该计划员下对应的零件是否全部审批完成
		// 取得用户中心、调拨申请单号下的零件状态
		// count1为状态值为未审核的次数
		bean.setZhuangt(Const.DIAOBL_ZT_APPLYING);
		int count1 = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.jihymxZhuangtCount", bean);
		if (count1 != 0) {
			// 未审核完，不能生效
			flag = "2";
		} else {
			Diaobsq dq = (Diaobsq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.queryState", bean);
			// 状态
			String zhuangt = dq.getZhuangt();
			bean.setZhuangt(zhuangt);
			// 修改时间
			bean.setEdit_time(dq.getEdit_time());
			// 修改人
			bean.setEditor(dq.getEditor());
			bean.setNewZhuangt(Const.DIAOBL_ZT_EFFECT);
			// 如果调拨申请单的状态为已审核，就改为生效状态
			if (zhuangt.equals(Const.DIAOBL_ZT_PASSED)) {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.update_diaobsqstate", bean);
				flag = "3";
			}
		}

		return flag;
	}

	/**
	 * 点击未生效查询
	 * 
	 * @param Diaobsq
	 * @return list
	 */
	public Map<String, Object> select(Diaobsq bean) {
		// 组装成map传参
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("diaobsqdh", bean.getDiaobsqdh());
		map.put("jihy", bean.getJihy());
		map.put("lingjbh", bean.getLingjbh());
		map.put("zhuangt", bean.getZhuangt());
		map.put("pageNo", String.valueOf(bean.getPageNo()));
		map.put("pageSize", String.valueOf(bean.getPageSize()));
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("dfpvdiaobl.select_Diaobsqmx", map, bean);
	}

	/**
	 * 调拨审核 调拨单确认 对实批数量进行归集 返回显示调拨申请明细
	 * 
	 * @param Diaobsq
	 *            实体
	 * @return map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> sumShipsl(Diaobsq bean) {
		Map<String, Object> mapRs = select(bean);
		List<Diaobsqmx> ls = (List<Diaobsqmx>) mapRs.get("rows");
		for (int i = 0; i < ls.size(); i++) {
			Diaobsqmx bean1 = ls.get(i);
			// 对实批数量进行汇总
			BigDecimal shipsl = (BigDecimal) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.sum_shipisl", bean1);
			if (null == shipsl) {
				shipsl = BigDecimal.ZERO;
			}
			bean1.setShipsl(shipsl);
			ls.set(i, bean1);
		}
		mapRs.put("rows", ls);
		return mapRs;
	}

	/**
	 * 调拨审核 调拨单确认 查询调拨明细
	 * 
	 * @param bean
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectDiaobmx(Diaobsqmx bean) {
		Map<String, String> kcmap = (Map<String, String>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.queryZykzb");
		bean.setZiyhqrq(kcmap.get("ZIYHQRQ") == null ? "" : kcmap.get("ZIYHQRQ"));
		// 查询调拨明细中是否存在该条记录
		Map<String, Object> mapRs = null;
		List<?> exist = (List<?>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("dfpvdiaobl.diaobmxExist", bean);
		if (exist.size() == 0) {
			if(bean.getZhuangt().equals("00") || bean.getZhuangt().equals("10")){
				mapRs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("dfpvdiaobl.queryDiaobshmx_notexist", bean, bean);
			}else{
				mapRs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("dfpvdiaobl.queryDiaobshmx_notexistsg", bean, bean);
			}
			
		} else {
			mapRs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("dfpvdiaobl.queryDiaobshmx_exist", bean, bean);
		}
		return mapRs;
	}

	/**
	 * 调拨审核 调拨单确认
	 * 
	 * 调拨单号的生成
	 * 
	 * @param diaobsqdh
	 * @return list
	 */
	public List<Diaobmx> diaobdhGeneration(Diaobmx bean) {
		ArrayList lszm=new ArrayList();
		Map mapzm=new HashMap();
		for (char i = 'A'; i <= 'Z'; i++) {
			if('O'!=i){
				lszm.add(i);
			}
		  }
		for (char i = 'a'; i <= 'z'; i++) {
			if('o' != i){
				lszm.add(i);
			}
		  }
		for (int j = 0; j < lszm.size(); j++) {
			mapzm.put(j+10,lszm.get(j));
		  }
		
		String usercenter = bean.getUsercenter();
		// 以仓库分组查询最大调拨单号
		ArrayList<Diaobmx> ls = (ArrayList<Diaobmx>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dfpvdiaobl.sel_dh", bean);
		// 循环遍历各个仓库相对应的调拨单号
		for (int i = 0; i < ls.size(); i++) {
			Diaobmx dbmx = ls.get(i);
			// 获取调拨单号
			String diaobdh = dbmx.getDiaobdh();
			if (i<9) {
				diaobdh=diaobdh+(i+1);
			} else if(i>=9) {
				if(mapzm.get(i+1)!= null){
					diaobdh=diaobdh+mapzm.get(i+1);
				}
			}
			dbmx.setDiaobdh(diaobdh);
		}
		return ls;
	}

	/**
	 * 调拨审核同意与拒绝按钮 改调拨申请明细表状态
	 * 
	 * @param bean
	 * 
	 * @return count
	 */
	public int updateMxState(Diaobsqmx bean) {
		// 更新调拨申请明细
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.update_diaobsqmxstate",
				bean);
		if (count == 0) {
			throw new ServiceException(MessageConst.UPDATE_COUNT_0);
		}
		return count;
	}
	
	/**
	 * 调拨审核同意与拒绝按钮 改调拨申请明细表状态
	 * 
	 * @param bean
	 * 
	 * @return count
	 */
	public int updateMxState2(Diaobsqmx bean) {
		// 更新调拨申请明细
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.update_diaobsqmxstate2",
				bean);
		if (count == 0) {
			throw new ServiceException(MessageConst.UPDATE_COUNT_0);
		}
		return count;
	}

	/**
	 * 点击确认按钮更新调拨申请主表中的调拨单号 与 状态
	 * 
	 * @param bean
	 * 
	 * @return count
	 */
	public int updateDiaobdhState(Diaobsqmx bean) {
		// map组装传参
		Map<String, String> map = new HashMap<String, String>();
		map.put("diaobsqdh", bean.getDiaobsqdh());
		map.put("usercenter", bean.getUsercenter());
		map.put("zhuangt", bean.getZhuangt());
		map.put("beiz", bean.getBeiz());
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.updateDiaobdh_state", map);
		/*
		 * Diaobsq dq = new Diaobsq(); dq.setDiaobsqdh(bean.getDiaobsqdh());
		 * dq.setUsercenter(bean.getUsercenter()); Diaobsq dqbean = (Diaobsq)
		 * baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.queryState", dq); 不能对已生效的同意或拒绝 if
		 * (dqbean.getZhuangt().compareTo(Const.DIAOBL_ZT_EFFECT) >= 0) { throw
		 * new ServiceException(MessageConst.UPDATE_COUNT_0); }
		 */
		return count;
	}

	public Diaobsq queryState(Diaobsq bean) {
		return (Diaobsq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.queryState", bean);
	}

	/**
	 * 调拨审核 调拨单确认 新增或更新调拨明细表
	 * 
	 * @param bean
	 * @return
	 */
	public int insertDiaobmx(Diaobmx bean) {
		Integer xuh = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.select_shxuh", bean);
		// 序号为空，初始为零
		int xuH = xuh == null ? 0 : xuh;
		// 序号从1开始递增
		bean.setXuh(xuH + 1);
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.doinsertOrupdate_diaobmx",
				bean);
		if (count == 0) {
			throw new ServiceException(MessageConst.UPDATE_COUNT_0);
		}
		return count;
	}

	/**
	 * 调拨审核 调拨单确认 审核的时候，主表的状态改变
	 * 
	 * @param bean
	 * @return
	 */
	public int changeState(Diaobsqmx bean) {
		// 取得用户中心、调拨申请单号下的零件状态
		bean.setZhuangt(null);
		int count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.get_mxZhuangt", bean);
		bean.setZhuangt(Const.DIAOBL_ZT_APPLYING);
		// count1为状态值为未审核的次数
		int count1 = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.get_mxZhuangt", bean);
		String state;
		if (count1 == count) {
			// 未审核
			state = Const.DIAOBL_ZT_APPLYING;
		} else if (count1 == 0) {
			// 已审核
			state = Const.DIAOBL_ZT_PASSED;
		} else {
			// 审核中
			state = Const.DIAOBL_ZT_APPROVING;
		}
		bean.setZhuangt(state);
		// 返回修改数
		return this.updateDiaobdhState(bean);
	}

	/**
	 * 调拨审核终止
	 * 
	 * 调拨单已生效查询
	 * 
	 * @param bean
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectZhongzi(Diaobsq bean) {
		Map<String, Object> mapRs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("dfpvdiaobl.zhongzi_select", bean, bean);
		// 调拨终止查询
		List<Diaobsqmx> ls = (List<Diaobsqmx>) mapRs.get("rows");
		for (int i = 0; i < ls.size(); i++) {
			Diaobsqmx resultBean = ls.get(i);
			// 汇总实批数量
			BigDecimal shipsl = (BigDecimal) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.sum_shipisl", resultBean);
			if (null == shipsl) {
				// 为空置为零
				shipsl = BigDecimal.ZERO;
			}
			resultBean.setShipsl(shipsl);
			// 汇总执行数量
			BigDecimal zhixsl = (BigDecimal) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.zhixsl_zhongzi", resultBean);
			if (null == zhixsl) {
				// 为空置为零
				zhixsl = BigDecimal.ZERO;
			}
			resultBean.setZhixsl(zhixsl);
			// 将resultBean重新放入list中
			ls.set(i, resultBean);
		}
		mapRs.put("rows", ls);
		return mapRs;
	}

	/**
	 * 获取数据库系统时间
	 */
	public String getSysdate() {
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.select_sysdate");
	}

	/**
	 * 合并方法 点击确认按钮，单条调拨申请明细的同意 1生成调拨单号， 2新增或修改调拨明细表， 3更改调拨申请明细表的状态 4判断更改调拨申请表的状态
	 */
	@Transactional
	public String verify(Diaobsqmx sqmx, List<Diaobmx> ls) {
		//将实批数量记录下来存入申请明细表
		BigDecimal shipsl =  BigDecimal.ZERO;
		BigDecimal ucrl =  BigDecimal.ZERO;
		String uclx =""; 
		String shifdbz="";
		String shifdcjf="";
		
		for (int i = 0; i < ls.size(); i++) {
			Diaobmx bean = ls.get(i);

			// 2新增或修改调拨明细表，
			if (bean.getShipsl() != null && !bean.getShipsl().equals(BigDecimal.ZERO)) {
				this.insertDiaobmx(bean);
			}
			shipsl=shipsl.add(bean.getShipsl());
			
			ucrl = bean.getUcrl();
			uclx = bean.getUclx();
			
			shifdbz = bean.getShifdbz();
			shifdcjf = bean.getShifdcjf();
			
		}
		if(ls==null || ls.size()<=0){
			sqmx.setChayibz("差异:"+sqmx.getShenbsl());
		}else if(shipsl.compareTo(sqmx.getShenbsl())!=0 ){
			sqmx.setChayibz("差异:"+(sqmx.getShenbsl().subtract(shipsl)));
		}
		sqmx.setShipsl(shipsl);
		sqmx.setUcrl(ucrl);
		sqmx.setUclx(uclx);
		
		sqmx.setShifdbz(shifdbz);
		sqmx.setShifdcjf(shifdcjf);
		
		// 3更改调拨申请明细表的状态
		this.updateMxState2(sqmx);
		// 4判断、更改调拨申请表的状态
////////////////////////////////////////////////////
		this.changeState2(sqmx);
		return Const.FLAG_TRUE;
	}
	
	private void changeState2(Diaobsqmx bean)
	{
		Diaobsq diaobsq = new Diaobsq();
		diaobsq.setUsercenter(bean.getUsercenter());
		diaobsq.setDiaobsqdh(bean.getDiaobsqdh());
		diaobsq = (Diaobsq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.queryState", diaobsq);
		if(null != diaobsq)
		{
			String flagZt = diaobsq.getZhuangt();
			if(StringUtils.isNotBlank(flagZt) && flagZt.equalsIgnoreCase(Const.DIAOBL_ZT_APPLYING))
			{
				//0010605备注字段英文双引号转换为中文双引号 gswang 2014-10-31
				bean.setBeiz(bean.getBeiz().replace("\"", "”"));
				bean.setZhuangt(Const.DIAOBL_ZT_APPROVING);
				this.updateDiaobdhState(bean);
			}
		}
	}

	/**
	 * 合并方法 点击拒绝按钮，多条调拨申请明细的拒绝 1.更改调拨申请明细表的状态 2.判断、更新调拨申请表的状态
	 */
	@Transactional
	public String refused(List<Diaobsqmx> ls) {
		// 1更改调拨申请明细表的状态
		for (int i = 0; i < ls.size(); i++) {
			Diaobsqmx bean = ls.get(i);
			this.updateMxState(bean);
			// 2.判断、更新调拨申请表的状态
			this.changeState(bean);
		}
		return Const.FLAG_TRUE;
	}

	/**
	 * 生效打印按钮
	 * 
	 * 1.生成调拨单号
	 * 
	 * 2.更新调拨明细的调拨单号、状态
	 * 
	 * 3.更新调拨申请的状态
	 */

	public String effect(Diaobmx bean, Diaobsq sq) {
		// 先进行生效操作
		String flag = null;
		sq.setUsercenter(bean.getUsercenter());
		sq.setDiaobsqdh(bean.getDiaobsqdh());
		sq.setEdit_time(bean.getEdit_time());
		sq.setEditor(bean.getEditor());
		sq.setNewEdit_time(bean.getNewEdit_time());
		sq.setNewEditor(bean.getNewEditor());
		flag = this.modifyState(sq);
		// 全部生效成功
		if (flag.equalsIgnoreCase("3")) {
			// 以仓库归集生成调拨单号
			List<Diaobmx> ls = this.diaobdhGeneration(bean);
			for (int i = 0; i < ls.size(); i++) {
				Diaobmx mx = ls.get(i);
				mx.setUsercenter(bean.getUsercenter());
				mx.setDiaobsqdh(bean.getDiaobsqdh());
				mx.setNewEdit_time(bean.getNewEdit_time());
				mx.setNewEditor(bean.getNewEditor());
				mx.setZhuangt(Const.DIAOBL_ZT_EFFECT);
				// 更新调拨单号、状态为已生效
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.insert_diaobdh", mx);							
			
				
				/* xss-0012554 记录异常消耗信息 生效时*/
				List<Diaobmx> diaobmxs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dfpvdiaobl.query_diaobmx", bean); 
				
				for (int j =0; j < diaobmxs.size();j++) {
					Yicsbcz mx2 =  new Yicsbcz();
					Diaobmx dbs = diaobmxs.get(j);
					
					mx2.setUsercenter(dbs.getUsercenter());
					mx2.setLiush( dbs.getDiaobdh() );
					mx2.setLingjbh( dbs.getLingjbh() );
					mx2.setCaozsl( dbs.getShipsl() );
					
					String shengbd = dbs.getCangkbh();
					mx2.setShengbd( shengbd);
					
					mx2.setFlag("1");
					mx2.setCaozlx("1");
					
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.insert_yicsbcz", mx2);								
				} 
			}

			
			List<Diaobsqmx> diaobsqmxs =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dfpvdiaobl.query_zhongzi_diaobsqmx", sq);
			for (Diaobsqmx diaobsqmx : diaobsqmxs) 
			{
				diaobsqmx.setNewEdit_time(bean.getNewEdit_time());
				diaobsqmx.setNewEditor(bean.getNewEditor());
				diaobsqmx.setZhuangt(Const.DIAOBL_ZT_EFFECT);
				//终止调拨明细
				updateMxState(diaobsqmx);
			}
						
		}
		return flag;
	}

	/**
	 * 调拨审核终止 查询调拨明细
	 * 
	 * @param bean
	 * @return list
	 */
	public Map<String, Object> selectZhongzimx(Diaobsqmx bean, Map<String, String> map) {
		Map<String, Object> mapRs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("dfpvdiaobl.zhongzimx_select", map, bean);
		// 调拨终止查询
		List<Diaobmx> ls = (List<Diaobmx>) mapRs.get("rows");
		for (int i = 0; i < ls.size(); i++) {
			Diaobmx resultBean = ls.get(i);

			// 汇总执行数量
			BigDecimal zhixsl = (BigDecimal) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.zhixsl_zhongzick", resultBean);
			if (null == zhixsl) {
				// 为空置为零
				zhixsl = BigDecimal.ZERO;
			}
			resultBean.setZhixsl(zhixsl);
			// 将resultBean重新放入list中
			ls.set(i, resultBean);
		}
		mapRs.put("rows", ls);
		return mapRs;
	}

	/**
	 * 调拨审核终止 查询调拨明细
	 * 
	 * @param bean
	 * @return list
	 */
	public List<?> selectZhongzimx(Diaobsqmx bean) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dfpvdiaobl.zhongzimx_select", bean);
	}

	/**
	 * 调拨审核终止 点击终止按钮
	 * 
	 * @param bean
	 * @return 主键
	 */
	public int updateZhongzi(Diaobmx bean) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.zhongzi_state", bean);
	}

	@Transactional
	public void updateZhongziDiaobmxAndYaohl(Diaobmx bean) 
	{
		//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.updateYhlNByDiaob", bean); 修改内部要货令状态
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.zhongzi_state", bean);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.update_Diaobsqmx_State", bean);
		
		//如果未发送过 - 30，进行终止操作时，将beiz1改为1，不发送终止申请。
		String beiz1 = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("dfpvdiaobl.selectDiaobBeiz1", bean);
		if( beiz1.equals("30") ){ 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.update_Diaob_State_1", bean);
		 }else{ 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.update_Diaobsq_State", bean);
		}
	}
	
	/**
	 * 调拨令审核-终止
	 * 
	 * 按零件进行终止
	 * 
	 */
	@Transactional
	public String lingjStop(List<Diaobsqmx> ls) {
		String flag = Const.FLAG_TRUE;
		for (int i = 0; i < ls.size(); i++) {
			// 查出该条申请下的所有调拨明细
			ArrayList<Diaobmx> mx = (ArrayList<Diaobmx>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dfpvdiaobl.read_diaobmx", ls.get(i));
			// 对调拨明细进行终止
			for (int j = 0; j < mx.size(); j++) {
				Diaobmx bean = mx.get(i);
				bean.setZhuangt(Const.DIAOBL_ZT_STOPPED);
				int count = this.updateZhongzi(bean);
				if (count == 0) {
					flag = Const.FLAG_FALSE;
					break;
				}
			}

		}
		return flag;
	}

	/**
	 * 调拨令审核-终止
	 * 
	 * 按仓库进行终止
	 * 
	 */
	@Transactional
	public String cangkStop(List<Diaobmx> mx) {
		String flag = Const.FLAG_TRUE;
		for (int i = 0; i < mx.size(); i++) {
			Diaobmx bean = mx.get(i);
			bean.setZhuangt(Const.DIAOBL_ZT_STOPPED);
			// 调拨明细进行终止
			if (this.updateZhongzi(bean) == 0) {
				flag = Const.FLAG_FALSE;
				break;
			}
		}
		return flag;
	}

	/**
	 * list拼接map方法
	 * 
	 * @param list
	 *            要拼接的list
	 * @return 拼接的map
	 */
	public Map<String, Object> listToMap(List list) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("total", list.size());
		map.put("rows", list);
		return map;
	}

	/**
	 * 调拨明细查询的打印方法
	 * 
	 * @param Diaobsqmx
	 * @return json字符串
	 */
	public String printDiaobmx(Diaobsqmx bean) {
		// 查询仓库明细
		@SuppressWarnings("unchecked")
		ArrayList<Diaobmx> mx = (ArrayList<Diaobmx>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dfpvdiaobl.read_diaobmx", bean);
		// 获取调拨申请
		Diaobsq paramSq = new Diaobsq();
		paramSq.setDiaobsqdh(bean.getDiaobsqdh());
		Diaobsq sq = dfpvdiaobsqService.selectDiaobsq(paramSq);
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		Map<String, String> u_map = new HashMap<String, String>();
		//0007964: 需求申请人为单据创建人
		u_map.put("loginname", sq.getCreator());
		List u_list = (ArrayList) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dfpvdiaobl.queryUserByLoginname", u_map);
		// json数组
		JSONArray ar = new JSONArray();
		// json对象
		JSONObject jsonObject = null;
		// set排序集合
		Set<String> set = new TreeSet<String>();
		// list集合
		List<String> ls = new ArrayList<String>();
		// 放入仓库编号
		for (int i = 0; i < mx.size(); i++) {
			Diaobmx dbmx = mx.get(i);
			ls.add(i, dbmx.getCangkbh());
		}
		// 过滤重复仓库编号
		for (int i = 0; i < ls.size(); i++) {

			set.add(ls.get(i));
		}
		// 取得仓库明细中同一仓库的信息
		for (Iterator<String> iter = set.iterator(); iter.hasNext();) {
			// 遍历set
			String element = (String) iter.next();
			// 取的改仓库编号在mx集合中的开始位置
			int index = ls.indexOf(element);
			// 取的改仓库编号在mx集合中的结束位置
			int lastindex = ls.lastIndexOf(element);
			log.info(element + ":" + "\t" + index + "\t" + lastindex);
			// 取出改仓库编号在mx集合中从开始位置到结束位置的所有记录
			// 每个仓库的总记录数
			int rowCount = lastindex - index + 1;
			// 每页显示数
			int pageSize = 20;
			// 总页数
			int totalPage = (rowCount - 1) / pageSize + 1;
			// System.out.println(totalPage +"\t"+rowCount);
			for (int i = 1; i <= totalPage; i++) {
				// 开始行
				int pageEndRow;
				// 结束行
				int pageStartRow;
				// 分页
				pageEndRow = pageSize * i;
				pageStartRow = pageSize * (i - 1);
				// json对象
				jsonObject = new JSONObject();
				jsonObject.put("DIAOBDSL", set.size());
				jsonObject.put("BEIZ", sq.getBeiz());
				jsonObject.put("XQSQR", sq.getCreator());
				jsonObject.put("CHENGBZX", sq.getChengbzx());
				jsonObject.put("USERCENTER", sq.getUsercenter());
				jsonObject.put("SHENGHYID", AuthorityUtils.getSecurityUser().getUsername());
				// 设置用户信息
				if (u_list.size() == 1) {
					Map<String, String> u_rmap = (Map<String, String>) u_list.get(0);
					jsonObject.put("NAME", u_rmap.get("NAME"));
					jsonObject.put("LOGINNAME", u_rmap.get("LOGINNAME"));
					jsonObject.put("CELLPHONE", u_rmap.get("CELLPHONE"));
					jsonObject.put("OFFICEPHONE", u_rmap.get("OFFICEPHONE"));
					jsonObject.put("FAMILYPHONE", u_rmap.get("FAMILYPHONE"));
				}
				for (int j = pageStartRow; j < pageEndRow; j++) {
					// 每页序号从1到8开始
					int temp = j % pageSize + 1;
					if (j + index > lastindex) {
						jsonObject.put("XUH" + temp, "");
						jsonObject.put("LUX" + temp, "");
						jsonObject.put("LINGJBH" + temp, "");
						jsonObject.put("LINGJMC" + temp, "");
						jsonObject.put("DIAOBSL" + temp, "");
					} else {
						Diaobmx dbmx = mx.get(j + index);
						jsonObject.put("XUH" + temp, j + 1);
						jsonObject.put("DIAOBDH", dbmx.getDiaobdh());//去调拨申请单号
						jsonObject.put("CANGKBH", dbmx.getCangkbh());
						jsonObject.put("SHENGXSJ", dbmx.getShengxsj());
						jsonObject.put("LUX" + temp, dbmx.getLux());
						jsonObject.put("LINGJBH" + temp, dbmx.getLingjbh());
						jsonObject.put("LINGJMC" + temp, dbmx.getLingjmc());
						jsonObject.put("DIAOBSL" + temp, dbmx.getShipsl());
					}
				}
				// 将jons对象放到数组里
				ar.add(jsonObject);
			}
		}
		// 转成字符串
		String json = ar.toString();
		// 打印字符串
		log.info(json);

		return json;
	}

	/**
	 * 调拨差异查询的打印方法
	 * 
	 * @param Diaobsqmx
	 * @return json字符串
	 */
	public String printDiaobCymx(Diaobsq bean) {

		@SuppressWarnings("unchecked")
		// json数组
		JSONArray ar = new JSONArray();
		// json对象
		JSONObject jsonObject = null;
			// 获取申请明细
			Map<String, String> map = new HashMap<String, String>();
			map.put("usercenter", bean.getUsercenter());
			map.put("diaobsqdh", bean.getDiaobsqdh());
			//map.put("diaobsqsj",bean.getDiaobsqsj());
			ArrayList<Diaobsqmx> sqmxls = (ArrayList<Diaobsqmx>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dfpvdiaobl.read_diaobcymx", map);
			jsonObject = new JSONObject();
			jsonObject.put("USERCENTER", bean.getUsercenter());
			jsonObject.put("DIAOBDH", bean.getDiaobsqdh());
			jsonObject.put("SHENGXSJ", bean.getDiaobsqsj());
			int rowCount = sqmxls.size();
			// 每页显示数
			int pageSize = 20;
			// 总页数
			int totalPage = (rowCount - 1) / pageSize + 1;
			for (int i = 1; i <= totalPage; i++) {
				// 开始行
				int pageEndRow;
				// 结束行
				int pageStartRow;
				// 大于总行数
				if (i == totalPage) {
					pageEndRow = pageSize * i;
					pageStartRow = pageSize * (totalPage - 1);
				} else if (i * pageSize < rowCount) {
					pageEndRow = i * pageSize;
					pageStartRow = pageEndRow - pageSize;
				} else {
					pageEndRow = rowCount;
					pageStartRow = pageSize * (totalPage - 1);
				}
				for (int j = pageStartRow; j < pageEndRow; j++) {
					// 每页序号从1到8开始
					int temp = j % pageSize + 1;
					if (j >= sqmxls.size()) {
						jsonObject.put("XUH" + temp, "");
						jsonObject.put("LUX" + temp, "");
						jsonObject.put("LINGJBH" + temp, "");
						jsonObject.put("LINGJMC" + temp, "");
						jsonObject.put("YAOHSJ" + temp, "");
						jsonObject.put("DIAOBSL" + temp, "");
						jsonObject.put("SHIPSL" + temp, "");
						jsonObject.put("BZ" + temp, "");
					} else {
						Diaobsqmx sqmx = sqmxls.get(j);
						jsonObject.put("XUH" + temp, j + 1);
						jsonObject.put("LUX" + temp, sqmx.getLux());
						jsonObject.put("LINGJBH" + temp, sqmx.getLingjbh());
						jsonObject.put("LINGJMC" + temp, sqmx.getLingjmc());
						jsonObject.put("YAOHSJ" + temp, sqmx.getYaohsj());
						jsonObject.put("DIAOBSL" + temp, sqmx.getShenbsl());
						jsonObject.put("SHIPSL" + temp, sqmx.getShipsl() );
						jsonObject.put("BZ" + temp, sqmx.getChayibz());
					}

				}
				// 将jons对象放到数组里
				ar.add(jsonObject);
			}
		return ar.toString();
	}
	/**
	 * @see 修改状态
	 * @param all
	 * @param diaoblZtStopped
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Transactional
	public void updateStoppedStateByLevel1(List<Diaobsqmx> all ,String time , String username) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		if(null != all && all.size() > 0)
		{
			int flagInt = 0;
			Map<String,Object> flagMap = translateListToMap(all, "usercenter","diaobsqdh","lux","lingjbh");
			String diaoblZtStopped = Const.DIAOBL_ZT_STOPPED;
			Diaobsq diaobsq = new Diaobsq();
			Diaobsqmx diaobsqmx = all.get(0);
			diaobsq.setUsercenter(diaobsqmx.getUsercenter());
			diaobsq.setDiaobsqdh(diaobsqmx.getDiaobsqdh());
			diaobsq = dfpvdiaobsqService.selectDiaobsq(diaobsq);
			//如果找到调拨申请单
			if(null != diaobsq)
			{
				//找调拨单下的未终止的调拨申请明细
				@SuppressWarnings("unchecked")
				List<Diaobsqmx> diaobsqmxs =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dfpvdiaobl.query_zhongzi_diaobsqmx", diaobsq);
				if(null != diaobsqmxs && diaobsqmxs.size() > 0)
				{
					for (Diaobsqmx flag : diaobsqmxs) 
					{
						StringBuffer flagSb = new StringBuffer();
						flagSb.append(flag.getUsercenter()).append(flag.getDiaobsqdh()).append(flag.getLux()).append(flag.getLingjbh());
						flag.setNewEdit_time(time);
						flag.setNewEditor(username);
						flag.setZhuangt(diaoblZtStopped);
						if(null == flagMap.get(flagSb.toString()))
						{
							continue;
						}
						//终止调拨明细
						updateMxState(flag);
						flagInt ++;
					}
					if(flagInt == diaobsqmxs.size())
					{
						//终止调拨申请
						updateDiaobdhState(diaobsqmxs.get(0));
					}
				}
			}
		}
	}
	
	
	/**
	 * @see   修改状态
	 * @param dbmx
	 * @param javaTime
	 * @param username
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Transactional
	public void updateStoppedStateByLevel2(List<Diaobmx> dbmx,String time, String username) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException 
	{
		if(null != dbmx && dbmx.size() > 0)
		{
			Map<String,Object> flagMap = translateListToMap(dbmx, "usercenter","diaobsqdh","lux","lingjbh","cangkbh");
			Diaobsqmx diaobsqmx = new Diaobsqmx();
			Diaobmx diaobmx = null;
			Set<Entry<String, Object>> flagSet = flagMap.entrySet();
			for (Entry<String, Object> entry : flagSet) 
			{
				diaobmx = (Diaobmx) entry.getValue();
				diaobsqmx.setUsercenter(diaobmx.getUsercenter());
				diaobsqmx.setDiaobsqdh(diaobmx.getDiaobsqdh());
				diaobsqmx.setLingjbh(diaobmx.getLingjbh());
				diaobsqmx.setLux(diaobmx.getLux());
				updateStoppedStateByLevel2(diaobsqmx, time, username);
			}
		}
	}
	
	
	@Transactional
	public void updateStoppedStateByLevel2(Diaobsqmx diaobsqmx,String time, String username) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException 
	{
		if(null != diaobsqmx )
		{
			diaobsqmx = dfpvdiaobsqOperationService.queryDiaobsqmx(diaobsqmx);
			if(null != diaobsqmx)
			{
				@SuppressWarnings("unchecked")
				//如果没有找到未被终止的调拨明细   则更新调拨申请零件状态
				List<Diaobmx> diaobmxs =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("dfpvdiaobl.query_zhongzi_diaobmx", diaobsqmx);
				//注释：如果不存在未被终止的调拨单
				//DFPV按照整个申请单，进行子申请单的全部终止
				//if(null == diaobmxs || diaobmxs.size() == 0)
				//{
					updateStoppedStateByLevel1(Arrays.asList(diaobsqmx), time, username);
				//}
			}
		}
	}
	
	
	/**
	 * @see   根据参数得到目标属性值作为KEY进行存储数据
	 * @param diaobsqmxs
	 * @param propertys
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private Map<String,Object> translateListToMap(List diaobsqmxs,String... propertys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Map<String, Object> result = new HashMap<String, Object>();
		Object diaobsqmx = null;
		if(null != diaobsqmxs && diaobsqmxs.size() > 0 && null != propertys && propertys.length > 0)
		{
			for (int i=0,j=diaobsqmxs.size();i<j;i++) 
			{
				diaobsqmx = diaobsqmxs.get(i);
				StringBuffer flagKey = new StringBuffer();
				for (int k = 0,p = propertys.length; k < p; k++)
				{
					flagKey.append(BeanUtils.getProperty(diaobsqmx, propertys[k]));
				}
				result.put(flagKey.toString(), diaobsqmx);
			}
		}
		return result;
	}

	public int dodiaobsm() 
	{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("dfpvdiaobl.diaobsm");
	}
	
	
	/*
	 * 更新状态为1，不发送
	 * */
	public void update_Diaob_State_1(Diaobmx bean){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.update_Diaobsq_State_1", bean);
	} 
	
	/*
	 * 更新状态为1，不发送
	 * */
	public void update_Diaobsqmx_State_1(Diaobsqmx bean){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.update_Diaobsq_State_1", bean);
	}
	
	/*
	 * 查询申请单目前的状态，是否已经发送
	 * */
	
	public String selectDiaobBeiz1(Diaobmx bean) {
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.selectDiaobBeiz1",bean);
	}
	
	/*
	 * 查询申请单目前的状态，是否已经发送
	 * */
	
	public String selectDiaobsqBeiz1(Diaobsqmx bean) {
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("dfpvdiaobl.selectDiaobsqBeiz1",bean);
	}

	/* 0012554 插入执行层异常表 */
	public void insert_yicsbcz(Yicsbcz bean){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("dfpvdiaobl.insert_yicsbcz", bean);		
	}
}
