/**
 * 
 */
package com.athena.component.exchange.field;

/**
 * @author Administrator
 *
 */
public class DataField {
	private String writerColumn;//输入字段
	
	private String readerColumn;//输出字段
	
	private String caption;//字段描述
	
	private boolean update;//是否更新
	
	private int start;//起始位置
	
	private int length;//长度
	
	private String type;//数据类型
	
	private String format;//数据格式
	
	//改造 --> 输出：支持输出 生成多文件
	private String separate;// 分隔符
	
	private String separate_size;// 填充分隔符的位置 before在字段前；after在字段后

	private boolean isParam;  //是否为参数值，不作入库字段
	
	public boolean getIsParam() {
		return isParam;
	}

	public void setIsParam(boolean isParam) {
		this.isParam = isParam;
	}

	public String getSeparate() {
		return separate;
	}

	public void setSeparate(String separate) {
		this.separate = separate;
	}

	public String getSeparate_size() {
		return separate_size;
	}

	public void setSeparate_size(String separate_size) {
		this.separate_size = separate_size;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getWriterColumn() {
		return writerColumn;
	}

	public void setWriterColumn(String writerColumn) {
		this.writerColumn = writerColumn;
	}

	public String getReaderColumn() {
		return readerColumn;
	}

	public void setReaderColumn(String readerColumn) {
		this.readerColumn = readerColumn;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

}
