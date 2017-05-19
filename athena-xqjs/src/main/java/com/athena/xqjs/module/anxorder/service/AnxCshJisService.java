package com.athena.xqjs.module.anxorder.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.anxorder.AnxMaoxqzjb;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.entity.anxorder.Chushzyb;
import com.athena.xqjs.entity.anxorder.GongysFeneJis;
import com.athena.xqjs.entity.anxorder.Kucjscsb;
import com.athena.xqjs.entity.anxorder.Ticxxsj;
import com.athena.xqjs.entity.common.Cangkxhsj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiehztxhsj;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 按需初始化计算Service类
 */
@Component
public class AnxCshJisService extends BaseService {
	
	/**
	 * 按需参数类
	 */
	@Inject
	private AnxParam anxParam;
	
	/**
	 * 按需计算类
	 */
	@Inject
	private AnxJisService anxjis;
	
	/**
	 * 异常报警Service
	 */
	@Inject
	private YicbjService yicbjService;
	
	/**
	 * 订单号MAP
	 */
	private HashMap<String, String> dingdhMap = null;
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(AnxJisService.class);
	
	public void cshAnxOrderMethod(String jisLx,String user) throws ParseException{
		
		/**
		 * 开始计算,计算时间
		 */
 		String dingdjssj = CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS);//订单计算时间
		logger.info("按需初始化计算开始------操作人:"+user+"------订单计算时间:"+dingdjssj);
		
		/**
		 * 计算模式判断
		 */
		Map<String,String> mosParam = new HashMap<String, String>();
		//计算类型为1,即CDMD模式计算
		if("1".equals(jisLx)){
			mosParam.put("mos", "CD");
			mosParam.put("mos2", "MD");
		}else{
			mosParam.put("mos", "C1");
			mosParam.put("mos2", "M1");
		}
		
		/**
		 * 初始化参考系参数,加载到缓存MAP
		 */
		anxParam.initParam(mosParam);
		dingdhMap = new HashMap<String, String>();
		/**
		 * 数据准备,生成中间表
		 */
		chushDataPreparation(user, dingdjssj, mosParam, jisLx);
		
