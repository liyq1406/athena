package com.athena.pc.module.webInterface;

import javax.jws.WebService;

@WebService
public interface IPCLeijjfService {

	/**
	 * 累计交付差额接口服务
	 * @date 2012-07-09
	 * @param bizJson 客户端业务请求json串
	 * @return 返回客户端需求json字符串
	 * @throws ServiceException
	 */
	public String callPcSchedule(String bizJson);
	
}
