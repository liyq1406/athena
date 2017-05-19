package com.athena.xqjs.module.kdorder.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.ilorder.Aixddppjg;
import com.athena.xqjs.entity.ilorder.Aixddzxgz;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.LingjGongysService;
import com.athena.xqjs.module.ilorder.service.DingdljService;
import com.athena.xqjs.module.kdorder.service.AixddService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：DingdxgjsxgAction
 * <p>
 * 类描述：订单修改及生效action
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-03-05
 * </p>
 * 
 * @version 1.0
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class AixddAction extends ActionSupport {

	@Inject
	private AixddService aixdd;
	@Inject
	private LingjGongysService lingjgongysService;
	@Inject
	private DingdljService dingdljService;

	@Inject
	private UserOperLog log;

	
	private LoginUser getUserInfo(){
		LoginUser user = AuthorityUtils.getSecurityUser();
		return user ;
	}
	
	/**
	 * 订单修改及生效页面初始化
	 * 
	 * @return 定位页面
	 */
	public String initAixdd(@Param Dingdlj bean) {
		String dingdlx = getParam("dingdlx");// 订单类型
		setResult("removeId", getParam("removeId"));  
		setResult("usercenter", this.getUserInfo().getUsercenter());
		String url = "";
		if (dingdlx.equals(Const.DINGDLX_AIX)) {// 如果订单类型是2-即为爱信订单
			setResult("usercenter", getParam("usercenter"));
			setResult("dingdh", getParam("dingdh"));// 订单号
			setResult("gongysdm", getParam("gongysdm"));
			setResult("dingdzt", getParam("dingdzt"));// 订单状态
			setResult("dingdnr", getParam("dingdnr"));// 订单状态
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("dingdh", getParam("dingdh"));
			bean.setUsercenter(null);
			getParams().remove("usercenter");
			Map map = this.dingdljService.queryAllKDDingdljForMap(bean, maps);
			List<Dingdlj> all = (List) map.get("rows");
			if (!all.isEmpty()&&(all.get(0).getZiyhqrq()!=null&&!"".equals(all.get(0).getZiyhqrq())) && (all.get(0).getP0fyzqxh()!=null&&!"".equals(all.get(0).getP0fyzqxh()))) {
				setResult("ziyhqrq", all.get(0).getZiyhqrq().substring(0, 10));
				String p0fyzqxh = all.get(0).getP0fyzqxh();
				setResult("p0fyzqxh", p0fyzqxh.substring(0, 4) + "年" + p0fyzqxh.substring(4, 6) + "月");
				setResult("gongysdm", all.get(0).getGongysdm());
				setResult("usercenter", all.get(0).getUsercenter());
				
			}
			else
			{
				setResult("gongysdm", "");
				setResult("usercenter", "");
			}
			url = "aixdd";
		} else if (dingdlx.equals(Const.DINGDLX_KD)) {// 如果订单类型是0-即为kd订单
			setResult("dingdh", getParam("dingdh"));
			setResult("jihyz1", this.getUserInfo().getJihyz());
			setResult("gongysdm", getParam("gongysdm"));
			setResult("dingdzzsj", getParam("dingdzzsj"));
			setResult("dingdnr", getParam("dingdnr"));
			setResult("dingdzt", getParam("dingdzt"));
			setResult("ziyhqrq", getParam("ziyhqrq"));
			bean.setUsercenter(null);
			getParams().remove("usercenter");
			Map map = this.dingdljService.queryAllKDDingdljForMap(bean, getParams());
			if (!map.isEmpty()) {
				List<Dingdlj> all = (List) map.get("rows");
				if (!all.isEmpty()&&(all.get(0).getZiyhqrq()!=null&&!"".equals(all.get(0).getZiyhqrq())) && (all.get(0).getP0fyzqxh()!=null&&!"".equals(all.get(0).getP0fyzqxh()))) {
					setResult("ziyhqrq", all.get(0).getZiyhqrq().substring(0, 10));
					String p0fyzqxh = all.get(0).getP0fyzqxh();
					setResult("p0fyzqxh", p0fyzqxh.substring(0, 4) + "年" + p0fyzqxh.substring(4, 6) + "月");
				}
				setResult("result", map);
			}
			url = "kdorder";
		} else if (dingdlx.equals(Const.DINGDLX_TS)) {// 特殊订单
			setResult("dingdh", getParam("dingdh"));
			setResult("jihyz1", this.getUserInfo().getJihyz());
			setResult("dingdlx", "4");
			setResult("gongysdm", getParam("gongysdm"));
			setResult("dingdzzsj", getParam("dingdzzsj"));
			setResult("dingdnr", getParam("dingdnr"));
			setResult("dingdzt", getParam("dingdzt"));
			setResult("ziyhqrq", getParam("ziyhqrq"));
			Map map = this.dingdljService.queryAllDingdljForMap(bean, getParams());
			if (!map.isEmpty()) {
				List<Dingdlj> all = (List) map.get("rows");
				if (!all.isEmpty()&&(all.get(0).getZiyhqrq()!=null&&!"".equals(all.get(0).getZiyhqrq())) && (all.get(0).getP0fyzqxh()!=null&&!"".equals(all.get(0).getP0fyzqxh()))) {
					setResult("ziyhqrq", all.get(0).getZiyhqrq().substring(0, 10));
					String p0fyzqxh = all.get(0).getP0fyzqxh();
					setResult("p0fyzqxh", p0fyzqxh.substring(0, 4) + "年" + p0fyzqxh.substring(4, 6) + "月");
				}
				setResult("result", map);
			}
			url = "kdorder";
		}
		return url;
	}

	
	public String  getUserCenter(){
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("result",loginUser.getUcList());
		return AJAX ;
	}
	
	
	
	/**
	 * 查询零件订单明细
	 * 
	 * @return 异步返回json
	 */
	public String queryDingdlj(@Param Dingdlj param) {
		setResult("result", aixdd.queryDingdlj(param, getParams()));
		return AJAX;
	}

	/**
	 * 匹配装箱规则
	 * 
	 * @param dingdmx
	 *            查询参数
	 * @return 匹配结果页面
	 */
	public String piPzxgz(@Param Dingdmx dingdmx) {
		String logInfo = "";
		try {
			setResult("gongysdm", dingdmx.getGongysdm());// 供应商代码
			setResult("usercenter", dingdmx.getUsercenter());// 用户中心
			setResult("dingdh", dingdmx.getDingdh());// 订单号
			setResult("lingjbh", dingdmx.getLingjbh());// 零件编号
			aixdd.piPzxgz(dingdmx);
			logInfo = "爱信订单匹配装箱规则成功";
			log.addCorrect(CommonUtil.MODULE_XQJS, "爱信订单匹配装箱规则", logInfo);
			CommonFun.logger.info(logInfo);
		} catch (Exception e) {
			logInfo = "爱信订单匹配装箱规则异常";
			log.addError(CommonUtil.MODULE_XQJS, "爱信订单匹配装箱规则", logInfo, CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.toString()));
			CommonFun.logger.info(logInfo);
			setResult("result", logInfo);
		}
		return "aixddppjg";
	}

	/**
	 * 查询爱信订单装修匹配规则结果
	 * 
	 * @return 异步返回json
	 */
	public String queryAixddppgzjg(@Param Aixddppjg param) {
		setResult("result", aixdd.queryAixddppgzjg(param, getParams()));
		return AJAX;
	}

	/**
	 * 保存爱信订单匹配规则结果
	 * 
	 * @return 异步返回json
	 */
	public String saveAixddppgzjg(@Param("operant") Integer operant, @Param Aixddppjg aixddppjg) {
		if (operant == 1) {// 增加
			LingjGongys lingjgys = lingjgongysService.select(aixddppjg.getUsercenter(), aixddppjg.getGongysdm(), aixddppjg.getLingjbh());
			if(lingjgys == null){
				setErrorMessage("零件供应商不对");
			}else{
				aixdd.saveAixddppgzjg(aixddppjg);
			}
		} else {// 修改
			aixdd.updateAixddppgzjg(aixddppjg);
		}
		return AJAX;
	}

	/**
	 * 初始化订单装箱规则
	 * 
	 * @return
	 */
	public String initDyzxgz() {
		return "dingyzxgz";
	}

	/**
	 * 装箱方案查询
	 * 
	 * @return
	 */
	public String initZxfncx() {
		setResult("dingdh", getParam("dingdh"));// 订单号
		setResult("gongysdm", getParam("gongysdm"));
		return "zhuangxfncx";
	}

	/**
	 * 查询装箱规则
	 * 
	 * @param aixddzxgz
	 * @return
	 */
	public String queryZxgz(@Param Aixddzxgz aixddzxgz) {
		setResult("result", aixdd.queryZxgz(aixddzxgz, getParams()));
		return AJAX;
	}

	/**
	 * 查询装箱规则
	 * 
	 * @param aixddzxgz
	 * @return
	 */
	public String queryZxgzMx(@Param Aixddzxgz aixddzxgz) {
		setResult("result", aixdd.queryZxgzMx(getParams()));
		return AJAX;
	}

	/**
	 * 定义爱信订单装箱规则
	 * 
	 * @param aixddzxgz
	 * @return
	 */
	public String dingYzxgz(@Param Aixddzxgz aixddzxgz, @Param("operant") Integer operant) {
		if (operant == 1) {// 增加
			String result = aixdd.insertAixddzxgz(aixddzxgz);
			if (result != null && !result.equals("")) {
				setErrorMessage(result);
			}
		} else {// 修改
			aixdd.updateAixddzxgz(aixddzxgz);
		}
		return AJAX;
	}

	/**
	 * 删除爱信订单装箱规则
	 * 
	 * @param aixddzxgz
	 * @return
	 */
	public String removeZxgz(@Param Aixddzxgz aixddzxgz) {
		aixdd.removeAixddzxgz(aixddzxgz);
		return AJAX;
	}
}
