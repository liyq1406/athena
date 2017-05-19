package com.athena.ckx.module.xuqjs.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.Clvjhzjb;
import com.athena.ckx.entity.xuqjs.Daxpcsl;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.XiaohcmbCk;
import com.athena.ckx.entity.xuqjs.Xiaohcmbyy;
import com.athena.ckx.entity.xuqjs.Xitcsdy;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;


/**
 * @description小火车运输时刻模板(执行层)
 * @author hj
 * @date 2013-12-6
 */
@Component
public class XXiaohcmbCkService extends BaseService<XiaohcmbCk> {
	private  Logger logger =Logger.getLogger(XiaohcmbCkService.class);
	private String creator;
	
	@Inject
	XiaohcmbCkTempService xiaohcmbCkTempService;
	
	/**
	 * @description获得命名空间
	 * @author denggq
	 * @date 2012-4-11
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	public String calculate(String creator){
		logger.info("执行层小火车模板计算开始---------------");
		this.creator = creator;
		//UW 计算
		calculateByUsercenter("UW");
		//UX 计算
		calculateByUsercenter("UX");
		// UL 计算
		calculateByUsercenter("UL");
		// VD 计算
		calculateByUsercenter("VD");
		
		logger.info("执行层小火车模板计算结束---------------计算成功");
		changePianyl(creator);
		return "";
	}
	/**
	 * 刷消耗点偏移量
	 * @param creator
	 * @return
	 */
	
