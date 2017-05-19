package com.athena.component.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.DateTimeUtil;
import com.athena.component.exchange.DataExchange;
import com.athena.component.exchange.FileLog;
import com.athena.component.exchange.Record;
import com.athena.component.exchange.SpaceFinal;
import com.athena.component.exchange.config.DataParserConfig;
import com.athena.component.exchange.db.DbDataWriter;
import com.athena.component.exchange.txt.TxtDataReader;
import com.toft.core2.dao.database.DbUtils;

public class CODDCDataRead extends TxtDataReader {

	protected static Logger logger = Logger.getLogger(CODDCDataRead.class); // 定义日志方法
	public String datasourceId = null;
	private DataParserConfig dataParserConfig = null;// 初始化配置文件信息

	private String usercenter = null;
	private String CODDC = null;
	private String lingj = null;
	private String xiaohd = null;
	private String sydw = null;
	private String EMON = null;
	private String total = null;
	private double d_xhdxs = 0;
	private int i_num = 0;// 插入记录条数;
	private int u_num = 0;// 更新记录条数;
	private int e_num = 0;// 错误记录条数;
	private Connection conn = null;
	private List list = new ArrayList();
	private PreparedStatement prep = null;
	Record record = new Record();

	public CODDCDataRead(DataParserConfig dataParserConfig) {
		super(dataParserConfig);
		// 获取数据库连接
		datasourceId = dataParserConfig.getWriterConfig().getDatasourceId();
		conn = DbUtils.getConnection(datasourceId);
	}

