package com.athena.truck.util;

import javax.jws.WebService;

/**
 * PDA webservice
 * @author CSY
 *
 */
@WebService
public interface PDATruckWebservice {
	
	/**
	 * PDA卡车服务
	 * @param param
	 * @return
	 */
	public String PDATruckService(String param);
	
}
