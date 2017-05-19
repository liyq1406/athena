package com.athena.xqjs.module.common;

import java.math.BigDecimal;

public class Const {
	
	/**
	 * 系统参数定义：需求来源
	 * **/
	public static final String ZIDLX_XQLY="XQLY" ;
	public static final String GYSLX="2" ;
	public static final String MAXZQ="12" ;
	public static final String MINZQ="01" ;

	/**
	 * 物流组
	 */
	public static final String QX_WULGYYZ = "WULGYYZ";
	/**
	 * 是否计算要货令
	 * **/
	public static final String SHIFYJSYHL_Y = "2" ;
	/**
	 * 是否已计算要货令
	 * **/
	public static final String SHIFYJSYHL_N = "0" ;
	/**
	 * 有效
	 * **/
	public static final String ACTIVE_1 = "1" ;
	
	/**
	 * 失效
	 * **/
	public static final String ACTIVE_0 = "0" ;
	
	/**
	 * 字符0
	 * **/
	public static final String STRING_ZERO = "0";

	/**
	 * 字符1
	 * **/
	public static final String STRING_ONE = "1";
	/**
	 * 字符2
	 * **/
	public static final String STRING_TWO = "2";
	/**
	 * 字符3
	 * **/
	public static final String STRING_THREE = "3";
	/**
	 * 时间12点
	 * **/
	public static final String TIME_12 = " 12:00:00" ;
	
	/**
	 * 订单类型：0kd
	 * **/
	public static final String DINGDLX_KD = "1" ;
	/**
	 * 订单类型：2ts
	 * **/
	public static final String DINGDLX_TS= "4" ;
	/**
	 * 订单类型：2aix
	 * **/
	public static final String DINGDLX_AIX = "2" ;
	/**
	 * 订单类型：3卷料
	 * **/
	public static final String DINGDLX_JL = "9";
	/**
	 * 订单类型：1表示是DINGHCK,0表示XIANBCK
	 * **/
	public static final String CANGKLX = "1" ;
	
	/**
	 * 初始化布线标志：1表示是
	 * **/
	public static final String CHUSHBX_Y = "1" ;
	/**
	 * 初始化布线标志：0表示不是
	 * **/
	public static final String CHUSHBX_N = "0" ;
	
	/**
	 * 按需模式C1
	 * */
	public static final String ANX_MS_C1 = "C1" ;
	
	/**
	 * 按需模式M1
	 * */
	public static final String ANX_MS_M1 = "M1" ;
	
	/**
	 * 按需模式CD
	 * */
	public static final String ANX_MS_CD = "CD" ;
	
	/**
	 * 按需模式MD
	 * */
	public static final String ANX_MS_MD = "MD" ;
	

	/**
	 * 按CD C1模式的订单号前缀
	 */
	public static final String C1 = "C";
	/**
	 * 按CD C1模式的订单号前缀
	 */
	public static final String CD = "D";
	/**
	 * 按MD M1模式的订单号前缀
	 */
	public static final String M1 = "M";
	/**
	 * 按MD M1模式的订单号前缀
	 */
	public static final String MD = "N";

	/**
	 * 临时订单号前缀
	 */
	public static final String LS = "T";
	
	
	/**
	 * 报警件临时订单号前缀
	 */
	public static final String BJ_LS = "J";
	
	public static final String BJ = "BJ";

	/**
	 * 按需计算：字段名称
	 * */
	public static final String ZIDMC = "按需计算时间间隔" ;
	
	/**
	 * 订单类型：IL
	 * **/
	public static final String DINGDLX_ILORDER = "0";

	/**
	 * 订单类型：KD
	 * **/
	public static final String DINGDLX_KDORDER = "1";
	
	/**
	 * 订单类型：爱信订单
	 * **/
	public static final String DINGDLX_AIXING = "2";

	/**
	 * 订单类型：临时订单
	 * **/
	public static final String DINGDLX_LINS = "3";

	/**
	 * 订单类型：特殊订单
	 * **/
	public static final String DINGDLX_TIESHU = "4";

	/**
	 * 订单类型：按需订单
	 * **/
	public static final String DINGDLX_ANXUE = "5";
	/**
	 * 订单类型：卷料订单
	 * **/
	public static final String DINGDLX_JUANL = "6";
	
	/**
	 * 零件制造路线：IL
	 */
	public static final String ZHIZAOLUXIAN_IL = "97W";
	/**
	 * 零件制造路线：GL
	 */
	public static final String ZHIZAOLUXIAN_GL = "UGB";

	/**
	 * 零件制造路线：KD_PSA
	 */
	public static final String ZHIZAOLUXIAN_KD_PSA = "97X";

	/**
	 * 零件制造路线：KD_爱信
	 */
	public static final String ZHIZAOLUXIAN_KD_AIXIN = "97D";

	// 陈骏
	
	/**
	 * kd计算专用版次
	 */
	public static final String KDBANCI = "NA01KD01";
	/**
	 * 爱信计算专用版次
	 */
	public static final String AXBANCI = "NA01AX01";
	/**
	 * 拉箱计算专用版次
	 */
	public static final String LAXBANCI = "NA01LX01";
	/**
	 * 标识生效
	 */
	public static final String SHENGXIAO = "1";
	/**
	 * 标识失效
	 */
	public static final String SHIXIAO = "0";
	/**
	 * KD周期订单的工作日历只使用“UW”用户中心下这一版
	 */
	public static final String KDRILI = "UW";
	/**
	 * 每周周一
	 */
	public static final String ZHOUYI = "1";
	/**
	 * 周期订单：汇总表最大周期数
	 */
	public static final String WZKCZM = "A35";
	/**
	 * 周期订单：汇总表最大周期数
	 */
	public static final int ZPCHANGDU = 11;
	/**
	 * 周期周订单：汇总表最大周数
	 */
	public static final int ZSCHANGDU = 10;
	/**
	 * 周期日订单：汇总表最大周数
	 */
	public static final int ZJCHANGDU = 15;
	/**
	 * 周期日订单：订单内容长度
	 */
	public static final int DINGDNRCHANGDU = 10;
	
