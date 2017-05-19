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
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.txt.TxtDataWriter;
/**
 * 外部要货令 输出接口
 * @author chenlei
 * @vesion 1.0
 * @date 2012-4-26
 */
public class WbyhlDataWriter extends TxtDataWriter {
	protected static Logger logger = Logger.getLogger(WbyhlDataWriter.class);	//定义日志方法

	//缓存map集合
	@SuppressWarnings("rawtypes")
	private Map bufferMap;
	
	//创建时间 格式为:yyyymmdd HH24:Mi:ss
	private String CJ_DATE;
	
	//计算同样零件超过第99条时将V1设置为1的一个临时变量
	private int temp_indexrow = 0;
	//相同零件计数器
	private int LJCount = 0;
	
	public WbyhlDataWriter(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		
		//查找接口表的创建时间 将创建时间设置到变量中
		CJ_DATE = get_CJ_DATE();
		
//		//删除接口外部要货令数据
//		deleteFxd();
//		//将仓库中的外部要货令数据写入到接口对应表中
//		insertFxd();
	}
	
	/**
	 * 得到创建时间
	 * 	从接口表拿  连接使用 接口连接
	 * @return
	 */
	private String get_CJ_DATE() {
		
		String result =null;
		try{
			//创建sql语句 查询
			String sql = "select to_char(y.CREATE_TIME,'yyyymmdd HH24:Mi:ss')  cj_date from "+SpaceFinal.spacename_xqjs+".IN_EDI_YAOHL y order by y.CREATE_TIME asc";
			PreparedStatement st = interfaceConn.prepareStatement(sql);
			ResultSet  rs = st.executeQuery();
			
			if(rs.next()){
				result = rs.getString("cj_date");
			}
		}catch(SQLException e){
			logger.error(e.getMessage());
		}
		return result;
	}
	
