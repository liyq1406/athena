package com.athena.xqjs.module.report.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.CalendarVersion;
import com.athena.xqjs.entity.common.Gongyzq;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.report.Kucfx;
import com.athena.xqjs.module.common.service.CalendarGroupService;
import com.athena.xqjs.module.common.service.CalendarVersionService;
import com.athena.xqjs.module.ilorder.service.GongyzqService;
import com.athena.xqjs.module.ilorder.service.ZiykzbService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.support.PageableSupport;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 
 * @author wuyichao
 * @see  库存分析服务
 */
@Component
public class KucfxService extends BaseService 
{

	
	@Inject
	private CalendarVersionService calendarVersionService;
	
	@Inject
	private CalendarGroupService calendarGroupService;
	
	@Inject
	private GongyzqService gongyzqService;
	
	@Inject
	private ZiykzbService ziykzbService;
	
	
	public Map queryIlKucfxByPage(Map<String, String> paramMap,PageableSupport pageBean) 
	{
		paramMap = this.translateParam(paramMap);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("report.queryIlKucfx", paramMap,pageBean);
	}

	public Map queryKdKucfxByPage(Map<String, String> paramMap,PageableSupport pageBean)
	{
		paramMap = this.translateParam(paramMap);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("report.queryKdKucfx", paramMap,pageBean);
	}
	
	public Map queryBjKucfxByPage(Map<String, String> paramMap,PageableSupport pageBean)
	{
		
		paramMap = this.translateParam(paramMap);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("report.queryBjKucfx", paramMap,pageBean);
	}
	
	public List<Kucfx> queryIlKucfx(Map<String, String> paramMap)
	{
		paramMap = this.translateParam(paramMap);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryIlKucfx", paramMap);
	}
	
	public List<Kucfx> queryKdKucfx(Map<String, String> paramMap)
	{
		paramMap = this.translateParam(paramMap);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryKdKucfx", paramMap);
	}
	
	public List<Kucfx> queryBjKucfx(Map<String, String> paramMap)
	{
		paramMap = this.translateParam(paramMap);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryBjKucfx", paramMap);
	}
	
	/**
	 * @see   转换查询参数
	 * @param paramMap
	 * @return
	 */
	private Map<String, String> translateParam(Map<String, String> paramMap)
	{
		if(null != paramMap)
		{
			String xuqbc = paramMap.get("qxuqbc");
			if(StringUtils.isNotBlank(xuqbc))
			{
				String[] flag = xuqbc.split(",");
				xuqbc = "";
				for(int i=0;i<flag.length;i++)
				{
					xuqbc += "'" + flag[i] + "'";
					if(i != flag.length -1)
					{
						xuqbc += ',';
					}
				}
				paramMap.put("qxuqbc", xuqbc);
			}
		}
		return paramMap;
	}
	
	
	private Map<String, String> translateParam(ArrayList<Maoxq> bancs,Map<String, String> paramMap)
	{
		if(null != paramMap)
		{
			String xuqbc = "";
			for(int i=0,j=bancs.size();i<j;i++)
			{
				xuqbc += "'" + bancs.get(i).getXuqbc() + "'";
				if(i != j -1)
				{
					xuqbc += ',';
				}
			}
			if(StringUtils.isNotBlank(xuqbc))
			{
				paramMap.put("xuqbc", xuqbc);
			}
		}
		return paramMap;
	}

