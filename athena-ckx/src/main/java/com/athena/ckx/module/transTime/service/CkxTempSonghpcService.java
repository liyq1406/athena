package com.athena.ckx.module.transTime.service;

import java.util.List;

import java.util.HashMap;

import java.util.Map;



import com.athena.ckx.entity.transTime.CkxGongysChengysXiehzt;
import com.athena.ckx.entity.transTime.CkxTempMon;
import com.athena.ckx.util.GetMessageByKey;


import com.athena.component.service.BaseService;

import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
/**
 * 送货频次
 * @author hj
 *
 */
@Component
public class CkxTempSonghpcService extends BaseService<CkxTempMon> {

	@Inject
	private CkxFrequencyService gongyschengysxiehztService;//供应商-承运商-卸货站台-送货频次
	protected String getNamespace() {
		return "transTime";
	}
	
	
	/**
	 * 检查是否存在此卸货站台
	 * map存放卸货站台的模拟次数
	 * @param insert
	 * @param usercenter
	 * @return
	 */
	public void checkXiehzt(List<CkxTempMon> MonList,String usercenter){
		Map<String,String> map = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		for (CkxTempMon ckxTempMon : MonList) {
			//将每个卸货站台的模拟次数存入map
			if(!map.containsKey(ckxTempMon.getXiehztbh())){
				map.put(ckxTempMon.getXiehztbh(),"");
			}else{
				//若同一卸货站台计算多次，则记录异常
//				throw new ServiceException("同一卸货站台编组只能计算一次");
				//卸货站台编组："+ckxTempMon.getXiehztbh()+"同时只能计算一次
				sb.append(GetMessageByKey.getMessage("xhztbztsznjsyc",new String[]{ckxTempMon.getXiehztbh()})+";");
			}
			CkxGongysChengysXiehzt ckxGongysChengysXiehzt = new CkxGongysChengysXiehzt();
			ckxGongysChengysXiehzt.setXiehztbh(ckxTempMon.getXiehztbh());
			ckxGongysChengysXiehzt.setUsercenter(usercenter);
			ckxGongysChengysXiehzt.setBiaos("1");
			//查询当前用户中心下的数据位有效的，卸货站台为传递过来的 是否存在，若不存在，则不能计算此卸货站台
			Map<String,Object> ckxGongysChengysXiehztMap =  gongyschengysxiehztService.select(ckxGongysChengysXiehzt);
			if("0".equals(ckxGongysChengysXiehztMap.get("total").toString())){
				//"卸货站台编组："+ckxTempMon.getXiehztbh()+"不存在于卸货站台编组送货频次中;"
				sb.append(GetMessageByKey.getMessage("xhztbzbczshpc",new String[]{ckxTempMon.getXiehztbh()}));
			}
		}
		if(!"".equals(sb.toString())){
			throw new ServiceException(GetMessageByKey.getMessage("jssb")+sb.toString());
		}
		
	}
}
