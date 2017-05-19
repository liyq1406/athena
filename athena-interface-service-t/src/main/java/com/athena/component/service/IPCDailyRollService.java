package com.athena.component.service;


public interface IPCDailyRollService {

	/**
	 * 滚动月模拟排产接口服务
	 * @date 2011-12-30
	 * @param bizJson 客户端业务请求json串
	 * @return 返回客户端需求json字符串
	 * @throws ServiceException
	 */
	public String callPcDailyRoll(String bizJson);
	
}
