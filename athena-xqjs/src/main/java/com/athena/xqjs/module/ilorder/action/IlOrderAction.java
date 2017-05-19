package com.athena.xqjs.module.ilorder.action;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.common.Jisclcssz;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Maoxqhzpc;
import com.athena.xqjs.entity.ilorder.Maoxqhzsc;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.GonghmsMaoxqService;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.GongyzqService;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.athena.xqjs.module.ilorder.service.MaoxqService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzpcService;
import com.athena.xqjs.module.ilorder.service.MaoxqhzscService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：IlOrderAction
 * <p>
 * 类描述： Il订单
 * </p>
 * 创建人：李智
 * <p>
 * 创建时间：2012-2-8
 * </p>
 * 
 * @version 1.0
 * 
 */

@Component
public class IlOrderAction extends ActionSupport {

	@Inject
	// 毛需求service
	private MaoxqService maoxqService;
	@Inject
	// 供货模式-毛需求service
	private GonghmsMaoxqService gonghmsMaoxqService;
	@Inject
	private IlOrderService ilorderservice;

	@Inject
	private MaoxqhzpcService maoxqhzpcservice;
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private GongyzqService gService;
	@Inject
	// 订单service
	private DingdService dingdService;
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;
	@Inject
	private MaoxqhzpcService maoxqhzpcService;// 注入毛需求汇总PCservice
	@Inject
	private DownLoadServices  downloadServices;
	@Inject
	private MaoxqhzscService maoxqhzscService;// 注入毛需求汇总PCservice
	
