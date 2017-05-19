package com.athena.xqjs.module.ilorder.service;

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
@WebService(endpointInterface="com.athena.xqjs.module.ilorder.service.PJCalculate", serviceName="/pjCalculate")
@Component
public class PJCalculateService extends BaseService implements PJCalculate{
	@Inject
	private IlOrderService ilorderservice;
	@Inject
	private MaoxqService maoxqservice;
	@Inject
	private JisclcsszService jiscclssz;
	
	public  final Logger log = Logger.getLogger(PJCalculateService.class);
	
	public String pjCalculate() throws RuntimeException{
		long start = System.currentTimeMillis();
		String message = "程序有误";
		Map<String, String> cmap = new HashMap<String, String>();
		cmap.put("jsrq", CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD));// 计算日期
		cmap.put("username", "4090");// 设置用户姓名
		cmap.put("jiscldm", Const.JISMK_IL_CD);// 计算处理代码：国产件（31）
		// 判断处理状态,是否有计算进行中
		if (jiscclssz.checkState(cmap)) {
			throw new ServiceException("已有人在进行国产件订单计算,请稍后运行!");
		} else {
			// 更新处理状态为1,计算中
			jiscclssz.updateState(cmap, Const.JSZT_EXECUTING);
		}
		try{
			//String ziyxqrq = CommonFun.getJavaTime().substring(0, 10);
			LoginUser loginUser = new LoginUser();
			loginUser.setUsername("4090");
			loginUser.setJihyz("sys");
			ArrayList al = null;
			Map<String, String> map = new HashMap<String,String>();
			List<Ziykzb> ziykzbList = this.maoxqservice.queryZiykzb(map);
			if(null != ziykzbList && ziykzbList.size() > 0)
			{
				String ziyxqrq = ziykzbList.get(0).getZiyhqrq();
				if(StringUtils.isNotBlank(ziyxqrq))
				{
					log.info("4090调用开始,ZCJ需求来源开始进行计算");
					 al = 	(ArrayList)this.maoxqservice.queryMaoxqList("XQLY","ZCJ");  //UX
					 if( null != al && al.size() > 0)
					 {
						 String flagBanc = ((Maoxq)al.get(0)).getXuqbc();
						 if(StringUtils.isNotBlank(flagBanc))
						 {
							 message = this.ilorderservice.doCalculate("97W", "PJ", ziyxqrq, "", new String[]{flagBanc}, null,"UX",new String[]{"UX"},loginUser);
						 }
					 }
					 log.info("ZCJ需求来源结束计算,CLV需求来源开始进行计算");
					 al =  (ArrayList)this.maoxqservice.queryMaoxqList("XQLY","CLV");
					 if( null != al && al.size() > 0)
					 {
						 String flagBanc = ((Maoxq)al.get(0)).getXuqbc();
						 if(StringUtils.isNotBlank(flagBanc))
						 {
							 message = this.ilorderservice.doCalculate("97W", "PJ", ziyxqrq, "", new String[]{flagBanc}, null,"UL",new String[]{"UL","UW"},loginUser);
						 }
					 }
					 log.info("CLV需求来源结束计算,4090正常结束");
					 
				}
			}
			// 计算完成更新处理状态为0,计算完成
			jiscclssz.updateState(cmap, Const.JSZT_SURE);
		}
		catch (Exception e) 
		{
			jiscclssz.updateState(cmap, Const.JSZT_EXECPTION);
			String error = CommonUtil.replaceBlank(e.toString());
			log.error("4090批量出错" + error);
			log.error(e);
			throw new ServiceException("4090 国产件日订单批量出错!" + e);
		}
		log.info("4090计算结束,耗时---------------------"+(System.currentTimeMillis() - start));
		return message;
	}
	
}
