package com.athena.xqjs.module.anxorder.action;

import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.module.anxorder.service.AnxJisService;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;

/**
 * 类描述： 按需计算
 * 创建人：李明
 * 创建时间：2012-4-25
 * @version 1.0
 */
@Component
public class AnxOrderAction extends ActionSupport {

	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private AnxJisService anxJisService;
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(AnxOrderAction.class);
	
	//获取用户中心
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}


	/**
	 * @：按需计算
	 * @：李明
	 * @：2012-4-25
	 */
	public String anxOrderCount() {
		String loginfo = "";	 
		Map<String,String> map = getParams();
		map.put("jiscldm", Const.JISMK_ANX_CD);//计算处理代码：按需订单计算(33)
		map.put("usercenter", getUserInfo().getUsercenter());//用户中心
		try {
			//判断处理状态,是否有计算进行中
			if(jiscclssz.checkState(map)){
				loginfo = "有计算正在进行,请稍后再计算";
				setResult("result", loginfo);
			}else{
				//更新处理状态为1,计算中 
				//jiscclssz.updateState(map,Const.JSZT_EXECUTING);
				userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需计算", "计算开始") ;
				anxJisService.anxOrderMethod(getUserInfo().getUsername(),getUserInfo().getUsercenter());
				userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需计算", "计算结束") ;
				//计算完成更新处理状态为0,计算完成
				jiscclssz.updateState(map,Const.JSZT_SURE);
				loginfo = "计算成功";
				setResult("result", loginfo);
			}
		} catch (Exception e) {
			loginfo = CommonUtil.replaceBlank(e.toString());
			setErrorMessage("计算异常"+loginfo);
			logger.error("按需计算计算异常"+loginfo);
			userOperLog.addError(CommonUtil.MODULE_XQJS, "按需计算", "计算结束", CommonUtil.getClassMethod(), loginfo) ;
			//异常时将处理状态更新为0
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
		}
		return AJAX;
	}

}
