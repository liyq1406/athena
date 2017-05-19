package com.athena.component.output;

import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;

public class GevpFhtzTxtWriter extends TxtDataWriter{
	protected static Logger logger = Logger.getLogger(GevpFhtzTxtWriter.class);	//定义日志方法
	
	public GevpFhtzTxtWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}

	
	/**
	 * 解析之前的数据操作
	 */
	@Override
	public boolean beforeRecord(int rowIndex, Object rowObject) {
//		try{
//        Map map=(Map) rowObject;
//		String fhtzh=(String) map.get("fhtzh");
//		String fhtzh1=Convert(splace(fhtzh,10));
//		map.put("fhtzh", fhtzh1);
//		
//		String scsj=(String) map.get("scsj");
//		String scsj1=Convert(splace(scsj,14));
//		map.put("scsj", scsj1);
//		
//		String fssj=(String) map.get("fssj");
//		String fssj1=Convert(splace(fssj,16));
//		map.put("fssj",fssj1);
//		
//		String ddsj=(String) map.get("ddsj");
//		String ddsj1=Convert(splace(ddsj,16));
//		map.put("ddsj", ddsj1);
//		
//		String chenys=(String) map.get("chenys");
//		String chenys1=Convert(splace(chenys,17));
//		map.put("chenys",chenys1);
//		
//		String gongys=(String) map.get("gongys");
//		String gongys1=Convert(splace(gongys,10));
//		map.put("gongys", gongys1);
//		
//		String xiehd=(String) map.get("xiehd");
//		String xiehd1=Convert(splace(xiehd,15));
//		map.put("xiehd", xiehd1);
//		
//		String kach=(String) map.get("kach");
//		String kach1=Convert(splace(kach,9));
//		map.put("kach", kach1);
//		
//		String usercenter=(String) map.get("usercenter");
//		map.put("usercenter", Convert(usercenter));
//        
//		String lingjh=(String) map.get("lingjh");
//		String lingjh1=Convert(splace(lingjh,10));
//		map.put("lingjh", lingjh1);
//        
//		String lingjmc=(String) map.get("lingjmc");
//		if(lingjmc==null){
//			lingjmc="";
//		}
//		String lingjmc1=Convert(splace(lingjmc,40));
//		map.put("lingjmc", lingjmc1);
//		
//        
//		String lingjsl=(String) map.get("lingjsl");
//		String lingjsl1=Convert(splace(lingjsl,9));
//		map.put("lingjsl",lingjsl1);
//       
//		String yaohlh=(String) map.get("yaohlh");
//		String yaohlh1=Convert(splace(yaohlh,22));
//		map.put("yaohlh",yaohlh1);
//        
//		String rqxh=(String) map.get("rqxh");
//		String rqxh1=Convert(splace(rqxh,6));
//		map.put("rqxh",rqxh1);
//		
//		String rqsl=(String) map.get("rqsl");
//		String rqsl1=Convert(splace(rqsl,9));
//		map.put("rqsl", rqsl1);
//		
//		String rqnljsl=(String) map.get("rqnljsl");
//		String rqnljsl1=Convert(splace(rqnljsl,9));
//		map.put("rqnljsl", rqnljsl1);
//		
//		String filler=(String) map.get("filler");
//		if(filler==null){
//			filler="";
//		}
//		String filler1=Convert(splace(filler,10));
//		map.put("filler",filler1);
//		}catch(RuntimeException e){
//			logger.error(e.getMessage());
//		}
		return super.beforeRecord(rowIndex, rowObject);
	}
	
    /**
     * 字段最后一位加逗号
     * @param str
     * @return
     */
	public String Convert(String str)
	{
		return str+",";
	}
	
	
	/**
	 * 字段后面加空格
	 * @param str
	 * @param t_num
	 * @return
	 */
	public String splace(String str,int t_num){
		int num=t_num-str.length();
		for (int i = 0; i < num; i++) {
			str=str+" ";
		}
		return str;
	}
	
}
