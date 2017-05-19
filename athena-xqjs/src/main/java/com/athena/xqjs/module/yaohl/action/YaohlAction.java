package com.athena.xqjs.module.yaohl.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.excore.template.export.IProcessData;
import com.athena.excore.template.export.ProcessDataFactory;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.entity.yaohl.Plcjlsk;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.entity.yaohl.Yaonbhl;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.utils.xls.plcjlsk.XlsHandlerUtilslsk;
import com.athena.xqjs.module.utils.xls.yaohlgl.XlsHandlerUtilsyaohlzz;
import com.athena.xqjs.module.utils.xls.yaohltz.XlsHandlerUtilsyaohltz;
import com.athena.xqjs.module.yaohl.service.YaohlService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * 要货令Action
 * 
 * @author Xiahui
 * @date 2012-1-17
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YaohlAction extends BaseWtcAction {
	@Inject
	private YaohlService yaohlService;
	private static final int PAGE_SIZE = 10000;
	@Inject
	private DownLoadServices downLoadServices;
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private AbstractIBatisDao baseDao;
	private final Logger log = Logger.getLogger(YaohlAction.class);

	/**
	 * getUserInfo获取用户信息方法
	 * 
	 * @return object userInfo
	 */
	public LoginUser getUserInfo() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}

	/**
	 * 要货令管理页面
	 * 
	 * @return 要货令查询
	 */
	public String init() {
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		HttpSession session = request.getSession();
		setResult("usercenter", getUserInfo().getUsercenter());
		if(session.getAttribute("zbcZxc")!=null){
			setResult("zbcZxc", (String) session.getAttribute("zbcZxc"));
		}
		return "yhl";
	}

	/**
	 * 要货令管理页面
	 * 
	 * @return 要货令查询
	 */
	public String nyhl() {
		setResult("usercenter", getUserInfo().getUsercenter());
		return "nyhl";
	}

	/**
	 * 外部要货令查询
	 * 
	 * @return 查询结果
	 */
	public String searchYaohl(@Param Yaohl yhl) {
		Map<String, String> message = new HashMap<String, String>();
		// String sj = getParams().get("sj"); // 获取时间
		// String qssj = getParams().get("qssj");// 起始时间
		// String jssj = getParams().get("jssj");// 起始时间
		String result = "";
		if (!(getParam("lingjbh") != null || getParam("yaohlh") != null || getParam("gongysdm") != null || getParam("dingdh") != null
				|| getParam("keh") != null || getParam("sj") != null || getParam("zt") != null || getParam("jiaofzt") != null)) {
			result = "要货令号、订单号、零件号、供应商编码、客户、时间、要货令状态、到达状态等几个条件必填一项";
			message.put("message", result);
		}
		/*
		 * else if (sj != null && (qssj == null || jssj == null)) { result =
		 * "请选择起始时间和结束时间！"; } else if (sj == null && (qssj != null || jssj !=
		 * null)) { result = "请选择时间状态！"; } else if (qssj != null && jssj != null
		 * && qssj.compareTo(jssj) > 0) { result = "起始时间不能大于结束时间！"; }
		 */
		if (!message.isEmpty()) {
			setResult("result", message);
		} else {
			setResult("result", yaohlService.select(yhl, getParams()));
		}
		return AJAX;
	}

	/**
	 * 导出订单xls
	 * 
	 * @throws Exception
	 * */
	public String yaoHlDownLoadFile(@Param Yaohl yhl, @Param("usercenter") String usercenter, @Param("jihyz") String jihyz,
			@Param("dingdh") String dingdh, @Param("yaohlh") String yaohlh, @Param("lingjbh") String lingjbh, @Param("gongysdm") String gongysdm,
			@Param("keh") String keh, @Param("mudd") String mudd, @Param("sj") String sj, @Param("qssj") String qssj, @Param("jislx") String jislx,
			@Param("jssj") String jssj, @Param("jiaofzt") String jiaofzt, @Param("zt") String zt, @Param("yaohllx") String yaohllx,
			@Param("yaohlzt") String yaohlzt, @Param("chengysdm") String chengysdm,@Param("shifpz") String shifpz,@Param("shiflsk") String shiflsk,@Param("chanx") String chanx,@Param("shiftt") String shiftt) throws Exception {
		getParams();
		List<Yaohl> yaohlList = new ArrayList<Yaohl>();
		if (yaohlh != null && !yaohlh.equals("")) {
			String[] yaoh = yaohlh.split(",");
			for (int i = 0; i < yaoh.length; i++) {
				Map<String, String> param = new HashMap<String, String>();
				param.put("yaohlh", yaoh[i]);
				param.put("usercenter", usercenter);
				List<Yaohl> list1 = this.yaohlService.yaoHlDownLoadFile(null, param);
				yaohlList.addAll(list1);
			}
			Map<String, Object> dataSource = new HashMap<String, Object>();
			dataSource.put("dataSource", yaohlList);
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			this.downLoadServices.downloadFile("yaohl.ftl", dataSource, response, "外部要货令查询终止--订单", ExportConstants.FILE_XLS, false);
		} else {
			Map<String, String> param = new HashMap<String, String>();
			param.put("usercenter", usercenter);
			param.put("jihyz", jihyz);
			param.put("dingdh", dingdh);
			param.put("yaohlh", yaohlh);
			param.put("lingjbh", lingjbh);
			param.put("gongysdm", gongysdm);
			param.put("keh", keh);
			param.put("mudd", mudd);
			param.put("sj", sj);
			param.put("qssj", qssj);
			param.put("jislx", jislx);
			param.put("jssj", jssj);
			param.put("jiaofzt", jiaofzt);
			param.put("zt", zt);
			param.put("yaohllx", yaohllx);
			param.put("yaohlzt", yaohlzt);
			param.put("chengysdm", chengysdm);
			param.put("shifpz", shifpz);
			param.put("shiflsk", shiflsk);
			//gswang 2014-11-29 0010411:要货令查询终止（外部）增加产线 
			param.put("chanx", chanx);
			param.put("shiftt",shiftt);
			moreDataListExport(param, yhl);
		}

		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "外部要货令导出", "外部要货令导出结束");
		return "download";
	}

	@SuppressWarnings("unchecked")
	private void moreDataListExport(Map<String, String> param, Yaohl yhl) {
		// 0007223  相同条件的数据量
		Integer count = (Integer) this.yaohlService.select(yhl, param).get("total");
		yhl.setTotal(count);
		yhl.setPageSize(PAGE_SIZE);
		int pageNos = count / PAGE_SIZE + (count % PAGE_SIZE > 0 ? 1 : 0);
		IProcessData prossData = ProcessDataFactory.getProcessFactoryInstatnces().getProcessDataInstatnces(ExportConstants.FILE_XLS);
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		exportFileBefor(response, "外部要货令查询终止--订单", ExportConstants.FILE_XLS);
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			Map<String, Object> dataSource = new HashMap<String, Object>();
			// 模版head
			dataSource.put("count", count + 1);
			output.write(prossData.processByteCache("yaohl_head.ftl", dataSource));
			// 模版body
			for (int i = 0; i < pageNos; i++) {
				yhl.setPageNo(i + 1);
				dataSource.put("dataSource", (List<Yaohl>)this.yaohlService.select(yhl, param).get("rows"));
				output.write(prossData.processByteCache("yaohl_body.ftl", dataSource));
			}
			// 模版foot
			output.write(prossData.processByteCache("yaohl_foot.ftl", dataSource));
			output.flush();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new ServiceException(ex.getMessage());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					log.error(e.getMessage());
					throw new ServiceException(e.getMessage());
				}
			}
			try {
				response.flushBuffer();
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new ServiceException(e.getMessage());
			}
		}

	}

	private void exportFileBefor(HttpServletResponse response, String filePrefix, String fileSuffix) {
		// 文件 名
		String downLoad = "";
		if (filePrefix != null) {
			try {
				downLoad = URLEncoder.encode(filePrefix, ExportConstants.CODE_UTF8);
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e.getMessage());
			}
		}
		// 设置文件名
		StringBuffer buf = new StringBuffer();
		buf.append(ExportConstants.ATTACHMENT);
		buf.append("\"");
		buf.append(downLoad);
		buf.append("_");
		buf.append(DateUtil.getCurrentDate());
		buf.append(fileSuffix);
		buf.append("\"");

		response.setContentType(ExportConstants.CONTEXT_TYPE);
		response.setCharacterEncoding(ExportConstants.CODE_UTF8);
		response.setHeader(ExportConstants.CONTEXT_DISPOSITION, buf.toString());

	}

	/**
	 * 内部要货令查询
	 */
	public String searchnYaohl(@Param Yaonbhl nyhl) {
		Map<String, String> message = new HashMap<String, String>();
		// String sj = getParams().get("sj"); // 获取时间
		// String qssj = getParams().get("qssj");// 起始时间
		// String jssj = getParams().get("jssj");// 起始时间
		String result = "";
		if (!(getParam("lingjbh") != null || getParam("yaohlh") != null || getParam("cangkbh") != null || getParam("dingdh") != null
				|| getParam("sj") != null || getParam("zt") != null)) {
			result = "要货令号、订单号、零件号、发货仓库、时间、要货令状态等几个条件必填一项";
			message.put("message", result);
			setResult("result", message);
		}
		/*
		 * else if (sj != null && (qssj == null || jssj == null)) { result =
		 * "请选择起始时间和结束时间！"; message.put("message", result); setResult("result",
		 * message); return AJAX; } else if (sj == null && (qssj != null || jssj
		 * != null)) { result = "请选择时间状态！"; message.put("message", result);
		 * setResult("result", message); return AJAX; } else if (qssj != null &&
		 * jssj != null && qssj.compareTo(jssj) > 0) { result = "起始时间不能大于结束时间！";
		 * message.put("message", result); setResult("result", message); return
		 * AJAX; }
		 */
		if (!message.isEmpty()) {
			setResult("result", message);
		} else {
			setResult("result", yaohlService.selectN(nyhl, getParams()));
		}
		return AJAX;

	}

	/**
	 * 导出订单xls
	 * 
	 * @throws Exception
	 * */
	public String nyaoHlDownLoadFile(@Param Yaonbhl nyhl, @Param("usercenter") String usercenter, @Param("jihyz") String jihyz,
			@Param("dingdh") String dingdh, @Param("yaohlh") String yaohlh, @Param("lingjbh") String lingjbh, @Param("gongysdm") String gongysdm,
			@Param("keh") String keh, @Param("mudd") String mudd, @Param("sj") String sj, @Param("qssj") String qssj, @Param("jislx") String jislx,
			@Param("jssj") String jssj, @Param("jiaofzt") String jiaofzt, @Param("zt") String zt, @Param("yaohllx") String yaohllx,
			@Param("yaohlzt") String yaohlzt, @Param("chengysdm") String chengysdm, @Param("cangkbh") String cangkbh,@Param("shifpz") String shifpz) throws Exception {
		getParams();
		List<Yaohl> yaohlList = new ArrayList<Yaohl>();
		if (yaohlh != null && !yaohlh.equals("")) {
			String[] yaoh = yaohlh.split(",");
			for (int i = 0; i < yaoh.length; i++) {
				Map<String, String> param = new HashMap<String, String>();
				param.put("yaohlh", yaoh[i]);
				param.put("usercenter", usercenter);
				List<Yaohl> list1 = this.yaohlService.nyaoHlDownLoadFile(null, param);
				yaohlList.addAll(list1);
			}
			Map<String, Object> dataSource = new HashMap<String, Object>();
			dataSource.put("dataSource", yaohlList);
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			this.downLoadServices.downloadFile("nyaohl.ftl", dataSource, response, "内部要货令查询终止--订单", ExportConstants.FILE_XLS, false);
		} else {
			Map<String, String> param = new HashMap<String, String>();
			param.put("usercenter", usercenter);
			param.put("jihyz", jihyz);
			param.put("dingdh", dingdh);
			param.put("yaohlh", yaohlh);
			param.put("lingjbh", lingjbh);
			param.put("gongysdm", gongysdm);
			param.put("keh", keh);
			param.put("mudd", mudd);
			param.put("sj", sj);
			param.put("qssj", qssj);
			param.put("jislx", jislx);
			param.put("jssj", jssj);
			param.put("jiaofzt", jiaofzt);
			param.put("zt", zt);
			param.put("yaohllx", yaohllx);
			param.put("yaohlzt", yaohlzt);
			param.put("chengysdm", chengysdm);
			param.put("cangkbh", cangkbh);
			param.put("shifpz", shifpz);
			nyaolmoreDataListExport(param,nyhl);
		}

		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "内部要货令导出", "内部要货令导出结束");
		return "download";
	}

	private void nyaolmoreDataListExport(Map<String, String> param,Yaonbhl nyhl) {
		Integer count = this.yaohlService.countYaohlN(param).intValue();
		nyhl.setTotal(count);
		nyhl.setPageSize(PAGE_SIZE);
		int pageNos = count / PAGE_SIZE + (count % PAGE_SIZE > 0 ? 1 : 0);
		IProcessData prossData = ProcessDataFactory.getProcessFactoryInstatnces().getProcessDataInstatnces(ExportConstants.FILE_XLS);
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		exportFileBefor(response, "内部要货令查询终止--订单", ExportConstants.FILE_XLS);
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			Map<String, Object> dataSource = new HashMap<String, Object>();
			// 模版head
			dataSource.put("count", count + 1);
			output.write(prossData.processByteCache("nyaohl_head.ftl", dataSource));
			// 模版body
			for (int i = 0; i < pageNos; i++) {
				nyhl.setPageNo(i + 1);
				dataSource.put("dataSource", this.yaohlService.nyaoHlDownLoadFile(nyhl, param));
				output.write(prossData.processByteCache("nyaohl_body.ftl", dataSource));
			}
			// 模版foot
			output.write(prossData.processByteCache("nyaohl_foot.ftl", dataSource));
			output.flush();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new ServiceException(ex.getMessage());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					log.error(e.getMessage());
					throw new ServiceException(e.getMessage());
				}
			}
			try {
				response.flushBuffer();
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new ServiceException(e.getMessage());
			}
		}

	}

	/**
	 * 终止要货令
	 * 
	 * @return
	 */
	public String ZZYaohl(@Param("edit") ArrayList<Yaohl> ls) {
		// 终止要货令操作
		/*
		 * List<Map<String, String>> paramMap = new ArrayList<Map<String,
		 * String>>(); for (int i = 0; i < ls.size(); i++) { Yaohl yaohl =
		 * ls.get(i); if (!yaohl.getYaohlzt().equals(Const.YAOHL_BIAOD) &&
		 * !yaohl.getYaohlzt().equals(Const.YAOHL_YANCJF)) { setResult("result",
		 * "不能再进行终止，请确认！"); return AJAX; } else { Map<String, String> map = new
		 * HashMap<String, String>(); map.put("yaohlh", yaohl.getYaohlh());
		 * map.put("usercenter", yaohl.getUsercenter()); map.put("lingjbh",
		 * yaohl.getLingjbh()); map.put("dingdmxid", yaohl.getDingdmxid());
		 * map.put("gongysdm", yaohl.getGongysdm()); map.put("yaohlzt",
		 * yaohl.getYaohlzt()); map.put("yaohsl",
		 * String.valueOf(yaohl.getYaohsl())); map.put("editor",
		 * yaohl.getEditor()); map.put("edit_time", yaohl.getEdit_time());
		 * map.put("dingdh", yaohl.getDingdh()); map.put("cangkbh",
		 * yaohl.getCangkbh()); paramMap.add(map); } } try {
		 * yaohlService.updateYhl(paramMap, getUserInfo().getUsername()); }
		 * catch (Exception e) { setResult("result", result); return AJAX; }
		 */
		String yaohllx = "2";
		Map<String, List<Map<String, String>>> csmap = setList(ls);
		int zzsl = getCount(csmap, yaohllx);
		setResult("result", "成功终止" + zzsl + "个要货令");
		return AJAX;

	}

	/**
	 * 终止要货令
	 * 
	 * @return
	 */
	public String ZZYaohlN(@Param("edit") ArrayList<Yaohl> ls) {
		// 终止要货令操作
		/*
		 * String time = CommonFun.getJavaTime(); String info; String banc;
		 * List<Map<String, String>> paramMap = new ArrayList<Map<String,
		 * String>>(); for (int i = 0; i < ls.size(); i++) { Yaonbhl yaonbhl =
		 * ls.get(i); if (!yaonbhl.getYaohlzt().equals(Const.YAOHL_BIAOD) &&
		 * !yaonbhl.getYaohlzt().equals(Const.YAOHL_YANCJF)) {
		 * setResult("result", "不能再进行终止，请确认！"); return AJAX; } else {
		 * Map<String, String> map = new HashMap<String, String>();
		 * map.put("yaohlh", yaonbhl.getYaohlh()); map.put("yaohlzt",
		 * yaonbhl.getYaohlzt()); map.put("usercenter",
		 * yaonbhl.getUsercenter()); map.put("lingjbh", yaonbhl.getLingjbh());
		 * map.put("gongysdm", yaonbhl.getGongysdm()); map.put("yaohsl",
		 * String.valueOf(yaonbhl.getYaohsl())); map.put("editor",
		 * yaonbhl.getEditor()); map.put("edit_time", yaonbhl.getEdit_time());
		 * paramMap.add(map); }
		 * 
		 * } try { yaohlService.updateYhlN(paramMap,
		 * getUserInfo().getUsername()); } catch (Exception e) {
		 * setResult("result", result); return AJAX; }
		 */
		String yaohllx = "1";
		Map<String, List<Map<String, String>>> csmap = setList(ls);
		int zzsl = getCount(csmap, yaohllx);
		setResult("result", "成功终止" + zzsl + "个要货令");
		return AJAX;

	}

	/**
	 * 将要货令集合按照用户中心分类
	 * @param ls 要货令集合
	 * @return
	 */
	public Map<String, List<Map<String, String>>> setList(List<Yaohl> ls) {
		// 0012764 hanwu 20160427	货令终止功能无法终止VD用户中心的数据
		Map<String, List<Map<String, String>>> csmap = new LinkedHashMap<String, List<Map<String, String>>>();
		for (Yaohl yhl : ls) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("yaohlh", yhl.getYaohlh());
			map.put("usercenter", yhl.getUsercenter());
			map.put("lingjbh", yhl.getLingjbh());
			if(csmap.containsKey(yhl.getUsercenter())){
				csmap.get(yhl.getUsercenter()).add(map);
			}else{
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				list.add(map);
				csmap.put(yhl.getUsercenter(), list);
			}
		}
		return csmap;
	}

	@SuppressWarnings("unchecked")
	public int getCount(Map<String, List<Map<String, String>>> csmap, String yaohllx) {
		int amount = 0;
		Set<Entry<String, List<Map<String, String>>>> setDd = csmap.entrySet();
		for (Entry<String, List<Map<String, String>>> entry : setDd) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yaohllx", yaohllx);
			map.put("list", entry.getValue());
			log.debug("要货令终止" + map);
			WtcResponse wtcResponse = callWtc(entry.getKey(), "Y0101", map);
			if (wtcResponse.get("response").equals(Const.WTC_SUSSCESS)) {
				// String response = wtcResponse.get("response").toString();
				Map<String, Object> wtcParameter = (Map<String, Object>) wtcResponse.get("parameter");
				List<Map<String, Object>> result = (List<Map<String, Object>>) wtcParameter.get("list");
				for (Map<String, Object> mapRes : result) {
					Object count = mapRes.get("count");
					if (!count.equals(-1)) {
						if (mapRes.get("yaohllx").equals(Const.STRING_ZERO)) {
							yaohlService.updateYhlN(mapRes, getUserInfo().getUsername());
						} else {
							yaohlService.updateYhl(mapRes, getUserInfo().getUsername());
						}
						amount++;
					}
				}
			}
		}
		return amount;
	}

	/***
	 * 查询零件是否存在
	 * 
	 */
	public String selectLingj() {
		setResult("result", yaohlService.selectLingj(getParams()));

		return AJAX;

	}
	
	
	/**
	 * 要货令批量终止导出模板
	 * @param bean
	 * @author denggq
	 * @date 2012-8-31
	 * @return String
	 */
	public String downloadzhongzyhlMob(@Param Kanbxhgm bean){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downLoadServices.downloadFile("yaohlMob.ftl", dataSurce, response, "要货令批量终止-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "要货令批量终止", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "要货令批量终止", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "要货令批量终止", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 要货令批量终止导入数据
	 * @author denggq
	 * @Date 2018-4-18
	 * @Param bean
	 * @return String
	 * */
	public String uploadyaohlzzdr(@Param Kanbxhgm bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();

			Map<String, String>  map=new HashMap<String,String>();
			//修改看板循环规模最大要货量及供应商交付总量设置为0
			String mes = XlsHandlerUtilsyaohlzz.analyzeXls(map,request,getUserInfo().getUsername(),baseDao);
			
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "要货令批量终止", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "要货令批量终止", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		setResult("usercenter", getUserInfo().getUsercenter());
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("zbcZxc")!=null){
			setResult("zbcZxc", (String) session.getAttribute("zbcZxc"));
		}
		return "upload";
	}
	/**
	 * 批量终止要货令页面
	 * 
	 * @return 要货令查询
	 */
	public String initplzzyhl(@Param String lingjbh,@Param String usercenter) {
		setRequestAttribute("usercenter",getUserInfo().getUsercenter());  
		return "init";
	}
   /**
    * 批量终止要货令查询
    */
	public String queryPlzzyhl(@Param Yaohl yhl) {
		Map map=getParams();
		map.put("creator", getUserInfo().getUsername());
		setResult("result", yaohlService.queryPlzzyhl(yhl,map));
		return AJAX;

	}
	/**
	 *批量终止要货令
	 * @return
	 */
	public String pizzyhl(){
		String loginfo = "";
		String response = "";
		try {
			String usercenter = getUserInfo().getUsercenter();
			Map param = new HashMap();
			//调用WTC批量终止要货令
			WtcResponse wtcResponse = callWtc(usercenter,"Q0311", param);
			//获取WTC
			response = CommonFun.strNull(wtcResponse.get("response"));
			if(response.equals("C_YHL_001")){
				response="参数缺失，用户中心和用户必传";
			}else if(response.equals("C_YHL_002")){
				response="终止要货令失败,请稍候再尝试";
			}
			if(response.equals(Const.WTC_SUSSCESS)){
				loginfo = "批量终止要货令操作成功！";
			}else{
				loginfo = "批量终止要货令调用WTC异常"+response;
			}
			CommonFun.logger.info(loginfo);
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "调用WTC批量终止要货令", loginfo);
		} catch (Exception e) {
			loginfo = "调用WTC批量指定要货令"+CommonUtil.replaceBlank(e.toString()) + response;
			CommonFun.logger.error(loginfo);
			userOperLog.addError(CommonUtil.MODULE_CANGK, "调用WTC批量终止要货令", "调用WTC批量终止要货令", CommonUtil.getClassMethod(), loginfo);
		}
		setResult("result", loginfo);
		return AJAX;
	}
	//// ----11512 xh 0720 start 
	/**
	 * 要货令调整导出模板
	 * @param bean
	 * @author xh
	 * @date 2015-7-18
	 * @return String
	 */
	public String downloadYaohltzMob(){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			downLoadServices.downloadFile("yaohltzMob.ftl", dataSurce, response, "要货令调整-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "要货令调整", "模板下载");
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "要货令调整", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "要货令调整", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	/**
	 * 要货令批量调整导入数据
	 * @author xh
	 * @Date 2015-7-20
	 * @Param bean
	 * @return String
	 * */
	public String uploadyaohltzdr(@Param Yaohl bean){
		Map<String,String> message = new HashMap<String,String>();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			Map<String, String>  map=new HashMap<String,String>();
			//修改看板循环规模最大要货量及供应商交付总量设置为0
			String mes = XlsHandlerUtilsyaohltz.analyzeXls(map,request,getUserInfo().getUsername(),baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "要货令批量调整", "数据导入");
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "要货令批量调整", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		setResult("usercenter", getUserInfo().getUsercenter());
		HttpServletRequest request = ActionContext.getActionContext().getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("zbcZxc")!=null){
			setResult("zbcZxc", (String) session.getAttribute("zbcZxc"));
		}
		return "upload";
	}
	//huxy_V4_023
	public String plcjlsk(@Param Plcjlsk lsk) {
		setResult("usercenter", getUserInfo().getUsercenter());
		return "plcjlsk";
	}
	public String searchPlcjlsk(@Param Plcjlsk lsk) {
		Map<String, String> map = new HashMap<String,String>();
		Map<String, String> message = new HashMap<String,String>();
		if(null == this.getParam("usercenter"))
		{
			message.put("message", "用户中心必选");
			this.setResult("result", message);
		}
		map.put("usercenter", getParam("usercenter"));
		map.put("creator", this.getUserInfo().getUsername());
		map.put("xunhbm", this.getParam("xunhbm"));
		map.put("biaos", this.getParam("biaos"));
		map.put("createTimefrom", this.getParam("createTimefrom"));
		map.put("createTimeto", this.getParam("createTimeto"));
		
		this.setResult("result", yaohlService.queryPlcjlsk(lsk,map));
		return AJAX;
	}
	//创建临时卡，调WTC Q0202
	public String doCreatelsk() 
	{
		Map<String,String> message = new HashMap<String,String>();
		String loginfo = new String();
		String response = new String();
		String transcode = "Q0102";
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", getUserInfo().getUsercenter());
		map.put("creator", getUserInfo().getUsername());
		try{
			WtcResponse wtcResponse = this.callWtc(transcode, map);
			response = CommonFun.strNull(wtcResponse.get("response"));
			if(response.equals(Const.WTC_SUSSCESS)){
				message.put("message", "拆分成功");
			}else{
				message.put("message", "拆分失败");
			}
		}catch(Exception e){
			loginfo = "调用WTC查询拒收跟踪单异常"+CommonUtil.replaceBlank(e.toString()) + response;
			CommonFun.logger.info(loginfo);
		}
		setResult("result", message.get("message"));
		return AJAX;
	}
	//下载模板
	public String downloadPlcjlsk()
	{
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			dataSurce.put("mob", "mob");
			
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			
			downLoadServices.downloadFile("pilcjlsk.ftl", dataSurce, response, "批量创建看板临时卡-模板", ExportConstants.FILE_XLS, false);
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "批量创建看板临时卡", "模板下载");
		
		}catch (ServiceException e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "批量创建看板临时卡", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "批量创建看板临时卡", "模板下载", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	/**
	 * 批量创建临时卡导入数据
	 * @author hxy
	 * @Date 2016-11-13
	 * @Param bean
	 * @return String
	 * */
	public String importAth_pilcklsk(@Param Plcjlsk bean){
		Map<String,String> message = new HashMap<String,String>();
		String userName = this.getUserInfo().getUsername();
		try {
			HttpServletRequest request =ActionContext.getActionContext().getRequest();
			Map<String, String>  map=new HashMap<String,String>();
			String mes = XlsHandlerUtilslsk.analyzeXls(map,request,userName,baseDao);
			if("success".equals(mes)){
				message.put("message", "导入成功");
			}else{
				message.put("message",mes);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "批量创建临时卡导入", "数据导入");
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "批量创建临时卡导入", "数据导入", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setRequestAttribute("uploudmessage",message.get("message"));
		setResult("usercenter", getUserInfo().getUsercenter());
		return "upload";
	}
	//huxy_12940
	public String deletelsk()
	{
		Plcjlsk lsk = new Plcjlsk(); 
		Map<String,String> map = new HashMap<String,String>();
		String liush = this.getParam("liush");
		lsk.setUsercenter(getUserInfo().getUsercenter());
		lsk.setEditor(getUserInfo().getUsername());
		lsk.setLiush(Long.parseLong(liush));
		try{
			map.put("message", yaohlService.deletelsk(lsk));
			userOperLog.addCorrect(CommonUtil.MODULE_CANGK, "批量创建看板临时卡", "数据删除");
		}catch(Exception e){
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CANGK, "批量创建看板临时卡", "数据删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		this.setResult("result", map);
		return AJAX;
	}
}
