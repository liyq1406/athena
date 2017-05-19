/**
 * 
 */
package com.athena.xqjs.module.laxkaix.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.laxkaix.Laxjh;
import com.athena.xqjs.entity.laxkaix.Laxjhmx;
import com.athena.xqjs.entity.laxkaix.LinsXuq;
import com.athena.xqjs.entity.laxkaix.Weimzlj;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.athena.xqjs.module.ilorder.service.ZiykzbService;
import com.athena.xqjs.module.laxkaix.service.LaxjhService;
import com.athena.xqjs.module.maoxq.service.MaoxqCompareService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * @author dsimedd001
 * 
 */
@Component
public class LaxjhAction  extends BaseWtcAction {
	@Inject
	private MaoxqCompareService maoxqCompareService;

	@Inject
	private LaxjhService laxjhService;
	
	@Inject
	private UserOperLog userOperLog;

	@Inject
	private DownLoadServices  downloadServices;
	
	@Inject
	private ZiykzbService ziykzbService ;
	@Inject
	private UserOperLog log;
	/**
	 * 计算处理参数设置service
	 */
	@Inject
	private JisclcsszService jiscclssz;
	/**
	 *拉箱计算设置
	 * 
	 * @param bean
	 * @return
	 */
	public String execute(@Param Maoxq bean) {
		//获取资源快照日期
		this.setRequestAttribute("ziyhqrq", this.ziykzbService.getFirstDay());
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		setResult("usercenter",loginUser.getUsercenter());
		return "select";
	}

	/**
	 * 查询毛需求
	 * @param bean
	 * @return 返回毛需求
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
	public String executeLingjbj(@Param("xuqbc") String xuqbc, @Param Laxjh bean,@Param("wuldgk") String wuldgk) throws ServiceException{
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		// 初始化毛需求明细类
		Maoxqmx xqmx = new Maoxqmx();
		// 设置毛需求版次
		xqmx.setXuqbc(xuqbc);
		// 设置用户中心
		xqmx.setUsercenter(bean.getUsercenter());
		// 计算用户中心缺短零件
		String msg = "";
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
				msg = laxjhService.jsLingjDuanq(xqmx, bean, loginUser);
				//计算完成更新处理状态为90,计算完成
				jiscclssz.updateState(map,Const.JSZT_SURE);
			}
			
			userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "计算短缺零件", "短缺零件计算成功");
		}catch(ServiceException e){
			//异常时将处理状态更新为99
			jiscclssz.updateState(map,Const.JSZT_EXECPTION);
			userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "计算短缺零件", "短缺零件计算失败");
		}
		setResult("result", msg);
		return AJAX;
	}

	/**
	 * 查询零件报警页面
	 * 
	 * @return
	 */
	public String selectLingjbj(@Param("xuqbc") String xuqbc,
			@Param("laxjx") String laxjx, @Param("anqkc") String anqkc,
			@Param("usercenter") String usercenter,
			@Param("suanfcl") String suanfcl,@Param("wuldgk") String wuldgk,@Param("laxbs") String laxbs,@Param("ziyts") String ziyts,@Param("kyljdm") String kyljdm) {
		this.setRequestAttribute("xuqbc", xuqbc);
		this.setRequestAttribute("laxjx", laxjx);
		this.setRequestAttribute("anqkc", anqkc);
		this.setRequestAttribute("usercenter", usercenter);
		this.setRequestAttribute("suanfcl", suanfcl);
		this.setRequestAttribute("wuldgk", wuldgk);
		this.setRequestAttribute("laxbs", laxbs);
		this.setRequestAttribute("ziyts", ziyts);
		this.setRequestAttribute("kyljdm", kyljdm);
		return "select";
	}

	/**
	 * 执行查询报警零件资源
	 * 
	 * @param xuqbc
	 * @param bean
	 * @return
	 * @throws Exception 
	 */
	public String queryLingjbj(@Param("xuqbc") String xuqbc,
			@Param("laxjx") String laxjx, @Param("anqkc") String anqkc,
			@Param("suanfcl") String suanfcl,@Param("wuldgk") String wuldgk,@Param LinsXuq bean,@Param("laxbs") String laxbs,@Param("ziyts") String ziyts,@Param("kyljdm") String kyljdm) {
		Map map = laxjhService.queryLingjbj(bean, bean,anqkc,laxjx,laxbs,ziyts,kyljdm);
		setResult("result", map);
		return AJAX;
	}

