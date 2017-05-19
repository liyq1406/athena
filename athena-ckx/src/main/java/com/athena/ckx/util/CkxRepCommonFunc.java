package com.athena.ckx.util;

import javax.jws.WebService;



@WebService
public interface CkxRepCommonFunc {

	
	/**
	 * 定时将数据插入到零件消耗点循环报表(bo)
	 * @return
	 */
	public String timingAddLingjxhdxh();
	
	
	
	/**
	 * 定时删除备货令跟踪超过3个月的数据(bo)
	 * @return
	 */
	public String timingDeleteRepBeihlgz();
	
	/**
	 * 定时将数据插入到开箱入库的集装箱报表(bo)
	 * @return
	 */
	public String timingAddkaixruk();
	
	/**
	 * 定时将数据插入到发运超过100天的零件清单报表(bo)
	 * @return
	 */
	public String timingAddfaycg100t();
}
