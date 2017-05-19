package com.athena.xqjs.module.ilorder.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Gongyzq;
import com.athena.xqjs.entity.common.Gongyzx;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiaohcyssk;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.common.Yunssk;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.module.anxorder.service.AnxMaoxqService;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.common.service.CalendarCenterService;
import com.athena.xqjs.module.common.service.GongysService;
import com.athena.xqjs.module.common.service.LingjGongysService;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.common.service.WulljService;
import com.athena.xqjs.module.common.service.XiaohcysskService;
import com.athena.xqjs.module.common.service.XqjsLingjckService;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.toft.utils.JSONUtils;
import com.toft.utils.json.JSONException;

/**
 * <p>
 * IL周期订单修改及生效
 * </p>
 * 
 * @author NIESY
 * 
 * @date 2012-03-26
 */
@SuppressWarnings("rawtypes")
@Component
public class IleditAndEffectService extends BaseService {
	@Inject
	private GongyzqService gongyzqService;

	@Inject
	private GongyzxService gongyzxService;

	@Inject
	private CalendarCenterService centerService;

	@Inject
	private DingdmxService dingdmxService;
	
	@Inject
	private DingdService dingdservice;
	
	@Inject 
	private DingdljService dingdljService;

	// 日志打印信息
	private final Log log = LogFactory.getLog(IleditAndEffectService.class);

	
	@Inject
	private LingjGongysService lingjGongysService;
	@Inject
	private LingjService lingjService;
	@Inject
	private GongysService gService;
	@Inject
	private LingjxhdService lingjxhdService;
	@Inject
	private XiaohcysskService xService;
	@Inject
	private WulljService wulljService;// 柔性比例service
	@Inject
	private XqjsLingjckService ljck;

	@Inject
	private AnxMaoxqService anxMaoxqService;
	@Inject
	private IlOrderService ilOrderService;
	
	/**
	 * <p>
	 * 查询IL订单
	 * </p>
	 * 
	 * @param page
	 * @param map
	 * @return
	 */
	public Map<String, Object> selectDd(Pageable page, Map<String, String> map) {
		String chullx = map.get("chullx");
		if (chullx != null && chullx.equalsIgnoreCase("C")) {
			chullx = " (t.chullx like '%C%' or t.chullx like '%M%')";
		} else {
			chullx = "chullx = '" + chullx + "'";
		}
		map.put("chullx", chullx);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryDd", map, page);
	}

	@SuppressWarnings("unchecked")
	public List<Dingd> selectDdLs(Map<String, String> map) {
		String chullx = map.get("chullx");
		if (chullx != null && chullx.equalsIgnoreCase("C")) {
			chullx = " ((t.chullx like '%C%' and t.chullx != 'CV') or " +
			"(t.chullx like '%M%' and t.chullx != 'MJ' and t.chullx != 'MV'))";
		} else if (chullx != null) {
			if (chullx.equals("PP")) {
				chullx = " (chullx = '" + chullx + "' or chullx = 'NP')";
			} else if (chullx.equals("PS")) {
				chullx = "(chullx = '" + chullx + "' or chullx = 'NS')";
			} else if (chullx.equals("PJ")) {
				chullx = "(chullx = '" + chullx + "' or chullx = 'NJ')";
			} else {
				chullx = "(chullx = '" + chullx + "')";
			}

		}
		map.put("chullx", chullx);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDdBean", map);
	}
	
/**********  hzg 添加方法 2015/07/14 start *******/	
	/**
	 * 按需订单数量
	 * @author 贺志国
	 * @date 2015-7-14
	 * @param map
	 * @return
	 */
	public String selectAnxDdCount(Map<String, String> map) {
		String chullx = map.get("chullx");
		//订单类型为按需，"C"表示按需所有的类型C1,CD,M1,MD
		if (chullx != null && chullx.equalsIgnoreCase("C")) {
			chullx = " ('C1','CD','M1','MD','CV','MV')";
		}
		map.put("chullx", chullx);
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryAnxDdCount", map);
	}
	
	
	/**
	 * 按需订单流水查询
	 * @author 贺志国
	 * @date 2015-7-14
	 * @param map 页面参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Dingdmx> selectAnxDdLs(Map<String, String> map) {
		String chullx = map.get("chullx");
		//订单类型为按需，"C"表示按需所有的类型C1,CD,M1,MD
		if (chullx != null && chullx.equalsIgnoreCase("C")) {
			chullx = " ('C1','CD','M1','MD','CV','MV')";
		}
		map.put("chullx", chullx);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryExpAnxOrder", map);
	}

/**********  hzg 添加方法 2015/07/14  end *******/		
	
