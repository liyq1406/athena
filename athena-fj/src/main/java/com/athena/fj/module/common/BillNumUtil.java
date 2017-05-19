package com.athena.fj.module.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.entity.Domain;
import com.athena.component.service.BaseService;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
 
import com.athena.db.ConstantDbCode;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-22
 * @time 下午01:49:38
 * @description 单拮号
 */
@Component
public class BillNumUtil extends BaseService<Domain> {

	public final static int BILL_YCJHH = 1;  //要车计划号
	public final static int BILL_PZDH = 2;   //配载单号
	public final static int BILL_ZCDH = 3;   //装车单号
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午01:53:58
	 * @param uc
	 *            用户中心
	 * @param lsh
	 *            流水号位数
	 * @param djLx
	 *            单拮类型
	 * @param pre
	 *            前缀
	 * @return String
	 * @description 生成单拮编号
	 */
	public String createDJNum(String uc, int lsh, int djLx, String pre ) {

		// 异常
		if (uc == null || uc.trim().length() <= 1){
			throw new ServiceException("用户中心为空,或者长度不正确!");
		}
		
		//前缀
		String prefix = "";
		prefix = (pre == null?"":pre);
		// 得到用户中心第二位
		String first = uc.substring(1, 2);
		
		// 流水号
		String lshN = getlshN(djLx, uc);

		
		// 如果存在流水号
		if (lshN.length() != 0) {
			String tmp_0 =  lshN.substring(lshN.length()-lsh) ;
			int  i = Integer.parseInt(tmp_0)+1 ;
			tmp_0  = String.valueOf(i) ;
			
			while(tmp_0.length()<lsh)
			{
				tmp_0 = "0"+tmp_0;
			}
			return (lshN.substring(0, lshN.length()-lsh)+tmp_0);
		}
		else {
			// 不存在流水号
			while (lshN.length() < (lsh - 1)) {
				lshN += "0";
			}
			lshN = lshN + "1";
			return (prefix+first + lshN);

		}
	}
	
	/**
	 * 得到流水单据号
	 * @param djLx
	 * @param uc
	 * @return String流水号
	 */
	@SuppressWarnings("unchecked")
	public String getlshN(int djLx,String uc)
	{
		// 封装用户中心
		Map<String, String> map = new HashMap<String, String>();
		map.put("UC", uc);
		//编号
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		// 流水号
		String lshN = ""; 
		switch (djLx)
		{
			// 要车计划号
			case BILL_YCJHH:
				// 得到最大的单拮号
			   list = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("baseDao.selectYaoCJHH", map);
				if (list.size() == 1&&list.get(0).get("DJBH")!=null) {
					// 设置 流水号
					lshN = list.get(0).get("DJBH");
				}
				break;
			// 配载计划单号
			case BILL_PZDH:
				// 得到最大的单拮号
				list = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("baseDao.selectPeiZaiCJHH", map);
				if (list.size() == 1&&list.get(0).get("DJBH")!=null) {
					// 设置 流水号
					lshN = list.get(0).get("DJBH");
				}
				break;
			// 装车单号
			case BILL_ZCDH:
				// 得到最大的单拮号
				list = this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("baseDao.selectZhuangCCJHH", map);
				if (list.size() == 1&&list.get(0).get("DJBH")!=null) {
					// 设置 流水号
					lshN = list.get(0).get("DJBH");
				}
				break;
		}
		
		return lshN;
	}

}
