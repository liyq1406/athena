package com.athena.ckx.util.xls.ckxouterpath;

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

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.carry.CkxWaibwl;
import com.athena.ckx.entity.carry.CkxWaibwlxx;
import com.athena.ckx.entity.xuqjs.CkxJiaofrl;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;


import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 按照XML文件处理
 * @author chenlei
 * @vesion 1.0
 * @date 2012-7-26
 */

public class XmlSheetHandlerouterpath extends SheetHandlerouterpath {
	private AbstractIBatisDao baseDao;
	private static Properties prop;
	private String editor;
	private Map<String,String> cmap;
	protected static Logger logger = Logger.getLogger(XmlSheetHandlerouterpath.class);	//定义日志方法
	public XmlSheetHandlerouterpath(Document document, String sheet, String table,
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
		int coluNum = getCellNumber(sheetElment);
		ArrayList<String> fieldList = new ArrayList<String>(); // 字段名 集合
		ArrayList<String> fieldValue = new ArrayList<String>(); // 一列的值
		ArrayList<String> fieldErrorValue = new ArrayList<String>(); // 一列的值 为错误使用
		//String insertSql = null; // 新增sql语句
		//String updateSql = null; // 修改sql语句
		//String selectSql = null; // 查找sql语句
		List<CkxWaibwl> datas = new ArrayList<CkxWaibwl>();
		StringBuffer errorBuffer = new StringBuffer(); //错误信息
		StringBuffer wronginfo=new StringBuffer("");
		int num = 0;
		int index = 3;
		List<Element> list = sheetElment.selectNodes("xmlns:Table/xmlns:Row");
		for (int i = 2; i < list.size(); i++) {
			CkxWaibwl wbwl = new CkxWaibwl();
			Element rowElement =  (Element) list.get(i);
			fieldValue=getCellValue(rowElement,fieldList.size());
			for (int j = 0; j < coluNum; j++) {
				if( j == 0)
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						wbwl.setUsercenter(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 1 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						wbwl.setGongysbh(fieldValue.get(j).toUpperCase());
					}
				}else if( j == 2  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						wbwl.setMudd(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 3 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						wbwl.setBeiz(fieldValue.get(j).toUpperCase() );
					}
				}else if( j == 4  && j< fieldValue.size())
				{
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						wbwl.setBeiz1(fieldValue.get(j).toUpperCase() );
					}
				}else if(j == 5 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						wbwl.setJiaofm(fieldValue.get(j).toUpperCase());
					}
				}else if(j == 6 && j< fieldValue.size()){
					if(null!=fieldValue.get(j)&& !" ".equals(fieldValue.get(j))){
						wbwl.setLujbh(fieldValue.get(j).toUpperCase());
					}
				}

			}
			datas.add(wbwl);
		}
		
		//校验有误的条数
		int wrongcoun=0;
		
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<CkxWaibwl> wrongdatas =  new ArrayList<CkxWaibwl>();
		//用一个新集合接收读取的数据，并且把错误信息放进去
		ArrayList<CkxWaibwl> datasnew =  new ArrayList<CkxWaibwl>();
		//校验每个字段
		for (int i=0;i<datas.size();i++) {
			CkxWaibwl bean = datas.get(i);
			//对主键进行校验 只需要验证该数据存不存在且有效
			if(null==bean.getUsercenter() || StringUtils.isBlank(bean.getUsercenter())){
				if(null==bean.getUsercenter()){
					bean.setUsercenter("");
				}
				result = resultMessage(index,"用户中心："+bean.getUsercenter()+"  必填并且长度必须为2位");
				wronginfo=wronginfo.append(result);
			}else if(!bean.getUsercenter().matches("^[A-Z]{2}$")){
				bean.setUsercenter("");
				result = resultMessage(index,"用户中心：必须为2位只能由字母以组成");
				wronginfo=wronginfo.append(result);
			}
			if(null==bean.getGongysbh() || StringUtils.isBlank(bean.getGongysbh())){
				if(null==bean.getGongysbh()){
					bean.setGongysbh("");
				}
				result = resultMessage(index,"供应商编号："+bean.getGongysbh()+" 必填并且长度必须为10位");
				wronginfo=wronginfo.append(result);
			}else {
			    String newchengysbh=((bean.getGongysbh()).trim()).replace(" ", "1");
			    if(!newchengysbh.matches("^[A-Za-z0-9]{10}$")){
			    	bean.setGongysbh("");
					result = resultMessage(index,"供应商编号：必须为10位只能由数字、字母、空格以组成");
					wronginfo=wronginfo.append(result);
			     }else{
			    	 Gongcy gongys=new Gongcy();
	    			 gongys.setUsercenter(bean.getUsercenter());
	    			 gongys.setGcbh(bean.getGongysbh());
	    				gongys.setLeix("3");//类型不等于承运商
	    				gongys.setBiaos("1");
	    				if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountGongys", gongys)){
	    					result = resultMessage(index,"供应商编码不存在！");
	    					wronginfo=wronginfo.append(result);
	    				}else{
	    					 String fayd = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.selectFayd", gongys);
	 		                 if(null ==fayd || StringUtils.isBlank(fayd)){
	 		                	result = resultMessage(index,"供应商编号："+bean.getGongysbh()+"无法带出发货地");
	 		    				wronginfo=wronginfo.append(result);	
	 		                 }else{
	 		                	bean.setFahd(fayd);
	 		                 }
	    				}
			     }
			}
			if(null==bean.getMudd() || StringUtils.isBlank(bean.getMudd())){
				if(null==bean.getMudd()){
					bean.setMudd("");
				}
				result = resultMessage(index,"目的地："+bean.getMudd()+" 必填并且长度必须为3位");
				wronginfo=wronginfo.append(result);
			}else if(!bean.getMudd().matches("^[A-Za-z0-9]{3}$")){
				bean.setMudd("");
				result = resultMessage(index,"目的地：必须为3位只能由数字、字母以组成");
				wronginfo=wronginfo.append(result);
			}else{
				//通过目的地带出卸货站台编组
				Cangk ck=new Cangk();
				ck.setUsercenter(bean.getUsercenter());
				ck.setCangkbh(bean.getMudd());
				ck.setBiaos("1");
				Cangk cangk = (Cangk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCangk",ck);
				
                if(null ==cangk){
                	result = resultMessage(index,"目的地："+bean.getMudd()+"不存在");
    				wronginfo=wronginfo.append(result);	
                }else{
                	bean.setXiehztbz(cangk.getXiehztbz());
                }
			}
			if(null==bean.getBeiz() || StringUtils.isBlank(bean.getBeiz())){
				bean.setPanysj(0.0);
				result = resultMessage(index,"发货周期："+bean.getPanysj()+"  必填");
				wronginfo=wronginfo.append(result);
			}else if(!bean.getBeiz().matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
				bean.setPanysj(0.0);
				result = resultMessage(index,"发货周期：长度必须为3位正整数或2位小数");
				wronginfo=wronginfo.append(result);
			}else{
				bean.setPanysj(Double.valueOf(bean.getBeiz()));
			}
			if(null==bean.getBeiz1() || StringUtils.isBlank(bean.getBeiz1())){
				bean.setBeihzq(0.0);
				result = resultMessage(index,"备货周期："+bean.getBeihzq()+"  必填");
				wronginfo=wronginfo.append(result);
			}else if(!bean.getBeiz1().matches("^[0-9]{1,3}$|^[0-9]{1,3}[.][0-9]{1,2}$")){
				bean.setBeihzq(0.0);
				result = resultMessage(index,"备货周期：长度必须为3位正整数或2位小数");
				wronginfo=wronginfo.append(result);
			}else{
				bean.setBeihzq(Double.valueOf(bean.getBeiz1()));
			}
			if(null==bean.getJiaofm() || StringUtils.isBlank(bean.getJiaofm())){
				bean.setJiaofm("");
				result = resultMessage(index,"交付码："+bean.getBeihzq()+"  必填");
				wronginfo=wronginfo.append(result);
			}else if(!bean.getJiaofm().matches("^[A-Za-z0-9]{3}$")){
				bean.setJiaofm("");
				result = resultMessage(index,"交付码：必须为3位只能由数字、字母以组成");
				wronginfo=wronginfo.append(result);
			}else{
				//验证交付码（从交付日历中获取）   
				CkxJiaofrl jfm=new CkxJiaofrl();
				jfm.setUsercenter(bean.getUsercenter());
				jfm.setJiaofm(bean.getJiaofm());
				List<CkxJiaofrl> jflist= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxJiaofrl",jfm);
				if(jflist==null||jflist.size()==0){
					//\n请确认交付日历中有此交付码！
					result = resultMessage(index,"交付码："+bean.getJiaofm()+"不存在！请重新输入");
					wronginfo=wronginfo.append(result);
				}
			}
			if(null==bean.getLujbh() || StringUtils.isBlank(bean.getLujbh())){
				if(null==bean.getLujbh()){
					bean.setLujbh("");
				}
				result = resultMessage(index,"路径编号："+bean.getLujbh()+" 必填并且长度必须为10位");
				wronginfo=wronginfo.append(result);
			}else if(!bean.getLujbh().matches("^[A-Za-z0-9]{10}$")){
				bean.setLujbh("");
				result = resultMessage(index,"路径编号：必须为10位只能由数字、字母以组成");
				wronginfo=wronginfo.append(result);
			}else{
				//通过路径编号带出承运商信息
				CkxWaibwlxx wlxx=(CkxWaibwlxx) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("carry.getMaxXuh", bean);
                if(null ==wlxx){
                	result = resultMessage(index,"路径编号："+bean.getLujbh()+"不存在");
    				wronginfo=wronginfo.append(result);	
                }else{
                	bean.setChengysbh(wlxx.getGcbh());	
                }
				
			}
			index ++;
			bean.setCreator(editor);
			bean.setCreateTime(DateTimeUtil.getAllCurrTime());
			bean.setEditor(editor);
			bean.setEditTime(DateTimeUtil.getAllCurrTime());
			for(int n=0;n<datasnew.size();n++){
				CkxWaibwl w=datasnew.get(n);
				if(w.getUsercenter().equals(bean.getUsercenter()) && w.getGongysbh().equals(bean.getGongysbh()) 
                    && w.getFahd().equals(bean.getFahd()) && w.getMudd().equals(bean.getMudd())){
					result = resultMessage(index,"导入的外部物流数据重复，请确认！");
					wronginfo=wronginfo.append(result);
				}
			}
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
			errorBuffer=new StringBuffer("");
		//查询外部物流信息
		List<CkxWaibwl> wbwllist=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("carry.getCkxWaibwl");
		Map<String,String> map=new HashMap<String,String>();
		for (CkxWaibwl wl : wbwllist) {
			map.put(wl.getUsercenter()+wl.getGongysbh()+wl.getFahd()+wl.getMudd(),wl.getUsercenter()+wl.getGongysbh()+wl.getFahd()+wl.getMudd());
	    }
		//判断外部物流是否重复
		index=3;
		for (int i=0;i<datasnew.size();i++) {
			CkxWaibwl bean= datas.get(i);
			if(null != map.get(bean.getUsercenter()+bean.getGongysbh()+bean.getFahd()+bean.getMudd())){
				result = resultMessage(index,"用户中心："+bean.getUsercenter()+"供应商编号："+bean.getGongysbh()+"目的地："+bean.getMudd()+"  的数据已存在");
				wronginfo=wronginfo.append(result);
			}
			
			index++;
			if(!wronginfo.toString().equals("")){
				wronginfo.append("\n");
				wrongcoun=wrongcoun+1;
				wrongdatas.add(bean);
				errorBuffer.append(wronginfo);
				wronginfo= new StringBuffer("");
			}
			if(wrongdatas.size()>0 && wrongcoun==5){
				break;
			}
		}
		}
		
		
			if(wrongdatas.size()<=0){
				return sqlInsertOrUpdate(datasnew,editor);
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
  
	public  String sqlInsertOrUpdate(ArrayList<CkxWaibwl> datasnew,String editor){
		String message = "导入成功！";
		int count = 0;
		count =insertCkxWaibwl(datasnew);
		if (count == 0) {
			message ="外部物流导入失败！";
		}
		return message;
	}
	
	@Transactional
	public Integer insertCkxWaibwl(ArrayList<CkxWaibwl> datasnew){
		int count =0;
		for(CkxWaibwl wl:datasnew){
		  //根据承运商和卸货站台编组 更新所有相同的备货周期和发运周期  xh  0728
		  //-----> mantis:0012759 hanwu 20160427 start	由于业务变更，不再更新备货周期和运输周期
		  //baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).execute("carry.updateCkxWaibwlByXiehztbz",wl);
		  //-----> mantis:0012759 hanwu 20160427 end
		  count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CK).execute("carry.insertCkxWaibwl",wl);
		}
		return count;
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
