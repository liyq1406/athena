package com.athena.ckx.module.xuqjs.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Anjmlxhd;
import com.athena.ckx.module.xuqjs.service.AnjmlxhdService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.module.common.CommonFun;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 按件目录卸货点Action
 * @author WL
 *
 */
@Component
public class AnjmlxhdAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private AnjmlxhdService anjmlxhdService;
	
	/**
	 * 加载按件目录卸货点页面
	 * @return
	 */
	public String initAnjmlxhd(){
		setResult("usercenter",AuthorityUtils.getSecurityUser().getUsercenter());
		return "anjmlxhd";
	}
	
	/**
	 * 查询按件目录卸货点
	 * @param anjmlxhd 查询参数
	 * @return 查询结果
	 */
	public String queryAnjmlxhd(@Param Anjmlxhd anjmlxhd){
		setResult("result", anjmlxhdService.queryAnjmlxhd(anjmlxhd));
		return AJAX;
	}

	/**
	 * 查询按件目录卸货点
	 * @param anjmlxhd 查询条件 
	 * @return 查询结果
	 */
	public String selectAnjmlxhd(@Param Anjmlxhd anjmlxhd){
		Map<String,String> message = new HashMap<String,String>();
		try {
			setResult("result", anjmlxhdService.selectAnjmlxhd(anjmlxhd));
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "按件目录卸货点", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**
	 * 保存按件目录卸货点
	 * @param anjmlxhd
	 * @param operant
	 * @return
	 */
	public String saveAnjmlxhd(@Param Anjmlxhd anjmlxhd,@Param("operant") Integer operant){
		Map<String,String> message = new HashMap<String,String>();
		try {
			String username = AuthorityUtils.getSecurityUser().getUsername();//操作人
			String create_time = DateTimeUtil.getAllCurrTime();//操作时间
			int i = anjmlxhdService.checkCangkbh(anjmlxhd);
			if(i > 0){
				//操作符为1为插入操作
				if(1 == operant){
					anjmlxhd.setCreator(username);
					anjmlxhd.setCreate_time(create_time);
					anjmlxhdService.saveAnjmlxhd(anjmlxhd);
					message.put("message", "保存成功!");
				//更新操作
				}else{
					anjmlxhd.setEditor(username);
					anjmlxhd.setEdit_time(create_time);
					anjmlxhdService.updateAnjmlxhd(anjmlxhd);
					message.put("message", "更新成功!");
				}
			}else{
				setResult("success",false);
				message.put("message", "卸货站台不存在!");
			}
		} catch (Exception e) {
			setResult("success",false);
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "按件目录卸货点", "数据保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result",message);
		return AJAX;
	}
}
