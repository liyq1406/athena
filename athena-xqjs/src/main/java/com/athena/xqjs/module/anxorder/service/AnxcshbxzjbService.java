package com.athena.xqjs.module.anxorder.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.anxorder.AnxMaoxq;
import com.athena.xqjs.entity.anxorder.AnxMaoxqzjb;
import com.athena.xqjs.entity.anxorder.Anxcshbxzjb;
import com.athena.xqjs.entity.common.Xitcsdy;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * Title:按需初始化布线中间表service
 * 
 * @author 李智
 * @version v1.0
 * @date 2012-03-28
 */
@Component
public class AnxcshbxzjbService extends BaseService {
	@Inject
	private AnxMaoxqService anxMaoxqService;

	/**
	 * 初始初始化布线中间表
	 * 
	 * @author 李智
	 * @version v1.0
	 * @throws ParseException
	 * @date 2012-3-28
	 */
	@Transactional
	public boolean executeAnxcshbxzjb(Maoxq maoxq,Map param,String jisLx,String dingdjssj) throws ParseException {
		// 初始化表数据之前清理数据（清按需毛需求明细中间表）
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.deleteAnxmaoxqzjb",
				new AnxMaoxqzjb());
		Map<String, String> map = new HashMap<String, String>();
		/**
		 * 把毛需求明细表分段放入中间表
		 */
		String today = CommonFun.getJavaTime("yyyy-MM-dd");
		//查询将来外部模式为CD/C1,将来模式2为MD/M1的用户中心,零件,产线,消耗点
		List<AnxMaoxq> listAnxmaoxqs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryCMMos",param);
		map.put("gongzr", today);
		//判断生效期,保留生效期在最近一个工作日之前
		for (int i = 0; i < listAnxmaoxqs.size(); i++) {
			//查询每条产线的最近一个工作日
			AnxMaoxq anxMaoxq = listAnxmaoxqs.get(i);
			map.put("usercenter", anxMaoxq.getUsercenter());
 			map.put("chanxck", anxMaoxq.getChanx());
 			Date gzr = CommonFun.sdf.parse(CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryGzr",map)));
			//如果为外部模式
			if(!CommonFun.strNull(anxMaoxq.getWaibms()).equals("")){
				//判断生效日期是否在最近一个工作日之前
				if(CommonFun.sdf.parse(anxMaoxq.getStartTime()).after(gzr)){
					//如果晚于最近一个工作日不要
					listAnxmaoxqs.remove(i);
				}
			}else{
				//判断生效日期是否在最近一个工作日之前
				if(CommonFun.sdf.parse(anxMaoxq.getStartTime2()).after(gzr)){
					//如果晚于最近一个工作日不要
					listAnxmaoxqs.remove(i);
				}
			}
		}
		map.put("xuqbc", maoxq.getXuqbc());
		// 排除重复的毛需求明细
		//List<AnxMaoxq> anxMaoxqs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryAnxMaoxqDistinct", map);
		for (int i = 0; i < listAnxmaoxqs.size(); i++) {
			AnxMaoxq anxMaoxq = listAnxmaoxqs.get(i);

			// 得到时间段的间隔参数
			map.put("usercenter", anxMaoxq.getUsercenter());
			map.put("zidmc", Const.ZIDMC);
			Xitcsdy xitcsdy = this.anxMaoxqService.queryAnxShijjg(map);
			map.clear();
			BigDecimal jiange = xitcsdy.getQujzxz();
			//如果间隔时间为空,则默认按30分钟
			if(jiange == null){
				jiange = new BigDecimal(30);
			}
			// 得到一个毛需求明细的计算初始点
			map.put("usercenter", anxMaoxq.getUsercenter());
			map.put("lingjbh", anxMaoxq.getLingjbh());
			map.put("xiaohd", anxMaoxq.getXiaohd());
			// 计算开始时间
			String startTime = "";
			if(jisLx.equals("1")){
				startTime = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryStartXiaohsj", map));
			}else{
				startTime = dingdjssj.substring(0,16);
			}
			if(startTime.isEmpty()){
				continue;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			// 下一个时间
			Date nextTimes = dateFormat.parse(startTime);
			// 最大时间
			Date maxTimes  = dateFormat.parse(CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryEndXiaohsj")));

			boolean flag = true;
			do {
				nextTimes = dateFormat.parse(startTime);
				// 得到下一时间段的时间点
				nextTimes.setTime(nextTimes.getTime() + jiange.longValue() * 60 * 1000);
				// 如果到了最大时间点，并且跳出循环
				if (nextTimes.after(maxTimes)) {
					// 下一时间段就是最大时间点
					//nextTimes = maxTimes;
					// 跳出循环的条件
					flag = false;
				}
				// 加入起止时间的条件
				map.put("xiaohsj", startTime);
				map.put("xiaohsj2", dateFormat.format(nextTimes));
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.insertAnxmaoxqzjb", map);
				startTime = dateFormat.format(nextTimes);

			} while (flag);
		}

		// 初始化表数据之前清理数据
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.deleteAnxcshbxzjb",
				new Anxcshbxzjb());
		int count = 0;
		// 初始化按需初始化布线中间表
		count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.insertAnxcshbxzjb",
				new Anxcshbxzjb());
		return count > 0;
	}

	/**
	 * 按模式查询初始化布线中间表
	 * 
	 * @author 李智
	 * @date 2012-3-28
	 * @return List<Anxcshbxzjb> 检索结果
	 */
	public List<Anxcshbxzjb> queryAnxcshbxzjbByMs(Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryAnxcshbxzjbByMs", param);
	}

	/**
	 * 查询线边仓库库存
	 * 
	 * @author 李智
	 * @date 2012-3-28
	 * @return double 检索结果
	 */
	public BigDecimal queryXianbkKucslBylingj(Map<String, String> param) {
		BigDecimal xianbkKucsl = (BigDecimal) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryXianbkKucslBylingj", param);
		if (xianbkKucsl == null) {
			return BigDecimal.ZERO;
		}
		return xianbkKucsl;
	}
}