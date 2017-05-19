package com.athena.xqjs.module.kdorder.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ppl.Niandyg;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ilorder.service.DingdFenxiService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ppl.service.NiandygService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：KdOrderFenxiAction
 * <p>
 * 类描述：Kd订单分析
 * </p>
 * 创建人：李智
 * <p>
 * 创建时间：2012-3-15
 * </p>
 * 
 * @version 1.0
 * 
 */

@Component
public class KdOrderFenxiAction extends ActionSupport {

	@Inject
	// 年度预告service
	private NiandygService niandygService;
	@Inject
	// 订单service
	private DingdService dingdService;
	@Inject
	// 订单分析service
	private DingdFenxiService dingdFenxiService;
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private DownLoadServices  downloadServices;
	// 获取用户中心
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser();
		// return loginUser ;
	}

	/**
	 * 设置用户中心查询条件
	 * 
	 * @param params
	 *            查询条件
	 * @return 带用户中心的查询条件
	 */
	public Map<String, String> setUsercenter(Map<String, String> params) {
		params.put("usercenter", this.getUserInfo().getUsercenter());
		return params;
	}

	/**
	 * 页面初始化，执行跳转
	 */
	public String execute() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Kd订单分析", "进入页面") ;
		return "success";
	}

	/**
	 * @：查询年度订单列表
	 * @：李智
	 * @：2012-4-18
	 */
	public String queryListNiandyg(@Param Niandyg bean) {
		Map<String, String> map = getParams();
		map.put("ppllx", Const.NIANDYG_PPLLX_KD);
		// 获取毛需求列表
		setResult("result", this.niandygService.select(bean, map));
		return AJAX;
	}

	/**
	 * @：查询订单列表
	 * @：李智
	 * @：2012-3-15
	 */
	public String queryListDingd(@Param Dingd bean) {
		Map<String, String> map = getParams();
		// kd类型
		map.put("dingdlx", Const.DINGD_LX_KD);
		// 获取订单列表列表
		setResult("result", this.dingdService.queryDingdBylx(bean, map));
		return AJAX;
	}

	/**
	 * 跳到比较结果页面
	 * 
	 * @return
	 */
	public String toBijiaoResult() {
		Map<String, String> map = getParams();
		// 选中的PPL版次
		String pplbc = map.get("pplbc");
		// 选中的比较的订单号1
		String dingdh1 = map.get("dingdh1");
		// 选中的比较的订单号2
		String dingdh2 = map.get("dingdh2");
		// 基准的值(版次或者订单号)
		String jizhunValue = map.get("jizhunValue");
		String dingdjssj1 = map.get("dingdjssj1");
		String dingdjssj2 = map.get("dingdjssj2");
		setResult("pplbc", pplbc);
		setResult("dingdh1", dingdh1);
		setResult("dingdh2", dingdh2);
		setResult("jizhunValue", jizhunValue);
		setResult("dingdjssj1", dingdjssj1);
		setResult("dingdjssj2", dingdjssj2);
		return SUCCESS;
	}

	/**
	 * 比较的结果发到页面
	 * 
	 * @author 李智
	 * @return String
	 */
	public String bijiao(@Param Dingd bean) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Kd订单分析", "比较开始") ;
		Map<String, String> map = getParams();
		// 选中的PPL版次
		String pplbc = map.get("pplbc");
		// 选中的比较的订单号1
		String dingdh1 = map.get("dingdh1");
		// 选中的比较的订单号2
		String dingdh2 = map.get("dingdh2");
		// 基准的值(版次或者订单号)
		String jizhunValue = map.get("jizhunValue");
		Map tjMap = new HashMap();
		String lingjbh = map.get("lingjbh");
		String gongysdm = map.get("gongysdm");
		tjMap.put("lingjbh", lingjbh);
		tjMap.put("gongysdm", gongysdm);
		String dingdjssj1 = "";
		if(map.get("dingdjssj1")!=null){
			dingdjssj1 = map.get("dingdjssj1");
		}
		String dingdjssjSub1 = "";
		String dingdjssjSub2 = "";
		if(dingdjssj1!=null&&!"".equals(dingdjssj1)){
			dingdjssjSub1 = dingdjssj1.substring(0,10);
			dingdjssjSub2 = dingdjssj1.substring(dingdjssj1.length()-5);
		}
		String tempDingdjssj = dingdjssjSub1+" "+dingdjssjSub2;
		String dingdjssj2 = "";
		String dingdjssjSub3 = "";
		String dingdjssjSub4 = "";
		if(map.get("dingdjssj2")!=null){
			dingdjssj2 = map.get("dingdjssj2");
		}
		if(dingdjssj2!=null&&!"".equals(dingdjssj2)){
			dingdjssjSub3 =dingdjssj2.substring(0,10); 
			dingdjssjSub4 =dingdjssj2.substring(dingdjssj1.length()-5); 
		}
		String tempDingdjssj2 = dingdjssjSub3+" "+dingdjssjSub4;
		Niandyg niandyg = new Niandyg();
		niandyg.setPplbc(pplbc);
		Dingd dingd1 = new Dingd();
		dingd1.setDingdh(dingdh1);
		dingd1.setDingdjssj(tempDingdjssj);
		Dingd dingd2 = new Dingd();
		dingd2.setDingdh(dingdh2);
		dingd2.setDingdjssj(tempDingdjssj2);
		Map<String,Object> resultDingdljMap = new HashMap<String, Object>();

		// 如果PPL版次不为空，就一定是ppl和订单比较
		if (map.get("pplbc") != null) {
			//暂时只有订单为基准
			try {
				resultDingdljMap = dingdFenxiService.dingdBijiaoByNiandyg(bean,niandyg,
						dingd1, 1, getUserInfo());
			} catch (Exception e) {
				userOperLog.addError(CommonUtil.MODULE_XQJS, "PPL与订单比较", "发生错误", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			}
		} else {
			if (dingdh1.equals(jizhunValue)) {
				resultDingdljMap = dingdFenxiService.dingdBijiaoByDingd(bean,dingd1,
						dingd2, getUserInfo(),tjMap);
			} else {
				resultDingdljMap = dingdFenxiService.dingdBijiaoByDingd(bean,dingd2,
						dingd1, getUserInfo(),tjMap);
			}
		}
		setResult("result", resultDingdljMap);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Kd订单分析", "比较结束") ;
		return AJAX;
	}
	
	public String dingdFxExport(){
		Map<String, String> map = getParams();
		// 选中的PPL版次
		String pplbc = map.get("pplbc");
		// 选中的比较的订单号1
		String dingdh1 = map.get("dingdh1");
		// 选中的比较的订单号2
		String dingdh2 = map.get("dingdh2");
		// 基准的值(版次或者订单号)
		String jizhunValue = map.get("jizhunValue");
		String dingdjssj1 = "";
		if(map.get("dingdjssj1")!=null){
			dingdjssj1 = map.get("dingdjssj1");
		}
		String dingdjssjSub1 = "";
		String dingdjssjSub2 = "";
		if(dingdjssj1!=null&&!"".equals(dingdjssj1)){
			dingdjssjSub1 = dingdjssj1.substring(0,10);
			dingdjssjSub2 = dingdjssj1.substring(dingdjssj1.length()-5);
		}
		String tempDingdjssj = dingdjssjSub1+" "+dingdjssjSub2;
		String dingdjssj2 = "";
		String dingdjssjSub3 = "";
		String dingdjssjSub4 = "";
		if(map.get("dingdjssj2")!=null){
			dingdjssj2 = map.get("dingdjssj2");
		}
		if(dingdjssj2!=null&&!"".equals(dingdjssj2)){
			dingdjssjSub3 =dingdjssj2.substring(0,10); 
			dingdjssjSub4 =dingdjssj2.substring(dingdjssj1.length()-5); 
		}
		String tempDingdjssj2 = dingdjssjSub3+" "+dingdjssjSub4;
		Niandyg niandyg = new Niandyg();
		niandyg.setPplbc(pplbc);
		Dingd dingd1 = new Dingd();
		dingd1.setDingdh(dingdh1);
		dingd1.setDingdjssj(tempDingdjssj);
		Dingd dingd2 = new Dingd();
		dingd2.setDingdh(dingdh2);
		dingd2.setDingdjssj(tempDingdjssj2);
		Map<String,Object> resultDingdljMap = new HashMap<String, Object>();
		List<Dingdlj> resultDingdlj = new ArrayList<Dingdlj>();
		// 如果PPL版次不为空，就一定是ppl和订单比较
		if (pplbc != null && !pplbc.equals("")) {
			//暂时只有订单为基准
			try {
				
				resultDingdlj = dingdFenxiService.expDingdBijiaoByNiandyg(niandyg,
						dingd1, 1, getUserInfo());
			} catch (Exception e) {
				userOperLog.addError(CommonUtil.MODULE_XQJS, "PPL与订单比较", "发生错误", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			}
		} else {
			if (dingdh1.equals(jizhunValue)) {
				resultDingdlj= dingdFenxiService.expDingdBijiaoByDingd(dingd1,
						dingd2, getUserInfo());
			} else {
				resultDingdlj = dingdFenxiService.expDingdBijiaoByDingd(dingd2,
						dingd1, getUserInfo());
			}
		}
		resultDingdljMap.put("list", resultDingdlj);
		HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
		downloadServices.downloadFile("kdorderdingdfenxi.ftl", resultDingdljMap, response, "KD订单分析导出", ExportConstants.FILE_XLS, false) ;	
		return "download";
	}
}
