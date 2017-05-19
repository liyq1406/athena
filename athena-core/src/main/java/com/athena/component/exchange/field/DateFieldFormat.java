/**
 * 
 */
package com.athena.component.exchange.field;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class DateFieldFormat
	implements FieldFormat<java.util.Date> {

	public final static String MAX_DATE_STR = "99999999"; 
	
	private String format;
	
	public DateFieldFormat(String format) {
		super();
		this.format = format;
	}

	@Override
	public Date parse(String source) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			if(MAX_DATE_STR.equals(source)){
				source = "20991230";
			}
			return sdf.parse(source);
		} catch (ParseException e) {
			throw new ParserException("日期格式解析错误!");
		}
	}

}
