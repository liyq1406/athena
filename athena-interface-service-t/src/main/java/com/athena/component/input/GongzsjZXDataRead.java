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

public class GongzsjZXDataRead extends TxtDataReader {

	protected static Logger logger = Logger.getLogger(GongzsjZXDataRead.class); // 定义日志方法
	public String datasourceId = null;
	private DataParserConfig dataParserConfig = null;// 初始化配置文件信息

	private int i_num = 0;// 插入记录条数;
	private int u_num = 0;// 更新记录条数;
	private int e_num = 0;// 错误记录条数;
	private Connection conn = null;  //数据连接
	private PreparedStatement prep = null; //数据预处理
	Record record = new Record();

	public GongzsjZXDataRead(DataParserConfig dataParserConfig) {
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
					is = new InputStreamReader(fs, dataParserConfig.getReaderConfig().getEncoding());
					br = new BufferedReader(is);
					String line = "";

					conn.setAutoCommit(false);// 事务开启
					prep = conn.prepareStatement(AddTableStr());
					while (null != (line = br.readLine())) { // 解析文本
						if (!"".equals(line)) {
							n++;
						    String usercenter = strNull(line.toString().substring(0, 3).trim());
						    String chanx = strNull(line.toString().substring(3, 9).trim());
						    String gongzr = strNull(line.toString().substring(9, 19).trim());
						    String xiaohsj = strNull(line.toString().substring(19, 23).trim());
							String juedsk = strNull(line.toString().substring(23, 42).trim());
							prep.setString(1,usercenter);
							prep.setString(2,chanx);
							prep.setString(3,gongzr);
							prep.setString(4,xiaohsj);
							prep.setString(5,juedsk);
							prep.addBatch();
							if(n%5000 == 0){
								prep.executeBatch();
								conn.commit();
								prep.clearBatch();
								logger.info("已解析---------------5000条数据！"+ new Date());
								//System.out.println("已解析---------------5000条数据！"+ new Date());
							}
						}
					}
					prep.executeBatch();
					conn.commit();
					prep.clearBatch();
				}
			}
			i_num = n;
			FileEndTime = DateTimeUtil.getAllCurrTime();// 文件运行结束时间
			FileLog.getInstance(datasourceId).File_info(SID, "2780", f_name,
					FileBeginTime, FileEndTime, i_num, u_num, e_num, "1");// 记录文件信息日志

		} catch (Exception e) {
			logger.error(e.getMessage());
			ErrorMessage = e.getMessage();
			FileEndTime = DateTimeUtil.getAllCurrTime();// 文件运行结束时间
			FileLog.getInstance(datasourceId).File_info(SID, "2780", f_name,
					FileBeginTime, FileEndTime, i_num, 0, e_num, "-1");// 记录文件信息日志
			FileLog.getInstance(datasourceId).File_ErrorInfo(EID, "2780", SID,
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
	 * 新增表CKX_GONGZSJMB
	 * @param usercenter
	 * @throws SQLException
	 */
	private String AddTableStr() {
		
			StringBuffer sqlbuf=new StringBuffer();
			sqlbuf.append("insert into "+SpaceFinal.spacename_ck+".CKX_GONGZSJMB (USERCENTER,CHANX,GONGZR,XIAOHSJ,JUEDSK) values(");
			sqlbuf.append("?,?,").append("to_date(?,'yyyy-MM-dd'),").append("?,").append("to_date(?,'yyyy-MM-dd HH24:mi:ss'))");
//			sqlbuf.append("'"+strNull(usercenter)+"',");
//			sqlbuf.append("'"+strNull(chanx)+"',");
//			sqlbuf.append("to_date('"+strNull(gongzr)+"','yyyy-MM-dd'),");
//			sqlbuf.append("'"+strNull(xiaohsj)+"',");
//			sqlbuf.append("to_date('"+strNull(juedsk)+"','yyyy-MM-dd HH24:mi:ss'))"); //add by pan.rui
			logger.info(sqlbuf.toString());
			return sqlbuf.toString();
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
