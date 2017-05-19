/**
 * 
 */
package com.athena.component.exchange.field;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import com.athena.component.exchange.ParserException;

/**
 * <p>Title:</p>
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
public class NumberFieldFormat implements FieldFormat<Number>{
	private String format;//自定义的格式
	
	private String numberFormat;//用于DecimalFormat的格式串
	
	private int numberLength;//数字的字符总长度
	
	private int dotLength;//数字的小数点长度
	
	public NumberFieldFormat(String format) {
		super();
		this.format = format;
		this.numberFormat = createNumberFormat();
	}
	
	/**
	 * #.0000
	 * @return
	 */
	private String createNumberFormat() {
		if(format.startsWith("number(")){
			String[] formatSplit = format.split(",");
			numberLength = Integer.parseInt(formatSplit[0].replace("number(",""));
			dotLength = Integer.parseInt(formatSplit[1].replace(")",""));
			return "#."+buildDotFormat();
		}
		return format;
	}
	
	private String buildDotFormat(){
		StringBuffer buf = new StringBuffer();
		for(int i=0;i<dotLength;i++){
			buf.append("#");
		}
		return buf.toString();
	}
	
	/* 
	 * @see com.athena.component.exchange.field.FieldFormat#parse(java.lang.String)
	 */
	@Override
	public Double parse(String source) {
		NumberFormat  nf = new DecimalFormat(numberFormat);
		if(!format.equals(numberFormat)){
			source = formatSource(source);
		}
		//特殊格式定义number(10,2)
		try {
			return nf.parse(source).doubleValue();
		} catch (ParseException e) {
			throw new ParserException("数字格式不符："+source);
		}
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	private String formatSource(String source) {
		int sourceLength = source.length();
		String prefix ="";
		if(source.startsWith("+")){
			sourceLength--;
			source = source.substring(1);
		}else if(source.startsWith("-")){
			sourceLength--;
			prefix = "-";
		} 
		if(sourceLength!=numberLength)throw new ParserException("数字长度不符："+source);
		return source.substring(0,prefix.length()+numberLength-dotLength)+"."+source.substring(prefix.length()+numberLength-dotLength);
	} 
}
