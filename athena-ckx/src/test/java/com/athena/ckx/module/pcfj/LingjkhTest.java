package com.athena.ckx.module.pcfj;



import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.paicfj.Ckx_lingjkh;
import com.athena.ckx.module.paicfj.service.Ckx_lingjkhService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class LingjkhTest extends AbstractCompomentTests {

	@Inject
	private Ckx_lingjkhService lingjkhService;
	@Test
	public void save(){
		LoginUser user = new LoginUser();
		user.setUsercenter("UW");
		user.setUsername("root");
		Ckx_lingjkh lingjkh=new Ckx_lingjkh();
		lingjkh.setUsercenter("UL");
		
		lingjkh.setLingjbh("lj001");
		lingjkh.setKehbh("kh000001");
		lingjkh.setKehljh("khljh001");
		lingjkh.setKehljmc("2222");
		lingjkh.setShengxrq("2012-03-12");
		lingjkh.setShixrq("2012-04-12");
		lingjkhService.save(lingjkh, 1, user);
		lingjkhService.save(lingjkh, 2, user);
		lingjkhService.remove(lingjkh);
	}
	
}
