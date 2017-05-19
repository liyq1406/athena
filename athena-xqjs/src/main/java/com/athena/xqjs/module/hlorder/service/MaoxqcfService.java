/**
 * VJ毛需求拆分服务
 */
package com.athena.xqjs.module.hlorder.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.xqjs.entity.hlorder.Clddxx2TmpNup;
import com.athena.xqjs.entity.hlorder.DdbhMaoxqmx;
import com.athena.xqjs.entity.hlorder.NupZongcdlf;
import com.athena.xqjs.entity.hlorder.Yonghmqxcfsz;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

@SuppressWarnings("rawtypes")
@WebService(endpointInterface = "com.athena.xqjs.module.hlorder.service.Maoxqcf", serviceName = "/maoxqcf")
@Component
public class MaoxqcfService extends BaseService implements Maoxqcf {
	public final Logger log = Logger.getLogger(MaoxqcfService.class);

	/**
	 * 毛需求拆分入口
	 */
	public void maoxqCf() {
		// 清空毛需求中间表DDBH_MAOXQMX_TMP
		// 清空毛需求中间表DDBH_MAOXQMX
		clearDDBH_MAOXQMX_TMP();
		// 根据nup单量份拆分出毛需求（插入到毛需求表中）ddbh_maoxqmx
		nupCfmaoxq();
		// 备件外销计划与总成单量份关联得到备件外销毛需求
		bjwxCfmaoxq();

		// 清除毛需求明细
		clearDDBH_MAOXQMX();
		// 转移数据从DDBH_MAOXQMX_TMP到DDBH_MAOXQMX
		int backValue = changeDDBH_MAOXQMX_TMPData();
		if ( backValue > 0)
		{
			// 删除汇总上线计划中间表IN_CLDDXX2_TMP全部记录
			baseDao.getSdcDataSource("2")
					.execute("hlorder.deleteClddxx2TmpAlldata");
		}
	}
	
	/*
	 * 根据nup单量份拆分出毛需求（插入到毛需求表中）ddbh_maoxqmx select
	 * t.usercenter,t.scxh,n.lingjbh,n.shul*t.shul,n.zhizlx,n.chej from in_nup
	 * n,in_clddxx2_tmp t where n.usercenter=t.usercenter and n.lcdv24=t.lcdv24
	 * and n.zhankrq=t.yjsyhsj and n.chej=substr(t.scxh,0,3) 变化后插入到表
	 * ddbh_maoxqmx
	 * 
	 * @return
	 */
	private void nupCfmaoxq() {
		//在执行JLV毛需求拆分时，只从上线计划里拆分总装和涂装的毛需求，焊装的毛需求通过主线排产计划与总成单量份关联获取，关联条件为用户中心，车间，总成号，展开日期，并且预计焊装时间大于系统当前时间。
		List<Clddxx2TmpNup> sumclddxx2lists = baseDao.getSdcDataSource("2")
				.select("hlorder.querynupclddxx2temmaoxqmx", new HashMap());
		//获取所有的拆分方式
		List<Yonghmqxcfsz> chaiflist = baseDao.getSdcDataSource("2")
		.select("hlorder.queryCkx_yonghmqxcfsz", new HashMap());
		for (Yonghmqxcfsz yonghmqxcfsz : chaiflist) {
			Map<String ,String> paream = new HashMap<String ,String>();
			if(yonghmqxcfsz.getChaflx().equalsIgnoreCase("1")){//老的拆分方式
				paream.put("USERCENTER", yonghmqxcfsz.getUsercenter());
				List<Clddxx2TmpNup> chan1list = baseDao.getSdcDataSource("2")
				.select("hlorder.querynupclddxx2temmaoxqmxHanz", paream);
				sumclddxx2lists.addAll(chan1list);
			}else if(yonghmqxcfsz.getChaflx().equalsIgnoreCase("2")){//新的拆分方式
				paream.put("USERCENTER", yonghmqxcfsz.getUsercenter());
				List<Clddxx2TmpNup> chan2list = baseDao.getSdcDataSource("2")
				.select("hlorder.queryZhupcjhZongcdlfmaoxqmxHanz", paream);
				sumclddxx2lists.addAll(chan2list);
			}else if(yonghmqxcfsz.getChaflx().equalsIgnoreCase("0")){
				paream.put("USERCENTER", yonghmqxcfsz.getUsercenter().trim());
				if(StringUtils.isNotBlank(yonghmqxcfsz.getChanx1())){//老的拆分方式
					String[] chanx1=	yonghmqxcfsz.getChanx1().trim().split(",");
					StringBuffer sb1=new StringBuffer();
					for (String string : chanx1) {
						sb1.append("'"+string+"',");
					}
					if(sb1.length()>0){
					sb1=sb1.delete(sb1.length()-1, sb1.length());
					}
					paream.put("CHANX1", sb1.toString());
					List<Clddxx2TmpNup> chan1list = baseDao.getSdcDataSource("2")
					.select("hlorder.querynupclddxx2temmaoxqmxHanzChan", paream);
					sumclddxx2lists.addAll(chan1list);
				}
				if(StringUtils.isNotBlank(yonghmqxcfsz.getChanx2())){//新的拆分方式
					String[] chanx2=	yonghmqxcfsz.getChanx2().trim().split(",");
					StringBuffer sb2=new StringBuffer();
					for (String string : chanx2) {
						sb2.append("'"+string+"',");
					}
					if(sb2.length()>0){
					sb2=sb2.delete(sb2.length()-1, sb2.length());
					}					
					paream.put("CHANX2", sb2.toString());				
					List<Clddxx2TmpNup> chan2list = baseDao.getSdcDataSource("2")
					.select("hlorder.queryZhupcjhZongcdlfmaoxqmxHanzChan", paream);
				
					sumclddxx2lists.addAll(chan2list);
				}
			}
		}
		if (sumclddxx2lists != null && sumclddxx2lists.size() > 0) {
			List<DdbhMaoxqmx> mxqmxlists = new ArrayList<DdbhMaoxqmx>(
					sumclddxx2lists.size());
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date cfsj = cal.getTime();
			for (Clddxx2TmpNup ctn : sumclddxx2lists) {
				DdbhMaoxqmx mx = clddxx2TmpNup2DdbhMaoxqmx(ctn);
				mx.setFlag("0");
				mx.setXuqcfsj(cfsj);
				mxqmxlists.add(mx);
			}
			// 批量插入
			baseDao.getSdcDataSource("2").executeBatch(
					"hlorder.insertDdbhMaoxqmxtmp", mxqmxlists);
		}

	}
	
