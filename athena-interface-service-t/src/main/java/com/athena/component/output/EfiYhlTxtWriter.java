package com.athena.component.output;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;
// lastModify  BY 王冲，2012-09-23 09:41   内容:注掉EfiYhlTxtWriter 构造方法中的几个方法 ,对应bug编号 :0004462
public class EfiYhlTxtWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(EfiYhlTxtWriter.class); // 定义日志方法

	private String y_datetime = null;
	private String t_datetime = null;
	private String d_datetime = null;

	public EfiYhlTxtWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
//		DateTime();//获取各个表的时间并且保存起来
//		DeleteTable();//清空各个表
//		InsertTable_YaoHl();//插入EFI外部要货令表数据
//		InsertTable_TonGbJbd();//插入EFI同步/集配单表数据
//		Insert_DaoWldsj();//插入EFI到物理点时间表数据
	}

	/**
	 * 获取各个表的时间并且保存起来
	 */
	public void DateTime() {
		Map map = null;
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String y_sql = "select cj_date from "+SpaceFinal.spacename_ckx+".IN_EFI_YAOHL order by cj_date asc";
			map = super.selectOne(y_sql);
			date = (Date) map.get("cj_date");
			if (null != date) {
				y_datetime = format.format(date);
			}
			

			String t_sql = "select cj_date from "+SpaceFinal.spacename_ckx+".IN_EFI_TONGBJPD order by cj_date asc";
			map = super.selectOne(t_sql);
			date = (Date) map.get("cj_date");
			if (null != date) {
				t_datetime = format.format(date);
			}
			

			String d_sql = "select cj_date from "+SpaceFinal.spacename_ckx+".IN_EFI_DAOWLDSJ order by cj_date asc";
			map = super.selectOne(d_sql);
			date = (Date) map.get("cj_date");
			if (null != date) {
				d_datetime = format.format(date);
			}
			

		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}

	}
	
	
	
	private String DateFormat(Object DateObj){
		String DateStr=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			 if(!"".equals(DateObj)){
				 DateStr=format.format(DateObj);
			 }
		} catch (RuntimeException e) {
              logger.error(e.getMessage());
		}
		return DateStr;
	}
	
	
	
	/**
	 * 清空各个表
	 */
	public void DeleteTable(){
		try{
		String y_sql="delete from "+SpaceFinal.spacename_ckx+".IN_EFI_YAOHL";
		int i=super.execute(y_sql);
		super.commit();
	    if(i>0){
	    	String t_sql="delete from "+SpaceFinal.spacename_ckx+".IN_EFI_TONGBJPD";
	    	int y=super.execute(t_sql);
	    	super.commit();
	    	
	    	if(y>0){
	    		String d_sql="delete from "+SpaceFinal.spacename_ckx+".IN_EFI_DAOWLDSJ";
	    		super.execute(d_sql);
	    		super.commit();
	    	}
	    }
	  }catch(RuntimeException e){
		  logger.error(e.getMessage());
	  }
	}
	
	/**
	 * 查询外部要货令表数据(仓库业务表)
	 *  要货令状态为 01
	 * @return List<Map>
	 */
	public List<Map> QueryTable_YaoHl(){
		List<Map> list=null;
		StringBuffer strbuf=new StringBuffer();
		try{
		strbuf.append("select YAOHLH,");
		strbuf.append("USERCENTER,");
		strbuf.append("JIAOFJ,");
		strbuf.append("ZUIZSJ,");
		strbuf.append("ZUIWSJ,");
		strbuf.append("LINGJBH,");
		strbuf.append("DANW,");
		strbuf.append("YAOHSL,");
		strbuf.append("BAOZXH,");
		strbuf.append("USXH,");
		strbuf.append("UCXH,");
		strbuf.append("UCRL,");
		strbuf.append("UCGS,");
		strbuf.append("FAYSJ,");
		strbuf.append("XIEHD,");
		strbuf.append("MUDD,");
		strbuf.append("DINGDH,");
		strbuf.append("YAOHLLX,");
		strbuf.append("TONGBLSHQJ,");
		strbuf.append("CHEJ,");
		strbuf.append("SHNAGXFS,");
		strbuf.append("CHANX,");
		strbuf.append("KEH,");
		strbuf.append("PEISLB,");
		strbuf.append("YAOHLSCSJ ");
		strbuf.append("from "+SpaceFinal.spacename_ck+".CK_YAOHL ");
		if(null==y_datetime||"".equals(y_datetime)){
			strbuf.append("where EDIT_TIME <= sysdate and yaohlzt='01'");
			}else{
			strbuf.append("where EDIT_TIME between to_date('"+y_datetime+"','yyyy-MM-dd Hh24:mi:ss') and sysdate and yaohlzt='01'");
			}
		list=DbUtils.select(strbuf.toString(), businessConn);
        
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
		return list;
	}
	
	
	/**
	 * 查询同步/集配单表数据(仓库业务表)
	 * @return List<Map>
	 */
	public List<Map> QueryTable_TonGbJbd(){
		List<Map> list=null;
		StringBuffer strbuf=new StringBuffer();
		try{
		strbuf.append("select USERCENTER,");
		strbuf.append("YAOHLH,");
		strbuf.append("LINGJXH,");
		strbuf.append("ZHENGCLSH,");
		strbuf.append("ZHENGCSXSJ,");
		strbuf.append("WEIZ,");
		strbuf.append("LINGJBH,");
		strbuf.append("LINGJMC,");
		strbuf.append("GONGYSDM,");
		strbuf.append("GONGYSMC,");
		strbuf.append("LINGJSL,");
		strbuf.append("DANW ");
		strbuf.append("from "+SpaceFinal.spacename_ck+".CK_TONGBJPD ");
		if(null==t_datetime||"".equals(t_datetime)){
			strbuf.append("where EDIT_TIME<= sysdate");
		}else{
			strbuf.append("where EDIT_TIME between to_date('"+t_datetime+"','yyyy-MM-dd Hh24:mi:ss') and sysdate ");
		}
		list=DbUtils.select(strbuf.toString(), businessConn);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
		return list;
	}
	
	
	
	public List<Map> QueryTable_DaoWldsj(){
		List<Map> list=null;
		StringBuffer strbuf=new StringBuffer();
		try{
			strbuf.append("select YAOHLH, USERCENTER, WULDBH, SHIFJK, GCBH ");
			strbuf.append("from "+SpaceFinal.spacename_ck+".CK_DAOWLDSJ ");
			if(null==d_datetime||"".equals(d_datetime)){
				strbuf.append("where WEIHSJ<= sysdate");
			}else{
				strbuf.append("where WEIHSJ between to_date('"+d_datetime+"','yyyy-MM-dd Hh24:mi:ss') and sysdate");	
			}
		   list=DbUtils.select(strbuf.toString(), businessConn);	
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
		return list;
	}
	
	
	public void InsertTable_YaoHl(){
		List<Map> list=null;
		try{
		list=QueryTable_YaoHl();
		if(list.size()!=0){
		for (Map map : list) {
		   String YAOHLH=String.valueOf(map.get("YAOHLH"));
		   String USERCENTER=String.valueOf(map.get("USERCENTER"));
		   String JIAOFJ=DateFormat(map.get("JIAOFJ"));
		   String ZUIZSJ=DateFormat(map.get("ZUIZSJ"));   
		   String ZUIWSJ=DateFormat(map.get("ZUIWSJ"));
           String LINGJBH=String.valueOf(map.get("LINGJBH"));           
		   String DANW=String.valueOf(map.get("DANW"));		   
		   double YAOHSL=Double.parseDouble(map.get("YAOHSL")==""?"0":String.valueOf(map.get("YAOHSL")));		   
		   String BAOZXH=String.valueOf(map.get("BAOZXH"));
		   double USXH=Double.parseDouble(String.valueOf(map.get("USXH")==""?"0":map.get("USXH")));
		   String UCXH=String.valueOf(map.get("UCXH"));
		   double UCRL=Double.parseDouble(map.get("UCRL")==""?"0":String.valueOf(map.get("UCRL")));
		   int UCGS=Integer.parseInt(map.get("UCGS").toString());
		   String FAYSJ=DateFormat(map.get("FAYSJ")); 
		   String XIEHD=String.valueOf(map.get("XIEHD"));
		   String MUDD=String.valueOf(map.get("MUDD"));
		   String DINGDH=String.valueOf(map.get("DINGDH"));
		   String YAOHLLX=String.valueOf(map.get("YAOHLLX"));
		   String TONGBLSHQJ=String.valueOf(map.get("TONGBLSHQJ"));
		   String CHEJ=String.valueOf(map.get("CHEJ"));
		   String SHNAGXFS=String.valueOf(map.get("SHNAGXFS"));
		   String CHANX=String.valueOf(map.get("CHANX"));
		   String KEH=String.valueOf(map.get("KEH"));
		   String PEISLB=String.valueOf(map.get("PEISLB"));
		   String YAOHLSCSJ=DateFormat(map.get("YAOHLSCSJ"));
	
           StringBuffer strbuf=new StringBuffer();
           strbuf.append("insert into "+SpaceFinal.spacename_ckx+".IN_EFI_YAOHL");
           strbuf.append("(YAOHLH,USERCENTER,JIAOFJ,ZUIZSJ,ZUIWSJ,LINGJBH,DANW,YAOHSL,BAOZXH,USXH,UCXH,UCRL,UCGS,FAYSJ,XIEHD,MUDD,DINGDH,YAOHLLX,TONGBLSHQJ,CHEJ,SHNAGXFS,CHANX,KEH,PEISLB,YAOHLSCSJ,cj_date,clzt)values(");
           strbuf.append("'"+ObjNull(YAOHLH)+"',");
           strbuf.append("'"+ObjNull(USERCENTER)+"',");
           strbuf.append("to_date('"+ObjNull(JIAOFJ)+"','yyyy-MM-dd Hh24:Mi:ss'),");
           strbuf.append("to_date('"+ObjNull(ZUIZSJ)+"','yyyy-MM-dd Hh24:Mi:ss'),");
           strbuf.append("to_date('"+ObjNull(ZUIWSJ)+"','yyyy-MM-dd Hh24:Mi:ss'),");
           strbuf.append("'"+ObjNull(LINGJBH)+"',");
           strbuf.append("'"+ObjNull(DANW)+"',");
           strbuf.append(""+YAOHSL+",");
           strbuf.append("'"+ObjNull(BAOZXH)+"',");
           strbuf.append(""+USXH+",");
           strbuf.append("'"+ObjNull(UCXH)+"',");		
           strbuf.append(""+UCRL+",");
           strbuf.append(""+UCGS+",");
           strbuf.append("to_date('"+ObjNull(FAYSJ)+"','yyyy-MM-dd Hh24:Mi:ss'),");
           strbuf.append("'"+ObjNull(XIEHD)+"',");
           strbuf.append("'"+ObjNull(MUDD)+"',");
           strbuf.append("'"+ObjNull(DINGDH)+"',");
           strbuf.append("'"+ObjNull(YAOHLLX)+"',");
           strbuf.append("'"+ObjNull(TONGBLSHQJ)+"',");
           strbuf.append("'"+ObjNull(CHEJ)+"',");
           strbuf.append("'"+ObjNull(SHNAGXFS)+"',");
           strbuf.append("'"+ObjNull(CHANX)+"',");
           strbuf.append("'"+ObjNull(KEH)+"',");
           strbuf.append("'"+ObjNull(PEISLB)+"',");
           strbuf.append("to_date('"+ObjNull(YAOHLSCSJ)+"','yyyy-MM-dd Hh24:Mi:ss'),");
           strbuf.append("sysdate,");
           strbuf.append("'0')");
           DbUtils.execute(strbuf.toString(), interfaceConn);
           interfaceConn.commit();
           strbuf=new StringBuffer("");    
		 }
		}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
	}
		
		
	public void InsertTable_TonGbJbd(){
		List<Map> list=null;
		try{
		list=QueryTable_TonGbJbd();
		if(list.size()!=0){
		for (Map map : list) {
			String USERCENTER=String.valueOf(map.get("USERCENTER"));
			String YAOHLH=String.valueOf(map.get("YAOHLH"));
			String LINGJXH=String.valueOf(map.get("LINGJXH"));
			String ZHENGCLSH=String.valueOf(map.get("ZHENGCLSH"));
			String ZHENGCSXSJ=DateFormat(map.get("ZHENGCSXSJ"));
			String WEIZ=String.valueOf(map.get("WEIZ"));
			String LINGJBH=String.valueOf( map.get("LINGJBH"));
			String LINGJMC=String.valueOf( map.get("LINGJMC"));
			String GONGYSDM=String.valueOf( map.get("GONGYSDM"));
			String GONGYSMC=String.valueOf( map.get("GONGYSMC"));
			double LINGJSL=Double.parseDouble(String.valueOf(map.get("LINGJSL")==""?"0":map.get("LINGJSL")));;
			String DANW=String.valueOf( map.get("DANW"));
            StringBuffer strbuf=new StringBuffer();
            strbuf.append("insert into "+SpaceFinal.spacename_ckx+".IN_EFI_TONGBJPD(USERCENTER,YAOHLH,LINGJXH,ZHENGCLSH,ZHENGCSXSJ,WEIZ,LINGJBH,LINGJMC,GONGYSDM,GONGYSMC,LINGJSL,DANW,cj_date,clzt)values(");
            strbuf.append("'"+ObjNull(USERCENTER)+"',");
            strbuf.append("'"+ObjNull(YAOHLH)+"',");
            strbuf.append("'"+ObjNull(LINGJXH)+"',");
            strbuf.append("'"+ObjNull(ZHENGCLSH)+"',");
            strbuf.append("to_date('"+ObjNull(ZHENGCSXSJ)+"','yyyy-MM-dd Hh24:Mi:ss'),");
            strbuf.append("'"+ObjNull(WEIZ)+"',");
            strbuf.append("'"+ObjNull(LINGJBH)+"',");
            strbuf.append("'"+ObjNull(LINGJMC)+"',");
            strbuf.append("'"+ObjNull(GONGYSDM)+"',");
            strbuf.append("'"+ObjNull(GONGYSMC)+"',");
            strbuf.append("'"+LINGJSL+"',");
            strbuf.append("'"+ObjNull(DANW)+"',");
            strbuf.append("sysdate,");
            strbuf.append("'0')");
			DbUtils.execute(strbuf.toString(), interfaceConn);
			strbuf=new StringBuffer("");
			interfaceConn.commit();
		}
		}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
			
		}
		
	
	
	public void Insert_DaoWldsj(){
		List<Map> list=null;
		try{
		list=QueryTable_DaoWldsj();
		if(list.size()!=0){
		for (Map map : list) {
			String YAOHLH=String.valueOf(map.get("YAOHLH"));
			String USERCENTER=String.valueOf(map.get("USERCENTER"));
			String WULDBH=String.valueOf(map.get("WULDBH"));
			String SHIFJK=String.valueOf(map.get("SHIFJK"));
			String GCBH=String.valueOf(map.get("GCBH"));
			StringBuffer strbuf=new StringBuffer();
			strbuf.append("insert into "+SpaceFinal.spacename_ckx+".IN_EFI_DAOWLDSJ(YAOHLH,USERCENTER,WULDBH,SHIFJK,GCBH,cj_date,clzt)values(");
			strbuf.append("'"+ObjNull(YAOHLH)+"',");
			strbuf.append("'"+ObjNull(USERCENTER)+"',");
			strbuf.append("'"+ObjNull(WULDBH)+"',");
			strbuf.append("'"+ObjNull(SHIFJK)+"',");
			strbuf.append("'"+ObjNull(GCBH)+"',");
			strbuf.append("sysdate,");
			strbuf.append("'0')");
			DbUtils.execute(strbuf.toString(), interfaceConn);
			strbuf=new StringBuffer("");
			interfaceConn.commit();
		}
		}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 字符串不为null方法
	 * @param str
	 * @return
	 */
    private String ObjNull(Object obj){
    
    	return obj==null?"":obj.toString();
    	
    }
		

}
