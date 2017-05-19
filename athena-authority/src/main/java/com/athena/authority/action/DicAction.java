/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 穆伟
 * @version v1.0
 * @date 
 */
package com.athena.authority.action;

import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.DataTable;
import com.athena.authority.entity.Dic;
import com.athena.authority.entity.LoginUser;
import com.athena.authority.service.DicService;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.cache.CacheManager;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class DicAction extends ActionSupport {
	@Inject
	private DicService dicService;
	@Inject
	private UserOperLog userOperLog;

	/**
	 * 业务管理主页面
	 * 
	 * @param bean
	 * @return
	 */
	public String execute(@Param DataTable bean) {
		return "select";
	}

	/**
	 * 业务管理查询AJAX
	 * 
	 * @param bean
	 * @return
	 */
	public String query(@Param Dic bean) {
		/**
		 * 获取所有字典信息
		 */
		setResult("result", dicService.select(bean));
		return AJAX;
	}

	/**
	 * AJAX查询所有业务信息 调用ACTION：listDic.ajax
	 * 
	 * @param bean
	 * @return
	 */
	public String list(@Param Dic bean) {
		/**
		 * 获取所有字典信息
		 */
		setResult("result", dicService.list(bean));
		return AJAX;
	}

	/**
	 * 业务管理单记录查询
	 * 
	 * @param bean
	 */
	public String get(@Param Dic bean) {
		/**
		 * 获取单条业务数据表信息并设置值
		 */
		setResult("result", dicService.get(bean));
		return AJAX;
	}

	/**
	 * 业务管理保存
	 * 
	 * @param operant
	 *            1、新增；2、更新
	 * @param bean
	 * @return
	 */
	public String save(@Param("operant") String operant, @Param Dic bean) {
		/**
		 * 保存业务数据表信息
		 */
		LoginUser loginUser = AuthorityUtils.getSecurityUser();
		String dicCode = bean.getDicCode();
		String dicName = bean.getDicName();
		if (operant.equals("1")) {
			try {
				if (!dicCode.trim().equals("") && !dicName.trim().equals("")) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("dicCode", dicCode);
					String msg = dicService.validateOnlyDic(map);
					if (msg.equals("0")) {
						bean.setCreator(loginUser.getUsername());
						bean.setCreateTime(DateUtil.curDateTime());
						dicService.save(bean);
						setResult("result", new Message("保存成功!"));
						userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY,
								"业务管理", "新增数据" + bean.getDicCode() + "操作成功");
					} else {
						setErrorMessage("保存失败，该用户组标识已存在!");
					}
				} else {
					setErrorMessage("业务标识、业务名称不能为空格!");
				}
			} catch (Exception e) {
				userOperLog.addError(CommonUtil.MODULE_AUTHORITY, "业务管理",
						"新增数据" + bean.getDicCode() + "操作失败",
						CommonUtil.getClassMethod(),
						CommonUtil.replaceBlank(e.getMessage()));
			}

		} else if (operant.equals("2")) {
			if (!dicCode.trim().equals("") && !dicName.trim().equals("")) {
				try {
					bean.setMender(loginUser.getUsername());
					bean.setModifyTime(DateUtil.curDateTime());
					dicService.update(bean);
					setResult("result", new Message("保存成功，请在用户组管理中修改"+bean.getDicName()+"所对应的所属角色!"));
					userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY, "业务管理",
							"修改数据" + bean.getDicCode() + "操作成功");
				} catch (Exception e) {
					userOperLog.addError(CommonUtil.MODULE_AUTHORITY, "业务管理",
							"修改数据" + bean.getDicCode() + "操作失败",
							CommonUtil.getClassMethod(),
							CommonUtil.replaceBlank(e.getMessage()));
				}
			} else {
				setErrorMessage("业务标识、业务名称不能为空格!");
			}

		}
		CacheManager cm = CacheManager.getInstance();
		Object object = dicService.refreshAllData();
		cm.refreshCache("queryDicCode", object);
		
		return AJAX;
	}

	/**
	 * 业务管理删除
	 * 
	 * @return
	 */
	public String remove(@Param Dic bean) {
		/**
		 * 删除业务数据表信息
		 */
		try {
			setResult("result", dicService.delete(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_AUTHORITY, "业务管理", "删除数据"
					+ bean.getDicCode() + "操作成功");
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_AUTHORITY, "业务管理", "删除数据"
					+ bean.getDicCode() + "操作失败", CommonUtil.getClassMethod(),
					CommonUtil.replaceBlank(e.getMessage()));
		}

		// 刷新缓存
		CacheManager cm = CacheManager.getInstance();
		Object object = dicService.refreshAllData();
		cm.refreshCache("queryDicCode", object);
		return AJAX;
	}

	/**
	 * 验证字典标识的唯一性 被validateOnlyDic.ajax调用
	 * 
	 * @param dicCode
	 * @return
	 */
	public String validateOnlyDic(@Param("dicCode") String dicCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dicCode", dicCode);
		setResult("result", dicService.validateOnlyDic(map));
		return AJAX;
	}

	/**
	 * 根据dicCode查询业务信息 被getDicByDicCode.ajax调用
	 * 
	 * @param dicCode
	 * @return
	 */

	public String getDicByDicCode(@Param("dicCode") String dicCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dicCode", dicCode);
		setResult("result", dicService.getDicByDicCode(map));
		return AJAX;
	}
}
