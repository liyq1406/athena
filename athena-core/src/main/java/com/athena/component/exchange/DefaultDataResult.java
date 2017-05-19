/**
 * 
 */
package com.athena.component.exchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.toft.core3.container.annotation.Component;


/**
 * <p>Title:数据交换组件</p>
 *
 * <p>Description:缓存模式实现的数据交换结果集</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0
 */
@Component
public class DefaultDataResult implements DataResult {
	
	private final static Log log = LogFactory.getLog(DefaultDataResult.class);
	
	private String prefix;
	
	public DefaultDataResult(){
		prefix = UUID.randomUUID().toString().replace("_", "");
	}
	
	//TODO 使用缓存服务替换
	private static Map<String,Record> recordsCache = 
		Collections.synchronizedMap(new HashMap<String,Record>());
	
	private int size = 0;
	
	public void addRecord(String key,Record record){
		if(record==null)return;
		String rowIndexKey = this.buildIndexKey(size);
		String recordKey = buildKey(key);
		
		if(recordsCache.containsKey(recordKey)){
			log.debug("重复的记录【"+recordKey+"】忽略写入。");
			return;
		}
		
		recordsCache.put(recordKey,new KeyRecord(rowIndexKey));
		recordsCache.put(rowIndexKey, new KeyRecord(key,record));
		size++;
	}
	
	/**
	 * 删除记录
	 * @param key
	 */
	public void removeRecord(String key){
		String recordKey = buildKey(key);
		
		Record keyRecord = recordsCache.get(recordKey);
		if(keyRecord!=null){
			recordsCache.remove(keyRecord.getString(KeyRecord.RECORD_KEY_ATTR));
			size--;
		}
		recordsCache.remove(recordKey);//
	}
	
	public List<Record> getRecords() {
		return getRecords(0,size);
	}
	
	public List<Record> getRecords(int from,int to) {
		if(from<0)from=0;
		if(to>=size)to = size-1;
		List<Record> records = new ArrayList<Record>();
		for(int i=from;i<=to;i++){
			records.add(recordsCache.get(buildIndexKey(i)));
		}
		return records;
	}

	public Record getRecord(String key) {
		String recordKey = buildKey(key);
		Record keyRecord = recordsCache.get(recordKey);
		if(keyRecord!=null){
			return recordsCache.get(keyRecord.getString(KeyRecord.RECORD_KEY_ATTR));
		}
		return null;
	}
	
	public int size(){
		return size;
	}
	
	public void clear(){
		Iterator<Record> iter = this.iterator();
		while(iter.hasNext()){
			iter.remove();
		}
		size = 0;
	}

	/**
	 * 生成key
	 * @param key
	 * @return
	 */
	private String buildKey(String key) {
		return prefix+"_r_"+key;
	}
	
	/**
	 * 生成key
	 * @param key
	 * @return
	 */
	private String buildIndexKey(int index) {
		return prefix+"_i_"+index;
	}
	
	public Iterator<Record> iterator() {
		return new Itr();
	}


	private class Itr implements Iterator<Record> {
		int cursor = 0;
		
		public boolean hasNext() {
			return cursor!=size();
		}

		public Record next() {
			Record record = recordsCache.get(buildIndexKey(cursor));
			if(record!=null){
				cursor++;
				return record;
			}
			return null;
		}

		public void remove() {
			Record record = recordsCache.get(buildIndexKey(size-1));
			//TODO  约束条件
			if(record!=null){
				removeRecord(record.getString(KeyRecord.RECORD_KEY_ATTR));
			}
		}
	}
}
