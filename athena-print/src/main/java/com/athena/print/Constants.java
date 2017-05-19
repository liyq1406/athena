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
 * @author zhouyi
 * @version 1.0.0
 */
public  class Constants {
	
public static final int TASK_STATUS_PRE= -1;  //准备作业
	
	public static final int TASK_STATUS_NEW = 0;  //新作业
	
	public static final int TASK_STATUS_SENDING_BROKEN = 1;//中断的作业，文件发送没有完成
	
	//public static final String TASK_STATUS_SENDING = "3";//作业发送中
	
	public static final int TASK_STATUS_PRINGING = 2;//打印中
	
	public static final int TASK_STATUS_SUCCESS = 3;//打印完成
	
	public static final int TASK_STATUS_CANLE = 4;//打印取消
	
	public static final String DEVICECODE = "NONE";//设备初始化的状态是未分
	
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
	
	
	
	public static final String PRINT_DICT_BHD = "p1"; //备货单
	
	public static final String PRINT_DICT_AXBHD = "p2"; //按需备货单
	
	public static final String PRINT_DICT_YCSBBHD = "p3"; //异常申报备货单
	
	public static final String PRINT_DICT_TBBHD = "p4"; //同步备货单
	
	public static final String PRINT_DICT_TBGJBHD = "p5"; //同步归集备货单
	
	public static final String PRINT_DICT_DBBHD = "p6"; //调拨备货单
	
	public static final String PRINT_DICT_CPKBHD = "p7"; //成品库备货单
	
	public static final String PRINT_DICT_UCBQ = "p8"; //UC标签
	
	public static final String PRINT_DICT_CKD = "p9"; //出库单
	
	public static final String PRINT_DICT_DBCKD = "p10"; //调拨出库单
	
	public static final String PRINT_DICT_FXD = "p11"; //返修单
	
	public static final String PRINT_DICT_THD = "p12"; //退货单
	
	public static final String PRINT_DICT_XHD = "p13"; //销毁单
	
	public static final String PRINT_DICT_FXRKD = "p14"; //返修入库单
	
	public static final String PRINT_DICT_DHD = "p16"; //到货单
	
	public static final String PRINT_DICT_YSD = "p17"; //验收单
	
	public static final String PRINT_DICT_JSGZD = "p18"; //拒收跟踪单
	
	public static final String PRINT_DICT_JSD = "p19"; //拒收单
	
	public static final String PRINT_DICT_CPKBQ = "p20"; //成品库标签(US三栏)
	
	public static final String PRINT_DICT_USBQ = "p21"; //US标签(两栏)
	
	public static final String PRINT_DICT_ZZGZD= "p22"; //质检跟踪单
	
	public static final String PRINT_DICT_GBZGZD = "p23"; //改包装跟踪单
	
	public static final String PRINT_DICT_JFD = "p24"; //纠纷单
	
	public static final String PRINT_DICT_PDD = "p25"; //盘点单
	
	public static final String PRINT_DICT_UAK = "p26"; //UA卡-排产发交
	
	public static final String PRINT_DICT_PZD = "p27"; //配载单-排产发交
	
	public static final String PRINT_DICT_FHTZD = "p28"; //发货通知单BL
	
	public static final String PRINT_DICT_TBBZDYK = "p29"; //同步包装单元卡
	
	public static final String PRINT_DICT_TBGJBZDYK = "p30"; //同步归集包装单元卡
	
	public static final String PRINT_DICT_ZDBZDYK = "p31"; //指导包装单元卡装箱单
	
	public static final String PRINT_DICT_TBYSD = "p32"; //同步验收单
	
	public static final String PRINT_DICT_TBDHD = "p33"; //同步到货单
	
	public static final String PRINT_DICT_YKQD = "p34"; //移库清单
	
	public static final String PRINT_DICT_AXUCBQ = "p35"; //UC标签(按需)
	
	public static final String PRINT_DICT_TBJSD = "p36"; //同步拒收单
	
	public static final String PRINT_DICT_YKBHD = "p37"; //移库备货单
	
	public static final String PRINT_DICT_CKYKQD = "p38"; //仓库间移库清单
	
	public static final String PRINT_DICT_DQYKD = "p39"; //单取移库单
	
	public static final int PRINT_PRINTNUMBER = 1; //打印单据份数
	
	public static final int PRINT_PRINTUNITCOUNT = 1; //打印单据联数
	
	public static final String PRINT_PRINTTYPE_A = "A"; //打印单据样式:A-样式123123,B-样式112233
	
	public static final String PRINT_PRINTTYPE_B = "B"; //打印单据样式:A-样式123123,B-样式112233 
	
	public static final String PRINT_QUY_ONE = "1"; //打印区域: 1单区域 2多区域
	
	public static final String PRINT_QUY_TWO = "2"; //打印区域: 1单区域 2多区域
	
	public static final int PRINT_BIAOS= 0; //打印作业辅状态,0停止,1启动
	
	public static final int PRINT_LIST= 2; //打印队列 grid 表格  所在区域
	
	public static final int PRINT_SHEET_GRID= 1; //打印队列 grid 表单  所在区域
	
	public static final int PRINT_SHEET_GRIDS= 3; //打印队列 grid 表单  所在区域
}
