package com.athena.component.output;

import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;

public class FhtzxxTxtWriter extends TxtDataWriter{
	protected static Logger logger = Logger.getLogger(FhtzxxTxtWriter.class);	//定义日志方法
	public FhtzxxTxtWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
	}


	@Override
	public boolean beforeRecord(int rowIndex, Object rowObject) {
		try{
        Map map=(Map) rowObject;
        String jfdh=map.get("jfdh").toString();
        map.put("jfdh", convert(space(jfdh,10))); //交付订单格式化
        
        String tch=map.get("tch").toString();
        String tch1=convert(space(tch,15)); 
        map.put("tch", tch1);//集装箱号格式化
        
        String um=map.get("um").toString();
        map.put("um", convert(space(um,10)));//UM号格式化
        
        String lingjh=map.get("lingjh").toString();
        map.put("lingjh", convert(space(lingjh,10)));//零件号格式化
        
		}catch(RuntimeException e){
			logger.error(e.getMessage());
		}
		return super.beforeRecord(rowIndex, rowObject);
	}

	public String convert(String str){
    	
    	return str==null?"":"\""+str+"\",";
    }
	
    
    public String space(String str,int t_num){
    	int num=t_num-str.length();
    	for (int i = 0; i < num; i++) {
			str+=" "; 
		}
    	return str;
    }
    
    
	
}