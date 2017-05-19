package com.athena.component.output;

import java.io.IOException;

import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
import com.toft.core2.dao.database.DbUtils;
/**
 * 仓库入库明细
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-18
 * 
 * lastModify  By 王冲, 2012-08-31 11:18  内容：注掉全部代码
 */
public class RkmxDataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(RkmxDataWriter.class);	//定义日志方法

	//仓库入库明细构造函数
	public RkmxDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
//		//删除接口仓库对应表数据
//		deleteCkmx();
//		//将仓库中的仓库明细表数据写入到接口对应表中
//		insertCkmx();
	}
	
//	/**
//	 * 删除接口仓库对应表数据
//	 */
//	private void deleteCkmx() {
//		try{
//			//创建sql语句 查询
//			String sql = "delete from "+SpaceFinal.spacename_ckx+".IN_CK_RUKMX";
//			PreparedStatement st = interfaceConn.prepareStatement(sql);
//			st.execute(sql);	
//		}catch(SQLException e){
//			logger.error(e.getMessage());
//		}		
//	}
//	
//	private List<Map<String,Object>> getBesinessDatas(){
//		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//		
//		StringBuffer sb = new StringBuffer();	
//		sb.append("select USERCENTER, ");
//		sb.append("RUKLSH, ");
//		sb.append("UTH, ");
//		sb.append("BLH, ");
//		sb.append("BLSCSJ, ");
//		sb.append("LINGJBH, ");
//		sb.append("LINGJSL, ");
//		sb.append("DANW, ");
//		sb.append("CHANX, ");
//		sb.append("GONGYSDM, ");
//		sb.append("GONGYSLX, ");
//		sb.append("CHENGYSMC, ");
//		sb.append("DINGDH, ");
//		sb.append("ELH, ");
//		sb.append("PICH, ");
//		sb.append("CANGKBH, ");
//		sb.append("ZICKBH, ");
//		sb.append("RUKSJ, ");
//		sb.append("CANGKGLYZ, ");
//		sb.append("RUKLX, ");
//		sb.append("sysdate, ");//创建时间为当前时间，不是仓库的创建时间
//		sb.append("0 ");
//		sb.append("from "+SpaceFinal.spacename_ck+".CK_RUKMX ");
//		
//		try {
//			PreparedStatement st = businessConn.prepareStatement(sb.toString());
//			ResultSet rs = st.executeQuery();
//			
//			while(rs!=null&&rs.next()){
//				Map<String,Object> result = new HashMap<String,Object>();			
//				result.put("USERCENTER", rs.getObject(1));
//				result.put("RUKLSH", rs.getObject(2));
//				result.put("UTH", rs.getObject(3));
//				result.put("BLH", rs.getObject(4));
//				result.put("BLSCSJ", rs.getObject(5));				
//				result.put("LINGJBH", rs.getObject(6));				
//				result.put("LINGJSL", rs.getObject(7));				
//				result.put("DANW", rs.getObject(8));				
//				result.put("CHANX", rs.getObject(9));				
//				result.put("GONGYSDM", rs.getObject(10));			
//				result.put("GONGYSLX", rs.getObject(11));
//				result.put("CHENGYSMC", rs.getObject(12));
//				result.put("DINGDH", rs.getObject(13));
//				result.put("ELH", rs.getObject(14));				
//				result.put("PICH", rs.getObject(15));
//				result.put("CANGKBH", rs.getObject(16));
//				result.put("ZICKBH", rs.getObject(17));
//				result.put("RUKSJ", rs.getObject(18));
//				result.put("CANGKGLYZ", rs.getObject(19));
//				result.put("RUKLX", rs.getObject(20));
//				result.put("sysdate", rs.getObject(21));
//				result.put("clzs", rs.getObject(22));
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
//	 * 将仓库中的仓库明细表数据写入到接口对应表中
//	 */
//	private void insertCkmx() {
//		try{
//			//创建sql语句 查询
//			StringBuffer sb = new StringBuffer();
//			
//			sb.append("insert into "+SpaceFinal.spacename_ckx+".IN_CK_RUKMX ");
//			sb.append("(USERCENTER, ");
//			sb.append("RUKLSH, ");
//			sb.append("UTH, ");
//			sb.append("BLH, ");
//			sb.append("BLSCSJ, ");
//			sb.append("LINGJBH, ");
//			sb.append("LINGJSL, ");
//			sb.append("DANW, ");
//			sb.append("CHANX, ");
//			sb.append("GONGYSDM, ");
//			sb.append("GONGYSLX, ");
//			sb.append("CHENGYSMC, ");
//			sb.append("DINGDH, ");
//			sb.append("ELH, ");
//			sb.append("PICH, ");
//			sb.append("CANGKBH, ");
//			sb.append("ZICKBH, ");
//			sb.append("RUKSJ, ");
//			sb.append("CANGKGLYZ, ");
//			sb.append("RUKLX, ");
//			sb.append("CREATE_TIME, ");
//			sb.append("CLZT) ");
//			sb.append(" values( ");
//			for(int i=0;i<22;i++){
//				if(i!=21){
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
//					st.setObject(2, map.get("RUKLSH"));
//					st.setObject(3, map.get("UTH"));
//					st.setObject(4, map.get("BLH"));
//					st.setObject(5, map.get("BLSCSJ"));
//					st.setObject(6, map.get("LINGJBH"));
//					st.setObject(7, map.get("LINGJSL"));
//					st.setObject(8, map.get("DANW"));					
//					st.setObject(9, map.get("CHANX"));
//					st.setObject(10, map.get("GONGYSDM"));					
//					st.setObject(11, map.get("GONGYSLX"));
//					st.setObject(12, map.get("CHENGYSMC"));
//					st.setObject(13, map.get("DINGDH"));
//					st.setObject(14, map.get("ELH"));
//					st.setObject(15, map.get("PICH"));
//					st.setObject(16, map.get("CANGKBH"));
//					st.setObject(17, map.get("ZICKBH"));
//					st.setObject(18, map.get("RUKSJ"));
//					st.setObject(19, map.get("CANGKGLYZ"));
//					st.setObject(20, map.get("RUKLX"));
//					st.setObject(21, map.get("sysdate"));
//					st.setObject(22, map.get("clzs"));
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
//
//	/**
//	 * 为后面改造--读取配置文件的sql
//	 * @return
//	 */
//	private String createSQL() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("insert into "+SpaceFinal.spacename_ckx+".IN_CK_RUKMX ");
//		sb.append("(USERCENTER, ");
//		sb.append("RUKLSH, ");
//		sb.append("UTH, ");
//		sb.append("BLH, ");
//		sb.append("BLSCSJ, ");
//		sb.append("LINGJBH, ");
//		sb.append("LINGJSL, ");
//		sb.append("DANW, ");
//		sb.append("CHANX, ");
//		sb.append("GONGYSDM, ");
//		sb.append("GONGYSLX, ");
//		sb.append("CHENGYSMC, ");
//		sb.append("DINGDH, ");
//		sb.append("ELH, ");
//		sb.append("PICH, ");
//		sb.append("CANGKBH, ");
//		sb.append("ZICKBH, ");
//		sb.append("RUKSJ, ");
//		sb.append("CANGKGLYZ, ");
//		sb.append("RUKLX, ");
//		sb.append("CREATE_TIME, ");
//		sb.append("CLZT) ");
//		sb.append("select USERCENTER, ");
//		sb.append("RUKLSH, ");
//		sb.append("UTH, ");
//		sb.append("BLH, ");
//		sb.append("BLSCSJ, ");
//		sb.append("LINGJBH, ");
//		sb.append("LINGJSL, ");
//		sb.append("DANW, ");
//		sb.append("CHANX, ");
//		sb.append("GONGYSDM, ");
//		sb.append("GONGYSLX, ");
//		sb.append("CHENGYSMC, ");
//		sb.append("DINGDH, ");
//		sb.append("ELH, ");
//		sb.append("PICH, ");
//		sb.append("CANGKBH, ");
//		sb.append("ZICKBH, ");
//		sb.append("RUKSJ, ");
//		sb.append("CANGKGLYZ, ");
//		sb.append("RUKLX, ");
//		sb.append("sysdate, ");//创建时间为当前时间，不是仓库的创建时间
//		sb.append("0 ");
//		sb.append("from "+SpaceFinal.spacename_ck+".CK_RUKMX ");
//
//		return sb.toString();
//	}
//	
	/**
	 * 改变供应商类型的值  为内部供应商时，填1；否则空值
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean beforeRecord(int rowIndex, Object rowObject) {
		//改变供应商类型的值  为内部供应商时，填1；否则空值
		//得到供应商类型
//		String gongyslx = (String) ((Map)rowObject).get("GONGYSLX");
//		if(gongyslx!=null){
//			if(!"1".equals(gongyslx)){
//				((Map)rowObject).remove("GONGYSLX");
//				((Map)rowObject).put("GONGYSLX", " ");
//			}
//		}
		return super.beforeRecord(rowIndex, rowObject);
	}
	
	/**
	 * 生成文件头 
	 *  文件头格式固定：
	 *  	DEB  HERMES  PSA       FE1172220110124003525
	 * @throws IOException 
	 */
	@Override
	public void fileBefore(OutputStreamWriter writer) {
			try {
				writer.write("DEB  ATHENA  ath1osap03FE11722"+dateToStringYMDHms());
				writer.write("\n");
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
	}
	/**
	 * 将日期转换为yyyy-MM-dd HH:mm:ss字符串
	 * @date 2012-1-18 
	 * @author hzg
	 * @param date
	 * @return String 日期字符串
	 */
	public static String dateToStringYMDHms(){
		DateFormat YMDHmsFormat = new SimpleDateFormat("yyyyMMddHHmmss",Locale.CHINA);
		return YMDHmsFormat.format(new Date());
	}
	
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
		//执行A08,A08S,A10
		dataEchange.doExchange("2460_CKLSZ_FX",out,getTotal(),dataParserConfig.getWriterConfigs()[0].getUsercenter());
		
	}
	
}