	/**
	 * @see 国产件库存分析 
	 * @param bancs
	 * @param searchMap
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public Object calculateIl(ArrayList<Maoxq> bancs, Map<String, String> searchMap) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		//查看工业周期
		Gongyzq gongyzq = gongyzqService.queryGongyzqbyRq(searchMap.get("jsrq"));
		List<Kucfx> aggregations = null;
		List<Kucfx> lingjs = null;
		List<Kucfx> kucs = null;
		List<Kucfx> riqs = null;
		Map<String, Kucfx> kucMap = null;
		Map<String, Kucfx> lingjMap = null;
		Map<String, Kucfx> riqMap = null;
		boolean flagNoClv = true;
		//查看是否为CLV毛需求
		if(null != bancs && bancs.size() > 0 && null != gongyzq)
		{
			//清空历史数据
			truncateKucfx("IL");
			searchMap.put("xuqsszq",gongyzq.getGongyzq());
			Maoxq maoxq = bancs.get(0);
			if((maoxq.getXuqbc().substring(0, 3)).equalsIgnoreCase("CLV")) //选取日毛需求
			{
				flagNoClv = false;
				searchMap.put("xuqbc",maoxq.getXuqbc());
				aggregations = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.aggregationILCLV", searchMap);
			}
			else //选取非日毛需求
			{
				searchMap = translateParam(bancs, searchMap);
				searchMap.put("nianzq",gongyzq.getGongyzq());
				aggregations = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.aggregationIL", searchMap);
			}
			if(null != aggregations && aggregations.size() > 0)
			{
				//零件信息
				lingjs = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryLingj", searchMap);
				kucs = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryKuc", searchMap);
				if(flagNoClv) riqs = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryRiqIL", searchMap);
				lingjMap = this.translateListToMap(lingjs, "usercenter","lingjbh");
				riqMap = this.translateListToMap(riqs, "usercenter","cangkdm");
				kucMap = this.translateListToMap(kucs, "usercenter","lingjbh");
				if(null != lingjs) lingjs.clear();
				if(null != kucs) kucs.clear();
				if(null != riqs) riqs.clear();
				//将所有计算的参数信息汇总
				for (Kucfx kucfx : aggregations) {
					if(null != kucMap && !kucMap.isEmpty()) copypropertys(kucfx, kucMap, Arrays.asList("usercenter","lingjbh"), Arrays.asList("cangkdm","kucsl"));
					if(null != riqMap && !riqMap.isEmpty()) copypropertys(kucfx, riqMap, Arrays.asList("usercenter","cangkdm"), Arrays.asList("workts"));
					if(null != lingjMap && !lingjMap.isEmpty()) copypropertys(kucfx, lingjMap, Arrays.asList("usercenter","lingjbh"), Arrays.asList("jihy","shiycj"));
					if(null != kucfx.getWorkts() && kucfx.getWorkts().doubleValue() > 0 )
					{
						//进行CMJ 天数计算
						kucfx.setCmj(kucfx.getXuqsl().divide(kucfx.getWorkts(),0,BigDecimal.ROUND_UP));
						if(null != kucfx.getKucsl() && kucfx.getCmj().doubleValue() > 0 )
							kucfx.setTians(kucfx.getKucsl().divide(kucfx.getCmj(),2,BigDecimal.ROUND_UP));
					}
					kucfx.setCreatetime(new Date());
					kucfx.setCreator(searchMap.get("username"));
				}
				if(null != kucMap) kucMap.clear();
				if(null != riqMap) riqMap.clear();
				if(null != lingjMap) lingjMap.clear();
				insertIntoKucfx(aggregations,"IL");
			}
		}
		return "计算完成";
	}
	
	

	/**
	 * @see   KD件库存分析计算
	 * @param bancs
	 * @param searchMap
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public Object calculateKd(ArrayList<Maoxq> bancs, Map<String, String> searchMap) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		//查看工业周期
		Gongyzq gongyzq = gongyzqService.queryGongyzqbyRq(searchMap.get("jsrq"));
		List<Kucfx> aggregations = null;
		List<Kucfx> lingjs = null;
		List<Kucfx> kucs = null;
		List<Kucfx> riqs = null;
		List<Kucfx> kdKucs = null;
		Map<String, Kucfx> kucMap = null;
		Map<String, Kucfx> lingjMap = null;
		Map<String, Kucfx> riqMap = null;
		Map<String, Kucfx> kdKucMap = null;
		if(null != bancs && bancs.size() > 0 && null != gongyzq)
		{
			//清空历史数据
			truncateKucfx("KD");
			searchMap.put("xuqsszq",gongyzq.getGongyzq());
			searchMap.put("nianzq",gongyzq.getGongyzq());
			searchMap = translateParam(bancs, searchMap);
			searchMap.put("zhizlx", "'97X','97D'");
			aggregations = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.aggregationKD", searchMap);
			if(null != aggregations && aggregations.size() > 0)
			{
				//零件信息
				lingjs = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryLingj", searchMap);
				kucs = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryKucBj", searchMap);
				kdKucs = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryKucKD",searchMap);
				riqs = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryRiqKD", searchMap);
				lingjMap = this.translateListToMap(lingjs, "usercenter","lingjbh");
				riqMap = this.translateListToMap(riqs, "zhizlx"); 
				kucMap = this.translateListToMap(kucs, "usercenter","lingjbh");
				kdKucMap = this.translateListToMap(kdKucs,"usercenter","lingjbh");
				if(null != lingjs) lingjs.clear();
				if(null != kucs) kucs.clear();
				if(null != riqs) riqs.clear();
				if(null != kdKucs) kdKucs.clear();
				//将所有计算的参数信息汇总
					
				for (Kucfx kucfx : aggregations) {
					if(null != kucMap && !kucMap.isEmpty()) copypropertys(kucfx, kucMap, Arrays.asList("usercenter","lingjbh"), Arrays.asList("cangkdm","kucsl"));
					if(null != kdKucMap && !kdKucMap.isEmpty()) copypropertys(kucfx, kdKucMap, Arrays.asList("usercenter","lingjbh"), Arrays.asList("ysgsl","zhongxqsl"));
					if(null != riqMap && !riqMap.isEmpty()) copypropertys(kucfx, riqMap, Arrays.asList("zhizlx"), Arrays.asList("workts"));
					if(null != lingjMap && !lingjMap.isEmpty()) copypropertys(kucfx, lingjMap, Arrays.asList("usercenter","lingjbh"), Arrays.asList("jihy","shiycj"));
					if(null != kucfx.getWorkts() && kucfx.getWorkts().doubleValue() > 0 )
					{
						//进行CMJ 天数计算
						kucfx.setCmj(kucfx.getXuqsl().divide(kucfx.getWorkts(),0,BigDecimal.ROUND_UP));
						BigDecimal flag = BigDecimal.ZERO;
						if(null != kucfx.getKucsl()) flag = flag.add(kucfx.getKucsl());
						if(null != kucfx.getYsgsl()) flag = flag.add(kucfx.getYsgsl());
						if(null != kucfx.getZhongxqsl()) flag = flag.add(kucfx.getZhongxqsl());
						if(kucfx.getCmj().doubleValue() > 0)kucfx.setTians(flag.divide(kucfx.getCmj(),2,BigDecimal.ROUND_UP));
					}
					kucfx.setCreatetime(new Date());
					kucfx.setCreator(searchMap.get("username"));
				}
				if(null != kucMap) kucMap.clear();
				if(null != riqMap) riqMap.clear();
				if(null != lingjMap) lingjMap.clear();
				if(null != kdKucMap) kdKucMap.clear();
				insertIntoKucfx(aggregations,"KD");
			}
		}
		return "计算完成";
	}
	

	public Object calculateBj(ArrayList<Maoxq> bancs, Map<String, String> searchMap) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		//查看工业周期
		Gongyzq gongyzq = gongyzqService.queryGongyzqbyRq(searchMap.get("jsrq"));
		List<Kucfx> aggregations = new ArrayList<Kucfx>();
		List<Kucfx> aggregationsFlags = null;
		List<Kucfx> aggregationsAfter = new ArrayList<Kucfx>();
		List<Kucfx> lingjs = null;
		List<Kucfx> kucs = null;
		List<Kucfx> riqs = null;
		List<Kucfx> kdKucs = null;
		Map<String, Kucfx> kucMap = null;
		Map<String, Kucfx> lingjMap = null;
		Map<String, Kucfx> riqMap = null;
		Map<String, Kucfx> kdKucMap = null;
		Map<String, Kucfx> aggregationsAfterMap = null;
		if(null != bancs && bancs.size() > 0 && null != gongyzq)
		{
			//清空历史数据
			truncateKucfx("BJ");
			
			searchMap = translateParam(bancs, searchMap);
			int works97x = 0 ,works97xAfter = 0 ,works97d = 0 ,works97dAfter = 0,i=0,j=0;
			List<CalendarVersion> after97xs = calendarVersionService.queryCalendarVersionGzrByMax( "NA01AX01", searchMap.get("jsrq"));
			List<CalendarVersion> after97ds = calendarVersionService.queryCalendarVersionGzrByMax( "NA01KD01", searchMap.get("jsrq"));
			
			//聚合计算当前时间所属周期毛需求
			searchMap.put("xuqsszq",gongyzq.getGongyzq());
			searchMap.put("zhizlx", "'97X','97D'");
			aggregations = addAllKucfxs(aggregations, searchMap);
			//查看是否跨周期
			if(null != after97xs && after97xs.size() > 0)
			{
				if(after97xs.size() > 15) //能推15天
				{
					after97xs =	after97xs.subList(0, 15);
				}
				i = 0 ; j = after97xs.size() - 1;
				for (CalendarVersion calendarVersion : after97xs) {
					String flagZq = calendarVersion.getNianzq();
					if(StringUtils.isNotBlank(flagZq))
					{
						i ++;
						if(flagZq.equalsIgnoreCase(gongyzq.getGongyzq()))
						{
							works97x ++;
						}
						else
						{
							works97xAfter ++;
							if(i == j)
							{
								searchMap.put("xuqsszq",flagZq);
								searchMap.put("zhizlx", "'97X'");
								aggregationsAfter = addAllKucfxs(aggregationsAfter, searchMap);
							}
						}
					}
				}
			}
			if(null != after97ds && after97ds.size() > 0)
			{
				
				if(after97ds.size() > 15) //能推15天
				{
					after97ds	= after97ds.subList(0, 15);
				}
				i = 0 ; j = after97ds.size() - 1;
				for (CalendarVersion calendarVersion : after97ds) {
					String flagZq = calendarVersion.getNianzq();
					if(StringUtils.isNotBlank(flagZq))
					{
						i++ ;
						if(flagZq.equalsIgnoreCase(gongyzq.getGongyzq()))
						{
							works97d ++;
						}
						else
						{
							works97dAfter ++;
							if(i == j)
							{
								searchMap.put("xuqsszq",flagZq);
								searchMap.put("zhizlx", "'97D'");
								aggregationsAfter = addAllKucfxs(aggregationsAfter, searchMap);
							}
						}
					}
				}
			}
			
			
			if(null != aggregations && aggregations.size() > 0)
			{
				//零件信息
				lingjs = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryLingj", searchMap);
				kucs = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryKucBj", searchMap);
				kdKucs = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryKucKD",searchMap);
				riqs = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.queryRiqKD", searchMap);
				lingjMap = this.translateListToMap(lingjs, "usercenter","lingjbh");
				riqMap = this.translateListToMap(riqs, "nianzq","zhizlx"); 
				kucMap = this.translateListToMap(kucs, "usercenter","lingjbh");
				kdKucMap = this.translateListToMap(kdKucs,"usercenter","lingjbh");
				aggregationsAfterMap = this.translateListToMap(aggregationsAfter, "usercenter","lingjbh");
				if(null != lingjs) lingjs.clear();
				if(null != kucs) kucs.clear();
				if(null != riqs) riqs.clear();
				if(null != kdKucs) kdKucs.clear();
				if(null != aggregationsAfter) aggregationsAfter.clear();
				//将所有计算的参数信息汇总
				Kucfx flagAfter = null;
				for (Kucfx kucfx : aggregations) {
					if(null != aggregationsAfterMap && !aggregationsAfterMap.isEmpty())
					{
						flagAfter = aggregationsAfterMap.get(kucfx.getUsercenter()+kucfx.getLingjbh());
					}
					if(null != kucMap && !kucMap.isEmpty()) copypropertys(kucfx, kucMap, Arrays.asList("usercenter","lingjbh"), Arrays.asList("cangkdm","kucsl"));
					if(null != kdKucMap && !kdKucMap.isEmpty()) copypropertys(kucfx, kdKucMap, Arrays.asList("usercenter","lingjbh"), Arrays.asList("ysgsl","zhongxqsl"));
					if(null != riqMap && !riqMap.isEmpty()) copypropertys(kucfx, riqMap, Arrays.asList("nianzq","zhizlx"), Arrays.asList("workts"));
					if(null != lingjMap && !lingjMap.isEmpty()) copypropertys(kucfx, lingjMap, Arrays.asList("usercenter","lingjbh"), Arrays.asList("jihy","shiycj"));
					if(null != flagAfter) 
					{
						copypropertys(flagAfter, riqMap, Arrays.asList("nianzq","zhizlx"), Arrays.asList("workts"));
						kucfx.setXuqslAfter(flagAfter.getXuqsl());
						if(null != kucfx.getWorkts() && null != flagAfter.getWorkts())
						{
							kucfx.setWorktsAfter(flagAfter.getWorkts());
							//当前需求总量 /  当前工业周期所有工作天数  * 工作天数+ 后推15天后需求总量  / 后推15天后工业周期所有工作天数  工作天数  /  后推的工作天数
							if(kucfx.getZhizlx().equalsIgnoreCase("97X") && (works97x + works97xAfter) != 0 )
							{
								kucfx.setCmj(kucfx.getXuqsl().divide(kucfx.getWorkts(),0,BigDecimal.ROUND_UP).multiply(new BigDecimal(works97x)).add(flagAfter.getXuqsl().divide(flagAfter.getWorkts(),0,BigDecimal.ROUND_UP).multiply(new BigDecimal(works97xAfter))).divide(new BigDecimal((works97x + works97xAfter)), 0,BigDecimal.ROUND_UP));
							}
							else if(kucfx.getZhizlx().equalsIgnoreCase("97D") && (works97d + works97dAfter) != 0 )
							{
								kucfx.setCmj(kucfx.getXuqsl().divide(kucfx.getWorkts(),0,BigDecimal.ROUND_UP).multiply(new BigDecimal(works97d)).add(flagAfter.getXuqsl().divide(flagAfter.getWorkts(),0,BigDecimal.ROUND_UP).multiply(new BigDecimal(works97dAfter))).divide(new BigDecimal((works97d + works97dAfter)), 0,BigDecimal.ROUND_UP));
							}
						}
					}
					else
					{
						if(null != kucfx.getWorkts() && kucfx.getWorkts().doubleValue() > 0)
						{
								kucfx.setCmj(kucfx.getXuqsl().divide(kucfx.getWorkts(),0,BigDecimal.ROUND_UP));
						}
					}
					if(null != kucfx.getCmj())
					{
						BigDecimal flag = BigDecimal.ZERO;
						if(null != kucfx.getKucsl()) flag = flag.add(kucfx.getKucsl());
						if(null != kucfx.getYsgsl()) flag = flag.add(kucfx.getYsgsl());
						if(null != kucfx.getZhongxqsl()) flag = flag.add(kucfx.getZhongxqsl());
						if(kucfx.getCmj().doubleValue() > 0)
						{
							kucfx.setTians(flag.divide(kucfx.getCmj(),2,BigDecimal.ROUND_UP));
						}
					}
					kucfx.setCreatetime(new Date());
					kucfx.setCreator(searchMap.get("username"));
				}
				if(null != kucMap) kucMap.clear();
				if(null != riqMap) riqMap.clear();
				if(null != lingjMap) lingjMap.clear();
				if(null != kdKucMap) kdKucMap.clear();
				if(null != aggregationsAfterMap) aggregationsAfterMap.clear();
				insertIntoKucfx(aggregations,"BJ");
			}
		}
		return "计算完成";
	}
	
	@Transactional
	private void insertIntoKucfx(List<Kucfx> aggregations,String type) 
	{
		
		if(StringUtils.isNotBlank(type))
		{
			String batch = null;
			if("IL".equalsIgnoreCase(type))
			{
				batch = "report.insertKucfxIL";
			}
			else if("KD".equalsIgnoreCase(type))
			{
				batch = "report.insertKucfxKD";
			}
			else if("BJ".equalsIgnoreCase(type))
			{
				batch = "report.insertKucfxBJ";
			}
			if(StringUtils.isNotBlank(batch))
				this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(batch, aggregations);
		}
	}
	
	@Transactional
	private void truncateKucfx(String type)
	{
		if(StringUtils.isNotBlank(type))
		{
			String truncate = null;
			if("IL".equalsIgnoreCase(type))
			{
				truncate = "report.deleteKucfxIL";
			}
			else if("KD".equalsIgnoreCase(type))
			{
				truncate = "report.deleteKucfxKD";
			}
			else if("BJ".equalsIgnoreCase(type))
			{
				truncate = "report.deleteKucfxBJ";
			}
			if(StringUtils.isNotBlank(truncate))
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(truncate);
		}
	}

	/**
	 * @see 根据参数列得到的值作为Key 将List 转换为Map
	 * @param kucfxs
	 * @param propertys
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private Map<String,Kucfx> translateListToMap(List<Kucfx> kucfxs,String... propertys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Map<String, Kucfx> result = new HashMap<String, Kucfx>();
		Kucfx kucfx = null;
		if(null != kucfxs && kucfxs.size() > 0 && null != propertys && propertys.length > 0)
		{
			for (int i=0,j=kucfxs.size();i<j;i++) 
			{
				kucfx = kucfxs.get(i);
				StringBuffer flagKey = new StringBuffer();
				for (int k = 0,p = propertys.length; k < p; k++)
				{
					flagKey.append(BeanUtils.getProperty(kucfx, propertys[k]));
				}
				result.put(flagKey.toString(), kucfx);
			}
		}
		return result;
	}
	
	
	/**
	 * @see 根据参数Map 的Key值 得到源Bean  将所需信息复制到目标bean
	 * @param target
	 * @param sourceMap
	 * @param key
	 * @param propertys
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private void copypropertys(Kucfx target,Map<String,Kucfx> sourceMap,List<String> key,List<String> propertys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		if(null != sourceMap && null != target)
		{
			if(null != key && key.size() > 0)
			{
				StringBuffer flagKey = new StringBuffer();
				for (int k = 0,p = key.size(); k < p; k++)
				{
					flagKey.append(BeanUtils.getProperty(target, key.get(k)));
				}
				Kucfx source = sourceMap.get(flagKey.toString());
				if(null != source)
				{
					if(null != propertys && propertys.size() > 0)
					{
						for (String prop : propertys) 
						{
							Object property = BeanUtils.getProperty(source,prop);
							if(null != property)
								BeanUtils.setProperty(target, prop, property);
						}
					}
					else
					{
						//BeanUtils.copyProperties(target, source);
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private List<Kucfx> addAllKucfxs(List<Kucfx> kucfxs,Map<String, String> searchMap)
	{
		List<Kucfx> aggregationsFlags =  this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("report.aggregationKD", searchMap);
		if(null != aggregationsFlags && aggregationsFlags.size() > 0)
		{
			kucfxs.addAll(aggregationsFlags);
		}
		return kucfxs;
	}
	
	
}