	@Override
	public int readLine() {
		FileInputStream fs = null;
		InputStreamReader is = null;
		BufferedReader br = null;
		File f = null;
		int n = 0;// 记录行数标记
		String f_name = null;// 文件名称
		String SID = DbDataWriter.getUUID();// 唯一标示
		String EID = DbDataWriter.getUUID();// 唯一标示
		String ErrorMessage = null;// 错误信息
		String FileBeginTime = DataExchange.RunStartTime;// 接口文件开始运行时间
		String FileEndTime = null;// 文件运行结束时间

		try {
			// 获取配置
			dataParserConfig = getDataParserConfig();
			f = new File(dataParserConfig.getReaderConfig().getFilePath());
			String filename = f.getName();
			String[] fname = filename.split(",");
			for (int i = 0; i < fname.length; i++) {
				f = new File("/users/ath00/tmp/" + fname[i] + ".txt");
				f_name = f.getName();
				if (f.exists()) {
					fs = new FileInputStream(f.getPath());
					is = new InputStreamReader(fs, dataParserConfig
							.getReaderConfig().getEncoding());
					br = new BufferedReader(is);
					String line = "";

					conn.setAutoCommit(false);// 事务开启
					while (null != (line = br.readLine())) { // 解析文本
						if (!"".equals(line)) {
							n++;
							if (n == 1) {
								continue;
							}
							total = line.substring(0, 53).trim();
							if (total.indexOf("PDS—ATHENA") != -1) {
								continue;
							}
							if(line.indexOf("END")!=-1){
								break;
							}
							CODDC = strNull(line.substring(0, 7).trim());
							lingj = strNull(line.substring(7, 18).trim()); // 零件号
							xiaohd = strNull(line.substring(18, 27).trim()); // 消耗点
							sydw = strNull(line.substring(27, 37).trim()); // 单位
							String xhdxs = "".equals(strNull(line.substring(37, 53).trim()))?"0":line.substring(37, 53).trim(); // 消耗点系数
							//零件号,消耗点,消耗点系数其一为中则不处理
							if("".equals(lingj)||"".equals(xiaohd)||"0".equals(xhdxs)){
								continue;
							}							
							d_xhdxs = Double.parseDouble(xhdxs);
							EMON = line.substring(53).trim(); // EMON商业化时间
							usercenter = "U" + xiaohd.substring(0, 1); // 用户中心
							String[] emon = EMON.split(",");
							for (int y = 0; y < emon.length; y++) {
								// list.add(compDate);
								String stardateStr = "";
								String enddateStr = "";
								String[] dataStr = null;
								if (emon[y].indexOf("-") != -1) {
									// 如果时间为20120501-20120502
									dataStr = emon[y].split("-");
									stardateStr = DateTimeUtil.DateStr(dataStr[0]);
									enddateStr = DateTimeUtil.DateStr(dataStr[1]);
									// 是否存相同的数据
									// compDate = usercenter+CODDC+lingj+xiaohd+
									// stardateStr;
									AddTable(usercenter, CODDC, lingj, xiaohd,
											sydw, d_xhdxs, stardateStr,
											enddateStr);
								} else {
									String em = DateTimeUtil.DateStr(emon[y].toString());
									AddTable(usercenter, CODDC, lingj, xiaohd,
											sydw, d_xhdxs, em, em);
								}
								
							}
							if(n%5000 == 0){
								conn.commit();
								logger.info("已解析---------------5000条数据！"+ new Date());
								//System.out.println("已解析---------------5000条数据！"+ new Date());
							}
						}
					}
					conn.commit();

				}
			}
			FileEndTime = DateTimeUtil.getAllCurrTime();// 文件运行结束时间
			FileLog.getInstance(datasourceId).File_info(SID, "3030", f_name,
					FileBeginTime, FileEndTime, i_num, u_num, e_num, "1");// 记录文件信息日志

		} catch (Exception e) {
			logger.error(e.getMessage());
			ErrorMessage = e.getMessage();
			FileEndTime = DateTimeUtil.getAllCurrTime();// 文件运行结束时间
			FileLog.getInstance(datasourceId).File_info(SID, "3030", f_name,
					FileBeginTime, FileEndTime, i_num, 0, e_num, "-1");// 记录文件信息日志
			FileLog.getInstance(datasourceId).File_ErrorInfo(EID, "3030", SID,
					ErrorMessage, "");// 记录错误信息日志
//			try {
//				conn.rollback();
//			} catch (SQLException e1) {
//				logger.error(e1.toString());
//			}
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (null != fs) {
					fs.close();
				}
				if (null != is) {
					is.close();
				}
				if (null != br) {
					br.close();
				}
				if (null != prep) {
					prep.close();
				}
				if (null != conn) {
					conn.close();
				}
				DbUtils.freeConnection(conn);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return 0;
	}

	/**
	 * 查找主键为条件的数据是否已经存在
	 * 
	 * @param dataStr
	 * @return
	 */
	public Map QueryTable(String dataStr, String dataEndStr) {
		Map map = null;
		StringBuffer strbuf = new StringBuffer();
		try {
			strbuf.append("select * from " + SpaceFinal.spacename_ddbh
					+ ".ddbh_CODDCxhdlj where ");
			strbuf.append("USERCENTER='" + strNull(usercenter) + "' ");
			strbuf.append("and CODDC='" + strNull(CODDC) + "' ");
			strbuf.append("and LINGJ='" + strNull(lingj) + "' ");
			strbuf.append("and XIAOHD='" + strNull(xiaohd) + "' ");
			strbuf.append("and ECOMQSSJ=to_date('" + strNull(dataStr)
					+ "','yyyy-MM-dd')");
			strbuf.append("and ECOMJSSJ=to_date('" + strNull(dataEndStr)
					+ "','yyyy-MM-dd')");
			map = DbUtils.selectOne(strbuf.toString(), datasourceId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return map;
	}

	/**
	 * 新增CODDC-消耗点零件表数据
	 * 
	 * @param dataStr
	 * @param xhdxs
	 * @throws Exception
	 * @throws SQLException
	 */

	public void AddTable(String usercenter, String CODDC, String lingj,
			String xiaohd, String sydw, double d_xhdxs, String dataStartStr,
			String dataEndStr) throws SQLException {
			Map map = null;
			try{
				map = GetChanx(usercenter, xiaohd); // 得到生产线号
				StringBuffer sqlbuf = new StringBuffer();
				sqlbuf.append("insert into "
						+ SpaceFinal.spacename_ddbh
						+ ".in_coddc (USERCENTER,CODDC,LINGJ,XIAOHD,DANW,XIAOHXS,SHENGCX,ECOMQSSJ,ECOMJSSJ,CHULZT,CREATOR,CREATE_TIME) values(");
				sqlbuf.append("'" + strNull(usercenter) + "',");
				sqlbuf.append("'" + strNull(CODDC) + "',");
				sqlbuf.append("'" + strNull(lingj) + "',");
				sqlbuf.append("'" + strNull(xiaohd) + "',");
				sqlbuf.append("'" + strNull(sydw) + "',");
				sqlbuf.append("" + d_xhdxs + ",");
				sqlbuf.append("'" + strNull(map.get("shengcxbh")) + "',");
				sqlbuf.append("to_date('" + strNull(dataStartStr)
						+ "','yyyy-MM-dd'),");
				sqlbuf.append("to_date('" + strNull(dataEndStr)
						+ "','yyyy-MM-dd'),"); // add by pan.rui
				sqlbuf.append("'0',");
				sqlbuf.append("'interspace',");
				sqlbuf.append("sysdate)");
				prep = conn.prepareStatement(sqlbuf.toString());
				prep.execute();
				i_num++;// 插入记录条数
			}catch(SQLException ex){
				e_num++;// 错误记录条数;
				throw new SQLException(ex.getMessage());
			}finally{
				prep.close();
			}
	}
	

	/**
	 * 在参考系分配区表里取出生产线号
	 * 
	 * @return
	 */
	public Map GetChanx(String usercenter, String xiaohd) {
		String chanx = "";
		Map map = null;
		try {
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("select shengcxbh from " + SpaceFinal.spacename_ddbh
					+ ".ckx_fenpq ");
			strbuf.append("where usercenter = '" + strNull(usercenter)
					+ "' and fenpqh = substr('" + strNull(xiaohd) + "', 1, 5)");
			map = DbUtils.selectOne(strbuf.toString(), datasourceId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return map;
	}

	/**
	 * 空串处理
	 * 
	 * @param obj
	 *            对象
	 * @return 处理后字符串
	 * @author GJ
	 * @date 2011-10-26
	 */
	private String strNull(Object obj) {// 对象为空返回空串,不为空toString
		return obj == null ? "" : obj.toString();
	}
}
