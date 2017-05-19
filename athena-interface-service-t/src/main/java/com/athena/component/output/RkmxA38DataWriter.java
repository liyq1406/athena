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

import com.athena.component.exchange.ParserException;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;
/**
 * A38正常退货表
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-23
 * lastModify  By 王冲, 2012-09-03 13:18  内容：注掉全部代码
 */
public class RkmxA38DataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(RkmxA38DataWriter.class);	//定义日志方法

	//创建时间 格式为:yyyymmdd HH24:Mi:ss
	private String CJ_DATE;
	
	public RkmxA38DataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		
//		//查找接口表的创建时间 将创建时间设置到变量中
//		CJ_DATE = get_CJ_DATE();
//		
//		//删除接口正常退库表数据
//		deleteFxd();
//		//将仓库中的正常退库数据写入到接口对应表中
//		insertFxd();
	}
//	
//	/**
//	 * 得到创建时间
//	 * @return
//	 */
//	private String get_CJ_DATE() {
//		
//		String result =null;
//		try{
//			//创建sql语句 查询
//			String sql = "select to_char(y.CREATE_TIME,'yyyymmdd HH24:Mi:ss')  cj_date from "+SpaceFinal.spacename_ckx+".IN_CK_ZHENGCTK y order by y.CREATE_TIME asc";
//			PreparedStatement st = interfaceConn.prepareStatement(sql);
//			ResultSet  rs = st.executeQuery();
//			
//			if(rs.next()){
//				result = rs.getString("cj_date");
//			}
//		}catch(SQLException e){
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
//		sb.append("SELECT USERCENTER, ");
//		sb.append(" LIUSH, ");
//		sb.append("KEH, ");
//		sb.append("ZHANGH, ");
//		sb.append("LINGJBH, ");
//		sb.append(" LINGJSL, ");
//		sb.append("ELH, ");
//		sb.append("PICH, ");
//		sb.append("GONGYSDM, ");
//		sb.append("CANGKBH, ");
//		sb.append("ZICKBH, ");
//		sb.append("CAOZSJ, ");
//		sb.append("ZHUANGT, ");
//		sb.append("sysdate, ");//创建时间为当前时间，不是仓库的创建时间
//		sb.append("CANGKGLYZ, ");
//		sb.append("0 ");
//		sb.append("FROM "+SpaceFinal.spacename_ck+".CK_ZHENGCTK ");
//		
//		//添加条件  拿出 在最近一次创建时间和当前时间之间的数据
//		if(CJ_DATE==null){
//			//创建时间为空 则默认拿出当前之前的记录
//			sb.append(" where EDIT_TIME <= sysdate");
//			
//		}else{
//			//不为空  
//			sb.append("WHERE EDIT_TIME >= to_date('"+CJ_DATE+"', 'yyyymmdd HH24:Mi:ss') ");
//			sb.append("and EDIT_TIME <= sysdate ");
//		}
//		
//		try {
//			PreparedStatement st = businessConn.prepareStatement(sb.toString());
//			ResultSet rs = st.executeQuery();
//			
//			while(rs!=null&&rs.next()){
//				Map<String,Object> result = new HashMap<String,Object>();			
//				result.put("USERCENTER", rs.getObject(1));
//				result.put("LIUSH", rs.getObject(2));
//				result.put("JIUFDH", rs.getObject(3));
//				result.put("UTH", rs.getObject(4));
//				result.put("BLH", rs.getObject(5));				
//				result.put("ELH", rs.getObject(6));				
//				result.put("PICH", rs.getObject(7));				
//				result.put("CANGKBH", rs.getObject(8));				
//				result.put("ZICKBH", rs.getObject(9));				
//				result.put("DANW", rs.getObject(10));			
//				result.put("CHENGYSMC", rs.getObject(11));
//				result.put("JIUFSJ", rs.getObject(12));
//				result.put("CANGKGLYZ", rs.getObject(13));
//				result.put("SYSDATE", rs.getObject(14));				
//				result.put("CLBZ", rs.getObject(15));
//				result.put("SHIJK", rs.getObject(16));
//				list.add(result);
//			}
//			
//		} catch (SQLException e) {
//			logger.error(e.getMessage());
//		}		
//		return list;
//	}
//	
//	//将仓库中的正常退库数据写入到接口对应表中
//	private void insertFxd() {
//		try{
//			//创建sql语句 查询
//			StringBuffer sb = new StringBuffer();
//			sb.append("INSERT INTO "+SpaceFinal.spacename_ckx+".IN_CK_ZHENGCTK ");
//			sb.append("(USERCENTER, ");
//			sb.append("LIUSH, ");
//			sb.append("KEH, ");
//			sb.append("ZHANGH, ");
//			sb.append("LINGJBH, ");
//			sb.append("LINGJSL, ");
//			sb.append("ELH, ");
//			sb.append("PICH, ");
//			sb.append("GONGYSDM, ");
//			sb.append("CANGKBH, ");
//			sb.append("ZICKBH, ");
//			sb.append("CAOZSJ, ");
//			sb.append("ZHUANGT, ");
//			sb.append("CREATE_TIME, ");
//			sb.append("CANGKGLYZ, ");
//			sb.append("CLZT) ");
//			sb.append(" values( ");
//			for(int i=0;i<16;i++){
//				if(i!=15){
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
//					st.setObject(2, map.get("LIUSH"));
//					st.setObject(3, map.get("JIUFDH"));
//					st.setObject(4, map.get("UTH"));
//					st.setObject(5, map.get("BLH"));
//					st.setObject(6, map.get("ELH"));
//					st.setObject(7, map.get("PICH"));
//					st.setObject(8, map.get("CANGKBH"));					
//					st.setObject(9, map.get("ZICKBH"));
//					st.setObject(10, map.get("DANW"));					
//					st.setObject(11, map.get("CHENGYSMC"));
//					st.setObject(12, map.get("JIUFSJ"));
//					st.setObject(13, map.get("CANGKGLYZ"));
//					st.setObject(14, map.get("SYSDATE"));
//					st.setObject(15, map.get("CLBZ"));
//					st.setObject(16, map.get("SHIJK"));					
//					st.execute();
//				}
//				
//			}
//			
//		}catch(SQLException e){
//			logger.error(e.getMessage());
//		}		
//	}
//	
//	/**
//	 * 为后面改造--读取配置文件的sql
//	 * @return
//	 */
//	private String createSQL() {
//		StringBuffer sb = new StringBuffer();
//		
//		sb.append("INSERT INTO "+SpaceFinal.spacename_ckx+".IN_CK_ZHENGCTK ");
//		sb.append("(USERCENTER, ");
//		sb.append("LIUSH, ");
//		sb.append("KEH, ");
//		sb.append("ZHANGH, ");
//		sb.append("LINGJBH, ");
//		sb.append("LINGJSL, ");
//		sb.append("ELH, ");
//		sb.append("PICH, ");
//		sb.append("GONGYSDM, ");
//		sb.append("CANGKBH, ");
//		sb.append("ZICKBH, ");
//		sb.append("CAOZSJ, ");
//		sb.append("ZHUANGT, ");
//		sb.append("CREATE_TIME, ");
//		sb.append("CANGKGLYZ, ");
//		sb.append("CLZT) ");
//		sb.append("SELECT USERCENTER, ");
//		sb.append(" LIUSH, ");
//		sb.append("KEH, ");
//		sb.append("ZHANGH, ");
//		sb.append("LINGJBH, ");
//		sb.append(" LINGJSL, ");
//		sb.append("ELH, ");
//		sb.append("PICH, ");
//		sb.append("GONGYSDM, ");
//		sb.append("CANGKBH, ");
//		sb.append("ZICKBH, ");
//		sb.append("CAOZSJ, ");
//		sb.append("ZHUANGT, ");
//		sb.append("sysdate, ");//创建时间为当前时间，不是仓库的创建时间
//		sb.append("CANGKGLYZ, ");
//		sb.append("0 ");
//		sb.append("FROM "+SpaceFinal.spacename_ck+".CK_ZHENGCTK ");
//		
//		//添加条件  拿出 在最近一次创建时间和当前时间之间的数据
//		if(CJ_DATE==null){
//			//创建时间为空 则默认拿出当前之前的记录
//			sb.append(" where EDIT_TIME <= sysdate");
//			
//		}else{
//			//不为空  
//			sb.append("WHERE EDIT_TIME >= to_date('"+CJ_DATE+"', 'yyyymmdd HH24:Mi:ss') ");
//			sb.append("and EDIT_TIME <= sysdate ");
//		}
//		
//		System.out.println(sb.toString());
//
//		return sb.toString();
//	}
//
//	//删除接口正常退库表数据
//	private void deleteFxd() {
//		try{
//			//创建sql语句 查询
//			String sql = "delete from "+SpaceFinal.spacename_ckx+".IN_CK_ZHENGCTK";
//			PreparedStatement st = interfaceConn.prepareStatement(sql);
//			st.execute(sql);	
//		}catch(SQLException e){
//			logger.error(e.getMessage());
//		}			
//	}
	
	/**
	 * 调用A39
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
		//输出处理仓库正常退库表 操作码为A39的结果集
		dataEchange.doExchange("2460_CKLSZ_ZCTK_A39",out,getTotal(),dataParserConfig.getWriterConfigs()[0].getUsercenter());
		
	}
}
