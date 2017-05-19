package com.athena.xqjs.module.maoxq.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.maoxq.Ddbhclv;
import com.athena.xqjs.entity.maoxq.DdbhclvCompare;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * DDBH-CLV比较
 * @author gswang
 *
 */
@WebService(endpointInterface="com.athena.xqjs.module.maoxq.service.DdbhClvCompareImpl", serviceName="/DdbhClvCompareService")
@Component
public class DdbhClvCompareService extends BaseService implements DdbhClvCompareImpl {
	private  Logger logger =Logger.getLogger(DdbhClvCompareService.class);
	
	@Inject
	private UserOperLog userOperLog;
	
	
	/**
	 * <p>
	 * 需求类型为日J0-J8
	 * </p>
	 * 
	 * @author NIESY
	 * @param beginDate
	 *            PJ开始日期
	 * @param maxIndex
	 * @date 2012-03-07
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getDays(String xuqbc, int maxIndex,Map<String,String> paream ) {
		Map<String, String> map = new HashMap<String, String>();
		paream.put("xuqbc", xuqbc);
		List<Map<String, String>> ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.queryddbhDay", paream);
		if(ls.size()==0){
			return map;
		}
		
		for (int i = 0; i < maxIndex; i++) {
			// 从JO起始日期顺序递增
			if (ls.size() < maxIndex && i >= ls.size()) {
				if(ls.size()==1 && i==0){
					map.put("J" + i, ls.get(i).get("XUQRQ"));
				}else{
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar = new GregorianCalendar();
					if (i >= ls.size() - 1) {
						try {
							sf.format(sf.parse(map.get("J" + (i - 1))));
							calendar = sf.getCalendar();
						} catch (ParseException e) {
							logger.error("日期时间解析错误", e);
							throw new ServiceException("日期时间解析错误", e);
						}
					}
					// 获取日期
					calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
					map.put("J" + i, sf.format(calendar.getTime()));
				}
			} else {

				map.put("J" + i, ls.get(i).get("XUQRQ"));
			}

		}
		paream.putAll(map);
		return map;
	}

	public Map<String, Object> selectDdbhclv(Map<String,String> paream,Ddbhclv bean){
		Map<String, Object> ls = new HashMap<String, Object>();
		if(paream.get("clvddhbchae") != null && paream.get("clvddhbchae").length()>0){
			ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("maoxqCompare.queryddbhclvce", paream, bean);
		}else{
			ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("maoxqCompare.queryddbhclv", paream, bean);
		}

		return ls;
	}
	
	/**
	 * DDBH-CLV比较
	 */
//	@Transactional
	public String DdbhClvCompare(){
		logger.info("ddbh-clv比较--4290--开始");
		String CREATOR = "4290";
		Map<String ,String> paream = new HashMap<String ,String>();
		String bijrq = CommonFun.getJavaTime("yyyy-MM-dd");//得到比较时间
		paream.put("BIJRQ", bijrq);
		paream.put("CREATOR", "4290");
		try {
			//查询最新一版clv毛需求
			String clvBanc = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("maoxqCompare.queryClvMaoxqNew", paream);
			logger.info("ddbh-clv比较--4290--clv版次:"+clvBanc);
			paream.put("XUQBC", clvBanc);
			//删除XQJS_DDBHCLV_TOTLE表中数据
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.deleteDdbhClvTotle", paream);
			//删除XQJS_DDBHCLV表中当天计算的一版比较记录，支持重新计算
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.deleteDdbhClv", paream);
			//汇总clv毛需求数据到XQJS_DDBHCLV_TOTLE表中
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.sumClvMaoxq", paream);
			//查询按需毛需求最大的消耗日期
			String maxXhsj = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("maoxqCompare.queryDdbhMaxXhsj", paream);
			if(maxXhsj!=null && maxXhsj.length()>0){
				paream.put("MAXXHSJ", maxXhsj);
				List<Map<String, String>> xhsjList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("maoxqCompare.queryDdbhXhsjRiqList", paream);;
				for(Map<String, String> xhsjriq : xhsjList){
					paream.put("XHSJRIQ", xhsjriq.get("RIQ"));
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.sumDdbhMaoxq", paream);
				}
			}else{
				paream.put("MAXXHSJ", "");
			}
			Map<String, String> bijtians ;
			List<DdbhclvCompare> bijchanx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("maoxqCompare.queryBijchanxList", paream);
			String uc = "";
			int bij = 0;
			for(DdbhclvCompare bijchanxBean: bijchanx){
				paream.put("USERCENTER", bijchanxBean.getUsercenter());
				if(!bijchanxBean.getUsercenter().equals(uc)){
					uc = bijchanxBean.getUsercenter();
					bijtians = (Map<String, String>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("maoxqCompare.queryBijts", paream);
					if(bijtians != null && bijtians.get("QUJZXZ")!= null && String.valueOf(bijtians.get("QUJZXZ")).length()>0){
						bij = Integer.parseInt(String.valueOf(bijtians.get("QUJZXZ")));
					}else{
						bij = 0;
					}
				}
				bijchanxBean.setBijts(bij);
				bijchanxBean.setBijrq(bijrq);
				List<DdbhclvCompare> bijchanxRiq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("maoxqCompare.queryBijchanxMaxRiq", bijchanxBean);
				if(bijchanxRiq != null && bijchanxRiq.size()>0){
					DdbhclvCompare maxrqBean = bijchanxRiq.get(0);
					bijchanxBean.setMaxrq(maxrqBean.getXuqrq());
					bijchanxBean.setEditor(CREATOR);
					bijchanxBean.setCreator(CREATOR);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.sumDdbhClv", bijchanxBean);
				}
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.jisClvDdbhChae", paream);
			logger.info("ddbh-clv比较 --4290 -- 计算完成");
		} catch (Exception e) {
			logger.error("ddbh-clv比较--4290--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "ddbh-clv比较", "ddbh-clv比较批量失败,"+e.toString());
			throw new ServiceException(e.toString());
		}	
		logger.info("ddbh-clv比较 --4290 -- 结束");
		return "success";
	}
}
