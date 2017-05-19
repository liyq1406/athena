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
import com.athena.xqjs.entity.fenzxpc.Dax;
import com.athena.xqjs.entity.fenzxpc.Fenzx;
import com.athena.xqjs.entity.maoxq.DdbhclvCompare;
import com.athena.xqjs.entity.maoxq.JlvXinax;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * DDBH-CLV比较
 * @author gswang
 *
 */
@WebService(endpointInterface="com.athena.xqjs.module.maoxq.service.JlvXinaxCompareImpl", serviceName="/JlvXinaxCompareService")
@Component
public class JlvXinaxCompareService extends BaseService implements JlvXinaxCompareImpl {
	private  Logger logger =Logger.getLogger(JlvXinaxCompareService.class);
	
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
		List<Map<String, String>> ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.queryxinaxDay", paream);
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

	public Map<String, Object> selectDdbhclv(Map<String,String> paream,JlvXinax bean){
		Map<String, Object> ls = new HashMap<String, Object>();
		if(paream.get("jlvxinaxchae") != null && paream.get("jlvxinaxchae").length()>0){
			ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("maoxqCompare.queryjlvxinaxce", paream, bean);
		}else{
			ls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("maoxqCompare.queryjlvxinax", paream, bean);
		}

		return ls;
	}
	
	

	
	
	/**
	 * DDBH-JLV比较
	 */
//	@Transactional
	public String jlvxinaxCompare(){
		logger.info("jlv-xinax比较--4291--开始");
		String CREATOR = "4291";
		Map<String ,String> paream = new HashMap<String ,String>();
		String bijrq = CommonFun.getJavaTime("yyyy-MM-dd");//得到比较时间
		paream.put("BIJRQ", bijrq);
		paream.put("CREATOR", "4291");
		try {		
			//删除XQJS_JLVXINAX表中当天计算的一版比较记录，支持重新计算
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.deleteXinaxJlv", paream);
			//删除XQJS_jlvxinax_TOTLE表中数据
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.deleteXQJS_jlvxinax_TOTLE", paream);
			//查询最新一版Jlv毛需求版次
			List<DdbhclvCompare> clvBanc =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("maoxqCompare.queryJlvMaoxqNew", paream);
			for (DdbhclvCompare ddbhclvCompare : clvBanc) {
				logger.info("XINAX-jlv比较--4291--Jlv版次:"+ddbhclvCompare.getXuqbc());				
				paream.put("XUQBC", ddbhclvCompare.getXuqbc());
				paream.put("usercenter", ddbhclvCompare.getUsercenter());
				paream.put("chanx", ddbhclvCompare.getChanx());
				//汇总jlv毛需求数据到XQJS_JLVXINAX表中
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.sumjlvMaoxqTOTLE", paream);
			}	
		//	//汇总新按需毛需求数据到XQJS_JLVXINAX表中
		//	baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.sumXinaxMaoxqTOTLE", paream);
			Map<String, String> bijtians ;
			//询比较的用户中心，生产线
			List<DdbhclvCompare> bijchanx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("maoxqCompare.queryBijchanxListNew", paream);
			String uc = "";
			int bij = 0;
			for(DdbhclvCompare bijchanxBean: bijchanx){
				paream.put("usercenter", bijchanxBean.getUsercenter());
				paream.put("chanx", bijchanxBean.getChanx());
				if(!bijchanxBean.getUsercenter().equals(uc)){
					uc = bijchanxBean.getUsercenter();
					//查询比较天数
					bijtians = (Map<String, String>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("maoxqCompare.queryBijtsNew", paream);
					if(bijtians != null && bijtians.get("QUJZXZ")!= null && String.valueOf(bijtians.get("QUJZXZ")).length()>0){
						bij = Integer.parseInt(String.valueOf(bijtians.get("QUJZXZ")));
					}else{
						bij = 0;
					}
				}
				bijchanxBean.setBijts(bij);
				bijchanxBean.setBijrq(bijrq);
				
				Dax dax=new Dax();
				dax.setUsercenter(bijchanxBean.getUsercenter());
				dax.setDaxxh(bijchanxBean.getChanx());
				//获取分装线所有的大线
				List<Fenzx> fenzxList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("maoxqCompare.getFenzx", dax);
				StringBuffer sb=new StringBuffer();
				//追加大线
				sb.append("'"+bijchanxBean.getChanx()+"',");
				//追加所有分装线
				for (Fenzx fenzx : fenzxList) {
					sb.append("'"+fenzx.getFenzxh()+"',");
				}
				if(sb.length()>0){
					sb=sb.delete(sb.length()-1, sb.length());
				}	
				paream.put("allchanx", sb.toString());
	
				//查询比较日期
				List<DdbhclvCompare> bijchanxRiq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("maoxqCompare.queryBijchanxMaxRiqNew", bijchanxBean);
				
				for (DdbhclvCompare ddbhclvCompare : bijchanxRiq) {
			
					paream.put("xuqrq",ddbhclvCompare.getXuqrq());
			
					//汇总新按需毛需求数据到XQJS_jlvxinax_TOTLE表中，新按需的比较日期以JLV为主
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.sumXinaxMaoxqTOTLE", paream);
				}
				if(bijchanxRiq != null && bijchanxRiq.size()>0){
					DdbhclvCompare maxrqBean = bijchanxRiq.get(0);//获得最大的比较日期
//					paream.put("minxuqrq",	bijchanxRiq.get(bijchanxRiq.size()-1).getXuqrq());//获得最小的比较日期
//					paream.put("maxxuqrq",maxrqBean.getXuqrq());
//					//汇总新按需毛需求数据到XQJS_jlvxinax_TOTLE表中，新按需的比较日期以JLV为主
//					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.sumXinaxMaoxqTOTLE", paream);
					
					bijchanxBean.setMaxrq(maxrqBean.getXuqrq());
					bijchanxBean.setEditor(CREATOR);
					bijchanxBean.setCreator(CREATOR);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.sumTotleMaoxq", bijchanxBean);
				}
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("maoxqCompare.jisJlvXinaxChae", paream);
			logger.info("jlv-xinax比较 --4291 -- 计算完成");
		} catch (Exception e) {
			logger.error("jlv-xinax比较--4291--错误"+e.toString());
			userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "jlv-xinax比较", "jlv-xinax比较批量失败,"+e.toString());
			throw new ServiceException(e.toString());
		}	
		logger.info("jlv-xinax比较 --4291 -- 结束");
		return "success";
	}
}