	public static final Integer YSGSHUNXH = 500;
	public static final Integer SHGSHUNXH = 300;
	public static final Integer QIYSHUNXH = 30;
	/**
	 * 非周期：非周期类型
	 */
	public static final String FEIZHOUQIPP = "NP";
	/**
	 * 非周期：非周类型
	 */
	public static final String FEIZHOUQIPS = "NS";
	/**
	 * 非周期：非日类型
	 */
	public static final String FEIZHOUQIPJ = "NJ";
	/**
	 * 供应商类型：外部
	 */
	public static final String WAIBUGONGYINGSHANG = "2";
	/**
	 * 异常报警类型 lx0 零件未生效
	 */
	public static final String YICHANG_LX0 = "000";
	
	public static final String YICHANG_LX0_str1 = "用户中心%1下零件%2不是生效零件";
	
	
	
	/**
	 * 异常报警类型 lx1 毛需求错误
	 */
	public static final String YICHANG_LX1 = "100";
	
	public static final String YICHANG_LX1_str1 = "毛需求制造路线和参考系维护不一致";
	
	
	/**
	 * 异常报警类型 lx2 参数缺失
	 */
	public static final String YICHANG_LX2 = "200";
	/**
	 * 异常报警类型 lx2 参数仓库为空
	 */
	public static final String YICHANG_LX2_str1 = "用户中心%1下零件%2的以下参数有误:查询零件消耗点分配区仓库为空,产线%3";
	
	public static final String YICHANG_LX2_str2 = "用户中心%1下零件%2的装车系数有误";
	
	public static final String YICHANG_LX2_str3 = "用户中心%1下零件%2的以下参数有误:%3（%3=存在多个订货仓库）";
	
	public static final String YICHANG_LX2_str4 = "用户中心%1下零件%2的以下参数有误:%3";//（%3=备货周期）
	
	public static final String YICHANG_LX2_str5 = "用户中心%1下零件%2的以下参数有误:%3";//(%3=线边仓库)
	
	public static final String YICHANG_LX2_str6 = "订货路线%1周期%2内的既定-预告-周期表订单内容字段为空";
	
	public static final String YICHANG_LX2_str7 = "%1模式送货频次数据有误：CKX_WULLJ表用户中心%2，零件号%3，承运商%4，卸货站台%5";
	
	public static final String YICHANG_LX2_str8 = "线边要货类型数据有误：CKX_LINGJXHD表，用户中心%1，零件号%2，消耗点编号%3";
	
	public static final String YICHANG_LX2_str9 =  "仓库US包装信息错误：CKX_LINGJCK表，用户中心%1，零件号%2，线边仓库%3";
	
	public static final String YICHANG_LX2_str10 = "是否延迟上线:I/P信息错误：CKX_LINGJXHD表，用户中心%1，零件号%2，消耗点编号%3";
	
	public static final String YICHANG_LX2_str11 = "用户中心%1下零件%2的以下参数有误:%3（%3=零件-计划员组）";
	
	public static final String YICHANG_LX2_str12 = "用户中心%1下零件%2的以下参数有误:%3";//（%3=发运周期）;
	
	public static final String YICHANG_LX2_str13 =  "用户中心%1下零件%2（供应商%3+目的地%4）的以下参数有误:%5,";//%5=路径代码;
	
	public static final String YICHANG_LX2_str14 = "物流路径：%1模式的循环时间有误：用户中心%2，零件号%3，供应商编号%4，分配循环%5";
	
	public static final String YICHANG_LX2_str15 =  "用户中心%1下零件%2的以下参数有误:%3";//（%3=制造路线）
	
	public static final String YICHANG_LX2_str16 = "用户中心%1下零件%2的以下参数有误:%3";//（%3=零件单位）
	
	public static final String YICHANG_LX2_str17 =  "在供货模式%1下，零件%2-客户%3：初始化资源表为空,生产线编号%4,消耗点%5";
	
	public static final String YICHANG_LX2_str18 =  "用户中心%1下零件%2的以下参数有误:日历版次为空,计算日期%3,仓库代码/产线%4";
	
	public static final String YICHANG_LX2_str19 =  "用户中心%1下零件%2的以下参数有误:零件仓库查询为空,仓库代码%3";
	
	public static final String YICHANG_LX2_str20 = "在供货模式%1下，零件%2-客户%3：未查到相关小火车信息,小火车编号%4）";
	
	public static final String YICHANG_LX2_str21 = "日历错误：用户中心%1，产线/仓库%2在CKX_CALENDAR_GROUP表中的数据有误";
	
	public static final String YICHANG_LX2_str22 = "用户中心%1下零件%2+供应商%3的以下参数有误:%4,";
	
	public static final String YICHANG_LX2_str23 = "用户中心%1下零件%2的以下参数有误:%3（%3=供应商代码）";
	
	public static final String YICHANG_LX2_str24 = "在供货模式%1下，零件%2-客户%3：%4";
	
	public static final String YICHANG_LX2_str25 = "用户中心%1下零件%2的以下参数有误:零件编号查询为空,零件编号%3";
	
	public static final String YICHANG_LX2_str26 = "用户中心%1下零件%2的以下参数有误:计算日期为空,日历版次%3";
	
	public static final String YICHANG_LX2_str27 = "产线/仓库信息错误：物流路径总表，用户中心%1，零件号%2，产线/仓库%3";
	
	public static final String YICHANG_LX2_str28 = "%1模式的包装容量数据有误：CKX_LINGJGONGYS表用户中心%2，零件号%3，供应商编号%4";
	
	public static final String YICHANG_LX2_str29 = "订货路线不匹配：用户中心%1，零件号%2，毛需求订货路线%3，供应商的供货类型%4";
	
	public static final String YICHANG_LX2_str30 = "用户中心%1下零件%2没有订货仓库";
	
	public static final String YICHANG_LX2_str31 = "用户中心%1下所对应的日历版次%2中，当前时间到周期%3中结束日期%4的工作天数为空";
	
	public static final String YICHANG_LX2_str32 = "用户中心%1下所对应的日历版次%2中，拉箱界限周期%3中开始日期%4到结束日期%5的工作天数为空";

