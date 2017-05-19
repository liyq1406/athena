package com.athena.xqjs.module.kanbyhl.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.LingjGongysService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.common.service.XqjsAuthority;
import com.athena.xqjs.module.common.service.XqjsLingjckService;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 看板手工设置
 * 
 * @author Xiahui
 * @date 2012-2-9
 */
@Component
public class KanbsgszService extends BaseService {
	@Inject
	private XqjsAuthority xqjsAuthority;
	// 注入看板计算service
	@Inject
	private KanbjsService kanbjsService;

	@Inject
	private LingjGongysService lGongysService;

	@Inject
	private XqjsLingjckService ljck;

	@Inject
	private LingjxhdService ljxhd;

	/**
	 * 查询物流路径获取未计算的看板循环
	 * 
	 * @param page
	 * @param param
	 * @return Map<String ,Object>
	 */
	public Map<String, Object> select(Pageable page, Map<String, String> param, LoginUser loginUser) {
		boolean ispoa = xqjsAuthority.isPoa(loginUser);
		// 查询不同的供货模式
		String gonghms = param.get("gonghms") == null ? "" : param.get("gonghms");
		// 供货模式为R1
		if (gonghms.equals(Const.KANB_JS_WAIBMOS_R1)) {
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kanbyhl.selectR1", param, page);
			// 供货模式为R2
		} else if (gonghms.equals(Const.KANB_JS_WAIBMOS_R2)) {
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kanbyhl.selectR2", param, page);
			// 供货模式为RD
		} else if (gonghms.equals(Const.KANB_JS_NEIBMOS_RD)) {
			wulgyyzDto(param, ispoa, gonghms);
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kanbyhl.selectRD", param, page);
			// 供货模式为RM
		} else if (gonghms.equals(Const.KANB_JS_NEIBMOS_RM)) {
			wulgyyzDto(param, ispoa, gonghms);
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kanbyhl.selectRM", param, page);
		} else {
			// 无模式，返回空
			return null;
		}
	}

	private void wulgyyzDto(Map<String, String> param, boolean ispoa, String gonghms) {
		boolean flag = gonghms.equalsIgnoreCase("RD");
		if (ispoa && param.get("kehd") == null) {
			param.put("kehd", "");
		} else if (!ispoa && param.get("kehd") == null) {
			List<Map<String, Object>> ls = xqjsAuthority.getWulgyyz(gonghms, "");
			StringBuffer kehd = new StringBuffer();
			if (flag) {
				kehd.append("substr(x.xiaohdbh,0,5)");
			} else {
				kehd.append("w.xianbck");
			}
			for (int i = 0; i < ls.size(); i++) {
				Map<String, Object> qxmap = ls.get(i);
				String khvalue = qxmap.get("KEH").toString();
				if (i == 0) {
					kehd.append(" in(");
				}
				if (i != ls.size() - 1) {
					kehd.append("'" + khvalue + "',");
				} else {
					kehd.append("'" + khvalue + "')");
				}
			}
			param.put("kehd", ls.size() == 0 ? kehd.toString() + "=''" : kehd.toString());
		} else if (param.get("kehd") != null && flag) {
			List<Map<String, Object>> ls = xqjsAuthority.getWulgyyz(gonghms, param.get("kehd"));
			if (ls.size() != 0) {
				param.put("kehd", "substr(x.xiaohdbh,0,5)='" + param.get("kehd") + "'");
			} else {
				param.put("kehd", "xiaohdbh=''");
			}
		} else if (param.get("kehd") != null && !flag) {
			List<Map<String, Object>> ls = xqjsAuthority.getWulgyyz(gonghms, param.get("kehd"));
			if (ls.size() != 0) {
				param.put("kehd", "w.xianbck='" + param.get("kehd") + "'");
			} else {
				param.put("kehd", "w.xianbck=''");
			}
		}
	}

