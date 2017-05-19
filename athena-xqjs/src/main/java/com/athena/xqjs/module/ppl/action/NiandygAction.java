package com.athena.xqjs.module.ppl.action;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.LoginUser;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.ilorder.service.MaoxqService;
import com.athena.xqjs.module.ppl.service.NiandygService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * PPL年度预告Action
 * 
 * @author Xiahui
 * @date 2011-12-21
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class NiandygAction extends ActionSupport {
	@Inject
	private NiandygService niandygService;
	@Inject
	private MaoxqService maoxqService;
	
    @Inject
    private JisclcsszService jisclcsszService;
    
	private Log log = LogFactory.getLog(getClass());

	// 获取用户信息
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser();
	}

	public String access_Niandyg() {
		return "select";
	}

	// 查询毛需求
	public String queryMaoxq(@Param Maoxq bean) {
		setResult("result", maoxqService.select(bean, getParams()));
		return AJAX;
	}

	// 年度预告计算确认画面
	public String niandygQuer(@Param Maoxq bean) throws ParseException, UnsupportedEncodingException {
		// System.out.println(getParams());
		log.info(getParams());
		System.out.println(new String(getParam("xuqcfsj").getBytes("ISO-8859-1"),"UTF-8"));
		setResult("xuqbc", getParam("xuqbc"));
		setResult("LX", getParam("LX"));
		setResult("jisnf", getParam("jisnf"));
		setResult("xuqly", getParam("xuqly"));
		setResult("xuqcfsj",new String(getParam("xuqcfsj").getBytes("ISO-8859-1"),"UTF-8"));
		return "niandygQuer";

	}

	// 判断计算年份是否大于当前年份
	public String comparetime(@Param Maoxq bean) {
		// 获取计算年份
		String jisnf = getParam("jisnf");
		String date3 = CommonFun.getJavaTime().substring(0, 4);
		boolean result;
		// 比较计算年份
		if (Integer.parseInt(jisnf) >= Integer.parseInt(date3)) {
			result = true;
		} else {
			result = false;
		}
		setResult("result", result);
		return AJAX;
	}

	// 年度预告计算
	public String niandygJis() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("creator", getUserInfo().getUsername());
		params.put("create_time", CommonFun.getJavaTime());
		params.put("editor", getUserInfo().getUsername());
		params.put("edit_time", CommonFun.getJavaTime());
		params.put("JISNF", getParam("jisnf"));
		params.put("XUQBC", getParam("xuqbc"));
		params.put("LX", getParam("LX"));
		params.put("usercenter", getUserInfo().getUsercenter());
		params.put("xuqcfsj", getParam("xuqcfsj"));
		params.put("jihydm", getUserInfo().getZuh());
		//ppl计算状态
		Map<String,String> map = new HashMap<String, String>();
		map.put("jiscldm", Const.JSMK_PPL);
		try {
			//判断处理状态,是否有计算进行中
			if(jisclcsszService.checkState(map)){
				setResult("result", "有计算正在进行,请稍后再计算");
			}else{
				//更新处理状态为1,计算中
				jisclcsszService.updateState(map,Const.JSZT_EXECUTING);
				//计算
				niandygService.niandygCollect(params);
				//计算完成更新处理状态为0,计算完成
				jisclcsszService.updateState(map,Const.JSZT_SURE);
				setResult("result", "ppl年度预告计算成功！");
			}
		} catch (Exception e) {
			log.error("PPL计算异常", e);
			//异常时将处理状态更新为0
			jisclcsszService.updateState(map,Const.JSZT_EXECPTION);

			if (e.getMessage().endsWith(MessageConst.EXCEPTION_CHONGFU)) {
				setResult("result", MessageConst.EXCEPTION_CHONGFU);
				return AJAX;
			}
			setResult("result", "计算异常");
		}
		return AJAX;
	}
}
