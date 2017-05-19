package com.athena.truck.module.liuccz.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Chew;
import com.athena.truck.entity.Yund;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


@Component
public class LiucczService extends BaseService {
	@Inject
	private UserOperLog userOperLog;
	
	
	/**
	 * 查询当前叉车车位关系是否存在
	 */
	public Integer  queryChewccgx(Map map){
		return (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("liuccz.queryChewccgx",map);
	}
	/**
	 * 叉车员查询入厂后的运单信息(通过叉车车位关系查询)
	 */
	public Map<String, Object>  queryChewccydxx(Pageable page, Map<String, String> params) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("liuccz.queryChewccydxx", params, page);
	}
	
	/**
	 *叉车员查询入厂后的运单信息(通过叉车查询大站台下的所有车位信息)
	 */
	public Map<String, Object>  queryChewydxx(Pageable page, Map<String, String> params) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("liuccz.queryChewydxx", params, page);
	}
	/**
	 *查询当前车辆排队最大序号
	 */
	public Map queryQueuemax(){
		List xuhlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("queue.queryQueuemax");
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(null != xuhlist&& xuhlist.size() > 0)
		{
			for (int i=0;i<xuhlist.size();i++) 
			{
				Map m = (Map) xuhlist.get(i);
				map.put(m.get("KEY").toString(),((BigDecimal)m.get("VALUE")).intValue());
			}
		}
		return map;
	}
	//运单撤销
	@Transactional
	public String yundChex(ArrayList<Yund> list,Map<String,String> params) throws ServiceException {
		for( int i = list.size()-1;i>=0;i--){
			Yund bean = list.get(i);
			Yund yd = (Yund)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kac_yundgz.queryYundCheck",bean);
			if(yd==null ){
				list.remove(i);
			}else{
				if("3".equals(yd.getZhuangt())){
					Chew cw=new Chew();
					cw.setChewbh(bean.getBeiz());
					yd.setZhuangtmc("撤销");
					yd.setBeiz1(params.get("username"));
					yd.setZhuangt("90");
					yd.setEditor(params.get("username"));
					try{
						
						//参数集合
						Map<String, String> map = new HashMap<String, String>();
						map = this.getParamMapCX(yd, cw);
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("liuccz.pro_truck_yundcx",map);
					
						if (map.get("result").equals("1")) {
							throw new ServiceException("运单"+bean.getYundh()+"操作失败！");
						}
						
					}catch(RuntimeException e){
						userOperLog.addError(CommonUtil.MODULE_CKX, "运单撤销", "运单撤销异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
						throw new ServiceException(e.getMessage());
					}
				}else{
					throw new ServiceException("运单"+bean.getYundh()+"状态为"+yd.getZhuangt()+" "+yd.getZhuangtmc()+"，不可以撤销!");
				}
			}
		}
		return "success";
	}
	
	
	
	//车位放空
	@Transactional
	public String liuccz(Yund bean,Map<String,String> params) throws ServiceException {
		Yund yd = (Yund)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("liuccz.queryYundCheck",bean);
		yd.setBeiz1(params.get("username"));
		yd.setBeiz(params.get("yd.beiz"));
		yd.setEditor(params.get("username"));
		bean.setEditTime(DateUtil.curDateTime());
		bean.setEditor(params.get("username"));
		yd.setZhuangt(bean.getZhuangt());
		yd.setBeiz1(params.get("username"));
		Chew cw=new Chew();
		cw.setChewbh(params.get("yd.beiz"));
		
		try {
			//参数集合
			Map<String, String> map = new HashMap<String, String>();
			map = this.getParamMap(yd, cw);
			
			//0013582执行操作前需要再次验证是否有符合流程操作的运单
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("yundh", bean.getYundh());
			paramMap.put("usercenter", bean.getUsercenter());
			String yundlccz = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("liuccz.getYundlccz",paramMap);
			yundlccz = CommonUtil.strNull(yundlccz);
			if (yundlccz.equals("")) {
				throw new ServiceException("当前运单"+bean.getYundh()+"状态发生变化，请刷新");
			}else{
				if (Integer.parseInt(bean.getZhuangt()) > Integer.parseInt(yundlccz)) {
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("liuccz.pro_truck_chewfk",map);	
					if (map.get("result").equals("1")) {
						throw new ServiceException("运单"+bean.getYundh()+"操作失败！");
					}
				}else {
					throw new ServiceException("当前运单"+bean.getYundh()+"状态发生变化，请刷新");
				}
			}
		} catch (RuntimeException e) {
			userOperLog.addError(CommonUtil.MODULE_CKX, "流程操作", "流程操作异常", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ServiceException(e.getMessage());
		}
		
		/*try{
				if("8".equals(bean.getZhuangt())){
					Chew cw=new Chew();
					cw.setUsercenter(bean.getUsercenter());
					cw.setQuybh(bean.getQuybh());
					cw.setDaztbh(bean.getDaztbh());
					cw.setEditor(params.get("username"));
					cw.setChewbh(params.get("yd.beiz"));
					cw.setChewzt("0");
					//mantis:0013039 by CSY 车位放空操作时，记录kc_chelpd_history有误，调整 写入历史表 和 删除 排队表 的前后顺序
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("liuccz.updateChew",cw);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("liuccz.updateYund",bean);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("liuccz.insertChlepdls",yd);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("liuccz.deleteChelpd",bean);
				}else{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("liuccz.updateYund",yd);
				}
					yd.setBeiz1(params.get("username"));
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("liuccz.insertChurcls",yd);
				}catch(RuntimeException e){
					userOperLog.addError(CommonUtil.MODULE_CKX, "流程操作", "流程操作", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
					throw new ServiceException("运单"+bean.getYundh()+"操作流水记录失败！");
		}*/
		
		return "success";
	}
	
	public List<Object>  queryLicdy() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("liuccz.queryLicdy");
	}
	
	/**
	 * 拼装参数集合 车位放空
	 * @param yd
	 * @param cw
	 * @return
	 */
	private Map<String, String> getParamMap(Yund yd, Chew cw){
		Map<String, String> map = new HashMap<String, String>();
		map.put("chewbh", cw.getChewbh());
		map.put("editor", yd.getEditor());
		map.put("usercenter", yd.getUsercenter());
		map.put("yundh", yd.getYundh());
		map.put("kach", yd.getKach());
		map.put("zhuangt", yd.getZhuangt());
		map.put("quybh", yd.getQuybh());
		map.put("daztbh", yd.getDaztbh());
		map.put("beiz", yd.getBeiz());
		map.put("beiz1", yd.getBeiz1());
		map.put("biaozsj", yd.getBiaozsj());
		map.put("jijbs", yd.getJijbs());
		map.put("tiqpdbs", yd.getTiqpdbs());
		map.put("result", "");
		
		return map;
	}
	
	/**
	 * 拼装参数集合 运单撤销
	 * @param yd
	 * @param cw
	 * @return
	 */
	private Map<String, String> getParamMapCX(Yund yd, Chew cw){
		Map<String, String> map = new HashMap<String, String>();
		map.put("chewbh", cw.getChewbh());
		map.put("editor", yd.getEditor());
		map.put("usercenter", yd.getUsercenter());
		map.put("yundh", yd.getYundh());
		map.put("kach", yd.getKach());
		map.put("zhuangt", yd.getZhuangt());
		map.put("zhuangtmc", yd.getZhuangtmc());
		map.put("quybh", yd.getQuybh());
		map.put("daztbh", yd.getDaztbh());
		map.put("beiz3", yd.getBeiz3());
		map.put("beiz1", yd.getBeiz1());
		map.put("biaozsj", yd.getBiaozsj());
		map.put("jijbs", yd.getJijbs());
		map.put("tiqpdbs", yd.getTiqpdbs());
		map.put("result", "");
		
		return map;
	}

}
