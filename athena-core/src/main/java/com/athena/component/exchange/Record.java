/**
 * 
 */
package com.athena.component.exchange;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class Record {

	/**
	 * 存放exchange-config-xxx.xml中需要插入到数据库的字段
	 */
    private Map<String,Object> value = new HashMap<String,Object>();
    /**
     * 存放exchange-config-xxx.xml中字段加属性isParam的值，该字段值用于参数获取，不作插入用
     */
    private Map<String,Object> param = new HashMap<String,Object>();
    /**
     * 行数
     */
    private int lineNum;

    /**
     * 获取要插入到数据库的字段Map对象
     */
    public Map<String,Object> getValue() {
        return value;
    }
    /**
     * 获取要作为参数的Map对象
     */
    public Map<String,Object> getParam() {
        return param;
    }

    public Object get(String key) {
        return value.get(key);
    }

    public Object put(String key, Object v) {
        return value.put(key,v);
    }
    public void putAll(Map<String, Object> m){
    	//清空value缓存值，将最新的record赋给Value
    	value.clear();
        value.putAll(m);
    }
    public Object getParamValue(String key) {
        return param.get(key);
    }

    public Object putParamObject(String key, Object v) {
        return param.put(key,v);
    }

	public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getLineNum() {
        return lineNum;
    }
	/**
	 * 
	 */
	private static final long serialVersionUID = -5465619992797312284L;
	
	/**
	 * @param key
	 * @return
	 */
	public String getString(String key){
		Object value = this.get(key);
		return value==null?"":value.toString();
	}
}

	class KeyRecord extends Record{
		public static final String RECORD_KEY_ATTR = "key";
		/**
		 * 
		 */
		private static final long serialVersionUID = -6074903019059895090L;
		
		public KeyRecord(String key){
			this.put(RECORD_KEY_ATTR, key);
		}

		public KeyRecord(String key, Record record) {
			this.putAll(record.getValue());
			this.put(RECORD_KEY_ATTR, key);
		}
}
