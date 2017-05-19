/**
 * 
 */
package com.athena.component.test;

import java.io.File;
import java.util.List;

/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class SqlTestDataGenerator extends AbstractTestDataGenerator implements TestDataGenerator {

	public SqlTestDataGenerator(String[] locations) {
		super(locations);
	}

	@Override
	public void generate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<InsertParameter> generateInsertParameter(File file) {
		// TODO Auto-generated method stub
		return null;
	}

}
