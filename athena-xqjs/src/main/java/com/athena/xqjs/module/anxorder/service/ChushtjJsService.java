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

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.anxorder.Anxcshbxzjb;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.entity.anxorder.Chushzyb;
import com.athena.xqjs.entity.anxorder.Kucjscsb;
import com.athena.xqjs.entity.anxorder.Ticxxsj;
import com.athena.xqjs.entity.anxorder.XbpdBean;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.common.Cangkxhsj;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Xiaohcyssk;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.common.Xiehztxhsj;
import com.athena.xqjs.entity.common.Yunssk;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.GongysService;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.common.service.XiaohcysskService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.DingdljService;
import com.athena.xqjs.module.ilorder.service.DingdmxService;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * Title:初始化提交计算service
 * 
 * @author 李智
 * @version v1.0
 * @date 2012-03-28
 */
@Component
public class ChushtjJsService extends BaseService {
	
	public static final Logger logger = Logger.getLogger(ChushtjJsService.class);
	
	@Inject
	// 按需初始化布线中间表service
	private AnxcshbxzjbService anxcshbxzjbService;
	@Inject
	// 初始化资源表service
	private ChushzybService chushzybService;
	// IL订单service
	@Inject
	private IlOrderService ilOrderService;
	// 订单service
	@Inject
	private DingdService dingdService;
	// 供应商service
	@Inject
	private GongysService gongysService;
	// 按需毛需求service
	@Inject
	private AnxMaoxqService anxMaoxqService;
	//零件service
	@Inject
	private LingjService lingjService;
	//订单service
	@Inject
	private DingdmxService dingdmxService;
	//小火车运输时刻service
	@Inject
	private XiaohcysskService xiaohcysskService;
	//按需计算
	@Inject
	private AnxOrderService anxOrderService;
	//订单零件service
	@Inject
	private DingdljService dingdljService;
	@Inject
	// 零件消耗点service
	private LingjxhdService lingjxhdService;
	@Inject
	// 库存计算service
	private KucjscsbService kucjscsbService;
	@Inject
	// 异常报警service
	private YicbjService yicbjService;


	/**
	 * 按需cd md模式的的初始化计算
	 * 
	 * @author 李智
	 * @version v1.0
	 * @throws Exception 
	 * @date 2012-4-11
	 * 
	 */
	public void calculateForcdmd(Maoxq maoxq, LoginUser loginUser,String zhizlx,String dingdjssj) throws Exception {
		String shangcjssj = dingdmxService.getShangcjssj();
		this.calculateMain(maoxq, loginUser, "1", zhizlx,dingdjssj,shangcjssj);
		updateXianbkc(loginUser, dingdjssj,shangcjssj);
	}
	
	public boolean lshcheckQuery(Map<String, String> map) {
		boolean flag = true;
		int count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryCDClsh", map);
		if (count == 0)
			flag = false;
		return flag;
	}

	public String pdjsdxh(Map<String, String> map) {
		// 上次计算时间
		String shangcjssj = map.get("scjssj");
		// 流水点消耗时间
		String xhsj = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryCDCMaxXhsj", map);
		if (xhsj == null) {
			return "0";
		}
		// 判断待消耗时间是在计算时间之前还是之后
		String zfh = "";
		if (shangcjssj.equals("")) {
			shangcjssj = CommonFun.getJavaTime("yyyy-MM-dd");
		}
		if (xhsj.compareTo(shangcjssj) >= 0) {
			zfh = "-";
			map.put("jiessj", xhsj);
			map.put("kaissj", shangcjssj);
		} else {
			zfh = "+";
			map.put("jiessj", shangcjssj);
			map.put("kaissj", xhsj);
		}
		// 查询两个时间点之间的待消耗
		map.remove("zhongzlxh");
		BigDecimal kucslDxh = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryDxh", map));
		String dxh = kucslDxh.equals(BigDecimal.ZERO) ? String.valueOf(kucslDxh) : zfh + kucslDxh;
		return dxh;
	}

