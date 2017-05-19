package com.athena.xqjs.module.anxorder.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.anxorder.Chushzyb;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.module.anxorder.service.ChushzybService;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.common.service.GongyxhdService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.diaobl.action.DiaobsqAction;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ChushzylrAction
 * <p>
 * 类描述： 初始化资源录入
 * </p>
 * 创建人：李智
 * <p>
 * 创建时间：2012-3-22
 * </p>
 * 
 * @version 1.0
 * 
 */

@Component
public class ChushzylrAction extends ActionSupport {
	@Inject
	//零件消耗点service
	private LingjxhdService lingjxhdService;
	@Inject
	//工艺消耗点service
	private GongyxhdService gongyxhdService;
	@Inject
	//初始化资源service
	private ChushzybService chushzybService;
	//log4j日志初始化
	private final Log log = LogFactory.getLog(DiaobsqAction.class); 
	@Inject
	private UserOperLog userOperLog;
	//log4j 日志定义 hzg 2015.7.15
	protected static Logger logger = Logger.getLogger(ChushzylrAction.class);	
	//获取用户中心
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}
	
	/**
	 * 页面初始化，执行跳转
	 */
	public String execute() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "进入页面") ;
		setResult("usercenter", getUserInfo().getUsercenter());
		return "success";
	}
	
	/**
	 * 根据用户中心和零件编号查询生产线编号
	 * @：李智
	 * @：2012-3-22
	 */
	public String queryShengcxByParam() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "查询生产线编号开始") ;
		Map<String, String> params = getParams();
		//返回生产线
		setResult("result", chushzybService.queryShengcxByParam(params));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "查询生产线编号结束") ;
		return AJAX;
	}
	
	/**
	 * 根据用户中心、零件编号和产线查询消耗点编号
	 * @：李智
	 * @：2012-3-22
	 */
	public String queryXiaohdByParam() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "查询消耗点编号开始") ;
		Map<String, String> params = getParams();
		//返回消耗点
		setResult("result", this.lingjxhdService.queryXiaohdByParam(params));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "查询消耗点编号结束") ;
		return AJAX;
	}
	
	/**
	 * 根据用户中心、零件编号、产线和消耗点查询继承的未交付量（已发要货令总量-交付总量-终止总量）
	 * @：李智
	 * @：2012-3-22
	 */
	public String queryWeijfzlByParam() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "查询继承的未交付量开始") ;
		Map<String, String> params = getParams();
		Lingjxhd lingjxhd = this.lingjxhdService.queryWeijfzlByParam(params);
		//返回继承的未交付量
		setResult("result",lingjxhd.getJicdwjfl());
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "查询继承的未交付量结束") ;
		return AJAX;
	}
	
	/**
	 * 根据消耗点查询流水号
	 * @：李智
	 * @：2012-3-23
	 */
	public String queryLiushByXhd() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "查询流水号开始") ;
		Map<String, String> params = getParams();
		String liush = this.gongyxhdService.queryLiushByXhd(params);
		//返回流水号
		setResult("result",liush);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "查询流水号结束") ;
		return AJAX;
	}
	/**
	 * 插入初始化资源
	 * @：李智
	 * @：2012-3-23
	 * 
	 * 更新按需初始化资源，单行更新 mantis:0011510
	 * @Author hzg
	 * @Date 2015.7.15
	 */
	public String addChushzyb(@Param Chushzyb bean,@Param("operant") Integer operant) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "插入初始化资源开始") ;
		//新增
		if(operant==1){
			// 执行插入操作
			int i = this.chushzybService.doInsert(getParams(),bean,getUserInfo());
			if(i == 0){
				setErrorMessage("插入初始化资源失败");
			}else if(i == 2){
				setErrorMessage("初始化资源已经存在");
			}else if(i == 3){
				setErrorMessage(bean.getLiush()+"流水号不存在,请重新输入!");
			}
			//更新操作
			/************************** mantis:0011510  modify by hzg 2015.7.15 start ***********************************/
		}else if(operant==2){
			String liushStr = "";
			//更新操作
			try {
				liushStr = chushzybService.doEdit(bean,getUserInfo());
			} catch (Exception e) {
				this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "更新初始化资源失败") ;
				setErrorMessage(e.getMessage());
				setResult("result", e.getMessage());
				return AJAX;
			}
			//并发 更新失败
			if(!"".equals(liushStr)){
				this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "更新初始化资源失败") ;
				setErrorMessage(liushStr+"流水号不存在,请重新输入！");
				setResult("result", liushStr+"流水号不存在,请重新输入！");
				return AJAX;
			}
			setResult("result", "更新初始化资源成功");
		}
		/************************** mantis:0011510  modify by hzg 2015.7.15 end ***********************************/
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "插入初始化资源结束") ;
		return AJAX;
	}
	
	
	public Map<String,BigDecimal> getYicxhOfanxcsh(Chushzyb bean){
		//创建webservice调用ZXC
		
		return null;
	}
	
	/**
	 * @：查询初始化资源
	 * @：李智
	 * @：2012-3-23
	 */
	public String queryChushzybByParam(@Param Chushzyb bean) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "查询初始化资源开始") ;
 		Map<String,String> map = getParams();
		//获取初始化资源列表
		setResult("result", this.chushzybService.queryChushzybByParam(bean, map));
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "查询初始化资源结束") ;
		return AJAX;
	}
	/**
	 * @：删除初始化资源
	 * @：李智
	 * @：2012-3-26
	 */
	public String deleteChushzyb(@Param("edit") ArrayList<Chushzyb> chushzybs) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "删除初始化资源开始") ;
		//删除操作
		String result = MessageConst.DELETE_COUNT_0;
		boolean info = true;
		//删除操作
		try {
			info = chushzybService.doRemove(chushzybs);
		} catch (Exception e) {
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "删除初始化资源失败") ;
			setResult("error", result);
			return AJAX;
		}
		//并发 删除失败
		if(!info){
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "并发删除初始化资源失败") ;
			setResult("error", result);
		   	return AJAX;
		}
		result = "删除成功！";
		setResult("result", result);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "删除初始化资源结束") ;
		return AJAX;
	}
	
	/**
	 * @：保存初始化资源
	 * @：李智
	 * @：2012-3-26
	 */
	public String saveChushzyb(@Param("edit") ArrayList<Chushzyb> chushzybs) {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "更新初始化资源开始") ;
		//更新操作
		String result = MessageConst.UPDATE_COUNT_0;
		StringBuilder info = new StringBuilder("");
		//更新操作
		try {
			info = chushzybService.doUpdate(chushzybs,getUserInfo());
		} catch (Exception e) {
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "更新初始化资源失败") ;
			setResult("error", e.getMessage());
			return AJAX;
		}
		//并发 更新失败
		if(info.length() != 0){
			info.append("流水号不存在,请重新输入！");
			this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "更新初始化资源失败") ;
			setResult("error", info.toString());
		   	return AJAX;
		}
		result = "更新成功！";
		setResult("result", result);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "更新初始化资源结束") ;
		return AJAX;
	}
	
	/**
	 * @：打印初始化资源
	 * @：李智
	 * @：2012-3-26
	 */
	public String printChushzyb() {
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "初始化资源打印开始") ;
		// 获取申请明细
		List<Chushzyb> ls = chushzybService.queryChushzybByParam(getParams());
		// json数组
		JSONArray ar = new JSONArray();
		// json对象
		JSONObject jsonObject = null;
		int rowCount = ls.size();
		// 每页显示数
		int pageSize = 12;
		// 总页数
		int totalPage = (rowCount - 1) / pageSize + 1;
		for (int i = 1; i <= totalPage; i++) {
			// 开始行
			int pageEndRow = i * pageSize;
			// 结束行
			int pageStartRow = pageSize * (i - 1);
			jsonObject = new JSONObject();
			for (int j = pageStartRow; j < pageEndRow; j++) {
				// 每页序号从1到8开始
				int temp = j % pageSize + 1;
				if (j >= ls.size()) {
					jsonObject.put("XUH" + temp, "");
					jsonObject.put("LINGJBH" + temp, "");
					jsonObject.put("USERCENTER" + temp, "");
					jsonObject.put("SHENGCXBH" + temp, "");
					jsonObject.put("XIAOHDBH" + temp, "");
					jsonObject.put("XIANBKC" + temp, "");
					jsonObject.put("LIUSH" + temp, "");
					jsonObject.put("BEIZ" + temp, "");
					jsonObject.put("EDITOR" + temp, "");
					jsonObject.put("EDIT_TIME" + temp, "");
				} else {
					Chushzyb chushzyb = ls.get(j);
					jsonObject.put("XUH" + temp, j + 1);
					jsonObject.put("LINGJBH" + temp, chushzyb.getLingjbh());
					jsonObject.put("USERCENTER" + temp, chushzyb.getUsercenter());
					jsonObject.put("SHENGCXBH" + temp, chushzyb.getShengcxbh());
					jsonObject.put("XIAOHDBH" + temp, chushzyb.getXiaohdbh());
					jsonObject.put("XIANBKC" + temp, chushzyb.getXianbkc());
					jsonObject.put("LIUSH" + temp, chushzyb.getLiush());
					jsonObject.put("BEIZ" + temp, chushzyb.getBeiz());
					jsonObject.put("EDITOR" + temp, chushzyb.getEditor());
					jsonObject.put("EDIT_TIME" + temp, chushzyb.getEdit_time());
				}

			}
			// 将jons对象放到数组里
			ar.add(jsonObject);
		}
		// 转换数据格式
		String json = ar.toString();
		log.info(json);
		setResult("json", json);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "按需初始化资源录入", "初始化资源打印结束") ;
		return AJAX;
	}
}
