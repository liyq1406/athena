package com.athena.ckx.module.transTime.service;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.transTime.CkxYunssjMb;
import com.athena.ckx.entity.xuqjs.Ticxxsj;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;



/**
 *  用与运输时刻表的时间归集
 *  0008252  运输时刻算法改变
 * @author hj
 *
 */
public class TransTimeTreadService {

	/** 数据库连接 */
	protected AbstractIBatisDao baseDao;
	
	/**
	 * 单个卸货站台对应的未来几日剔除休息时间的工作日的集合
	 */
	protected Map<String , List<Ticxxsj>> mapTicxxsj ;
	/**
	 *  单个卸货站台对应的 所有承运商的集合 
	 */
	protected Map<String , List<CkxYunssjMb>> mapBean;
	
	//记录 工作日为key，工作日对应的剔除休息时间为Value
	private Map<String , List<Ticxxsj>> map = new HashMap<String , List<Ticxxsj>>();
	//记录工作日的集合
	private List<String> listGongzr = new ArrayList<String>();
	/**
	 * 构造方法
	 * @param baseDao 数据库连接
	 */
	public TransTimeTreadService(AbstractIBatisDao baseDao , Map<String , List<CkxYunssjMb>> mapBean , Map<String , List<Ticxxsj>> mapTicxxsj , List<String> listGongzr){
		this.baseDao=baseDao;
		this.mapBean = mapBean;
		this.mapTicxxsj = mapTicxxsj ;
		this.listGongzr = listGongzr;
	}

	public void execute(String creator) {
		try {
			this.compTransTime(creator);
		} catch (Exception e) {
			throw new RuntimeException (e);
		}
	}

	/**
	 * 
	 * 计算一个卸货站台，所有承运商对应的运输时刻 
	 * @return
	 */
	private void compTransTime(String creator){
		// 用于存储插入表的对象
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		for (String key: mapBean.keySet()) {
			int j = 0 ;//用于判断是否是第一个工作日，跳过计算第一个工作日 
			List<CkxYunssjMb> listGcbhAllXuh = mapBean.get(key);
			for (String gongzrKey : listGongzr) {
				List<Ticxxsj> listTicxxsj = mapTicxxsj.get(gongzrKey);
				map.put(gongzrKey , listTicxxsj);
				j++;
				if( j == 1){
					continue ; //如果是第一个工作日，则跳过
				}
			for (CkxYunssjMb ckxYunssjMb : listGcbhAllXuh) {
					Map<String, Object> mapYunssk = new HashMap<String, Object>();
					mapYunssk.put("usercenter", ckxYunssjMb.getUsercenter());
					mapYunssk.put("xiehztbh", ckxYunssjMb.getXiehztbh());
					mapYunssk.put("gcbh", ckxYunssjMb.getGcbh());
					mapYunssk.put("xuh",getXuh(ckxYunssjMb.getXuh(),gongzrKey));
					// 获取具体的到货时间
					Date daohsjTime  =  getShijByTime( gongzrKey , ckxYunssjMb.getDaohsj().intValue());
					//如果上线偏移时间超出上班结束时间，则从此趟次开始，后续趟次全部删除
					if (daohsjTime.getTime() == 0){
						break;
					}
					mapYunssk.put("daohsj",	daohsjTime);
					Date facsjTime  =  getShijByTime( gongzrKey , ckxYunssjMb.getFacsj().intValue());
					// 获取具体的发出时间
					mapYunssk.put("facsj",facsjTime);
					mapYunssk.put("creator", creator);
					mapYunssk.put("create_time", new Date());
					mapYunssk.put("editor", creator);
					mapYunssk.put("edit_time", new Date());
					listMap.add(mapYunssk);
				}
			}
		}
		//批量插入
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("transTime.insertCkxYunsskEffect" ,listMap);//增加数据库
		listMap.clear();
	}
	
	/* xss-0011489
	 * 运输时间-手工计算
	 * */
	public void execute2(String creator) {
		try {
			this.compTransTime2(creator);
		} catch (Exception e) {
			throw new RuntimeException (e);
		}
	}
	
	/**xss-0011489
	 * 运输时间-手工计算
	 * 计算一个卸货站台，所有承运商对应的运输时刻 
	 * @return
	 */
	private void compTransTime2(String creator){
		// 用于存储插入表的对象
		List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
		for (String key: mapBean.keySet()) {
			int j = 0 ;//用于判断是否是第一个工作日，跳过计算第一个工作日 
			List<CkxYunssjMb> listGcbhAllXuh = mapBean.get(key);
			for (String gongzrKey : listGongzr) {
				List<Ticxxsj> listTicxxsj = mapTicxxsj.get(gongzrKey);
				map.put(gongzrKey , listTicxxsj);
				j++;
				if( j == 1){
					continue ; //如果是第一个工作日，则跳过
				}
			for (CkxYunssjMb ckxYunssjMb : listGcbhAllXuh) {
					Map<String, Object> mapYunssk = new HashMap<String, Object>();
					mapYunssk.put("usercenter", ckxYunssjMb.getUsercenter());
					mapYunssk.put("xiehztbh", ckxYunssjMb.getXiehztbh());
					mapYunssk.put("gcbh", ckxYunssjMb.getGcbh());
					mapYunssk.put("xuh",getXuh(ckxYunssjMb.getXuh(),gongzrKey));
					// 获取具体的到货时间
					Date daohsjTime  =  getShijByTime( gongzrKey , ckxYunssjMb.getDaohsj().intValue());
					//如果上线偏移时间超出上班结束时间，则从此趟次开始，后续趟次全部删除
					if (daohsjTime.getTime() == 0){
						break;
					}
					mapYunssk.put("daohsj",	daohsjTime);
					Date facsjTime  =  getShijByTime( gongzrKey , ckxYunssjMb.getFacsj().intValue());
					// 获取具体的发出时间
					mapYunssk.put("facsj",facsjTime);
					mapYunssk.put("creator", creator);
					mapYunssk.put("create_time", new Date());
					mapYunssk.put("editor", creator);
					mapYunssk.put("edit_time", new Date());
					listMap.add(mapYunssk);
				}
			}
		}
		//批量插入
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("transTime.insertCkxYunsskTemp" ,listMap);//增加数据库
		listMap.clear();
	}
	
	
	
