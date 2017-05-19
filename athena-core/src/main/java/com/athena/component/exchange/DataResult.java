/**
 * 
 */
package com.athena.component.exchange;

import java.util.Iterator;
import java.util.List;

/**
 * @author Administrator
 *
 */
public interface DataResult {
	/**
	 * 获取数据结果集的所有记录
	 * @return
	 */
	public List<Record> getRecords();
	
	/**
	 * 获取记录从from到to
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Record> getRecords(int from,int to);
	
	/**
	 * 根据关键字段的值获取记录,如果没有设置关键字，使用记录行索引作为关键字
	 * @param key
	 * @return
	 */
	public Record getRecord(String key);
	
	/**
	 * 添加记录
	 * @param key
	 * @param record
	 */
	public void addRecord(String key,Record record);
	
	/**
	 * 
	 * @return
	 */
	public Iterator<Record> iterator();
	
	public int size();
	
	/**
	 * 清空
	 */
	public void clear();
	
	
}
