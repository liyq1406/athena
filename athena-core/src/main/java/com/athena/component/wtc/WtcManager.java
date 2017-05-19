package com.athena.component.wtc;

import com.athena.authority.entity.PltTrscode;

public interface WtcManager {
	
	
	/**
	 * 通过WTC请求Tuxedo服务
	 * @param wtcRequest
	 * {
	 * 'transCode'：'TX001',  //TX001  userName,userId
	 * 'userName':'lll'
	 * 'userId':'222'
	 * }
	 * 
	 * @return
	 */
	public WtcResponse request(WtcRequest wtcRequest, PltTrscode pltTrscode);
	
	
}
