/**
 * 
 */
package com.athena.print;

/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author wy
 * @version 1.0.0
 */
public final class Constants {
	
	public static final int TASK_STATUS_PRE= -1;  //准备作业
	
	public static final int TASK_STATUS_NEW = 0;  //新作业
	
	public static final int TASK_STATUS_SENDING_BROKEN = 1;//中断的作业，文件发送没有完成
	
	//public static final String TASK_STATUS_SENDING = "3";//作业发送中
	
	public static final int TASK_STATUS_PRINGING = 2;//打印中
	
	public static final int TASK_STATUS_SUCCESS = 3;//打印完成
	
	public static final int TASK_STATUS_CANLE = 4;//打印取消
	
	public static final int TASK_STATUS_NOSEND= 0;//子作业未发送打印
	
	public static final int TASK_STATUS_SEND= 1;//子作业已发送打印
	
	public static final int TASK_STATUS_SEQCANLE= 2;//子作业打印取消
	
	//public static final String TASK_STATUS_OUT_PAPER = "6";//打印缺纸
	
	//public static final String TASK_STATUS_JAM_PAPER = "7";//打印卡纸
	
	//public static final String TASK_STATUS_ALERT = "8";//打印机其他警报
	
	//public static final String TASK_STATUS_CONN_BROKEN = "9";//打印连接故障

	public static final int PIRNTER_IDLE = 0;//打印机空闲
	
	public static final int PIRNTER_BUSY = 1;//打印机繁忙
	
	public static final int PIRNTER_OUT_PAPER = 2;//打印机缺纸
	
	public static final int PIRNTER_JAM_PAPER = 3;//打印机卡纸
	
	public static final int PIRNTER_STATUS_ALERT = 5;//打印机故障(其他警报)
	
	public static final int PIRNTER_CONN_BROKEN = 4;//打印网络故障
}