	public static final Logger log = Logger.getLogger(IlOrderAction.class);
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
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "进入页面");
		return "success";
	}

	/**
	 * 初始化获取资源获取日期
	 * 
	 * @：李智
	 * @：2012-2-10
	 */
	public String executeZiykzb() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "获取资源获取日期开始");
		Map<String, String> map = new HashMap<String,String>();
		setResult("result", this.maoxqService.queryZiykzb(map));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "获取资源获取日期结束");
		return AJAX;
	}

	/**
	 * 初始化获取资源获取日期(卷料)
	 * 
	 * @author NIESY
	 * @throws ParseException
	 * @DATE 2012-5-11
	 */
	public String juanlZrhqrq() throws ParseException {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "获取资源获取日期开始");
		Map<String, String> map = new HashMap<String,String>();
		setResult("result", this.maoxqService.queryZiykzb(map));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "获取资源获取日期结束");
		return AJAX;
	}

	/**
	 * 初始化需求来源
	 * 
	 * @：李智
	 * @：2012-3-2
	 */
	public String executeMaoxqXqly() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "获取需求来源开始");
		Map<String, String> map = setUsercenter(getParams());
		map.put("zhizlx", map.get("zhizlxForXuqly"));
		String[] xuqlxs = map.get("xuqlx").split("、");
		// 设置PP/PS/PJ/C1,CD,M1,MD
		for (int i = 0; i < xuqlxs.length; i++) {
			map.put("gonghms" + i, xuqlxs[i]);
		}
		setResult("result", this.gonghmsMaoxqService.queryGonghmsMaoxq(map));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "获取需求来源结束");
		return AJAX;
	}

	/**
	 * 在session中查询选中的毛需求
	 * 
	 * @：李智
	 * @：2012-3-2
	 */
	public String querySessionMaoxq() {

		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "session中查询选中的毛需求开始");
		// getParams().put("zidlx", Const.ZIDLX_XQLY) ;
		Map<String, String> map = getParams();

		map.put("usercenter", getUserInfo().getUsercenter());
		map.put("zidlx", Const.ZIDLX_XQLY);
		if (map.get("xuqbc") != null) {
			Maoxq maoxq = this.maoxqService.queryOneMaoxq(map);

			List<Maoxq> list = new ArrayList<Maoxq>();

			// 如果之前有放过
			if (getSessionAttribute("chooseMaoxqForIl") != null) {
				list = (List) getSessionAttribute("chooseMaoxqForIl");
			}
			if (maoxq != null) {
				if ("1".equals(map.get("isDel"))) {
					list.remove(maoxq);
				} else {
					for (int i = 0; i < list.size(); i++) {
						Maoxq oneMaoxq = list.get(i);
						if (oneMaoxq.getXuqly().equals(maoxq.getXuqly())) {
							setErrorMessage("相同需求来源的需求只能存在一条！");
							return AJAX;
						}
					}
					list.add(maoxq);
				}
			}
			// 放入session
			setSessionAttribute("chooseMaoxqForIl", list);
			setResult("result", CommonFun.listToMap(list));
		}
		// 当没版次的时候表示订单发生变化,清空session
		else {
			// 放入session
			setSessionAttribute("chooseMaoxqForIl", null);
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "session中查询选中的毛需求结束");
		return AJAX;
	}
	
	
	

	/**
	 * @：查询毛需求列表
	 * @：李智
	 * @：2012-2-8
	 */
	public String queryListMaoxq(@Param Maoxq mxq) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "查询毛需求列表开始");
		Map<String, String> map = this.setUsercenter(getParams());
		if (map.get("xuqlx") != null && map.get("xuqlx").equals("C1、CD、M1、MD")) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			List<Maoxq> maoxqList = new ArrayList<Maoxq>();
			Maoxq maoxq = new Maoxq();
			maoxq.setXuqbc("最新");
			maoxqList.add(maoxq);
			resultMap.put("total", 1);
			resultMap.put("rows", maoxqList);
			setResult("result", resultMap);
			return AJAX;
		}

		// 设置查询条件
		String zhizlx = map.get("zhizlxForXuqly");
		// 1为一个制造路线为空的特指值
		map.put("zhizlx", zhizlx);
		if (map.get("xuqlx") != null) {
			// 获取毛需求列表

			map.put("zidlx", Const.XITCSDY_XQLY_CHN);
			setResult("result", this.maoxqService.queryMaoxqByXqlx(mxq, map));

		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "查询毛需求列表结束");
		return AJAX;
	}

	/**
	 * @throws Exception
	 * @：国产件订单计算
	 * @：陈骏
	 * @：2012-2-8
	 */
	public String ilCalculate(@Param("edit") ArrayList<Maoxq> ls, @Param("dingdh") ArrayList<Dingd> ddh)
			{
		long start = System.currentTimeMillis();
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "ilCalculate---Il订单计算开始");
		if (ls.isEmpty()) {
			setResult("result", "版次错误");
		} else {
			String banc[] = new String[ls.size()];
			for (int i = 0; i < ls.size(); i++) {
				banc[i] = ls.get(i).getXuqbc();
			}
			String ziyhqrq = getParam("ziyhqrq");

			String zhizlx = getParam("zhizlx");
			String dingdlx = getParam("xuqlx");
			LoginUser loginUser = AuthorityUtils.getSecurityUser();
			String jihyz = loginUser.getZuh();
			String result = "";
			result = this.jiS(ziyhqrq, zhizlx, dingdlx, jihyz, banc, ddh);
			setResult("result", result);
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "ilCalculate---Il订单计算结束");
		log.info("IL订单计算结束,耗时---------------------"+(System.currentTimeMillis() - start));
		return AJAX;
	}

	/**
	 * @throws Exception
	 * @：国产件参数校验
	 * @：陈骏
	 * @：2012-2-8
	 */
	public String checkValue(@Param("edit") ArrayList<Maoxq> ls, @Param("dingdh") ArrayList<Dingd> ddh)
			throws Exception {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "国产件参数校验开始");
		int x = 30;
		String result = "";
		Map error = new HashMap();
		String banc[] = new String[x];
		for (int i = 0; i < ls.size(); i++) {
			Maoxq maoxq = ls.get(i);

			banc[i] = maoxq.getXuqbc();
		}

		String ziyhqrq = getParam("ziyhqrq");

		String dingdlx = getParam("xuqlx");
		String zhizlx = getParam("zhizlx");

		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String jihyz = loginUser.getZuh();

		int s = 30;
		String usercenter[] = new String[s];
		List<String> useList = loginUser.getUcList();
		for (int i = 0; i < useList.size(); i++) {

			usercenter[i] = useList.get(i);
		}
		this.ilorderservice.clearData(dingdlx);// 清理中间表

		try
		{
			this.ilorderservice.conversionColRow(banc, dingdlx, ziyhqrq, usercenter, zhizlx, ddh);// 取出需要计算的毛需求并进行行列转换
			int errorCount = this.ilorderservice.checkXiaohbl(dingdlx);
			result = this.ilorderservice.maoxqhzglckx(ziyhqrq, dingdlx, jihyz, zhizlx, ddh,loginUser);// 将毛需求汇总到仓库，并关联参考系参数然后将数据插入到毛需求汇总_参考系表
			if (  null == result||result.equals("")) {
				error = this.maoxqhzpcservice.checkAll(dingdlx);// 校验物流路径，供应商份额以及参考系参数
				if(Integer.parseInt(error.get("result").toString())!=0){
					result = "校验完成，但是校验时某些零件参数有误，请查看异常报警信息！";
				}else {
					if(errorCount > 0)
					{
						result = "校验完成，但是校验时某些零件参数有误，请查看异常报警信息！";
					}
					else
					{
						result = "校验完成,无异常";
					}
				}
				
			}
		}
		catch (Exception e)
		{
			result = "效验异常";
		}
		setResult("result", result);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "国产件参数校验结束");
		return AJAX;
	}

	public boolean checkDingdh(ArrayList<Dingd> ddh) {
		CommonFun.logger.info("订单状态校验方法：checkDingdh开始");
		boolean flag = true;
		for (Dingd dingd : ddh) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("dingdh", dingd.getDingdh());
			CommonFun.logger.debug("订单号状态校验中参数：dingdh值为"+dingd.getDingdh());
			Dingd dingdan = this.dingdService.queryDingdByDingdh(map);
			CommonFun.logger.debug("该订单查询后的状态为："+dingdan.getDingdzt());
			if (!(Const.DINGD_STATUS_YDY.equals(dingdan.getDingdzt()) || Const.DINGD_STATUS_JSZ.equals(dingdan.getDingdzt()))) {
				flag = false;
			}
		}
		CommonFun.logger.debug("返回值为："+flag);
		CommonFun.logger.info("订单状态校验方法：checkDingdh结束");
		return flag;
	}

	public String jiS(String ziyhqrq, String zhizlx, String dingdlx, String jihyz, String[] banc,
			ArrayList<Dingd> dingdh)  {
		long start = System.currentTimeMillis();
		CommonFun.logger.info("Il订单计算模式为"+dingdlx+"的计算开始");
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "计算开始");
		String result = "";
		Map<String, String> map = getParams();
		map.put("jsrq", CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD));// 计算日期