	/**
	 * 
	 * 时刻list加上分钟得到新的时刻
	 * @param list 一天的工作时刻表
	 * @param pysj 偏移时间
	 * @author huj
	 * @date 2013-8-31
	 * @return 新的时刻
	 */
	public Date getShijByTime(String riq,int pysj){
		//运算得出的时间
		String timeStr = "";
		//用于计算的对应的工作日的工作时间段
		List<Ticxxsj> sjList = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		if(pysj < 0 ){//如果为负数 ，则取前一个工作日的工作时间段推算
			//循环未来几日剔除休息时间的工作日的集合
			for(int i = 1 ;i < listGongzr.size() ; i++ ){
				String gzr = listGongzr.get(i);
				if(gzr.equals(riq)){//找到当前计算的日期，推算出前一个工作日
					riq = listGongzr.get(i-1) ;
					break ; 
				}
			}		
			//根据前一个工作日 取出对应的工作时间关联
			sjList =  map.get(riq) == null ? new ArrayList<Ticxxsj>():map.get(riq);
			//如果超出前一个工作日的开班时间，则为该工作日的开班时间
			timeStr = getReductionTime(sjList,pysj);
		}else{//如果为正数 ，则时间往下推算
			//根据工作日获取工作时间段
			sjList = map.get(riq);
			//根据偏移时间算出具体工作时间，如果超出收班时间，则时间为 January 1, 1970 00:00:00 GMT. 
			timeStr = getPositiveTime(sjList,pysj);
		}
		try {
			//转成时间类型，如果超出收班时间，则时间为 January 1, 1970 00:00:00 GMT. 
			return sdf.parse(timeStr);
		} catch (ParseException e) {
			throw new ServiceException("运输时刻：计算时间偏量 --时间转换异常");
		}
	}
	/**
	 *
	 * 若 偏移时间大于0，则得到该时间
	 * @param gzsjList
	 * @param pysj
	 * @return
	 */
	private String getPositiveTime(List<Ticxxsj> gzsjList,int pysj){
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		//循序循环工作时间段
		for (Ticxxsj ticxxsj : gzsjList) {
			try {
				Date kaissj = sdf.parse(ticxxsj.getRiq()+" "+ticxxsj.getShijdkssj());//开始时间:字符串时间转化为date类型
				Date jiessj = sdf.parse(ticxxsj.getRiq()+" "+ticxxsj.getShijdjssj());//结束时间:字符串时间转化为date类型
				if(pysj <= ticxxsj.getShijcd()){	//偏移时间小于剔除休息时间的工作时间间隔				
					//时间落于该时间段内，把偏移时间转换成毫秒数
					currentTime.setTime(kaissj.getTime()+pysj*60*1000);
				    return  sdf.format(currentTime);
			   }
			   currentTime = jiessj;//若算出的结束时间不在工作时间范围内则为收班时间
			   //偏移时间减去该时间段，过了该时间段，继续下一循环
			   pysj -= ticxxsj.getShijcd();
			} catch (ParseException e) {
				throw new ServiceException(GetMessageByKey.getMessage("shijianzhyc"));
			}
		}
		//如果循环结束，则当前时间为0，用于判断上线偏移时间是否超出下班时间
		currentTime.setTime(0);
		return sdf.format(currentTime);//date类型转化为字符串
	}
	/**
	 *  根据备货时间偏量 得到具体对应时间
	 * 若 偏移时间小于0，则得到该时间
	 * @param gzsjList
	 * @param pysj
	 * @return
	 */
	private String getReductionTime(List<Ticxxsj> gzsjList,int pysj){
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		//逆序循环工作时间段，
		for(int i =gzsjList.size()-1 ; i >= 0 ; i-- ){
			try {
				Ticxxsj ticxxsj = gzsjList.get(i);
				Date kaissj = sdf.parse(ticxxsj.getRiq()+" "+ticxxsj.getShijdkssj());//开始时间:字符串时间转化为date类型
				Date jiessj = sdf.parse(ticxxsj.getRiq()+" "+ticxxsj.getShijdjssj());//结束时间:字符串时间转化为date类型
				int shijd = ticxxsj.getShijcd().intValue();
				if(shijd+pysj >=  0 ){ //偏移时间在上一个日期的时间段里 ，则得到对应的具体时间
					currentTime.setTime(jiessj.getTime()+pysj*60*1000);
					break;
				}else{
					//如果循环结束，则为当前日期的开班时间
					currentTime.setTime(kaissj.getTime());
				}
				pysj += shijd;
			}catch (ParseException e) {
				throw new ServiceException(GetMessageByKey.getMessage("shijianzhyc"));
			}
		}
		return sdf.format(currentTime);//date类型转化为字符串
	}
	/**
	 * 获取日期加序号，如：2012031401
	 * @return
	 */
	private Double getXuh(Double x,String date){
		String num="";
		if(x<10){//不足2位则补0
			num="0"+String.valueOf(x);
		}else{
			num=String.valueOf(x);
		}
		Double xuh = 0.0;
		String dates = date.replace("-", "")+num;
		xuh = Double.parseDouble(dates);
		return xuh;
	}
	
}