	public static final String YICHANG_LX2_str33 = "消耗比例为空：CKX_LINGJXHD表用户中心%2，零件号%3，消耗点编号%3";
	public static final String YICHANG_LX2_str34 = "用户中心%1下所对应的日历版次%2中，开始时间%3到结束日期%4的工作天数为零";
	public static final String YICHANG_LX2_str35 = "仓库UC包装信息错误：CKX_LINGJCK表，用户中心%1，零件号%2，线边仓库%3";
	public static final String YICHANG_LX2_str36 = "RD模式I类型备货时间有误：CKX_WULLJ表(将来模式取对应的仓库循环时间)，用户中心%1，零件号%2，分配循环%3，仓库%4";
	public static final String YICHANG_LX2_str37 = "RD模式P类型备货时间有误：CKX_WULLJ表(将来模式取对应的仓库循环时间)，用户中心%1，零件号%2，分配循环%3，仓库%4";

	public static final String YICHANG_LX2_str38 = "%1模式的送货频次数据有误：用户中心%2，零件号%3，供应商编号%4，分配循环%5";

	public static final String YICHANG_LX2_str39 = "供应商表备货周期为空：用户中心%1，供应商编号%2";
	public static final String YICHANG_LX2_str40 = "供应商表发运周期为空：用户中心%1，供应商编号%2";
	public static final String YICHANG_LX2_str41 = "包装容量为空：CKX_LINGJGONGYS表用户中心%1，零件号%2，供应商编号%3";
	public static final String YICHANG_LX2_str42 = "未找到零件对应的供应商：CKX_LINGJGONGYS表用户中心%1，零件号%2";
	public static final String YICHANG_LX2_str43 = "供应商份额为空：CKX_LINGJGONGYS表用户中心%1，零件号%2，供应商编号%3";
	public static final String YICHANG_LX2_str44 = "毛需求制造路线有误：CKX_MAOXQMX表用户中心%1，零件号%2";
	public static final String YICHANG_LX2_str45 = "用户中心%1下零件%2日历版次为空";
	public static final String YICHANG_LX2_str46 = "产线/仓库信息错误：物流路径总表，用户中心%1，零件号%2，产线/仓库为空";
	public static final String YICHANG_LX2_str47 = "参考系零件表：订货车间为空，用户中心%1，零件号%2";

	public static final String YICHANG_LX2_str50 = "用户中心%1下零件%2的以下参数有误:%3";//%3=订货车间
	public static final String YICHANG_LX2_str49 = "用户中心%1下零件%2的以下参数有误:%3";//%3=资源获取日期
	public static final String YICHANG_LX2_str48 = "用户中心%1下集装箱%2路径代码为空";
	public static final String YICHANG_LX2_str51 = "RD模式仓库送货时间为空：CKX_WULLJ表(将来模式取对应的仓库循环时间)，用户中心%1，零件号%2，分配循环%3，仓库%4";
	public static final String YICHANG_LX2_str52 = "RD模式仓库返回时间为空：CKX_WULLJ表(将来模式取对应的仓库循环时间)，用户中心%1，零件号%2，分配循环%3，仓库%4";
	public static final String YICHANG_LX2_str59 = "用户中心%1下零件%2+供应商%3的以下参数有误:%4";
	public static final String YICHANG_LX2_str60 = "用户中心%1下零件%2的第一启运时间%3在p0周期序号之后";
	
	public static final String YICHANG_LX2_str61 = "物流路径(模式取对应的仓库循环时间)：%1模式的仓库循环时间有误：用户中心%2，零件号%3，订货仓库%4，线边仓库%5";
	public static final String YICHANG_LX2_str62 = "%1模式的送货频次数据有误：用户中心%2，零件号%3，订货仓库%4，分配循环%5";
	
	
	// 0006969 RM看板计算时验证订货仓库与工作时间的关联
	public static final String YICHANG_LX2_str63 = "用户中心%1,零件%2,线边仓库%3,RM模式下,订货仓库%4未配置工作时间编组";
	
	// 0006969 RD看板计算时验证线边仓库与工作时间的关联
	public static final String YICHANG_LX2_str64 = "用户中心%1,零件%2,消耗点编号%3,RD模式下,线边仓库%4未配置工作时间编组";
	
	public static final String YICHANG_LX2_str65 = " 用户中心：%1下零件编号为：%2在毛需求中订货路线为：%3  未找到订货路线与其匹配的零件供应商，不予计算!";
	
	public static final String YICHANG_LX2_str66 = "%1模式的年型码为空：用户中心%2，供应商号%3，抛出异常";
	/**
	 * 资源跟踪报警
	 */
	public static final String YICHANG_LX2_str53 = "%1零件消耗点分配区仓库为空,外部模式:%2,产线:%3,分配循环:%4";
	public static final String YICHANG_LX2_str54 = "用户中心%1零件有以下参数错误:%2零件号不存在";
	public static final String YICHANG_LX2_str55 = "用户中心%1零件仓库有以下参数错误:%2零件号不存在";
	public static final String YICHANG_LX2_str56 = "日历版次为空,计算日期为%1仓库代码为%2";
	public static final String YICHANG_LX2_str57 = "日历版次为空,计算日期为%1产线为%2";
	public static final String YICHANG_LX2_str58 = "计算日期为空,计算日期为%1日历版次为%2";
	/**
	 * 异常报警类型 lx3（计算逻辑错误）
	 */
	public static final String YICHANG_LX3= "300";
	
	public static final String YICHANG_LX3_str1 = "柔性比例错误：用户中心%1下零件%2的要货数量小于或者大于规定的柔性比例";
	
	public static final String YICHANG_LX3_str2 = "用户中心%1下零件%2的第一启运时间%3在p0周期序号之后";
	
	public static final String YICHANG_LX3_str3 = "用户中心%1下零件%2在周期%3的工作日为0天";
	
	public static final String YICHANG_LX3_str4 = "在中心日历中没有用户中心%1和日期%2对应的周序";
	
	public static final String YICHANG_LX3_str5 = "柔性比例错误：用户中心%1下零件%2的要货数量超过最大要货数量";
	
	public static final String YICHANG_LX3_str6 =  "柔性比例错误：订单%1找不到上一周期订单";
	
	public static final String YICHANG_LX3_str7 = "工作天数为零：用户中心%1，零件号%2，产线%3";
	
	public static final String YICHANG_LX3_str8 = "用户中心%1下资源获取日期为%2零件%3在仓库%4下存在重复数据";
	
	public static final String YICHANG_LX3_str9 = "用户中心%1下供应商为%2编号为%3的零件的备货周期及发运周期过长";
	
