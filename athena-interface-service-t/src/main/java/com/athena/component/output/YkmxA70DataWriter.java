package com.athena.component.output;

import java.io.IOException;
import java.io.OutputStreamWriter;
import org.apache.log4j.Logger;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;


/**
 * A35备货单输出
 * 
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-20
 * lastModify  By 王冲, 2012-08-31 13:18  内容：注掉全部代码
 */
public class YkmxA70DataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(RkmxA35DataWriter.class);	//定义日志方法

	// 创建时间 格式为:yyyymmdd HH24:Mi:ss
	//private String CJ_DATE;

	public YkmxA70DataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
//
//		// 查找接口表的创建时间 将创建时间设置到变量中
//		CJ_DATE = get_CJ_DATE();
//
//		// 删除接口备货单表数据
//		deleteFxd();
//		// 将仓库中的备货单数据写入到接口对应表中
//		insertFxd();
	}
//
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
//			String sql = "select to_char(y.CREATE_TIME,'yyyymmdd HH24:Mi:ss')  cj_date from "+SpaceFinal.spacename_ckx+".IN_CK_BEIHD y order by y.CREATE_TIME asc";
//			PreparedStatement st = interfaceConn.prepareStatement(sql);
//			ResultSet rs = st.executeQuery();
//
//			if (rs.next()) {
//				result = rs.getString("cj_date");
//			}
//		} catch (SQLException e) {
//			logger.error(e.getMessage());
//		}
//		return result;
//
//	}
//	
//	private List<Map<String,Object>> getBesinessDatas(){
//		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//		
//		StringBuffer sb = new StringBuffer();	
//		sb.append(" select d.usercenter, ");
//		sb.append("d.beihdh, ");
//		sb.append("m.liush, ");
//		sb.append("d.CANGKBH, ");
//		sb.append("d.ZICKBH, ");
//		sb.append(" d.KEH, ");
//		sb.append(" d.ZHANGH, ");
//		sb.append("  d.CHUKSJ, ");
//		sb.append("  d.CANGKGLYZ, ");
//		sb.append("m.ELH, ");
//		sb.append(" m.PICH, ");
//		sb.append(" m.LINGJBH, ");
//		sb.append(" m.DANW, ");
//		sb.append("m.DINGDH, ");
//		sb.append("m.GONGYSDM, ");
//		sb.append(" m.SHIFSL, ");
//		sb.append("sysdate, ");// 创建时间为当前时间，不是仓库的创建时间
//		sb.append("0 ");
//		sb.append("from "+SpaceFinal.spacename_ck+".CK_BEIHD d ");
//		sb.append(" join "+SpaceFinal.spacename_ck+".CK_BEIHDMX m on d.usercenter = m.usercenter ");
//		sb.append(" and d.BEIHDH = m.beihdh ");
//
//		// 添加条件 拿出 在最近一次创建时间和当前时间之间的数据
//		if (CJ_DATE == null) {
//			// 创建时间为空 则默认拿出当前之前的记录
//			sb.append(" and m.EDIT_TIME <= sysdate");
//
//		} else {
//			// 不为空
//			sb.append("and m.EDIT_TIME >= to_date('" + CJ_DATE
//					+ "', 'yyyymmdd HH24:Mi:ss') ");
//			sb.append("and m.EDIT_TIME <= sysdate ");
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
//				result.put("LINGJBH", rs.getObject(17));
//				result.put("GONGYSDM", rs.getObject(18));	
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
//	 * 将仓库中的备货单数据写入到接口对应表中
//	 */
//	@SuppressWarnings("rawtypes")
//	private void insertFxd() {
//		try{
//			//创建sql语句 查询
//			StringBuffer sb = new StringBuffer();
//			
//			sb.append("INSERT INTO "+SpaceFinal.spacename_ckx+".IN_CK_BEIHD ");
//			sb.append("(USERCENTER, ");
//			sb.append(" BEIHDH, ");
//			sb.append(" LIUSH, ");
//			sb.append(" CANGKBH, ");
//			sb.append(" ZICKBH, ");
//			sb.append(" KEH, ");
//			sb.append(" ZHANGH, ");
//			sb.append("CHUKSJ, ");
//			sb.append("CANGKGLYZ, ");
//			sb.append("ELH, ");
//			sb.append("PICH, ");
//			sb.append("LINGJBH, ");
//			sb.append("DANW, ");
//			sb.append("DINGDH, ");
//			sb.append("GONGYSDM, ");
//			sb.append("SHIFSL, ");
//			sb.append("CREATE_TIME, ");
//			sb.append("CLZT) ");
//			sb.append(" values( ");
//			for(int i=0;i<18;i++){
//				if(i!=17){
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
//					st.setObject(17, map.get("LINGJBH"));
//					st.setObject(18, map.get("GONGYSDM"));
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
//
//	/**
//	 * 为后面改造--读取配置文件的sql
//	 * 
//	 * @return
//	 */
//	private String createSQL() {
//		StringBuffer sb = new StringBuffer();
//
//		// 根据用户中心 备货单号做关联 拿出全集
//		sb.append("INSERT INTO "+SpaceFinal.spacename_ckx+".IN_CK_BEIHD ");
//		sb.append("(USERCENTER, ");
//		sb.append(" BEIHDH, ");
//		sb.append(" LIUSH, ");
//		sb.append(" CANGKBH, ");
//		sb.append(" ZICKBH, ");
//		sb.append(" KEH, ");
//		sb.append(" ZHANGH, ");
//		sb.append("CHUKSJ, ");
//		sb.append("CANGKGLYZ, ");
//		sb.append("ELH, ");
//		sb.append("PICH, ");
//		sb.append("LINGJBH, ");
//		sb.append("DANW, ");
//		sb.append("DINGDH, ");
//		sb.append("GONGYSDM, ");
//		sb.append("SHIFSL, ");
//		sb.append("CREATE_TIME, ");
//		sb.append("CLZT) ");
//		sb.append(" select d.usercenter, ");
//		sb.append("d.beihdh, ");
//		sb.append("m.liush, ");
//		sb.append("d.CANGKBH, ");
//		sb.append("d.ZICKBH, ");
//		sb.append(" d.KEH, ");
//		sb.append(" d.ZHANGH, ");
//		sb.append("  d.CHUKSJ, ");
//		sb.append("  d.CANGKGLYZ, ");
//		sb.append("m.ELH, ");
//		sb.append(" m.PICH, ");
//		sb.append(" m.LINGJBH, ");
//		sb.append(" m.DANW, ");
//		sb.append("m.DINGDH, ");
//		sb.append("m.GONGYSDM, ");
//		sb.append(" m.SHIFSL, ");
//		sb.append("sysdate, ");// 创建时间为当前时间，不是仓库的创建时间
//		sb.append("0 ");
//		sb.append("from "+SpaceFinal.spacename_ck+".CK_BEIHD d ");
//		sb.append(" join "+SpaceFinal.spacename_ck+".CK_BEIHDMX m on d.usercenter = m.usercenter ");
//		sb.append(" and d.BEIHDH = m.beihdh ");
//
//		// 添加条件 拿出 在最近一次创建时间和当前时间之间的数据
//		if (CJ_DATE == null) {
//			// 创建时间为空 则默认拿出当前之前的记录
//			sb.append(" and m.EDIT_TIME <= sysdate");
//
//		} else {
//			// 不为空
//			sb.append("and m.EDIT_TIME >= to_date('" + CJ_DATE
//					+ "', 'yyyymmdd HH24:Mi:ss') ");
//			sb.append("and m.EDIT_TIME <= sysdate ");
//		}
//
//		System.out.println(sb.toString());
//
//		return sb.toString();
//	}

