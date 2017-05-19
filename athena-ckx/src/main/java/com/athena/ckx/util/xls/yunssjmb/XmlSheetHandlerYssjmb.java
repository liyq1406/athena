package com.athena.ckx.util.xls.yunssjmb;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.athena.ckx.entity.transTime.CkxYunssjMb;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.xls.BaseSheetHandler;
import com.athena.ckx.util.xls.SheetHandler;
import com.athena.db.ConstantDbCode;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
/**
 * 按照XML文件处理
 * @author chenlei
 * @vesion 1.0
 * @date 2012-7-26
 */
public class XmlSheetHandlerYssjmb extends SheetHandler {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> cmap;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerYssjmb.class);	//定义日志方法
	public XmlSheetHandlerYssjmb(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(String editor,Map<String,String> cmap,AbstractIBatisDao baseDao) {
		try{
			
		this.editor = editor;
		this.cmap=cmap;
		this.baseDao = baseDao;
		
		String xiehztbzs = cmap.get("xiehztbzs");
		if("".equals(xiehztbzs)){
			String  message = "无物流工艺员权限，无法导入数据！";
			return message;
      	}else if("('')".equals(xiehztbzs)){
			String  message = "物流工艺员组无对应的卸货站台编组权限";
			return message;
      	}
		String result = "";
		// 对sheet处理 保存入库
		Element sheetElment = getSheetElment(sheet);
		int rowNum = getRowNumber(sheetElment);
		//导入的数据超过5000行 提示不让插入
		if(rowNum>5002){
			String  message = "导入数据超过5000行,请调整数据行数";
			return message;
			//throw new ServiceException("导入数据超过5000行,请调整数据行数");
		}
		int coluNum = getCellNumber(sheetElment);
		ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
		ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
		ArrayList<String> fieldErrorValue = new ArrayList<String>(); // 一列的值 为错误使用
		//String insertSql = null; // 新增sql语句
		//String updateSql = null; // 修改sql语句
		//String selectSql = null; // 查找sql语句
		List<CkxYunssjMb> datas = new ArrayList<CkxYunssjMb>();
		StringBuffer errorBuffer = new StringBuffer(""); //错误信息
		StringBuffer wronginfo=new StringBuffer("");
		int num = 0;
		int index = 3;
		List<Element> list  = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
		for (int i = 2; i<list.size(); i++) {
			CkxYunssjMb bean = new CkxYunssjMb();
//			Element rowElement = (Element) sheetElment.selectNodes("xmlns:Table/xmlns:Row").get(i+2);
			Element rowElement =  (Element) list.get(i);
			fieldValue=getCellValue(rowElement,fieldList.size());
			for (int j = 0; j < coluNum; j++) {
				if(fieldValue.get(j)==null){
					continue;
				}
				if( j == 0)
				{
					bean.setUsercenter(fieldValue.get(j).trim());
				}
				else if( j == 1)
				{
					bean.setXiehztbh(fieldValue.get(j).trim());
				}
				else if( j == 2)
				{
					bean.setGcbh(fieldValue.get(j).trim());
					
				}
				else if( j == 3)
				{
					String va = fieldValue.get(j);
					if(va != null && va.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$") && va.indexOf(".")==-1){
						bean.setDaohsj(Double.valueOf(fieldValue.get(j)));
					}
				}
				else if( j == 4)
				{
					String va = fieldValue.get(j);
					if(va != null && va.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$") && va.indexOf(".")==-1){
						bean.setFacsj(Double.valueOf(fieldValue.get(j)));
					}
				}
				else if( j == 5)
				{
					String va = fieldValue.get(j);
					if(StringUtils.isNumeric(String.valueOf(va))){
						bean.setXuh(Double.valueOf(fieldValue.get(j)));
					}
				}
			}
			datas.add(bean);
			logger.info("读取excel数据，条数："+i);
		}
		logger.info("数据读取完成，总条数："+datas.size());
		//校验有误的条数
		int wrongcoun=0;
		
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<CkxYunssjMb> wrongdatas =  new ArrayList<CkxYunssjMb>();
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<CkxYunssjMb> datasnew =  new ArrayList<CkxYunssjMb>();
		Map<String,String> chengysMap = new HashMap<String,String>();
		
		List<Map<String,String>> chengysList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.queryChengysListUpload",cmap);
		for (int i = 0; i < chengysList.size(); i++) {
			Map<String,String> dMap = chengysList.get(i);
			String key = dMap.get("USERCENTER") + dMap.get("GCBH");
			chengysMap.put(key, key);
		}

		List<Map<String,String>> cangkuList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.querycangkuListUpload",cmap);
		for (int i = 0; i < cangkuList.size(); i++) {
			Map<String,String> dMap = cangkuList.get(i);
			String key = dMap.get("USERCENTER") + dMap.get("CANGKBH");
			chengysMap.put(key, key);
		}
		
		Map<String,String> xiehztbzhMap = new HashMap<String,String>();
		
		List<Map<String,String>> xiehztbzhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.queryXiehztbzhListUpload",cmap);
		for (int i = 0; i < xiehztbzhList.size(); i++) {
			Map<String,String> dMap = xiehztbzhList.get(i);
			String key = dMap.get("USERCENTER") + dMap.get("XIEHZTBZH");
			xiehztbzhMap.put(key, key);
		}
		
		Map<String,String> only = new HashMap<String,String>();
		logger.info("缓存数据读取完成");
		int errnum = 0;
		//校验每个字段
		for (int i=0;i<datas.size();i++) {
			String pianyisj="";
			CkxYunssjMb bean = datas.get(i);
			if(bean.getUsercenter() == null || " ".equals(bean.getUsercenter()) || bean.getUsercenter().length()!=2){
				result = resultMessage(index,"用户中心："+bean.getUsercenter()+" 必填并且长度必须为2位");
				wronginfo=wronginfo.append(result);
			}
			//判断卸货站台编组号
			if(bean.getXiehztbh()==null || " ".equals(bean.getXiehztbh()) || bean.getXiehztbh().length()!=4){
				result = resultMessage(index,"卸货站台编组："+bean.getXiehztbh()+"  必填并且长度必须为4位");
				wronginfo=wronginfo.append(result);
				//throw new SQLException("工艺车身数量："+fieldValue.get(i+1)+" 必填并且为正整数");
			}else if(xiehztbzhMap.get(bean.getUsercenter()+bean.getXiehztbh()) == null){
				result = resultMessage(index,"用户中心："+bean.getUsercenter()+" 卸货站台编组："+bean.getXiehztbh()+"  不是有效的数据");
				wronginfo=wronginfo.append(result);
			}
			
			//判断承运商，仓库编号
			if(bean.getGcbh() == null || " ".equals(bean.getGcbh()) || !(bean.getGcbh().length()==10 || bean.getGcbh().length()==3)){
				result = resultMessage(index,"承运商："+bean.getGcbh()+"  必填,并且长度必须为10位或者长度必须为3位仓库编号");
				wronginfo=wronginfo.append(result);
				//throw new SQLException("工艺车身数量："+fieldValue.get(i+1)+" 必填并且为正整数");
			}else if(chengysMap.get(bean.getUsercenter()+bean.getGcbh()) == null){
				result = resultMessage(index,"用户中心："+bean.getUsercenter()+" 承运商："+bean.getGcbh()+"  不是有效的数据");
				wronginfo=wronginfo.append(result);
			}

//			if(bean.getDaohsj()== null || !String.valueOf(bean.getDaohsj()).matches("^-?[1-9][0-9]{0,4}$|0")){
			if(bean.getDaohsj()== null || "".equals(bean.getDaohsj())|| bean.getDaohsj()>9999 || bean.getDaohsj()<-9999){
				result = resultMessage(index,"到货时间偏量必须为-9999~9999的自然数");
				wronginfo=wronginfo.append(result);
			}	

//			if(bean.getFacsj()== null || !String.valueOf(bean.getFacsj()).matches("^-?[1-9][0-9]{0,4}$|0")){
			if(bean.getFacsj()== null || "".equals(bean.getFacsj()) || bean.getFacsj()>9999 || bean.getFacsj()<-9999){
				result = resultMessage(index,"发出时间偏量必须为-9999~9999的自然数");
				wronginfo=wronginfo.append(result);
			}else if(bean.getFacsj()!= null && bean.getDaohsj()!= null && bean.getFacsj()>=bean.getDaohsj()){
				result = resultMessage(index,"到货时间偏量必须大于发货时间偏量");
				wronginfo=wronginfo.append(result);
			}
			
//			if(bean.getXuh()== null || !StringUtils.isNumeric(String.valueOf(bean.getXuh())) || !String.valueOf(bean.getXuh()).matches("^[1-9][0-9]{0,4}$")){
			if(bean.getXuh()== null || "".equals(bean.getXuh()) ||bean.getXuh()>9999 || bean.getXuh()<1){
				result = resultMessage(index,"序号必须为1-9999的正整数");
				wronginfo=wronginfo.append(result);
			}			
			String on = bean.getUsercenter()+bean.getXiehztbh()+bean.getGcbh()+bean.getXuh();
			if(only.get(on)==null){
				only.put(on, String.valueOf(index));
			}else{
				String index_old = String.valueOf(only.get(on));
				result = resultMessage(index,"第"+String.valueOf(index)+"行数据与第"+index_old+"行数据主键重复");
				wronginfo=wronginfo.append(result);
			}
			logger.info("当前校验数据条数index："+index);
			index ++;

			bean.setCreator(editor);
			bean.setCreate_time(new Date());
			bean.setEditor(editor);
			bean.setEdit_time(new Date());
			//如果有错误信息将会返回
			if(!wronginfo.toString().equals("")){
				wronginfo.append("\n");
				wrongcoun++;
				wrongdatas.add(bean);
				errorBuffer.append(wronginfo);
				wronginfo = new StringBuffer("");
				//return wronginfo.toString();
			}

			if(wrongdatas.size()>0 && wrongcoun==5){
				break;
			}
			
			if(cmap.get("usercenter").equals(bean.getUsercenter())){
				if((!"".equals(xiehztbzs))&&0 > xiehztbzs.indexOf(bean.getXiehztbh())){
					continue;
				}
				datasnew.add(bean);
			}	
		}
		
		
			
			//如果没有错误信息，正常操作，如果有则需要返回
			if(wrongdatas.size()<=0 && datasnew.size()>0){
				//在判断是插入 还是更新
				//先判断导入的数据是否存在 如果存在则更新
				String m = sqlInsertOrUpdate(datasnew,cmap);
				return m;
			}
			
			return errorBuffer.toString();
		}finally{
			//关闭数据库连接
			closeConnection();
		}
		
		
		
//			try {
//				Element rowElement = (Element) sheetElment.selectNodes("xmlns:Table/xmlns:Row").get(i);
//				
//				if (i > 1) {
//					// 取第三行 开始值 保存入库
//					fieldValue=getCellValue(rowElement,fieldList.size()); // 或者从第三行的每列值
//					fieldErrorValue = (ArrayList<String>) fieldValue.clone();
//				} else if(i==0) {
//					// 第一次 获得 字段名
//					fieldList=getCellValue(rowElement); // 得到第一行的每列值
//				}
//
//				if (i == 0) {
//					// 生成新增sql
//					insertSql = makeInsertSql(fieldList,null);
//					updateSql = makeUpdateSql(fieldList,keys,null);
//					selectSql = makeSelectSql(fieldList, keys);
//				} else if (i > 1) {
//					// 从第二行保存入库
//				message =addOrUpdateToTable(fieldValue, fieldList, insertSql,
//							updateSql, selectSql,fieldErrorValue,index);
//				}
//				//最对给用户 5行提示  数据不够5行的 按当前的行数给出提示
//				if(!"".equals(message)&& i==5){
//					break;
//				}
//			}catch(SQLException e){
//				num++;
//				//errorBuffer.append(e.getMessage()); //记录错误信息
//				errorBuffer.append(message); //记录错误信息
//			}catch (Exception e) {
//				num++;
//				//不处理 记录一日志
//				//errorBuffer.append("配置有误：错误信息为  " + e.getMessage()); //记录错误信息
//				errorBuffer.append("配置有误：错误信息为  " +  message); //记录错误信息
//			}finally{
//				fieldValue.clear();
//				fieldValue.trimToSize();
//				fieldErrorValue.clear();
//				fieldErrorValue.trimToSize();
//			}
		//关闭连接
//		if(conn!=null){
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		if( 0 != num ){
//			errorBuffer.insert(0, "错误："+num+"条      ");
//		}
//		return errorBuffer.toString();
	}
	
	
	/**
	 * 关闭数据库连接
	 */
	private void closeConnection(){
		//关闭连接
		if(conn!=null){
			try {
				if(!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	
	public String sqlInsertOrUpdate(ArrayList<CkxYunssjMb> datasnew,Map<String,String> cmap){
		String xiehztbzs = cmap.get("xiehztbzs");
		StringBuffer errorMessage = new StringBuffer(); //错误信息 
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteYunssjmbTempUpload",cmap);
		try{
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("transTime.insertCkxYunssjmbUpload",datasnew);
		}catch(Exception e){
			errorMessage.append("导入数据重复，请稍候再导入："+e.getMessage());
		}
		return errorMessage.toString();
//		for (CkxYunssjMb bean : datasnew) {
//			//判断导入的数据是新增还是更新后 在来做操作
//			//是否需要带标识来判断 数据是否存在
//			if(DBUtil.checkCount(baseDao,"transTime.getCountYunssjmb", bean)){
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.updateCkxYunssjmbUpload",bean);
//			}else{
//				//插入
//				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertCkxYunssjmbUpload",bean);
//			}
//
//		}
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
	private String makeUpdateSql(ArrayList<String> fieldList,String keys,ArrayList<String> fieldValue) {
		String[] keyStrs = keys.split(",");

		// update sys_bus_log set OPERATOR='2',OPERATOR_IP='22' where id='333'
		StringBuffer sb = new StringBuffer();
		sb.append("update ");
		sb.append(replacetable(table));
		sb.append(" set ");
		
		for (int i = 0; i < fieldList.size(); i++) {
			if(fieldValue==null){
				if (i == fieldList.size() - 1) {
					// 不加 逗号
					sb.append(fieldList.get(i) + "=" + createPlaceholder(fieldList.get(i)));
				} else {
					sb.append(fieldList.get(i) + "=" + createPlaceholder(fieldList.get(i))+",");
				}
			}else{
				if(fieldValue.get(i)!=null){
					if (i == fieldList.size() - 1) {
						// 不加 逗号
						sb.append(fieldList.get(i) + "=" + createPlaceholder(fieldList.get(i)));
					} else {
						sb.append(fieldList.get(i) + "=" + createPlaceholder(fieldList.get(i))+",");
					}
				}
			}
		}
		
		//剔除最后的逗号
		if(sb.toString().endsWith(",")){
			sb= sb.deleteCharAt(sb.length()-1);
		}
		//0007177  将修改人编辑为当前登录人  编辑时间写入数据库
		sb.append(",PIANYSJ=?,SHENGCXBH=?,biaos='1',");
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
	private String  addOrUpdateToTable(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String insertSql, String updateSql,
			String selectSql,ArrayList<String> fieldErrorValue,int index) throws SQLException {
		//错误信息
		StringBuffer esbs = new StringBuffer();
		String esb1 = null;
		if (getFiledByKey(fieldValue, fieldList, selectSql) < 1) {
			esb1 =insertValue(fieldValue, fieldList, insertSql,fieldErrorValue,index);
//			throw new ServiceException("错误数据");		
		} else {
			// 数据库有 则执行 修改
			if(fieldValue.contains(null)){
				//Value中有空值  则重新生成sql 踢出null
				String insertSqlNNull = makeUpdateSql(fieldList,keys,fieldValue);
				fieldValue = removeNull(fieldValue);
				updateValue(fieldValue, fieldList, insertSqlNNull,fieldErrorValue,index);
			}else{
				updateValue(fieldValue, fieldList, updateSql,fieldErrorValue,index);
			}	
		}
       
		return esbs.append(esb1).toString();
	}
	
	/**
	 * 移除fieldValue为Null的元素
	 * @param fieldValue
	 * @return
	 */
	private ArrayList<String> removeNull(ArrayList<String> fieldValue) {
		for(int i=0;i<fieldValue.size();){
			if(fieldValue.get(i)==null){
				fieldValue.remove(i);
			}else{
				i++;
			}
		}
		return fieldValue;
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
			ArrayList<String> fieldList, String updateSql,ArrayList<String> fieldErrorValue,int index) throws SQLException {
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
				if(i==0){
					if(null==fieldValue.get(i) || fieldValue.get(i).length()!=9){
						throw new SQLException("工艺消耗点："+fieldValue.get(i)+" 必填并且长度必须为9位");
					}
					//判断 车身数量为正整数且必填
					if(StringUtils.isBlank(String.valueOf(fieldValue.get(i+1))) || !StringUtils.isNumeric(String.valueOf(fieldValue.get(i+1)))){
						throw new SQLException("工艺车身数量："+fieldValue.get(i+1)+" 必填并且为正整数");
					}
					String f=fieldValue.get(i).substring(0,5);//取消耗点的前5为对应分配区
					String s=this.cmap.get("U"+f.substring(0, 1)+f);//根据用户中心+分配区编号，取产线
//					st.setObject(n, f);
					if(null==s){
						throw new SQLException("用户中心："+"U"+f.substring(0, 1)+" 分配区："+f+"没有对应有效产线");
					}
					//根据查询得到的生产线编号来获取 生产节拍 并计算出 偏移时间
					//验证生产线
					Shengcx scx = new Shengcx();
					scx.setUsercenter("U"+f.substring(0, 1));
					scx.setShengcxbh(s);
					scx.setBiaos("1");
					Shengcx sxcs = (Shengcx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryShengcxs", scx);
					//先判断 是否存在 生产节拍
					if(null!=sxcs){
						if(!sxcs.getShengcjp().isEmpty()){
							//计算出偏移时间  将值存入 list
							fieldValue.add(i+2,String.valueOf((int)Math.ceil((Integer.valueOf(fieldValue.get(i+1))*60.0)/Integer.parseInt(sxcs.getShengcjp()))));
						}else{
							throw new SQLException("生产线："+s+" 的生产节拍为空");
						}
					}else{
						throw new SQLException("用户中心："+"U"+f.substring(0, 1)+" 生产线："+s+"不存在");
					}
					//根据用户中心+分配区编号，取产线  ,将值存入list
					fieldValue.add(i+3, s);
//					st.setObject(n+1, s);
				}
				st.setObject(i+1, fieldValue.get(i));
			}
			st.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			//错误信息
			StringBuffer esb = new StringBuffer();
			for (int i = 0; i < keyStrs.length; i++) {
				esb.append(keyStrs[i]+"=");
				esb.append(fieldErrorValue.get(fieldList.indexOf(keyStrs[i]))+ "   ");
			}
			esb.append(" 此条数据修改失败.");
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
	 * 添加操作
	 * 
	 * @param fieldValue
	 * @param fieldList
	 * @param insertSql
	 * @throws SQLException
	 */
	private String  insertValue(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String insertSql,ArrayList<String> fieldErrorValue,int index) throws SQLException {
		//错误信息
		StringBuffer esb = new StringBuffer();
		String[] keyStrs = keys.split(",");
		String result = "";
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(insertSql);
			// 将占位符替换
			for (int i = 0; i < fieldValue.size(); i++) {
				if(i==0){
					if(null==fieldValue.get(i) || fieldValue.get(i).length()!=9){
						result = resultMessage(index,"工艺消耗点："+fieldValue.get(i)+" 必填并且长度必须为9位");
						esb=esb.append(result);
						//throw new SQLException("工艺消耗点："+fieldValue.get(i)+" 必填并且长度必须为9位");
					}
					//判断 车身数量为正整数且必填
					if(StringUtils.isBlank(String.valueOf(fieldValue.get(i+1))) || !StringUtils.isNumeric(String.valueOf(fieldValue.get(i+1)))){
						result = resultMessage(index,"工艺车身数量："+fieldValue.get(i+1)+" 必填并且为正整数");
						esb=esb.append(result);
						//throw new SQLException("工艺车身数量："+fieldValue.get(i+1)+" 必填并且为正整数");
					}
					String f=fieldValue.get(i).substring(0,5);//取消耗点的前5为对应分配区
					String s=this.cmap.get("U"+f.substring(0, 1)+f);//根据用户中心+分配区编号，取产线
//					st.setObject(n, f);
					if(null==s){
						result = resultMessage(index,"用户中心："+"U"+f.substring(0, 1)+" 分配区："+f+"没有对应有效产线");
						esb=esb.append(result);
						//throw new SQLException("用户中心："+"U"+f.substring(0, 1)+" 分配区："+f+"没有对应有效产线");
					}
					//根据查询得到的生产线编号来获取 生产节拍 并计算出 偏移时间
					//验证生产线
					Shengcx scx = new Shengcx();
					scx.setUsercenter("U"+f.substring(0, 1));
					scx.setShengcxbh(s);
					scx.setBiaos("1");
					Shengcx sxcs = (Shengcx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryShengcxs", scx);
					//先判断 是否存在 生产节拍
					if(null!=sxcs){
						if(!sxcs.getShengcjp().isEmpty()){
							//计算出偏移时间  将值存入 list
							try {
								fieldValue.add(i+2,String.valueOf((int)Math.ceil((Integer.valueOf(fieldValue.get(i+1))*60.0)/Integer.parseInt(sxcs.getShengcjp()))));
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}else{
							result = resultMessage(index,"生产线："+s+" 的生产节拍为空");
							esb=esb.append(result);
							//throw new SQLException("生产线："+s+" 的生产节拍为空");
						}
					}else{
						result = resultMessage(index,"用户中心："+"U"+f.substring(0, 1)+" 生产线："+s+"不存在");
						esb=esb.append(result);
						//throw new SQLException("用户中心："+"U"+f.substring(0, 1)+" 生产线："+s+"不存在");
					}
					//根据用户中心+分配区编号，取产线  ,将值存入list
					fieldValue.add(i+3, s);
//					st.setObject(n+1, s);
				}
				st.setObject(i+1, fieldValue.get(i));
			}
			st.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			for (int i = 0; i < keyStrs.length; i++) {
				esb.append(keyStrs[i]+"=");
				esb.append(fieldErrorValue.get(fieldList.indexOf(keyStrs[i]))+ "   ");
			}
			esb.append(" 此条数据添加失败.");
			esb.append(" : 错误信息   ");
			esb.append(e.getMessage());
			return esb.toString();
			//throw new SQLException(esb.toString());
		}finally{
			if(st!=null){
				st.close();
			}			
		}
			return esb.toString();
	}
	
	/**
	 * 添加操作
	 * 
	 * @param fieldValue
	 * @param fieldList
	 * @param insertSql
	 * @throws SQLException
	 */
	private void insertXhdbgValue(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String insertSql,ArrayList<String> fieldErrorValue) throws SQLException {
		String[] keyStrs = keys.split(",");
	
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(insertSql);

			// 将占位符替换
			for (int i = 0; i < fieldValue.size()-4; i++) {
				st.setObject(i+1, fieldValue.get(i));
			}
			st.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			//错误信息
			StringBuffer esb = new StringBuffer();
			for (int i = 0; i < keyStrs.length; i++) {
				esb.append(keyStrs[i]+"=");
				esb.append(fieldErrorValue.get(fieldList.indexOf(keyStrs[i]))+ "   ");
			}
			esb.append("YUANXHDBH=");
			esb.append(fieldErrorValue.get(4)+ "   ");
			esb.append(" 零件消耗点变更 数据添加失败.");
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
	private String makeInsertSql(ArrayList<String> fieldList,ArrayList<String> fieldValue) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb_value = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append(replacetable(table));
		sb.append(" ( ");
		
		for (int i = 0; i < fieldList.size(); i++) {
			if (i == fieldList.size() - 1) {
				// 不加 逗号
				if(fieldValue!=null){
					if(fieldValue.get(i)!=null){
						sb.append(fieldList.get(i));
						sb_value.append(createPlaceholder(fieldList.get(i)));
					}else{
						//去除逗号
						 if(sb.toString().endsWith(",")){
							 sb = sb.deleteCharAt(sb.length()-1);
							 sb_value = sb_value.deleteCharAt(sb_value.length()-1);
						 }
					}
				}else{
					sb.append(fieldList.get(i));
					sb_value.append(createPlaceholder(fieldList.get(i)));
				}
			} else {
				if(fieldValue!=null){
					if(fieldValue.get(i)!=null){
						sb.append(fieldList.get(i) + ",");
						sb_value.append(createPlaceholder(fieldList.get(i))+",");
					}
				}else{
					sb.append(fieldList.get(i) + ",");
					sb_value.append(createPlaceholder(fieldList.get(i))+",");
				}
			}
		}
		sb.append(",PIANYSJ,SHENGCXBH,BIAOS,CREATOR,CREATE_TIME,EDITOR,EDIT_TIME");
		sb.append(") values (");
		sb_value.append(",?,?,'1'");
		sb_value.append(",'"+editor+"'");
		sb_value.append(",sysdate");
		sb_value.append(",'"+editor+"'");
		sb_value.append(",sysdate");
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
			InputStream in = BaseSheetHandler.class.getClassLoader()
					.getResourceAsStream(
							"com/athena/component/config/sqlmap.properties");
			prop = new Properties();
			prop.load(in);

		} catch (Exception e) {
			logger.error("加载sqlmap.properties文件出错...");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据sheet的名字去从XML拿出此sheet的对象
	 * @param sheetName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Element getSheetElment(String sheetName){
		Element sheetElement = null;
		Node node = document.selectSingleNode("Workbook");
		List list = node.selectNodes("xmlns:Worksheet");
		for(int i=0;i<list.size();i++){
			Element ele = (Element) list.get(i);
			if(sheetName.equals(ele.attributeValue("Name"))){
				sheetElement = ele;
				break;
			}
		}
		return sheetElement;
	}
	
	/**
	 * 拿出此sheet中包含的列数  默认取 第一个行的包含列数
	 * @param sheetElement sheet对象
	 * @return
	 */
	private int getCellNumber(Element sheetElement){
		Element firstRowElment = (Element) sheetElement.selectNodes("xmlns:Table/xmlns:Row").get(0);
		return firstRowElment.selectNodes("xmlns:Cell").size();
	}
	/**
	 * 
	 * @param sheetElement sheet对象
	 * @return
	 */
	private int getRowNumber(Element sheetElement){
		return sheetElement.selectNodes("xmlns:Table/xmlns:Row").size();
	}
	
	/**
	 * 得到此行的 列的值
	 * @param rowElement
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList<String> getCellValue(Element rowElement){
		int top = 0;
		ArrayList<String> resultList = new ArrayList<String>();
		//拿出此行包含的列
		List cellList = rowElement.selectNodes("xmlns:Cell");
		for(int i=0;i<cellList.size();i++){
			Element cellElement = (Element) cellList.get(i);
			
			if(top==0){
				if(cellElement.attributeValue("Index")==null){
					//没有序列号 则认为此列为第一列
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}else{
					//补上前面的值 统一补null
					int b = Integer.parseInt(cellElement.attributeValue("Index"));
					for(int j=0;j<b-1;j++){
						resultList.add(null);
						top++;
					}
					//再将本身添加进去
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}
			}else{
				if(cellElement.attributeValue("Index")==null){
					//就是默认后面的
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}else{
					//补上 Integer.parseInt(cellElement.attributeValue("Index"))-top-1 个值
					int b = Integer.parseInt(cellElement.attributeValue("Index"));
					int top2 = top;
					for(int j=0;j<b-1-top2;j++){
						resultList.add(null);
						top++;
					}
					String value = getDataValue((Element)(cellElement.selectSingleNode("xmlns:Data")));
					resultList.add(value);
					top++;
				}
			}
		}
		
		return resultList;
	}
	/**
	 * 
	 * @param rowElement
	 * @param fieldSize
	 * @return如果最后列为空 则要放入null
	 */
	private ArrayList<String> getCellValue(Element rowElement,int fieldSize){
		ArrayList<String> resultList = getCellValue(rowElement);
		if(resultList.size()<fieldSize){
			for(int i=0;i<fieldSize-resultList.size();){
				resultList.add(null);
			}
		}
		return resultList;
	}
	
	/**
	 * 如果data标签不为空 则返回此value  为空则返回Null
	 * @param dataElement
	 * @return
	 */

	@SuppressWarnings("unchecked")
	private String getDataValue(Element dataElement){
		String result = null;
		if(dataElement!=null){
			//0007177 如果日期存了两层，则取到最里层的值，外面一层为空
			List<Branch> list = dataElement.content();
//			if(list.size() > 1){
//				Element e = (Element) list.get(1);
//				dataElement = e;
//			}			
			result = dataElement.getText();
		}
		return result;
	}

	private String resultMessage(int index, String message) 
	{
		StringBuffer flagSb = new StringBuffer();
		flagSb.append("  导入文件中第").append(index).append("行,").append(message);
		return flagSb.toString();
	}
	
}
