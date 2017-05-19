package com.athena.xqjs.module.anxorder.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.component.utils.LoaderProperties;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.anxorder.AnxMaoxqJLzjb;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.entity.anxorder.Chushzyb;
import com.athena.xqjs.entity.anxorder.Dingdjssj;
import com.athena.xqjs.entity.anxorder.GongysFeneJis;
import com.athena.xqjs.entity.anxorder.Gongysxhc;
import com.athena.xqjs.entity.anxorder.Kuczh;
import com.athena.xqjs.entity.anxorder.Tangchbzjb;
import com.athena.xqjs.entity.anxorder.Ticxxsj;
import com.athena.xqjs.entity.anxorder.UsercenterSet;
import com.athena.xqjs.entity.anxorder.Xianbkc;
import com.athena.xqjs.entity.anxorder.Xiaohcxhdmb;
import com.athena.xqjs.entity.common.Cangkxhsj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Tidj;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.common.Xiehztxhsj;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 新按需初始化计算Service类
 */
@Component
public class XinaxCshJisService extends BaseService {
	
	//配置文件路径 zbb 2015.7.16
	private final String fileName="urlPath.properties"; 
	/**
	 * 按需参数类
	 */
	@Inject
	private XinAxParam anxParam;
	
	/**
	 * 按需计算类
	 */
	@Inject
	private XinaxJisService xinaxjis;
	
	/**
	 * 异常报警Service
	 */
	@Inject
	private YicbjService yicbjService;
	
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;
	
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(XinaxJisService.class);
	
	public void cshAnxOrderMethod(String jisLx,String user,String usercenter) throws ParseException{
		/**
		 * 用户设置的C1CDM1MD名称
		 * 
		 */
		UsercenterSet uset=null;
		/**
		 * 订单号MAP
		 */
	    HashMap<String, String> dingdhMap = null;
		
		/**
		 * 开始计算,计算时间
		 */
 		String dingdjssj = CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS);//订单计算时间
		logger.info("按需初始化计算开始------操作人:"+user+"------订单计算时间:"+dingdjssj);

		/**
		 * 上次计算时间
		 */
		Map<String,String> map = new HashMap<String, String>();
		Dingdjssj dingd = new Dingdjssj();
		
