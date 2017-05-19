package com.athena.component.service.imp;


import java.sql.Connection;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.athena.component.exchange.SpaceFinal;
import com.athena.component.service.InterfaceService;
import com.athena.component.service.bean.ServiceBean;
import com.toft.core2.dao.database.DbUtils;

@WebService(endpointInterface="com.athena.component.service.InterfaceService",serviceName="/InterfaceServiceImp")
public class InterfaceServiceImp implements InterfaceService {
	private static final Log logger = LogFactory.getLog(InterfaceServiceImp.class);
	private Connection conn=DbUtils.getConnection("1");
	
	@Override
	public void setServiceBean(List<ServiceBean> list) {
		try{
		 if(null!=list){
			for (ServiceBean serviceBean : list) {
				int i=1;
				String Dbsqdh=serviceBean.getDbsqdh();
				String Dbsq_date=serviceBean.getDbsq_date();
				String Kjkm=serviceBean.getKjkm();
				String Cbzx=serviceBean.getCbzx();
				String Zzlx=serviceBean.getZzlx();
				String Ljh=serviceBean.getLjh();
				double Sbsl=serviceBean.getSbsl();
				String Usercenter=serviceBean.getUsercenter();
				
				
				Object obj_dbsqd=this.GetDbsq(Usercenter,Dbsqdh);
                if(null!=obj_dbsqd){
					this.UpdateDbsqd(serviceBean);
                }
                else{
                	//add by pan.rui 外部系统可能会传入10位调拨申请单
                	if(Dbsqdh.length() <= 8){
						StringBuffer strbuf=new StringBuffer();
					    strbuf.append("insert into "+SpaceFinal.spacename_ckx+".xqjs_diaobsq");
					    strbuf.append("(usercenter,");
					    strbuf.append("diaobsqdh,");
					    strbuf.append("diaobsqsj,");
					    strbuf.append("banc,");
					    strbuf.append("huijkm,");
					    strbuf.append("chengbzx,");
					    strbuf.append("zhuangt,");
					    strbuf.append("creator,");
					    strbuf.append("create_time,");
					    strbuf.append("editor,");
					    strbuf.append("edit_time,");
					    strbuf.append("active)");
					    strbuf.append("values(");
					    strbuf.append("'"+StrNull(Usercenter)+"',");
					    strbuf.append("'"+StrNull(Dbsqdh)+"',");
					    strbuf.append("to_date('"+StrNull(Dbsq_date)+"','yyyy-MM-dd'),");
					    strbuf.append("'0001',");
					    strbuf.append("'"+StrNull(Kjkm)+"',");
					    strbuf.append("'"+StrNull(Cbzx)+"',");
					    strbuf.append("'30',");
					    strbuf.append("'OEDIPP',");
					    strbuf.append("current_timestamp,");
					    strbuf.append("'OEDIPP',");
					    strbuf.append("current_timestamp,");
					    strbuf.append("'1')");
					    DbUtils.execute(strbuf.toString(), conn);
					    conn.commit();
                	}
                }
                
				Object obj_dbsqdmx=this.GetDbsqdmx(Usercenter,Dbsqdh,Zzlx,Ljh);
                if(null!=obj_dbsqdmx){
					this.UpdateDbsqdmx(serviceBean);
                }else{
                	if(Dbsqdh.length() <= 8){
					    StringBuffer strbufmx=new StringBuffer();
					    strbufmx.append("insert into "+SpaceFinal.spacename_ckx+".XQJS_DIAOBSQMX");
					    strbufmx.append("(xuh,");
					    strbufmx.append("usercenter,");
					    strbufmx.append("diaobsqdh,");
					    strbufmx.append("lux,");
					    strbufmx.append("lingjbh,");
					    strbufmx.append("shenbsl,");
					    strbufmx.append("zhuangt,");
		                strbufmx.append("creator,");
					    strbufmx.append("create_time,");
					    strbufmx.append("editor,");
					    strbufmx.append("edit_time,");
					    strbufmx.append("active)");
					    strbufmx.append("values(");
					    strbufmx.append(""+i+++",");
					    strbufmx.append("'"+StrNull(Usercenter)+"',");
					    strbufmx.append("'"+StrNull(Dbsqdh)+"',");
					    strbufmx.append("'"+StrNull(Zzlx)+"',");
					    strbufmx.append("'"+StrNull(Ljh)+"',"); 
					    strbufmx.append(""+Sbsl+",");
					    strbufmx.append("'30',");
					    strbufmx.append("'OEDIPP',");
					    strbufmx.append("current_timestamp,");
					    strbufmx.append("'OEDIPP',");
					    strbufmx.append("current_timestamp,");
					    strbufmx.append("'1')");
					    DbUtils.execute(strbufmx.toString(), conn);
					    conn.commit(); 
                	}
                }
			 }
		  }
		}catch(Exception e){
			logger.error(e.getMessage());
		}
  }
	
	
	/**
	 * 查询调拨申请单表是否有重复数据
	 * @param USERCENTER
	 * @param DIAOBSQDH
	 * @return
	 */
	public Object GetDbsq(String USERCENTER,String DIAOBSQDH){
		StringBuffer strbuf=new StringBuffer();
		Object obj=null;
		try{
		strbuf.append("select * from "+SpaceFinal.spacename_ckx+".XQJS_DIAOBSQ where ");
		strbuf.append("USERCENTER='"+StrNull(USERCENTER)+"' and ");
		strbuf.append("DIAOBSQDH='"+StrNull(DIAOBSQDH)+"'");
		obj=DbUtils.selectValue(strbuf.toString(), conn);	
	       }catch(Exception e){
	    	   logger.error(e.getMessage());
	       }
	     return obj;
	}
	
	
	
