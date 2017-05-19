/**
 * 
 */
package com.athena.xqjs.module.lingjcx.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.laxkaix.TC;
import com.athena.xqjs.module.lingjcx.service.LingjcxService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * @author dsimedd001
 * 
 */
@Component
public class LingjcxAction extends ActionSupport {
	@Inject
	private LingjcxService lingjcxService;

	/**
	 * 根据零件查询
	 * @param bean
	 * @return
	 */
	public String execute(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh) {
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("usercenter",loginUser.getUsercenter());
		return "select";
	}

	/**
	 * 执行零件查询页面
	 * 查询条件：
	 * 1、用户中心;2、零件编号
	 * @param bean
	 * @return
	 */
	public String queryLingjztcx(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh,@Param Maoxqmx mx) {
		setResult("result", lingjcxService.queryLingjcx(usercenter, lingjbh,mx,getParams()));
		return AJAX;
	}
	/**
	 * 根据物理点进行零件查询
	 * @param usercenter
	 * @param lingjbh
	 * @param wuld
	 * @return
	 */
	public String selectTCLingjByWuld(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh, @Param("wuld") String wuld) {
		this.setRequestAttribute("usercenter", usercenter);
		this.setRequestAttribute("lingjbh", lingjbh);
		this.setRequestAttribute("wuld", wuld);
		return "select";
	}
	/**
	 * 根据零件和TC信息进行查询(根据零件查询物理点击页面)
	 * @param usercenter
	 * @param lingjbh
	 * @param wuld
	 * @param tcNo
	 * @return
	 */
	public String selectLingjBytcNo(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh, @Param("wuld") String wuld,
			@Param("tcNo") String tcNo,@Param TC tc) {
		Map<String, String> tjMap = new HashMap<String, String>();
		tjMap.put("usercenter", usercenter);
		tjMap.put("lingjbh", lingjbh);
		tjMap.put("wuld", wuld);
		tjMap.put("tcNo", tcNo);
		setResult("result", lingjcxService.selectTCLingjByWuld(tc,tjMap,getParams()));
		return AJAX;
	}
	/**
	 * 查询要货令信息(根据零件查询)
	 * @param usercenter
	 * @param lingjbh
	 * @param tcNo
	 * @return
	 */
	public String selectYaohlBytcNo(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh, @Param("tcNo") String tcNo) {
		this.setRequestAttribute("usercenter", usercenter);
		this.setRequestAttribute("lingjbh", lingjbh);
		this.setRequestAttribute("tcNo", tcNo);
		return "select";
	}
	/**
	 * 根据要货令信息(根据零件查询)
	 * @param usercenter
	 * @param lingjbh
	 * @param tcNo
	 * @param yaohlh
	 * @param jiaofj
	 * @return
	 */
	public String selectYaohlByYaohlh(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh, @Param("tcNo") String tcNo,
			@Param("yaohlh") String yaohlh, @Param("jiaofj") String jiaofj,@Param TC tc) {
		Map<String, String> tjMap = new HashMap<String, String>();
		tjMap.put("usercenter", usercenter);
		tjMap.put("lingjbh", lingjbh);
		tjMap.put("tcNo", tcNo);
		tjMap.put("yaohlh", yaohlh);
		tjMap.put("jiaofj", jiaofj);
		setResult("result", lingjcxService.selectYaohlByYaohlh(tjMap,tc,getParams()));
		return AJAX;
	}
	/**
	 * 拉箱开箱中根据TC信息查询要货令
	 * @param usercenter
	 * @param lingjbh
	 * @param tcNo
	 * @return
	 */
	public String selectYaohlBylaxkaix(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh, @Param("tcNo") String tcNo) {
		this.setRequestAttribute("usercenter", usercenter);
		this.setRequestAttribute("tcNo", tcNo);
		return "select";
	}
	/**
	 * 根据零件+订单号查询
	 * @param bean
	 * @return
	 */
	public String selectlingjDingdh(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh, @Param("dingdh") String dingdh) {
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("usercenter",loginUser.getUsercenter());
		return "select";
	}

