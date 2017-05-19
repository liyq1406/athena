package com.athena.component.input;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;



import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

/**
 * 订单信息接口输入类
 * @author GJ
 *
 */
public class DdxxDbDataWriter extends DbDataWriter {

	protected static Logger logger = Logger.getLogger(DdxxDbDataWriter.class);	//定义日志方法
	public DdxxDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	/**
	 * 解析数据之前清空订单信息表数据
	 */
	@Override
	public boolean before() {
        try {
    		String sql="delete from "+SpaceFinal.spacename_ckx+".in_ddxx";
			super.execute(sql);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
		return super.before();
	}
	
	
	@Override
	public boolean beforeRecord(int rowIndex, Object line) {
	   String rowStr=line.toString().substring(0,3);
	   if(rowIndex==1){ //过滤掉第一行
    	   return false;
       }
       if(rowStr.indexOf("END")!=-1){ //行数据中含END的过滤掉
		   return false;
	   }
       else{
    	   return true;
       }
 }
	

	/**
	 * 行解析之后处理方法
	 * @param rowIndex 行标
	 * @param record 行数据集合
	 * @author GJ
	 */
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{ 
			if(!record.isEmpty()){
				String shul=record.getString("shul");
				int shul_substr=Integer.parseInt(shul.substring(0, 10).trim());
				record.put("shul", shul_substr);

				String usercenter = record.getString("usercenter"); //用户中心
				String gysdm = record.getString("gysdm");  //供应商代码
				String lingjbh =  record.getString("lingjbh");
				String ckdm = record.getString("ckdm");    //仓库代码

				//从供应商参考系表中获取供应商类型存入集合
				String leix = this.queryLeixOfGongys(usercenter, gysdm);
				record.put("leix", leix);

				//从零件仓库设置参考系表中获取卸货站台编号存入集合
				String xiehztbh = this.queryXiehztbhOfLingjck(usercenter, lingjbh, ckdm);
				record.put("xiehztbh", xiehztbh);

				//从零件表中获得‘订单车间’存到接口表中
				String dinghcj = this.queryDinghcjOfLingj(usercenter, lingjbh);
				record.put("dhcj", dinghcj);			

				//从零件-供应商参考系表中获取UA型号，UC型号，UA中UC容量，UC中零件容量
				Map<String,String> map = this.queryUAOfLingjgys(usercenter, lingjbh, gysdm);
				if(!map.isEmpty()){
					//获取UA型号存入集合
					record.put("uabzlx", map.get("uabzlx"));
					//获取UC型号存入集合
					record.put("ucbzlx", map.get("ucbzlx"));
					//获取UA中UC容量存入集合
					record.put("ucrl", map.get("ucrl")); 
					//获取UC中零件容量存入集合
					record.put("uaucgs", map.get("uaucgs"));
				}

				//获得要货起始日期
				String strYhqsDate=record.getString("yhqs_date").trim();
				if(!"".equals(strYhqsDate)){
					Date yhqs_date = this.dealYhqsrqStringToDate(strYhqsDate);
					record.put("yhqs_date", yhqs_date);//格式化的结果集存入表字段中
					record.put("jf_date", yhqs_date);//交付日期与要货起始日期一致
				}

				//获得要货结束日期
				String strYhjsDate=record.getString("yhjs_date").trim();
				if(!"".equals(strYhjsDate)){
					Date yhjs_date = this.dealYhjsrqStringToDate(strYhjsDate);
					record.put("yhjs_date", yhjs_date);//格式化的结果集存入表字段中
				}	

				//从外部物流参考系表中获取路径编号,如果存在则存入集合
				String lujbh = this.queryLujbhOfWaibwl(usercenter, ckdm, gysdm);
				record.put("lujbh", lujbh);

				//获得当前系统时间
				record.put("zhwhsj", new Date());
				//存入创建时间和处理状态初始值
				record.put("cj_date", new Date());
				record.put("clzt", 0);		 
			}
			super.afterRecord(rowIndex, record, line); 
		}catch(Exception e)
		{
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
	
	/**
	 * 根据’用户中心’，’供应商代码’从供应商参考系表中获取供应商类型
	 * @author 贺志国
	 * @date 2012-10-23
	 * @param usercenter 用户中心
	 * @param gysdm 供应商代码
	 * @return String 供应商类型
	 */
	@SuppressWarnings("unchecked")
	public String queryLeixOfGongys(String usercenter,String gysdm){
		StringBuffer sqlBuf=new StringBuffer();
		String leix = "";
		sqlBuf.append("select leix from "+SpaceFinal.spacename_ckx+".ckx_gongys t ");
		sqlBuf.append("where t.usercenter='").append(usercenter);
		sqlBuf.append("' and t.gcbh='").append(gysdm).append("'");
		Map<String,String> map=super.selectOne(sqlBuf.toString());
		if(!map.isEmpty()){
			leix = map.get("leix");
		}
		return leix;
	}
	
	/**
	 * 根据’用户中心’，’零件号’，’仓库代码’从零件仓库参考系表中获取卸货站台编号
	 * @author 贺志国
	 * @date 2012-10-23
	 * @param usercenter 用户中心
	 * @param lingjbh 零件编号
	 * @param ckdm 仓库代码
	 * @return String 卸货站台编号
	 */
	@SuppressWarnings("unchecked")
	public String queryXiehztbhOfLingjck(String usercenter,String lingjbh,String ckdm){
		StringBuffer sqlBuf = new StringBuffer();
		String xiehztbh = "";
		sqlBuf.append("select xiehztbh from "+SpaceFinal.spacename_ckx+".ckx_lingjck t ");
		sqlBuf.append("where t.usercenter='").append(usercenter);
		sqlBuf.append("' and t.lingjbh='").append(lingjbh);
		sqlBuf.append("' and t.cangkbh='").append(ckdm).append("'");	
		Map<String,String> map = super.selectOne(sqlBuf.toString());
		if(!map.isEmpty()){
			xiehztbh = map.get("xiehztbh");
		}
		return xiehztbh;
	}
	
	/**
	 * 根据’用户中心’,’零件编号’从零件表参考系中获取订货车间
	 * @author 贺志国
	 * @date 2012-10-23
	 * @param usercenter 用户中心
	 * @param lingjbh 零件编号
	 * @return String 订货车间
	 */
	@SuppressWarnings("unchecked")
	public String queryDinghcjOfLingj(String usercenter,String lingjbh){
		StringBuffer sqlBuf = new StringBuffer();
		String dinghcj = "";
		sqlBuf.append("select dinghcj from "+SpaceFinal.spacename_ckx+".ckx_lingj t ");
		sqlBuf.append("where t.usercenter='").append(usercenter);
		sqlBuf.append("' and t.lingjbh='").append(lingjbh).append("'");
		Map<String,String> map = super.selectOne(sqlBuf.toString());
		if(!map.isEmpty()){
			dinghcj = map.get("dinghcj");		
		}
		return dinghcj;
	}
	
	/**
	 * 根据’用户中心’，’零件号’，’供应商代码’从零件-供应商表中获取供应商UA包装类型、UC包装类型、UC容量、UA里UC的个数
	 * @author 贺志国
	 * @date 2012-10-23
	 * @param usercenter
	 * @param lingjbh
	 * @param gysdm
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> queryUAOfLingjgys(String usercenter,String lingjbh,String gysdm){
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select uabzlx,ucbzlx,ucrl,uaucgs ");
		sqlBuf.append("from "+SpaceFinal.spacename_ckx+".ckx_lingjgys t ");
		sqlBuf.append("where t.usercenter='").append(usercenter);
		sqlBuf.append("' and t.lingjbh='").append(lingjbh);
		sqlBuf.append("' and t.gongysbh='").append(gysdm).append("'");
		Map<String,String> map=super.selectOne(sqlBuf.toString());
		return map;
	}
	
	/**
	 * 根据’用户中心’,’目的地’,’供应商代码’从外部物流表中获取物流路径编号
	 * @author 贺志国
	 * @date 2012-10-23
	 * @param usercenter 用户中心
	 * @param xiehztbh 卸货站台编号   cankdm 仓库代码 hzg 2013-4-27
	 * @param gysdm 供应商代码
	 * @return String 物流路径编号
	 */
	@SuppressWarnings("unchecked")
	public String queryLujbhOfWaibwl(String usercenter,String cankdm,String gysdm){
		StringBuffer sqlBuf = new StringBuffer();
		String lujbh = "";
		sqlBuf.append("select lujbh from "+SpaceFinal.spacename_ckx+".ckx_waibwl t ");
		sqlBuf.append("where t.usercenter='").append(usercenter);
		if(!"".equals(cankdm)){
			sqlBuf.append("' and t.mudd='").append(cankdm);
		}
		sqlBuf.append("' and t.gongysbh='").append(gysdm).append("'");
		Map<String,String> map=super.selectOne(sqlBuf.toString());
		if(!map.isEmpty()){
		 lujbh=(String)map.get("lujbh");
		}
		return lujbh;
	}
	
	
	/**
	 * 要货开始日期格式化
	 * @author 贺志国
	 * @date 2012-10-23
	 * @param yhqsrq 要货开始日期字符串
	 * @return Date 格式过的日期 
	 */
	public Date dealYhqsrqStringToDate(String yhqsrq){
		SimpleDateFormat formatYMDhms = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date dateFormat = null;
		if(!"".equals(yhqsrq)){
			String subStringDate = DateTimeUtil.SubString(yhqsrq);
			try {
				//要货开始日期格式化
				dateFormat = formatYMDhms.parse(subStringDate);
			} catch (ParseException e) {
				logger.error(e.getMessage());
			}
		}

		return dateFormat;
	}
	
	/**
	 * 要货结束日期格式化
	 * @author 贺志国
	 * @date 2012-10-23
	 * @param yhjsrq 要货结束日期字符串
	 * @return Date 格式过的日期 
	 */
	public Date dealYhjsrqStringToDate(String yhjsrq){
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFormat = null;
		if(!"".equals(yhjsrq)){
			String strgDate = DateTimeUtil.DateStr(yhjsrq);
			try {
				//要货结束日期格式化
				dateFormat = formatYMD.parse(strgDate);
			} catch (ParseException e) {
				logger.error(e.getMessage());
			}
		}

		return dateFormat;
	}

}
