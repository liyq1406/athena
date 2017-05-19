/**
 * 
 */
package com.athena.xqjs.module.kdys.service;

import com.toft.core3.container.annotation.Component;
import com.toft.mvc.ActionSupport;

/**
 * @author dsimedd001
 *
 */
@Component
public class KdysAction  extends ActionSupport {
	public String execute() {
		return "select";
	}
}
