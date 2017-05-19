/**
 * mJ订单计算
 */
package com.athena.xqjs.module.hlorder.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.util.CommonUtil;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.JisclcsszService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

@SuppressWarnings("rawtypes")
@WebService(endpointInterface="com.athena.xqjs.module.hlorder.service.MJCalculate", serviceName="/mjCalculate")
@Component
public class MJCalculateService extends BaseService implements  MJCalculate{
	@Inject
	private MjOrderService mjOrderService;
	@Inject
	private VjMaoxqService vjMaoxqService;
	@Inject
	private JisclcsszService jiscclssz;
	
	public  final Logger log = Logger.getLogger(MJCalculateService.class);

	@Override
	public void mjCalculateUl() {
		mjCalculate("4321", "UL",Const.MJJS_UL);
		
	}

	@Override
	public void mjCalculateUw() {
		mjCalculate("4322", "UW",Const.MJJS_UW);
		
	}

	@Override
	public void mjCalculateUx() {
		mjCalculate("4323", "UX",Const.MJJS_UX);
		
	}
	

	private String mjCalculate(String jkCode ,String userCenter,String jssdm) throws RuntimeException{
		long start = System.currentTimeMillis();
		String message = "程序有误";
		Map<String, String> cmap = new HashMap<String, String>();
		cmap.put("jsrq", CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD));// 计算日期
		cmap.put("username", jkCode);// 设置用户姓名
		cmap.put("jiscldm", jssdm );// 计算处理代码：
		// 判断处理状态,是否有计算进行中
		if (jiscclssz.checkState(cmap)) {
			throw new ServiceException("已有人在进行国产件订单计算,请稍后运行!");
		} else {
			// 更新处理状态为1,计算中
			jiscclssz.updateState(cmap, Const.JSZT_EXECUTING);
		}
		try{
			LoginUser loginUser = new LoginUser();
			loginUser.setUsername(jkCode);
			loginUser.setJihyz("sys");
			ArrayList al = null;
			Map<String, String> map = new HashMap<String,String>();
			List<Ziykzb> ziykzbList = this.vjMaoxqService.queryZiykzb(map);//空查询参数，不用修改
			if(null != ziykzbList && ziykzbList.size() > 0)
			{
				//得到最近的资源获取日期
				String ziyxqrq = ziykzbList.get(0).getZiyhqrq();
				if(StringUtils.isNotBlank(ziyxqrq))
				{
					log.info("ZCJ需求来源结束计算,JLV需求来源开始进行计算");
					//加用户中心，获取毛需求列表
					 al =  (ArrayList)this.vjMaoxqService.queryMaoxqList("XQLY","JLV",userCenter); //通过参数输入，不用修改
					 if( null != al && al.size() > 0)
					 {
						 //获取需求版次
						 String flagBanc = ((Maoxq)al.get(0)).getXuqbc();
						 if(StringUtils.isNotBlank(flagBanc))
						 {
							 message = this.mjOrderService.doCalculate("MJ", ziyxqrq, "", flagBanc, userCenter,loginUser,null,jkCode);
						 }
					 }
					 log.info("JLV需求来源结束计算,"+jkCode+"正常结束");
				}
			}
			// 计算完成更新处理状态为0,计算完成
			jiscclssz.updateState(cmap, Const.JSZT_SURE);
		}
		catch (Exception e) 
		{
			jiscclssz.updateState(cmap, Const.JSZT_EXECPTION);
			String error = CommonUtil.replaceBlank(e.toString());
			log.error(jkCode+"批量出错" + error);
			log.error(e);
			throw new ServiceException(jkCode+"国产件日订单批量出错!" + e);
		}
		log.info(jkCode+"计算结束,耗时---------------------"+(System.currentTimeMillis() - start));
		return message;
	
	}
	
	
	
}
