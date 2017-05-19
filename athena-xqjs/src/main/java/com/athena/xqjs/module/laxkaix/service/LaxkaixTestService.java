/**
 * 
 */
package com.athena.xqjs.module.laxkaix.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.laxkaix.Kaixjhmx;
import com.athena.xqjs.entity.laxkaix.Laxjhmx;
import com.athena.xqjs.entity.laxkaix.LinsXuq;
import com.toft.core3.container.annotation.Component;

/**
 * @author dsimedd001
 * 
 */
@Component
public class LaxkaixTestService extends BaseService {
	public void deleteLinsTclj() {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("laxkaixTest.deleteLinstclj");
	}

	/**
	 * 
	 */
	public void deleteLinsXuq(LinsXuq xuq) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("laxkaixTest.deleteLinsXuq", xuq);
	}

	/**
	 * @param object
	 * @return
	 */
	public List<Map<String, Object>> getSumLingjslByTcTest() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("laxkaixTest.getLinsTcljslTest");
	}

	public LinsXuq getLinsXuq(LinsXuq xuq) {
		return (LinsXuq) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("laxkaixTest.getLinsXuq", xuq);
	}

	public Laxjhmx getLaxjhmx(Laxjhmx laxjhmx) {
		return (Laxjhmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("laxkaixTest.getLaxjhmx", laxjhmx);
	}

	/**
	 * @param kaixjhmx
	 */
	public void deleteKaixjhmxByKaijhNo(Kaixjhmx kaixjhmx) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("laxkaixTest.deleteKaixjhmxByKaijhNo",
				kaixjhmx);

	}
}
