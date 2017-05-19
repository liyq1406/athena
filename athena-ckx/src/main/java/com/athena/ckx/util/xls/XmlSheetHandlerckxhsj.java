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

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.entity.carry.CkxCangkxhsj;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Post;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
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
public class XmlSheetHandlerckxhsj extends SheetHandlerckxhsj {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> mapcangk;
	private Map<String,String> mapcangkzick;
	private Map<String,String> mapfenpq;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerckxhsj.class);	//定义日志方法
	public XmlSheetHandlerckxhsj(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(Map<String,String>mapcangk,Map<String,String>mapcangkzick,Map<String,String>mapfenpq,String editor,AbstractIBatisDao baseDao) {
		try{
			
		this.editor = editor;
		this.baseDao = baseDao;
		String result = "";
		this.mapcangk=mapcangk;
		this.mapcangkzick=mapcangkzick;
		this.mapfenpq=mapfenpq;
		// 对sheet处理 保存入库
		Element sheetElment = getSheetElment(sheet);
		int rowNum = getRowNumber(sheetElment);
		int coluNum = getCellNumber(sheetElment);
		//导入的数据超过5000行 提示不让插入
		if(rowNum>5002){
			String  message = "导入数据超过5000行,请调整数据行数";
			return message;
			//throw new ServiceException("导入数据超过5000行,请调整数据行数");
		}
		ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
		ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
		List<CkxCangkxhsj> datas = new ArrayList<CkxCangkxhsj>();
		StringBuffer errorBuffer = new StringBuffer(); //错误信息
		StringBuffer wronginfo=new StringBuffer("");
		int index = 3;
		List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
		for (int i = 2; i < list.size(); i++) {
			CkxCangkxhsj ckxCangkxhsj = new CkxCangkxhsj();
			//Element rowElement = (Element) sheetElment.selectNodes("xmlns:Table/xmlns:Row").get(i+2);
			Element rowElement =  (Element) list.get(i);
			fieldValue=getCellValue(rowElement,fieldList.size());
			for (int j = 0; j < coluNum; j++) {
				if( j == 0)
				{
					if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z]+$")){
						ckxCangkxhsj.setUsercenter(fieldValue.get(j).toUpperCase());
					}
				}
				else if( j == 1)
				{
					if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9_]+$")){
						ckxCangkxhsj.setCangkbh(fieldValue.get(j).toUpperCase());
					}
				}
				else if (j==2){
					if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9_]+$")){
						ckxCangkxhsj.setFenpqhck(fieldValue.get(j).toUpperCase());
					}
				}
				else if (j==3){
					if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[A-Za-z0-9]+$")){
						ckxCangkxhsj.setMos(fieldValue.get(j).toUpperCase());
					}
				}
				else if (j==4){
					if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[0-9]{1,2}$")){
						ckxCangkxhsj.setCangkshpcf(Integer.valueOf(fieldValue.get(j)));
					}
				}
				else if (j==5){
					if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
						ckxCangkxhsj.setCangkshsj(Double.valueOf(fieldValue.get(j)));
					}
				}
				else if (j==6){
					if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
						ckxCangkxhsj.setCangkfhsj(Double.valueOf(fieldValue.get(j)));
					}
				}
				else if (j==7){
					if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
						ckxCangkxhsj.setBeihsj(Double.valueOf(fieldValue.get(j)));
					}
				}
				else if (j==8){
					if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
						ckxCangkxhsj.setIbeihsj(Double.valueOf(fieldValue.get(j)));
					}
				}
				else if (j==9){
					if(null!=fieldValue.get(j)&& fieldValue.get(j).matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
						ckxCangkxhsj.setPbeihsj(Double.valueOf(fieldValue.get(j)));
					}
			    }
				else if (j==10){
					ckxCangkxhsj.setShifzdbh(fieldValue.get(j));
			    }
		   }
		    datas.add(ckxCangkxhsj);
		}		
		
		//校验有误的条数
		int wrongcoun=0;
		
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<CkxCangkxhsj> wrongdatas =  new ArrayList<CkxCangkxhsj>();
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<CkxCangkxhsj> datasnew =  new ArrayList<CkxCangkxhsj>();
		
		//校验每个字段
		for (int i=0;i<datas.size();i++) {
			CkxCangkxhsj ckxhsj = datas.get(i);
			//校验用户中心
			if(null==ckxhsj.getUsercenter() || StringUtils.isBlank(String.valueOf(ckxhsj.getUsercenter())) || ckxhsj.getUsercenter().length()!=2){
				if(null==ckxhsj.getUsercenter()){
					ckxhsj.setUsercenter("");
				}
				result = resultMessage(index,"用户中心："+ckxhsj.getUsercenter()+" 必填并且长度必须为2位");
				wronginfo=wronginfo.append(result);
			}
			//校验仓库编号必填 且有效存在性
			if(null==ckxhsj.getCangkbh() || StringUtils.isBlank(String.valueOf(ckxhsj.getCangkbh())) || ckxhsj.getCangkbh().length()!=6){
				if(null==ckxhsj.getCangkbh()){
					ckxhsj.setCangkbh("");
				}
				result = resultMessage(index,"仓库编号："+ckxhsj.getCangkbh()+" 必填并且长度必须为6位");
				wronginfo=wronginfo.append(result);
			}else{
				//校验仓库编号的存在有效性
				//Cangk cangk =new Cangk();
				//cangk.setUsercenter(ckxhsj.getUsercenter());
				//cangk.setCangkbh(ckxhsj.getCangkbh().substring(0, 3));
				//cangk.setBiaos("1");
				String cangk = mapcangk.get(ckxhsj.getUsercenter()+ckxhsj.getCangkbh().substring(0, 3));
				if(null==cangk){
					result = resultMessage(index,"仓库编号："+ckxhsj.getCangkbh().substring(0, 3)+" 不存在或者已失效");
					wronginfo=wronginfo.append(result);
					//throw new ServiceException("不存在此用户中心下的仓库编号或仓库编号已失效");
				}
				//校验子仓库编号以及子仓库编号的存在性
//				Zick zick=new Zick();
//				zick.setCangkbh(ckxhsj.getCangkbh().substring(0, 3));
//				zick.setZickbh(ckxhsj.getCangkbh().substring(3, 6));
//				zick.setUsercenter(ckxhsj.getUsercenter());
//				zick.setBiaos("1");
				String cangkzck = mapcangkzick.get(ckxhsj.getUsercenter()+ckxhsj.getCangkbh().substring(0, 3)+ckxhsj.getCangkbh().substring(3, 6));
				if(null==cangkzck){
					result = resultMessage(index,"子仓库表中不存在用户中心下："+ckxhsj.getUsercenter()+" 仓库号为:"+ckxhsj.getCangkbh().substring(0, 3)+"且子仓库号为:"+ckxhsj.getCangkbh().substring(3, 6)+"  的数据或该数据已失效");
					wronginfo=wronginfo.append(result);
					//throw new ServiceException(mes1);
				}
			}
			//校验   模式 必须为 RD,SY,M1，MD,RM 中的一种 否则提示给用户    
			if(null==ckxhsj.getMos() || StringUtils.isBlank(String.valueOf(ckxhsj.getMos())) || (!"RD".equals(ckxhsj.getMos()) && !"RM".equals(ckxhsj.getMos()) && !"M1".equals(ckxhsj.getMos()) && !"MD".equals(ckxhsj.getMos()) && !"SY".equals(ckxhsj.getMos()) && !"MV".equals(ckxhsj.getMos()) && !"MJ".equals(ckxhsj.getMos()))){
				if(null==ckxhsj.getMos()){
					ckxhsj.setMos("");
				}
				result = resultMessage(index,"模式："+ckxhsj.getMos()+" 必填并且只能为 RD/SY/M1/MD/RM/MV/MJ 中的一种");
				wronginfo=wronginfo.append(result);
			}
			//校验 分配循环和仓库  当模式为 RD\SY时,FENPQHCK为循环(即校验他的存在且有效性).当模式为M1\MD\RM时,FENPQHCK为仓库+子仓库(即校验他的存在且有效性)  
			if(null==ckxhsj.getFenpqhck() || StringUtils.isBlank(String.valueOf(ckxhsj.getFenpqhck())) || !(ckxhsj.getFenpqhck().length()>=5 && ckxhsj.getFenpqhck().length()<=6)){
				if(null==ckxhsj.getFenpqhck()){
					ckxhsj.setFenpqhck("");
				}
				result = resultMessage(index,"循环/仓库："+ckxhsj.getFenpqhck()+" 必填并且最小长度必须为5位,最大为6位");
				wronginfo=wronginfo.append(result);
			}else{
				//在根据上面的校验规则来判断
				if("RD".equals(ckxhsj.getMos())||"SY".equals(ckxhsj.getMos())){
					    if(5 != ckxhsj.getFenpqhck().length()){
					    	result = resultMessage(index,"模式为 RD/SY 时,循环/仓库："+ckxhsj.getFenpqhck()+" 必须为  5位分配区");
						    wronginfo=wronginfo.append(result);
					    }else{
					    	//在校验 分配区的存在且有效性
							//Fenpq fenpq=new Fenpq();
							//fenpq.setUsercenter(ckxhsj.getUsercenter());
							//fenpq.setFenpqh(ckxhsj.getFenpqhck());
							//fenpq.setBiaos("1");
					    	String fenpq = mapfenpq.get(ckxhsj.getUsercenter()+ckxhsj.getFenpqhck());
							if(null==fenpq){
								result = resultMessage(index,"分配区表中不存在当前用户中心下分配区号为："+ckxhsj.getFenpqhck()+"的数据或该数据已失效");
								wronginfo=wronginfo.append(result);
							}
					    }
				}else{
					if(6 != ckxhsj.getFenpqhck().length()){
				    	result = resultMessage(index,"模式为 M1/RM/MD/MV/MJ 时,循环/仓库："+ckxhsj.getFenpqhck()+" 必须为 3位仓库和3位子仓库");
						wronginfo=wronginfo.append(result);
				    }else{
				    	//在校验 仓库的存在且有效性
						//校验子仓库编号以及子仓库编号的存在性
						//Zick zck=new Zick();
						//zck.setCangkbh(ckxhsj.getFenpqhck().substring(0, 3));
						//zck.setZickbh(ckxhsj.getFenpqhck().substring(3, 6));
						//zck.setUsercenter(ckxhsj.getUsercenter());
						//zck.setBiaos("1");
						String cangkzick = mapcangkzick.get(ckxhsj.getUsercenter()+ckxhsj.getFenpqhck().substring(0, 3)+ckxhsj.getFenpqhck().substring(3, 6));
						if(null==cangkzick){
							result = resultMessage(index,"子仓库表中不存在用户中心下："+ckxhsj.getUsercenter()+" 仓库号为:"+ckxhsj.getFenpqhck().substring(0, 3)+"且子仓库号为:"+ckxhsj.getFenpqhck().substring(3, 6)+"  的数据或该数据已失效");
							wronginfo=wronginfo.append(result);
						}
				    }
				}
//				if("M1".equals(ckxhsj.getMos())||"MD".equals(ckxhsj.getMos())||"RM".equals(ckxhsj.getMos())){
//					    
//				}
			}
			
			//校验仓库送货频次F  规则为 number(2)
			if(null==ckxhsj.getCangkshpcf() || StringUtils.isBlank(String.valueOf(ckxhsj.getCangkshpcf())) || !ckxhsj.getCangkshpcf().toString().matches("^[0-9]{1,2}$")){
				result = resultMessage(index,"仓库送货频次F 必填并且只能为 0-99的整数");
				//+ckxhsj.getCangkshpcf()
				wronginfo=wronginfo.append(result);
			}
			//校验仓库送货时间   规则为 number(5,2)
			if(null==ckxhsj.getCangkshsj() || StringUtils.isBlank(String.valueOf(ckxhsj.getCangkshsj())) || !ckxhsj.getCangkshsj().toString().matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
					 result = resultMessage(index,"仓库送货时间    必填并且只能为 0-999.99");
					 //ckxhsj.getCangkshsj()
					 wronginfo=wronginfo.append(result);
			}
			//校验仓库返回时间   规则为 number(5,2)
			if(null==ckxhsj.getCangkfhsj() || StringUtils.isBlank(String.valueOf(ckxhsj.getCangkfhsj())) || !ckxhsj.getCangkfhsj().toString().matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
				 result = resultMessage(index,"仓库返回时间    必填并且只能为 0-999.99");
				 //+ckxhsj.getCangkfhsj()
				 wronginfo=wronginfo.append(result);
		    }
			//校验备货时间   规则为 number(5,2)
			if(null==ckxhsj.getBeihsj() || StringUtils.isBlank(String.valueOf(ckxhsj.getBeihsj())) || !ckxhsj.getBeihsj().toString().matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
				 result = resultMessage(index,"备货时间    必填并且只能为 0-999.99");
				 //+ckxhsj.getBeihsj()
				 wronginfo=wronginfo.append(result);
		    }
			//校验I类型备货时间   规则为 number(5,2)
			if(null==ckxhsj.getIbeihsj() || StringUtils.isBlank(String.valueOf(ckxhsj.getIbeihsj())) || !ckxhsj.getIbeihsj().toString().matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
				 result = resultMessage(index,"I类型备货时间   必填并且只能为 0-999.99");
				 //+ckxhsj.getIbeihsj()
				 wronginfo=wronginfo.append(result);
		    }
			//校验P类型备货时间   规则为 number(5,2)
			if(null==ckxhsj.getPbeihsj() || StringUtils.isBlank(String.valueOf(ckxhsj.getPbeihsj())) || !ckxhsj.getPbeihsj().toString().matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
				 result = resultMessage(index,"P类型备货时间   必填并且只能为 0-999.99");
				 //+ckxhsj.getPbeihsj()
				 wronginfo=wronginfo.append(result);
		    }
			//校验是否自动补货  
			if(null==ckxhsj.getShifzdbh() || StringUtils.isBlank(String.valueOf(ckxhsj.getShifzdbh())) || (!"0".equals(ckxhsj.getShifzdbh()) && !"1".equals(ckxhsj.getShifzdbh()))){
				if(null==ckxhsj.getShifzdbh()){
					ckxhsj.setShifzdbh("");
				}
				 result = resultMessage(index,"是否自动补货："+ckxhsj.getShifzdbh()+" 必填 并且只能为 0或1");
				 wronginfo=wronginfo.append(result);
		    }
			ckxhsj.setShengxbs("1");
			ckxhsj.setCreator(editor);
			ckxhsj.setCreateTime(DateTimeUtil.getAllCurrTime());
			ckxhsj.setEditor(editor);
			ckxhsj.setEditTime(DateTimeUtil.getAllCurrTime());
			index ++;
			//如果有错误信息将会返回
			if(!wronginfo.toString().equals("")){
				wronginfo.append("\n");
				wrongcoun=wrongcoun+1;
				wrongdatas.add(ckxhsj);
				errorBuffer.append(wronginfo);
				wronginfo= new StringBuffer("");
				//return wronginfo.toString();
			}
			datasnew.add(ckxhsj);
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
			//关闭数据库连接
			closeConnection();
		}
		
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

	
	public String  sqlInsertOrUpdate(ArrayList<CkxCangkxhsj> datasnew){
		try {
			for (CkxCangkxhsj ckxhsj : datasnew) {
				//判断导入的数据是新增还是更新后 在来做操作
				if(DBUtil.checkCount(baseDao,"carry.getCountCangkxhsj", ckxhsj)){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.updateCkxCangkxhsjbyexport",ckxhsj);
				}else{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("carry.insertCkxCangkxhsj",ckxhsj);
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
