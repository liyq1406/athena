package com.athena.component.input;




import org.apache.log4j.Logger;

import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

/**
 * EFI发货通知接口输入类
 * @author GJ
 */
public class EFIFhtzDbDataWriter extends DbDataWriter{
	protected static Logger logger = Logger.getLogger(EFIFhtzDbDataWriter.class);	//定义日志方法
	public EFIFhtzDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	
	 /**
	 * 解析之前清空表数据
	 */
	@Override
	public boolean before() {
        try {
        	//EFI发货通知-发运主体信息表是否存在处理状态为1的数据
        	String sqlClzt = "select count(JFDH) total from "+SpaceFinal.spacename_ck+".in_efi_fhtz_fyztxx where CLZT = '1'";
        	int i = Integer.parseInt(String.valueOf(super.selectValue(sqlClzt)));
			if(i>0){
				//清空EFI发货通知-同步零件信息表
				String sql3="delete from "+SpaceFinal.spacename_ck+".in_efi_fhtz_tbljxx where UA in "
						   +" (select t.ua from "+SpaceFinal.spacename_ck+".in_efi_fhtz_bzdyxx t,"
						   +" "+SpaceFinal.spacename_ck+".in_efi_fhtz_fyztxx t1"
						   +" where t.jfdh = t1.jfdh and t1.clzt = '1')";
				super.execute(sql3);
				//清空EFI发货通知-包装对应信息表
				String sql2="delete from "+SpaceFinal.spacename_ck+".in_efi_fhtz_bzdyxx where JFDH in "
						   +" (select JFDH from "+SpaceFinal.spacename_ck+".in_efi_fhtz_fyztxx where CLZT = '1')";
				super.execute(sql2);
				//清空EFI发货通知-发运明细表
				String sql1="delete from "+SpaceFinal.spacename_ck+".in_efi_fhtz_fymx where JFDH in " 
						   +" (select JFDH from "+SpaceFinal.spacename_ck+".in_efi_fhtz_fyztxx where CLZT = '1')";
				super.execute(sql1);
	        	//清空EFI发货通知-发运主体信息表
				String sql="delete from "+SpaceFinal.spacename_ck+".in_efi_fhtz_fyztxx where CLZT = '1'";
				super.execute(sql);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return super.before();
	}


	@Override
	public void after() {
		System.out.println("执行完毕！");
	}
	
	
	
	
	
	

}
	