	/**
	 * 执行零件+订单号查询
	 * @param bean
	 * @return
	 */
	public String selectlingjDingdhcx(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh, @Param("dingdh") String dingdh,@Param Maoxqmx mx) {
		setResult("result",
				lingjcxService.selectlingjDingdhcx(usercenter, lingjbh, dingdh,mx,getParams()));
		return AJAX;
	}
	/**
	 * 根据订单号、物理点、零件号查询(根据零件+订单号查询)
	 * @param usercenter
	 * @param lingjbh
	 * @param wuld
	 * @param dingdh
	 * @return
	 */
	public String selectTCLingjByDingdh(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh, @Param("wuld") String wuld,
			@Param("dingdh") String dingdh) {
		this.setRequestAttribute("usercenter", usercenter);
		this.setRequestAttribute("lingjbh", lingjbh);
		this.setRequestAttribute("wuld", wuld);
		this.setRequestAttribute("dingdh", dingdh);
		
		return "select";
	}
	/**
	 * 执行订单号、物理点、零件号查询(根据零件+订单号查询)
	 * @param usercenter
	 * @param lingjbh
	 * @param wuld
	 * @param tcNo
	 * @param dingdh
	 * @return
	 */
	public String selectTCLingjByDingdhcx(
			@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh, @Param("wuld") String wuld,
			@Param("tcNo") String tcNo, @Param("dingdh") String dingdh,@Param TC tc) {
		Map<String, String> tjMap = new HashMap<String, String>();
		tjMap.put("usercenter", usercenter);
		tjMap.put("lingjbh", lingjbh);
		tjMap.put("wuld", wuld);
		tjMap.put("tcNo", tcNo);
		tjMap.put("dingdh", dingdh);
		setResult("result", lingjcxService.selectTCLingjByDingdhcx(tjMap,tc,getParams()));
		return AJAX;
	}
	/**
	 * 根据TC号和订单号查询要货令
	 * @param usercenter
	 * @param lingjbh
	 * @param tcNo
	 * @param dingdh
	 * @return
	 */
	public String selectYaohlBytcNoAndDingdh(
			@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh, @Param("tcNo") String tcNo,
			@Param("dingdh") String dingdh) {
		this.setRequestAttribute("usercenter", usercenter);
		this.setRequestAttribute("lingjbh", lingjbh);
		this.setRequestAttribute("tcNo", tcNo);
		this.setRequestAttribute("dingdh", dingdh);
		return "select";
	}
	/**
	 * 执行根据要货令号和订单号查询要货令
	 * @param usercenter
	 * @param lingjbh
	 * @param tcNo
	 * @param yaohlh
	 * @param jiaofj
	 * @param dingdh
	 * @return
	 */
	public String selectYaohlByYaohlhAndDingdh(
			@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh, @Param("tcNo") String tcNo,
			@Param("yaohlh") String yaohlh, @Param("jiaofj") String jiaofj,
			@Param("dingdh") String dingdh,@Param TC tc) {
		Map<String, String> tjMap = new HashMap<String, String>();
		tjMap.put("usercenter", usercenter);
		tjMap.put("lingjbh", lingjbh);
		tjMap.put("tcNo", tcNo);
		tjMap.put("yaohlh", yaohlh);
		tjMap.put("jiaofj", jiaofj);
		tjMap.put("dingdh", dingdh);
		setResult("result", lingjcxService.selectYaohlByYaohlh(tjMap,tc,getParams()));
		return AJAX;
	}
	/**
	 * 根据TC号查询
	 * @param usercenter
	 * @param tcNo
	 * @return
	 */
	public String selectTc(@Param("usercenter") String usercenter,
			@Param("tcNo") String tcNo) {
		if(null!=usercenter&&!"".equals(usercenter)){
			this.setRequestAttribute("usercenter", usercenter);
		}else{
			// 获取当前登录用户信息
			LoginUser loginUser = AuthorityUtils.getSecurityUser();
			setResult("usercenter",loginUser.getUsercenter());
		}
		this.setRequestAttribute("tcNo", tcNo);
		return "select";
	}
	/**
	 * 执行TC号查询
	 * @param usercenter
	 * @param tcNo
	 * @param dingdh
	 * @return
	 */
	public String selectTccx(@Param("usercenter") String usercenter,@Param("tczt") String tczt,
			@Param("tcNo") String tcNo, @Param("dingdh") String dingdh,@Param TC tc) {
		Map<String, String> tjMap = new HashMap<String, String>();
		tjMap.put("usercenter", usercenter);
		tjMap.put("tcNo", tcNo);
		tjMap.put("dingdh", dingdh);
		tjMap.put("tczt", tczt);
		setResult("result", lingjcxService.selectTccx(tjMap,tc,getParams()));
		return AJAX;
	}
	/**
	 * 
	 * @param usercenter
	 * @param tcNo
	 * @return
	 */
	public String selectTcByQiysj(@Param("usercenter") String usercenter,
			@Param("tcNo") String tcNo) {
		this.setRequestAttribute("usercenter", usercenter);
		this.setRequestAttribute("tcNo", tcNo);
		return "select";
	}
	/**
	 * 根据启运时间查询
	 * @param usercenter
	 * @param startQiysj
	 * @param endQiysj
	 * @return
	 */
	public String selectTcByQysj(@Param("usercenter") String usercenter,
			@Param("startQiysj") String startQiysj,
			@Param("endQiysj") String endQiysj) {
			// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("usercenter",loginUser.getUsercenter());
		return "select";
	}
	/**
	 * 执行启运时间查询
	 * @param usercenter
	 * @param startQiysj
	 * @param endQiysj
	 * @return
	 */
	public String selectTcByQysjcx(@Param("usercenter") String usercenter,
			@Param("startQiysj") String startQiysj,
			@Param("endQiysj") String endQiysj,@Param TC tc) {
		Map<String, String> tjMap = new HashMap<String, String>();
		tjMap.put("usercenter", usercenter);
		tjMap.put("startQiysj", startQiysj);
		tjMap.put("endQiysj", endQiysj);
		setResult("result", lingjcxService.selectTcByQysjcx(tjMap,tc,getParams()));
		return AJAX;
	}
	/**
	 * 根据物理点查询
	 * @param usercenter
	 * @param lingjbh
	 * @param startwuldfw
	 * @param endwuldfw
	 * @return
	 */
	public String selectTcBywuld(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh,
			@Param("startwuldfw") String startwuldfw,
			@Param("endwuldfw") String endwuldfw) {
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("usercenter",loginUser.getUsercenter());
		return "select";
	}
	/**
	 * 执行物理点查询TC信息
	 * @param usercenter
	 * @param lingjbh
	 * @param startwuldfw
	 * @param endwuldfw
	 * @param startwuld
	 * @param endwuld
	 * @return
	 */
	public String selectTcBywuldcx(@Param("usercenter") String usercenter,
			@Param("lingjbh") String lingjbh,
			@Param("startwuldfw") String startwuldfw,
			@Param("endwuldfw") String endwuldfw,
			@Param("startwuld") String startwuld,
			@Param("endwuld") String endwuld,@Param("tcno") String tcno,@Param TC tc,
			@Param("kdys_sheet_id") String kdys_sheet_id) {
		Map<String, String> tjMap = new HashMap<String, String>();
		String qiwuld = "";
		if (startwuld != null && !"".equals(startwuld)) {
			qiwuld = this.getCache(startwuld);
		}
		String zhiwuld = "";
		if(endwuld != null&& !"".equals(endwuld)){
			zhiwuld = this.getCache(endwuld);
		}
		tjMap.put("usercenter", usercenter);
		tjMap.put("lingjbh", lingjbh);
		tjMap.put("tcno", tc.getTcNo());
		tjMap.put("startwuldfw", startwuldfw);
		tjMap.put("endwuldfw", endwuldfw);
		tjMap.put("qiwuld", qiwuld);
		tjMap.put("zhiwuld", zhiwuld);
		tjMap.put("kdys_sheet_id", kdys_sheet_id);
		Map<String,String> pagemap = getParams();
		pagemap.put("usercenter", usercenter);
		pagemap.put("lingjbh", lingjbh);
		//导出时不能通过tc号导出指定的信息 
		pagemap.put("tcno", tc.getTcNo());
		pagemap.put("startwuldfw", startwuldfw);
		pagemap.put("endwuldfw", endwuldfw);
		pagemap.put("qiwuld", qiwuld);
		pagemap.put("zhiwuld", zhiwuld);
		pagemap.put("kdys_sheet_id", kdys_sheet_id);
		setResult("result", lingjcxService.selectTcBywuldcx(tjMap,tc,pagemap));
		return AJAX;
	}
	/**
	 * 获取缓存信息
	 * @param startwuldfw
	 * @param endwuldfw
	 * @return
	 */
	public String getCache(String wuld) {
		String param = "";
		CacheManager cm = CacheManager.getInstance();
		List list = (List) cm.getCacheInfo("queryYunswuld").getCacheValue();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			CacheValue cacheValue = (CacheValue) list.get(i);
			String key = cacheValue.getKey();
			String value = cacheValue.getValue();
			if(wuld.equals(key)){
				param = value;
			}
		}
		return param;
	}
	/**
	 * 获取运输物理点信息
	 * @param startwuldlx
	 * @param endwuldlx
	 * @return
	 */
	public String getYunswuld(@Param("startwuldlx") String startwuldlx,
			@Param("endwuldlx") String endwuldlx) {
		Map tjMap = new HashMap();
		String wuldlx = "";
		if (startwuldlx != null && !"".equals(startwuldlx)) {
			wuldlx = startwuldlx;
		} else if (endwuldlx != null && !"".equals(endwuldlx)) {
			wuldlx = endwuldlx;
		}
		tjMap.put("wuldlx", wuldlx);
		setResult("result", lingjcxService.getYunswuld(tjMap));
		return AJAX;
	}
}
