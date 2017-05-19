package com.athena.xqjs.module.zygzbj.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.util.AuthorityUtils;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.zygzbj.service.ZygzbjjsService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ZygzbjjsAction
 * <p>
 * 类描述：资源跟踪报警计算action
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-01-18
 * </p>
 * 
 * @version 1.0
 * 
 */  
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ZygzbjjsAction extends ActionSupport {
	
	/**
	 * 资源跟踪报警计算service
	 */
	@Inject
	private ZygzbjjsService zygzbjjs;
	
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;
	
	@Inject
	private UserOperLog log;
	
	/**
	 * 资源跟踪报警计算页面初始化
	 * @return 定位页面
	 */
	public String initJs(){
		setResult("zyhqrq", CommonFun.sdf.format(new Date()));
		return "zygzbjjs";
	}
	
	/**
	 * 查询毛需求
	 * @param jslx 计算类型
	 * @return 毛需求版次信息
	 */
	public String selectMxq(@Param Maoxq mxq){
		Map<String,String> map = getParams();
		map.put("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());//设置用户中心
		setResult("result", zygzbjjs.queryMxq(mxq, map));
		return AJAX;
	}
	
	/**
	 * 毛需求明细页面跳转
	 * @return 定位页面
	 */
	public String mxqMx(){
		setResult("xuqbc", getParam("xuqbc"));
		setResult("removeId", getParam("removeId")); 
		setResult("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());
		return "mxqmx";
	}
	
	/**
	 * 查询毛需求明细信息
	 * @param jslx 计算类型
	 * @return 毛需求版次信息
	 */
	public String selectMxqMx(@Param Maoxqmx mxqmx){
 		Map<String,String> map = getParams();
		//map.put("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());//设置用户中心
		setResult("result", zygzbjjs.selMxqmx(mxqmx,map));
		return AJAX;
	} 
	
	/**
	 * 查询资源获取日期
	 * @param jslx 计算类型
	 * @return 毛需求版次信息
	 */
	public String queryZyhqrq(){
		setResult("result", zygzbjjs.queryZiyhqrl());
		return AJAX;
	}
	
	/**
	 * 查询资源计算类型
	 * @param jslx 计算类型
	 * @return 毛需求版次信息
	 */
	public String queryZyJslx(){
		setResult("result", zygzbjjs.queryZyJslx());
		return AJAX;
	}
	
	/**
	 * 资源跟踪报警计算
	 * @return 计算结果
	 */
	public String jiS(@Param("edit") ArrayList<Maoxq> edit){
		String loginfo = "";	 
		Map<String,String> map =new HashMap<String, String>();
		map.put("jsrq", CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD));//计算日期
		//0012843-xss-20160913  
		map.put("username", AuthorityUtils.getSecurityUser().getUsername());//设置用户姓名
		map.put("jiscldm", Const.JISMK_GZBJ_CD);//计算处理代码：跟踪报警（50）
		
		//计算用参数
		//xss_20161227
		Map<String,String> map2 = getParams();
		map2.put("jsrq", CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD));//计算日期
		map2.put("usercenter", AuthorityUtils.getSecurityUser().getUsercenter());//设置用户中心
		map2.put("username", AuthorityUtils.getSecurityUser().getUsername());//设置用户姓名
		map2.put("jiscldm", Const.JISMK_GZBJ_CD);//计算处理代码：跟踪报警（50）
		
		try {
			//判断处理状态,是否有计算进行中
			if(jiscclssz.checkState(map)){
				loginfo = "有计算正在进行,请稍后再计算";
				setResult("result", loginfo);
			}else{
				//更新处理状态为20,计算中
				jiscclssz.updateState(map,Const.JSZT_EXECUTING);
				//计算
				zygzbjjs.jiS(map2,edit);//xss_20161227
				//计算完成更新处理状态为90,计算完成
				jiscclssz.updateState(map,Const.JSZT_SURE);
				loginfo = "计算成功";
				setResult("result", loginfo);
			}
			CommonFun.logger.info(loginfo);
			log.addCorrect(CommonUtil.MODULE_XQJS,"资源跟踪报警计算", loginfo);
		} catch (Exception e) {
			loginfo = "资源跟踪报警计算异常"+CommonUtil.replaceBlank(e.toString());
			CommonFun.logger.error(loginfo);
			setResult("result","计算异常" + e.toString());
			log.addError(CommonUtil.MODULE_XQJS, "卷料订单定义", "卷料订单定义异常", CommonUtil.getClassMethod(), loginfo);
			//异常时将处理状态更新为99
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
		}
		return AJAX;
	}
}
