package com.athena.component.input;


import java.sql.SQLException;
import java.util.Date;

import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class GongzsjDBDataWriter extends DbDataWriter {

	public GongzsjDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	public boolean before() {
		try{
			String sql="truncate table "+SpaceFinal.spacename_ddbh+".CKX_GONGZSJMB";
			super.execute(sql);
			super.commit();
		}catch(RuntimeException e)
		{
			logger.error(e.getMessage());
		}
		return super.before();
	}
	
	/**
	 * 行解析前处理
	 */
	@Override
	public boolean beforeRecord(int rowIndex, Object line) {

		if(line==null|"".equals(line)){
			return false;
		}
		return true;
		
	}
	/**
	 * 行解析之后处理方法
	 * @param rowIndex 行标
	 * @param record 行数据集合
	 * @author GJ
	 */
	@Override
	public void afterRecord(int rowIndex, Record record,Object line) {
//			synchronized(this){
//			    String usercenter = line.toString().substring(0, 3).trim();
//			    String chanx = line.toString().substring(3, 9).trim();
//			    String gongzr = line.toString().substring(9, 19).trim();
//			    String xiaohsj = line.toString().substring(19, 23).trim();
//				String juedsk = line.toString().substring(23, 42).trim();
//				AddTable(usercenter,chanx,gongzr,xiaohsj,juedsk);
//			}
//			super.afterRecord(rowIndex, record, line);

	}
	
	/**
	 * 新增表CKX_GONGZSJMB
	 * @param usercenter
	 * @throws SQLException
	 */
//	private void AddTable(String usercenter,String chanx,String gongzr,String xiaohsj,String juedsk) throws SQLException{
//     		try {
//     			
//     			StringBuffer sqlbuf=new StringBuffer();
//				sqlbuf.append("insert into "+SpaceFinal.spacename_ddbh+".CKX_GONGZSJMB (USERCENTER,CHANX,GONGZR,XIAOHSJ,JUEDSK) values(");
//				sqlbuf.append("'"+strNull(usercenter)+"',");
//				sqlbuf.append("'"+strNull(chanx)+"',");
//				sqlbuf.append("to_date('"+strNull(gongzr)+"','yyyy-MM-dd'),");
//				sqlbuf.append("'"+strNull(xiaohsj)+"',");
//				sqlbuf.append("to_date('"+strNull(juedsk)+"','yyyy-MM-dd HH24:mi:ss'))"); //add by pan.rui
//				super.execute(sqlbuf.toString());
//				super.INSERT_COUNT++;
//				logger.info(sqlbuf.toString());
//			} catch(Exception e){
//               throw new SQLException (e.getMessage());
//			}
//    }
	
	/**
	 * 文件解析完后的处理
	 */
	@Override
	public void after() {

	}
	
	/**
	 * 空串处理
	 * 
	 * @param obj
	 *            对象
	 * @return 处理后字符串
	 * @author GJ
	 * @date 2011-10-26
	 */
	private String strNull(Object obj) {// 对象为空返回空串,不为空toString
		return obj == null ? "" : obj.toString();
	}
}
