package com.auto.testcases.login;

import java.io.IOException;

import org.junit.Test;

import com.auto.common.PublicVerify;
import com.auto.common.Steup;

/**
 * 登陆登出
 * @author lizhi
 * @date 2012-8-9
 */
public class Login extends PublicVerify {

	public Login() throws IOException {
		super();
	}
	
	/**
	 * 登陆case
	 * @throws Exception
	 */
	@Test
	public void loginCase() throws Exception {
		//登陆
		PublicVerify p = new PublicVerify();
		p.loginSystem(Steup.url.get("zbc"), Steup.username.get("zbc_POA"), Steup.password,
				Steup.usercenter.get("zbc_POA"));
		//assertTrue(selenium.isVisible(""));
	}
}
