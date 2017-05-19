package com.athena.xqjs.module.ilorder.service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ppl.Niandyg;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.CalendarCenterService;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * <p>
 * Title:订单分析类
 * </p>
 * <p>
 * Description:订单分析类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李智
 * @version v1.0
 * @date 2012-03-06
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class DingdFenxiService extends BaseService{
	@Inject
	private CalendarCenterService calendarCenterService;
	/**
	 * 订单和毛需求比较
	 * @author 李智
	 * @param maoxq
	 * @param dingd
	 * @param jzlx(基准类型,1,订单为基准,2毛需求为基准)
	 * @return List<Dingdlj> 比较的结果
	 */
	public Map<String, Object> dingdBijiaoByMaoxq(Dingd dingd,Maoxq maoxq,int jzlx,LoginUser loginUser,String dingdjssj,Map tjMap) {
		Map<String, String> param = new HashMap<String, String>();
		//param.put("maoxqbc",maoxq.getXuqbc());
		String tempDingdjssj = "";
		if(dingdjssj!=null&&!"".equals(dingdjssj)){
			String tempDingdjssjSub1 = dingdjssj.substring(0,10);
			String tempDingdjssjSub2 = dingdjssj.substring(11);
			tempDingdjssj = tempDingdjssjSub1+" "+tempDingdjssjSub2;
		}
		param.put("dingdjssj",tempDingdjssj);
		String lingjbh = "";
		if(tjMap.get("lingjbh")!=null){
			lingjbh = (String)tjMap.get("lingjbh");
		}
		param.put("lingjbh", lingjbh);
		String gongysdm = "";
		if(tjMap.get("gongysdm")!=null){
			gongysdm = (String)tjMap.get("gongysdm");
		}
		param.put("gongysdm", gongysdm);
		//取订单零件处理类型,资源获取日期
		List dingdljList = (List)this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingdljZq",param);
		Dingdlj dingdlj = (Dingdlj)dingdljList.get(0);
		//设置查询条件
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", loginUser.getUsercenter());
		map.put("riq", dingdlj.getZiyhqrq());
		//得到资源获取日期的年周期和周
		CalendarCenter calendarCenter = calendarCenterService.queryCalendarCenterObject(map);
		
		//设置比较条件
		Map<String, Object> params = new HashMap<String, Object>();
		//资源获取日期
		params.put("ziyhqrq", dingdlj.getZiyhqrq());
		//资源获取日期的所属周期
		params.put("ziyhqZq", calendarCenter.getNianzq());
		//需求版次
		params.put("xuqbc", maoxq.getXuqbc());
		params.put("lingjbh", dingd.getLingjbh());
		params.put("gongysdm", dingd.getGongysdm());
		params.put("dingdjssj",tempDingdjssj);
		//PP处理类型
		List<Dingdlj> ljList = new ArrayList<Dingdlj>();
		Map<String, Object> ljmap = new HashMap<String, Object>();
		
		if(dingdlj.getChullx().equals(Const.PP)||dingdlj.getChullx().equals(Const.NP)) {
			//PP类型的比较查询
			ljmap = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.dingdBijiaoByMaoxq_PP", params, dingd);
		}
		else if(dingdlj.getChullx().equals(Const.PS)||dingdlj.getChullx().equals(Const.NS)) {
			//PS类型的比较查询
			ljmap = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.dingdBijiaoByMaoxq_PS",params, dingd);
		}
		else if(dingdlj.getChullx().equals(Const.PJ)||dingdlj.getChullx().equals(Const.NJ)) {
			//PS类型的比较查询
			ljmap = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.dingdBijiaoByMaoxq_PJ",params, dingd);
		}
		ljList = (List<Dingdlj>)ljmap.get("rows");
		
		for(int i=0; i<ljList.size(); i++) {
			Dingdlj lj = ljList.get(i);
			//当订单零件在明细零件中不存在的时候
			if(null==lj.getMxLingjbh() || "".equals(lj.getMxLingjbh())) {
				bijiao(jzlx, lj, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
						lj.getP0sl(), lj.getP1sl(), lj.getP2sl(), lj.getP3sl(), lj.getP4sl());
			}
			//当明细零件在订单零件中不存在的时候
			else if(null==lj.getLingjbh() || "".equals(lj.getLingjbh())) {
				//把明细零件的信息放到订单中方便页面显示
				lj.setLingjbh(lj.getMxLingjbh());			//零件编号
				lj.setUsercenter(lj.getMxUsercenter());		//用户中心
				lj.setZhongwmc(lj.getMxZhongwmc());			//中文名称
				lj.setGongysdm(lj.getMxGongysbh());			//供应商编码
				lj.setZhizlx(lj.getMxZhizlx());				//制造线路
				lj.setDanw(lj.getMxDanw());					//单位
				
				bijiao(jzlx, lj, lj.getMxP0sl(), lj.getMxP1sl(), lj.getMxP2sl(), lj.getMxP3sl(), lj.getMxP4sl(), 
						BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
			}
			else {
				bijiao(jzlx, lj, lj.getMxP0sl(), lj.getMxP1sl(), lj.getMxP2sl(), lj.getMxP3sl(), lj.getMxP4sl(), 
						lj.getP0sl(), lj.getP1sl(), lj.getP2sl(), lj.getP3sl(), lj.getP4sl());
			}
		}
		return ljmap;
	}
	
	/**
	 * 订单和年度预告比较
	 * @author 李智
	 * @param niandyg
	 * @param dingd
	 * @param jzlx(基准类型,1,订单为基准,2年度预告为基准,现在只有订单为基准)
	 * @return List<Dingdlj> 比较的结果
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IntrospectionException 
	 * @throws ParseException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 
	 */
	public Map<String, Object> dingdBijiaoByNiandyg(Pageable page,Niandyg niandyg,Dingd dingd,int jzlx,LoginUser loginUser) throws IllegalArgumentException, ParseException, IntrospectionException, IllegalAccessException, InvocationTargetException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("dingdh", dingd.getDingdh());
		param.put("pplbc", niandyg.getPplbc());
		param.put("dingdjssj", dingd.getDingdjssj());
		List<Dingdlj> ljList = new ArrayList<Dingdlj>();
		//暂时只有PP处理类型
		Map<String, Object> ljmap = new HashMap<String, Object>();
		ljmap = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.dingdBijiaoByNiandyg",param,page);
		ljList = (List<Dingdlj>)ljmap.get("rows");
		
		for(int i=0; i<ljList.size(); i++) {
			Dingdlj lj = ljList.get(i);
			
			//当订单零件在明细零件中不存在的时候
			if(null==lj.getMxLingjbh() || "".equals(lj.getMxLingjbh())) {
				
				bijiao(jzlx, lj, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
						lj.getP0sl(), lj.getP1sl(), lj.getP2sl(), lj.getP3sl(), lj.getP4sl());
			}
			//当明细零件在订单零件中不存在的时候
			else if(null==lj.getLingjbh() || "".equals(lj.getLingjbh())) {
				//把明细零件的信息放到订单中方便页面显示
				lj.setLingjbh(lj.getMxLingjbh());			//零件编号
				lj.setUsercenter(lj.getMxUsercenter());		//用户中心
				lj.setZhongwmc(lj.getMxZhongwmc());			//中文名称
				lj.setGongysdm(lj.getMxGongysbh());			//供应商编码
				lj.setZhizlx(lj.getMxZhizlx());				//制造线路
				lj.setDanw(lj.getMxDanw());					//单位
				
				//得到这个订单的P0日期，是相同的
				String p0fyzqxh = (String)this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDingdljP0rq",param);
				String mxP0zhouqxh = lj.getMxP0zhouqxh();
				lj = getMxBijiaoSl(p0fyzqxh, mxP0zhouqxh, lj);
				
				bijiao(jzlx, lj, lj.getMxP0sl(), lj.getMxP1sl(), lj.getMxP2sl(), lj.getMxP3sl(), lj.getMxP4sl(), 
						BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
				lj.setP0fyzqxh(p0fyzqxh);
			}
			else {
				String p0fyzqxh = lj.getP0fyzqxh();
				String mxP0zhouqxh = lj.getMxP0zhouqxh();
				lj = getMxBijiaoSl(p0fyzqxh, mxP0zhouqxh, lj);
				bijiao(jzlx, lj, lj.getMxP0sl(), lj.getMxP1sl(), lj.getMxP2sl(), lj.getMxP3sl(), lj.getMxP4sl(), 
						lj.getP0sl(), lj.getP1sl(), lj.getP2sl(), lj.getP3sl(), lj.getP4sl());
			}
		}
		return ljmap;
	}
	
	public List<Dingdlj> expDingdBijiaoByNiandyg(Niandyg niandyg,Dingd dingd,int jzlx,LoginUser loginUser) throws IllegalArgumentException, ParseException, IntrospectionException, IllegalAccessException, InvocationTargetException {
		Map<String, String> param = new HashMap<String, String>();
		param.put("dingdh", dingd.getDingdh());
		param.put("pplbc", niandyg.getPplbc());
		param.put("dingdjssj", dingd.getDingdjssj());
		List<Dingdlj> ljList = new ArrayList<Dingdlj>();
		//暂时只有PP处理类型
		ljList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.dingdBijiaoByNiandyg",param);
		
		for(int i=0; i<ljList.size(); i++) {
			Dingdlj lj = ljList.get(i);
			
			//当订单零件在明细零件中不存在的时候
			if(null==lj.getMxLingjbh() || "".equals(lj.getMxLingjbh())) {
				
				bijiao(jzlx, lj, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
						lj.getP0sl(), lj.getP1sl(), lj.getP2sl(), lj.getP3sl(), lj.getP4sl());
			}
			//当明细零件在订单零件中不存在的时候
			else if(null==lj.getLingjbh() || "".equals(lj.getLingjbh())) {
				//把明细零件的信息放到订单中方便页面显示
				lj.setLingjbh(lj.getMxLingjbh());			//零件编号
				lj.setUsercenter(lj.getMxUsercenter());		//用户中心
				lj.setZhongwmc(lj.getMxZhongwmc());			//中文名称
				lj.setGongysdm(lj.getMxGongysbh());			//供应商编码
				lj.setZhizlx(lj.getMxZhizlx());				//制造线路
				lj.setDanw(lj.getMxDanw());					//单位
				
				//得到这个订单的P0日期，是相同的
				String p0fyzqxh = (String)this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDingdljP0rq",param);
				String mxP0zhouqxh = lj.getMxP0zhouqxh();
				lj = getMxBijiaoSl(p0fyzqxh, mxP0zhouqxh, lj);
				
				bijiao(jzlx, lj, lj.getMxP0sl(), lj.getMxP1sl(), lj.getMxP2sl(), lj.getMxP3sl(), lj.getMxP4sl(), 
						BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
				lj.setP0fyzqxh(p0fyzqxh);
			}
			else {
				String p0fyzqxh = lj.getP0fyzqxh();
				String mxP0zhouqxh = lj.getMxP0zhouqxh();
				lj = getMxBijiaoSl(p0fyzqxh, mxP0zhouqxh, lj);
				bijiao(jzlx, lj, lj.getMxP0sl(), lj.getMxP1sl(), lj.getMxP2sl(), lj.getMxP3sl(), lj.getMxP4sl(), 
						lj.getP0sl(), lj.getP1sl(), lj.getP2sl(), lj.getP3sl(), lj.getP4sl());
			}
		}
		return ljList;
	}
	/**
	 * 得到明细p0sl/p1sl/p2sl/p3sl/p4sl
	 * @param p0fyzqxh
	 * @param mxP0zhouqxh
	 * @param lj
	 * @return
	 * @throws ParseException 
	 * @throws IntrospectionException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception
	 */
	public Dingdlj getMxBijiaoSl(String p0fyzqxh,String mxP0zhouqxh,Dingdlj lj) throws ParseException, IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
		//将p0fyzqxh还原成YYYY-MM-DD类型
		p0fyzqxh = p0fyzqxh.substring(0,4)+"-"+p0fyzqxh.substring(4,6)+"-01";
		//将mxP0zhouqxh还原成YYYY-MM-DD类型
		mxP0zhouqxh = mxP0zhouqxh.substring(0,4)+"-"+mxP0zhouqxh.substring(4,6)+"-01";
		List list = new ArrayList();
		for(int j=1; j<6; j++) {
			//推算出p0fyzqxh后面的五个日期 
			String resultDate = "";
			try {
				 resultDate = CommonFun.getEndDate(p0fyzqxh, j);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String year = resultDate.substring(0,4);
			String month = resultDate.substring(5,7);
			//再将日期还原成yyyyMM类型
			String tempYear = year + month;
			//将日期加入其准LIST中
			list.add(tempYear);
		}
		Map mxljslMap = new HashMap();
		Class dingdljCs = Dingdlj.class;
		for(int j=1; j<6; j++) {
			//获取反射属性
			PropertyDescriptor pd = new PropertyDescriptor("MxP"+(j-1)+"sl", dingdljCs);
			String resultDate = "";
			try {
				 resultDate = CommonFun.getEndDate(mxP0zhouqxh, j);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String year = resultDate.substring(0,4);
			String month = resultDate.substring(5,7);
			String tempYear = year + month;
			//将年份和获取到的零件值放入MAP中
			mxljslMap.put(tempYear,pd.getReadMethod().invoke(lj));
		}
		
		for(int i=0;i<list.size();i++){
			//把取出的月份数量赋值给p0/p1/p2/p3/p4
			PropertyDescriptor pd2 = new PropertyDescriptor("MxP"+i+"sl", dingdljCs);
			if(mxljslMap.get(list.get(i))==null){
				pd2.getWriteMethod().invoke(lj, BigDecimal.ZERO);
			}else{
				pd2.getWriteMethod().invoke(lj, mxljslMap.get((String)list.get(i)));
			}
			
		
		}
		return lj;
	}
	/**
	 * 比较订单和毛需求的数量
	 * @author 李智
	 * @param xuhao 比较的周期或者周序号
	 * @param jzlx 基准类型
	 * @param dingdlj 订单
	 * @param maoxqmxSl 毛需求数量
	 */
	public void bijiao(int jzlx,Dingdlj lj,BigDecimal mxP0sl,BigDecimal mxP1sl,BigDecimal mxP2sl,BigDecimal mxP3sl,BigDecimal mxP4sl,
			BigDecimal p0sl,BigDecimal p1sl,BigDecimal p2sl,BigDecimal p3sl,BigDecimal p4sl) {
		//根据基准设置基准值、比较值和比例
		if(jzlx == 1) {
			lj.setJizhunSlp0(p0sl);
			lj.setJizhunSlp1(p1sl);
			lj.setJizhunSlp2(p2sl);
			lj.setJizhunSlp3(p3sl);
			lj.setJizhunSlp4(p4sl);
			
			lj.setBijiaoSlp0(mxP0sl);
			lj.setBijiaoSlp1(mxP1sl);
			lj.setBijiaoSlp2(mxP2sl);
			lj.setBijiaoSlp3(mxP3sl);
			lj.setBijiaoSlp4(mxP4sl);
		}
		else {
			lj.setJizhunSlp0(mxP0sl);
			lj.setJizhunSlp1(mxP1sl);
			lj.setJizhunSlp2(mxP2sl);
			lj.setJizhunSlp3(mxP3sl);
			lj.setJizhunSlp4(mxP4sl);
			
			lj.setBijiaoSlp0(p0sl);
			lj.setBijiaoSlp1(p1sl);
			lj.setBijiaoSlp2(p2sl);
			lj.setBijiaoSlp3(p3sl);
			lj.setBijiaoSlp4(p4sl);
		}
	/*	double bijiaop0 = 0;
		//变成百分比比例
		int bili = 100;
		if(lj.getJizhunSlp0().doubleValue()!=0) {
			bijiaop0 = (lj.getBijiaoSlp0().doubleValue()-lj.getJizhunSlp0().doubleValue())/lj.getJizhunSlp0().doubleValue();
		}
		lj.setBijiaop0(new BigDecimal(bijiaop0*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		
		double bijiaop1 = 0;
		if(lj.getJizhunSlp1().doubleValue()!=0) {
			bijiaop1 = (lj.getBijiaoSlp1().doubleValue()-lj.getJizhunSlp1().doubleValue())/lj.getJizhunSlp1().doubleValue();
		}
		lj.setBijiaop1(new BigDecimal(bijiaop1*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		
		double bijiaop2 = 0;
		if(lj.getJizhunSlp2().doubleValue()!=0) {
			bijiaop2 = (lj.getBijiaoSlp2().doubleValue()-lj.getJizhunSlp2().doubleValue())/lj.getJizhunSlp2().doubleValue();
		}
		lj.setBijiaop2(new BigDecimal(bijiaop2*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		
		double bijiaop3 = 0;
		if(lj.getJizhunSlp3().doubleValue()!=0) {
			bijiaop3 = (lj.getBijiaoSlp3().doubleValue()-lj.getJizhunSlp3().doubleValue())/lj.getJizhunSlp3().doubleValue();
		}
		lj.setBijiaop3(new BigDecimal(bijiaop3*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		
		double bijiaop4 = 0;
		if(lj.getJizhunSlp4().doubleValue()!=0) {
			bijiaop4 = (lj.getBijiaoSlp4().doubleValue()-lj.getJizhunSlp4().doubleValue())/lj.getJizhunSlp4().doubleValue();
		}
		lj.setBijiaop4(new BigDecimal(bijiaop4*bili).setScale(0, BigDecimal.ROUND_HALF_UP));*/
		double bijiaop0 = 0;
		//变成百分比比例
		int bili = 100;
		if(lj.getJizhunSlp0().doubleValue()!=0) {
			bijiaop0 = (lj.getBijiaoSlp0().doubleValue()-lj.getJizhunSlp0().doubleValue())/lj.getJizhunSlp0().doubleValue();
		}
		//变成百分比比例
		if(lj.getJizhunSlp0().doubleValue() == 0 && lj.getBijiaoSlp0().doubleValue() != 0)
		{
			lj.setBijiaop0(new BigDecimal(100));
		}
		else
		{
			lj.setBijiaop0(new BigDecimal(bijiaop0*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		
		double bijiaop1 = 0;
		if(lj.getJizhunSlp1().doubleValue()!=0) {
			bijiaop1 = (lj.getBijiaoSlp1().doubleValue()-lj.getJizhunSlp1().doubleValue())/lj.getJizhunSlp1().doubleValue();
		}
		//变成百分比比例
		if(lj.getJizhunSlp1().doubleValue() ==0 && lj.getBijiaoSlp1().doubleValue() != 0)
		{
			lj.setBijiaop1(new BigDecimal(100));
		}
		else
		{
			lj.setBijiaop1(new BigDecimal(bijiaop1*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		
		double bijiaop2 = 0;
		if(lj.getJizhunSlp2().doubleValue()!=0) {
			bijiaop2 = (lj.getBijiaoSlp2().doubleValue()-lj.getJizhunSlp2().doubleValue())/lj.getJizhunSlp2().doubleValue();
		}
		//变成百分比比例
		if(lj.getJizhunSlp2().doubleValue() ==0 && lj.getBijiaoSlp2().doubleValue() != 0)
		{
			lj.setBijiaop2(new BigDecimal(100));
		}
		else
		{
			lj.setBijiaop2(new BigDecimal(bijiaop2*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		
		double bijiaop3 = 0;
		if(lj.getJizhunSlp3().doubleValue()!=0) {
			bijiaop3 = (lj.getBijiaoSlp3().doubleValue()-lj.getJizhunSlp3().doubleValue())/lj.getJizhunSlp3().doubleValue();
		}
		//变成百分比比例
		if(lj.getJizhunSlp3().doubleValue()==0 && lj.getBijiaoSlp3().doubleValue() != 0)
		{
			lj.setBijiaop3(new BigDecimal(100));
		}
		else
		{
			lj.setBijiaop3(new BigDecimal(bijiaop3*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		
		double bijiaop4 = 0;
		if(lj.getJizhunSlp4().doubleValue()!=0) {
			bijiaop4 = (lj.getBijiaoSlp4().doubleValue()-lj.getJizhunSlp4().doubleValue())/lj.getJizhunSlp4().doubleValue();
		}
		//变成百分比比例
		if(lj.getJizhunSlp4().doubleValue() ==0 && lj.getBijiaoSlp4().doubleValue() != 0)
		{
			lj.setBijiaop4(new BigDecimal(100));
		}
		else
		{
			lj.setBijiaop4(new BigDecimal(bijiaop4*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
	}
	
	/**
	 * 订单和订单比较
	 * @author 李智
	 * @param dingd1 基准订单
	 * @param dingd2 比较订单
	 * return List<Dingdlj> 比较的结果
	 */
	public Map<String, Object> dingdBijiaoByDingd(Dingd dingd,Dingd dingd1,Dingd dingd2,LoginUser loginUser,Map tjMap) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("dingdjssj", dingd1.getDingdjssj());
		//取订单零件处理类型
		List dingdljList = (List)this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingdljZq",param);
		Dingdlj dingdlj = (Dingdlj)dingdljList.get(0);
		//设置2个比较订单的订单时间
		param = new HashMap<String, String>();
		param.put("dingdjssj1", dingd1.getDingdjssj());
		param.put("dingdjssj2", dingd2.getDingdjssj());
		param.put("lingjbh", dingd.getLingjbh());
		param.put("gongysdm", dingd.getGongysdm());
		List<Dingdlj> ljList = new ArrayList<Dingdlj>();
		Map<String, Object> ljmap = new HashMap<String, Object>();
		
		//根据处理类型取不同的比较结果
		if(dingdlj.getChullx().equals(Const.PP)||dingdlj.getChullx().equals(Const.NP)) {
			//PP类型比较
			ljmap = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.dingdBijiaoByDingd_PP",param,dingd);
		} else if(dingdlj.getChullx().equals(Const.PS)||dingdlj.getChullx().equals(Const.NS)) {
			//PS类型比较
			ljmap = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.dingdBijiaoByDingd_PS",param,dingd);
		} else if(dingdlj.getChullx().equals(Const.PJ)||dingdlj.getChullx().equals(Const.NJ)) {
			//PJ类型比较
			ljmap = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.dingdBijiaoByDingd_PJ",param,dingd);
		}
		ljList = (List<Dingdlj>)ljmap.get("rows");
		
		for(int i=0; i<ljList.size(); i++) {
			Dingdlj lj = ljList.get(i);
			//当比较订单零件在基准订单零件中不存在的时候
			if(null == lj.getLingjbh() || "".equals(lj.getLingjbh())) {
				//把比较零件的信息放到基准订单中方便页面显示
				lj.setLingjbh(lj.getLingjbh2());			//零件编号
				lj.setUsercenter(lj.getUsercenter2());		//用户中心
				lj.setZhongwmc(lj.getZhongwmc2());			//中文名称
				lj.setGongysdm(lj.getGongysdm2());			//供应商编码
				lj.setZhizlx(lj.getZhizxl2());				//制造线路
				lj.setDanw(lj.getDanw2());					//单位
				lj.setP0fyzqxh(lj.getP0fyzqxh2());
				lj.setP1rq(lj.getP1rq2());
				lj.setP2rq(lj.getP2rq2());
				lj.setP3rq(lj.getP3rq2());
				lj.setP4rq(lj.getP4rq2());
				
				bijiao(lj);
			}
			//当基准订单零件在比较订单零件中不存在的时候
			else if(null == lj.getLingjbh2() || "".equals(lj.getLingjbh2())) {
				bijiao(lj);
			}
			//都存在的时候
			else {
				bijiao(lj);
			}
		}
		return ljmap;
	}
	public List<Dingdlj> expDingdBijiaoByDingd(Dingd dingd1,Dingd dingd2,LoginUser loginUser) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("dingdjssj", dingd1.getDingdjssj());
		//取订单零件处理类型
		List dingdljList = (List)this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingdljZq",param);
		Dingdlj dingdlj = (Dingdlj)dingdljList.get(0);
		//设置2个比较订单的订单时间
		param = new HashMap<String, String>();
		param.put("dingdjssj1", dingd1.getDingdjssj());
		param.put("dingdjssj2", dingd2.getDingdjssj());
		List<Dingdlj> ljList = new ArrayList<Dingdlj>();
		Map<String, Object> ljmap = new HashMap<String, Object>();
		
		//根据处理类型取不同的比较结果
		if(dingdlj.getChullx().equals(Const.PP)||dingdlj.getChullx().equals(Const.NP)) {
			//PP类型比较
			ljList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.dingdBijiaoByDingd_PP",param);
		} else if(dingdlj.getChullx().equals(Const.PS)||dingdlj.getChullx().equals(Const.NS)) {
			//PS类型比较
			ljList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.dingdBijiaoByDingd_PS",param);
		} else if(dingdlj.getChullx().equals(Const.PJ)||dingdlj.getChullx().equals(Const.NJ)) {
			//PJ类型比较
			ljList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.dingdBijiaoByDingd_PJ",param);
		}
		for(int i=0; i<ljList.size(); i++) {
			Dingdlj lj = ljList.get(i);
			//当比较订单零件在基准订单零件中不存在的时候
			if(null == lj.getLingjbh() || "".equals(lj.getLingjbh())) {
				//把比较零件的信息放到基准订单中方便页面显示
				lj.setLingjbh(lj.getLingjbh2());			//零件编号
				lj.setUsercenter(lj.getUsercenter2());		//用户中心
				lj.setZhongwmc(lj.getZhongwmc2());			//中文名称
				lj.setGongysdm(lj.getGongysdm2());			//供应商编码
				lj.setZhizlx(lj.getZhizxl2());				//制造线路
				lj.setDanw(lj.getDanw2());					//单位
				lj.setP0fyzqxh(lj.getP0fyzqxh2());
				lj.setP1rq(lj.getP1rq2());
				lj.setP2rq(lj.getP2rq2());
				lj.setP3rq(lj.getP3rq2());
				lj.setP4rq(lj.getP4rq2());
				
				bijiao(lj);
			}
			//当基准订单零件在比较订单零件中不存在的时候
			else if(null == lj.getLingjbh2() || "".equals(lj.getLingjbh2())) {
				bijiao(lj);
			}
			//都存在的时候
			else {
				bijiao(lj);
			}
		}
		return ljList;
	}
	/**
	 * 订单和订单比较
	 * @param lj
	 */
	public void bijiao(Dingdlj lj) {
		//比较订单零件的周期/周/日期和数量组成map
		Map<String,BigDecimal> ljslMap2 = new HashMap<String,BigDecimal>();
		//p0
		ljslMap2.put(lj.getP0fyzqxh2(), lj.getP0sl2());
		//p1
		ljslMap2.put(lj.getP1rq2(), lj.getP1sl2());
		//p2
		ljslMap2.put(lj.getP2rq2(), lj.getP2sl2());
		//p3
		ljslMap2.put(lj.getP3rq2(), lj.getP3sl2());
		//p4
		ljslMap2.put(lj.getP4rq2(), lj.getP4sl2());
		
		lj.setJizhunSlp0(lj.getP0sl());
		lj.setBijiaoSlp0(ljslMap2.get(lj.getP0fyzqxh())==null?BigDecimal.ZERO:ljslMap2.get(lj.getP0fyzqxh()));
			
		lj.setJizhunSlp1(lj.getP1sl());
		lj.setBijiaoSlp1(ljslMap2.get(lj.getP1rq())==null?BigDecimal.ZERO:ljslMap2.get(lj.getP1rq()));
		
		lj.setJizhunSlp2(lj.getP2sl());
		lj.setBijiaoSlp2(ljslMap2.get(lj.getP2rq())==null?BigDecimal.ZERO:ljslMap2.get(lj.getP2rq()));
		
		lj.setJizhunSlp3(lj.getP3sl());
		lj.setBijiaoSlp3(ljslMap2.get(lj.getP3rq())==null?BigDecimal.ZERO:ljslMap2.get(lj.getP3rq()));
		
		lj.setJizhunSlp4(lj.getP4sl());
		lj.setBijiaoSlp4(ljslMap2.get(lj.getP4rq())==null?BigDecimal.ZERO:ljslMap2.get(lj.getP4rq()));
		
		double bijiaop0 = 0;
		//变成百分比比例
		int bili = 100;
		if(lj.getJizhunSlp0().doubleValue()!=0) {
			bijiaop0 = (lj.getBijiaoSlp0().doubleValue()-lj.getJizhunSlp0().doubleValue())/lj.getJizhunSlp0().doubleValue();
		}
		//变成百分比比例
		if(lj.getJizhunSlp0().doubleValue() == 0 && lj.getBijiaoSlp0().doubleValue() != 0)
		{
			lj.setBijiaop0(new BigDecimal(100));
		}
		else
		{
			lj.setBijiaop0(new BigDecimal(bijiaop0*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		
		double bijiaop1 = 0;
		if(lj.getJizhunSlp1().doubleValue()!=0) {
			bijiaop1 = (lj.getBijiaoSlp1().doubleValue()-lj.getJizhunSlp1().doubleValue())/lj.getJizhunSlp1().doubleValue();
		}
		//变成百分比比例
		if(lj.getJizhunSlp1().doubleValue() ==0 && lj.getBijiaoSlp1().doubleValue() != 0)
		{
			lj.setBijiaop1(new BigDecimal(100));
		}
		else
		{
			lj.setBijiaop1(new BigDecimal(bijiaop1*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		
		double bijiaop2 = 0;
		if(lj.getJizhunSlp2().doubleValue()!=0) {
			bijiaop2 = (lj.getBijiaoSlp2().doubleValue()-lj.getJizhunSlp2().doubleValue())/lj.getJizhunSlp2().doubleValue();
		}
		//变成百分比比例
		if(lj.getJizhunSlp2().doubleValue() ==0 && lj.getBijiaoSlp2().doubleValue() != 0)
		{
			lj.setBijiaop2(new BigDecimal(100));
		}
		else
		{
			lj.setBijiaop2(new BigDecimal(bijiaop2*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		
		double bijiaop3 = 0;
		if(lj.getJizhunSlp3().doubleValue()!=0) {
			bijiaop3 = (lj.getBijiaoSlp3().doubleValue()-lj.getJizhunSlp3().doubleValue())/lj.getJizhunSlp3().doubleValue();
		}
		//变成百分比比例
		if(lj.getJizhunSlp3().doubleValue()==0 && lj.getBijiaoSlp3().doubleValue() != 0)
		{
			lj.setBijiaop3(new BigDecimal(100));
		}
		else
		{
			lj.setBijiaop3(new BigDecimal(bijiaop3*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		
		double bijiaop4 = 0;
		if(lj.getJizhunSlp4().doubleValue()!=0) {
			bijiaop4 = (lj.getBijiaoSlp4().doubleValue()-lj.getJizhunSlp4().doubleValue())/lj.getJizhunSlp4().doubleValue();
		}
		//变成百分比比例
		if(lj.getJizhunSlp4().doubleValue() ==0 && lj.getBijiaoSlp4().doubleValue() != 0)
		{
			lj.setBijiaop4(new BigDecimal(100));
		}
		else
		{
			lj.setBijiaop4(new BigDecimal(bijiaop4*bili).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
	}

	/**
	 * @param bean
	 * @param maoxq
	 * @param i
	 * @param userInfo
	 * @param dingdjssj
	 * @return
	 */
	public Map<String, Object> dingdBijiaoByMaoxqDownload(Dingd bean,
			Maoxq maoxq, int jzlx, LoginUser loginUser, String dingdjssj,Map tjMap) {
		Map<String, String> param = new HashMap<String, String>();
		String tempDingdjssj = "";
		if(dingdjssj!=null&&!"".equals(dingdjssj)){
			String tempDingdjssjSub1 = dingdjssj.substring(0,10);
			String tempDingdjssjSub2 = dingdjssj.substring(11);
			tempDingdjssj = tempDingdjssjSub1+" "+tempDingdjssjSub2;
		}
		param.put("dingdjssj",tempDingdjssj);
		String lingjbh = "";
		if(tjMap.get("lingjbh")!=null){
			lingjbh = (String)tjMap.get("lingjbh");
		}
		param.put("lingjbh", lingjbh);
		String gongysdm = "";
		if(tjMap.get("gongysdm")!=null){
			gongysdm = (String)tjMap.get("gongysdm");
		}
		param.put("gongysdm", gongysdm);
		//取订单零件处理类型,资源获取日期
		List dingdljList = (List)this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingdljZq",param);
		Dingdlj dingdlj = (Dingdlj)dingdljList.get(0);
		//设置查询条件
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", loginUser.getUsercenter());
		map.put("riq", dingdlj.getZiyhqrq());
		//得到资源获取日期的年周期和周
		CalendarCenter calendarCenter = calendarCenterService.queryCalendarCenterObject(map);
		
		//设置比较条件
		Map<String, Object> params = new HashMap<String, Object>();
		//资源获取日期
		params.put("ziyhqrq", dingdlj.getZiyhqrq());
		//资源获取日期的所属周期
		params.put("ziyhqZq", calendarCenter.getNianzq());
		//需求版次
		params.put("xuqbc", maoxq.getXuqbc());
		params.put("dingdjssj",tempDingdjssj);
		//PP处理类型
		Map<String,Object> ljmap = new HashMap<String,Object>();
		List<Dingdlj> ljList = new ArrayList<Dingdlj>();
		if(dingdlj.getChullx().equals(Const.PP)||dingdlj.getChullx().equals(Const.NP)) {
			//PP类型的比较查询
			ljList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.dingdBijiaoByMaoxq_PP", params);
		}
		else if(dingdlj.getChullx().equals(Const.PS)||dingdlj.getChullx().equals(Const.NS)) {
			//PS类型的比较查询
			ljList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.dingdBijiaoByMaoxq_PS",params);
		}
		else if(dingdlj.getChullx().equals(Const.PJ)||dingdlj.getChullx().equals(Const.NJ)) {
			//PS类型的比较查询
			ljList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.dingdBijiaoByMaoxq_PJ",params);
		}
		for(int i=0; i<ljList.size(); i++) {
			Dingdlj lj = ljList.get(i);
			//当订单零件在明细零件中不存在的时候
			if(null==lj.getMxLingjbh() || "".equals(lj.getMxLingjbh())) {
				bijiao(jzlx, lj, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
						lj.getP0sl(), lj.getP1sl(), lj.getP2sl(), lj.getP3sl(), lj.getP4sl());
			}
			//当明细零件在订单零件中不存在的时候
			else if(null==lj.getLingjbh() || "".equals(lj.getLingjbh())) {
				//把明细零件的信息放到订单中方便页面显示
				lj.setLingjbh(lj.getMxLingjbh());			//零件编号
				lj.setUsercenter(lj.getMxUsercenter());		//用户中心
				lj.setZhongwmc(lj.getMxZhongwmc());			//中文名称
				lj.setGongysdm(lj.getMxGongysbh());			//供应商编码
				lj.setZhizlx(lj.getMxZhizlx());				//制造线路
				lj.setDanw(lj.getMxDanw());					//单位
				
				bijiao(jzlx, lj, lj.getMxP0sl(), lj.getMxP1sl(), lj.getMxP2sl(), lj.getMxP3sl(), lj.getMxP4sl(), 
						BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
			}
			else {
				bijiao(jzlx, lj, lj.getMxP0sl(), lj.getMxP1sl(), lj.getMxP2sl(), lj.getMxP3sl(), lj.getMxP4sl(), 
						lj.getP0sl(), lj.getP1sl(), lj.getP2sl(), lj.getP3sl(), lj.getP4sl());
			}
		}
		ljmap.put("rows", ljList);
		return ljmap;
	}

	/**
	 * @param bean
	 * @param dingd1
	 * @param dingd2
	 * @param userInfo
	 * @return
	 */
	public Map<String, Object> dingdBijiaoByDingdDownload(Dingd bean, Dingd dingd1,
			Dingd dingd2, LoginUser userInfo,Map tjMap) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("dingdjssj", dingd1.getDingdjssj());
		//取订单零件处理类型
		List dingdljList = (List)this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDingdljZq",param);
		Dingdlj dingdlj = (Dingdlj)dingdljList.get(0);
		//设置2个比较订单的订单时间
		param = new HashMap<String, String>();
		param.put("dingdjssj1", dingd1.getDingdjssj());
		param.put("dingdjssj2", dingd2.getDingdjssj());
		List<Dingdlj> ljList = new ArrayList<Dingdlj>();
		Map<String, Object> ljmap = new HashMap<String, Object>();
		String lingjbh = "";
		if(tjMap.get("lingjbh")!=null){
			lingjbh = (String)tjMap.get("lingjbh");
		}
		param.put("lingjbh", lingjbh);
		String gongysdm = "";
		if(tjMap.get("gongysdm")!=null){
			gongysdm = (String)tjMap.get("gongysdm");
		}
		param.put("gongysdm", gongysdm);
		//根据处理类型取不同的比较结果
		if(dingdlj.getChullx().equals(Const.PP)||dingdlj.getChullx().equals(Const.NP)) {
			//PP类型比较
			ljList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.dingdBijiaoByNiandyg",param);
			
			ljList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.dingdBijiaoByDingd_PP",param);
		} else if(dingdlj.getChullx().equals(Const.PS)||dingdlj.getChullx().equals(Const.NS)) {
			//PS类型比较
			ljList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.dingdBijiaoByDingd_PS",param);
		} else if(dingdlj.getChullx().equals(Const.PJ)||dingdlj.getChullx().equals(Const.NJ)) {
			//PJ类型比较
			ljList = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.dingdBijiaoByDingd_PJ",param);
		}
		
		for(int i=0; i<ljList.size(); i++) {
			Dingdlj lj = ljList.get(i);
			//当比较订单零件在基准订单零件中不存在的时候
			if(null == lj.getLingjbh() || "".equals(lj.getLingjbh())) {
				//把比较零件的信息放到基准订单中方便页面显示
				lj.setLingjbh(lj.getLingjbh2());			//零件编号
				lj.setUsercenter(lj.getUsercenter2());		//用户中心
				lj.setZhongwmc(lj.getZhongwmc2());			//中文名称
				lj.setGongysdm(lj.getGongysdm2());			//供应商编码
				lj.setZhizlx(lj.getZhizxl2());				//制造线路
				lj.setDanw(lj.getDanw2());					//单位
				lj.setP0fyzqxh(lj.getP0fyzqxh2());
				lj.setP1rq(lj.getP1rq2());
				lj.setP2rq(lj.getP2rq2());
				lj.setP3rq(lj.getP3rq2());
				lj.setP4rq(lj.getP4rq2());
				
				bijiao(lj);
			}
			//当基准订单零件在比较订单零件中不存在的时候
			else if(null == lj.getLingjbh2() || "".equals(lj.getLingjbh2())) {
				bijiao(lj);
			}
			//都存在的时候
			else {
				bijiao(lj);
			}
		}
		ljmap.put("rows", ljList);
		return ljmap;
	}
}