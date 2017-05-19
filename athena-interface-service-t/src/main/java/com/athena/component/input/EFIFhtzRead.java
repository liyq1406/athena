package com.athena.component.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.DataExchange;
import com.athena.component.exchange.FileLog;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.component.exchange.txt.TxtDataReader;
import com.toft.core2.dao.database.DbUtils;


/**
 * EFI发货通知接口输入类
 * update 简化if判断语句，合并拆分方法 hzg  2012-10-19
 * @author GJ
 * @update 代码优化   hzg  2012-10-19
 */
public class EFIFhtzRead extends TxtDataReader {
	/**
	 * 数据库连接初始化
	 */
	private Connection conn = null;
	private PreparedStatement prep=null;
	protected static Logger logger = Logger.getLogger(EFIFhtzRead.class);	//定义日志方法
	public String datasourceId=null;

	private static final int INSERT_FLAG_ALL =1;       //交付单号为空，全部插入三张表（FYZTXX,FYMX,BZDYXX）
	private static final int INSERT_FLAG_MXBZ =2;      //有交付单号，并且发运明细表不存在序号和交付单号，则插入两张表（FYMX,BZDYXX）
	private static final int INSERT_FLAG_BZ =3;        //有交付单号，并且存在序号和交付单号以及UA号，则插入一张表（BZDYXX）
	private static final int UPDATE_FLAG_BZ =4;        //更新EFI发货通知-包装对应信息表数据
	private static final String INSERT_FLAG_TBLJ ="1"; //插入数据到发货通知-同步零件信息表
	// 定义解析的字段
	private String jfdh = null;// 交付单号
	private String jf_date = null;// 交付时间
	private String fyzmzdw = null;// 发运总毛重单位
	private String fyyjdd_date = null;// 发运预计到达日期时间
	private String cysdm = null;// 承运商代码
	private String jszdm = null;// 接收者代码
	private String xzddm = null;// 卸载点代码
	private String fhrdm = null;// 发货人代码
	private String jzxh = null;// 集装箱号
	private String sftbfhtz = null;// 是否同步发货通知
	private String sfgzxlh = null;// 是否关注序列号
	private String xuh = null;// 序号
	private double d_fysl=0.0;//发运的数量
	private String jldw = null;// 发运数量计量单位
	private String bzlx = null;// 包装类型
	private String ljh = null;// 零件号
	private String ddh = null;// 订单号
	private String lygj = null;// 零件来源国家
	private String ua = null;// UA
	private String ualx = null;// UA类型
	private String uc = null;// UC
	private String uclx = null;// UC类型
	private double d_ucljsl = 0.0;// UC零件数量
	private double d_ualjsl = 0.0;// UA零件数量
	private String yhl = null;// 要货令
	private String pzh = null;// 批组号
	private String xlh = null;// 序列号
	private String ggph=null;//规格牌号
	//新增长宽高add by 潘瑞
	private String chang = null;//长
	private String kuan = null; //宽
	private String gao = null; //高
	private String gys = null;// 供应商
	private String lsh = null;// 流水号
	private int shul = 0;// 数量
	
	private String ErrorMessage=null;//错误信息
	private int num = 0;//插入总行数
	private int ztxx_num = 0;//插入主体信息行数
	private int fymx_num = 0;//插入发运明细行数
	private int bzxx_num = 0;//插入包装信息行数
	private int ubzxx_num = 0;//更新包装信息行数
	private int tbxx_num = 0;//插入同步信息行数
    private int ERROR_COUNT = 0;//错误信息行数
    private DataParserConfig dataParserConfig=null;//初始化配置文件信息
    
    private String sameJiaofdh = "";
    
	public EFIFhtzRead(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// 获取数据库连接
		datasourceId=dataParserConfig.getWriterConfig().getDatasourceId();
		conn = DbUtils.getConnection(datasourceId);
	}



