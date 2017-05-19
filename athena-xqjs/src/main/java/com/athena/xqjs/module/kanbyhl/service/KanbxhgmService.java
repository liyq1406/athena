package com.athena.xqjs.module.kanbyhl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.module.common.service.XqjsAuthority;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 看板循环管理service
 * 
 * @author Xiahui
 * @date 2012-1-31
 * @param page
 * @param params
 * @return
 */
@Component
public class KanbxhgmService extends BaseService {

	@Inject
	private XqjsAuthority xAuthority;
	/**
	 * 查询看板循环规模
	 * 
	 * @param page
	 * @param param
	 * @return Map<String, Object>
	 */
	public Map<String, Object> select(Pageable page, Map<String, String> param) {
		// 规模变化
		String gmbh = param.get("gmbh");
		// 获取规模变化的范围
		String bh = param.get("bh");
		String aaa = "";
		if ("01".equalsIgnoreCase(gmbh)) {
			// 计算循环规模等于当前循环规模
			aaa += "decode(dangqxhgm, 0, null, ceil((jisxhgm-dangqxhgm) / dangqxhgm * 100))='0'";
		} else if (null != gmbh && !gmbh.equals("") && bh != null && gmbh.equals("02")) {
			// 计算循环规模小于当前循环规模
			aaa += "decode(dangqxhgm, 0, null, ceil((jisxhgm-dangqxhgm) / dangqxhgm * 100))<=-" + Integer.parseInt(bh);
		} else if (null != gmbh && !gmbh.equals("") && bh != null && gmbh.equals("03")) {
			// 计算循环规模大于当前循环规模
			aaa += "decode(dangqxhgm, 0, null, ceil((jisxhgm-dangqxhgm) / dangqxhgm * 100))>=" + Integer.parseInt(bh);
		}
		if ("RD".equalsIgnoreCase(param.get("gonghms")) || "RM".equalsIgnoreCase(param.get("gonghms")) || param.get("nwb").equalsIgnoreCase("n")) {
			getWlgyyzDto(param);
		} else if (param.get("kehd") != null) {
			param.put("kehd", "kehd='" + param.get("kehd") + "'");
		}
		param.put("gmbh", aaa);
		if(param.get("gonghms")==null&&param.get("nwb").equalsIgnoreCase("w")){
			param.put("ghms", "gonghms in('R1','R2')");
		} else if (param.get("gonghms") == null && param.get("nwb").equalsIgnoreCase("n")) {
			param.put("ghms", "gonghms in('RD','RM')");
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kanbyhl.selectKbxhgm", param, page);
	}
	/**
	 * 查询看板循环规模 --已交付量
	 * 
	 * @param page
	 * @param param
	 * @return Map<String, Object>
	 */
	public Map<String, Object> selectYjfzl(Pageable page, Map<String, String> param) {
		
		if ("RD".equalsIgnoreCase(param.get("gonghms")) || "RM".equalsIgnoreCase(param.get("gonghms")) || param.get("nwb").equalsIgnoreCase("n")) {
			getWlgyyzDto(param);
		} else if (param.get("kehd") != null) {
			param.put("kehd", "kehd='" + param.get("kehd") + "'");
		}
		if(param.get("gonghms")==null&&param.get("nwb").equalsIgnoreCase("w")){
			param.put("ghms", "gonghms in('R1','R2')");
		} else if (param.get("gonghms") == null && param.get("nwb").equalsIgnoreCase("n")) {
			param.put("ghms", "gonghms in('RD','RM')");
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kanbyhl.selectKbxhgmYjfzl", param, page);
	}
	private void getWlgyyzDto(Map<String, String> param) {
		String kehd = param.get("kehd");
		boolean ispoa = xAuthority.isPoa(AuthorityUtils.getSecurityUser());
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();
		if (param.get("gonghms") != null) {
			ls = xAuthority.getWulgyyz(param.get("gonghms"), kehd);
		} else {
			List<Map<String, Object>> lsRd = xAuthority.getWulgyyz("RD", kehd);
			List<Map<String, Object>> lsRm = xAuthority.getWulgyyz("RM", kehd);
			ls.addAll(lsRd);
			ls.addAll(lsRm);
		}

		if (ispoa && kehd == null) {
			param.put("kehd", "");
		} else if (!ispoa && kehd == null) {
			StringBuffer kehdVal = new StringBuffer();
			kehdVal.append("substr(kehd,0,5)");
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
			param.put("kehd", ls.size() == 0 ? "kehd=''" : kehdVal.toString());
		} else if (kehd != null && ls.size() != 0) {
			param.put("kehd", "substr(kehd,0,5)='" + kehd + "'");
		} else {
			param.put("kehd", "kehd=''");
		}
	}

	/**
	 * 修改看板循环规模最大要货量
	 * 
	 * @param kanb
	 * @return
	 */
	public int updateZuidyhl(Kanbxhgm kanb) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateZuidyhl", kanb);
	}

	/**
	 * 查询生产线
	 * 
	 * @param param
	 * @return list
	 */
	public List<?> selectChanx(Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kanbyhl.selectChanx", param);
	}

	/**
	 * 批量修改看板循环规模中的数据
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public String doUpdate(List<Kanbxhgm> ls) {
		// 默认FALSE
		String flag = "true";
		for (int i = 0; i < ls.size(); i++) {
			// 更新看板循环规模表
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateKandxhgm",
					ls.get(i));
			/* xh 041218  修改仓库供应商交付总量后  准备层 操作时 缺少表报错
			//xh 10825冻结时需修改仓库供应商交付总量为0 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).execute("kanbyhl.updateYjfzldj",ls.get(i));*/
			if (count == 0) {
				flag = "false";
				break;
			}
		}
		return flag;
	}
	/**
	 * 
	 * @param kanbxhgm
	 * @return
	 */
	@Transactional
	public Integer doUpdate(Kanbxhgm kanbxhgm){
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateYjfzl",
				kanbxhgm);
		return count;
	}
	/**
	 * 批量修改看板循环规模中的数据
	 * 
	 * @param ls
	 * @return
	 */
	@Transactional
	public void doUpdate(List<Kanbxhgm> ls, String time, LoginUser loginUser) {
		for (int i = 0; i < ls.size(); i++) {
			ls.get(i).setEdit_time(time);
			ls.get(i).setEditor(loginUser.getUsername());
			// 更新看板循环规模表
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateKandxhgm",
					ls.get(i));

		}
	}
	
	/**
	 * 查询看板循环规模查看零件消耗点或仓库编号信息是否存在
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getXiaohdckMap(){
		List<Kanbxhgm>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).select("kanbyhl.queryXiaohdck");
		Map<String,String> map=new HashMap<String,String>();
		for (Kanbxhgm xhgm : list) {
			if(xhgm.getGonghms().equals("R1")){
			map.put("R1"+xhgm.getUsercenter()+xhgm.getLingjbh()+xhgm.getXiaohd(), xhgm.getXunhbm());
			}else if(xhgm.getGonghms().equals("R2")){
			map.put("R2"+xhgm.getUsercenter()+xhgm.getLingjbh()+xhgm.getCangkdm(), xhgm.getXunhbm());
			}
	    }
		return map;
	}
}
