/**
 * 
 */
package com.athena.xqjs.module.kdys.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.date.DateUtil;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.kdys.Inwuld;
import com.athena.xqjs.entity.kdys.KdysBean;
import com.athena.xqjs.entity.laxkaix.TC;
import com.athena.xqjs.module.laxkaix.service.LaxjhService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * @author dsimedd001
 * 
 */
@WebService(endpointInterface="com.athena.xqjs.module.kdys.service.Kdys", serviceName="/kdysService1")
@Component("kdysService1")
public class KdysService extends BaseService implements Kdys{

	// log4j日志初始化
	static Logger logger = Logger.getLogger(KdysService.class); 
	@Inject
	private YicbjService yicbjService;
	/**
	 * 获取Kdys更新信息 逻辑： KDYS更新后，将数据表更新进入In_wuld表中，将其发布成服务后，自动的读取In_wuld中的数据
	 * 
	 * @return
	 */
	public String getKdysInfo() {
		//获取kdys接口数据(KD件在途、物理点信息)
		List<Map<String, Object>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdys.selectInwuld");
		// 初始化标识为A的List集合
		List<Inwuld> insertTcList = new ArrayList<Inwuld>();
		// 初始化标识为B的List集合
		List<Inwuld> updateTcList = new ArrayList<Inwuld>();
		// 初始化标识为C的List集合
		List<Inwuld> updateTcWuldList = new ArrayList<Inwuld>();
		// 用户登录信息
		for (Map<String, Object> map : list) {
			// ID
			String id = (String) map.get("ID");
			// 标识
			String flag = (String) map.get("FALG");
			// PAP提单号
			String papSheetId = (String) map.get("PAP_SHEET_ID");
			// PAP集装箱号
			String papBoxId = (String) map.get("PAP_BOX_ID");
			// kdys提单号
			String kdysSheetId = (String) map.get("KDYS_SHEET_ID");
			// kdys集装箱号
			String kdysBoxId = (String) map.get("KDYS_BOX_ID");
			// 物理点
			String cPointId = (String) map.get("C_POINT_ID");
			// 时间
			String tTime = "";
			if (map.get("T_TIME") != null) {
				String temptTime = (String) map.get("T_TIME");
				Date temptTimeD;
				try {
					temptTimeD = DateUtil.stringToDateYMD(temptTime);
					tTime = DateUtil.dateFromat(temptTimeD, "yyyy-MM-dd");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			String cjDate = (String) map.get("CJ_DATE");
			// 状态
			String clzt = (String) map.get("CLZT");
			Inwuld inwuld = new Inwuld();
			inwuld.setId(id);
			inwuld.setFalg(flag);
			inwuld.setPapSheetId(papSheetId);
			inwuld.setPapBoxId(papBoxId);
			inwuld.setKdysSheetId(kdysSheetId);
			inwuld.setKdysBoxId(kdysBoxId);
			inwuld.setcPointId(cPointId);
			inwuld.settTime(tTime);
			inwuld.setCjDate(cjDate);
			inwuld.setCljt(clzt);
			/*
			 * 1、如果标识为A，则将数据加入集合insertTcList中 
			 * 2、标识为B，则将数据加入集合updateTcList中
			 * 3、标识为C，则将数据加入集合updateTcWuldList中
			 */
			if (flag != null && "A".equals(flag)) {
				insertTcList.add(inwuld);
			} else if (flag != null && "B".equals(flag)) {
				updateTcList.add(inwuld);
			} else if (flag != null && "C".equals(flag)) {
				updateTcWuldList.add(inwuld);
			}
		}
		// 插入TC信息
		this.insertTC(insertTcList);
		// 更新TC信息
		this.updateTC(updateTcList);
		// 更新TC物理点信息
		this.updateTCWuld(updateTcWuldList);
		return null;
	}

	/**
	 * 获取kdys更新信息 业务标识为A： 根据"KDYS提单号"和"KDYS集装箱号"两个维度存在则将现有文本插入数据表中
	 * 
	 * @param insertTcList
	 */
	@Transactional
	public void insertTC(List<Inwuld> insertTcList) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdys.deleteTc");
		List<TC> tcList = new ArrayList<TC>();
		for (Inwuld inwuld : insertTcList) {
			String createTime = DateUtil.curDateTime();
			// 获取流水号-流水号生成方式:当前日期+四位的流水号
			String id = this.getId(createTime);
			TC tc = new TC();
			tc.setId(id);
			// TC号
			tc.setTcNo(inwuld.getKdysBoxId());
			tc.setCreateTime(createTime);
			// pap提单号
			tc.setPapSheetId(inwuld.getPapSheetId());
			// pap集装箱号
			tc.setPapBoxId(inwuld.getPapBoxId());
			// kdys提单号
			tc.setKdysSheetId(inwuld.getKdysSheetId());
			// 看TC信息是否存在,如果不存在则进行新增
			TC newTc = this.getXqjsTc(tc);
			if (newTc == null) {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdys.insertTC", tc);
				tcList.add(tc);
			}
		}
	}

	/**
	 * 业务标识为B： 根据"PAP集装箱号"和"KDYS提单号"两个维度,更新"KDYS集装箱号"
	 * 
	 * @param updateTcList
	 */
	@Transactional
	private void updateTC(List<Inwuld> updateTcList) {
		String createTime = DateUtil.curDateTime();
		for (Inwuld inwuld : updateTcList) {
			// 初始化TC
			TC tc = new TC();
			// TC号
			tc.setTcNo(inwuld.getKdysBoxId());
			tc.setEditTime(createTime);
			// kyds提单号
			tc.setKdysSheetId(inwuld.getKdysSheetId());
			// pap集装箱号
			tc.setPapBoxId(inwuld.getPapBoxId());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdys.updateTC", tc);
		}
	}

	/**
	 * 业务标识为C： 根据"PAP提单号"和"PAP集装箱号"两个维度更新"时间"和"物理点"两个维度<BR/> <P/>
	 * 注意点： 如果C标识的信息在没有A的情况下，则将其信息先进行插入，再将其信息进行更新
	 * 
	 * @param updateTcWuldList 标识为C的List集合
	 */
	@Transactional
	public void updateTCWuld(List<Inwuld> updateTcWuldList) {
		// 当前时间
		String createTime = DateUtil.curDateTime();
		List<TC> tcList = new ArrayList<TC>();
		Map<String, String> wuldShunxhMap = this.getwuldList();
		Map<String, TC> tcMap = new HashMap<String, TC>();
		for (Inwuld inwuld : updateTcWuldList) {
		/*	// PAP集装箱号
			String papBoxId = inwuld.getPapBoxId();
			// kdys提单号
			String kdysSheetId = inwuld.getKdysSheetId();
			String tempKey = papBoxId + ":" + kdysSheetId;
			TC tjTc = new TC();
			// TC号
			tjTc.setTcNo(papBoxId);
			// kdys提单号
			tjTc.setKdysSheetId(kdysSheetId);
			// 判断TC信息是否为空,如果TC信息为空，则表示该数据为新TC信息，先将其插入
			TC tc = this.getXqjsTc(tjTc);
			if (tc == null) {
				tjTc.setPapBoxId(papBoxId);
				String id = this.getId(createTime);
				tjTc.setId(id);
				this.insertTCNew(tjTc);
				// 插入后，重新获取TC信息
				tc = this.getXqjsTc(tjTc);
			}
			if (tcMap.get(tempKey) != null) {
				tc = (TC) tcMap.get(tempKey); 
			}
			// 物理点
			String wuld = inwuld.getcPointId();
			// 物理点时间
			String wuldsj = inwuld.gettTime();*/
			TC tc = new TC();
			// 到达物理点时间
			tc.setDaodwldsj(inwuld.getcPointId());
			// 最新物理点
			tc.setZuiswld(inwuld.gettTime());
			tc.setEditTime(createTime);
			// TC号(KDYS集装箱号)
			tc.setTcNo(inwuld.getKdysBoxId());
			// PAP集装箱号
			tc.setPapBoxId(inwuld.getPapBoxId());
			// kdys提单号
			tc.setKdysSheetId(inwuld.getKdysSheetId());
			// 根据物理点编号获取顺序号
			String shunxh = wuldShunxhMap.get(inwuld.getcPointId());
			// 如果物理点顺序号在100到199之间，则表示该顺序号为启运点
			if (shunxh.compareTo("100") >= 0 && shunxh.compareTo("199") < 0) {
				tc.setQiysj(inwuld.gettTime());
				tc.setQiyd(inwuld.getcPointId());
			}
			String key = tc.getPapBoxId() + ":" + tc.getKdysSheetId();
			tcMap.put(key, tc);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdys.updateTCWuld", tc);
			tcList.add(tc);
		}
		this.updateTcProperty(tcList);
	}

	/**
	 * @param tc
	 */
	public void insertTCNew(TC tc) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdys.insertTC", tc);
	}