	/**
	 * 保存异常待消耗
	 * @param params
	 */
	public void addYicdxh(ArrayList<Kucjscsb> params,String user){
		//创建时间
		String create_time = CommonFun.getJavaTime("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < params.size(); i++) {
			Kucjscsb kucjscsb = params.get(i);
			kucjscsb.setCreator(user);//创建人
			kucjscsb.setJilrq(create_time.substring(0,10));//记录日期
			kucjscsb.setCreate_time(create_time);//创建时间
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("anx.addYicdxh", params);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> xbpdxzQuery(Maoxq maoxq, Map<String, String> map) {
		// 查询物流路径CD/MD/C0模式=用户中心、消耗点、产线、零件号、线边仓库、线边理论库存
		Map<String, Object> mapRes = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("anx.queryXBPD", map, maoxq);
		//判断用户中心订单号
		if(Const.WTC_CENTER_UW.equals(map.get("usercenter"))){
			map.put("dingdh", Const.ANX_UW_DINGDH);
		}else{
			map.put("dingdh", Const.ANX_UL_DINGDH);
		}
		// 按需上次计算时间7/8
		Dingd dingd =  (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("calendarVersion.queryAxScjssj",map);
		List<XbpdBean> ls = (List<XbpdBean>) mapRes.get("rows");
		for (int i = 0; i < ls.size(); i++) {
			ls.get(i).setScjssj(dingd.getDingdsxsj());
		}
		mapRes.put("rows", ls);
		return mapRes;
	}

	/**
	 * 更新线边库存
	 * @param user 用户信息
	 */
	public void updateXianbkc(LoginUser user,String dingdjssj,String shangcjssj){
		logger.info("按需初始化计算初始化计算更新线边理论库存");
		Map<String,String> map = new HashMap<String,String>();
		// 更新的CD,MD的线边理论库存
		List<Anxjscszjb> listAnxjscszjb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryCshLjxhd");
		for (int i = 0; i < listAnxjscszjb.size(); i++) {
			Anxjscszjb anxjscszjb = listAnxjscszjb.get(i);
			//用户中心
			map.put("usercenter", anxjscszjb.getUsercenter());
			//零件编号
			map.put("lingjbh", anxjscszjb.getLingjbh());
			//生产线编号
			map.put("shengcxbh", anxjscszjb.getShengcxbh());
			//消耗点编号
			map.put("xiaohdbh", anxjscszjb.getXiaohd());
			map.put("xiaohd", anxjscszjb.getXiaohd());
			//查询初始化资源表
			Chushzyb chushzyb = chushzybService.queryOneChushzyb(map);
			if(null == chushzyb){
				continue;
			}
			BigDecimal kucslDxh = BigDecimal.ZERO;
			//计算时间
			String jn = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryMxqxhsj",map));
			if(!shangcjssj.equals("")){
				map.put("xiaohsj2", jn.substring(0, 16));
				map.put("xiaohsj", shangcjssj.substring(0, 16));
				kucslDxh = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryXiaohl", map));
			}
			//查询零件消耗点信息
			Lingjxhd lingjxhd = lingjxhdService.queryLingjxhdObject(map);
			//获取库存数量信息
			Kucjscsb kucsl = kucjscsbService.queryAllKuc(map);
			// 设置现编理论库存
			lingjxhd.setXianbllkc(CommonFun.getBigDecimal(chushzyb.getXianbkc()).subtract(CommonFun.getBigDecimal(kucsl.getDaohl()))
					.add(CommonFun.getBigDecimal(kucsl.getYicxhl())).add(kucslDxh));
			lingjxhd.setEditor(user.getUsername());// 后期需要获取当前登录信息，设置到其中
			lingjxhd.setEdit_time(CommonFun.getJavaTime());
			// 将得到的库存更新到零件消耗点中
			lingjxhdService.doUpdate(lingjxhd);
		}
	}
	
	/**
	 * 按需c1 m1模式的的初始化计算
	 * 
	 * @author 李智
	 * @version v1.0
	 * @throws Exception 
	 * @date 2012-4-11
	 * 
	 */
	public void calculateForc1m1(Maoxq maoxq, LoginUser loginUser, String zhizlx,String dingdjssj) throws Exception {
		String shangcjssj = dingdmxService.getShangcjssj();
		this.calculateMain(maoxq, loginUser, "2", zhizlx,dingdjssj,shangcjssj);
	}
	
	/**
	 * 校验数据
	 * @param obj 数据对象
	 * @return true:校验通过;false:校验不通过
	 */
	public boolean checkData(Anxcshbxzjb obj,LoginUser user){
		//错误详细信息
		StringBuilder cuowxxxx = new StringBuilder("");
		//已发要货总量为空
		if(obj.getYifyhlzl() == null){
			cuowxxxx.append("已发要货总量为空");
		//交付总量为空
		}else if(obj.getJiaofzl() == null){
			cuowxxxx.append("交付总量为空");
		//终止总量为空
		}else if(obj.getZhongzzl() == null){ 
			cuowxxxx.append("终止总量为空");
		}else if(obj.getXiaohcbh() == null || obj.getXiaohcbh().equals("")){
			cuowxxxx.append("小火车编号为空");
		}else if(obj.getBeihzq() == null || obj.getBeihzq().equals("")){
			cuowxxxx.append("备货周期为空");
		}else if(obj.getYunszq() == null || obj.getYunszq().equals("")){
			cuowxxxx.append("发运周期为空");
		}
		//C1/CD模式校验UA包装信息,M1/MD模式校验US包装信息
		if(CommonFun.strNull(obj.getJianglms()).equalsIgnoreCase(Const.ANX_MS_CD) || CommonFun.strNull(obj.getJianglms()).equalsIgnoreCase(Const.ANX_MS_C1)){
			if(obj.getGysucrl() == null || obj.getGysuaucgs() == null || CommonFun.strNull(obj.getGysuabzlx()).equals("") || CommonFun.strNull(obj.getGysucbzlx()).equals("")){
				cuowxxxx.append("UA包装信息为空");
			}
		}else if(CommonFun.strNull(obj.getJianglms2()).equalsIgnoreCase(Const.ANX_MS_CD)){
			if(obj.getCkusbzrl() == null || CommonFun.strNull(obj.getCkusbzlx()).equals("")){
				cuowxxxx.append("US包装信息为空");
			}
		}
		//如果错误详细信息不为空,则保存异常报警表,跳过本条数据.
		if(cuowxxxx.length() != 0){
			cuowxxxx.insert(0, "在供货模式%1下，零件%2-客户%3：");
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", obj.getLingjbh());
			lingjMap.put("usercenter", obj.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(obj.getMos());
			paramStr.add(obj.getLingjbh());
			paramStr.add(obj.getXiaohd());
			yicbjService.insertError(Const.YICHANG_LX2,cuowxxxx.toString(), lingj.getJihy(), 
					paramStr, obj.getUsercenter(), obj.getLingjbh(), user, Const.JISMK_ANX_CD);
			return false;
		}
		return true;
	}
	
	/**
	 * 按需的初始化计算主方法
	 * 
	 * @author 李智
	 * @version v1.0
	 * @throws Exception 
	 * @date 2012-3-23
	 * 
	 */
	public boolean calculateMain(Maoxq maoxq, LoginUser loginUser, String jisLx, String zhizlx,String dingdjssj,String shangcjssj) throws Exception{
		logger.info("按需初始化计算开始,计算类型"+jisLx+"制造路线"+zhizlx);
		// 中间表查出CD/MD模式的毛需求明细
		Map<String, String> param = new HashMap<String, String>();
		if(jisLx.equals("1")) {
			// 2种模式
			param.put("ms", "CD");
			param.put("ms2", "MD");
			param.put("type", "xiaohd");
		}
		else if(jisLx.equals("2")) {
			// 2种模式
			param.put("ms", "C1");
			param.put("ms2", "M1");
			param.put("type", "xianbck");
		}
		// 初始化中间表
		boolean boo = anxcshbxzjbService.executeAnxcshbxzjb(maoxq,param,jisLx,dingdjssj);
		logger.info("按需初始化计算初始化中间表完成"+boo);
		// 中间表初始化成功
		if (boo) {
			//根据模式查询零件,用户中心,消耗点
			List<Anxcshbxzjb> anxcshbxzjbs = anxcshbxzjbService
					.queryAnxcshbxzjbByMs(param);
			List<Dingdlj> list = new ArrayList<Dingdlj>();
			for (int i = 0; i < anxcshbxzjbs.size(); i++) {
				Anxcshbxzjb bean = anxcshbxzjbs.get(i);
				param.put("usercenter", bean.getUsercenter());
				param.put("lingjbh", bean.getLingjbh()); 
				
				// 从初始化资源表查找线边和继承的未交付量
				param.put("xiaohdbh", bean.getXiaohd());
				param.put("xianbck", bean.getXianbck());
				BigDecimal xianbkc = BigDecimal.ZERO;
				if(jisLx.equals("1")){
					param.put("xiaohd", bean.getXiaohd());
					Chushzyb chushzyb = chushzybService.queryOneChushzyb(param);
					if(chushzyb == null){
						Map lingjMap = new HashMap();
						lingjMap.put("lingjbh", bean.getLingjbh());
						lingjMap.put("usercenter", bean.getUsercenter());
						// 查询零件
						Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
						List<String> paramStr = new ArrayList<String>();
						paramStr.add(bean.getJianglms()+bean.getJianglms2());
						paramStr.add(bean.getLingjbh());
						paramStr.add(bean.getXiaohd());
						yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：初始化资源表为空", lingj.getJihy(), 
								paramStr, bean.getUsercenter(), bean.getLingjbh(), loginUser.getUsername(), Const.JISMK_ANX_CD);
						continue;
					}
					xianbkc = chushzyb.getXianbkc();
				}
				BigDecimal ziy = BigDecimal.ZERO;//资源
				// 查询全部信息
				List<Anxcshbxzjb> anxjscszjbList = null;
				if(jisLx.equals("1")){
					 anxjscszjbList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryAnxcshbxzjbMx",param);	
				}else{
					anxjscszjbList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryAnxcshbxzjbMxC1M1",param);	
				}
				// 查询全部信息
				//List<Anxcshbxzjb> anxjscszjbList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryAnxcshbxzjbMx",param);	
				for (int j = 0; j < anxjscszjbList.size(); j++) {
					Anxcshbxzjb anxcshbxzjb = anxjscszjbList.get(j);
					//校验数据,如果校验不通过,该条不计算
					if(!checkData(anxcshbxzjb,loginUser)){
						continue;
					}
					// 资源
					if(j == 0){
						BigDecimal anqkc = BigDecimal.ZERO;
						if(jisLx.equals("1")){//CDMD模式安全库存取零件消耗点
							anqkc = CommonFun.getBigDecimal(anxcshbxzjb.getAnqkcs());
						}else{//C1M1模式取零件仓库安全库存
							anqkc = CommonFun.getBigDecimal(anxcshbxzjb.getAnqkcsl());
						}
						ziy = getZiy(anxcshbxzjb,xianbkc).subtract(anqkc);
					}
					// 消耗量
					//BigDecimal xiaohl = getXiaohl(anxcshbxzjb);
					BigDecimal xiaohl = anxcshbxzjb.getShul();
					// 判断资源-消耗量
					ziy = ziy.subtract(xiaohl);
						
					// 资源小于0
					if (ziy.signum() < 0) {
						//要货量=把资源补为0
						BigDecimal yaohl = ziy.abs();
						//生成订单明细
						Map result = insertDingdmx(anxcshbxzjb, loginUser, maoxq, yaohl, zhizlx,dingdjssj,ziy);
						ziy = CommonFun.getBigDecimal(result.get("ziy"));
						Object dingdlj = result.get("dingdlj");
						if(dingdlj != null){
							list.add((Dingdlj) dingdlj);
						}
						int flag = (Integer) result.get("flag");
						if (flag < 0) {
							break;
							
						}else if(flag > 0){
							continue;
						}
					} 
				}
			}
			for (int i = 0; i < list.size(); i++) {
				Dingdlj dingdlj = list.get(i);
				dingdlj.setP0sl(CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryDingdmxSl",dingdlj)));
				dingdljService.doUpdateForKd(dingdlj);
			}
		}
		ilOrderService.setOrderNumberMap(new HashMap<String, String>());
		return true;
	}
	
	/**
	 * 计算资源
	 * 区分CD MD/C1 M1
	 * CD、MD线边仓库库存+线边+继承的未交付量+到i-1时段前的到货量+替代件数量，
	 * C1、M1线边仓库库存+继承的未交付量+到i-1时段前的到货量+替代件数量
	 * @param anxcshbxzjb
	 * @author 李智
	 * @version v1.0
	 * @date 2012-3-30
	 * @return 资源
	 * @throws Exception 
	 */
	public BigDecimal getZiy(Anxcshbxzjb anxcshbxzjb,BigDecimal xianbkc) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		
		String sysTime = CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD);
		
		// 算资源
		// 从资源快照表查询线边仓库库存
		param.put("usercenter", anxcshbxzjb.getUsercenter());
		param.put("lingjbh", anxcshbxzjb.getLingjbh());
		param.put("xiaohd", anxcshbxzjb.getXiaohd());
		param.put("xiaohdbh", anxcshbxzjb.getXiaohd());
		param.put("cangkdm", anxcshbxzjb.getXianbck());
		param.put("shengcxbh", anxcshbxzjb.getShengcxbh());
		param.put("xiaohsj", anxcshbxzjb.getXiaohsj());
		//还不太清楚是否要加上资源获取日期
		param.put("ziyhqrq", sysTime);
		//BigDecimal xianbkKucsl = (BigDecimal) this.anxcshbxzjbService.queryXianbkKucslBylingj(param);
		// 最终得到库存
		Ziykzb ziykzb = (Ziykzb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryZiykzbObject", param);
		if(ziykzb == null){
			ziykzb = new Ziykzb();
			ziykzb.setKucsl(BigDecimal.ZERO);
			ziykzb.setDingdlj(BigDecimal.ZERO);
			ziykzb.setJiaoflj(BigDecimal.ZERO);
			ziykzb.setDingdzzlj(BigDecimal.ZERO);
			ziykzb.setXttzz(BigDecimal.ZERO);
		}
		//替代件数量
		BigDecimal tidjzl = this.anxOrderService.queryTidjsl(anxcshbxzjb.getXianbck(), anxcshbxzjb.getUsercenter(), anxcshbxzjb.getLingjbh(),sysTime);
		
		// 加入消耗点和供应商条件
		param.put("gongysdm", anxcshbxzjb.getGongysbh());
		param.put("jiaofrq", sysTime);
		BigDecimal ziy = BigDecimal.ZERO;
		
		//CD、MD
		if(CommonFun.strNull(anxcshbxzjb.getJianglms()).equalsIgnoreCase(Const.ANX_MS_CD) || CommonFun.strNull(anxcshbxzjb.getJianglms2()).equalsIgnoreCase(Const.ANX_MS_MD)) {
			// 继承的未交付量
			Lingjxhd xhd = (Lingjxhd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryWeijfzlByParam", param);
			BigDecimal jicdwjfl = xhd.getJicdwjfl();
			// 资源=线边仓库库存+线边+继承的未交付量+到i-1时段前的到货量+替代件数量
			ziy = ziykzb.getKucsl().add(xianbkc).add(jicdwjfl).add(tidjzl).add(ziykzb.getXttzz());
		}
		//C1、M1
		else{
			// 资源=线边仓库库存+继承的未交付量+到i-1时段前的到货量+替代件数量
			ziy = ziykzb.getKucsl().add(ziykzb.getDingdlj().subtract(ziykzb.getJiaoflj().subtract(ziykzb.getDingdzzlj()))).add(tidjzl).add(ziykzb.getXttzz());
		}
		// 算资源结束
		return ziy;
	}

	/**
	 * 计算消耗量
	 * 
	 * @param anxcshbxzjb
	 * @author 李智
	 * @version v1.0
	 * @date 2012-3-30
	 * @return 消耗量
	 */
	public BigDecimal getXiaohl(Anxcshbxzjb anxcshbxzjb) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("usercenter", anxcshbxzjb.getUsercenter());
		param.put("lingjbh", anxcshbxzjb.getLingjbh());
		param.put("xiaohd", anxcshbxzjb.getXiaohd());
		/*
		// 算消耗量
		// 得到一个毛需求明细的计算初始点
		// 计算开始时间
		String startTime = "";
		if(anxcshbxzjb.getJianglms().equalsIgnoreCase(Const.ANX_MS_CD) || anxcshbxzjb.getJianglms2().equalsIgnoreCase(Const.ANX_MS_MD)) {
			Map time = (Map) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(
					"anx.queryStartXiaohsj", param);
			startTime = CommonFun.strNull(time.get("STARTTIME"));
		} 
		else if(anxcshbxzjb.getJianglms().equalsIgnoreCase(Const.ANX_MS_C1) || anxcshbxzjb.getJianglms2().equalsIgnoreCase(Const.ANX_MS_M1)) {
			//C1 M1取当天日期的第一时刻
			param.put("nowTime", CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD));
			startTime = (String) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(
				"anx.queryStartXiaohsjForC1M1", param);
		}
		*/
		// 截至到消耗时刻
		String endTime = anxcshbxzjb.getXiaohsj();
		param.put("xiaohsj", endTime);
		param.put("xiaohsj2", endTime);
		BigDecimal xiaohl = CommonFun.getBigDecimal(this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryCshXiaohl",
				param));
		return xiaohl;
	}	
	
	/**
	 * 获取计算日期
	 * @param obj 按需计算初始化中间对象
	 * @return 计算日期
	 * @throws ParseException 
	 */
	public int getJisDate(Anxcshbxzjb obj,String user) throws ParseException{
		//如果是CD,C1,取备货周期,运输周期向上取整
		int day = 0;
		if(Const.ANX_MS_CD.equalsIgnoreCase(obj.getJianglms()) || Const.ANX_MS_C1.equalsIgnoreCase(obj.getJianglms())){
			day = obj.getBeihzq().add(obj.getYunszq()).setScale(0,BigDecimal.ROUND_UP).intValue();
		}else{//如果是MD,M1模式,取备货时间2+仓库送货时间2+仓库返回时间2向上取整
			Map map = new HashMap();
			String zickbh = "";//子仓库编号
			map.put("usercenter", obj.getUsercenter());
			map.put("cangkbh", obj.getDinghck());
			map.put("lingjbh", obj.getLingjbh());
			zickbh = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.selZickbh", map));
			map.put("cangkbh", obj.getDinghck()+zickbh);
			//如果子仓库编号为空,查询子仓库编号
			if(obj.getZickbh() == null || obj.getZickbh().equals("")){
				map.put("cangkbh", obj.getXianbck());
				zickbh = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.selZickbh", map));
			}else{
				zickbh = obj.getZickbh();
			}
			map.put("fenpqhck", obj.getXianbck()+zickbh);
			map.put("mos", obj.getJianglms2());
			Cangkxhsj cangkxhsj = (Cangkxhsj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryCangkxhsj",map);
			//如果备货时间为空
			if(cangkxhsj == null){
				Map lingjMap = new HashMap();
				lingjMap.put("lingjbh", obj.getLingjbh());
				lingjMap.put("usercenter", obj.getUsercenter());
				// 查询零件
				Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
				List<String> paramStr = new ArrayList<String>();
				paramStr.add(obj.getJianglms()+obj.getJianglms2());
				paramStr.add(obj.getLingjbh());
				paramStr.add(obj.getXiaohd());
				yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：仓库循环时间为空", lingj.getJihy(), 
						paramStr, obj.getUsercenter(), obj.getLingjbh(), user, Const.JISMK_ANX_CD);
				return -1;
			}
			day = cangkxhsj.getBeihsj().add(cangkxhsj.getCangkshsj()).add(cangkxhsj.getCangkfhsj()).setScale(0,BigDecimal.ROUND_UP).intValue();
		}
		return day;
	}
	
	/**
	 * 生成订单明细
	 * @param anxcshbxzjb
	 * @param loginUser
	 * @param maoxq
	 * @param yaohl
	 * @throws Exception 
	 */
	@Transactional
	public Map insertDingdmx(Anxcshbxzjb anxcshbxzjb,LoginUser loginUser,Maoxq maoxq,BigDecimal yaohl,String zhizlx,String dingdjssj,BigDecimal ziy) throws Exception {
		//模式
		String mos = "";
		//供应商代码字段,如果为C1/CD模式的,则存物流路径供应商代码,如果为M1/MD模式的则存订货库
		String gongysdm = "";
		//供应商类型,如果为C1/CD模式,则存物流路径供应商类型,如果为M1/MD模式则存物流路径订货库类型
		String gongyslx = "";
		//CD、C1
		if(CommonFun.strNull(anxcshbxzjb.getJianglms()).equalsIgnoreCase(Const.ANX_MS_CD) || CommonFun.strNull(anxcshbxzjb.getJianglms()).equalsIgnoreCase(Const.ANX_MS_C1)) {
			mos = anxcshbxzjb.getJianglms();
			gongysdm = anxcshbxzjb.getGongysbh();
			gongyslx = anxcshbxzjb.getGongyslx();
		}
		//MD、M1
		else {
			mos = anxcshbxzjb.getJianglms2();
			gongysdm = anxcshbxzjb.getDinghck();
			gongyslx = anxcshbxzjb.getDinghcklx();
		}
		Dingdlj dingdlj = null;
		Map result = new HashMap();
		Map map = new HashMap();
		Map<String,String> timeMap = anxTimeCount(anxcshbxzjb,mos,loginUser);
		//如果资源要货量为空,跳过当前
		if(timeMap == null){
			result.put("flag", 1);
			result.put("ziy", ziy);
			return result;
		}
		int day = getJisDate(anxcshbxzjb,loginUser.getUsername());
		if(day < 0){
			result.put("flag", 1);
			result.put("ziy", ziy);
			return result;
		}
		//Calendar calendar = Calendar.getInstance();
		//calendar.setTime(new Date());
		//calendar.add(Calendar.DAY_OF_YEAR, 1);
		//计算日期,推最晚到货时间在计算日期工作时间内的才生成订单明细
		//String jisrq = CommonFun.sdf.format(calendar.getTime());
		String gongzr = CommonFun.sdf.format(new Date());
		map.put("gongzr", gongzr);
		map.put("usercenter", anxcshbxzjb.getUsercenter());
		map.put("shengcxbh", anxcshbxzjb.getShengcxbh());
		List<Ticxxsj> listTicxxsj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTicxxsjGzr",map);
		//queryCalendarTeam(map);
		//工作日历中没有相关的维护，系统给出报 警，不进行计算
		if(listTicxxsj == null || listTicxxsj.isEmpty()){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", anxcshbxzjb.getLingjbh());
			lingjMap.put("usercenter", anxcshbxzjb.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(anxcshbxzjb.getMos()+anxcshbxzjb.getMos2());
			paramStr.add(anxcshbxzjb.getLingjbh());
			paramStr.add(anxcshbxzjb.getXiaohd());
			paramStr.add(gongzr);
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4工作日历为空", lingj.getJihy(), 
					paramStr, anxcshbxzjb.getUsercenter(), anxcshbxzjb.getLingjbh(), loginUser.getUsername(), Const.JISMK_ANX_CD);
			result.put("flag", 1);
			result.put("ziy", ziy);
			return result;
		}
		gongzr = listTicxxsj.get(0).getGongzr();
		Date start = CommonFun.yyyyMMddHHmmss.parse(listTicxxsj.get(0).getRiq() + " " +listTicxxsj.get(0).getShijdkssj());
		Date end = CommonFun.yyyyMMddHHmmss.parse(listTicxxsj.get(listTicxxsj.size() - 1).getRiq() + " " + listTicxxsj.get(listTicxxsj.size() - 1).getShijdjssj());
		if(day > 1){
			//calendar.add(Calendar.DAY_OF_YEAR, day - 1);
			//String riq = CommonFun.sdf.format(calendar.getTime());
			map.put("gongzr", gongzr);
			List<Ticxxsj> listTicxxsj2 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTicxxsjGzr",map);
			//queryCalendarTeam(map);
			//工作日历中没有相关的维护，系统给出报 警，不进行计算
			if(listTicxxsj2 == null || listTicxxsj2.isEmpty()){
				Map lingjMap = new HashMap();
				lingjMap.put("lingjbh", anxcshbxzjb.getLingjbh());
				lingjMap.put("usercenter", anxcshbxzjb.getUsercenter());
				// 查询零件
				Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
				List<String> paramStr = new ArrayList<String>();
				paramStr.add(anxcshbxzjb.getMos()+anxcshbxzjb.getMos2());
				paramStr.add(anxcshbxzjb.getLingjbh());
				paramStr.add(anxcshbxzjb.getXiaohd());
				paramStr.add(gongzr);
				yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4工作日历为空", lingj.getJihy(), 
						paramStr, anxcshbxzjb.getUsercenter(), anxcshbxzjb.getLingjbh(), loginUser.getUsername(), Const.JISMK_ANX_CD);
				result.put("flag", 1);
				result.put("ziy", ziy);
				return result;
			}
			end = CommonFun.yyyyMMddHHmmss.parse(listTicxxsj2.get(listTicxxsj2.size() - 1).getRiq() + " " + listTicxxsj2.get(listTicxxsj2.size() - 1).getShijdjssj());;
		}
		//比较最晚到货时间和当前时间
		int flag = 0;//如果最晚到货时间早于到货日期开始工作时间,判断是否有要货,如果有,要货,没有则不要
		if(CommonFun.yyyyMMddHHmm.parse(timeMap.get("zuiwdhsj")).before(start)){
			flag = 1;
		//如果最晚到货时间早于到货日期开始工作时间,终止
		}else if(CommonFun.yyyyMMddHHmm.parse(timeMap.get("zuiwdhsj")).after(end)){
			flag = -1;
		}
		result.put("flag", flag);
		//
		if (flag != -1) {
		//yyyy-mm-dd日期
		String rq = CommonFun.sdf.format(CommonFun.sdf.parse(anxcshbxzjb.getXiaohsj()));
		String shid = anxcshbxzjb.getXiaohsj();
			if(flag == 1){
				//如果早于,但是没有要货,则跳过
				if(yaohl.compareTo(BigDecimal.ZERO) != 1){
					return result;
				}else{
					rq = listTicxxsj.get(0).getRiq();
					shid = rq + " " +listTicxxsj.get(0).getShijdkssj();
					shid = shid.substring(0,16);
				}
			}
		Map<String, String> param = new HashMap<String, String>();
		
		// 得到订单号
		Map dingdhMap = new HashMap();
		String dingdh = ilOrderService.getOrderNumber(
				mos, anxcshbxzjb.getUsercenter(),
				gongysdm,dingdhMap);
		map.put("dingdh", dingdh);
		map.put("usercenter", anxcshbxzjb.getUsercenter());
		map.put("lingjbh", anxcshbxzjb.getLingjbh());
		map.put("gongysdm", gongysdm);
		map.put("xiaohd", anxcshbxzjb.getXiaohd());
		// 得到到货数量
		//Dingdmx yaohmx = this.anxMaoxqService.queryCountAnxDingddh(map);
		//Dingdmx yaohmx = anxMaoxqService.queryCountAnxDingdmx(map);
		// 当是第一次计算的时候，到货数量为0
		//if (null != yaohmx) {
			//yaohl = yaohl.subtract(yaohmx.getShul().subtract(yaohmx.getJissl()));
		//}
		if(yaohl.compareTo(BigDecimal.ZERO) != 1){
			result.put("flag", flag);
			result.put("ziy", ziy);
			return result;
		}
		// 根据订单号查询订单
		param.put("usercenter", anxcshbxzjb.getUsercenter());
		param.put("lingjbh", anxcshbxzjb.getLingjbh());
		param.put("gongysdm", anxcshbxzjb.getGongysbh());            
		param.put("cangkdm", anxcshbxzjb.getXianbck());
		param.put("dingdh", dingdh);
		Dingd dingd = dingdService.queryDingdByDingdh(param);
		// 得到供应商信息
		Gongys gongys = gongysService.queryObject(
				anxcshbxzjb.getGongysbh(),
				anxcshbxzjb.getUsercenter());
		// 订单是否插入成功
		boolean insertDingdBool = true;
		// 订单不存在，生成订单插入
		if (dingd == null) {
			dingd = new Dingd();
			
			String sysTime = CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD);
			param.put("riq", sysTime);
			
			param.put("shifgzr", Const.GZR_Y);
			// 取得前一个工作日日期
			CalendarCenter center = anxMaoxqService.queryWorkDay(param);
			//创建时间
			String createTime = CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS);
			dingd.setDingdh(dingdh);
			dingd.setUsercenter(anxcshbxzjb.getUsercenter());//用户中心
			dingd.setHeth(anxcshbxzjb.getGongyhth());// 合同号
			if(mos.equalsIgnoreCase(Const.ANX_MS_C1) || mos.equalsIgnoreCase(Const.ANX_MS_CD)){
				dingd.setDingdlx(Const.DINGD_LX_ANX_CSH_C);// 类型
			}else if(mos.equalsIgnoreCase(Const.ANX_MS_M1) || mos.equalsIgnoreCase(Const.ANX_MS_MD)){
				dingd.setDingdlx(Const.DINGD_LX_ANX_CSH_M);// 类型
			}
			
			dingd.setGongysdm(gongysdm);//供应商代码
			dingd.setGongyslx(gongyslx);// 供应商类型
			dingd.setChullx(mos);// C
			dingd.setDingdzt(Const.DINGD_STATUS_DSX);// 状态
			dingd.setCreator(loginUser.getUsername());//订单创建人
			dingd.setCreate_time(createTime);//订单创建时间
			dingd.setEditor(loginUser.getUsername());//订单编辑人
			dingd.setEdit_time(createTime);//订单编辑时间
			dingd.setShiffsgys(Const.SHIFFSGYS_NO);// 不是临时订单
			dingd.setDingdjssj(dingdjssj);// 订单计算时间
			dingd.setDingdnr(Const.SHIFOUSHIJIDING);// 既定
			dingd.setFahzq(rq);//发运周期
			dingd.setZiyhqrq(center.getRiq().substring(0,10));// 资源获取日期
			dingd.setMaoxqbc(maoxq.getXuqbc());// 版次
			dingd.setJislx(loginUser.getUsername());// 系统计算(admin)、用户提交(提交者用户名)
			dingd.setShifzfsyhl(Const.SHIFZFSYHL_YES);// 发送要货令
			dingd.setActive(Const.ACTIVE_1);	//删除标识
			
			insertDingdBool = this.dingdService.doInsert(dingd);
		}
		// 订单插入成功
		if (insertDingdBool) {
			//得到零件信息
			Lingj lingj = lingjService.queryObject(anxcshbxzjb.getLingjbh(), anxcshbxzjb.getUsercenter());
			Dingdmx dingdmx = new Dingdmx();
			dingdmx.setDingdh(dingdh);//订单号
			dingdmx.setUsercenter(anxcshbxzjb.getUsercenter());//用户中心
			dingdmx.setLingjbh(anxcshbxzjb.getLingjbh());//零件号
			dingdmx.setGongysdm(gongysdm);	//供应商代码
			dingdmx.setGongyslx(gongyslx);			//供应商类型
			dingdmx.setZhidgys(anxcshbxzjb.getZhidgys());	//指定供应商
			dingdmx.setLingjmc(lingj.getZhongwmc());//零件名称
			dingdmx.setGongsmc(gongys.getGongsmc());//供应商名称
			
			dingdmx.setYaohqsrq(rq);//要货起始日期
			dingdmx.setYaohjsrq(rq);//要货结束日期
			dingdmx.setCangkdm(anxcshbxzjb.getXianbck());//仓库代码
			dingdmx.setXiehzt(anxcshbxzjb.getXiehztbh());//卸货站台
			dingdmx.setFahd(anxcshbxzjb.getFahd());	//发货地
			dingdmx.setDinghcj(anxcshbxzjb.getChej());//订货车间
			
			dingdmx.setDanw(anxcshbxzjb.getDanw());//单位
			dingdmx.setJiaofrq(rq);//交付日期
			dingdmx.setZhuangt(Const.DINGD_STATUS_DSX);//状态
			dingdmx.setLeix(Const.SHIFOUSHIJIDING);//既定
			dingdmx.setLujdm(anxcshbxzjb.getLujbh());//路径代码
			dingdmx.setJihyz(lingj.getJihy());//计划员组
			dingdmx.setZuihwhr(loginUser.getUsername());// 订单维护人
			dingdmx.setZuihwhsj(CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS));// 维护时间
			//承运商编号
			dingdmx.setGcbh(anxcshbxzjb.getGcbh());
			//仓库备货时间
			dingdmx.setBeihsj2(anxcshbxzjb.getBeihsj2());
			dingdmx.setGonghlx(mos);//供货类型
			dingdmx.setYijfl(anxcshbxzjb.getJiaofzl());//交付总量
			dingdmx.setShid(shid);//时段
			dingdmx.setUabzlx(anxcshbxzjb.getGysuabzlx());//UA包装类型
			dingdmx.setUabzuclx(anxcshbxzjb.getGysucbzlx());//UA包装内UC类型
			dingdmx.setUabzucsl(anxcshbxzjb.getGysuaucgs());//UA包装内UC数量
			dingdmx.setUabzucrl(anxcshbxzjb.getGysucrl());//供应商UC的容量
			dingdmx.setUsbaozlx(anxcshbxzjb.getCkusbzlx());	//仓库US的包装类型
			dingdmx.setUsbaozrl(anxcshbxzjb.getCkusbzrl());	//仓库US的包装容量
			dingdmx.setXiaohsj(anxcshbxzjb.getXiaohsj());//消耗时间
			dingdmx.setXianbyhlx(anxcshbxzjb.getXianbyhlx());//线边要货类型：K/R
			dingdmx.setXiaohch(anxcshbxzjb.getXiaohcbh());	//小火车号
			dingdmx.setXiaohccxh(anxcshbxzjb.getXiaohccxbh());//小火车车厢号
			dingdmx.setChanx(anxcshbxzjb.getXianh());		//产线
			dingdmx.setXiaohd(anxcshbxzjb.getXiaohd());	//消耗点
			dingdmx.setFenpxh(anxcshbxzjb.getFenpqbh());//分配循环
			dingdmx.setCreator(loginUser.getUsername());// 创建人
			dingdmx.setCreate_time(CommonFun.getJavaTime());// 创建时间
			dingdmx.setEditor(loginUser.getUsername());// 修改人
			dingdmx.setEdit_time(CommonFun.getJavaTime());// 修改时间
			String beihsj = "";// 小火车备货时间
			String shangxsj = "";// 小火车上线时间
			if(flag == 0){
				dingdmx.setZuizdhsj(timeMap.get("zuizdhsj"));// 最早到货时间
				dingdmx.setZuiwdhsj(timeMap.get("zuiwdhsj"));// 最晚到货时间
				beihsj = timeMap.get("beihsj");// 小火车备货时间
				shangxsj = timeMap.get("shangxsj");// 小火车上线时间
			}else{
				dingdmx.setZuizdhsj(shid);// 最早到货时间
				dingdmx.setZuiwdhsj(shid);// 最晚到货时间
				beihsj = shid;// 小火车备货时间
				shangxsj = shid;// 小火车上线时间
			}
			
			dingdmx.setGongyfe(anxcshbxzjb.getGongyfe());//供应份额
			dingdmx.setZuixqdl(anxcshbxzjb.getZuixqdl());//最小起订量
			dingdmx.setLingjsx(lingj.getLingjsx());		//零件属性(A零件,K卷料,M辅料)
			
			// 是CD或者C1模式
			if (Const.ANX_MS_CD.equals(mos) || Const.ANX_MS_C1.equals(mos)) {
				if(Const.ANX_MS_CD.equals(mos)){
					dingdmx.setXiaohcbhsj(beihsj);
					dingdmx.setXiaohcsxsj(shangxsj);
				}
				BigDecimal feneMap = fenejs(dingdmx, yaohl);
				//如果为CD就取供应商份额数量
				dingdmx.setShul(CommonFun.roundingByPack(feneMap, anxcshbxzjb.getGysucrl().multiply(anxcshbxzjb.getGysuaucgs())));
				//如果为CD就取供应商份额数量
				dingdmx.setJissl(yaohl);//计算数量
			}else if(Const.ANX_MS_MD.equals(mos) ){
				dingdmx.setXiaohcbhsj(beihsj);
				dingdmx.setXiaohcsxsj(shangxsj);
				//MD模式取UC容量
				dingdmx.setShul(CommonFun.roundingByPack(yaohl, anxcshbxzjb.getCkucrl()));
				dingdmx.setJissl(yaohl);//计算数量
			}else{
				//M1模式取US容量
				dingdmx.setShul(CommonFun.roundingByPack(yaohl, anxcshbxzjb.getCkusbzrl()));
				dingdmx.setJissl(yaohl);//计算数量
			}
			ziy = dingdmx.getShul().subtract(dingdmx.getJissl());
			dingdmxService.doInsert(dingdmx);
			//查询订单零件
			dingdlj = (Dingdlj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryDingdlj",param);
			//如果订单零件不为空
			if(dingdlj == null){
				dingdlj = new Dingdlj();
				//订单号
				dingdlj.setDingdh(param.get("dingdh")) ;
				dingdlj.setP0fyzqxh(rq);
				//删除标识
				dingdlj.setActive(Const.ACTIVE_1) ;
				//用户中心
				dingdlj.setUsercenter(anxcshbxzjb.getUsercenter());
				//零件编号
				dingdlj.setLingjbh(anxcshbxzjb.getLingjbh());
				dingdlj.setZuixqdl(anxcshbxzjb.getZuixqdl());
				//指定供应商
				dingdlj.setZhidgys(anxcshbxzjb.getZhidgys());
				//终止零件
				dingdlj.setZhongzlj(anxcshbxzjb.getZhongzzl());
				//制造路线
				dingdlj.setZhizlx(anxcshbxzjb.getZhizlx());
				//发货地
				dingdlj.setFahd(anxcshbxzjb.getFahd());
				//创建时间
				dingdlj.setCreate_time(CommonFun.getJavaTime());
				//订单制作时间
				dingdlj.setDingdzzsj(CommonFun.getJavaTime().substring(0, 19)) ;
				Date date = new Date();
				//date.setTime(CommonFun.sdf.parse(CommonFun.getJavaTime().substring(0, 19)).getTime() - 24 * 60 * 60 * 1000);
				//资源获取日期
				dingdlj.setZiyhqrq(CommonFun.sdf.format(date)) ;
				//创建人
				dingdlj.setCreator(loginUser.getUsername());
				//仓库
				dingdlj.setCangkdm(anxcshbxzjb.getXianbck().substring(0,3));
				//单位
				dingdlj.setDanw(anxcshbxzjb.getDanw());
				//安全库存数量
				dingdlj.setAnqkc(anxcshbxzjb.getAnqkcs());
				dingdlj.setUabzlx(anxcshbxzjb.getGysuabzlx());
				dingdlj.setUabzuclx(anxcshbxzjb.getGysucbzlx());
				dingdlj.setUabzucrl(anxcshbxzjb.getGysucrl());
				dingdlj.setUabzucsl(anxcshbxzjb.getGysuaucgs());
				dingdlj.setUsbaozlx(anxcshbxzjb.getCkusbzlx());
				dingdlj.setUsbaozrl(anxcshbxzjb.getCkusbzrl());
				//供应商份额
				dingdlj.setGongysfe(anxcshbxzjb.getGongyfe());
				//备货周期
				dingdlj.setBeihzq(anxcshbxzjb.getBeihzq());
				//发货周期
				dingdlj.setFayzq(anxcshbxzjb.getYunszq());
				//供货模式
				dingdlj.setGonghms(mos);
				//路径代码
				dingdlj.setLujdm(anxcshbxzjb.getLujbh());
				//供应商代码
				dingdlj.setGongysdm(gongysdm);
				//终止零件
				dingdlj.setZhongzlj(anxcshbxzjb.getZhongzzl());
				//交付零件
				dingdlj.setJiaoflj(anxcshbxzjb.getJiaofzl());
				dingdljService.doInsert(dingdlj);
				result.put("dingdlj",dingdlj);
			}
		}
		}
		result.put("ziy", ziy);
		return result;
	}
	/**
	 * 份额计算
	 * 
	 * @param bean 按需订单明细
	 * @param zongyhl 时段总要货量
	 * 
	 * @return 份额计算后的要货量
	 *
	 * @throws ServiceException
	 */
	public BigDecimal fenejs(Dingdmx bean, BigDecimal zongyhl) throws ServiceException {
	 
	logger.info(this.getClass().getName() +"：份额计算开始：fenejs():");
	// 订单明细为空，终止计算
	if (null == bean) {
	logger.info(this.getClass().getName() +"：份额计算异常结束：fenejs():Dingdmx bean == null");
	logger.debug(this.getClass().getName() +"：份额计算正常结束：yaohl=BigDecimal.ZERO");
	return BigDecimal.ZERO;
	}
	// 要货量
	BigDecimal yaohl = BigDecimal.ZERO;
	// 指定供应商时
	if (!StringUtils.isBlank(bean.getZhidgys())) {
	 
	// 指定的供应商代码  = 订单明细关联的供应商时
	if (bean.getGongysdm().equalsIgnoreCase(bean.getZhidgys())) {
	// 要货份额为100%
	yaohl = zongyhl;
	// 指定的供应商代码  ≠ 订单明细关联的供应商时
	} else {
	// 要货量为０
	yaohl = BigDecimal.ZERO;
	}
	// 未指定供应商时
	} else {
	// 要货量 = 总要货量   X 要货份额%
	yaohl = zongyhl.multiply(bean.getGongyfe());
	}
	logger.info(this.getClass().getName() +"：份额计算正常结束：fenejs():");
	logger.debug(this.getClass().getName() +"：份额计算正常结束：yaohl=" + yaohl);
	 
	// 返回计算份额后的要货量
	return yaohl;
	}
	
	/*
	 * 时间计算(最早，最晚及上线时间计算) 返回结果位map暂定为存放的最早，最晚，预计到货时间
	 */
	public Map<String, String> anxTimeCount(Anxcshbxzjb bean,String mos,LoginUser user) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
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
		 * 从卸货站台得到提前到货时间
		 */
		map.put("usercenter", bean.getUsercenter());
		map.put("xiehztbh", bean.getXiehztbh());
		Xiehzt xiehzt = this.anxMaoxqService.queryXiehztObject(map);
		map.clear();
		// 允许提前到货时间
		BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
		//如果备货时间为空
		if(yunxtqdhsj == null){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", bean.getLingjbh());
			lingjMap.put("usercenter", bean.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(bean.getMos()+bean.getMos2());
			paramStr.add(bean.getLingjbh());
			paramStr.add(bean.getXiaohd());
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：允许提前到货时间为空", lingj.getJihy(), 
					paramStr, bean.getUsercenter(), bean.getLingjbh(), user.getUsername(), Const.JISMK_ANX_CD);
			return null;
		}
		if(bean.getXiehztbh() == null || bean.getXiehztbh().equals("")){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", bean.getLingjbh());
			lingjMap.put("usercenter", bean.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(bean.getMos());
			paramStr.add(bean.getLingjbh());
			paramStr.add(bean.getXiaohd());
			paramStr.add(bean.getXiaohcbh());
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4卸货站台编号为空", lingj.getJihy(), 
					paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
			return null;
		}
		/**
		 * 从卸货站台循环时间得到内部物流时间
		 */
		map.put("usercenter", bean.getUsercenter());
		map.put("xiehztbh", bean.getXiehztbh().substring(0,4));
		String zickbh = "";
		//如果子仓库编号为空,查询子仓库编号
		if(bean.getZickbh() == null || bean.getZickbh().equals("")){
			map.put("cangkbh", bean.getXianbck());
			map.put("lingjbh", bean.getLingjbh());
			zickbh = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.selZickbh", map));
		}else{
			zickbh = bean.getZickbh();
		}
		map.put("cangkbh", bean.getXianbck()+zickbh);
		map.put("mos", mos);
		Xiehztxhsj xiehztxhsj = this.anxMaoxqService.queryXiehztxhsjObject(map);
		map.clear();
		//如果备货时间为空
		if(xiehztxhsj == null || xiehztxhsj.getBeihsj() == null){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", bean.getLingjbh());
			lingjMap.put("usercenter", bean.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(bean.getMos()+bean.getMos2());
			paramStr.add(bean.getLingjbh());
			paramStr.add(bean.getXiaohd());
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：内部物流时间为空", lingj.getJihy(), 
					paramStr, bean.getUsercenter(), bean.getLingjbh(), user.getUsername(), Const.JISMK_ANX_CD);
			return null;
		}
		// 内部物流时间
		BigDecimal neibwlsj = xiehztxhsj.getBeihsj();
		Date date = new Date();
		Date dateBeihsj = new Date();
		if (mos.equalsIgnoreCase(Const.ANX_MS_CD) || 
				mos.equalsIgnoreCase(Const.ANX_MS_MD)) {
			// ==============备货时间--strat==============
			map.put("usercenter", bean.getUsercenter());
			// map.put("riq", CommonFun.getJavaTime().substring(0, 10));
			map.put("xiaohcbh", bean.getXiaohcbh());
			map.put("shengcxbh", bean.getShengcxbh());
			map.put("kaisbhsj", bean.getXiaohsj().substring(0, 16));
			// 取小火车运输时刻
			List<Xiaohcyssk> Listxhcyssk = xiaohcysskService.queryXiaohcysskObject(map);
			//如果小火车时刻表没有数据,跳过该条记录,不予计算
			if(Listxhcyssk == null || Listxhcyssk.isEmpty()){
				Map lingjMap = new HashMap();
				lingjMap.put("lingjbh", bean.getLingjbh());
				lingjMap.put("usercenter", bean.getUsercenter());
				// 查询零件
				Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
				List<String> paramStr = new ArrayList<String>();
				paramStr.add(bean.getMos()+bean.getMos2());
				paramStr.add(bean.getLingjbh());
				paramStr.add(bean.getXiaohd());
				paramStr.add(bean.getXiaohcbh());
				yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4小火车信息为空", lingj.getJihy(), 
						paramStr, bean.getUsercenter(), bean.getLingjbh(), user.getUsername(), Const.JISMK_ANX_CD);
				return null;
			}
			map.clear();
			// 第一条运输时间如果和消耗时间相等,则取前面一条小火车备货时间为备货时间
			// 反之,取第二条
			Xiaohcyssk xiaohcyssk = Listxhcyssk.get(1);
			// 备货时间
			beihsj = xiaohcyssk.getKaisbhsj();
			// 上线时间
			shangxsj = xiaohcyssk.getChufsxsj();
			/**
			 * ==============上线时间--end==============
			 * **/
			// 预计到货时间(yujdhsj)=小火车备货时间-内部物流时间
			Date dates = CommonFun.yyyyMMddHHmm.parse(beihsj);
			dates.setTime(dates.getTime() - neibwlsj.intValue() * 60 * 1000);
			// 最后得到:预计到货时间
			yujdhsj = CommonFun.yyyyMMddHHmm.format(dates);
			dateBeihsj = CommonFun.yyyyMMddHHmm.parse(beihsj);
		} else {
			// C1,M1模式的时间
			// 预计到货时间(yujdhsj)=消耗时间-内部物流时间
			dateBeihsj = CommonFun.yyyyMMddHHmm.parse(bean.getXiaohsj());
		}
		
		
		date.setTime(dateBeihsj.getTime() - neibwlsj.intValue() * 60 * 1000);

		/**
		 * 获取到最晚到货时间
		 * */
		map.put("usercenter", bean.getUsercenter());
		map.put("xiehztbh", bean.getXiehztbh().substring(0,4));//取编号前4位为卸货站台编组
		map.put("gcbh", bean.getGcbh());//承运商编号
		zuiwdhsj = CommonFun.yyyyMMddHHmm.format(date);
		map.put("daohsj", zuiwdhsj);
		List<Yunssk> listYunssk = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryYunsskObject", map);
		if (listYunssk != null && !listYunssk.isEmpty()) {
			zuiwdhsj = listYunssk.get(0).getDaohsj().substring(0, 16);
		}
		Date zuizDate = new Date();
		zuizDate.setTime(CommonFun.yyyyMMddHHmm.parse(zuiwdhsj).getTime() - yunxtqdhsj.intValue() * 60 * 1000);

		// 最早到货时间
		zuizdhsj = CommonFun.yyyyMMddHHmm.format(zuizDate);
		// 存放到Map中
		map.put("zuiwdhsj", zuiwdhsj);
		map.put("zuizdhsj", zuizdhsj);
		map.put("yujdhsj", yujdhsj);
		map.put("shangxsj", shangxsj);
		map.put("beihsj", beihsj);

		return map;
	}
}