	public static final String YICHANG_LX3_str10 = "柔性比例错误：用户中心%1下零件%2周期%3的要货数量超过最大要货数量";
	
	/**
	 * 异常报警类型   lx4 供应商份额不足100%
	 */
	public  static final  String  YICHANG_LX4="400";
	
	public static final String YICHANG_LX4_str1 = "用户中心%1下零件%2的供应商份额不为100%";
	
	/**
	 * 异常报警类型 lx5 数据插入错误
	 */
	public static final String YICHANG_LX5 = "500";
	
	/**
	 * 异常报警类型 lx6 消耗比例不为100%
	 */
	public static final String YICHANG_LX6 = "600";
	/**
	 * 异常报警类型 lx6 消耗比例不为100% 产线消耗比例
	 */
	public static final String YICHANG_LX6_str1 = "用户中心%1下零件%2在产线%3上消耗比例和不为100%";
	
	public static final String YICHANG_LX6_str2 = "用户中心%1下零件%2在产线%3的循环%4上消耗比例和不为100%";
	
	
	public static final String POAcode = "POA";
	
	
	
	
	/**
	 * 异常报警计算模块：kd件目录比对
	 */
	public static final String JISUANMOKUAI_MULUBIDUI = "kd件目录比对";

	
	/**
	 * sheet列数：第一列
	 */

    public static final int SHEETCELL1 = 0;
    /**
	 * sheet列数：第二列
	 */

    public static final int SHEETCELL2 = 1;
    /**
	 * sheet列数：第三列
	 */

    public static final int SHEETCELL3 = 2;
    /**
	 * sheet列数：第四列
	 */

    public static final int SHEETCELL4 = 3;
    /**
	 * sheet列数：第五列
	 */

    public static final int SHEETCELL5 = 4;
    /**
	 * sheet列数：第六列
	 */

    public static final int SHEETCELL6 = 5;
    /**
	 * sheet列数：第七列
	 */

    public static final int SHEETCELL7 = 6;
    /**
	 * sheet列数：第八列
	 */

    public static final int SHEETCELL8 = 7;
    /**
	 * sheet列数：第九列
	 */

    public static final int SHEETCELL9 = 8;
    /**
	 * sheet列数：第二十七列
	 */

    public static final int SHEETCELL27 = 27;
    /**
	 * sheet列数：第二十八列
	 */

    public static final int SHEETCELL28 = 28;
    /**
	 * sheet列数：第二十九列
	 */

    public static final int SHEETCELL29 = 29;
    /**
	 * 目录比对：插入数据错误
	 */

    public static final String MULUBIDUI_INSERTERRO = "插入数据库时产生错误！！请检查异常报警模块";
    /**
	 * 目录比对：导入成功
	 */

    public static final String MULUBIDUI_INSERTSUCCESS = "数据导入成功！";
	
	/**
	 * 目录比对：导入sheet名称
	 */

    public static final String MULUBIDUI_SHEETMING = "CATALOGUE DU MOIS A VENIR";
	/**
	 * 零件周期供货模式：国产普通零件
	 */

    public static final String ZHOUQIGONGHUOMOSHI_IL_PUTONG = "A";
    
    /**
	 * 零件周期供货模式：国产卷料零件
	 */
    public static final String ZHOUQIGONGHUOMOSHI_IL_JUANLIAO = "UGB";
    /**
	 * 零件周期供货模式：国产辅料零件
	 */
    public static final String ZHOUQIGONGHUOMOSHI_IL_FULIAO = "M";
    
    /**

	 * 依赖参考系参数：国产订单计算
	 */
	public static final String YiLAI = "1";
	 /**

	 * 不依赖参考系参数：国产订单计算
	 */
	public static final String BUYiLAI = "0";

	/**
	 * 国产件订单内容：标示是否是既定
	 */
	public static final String SHIFOUSHIJIDING = "9";
	
	/**
	 * 国产件订单内容：标示是否是预告
	 */

	public static final String SHIFOUSHIYUGAO = "8";


    /**
	 * 国产件订单内容：订单内容长度
	 */
    public static final int DINGDANNEIRONGCHANGDU = 4;
    
    /**
	 * KD件订单内容：订单内容长度
	 */
    public static final int KDDINGDANNEIRONGCHANGDU = 5;
    
    /**
	 * 中心日历：周期内第一周
	 */
    public static final String ZHOUQIDIYIZHOU = "01";
    /**
	 * KD订单待生效状态
	 */
    public static final String KDDINGDANDAISHENGXIAO = "2";
    
    /**
	 * KD订单制作中状态
	 */
    public static final String KDDINGDANZHIZUOZHONG = "1";
   
    
    
    //李明
    
    /**
     *是关键零件
     */
    public static final String KY = "1" ;   
    
    /**
     *不是关键零件
     */
    public static final String KN = "0" ;   
    
    /**
     *S
     */
    public static final String S = "s" ;    
    
    /**
     *getP
     */
    public static final String GETP = "getP" ;    
    
    /**
     *getS
     */
    public static final String GETS = "getS" ;    
    
    /**
     *getJ
     */
    public static final String GETJ = "getJ" ;    
    
    /**
     * 非空
     */
    public static final BigDecimal NOTNULL = new BigDecimal("0.1") ;
    
    /**
     * 第一周的周序
     */
    public static final String FIRSTZX = "01";
    
    /**
	 * 周期类型：pp
	 */
	public static final String PP = "PP";
	public static final String NP = "NP";
	/**
	 * 周期类型：ps
	 */
	public static final String PS = "PS";
	public static final String NS = "NS";
	/**
	 * 周期类型：pj
	 */
	public static final String PJ = "PJ";
	public static final String NJ = "NJ";
	/**
	 * 周期类型：C
	 */
	public static final String CHULLX_C = "C";
	
	/**
	 * 供货类型：c
	 */
	public static final String GONGHLX_C = "C";

	/**
	 * 是否工作日：GZR_Y
	 */
	public static final String GZR_Y = "1";
	
	/**
	 * 是否工作日：GZR_N
	 */
	public static final String GZR_N = "0";
	
	/**
	 * 是交付日：JIAOFR_Y
	 */
	public static final String JIAOFR_Y = "1";

	/**
	 * 不是交付日：JIAOFR_N
	 */
	public static final String JIAOFR_N = "0";
	
