package com.athena.component.test;

import java.util.ArrayList;
import java.util.List;

public class RowValues {
	
	List<Object> values; 

	public RowValues(int size) {
		values = new ArrayList<Object>(size);
	}

	public void addValue(String content) {
		values.add(content);
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

}
