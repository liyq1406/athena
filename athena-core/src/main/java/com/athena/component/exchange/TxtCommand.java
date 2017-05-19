package com.athena.component.exchange;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.field.DataField;
import com.athena.component.runner.Command;

public class TxtCommand<T> extends Command<Object> {
	protected final Log logger = LogFactory.getLog(getClass());//定义日志方法
	private OutputStreamWriter out;
	private DataParserConfig dataParserConfig;
	private T rowObject; //行结果
	
	private static String AFTER = "after";
	
	private static String LineSign = "\\n"; //换行符
	
	//返回的结果集
	private HashMap<String,Object> result = new HashMap<String, Object>();

	public TxtCommand(OutputStreamWriter out, DataParserConfig dataParserConfig,T rowObject) {
		super();
		this.out = out;
		this.dataParserConfig = dataParserConfig;
		this.rowObject = rowObject;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute() {
		if (rowObject instanceof Map) {
			Map rowObjectMap = (Map) rowObject;
			StringBuffer sb = new StringBuffer();
			
			//处理要输出的的字符信息
			DataField[] df = dataParserConfig.getDataFields();
			for(int i=0;i<df.length;i++){
				String writerColumn = df[i].getWriterColumn(); //列名
				int length = df[i].getLength(); //列长度
				String separate = df[i].getSeparate(); //分隔符
				String separate_size = df[i].getSeparate_size(); //分隔符的位置
				
				//作为字符串处理--- null
				String columnValue;	
				Object columnObject = rowObjectMap.get(writerColumn);				
				columnValue = columnObject!=null?(columnObject.toString()): "";
				
				//创建一个字段
				createField(sb,columnValue,length,separate,separate_size);				
			}
			
			//生成换行符
			if(sb.length()>0){
				sb.append("\n");
			}
			
			//out对象输出
			try {
				out.write(sb.toString());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	/**
	 * 生成一个字段 
	 * 	1：如果columnValue为空，则输出length制定长度的字符；
	 * 	2：如果columenValue不为空，则输出制定长度的字符；不足长度的补separate；
	 * 	3：默认separate是空格；separate_size是 after;
	 * @param sb
	 * @param columnValue
	 * @param length
	 * @param separate
	 * @param separate_size
	 */
	private void createField(StringBuffer sb, String columnValue, int length,
			String separate, String separate_size) {
		if(columnValue!=null){
			//输出字段不为空
			String charset = dataParserConfig.getWriterConfigs()[0].getEncoding();
			int value_length = 0;
			try {
				value_length = columnValue.getBytes(charset).length;
			} catch (UnsupportedEncodingException e) {
				logger.info(e.getMessage());
			}
			if(length>0){
				if(value_length>=length){
					//字段长度比要输出的长度长 则从左边截取	
					sb.append(makeStrByLength(columnValue,length,charset));
				}else{
					if(separate_size!=null){
						//填充符位置不为空
						if(separate!=null){
							//填写了 分隔符
							fillSeparate(length-value_length,separate_size,sb,separate,columnValue);
						}else{
							//没有填写分隔符  则默认补空格
							fillSeparate(length-value_length, separate_size, sb, " ", columnValue);
						}
					}else{
						//填充符位置为空 默认在后面添加
						if(separate!=null){
							//填写了 分隔符
							fillSeparate(length-value_length,AFTER,sb,separate,columnValue);
						}else{
							//没有填写分隔符  则默认补空格
							fillSeparate(length-value_length, AFTER, sb, " ", columnValue);
						}
					}
				}
			}else{
				//没有填写长度  就默认输出 columnValue
				sb.append(columnValue);
			}
		}else{
			//输出字段为空
			if(length>0){
				if(separate!=null){
					//填充符不为空
					for(int i=0;i<length;i++){
						sb.append(separate);
					}
				}else{
					//填充符为空 默认用空格填充
					for(int i=0;i<length;i++){
						sb.append(" ");
					}
				}
			}
		}
		
	}
	
	/**
	 * 如果数据库取出的字段比要生成的字段长，则按照字节来截取
	 * @param columnValue
	 * @param length
	 * @param charset
	 * @return
	 */
	private String makeStrByLength(String columnValue,int length,String charset){
		String result = null;
		try {
			byte[] bys = columnValue.getBytes(charset);
			byte[] bs = new byte[length];
			for(int i=0;i<length;i++){
				bs[i] = bys[i];
			}			
			result = new String(bs,charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		
		
		return result;
	}
	
	/**
	 * 填写分隔符
	 * @param i 填写的位数
	 * @param separate_size 分隔符位置
	 * @param sb 
	 * @param separate  分隔符
	 * @param columnValue  此字段的值
	 */
	private void fillSeparate(int i, String separate_size, StringBuffer sb,
			String separate,String columnValue) {
		
		if(AFTER.equals(separate_size)){
			//在后面添加
			sb.append(columnValue);
			for(int j=0;j<i;j++){
				//为了支持换行 
				if(LineSign.equals(separate)){
					sb.append("\n");
					break;
				}else{
					sb.append(separate);
				}			
			}
		}else{
			//在前面添加
			for(int j=0;j<i;j++){
				//为了支持换行 
				if(LineSign.equals(separate)){
					sb.append("\n");
					break;
				}else{
					sb.append(separate);
				}
			}
			sb.append(columnValue);
		}
		
	}
	
	/**
	 * 不返回 结果
	 */	
	@Override
	public Object result() {	
		return result;
	}

}
