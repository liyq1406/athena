package com.athena.component.input;





import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;


import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.util.date.DateUtil;




/**
 * 零件参考系接口输入类
 * @author WL
 * @date 2011-10-20
 */
public class ComponentDbDataWriter extends DbDataWriter {
	protected static Logger logger = Logger.getLogger(ComponentDbDataWriter.class);	//定义日志方法
	//初始化时间变量
	public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	public static String yyyy=null;
	public static String mm=null;
	public static String dd=null;
	public static String datetime=null;
   
	
	public ComponentDbDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		
	}
	
	/**
	 * 解析数据之前清空零件参考系表数据
	 * hzg 2012-10-11 去掉in_component中间表，直接写ckx_lingj表。
	 */
	public boolean before()
	{ 
		/*try{
		String sql="delete from "+SpaceFinal.spacename_ckx+".in_component";
		super.execute(sql);
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}*/
		return super.before();
	}
	
	/**
	 * 时间格式化
	 * @param time
	 * @return String
	 */
    public String substring(String time)
    {
    	    if(time.length()!=0){
            yyyy="20"+time.substring(4);
            mm=time.substring(2,4);
            dd=time.substring(0,2);
            datetime=yyyy+"-"+mm+"-"+dd;
    	    }
            return datetime;
    	    
    }
    
    
//    /**
//     * 判断制造路线是否存ckx_xitcsdy
//     * @param usercenter
//     * @param lcdv
//     * @param scxh
//     * @return
//     */
//	private String isExitsZhiZLX(String zhizlx){
//		String strflag="";
//		try{
//			StringBuffer strbuf=new StringBuffer();
//			strbuf.append(" select count(*) num from "+SpaceFinal.spacename_ckx+".ckx_xitcsdy t ");
//			strbuf.append(" where t.zidlx = 'DINGHLX' ");
//			strbuf.append(" and t.zidbm = '"+strNull(zhizlx)+"' ");
//			int cxNum = Integer.parseInt(selectValue(strbuf.toString()).toString());
//			if(cxNum > 0){
//				strflag = "1";
//			}
//		}catch(Exception e){
//			logger.error(e.getMessage());
//			throw new RuntimeException(e.getMessage());
//		}
//		return strflag;
//	}
    
	/**
	 * 行解析之后处理方法
	 * @param rowIndex 行标
	 * @param record 行数据集合
	 * @author GJ
	 * @update hzg 2012-10-11  直接写业务表，不走中间表
	 */
	@Override
	public  void afterRecord(int rowIndex, Record record,Object line) {
		try {	
		if(record.size()!=0){
		String ljdhcj=record.getString("dinghcj");//取零件订货车间
		if(ljdhcj.length()!=0){
			String usercenter=ljdhcj.substring(0, 2);//取零件订货车间前2个字符为用户中心
			record.put("usercenter", usercenter);
		}

		//取开始日期
		String ksrq=record.getString("kaisrq");
		if(ksrq.length()!=0&&"999999".equals(ksrq)){//如果开始日期为99999将日期格式转2099-12-31
			record.put("kaisrq",DateUtil.stringToDateYMD("2099-12-31"));
		}
		else{
			record.put("kaisrq",DateUtil.stringToDateYMD(substring(ksrq)));
		}
	   
		//取结束日期
		String jsrq =  record.getString("jiesrq");
		if(jsrq.length()!=0&&"999999".equals(jsrq)){//如果结束日期为99999将日期格式转成2099-12-31
			record.put("jiesrq",DateUtil.stringToDateYMD("2099-12-31"));
		}
		else{
			record.put("jiesrq",DateUtil.stringToDateYMD(substring(jsrq)));
		}
//		//取制造路线  由于业务问题此需求取消
//		String zhizlx1 =  record.getString("zhizlx1"); 
//		String zhizlx = ("1").equals(isExitsZhiZLX(zhizlx1))?zhizlx1:""; //得到订货路线
//		record.put("zhizlx", zhizlx);
		//存入创建时间和处理状态初始数据
		record.put("biaos", 2);
//		record.put("creator", "interface") ;
//		record.put("editor", "interface") ;
		record.put("creator", super.dataExchange.getCID()) ;
		record.put("editor", super.dataExchange.getCID()) ;
		record.put("create_time", new Date()) ;
		record.put("edit_time", new Date()) ;
		
		 }
		super.afterRecord(rowIndex,record,line);
		} catch (RuntimeException e) {
			super.makeErrorMessage(rowIndex, record, e.getMessage(), line);
		}
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
