package com.athena.xqjs.module.yaohl.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.yaohl.Plcjlsk;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.XqjsAuthority;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

//要货令管理service
@Component
public class YaohlService extends BaseService {

	// 获取用户信息
	public LoginUser getUserInfo() {
		return com.athena.authority.util.AuthorityUtils.getSecurityUser();
	}

	@Inject
	private XqjsAuthority xAuthority;

	/**
	 * 查询外部要货令
	 * 
	 * @author Xiahui
	 * @date 2012-1-17
	 * @param page
	 * @param params
	 * @return
	 */
	public Map<String, Object> select(Pageable page, Map<String, String> params) {
		// String sj = params.get("sj"); // 获取时间
		// 获取查询状态
		String zt = params.get("zt");
		String yaohlzt = "";
		if (!StringUtils.isEmpty(zt)) {
			String[] str = zt.split(",");
			// 拼接查询条件-到达状态
			yaohlzt += " and (";
			for (int i = 0; i < str.length; i++) {
				yaohlzt += "yaohlzt=" + "'" + str[i] + "'";
				if (str.length != 1 && i != str.length - 1) {
					yaohlzt += " or ";
				}
			}
			yaohlzt += " )";
			params.put("yaohlzt", yaohlzt);
		}
		

		if (Const.STRING_ZERO.equals(params.get("jiaofzt"))) {
			params.put("jiaofzt", " and A.yaohlzt<>'05' and nvl(A.xiughyjjfsj,A.zuiwsj)<to_date(to_char(nvl(A.jiaofj,sysdate), 'yyyy-mm-dd HH24:mi'), 'yyyy-mm-dd HH24:mi') ");
		} else if (Const.STRING_TWO.equals(params.get("jiaofzt"))) {
			params.put("jiaofzt", " and A.yaohlzt<>'05'  and nvl(A.xiughyjjfsj,A.zuiwsj)=to_date(to_char(nvl(A.jiaofj,sysdate), 'yyyy-mm-dd HH24:mi'), 'yyyy-mm-dd HH24:mi') ");
		} else if (Const.STRING_ONE.equals(params.get("jiaofzt"))) {
			params.put("jiaofzt", " and A.yaohlzt<>'05' and A.yaohlzt = '00'  and nvl(A.xiughyjjfsj,A.zuiwsj)>to_date(to_char(nvl(A.jiaofj,sysdate), 'yyyy-mm-dd HH24:mi'), 'yyyy-mm-dd HH24:mi') ");
		}
		
		if (Const.STRING_ZERO.equals(params.get("shiftt"))) {
			params.put("shiftt", "  and A.yuanzuizsj is null ");
		} else if (Const.STRING_ONE.equals(params.get("shiftt"))) {
			params.put("shiftt", " and A.yuanzuizsj is not null ");
		}
		
		// 分页查询
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("yhl.queryYaohl", params, page);
	}

