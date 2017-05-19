package com.athena.xqjs.module.utils.xls.lingxjyfgl;

import java.io.IOException;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheValue;
import com.athena.xqjs.entity.common.Xitcsdy;
import com.athena.xqjs.entity.quhyuns.Rukmx;
import com.athena.xqjs.entity.quhyuns.Yunsfyhz;
import com.athena.xqjs.util.GetPostOnly;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


/**
 * 按照XML文件处理
 * @author lc
 * @vesion 1.0
 * @date 2016-11-30
 */
public class XmlSheetHandlerlxjyfgl extends SheetHandlerlxjyfgl {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> cmap;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerlxjyfgl.class);	//定义日志方法
	public XmlSheetHandlerlxjyfgl(Document document, String sheet, String table,
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
			List<Rukmx> datas = new ArrayList<Rukmx>();
			StringBuffer errorBuffer = new StringBuffer(); //错误信息
			StringBuffer wronginfo=new StringBuffer("");
			int num = 0;
			int index = 3;
			List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
			for (int i = 2; i < list.size(); i++) {
				Rukmx rukmx = new Rukmx();
				Element rowElement =  (Element) list.get(i);
				fieldValue=getCellValue(rowElement,fieldList.size());
				for (int j = 0; j < coluNum; j++) {
					if( j == 0)
					{
						if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
							rukmx.setUsercenter(fieldValue.get(j).toUpperCase());
						}
					}else if(j == 1 && j< fieldValue.size()){
						if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
							rukmx.setChengysdm(fieldValue.get(j).toUpperCase());
						}
					}else if( j == 2  && j< fieldValue.size()){
						if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
							rukmx.setGongysdm(fieldValue.get(j).toUpperCase());
						}
					}else if(j == 3 && j< fieldValue.size()){
						if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
							rukmx.setLingjbh(fieldValue.get(j).toUpperCase());
						}
					}else if( j == 4  && j< fieldValue.size()){
						if(null==fieldValue.get(j) || StringUtils.isBlank(fieldValue.get(j))){
							rukmx.setXinlxfy(0.0);
						}else if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))&& (fieldValue).get(j).matches("^[1-9][0-9]{0,6}(?:[.][0-9]{1,6})?$|0[.][0-9]{1,6}$|^0$")){
							rukmx.setXinlxfy(Double.valueOf((fieldValue.get(j))));
						}else{
							rukmx.setXinlxfy(-1.0);
						}
					}else if(j == 5 && j< fieldValue.size()){
						if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
							rukmx.setRuksj(fieldValue.get(j).toUpperCase());
						}
					}else if(j == 6 && j< fieldValue.size()){
						if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
							rukmx.setLxjlb(fieldValue.get(j).toUpperCase());
						}
					}
				}
				datas.add(rukmx);
			}
			
			//校验有误的条数
			int wrongcoun=0;
			
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<Rukmx> wrongdatas =  new ArrayList<Rukmx>();
			//用一个新集合接收读取的数据，并且把错误信息放进去
			ArrayList<Rukmx> datasnew =  new ArrayList<Rukmx>();
			//校验每个字段
			for (int i=0;i<datas.size();i++) {
				Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Rukmx bean = datas.get(i);
				//对主键进行校验 只需要验证该数据存不存在且有效
				if(null==bean.getUsercenter() || StringUtils.isBlank(bean.getUsercenter())){
					if(null==bean.getUsercenter()){
						bean.setUsercenter("");
					}
					result = resultMessage(index,"用户中心："+bean.getUsercenter()+" 必填并且长度必须为2位");
					wronginfo=wronginfo.append(result);
				}else if(!bean.getUsercenter().matches("^[A-Z]{2}$")){
					bean.setUsercenter("");
					result = resultMessage(index,"用户中心：必须为2位只能由字母组成");
					wronginfo=wronginfo.append(result);
				}else{
					//用户中心权限校验
					if((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")!=null && ((List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY")).size()>0){
						List<String> uclist=(List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY");
						if(!uclist.contains(bean.getUsercenter())){							
							result = resultMessage(index,"用户中心：您没有权限操作用户中心为"+bean.getUsercenter()+"的数据");
							bean.setUsercenter("");
							wronginfo=wronginfo.append(result);
						}
					}
				}

				if(null==bean.getChengysdm() || StringUtils.isBlank(bean.getChengysdm())){
					if(null==bean.getChengysdm()){
						bean.setChengysdm("");
					}
					result = resultMessage(index,"承运商代码："+bean.getChengysdm()+" 必填并且长度必须为10位");
					wronginfo=wronginfo.append(result);
				}else{
					String newchengysdm=((bean.getChengysdm()).trim()).replace(" ", "1");
					if(!newchengysdm.matches("^[A-Za-z0-9]{10}$")){
						bean.setChengysdm("");
						result = resultMessage(index,"承运商代码：必须为10位只能由数字、字母、空格组成");
						wronginfo=wronginfo.append(result);
					}else if(StringUtils.isNotBlank(bean.getUsercenter())){
						Rukmx rukmx=new Rukmx();
						rukmx.setUsercenter(bean.getUsercenter());
						rukmx.setGongysdm(bean.getChengysdm());
						rukmx.setBiaos("1");
						Object obj1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.getCountGongys", rukmx);
						if (Integer.valueOf(obj1.toString())==0) {
							result = resultMessage(index,"承运商代码:"+bean.getChengysdm()+"在参考系供应商不存在！");
							wronginfo=wronginfo.append(result);
						}
					}
				}
			
				if(bean.getXinlxfy() == -1){
					result = resultMessage(index,"新零星费用：只能输入0-9999999.999999之间的数字");
					wronginfo=wronginfo.append(result);
				}
				if(null==bean.getRuksj() || StringUtils.isBlank(bean.getRuksj())){
					if(null==bean.getRuksj()){
						bean.setRuksj("");
					}
					result = resultMessage(index,"入库时间："+bean.getRuksj()+" 必填并且格式必须为 YYYY-MM-DD");
					wronginfo=wronginfo.append(result);
				}else if(!bean.getRuksj().matches("^[0-9]{1,4}[-][0-9]{1,2}[-][0-9]{1,2}$")){
					bean.setRuksj("");
					result = resultMessage(index,"入库时间：格式必须为 YYYY-MM-DD");
					wronginfo=wronginfo.append(result);
				}else{
					if(bean.getRuksj().compareTo(formatter.format(new Date()))>0){
						bean.setRuksj("");
						result = resultMessage(index,"入库时间：不能大于系统当前时间");
						wronginfo=wronginfo.append(result);
					}else{
						Yunsfyhz yunsfyhz=new Yunsfyhz();
						yunsfyhz.setUsercenter(bean.getUsercenter());
						yunsfyhz.setDanjlx("1");
						yunsfyhz.setShenhkssj(bean.getRuksj());
						yunsfyhz.setBiaos("0");//不为失效的单据
						Object obj4= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.checkRukrq", yunsfyhz);
						if(Integer.valueOf(obj4.toString())>0){
							result = resultMessage(index,"入库日期:"+bean.getRuksj()+"已在审核时间段内,不允许新增");
							wronginfo=wronginfo.append(result);
						}
					}
				}
				if(null==bean.getLxjlb() || StringUtils.isBlank(bean.getLxjlb())){
					if(null==bean.getLxjlb()){
						bean.setLxjlb("");
					}
					result = resultMessage(index,"零星件类别："+bean.getLxjlb()+" 必填并且需在系统参数定义表存在");
					wronginfo=wronginfo.append(result);
				}else{
					Xitcsdy xitcsdy=new Xitcsdy();
					xitcsdy.setZidlx("LXJLB");
					xitcsdy.setZidbm(bean.getLxjlb());
					CacheValue cacheValue=(CacheValue) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.queryPostLxjlb", xitcsdy);
					if (cacheValue==null) {
						result = resultMessage(index,"零星件类别：系统参数定义表中不存在零星件类别："+bean.getLxjlb()+"的数据或数据已失效");
						wronginfo=wronginfo.append(result);
					}else if(cacheValue.getKey().equalsIgnoreCase("LINGXING")){
						if(null==bean.getGongysdm() || StringUtils.isBlank(bean.getGongysdm())){
							if(null==bean.getGongysdm()){
								bean.setGongysdm("");
							}
							result = resultMessage(index,"供应商代码："+bean.getGongysdm()+" 必填并且长度必须为10位");
							wronginfo=wronginfo.append(result);
						}
						
						if(null==bean.getLingjbh() || StringUtils.isBlank(bean.getLingjbh())){
							if(null==bean.getLingjbh()){
								bean.setLingjbh("");
							}
							result = resultMessage(index,"零件编号："+bean.getLingjbh()+" 必填并且长度必须为10位");
							wronginfo=wronginfo.append(result);
						}
						
			
					}
				}
				
				//校验单价参考系,应放在检验供应商代码和零件编号前面，以免检验供应商代码和零件编号时为空把值设为"DEFAULT"
			/*	if(  StringUtils.isNotBlank(bean.getUsercenter()) && StringUtils.isNotBlank(bean.getChengysdm()) && StringUtils.isNotBlank(bean.getRuksj())){
					Object wullj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.checkWullj", bean);
					if (Integer.valueOf(wullj.toString())==0) {
						result = resultMessage(index,"取货运输单价参考系无对应承运商信息");
						wronginfo=wronginfo.append(result);
					}
				}*/     	//  0013569 只用校验供应商、承运商存在性即可，不校验单价参考系有没有  王朋
		
				
				if(null==bean.getGongysdm() || StringUtils.isBlank(bean.getGongysdm())){
					if(null==bean.getGongysdm()){
						bean.setGongysdm("DEFAULT");
					}
				//	result = resultMessage(index,"供应商代码："+bean.getGongysdm()+" 必填并且长度必须为10位");
				//	wronginfo=wronginfo.append(result);
				}else{
					String newgongysdm=((bean.getGongysdm()).trim()).replace(" ", "1");
					if(!newgongysdm.matches("^[A-Za-z0-9]{10}$")){
						bean.setGongysdm("");
						result = resultMessage(index,"供应商代码：必须为10位只能由数字、字母、空格组成");
						wronginfo=wronginfo.append(result);
					}else if(StringUtils.isNotBlank(bean.getUsercenter())){
						Rukmx rukmx=new Rukmx();
						rukmx.setUsercenter(bean.getUsercenter());
						rukmx.setGongysdm(bean.getGongysdm());
						rukmx.setBiaos("1");
						rukmx.setLingjlx("3");//类型不等于承运商
						Object obj1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.getCountGongys", rukmx);
						if (Integer.valueOf(obj1.toString())==0) {
							result = resultMessage(index,"供应商代码："+rukmx.getGongysdm()+"在参考系供应商不存在！");
							wronginfo=wronginfo.append(result);
						}
					}
				}
				
				if(null==bean.getLingjbh() || StringUtils.isBlank(bean.getLingjbh())){
					if(null==bean.getLingjbh()){
						bean.setLingjbh("DEFAULT");
					}
				//	result = resultMessage(index,"零件编号："+bean.getLingjbh()+" 必填并且长度必须为10位");
				//	wronginfo=wronginfo.append(result);
				}else if(!bean.getLingjbh().matches("^[A-Za-z0-9]{10}$")){
					bean.setLingjbh("");
					result = resultMessage(index,"零件编号：必须为10位只能由数字、字母以组成");
					wronginfo=wronginfo.append(result);
				}else if(StringUtils.isNotBlank(bean.getUsercenter())){
					Rukmx rukmx=new Rukmx();
					rukmx.setUsercenter(bean.getUsercenter());
					rukmx.setLingjbh(bean.getLingjbh());
					rukmx.setBiaos("1");
					Object obj1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_quhysfy.getCountLingj", rukmx);
					if (Integer.valueOf(obj1.toString())==0) {
						result = resultMessage(index,"零件编号：零件表中不存在零件编号："+bean.getLingjbh()+"的数据或数据已失效");
						wronginfo=wronginfo.append(result);
					}
				}
				
				

				index ++;
				bean.setBiaos("1");
				bean.setCreator(editor);
				bean.setCreate_time(formatter.format(new Date()));
				bean.setEditor(editor);
				bean.setEdit_time(formatter.format(new Date()));
				//如果有错误信息将会返回
				if(!wronginfo.toString().equals("")){
					wronginfo.append("\n");
					wrongcoun=wrongcoun+1;
					wrongdatas.add(bean);
					errorBuffer.append(wronginfo);
					wronginfo= new StringBuffer("");
					//return wronginfo.toString();
				}
				
				datasnew.add(bean);
				
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
	public String sqlInsertOrUpdate(ArrayList<Rukmx> datasnew){
		try {
			for (Rukmx rk : datasnew) {
				
				//判断导入的数据是新增还是更新后再来做操作	
				Object obj=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).selectObject("ts_quhysfy.queryLxj",rk);
				if (Integer.valueOf(obj.toString())==0) {//如果不存在则插入		
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).execute("ts_quhysfy.insertRukmxLingxj",rk);
				}else{//否则更新						
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).execute("ts_quhysfy.updateRukmxLingxj",rk);
				}																				
			}
		} catch (DataAccessException e) {
			logger.info(e.getMessage());
			return "插入或更新失败";
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
