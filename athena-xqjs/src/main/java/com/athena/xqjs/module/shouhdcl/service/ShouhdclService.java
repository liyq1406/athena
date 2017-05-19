package com.athena.xqjs.module.shouhdcl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.shouhdcl.Jusgzd;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.GongysService;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.support.PageableSupport;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ShouhdclService
 * <p>
 * 类描述：收货待处理service
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-04-06
 * </p>
 * 
 * @version 1.0
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
public class ShouhdclService extends BaseService {

	@Inject
	private DingdService dingdService;

	@Inject
	private IlOrderService ilOrderService;

	@Inject
	private GongysService gService;
	
	@Inject
	private LingjService ljService;

	public static final Logger logger = Logger.getLogger(ShouhdclService.class);

	/**
	 * 查询拒收跟踪单
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryShouhdcl(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("shouhdcl.queryShouhdcl", param, page);
	}

	/**
	 * 查询拒收跟踪单
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryShouhdclshlb(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("shouhdcl.queryShouhdclshlb", param, page);
	}

	/**
	 * 审核拒收跟踪单
	 * 
	 * @param jusgzds
	 *            拒收跟踪单列表
	 * @return 审核结果
	 */
	public void shenH(List<Jusgzd> jusgzds) {
		for (int i = 0; i < jusgzds.size(); i++) {
			Jusgzd jusgzd = jusgzds.get(i);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("shouhdcl.shenH", jusgzd);
		}
	}

	/**
	 * 查询要货令列表
	 * 
	 * @param page
	 * @param param
	 * @return 要货令列表
	 */
	public Map queryYaohl(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("shouhdcl.queryYaohl", param, page);
	}