	/**
	 * <p>
	 * 查询IL订单,不分页
	 * </p>
	 * 
	 * @param page
	 * @param map
	 * @return
	 */
	public Map<String, Object> selectDd(Map<String, String> map, Pageable bean) {
		Map<String, Object> res = new HashMap<String, Object>();
		String chullx = map.get("chullx");
		if (chullx != null && chullx.equalsIgnoreCase("C")) {
			chullx = " ((t.chullx like '%C%' and t.chullx != 'CV') or " +
			"(t.chullx like '%M%' and t.chullx != 'MJ' and t.chullx != 'MV'))";
		} else if (chullx != null) {
			if(chullx.equals("PP")){
				chullx = " (chullx = '" + chullx + "' or chullx = 'NP')";
			}else if(chullx.equals("PS")){
				chullx = "(chullx = '" + chullx + "' or chullx = 'NS')";
			}else if(chullx.equals("PJ")){
				chullx = "(chullx = '" + chullx + "' or chullx = 'NJ')";
			}else {
				chullx = "(chullx = '" + chullx + "')";
			}
			
		}
		map.put("chullx", chullx);
		res = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryDdNojihyz", map, bean);
		List ls = (List) res.get("rows");
		List jihyzList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDdmxJihyz");
		Map alljzmap = new HashMap();
		for(int i=0;i<jihyzList.size();i++){
			Map jihyzmap = (Map)jihyzList.get(i);
			String dingdh = (String)jihyzmap.get("DINGDH");
			String usercenter = (String)jihyzmap.get("USERCENTER");
			String jihyz = (String)jihyzmap.get("JIHYZ");
			String key = usercenter+":"+dingdh;
			if(alljzmap.get(key)==null){
				alljzmap.put(key, jihyz);
			}else if(alljzmap.get(key)!=null&&!"".equals(alljzmap.get(key))){
				String values = (String)alljzmap.get(key)+",";
				values +=jihyz;
				alljzmap.put(key, values);
			}
		}
		int lssize = ls.size();
		List newlist = new ArrayList();
		for(int i=0;i<lssize;i++){
			Map ddmap = (Map)ls.get(i);
			String dingdh = (String)ddmap.get("DINGDH");
			String usercenter = (String)ddmap.get("USERCENTER");
			String key = usercenter+":"+dingdh;
			String tempValue = "";
			if(alljzmap.get(key)!=null&&!"".equals(alljzmap.get(key))){
				tempValue = (String)alljzmap.get(key);
			}
			ddmap.put("JIHYZ", tempValue);
			try {
				String json = JSONUtils.toJSON(ddmap);
				newlist.add(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// res.put("total", lssize);
		res.put("rows", newlist );
		return res;
	}

	/**
	 * <p>
	 * 查询IL订单零件
	 * </p>
	 * 
	 * @param page
	 * @param map
	 * @return
	 */
	public Map<String, Object> selectDalj(Pageable page, Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryAllDingdlj", map, page);
	}

	/**
	 * <p>
	 * 查询IL订单明细
	 * </p>
	 * 
	 * @param page
	 * @param map
	 * @return
	 */
	public Map<String, Object> selectDaMx(Pageable page, Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryDingdmx", map, page);
	}

	/**
	 * <p>
	 * 查询要导出的IL生效的订单
	 * </p>
	 * 
	 * @param page
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectEeDdmx(Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryDdmxEffect", map);
	}

	/**
	 * <p>
	 * 删除订单
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public boolean deleteDd(List<Dingd> ls, String newEditor, String editTime) {
		for (int i = 0, len = ls.size(); i < len; i++) {
			Dingd dd = ls.get(i);
			// 订单零件
			Dingdlj ddlj = new Dingdlj();
			ddlj.setDingdh(dd.getDingdh());
			// 订单明细
			Dingdmx dmx = new Dingdmx();
			// 订单号
			dmx.setDingdh(dd.getDingdh());
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteEeDd", dd);
			// 订单零件
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteEeDdlj", ddlj);
			// 订单明细
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteEeDdmx", dmx);

			if (count == 0) {
				throw new ServiceException(MessageConst.DELETE_COUNT_0);
			}
		}
		return true;
	}

	/**
	 * <p>
	 * 删除订单零件
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public boolean deleteDdLj(List<Dingdlj> ls) {
		for (int i = 0, len = ls.size(); i < len; i++) {
			Dingdlj dd = ls.get(i);
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteEeDdlj", dd);
			if (count == 0) {
				throw new ServiceException(MessageConst.DELETE_COUNT_0);
			}
		}
		return true;
	}

	/**
	 * <p>
	 * 删除订单明细
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public boolean deleteDdMx(List<Dingdmx> ls) {
		for (int i = 0, len = ls.size(); i < len; i++) {
			Dingdmx dd = ls.get(i);
			if(dd.getJiaofrq() != null){
				dd.setJiaofrq(dd.getJiaofrq().substring(0,10));
			}
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.deleteEeDdmx", dd);
			if (count == 0) {
				throw new ServiceException(MessageConst.DELETE_COUNT_0);
			}
		}
		return true;
	}

	/**
	 * <p>
	 * 修改订单状态（待生效、生效、拒绝）
	 * </p>
	 * 
	 * @param ls
	 * @param newEditor
	 * @param editTime
	 * @return
	 */
	@Transactional
	public String updateDaStatus(List<Dingd> ls, String newEditor, String editTime, String flag) {
		String result = "修改成功!";
		List list = new ArrayList();
		for (int i = 0, len = ls.size(); i < len; i++) {
			Dingd dd = ls.get(i);
			dd.setNewEditor(newEditor);
			dd.setNewEditTime(editTime);
			String dingdzt = dd.getDingdzt();
			String zhuangt = "";
			String dingdh = dd.getDingdh();
			int j = 0;
			// 0待生效1生效2拒绝
			if(!list.contains(dingdh)){
				list.add(dingdh);
				if (flag.equalsIgnoreCase(Const.DINGD_STATUS_YDY)) {
					if (dingdzt.equalsIgnoreCase(Const.DINGD_STATUS_YDY)
							|| dingdzt.equalsIgnoreCase(Const.DINGD_STATUS_ZZZ)) {
						// 待生效
						dd.setDingdzt(Const.DINGD_STATUS_DSX);
						j = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateUnEe", dd);
						zhuangt = Const.DINGD_STATUS_DSX;
					} else {
						throw new ServiceException("只能待生效制作中的订单！");
					}
				} else if (flag.equalsIgnoreCase(Const.DINGD_STATUS_ZZZ)) {
					if (dingdzt.equalsIgnoreCase(Const.DINGD_STATUS_DSX)) {
						// 生效
						dd.setDingdzt(Const.DINGD_STATUS_YSX);
						dd.setDingdsxsj(editTime.substring(0, 10));
						j = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateUnEe", dd);
						zhuangt = Const.DINGD_STATUS_YSX;
						Map<String,String> map = new HashMap<String,String>();
						map.put("dingdh", dd.getDingdh());
						Dingd ddBack = this.dingdservice.queryDingdByDingdh(map);
						if(ddBack.getMaoxqbc()!=null&&!"".equals(ddBack.getMaoxqbc())){
							String [] banc = ddBack.getMaoxqbc().split(",");
							for(int x = 0;x<banc.length;x++){
								map.clear();
								map.put("xuqbc", banc[x]);
								baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateMaoxq",map);
							}
							
						}
					} else {
						throw new ServiceException("只能生效状态为待生效的订单！");
					}
				} else if (flag.equalsIgnoreCase(Const.DINGD_STATUS_DSX)) {
					if (dingdzt.equalsIgnoreCase(Const.DINGD_STATUS_DSX)) {
						// 拒绝
						dd.setDingdzt(Const.DINGD_STATUS_JUJ);
						j = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateUnEe", dd);
						zhuangt = Const.DINGD_STATUS_JUJ;
					} else {
						throw new ServiceException("只能拒绝状态为待生效的订单！");
					}
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("zhuangt", zhuangt);
				// 当前更新人
				map.put("newEditor", newEditor);
				// 当前更新时间
				map.put("newEditTime", editTime);
				// 订单号
				map.put("dingdh", dd.getDingdh());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.editDdmx", map);
				// 如果更新行数为，则抛出异常
				if (j == 0) {
					throw new ServiceException(MessageConst.UPDATE_COUNT_0);
				}
			}
		}
		return result;
	}

	/**
	 * <p>
	 * 修改临时订单状态（待生效、生效）
	 * </p>
	 * 
	 * @param ls
	 * @param newEditor
	 * @param editTime
	 * @return
	 * xh  0715
	 */
	@Transactional
	public String updateLsStatus(Dingd bean, String newEditor, String editTime, String flag) {
		bean.setNewEditor(newEditor);
		bean.setNewEditTime(editTime);
		String dingdzt = bean.getDingdzt();
		String zhuangt = "";
			int j = 0;
			// 0待生效1生效2拒绝
				if (flag.equalsIgnoreCase(Const.DINGD_STATUS_YDY)) {
					if (dingdzt.equalsIgnoreCase(Const.DINGD_STATUS_YDY)
							|| dingdzt.equalsIgnoreCase(Const.DINGD_STATUS_ZZZ)) {
						// 待生效
						bean.setDingdzt(Const.DINGD_STATUS_DSX);
						j = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateUnEe", bean);
						zhuangt = Const.DINGD_STATUS_DSX;
					} else {
						throw new ServiceException("只能待生效制作中的订单！");
					}
				} else if (flag.equalsIgnoreCase(Const.DINGD_STATUS_ZZZ)) {
					if (dingdzt.equalsIgnoreCase(Const.DINGD_STATUS_DSX)) {
						// 生效
						bean.setDingdzt(Const.DINGD_STATUS_YSX);
						bean.setDingdsxsj(editTime.substring(0, 10));
						j = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateUnEe", bean);
						zhuangt = Const.DINGD_STATUS_YSX;
						Map<String,String> map = new HashMap<String,String>();
						map.put("dingdh", bean.getDingdh());
						/*Dingd ddBack = this.dingdservice.queryDingdByDingdh(map);
						if(ddBack.getMaoxqbc()!=null&&!"".equals(ddBack.getMaoxqbc())){
							String [] banc = ddBack.getMaoxqbc().split(",");
							for(int x = 0;x<banc.length;x++){
								map.clear();
								map.put("xuqbc", banc[x]);
								baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateMaoxq",map);
							}
							
						}*/
					} else {
						throw new ServiceException("只能生效状态为待生效的订单！");
					}
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("zhuangt", zhuangt);
				// 当前更新人
				map.put("newEditor", newEditor);
				// 当前更新时间
				map.put("newEditTime", editTime);
				// 订单号
				map.put("dingdh", bean.getDingdh());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.editDdmx", map);
				// 如果更新行数为，则抛出异常
				if (j == 0) {
					throw new ServiceException(MessageConst.UPDATE_COUNT_0);
				}

		return zhuangt;
	}
	
	
	/**
	 * <p>
	 * 新增订单
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public String insDingdIl(List<Dingd> ls, String creator, String time, String dingdh) {
		String result = "新增成功!";
		for (int i = 0, len = ls.size(); i < len; i++) {
			Dingd lj = ls.get(i);
			this.insDingdIl(lj, creator, time, dingdh);
		}
		return result;
	}

	/**
	 * <p>
	 * 新增订单
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public String insDingdIl(Dingd lj, String creator, String time, String dingdh) {
		String result = "新增成功!";
		lj.setCreator(creator);
		lj.setCreate_time(time);
		lj.setEdit_time(time);
		lj.setEditor(creator);
		lj.setDingdh(dingdh);
		lj.setShiffsgys(lj.getShiffsgys());
		if (StringUtils.isEmpty(lj.getDingdjssj())) {
			lj.setDingdjssj(time.substring(0, 19));
		}
		lj.setActive("1");
		String dingdzt = Const.DINGD_STATUS_ZZZ;
		lj.setDingdzt(dingdzt);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.addDingd", lj);
		return result;
	}

	/**
	 * <p>
	 * 新增订单零件
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public String insDingdljIl(List<Dingdlj> ls, String creator, String time) {
		String result = "新增成功!";
		for (int i = 0, len = ls.size(); i < len; i++) {
			Dingdlj lj = ls.get(i);
			lj.setCreator(creator);
			lj.setCreate_time(time);
			lj.setEdit_time(time);
			lj.setEditor(creator);
			lj.setActive("1");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.addDingdLj", lj);
		}
		return result;
	}

	/**
	 * <p>
	 * 新增订单明细
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public String insDingdmxIl(List<Dingdmx> ls, String creator, String time) {
		String result = "新增成功!";
		for (int i = 0, len = ls.size(); i < len; i++) {
			this.insDingdmxIl(ls.get(i), creator, time);
		}
		return result;
	}

	/**
	 * <p>
	 * 新增订单明细
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public String insDingdmxIl(Dingdmx mx, String creator, String time) {
		String result = "新增成功!";
		mx.setCreator(creator);
		mx.setCreate_time(time);
		mx.setEdit_time(time);
		mx.setEditor(creator);
		mx.setActive("1");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.addDingdh", mx);
		return result;
	}

	/**
	 * <p>
	 * 新增订单零件
	 * </p>
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public String insDingdljIl(Dingdlj lj, String creator, String time) {
		String result = "新增成功!";
		if (lj.getGonghms().indexOf("N") != -1) {
			lj.setCangkdm("");
		}
		lj.setCreator(creator);
		lj.setCreate_time(time);
		lj.setEdit_time(time);
		lj.setEditor(creator);
		lj.setActive("1");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.addDingdLj", lj);
		return result;
	}

	/**
	 * <p>
	 * 订单明细修改
	 * </p>
	 * <p>
	 * 数量与日期
	 * </p>
	 * 
	 * @author NIESY
	 * @param ls
	 * @return
	 */
	@Transactional
	public String updateDdmxSl(List<Dingdmx> ls, String newEditor, String newEditTime) {
		String result = "修改成功！";
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0, len = ls.size(); i < len; i++) {
			Dingdmx ddmx = ls.get(i);
			// 零件数量
			map.put("shul", ddmx.getShul());
			// 交付日期
			map.put("jiaofrq", ddmx.getJiaofrq());
			map.put("editor", ddmx.getEditor());
			map.put("edit_time", ddmx.getEdit_time());
			// 当前更新人
			map.put("newEditor", newEditor);
			//预计交付实际
			map.put("zuiwdhsj", ddmx.getZuiwdhsj());
			//预计上线时间
			map.put("xiaohcsxsj", ddmx.getXiaohcsxsj());
			// 当前更新时间
			map.put("newEditTime", newEditTime);
			// id
			map.put("id", ddmx.getId());
			map.put("leix", ddmx.getLeix());
             //PJ订单 计算最早最晚到货时间
			if(("PJ".equals(ddmx.getGonghlx()) || "VJ".equals(ddmx.getGonghlx())) 
					&& "9".equals(ddmx.getLeix())){
				Map map1=new HashMap();
				map1.put("usercenter", ddmx.getUsercenter());
				map1.put("xiehztbh",ddmx.getXiehzt());
				map1.put("shengcxbh", ddmx.getXiehzt().substring(0, 4));
				//查询运输时刻的发运周期
				map1.put("lingjbh", ddmx.getLingjbh());
				map1.put("gongysbh", ddmx.getGongysdm());
				map1.put("mudd",ddmx.getCangkdm());
				Xiehzt xiehzt = anxMaoxqService.queryXiehztObject(map1);
				if (xiehzt == null) {
					
				}else{
					BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
				    map1.put("juedsk", ddmx.getJiaofrq());
					map1.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));//允许提前到货时间
					String zuizdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map1));
					if(StringUtils.isBlank(zuizdhsj)){
						zuizdhsj=ddmx.getJiaofrq();
					}
					ddmx.setZuizdhsj(zuizdhsj);
					ddmx.setZuiwdhsj(ddmx.getJiaofrq());
					map.put("zuiwdhsj", ddmx.getJiaofrq());
					map.put("zuizdhsj", zuizdhsj);
				}
				map1.remove("shengcxbh");
				map1.put("waibms", ddmx.getGonghlx());
				if (ddmx.getFayrq() == null || ddmx.getFayrq().equals("")) {
					List<Wullj> wulljxx =ilOrderService.queryWulljxx(map1);
					if(wulljxx.size()>0){
						 Wullj wullj= wulljxx.get(0);
						 BigDecimal yunszq = (wullj.getYunszq()).multiply(BigDecimal.valueOf(24*60));
						 String faysj=DateUtil.DateSubtractMinutes(ddmx.getJiaofrq(),yunszq.intValue());
						 map.put("fayrq",faysj);
					}	
				}else {
					map.put("fayrq", ddmx.getFayrq());
				}
		    }
			if (ddmx.getFayrq() == null || ddmx.getFayrq().equals("")) {
			}else {
				map.put("fayrq", ddmx.getFayrq());
			}
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.editDdmx", map);
			if (count == 0) {
				throw new ServiceException();
			}
			map.clear();
		}
		return result;
	}

