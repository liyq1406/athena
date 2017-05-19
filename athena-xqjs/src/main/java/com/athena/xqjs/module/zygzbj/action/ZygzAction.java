package com.athena.xqjs.module.zygzbj.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.entity.zygzbj.ZiygzbjHz;
import com.athena.xqjs.entity.zygzbj.Ziygzlj;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.laxkaix.service.LaxjhService;
import com.athena.xqjs.module.zygzbj.service.ZygzService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ZygzAction
 * <p>
 * 类描述：资源跟踪action
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-02-13
 * </p>
 * 
 * @version 1.0
 * 
 */  
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ZygzAction  extends BaseWtcAction {

	@Inject
	private ZygzService zygz;
	
	@Inject
	private LaxjhService lxService;
	@Inject
	private UserOperLog log;
	
	/**
	 * 资源跟踪报警计算页面初始化
	 * @return 定位页面
	 */
	public String initZygz(){
		setResult("lingjbh", getParam("lingjbh"));
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("usercenter",loginUser.getUsercenter());
		return "zygz";
	}
	
	/**
	 * 查询资源跟踪
	 * @return
	 */
	public String queryZygz(@Param ZiygzbjHz hz){
		setResult("result", zygz.queryZygz(hz,getParams()));
		return AJAX;
	}
	
	/**
	 * 查询资源跟踪明细
	 * @return
	 */
	public String initZygzMx(@Param ZiygzbjHz hz){
		setResult("id", getParam("id"));
		setResult("removeId", getParam("removeId"));
		return "zygzMx";
	}
	
	/**
	 * 查询资源跟踪明细
	 * @return
	 */
	public String queryZygzMx(@Param ZiygzbjHz hz){
		setResult("result", zygz.queryZygzMx(hz,getParams()));
		return AJAX;
	}
	
	/**
	 * 资源跟踪零件页面
	 * @return
	 */
	public String initZygzlj(){
		setResult("usercenter", getParam("usercenter"));//用户中心
		setResult("lingjbh", getParam("lingjbh"));//零件编号
		return "zygzlj";
	}
	
	/**
	 * 查询集装箱列表
	 * @return
	 */
	public String queryJizxlb(@Param Ziygzlj param){
		setResult("result", zygz.queryJizxlb(param,getParams()));
		return AJAX;
	}
	
	/**
	 * 查询未发运零件
	 * @return
	 */
	public String queryWeifylj(@Param Yaohl param){
		setResult("result", zygz.queryWeifylj(param,getParams()));
		return AJAX;
	}
	
	/**
	 * 集装箱列表初始化
	 * @return
	 */
	public String initJzx(){
		setResult("lingjbh", getParam("lingjbh"));//零件编号
		setResult("usercenter", getParam("usercenter"));//用户中心
		return "zygzjzx";
	}
	
	/**
	 * 查询资源跟踪集装箱列表
	 * @param param 查询参数
	 * @return
	 */
	public String queryZygzjzx(@Param Yaohl param){
		setResult("result", zygz.queryZygzjzx(param, getParams()));
		return AJAX;
	}
	
	/**
	 * 更新交付时间
	 * @return
	 */
	public String updateJiaofsj(){
		//更新要货令修改后预计交付时间
		String tcNo = getParam("tcno");
		Map map = new HashMap();
		map.put("tcNo", tcNo);
		String kdysSheetId = zygz.queryTcSheetId(map);
		String  zuixyjddsj = getParam("zuixyjddsj");
		String  lujdm = getParam("lujdm");
		Map<String,String> wtcMap = new HashMap<String,String>();
		wtcMap.put("tch", tcNo);
		wtcMap.put("fasbh", kdysSheetId);
		wtcMap.put("celbh", CommonFun.strNull(lujdm));
		wtcMap.put("daoxsj", zuixyjddsj);
		String response = "";
		String loginfo = "";
		String msg = "";
		List wtcList = new ArrayList();
		wtcList.add(wtcMap);
		Map param = new HashMap();
		param.put("list", wtcList);
		try {
			LoginUser loginUser = AuthorityUtils.getSecurityUser();
			String usercenter = loginUser.getUsercenter();
			//调用WTC查询拒收跟踪单
			WtcResponse wtcResponse = callWtc(usercenter,"Q0401", param);
			//获取WTC
			response = CommonFun.strNull(wtcResponse.get("response"));
			if(response.equals(Const.WTC_SUSSCESS)){
				zygz.updateJiaofsj(getParams());
				lxService.updateYaohlBytc(getParam("tcno"), getParam("zuixyjddsj"), lxService.getTcByYaohl());
			}
		} catch (Exception e) {
			log.addError(CommonUtil.MODULE_XQJS, "调用WTC发送最新预计到达时间信息异常", "调用WTC发送拉箱指定到达时间信息异常", CommonUtil.getClassMethod(), loginfo);
		}
		return AJAX;
	}
	
	/**
	 * 未装箱要货令列表初始化
	 * @return
	 */
	public String initWeizxyhllb(){
		setResult("lingjbh", getParam("lingjbh"));//TC号
		setResult("usercenter", getParam("usercenter"));//用户中心
		return "weizxljlb";
	}
	
	/**
	 * 查询未装箱要货令列表
	 * @param param 查询参数
	 * @return
	 */
	public String queryWeizxyhllb(@Param Ziygzlj param){
		setResult("result", zygz.queryWeizxyhllb(param, getParams()));
		return AJAX;
	}
	
	/**
	 * 查询要货令
	 * @return
	 */
	public String initYaohl(){
		setResult("lingjbh", getParam("lingjbh"));//零件编号
		setResult("usercenter", getParam("usercenter"));//用户中心
		setResult("duandsj", getParam("duandsj"));//仓库编号
		setResult("cangkdm", getParam("cangkdm"));//查询日期
		setResult("removeId", getParam("removeId"));
		return "yaohlInfo";
	}
	
	/**
	 * 查询要货令
	 * @return
	 */
	public String queryYaohl(@Param Ziygzlj param){
		setResult("result", zygz.queryYhl(param, getParams()));
		return AJAX;
	}
}
