package com.athena.xqjs.module.report.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.report.Kucfx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.ilorder.service.MaoxqService;
import com.athena.xqjs.module.report.service.KucfxService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/*
**
* <p>
* 项目名称：athena-xqjs
* </p>
* 类名称：IlKucfxAction
* <p>
* 类描述： 库存分析报表
* </p>
* 创建人：wuyichao
* <p>
* 创建时间：2013/3/25
* </p>
* 
* @version 1.0
* 
*/
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KucfxAction extends ActionSupport {
	//日志
	static Logger logger = Logger.getLogger(KucfxAction.class); 
	
	@Inject
	private KucfxService kucfxService;
	
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	
	@Inject
	private UserOperLog log;
	
	@Inject
	private MaoxqService maoxqService;
	
	@Inject
	private JisclcsszService jiscclssz;    //计算锁
	
	
	//获取用户中心
	public LoginUser getUserInfo() 
	{
		return AuthorityUtils.getSecurityUser() ;
	}
	
	/**
	 * 2.15.6.5IL库存天数分析（新）
	 * 初始化国产件库存分析页面
	 * @return
	 */
	public String initIlPage()
	{
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "il";
	}	
	
	
	/**
	 * @see   国产件库存分析 
	 * @param bancs
	 * @return
	 */
	public String calculateIl(@Param("banc") ArrayList<Maoxq> bancs)
	{
		Map<String, String> searchMap = initCalculate(getParams(),Const.JISMK_ILKCJS_CD);
		if(!this.checkCalculateState(searchMap)) //查看是否有人在计算
		{
			if(null != bancs)  // 查看参数是否正确
			{
				this.updatCalculateState(searchMap, Const.JSZT_EXECUTING);
				try
				{
					setResult("result",kucfxService.calculateIl(bancs,searchMap));
				}
				catch(Exception e)
				{
					this.updatCalculateState(searchMap,Const.JSZT_EXECPTION);
					setResult("result","计算参数有误.");
				}
				this.updatCalculateState(searchMap, Const.JSZT_SURE);
			}
			else
			{
				setResult("result","计算参数有误,没有选择毛需求版次.");
			}
		}
		else
		{
			setResult("result","有计算正在进行,请稍后再计算");
		}
		return AJAX;
	}
	
	
	/**
	 * @see   国产件库存分析 
	 * @param bancs
	 * @return
	 */
	public String calculateKd(@Param("banc") ArrayList<Maoxq> bancs)
	{
		Map<String, String> searchMap = initCalculate(getParams(),Const.JISMK_KDKCJS_CD);
		if(!this.checkCalculateState(searchMap)) //查看是否有人在计算
		{
			if(null != bancs)  // 查看参数是否正确
			{
				this.updatCalculateState(searchMap, Const.JSZT_EXECUTING);
				try
				{
					setResult("result",kucfxService.calculateKd(bancs,searchMap));
				}
				catch(Exception e)
				{
					this.updatCalculateState(searchMap,Const.JSZT_EXECPTION);
					setResult("result","计算参数有误.");
				}
				this.updatCalculateState(searchMap, Const.JSZT_SURE);
			}
			else
			{
				setResult("result","计算参数有误,没有选择毛需求版次.");
			}
		}
		else
		{
			setResult("result","有计算正在进行,请稍后再计算");
		}
		return AJAX;
	}
	
	
	
	/**
	 * @see   国产件库存分析 
	 * @param bancs
	 * @return
	 */
	public String calculateBj(@Param("banc") ArrayList<Maoxq> bancs)
	{
		Map<String, String> searchMap = initCalculate(getParams(),Const.JISMK_BJKCJS_CD);
		if(!this.checkCalculateState(searchMap)) //查看是否有人在计算
		{
			if(null != bancs)  // 查看参数是否正确
			{
				this.updatCalculateState(searchMap, Const.JSZT_EXECUTING);
				try
				{
					setResult("result",kucfxService.calculateBj(bancs,searchMap));
				}
				catch(Exception e)
				{
					this.updatCalculateState(searchMap,Const.JSZT_EXECPTION);
					setResult("result","计算参数有误.");
				}
				this.updatCalculateState(searchMap, Const.JSZT_SURE);
			}
			else
			{
				setResult("result","计算参数有误,没有选择毛需求版次.");
			}
		}
		else
		{
			setResult("result","有计算正在进行,请稍后再计算");
		}
		return AJAX;
	}
	
	
	/**
	 * @see 对计算锁进行状态更改
	 * @param searchMap
	 * @param jSZT
	 */
	private void updatCalculateState(Map<String, String> searchMap,
			String jSZT) {
		if(null != searchMap && StringUtils.isNotBlank(searchMap.get("jiscldm")))
		{
			jiscclssz.updateState(searchMap, jSZT);
		}
	}

	/***
	 * @see   初始化计算时需要的数据
	 * @param params
	 * @param jismkIlkcjsCd
	 * @return
	 */
	private Map<String, String> initCalculate(Map<String, String> params,
			String jismkIlkcjsCd) {
		if(params != null)
		{
			params.put("jsrq", CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD));// 计算日期
//		    params.put("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());// 设置用户中心
			params.put("username", AuthorityUtils.getSecurityUser().getUsername());// 设置用户姓名
			params.put("jiscldm", jismkIlkcjsCd);
		}
		return params;
	}

	/**
	 * @see 查看是否有人在计算
	 * @param searchMap
	 * @return
	 */
	private boolean checkCalculateState(Map<String, String> searchMap) {
		if(null != searchMap && StringUtils.isNotBlank(searchMap.get("jiscldm")))
		{
			return jiscclssz.checkState(searchMap);
		}
		return true;
	}

	/**
	 * 2.15.6.5KD库存天数分析（新）
	 * 初始化KD件库存分析页面
	 * @return
	 */
	public String initKdPage()
	{
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "kd";
	}	
	
	/**
	 * 2.15.6.5(3)KD件15天库存不足报警(新)
	 * 初始化KD件库存分析页面
	 * @return
	 */
	public String initBjPage()
	{
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "bj";
	}	
	
	
	
	/**
	 * 2.15.6.5IL库存天数分析（新）
	 * 查询库存信息
	 * @return
	 */
	public String queryIlKuc(@Param Kucfx kucfx)
	{
		try 
		{
			
			setResult("result",kucfxService.queryIlKucfxByPage(this.getParams(),kucfx));
		} 
		catch (Exception e)
		{
			setErrorMessage("查询错误!" + e.toString());
		}
		return AJAX;
	}
	
	/**
	 * 2.15.6.5KD库存天数分析（新）
	 * 查询库存信息
	 * @return
	 */
	public String queryKdKuc(@Param Kucfx kucfx)
	{
		try 
		{
			setResult("result",kucfxService.queryKdKucfxByPage(this.getParams(),kucfx));
		} 
		catch (Exception e)
		{
			setErrorMessage("查询错误!" + e.toString());
		}
		return AJAX;
	}
	
	/**
	 *  2.15.6.5(3)KD件15天库存不足报警(新)
	 * 查询库存信息
	 * @return
	 */
	public String queryBjKuc(@Param Kucfx kucfx)
	{
		try 
		{
			Map result = kucfxService.queryBjKucfxByPage(this.getParams(),kucfx);
			if(null != result)
			{
				setResult("result",result);
			}
			else
			{
				setErrorMessage("日历版次内没有当前时间后推15天的数据,请添加!");
			}
		} 
		catch (Exception e)
		{
			setErrorMessage("查询错误!" + e.toString());
		}
		return AJAX;
	}
	
	
	/**
	 * 2.15.6.5IL库存天数分析（新）
	 * @return
	 */
	public String downloadIlKuc()
	{
		Map<String,String> message = new HashMap<String,String>();
		try
		{
			List<Kucfx> rows = kucfxService.queryIlKucfx(this.getParams());
			download(rows, "ilKucfx.ftl", "国产件库存分析报表");
			log.addCorrect(CommonUtil.MODULE_XQJS, "国产件库存分析报表", "数据导出");
		} 
		catch (Exception e) 
		{
			message.put("message", e.getMessage());
			log.addError(CommonUtil.MODULE_XQJS, "国产件库存分析报表", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}

	/**
	 * 2.15.6.5KD库存天数分析（新）
	 * @return
	 */
	public String downloadKdKuc()
	{
		Map<String,String> message = new HashMap<String,String>();
		try
		{
			List<Kucfx> rows = kucfxService.queryKdKucfx(this.getParams());
			download(rows, "kdKucfx.ftl", "KD件库存分析报表");
			log.addCorrect(CommonUtil.MODULE_XQJS, "KD件库存分析报表", "数据导出");
		} 
		catch (Exception e) 
		{
			message.put("message", e.getMessage());
			log.addError(CommonUtil.MODULE_XQJS, "KD件库存分析报表", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	/**
	 * 2.15.6.5 KD件库存十五天库存报警
	 * @return
	 */
	public String downloadBjKuc()
	{
		Map<String,String> message = new HashMap<String,String>();
		try
		{
			List<Kucfx> rows = kucfxService.queryBjKucfx(this.getParams());
			download(rows, "kdKucfx.ftl", "KD件库存十五天库存报警分析报表");
			log.addCorrect(CommonUtil.MODULE_XQJS, "KD件库存十五天库存报警分析报表", "数据导出");
		} 
		catch (Exception e) 
		{
			message.put("message", e.getMessage());
			log.addError(CommonUtil.MODULE_XQJS, "KD件库存十五天库存报警报表", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	private void download(List rows,String ftlName , String header) throws Exception
	{
		Map<String, Object> dataSurce = new HashMap<String, Object>();
		dataSurce.put("rows", rows);
		//拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		downloadServices.downloadFile(ftlName, dataSurce, response, header, ExportConstants.FILE_XLS, false);
	}
	
	
	
	
	public String querySessionMaoxqIlKcfxbb() {

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
					for (int i = 0; i < list.size(); i++) {
						Maoxq oneMaoxq = list.get(i);
						if (oneMaoxq.getXuqly().equalsIgnoreCase("CLV")|| "CLV".equals(maoxq.getXuqly())) {
							setErrorMessage("CLV毛需求来源与其他类型毛需求来源互斥！");
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
		return AJAX;
	}
}