	/**
	 * 订单状态为A的时候表示已定义DINGD_STATUS_JSZ
	 */
	public static final String DINGD_STATUS_JSZ = "A";
	public static final String DINGD_STATUS_JSZ_NAME = "计算中";
	
	/**
	 * 订单状态为0的时候表示已定义DINGD_STATUS_YDY
	 */
	public static final String DINGD_STATUS_YDY = "0";
	public static final String DINGD_STATUS_YDY_NAME = "已定义";
	/**
	 * 订单状态为制作中
	 */
	public static final String DINGD_STATUS_ZZZ = "1";
	public static final String DINGD_STATUS_ZZZ_NAME = "制作中";
	/**
	 * 订单状态为待生效
	 */
	public static final String DINGD_STATUS_DSX = "2";
	public static final String DINGD_STATUS_DSX_NAME = "待生效";
	/**
	 * 订单状态为拒绝
	 */
	public static final String DINGD_STATUS_JUJ = "3";
	public static final String DINGD_STATUS_JUJ_NAME = "拒绝";
	/**
	 * 订单状态为已生效
	 */
	public static final String DINGD_STATUS_YSX = "4";
	public static final String DINGD_STATUS_YSX_NAME = "已生效";
	/**
	 * 订单状态为已发送
	 */
	public static final String DINGD_STATUS_YFS = "5";
	public static final String DINGD_STATUS_YFS_NAME = "已发送";
	/**
	 * 订单状态为执行中
	 */
	public static final String DINGD_STATUS_ZXZ = "7";
	public static final String DINGD_STATUS_ZXZ_NAME = "执行中";
	/**
	 * KD删除状态成功
	 */
	public static final String DELETE_SUCSS = "删除成功";
	
	/**
	 * KD删除状态失败
	 */
	public static final String DELETE_FAIL = "删除失败，只能删除已定义的订单";
	
	/**
	 * KD修改状态成功
	 */
	public static final String UPDATE_SUCSS = "修改成功";
	
	/**
	 * KD修改状态失败
	 */
	public static final String UPDATE_FAIL = "修改失败";
	

	/**
	 * KD插入成功
	 */
	public static final String INSERT_SUCSS = "插入成功";
	
	/**
	 * KD插入失败
	 */
	public static final String INSERT_FAIL= "插入失败";

	/**
	 * 计算模块代码：毛需求
	 */
	public static final String JISMK_MAOXQ_CD = "10";
	
	/**
	 * 计算模块代码:毛需求报表
	 */
	public static final String JISMK_MAOXQ_REP = "11";
	/**
	 * 计算模块代码：PPL预告
	 */
	public static final String JISMK_PPL_CD = "20";
	/**
	 * 计算模块代码：IL订单
	 */
	public static final String JISMK_IL_CD = "31";
	/////wuyichao 2014-10-31 导入国产件临时订单/////////
	public static final String JISMK_IL_TEMP_CD = "30";
	/////wuyichao 2014-10-31 导入国产件临时订单/////////
	
	/**
	 * 计算模块代码：KD订单
	 */
	public static final String JISMK_KD_CD = "32";

	/**
	 * 计算模块代码：按需订单计算
	 */
	public static final String JISMK_ANX_CD = "33";	
	
	/**
	 * 计算模块代码：新按需订单计算
	 */
	public static final String JISMK_XANX_CD = "35";	
	
		
	/**
	 * 计算模块代码：要货令管理
	 */
	public static final String JISMK_YAOHL_CD = "41";
	/**
	 * 计算模块代码：看板要货令
	 */
	public static final String JISMK_kANB_CD = "42";
	/**
	 * 计算模块代码：跟踪报警
	 */
	public static final String JISMK_GZBJ_CD = "50";
	/**
	 * 计算模块代码：跟踪报警MAF库毛需求对比
	 */
	public static final String JISMK_GZBJ_MAF = "51";
	/**
	 * 计算模块代码：拉箱计算
	 */
	public static final String JISMK_LAX_CD = "61";
	/**
	 * 计算模块代码：开箱计算
	 */
	public static final String JISMK_KAIX_CD = "62";
	/**
	 * 计算模块代码：零件在途查询
	 */
	public static final String JISMK_LINGJCX_CD = "70";
	/**
	 * 计算模块代码：收货处理
	 */
	public static final String JISMK_SHDCL_CD = "80";
	/**
	 * 计算模块代码：调拨令
	 */
	public static final String JISMK_DIAOBL_CD = "90";
	/**
	 * 计算模块代码：异常报警查询
	 */
	public static final String JISMK_YICBJ_CD = "99";
	
	/**
	 * 计算模块代码：库存报警IL计算
	 */
	public static final String JISMK_ILKCJS_CD = "35";
	
	/**
	 * 计算模块代码：库存报警KD计算
	 */
	public static final String JISMK_KDKCJS_CD = "36";
	
	/**
	 * 计算模块代码：库存报警(kd件15天报警)计算
	 */
	public static final String JISMK_BJKCJS_CD = "37";
	
	/**
	 * 错误类型：200（参数缺失）
	 */
	public static final String CUOWLX_200= "200";
	
	/**
	 *月：30
	 */
	public static final String MONTH_30= "30";
	/**
	 * 周：7
	 */
	public static final String WEEK_7= "7";
	
	
	
	/**
	 * KD模块毛需求错误
	 */
	public static final String MXQ_ERROR= "毛需求错误";
	
	/**
	 * KD模块代码份额不足100
	 */
	public static final String FENE_ERROR= "份额不足100";
	
	/**
	 * KD参数为空conditions
	 */
	public static final String CONDITIONS_NULL= "参数为空";
	
	/**
	 * 日期为1月1日
	 */
	public static final String RIQI_01_01= "01_01";
	
	/**
	 * 发运周期不存在
	 */
	public static final String FA_YUN_ZHOU= "发运周期不存在";
	/**
	 * 第一次启运时间不在发运周期之内
	 */
	public static final String FAYUNZHOU_WRONG= "第一次启运时间不在发运周期之内";
	
	
	/**
	 * 订货仓库
	 */
	public static final String DINGHCK= "1";
	
	/**
	 * 年
	 */
	public static final String YEARY= "yyyy";
	
	/**
	 * 月
	 */
	public static final String MONTH= "MM";
	
	/**
	 * 日
	 */
	public static final String DAY= "dd";
	
