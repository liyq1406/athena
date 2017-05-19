package com.athena.ckx.module.paicfj.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.carry.CkxMsqhb;
import com.athena.ckx.entity.carry.CkxShengcxXianb;
import com.athena.ckx.entity.carry.CkxWaibms;
import com.athena.ckx.entity.carry.CkxXianbDingh;
import com.athena.ckx.entity.xuqjs.Biangjlb;
import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.Xianbkc;
import com.athena.ckx.entity.xuqjs.Yingyxgjl;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 系统控制是否失效
 * 开始日期，结束日期
 * @author hj
 *
 */
@SuppressWarnings("rawtypes")
@Component
public class UtilControlService extends BaseService{
	private  Logger logger =Logger.getLogger(UtilControlService.class);
	/**
	 * 定时任务 根据生效时间和失效时间批量更新biaos
	 * @return
	 */
	@Transactional
	public String updateUtilControlBiaos(String creator){
		Date date = new Date();
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sf.parse(sf.format(date));
		} catch (ParseException e) {
			 sb.append(GetMessageByKey.getMessage("sjzhyc"));
			 return sb.toString();
		}	
		Map<String,String> map = new HashMap<String, String>();
		map.put("creator", creator);
		//"批量更新零件出错,"
//		sb.append(update(date,"ckx.updateckxLingjBiaos",GetMessageByKey.getMessage("plgxcc")));
//		sb.append(update(date,"ckx.updateckxLingjgysBiaos","批量更新零件-供应商出错,"));
		//批量更新零件-消耗点出错,"
		sb.append(update(map,"ckx.updateckxLingjXHDBiaos",GetMessageByKey.getMessage("plgxxhdcc")));
		// 根据原消耗点更新标识并将数据插入到变更记录表中
		sb.append(updateLingjxhdYxhdBsBysxr(map));
		sb.append(updateWullj(map));
		//"批量更新生产线出错,"
		sb.append(update(map,"ckx.updateckxShengcxScjp",GetMessageByKey.getMessage("plgxxsccc")));
		//"批量更新生产平台出错,"
		sb.append(update(map,"ckx.updateckxShengcPTScjp",GetMessageByKey.getMessage("plgxscptcc")));
		//"批量更新工作时间关联出错,"
		sb.append(update(map,"ckx.updateckxCalendarGroupSXSJ",GetMessageByKey.getMessage("plgxgzsjglcc")));
		if(0 < sb.length()){
			return sb.substring(0, sb.length()-1).toString();
		}
		return GetMessageByKey.getMessage("pilgxztcg");
	}
	/**
	 * 根据原消耗点更新标识并将数据插入到变更记录表中
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String updateLingjxhdYxhdBsBysxr(Map<String,String> map){
		String message ="";		
		try {
			List<CkxLingjxhd> lists =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ckx.queryckxLingjXHDYxhdBySxr");
			for (CkxLingjxhd ckxLingjxhd2 : lists) {
				biang(ckxLingjxhd2.getUsercenter(),ckxLingjxhd2.getLingjbh(),ckxLingjxhd2.getYuanxhdbh(),ckxLingjxhd2.getXiaohdbh(),map.get("creator"));
				CkxLingjxhd ckxLingjxhd = new CkxLingjxhd();
				ckxLingjxhd.setUsercenter(ckxLingjxhd2.getUsercenter());
				ckxLingjxhd.setLingjbh(ckxLingjxhd2.getLingjbh());
				ckxLingjxhd.setXiaohdbh(ckxLingjxhd2.getYuanxhdbh());
				ckxLingjxhd =  (CkxLingjxhd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCkxLingjxhd",ckxLingjxhd);
				try {
					ckxLingjxhd.setJiesr(ckxLingjxhd2.getShengxr());
					ckxLingjxhd.setCreator(map.get("creator"));
					super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckx.updateckxLingjXHDBiaosByYxhd",ckxLingjxhd);
				} catch (Exception e) {
					//批量更新零件-消耗点标识by原消耗点出错,"
					message += GetMessageByKey.getMessage("plgxljxhdbsbyyxhdcx");
				}
				try {
					ckxLingjxhd.setXiaohdbh(ckxLingjxhd2.getXiaohdbh());
					super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckx.updateckxLingjXHDYxhdBySxr",ckxLingjxhd);
				} catch (Exception e) {
					//批量更新零件-消耗点原消耗点出错,"
					message += GetMessageByKey.getMessage("plgxljxhdyxhdcx");
				}
				//根据消耗点的变更，同步变更xianbkc
				try{
					xianbkcChange(ckxLingjxhd2,map);
				}catch(Exception e) {
					//批量更新线边库存出错
					message += GetMessageByKey.getMessage("plgxljxhdxbkccx");
				}
				
			}
		} catch (Exception e) {
			message = e.getMessage();
		}		
		return message;
	}
	/**
	 * 线边库存修改
	 * @author zbb
	 * @date 2016-1-20
	 * 
	 */
	private void xianbkcChange(CkxLingjxhd ckxLingjxhd,Map<String,String> map){
		Map param = new HashMap<String,String>();
		Yingyxgjl yingyxgjl = null;
		Xianbkc xianbkc = null;
		Xianbkc nxianbkc = null;
		Xianbkc oxianbkc = null;	
		//分别查出原消耗点好新消耗点对应的线边库存
		param.put("usercenter", ckxLingjxhd.getUsercenter());
		param.put("lingjbh", ckxLingjxhd.getLingjbh());
		param.put("mudd", ckxLingjxhd.getXiaohdbh());
		nxianbkc =  (Xianbkc) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ckxXianbkc.queryXianbkcByParam",param);
		
		param.put("mudd", ckxLingjxhd.getYuanxhdbh());
		oxianbkc =  (Xianbkc) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ckxXianbkc.queryXianbkcByParam",param);
		//如果如果新消耗点不存在，修改原消耗点的Mudd为新消耗点，添加盈余修改记录原盈余按0计算，如果新消耗点已存在，则更新盈余并记录
		if(nxianbkc==null){
			//插入一条新记录
			xianbkc = new Xianbkc();
			xianbkc.setUsercenter(ckxLingjxhd.getUsercenter());
			xianbkc.setChanx(ckxLingjxhd.getShengcxbh());
			xianbkc.setLingjbh(ckxLingjxhd.getLingjbh());			
			xianbkc.setMudd(ckxLingjxhd.getXiaohdbh());
			xianbkc.setLeix("2");			
			xianbkc.setDanw("UN");
			xianbkc.setCreator(map.get("creator"));
			xianbkc.setCreate_time(DateTimeUtil.getAllCurrTime());
			if(oxianbkc==null){				
				xianbkc.setYingy(BigDecimal.ZERO);
				xianbkc.setPandcy(BigDecimal.ZERO);
			}else{				
				xianbkc.setYingy(oxianbkc.getYingy());
				xianbkc.setPandcy(oxianbkc.getPandcy());
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxXianbkc.insertXianbkc", xianbkc);
			
			if(oxianbkc!=null){
				yingyxgjl = new Yingyxgjl();
				yingyxgjl.setUsercenter(ckxLingjxhd.getUsercenter());
				yingyxgjl.setLingjbh(ckxLingjxhd.getLingjbh());
				yingyxgjl.setMudd(ckxLingjxhd.getXiaohdbh());
				yingyxgjl.setOyingy(BigDecimal.ZERO);
				yingyxgjl.setNyingy(oxianbkc.getYingy());
				yingyxgjl.setEditor(map.get("creator"));
				yingyxgjl.setEdit_time(DateTimeUtil.getAllCurrTime());
				//插入盈余修改记录
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxXianbkc.insertYingyxgjl",yingyxgjl);
			}
			
		}else{
			BigDecimal yingy = nxianbkc.getYingy();
			//更新记录
			if(oxianbkc==null){
				nxianbkc.setYingy(BigDecimal.ZERO);
				nxianbkc.setPandcy(BigDecimal.ZERO);
			}else{
				nxianbkc.setYingy(oxianbkc.getYingy());
				nxianbkc.setPandcy(oxianbkc.getPandcy());
			}
			nxianbkc.setEditor(map.get("creator"));
			nxianbkc.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxXianbkc.updateXianbkcByParam",nxianbkc);
			
			
			yingyxgjl = new Yingyxgjl();
			yingyxgjl.setUsercenter(ckxLingjxhd.getUsercenter());
			yingyxgjl.setLingjbh(ckxLingjxhd.getLingjbh());
			yingyxgjl.setMudd(ckxLingjxhd.getXiaohdbh());
			yingyxgjl.setOyingy(yingy);
			if(oxianbkc!=null){
				yingyxgjl.setNyingy(oxianbkc.getYingy());
			}else{
				yingyxgjl.setNyingy(BigDecimal.ZERO);
			}
			yingyxgjl.setEditor(map.get("creator"));
			yingyxgjl.setEdit_time(DateTimeUtil.getAllCurrTime());
			//插入盈余修改记录
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxXianbkc.insertYingyxgjl",yingyxgjl);
		}		
	}

	/**
	 * @description记录零件消耗点变更
	 * @author denggq
	 * @date 2012-4-28
	 */
	private void biang(String usercenter,String lingjbh, String yuanbh,String xianbh,String userId) throws ServiceException {
		Biangjlb bean = new Biangjlb();
		bean.setUsercenter(usercenter);//用户中心
		bean.setLingjbh(lingjbh);//零件编号
		bean.setBianglx("2");//变更类型:消耗点
		bean.setYuanbh(yuanbh);//原编号
		bean.setXianbh(xianbh);//现编号
		bean.setCreator(userId);
		bean.setCreate_time(DateTimeUtil.getAllCurrTime());
		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		bean.setShifsy("0");
//		List<Biangjlb> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryBiangjlb", bean);//查询变更记录表
//		if(0 == list.size()){//若表中无此数据
		// 0007375: 只新增不修改	
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertBiangjlb", bean);
//		}else{//有此数据
//			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateBiangjlb", bean);
//		}
	}
	public String updatePCCalendarBianzh(String creator){
		Date date = new Date();
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sf.parse(sf.format(date));
		} catch (ParseException e) {
			 sb.append(GetMessageByKey.getMessage("sjzhyc"));
			 return sb.toString();
		}
		Map<String,String> map = new HashMap<String, String>();
		map.put("creator", creator);
		
		//"批量更新工作时间关联出错,"
		sb.append(update(map,"ckx.updateckxPCCalendarGroupSXSJ",GetMessageByKey.getMessage("plgxgzsjglcc")));
		if(0 < sb.length()){
			return sb.substring(0, sb.length()-1).toString();
		}
		return GetMessageByKey.getMessage("pilgxztcg");
	}
	/**
	 *  操作表
	 */
	private String update(Map<String,String> map,String sql,String mes){
		String message = "";
		try {
			super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(sql,map);
		} catch (DataAccessException e) {
			logger.info(mes+"------"+e);
			message = mes+":"+e;
		}
		return message;
	}
	/**
	 * 切换模式，记录切换状态
	 * @param date
	 */
	public String updateWullj(Map<String,String> map){
		try {
			this.updateWulljByTransactional(map);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "";
	}
	/**
	 * 切换模式，记录切换状态
	 * @param date
	 */
	@Transactional
	public void updateWulljByTransactional(Map<String,String> map){
		MosqhbService mosqhb = new MosqhbService();		
		mosqhb.insertMosqh(baseDao,map.get("creator"));
		//"批量更新外部模式出错,"
		StringBuffer sb = new StringBuffer();
		sb.append(update(map,"ckx.updateckxWaibms",GetMessageByKey.getMessage("plgxwbms")));
		//"批量更新生产线-线边出错,"
		sb.append(update(map,"ckx.updateckxShengcxXianb",GetMessageByKey.getMessage("plgxxbcc")));
		//"批量更新线边-订货出错,"
		sb.append(update(map,"ckx.updateckxXianbDingh",GetMessageByKey.getMessage("plgxdhcc")));
		
		if(0 < sb.length()){
			throw new ServiceException(sb.toString());
		}
		
	}
	/**
	 * 模式切换判断和记录
	 *  hj 2012-12-10
	 */
	 public  class MosqhbService  {		
		/**
		 *  判断 数据是否已存在，若不存在，则填充进数据
		 * @return Map<String,String>
		 */		
		private Map<String,String> getInstance(){
			Map<String,String> map = new HashMap<String, String>();//模式切换是否记录的判断依据的数据存储
			map.put("R2", "R1");
			map.put("C1", "R1,R2");
			map.put("P*", "R1,R2");
			map.put("RD", "R1");
			map.put("R1", "R2,RD");
			map.put("C0", "R1,RD");
			map.put("CD", "R1,R2,RD");
			map.put("M1", "RM");
			return map;
		}
		/**
		 * 根据模式和将来模式，判断是否进行变更记录
		 * @param mos
		 * @param jlmos
		 * @return boolean
		 */
		public boolean checkSfjl(String mosKey,String jlmosKey){
			Map<String,String> map = getInstance();
			String mosValue = "";	
			//判断是否是以P开头（P开头的符合条件的都需要记录）
			if( mosKey.startsWith("P")){
				mosKey = mosKey.substring(0,1)+"*";
			}
			if( jlmosKey.startsWith("P")){
				jlmosKey = jlmosKey.substring(0,1)+"*";
			} 
			mosValue = map.get(mosKey);		
			boolean flag = false;
			//判断map中是否存在key值，若不存在，则不记录
			if(null != mosValue){
				//若存在，并将来模式在对应的key值中，则记录，否则不记录
				flag = mosValue.contains(jlmosKey);				
			}
			return  flag ? flag : checksfkbqqt(mosKey,jlmosKey);
		}
		/**
		 * 检测是否是看板切其他
		 * @return
		 */
		public boolean checksfkbqqt(String mosKey,String jlmosKey){
			if(mosKey.startsWith("R")&& !jlmosKey.contains("R")){
				return true;
			}
			return false;
		}
		
		/**
		 * 根据模式和将来模式，是否存在订货库，判断是否进行变更记录 0006274
		 * @param mos
		 * @param jlmos
		 * @return boolean
		 */
		public boolean checkSfjl(String mosKey,String jlmosKey,String dinghk){
			Map<String,String> map =  new HashMap<String, String>();//模式切换是否记录的判断依据的数据存储
			map.put("C1", "R2");
			map.put("P*", "R2");
			map.put("C0", "RD");
			String mosValue = "";	
			//判断是否是以P开头（P开头的符合条件的都需要记录）
			if(  mosKey.startsWith("P")){
				mosKey = mosKey.substring(0,1)+"*";
			}
			if( jlmosKey.startsWith("P")){
				jlmosKey = jlmosKey.substring(0,1)+"*";
			} 
			mosValue = map.get(mosKey);		
			//判断map中是否存在key值，若不存在，则不记录
			if(null != mosValue){
				if( mosValue.contains(jlmosKey)){
					return true;
				}
			}
			//若存在订货库，则不记录
			if(null == dinghk){
				return checkSfjl(mosKey, jlmosKey);
			}
			return checksfkbqqt(mosKey,jlmosKey);
		}
		/**
		 * 批量插入变更记录
		 * @param baseDao
		 * @param list
		 */
		@SuppressWarnings("unchecked")
		public void insertMosqh(AbstractIBatisDao baseDao,String creator){
			//查询需要切换的外部模式
			List<CkxWaibms> listWaibms = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ckx.queryckxWaibmsList",new Date());
			List<CkxMsqhb> list = new ArrayList<CkxMsqhb>();
			//遍历外部模式
			for (CkxWaibms ckxWaibms : listWaibms) {		
				//判断是否记录变更，若不是，则跳过此次循环
				if(!checkSfjl(ckxWaibms.getMos(), ckxWaibms.getJianglms(),ckxWaibms.getCreator())){
					continue;
				}
				//如果模式为R1，则需要记录消耗点编号
				if("R1".equals(ckxWaibms.getMos()) || "R1".equals(ckxWaibms.getJianglms())){
					CkxLingjxhd ckxlingjxhd = new CkxLingjxhd();
					ckxlingjxhd.setUsercenter(ckxWaibms.getUsercenter());
					ckxlingjxhd.setLingjbh(ckxWaibms.getLingjbh());
					ckxlingjxhd.setFenpqbh(ckxWaibms.getFenpqh());
					ckxlingjxhd.setBiaos("1");
					List<CkxLingjxhd> listLingjxhd = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getCkxLingjxhd",ckxlingjxhd);
					for (CkxLingjxhd ckxLingjxhd2 : listLingjxhd) {
						CkxMsqhb bean = new CkxMsqhb();
						bean = getCkxMsqhb(ckxWaibms.getUsercenter(), ckxWaibms.getLingjbh(), 
								ckxWaibms.getQid(),ckxLingjxhd2.getXiaohdbh(),
								ckxWaibms.getMos(), ckxWaibms.getJianglms(),"2", creator);
						list.add(bean);	
					}	
				}else{
					CkxMsqhb bean = new CkxMsqhb();
					bean = getCkxMsqhb(ckxWaibms.getUsercenter(), ckxWaibms.getLingjbh(),ckxWaibms.getQid(),null, 
						ckxWaibms.getMos(), ckxWaibms.getJianglms(),"2", creator);
					list.add(bean);		
				}					
			}
			//清除模式切换表
			clearMosqh(baseDao);	
			//将外部模式变更记录插入到模式切换中
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ckx.insertCkxMosqh",list);
			list.clear();
			
			//查询需要切换的线边-订货
			List<CkxXianbDingh> listXianbDingh = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ckx.queryckxXianbDinghList",new Date());
			//遍历线边-订货
			for (CkxXianbDingh ckxXianbDingh : listXianbDingh) {		
				//判断是否记录变更，若不是，则跳过此次循环
				if(!checkSfjl(ckxXianbDingh.getMos(), ckxXianbDingh.getJianglms())){
					continue;
				}
				CkxMsqhb bean = new CkxMsqhb();
				bean = getCkxMsqhb(ckxXianbDingh.getUsercenter(), ckxXianbDingh.getLingjbh(), 
						ckxXianbDingh.getQid(),ckxXianbDingh.getXianbck(),
						ckxXianbDingh.getMos(), ckxXianbDingh.getJianglms(),"3", creator);
				list.add(bean);	
			}
			//将线边-订货变更记录插入到模式切换中
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ckx.insertCkxMosqh",list);
			list.clear();
			

			//查询需要切换的生产线-线边
			List<CkxShengcxXianb> listShengcxXianb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ckx.queryckxShengcxXianbList",new Date());
			//遍历生产线-线边
			for (CkxShengcxXianb ckxShengcxXianb : listShengcxXianb) {		
				CkxLingjxhd ckxlingjxhd = new CkxLingjxhd();
				ckxlingjxhd.setUsercenter(ckxShengcxXianb.getUsercenter());
				ckxlingjxhd.setLingjbh(ckxShengcxXianb.getLingjbh());
				ckxlingjxhd.setFenpqbh(ckxShengcxXianb.getFenpqh());
				ckxlingjxhd.setBiaos("1");
				ckxlingjxhd.setCreator(creator);
				updateLingjxhd(ckxlingjxhd,ckxShengcxXianb.getMos());
				//判断是否记录变更，若不是，则跳过此次循环
				if(!checkSfjl(ckxShengcxXianb.getMos(), ckxShengcxXianb.getJianglms(),ckxShengcxXianb.getCreator())){
					continue;
				}
				List<CkxLingjxhd> listLingjxhd = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getCkxLingjxhd",ckxlingjxhd);
				for (CkxLingjxhd ckxLingjxhd2 : listLingjxhd) {
					CkxMsqhb bean = new CkxMsqhb();
					bean = getCkxMsqhb(ckxShengcxXianb.getUsercenter(), ckxShengcxXianb.getLingjbh(), 
							ckxShengcxXianb.getQid(),ckxLingjxhd2.getXiaohdbh(),
							ckxShengcxXianb.getMos(), ckxShengcxXianb.getJianglms(),"1", creator);
					list.add(bean);	
				}	
			}
			//将生产线-线边变更记录插入到模式切换中
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ckx.insertCkxMosqh",list);
			list.clear();
			
		 }
		/**
		 * 清空模式切换表
		 * @param baseDao
		 */
		public void clearMosqh(AbstractIBatisDao baseDao){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckx.clearckxMosqh");
		}
		 /**
		  * 返回模式切换表的对象
		  * @param usercenter
		  * @param lingjbh
		  * @param yuanms
		  * @param mos
		  * @param muddlx
		  * @param creator
		  * @return
		  */
		 public   CkxMsqhb getCkxMsqhb(String usercenter,String lingjbh,String cangkbh,String xiaohd,String yuanms,String mos,String muddlx,String creator){
			 CkxMsqhb bean = new CkxMsqhb();
			 bean.setLiush(getUUID());
			 bean.setUsercenter(usercenter);
			 bean.setLingjbh(lingjbh);	
			 bean.setCangkbh(cangkbh);
			 bean.setXiaohd(xiaohd);
			 bean.setYuanms(yuanms);
			 bean.setMos(mos);
			 bean.setMuddlx(muddlx);			
			 bean.setCreator(creator);
			 bean.setCreate_time(new Date());
			 bean.setEditor(creator);
			 bean.setEdit_time(new Date());
			 return bean;
		 }
	}
	 /**
	  * 如果模式切换时由MD,CD,C0切换到其他模式，则清除掉对应的零件-消耗点的线边理论库存
	  * @param lingjxhd
	  * @param mos
	  */
	public void updateLingjxhd(CkxLingjxhd lingjxhd,String mos ){
		if(0 <= "MD,CD,C0".indexOf(mos)){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckx.clearLingjxhdXianbLlkc",lingjxhd);
		}
	}
}

