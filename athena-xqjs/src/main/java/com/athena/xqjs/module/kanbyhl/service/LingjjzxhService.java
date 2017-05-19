package com.athena.xqjs.module.kanbyhl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.module.common.service.XqjsAuthority;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * 批量零件截止循环service
 * 
 * @author hanwu
 * @date 2015-07-09
 */
@Component
public class LingjjzxhService extends BaseService{
	
	@Inject
	private XqjsAuthority xAuthority;
	
	/**
	 * 查询所有的看板循环规模，并封装成map<key：用户中心+零件编号  value：看板循环规模集合>形式
	 */
	@SuppressWarnings("unchecked")
	public Map<String,List<Kanbxhgm>> getKanbxhMap(){
		List<Kanbxhgm>list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).select("kanbyhl.queryAllKbxhgm");
		//封装看板循环规模  key：用户中心+零件编号  value：看板循环规模集合
		Map<String,List<Kanbxhgm>> kanbMap=new HashMap<String,List<Kanbxhgm>>();
		for (Kanbxhgm xhgm : list) {
			//看板循环规模map存在 当前用户中心+用户编号，则循环编码集合加上当前看板循环规模
			if(kanbMap.containsKey(xhgm.getUsercenter()+xhgm.getLingjbh())){
				List<Kanbxhgm> kanbList = kanbMap.get(xhgm.getUsercenter()+xhgm.getLingjbh());
				kanbList.add(xhgm);
			}
			//看板循环规模map不存在 当前用户中心+用户编号，则new一个循环编码集合
			else{
				List<Kanbxhgm> kanbList = new ArrayList<Kanbxhgm>();
				kanbList.add(xhgm);
				kanbMap.put(xhgm.getUsercenter()+xhgm.getLingjbh(), kanbList);
			}
	    }
		return kanbMap;
	}
	
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
			aaa += "decode(kb.dangqxhgm, 0, null, ceil((kb.jisxhgm-kb.dangqxhgm) / kb.dangqxhgm * 100))='0'";
		} else if (null != gmbh && !gmbh.equals("") && bh != null && gmbh.equals("02")) {
			// 计算循环规模小于当前循环规模
			aaa += "decode(kb.dangqxhgm, 0, null, ceil((kb.jisxhgm-kb.dangqxhgm) / kb.dangqxhgm * 100))<=-" + Integer.parseInt(bh);
		} else if (null != gmbh && !gmbh.equals("") && bh != null && gmbh.equals("03")) {
			// 计算循环规模大于当前循环规模
			aaa += "decode(kb.dangqxhgm, 0, null, ceil((kb.jisxhgm-kb.dangqxhgm) / kb.dangqxhgm * 100))>=" + Integer.parseInt(bh);
		}
		if ("RD".equalsIgnoreCase(param.get("gonghms")) || "RM".equalsIgnoreCase(param.get("gonghms")) || param.get("nwb").equalsIgnoreCase("n")) {
			getWlgyyzDto(param);
		} else if (param.get("kehd") != null) {
			param.put("kehd", "kb.kehd='" + param.get("kehd") + "'");
		}
		param.put("gmbh", aaa);
		if(param.get("gonghms")==null&&param.get("nwb").equalsIgnoreCase("w")){
			param.put("ghms", "kb.gonghms in('R1','R2')");
		} else if (param.get("gonghms") == null && param.get("nwb").equalsIgnoreCase("n")) {
			param.put("ghms", "kb.gonghms in('RD','RM')");
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kanbyhl.selectLingjjzxh", param, page);
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
			kehdVal.append("substr(kb.kehd,0,5)");
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
			param.put("kehd", ls.size() == 0 ? "kb.kehd=''" : kehdVal.toString());
		} else if (kehd != null && ls.size() != 0) {
			param.put("kehd", "substr(kb.kehd,0,5)='" + kehd + "'");
		} else {
			param.put("kehd", "kb.kehd=''");
		}
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
			param.put("kehd", "m.kehd='" + param.get("kehd") + "'");
		}
		if(param.get("gonghms")==null&&param.get("nwb").equalsIgnoreCase("w")){
			param.put("ghms", "m.gonghms in('R1','R2')");
		} else if (param.get("gonghms") == null && param.get("nwb").equalsIgnoreCase("n")) {
			param.put("ghms", "m.gonghms in('RD','RM')");
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kanbyhl.selectLingjjzxhYjfzl", param, page);
	}
}
