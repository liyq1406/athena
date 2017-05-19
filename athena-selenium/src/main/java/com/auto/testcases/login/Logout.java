package com.auto.testcases.login;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

import com.auto.common.PublicVerify;

/**
 * 登出
 * @author lizhi
 * @date 2012-8-9
 */
public class Logout extends PublicVerify {

	public Logout() throws IOException {
		super();
	}
	
	/**
	 * 登出case
	 * @throws Exception
	 */
	@Test
	public void logoutCase() {
		logoutSystem();
	}
}
