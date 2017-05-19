package com.athena.ckx.module.xuqjs.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.athena.ckx.entity.xuqjs.Baoz;
import com.athena.ckx.entity.xuqjs.CkxShiwtx;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

/**
 * 包装
 * @author denggq
 * 2012-3-19
 */
@Component
public class CkxShiwtxService extends BaseService<Baoz>{

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}
	public static String TIXLX_LINGJ    = "1";//新零件
	public static String TIXLX_GONGYS   = "2";//新供应商
	public static String TIXLX_LINGJGYS = "3";//新零件-供应商
	public static String TIXLX_XIAOHD   = "4";//新消耗点
	public static String TIXLX_LINGJXHD = "5";//新零件-消耗点
	
	public String update(String usercenter,String tixlx,String guanjz1,String guanjz2,String zhuangt){
		CkxShiwtx bean = getBean(usercenter,tixlx,guanjz1,guanjz2,zhuangt);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxShiwtx",bean);	//增加数据库
		return null;
	}
	private CkxShiwtx getBean(String usercenter,String tixlx,String guanjz1,String guanjz2,String zhuangt){
		CkxShiwtx bean = new CkxShiwtx();
		bean.setUsercenter(usercenter);
		bean.setTixlx(tixlx);
		bean.setGuanjz1(guanjz1);
		bean.setGuanjz2(guanjz2);
		bean.setZhuangt(zhuangt);
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
		try {
			bean.setJiejsj(sf.format(date));
		} catch (Exception e) {
			throw new ServiceException("时间格式转换异常");
		}
		return bean;
	}
}
