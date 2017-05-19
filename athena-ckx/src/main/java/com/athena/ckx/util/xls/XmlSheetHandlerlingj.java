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
import jxl.Sheet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.Post;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 按照XML文件处理
 * @author chenlei
 * @vesion 1.0
 * @date 2012-7-26
 */
public class XmlSheetHandlerlingj extends SheetHandlerlingj {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private String usercenter;
	private Map<String,String> cmap;
	private Map<String,Object> mapusercenter;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerlingj.class);	//定义日志方法
	public XmlSheetHandlerlingj(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(Map<String, Object> mapusercenter,String editor,Map<String,String> cmap,AbstractIBatisDao baseDao,String usercenter) {
		try{
			this.editor = editor;
			this.usercenter = usercenter;
			this.baseDao = baseDao;
			this.cmap = cmap;
			this.mapusercenter = mapusercenter;
			String result = "";
			// 对sheet处理 保存入库
			Element sheetElment = getSheetElment(sheet);
			int rowNum = getRowNumber(sheetElment);
			int coluNum = getCellNumber(sheetElment);
			//导入的数据超过5000行 提示不让插入
			if (rowNum > 5002) {
				String message = "导入数据超过5000行,请调整数据行数";
				return message;
				// throw new ServiceException("导入数据超过5000行,请调整数据行数");
			}
			ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
			ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
			List<CkxLingj> datas = new ArrayList<CkxLingj>();
			StringBuffer errorBuffer = new StringBuffer(); //错误信息
			StringBuffer wronginfo=new StringBuffer("");
			int index = 3;
			List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
			for (int i = 2; i < list.size(); i++) {
				CkxLingj ckxlingj = new CkxLingj();
				//Element rowElement = (Element) sheetElment.selectNodes("xmlns:Table/xmlns:Row").get(i+2);
				Element rowElement =  (Element) list.get(i);
				fieldValue=getCellValue(rowElement,fieldList.size());
				for (int j = 0; j < coluNum; j++) {
					if( j == 0)
					{
						ckxlingj.setUsercenter(fieldValue.get(j));
					}
					else if( j == 1)
					{
							ckxlingj.setLingjbh(fieldValue.get(j));
					}
					else if (j==2){
						ckxlingj.setLingjlx(fieldValue.get(j));
					}
					else if (j==3){
						ckxlingj.setLingjsx(fieldValue.get(j));
					}
					else if (j==4){
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[1-9][0-9]{0,3}(?:[.][0-9]{1,3})?$|0[.][0-9]{1,3}$|^0$")){
							ckxlingj.setZhuangcxs(Double.valueOf(fieldValue.get(j)));
						}
					}
					else if (j==5){
						//非必填
						if(null!=fieldValue.get(j) && StringUtils.isNotBlank(fieldValue.get(j))){
							ckxlingj.setAnqm(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==6){
						//非必填
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[1-9][0-9]{0,3}(?:[.][0-9]{1,3})?$|0[.][0-9]{1,3}$|^0$")){
							ckxlingj.setLingjzl(Double.valueOf(fieldValue.get(j)));
						}
					}
					else if (j==7){
						ckxlingj.setGuanjljjb(fieldValue.get(j));
					}
					else if (j==8){
						if(null!=fieldValue.get(j) && StringUtils.isNotBlank(fieldValue.get(j))){
							ckxlingj.setAnjmlxhd(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==9){
						ckxlingj.setJihy(fieldValue.get(j));
				    }
			   }
			    datas.add(ckxlingj);
			}		
			
			//校验有误的条数
			int wrongcoun=0;
			
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<CkxLingj> wrongdatas =  new ArrayList<CkxLingj>();
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<CkxLingj> datasnew =  new ArrayList<CkxLingj>();
			
			//校验每个字段
			for (int i=0;i<datas.size();i++) {
				CkxLingj ckxlj = datas.get(i);
				//校验用户中心
				if(null==ckxlj.getUsercenter() || " ".equals(ckxlj.getUsercenter()) || ckxlj.getUsercenter().length()!=2){
					if(null==ckxlj.getUsercenter()){
						ckxlj.setUsercenter("");
					}
					result = resultMessage(index,"用户中心："+ckxlj.getUsercenter()+" 必填并且长度必须为2位");
					wronginfo=wronginfo.append(result);
				}
	//			else{
	//				//校验用户中心的存在性
	//				if(null==mapusercenter.get(ckxlj.getUsercenter())){
	//					result = resultMessage(index,"用户中心："+ckxlj.getUsercenter()+" 不存在或已失效");
	//					wronginfo=wronginfo.append(result);
	//				}
	//			}
				//校验零件编号
				if(null==ckxlj.getLingjbh() || " ".equals(ckxlj.getLingjbh()) || ckxlj.getLingjbh().length()!=10 || !ckxlj.getLingjbh().matches("^[A-Za-z0-9_]+$")){
					if(null==ckxlj.getLingjbh()){
						ckxlj.setLingjbh("");
					}
					result = resultMessage(index,"零件编号："+ckxlj.getLingjbh()+" 必填并且长度必须为10位,只能由数字、字母以及下划线组成");
					wronginfo=wronginfo.append(result);
				}else{
					//校验用户中心+零件编号 的存在性 只校验存在性 不校验有效性
					if(usercenter.equals(ckxlj.getUsercenter())){
						CkxLingj lingj	= (CkxLingj)mapusercenter.get(ckxlj.getUsercenter()+ckxlj.getLingjbh());
						if(null==lingj){
						     result = resultMessage(index,"用户中心："+ckxlj.getUsercenter()+ " 零件编号："+ckxlj.getLingjbh()+" 的数据不存在");
						     wronginfo=wronginfo.append(result);
					  }
					}
				}
				//校验零件类型  必须是 A B C 中的一种
				if(null==ckxlj.getLingjlx() || " ".equals(ckxlj.getLingjlx()) || (!"A".equals(ckxlj.getLingjlx()) && !"B".equals(ckxlj.getLingjlx()) && !"C".equals(ckxlj.getLingjlx()))){
					if(null==ckxlj.getLingjlx()){
						ckxlj.setLingjlx("");
					}
					result = resultMessage(index,"零件类型："+ckxlj.getLingjlx()+" 必填并且只能为 A/B/C中的一种");
					wronginfo=wronginfo.append(result);
				}
				//校验零件属性  必须是 A K M 中的一种
				if(null==ckxlj.getLingjsx() || " ".equals(ckxlj.getLingjsx()) || (!"A".equals(ckxlj.getLingjsx()) && !"K".equals(ckxlj.getLingjsx()) && !"M".equals(ckxlj.getLingjsx()))){
					if(null==ckxlj.getLingjsx()){
						ckxlj.setLingjsx("");
					}
					result = resultMessage(index,"零件属性："+ckxlj.getLingjsx()+" 必填并且只能为 A/K/M中的一种");
					wronginfo=wronginfo.append(result);
				}
				
				//校验装车系数  规则为 number(7,3)
				//String regex = "^[1-9][0-9][0-9][0-9]{0,1}(?:[.][0-9]{1,3})?$|0[.][0-9]{1,3}$|^0$";
				if(null==ckxlj.getZhuangcxs() || " ".equals(ckxlj.getZhuangcxs().toString()) || !ckxlj.getZhuangcxs().toString().matches("^[1-9][0-9]{0,3}(?:[.][0-9]{1,3})?$|0[.][0-9]{1,3}$|^0$")){
					result = resultMessage(index,"装车系数   必填并且只能为 0-9999.999");
					wronginfo=wronginfo.append(result);
				}
				//校验安全码
				if(null!=ckxlj.getAnqm() && !" ".equals(ckxlj.getAnqm())){
					if( ckxlj.getAnqm().length()>10 || !ckxlj.getAnqm().matches("^[A-Za-z0-9_]+$")){
						result = resultMessage(index,"安全码："+ckxlj.getAnqm()+"长度不能超过10位,只能由数字、字母以及下划线组成");
						wronginfo=wronginfo.append(result);
					}
				}
				//校验零件重量  规则为 number(7,3)
				if(null!=ckxlj.getLingjzl() && !" ".equals(ckxlj.getLingjzl())){
					if(!ckxlj.getLingjzl().toString().matches("^[1-9][0-9]{0,3}(?:[.][0-9]{1,3})?$|0[.][0-9]{1,3}$|^0$")){
						result = resultMessage(index,"零件重量   只能为 0-9999.999");
						wronginfo=wronginfo.append(result);
					}
				}
				//校验关键零件级别   必须是 1 2 3 中的一种
				if(null==ckxlj.getGuanjljjb() || " ".equals(ckxlj.getGuanjljjb()) || (!"1".equals(ckxlj.getGuanjljjb()) && !"2".equals(ckxlj.getGuanjljjb()) && !"3".equals(ckxlj.getGuanjljjb()))){
					if(null==ckxlj.getGuanjljjb()){
						ckxlj.setGuanjljjb("");
					}
					result = resultMessage(index,"关键零件级别："+ckxlj.getGuanjljjb()+" 必填并且只能为 1/2/3中的一种");
					wronginfo=wronginfo.append(result);
				}
				//校验按件目录卸货点    
	//			if(null!=ckxlj.getAnjmlxhd() && ckxlj.getAnjmlxhd().length()>10 &&ckxlj.getAnjmlxhd().matches("^[A-Za-z0-9_]+$")){
	//				if(null==ckxlj.getAnjmlxhd()){
	//					ckxlj.setAnjmlxhd("");
	//				}
	//				result = resultMessage(index,"按件目录卸货点："+ckxlj.getAnjmlxhd()+" 长度不能超过10位");
	//				wronginfo=wronginfo.append(result);
	//			}
				if(null!=ckxlj.getAnjmlxhd() && !" ".equals(ckxlj.getAnjmlxhd())){
					if( ckxlj.getAnjmlxhd().length()>10 || !ckxlj.getAnjmlxhd().matches("^[A-Za-z0-9_]+$")){
						result = resultMessage(index,"按件目录卸货点："+ckxlj.getAnjmlxhd()+"长度不能超过10位,只能由数字、字母以及下划线组成");
						wronginfo=wronginfo.append(result);
					}
				}
				//校验计划员组 
				if(null==ckxlj.getJihy() || " ".equals(ckxlj.getJihy())){
					if(null==ckxlj.getJihy()){
						ckxlj.setJihy("");
					}
					result = resultMessage(index,"计划员组："+ckxlj.getJihy()+" 必填");
					wronginfo=wronginfo.append(result);
				}else{
					//如果计划员组不为空 则校验 计划员组存在并有效且所属业务是计划员组
					//Post post = new Post();
					//post.setPostCode(ckxlj.getJihy());
					//post.setDicCode("JIHUAY");
					//post.setBiaos("1");
					//先判断 用户登录的当前用户中心下不能导入其他用户中心的数据，计划员的校验 也必须是以当前登录的用户中心为准
					if(!usercenter.equals(ckxlj.getUsercenter())){
						result = resultMessage(index,"用户登录的当前用户中心："+usercenter+" 下不能导入 用户中心："+ ckxlj.getUsercenter() +" 的数据");
						wronginfo=wronginfo.append(result);
					}else{
						String jihy = cmap.get(ckxlj.getJihy());
						if(null==jihy){
							result = resultMessage(index,"计划员组："+ckxlj.getJihy()+" 在用户中心："+ usercenter +" 不存在 或 存在但所属业务不是计划员组");
							wronginfo=wronginfo.append(result);
						}
					}
				}
				ckxlj.setBiaos("1");
				ckxlj.setCreator(editor);
				ckxlj.setCreate_time(DateTimeUtil.getAllCurrTime());
				ckxlj.setEditor(editor);
				ckxlj.setEdit_time(DateTimeUtil.getAllCurrTime());
				index ++;
				//如果有错误信息将会返回
				if(!wronginfo.toString().equals("")){
					wronginfo.append("\n");
					wrongcoun=wrongcoun+1;
					wrongdatas.add(ckxlj);
					errorBuffer.append(wronginfo);
					wronginfo= new StringBuffer("");
					//return wronginfo.toString();
				}
				datasnew.add(ckxlj);
				if(wrongdatas.size()>0 && wrongcoun==5){
					break;
				}
			}
			
			
				//如果没有错误信息，正常操作，如果有则需要返回
				if(wrongdatas.size()<=0){
					//直接进行更新操作
					//sqlInsertOrUpdate(datasnew);
					return sqlInsertOrUpdate(datasnew);
				}
				
				  return errorBuffer.toString();
		}finally{
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
	}

	
	public String sqlInsertOrUpdate(ArrayList<CkxLingj> datasnew){
		try {
			for (CkxLingj ckxLingj : datasnew) {
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingj",ckxLingj);
				}
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.info(e.getMessage());
			return "更新失败";
		}
		return "";
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
			if(list.size() > 1){
				Element e = (Element) list.get(1);
				dataElement = e;
			}			
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
