package com.athena.xqjs.module.ilorder.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiaohcyssk;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.module.anxorder.service.AnxMaoxqService;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.common.service.GongysService;
import com.athena.xqjs.module.common.service.LingjGongysService;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.common.service.WulljService;
import com.athena.xqjs.module.common.service.XqjsLingjckService;
import com.athena.xqjs.module.ilorder.service.AnxlsOrderWebEeService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.DingdmxService;
import com.athena.xqjs.module.ilorder.service.IleditAndEffectService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：AnxlsOrderWebEeAction
 * <p>
 * 类描述： 按需临时订单明细页面添加明细
 * </p>
 * 创建人：CSY
 * <p>
 * 创建时间：2017-04-01
 * </p>
 * 
 * @version
 * 
 */
@Component
public class AnxlsOrderWebEeAction extends BaseWtcAction {
	
	@Inject
	private GongysService gService;//供应商
	@Inject
	private LingjGongysService lingjGongysService;//零件供应商
	@Inject
	private LingjService lingjService;//零件
	@Inject
	private DingdmxService dingdmxService;//订单明细
	@Inject
	private DingdService dingdService;//订单
	@Inject
	private LingjxhdService lingjxhdService;//零件消耗点
	@Inject
	private WulljService wulljService;//物流路径
	@Inject
	private XqjsLingjckService ljck;//零件仓库
	@Inject
	private AnxMaoxqService anxMaoxqService;//按需毛需求
	@Inject
	private IleditAndEffectService ileditAndEffectService;
	@Inject
	private AnxlsOrderWebEeService anxlsOrderService;//临时订单
	
	private final Logger log = Logger.getLogger(AnxlsOrderWebEeAction.class);

