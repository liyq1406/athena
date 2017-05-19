package com.athena.xqjs.anxorder;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.module.anxorder.service.AnxOrderService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 
 * @author 李智
 * 
 */
@TestData(locations = { "classpath:testData/anxorder/anxFilterPartten.xls" })
public class AnxFilterParttenTest extends AbstractCompomentTests {
	@Inject
	private AnxOrderService anxOrderService;
	@Inject
	private AbstractIBatisDao baseDao;

	// 按需计算 CD MD
	@Test
	public void anxFilterParttenTest() throws Exception {
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxmaoxqTest.updateYunssk");
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxmaoxqTest.updateCaifsj");
		// this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anxmaoxqTest.updateZykzb");

		this.anxOrderService.anxDataPreparation("SYS");
		LoginUser user = new LoginUser();
		user.setUsername("sys");
		anxOrderService.anxFilterPartten( "SYS");
	}

}