	public String changePianyl(String creator){
		logger.info("执行层小火车刷拆分结果偏移量开始---------------");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateXiaohdPyl",creator);
		logger.info("执行层小火车刷拆分结果偏移量结束---------------");
		return "";
	}
	/**
	 * 自动根据将来参数生效日赋值将来参数
	 * @param creator
	 * @return
	 */
	public String autoUpdateXiaohc(String creator){
		logger.info("执行层小火车将来参数更新 开始---------------");
		Xiaohc bean = new Xiaohc();
		bean.setEditor(creator);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx_ck.updateXiaohcByshengxr",bean);
		logger.info("执行层小火车将来参数更新 完成---------------");
		return "success";
	}
    /**
     * 根据用户中心，查询小火车的循环车身数>0 的数据开始计算
     * @param usercenter
     */
	@SuppressWarnings("unchecked")
	private void calculateByUsercenter(String usercenter){
		try {
			Xiaohc xiaohcbean = new Xiaohc();
			List<Daxpcsl> daxpcslList = new ArrayList<Daxpcsl>();
			List<Daxpcsl> tempList = null;
			String dingdjssj = DateTimeUtil.getCurrDate();//订单计算时间
			HashMap<String, Object> param = new HashMap<String,Object>();			
			HashMap<String,Daxpcsl> daxpcslMap = new HashMap<String,Daxpcsl>();		
			HashMap<String,Daxpcsl> clddxxMap = new HashMap<String,Daxpcsl>();	
			HashMap<String,Daxpcsl> fenzxpcslMap = new HashMap<String,Daxpcsl>();
			HashMap<String,Daxpcsl> fenzxpcjhMap = new HashMap<String,Daxpcsl>();
			HashMap<String,Daxpcsl> tempMap = new HashMap<String,Daxpcsl>();	
			List<String> keyList = null;
			Xiaohcmbyy xiaohcmbyy = null;
			String maxRiq=null;
			Shengcx shengcx = null;
			xiaohcbean.setUsercenter(usercenter);//通过传递的用户中心计算
			xiaohcbean.setXunhcss(0+"");//取循环车身数大于0 的数据
			xiaohcbean.setBiaos("1");//取有效的小火车表数据进行计算
			//取到该用户中心下的所有产线
			List<Xiaohc> listChanx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("ts_ckx.xiaohc_GroupByChanx",xiaohcbean);
			for (Xiaohc bean : listChanx) {
				
				bean.setXunhcss(0+"");//取循环车身数大于0 的数据
				bean.setBiaos("1");//取有效的小火车表数据进行计算	
				
				//取到该产线下的所有小火车进行计算
				List<Xiaohc> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("ts_ckx.queryXiaohcList",bean);
				
				param.put("usercenter", bean.getUsercenter());
				param.put("shengcxbh", bean.getShengcxbh());	
				//加上当前日期的条件
				param.put("riq", dingdjssj);
				tempList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("ts_ckx.queryDaxpcsl",param);
				for (int i = 0; i < tempList.size(); i++) {
					Daxpcsl daxpcsl = tempList.get(i);					
					String key = daxpcsl.getRiq();
					daxpcslMap.put(key, daxpcsl);
				}
				tempList.clear();
				tempList=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("ts_ckx.queryDaxpcslClddxx",param);
				for (int i = 0; i < tempList.size(); i++) {
					Daxpcsl daxpcsl = tempList.get(i);					
					String key = daxpcsl.getRiq();
					clddxxMap.put(key, daxpcsl);
				}					
				tempList.clear();
				tempList=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("ts_ckx.queryCkxFenzxpcsl",param);
				for (int i = 0; i < tempList.size(); i++) {
					Daxpcsl daxpcsl = tempList.get(i);					
					String key = daxpcsl.getRiq();
					fenzxpcslMap.put(key, daxpcsl);
				}	
				
				
				tempList.clear();
				tempList=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("ts_ckx.queryXqjsFenzxpcjh",param);
				for (int i = 0; i < tempList.size(); i++) {
					Daxpcsl daxpcsl = tempList.get(i);					
					String key = daxpcsl.getRiq();
					fenzxpcjhMap.put(key, daxpcsl);
				}	
				
				//合并两个map
				tempMap.putAll(clddxxMap);
				tempMap.putAll(daxpcslMap);
				tempMap.putAll(fenzxpcslMap);
				tempMap.putAll(fenzxpcjhMap);
				keyList = new ArrayList<String>(tempMap.keySet());
				//key排序
				Collections.sort(keyList);
				
				for (int i = 0; i < keyList.size(); i++) {					
					//key=用户中心+产线+日期
					String key = keyList.get(i);
					logger.debug(key);
					//如果大线排产有计划上线量就用大线排产，如果没有从九天排查计划查出
					if(daxpcslMap.get(key)!=null){
						daxpcslList.add(daxpcslMap.get(key));
					}else if(clddxxMap.get(key)!=null){
						daxpcslList.add(clddxxMap.get(key));
					}else if(fenzxpcslMap.get(key)!=null){
						daxpcslList.add(fenzxpcslMap.get(key));
					}else{
						daxpcslList.add(fenzxpcjhMap.get(key));
					}
				}				
				
				String mes = "未找到用户中心："+bean.getUsercenter()+",产线："+bean.getShengcxbh()+"的上线量数据" ;
				if(daxpcslList==null||daxpcslList.size()==0){//20160829 by CSY 当产线为空，跳过
					logger.error(mes);
					logger.debug(mes);
					continue ;//跳过计算该产线					
				}
				//获取对应的拆分天数
				Object chaifts = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("ts_ckx.queryChaifts",param);
				if(chaifts!=null){
					if(Integer.parseInt(chaifts.toString())<daxpcslList.size()){
						daxpcslList = daxpcslList.subList(0, Integer.parseInt(chaifts.toString()));
					}
				}
				//根据用户中心产线查出对应的最大流水号
				String maxLiush = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getZhengcgdMaxlsh",param);
				//String maxLiush = null;
				
				//获取修正日期
				String xiuzrq = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getXiuzrq",param);
				logger.debug("修正日期="+xiuzrq);
				boolean isXiuzrq = false;
				//判断是否是修正日期
				if(xiuzrq!=null&&xiuzrq.equals(dingdjssj)){
					isXiuzrq = true;
				}
				for(Xiaohc xiaohc:list){
					List<Daxpcsl> xiaoDaxpcslList = new ArrayList<Daxpcsl>();					
					
					//小火车上次多要的数量
					int yingy=0;
					xiaohcmbyy = this.queryXiaohcmbyy(xiaohc.getUsercenter(), xiaohc.getShengcxbh(), xiaohc.getXiaohcbh(), this.creator);
					yingy = xiaohcmbyy.getYingy();					
					
					param.put("xiaohcbh", xiaohc.getXiaohcbh());
					
					//查询开始流水号kaislsh
					String kaislsh = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("ts_ckx.getKaislsh",param);
					logger.debug("第一趟的kailsh="+kaislsh);
					//查询已算趟次数ystcs
					int ystcs = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("ts_ckx.getYistcs",param);
					logger.debug("已算趟次数="+ystcs);
					
					//查出对应小火车在小火车模板最大日期
					maxRiq = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("ts_ckx.queryMaxrq",param);
					//根据用户中心，生产线查出生产线号，上线日期，上线量的daxpcslList
					if(maxRiq!=null){
						Daxpcsl dpl=null;
						//找到最大日期对应的list
						for(int i=0;i<daxpcslList.size();i++){
							dpl = daxpcslList.get(i);
							//比较riq与maxRiq
							if(dpl.getRiq().compareTo(maxRiq)>0){
								xiaoDaxpcslList = daxpcslList.subList(i, daxpcslList.size());
								break;
							}							
						}
						
					}else{
						xiaoDaxpcslList = daxpcslList;
					}
					List<XiaohcmbCk> listXiaohcmbCk = new ArrayList<XiaohcmbCk>();
					//遍历daxpcslList,生成小火车模板
					for(int i=0;i<xiaoDaxpcslList.size();i++){		
						Daxpcsl daxpcsl = xiaoDaxpcslList.get(i);
						shengcx = new Shengcx();
						shengcx.setUsercenter(daxpcsl.getUsercenter());
						shengcx.setShengcxbh(daxpcsl.getDaxxh());//大线号
						shengcx.setBiaos("1");
						//如果此产线的计算出错，则跳过此产线的计算
						whileAdd(xiaohc,shengcx,daxpcsl,listXiaohcmbCk,xiaohcmbyy,i,maxLiush,isXiuzrq,kaislsh,ystcs);						
						
					}	
					//按照产线将数据批量插入到小火车模板表中
					//cs优化变更010466 gswang 2014-09-10
					if(listXiaohcmbCk.size()>0){
						xiaohcmbCkTempService.insertXXiaohcmb(listXiaohcmbCk,shengcx);
						//清空list防止重复插入
						listXiaohcmbCk.clear();
					}	
					
					//更新上次多要的要货量
					param.put("chanx", xiaohc.getShengcxbh());
					param.put("yingy", xiaohcmbyy.getYingy());
					param.put("editor", this.creator);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("ts_ckx.updateXiaohcmbyyByParam",param);
										
					maxRiq = null;					
					xiaoDaxpcslList=null;
					//释放对象资源
					listXiaohcmbCk = null;
				}
				//大线排产列表清空
				daxpcslList.clear();
				daxpcslMap.clear();
				clddxxMap.clear();
				fenzxpcjhMap.clear();
				fenzxpcslMap.clear();
				tempMap.clear();
				
			}
			logger.info("执行层小火车模板计算   "+usercenter+" 计算完成---------------");
		} catch (Exception e) {
			logger.error(e.toString());			
			logger.info(e.toString()+"执行层小火车模板计算   "+usercenter+" 计算失败---------------");
			throw new ServiceException(e.toString());
		}
		
	}
	
