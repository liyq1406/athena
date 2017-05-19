/**
 * 
 */
package com.athena.component.wtc;

import java.util.Map;

/**
 * 响应信息
 * @author Administrator
 *
 */
public class WtcResponse  extends WtcBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6216362830840583415L;

	public WtcResponse(Map<String,Object> map) {
		this.putAll(map);
	}
	
}