	/**
	 * 流水号0
	 */
	public static final String LSH_0 = "0000" ;
	
	/**
	 * 流水号1
	 */
	public static final String LSH_1 = "0001" ;
	
	
	/**
	 * P
	 */
	public static final String PM= "PP";
	
	/**
	 * J
	 */
	public static final String JM= "PJ";
	
	/**
	 * S
	 */
	public static final String SM= "PS";
	
	
	
	// 夏晖

	/**
	 * 画面参数设置：零件类型：零件类型
	 */
	public static final String PARAMS_PPL_LINGJLEIXING = "LX";
	/**
	 * 零件类型：KD
	 */
	public static final String LINGJIAN_LX_KD = "KD";
	/**
	 * 零件类型：IL
	 */
	public static final String LINGJIAN_LX_IL = "IL";
	/**
	 * 画面参数设置：PPL年度明细：比较两个版次 版次1
	 */
	public static final String PPL_NIANDYGMX_BC1 = "PPLBC1";
	/**
	 * 画面参数设置：PPL年度明细：比较两个版次 版次2
	 */
	public static final String PPL_NIANDYGMX_BC2 = "PPLBC2";
	/**
	 * 画面参数设置：毛需求版次：
	 */
	public static final String MAOXQ_BC = "XUQBC";
	/**
	 * 画面参数设置：年度预告：计算年份
	 */
	public static final String NAINDYG_JISNF = "JISNF";
	/**
	 * 画面参数设置：年度预告：计算时间
	 */
	public static final String NAINDYG_JISSJ = "JISSJ";
	/**
	 * 设置年度预告的状态 01 代表生效
	 */
	public static final Integer NIANDYG_ZHUANGT_SX = 1;
	/**
	 * 设置年度预告的状态 -1 代表已删除
	 */
	public static final Integer NIANDYG_ZHANGT_SC = -1;
	/**
	 * 设置年度预告的状态00 代表未生效
	 */
	public static final Integer NIANDYG_ZHANGT_WSX = 0;
	
	/**
	 * 9位空格
	 */
	public static final String SPACE_9 = "         ";
	/**
	 * 15位空格
	 */
	public static final String SPACE_15 = "               ";
	/**
	 * 12位空格
	 */
	public static final String SPACE_12 = "            ";
	/**
	 * 2位空格
	 */
	public static final String SPACE_2 = "  ";
	/**
	 * 4位空格
	 */
	public static final String SPACE_4 = "    ";
	/**
	 * 3位空格
	 */
	public static final String SPACE_3 = "   ";
	/**
	 * 要货令管理 时间
	 */
	/**
	 * 预计发出
	 */
	public  static   final String  SJ_1="0";
	/**
	 * 实际发出
	 */
	public  static   final String  SJ_2="1";
	/**
	 * 预计交付
	 */
	public  static   final String  SJ_3="2";
	/**
	 * 实际交付
	 */
	public  static   final String  SJ_4="3";
	/**
	 * 看板循环管理 导入sheet名称
	 */
	   public static final String KANBXH_ZUIDYHL = "KBZDYHL";
	/**
	 * 看板循环 导入最大值  
	 */
	 public static final String KANBXH_DATAERRO = "插入数据库时产生错误！请检查execl表中数据！";
	 /**
	  * 要货令状态   要要货令终止  --05
	  */
	 public static final String YAOHL_ZHONGZ = "05";
	 
	 /**
	  * 要货令状态   要要货令表达  --01
	  */
	 public static final String YAOHL_BIAOD = "01";
	 
	 /**
	  * 要货令状态   要要货令延迟交付  --04
	  */
	 public static final String YAOHL_YANCJF = "04";
	 /**
	  *  看板循环规模管理 生效状态标识  失效  -1
	  */
	 public static final String KANBXH_SHIXIAO = "-1";
	 /**
	  *  看板循环规模管理 生效状态标识  未生效  "0"
	  */
	 public static final String KANBXH_WEISX = "0";
	 /**
	  *  看板循环规模管理 生效状态标识  生效  "1"
	  */
	 public static final String KANBXH_SHENGX = "1";
	 /**
	  *  看板循环规模管理 冻结状态标识 已冻结 "0"
	  */
	 public static final String KANBXH_YIDONGJ = "0";
	 /**
	  *  看板循环规模管理 冻结状态标识 未冻结  "1"
	  */
	 public static final String KANBXH_WEIDONGJ = "1";
	 /**
	  * 计算模块     ppl计算
	  */
	 public  static  final   String  JSMK_PPL="20";
	// 聂士元
	/**
	 * 零件制造路线：调拨令_未审核
	 */
	public static final String DIAOBL_ZT_APPLYING = "00";
	/**
	 * 零件制造路线：调拨令_审核中
	 */
	public static final String DIAOBL_ZT_APPROVING = "10";
	/**
	 * 零件制造路线：调拨令_同意
	 */
	public static final String DIAOBL_ZT_APPROVED = "14";
	/**
	 * 零件制造路线：调拨令_拒绝
	 */
	public static final String DIAOBL_ZT_REJECTED = "18";
	/**
	 * 零件制造路线：调拨令_已审核
	 */
	public static final String DIAOBL_ZT_PASSED = "20";
	/**
	 * 零件制造路线：调拨令_取消
	 */
	public static final String DIAOBL_ZT_cancle = "22";
	/**
	 * 零件制造路线：调拨令_已生效
	 */
	public static final String DIAOBL_ZT_EFFECT = "30";
	/**
	 * 零件制造路线：调拨令_执行中
	 */
	public static final String DIAOBL_ZT_EXCUTING = "40";
	/**
	 * 零件制造路线：调拨令_已执行
	 */
	public static final String DIAOBL_ZT_EXCUTED = "50";
	/**
	 * 零件制造路线：调拨令_终止
	 */
	public static final String DIAOBL_ZT_STOPPED = "60";
	
	/**
	 * 看板循环规模计算:外部模式R1
	 */
    public static final String KANB_JS_WAIBMOS_R1 = "R1";
    
    /**
	 * 看板循环规模计算:外部模式R2
	 */
    public static final String KANB_JS_WAIBMOS_R2 = "R2";
    
    /**
	 * 看板循环规模计算:内部模式RD
	 */
    public static final String KANB_JS_NEIBMOS_RD = "RD";
    
