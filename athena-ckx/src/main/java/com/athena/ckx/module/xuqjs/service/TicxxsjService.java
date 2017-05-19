package com.athena.ckx.module.xuqjs.service;



import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.Ticxxsj;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 未来几日剔除休息时间
 * 
 * @author denggq
 * @date 2012-4-7
 */
@Component
public class TicxxsjService extends BaseService<Ticxxsj> {
	protected static Logger logger = Logger.getLogger(TicxxsjService.class);	//定义日志方法
	@Inject
	private TicxxsjGzsjmbService ticxxsjGzsjmbService;
	/**
	 * 获得命名空间
	 * 
	 * @author denggq
	 * @date 2012-4-7
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 计算未来几日休息时间
	 * 
	 * @author denggq
	 * @date 2012-4-7
	 * @param year
	 * @param username
	 * @return String
	 */
	public String calculate(String userId) throws ServiceException {
//		未来几日剔除休息时间
		addTicxxsj(userId);
//		工作时间模板
//		ticxxsjGzsjmbService.calculate(userId);
//		calculateGongzsjmb();
		return "tichucg";
	}
	@Transactional
	public String addTicxxsj(String userId){
		logger.info("未来几日剔除休息时间计算开始");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(
				"ts_ckx.deleteTicxxsj");// 清空交付日历表数据
		Ticxxsj bean = new Ticxxsj();// 剔除休息时间实体类
		bean.setRiq(DateTimeUtil.getCurrDate());// 前两天 未来18天
		bean.setCreator(userId);// 增加人
		bean.setEditor(userId);// 修改人
		// 按照编组号生成未来几日剔除休息时间
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(
				"ts_ckx.insertTicxxsjByBianzh", bean);// 根据工作时间分组，日历版次和工作时间生成剔除休息日的工作时间
		// 按照编组号和未来编组号生成未来几日剔除休息时间
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(
				"ts_ckx.insertTicxxsjByWeilbzh", bean);// 根据工作时间分组，日历版次和工作时间生成剔除休息日的工作时间
		logger.info("未来几日剔除休息时间计算成功");
		return "";
	}
	
	public String calculateTicxxsjTemp(String userId){
		
		ticxxsjGzsjmbService.addGongzsjmb(userId);
		return "success";
	}
	
	
	public Map<String, Object> selectSgjs(Ticxxsj bean){
		
		Map<String, Object> t= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages(
				"ts_ckx.queryTicxxsjSgjs", bean,bean);
		return t;
	}
}