		/**
		 * 根据计算类型进行遍历计算
		 */
		List<Dingdlj> listDingdlj = new ArrayList<Dingdlj>();//订单零件集合
		List<Dingdmx> listDingdmx = new ArrayList<Dingdmx>();//订单明细集合
		Map<String,String> param = new HashMap<String,String>();
		List<Anxjscszjb> listAnxjscszjb = new ArrayList<Anxjscszjb>();
		//如果计算类型为1,即CDMD模式计算,更新线边理论库存
		if("1".equals(jisLx)){
			Map<String,String> map = new HashMap<String, String>();
			map.put("usercenter",Const.WTC_CENTER_UW);
			map.put("dingdh", Const.ANX_UW_DINGDH);
			//上次计算时间
			Dingd dingd = (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryAxScjssj",map);//上次计算时间
			updateXbkc(map, dingd, user);
			//计算类型1,CDMD模式计算,按用户中心,零件编号,消耗点进行遍历
			listAnxjscszjb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxChushJis.queryAnxjscszjbCDMD");
		}else{
			//计算类型2,C1M1模式计算,按用户中心,零件编号,线边仓库进行遍历
			listAnxjscszjb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxChushJis.queryAnxjscszjbC1M1");
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
			if (Const.ANX_MS_CD.equalsIgnoreCase(anxjscszjb.getMos()) || Const.ANX_MS_C1.equalsIgnoreCase(anxjscszjb.getMos())) {
				mos = anxjscszjb.getMos();
				gongysdm = anxjscszjb.getGongysbh();//C1CD模式供应商为供应商
				gongyslx = anxjscszjb.getGongyslx();//供应商类型为供应商类型
				dingdlx = Const.DINGD_LX_ANX_CSH_C;//订单类型为 按需初始化C
			} else {
				mos = anxjscszjb.getMos2();
				gongysdm = anxjscszjb.getDinghck();//M1MD模式供应商为订货库
				gongyslx = anxjscszjb.getDinghcklx();//供应商类型为订货库类型
				dingdlx = Const.DINGD_LX_ANX_CSH_M;//订单类型为 按需正常M
			}
			
			/**
			 * 计算封闭期
			 */
			Map JisDateCsh = getJisDateCsh(anxjscszjb,user,mos);//封闭期天数
			if(JisDateCsh == null){
				continue;
			}
			BigDecimal cangkshsj = CommonFun.getBigDecimal(JisDateCsh.get("cangkshsj"));
			//2014-11-11 仓库送货时间取ckx_cangkxhsj 表中的仓库送货时间，不取物流路径中的Cangkshsj2
			param.put("cangkshsj", cangkshsj.toString());
			int day = (Integer) JisDateCsh.get("day");
			if(day < 0){
				continue;
			}
			int num = 0;
			/**
			 * 校验计算日期是否为工作日
			 */
			if (Const.ANX_MS_C1.equalsIgnoreCase(mos) || Const.ANX_MS_M1.equalsIgnoreCase(mos)) {
				param.put("shengcxbh", anxjscszjb.getXianbck());//线边仓库
			}else{
				param.put("shengcxbh", anxjscszjb.getShengcxbh());//生产线
			}	
			List<Ticxxsj> checkGzr = anxParam.ticxxsjMap.get(anxjscszjb.getUsercenter()+param.get("shengcxbh")+dingdjssj.substring(0,10));
			//如果计算日期是工作日
			//封闭期1天，算下一工作日到货；
			//封闭期2天，算下一工作日起2个工作日到货 
			if(checkGzr != null && !checkGzr.isEmpty()){
				num = day;
			}else{
			//如果计算日不为工作日
			//封闭期1天，算下一工作日起2个工作日到货；
			//封闭期2天，算下一工作日起3个工作日到货 
				num = day + 1;
			}
			param.put("usercenter", anxjscszjb.getUsercenter());//用户中心
			param.put("num", String.valueOf(num));//当前日期后最近一个工作日后的封闭期日期
			param.put("gongzr", dingdjssj.substring(0,10));//根据计算时间推封闭期日期
			List<Ticxxsj> listJsrq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxChushJis.queryTicxxsjGzrNumCsh",param);//查询封闭期日期
			//工作日历中没有相关的维护,系统给出报警,不进行计算
			if(listJsrq == null || listJsrq.isEmpty() || day > listJsrq.size()+1){
				anxjis.saveYicbj(anxjscszjb, user, dingdjssj+"工作日历为空",mos);
				continue;
			}
			String kaibsj = "";//上班时间
			String xiabsj = "";//下班时间
			
			/**
			 * 取计算日期剔除休息时间
			 */
			String jisrq = listJsrq.get(0).getGongzr();//计算日期取第一天
			List<Ticxxsj> listTicxxsj = anxParam.ticxxsjMap.get(anxjscszjb.getUsercenter()+param.get("shengcxbh")+jisrq);
			if(listTicxxsj == null){
				anxjis.saveYicbj(anxjscszjb, user, jisrq+"封闭期工作日历为空",mos);
				continue;
			}
			kaibsj = listTicxxsj.get(0).getRiq() + " " +listTicxxsj.get(0).getShijdkssj();//计算日期开班时间
			jisrq = listJsrq.get(num - 1).getGongzr();//计算日期取到最后一天
			listTicxxsj = anxParam.ticxxsjMap.get(anxjscszjb.getUsercenter()+param.get("shengcxbh")+jisrq);
			if(listTicxxsj == null){
				anxjis.saveYicbj(anxjscszjb, user, jisrq+"封闭期工作日历为空",mos);
				continue;
			}
			xiabsj = listTicxxsj.get(listTicxxsj.size() - 1).getRiq() + " " + listTicxxsj.get(listTicxxsj.size() - 1).getShijdjssj();//计算日期下班时间
			
			param.put("lingjbh", anxjscszjb.getLingjbh()); 
			param.put("jisrq", jisrq);
			
			/**
			 * 计算初始资源
			 */
			BigDecimal ziy = resourceCalculationCsh(anxjscszjb, dingdjssj, mos, user);
			if(ziy == null){
				continue;
			}
			/**
			 * 初始资源为负数,需生成要货
			 */
			if(ziy.signum() < 0){
				BigDecimal yaohl = ziy.abs();//要货量为将资源补零
				String time = kaibsj.substring(0,16);
				Map<String,String> timeMap = new HashMap<String, String>();
				timeMap.put("zuizdhsj", time);//最早到货时间
				timeMap.put("zuiwdhsj", time);//最晚到货时间
				//向前推小火车备货时间,前一个工作日收班时间再往前推135分钟
				param.put("riqParam", kaibsj.substring(0,10));//下一个工作日
				List<Ticxxsj> lastGzr = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxChushJis.queryLastGzr",param);
				if(null != lastGzr && !lastGzr.isEmpty()){
					String shoubsj = lastGzr.get(0).getRiq() + " " + lastGzr.get(0).getShijdjssj();//收班时间
					Date date = new Date();
					//向前推135分钟
					date.setTime(CommonFun.yyyyMMddHHmmss.parse(shoubsj).getTime() - 135*60*1000);
					timeMap.put("beihsj", CommonFun.yyyyMMddHHmmss.format(date));//小火车备货时间
				}else{//如果查询不到前一个工作日,则备货时间也设为开班
					timeMap.put("beihsj", time);//小火车备货时间
				}
				timeMap.put("shangxsj", time);//小火车上线时间
				//0013016: 按需紧急订单的发运时间 by CSY 20170112
				timeMap.put("faysj", anxjis.getFaysj(mos, kaibsj, time, anxjscszjb));//发运日期
				AnxMaoxqzjb anxMaoxqzjb = new AnxMaoxqzjb();
				anxMaoxqzjb.setXhsj(time);
				Map mapDingdlj = calculateDingd(mos, anxjscszjb, yaohl, param, user, dingdjssj, gongyslx, dingdlx,listDingdmx, 
						anxMaoxqzjb, time, timeMap, gongysdm,Const.DINGD_STATUS_DSX,listDingdlj);
				ziy = CommonFun.getBigDecimal(mapDingdlj.get("ziy"));
			}
			/**
			 * 取用户中心,零件,消耗点,封闭期后日期毛需求,按需求时间排序,升序
			 */
			List<AnxMaoxqzjb> listAnxMaoxq = new ArrayList<AnxMaoxqzjb>();
			//计算类型1,CDMD模式计算,按用户中心,零件编号,消耗点取毛需求
			if("1".equals(jisLx)){
				param.put("xiaohd", anxjscszjb.getXiaohd());
				listAnxMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryAnxmaoxqzjbMx",param);
			}else{
				//计算类型2,C1M1模式计算,按用户中心,零件编号,线边仓库汇总毛需求
				param.put("xianbck", anxjscszjb.getXianbck());
				listAnxMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryAnxmaoxqzjbMxByXianbk",param);
			}
			/**
			 * 计算备货,到货等时间
			 */
			
			// 内部物流时间
			BigDecimal neibwlsj = queryNeibwlsj(anxjscszjb, user, mos);
			if(neibwlsj == null){
				anxjis.saveYicbj(anxjscszjb, user, "内部物流时间为空", mos);
				continue;
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
				
				Map mapDingdlj = calculateDingd(mos, anxjscszjb, yaohl, param, user, dingdjssj, gongyslx, dingdlx, listDingdmx, anxMaoxqzjb, xiaohSj, 
						gongysdm, Const.DINGD_STATUS_DSX, listDingdlj,kaibsj,xiabsj,neibwlsj);
				String biaos = CommonFun.strNull(mapDingdlj.get("biaos"));//标识符
				//如果标识符为continue,则代表有异常报警,返回标识符,跳过当前数据计算下一条
				if("continue".equals(biaos)){
					continue;
				//如果标识符为break,则代表计算结束,该品种不再计算
				}else if("break".equals(biaos)){
					break;
				}
				ziy = CommonFun.getBigDecimal(mapDingdlj.get("ziy"));
			}
		}
		
		//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ilorder.insertDingdlj", listDingdlj);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ilorder.insertDingdmx", listDingdmx);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("anxjis.updateDingdljP0", listDingdlj);
		
		/**
		 
		for (int i = 0; i < listDingdlj.size(); i++) {
			Dingdlj dingdlj = listDingdlj.get(i);
			//汇总P0数量
			dingdlj.setP0sl(CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryDingdmxSl", dingdlj)));
			dingdlj.setActive(Const.ACTIVE_1);
			//更新订单零件数量
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateDingdljPart", dingdlj);
		}* 汇总订单零件P0数量
		 */
		
		/**
		 * 更新订单状态
		 */
		logger.info("更新订单状态");
		anxjis.updateDingd(dingdjssj, Const.DINGD_STATUS_DSX, user,"'"+Const.DINGD_LX_ANX_CSH_C+"','"+Const.DINGD_LX_ANX_CSH_M+"'");
		
		/**
		 * 按需计算结束,销毁参考系参数
		 */
		anxParam.destory();
		dingdhMap = null;
		logger.info("按需计算结束------操作人:"+user+"------时间:"+CommonFun.getJavaTime());
	}
	
