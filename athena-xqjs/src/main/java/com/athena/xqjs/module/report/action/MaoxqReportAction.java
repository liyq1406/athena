package com.athena.xqjs.module.report.action;

import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.util.AuthorityUtils;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.report.RepMaoxq;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.report.service.MaoxqReportService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 毛需求报表2.15.1.6
 * @author WL
 *
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class MaoxqReportAction extends ActionSupport {

	@Inject
	private MaoxqReportService maoxqReportService;
	
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;
	
	@Inject
	private UserOperLog userOperLog;
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(MaoxqReportAction.class);
	
	/**
	 * 初始化毛需求报表信息
	 * @return
	 */
	public String initMaoxqReport(){
		setResult("usercenter",AuthorityUtils.getSecurityUser().getUsercenter());
		return "maoxqReport";
	}
	
	/**
	 * 毛需求分析
	 * @return
	 */
	public String maoxqFenx(){
		String loginfo = "";	 
		Map<String,String> map = getParams();
		map.put("jiscldm", Const.JISMK_MAOXQ_REP);//计算处理代码：毛需求报表(11)
		try {
			//判断处理状态,是否有计算进行中
			if(jiscclssz.checkState(map)){
				loginfo = "有人正在进行毛需求分析,请稍后";
				setResult("result", loginfo);
			}else{
				//更新处理状态为1,计算中
				jiscclssz.updateState(map,Const.JSZT_EXECUTING);
				userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "毛需求分析报表", "计算开始") ;
				maoxqReportService.maoxqFenx(getParams(),AuthorityUtils.getSecurityUser().getUsername());
				userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "毛需求分析报表", "计算结束") ;
				//计算完成更新处理状态为0,计算完成
				jiscclssz.updateState(map,Const.JSZT_SURE);
				loginfo = "分析成功";
				setResult("result", loginfo);
			}
		} catch (Exception e) {
			loginfo = CommonUtil.replaceBlank(e.toString());
			setErrorMessage("计算异常"+loginfo);
			logger.error("按需计算计算异常"+loginfo);
			userOperLog.addError(CommonUtil.MODULE_XQJS, "毛需求分析报表", "计算结束", CommonUtil.getClassMethod(), loginfo) ;
			//异常时将处理状态更新为0
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
		}
		return AJAX;
	}
	
	/**
	 * 毛需求查询
	 * @param repMaoxq
	 * @return
	 */
	public String queryMaoxq(@Param RepMaoxq repMaoxq){
		setResult("result", maoxqReportService.queryMaoxq(repMaoxq, getParams()));
		return AJAX;
	}
}
