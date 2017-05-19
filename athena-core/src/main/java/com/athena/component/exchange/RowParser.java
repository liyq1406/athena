/**
 * 
 */
package com.athena.component.exchange;

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
public interface RowParser<T> {

	void parse(int i, T line);

	void complete();

}
