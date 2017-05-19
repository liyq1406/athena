package com.athena.ckx.module.xuqjs.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.Ticxxsj;
import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 工作时间模板
 * 
 * @author hj
 * @date 2014-2-19
 */
@Component
public class TicxxsjGzsjmbService extends BaseService<Ticxxsj> {
	protected static Logger logger = Logger.getLogger(TicxxsjGzsjmbService.class);	//定义日志方法
	/**
	 * 获得命名空间
	 * 
	 * @author hj
     * @date 2014-2-19
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 工作时间模板
	 * @author hj
	 * @date 2014-2-19
	 * @param year
	 * @param username
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String calculate(String userId) throws ServiceException {
		logger.info("--工作时间模板计算开始");
		Usercenter usercenter = new Usercenter();
		usercenter.setBiaos("1");
		List<Usercenter> list =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryUsercenter",usercenter);
//		2.1 循环用户中心
		for (final Usercenter user : list)
		{
//			2.2 根据用户中心插入表
			calculateGongzsjmb(user.getUsercenter());
			logger.info("用户中心："+user.getUsercenter()+" 工作时间模板计算成功");
		}
		logger.info("---工作时间模板计算成功");
		addTicxxsjTemp();
		return "success";
	}
	
	
	@Transactional
	@SuppressWarnings("unchecked")
	public String calculateGongzsjmb(String usercenter){
		//删除多余的数据
		removeGzsjmb(usercenter);
		//计算新的剔除休息时间模板的数据
		List<Ticxxsj> list =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryAddByUsercenter",usercenter);
		for (Ticxxsj ticxxsj : list)
		{
			insertGongzsjmb(ticxxsj);
		}
		return usercenter;
	}
	/**
	 * 将剔除休息时间表数据写入到临时表中
	 */
	public void addTicxxsjTemp()
	{
		//清空剔除休息时间临时表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.truncateTicxxsjTemp");
		//将剔除休息时间表数据写入到临时表中
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.addTicxxsjTemp");
	}
	/**
	 * 根据对比，将数据计算后写入工作时间模板中
	 * @param bean
	 */
	@SuppressWarnings("unchecked")
	public void insertGongzsjmb(Ticxxsj bean)
	{
		//根据用户中心+产线，查询所有日期的未来几日剔除休息时间 ，需要按照工作日、顺序号排序（升序）
		List<Ticxxsj> listBean =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryTicxxsjByGzsjmb",bean);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		int i = getSumShijcd(listBean.get(0)) , temp = i;
		for (Ticxxsj ticxxsj : listBean) 
		{
			temp += ticxxsj.getShijcd();
			int j=0;
			for (; i < temp; i++)
			{					
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("USERCENTER", ticxxsj.getUsercenter());
				map.put("CHANX", ticxxsj.getChanxck());
				map.put("GONGZR", ticxxsj.getGongzr());
				map.put("XIAOHSJ", i);
				Date date = DateTimeUtil.zidyParse(ticxxsj.getRiq()+" "+ticxxsj.getShijdkssj(), "yyyy-MM-dd HH:mm", GetMessageByKey.getMessage("riqgszhcw"));
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");					
				//绝对时刻：日期+开始时间段+时间序号（分钟，需要转换成毫秒）
				map.put("JUEDSK",sf.format(new Date(date.getTime()+j*1000*60)));					
				list.add(map);
				j++;
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.mergeGongzsjmb",map);
//				数据满1000 ，则插入到数据库中
				if(1000 < list.size())
				{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_ckx.insertGongzsjmb",list);
					list.clear();
				}
			}
		}
		if(0 < list.size())
		{
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_ckx.mergeGongzsjmb",list);
			list.clear();
		}
	} 
	/**
	 * 将临时表中存在未来几日剔除休息时间表中不存在的数据在工作时间模板中删掉
	 * @param usercenter
	 */
	@SuppressWarnings("unchecked")
	public void removeGzsjmb(String usercenter){
		List<Ticxxsj> list =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryRomoveByUsercenter",usercenter);
		for (Ticxxsj ticxxsj : list) 
		{
			//如果从序号1开始不存在新计算的未来几日剔除休息时间，则直接删除掉该日期的工作时间模板
			if(ticxxsj.getShunxh() == 1)
			{
				ticxxsj.setShunxh(null);
			}
			else
			{
				ticxxsj.setShunxh(getSumShijcd(ticxxsj));
			}
		}
		if(list.size() > 0){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_ckx.removeGongzsjmb",list);
		}
	}
	/**
	 * 根据序号获取对应的工作时间模板的消耗时间
	 * @param ticxxsj
	 * @return
	 */
	public Integer getSumShijcd(Ticxxsj ticxxsj)
	{
		Object obj= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getSumSjcd",ticxxsj);
		return Integer.valueOf(obj.toString());
	}
	
