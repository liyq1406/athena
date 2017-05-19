package com.athena.xqjs.module.anxorder.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.anxorder.AnxMaoxqzjb;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.entity.anxorder.GongysFeneJis;
import com.athena.xqjs.entity.anxorder.Kucjscsb;
import com.athena.xqjs.entity.anxorder.Ticxxsj;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Tidj;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiaohcyssk;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.common.Yunssk;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 按需计算Service
 * @author WL
 *
 */
@WebService(endpointInterface="com.athena.xqjs.module.anxorder.service.AnxJsInter", serviceName="/anxOrderService",
		targetNamespace="http://service.anxorder.module.xqjs.athena.com/")
@Component
public class AnxJisService extends BaseService implements AnxJsInter {
	
	/**
	 * 异常报警Service
	 */
	@Inject
	private YicbjService yicbjService;
	
	/**
	 * IL订单service
	 */
	@Inject
	private IlOrderService ilOrderService;
	
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;
	
	//新按需service
	@Inject 
	private XinaxJisService xqjsXaxClient;
	
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(AnxJisService.class);
	
	/**
	 * 按需计算
	 * @param 操作人
	 * @throws Exception 
	 */
	public void anxOrderMethod(String user,String usercenter) throws ServiceException{
		String dingdjssj = CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS);//订单计算时间
		/**
		 * 开始计算,计算时间
		 */
		logger.info("按需计算开始------操作人:"+user+",用户中心:"+usercenter+"------订单计算时间:"+dingdjssj);
		Map<String,String> map = new HashMap<String, String>();
		map.put("jiscldm", Const.JISMK_ANX_CD);//计算处理代码：按需订单计算(33)
		map.put("usercenter", usercenter);//用户中心
		try {
			//判断处理状态,是否有计算进行中	
			if(jiscclssz.checkState(map)){
				logger.info("按需计算有计算正在进行中------操作人:"+user+"------订单计算时间:"+dingdjssj);
				throw new ServiceException("按需计算异常:按需计算有计算正在进行中------操作人:"+user+"------订单计算时间:"+dingdjssj);
			}else{
				//更新处理状态为1,计算中
				jiscclssz.updateState(map,Const.JSZT_EXECUTING);
				
				/**
				 * 初始化参考系参数,加载到缓存MAP
				 */
				long start = System.currentTimeMillis();
				Map<String, List<Tidj>> tidjMap = new HashMap<String, List<Tidj>>();
				Map<String, List<Ticxxsj>> ticxxsjMap = new HashMap<String, List<Ticxxsj>>();
				Map<String,Xiehzt> xiehztMap = new HashMap<String, Xiehzt>();
				Map<String,BigDecimal> lingjSlMap = new HashMap<String, BigDecimal>();
				Map<String,GongysFeneJis> gongysFeneMap = new HashMap<String, GongysFeneJis>();
				Map<String,List<LingjGongys>> lingjgysMap = new HashMap<String, List<LingjGongys>>();
				Map<String,Wullj> wulljMap = new HashMap<String, Wullj>();
				HashMap<String, String> dingdhMap = new HashMap<String, String>();
				
				//查询替代件信息
				List<Tidj> tidjList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTidj",map);
				//组装替代件信息数据map
				for (int i = 0; i < tidjList.size(); i++) {
					Tidj tidj = tidjList.get(i);
					//key=用户中心+零件号
					String key = tidj.getUsercenter()+tidj.getLingjbh();
					//如果已经包含该用户中心,零件号的替代件信息,添加
					if(tidjMap.containsKey(key)){
						tidjMap.get(key).add(tidj);
					}else{//如果没有,新建该用户中心,零件号替代件信息集合
						List<Tidj> tidjs = new ArrayList<Tidj>();
						tidjs.add(tidj);
						tidjMap.put(key,tidjs);
					}
				}
				//释放内存
				tidjList = null;
				
				//查询未来几日剔除休息时间
				List<Ticxxsj> ticxxsjList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTicxxsj",map);
				//组装数据
				for (int i = 0; i < ticxxsjList.size(); i++) {
					Ticxxsj ticxxsj = ticxxsjList.get(i);
					//key=用户中心+产线仓库+工作日
					String key = ticxxsj.getUsercenter()+ticxxsj.getChanxck()+ticxxsj.getGongzr();
					//已经存在,添加
					if(ticxxsjMap.containsKey(key)){
						ticxxsjMap.get(key).add(ticxxsj);
					//不存在,新建
					}else{
						List<Ticxxsj> ticxxsjs = new ArrayList<Ticxxsj>();
						ticxxsjs.add(ticxxsj);
						ticxxsjMap.put(key, ticxxsjs);
					}
				}
				//释放内存
				ticxxsjList = null;
				
				//查询卸货站台
				List<Xiehzt> xiehztList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryXiehztObject",map);
				//组装数据
				for (int i = 0; i < xiehztList.size(); i++) {
					Xiehzt xiehzt = xiehztList.get(i);
					xiehztMap.put(xiehzt.getUsercenter()+xiehzt.getXiehztbh(), xiehzt);
				}
				//释放内存
				xiehztList = null;
				
				/**
				 * 零件供应商集合
				 */
				List<LingjGongys> lingjgysList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryLingjGysList",map);
				for (int i = 0; i < lingjgysList.size(); i++) {
					LingjGongys lingjgys = lingjgysList.get(i);
					String key = lingjgys.getUsercenter() + lingjgys.getLingjbh();
					//已经存在,添加
					if(lingjgysMap.containsKey(key)){
						lingjgysMap.get(key).add(lingjgys);
					//不存在,新建
					}else{
						List<LingjGongys> lingjgyss = new ArrayList<LingjGongys>();
						lingjgyss.add(lingjgys);
						lingjgysMap.put(key, lingjgyss);
					}
				}
				lingjgysList = null;
				
				/**
				 * 物流路径集合
				 */
				List<Wullj> wulljList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryWulljOnly",map);
				for (int i = 0; i < wulljList.size(); i++) {
					Wullj wullj = wulljList.get(i);
					String key = wullj.getUsercenter() + wullj.getLingjbh() + wullj.getGongysbh() + wullj.getFenpqh();
					wulljMap.put(key, wullj);
				}
				wulljList = null;	
				
				logger.info("加载按需计算参数结束,耗时---------------------"+(System.currentTimeMillis() - start));
				/**
				 * 更新线边理论库存
				 */
				if(Const.WTC_CENTER_UW.equals(map.get("usercenter"))){
					map.put("dingdh", Const.ANX_UW_DINGDH);
				}else{
					map.put("dingdh", Const.ANX_UL_DINGDH);
				}
				//上次计算时间
				Dingd dingd = (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryAxScjssj",map);//上次计算时间
				map.remove("dingdh");
				logger.info("按需计算更新线边理论库存开始------时间:"+CommonFun.getJavaTime()+"上次计算时间---"+dingd.getDingdjssj()
						+"当前资源文件时间---"+dingd.getDingdsxsj());
				updateXbkc(map, user, dingd);
				logger.info("按需计算更新线边理论库存结束------时间:"+CommonFun.getJavaTime());
				
				logger.info("按需计算中间表生成完毕--------时间"+CommonFun.getJavaTime());
				/**
				 * 数据准备,生成中间表
				 */
				dataPreparation(user,map,xiehztMap);
				logger.info("按需计算中间表生成完毕--------时间"+CommonFun.getJavaTime());
				/**
				 *进行CDMD模式计算 ,计算类型为1
				 */
				logger.info("按需计算CDMD部分计算开始------时间:"+CommonFun.getJavaTime());
				anxCalculate(dingdjssj, user,"1",map,dingd.getDingdsxsj(), tidjMap, ticxxsjMap,xiehztMap,lingjSlMap,lingjgysMap,dingdhMap,wulljMap,gongysFeneMap);
				logger.info("按需计算CDMD部分计算结束------时间:"+CommonFun.getJavaTime());
				
				/**
				 * 进行C1M1模式计算计算类型为2
				 */
				logger.info("按需计算C1M1部分计算开始------时间:"+CommonFun.getJavaTime());
				anxCalculate(dingdjssj, user,"2",map,dingd.getDingdsxsj(), tidjMap, ticxxsjMap,xiehztMap,lingjSlMap,lingjgysMap,dingdhMap,wulljMap,gongysFeneMap);
				logger.info("按需计算C1M1部分计算结束------时间:"+CommonFun.getJavaTime());
				
				/**
				 * 更新订单态
				 */
				logger.info("更新订单状态");
				
				/** hzg 2015.5.9 mantis:0011506 start */
				//查询按需自动下发状态,如果为Y或"",表示自动下发,如果为N,表示手动下发
				String handState = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryAnxHandState",usercenter));
				logger.info("按需手动下发-----状态："+handState);
				if(handState.equals("Y")||"".equals(handState)){
					updateDingd(dingdjssj, Const.DINGD_STATUS_YSX, user,"'"+Const.DINGD_LX_ANX_ZC_C+"','"+Const.DINGD_LX_ANX_ZC_M+"'",map);
				}
				/** hzg 2015.5.9 mantis:0011506  end */
				//计算完成更新处理状态为0,计算完成
				jiscclssz.updateState(map,Const.JSZT_SURE);
				
				
				
				//增加计算结果对比表 0008573: 按需计算结果分析日报表
			//	makeReport(usercenter);
				logger.info("按需计算结束------操作人:"+user+"------时间:"+CommonFun.getJavaTime());
			}
		} catch (Exception e) {
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
			logger.error("按需计算异常"+e);
			throw new ServiceException("按需计算异常"+e);
		}
	}
	
	/**
	 * update线边库存
	 * @param map 数据集合
	 * @param user 用户
	 * @param dingd 订单信息
	 */
	@Transactional
	public void updateXbkc(Map<String,String> map,String user,Dingd dingd){
		
		//查询所以CDMD的零件消耗点集合
		List<Lingjxhd> listLingjxhd = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryXbkcXhd",map);
		List<Lingjxhd> updateLingjxhd = new ArrayList<Lingjxhd>();
		//遍历零件消耗点集合
		for (int i = 0; i < listLingjxhd.size(); i++) {
			Lingjxhd lingjXhd = listLingjxhd.get(i);
			//更新线边理论库存
			lingjXhd = updateXbllkc(user, dingd.getDingdjssj(), dingd.getDingdsxsj(), lingjXhd);
			updateLingjxhd.add(lingjXhd);
		}
		updateShengxsj(user, map, dingd.getDingdsxsj());
		//更新线边理论库存
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("common.updateLingjxhd", updateLingjxhd);
	}
	
	/**
	 * 按需计算方法
	 * @param dingdjssj 订单计算时间
	 * @param user 操作用户
	 * @param jislx 计算类型 1:CDMD模式计算;2:C1M1模式计算
	 * @param ziywjsj 资源文件时间
	 * @throws ParseException
	 */
	@Transactional
	public void anxCalculate(String dingdjssj,String user,String jislx,Map<String,String> map,String ziywjsj,
			Map<String, List<Tidj>> tidjMap,Map<String, List<Ticxxsj>> ticxxsjMap,Map<String,Xiehzt> xiehztMap,Map<String,BigDecimal> lingjSlMap,
			Map<String,List<LingjGongys>> lingjgysMap,HashMap<String, String> dingdhMap,Map<String,Wullj> wulljMap,Map<String,GongysFeneJis> gongysFeneMap) throws ParseException{
		Map<String,String> param = new HashMap<String,String>();
		List<Dingdlj> listDingdlj = new ArrayList<Dingdlj>();//订单零件集合
		List<Dingdmx> listDingdmx = new ArrayList<Dingdmx>();//订单明细集合
		Map<String,String> sppvxhsj = new HashMap<String,String>();
		/**
		 * 根据计算类型进行遍历计算
		 */
		List<Anxjscszjb> listAnxjscszjb = new ArrayList<Anxjscszjb>();
		//计算类型1,CDMD模式计算,按用户中心,零件编号,消耗点进行遍历
		if("1".equals(jislx)){
			listAnxjscszjb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryAnxjscszjbCDMD",map);
			logger.info("按需CDMD计算,零件消耗点数量:"+listAnxjscszjb.size());
		}else{
			//计算类型2,C1M1模式计算,按用户中心,零件编号,线边仓库进行遍历
			listAnxjscszjb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryAnxjscszjbC1M1",map);
			logger.info("按需C1M1计算,零件仓库数量:"+listAnxjscszjb.size());
		}
		//遍历计算中间表集合
		for (int i = 0; i < listAnxjscszjb.size(); i++) {
			Anxjscszjb anxjscszjb = listAnxjscszjb.get(i);
			
			/**
			 * 模式判断
			 */
			String mos = "";
			String gongysdm = "";//供应商代码
			String gongyslx = "";//供应商类型
			String dingdlx = "";//订单类型
			Date shengxrq = null;//将来模式生效日期
			if (Const.ANX_MS_CD.equalsIgnoreCase(anxjscszjb.getMos()) || Const.ANX_MS_C1.equalsIgnoreCase(anxjscszjb.getMos())) {
				mos = anxjscszjb.getMos();
				gongysdm = anxjscszjb.getGongysbh();//C1CD模式供应商为供应商
				gongyslx = anxjscszjb.getGongyslx();//供应商类型为供应商类型
				dingdlx = Const.DINGD_LX_ANX_ZC_C;//订单类型为 按需正常C
				//将来模式生效日期不为空
				if(!StringUtils.isEmpty(anxjscszjb.getShengxsj())){
					shengxrq = CommonFun.sdfAxparse(anxjscszjb.getShengxsj());
				}		
			} else {
				mos = anxjscszjb.getMos2();
				gongysdm = anxjscszjb.getDinghck();//M1MD模式供应商为订货库
				gongyslx = anxjscszjb.getDinghcklx();//供应商类型为订货库类型
				dingdlx = Const.DINGD_LX_ANX_ZC_M;//订单类型为 按需正常M
				//将来模式生效日期不为空
				if(!StringUtils.isEmpty(anxjscszjb.getShengxsj())){
					//生效时间2为空，跳过计算 by CSY 20170309
					if (StringUtils.isEmpty(anxjscszjb.getShengxsj2())) {
						logger.error("logAnx002在供货模式："+mos+"下，零件："+anxjscszjb.getLingjbh()+"在用户中心："+anxjscszjb.getUsercenter()+"下分配区:"+anxjscszjb.getFenpbh()+"  路径编号："+anxjscszjb.getLujbh()+"的物流路径  生效时间有值但生效时间2为空，跳过该零件的计算");
						this.saveYicbj(anxjscszjb, user, "分配区:"+anxjscszjb.getFenpbh()+"  路径编号:"+anxjscszjb.getLujbh()+"的物流路径  生效时间有值，但生效时间2为空，跳过该零件的计算", mos, anxjscszjb.getJihy());
						continue;
					}
					shengxrq = CommonFun.sdfAxparse(anxjscszjb.getShengxsj2());
				}
			}
			
			/**
			 * 计算封闭期
			 */
			int day = getJisDate(anxjscszjb);//封闭期天数
			//如果封闭期天数计算有误,跳过当前
			if(day < 0){
				continue;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
			}
			param.put("usercenter", anxjscszjb.getUsercenter());//用户中心
			if (Const.ANX_MS_C1.equalsIgnoreCase(mos) || Const.ANX_MS_M1.equalsIgnoreCase(mos)) {
				param.put("shengcxbh", anxjscszjb.getXianbck());//线边仓库
			}else{
				param.put("shengcxbh", anxjscszjb.getShengcxbh());//生产线
			}	
			param.put("gongzr", dingdjssj.substring(0,10));//当前日期
			//判断订单计算时间是否工作日
			List<Ticxxsj> dingdjssjTicxxsj = ticxxsjMap.get(anxjscszjb.getUsercenter()+param.get("shengcxbh")+dingdjssj.substring(0,10));
			//取不到为非工作日
			if(dingdjssjTicxxsj == null || dingdjssjTicxxsj.isEmpty()){
				day++;
			}
			param.put("num", String.valueOf(day));
			List<Ticxxsj> listJsrq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryTicxxsjGzrNum",param);//查询封闭期日期
			//工作日历中没有相关的维护，系统给出报 警，不进行计算,day++之后还是非工作日，这里就给出报警
			if(listJsrq == null || listJsrq.isEmpty() || day > listJsrq.size()+1){
				saveYicbj(anxjscszjb, user, dingdjssj+"工作日历为空",mos);
				continue;
			}
			//封闭期后计算日期,推最晚到货时间在计算日期工作时间内的才生成订单明细
			String jisrq = listJsrq.get(day - 1).getGongzr();
			
			param.put("lingjbh", anxjscszjb.getLingjbh()); 
			param.put("jisrq", jisrq);
			
			/**
			 * 取计算日期剔除休息时间
			 */
			List<Ticxxsj> listTicxxsj = ticxxsjMap.get(anxjscszjb.getUsercenter()+param.get("shengcxbh")+jisrq);
			if(listTicxxsj == null){
				saveYicbj(anxjscszjb, user, jisrq+"封闭期工作日历为空",mos);
				continue;
			}
			String kaibsj = listTicxxsj.get(0).getRiq() + " " +listTicxxsj.get(0).getShijdkssj();//计算日期开班时间
			String xiabsj = listTicxxsj.get(listTicxxsj.size() - 1).getRiq() + " " + listTicxxsj.get(listTicxxsj.size() - 1).getShijdjssj();//计算日期下班时间
			
			param.put("fengbqrq", kaibsj);
			
			//如果将来模式生效日期不为空,判断到货时间是否晚于生效日期,如果晚于表示模式已经切换,不要货，将来生效日期不为空表示模式切换。
			if(shengxrq != null && !CommonFun.sdfAxparse(jisrq).before(shengxrq)){
				saveYicbj(anxjscszjb, user, jisrq+"将来模式生效日期判断,该零件模式切换不计算",mos);
				continue;
			}
			///0010991 按需计算sppv需求 截止点采用当天开班时间，目前逻辑有问题
			String sppvsjcxck = param.get("usercenter")+param.get("shengcxbh");
			if(!(sppvxhsj.get(sppvsjcxck) != null && sppvxhsj.get(sppvsjcxck).length()>0)){
				List<Ticxxsj> listsppvxhsj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryTicxxsjGzrSppvNum",param);//查询封闭期日期
				if(listsppvxhsj!=null && listsppvxhsj.size()>0){
					sppvxhsj.put(sppvsjcxck, listsppvxhsj.get(0).getGongzr() + " "+ listsppvxhsj.get(0).getShijdkssj());
				}else{
					saveYicbj(anxjscszjb, user, sppvsjcxck+" sppv消耗时间工作日历为空",mos);
					continue;
				}
			}
			param.put("xiaohsjsppv", sppvxhsj.get(sppvsjcxck));
			/**
			 * 计算初始资源
			 */
			BigDecimal ziy = resourceCalculation(anxjscszjb, mos, user, dingdjssj, kaibsj,ziywjsj,tidjMap,param);
			if(ziy == null){
				continue;
			}
			/**
			 * 初始资源为负数,需生成要货
			 */
			if(ziy.signum() < 0){
				BigDecimal yaohl = ziy.abs();//要货量为将资源补零
				String time = kaibsj.substring(0,16);
				//如果将来模式生效日期不为空,判断到货时间是否晚于生效日期,如果晚于表示模式已经切换,不要货
				if(shengxrq != null && !CommonFun.sdfAxparse(time).before(shengxrq)){
					continue;
				}
				Map<String,String> timeMap = new HashMap<String, String>();
				timeMap.put("zuizdhsj", time);//最早到货时间
				timeMap.put("zuiwdhsj", time);//最晚到货时间
				timeMap.put("shangxsj", time);//小火车上线时间
				//0013016: 按需紧急订单的发运时间 by CSY 20170112
				timeMap.put("faysj", this.getFaysj(mos, kaibsj, time, anxjscszjb));//发运日期
				//向前推小火车备货时间,前一个工作日收班时间再往前推135分钟
				List<Ticxxsj> lastGzr = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryLastGzr",param);
				if(null != lastGzr && !lastGzr.isEmpty()){
					String shoubsj = lastGzr.get(0).getRiq() + " " + lastGzr.get(0).getShijdjssj();//收班时间
					Date date = new Date();
					//向前推135分钟
					date.setTime(CommonFun.yyyyMMddHHmmssAxparse(shoubsj).getTime() - 135*60*1000);
					timeMap.put("beihsj", CommonFun.yyyyMMddHHmmssAxformat(date));//小火车备货时间
				}else{//如果查询不到前一个工作日,则备货时间也设为开班
					timeMap.put("beihsj", time);//小火车备货时间
				}
				AnxMaoxqzjb anxMaoxqzjb = new AnxMaoxqzjb();
				anxMaoxqzjb.setXhsj(time);
				Map mapDingdlj = calculateDingd(mos, anxjscszjb, yaohl, param, user, dingdjssj, gongyslx, dingdlx, 
						anxMaoxqzjb, time, timeMap, gongysdm, Const.DINGD_STATUS_YSX, listDingdmx, listDingdlj, lingjSlMap, lingjgysMap, 
						gongysFeneMap, wulljMap, dingdhMap);
				ziy = this.getBigDecimalAx(mapDingdlj.get("ziy"));
			}
			
			
			/**
			 * 取用户中心,零件,消耗点,封闭期后日期毛需求,按需求时间排序,升序
			 */
			List<AnxMaoxqzjb> listAnxMaoxq = new ArrayList<AnxMaoxqzjb>();
			//计算类型1,CDMD模式计算,按用户中心,零件编号,消耗点取毛需求
			if("1".equals(jislx)){
				param.put("xiaohd", anxjscszjb.getXiaohd());
				listAnxMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryAnxmaoxqzjbMx",param);
			}else{
				//计算类型2,C1M1模式计算,按用户中心,零件编号,线边仓库汇总毛需求
				param.put("xianbck", anxjscszjb.getXianbck());
				listAnxMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryAnxmaoxqzjbMxByXianbk",param);
			}
			
			for (int j = 0; j < listAnxMaoxq.size(); j++) {
				AnxMaoxqzjb anxMaoxqzjb = listAnxMaoxq.get(j);
				
				/**
				 * 计算要货量
				 */
				ziy = ziy.subtract(anxMaoxqzjb.getXiaohxs());//资源 = 资源-消耗数量
				//如果资源大于等于0,则无需要货
				if(ziy.signum() >= 0){
					continue;
				}
				BigDecimal yaohl = ziy.abs();//要货量为将资源补零
				String xiaohSj = anxMaoxqzjb.getXhsj().substring(0,16);//消耗时间
				
				Map mapDingdlj = calculateDingd(mos, anxjscszjb, yaohl, param, user, dingdjssj, gongyslx, dingdlx, 
						anxMaoxqzjb, xiaohSj, gongysdm,Const.DINGD_STATUS_YSX,listDingdmx,listDingdlj,lingjSlMap,lingjgysMap,gongysFeneMap,wulljMap,
						dingdhMap,xiehztMap,kaibsj,xiabsj,shengxrq);
				String biaos = this.strNullAx(mapDingdlj.get("biaos"));//标识符
				//如果标识符为continue,则代表有异常报警,返回标识符,跳过当前数据计算下一条
				if("continue".equals(biaos)){
					continue;
				//如果标识符为break,则代表计算结束,该品种不再计算
				}else if("break".equals(biaos)){
					break;
				}
				ziy = this.getBigDecimalAx(mapDingdlj.get("ziy"));
			}
		}
		//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ilorder.insertDingdlj", listDingdlj);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ilorder.insertDingdmx", listDingdmx);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("anxjis.updateDingdljP0", listDingdlj);
		
		/**
		 * 汇总订单零件P0数量
		 */
		/*
		List<Dingdlj> listDingdljs = new ArrayList<Dingdlj>();
		
		for (int i = 0; i < listDingdlj.size(); i++) {
			Dingdlj dingdlj = listDingdlj.get(i);
			//汇总P0数量
			dingdlj.setP0sl(this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryDingdmxSl", dingdlj)));
			dingdlj.setActive(Const.ACTIVE_1);
			listDingdljs.add(dingdlj);
		}
		//更新订单零件数量
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateDingdljPart", listDingdljs);
		*/
	}
	
