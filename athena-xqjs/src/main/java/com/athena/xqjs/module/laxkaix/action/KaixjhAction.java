/**
 * 
 */
package com.athena.xqjs.module.laxkaix.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.laxkaix.Kaixjh;
import com.athena.xqjs.entity.laxkaix.Kaixjhmx;
import com.athena.xqjs.entity.laxkaix.LinsXuq;
import com.athena.xqjs.entity.laxkaix.Weimzlj;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.ilorder.service.ZiykzbService;
import com.athena.xqjs.module.laxkaix.service.KaixjhService;
import com.athena.xqjs.module.laxkaix.service.LaxjhService;
import com.athena.xqjs.module.maoxq.service.MaoxqCompareService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;
import com.toft.utils.json.JSONException;

/**
 * @author dsimedd001
 *
 */
@Component
public class KaixjhAction extends ActionSupport  {
	@Inject
	private MaoxqCompareService maoxqCompareService;

	@Inject
	private KaixjhService kaixjhService;
	
	@Inject
	private LaxjhService laxjhService;
	
	@Inject
	private DownLoadServices  downloadServices;
	
	@Inject
	private ZiykzbService ziykzbService ;
	@Inject
	private JisclcsszService jiscclssz;
	/**
	 * 转向开箱计算设置页面
	 * @param bean
	 * @return
	 */
	public String execute(@Param Maoxq bean) {
		this.setRequestAttribute("ziyhqrq", this.ziykzbService.getFirstDay());
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("usercenter",loginUser.getUsercenter());
		return "select";
	}

	/**
	 * 查询毛需求 
	 * @param bean
	 * @return
	 */
	public String query(@Param Maoxq bean) {
		/**
		 * 查询毛需求
		 */
		Map map = maoxqCompareService.selectMxq(getParams(), bean);
		setResult("result", map);
		return AJAX;
	}

	/**
	 * 转向零件报警页面(点击下一步按钮)
	 * 
	 * @param xuqbc
	 * @param bean
	 * @return
	 */
	public String executeLingjbj(@Param("xuqbc") String xuqbc, @Param Kaixjh bean,@Param("wuldgk") String wuldgk) {
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		// 初始化毛需求明细类
		Maoxqmx xqmx = new Maoxqmx();
		// 设置毛需求版次
		xqmx.setXuqbc(xuqbc);
		// 设置用户中心
		xqmx.setUsercenter(bean.getUsercenter());
		// 计算用户中心缺短零件
		String msg ="";
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("jiscldm", Const.JISMK_LAX_CD);
		try{
			//判断处理状态,是否有计算进行中
			if(jiscclssz.checkState(map)){
				msg = "6";
			}else{
				//更新处理状态为20,计算中
				jiscclssz.updateState(map,Const.JSZT_EXECUTING);
				msg = kaixjhService.jsLingjDuanq(xqmx, bean,loginUser);
				//计算完成更新处理状态为90,计算完成
				jiscclssz.updateState(map,Const.JSZT_SURE);
			}
			
		}catch(ServiceException e){
			//异常时将处理状态更新为99
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
		}
		
		setResult("result", msg);
		return AJAX;
	}

	/**
	 * 转向零件报警页面
	 * 
	 * @return
	 */
	public String selectLingjbj(@Param("xuqbc") String xuqbc,
			@Param("kaixjx") String kaixjx, @Param("anqkc") String anqkc,
			@Param("usercenter") String usercenter,
			@Param("suanfcl") String suanfcl,@Param("wuldgk") String wuldgk) {
		this.setRequestAttribute("xuqbc", xuqbc);
		this.setRequestAttribute("kaixjx", kaixjx);
		this.setRequestAttribute("anqkc", anqkc);
		this.setRequestAttribute("usercenter", usercenter);
		this.setRequestAttribute("suanfcl", suanfcl);
		this.setRequestAttribute("wuldgk", wuldgk);
		return "select";
	}

	/**
	 * 查询短缺报警零件资源
	 * @param xuqbc
	 * @param bean
	 * @return
	 * @throws JSONException 
	 */
	public String queryLingjbj(@Param("xuqbc") String xuqbc,
			@Param("kaixjx") String kaixjx, @Param("anqkc") String anqkc,
			@Param("usercenter") String usercenter,
			@Param("suanfcl") String suanfcl,@Param("wuldgk") String wuldgk,@Param("lingjh") String lingjh,@Param("jihy") String jihy,@Param LinsXuq bean) throws JSONException {
		// 获取用户中心
		bean.setUsercenter(usercenter);
		bean.setLingjh(lingjh);
		bean.setJihydm(jihy);
		Map map = kaixjhService.queryLingjbj(bean, bean,anqkc,kaixjx);
		setResult("result", map);
		return AJAX;
	}

