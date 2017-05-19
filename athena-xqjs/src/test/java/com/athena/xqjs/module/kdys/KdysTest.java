/**
 * 
 */
package com.athena.xqjs.module.kdys;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.util.date.DateUtil;
import com.athena.xqjs.entity.laxkaix.TC;
import com.athena.xqjs.module.kdys.service.KdysService;
import com.toft.core3.container.annotation.Inject;

/**
 * @author dsimedd001
 *
 */
@TestData(locations = {"classpath:testData/kdys/kdys.xls"})
public class KdysTest  extends AbstractCompomentTests{
	@Inject
	private KdysService kdysService;
	
	@Test
	public void getKdysInfoTest() throws ParseException{
		String createTime = DateUtil.curDateTime();
		Date createTimeLine = DateUtil.stringToDateYMD(createTime);
		String createTimeLineS = DateUtil.dateFromat(createTimeLine, "yyyy-MM-dd");
		kdysService.getKdysInfo();
		List<TC> list = kdysService.getNewKdysInfo();
		Assert.assertEquals(7, list.size());
		TC tc = list.get(0);
		Assert.assertEquals("AXF00001086", tc.getTcNo());
		Assert.assertEquals("UT01", tc.getUtNo());
		Assert.assertEquals(createTimeLineS, tc.getQiysj());
		Assert.assertEquals("010", tc.getQiyd());
		Assert.assertEquals("UW2", tc.getDinghcj());
		Assert.assertEquals("2012-05-08", tc.getYujddsj());
		Assert.assertEquals("SHG", tc.getZuiswld());
		Assert.assertEquals(createTimeLineS, tc.getDaodwldsj());
		Assert.assertEquals("WK00000001", tc.getLujdm());
		Assert.assertEquals("Z0000", tc.getPapSheetId());
		Assert.assertEquals("AXF00001086", tc.getPapBoxId());
		Assert.assertEquals("999-94560384", tc.getKdysSheetId());
	}
	
}