	/**
	 * 按需计算数据准备
	 * @param user 操作用户
	 * @throws ParseException
	 */
	public void dataPreparation(String user,Map<String,String> map,Map<String,Xiehzt> xiehztMap) throws ParseException{
		
		/**
		 * 清除中间表数据
		 */
		clearData(user,"'"+Const.DINGD_LX_ANX_ZC_C+"','"+Const.DINGD_LX_ANX_ZC_M+"'",map);
		
		/**
		 * 汇按需计算间隔时间汇总SPPV部分毛需求,CLV部分毛需求已经汇总,直接插入中间表
		 */
		List<Anxjscszjb> listAnxjscszjb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryMosXhd",map);//取C1CDM1MD模式消耗点集合
		
		//CLV毛需求,生成毛需求中间表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.insertAnxmaoxqzjbClv",map);
		
		/**
		 * 关联物流路径生成按需计算中间表
		 */
		wulljDataPreparation(listAnxjscszjb,user,"1",xiehztMap);
	}
	
	/**
	 * 物流路径中间表准备
	 * @param user 操作用户
	 */
	public void wulljDataPreparation(List<Anxjscszjb> listAnxjscszjb,String user,String type,Map<String,Xiehzt> xiehztMap){
		Map<String,String> param = new HashMap<String, String>();
		List<Anxjscszjb> listAnxjscszjbs = new ArrayList<Anxjscszjb>();
		/**
		 * 关联物流路径等相关参数生成计算中间表
		 */
		for (int i = 0; i < listAnxjscszjb.size(); i++) {
			Anxjscszjb anxjscszjb = listAnxjscszjb.get(i);
			param.put("usercenter", anxjscszjb.getUsercenter());
			param.put("lingjbh", anxjscszjb.getLingjbh());
			param.put("fenpbh", anxjscszjb.getFenpbh());
			Anxjscszjb wulljAnxjscszjb = null;
			//如果类型为1,查询模式为外部模式和模式2
			if("1".equals(type)){
				wulljAnxjscszjb = (Anxjscszjb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryWullj",param);
				param.put("xiaohdbh", anxjscszjb.getXiaohd());
				anxjscszjb = (Anxjscszjb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryLingjXhd",param);
				if (anxjscszjb == null) {
					logger.error("logAnx001零件："+param.get("lingjbh")+"在用户中心："+param.get("usercenter")+" 的零件消耗点："+param.get("xiaohdbh")+"不存在或已失效，跳过该零件的计算");
					this.saveYcbjXhd(param.get("lingjbh"), param.get("usercenter"), wulljAnxjscszjb.getJihy(), user, "的零件消耗点："+param.get("xiaohdbh")+"不存在或已失效，跳过该零件的计算");
					continue;
				}
			}else{
				//如果类型为2,为初始化计算查询模式为将来模式和将来模式2
				wulljAnxjscszjb = (Anxjscszjb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxChushJis.queryWullj",param);
			}
			
			//物流路径不为空,保存到中间表进行后续计算
			if(wulljAnxjscszjb != null && anxjscszjb != null){
				wulljAnxjscszjb.setXiaohd(anxjscszjb.getXiaohd());//消耗点
				wulljAnxjscszjb.setFenpbh(anxjscszjb.getFenpbh());//分配循环
				wulljAnxjscszjb.setXiaohcbh(anxjscszjb.getXiaohcbh());//小火车编号
				wulljAnxjscszjb.setXiaohccxbh(anxjscszjb.getXiaohccxbh());//小火车车厢编号
				wulljAnxjscszjb.setAnqkcs(anxjscszjb.getAnqkcs());//零件消耗点安全库存
				wulljAnxjscszjb.setXianbllkc(anxjscszjb.getXianbllkc());//线边理论库存
				wulljAnxjscszjb.setYifyhlzl(anxjscszjb.getYifyhlzl());//已发要货总量
				wulljAnxjscszjb.setJiaofzl(anxjscszjb.getJiaofzl());//交付总量
				wulljAnxjscszjb.setZhongzzl(anxjscszjb.getZhongzzl());//终止总量
				wulljAnxjscszjb.setXhdxittzz(anxjscszjb.getXhdxittzz());//消耗点系统调整值
				//数据校验
				if(checkData(wulljAnxjscszjb, user,type,xiehztMap)){
					listAnxjscszjbs.add(wulljAnxjscszjb);
				}
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("anxjis.insertAnxjscszjb",listAnxjscszjbs);
	}
	
	/**
	 * 校验数据
	 * @param obj 数据对象
	 * @return true:校验通过;false:校验不通过
	 */
	public boolean checkData(Anxjscszjb obj,String user,String type,Map<String,Xiehzt> xiehztMap){
		//错误详细信息
		StringBuilder cuowxxxx = new StringBuilder("");
		String mos = "";
		if(Const.ANX_MS_CD.equalsIgnoreCase(obj.getMos()) || Const.ANX_MS_C1.equalsIgnoreCase(obj.getMos())) {
			mos = obj.getMos();
			if(obj.getBeihzq() == null){
				cuowxxxx.append("备货周期为空|");
			}else if(obj.getYunszq() == null){
				cuowxxxx.append("发运周期为空|");
			}
			if(StringUtils.isEmpty(obj.getGysuabzlx()) || obj.getGysuaucgs() == null 
					|| obj.getGysuaucgs() == BigDecimal.ZERO || obj.getGysucrl() == null 
					|| obj.getGysucrl() == BigDecimal.ZERO || StringUtils.isEmpty(obj.getGysucbzlx())){
				cuowxxxx.append("包装信息为空|");
			}
		}else{
			mos = obj.getMos2();
			if("1".equals(type)){
				if(obj.getCangkshsj2() == null){
					cuowxxxx.append("仓库送货时间2为空|");
				}
				if(obj.getBeihsj2() == null){
					cuowxxxx.append("备货时间2为空|");
				}
				if(obj.getCangkfhsj2() == null){
					cuowxxxx.append("仓库返回时间2为空|");
				}
			}
		}
		if(Const.ANX_MS_M1.equalsIgnoreCase(mos)){
			if(StringUtils.isEmpty(obj.getCkusbzlx()) || obj.getCkusbzrl() == null 
					|| obj.getCkusbzrl() == BigDecimal.ZERO){
				cuowxxxx.append("M1模式US包装信息为空|");
			}
		}else if(Const.ANX_MS_MD.equalsIgnoreCase(mos)){
			if(StringUtils.isEmpty(obj.getCkuclx()) || obj.getCkucrl() == null 
					|| obj.getCkucrl() == BigDecimal.ZERO){
				cuowxxxx.append("MD模式UC包装信息为空|");
			}
			if(StringUtils.isEmpty(obj.getXiaohcbh())){
				saveYicbj(obj, user, "小火车编号为空",mos,obj.getJianglms());
				return false;
			}
		}else if(Const.ANX_MS_CD.equalsIgnoreCase(mos)){
			if(StringUtils.isEmpty(obj.getXiaohcbh())){
				saveYicbj(obj, user, "小火车编号为空", mos,obj.getJianglms());
				return false;
			}
		}
		if("1".equals(type)){
			if(obj.getBeihsjc() == null){
				saveYicbj(obj, user, "内部物流时间为空",mos,obj.getJianglms2());
				return false;
			}
		}
		if(StringUtils.isEmpty(obj.getXiehztbh())){
			cuowxxxx.append("卸货站台编号为空|");
		}else{
			//从卸货站台得到提前到货时间
			Xiehzt xiehzt = xiehztMap.get(obj.getUsercenter()+obj.getXiehztbh());
			if(xiehzt == null){
				cuowxxxx.append("卸货站台为空|");
			}else{
				BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();//允许提前到货时间
				if(yunxtqdhsj == null){
					cuowxxxx.append("允许提前到货时间为空|");
				}
			}
		}
		
		//如果错误详细信息不为空,则保存异常报警表,跳过本条数据.
		if(cuowxxxx.length() != 0){
			saveYicbj(obj, user, cuowxxxx.toString(),mos);
			return false;
		}
		return true;
	}
	
	/**
	 * 获取封闭期天数
	 * @param obj 按需计算初始化中间对象
	 * @return 封闭期天数
	 * @throws ParseException 
	 */
	public int getJisDate(Anxjscszjb obj){
		int day = 0;
		try {
			//如果是CD,C1,取备货周期,运输周期向上取整
			if(Const.ANX_MS_CD.equalsIgnoreCase(obj.getMos()) || Const.ANX_MS_C1.equalsIgnoreCase(obj.getMos())){
				day = obj.getBeihzq().add(obj.getYunszq()).setScale(0,BigDecimal.ROUND_UP).intValue();
			}else{//如果是MD,M1模式,取备货时间2+仓库送货时间2+仓库返回时间2向上取整
				day = obj.getBeihsj2().add(obj.getCangkshsj2()).add(obj.getCangkfhsj2()).setScale(0,BigDecimal.ROUND_UP).intValue();
			}
		} catch (Exception e) {
			day = -1;//如果异常则返回负数跳过
		}
		return day;
	}
	
	/**
	 * 按需时间计算
	 * @param bean 按需计算中间表参数
	 * @param mos 模式
	 * @param user 操作人
	 * @param xiaohsj 消耗时间
	 * @param neibwlsj 内部物流时间
	 * @param cangkshsj 仓库送货时间
	 * @param xiehztMap 卸货站台参数
	 * @param gcbh 承运商编号
	 * @param kaibsj 开班时间
	 * @param xiabsj 下班时间
	 * @param shengxrq 将来模式生效日期
	 * @return 时间计算结果,如果返回空,则表示有异常报警或者时间校验不要货
	 * @throws ParseException
	 */
	public Map<String, String> timeCalculate(Anxjscszjb bean,String mos,String user,String xiaohsj,BigDecimal neibwlsj,
			BigDecimal cangkshsj,Map<String,Xiehzt> xiehztMap,String gcbh,String kaibsj,String xiabsj,Date shengxrq) throws ParseException{
		
		Map<String, String> map = new HashMap<String, String>();
		//存放计算结果
		Map<String, String> result = new HashMap<String, String>();//计算结果
		map.put("usercenter", bean.getUsercenter());//用户中心
		if (mos.equalsIgnoreCase(Const.ANX_MS_M1) || mos.equalsIgnoreCase(Const.ANX_MS_C1)) {
			map.put("shengcxbh", bean.getXianbck());//生产线编号
		}else{
			map.put("shengcxbh", bean.getShengcxbh());//生产线编号
		}
		map.put("jisrq", kaibsj.substring(0,10));//计算日期
		// 定义最早到货时间
		String zuizdhsj = "";
		// 最晚到货时间
		String zuiwdhsj = "";
		// 定义预计到货时间
		String yujdhsj = "";
		// 定义上线时间
		String shangxsj = "";
		// 定义备货时间
		String beihsj = "";
		
		/**
		 * 计算预计到货时间
		 * CDMD模式需计算小火车备货时间,小火车上线时间
		 */
		
		//CDMD模式计算小火车时间
		if (mos.equalsIgnoreCase(Const.ANX_MS_CD) || mos.equalsIgnoreCase(Const.ANX_MS_MD)) {
			map.put("xiaohcbh", bean.getXiaohcbh());
			map.put("chufsxsj", xiaohsj.substring(0, 16));
		 	//取消耗时间前面的前面的小火车时刻信息(相等的则取前面一条)
			List<Xiaohcyssk> listXhcyssk = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryXiaohcyssk", map);
			if(listXhcyssk == null || listXhcyssk.isEmpty()){
				saveYicbj(bean, user, "根据消耗时间取小火车信息为空,消耗时间:"+xiaohsj+",小火车编号:"+bean.getXiaohcbh(),mos,bean.getJianglms());
				result.put("biaos", "continue");
				return result;
			}else if (listXhcyssk.size()<2) {//0012871: 按需计算--取小火车备货时间，当取当前趟次前一趟无数据，异常报警，不计算订单 by CSY 20160913
				saveYicbj(bean, user, "根据消耗时间取小火车前一趟信息为空,消耗时间:"+xiaohsj+",小火车编号:"+bean.getXiaohcbh(),mos,bean.getJianglms());
				result.put("biaos", "continue");
				return result;
			}
			Xiaohcyssk xiaohcyssk = listXhcyssk.get(1);//取前面的前面,下标0为前面,下标1为前面的前面
			beihsj = xiaohcyssk.getKaisbhsj();//备货时间为小火车备货时间
			shangxsj = xiaohcyssk.getChufsxsj();//上线时间为小火车出发上线时间
			result.put("shangxsj", shangxsj);
			result.put("tangc", this.strNullAx(xiaohcyssk.getTangc()));//趟次
			Date shangxsjDate = CommonFun.yyyyMMddHHmmAxparse(shangxsj);//小火车上线时间
			//如果小火车上线时间早于开班时间,则小火车上线时间设为开班时间,备货时间设为前一个工作日收班往前推135分钟
			if(shangxsjDate.before(CommonFun.yyyyMMddHHmmssAxparse(kaibsj))){
				//把开班时间视为消耗时间以及到货,备货时间
				String xiaohSj = kaibsj.substring(0,16);//消耗时间
				result.put("zuizdhsj", xiaohSj);//最早到货时间
				result.put("zuiwdhsj", xiaohSj);//最晚到货时间
				//向前推小火车备货时间,前一个工作日收班时间再往前推135分钟
				List<Ticxxsj> lastGzr = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryLastGzr",map);
				if(null != lastGzr && !lastGzr.isEmpty()){
					String shoubsj = lastGzr.get(0).getRiq() + " " + lastGzr.get(0).getShijdjssj();//收班时间
					Date date = new Date();
					//向前推135分钟
					date.setTime(CommonFun.yyyyMMddHHmmssAxparse(shoubsj).getTime() - 135*60*1000);
					result.put("beihsj", CommonFun.yyyyMMddHHmmssAxformat(date));//小火车备货时间
				}else{//如果查询不到前一个工作日,则备货时间也设为开班
					result.put("beihsj", xiaohSj);//小火车备货时间
				}
				result.put("shangxsj", xiaohSj);//小火车上线时间
				//0013016: 按需紧急订单的发运时间 by CSY 20170112
				result.put("faysj", this.getFaysj(mos, kaibsj, xiaohSj, bean));//发运日期
				return result;
			}
			result.put("beihsj", beihsj);
			//从小火车备货时间开始推算
			map.put("juedsk", beihsj);
		} else {
			// C1,M1模式的时间,直接从消耗时间开始推算
			map.put("juedsk", xiaohsj);
		}
		map.put("shijNum", this.strNullAx(neibwlsj.intValue()));//内部物流时间
		//预计到货时间=时间(CDMD为小火车备货时间,C1M1为消耗时间)-内部物流时间
		yujdhsj = this.strNullAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryGongzsjmbQ",map));
		if(StringUtils.isEmpty(yujdhsj)){
			saveYicbj(bean, user, "工作时间模板查询无数据"+xiaohsj, mos);
			result.put("biaos", "continue");
			return result;
		}
		/**
		 * 计算最早到货时间,最晚到货时间
		 * mantis:0011557  by hzg modify 调整为取线边库
		 */
		map.put("xianbck",bean.getXianbck());
		//map.put("xiehztbh", bean.getXiehztbh().substring(0,4));//取编号前4位为卸货站台编组
		map.put("xiehztbh", getXiehztbhOfXianbk(map));//modify by hzg
		map.put("gcbh", gcbh);//承运商编号
		map.put("daohsj", yujdhsj);//预计到货时间
		//根据卸货站台编组,承运商编号去运输时刻取预计到货时间前面的一条为最晚到货时间
		Yunssk yunssk = (Yunssk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryYunsskObject", map);//查询运输时刻
		if (yunssk != null) {
			zuiwdhsj = yunssk.getDaohsj().substring(0,16);//取运输时刻最到货时间为最晚到货时间
		}else{
			zuiwdhsj = yujdhsj;//取预计到货时间为最晚到货时间
		}
		
		//预计到货时间=时间(CDMD为小火车备货时间,C1M1为消耗时间)-内部物流时间
		map.put("juedsk", zuiwdhsj);
		//从卸货站台得到提前到货时间
		Xiehzt xiehzt = xiehztMap.get(bean.getUsercenter()+bean.getXiehztbh());
		BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();//允许提前到货时间
		map.put("shijNum", this.strNullAx(yunxtqdhsj.intValue()));//允许提前到货时间
		logger.info("按需时间log:kaibsj="+kaibsj+":shengxrq="+shengxrq+":xiabsj="+xiabsj+"	result"+result);
		zuizdhsj = this.strNullAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryGongzsjmbQ",map));
		logger.info("按需时间log1:kaibsj="+kaibsj+":shengxrq="+shengxrq+":xiabsj="+xiabsj+":zuizdhsj="+zuizdhsj+"	result"+result+"	map"+map);
		if(StringUtils.isEmpty(zuizdhsj)){
			saveYicbj(bean, user, "工作时间模板查询无数据"+zuiwdhsj, mos);
			result.put("biaos", "continue");
			return result;
		}
		result.put("zuiwdhsj", zuiwdhsj);
		result.put("zuizdhsj", zuizdhsj);
		result.put("yujdhsj", yujdhsj);
		
		logger.info("按需时间log11:kaibsj="+kaibsj+":shengxrq="+shengxrq+":xiabsj="+xiabsj+":zuizdhsj="+zuizdhsj+"	result"+result+"	map"+map);
		
		Date zuiwdhsjDate = CommonFun.yyyyMMddHHmmAxparse(zuiwdhsj);//最晚到货时间
		Date zuizdhsjDate = CommonFun.yyyyMMddHHmmAxparse(zuizdhsj);//最早到货时间
		logger.info("按需时间log2:kaibsj="+kaibsj+":shengxrq="+shengxrq+":xiabsj="+xiabsj+":zuizdhsj="+zuizdhsj+":zuiwdhsjDate="+zuiwdhsjDate+":zuizdhsjDate="+zuizdhsjDate+"	result"+result);
		String xiaohSj = "";//消耗时间
		//如果最晚到货时间早于到开班时间,则最晚最早到货时间设为开班时间
		//如果最晚到货时间早于开班时间,则把时间设为开班
		if(zuiwdhsjDate.before(CommonFun.yyyyMMddHHmmssAxparse(kaibsj))){
			xiaohSj = kaibsj.substring(0,16);//消耗时间
			result.put("zuizdhsj", xiaohSj);//最早到货时间
			result.put("zuiwdhsj", xiaohSj);//最晚到货时间
			//0013016: 按需紧急订单的发运时间 by CSY 20170112
			result.put("faysj", this.getFaysj(mos, kaibsj, xiaohSj, bean));//发运日期
			logger.info("按需时间log21:kaibsj="+kaibsj+":shengxrq="+shengxrq+":xiabsj="+xiabsj+":zuizdhsj="+zuizdhsj+":zuiwdhsjDate="+zuiwdhsjDate+":zuizdhsjDate="+zuizdhsjDate+"	result"+result);
			return result;
		//如果最晚到货时间晚于到货日期开始工作时间,终止
		}else if(zuiwdhsjDate.after(CommonFun.yyyyMMddHHmmssAxparse(xiabsj))){
			result.put("biaos", "break");
			logger.info("按需时间log22:kaibsj="+kaibsj+":shengxrq="+shengxrq+":xiabsj="+xiabsj+":zuizdhsj="+zuizdhsj+":zuiwdhsjDate="+zuiwdhsjDate+":zuizdhsjDate="+zuizdhsjDate+"	result"+result);
			return result;
		//如果最早到货早于开班时间则把最早到货时间设为开班
		}else if(zuizdhsjDate.before(CommonFun.yyyyMMddHHmmssAxparse(kaibsj))){
			xiaohSj = kaibsj.substring(0,16);//消耗时间
			result.put("zuizdhsj", xiaohSj);//最早到货时间
			//return result;
		}
		
		//如果将来模式生效日期不为空,判断到货时间是否晚于生效日期,如果晚于表示模式已经切换,不要货
		if(shengxrq != null && !zuiwdhsjDate.before(shengxrq)){
			result.put("biaos", "break");
			logger.info("按需时间log23:kaibsj="+kaibsj+":shengxrq="+shengxrq+":xiabsj="+xiabsj+":zuizdhsj="+zuizdhsj+":zuiwdhsjDate="+zuiwdhsjDate+":zuizdhsjDate="+zuizdhsjDate+"	result"+result);
			return result;
		}
		logger.info("按需时间log3:kaibsj="+kaibsj+":shengxrq="+shengxrq+":xiabsj="+xiabsj+":zuizdhsj="+zuizdhsj+"	result"+result);
		/**
		 * M1MD模式计算发运时间
		 */
		/*if (mos.equalsIgnoreCase(Const.ANX_MS_M1) || mos.equalsIgnoreCase(Const.ANX_MS_MD)) {
			//发运时间 = 最晚到货时间 - 仓库送货时间2
			map.put("juedsk", zuiwdhsj);
			map.put("shijNum", this.strNullAx(this.getBigDecimalAx(cangkshsj).multiply(new BigDecimal(24)).multiply(new BigDecimal(60)).intValue()));
			result.put("faysj", this.strNullAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryGongzsjmbQ",map)));
		}else{//C1CD模式发运时间= 最晚到货时间 - 运输周期
			Date date = new Date();
			date = CommonFun.yyyyMMddHHmmAxparse(zuiwdhsj);//设置为最晚到货时间
			logger.info("按需时间log4:kaibsj="+kaibsj+":shengxrq="+shengxrq+":xiabsj="+xiabsj+":zuizdhsj="+zuizdhsj+":zuiwdhsj="+zuiwdhsj+":Yunszq="+bean.getYunszq());
			//发运时间 = 最晚到货时间 - 运输周期
			date.setTime(date.getTime() - this.getBigDecimalAx(bean.getYunszq()).multiply(new BigDecimal(24))
					.multiply(new BigDecimal(60)).multiply(new BigDecimal(60)).multiply(new BigDecimal(1000)).intValue());
			result.put("faysj", CommonFun.yyyyMMddHHmmAxformat(date));
		}*/
		//0013016: 按需紧急订单的发运时间 by CSY 20170112
		result.put("faysj", this.getFaysj(mos, kaibsj, zuiwdhsj, bean));
		logger.info("按需时间计算:"+map);
		return result;
	}
	
	/**
	 * 取仓库卸货站台编组号
	 * @author 贺志国
	 * @date 2015-8-25
	 * @param xianbk 线边仓库
	 * @return 
	 */
	private String getXiehztbhOfXianbk(Map<String,String> map) {
		return this.strNullAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryXiehztbhOfCangk",map));
	}

	public String jisFysj(String mos,String zuiwdhsj,Anxjscszjb bean,BigDecimal cangkshsj) throws ParseException{
		Map<String, String> map = new HashMap<String, String>();
		/**
		 * M1MD模式计算发运时间
		 */
		if (mos.equalsIgnoreCase(Const.ANX_MS_M1) || mos.equalsIgnoreCase(Const.ANX_MS_MD)) {
			//发运时间 = 最晚到货时间 - 仓库送货时间2
			map.put("juedsk", zuiwdhsj);
			map.put("shijNum", this.strNullAx(this.getBigDecimalAx(cangkshsj).multiply(new BigDecimal(24)).multiply(new BigDecimal(60)).intValue()));
			return this.strNullAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryGongzsjmbQ",map));
		}else{//C1CD模式发运时间= 最晚到货时间 - 运输周期
			Date date = new Date();
			date = CommonFun.yyyyMMddHHmmAxparse(zuiwdhsj);//设置为最晚到货时间
			//发运时间 = 最晚到货时间 - 运输周期
			date.setTime(date.getTime() - this.getBigDecimalAx(bean.getYunszq()).multiply(new BigDecimal(24))
					.multiply(new BigDecimal(60)).multiply(new BigDecimal(60)).multiply(new BigDecimal(1000)).intValue());
			return CommonFun.yyyyMMddHHmmAxformat(date);
		}
	}
	
	/**
	 * 初始资源计算方法
	 * @param bean 按需计算中间表对象
	 * @param mos 模式
	 * @param user 操作用户
	 * @param dingdjssj 订单计算时间
	 * @param kaibsj 结束时间
	 * @param ziywjsj 资源文件时间
	 * @return
	 */
	public BigDecimal resourceCalculation(Anxjscszjb bean,String mos,String user,String dingdjssj,String kaibsj,
			String ziywjsj,Map<String, List<Tidj>> tidjMap,Map<String, String> param){
		Map<String, String> map = new HashMap<String, String>();
		BigDecimal ziy = BigDecimal.ZERO;// 资源总量
		BigDecimal weijfzl = BigDecimal.ZERO;//未交付总量
		BigDecimal weijfzlbj = BigDecimal.ZERO;//未交付报警
		//订单计算时间日期
		String dateNow = dingdjssj.substring(0,10);
		
		map.put("xiaohsj", ziywjsj.substring(0,16));//开始时间，ziywjsj资源生效时间
		map.put("xiaohsj2", kaibsj.substring(0,16));//结束时间
		///0010991 按需计算sppv需求 截止点采用当天开班时间，目前逻辑有问题
		map.put("xiaohsjsppv", param.get("xiaohsjsppv").substring(0,16));//结束时间
		map.put("usercenter", bean.getUsercenter());//用户中心
		map.put("lingjbh", bean.getLingjbh());//零件编号
		
		/**
		 * 计算资源
		 */
		if(Const.ANX_MS_C1.equals(mos) || Const.ANX_MS_M1.equals(mos)){
			//查询库存(资源快照)
			Ziykzb ziykzb = queryZiykzb(bean.getUsercenter(),bean.getLingjbh(),bean.getXianbck(), dateNow);
			//C1M1模式:未交付=已发要货总量 – 交付总量 – 终止总量(零件仓库)
			weijfzl = this.getBigDecimalAx(bean.getCkyifyhlzl()).subtract(this.getBigDecimalAx(bean.getCkjiaofzl()))
				.subtract(this.getBigDecimalAx(bean.getCkzhongzzl())); 
			//C1M1模式汇总替代件数量
			BigDecimal tidjzl = queryTidjsl(bean.getXianbck(), bean.getUsercenter(), bean.getLingjbh(), dateNow,tidjMap);
			map.put("xianbck", bean.getXianbck());//线边仓库
			map.put("xuqly", "1");//计算待消耗使用CLV部分毛需求
			BigDecimal daixh = this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryXhlByXianbk", map));//待消耗
			map.put("xuqly", "3");//计算待消耗使用SPPV部分毛需求
			daixh = daixh.add(this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryEmonXhlByXianbk", map)));//待消耗
			//C1M1模式资源=库存数量 + 未交付 + 替代件数量 - 待消耗  + 仓库系统调整值(年末) - 安全库存(零件仓库)
			ziy = ziykzb.getKucsl().add(weijfzl).add(tidjzl).subtract(daixh).add(this.getBigDecimalAx(bean.getCkxittzz()))
				.subtract(this.getBigDecimalAx(bean.getAnqkcsl()));
			weijfzlbj = weijfzl.add(this.getBigDecimalAx(bean.getCkxittzz()));
			logger.info("库存数量:"+ziykzb.getKucsl()+"零件:"+bean.getLingjbh()+"线边库:"+bean.getXianbck()+"未交付:"+weijfzlbj+"替代件:"+tidjzl+"待消耗:"+daixh+"系统调整值"+bean.getCkxittzz()
					+"安全库存:"+bean.getAnqkcsl()+"初始资源:"+ziy);
		}else{
			//CDMD模式:未交付=已发要货总量 – 交付总量 – 终止总量(零件消耗点)
			weijfzl = this.getBigDecimalAx(bean.getYifyhlzl()).subtract(this.getBigDecimalAx(bean.getJiaofzl()))
				.subtract(this.getBigDecimalAx(bean.getZhongzzl()));
			map.put("xiaohd", bean.getXiaohd());//消耗点
			map.put("xuqly", "1");//计算待消耗使用CLV部分毛需求
			BigDecimal daixh = this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryXiaohl", map));//待消耗
			map.put("xuqly", "3");//计算待消耗使用SPPV部分毛需求
			daixh = daixh.add(this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryEmonXiaohl", map)));//待消耗
			//CDMD模式资源=线边理论库存 + 未交付 - 待消耗 + 零件消耗点系统调整值(年末) - 安全库存(零件消耗点)
			ziy = bean.getXianbllkc().add(weijfzl).subtract(daixh).add(this.getBigDecimalAx(bean.getXhdxittzz()))
				.subtract(this.getBigDecimalAx(bean.getAnqkcs()));
			weijfzlbj = weijfzl.add(this.getBigDecimalAx(bean.getXhdxittzz()));
			logger.info("线边理论库存:"+bean.getXianbllkc()+"零件:"+bean.getLingjbh()+"消耗点:"+bean.getXiaohd()+"未交付:"+weijfzlbj+"待消耗:"+daixh+"系统调整值"+bean.getXhdxittzz()
					+"安全库存:"+bean.getAnqkcs()+"初始资源:"+ziy);
		}
		//如果未交付为负数,保存异常报警
		if(weijfzlbj.signum() < 0){
			saveYicbj(bean, user, "未交付数量为负数:"+weijfzlbj,mos);
		}
		return ziy;
	}
	
	/**
	 * 保存异常报警
	 * @param bean 中间表信息
	 * @param user 操作用户
	 * @param str 错误信息
	 */
	public void saveYicbj(Anxjscszjb bean,String user,String str,String mos){
		//拼接错误信息
		StringBuilder cuowxx = new StringBuilder("在供货模式:");
		cuowxx.append(this.strNullAx(mos));
		cuowxx.append("下,零件:");
		cuowxx.append(bean.getLingjbh());
		cuowxx.append("客户:");
		//CDMD模式客户为消耗点
		if(Const.ANX_MS_CD.equalsIgnoreCase(mos) || Const.ANX_MS_MD.equalsIgnoreCase(mos)) {
			cuowxx.append(bean.getXiaohd());
		}else{
			//C1M1模式客户为线边库
			cuowxx.append(bean.getXianbck());
		}
		cuowxx.append(str);
		yicbjService.saveYicbj(bean.getLingjbh(), bean.getUsercenter(), cuowxx.toString(), user, Const.JISMK_ANX_CD, Const.YICHANG_LX2,bean.getJihy());
	}
	
	/**
	 * 保存异常报警
	 * @param bean 中间表信息
	 * @param user 操作用户
	 * @param str 错误信息
	 * @param jihy 计划员
	 */
	public void saveYicbj(Anxjscszjb bean,String user,String str,String mos,String jihy){
		//拼接错误信息
		StringBuilder cuowxx = new StringBuilder("在供货模式:");
		cuowxx.append(this.strNullAx(mos));
		cuowxx.append("下,零件:");
		cuowxx.append(bean.getLingjbh());
		cuowxx.append("客户:");
		//CDMD模式客户为消耗点
		if(Const.ANX_MS_CD.equalsIgnoreCase(mos) || Const.ANX_MS_MD.equalsIgnoreCase(mos)) {
			cuowxx.append(bean.getXiaohd());
		}else{
			//C1M1模式客户为线边库
			cuowxx.append(bean.getXianbck());
		}
		cuowxx.append(str);
		yicbjService.saveYicbj(bean.getLingjbh(), bean.getUsercenter(), cuowxx.toString(), user, Const.JISMK_ANX_CD, Const.YICHANG_LX2,jihy);
	}
	
	/**
	 * 查询资源快照库存
	 * @param bean 按需计算中间表对象
	 * @param ziyhqrq 资源获取日期
	 * @return 资源快照(没有则数量都为0)
	 */
	public Ziykzb queryZiykzb(String usercenter,String lingjbh,String cangkdm, String ziyhqrq) {
		Map<String, String> map = new HashMap<String, String>();// 参数map
		map.put("usercenter", usercenter);
		map.put("lingjbh", lingjbh);
		map.put("cangkdm", cangkdm);
		map.put("ziyhqrq", ziyhqrq);
		// 查询资源快照表
		Ziykzb ziykzb = (Ziykzb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryZiykzb", map);
		//查不到库存,把库存当做0计算
		if(ziykzb == null){
			ziykzb = new Ziykzb();
			ziykzb.setAnqkc(BigDecimal.ZERO);
			ziykzb.setKucsl(BigDecimal.ZERO);
			ziykzb.setDingdlj(BigDecimal.ZERO);
			ziykzb.setJiaoflj(BigDecimal.ZERO);
			ziykzb.setXttzz(BigDecimal.ZERO);
			ziykzb.setDingdzzlj(BigDecimal.ZERO);
		}
		return ziykzb;
	}
	
	/**
	 * 查询指定零件的替代件数量
	 * @param cangk 仓库代码
	 * @param usercenter 用户中心
	 * @param lingjbh 零件编号
	 * @param ziyhqrq 资源获取日期
	 * @return 替代件数量
	 */
	public BigDecimal queryTidjsl(String cangk, String usercenter, String lingjbh, String ziyhqrq,Map<String, List<Tidj>> tidjMap) {
		//获取替代件信息集合
		List<Tidj> tidjAll = tidjMap.get(usercenter+lingjbh);
		//如果替代件信息为空,则返回0
		if(tidjAll == null){
			return BigDecimal.ZERO;
		}
		//拼接替代件零件号,用于汇总替代件数量
		StringBuilder tidljh = new StringBuilder("");
		for (int i = 0; i < tidjAll.size(); i++) {
			tidljh.append("'");
			tidljh.append(tidjAll.get(i).getTidljh());
			tidljh.append("',");
		}
		if(tidljh.length() == 0){
			return BigDecimal.ZERO;
		}
		//去掉最后一个,
		tidljh.deleteCharAt(tidljh.lastIndexOf(","));
		Map<String,String> param = new HashMap<String, String>();
		param.put("usercenter", usercenter);
		param.put("lingjbh", tidljh.toString());
		param.put("cangkdm", cangk);
		param.put("ziyhqrq", ziyhqrq);
		//返回替代件数量
		return this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryZiykzbLjSl",param));
	}
	
	/**
	 * 保存订单信息
	 * @param anxjscszjb 按需计算初始中间表
	 * @param gongysdm 供应商代码
	 * @param mos 模式
	 * @param user 操作用户
	 * @param dingdjssj 订单计算时间
	 * @param gongyslx 供应商类型
	 * @param dingdlx 订单类型
	 * @param yaohrq 要货日期
	 * @param state 订单状态
	 * @return 订单号
	 */
	public Dingd insertDingd(Anxjscszjb anxjscszjb,String gongysdm,String mos,String user,String dingdjssj,
			String gongyslx,String dingdlx,String state,Map<String,String> dingdMap,String jiaofrq){
		
		/**
		 * 生成订单表信息
		 */
		//根据模式,用户中心,供应商代码生成订单号
		String dingdh = ilOrderService.getOrderNumber(mos, anxjscszjb.getUsercenter(),gongysdm,dingdMap);
		Map<String,String> map = new HashMap<String, String>();
		map.put("dingdh", dingdh);
		if(Const.DINGD_STATUS_DSX.equals(state)){
			map.put("dingdzt", Const.DINGD_STATUS_JSZ);
		}
		Dingd dingd = (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDingdByDingdh", map);
		//如果订单不存在,新增订单信息
		if(dingd == null){
			dingd = new Dingd();
			dingd.setDingdh(dingdh);//订单号
			dingd.setUsercenter(anxjscszjb.getUsercenter());//用户中心
			dingd.setHeth(anxjscszjb.getGongyhth());// 合同号
			dingd.setDingdlx(dingdlx);//订单类型
			dingd.setGongysdm(gongysdm);//供应商代码
			dingd.setGongyslx(gongyslx);// 供应商类型
			dingd.setChullx(mos);//处理类型,供货模式
			/** hzg 2015.7.9 modify dingdzt由A改为2(待生效) 2015.7.14  还原成A   */
			dingd.setDingdzt(Const.DINGD_STATUS_JSZ);//订单状态(正常为已生效,初始化计算为待生效)
			String createTime = CommonFun.getJavaTime();//创建时间
			dingd.setCreator(user);//订单创建人
			dingd.setCreate_time(createTime);//订单创建时间
			dingd.setEditor(user);//订单编辑人
			dingd.setEdit_time(createTime);//订单编辑时间
			//dingd.setShiffsgys(Const.SHIFFSGYS_NO);// 不是临时订单
			dingd.setDingdjssj(dingdjssj);// 订单计算时间
			dingd.setDingdnr(Const.SHIFOUSHIJIDING);// 既定
			dingd.setFahzq(jiaofrq);//发运周期
			dingd.setZiyhqrq(dingdjssj.substring(0,10));// 资源获取日期
			dingd.setJislx(user);// 系统计算(admin)、用户提交(提交者用户名)
			dingd.setShifzfsyhl(Const.SHIFZFSYHL_YES);// 发送要货令
			/** hzg 2015.7.14 modify 针对按需计算订单类型5或6，修改 active=1，用于手动下发控制在页面可查中间状态A订单  start  */
			if(Const.DINGD_LX_ANX_ZC_C.equals(dingdlx)||Const.DINGD_LX_ANX_ZC_M.equals(dingdlx)){
				dingd.setActive(Const.ACTIVE_1);	//删除标识 add
			}else{
				dingd.setActive(Const.ACTIVE_0);	//删除标识
			}
			/** hzg 2015.7.14 modify  end  */
			//保存订单
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertDingd", dingd);
		}
		return dingd;
	}
	
	/**
	 * 保存订单零件
	 * @param anxjscszjb 按需计算中间表表对象
	 * @param dingd 订单信息
	 * @param user 操作用户
	 * @param gongysdm 供应商代码
	 * @param dingdjssj 订单计算时间
	 * @param yaohrq 要货日期
	 * @param mos 模式
	 * @return 订单零件信息
	 */
	public Dingdlj insertDingdlj(Anxjscszjb anxjscszjb,Dingd dingd,String user,String gongysdm,String dingdjssj,String yaohrq,
			String mos,String jiaofrq){
		
		/**
		 * 保存订单零件
		 */
		Map<String,String> param = new HashMap<String, String>();
		param.put("dingdh", dingd.getDingdh());//订单号
		param.put("usercenter", anxjscszjb.getUsercenter());//用户中心
		param.put("lingjbh", anxjscszjb.getLingjbh());//零件编号
		param.put("gongysdm", gongysdm);//
		//查询订单零件
		Dingdlj dingdlj = (Dingdlj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryDingdlj",param);
		//如果订单零件信息不存在,新增
		if(dingdlj == null){
			dingdlj = new Dingdlj();
			dingdlj.setDingdh(dingd.getDingdh());//订单号
			dingdlj.setP0fyzqxh(jiaofrq);//P0发运周期序号
			dingdlj.setActive(Const.ACTIVE_0);//删;除标识
			dingdlj.setUsercenter(anxjscszjb.getUsercenter());//用户中心
			dingdlj.setLingjbh(anxjscszjb.getLingjbh());//零件编号
			dingdlj.setZuixqdl(anxjscszjb.getZuixqdl());//最小起订量
			dingdlj.setZhidgys(anxjscszjb.getZhidgys());//指定供应商
			dingdlj.setZhizlx(anxjscszjb.getZhizlx());//制造路线
			dingdlj.setFahd(anxjscszjb.getFahd());//发货地
			String createTime = CommonFun.getJavaTime();//创建时间
			dingdlj.setCreate_time(createTime);//创建时间
			dingdlj.setEdit_time(createTime);//编辑时间
			dingdlj.setUabzlx(anxjscszjb.getGysuabzlx());//UA包装类型
			dingdlj.setJihyz(anxjscszjb.getJihy());//计划员组
			dingdlj.setUabzuclx(anxjscszjb.getGysucbzlx());//UA包装内UC类型
			dingdlj.setUabzucsl(anxjscszjb.getGysuaucgs());//UA包装内UC数量
			dingdlj.setUabzucrl(anxjscszjb.getGysucrl());//供应商UC的容量
			dingdlj.setUsbaozlx(anxjscszjb.getCkusbzlx());	//仓库US的包装类型
			dingdlj.setUsbaozrl(anxjscszjb.getCkusbzrl());	//仓库US的包装容量
			dingdlj.setDingdzzsj(dingdjssj);//订单制作时间
			dingdlj.setZiyhqrq(dingdjssj.substring(0,10)) ;//资源获取日期
			dingdlj.setCreator(user);//创建人
			dingdlj.setEditor(user);//编辑人
			dingdlj.setCangkdm(anxjscszjb.getXianbck());//仓库
			dingdlj.setDanw(anxjscszjb.getDanw());//单位
			dingdlj.setUsbaozlx(anxjscszjb.getCkusbzlx());//US包装类型
			dingdlj.setUsbaozrl(anxjscszjb.getCkusbzrl());//US包装容量
			dingdlj.setGonghms(mos);//供货模式
			dingdlj.setLujdm(anxjscszjb.getLujbh());//路径代码
			dingdlj.setGongysdm(gongysdm);//供应商代码
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertDingdlj", dingdlj);
			return dingdlj;
		}
		return null;
	}
	
	/**
	 * 保存订单零件
	 * @param anxjscszjb 按需计算中间表表对象
	 * @param dingd 订单信息
	 * @param user 操作用户
	 * @param gongysdm 供应商代码
	 * @param dingdjssj 订单计算时间
	 * @param yaohrq 要货日期
	 * @param mos 模式
	 * @param lingjgys 零件供应商
	 * @return 订单零件信息
	 */
	public Dingdlj insertDingdlj(Anxjscszjb anxjscszjb,Dingd dingd,String user,String gongysdm,String dingdjssj,String yaohrq,
			String mos,String jiaofrq,LingjGongys lingjgys){
		
		/**
		 * 保存订单零件
		 */
		Map<String,String> param = new HashMap<String, String>();
		param.put("dingdh", dingd.getDingdh());//订单号
		param.put("usercenter", anxjscszjb.getUsercenter());//用户中心
		param.put("lingjbh", anxjscszjb.getLingjbh());//零件编号
		param.put("gongysdm", gongysdm);//
		//查询订单零件
		Dingdlj dingdlj = (Dingdlj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryDingdlj",param);
		//如果订单零件信息不存在,新增
		if(dingdlj == null){
			dingdlj = new Dingdlj();
			dingdlj.setDingdh(dingd.getDingdh());//订单号
			dingdlj.setP0fyzqxh(jiaofrq);//P0发运周期序号
			dingdlj.setActive(Const.ACTIVE_0);//删;除标识
			dingdlj.setUsercenter(anxjscszjb.getUsercenter());//用户中心
			dingdlj.setLingjbh(anxjscszjb.getLingjbh());//零件编号
			dingdlj.setZuixqdl(anxjscszjb.getZuixqdl());//最小起订量
			dingdlj.setZhidgys(anxjscszjb.getZhidgys());//指定供应商
			dingdlj.setZhizlx(anxjscszjb.getZhizlx());//制造路线
			dingdlj.setFahd(anxjscszjb.getFahd());//发货地
			String createTime = CommonFun.getJavaTime();//创建时间
			dingdlj.setCreate_time(createTime);//创建时间
			dingdlj.setEdit_time(createTime);//编辑时间
			dingdlj.setUabzlx(lingjgys.getUabzlx());//UA包装类型
			dingdlj.setJihyz(anxjscszjb.getJihy());//计划员组
			dingdlj.setUabzuclx(lingjgys.getUcbzlx());//UA包装内UC类型
			dingdlj.setUabzucsl(lingjgys.getUaucgs());//UA包装内UC数量
			dingdlj.setUabzucrl(lingjgys.getUcrl());//供应商UC的容量
			dingdlj.setUsbaozlx(anxjscszjb.getCkusbzlx());	//仓库US的包装类型
			dingdlj.setUsbaozrl(anxjscszjb.getCkusbzrl());	//仓库US的包装容量
			dingdlj.setDingdzzsj(dingdjssj);//订单制作时间
			dingdlj.setZiyhqrq(dingdjssj.substring(0,10)) ;//资源获取日期
			dingdlj.setCreator(user);//创建人
			dingdlj.setEditor(user);//编辑人
			dingdlj.setCangkdm(anxjscszjb.getXianbck());//仓库
			dingdlj.setDanw(anxjscszjb.getDanw());//单位
			dingdlj.setUsbaozlx(anxjscszjb.getCkusbzlx());//US包装类型
			dingdlj.setUsbaozrl(anxjscszjb.getCkusbzrl());//US包装容量
			dingdlj.setGonghms(mos);//供货模式
			dingdlj.setLujdm(anxjscszjb.getLujbh());//路径代码
			dingdlj.setGongysdm(gongysdm);//供应商代码
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertDingdlj", dingdlj);
			return dingdlj;
		}
		return null;
	}
	
	/**
	 * 保存订单明细
	 * @param dingd 订单信息
	 * @param anxjscszjb 按需计算中间表对象
	 * @param anxmaoxqzjb 按需毛需求中间表对象
	 * @param xiaohSj 消耗时间
	 * @param yaohRq 要货日期
	 * @param timeMap 时间结果
	 * @param yaohl 要货数量
	 * @param user 操作用户
	 * @param state 订单状态
	 * @param gongysdm 供应商代码
	 * @param gongyslx 供应商类型
	 * @param dingdjssj 订单计算时间
	 * @return 订单明细信息
	 */
	public Dingdmx insertDingdmx(Dingd dingd,Anxjscszjb anxjscszjb,AnxMaoxqzjb anxmaoxqzjb,String xiaohSj,String yaohRq,
			Map<String,String> timeMap,BigDecimal yaohl,BigDecimal jissl,String mos,String user,String state,String gongysdm,
			String gongyslx,String dingdjssj){
		//如果要货数量不为0,保存订单明细
			/**
			 * 保存订单明细
			 */
			Dingdmx dingdmx = new Dingdmx();
			dingdmx.setDingdh(dingd.getDingdh());//订单号
			dingdmx.setActive(Const.ACTIVE_1);//删除标识
			dingdmx.setUsercenter(anxjscszjb.getUsercenter());//用户中心
			dingdmx.setLingjbh(anxjscszjb.getLingjbh());//零件号
			dingdmx.setGongysdm(gongysdm);//供应商代码
			dingdmx.setGongyslx(gongyslx);//供应商类型
			dingdmx.setZhidgys(anxjscszjb.getZhidgys());//指定供应商
			dingdmx.setLingjmc(anxjscszjb.getZhongwmc());//零件名称
			//C1CD模式查询供应商名称
			if(Const.ANX_MS_CD.equals(mos) || Const.ANX_MS_C1.equals(mos)){
				//查询供应商名称
				Gongys gongys = (Gongys) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryGysmc",dingdmx);
				if(gongys != null){
					dingdmx.setGongsmc(gongys.getGongsmc());//供应商名称
				}
			}
			dingdmx.setYaohqsrq(yaohRq);//要货起始日期
			dingdmx.setYaohjsrq(yaohRq);//要货结束日期
			dingdmx.setCangkdm(anxjscszjb.getXianbck());//仓库代码
			dingdmx.setXiehzt(anxjscszjb.getXiehztbh());//卸货站台
			dingdmx.setFahd(anxjscszjb.getFahd());	//发货地
			dingdmx.setDinghcj(anxmaoxqzjb.getChejh());//订货车间
			dingdmx.setDanw(anxjscszjb.getDanw());//单位
			dingdmx.setJiaofrq(timeMap.get("zuiwdhsj").substring(0,10));//交付日期
			dingdmx.setZhuangt(state);//状态
			dingdmx.setLeix(Const.SHIFOUSHIJIDING);//既定
			dingdmx.setLujdm(anxjscszjb.getLujbh());//路径代码
			dingdmx.setJihyz(anxjscszjb.getJihy());//计划员组
			dingdmx.setZuihwhr(user);// 订单维护人
			String createtime = CommonFun.getJavaTime();//创建时间
			dingdmx.setZuihwhsj(dingdjssj);// 维护时间
			dingdmx.setGcbh(anxjscszjb.getGcbh());//承运商编号
			dingdmx.setBeihsj2(anxjscszjb.getBeihsj());//仓库备货时间
			dingdmx.setGonghlx(mos);//供货类型
			dingdmx.setShid(xiaohSj);//时段
			dingdmx.setUabzlx(anxjscszjb.getGysuabzlx());//UA包装类型
			dingdmx.setUabzuclx(anxjscszjb.getGysucbzlx());//UA包装内UC类型
			dingdmx.setUabzucsl(anxjscszjb.getGysuaucgs());//UA包装内UC数量
			dingdmx.setUabzucrl(anxjscszjb.getGysucrl());//供应商UC的容量
			dingdmx.setUsbaozlx(anxjscszjb.getCkusbzlx());	//仓库US的包装类型
			dingdmx.setUsbaozrl(anxjscszjb.getCkusbzrl());	//仓库US的包装容量
			dingdmx.setXiaohsj(anxmaoxqzjb.getXhsj().substring(0,16));//消耗时间
			dingdmx.setXianbyhlx(anxjscszjb.getXianbyhlx());//线边要货类型：K/R
			dingdmx.setXiaohch(anxjscszjb.getXiaohcbh());	//小火车号
			dingdmx.setXiaohccxh(anxjscszjb.getXiaohccxbh());//小火车车厢号
			dingdmx.setChanx(anxjscszjb.getShengcxbh());		//产线
			dingdmx.setXiaohd(anxjscszjb.getXiaohd());	//消耗点
			dingdmx.setFenpxh(anxjscszjb.getFenpbh());//分配循环
			dingdmx.setCreator(user);// 创建人
			dingdmx.setCreate_time(createtime);// 创建时间
			dingdmx.setEditor(user);// 修改人
			dingdmx.setEdit_time(createtime);// 修改时间
			dingdmx.setZuizdhsj(timeMap.get("zuizdhsj"));// 最早到货时间
			dingdmx.setZuiwdhsj(timeMap.get("zuiwdhsj"));// 最晚到货时间
			String faysj = timeMap.get("faysj");//发运时间
			//如果发运时间为空
			if(StringUtils.isEmpty(faysj)){
				faysj = timeMap.get("zuizdhsj");
			}
			dingdmx.setFaysj(timeMap.get("faysj"));//发运日期
			dingdmx.setGongyfe(anxjscszjb.getGongysfe());//供应份额
			dingdmx.setZuixqdl(anxjscszjb.getZuixqdl());//最小起订量
			dingdmx.setLingjsx(anxjscszjb.getLingjsx());//零件属性(A零件,K卷料,M辅料)
			// CDMD模式保存小火车备货时间
			if(Const.ANX_MS_CD.equals(mos) || Const.ANX_MS_MD.equals(mos)){
				dingdmx.setXiaohcbhsj(timeMap.get("beihsj"));//小火车备货时间
				dingdmx.setXiaohcsxsj(timeMap.get("shangxsj"));//小火车上线时间	
				dingdmx.setTangc(this.getBigDecimalAx(timeMap.get("tangc")));//小火车趟次
			}else if(Const.ANX_MS_C1.equals(mos))	{
				dingdmx.setXiaohcsxsj(timeMap.get("zuiwdhsj"));//C1模式保存小火车上线时间=最晚到货时间
			}
			dingdmx.setShul(yaohl);//要货数量
			dingdmx.setJissl(jissl);//计算数量
			return dingdmx;
	}

	/**
	 * 取指定供应商的物流路径0011221 gswang 2015-03-06
	 * @param dingd 订单信息
	 * @param anxjscszjb 按需计算中间表对象
	 * @param anxmaoxqzjb 按需毛需求中间表对象
	 * @param xiaohSj 消耗时间
	 * @param yaohRq 要货日期
	 * @param timeMap 时间结果
	 * @param yaohl 要货数量
	 * @param user 操作用户
	 * @param state 订单状态
	 * @param gongysdm 供应商代码
	 * @param gongyslx 供应商类型
	 * @param dingdjssj 订单计算时间
	 * @param wul 物流路径
	 * @return 订单明细信息
	 */
	public Dingdmx insertDingdmxZdgys(Dingd dingd,Anxjscszjb anxjscszjb,AnxMaoxqzjb anxmaoxqzjb,String xiaohSj,String yaohRq,
			Map<String,String> timeMap,BigDecimal yaohl,BigDecimal jissl,String mos,String user,String state,String gongysdm,
			String gongyslx,String dingdjssj,Wullj wul){
		//如果要货数量不为0,保存订单明细
			/**
			 * 保存订单明细
			 */
			Dingdmx dingdmx = new Dingdmx();
			dingdmx.setDingdh(dingd.getDingdh());//订单号
			dingdmx.setActive(Const.ACTIVE_1);//删除标识
			dingdmx.setUsercenter(anxjscszjb.getUsercenter());//用户中心
			dingdmx.setLingjbh(anxjscszjb.getLingjbh());//零件号
			dingdmx.setGongysdm(gongysdm);//供应商代码
			dingdmx.setGongyslx(gongyslx);//供应商类型
			dingdmx.setZhidgys(anxjscszjb.getZhidgys());//指定供应商
			dingdmx.setLingjmc(anxjscszjb.getZhongwmc());//零件名称
			//C1CD模式查询供应商名称
			if(Const.ANX_MS_CD.equals(mos) || Const.ANX_MS_C1.equals(mos)){
				//查询供应商名称
				Gongys gongys = (Gongys) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryGysmc",dingdmx);
				if(gongys != null){
					dingdmx.setGongsmc(gongys.getGongsmc());//供应商名称
				}
			}
			dingdmx.setYaohqsrq(yaohRq);//要货起始日期
			dingdmx.setYaohjsrq(yaohRq);//要货结束日期
			dingdmx.setCangkdm(anxjscszjb.getXianbck());//仓库代码
			dingdmx.setXiehzt(anxjscszjb.getXiehztbh());//卸货站台
			dingdmx.setFahd(anxjscszjb.getFahd());	//发货地
			dingdmx.setDinghcj(anxmaoxqzjb.getChejh());//订货车间
			dingdmx.setDanw(anxjscszjb.getDanw());//单位
			dingdmx.setJiaofrq(timeMap.get("zuiwdhsj").substring(0,10));//交付日期
			dingdmx.setZhuangt(state);//状态
			dingdmx.setLeix(Const.SHIFOUSHIJIDING);//既定
			dingdmx.setJihyz(anxjscszjb.getJihy());//计划员组
			dingdmx.setZuihwhr(user);// 订单维护人
			String createtime = CommonFun.getJavaTime();//创建时间
			dingdmx.setZuihwhsj(dingdjssj);// 维护时间
			if(wul!=null){
				dingdmx.setLujdm(wul.getLujbh());//路径代码
				dingdmx.setGcbh(wul.getGcbh());//承运商编号
			}else{
				dingdmx.setLujdm(anxjscszjb.getLujbh());//路径代码
				dingdmx.setGcbh(anxjscszjb.getGcbh());//承运商编号
			}
			dingdmx.setBeihsj2(anxjscszjb.getBeihsj());//仓库备货时间
			dingdmx.setGonghlx(mos);//供货类型
			dingdmx.setShid(xiaohSj);//时段
			dingdmx.setUabzlx(anxjscszjb.getGysuabzlx());//UA包装类型
			dingdmx.setUabzuclx(anxjscszjb.getGysucbzlx());//UA包装内UC类型
			dingdmx.setUabzucsl(anxjscszjb.getGysuaucgs());//UA包装内UC数量
			dingdmx.setUabzucrl(anxjscszjb.getGysucrl());//供应商UC的容量
			dingdmx.setUsbaozlx(anxjscszjb.getCkusbzlx());	//仓库US的包装类型
			dingdmx.setUsbaozrl(anxjscszjb.getCkusbzrl());	//仓库US的包装容量
			dingdmx.setXiaohsj(anxmaoxqzjb.getXhsj().substring(0,16));//消耗时间
			dingdmx.setXianbyhlx(anxjscszjb.getXianbyhlx());//线边要货类型：K/R
			dingdmx.setXiaohch(anxjscszjb.getXiaohcbh());	//小火车号
			dingdmx.setXiaohccxh(anxjscszjb.getXiaohccxbh());//小火车车厢号
			dingdmx.setChanx(anxjscszjb.getShengcxbh());		//产线
			dingdmx.setXiaohd(anxjscszjb.getXiaohd());	//消耗点
			dingdmx.setFenpxh(anxjscszjb.getFenpbh());//分配循环
			dingdmx.setCreator(user);// 创建人
			dingdmx.setCreate_time(createtime);// 创建时间
			dingdmx.setEditor(user);// 修改人
			dingdmx.setEdit_time(createtime);// 修改时间
			dingdmx.setZuizdhsj(timeMap.get("zuizdhsj"));// 最早到货时间
			dingdmx.setZuiwdhsj(timeMap.get("zuiwdhsj"));// 最晚到货时间
			String faysj = timeMap.get("faysj");//发运时间
			//如果发运时间为空
			if(StringUtils.isEmpty(faysj)){
				faysj = timeMap.get("zuizdhsj");
			}
			dingdmx.setFaysj(timeMap.get("faysj"));//发运日期
			dingdmx.setGongyfe(anxjscszjb.getGongysfe());//供应份额
			dingdmx.setZuixqdl(anxjscszjb.getZuixqdl());//最小起订量
			dingdmx.setLingjsx(anxjscszjb.getLingjsx());//零件属性(A零件,K卷料,M辅料)
			// CDMD模式保存小火车备货时间
			if(Const.ANX_MS_CD.equals(mos) || Const.ANX_MS_MD.equals(mos)){
				dingdmx.setXiaohcbhsj(timeMap.get("beihsj"));//小火车备货时间
				dingdmx.setXiaohcsxsj(timeMap.get("shangxsj"));//小火车上线时间	
				dingdmx.setTangc(this.getBigDecimalAx(timeMap.get("tangc")));//小火车趟次
			}else if(Const.ANX_MS_C1.equals(mos))	{
				dingdmx.setXiaohcsxsj(timeMap.get("zuiwdhsj"));//C1模式保存小火车上线时间=最晚到货时间
			}
			dingdmx.setShul(yaohl);//要货数量
			dingdmx.setJissl(jissl);//计算数量
			return dingdmx;
	}
	
	/**
	 * 保存订单明细
	 * @param dingd 订单信息
	 * @param anxjscszjb 按需计算中间表对象
	 * @param anxmaoxqzjb 按需毛需求中间表对象
	 * @param xiaohSj 消耗时间
	 * @param yaohRq 要货日期
	 * @param timeMap 时间结果
	 * @param yaohl 要货数量
	 * @param user 操作用户
	 * @param state 订单状态
	 * @param gongys 供应商
	 * @param dingdjssj 订单计算时间
	 * @param Wullj 物流路径信息
	 * @param lingjgys 零件供应商
	 * @return 订单明细信息
	 */
	public Dingdmx insertDingdmx(Dingd dingd,Anxjscszjb anxjscszjb,AnxMaoxqzjb anxmaoxqzjb,String xiaohSj,String yaohRq,
			Map<String,String> timeMap,BigDecimal yaohl,BigDecimal jissl,String mos,String user,String state,
			String dingdjssj,Wullj wul,LingjGongys lingjgys){
		
			/**
			 * 保存订单明细
			 */
			Dingdmx dingdmx = new Dingdmx();
			dingdmx.setDingdh(dingd.getDingdh());//订单号
			dingdmx.setActive(Const.ACTIVE_1);//删除标识
			dingdmx.setUsercenter(anxjscszjb.getUsercenter());//用户中心
			dingdmx.setLingjbh(anxjscszjb.getLingjbh());//零件号
			dingdmx.setGongysdm(lingjgys.getGongysbh());//供应商代码
			dingdmx.setGongyslx(lingjgys.getLeix());//供应商类型
			dingdmx.setZhidgys(anxjscszjb.getZhidgys());//指定供应商
			dingdmx.setLingjmc(anxjscszjb.getZhongwmc());//零件名称
			dingdmx.setGongsmc(lingjgys.getGongsmc());//供应商名称
			dingdmx.setYaohqsrq(yaohRq);//要货起始日期
			dingdmx.setYaohjsrq(yaohRq);//要货结束日期
			dingdmx.setCangkdm(anxjscszjb.getXianbck());//仓库代码
			dingdmx.setXiehzt(anxjscszjb.getXiehztbh());//卸货站台
			dingdmx.setFahd(wul.getFahd());	//发货地
			dingdmx.setDinghcj(anxmaoxqzjb.getChejh());//订货车间
			dingdmx.setDanw(anxjscszjb.getDanw());//单位
			dingdmx.setJiaofrq(timeMap.get("zuiwdhsj").substring(0,10));//交付日期
			dingdmx.setZhuangt(state);//状态
			dingdmx.setLeix(Const.SHIFOUSHIJIDING);//既定
			dingdmx.setLujdm(wul.getLujbh());//路径代码
			dingdmx.setJihyz(anxjscszjb.getJihy());//计划员组
			dingdmx.setZuihwhr(user);// 订单维护人
			String createtime = CommonFun.getJavaTime();//创建时间
			dingdmx.setZuihwhsj(dingdjssj);// 维护时间
			dingdmx.setGcbh(wul.getGcbh());//承运商编号
			dingdmx.setBeihsj2(anxjscszjb.getBeihsj());//仓库备货时间
			dingdmx.setGonghlx(mos);//供货类型
			dingdmx.setShid(xiaohSj);//时段
			dingdmx.setUabzlx(lingjgys.getUabzlx());//UA包装类型
			dingdmx.setUabzuclx(lingjgys.getUcbzlx());//UA包装内UC类型
			dingdmx.setUabzucsl(lingjgys.getUaucgs());//UA包装内UC数量
			dingdmx.setUabzucrl(lingjgys.getUcrl());//供应商UC的容量
			dingdmx.setUsbaozlx(anxjscszjb.getCkusbzlx());	//仓库US的包装类型
			dingdmx.setUsbaozrl(anxjscszjb.getCkusbzrl());	//仓库US的包装容量
			dingdmx.setXiaohsj(anxmaoxqzjb.getXhsj().substring(0,16));//消耗时间
			dingdmx.setXianbyhlx(anxjscszjb.getXianbyhlx());//线边要货类型：K/R
			dingdmx.setXiaohch(anxjscszjb.getXiaohcbh());	//小火车号
			dingdmx.setXiaohccxh(anxjscszjb.getXiaohccxbh());//小火车车厢号
			dingdmx.setChanx(anxjscszjb.getShengcxbh());		//产线
			dingdmx.setXiaohd(anxjscszjb.getXiaohd());	//消耗点
			dingdmx.setFenpxh(anxjscszjb.getFenpbh());//分配循环
			dingdmx.setCreator(user);// 创建人
			dingdmx.setCreate_time(createtime);// 创建时间
			dingdmx.setEditor(user);// 修改人
			dingdmx.setEdit_time(createtime);// 修改时间
			dingdmx.setZuizdhsj(timeMap.get("zuizdhsj"));// 最早到货时间
			dingdmx.setZuiwdhsj(timeMap.get("zuiwdhsj"));// 最晚到货时间
			String faysj = timeMap.get("faysj");//发运时间
			//如果发运时间为空
			if(StringUtils.isEmpty(faysj)){
				faysj = timeMap.get("zuizdhsj");
			}
			dingdmx.setFaysj(timeMap.get("faysj"));//发运日期
			dingdmx.setGongyfe(anxjscszjb.getGongysfe());//供应份额
			dingdmx.setZuixqdl(anxjscszjb.getZuixqdl());//最小起订量
			dingdmx.setLingjsx(anxjscszjb.getLingjsx());//零件属性(A零件,K卷料,M辅料)
			// CDMD模式保存小火车备货时间
			if(Const.ANX_MS_CD.equals(mos) || Const.ANX_MS_MD.equals(mos)){
				dingdmx.setXiaohcbhsj(timeMap.get("beihsj"));//小火车备货时间
				dingdmx.setXiaohcsxsj(timeMap.get("shangxsj"));//小火车上线时间	
				dingdmx.setTangc(this.getBigDecimalAx(timeMap.get("tangc")));//小火车趟次
			}else if(Const.ANX_MS_C1.equals(mos))	{
				dingdmx.setXiaohcsxsj(timeMap.get("zuiwdhsj"));//M1模式保存小火车上线时间=最晚到货时间
			}
			dingdmx.setShul(yaohl);//要货数量
			dingdmx.setJissl(jissl);//计算数量
			return dingdmx;
	}
	
	/**
	 * 供应商份额差异比较
	 * @param listLingjGys 要比较的零件供应商集合
	 * @param usercenter 用户中心
	 * @param lingjbh 零件编号
	 * @return 实际份额差异最低的供应商,如果实际份额差异相等,则为计划份额高的供应商
	 */
	public GongysFeneJis compareFenecy(List<LingjGongys> listLingjGys,String usercenter,String lingjbh,Map<String,GongysFeneJis> gongysFeneMap){
		
		/**
		 * 供应商份额差异比较
		 */
		//供应商份额比较temp对象
		GongysFeneJis gongysFeneTemp = new GongysFeneJis();
		gongysFeneTemp.setJihfene(BigDecimal.ZERO);//计划份额
		gongysFeneTemp.setGongyssjghfecy(BigDecimal.ONE);//供应商份额差异
		//遍历零件供应商信息集合
		for (int k = 0; k < listLingjGys.size(); k++) {
			LingjGongys lingjGongys = listLingjGys.get(k);
			//获取供应商份额对象
			GongysFeneJis gongysFene = gongysFeneMap.get(usercenter+lingjbh+lingjGongys.getGongysbh());
			//比较实际份额差异
			int num = gongysFene.getGongyssjghfecy().compareTo(gongysFeneTemp.getGongyssjghfecy());
			//如果实际份额差异较低,则取份额较低的对象
			if(num < 0){
				gongysFeneTemp = gongysFene;
			}else if(num == 0){
				//如果实际份额差异相等,则比较计划份额,取计划份额较高的对象
				if(gongysFene.getJihfene().compareTo(gongysFeneTemp.getJihfene()) >= 0){
					gongysFeneTemp = gongysFene;
				}
			}
		}
		return gongysFeneTemp;
	}
	
	/**
	 * 份额计算回退
	 * @param shul 回退数量
	 * @param gongysFeneMap 供应商份额计算缓存MAP
	 * @param lingjSlMap 零件累积缓存MAP
	 * @param lingjgys 零件供应商对象
	 */
	public void fenEHuit(BigDecimal shul,Map<String,GongysFeneJis> gongysFeneMap,Map<String,BigDecimal> lingjSlMap,
			LingjGongys lingjGongys){
		GongysFeneJis gongysFene = gongysFeneMap.get(lingjGongys.getUsercenter()+lingjGongys.getLingjbh()+lingjGongys.getGongysbh());
		BigDecimal lingjlj = lingjSlMap.get(lingjGongys.getUsercenter()+lingjGongys.getLingjbh());
		//供应商累积回退
		gongysFene.setGongyslj(gongysFene.getGongyslj().subtract(shul));
		//零件累积回退
		lingjlj = lingjlj.subtract(shul);
		if(lingjlj.compareTo(BigDecimal.ZERO) != 0){
			//供应商份额差异 = 供应商累积 / 零件累积 - 供应商计划份额
			gongysFene.setGongyssjghfecy(gongysFene.getGongyslj().divide(lingjlj.add(shul),5, BigDecimal.ROUND_CEILING).subtract(gongysFene.getJihfene()));
		}else{
			gongysFene.setGongyssjghfecy(BigDecimal.ZERO);
		}
		gongysFeneMap.put(lingjGongys.getUsercenter()+lingjGongys.getLingjbh()+lingjGongys.getGongysbh(),gongysFene);
		lingjSlMap.put(lingjGongys.getUsercenter()+lingjGongys.getLingjbh(),lingjlj);
		
	}
	
	/**
	 * 份额计算
	 * @param listLingjGys 零件供应商集合
	 * @param baozrl 包装容量
	 * @param lingjlj 零件累积
	 * @param shul 实际要货数量
	 * @param usercenter 用户中心
	 * @param lingjbh 零件编号
	 */
	public void fenEJis(List<LingjGongys> listLingjGys,BigDecimal baozrl,Map<String,BigDecimal> lingjSlMap,BigDecimal shul,
			String usercenter,String lingjbh,Map<String,GongysFeneJis> gongysFeneMap){
		BigDecimal yifpsl = BigDecimal.ZERO;
		/** 初始化参数start hzg 2015.6.30 11507 **/
		BigDecimal biaodsl = BigDecimal.ZERO;
		BigDecimal ckLingjljsl = BigDecimal.ZERO;
		Map<String,String> paraMap = new HashMap<String,String>();
		paraMap.put("usercenter", usercenter);
		paraMap.put("lingjbh", lingjbh);
		/** 初始化参数end **/
		//零件累积
		BigDecimal lingjlj = this.getBigDecimalAx(lingjSlMap.get(usercenter+lingjbh));
		/** 仓库零件供应商零件累计量  start hzg 2015.7.13 11507  **/
		ckLingjljsl = this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryLingjljOfCKLingjgys", paraMap));
		/** 仓库零件供应商零件累计量   hzg 2015.7.13 11507  **/
		//mantis:0012855 20160919 by CSY 先遍历零件供应商集合，得到计算要货总量和每个供应商的计算要货量
		BigDecimal lingjjsTotal = BigDecimal.ZERO;	//根据公式计算的零件总量
		//遍历零件供应商集合
		for (int i = 0; i < listLingjGys.size(); i++) {
			LingjGongys lingjGongys = listLingjGys.get(i);
			//获取供应商份额对象
			GongysFeneJis gongysFene = gongysFeneMap.get(usercenter+lingjbh+lingjGongys.getGongysbh());
			/** 仓库零件供应商累计量获取 start hzg 2015.6.30 11507  **/
			paraMap.put("gongysbh", lingjGongys.getGongysbh());
			//零件供应商累计
			biaodsl = this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryBiaodslOfCKLingjgys", paraMap));
			/** 仓库零件供应商累计获取  end  **/
			//如果对象为空,新建,保存
			if(gongysFene == null){
				gongysFene = new GongysFeneJis();
				gongysFene.setGongysbh(lingjGongys.getGongysbh());//供应商编号
				gongysFene.setYaohsl(BigDecimal.ZERO);//当前要货数量
				gongysFene.setJihfene(lingjGongys.getGongyfe());//计划份额
				gongysFene.setGongyslj(biaodsl);//供应商累积  update hzg ZERO to biaodsl
				gongysFene.setGongyssjghfecy(BigDecimal.ZERO);//供应商份额差异
				/** ck_lingjgys零件要货量汇总  hzg 2015.7.13  **/
				lingjlj = ckLingjljsl;
			}
			BigDecimal gongysjsyhl = (lingjlj.add(shul)).multiply(lingjGongys.getGongyfe()).subtract(gongysFene.getGongyslj());
			if (gongysjsyhl.compareTo(BigDecimal.ZERO) > 0) {
				lingjjsTotal = lingjjsTotal.add(gongysjsyhl);
			}else {
				gongysjsyhl = BigDecimal.ZERO;
			}
			gongysFene.setYaohsl(gongysjsyhl);
			gongysFeneMap.put(usercenter+lingjbh+lingjGongys.getGongysbh(),gongysFene);
		}
		
		//再次遍历零件供应商集合
		for (int k = 0; k < listLingjGys.size(); k++) {
			LingjGongys lingjGongys = listLingjGys.get(k);
			//获取供应商份额对象
			GongysFeneJis gongysFene = gongysFeneMap.get(usercenter+lingjbh+lingjGongys.getGongysbh());
			
			//供应商要货量 = (本次要货量*供应商计算数量/计算总量)按包装向下取整
			BigDecimal gongysyhl = this.roundingByPackDownAx(shul.multiply(gongysFene.getYaohsl()).divide(lingjjsTotal,5, BigDecimal.ROUND_CEILING),
					baozrl);
			//当前要货数量
			gongysFene.setYaohsl(gongysyhl);
			//已分配数量 = 已分配数量 + 供应商要货量
			yifpsl = yifpsl.add(gongysyhl);
			//供应商计划份额 = 零件供应商份额
			gongysFene.setJihfene(lingjGongys.getGongyfe());
			//供应商累积= 供应商累积 + 供应商要货量
			gongysFene.setGongyslj(gongysFene.getGongyslj().add(gongysyhl));
			//供应商份额差异 = 供应商累积 / 零件累积 - 供应商计划份额，fenecy应该是个负数
			gongysFene.setGongyssjghfecy(gongysFene.getGongyslj().divide(lingjlj.add(shul),5, BigDecimal.ROUND_CEILING).subtract(lingjGongys.getGongyfe()));
			logger.info("供应商份额差异："+gongysFene.getGongyssjghfecy());
			gongysFeneMap.put(usercenter+lingjbh+lingjGongys.getGongysbh(),gongysFene);
		}
		//计算未分配量，如果是负数，表示这个供应商的能完全满足要货量，不需要再分配
		BigDecimal weifpsl = shul.subtract(yifpsl);
		//如果存在未分配量,则未分配量为一个包装容量
		if(weifpsl.signum() > 0){
			//mantis:0012855 20160919 by CSY 未分配数量 = （未分配数量 / 包装容量）向上取整 * 包装容量
			weifpsl = weifpsl.divide(baozrl,5, BigDecimal.ROUND_CEILING).setScale(0, 0).multiply(baozrl);
			//获取份额差异最低的供应商,将未分配量分配给该供应商
			GongysFeneJis gongysFene = compareFenecy(listLingjGys,usercenter, lingjbh,gongysFeneMap);
			//供应商当前要货量  = 供应商当前要货量 + 未分配数量
			gongysFene.setYaohsl(gongysFene.getYaohsl().add(weifpsl));
			//供应商累积= 供应商累积 + 供应商要货量
			gongysFene.setGongyslj(gongysFene.getGongyslj().add(weifpsl));
			//供应商份额差异 = 供应商累积 / 零件累积 - 供应商计划份额
			gongysFene.setGongyssjghfecy(gongysFene.getGongyslj().divide(lingjlj.add(shul),5, BigDecimal.ROUND_CEILING).subtract(gongysFene.getJihfene()));
			gongysFeneMap.put(usercenter+lingjbh+gongysFene.getGongysbh(),gongysFene);
		}
		lingjSlMap.put(usercenter+lingjbh, lingjlj.add(shul));
	}
	
	/**
	 * 更新线边理论库存
	 * @param user 操作人
	 * @param shangcjssj 上次计算时间
	 * @param jiessj 结束时间
	 * @param lingjxhd 零件消耗点
	 */
	public Lingjxhd updateXbllkc(String user,String shangcjssj,String jiessj,Lingjxhd lingjxhd) {
		
		/**
		 * 更线边理论库存
		 */
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", lingjxhd.getUsercenter());//用户中心
		map.put("lingjbh", lingjxhd.getLingjbh());//零件编号
		map.put("xiaohd", lingjxhd.getXiaohdbh());//消耗点编号
		map.put("xuqly", "3");//计算线边理论库存用SPPV部分毛需求
		//计算库存待消耗,从上次计算时间到结束时间段内的消耗数量
		BigDecimal kucslDxh = BigDecimal.ZERO;
		Kucjscsb kucsl = null;
		if(!StringUtils.isEmpty(shangcjssj) && !StringUtils.isEmpty(jiessj)){
			map.put("xiaohsj2", jiessj.substring(0, 16));
			map.put("xiaohsj", shangcjssj.substring(0, 16));
			kucslDxh = this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryXiaohl", map));
			//在库存计算参数表中获取到统计后的异常总量,到库量
			kucsl = (Kucjscsb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryYcxhDh",map);
		}
		if(kucsl == null){
			kucsl = new Kucjscsb();
			kucsl.setDaohl(BigDecimal.ZERO);//到货量,为空视为0
			kucsl.setYicxhl(BigDecimal.ZERO);//异常消耗,为空视为0
		}
		logger.info("更新线边理论库存:"+map+"库存待消耗:"+kucslDxh+"到货量"+kucsl.getDaohl()+"异常消耗:"+kucsl.getYicxhl()+"更新前线边理论库存:"+lingjxhd.getXianbllkc());
		// 设置现编理论库存 = 线边理论库存 + 到库量 - 异常消耗量 - 库存待消耗
		lingjxhd.setXianbllkc(this.getBigDecimalAx(lingjxhd.getXianbllkc()).add(this.getBigDecimalAx(kucsl.getDaohl()))
				.subtract(this.getBigDecimalAx(kucsl.getYicxhl())).subtract(kucslDxh));
		lingjxhd.setEditor(user);//编辑人
		lingjxhd.setEdit_time(CommonFun.getJavaTime());//编辑时间
		logger.info("更新后线边理论库存:"+lingjxhd.getXianbllkc());
		return lingjxhd;
	}
	
	/**
	 * 计算订单
	 * @param mos 模式
	 * @param anxjscszjb 按需计算初始中间表对象
	 * @param yaohl 要货数量
	 * @param param 查询参数
	 * @param user 操作用户
	 * @param dingdjssj 订单计算时间
	 * @param gongyslx 供应商类型
	 * @param dingdlx 订单类型
	 * @param listDingdlj 订单零件集合
	 * @param anxMaoxqzjb 按需毛需求中间对象
	 * @param xiaohSj 消耗时间
	 * @param timeMap 时间结果
	 * @param gongysdm 供应商代码
	 * @param state 订单状态
	 * @return 订单,订单零件,订单,资源
	 */
	public Map calculateDingd(String mos,Anxjscszjb anxjscszjb,BigDecimal yaohl,Map<String,String> param,String user,
			String dingdjssj,String gongyslx,String dingdlx,AnxMaoxqzjb anxMaoxqzjb,String xiaohSj,
			Map<String,String> timeMap,String gongysdm,String state,List<Dingdmx> listDingdmx,List<Dingdlj> listDingdlj,
			Map<String,BigDecimal> lingjSlMap,Map<String,List<LingjGongys>> lingjgysMap,Map<String,GongysFeneJis> gongysFeneMap,Map<String,Wullj> wulljMap,
			HashMap<String, String> dingdhMap){
		BigDecimal ziy = BigDecimal.ZERO;//资源
		BigDecimal shul = BigDecimal.ZERO;//数量		
		String yaohRq = xiaohSj.substring(0,10);//要货日期
		String jiaorRq = timeMap.get("zuiwdhsj").substring(0,10);//交付日期
		/**
		 * 生成订单,订单零件,订单明细信息
		 */
		Dingd dingd = new Dingd();//订单
		Dingdlj dingdlj = new Dingdlj();//订单零件
		Dingdmx dingdmx = new Dingdmx();//订单明细
		Map result = new HashMap();
		//如果是C1CD模式需进行份额计算
		if(Const.ANX_MS_C1.equals(mos) || Const.ANX_MS_CD.equals(mos)){

			/**
			 * 份额计算
			 */
			//包装容量 = uc容量*uauc个数
			BigDecimal baozrl = anxjscszjb.getGysucrl().multiply(anxjscszjb.getGysuaucgs());
			shul = this.roundingByPackAx(yaohl, baozrl);
			
			//指定供应商为空,不指定供应商,需进行份额计算,根据不同供应商生成订单
			if(StringUtils.isEmpty(anxjscszjb.getZhidgys())){
				param.put("gonghlx", Const.ZHIZAOLUXIAN_IL);
				//获取零件供应商集合
				List<LingjGongys> listLingjGys = lingjgysMap.get(param.get("usercenter")+param.get("lingjbh"));
				if(listLingjGys==null){
					result.put("biaos", "break");
					logger.info("零件无供应商");
					return result;
				}
				//如果零件供应商为数量为1,单独供应商供货
				if(listLingjGys.size() == 1){
					LingjGongys lingjGongys = listLingjGys.get(0);
					String gongysbh = listLingjGys.get(0).getGongysbh();
										
					//查询物流路径取承运商编号
					String wkey = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + gongysbh + anxjscszjb.getFenpbh();	
					Wullj wullj = wulljMap.get(wkey);
					if(wullj == null){
						saveYicbj(anxjscszjb, user, "物流路径信息未查询到"+anxjscszjb.getFenpbh(), mos);
						result.put("biaos", "break");						
						return result;
					}
					//保存订单
					dingd = insertDingd(anxjscszjb, lingjGongys.getGongysbh(), mos, user, dingdjssj, lingjGongys.getLeix(), dingdlx,
							state,dingdhMap,jiaorRq);
					//保存订单零件
					dingdlj = insertDingdlj(anxjscszjb, dingd, user, lingjGongys.getGongysbh(), dingdjssj, yaohRq, mos,jiaorRq,lingjGongys);
					if(dingdlj != null){
						listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
					}
					
					//如果要货数量不为0,保存订单明细
					if(yaohl.signum() != 0){
					//保存订单明细  modify：gysYaohsl = gongysFene.getYaohsl()  by hzg		
						dingdmx = insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, shul,yaohl,mos,user,state,
								dingdjssj,wullj,lingjGongys);
						listDingdmx.add(dingdmx);
					}
					/**
					 * 剩余资源,考虑盈余
					 */
					
					ziy = shul.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
					
					
				}else{//零件供应商数量大于1,多供应商供货
					/**
					 * 进行份额计算,生成用户中心,零件,供应商份额对象集合
					 */
					fenEJis(listLingjGys, baozrl, lingjSlMap, shul, anxjscszjb.getUsercenter(), anxjscszjb.getLingjbh(),gongysFeneMap);
					
					//供应商累积实际要货量
					BigDecimal shijyhl = BigDecimal.ZERO;
					BigDecimal gysYaohsl = BigDecimal.ZERO; //hzg 2015.8.28
					/**
					 * 判断是否维护了供 应商包装并返回包装个数 begin
					 * hzg 2015.8.27
					 */
					int bzgys = 0;
					int nullBaozgys = getBaozGsOfGys(anxjscszjb,user,listLingjGys,mos);
					/**
					 * 生成订单信息
					 */
					//遍历零件供应商集合
					for (int k = 0; k < listLingjGys.size(); k++) {
						LingjGongys lingjGongys = listLingjGys.get(k);
						/** mantis:11450 by hzg AB点要货令未考虑是否维护包装  begin **/
						if(StringUtils.isEmpty(lingjGongys.getUcbzlx()) || lingjGongys.getUcrl() == null 
								||lingjGongys.getUcrl()==BigDecimal.ZERO || StringUtils.isEmpty(lingjGongys.getUabzlx())
								||lingjGongys.getUaucgs() ==null ||lingjGongys.getUaucgs() == BigDecimal.ZERO  ){
							//如果存在未维护包装的供应商，要货量全部给其中一个供应商（不考虑份额）
							saveYicbj(anxjscszjb, user, " "+lingjGongys.getGongysbh()+"包装信息为空",mos);
							continue;
						}
						bzgys ++;
						/**  end  **/
						
						anxjscszjb.setGongyhth(lingjGongys.getGongyhth());//供应合同号
						GongysFeneJis gongysFene = gongysFeneMap.get(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+lingjGongys.getGongysbh());
						/**
						 * 供应商包装判断 begin
						 * hzg 2015.8.28 
						 */
						if(nullBaozgys==0){//没有空包装，按份额计算要货量 hzg
							//获取供应商份额对象 
							shijyhl = shijyhl.add(gongysFene.getYaohsl());
							gysYaohsl = gongysFene.getYaohsl();
						}else if(nullBaozgys==1){//存在一个没有维护包装的供应商，不按份额算，要货量全部给某个有包装的供应商
							if(listLingjGys.size()==2){ //AB点2个供应商情况
								shijyhl = shul;  //实际要货量等于要货数量
								gysYaohsl = shul; //供应商要货数量
							}else if(listLingjGys.size()>2){ //AB点为3个供应商情况
								if(bzgys>1){//bzgys=1的时候已经将要货量全部给了第一家，后面的不需要计算了。
									continue;
								}
								shijyhl = shul;  //实际要货量等于要货数量
								gysYaohsl = shul; //供应商要货数量
							}
						}else {  //空包装供应商数大于2,不是不要货吗？
							shijyhl = shul;  //实际要货量等于要货数量
							gysYaohsl = shul; //供应商要货数量
						}
						/**  end  **/
						//mantis：0012434 add by zbb
						if(gysYaohsl.signum() > 0){
							String key = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + lingjGongys.getGongysbh()
								+ anxjscszjb.getFenpbh();	
							Wullj wullj = wulljMap.get(key);
							if(wullj == null){
								saveYicbj(anxjscszjb, user, "物流路径信息未查询到"+anxjscszjb.getFenpbh(), mos);
								continue;
							}
							//保存订单
							dingd = insertDingd(anxjscszjb, lingjGongys.getGongysbh(), mos, user, dingdjssj, lingjGongys.getLeix(), dingdlx,
									state,dingdhMap,jiaorRq);
							//保存订单零件
							dingdlj = insertDingdlj(anxjscszjb, dingd, user, lingjGongys.getGongysbh(), dingdjssj, yaohRq, mos,jiaorRq,lingjGongys);
							if(dingdlj != null){
								listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
							}
							//如果要货数量不为0,保存订单明细
							if(yaohl.signum() != 0){
							//保存订单明细  modify：gysYaohsl = gongysFene.getYaohsl()  by hzg
								dingdmx = insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, gysYaohsl,yaohl,mos,user,state,
										dingdjssj,wullj,lingjGongys);
								listDingdmx.add(dingdmx);
							}
						}
					}
					/**
					 * 剩余资源,考虑盈余
					 */
					ziy = shijyhl.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
				}
			}else{//指定供应商
				//查询零件供应商
				param.put("gongysbh", anxjscszjb.getZhidgys());
				//获取供应商份额对象
				GongysFeneJis gongysFene = gongysFeneMap.get(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+anxjscszjb.getZhidgys());
				//如果对象为空,新建,保存
				if(gongysFene == null){
					gongysFene = new GongysFeneJis();
					gongysFene.setGongysbh(anxjscszjb.getZhidgys());//供应商编号
					gongysFene.setYaohsl(shul);//当前要货数量
					gongysFene.setJihfene(BigDecimal.ONE);//计划份额,指定供应商份额100%
					gongysFene.setGongyslj(BigDecimal.ONE);//供应商累积 
					gongysFene.setGongyssjghfecy(BigDecimal.ZERO);//供应商份额差异
				}
				//供应商计划份额 = 计划份额,指定供应商份额100%
				gongysFene.setJihfene(BigDecimal.ONE);
				//供应商累积= 供应商累积 + 供应商要货量
				gongysFene.setGongyslj(gongysFene.getGongyslj().add(shul));
				gongysFeneMap.put(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+anxjscszjb.getZhidgys(),gongysFene);
				//取指定供应商的物流路径0011221 gswang 2015-03-06
				String key = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + anxjscszjb.getZhidgys() + anxjscszjb.getFenpbh();	
				Wullj wullj = wulljMap.get(key);
				if(wullj != null){
					anxjscszjb.setFahd(wullj.getFahd());
				}

				//生成订单
				//保存订单
				dingd = insertDingd(anxjscszjb, anxjscszjb.getZhidgys(), mos, user , dingdjssj, gongyslx, dingdlx,state
						,dingdhMap,jiaorRq);
				//保存订单零件
				dingdlj = insertDingdlj(anxjscszjb, dingd, user, anxjscszjb.getZhidgys(), dingdjssj, yaohRq, mos,jiaorRq);
				if(dingdlj != null){
					listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
				}
				//如果要货数量不为0,保存订单明细
				if(yaohl.signum() != 0){
				//保存订单明细
					dingdmx = insertDingdmxZdgys(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, gongysFene.getYaohsl(),yaohl,mos,user,state,
								anxjscszjb.getZhidgys(),gongyslx,dingdjssj,wullj);
					listDingdmx.add(dingdmx);
				}
				
				/**
				 * 剩余资源,考虑盈余
				 */
				ziy = shul.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
			}
		}else{
			BigDecimal baozrl = BigDecimal.ZERO;
			//MD模式按仓库UC容量取整
			if(Const.ANX_MS_MD.equals(mos) ){
				baozrl = anxjscszjb.getCkucrl();
			}else{
				//M1模式按仓库US容量取整
				baozrl = anxjscszjb.getCkusbzrl();
			}
			shul = this.roundingByPackAx(yaohl, baozrl);//按包装取整
			//M1MD模式生成订单
			//保存订单
			dingd = insertDingd(anxjscszjb, gongysdm, mos, user, dingdjssj, gongyslx, dingdlx,state,
					dingdhMap,jiaorRq);
			//保存订单零件
			dingdlj = insertDingdlj(anxjscszjb, dingd, user, gongysdm, dingdjssj, yaohRq, mos,jiaorRq);
			if(dingdlj != null){
				listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
			}
			//如果要货数量不为0,保存订单明细
			if(yaohl.signum() != 0){
				//保存订单明细  
				dingdmx = insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, shul,yaohl,mos,user,state,
						gongysdm,gongyslx,dingdjssj);
				listDingdmx.add(dingdmx);
			}
			/**
			 * 剩余资源,考虑盈余
			 */
			ziy = shul.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
		}
		
		/**
		 *返回计算结果 
		 */
		
		result.put("dingd", dingd);
		result.put("dingdmx", listDingdmx);
		result.put("dingdlj", listDingdlj);
		result.put("ziy", ziy);
		return result;
	}
	
	/**
	 * 返回零件供应商中无包装的供应商个数 
	 * @author 贺志国
	 * @date 2015-8-27
	 * @param bean 按需参数中间表
	 * @param user 用户信息
	 * @param listLingjGys 零件供应商集合
	 * @param mos 模式
	 * @return 零件供应商包装个数 
	 */
	protected int getBaozGsOfGys(Anxjscszjb bean,String user,List<LingjGongys> listLingjGys,String mos) {
		int nullBaozgys = 0;
		for (int k = 0; k < listLingjGys.size(); k++) {
			LingjGongys lingjGongys = listLingjGys.get(k);
			/** mantis:11450 by hzg AB点要货令未考虑是否维护包装  **/
			if(StringUtils.isEmpty(lingjGongys.getUcbzlx()) || lingjGongys.getUcrl() == null 
					||lingjGongys.getUcrl()==BigDecimal.ZERO || StringUtils.isEmpty(lingjGongys.getUabzlx())
					||lingjGongys.getUaucgs() ==null ||lingjGongys.getUaucgs() == BigDecimal.ZERO  ){
				//如果存在未维护包装的供应商，要货量全部给其中一个供应商
				nullBaozgys++;
				saveYicbj(bean, user, " "+lingjGongys.getGongysbh()+"包装信息为空",mos);
				continue;
			}
		}
		return nullBaozgys;
	}
	
	/**
	 * 订单计算
	 * @param mos 模式
	 * @param anxjscszjb 按需计算参数数据
	 * @param yaohl 要货量
	 * @param param 查询参数
	 * @param user 操作人
	 * @param dingdjssj 订单计算时间
	 * @param gongyslx 供应商类型
	 * @param dingdlx 订单类型
	 * @param anxMaoxqzjb 按需毛需求数据
	 * @param xiaohSj 消耗时间
	 * @param gongysdm 供应商代码
	 * @param state 状态
	 * @param listDingdmx 订单明细集合
	 * @param listDingdlj 订单零件集合
	 * @param lingjSlMap 零件数量map
	 * @param lingjgysMap 零件供应商map
	 * @param gongysFeneMap 份额计算map
	 * @param wulljMap 物流路径参数map
	 * @param dingdhMap 订单号map
	 * @param xiehztMap 卸货站台map
	 * @param kaibsk 开班时间
	 * @param xiabsj 下班时间
	 * @param shengxrq 将来模式生效日期
	 * @return 订单生成结果
	 * @throws ParseException
	 */
	public Map calculateDingd(String mos,Anxjscszjb anxjscszjb,BigDecimal yaohl,Map<String,String> param,String user,
			String dingdjssj,String gongyslx,String dingdlx,AnxMaoxqzjb anxMaoxqzjb,String xiaohSj,String gongysdm,String state,
			List<Dingdmx> listDingdmx,List<Dingdlj> listDingdlj,Map<String,BigDecimal> lingjSlMap,Map<String,List<LingjGongys>> lingjgysMap,
			Map<String,GongysFeneJis> gongysFeneMap,Map<String,Wullj> wulljMap,HashMap<String, String> dingdhMap,Map<String,Xiehzt> xiehztMap,
			String kaibsk,String xiabsj,Date shengxrq) throws ParseException{
		BigDecimal ziy = BigDecimal.ZERO;//资源
		BigDecimal shul = BigDecimal.ZERO;//数量
		String yaohRq = xiaohSj.substring(0,10);//要货日期
		Map<String,String> timeMap = new HashMap<String, String>();
		/**
		 * 生成订单,订单零件,订单明细信息
		 */
		Dingd dingd = new Dingd();//订单
		Dingdlj dingdlj = new Dingdlj();//订单零件
		Dingdmx dingdmx = new Dingdmx();//订单明细
		Map result = new HashMap();
		//如果是C1CD模式需进行份额计算
		if(Const.ANX_MS_C1.equals(mos) || Const.ANX_MS_CD.equals(mos)){

			/**
			 * 份额计算
			 */
			//包装容量 = uc容量*uauc个数
			BigDecimal baozrl = anxjscszjb.getGysucrl().multiply(anxjscszjb.getGysuaucgs());
			//按包装取整
			shul = this.roundingByPackAx(yaohl, baozrl);
			
			//指定供应商为空,不指定供应商,需进行份额计算,根据不同供应商生成订单
			if(StringUtils.isEmpty(anxjscszjb.getZhidgys())){
				param.put("gonghlx", Const.ZHIZAOLUXIAN_IL);
				//获取零件供应商集合
				List<LingjGongys> listLingjGys = lingjgysMap.get(param.get("usercenter")+param.get("lingjbh"));
				if(listLingjGys==null){
					result.put("biaos", "break");
					logger.info("零件无供应商");
					return result;
				}
				//如果零件供应商为数量为1,单独供应商供货
				if(listLingjGys.size() == 1){
					String gongysbh = listLingjGys.get(0).getGongysbh();
					//查询物流路径取承运商编号
					String key = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + gongysbh + anxjscszjb.getFenpbh();	
					Wullj wullj = wulljMap.get(key);
					/**
					 * 计算备货,到货等时间
					 */
					timeMap = timeCalculate(anxjscszjb, mos, user,anxMaoxqzjb.getXhsj(),
							anxjscszjb.getBeihsjc(),anxjscszjb.getCangkshsj2(),xiehztMap,wullj.getGcbh(),kaibsk,xiabsj,shengxrq);
					String biaos = this.strNullAx(timeMap.get("biaos"));//标识符
					//如果标识符为continue,则代表有异常报警,返回标识符,跳过当前数据计算下一条
					if("continue".equals(biaos)){
						result.put("biaos", biaos);
						logger.info("按需时间log91:result"+result);
						return result;
					//如果标识符为break,则代表计算结束,该品种不再计算
					}else if("break".equals(biaos)){
						result.put("biaos", biaos);
						logger.info("按需时间log92:result"+result);
						return result;
					}
					String jiaorRq = timeMap.get("zuiwdhsj").substring(0,10);//交付日期
					//生成订单
					//保存订单
					dingd = insertDingd(anxjscszjb, gongysbh, mos, user , dingdjssj, gongyslx, dingdlx,state
							,dingdhMap,jiaorRq);
					//保存订单零件
					dingdlj = insertDingdlj(anxjscszjb, dingd, user, gongysbh, dingdjssj, yaohRq, mos,jiaorRq);
					if(dingdlj != null){
						listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
					}
					//如果要货数量不为0,保存订单明细
					if(yaohl.signum() != 0){
					//保存订单明细
						dingdmx = insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, shul,yaohl,mos,user,state,
								gongysbh,gongyslx,dingdjssj);
						listDingdmx.add(dingdmx);
					}
					/**
					 * 剩余资源,考虑盈余
					 */
					ziy = shul.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
				}else{//零件供应商数量大于1,多供应商供货
					/**
					 * 进行份额计算,生成用户中心,零件,供应商份额对象集合
					 */
					fenEJis(listLingjGys, baozrl, lingjSlMap, shul, anxjscszjb.getUsercenter(), anxjscszjb.getLingjbh(),gongysFeneMap);
					
					//供应商累积实际要货量
					BigDecimal shijyhl = BigDecimal.ZERO;
					BigDecimal gysYaohsl = BigDecimal.ZERO; //hzg 2015.8.28
					/**
					 * 判断是否维护了供 应商包装并返回包装个数 begin
					 * hzg 2015.8.27
					 */
					int bzgys = 0;
					int nullBaozgys = getBaozGsOfGys(anxjscszjb,user,listLingjGys,mos);
					/**
					 * 生成订单信息
					 */
					//遍历零件供应商集合
					for (int k = 0; k < listLingjGys.size(); k++) {
						LingjGongys lingjGongys = listLingjGys.get(k);
						/** mantis:11450 by hzg AB点要货令未考虑是否维护包装  begin **/
						if(StringUtils.isEmpty(lingjGongys.getUcbzlx()) || lingjGongys.getUcrl() == null 
								||lingjGongys.getUcrl()==BigDecimal.ZERO || StringUtils.isEmpty(lingjGongys.getUabzlx())
								||lingjGongys.getUaucgs() ==null ||lingjGongys.getUaucgs() == BigDecimal.ZERO  ){
							//如果存在未维护包装的供应商，要货量全部给其中一个供应商（不考虑份额）
							saveYicbj(anxjscszjb, user, lingjGongys.getGongysbh()+"包装信息为空",mos);
							continue;
						}
						bzgys ++;
						/**  end  **/
						
						anxjscszjb.setGongyhth(lingjGongys.getGongyhth());//供应合同号
						//获取供应商份额对象
						GongysFeneJis gongysFene = gongysFeneMap.get(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+lingjGongys.getGongysbh());
						
						/**
						 * 供应商包装判断 begin
						 * hzg 2015.8.28 
						 */
						if(nullBaozgys==0){//没有空包装，按份额计算要货量 hzg
							//获取供应商份额对象 
							shijyhl = shijyhl.add(gongysFene.getYaohsl());
							gysYaohsl = gongysFene.getYaohsl();
						}else if(nullBaozgys==1){//存在一个没有维护包装的供应商，不按份额算，要货量全部给某个有包装的供应商
							if(listLingjGys.size()==2){ //AB点2个供应商情况
								shijyhl = shul;  //实际要货量等于要货数量
								gysYaohsl = shul; //供应商要货数量
							}else if(listLingjGys.size()>2){ //AB点为3个供应商情况
								if(bzgys>1){
									continue;
								}
								shijyhl = shul;  //实际要货量等于要货数量
								gysYaohsl = shul; //供应商要货数量
							}
						}else {  //空包装供应商数大于2
							shijyhl = shul;  //实际要货量等于要货数量
							gysYaohsl = shul; //供应商要货数量
						}
						/**  end  **/
						//mantis:0012434 add by zbb
						if(gysYaohsl.signum() > 0){
							String key = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + lingjGongys.getGongysbh()
								+ anxjscszjb.getFenpbh();	
							Wullj wullj = wulljMap.get(key);
							if(wullj == null){
								fenEHuit(gongysFene.getYaohsl(), gongysFeneMap, lingjSlMap, lingjGongys);
								saveYicbj(anxjscszjb, user, "物流路径信息未查询到"+anxjscszjb.getFenpbh(), mos);
								continue;
							}
							/**
							 * 计算备货,到货等时间
							 */
							timeMap = timeCalculate(anxjscszjb, mos, user,anxMaoxqzjb.getXhsj(),
									anxjscszjb.getBeihsjc(),anxjscszjb.getCangkshsj2(),xiehztMap,wullj.getGcbh(),kaibsk,xiabsj,shengxrq);
							String biaos = this.strNullAx(timeMap.get("biaos"));//标识符
							//如果标识符为continue,则代表有异常报警,返回标识符,跳过当前数据计算下一条
							if("continue".equals(biaos)){
								result.put("biaos", biaos);
								fenEHuit(gongysFene.getYaohsl(), gongysFeneMap, lingjSlMap, lingjGongys);
								continue;
							//如果标识符为break,则代表计算结束,该品种不再计算
							}else if("break".equals(biaos)){
								result.put("biaos", "continue");
								fenEHuit(gongysFene.getYaohsl(), gongysFeneMap, lingjSlMap, lingjGongys);
								continue;
							}
							String jiaorRq = timeMap.get("zuiwdhsj").substring(0,10);//交付日期
							//保存订单
							dingd = insertDingd(anxjscszjb, lingjGongys.getGongysbh(), mos, user, dingdjssj, lingjGongys.getLeix(), dingdlx,
									state,dingdhMap,jiaorRq);
							//保存订单零件
							dingdlj = insertDingdlj(anxjscszjb, dingd, user, lingjGongys.getGongysbh(), dingdjssj, yaohRq, mos,jiaorRq,lingjGongys);
							if(dingdlj != null){
								listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
							}
							//如果要货数量不为0,保存订单明细
							if(yaohl.signum() != 0){
							//保存订单明细    modify：gysYaohsl = gongysFene.getYaohsl()  by hzg
								//shijyhl = shijyhl.add(gongysFene.getYaohsl());
								dingdmx = insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap,gysYaohsl,yaohl,mos,user,state,
										dingdjssj,wullj,lingjGongys);
								listDingdmx.add(dingdmx);
							}
						}
					}
					/**
					 * 剩余资源,考虑盈余
					 */
					ziy = shijyhl.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
				}
			}else{
				//查询零件供应商
				param.put("gongysbh", anxjscszjb.getZhidgys());
				/*
				//获取供应商份额对象
				GongysFeneJis gongysFene = gongysFeneMap.get(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+anxjscszjb.getZhidgys());
				//如果对象为空,新建,保存
				if(gongysFene == null){
					gongysFene = new GongysFeneJis();
					gongysFene.setGongysbh(anxjscszjb.getZhidgys());//供应商编号
					gongysFene.setYaohsl(shul);//当前要货数量
					gongysFene.setJihfene(BigDecimal.ONE);//计划份额,指定供应商份额100%
					gongysFene.setGongyslj(BigDecimal.ZERO);//供应商累积
					gongysFene.setGongyssjghfecy(BigDecimal.ZERO);//供应商份额差异
				}
				//供应商计划份额 = 计划份额,指定供应商份额100%
				gongysFene.setJihfene(BigDecimal.ONE);
				//供应商累积= 供应商累积 + 供应商要货量
				gongysFene.setGongyslj(gongysFene.getGongyslj().add(shul));
				gongysFeneMap.put(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+anxjscszjb.getZhidgys(),gongysFene);
				*/
				//取指定供应商的物流路径0011221 gswang 2015-03-06
				String key = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + anxjscszjb.getZhidgys() + anxjscszjb.getFenpbh();	
				Wullj wullj = wulljMap.get(key);
				if(wullj !=null ){
					anxjscszjb.setFahd(wullj.getFahd());
				}

				/**
				 * 计算备货,到货等时间
				 */
				timeMap = timeCalculate(anxjscszjb, mos, user,anxMaoxqzjb.getXhsj(),
						anxjscszjb.getBeihsjc(),anxjscszjb.getCangkshsj2(),xiehztMap,wullj.getGcbh(),kaibsk,xiabsj,shengxrq);
				String biaos = this.strNullAx(timeMap.get("biaos"));//标识符
				//如果标识符为continue,则代表有异常报警,返回标识符,跳过当前数据计算下一条
				if("continue".equals(biaos)){
					result.put("biaos", biaos);
					return result;
				//如果标识符为break,则代表计算结束,该品种不再计算
				}else if("break".equals(biaos)){
					result.put("biaos", biaos);
					return result;
				}
				String jiaorRq = timeMap.get("zuiwdhsj").substring(0,10);//交付日期
				//生成订单
				//保存订单
				dingd = insertDingd(anxjscszjb, anxjscszjb.getZhidgys(), mos, user , dingdjssj, gongyslx, dingdlx,state
						,dingdhMap,jiaorRq);
				//保存订单零件
				dingdlj = insertDingdlj(anxjscszjb, dingd, user, anxjscszjb.getZhidgys(), dingdjssj, yaohRq, mos,jiaorRq);
				if(dingdlj != null){
					listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
				}
				//如果要货数量不为0,保存订单明细
				if(yaohl.signum() != 0){
				//保存订单明细
					dingdmx = insertDingdmxZdgys(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, shul,yaohl,mos,user,state,
								anxjscszjb.getZhidgys(),gongyslx,dingdjssj,wullj);
					listDingdmx.add(dingdmx);
				}
				
				/**
				 * 剩余资源,考虑盈余
				 */
				ziy = shul.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
			}
		}else{
			BigDecimal baozrl = BigDecimal.ZERO;
			//MD模式按仓库UC容量取整
			if(Const.ANX_MS_MD.equals(mos) ){
				baozrl = anxjscszjb.getCkucrl();
			}else{
				//M1模式按仓库US容量取整
				baozrl = anxjscszjb.getCkusbzrl();
			}
			shul = this.roundingByPackAx(yaohl, baozrl);//按包装取整
			//M1MD模式生成订单
			/**
			 * 计算备货,到货等时间
			 */
			timeMap = timeCalculate(anxjscszjb, mos, user,anxMaoxqzjb.getXhsj(),
					anxjscszjb.getBeihsjc(),anxjscszjb.getCangkshsj2(),xiehztMap,anxjscszjb.getDinghck(),kaibsk,xiabsj,shengxrq);
			String biaos = this.strNullAx(timeMap.get("biaos"));//标识符
			//如果标识符为continue,则代表有异常报警,返回标识符,跳过当前数据计算下一条
			if("continue".equals(biaos)){
				result.put("biaos", biaos);
				return result;
			//如果标识符为break,则代表计算结束,该品种不再计算
			}else if("break".equals(biaos)){
				result.put("biaos", biaos);
				return result;
			}
			String jiaorRq = timeMap.get("zuiwdhsj").substring(0,10);//交付日期
			//保存订单
			dingd = insertDingd(anxjscszjb, gongysdm, mos, user, dingdjssj, gongyslx, dingdlx,state,
					dingdhMap,jiaorRq);
			//保存订单零件
			dingdlj = insertDingdlj(anxjscszjb, dingd, user, gongysdm, dingdjssj, yaohRq, mos,jiaorRq);
			if(dingdlj != null){
				listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
			}
			//如果要货数量不为0,保存订单明细
			if(yaohl.signum() != 0){
				//保存订单明细
				dingdmx = insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, shul,yaohl,mos,user,state,
						gongysdm,gongyslx,dingdjssj);
				listDingdmx.add(dingdmx);
			}
			/**
			 * 剩余资源,考虑盈余
			 */
			ziy = shul.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
		}
		
		/**
		 *返回计算结果 
		 */
		result.put("dingd", dingd);
		result.put("timeMap", timeMap);
		result.put("dingdmx", listDingdmx);
		result.put("dingdlj", listDingdlj);
		result.put("ziy", ziy);
		return result;
	}
	
	/**
	 * 清除数据
	 */
	public void clearData(String user,String dingdlx){
		//清除按需毛需求中间表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteAnxmaoxqzjb");
		//清除按需计算中间表数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteAnxjscszjb");
		deleteDingd(user,dingdlx,Const.ACTIVE_0);		
	}
	
	/**
	 * 清除数据
	 */
	public void clearData(String user,String dingdlx,Map<String,String> map){
		//清除按需毛需求中间表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteAnxmaoxqzjb",map);
		//清除按需计算中间表数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteAnxjscszjb",map);
		deleteDingd(user,dingdlx,Const.ACTIVE_0,map);		
	}
	
	/**
	 * 删除订单信息
	 * @param user 操作人
	 * @param dingdlx 订单类型
	 */
	public void deleteDingd(String user,String dingdlx,String active){
		Map<String,String> param = new HashMap<String, String>();
		param.put("creator", user);
		param.put("dingdlx", dingdlx);
		if(!StringUtils.isEmpty(active)){
			param.put("dingdlx", dingdlx);
		}
		//清除订单内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteDingdLjJsz",param);
		//清除订单明细内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteDingdMxJsz",param);
		//清除订单内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteDingdJsz",param);
	}
	
	/**
	 * 删除订单信息
	 * @param user 操作人
	 * @param dingdlx 订单类型
	 */
	public void deleteDingd(String user,String dingdlx,String active,Map<String,String> map){
		map.put("creator", user);
		map.put("dingdlx", dingdlx);
		if(!StringUtils.isEmpty(active)){
			map.put("dingdlx", dingdlx);
		}
		//清除订单内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteDingdLjJsz",map);
		//清除订单明细内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteDingdMxJsz",map);
		//清除订单内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.deleteDingdJsz",map);
	}
	
	/**
	 * 更新订单状态
	 * @param dingdjssj 订单计算时间
	 * @param state 订单状态
	 * @param user 操作人
	 */
	public void updateDingd(String dingdjssj,String state,String user,String dingdlx){
		Map<String,String> param = new HashMap<String, String>();
		param.put("state", state);
		param.put("dingdjssj", dingdjssj);
		param.put("creator", user);
		param.put("dingdlx", dingdlx);
		//更新订单状态
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.updateDingdJszCsh",param);
	}
	
	/**
	 * 更新订单状态
	 * @param dingdjssj 订单计算时间
	 * @param state 订单状态
	 * @param user 操作人
	 * @param dingdsxsj 资源文件时间
	 */
	public void updateDingd(String dingdjssj,String state,String user,String dingdlx,Map<String,String> map){
		map.put("state", state);
		map.put("dingdjssj", dingdjssj);
		map.put("creator", user);
		map.put("dingdlx", dingdlx);
		//更新订单状态
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.updateDingdJsz",map);
		
	}
	
	/**
	 * 修改生效时间
	 * @param user 操作人
	 * @param map 数据集合
	 * @param dingdsxsj 订单生效时间
	 */
	public void updateShengxsj(String user,Map<String,String> map,String dingdsxsj){
		map.put("creator", user);
		if(Const.WTC_CENTER_UW.equals(map.get("usercenter"))){
			map.put("dingdh", Const.ANX_UW_DINGDH);
		}else{
			map.put("dingdh", Const.ANX_UL_DINGDH);
		}
		map.put("dingdjssj", dingdsxsj);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.updateDingdjssj",map);
	}

	public BigDecimal getBigDecimalAx(Object obj) {
		if (obj != null) {
			return new BigDecimal(this.strNullAx(obj));
		} else {
			return BigDecimal.ZERO;
		}
	}

	public String strNullAx(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	/**
	 * 按包装向下取整
	 * @param shul 数量
	 * @param pack 包装容量
	 * @return 取整后数量
	 */
	public BigDecimal roundingByPackDownAx(BigDecimal shul,BigDecimal pack){
		//比较数量和包装容量
		if(shul.compareTo(pack) > 0){
			//数量大于包装容量,需要多个包装
			//计算数量除包装容量,需要包装个数和余数
			BigDecimal[] shuls = shul.divideAndRemainder(pack); 
			//如果余数不为0,则表示数量无法整除,需要按包装取整
			if(shuls[1].compareTo(BigDecimal.ZERO) != 0){
				//计算取整后数量,向下取整(包装个数)
				shul = shuls[0].multiply(pack);
			}
		}else{
			//数量小于包装容量,则不要货
			shul = BigDecimal.ZERO;
		}
		return shul;
	}

	/**
	 * 按包装向上取整
	 * @param shul 数量
	 * @param pack 包装容量
	 * @return 取整后数量
	 */
	public BigDecimal roundingByPackAx(BigDecimal shul,BigDecimal pack){
		//比较数量和包装容量
		if(shul.compareTo(pack) > 0){
			//数量大于包装容量,需要多个包装
			//计算数量除包装容量,需要包装个数和余数
			BigDecimal[] shuls = shul.divideAndRemainder(pack); 
			//如果余数不为0,则表示数量无法整除,需要按包装取整
			if(shuls[1].compareTo(BigDecimal.ZERO) != 0){
				//计算取整后数量(包装个数+1)
				shul = (shuls[0].add(BigDecimal.ONE)).multiply(pack);
			}
		}else{
			//数量小于包装容量,则取整为包装容易,即一个包装
			shul = pack;
		}
		return shul;
	}
	
	public final SimpleDateFormat sdfAx = new SimpleDateFormat("yyyy-MM-dd");
	public final SimpleDateFormat yyyyMMddHHmmssAx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final SimpleDateFormat yyyyMMddHHmmAx = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	public void anxOrderMethodUW() throws ServiceException { 
		anxOrderMethod("4101", "UW");
	}

	public void anxOrderMethodUL() throws ServiceException {		
		anxOrderMethod("4100", "UL"); 
	}
	
	/**
	 * 计算发运时间
	 * 0013016: 按需紧急订单的发运时间 by CSY 20170112
	 * @return
	 */
	public String getFaysj(String mos, String kaibsj, String zuiwdhsj, Anxjscszjb bean) throws ParseException {
		String faysj = kaibsj.substring(0,16);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usercenter", bean.getUsercenter());//用户中心
		map.put("shengcxbh", bean.getShengcxbh());//生产线编号
		if (mos.equalsIgnoreCase(Const.ANX_MS_M1) || mos.equalsIgnoreCase(Const.ANX_MS_MD)) {
			//发运时间 = 最晚到货时间 - 仓库送货时间2
			map.put("juedsk", zuiwdhsj);
			map.put("shijNum", this.strNullAx(this.getBigDecimalAx(bean.getCangkshsj2()).multiply(new BigDecimal(24)).multiply(new BigDecimal(60)).intValue()));
			faysj = this.strNullAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryGongzsjmbQ",map));
		}else{//C1CD模式发运时间= 最晚到货时间 - 运输周期
			Date date = new Date();
			date = CommonFun.yyyyMMddHHmmAxparse(zuiwdhsj);//设置为最晚到货时间
			logger.info("按需时间log4:kaibsj="+kaibsj+":zuiwdhsj="+zuiwdhsj+":Yunszq="+bean.getYunszq());
			//发运时间 = 最晚到货时间 - 运输周期
			date.setTime(date.getTime() - this.getBigDecimalAx(bean.getYunszq()).multiply(new BigDecimal(24))
					.multiply(new BigDecimal(60)).multiply(new BigDecimal(60)).multiply(new BigDecimal(1000)).intValue());
			faysj = CommonFun.yyyyMMddHHmmAxformat(date);
		}
		
		return faysj;
	}
	
	/**
	 * 消耗点不存在时存储异常报警
	 * by CSY
	 * 20170309
	 * @param lingjbh
	 * @param lingjxhd
	 * @param usercenter
	 * @param jihy
	 * @param user
	 * @param mos
	 * @param str
	 * @param uset
	 */
	private void saveYcbjXhd(String lingjbh, String usercenter, String jihy, String user, String str){
		//拼接错误信息
		StringBuilder cuowxx = new StringBuilder("");
		cuowxx.append("零件:");
		cuowxx.append(lingjbh);
		cuowxx.append("在用户中心:");
		cuowxx.append(usercenter);
		cuowxx.append(str);
		yicbjService.saveYicbj(lingjbh, usercenter, cuowxx.toString(), user, Const.JISMK_ANX_CD, Const.YICHANG_LX2,jihy);
	}
	
}