	//按需临时订单增加订单明细
	public String addAnxlsddMx(@Param Dingdmx bean) throws ParseException {
		String newEditor = getUserInfo().getUsername();
		String editTime = CommonFun.getJavaTime();
		bean.setActive("1");
		String result = MessageConst.INSERT_COUNT_0;
		bean.setCreator(getUserInfo().getUsername());
		bean.setEditor(getUserInfo().getUsername());
		bean.setCreate_time(CommonFun.getJavaTime());
		bean.setEdit_time(CommonFun.getJavaTime());
		if (!bean.getGonghlx().equals("M1") && !bean.getGonghlx().equals("MD") 
				&& !bean.getGonghlx().equals("MJ") && !bean.getGonghlx().equals("MV")) {
			Gongys gys = gService.queryObject(bean.getGongysdm(), bean.getUsercenter());
			bean.setGongyslx(gys.getLeix());
			bean.setGongsmc(gys.getGongsmc());
			bean.setNeibyhzx(gys.getUsercenter());
			LingjGongys ljgys = lingjGongysService.select(bean.getUsercenter(), bean.getGongysdm(), bean.getLingjbh());
			bean.setGongyfe(ljgys.getGongyfe());
			bean.setUabzlx(ljgys.getUabzlx());
			bean.setUabzucsl(ljgys.getUaucgs());
			bean.setUabzuclx(ljgys.getUcbzlx());
			bean.setUabzucrl(ljgys.getUcrl());
			// xh 10607 临时订单增加明细时判断包装信息是否存在
			if(StringUtils.isBlank(ljgys.getUabzlx())
					|| StringUtils.isBlank(ljgys.getUcbzlx())
					|| null == ljgys.getUaucgs()
					|| null == ljgys.getUcrl()
			)
			{
				setResult("errorMsg", "用户中心" + bean.getUsercenter() + "零件号" + bean.getLingjbh() + "零件包装信息不完整(UA包装类型,UC包装类型,UC容量,UAUC个数)!");
				return AJAX;
			}
		}
		bean.setZhuangt(Const.DINGD_STATUS_ZZZ);
		if (bean.getGonghlx().indexOf("M") != -1 || bean.getGonghlx().indexOf("C") != -1) {
			String flag = this.setAxsj(bean);
			if (!StringUtils.isEmpty(flag)) {
				setResult("errorMsg", flag);
				return AJAX;
			}
///////////针对按需临时订单进行修改，修改内容为利用WTC服务，将订单明细信息传入执行层，查看该信息是否满足执行层要货令的拆分条件		
			Map<String, Object> wtcMap = new HashMap<String, Object>();
			wtcMap.put("usercneter", bean.getUsercenter());
			wtcMap.put("lingjbh", bean.getLingjbh());
			wtcMap.put("gongysdm", bean.getGongysdm());
			wtcMap.put("gonghms", bean.getGonghlx());
			wtcMap.put("xiaohd", bean.getXiaohd());
			//MD M1  仓库 = bean.getGongysdm  zickbh =  bean.getCangkdm()  CD MD 仓库 = bean.getCangkdm() zickbh =  bean.getCangkdm()
			if(bean.getGonghlx().indexOf("M") != -1)
			{
				wtcMap.put("cangkbh", bean.getGongysdm());
			}
			else
			{
				wtcMap.put("cangkbh", bean.getCangkdm());
			}
			wtcMap.put("zickbh", bean.getCangkdm());
			wtcMap.put("xiehzt", bean.getXiehzt());
			try
			{
				Map<String, String> errorMap = new HashMap<String, String>();
				errorMap.put("C_Y00001", "执行层参考系-零件关系缺失");
				errorMap.put("C_Y00002", "执行层参考系-供应商关系缺失");
				errorMap.put("C_Y00003", "执行层参考系-零件供应商关系缺失");
				errorMap.put("C_Y00004", "执行层参考系-物流路径关系缺失");
				errorMap.put("C_Y00005", "执行层参考系-零件仓库关系缺失");
				errorMap.put("C_Y00006", "执行层参考系-零件仓库关系缺失");
				errorMap.put("C_Y00007", "执行层参考系-卸货站台关系缺失");
			
				WtcResponse wtcResponse = callWtc(bean.getUsercenter(), "Y0201", wtcMap);
				
				if (!wtcResponse.get("response").equals(Const.WTC_SUSSCESS)) {
					if(errorMap.containsKey(wtcResponse.get("response"))){
						setResult("errorMsg", errorMap.get(wtcResponse.get("response")) + ":零件号" + bean.getLingjbh()  
								+ "未能满足执行层拆分要货令条件,该零件部分信息可能于当天维护,请于明天操作!");
					}else{
						setResult("errorMsg", "未知原因:零件号" + bean.getLingjbh() 
								+ "未能满足执行层拆分要货令条件,该零件部分信息可能于当天维护,请于明天操作!");
					}
					return AJAX;
				}
			}
			catch (Exception e) {
				setResult("errorMsg", "调用WTC服务失败,请联系管理人员!");
				e.printStackTrace();
				log.error("调用WTC服务失败", e);
				return AJAX;
			}
			
///////////针对按需临时订单进行修改，修改内容为利用WTC服务，将订单明细信息传入执行层，查看该信息是否满足执行层要货令的拆分条件	
		}
		// 订货车间
		Lingj lingj = lingjService.select(bean.getUsercenter(), bean.getLingjbh());
		bean.setDinghcj(lingj == null ? null : lingj.getDinghcj());
		bean.setLingjmc(lingj == null ? null : lingj.getZhongwmc());
		bean.setJihyz(lingj == null ? null : lingj.getJihy());
		bean.setJissl(bean.getShul());
		if (bean.getXiaohsj() != null || dingdmxService.queryListMx(getParams()).size() == 0) {
			//判断订单状态
			int zhuangt=dingdService.queryDingdzt(bean.getDingdh());
			if(zhuangt ==-99){
				setResult("errorMsg", "该订单不存在或状态有误，请查证！");
			   return AJAX;
			}else if(zhuangt >3){
				setResult("errorMsg", "只能对已定义、制作中、待生效、拒绝的订单进行操作！请检查订单状态！");
			   return AJAX;
			 }
			this.dingdmxService.doInsert(bean);
			result = "新增成功！";
			//按需订单明细添加时修改订单零件P0数量   
			if ( !("temp").equalsIgnoreCase(getParam("lx")) && (bean.getGonghlx().indexOf("M") != -1 || bean.getGonghlx().indexOf("C") != -1)) {
				this.sumSlToPn(newEditor, editTime);
			}
			
			this.dingdService.updateDingdzt(bean.getDingdh());
			setResult("msg", result);
			log.debug("========addAnxlsddMx 新增成功=========");
		} else {
			log.debug("========addAnxlsddMx 新增失败=========");
			setResult("errorMsg", result);
		}
		return AJAX;
	}
	
	
	/******************************************************************************/

	
	// 临时订单按需，根据消耗时间推算出预计交付时间和预计上线时间
	public String setAxsj(Dingdmx bean) throws ParseException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		String beihsj = null;
		//先创建所需参考系类
		Lingjxhd lingjxhd = null;	//零件消耗点
		Wullj wullj = null;			//物流路径
		Xiehzt xiehzt = null;		//卸货站台
		
