package com.athena.xqjs.module.zygzbj.service;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.zygzbj.Mafmxq;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：MafmxqService
 * <p>
 * 类描述：MAF库毛需求对比service
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-02-13
 * </p>
 * 
 * @version 1.0
 * 
 */  
@SuppressWarnings({"unchecked","rawtypes"})
@Component
public class MafmxqService extends BaseService {
	
	/**
	 * MAF毛需求对比
	 * @param param 查询参数
	 * @throws Exception 
	 */
	public void maoxqDuib(Map<String, String> param) throws Exception{
		//清空MAF库毛需求对比表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("zygzbj.deleteMafmxq");
		
		//查询毛需求信息
		List<Maoxqmx> listMxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryMafmxq");
		List<Mafmxq> listMafmxq = new ArrayList<Mafmxq>();
		int size = listMxq.size();
		
		for (int i = 0; i < size; i++) {
			
			Maoxqmx maoxq = listMxq.get(i);
			
			param.put("xuqbc", maoxq.getXuqbc());//需求版次
			param.put("lingjbh", maoxq.getLingjbh());//零件编号
			param.put("usercenter", maoxq.getUsercenter());//用户中心
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", param);
			
			param.put("gonghlx", "'97X','97D'");//供货路线,剔除KD件
			//获取零件供应商信息集合
			List<LingjGongys> listLjgys = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryLingjGysList",param);
			for (int j = 0; j < listLjgys.size(); j++) {
				LingjGongys ljgys = listLjgys.get(j);
				param.put("gongysdm", ljgys.getGongysbh());//供应商代码
				
				//查询maf库数据
				List<Mafmxq> listMaf =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryMaf",param);
				//如果maf库存信息为空,直接对比
				if(listMaf == null || listMaf.isEmpty()){
					//maf毛需求对比结果对象
					Mafmxq mafmxq = new Mafmxq();
					mafmxq = duiB(param, mafmxq, BigDecimal.ZERO,CommonFun.getBigDecimal(ljgys.getGongyfe()));
					if(mafmxq == null){
						continue;
					}
					mafmxq.setGongysdm(ljgys.getGongysbh());//供应商编号
					mafmxq.setFene(ljgys.getGongyfe());//供应份额
					mafmxq.setGongysmc(ljgys.getGongsmc());//供应商名称
					mafmxq.setLingjmc(lingj.getZhongwmc());//零件名称
					mafmxq.setJihy(lingj.getJihy());//计划员
					mafmxq.setId(getUUID());
					mafmxq.setUsercenter(maoxq.getUsercenter());//用户中心
					mafmxq.setLingjbh(maoxq.getLingjbh());//零件编号
					listMafmxq.add(mafmxq);
				}else{
					for (int k = 0; k < listMaf.size(); k++) {
						Mafmxq maf = listMaf.get(k);
						Mafmxq mafmxq = new Mafmxq();
						mafmxq.setMafcode(maf.getMafcode());//MAF库代码
						mafmxq.setMafkc(maf.getMafkc());//MAF库库存
						
						//MAF库存
						BigDecimal mafkc = CommonFun.getBigDecimal(mafmxq.getMafkc());
						mafmxq = duiB(param, mafmxq, mafkc,CommonFun.getBigDecimal(ljgys.getGongyfe()));
						if(mafmxq == null){
							continue;
						}
						mafmxq.setId(getUUID());
						mafmxq.setGongysdm(ljgys.getGongysbh());//供应商编号
						mafmxq.setFene(ljgys.getGongyfe());//供应份额
						mafmxq.setGongysmc(ljgys.getGongsmc());//供应商名称
						mafmxq.setLingjmc(lingj.getZhongwmc());//零件名称
						mafmxq.setJihy(lingj.getJihy());//计划员
						mafmxq.setUsercenter(maoxq.getUsercenter());//用户中心
						mafmxq.setLingjbh(maoxq.getLingjbh());//零件编号
						listMafmxq.add(mafmxq);
					}
				}
			}
		}
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("zygzbj.insertMafmxq",listMafmxq);
	}
	
	/**
	 * 对比
	 * @param param 参数
	 * @param mafmxq maf毛需求信息
	 * @param mafkc maf库存
	 * @return maf毛需求信息
	 * @throws Exception
	 */
	public Mafmxq duiB(Map param,Mafmxq mafmxq,BigDecimal mafkc,BigDecimal fene) throws Exception{
		Class cl = Mafmxq.class;
		//查询毛需求信息
		List<Maoxqmx> listMaoxqmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryMaoxqbc",param);
		//如果毛需求不足或只有1天
		if(listMaoxqmx.size() <= 1){
			return null;
		}
		//设置毛需求(J1-J5),从J1开始,即毛需求的第二天
		for (int i = 1; i <= 5 && i < listMaoxqmx.size(); i++) {
			//毛需求明细信息
			Maoxqmx maoxqmx = listMaoxqmx.get(i);
			//J1开始日期
			if(i == 1){
				mafmxq.setRiq(maoxqmx.getXuqrq());//J1开始日期
				mafmxq.setChej(maoxqmx.getShiycj());//使用车间
				mafmxq.setZhizlx(maoxqmx.getZhizlx());//制造路线
			}
			//设置J
			PropertyDescriptor pd = new PropertyDescriptor("xuq"+i, cl);
			BigDecimal xuqsl = CommonFun.getBigDecimal(maoxqmx.getXuqsl()).multiply(fene);
			pd.getWriteMethod().invoke(mafmxq, xuqsl);
			//短缺日期
			String duanqrq = mafmxq.getGongzr();
			//短缺日期为空,则进行判断
			if(StringUtils.isEmpty(duanqrq)){
				//maf库存-需求数量
				mafkc = mafkc.subtract(xuqsl);
				//如果maf库存小于0,设置短缺日期
				if(mafkc.compareTo(BigDecimal.ZERO) < 0){
					//短缺日期为需求日期
					mafmxq.setGongzr(maoxqmx.getXuqrq());
				}
			}
		}
		return mafmxq;
	}
	
	/**
	 * 查询MAF毛需求对比信息
	 * @param page 查询对象
	 * @param param 查询参数
	 * @return
	 */
	public Map queryMafmxq(PageableSupport page, Map<String, String> param){
		if("exportXls".equals(param.get("exportXls"))){
			return CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryMafmxqDd",param));
		}else{
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.queryMafmxqDd",param,page);
		}
	}
}