//	/**
//	 * 删除接口备货单表数据
//	 */
//	private void deleteFxd() {
//		try {
//			// 创建sql语句 查询
//			String sql = "delete from "+SpaceFinal.spacename_ckx+".IN_CK_BEIHD";
//			PreparedStatement st = interfaceConn.prepareStatement(sql);
//			st.execute(sql);
//		} catch (SQLException e) {
//			logger.error(e.getMessage());
//		}
//	}


	/**
	 * 后期要新增A70 A90 补充 现在直接终止
	 */
	@Override
	public void fileAfter(OutputStreamWriter out) {
		try {
			// 生成文件尾部
			out.write(makefileEnd("FIN  ATHENA  ath1osap03FE11722"));
			out.write("\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 生成文件尾部
	 * 
	 * @param str
	 * @return
	 */
	public String makefileEnd(String str) {
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		
		String total_Str = String.valueOf(getTotal());
		
		//String total_Str1 = String.valueOf(dataEchange.total);
		if (total_Str != null) {
			if (total_Str.length() < 9) {
				// 生成9位长的字段
				int length = 9 - total_Str.length();
				for (int i = 0; i < length; i++) {
					sb.append("0");
				}
				sb.append(total_Str);
			} else {
				// 截取后九尾
				sb.append(total_Str.substring(0, 8));
			}
		} else {
			sb.append("000000000");
		}
		return sb.toString();
	}
	
}
