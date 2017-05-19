package com.athena.ckx.util;

import javax.jws.WebService;



@WebService
public interface CkxCommonFunc {

	/**
	 * 运输时刻定时计算方法
	 * @return
	 */
	public String jisYunssk();
	/**
	 * 定时将模板数据导入运输时刻表
	 * @return
	 */
	public String insertTimeOut();
	/**
	 * 将物流路径写入数据表(定时任务)
	 */
	public String addWullj();
	
	/**
	 * 定时任务
	 * 定时检查版次号信息，如果没有与别的表关联则删除
	 * 定时检测有无两年前的版本，如果有，则删除
	 */
	public String timingTask();
	/**
	 * 定时计算  剔除休息时间
	 * @return
	 */
	public String calculate();
	/**
	 * 定时计算 cmj
	 * @return
	 */
	public String calculateCmj();
	/**
	 * 定式计算 小火车运输时刻
	 * @return
	 */
	public String calculateXiaohcYssk();
	
	
	/**
	 * 定时任务 根据生效时间和失效时间批量更新biaos
	 * @return
	 */
	public String updateUtilControlBiaos();
	
	/**
	 * 定时清除无用的版次
	 * 执行层专用
	 */
	public String clearVersion();
	/**
	 * 将未来编组号按照生效时间更新到编组号中，并清空未来编组号和生效时间
	 * 执行层专用
	 */
	public String updateWeilbzhTobianzh();
	/**
	 * 自动根据将来参数生效日赋值将来参数
	 * @return
	 */
	public String autoUpdateXiaohc();
	
	/**
	 * 定时计算小火车模板（执行层）
	 * @return
	 */
	public String calculateXiaohcmbCk();
	
	/**
	 *  新定时计算小火车模板（执行层）
	 * @return
	 */
	public String calculateXXiaohcmbCk();
	
	
	/**
	 * 时间合并-工作时间模板
	 * @return
	 */
	public String calulateHebTime();
	
	/**
	 * 批量更新仓库库位包装信息表
	 */
	public String updatekuwbzxx();
}
