package com.athena.fj.module.interfaces;

import javax.jws.WebService;

/**
 * @author 王国首
 * @date 2012-8-16
 * @time 上午11:16:07
 * @description 定时更新服务
 */
@WebService
public interface IFJScheduleService {
	
	/**
	 * @author 王国首
	 * @date 2012-8-16
	 * @time 上午11:16:54
	 * @description  定时更新服务
	 */
	public String scheduleRun(String bizJson);

}
