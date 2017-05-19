package com.athena.xqjs.module.ilorder.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.athena.xqjs.entity.common.GonghmsMaoxq;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.service.GonghmsMaoxqService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.MaoxqService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：IldingddyAction
 * <p>
 * 类描述： Il订单计算
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-4-17
 * </p>
 * 
 * @version 1.0
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class IldingdjsAction extends ActionSupport {
	
	/**
	 * 注入DingdService
	 */
	@Inject
	private DingdService dingdService;
	
	//供货模式-毛需求service
	@Inject
	private GonghmsMaoxqService gonghmsMaoxqService;
	
	@Inject
	//毛需求service
	private MaoxqService maoxqService;
	
	/**
	 * 订单定义页面初始化
	 * @return
	 */
	public String initDingdjs(){
		return "ildingdjs";
	}
	
	/**
	 * 查询订单号
	 * @return 订单号下拉列表
	 */
	public String queryDingdh(@Param Dingd dingd){
		setResult("result", dingdService.queryDingdhs(dingd,getParams()));
		return AJAX;
	}
	
	/**
	 * 查询需求来源
	 * @return
	 */
	public String queryXuqly(){
		setResult("result", gonghmsMaoxqService.queryGonghmsMaoxq(CommonFun.setUsercenter(getParams())));
		return AJAX;
	}
	
	public String queryXuqlyIl(){
		List<GonghmsMaoxq> gonghmsMaoxqs = gonghmsMaoxqService.queryGonghmsMaoxq(CommonFun.setUsercenter(getParams()));
		List<GonghmsMaoxq> il = new ArrayList<GonghmsMaoxq>();
		for (GonghmsMaoxq gonghmsMaoxq : gonghmsMaoxqs) {
			String flag = gonghmsMaoxq.getXuqly();
			if(!(StringUtils.isNotBlank(flag) && flag.equalsIgnoreCase("DKS")))
			{
				il.add(gonghmsMaoxq);
			}
		}
		setResult("result", il);
		return AJAX;
	}
	
	public String queryXuqlyKd(){
		List<GonghmsMaoxq> gonghmsMaoxqs = gonghmsMaoxqService.queryGonghmsMaoxq(CommonFun.setUsercenter(getParams()));
		List<GonghmsMaoxq> kd = new ArrayList<GonghmsMaoxq>();
		for (GonghmsMaoxq gonghmsMaoxq : gonghmsMaoxqs) {
			String flag = gonghmsMaoxq.getXuqly();
			if(StringUtils.isNotBlank(flag) && flag.equalsIgnoreCase("DKS"))
			{
				kd.add(gonghmsMaoxq);
			}
		}
		setResult("result", kd);
		return AJAX;
	}
	/**
	 * 查询资源获取日期
	 * @return
	 */
	public String queryZiyhqrq(){
		setResult("result", gonghmsMaoxqService.queryGonghmsMaoxq(CommonFun.setUsercenter(getParams())));
		return AJAX;
	}
	
	/**
	 * 查询毛需求1
	 * @return
	 */
	public String queryMaoxqo(@Param Maoxq bean){
		setResult("result", maoxqService.queryMaoxqByXqlx(bean, getParams()));
		return AJAX;
	}
	
	
	/**
	 * 查询毛需求1
	 * @return
	 */
	public String queryChullx(@Param("dingdh") ArrayList<Dingd> dingds){
		setResult("result", dingdService.queryChullxByDingdh(dingds));
		return AJAX;
	}
}