		map.put("usercenter", usercenter);//用户中心
		dingd = (Dingdjssj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxjis.queryDingdjssj",map);
		map.remove("dingdbh");				
		
		//获取用户设置的模式名称
		uset = (UsercenterSet) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxjis.queryUsercenterSet", map);
		if(uset==null){
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
			logger.error("配置不完全，未配置用户中心"+usercenter+"对应的模式名称！");
			//配置不完全，不执行下面代码，报警退出
			yicbjService.saveYicbj("", usercenter, "配置不完全，未配置用户中心"+usercenter+"对应的模式名称！", user, Const.JISMK_ANX_CD, Const.YICHANG_LX2,"");
			return;
		}
		
		map.put("C1", uset.getC1());
		map.put("CD", uset.getCD());
		map.put("M1", uset.getM1());
		map.put("MD", uset.getMD());
		//计算类型为1,即CDMD模式计算
		if("1".equals(jisLx)){
			map.put("mos", uset.getCD());
			map.put("mos2", uset.getMD());
		}else{
			map.put("mos", uset.getC1());
			map.put("mos2", uset.getM1());
		}
		/**
		 * 初始化参考系参数,加载到缓存MAP
		 */
		anxParam.initParam(map);
		dingdhMap = new HashMap<String, String>();		
		HashMap<String,Tangchbzjb> tangchbMap = new HashMap<String,Tangchbzjb>();
		
		/**
		 * 数据准备,生成中间表
		 */
		chushDataPreparation(user, dingdjssj, map, jisLx,uset);
		
		
		/**
		 * 趟次合并集合,放在生产中间表之后
		 * 
		 */
		List<Tangchbzjb> tangchbList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("xinaxjis.queryTangchbzjbByParam", map);
		for(int i=0;i<tangchbList.size();i++){
			Tangchbzjb tangchbzjb = tangchbList.get(i);
			String key = tangchbzjb.getUsercenter()+tangchbzjb.getChanx()
			+tangchbzjb.getGongysbh()+tangchbzjb.getXiaohcbh()+tangchbzjb.getTangc()+tangchbzjb.getDangqtcrq();
			logger.info("小火车趟次合并的key:"+key);
			tangchbMap.put(key, tangchbzjb);
		}
		tangchbList = null;
		
		
		//计算类型为1,即CDMD模式计算
		if("1".equals(jisLx)){
			/**
			 *进行CDMD模式计算 ,计算类型为1
			 */
			logger.info("按需计算CDMD部分计算开始------时间:"+CommonFun.getJavaTime());
			anxCalculate(dingdjssj, user,"1",map,dingd.getDingdsxsj().toString(), anxParam.tidjMap, anxParam.ticxxsjMap,anxParam.xiehztMap,anxParam.lingjSlMap,anxParam.lingjgysMap,dingdhMap,anxParam.wulljMap,anxParam.gongysFeneMap,anxParam.kuczhMap,tangchbMap,uset);
			logger.info("按需计算CDMD部分计算结束------时间:"+CommonFun.getJavaTime());		
		}else{
			
			/**
			 * 进行C1M1模式计算计算类型为2
			 */
			logger.info("按需计算C1M1部分计算开始------时间:"+CommonFun.getJavaTime());
			anxCalculate(dingdjssj, user,"2",map,dingd.getDingdsxsj().toString(), anxParam.tidjMap, anxParam.ticxxsjMap,anxParam.xiehztMap,anxParam.lingjSlMap,anxParam.lingjgysMap,dingdhMap,anxParam.wulljMap,anxParam.gongysFeneMap,anxParam.kuczhMap,tangchbMap,uset);
			logger.info("按需计算C1M1部分计算结束------时间:"+CommonFun.getJavaTime());			
		}
		
		/**
		 * 将已经计算的anxMaoxq_jlzjb中的记录flag=2同步到anxMaoxq_jl表中 flag=2
		 */
		logger.info("更新anxMaoxq_jl开始");
		map.put("usercenter", usercenter);
		map.put("editor", user);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("xinaxjis.updateAnxmaoxqJL2",map);
		logger.info("更新anxMaoxq_jl结束");
		
		
		/**
		 * 更新订单状态
		 */
		logger.info("更新订单状态");
		xinaxjis.updateDingd(dingdjssj, Const.DINGD_STATUS_DSX, user,"'"+Const.DINGD_LX_ANX_CSH_C+"','"+Const.DINGD_LX_ANX_CSH_M+"'");
		
		/**
		 * 计算完成，更新计算时间
		 * 
		 */
		xinaxjis.updateShengxsj(user,map,dingdjssj);
							
		/**
		 * 按需计算结束,销毁参考系参数
		 */
		anxParam.destory();
		dingdhMap = null;
		logger.info("按需计算结束------操作人:"+user+"------时间:"+CommonFun.getJavaTime());
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
			Map<String,List<LingjGongys>> lingjgysMap,HashMap<String, String> dingdhMap,Map<String,Wullj> wulljMap,Map<String,GongysFeneJis> gongysFeneMap,HashMap<String,Kuczh> kuczhMap,HashMap<String,Tangchbzjb> tangchbzjbMap,UsercenterSet uset) throws ParseException{
		
		List<Dingdlj> listDingdlj = new ArrayList<Dingdlj>();//订单零件集合
		List<Dingdmx> listDingdmx = new ArrayList<Dingdmx>();//订单明细集合
		Map<String,String> param = new HashMap<String,String>();
		List<AnxMaoxqJLzjb> yijsAnxMaoxqList= null;
		
		/**
		 * 根据计算类型进行遍历计算
		 */
		List<Anxjscszjb> listAnxjscszjb = new ArrayList<Anxjscszjb>();
		//如果计算类型为1,即CDMD模式计算,更新线边理论库存
		if("1".equals(jislx)){
			//计算类型1,CDMD模式计算,按用户中心,零件编号,消耗点进行遍历
			listAnxjscszjb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("xinaxChushJis.queryAnxjscszjbCDMD",map);
		}else{
			//计算类型2,C1M1模式计算,按用户中心,零件编号,线边仓库进行遍历
			listAnxjscszjb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("xinaxChushJis.queryAnxjscszjbC1M1",map);
		}
		
		//遍历计算中间表集合
		for (int i = 0; i < listAnxjscszjb.size(); i++) {
			Anxjscszjb anxjscszjb = listAnxjscszjb.get(i);			
			
			String key = anxjscszjb.getUsercenter()+anxjscszjb.getXianbck();
			Kuczh kuczh = kuczhMap.get(key);			

			yijsAnxMaoxqList = new ArrayList<AnxMaoxqJLzjb>();//已经计算的anxmaoxq
			
			/**
			 * 模式判断
			 */
			String mos = "";
			String gongysdm = "";//供应商代码
			String gongyslx = "";//供应商类型
			String dingdlx = "";//订单类型
			Date shengxrq = null;//将来模式生效日期
			if (uset.getCD().equalsIgnoreCase(anxjscszjb.getMos()) || uset.getC1().equalsIgnoreCase(anxjscszjb.getMos())) {
				mos = anxjscszjb.getMos();
				gongysdm = anxjscszjb.getGongysbh();//C1CD模式供应商为供应商
				gongyslx = anxjscszjb.getGongyslx();//供应商类型为供应商类型
				dingdlx = Const.DINGD_LX_ANX_CSH_C;//订单类型为 按需初始化C
				
				//将来模式生效日期不为空
				if(!StringUtils.isEmpty(anxjscszjb.getShengxsj())){
					shengxrq = CommonFun.sdfAxparse(anxjscszjb.getShengxsj());
				}	
			} else {
				mos = anxjscszjb.getMos2();
				gongysdm = anxjscszjb.getDinghck();//M1MD模式供应商为订货库
				gongyslx = anxjscszjb.getDinghcklx();//供应商类型为订货库类型
				dingdlx = Const.DINGD_LX_ANX_CSH_M;//订单类型为 按需正常M
				
				//将来模式生效日期不为空
				if(!StringUtils.isEmpty(anxjscszjb.getShengxsj())){
					shengxrq = CommonFun.sdfAxparse(anxjscszjb.getShengxsj2());
				}
			}
			
			/**
			 * 计算封闭期
			 */
			Map JisDateCsh = getJisDateCsh(anxjscszjb,user,mos,uset);//封闭期天数
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
			if (uset.getC1().equalsIgnoreCase(mos) || uset.getM1().equalsIgnoreCase(mos)) {
				param.put("shengcxbh", anxjscszjb.getXianbck());//线边仓库
			}else{
				param.put("shengcxbh", anxjscszjb.getShengcxbh());//生产线
			}	
				
			//	param.put("shengcxbh", anxjscszjb.getShengcxbh());//生产线
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
			List<Ticxxsj> listJsrq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("xinaxChushJis.queryTicxxsjGzrNumCsh",param);//查询封闭期日期
			//工作日历中没有相关的维护,系统给出报警,不进行计算
			if(listJsrq == null || listJsrq.isEmpty() || day > listJsrq.size()+1){
				xinaxjis.saveYicbj(anxjscszjb, user, dingdjssj+"工作日历为空",mos,uset);
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
				xinaxjis.saveYicbj(anxjscszjb, user, jisrq+"封闭期工作日历为空",mos,uset);
				continue;
			}
			kaibsj = listTicxxsj.get(0).getRiq() + " " +listTicxxsj.get(0).getShijdkssj();//计算日期开班时间
			jisrq = listJsrq.get(num - 1).getGongzr();//计算日期取到最后一天
			listTicxxsj = anxParam.ticxxsjMap.get(anxjscszjb.getUsercenter()+param.get("shengcxbh")+jisrq);
			if(listTicxxsj == null){
				xinaxjis.saveYicbj(anxjscszjb, user, jisrq+"封闭期工作日历为空",mos,uset);
				continue;
			}
			xiabsj = listTicxxsj.get(listTicxxsj.size() - 1).getRiq() + " " + listTicxxsj.get(listTicxxsj.size() - 1).getShijdjssj();//计算日期下班时间
			
			logger.info("开班时间："+kaibsj);
			logger.info("下班时间："+xiabsj);
			logger.info("计算日期："+jisrq);
			param.put("lingjbh", anxjscszjb.getLingjbh()); 
			param.put("jisrq", jisrq);
			

			//设置缺省的小货车编号
			if (uset.getC1().equalsIgnoreCase(mos) || uset.getM1().equalsIgnoreCase(mos)) {
				anxjscszjb.setXiaohcbh("DEFLT");
			}
			
			/**
			 * 计算初始资源
			 */
			BigDecimal ziy = resourceCalculationCsh(anxjscszjb, dingdjssj,ziywjsj, mos, user,uset);
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
				timeMap.put("beihsj", time);//小火车备货时间
				timeMap.put("shangxsj", time);//小火车上线时间
				timeMap.put("tangc", "0");
				//0013112: 新按需计算---跨日期的趟次合并，工作日信息记录不准确 by CSY 20170112
				timeMap.put("xiaohcrq", time);//小火车日期
				//0013016: 按需紧急订单的发运时间 by CSY 20170112
				timeMap.put("faysj", xinaxjis.getFaysj(mos, kaibsj, time, anxjscszjb, uset));//发运日期
				AnxMaoxqJLzjb anxMaoxqzjb = new AnxMaoxqJLzjb();			
				anxMaoxqzjb.setXiaohcrq(kaibsj.substring(0,10));
				Map mapDingdlj = xinaxjis.calculateDingd(mos, anxjscszjb, yaohl, param, user, dingdjssj, gongyslx, dingdlx, anxMaoxqzjb, time, timeMap, gongysdm, Const.DINGD_STATUS_DSX, listDingdmx, listDingdlj, lingjSlMap, lingjgysMap, gongysFeneMap, wulljMap, dingdhMap,uset);
				ziy = CommonFun.getBigDecimal(mapDingdlj.get("ziy"));
				logger.info("资源数:"+ziy);
			}
			
			
			//更新xqjs_anxmaoxq_jlzjb的flag为1,开始计算
			param.put("chanx", anxjscszjb.getShengcxbh());
			if("1".equals(jislx)){
				param.put("xiaohd", anxjscszjb.getXiaohd());
			}			
			param.put("xiaohcbh", anxjscszjb.getXiaohcbh());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("xinaxjis.updateAnxmaoxqJLzjb1", param);
			logger.info("更新jlzjb1:"+param.get("chanx")+"-"+param.get("xiaohd")+"-"+param.get("lingjbh"));
			
			
			/**
			 * 取用户中心,零件,消耗点,封闭期后日期毛需求,按需求时间排序,升序
			 */
			List<AnxMaoxqJLzjb> listAnxMaoxq = new ArrayList<AnxMaoxqJLzjb>();
			
			//计算类型1,CDMD模式计算,按用户中心,零件编号,消耗点取毛需求
			if("1".equals(jislx)){
				param.put("xiaohd", anxjscszjb.getXiaohd());
				param.put("xiaohcbh", anxjscszjb.getXiaohcbh());
				listAnxMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("xinaxjis.queryAnxmaoxqzjbMx",param);
			}else{
				//计算类型2,C1M1模式计算,按用户中心,零件编号,线边仓库汇总毛需求
				param.put("xianbck", anxjscszjb.getXianbck());
				param.put("xiaohcbh", "DEFLT");
				listAnxMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("xinaxjis.queryAnxmaoxqzjbMxByXianbk",param);
			}
			/**
			 * 计算备货,到货等时间
			 */
			
			// 内部物流时间
			BigDecimal neibwlsj = queryNeibwlsj(anxjscszjb, user, mos);
			if(neibwlsj == null){
				xinaxjis.saveYicbj(anxjscszjb, user, "内部物流时间为空", mos,uset);
				continue;
			}
			
			logger.info("毛需求列表数量:"+listAnxMaoxq.size());
			
			//定义以待定供应商list
			List yiddgys = new ArrayList<String>();
			//检验零件供应商是否有tangc合并	
			boolean hasTangchb = false;
			
			//根据用户中心查出所有有效的gongysxhc
			Map<String,String> tmp = new HashMap<String,String>();
			tmp.put("usercenter", anxjscszjb.getUsercenter());
			//有效的供应商小火车
			tmp.put("biaos", "1");
			if (uset.getCD().equalsIgnoreCase(mos)) {
				List<LingjGongys> listLingjGys = lingjgysMap.get(anxjscszjb.getUsercenter()+anxjscszjb.getLingjbh());
				if(listLingjGys!=null&&listLingjGys.size()>0){
					for(int k=0;k<listLingjGys.size();k++){
						tmp.put("gongysbh", listLingjGys.get(k).getGongysbh());
						tmp.put("shengcxbh", anxjscszjb.getShengcxbh());
						tmp.put("xiaohcbh", anxjscszjb.getXiaohcbh());
						List<Gongysxhc> gongysxhcList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("gongysxhc.queryGongysxhcByParam",tmp);
						if(gongysxhcList!=null&&gongysxhcList.size()>0){
							hasTangchb = true;
							break;
						}
					}
				}
			}
			tmp.clear();
			tmp = null;
			// 遍历合并后的JL毛需求
			for (int j = 0; j < listAnxMaoxq.size(); j++) {
				AnxMaoxqJLzjb anxMaoxqzjb = listAnxMaoxq.get(j);			
				
				/**
				 * 计算要货量
				 */
				ziy = ziy.subtract(anxMaoxqzjb.getXiaohxs());//资源 = 资源-消耗数量
				//如果资源大于等于0,则无需要货
				BigDecimal yaohl = BigDecimal.ZERO;
				if(ziy.signum() >= 0){
					yijsAnxMaoxqList.add(anxMaoxqzjb);//未要货但计算成功，添加到已计算中
					if(hasTangchb){//有tangc合并继续向下计算
						yaohl =BigDecimal.ZERO;
					}else{
						continue;
					}
					//continue;
				}else{
					yaohl = ziy.abs();//要货量为将资源补零
				}
				logger.info("要货量:"+yaohl);
				String yaohrq = anxMaoxqzjb.getXiaohcrq();//要货日期
				logger.info("要货日期:"+yaohrq);
				
				Map mapDingdlj = calculateDingd(mos, anxjscszjb, yaohl, param, user, dingdjssj, gongyslx, dingdlx, 
						anxMaoxqzjb, yaohrq, gongysdm,Const.DINGD_STATUS_YSX,listDingdmx,listDingdlj,lingjSlMap,lingjgysMap,gongysFeneMap,wulljMap,
						dingdhMap,xiehztMap,kaibsj,xiabsj,neibwlsj,shengxrq,tangchbzjbMap,uset,yiddgys);
				String biaos = this.strNullAx(mapDingdlj.get("biaos"));//标识符
				logger.info("标识："+biaos);
				//如果标识符为continue,则代表有异常报警,返回标识符,跳过当前数据计算下一条
				if("continue".equals(biaos)){
					//回补多余减掉的盈余
					ziy = ziy.add(anxMaoxqzjb.getXiaohxs());
					continue;
				//如果标识符为break,则代表计算结束,该品种不再计算
				}else if("break".equals(biaos)){
					//回补多余减掉的盈余
					ziy = ziy.add(anxMaoxqzjb.getXiaohxs());
					break;
				}
				
				//计算成功，添加到已计算中
				yijsAnxMaoxqList.add(anxMaoxqzjb);
				
				ziy = CommonFun.getBigDecimal(mapDingdlj.get("ziy"));
				
			}
			logger.info("除去消耗后的盈余:"+ziy);
			//计算完成清空yiddgys
			yiddgys=null;
			
			//更新盈余
			if(kuczh==null||"2".equals(kuczh.getIskaolckjs())){//按 盈余计算，更新盈余
				
				BigDecimal anqkc=BigDecimal.ZERO;
				//CDMD模式
				if("1".equals(jislx)){
					anqkc = xinaxjis.queryAnqkcByXiaohd(anxjscszjb.getUsercenter(),anxjscszjb.getLingjbh(),anxjscszjb.getXiaohd());
				}else{
					anqkc = xinaxjis.queryAnqkcByXianbck(anxjscszjb.getUsercenter(),anxjscszjb.getLingjbh(),anxjscszjb.getXianbck());
				}
				
				logger.info("安全库存:"+anqkc);
				ziy = ziy.add(anqkc);
				logger.info("更新到线边库的盈余（加上安全库存后）:"+ziy);
				xinaxjis.updateYingy(anxjscszjb,ziy,jislx,user);
			}
		
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("xinaxjis.updateAnxmaoxqJLzjb2", yijsAnxMaoxqList);
		}
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ilorder.insertDingdmx", listDingdmx);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("xinaxjis.updateDingdljP0", listDingdlj);
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
	 * @param yijsAnxMaoxqList 
	 * @return 订单生成结果
	 * @throws ParseException
	 */
	public Map calculateDingd(String mos,Anxjscszjb anxjscszjb,BigDecimal yaohl,Map<String,String> param,String user,
			String dingdjssj,String gongyslx,String dingdlx,AnxMaoxqJLzjb anxMaoxqzjb,String yaohrq,String gongysdm,String state,
			List<Dingdmx> listDingdmx,List<Dingdlj> listDingdlj,Map<String,BigDecimal> lingjSlMap,Map<String,List<LingjGongys>> lingjgysMap,
			Map<String,GongysFeneJis> gongysFeneMap,Map<String,Wullj> wulljMap,HashMap<String, String> dingdhMap,Map<String,Xiehzt> xiehztMap,
			String kaibsk,String xiabsj,BigDecimal neibwlsj,Date shengxrq,HashMap<String,Tangchbzjb> tangchbzjbMap,UsercenterSet uset,List yiddgys) throws ParseException{
		BigDecimal ziy = BigDecimal.ZERO;//资源
		BigDecimal shul = BigDecimal.ZERO;//数量
		String yaohRq = yaohrq;//要货日期,小火车日期
		Map<String,String> timeMap = new HashMap<String, String>();
		/**
		 * 生成订单,订单零件,订单明细信息
		 */
		Dingd dingd = new Dingd();//订单
		Dingdlj dingdlj = new Dingdlj();//订单零件
		Dingdmx dingdmx = new Dingdmx();//订单明细
		Map result = new HashMap();
		String key="";
		Tangchbzjb tangchbzjb=null;
		//如果是C1CD模式需进行份额计算
		if(uset.getC1().equals(mos) || uset.getCD().equals(mos)){

			/**
			 * 份额计算
			 */
			//包装容量 = uc容量*uauc个数
			BigDecimal baozrl = anxjscszjb.getGysucrl().multiply(anxjscszjb.getGysuaucgs());
			//按包装取整
			shul = xinaxjis.roundingByPackAx(yaohl, baozrl);
			
			//指定供应商为空,不指定供应商,需进行份额计算,根据不同供应商生成订单
			if(StringUtils.isEmpty(anxjscszjb.getZhidgys())){
				param.put("gonghlx", Const.ZHIZAOLUXIAN_IL);
				//获取零件供应商集合
				List<LingjGongys> listLingjGys = lingjgysMap.get(param.get("usercenter")+param.get("lingjbh"));
				if(listLingjGys==null){
					result.put("biaos", "break");
					logger.info("零件没有供应商"+anxjscszjb.getLingjbh());
					return result;
				}
				//如果零件供应商为数量为1,单独供应商供货
				if(listLingjGys.size() == 1){
					
					String gongysbh = listLingjGys.get(0).getGongysbh();
					
					/**
					 * 根据tangchbzjbMap取出对应的小火车趟次合并信息	
					 */
					key = anxMaoxqzjb.getUsercenter()+anxMaoxqzjb.getChanx()+gongysbh
					+anxMaoxqzjb.getXiaohcbh()+anxMaoxqzjb.getTangc()+anxMaoxqzjb.getXiaohcrq();
					
					tangchbzjb = tangchbzjbMap.get(key);
					
					//查询物流路径取承运商编号
					String wkey = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + gongysbh + anxjscszjb.getFenpbh();	
					Wullj wullj = wulljMap.get(wkey);
					/**
					 * 计算备货,到货等时间
					 */					
					timeMap = xinaxjis.timeCalculate(anxjscszjb, mos, user,neibwlsj,anxjscszjb.getCangkshsj2(),xiehztMap,wullj.getGcbh(),kaibsk,xiabsj,shengxrq,anxMaoxqzjb.getXiaohcbh(),anxMaoxqzjb.getTangc(),anxMaoxqzjb.getXiaohcrq(),tangchbzjb,uset);
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
					}else if("daiding".equals(biaos)){
						result.put("biaos", "break");
						return result;
					}
					String jiaorRq = timeMap.get("zuiwdhsj").substring(0,10);//交付日期
					//生成订单
					//保存订单
					dingd = xinaxjis.insertDingd(anxjscszjb, gongysbh, mos, user , dingdjssj, gongyslx, dingdlx,state
							,dingdhMap,jiaorRq,uset);
					//保存订单零件
					dingdlj = xinaxjis.insertDingdlj(anxjscszjb, dingd, user, gongysbh, dingdjssj, yaohRq, mos,jiaorRq);
					if(dingdlj != null){
						listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
					}
					//如果要货数量不为0,保存订单明细
					if(yaohl.signum() != 0){					
						dingdmx = xinaxjis.insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, yaohRq,timeMap, shul,yaohl,mos,user,state,
								gongysbh,gongyslx,dingdjssj,uset);
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
					xinaxjis.fenEJis(listLingjGys, baozrl, lingjSlMap, shul, anxjscszjb.getUsercenter(), anxjscszjb.getLingjbh(),gongysFeneMap);
					
					//供应商累积实际要货量
					BigDecimal shijyhl = BigDecimal.ZERO;
					BigDecimal gysYaohsl = BigDecimal.ZERO; //hzg 2015.8.28
					/**
					 * 判断是否维护了供 应商包装并返回包装个数 begin
					 * hzg 2015.8.27
					 */
					int bzgys = 0;
					int nullBaozgys = xinaxjis.getBaozGsOfGys(anxjscszjb,user,listLingjGys,mos,uset);
					/**
					 * 生成订单信息
					 */
					List<Dingd> dingdTempList = new ArrayList<Dingd>();
					List<Dingdlj> dingdljTempList = new ArrayList<Dingdlj>();
					List<Dingdmx> dingdmxTempList = new ArrayList<Dingdmx>();
					List<LingjGongys> lingjGongysTempList = new ArrayList<LingjGongys>();//缓存要了货的供应商
				
					//遍历零件供应商集合
					for (int k = 0; k < listLingjGys.size(); k++) {
						LingjGongys lingjGongys = listLingjGys.get(k);
						/** mantis:11450 by hzg AB点要货令未考虑是否维护包装  begin **/
						if(StringUtils.isEmpty(lingjGongys.getUcbzlx()) || lingjGongys.getUcrl() == null 
								||lingjGongys.getUcrl()==BigDecimal.ZERO || StringUtils.isEmpty(lingjGongys.getUabzlx())
								||lingjGongys.getUaucgs() ==null ||lingjGongys.getUaucgs() == BigDecimal.ZERO  ){
							//如果存在未维护包装的供应商，要货量全部给其中一个供应商（不考虑份额）
							xinaxjis.saveYicbj(anxjscszjb, user, lingjGongys.getGongysbh()+"包装信息为空",mos,uset);
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
						
						/**
						 * 根据tangchbzjbMap取出对应的小火车趟次合并信息	
						 */
						key = anxMaoxqzjb.getUsercenter()+anxMaoxqzjb.getChanx()+lingjGongys.getGongysbh()
						+anxMaoxqzjb.getXiaohcbh()+anxMaoxqzjb.getTangc()+anxMaoxqzjb.getXiaohcrq();						
						tangchbzjb = tangchbzjbMap.get(key);
						
						
						String wkey = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + lingjGongys.getGongysbh()
							+ anxjscszjb.getFenpbh();	
						Wullj wullj = wulljMap.get(wkey);
						if(wullj == null){
							xinaxjis.fenEHuit(gongysFene.getYaohsl(), gongysFeneMap, lingjSlMap, lingjGongys);
							xinaxjis.saveYicbj(anxjscszjb, user, "物流路径信息未查询到"+anxjscszjb.getFenpbh(), mos,uset);
							continue;
						}
						/**
						 * 计算备货,到货等时间
						 */
						timeMap = xinaxjis.timeCalculate(anxjscszjb, mos, user,neibwlsj,anxjscszjb.getCangkshsj2(),xiehztMap,wullj.getGcbh(),kaibsk,xiabsj,shengxrq,anxMaoxqzjb.getXiaohcbh(),anxMaoxqzjb.getTangc(),anxMaoxqzjb.getXiaohcrq(),tangchbzjb,uset);
						String biaos = this.strNullAx(timeMap.get("biaos"));//标识符
						logger.info("时间计算标识:"+biaos);
						//如果标识符为continue,则代表有异常报警,返回标识符,跳过当前数据计算下一条
						if("continue".equals(biaos)){
							result.put("biaos", biaos);
							xinaxjis.fenEHuit(gongysFene.getYaohsl(), gongysFeneMap, lingjSlMap, lingjGongys);
							continue;
						//如果标识符为break,则代表计算结束,该品种不再计算
						}else if("break".equals(biaos)){
							result.put("biaos", "continue");
							xinaxjis.fenEHuit(gongysFene.getYaohsl(), gongysFeneMap, lingjSlMap, lingjGongys);
							continue;
						}else if("daiding".equals(biaos)){
							//判断如果已经在yiddgyslist里面就不继续计算,否则
							if(!yiddgys.contains(lingjGongys.getGongysbh())){
								yiddgys.add(lingjGongys.getGongysbh());
							}				
						}
						String jiaorRq = timeMap.get("zuiwdhsj").substring(0,10);//交付日期
						//保存订单
						dingd = xinaxjis.saveDingd(anxjscszjb, lingjGongys.getGongysbh(), mos, user, dingdjssj, lingjGongys.getLeix(), dingdlx,
								state,dingdhMap,jiaorRq,uset);
						dingdTempList.add(dingd);
						//保存订单零件
						dingdlj = xinaxjis.saveDingdlj(anxjscszjb, dingd, user, lingjGongys.getGongysbh(), dingdjssj, yaohRq, mos,jiaorRq,lingjGongys);
						if(dingdlj != null){
							dingdljTempList.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
						}
						//如果要货数量不为0,保存订单明细
						if(yaohl.signum() != 0&& gysYaohsl.signum()!=0){
						//保存订单明细    modify：gysYaohsl = gongysFene.getYaohsl()  by hzg
							//shijyhl = shijyhl.add(gongysFene.getYaohsl());
							dingdmx = xinaxjis.insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, yaohRq,timeMap,gysYaohsl,yaohl,mos,user,state,
									dingdjssj,wullj,lingjGongys,uset);
							dingdmxTempList.add(dingdmx);
							lingjGongysTempList.add(lingjGongys);
						}
						
					}
					//如果供应商都为daiding状态就退出，并进行回退，否则生成订单。
					if(yiddgys.size()!=listLingjGys.size()){
						//插入订单，订单明细							
						xinaxjis.insertDingds(dingdTempList);
						xinaxjis.insertDingdljs(dingdljTempList);
						listDingdlj.addAll(dingdljTempList);
						listDingdmx.addAll(dingdmxTempList);
						ziy = shijyhl.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量						
					}else{
						//份额回退
						Dingdmx dingdmxTemp= null;
						for(int i=0;i<dingdmxTempList.size();i++){
							dingdmxTemp = dingdmxTempList.get(i);
							result.put("biaos", "break");
							xinaxjis.fenEHuit(dingdmxTemp.getShul(), gongysFeneMap, lingjSlMap, lingjGongysTempList.get(i));
							return result;
						}
					}
					
				}
			}else{//指定供应商
				//查询零件供应商
				param.put("gongysbh", anxjscszjb.getZhidgys());				
				
				/**
				 * 根据tangchbzjbMap取出对应的小火车趟次合并信息	
				 */
				key = anxMaoxqzjb.getUsercenter()+anxMaoxqzjb.getChanx()+anxjscszjb.getZhidgys()
				+anxMaoxqzjb.getXiaohcbh()+anxMaoxqzjb.getTangc()+anxMaoxqzjb.getXiaohcrq();						
				tangchbzjb = tangchbzjbMap.get(key);
				
				//取指定供应商的物流路径0011221 gswang 2015-03-06
				String wkey = anxjscszjb.getUsercenter() + anxjscszjb.getLingjbh() + anxjscszjb.getZhidgys() + anxjscszjb.getFenpbh();	
				Wullj wullj = wulljMap.get(wkey);
				if(wullj !=null ){
					anxjscszjb.setFahd(wullj.getFahd());
				}

				/**
				 * 计算备货,到货等时间
				 */
				timeMap = xinaxjis.timeCalculate(anxjscszjb, mos, user,neibwlsj,anxjscszjb.getCangkshsj2(),xiehztMap,wullj.getGcbh(),kaibsk,xiabsj,shengxrq,anxMaoxqzjb.getXiaohcbh(),anxMaoxqzjb.getTangc(),anxMaoxqzjb.getXiaohcrq(),tangchbzjb,uset);
				String biaos = this.strNullAx(timeMap.get("biaos"));//标识符
				//如果标识符为continue,则代表有异常报警,返回标识符,跳过当前数据计算下一条
				if("continue".equals(biaos)){
					result.put("biaos", biaos);
					return result;
				//如果标识符为break,则代表计算结束,该品种不再计算
				}else if("break".equals(biaos)){
					result.put("biaos", biaos);
					return result;
				}else if("daiding".equals(biaos)){
					result.put("biaos", "break");
					return result;
				}
				String jiaorRq = timeMap.get("zuiwdhsj").substring(0,10);//交付日期
				//生成订单
				//保存订单
				dingd = xinaxjis.insertDingd(anxjscszjb, anxjscszjb.getZhidgys(), mos, user , dingdjssj, gongyslx, dingdlx,state
						,dingdhMap,jiaorRq,uset);
				//保存订单零件
				dingdlj = xinaxjis.insertDingdlj(anxjscszjb, dingd, user, anxjscszjb.getZhidgys(), dingdjssj, yaohRq, mos,jiaorRq);
				if(dingdlj != null){
					listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
				}
				//如果要货数量不为0,保存订单明细
				if(yaohl.signum() != 0){
				//保存订单明细
					dingdmx = xinaxjis.insertDingdmxZdgys(dingd, anxjscszjb, anxMaoxqzjb, yaohRq,timeMap, shul,yaohl,mos,user,state,
								anxjscszjb.getZhidgys(),gongyslx,dingdjssj,wullj,uset);
					listDingdmx.add(dingdmx);					
				}
				
				/**
				 * 剩余资源,考虑盈余
				 */
				ziy = shul.subtract(yaohl);//盈余 = 实际要货数量 - 计划要货数量
			}
		}else{//M1,MD模式计算
			BigDecimal baozrl = BigDecimal.ZERO;
			//MD模式按仓库UC容量取整
			if(uset.getMD().equals(mos) ){
				baozrl = anxjscszjb.getCkucrl();
			}else{
				//M1模式按仓库US容量取整
				baozrl = anxjscszjb.getCkusbzrl();
			}
			shul = xinaxjis.roundingByPackAx(yaohl, baozrl);//按包装取整
			//M1MD模式生成订单
			/**
			 * 计算备货,到货等时间
			 */
			timeMap = xinaxjis.timeCalculate(anxjscszjb, mos, user,neibwlsj,anxjscszjb.getCangkshsj2(),xiehztMap,anxjscszjb.getDinghck(),kaibsk,xiabsj,shengxrq,anxMaoxqzjb.getXiaohcbh(),anxMaoxqzjb.getTangc(),anxMaoxqzjb.getXiaohcrq(),tangchbzjb,uset);
			String biaos = this.strNullAx(timeMap.get("biaos"));//标识符
			
			//如果标识符为continue,则代表有异常报警,返回标识符,跳过当前数据计算下一条
			if("continue".equals(biaos)){
				result.put("biaos", biaos);
				return result;
			//如果标识符为break,则代表计算结束,该品种不再计算
			}else if("break".equals(biaos)){
				result.put("biaos", biaos);
				return result;
			}else if("daiding".equals(biaos)){
				result.put("biaos", "break");
				return result;
			}
			String jiaorRq = timeMap.get("zuiwdhsj").substring(0,10);//交付日期
			//保存订单
			dingd = xinaxjis.insertDingd(anxjscszjb, gongysdm, mos, user, dingdjssj, gongyslx, dingdlx,state,
					dingdhMap,jiaorRq,uset);
			//保存订单零件
			dingdlj = xinaxjis.insertDingdlj(anxjscszjb, dingd, user, gongysdm, dingdjssj, yaohRq, mos,jiaorRq);
			if(dingdlj != null){
				listDingdlj.add(dingdlj);//保存订单零件,按需计算完毕后汇总明细数量为订单零件的P0数量
			}
			//如果要货数量不为0,保存订单明细
			if(yaohl.signum() != 0){
				//保存订单明细
				dingdmx = xinaxjis.insertDingdmx(dingd, anxjscszjb, anxMaoxqzjb, yaohRq,timeMap, shul,yaohl,mos,user,state,
						gongysdm,gongyslx,dingdjssj,uset);
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
	 * 按需初始化计算数据准备
	 * @param user 操作用户
	 * @throws ParseException 
	 */
	@SuppressWarnings("static-access")
	public void chushDataPreparation(String user,String dingdjssj,Map<String,String> map,String jisLx,UsercenterSet uset) throws ParseException{
		
		/**
		 * 清除中间表数据
		 */
		xinaxjis.clearData(user,"'"+Const.DINGD_LX_ANX_CSH_C+"','"+Const.DINGD_LX_ANX_CSH_M+"'");
		
		/**
		 * 判断生效期,保留生效期在最近一个工作日之前
		 */
		Map<String, String> param = new HashMap<String, String>();
		String today = dingdjssj.substring(0,10);//当前日期		
		
		//查询将来外部模式为CD/C1,将来模式2为MD/M1的用户中心,零件,产线,消耗点
		List<Anxjscszjb> listAnxjscszjbs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("xinaxChushJis.queryCMMos",map);
		List<Anxjscszjb> listAnxjscszjb = new ArrayList<Anxjscszjb>();
		param.put("gongzr", today);
		//遍历用户中心,零件,消耗点集合
		for (int i = 0; i < listAnxjscszjbs.size(); i++) {
			//查询每条产线的最近一个工作日
			Anxjscszjb anxjscszjb = listAnxjscszjbs.get(i);	
			
//			//测试专用需要删除
//			if(anxjscszjb.getLingjbh().equals("7903011306")&&anxjscszjb.getXiaohd().equals("L4201S032")){
//				logger.info("测试专用:"+anxjscszjb.getUsercenter());
//			}
			
			String mos = "";//模式
			//C1CD模式
			if (uset.getCD().equalsIgnoreCase(anxjscszjb.getMos()) || uset.getC1().equalsIgnoreCase(anxjscszjb.getMos())) {
				mos = anxjscszjb.getMos();
			} else {
				//模式2为M1MD模式
				mos = anxjscszjb.getMos2();
			}
			//设置缺省的小货车编号
			if (uset.getC1().equalsIgnoreCase(mos) || uset.getM1().equalsIgnoreCase(mos)) {
				anxjscszjb.setXiaohcbh("DEFLT");
			}
			
			param.put("lingjbh", anxjscszjb.getLingjbh());//零件编号
			param.put("xiaohd", anxjscszjb.getXiaohd());//消耗点
			param.put("usercenter", anxjscszjb.getUsercenter());//用户中心	
			param.put("xiaohcbh", anxjscszjb.getXiaohcbh());//小火车编号
			param.put("shengcxbh", anxjscszjb.getShengcxbh());//生产线
			
			Map JisDateCsh = getJisDateCsh(anxjscszjb,user,mos,uset);//封闭期天数
			if(JisDateCsh == null){
				continue;
			}
			int num = (Integer) JisDateCsh.get("day");
			logger.info("封闭期："+num);
			param.put("num",String.valueOf(num + 1));//天数差,最近一个工作日+封闭期
			List<Ticxxsj> listTicxxsj =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("xinaxjis.queryTicxxsjGzrNumCsh",param);//查询封闭期日期
			if(listTicxxsj == null || listTicxxsj.isEmpty()){
				xinaxjis.saveYicbj(anxjscszjb, user, "查询最近一个工作日为空,消耗点:产线:"+param.get("shengcxbh")+"订单计算时间"+dingdjssj.substring(0,10)+"工作日历为空", mos,uset);
				continue;
			}
			if(listTicxxsj.size()<num+1){
				xinaxjis.saveYicbj(anxjscszjb, user, "查询最近一个工作日为空,消耗点:产线:"+param.get("shengcxbh")+"订单计算时间"+dingdjssj.substring(0,10)+"工作日历为空", mos,uset);
				continue;
			}
			//封闭期结束剔除休息时间
			Ticxxsj fbqjsticxxsj = listTicxxsj.get(num);
			//封闭期开始剔除休息时间
			Ticxxsj fbqksticxxsj = listTicxxsj.get(0);
			
 			Date gzr = CommonFun.sdf.parse(fbqjsticxxsj.getGongzr());//最近一个工作日+封闭期后的日期
 			logger.info("工作日："+fbqjsticxxsj.getGongzr());
 			logger.info("生效时间："+anxjscszjb.getShengxsj());
 			
			//如果为外部模式,模式为C1CD
			if(uset.getC1().equalsIgnoreCase(mos) || uset.getCD().equalsIgnoreCase(mos)){
				if(anxjscszjb.getShengxsj() == null){
	 				xinaxjis.saveYicbj(anxjscszjb, user, "生效时间为空,消耗点:产线:"+param.get("shengcxbh")+"订单计算时间"+dingdjssj.substring(0,10)+"生效时间为空", mos,uset);
					continue;
				}
				//判断生效日期是否在//最近一个工作日+封闭期后的日期之前,小于等于
				if(CommonFun.sdf.parse(anxjscszjb.getShengxsj()).compareTo(gzr) <= 0){
					listAnxjscszjb.add(listAnxjscszjbs.get(i));
					//插入毛需求数据到毛需求中间表
					insertAnxMaoxqZjb(jisLx,dingdjssj,fbqksticxxsj.getGongzr(),fbqjsticxxsj.getGongzr(),anxjscszjb);
				}
			}else{
				if(anxjscszjb.getShengxsj2()==null){
	 				xinaxjis.saveYicbj(anxjscszjb, user, "生效时间为空,消耗点:产线:"+param.get("shengcxbh")+"订单计算时间"+dingdjssj.substring(0,10)+"生效时间为空", mos,uset);
					continue;
				}
				//判断生效日期是否在//最近一个工作日+封闭期后的日期之前,小于等于
				if(CommonFun.sdf.parse(anxjscszjb.getShengxsj2()).compareTo(gzr) <= 0){
					listAnxjscszjb.add(listAnxjscszjbs.get(i));
					//插入毛需求数据到毛需求中间表
					insertAnxMaoxqZjb(jisLx,dingdjssj,fbqksticxxsj.getGongzr(),fbqjsticxxsj.getGongzr(),anxjscszjb);
				}
			}
		}
		
		/**
		 * 关联物流路径生成按需计算中间表
		 */
		xinaxjis.wulljDataPreparation(listAnxjscszjb,user,"2",anxParam.xiehztMap,uset);
		logger.info("按需计算中间表生成完毕--------时间"+CommonFun.getJavaTime());
		
		/**
		 * 生成小火车趟次合并中间表
		 * 
		 */
		xinaxjis.generateTangchbZjb(map.get("usercenter"),dingdjssj,anxParam.ticxxsjMap,uset,user);
	}
	
	/**
	 * 查询按需毛需求中间表表
	 * @param jisLx 计算类型 1:CDMD模式计算,2:C1M1模式计算
	 * @param dingdjssj 订单计算时间
	 * @param gongzr 最近工作日
	 * @param param 相关参数
	 */
	public void insertAnxMaoxqZjb(String jisLx,String dingdjssj,String kaisgzr,String jiesgzr,Anxjscszjb anxjscszjb){				
		Map<String, String> param = new HashMap<String, String>();
		BigDecimal sppvxhl=BigDecimal.ZERO;
		param.put("lingjbh", anxjscszjb.getLingjbh());//零件编号
		param.put("xiaohd", anxjscszjb.getXiaohd());//消耗点
		param.put("usercenter", anxjscszjb.getUsercenter());//用户中心	
		param.put("xiaohcbh", anxjscszjb.getXiaohcbh());//小火车编号
		param.put("shengcxbh", anxjscszjb.getShengcxbh());//生产线
		
		//根据用户中心，产线，零件，消耗点查出流水号
		String liush = this.strNullAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("xinaxChushJis.selectLiush", param));
		if(liush==null||"".equals(liush)){
			param.put("jiesgzr", jiesgzr);
			//焊装毛需求,生成毛需求中间表
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("xinaxChushJis.insertAnxmaoxqzjbHanz",param);
			
			//总装毛需求,生成毛需求中间表
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("xinaxChushJis.insertAnxmaoxqzjbZongz",param);
			return;
		}
		logger.info("liush的值为:"+liush);
		
		
		//根据流水号查询整车序号
		param.put("zongzlsh", liush);
		String zhengcxh = queryZhengcxh(anxjscszjb,liush);		
		if(zhengcxh==null||"".equals(zhengcxh)){
			param.put("jiesgzr", jiesgzr);
			//焊装毛需求,生成毛需求中间表
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("xinaxChushJis.insertAnxmaoxqzjbHanz",param);
			
			//总装毛需求,生成毛需求中间表
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("xinaxChushJis.insertAnxmaoxqzjbZongz",param);
			return;			
		}			
		logger.info("zhengcxh的值为:"+zhengcxh);
		
		
		//小火车模板表找出趟次
		param.put("liush", zhengcxh);
		Xiaohcxhdmb xiaohcxhdmb = this.queryXiaohcxhdmb(anxjscszjb,zhengcxh);
		if(xiaohcxhdmb==null){
			param.put("jiesgzr", jiesgzr);
			//焊装毛需求,生成毛需求中间表
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("xinaxChushJis.insertAnxmaoxqzjbHanz",param);
			
			//总装毛需求,生成毛需求中间表
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("xinaxChushJis.insertAnxmaoxqzjbZongz",param);
			return;
		}
		logger.info("xiaohcxhdmb的tangc和kaislsh:"+xiaohcxhdmb.getTangc()+"-"+xiaohcxhdmb.getKaislsh());	 
		
		//查出开始流水号，整车序号之间的lingjsl之和作为sppvxhl		
		param.put("kaislsh", ""+xiaohcxhdmb.getKaislsh());
		param.put("tangc", ""+xiaohcxhdmb.getTangc());
		sppvxhl = this.querySppvxhl(anxjscszjb,""+xiaohcxhdmb.getKaislsh(),zhengcxh,""+xiaohcxhdmb.getTangc());
		if(sppvxhl==null||"".equals(sppvxhl)){
			sppvxhl = BigDecimal.ZERO;
		}
		
		logger.info("sppvxhl的值为:"+sppvxhl);
		
		//将sppvxhl写入到chushzyb
		param.put("sppvxhl", ""+sppvxhl);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.updateSppvxhl",param);
		
		//将xiaohcrq>=查出的xiaohcrq,tangc>=查出tangc,xiaohcrq<gongzr的jl毛需求插入zjb，带上用户中心，生产线编号，零件编号，小火车编号，flag<>2条件
		param.put("kaisgzr", xiaohcxhdmb.getXiaohcrq());
		param.put("jiesgzr", jiesgzr);
		//焊装毛需求,生成毛需求中间表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("xinaxChushJis.insertAnxmaoxqzjbHanz",param);
		
		//总装毛需求,生成毛需求中间表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("xinaxChushJis.insertAnxmaoxqzjbZongz",param);
	}
		
	
	/**
	 * 初始化计算获取封闭期天数
	 * @param obj 按需计算中间表对象
	 * @param user 操作用户
	 * @param mos 模式
	 * @return 封闭期天数
	 */
	public Map getJisDateCsh(Anxjscszjb obj,String user,String mos,UsercenterSet uset){
		Map result = new HashMap();
		int day = 0;
		try {
			//如果是CD,C1,取备货周期,运输周期向上取整
			if(uset.getCD().equalsIgnoreCase(mos) || uset.getC1().equalsIgnoreCase(mos)){
				day = obj.getBeihzq().add(obj.getYunszq()).setScale(0,BigDecimal.ROUND_UP).intValue();
			}else{//如果是MD,M1模式,取备货时间2+仓库送货时间2+仓库返回时间2向上取整
				Map<String,String> map = new HashMap<String,String>();
				Object zickbh = "";//子仓库编号
				map.put("usercenter", obj.getUsercenter());
				map.put("cangkbh", obj.getDinghck());
				map.put("lingjbh", obj.getLingjbh());
				zickbh = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxChushJis.selZickbh", map));
				String cangkbh = CommonFun.strNull(obj.getDinghck())+zickbh;
				logger.info("cangkbh="+cangkbh);
				map.put("cangkbh", cangkbh);
				//如果子仓库编号为空,查询子仓库编号
				if(StringUtils.isEmpty(obj.getZickbh())){
					map.put("cangkbh", obj.getXianbck());
					zickbh = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxChushJis.selZickbh", map));
				}else{
					zickbh = obj.getZickbh();
				}	
				logger.info("fenpqhck="+obj.getXianbck()+zickbh);			
				map.put("fenpqhck", obj.getXianbck()+zickbh);
				map.put("mos", mos);
				Cangkxhsj cangkxhsj = (Cangkxhsj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryCangkxhsj",map);
				//如果备货时间为空
				if(cangkxhsj == null){
					xinaxjis.saveYicbj(obj, user, "仓库循环时间为空",mos,uset);
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
	 * @param ziywjsj 资源生效时间
	 * @param mos 模式
	 * @param user 操作用户
	 * @return 初始资源数量
	 */
	public BigDecimal resourceCalculationCsh(Anxjscszjb bean,String dingdjssj,String ziywjsj,String mos,String user,UsercenterSet uset){
	
		/**
		 * 按需初始化资源计算
		 */
		String dateNow = dingdjssj.substring(0,10);//当前时间
		BigDecimal weijfzl = BigDecimal.ZERO;//未交付数量
		BigDecimal ziy = BigDecimal.ZERO;//资源数量
		BigDecimal weijfzlbj = BigDecimal.ZERO;//未交付报警		
		Map<String, String> map = new HashMap<String, String>();	
				
		map.put("usercenter", bean.getUsercenter());//用户中心
		map.put("lingjbh", bean.getLingjbh());//零件编号
		map.put("cangk",bean.getXianbck()); //线边仓库
		//logger.info("资源生效时间:"+ziywjsj);
		/**
		 * 计算资源
		 */
		if(uset.getC1().equals(mos) || uset.getM1().equals(mos)){
			//查询用户设置的模式，是按盈余还是按库存计算
			String key = bean.getUsercenter()+bean.getXianbck();
			Kuczh kuczh = anxParam.kuczhMap.get(key);
			if(kuczh!=null&&"1".equals(kuczh.getIskaolckjs())){//按仓库计算
				
				//查询库存(资源快照)
				Ziykzb ziykzb = xinaxjis.queryZiykzb(bean.getUsercenter(),bean.getLingjbh(),bean.getXianbck(), dateNow);
				
				//C1M1模式:未交付=零件仓库已发要货总量 – 零件仓库交付总量 – 零件仓库终止+ 仓库系统调整值(年末)
				weijfzl = this.getBigDecimalAx(bean.getCkyifyhlzl()).subtract(this.getBigDecimalAx(bean.getCkjiaofzl()))
					.subtract(this.getBigDecimalAx(bean.getCkzhongzzl())).add(this.getBigDecimalAx(bean.getCkxittzz())); 
				logger.info("未交付:------"+weijfzl+"已发要货总量:"+bean.getCkyifyhlzl()+"零件仓库交付总量:"+bean.getCkjiaofzl()+"零件仓库终止:"+bean.getCkzhongzzl()+"仓库系统调整值:"+bean.getCkxittzz());
					
				//C1M1模式汇总替代件数量
				BigDecimal tidjzl = xinaxjis.queryTidjsl(bean.getXianbck(), bean.getUsercenter(), bean.getLingjbh(), dateNow,anxParam.tidjMap);
				logger.info("替代件数量:------"+tidjzl);
				
				//计算待消耗
				map.put("xianbck",bean.getXianbck()); //线边仓库
				map.put("dingdsxsj", ziywjsj.substring(0,10));
				BigDecimal daixh = this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxjis.queryDaixh", map));//待消耗
				logger.info("待消耗:------"+daixh);
				
				//替代件待交付
				map.put("cangkbh", bean.getXianbck());				
				BigDecimal tidjdjf = BigDecimal.ZERO;
				if(uset.getC1().equals(mos)){
					tidjdjf = this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxjis.queryTidjdjfC1", map));
				}else{					
					tidjdjf = this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxjis.queryTidjdjfM1", map));
				}
				logger.info("替代件待交付:------"+tidjdjf);
				
				//C1M1模式资源=库存数量 + 未交付 + 替代件数量 +替代件待交付- 安全库存(零件仓库) - 待消耗
				ziy = ziykzb.getKucsl().add(weijfzl).add(tidjzl).add(tidjdjf).subtract(daixh)
					.subtract(this.getBigDecimalAx(bean.getAnqkcsl()));
				weijfzlbj = weijfzl.add(this.getBigDecimalAx(bean.getCkxittzz()));
				logger.info("库存数量:"+ziykzb.getKucsl()+"零件:"+bean.getLingjbh()+"线边库:"+bean.getXianbck()+"未交付:"+weijfzlbj+"替代件:"+tidjzl+"待消耗:"+daixh+"系统调整值"+bean.getCkxittzz()
						+"安全库存:"+bean.getAnqkcsl()+"初始资源:"+ziy);
				
			}else{//没有设置或者设置为2，按盈余计算	
				//查询库存(资源快照)
				Ziykzb ziykzb = xinaxjis.queryZiykzb(bean.getUsercenter(),bean.getLingjbh(),bean.getXianbck(), dateNow);
				
				//C1M1模式:未交付=已发要货总量 – 交付总量 – 终止总量(零件仓库)
				weijfzl = CommonFun.getBigDecimal(bean.getCkyifyhlzl()).subtract(CommonFun.getBigDecimal(bean.getCkjiaofzl()))
					.subtract(CommonFun.getBigDecimal(bean.getCkzhongzzl())); 
				
				logger.info("线边库存："+ziykzb.getKucsl());
								
				//查询线边库存，只做插入操作，不进行计算
				Xianbkc xianbkc = xinaxjis.queryXianbkc(bean.getUsercenter(),bean.getLingjbh(),bean.getShengcxbh(),bean.getXianbck(),"1",bean.getDanw());
								
				//map.put("xiaohdbh", bean.getXiaohd());	
				BigDecimal anqkcs = this.getBigDecimalAx(bean.getAnqkcsl());
				//BigDecimal anqkcs = this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxjis.queryAnqkcByXiaohd", map));			
				logger.info("安全库存："+anqkcs);
					
				//净资源=库存数量+未交付+仓库系统调整值-安全库存
				ziy = ziykzb.getKucsl().add(weijfzl).add(this.getBigDecimalAx(bean.getCkxittzz())).subtract(anqkcs);
				logger.info("资源="+ziy+"系统调整值："+bean.getCkxittzz()+"未交付:"+weijfzl+"线边库存："+ziykzb.getKucsl()+"安全库存:"+anqkcs );
			}
		
		}else{
			//CDMD模式:未交付=已发要货总量 – 交付总量 – 终止总量(零件消耗点)
			weijfzl = CommonFun.getBigDecimal(bean.getYifyhlzl()).subtract(CommonFun.getBigDecimal(bean.getJiaofzl()))
			.subtract(CommonFun.getBigDecimal(bean.getZhongzzl()));
			logger.info("未交付："+weijfzl );
			//初始化线边库存
			map.put("usercenter", bean.getUsercenter());//用户中心
			map.put("lingjbh", bean.getLingjbh());//零件编号
			map.put("xiaohdbh", bean.getXiaohd());//消耗点编号
			//查询初始化资源表
			Chushzyb chushzyb = (Chushzyb)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryOneChushzyb", map);
			if(chushzyb == null){
				xinaxjis.saveYicbj(bean, user, "初始化资源表为空",mos,bean.getJianglms(),uset);
				return null;
			}
			BigDecimal chushxbkc = this.getBigDecimalAx(chushzyb.getXianbkc()).add(this.getBigDecimalAx(chushzyb.getSppvxhl()));
			
			
			//查询线边库存，只做插入操作，不进行计算
			Xianbkc xianbkc = xinaxjis.queryXianbkc(bean.getUsercenter(),bean.getLingjbh(),bean.getShengcxbh(),bean.getXiaohd(),"2",bean.getDanw());
						
			map.put("xiaohdbh", bean.getXiaohd());			
			BigDecimal anqkcs = this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxjis.queryAnqkcByXiaohd", map));
			logger.info("安全库存："+anqkcs);
			
			//净资源=初始化线边库存+未交付+零件消耗点系统调整值-安全库存
			ziy = chushxbkc.add(weijfzl).add(this.getBigDecimalAx(bean.getCkxittzz())).subtract(anqkcs);
			logger.info("资源="+ziy+"系统调整值："+bean.getCkxittzz()+"未交付:"+weijfzl+"初始化线边库存："+chushxbkc+"安全库存:"+anqkcs );
		}
		//如果未交付为负数,保存异常报警
		if(weijfzlbj.signum() < 0){
			xinaxjis.saveYicbj(bean, user, "未交付数量为负数:"+weijfzlbj,mos,uset);
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
			zickbh = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxChushJis.selZickbh", map));
		}else{
			zickbh = bean.getZickbh();
		}
		map.put("xiehztbh", bean.getXiehztbh().substring(0,4));//卸货站台编组
		map.put("cangkbh", bean.getXianbck()+zickbh);//仓库编号
		map.put("mos", mos);//模式
		Xiehztxhsj xiehztxhsj = (Xiehztxhsj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxChushJis.queryXiehztxhsjObject", map);
		if(xiehztxhsj == null){
			return null;
		}
		return xiehztxhsj.getBeihsj();
	}	

	/**
	 * 调用webservice接口获取整车序号
	 * @param param
	 * @param urlpath
	 * @author zbb
	 * @return
	 */
	public String queryZhengcxh(Anxjscszjb anxjscszjb,String liush){
		String urlpath="";
		if("UL".equals(anxjscszjb.getUsercenter())||"UX".equals(anxjscszjb.getUsercenter())){			
			urlpath= "urlPathUL";
		}else if("UW".equals(anxjscszjb.getUsercenter())){
			urlpath= "urlPathUW";			
		}
		else if("VD".equals(anxjscszjb.getUsercenter())){//添加VDwebservie 调用 
			urlpath= "urlPathVD";			
		}
		String urlPathAddress = LoaderProperties.getPropertiesMap(fileName).get(urlpath);
		ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
		factory.setServiceClass(XinaxCshWebservice.class);
		factory.setAddress(urlPathAddress+"xinaxCshWebservice");
		XinaxCshWebservice client = (XinaxCshWebservice) factory.create();
		String zhengcxh = client.queryZhengcxh(anxjscszjb,liush);    
		return this.strNullAx(zhengcxh);
	}
	
	/**
	 * 调用webservice接口获取sppv消耗量
	 * @param param
	 * @param urlpath
	 * @author zbb
	 * @return
	 */
	public BigDecimal querySppvxhl(Anxjscszjb anxjscszjb,String kaislsh,String zhengcxh,String tangc){
		String urlpath="";
		if("UL".equals(anxjscszjb.getUsercenter())||"UX".equals(anxjscszjb.getUsercenter())){			
			urlpath= "urlPathUL";
		}else if("UW".equals(anxjscszjb.getUsercenter())){
			urlpath= "urlPathUW";			
		}else if("VD".equals(anxjscszjb.getUsercenter())){//添加VDwebservie 调用 
			urlpath= "urlPathVD";			
		}
		String urlPathAddress = LoaderProperties.getPropertiesMap(fileName).get(urlpath);
		ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
		factory.setServiceClass(XinaxCshWebservice.class);
		factory.setAddress(urlPathAddress+"xinaxCshWebservice");
		XinaxCshWebservice client = (XinaxCshWebservice) factory.create();
		BigDecimal sppvxhl = client.querySppvxhl(anxjscszjb,kaislsh,zhengcxh,tangc);    
		return sppvxhl;
	}
	/**
	 * 调用webservice接口，查询小火车消耗点模板表
	 * @param param	 
	 * @author zbb
	 * @return
	 */
	public Xiaohcxhdmb queryXiaohcxhdmb(Anxjscszjb anxjscszjb,String zhengcxh){
		String urlpath="";
		if("UL".equals(anxjscszjb.getUsercenter())||"UX".equals(anxjscszjb.getUsercenter())){			
			urlpath= "urlPathUL";
		}else if("UW".equals(anxjscszjb.getUsercenter())){
			urlpath= "urlPathUW";			
		}else if("VD".equals(anxjscszjb.getUsercenter())){//添加VDwebservie 调用 
			urlpath= "urlPathVD";			
		}
		String urlPathAddress = LoaderProperties.getPropertiesMap(fileName).get(urlpath);
		ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
		factory.setServiceClass(XinaxCshWebservice.class);
		factory.setAddress(urlPathAddress+"xinaxCshWebservice");
		XinaxCshWebservice client = (XinaxCshWebservice) factory.create();
		Xiaohcxhdmb xiaohcxhdmb = client.queryXiaohcxhdmb(anxjscszjb,zhengcxh);    
		return xiaohcxhdmb;
	}	
	/**
	 * 根据用户中心查询该用户中心对应的模式名
	 * @author zbb
	 * @param usercenter 
	 * @return list 模式列表
	 */
	public List queryMosbyUserCenter(String usercenter){
		Map map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		List mosList = new ArrayList<HashMap<String,String>>();		
		UsercenterSet userset = (UsercenterSet) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxjis.queryUsercenterSet", map);
		map.remove("usercenter");
		if(userset!=null){
			map.put("value", 2);
			map.put("name", userset.getC1()+"/"+userset.getM1());
			mosList.add(map);
			if(userset.getMD()!=null&&!"".equals(userset.getMD()))
			{
				map = new HashMap<String,String>();
				map.put("value", 1);
				map.put("name", userset.getCD()+"/"+userset.getMD());
				mosList.add(map);
			}
		}
		return mosList;
	}
	public String strNullAx(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}
	public BigDecimal getBigDecimalAx(Object obj) {
		if (obj != null) {
			return new BigDecimal(this.strNullAx(obj));
		} else {
			return BigDecimal.ZERO;
		}
	}
}
