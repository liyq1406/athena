package com.athena.xqjs.module.zygzbj.action;

import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.xqjs.entity.zygzbj.Mafmxq;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.zygzbj.service.MafmxqService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：MafMxqAction
 * <p>
 * 类描述：MAF库毛需求对比action2.15.9.12
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-02-13
 * </p>
 * 
 * @version 1.0
 * 
 */  
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class MafMxqAction extends ActionSupport {
	
	@Inject
	private MafmxqService mafmxq;
	
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;
	
	/**
	 * maf毛需求比对
	 * @return
	 */
	public String initMafmxq(){
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("usercenter",loginUser.getUsercenter());
		return "mafmxq";
	}
	
	/**
	 * 查询MAF毛需求对比
	 * @return
	 */
	public String queryMafmxq(@Param Mafmxq maf){
		try {
			setResult("result", mafmxq.queryMafmxq(maf,getParams()));
		} catch (Exception e) {
			setErrorMessage("对比异常" + e.toString());
		}
		return AJAX;
	}
	
	/**
	 * MAF毛需求对比
	 * @return
	 */
	public String maoxqDuib(){
		Map<String,String> map = getParams();
		try {
			map.put("jiscldm", Const.JISMK_GZBJ_MAF);//计算处理代码：跟踪报警MAF库毛需求对比(51)
			//判断处理状态,是否有计算进行中
			if(jiscclssz.checkState(map)){
				setResult("result", "有人正在操作,请稍后再进行操作");
			}else{
				//更新处理状态为1,计算中
				jiscclssz.updateState(map,Const.JSZT_EXECUTING);
				mafmxq.maoxqDuib(map);
				//计算完成更新处理状态为0,计算完成
				jiscclssz.updateState(map,Const.JSZT_SURE);
				setResult("result", "对比成功");
			}
		} catch (Exception e) {
			setErrorMessage("对比异常" + e.toString());
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
		}
		return AJAX;
	}
}
