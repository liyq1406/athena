package com.athena.pc.module.webInterface;

import javax.jws.WebService;

@WebService
public interface IPCDailyProduceService {

	/**
	 * 滚动月模拟排产接口服务
	 * @date 2012-05-16
	 * @param bizJson 客户端业务请求json串
	 * @return 返回客户端需求json字符串
	 * @throws ServiceException
	 */
	public String callPcDailyProduce(String bizJson);
	
}
