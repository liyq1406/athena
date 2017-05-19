package com.athena.ckx.util.xls;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.tree.ContentListFacade;
import org.apache.commons.lang.StringUtils;

import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.CkxLingjxhds;
import com.athena.ckx.entity.xuqjs.Lingjck;
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
public class XmlSheetHandlerlingjck extends SheetHandlerlingjck {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> mapcangk;
	private Map<String,String> mapzick;
	private Map<String,String> maplingj;
	private Map<String,String> mapbaoz;
	private Map<String,String> mapxiehzt;
	private Map<String,String> mapfenpq;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerlingjck.class);	//定义日志方法
	public XmlSheetHandlerlingjck(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(String editor,Map<String, String>  maplingj,Map<String,String>  mapcangk,Map<String, String>  mapzick,Map<String, String>  mapbaoz,Map<String, String>  mapxiehzt,AbstractIBatisDao baseDao) {
		try{
			this.editor = editor;
			this.mapbaoz=mapbaoz;
			this.mapcangk=mapcangk;
			this.mapzick=mapzick;
			this.maplingj=maplingj;
			this.mapxiehzt=mapxiehzt;
			this.mapfenpq= mapfenpq;
			this.baseDao = baseDao;
			// 对sheet处理 保存入库
			// 对sheet处理 保存入库
			Element sheetElment = getSheetElment(sheet);
			int rowNum = getRowNumber(sheetElment);
			int coluNum = getCellNumber(sheetElment);
			String result = "";
			//导入的数据超过5000行 提示不让插入
			if(rowNum>5002){
	//			if(conn!=null){
	//				try {
	//					if(!conn.isClosed())
	//						conn.close();
	//				} catch (SQLException e) {
	//					e.printStackTrace();
	//				}
	//			}
				
				String  message = "导入数据超过5000行,请调整数据行数";
				return message;
				//throw new ServiceException("导入数据超过5000行,请调整数据行数");
			}
			ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
			ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
			List<Lingjck> datas = new ArrayList<Lingjck>();
			StringBuffer errorBuffer = new StringBuffer(); //错误信息
			StringBuffer wronginfo=new StringBuffer("");
			int index = 3;
			List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
			for (int i = 2; i < list.size(); i++) {
				Lingjck lingjck = new Lingjck();
				//Element rowElement = (Element) sheetElment.selectNodes("xmlns:Table/xmlns:Row").get(i+2);
				Element rowElement =  (Element) list.get(i);
				fieldValue=getCellValue(rowElement,fieldList.size());
				for (int j = 0; j < coluNum; j++) {
					if( j == 0)
					{
						//用户中心
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z]+$")){
							lingjck.setUsercenter(fieldValue.get(j).toUpperCase());
						}
					}
					else if( j == 1)
					{
						//零件编号
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9_]+$")){
							lingjck.setLingjbh(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==2){
						//仓库编号
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9_]+$")){
							lingjck.setCangkbh(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==3){
						//子仓库编号
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9_]+$")){
							lingjck.setZickbh(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==4){
						//安全库存天数
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							if(fieldValue.get(j).matches("^[1-9][0-9]{0,2}(?:[.][0-9]{1,2})?$|0[.][0-9]{1,2}$|^0$")){
								lingjck.setAnqkcts(Double.valueOf(fieldValue.get(j)));
							}else if(null==fieldValue.get(j)){
								
							}else{
								lingjck.setAnqkcts(Double.valueOf("99999999"));
							}
						}
					}
					else if (j==5){
						//安全库存数量
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							if(fieldValue.get(j).matches("^[1-9][0-9]{0,5}$|0[.]0$|^0$")){
								lingjck.setAnqkcsl(Double.valueOf(fieldValue.get(j)));
							}else if(null==fieldValue.get(j)){
								
							}else{
								lingjck.setAnqkcsl(Double.valueOf("99999999"));
							}
						}
					}
					
					else if (j==6){
						//最大库存天数
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							if(fieldValue.get(j).matches("^[1-9][0-9]{0,2}(?:[.][0-9]{1,2})?$|0[.][0-9]{1,2}$|^0$")){
								lingjck.setZuidkcts(Double.valueOf(fieldValue.get(j)));
							}else if(null==fieldValue.get(j)){
								
							}else{
								lingjck.setZuidkcts(Double.valueOf("99999999")); 
							}
						}
					}
					
					else if (j==7){
						//最大库存数量
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							if(fieldValue.get(j).matches("^[1-9][0-9]{0,5}$|0[.]0$|^0$")){
								lingjck.setZuidkcsl(Double.valueOf(fieldValue.get(j)));
							}else if(null==fieldValue.get(j)){
								
							}else{
								lingjck.setZuidkcsl(Double.valueOf("99999999999"));
							}
						}
					}
					
					else if (j==8){
						//US包装类型
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9_]+$")){
							lingjck.setUsbzlx(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==9){
						//US包装容量
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							if(fieldValue.get(j).matches("^[0-9]{1,6}$|^[0-9]{1,6}[.][0-9]{1,3}$|^0$")){
								lingjck.setUsbzrl(Double.valueOf(fieldValue.get(j)));
							}
						}
					}
					else if (j==10){
						//UC类型
						if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9_]+$")){
							lingjck.setUclx(fieldValue.get(j).toUpperCase());
						}
					}
					else if (j==11){
						//UC容量
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							if(fieldValue.get(j).matches("^[0-9]{1,6}$|^[0-9]{1,6}[.][0-9]{1,3}$|^0$")){
								lingjck.setUcrl(Double.valueOf(fieldValue.get(j)));
							}
						}
					}
					else if (j==12){
						//是否自动发送
						if(null!=fieldValue.get(j)&& !StringUtils.isBlank(fieldValue.get(j))){
							lingjck.setZidfhbz(fieldValue.get(j));
						}
					}
			   }
			    datas.add(lingjck);
			}		
			
			//校验有误的条数
			int wrongcoun=0;
			
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<Lingjck> wrongdatas =  new ArrayList<Lingjck>();
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<Lingjck> datasnew =  new ArrayList<Lingjck>();
			
			//校验每个字段
			for (int i=0;i<datas.size();i++) {
				Lingjck ljck = datas.get(i);
				String s="";
				//校验用户中心
				if(null==ljck.getUsercenter() || StringUtils.isBlank(ljck.getUsercenter()) || ljck.getUsercenter().length()!=2){
					if(null==ljck.getUsercenter()){
						ljck.setUsercenter("");
					}
					result = resultMessage(index,"用户中心："+ljck.getUsercenter()+" 必填 只能由数字、字母以及下划线组成并且长度必须为2位");
					wronginfo=wronginfo.append(result);
				}
				//校验零件编号必填
				if(null==ljck.getLingjbh() || StringUtils.isBlank(ljck.getLingjbh()) || ljck.getLingjbh().length()!=10){
					if(null==ljck.getLingjbh()){
						ljck.setLingjbh("");
					}
					result = resultMessage(index,"零件编号："+ljck.getLingjbh()+"  必填 只能由数字、字母以及下划线组成并且长度必须为10位");
					wronginfo=wronginfo.append(result);
				}else{
					//校验零件编号的存在有效性
					 if (null==maplingj.get(ljck.getUsercenter()+ljck.getLingjbh())){
						 result = resultMessage(index,"用户中心："+ljck.getUsercenter() +" 零件编号："+ljck.getLingjbh()+" 不存在或已失效");
						wronginfo=wronginfo.append(result);
					 }
				}
				//校验   仓库编号必填且存在有效性  
				if(null==ljck.getCangkbh() || StringUtils.isBlank(ljck.getCangkbh()) || ljck.getCangkbh().length()!=3){
					if(null==ljck.getCangkbh()){
						ljck.setCangkbh("");
					}
					result = resultMessage(index,"仓库编号："+ljck.getCangkbh()+" 必填 只能由数字、字母以及下划线组成 并且长度必须为3位");
					wronginfo=wronginfo.append(result);
				}else{
					//在校验 仓库编号的存在有效性
					if(null==mapcangk.get(ljck.getUsercenter()+ljck.getCangkbh())){
						result = resultMessage(index,"仓库编号："+ljck.getCangkbh()+" 不存在或已失效");
						wronginfo=wronginfo.append(result);
					}
				}
				//校验   子仓库编号必填且存在有效性  
				if(null==ljck.getZickbh() || StringUtils.isBlank(ljck.getZickbh()) || ljck.getZickbh().length()!=3){
					if(null==ljck.getZickbh()){
						ljck.setZickbh("");
					}
					result = resultMessage(index,"子仓库编号："+ljck.getZickbh()+" 必填 只能由数字、字母以及下划线组成 并且长度必须为3位");
					wronginfo=wronginfo.append(result);
				}else{
					//在校验 子仓库编号的存在有效性
					if(null==mapzick.get(ljck.getUsercenter()+ljck.getCangkbh()+ljck.getZickbh())){
						result = resultMessage(index,"子仓库编号："+ljck.getZickbh()+" 不存在或已失效");
						wronginfo=wronginfo.append(result);
					}else{
						//校验完仓库和子仓库的必填并存在性后,自动带出  卸货站台编号 前提是 仓库和子仓库都不为空
						if(null==mapxiehzt.get(ljck.getUsercenter()+ljck.getCangkbh()+ljck.getZickbh())){
							result = resultMessage(index,"用户中心："+ljck.getUsercenter()+" 仓库编号："+ljck.getCangkbh()+" 子仓库编号："+ljck.getZickbh()+" 没有对应有效的卸货站台");
							wronginfo=wronginfo.append(result);
						}else{
							ljck.setXiehztbh(mapxiehzt.get(ljck.getUsercenter()+ljck.getCangkbh()+ljck.getZickbh()));
						}
					} 
				}
				
				//安全库存天数
				if(null!=ljck.getAnqkcts() && !ljck.getAnqkcts().toString().matches("^[1-9][0-9]{0,2}(?:[.][0-9]{1,2})?$|0[.][0-9]{1,2}$|^0$")){
						result = resultMessage(index,"安全库存天数   只能为 0-999.99");
						wronginfo=wronginfo.append(result);
				}
				//安全库存数量
				if(null!=ljck.getAnqkcsl() && !ljck.getAnqkcsl().toString().matches("^[1-9][0-9]{0,5}(?:[.]0)?$|0[.]0$|^0$")){
						result = resultMessage(index,"安全库存数量   只能为 0-999999");
						wronginfo=wronginfo.append(result);
				}
				//最大库存天数
				if(null!=ljck.getZuidkcts() && !ljck.getZuidkcts().toString().matches("^[1-9][0-9]{0,2}(?:[.][0-9]{1,2})?$|0[.][0-9]{1,2}$|^0$")){
						result = resultMessage(index,"最大库存天数   只能为 0-999.99");
						wronginfo=wronginfo.append(result);
				}
				//最大库存数量
				if(null!=ljck.getZuidkcsl()){
						BigDecimal Zuidkcsl = new BigDecimal(ljck.getZuidkcsl().doubleValue());
						if(!Zuidkcsl.toString().matches("^[1-9][0-9]{0,5}(?:[.]0)?$|0[.]0$|^0$")){
							result = resultMessage(index,"最大库存数量   只能为 0-999999");
							wronginfo=wronginfo.append(result);
						}
				}
				//US包装类型
				if(null==ljck.getUsbzlx() || StringUtils.isBlank(ljck.getUsbzlx()) || ljck.getUsbzlx().length()!=5){
					if(null==ljck.getUsbzlx()){
						ljck.setUsbzlx("");
					}
					result = resultMessage(index,"US包装类型："+ljck.getUsbzlx()+"  必填 只能由数字、字母以及下划线组成并且长度必须为5位");
					wronginfo=wronginfo.append(result);
				}else{
					//校验US包装类型的存在有效性
					 if (null==mapbaoz.get(ljck.getUsbzlx())){
						result = resultMessage(index,"US包装类型："+ljck.getUsbzlx() +" 不存在或已失效");
						wronginfo=wronginfo.append(result);
					 }
				}
				//UC类型
				if(null==ljck.getUclx() || StringUtils.isBlank(ljck.getUclx()) || ljck.getUclx().length()!=5){
					if(null==ljck.getUclx()){
						ljck.setUclx("");
					}
					result = resultMessage(index,"UC类型："+ljck.getUclx()+"  必填 只能由数字、字母以及下划线组成并且长度必须为5位");
					wronginfo=wronginfo.append(result);
				}else{
					//校验US包装类型的存在有效性
					 if (null==mapbaoz.get(ljck.getUclx())){
						result = resultMessage(index,"UC类型："+ljck.getUclx() +" 不存在或已失效");
						wronginfo=wronginfo.append(result);
					 }
				}
				//US包装容量
				if(null==ljck.getUsbzrl() || StringUtils.isBlank(ljck.getUsbzrl().toString())){
	//				if(null==ljck.getUsbzrl()){
	//					ljck.setUsbzrl(Double.valueOf(" "));
	//				}
					result = resultMessage(index,"US包装容量   必填并且只能为 0-999999.999");
					wronginfo=wronginfo.append(result);
				}
	//			if(null!=ljck.getUsbzrl()&& !StringUtils.isBlank(ljck.getUsbzrl().toString())){
	//				if(0.0==ljck.getUsbzrl()){
	//					result = resultMessage(index,"US包装容量   只能为 0-9999999");
	//					wronginfo=wronginfo.append(result);
	//				}
	//			}
				//UC容量
				if(null==ljck.getUcrl() || StringUtils.isBlank(ljck.getUcrl().toString())){
	//				if(null==ljck.getUcrl()){
	//					ljck.setUcrl(Double.valueOf(" "));
	//				}
					result = resultMessage(index,"UC容量   必填并且只能为 0-999999.999");
					wronginfo=wronginfo.append(result);
				}
	//			if(null!=ljck.getUcrl()&& !StringUtils.isBlank(ljck.getUcrl().toString())){
	//				if(0.0==ljck.getUcrl()){
	//					result = resultMessage(index,"UC容量   只能为 0-9999999");
	//					wronginfo=wronginfo.append(result);
	//				}
	//			}
				//是否自动发送 (1/0)
				if(null!=ljck.getZidfhbz() && !StringUtils.isBlank(ljck.getZidfhbz())){
					if((!"1".equals(ljck.getZidfhbz()) && !"0".equals(ljck.getZidfhbz()))){
						result = resultMessage(index,"是否自动发送："+ljck.getZidfhbz()+" 只能为 1/0中的一种");
						wronginfo=wronginfo.append(result);
					}
				}
				ljck.setCreator(editor);
				ljck.setCreate_time(DateTimeUtil.getAllCurrTime());
				ljck.setEditor(editor);
				ljck.setEdit_time(DateTimeUtil.getAllCurrTime());
				index ++;
				//如果有错误信息将会返回
				if(!wronginfo.toString().equals("")){
					wronginfo.append("\n");
					wrongcoun=wrongcoun+1;
					wrongdatas.add(ljck);
					errorBuffer.append(wronginfo);
					wronginfo= new StringBuffer("");
				}
				datasnew.add(ljck);
				if(wrongdatas.size()>0 && wrongcoun==5){
					break;
				}
			}
			
			
			
				//如果没有错误信息，正常操作，如果有则需要返回
				if(wrongdatas.size()<=0){
					
					//在判断是插入 还是更新
					//先判断导入的数据是否存在 如果存在则更新
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

	
	
	public  String  sqlInsertOrUpdate(ArrayList<Lingjck> datasnew){
		try {
			for (Lingjck lingjck : datasnew) {
				//判断导入的数据是新增还是更新后 在来做操作
				if(DBUtil.checkCount(baseDao,"ts_ckx.getCheckCountLingjCK", lingjck)){
					 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateLingjckImport",lingjck);
				}else{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertLingjckImport",lingjck);
				}
			}
		} catch (DataAccessException e) {
			logger.info(e.getMessage());
			return "插入或更新失败";
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
		sb.append(",SHENGCXBH=?,biaos='1',");
		sb.append("jiesr=to_date('9999-12-31','yyyy-MM-dd'),");
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
			String selectSql,ArrayList<String> fieldErrorValue) throws SQLException {

		if (getFiledByKey(fieldValue, fieldList, selectSql) < 1) {
			insertValue(fieldValue, fieldList, insertSql,fieldErrorValue);
//			throw new ServiceException("错误数据");		
		} else {
			// 数据库有 则执行 修改
			if(fieldValue.contains(null)){
				//Value中有空值  则重新生成sql 踢出null
				String insertSqlNNull = makeUpdateSql(fieldList,keys,fieldValue);
				fieldValue = removeNull(fieldValue);
				updateValue(fieldValue, fieldList, insertSqlNNull,fieldErrorValue);
			}else{
				updateValue(fieldValue, fieldList, updateSql,fieldErrorValue);
			}	
		}

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
			ArrayList<String> fieldList, String updateSql,ArrayList<String> fieldErrorValue) throws SQLException {
		String[] keyStrs = keys.split(",");
		
		//将keyValues的值保存到fieldValue中
		for (int i = 0; i < keyStrs.length; i++) {
			//keyValues[i] = fieldValue.get(fieldList.indexOf(keyStrs[i]));
			fieldValue.add(fieldValue.get(fieldList.indexOf(keyStrs[i])));
		}
	
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(updateSql);
			if(fieldValue.get(4).length()==9){
				//原消耗点编号  必须要提前存在 
				Map map1 = new HashMap();
				map1.put("usercenter", fieldValue.get(0));
				map1.put("lingjbh", fieldValue.get(1));
				map1.put("xiaohdbh",fieldValue.get(4));
				map1.put("biaos", "1");
				CkxLingjxhd Lingjxhds = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjxhd",map1);
				if(null==Lingjxhds){
					throw new SQLException("原消耗点编号："+fieldValue.get(4)+" 不存在");
				}
			}
			// 将占位符替换
			for (int i = 0; i < fieldValue.size(); i++) {
				if(i+1==3){
					String f=fieldValue.get(i).substring(0,5);//取消耗点的前5为对应分配区
					String s=this.mapfenpq.get(fieldValue.get(i-2)+f);//根据用户中心+分配区编号，取产线
//					st.setObject(n, f);
					if(null==s){
						throw new SQLException("用户中心："+fieldValue.get(i-2)+"分配区："+f+"没有对应有效产线");
					}
					
					//根据用户中心+分配区编号，取产线  ,将值存入list
					fieldValue.add(i+7, s);
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
	private void insertValue(ArrayList<String> fieldValue,
			ArrayList<String> fieldList, String insertSql,ArrayList<String> fieldErrorValue) throws SQLException {
		String[] keyStrs = keys.split(",");
	
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(insertSql);
			if(fieldValue.get(4).length()==9){
				//原消耗点编号  必须要提前存在 
				Map map1 = new HashMap();
				map1.put("usercenter", fieldValue.get(0));
				map1.put("lingjbh", fieldValue.get(1));
				map1.put("xiaohdbh",fieldValue.get(4));
				map1.put("biaos", "1");
				CkxLingjxhd Lingjxhds = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjxhd",map1);
				if(null==Lingjxhds){
					throw new SQLException("原消耗点编号："+fieldValue.get(4)+" 不存在");
				}
			}
			// 将占位符替换
			for (int i = 0; i < fieldValue.size(); i++) {
				if(i+1==3){
					
					
					String f=fieldValue.get(i).substring(0,5);//取消耗点的前5为对应分配区
					String s=this.mapfenpq.get(fieldValue.get(i-2)+f);//根据用户中心+分配区编号，取产线
					st.setObject(i+8, f);
					if(null==s){
						throw new SQLException("用户中心："+fieldValue.get(i-2)+"分配区："+f+"没有对应有效产线");
					}
					//根据用户中心+分配区编号，取产线
					st.setObject(i+9, s);
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
			esb.append(" 此条数据添加失败.");
			esb.append(" : 错误信息   ");
			esb.append(e.getMessage());
	
			throw new SQLException(esb.toString());
		}finally{
			if(st!=null){
				st.close();
			}			
		}

	}

	@Transactional
	private void biangLingjxhd(CkxLingjxhd ckxLingjxhd)  throws SQLException{
		//根据新老消耗点去查询对应的PDS拆分标识
		Map map = new HashMap();
		map.put("usercenter", ckxLingjxhd.getUsercenter());
		map.put("lingjbh", ckxLingjxhd.getLingjbh());
		map.put("xiaohdbh", ckxLingjxhd.getXiaohdbh());
		map.put("biaos", "1");
		CkxLingjxhd Lingjxhd = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjxhd",map);
		Map map1 = new HashMap();
		map1.put("usercenter", ckxLingjxhd.getUsercenter());
		map1.put("lingjbh", ckxLingjxhd.getLingjbh());
		map1.put("xiaohdbh", ckxLingjxhd.getYuanxhdbh());
		map1.put("biaos", "1");
		CkxLingjxhd Lingjxhds = (CkxLingjxhd)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjxhd",map1);
//		if(null==Lingjxhd){
//			throw new SQLException("消耗点编号："+ckxLingjxhd.getXiaohdbh()+" 不存在");
//		}
//		if(null==Lingjxhds){
//			throw new SQLException("原消耗点编号："+ckxLingjxhd.getYuanxhdbh()+" 不存在");
//		}
		//根据新老消耗点去查询对应的PDS拆分标识  如果标识为 按需  则 对应的插入表ckx_lingjxhd_s
		//插入的时候 需要注意判断 如果 ckx_lingjxhd_s 表中不存在此条记录 则 直接插入 初始化为 未生效状态,并且 shengxr 和 jiesr 默认都是空.待用户去该页面手动生效时填写
		//如ckx_lingjxhd_s表中存在此条记录,则清空 shengxr 和 jiesr  并且标识为 未生效
		//只要PDS拆分标识中的值不为空 就可以替换。新消耗点或老消耗点 中的任意一个
		if((!"".equals(Lingjxhd.getPdsbz())&& null!=Lingjxhd.getPdsbz())||(!"".equals(Lingjxhds.getPdsbz())&& null!=Lingjxhds.getPdsbz())){
			//if(Lingjxhd.getPdsbz().equals("按需")||Lingjxhds.getPdsbz().equals("按需")){
				CkxLingjxhds ckxLingjxhds = new CkxLingjxhds();
				ckxLingjxhds.setUsercenter(ckxLingjxhd.getUsercenter());
				ckxLingjxhds.setLingjbh(ckxLingjxhd.getLingjbh());
				ckxLingjxhds.setXiaohd(ckxLingjxhd.getXiaohdbh());
				ckxLingjxhds.setYuanxhd(ckxLingjxhd.getYuanxhdbh());
				ckxLingjxhds.setBiaos("0");
				ckxLingjxhds.setCreator(ckxLingjxhd.getCreator());
				ckxLingjxhds.setEditor(ckxLingjxhd.getEditor());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.mergelingjXhds",ckxLingjxhds);
			//}
		}
//		else{
//			throw new ServiceException("零件消耗点变更中的 新老消耗点的PDS拆分标识不能全部为空,并且PDS拆分标识只能为  按需");
//		}
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
	private String makeInsertxhdbgSql(ArrayList<String> fieldList,ArrayList<String> fieldValue) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb_value = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append("ZBC_TEST.CKX_LINGJXHD_S");
		sb.append(" ( ");
		
		for (int i = 0; i < fieldList.size()-4; i++) {
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
		sb.append("BIAOS,CREATOR,CREATE_TIME,EDITOR,EDIT_TIME");
		sb.append(") values (");
		sb_value.append("'0'");
		sb_value.append(",'"+editor+"'");
		sb_value.append(",sysdate");
		sb_value.append(",'"+editor+"'");
		sb_value.append(",sysdate");
		sb.append(sb_value.toString() + ")");

		return sb.toString();
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
		sb.append(",FENPQBH,SHENGCXBH,BIAOS,JIESR,CREATOR,CREATE_TIME,EDITOR,EDIT_TIME");
		sb.append(") values (");
		sb_value.append(",?,?,'1'");
		sb_value.append(",to_date('9999-12-31','yyyy-MM-dd')");
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
