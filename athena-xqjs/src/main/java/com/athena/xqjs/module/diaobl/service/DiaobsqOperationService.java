package com.athena.xqjs.module.diaobl.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.diaobl.Diaobsq;
import com.athena.xqjs.entity.diaobl.DiaobsqExport;
import com.athena.xqjs.entity.diaobl.Diaobsqmx;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：DiaobsqOperationService
 * <p>
 * 类描述：调拨令申请CRUD操作
 * </p>
 * 创建人：Niesy
 * <p>
 * 创建时间：2011-11-22
 * </p>
 * 
 * @version
 * 
 */
@Component
public class DiaobsqOperationService extends BaseService {

	/**
	 * 
	 * 调拨令查询主页面
	 * 
	 * @param bean
	 * @return map
	 * @author Niesy
	 * @date
	 */
	public Map<String, Object> select(Pageable page, Map<String, String> params) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("diaobl.queryDiaobsq", params, page);
	}

	/**
	 * 
	 * 调拨令查询页面上选择一条状态未生效的调拨申请，改调拨申请的状态
	 * 
	 * @param bean
	 * @return int
	 * @author Niesy
	 * @date
	 */
	@Transactional
	public String updateCancle(List<Diaobsq> ls) {
		String flag = Const.FLAG_TRUE;
		for (int i = 0; i < ls.size(); i++) {
			// 循环取消每一条未审核的申请
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobl.updateCancle",
					ls.get(i));
			if (count == 0) {
				flag = Const.FLAG_FALSE;
				break;
			}
		}
		return flag;
	}

	/**
	 * 
	 * 调拨申请单号下的增加
	 * 
	 * @param bean
	 * @return 主键
	 * @author Niesy
	 * @date
	 */
	@Transactional
	public boolean insert(Diaobsqmx bean) {
		// 序号加1
		Integer xuh = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobl.select_sqxuh", bean);
		// 序号为空，初始为零
		int xuH = xuh == null ? 0 : xuh;
		bean.setXuh(xuH + 1);
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobl.insertdiaobsqmx", bean);
		return count > 0;
	}

	/**
	 * 
	 * 调拨申请单号下的修改
	 * 
	 * @param bean
	 * @return 主键
	 * @author Niesy
	 * @date
	 */
	@Transactional
	public String update(List<Diaobsqmx> ls) {
		String flag = Const.FLAG_TRUE;
		for (int i = 0; i < ls.size(); i++) {
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobl.doupdate", ls.get(i));
			if (count == 0) {
				flag = Const.FLAG_FALSE;
				break;
			}
		}
		return flag;
	}

	public String checkDiaobmx(Diaobsqmx mx){
		String r = "";
		mx.setZhuangt(Const.DIAOBL_ZT_APPLYING);
		int checkCount = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobl.get_mxZhuangtxiug", mx);
		if(checkCount==0){
			r = "零件:"+mx.getLingjbh()+"状态不为未审核状态";
		}
		return r;
	}
	
	/**
	 * 
	 * 调拨申请单号下的修改及打印，修改版次号
	 * 
	 * @param bean
	 * @return 主键
	 * @author Niesy
	 * @date
	 */
	public String updateBanc(Diaobsq bean) {
		// 得到版次号
		String banc = bean.getBanc();
		// 转成整形
		int number = Integer.parseInt(banc) + 1;
		// 前面补0，长度为4位
		banc = String.format("%04d", number);
		bean.setBanc(banc);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobl.update_banc", bean);
		return banc;
	}

	/**
	 * 
	 * 调拨申请单号下的删除
	 * 
	 * @param bean
	 * @return 主键
	 * @author Niesy
	 * @date
	 */
	@Transactional
	public String doDelete(List<Diaobsqmx> ls) {
		String flag = Const.FLAG_TRUE;
		for (int i = 0; i < ls.size(); i++) {
			int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("diaobl.deletediaobsqmx",
					ls.get(i));
			if (count == 0) {
				flag = Const.FLAG_FALSE;
				break;
			}
		}
		return flag;
	}

	/**
	 * 查询调拨申请明细
	 * 
	 * @param bean
	 * @return diaobsqmx
	 */
	public Diaobsqmx queryDiaobsqmx(Diaobsqmx bean) {
		return (Diaobsqmx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("diaobl.selectDiaobsqmx", bean);
	}

	
	/**
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DiaobsqExport> queryDiaobsqExport(Map<String,String> paramMap) 
	{
		
		if(null != paramMap && StringUtils.isNotBlank(paramMap.get("ids")))
		{
			String ids = paramMap.get("ids");
			List<String> idArray = (List<String>) Arrays.asList(ids.split(","));
			int index = 0 ,max = idArray.size() ; 
			StringBuffer param = new StringBuffer();
			for (String id : idArray)
			{
				param.append("'").append(id).append("'");
				if(index < max - 1)
				{
					param.append(",");
				}
				index ++;
			}
			String paramStr =  param.toString();
			if(StringUtils.isNotBlank(paramStr))
			{
				paramMap = new HashMap<String, String>();
				paramMap.put("ids", param.toString());
			}
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("diaobl.exportDiaob", paramMap);
	}
	
}