	/**
	 * 获取ID
	 * 
	 * @param createTime
	 * @return
	 */
	public String getId(String createTime) {
		// 获取yyyyMMdd时间
		String createTimeNoLine="";
		Map<String, String> map = new HashMap<String, String>();
		try {
			createTimeNoLine = DateUtil.StringFormatToString(createTime);
			map.put("createTimeNoLine", createTimeNoLine);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// 查询当天最大的TC号
		Map<String, String> tempIdMap = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kdys.getTcId", map);
		String tempId = tempIdMap.get("ID");
		String ids = "";
		String id = "";
		if (tempId == null || "".equals(tempId)) {
			ids = "0000";
		} else {
			// 获取其长度
			int length = tempId.length();
			// 获取其后4位
			String temp = tempId.substring(length - 4, length);
			// 去掉4位中的前面的零
			String newString = "";
			if (temp.equals("0000")) {
				newString = "0";
			} else {
				newString = LaxjhService.getNewString(temp);
			}
			// 变成整型+1;
			Integer idI = Integer.valueOf(newString) + 1;
			// 然后将其变成四位字符串，不够四位补零
			ids = String.format("%04d", idI);
		}
		id = createTimeNoLine + ids;
		return id;
	}

	/**
	 * 获取TC信息
	 * 
	 * @param inwuld
	 * @return
	 */
	public TC getXqjsTc(TC tc) {
		return (TC) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kdys.selectTc", tc);
	}

	/**
	 * 获取参考系所有物理点信息集合
	 * 
	 * @return map  k:物理点编号  v:顺序号
	 */
	public Map<String, String> getwuldList() {
		List<Map<String, String>> wuldlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdys.getYunswuld");
		Map<String, String> wuldShunxhMap = new HashMap<String, String>();
		for (Map<String, String> wuldMap : wuldlist) {
			// 物理点编号
			String wuldbh = "";
			if(wuldMap.get("WULDBH")!=null){
				wuldbh = wuldMap.get("WULDBH");
			}
			// 顺序号
			String shunxh = "";
			if(wuldMap.get("SHUNXH")!=null){
				shunxh = wuldMap.get("SHUNXH");
			}
			if(wuldbh!=null&&!"".equals(wuldbh)){
				wuldShunxhMap.put(wuldbh, shunxh);
			}
		}
		return wuldShunxhMap;
	}

	/**
	 * 更新完TCNO信息后，再更新其他字段: 1、UT号 2、启运点(发货地) 3、目的地(仓库编号) 4、预计到达神龙时间(最晚交付时间)
	 * 5、最新预计到达时间
	 * 
	 * @param tcList
	 * @param loginUser
	 */
	public void updateTcProperty(List<TC> tcList) {
		Map<String, TC> tcMap = this.getTcMap(tcList);
		/**
		 * 获取状态为2，3，4的到货通知单位信息 该数据集合中包括：
		 * ckbhMap:以usercenter+":"+uth为key,cangkbh为value
		 * zhuangtMap：以usercenter+":"+uth为key,zhuangt为value
		 * wuldMap：以usercenter+":"+uth为key,zuiswld为value 
		 * uttcList:将usercenter +":" + tcNo + ":" + uth为值，加入集合uttcList中
		 * utList:以usercenter+":"+uth为值，加入集合utList中
		 */
		KdysBean bean = this.getDaohtzdInfo(tcMap);
		List<String> utList = bean.getUtList();
		Map<String, String> ckbhMap = bean.getCkbhMap();
		Map<String, BigDecimal> zhuangtMap = bean.getZhuangtMap();
		List<String> uttcList = bean.getUttcList();
		Map<String, String> wuldMap = bean.getWuldMap();
		/**
		 * keyuaMap：以usercenter + ":" + uth为key,以 gongysdm + ":" + lingjbh + ":"
		 * + dingdh + ":" + yaohlh为value yaohlKeyList:以usercenter + ":" +
		 * yaohlh为值,加入集合yaohlKeyList中 yaohlUtMap：以usercenter + ":" +
		 * uth为key，以yaohlh为value
		 */
		KdysBean uabqBean = this.getUabqInfo();
		Map<String, String> keyuaMap = uabqBean.getKeyuaMap();
		List<String> yaohlKeyList = uabqBean.getYaohlKeyList();
		Map<String, String> yaohlUtMap = uabqBean.getYaohlUtMap();
		/**
		 * dingdljList集合中加入dingdlj信息 dingdljMap：以usercenter + ":" + gongysdm +
		 * ":" + lingjbh + ":" + dingdh;以uth + ":" + cangkbh为value
		 */
		KdysBean dingdljBean = this.getDingljInfo(utList, ckbhMap, keyuaMap);
		List<Dingdlj> dingdljList = dingdljBean.getDingdljList();
		Map<String, String> dingdljMap = dingdljBean.getDingdljMap();
		/**
		 * jihdhsysjMap中以lujbh+":"+wuldbh为key,jihdhsysj为value
		 */
		Map<String, BigDecimal> jihdhsysjMap = this.getJihdhsysj();
		/**
		 * utcangkMap中以uth+":"+cangkbh为key,fahd+":"+jihdhsysj+":"+dingdhcj+":"+
		 * lujdm为value
		 */
		Map<String, String> utcangkMap = this.getUtcangkMap(dingdljList, dingdljMap, wuldMap, jihdhsysjMap);
		/**
		 * 获取最晚将付时间信息
		 */
		Map<String, String> zuiwjfsjMap = new HashMap<String, String>();
		
		zuiwjfsjMap = this.getZuiwjifsjMap(yaohlKeyList);
	
		
		for (String uttcKey : uttcList) {
			String[] uttcKeys = uttcKey.split(":");
			// 用户中心
			String usercenter = "";
			if(uttcKeys[0]!=null&&!"".equals(uttcKeys[0])){
				usercenter = uttcKeys[0];
			}
			// TC号
			String tch = "";
			if(uttcKeys[1]!=null&&!"".equals(uttcKeys[1])){
				tch = uttcKeys[1];
			}else{
				logger.error("KDYS更新中查询UA标签时集装箱号为空");
			}
			// UT号
			String uth = "";
			if(uttcKeys[2]!=null&&!"".equals(uttcKeys[2])){
				uth = uttcKeys[2];
			}else{
				logger.error("KDYS更新中查询UA标签时UT号为空");
			}
			String keyUt = "";
			if(usercenter!=null&&!"".equals(usercenter)){
				keyUt = usercenter + ":" + uth;
			}
			// 仓库编号
			String cangkbh = "";
			if(ckbhMap.get(keyUt)!=null){
				cangkbh = ckbhMap.get(keyUt);
			}else{
				logger.error("KDYS更新中查询UA标签时仓库编号为空");
			}
			String tempValue = "";
			String keyUT = uth + ":" + cangkbh;
			if(utcangkMap.get(keyUT)!=null){
				tempValue = utcangkMap.get(keyUT);
			}
			String fahd = "";
			String jihdhsysj = "0";
			String dinghcj = "";
			String lujdm = "";
			if (tempValue != null && !"".equals(tempValue)) {
				String[] tempValues = tempValue.split(":");
				// 发货地
				fahd = tempValues[0];
				// 计划剩余时间
				if (tempValues[1] != null) {
					jihdhsysj = tempValues[1];
				}
				// 订货车间
				dinghcj = tempValues[2];
				// 路径代码
				lujdm = tempValues[3];
				// 要货令号
				String yaohlh = yaohlUtMap.get(usercenter + ":" + uth);
				// key:用户中心+":"+要货令号
				String key = usercenter + ":" + yaohlh;
				// 最晚时间
				String zuiwsj = zuiwjfsjMap.get(key);
				// 获取TC号信息
				TC tc = tcMap.get(tch);
				// 到达物理点时间
				String wuldsj = tc.getDaodwldsj();
				String zxyjddsj = "";
				if (!jihdhsysj.equals("0") && jihdhsysj != null && !"".equals(jihdhsysj)) {
					try {
						// 最晚预计到达时间
						zxyjddsj = DateUtil.dateAddDays(wuldsj, Integer.parseInt(jihdhsysj));
					} catch (Exception e) {
						logger.info(e.toString());
					}
				}
				// ut号
				tc.setUtNo(uth);
				// 发货地
				tc.setQiyd(fahd);
				// 目的地
				tc.setMudd(cangkbh);
				// 订货车间
				tc.setDinghcj(dinghcj);
				// 预计到达时间
				tc.setYujddsj(zuiwsj);
				// 最新预计到达时间
				tc.setZuixyjddsj(zxyjddsj);
				// 路径代码
				tc.setLujdm(lujdm);
				tc.setTczt("1");
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdys.updateTcInfo", tc);
				
				this.updateYoahl(zhuangtMap, keyUt, zxyjddsj, zuiwsj, tc, usercenter, yaohlh);
			}
		}
	}

	/**
	 * 更新要货令信息
	 */
	public void updateYoahl(Map<String, BigDecimal> zhuangtMap, String keyUt, String zxyjddsj, String zuiwsj, TC tc,
			String usercenter, String yaohlh) {
		BigDecimal zhuangt = BigDecimal.ZERO;
		// 获取到货通知单状态
		if (zhuangtMap.get(keyUt) != null) {
			zhuangt = zhuangtMap.get(keyUt);
		}
		Map<String, String> yaohlMap = new HashMap<String, String>();
		if (zhuangt.equals(new BigDecimal("2"))) {
			if (zxyjddsj != null && !"".equals(zxyjddsj)) {
				yaohlMap.put("xiughyjjfsj", zxyjddsj);
			}
			if (zxyjddsj.compareTo(zuiwsj) > 0) {
				yaohlMap.put("jiaofzt", "0");
			} else if (zxyjddsj.compareTo(zuiwsj) < 0) {
				yaohlMap.put("jiaofzt", "1");
			} else {
				yaohlMap.put("jiaofzt", "2");
			}
			yaohlMap.put("shijfysj", tc.getQiysj());
			yaohlMap.put("usercenter", usercenter);
			yaohlMap.put("yaohlh", yaohlh);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdys.updateYaohl", yaohlMap);
		}
	}

	/**
	 * 将tcList中的信息遍历出来，以tc号(tcNo)为key，tc对象为value
	 * 
	 * @param tcList
	 * @return
	 */
	public Map<String, TC> getTcMap(List<TC> tcList) {
		Map<String, TC> tcMap = new HashMap<String, TC>();
		for (TC tc : tcList) {
			String tcNo = tc.getTcNo();
			tcMap.put(tcNo, tc);
		}
		return tcMap;
	}

	/**
	 * 获取到货通知单信息 查询字段: 1、USERCENTER;2、TCH;3、UTH;4、CANGKBH;5、ZHUANGT 返回值
	 * ckbhMap:以usercenter+":"+uth为key,cangkbh为value
	 * zhuangtMap：以usercenter+":"+uth为key,zhuangt为value
	 * wuldMap：以usercenter+":"+uth为key,zuiswld为value uttcList:将usercenter + ":"
	 * + tcNo + ":" + uth为值，加入集合uttcList中
	 * utList:以usercenter+":"+uth为值，加入集合utList中
	 * 
	 * @param tcMap
	 * @return
	 */
	public KdysBean getDaohtzdInfo(Map<String, TC> tcMap) {
		Map<String, String> ckbhMap = new HashMap<String, String>();
		Map<String, BigDecimal> zhuangtMap = new HashMap<String, BigDecimal>();
		Map<String, String> wuldMap = new HashMap<String, String>();
		List<String> uttcList = new ArrayList<String>();
		List<String> utList = new ArrayList<String>();
		List<Map<String, Object>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdys.getDaohtzdInfo");
		for (Map<String, Object> resultMap : list) {
			String tcNo = (String) resultMap.get("TCH");
			if (tcMap.containsKey(tcNo)) {
				// TC号
				TC tc = tcMap.get(tcNo);
				// 用户中心
				String usercenter = "";
				if(resultMap.get("USERCENTER")!=null){
					usercenter = (String) resultMap.get("USERCENTER");
				}else{
					logger.error("KDYS更新中查询到货通知单时用户中心为空");
				}
				// UT号
				String uth = "";
				if(resultMap.get("UTH")!=null){
					uth = (String) resultMap.get("UTH");
				}else{
					logger.error("KDYS更新中查询到货通知单时UT号为空");
				}
				// 仓库编号
				String cangkbh = "";
				if(resultMap.get("CANGKBH")!=null){
					cangkbh = (String) resultMap.get("CANGKBH");
				}else{
					logger.error("KDYS更新中查询到货通知单时仓库编号为空");
				}
				// 状态
				BigDecimal zhuangt = BigDecimal.ZERO;
				if(resultMap.get("ZHUANGT")!=null){
					zhuangt = (BigDecimal) resultMap.get("ZHUANGT");
				}else{
					logger.error("KDYS更新中查询到货通知单时状态为空");
				}
				String keyUt = usercenter + ":" + uth;
				if (!utList.contains(keyUt)) {
					utList.add(keyUt);
				}
				ckbhMap.put(keyUt, cangkbh);
				zhuangtMap.put(keyUt, zhuangt);
				// 用户中心+":"+TC号+":"+UT号
				String uttcValue = usercenter + ":" + tcNo + ":" + uth;
				if (!uttcList.contains(uttcValue)) {
					uttcList.add(uttcValue);
				}
				wuldMap.put(keyUt, tc.getZuiswld());
			}
		}
		KdysBean bean = new KdysBean();
		bean.setUttcList(uttcList);
		bean.setCkbhMap(ckbhMap);
		bean.setZhuangtMap(zhuangtMap);
		bean.setWuldMap(wuldMap);
		bean.setUtList(utList);
		return bean;
	}

	/**
	 * 获取UABQ信息 查询字段: 1、USERCENTER;2、UTH;3、GONGYSDM;4、LINGJBH;5、DINGDH,YAOHLH
	 * 返回值: keyuaMap：以usercenter + ":" + uth为key,以 gongysdm + ":" + lingjbh +
	 * ":" + dingdh + ":" + yaohlh为value yaohlKeyList:以usercenter + ":" +
	 * yaohlh为值,加入集合yaohlKeyList中 yaohlUtMap：以usercenter + ":" +
	 * uth为key，以yaohlh为value
	 * 
	 * @return
	 */
	public KdysBean getUabqInfo() {
		List<Map<String, Object>> uaList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdys.getUabqInfo");
		Map<String, String> keyuaMap = new HashMap<String, String>();
		List<String> yaohlKeyList = new ArrayList<String>();
		Map<String, String> yaohlUtMap = new HashMap<String, String>();
		for (Map<String, Object> resultMap : uaList) {
			// 用户中心
			String usercenter = "";
			if(resultMap.get("USERCENTER")!=null){
				usercenter = (String) resultMap.get("USERCENTER");
			}else{
				logger.error("KDYS更新中查询UA标签时用户中心为空");
			}
			// UT号
			String uth = "";
			if( resultMap.get("UTH")!=null){
				uth = (String) resultMap.get("UTH");
			}else{
				logger.error("KDYS更新中查询UA标签时UT号为空");
			}
			// 供应商代码
			String gongysdm = "";
			if(resultMap.get("GONGYSDM")!=null){
				gongysdm = (String) resultMap.get("GONGYSDM");
			}else{
				logger.error("KDYS更新中查询UA标签时供应商为空");
			}
			// 零件编号
			String lingjbh = "";
			if(resultMap.get("LINGJBH")!=null){
				lingjbh = (String) resultMap.get("LINGJBH");
			}else{
				logger.error("KDYS更新中查询UA标签时零件编号为空");
			}
			// 订单号
			String dingdh = "";
			if(resultMap.get("DINGDH")!=null){
				dingdh = (String) resultMap.get("DINGDH");
			}else{
				logger.error("KDYS更新中查询UA标签时订单号为空");
			}
			// 要货令号
			String yaohlh = "";
			if(resultMap.get("YAOHLH")!=null){
				yaohlh = (String) resultMap.get("YAOHLH");
			}else{
				logger.error("KDYS更新中查询UA标签时要货令号为空");
			}
			String key = usercenter + ":" + uth;
			String value = gongysdm + ":" + lingjbh + ":" + dingdh + ":" + yaohlh;
			// 设置UAmap信息
			keyuaMap.put(key, value);
			// 要货令key
			String yaohlKey = usercenter + ":" + yaohlh;
			yaohlKeyList.add(yaohlKey);
			yaohlUtMap.put(usercenter + ":" + uth, yaohlh);
		}
		KdysBean bean = new KdysBean();
		bean.setKeyuaMap(keyuaMap);
		bean.setYaohlKeyList(yaohlKeyList);
		bean.setYaohlUtMap(yaohlUtMap);
		return bean;
	}

	/**
	 * 将上述方法getDaohtzdInf、getUabqInfo所返回的值进行处理: 返回: dingdljList集合中加入dingdlj信息
	 * dingdljMap：以usercenter + ":" + gongysdm + ":" + lingjbh + ":" +
	 * dingdh;以uth + ":" + cangkbh为value
	 * 
	 * @param utList
	 * @param ckbhMap
	 * @param keyuaMap
	 * @return
	 */
	public KdysBean getDingljInfo(List<String> utList, Map<String, String> ckbhMap, Map<String, String> keyuaMap) {
		List<String> tjDingdlingjList = new ArrayList<String>();
		for (String keyut : utList) {
			String[] keys = keyut.split(":");
			String usercenter = keys[0];
			String uth = keys[1];
			// 初始化条件MAP
			Map<String, String> tjMap = new HashMap<String, String>();
			// 用户中心
			tjMap.put("usercenter", usercenter);
			// UT号
			tjMap.put("uth", uth);
			// 仓库编号
			String cangkbh = (String) ckbhMap.get(keyut);
			String value = keyuaMap.get(keyut);
			// key:usercenter+":"+cangkbh+":"+gongysdm + ":" + lingjbh + ":" +
			// dingdh + ":" + yaohlh+"uth"
			String key = usercenter + ":" + cangkbh + ":" + value + ":" + uth;
			tjDingdlingjList.add(key);
		}
		List<Dingdlj> dingdljList = new ArrayList<Dingdlj>();
		Map<String, String> dingdljMap = new HashMap<String, String>();
		for (String key : tjDingdlingjList) {
			String[] keys = key.split(":");
			String usercenter = keys[0];
			// 仓库编号
			String cangkbh = keys[1];
			// 供应商代码
			String gongysdm = keys[2];
			// 零件编号
			String lingjbh = keys[3];
			// 订单号
			String dingdh = keys[4];
			Map<String, String> tjMap = new HashMap<String, String>();
			// 设置条件Map
			// 用户中心
			tjMap.put("usercenter", usercenter);
			// 仓库编号
			tjMap.put("cangkbh", cangkbh);
			// 供应商代码
			tjMap.put("gongysdm", gongysdm);
			// 零件编号
			tjMap.put("lingjbh", lingjbh);
			// 订单号
			tjMap.put("dingdh", dingdh);
			Dingdlj dingdlj = (Dingdlj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kdys.getDingdlj", tjMap);
			if (dingdlj != null) {
				dingdljList.add(dingdlj);
			}
			String uth = keys[6];
			String tempKey = usercenter + ":" + gongysdm + ":" + lingjbh + ":" + dingdh;
			dingdljMap.put(tempKey, uth + ":" + cangkbh);
		}
		KdysBean bean = new KdysBean();
		bean.setDingdljList(dingdljList);
		bean.setDingdljMap(dingdljMap);
		return bean;
	}

	/**
	 * 查询查询剩余时间 查询表：CKX_WAIBWLXX 查询字段: LUJBH,XUH,WULDBH,JIHDHSYSJ 返回值：
	 * jihdhsysjMap中以lujbh+":"+wuldbh为key,jihdhsysj为value
	 * 
	 * @return
	 */
	public Map<String, BigDecimal> getJihdhsysj() {
		List<Map<String, Object>> jihList = (List) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdys.getJihsysj");
		Map<String, BigDecimal> jihdhsysjMap = new HashMap<String, BigDecimal>();
		for (Map<String, Object> jihmap : jihList) {
			// 路径编号
			String lujbh = "";
			if(jihmap.get("LUJBH")!=null){
				lujbh = (String) jihmap.get("LUJBH");
			}else{
				logger.error("KDYS更新中查询外部物流详细信息时路径编号为空");
			}
			// 物理点编号
			String wuldbh = "";
			if(jihmap.get("WULDBH")!=null){
				wuldbh = (String) jihmap.get("WULDBH");;
			}else{
				logger.error("KDYS更新中查询外部物流详细信息时物理点编号为空");
			}
			// 计划剩余时间
			BigDecimal jihdhsysj = BigDecimal.ZERO;
			if(jihmap.get("JIHDHSYSJ")!=null){
				jihdhsysj = (BigDecimal) jihmap.get("JIHDHSYSJ");
			}else{
				logger.error("KDYS更新中查询外部物流详细信息时计划剩余时间为空");
			}
			String key = lujbh + ":" + wuldbh;
			jihdhsysjMap.put(key, jihdhsysj);
		}
		return jihdhsysjMap;
	}

	/**
	 * 返回值: utcangkMap中以uth+":"+cangkbh为key,fahd+":"+jihdhsysj+":"+dingdhcj+":"+
	 * lujdm为value
	 * 
	 * @param dingdljList
	 * @param dingdljMap
	 * @param wuldMap
	 * @param jihdhsysjMap
	 * @return
	 */
	public Map<String, String> getUtcangkMap(List<Dingdlj> dingdljList, Map<String, String> dingdljMap,
			Map<String, String> wuldMap, Map<String, BigDecimal> jihdhsysjMap) {
		Map<String, String> utcangkMap = new HashMap<String, String>();
		for (Dingdlj dingdlj : dingdljList) {
			// 用户中心
			String usercenter = dingdlj.getUsercenter();
			// 仓库
			String cangkbh = dingdlj.getCangkdm();
			// 供应商代码
			String gongysdm = dingdlj.getGongysdm();
			// 零件编号
			String lingjbh = dingdlj.getLingjbh();
			// 订单号
			String dingdh = dingdlj.getDingdh();
			// 发货地
			String fahd = dingdlj.getFahd();
			// 路径代码
			String lujdm = dingdlj.getLujdm();
			// 订货车间
			String dinghcj = dingdlj.getDinghcj();
			String tempKey = usercenter + ":" + gongysdm + ":" + lingjbh + ":" + dingdh;
			String tempValue = dingdljMap.get(tempKey);
			String[] tempValues = tempValue.split(":");
			String uth = tempValues[0];
			String key = usercenter + ":" + uth;
			String wuld = wuldMap.get(key);
			BigDecimal jihdhsysj = (BigDecimal) jihdhsysjMap.get(lujdm + ":" + wuld);
			utcangkMap.put(uth + ":" + cangkbh, fahd + ":" + jihdhsysj + ":" + dinghcj + ":" + lujdm);
		}
		return utcangkMap;
	}

	/**
	 * 条件:1、用户中心;2、要货令号 查询字段:ZUIWSJ(最晚交付时间)
	 * 
	 * @param yaohlKeyList
	 * @return
	 */
	public Map<String, String> getZuiwjifsjMap(List<String> yaohlKeyList) {
		Map<String, String> zuiwjfsjMap = new HashMap<String, String>();
		for (String yaohlhKey : yaohlKeyList) {
			String[] yaohlhKeys = yaohlhKey.split(":");
			String usercenter = "";
			if(yaohlhKeys[0]!=null){
				usercenter = yaohlhKeys[0];
			}
			String yaohlh = "";
			if(yaohlhKeys[1]!=null){
				yaohlh = yaohlhKeys[1];
			}
			Map<String, String> tjMap = new HashMap<String, String>();
			// 用户中心
			tjMap.put("usercenter", usercenter);
			// 要货令号
			tjMap.put("yaohlh", yaohlh);
			Map<String, Object> zwjfsjMap = new HashMap<String, Object>();
			// 最晚交付时间
			zwjfsjMap = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kdys.getZwjfsj", tjMap);
			String zuiwjfsj = "";
			if(zwjfsjMap.get("ZUIWSJ")!=null){
				zuiwjfsj = (String) zwjfsjMap.get("ZUIWSJ");
			}else{
				logger.error("KDYS更新中查询要货令信息时最晚时间为空");
			}
			zuiwjfsjMap.put(yaohlhKey, zuiwjfsj);
		}
		return zuiwjfsjMap;
	}

	/**
	 * @return
	 */
	public List<TC> getNewKdysInfo() {
		String createTime = DateUtil.curDateTime();
		String createTimeNoLine;
		Map<String, String> map = new HashMap<String, String>();
		try {
			createTimeNoLine = DateUtil.StringFormatToString(createTime);
			map.put("createTimeNoLine", createTimeNoLine);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return (List<TC>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kdys.selectNewTC", map);
	}
}