    /**
	 * 看板循环规模计算:内部模式RM
	 */
    public static final String KANB_JS_NEIBMOS_RM = "RM";
    
    /**
	 * 看板循环规模计算:毛需求类型为日
	 */
    public static final String KANB_MXQLX_CLV = "CLV";
    
    /**
	 * 看板循环规模计算:毛需求类型为周期
	 */
    public static final String KANB_MXQLX_NUP = "NUP";
    
    /**
	 * 计算参数处理模块代码：42（看板要货令）
	 */
    public static String  KANB_MKDM = "42";
    
    /**
	 * 计算参数处理：处理状态：10-未开始   
	 */
    public static String  JSZT_UNBEGIN = "10";
    
    /**
	 * 计算参数处理：处理状态： 20-执行中
	 */
    public static String  JSZT_EXECUTING = "20";
    
    /**
	 * 计算参数处理：处理状态：90-正常结束
	 */
    public static String  JSZT_SURE = "90";
    
    /**
	 * 计算参数处理：处理状态： 99-异常结束
	 */
    public static String  JSZT_EXECPTION = "99";

	/************************** 参数校验：异常报警类型 *************************************/

	/**
	 * 异常报警错误类型：中心日历数据有误：420
	 */
	public static String KANB_CALENDAR_ERROR = "420";
	/**
	 * 异常报警错误类型：零件表数据有误：421
	 */
	public static String KANB_LINGJ_ERROR = "421";
	/**
	 * 异常报警错误类型：零件仓库数据有误：422
	 */
	public static String KANB_LINGJCK_ERROR = "423";
	/**
	 * 异常报警错误类型：物流路径总图数据有误：423
	 */
	public static String KANB_WULLJ_ERROR = "423";
	/**
	 * 异常报警错误类型：消耗点数据有误：424
	 */
	public static String KANB_XIAOHD_ERROR = "424";
	/**
	 * 异常报警错误类型：零件供应商数据有误：400
	 */
	public static String KANB_LINGJGONGYS_ERROR = "400";
	public static String KANB_XIAOHDBL_ERROR = "600";

	/******************************************* END ******************************************/
    /**
	 * 毛需求查询与比较：需求来源：IL周期毛需求
	 */
    public static String  MAOXQ_XUQLY_DIP = "DIP";
    
    /**
	 * 毛需求查询与比较：需求来源：IL周毛需求
	 */
    public static String  MAOXQ_XUQLY_DIS = "DIS";
    
    /**
	 * 毛需求查询与比较：需求来源：KD周毛需求
	 */
    public static String  MAOXQ_XUQLY_DKS = "DKS";
    /**
     * 需求来源：KD周毛需求名称
     */
    public static String  MAOXQ_XUQLY_DKS_NAME = "KD周毛需求";    
    /**
	 * 毛需求查询与比较：需求来源：CLV日滚动毛需求
	 */
    public static String  MAOXQ_XUQLY_CLV = "CLV";
    
    /**
	 * 毛需求查询与比较：需求来源：备件毛需求周期
	 */
    public static String  MAOXQ_XUQLY_BJP = "BJP";
    
    /**
	 * 毛需求查询与比较：需求来源：外销周期毛需求
	 */
    public static String  MAOXQ_XUQLY_WXP = "WXP";
    
    /**
	 * 毛需求查询与比较：需求来源：总成毛需求（日滚动）
	 */
    public static String  MAOXQ_XUQLY_ZCJ = "ZCJ";
    
    /**
	 * 毛需求查询与比较：需求来源：冲压排产（日）
	 */
    public static String  MAOXQ_XUQLY_CYJ = "CYJ";
    
    /**
	 * 毛需求查询与比较：需求来源：冲压排产（周期）
	 */
    public static String  MAOXQ_XUQLY_CYP = "CYP";
    /**
     * CJT
     */
    public static String MAOXQ_XUQLY_CJT = "CJT";
    /**
     * CSX
     */
    public static String MAOXQ_XUQLY_CSX = "CSX";
    /**
	 * 毛需求查询与比较：需求类型：周期
	 */
    public static String  MAOXQ_XUQLX_CYC = "周期";
    
    /**
	 * 毛需求查询与比较：需求类型：周
	 */
    public static String  MAOXQ_XUQLX_WEEK = "周";
    
    /**
	 * 毛需求查询与比较：需求类型：日
	 */
    public static String  MAOXQ_XUQLX_DAYS = "日";
    
    /**
	 * 角色权限：POA：计划室
	 */
	public static String QUANX_POA = "ZBCPOA";
    
    /**
	 * 角色权限：POA：仓库物流
	 */
    public static String  QUANX_POA_CANGK = "ZXCPOA";
    
    /**
	 * 字符：true
	 */
    public static String  FLAG_TRUE = "true";
    
    /**
	 * 字符：false
	 */
    public static String  FLAG_FALSE = "false";
    
    /**
	 * 字符串：jsDate
	 */
    public static String  JSDATE = "jsDate";
    
    /**
	 * 字符串：jsEnd
	 */
    public static String  JSEND = "jsEnd";
    
    /**
	 * 字符串：xuqsszq
	 */
    public static String  XUQSSZQ = "xuqsszq";
    
    /**
	 * 导入功能模块名：ppl
	 */
    public static String  FILE_UP_PPL= "ppl";
    
    /**
	 * 导入功能模块名：maoxq
	 */
    public static String  FILE_UP_MAOXQ= "maoxq";
    
    /**
	 * 导入功能模块名：kd目录比对
	 */
    public static String  FILE_UP_KDBD= "kdmlbd";
    
    /**
	 * 导入功能模块名：看板要货令 :导入最大要货量
	 */
    public static String  FILE_UP_DRZDYHL= "drzdyhl";
    
    /**
     * -999999999表示空数
     */
    public static BigDecimal nullBigDecimal = new BigDecimal(-999999999);
    
    //李智
    /**
     * 订单类型:kd订单
     */
    public static String  DINGD_LX_KD= "1";
    
    /**
     * 订单类型:特殊订单
     */
    public static String  DINGD_LX_TS= "4";
    
    /**
     * 订单类型:爱信订单
     */
    public static String  DINGD_LX_AX= "2";
    
    /**
     * 订单类型:il
     */
    public static String  DINGD_LX_IL= "0";
    