	/**
	 * 读取文本
	 */
	@SuppressWarnings("finally")
	@Override
	public int readLine() {
		// 读取文件
		FileInputStream fs=null;
		InputStreamReader is=null;
		BufferedReader br=null;
		File f=null;
		String f_name=null;//文件名称
		String FileBeginTime=DataExchange.RunStartTime;//接口文件开始运行时间
		String FileEndTime=null;//文件运行结束时间 
		String SID=DbDataWriter.getUUID();//唯一标示
		String EID=DbDataWriter.getUUID();//唯一标示
		//add by pan.rui
		List<String> dataList = new ArrayList<String>();

		try {
			// 获取配置
			dataParserConfig = getDataParserConfig();
			f=new File(dataParserConfig.getReaderConfig().getFilePath());
			String filename=f.getName();
			String[] fname=filename.split(",");
			for (int i = 0; i < fname.length; i++) {
				f=new File("/users/ath00/tmp/"+fname[i]+".txt");
				f_name=f.getName();
				if(f.exists()){
					fs = new FileInputStream(f.getPath());
					is = new InputStreamReader(fs, dataParserConfig.getReaderConfig().getEncoding());
					br = new BufferedReader(is);
					String line = "";
					//开启事物
					conn.setAutoCommit(false);
					//一个交付单号可对应多个零件，一个零件可以对应多个UA
					while (null != (line = br.readLine())) {// 解析文本
						//参数解析
						this.ParseParameters(line);
						
						String jXhu = xuh + jfdh;
						String jxu = xuh + jfdh +ua;
						if(sameJiaofdh.equals(jfdh)){//文本中的交付单号和数据库中的交付单号相同
							continue;
						}
							if(!dataList.contains(jfdh)){
								//查询数据库中是否已存在相同数据，存在则记Log日志
								String jiaofdh = queryJiaofdh(jfdh);
								if("".equals(jiaofdh)){//数据库中不存在文本中的数据，写数据库
									//交付单号为空，则为第一条数据，全部插入
									insert(INSERT_FLAG_ALL);
								}else{//重复数据，写Log日志
									this.writeLog();
									sameJiaofdh = jiaofdh;
									continue;
								}
							}else if(dataList.contains(jfdh)&&!dataList.contains(jXhu)){
								//不插入IN_EFI_FHTZ_FYZTXX，插入明细和包装表
								insert(INSERT_FLAG_MXBZ);
							}else if(dataList.contains(jfdh)&&dataList.contains(jXhu)){
								//如果xuh_jfdh_ua相同的话，则更新
								if(dataList.contains(jxu)){
									insert(UPDATE_FLAG_BZ);
								}else{
									//插入IN_EFI_FHTZ_BZDYXX
									insert(INSERT_FLAG_BZ);
								}
							}else{
								//全部插入
								insert(INSERT_FLAG_ALL);
							}
							if(!dataList.contains(jfdh)){
								dataList.add(jfdh);
							}
							if(!dataList.contains(jXhu)){
								dataList.add(jXhu);
							}
							if(!dataList.contains(jxu)){
								dataList.add(jxu);
							}
					}
					conn.commit();//事物提交

					FileEndTime=DateTimeUtil.getAllCurrTime();//文件运行结束时间  
					num=ztxx_num+fymx_num+bzxx_num+tbxx_num;//插入表数据总行数
					FileLog.getInstance(datasourceId).File_info(SID, "2040", f_name, FileBeginTime, FileEndTime, num, ubzxx_num, ERROR_COUNT, "1");//记录文件信息日志
				}
			} 
		}catch (Exception e) {
			conn.rollback();
			ErrorMessage=e.getMessage();//记录错误原因
			FileEndTime=DateTimeUtil.getAllCurrTime();//文件运行结束时间
			FileLog.getInstance(datasourceId).File_info(SID, "2040", f_name, FileBeginTime, FileEndTime, num, ubzxx_num, ERROR_COUNT, "-1");//记录文件信息日志
			FileLog.getInstance(datasourceId).File_ErrorInfo(EID, "2040", SID, ErrorMessage, "");//记录错误信息日志
			logger.error(e.getMessage());
		} finally {
			try {
				if(null!=fs){
					fs.close();
				}
				if (null != is) {
					is.close();
				}
				if (null != br) {
					br.close();
				}
				if(null!=prep){
					prep.close();
				}
				if(null!=conn){
					conn.close();
				}
				DbUtils.freeConnection(conn);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			return 0;
		}
	}
	
	/**
	 * 读取的行参数解析
	 * @author 贺志国
	 * @date 2012-10-19
	 * @param line 文本行数据
	 */
	public void ParseParameters(String line ){
		jfdh = line.substring(0, 17).trim();// 交付单号
		jf_date = line.substring(17, 31).trim();// 交付时间
		fyzmzdw = line.substring(31, 34).trim();// 发运总毛重单位
		fyyjdd_date = line.substring(34, 48).trim();// 发运预计到达日期时间
		cysdm = line.substring(48, 58).trim();// 承运商代码
		jszdm = line.substring(58, 78).trim();// 接收者代码
		xzddm = line.substring(78, 95).trim();// 卸载点代码
		fhrdm = line.substring(95, 115).trim();// 发货人代码
		jzxh = line.substring(115, 132).trim();// 集装箱号
		String tmp_sftbfhtz = line.substring(132, 134).trim();// 是否同步发货通知(1为不同步)
		sftbfhtz = "1".equals(tmp_sftbfhtz)?"0":"1"; // 是否同步发货通知，插入数据库要为0，所以此处作转换
		sfgzxlh = line.substring(134, 136).trim();// 是否关注序列号(1为关注)
		xuh = line.substring(136, 142).trim();// 序号
		String str_fysl =  line.substring(143, 157).trim(); // 发运的数量
		double fysl = "".equals(str_fysl)?0:Double.parseDouble(str_fysl);
		d_fysl=fysl/1;
		jldw = line.substring(157, 160).trim();// 发运数量计量单位
		bzlx = line.substring(160, 177).trim();// 包装类型
		String ljh1 = line.substring(177, 212).trim();// 零件号
		ljh = "".equals(ljh1)?"":ljh1.substring(0, 10);
		ddh = line.substring(212, 229).trim();// 订单号
		lygj = line.substring(229, 232).trim();// 零件来源国家(位置来源)
		ua = line.substring(232, 249).trim();// UA
		ualx = line.substring(249, 254).trim();// UA类型
		uc = line.substring(254, 271).trim();// UC
		uclx = line.substring(271, 276).trim();// UC类型
		String uc_ljsl = line.substring(276, 286).trim(); // UC零件数量
		/**** 增加ua_ljsl 并优化代码   hzg 2012-10-19 ****/
		String ua_ljsl = line.substring(286, 296).trim(); // UA零件数量
		double ucljsl = "".equals(uc_ljsl)?0:Double.parseDouble(uc_ljsl);
		d_ucljsl=ucljsl/1;
		double ualjsl = "".equals(ua_ljsl)?0:Double.parseDouble(ua_ljsl);// UA零件数量 ucljsl->ualjsl
		d_ualjsl=ualjsl/1;
		yhl = line.substring(296, 309).trim();// 要货令
		pzh = line.substring(309, 326).trim();// 批组号
		xlh = line.substring(326, 339).trim();// 序列号
		ggph=line.substring(339, 359).trim();//规格牌号
		//新增长宽高三个值
		chang = line.substring(359, 369).trim();//长
		kuan = line.substring(369,379).trim();//宽
		gao = line.substring(379,389).trim();//高
		gys = line.substring(389, 399).trim();// 供应商
		lsh = line.substring(399, 408).trim();// 流水号
		String t_shul = strNull(line.substring(408, 414).trim());
		shul = "".equals(t_shul)?0:Integer.parseInt(t_shul);// 数量	
	}
	
	/**
	 * 查询卸货站台编号
	 * @author 贺志国
	 * @date 2013-1-9
	 * @param jiaofdh 交付单号
	 * @return String 查询到的交付单号
	 * @throws SQLException
	 */
	private String  queryJiaofdh(String jiaofdh) throws SQLException{
		StringBuilder sbuf = new StringBuilder();
		String strJfd = "";
		sbuf.append("select JFDH from "+SpaceFinal.spacename_ck+".in_efi_fhtz_fyztxx ");
		sbuf.append("where JFDH='" + strNull(jfdh) + "'");
		prep=conn.prepareStatement(sbuf.toString());
		ResultSet rs = prep.executeQuery();
		while(rs.next()){
			strJfd = rs.getString("JFDH");
		}
		prep.close();
		rs.close();
		return strJfd;
}
	/**
	 * 写LOG日志
	 * @author 贺志国
	 * @date 2013-1-10
	 */
	private void writeLog(){
		StringBuilder buffer = new StringBuilder();
		buffer.append("交付单号已存在，交付单号jfdh="+jfdh);
		buffer.append(",jf_date="+strNull(jfdh)+",jf_date="+strNull(DateTimeUtil.SubString(jf_date)));
		buffer.append(",fyzmzdw="+strNull(fyzmzdw)+",fyyjdd_date="+strNull(DateTimeUtil.SubString(fyyjdd_date)));
		buffer.append(",cysdm="+strNull(cysdm)+",jszdm="+strNull(jszdm)+",xzddm="+strNull(xzddm));
		buffer.append(",fhrdm="+strNull(fhrdm)+",jzxh="+strNull(jzxh)+",sftbfhtz="+strNull(sftbfhtz));
		buffer.append(",sfgzxlh="+strNull(sfgzxlh)+",clzt=0");
		logger.info(buffer.toString());
	}
	/**
	 * 数据插入
	 * @author 贺志国
	 * @date 2012-10-19
	 * @param flag 1:插入发运主体信息表，2：插入发运明细表，3：插入包装对应信息表，4：更新包装对应信息表
	 * @throws SQLException
	 */
	public void insert(int flag) throws SQLException{
		try {
			if(flag==1){
				StringBuffer str = new StringBuffer();
				// 插入数据到EFI发货通知-发运主体信息表
				str.append("insert into "+SpaceFinal.spacename_ck+".in_efi_fhtz_fyztxx");
				str.append("(jfdh,");
				str.append("jf_date,");
				str.append("fyzmzdw,");
				str.append("fyyjdd_date,");
				str.append("cysdm,");
				str.append("jszdm,");
				str.append("xzddm,");
				str.append("fhrdm,");
				str.append("jzxh,");
				str.append("sftbfhtz,");
				str.append("sfgzxlh,");
				str.append("cj_date,");
				str.append("clzt)");
				str.append("values");
				str.append("('" + strNull(jfdh) + "',");
				str.append("'" + strNull(DateTimeUtil.SubString(jf_date)) + "',");
				str.append("'" + strNull(fyzmzdw) + "',");
				str.append("'" + strNull(DateTimeUtil.SubString(fyyjdd_date)) + "',");
				str.append("'" + strNull(cysdm) + "',");
				str.append("'" + strNull(jszdm) + "',");
				str.append("'" + strNull(xzddm) + "',");
				str.append("'" + strNull(fhrdm) + "',");
				str.append("'" + strNull(jzxh) + "',");
				str.append("'" + strNull(sftbfhtz) + "',");
				str.append("'" + strNull(sfgzxlh) + "',");
				str.append("to_date('" + DateTimeUtil.getAllCurrTime()+ "','yyyy-MM-dd hh24:mi:ss'),");
				str.append("0)");
			    prep=conn.prepareStatement(str.toString());
				prep.execute();
				prep.close();
				ztxx_num++;//记录插入行数
			}
			// 插入数据到EFI发货通知-发运明细表
			if(flag==1||flag==2){
				StringBuffer str1 = new StringBuffer();
				str1.append("insert into "+SpaceFinal.spacename_ck+".in_efi_fhtz_fymx");
				str1.append("(xuh,");
				str1.append("jfdh,");
				str1.append("fysl,");
				str1.append("jldw,");
				str1.append("bzlx,");
				str1.append("ljh,");
				str1.append("ddh,");
				str1.append("lygj)values(");
				str1.append("'" + strNull(xuh) + "',");
				str1.append("'" + strNull(jfdh) + "',");
				str1.append("'"+ d_fysl + "',");
				str1.append("'" + strNull(jldw) + "',");
				str1.append("'" + strNull(bzlx) + "',");
				str1.append("'" + strNull(ljh) + "',");
				str1.append("'" + strNull(ddh) + "',");
				str1.append("'" + strNull(lygj) + "')");
				prep=conn.prepareStatement(str1.toString());
				prep.execute();
				prep.close();
				fymx_num++;//记录插入行数
			}
			
			// //插入数据到插入EFI发货通知-包装对应信息表
			if(flag==1||flag==2||flag==3){
				StringBuffer str2 = new StringBuffer();
				str2.append("insert into "+SpaceFinal.spacename_ck+".in_efi_fhtz_bzdyxx");
				str2.append("(xuh,");
				str2.append("jfdh,");
				str2.append("ua,");
				str2.append("ualx,");
				str2.append("uc,");
				str2.append("uclx,");
				str2.append("ucljsl,");
				str2.append("ualjsl,");
				str2.append("yhl,");
				str2.append("pzh,");
				str2.append("gys,");
				str2.append("ggph,");
				str2.append("changd,");
				str2.append("kuand,");
				str2.append("gaod)");
				str2.append("values(");
				str2.append("'" + strNull(xuh) + "',");
				str2.append("'" + strNull(jfdh) + "',");
				str2.append("'" + strNull(ua) + "',");
				str2.append("'" + strNull(ualx) + "',");
				str2.append("'" + strNull(uc) + "',");
				str2.append("'" + strNull(uclx) + "',");
				str2.append("'" + strNull(d_ucljsl) + "',");
				str2.append("'" + strNull(d_ualjsl) + "',");
				str2.append("'" + strNull(yhl) + "',");
				str2.append("'" + strNull(pzh) + "',");
				str2.append("'" + strNull(gys) + "',");
				str2.append("'" + strNull(ggph) + "',");
				str2.append("'" + strNull(chang) + "',");
				str2.append("'" + strNull(kuan) + "',");
				str2.append("'" + strNull(gao) + "')");
				prep=conn.prepareStatement(str2.toString());
				prep.execute();
				prep.close();
	            bzxx_num++;//记录插入行数
			}
			
			// //更新数据到插入EFI发货通知-包装对应信息表
			if(flag==4){
				StringBuffer str4 = new StringBuffer();
				str4.append("update "+SpaceFinal.spacename_ck+".in_efi_fhtz_bzdyxx ");
				str4.append(" set ualx='"+ strNull(ualx) + "',");
				str4.append(" uc='"+ strNull(uc) + "',");
				str4.append(" uclx='"+ strNull(uclx) + "',");
				str4.append(" ucljsl='"+ strNull(d_ucljsl) + "',");
				str4.append(" ualjsl='"+ strNull(d_ualjsl) + "',");
				str4.append(" yhl='"+ strNull(yhl) + "',");
				str4.append(" pzh='"+ strNull(pzh) + "',");
				str4.append(" gys='"+ strNull(gys) + "' ");
				str4.append(" where xuh = '" + strNull(xuh) + "' ");
				str4.append(" and jfdh = '" + strNull(jfdh) + "' ");
				str4.append(" and ua = '" + strNull(ua) + "'");
				prep=conn.prepareStatement(str4.toString());
				prep.execute();
				prep.close();
				ubzxx_num++;//更新的行数啊
			}
			// 是否同步发货通知为1时，则不进同步零件信息表
			// 插入数据到EFI发货通知-同步零件信息表
            if(INSERT_FLAG_TBLJ.equals(sftbfhtz)){
				StringBuffer str3 = new StringBuffer();
				str3.append("insert into "+SpaceFinal.spacename_ck+".in_efi_fhtz_tbljxx(");
				str3.append("ua,");
				str3.append("uc,");
				str3.append("xlh,");
				str3.append("gys,");
				str3.append("lsh,");
				str3.append("shul,");
				str3.append("wzly");
				str3.append(")values(");
				str3.append("'" + strNull(ua) + "',");
				str3.append("'" + strNull(uc) + "',");
				str3.append("'" + strNull(xlh) + "',");
				str3.append("'" + strNull(gys) + "',");
				str3.append("'" + strNull(lsh) + "',");
				str3.append("" + strNull(shul) + ",");
				str3.append("'" + strNull(lygj) + "')");
				prep=conn.prepareStatement(str3.toString());
				prep.execute(); 
				prep.close();
				tbxx_num++;//记录插入行数
            }
		} catch (SQLException e) {
			ERROR_COUNT++;//记录异常数据行数
			throw new SQLException (e.getMessage()+jfdh+ua+xuh);
		}finally {
			try {
				if(null!=prep){
					prep.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}		
	}

	/**
	 * 空串处理
	 * @param obj对象
	 * @return 处理后字符串
	 * @author WL
	 * @date 2011-10-26
	 */
	private String strNull(Object obj) {// 对象为空返回空串,不为空toString
		return obj == null ? "" : obj.toString();
	}
}