	/**
	 * 批量插入看板循环规模
	 * 
	 * @param ls
	 * @return flag
	 */
	@Transactional
	public String doInsert(List<Kanbxhgm> ls) {
		// 默认为真
		String flag = "true";
		Map<String,Integer> mapXunhbmKeyCount = new HashMap<String, Integer>();
		//记录key 多对应的所有循环编号（已经使用过得循环编码）
		Map<String,Map<String,Integer>> map = new HashMap<String, Map<String,Integer>>();
		for (int i = 0; i < ls.size(); i++) {
			Kanbxhgm bean = ls.get(i);
			String xunhbmKey  = "" ;
			if(bean.getGonghms().equals("R1") ||
					bean.getGonghms().equals("RD")){
				xunhbmKey =  bean.getUsercenter().substring(1, 2) 
				              + bean.getChanx().substring(2, 3)
				              + bean.getChanx().substring(4, 5);
			}else if(bean.getGonghms().equals("R2") ||
					bean.getGonghms().equals("RM")){
				xunhbmKey = bean.getKehd();
			}
			if(!map.containsKey(xunhbmKey)){
				@SuppressWarnings("unchecked")
				List<String> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.xhbmList",xunhbmKey);
				map.put(xunhbmKey, getListToMap(list));
			}
			if(!mapXunhbmKeyCount.containsKey(xunhbmKey)){
				mapXunhbmKeyCount.put(xunhbmKey, 0);
			}
			// 编码后五位最大流水号
			Integer xhNum = mapXunhbmKeyCount.get(xunhbmKey)+1 ;
			if(map.get(xunhbmKey).keySet().size() > 0){
				xhNum = getValueByMap(xhNum , map.get(xunhbmKey));
			}
			mapXunhbmKeyCount.put(xunhbmKey, xhNum);	
			// 产生循环编码
//			String xunhbm = kanbjsService.xhbmGeneration(ls.get(i));
			String xunhbm = xunhbmKey + String.format("%05d", xhNum);
			// 设置循环编码
			ls.get(i).setXunhbm(xunhbm);
			// 插入成功则返回真，插入错误则抛出异常，无返回值
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.insertKanbxhgm", ls.get(i));
		}
		return flag;
	}

	/**
	 * 校验R1/R2看板卡创建UA容量
	 * 
	 * @param ls
	 * @param mos
	 * @param time
	 * @param loginUser
	 * @return
	 */
	public Object checkKbWbCreate(List<Kanbxhgm> ls, String time, LoginUser loginUser) {
		StringBuffer sb = new StringBuffer();
			for (int i = 0; i < ls.size(); i++) {
				Kanbxhgm kb = ls.get(i);
				// 当前创建人
				kb.setCreator(loginUser.getUsername());
				// 当前创建时间
				kb.setCreate_time(time);
				// 当前修改时间
				kb.setWeihsj(time);
				// 当前修改人
				kb.setWeihr(loginUser.getUsername());
				// 设置生效状态
				kb.setShengxzt(Const.KANBXH_WEISX);
				kb.setDangqxhgm(BigDecimal.ZERO);
				kb.setLeix("1");
				kb.setDongjjdzt("1");
				kb.setShifzdfs(0);
			List<LingjGongys> ljgys = lGongysService.query(kb.getUsercenter(), kb.getLingjbh(), Const.ACTIVE_1);
				if(ljgys.size()>0&&(ljgys.get(0).getUcrl()==null||ljgys.get(0).getUaucgs()==null)){
				sb.append("用户中心").append(kb.getUsercenter()).append(",").append("零件号").append(kb.getLingjbh()).append(",").append("UA包装容量为空").append("\n");
				} else {
					kb.setUmlx(ljgys.get(0).getUabzlx());
				kb.setUclx(ljgys.get(0).getUcbzlx());
				kb.setUmljsl(ljgys.get(0).getUaucgs().multiply(ljgys.get(0).getUcrl()));
					kb.setUcrl(ljgys.get(0).getUcrl());
				kb.setUmzucgs(ljgys.get(0).getUaucgs());
				}
			}
		return StringUtils.isEmpty(sb.toString()) ? ls : sb.toString();
	}