	/**
	 * 更新线边库存
	 * @param map 数据集合
	 * @param dingd 订单
	 * @param user 操作用户
	 */
	@Transactional
	public void updateXbkc(Map<String,String> map,Dingd dingd,String user){
		map.put("shangcjssj", dingd.getDingdjssj());
		/**
		 * 更新线边理论库存
		 */
		updateXianbllkc(user,map);
		map.put("usercenter",Const.WTC_CENTER_UL);
		map.put("dingdh", Const.ANX_UL_DINGDH);
		//上次计算时间
		dingd = (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryAxScjssj",map);//上次计算时间
		map.put("shangcjssj", dingd.getDingdjssj());
		updateXianbllkc(user,map);
	}
	
	/**
	 * 按需初始化计算数据准备
	 * @param user 操作用户
	 * @throws ParseException 
	 */
	@SuppressWarnings("static-access")
	public void chushDataPreparation(String user,String dingdjssj,Map<String,String> mosParam,String jisLx) throws ParseException{
		
		/**
		 * 清除中间表数据
		 */
		anxjis.clearData(user,"'"+Const.DINGD_LX_ANX_CSH_C+"','"+Const.DINGD_LX_ANX_CSH_M+"'");
		
		/**
		 * 判断生效期,保留生效期在最近一个工作日之前
		 */
		Map<String, String> param = new HashMap<String, String>();
		String today = dingdjssj.substring(0,10);//当前日期
		//查询将来外部模式为CD/C1,将来模式2为MD/M1的用户中心,零件,产线,消耗点
		List<Anxjscszjb> listAnxjscszjbs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxChushJis.queryCMMos",mosParam);
		List<Anxjscszjb> listAnxjscszjb = new ArrayList<Anxjscszjb>();
		param.put("gongzr", today);
		//遍历用户中心,零件,消耗点集合
		for (int i = 0; i < listAnxjscszjbs.size(); i++) {
			//查询每条产线的最近一个工作日
			Anxjscszjb anxjscszjb = listAnxjscszjbs.get(i);
			param.put("lingjbh", anxjscszjb.getLingjbh());//零件编号
			param.put("xiaohd", anxjscszjb.getXiaohd());//消耗点
			param.put("usercenter", anxjscszjb.getUsercenter());//用户中心	
			String mos = "";//模式
			//C1CD模式
			if (Const.ANX_MS_CD.equalsIgnoreCase(anxjscszjb.getMos()) || Const.ANX_MS_C1.equalsIgnoreCase(anxjscszjb.getMos())) {
				mos = anxjscszjb.getMos();
			} else {
				//模式2为M1MD模式
				mos = anxjscszjb.getMos2();
			}
			if (Const.ANX_MS_C1.equalsIgnoreCase(mos) || Const.ANX_MS_M1.equalsIgnoreCase(mos)) {
				param.put("shengcxbh", anxjscszjb.getXianbck());//线边仓库
			}else{
				param.put("shengcxbh", anxjscszjb.getShengcxbh());//生产线
			}
			Map JisDateCsh = getJisDateCsh(anxjscszjb,user,mos);//封闭期天数
			if(JisDateCsh == null){
				continue;
			}
			int num = (Integer) JisDateCsh.get("day");
			param.put("num",String.valueOf(num + 1));//天数差,最近一个工作日+封闭期
			List<Ticxxsj> listTicxxsj =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryTicxxsjGzrNumCsh",param);//查询封闭期日期
			if(listTicxxsj == null || listTicxxsj.isEmpty()){
				anxjis.saveYicbj(anxjscszjb, user, "查询最近一个工作日为空,消耗点:产线:"+param.get("shengcxbh")+"订单计算时间"+dingdjssj.substring(0,10)+"工作日历为空", mos);
				continue;
			}
			Ticxxsj ticxxsj = listTicxxsj.get(num);
			Ticxxsj ticxxsj2 = listTicxxsj.get(0);
			if(ticxxsj == null){
				anxjis.saveYicbj(anxjscszjb, user, "查询最近一个工作日为空,消耗点:产线:"+param.get("shengcxbh")+"订单计算时间"+dingdjssj.substring(0,10)+"工作日历为空", mos);
				continue;
			}
 			Date gzr = CommonFun.sdf.parse(ticxxsj.getGongzr());//最近一个工作日+封闭期后的日期
			//如果为外部模式,模式为C1CD
			if(Const.ANX_MS_C1.equalsIgnoreCase(mos) || Const.ANX_MS_CD.equalsIgnoreCase(mos)){
				//判断生效日期是否在//最近一个工作日+封闭期后的日期之前,小于等于
				if(CommonFun.sdf.parse(anxjscszjb.getShengxsj()).compareTo(gzr) <= 0){
					listAnxjscszjb.add(listAnxjscszjbs.get(i));
					//插入毛需求数据到毛需求中间表
					insertAnxMaoxqZjb(jisLx,dingdjssj,ticxxsj2.getGongzr(),param);
				}
			}else{
				//判断生效日期是否在//最近一个工作日+封闭期后的日期之前,小于等于
				if(CommonFun.sdf.parse(anxjscszjb.getShengxsj2()).compareTo(gzr) <= 0){
					listAnxjscszjb.add(listAnxjscszjbs.get(i));
					//插入毛需求数据到毛需求中间表
					insertAnxMaoxqZjb(jisLx,dingdjssj,ticxxsj2.getGongzr(),param);
				}
			}
		}
		
		/**
		 * 关联物流路径生成按需计算中间表
		 */
		anxjis.wulljDataPreparation(listAnxjscszjb,user,"2",anxParam.xiehztMap);
		logger.info("按需计算中间表生成完毕--------时间"+CommonFun.getJavaTime());
	}
	
	/**
	 * 查询按需毛需求中间表表
	 * @param jisLx 计算类型 1:CDMD模式计算,2:C1M1模式计算
	 * @param dingdjssj 订单计算时间
	 * @param gongzr 最近工作日
	 * @param param 相关参数
	 */
	public void insertAnxMaoxqZjb(String jisLx,String dingdjssj,String gongzr,Map<String,String> param){
		String startTime = "";//开始时间
		String endTime = "";//结束时间
		
		//如果为CDMD模式计算,开始时间取盘点流水号后的毛需求时间
		if("1".equals(jisLx)){
			//获取毛需求开始时间,结束时间
			startTime = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxChushJis.queryStartXiaohsj",param));
		}else{
			//如果是C1M1模式计算,开始时间为计算时间,结束时间为毛需求最大时间
			startTime = dingdjssj.substring(0,16);
		}
		//查询开班时间
		List<Ticxxsj> listTicxxsj = anxParam.ticxxsjMap.get(param.get("usercenter")+param.get("shengcxbh")+gongzr);
		Ticxxsj ticxxsj = listTicxxsj.get(0);
		endTime = ticxxsj.getRiq() + " " +ticxxsj.getShijdkssj();//开班时间
		param.put("xiaohsj", startTime);//开始时间
		param.put("xiaohsj2", endTime.substring(0,16));//结束时间
		//SPPV毛需求,生成毛需求中间表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxChushJis.insertAnxmaoxqzjbSPPV",param);
		//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxjis.insertAnxmaoxqzjbSppv",param);
		//CLV毛需求,生成毛需求中间表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxChushJis.insertAnxmaoxqzjbClv",param);
		
	}
	
	/**
	 * 更新线边理论库存
	 * 初始化计算倒退线边理论库存 编理论库存 = 线边理论库存 - 到库量 + 异常消耗量 + 库存待消耗
	 * @param user 操作用户
	 * @param shangcjssj 上次计算时间
	 */
	public void updateXianbllkc(String user,Map<String,String> map){
		
		/**
		 * 更新线边理论库存
		 */
		logger.info("按需初始化计算初始化计算更新线边理论库存------时间"+CommonFun.getJavaTime());
		//查询将来模式为CDMD的用户中心,零件,消耗点
		List<Lingjxhd> listAnxjscszjb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxChushJis.queryCshLjxhd",map);
		
		String shangcjssj = null;
		String xhsj=null;
		
		//遍历用户中心,零件,消耗点
		for (int i = 0; i < listAnxjscszjb.size(); i++) {
			Lingjxhd lingjxhd = listAnxjscszjb.get(i);
			map.put("usercenter", lingjxhd.getUsercenter());//用户中心
			map.put("lingjbh", lingjxhd.getLingjbh());//零件编号
			map.put("xiaohd", lingjxhd.getXiaohdbh());//消耗点编号
			map.put("xiaohdbh", lingjxhd.getXiaohdbh());//消耗点编号
			//查询初始化资源表
			Chushzyb chushzyb = (Chushzyb)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryOneChushzyb", map);
			if(chushzyb == null){
				continue;
			}
			/******************************** mantis:115010 by hzg 2015.7.17 start  ******************************/
			//1、查询xqjs_kucjscsb是否存在抵消数据（条件：用户中心、零件编号、消耗点、记录日期范围、flag=3）
			Kucjscsb kucsl = null;
			//如果出库量或异常消耗量不为0，则插入和更新xqjs_kucjscsb
			if(BigDecimal.ZERO!=(chushzyb.getChukl()==null?BigDecimal.ZERO:chushzyb.getChukl()) 
					|| BigDecimal.ZERO!=(chushzyb.getYicxhl()==null?BigDecimal.ZERO:chushzyb.getYicxhl())){
				kucsl = (Kucjscsb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxChushJis.queryDixInfo",map);
				chushzyb.setDingdjssj(map.get("shangcjssj"));
				/**** 出库量和异常消耗量取反转换 ：正数转负数，负数转正数 hzg 2015.7.15   ****/
				chushzyb = reverseYicxhl(chushzyb);
				if(kucsl==null){
					//将该数据插入到xqjs_kucjscsb表
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("anxChushJis.insertDixInfoToKucjscsb", chushzyb);
				}else{
					//更新xqjs_kucjscsb表该条数据
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("anxChushJis.updateDixInfoOfKucjscsb", chushzyb);
				}
			}
			/******************************** mantis:115010 by hzg 2015.7.17 start  ******************************/
			//计算库存待消耗,从上次计算时间到结束时间段内的消耗数量
			BigDecimal kucslDxh = BigDecimal.ZERO;
			
			shangcjssj = null;
			xhsj=null;
			//Kucjscsb kucsl = null;
			if(!StringUtils.isEmpty(map.get("shangcjssj"))){
				
				map.put("zhongzlxh", chushzyb.getLiush());//流水号----------------------hzg 2015.6.12流水号 -----------------------------
				//获取毛需求开始时间,结束时间
				String jiessj = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxChushJis.queryLiushsj",map));//
				//-----------------hzg 查询结果为空，按需毛需求中无大于此流水号的数据，导致String Index out of rang 16----------------------
				//如果得不到时间，则默认为系统当前时间sysdate map.put("xiaohsj2", jiessj.substring(0, 16)); -->map.put("xiaohsj2", "".equals(jiessj)?CommonFun.getJavaTime().substring(0, 16):jiessj.substring(0, 16));
				shangcjssj = map.get("shangcjssj").substring(0, 16);
				xhsj = "".equals(jiessj)?CommonFun.getJavaTime().substring(0, 16):jiessj.substring(0, 16);
				//如果xhsj>shangcjssj
				if(xhsj.compareTo(shangcjssj)>0){
					map.put("xiaohsj", shangcjssj);//上次计算时间
					map.put("xiaohsj2", xhsj);
				}else{
					map.put("xiaohsj", xhsj);
					map.put("xiaohsj2", shangcjssj);//上次计算时间
				}
				
				kucslDxh = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxChushJis.queryXiaohl", map));
				kucsl = (Kucjscsb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxChushJis.queryYcxhDh",map);
			}
			if(kucsl == null){
				kucsl = new Kucjscsb();
				kucsl.setDaohl(BigDecimal.ZERO);//到货量,为空视为0
				kucsl.setYicxhl(BigDecimal.ZERO);//异常消耗,为空视为0
			}
			/*************** edit by zbb 2016.6.1 start  ******************************/
			/** 计算理论库存时不考虑到库量和异常消耗量 **/
			//盘点时间早于上次计算时间，向后推线边理论库存；盘点时间晚于上次计算时间，向前倒退线边理论库存。
			if(shangcjssj!=null&&xhsj.compareTo(shangcjssj)<0){
				//初始化计算向后推线边理论库存 编理论库存 = 线边理论库存 - 库存待消耗
				lingjxhd.setXianbllkc(CommonFun.getBigDecimal(chushzyb.getXianbkc()).subtract(kucslDxh));
				logger.info("盘点时间早于上次计算时间，xianbllkc="+lingjxhd.getXianbllkc());
			}else{
				//初始化计算倒退线边理论库存 编理论库存 = 线边理论库存  + 库存待消耗
				lingjxhd.setXianbllkc(CommonFun.getBigDecimal(chushzyb.getXianbkc()).add(kucslDxh));
				logger.info("盘点时间晚于上次计算时间，xianbllkc="+lingjxhd.getXianbllkc());
			}
			/*************** edit by zbb 2016.6.1 end  ******************************/
			lingjxhd.setEditor(user);//编辑人
			lingjxhd.setEdit_time(CommonFun.getJavaTime());//编辑时间
			//更新线边理论库存
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("common.updateLingjxhd", lingjxhd);
		}
		logger.info("按需初始化计算初始化计算更新线边理论库存结束------时间"+CommonFun.getJavaTime());
	}
	
	
	/**
	 * 出库量和异常消耗量取反转换 mantis:0011510 by hzg
	 * @author 贺志国
	 * @date 2015-7-17
	 * @param chushzyb
	 * @return
	 */
	protected Chushzyb reverseYicxhl(Chushzyb chushzyb){
		BigDecimal chukl = chushzyb.getChukl()==null?BigDecimal.ZERO:chushzyb.getChukl();
		BigDecimal yicxhl = chushzyb.getYicxhl()==null?BigDecimal.ZERO:chushzyb.getYicxhl();
		if(chukl!=BigDecimal.ZERO){
			chukl = BigDecimal.ZERO.subtract(chukl);
		}
		if(yicxhl!=BigDecimal.ZERO){
			yicxhl =  BigDecimal.ZERO.subtract(yicxhl);
		}
		chushzyb.setChukl(chukl);
		chushzyb.setYicxhl(yicxhl);
		return chushzyb;
	}
	
	
	
	/**
	 * 初始化计算获取封闭期天数
	 * @param obj 按需计算中间表对象
	 * @param user 操作用户
	 * @param mos 模式
	 * @return 封闭期天数
	 */
	public Map getJisDateCsh(Anxjscszjb obj,String user,String mos){
		Map result = new HashMap();
		int day = 0;
		try {
			//如果是CD,C1,取备货周期,运输周期向上取整
			if(Const.ANX_MS_CD.equalsIgnoreCase(mos) || Const.ANX_MS_C1.equalsIgnoreCase(mos)){
				day = obj.getBeihzq().add(obj.getYunszq()).setScale(0,BigDecimal.ROUND_UP).intValue();
			}else{//如果是MD,M1模式,取备货时间2+仓库送货时间2+仓库返回时间2向上取整
				Map<String,String> map = new HashMap<String,String>();
				String zickbh = "";//子仓库编号
				map.put("usercenter", obj.getUsercenter());
				map.put("cangkbh", obj.getDinghck());
				map.put("lingjbh", obj.getLingjbh());
				zickbh = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxChushJis.selZickbh", map));
				String cangkbh = obj.getDinghck()+zickbh;
				//如果子仓库编号为空,查询子仓库编号
				if(StringUtils.isEmpty(obj.getZickbh())){
					map.put("cangkbh", obj.getXianbck());
					zickbh = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxChushJis.selZickbh", map));
				}else{
					zickbh = obj.getZickbh();
				}
				map.put("cangkbh", cangkbh);
				map.put("fenpqhck", obj.getXianbck()+zickbh);
				map.put("mos", mos);
				Cangkxhsj cangkxhsj = (Cangkxhsj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryCangkxhsj",map);
				//如果备货时间为空
				if(cangkxhsj == null){
					anxjis.saveYicbj(obj, user, "仓库循环时间为空",mos);
					return null;
				}
				day = cangkxhsj.getBeihsj().add(cangkxhsj.getCangkshsj()).add(cangkxhsj.getCangkfhsj()).setScale(0,BigDecimal.ROUND_UP).intValue();
				result.put("cangkshsj", cangkxhsj.getCangkshsj());
			}
		} catch (Exception e) {
			return null;
		}
		result.put("day", day);
		
		return result;
	}
	
	/**
	 * 计算初始资源
	 * @param bean 按需计算中间表对象
	 * @param dingdjssj 订单计算时间
	 * @param mos 模式
	 * @param user 操作用户
	 * @return 初始资源数量
	 */
	public BigDecimal resourceCalculationCsh(Anxjscszjb bean,String dingdjssj,String mos,String user){
	
		/**
		 * 按需初始化资源计算
		 */
		String dateNow = dingdjssj.substring(0,10);//当前时间
		BigDecimal weijfzl = BigDecimal.ZERO;//未交付数量
		BigDecimal ziy = BigDecimal.ZERO;//资源数量
		BigDecimal weijfzlbj = BigDecimal.ZERO;//未交付报警
		if(Const.ANX_MS_C1.equals(mos) || Const.ANX_MS_M1.equals(mos)){
			//查询库存(资源快照)
			Ziykzb ziykzb = anxjis.queryZiykzb(bean.getUsercenter(),bean.getLingjbh(),bean.getXianbck(), dateNow);
			//C1M1模式:未交付=已发要货总量 – 交付总量 – 终止总量(零件仓库)
			weijfzl = CommonFun.getBigDecimal(bean.getCkyifyhlzl()).subtract(CommonFun.getBigDecimal(bean.getCkjiaofzl()))
				.subtract(CommonFun.getBigDecimal(bean.getCkzhongzzl())); 
			//C1M1模式汇总替代件数量
			BigDecimal tidjzl = anxjis.queryTidjsl(bean.getXianbck(), bean.getUsercenter(), bean.getLingjbh(), dateNow,anxParam.tidjMap);
			//C1M1模式资源=库存数量 + 未交付 + 替代件数量   + 仓库系统调整值(年末) - 安全库存(零件仓库)
			ziy = ziykzb.getKucsl().add(weijfzl).add(tidjzl).add(CommonFun.getBigDecimal(bean.getCkxittzz()))
				.subtract(CommonFun.getBigDecimal(bean.getAnqkcsl()));
			weijfzlbj = weijfzl.add(CommonFun.getBigDecimal(bean.getCkxittzz()));
		}else{
			//CDMD模式:未交付=已发要货总量 – 交付总量 – 终止总量(零件消耗点)
			weijfzl = CommonFun.getBigDecimal(bean.getYifyhlzl()).subtract(CommonFun.getBigDecimal(bean.getJiaofzl()))
			.subtract(CommonFun.getBigDecimal(bean.getZhongzzl()));
			Map<String,String> map = new HashMap<String,String>();
			map.put("usercenter", bean.getUsercenter());//用户中心
			map.put("lingjbh", bean.getLingjbh());//零件编号
			map.put("xiaohdbh", bean.getXiaohd());//消耗点编号
			//查询初始化资源表
			Chushzyb chushzyb = (Chushzyb)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryOneChushzyb", map);
			if(chushzyb == null){
				anxjis.saveYicbj(bean, user, "初始化资源表为空",mos,bean.getJianglms());
				return null;
			}
			//CDMD模式资源=线边库存(初始化资源表盘点库存) + 未交付  + 零件消耗点系统调整值(年末) - 安全库存(零件消耗点)
			ziy = chushzyb.getXianbkc().add(weijfzl).add(CommonFun.getBigDecimal(bean.getXhdxittzz()))
				.subtract(CommonFun.getBigDecimal(bean.getAnqkcs()));
			weijfzlbj = weijfzl.add(CommonFun.getBigDecimal(bean.getXhdxittzz()));
		}
		//如果未交付为负数,保存异常报警
		if(weijfzlbj.signum() < 0){
			anxjis.saveYicbj(bean, user, "未交付数量为负数:"+weijfzlbj,mos);
		}
		return ziy;
	}
	
	/**
	 * 查询内部物流时间
	 * @param bean 按需计算对象bean
	 * @param user 操作用
	 * @param mos 供货模式
	 * @return 内部物流时间
	 */
	public BigDecimal queryNeibwlsj(Anxjscszjb bean,String user,String mos) {
		
		/**
		 * 查询卸货站台循环时间
		 */
		Map<String,String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());//用户中心
		String zickbh = "";
		//如果子仓库编号为空,查询子仓库编号
		if(StringUtils.isEmpty(bean.getZickbh())){
			map.put("cangkbh", bean.getXianbck());
			map.put("lingjbh", bean.getLingjbh());
			zickbh = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxChushJis.selZickbh", map));
		}else{
			zickbh = bean.getZickbh();
		}
		map.put("xiehztbh", bean.getXiehztbh().substring(0,4));//卸货站台编组
		map.put("cangkbh", bean.getXianbck()+zickbh);//仓库编号
		map.put("mos", mos);//模式
		Xiehztxhsj xiehztxhsj = (Xiehztxhsj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxChushJis.queryXiehztxhsjObject", map);
		if(xiehztxhsj == null){
			return null;
		}
		return xiehztxhsj.getBeihsj();
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
	 * @return 订单零件信息,资源
	 * @throws ParseException 
	 */
	public Map calculateDingd(String mos,Anxjscszjb anxjscszjb,BigDecimal yaohl,Map<String,String> param,String user,
			String dingdjssj,String gongyslx,String dingdlx,List<Dingdmx> listDingdmx,AnxMaoxqzjb anxMaoxqzjb,
			String xiaohSj,String gongysdm,String state,List<Dingdlj> listDingdlj,String kaibsk,String xiabsj,BigDecimal neibwlsj) throws ParseException{
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
			shul = CommonFun.roundingByPack(yaohl, baozrl);
			//零件累积
			//BigDecimal lingjlj = CommonFun.getBigDecimal(anxParam.lingjSlMap.get(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()));
			//指定供应商为空,不指定供应商,需进行份额计算,根据不同供应商生成订单
			if(StringUtils.isEmpty(anxjscszjb.getZhidgys())){
				param.put("gonghlx", Const.ZHIZAOLUXIAN_IL);
				//获取零件供应商集合
				List<LingjGongys> listLingjGys = anxParam.lingjgysMap.get(param.get("usercenter")+param.get("lingjbh"));
				//如果零件供应商为数量为1,单独供应商供货
				if(listLingjGys.size() == 1){
					String gongysbh = listLingjGys.get(0).getGongysbh();
					String key = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + gongysbh + anxjscszjb.getFenpbh();	
					Wullj wullj = anxParam.wulljMap.get(key);
					/**
					 * 计算备货,到货等时间
					 */
					timeMap = anxjis.timeCalculate(anxjscszjb, mos, user,anxMaoxqzjb.getXhsj(),
							neibwlsj,anxjscszjb.getCangkshsj2(),anxParam.xiehztMap,wullj.getGcbh(),kaibsk,xiabsj,null);
					String biaos = CommonFun.strNull(timeMap.get("biaos"));//标识符
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
					dingd = anxjis.insertDingd(anxjscszjb, gongysbh, mos, user , dingdjssj, gongyslx, dingdlx,state
							,dingdhMap,jiaorRq);
					//保存订单零件
					dingdlj = anxjis.insertDingdlj(anxjscszjb, dingd, user, gongysbh, dingdjssj, yaohRq, mos,jiaorRq);
					if(dingdlj != null){
						listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
					}
					//如果要货数量不为0,保存订单明细
					if(yaohl.signum() != 0){
						//保存订单明细
						dingdmx = anxjis.insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap,shul,yaohl,mos,user,state,
								gongysbh,gongyslx,dingdjssj);
						listDingdmx.add(dingdmx);
					}
					
					/**
					 * 剩余资源,考虑盈余
					 */
					ziy = shul.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
				}else{
				
				/**
				 * 进行份额计算,生成用户中心,零件,供应商份额对象集合
				 */
				anxjis.fenEJis(listLingjGys, baozrl, anxParam.lingjSlMap, shul, anxjscszjb.getUsercenter(), anxjscszjb.getLingjbh(),anxParam.gongysFeneMap);
				
				//供应商累积实际要货量
				BigDecimal shijyhl = BigDecimal.ZERO;
				BigDecimal gysYaohsl = BigDecimal.ZERO; //hzg 2015.8.28
				/**
				 * 判断是否维护了供 应商包装并返回包装个数 begin
				 * hzg 2015.8.27
				 */
				int bzgys = 0;
				int nullBaozgys = anxjis.getBaozGsOfGys(anxjscszjb,user,listLingjGys,mos);
				
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
						anxjis.saveYicbj(anxjscszjb, user, lingjGongys.getGongysbh()+"包装信息为空",mos);
						continue;
					}
					bzgys ++;
					/**  end  **/
					
					anxjscszjb.setGongyhth(lingjGongys.getGongyhth());//供应合同号
					
					//获取供应商份额对象
					GongysFeneJis gongysFene = anxParam.gongysFeneMap.get(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+lingjGongys.getGongysbh());
					//shijyhl = shijyhl.add(gongysFene.getYaohsl());
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
					
					if(gongysFene.getYaohsl().signum() > 0){
						String key = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + lingjGongys.getGongysbh()
						+ anxjscszjb.getFenpbh();	
						Wullj wullj = anxParam.wulljMap.get(key);
						if(wullj == null){
							anxjis.saveYicbj(anxjscszjb, user, "物流路径信息未查询到"+anxjscszjb.getFenpbh(), mos);
							continue;
						}
						/**
						 * 计算备货,到货等时间
						 */
						timeMap = anxjis.timeCalculate(anxjscszjb, mos, user,anxMaoxqzjb.getXhsj(),
								neibwlsj,anxjscszjb.getCangkshsj2(),anxParam.xiehztMap,wullj.getGcbh(),kaibsk,xiabsj,null);
						String biaos = CommonFun.strNull(timeMap.get("biaos"));//标识符
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
						dingd = anxjis.insertDingd(anxjscszjb, lingjGongys.getGongysbh(), mos, user, dingdjssj, lingjGongys.getLeix(), dingdlx,
								state,dingdhMap,jiaorRq);
						//保存订单零件
						dingdlj = anxjis.insertDingdlj(anxjscszjb, dingd, user, lingjGongys.getGongysbh(), dingdjssj, yaohRq, mos,jiaorRq,lingjGongys);
						if(dingdlj != null){
							listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
						}
						//如果要货数量不为0,保存订单明细
						if(yaohl.signum() != 0){
							//保存订单明细    modify：gysYaohsl = gongysFene.getYaohsl()  by hzg
							dingdmx = anxjis.insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, gysYaohsl ,yaohl,mos,user,state,
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
				GongysFeneJis gongysFene = anxParam.gongysFeneMap.get(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+anxjscszjb.getZhidgys());
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
				anxParam.gongysFeneMap.put(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+anxjscszjb.getZhidgys(),gongysFene);
				*/
				//查询物流路径取承运商编号
				String key = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + anxjscszjb.getZhidgys() + anxjscszjb.getFenpbh();	
				Wullj wullj = anxParam.wulljMap.get(key);
				//取指定供应商的物流路径0011221 gswang 2015-03-06
				if(wullj != null){
					anxjscszjb.setFahd(wullj.getFahd());	
				}

				/**
				 * 计算备货,到货等时间
				 */
				timeMap = anxjis.timeCalculate(anxjscszjb, mos, user,anxMaoxqzjb.getXhsj(),
						neibwlsj,anxjscszjb.getCangkshsj2(),anxParam.xiehztMap,wullj.getGcbh(),kaibsk,xiabsj,null);
				String biaos = CommonFun.strNull(timeMap.get("biaos"));//标识符
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
				dingd = anxjis.insertDingd(anxjscszjb, anxjscszjb.getZhidgys(), mos, user , dingdjssj, gongyslx, dingdlx,state
						,dingdhMap,jiaorRq);
				//保存订单零件
				dingdlj = anxjis.insertDingdlj(anxjscszjb, dingd, user, anxjscszjb.getZhidgys(), dingdjssj, yaohRq, mos,jiaorRq);
				if(dingdlj != null){
					listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
				}
				//如果要货数量不为0,保存订单明细
				if(yaohl.signum() != 0){
					//保存订单明细
					dingdmx = anxjis.insertDingdmxZdgys(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap,shul,yaohl,mos,user,state,
							anxjscszjb.getZhidgys(),gongyslx,dingdjssj,wullj);
					listDingdmx.add(dingdmx);
				}
				
				/**
				 * 剩余资源,考虑盈余
				 */
				ziy = shul.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
			}
			//anxParam.lingjSlMap.put(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh(), lingjlj.add(shul));
		}else{
			BigDecimal baozrl = BigDecimal.ZERO;
			//MD模式按仓库UC容量取整
			if(Const.ANX_MS_MD.equals(mos) ){
				baozrl = anxjscszjb.getCkucrl();
			}else{
				//M1模式按仓库US容量取整
				baozrl = anxjscszjb.getCkusbzrl();
			}
			shul = CommonFun.roundingByPack(yaohl, baozrl);//按包装取整
			//M1MD模式生成订单
			/**
			 * 计算备货,到货等时间
			 */
			//2014-11-11 仓库送货时间取ckx_cangkxhsj 表中的仓库送货时间，不取物流路径中的Cangkshsj2
//			timeMap = anxjis.timeCalculate(anxjscszjb, mos, user,anxMaoxqzjb.getXhsj(),
//					neibwlsj,anxjscszjb.getCangkshsj2(),anxParam.xiehztMap,anxjscszjb.getDinghck(),kaibsk,xiabsj,null);
			timeMap = anxjis.timeCalculate(anxjscszjb, mos, user,anxMaoxqzjb.getXhsj(),
					neibwlsj,CommonFun.getBigDecimal(param.get("cangkshsj")),anxParam.xiehztMap,anxjscszjb.getDinghck(),kaibsk,xiabsj,null);
			String biaos = CommonFun.strNull(timeMap.get("biaos"));//标识符
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
			dingd = anxjis.insertDingd(anxjscszjb, gongysdm, mos, user, dingdjssj, gongyslx, dingdlx,state,
					dingdhMap,jiaorRq);
			//保存订单零件
			dingdlj = anxjis.insertDingdlj(anxjscszjb, dingd, user, gongysdm, dingdjssj, yaohRq, mos,jiaorRq);
			if(dingdlj != null){
				listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
			}
			//如果要货数量不为0,保存订单明细
			if(yaohl.signum() != 0){
				//保存订单明细
				dingdmx = anxjis.insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, shul,yaohl,mos,user,state,
						gongysdm,gongyslx,dingdjssj);
				listDingdmx.add(dingdmx);
			}
			
			/**
			 * 剩余资源,考虑盈余
			 */
			ziy = shul.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
		}
		/**
		 * 返回计算结果
		 */
		result.put("dingd", dingd);
		result.put("dingdlj", listDingdlj);
		result.put("dingdmx", listDingdmx);
		result.put("ziy", ziy);
		return result;
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
	 * @return 订单零件信息,资源
	 */
	public Map calculateDingd(String mos,Anxjscszjb anxjscszjb,BigDecimal yaohl,Map<String,String> param,String user,
			String dingdjssj,String gongyslx,String dingdlx,List<Dingdmx> listDingdmx,AnxMaoxqzjb anxMaoxqzjb,
			String xiaohSj,Map<String,String> timeMap,String gongysdm,String state,List<Dingdlj> listDingdlj){
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
		//如果是C1CD模式需进行份额计算
		if(Const.ANX_MS_C1.equals(mos) || Const.ANX_MS_CD.equals(mos)){

			/**
			 * 份额计算
			 */
			//包装容量 = uc容量*uauc个数
			BigDecimal baozrl = anxjscszjb.getGysucrl().multiply(anxjscszjb.getGysuaucgs());
			//按包装取整
			shul = CommonFun.roundingByPack(yaohl, baozrl);
			//指定供应商为空,不指定供应商,需进行份额计算,根据不同供应商生成订单
			if(StringUtils.isEmpty(anxjscszjb.getZhidgys())){
				param.put("gonghlx", Const.ZHIZAOLUXIAN_IL);
				//获取零件供应商集合
				List<LingjGongys> listLingjGys = anxParam.lingjgysMap.get(param.get("usercenter")+param.get("lingjbh"));
				
				/**
				 * 进行份额计算,生成用户中心,零件,供应商份额对象集合
				 */
				anxjis.fenEJis(listLingjGys, baozrl, anxParam.lingjSlMap, shul, anxjscszjb.getUsercenter(), anxjscszjb.getLingjbh(),anxParam.gongysFeneMap);
				
				//供应商累积实际要货量
				BigDecimal shijyhl = BigDecimal.ZERO;
				BigDecimal gysYaohsl = BigDecimal.ZERO; //hzg 2015.8.28
				/**
				 * 判断是否维护了供 应商包装并返回包装个数 begin
				 * hzg 2015.8.27
				 */
				int bzgys = 0;
				int nullBaozgys = anxjis.getBaozGsOfGys(anxjscszjb,user,listLingjGys,mos);
				
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
						anxjis.saveYicbj(anxjscszjb, user, lingjGongys.getGongysbh()+"包装信息为空",mos);
						continue;
					}
					bzgys ++;
					/**  end  **/
					anxjscszjb.setGongyhth(lingjGongys.getGongyhth());//供应合同号
					
					//获取供应商份额对象
					GongysFeneJis gongysFene = anxParam.gongysFeneMap.get(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+lingjGongys.getGongysbh());
					//shijyhl = shijyhl.add(gongysFene.getYaohsl());
					
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
					
					
					if(gongysFene.getYaohsl().signum() > 0){
						String key = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + lingjGongys.getGongysbh()
						+ anxjscszjb.getFenpbh();	
						Wullj wullj = anxParam.wulljMap.get(key);
						if(wullj == null){
							anxjis.saveYicbj(anxjscszjb, user, "物流路径信息未查询到"+anxjscszjb.getFenpbh(), mos);
							continue;
						}
						//保存订单
						dingd = anxjis.insertDingd(anxjscszjb, lingjGongys.getGongysbh(), mos, user, dingdjssj, lingjGongys.getLeix(), dingdlx,
								state,dingdhMap,jiaorRq);
						//保存订单零件
						dingdlj = anxjis.insertDingdlj(anxjscszjb, dingd, user, lingjGongys.getGongysbh(), dingdjssj, yaohRq, mos,jiaorRq,lingjGongys);
						if(dingdlj != null){
							listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
						}
						//如果要货数量不为0,保存订单明细
						if(yaohl.signum() != 0){
							//保存订单明细    modify：gysYaohsl = gongysFene.getYaohsl()  by hzg
							dingdmx = anxjis.insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, gysYaohsl,yaohl,mos,user,state,
										dingdjssj,wullj,lingjGongys);
							listDingdmx.add(dingdmx);
						}
					}
				}
				/**
				 * 剩余资源,考虑盈余
				 */
				ziy = shijyhl.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
			}else{
				//查询零件供应商
				param.put("gongysbh", anxjscszjb.getZhidgys());
				//获取供应商份额对象
				GongysFeneJis gongysFene = anxParam.gongysFeneMap.get(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+anxjscszjb.getZhidgys());
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
				anxParam.gongysFeneMap.put(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh()+anxjscszjb.getZhidgys(),gongysFene);
				//取指定供应商的物流路径0011221 gswang 2015-03-06
				String key = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + anxjscszjb.getZhidgys() + anxjscszjb.getFenpbh();	
				Wullj wullj = anxParam.wulljMap.get(key);
				if(wullj != null){
					anxjscszjb.setFahd(wullj.getFahd());
				}
				
				//生成订单
				//保存订单
				dingd = anxjis.insertDingd(anxjscszjb, anxjscszjb.getZhidgys(), mos, user , dingdjssj, gongyslx, dingdlx,state
						,dingdhMap,jiaorRq);
				//保存订单零件
				dingdlj = anxjis.insertDingdlj(anxjscszjb, dingd, user, anxjscszjb.getZhidgys(), dingdjssj, yaohRq, mos,jiaorRq);
				if(dingdlj != null){
					listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
				}
				//如果要货数量不为0,保存订单明细
				if(yaohl.signum() != 0){
					//保存订单明细
					dingdmx = anxjis.insertDingdmxZdgys(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, gongysFene.getYaohsl(),yaohl,mos,user,state,
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
			shul = CommonFun.roundingByPack(yaohl, baozrl);//按包装取整
			//M1MD模式生成订单
			//保存订单
			dingd = anxjis.insertDingd(anxjscszjb, gongysdm, mos, user, dingdjssj, gongyslx, dingdlx,state,
					dingdhMap,jiaorRq);
			//保存订单零件
			dingdlj = anxjis.insertDingdlj(anxjscszjb, dingd, user, gongysdm, dingdjssj, yaohRq, mos,jiaorRq);
			if(dingdlj != null){
				listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
			}
			//如果要货数量不为0,保存订单明细
			if(yaohl.signum() != 0){
				//保存订单明细
				dingdmx = anxjis.insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, xiaohSj, yaohRq,timeMap, shul,yaohl,mos,user,state,
						gongysdm,gongyslx,dingdjssj);
				listDingdmx.add(dingdmx);
			}
			
			/**
			 * 剩余资源,考虑盈余
			 */
			ziy = shul.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
		}
		/**
		 * 返回计算结果
		 */
		Map result = new HashMap();
		result.put("dingd", dingd);
		result.put("dingdlj", listDingdlj);
		result.put("dingdmx", listDingdmx);
		result.put("ziy", ziy);
		return result;
	}
}