	/**
	 * 循环将数据插入小火车模板
	 * @param list
	 * shengcx
	 */
	private void whileAdd(Xiaohc xiaohc,Shengcx shengcx,Daxpcsl daxpcsl,List<XiaohcmbCk> listXiaohcmbCk,Xiaohcmbyy xiaohcmbyy,int qis,String maxLiush,boolean isXiuzrq,String kaislsh,int ystcs){
		String mes = "未找到用户中心："+shengcx.getUsercenter()+",产线："+shengcx.getShengcxbh()+"的数据或数据已失效" ;
		shengcx = (Shengcx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("ts_ckx.getDdbhShengcx",shengcx);
		XiaohcmbCk xiaohcmbLast=null;
		if(null == shengcx){
			//如果生产线不存在或数据已失效，则跳过此生产线的计算
			logger.error(mes);		
			logger.debug(mes);
			return ;//跳过计算该产线
///			throw new ServiceException(mes);
		}
		if(shengcx.getChews() == null){
			//如果生产线的车位数未空，则跳过此生产线的计算
			logger.error("用户中心："+shengcx.getUsercenter()+",产线："+shengcx.getShengcxbh()+"的车位数未维护，请维护后重新计算");		
			logger.debug("用户中心："+shengcx.getUsercenter()+",产线："+shengcx.getShengcxbh()+"的车位数未维护，请维护后重新计算");
			return ;//跳过计算该产线
		}
		int xhtc = 30;		
		
		if(xiaohc.getShangxtqcss()==null
				|| xiaohc.getPianycws()==null || xiaohc.getBeihtqcss()==null){
			logger.debug("生产线："+shengcx.getShengcxbh()+"的未配置对应的模板控制");
			return ;
		}
		//循环次数=产线当日上线车身数/小火车循环车身数
		if(xiaohc.getXunhcss()!=null&&!"".equals(xiaohc.getXunhcss())&&Integer.parseInt(xiaohc.getXunhcss())!=0){
			int yingy = xiaohcmbyy.getYingy();
			//如果上线量小于盈余，必须要一趟货
			if(daxpcsl.getJihsxl()-yingy<0){
				xhtc = 1;
				yingy = Integer.parseInt(xiaohc.getXunhcss())*xhtc -(daxpcsl.getJihsxl()-yingy);
				xiaohcmbyy.setYingy(yingy);
			}else{
				xhtc = (int) Math.ceil((daxpcsl.getJihsxl()-yingy)/Float.parseFloat(xiaohc.getXunhcss()));//向上取整
				yingy = Integer.parseInt(xiaohc.getXunhcss())*xhtc -(daxpcsl.getJihsxl()-yingy);
				xiaohcmbyy.setYingy(yingy);
			}
			logger.debug("yingy："+xiaohcmbyy.getYingy());
		}
		XiaohcmbCk	xiaohcmbCk = new XiaohcmbCk();
		xiaohcmbCk.setUsercenter(xiaohc.getUsercenter());
		xiaohcmbCk.setChanx(xiaohc.getShengcxbh());
		xiaohcmbCk.setXiaohcbh(xiaohc.getXiaohcbh());
		// 当前趟次
		if(xiaohc.getDangqtc()!=null){
			xiaohcmbCk.setTangc(Integer.parseInt(xiaohc.getDangqtc()));
		}
		
		//如果起始点是0,则查询新的最大值；如果不是，计算流水号	
		
		// 查询结束流水号
		Object jslsh = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("ts_ckx.getMaxJieslshByXiaohc", xiaohcmbCk);
		Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("ts_ckx.getXMaxLiushByXiaohc", xiaohcmbCk);
		//是否需要修正
		boolean flag=false;
		Integer xiuzLiush = null;
		 
		if(qis==0){
			//如果有结束流水号
			if(jslsh!=null){
				//isxiuzrq,ystcs,maxLiush
				if(maxLiush!=null && isXiuzrq==true && ystcs!=0 ){
					//maxLiush为起始流水号,iMaxlsh为上次开始流水号
					int iMaxlsh = Integer.parseInt(maxLiush)-Integer.parseInt(xiaohc.getPianycws());
					flag = true;
					logger.debug("上次起始流水号="+iMaxlsh);
					//iMaxlsh>kaislsh
					if(iMaxlsh>=Integer.parseInt(kaislsh)){
						xiuzLiush = iMaxlsh -1 + (ystcs + 1)*Integer.parseInt(xiaohc.getXunhcss());
						logger.debug("修正流水号="+xiuzLiush);
					}else{//Max<jieslsMax,Max+n*xunhcss>jieslshMax最小n
						int tangcx = (int) Math.floor( (Integer.parseInt(kaislsh) - iMaxlsh)/Float.parseFloat(xiaohc.getXunhcss()) );
						logger.debug("趟次差="+tangcx);
						//tangcx大于等于xhtc时不做计算
						if(tangcx>=xhtc){
							return;
						}else{
							
							xiuzLiush = iMaxlsh -1 + (ystcs + tangcx + 1)*Integer.parseInt(xiaohc.getXunhcss());
							logger.debug("修正流水号="+xiuzLiush);
							//循环趟次减掉tangcx
							xhtc = xhtc - tangcx;
							logger.debug("修正后xhtc="+xhtc);
						}
					}				
				}
				xiaohcmbCk.setJieslsh(Integer.valueOf(jslsh.toString()));
			}else{
				int jieslsh = this.getJieslsh(xiaohc.getUsercenter(), xiaohc.getShengcxbh(),daxpcsl.getRiq(),xiaohc);
				//未查出结束流水号
				if(jieslsh==-9999){
					return;
				}else{
					xiaohcmbCk.setJieslsh(jieslsh);
				}
			}
			logger.debug("上次结束流水号="+jslsh);
			// 小火车模板流水号
			xiaohcmbCk.setLiush(Integer.valueOf(obj.toString()));
			logger.debug("最大流水号="+obj);
		}else{
			if(listXiaohcmbCk.size()>0){
				xiaohcmbLast = listXiaohcmbCk.get(listXiaohcmbCk.size()-1);
			}
			if(xiaohcmbLast!=null){
				xiaohcmbCk.setJieslsh(xiaohcmbLast.getJieslsh());
				logger.debug("上次结束流水号="+xiaohcmbCk.getJieslsh());
				// 小火车模板流水号
				xiaohcmbCk.setLiush(xiaohcmbLast.getLiush());
				logger.debug("最大流水号="+xiaohcmbCk.getLiush());
			}else{				
				return;	
			}
		}
		
		//xiaohcmbCkTempService.removeMbByQislsh(xiaohcmbCk);
		//重新查询，避免并发
		if(flag==true&&xiuzLiush!=null){
			formulaCalculate( xiaohcmbCk, xiaohc, shengcx,xhtc,daxpcsl,listXiaohcmbCk,xiuzLiush);
		}else{
			// 根据公式，循环计算出备货流水号。。。，将循环30次得到的流水号插入小火车模板，
			formulaCalculate( xiaohcmbCk, xiaohc, shengcx,xhtc,daxpcsl,listXiaohcmbCk);
		}
		
	}
	
	/**
	 * 根据公式，循环计算出备货流水号。。。，将循环30次得到的流水号插入小火车模板，
	 * @param liush
	 * @param temp
	 * @param xunhcss
	 * @param xiaohc
	 * @param listXiaohcmbCk
	 */
	private void formulaCalculate(XiaohcmbCk xiaohcmbCk,Xiaohc xiaohc,Shengcx shengcx,int xhtc,Daxpcsl daxpcsl,List<XiaohcmbCk> listXiaohcmbCk){
		// 循环车身数
		int xunhcss = Integer.parseInt(xiaohc.getXunhcss());
		// 流水号
		int liush = xiaohcmbCk.getLiush();
		// 小火车临时趟次
		int tangc = 0;
		
		int jieslsh = xiaohcmbCk.getJieslsh();
		for(int i = 0 ; i < xhtc ; i++){			
			tangc++;
			//流水号顺序生成
			liush++;
			XiaohcmbCk bean = new XiaohcmbCk();
			bean.setUsercenter(xiaohc.getUsercenter());
			bean.setChanx(xiaohc.getShengcxbh());
			bean.setXiaohcbh(xiaohc.getXiaohcbh());
			bean.setLiush(liush);
			bean.setTangc(tangc);
			bean.setXiaohcrq(daxpcsl.getRiq());
	
			//起始流水号
			bean.setQislsh(jieslsh+1);
			//结束流水号
			bean.setJieslsh(bean.getQislsh()-1+xunhcss);		
			
			//备货流水号E = 偏移车位数-备货提前车身数+循环车身数（第一次不加）
			bean.setEmonbhlsh(Integer.parseInt(xiaohc.getPianycws()) - Integer.parseInt(xiaohc.getBeihtqcss()) + jieslsh);
			
			//上线流水号E = 偏移车位数-上线提前车身数+循环车身数（第一次不加）
			bean.setEmonsxlsh(Integer.parseInt(xiaohc.getPianycws()) - Integer.parseInt(xiaohc.getShangxtqcss())+jieslsh);
			
			//备货流水号S = 备货流水号E - 车位数
			bean.setSmonbhlsh(bean.getEmonbhlsh() - Integer.parseInt(shengcx.getChews()));
		
			//上线流水号S = 上线流水号E-车位数
			bean.setSmonsxlsh(bean.getEmonsxlsh() - Integer.parseInt(shengcx.getChews()));
			
			bean.setFlag("0");
			bean.setCreator(creator);
			bean.setEditor(creator);			
			logger.debug(bean.getUsercenter()+","+bean.getChanx()+","+bean.getXiaohcbh()+","+bean.getLiush()+","+bean.getTangc());
			listXiaohcmbCk.add(bean);
			jieslsh = bean.getJieslsh();
		}	
		
	}
	
	/**
	 * 根据公式，循环计算出备货流水号。。。，将循环30次得到的流水号插入小火车模板，
	 * @param liush
	 * @param temp
	 * @param xunhcss
	 * @param xiaohc
	 * @param listXiaohcmbCk
	 */
	private void formulaCalculate(XiaohcmbCk xiaohcmbCk,Xiaohc xiaohc,Shengcx shengcx,int xhtc,Daxpcsl daxpcsl,List<XiaohcmbCk> listXiaohcmbCk,Integer xiuzLiush){
		// 循环车身数
		int xunhcss = Integer.parseInt(xiaohc.getXunhcss());
		// 流水号
		int liush = xiaohcmbCk.getLiush();
		// 小火车临时趟次
		int tangc = 0;
		
		int jieslsh = xiaohcmbCk.getJieslsh();
		for(int i = 0 ; i < xhtc ; i++){			
			tangc++;
			//流水号顺序生成
			liush++;
			XiaohcmbCk bean = new XiaohcmbCk();
			bean.setUsercenter(xiaohc.getUsercenter());
			bean.setChanx(xiaohc.getShengcxbh());
			bean.setXiaohcbh(xiaohc.getXiaohcbh());
			bean.setLiush(liush);
			bean.setTangc(tangc);
			bean.setXiaohcrq(daxpcsl.getRiq());
			
			//起始流水号
			bean.setQislsh(jieslsh+1);
			//对第一趟进行修正
			if(i==0){
				//修正流水号不为null
				if(xiuzLiush!=null){					
					//结束流水号
					bean.setJieslsh(xiuzLiush);				
				}else{
					bean.setJieslsh(bean.getQislsh()-1+xunhcss);		
				}
			}else{
				bean.setJieslsh(bean.getQislsh()-1+xunhcss);		
			}
			//备货流水号E = 偏移车位数-备货提前车身数+循环车身数（第一次不加）
			bean.setEmonbhlsh(Integer.parseInt(xiaohc.getPianycws()) - Integer.parseInt(xiaohc.getBeihtqcss()) + jieslsh);
			
			//上线流水号E = 偏移车位数-上线提前车身数+循环车身数（第一次不加）
			bean.setEmonsxlsh(Integer.parseInt(xiaohc.getPianycws()) - Integer.parseInt(xiaohc.getShangxtqcss())+jieslsh);
			
			//备货流水号S = 备货流水号E - 车位数
			bean.setSmonbhlsh(bean.getEmonbhlsh() - Integer.parseInt(shengcx.getChews()));
		
			//上线流水号S = 上线流水号E-车位数
			bean.setSmonsxlsh(bean.getEmonsxlsh() - Integer.parseInt(shengcx.getChews()));
			
			bean.setFlag("0");
			bean.setCreator(creator);
			bean.setEditor(creator);			
			logger.debug(bean.getUsercenter()+","+bean.getChanx()+","+bean.getXiaohcbh()+","+bean.getLiush()+","+bean.getTangc());
			listXiaohcmbCk.add(bean);
			jieslsh = bean.getJieslsh();
		}	
		
	}
	
	
	/**
	 * 获取线边库存
	 * @param userCenter 用户中心
	 * @param chanx 生产线
	 * @param xiaohcbh 小火车编号
	 * @param user 用户 
	 * @return xiaohcmbyy 小火车模板上次多要量
	 */
	public Xiaohcmbyy queryXiaohcmbyy(String userCenter,String chanx,String xiaohcbh,String user){
		Map<String, String> map = new HashMap<String, String>();// 参数map
		map.put("usercenter", userCenter);
		map.put("chanx", chanx);		
		map.put("xiaohcbh", xiaohcbh);
		
		Xiaohcmbyy xiaohcmbyy = (Xiaohcmbyy)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectObject("ts_ckx.queryXiaohcmbyyByParam", map);
		if(xiaohcmbyy == null){
			xiaohcmbyy = new Xiaohcmbyy();
			xiaohcmbyy.setUsercenter(userCenter);
			xiaohcmbyy.setChanx(chanx);
			xiaohcmbyy.setXiaohcbh(xiaohcbh);	
			xiaohcmbyy.setCreator(user);
			xiaohcmbyy.setYingy(0);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("ts_ckx.insertXiaohcmbyy", xiaohcmbyy);
		}		
		return xiaohcmbyy;
	}
	
	/**
	 * 获取上次的jieslsh
	 * @param userCenter 用户中心
	 * @param chanx 生产线	
	 * @param riq 日期
	 * @param xiaohc 小火车信息
	 * @return jieslsh -9999表示没有查出结果
	 */
	public int getJieslsh(String userCenter,String chanx,String riq,Xiaohc xiaohc){
		int jielsh = -9999;
		Map<String, String> map = new HashMap<String, String>();// 参数map
		map.put("usercenter", userCenter);
		map.put("shengcxbh", chanx);		
	
		//用户中心产线，查询ddbh_clvjh_zjb,ddbh_pcjh_zjb
		Map liushMap = new HashMap<String,Clvjhzjb>();
		List<Clvjhzjb> liushList = null;//ts_ckx.getClvjhPcjh
		liushList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).select("ts_ckx.getClvjhPcjh",map);
		//如果liushList为空
		if(liushList.size()==0){	
			//开始流水号1-Integer.parseInt(xiaohc.getPianycws()),上次的结束流水号=开始流水号-1
			return 1-Integer.parseInt(xiaohc.getPianycws())-1;
		}
		//将liushList放入到liushMap中,日期作为key
		for(Clvjhzjb item:liushList){
			if(liushMap.get(item.getShangxsj())==null){
				liushMap.put(item.getShangxsj(), item);
			}
		}
		
		Clvjhzjb clvjhzjb = (Clvjhzjb) liushMap.get(riq);
		if(clvjhzjb!=null){
			jielsh = clvjhzjb.getLiush()-Integer.parseInt(xiaohc.getPianycws())-1;
		}
		
		return jielsh;
	}
}