	/**
	 * 将数据插入到剔除休息时间-合并时间  表中
	 * @param userID  接口号
	 * @return
	 */

	public String addTicxxsjTemp(String userID){
		
		//清除剔除休息时间-合并时间  表数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.removeTicxxsjTemp");
		//将数据插入到剔除休息时间-合并时间  表中
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.addTicxxsjTemp",userID);
		//将数据插入到剔除休息时间-合并时间  表中   有未来编组号
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.addTicxxsjTempWeilbzh",userID);
		return "success";
	}
	/**
	 * 剔除休息时间-时间合并  操作
	 * @param userID
	 * @return
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public String addGongzsjmb(String userID){
		try {
			addTicxxsjTemp(userID);
		} catch (Exception e) {
			logger.error(userID+"批量，剔除休息时间-时间合并操作出错"+e.toString());
			throw new ServiceException(e.toString());
		}
		try {
			//清除工作时间模板-合并时间 表数据
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteGongzsjmb");
			List<String> list =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryHebtimeh");
			for (String str : list) {
				insert(userID,str);
				logger.info("---合并时间号"+str+"：计算成功");
			}
		} catch (Exception e) {
			logger.error(userID+"批量，工作时间模板-合并时间操作出错"+e.toString());
			throw new ServiceException(e.toString());
		}
		return "success";
	}
	
	@SuppressWarnings("unchecked")
	public String insert(String userID,String hebtimeh){
		List<Map<String,Object>> listInfo =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryHebtimehInfo",hebtimeh);
		List<Map<String,String>> listGongzsjmb = new ArrayList<Map<String,String>>();
		String gongzr = "";
		int count = 0 ,tempCount = 0;
		for (Map<String, Object> map : listInfo) {
			if(!gongzr.equals(map.get("GONGZR").toString())){
				count = 0;
				tempCount = 0;
				gongzr = map.get("GONGZR").toString();
			}
			BigDecimal s = (BigDecimal)map.get("SHIJCD");
			tempCount += s.intValue();
			int j = 0;
			 for(;count < tempCount ;){
			    Map<String,String> mapBean = new HashMap<String, String>();
			    mapBean.put("HEBTIMEH", map.get("HEBTIMEH").toString());
				mapBean.put("GONGZR",   map.get("GONGZR").toString());
				mapBean.put("XIAOHSJ",  count+"");
				Date date = DateTimeUtil.zidyParse(map.get("RIQ")+" "+map.get("SHIJDKSSJ"), "yyyy-MM-dd HH:mm", GetMessageByKey.getMessage("riqgszhcw"));
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");					
				//绝对时刻：日期+开始时间段+时间序号（分钟，需要转换成毫秒）
				mapBean.put("JUEDSK",sf.format(new Date(date.getTime()+j*1000*60)));	
				mapBean.put("CREATOR",  userID);		
				listGongzsjmb.add(mapBean);
				j++;count++;
				if(listGongzsjmb.size() > 180){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_ckx.addGongzsjmb",listGongzsjmb);
					listGongzsjmb.clear();
				}
			}
		}
		if(listGongzsjmb.size() > 0){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_ckx.addGongzsjmb",listGongzsjmb);
			listGongzsjmb.clear();
		}
		return "success";
	}
}
