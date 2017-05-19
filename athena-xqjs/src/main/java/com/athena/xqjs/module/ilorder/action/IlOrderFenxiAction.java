package com.athena.xqjs.module.ilorder.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ilorder.service.DingdFenxiService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.MaoxqService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：IlOrderFenxiAction
 * <p>
 * 类描述： Il订单分析
 * </p>
 * 创建人：李智
 * <p>
 * 创建时间：2012-3-7
 * </p>
 * 
 * @version 1.0
 * 
 */

@Component
public class IlOrderFenxiAction extends ActionSupport {

	@Inject
	// 毛需求service
	private MaoxqService maoxqService;
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
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单分析", "进入页面") ;
		return "success";
	}

	/**
	 * @：查询毛需求列表
	 * @：李智
	 * @：2012-3-7
	 */
	public String queryListMaoxq(@Param Maoxq bean) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单分析", "查询毛需求列表开始") ;
		Map<String, String> map = getParams();
		map.put("xuqly",  "DIP");
		map.put("xuqly2", "BJP");
		map.put("xuqly3", "WXP");
		map.put("xuqly4", "ZCP");
		map.put("xuqly5", "CYP");
		map.put("xuqly6", "DIS");
		map.put("xuqly7", "CLV");
		map.put("xuqly8", "DKS");
		// 获取毛需求列表
		//bean.setPageSize(5);
		setResult("result", this.maoxqService.queryMaoxqByXqlx(bean, map));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单分析", "查询毛需求列表结束") ;
		return AJAX;
	}

	/**
	 * @：查询订单列表
	 * @：李智
	 * @：2012-3-7
	 */
	public String queryListDingd(@Param Dingd bean) {
		Map<String, String> map = getParams();
		// Il类型
		map.put("dingdlx", Const.DINGDLX_ILORDER);
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
		// 选中的需求版次
		String xuqbc = map.get("xuqbc");
		// 基准的值(版次或者订单号)
		String jizhunValue = map.get("jizhunValue");
		String dingdjssj = map.get("dingdjssj");
		String dingdjssj1 = map.get("dingdjssj1");
		String dingdjssj2 = map.get("dingdjssj2");
		String flag = map.get("flag");
		setResult("xuqbc", xuqbc);
		setResult("jizhunValue", jizhunValue);
		setResult("flag", flag);
		setResult("dingdjssj", dingdjssj);
		setResult("dingdjssj1", dingdjssj1);
		setResult("dingdjssj2", dingdjssj2);
		return SUCCESS;
	}
	/**
	 * @：查询订单汇总
	 * @：2012-3-7
	 */
	public String querySumDingd(@Param Dingd bean) {
		Map<String, String> map = getParams();
		// Il类型
		map.put("dingdlx", Const.DINGDLX_ILORDER);
		map.put("dingdlx1", Const.DINGDLX_JUANL);
		// 获取订单列表列表
		//bean.setPageSize(5);
		setResult("result", this.dingdService.querySumDingd(bean, map));
		return AJAX;
	}
	/**
	 * 比较的结果发到页面
	 * 
	 * @author 李智
	 * @return String
	 */
	public String bijiao(@Param Dingd bean,@Param("flag") String flag) {
		Map<String, String> map = getParams();
		// 选中的需求版次
		String xuqbc = map.get("xuqbc");
		// 基准的值(版次或者订单号)
		String jizhunValue = map.get("jizhunValue");
		if(StringUtils.isNotBlank(jizhunValue))
		{
			String dingdjssjSubj1 = jizhunValue.substring(0,10);
			String dingdjssjSubj2 = jizhunValue.substring(jizhunValue.length()-5);
			jizhunValue = dingdjssjSubj1 + " " + dingdjssjSubj2;
		}
		String lingjbh = map.get("lingjbh");
		String gongysdm = map.get("gongysdm");
		Map tjMap = new HashMap();
		tjMap.put("lingjbh", lingjbh);
		tjMap.put("gongysdm", gongysdm);
		Maoxq maoxq = new Maoxq();
		try
		{
			if(StringUtils.isNotBlank(xuqbc))
			{
				
				List<String> bancArray = Arrays.asList(xuqbc.split(","));
				String flagBc = "";
				if(null != bancArray && !bancArray.isEmpty())
				{
					for(int i=0;i<bancArray.size();i++)
					{
						flagBc += "'" + bancArray.get(i) + "'";
						if(i != (bancArray.size() - 1))
						{
							flagBc += ",";
						}
					}
				}
				if(StringUtils.isNotBlank(flagBc))
				{
					maoxq.setXuqbc(flagBc);
				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			this.setErrorMessage("没有毛需求版次或选择的毛需求版次有误，未生成订单!");
			return AJAX;
		}

		Map<String,Object> resultDingdljMap = new HashMap<String, Object>();
		
		// 选中的比较的订单号1
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
		dingdjssj1 = tempDingdjssj;
		// 选中的比较的订单号2
		String dingdjssj2 = "";
		if(map.get("dingdjssj2")!=null){
			dingdjssj2= map.get("dingdjssj2");
		}
		String dingdjssjSub3 = "";
		String dingdjssjSub4 = "";
		if(dingdjssj2!=null&&!"".equals(dingdjssj2)){
			dingdjssjSub3 =dingdjssj2.substring(0,10); 
			dingdjssjSub4 =dingdjssj2.substring(dingdjssj1.length()-5); 
		}
		
		String tempDingdjssj2 = dingdjssjSub3+" "+dingdjssjSub4;
		Dingd dingd1 = new Dingd();
		dingd1.setDingdjssj(tempDingdjssj);
		Dingd dingd2 = new Dingd();
		dingd2.setDingdjssj(tempDingdjssj2);
		// 如果需求版次不为空，就一定是需求和订单比较
		if (flag.equals("1")) {
			try{
			if (xuqbc.indexOf(jizhunValue) >= 0) {
				resultDingdljMap = dingdFenxiService.dingdBijiaoByMaoxq(bean,maoxq,
						2, getUserInfo(),bean.getDingdjssj(),tjMap);
			} else {
				resultDingdljMap = dingdFenxiService.dingdBijiaoByMaoxq(bean,maoxq,
					    1, getUserInfo(),bean.getDingdjssj(),tjMap);
			}
			}catch(Exception e){
				this.setErrorMessage("所选毛需求有误，未生成订单!");
			}
		} else if(flag.equals("2")) {
			try{
				
				if (dingdjssj1.equalsIgnoreCase(jizhunValue)) {
					resultDingdljMap = dingdFenxiService.dingdBijiaoByDingd(bean,dingd1,
							dingd2, getUserInfo(),tjMap);
				} else {
					resultDingdljMap = dingdFenxiService.dingdBijiaoByDingd(bean,dingd2,
							dingd1, getUserInfo(),tjMap);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				this.setErrorMessage("所选订单有误，未生成订单!");
			}
		}
		setResult("result", resultDingdljMap);
		
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单分析", "比较成功") ;
		return AJAX;
	}
	
	
	public String dingdFenxiDownLoadFile(@Param Dingd bean,@Param("flag") String flag){
		Map<String, String> map = getParams();
		// 选中的需求版次
		String xuqbc = map.get("xuqbc");
		// 基准的值(版次或者订单号)
		String jizhunValue = map.get("jizhunValue");
		if(StringUtils.isNotBlank(jizhunValue))
		{
			String dingdjssjSubj1 = jizhunValue.substring(0,10);
			String dingdjssjSubj2 = jizhunValue.substring(jizhunValue.length()-5);
			jizhunValue = dingdjssjSubj1 + " " + dingdjssjSubj2;
		}
		String lingjbh = map.get("lingjbh");
		String gongysdm = map.get("gongysdm");
		Map tjMap = new HashMap();
		tjMap.put("lingjbh", lingjbh);
		tjMap.put("gongysdm", gongysdm);
		Maoxq maoxq = new Maoxq();
		
		try
		{
			if(StringUtils.isNotBlank(xuqbc))
			{
				
				List<String> bancArray = Arrays.asList(xuqbc.split(","));
				String flagBc = "";
				if(null != bancArray && !bancArray.isEmpty())
				{
					for(int i=0;i<bancArray.size();i++)
					{
						flagBc += "'" + bancArray.get(i) + "'";
						if(i != (bancArray.size() - 1))
						{
							flagBc += ",";
						}
					}
				}
				if(StringUtils.isNotBlank(flagBc))
				{
					maoxq.setXuqbc(flagBc);
				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			this.setErrorMessage("没有毛需求版次或选择的毛需求版次有误，未生成订单!");
			return AJAX;
		}
		

		Map<String,Object>  resultDingdljMap = new HashMap<String,Object>();
		
		// 选中的比较的订单号1
		String dingdjssj1 = "";
		if(map.get("dingdjssj1")!=null){
			dingdjssj1 = map.get("dingdjssj1");
		}
		String dingdjssjSub1 = "";
		String dingdjssjSub2 = "";
		if(dingdjssj1!=null&&!"".equals(dingdjssj1)){
			dingdjssjSub1 = dingdjssj1.substring(0,10);
			dingdjssjSub2 = dingdjssj1.substring(11);
		}
		String tempDingdjssj = dingdjssjSub1+" "+dingdjssjSub2;
		dingdjssj1 = tempDingdjssj;
		// 选中的比较的订单号2
		String dingdjssj2 = "";
		if(map.get("dingdjssj2")!=null){
			dingdjssj2= map.get("dingdjssj2");
		}
		String dingdjssjSub3 = "";
		String dingdjssjSub4 = "";
		if(dingdjssj2!=null&&!"".equals(dingdjssj2)){
			dingdjssjSub3 =dingdjssj2.substring(0,10); 
			dingdjssjSub4 =dingdjssj2.substring(11); 
		}
		
		String tempDingdjssj2 = dingdjssjSub3+" "+dingdjssjSub4;
		Dingd dingd1 = new Dingd();
		dingd1.setDingdjssj(tempDingdjssj);
		Dingd dingd2 = new Dingd();
		dingd2.setDingdjssj(tempDingdjssj2);
		// 如果需求版次不为空，就一定是需求和订单比较
		if (flag.equals("1")) {
			if (xuqbc.indexOf(jizhunValue) >= 0) {
				resultDingdljMap = dingdFenxiService.dingdBijiaoByMaoxqDownload(bean,maoxq,
						2, getUserInfo(),bean.getDingdjssj(),tjMap);
			} else {
				resultDingdljMap = dingdFenxiService.dingdBijiaoByMaoxqDownload(bean,maoxq,
					    1, getUserInfo(),bean.getDingdjssj(),tjMap);
			}
		} else if(flag.equals("2")) {
			if (dingdjssj1.equals(jizhunValue)) {
				resultDingdljMap = dingdFenxiService.dingdBijiaoByDingdDownload(bean,dingd1,
						dingd2, getUserInfo(),tjMap);
			} else {
				resultDingdljMap = dingdFenxiService.dingdBijiaoByDingdDownload(bean,dingd2,
						dingd1, getUserInfo(),tjMap);
			}
		}
		HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
		downloadServices.downloadFile("dingdfenxi.ftl", resultDingdljMap, response, "订单分析", ExportConstants.FILE_XLS, false) ;
		return AJAX;
	}
	
}
