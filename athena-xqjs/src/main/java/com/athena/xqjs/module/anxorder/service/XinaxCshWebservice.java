package com.athena.xqjs.module.anxorder.service;

import java.math.BigDecimal;

import javax.jws.WebService;

import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.entity.anxorder.Xiaohcxhdmb;

/**
 * 新按需计算webservice接口
 * @author zbb
 *
 */
@WebService
public interface XinaxCshWebservice {

	/**
	 * 从执行层ck_zhengcgd表查出整车序号
	 */
	public String queryZhengcxh(Anxjscszjb anxjscszjb,String liush);
	
	/**
	 * 从执行层ddbh_xiaohcxhdmb表查出tangc和起始流水号
	 */
	public Xiaohcxhdmb queryXiaohcxhdmb(Anxjscszjb anxjscszjb,String zhengcxh);
	
	/**
	 * 从执行层ddbh_caifjg_cs表查出整车流水号在起始流水号和zhengcgd的整车序号之间的lingjsl之和
	 */
	public BigDecimal querySppvxhl(Anxjscszjb anxjscszjb,String kaislsh,String zhengcxh,String tangc);
	
}
