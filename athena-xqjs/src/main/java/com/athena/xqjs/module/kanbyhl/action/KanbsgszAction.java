package com.athena.xqjs.module.kanbyhl.action;

import java.util.ArrayList;
import java.util.List;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.MessageConst;
import com.athena.xqjs.module.kanbyhl.service.KanbjsService;
import com.athena.xqjs.module.kanbyhl.service.KanbsgszService;
import com.athena.xqjs.module.kanbyhl.service.KanbxhgmService;
import com.athena.xqjs.module.yaohl.service.YaohlService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 看板手工设置Action
 * 
 * @author Xiahui
 * @date 2012-1-31
 * 
 */
@Component(scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class KanbsgszAction extends ActionSupport {
	// 注入看板手工设置service
	@Inject
	private KanbsgszService kanbsgszService;
	// 注入要货令service
	@Inject
	private YaohlService yaohlService;
	@Inject
	// 注入看板循环规模service
	private KanbxhgmService kanbxhgmService;
	@Inject
	private KanbjsService kanbjsService;

	@Inject
	private YicbjService yicbjService;
	/**
	 * getUserInfo获取用户信息方法
	 * 
	 * @return object userInfo
	 */
	public LoginUser getUserInfo() {
		return (LoginUser) AuthorityUtils.getSecurityUser();
	}

	/**
	 * 看板手工设置（外部）
	 */
	public String kbszw() {
		setResult("usercenter", getUserInfo().getUsercenter());
		return "kanbszw";
	}

	/**
	 * 看板手工设置（内部）
	 */
	public String kbszn() {
		setResult("usercenter", getUserInfo().getUsercenter());
		return "kanbszn";
	}

	/**
	 * 查询物流路径及消耗点 零件仓库
	 * 
	 * @return 查询结果
	 */
	public String searchKbsz(@Param Kanbxhgm kbxh) {
		setResult("result", kanbsgszService.select(kbxh, getParams(), getUserInfo()));
		return AJAX;
	}

	/**
	 * 生效看板循环规模
	 */
	@SuppressWarnings("unchecked")
	public String chuangJKanbxhgm(@Param("kb") ArrayList<Kanbxhgm> ls) {
		// 修改操作
		String result = MessageConst.UPDATE_COUNT_0;
		String time = CommonFun.getJavaTime();
		String info;
		String gonghms = ls.get(0).getGonghms();
		Object inLs = null;
		if (gonghms.equals("RD")) {
			inLs = kanbsgszService.checkKbRdCreate(ls, time, getUserInfo());
		} else if (gonghms.equals("R1") || gonghms.equals("R2")) {
			inLs = kanbsgszService.checkKbWbCreate(ls, time, getUserInfo());
		} else {
			inLs = kanbsgszService.checkKbRmCreate(ls, time, getUserInfo());
		}
		if ("java.lang.String".equals(inLs.getClass().getName())) {
			setResult("result", inLs);
			return AJAX;
		} else {
			info = kanbsgszService.doInsert((List<Kanbxhgm>) inLs);
		}
		if (info.equals("false")) {
			setResult("flag", result);
			return AJAX;
		}
		result = "创建成功！";
		setResult("result", result);
		return AJAX;
	}

	/***
	 * 查询零件是否存在
	 */
	public String selectLingj(@Param Kanbxhgm kbxh) {
		setResult("result", yaohlService.selectLingj(getParams()));
		return AJAX;

	}

	/***
	 * 查询产线
	 */
	public String selectChanx(@Param Kanbxhgm kbxh) {
		setResult("result", kanbxhgmService.selectChanx(getParams()));
		return AJAX;
	}


	/***
	 * 查询产线
	 */
	public void insertYcbj(String usercenter, String lingjbh) {
		Yicbj yc = new Yicbj();
		yc.setJihydm(getUserInfo().getUsername());
		yc.setUsercenter(usercenter);
		yc.setLingjbh(lingjbh);
		Lingj lj = new Lingj();
		lj.setUsercenter(usercenter);
		lj.setLingjbh(lingjbh);
		yc.setJismk("42");
		yc.setCuowlx(Const.KANB_LINGJGONGYS_ERROR);
		yc.setCuowxxxx("用户中心" + usercenter + "零件号" + lingjbh + "零件供应商表各个供应商包装容量不一致");
		yc.setJihyz(kanbjsService.getJihyz(lj));
		yicbjService.insert(yc);
	}

}
