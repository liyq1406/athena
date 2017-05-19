/**
 * 
 */
package com.athena.component.exchange.txt;

import org.apache.log4j.Logger;

import com.athena.component.exchange.AbstractRowParser;
import com.athena.component.exchange.DataWriter;
import com.athena.component.exchange.ParseLineCommand;
import com.athena.component.exchange.ParserException;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.RowParser;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.component.exchange.field.DataField;
import com.athena.component.runner.RunnerService;

/**
 * <p>Title:数据交换平台</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
public class TxtRowParser extends AbstractRowParser<String> implements RowParser<String>{
	protected static Logger logger = Logger.getLogger(TxtRowParser.class);	//定义日志方法

	public TxtRowParser(DataParserConfig config,
			DataWriter dataWriter,
			RunnerService runnerService){
		super(config,dataWriter,runnerService);
	}
	
	/**
	 * 清除任务集合
	 */
	public void clearCommands(){
		this.commands.clear();
	}
	
	@Override
	protected ParseLineCommand<String> createParseRowCommand(
			DataWriter dataWriter, int rowIndex, String rowObject) {
		return new ParseTxtLineCommand(dataWriter,rowIndex,rowObject);
	}
	/**
	 * 
	 */
	class ParseTxtLineCommand extends ParseLineCommand<String>{

		public ParseTxtLineCommand(
				DataWriter dataWriter,
				int rowIndex, String rowObject) {
			super(dataWriter, rowIndex, rowObject);
		}
		
		public Record buildRecord(){
			String line = rowObject.toString();
			Record record = new Record();
			String quote = config.getReaderConfig().getQuote();
			if(quote!=null&&!quote.equals("")){
				line = line.replace(quote, "");
			}
			int lineLength =  countString(line);
			DataField[] dataFileds = config.getDataFields();
			for(DataField dataField:dataFileds){
				int start = dataField.getStart();
				int end = dataField.getStart()+dataField.getLength();
				String strValue = "";
				if(start<lineLength){
					strValue =  cutString(line,start, Math.min(end,lineLength));
				}
				try {
					record.put(dataField.getWriterColumn(),convertValue(dataField,strValue.trim()));
				} catch (ParserException e) {
					logger.error(e.getMessage());
				}
			}
			
			String table = config.getWriterConfig().getTable();
			boolean hasMiddle = false; //是否有中间表  
			if(table!=null){
				if(table.contains("${")){
					String[] strs = SpaceFinal.replaceSql(table).split("\\.");
					if(strs.length>1){
						table = strs[1];
					}
				}
				hasMiddle = table.toLowerCase().startsWith("in");
			}
			if("txt".equals(config.getGroupConfig().getReader()) && hasMiddle){ //必须有中间表才去填上 文件路径 文件名称两个字段
				//添加 文件路径 文件名称
					record.put("WENJMC", ((DbDataWriter)getDataWriter()).getWENJMC());
					record.put("WENJLJ", ((DbDataWriter)getDataWriter()).getWENJLJ());
			}
			return record;
		}
		
		/**
		 * 截取字符串,中文当做2位处理
		 * @param str 原字符串
		 * @param start 开始下标
		 * @param end 结束下标
		 * @return 截取的字符串
		 */
		private String cutString(String str,int start,int end){
	        int length = end - start;//计算长度
	        byte[] dest = new byte[length];//新建byte数组
	        try {
	        	//拷贝byte数组
	        	System.arraycopy(str.getBytes("GBK"), start, dest, 0, length);
	        	 return new String(dest,"GBK");//返回截取字符串
			} catch (Exception e) {
				return str.substring(start, end);
			}
		}
		
		/**
		 * 计算字符串长度,中文算2位
		 * @param str 字符串
		 * @return 长度
		 */
		private int countString(String str){
			try {
				return str.getBytes("GBK").length;
			} catch (Exception e) {
				return str.length();
			}
		}

	}
}


