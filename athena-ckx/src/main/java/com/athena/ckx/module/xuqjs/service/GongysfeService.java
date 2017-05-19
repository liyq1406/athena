package com.athena.ckx.module.xuqjs.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.xuqjs.Gongysfe;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class GongysfeService extends BaseService<Gongysfe>{
	
	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 查询供应商份额
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Gongysfe> queryGongysfe(Gongysfe bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryGongysfe",bean);
	}
	
	/**
	 * 重置AB点供应商份额
	 * @param bean
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String resetGongysfe(Gongysfe bean){
		//获取供应商份额
		List<Gongysfe> gongysfeList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryGongysfe",bean);
		//看板扫卡的每个供应商的总要货量
		List<Map<String, Object>> kbsklsList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getKanbskyhl",bean);
		//要货令的每个供应商的总要货量
		List<Map<String, Object>> yaohlList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getYaohlyhl",bean);
		//看板扫卡Map  用户中心+零件编号+供应商编号：要货量
		Map<String, BigDecimal> kbskMap = convertListToMap(kbsklsList);
		//要货令Map  用户中心+零件编号+供应商编号：要货量
		Map<String, BigDecimal> yaohlMap = convertListToMap(yaohlList);
		//将同一用户中心、零件编号、供应商的要货量相加 放到yaohlMap中
		for (String key : kbskMap.keySet()) {
			if(yaohlMap.containsKey(key)){
				yaohlMap.put(key, kbskMap.get(key).add(yaohlMap.get(key)));
			}else{
				yaohlMap.put(key, kbskMap.get(key));
			}
		}
		String msg = "重置AB点供应商份额";
		//更新供应商份额
		for (int i = 0; i < gongysfeList.size(); i++) {
			Gongysfe gongysfe = gongysfeList.get(i);
			msg += "，供应商"+gongysfe.getGongysdm()+ "份额重置之前的供应商表达量为" + gongysfe.getBiaodsl();
			gongysfe.setBiaodsl(0.0);
			gongysfe.setEdit_time(DateTimeUtil.getAllCurrTime());
			String key = gongysfe.getUsercenter()+gongysfe.getLingjbh()+gongysfe.getGongysdm();
			if(yaohlMap.containsKey(key)){
				gongysfe.setBiaodsl(yaohlMap.get(key).doubleValue());
			}
			msg += "重置之后的供应商表达量为" + gongysfe.getBiaodsl();
		}
		//插入数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(
				"ts_ckx.updateGongysfe", gongysfeList);
		return msg;
	}
	
	/**
	 * 将对象集合装换为 用户中心+零件编号+供应商编号：要货量 格式的Map
	 * @param list
	 * @return
	 */
	private Map<String, BigDecimal> convertListToMap(List<Map<String, Object>> list){
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		if(list != null){
			Map<String, Object> bean;
			for (int i = 0; i < list.size(); i++) {
				bean = list.get(i);
				map.put(String.valueOf(bean.get("USERCENTER"))+String.valueOf(bean.get("LINGJBH"))+
						String.valueOf(bean.get("GONGYSDM")), new BigDecimal(String.valueOf(bean.get("SL"))));
			}
		}
		return map;
	}
}
