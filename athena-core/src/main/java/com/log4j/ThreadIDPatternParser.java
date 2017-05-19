package com.log4j;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;


public class ThreadIDPatternParser extends PatternParser {
	public ThreadIDPatternParser(String pattern) {
		 super(pattern);
	}
	
	/**
	 * 重写finalizeConverter，对特定的占位符进行处理，T表示线程ID占位符
	 */
	 @Override
	 protected void finalizeConverter(char c) {
		 if ( c == 'T' ) {
			 this.addConverter(new ExTPatternConverter(this.formattingInfo));
		 }else if(  c == 'V' ){
			 this.addConverter(new ExVPatternConverter(this.formattingInfo));
		 }else {
			 super.finalizeConverter(c);
		 }
	 }
	 
	 /**
	  * 显示线程id的内部处理类
	  * @author Administrator
	  *
	  */
	 private static class ExTPatternConverter extends PatternConverter {
		 public ExTPatternConverter(FormattingInfo fi) {
			 super(fi);
		 }
		 
		 /**
		 * 当需要显示线程ID的时候，返回当前调用线程的ID
		 */
		 @Override
		 protected String convert(LoggingEvent event) {
			 return String.valueOf(Thread.currentThread().getId());
		 }
	 }
	 
	 /**
	  * 显示JVM内存情况的类
	  * @author Administrator
	  *
	  */
	 private static class ExVPatternConverter extends PatternConverter {
		 public ExVPatternConverter(FormattingInfo fi) {
			 super(fi);
		 }
		 
		 /**
		 * 当需要显示JVM内存情况
		 */
		 @Override
		 protected String convert(LoggingEvent event) {
			 
			 int total =(int)(Runtime.getRuntime().totalMemory()/1024); 
			 int free=(int)(Runtime.getRuntime().freeMemory()/1024); 
			 
			 int use = total - free;
			 
			 return String.valueOf("JVM Memory:total{" + total + "};free{" +free+ "};already use{"+use+"}");
		 }
	 }

}
