package com.athena.component.output;

import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;

/**
 * 处理返修单和退货单表 操作码为O8的结果集输出
 * 
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-19
 * lastModify  By 王冲, 2012-08-31 13:18  内容：注掉全部代码
 */
public class Rkmx08DataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(Rkmx08DataWriter.class);	//定义日志方法

	// 创建时间 格式为:yyyymmdd HH24:Mi:ss
	private String CJ_DATE;

	public Rkmx08DataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);

//		// 查找接口表的创建时间 将创建时间设置到变量中
//		CJ_DATE = get_CJ_DATE();
//
//		// 删除接口返修单和退货单表数据
//		deleteFxd();
//		// 将仓库中的返修单和退货单表数据写入到接口对应表中
//		insertFxd();
	}

//	/**
//	 * 得到创建时间
//	 * 
//	 * @return
//	 */
//	private String get_CJ_DATE() {
//
//		String result = null;
//		try {
//			// 创建sql语句 查询
//			String sql = "select to_char(y.CREATE_TIME,'yyyymmdd HH24:Mi:ss') cj_date from "+SpaceFinal.spacename_ckx+".IN_CK_FANXD y order by y.CREATE_TIME asc";
//			PreparedStatement st = interfaceConn.prepareStatement(sql);
//			ResultSet rs = st.executeQuery();
//			if (rs.next()) {
//				result = rs.getString("cj_date");
//			}
//		} catch (SQLException e) {
//			logger.error(e.getMessage());
//		}
//		return result;
//	}
//	
//	
//	private List<Map<String,Object>> getBesinessDatas(){
//		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//		
//		StringBuffer sb = new StringBuffer();	
//		sb.append("select USERCENTER, ");
//		sb.append("USH, ");
//		sb.append("ELH, ");
//		sb.append("CANGKBH, ");
//		sb.append("ZICKBH, ");
//		sb.append("CHENGYSMC, ");
//		sb.append("LINGJBH, ");
//		sb.append(" DANW, ");
//		sb.append(" LINGJSL, ");
//		sb.append(" GONGYSDM, ");
//		sb.append(" SHENGCSJ, ");
//		sb.append(" CANGKGLYZ, ");
//		sb.append(" sysdate, "); // 创建时间 为当前 不是仓库的创建时间
//		sb.append("  0 ");
//		sb.append("FROM "+SpaceFinal.spacename_ck+".Ck_Fanxd ");
//
//		// 添加条件 拿出 在最近一次创建时间和当前时间之间的数据
//		if (CJ_DATE == null) {
//			// 创建时间为空 则默认拿出当前之前的记录
//			sb.append(" where EDIT_TIME <= sysdate");
//
//		} else {
//			// 不为空
//			sb.append("WHERE EDIT_TIME >= to_date('" + CJ_DATE
//					+ "', 'yyyymmdd HH24:Mi:ss') ");
//			sb.append("and EDIT_TIME <= sysdate ");
//		}
//		
//		try {
//			PreparedStatement st = businessConn.prepareStatement(sb.toString());
//			ResultSet rs = st.executeQuery();
//			
//			
//			while(rs!=null&&rs.next()){
//				Map<String,Object> result = new HashMap<String,Object>();			
//				result.put("USERCENTER", rs.getObject(1));
//				result.put("USH", rs.getObject(2));
//				result.put("ELH", rs.getObject(3));
//				result.put("CANGKBH", rs.getObject(4));
//				result.put("ZICKBH", rs.getObject(5));				
//				result.put("CHENGYSMC", rs.getObject(6));				
//				result.put("LINGJBH", rs.getObject(7));				
//				result.put("DANW", rs.getObject(8));				
//				result.put("LINGJSL", rs.getObject(9));				
//				result.put("GONGYSDM", rs.getObject(10));			
//				result.put("SHENGCSJ", rs.getObject(11));
//				result.put("CANGKGLYZ", rs.getObject(12));
//				result.put("CJSJ", rs.getObject(13));
//				result.put("CLBS", rs.getObject(14));				
//						
//				list.add(result);
//			}
//			
//		} catch (SQLException e) {
//			logger.error(e.getMessage());
//		}		
//		return list;
//	}
//
//	/**
//	 * 将仓库中的返修单和退货单表数据写入到接口对应表中
//	 */
//	private void insertFxd() {
//		try{
//			//创建sql语句 查询
//			StringBuffer sb = new StringBuffer();
//			
//			sb.append("insert into "+SpaceFinal.spacename_ckx+".IN_CK_FANXD ");
//			sb.append("(USERCENTER, ");
//			sb.append(" USH, ");
//			sb.append("  ELH, ");
//			sb.append(" CANGKBH, ");
//			sb.append(" ZICKBH, ");
//			sb.append(" CHENGYSMC, ");
//			sb.append(" LINGJBH, ");
//			sb.append(" DANW, ");
//			sb.append(" LINGJSL, ");
//			sb.append("GONGYSDM, ");
//			sb.append("SHENGCSJ, ");
//			sb.append("CANGKGLYZ, ");
//			sb.append("CREATE_TIME, ");
//			sb.append("CLZT) ");
//			sb.append(" values( ");
//			for(int i=0;i<14;i++){
//				if(i!=13){
//					sb.append(" ?, ");
//				}else{
//					sb.append(" ? )");
//				}
//			}
//			
//			List<Map<String, Object>> resyltList = getBesinessDatas();
//			
//			if(getBesinessDatas().size()>0){
//				PreparedStatement st = interfaceConn.prepareStatement(sb.toString());
//				
//				for(Map map: resyltList){									
//					st.setObject(1, map.get("USERCENTER"));
//					st.setObject(2, map.get("USH"));
//					st.setObject(3, map.get("ELH"));
//					st.setObject(4, map.get("CANGKBH"));
//					st.setObject(5, map.get("ZICKBH"));
//					st.setObject(6, map.get("CHENGYSMC"));
//					st.setObject(7, map.get("LINGJBH"));
//					st.setObject(8, map.get("DANW"));					
//					st.setObject(9, map.get("LINGJSL"));
//					st.setObject(10, map.get("GONGYSDM"));					
//					st.setObject(11, map.get("SHENGCSJ"));
//					st.setObject(12, map.get("CANGKGLYZ"));
//					st.setObject(13, map.get("CJSJ"));
//					st.setObject(14, map.get("CLBS"));
//					
//					st.execute();
//				}
//				
//			}
//			
//		}catch(SQLException e){
//			logger.error(e.getMessage());
//		}			
//	}

	/**
	 * 为后面改造--读取配置文件的sql
	 * 
	 * @return
	 */
