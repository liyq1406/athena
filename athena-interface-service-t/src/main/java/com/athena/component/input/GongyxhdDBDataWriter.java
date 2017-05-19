package com.athena.component.input;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;

public class GongyxhdDBDataWriter extends DbDataWriter {
	private List listData = new ArrayList();
	private  Date date = new Date();
	public GongyxhdDBDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}
	
	
	public void afterRecord(int rowIndex, Record record,Object line){		
		//record.put("CREATOR", "interface");
		record.put("CREATOR", super.dataExchange.getCID());
		record.put("CREATE_TIME", date);
		//record.put("EDITOR", "interface");
		record.put("EDITOR", super.dataExchange.getCID());
		record.put("EDIT_TIME", date);
		
		String GONGYXHD = record.getString("GONGYXHD").substring(0,3);
		String SHENGCXBH = "".equals(strNull(record.getString("SHENGCXBH")))?"":record.getString("SHENGCXBH").substring(0,2);
		String gyxhd = GONGYXHD + SHENGCXBH;
		if(!listData.contains(gyxhd)){
			listData.add(gyxhd);
		}
		super.afterRecord(rowIndex, record, line);
	}
	
	public void after(){
		try{
			addTable();
		} catch(Exception e){
			e.printStackTrace();
		}
		super.after();
	}
	

//	/**
//	 * 查询工艺消耗点
//	 * @return
//	 */
//	private List getBesinessDatas(){
//		List list = new ArrayList<Map<String,Object>>();
//		try{
//			StringBuffer sb = new StringBuffer();	
//			sb.append(" SELECT ");
//			sb.append(" DISTINCT SUBSTR(t.GONGYXHD,1,3) GONGYXHD,SUBSTR(t.SHENGCXBH,1,2) SHENGCXBH ");
//			sb.append(" FROM "+SpaceFinal.spacename_ck+".CKX_GONGYXHD t  ");
//			sb.append(" WHERE 1=1  AND t.edit_time between ");
//			sb.append(" (select b.lastlctime from "+SpaceFinal.spacename_ck+".in_zidb b where b.inbh = '2080') and sysdate ");
//	
//			list = select(sb.toString());
//			
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}		
//		return list;
//	}
	
	/**
	 * 添容器区表
	 */
	private void insertRQC(String usercenter,String rongqcbh) {
		try{
			//创建sql语句 查询
			StringBuffer sb = new StringBuffer();
			
			sb.append("INSERT INTO "+SpaceFinal.spacename_ck+".CKX_RONGQC ");
			sb.append("(USERCENTER, ");
			sb.append(" RONGQCBH, ");
			sb.append(" MIAOS, ");
			sb.append(" BIAOS, ");
			sb.append(" CREATOR, ");
			sb.append(" CREATE_TIME, ");
			sb.append(" EDITOR, ");
			sb.append(" EDIT_TIME, ");
			sb.append(" SHIFFK) ");
			sb.append(" values( ");
			sb.append("'"+strNull(usercenter)+"',");
			sb.append("'"+strNull(rongqcbh)+"',");
			sb.append("'工艺消耗点',");
			sb.append("'1',");
			sb.append("'interspace',");
			sb.append("sysdate,");
			sb.append("'interspace',");
			sb.append("sysdate,");
			sb.append("'R')");
			execute(sb.toString());
		}catch(Exception e){
			logger.error(e.getMessage());
		}			
	}
	

	//删除接口外部要货令表表数据
	//使用接口表 连接
	private void addTable() {	
		if(!listData.isEmpty()){
			for(int i=0;i<listData.size();i++){
				String rongqcbh = listData.get(i).toString().substring(0,3);
				String usercenter = strNull(listData.get(i).toString().substring(3,5));
				if(!QueryTable(usercenter,rongqcbh)){
					insertRQC(usercenter,rongqcbh);
				}
				
			}
		}
	}
	
	/**查找主键为条件的数据是否已经存在
	 * @param dataStr
	 * @return
	 */
	public boolean QueryTable(String usercenter,String rongqcbh){
		boolean flag=false;
		StringBuffer strbuf=new StringBuffer();
		try{
			strbuf.append("select count(1) from "+SpaceFinal.spacename_ck+".CKX_RONGQC where ");
			strbuf.append(" USERCENTER='"+strNull(usercenter)+"' ");
			strbuf.append(" AND RONGQCBH='"+strNull(rongqcbh)+"' ");
			int count = Integer.valueOf(selectValue(strbuf.toString()).toString());
			
			if(count>0){
				flag = true;
			}
			
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return flag;
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
		return obj == null || obj.equals("null")? "" : obj.toString();
	}

}
