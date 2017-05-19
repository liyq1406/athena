package com.athena.xqjs.module.anxorder.action;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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
import com.athena.xqjs.entity.anxorder.Kucjscsb;
import com.athena.xqjs.entity.anxorder.XbpdBean;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.module.anxorder.service.AnxCshJisService;
import com.athena.xqjs.module.anxorder.service.ChushtjJsService;
import com.athena.xqjs.module.anxorder.service.XinaxCshJisService;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
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
 * 类名称：ChushtjJsAction
 * <p>
 * 类描述： 按需初始化计算
 * </p>
 * 创建人：李智
 * <p>
 * 创建时间：2012-4-11
 * </p>
 * 
 * @version 1.0
 * 
 */

@Component
public class ChushtjJsAction extends ActionSupport {

	@Inject
	//毛需求service
	private MaoxqService maoxqService;
	//初始化提交计算
	@Inject
	private ChushtjJsService chushtjJsService;
	@Inject
	private UserOperLog userOperLog;
	// 注入downloadsevices
	@Inject
	private DownLoadServices downloadServices = null;
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(ChushtjJsAction.class);
	
	/**
	 * 按需初始化计算对象
	 */
	@Inject
	private AnxCshJisService anxCshJis;
	
	/**
	 * 新按需初始化计算对象
	 */
	@Inject
	private XinaxCshJisService xinaxCshJis;
	
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;
	//获取用户中心
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}
	/**
	 * 设置用户中心查询条件
	 * @param params 查询条件
	 * @return 带用户中心的查询条件
	 */
	public Map<String,String> setUsercenter(Map<String,String> params) {
		params.put("usercenter", this.getUserInfo().getUsercenter());
		return params;
	}
	/**
	 * 页面初始化，执行跳转
	 */
	public String execute() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化提交计算", "页面初始化") ;
		return "success";
	}
	
	/**
	 * @：查询毛需求列表
	 * @：李智
	 * @：2012-4-11
	 */
	public String queryListMaoxq(@Param Maoxq bean) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化提交计算", "查询毛需求列表开始") ;
 		Map<String,String> map = new HashMap<String, String>();
		map.put("xuqly", Const.MAOXQ_XUQLY_CJT);
		map.put("xuqly2", Const.MAOXQ_XUQLY_CSX);
		//获取毛需求列表
		setResult("result", this.maoxqService.queryMaoxqByXqlx(bean, map));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化提交计算", "查询毛需求列表结束") ;
		return AJAX;
	}
	/**
	 * @：计算
	 * @：李智
	 * @：2012-4-11
	 */
	public String calculateAnxOrder(@Param Maoxq bean) {
		Map<String,String> map = getParams();
		String loginfo = "";	 
		map.put("jiscldm", Const.JISMK_ANX_CD);//计算处理代码：按需订单计算(33)
		try {
			//判断处理状态,是否有计算进行中
			if(jiscclssz.checkState(Const.JISMK_ANX_CD)){
				loginfo = "有计算正在进行,请稍后再计算";
				setResult("result", loginfo);
			}else{
				//更新处理状态为1,计算中
				jiscclssz.updateState(map,Const.JSZT_EXECUTING);
				userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化提交计算", "计算开始") ;
				String jisLx = getParam("jisLx");
				anxCshJis.cshAnxOrderMethod(jisLx, getUserInfo().getUsername());
				userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化提交计算", "计算结束") ;
				setResult("result", "计算成功！");
				//计算完成更新处理状态为0,计算完成
				jiscclssz.updateState(map,Const.JSZT_SURE);
				loginfo = "计算成功";
				setResult("result", loginfo);
			}
		} catch (Exception e) {
			loginfo = CommonUtil.replaceBlank(e.toString());
			setErrorMessage("按需初始化计算异常"+loginfo);
			logger.error("按需初始化计算计算异常"+e);
			userOperLog.addError(CommonUtil.MODULE_XQJS, "按需初始化计算", "计算结束", CommonUtil.getClassMethod(), loginfo) ;
			//异常时将处理状态更新为0
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
		}
		return AJAX;
	}

	/**
	 * 线边理论库存查询
	 * 
	 * @param bean
	 * @return
	 */
	public String queryxbkpd(@Param Maoxq bean) {
		setResult("result", chushtjJsService.xbpdxzQuery(bean, getParams()));
		return AJAX;
	}
	
	
	public String addYicdxh(@Param("list") ArrayList<Kucjscsb> params){
		try {
			chushtjJsService.addYicdxh(params,getUserInfo().getUsername());
			setResult("result", "保存成功");
			userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "线边理论库存盘点", "保存异常待消耗");
			logger.info(getUserInfo().getUsername()+"线边理论库存盘点保存异常待消耗");
		} catch (Exception e) {
			setResult("result", "保存失败");
			logger.error("线边理论库存盘点保存异常待消耗异常"+e);
		}
		return AJAX; 
	}
	
	/**
	 * 判断流水号是否存在
	 * 
	 * @return
	 */
	public String checkLsh() {
		setResult("result", chushtjJsService.pdjsdxh(getParams()));
		return AJAX;
	}

	// 盘点导出
	public String downLoadFilePd() throws IllegalAccessException, InvocationTargetException, InstantiationException {
		String str = getParam("param");
		ArrayList<XbpdBean> expls = new ArrayList<XbpdBean>(50);
		if (str.indexOf("[") != -1) {
			String[] pls = str.split("&");
			Class<XbpdBean> c = XbpdBean.class;
			// 获取字段对象集合
			Field[] fields = c.getDeclaredFields();
			for (int j = 0; j < fields.length; j++) {
				Field f = fields[j];
				f.setAccessible(true);
				if ("serialVersionUID".equals(f.getName())) {
					continue;
				}
				for (int i = 0; i < pls.length; i++) {
					String fieldvalueStr = pls[i];
					if (fieldvalueStr.indexOf(f.getName()) != -1) {
						if (j == 1) {
							// 创建实例
							XbpdBean temp = c.newInstance();
							Object value = fieldvalueStr.substring(fieldvalueStr.lastIndexOf("=") + 1);
							value = f.getType() == BigDecimal.class ? new BigDecimal(value.toString()) : value.toString();
							org.apache.commons.beanutils.BeanUtils.setProperty(temp, f.getName(), value);
							expls.add(temp);
						} else {
							Integer index = Integer.parseInt(fieldvalueStr.substring(fieldvalueStr.indexOf("[") + 1, fieldvalueStr.lastIndexOf("]")));
							Object value = fieldvalueStr.substring(fieldvalueStr.lastIndexOf("=") + 1);
							value = f.getType() == BigDecimal.class ? new BigDecimal(value.toString()) : value.toString();
							org.apache.commons.beanutils.BeanUtils.setProperty(expls.get(index), f.getName(), value);
						}
					}
				}

			}
		}
		Map<String, Object> dataSource = new HashMap<String, Object>();
		dataSource.put("list", expls);
		// 拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse();
		downloadServices.downloadFile("xbpdDownxqjs.ftl", dataSource, response, "线边库存盘点", ExportConstants.FILE_XLS, false);

		// 返回类型一定要是download类型的
		return "downLoad";

	}

	public String executeXbpd() {
		setResult("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());
		return "success";
	}
	
	/**
	 * 新按需初始化计算初始页面
	 */
	public String executeXinax() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "新按需初始化提交计算", "页面初始化") ;
		return "success";
	}
	/**
	 * 新按需初始化计算
	 */
	public String calculateXinaxOrder(@Param Maoxq bean) {
		Map<String,String> map = getParams();
		String loginfo = "";	 
		map.put("jiscldm", Const.JISMK_ANX_CD);//计算处理代码：按需订单计算(33)
		try {
			//判断处理状态,是否有计算进行中
			if(jiscclssz.checkState(Const.JISMK_ANX_CD)){
				loginfo = "有计算正在进行,请稍后再计算";
				setResult("result", loginfo);
			}else{
				//更新处理状态为1,计算中
				jiscclssz.updateState(map,Const.JSZT_EXECUTING);
				
				userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化提交计算", "计算开始") ;
				String jisLx = getParam("jisLx");
				String usercenter = getParam("usercenter");
				xinaxCshJis.cshAnxOrderMethod(jisLx, getUserInfo().getUsername(),usercenter);
				userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化提交计算", "计算结束") ;
				
				setResult("result", "计算成功！");
				//计算完成更新处理状态为0,计算完成
				jiscclssz.updateState(map,Const.JSZT_SURE);
				loginfo = "计算成功";
				setResult("result", loginfo);
			}
		} catch (Exception e) {
			loginfo = CommonUtil.replaceBlank(e.toString());
			setErrorMessage("按需初始化计算异常"+loginfo);
			logger.error("按需初始化计算计算异常"+e);
			userOperLog.addError(CommonUtil.MODULE_XQJS, "按需初始化计算", "计算结束", CommonUtil.getClassMethod(), loginfo) ;
			//异常时将处理状态更新为0
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
		}
		return AJAX;
	}
	
	/**
	 * 根据用户中心获取对应的模式
	 */
	public String queryMosbyUserCenter(){
		String usercenter = getParam("usercenter");
		List mosList = xinaxCshJis.queryMosbyUserCenter(usercenter);
		setResult("result",mosList);
		return AJAX;
		
	}
}