	/**
	 * 指定要货令
	 * 
	 * @param jusgzd
	 *            拒收跟踪单信息
	 */
	public void zhiDyhl(Jusgzd jusgzd) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("shouhdcl.zhiDyhl", jusgzd);
	}

	/**
	 * 校验要货令
	 * 
	 * @param yaohl
	 *            要货令信息
	 * @return true-不存在,可以指定/false-存在,不可以指定
	 */
	public boolean checkYhl(Yaohl yaohl) {
		// 查询要货令是否被指定,为空,则未被指定
		return (baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("shouhdcl.checkYhl", yaohl) == null);
	}

	/**
	 * 批量指定要货令
	 * 
	 * @param jusgzd
	 *            拒收跟踪单信息
	 */
	public String piLzdyhl(List<Jusgzd> jusgzds) {
		// 根据用户中心,零件编号查询要货令列表
		Jusgzd jusgzd = jusgzds.get(0);
		Map param = new HashMap<String, String>();
		param.put("lingjbh", jusgzd.getLingjbh());// 零件编号
		param.put("usercenter", jusgzd.getUsercenter());// 用户中心
		List<Yaohl> yaohls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("shouhdcl.queryYaohl", param);
		int size = yaohls.size();
		int num = 0;
		// 拒收跟踪单
		for (int i = 0; i < jusgzds.size(); i++) {
			// 如果还有要货令,给拒收跟踪单指定要货令
			if (size > i) {
				jusgzd = jusgzds.get(i);
				Yaohl yaohl = yaohls.get(i);
				// 如果要货令已经被指定,跳过该要货令
				if (!checkYhl(yaohl)) {
					continue;
				}
				jusgzd.setYaohlh(yaohl.getYaohlh());// 要货令号
				jusgzd.setDingdh(yaohl.getDingdh());// 订单号
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("shouhdcl.zhiDyhl", jusgzd);
				num++;
			} else {// 要货令不足
				return "已经指定" + num + "个要货令,还有" + (jusgzds.size() - num) + "个" + "要货令指定不成功,因为表达状态要货令不足,请为剩下的创建临时订单";
			}
		}
		return "批量指定要货令成功";
	}

	/**
	 * 创建临时订单
	 * 
	 * @param dingd
	 *            临时订单
	 * @param jusgzd
	 *            拒收跟踪单
	 * @param creator
	 *            创建人
	 */
	@Transactional
	public String saveLinsdd(String jiaofrq, Dingd dingd, List<Jusgzd> jusgzds, String creator) {
		String str = "";
		dingd.setActive("1");
		dingd.setCreator(creator);
		dingd.setEditor(creator);
		// 创建时间
		String time = CommonFun.getJavaTime();
		dingd.setCreate_time(time);
		dingd.setEdit_time(time);
		dingd.setDingdjssj(time.substring(0, 10));
		Map<String, String> dingdhMap = new HashMap<String, String>();
		Map<String, String> wulljmap = new HashMap<String, String>();
		// 生成订单号
		String dingdh = ilOrderService.getOrderNumber("T", dingd.getUsercenter(), dingd.getGongysdm(), dingdhMap);
		dingdhMap = null;
		dingd.setDingdh(dingdh);
		// 如果为发送供应商,则状态为待生效
		if ("1".equals(dingd.getShiffsgys())) {
			dingd.setDingdzt(Const.DINGD_STATUS_DSX);
		} else {// 否则为已生效
			dingd.setDingdzt(Const.DINGD_STATUS_YSX);
		}
		// 查询供应商类型
		Gongys gys = gService.queryObject(dingd.getGongysdm(), dingd.getUsercenter());
		Lingj lingj = ljService.select(dingd.getUsercenter(), dingd.getLingjbh());
////////////////////wuyichao  2014-09-15 KD订单取按键目录卸货点前3位 ///////////
		if (lingj == null) {
			throw new RuntimeException("零件不存在或未生效，无法创建临时订单");
		}
////////////////////wuyichao  2014-09-15 KD订单取按键目录卸货点前3位 ///////////
		boolean bl = dingdService.doInsert(dingd);
		logger.info(creator + "创建临时订单,订单号" + dingdh + ",结果" + bl);
		if (bl) {
			for (int i = 0; i < jusgzds.size(); i++) {
				Jusgzd jusgzd = jusgzds.get(i);
				Wullj wl=null;
				if(Const.ANX_MS_CD.equals(jusgzd.getGonghms())){
					//校验消耗点 物流路径
					wulljmap.put("usercenter", dingd.getUsercenter());
					wulljmap.put("lingjbh", dingd.getLingjbh());
					wulljmap.put("xiaohdbh", dingd.getMdd());
					wulljmap.put("waibms", jusgzd.getGonghms());
					 wl = (Wullj) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.ShouhdclCheckXhd", wulljmap);
				}else{
					wulljmap.put("usercenter", dingd.getUsercenter());
					wulljmap.put("lingjbh", dingd.getLingjbh());
					wulljmap.put("mudd", jusgzd.getCangkbh());
					wulljmap.put( "gongysbh", CommonFun.strUndefined(dingd.getGongysdm()) ); //xss_按照供应商取一条
					
					wl = (Wullj) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryWulljForShouhdcl", wulljmap);
				}
				if (wl != null) {
					Dingdmx dingdmx = new Dingdmx();
					if(Const.ANX_MS_CD.equals(jusgzd.getGonghms())){
						dingdmx.setXiaohd(dingd.getMdd());//目的地 
						dingdmx.setBeihsj2(wl.getBeihsj());//备货时间2=物流路径备货时间
						//gswang 2014-10-29 0010411:要货令查询终止（外部）增加产线
						dingdmx.setChanx(wl.getShengcxbh());
						
					}
					if("R1".equals(wl.getWaibms()) || "R2".equals(wl.getWaibms())){
						throw new RuntimeException("看板的零件在审核时不能通过创建临时订单来收货，需指定要货令");
					}
					//0010394 收货待处理创建临时订单 校验物流路径中卸货站台编号是否存在，不存在提示并终止后续操作。
					if(jusgzd.getXiehd() == null || "".equals(jusgzd.getXiehd())){
						throw new RuntimeException("物流路径中卸货站台编号为空，无法创建临时订单");
					}
					dingdmx.setGonghlx(CommonFun.strUndefined(wl.getWaibms()));// 供货模式
					dingdmx.setFayjssj(jiaofrq);//发运日期
					dingdmx.setDingdh(dingdh);// 订单号
					dingdmx.setJiaofrq(jiaofrq);// 交付日期
					dingdmx.setLingjbh(dingd.getLingjbh());// 零件编号
					dingdmx.setUsercenter(jusgzd.getUsercenter());// 用户中心
					dingdmx.setActive("1");// 标识
					dingdmx.setCreator(creator);// 创建人
					dingdmx.setEditor(creator);// 编辑人
					dingdmx.setCreate_time(time);// 创建时间
					dingdmx.setEdit_time(time);// 编辑时间
					dingdmx.setGongyslx(CommonFun.strUndefined(gys.getLeix()));// 供应商类型
					dingdmx.setShul(jusgzd.getJusljs());// 拒收数量
					dingdmx.setGongysdm(CommonFun.strUndefined(dingd.getGongysdm()));// 供应商代码
					wulljmap.put("gongysdm", CommonFun.strUndefined(dingd.getGongysdm()));
					LingjGongys lingjgys = (LingjGongys) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anxjis.queryLingjGys",wulljmap);
					if(lingjgys != null){                                                                                                              
						dingdmx.setUabzlx(lingjgys.getUabzlx());// UA包装类型
						dingdmx.setUabzrl(CommonFun.getBigDecimal(lingjgys.getUcrl()).multiply(CommonFun.getBigDecimal(lingjgys.getUaucgs())));// UA容量
						dingdmx.setUabzuclx(lingjgys.getUcbzlx());// UC类型
						dingdmx.setUabzucrl(lingjgys.getUcrl());// UC容量
						dingdmx.setUabzucsl(lingjgys.getUaucgs());// UC个数
					}
					
					dingdmx.setZhuangt(dingd.getDingdzt());// 订单状态
					dingdmx.setDanw(CommonFun.strUndefined(jusgzd.getDanw()));// 单位
					dingdmx.setGcbh(CommonFun.strUndefined(jusgzd.getChengysdm()));// 承运商代码
					dingdmx.setXiehzt(CommonFun.strUndefined(jusgzd.getXiehd()));// 卸货点
					dingdmx.setCangkdm(CommonFun.strUndefined(jusgzd.getCangkbh()));// 仓库代码
////////////////////wuyichao  2014-09-15 KD订单取按键目录卸货点前3位 ///////////
				//	if(StringUtils.isNotBlank(gys.getGonghlx()) &&( gys.getGonghlx().equalsIgnoreCase("97X") ||  gys.getGonghlx().equalsIgnoreCase("97D")))
				//	{
				//		if (StringUtils.isBlank(lingj.getAnjmlxhd()) || lingj.getAnjmlxhd().length() < 3) {
				//			throw new RuntimeException("零件号" + dingd.getLingjbh() + "的按件目录卸货点字段为空或长度不足3位，无法创建临时订单");
				//		}
				//		dingdmx.setDinghcj(lingj.getAnjmlxhd().substring(0, 3));
				//	}
				//	else
				//	{
						dingdmx.setDinghcj(lingj.getDinghcj());// 订货车间
				//	}
////////////////////wuyichao  2014-09-15 KD订单取按键目录卸货点前3位 ///////////					
					dingdmx.setZuizdhsj(jiaofrq);//最早到货时间
					dingdmx.setZuiwdhsj(jiaofrq);//最晚到货时间
					dingdmx.setXiaohcbhsj(jiaofrq);//小火车备货时间
					dingdmx.setXiaohcsxsj(jiaofrq);//小火车出发上线时间
					dingdmx.setLeix("9");
					
					//xss-0012868
					String lujdm ="";
					lujdm = wl.getLujbh();
					dingdmx.setLujdm(lujdm);					
					
					// 保存订单明细
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.createTempDingdmx", dingdmx);
					str = dingdh;
				} else {
					throw new RuntimeException("创建临时订单失败,物流路径为空!");
					//str = "2";
				}
			}

		} else {
			// 判断订单是否存在
			str = "false";
		}
		return str;
	}
	
	/*
	 * wtc调用失败后删除生成的订单和订单明细
	 * */
	public void deleteLinsddmx(String dingdh){
		
		Dingdmx dingdmx = new Dingdmx();
		Dingd dingd = new Dingd();
		
		dingdmx.setDingdh(dingdh);// 订单号
		dingd.setDingdh(dingdh);// 订单号
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteDingd", dingd);			
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteDingdmx", dingdmx);		
	}
	
	
	/**
	 * 查询物流路径
	 * @param map 查询参数
	 * @return 物流路径信息
	 */
	public Wullj quertWullj(Map map){
		return (Wullj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.ShouhdclQueryWullj", map);
	}
	
	
	/**
	 * 校验拒收跟踪单和消耗点的仓库编号 0011223
	 * @param dingd 订单
	 * @return
	 */
	@Transactional
	public String checkLinsdd(String jiaofrq, Dingd dingd, List<Jusgzd> jusgzds, String creator) {
		String str = "";
		
		Map<String, String> dingdhMap = new HashMap<String, String>();
		Map<String, String> wulljmap = new HashMap<String, String>();
		
		for (int i = 0; i < jusgzds.size(); i++) {
			Jusgzd jusgzd = jusgzds.get(i);
			Wullj wl=null;
			if(Const.ANX_MS_CD.equals(jusgzd.getGonghms())){
				//校验消耗点 物流路径
				wulljmap.put("usercenter", dingd.getUsercenter());
				wulljmap.put("lingjbh", dingd.getLingjbh());
				wulljmap.put("xiaohdbh", dingd.getMdd());
				wulljmap.put("waibms", jusgzd.getGonghms());
				wl = (Wullj) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.ShouhdclCheckXhd", wulljmap);
				 
				if (wl != null) {
						//xss-0011223
						String ck = jusgzd.getCangkbh();//拒收跟踪单中的仓库
										
						if(ck!=null && !ck.equals(wl.getMudd()) ){
								str = "0";//不一致
						}else{
								str = "1";
						}
					}else {
						throw new RuntimeException("创建临时订单失败,物流路径为空!");
						//str = "2";
					}
		   }
		}
		return str;
	}
}
