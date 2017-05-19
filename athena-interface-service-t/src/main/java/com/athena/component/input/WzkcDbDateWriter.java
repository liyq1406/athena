package com.athena.component.input;

import java.util.Date;

import org.apache.log4j.Logger;



import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

/**
 * 外租库存接口输入类
 * @author PR
 *
 */
public class WzkcDbDateWriter extends DbDataWriter{
	protected static Logger logger = Logger.getLogger(WzkcDbDateWriter.class);	//定义日志方法
	public WzkcDbDateWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	

	/**
	 * 数据解析之前清空 外租库存表数据
	 */
	@Override
	public boolean before() {
		try{
			String sql="delete from "+SpaceFinal.spacename_ck+".ck_wzkc";
			super.execute(sql);
		}catch(RuntimeException e)
		{
			logger.error(e.getMessage());
		}
		return super.before();
	}


	/**
	 * 行解析之后处理方法
	 * @param rowIndex 行标
	 * @param record 行数据集合
	 * @author PR
	 */
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		try{
		if(!record.isEmpty()){
		String ck_date=record.getString("ck_date");//获得库存时间
		record.put("ck_date", DateTimeUtil.DateStr(ck_date));//时间格式化
		
		//库存数量格式转换
		String num=record.getString("n_maf_num").trim();
		if(!"".equals(num)){
			double d_num=Double.parseDouble(num);
			double n_num=d_num/1000;
			record.put("n_maf_num", num);
		}
		
		//卷号取空格
		String juanh=record.getString("juanh").trim();
		if(!"".equals(juanh)){
			record.put("juanh", juanh);
		}
		
		
		//存入创建时间和处理状态初始值
		record.put("cj_date", new Date());
//		record.put("clzt", 0);
		}
		super.afterRecord(rowIndex, record, line);
		}catch(Exception e){
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
	}
}