//	private String createSQL() {
//		StringBuffer sb = new StringBuffer();
//
//		sb.append("insert into DEV_CKX_TEST.IN_CK_FANXD ");
//		sb.append("(USERCENTER, ");
//		sb.append(" USH, ");
//		sb.append("  ELH, ");
//		sb.append(" CANGKBH, ");
//		sb.append(" ZICKBH, ");
//		sb.append(" CHENGYSMC, ");
//		sb.append(" LINGJBH, ");
//		sb.append(" DANW, ");
//		sb.append(" LINGJSL, ");
//		sb.append("GONGYSDM, ");
//		sb.append("SHENGCSJ, ");
//		sb.append("CANGKGLYZ, ");
//		sb.append("CREATE_TIME, ");
//		sb.append("CLZT) ");
//		sb.append("select USERCENTER, ");
//		sb.append("USH, ");
//		sb.append("ELH, ");
//		sb.append("CANGKBH, ");
//		sb.append("ZICKBH, ");
//		sb.append("CHENGYSMC, ");
//		sb.append("LINGJBH, ");
//		sb.append(" DANW, ");
//		sb.append(" LINGJSL, ");
//		sb.append(" GONGYSDM, ");
//		sb.append(" SHENGCSJ, ");
//		sb.append(" CANGKGLYZ, ");
//		sb.append(" sysdate, "); // 创建时间 为当前 不是仓库的创建时间
//		sb.append("  0 ");
//		sb.append(" from DEV_CK_TEST.Ck_Fanxd ");
//
//		// 添加条件 拿出 在最近一次创建时间和当前时间之间的数据
//		if (CJ_DATE == null) {
//			// 创建时间为空 则默认拿出当前之前的记录
//			sb.append(" where EDIT_TIME <= sysdate");
//
//		} else {
//			// 不为空
//			sb.append("WHERE EDIT_TIME >= to_date('" + CJ_DATE
//					+ "', 'yyyymmdd HH24:Mi:ss') ");
//			sb.append("and EDIT_TIME <= sysdate ");
//		}
//		return sb.toString();
//	}
//
//	/**
//	 * 删除接口返修单和退货单表数据
//	 */
//	private void deleteFxd() {
//		try {
//			// 创建sql语句 查询
//			String sql = "delete from DEV_CKX_TEST.IN_CK_FANXD";
//			PreparedStatement st = interfaceConn.prepareStatement(sql);
//			st.execute(sql);
//		} catch (SQLException e) {
//			logger.error(e.getMessage());
//		}
//	}

	/**
	 * 调用O8S
	 */
	@Override
	public void fileAfter(OutputStreamWriter out) {
		//关闭连接
		if(interfaceConn!=null){
			DbUtils.freeConnection(interfaceConn);
		}
		
		if(businessConn!=null){
			DbUtils.freeConnection(businessConn);
		}
//		setTotal(getTotal()+dataEchange.total) ;
		// 处理A25,A25S
		dataEchange.doExchange("2460_CKLSZ_JF", out, getTotal(),dataParserConfig.getWriterConfigs()[0].getUsercenter());
		
	}

}