	public List<Yaohl> yaoHlDownLoadFile(Pageable page, Map<String, String> params) {
		if (page != null) {
			return (List<Yaohl>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("yhl.queryExpYaohl", params, page).get("rows");
		} else {
			return (List<Yaohl>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("yhl.queryExpYaohl", params);
		}
	}

	public BigDecimal countExpYaohl(Map<String, String> params) {
		// String sj = params.get("sj"); // 获取时间
		// 获取查询状态
		String zt = params.get("zt");
		String yaohlzt = "";
		if ((zt != null) && (!"".equals(zt))) {
			String[] str = zt.split(",");
			// 拼接查询条件-到达状态
			yaohlzt += " and (";
			for (int i = 0; i < str.length; i++) {
				yaohlzt += "yaohlzt=" + "'" + str[i] + "'";
				if (str.length != 1 && i != str.length - 1) {
					yaohlzt += " or ";
				}
			}
			yaohlzt += " )";
		}
		params.put("yaohlzt", yaohlzt);

		/*
		 * String js = null; // 起始时间与开始时间 String conectSql = "to_date('" +
		 * params.get("qssj") + "','YYYY-MM-DD')" + "and " + "to_date('" +
		 * params.get("jssj") + "','YYYY-MM-DD')"; if (Const.SJ_1.equals(sj)) {
		 * // 发运时间 js = " and  faysj between " + conectSql; } else if
		 * (Const.SJ_2.equals(sj)) { // 实际发运时间 js = " and  shijfysj between " +
		 * conectSql; } else if (Const.SJ_3.equals(sj)) { // 最晚时间 js =
		 * " and  zuiwsj between " + conectSql;
		 * 
		 * } else if (Const.SJ_4.equals(sj)) { // 交付时间 js =
		 * " and  jiaofj between " + conectSql; } else { // 否则时间为空 js = null; }
		 */
		// 动态传参拼接SQL
		if (Const.STRING_ZERO.equals(params.get("jiaofzt"))) {
			// params.put("jfzt", Const.STRING_ZERO);
			params.put("jiaofzt", "and (jiaofzt ='" + Const.STRING_ZERO + "' OR jiaofzt is null)");
			params.put("currtime", CommonFun.getJavaTime("yyyy-MM-dd HH:mm"));
		} else if (!StringUtils.isEmpty(params.get("jiaofzt"))) {
			params.put("jiaofzt", "and jiaofzt='" + params.get("jiaofzt") + "'");
		}

		return (BigDecimal) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("yhl.countExpYaohl", params);
	}

	/**
	 * 查询内部要货令
	 * 
	 * @author Xiahui
	 * @date 2012-1-17
	 * @param page
	 * @param params
	 * @return
	 */
	public Map<String, Object> selectN(Pageable page, Map<String, String> params) {
		// String sj = params.get("sj"); // 获取时间
		// 获取查询状态
		String zt = params.get("zt");
		String yaohlzt = "";
		if (zt != null) {
			String[] str = zt.split(",");
			// 拼接查询条件-到达状态
			yaohlzt += " and (";
			for (int i = 0; i < str.length; i++) {
				yaohlzt += "yaohlzt=" + "'" + str[i] + "'";
				if (str.length != 1 && i != str.length - 1) {
					yaohlzt += " or ";
				}
			}
			yaohlzt += " )";
		}
		params.put("yaohlzt", yaohlzt);
		/*
		 * String js = null; String conectSql = "to_date('" + params.get("qssj")
		 * + "','YYYY-MM-DD')" + "and " + "to_date('" + params.get("jssj") +
		 * "','YYYY-MM-DD')"; if (Const.SJ_1.equals(sj)) { // 发运时间 js =
		 * " and  faysj between " + conectSql; } else if (Const.SJ_2.equals(sj))
		 * { // 实际发运时间 js = " and  shijfysj between " + conectSql; } else if
		 * (Const.SJ_3.equals(sj)) { // 最晚时间 js = " and  zuiwsj between " +
		 * conectSql;
		 * 
		 * } else if (Const.SJ_4.equals(sj)) { // 交付时间 js =
		 * " and  jiaofj between " + conectSql; } else { js = null; }
		 */
		if (!params.get("yaohllx").equals("SG")) {
			getWlgyyzDto(params);
		} else if (!StringUtils.isEmpty(params.get("keh"))) {
			params.put("keh", "keh='" + params.get("keh") + "'");
		}
		// 动态传参拼接SQL
		// params.put("shij", js);
		// 分页查询-内部要货令
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("yhl.queryYaohlN", params, page);
	}

	public List<Yaohl> nyaoHlDownLoadFile(Pageable page, Map<String, String> params) {
		if (page != null) {
			return (List<Yaohl>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("yhl.queryYaohlN", params, page).get("rows");
		} else {
			return (List<Yaohl>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("yhl.queryYaohlN", params);
		}
	}

	public BigDecimal countYaohlN(Map<String, String> params) {
		// String sj = params.get("sj"); // 获取时间
		// 获取查询状态
		String zt = params.get("zt");
		String yaohlzt = "";
		if ((zt != null) && (!"".equals(zt))) {
			String[] str = zt.split(",");
			// 拼接查询条件-到达状态
			yaohlzt += " and (";
			for (int i = 0; i < str.length; i++) {
				yaohlzt += "yaohlzt=" + "'" + str[i] + "'";
				if (str.length != 1 && i != str.length - 1) {
					yaohlzt += " or ";
				}
			}
			yaohlzt += " )";
		}
		params.put("yaohlzt", yaohlzt);
		if (!params.get("yaohllx").equals("SG")) {
			getnbWlgyyzDto(params);
		} else if (!StringUtils.isEmpty(params.get("keh"))) {
			params.put("keh", "keh='" + params.get("keh") + "'");
		}
		return (BigDecimal) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("yhl.countYaohlN", params);
	}

	private void getWlgyyzDto(Map<String, String> param) {
		String keh = param.get("keh");
		boolean ispoa = xAuthority.isPoa(AuthorityUtils.getSecurityUser());
		if (ispoa && keh == null) {
			param.put("keh", "");
		} else if (!ispoa && keh == null) {
			List<Map<String, Object>> ls = xAuthority.getWulgyyz(param.get("yaohllx"), "");
			StringBuffer kehdVal = new StringBuffer();
			kehdVal.append("keh");
			for (int i = 0; i < ls.size(); i++) {
				Map<String, Object> qxmap = ls.get(i);
				String khvalue = qxmap.get("KEH").toString();
				if (i == 0) {
					kehdVal.append(" in(");
				}
				if (i != ls.size() - 1) {
					kehdVal.append("'" + khvalue + "',");
				} else {
					kehdVal.append("'" + khvalue + "')");
				}
			}
			param.put("keh", ls.size() == 0 ? "keh=''" : kehdVal.toString());
		} else if (keh != null) {
			List<Map<String, Object>> ls = xAuthority.getWulgyyz(param.get("yaohllx"), keh);
			if (ls.size() != 0) {
				param.put("keh", "keh='" + keh + "'");
			} else {
				param.put("keh", "keh=''");
			}
		}
	}

	private void getnbWlgyyzDto(Map<String, String> param) {
		String keh = param.get("keh");
		boolean ispoa = xAuthority.isPoa(AuthorityUtils.getSecurityUser());
		if (ispoa && keh == null) {
			param.put("keh", "");
		} else if (!ispoa && keh == null) {
			List<Map<String, Object>> ls = xAuthority.getWulgyyz(param.get("yaohllx"), "");
			StringBuffer kehdVal = new StringBuffer();
			kehdVal.append("keh");
			for (int i = 0; i < ls.size(); i++) {
				Map<String, Object> qxmap = ls.get(i);
				String khvalue = qxmap.get("KEH").toString();
				if (i == 0) {
					kehdVal.append(" in(");
				}
				if (i != ls.size() - 1) {
					kehdVal.append("'" + khvalue + "',");
				} else {
					kehdVal.append("'" + khvalue + "')");
				}
			}
			param.put("keh", ls.size() == 0 ? "keh=''" : kehdVal.toString());
		} else if (keh != null && !"".equals(keh)) {
			List<Map<String, Object>> ls = xAuthority.getWulgyyz(param.get("yaohllx"), keh);
			if (ls.size() != 0) {
				param.put("keh", "keh='" + keh + "'");
			} else {
				param.put("keh", "keh=''");
			}
		}
	}

	/**
	 * 修改要货令的状态为已终止(外部)
	 */
	@Transactional
	public int updateYhl(Map<String, Object> params, String neweditor) {
		// 修改人
		params.put("neweditor", neweditor);
		// 修改时间
		params.put("edittime", CommonFun.getJavaTime());
		// 更新外部要货令
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("yhl.updateYhl", params);

	}

	/**
	 * 修改内部要货令的状态为已终止
	 */
	@Transactional
	public int updateYhlN(Map<String, Object> params, String neweditor) {
		// 修改人
		params.put("neweditor", neweditor);
		// 修改时间
		params.put("edittime", CommonFun.getJavaTime());
		// 修改内部要货令状态
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("yhl.updateYhlN", params);
	}

	/**
	 * 查询零件是否存在
	 */
	public boolean selectLingj(Map map) {
		// 查询零件表
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("yhl.queryLingj", map);
		return (list.size() != 0);

	}
	/**
	 * 查询批量终止要货令 V4_023
	 */

	public Map<String, Object> queryPlzzyhl(Pageable page, Map<String, String> params) { 
		// 查询批量终止要货令
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("yhl.queryYaohlzz", params, page);
	}

	public Map<String, Object> queryPlcjlsk(Pageable page, Map<String, String> map) {
		// TODO Auto-generated method stub
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectPages("yhl.queryPlcjlsk", map, page);
	}
	//huxy_12940
	public String deletelsk(Plcjlsk lsk) {
		lsk.setEdit_time(CommonFun.getJavaTime());
		
		List<Plcjlsk> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("yhl.querylsk",lsk);
		if(list.size() < 1)
		{
			return "只有创建和创建失败状态的数据才能删除";
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("yhl.deletelsk", lsk);
		 
		return "删除成功";
	}
}