package com.athena.ckx.module.xuqjs.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.CkxXiaohcyssk;
import com.athena.ckx.entity.xuqjs.Ticxxsj;
import com.athena.ckx.entity.xuqjs.Xiaohcmb;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.runner.Command;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 小火车运输时刻线程类
 * @author huj
 * @date 2012-4-10
 * @update denggq 2012-7-31
 */
public class XiaohcYunsskTread extends Command<Object>{
	
	protected static Logger logger = Logger.getLogger(XiaohcYunsskTread.class);	//定义日志方法
	
	protected AbstractIBatisDao baseDao;//操作数据库
	
	private List<Xiaohcmb> mbList;//按小火车归集的模板
	
	private List<Ticxxsj> gongzrList;//某生产线工作时间
	
	private Ticxxsj t;//剔除休息时间实体类
	
	private String userID;//操作人
	//记录 工作日为key，工作日对应的剔除休息时间为Value
	private Map<String , List<Ticxxsj>> map = new HashMap<String , List<Ticxxsj>>();
	//记录工作日的集合
	private List<String> listGongzr = new ArrayList<String>();
	public XiaohcYunsskTread(AbstractIBatisDao baseDao,Ticxxsj t,List<Xiaohcmb> mbList,List<Ticxxsj> gongzrList,String userID){
		this.baseDao = baseDao;
		this.mbList = mbList;
		this.gongzrList = gongzrList;
		this.userID = userID;
		this.t=t;
	}
	
	/**
	 * 线程启动执行的方法
	 * @author huj
	 * @date 2012-4-10
	 */
	@Override
	public void execute() {
		try {
			this.addXiaohcYssk();
		} catch (Exception e) {
			throw new RuntimeException (e);
		}		
	}

