package com.athena.component.input;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;



import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class GEVPYhlDbDataWriter extends DbDataWriter{
	protected static Logger logger = Logger.getLogger(GEVPYhlDbDataWriter.class);	//定义日志方法
	
	public GEVPYhlDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}


	/**
	 * 解析数据之前清空GEVP要货令表数据
	 */
	@Override
	public boolean before(){
		try {
    		String sql="delete from "+SpaceFinal.spacename_ck+".IN_GEVP_YAOHL";
			super.execute(sql);
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
		return super.before();
	}
	

	@Override
	public boolean beforeRecord(int rowIndex, Object line) {
		
		if(line.toString().startsWith("ENROB")){
			return false;
		}
		return super.beforeRecord(rowIndex, line);
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
			//SimpleDateFormat dateformat=new SimpleDateFormat("yyyyMMddHHmmssss");
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			if(null!=record){	
				//供应商代码
				String gysdm=record.getString("GYSDM").trim();
				if(null!=gysdm&&!"".equals(gysdm)){
					record.put("GYSDM", gysdm);
				}

				//最晚交付时间
				String zuiwsj=record.getString("ZUIWSJ").trim();
				if(null!=zuiwsj&&!"".equals(zuiwsj)){
					String d_zuiwsj=DateTimeUtil.DateFormat_Fhtz(zuiwsj);
					Date w_date=dateformat.parse(d_zuiwsj);
					record.put("ZUIWSJ",w_date);
				}

				//零件编码
				String ljbm=record.getString("LINGJBH").trim();
				if(null!=ljbm&&!"".equals(ljbm)){
					record.put("LINGJBH", ljbm);
				}

				//单位
				String danw=record.getString("DANW").trim();
				if(null!=danw&&!"".equals(danw)){
					record.put("DANW", danw);
				}


				//UC型号
				String ucxh=record.getString("UCXH").trim();
				if(null!=ucxh&&!"".equals(ucxh)){
					record.put("UCXH", ucxh);
				}

				//UC个数
				String ucgs=record.getString("UCGS").trim();
				if(null!=ucgs&&!"".equals(ucgs)){
					int i_ucgs =Integer.parseInt(ucgs);
					record.put("UCGS", i_ucgs);
				}

				//卸货点
				String xiehd=record.getString("XIEHD").trim();
				if(null!=xiehd&&!"".equals(xiehd)){
					record.put("XIEHD", xiehd);
				}
				//目的地
				String mudd=record.getString("MUDD").trim();
				if(null!=mudd&&!"".equals(mudd)){
					record.put("MUDD",mudd);
				}

				//订单号
				String dingdh=record.getString("DINGDH").trim();
				if(null!=dingdh&&!"".equals(dingdh)){
					record.put("DINGDH", dingdh);
				}

				//客户
				String kehu=record.getString("KEHU").trim();
				if(null!=kehu&&!"".equals(kehu)){
					record.put("KEHU", kehu);
				}

				//插入创建日期和处理状态
				record.put("CJ_DATE",new Date());
				record.put("CLZT",0);

			}
			super.afterRecord(rowIndex, record, line);
		}catch(Exception e){
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		} 	
	}
}