	/**
	 * 计算拉箱计划
	 * 
	 * @param xuqbc
	 * @param laxjx
	 * @param anqkc
	 * @param usercenter
	 * @return
	 */
	public String jsLaxjh(@Param("xuqbc") String xuqbc,
			@Param("laxjx") String laxjx, @Param("anqkc") String anqkc,
			@Param("usercenter") String usercenter,
			@Param("suanfcl") String suanfcl,@Param("wuldgk") String wuldgk,@Param("laxbs") String laxbs,@Param("ziyts") String ziyts,@Param("kyljdm") String kyljdm) {
		// 初始化毛需求明细类
		LinsXuq linsXuq = new LinsXuq();
		// 设置毛需求版次
		linsXuq.setMaoxqbc(xuqbc);
		// 设置用户中心
		Laxjh laxjh = new Laxjh();
		laxjh.setUsercenter(usercenter);
		laxjh.setAnqkc(new BigDecimal(CommonFun.checkStringValue(anqkc)));
		laxjh.setLaxjx(new BigDecimal(CommonFun.checkStringValue(laxjx)));
		laxjh.setSuanfcl(suanfcl);
		laxjh.setLaxbs(laxbs);
		laxjh.setZiyts(new BigDecimal(CommonFun.checkStringValue(ziyts)));
		laxjh.setKyljdm(kyljdm);
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		// 获取一个用户中心各使用零件的CMJ值
		String msg = "";
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
				msg = laxjhService.jsLaxjh(linsXuq, laxjh, loginUser,wuldgk);
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
	 * 转向拉箱计划查询及生效
	 * @param bean
	 * @return
	 */
	public String queryLaxjh(@Param Laxjh laxjh) {
		return "select";
	}

	/**
	 * 执行查询拉箱计划
	 * 
	 * @param bean
	 * @return
	 */
	public String executeLaxjh(@Param Laxjh laxjh) {
		/**
		 * 查询拉箱计划
		 */
		Map map = laxjhService.queryLaxjh(laxjh, laxjh);
		setResult("result", map);
		return AJAX;
	}

	/**
	 * 修改需求数量(差异)
	 * 
	 * @param linsXuqList
	 * @return
	 */
	public String editLinsxuq(@Param("edit") ArrayList<LinsXuq> linsXuqList,
			@Param("delete") ArrayList<LinsXuq> deleteList) {
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		for (LinsXuq linsXuq : linsXuqList) {
			laxjhService.editLinsxuq(linsXuq, loginUser);
		}
		if (deleteList.size() > 0) {
			for (LinsXuq linsXuq : deleteList) {
				laxjhService.deleteLinsxuq(linsXuq);
			}
		}
		setResult("result", new Message("保存成功"));
		return AJAX;
	}
	/**
	 * 删除零件报警数据
	 * @param usercenter
	 * @param lingjh
	 * @param jihydm
	 * @return
	 */
	public String delLingjbj(@Param("usercenter") String usercenter,@Param("lingjh") String lingjh,@Param("jihydm") String jihydm){
		LinsXuq linsXuq = new LinsXuq();
		linsXuq.setUsercenter(usercenter);
		linsXuq.setLingjh(lingjh);
		linsXuq.setJihydm(jihydm);
		laxjhService.deleteLinsxuq(linsXuq);
		return AJAX;
	}
	/**
	 * 保存临时需求
	 * @param linsXuq
	 * @return
	 */
	public String saveLinsXuq(@Param LinsXuq linsXuq) {
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		laxjhService.saveLinsXuq(linsXuq, loginUser);
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
			@Param("lingjh") String lingjh,@Param("ziyts") String ziyts,@Param("laxbs") String laxbs) {
		Map map = laxjhService.getLingjh(usercenter, lingjh, ziyts,laxbs,"1");
		setResult("result", map);
		return AJAX;
	}
	/**
	 * 查询拉箱明细明细
	 * @param laxjh
	 * @return
	 */
	public String selectLaxjhmx(@Param("usercenter") String usercenter,@Param("laxjhNo") String laxjhNo,@Param("shengxzt") String shengxzt) {
		this.setRequestAttribute("usercenter", usercenter);
		this.setRequestAttribute("jihNo", laxjhNo);
		this.setRequestAttribute("shengxzt", shengxzt);
		return "select";
	}
	/**
	 * 执行拉箱计划明细页面
	 * @param laxjh
	 * @param tcNo
	 * @param dinghcj
	 * @param qiysj
	 * @param wuld
	 * @return
	 */
	public String queryLaxjhmx(@Param Laxjh laxjh,@Param("tcNo") String tcNo,@Param("dinghcj") String dinghcj
			,@Param("qiysj") String qiysj,@Param("wuld") String wuld,@Param Laxjhmx mx) {
		Map<String,String> tjMap = new HashMap<String,String>();
		tjMap.put("laxjhNo", laxjh.getLaxjhh());
		tjMap.put("usercenter", laxjh.getUsercenter());
		tjMap.put("tcNo", tcNo);
		tjMap.put("dinghcj", dinghcj);
		tjMap.put("qiysj", qiysj);
		tjMap.put("wuld", wuld);
		//mx.setPageSize(5);
		Map map = laxjhService.queryLaxjhmx(mx,tjMap);
		if((Integer)map.get("total")==0){
			setResult("result", new Message("无箱可拉，请查看未满足零件列表!"));
		}else{
			setResult("result", map);
		}
		return AJAX;
	}
	/**
	 * 查询未满足零件信息
	 * @param weimzlj
	 * @return
	 */
	public String queryLaxjhWeimzlj(@Param Weimzlj weimzlj){
		//weimzlj.setPageSize(5);
		Map map = laxjhService.queryLaxjhWeimzlj(weimzlj,getParams());
		setResult("result",map);
		return AJAX;
	}
	/**
	 * 编辑拉箱计划明细
	 * @param deleteList
	 * @return
	 */
	public String editLaxjhmx(@Param Laxjhmx laxjhmx){
		laxjhService.editLaxjhmx(laxjhmx);
		setResult("result", new Message("保存成功!"));
		return AJAX;
	}
	/**
	 * 保存拉箱计划明细
	 * @param laxjhmx
	 * @return
	 */
	public String saveLaxjhmx(@Param Laxjhmx laxjhmx){
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		laxjhService.saveLaxjhmx(laxjhmx,loginUser);
		String msg = "1";
		setResult("result", msg);
		return AJAX;
	}
	/**
	 * 生效拉箱计划 
	 * @param laxjh
	 * @return
	 */
	public String sxLaxjh(@Param Laxjh laxjh){
		// 获取当前登录用户信息
		String loginfo = "";
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String usercenter = loginUser.getUsercenter();
		String response = "";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Laxjhmx mx = new Laxjhmx();
		String laxjhNo = laxjh.getLaxjhh();
		String _usercenter = laxjh.getUsercenter();
		mx.setLaxjhNo(laxjhNo);
		mx.setUsercenter(_usercenter);
		list = laxjhService.getLaxjhmx(mx);
		List wtcList = new ArrayList();
		for (Map<String, Object> map : list) {
			String tcNo = (String) map.get("TCNO");
			String laxzdsj = (String) map.get("LAXZDSJ");
			String kyljdm = "";
			if(map.get("KYLJDM")!=null&&!"".equals(map.get("KYLJDM"))){
				kyljdm = (String) map.get("KYLJDM");
			}
			String kdysSheetId ="";
			if(map.get("KDYS_SHEET_ID")!=null&&!"".equals(map.get("KDYS_SHEET_ID"))){
				kdysSheetId	= (String)map.get("KDYS_SHEET_ID");
			}
			Map<String,String> wtcMap = new HashMap<String,String>();
			wtcMap.put("tch", tcNo);
			wtcMap.put("fasbh", kdysSheetId);
			wtcMap.put("celbh", kyljdm);
			wtcMap.put("daoxsj", laxzdsj);
			wtcList.add(wtcMap);
		}
		String msg = "";
		Map param = new HashMap();
		param.put("list", wtcList);
		try {
				//调用WTC查询拒收跟踪单
				WtcResponse wtcResponse = callWtc(usercenter,"Q0401", param);
				//获取WTC
				response = CommonFun.strNull(wtcResponse.get("response"));
				if(response.equals(Const.WTC_SUSSCESS)){
					laxjhService.sxLaxjh(laxjh,loginUser);
					msg = "拉箱计划已生效";
					setResult("result", msg);
				}else{
					msg = "调用WTC异常,无法发送拉箱指定到达时间执行层中,请检查,拉箱计划未生效";
					setResult("result", msg);
				}
		} catch (Exception e) {
			loginfo = "调用WTC发送拉箱指定到达时间信息异常"+CommonUtil.replaceBlank(e.toString())+response;
			CommonFun.logger.error(loginfo);
			log.addError(CommonUtil.MODULE_XQJS, "调用WTC发送拉箱指定到达时间信息异常", "调用WTC发送拉箱指定到达时间信息异常", CommonUtil.getClassMethod(), loginfo);
		}
		
		return AJAX;
	}
	/**
	 * 取消拉箱计划
	 * @param laxjh
	 * @return
	 */
	public String qxLaxjh(@Param Laxjh laxjh){
		// 获取当前登录用户信息
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		laxjhService.qxLaxjh(laxjh,loginUser);
		String msg = "拉箱计划已删除!";
		setResult("result", msg);
		return AJAX;
	}
	/**
	 * 获取TC的ID号
	 * @param tcNo
	 * @return
	 */
	public String getTcId(@Param("tcNo") String tcNo){
		setResult("result", laxjhService.getTcId(tcNo));
		return AJAX;
	}
	/**
	 * 验证零件号的维一性
	 * @param lingjh
	 * @return
	 */
	public String validateLingjh(@Param("lingjh") String lingjh){
		setResult("result", laxjhService.validateLingjh(lingjh));
		return AJAX;
	}
	/**
	 * 验证临时需求中的零件号信息
	 * @param lingjh
	 * @param usercenter
	 * @param jihydm
	 * @return
	 */
	public String validateLinsxuqLingjh(@Param("lingjh") String lingjh,@Param("usercenter") String usercenter,@Param("jihydm") String jihydm){
		Map<String,String> tjMap = new HashMap<String,String>();
		tjMap.put("lingjh", lingjh);
		tjMap.put("usercenter", usercenter);
		tjMap.put("jihydm", jihydm);
		setResult("result", laxjhService.validateLinsxuqLingjh(tjMap));
		return AJAX;
	}
	/**
	 * 验证拉箱计划明细中的TC号的唯一性
	 * @param tcNo
	 * @param laxjhh
	 * @param id
	 * @param usercenter
	 * @return
	 */
	public String validateLaxjhmxTcNo(@Param("tcNo") String tcNo,@Param("laxjhh") String laxjhh,@Param("id") String id,@Param("usercenter") String usercenter){
		Map<String,String> tjMap = new HashMap<String,String>();
		tjMap.put("tcNo", tcNo);
		tjMap.put("laxjhh", laxjhh);
		tjMap.put("usercenter", usercenter);
		tjMap.put("id", id);
		setResult("result", laxjhService.validateLaxjhmxTcNo(tjMap));
		return AJAX;
	}
	
	public String laxjhDownLoadFile(@Param("usercenter") String usercenter,@Param("laxjhh") String laxjhh,@Param("tcNo") String tcNo,@Param("dinghcj") String dinghcj
			,@Param("qiysj") String qiysj,@Param("wuld") String wuld) throws IOException, ParseException{
		Map<String,String> tjMap = new HashMap<String,String>();
		tjMap.put("laxjhNo", laxjhh);
		tjMap.put("usercenter", usercenter);
		tjMap.put("tcNo", tcNo);
		tjMap.put("dinghcj", dinghcj);
		tjMap.put("qiysj", qiysj);
		tjMap.put("wuld", wuld);
		Map<String, Object> dataSource = laxjhService.laxjhDownLoadFile(tjMap);
		//拿到response对象
		HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
		downloadServices.downloadFile("laxjh.ftl", dataSource, response, "拉箱计划", ExportConstants.FILE_XLS, false) ;
		return "download";
	}

	public String listKayunLujdm(){
		setResult("result", laxjhService.listKayunLujdm());
		return AJAX;
	}
}