	/**
	 * @author huj
	 * @date 2012-4-10
	 * @return
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String addXiaohcYssk() throws ServiceException{
		int j = 0 ;//用于判断是否是第一个工作日，跳过计算第一个工作日 
		List<CkxXiaohcyssk> xiaohcysskList = new ArrayList<CkxXiaohcyssk>();
		for (Ticxxsj ticxxsj : gongzrList) {
			ticxxsj.setUsercenter(t.getUsercenter());	//用户中心
			ticxxsj.setChanxck(t.getChanxck());			//生产线编号
			List<Ticxxsj> sjList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryTicxxsj", ticxxsj);//按照用户中心+生产线+工作日获得当天的工作时间
			//0007569记录 工作日为key，工作日对应的剔除休息时间为Value
			map.put(ticxxsj.getGongzr(), sjList);
			//0007569记录剔除休息时间的工作日的集合
			listGongzr.add(ticxxsj.getGongzr());
			j++ ;
			if(j == 1){ //0007569 如果是第一个工作日，则跳过
				continue ; 
			}			
			int i =1;
			for (Xiaohcmb xiaohcmb : mbList) {
				CkxXiaohcyssk xiaohcyssk = new CkxXiaohcyssk();	//小火车运输时刻实体类
				xiaohcyssk.setUsercenter(xiaohcmb.getUsercenter());
				xiaohcyssk.setShengcxbh(xiaohcmb.getShengcxbh());
				xiaohcyssk.setXiaohcbh(xiaohcmb.getXiaohcbh());
				xiaohcyssk.setRiq(sjList.get(0).getGongzr());//日期(工作日)
				xiaohcyssk.setTangc(i);//趟次
				
				// 0007569 修改推算上线时间的公式
				String shangxsj = getShijByTime(xiaohcyssk.getRiq(),xiaohcmb.getShangxpysj());
				String kaisbhsj = "" ;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					//如果上线偏移时间超出上班结束时间，则从此趟次开始，后续趟次全部删除
					if(formatter.parse(shangxsj).getTime() == 0){
						break;
					}
					kaisbhsj = getShijByTime(xiaohcyssk.getRiq(),xiaohcmb.getShangxpysj()-xiaohcmb.getBeihpysj());
//					boolean flag = formatter.parse(kaisbhsj).before(formatter.parse(shangxsj));
//					//如果备货时间不小于上线时间，则跳过
//					if(!flag){
//						continue;
//					}
				} catch (ParseException e) {
					logger.error(e.getMessage());
				}
				xiaohcyssk.setKaisbhsj(kaisbhsj);//开始备货时间(年月日时分秒)
				xiaohcyssk.setChufsxsj(shangxsj);//出发上线时间(年月日时分秒)
				xiaohcyssk.setCreator(userID);
				xiaohcyssk.setCreate_time(DateTimeUtil.getCurrDateTime());
				xiaohcyssk.setEditor(userID);
				xiaohcyssk.setEdit_time(DateTimeUtil.getCurrDateTime());				
				xiaohcysskList.add(xiaohcyssk);
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxXiaohcyssk" ,xiaohcyssk);//增加数据库			
				i++;
			}
		}
		//批量插入
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_ckx.insertCkxXiaohcyssk" ,xiaohcysskList);//增加数据库
		return "";
	}
	
	/**
	 * 0007569
	 * 时刻list加上分钟得到新的时刻
	 * @param list 一天的工作时刻表
	 * @param pysj 偏移时间
	 * @author huj
	 * @date 2012-4-10
	 * @return 新的时刻
	 */
	private String getShijByTime(String riq,Integer pysj) throws ServiceException{
		String timeStr = "" ;
		int pianysj = pysj.intValue();
		List<Ticxxsj> gzsjList = null;
		if(pianysj < 0){
			//0007569 如果是负数，则取当前日期往前推一个工作日对应的剔除休息时间
			int i = 1;
			for(; i < listGongzr.size() ; i++){
				if(riq.equals(listGongzr.get(i))){
					//取到当前日期对应的坐标，往前取1位
					i--;
					break ; 
				}
			}
			//根据往前推一个的工作日获取对应的剔除休息时间
			String shangcriq = listGongzr.get(i);
			gzsjList = map.get(shangcriq) == null ? new ArrayList<Ticxxsj>():map.get(shangcriq);
			timeStr = getReductionTime(gzsjList,pianysj);
		}else{
			//0007569 如果是正数，则取当前日期对应的循环
			gzsjList = map.get(riq) == null ? new ArrayList<Ticxxsj>():map.get(riq);
			timeStr = getPositiveTime(gzsjList,pianysj);
		}
		return timeStr;
	}
	/**
	 * 0007569
	 * 若 偏移时间大于0，则得到该时间
	 * @param gzsjList
	 * @param pysj
	 * @return
	 */
	private String getPositiveTime(List<Ticxxsj> gzsjList,int pysj){
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		for (Ticxxsj ticxxsj : gzsjList) {
			try {
				Date kaissj = sdf.parse(ticxxsj.getRiq()+" "+ticxxsj.getShijdkssj());//开始时间:字符串时间转化为date类型
				Date jiessj = sdf.parse(ticxxsj.getRiq()+" "+ticxxsj.getShijdjssj());//结束时间:字符串时间转化为date类型
				if(pysj <= ticxxsj.getShijcd()){	//偏移时间小于剔除休息时间的工作时间间隔				
					currentTime.setTime(kaissj.getTime()+pysj*60*1000);
				    return  sdf.format(currentTime);
			   }
			   currentTime = jiessj;//若算出的结束时间不在工作时间范围内则为收班时间
			   //偏移时间减去该时间段
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
	 * 0007569 根据备货时间偏量 得到具体对应时间
	 * 若 偏移时间小于0，则得到该时间
	 * @param gzsjList
	 * @param pysj
	 * @return
	 */
	private String getReductionTime(List<Ticxxsj> gzsjList,int pysj){
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
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
	@Override
	public Object result() {
		return null;
	}

}