	/**
	 * 计算开箱计划
	 * @param xuqbc
	 * @param laxjx
	 * @param anqkc
	 * @param usercenter
	 * @return
	 */
	public String jsKaixjh(@Param("xuqbc") String xuqbc,
			@Param("kaixjx") String kaixjx, @Param("anqkc") String anqkc,
			@Param("usercenter") String usercenter,
			@Param("suanfcl") String suanfcl,@Param("wuldgk") String wuldgk) {
		// 初始化毛需求明细类
		LinsXuq linsXuq = new LinsXuq();
		// 设置毛需求版次
		linsXuq.setMaoxqbc(xuqbc);
		// 设置用户中心
		Kaixjh kaixjh = new Kaixjh();
		kaixjh.setUsercenter(usercenter);
		kaixjh.setAnqkc(new BigDecimal(CommonFun.checkStringValue(anqkc)));
		kaixjh.setKaixjx(new BigDecimal(CommonFun.checkStringValue(kaixjx)));
		kaixjh.setSuanfcl(suanfcl);
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		// 获取一个用户中心各使用零件的CMJ值
		String msg ="";
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);
		map.put("jiscldm", Const.JISMK_LAX_CD);
		try {
			//判断处理状态,是否有计算进行中
			if(jiscclssz.checkState(map)){
				msg = "6";
				setResult("result",msg);
			}else{
				//更新处理状态为20,计算中
				jiscclssz.updateState(map,Const.JSZT_EXECUTING);
				msg = kaixjhService.jsKaixjh(linsXuq, kaixjh,loginUser,wuldgk);
				//计算完成更新处理状态为90,计算完成
				jiscclssz.updateState(map,Const.JSZT_SURE);
				setResult("result", msg);
			}
		}catch (Exception e) {
			setResult("result","计算异常" + e.toString());
			//异常时将处理状态更新为99
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
		}
		return AJAX;
	}
	
	/**
	 * 查询开箱计划
	 * @param bean
	 * @return
	 */
	public String queryKaixjh(@Param Kaixjh kaixjh) {
		return "select";
	}

	/**
	 * 执行查询开箱计划
	 * @param bean
	 * @return
	 */
	public String executeKaixjh(@Param Kaixjh kaixjh) {
		/**
		 * 查询拉箱计划
		 */
		Map map = kaixjhService.queryKaixjh(kaixjh,kaixjh);
		setResult("result", map);
		return AJAX;
	}
	
	/**
	 * 修改需求数量(差异)
	 * @param linsXuqList
	 * @return
	 */
	public String editKaixLinsxuq(@Param("edit") ArrayList<LinsXuq> linsXuqList,@Param("delete") ArrayList<LinsXuq> deleteList){
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		for(LinsXuq linsXuq:linsXuqList){
			kaixjhService.editLinsxuq(linsXuq,loginUser);
		}
		if(deleteList.size()>0){
			for(LinsXuq linsXuq:deleteList){
				laxjhService.deleteLinsxuq(linsXuq);
			}
		}
		setResult("result", new Message("保存成功!"));
		return AJAX;
	}
	/**
	 * 保存开箱临时需求
	 * @param linsXuq
	 * @return
	 */
	public String saveKaixLinsXuq(@Param LinsXuq linsXuq){
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		kaixjhService.saveLinsxuq(linsXuq,loginUser);
		String msg = "1";
		setResult("result", msg);
		return AJAX;
	}
	/**
	 * 获取零件号
	 * @param usercenter
	 * @param lingjh
	 * @return
	 */
	public String getLingjh(@Param("usercenter") String usercenter,
			@Param("lingjh") String lingjh,@Param("lingjh") String ziyts) {
		Map map = laxjhService.getLingjh(usercenter, lingjh,ziyts,null,"2");
		setResult("result", map);
		return AJAX;
	}
	/**
	 * 查询开箱计划明细
	 * @param kaixjh
	 * @return
	 */
	public String selectKaixjhmx(@Param("usercenter") String usercenter,@Param("kaixjhNo") String kaixjhNo,@Param("shengxzt") String shengxzt) {
		this.setRequestAttribute("usercenter", usercenter);
		this.setRequestAttribute("jihNo", kaixjhNo);
		this.setRequestAttribute("shengxzt", shengxzt);
		return "select";
	}
	/**
	 * 执行查询开箱计划明细
	 * @param kaixjh
	 * @param tcNo
	 * @param dinghcj
	 * @param qiysj
	 * @param kaixzdsj
	 * @return
	 */
	public String queryKaixjhmx(@Param Kaixjh kaixjh,@Param Kaixjhmx mx,@Param("tcNo") String tcNo,@Param("dinghcj") String dinghcj
			,@Param("qiysj") String qiysj,@Param("kaixzdsj") String kaixzdsj) {
		Map<String,String> tjMap = new HashMap<String,String>();
		tjMap.put("kaixjhNo", kaixjh.getKaixjhh());
		tjMap.put("usercenter", kaixjh.getUsercenter());
		tjMap.put("tcNo", tcNo);
		tjMap.put("dinghcj", dinghcj);
		tjMap.put("qiysj", qiysj);
		tjMap.put("kaixzdsj", kaixzdsj);
		//mx.setPageSize(5);
		Map map = kaixjhService.queryKaixjhmx(mx,tjMap);
		setResult("result", map);
		return AJAX;
	}
	/**
	 * 查询开箱计划未满足零件
	 * @param weimzlj
	 * @return
	 */
	public String queryKaixjhWeimzlj(@Param Weimzlj weimzlj){
		//weimzlj.setPageSize(5);
		Map map = kaixjhService.queryKaixjhWeimzlj(weimzlj);
		setResult("result",map);
		return AJAX;
	}
	/**
	 * 编辑开箱计划明细
	 * @param deleteList
	 * @return
	 */
	public String editKaixjhmx(@Param Kaixjhmx kaixjhmx){
		kaixjhService.editKaixjhmx(kaixjhmx);
		setResult("result", new Message("保存成功!"));
		return AJAX;
	}
	/**
	 * 保存开箱计划明细
	 * @param kaixjhmx
	 * @return
	 */
	public String saveKaixjhmx(@Param Kaixjhmx kaixjhmx){
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		kaixjhService.saveKaixjhmx(kaixjhmx,loginUser);
		String msg = "1";
		setResult("result", msg);
		return AJAX;
	}
	/**
	 * 生效开箱计划
	 * @param kaixjh
	 * @return
	 */
	public String sxKaixjh(@Param Kaixjh kaixjh){
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		kaixjhService.sxKaixjh(kaixjh,loginUser);
		String msg = "开箱计划已生效!";
		setResult("result", msg);
		return AJAX;
	}
	/**
	 * 取消开箱计划
	 * @param kaixjh
	 * @return
	 */
	public String qxKaixjh(@Param Kaixjh kaixjh){
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		kaixjhService.qxKaixjh(kaixjh,loginUser);
		String msg = "开箱计划已取消!";
		setResult("result", msg);
		return AJAX;
	}
	/**
	 * 验证开箱计划明细TC号
	 * @param tcNo
	 * @param kaixjhh
	 * @param id
	 * @param usercenter
	 * @return
	 */
	public String validateKaixjhmxTcNo(@Param("tcNo") String tcNo,@Param("kaixjhh") String kaixjhh,@Param("id") String id,@Param("usercenter") String usercenter){
		Map<String,String> tjMap = new HashMap<String,String>();
		tjMap.put("tcNo", tcNo);
		tjMap.put("kaixjhh", kaixjhh);
		tjMap.put("usercenter", usercenter);
		tjMap.put("id", id);
		setResult("result", kaixjhService.validateKaixjhmxTcNo(tjMap));
		return AJAX;
	}
	public String kaixjhDownLoadFile(@Param("usercenter") String usercenter,@Param("kaixjhh") String kaixjhh,@Param("tcNo") String tcNo,@Param("dinghcj") String dinghcj
			,@Param("qiysj") String qiysj,@Param("kaixzdsj") String kaixzdsj) throws IOException, ParseException{
		Map<String,String> tjMap = new HashMap<String,String>();
		tjMap.put("kaixjhNo", kaixjhh);
		tjMap.put("usercenter", usercenter);
		tjMap.put("tcNo", tcNo);
		tjMap.put("dinghcj", dinghcj);
		tjMap.put("qiysj", qiysj);
		tjMap.put("kaixzdsj", kaixzdsj);
		Map<String, Object> dataSource = kaixjhService.kaixjhDownLoadFile(tjMap);
		//拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
		downloadServices.downloadFile("kaixjh.ftl", dataSource, response, "开箱计划", ExportConstants.FILE_XLS, false) ;
		return "download";
	}
}