//		map.put("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());// 设置用户中心
		map.put("username", AuthorityUtils.getSecurityUser().getUsername());// 设置用户姓名
		map.put("jiscldm", Const.JISMK_IL_CD);// 计算处理代码：国产件（31）
		CommonFun.mapPrint(map,"计算锁查询参数map");
		try {
			// 判断处理状态,是否有计算进行中
			if (jiscclssz.checkState(map)) {
				result = "有计算正在进行,请稍后再计算";
			} else {
				// 更新处理状态为1,计算中
				jiscclssz.updateState(map, Const.JSZT_EXECUTING);
				// 计算
				CommonFun.logger.debug("开始打印国产计算锁查询参数list：dingdh");
				//CommonFun.objListPrint(dingdh, "dingdh");
				boolean flagDdh = this.checkDingdh(dingdh);
				CommonFun.logger.debug("订单校验结果为："+flagDdh);
				CommonFun.logger.debug("参与此次计算的订货路线为："+zhizlx);
				LoginUser loginUser = AuthorityUtils.getSecurityUser();
				List<String> useList = loginUser.getUcList();
				//CommonFun.listPrint(useList, "useList");
				String[] usercenter = new String[useList.size()];
				if (!useList.isEmpty()) {
					Object[] obj = (Object[])useList.toArray();
					System.arraycopy(obj, 0, usercenter, 0, obj.length);
				}else{
					usercenter = new String []{"UW"};
				}
				if (zhizlx.equals(Const.ZHIZAOLUXIAN_IL)) {
					result = this.ilorderservice.doCalculate(zhizlx, dingdlx, ziyhqrq, jihyz, banc, null,null,usercenter,loginUser);
				} else if (zhizlx.equals(Const.ZHIZAOLUXIAN_GL)) {
					if(dingdh.size()!=0){
						if (flagDdh) {
							result = this.ilorderservice.doCalculate(zhizlx, dingdlx, ziyhqrq, jihyz, banc, dingdh,null,usercenter,loginUser);
						} else {
							result = "选择的订单状态不是\"已定义\"！";
						}
					}else{
						result = "未选择订单号！";
					}
				}

				// 计算完成更新处理状态为0,计算完成
				jiscclssz.updateState(map, Const.JSZT_SURE);

			}
		} catch (Exception e) {
			// 异常时将处理状态更新为0
			jiscclssz.updateState(map, Const.JSZT_EXECPTION);
			result = "计算异常";
			CommonFun.logger.error(e);
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException("IlOrderAction.jiS,Exception"+e1);
			}
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "计算结束");
		CommonFun.logger.info("Il订单计算模式为"+dingdlx+"的计算结束");
		long end = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,计算时间"+ (end - start)/1000);
		return result;
	}
	public String hzDownLoadFile() throws IOException{
		String dingdlx = getParam("xuqlx");
		Map<String ,Object> map = new HashMap<String,Object>();
		if(null!=dingdlx){
			if(dingdlx.equals(Const.PP)){
				ArrayList<Maoxqhzpc> list = (ArrayList)this.maoxqhzpcService.selectDingd(null);
				map.put("res", list);
				Map<String, Object> dataSource = map;
				//拿到response对象
				HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
				downloadServices.downloadFile("maoxqhzpc.ftl", dataSource, response, "订单计算中间表导出", ExportConstants.FILE_XLS, false) ;
			}else if(dingdlx.equals(Const.PS)){
				ArrayList<Maoxqhzsc> list = (ArrayList)this.maoxqhzscService.selectDingd(null);
				map.put("res", list);
				Map<String, Object> dataSource = map;
				//拿到response对象
				HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
				downloadServices.downloadFile("maoxqhzsc.ftl", dataSource, response, "订单计算中间表导出", ExportConstants.FILE_XLS, false) ;
			}
		}
		
		
		return "download";
	}
	public String statsExcu(){
		return "success";
	}
	
	public String queryAllStats(@Param Jisclcssz jisclcssz){
		setResult("result", this.jiscclssz.queryAllStats(jisclcssz));
		return AJAX;
	}
	public String changeStats(@Param("zt") ArrayList<Jisclcssz> list){
		String message = "计算锁更新完毕";
		int count = 0;
		for(Jisclcssz jisclcssz :list){
			Map<String,String> map = new HashMap<String,String>();
			map.put("usercenter", jisclcssz.getUsercenter());
			map.put("jiscldm", jisclcssz.getJiscldm());
			count =this.jiscclssz.updateState(map, "90");
			if(count<=0){
				message = "计算锁有部分未更新";
			}
		}
		setResult("result", message);
		return AJAX;
	}
	
}