	/**
	 * 校验RM仓库US容量
	 * 
	 * @param ls
	 * @param mos
	 * @param time
	 * @param loginUser
	 * @return
	 */
	public Object checkKbRmCreate(List<Kanbxhgm> ls, String time, LoginUser loginUser) {
		StringBuffer sb = new StringBuffer();
			for (int i = 0; i < ls.size(); i++) {
				Kanbxhgm kb = ls.get(i);
				// 当前创建人
				kb.setCreator(loginUser.getUsername());
				// 当前创建时间
				kb.setCreate_time(time);
				// 当前修改时间
				kb.setWeihsj(time);
				// 当前修改人
				kb.setWeihr(loginUser.getUsername());
				// 设置生效状态
				kb.setShengxzt(Const.KANBXH_WEISX);
				kb.setDangqxhgm(BigDecimal.ZERO);
				kb.setLeix("1");
				kb.setDongjjdzt("1");
				kb.setShifzdfs(0);
				Map<String, String> map = new HashMap<String, String>();
				map.put("usercenter", kb.getUsercenter());
				map.put("lingjbh", kb.getLingjbh());
				map.put("cangkbh", kb.getCangkdm());
				Lingjck lingjck = ljck.querySingle(map);
				if (lingjck.getUsbzrl() == null) {
				sb.append("用户中心").append(kb.getUsercenter()).append(",").append("零件号").append(kb.getLingjbh()).append(",").append("客户").append(kb.getCangkdm()).append("US包装容量为空").append("\n");
				} else {
					kb.setUclx(lingjck.getUsbzlx());
				kb.setUcrl(lingjck.getUsbzrl());
				}
			}
		return StringUtils.isEmpty(sb.toString()) ? ls : sb.toString();
	}

	/**
	 * 校验RD仓库UC/US容量
	 * 
	 * @param ls
	 * @param mos
	 * @param time
	 * @param loginUser
	 * @return
	 */
	public Object checkKbRdCreate(List<Kanbxhgm> ls, String time, LoginUser loginUser) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ls.size(); i++) {
			Kanbxhgm kb = ls.get(i);
			// 当前创建人
			kb.setCreator(loginUser.getUsername());
			// 当前创建时间
			kb.setCreate_time(time);
			// 当前修改时间
			kb.setWeihsj(time);
			// 当前修改人
			kb.setWeihr(loginUser.getUsername());
			// 设置生效状态
			kb.setShengxzt(Const.KANBXH_WEISX);
			kb.setDangqxhgm(BigDecimal.ZERO);
			kb.setLeix("1");
			kb.setDongjjdzt("1");
			kb.setShifzdfs(0);
			Map<String, String> map = new HashMap<String, String>();
			map.put("usercenter", kb.getUsercenter());
			map.put("lingjbh", kb.getLingjbh());
			map.put("xiaohdbh", kb.getXiaohd());
			Lingjxhd xhd = ljxhd.queryLingjxhdObject(map);
			if (xhd == null || (!"K".equals(xhd.getXianbyhlx()) && !"R".equals(xhd.getXianbyhlx()))) {
				sb.append("用户中心").append(kb.getUsercenter()).append(",").append("零件号").append(kb.getLingjbh()).append(",").append("客户").append(kb.getXiaohd()).append("消耗点线边要货类型不为K或R").append("\n");
			} else if(xhd.getXianbyhlx().equals("K")){
				map.put("cangkbh", kb.getXianbck());
				Lingjck ck = ljck.querySingle(map);
				if (ck.getUcrl() == null) {
					sb.append("用户中心").append(kb.getUsercenter()).append(",").append("零件号").append(kb.getLingjbh()).append(",").append("仓库编号").append(kb.getXiaohd()).append("仓库UC容量为空").append("\n");
				} else {
					kb.setUclx(ck.getUclx());
					kb.setUcrl(ck.getUcrl());
					kb.setXianbyhlx("K");
					kb.setYancsxbz(xhd.getYancsxbz());
				}
			} else if (xhd.getXianbyhlx().equals("R")) {
				map.put("cangkbh", kb.getXianbck());
				Lingjck ck = ljck.querySingle(map);
				if (ck.getUsbzrl() == null) {
					sb.append("用户中心").append(kb.getUsercenter()).append(",").append("零件号").append(kb.getLingjbh()).append("仓库编号").append(kb.getXiaohd()).append("仓库UC容量为空").append("\n");
				} else {
					kb.setUclx(ck.getUsbzlx());
					kb.setUcrl(ck.getUsbzrl());
					kb.setXianbyhlx("R");
					kb.setYancsxbz(xhd.getYancsxbz());
				}
			}
		}
		return StringUtils.isEmpty(sb.toString()) ? ls : sb.toString();
	}
	
	
	public Map<String,Integer> getListToMap(List<String> list){
		Map<String,Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i), Integer.parseInt(list.get(i)));
		}
		return map;
	}
	public Integer getValueByMap(int num,Map<String,Integer> map){
		while(true){
			if(num == 99999){
				return 0;
			}			
			String key = String.format("%05d", num) ; 
			if(!map.containsKey(key)){
				return num;
			}
			num++;
		}
		
	}
}
