package com.athena.ckx.util.xls;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger; 

import com.toft.core3.ibatis.support.AbstractIBatisDao;

import jxl.Sheet;
import jxl.Workbook;

/**
 * 基础sheet处理器
 * 
 * @author chenlei
 * @vesion 1.0
 * @date 2012-5-17
 */
public class BaseSheetHandlergyxhd extends SheetHandler {
	protected static Logger logger = Logger.getLogger(BaseSheetHandlergyxhd.class);	//定义日志方法
	private static Properties prop;
	private String editor;
	private AbstractIBatisDao baseDao;
	public BaseSheetHandlergyxhd(Workbook workbook, String sheet, String table,
			String datasourceId, String clazz, String keys,String dateColumns,String dateFormats) {
		super(workbook, sheet, table, datasourceId, clazz, keys,dateColumns,dateFormats);
	}
	//0007177  将修改人编辑为当前登录人  编辑时间写入数据库
	@Override
	public String doSheetHandler(String editor,Map<String,String> map,AbstractIBatisDao baseDao) {
		this.editor = editor;
		// 对sheet处理 保存入库
		Sheet she = workbook.getSheet(sheet);
		int rowNum = she.getRows();
		int coluNum = she.getColumns();
		ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
		ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
		String insertSql = null; // 新增sql语句
		String updateSql = null; // 修改sql语句
		String selectSql = null; // 查找sql语句
		
		StringBuffer errorBuffer = new StringBuffer(); //错误信息
		
		
		for (int i = 0; i < rowNum; i++) {
			try {
				for (int j = 0; j < coluNum; j++) {
					if (i > 1) {
						// 取第三行 开始值 保存入库
						fieldValue.add((she.getCell(j, i)).getContents()); // 或者从第三行的每列值
					} else if(i==0) {
						// 一次 获得 字段名
						fieldList.add((she.getCell(j, i)).getContents()); // 得到第一行的每列值
					}
				}

				if (i == 0) {
					// 生成新增sql
					insertSql = makeInsertSql(fieldList);
					updateSql = makeUpdateSql(fieldList,keys);
					selectSql = makeSelectSql(fieldList, keys);
				} else if (i > 1) {
					// 从第二行保存入库
					addOrUpdateToTable(fieldValue, fieldList, insertSql,
							updateSql, selectSql);
				}
			} catch (SQLException e) {
				//不处理 记录一日志
				errorBuffer.append(e.getMessage()); //记录错误信息
			}finally{
				fieldValue.clear();
				fieldValue.trimToSize();
				logger.info(i);
			}
		}
		
		//关闭连接
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return errorBuffer.toString();
	}

	/**
	 * 创建查找sql
	 * 
	 * @param fieldList
	 * @param keys
	 * @return
	 */
	private String makeSelectSql(ArrayList<String> fieldList, String keys) {
		// select count(1) from sys_bus_log l where
		// l.id='8a9a2b26354157d601354157d64f0000'
		String[] keyStrs = keys.split(",");
		StringBuffer sb = new StringBuffer();

		sb.append("select count(1) from ");
		sb.append(replacetable(table));
		sb.append(" where ");

		for (int j = 0; j < keyStrs.length; j++) {
			if (j == keyStrs.length - 1) {
				// 不加 逗号
				sb.append(keyStrs[j] + "=?");
			} else {
				sb.append(keyStrs[j] + "=? and ");
			}
		}

		return sb.toString();
	}

	/**
	 * 创建修改sql
	 * 
	 * @param fieldList
	 * @param keys
	 *            主键
	 * @return
	 */
	private String makeUpdateSql(ArrayList<String> fieldList,String keys) {
		String[] keyStrs = keys.split(",");

		// update sys_bus_log set OPERATOR='2',OPERATOR_IP='22' where id='333'
		StringBuffer sb = new StringBuffer();
		sb.append("update ");
		sb.append(replacetable(table));
		sb.append(" set ");

		for (int i = 0; i < fieldList.size(); i++) {
			if (i == fieldList.size() - 1) {
				// 不加 逗号
				sb.append(fieldList.get(i) + "=" + createPlaceholder(fieldList.get(i)));
			} else {
				sb.append(fieldList.get(i) + "=" + createPlaceholder(fieldList.get(i))+",");
			}
		}
		sb.append("editor='" + editor+"',");
		sb.append("edit_time = sysdate ");
		sb.append(" where ");

		for (int j = 0; j < keyStrs.length; j++) {
			if (j == keyStrs.length - 1) {
				// 不加 逗号
				sb.append(keyStrs[j] + "=?");
			} else {
				sb.append(keyStrs[j] + "=? and ");
			}
		}

		return sb.toString();
	}

	/**
	 * 将fieldValue保存入库 如果库有此条记录，则执行更新操作； 没有，则执行新增操作
	 * 
	 * @param fieldValue
	 * @param fieldList
	 * @param insertSql
	 * @param updateSql
	 * @param selectSql
	 * @throws SQLException
	 */
	private void addOrUpdateToTable(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String insertSql, String updateSql,
			String selectSql) throws SQLException {

		if (getFiledByKey(fieldValue, fieldList, selectSql) < 1) {
			// 数据库中 无 ;则执行新增操作
			insertValue(fieldValue, fieldList, insertSql);
		} else {
			// 数据库有 则执行 修改
			updateValue(fieldValue, fieldList, updateSql);
		}

	}