    /**
     * 订单类型：正常_按需C
     */
    public static String  DINGD_LX_ANX_ZC_C= "5";
    /**
     * 订单类型：正常_按需M
     */
    public static String  DINGD_LX_ANX_ZC_M= "6";
    /**
     * 订单类型：初始化_按需C
     */
    public static String  DINGD_LX_ANX_CSH_C= "7";
    /**
     * 订单类型：初始化_按需M
     */
    public static String  DINGD_LX_ANX_CSH_M= "8";
    
    /**
     * 不为临时订单使用
     */
    public static String SHIFFSGYS_NO = "3";
    
    /**
     * 为临时订单使用
     */
    public static String SHIFFSGYS_YES = "1";
    
    /**
     * 是否只发送要货令  不发送
     */
    public static String SHIFZFSYHL_NO = "0";
    /**
     * 是否只发送要货令  发送
     */
    public static String SHIFZFSYHL_YES = "1";
    
    /**
     * 时间格式时分秒:yyyy-MM-dd HH:mm:ss
     */
    public static String TIME_FORMAT_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 时间格式年月日:yyyyMMddHHmm
     */
    public static String TIME_FORMAT_yyyyMMddHHmm = "yyyyMMddHHmm";
    
    /**
     * 时间格式年月日:yyyy-MM-dd
     */
    public static String TIME_FORMAT_YY_MM_DD = "yyyy-MM-dd";
    
    /**
     * 年度预告的PPL类型 KD
     */
    public static String NIANDYG_PPLLX_KD = "KD";
    /**
     * 年度预告的PPL类型 IL
     */
    public static String NIANDYG_PPLLX_IL = "IL";
    //end
    
	/**
	 * 毛需求：需求来源-中文
	 */
	public final static String XITCSDY_XQLY_CHN = "XQLY";
	/**
	 * 毛需求：需求来源-英文
	 */
	public final static String XITCSDY_XUQLY_ENG = "XUQLY";
	
	/**
	 * wtc返回码0000代表成功
	 */
	public final static String WTC_SUSSCESS = "0000";
    
	public final static String WTC_DIAOLZZLJ = "DIAOBLZZLJ";
	public final static String WTC_DIAOLZZCK = "DIAOBLZZLJCK";

	public final static String WTC_CENTER_UW = "UW";
	public final static String WTC_CENTER_UL = "UL";
	public final static String WTC_CENTER_UX = "UX";
	public final static String WTC_CENTER_VD = "VD";
	
	public final static String ERROR_GETZIYHQRQ = "资源获取日期失败!";
	
	/**
	 * 事务提醒类型新零件-1
	 */
	public final static String SWTXLX_NEWLJ = "1";
	
	/**
	 * 事务提醒类型新供应商-2
	 */
	public final static String SWTXLX_NEWGYS = "2";
	
	/**
	 * 事务提醒类型新零件供应商-3
	 */
	public final static String SWTXLX_NEWLJGYS = "3";
	
	/**
	 * 事务提醒类型新消耗点-4
	 */
	public final static String SWTXLX_NEWXHD = "4";
	
	/**
	 * 事务提醒类型新零件消耗点-5
	 */
	public final static String SWTXLX_NEWLJXHD = "5";
	
	/**
	 * 事务提醒类型失效零件消耗点-7 hzg 2015.5.21
	 */
	public final static String SWTXLX_SHIXLJXHD = "7";
	
	/**
	 * 事务提醒类型新零件提醒内容
	 */
	public final static String SWTXLX_NEWLJNR = "请维护零件相关信息";
	
	/**
	 * 事务提醒类型新供应商提醒内容
	 */
	public final static String SWTXLX_NEWGYSNR = "请维护供应商信息及外部物流";
	
	/**
	 * 事务提醒类型新零件供应商提醒内容
	 */
	public final static String SWTXLX_NEWLJGYSNR = "请维护零件-供应商信息,包装及外部物流";
	
	/**
	 * 事务提醒类型新消耗点提醒内容
	 */
	public final static String SWTXLX_NEWXHDNR = "请维护消耗点信息";
	
	/**
	 * 事务提醒类型新零件消耗点提醒内容
	 */
	public final static String SWTXLX_NEWLJXHDNR = "请维护零件-消耗点信息";
	
	/**
	 * 按需UW订货单号
	 */
	public final static String ANX_UW_DINGDH = "C1DDD0009";
	
	/**
	 * 按需UL订单号
	 */
	public final static String ANX_UL_DINGDH = "C1DDD0008";
	
	/**
	 * 按需VD订单号
	 */
	public final static String ANX_VD_DINGDH = "C1DDD0007";
	
	
	/**
	 * 周期类型：vj
	 */
	public static final String VJ = "VJ";
	
	/**
	 * 分装线排产：上线
	 */
	public static final String FENZXPC_ONLINE = "S";
	
	/**
	 * 分装线排产：下线
	 */
	public static final String FENZXPC_OFFLINE = "X";
	
	/**
	 * 计算模块代码：用户中心为UL的VJ计算
	 */
	public static final String VJJS_UL = "38";
	
	/**
	 * 计算模块代码：用户中心为UW的VJ计算
	 */
	public static final String VJJS_UW = "39";
	
	/**
	 * 计算模块代码：用户中心为UD的VJ计算
	 */
	public static final String VJJS_VD = "47";
	/**
	 * 计算模块代码：用户中心为UX的VJ计算
	 */
	public static final String VJJS_UX = "43";
	
	
	/**
	 * 计算模块代码：用户中心为UL的MJ计算
	 */
	public static final String MJJS_UL = "44";
	
	/**
	 * 计算模块代码：用户中心为UW的MJ计算
	 */
	public static final String MJJS_UW = "45";
	
	/**
	 * 计算模块代码：用户中心为UX的MJ计算
	 */
	public static final String MJJS_UX = "46";
	
	
	/**
	 * 周期类型：mj
	 */
	public static final String MJ = "MJ";
	/**
	 * 按需模式：mv
	 */
	public static final String MV = "MV";
	
	//xss_周期毛需求比较_0012943
	public static final String MXQBJ_UW = "51";
	public static final String MXQBJ_UL = "52";
	public static final String MXQBJ_UX = "53";
	public static final String MXQBJ_VD = "54";
	
}
