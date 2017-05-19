package com.athena.xqjs.module.dfpvdiaobl.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.diaobl.Diaobsq;
import com.athena.xqjs.entity.diaobl.DiaobsqExport;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.dfpvdiaobl.service.DfpvDiaobshService;
import com.athena.xqjs.module.dfpvdiaobl.service.DfpvDiaobsqOperationService;
import com.athena.xqjs.module.dfpvdiaobl.service.DfpvDiaobsqService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * 
 * DFPV调拨申请查询action
 * 
 * @author xss date:2016-1-4
 */
@Component
public class DfpvDiaobsqOperationAction extends ActionSupport {

	@Inject
	private DfpvDiaobshService dfpvdiaobshService;

	@Inject
	private DfpvDiaobsqOperationService dfpvdiaobsqOperationService;
	
	@Inject
	private LingjService lingjService;

	// log4j初始化
	public  final Logger log = Logger.getLogger(DfpvDiaobsqOperationAction.class);
	
	static Logger logger = Logger.getLogger(DfpvDiaobsqOperationAction.class); 
	
	/**
	 * 注入DiaobsqService
	 */
	@Inject
	private DfpvDiaobsqService dfpvdiaobsqService;
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	/**
	 * getUserInfo获取用户信息方法
	 * 
	 * @return object userInfo
	 */
	public LoginUser getUserInfo() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}

	/**
	 * 调拨令查询 -页面初始化
	 */
	public String initQuery() {
		setResult("usercenter", getUserInfo().getUsercenter());
		setResult("creator", getUserInfo().getUsername());
		return "init";
	}

	/**
	 * 调拨令申请主页面查询
	 */
	public String querySq(@Param Diaobsq bean) {
		Map<String, String> map = getParams();
		/*
		 * if (!this.getUserInfo().hasRole(Const.QUANX_POA)) { map.put("jihy",
		 * getUserInfo().getZuh()); }
		 */
		map.put("sh", "70");
		setResult("result", dfpvdiaobshService.select(bean, map));
		return AJAX;
	}

	/**
	 * 增加方法
	 * 
	 * @param bean
	 * @param diaobsqmx
	 * @return
	 */
	public String save(@Param Diaobsqmx bean) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException{
		// 增加操作
		String result = MessageConst.INSERT_COUNT_0;
		String time = CommonFun.getJavaTime();
		Map map=this.getParams();
		boolean info;
		Diaobsq sq = new Diaobsq();
		sq.setUsercenter(bean.getUsercenter());
		sq.setDiaobsqdh(bean.getDiaobsqdh());
		sq = dfpvdiaobsqService.selectDiaobsq(sq);
		if (sq.getZhuangt().compareTo(Const.DIAOBL_ZT_EFFECT) >= 0) {
			setResult("flag", "不能对已生效的做增加操作，请确认！");
			return AJAX;
		}
		if (StringUtils.isEmpty(bean.getYaohsj()) || bean.getYaohsj().compareTo(time.substring(0, 10)) < 0) {
			setResult("flag", "要货时间不能小于当前时间！");
			return AJAX;
		}
		Map<String,Lingj> lingjMap = new HashMap<String, Lingj>();
		List<Lingj> lingjs = lingjService.queryList(this.getParams());
		lingjMap = this.translateListToMap(lingjs, "lingjbh");
//		int num=dfpvdiaobsqService.querylucz(map);
//		if(num==0){
//				setResult("flag", "零件编号为:" + bean.getLingjbh() + "的制造路线有误，请重新输入！");
//				return AJAX;	
//		}
		/*if(!bean.getLux().toString().equals(lingjMap.get(bean.getLingjbh()).getZhizlx()) ){
    		setResult("flag", "零件编号为:" + bean.getLingjbh() + "的制造路线为:" + lingjMap.get(bean.getLingjbh()).getZhizlx());
			return AJAX;
    	}*/
		try {
			bean.setZhuangt(Const.DIAOBL_ZT_APPLYING);
			bean.setCreator(getUserInfo().getUsername());
			bean.setCreate_time(time);
			bean.setEditor(getUserInfo().getUsername());
			bean.setEdit_time(time);
			info = dfpvdiaobsqOperationService.insert(bean);

		} catch (Exception e) {
			setResult("flag", result);
			return AJAX;
		}
		if (!info) {
			setResult("flag", result);
			return AJAX;
		}
		// 更新版次
		String banc = dfpvdiaobsqOperationService.updateBanc(sq);
		result = "新增成功！";
		setResult("banc", banc);
		setResult("zhuangt", sq.getZhuangt());
		setResult("result", result);
		return AJAX;
	}

	/**
	 * 修改方法
	 * 
	 * @param bean
	 * @param diaobsqmx
	 * @return
	 */
	public String update(@Param("edit") ArrayList<Diaobsqmx> ls, @Param Diaobsq sqbean) {
		this.getParams();
		String result = MessageConst.UPDATE_COUNT_0;
		String time = CommonFun.getJavaTime();
		String info;
		String banc;
		Diaobsq bean;
		// 修改操作
		try {
			for (int i = 0; i < ls.size(); i++) {
				// 明细修改
				Diaobsqmx mx = ls.get(i);
				// 当前修改时间
				mx.setNewEdit_time(time);
				// 当前修改人
				mx.setNewEditor(getUserInfo().getUsername());
				String dmxr = dfpvdiaobsqOperationService.checkDiaobmx(mx);
				if(dmxr!=null && !"".equals(dmxr) && dmxr.length()>0){
					setResult("flag", dmxr+"，无法修改！");
					return AJAX;
				}
			}

			bean = dfpvdiaobsqService.selectDiaobsq(sqbean);
			if (bean.getZhuangt().compareTo(Const.DIAOBL_ZT_EFFECT) >= 0) {
				setResult("flag", "不能对已生效的做修改操作，请确认！");
				return AJAX;
			}
			info = dfpvdiaobsqOperationService.update(ls);
			banc = dfpvdiaobsqOperationService.updateBanc(bean);
		} catch (Exception e) {
			setResult("flag", result);
			return AJAX;
		}
		if (info.equals("false")) {
			setResult("flag", result);
			return AJAX;
		}
		result = "修改成功！";
		setResult("banc", banc);
		setResult("zhuangt", bean.getZhuangt());
		setResult("result", result);
		return AJAX;
	}

	/**
	 * 删除方法
	 * 
	 * @param bean
	 * @param diaobsqmx
	 * @return
	 */
	public String delete(@Param("edit") ArrayList<Diaobsqmx> ls, @Param Diaobsq sqbean) {
		// 删除操作
		String result = MessageConst.DELETE_COUNT_0;
		String time = CommonFun.getJavaTime();
		String info;
		String banc;
		Diaobsq bean;
		// 删除操作
		try {
			for (int i = 0; i < ls.size(); i++) {
				Diaobsqmx mx = ls.get(i);
				// 当前修改时间
				mx.setNewEdit_time(time);
				// 当前修改人
				mx.setNewEditor(getUserInfo().getUsername());
				String dmxr = dfpvdiaobsqOperationService.checkDiaobmx(mx);
				if(dmxr!=null && !"".equals(dmxr) && dmxr.length()>0){
					setResult("flag", dmxr+"，无法删除！");
					return AJAX;
				}
			}
			bean = dfpvdiaobsqService.selectDiaobsq(sqbean);
			if (bean.getZhuangt().compareTo(Const.DIAOBL_ZT_EFFECT) >= 0) {
				setResult("flag", "不能对已生效的做删除操作，请确认！");
				return AJAX;
			}
			info = dfpvdiaobsqOperationService.doDelete(ls);
			banc = dfpvdiaobsqOperationService.updateBanc(bean);
		} catch (Exception e) {
			setResult("flag", result);
			return AJAX;
		}
		if (info.equals("false")) {
			setResult("flag", result);
			return AJAX;
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "调拨令查询", "调拨令查询：明细删除");
		result = "删除成功！";
		setResult("banc", banc);
		setResult("zhuangt", bean.getZhuangt());
		setResult("result", result);
		return AJAX;
	}

	/**
	 * 调拨令查询-跳转
	 */
	public String initEdit(@Param Diaobsq bean) {
		String forward = "";
		setResult("diaobsqdh", bean.getDiaobsqdh());
		setResult("usercenter", bean.getUsercenter());
		setResult("diaobsqsj", bean.getDiaobsqsj());
		setResult("banc", bean.getBanc());
		setResult("removeId", getParam("removeId"));
		
		setResult("leix", bean.getLeix());
			
			if (bean.getZhuangt().compareTo(Const.DIAOBL_ZT_PASSED) < 0) {
				forward = "success";
			} else {
				forward = "sqysx";
			}
		return forward;
	}

	/*
	 * 查询-增删改页面
	 */
	public String searchsqmx(@Param("usercenter") String usercenter, @Param("diaobsqdh") String diaobsqdh, @Param("lingjbh") String lingjbh, @Param("zhuangt") String zhuangt) {
		Diaobsq ds = new Diaobsq();
		ds.setUsercenter(usercenter);
		ds.setDiaobsqdh(diaobsqdh);
		ds.setLingjbh(lingjbh);
		ds.setZhuangt(zhuangt);
		Diaobsq sq = dfpvdiaobsqService.selectDiaobsq(ds);
		ds.setPageNo(Integer.parseInt(getParam("pageNo")));
		ds.setPageSize(Integer.parseInt(getParam("pageSize")));
		setResult("banc", sq.getBanc());
		setResult("result", dfpvdiaobshService.sumShipsl(ds));
		return AJAX;
	}

	/**
	 * 调拨令查询-取消
	 */
	public String docancle(@Param("edit") ArrayList<Diaobsq> ls) {
		String result = MessageConst.UPDATE_COUNT_0;
		String time = com.athena.xqjs.module.common.CommonFun.getJavaTime();
		for (int i = 0; i < ls.size(); i++) {
			Diaobsq bean = ls.get(i);
			String zhuangt = bean.getZhuangt();
			if (zhuangt.compareTo(Const.DIAOBL_ZT_EFFECT) >= 0) {
				result = "不能对已生效的申请进行取消！";
				setResult("result", result);
				return AJAX;
			}
			bean.setNewZhuangt(Const.DIAOBL_ZT_cancle);
			bean.setNewEditor(getUserInfo().getUsername());
			bean.setNewEdit_time(time);
		}
		String info = null;
		try {
			info = dfpvdiaobsqOperationService.updateCancle(ls);

		} catch (Exception e) {
			log.info(e.toString());
		}
		if (info.equals("true")) {
			result = "取消成功！";
		}
		setResult("result", result);
		return AJAX;
	}
	
	
	
	/**
	 * 调拨单导出
	 * @return
	 */
	public String downloadDiaobsq()
	{
		
		Map<String,String> message = new HashMap<String,String>();
	
		try
		{
			//将需要导出的数据查询出来
			List<DiaobsqExport> rows = dfpvdiaobsqOperationService.queryDiaobsqExport(this.getParams());
			//将需要导入成EXCEL的数据传入导出方法
			download(rows, "diaobsq_export.ftl", "调拨申请单导出列表");
			userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "调拨申请单导出列表", "数据导出");
		} 
		catch (Exception e) 
		{
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_XQJS, "调拨申请单导出列表", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			log.error(e);
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/*
	 * @see excel导出
	 */
	private void download(List rows,String ftlName , String header) throws Exception
	{
		Map<String, Object> dataSurce = new HashMap<String, Object>();
		dataSurce.put("rows", rows);
		//拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		downloadServices.downloadFile(ftlName, dataSurce, response, header, ExportConstants.FILE_XLS, false);
	}
	
	
	private Map<String,Lingj> translateListToMap(List<Lingj> lingjs,String... propertys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Map<String, Lingj> result = new HashMap<String, Lingj>();
		Lingj lingj = null;
		if(null != lingjs && lingjs.size() > 0 && null != propertys && propertys.length > 0)
		{
			for (int i=0,j=lingjs.size();i<j;i++) 
			{
				lingj = lingjs.get(i);
				StringBuffer flagKey = new StringBuffer();
				for (int k = 0,p = propertys.length; k < p; k++)
				{
					flagKey.append(BeanUtils.getProperty(lingj, propertys[k]));
				}
				result.put(flagKey.toString(), lingj);
			}
		}
		return result;
	}
	
	
	
	
	
	
	
}
