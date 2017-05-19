package com.athena.component.input;




import org.apache.log4j.Logger;


import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

/**
 * EDI发货通知接口输入类
 * @author GJ
 */
public class FhtzDbDataWriter extends DbDataWriter {
	protected static Logger logger = Logger.getLogger(FhtzDbDataWriter.class);	//定义日志方法
	protected static String RunEndTime=null;
	public FhtzDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	/**
	 * 解析之前清空表数据
	 */
	@Override
	public boolean before() {
        try{
	        //EDI发货通知-发运主体信息表数据是否存在处理状态为1的数据
        	String sqlT01A001 = "select count(T01A001) total from "+SpaceFinal.spacename_ck+".in_edi_fhtz_fyxx where CLZT = '1'";
        	int i = Integer.parseInt(String.valueOf(super.selectValue(sqlT01A001)));	
        	//存在数据
	        if(i>0)
	        {   
	        	//清空EDI发货通知-包装对应信息表数据
	        	String sql3="delete from "+SpaceFinal.spacename_ck+".in_edi_fhtz_bzxx where T01A001 in "
	        	 		   +" (select T01A001 from "+SpaceFinal.spacename_ck+".in_edi_fhtz_fyxx where CLZT = '1')";
	        	super.execute(sql3);
	        	//清空EDI发货通知-发运明细表数据
	        	String sql2="delete from "+SpaceFinal.spacename_ck+".in_edi_fhtz_fymx where T01A001 in " 
	        			   +" (select T01A001 from "+SpaceFinal.spacename_ck+".in_edi_fhtz_fyxx where CLZT = '1')";
	        	super.execute(sql2);
		        //清空EDI发货通知-发运主体信息表数据
				String sql1="delete from "+SpaceFinal.spacename_ck+".in_edi_fhtz_fyxx where CLZT = '1'";
				super.execute(sql1);
	        }
        }catch(Exception e)
        {
        	logger.error(e.getMessage());
        }
		return super.before();
	}
	
	
	@Override
	public void after() {
		
		RunEndTime=DateTimeUtil.getAllCurrTime();
		System.out.println("运行完毕！");
		
	}

}