	private List<Map<String,Object>> getBesinessDatas(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		StringBuffer sb = new StringBuffer();	
		sb.append(" SELECT t.USERCENTER, ");
		sb.append(" t.YAOHLH, ");
		sb.append(" t.GONGYSDM, ");
		sb.append(" t.JIAOFJ, ");
		sb.append(" t.ZUIZSJ, ");
		sb.append("t.ZUIWSJ, ");
		sb.append(" t.LINGJBH, ");
		sb.append(" t.DANW, ");
		sb.append(" t.YAOHSL, ");
		sb.append(" t.BAOZXH, ");
		sb.append(" t.USXH, ");
		sb.append(" t.UCXH, ");
		sb.append(" t.UCRL, ");
		sb.append(" t.UCGS, ");
		sb.append("t.XIEHD, ");
		sb.append("t.MUDD, ");
		sb.append(" t.DINGDH, ");
		sb.append(" sysdate, ");
		sb.append(" '0' ");
		sb.append("FROM "+SpaceFinal.spacename_xqjs+".CK_YAOHL t left join "+SpaceFinal.spacename_xqjs+".XQJS_DINGD y ");
		sb.append("on y.DINGDH=t.DINGDH and y.DINGDLX='1' or y.DINGDLX='4' and y.DINGDZT='4' and y.SHIFYJSYHL='2'");
		
		//添加条件  拿出 在最近一次创建时间和当前时间之间的数据
		if(CJ_DATE==null){
			//创建时间为空 则默认拿出当前之前的记录
			sb.append(" where t.EDIT_TIME <= sysdate");
			
		}else{
			//不为空  
			sb.append("WHERE t.EDIT_TIME >= to_date('"+CJ_DATE+"', 'yyyymmdd HH24:Mi:ss') ");
			sb.append("and t.EDIT_TIME <= sysdate ");
		}
		
		try {
			PreparedStatement st = businessConn.prepareStatement(sb.toString());
			ResultSet rs = st.executeQuery();
			
			 
			while(rs!=null&&rs.next()){
				Map<String,Object> result = new HashMap<String,Object>();			
				result.put("USERCENTER", rs.getObject(1));
				result.put("YAOHLH", rs.getObject(2));
				result.put("GONGYSDM", rs.getObject(3));
				result.put("JIAOFJ", rs.getObject(4));
				result.put("ZUIZSJ", rs.getObject(5));
				result.put("ZUIWSJ", rs.getObject(6));
				result.put("LINGJBH", rs.getObject(7));
				result.put("DANW", rs.getObject(8));
				result.put("YAOHSL", rs.getObject(9));
				result.put("BAOZXH", rs.getObject(10));
				result.put("USXH", rs.getObject(11));
				result.put("UCXH", rs.getObject(12));
				result.put("UCRL", rs.getObject(13));
				result.put("UCGS", rs.getObject(14));				
				result.put("XIEHD", rs.getObject(15));
				result.put("MUDD", rs.getObject(16));
				result.put("DINGDH", rs.getObject(17));
				result.put("XZDJ", rs.getObject(18));
				result.put("CLBS", rs.getObject(19));		
				list.add(result);
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}		
		return list;
	}
	
	//将仓库中的外部要货令表数据写入到接口对应表中
	private void insertFxd() {
		try{
			//创建sql语句 查询
			StringBuffer sb = new StringBuffer();
			
			sb.append("INSERT INTO "+SpaceFinal.spacename_xqjs+".IN_EDI_YAOHL ");
			sb.append("(USERCENTER, ");
			sb.append(" YAOHLH, ");
			sb.append("GONGYSDM, ");
			sb.append(" JIAOFJ, ");
			sb.append(" ZUIZSJ, ");
			sb.append(" ZUIWSJ, ");
			sb.append(" LINGJBH, ");
			sb.append(" DANW, ");
			sb.append(" YAOHSL, ");
			sb.append("BAOZXH, ");
			sb.append("USXH, ");
			sb.append(" UCXH, ");
			sb.append(" UCRL, ");
			sb.append(" UCGS, ");
			sb.append(" XIEHD, ");
			sb.append(" MUDD, ");
			sb.append("DINGDH, ");
			sb.append("CREATE_TIME) ");
//			sb.append("CLZT) ");
			sb.append(" values( ");
			for(int i=0;i<18;i++){
				if(i!=17){
					sb.append(" ?, ");
				}else{
					sb.append(" ? )");
				}
			}
			
			List<Map<String, Object>> resyltList = getBesinessDatas();
			
			if(getBesinessDatas().size()>0){
				PreparedStatement st = interfaceConn.prepareStatement(sb.toString());
				
				for(Map map: resyltList){		
					st.setObject(1, map.get("USERCENTER"));
					st.setObject(2, map.get("YAOHLH"));
					st.setObject(3, map.get("GONGYSDM"));
					st.setObject(4, map.get("JIAOFJ"));
					st.setObject(5, map.get("ZUIZSJ"));
					st.setObject(6, map.get("ZUIWSJ"));
					st.setObject(7, map.get("LINGJBH"));
					st.setObject(8, map.get("DANW"));
					st.setObject(9, map.get("YAOHSL"));
					st.setObject(10, map.get("BAOZXH"));
					st.setObject(11, map.get("USXH"));
					st.setObject(12, map.get("UCXH"));
					st.setObject(13, map.get("UCRL"));
					st.setObject(14, map.get("UCGS"));
					st.setObject(15, map.get("XIEHD"));
					st.setObject(16, map.get("MUDD"));
					st.setObject(17, map.get("DINGDH"));
					st.setObject(18, map.get("XZDJ"));
	//				st.setObject(19, map.get("CLBS"));
					
					st.execute();
				}
				
			}
			
		}catch(SQLException e){
			logger.error(e.getMessage());
		}			
	}
	
//	/**
//	 * 为后面改造--读取配置文件的sql
//	 * 	添加的为全集 
//	 * @return
//	 */
//	private String createSQL() {
//		StringBuffer sb = new StringBuffer();
//		
//		sb.append("INSERT INTO "+SpaceFinal.spacename_ckx+".IN_EDI_YAOHL ");
//		sb.append("(USERCENTER, ");
//		sb.append(" YAOHLH, ");
//		sb.append("GYSDM, ");
//		sb.append(" JIAOFJ, ");
//		sb.append(" ZUIZSJ, ");
//		sb.append(" ZUIWSJ, ");
//		sb.append(" LINGJBH, ");
//		sb.append(" DANW, ");
//		sb.append(" YAOHSL, ");
//		sb.append("BAOZXH, ");
//		sb.append("USXH, ");
//		sb.append(" UCXH, ");
//		sb.append(" UCRL, ");
//		sb.append(" UCGS, ");
//		sb.append(" XIEHD, ");
//		sb.append(" MUDD, ");
//		sb.append("DINGDH, ");
//		sb.append("CJ_DATE, ");
//		sb.append("CLZT) ");
//		sb.append(" SELECT USERCENTER, ");
//		sb.append(" YAOHLH, ");
//		sb.append(" GONGYSDM, ");
//		sb.append(" JIAOFJ, ");
//		sb.append(" ZUIZSJ, ");
//		sb.append("ZUIWSJ, ");
//		sb.append(" LINGJBH, ");
//		sb.append(" DANW, ");
//		sb.append(" YAOHSL, ");
//		sb.append(" BAOZXH, ");
//		sb.append(" USXH, ");
//		sb.append(" UCXH, ");
//		sb.append(" UCRL, ");
//		sb.append(" UCGS, ");
//		sb.append("XIEHD, ");
//		sb.append("MUDD, ");
//		sb.append(" DINGDH, ");
//		sb.append(" sysdate, ");
//		sb.append(" '0' ");
//		sb.append("FROM "+SpaceFinal.spacename_ck+".CK_YAOHL ");
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
//		return sb.toString();
//	}

	//删除接口外部要货令表表数据
	//使用接口表 连接
	private void deleteFxd() {
		try{
			//创建sql语句 查询
			String sql = "delete from "+SpaceFinal.spacename_xqjs+".IN_EDI_YAOHL";
			PreparedStatement st = interfaceConn.prepareStatement(sql);
			st.execute(sql);	
		}catch(SQLException e){
			logger.error(e.getMessage());
		}			
	}
	
	
	/**
	 * 对数据 在数据号 和 用户中心生成 字符 
	 *  即对： SCHMD 字段重新生成
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void afterRecord(int rowIndex, Record record, Object line) {
		
		if (line instanceof Map) {
			Map rowObject  = (Map)line;
			createYAOHL(rowObject);
			if(rowIndex==1){
				//为第一行 则只是将记录保存到 缓冲变量中，不执行写文件
				modify( rowObject, rowIndex) ;
				bufferMap = rowObject;
			}else if(rowIndex!=-1){
				//不为第一行，则将缓存数据和传进的数据比对后，写入缓存数据，并将传进数据写入缓存
				//changeBufferMap(bufferMap,rowObject,rowIndex);
				modify(bufferMap, rowObject, rowIndex) ;
				super.afterRecord(rowIndex, record, bufferMap);
				bufferMap = rowObject;
			}

        if(rowIndex==-1){ 
				//为-1 则为最后一条 输出最后一条
//				rowObject.remove("SCHMD");
				//只设置V3为1
				rowObject.put("SCHMD", "000100");			
				super.afterRecord(rowIndex, record, rowObject);
			}
		}
	}
	
//	/**
//	 * 改变bufferMap2中的SCHMD字段值
//	 * @param bufferMap2
//	 * @param rowObject
//	 */
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	private void changeBufferMap(Map bufferMap2, Map rowObject,int rowIndex) {
//		StringBuffer sb = new StringBuffer();
//		
//		//得到用户中心
//		String userCenter_buffer = ((String)bufferMap2.get("FSQDM")).trim();
//		String userCenter_row = ((String)rowObject.get("FSQDM")).trim();
//		
//		//拿到供应商代码
//		String gys_buffer  = (String) bufferMap2.get("XSSDM");
//		String gys_row = (String) rowObject.get("XSSDM");
//		
//		//零件号
//		String ljs_buffer = (String) bufferMap2.get("GMSLJH");
//		String ljs_row = (String) rowObject.get("GMSLJH");
//			
//		//订单号
//		String ddh_buffer= (String) bufferMap2.get("DDH");
//		String ddh_row = (String) rowObject.get("DDH");
//		
//		//卸货点XHD
//		String xhd_buffer= (String) bufferMap2.get("XHD");
//		String xhd_row = (String) rowObject.get("XHD");
//		
//		//um ,uc 本条比较
//		String um_buffer= (String) rowObject.get("UMLX");
//		String uc_buffer =(String) rowObject.get("UCLX");
//			
//		
//		//生成V0字符
//		if(rowIndex-1 == 1){
//			replace(0, rowObject, "1") ;
////			sb.append("1");
//		}else {
//			if(userCenter_buffer.equals(userCenter_row)){
//				createV(sb,gys_buffer,gys_row);
//			}else{
//				createV(sb,userCenter_buffer,userCenter_row);
//			}
//		}
//		
//		//生成V1字符
//		if(!isEquals(ljs_buffer, ljs_row) || !isEquals(ddh_row, ddh_buffer)||!isEquals(xhd_row, xhd_buffer)){
//			//订单号或者零件号不相等，则写入1
//			sb.append("1");
//		}else{
//			sb.append("0");
//		}
//		
//		//生成V2 没有判断 则默认生成0；
//		sb.append("0");
//		
//		//生成V3
//		if(!isEquals(gys_row, gys_buffer) || rowIndex==getTotal()||!isEquals(userCenter_row, userCenter_buffer)){
//			//供应商不相等 或者为最后一条
//			sb.append("1");
//		}else{
//			sb.append("0");
//		}
//		
//		//生成V4
//		if(isEquals(um_buffer, uc_buffer)){
//			//相等
//			sb.append("0");
//		}else{
//			//不相等
//			sb.append("1");
//		}
//		
//		//生成V5 零件号发生改变
//		if(isEquals(ljs_row, ljs_buffer)||isEquals(xhd_row, xhd_buffer)){
//			sb.append("0");
//		}else{
//			sb.append("1");
//		}
//		
//		//将bufferMap2中的只改变
//		bufferMap2.remove("SCHMD");
//		bufferMap2.put("SCHMD", sb.toString());
//	}
//	
	/**
	 * 输出最后一条数据
	 */
	@Override
	public void fileAfter(OutputStreamWriter out) {
		//将最后一条缓存输出出去
		afterRecord(-1,new Record(),bufferMap);
		super.fileAfter(out);
	}

	/**
	 * 两者是否相等
	 * @param str1
	 * @param str2
	 * @return
	 */
	public boolean isEquals(String str1,String str2){
		boolean result = true;
		if(str1!=null){
			if(!str1.equals(str2)){
				//不相同
				result = false;
			}
		}else{
			//为空
			if(str2!=null){
				//两者不相同
				result  = false;
			}
		}	
			
		return result;
	}
	
	/**
	 * str1和str2不相等，就像sb写入1；相等就写入0
	 * @param sb
	 * @param str1
	 * @param str2
	 */
	public void createV(StringBuffer sb,String str1,String str2){
		if(str1!=null){
			if(str1.equals(str2)){
				//相同
				sb.append("0");
			}else{
				//不相同
				sb.append("1");
			}
		}else{
			//为空
			if(str2==null){
				//两者都为空
				sb.append("0");
			}else{
				//两者不相同
				sb.append("1");
			}
		}	
	}
	
	/**
	 * 对外部要货令表里提取的要货令号第一位字符为’L’进行处理
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createYAOHL(Map rowObject){
		//得到要货令
		String YAOHL = (String) rowObject.get("ZLHKH");
		String YAOHLStr = "";
//		if(YAOHL.toUpperCase().startsWith("L")){
//			YAOHLStr = "W"+YAOHL.substring(1, YAOHL.trim().length()-2)+"03";
//			rowObject.put("ZLHKH", YAOHLStr);
//		}
		
		//截取要货令最后一位后，判断用户中心是否为L，为L，则将后两位00修改为03
//		if(YAOHL.toUpperCase().endsWith("P")){
//			YAOHLStr = YAOHL.substring(0, YAOHL.trim().length()-1);
//			rowObject.put("ZLHKH", YAOHLStr);
//		}
		
	}

	
	
	
	
	
	
	
	
	
	/**last addd method  by 王冲   2012-09-24 11:01**/
	
	
	
	public static void  main(String a[]){
		StringBuffer s= new StringBuffer("000000");
		System.out.print(s.replace(5, 6, "1")) ;
		
	}
	
	/**
	 * 修改V0.V1.V2.V4
	 * @param rowObject
	 * @param rowIndex
	 */
	public void modify(  Map rowObject,int rowIndex){

			
		//um ,uc 本条比较
		String um_buffer= (String) rowObject.get("UMLX");
		String uc_buffer =(String) rowObject.get("UCLX");
			
		
		//生成V0字符
		replace(0, rowObject, "1") ;
		
		//订单号或者零件号不相等，则写入1
		replace(1, rowObject, "1") ;
		
		//生成V2 没有判断 则默认生成0；
		replace(2, rowObject, "0") ;
		
		//生成V4
		if(isEquals(um_buffer, uc_buffer)){
			//相等
			replace(4, rowObject, "0") ;
			rowObject.put("UMLX", "");//如果UC型号和UA型号相同，不输出UC
			rowObject.put("FZDLC2", ""); //不输出负责代理处 92
			rowObject.put("BZJB2", "");  //不输出包装级别  3
		}else{
			//不相等
			replace(4, rowObject, "1") ;
		}
		
	}
	
	/**
	 * 修改V0.V1.V2.V4
	 * @param rowObject
	 * @param rowIndex
	 */
	@SuppressWarnings("unchecked")
	public void modify( Map bufferMap2, Map rowObject,int rowIndex){
		//得到用户中心
		String userCenter_buffer = ((String)bufferMap2.get("FSQDM")).trim();
		String userCenter_row = ((String)rowObject.get("FSQDM")).trim();
		
		//卸货点XHD
		String xhd_buffer= (String) bufferMap2.get("XHD");
		String xhd_row = (String) rowObject.get("XHD");
		
		//拿到供应商代码
		String gys_buffer  = (String) bufferMap2.get("JSQDM");
		String gys_row = (String) rowObject.get("JSQDM");
		
		//零件号
		String ljs_buffer = (String) bufferMap2.get("GMSLJH");
		String ljs_row = (String) rowObject.get("GMSLJH");
			
		//订单号
		String ddh_buffer= (String) bufferMap2.get("DDH");
		String ddh_row = (String) rowObject.get("DDH");
			
		//um ,uc 本条比较
		String um_buffer= (String) rowObject.get("UMLX");
		String uc_buffer =(String) rowObject.get("UCLX");
			
		
		//生成V0字符
		if(rowIndex == 1){
			replace(0, rowObject, "1") ;
		}else {
			//replace(0, rowObject,createV(gys_buffer,gys_row)) ;
			if(!isEquals(gys_buffer, gys_row) || !isEquals(userCenter_buffer, userCenter_row)){
				//订单号或者零件号不相等，则写入1
				replace(0, rowObject, "1") ;
			}else{
				replace(0, rowObject, "0") ;
			}
		}
		
		//生成V1字符
		if(!isEquals(ljs_buffer, ljs_row) || !isEquals(ddh_row, ddh_buffer)||!isEquals(xhd_buffer, xhd_row)){
			//订单号或者零件号不相等，则写入1
			replace(1, rowObject, "1") ;
		}else{
			replace(1, rowObject, "0") ;
		}
		
		//生成V2 没有判断 则默认生成0；
		replace(2, rowObject, "0") ;
		
		//生成V4
		if(isEquals(um_buffer, uc_buffer)){
			//相等
			replace(4, rowObject, "0") ;
			rowObject.put("UMLX", "");//如果UC型号和UA型号相同，不输出UC
			rowObject.put("FZDLC2", ""); //不输出负责代理处 92
			rowObject.put("BZJB2", "");  //不输出包装级别  3
		}else{
			//不相等
			replace(4, rowObject, "1") ;
		}
		
		//生成V3
		if(!isEquals(gys_row, gys_buffer)){
			//供应商不相等 将上一条数据设为1
			replace(3, bufferMap2, "1") ;
		}else{
			replace(3, bufferMap2, "0") ;
		}
		//生成V3
		//用户中心发生变化将本条设置为1，否则为0
		if(!isEquals(userCenter_row, userCenter_buffer)){
			replace(3, rowObject, "1") ;
		}else{
			replace(3, rowObject, "0") ;
		}
		
		//生成V5
		if(!isEquals(ljs_row, ljs_buffer)||!isEquals(xhd_row, xhd_buffer)){
			replace(5, bufferMap2, "1") ;
			if(rowIndex == -1){
				replace(5, rowObject, "1");
			}
		}else{
			replace(5, bufferMap2, "0") ;
		}
		if(isEquals(ljs_row, ljs_buffer)){
			LJCount ++;//统计相同零件个数
		}else{
			LJCount = 0;
		}
//		//生成V5 
//		if(isEquals(ljs_row, ljs_buffer)||isEquals(xhd_row, xhd_buffer)){
//			LJCount ++;//统计相同零件个数
//			replace(5, bufferMap2, "0") ;
//		}else{
//			replace(5, bufferMap2, "1") ;  //零件号或卸货点发生变化将上一条设置为1
//			if(rowIndex==-1){
//				replace(5, rowObject, "1") ;
//			}
//			LJCount = 0;
//		}

		//V5 同样的零件，过了99条，按照零件变化进行标号 2012-12-24 hzg
		if(LJCount==99){
			replace(5, bufferMap2, "1");
			temp_indexrow = rowIndex+1;
			LJCount = 0;
		}
		//V1 相同零件超过了99条，将下一个零件设置为新零件，所以将V1设置为1  2012-12-24 hzg
		if(temp_indexrow>0&&rowIndex==temp_indexrow){
			replace(1, bufferMap2, "1");
		}
		
	}
	
	
	
	
	public  void replace(int i,Map<String,String> obj,String value){
		StringBuffer ss= new StringBuffer(obj.get("SCHMD"));
		obj.put("SCHMD", ss.replace(i, i+1, value).toString() );
	}
	
	/**
	 * str1和str2不相等，就像sb写入1；相等就写入0
	 * @param sb
	 * @param str1
	 * @param str2
	 */
	public String createV(String str1,String str2){
		if(str1!=null){
			if(str1.equals(str2)){
				//相同
				return  "0";
			}else{
				//不相同.
				return  "1";
			}
		}else{
			//为空
			if(str2==null){
				//两者都为空
				return  "0";
			}else{
				//两者不相同
				return  "1";			}
		}	
	}
}
