package com.athena.xqjs.anxorder;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.module.anxorder.service.ChushtjJsService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 
 * @author 李智
 * 
 */
@TestData(locations = { "classpath:testData/anxorder/ChushtjJs.xls" })
public class ChushtjJsTest extends AbstractCompomentTests {
	@Inject
	private ChushtjJsService chushtjJsService;
	@Inject
	private AbstractIBatisDao baseDao;

	@Test
	public void calculateForcdmdTest() throws Exception {
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxmaoxqTest.updateYunssk");

		Maoxq maoxq = new Maoxq();
		maoxq.setXuqbc("A010");

		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");

		//boolean bool = this.chushtjJsService.calculateMain(maoxq, loginUser, "1", "97W","");
		//org.junit.Assert.assertEquals(true, bool);
	}

	@Test
	public void calculateForc1m1Test() throws Exception {
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxmaoxqTest.updateAnxmaoxq");
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxmaoxqTest.updateYunssk");

		Maoxq maoxq = new Maoxq();
		maoxq.setXuqbc("A011");

		LoginUser loginUser = new LoginUser();
		loginUser.setUsercenter("UW");

		//boolean bool = this.chushtjJsService.calculateMain(maoxq, loginUser, "2", "97W","");
		//org.junit.Assert.assertEquals(true, bool);
	}
}