	/**
	 *查询调拨申请单明细表是否有重复数据
	 * @param USERCENTER
	 * @param DIAOBSQDH
	 * @param LUX
	 * @param LINGJBH
	 * @return
	 */
	public Object GetDbsqdmx(String USERCENTER,String DIAOBSQDH,String LUX,String LINGJBH){
		StringBuffer strbuf=new StringBuffer();
		Object obj=null;
		try{
		strbuf.append("select * from "+SpaceFinal.spacename_ckx+".XQJS_DIAOBSQMX where ");
		strbuf.append("USERCENTER='"+StrNull(USERCENTER)+"' and ");
		strbuf.append("DIAOBSQDH='"+StrNull(DIAOBSQDH)+"' and ");
		strbuf.append("LUX='"+StrNull(LUX)+"' and ");
		strbuf.append("LINGJBH='"+StrNull(LINGJBH)+"' ");
		obj=DbUtils.selectValue(strbuf.toString(), conn);	
	       }catch(Exception e){
	    	   logger.error(e.getMessage());
	       }
	     return obj;
	}
	
	
	/**
	 * 更新调拨申请单表
	 * @param servicebean
	 */
	public void UpdateDbsqd(ServiceBean servicebean){
		StringBuffer strbuf=new StringBuffer();
		try{
		strbuf.append("update "+SpaceFinal.spacename_ckx+".XQJS_DIAOBSQ set ");
		strbuf.append("DIAOBSQSJ=to_date('"+StrNull(servicebean.getDbsq_date())+"','yyyy-MM-dd'),");
		strbuf.append("BANC='0001',");
		strbuf.append("HUIJKM='"+StrNull(servicebean.getKjkm())+"',");
		strbuf.append("CHENGBZX='"+StrNull(servicebean.getCbzx())+"',");
		strbuf.append("ZHUANGT='30',");
		strbuf.append("EDIT_TIME=current_timestamp where ");
		strbuf.append("USERCENTER='"+StrNull(servicebean.getUsercenter())+"' and ");
		strbuf.append("DIAOBSQDH='"+StrNull(servicebean.getDbsqdh())+"' ");
		DbUtils.execute(strbuf.toString(),conn);
		conn.commit();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	
	/**
	 * 更新调拨申请单表
	 * @param servicebean
	 */
	public void UpdateDbsqdmx(ServiceBean servicebean){
		StringBuffer strbuf=new StringBuffer();
		try{
		strbuf.append("update "+SpaceFinal.spacename_ckx+".XQJS_DIAOBSQMX set ");
		strbuf.append("SHENBSL="+servicebean.getSbsl()+",");
		strbuf.append("ZHUANGT='30',");
		strbuf.append("EDIT_TIME=current_timestamp where ");
		strbuf.append("USERCENTER='"+StrNull(servicebean.getUsercenter())+"' and ");
		strbuf.append("LUX='"+StrNull(servicebean.getZzlx())+"' and ");
		strbuf.append("LINGJBH='"+StrNull(servicebean.getLjh())+"'");
		DbUtils.execute(strbuf.toString(),conn);
		conn.commit();
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	
	
	/**
	 * 判断字符串为null时
	 * @param objStr
	 * @return
	 */
	private String StrNull(Object objStr){
		return objStr==null?"":objStr.toString();
	}
	
}