	/**
	 * <p>
	 * 订单零件修改
	 * </p>
	 * <p>
	 * 数量与日期
	 * </p>
	 * 
	 * @author NIESY
	 * @param ls
	 * @return
	 */
	@Transactional
	public String updateDdljSl(List<Dingdlj> ls, String newEditor, String newEditTime) {
		String result = "修改成功！";
		for (int i = 0, len = ls.size(); i < len; i++) {
			this.updateDdljSl(ls.get(i), newEditor, newEditTime);
		}
		return result;
	}

	/**
	 * <p>
	 * 订单零件修改
	 * </p>
	 * <p>
	 * 数量与日期
	 * </p>
	 * 
	 * @author NIESY
	 * @param ls
	 * @return
	 */
	@Transactional
	public String updateDdljSl(Dingdlj ddlj, String newEditor, String newEditTime) {
		String result = "修改成功！";
		Map<String, Object> map = new HashMap<String, Object>();
		// 零件数量p0
		map.put("p0sl", ddlj.getP0sl());
		// 零件数量p1
		map.put("p1sl", ddlj.getP1sl());
		// 零件数量p2
		map.put("p2sl", ddlj.getP2sl());
		// 零件数量p3
		map.put("p3sl", ddlj.getP3sl());
		map.put("editor", ddlj.getEditor());
		map.put("edit_time", ddlj.getEdit_time());
		// 当前更新人
		map.put("newEditor", newEditor);
		// 当前更新时间
		map.put("newEditTime", newEditTime);
		// id
		map.put("id", ddlj.getId());
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.upDdljsl", map);
		if (count == 0) {
			throw new ServiceException();
		}
		return result;
	}

