package com.athena.xqjs.module.report.service;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.report.RepMaoxq;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.support.PageableSupport;

/**
 * 毛需求报表Service
 * @author WL
 *
 */
@Component
public class MaoxqReportService extends BaseService {

	@Inject
	private MaoxqReportParam maoxqReportParam; 
	
	/**
	 * 查询毛需求报表信息
	 * @param page 查询对象
	 * @param param
	 * @return
	 */
	public Map queryMaoxq(PageableSupport page, Map<String, String> param){
		String sql = "report.queryMaoxq";
		//IL按用户中心显示
		if("1".equals(param.get("xianslx")) && "1".equals(param.get("baoblx"))){
			sql = "report.queryMaoxqUc";
		}
		if("exportXls".equals(param.get("exportXls"))){//导出报表
			return CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(sql,param));
		}else{
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages(sql,param,page);
		}
	}
	
	/**
	 * 毛需求分析
	 * @param param
	 * @throws Exception 
	 */
	public void maoxqFenx(Map<String,String> param,String username) throws Exception{
		maoxqReportParam.initParam(param);
		//清除毛需求中间表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("report.deleteRepMaoxq");
 		String today = CommonFun.getJavaTime("yyyy-MM-dd");//当前日期
		List<RepMaoxq> listRepMaoxq = new ArrayList<RepMaoxq>();
		//汇总用户中心,零件
		List<Maoxqmx> listMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryMaoxqLjByCx",param);
		listRepMaoxq = ilMaoxqFenx(listMaoxq, param, today,username);
		List<RepMaoxq> listRepMaoxq2 = new ArrayList<RepMaoxq>();
		listMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryMaoxqKDLjByUc",param);
		listRepMaoxq2 = kdMaoxqFenx(listMaoxq, param, today,username);
		listRepMaoxq.addAll(listRepMaoxq2);
		//保存毛需求分析结果信息
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("report.insertRepMaoxq",listRepMaoxq);
		maoxqReportParam.destory();
		/*
		//IL类型毛需求
		if("1".equals(baoblx)){
			String xianslx = param.get("xianslx");//显示类型,1为按用户中心,2为按产线
			if("1".equals(xianslx)){//按用户中心
				//汇总用户中心,零件
				List<Maoxqmx> listMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryMaoxqLjByUc");
				listRepMaoxq = ilMaoxqFenx(listMaoxq, param, today,username);
			}else{//按产线
				//汇总用户中心,零件
				List<Maoxqmx> listMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryMaoxqLjByCx");
				listRepMaoxq = ilMaoxqFenx(listMaoxq, param, today,username);
			}
		//KD类型毛需求
		}else{
			//汇总用户中心,零件
			//List<Maoxqmx> listMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryMaoxqKDLjByUc");
			//listRepMaoxq = kdMaoxqFenx(listMaoxq, param, today,username);
		}
		*/
	}
	
	/**
	 * IL毛需求分析
	 * @param listMaoxq 毛需求信息
	 * @param param 查询参数
	 * @param today 当前日期
	 * @return 毛需求分析结果集
	 * @throws Exception
	 */
	public List<RepMaoxq> ilMaoxqFenx(List<Maoxqmx> listMaoxq,Map<String,String> param,String today,String username) throws Exception{
		List<RepMaoxq> listRepMaoxq = new ArrayList<RepMaoxq>();
		Class cl = RepMaoxq.class;
		for (int i = 0; i < listMaoxq.size(); i++) {
			Maoxqmx maoxq = listMaoxq.get(i);
			param.put("lingjbh", maoxq.getLingjbh());//零件编号
			param.put("usercenter", maoxq.getUsercenter());//用户中心
			param.put("appobj", maoxq.getChanx());//产线
			//查询日历版次号
			String rilbc = CommonFun.strNull(maoxqReportParam.rilbcMap.get(maoxq.getUsercenter()+maoxq.getChanx()));
			if(StringUtils.isEmpty(rilbc)){
				continue;
			}
			param.put("rilbc",rilbc);
			
			Map<String,BigDecimal> mapMaoxqmxCLV = new HashMap<String, BigDecimal>();
			Map<String,BigDecimal> mapMaoxqmxDIS = new HashMap<String, BigDecimal>();
			Map<String,BigDecimal> mapMaoxqmxDIP = new HashMap<String, BigDecimal>();
			//CLV毛需求明细
			List<Maoxqmx> listMaoxqmxCLV = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryCLVMaoxqMx",param);
			for (int k = 0; k < listMaoxqmxCLV.size(); k++) {
				Maoxqmx maoxqmx = listMaoxqmxCLV.get(k);
				mapMaoxqmxCLV.put(maoxqmx.getXuqrq(), maoxqmx.getXuqsl());
			}
			
			//DIS毛需求明细
			List<Maoxqmx> listMaoxqmxDIS = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryDISMaoxqMx",param);
			for (int k = 0; k < listMaoxqmxDIS.size(); k++) {
				Maoxqmx maoxqmx = listMaoxqmxDIS.get(k);
				mapMaoxqmxDIS.put(maoxqmx.getXuqz(), maoxqmx.getXuqsl());
			}
			
			//DIP毛需求明细
			List<Maoxqmx> listMaoxqmxDIP = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryDIPMaoxqMx",param);
			for (int k = 0; k < listMaoxqmxDIP.size(); k++) {
				Maoxqmx maoxqmx = listMaoxqmxDIP.get(k);
				mapMaoxqmxDIP.put(maoxqmx.getXuqsszq(), maoxqmx.getXuqsl());
			}
			
			//获取零件信息
			Lingj lingj = maoxqReportParam.lingjMap.get(maoxq.getUsercenter()+maoxq.getLingjbh());
			if(lingj == null){
				continue;
			}
			//获取零件供应商信息集合
			List<LingjGongys> listLjgys = maoxqReportParam.lingjgysMap.get(maoxq.getUsercenter()+maoxq.getLingjbh());
			if(listLjgys == null){
				continue;
			}
			for (int j = 0; j < listLjgys.size(); j++) {
				LingjGongys lingjGongys = listLjgys.get(j);//零件供应商信息
				RepMaoxq repMaoxq = new RepMaoxq();//毛需求报表信息
				if(Const.ZHIZAOLUXIAN_IL.equals(maoxq.getZhizlx())){
					repMaoxq.setLeix("1");//IL
				}else{
					repMaoxq.setLeix("2");//KD
				}
				repMaoxq.setId(getUUID());//ID
				repMaoxq.setCreator(username);//创建人
				repMaoxq.setCreate_time(CommonFun.getJavaTime());//创建时间
				repMaoxq.setUsercenter(maoxq.getUsercenter());//用户中心
				repMaoxq.setLingjbh(maoxq.getLingjbh());//零件编号
				repMaoxq.setLingjmc(lingj.getZhongwmc());//零件名称
				repMaoxq.setJihy(lingj.getJihy());//计划员
				repMaoxq.setDanw(maoxq.getDanw());//单位
				repMaoxq.setDinghlx(maoxq.getZhizlx());//制造路线
				repMaoxq.setChanx(maoxq.getChanx());//产线
				repMaoxq.setGongysdm(lingjGongys.getGongysbh());//供应商代码
				repMaoxq.setGongysmc(lingjGongys.getGongsmc());//供应商名称
				repMaoxq.setGongyfe(lingjGongys.getGongyfe());//供应份额
					
				BigDecimal zhoujf = BigDecimal.ZERO;
				int size = listMaoxqmxCLV.size();
				//14天的需求
				for (int k = 0; k < 14; k++) {
					PropertyDescriptor pd = new PropertyDescriptor("J" + k, cl);
					BigDecimal xuqsl = BigDecimal.ZERO;//需求数量
					//需求日期
					String date = CommonFun.getDayTime(today, k,"yyyy-MM-dd");
					//如果还有CLV的需求,则取CLV需求数量
					if(k < size){
						//需求数量为毛需求数量
						xuqsl = CommonFun.getBigDecimal(mapMaoxqmxCLV.get(date));
					}else{//CLV毛需求不够,则采用周毛需求进行均分
						param.put("riq", date);
						//查询年周序
						CalendarCenter calendar = maoxqReportParam.calendarVersionMap.get(maoxq.getUsercenter() + rilbc + date);
						if(calendar == null){
							continue;
						}
						//是否工作日,工作日计算需求,非工作日则取0
						if("1".equals(calendar.getShifgzr())){
							if(zhoujf.signum() <= 0){//取周均分
								param.put("nianzx", calendar.getNianzx());//年周序
								//查询周序内工作天数
								BigDecimal gongzrSl = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("report.queryZxGongzrSlBanc",param));
								//查询周需求数量
								BigDecimal shul = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("report.queryDISMaoxqMxSl",param));//DIS毛需求明细
								//如果工作日天数大于0,则均分需求数量
								if(gongzrSl.signum() > 0){
									zhoujf = shul.divide(gongzrSl,0, BigDecimal.ROUND_CEILING);//周均分=数量/工作天数
								}
							}
							xuqsl = zhoujf;//需求数量 = 周均分
						}
					}
					pd.getWriteMethod().invoke(repMaoxq, xuqsl.multiply(lingjGongys.getGongyfe()));//设置需求数量
				}
				
				size = listMaoxqmxDIS.size();
				BigDecimal yuejf = BigDecimal.ZERO;//月均分
				param.put("today", today);
				//5周的需求
				for (int k = 0; k < 5; k++) {
					PropertyDescriptor pd = new PropertyDescriptor("S" + k, cl);
					BigDecimal xuqsl = BigDecimal.ZERO;//需求数量
					param.put("num", String.valueOf(k+3));
					//查询需求周
					List<String> xuqzs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryNextNianzxBanc",param);
					if(xuqzs == null || xuqzs.isEmpty()){
						continue;
					}
					String xuqz = xuqzs.get(xuqzs.size() - 1);//需求周
					//如果还有DIS的需求,则取DIS需求数量
					if(k < size){
						//需求数量为毛需求数量
						xuqsl = CommonFun.getBigDecimal(mapMaoxqmxDIS.get(xuqz));
					}else{//DIS毛需求不够,则采用周期毛需求进行均分
						param.put("xuqz", xuqz);
						//查询年周期
						String nianzq = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("report.queryNianzqBanc",param));
						if(yuejf.signum() <= 0){//取周均分
							param.put("nianzq", nianzq);//年周序
							//查询周期内工作天数
							BigDecimal gongzrSl = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("report.queryZqGongzrSlBanc",param));
							//查询周期需求数量
							BigDecimal shul = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("report.queryDIPMaoxqMxSl",param));//DIP毛需求明细
							//如果工作日天数大于0,则均分需求数量
							if(gongzrSl.signum() > 0){
								yuejf = shul.divide(gongzrSl,0, BigDecimal.ROUND_CEILING);//周均分=数量/工作天数
							}
							xuqsl = yuejf;//需求数量 = 周均分
						}
					}
					pd.getWriteMethod().invoke(repMaoxq, xuqsl.multiply(lingjGongys.getGongyfe()));//设置需求数量
				}
				
				size = listMaoxqmxDIP.size();
				//12个月的需求
				for (int k = 0; k < 12; k++) {
					PropertyDescriptor pd = new PropertyDescriptor("P" + k, cl);
					BigDecimal xuqsl = BigDecimal.ZERO;//需求数量
					param.put("num", String.valueOf(k+1));
					//查询年周期
					List<String> listNianzq = (List<String>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryNianzqByRiq",param);
					if(k < listNianzq.size()){
						String nianzq = listNianzq.get(k);
						//需求数量为毛需求数量
						xuqsl = CommonFun.getBigDecimal(mapMaoxqmxDIP.get(nianzq));
					}
					pd.getWriteMethod().invoke(repMaoxq, xuqsl.multiply(lingjGongys.getGongyfe()));//设置需求数量
				}
				listRepMaoxq.add(repMaoxq);
			}
		}
		return listRepMaoxq;
	}
	
	/**
	 * kd毛需求分析
	 * @param listMaoxq 毛需求信息
	 * @param param 查询参数
	 * @param today 当前日期
	 * @return 毛需求分析结果
	 * @throws Exception
	 */
	public List<RepMaoxq> kdMaoxqFenx(List<Maoxqmx> listMaoxq,Map<String,String> param,String today,String username) throws Exception{
		List<RepMaoxq> listRepMaoxq = new ArrayList<RepMaoxq>();
		Class cl = RepMaoxq.class; 
		for (int i = 0; i < listMaoxq.size(); i++) {
			Maoxqmx maoxq = listMaoxq.get(i);
			param.put("lingjbh", maoxq.getLingjbh());//零件编号
			param.put("usercenter", maoxq.getUsercenter());//用户中心
			//获取零件信息
			Lingj lingj = maoxqReportParam.lingjMap.get(maoxq.getUsercenter()+maoxq.getLingjbh());
			if(lingj == null){
				continue;
			}
			
			Map<String,BigDecimal> mapMaoxqmxCLV = new HashMap<String, BigDecimal>();
			Map<String,BigDecimal> mapMaoxqmxDKS = new HashMap<String, BigDecimal>();
			Map<String,BigDecimal> mapMaoxqmxZQ = new HashMap<String, BigDecimal>();
			//CLV毛需求明细
			List<Maoxqmx> listMaoxqmxCLV = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryCLVMaoxqMx",maoxq);
			for (int k = 0; k < listMaoxqmxCLV.size(); k++) {
				Maoxqmx maoxqmx = listMaoxqmxCLV.get(k);
				mapMaoxqmxCLV.put(maoxqmx.getXuqrq(), maoxqmx.getXuqsl());
			}
			
			//DKS毛需求明细
			List<Maoxqmx> listMaoxqmxDKS = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryDKSMaoxqMx",maoxq);
			for (int k = 0; k < listMaoxqmxDKS.size(); k++) {
				Maoxqmx maoxqmx = listMaoxqmxDKS.get(k);
				mapMaoxqmxDKS.put(maoxqmx.getXuqz(), maoxqmx.getXuqsl());
			}
			
			//DKS毛需求明细
			for (int k = 0; k < listMaoxqmxDKS.size(); k++) {
				Maoxqmx maoxqmx = listMaoxqmxDKS.get(k);
				mapMaoxqmxZQ.put(maoxqmx.getXuqsszq(), CommonFun.getBigDecimal(mapMaoxqmxDKS.get(maoxqmx.getXuqsszq())).add(maoxqmx.getXuqsl()));
			}
			
			//获取零件供应商信息集合
			List<LingjGongys> listLjgys = maoxqReportParam.lingjgysMap.get(maoxq.getUsercenter()+maoxq.getLingjbh());
			if(listLjgys == null){
				continue;
			}
			for (int j = 0; j < listLjgys.size(); j++) {
				LingjGongys lingjGongys = listLjgys.get(j);//零件供应商信息
				RepMaoxq repMaoxq = new RepMaoxq();//毛需求报表信息
				repMaoxq.setId(getUUID());//ID
				if(Const.ZHIZAOLUXIAN_IL.equals(maoxq.getZhizlx())){
					repMaoxq.setLeix("1");//IL
				}else{
					repMaoxq.setLeix("2");//KD
				}
				repMaoxq.setUsercenter(maoxq.getUsercenter());//用户中心
				repMaoxq.setLingjbh(maoxq.getLingjbh());//零件编号
				repMaoxq.setLingjmc(lingj.getZhongwmc());//零件名称
				repMaoxq.setJihy(lingj.getJihy());//计划员
				repMaoxq.setCreator(username);//创建人
				repMaoxq.setCreate_time(CommonFun.getJavaTime());//创建时间
				repMaoxq.setDanw(maoxq.getDanw());//单位
				repMaoxq.setDinghlx(maoxq.getZhizlx());//制造路线
				repMaoxq.setChanx(maoxq.getChanx());//产线
				repMaoxq.setGongysdm(lingjGongys.getGongysbh());//供应商代码
				repMaoxq.setGongysmc(lingjGongys.getGongsmc());//供应商名称
				repMaoxq.setGongyfe(lingjGongys.getGongyfe());//供应份额
				
				int size = listMaoxqmxCLV.size();
				BigDecimal zhoujf = BigDecimal.ZERO;
				//14天的需求
				for (int k = 0; k < 14; k++) {
					PropertyDescriptor pd = new PropertyDescriptor("J" + k, cl);
					BigDecimal xuqsl = BigDecimal.ZERO;//需求数量
					//需求日期
					String date = CommonFun.getDayTime(today, k,"yyyy-MM-dd");
					//如果还有CLV的需求,则取CLV需求数量
					if(k < size){
						//需求数量为毛需求数量
						xuqsl = CommonFun.getBigDecimal(mapMaoxqmxCLV.get(date));
					}else{//CLV毛需求不够,则采用周毛需求进行均分
						param.put("riq", date);
						//查询年周序
						CalendarCenter calendar = maoxqReportParam.calendarVersionMap.get(maoxq.getUsercenter() + date);
						if(calendar == null){
							continue;
						}
						//是否工作日,工作日计算需求,非工作日则取0
						if("1".equals(calendar.getShifgzr())){
							if(zhoujf.signum() <= 0){//取周均分
								param.put("nianzx", calendar.getNianzx());//年周序
								//查询周序内工作天数
								BigDecimal gongzrSl = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("report.queryZxGongzrSl",param));
								//查询周需求数量
								BigDecimal shul = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("report.queryDKSMaoxqMxSl",param));//DIS毛需求明细
								//如果工作日天数大于0,则均分需求数量
								if(gongzrSl.signum() > 0){
									zhoujf = shul.divide(gongzrSl,0, BigDecimal.ROUND_CEILING);//周均分=数量/工作天数
								}
							}
							xuqsl = zhoujf;//需求数量 = 周均分
						}
					}
					pd.getWriteMethod().invoke(repMaoxq, xuqsl.multiply(lingjGongys.getGongyfe()));//设置需求数量
				}
				
				size = listMaoxqmxDKS.size();
				param.put("today", today);
				//5周的需求
				for (int k = 0; k < 5; k++) {
					PropertyDescriptor pd = new PropertyDescriptor("S" + k, cl);
					BigDecimal xuqsl = BigDecimal.ZERO;//需求数量
					param.put("num", String.valueOf(k+3));
					//查询需求周
					List<String> xuqzs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryNextNianzx",param);
					if(xuqzs == null || xuqzs.isEmpty()){
						continue;
					}
					String xuqz = xuqzs.get(xuqzs.size() - 1);//需求周
					//如果还有DKS的需求,则取DKS需求数量
					if(k < size){
						//需求数量为毛需求数量
						xuqsl = CommonFun.getBigDecimal(mapMaoxqmxDKS.get(xuqz));
					}
					pd.getWriteMethod().invoke(repMaoxq, xuqsl.multiply(lingjGongys.getGongyfe()));//设置需求数量
				}
				
				size = listMaoxqmxDKS.size();
				//12个月的需求
				for (int k = 0; k < 12; k++) {
					PropertyDescriptor pd = new PropertyDescriptor("P" + k, cl);
					BigDecimal xuqsl = BigDecimal.ZERO;//需求数量
					param.put("num", String.valueOf(k+1));
					//查询年周期
					List<String> listNianzq = (List<String>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryNianzqByRiq",param);
					if(k < listNianzq.size()){
						String nianzq = listNianzq.get(k);
						//需求数量为毛需求数量
						xuqsl = CommonFun.getBigDecimal(mapMaoxqmxZQ.get(nianzq));
					}
					pd.getWriteMethod().invoke(repMaoxq, xuqsl.multiply(lingjGongys.getGongyfe()));//设置需求数量
				}
				listRepMaoxq.add(repMaoxq);
			}
		}
		return listRepMaoxq;
	}
	
}
