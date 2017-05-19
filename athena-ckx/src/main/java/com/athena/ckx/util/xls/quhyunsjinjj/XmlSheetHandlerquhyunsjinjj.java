package com.athena.ckx.util.xls.quhyunsjinjj;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.Lingjshhbftj;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.entity.xuqjs.JinjjQuhYuns;
import com.athena.ckx.entity.xuqjs.QuhYuns;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetPostOnly;
import com.athena.db.ConstantDbCode;

import com.toft.core3.dao.DataAccessException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


/**
 * 按照XML文件处理
 * @author lc
 * @date 2016-11-3
 */
public class XmlSheetHandlerquhyunsjinjj extends SheetHandlerquhyunsjinjj {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> cmap;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerquhyunsjinjj.class);	//定义日志方法
	public XmlSheetHandlerquhyunsjinjj(Document document, String sheet, String table,
			String datasourceId, String clazz, String keys, String dateColumns,
			String dateFormats) {
		super(document, sheet, table, datasourceId, clazz, keys, dateColumns,
				dateFormats);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doSheetHandler(String editor,Map<String,String> cmap,AbstractIBatisDao baseDao) throws BiffException, IOException, ParseException {
		try{
			
		this.editor = editor;
		this.cmap=cmap;
		this.baseDao = baseDao;
		String result = "";
		// 对sheet处理 保存入库
		Element sheetElment = getSheetElment(sheet);
		int rowNum = getRowNumber(sheetElment);
		//导入的数据超过5000行 提示不让插入
		if(rowNum>5002){
			String  message = "导入数据超过5000行,请调整数据行数";
			return message;
		}
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		if(!"root".equals(key)){	
			if(0 > key.indexOf("QUHYFJHY")){
				if(map.get("QUHYFJHY")=="" || null == map.get("QUHYFJHY")){
					String  message = "您没有取货运费计划员权限，不能操作数据";	
					return message;
				}
			}
		}
		int coluNum = getCellNumber(sheetElment);
		ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
		ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
		ArrayList<String> fieldErrorValue = new ArrayList<String>(); // 一列的值 为错误使用
		//String insertSql = null; // 新增sql语句
		//String updateSql = null; // 修改sql语句
		//String selectSql = null; // 查找sql语句
		List<JinjjQuhYuns> datas = new ArrayList<JinjjQuhYuns>();
		StringBuffer errorBuffer = new StringBuffer(); //错误信息
		StringBuffer wronginfo=new StringBuffer("");
		int num = 0;
		int index = 3;
		List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
		//校验有误的条数
		int wrongcoun=0;
		
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<JinjjQuhYuns> wrongdatas =  new ArrayList<JinjjQuhYuns>();
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<JinjjQuhYuns> datasnew =  new ArrayList<JinjjQuhYuns>();
		for (int i = 2; i < list.size(); i++) {
			JinjjQuhYuns quhYuns=new JinjjQuhYuns();
			Element rowElement =  (Element) list.get(i);
			fieldValue=getCellValue(rowElement,fieldList.size());
			quhYuns.setUsercenter(fieldValue.get(0));	
			quhYuns.setGongysdm(fieldValue.get(1));
			quhYuns.setChengysdm(fieldValue.get(2));
			quhYuns.setShengxsj(fieldValue.get(3));
			quhYuns.setShixsj(fieldValue.get(4));
			datas.add(quhYuns);
		}
		for (int i = 2; i < list.size(); i++) {
			JinjjQuhYuns quhYuns=new JinjjQuhYuns();
			Element rowElement =  (Element) list.get(i);
			fieldValue=getCellValue(rowElement,fieldList.size());
			for (int j = 0; j < coluNum; j++) {
				if( j == 0)
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						quhYuns.setUsercenter(fieldValue.get(j).toUpperCase());
					}
				}
				
				

			}
			if(null == fieldValue.get(0)|| "".equals(fieldValue.get(0))){
				result = resultMessage(index,"用户中心：用户中心不能为空");
				wronginfo=wronginfo.append(result);
			}
			if(null == fieldValue.get(1)|| "".equals(fieldValue.get(1))){
				result = resultMessage(index,"承运商：承运商代码不能为空");
				wronginfo=wronginfo.append(result);
			}
			if(null == fieldValue.get(2)|| "".equals(fieldValue.get(2))){
				result = resultMessage(index,"供应商：供应商代码不能为空");
				wronginfo=wronginfo.append(result);
			}
			if(null == fieldValue.get(3)|| "".equals(fieldValue.get(3))){
				result = resultMessage(index,"生效时间：生效开始时间不能为空");
				wronginfo=wronginfo.append(result);
			}
			if(null == fieldValue.get(4)|| "".equals(fieldValue.get(4))){
				result = resultMessage(index,"失效时间：失效开始时间不能为空");
				wronginfo=wronginfo.append(result);
			}
			if(null == fieldValue.get(5)|| "".equals(fieldValue.get(5))){
				result = resultMessage(index,"趟次单价：趟次单价不能为空");
				wronginfo=wronginfo.append(result);
			}
			
			if(null != fieldValue.get(0)&& !"".equals(fieldValue.get(0))){
				if(!fieldValue.get(0).matches("^[A-Z]{2}$")){
					result = resultMessage(index,"用户中心：必须为2位只能由字母组成");
					wronginfo=wronginfo.append(result);
				}else{
					//用户中心权限校验
					if((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")!=null && ((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")).size()>0){
						List<String> uclist=(List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY");
						if(!uclist.contains(fieldValue.get(0))){
							result = resultMessage(index,"用户中心：您没有权限操作用户中心为"+fieldValue.get(0)+"的数据");
							wronginfo=wronginfo.append(result);
						}
					}
				}
			}
			if(null != fieldValue.get(1)&& !"".equals(fieldValue.get(1))){
				if(!fieldValue.get(1).matches("^[0-9A-Z]{6}[\\s0-9A-Z]{2}[0-9A-Z]{2}$")){
					result = resultMessage(index,"承运商代码：只能输入大写字母或数字,固定长度10位,7,8位能输入空格");
					wronginfo=wronginfo.append(result);
				}else if(StringUtils.isNotBlank(fieldValue.get(0))){
					 Gongcy gongys=new Gongcy();
					 gongys.setUsercenter(fieldValue.get(0));
					 gongys.setGcbh(fieldValue.get(1));
					 gongys.setBiaos("1");
					 Object obj1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountGongys", gongys);
					if (Integer.valueOf(obj1.toString())==0) {
						result = resultMessage(index,"承运商代码："+fieldValue.get(1)+"在参考系供应商不存在！");
						wronginfo=wronginfo.append(result);
					}
				}
			}	
			if(null != fieldValue.get(2)&& !"".equals(fieldValue.get(2))){
				if(!fieldValue.get(2).matches("^[0-9A-Z]{6}[\\s0-9A-Z]{2}[0-9A-Z]{2}$")){
					result = resultMessage(index,"供应商代码：只能输入大写字母或数字,固定长度10位,7,8位能输入空格");
					wronginfo=wronginfo.append(result);
				}else if(StringUtils.isNotBlank(fieldValue.get(0))){
					 Gongcy gongys=new Gongcy();
					 gongys.setUsercenter(fieldValue.get(0));
					 gongys.setGcbh(fieldValue.get(2));
					 gongys.setBiaos("1");
					 gongys.setLeix("3");//类型不等于承运商
					 Object obj1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountGongys", gongys);
					if (Integer.valueOf(obj1.toString())==0) {
						result = resultMessage(index,"供应商代码："+fieldValue.get(2)+"在参考系供应商不存在！");
						wronginfo=wronginfo.append(result);
					}
				}
			}

			
			if(null != fieldValue.get(3)&& !"".equals(fieldValue.get(3))){
			if(!fieldValue.get(3).matches("^((?:19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")){
				result = resultMessage(index,"生效时间：日期格式不匹配,日期格式必须为yyyy-mm-dd");
				wronginfo=wronginfo.append(result);

			}
			}
			if(null != fieldValue.get(4)&& !"".equals(fieldValue.get(4))){
			if(!fieldValue.get(4).matches("^((?:19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")){
				result = resultMessage(index,"失效时间：日期格式不匹配,日期格式必须为yyyy-mm-dd");
				wronginfo=wronginfo.append(result);

			}
			}
			if(null != fieldValue.get(3)&& !"".equals(fieldValue.get(3)) && null != fieldValue.get(4)&& !"".equals(fieldValue.get(4))){
				if(fieldValue.get(3).matches("^((?:19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$") && fieldValue.get(4).matches("^((?:19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")){			
					if(fieldValue.get(3).compareTo(fieldValue.get(4))>0){
						result = resultMessage(index,"生效时间必须小于失效时间");
						wronginfo=wronginfo.append(result);
					}
				}
			}

			if(null != fieldValue.get(5)&& !"".equals(fieldValue.get(5))){
				if(!fieldValue.get(5).matches("^[1-9][0-9]{0,9}(?:\\.[0-9]{1,3})?$|0\\.[0-9]{1,3}$")){
					result = resultMessage(index,"趟次单价：只能输入大于0-9999999999.999之间的数字");
					wronginfo=wronginfo.append(result);
	
				}else{
					quhYuns.setTangcdj(Double.parseDouble(fieldValue.get(5)));
				}
			}

			quhYuns.setUsercenter(fieldValue.get(0));	
			quhYuns.setChengysdm(fieldValue.get(1));
			quhYuns.setGongysdm(fieldValue.get(2));
			quhYuns.setShengxsj(fieldValue.get(3));
			quhYuns.setShixsj(fieldValue.get(4));
		
			quhYuns.setCreate_time(DateTimeUtil.getAllCurrTime());
			quhYuns.setEdit_time(DateTimeUtil.getAllCurrTime());
			quhYuns.setEditor(editor);
			quhYuns.setCreator(editor);

			
	

			JinjjQuhYuns jinjjQuhYuns=new JinjjQuhYuns();
			jinjjQuhYuns.setJinjjid(quhYuns.getJinjjid());
			jinjjQuhYuns.setUsercenter(quhYuns.getUsercenter());
			jinjjQuhYuns.setGongysdm(quhYuns.getGongysdm());
		//	jinjjQuhYuns.setChengysdm(quhYuns.getChengysdm());  一个用户中心+供应商+生效时间只能有一个承运商。
			List<JinjjQuhYuns> oldbeans=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_jinjjquhyuns.getJinjjQuhYuns",jinjjQuhYuns);
			for (JinjjQuhYuns jinjjQuhYuns2 : oldbeans) {
				if(jinjjQuhYuns2!=null ){
					if((jinjjQuhYuns2.getShengxsj().compareTo(quhYuns.getShengxsj())<=0 && jinjjQuhYuns2.getShixsj().compareTo(quhYuns.getShengxsj())>=0 ) 
					|| (jinjjQuhYuns2.getShengxsj().compareTo(quhYuns.getShixsj())<=0   && jinjjQuhYuns2.getShixsj().compareTo(quhYuns.getShixsj())>=0 )
					|| (jinjjQuhYuns2.getShengxsj().compareTo(quhYuns.getShengxsj())<=0 && jinjjQuhYuns2.getShixsj().compareTo(quhYuns.getShixsj())>=0 )
					|| (jinjjQuhYuns2.getShengxsj().compareTo(quhYuns.getShengxsj())>=0 && jinjjQuhYuns2.getShixsj().compareTo(quhYuns.getShixsj())<=0) 
					){
					if(jinjjQuhYuns2.getShengxsj().compareTo(quhYuns.getShengxsj())==0 && jinjjQuhYuns2.getShixsj().compareTo(quhYuns.getShixsj())==0){
							continue;
					}else{
						result = resultMessage(index,"生效时间重叠");
						wronginfo=wronginfo.append(result);
						break;
					}
					}
				}
			}		
			//excel存在时间重叠
			for (int j = 0; j < datas.size(); j++) {
				if(j+3!=index && datas.get(j).getUsercenter()!=null && datas.get(j).getShengxsj()!=null && datas.get(j).getShixsj()!=null){
					if(datas.get(j).getGongysdm()!=null && datas.get(j).getChengysdm()!=null){
					if( datas.get(j).getUsercenter().equalsIgnoreCase(quhYuns.getUsercenter()) 
							&& (datas.get(j).getGongysdm().equals(quhYuns.getGongysdm()) || datas.get(j).getGongysdm()==quhYuns.getGongysdm()) 
							&& (datas.get(j).getChengysdm().equals(quhYuns.getChengysdm()) || datas.get(j).getChengysdm()==quhYuns.getChengysdm()) ){
						if((datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())<=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShengxsj())>=0 ) 
						|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShixsj())<=0   && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())>=0 )
						|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())<=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())>=0 )
						|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())>=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())<=0) 
						){
								result = resultMessage(index,"生效时间与第"+(j+3)+"行重叠");
								wronginfo=wronginfo.append(result);
								break;
						}
				     }
					}
					else if(datas.get(j).getGongysdm()==null && datas.get(j).getChengysdm()!=null){
						if( datas.get(j).getUsercenter().equalsIgnoreCase(quhYuns.getUsercenter()) 
								&& datas.get(j).getGongysdm()==quhYuns.getGongysdm()
								&& datas.get(j).getChengysdm().equals(quhYuns.getChengysdm())  ){
							if((datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())<=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShengxsj())>=0 ) 
							|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShixsj())<=0   && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())>=0 )
							|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())<=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())>=0 )
							|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())>=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())<=0) 
							){
									result = resultMessage(index,"生效时间与第"+(j+3)+"行重叠");
									wronginfo=wronginfo.append(result);
									break;
							}
					}
				
						}	else if(datas.get(j).getGongysdm()!=null && datas.get(j).getChengysdm()==null){
							if( datas.get(j).getUsercenter().equalsIgnoreCase(quhYuns.getUsercenter()) 
									&&  datas.get(j).getGongysdm().equals(quhYuns.getGongysdm())
									&&  datas.get(j).getChengysdm()==quhYuns.getChengysdm() ){
								if((datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())<=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShengxsj())>=0 ) 
								|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShixsj())<=0   && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())>=0 )
								|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())<=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())>=0 )
								|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())>=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())<=0) 
								){
										result = resultMessage(index,"生效时间与第"+(j+3)+"行重叠");
										wronginfo=wronginfo.append(result);
										break;
								}
						}
					
					}else if(datas.get(j).getGongysdm()==null && datas.get(j).getChengysdm()==null){
						if( datas.get(j).getUsercenter().equalsIgnoreCase(quhYuns.getUsercenter()) 
								&&  datas.get(j).getGongysdm()==quhYuns.getGongysdm()
								&&  datas.get(j).getChengysdm()==quhYuns.getChengysdm() ){
							if((datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())<=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShengxsj())>=0 ) 
							|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShixsj())<=0   && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())>=0 )
							|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())<=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())>=0 )
							|| (datas.get(j).getShengxsj().compareTo(quhYuns.getShengxsj())>=0 && datas.get(j).getShixsj().compareTo(quhYuns.getShixsj())<=0) 
							){
									result = resultMessage(index,"生效时间与第"+(j+3)+"行重叠");
									wronginfo=wronginfo.append(result);
									break;
							}
					}
				
				}
				}
			  
			}
			index ++;
			//如果有错误信息将会返回
			if(!wronginfo.toString().equals("")){
				wronginfo.append("\n");
				wrongcoun=wrongcoun+1;
				wrongdatas.add(quhYuns);
				errorBuffer.append(wronginfo);
				wronginfo= new StringBuffer("");
				//return wronginfo.toString();
			}
			
			datasnew.add(quhYuns);
			
			if(wrongdatas.size()>0 && wrongcoun==5){
				break;
			}

		}
		  //如果没有错误信息，正常操作，如果有则需要返回						
		if(wrongdatas.size()<=0){
			return sqlInsertOrUpdate(datasnew);
		}else{
			return errorBuffer.toString();
		}
	
	}finally{
		//关闭数据库连接
		closeConnection();
	}


			//对主键进行校验 只需要验证该数据存不存在且有效