	/**
	 * <p>
	 * 订单零件修改
	 * </p>
	 * <p>
	 * 数量与日期
	 * </p>
	 * 
	 * @author NIESY
	 * @param ls
	 * @return
	 */
	@Transactional
	public String updateDdljSl(Map<String, Object> map) {
		String result = "修改成功！";
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.upDdljsl", map);
		if (count == 0) {
			throw new ServiceException();
		}
		return result;
	}

	/**
	 * <p>
	 * 订单零件修改
	 * </p>
	 * <p>
	 * 数量与日期
	 * </p>
	 * 
	 * @author NIESY
	 * @param ls
	 * @return
	 */
	@Transactional
	public String updateDdljSl(Map<String, Object> map, String newEditor, String newEditTime) {
		String result = "修改成功！";
		// 当前更新人
		map.put("newEditor", newEditor);
		// 当前更新时间
		map.put("newEditTime", newEditTime);
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.upDdljsl", map);
		if (count == 0) {
			throw new ServiceException();
		}
		return result;
	}

	/**
	 * <p>
	 * 汇总IL/按需订单明细至订单零件,推出p0-p3的周期序号
	 * <p>
	 * 
	 * @author NIESY
	 * @param ddmx
	 * @return
	 */
	public Map<String, String> getPartsDate(Dingdlj ddlj) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("dingdh", ddlj.getDingdh());
		// Map<String, Object> dd = (Map<String, Object>)
		// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDd", map);
		// 处理类型
		String chullx = ddlj.getGonghms();
		map.put("usercenter", ddlj.getUsercenter());
		String ksrq = "";
		String jsrq = "";
		String p0zqxh = ddlj.getP0fyzqxh();
		// 按处理类型拼接条件
		if (chullx.equalsIgnoreCase(Const.PP) || chullx.equalsIgnoreCase("NP")) {
			// 工业周期
			List<Gongyzq> gyzq = gongyzqService.queryGongyzqbyZQ(p0zqxh);
			// 周期类型
			for (int i = 0; i <= 3; i++) {
				ksrq = gyzq.get(i).getKaissj();
				jsrq = gyzq.get(i).getJiessj();
				map.put("P" + i, this.joinSqlDate(ksrq, jsrq));
				map.put("P" + i + "fw", ksrq + "_" + jsrq);
			}
			log.info(map);
		} else if (chullx.equalsIgnoreCase(Const.PS) || chullx.equalsIgnoreCase("NS")) {
			// 工业周序
			List<Gongyzx> gyzx = gongyzxService.queryGongyzxByApointZx(p0zqxh);
			// 周类型
			for (int i = 0; i <= 3; i++) {
				ksrq = gyzx.get(i).getKaissj();
				jsrq = gyzx.get(i).getJiessj();
				map.put("P" + i, this.joinSqlDate(ksrq, jsrq));
				map.put("P" + i + "fw", ksrq + "_" + jsrq);
			}
			log.info(map);
		} else if (chullx.equalsIgnoreCase(Const.PJ) || chullx.indexOf("C") != -1 || chullx.indexOf("M") != -1 || chullx.equalsIgnoreCase("NJ")|| chullx.equalsIgnoreCase("VJ")) {
			// 日类型or按需
			String p0rq = "to_char(jiaofrq,'yyyy-MM-dd')='" + p0zqxh + "'";
			// p1
			// String p1 = this.getNextDate(p0zqxh);
			String p1rq = "to_char(jiaofrq,'yyyy-MM-dd')='" + ddlj.getP1rq() + "'";
			// p2
			// String p2 = this.getNextDate(p1);
			String p2rq = "to_char(jiaofrq,'yyyy-MM-dd')='" + ddlj.getP2rq() + "'";
			// p3
			// String p3 = this.getNextDate(p2);
			String p3rq = "to_char(jiaofrq,'yyyy-MM-dd')='" + ddlj.getP3rq() + "'";
			map.put("P0", p0rq);
			map.put("P0fw", p0zqxh);
			map.put("P1", p1rq);
			map.put("P1fw", ddlj.getP1rq());
			map.put("P2", p2rq);
			map.put("P2fw", ddlj.getP2rq());
			map.put("P3", p3rq);
			map.put("P3fw", ddlj.getP3rq());
		}
		map.put("lingjbh", ddlj.getLingjbh());
		// 得到日期
		return map;
	}

	/**
	 * <p>
	 * 拼接日期的SQL语句
	 * </p>
	 * 
	 * @param ksrq
	 * @param jsrq
	 * @author NIESY
	 * @return
	 */
	public String joinSqlDate(String ksrq, String jsrq) {
		return "to_char(jiaofrq,'yyyy-mm-dd') between '" + ksrq + "' and  '" + jsrq + "'";
	}

	/**
	 * <p>
	 * 根据指定日期得到下一天的日期
	 * </p>
	 * 
	 * @author NIESY
	 * @return
	 */
	public String getNextDate(String rq) {
		// 时间格式
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sf.format(sf.parse(rq));
		} catch (ParseException e) {
			log.error("日期时间解析错误", e);
			throw new ServiceException("日期时间解析错误", e);
		}
		// 获取日历
		Calendar calendar = sf.getCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		return sf.format(calendar.getTime());
	}

	/**
	 * <p>
	 * 汇总IL/按需订单明细至订单零件
	 * <p>
	 * 
	 * @author NIESY
	 * @param ddmx
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> sumDdmxToLj(Map<String, String> map) {
		// 汇总订单明细
		return (Map<String, Object>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.hzdingdh", map);
	}

	/**
	 * 保存临时订单
	 * 
	 * @param ding
	 *            订单信息
	 * @param creator
	 *            创建人
	 * @param time
	 *            创建时间
	 */
	public void saveDingdIl(Dingd ding) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.addDingd", ding);
	}

	// 创建临时看板的订单号生成
	public String getMaxKlddh(String dingdh) {
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ilorder.queryMaxKlddh", dingdh);
	}

	/**
	 * 筛选看板循环编码
	 */
	@SuppressWarnings("unchecked")
	public List<String> getKanbxhbm(Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("ilorder.querykanbxhbm", map);
	}

	/**
	 * <p>
	 * 国产件订单导出
	 * </p>
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> exportOrder(Map<String, String> map, Dingd bean) {
		Map<String, Object> resMap = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryExpAllOrder", map, bean);
		List<Map<String, Object>> ls = (List<Map<String, Object>>) resMap.get("rows");
		if (ls.size() == 0) {
			return null;
		}
		/*List<Map<String, String>> colHead = new ArrayList<Map<String, String>>();
		Map<String, String> col = new HashMap<String, String>();
		col.put("jihyz", "计划员代码");
		col.put("gongysdm", "供应商代码");
		col.put("dingdh", "订单号");
		col.put("lingjbh", "零件号");
		col.put("p0fyzqxh", "既定开始日期");
		col.put("jiszq", "订单周期");
		col.put("dingdzt", "订单状态");
		
		setColHead(ls, col, chullx);
		colHead.add(col);*/
		String chullx = map.get("chullx");
		List<Gongyzq> lszq = gongyzqService.queryGongyzq(map);
		Map<String, Gongyzq> zqmap = new HashMap<String, Gongyzq>();
		for (int i = 0; i < lszq.size(); i++) {
			zqmap.put(lszq.get(i).getGongyzq(), lszq.get(i));
		}
		lszq = null;
		List<Gongyzx> lszx = gongyzxService.queryGongyzx();
		Map<String, Gongyzx> zxmap = new HashMap<String, Gongyzx>();
		for (int i = 0; i < lszx.size(); i++) {
			zxmap.put(lszx.get(i).getGongyzx(), lszx.get(i));
		}
		lszx = null;
		for (int i = 0; i < ls.size(); i++) {
			Map<String, Object> map2 = ls.get(i);
			boolean flag = map2.get("P0FYZQXH") != null;
			if (chullx.equalsIgnoreCase(Const.PP) && flag) {
				// 工业周期
				map2.put("P0FYZQXH", zqmap.get(map2.get("P0FYZQXH")).getKaissj().replaceAll("-", ""));
			} else if (chullx.equalsIgnoreCase(Const.PS) && flag) {
				// 工业周序
				map2.put("P0FYZQXH", zxmap.get(map2.get("P0FYZQXH")).getKaissj().replaceAll("-", ""));
			} else if (chullx.equalsIgnoreCase(Const.PJ) && flag) {
				map2.put("P0FYZQXH", map2.get("P0FYZQXH").toString().replaceAll("-", ""));
			}

		}
		resMap.remove("rows");
		resMap.put("list", ls);
		//resMap.put("colHead", colHead);
		return resMap;
	}

	public void setColHead(List<Map<String, Object>> ls, Map<String, String> col, String chullx) {

		String jdstr = "既定";
		String ygstr = "预告";
		if (chullx.equalsIgnoreCase(Const.PP)) {
			// 工业周期
			List<Gongyzq> gyzq = gongyzqService.queryGongyzqbyZQ(ls.get(0).get("P0FYZQXH").toString());
			// 周期类型
			for (int i = 0; i <= 3; i++) {
				String pgyzq = gyzq.get(i).getGongyzq();
				String str = "月份" + jdstr;
				if (i != 0) {
					str = "月份" + ygstr;
				}
				col.put("p" + i + "sl", pgyzq.substring(pgyzq.length() - 2) + str);
			}

		} else if (chullx.equalsIgnoreCase(Const.PS)) {
			// 工业周序
			List<Gongyzx> gyzx = gongyzxService.queryGongyzxByApointZx(ls.get(0).get("P0FYZQXH").toString());
			// 周类型
			for (int i = 0; i <= 3; i++) {
				String pgyzx = gyzx.get(i).getGongyzx();
				String str = "周" + jdstr;
				if (i != 0) {
					str = "周" + ygstr;
				}
				col.put("p" + i + "sl", pgyzx.substring(pgyzx.length() - 2) + str);
			}
		} else if (chullx.equalsIgnoreCase(Const.PJ)) {
			// 日类型or按需
			String p0rq = (String) ls.get(0).get("P0FYZQXH");
			// p1
			String p1 = (String) ls.get(0).get("P0RQ");
			// p2
			String p2 = (String) ls.get(0).get("P1RQ");
			// p3
			String p3 = (String) ls.get(0).get("P3RQ");
			String pjStr = "日";
			col.put("p0sl", p0rq.substring(p0rq.length() - 2) + pjStr + jdstr);
			col.put("p1sl", p1.substring(p1.length() - 2) + pjStr + ygstr);
			col.put("p2sl", p2.substring(p2.length() - 2) + pjStr + ygstr);
			col.put("p3sl", p3.substring(p3.length() - 2) + pjStr + ygstr);
		}
	}

	/**
	 * <p>
	 * 查询国产件订单号-导出
	 * </p>
	 * 
	 * @param pageable
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryExportDd(Pageable pageable, Map<String, String> map) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryExpDindh", map, pageable);
	}

	/**
	 * <p>
	 * 临时订单实时根据订单明细状态更新订单状态
	 * </p>
	 * <p>
	 * 针对生效操作
	 * </p>
	 * 
	 * @param dingdh
	 */
	public void dynamicUpdateStatus(String dingdh, String edit_time, String editor) {
		Dingd dd = new Dingd();
		dd.setDingdh(dingdh);
		dd.setNewEditor(editor);
		dd.setNewEditTime(edit_time);
		int count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryLsdingdmx", dingdh);
		if (count == 0) {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.updateUnEe", dd);
		}
	}

	/**
	 * <p>
	 * 汇总IL/按需订单明细至订单零件,推出p0-p3的周期序号
	 * <p>
	 * 
	 * @author NIESY
	 * @param ddmx
	 * @return
	 */
	public Map<String, String> getOrderPartsDate(String p0fyzqxh, String chullx, String usercenter) {
		Map<String, String> map = new HashMap<String, String>();
		// 按处理类型拼接条件
		if (chullx.equalsIgnoreCase(Const.PP) || chullx.equalsIgnoreCase("NP")) {
			// 工业周期
			List<Gongyzq> gyzq = gongyzqService.queryGongyzqbyZQ(p0fyzqxh);
			// 周期类型
			for (int i = 0; i <= 3; i++) {
				map.put("" + i, gyzq.get(i).getGongyzq());
				map.put("qsrq" + i, gyzq.get(i).getKaissj());
				map.put("jsrq" + i, gyzq.get(i).getJiessj());
			}
			log.info(map);
		} else if (chullx.equalsIgnoreCase(Const.PS) || chullx.equalsIgnoreCase("NS")) {
			// 工业周序
			List<Map<String, Object>> gyzx = centerService.getWorkWeeks(p0fyzqxh, usercenter, "5");
			// 周类型
			for (int i = 0; i <= 3; i++) {
				map.put("" + (i + 1), gyzx.get(i).get("NIANZX").toString());
				Gongyzx zx = gongyzxService.queryGongyzx(gyzx.get(i).get("NIANZX").toString());
				map.put("qsrq" + (i + 1), zx.getKaissj());
				map.put("jsrq" + (i + 1), zx.getJiessj());
			}
			log.info(map);
		} else if (chullx.equalsIgnoreCase(Const.PJ) || chullx.indexOf("C") != -1 || chullx.indexOf("M") != -1 || chullx.equalsIgnoreCase("NJ")) {
			// 日类型or按需
			// 工业周序
			List<Map<String, Object>> gzr = centerService.getWorkDays(p0fyzqxh, usercenter, "5");
			for (int i = 0; i <= 3; i++) {
				map.put("" + (i + 1), gzr.get(i).get("RIQ").toString());
				map.put("qsrq" + (i + 1), gzr.get(i).get("RIQ").toString());
				map.put("jsrq" + (i + 1), gzr.get(i).get("RIQ").toString());
			}
		}
		// 得到日期
		return map;
	}

	/**
	 * 插入预告明细
	 * 
	 * @param bean
	 */
	@Transactional
	public void insertYugMx(Dingdlj bean) {
		Map<String, String> map = this.getOrderPartsDate(bean.getP0fyzqxh(), bean.getGonghms(), bean.getUsercenter());
		for (int i = 0; i < 3; i++) {
			Dingdmx mx = new Dingdmx();
			mx.setUsercenter(bean.getUsercenter());
			mx.setCangkdm(bean.getCangkdm());
			mx.setActive("1");
			String time = CommonFun.getJavaTime();
			mx.setCreate_time(time);
			mx.setCreator(bean.getCreator());
			mx.setEdit_time(time);
			mx.setEditor(bean.getCreator());
			mx.setDingdh(bean.getDingdh());
			mx.setGongysdm(bean.getGongysdm());
			mx.setDanw(bean.getDanw());
			mx.setLingjbh(bean.getLingjbh());
			mx.setLeix("8");
			mx.setId(getUUID());
			mx.setGonghlx(bean.getGonghms());
			mx.setGonghms(bean.getGonghms());
			mx.setGongsmc(bean.getGongsmc());
			mx.setGongyfe(bean.getGongysfe());
			mx.setGongyslx(bean.getGongyslx());
			mx.setJiaofrq(map.get("" + (i + 1)));
			mx.setYaohqsrq(map.get("qsrq" + (i + 1)));
			mx.setYaohjsrq(map.get("jsrq" + (i + 1)));
			dingdmxService.doInsert(mx);
		}

	}

	/***
	 * @author wuyichao
	 * @see  添加订单明细
	 * @param bean
	 * @param dingdlj
	 * @return
	 * @throws ParseException 
	 */
	public boolean addDdMx(Dingdmx bean, Dingdlj dingdlj) throws ParseException {
		bean.setGongsmc(dingdlj.getGongsmc());
		bean.setGongysdm(dingdlj.getGongysdm());
		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put("usercenter", bean.getUsercenter());
		searchMap.put("lingjbh", bean.getLingjbh());
		searchMap.put("cangkbh", bean.getCangkdm());
		Lingjck lingjck = ljck.querySingle(searchMap);
		if(null != lingjck)
		{
			bean.setXiehzt(lingjck.getXiehztbh());
		}
		else
		{
			return false;
		}
		bean.setLujdm(dingdlj.getLujdm());
		bean.setActive("1");
		bean.setCreate_time(CommonFun.getJavaTime());
		bean.setEdit_time(CommonFun.getJavaTime());
		bean.setZhuangt(Const.DINGD_STATUS_DSX);
		if (!bean.getGonghlx().equals("M1") && !bean.getGonghlx().equals("MD") && !bean.getGonghlx().equals("MJ")) {
			Gongys gys = gService.queryObject(bean.getGongysdm(), bean.getUsercenter());
			bean.setGongyslx(gys.getLeix());
			bean.setGongsmc(gys.getGongsmc());
			bean.setNeibyhzx(gys.getUsercenter());
			bean.setGcbh(gys.getGcbh());
			searchMap.put("gongysbh", bean.getGongysdm());
			List<Wullj> wulljs = wulljService.queryWulljList(searchMap);
			if(null != wulljs && wulljs.size() > 0)
			{
				String flag = wulljs.get(0).getGcbh();
				for (Wullj wullj : wulljs) {
					if(!(null != wullj.getGcbh() && wullj.getGcbh().equalsIgnoreCase(flag)))
					{
						return false;
					}
				}
				bean.setGcbh(flag);
			}
			else
			{
				return false;
			}
			LingjGongys ljgys = lingjGongysService.select(bean.getUsercenter(), bean.getGongysdm(), bean.getLingjbh());
			bean.setGongyfe(ljgys.getGongyfe());
			bean.setUabzlx(ljgys.getUabzlx());
			bean.setUabzucsl(ljgys.getUaucgs());
			bean.setUabzuclx(ljgys.getUcbzlx());
			bean.setUabzucrl(ljgys.getUcrl());
		}

		if (bean.getGonghlx().indexOf("M") != -1 || bean.getGonghlx().indexOf("C") != -1) {
			String flag = this.setAxsj(bean, !StringUtils.isEmpty(bean.getXiaohsj()));
			if (!StringUtils.isEmpty(flag)) {
				return false;
			}
		}
		// 订货车间
		Lingj lingj = lingjService.select(bean.getUsercenter(), bean.getLingjbh());
		bean.setDinghcj(lingj == null ? null : lingj.getDinghcj());
		bean.setLingjmc(lingj == null ? null : lingj.getZhongwmc());
		bean.setJihyz(lingj == null ? null : lingj.getJihy());
		bean.setJissl(bean.getShul());
		this.dingdmxService.doInsert(bean);
		
		return true;
	}

	// 临时订单按需，根据消耗时间推算出预计交付时间和预计上线时间
	public String setAxsj(Dingdmx bean, boolean flag) throws ParseException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		String beihsj = null;
		if (bean.getGonghlx().equals("MD") || bean.getGonghlx().equals("CD")) {
			map.put("xiaohdbh", bean.getXiaohd());
			map.put("gongysbh", bean.getGongysdm());
			Lingjxhd lingjxhd = lingjxhdService.queryLingjxhdObject(map);
			// 如果小火车时刻表没有数据,跳过该条记录,不予计算
			if (lingjxhd == null) {
				return "未找到该消耗点";
			}
			if (flag) {
				bean.setFenpxh(bean.getXiaohd().substring(0, 5));
				bean.setXianbyhlx(lingjxhd.getXianbyhlx());
				map.put("xiaohcbh", lingjxhd.getXiaohcbh());
				map.put("shengcxbh", lingjxhd.getShengcxbh());
				map.put("kaisbhsj", bean.getXiaohsj().substring(0, 16));
				bean.setXiaohch(lingjxhd.getXiaohcbh());
				bean.setChanx(lingjxhd.getShengcxbh());
				// 取小火车运输时刻
				List<Xiaohcyssk> Listxhcyssk = xService.queryXiaohcysskObject(map);
				// 如果小火车时刻表没有数据,跳过该条记录,不予计算
				if (Listxhcyssk == null || Listxhcyssk.isEmpty()) {
					return "未查到相关小火车信息";
				}
				Xiaohcyssk xiaohcyssk = Listxhcyssk.get(1);
				// 备货时间
				beihsj = xiaohcyssk.getKaisbhsj();
				bean.setXiaohcbhsj(beihsj);
				// 上线时间
				String shangxsj = xiaohcyssk.getChufsxsj();
				bean.setXiaohcsxsj(shangxsj.substring(0, 16));
				if (beihsj == null) {
					return "小火车运输时刻,小火车编号" + lingjxhd.getXiaohcbh() + "生产线编号" + lingjxhd.getShengcxbh() + "备货时间为空";
				}
			}

		} else {
			beihsj = bean.getXiaohsj();
		}
		// 预计到货时间(yujdhsj)=小火车备货时间-内部物流时间
		Wullj wullj=null;
		String gcbh = null;
		if (bean.getGonghlx().equals("CD")) {
			map.put("fenpqh", bean.getXiaohd().substring(0, 5));
			map.put("waibms", bean.getGonghlx());
			wullj= wulljService.queryWulljObject(map);
			gcbh = wullj == null ? null : wullj.getGcbh();
			bean.setCangkdm(wullj == null ? null : wullj.getMudd());
		} else if (bean.getGonghlx().equals("C1") || bean.getGonghlx().equals("CV")) {
			map.put("gongysbh", bean.getGongysdm());
			map.put("waibms", bean.getGonghlx());
			map.put("mudd", bean.getCangkdm());
			wullj = wulljService.queryWulljObjectLsaxc1(map);
			gcbh = wullj == null ? null : wullj.getGcbh();
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
		}else if(bean.getGonghlx().equals("MD")){
			map.put("mos", "MD");
			map.put("dinghck", bean.getGongysdm());
			map.put("fenpqh", bean.getXiaohd().substring(0, 5));
			wullj = wulljService.queryWulljObjectLsaxck(map);
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

		if (flag) {
			BigDecimal neibwlsj = wullj.getBeihsjc();
			// 如果备货时间为空
			if (neibwlsj == null) {
				return "内部物流时间C为空";
			}
			/**
			 * 获取到最晚到货时间
			 * */
			Date dates = CommonFun.yyyyMMddHHmm.parse(beihsj);
			dates.setTime(dates.getTime() - neibwlsj.intValue() * 60 * 1000);
			map.put("usercenter", bean.getUsercenter());
			map.put("xiehztbh", wullj.getXiehztbh());
			map.put("gcbh", gcbh);
			String zuiwdhsj = CommonFun.yyyyMMddHHmm.format(dates);
			map.put("daohsj", zuiwdhsj);
			/**
			 * 从卸货站台得到提前到货时间
			 */
			Xiehzt xiehzt = anxMaoxqService.queryXiehztObject(map);
			BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
			if (yunxtqdhsj == null) {
				return "卸货站台允许提前到货时间";
			}
			List<Yunssk> listYunssk = (List<Yunssk>) anxMaoxqService.queryYunssk(map);
			if (listYunssk != null && !listYunssk.isEmpty()) {
				zuiwdhsj = listYunssk.get(0).getDaohsj().substring(0, 16);
			}
			Date zuizDate = new Date();
			zuizDate.setTime(CommonFun.yyyyMMddHHmm.parse(zuiwdhsj).getTime() - yunxtqdhsj.intValue() * 60 * 1000);

			// 最早到货时间
			String zuizdhsj = CommonFun.yyyyMMddHHmm.format(zuizDate);
			bean.setZuizdhsj(zuizdhsj);
			bean.setZuiwdhsj(zuiwdhsj);
		}
		bean.setFayrq(bean.getZuiwdhsj());
		bean.setJiaofrq(bean.getZuiwdhsj());
		return "";
	}

	public Map<String, Object>  selectDdByPage(Map<String, String> map, Dingd bean) {
		Map<String, Object> res = new HashMap<String, Object>();
		String chullx = map.get("chullx");
		if (chullx != null && chullx.equalsIgnoreCase("C")) {
			chullx = " ((t.chullx like '%C%' and t.chullx != 'CV') or " +
					"(t.chullx like '%M%' and t.chullx != 'MJ' and t.chullx != 'MV'))";
		} else if (chullx != null) {
			if(chullx.equals("PP")){
				chullx = " (chullx = '" + chullx + "' or chullx = 'NP')";
			}else if(chullx.equals("PS")){
				chullx = "(chullx = '" + chullx + "' or chullx = 'NS')";
			}else if(chullx.equals("PJ")){
				chullx = "(chullx = '" + chullx + "' or chullx = 'NJ')";
			}else {
				chullx = "(chullx = '" + chullx + "')";
			}
			
		}
		map.put("chullx", chullx);
		res = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ilorder.queryDdNojihyz", map, bean);
		List ls = (List) res.get("rows");
		List newlist = new ArrayList();
		
		int lssize = ls.size();
		Map<String, String> searchMap = new HashMap<String, String>();
		List<Dingdlj> dingdljs = null;
		
		for(int i=0;i<lssize;i++){
			Map ddmap = (Map)ls.get(i);
			String tempValue = "";
			searchMap.put("dingdh", (String)ddmap.get("DINGDH"));
			dingdljs = dingdljService.queryAllJihyByDingd(searchMap);
			if(null != dingdljs && dingdljs.size() > 0)
			{
				for(int j = 0 ; j <dingdljs.size();j++)
				{
					tempValue += dingdljs.get(j).getJihyz();
					if(j != dingdljs.size() - 1)
					{
						tempValue += ",";
					}
				}
			}
			ddmap.put("JIHYZ", tempValue);
			try {
				String json = JSONUtils.toJSON(ddmap);
				newlist.add(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		res.put("rows", newlist );
		return res;
	}
}