		if (bean.getGonghlx().equals("MD") || bean.getGonghlx().equals("CD")) {
			map.put("xiaohdbh", bean.getXiaohd());
			map.put("gongysbh", bean.getGongysdm());
			//根据用户中心、零件编号、消耗点编号查询出有效零件消耗点
			lingjxhd = lingjxhdService.queryLingjxhdObject(map);
			// 如果小火车时刻表没有数据,跳过该条记录,不予计算
			if (lingjxhd == null) {
				return "未找到该消耗点";
			}
			if(StringUtils.isEmpty(lingjxhd.getXiaohcbh())){
				return "小火车编号为空";
			}
			//mantis:0013505
			bean.setFenpxh(bean.getXiaohd().substring(0, 5));
			bean.setXianbyhlx(lingjxhd.getXianbyhlx());
			bean.setXiaohch(lingjxhd.getXiaohcbh());
			bean.setChanx(lingjxhd.getShengcxbh());
		} else {
			beihsj = bean.getXiaohsj();
		}
		// 预计到货时间(yujdhsj)=小火车备货时间-内部物流时间
		
		//这里就先只查询物流路径 ，获取对应物流路径和内部物流时间
		String gcbh = null;
		//根据用户中心、零件编号、供应商编号、生产线编号、分配区、外部模式查询出有效物流路径
		if (bean.getGonghlx().equals("CD")) {
			map.put("fenpqh", bean.getXiaohd().substring(0, 5));
			map.put("waibms", bean.getGonghlx());
			wullj= wulljService.queryWulljObject(map);
			gcbh = wullj == null ? null : wullj.getGcbh();
			bean.setCangkdm(wullj == null ? null : wullj.getMudd());
			bean.setBeihsj2(wullj == null ? null : wullj.getBeihsj());//备货时间
		} else if (bean.getGonghlx().equals("C1") || bean.getGonghlx().equals("CV")) {
			map.put("gongysbh", bean.getGongysdm());
			map.put("waibms", bean.getGonghlx());
			map.put("mudd", bean.getCangkdm());
			wullj = wulljService.queryWulljObjectLsaxc1(map);
			gcbh = wullj == null ? null : wullj.getGcbh();
			if(wullj!=null && wullj.getXianbck()!=null && wullj.getXianbck().length()>0){
				map.put("shengcxbh", wullj.getXianbck());
			}
		}else if(bean.getGonghlx().equals("M1") || bean.getGonghlx().equals("MJ") || bean.getGonghlx().equals("MV")){
			map.put("mos2", bean.getGonghlx());
			map.put("dinghck", bean.getGongysdm());
			map.put("xianbck", bean.getCangkdm());
			map.put("cangkbh", bean.getCangkdm());
			Lingjck ck = ljck.querySingle(map);
			wullj= wulljService.queryWulljObjectLsaxck(map);
			gcbh = bean.getGongysdm();
			bean.setUsbaozlx(ck == null ? null : ck.getUsbzlx());
			bean.setUsbaozrl(ck == null ? null : ck.getUsbzrl());
			if(wullj!=null && wullj.getXianbck()!=null && wullj.getXianbck().length()>0){
				map.put("shengcxbh", wullj.getXianbck());
			}
		}else if(bean.getGonghlx().equals("MD")){
			map.put("mos", "MD");
			map.put("dinghck", bean.getGongysdm());
			map.put("fenpqh", bean.getXiaohd().substring(0, 5));
			wullj = wulljService.queryWulljObjectLsaxck(map);
			bean.setBeihsj2(wullj == null ? null : wullj.getBeihsj());//备货时间
			map.put("cangkbh", wullj == null ? " " : wullj.getXianbck());
			Lingjck ck = ljck.querySingle(map);
			bean.setUabzuclx(ck == null ? null : ck.getUclx());
			bean.setUabzucrl(ck == null ? null : ck.getUcrl());
			bean.setCangkdm(wullj == null ? null : wullj.getXianbck());
			gcbh = bean.getGongysdm();
		}
		if (wullj == null) {
			return "未在物流路径中找到相关记录.";
		}
		
		bean.setGcbh(gcbh);
		bean.setXiehzt(wullj.getXiehztbh());
		bean.setLujdm(wullj.getLujbh());
		bean.setLeix("9");