//			if(null != bean.getLingjbh()&& !"".equals(bean.getLingjbh())){
//				if(!bean.getLingjbh().matches("^[A-Za-z0-9_]{10}$")){
//			    	bean.setLingjbh("");
//					result = resultMessage(index,"零件编号：必须为10位只能由数字、字母以及下划线组成");
//					wronginfo=wronginfo.append(result);
//			     }
//			}
			

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
  
	/**
	 * 插入数据到表
	 */
	public String sqlInsertOrUpdate(ArrayList<JinjjQuhYuns> datasnew){
		
		try {
			for (JinjjQuhYuns bean : datasnew) {		
				JinjjQuhYuns jinjjQuhYuns=(JinjjQuhYuns) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).selectObject("ts_jinjjquhyuns.queryJinjjQuhYunsByyouxj",bean);
				if(jinjjQuhYuns!=null){
					//更新数据
					bean.setBiaos("1");
					bean.setEdit_time(DateTimeUtil.getAllCurrTime());
					bean.setEditor(editor);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_jinjjquhyuns.updateJinjjQuhYunsDownload",bean);//增加数据库	
				}else{
					bean.setBiaos("1");
					//插入数据
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_jinjjquhyuns.insertJinjjQuhYuns",bean);//增加数据库
				}
			}
		} catch (DataAccessException e) {
			logger.info(e.getMessage());
			return "插入失败";
		}
		return "";
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
