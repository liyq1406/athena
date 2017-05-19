package com.athena.component.output;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;

//lastModify 王冲, 2012-08-30 11:18
public class CpklszTxtWriter extends TxtDataWriter{
	protected static Logger logger = Logger.getLogger(CpklszTxtWriter.class);	//定义日志方法
	private String datetime=null;
	
	public CpklszTxtWriter(DataParserConfig dataParserConfig) throws SQLException{
		super(dataParserConfig);
//		DateTime();//查询上次运行的时间
//		DeleteTable();//清空表数据
//		InsertTable();//业务表数据插入到接口表中
	}
	
//	
//	/**
//	 * 查找上次运行的时间
//	 */
//	public void DateTime(){
//		try{
//		String sql="select to_char(cj_date,'yyyy-MM-dd Hh24:mi:ss') as cj_date from "+SpaceFinal.spacename_ck+".in_cpk_lszbhd order by cj_date asc";
//		Map map=DbUtils.selectOne(sql, interfaceConn);
//		datetime=(String)map.get("cj_date");
//		}catch(RuntimeException e){
//			logger.error(e.getMessage());
//		}
//	}
//	
//	
//    /**
//     * 清空表数据
//     */
//	public void DeleteTable()
//	{
//		try{
//	        String sql="delete from "+SpaceFinal.spacename_ck+".in_cpk_lszbhd";
//	        DbUtils.execute(sql, interfaceConn);
//			}catch(RuntimeException e){
//				logger.error(e.getMessage());
//			}
//	}
//	
//
//
//	/**
//	 * 查找备货单与备货单明细表数据
//	 * @return
//	 */
//	public List<Map> QueryCKTable(){
//	    StringBuffer strbuf=new StringBuffer();
//	    List<Map> list=null;
//	    try{
//	    strbuf.append("select y.liush as liush,");
//	    strbuf.append("t.usercenter as usercenter,");
//		strbuf.append("y.lingjbh as lingjbh,");
//		strbuf.append("t.cangkbh as cangkbh,");
//		strbuf.append("t.zickbh as zickbh,");
//		strbuf.append("t.keh as keh,");
//		strbuf.append("y.danw as danw,");
//		strbuf.append("y.chanx as chanx,");
//		strbuf.append("y.dingdh as dingdh,");
//		strbuf.append("y.shifsl as shifsl,");
//		strbuf.append("y.ush as ush ");
//	    strbuf.append("from  "+SpaceFinal.spacename_ck+".ck_beihd t, "+SpaceFinal.spacename_ck+".ck_beihdmx y ");
//		strbuf.append("where t.usercenter = y.usercenter ");
//		strbuf.append("and t.beihdh = y.beihdh ");
//		if(null==datetime||"".equals(datetime)){
//		strbuf.append("and t.EDIT_TIME<= sysdate ");
//		strbuf.append("and y.EDIT_TIME<= sysdate ");
//		}else{
//		strbuf.append("and t.EDIT_TIME between to_date('"+datetime+"','yyyy-MM-dd Hh24:mi:ss') and sysdate ");
//		strbuf.append("and y.EDIT_TIME between to_date('"+datetime+"','yyyy-MM-dd Hh24:mi:ss') and sysdate ");
//		}
//		list=DbUtils.select(strbuf.toString(), businessConn);
//	    }catch(RuntimeException e){
//	    	logger.error(e.getMessage());
//	    }
//		return list;
//	}
//	
//	
//	/**
//	 * 查找参考系零件客户关系与零件表数据
//	 * @return
//	 */
//	public List<Map> QueryCKXTable(){
//	   StringBuffer strbuf=new StringBuffer();
//       List<Map> list=null;
//       List<Map> ckx_list=null;
//	   try{
//	   list=QueryCKTable();
//	   for (Map map : list) {
//	   String usercenter=(String) map.get("usercenter");
//	   String lingjbh=(String) map.get("lingjbh");
//	   strbuf.append("select z.kehljh as kehljh,");
//	   strbuf.append("x.lingjzl as lingjzl ");
//	   strbuf.append("from "+SpaceFinal.spacename_ckx+".ckx_lingjkh z,"+SpaceFinal.spacename_ckx+".ckx_lingj x ");
//	   strbuf.append("where z.usercenter='"+usercenter+"' ");
//	   strbuf.append("and z.lingjbh='"+lingjbh+"' ");
//	   strbuf.append("and x.usercenter='"+usercenter+"' ");
//	   strbuf.append("and x.lingjbh='"+lingjbh+"'");
//	   ckx_list=DbUtils.select(strbuf.toString(), interfaceConn);
//	   map.put("ckx_list", ckx_list);
//	   strbuf=new StringBuffer("");
//	   }
//	   }catch(RuntimeException e){
//		   logger.error(e.getMessage());
//	   }
//	   return list;
//	}
//	
//	
//	/**
//	 * 插入成品库流水账接口表里
//	 * @throws SQLException 
//	 */
//	public void InsertTable() throws SQLException{
//        List<Map> list=null;
//        List<Map> ckx_list=null;
//        try{
//        interfaceConn.setAutoCommit(false);
//        list=QueryCKXTable();
//        for (Map map : list) {
//        String liush=(String)map.get("liush");
//        String usercenter=(String) map.get("usercenter");
//        String lingjbh=(String) map.get("lingjbh");
//        String cangkbh=(String) map.get("cangkbh");
//        String zickbh=(String) map.get("zickbh");
//        String keh=(String) map.get("keh");
//        String danw=(String) map.get("danw");
//        String chanx=(String) map.get("chanx");
//        String dingdh=(String) map.get("dingdh");
//        double shifsl=Double.parseDouble(map.get("shifsl").toString());
//        String bqh=(String) map.get("bqh");
//        
//        StringBuffer strbuf=new StringBuffer();
//		strbuf.append("insert into "+SpaceFinal.spacename_ck+".in_cpk_lszbhd");
//		strbuf.append("(liush,usercenter,lingjbh,cangkbh,zickbh,keh,danw,chanx,dingdh,shifsl,bqh,cj_date,clzt,kehljh,lingjzl)values(");
//		strbuf.append("'"+StrNull(liush)+"','"+StrNull(usercenter)+"','"+StrNull(lingjbh)+"','"+StrNull(cangkbh)+"','"+StrNull(zickbh)+"','"+StrNull(keh)+"','"+StrNull(danw)+"',");
//		strbuf.append("'"+StrNull(chanx)+"','"+StrNull(dingdh)+"',"+shifsl+",'"+StrNull(bqh)+"',sysdate,'0',");
//		ckx_list=(List<Map>) map.get("ckx_list");
//		if(!ckx_list.isEmpty()){
//			for (Map map2 : ckx_list) {
//				String kehljh=(String) map2.get("kehljh");
//		        double lingjzl=Double.parseDouble(map2.get("lingjzl").toString());
//		        strbuf.append("'"+StrNull(kehljh)+"',"+lingjzl+")");
//			}
//		}else{
//			strbuf.append("'','')");	
//		}
//        DbUtils.execute(strbuf.toString(), businessConn);
//        interfaceConn.commit();
//        strbuf=new StringBuffer("");
//        }
//        
//		}catch(SQLException e){
//        	interfaceConn.rollback();
//        	logger.error(e.getMessage());     	
//        } 
//	}
//	
//	
	/**
	 * 增加空格
	 * @param num
	 * @return
	 */
	public static String getBlank(int num){
		String str="";
		for (int i = 0; i <= num; i++) {
			str+=" ";
		}
		return str;
	}
//	
//	
//	/**
//	 * 字符串不为null方法
//	 * @param str
//	 * @return
//	 */
//    private String StrNull(Object objstr){
//    
//    	return objstr==null?"":objstr.toString();
//    	
//    }
//	

	/**
	 * 文件头记录的数据
	 */
	@Override
	public void fileBefore(OutputStreamWriter writer) {

		String DateTime=DateTimeUtil.getDateTimeStr("yyyyMMddHHmmss");
		try {
			writer.write("DEBATHENA  ath1osap04FS02492"+DateTime+""+getBlank(158)+"");
			writer.write("\n");
		} catch (IOException e) {
			 logger.error(e.getMessage());
		}
		super.fileBefore(writer);
	}

	
	/**
	 * 文件尾记录的数据
	 */
	@Override
	public void fileAfter(OutputStreamWriter out) {
        try {
			out.write("FINATHENA  ath1osap04FS02492+"+totalToString()+""+getBlank(163)+"");
		} catch (IOException e) {
			 logger.error(e.getMessage());
		}	
		super.fileAfter(out);
}	

	public   String totalToString(){
		StringBuffer total = new StringBuffer(String.valueOf(this.getTotal()) ) ; 
		
		for(int i = total.length() ;i<9;i++){
			total.insert(0, "0") ;
		}
		
		return total.toString();
	}
	
	
}