	/**
	 * 修改操作
	 * 
	 * @param fieldValue
	 * @param fieldList
	 * @param updateSql
	 * @throws SQLException
	 */
	private void updateValue(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String updateSql) throws SQLException {
		String[] keyStrs = keys.split(",");
		
		//将keyValues的值保存到fieldValue中
		for (int i = 0; i < keyStrs.length; i++) {
			//keyValues[i] = fieldValue.get(fieldList.indexOf(keyStrs[i]));
			fieldValue.add(fieldValue.get(fieldList.indexOf(keyStrs[i])));
		}
	
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(updateSql);

			// 将占位符替换
			for (int i = 0; i < fieldValue.size(); i++) {
				st.setObject(i+1, fieldValue.get(i));
			}
			st.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			//错误信息
			StringBuffer esb = new StringBuffer();
			for (int i = 0; i < keyStrs.length; i++) {
				esb.append(keyStrs[i]+"=");
				esb.append(fieldValue.get(fieldList.indexOf(keyStrs[i]))+ "   ");
			}
			esb.append(" : 错误信息   ");
			esb.append(e.getMessage());
	
			throw new SQLException(esb.toString());
		}finally{
			if(st!=null){
				st.close();
			}			
		}

	}

	/**
	 * 新增操作
	 * 
	 * @param fieldValue
	 * @param fieldList
	 * @param insertSql
	 * @throws SQLException
	 */
	private void insertValue(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String insertSql) throws SQLException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(insertSql);

			for (int i = 0; i < fieldValue.size(); i++) {
				st.setObject(i+1, fieldValue.get(i));
			}
			st.execute();
			conn.commit();
		} catch (SQLException e) {
			//错误信息
			String[] keyStrs = keys.split(",");
			StringBuffer esb = new StringBuffer();
			for (int i = 0; i < keyStrs.length; i++) {
				esb.append(keyStrs[i]+"=");
				esb.append(fieldValue.get(fieldList.indexOf(keyStrs[i]))+ "   ");
			}
			esb.append(" : 错误信息   ");
			esb.append(e.getMessage());
	
			throw new SQLException(esb.toString());
			
		}finally{
			if(st!=null){
				st.close();
			}
		}

	}

	/**
	 * 根据key做查找
	 * 
	 * @param fieldValue
	 * @param selectSql
	 * @return
	 * @throws SQLException
	 */
	private int getFiledByKey(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String selectSql) throws SQLException {
		int result = 0;
		String[] keyStrs = keys.split(",");
		String[] keyValues = new String[keyStrs.length]; // key值

		for (int i = 0; i < keyStrs.length; i++) {
			keyValues[i] = fieldValue.get(fieldList.indexOf(keyStrs[i]));
		}
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(selectSql);

			// 将占位符替换
			for (int i = 0; i < keyValues.length; i++) {
				st.setObject(i+1, keyValues[i]);
			}

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
				return result;
			}
		
		} catch (SQLException e) {
			throw e;
		}finally{
			if(st!=null){
				st.close();
			}
		}

		return result;
	}

	/**
	 * 生成保存sql
	 * 
	 * @param fieldList
	 *            要保存的sql字段名称
	 * @return
	 */
	private String makeInsertSql(ArrayList<String> fieldList) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb_value = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append(replacetable(table));
		sb.append(" ( ");

		for (int i = 0; i < fieldList.size(); i++) {
			if (i == fieldList.size() - 1) {
				// 不加 逗号
				sb.append(fieldList.get(i));
				sb_value.append(createPlaceholder(fieldList.get(i)));
			} else {
				sb.append(fieldList.get(i) + ",");
				sb_value.append(createPlaceholder(fieldList.get(i))+",");
			}
		}

		sb.append(") values (");
		sb.append(sb_value.toString() + ")");

		return sb.toString();
	}
	
	/**
	 * 生成占位符 
	 * 	时间类型的 生成to_date() ?
	 * @return
	 */
	private String createPlaceholder(String field){
		String result = "?";
		
		if(dateColumns!=null || !"null".equals(dateColumns)){
			//看此字段是否为时间类型
			if(dateColumns.contains(field)){
				//包含此字段，此字段为时间类型
				String[] dateColumn = dateColumns.split(",");
				String[] dateType = dateFormats.split(",");
				for(int i=0;i<dateColumn.length;i++){
					if(field.equals(dateColumn[i])){
						result = "to_date(?,'"+dateType[i]+"')";
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 替换表空间名 ${dbSchemal3}
	 * 
	 * @param sql
	 * @return
	 */
	public String replacetable(String sql) {

		String result = sql;

		if (sql.contains("{")) {
			String regex = "\\{\\w+\\}";
			List<String> li = new ArrayList<String>();
			final Pattern pa = Pattern.compile(regex);
			final Matcher ma = pa.matcher(sql);
			while (ma.find()) {
				li.add(ma.group());
			}

			// 将占位符替换成真命名空间名
			for (String key : li) {
				String realValue = getValueByKey(key);
				sql = sql.replace(key, realValue);
			}

			result = sql;
		}
		return result;

	}

	/**
	 * 对Key 最处理后 再依据处理后的key到 properties文件中拿值 key:{xxxx} 处理后 xxx
	 * 
	 * @param key
	 * @return
	 */
	private String getValueByKey(String key) {
		String result = null;
		String realKey = key.substring(1, key.length() - 1);
		result = prop.getProperty(realKey);
		return result;
	}

	// 加载sqlmap.properties文件
	static {
		try {
			logger.info("加载了......");
			InputStream in = BaseSheetHandlergyxhd.class.getClassLoader()
					.getResourceAsStream(
							"com/athena/component/config/sqlmap.properties");
			prop = new Properties();
			prop.load(in);

		} catch (Exception e) {
			logger.error("加载sqlmap.properties文件出错...");
			throw new RuntimeException(e);
		}
	}

}