		if (bean.getGonghlx().equals("MD") || bean.getGonghlx().equals("CD")) {
			BigDecimal neibwlsj = wullj.getBeihsjc();
			// 如果内部物流时间C为空
			if (neibwlsj == null) {
				return "内部物流时间C为空";
			}
			//根据用户中心、物流路径的卸货站台编号查询出有效卸货站台
			map.put("usercenter", bean.getUsercenter());
			map.put("xiehztbh", wullj.getXiehztbh());
			xiehzt = anxMaoxqService.queryXiehztObject(map);
			BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
			if (yunxtqdhsj == null) {
				return "卸货站台允许提前到货时间";
			}
			//到这一步，所有所需参考系类（lingjxhd, wullj, xiehzt）已经获取完毕，可以开始计算时间
			
			//1. 根据提前到货时间和最晚到货时间向前推工作时间模板，获取最早到货时间
			//最晚到货时间现在取页面填的预计交付时间
			//预计到货时间=时间(CDMD为小火车备货时间,C1M1为消耗时间)-内部物流时间
			String zuiwdhsj = bean.getZuiwdhsj();
			map.put("juedsk", zuiwdhsj);
			map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));//允许提前到货时间
			map.put("shengcxbh", lingjxhd.getShengcxbh());
			// 最早到货时间
			String zuizdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));//向前 推工作时间模板
			if (zuizdhsj.length()<16) {
				return "找不到有效的工作时间模板";
			}
			bean.setZuizdhsj(zuizdhsj);
			
			//2. 从物流路径获取内部物流时间，再根据最晚到货时间和内部物流时间从工作时间模板向后推算出备货时间
			map.put("juedsk", zuiwdhsj);//最晚到货时间
			map.put("shijNum", CommonFun.strNull(neibwlsj.intValue()));//内部物流时间
			//推算备货时间（用户中心、生产线、最晚到货时间）
			beihsj = CommonFun.strNull(anxlsOrderService.queryGongzsjmbH(map));//向后推工作时间模板
			if (beihsj.length()<16) {
				return "找不到有效的工作时间模板";
			}
			map.put("gcbh", gcbh);
			map.put("daohsj", zuiwdhsj);
			map.put("xiehztbh", wullj.getXiehztbh().substring(0,4));
			
			//根据备货时间获取上线时间和消耗时间（消耗时间取上线时间）
			map.put("xiaohcbh", lingjxhd.getXiaohcbh());
			
			//消耗时间和备货时间现在应该是最后推算的
			map.put("kaisbhsj", beihsj.substring(0, 16));
			// 取小火车运输时刻
			//需要新增一个sql，现在的小火车运输时刻是向前推，需要改为向后推
			List<Xiaohcyssk> Listxhcyssk = anxlsOrderService.queryXiaohcysskObjectByShangxH(map);
			
			// 如果小火车时刻表没有数据,跳过该条记录,不予计算
			if (Listxhcyssk == null || Listxhcyssk.isEmpty()) {
				return "未查到相关小火车信息";
			}
			// 如果小火车时刻表数据不足,跳过该条记录,不予计算
			if (Listxhcyssk.size()<2) {
				return "相关小火车信息数量不足";
			}
			Xiaohcyssk xiaohcyssk = Listxhcyssk.get(1);
			// 上线时间
			String shangxsj = xiaohcyssk.getChufsxsj();
			//备货时间
			beihsj = xiaohcyssk.getKaisbhsj();
			
			if (beihsj == null || beihsj.equals("")) {
				return "小火车运输时刻,小火车编号" + lingjxhd.getXiaohcbh() + "生产线编号" + lingjxhd.getShengcxbh() + "备货为空";
			}
			if (shangxsj == null || shangxsj.equals("")) {
				return "小火车运输时刻,小火车编号" + lingjxhd.getXiaohcbh() + "生产线编号" + lingjxhd.getShengcxbh() + "上线时间为空";
			}
			bean.setXiaohcsxsj(shangxsj.substring(0, 16));
			bean.setXiaohcbhsj(beihsj);
			bean.setTangc(xiaohcyssk.getTangc());//小火车运输时刻趟次
			bean.setXiaohch(lingjxhd.getXiaohcbh());//小火车编号
			bean.setXiaohccxh(lingjxhd.getXiaohccxbh());//小火车车厢编号
			
			bean.setXiaohsj(shangxsj);
		}else{//如果是C1/CV/M1/MV模式
			map.put("usercenter", bean.getUsercenter());
			map.put("xiehztbh", wullj.getXiehztbh());
			map.put("gcbh", gcbh);
			/**
			 * 从卸货站台得到提前到货时间
			 */
			xiehzt = anxMaoxqService.queryXiehztObject(map);
			BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
			if (yunxtqdhsj == null) {
				return "卸货站台允许提前到货时间";
			}
			//预计到货时间=时间(CDMD为小火车备货时间,C1M1为消耗时间)-内部物流时间
			map.put("juedsk", bean.getZuiwdhsj());
			map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));//允许提前到货时间
	
			// 最早到货时间
			String zuizdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));
			if (zuizdhsj.length()<16) {
				return "找不到有效的工作时间模板";
			}
			bean.setZuizdhsj(zuizdhsj);
		}
		//M1模式保存小火车出发上线时间 = 最晚到货时间
		if(bean.getGonghlx().equals("M1") || bean.getGonghlx().equals("MJ") || bean.getGonghlx().equals("MV")){
			bean.setXiaohcsxsj(bean.getZuiwdhsj());
		}
		/**
		 * M1MD模式计算发运时间
		 */
		if (bean.getGonghlx().equalsIgnoreCase(Const.ANX_MS_M1) || bean.getGonghlx().equalsIgnoreCase(Const.ANX_MS_MD)
				|| bean.getGonghlx().equalsIgnoreCase(Const.MJ) || bean.getGonghlx().equalsIgnoreCase(Const.MV)) {
			Date date = CommonFun.yyyyMMddHHmm.parse(bean.getZuiwdhsj());//设置为最晚到货时间
			//发运时间 = 最晚到货时间 - 仓库送货时间2
			date.setTime(date.getTime() - CommonFun.getBigDecimal(wullj.getCangkshsj2()).multiply(new BigDecimal(24))
					.multiply(new BigDecimal(60)).multiply(new BigDecimal(60)).multiply(new BigDecimal(1000)).intValue());
			bean.setFaysj(CommonFun.yyyyMMddHHmm.format(date));
		}else{//C1CD模式发运时间= 最晚到货时间 - 运输周期
			Date date = CommonFun.yyyyMMddHHmm.parse(bean.getZuiwdhsj());//设置为最晚到货时间
			//发运时间 = 最晚到货时间 - 运输周期
			date.setTime(date.getTime() - CommonFun.getBigDecimal(wullj.getYunszq()).multiply(new BigDecimal(24))
					.multiply(new BigDecimal(60)).multiply(new BigDecimal(60)).multiply(new BigDecimal(1000)).intValue());
			bean.setFaysj(CommonFun.yyyyMMddHHmm.format(date));
		}
		bean.setJiaofrq(bean.getZuiwdhsj());
		return "";
	}
	
	
	/******************************************************************************/
	
	
	//按需订单明细添加时修改订单零件P0数量   wuyichao修改
	public void sumSlToPn(String newEditor, String newEditTime) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", getParam("usercenter"));
		map.put("lingjbh", getParam("lingjbh"));
		map.put("dingdh", getParam("dingdh"));
		map.put("cangkdm", getParam("cangkdm"));
		map.put("gonghlx", getParam("gonghlx"));
		String prq = getParam("prq");
		if(StringUtils.isNotBlank(prq))
		{
			int index = prq.indexOf("_");
			String prqfwK = prq.substring(0, index);
			String prqfwJ = prq.substring(index + 1);
			String rq = "to_char(jiaofrq,'yyyy-mm-dd') between '" + prqfwK + "' and  '" + prqfwJ + "'";
			map.put("RQ", rq);
		}
		Map<String, Object> sumLj = ileditAndEffectService.sumDdmxToLj(map);
		BigDecimal shul = BigDecimal.ZERO;
		if (sumLj == null) {
			sumLj = new HashMap<String, Object>();
		} else {
			shul = (BigDecimal) sumLj.get("SHUL");
		}
		sumLj.put((getParam("pn") + "sl").toLowerCase(), shul);
		sumLj.put("usercenter", getParam("usercenter"));
		sumLj.put("lingjbh", getParam("lingjbh"));
		sumLj.put("dingdh", getParam("dingdh"));
		sumLj.put("gongysdm", getParam("gongysdm"));
		sumLj.put("cangkdm", getParam("cangkdm"));
		sumLj.put("gonghms", getParam("gonghms"));
		ileditAndEffectService.updateDdljSl(sumLj, newEditor, newEditTime);
	}
	
	
	/******************************************************************************/
	
	
	/**
	 * getUserInfo获取用户信息方法
	 * 
	 * @return object userInfo
	 */
	public LoginUser getUserInfo() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}
	
}