	/*
	 * 清空中间表DDBH_MAOXQMX_TMP
	 */
	private void clearDDBH_MAOXQMX_TMP() {
		CommonFun.logger.info("清空毛需求拆分中间表DDBH_MAOXQMX_TMP清理开始");
		baseDao.getSdcDataSource("2").execute("hlorder.deleteDDBH_MAOXQMX_TMP");
		CommonFun.logger.info("清空毛需求拆分中间表DDBH_MAOXQMX_TMP清理结束");
	}

	/*
	 * 清空中间表DDBH_MAOXQMX
	 */
	private void clearDDBH_MAOXQMX() {
		CommonFun.logger.info("清空毛需求拆分中间表DDBH_MAOXQMX清理开始");
		baseDao.getSdcDataSource("2").execute(
				"hlorder.deleteallDDBH_DdbhMaoxqmx");
		CommonFun.logger.info("清空毛需求拆分中间表DDBH_MAOXQMX清理结束");
	}
	
	/*
	 * 备件外销计划与总成单量份关联得到备件外销毛需求 select
	 * z.usercenter,z.scxh,z.lingjbh,z.shul*b.shul,z.zhizlx,z.chej,z.danw from
	 * in_nup_zongcdlf z,in_beijwxjh bwhere z.usercenter=b.usercenter and
	 * z.zongclj=b.zongch and z.ecom=b.zhankrq变化后插入到表 ddbh_maoxqmx
	 */
	private void bjwxCfmaoxq() {
		List<NupZongcdlf> sumnupzonglists = baseDao.getSdcDataSource("2")
				.select("hlorder.querynupzongcdlfbeijwxjhmxq");
		if (sumnupzonglists != null && sumnupzonglists.size() > 0) {
			List<DdbhMaoxqmx> mxqmxlists = new ArrayList<DdbhMaoxqmx>(
					sumnupzonglists.size());
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date cfsj = cal.getTime();
			for (NupZongcdlf ctn : sumnupzonglists) {
				DdbhMaoxqmx mx = nupZongcdlf2DdbhMaoxqmx(ctn);
				mx.setFlag("0");
				mx.setXuqcfsj(cfsj);
				mxqmxlists.add(mx);
			}
			// 批量插入
			baseDao.getSdcDataSource("2").executeBatch(
					"hlorder.insertDdbhMaoxqmxtmp", mxqmxlists);
		}
	}

	private int changeDDBH_MAOXQMX_TMPData() {
		int backValue = 0;
		List<DdbhMaoxqmx> sumnupzonglists = baseDao.getSdcDataSource("2")
				.select("hlorder.queryDDBH_MAOXQMX_TMPData");
		if (sumnupzonglists != null && sumnupzonglists.size() > 0) {
			List<DdbhMaoxqmx> mxqmxlists = new ArrayList<DdbhMaoxqmx>(
					sumnupzonglists.size());
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date cfsj = cal.getTime();
			for (DdbhMaoxqmx ctn : sumnupzonglists) {
				ctn.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				ctn.setXuqcfsj(cfsj);
				mxqmxlists.add(ctn);
			}
			// 批量插入
			backValue = baseDao.getSdcDataSource("2").executeBatch(
					"hlorder.insertDdbhMaoxqmx", mxqmxlists);
		}
		return backValue;
	}

	private DdbhMaoxqmx nupZongcdlf2DdbhMaoxqmx(NupZongcdlf ctn) {
		if (ctn != null) {
			DdbhMaoxqmx temp = new DdbhMaoxqmx();
			temp.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			temp.setShiycj(ctn.getZijsycj());
			temp.setDanw(ctn.getDanw());
			temp.setLingjbh(ctn.getLingjbh());
			temp.setChanx(ctn.getScxh());
			temp.setLingjsl(ctn.getShul());
			temp.setUsercenter(ctn.getUsercenter());
			temp.setZhizlx(ctn.getZhizlx());
			temp.setXuqrq(ctn.getYjsxsj());

			return temp;
		}
		return null;
	}
	
	private DdbhMaoxqmx clddxx2TmpNup2DdbhMaoxqmx(Clddxx2TmpNup ctn) {
		if (ctn != null) {
			DdbhMaoxqmx temp = new DdbhMaoxqmx();
			temp.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			temp.setShiycj(ctn.getChej());
			temp.setDanw(ctn.getDanw());
			temp.setLingjbh(ctn.getLingjbh());
			temp.setChanx(ctn.getScxh());
			temp.setLingjsl(ctn.getShul());
			temp.setUsercenter(ctn.getUsercenter());
			temp.setZhizlx(ctn.getZhizlx());
			temp.setXuqrq(ctn.getYjsxsj());
			return temp;
		}
		return null;
	}
}
