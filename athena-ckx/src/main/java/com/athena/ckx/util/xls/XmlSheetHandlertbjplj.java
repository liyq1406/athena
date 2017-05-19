package com.athena.ckx.util.xls;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.dom4j.tree.ContentListFacade;

import com.athena.ckx.entity.carry.CkxShengcxXianb;
import com.athena.ckx.entity.paicfj.Ckx_shengcx;
import com.athena.ckx.entity.xuqjs.CkxGongyxhd;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.CkxLingjxhds;
import com.athena.ckx.entity.xuqjs.Lingjck;
import com.athena.ckx.entity.xuqjs.Peislb;
import com.athena.ckx.entity.xuqjs.Post;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Tongbjplj;
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
public class XmlSheetHandlertbjplj extends SheetHandlertbjplj {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,Object> maplingj;
	private Map<String,Object> mapscx ;
	private Map<String,Object> mappslx;
	private Map<String,String> maptbjplj;
	private Map<String,String> mapljck;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlertbjplj.class);	//定义日志方法
	public XmlSheetHandlertbjplj(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(Map<String,String> mapljck,Map<String,String> maptbjplj,Map<String,Object> maplingj,Map<String,Object> mapscx,Map<String,Object> mappslx,String editor,AbstractIBatisDao baseDao) {
		try{
			this.editor = editor;
			this.maplingj=maplingj;
			this.mapscx=mapscx;
			this.mappslx=mappslx;
			this.maptbjplj=maptbjplj;
			this.baseDao = baseDao;
			this.mapljck=mapljck;
			String result = "";
			// 对sheet处理 保存入库
			Element sheetElment = getSheetElment(sheet);
			int rowNum = getRowNumber(sheetElment);
			// 导入的数据超过5000行 提示不让插入
			if (rowNum > 5002) {
				String message = "导入数据超过5000行,请调整数据行数";
				return message;
			}
			int coluNum = getCellNumber(sheetElment);
			ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
			ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
			List<Tongbjplj> datas = new ArrayList<Tongbjplj>();
			StringBuffer errorBuffer = new StringBuffer(); //错误信息
			StringBuffer wronginfo=new StringBuffer("");
			int index = 3;
			List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
			for (int i = 2; i < list.size(); i++) {
				Tongbjplj tongbjplj = new Tongbjplj();
				//Element rowElement = (Element) sheetElment.selectNodes("xmlns:Table/xmlns:Row").get(i+2);
				Element rowElement =  (Element) list.get(i);
				fieldValue=getCellValue(rowElement,fieldList.size());
				for (int j = 0; j < coluNum; j++) {
					if( j == 0)
					{
						tongbjplj.setUsercenter(fieldValue.get(j));
					}
					else if( j == 1)
					{
						tongbjplj.setLingjbh(fieldValue.get(j));
					}
					else if( j == 2)
					{
						tongbjplj.setShengcxbh(fieldValue.get(j));
					}
					else if( j == 3)
					{
						if(null!=fieldValue.get(j) && StringUtils.isNotBlank(fieldValue.get(j))){
							tongbjplj.setNclass(fieldValue.get(j).toUpperCase());
						}
					}
					else if( j == 4)
					{
						if(null!=fieldValue.get(j) && StringUtils.isNotBlank(fieldValue.get(j))){
							tongbjplj.setNvalue(fieldValue.get(j).toUpperCase());
						}
					}
					else if( j == 5)
					{
						tongbjplj.setPeislx(fieldValue.get(j));
					}
				}
				datas.add(tongbjplj);
			}
			
			//校验有误的条数
			int wrongcoun=0;
			
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<Tongbjplj> wrongdatas =  new ArrayList<Tongbjplj>();
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<Tongbjplj> datasnew =  new ArrayList<Tongbjplj>();
			
			//校验每个字段
			for (int i=0;i<datas.size();i++) {
				String lingjmc = "";
				Tongbjplj tbjplj = datas.get(i);
				//校验用户中心
				if(null==tbjplj.getUsercenter() || " ".equals(tbjplj.getUsercenter()) || tbjplj.getUsercenter().length()!=2){
					result = resultMessage(index,"用户中心："+tbjplj.getUsercenter()+" 必填并且长度必须为2位");
					wronginfo=wronginfo.append(result);
				}
				//校验零件编号
				if(null==tbjplj.getLingjbh() || " ".equals(tbjplj.getLingjbh()) || tbjplj.getLingjbh().length()!=10){
					if(null==tbjplj.getLingjbh()){
						tbjplj.setLingjbh("");
					}
					result = resultMessage(index,"零件编号："+tbjplj.getLingjbh()+" 必填并且长度必须为10位");
					wronginfo=wronginfo.append(result);
				}else{
					//如果填了并且也是十位  这个时候校验 存在有效性  并带出 零件名称
					//CkxLingj ckxlj = new CkxLingj();
					//ckxlj.setUsercenter(tbjplj.getUsercenter());
					//ckxlj.setLingjbh(tbjplj.getLingjbh());
					//ckxlj.setBiaos("1");
					//CkxLingj lj = (CkxLingj)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLingj", ckxlj);
					CkxLingj lj  = (CkxLingj)maplingj.get(tbjplj.getUsercenter()+tbjplj.getLingjbh());
					if(null!=lj){
						lingjmc = lj.getZhongwmc();
					}else{
						result = resultMessage(index,"用户中心："+tbjplj.getUsercenter()+" 零件编号："+tbjplj.getLingjbh()+" 的数据不存在或已失效");
						wronginfo=wronginfo.append(result);
					}
				}
				//校验生产线编号
				if(null==tbjplj.getShengcxbh() || " ".equals(tbjplj.getShengcxbh()) || tbjplj.getShengcxbh().length()!=5){
					if(null==tbjplj.getLingjbh()){
						tbjplj.setShengcxbh("");
					}
					result = resultMessage(index,"生产线编号："+tbjplj.getShengcxbh()+" 必填并且长度必须为5位");
					wronginfo=wronginfo.append(result);
				}else{
					//如果填了并且也是五位  这个时候校验 存在有效性  
					//Shengcx ckxscx = new Shengcx();
					//ckxscx.setUsercenter(tbjplj.getUsercenter());
					//ckxscx.setShengcxbh(tbjplj.getShengcxbh());
					//ckxscx.setBiaos("1");
					//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getShengcx", ckxscx);
					Shengcx scx = (Shengcx)mapscx.get(tbjplj.getUsercenter()+tbjplj.getShengcxbh());
					if(null==scx){
						result = resultMessage(index,"用户中心："+tbjplj.getUsercenter()+" 生产线编号："+tbjplj.getShengcxbh()+" 的数据不存在或已失效");
						wronginfo=wronginfo.append(result);
					}
				}
				//校验NCLASS  长度为 4位  只能是 数字、字母以及下划线
				if(null==tbjplj.getNclass() || " ".equals(tbjplj.getNclass()) || tbjplj.getNclass().length()!=4 || !tbjplj.getNclass().matches("^[A-Za-z0-9_]+$")){
					if(null==tbjplj.getLingjbh()){
						tbjplj.setNclass("");
					}
					result = resultMessage(index,"CLASS："+tbjplj.getNclass()+" 必填并且长度必须为4位,只能由数字、字母以及下划线组成");
					wronginfo=wronginfo.append(result);
				}
				
				//校验NVALUE 长度为 3位  只能是 数字、字母以及下划线
				if(null==tbjplj.getNvalue() || " ".equals(tbjplj.getNvalue()) || tbjplj.getNvalue().length()!=3 || !tbjplj.getNvalue().matches("^[A-Za-z0-9_]+$")){
					if(null==tbjplj.getLingjbh()){
						tbjplj.setNvalue("");
					}
					result = resultMessage(index,"VALUE："+tbjplj.getNvalue()+" 必填并且长度必须为3位,只能由数字、字母以及下划线组成");
					wronginfo=wronginfo.append(result);
				}
				//校验配送类型  
				if(null==tbjplj.getPeislx() || " ".equals(tbjplj.getPeislx()) || tbjplj.getPeislx().length()!=4){
					if(null==tbjplj.getLingjbh()){
						tbjplj.setPeislx("");
					}
					result = resultMessage(index,"配送类型："+tbjplj.getPeislx()+" 必填并且长度必须为4位");
					wronginfo=wronginfo.append(result);
				}else{
					//如果填了并且也是四位  这个时候校验 存在有效性  
					//Peislb pslx = new Peislb();
					//pslx.setUsercenter(tbjplj.getUsercenter());
					//pslx.setPeislx(tbjplj.getPeislx());
					//pslx.setBiaos("1");
					//Peislb px = (Peislb)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getPeislb", pslx);
					Peislb  px = (Peislb)mappslx.get(tbjplj.getUsercenter()+tbjplj.getPeislx());
					if(null==px){
						result = resultMessage(index,"用户中心："+tbjplj.getUsercenter()+" 配送类型："+tbjplj.getPeislx()+" 的数据不存在或已失效");
						wronginfo=wronginfo.append(result);
					}else{
						//Lingjck ljck = new Lingjck();
						//ljck.setUsercenter(bean.getUsercenter());
						//ljck.setLingjbh(bean.getLingjbh());
						//ljck.setCangkbh(pslb.getCangkbh());
						//ljck.setZickbh(pslb.getZickbh());
						String zickbh = mapljck.get(tbjplj.getUsercenter()+tbjplj.getLingjbh()+px.getCangkbh()+px.getZickbh());
						//Lingjck  lingjck = (Lingjck)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryLingjck", ljck);
						if(null==zickbh){
							result = resultMessage(index,"配送类型"+px.getPeislx()+"对应的仓库与零件"+tbjplj.getLingjbh()+"在零件-仓库中未设置");
							wronginfo=wronginfo.append(result);
						}
					}
				}
				//所有的校验后 需要查询数据是否存在，如果存在 则提示给用户 (前提是校验全部通过后在检查数据是否存在)
				if(wronginfo.toString().equals("")){
					String  tplj =maptbjplj.get(tbjplj.getUsercenter()+tbjplj.getLingjbh()+tbjplj.getShengcxbh()+tbjplj.getNclass());
					if(null!=tplj){
						result = resultMessage(index,"用户中心："+tbjplj.getUsercenter()+" 零件编号："+tbjplj.getLingjbh()+" 生产线编号："+tbjplj.getShengcxbh()+" NCLASS："+tbjplj.getNclass()+" 的数据已存在");
						wronginfo=wronginfo.append(result);
					}
				}
				index ++;
				tbjplj.setLingjmc(lingjmc);
				tbjplj.setCreator(editor);
				tbjplj.setCreate_time(DateTimeUtil.getAllCurrTime());
				tbjplj.setEditor(editor);
				tbjplj.setEdit_time(DateTimeUtil.getAllCurrTime());
				//如果有错误信息将会返回
				if(!wronginfo.toString().equals("")){
					wronginfo.append("\n");
					wrongcoun=wrongcoun+1;
					wrongdatas.add(tbjplj);
					errorBuffer.append(wronginfo);
					wronginfo= new StringBuffer("");
				}
				datasnew.add(tbjplj);
				if(wrongdatas.size()>0 && wrongcoun==5){
					break;
				}
			}
			
			
				//如果没有错误信息，正常操作，如果有则需要返回
				if(wrongdatas.size()<=0){
					//插入数据
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

	
	public String sqlInsertOrUpdate(ArrayList<Tongbjplj> datasnew){
		try {
			for (Tongbjplj tbjplj : datasnew) {
				 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertTongbjplj",tbjplj);
			}
		} catch (DataAccessException e) {
			logger.info(e.getMessage());
			return "插入失败";
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
					String s=this.maptbjplj.get("U"+f.substring(0, 1)+f);//根据用户中心+分配区编号，取产线
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
					String s=this.maptbjplj.get("U"+f.substring(0, 1)+f);//根据用户中心+分配区编号，取产线
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
//			List<Branch> list = dataElement.content();
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
