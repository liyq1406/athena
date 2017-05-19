package com.athena.component.output;


import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;
/**
 * 工作时间模板
 * @author PAN.RUI
 * @vesion 1.0
 * @date 2012-4-18
 * 
 */
public class GongzJSDataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(GongzJSDataWriter.class);	//定义日志方法
	protected static int i = 0;
	//仓库入库明细构造函数
	public GongzJSDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	@Override
	public void fileAfter(OutputStreamWriter out) {
//		//关闭连接
//		if(interfaceConn!=null){
//			DbUtils.freeConnection(interfaceConn);
//		}
//		
//		if(businessConn!=null){
//			DbUtils.freeConnection(businessConn);
//		}
//		
//		i++;
////		setTotal(getTotal()+dataEchange.total) ;
//		//执行A08,A08S,A10
//		if(i < 3){
//			int page = i*3400+1;
//			int next_Page = (i+1)*3400;
//			String sql =" select t.USERCENTER USERCENTER, "
//					   +" t.CHANX CHANX, "
//					   +" to_char(t.GONGZR, 'yyyy-MM-dd') as GONGZR, "
//					   +" t.XIAOHSJ XIAOHSJ, "
//				       +" to_char(t.JUEDSK, 'yyyy-MM-dd HH24:MI:SS') as JUEDSK "
//					   +" from (SELECT a.*, rownum rn FROM CKX_GONGZSJMB a where rownum <= "+next_Page+") t "
//					   +" where t.RN >= "+page ;
//			System.out.println(i+"------------------------------"+dataParserConfig.getWriterConfigs()[0].getUsercenter());
//			dataParserConfig.getReaderConfig().setSql(sql);
//			dataEchange.doExchange("1910",out,getTotal(),dataParserConfig.getWriterConfigs()[0].getUsercenter());
//		}
//		
//		
	}
	
}
